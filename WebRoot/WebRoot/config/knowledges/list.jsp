<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	JspPage jp = (JspPage) request.getAttribute("page");
	UserView view = new UserView();
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getKnowledgesOperationPermission());
    boolean isCreateOperation = roleOperationPermissionService.isCreateOperationPermission();
    boolean isUpdateOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteOperation = roleOperationPermissionService.isDeleteOperationPermission();
    String createOperationDisable = "inline";
    if (!isCreateOperation) {
        createOperationDisable = "none";
    }
    String updateOperationDisable = "inline";
    if (!isUpdateOperation) {
        updateOperationDisable = "none";
    }
    String deleteOperationDisable = "inline";
    if (!isDeleteOperation) {
        deleteOperationDisable = "none";
    }
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
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
	/**
	*显示 删除 增加方法
	*/		
  var listAction = "<%=rootPath%>/knowledge.do?action=list";
  var delAction = "<%=rootPath%>/knowledge.do?action=delete";
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/knowledge.do?action=ready_add";
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
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>
					<td align="center" valign=top bgcolor="#ababab">
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
												系统管理 >> 方案管理 >> 方案列表
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
												<a href="#" onclick="toAdd()" style="display: <%=createOperationDisable%>">添加</a>
												<a href="#" onclick="toDelete()" style="display: <%=deleteOperationDisable%>">删除</a>&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="80%" align="center">
												<jsp:include page="../../common/page.jsp">
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
										<tr class="microsoftLook0" height=28>
											<th width='5%'>
												<INPUT type="checkbox" class=noborder name="checkall"
													onclick="javascript:chkall()">
											</th>
											<th width='5%'>
												序号
											</th>
											<th width='10%'>
												类别
											</th>
											<th width='10%'>
												类型
											</th>
											<th width="10%">
												指标
											</th>
											<th width='15%'>
												附件
											</th>
											<th width='15%'>
												备注
											</th>
											<th width='5%'>编辑</th>
										</tr>
										<%
											Knowledge vo = null;
											int startRow = jp.getStartRow();
											for (int i = 0; i < list.size(); i++) {
												vo = (Knowledge) list.get(i);
												String attachfiles = vo.getAttachfiles();
												String category=vo.getCategory();
												String entity=vo.getEntity();
												String subentity=vo.getSubentity();
												request.setAttribute("attachfiles",attachfiles);
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook" height=25>
											<td align='center'>
												<INPUT type="checkbox" class=noborder name=checkbox
													value="<%=vo.getId()%>">
											</td>
											<td align='center'><a href="#"  style="cursor:hand" onclick="window.showModalDialog('<%=rootPath%>/knowledge.do?action=read&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')">
												<font color='blue'><%=startRow + i%></font></a>
											</td>
											<td align='center'><%=vo.getCategory()%></td>
											<td align='center'><%=vo.getEntity()%></td>
											<td align='center'><%=vo.getSubentity()%></td>
											<!-- 在一个新的JSP中打开PDF文件 并将category entity subentity属性传过去-->
											<td align='center'><a href="#" onclick="window.open('<%=rootPath%>/config/knowledges/show.jsp?attachfiles=<%=attachfiles%>&category=<%=category%>&entity=<%=entity%>&subentity=<%=subentity%>')"><%=vo.getAttachfiles()%></a></td>
											<td align='center'><%=vo.getBak()%></td>
											<td align='center'><a style="display: <%=updateOperationDisable%>" href="<%=rootPath%>/knowledge.do?action=ready_edit&id=<%=vo.getId()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
										</tr>
										<%
											}
										%>
									</table>
								</td>
							</tr>
							<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
                  </tr>
									</table>
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
	</BODY>
</HTML>
