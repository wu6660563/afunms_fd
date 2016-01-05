<%@page language="java" contentType="text/html;charset=gb2312"%> 
<%@page import="com.afunms.common.util.CreateMetersPic"%> 
<%@page import="com.afunms.initialize.ResourceCenter"%> 
<%
	String rootPath = request.getContextPath();;  
	String  dbName= request.getParameter("dbName")==null?"":request.getParameter("dbName"); //库名称
	String  alias= request.getParameter("alias")==null?"":request.getParameter("alias"); //别名
	String  ipAddress= request.getParameter("ipAddress")==null?"":request.getParameter("ipAddress"); //ip
	String  port= request.getParameter("port")==null?"":request.getParameter("port");  //端口
	
 	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye");
 	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed");
 	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr");
 	String  version= request.getParameter("version")==null?"":request.getParameter("version");
 	String  hostOS= request.getParameter("hostOS")==null?"":request.getParameter("hostOS");
 	String  basePath= request.getParameter("basePath")==null?"":request.getParameter("basePath");
 	String  dataPath= request.getParameter("dataPath")==null?"":request.getParameter("dataPath");
 	String  logerrorPath= request.getParameter("logerrorPath")==null?"":request.getParameter("logerrorPath");
 	String  picip= request.getParameter("picip")==null?"":request.getParameter("picip");
    double  avgpingcon= Double.parseDouble(request.getParameter("pingavg")==null?"0":request.getParameter("pingavg")); // 平均连通率
    StringBuffer dataStr = new StringBuffer();
	dataStr.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
	dataStr.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
	String avgdata = dataStr.toString();
	System.out.println(avgpingcon+"...........");
	 String pingavg=String.valueOf(Math.round(avgpingcon));
		CreateMetersPic cmp = new CreateMetersPic();
		String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	     cmp.createChartByParam(picip , pingavg , pathPing,"连通率","pingdata");			  	   
%>
 
<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="数据信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td>
						<table align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td>
												<table>
													<tr>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;数据库别名:
														</td>
														<td width="35%"><%=alias%>
														</td>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;库名称:
														</td>
														<td width="35%"><%=dbName%>
														</td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;类型:
														</td>
														<td width="35%"><%=dbtye%>
														</td>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;IP地址:
														</td>
														<td width="35%"><%=ipAddress%>
														</td>
													</tr>
													<tr>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;端口:
														</td>
														<td width="35%"><%=port%>
														</td>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;管理状态:
														</td>
														<td width="35%"><%=managed%>
														</td>
													</tr>

													<tr bgcolor="#F1F1F1">
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;当前状态:
														</td>
														<td width="85%" colspan=3><%=runstr%>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table>
													<tr>
														<%
															if (version != null) {
														%>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;数据库版本号：
														</td>
														<td width="35%"><%=version%>
														</td>
														<%
															}
														%>
													</tr>
													<tr bgcolor="#F1F1F1">
														<%
															if (hostOS != null) {
														%>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;服务器操作系统:
														</td>
														<td width="35%"><%=hostOS%>
														</td>
														<%
															}
														%>
													</tr>
													<tr>
														<%
															if (basePath != null) {
														%>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;基本路径:
														</td>
														<td width="35%"><%=basePath%>
														</td>
														<%
															}
														%>
													</tr>
													<tr bgcolor="#F1F1F1">
														<%
															if (dataPath != null) {
														%>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;数据路径:
														</td>
														<td width="35%"><%=dataPath%>
														</td>
														<%
															}
														%>
													</tr>
													<tr>
														<%
															if (logerrorPath != null) {
														%>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;错误日志路径:
														</td>
														<td width="35%"><%=logerrorPath%>
														</td>
														<%
															}
														%>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>


								<td valign="top">
									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
										rules=none align=center border=1 cellpadding=0
										cellspacing="0" width=100%>
										<tr bgcolor=#F1F1F1 height="26">
											<td align="center" >
												今日平均连通率
											</td>
										</tr>
										<tr>
											<td width="100%" align="left" valign="middle">
												<table cellpadding=0 width=80% algin="center">
													<tr>
														<td height="30" align="center">
														 <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png" > 
														<!--  
																<div id="avgping">
	                                            <strong>You need to upgrade your Flash Player</strong>
	                                        </div>
	                                           <script type="text/javascript"
				                                      src="<%=rootPath%>/include/swfobject.js"></script>
		                                       <script type="text/javascript">
			                                    var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
			                                        so.addVariable("path", "<%=rootPath%>/amchart/");
			                                        so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
			                                        so.addVariable("chart_data","<%=avgdata%>");
			                                        so.write("avgping");
		                                      </script>
		                                      -->
														</td>
													</tr>
													<tr>
														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
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
			</table>
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table> 