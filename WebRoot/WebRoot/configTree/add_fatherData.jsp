<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String rootPath = request.getContextPath();
String text=" ";
String descn=" ";
request.setCharacterEncoding("gb2312");// ������ĳ������������
response.setCharacterEncoding("gb2312");
if (request.getAttribute("text")!=null) 
{ 
    text=request.getAttribute("text").toString(); 
    descn=request.getAttribute("descn").toString();
}else out.print("ERROR");
GridDS gridds=new GridDS();
Boolean addsuccess=true;
try{

	addsuccess =gridds.insertNode(text,descn);
	
	//out.println("==============="+addsuccess);
		}catch (Exception e) {
			e.printStackTrace();
		}
%> 
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script>
if(<%=addsuccess%>){
Ext.onReady(function(){

Ext.MessageBox.alert("�����ʾ��","��ӳɹ���",function(){window.location.href="configureTree.do?action=homepage";self.parent.Ext.getCmp("menuTree").root.reload();});

});   
}else{
Ext.MessageBox.alert("�����ʾ��","���ʧ�ܣ���������ӣ�");
}
</script>
</head>
<body>

</body>
</html>