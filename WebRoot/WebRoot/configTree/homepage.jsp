<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script>  
Ext.onReady(function(){
	   var sm=new Ext.grid.CheckboxSelectionModel();
       var cm=new Ext.grid.ColumnModel([
       new Ext.grid.RowNumberer(),
       sm,
       	{header:'编号',dataIndex:'id',sortable:true},
       	{header:'名称',dataIndex:'text'},
       	{id:'descn',header:'描述',dataIndex:'descn'}
       //	{header:'操作',width:150,dataIndex:'handle',menuDisabled:true,
       	//renderer:function(v){return "<span style='margin-right:10px'><a href='#'>修改</a></span><span><a href='#'>删除</a></span>";}
       //	}
       ]);  
       var MyRecord=Ext.data.Record.create([
       		{name:'id'},
       		{name:'text'},
       		{name:'descn'}
       	]);
        var ds=new Ext.data.Store({
        proxy:new Ext.data.HttpProxy({url:"configureTree.do?action=gridData"}),
        reader:new Ext.data.JsonReader({
        	root:'results',
        	totalProperty:'count',
        	id:'id',
        	text:'text',
        	descn:'descn'
        },MyRecord)	
        });
        var grid=new Ext.grid.GridPanel({
            // autoExpandColumn:"descn"//最后一列占满整个剩余的列空间
        	el:'grid',
        	ds:ds,
        	//title:'配置项',
        	autoHeight:true,
        	TrackMouseOver:"true", 
        	Border:"true",
        	cm:cm,
        	sm:sm,
        	tbar:['-',{text:'添加配置项大类别&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=add_father";},
        			 scope:this
        			},'-',{
        			text:'添加配置项小类别&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=add_childnode";},
        			 scope:this
        			},'-',{text:'删除配置项类别&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=delenode";},
        			 scope:this
        			},'-',{
        			 text:'修改配置项&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("请选择一个配置项");   
        									}else{window.location.href="configureTree.do?action=modifynode&nodeid="+item.get('id');      
                								}
                						},
        			 scope:this
        			}],
        bbar:new Ext.PagingToolbar({   //显示分页栏
        				pageSize:10,
        				store:ds,
        				displayInfo:true,
        				displayMsg:'显示第{0}条到第{1}条记录，一共{2}条',
        				emptyMsg:'没有记录'
        				
        			})
        });
        grid.render(); 
        ds.load({params:{start:0,limit:10}});
        
});      
    </script>   
   </head>   
    <body>   
<table width="100%"   border="0" cellpadding="0" cellspacing="0" > 
<tr> 
   <td align=left valign="top" >     
      <div id="grid" style="height:1000px;width:100%" ></div> 
   </td> 
</tr>
</table>     
    </body>    
</html>