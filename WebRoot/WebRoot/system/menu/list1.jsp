<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.Menu"%>
<%@page import="java.util.List"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  String rootPath = request.getContextPath();
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/menu.do?action=delete_top";
  var alertInfo = "删除一级菜单,将同时删除其下所有子菜单,你确定要删除吗?";
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/menu.do?action=ready_add_top";
     mainForm.submit();
  }

  function toOrder()
  {
     mainForm.action = "<%=rootPath%>/menu.do?action=ready_change_sort";
     mainForm.submit();
  }

</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
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
//添加菜单	
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


<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
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
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>　</td>
				<td height=300 bgcolor="#FFFFFF" valign="top">
	<table cellSpacing="1" cellPadding="0" width="100%" border="0">
		<tr>
			<td colspan="2">
			<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
		<tr><th width='20'></th>
          <th width='30'>序号</th>				
          <th width='100'>一级菜单</th>
          <th width='30'>顺序</th>   
          <th width='30'>编辑</th>
          <th width='60'>二级菜单</th> 
        </tr>
<%
    Menu vo = null;
    for(int i=0;i<rc;i++)
    {
       vo = (Menu)list.get(i);
%>
    <tr class="microsoftLook0">
        <td class="microsoftLook0"><INPUT type="radio" class=noborder name=radio value="<%=vo.getId()%>"></td>
    	<td class="microsoftLook0"><font color='blue'><%=i + 1%></font></td>
    	<td class="microsoftLook0"><%=vo.getTitle()%></td>
    	<td class="microsoftLook0"><%=vo.getSort()%></td>
		<td class="microsoftLook0"><a href="<%=rootPath%>/menu.do?action=ready_edit_top&id=<%=vo.getId()%>">
		  <img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
		<td class="microsoftLook0"><a href="<%=rootPath%>/menu.do?action=list_sub&id=<%=vo.getId()%>">
		  <img src="<%=rootPath%>/resource/image/lead.gif" border="0"/></a></td>		
  	</tr>
<%
    }
%> 				
			</table>
			</td>
		</tr>	
	</table>
	<table cellspacing="5" border="0" >
	    <tr>
			<td><a href="#" onclick="toAdd()">添加</a></td>
			<td><a href="#" onclick="toDelete2()">删除</a></td>
			<td><a href="#" onclick="toOrder()">改变菜单顺序</a></td>
		</tr>
	 </table>
</td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13></td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
</form>		
</BODY>
</HTML>
