<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.MonitorDBDTO"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>


<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  String flag = (String)request.getAttribute("flag");
  
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getDBOperationPermission());
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
	
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
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
			var sid="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip)
			{	
				//alert(id+"==="+nodeid+"=="+ip);
				ipaddress=ip;
				var arr=ipaddress.split(":");
				if(arr.length>1){
				  sid=arr[1];
				}else{
				  sid="-1";
				}
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"0000");
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
			function detail()
			{
			    mainForm.action="<%=rootPath%>/db.do?action=check&id="+node+"&sid="+sid;
			    mainForm.submit();
			}
			function edit()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=ready_edit&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function cancelmanage()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=cancelmanage&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function addmanage()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=addmanage&id="+node+"&sid="+sid;
				 mainForm.submit();
			}	
			function clickMenu()
			{
			}
			
			function toAdd(){
			     mainForm.action = "<%=rootPath%>/db.do?action=ready_add";
			     mainForm.submit();
			}
			function toDelete(){
				mainForm.action = "<%=rootPath%>/db.do?action=delete";
				mainForm.submit();
			}  
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();" leftmargin="0" topmargin="0">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail();">监视信息</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.cancelmanage()">取消监视</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addmanage()">添加监视</td>
			</tr>	
		</table>
		</div>
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<table id="body-container" class="body-container" height="100%">
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
									                	<td class="content-title">&nbsp;应用 &gt;&gt; 数据库管理 &gt;&gt; 数据库列表</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<%if(flag == null) {
		        									%>
		        									<tr>
        												<td colspan="11">
											        		<table>
											        			<tr>
																	<td style="text-align: right;" class="body-data-title">
                                                                        <a href="#" onclick="toAdd()" style="display: <%=createOperationDisable%>">添加</a>
																		<a href="#" onclick="toDelete()" style="display: <%=deleteOperationDisable%>">删除</a>&nbsp;&nbsp;&nbsp;
											  						</td>  
					        									</tr>
					        								</table>
					        							</td>                     
					        						</tr>
		        									<%
		        									} %>
		        									<tr>
       													<td align="center" class="body-data-title">序号</td>
       													<td align="center" class="body-data-title">名称</td>
       													<td align="center" class="body-data-title">类型</td>
       													<td align="center" class="body-data-title">数据库名</td>
       													<td align="center" class="body-data-title">IP地址</td>
       													<td align="center" class="body-data-title">端口</td>
       													<td align="center" class="body-data-title">监视状态</td>
       													<td align="center" class="body-data-title">状态</td>
       													<td align="center" class="body-data-title">可用性</td>
       													<td align="center" class="body-data-title">操作</td>
		        									</tr>
		        									<%
		        									    if(list!=null&& list.size()>0){
		        									    	
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									        	MonitorDBDTO monitorDBDTO = (MonitorDBDTO)list.get(i);
		        									        	Hashtable eventListSummary = monitorDBDTO.getEventListSummary();
		        									        	String generalAlarm = (String)eventListSummary.get("generalAlarm");
	        									        		String urgentAlarm = (String)eventListSummary.get("urgentAlarm");
		        									        	String seriousAlarm = (String)eventListSummary.get("seriousAlarm");
		        									        	
		        									        	
		        									        	String status = monitorDBDTO.getStatus();
					        									  
		        									        	String statusImg = "";
		        									        	if("1".equals(status)){
		        									        		statusImg = "alarm_level_1.gif";
		        									        	}else if("2".equals(status)){
		        									        		statusImg = "alarm_level_2.gif";
		        									        	}else if("3".equals(status)){
		        									        		statusImg = "alert.gif";
		        									        	}else{
		        									        		statusImg = "status_ok.gif";
		        									        	}
		        									        	
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        									            	<td align="center" class="body-data-list"><INPUT type="radio" class=noborder name=radio value="<%=monitorDBDTO.getId()+ ":" + monitorDBDTO.getSid()%>"><%=i+1%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getAlias()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getDbtype()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getDbname()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getIpAddress()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getPort()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getMon_flag()%></td>
			       													<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>"><%=NodeHelper.getStatusDescr(Integer.valueOf(status))%></td>
												                    <td align="center" class="body-data-list"><%=monitorDBDTO.getPingValue()%></td>
			       													<!-- 
			       													<td align="center" class="body-data-list">
			       														<img style="display: <//%=updateOperationDisable%>;" src="<//%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<//%=monitorDBDTO.getId()%>','<//%=monitorDBDTO.getIpAddress()%>:<//%=monitorDBDTO.getSid()%>') alt="右键操作">
			       													</td>
			       													 -->
			       													 <td  align='center'  class="body-data-list"><a href="<%=rootPath%>/db.do?action=ready_edit&id=<%=monitorDBDTO.getId()%>&sid=<%=monitorDBDTO.getSid()%>">
																		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="编辑"/></a>
																	</td>
					        									</tr>
		        									            
		        									            <% 
		        									        }
		        									        
		        									    }
		        									 %>
		        									
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
