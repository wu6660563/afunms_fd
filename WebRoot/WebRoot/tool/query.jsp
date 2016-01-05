<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.afunms.query.QueryService"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/table.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/resource/css/style.css">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<title>SQL执行工具</title>
		<script language="javascript" src="<%=path%>/js/query.js"></script>
	</head>
	<body>

		<%
			//存放表列的名称
			Vector<String> head = new Vector<String>();
			//存放查询得到的各行记录
			Vector<Vector<String>> rows = new Vector<Vector<String>>();
			String headName = null;
			String sql = "";
			sql = request.getParameter("name");
			QueryService query = new QueryService();
			query.getAllDataFromDB(sql);
			head = query.getHead();
			rows = query.getRows();
			//当为空时代表删除、修改操作
			if (head == null) {
		%>
		<%=query.getMessage()%>
		<%
			} else {
		%>
		<hr color="#007108">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="30" background="<%=path%>/resource/queryimage/images/tab_05.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="12" height="30">
								<img src="<%=path%>/resource/queryimage/images/tab_03.gif" width="12" height="30" />
							</td>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="46%" valign="middle">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<td width="5%">
														<div align="center">
															<img src="<%=path%>/resource/queryimage/images/tb.gif" width="16" height="16" />
														</div>
													</td>
													<td width="95%" class="STYLE1">
														<span class="STYLE3">当前位置</span>:SQL查询工具
													</td>
												</tr>
											</table>
										</td>
										<td width="54%">
										</td>
									</tr>
								</table>
							</td>
							<td width="16">
								<img src="<%=path%>/resource/queryimage/images/tab_07.gif" width="16" height="30" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="8" background="<%=path%>/resource/queryimage/images/tab_12.gif">
								&nbsp;
							</td>
							<td>
								<table border="0" cellpadding="0" cellspacing="1"
									bgcolor="b5d6e6" onmouseover="changeto()"
									onmouseout="changeback()">
									<tr>
										<%
											Enumeration<String> headEnumeration = head.elements();
												while (headEnumeration.hasMoreElements()) {
													headName = headEnumeration.nextElement();
										%>
										<td id="col" height="22" background="<%=path%>/resource/queryimage/images/bg.gif"
											bgcolor="#FFFFFF">
											<div align="center">
												<span class="STYLE1"><b><%=headName%></b>
										</td>
										<%
											}
											}
										%>
									</tr>
									<%
										if (rows != null) {
											Vector<String> row = new Vector<String>();
											String colname = null;
											Enumeration<Vector<String>> rowsEnumeration = rows.elements();
											//双重遍历获得查询记录
											while (rowsEnumeration.hasMoreElements()) {
												row = rowsEnumeration.nextElement();
												Enumeration<String> rowEnumeration = row.elements();
									%>
									<tr>
										<%
											while (rowEnumeration.hasMoreElements()) {
														colname = rowEnumeration.nextElement();
										%>
										<td id="col" height="20" bgcolor="#FFFFFF">
											<div align="center">
												<span class="STYLE1"><%=colname%></span>
											</div>
										</td>
										<%
											}
										%>
									</tr>
									<%
										}
									%>
								</table>
							</td>
							<td width="8" background="<%=path%>/resource/queryimage/images/tab_15.gif">
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="35" background="<%=path%>/resource/queryimage/images/tab_19.gif">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="12" height="35">
								<img src="<%=path%>/resource/queryimage/images/tab_18.gif" width="12" height="35" />
							</td>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td class="STYLE4">
											&nbsp;&nbsp;记录数目
										</td>
										<td>
											<table border="0" align="right" cellpadding="0"
												cellspacing="0">
												<tr>
													<td width="40">
													</td>
													<td width="45">
													</td>
													<td width="45">
													</td>
													<td width="40">
													</td>
													<td width="100">
													</td>
													<td width="40">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td width="16">
								<img src="<%=path%>/resource/queryimage/images/tab_20.gif" width="16" height="35" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<%
			}
		%>


	</body>
</html>
