<%@page language="java" contentType="text/html;charset=gb2312"%>
<%
  String rootPath = request.getContextPath();
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body bgcolor='white'>
<table cellpadding="0" cellspacing="0" width=100% align='center'>
  <tr height='30'>
    <td><a href="<%=rootPath%>/inform.do?action=server_jsp" target='rightFrame'>���������ܱ���</a></td>
  </tr>  
  <tr height='30'>
    <td><a href="<%=rootPath%>/inform.do?action=network_jsp" target='rightFrame'>�����豸���ܱ���</a></td>
  </tr>   
  <tr height='30'>
    <td><a href="<%=rootPath%>/inform.do?action=virus_jsp" target='rightFrame'>���������ݱ���</a></td>
  </tr>
  <tr height='30'>
    <td><a href="<%=rootPath%>/inform/report/allreport.jsp" target='rightFrame'>�ۺϱ���</a></td>
  </tr>       
</table>
</body>
</html>