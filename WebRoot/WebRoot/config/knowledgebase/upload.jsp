<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.jspsmart.upload.*" %>
<%@ page import="com.jspsmart.file.*" %>
<%

String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";

String fname = "";
String mypath=".."+request.getContextPath()+"/config/knowledgebase/attachfiles/";
    	SmartUpload mySmartUpload=new SmartUpload();
    	mySmartUpload.initialize(pageContext);//��ʼ��
    	mySmartUpload.upload();	//�ϴ�
    	mySmartUpload.save(mypath);//����·��
    	for(int i=0;i<mySmartUpload.getFiles().getCount();i++){
    		File myfile=mySmartUpload.getFiles().getFile(i);//����ļ�
    		fname=myfile.getFileName();//����ļ���
    		request.setAttribute("ffname",fname);
    		session.setAttribute("upfname",fname);
    		
    	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>�ϴ��ɹ�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- 
	<script type="text/javascript">
		setTimeout('window.close()','2000') 
	</script>
	 -->
  </head>
  
  <body onload="show()">
  </body>
  	 <script language="JavaScript" type="text/javascript"> 
	 	function show(){
	 		parent.opener.document.getElementById("aaa").value="<%=fname%>";
	 		window.parent.close();
	 	}
	</script> 
	
	
	<script type="text/javascript">
		setTimeout('window.close()','1');
	</script>
</html>
