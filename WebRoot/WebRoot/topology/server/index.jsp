<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
  String rootPath = request.getContextPath();  
  
  String menu = request.getParameter("menu");
  if(menu!=null)
     session.setAttribute(SessionConstant.CURRENT_MENU,menu); 
   
  session.setAttribute(SessionConstant.CURRENT_TOPO_VIEW,"server.jsp");
%>
<html>
<head>
<title>dhcnms</title>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>  
<frameset rows="30,*" cols="*" frameborder="NO" border="0" framespacing="0" noresize>
  <frame name="topFrame" scrolling="NO" noresize src="top.jsp" marginheight="0" marginwidth="0">
  <frameset name=search cols="0,*" frameborder="NO" border="0" framespacing="0" rows="*">
    <frame name="leftFrame" src="tree.jsp?treeflag=0">
    <frame name="mainFrame" src="server.jsp">
  </frameset>
</frameset>
</html>
