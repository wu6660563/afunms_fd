<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.topology.model.MonitorHostDTO"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.MonitorNetDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  JspPage jp = (JspPage)request.getAttribute("page");
  
  String field = (String)request.getAttribute("field");
  String sorttype = (String)request.getAttribute("sorttype");
  if(sorttype == null || sorttype.trim().length() == 0){
  	  sorttype = "";
  }
  
  String nameImg = "";
  
  String categoryImg = "";
  
  String ipaddressImg = "";
  
  String pingImg = "";
  
  String cpuImg = "";
  
  String memoryImg = "";
  
  String inutilhdxImg = "";
  
  String oututilhdxImg = "";
  
  String imgSrc = "";
  
  if("desc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up2.gif";
  }else if("asc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up1.gif";
  }
  
  if("name".equals(field)){
  	  nameImg = "<img src='" + imgSrc + "'>";
  }
  if("category".equals(field)){
  	  categoryImg = "<img src='" + imgSrc + "'>";
  }
  if("ipaddress".equals(field)){
  	  ipaddressImg = "<img src='" + imgSrc + "'>";
  }
  if("ping".equals(field)){
  	  pingImg = "<img src='" + imgSrc + "'>";
  }
  if("cpu".equals(field)){
  	  cpuImg = "<img src='" + imgSrc + "'>";
  }
  if("memory".equals(field)){
  	  memoryImg = "<img src='" + imgSrc + "'>";
  }
  if("inutilhdx".equals(field)){
  	  inutilhdxImg = "<img src='" + imgSrc + "'>";
  }
  if("oututilhdx".equals(field)){
  	  oututilhdxImg = "<img src='" + imgSrc + "'>";
  }
  
  
  //System.out.println("======type===========" + sorttype);
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
			function detail()
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node;
			}
			function ping()
			{
				window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
			}
			function traceroute()
			{
				window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}
			function telnet()
			{
				window.open('<%=rootPath%>/perform.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}	
			function clickMenu()
			{
			}
		</script>
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/perform.do?action=delete";
  			var listAction = "<%=rootPath%>/perform.do?action=monitornetlist";
  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
				mainForm.submit();
			}
			  
			function doCancelManage(){  
     			mainForm.action = "<%=rootPath%>/perform.do?action=cancelmanage";
     			mainForm.submit();
	  		}
	  		
	  		function doQuery(){  
				window.location = window.location;
  			}
  			
  			function doSearch(){  
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornetlist";
     			mainForm.submit();
  			}
  			
  			function showCpu(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showMemery(id , ip){  
				CreateWindow("<%=rootPath%>/detail/host_memory_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showPing(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_ping_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showFlux(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_flux_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=850,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}
			
			function showSort(fieldValue){  
				var field = document.getElementById('field');
				field.value = fieldValue;
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornetlist";
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
					onclick="parent.detail()">查看状态</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.ping();">ping</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.traceroute()">traceroute</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.telnet()">telnet</td>
			</tr>		
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.clickMenu()">关闭</td>
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
									                	<td class="content-title">&nbsp;资源 >> 性能监视 >> 监视对象一览 >> 网络设备列表 </td>
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
															<table>
																<tr>
													    			<td  class="body-data-title">
							    										<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
							  										 	</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr> 
													<tr>
														<td >
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: left;" >
																		&nbsp;
																		<B>查&nbsp;&nbsp;询:</B>
							        									<SELECT name="key"> 
								          									<OPTION value="alias" selected>名称</OPTION>
								          									<OPTION value="ip_address">IP地址</OPTION>
								          									<OPTION value="sys_oid">系统OID</OPTION>          
								          									<OPTION value="type">型号</OPTION>
								          								</SELECT>&nbsp;<b>=</b>&nbsp; 
								          								<INPUT type="text" name="value" width="15" class="formStyle">
								          								<INPUT type="button" class="formStyle" value="查    询" onclick=" return doSearch()">
								          							</td>            
																	<td class="body-data-title" style="text-align: right;">
																		<INPUT type="button" value="添    加" onclick=" return toAdd()">
																		<INPUT type="button" value="删    除" onclick=" return toDelete()">
																		<INPUT type="button" value="取消管理" onclick=" return doCancelManage()">
														                <INPUT type="button" value="刷    新" onclick=" return doQuery()">
														                &nbsp;&nbsp;&nbsp;
											  						</td>
																</tr>
															</table>
											  			</td>
													</tr>
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<input type="hidden" id="field" name="field" value="<%=field%>">
		        													<input type="hidden" id="sorttype" name="sorttype" value="<%=sorttype%>">
		        													<td align="center" class="body-data-title" width="5%"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
		        													<td align="center" class="body-data-title" width="7%"><a href="#" onclick="showSort('name')">名称</a><%=nameImg%></td>
		        													<td align="center" class="body-data-title" width="7%"><a href="#" onclick="showSort('category')">类型</a><%=categoryImg%></td>
		        													<td align="center" class="body-data-title" width="8%"><a href="#" onclick="showSort('ipaddress')">IP地址</a><%=ipaddressImg%></td>
		        													<td align="center" class="body-data-title" width="8%"><a href="#" onclick="showSort('ping')">可用性</a><%=pingImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('cpu')">CPU(%)</a><%=cpuImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('memory')">内存(%)</a><%=memoryImg%></td>
		        													<td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('inutilhdx')">出口流速(KB/S)</a><%=inutilhdxImg%></td>
		        													<td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('oututilhdx')">入口流速(KB/S)</a><%=oututilhdxImg%></td>
		        													<td align="center" class="body-data-title" width="10%">告警</td>
		        													<td align="center" class="body-data-title" width="6%">采集方式</td>
		        													<td align="center" class="body-data-title" width="5%">详情</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	MonitorNetDTO monitorNetDTO = (MonitorNetDTO)list.get(i);
					        									        	
					        									        	String cpuUsed = monitorNetDTO.getCpuValue();
					        									        	String cpuUnUsed = String.valueOf(100 - Double.valueOf(cpuUsed));

					        									        	String memeryUsed = monitorNetDTO.getMemoryValue();
					        									        	String memeryUnUsed = String.valueOf(100 - Double.valueOf(memeryUsed));
					        									        	
					        									        	String cpuValueColor = monitorNetDTO.getCpuValueColor();
					        									        	String memeryValueColor = monitorNetDTO.getMemoryValueColor();
					        									        	
					        									        	String status = monitorNetDTO.getStatus();
					        									        	
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
					        									        	
					        									        	Hashtable eventListSummary = monitorNetDTO.getEventListSummary();
					        									        	String generalAlarm = (String)eventListSummary.get("generalAlarm");
					        									        	String urgentAlarm = (String)eventListSummary.get("urgentAlarm");
					        									        	String seriousAlarm = (String)eventListSummary.get("seriousAlarm");
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
						        									            <td align="center" class="body-data-list"><INPUT type="checkbox" id="checkbox" name="checkbox" value="<%=monitorNetDTO .getId()%>"><%=jp.getStartRow()+i %></td>
					        													<td align="left" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>">&nbsp;<a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=monitorNetDTO.getId()%>"><%=monitorNetDTO.getAlias()%></a></td>
					        													<td align="center" class="body-data-list"><%=monitorNetDTO.getCategory()%></td>
					        													<td align="center" class="body-data-list"><%=monitorNetDTO.getIpAddress()%></td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=monitorNetDTO.getPingValue()%>%</td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showPing("<%=monitorNetDTO .getId()%>" , "<%=monitorNetDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td><%=monitorNetDTO.getCpuValue()%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=cpuUsed%>%" bgcolor="<%=cpuValueColor%>"></td>
																                      					<td width="<%=cpuUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      					<td><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showCpu("<%=monitorNetDTO .getId()%>" , "<%=monitorNetDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list" >
					        														<table>
													                      				<tr>
													                      					<td><%=monitorNetDTO.getMemoryValue()%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=memeryUsed%>%" bgcolor="<%=memeryValueColor%>"></td>
																                      					<td width="<%=memeryUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      					<td><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showMemery("<%=monitorNetDTO .getId()%>" , "<%=monitorNetDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=monitorNetDTO.getInutilhdxValue()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showFlux("<%=monitorNetDTO .getId()%>" , "<%=monitorNetDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=monitorNetDTO.getOututilhdxValue()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showFlux("<%=monitorNetDTO .getId()%>" , "<%=monitorNetDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table></td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center">
													                      						<table border=1 height=15 bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="30%" bgcolor="#ffff00" align="center"><%=generalAlarm%></td>
																                      					<td width="30%" bgcolor="orange" align="center"><%=urgentAlarm%></td>
																                      					<td width="30%" bgcolor="#ff0000" align="center"><%=seriousAlarm%></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list"><%=monitorNetDTO.getCollectType()%></td>
					        													<td align="center" class="body-data-list">
																					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=monitorNetDTO.getId()%>','<%=monitorNetDTO.getIpAddress()%>') alt="右键操作">
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
