<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Role"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.*"%>
<%
    String rootPath = request.getContextPath();
    User user = (User) session
		    .getAttribute(SessionConstant.CURRENT_USER);
    String menuTable = (String) request.getAttribute("menuTable");
    System.out.println("-----------role首页模块列表页面-----------");
%>

<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		<script language="JavaScript" type="text/javascript">

  function toUpdate()
  {
     mainForm.action = "<%=rootPath%>/homerole.do?action=update";
     mainForm.submit();
  }
</script>
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
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">
		<form method="post" name="mainForm">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td width="200" valign=top align=center rowspan="2">
						<%=menuTable%>

					</td>

					<td align="center" valign=top>
						<table width="98%" cellpadding="0" cellspacing="0" algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="layout_title">
												系统管理 >> 角色 >> 首页模块分配
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="100%" align='right'>
												<a href="#" onclick="toUpdate()">修改</a>&nbsp;&nbsp;&nbsp;
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table width="100%" border="0" cellpadding="0" cellspacing="1">
										<tr class="microsoftLook0" height=28>
											<%
											    List<Role> Rolelist = (List<Role>) request.getAttribute("Rolelist");
											    for (int i = 0; i < Rolelist.size(); i++) {
													//超级管理员  显示所有
													//管理员 那么添加超级管理员以外所有角色
													//一般用户 只显示所属角色 
													//超级管理员模式
													if (user.getRole() == 0) {
											%>
											<td height="28" bgcolor="#FFFFFF" align="center"
												valign="middle">
												<input type="radio" id="role" name="roleId"
													<%if (user.getRole() == Rolelist.get(i).getId()){%>
													checked="checked" <%}%> value=<%=Rolelist.get(i).getId()%>>
												&nbsp;<%=(String) Rolelist.get(i).getRole()%>
											</td>
											<%
											    //管理员模式
													} else if (user.getRole() == 2 && Rolelist.get(i).getId() != 0) {
											%>
											<td height="28" bgcolor="#FFFFFF" align="center"
												valign="middle">
												<input type="radio" id="role" name="roleId"
													<%if (user.getRole() == Rolelist.get(i).getId()){%>
													checked="checked" <%}%> value=<%=Rolelist.get(i).getId()%>>
												&nbsp;<%=(String) Rolelist.get(i).getRole()%>
											</td>

											<%
											    } else if (user.getRole() == Rolelist.get(i).getId()) {
													    //普通用户登录后显示的角色
											%>
											<td height="28" bgcolor="#FFFFFF" align="center"
												valign="middle">
												<input type="radio" id="role" name="roleId"
													checked="checked" value=<%=Rolelist.get(i).getId()%>>
												&nbsp;<%=(String) Rolelist.get(i).getRole()%>
											</td>
											<%
											    }
											    }
											%>


										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td background="<%=rootPath%>/common/images/right_b_02.jpg">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_01.jpg"
													width="5" height="12" />
											</td>
											<td></td>
											<td align="right" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_03.jpg"
													width="5" height="12" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form> 
	</BODY>
</HTML>
