<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Role"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.model.UserAudit"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	List<UserAudit> list = (List<UserAudit>)request.getAttribute("list");
	List userList = (List)request.getAttribute("userList");
	JspPage jp = (JspPage)request.getAttribute("page");
	%>

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
<script language="JavaScript" type="text/javascript">
	var curpage= <%=jp.getCurrentPage()%>;
  	var totalpages = <%=jp.getPageTotal()%>;
  	var listAction = "<%=rootPath%>/userAudit.do?action=list";
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/userAudit.do?action=delete";
     mainForm.submit();
  }
  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/userAudit.do?action=ready_add";
      mainForm.submit();
  }
</script>
<script language="javascript">	
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
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
		<td width="200" valign=top align=center rowspan="2">
			<%=menuTable %>
		
		</td>
		
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">系统管理 >> 角色管理 >> 用户操作审计</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
							<a href="#" onclick="toAdd()">添加</a>
							<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
							</td>
									</tr>
        								</table>
										</td>
        						</tr>				
	<tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
   					 			<jsp:include page="../../common/page.jsp">
     							<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     							<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
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
							<td width=8%>&nbsp;序号
							<INPUT type="checkbox" class=noborder id="allcheckbox" value="">
							</td>
							<td width=8%>&nbsp;用户</td>
							<td width=69%>&nbsp;操作</td>
							<td width=15%>&nbsp;时间</td>
						</tr>
						
						<%
						int startRow = jp.getStartRow();
						for(int i = 0 ; i<list.size(); i++){
							for(int j = 0 ; j<userList.size(); j++){
								User user = (User)userList.get(j);
								if(user.getId() == list.get(i).getUserId()){
							%>
							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25>
								<td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name="checkbox" value="<%=list.get(i).getId()%>"><%=startRow + i%></td>
								<td>&nbsp;<%=user.getName()%></td>
								<td>&nbsp;<%=list.get(i).getAction() %></td>
								<td>&nbsp;<%=list.get(i).getTime() %></td>
							</tr>
							<%
							}
							}
						} %>
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
              </table></td>
            </tr>	
		</table>
			</td>

			</tr>
</table>
</form>

</BODY>
<script>
	function unionCheckbox(){
		var allcheckbox = document.getElementById("allcheckbox");
		var checkboxes = document.getElementsByName("checkbox");
		
		allcheckbox.onclick = function(){
		
			var chk = allcheckbox.checked;
			
			for(var i = 0 , j = checkboxes.length; i<j; i++){
				var checkbox = checkboxes[i];
				checkbox.checked = chk;
			}
		};
	}
	
	unionCheckbox();
</script>
</HTML>
