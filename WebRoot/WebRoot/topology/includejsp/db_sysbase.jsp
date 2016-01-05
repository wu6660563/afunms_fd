<%@page language="java" contentType="text/html;charset=gb2312"%>
<%
	String  rootPath = request.getContextPath();;  
	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye"); //库名称
	String  IpAddress= request.getParameter("IpAddress")==null?"":request.getParameter("IpAddress"); //vo.getIpAddress()
	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed"); 
	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr"); 
	String  ServerName= request.getParameter("ServerName")==null?"":request.getParameter("ServerName"); //sysbaseVO.getServerName()
	String  Version= request.getParameter("Version")==null?"":request.getParameter("Version"); //sysbaseVO.getVersion()
	String  avgdata= request.getParameter("avgdata")==null?"":request.getParameter("avgdata");	
	
	
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
			<table id="application-detail-content-body"
				class="application-detail-content-body">
				<tr>
					<td>

						<table width="100%" cellPadding=0 rules=none  align=center algin="center">
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="30%" height="29"  nowrap class=txtGlobal>
												&nbsp;类型:
											</td>
											<td width="70%">
												&nbsp;<%=dbtye%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td  height="29"  nowrap class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td>
												&nbsp;<%=IpAddress%>
											</td>
										</tr>
										<tr>
											<td  height="29"  nowrap class=txtGlobal>
												&nbsp;管理状态:
											</td>
											<td>
												&nbsp;<%=managed%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td  height="29"  nowrap class=txtGlobal>
												&nbsp;当前状态:
											</td>
											<td>
												&nbsp;<%=runstr%>
											</td>
										</tr>
										<tr>
											<td  height="29"  nowrap
												class=txtGlobal>
												&nbsp;服务器名称:
											</td>
											<td><%=ServerName%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1"> 
											<td  height="29"  nowrap
												class=txtGlobal>
												&nbsp;版本:
											</td>
											<td colspan="3"><%=Version%>
											</td>
										</tr>
									</table>
								</td>

								<td valign="middle">
									<table cellPadding=0 cellspacing="0" align=center>
										<tr>
											<td width="100%" valign="middle">
												<table>
													<tr>
														<td width="100%"> 
															<div id="pie">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript"
																src="<%=rootPath%>/include/swfobject.js"></script>
															<script type="text/javascript">
                     	 										// <![CDATA[		
                                                             	var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","180", "155", "8", "#FFFFFF");
                                                                so.addVariable("path", "<%=rootPath%>/amchart/");
                                                                so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                                                                so.addVariable("chart_data","<%=avgdata%>");
                                                                so.write("pie");
                                                              // ]]>
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