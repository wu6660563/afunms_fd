<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// ������ĳ������������
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
       	{header:'���',dataIndex:'id',sortable:true},
       	{header:'����',dataIndex:'text'},
       	{id:'descn',header:'����',dataIndex:'descn'}
       //	{header:'����',width:150,dataIndex:'handle',menuDisabled:true,
       	//renderer:function(v){return "<span style='margin-right:10px'><a href='#'>�޸�</a></span><span><a href='#'>ɾ��</a></span>";}
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
            // autoExpandColumn:"descn"//���һ��ռ������ʣ����пռ�
        	el:'grid',
        	ds:ds,
        	//title:'������',
        	autoHeight:true,
        	TrackMouseOver:"true", 
        	Border:"true",
        	cm:cm,
        	sm:sm,
        	tbar:['-',{text:'�������������&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=add_father";},
        			 scope:this
        			},'-',{
        			text:'���������С���&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=add_childnode";},
        			 scope:this
        			},'-',{text:'ɾ�����������&nbsp;&nbsp',
        			 handler:function(){window.location.href="configureTree.do?action=delenode";},
        			 scope:this
        			},'-',{
        			 text:'�޸�������&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("��ѡ��һ��������");   
        									}else{window.location.href="configureTree.do?action=modifynode&nodeid="+item.get('id');      
                								}
                						},
        			 scope:this
        			}],
        bbar:new Ext.PagingToolbar({   //��ʾ��ҳ��
        				pageSize:10,
        				store:ds,
        				displayInfo:true,
        				displayMsg:'��ʾ��{0}������{1}����¼��һ��{2}��',
        				emptyMsg:'û�м�¼'
        				
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