<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.event.model.IManagerTrap"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	
	String menuTable = (String)request.getAttribute("menuTable");
	
	
	String name = (String)request.getAttribute("name");
	String deviceIP = (String)request.getAttribute("deviceIP");
	//String subtype = (String)request.getAttribute("subtype");
	
	List nodelist = (List)request.getAttribute("nodelist");
	
	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
	List<IManagerTrap> list = (List<IManagerTrap>)request.getAttribute("list");
	
	JspPage jp = (JspPage)request.getAttribute("page");
    
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getThresholdOperationPermission());
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


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
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
		<script language="JavaScript">

			//公共变量
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"100");
			    }
			    else
			    {
			        popMenu(itemMenu,100,"1111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*显示弹出菜单
			*menuDiv:右键菜单的内容
			*width:行显示的宽度
			*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //创建弹出菜单
			    var pop=window.createPopup();
			    //设置弹出菜单的内容
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //获得弹出菜单的行数
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //循环设置每行的属性
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //如果设置该行不显示，则行数减一
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //设置是否显示该行
			        rowObjs[i].style.display=(hide)?"none":"";
			        //设置鼠标滑入该行时的效果
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //设置鼠标滑出该行时的效果
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //屏蔽菜单的菜单
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //选择右键菜单的一项后，菜单隐藏
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //显示菜单
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			
			function add(){
				
				
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" ){
					mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showAdd&jspFlag=add";
				mainForm.submit();
				}
				if(type.value=="-1"){
					alert("请选择类型!");
				}
				if(subtype.value=="-1"){
					alert("请选择子类型!");
				}
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showEdit&id=" + node;
				mainForm.submit();
			}
			
			function toChooseNode(){
				var nodeid = document.getElementById("nodeid");
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" && nodeid.value !="-1"){
					mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showAdd&jspFlag=multi";
					mainForm.submit();
				}
				if(type.value=="-1"){
					alert("请选择类型!");
				}
				if(subtype.value=="-1"){
					alert("请选择子类型!");
				}
				if(nodeid.value=="-1"){
					alert("请选择设备!");
				}
				
			}
			
			function search(){
				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showlist";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var listAction = "<%=rootPath%>/imanagertrap.do?action=list";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">编辑</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title">&nbsp;告警 >> TRAP管理 >> SDH告警列表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
													<!--
													<tr>
														<td>
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: left;">
																		&nbsp;&nbsp;<b>名称：</b>
																		<select id="name" name="name" style="width:150px" onchange="changeName()">
																			<option value="-1">不限</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<b>设备IP：</b>
																		<select id="deviceIP" name="deviceIP" style="width:150px" >
																			<option value="-1">不限</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<input type="button" value="查  询" onclick="search()">
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													-->
													<tr>
														<td>
															<table>
																<tr>
													    			<td  class="body-data-title">
							    										<jsp:include page="/common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
							  										 	</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
															<td class="body-data-title" width=5%>
																	<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>序号</td>
													    			<td class="body-data-title" width=8%>名称</td>
													    			<td class="body-data-title" width=7%>设备IP</td>
													    			<td class="body-data-title" width=8%>设备类型</td>
													    			<td class="body-data-title" width=18%>部件信息</td>
													    			<td class="body-data-title" width=8%>告警源类型</td>
													    			<td class="body-data-title">告警信息</td>
													    			<td class="body-data-title">告警引起原因</td>
													    			
													    			
							        							<%
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										IManagerTrap trap = (IManagerTrap)list.get(i);
							        										
							        									
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list">
							        												<INPUT type="checkbox" class=noborder name=checkbox value="<%=trap.getId()%>" class=noborder><%=jp.getStartRow()+i%>
							        											</td>
							        											<td class="body-data-list"><%=trap.getName()== null ? "" : trap.getName()%></td>
																	    		<td class="body-data-list"><%=trap.getDeviceIP() == null ? "" : trap.getDeviceIP()%></td>
																    			<td class="body-data-list"><%=trap.getDeviceType()== null ? "" : trap.getDeviceType() %></td>
																    			<td class="body-data-list"><%=trap.getElementInstance()== null ? "" : trap.getElementInstance() %></td>
																    			<td class="body-data-list"><%=trap.getAlarmFunctionType()== null ? "" : trap.getAlarmFunctionType() %></td>
																    			<td class="body-data-list"><%=trap.getAlarmInfo()==null ? "" : trap.getAlarmInfo() %></td>
																    			<td class="body-data-list"><%=trap.getReasonOfCausingAlarm() == null ? "" : trap.getReasonOfCausingAlarm() %></td>
																    			
																  				</tr>
							        										<%
							        									}
							        								}%>
															</table>
														</td>
													</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-footer" class="content-footer">
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
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
	

	
</html>
