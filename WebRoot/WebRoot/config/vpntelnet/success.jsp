<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%  String rootPath = request.getContextPath(); %>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body background="<%=rootPath%>/resource/image/bg6.jpg">
  	<br><br><br><br><br><br>
  	<div align="center"><h3>信息保存成功</h3></div>
  	<div align="center"><input type="button" value="关 闭" onclick="window.close()"></div>
  </body>
</html>
