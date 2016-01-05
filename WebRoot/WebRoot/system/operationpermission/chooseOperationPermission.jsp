<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Role"%>


<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.Function"%>
<%@page import="com.afunms.system.model.RoleFunction"%>
<%@page import="com.afunms.system.util.CreateRoleFunctionTable"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@page import="com.afunms.system.model.OperationPermission"%>
<%@page import="com.afunms.system.model.Operation"%>
<%@page import="com.afunms.system.model.RoleOperation"%>
<%@page import="java.util.Map"%>
<%@include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%
  	String rootPath = request.getContextPath();
  	Role role = (Role)request.getAttribute("role");
	String menuTable = (String)request.getAttribute("menuTable");
	CreateRoleFunctionTable crft = new CreateRoleFunctionTable();
	List<OperationPermission> operationPermissionList = (List<OperationPermission>) request.getAttribute("operationPermissionList");
    Map<String, RoleOperation> roleOperationMap = (Map<String, RoleOperation>) request.getAttribute("roleOperationMap");
%>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->




<script language="JavaScript" type="text/javascript">

  function toUpdate()
  {
  	
     mainForm.action = "<%=rootPath%>/operationPermission.do?action=update";
     
     mainForm.submit();
  }

//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}

</script>


</head>
<body id="body" class="body" onload="initmenu();">



	<form id="mainForm" method="post" name="mainForm">
		
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">系统管理 >> 权限管理 >> 操作权限列表</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
                                                            <tr class="body-data-title">
                                                                <td class="body-data-title"><input type="hidden" name="RoleId" value=<%=role.getId()%>><%=role.getRole() %></td>
                                                            </tr>
				        									<tr>
				        										<td>
		        												    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                        <tr class="body-data-title">
                                                                            <td class="body-data-title">权限名称</td>
                                                                            <td class="body-data-title" colspan="3">操作</td>
                                                                        </tr>
                                                                        
                                                                            <%
                                                                                for(OperationPermission operationPermission : operationPermissionList) {
                                                                                    String name = operationPermission.getName();
                                                                                    String code = operationPermission.getCode();
                                                                                    List<Operation> operationList = operationPermission.getOperationlist();
                                                                                    %>
                                                                                <tr>
                                                                                    <td class="body-data-list"><%=name%></td>
                                                                                    <%
                                                                                        for(Operation operation : operationList) {
                                                                                            String operationName = operation.getName();
                                                                                            String operationCode = operation.getCode();
                                                                                            RoleOperation roleOperation = roleOperationMap.get(code + "-" + operationCode);
                                                                                            String isChecked = "";
                                                                                            if (roleOperation != null) {
                                                                                                isChecked = "checked =\"checked\"";
                                                                                            }
                                                                                    %>
                                                                                    <td class="body-data-list"><input type="checkbox" name="operationPermission" value="<%=code + "-" + operationCode%>" <%=isChecked%>><%=operationName%></td>
                                                                                    <%
                                                                                        }
                                                                                        %>
                                                                                </tr>
                                                                                        <%
                                                                                }
                                                                            %>
                                                                            
                                                                        
						                                            </table>				
				        										</td>
				        									</tr>
				        									<tr>
							                                     <td bgcolor="#ffffff" colspan="5" align=center><input id="write" type="button" class="button" value="确定" name="B2" onclick="toUpdate()"></td>
						                                      </tr>
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
										</td>
									</tr>
									<tr>
										<td>
											
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
</body>
<script>
</script>
</HTML>