<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
  String rootPath = request.getContextPath();  
  
  String menu = request.getParameter("menu");
  if(menu!=null){
     session.setAttribute(SessionConstant.CURRENT_MENU,menu); 
  }
  User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  String bids[] = current_user.getBusinessids().split(","); 
  String rightFramePath = request.getParameter("rightFramePath");
%>
<html>
<head>
<title>�豸����</title>  
</head>  
  <frameset name=search  rows="*" cols="199,*" framespacing="0" rows="*" frameborder="no">
    <frame name="leftFrame" frameborder="no" src="tree.jsp?treeflag=0&rightFramePath=<%=rightFramePath%>">
    <frame name="rightFrame" frameborder="no" id="rightFrame" src="#">
  </frameset>
</html>
