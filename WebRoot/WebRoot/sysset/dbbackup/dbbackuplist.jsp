<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.sysset.model.DBBackup"%>
<%@ include file="/include/globe.inc"%>
<%   
	List<DBBackup> dbBackupList = (List<DBBackup>)request.getAttribute("list");
  	String rootPath = request.getContextPath(); 
    JspPage jp = (JspPage)request.getAttribute("page");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="javascript">
	var curpage= <%=jp.getCurrentPage()%>;
    var totalpages = <%=jp.getPageTotal()%>;
    var listAction = "<%=rootPath%>/dbbackup.do?action=dbbackuplist";
	function startload(obj){
		if( !confirm("确认要开始导入数据文件吗？") )
		{
			return false;
		}
		mainForm.action = "<%=rootPath%>/dbbackup.do?action=load&filename="+obj;
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();if( ${result } ) alert('${msg }');">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		</td>		
		<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">系统管理 >> 数据库维护 >> 数据库备份列表</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>

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

				<tr >
					<td colspan="2">
						<table cellspacing="1" cellpadding="0" width="100%" >
							<tr class="microsoftLook0" height=28>
								<td width="10%" align="center">序号</td>
								<td width="40%" align="center">备份文件名</td>
								<td width="30%" align="center">时间</td>
								<td width="20%" align="center">操作</td>
    						</tr>
							<%for(int i = 0; i < dbBackupList.size(); i++){
								DBBackup dbBackup = dbBackupList.get(i);
							%>
							<tr height="28">
								<td align="center"><%=i+1 %></td>
								<td><%=dbBackup.getFilename()%></td>
								<td><%=dbBackup.getTime()%></td>
								<td align="center"><input type="button" value="导入文件" onclick=startload("<%=dbBackup.getFilename()%>")></td>
							</tr>	
							<% 
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
</HTML>
