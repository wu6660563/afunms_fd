<%@page language="java" contentType="text/html;charset=gb2312"%> 
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%> 
<%@page import="com.afunms.common.util.*" %> 
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%> 
<%@ page import="com.afunms.polling.om.*"%>  
<%@ page import="com.afunms.polling.PollingEngine"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>  
<%@page import="com.afunms.polling.node.Host" %>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.service.memoryInfo.MemoryInfoService"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%
  	String rootPath = request.getParameter("rootPath");    
	String tmp = request.getParameter("nodeid"); 
	String picip =request.getParameter("picip");    
	String collecttime =request.getParameter("collecttime"); //数据采集时间
	String sysUpTime =request.getParameter("sysUpTime");//设备启动时间 
	String sysdescr =request.getParameter("sysdescr");//MAC地址  
	String sysservices =request.getParameter("sysservices");//MAC地址  
	String runmodel = PollingEngine.getCollectwebflag(); 
	
 	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));	 
  	SupperDao supperdao = new SupperDao();
   	Supper supper = null;
   	String suppername = "";
   	try{
   		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
   		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
   	}catch(Exception e){
   		e.printStackTrace();
   	}finally{
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
	}
	//内存利用率
	Vector memoryVector = new Vector(); 
	String memoryvalue = "0";
	Hashtable memhash=new Hashtable();
	Hashtable diskhash=new Hashtable();
	I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	if("0".equals(runmodel)){
       	//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
		//wxy edit 
		if(ipAllData!=null)
		 memoryVector = (Vector) ipAllData.get("memory"); 
		  try{
			memhash = hostlastmanager.getMemory_share(host.getIpAddress(),"Memory",starttime,totime);
		    diskhash = hostlastmanager.getDisk_share(host.getIpAddress(),"Disk",starttime,totime);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	 }else{
	    //采集与访问是分离模式
		NodeUtil nodeUtil = new NodeUtil();
   		NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host); 
	 	MemoryInfoService memoryInfoService = new MemoryInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		memoryVector = memoryInfoService.getMemoryInfo();
		try {
			memhash = hostlastmanager.getMemory(host.getIpAddress(),
					"Memory", starttime, totime);
			diskhash = hostlastmanager.getDisk(host.getIpAddress(),
					"Disk", starttime, totime);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	 double usememoryvalue=0;	
	 double allmemoryvalue = 1;	 
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
			<table id="detail-content-body" class="detail-content-body" >
				<tr>
					<td>
						<table align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="50%" align="left" valign="top">
									<table cellpadding="0" cellspacing="0" width=100% align=center>
										<tr>
											<td width=30% height="29" align="left" nowrap>
												&nbsp;设备标签:
											</td>
											<td width=70%><%=host.getAlias()%>
												<!-- [<a href="<//%=rootPath%>/network.do?action=ready_editalias&id=<//%=tmp%>&ipaddress=<//%=host.getIpAddress()%>"><img src="<//%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>] -->
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align="left" nowrap>
												&nbsp;系统名称:
											</td>
											<td>
												<%=host.getSysName()%>
												[<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
											</td>
										</tr>
										<tr>
											<td height="29" align="left">
												&nbsp;状态:
											</td>
											<td>
												<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>">
												<%=NodeHelper.getStatusDescr(host.getStatus())%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap>
												&nbsp;IP地址:
											</td>
											<%
												IpAliasDao ipdao = new IpAliasDao();
												List iplist = ipdao.loadByIpaddress(host.getIpAddress());
												ipdao.close();
												ipdao = new IpAliasDao();
												IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(), "1");
												ipdao.close();
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
														<%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>
														selected <%} %>><%=voTemp.getAliasip()%></option>
													<%
														}
													%>
												</select>
												[<a href="#" style="cursor: hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">修改</a>]
											</TD>
										</tr>
										<tr>
											<td height="29" align="left">
												&nbsp;子网掩码:
											</td>
											<td><%=host.getNetMask()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left>
												&nbsp;类别:
											</td>
											<td><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
										</tr>
										<tr>
											<td height="29" align=left>
												&nbsp;类型:
											</td>
											<td><%=host.getType()%></td>
										</tr>
										<!-- 
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap>
												&nbsp;系统描述:
											</td>
											<td><//%=sysdescr%></td>
										</tr>
										 -->
										<tr>
											<td height="29">
												&nbsp;设备启动时间:
											</td>
											<td><%=sysUpTime%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left valign=middle nowrap
												class=txtGlobal>
												&nbsp;数据采集时间:
											</td>
											<td><%=collecttime%></td>
										</tr>
										<tr>
											<td height="29" class=txtGlobal valign=middle nowrap>
												&nbsp;设备服务数:
											</td>
											<td><%=sysservices%></td>
										</tr> 
      									<tr bgcolor="#F1F1F1" >
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备资产编号:</td>
											<td width="70%"><span id="assetid"><%=host.getAssetid()%></span></td>
										</tr>
										<tr>
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;机房位置:</td>
											<td width="70%"><span id="location"><%=host.getLocation()%></span></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal valign=middle nowrap>
												&nbsp;设备供应商:
											</td>
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
							</tr>
							<tr>
							             <td width="100%" align="center" valign="top" colspan=2>
							                    <table> 
                      								<tr>
	                      								<td width="30%" align=center>
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
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
                                                                    </td>
			                      								</tr>
		                      								</table>
	                      								</td>	
	                      								<td width="42%" align=center>
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
	                      								<td width="28%" align=center>
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
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
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
