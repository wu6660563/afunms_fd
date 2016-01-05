<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.security.model.MgeUps"%>
<%@page import="com.afunms.polling.node.UPSNode"%>
<%@page import="com.afunms.polling.node.AirNode"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  //JspPage jp = (JspPage)request.getAttribute("page");
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
			function detail()
			{
			    mainForm.action="<%=rootPath%>/ups.do?action=toDetail&id="+node;
			    mainForm.submit();
			}
			function doFileTask()
			{
				mainForm.action="<%=rootPath%>/ups.do?action=doTask&id="+node;
			    mainForm.submit();
			}
			function edit()
			{
				 mainForm.action="<%=rootPath%>/ups.do?action=ready_edit&id="+node;
				 mainForm.submit();
			}
			function cancelmanage()
			{
				 mainForm.action="<%=rootPath%>/ups.do?action=cancelmanage&id="+node;
				 mainForm.submit();
			}
			function addmanage()
			{
				 mainForm.action="<%=rootPath%>/ups.do?action=addmanage&id="+node;
				 mainForm.submit();
			}	
			function clickMenu()
			{
			}
			
			function toAdd(){
			     mainForm.action = "<%=rootPath%>/ups.do?action=ready_add";
			     mainForm.submit();
			}
			function toDelete(){   
				mainForm.action = "<%=rootPath%>/ups.do?action=delete";
				mainForm.submit();
			}  
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">修改信息</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail();">详细信息</td>
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
									                	<td class="content-title"> 环境动力设备监视 &gt;&gt;列表</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
        												<td colspan="11">
											        		<table>
											        			<tr>
																	<td style="text-align: right;" class="body-data-title">
																		&nbsp;&nbsp;
																		<a href="#" onclick="toAdd()">添加</a>
																		<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
											  						</td>  
					        									</tr>
					        								</table>
					        							</td>                     
					        						</tr>	
		        									<tr>
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
       													<td align="center" class="body-data-title">名称</td>
       													<td align="center" class="body-data-title">IP地址</td>
       													<td align="center" class="body-data-title">设备类型</td>
       													<td align="center" class="body-data-title">设备子类型</td>
       													<td align="center" class="body-data-title">告警状态</td>
       													<td align="center" class="body-data-title">监视状态</td>
       													<td align="center" class="body-data-title">详情</td>
		        									</tr>
		        									<%
		        										for(int i = 0; i < list.size(); i++)
		        										{
		        											MgeUps vo = (MgeUps)list.get(i);
		        											int warningStatus = 0;
		        											if("ups".equalsIgnoreCase(vo.getType())){
		        											    UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(vo.getId());
		        											    warningStatus = SystemSnap.getUpsStatus(node.getId()+"");
		        											} else if("air".equalsIgnoreCase(vo.getType())){
		        											    AirNode node = (AirNode)PollingEngine.getInstance().getAirByID(vo.getId());
		        											    warningStatus = node.getStatus();
		        											} else {
		        											
		        											}
		        											String type = "";
		        											String subtype = "";
		        											if("ups".equals(vo.getType())){
		        											    type = "UPS";
		        											}else{
		        											    type = "空调";
		        											}
		        											if("ems".equals(vo.getSubtype())){
		        											    subtype = "艾默生";
		        											}else{
		        											    subtype = "梅兰日兰";
		        											}
		        									 %>
		        									<tr <%=onmouseoverstyle%>>
		        										<td align="center" class="body-data-list"><INPUT type="checkbox" class=noborder name="checkbox" value="<%=vo.getId() %>"><%=i + 1%></td>
		        										<td align="center" class="body-data-list"><%=vo.getAlias() %></td>
		        										<td align="center" class="body-data-list"><%=vo.getIpAddress()%></td>
		        										<td align="center" class="body-data-list"><%=type%></td>
		        										<td align="center" class="body-data-list"><%=subtype%></td>
		        										<%
		        											String monitor = vo.getIsmanaged();
		        											String s_monitor = null;
		        											if("1".equals(monitor))
		        												s_monitor = "是";
		        											else
		        												s_monitor = "否";
		        											String statusImg = "";
	        									        	if("1".equals(warningStatus)){
	        									        		statusImg = "alarm_level_1.gif";
	        									        	}else if("2".equals(warningStatus)){
	        									        		statusImg = "alarm_level_2.gif";
	        									        	}else if("3".equals(warningStatus)){
	        									        		statusImg = "alert.gif";
	        									        	}else{
	        									        		statusImg = "status_ok.gif";
	        									        	}
		        										 %>
		        										<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>"></td>
		        										<td align="center" class="body-data-list"><%=s_monitor %></td>
		        										<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','') alt="右键操作"></td>
			       									</tr>
			       									<%
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
