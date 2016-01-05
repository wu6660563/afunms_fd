<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
   String menu = request.getParameter("menu");
   session.setAttribute(SessionConstant.CURRENT_MENU,menu);
   String rootPath = request.getContextPath();
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
  <frameset name=search cols="130,*" frameborder="NO" border="0" framespacing="0">
    <frame name="leftFrame" noresize scrolling="NO" src="left.jsp">
    <frame name="rightFrame" src="<%=rootPath%>/inform.do?action=server_jsp">
  </frameset>
</html>