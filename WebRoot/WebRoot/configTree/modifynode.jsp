<%@page language="java" contentType="text/html;charset=gb2312"%>
<% 
String rootPath = request.getContextPath();
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
String s="";
s=request.getAttribute("nodeid").toString();
//System.out.println("======================"+s);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" />
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"></script>
<script type="text/javascript">Ext.BLANK_IMAGE_URL = '<%=rootPath%>/js/s.gif';</script>
<script> 
Ext.onReady(function(){
  Ext.QuickTips.init();
  var form=new Ext.FormPanel({
     frame:true,
     width:300,
     monitorValid:true,//绑定验证
     layout:"form",
     labelWidth:70,
     title:"修改配置项信息",
     labelAlign:"left",
     renderTo:"form",
     submit: function(){
                    this.getEl().dom.action ='configureTree.do?action=modifynodeData&nodeid='+<%=s%>,
                    this.getEl().dom.method='POST',
                    this.getEl().dom.submit();
              },
     items:[{
               xtype:"textfield",
               fieldLabel:"配置项名",
               id:"text",
               allowBlank:false,
               blankText:"不能为空，请正确填写",
               name:"text",
               anchor:"90%"
           },{
               xtype:"textarea",
               fieldLabel:"描述",
               id:"descn",
               name:"descn",
               anchor:"90%"
           }],
     buttons:[{text:"确定",handler:function(){form.form.submit();},formBind:true},{text:"取消",handler:function(){form.form.reset();}},{text:"回到配置首页",handler:function(){window.location.href="configureTree.do?action=homepage";}}]           
       });
});

</script>
   </head>   
    <body>   
<table width="100%"   border="0" cellpadding="0" cellspacing="0" > 
<tr> 
   <td align=center valign="top" >    
      <div id="form" style="height:1000px;width:17%" ></div>   
   </td> 
</tr>
</table>     
    </body>    
</html> 
