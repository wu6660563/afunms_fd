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
	
	List urllist = new ArrayList(); // 用做条件选择列表
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
	
	FTPConfig queryconf = new FTPConfig(); // 当前的对象
	Integer queryid =Integer.parseInt(request.getParameter("id"));// .getUrl_id();
		request.setAttribute("id", queryid);
		if (urllist.size() > 0 && queryid == null) {
			Object obj = urllist.get(0);
		}
		if (queryid != null) {
			// 如果是链接过来则取用查询条件
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
				<jsp:param name="contentTitle" value="FTP 详细信息" />
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

									&nbsp;服务名称
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
										value="查询">
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
															&nbsp;名称:
														</td>
														<td width="70%"><%=queryconf.getName()%></td>
													</tr>
													<tr>
														<td width="30%" height="26" align="left" nowrap>
															&nbsp;类型:
														</td>
														<td width="70%">
															FTP服务监视
														</td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td width="30%" height="26" align="left" nowrap>
															&nbsp;状态:
														</td>
														<td width="70%">
															<img
																src="<%=rootPath%>/resource/<%=status%>"
																border="0" alt=<%=alarmmessage%>>
														</td>
													</tr>
													<tr>
														<td width="30%" height="26" align=left nowrap>
															&nbsp;IP地址:
														</td>
														<td width="70%"><%=queryconf.getIpaddress()%></td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td height="29" align="left">
															&nbsp;测试文件:
														</td>
														<td>
															&nbsp;<%=queryconf.getFilename()%>
														</td>
													</tr>
													<tr>
														<td width="30%" height="26" align=left nowrap>
															&nbsp;用户名:
														</td>
														<td width="70%"><%=queryconf.getUsername()%></td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td height="29" align="left">
															&nbsp;数据采集时间:
														</td>
														<td><%=request.getAttribute("lasttime")%></td>
													</tr>
													<tr>
														<td height="29" class=txtGlobal valign=center nowrap>
															&nbsp;供应商:
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