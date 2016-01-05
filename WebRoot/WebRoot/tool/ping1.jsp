 <%@ page language="java" contentType="text/html; charset=GBK" %>
 <%@ page  import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%String getIp=request.getParameter("ipaddress") ;
if(getIp==null) getIp="";
String rootPath = request.getContextPath();
%>
<html locale="true">
  <head>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<title>ping</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<script language="javascript">
function onLoad() {
  var width = 700;
  var height = 350;
  var swidth = window.screen.width;
  var sheight = window.screen.height;
  var str = "left=" + ((swidth - width)/2) + ",top=" + ((sheight - height)/2 - 20)+",width=" + width + ",height=" + height;
  str = str + ",toolbar=no,directories=no,status=no,location=no,resizable=true,scrollbars=yes,menubar=no";  
  window.open('<%=rootPath%>/tool/ping.jsp?ipaddress=<%=getIp%>','Ping',str);
  window.close();
  }
</script>
</head>
<body bgcolor="#F1F1F1" onload="onLoad();">
</body>
</html>

