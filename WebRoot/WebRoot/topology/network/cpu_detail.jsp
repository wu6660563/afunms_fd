<%@page language="java" contentType="text/html;charset=gb2312" %>
<%
	String rootPath = request.getContextPath();   
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/Controler.js"></script>
<title>CPU ������</title>
</head>
<body style="text-align: center">
<div id="flashcontent"></div>
<script type="text/javascript">
 // <![CDATA[ 
	var regenerate_data_interval = 1000;
	var datafileName="data.xml";
	window.setInterval('generateData()',regenerate_data_interval); 
	
	function generateData(){       
      DWREngine.setAsync(false);//����ͬ��   
      Controler.generateData(datafileName,{
      						callback:callback,//�ص����� 
      						timeout:5000,//��ʱʱ��
      						errorHandler:function(message) { alert("ERROR: " + message);}
      						}); 
	}  
	function callback(result){
		if(null!=result&&"success"==result){
			//�����ļ��ɹ���������		
		}else{
			//�����ļ�ʧ��
			alert("Failed MSG:"+result);
		}
	}
	var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline", "600", "300", "8", "#fffff");
	so.addVariable("path", "<%=rootPath%>/amchart/");
	so.addVariable("settings_file", escape("./amcharts_settings/setting.xml"));
	so.addVariable("data_file", "<%=rootPath%>/"+datafileName);
	so.write("flashcontent");			
		// ]]>
</script>
</body>
</html>
 