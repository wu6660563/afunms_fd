<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// ������ĳ������������
response.setCharacterEncoding("gb2312");
%>
<html>
<head>
<title>������</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script type="text/javascript">Ext.BLANK_IMAGE_URL = '<%=rootPath%>/js/s.gif';</script>
<script> 
var root=new Ext.tree.AsyncTreeNode({ 
        id:'0', 
        text:'�������б�'       
    });
var menuTree = new Ext.tree.TreePanel({
	id:'menuTree',
	region:'west',
	title:'������˵�',
	width:180,
	//minSize:150,
	maxSize:180,
	frame : true, //��������
	autoScroll:true,
	containerScroll:true,
	autoHeight:false,
	collapsible:true,
	root:root,
	loader:new Ext.tree.TreeLoader({dataUrl:'configureTree.do?action=treeData&DID=0'}),
	listeners:   
    {   
     'click':   
       function( node, e )   
        {//alert("sds"+node.id);
        e.stopEvent();
        var n=contentPanel.getComponent(node.id);
        if(!n){
        		n=contentPanel.add({
        		'id':node.id,
        		'title':node.text,
        		closable:true,
        		//autoLoad:{url:'iframe.html',scripts:true}//�޷�ʹ���Զ�װ��ҳ��,��̬������
        		html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="configureTree.do?action=iframe&DID='+node.id+'"></iframe>'
        		});
        		}
        		contentPanel.setActiveTab(n);
        }   
   }
});
//�ұ߾��幦�������
var contentPanel = new Ext.TabPanel({
	region:'center',
	layoutOnTabChange:true,
	enableTabScroll:true,
	activeTab:0,
	items:[{
		id:'homePage',
		title:'���������',
		//autoScroll:true,
		//autoLoad:{url:'homepage.jsp',script:true}  //�Զ����أ����htmlռ���ڴ���
		html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="configureTree.do?action=homepage"></iframe>'
	}] 
});

Ext.onReady(function(){
	new Ext.Viewport({
		layout:'border', //ʹ��border����
		defaults:{activeItem:0},
		items:[menuTree, contentPanel]
	});	
	menuTree.on( 'beforeload', function(node){ 
           menuTree.loader.dataUrl='configureTree.do?action=treeData&DID='+node.id;//ÿ���ڵ��Ӧ��ҳ��   
           });
   menuTree.setRootNode(root); 
   menuTree.render();            
   root.expand();
});
</script>
</head>
<body>
</body>
</html>




