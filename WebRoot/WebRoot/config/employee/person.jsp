<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
   UserView view = new UserView();
   User vo = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   String rootPath = request.getContextPath();
%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  function toAdd()
  {
     if(mainForm.password0.value!=mainForm.password.value)
     {
        alert("����������ȷ�����벻ͬ������������!");
        mainForm.password.focus();
        return false;
     }

     var chk1 = checkinput("name","string","����",50,false);
     var chk2 = checkinput("password","string","����",15,true);
     var chk3 = checkinput("password0","string","����",15,true);
     var chk4 = checkinput("phone","string","�绰",30,true);
     var chk5 = checkinput("mobile","string","�ֻ�",30,true);
     var chk6 = checkinput("email","string","Email",30,true);

     if(chk1&&chk2&&chk3&&chk4&&chk5&&chk6)
     {
        mainForm.action = "saveok.jsp";
        mainForm.submit();
     }
  }
</script>
<script language="javascript">	
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function doCancelManage()
  {  
     mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }


</script>
<script language="JavaScript" type="text/javascript">
  function toCheck()
  {
     if(FrmDeal.pwd.value=="")
     {
        alert("<����>����Ϊ��!");
        FrmDeal.pwd.focus();
        return false;
     }
     FrmDeal.action = "check.jsp";
     FrmDeal.submit();
  }
</script>
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		</td>
		<td bgcolor="#ffffff" align="center" valign=top>
		<table width=100% bgcolor=#ffffff>
		<tr>
		<td>
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=4>&nbsp;&nbsp;<font color=#ffffff>��Դ���� >> �޸ĸ�������</font></td>
				</tr>
				<tr>					
					<td height=50 bgcolor="#FFFFFF" valign="top" align=center valign=center>
					<input type="hidden" name="id" value="<%=vo.getId()%>">
					<input type="hidden" name="category" value="25">
					<input type="hidden" name="code" value="DATABASE">					
					<TBODY>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr style="background-color: #ECECEC;">
						    <TD nowrap align="right" height="24" width="10%">�ʺ�&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<%=vo.getUserid()%></TD>
							<TD nowrap align="right" height="24">����&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input type="text" name="name" value="<%=vo.getName()%>" size="16" class="formStyle"><font color="red">&nbsp;*</font>
							</TD>	
						</tr>
						<tr>	
							<TD nowrap align="right" height="24">����&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input name="password" type="password" size="16"  class="formStyle"><font color="red">&nbsp;(�����ʾ���޸�)</font>
							</TD>						
							<TD nowrap align="right" height="24">ȷ������&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="password0" type="password" size="16" class="formStyle">														
							</TD>	
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">�Ա�&nbsp;</TD>				
							<TD nowrap>&nbsp;<% if(vo.getSex()==1) out.print("��"); else out.print("Ů"); %></TD>								
							<td align="right" height="20">��ɫ&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<%=view.getRole(vo.getRole())%></td>
						</tr>
						<tr>
							<TD nowrap align="right" height="24">����&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=view.getDept(vo.getDept())%></TD>								
							<td align="right" height="20">ְ��&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<%=view.getPosition(vo.getPosition())%></td>
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">�绰&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="phone" type="text" size="16" class="formStyle" value="<%=SysUtil.ifNull(vo.getPhone())%>"></TD>								
							<td align="right" height="20">�ֻ�&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="mobile" value="<%=SysUtil.ifNull(vo.getMobile())%>" type="text" size="16" class="formStyle"></td>
						</tr>
						<tr>
							<TD nowrap align="right" height="24">Email&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="email" type="text" size="16" class="formStyle" value="<%=SysUtil.ifNull(vo.getEmail())%>"></TD>								
							<td align="right" height="20">&nbsp;</td>
							<td colspan="3" align="left">&nbsp;</td>
						</tr>											
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="4" align="center">
								<br>
								<input type="button" value="����" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="����" onclick="javascript:history.back(1)">
								<br>
								<br>
							</TD>	
						</tr>						
					</TBODY>
				</TABLE>
						<br>						
					</td>
				</tr>
			</table>
			<br>
			<br>						
		</td>
		</tr>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>
