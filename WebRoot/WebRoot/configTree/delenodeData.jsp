<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// ������ĳ������������
response.setCharacterEncoding("gb2312");
String fathernode="";
int nodeid=0;
if (request.getAttribute("nodeid")!=null) 
{ 
    fathernode=request.getAttribute("nodeid").toString();
}else out.print("ERROR");
nodeid=Integer.parseInt(fathernode);
//out.print("======================="+nodeid);
GridDS gridds=new GridDS();
Boolean delenodesuccess=true;
try{
	delenodesuccess=gridds.deleNode(nodeid);
	//out.println("==============="+delenodesuccess);
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
if(<%=delenodesuccess%>){
Ext.onReady(function(){

Ext.MessageBox.alert("ɾ����ʾ","ɾ���ɹ���",function(){window.location.href="configureTree.do?action=homepage";self.parent.Ext.getCmp("menuTree").root.reload();});

});   
}else{
Ext.onReady(function(){
Ext.MessageBox.alert("ɾ����ʾ","ɾ��ʧ�ܣ�������ɾ����",function(){window.location.href="configureTree.do?action=homepage";});
});
}
</script>
</head>
<body>
</body>
</html>