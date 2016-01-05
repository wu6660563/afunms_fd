<%@page language="java" contentType="text/html;charset=gb2312"%>
<% 
String rootPath = request.getContextPath();
String s="";
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
s=request.getAttribute("DID").toString();
//System.out.println("================="+s);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script>
Ext.onReady(function(){
	   var sm=new Ext.grid.CheckboxSelectionModel();
       var cm=new Ext.grid.ColumnModel([  //设置表头
       new Ext.grid.RowNumberer(),
       sm,
       	{header:'编号',dataIndex:'id'},
       	{header:'名称',dataIndex:'text'},
       	{id:'descn',header:'描述',dataIndex:'descn'}
      // 	{header:'操作',width:150,dataIndex:'handle',menuDisabled:true,
      // 	renderer:function(v){
     //  	var item = grid.getSelectionModel().getSelected();
      // 	return "<span style='margin-right:10px'><a onclick='configureTree.do?action=modifynode&nodeid="+item.get('id')+"'>修改配置项</a></span>";}
    //  	}
       ]);  
       var MyRecord=Ext.data.Record.create([
       		{name:'id'},
       		{name:'text'},
       		{name:'descn'}
       	]);
        var ds=new Ext.data.Store({ //读取数据
        proxy:new Ext.data.HttpProxy({url:"configureTree.do?action=iframeData&DID="+<%=s%>}),
        reader:new Ext.data.JsonReader({
        	id:'id',
        	text:'text',
        	descn:'descn'
        },MyRecord)	
        });
        var grid=new Ext.grid.GridPanel({
            // autoExpandColumn:"descn"//最后一列占满整个剩余的列空间
        	el:'grid',
        	ds:ds,
        	autoHeight:true,
        	//TrackMouseOver:"true", 
        	Border:"true",
        	cm:cm,
        	sm:sm,  
        	tbar:['-',{
        			 text:'修改配置项&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("请选择一个配置项");   
        									}else{window.location.href="configureTree.do?action=modifynode&nodeid="+item.get('id');      
                								}
                						},
        			 scope:this
        			},'-',{
        			 text:'删除配置项&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("请选择一个配置项");   
        									}else{window.location.href="configureTree.do?action=delenodeData&nodeid="+item.get('id');      
                								}
                						},
        			 scope:this
        			}]    	  	
        });
        grid.render(); 
        ds.load();
        
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