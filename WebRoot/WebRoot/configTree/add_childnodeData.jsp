<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
String text=" ";
String descn=" ";
String fathernode=" ";
int father_id=0;
if (request.getAttribute("text")!=null) 
{ 
    text=request.getAttribute("text").toString(); 
    descn=request.getAttribute("descn").toString();
    fathernode=request.getAttribute("fathernode").toString();
}else out.print("ERROR");
father_id=Integer.parseInt(fathernode);GridDS gridds=new GridDS();
Boolean addnodesuccess=true;
try{
	addnodesuccess=gridds.insertChildNode(text,descn,father_id);
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
if(<%=addnodesuccess%>){
Ext.onReady(function(){
Ext.MessageBox.alert("添加提示框","添加成功！",function(){window.location.href="configureTree.do?action=homepage";self.parent.Ext.getCmp("menuTree").root.reload();});
});   
}else{
Ext.MessageBox.alert("添加提示框","添加失败，请重新添加！");
}
</script>
</head>
<body>
</body>
</html>