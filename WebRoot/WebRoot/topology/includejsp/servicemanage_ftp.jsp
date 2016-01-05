<%@page language="java" contentType="text/html;charset=gb2312"%> 
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%> 
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%


	String rootPath = request.getContextPath();
	String status=request.getParameter("status");//NodeHelper.getCurrentStatusImage(status)
	
	String alarmmessage=request.getParameter("alarmmessage"); 
	
	List urllist = new ArrayList(); // ��������ѡ���б�
	FTPConfigDao configdao = new FTPConfigDao();
	User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	String bids = operator.getBusinessids();
	String bid[] = bids.split(",");
	Vector rbids = new Vector();
	if (bid != null && bid.length > 0) {
		for (int i = 0; i < bid.length; i++) {
			if (bid[i] != null && bid[i].trim().length() > 0)
				rbids.add(bid[i].trim());
		}
	}
	try {
		urllist = configdao.getFtpByBID(rbids);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		configdao.close();
	}
	
	FTPConfig queryconf = new FTPConfig(); // ��ǰ�Ķ���
	Integer queryid =Integer.parseInt(request.getParameter("id"));// .getUrl_id();
		request.setAttribute("id", queryid);
		if (urllist.size() > 0 && queryid == null) {
			Object obj = urllist.get(0);
		}
		if (queryid != null) {
			// ��������ӹ�����ȡ�ò�ѯ����
			configdao = new FTPConfigDao();
			try {
				queryconf = (FTPConfig) configdao.findByID(queryid + "");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				configdao.close();
			}
		} 
	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(queryconf.getSupperid()
				+ "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
%>
<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			<jsp:include page="/topology/includejsp/detail_content_top.jsp">
				<jsp:param name="contentTitle" value="FTP ��ϸ��Ϣ" />
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
								<td>

									&nbsp;��������
									<select name="id">
										<%
											if (urllist != null && urllist.size() > 0) {
												for (int i = 0; i < urllist.size(); i++) {
													FTPConfig webconfig = (FTPConfig) urllist.get(i);
													if (webconfig.getId() == queryconf.getId()) {
										%>

										<option value="<%=webconfig.getId()%>" selected="selected">
											<%=webconfig.getName()%></option>

										<%
											} else {
										%>
										<option value="<%=webconfig.getId()%>">
											<%=webconfig.getName()%></option>
										<%
											}
												}
											}
										%>
									</select>
									<input type="button" onclick="show_graph()" class=button
										value="��ѯ">
									&nbsp;&nbsp;&nbsp;
									<br>

								</td>
							</tr>
							<tr>
								<td>
									<table cellspacing="10">
										<tr>
											<td width="60%" align="center">
												<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													cellpadding=0 rules=none width=100% align=center border=1
													algin="center">
													<tr bgcolor="#F1F1F1">
														<td width="30%" height="26" align="left" nowrap>
															&nbsp;����:
														</td>
														<td width="70%"><%=queryconf.getName()%></td>
													</tr>
													<tr>
														<td width="30%" height="26" align="left" nowrap>
															&nbsp;����:
														</td>
														<td width="70%">
															FTP�������
														</td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td width="30%" height="26" align="left" nowrap>
															&nbsp;״̬:
														</td>
														<td width="70%">
															<img
																src="<%=rootPath%>/resource/<%=status%>"
																border="0" alt=<%=alarmmessage%>>
														</td>
													</tr>
													<tr>
														<td width="30%" height="26" align=left nowrap>
															&nbsp;IP��ַ:
														</td>
														<td width="70%"><%=queryconf.getIpaddress()%></td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td height="29" align="left">
															&nbsp;�����ļ�:
														</td>
														<td>
															&nbsp;<%=queryconf.getFilename()%>
														</td>
													</tr>
													<tr>
														<td width="30%" height="26" align=left nowrap>
															&nbsp;�û���:
														</td>
														<td width="70%"><%=queryconf.getUsername()%></td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td height="29" align="left">
															&nbsp;���ݲɼ�ʱ��:
														</td>
														<td><%=request.getAttribute("lasttime")%></td>
													</tr>
													<tr>
														<td height="29" class=txtGlobal valign=center nowrap>
															&nbsp;��Ӧ��:
														</td>
														<td>
															<%
																if (supper != null) {
															%>
															<a href="#" style="cursor: hand"
																onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
															<%
																}
															%>
														</td>
													</tr>
												</table>
											</td>
											<td width="40%" align="center">
												<table width="100%" cellspacing="0" cellpadding="0"
													align="center">
													<tr>
														<td width="100%" align="center">
															<div id="flashcontent2">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript">
																var so = new SWFObject("<%=rootPath%>/flex/Ftp_Ping_Pie.swf?id=<%=queryconf.getId()%>", "Ftp_Ping_Pie", "380", "220", "8", "#ffffff");
																so.write("flashcontent2");
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
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<jsp:include page="/topology/includejsp/detail_content_footer.jsp" />
		</td>
	</tr>
</table>