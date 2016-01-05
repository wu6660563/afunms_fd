<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.polling.loader.HostLoader"%>
<html>
<head>
<%
	String rootPath = request.getContextPath();  
	User user=(User)session.getAttribute(SessionConstant.CURRENT_USER);
 	String fileName=user.getUserid()+"_"+user.getId()+"_interface.xml";  
 	String id=request.getParameter("id");
 	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id)); 
    	if(host == null){
    		//�����ݿ����ȡ
    		HostNodeDao hostdao = new HostNodeDao();
    		HostNode node = null;
    		try{
    			node = (HostNode)hostdao.findByID(id);
    		}catch(Exception e){
    		}finally{
    			hostdao.close();
    		}
    		HostLoader loader = new HostLoader();
    		loader.loadOne(node);
    		loader.close();
    		host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id)); 
    	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/InterfaceControler.js"></script>
<title>����</title>
</head>
<body style="text-align: center" onload="generateData();" bgcolor="#0c2869">
<h2>�豸��Ϣ</h2>
<table width="100%" align="center">
	<tr>
		<td align="right">�豸��ǩ:</td><td align="left">&nbsp;<%=host.getAlias()%></td>
		<td align="right">ϵͳ����:</td><td align="left">&nbsp;<%=host.getSysName()%></td>
	</tr>
	<tr>
		<td align="right">IP&nbsp;��&nbsp;ַ:</td> <td align="left">&nbsp;<%=host.getIpAddress()%></td> 
		<td align="right">��&nbsp;&nbsp;&nbsp;&nbsp;��:</td> <td align="left">&nbsp;<%=host.getType()%></td>
	</tr>
</table>
<br>
<div id="flashcontent"></div>
<script type="text/javascript">
 // <![CDATA[ 
	var regenerate_data_interval = 5000;
	var delayPrint_time = 500;
	var datafileName="<%=fileName%>";
	var blackFlag=true; //������ʾ�Ƿ�Ҫ���ɿ��ļ� 
	oTimer=window.setInterval('generateData()',regenerate_data_interval); 
	window.setTimeout('printChart()',delayPrint_time);
	function generateData(){   
      var nodeID=<%=request.getParameter("id")%>;
      DWREngine.setAsync(false);//����ͬ��
      InterfaceControler.generateData(datafileName,blackFlag,nodeID,{
      						callback:callback,//�ص�����
      						timeout:5000,//��ʱʱ��
      						errorHandler:function(message) { alert("ERROR: " + message);}
      						}); 
	}  
	function callback(result){
		if(null!=result&&"success"==result){//�����ļ��ɹ�
			blackFlag=false;//�������ɿ��ļ� 
		}else{
			//�����ļ�ʧ��
			window.clearInterval(oTimer);
			alert("���������ļ�ʧ��:"+result);
		}
	}
	function printChart(){
		var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline", "650", "300", "8", "#fffff");
		so.addVariable("path", "<%=rootPath%>/amchart/");
		so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/interface_setting.xml"));
		so.addVariable("data_file", "./amcharts_data/"+datafileName);
		so.write("flashcontent");
	}
		// ]]>
</script>
</body>
</html>
