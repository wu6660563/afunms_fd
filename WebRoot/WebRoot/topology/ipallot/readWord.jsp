<%@ page contentType="application/msword; charset=gb2312"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>

<%
	String[] value = (String[]) request.getAttribute("value");
	String key = (String) request.getAttribute("key");
%>
<html>
	<head>
		<title>ip分配信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
	</head>
	<body id="body" class="body">
		<form id="mainForm" method="post" name="mainForm">
			<table>
				<tr>
					<td colSpan="2" align="center" class="content-title">ip分配信息</td>
				</tr>
				<tr style="background-color: #FFFFFF;">
					<td align="center" class="body-data-title" width="15%">序号</td>
					<td align="center" class="body-data-title"><%=key%>.*网段已分配IP</td>
				</tr>
				<%
					for (int i = 0; i < value.length; i++) {
				%>
				<tr style="background-color: #FFFFFF;">
					<td align="center" class="body-data-list"><font color='blue'><%=i + 1%></font></td>
					<td align="center" class="body-data-list"><%=value[i]%></td>
				</tr>
				<%
					}
				%>
			</table>
		</form>
	</body>
</html>


