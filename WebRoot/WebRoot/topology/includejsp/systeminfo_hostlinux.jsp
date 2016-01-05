 <%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.manage.PollMonitorManager"%>
<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.detail.service.memoryInfo.MemoryInfoService"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.temp.dao.*"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%   String runmodel = PollingEngine.getCollectwebflag(); 
	String rootPath = request.getParameter("rootPath");  
	String tmp = request.getParameter("tmp")==null?"":request.getParameter("tmp"); 
	String hostname =request.getParameter("hostname"); 
    String sysname =request.getParameter("sysname"); 
    String processornum =request.getParameter("processornum");  
    String sysuptime =request.getParameter("sysuptime"); 
    String collecttime =request.getParameter("collecttime"); 
    String mac =request.getParameter("mac");   
    String CSDVersion =request.getParameter("CSDVersion");  
    String picip =request.getParameter("picip");  
     
  	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
 	 SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "("+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
		
	//响应时间 
	
	String responsevalue = "0";
	String avgresponse = "0";
	String maxresponse = "0"; 
	I_HostCollectData hostmanager = new HostCollectDataManager();
	Hashtable ConnectUtilizationhash = new Hashtable();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time = sdf.format(new Date());
	String starttime = time + " 00:00:00";
	String totime = time + " 23:59:59";
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ResponseTime", starttime,totime);
	} catch (Exception ex) {
		ex.printStackTrace();
	} 
	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("毫秒", "").replaceAll("%", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		maxresponse = maxresponse.replaceAll("%", "");
	} 
	
	//ping连通率
	String pingavg="0";  
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime,totime);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingavg = pingavg.replace("%", "");
		pingavg=String.valueOf(Math.round(Float.parseFloat(pingavg))); 
	}
	
	
	//内存利用率
	Vector memoryVector = new Vector(); 
	String memoryvalue = "0";
	Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
	//wxy edit
	if(ipAllData!=null)
	 memoryVector = (Vector) ipAllData.get("memory");	
	 //wxy end
	double usememoryvalue=0;	
	double allmemoryvalue = 1;	 
	try {
		if (memoryVector != null && memoryVector.size() > 0) {
			for (int i = 0; i < memoryVector.size(); i++) {
				Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
				if(memorycollectdata.getSubentity().equals("PhysicalMemory"))
				{
					if(memorycollectdata.getEntity().equals("UsedSize")){ 
				 		usememoryvalue+=Double.parseDouble(memorycollectdata.getThevalue());
				 	} 
					if(memorycollectdata.getEntity().equals("Capability")){
					 	allmemoryvalue+=Double.parseDouble(memorycollectdata.getThevalue());
					}
				} 
			}
			if(allmemoryvalue>1){
					double d= usememoryvalue/(allmemoryvalue-1)*100; 
					memoryvalue= String.format("%.0f",d); 
				}
				else{
					memoryvalue=0+"";
				} 
		} 
	}catch(Exception ex){
		ex.printStackTrace();
	}
	Hashtable memhash=new Hashtable();
	Hashtable diskhash=new Hashtable();
	//模式
    	I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
    	if(runmodel.equals("0")){
		
	     try{
			memhash = hostlastmanager.getMemory_share(host.getIpAddress(),"Memory",starttime,totime);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			diskhash = hostlastmanager.getDisk_share(host.getIpAddress(),"Disk",starttime,totime);
		}catch(Exception e){
			e.printStackTrace();
		}	
		}else{
		
		try {
			memhash = hostlastmanager.getMemory(host.getIpAddress(),
					"Memory", starttime, totime);
			diskhash = hostlastmanager.getDisk(host.getIpAddress(),
					"Disk", starttime, totime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
       //内存最大、平均利用率 amcharts wxy add
		CreateAmColumnPic aixColumnPic=new CreateAmColumnPic();
	    String temp=aixColumnPic.createAmMemoryChart(host.getIpAddress(),memhash);
 		
	   //最近一周CPU平均利用率 amcharts wxy add
	   String cpuData=aixColumnPic.createCpuChartLastWeek(host.getIpAddress());
	
	   //磁盘利用率 amcharts wxy add
		String valueStr=aixColumnPic.createDiskChartTop5(diskhash);	
 %>	
<table id="detail-content" class="detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="设备详细信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body">
				<tr>
					<td>
						<table cellpadding=0 rules=none width=100% align=center algin="center">
						 	<tr>
								<td width="50%" align="left" valign="top">
									<table>
										<tr>
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;主机名:</td>
											<td width="70%"><%=hostname%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>	
											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>">&nbsp;<%=NodeHelper.getStatusDescr(host.getStatus())%></td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;系统名称:</td>
											<td width="70%"><%=sysname%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left"nowrap>&nbsp;版本:</td>
											<!--<td style="word-break: break-all;"> -->
											<td>
												<jsp:include page="/topology/includejsp/td_acronym.jsp">
													<jsp:param name="wholeStr" value="<%= CSDVersion %>"/>
												</jsp:include> 
											</td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;类别:</td>
											<td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;类型:</td>
											<td width="70%"><%=host.getType()%></td>
										</tr>
										<tr>
											<td width="30%" height="26" nowrap class=txtGlobal>&nbsp;CPU个数:</td>
											<td width="70%"><%=processornum%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal  nowrap> &nbsp;设备启动时间:</td>
											<td><%=sysuptime%></td>
										</tr>
										<tr>
											<td width="30%" height="26" nowrap class=txtGlobal>&nbsp;数据采集时间:</td>
											<td width="70%"><%=collecttime%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal nowrap>&nbsp;IP地址:</td>
											<%
												List iplist = null;
												IpAliasDao ipdao = new IpAliasDao();
												try {
													iplist = ipdao.loadByIpaddress(host.getIpAddress());
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													ipdao.close();
												}
	
												IpAlias ipalias = null;
												ipdao = new IpAliasDao();
												try {
													ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(), "1");
	
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													ipdao.close();
												}
	
												if (iplist == null)
													iplist = new ArrayList();
											%>
											<TD nowrap width="40%">
												<select name="ipalias<%=host.getIpAddress()%>">
													<option selected><%=host.getIpAddress()%></option>
													<%
														for (int j = 0; j < iplist.size(); j++) {
															IpAlias voTemp = (IpAlias) iplist.get(j);
													%>
															<option
															<%
																if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ 
															%>
																	selected 
															<%
																} 
															%>		
																>
															<%=voTemp.getAliasip()%>
															</option>
													<%
														}
													%>
												</select>
												[<a href="#" style="cursor: hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">修改</a>]
											</TD>
										</tr>
										<tr>
											<td height="29" class=txtGlobal valign=center nowrap> &nbsp;MAC地址:</td> 
											<!--<td style="word-break: break-all;"> -->
											<td>
												<select name="mac" id="mac">
												<%
														String[] as = mac.split(",");
														int num=mac.length()/17;
														for(int i=0;i<num;i++){
													%>
														<option value="<%=as[i]%>"><%=as[i]%></option>
													<%	
														}
												 %> 
												 </select>
												 [<a href="#" style="cursor:hand" onclick="updateMac()">入库</a>]
												 <script type="text/javascript">
												 	function updateMac(){
												 		var mac="<%=mac%>";
												 		var id="<%=tmp%>";
        												$.ajax({
															type:"GET",
															dataType:"json",
															url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=updatemac&mac="+mac+"&id="+id+"&flag="+(new Date()),
															success:function(data){
																window.alert("入库成功！");
															}
														});
												 	}
												 </script>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal valign=center nowrap> &nbsp;系统OID:</td>
											<td><%=host.getSysOid()%></td>
										</tr>
      									<tr >
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备资产编号:</td>
											<td width="70%"><span id="assetid"><%=host.getAssetid()%></span></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;机房位置:</td>
											<td width="70%"><span id="location"><%=host.getLocation()%></span></td>
										</tr>
										<tr>
											<td height="29" class=txtGlobal valign=center nowrap> &nbsp;设备供应商: </td>
											<td>
												<%
													if (supper != null) {
												%>
													<a href="#" style="cursor: hand" onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
												<%
													}
												%>
											</td>
										</tr> 
									</table>
								</td> 
													
								<td width="40%" align="right" valign="top">
									<jsp:include page="/topology/includejsp/systeminfo_graphic.jsp">
										<jsp:param name="rootPath" value="<%=rootPath %>"/>
										<jsp:param name="picip" value="<%=picip %>"/> 
										<jsp:param name="avgresponse" value="<%=avgresponse %>"/>  
										<jsp:param name="pvalue" value="<%=memoryvalue %>"/>
										<jsp:param name="pingavg" value="<%=pingavg %>"/>	 
									</jsp:include>
								</td>  
								<!-- <td width="40%" align="right" valign="middle">
									<table cellPadding=0 cellspacing="0" align=center>
										<tr>
											<td width="100%" align="left" valign="middle">
												<table cellpadding=0 cellspacing="0" width=80% align=center>
													<tr>
														<td height="30" align="center"><img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png" id="pingavg"></td>
													</tr>
													<tr>
														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
													</tr>
													<tr>
														<td height="7">&nbsp;</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td width="100%" align="left" valign="middle">
												<table cellpadding=0 cellspacing="0" width=80% align=center>
													<tr>
														<td align="center" height="30"><img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png" id="cpu"></td>
													</tr>
													<tr>
														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading.gif"></td>
													</tr> 
												</table>
											</td>
										</tr>
									</table>
								</td> -->
							</tr>
							<tr>
							             <td width="100%" align="center" valign="top" colspan=2>
							                    <table> 
                      								<tr>
	                      								<td width="30%">
                      						 				<table  height=15 width="100%" bgcolor=#ffffff id="XNTABLE">
			                      								<tr>
			                      									<td>
			                      									<div id="hostCpu">
														
														            </div>		
	                                          <script type="text/javascript"
	                                                src="<%=rootPath%>/include/swfobject.js"></script>
	                                            
	                                               <script type="text/javascript">
	                                              <% if(!cpuData.equals("0")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostcpu_settings.xml"));
				                                  so.addVariable("chart_data","<%=cpuData%>");
				                                  so.write("hostCpu");
				                                  <%}else{%>
				                                  var _div=document.getElementById("hostCpu");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.png");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
                                                                    </td>
			                      								</tr>
		                      								</table>
	                      								</td>	
	                      								<td width="42%" align=left>
	                      								<div id="Infomemory">
														<strong>You need to upgrade your Flash Player</strong>
														</div>

												<script type="text/javascript">
				
		                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "330", "210", "8", "#FFFFFF");
		                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/hostmemory_settings.xml"));
	                                                   so.addVariable("chart_data", "<%=temp%>");
		                                               so.addVariable("preloader_color", "#999999");
		                                               so.write("Infomemory");
	                                            </script>
	                      									
	                      								</td>
	                      								<td width="28%" align=left>
	                      						<div id="hostdisk">
														
														</div>		
	                                         
	                                               <script type="text/javascript">
	                                              <% if(!valueStr.equals("0")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostdisk_settings.xml"));
				                                  so.addVariable("chart_data","<%=valueStr%>");
				                                  so.write("hostdisk");
				                                  <%}else{%>
				                                  var _div=document.getElementById("hostdisk");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.png");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
	                      									
	                      								</td>
                      								</tr>
                     							</table>
							          </td>
							     </tr>
						</table> 
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>