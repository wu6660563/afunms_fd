<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.common.util.SessionConstant"%>

<%
  	String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
  	List list = null;
  	Date c1=new Date();
	String timeFormat = "MM-dd HH:mm:ss";
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
	List networklist = (List)session.getAttribute("networklist");
	List hostlist = (List)session.getAttribute("hostlist");
	String hostsize = "0";
	if(hostlist != null && hostlist.size()>0)hostsize=hostlist.size()+"";
	String dbsize = (String)session.getAttribute("dbsize");
	if(dbsize == null)dbsize="0";
	String securesize = (String)session.getAttribute("securesize");
	if(securesize == null)securesize="0";
	String servicesize = (String)session.getAttribute("servicesize");
	if(servicesize == null)servicesize="0";
	String midsize = (String)session.getAttribute("midsize");
	if(midsize == null)midsize="0";
	String routesize = (String)session.getAttribute("routesize");
	if(routesize == null)routesize="0";
	String switchsize = (String)session.getAttribute("switchsize");
	if(switchsize == null)switchsize="0";
		ManageXmlDao dao = new ManageXmlDao();
    ManageXml vo = (ManageXml)dao.findByView("1");
    dao.close();
    String topo_name = "物理根图";
    String home_topo_view = "network.jsp";
    if(vo!=null){
      topo_name = vo.getTopoTitle();
      home_topo_view = vo.getXmlName();
    }
    session.setAttribute(SessionConstant.HOME_TOPO_VIEW,home_topo_view); 
    ManageXmlDao mxdao = new ManageXmlDao();
    ManageXml mxvo = (ManageXml)mxdao.findByBusView("1");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.3.2.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.2.6.js"></script>
		
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
		
			function hideMenu(){
				var element = document.getElementById("container-menu-bar").parentElement;
				var display = element.style.display;
				if(display == "inline"){
					hideMenuBar();
				}else{
					showMenuBar();
				}
			}
			
			function showMenuBar(){
				var element = document.getElementById("container-menu-bar").parentElement;
				element.style.display = "inline";
			}
			
			function hideMenuBar(){
				var element = document.getElementById("container-menu-bar").parentElement;
				element.style.display = "none";
			}
			var fatherXML = "<%=home_topo_view%>";
function redirectUrl(){
	if(fatherXML=="network.jsp"){
	    window.open('<%=rootPath%>/topology/network/index.jsp','window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
	} else {
	    window.open('<%=rootPath%>/topology/submap/index.jsp?submapXml='+fatherXML,'window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
	}
}
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();hideMenuBar();">
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar" height="100%">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>
							</tr>
						</table>
					</td>
					<td height="100%" width="3px">
						<img src="<%=rootPath%>/common/images/arrow_close.jpg" onclick="hideMenu();">
					</td>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content" style="width: 99%">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 系统快照 </td>
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
									  								<td align="center">
									   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getRouterStatus(),1)%>" ><br>
									   									<a href="<%=rootPath%>/network.do?action=monitorroutelist&jp=1">路由器(<%=routesize%>)</a>
									   								</td>
									  								<td align="center">
									   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getSwitchStatus(),2)%>" ><br>
									   									<a href="<%=rootPath%>/network.do?action=monitorswitchlist&jp=1">交换机(<%=switchsize%>)</a>
									   								</td>   
									  								<td align="center">
									   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServerStatus(),3)%>"><br>
									   									<a href="<%=rootPath%>/network.do?action=monitorhostlist&jp=1">服务器(<%=hostsize%>)</a>
									   								</td>
									   								<td align="center">
									   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),4)%>"><br>
									   									<a href="<%=rootPath%>/db.do?action=list&jp=1">数据库(<%=dbsize%>)</a>
									   								</td>
									  								<td align="center">
									  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getMiddleStatus(),5)%>"><br>
									  									<a href="<%=rootPath%>/service.do?action=list&category=services">中间件(<%=midsize%>)</a>
									  								</td>
									  								<td align="center">
									  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServiceStatus(),6)%>"><br>
									  									<!--<img src="<%=rootPath%>/resource/image/service.gif" ><br>-->
									  									<a href="<%=rootPath%>/service.do?action=list&category=services">服务(<%=servicesize%>)</a>
									  								</td>
									  								<td align="center">
									  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getFirewallStatus(),7)%>">
									  									<!--<img src="<%=rootPath%>/resource/image/topo/firewall/firewall.gif">-->
									  									<br>
									  									<a href="#">安全(<%=securesize%>)</a>
									  								</td>
									  								<td align="center" bgcolor=#ffffff>
									  									<img src="<%=rootPath%>/resource/image/ariconditioner.gif"><br>
									  									<a href="#">环境</a>
									  								</td>
																</tr>	
															</table>
		        										</td>
		        									</tr>
		        									<tr>
		        										<td>
		        											<table>
																<tr>
																	<td width=49% align='center' height="300"> 
									          			                <table width=100% height="100%" border="1" align='center'>
												          			        <tr>
																				<td align='center' height="24"><b>网络拓扑图-<%=topo_name%></b></td>
															              	</tr>
													              			<tr> 
													                			<td align='center' >
													                			    <iframe name="topo_Frame" src="<%=rootPath%>/topology/network/h_showMap.jsp?zoom=<%=vo.getPercent()%>" width="99%" height="99%" scrolling="No" frameborder="0" noresize></iframe>
																				</td>
													              			</tr>
													            		</table>
			          			            						</td>
			          			            						<td width=49% align='center' height="300">
								          			                <%
																        if(mxvo!=null){
																    %>
																        <table width=100%  height="100%" border="1" align='center'>
									          			                    <tr>
																				<td align='center' height="24"><b><%=mxvo.getTopoName()%></b></td>
															              	</tr>
													              			<tr> 
													                			<td align='center'> 
													                				<div id="flashcontent_0">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																					    var so = new SWFObject("<%=rootPath%>/flex/h_topo.swf?xml=<%=mxvo.getXmlName().replace("jsp","xml")%>&toponame=<%=mxvo.getTopoName()%>&x=<%=mxvo.getPercent()%>&y=<%=mxvo.getPercent()%>", "h_topo", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent_0");
																					</script> 				
													                			</td>
													              			</tr>
													            		</table>
																    <%    
																        } else {
																    %>
																        <table width=100%  height="100%" border="1" align='center'>
													              			<tr> 
													                			<td align='center'> 
													                				<div id="flashcontent_0">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																					    var so = new SWFObject("<%=rootPath%>/flex/equip_alarm_List.swf", "equip_alarm_List", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent_0");
																					</script> 				
													                			</td>
													              			</tr>
													            		</table>
																    <%
																        }
																    %>
			          			            						</td>
									          			        </tr>
									          			    </table>
										            	</td>
						          					</tr>
													<tr>
							            				<td>
							            				    <table width="100%" height="300" align='center'>
									          			        <tr>
									          			            <td width=49% align='center'>
																		<table width="100%" align='center' height="100%">
																			
																	  		<%
																	  			int index3 = 0;
																	  			List _rpceventlist = (List)session.getAttribute("rpceventlist");
																	  			if(_rpceventlist != null && _rpceventlist.size()>0){
																	          	   
																			 %>
																		    <tr> 
													                			<td align=center> 
													                				<div id="flashcontent1">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Event_Grid_List.swf", "Event_Grid_List", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent1");
																					</script> 				
													                			</td>
													              			</tr>  
																	        <%
																	          	} else {
																	        %>
																	
																			<!-- 没有设备显示时调用 -->
																			<tr>
																				<td>
																					<table width="100%" height="100%" style="background-color:#ffffff;">
																						<tr>
																							<td width="75%" valign="middle" align="center"><img  align="absmiddle" style="margin-right:3px;" src="<%=rootPath%>/img/talk.gif" />该视图中没有项目可显示</td>
																							<td width="25%" valign="bottom" align="right"><img style="margin:30px;" src="<%=rootPath%>/img/bg.gif" /></td>
																					</table>
																				</td>
																			</tr>
																			<%
																				}
																			 %>				
																		</table>
														            </td>
														            <td width=49% align='center'>
													            		<table width=100% align='center' height="100%">
													              			<tr> 
													                			<td align='center'>
													                				<div id="flashcontent0">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Event_Pie.swf", "Event_Pie", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent0");
																					</script> 				
													                			</td>
													              			</tr>
													            		</table>
										            	            </td>
													            </tr>
													        </table>
													    </td>
						          					</tr>
			          						<!--
										<tr>
								          	<td width=100% colspan=3>
								          		<table width="100%" align='center'>
								              		<tr> 
				                						<td align='center'> 
				                							<div id="flashcontent_yewu1">
														       <strong>You need to upgrade your Flash Player</strong>
													        </div>
								          	                <script type="text/javascript">
									                            var so = new SWFObject("<%=rootPath%>/flex/NetworkTopology.swf", "NetworkTopology", "99%", "280", "9", "#FFFFFF");
									                            so.write("flashcontent_yewu1");
								                            </script>
								                        </td>
								              		</tr>
							            		</table>
							          	    </td>
							          	</tr>
									-->
										          	<% 
										          		if(networklist != null && networklist.size()>0){
										          	%>
									           		<tr>
									           			<td>
									           				<table>
									           					<tr>
												          			<td width="33%" align=center>
												            			<table width="100%" height="220">
												              				<tr> 
												                				<td  align=center> 
									                								<div id="flashcontent2">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Network_CPU_TOP10.swf", "Column_Network_Cpu_Top10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent2");
																					</script>
																				</td>
																			</tr>
																        </table>
																    </td>
																    <td width="33%" align=center>
																		<table width=100% height="220">
																			<tr> 
																				<td width="100%" > 
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Network_In_TOP10.swf", "Column_Network_In_TOP10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent3");
																					</script>				
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="33%" align=center>
														          		<table width=100% height="220">
														              		<tr> 
														                		<td width="100%"> 
														                			<div id="flashcontent4">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Network_Out_TOP10.swf", "Column_Network_Out_TOP10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent4");
																					</script>				
														                		</td>
														              		</tr>             
														            	</table> 
														          	</td>
									          					</tr>
									          				</table>
									          			</td>
									          		</tr>
											 		<%
											 			}
											 			if(hostlist != null && hostlist.size()>0){
											 		%>
									 				<tr>
									 					<td>
									 						<table>
									 							<tr>
																	<td width="33%" align=center>
																		<table width="100%" height="220" cellpadding="0" cellspacing="0">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent5">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Host_CPU_TOP10.swf", "Column_Host_CPU_TOP10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent5");
																					</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="220">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent6">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Host_Memory_TOP10.swf", "Column_Host_Memory_TOP10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent6");
																					</script>			
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="220">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent7">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Column_Host_Disk_TOP10.swf", "Column_Host_Disk_TOP10", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent7");
																					</script>				
																				</td>
																			</tr>             
																		</table> 
																	</td>
																</tr>
															</table>
														</td>
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
