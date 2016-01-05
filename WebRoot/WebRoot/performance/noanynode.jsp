<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		
		<script>
		window.setTimeout(function(){
			document.getElementById("div").innerHTML = "无任何设备节点";
		}, 5000);
		</script>
	</head>
	
	<BODY>
		<div id="div"><div>
	</BODY>
</HTML>
