<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.Menu"%>
<%
  Menu vo = (Menu)request.getAttribute("vo");
  String rootPath = request.getContextPath();
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="JavaScript" type="text/javascript">
  function toAdd()
  {
     var chk1 = checkinput("title","string","�˵�",14,false);
     var chk2 = checkinput("url","string","URL",100,false);

     if(chk1&&chk2)
     {
        mainForm.action = "<%=rootPath%>/menu.do?action=update_sub";
        mainForm.submit();
     }
  }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
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

<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=vo.getId()%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16">��</td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background="<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>��</td>
				<td height=300 bgcolor="#FFFFFF" valign="top"  align="center">
				  <table border="0" id="table1" cellpadding="0" cellspacing="1"
						width="95%">
					<TBODY>
						<tr>
							<TD align="left" colspan="4" height="24">�����˵�--�༭</TD>
						</tr>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr style="background-color: #ECECEC;">
						    <TD nowrap align="right" height="24" width="10%">�����˵�&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="title" size="20" class="formStyle" value="<%=vo.getTitle()%>">
							<font color="red">&nbsp;*</font></TD>
						</tr>
						<tr>
						    <TD nowrap align="right" height="24" width="10%">URL&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="url" size="30" class="formStyle" value="<%=vo.getUrl()%>">
							<font color="red">&nbsp;*</font></TD>
						</tr>
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="4">
								<br>
								<input type="button" value="����" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="����" onclick="javascript:history.back(1)">
							</TD>	
						</tr>						
					</TBODY>
				</TABLE>
				</td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13 colspan="3">��</td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>	
</body>
</html>