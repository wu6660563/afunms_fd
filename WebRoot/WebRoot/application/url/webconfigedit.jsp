<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%  
   String rootPath = request.getContextPath();  
   BusinessDao bussdao = new BusinessDao();
   List allbuss = bussdao.loadAll();  
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
     var chk1 = checkinput("ip_address","ip","IP��ַ",15,false);
     var chk2 = checkinput("alias","string","������",30,false);
     var chk3 = checkinput("community","string","��ͬ��",30,false);
     
     if(chk1&&chk2&&chk3)
     {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/network.do?action=webadd";
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
  
     //var chk1 = checkinput("ip_address","ip","����",15,false);
     //var chk2 = checkinput("alias","string","����",30,false);
     //var chk3 = checkinput("community","string","�û���",30,false);
     
     //if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/web.do?action=add";
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
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//��Ӳ˵�	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
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
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center >
			<%=menuTable%> 
		
		</td>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;�豸ά�� >> ����豸</td>
				</tr>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
				<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="name" maxlength="50" size="20" class="formStyle">
							</TD>
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">·��&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="str" maxlength="50" size="50" class="formStyle" value="http://">
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">��ʱ(����)&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="timeout" maxlength="50" size="20" class="formStyle">
							</TD>		
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">�Ƿ���&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select   name="flag"  class="formStyle">
									<option value=0>��</option>
									<option value=1>��</option>
								</select>
							</TD>															
						</tr>
						<tr style="background-color: #ECECEC;"> 
  							<TD nowrap align="right" height="24" width="10%">���Ž�����&nbsp;</TD>       
							<TD nowrap width="40%" colspan=3>&nbsp;
							<input type="text" name="sendmobiles" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="���ö��Ž�����" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setwebmobiles')"> 
							</td>
 						</tr>
    						<tr> 
  							<TD nowrap align="right" height="24" width="10%">�ʼ�������&nbsp;</TD>       
							<TD nowrap width="40%" colspan=3>&nbsp;
							<input type="text" name="sendemail" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setwebemail&flag=db')">
							</td>
 						</tr> 
						<tr>						
							<TD nowrap align="right" height="24" width="10%">����ҵ��&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        					<tbody>  
                        									<% if( allbuss.size()>0){
           												for(int i=0;i<allbuss.size();i++){
           													Business buss = (Business)allbuss.get(i);
                        									%>                  										                        								
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=buss.getId()%>" >&nbsp;<%=buss.getName()%></td>
                    										</tr>  
                    										<%
                    											}
                    										}
                    										%>                  										                 										                      								
            							</tbody>
            							</table>
							</TD>																			
						</tr> 						 								
						<tr>
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">��Ҫһ��ʱ��,���Ժ�...</font></div>
							</td>
						</tr>			

						<tr>
							<TD nowrap colspan="4" align=center>
							<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
								<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
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