<%@page language="java" contentType="text/html;charset=gb2312"%>
<%

String rootPath=request.getContextPath();  
String  IpAddress= request.getParameter("IpAddress")==null?"":request.getParameter("IpAddress"); //weblogicconf.getIpAddress()  
String  Portnum= request.getParameter("Portnum")==null?"":request.getParameter("Portnum"); //weblogicconf.getPortnum() 
String  DomainName= request.getParameter("DomainName")==null?"":request.getParameter("DomainName"); //normalvalue.getDomainName()  
String  DomainActive= request.getParameter("DomainActive")==null?"":request.getParameter("DomainActive"); //normalvalue.getDomainActive() 
String  DomainAdministrationPort= request.getParameter("DomainAdministrationPort")==null?"":request.getParameter("DomainAdministrationPort"); //normalvalue.getDomainAdministrationPort() 

String  DomainConfigurationVersion= request.getParameter("DomainConfigurationVersion")==null?"":request.getParameter("DomainConfigurationVersion"); //normalvalue.getDomainConfigurationVersion() 

String  lasttime= request.getParameter("lasttime")==null?"":request.getParameter("lasttime");  
String  nexttime= request.getParameter("nexttime")==null?"":request.getParameter("nexttime"); 
String  percent1= request.getParameter("percent1")==null?"0":request.getParameter("percent1");   
String  percent2= request.getParameter("percent2")==null?"0":request.getParameter("percent2");   

int  Mon_flag=Integer.parseInt(request.getParameter("Mon_flag")==null?"":request.getParameter("Mon_flag")); //weblogicconf.getMon_flag()
 float avgPing=Float.parseFloat(percent1);
 StringBuffer dataStr = new StringBuffer();
		 	dataStr.append("连通;").append(Math.round(avgPing)).append(";false;7CFC00\\n");
		 	dataStr.append("未连通;").append(100-Math.round(avgPing)).append(";false;FF0000\\n");
		 	String avgdata = dataStr.toString();
 %>
<table id="application-detail-content" class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="WEBLOGIC详细信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td>
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="70%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td width="35%"><%=IpAddress%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;监控端口:
											</td>
											<td width="35%"><%=Portnum%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理域名:
											</td>
											<td width="35%"><%=DomainName%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理域状态:
											</td>
											<%
												if ("1".equals(DomainActive)) {
											%>
											<td width="35%">
												活动
											</td>
											<%
												} else {
											%>
											<td width="35%">
												不活动
											</td>
											<%
												}
											%>
										</tr>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理端口:
											</td>
											<td width="35%"><%=DomainAdministrationPort%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理状态:
											</td>
											<%
												if (Mon_flag == 1) {
											%>
											<td width="35%">
												管理中
											</td>
											<%
												} else {
											%>
											<td width="35%">
												未管理
											</td>
											<%
												}
											%>
											<td width="35%">
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;版本:
											</td>
											<td width="85%" colspan=3><%=DomainConfigurationVersion%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;上一次轮询:
											</td>
											<td width="35%"><%=lasttime%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;下一次轮询:
											</td>
											<td width="35%"><%=nexttime%>
											</td>
										</tr>

									</table>

								</td>
								<td width="20%" align="center" valign="middle" class=dashLeft>

									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center" >
															今日连通率
														</td>
													</tr>

										<tr>
											<td width="400" align="left" valign="middle" class=dashLeft>
												<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													cellpadding=0 rules=none width=80% align=center border=0
													algin="center">
													<tr class="topNameRight">
														<td height="30" align="center">
														<!-- <div id="flashcontent00">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript">
																var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																so.write("flashcontent00"); 
															</script>
															    -->
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
														</td>

													</tr>
													<tr>
														<td height="7" align=center>
															<img src="<%=rootPath%>/resource/image/Loading_2.gif">
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
			</table>
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>