<%@page language="java" contentType="text/html;charset=gb2312"%>
<%

	String rootPath = request.getContextPath();;  
	String  Name= request.getParameter("Name")==null?"":request.getParameter("Name"); //storage.getName()
	String  Ipaddress= request.getParameter("Ipaddress")==null?"":request.getParameter("Ipaddress"); //storage.getIpaddress()
	String  Mon_flag= request.getParameter("Mon_flag")==null?"":request.getParameter("Mon_flag"); //storage.getMon_flag()
	String  Status= request.getParameter("Status")==null?"":request.getParameter("Status"); //storage.getStatus()
	String  producerStr= request.getParameter("producerStr")==null?"":request.getParameter("producerStr"); // 
	String  SerialNumber= request.getParameter("SerialNumber")==null?"":request.getParameter("SerialNumber"); // storage.getSerialNumber() 
	String  chart1= request.getParameter("chart1")==null?"":request.getParameter("chart1");
	
 %>
<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="存储详细信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body"
				class="application-detail-content-body">
				<tr>
					<td>

						<table>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;名称:
											</td>
											<td width="35%"><%=Name%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td width="35%"><%=Ipaddress%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理状态:
											</td>
											<%
												if ("1".equals(Mon_flag)) {
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
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;运行状态:
											</td>
											<%
												if ("1".equals(Status)) {
											%>
											<td width="35%">
												正常
											</td>
											<%
												} else {
											%>
											<td width="35%">
												不连通
											</td>
											<%
												}
											%>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;型号:
											</td>
											<td width="35%"><%=producerStr%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;序列号:
											</td>
											<td width="35%"><%=SerialNumber%>
											</td>
										</tr> 
									</table>
								</td>
								<td>
									<table>
										<tr>
											<td>
												<table align="center">
													<tr>
														<td align="center"> 
															<img src="<%=rootPath%>/artist?series_key=<%=chart1%>">
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
