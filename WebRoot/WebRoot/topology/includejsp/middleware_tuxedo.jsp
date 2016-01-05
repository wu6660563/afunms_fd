<%@page language="java" contentType="text/html;charset=gb2312"%>

<%
	String name = request.getParameter("name");//tuxedoConfig.getName();
	String address = request.getParameter("address");//tuxedoConfig.getIpAddress();
	String flag = request.getParameter("flag");//tuxedoConfig.getMon_flag();
	String status = request.getParameter("status");//tuxedoConfig.getStatus(); 
	
%>
<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			<jsp:include page="/topology/includejsp/detail_content_top.jsp">
				<jsp:param name="contentTitle" value="Tuxedo详细信息" />
			</jsp:include>
		</td>
	</tr>

	<tr>
		<td>
			<table id="application-detail-content-body"
				class="application-detail-content-body">
				<tr>
					<td>
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
							rules=none align=center border=1 cellpadding=0 cellspacing="0"
							width=100%>
							<tr>
								<td width="70%" align="left" valign="top">
									<table>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;名称:
											</td>
											<td width="35%"><%=name%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td width="35%"><%=address%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;管理状态:
											</td>
											<%
												if ("1".equals(flag)) {
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
												if ("1".equals(status)) {
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
			<jsp:include page="/topology/includejsp/detail_content_footer.jsp" />
		</td>
	</tr>
</table>