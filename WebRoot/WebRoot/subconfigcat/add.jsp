<%@page language="java" contentType="text/html;charset=gb2312"%>

<%  
   String rootPath = request.getContextPath();  
 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script language="javascript">
  function toAdd()
  {
     var chk1 = checkinput("ip_address","ip","IP地址",15,false);
     var chk2 = checkinput("alias","string","机器名",30,false);
     var chk3 = checkinput("community","string","共同体",30,false);
     
     if(chk1&&chk2&&chk3)
     {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/network.do?action=dnsadd";
        mainForm.submit();
     }
  }
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     //var chk1 = checkinput("ip_address","ip","名称",15,false);
     //var chk2 = checkinput("alias","string","描述",30,false);
     //var chk3 = checkinput("community","string","用户名",30,false);
     
     //if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/dns.do?action=add";
        mainForm.submit();
     }  
       // mainForm.submit();
 });	
	
});
function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
function toAdd()
  {  
   //定义错误信息变量
       var error = "";
       //保存信息
       var name,desc;
       
        name = document.mainForm.name.value;
        desc = document.mainForm.desc.value;
          if (name == "")
       {
          error += "名称不能为空！\n";
       }
       if (desc == "")
       {
          error += "描述不能为空！\n";
       }
       
        
       if (error == "")
       {
            error = "提交信息如下:\n";
            error += "名称:"+name+"\n";
            error += "描述:"+desc+"\n";
          mainForm.action = "<%=rootPath%>/subconfigcat.do?action=add";
         mainForm.submit();
        
       }else{
       		alert(error);
       }
   
  }

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>

<form name="mainForm" method="post"  >
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center >
			<%=menuTable%>
		</td>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;>> </td>
				</tr>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
				<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
						<TBODY>
						<tr><td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td></tr>
				
					
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="20%">名称&nbsp;</TD>				
			<TD nowrap align="left" width="40%">&nbsp;<input type="text" name="name" size="25" class="formStyle"><font color="red">&nbsp;*</font></TD></tr>														
			<tr>
			<TD nowrap align="right" height="24">描述&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<textarea  name="desc"  rows="6" cols="40" onpropertychange="if(value.length>1000) value=value.substr(0,1000)" class="smallArea"  ></textarea><font color="red">*限500字以内</font></TD>						
			</tr>
			
	      	
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr align=center>
							<TD nowrap colspan="4">
								<br>
								<input type="button" value="保存" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="返回" onclick="javascript:history.back(1)">
							</TD>	
						</tr>
					</TBODY>
				</TABLE>				
				</td>
			</tr>			
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>