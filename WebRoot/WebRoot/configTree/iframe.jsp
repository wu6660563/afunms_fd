<%@page language="java" contentType="text/html;charset=gb2312"%>
<% 
String rootPath = request.getContextPath();
String s="";
request.setCharacterEncoding("gb2312");// ������ĳ������������
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
       var cm=new Ext.grid.ColumnModel([  //���ñ�ͷ
       new Ext.grid.RowNumberer(),
       sm,
       	{header:'���',dataIndex:'id'},
       	{header:'����',dataIndex:'text'},
       	{id:'descn',header:'����',dataIndex:'descn'}
      // 	{header:'����',width:150,dataIndex:'handle',menuDisabled:true,
      // 	renderer:function(v){
     //  	var item = grid.getSelectionModel().getSelected();
      // 	return "<span style='margin-right:10px'><a onclick='configureTree.do?action=modifynode&nodeid="+item.get('id')+"'>�޸�������</a></span>";}
    //  	}
       ]);  
       var MyRecord=Ext.data.Record.create([
       		{name:'id'},
       		{name:'text'},
       		{name:'descn'}
       	]);
        var ds=new Ext.data.Store({ //��ȡ����
        proxy:new Ext.data.HttpProxy({url:"configureTree.do?action=iframeData&DID="+<%=s%>}),
        reader:new Ext.data.JsonReader({
        	id:'id',
        	text:'text',
        	descn:'descn'
        },MyRecord)	
        });
        var grid=new Ext.grid.GridPanel({
            // autoExpandColumn:"descn"//���һ��ռ������ʣ����пռ�
        	el:'grid',
        	ds:ds,
        	autoHeight:true,
        	//TrackMouseOver:"true", 
        	Border:"true",
        	cm:cm,
        	sm:sm,  
        	tbar:['-',{
        			 text:'�޸�������&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("��ѡ��һ��������");   
        									}else{window.location.href="configureTree.do?action=modifynode&nodeid="+item.get('id');      
                								}
                						},
        			 scope:this
        			},'-',{
        			 text:'ɾ��������&nbsp;&nbsp',
        			 handler: function(){   
       				 						var item = grid.getSelectionModel().getSelected();   
       										if(!item){   
                							Ext.Msg.alert("��ѡ��һ��������");   
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