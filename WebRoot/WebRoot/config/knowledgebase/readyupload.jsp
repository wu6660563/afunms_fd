<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>�����ϴ�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
		<script type="text/javascript">
		/**
	*�����չ��
	*/
		function check(){
			var upfilename=document.uploadform.upload.value;
			if(upfilename==""){
			   alert("�㻹û��ѡ����Ҫ�ϴ����ļ�!");
			   return false;
			  } 
			  var filetype=upfilename.substring(upfilename.indexOf(".")+1,upfilename.length).toLowerCase();
			  var fileName=upfilename.substring(0,upfilename.indexOf("."));
			  /**
	*�����ϴ�����
	*/
			  if(filetype!="jpg"&&filetype!="gif"&&filetype!="bmp"&&filetype!="zip" &&filetype!="rar" &&filetype!="doc"&&filetype!="xls"&&filetype!="pdf")
			  {
			     alert("ֻ֧��ͼƬ��:jpg,gif,bmp;�ĵ���:word,excel,pdf;ѹ����:rar,zip!!!");
			     return false;
			  }
			  
			  window.uploadform.action="<%=rootPath%>/config/knowledgebase/upload.jsp";
			  window.uploadform.submit();
		}	
	</script>
	
	
  </head>
  
  <body>
  	<form method="post" enctype="multipart/form-data" target="_self" name="uploadform">
    <h2 align="center">ѡ����Ҫ�ϴ��ķ���</h2> <br>
    ������<input type="file" name="upload">
    	<input type="button" value="�ϴ�" align="middle" onclick="check()">
    	<br><br>
    	<div align="center"><font color="red" style="font-size: 13">&nbsp;</font></div>
  	</form>
  </body>
</html>
