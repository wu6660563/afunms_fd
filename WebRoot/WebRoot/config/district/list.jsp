<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
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
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/district.do?action=delete";
  var listAction = "<%=rootPath%>/district.do?action=list";
   
 
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/district.do?action=ready_add";
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
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">ϵͳ���� >> ϵͳ���� >> �������</td>
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
									<a href="#" onclick="toAdd()">���</a>
									<a href="#" onclick="toDelete2()">ɾ��</a>&nbsp;&nbsp;&nbsp;
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
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
					<th ></th>
    					<th width="20%">���</th>
    					<th width="20%"><strong>��������</strong></th>
                      			<th width="20%"><strong>����</strong></th>
                      			<th width="20%"><strong>��ɫ</strong></th>
                      			<th width="20%">�༭</th>
					</tr>
     <%
int startRow = jp.getStartRow();
DistrictConfig vo=null;
for(int i=0;i<list.size();i++)
{ 
     vo=(DistrictConfig)list.get(i);
     String status=null;
    
%>
      <tr bgcolor="#FFFFFF"  <%=onmouseoverstyle%> height=25>
    <td height=25>&nbsp;<INPUT type="radio" class=noborder name="radio" value="<%=vo.getId()%>"></td>
    <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;">&nbsp; <%=startRow + i%></td>
       <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=vo.getName()%>&nbsp;</td>
      <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=vo.getDesc()%>&nbsp;</td>
      <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor="#<%=vo.getDescolor()%>">&nbsp;</td>
       <td align="center"><a href="<%=rootPath%>/district.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
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
              </table></td>
            </tr>	
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
