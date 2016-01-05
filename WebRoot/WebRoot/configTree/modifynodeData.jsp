<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
String text=" ";
String descn=" ";
String node=" ";
int nodeid=0;
if (request.getAttribute("text")!=null) 
{ 
    text=request.getAttribute("text").toString(); 
    descn=request.getAttribute("descn").toString();
    node=request.getAttribute("nodeid").toString();
}else out.print("ERROR");
nodeid=Integer.parseInt(node);
System.out.print("===============sd"+nodeid);
GridDS gridds=new GridDS();
Boolean modifynodesuccess=true;
try{

	modifynodesuccess =gridds.modifyNode(text,descn,nodeid);
	
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
if(<%=modifynodesuccess%>){
Ext.onReady(function(){

Ext.MessageBox.alert("修改提示框","修改成功！",function(){window.location.href="configureTree.do?action=homepage";self.parent.Ext.getCmp("menuTree").root.reload();});

});   
}else{
Ext.MessageBox.alert("修改提示框","修改失败，请重新修改！");
}
</script>
</head>
<body>
</body>
</html>