<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Function"%>
<%@page import="com.afunms.system.util.CreateRoleFunctionTable"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List<Function> functionList = (List<Function>)request.getAttribute("functionList");
  Function root= (Function)request.getAttribute("root");
  CreateRoleFunctionTable crft = new CreateRoleFunctionTable();
  List<Function> secondfunctionList = crft.getFunctionChild(root,functionList);
   
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


<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">



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

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
	<form method="post" name="mainForm">
		<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
			<tr>
				<td width="200" valign=top align=center>
					<%=menuTable %>
				</td>
				<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
               <tr>
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%">
					<tr class="microsoftLook0" height=28>
							<td>
								<%
									
									for(int i = 0; i<secondfunctionList.size(); i++){
										List<Function> functionChildList = crft.getFunctionChild(secondfunctionList.get(i),functionList);
										%>
											<table width="100%" class="microsoftLook0" align=center border=0 cellSpacing="1">
												<tr>
													<td height="28" align="left" class="microsoftLook0"><b>&nbsp;&nbsp;<%=secondfunctionList.get(i).getCh_desc() %>&nbsp;>></b></td>
												</tr>
												<tr>					
													<td height=50 bgcolor="#FFFFFF" valign="top" align=center>
														<br>
															<table cellSpacing="0" cellPadding="0" width="98%" border="0"> 						
									        						
																<tr>
																	<td colspan="2">
																		<table cellspacing="0" cellpadding="0" width="100%" height=50>
										  									<tr>
										  										<%for(int j = 0; j<functionChildList.size();j++){
										  											int k = 98/functionChildList.size();
										  											%>
										  												<td align="center" width="<%=k %>%"><a href="<%=rootPath%>/<%=functionChildList.get(j).getUrl() %>" ><img src="<%=rootPath%>/<%=functionChildList.get(j).getImg_url()%>" border=0><br><%=functionChildList.get(j).getCh_desc() %></a></td>
										  											<%
										  										} %>
									      															
									      											
																			</tr>
																		</table>
																	</td>
																</tr>	
															</table>
														<br>						
													</td>
												</tr>
											</table>
										
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
