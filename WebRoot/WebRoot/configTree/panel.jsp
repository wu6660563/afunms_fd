<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
%>
<html>
<head>
<title>配置项</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script type="text/javascript">Ext.BLANK_IMAGE_URL = '<%=rootPath%>/js/s.gif';</script>
<script> 
var root=new Ext.tree.AsyncTreeNode({ 
        id:'0', 
        text:'配置项列表'       
    });
var menuTree = new Ext.tree.TreePanel({
	id:'menuTree',
	region:'west',
	title:'配置项菜单',
	width:180,
	//minSize:150,
	maxSize:180,
	frame : true, //美化界面
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
        		//autoLoad:{url:'iframe.html',scripts:true}//无法使用自动装载页面,动态树生成
        		html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="configureTree.do?action=iframe&DID='+node.id+'"></iframe>'
        		});
        		}
        		contentPanel.setActiveTab(n);
        }   
   }
});
//右边具体功能面板区
var contentPanel = new Ext.TabPanel({
	region:'center',
	layoutOnTabChange:true,
	enableTabScroll:true,
	activeTab:0,
	items:[{
		id:'homePage',
		title:'配置项管理',
		//autoScroll:true,
		//autoLoad:{url:'homepage.jsp',script:true}  //自动加载，相比html占用内存少
		html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="configureTree.do?action=homepage"></iframe>'
	}] 
});

Ext.onReady(function(){
	new Ext.Viewport({
		layout:'border', //使用border布局
		defaults:{activeItem:0},
		items:[menuTree, contentPanel]
	});	
	menuTree.on( 'beforeload', function(node){ 
           menuTree.loader.dataUrl='configureTree.do?action=treeData&DID='+node.id;//每个节点对应的页面   
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




