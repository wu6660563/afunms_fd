<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Boolean flag = (Boolean) request.getAttribute("flag");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <%if(flag) {
    	//关联成功，关联关系存在nms_alarm_device_dependence表里，需要自己设定基准设备
    	//隐藏该功能，目的防止失误修改基准设备
    	//拓扑图创建完毕，关联之后，该功能则无需使用
    	out.print("关联成功！");
    } %>
  </body>
</html>
