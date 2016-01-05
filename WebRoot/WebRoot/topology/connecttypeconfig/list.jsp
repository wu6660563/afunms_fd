<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.ConnectTypeConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  Hashtable hostNodeHashtable = (Hashtable)request.getAttribute("hostNodeHashtable");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
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
		<script type="text/javascript">
			function chkall(){
				var checkall = document.getElementById("checkall");
				var checkboxes = document.getElementsByName("checkbox");
				for(var i = 0 ; i < checkboxes.length; i++){
					var checkbox = checkboxes[i];
					checkbox.checked = checkall.checked;
				}
			}
		</script>
		<script type="text/javascript">
			//公共变量
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip,showItemMenu)
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
			        popMenu(itemMenu,100,showItemMenu);
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
			
			function updateHostCollectionAgreement()
			{
				location.href="<%=rootPath%>/connectConfig.do?action=readyConnectConfig&id="+node;
			}
			
			function showChildNode(){
				openWindow("<%=rootPath%>/remotePing.do?action=showChildNode&node="+node);
			}
			
			function setChildNode(){
				var url = "<%=rootPath%>/remotePing.do?action=setChildNode&node="+node;
				openWindow(url);
			}
			function deleteCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=deleteCollectionAgreement&id="+node + "&endpoint="+endpoint;
	}
		</script>
		
		<script type="text/javascript">
			function openWindow(url){
				window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}

	
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
	<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin;font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.updateHostCollectionAgreement()">
						编辑
					</td>
				</tr>
				<!--<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.showChildNode()">
						查看子节点
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setChildNode()">
						设置子节点
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.deleteCollectionAgreement(0)">
						取消所属协议
					</td>
				</tr>-->
			</table>
		</div>
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
									                	<td class="content-title"> 资源 >> 设备维护 >> 设备连通性检测设置 >> 设备连通性检测设置列表</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
		        										<td>
		        											<table cellspacing="0" border="1" bordercolor="#ababab">
		        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
		        													<td><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
		        													<td align="center">设备名称</td>
		        													<td align="center">IP地址</td>
		        													<td align="center">检测方式</td>
		        													<td align="center">用户名</td>
		        													<td align="center">登陆提示符</td>
		        													<td align="center">密码提示符</td>
		        													<td align="center">shell提示符</td>
		        													<td align="center">操作</td>
		        												</tr>
		        												<%
		        												
		        												if(list!=null && list.size()>0 && hostNodeHashtable !=null && hostNodeHashtable.size()>0){
			        												for(int i = 0 ; i < list.size(); i ++){
			        													ConnectTypeConfig remotePingHost = (ConnectTypeConfig)list.get(i);
			        													HostNode hostNode = (HostNode)hostNodeHashtable.get(remotePingHost.getNode_id());
			        													String collectAgreement = remotePingHost.getConnecttype();;
			        													if(hostNode == null)continue;
			        													String showItemMenu = "";
																		if (hostNode.getEndpoint() == 0) {
																			//collectAgreement = "snmp";
																			showItemMenu = "0000";
																		}else if(hostNode.getEndpoint() == 1){
																			//collectAgreement = "Ping 服务器";	
																			showItemMenu = "1111";
																		}else if(hostNode.getEndpoint() == 2){
																			//collectAgreement = "Ping 节点";	
																			showItemMenu = "0001";
																		}else if(hostNode.getEndpoint() == 3){
																			//collectAgreement = "Telnet";	
																			showItemMenu = "1001";
																		}else if(hostNode.getEndpoint() == 4){
																			//collectAgreement = "SSH";	
																			showItemMenu = "1001";
																		}
																		showItemMenu = "1111";
			        													%>
			        													<tr <%=onmouseoverstyle%>>
			        														<td>
			        															<input type="checkbox" id="<%=remotePingHost.getId()%>" name="checkbox" value="<%=remotePingHost.getId()%>">
			        															<%=i+1%>
			        														</td>
			        														<td>&nbsp;<%=hostNode.getAlias()%></td>
			        														<td>&nbsp;<%=hostNode.getIpAddress()%></td>
																		<td>&nbsp;<%=collectAgreement%></td>
			        														<td>&nbsp;<%=remotePingHost.getUsername()%></td>
			        														<td>&nbsp;<%=remotePingHost.getLoginPrompt()%></td>
			        														<td>&nbsp;<%=remotePingHost.getPasswordPrompt()%></td>
			        														<td>&nbsp;<%=remotePingHost.getShellPrompt()%></td>
			        														<td>
																				&nbsp;&nbsp;
																				<img src="<%=rootPath%>/resource/image/status.gif"
																					border="0" width=15 oncontextmenu=showMenu('2','<%=hostNode.getId()%>','<%=hostNode.getIpAddress()%>','<%=showItemMenu%>')>
					
					
																			</td>
			        													</tr>
			        													<%
			        												}
		        												}
		        												%>
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
