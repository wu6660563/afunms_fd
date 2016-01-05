<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.subconfigcat.model.SubconfigCatConfig"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list"); 
  int rc = list.size();
  JspPage jp = (JspPage)request.getAttribute("page");
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
<script language="JavaScript" type="text/javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/subconfigcat.do?action=delete";
  var listAction = "<%=rootPath%>/subconfigcat.do?action=list";
   
 
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/subconfigcat.do?action=ready_add";
     mainForm.submit();
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
		<td bgcolor="#ffffff" align="center" valign=top>
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;<font color=#ffffff>>> </font></td>
				</tr>
        						<TR>
        							<TD width="50%">&nbsp;
          							</td>            
								<td width="50%" align='right'>
									<a href="#" onclick="toAdd()">添加</a>
									<a href="#" onclick="toDelete2()">删除</a>&nbsp;&nbsp;&nbsp;
		  						</td>                       
        						</tr>	
        						 <tr>
						    <td colspan="2" width="100%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td></tr> 				
				
		<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
					<th ></th>
    					<th width="20%">序号</th>
    					<th width="20%"><strong>名字</strong></th>
                      <th width="40%"><strong>描述</strong></th>
                      <th width="20%">编辑</th>
					</tr>
     <%
int startRow = jp.getStartRow();
SubconfigCatConfig vo=null;
for(int i=0;i<list.size();i++)
{ 
     vo=(SubconfigCatConfig)list.get(i);
     String status=null;
    
%>
      <tr bgcolor="DEEBF7"  <%=onmouseoverstyle%> height=25>
    <td height=25>&nbsp;<INPUT type="radio" class=noborder name="radio" value="<%=vo.getId()%>"></td>
    <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;">&nbsp; <%=startRow + i%></td>
       <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=vo.getName()%>&nbsp;</td>
      <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=vo.getDesc()%>&nbsp;</td>
       <td align="center"><a href="<%=rootPath%>/subconfigcat.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
 </tr>
 <%
}
 %> 				
			</table>
			</td>
		</tr>	
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
