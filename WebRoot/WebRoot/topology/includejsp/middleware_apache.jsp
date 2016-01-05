<%@page language="java" contentType="text/html;charset=gb2312"%>  
<%

 String rootPath = request.getContextPath(); 
 String alias=request.getParameter("alias");//名称  vo.getAlias() 
 String ipaddress=request.getParameter("ipaddress");//IP地址  vo.getIpaddress()
 String status=request.getParameter("status");//状态 NodeHelper.getCurrentStatusImage(status)
 String port=request.getParameter("port");//端口号 vo.getPort()
 String version=request.getParameter("version");//版本号  if (apache_ht.get("version") != null) 
 String built=request.getParameter("built");//编译安装时间  if (apache_ht.get("built") != null) 
 String current=request.getParameter("current");//系统时间:  if (apache_ht.get("current") != null)  
 String restart=request.getParameter("restart");//重新启动时间  if (apache_ht.get("restart") != null)  
 String parent=request.getParameter("parent");//父程序的世代编号  if (apache_ht.get("parent") != null)  
 String uptime=request.getParameter("uptime");//服务器运行时间  if (apache_ht.get("uptime") != null)   
 String accesses=request.getParameter("accesses");//接收的联机数量  if (apache_ht.get("accesses") != null)  
 String traffic=request.getParameter("traffic");//传输的数据量  if (apache_ht.get("traffic") != null)  
 String percent1=request.getParameter("percent1");// 
 String percent2=request.getParameter("percent2");//   
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
			 	<jsp:param name="contentTitle" value="Apache服务监视"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body">
				<tr>
					<td>  
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
							rules=none align=center border=1 cellpadding=0 cellspacing="0"
							width=100%>
							<TBODY>
								<tr>
									<td width="80%" align="left" valign="top" class=dashLeft>
										<table>
											<tr bgcolor="#F1F1F1">
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;名称:
												</td>
												<td width="35%"><%=alias%></td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;IP地址:
												</td>
												<td><%=ipaddress%></td>
											</tr>
											<tr>
												<td width="10%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;状态:
												</td>
												<td width="35%">
													<img src="<%=rootPath%>/resource/<%=status%>" border="0">
												</td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;端口号:
												</td>
												<td><%=port%></td>
											</tr>
											<tr bgcolor="#F1F1F1">
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;版本号:
												</td>
												<td width="35%"> 
													<%= version %> 
												</td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;编译安装时间:
												</td>
												<td>
													<%= built%>
												</td>
											</tr>
											<tr>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;系统时间:
												</td>
												<td width="35%"> 
													<%=current%> 
												</td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;重新启动时间:
												</td>
												<td> 
													<%=restart%> 
												</td>
											</tr>
											<tr bgcolor="#F1F1F1">
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;父程序的世代编号:
												</td>
												<td width="35%">
													<%=parent%>
												</td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;服务器运行时间:
												</td>
												<td>
													<%=uptime%>
												</td>
											</tr>
											<tr>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;接收的联机数量:
												</td>
												<td width="35%">
													<%=accesses%>
												</td>
												<td width="15%" height="29"  nowrap
													class=txtGlobal>
													&nbsp;传输的数据量:
												</td>
												<td>
													<%=traffic%>
												</td>
											</tr>

										</table>

									</td>

									<td width="20%" align="center" valign="middle" class=dashLeft>

										<table width="100%" style="BORDER-COLLAPSE: collapse"
											borderColor=#cedefa cellPadding=0 rules=none align=center
											border=1 algin="center">

											<tr>
												<td align="center" valign="middle" class=dashLeft>

													<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															今日连通率
														</td>
													</tr>
														<tr>
															<td width="100%" align="center">
															<!-- 	<div id="flashcontent00">
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