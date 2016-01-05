
<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%
Date c1=new Date();
String rootPath = request.getContextPath();
String timeFormat = "yyyy-MM-dd";
java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);

%>

<html locale="true">
  <head>
<title>声音控制窗口</title>
	
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">



 <link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" src="<%=rootPath%>/include/prototype.js"></script>
     <script language="JavaScript" src="<%=rootPath%>/include/buffalo.js"></script>
     <script language="javascript" src="<%=rootPath%>/include/alertalarm.js"></script>
     <script type="text/javascript">
     	var MSG1=null;
     	setInterval(alertAlarm,10000);
     	
     var endPoint="<%=request.getContextPath()%>/bfapp";
    
     var buffalo = new Buffalo(endPoint);
         buffalo.remoteCall("alertAlarm.readAlertAlarm",[], function(reply){ 
         	allAlertAlarm = reply.getResult(); 
         //alert(reply.getResult());
         })
     //setInterval(alertAlarm,10000);	
     </script>

</head>
   <body>
     声音告警窗口    
     <input type="button" value="关闭窗口" onclick="window.close();"><br>
   </body>

</html>
