<%@page language="java" contentType="text/html;charset=gb2312"%>  
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%> 
<%@page import="com.afunms.common.util.*" %> 
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%> 
<%@page import="com.afunms.polling.om.*"%> 
<%@page import="com.afunms.polling.PollingEngine"%>  
<%@page import="com.afunms.polling.node.Host"%>  
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>  
<%
	//获取参数
	String rootPath = request.getParameter("rootPath");  
	String tmp = request.getParameter("tmp")==null?"":request.getParameter("tmp");  
	String collecttime =request.getParameter("collecttime"); //数据采集时间
	String sysuptime =request.getParameter("sysuptime");//设备启动时间 
	String sysdescr =request.getParameter("sysdescr");// 系统描述 

	
	
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	 
	String picip = "";//ip
 	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try { 
		String ip = host.getIpAddress();
		picip = CommonUtil.doip(ip);
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
						<table align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="50%" align="left" valign="top">
									<table cellpadding="0" cellspacing="0" width=100% align=center>
										<tr>
											<td width="30%" height="29" align="left" nowrap>
												&nbsp;设备标签:
											</td>
											<td width="70%"><%=host.getAlias()%>
												[<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align="left" nowrap>
												&nbsp;系统名称:
											</td>
											<td><%=host.getSysName()%>
												[<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
											</td>
										</tr>
										<tr>
											<td height="29" align="left">
												&nbsp;状态:
											</td>
											<td>
												<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>">&nbsp;<%=NodeHelper.getStatusDescr(host.getStatus())%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap>
												&nbsp;IP地址:
											</td>
											<td><%=host.getIpAddress()%></td>
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
										<%
											if(sysdescr==null){
												sysdescr = "";
											} 
										%>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap>
												&nbsp;系统描述:
											</td>
											<td>
												<jsp:include page="/topology/includejsp/td_acronym.jsp">
													<jsp:param name="wholeStr" value="<%= sysdescr %>"/>
												</jsp:include> 
											</td>
										</tr>
										<%
											if(sysuptime==null){
												sysuptime = "";
										   	} 
										%>
										<tr>
											<td height="29">
												&nbsp;设备启动时间:
											</td>
											<td><%=sysuptime%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left valign=middle  nowrap class=txtGlobal>
												&nbsp;数据采集时间:
											</td>
											<td><%=collecttime%></td>
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
											<td height="29" class=txtGlobal valign=middle  nowrap>
												&nbsp;设备供应商:
											</td>
											<td>
											<% 
												if(supper != null){
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