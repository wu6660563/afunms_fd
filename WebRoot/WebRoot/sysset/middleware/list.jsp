<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.Map"%>
<%@page import="com.afunms.sysset.model.Middleware"%>
<%@ include file="/include/globe.inc"%>
<% 
  List listSub = (List)request.getAttribute("listSub");
  Map father = (Map)request.getAttribute("father");
  String show = (String)request.getAttribute("show");
  String fOrc = "0";
  if( father.get(0) != null)
  {
	  fOrc = "1";
  }
  int rc = listSub.size();

  JspPage jp = (JspPage)request.getAttribute("page");  
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
<script language="javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var listAction = "";
  var delAction = "<%=rootPath%>/middleware.do?action=delete"; 
  if( <%=fOrc%> == "1" )
  {
  	listAction = "<%=rootPath%>/middleware.do?action=listFather";
  }
  else
  {
  	listAction = "<%=rootPath%>/middleware.do?action=list";
  } 
  
  var alertInfo = "确实要删除吗?";
  if( <%=fOrc%> == "1" )
  {
  	alertInfo = "删除父类型，则它所有的子类都会被删除，确实要删除吗?";
  }

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/middleware.do?action=ready_add";
     mainForm.submit();
  }
  function toShow()
  {
  	 if( <%=fOrc%> == "1" )
  	 {
  	 	mainForm.action = "<%=rootPath%>/middleware.do?action=list&jp=1";
     	mainForm.submit();
  	 }
  	 else
  	 {
  	 	mainForm.action = "<%=rootPath%>/middleware.do?action=listFather&jp=1";
     	mainForm.submit();
     }
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
		<td width="200" valign=top align=center>
			<%=menuTable%>
		</td>
		<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">系统管理 >> 资源管理 >> 中间件管理列表</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  <tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
			<a href="#" onclick="toAdd()">添加</a>
			<a href="#" onclick="toDelete()">删除</a>
			<a href="#" onclick="toShow()"><%=show %></a>&nbsp;&nbsp;&nbsp;<input name="faOrCh" type="hidden" value="<%=fOrc%>"/>
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
						<tr >
							<td colspan="2">
								<table cellspacing="1" cellpadding="0" width="100%">
								<tr class="microsoftLook0" height=28>
									<th width='22'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    									<th width='30'>序号</th>
    									<th width='80'>名称</th>
    									<th width='80'>注解</th>
    									<th width='100'>对应数据库表</th>
    									<th width='80'>类型</th>
    									<th >编辑</th>
								</tr>
<%
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
    	Middleware vo = (Middleware)listSub.get(i);
%>
       		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25>
    			<td ><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    			<td ><font color='blue'><%=startRow + i%></font></td>
			<td ><%=vo.getName()%></td>
			<td ><%=vo.getText()%></td> 
			<td ><%=vo.getTable_name()%></td>				
			<td ><%=father.get(vo.getFather_id()) %></td>
			<td align="center" ><a href="<%=rootPath%>/middleware.do?action=ready_edit&id=<%=vo.getId()%>">
				<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
  		</tr>
<%}%>  	  				
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
