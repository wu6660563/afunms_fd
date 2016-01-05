<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.flex.networkTopology.NetworkMonitor"%>
<%@page import="org.apache.commons.collections.functors.WhileClosure"%>
<%@page import="org.apache.commons.lang.SystemUtils"%>
<%@page import="org.apache.commons.net.DefaultDatagramSocketFactory"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.vo.EventVo"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.initialize.ResourceCenter"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.afunms.system.dao.FunctionDao"%>
<%@page import="com.afunms.system.model.Function"%>  
<%@page import="com.afunms.home.role.dao.HomeRoleDao"%>
<%@page import="com.afunms.home.user.dao.HomeUserDao"%>
<%
    // 项目路径
    String rootPath = request.getContextPath();
    // 统一门户
    response.setHeader("P3P","CP=CAO PSA OUR");
    // 路由器数量
    String routeSize = (String) request.getAttribute("routeSize");
    // 交换机数量
    String switchSize = (String) request.getAttribute("switchSize");
    // 服务器数量
    String hostSize = (String) request.getAttribute("hostSize");
    // 数据库数量
    String DBSize = (String) request.getAttribute("DBSize");
	// 中间件数量
    String middleSize = (String) request.getAttribute("middleSize");
    // 服务数量
    String serviceSize = (String) request.getAttribute("serviceSize");
    // 安全数量
    String secureSize = (String) request.getAttribute("secureSize");

    // 路由器系统快照状态图片
    String routeSnapStatusImage = (String) request.getAttribute("routeSnapStatusImage");
    // 交换机系统快照状态图片
    String switchSnapStatusImage = (String) request.getAttribute("switchSnapStatusImage");
    // 服务器系统快照状态图片
    String hostSnapStatusImage = (String) request.getAttribute("hostSnapStatusImage");
    // 数据库系统快照状态图片
    String DBSnapStatusImage = (String) request.getAttribute("DBSnapStatusImage");
    // 中间件系统快照状态图片
    String middleSnapStatusImage = (String) request.getAttribute("middleSnapStatusImage");
    // 服务系统快照状态图片
    String serviceSnapStatusImage = (String) request.getAttribute("serviceSnapStatusImage");
    // 安全系统快照状态图片
    String secureSnapStatusImage = (String) request.getAttribute("secureSnapStatusImage");

    // 关键业务
    Hashtable bussinessviewHash = (Hashtable) request.getAttribute("bussinessviewHash");

    // 拓扑图
    ManageXml manageXml = (ManageXml) request.getAttribute("manageXml");

    // 拓扑图标题
    String topoTitle = "物理根图";
    // 拓扑图名称
    String xmlName = "network.jsp";
    String zoom = "1";
    if (manageXml != null) {
        topoTitle = manageXml.getTopoTitle();
        xmlName = manageXml.getXmlName();
        zoom = manageXml.getPercent() + "";
    }

    // 告警状态
    String alarmState = (String) request.getAttribute("alarmState");
    // 当天告警类别数量
    String[][] alarmTypeSizeByDay = (String[][])request.getAttribute("alarmTypeSizeByDay");
    // 当周告警类别数量
    String alarmTypeSizeByWeek = (String)request.getAttribute("alarmTypeSizeByWeek");
    // 当日告警数
    String alarmSizeByDay = (String) request.getAttribute("alarmSizeByDay");
    // 当周告警数
    String alarmSizeByWeek = (String) request.getAttribute("alarmSizeByWeek");
    // 告警关闭率图片
    String closedAlarmPicFile = (String) request.getAttribute("closedAlarmPicFile");

    Hashtable smallHashtable = (Hashtable) request.getAttribute("smallHashtable");
    Hashtable bigHashtable = (Hashtable) request.getAttribute("bigHashtable");
    int lastOne = 0;//是否最后一个元素
    Enumeration RLKey = smallHashtable.elements();
    while (RLKey.hasMoreElements()) {
		String accRole = RLKey.nextElement().toString();
		if (accRole.equals("1")) {
		    lastOne++;
		}
    }

    //默认选择该用户第一个所属业务作为跳转的treeBid

    //该所属业务中没有路由器，且该用户没有下一个所属业务，不跳转！
    User user = (User) session
		    .getAttribute(SessionConstant.CURRENT_USER);
    String[] bids = user.getBusinessids().split(",");
    String defaultbid = "";
    for (String bid : bids) {
		if (bid != null && !bid.equals("")) {
		    defaultbid = bid;
		    break;
		}
    }
    //该所属业务中没有路由器，且该用户下一个所属业务中存在路由器，将该业务id作为treeBid,跳转！
    String treeBidRouter = defaultbid;
    String treeBidHost = defaultbid;
    String treeBidSwitch = defaultbid;
    String treeBidDb = defaultbid;
    String treeBidMid = defaultbid;
    String treeBidService = defaultbid;
    String treeBidSecu = defaultbid;
    Hashtable treeBidHash = (Hashtable) request
		    .getAttribute("treeBidHash");
    if (treeBidHash != null) {
		if (treeBidHash.containsKey("treeBidRouter")) {
		    treeBidRouter = (String) treeBidHash.get("treeBidRouter");
		}
		if (treeBidHash.containsKey("treeBidHost")) {
		    treeBidHost = (String) treeBidHash.get("treeBidHost");
		}
		if (treeBidHash.containsKey("treeBidSwitch")) {
		    treeBidSwitch = (String) treeBidHash.get("treeBidSwitch");
		}
		if (treeBidHash.containsKey("treeBidDb")) {
		    treeBidDb = (String) treeBidHash.get("treeBidDb");
		}
		if (treeBidHash.containsKey("treeBidMid")) {
		    treeBidMid = (String) treeBidHash.get("treeBidMid");
		}
		if (treeBidHash.containsKey("treeBidService")) {
		    treeBidService = (String) treeBidHash.get("treeBidService");
		}
		if (treeBidHash.containsKey("treeBidSecu")) {
		    treeBidSecu = (String) treeBidHash.get("treeBidSecu");
		}
    }

    String routepath = "/perform.do?action=monitornodelist&flag=1&category=net_router&treeBid="
		    + treeBidRouter;
    String switchpath = "/perform.do?action=monitornodelist&flag=1&category=net_switch&treeBid="
		    + treeBidSwitch;
    String hostpath = "/perform.do?action=monitornodelist&flag=1&category=net_server&treeBid="
		    + treeBidHost;
    String dbpath = "/db.do?action=list&flag=1&treeBid=" + treeBidDb;
    String midpath = "/middleware.do?action=list&flag=1&category=middleware&treeBid="
		    + treeBidMid;
    String servicepath = "/service.do?action=list&flag=1&treeBid="
		    + treeBidService;
    String securepath = "/perform.do?action=monitornodelist&flag=1&category=safeequip&treeBid="
		    + treeBidSecu;

    //存储链接路(暂时给定路由器的链接)   无测试环境<Task未完成>
    String storagepath = "/perform.do?action=monitornodelist&flag=1&category=net_router&treeBid="
		    + treeBidRouter;
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
		
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/AlarmSummarizeService.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeviceSnapshotService.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/TopoRemoteService.js"></script>
        
        <script type="text/javascript" src="<%=rootPath%>/js/home.js"></script>
        
        <link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
        <!-- GC -->
		<!-- LIBS -->
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<!-- ENDLIBS -->
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
        <!-- EXT做的重点资源标签页home.js  css样式修改 -->
        <style type="text/css">
            body {
            	background: url(images/bg.jpg)
            }
            
            .x-tab-strip-top .x-tab-right {
            	background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
            }
            
            .x-tab-strip-top .x-tab-left {
            	background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
            }
            
            .x-tab-strip-top .x-tab-strip-inner {
            	background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
            }
            
            .x-tab-panel-body {
            	border-bottom-color: #EAEAEA;
            	border-left-color: #EAEAEA;
            	border-right-color: #EAEAEA;
            	border-top-color: #EAEAEA;
            }
            
            .x-tab-panel-header {
            	background-color: #EAEAEA;
            	border-bottom-color: #EAEAEA;
            	border-left-color: #EAEAEA;
            	border-right-color: #EAEAEA;
            	border-top-color: #EAEAEA;
            }
            
            .x-tab-panel-header-plain .x-tab-strip-spacer {
            	background-color: #EAEAEA;
            	border-bottom-color: #EAEAEA;
            	border-left-color: #EAEAEA;
            	border-right-color: #EAEAEA;
            	border-top-color: #EAEAEA;
            }
            
            .x-panel {
            	border-bottom-color: #EAEAEA;
            	border-left-color: #EAEAEA;
            	border-right-color: #EAEAEA;
            	border-top-color: #EAEAEA;
            }
            
            .x-panel-body {
            	border-bottom-color: #EAEAEA;
            	border-left-color: #EAEAEA;
            	border-right-color: #EAEAEA;
            	border-top-color: #EAEAEA;
            }
            
            UL.x-tab-strip-top {
            	background-color: #EAEAEA;
            }
        </style>

		<script type="text/javascript">
            var fatherXML = "<%=xmlName%>";
    		function redirectUrl() {
                if (fatherXML=="network.jsp") {
		            window.open('<%=rootPath%>/topology/network/index.jsp','window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
		        } else {
		            window.open('<%=rootPath%>/topology/submap/index.jsp?submapXml='+fatherXML,'window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
    	        }
    		}
	   </script>

	</head>
	<body id="body" class="body">
		<!-- 定义一个空div -->
		<span id="rootpath" value="<%=rootPath%>"></span>
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content" style="width: 99%" border="0">
										<tr>
											<td>
												<table id="content-body" class="content-body"
													style="border-left: #737272 0px solid; border-right: #737272 0px solid;">
													<tr>
														<td>
															<table>
																<tr>
																	<%
																	    int count = 0;
																	    //第一块显示开始 
																	    if (smallHashtable.get("设备快照").equals(1)) {
																			count++;
																	%>
																	<!-- 设备快照 -->
																	<td width=50% align='center' height="300">
																		<table width=100% height="100%" border="0"
																			align='center'>
																			<tr valign="top">
																				<td>
																					<table id="content-header" class="content-header">
																						<tr>
																							<td align="left" width="5">
																								<img src="<%=rootPath%>/common/images/right_t_1.jpg" width="5" height="29" />
																							</td>
																							<td align='center' height="29" class="content-title" style="text-align: center;">
																								<b>设备快照</b>
																							</td>
																							<td align="right">
																								<img src="<%=rootPath%>/common/images/right_t_3.jpg" width="5" height="29" />
																							</td>
																						<tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td width="100%">
																					<table width="100%">
																						<tr>
																							<td align="center">
																								<img id="routeSnapStatusImage" src="<%=rootPath%>/resource/<%=routeSnapStatusImage%>">
																								<br>
																								<a id="routeInfoText" href="javascript:void(0);" onClick="showTree('<%=routepath%>');">路由器(<%=routeSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="switchSnapStatusImage" src="<%=rootPath%>/resource/<%=switchSnapStatusImage%>">
																								<br>
																								<a id="switchInfoText" href="javascript:void(0);" onClick="showTree('<%=switchpath%>');">交换机(<%=switchSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="hostSnapStatusImage" src="<%=rootPath%>/resource/<%=hostSnapStatusImage%>">
																								<br>
																								<a id="hostInfoText" href="javascript:void(0);" onClick="showTree('<%=hostpath%>');">服务器(<%=hostSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="DBSnapStatusImage" src="<%=rootPath%>/resource/<%=DBSnapStatusImage%>">
																								<br>
																								<a id="DBInfoText" href="javascript:void(0);" onClick="showTree('<%=dbpath%>');">数据库(<%=DBSize%>)</a>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<div style="height: 50px;"></div>
																							</td>
																						</tr>
																						<tr>
																							<td align="center">
																								<img id="middleSnapStatusImage" src="<%=rootPath%>/resource/<%=middleSnapStatusImage%>">
																								<br>
																								<a id="middleInfoText" href="javascript:void(0);" onClick="showTree('<%=midpath%>');">中间件(<%=middleSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="serviceSnapStatusImage" src="<%=rootPath%>/resource/<%=serviceSnapStatusImage%>">
																								<br>
																								<a id="serviceInfoText" href="javascript:void(0);" onClick="showTree('<%=servicepath%>');">服务(<%=serviceSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="secureSnapStatusImage" src="<%=rootPath%>/resource/<%=secureSnapStatusImage%>">
																								<br>
																								<a id="secureInfoText" href="javascript:void(0);" onClick="showTree('<%=securepath%>');">安全(<%=secureSize%>)</a>
																							</td>
																							<td align="center" bgcolor=#ffffff>
                                                                                                &nbsp;
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<div style="height: 20px;"></div>
																							</td>
																						</tr>
																					</table>
																				<td>
																			</tr>
																			<tr valign="bottom">
																				<td>

																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    //是否添加列空白
																			if (count % 2 == 1) {
																	%>
																	<td style="background: #bec7ce;">
																		&nbsp;
																	</td>
																	<%
																	    if (count == lastOne) {
																				System.out.println(count);
																	%>
																	<td width=50% align='center' height="270">
																	</td>
																	<%
																	    }
																			}
																	%>
																	<%
																	    //是否换新行开始 
																			if (count % 2 == 0 || count == lastOne) {
																	%>
																</tr>
															</table>
														</td>
													</tr>
													<tr style="height: 7px; background: #bec7ce;">
														<td>
															<div></div>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<%
																	    }//是否换新航结束
																	    }
																	%>
																	<%
																	    //第二块显示开始
																	    if (smallHashtable.get("关键业务").equals(1)) {
																			count++;
																	%>
																	<!-- 关键业务 -->
																	<td width=50% align='center' height="300">
																		<table width=100% height="100%" border="0"
																			align='center'>
																			<tr valign="top" id="keybusiness_tr">
																				<td>
																					<table id="content-header" class="content-header">
																						<tr>
																							<td align="left" width="5">
																								<img
																									src="<%=rootPath%>/common/images/right_t_1.jpg"
																									width="5" height="29" />
																							</td>
																							<td align='center' height="29"
																								class="content-title"
																								style="text-align: center;">
																								<b>关键业务</b>
																							</td>
																							<td align="right">
																								<img
																									src="<%=rootPath%>/common/images/right_t_3.jpg"
																									width="5" height="29" />
																							</td>
																						<tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<table>
																						<tr align="center">
																							<!-- 数据库、网络设备、服务器等 -->
																							<%
																							    Iterator iterator = bussinessviewHash.keySet().iterator();
                                                                                                System.out.println(bussinessviewHash.size() + "===bussinessviewHash");
																									int num = 0;
																									int maxnum = 9;//最大允许显示的个数 
																									int index = 0;//已经显示的个数
                                                                                                    NodeAlarmService nodeAlarmService = new NodeAlarmService();
																									while (iterator.hasNext()) {
																									    num++;
																									    index++;
																									    if (index > maxnum) {
																										break;
																									    }
																									    ManageXml manageXmlTemp = (ManageXml) iterator.next();
																									    String toponame = manageXmlTemp.getTopoName();
																									    int alarmstatus = 0;
																									    List<NodeDTO> nodeList = (List<NodeDTO>) bussinessviewHash
																										    .get(manageXmlTemp);
																									    String imgname = "bussiness.gif";
																									    if (nodeList != null) {
                                                                                                          int status = nodeAlarmService.getMaxAlarmLevel(nodeList);
                                                                                                          if (status > alarmstatus) {
                                                                                                            imgname = "bussiness_alarm.gif";
                                                                                                          }
																									    }
																									    if (num > 3) {
																										num = 1;
																							%>
																						</tr>
																						<tr>
																							<td>
																								<div style="height: 50px;"></div>
																							</td>
																						</tr>
																						<!-- 每行三个业务视图 -->
																						<tr align="center">
																							<%
																							    }
																							%>
																							<td align="center" valign="top">
																								<img
																									src="<%=rootPath + "/resource/image/topo/"
				    + imgname%>">
																								<br>
																								<%-- '<%=rootPath%>/detail/dispatcher.jsp?flag=1&id=bus4' --%>
																								<%
																								    String rightFramePath = "/detail/dispatcher.jsp?flag=1&id=bus"
																											    + manageXmlTemp.getId();
																								%>
																								<a href="javascript:void(0);"
																									onClick="showTree('<%=rightFramePath%>');"><%=toponame%></a>
																							</td>
																							<%
																							    }
																							%>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<tr valign="bottom">
																				<td>

																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    //是否添加列空白
																			if (count % 2 == 1) {
																	%>
																	<td style="background: #bec7ce;">
																		&nbsp;
																	</td>
																	<%
																	    if (count == lastOne) {
																				System.out.println(count);
																	%>
																	<td width=50% align='center' height="270">
																	</td>
																	<%
																	    }
																			}
																	%>
																	<%
																	    //是否换新行开始 
																			if (count % 2 == 0 || count == lastOne) {
																	%>
																</tr>
															</table>
														</td>
													</tr>
													<tr style="height: 7px; background: #bec7ce;">
														<td>
															<div></div>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<%
																	    }//是否换新航结束
																	    }
																	%>
																	<%
																	    if (smallHashtable.get("网络拓扑图").equals(1)) {
																			count++;
																	%>
																	<!-- 网络拓扑图  -->
																	<td width=50% align='center' height="270">
																		<table width=100% height="100%" border="0"
																			align='center'>
																			<tr>
																				<td align='center' height="24">
																					<table id="content-header" class="content-header">
																						<tr>
																							<td align="left" width="5">
																								<img
																									src="<%=rootPath%>/common/images/right_t_1.jpg"
																									width="5" height="29" />
																							</td>
																							<td align='center' height="29"
																								class="content-title"
																								style="text-align: center;">
																								<b>网络拓扑图-<%=topoTitle%></b>
																							</td>
																							<td align="right">
																								<img
																									src="<%=rootPath%>/common/images/right_t_3.jpg"
																									width="5" height="29" />
																							</td>
																						<tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td align='center'>
																					<iframe name="topo_Frame"
																						src="<%=rootPath%>/topology/network/h_showMap.jsp?zoom=<%=zoom%>"
																						width="99%" height="99%" scrolling="No"
																						frameborder="0" noresize></iframe>
																				</td>
																			</tr>
																			<tr valign="bottom" style="height: 4px;">
																				<td>
																					<table id="content-footer" class="content-footer">
																						<tr>
																							<td>

																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    //是否添加列空白
																			if (count % 2 == 1) {
																	%>
																	<td style="background: #bec7ce;">
																		&nbsp;
																	</td>
																	<%
																	    if (count == lastOne) {
																				System.out.println(count);
																	%>
																	<td width=50% align='center' height="270">
																	</td>
																	<%
																	    }
																			}
																	%>
																	<%
																	    //是否换新行开始 
																			if (count % 2 == 0 || count == lastOne) {
																	%>
																</tr>
															</table>
														</td>
													</tr>
													<tr style="height: 7px; background: #bec7ce;">
														<td>
															<div></div>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<%
																	    }//是否换新航结束
																	    }
																	%>
																	<%
																	    if (smallHashtable.get("设备性能").equals(1)) {
																			count++;
																	%>
																	<!-- 设备性能 -->
																	<td width=50% align='center' height="270">
																		<table width=100% height="100%" border="0"
																			align='center'>
																			<tr>
																				<td id='devicexn_title' align='center' height="24">
																					<table id="content-header" class="content-header">
																						<tr>
																							<td align="left" width="5">
																								<img
																									src="<%=rootPath%>/common/images/right_t_1.jpg"
																									width="5" height="29" />
																							</td>
																							<td align='center' height="29"
																								class="content-title"
																								style="text-align: center;">
																								<b>性能</b>
																							</td>
																							<td align="right">
																								<img
																									src="<%=rootPath%>/common/images/right_t_3.jpg"
																									width="5" height="29" />
																							</td>
																						<tr>
																					</table>
																				</td>
																			</tr>
																			<tr id="tab_list_tr">
																				<td>
																					<!-- EXT的做的TAB 详见home.js文件 -->
																					<!-- 加重点资源Tab页 -->
																					<div id="tab_list" style="width: 100%;"></div>
																				</td>
																			</tr>
																			<tr valign="bottom">
																				<td>

																				</td>
																			</tr>
																		</table>
																	</td>

																	<%
																	    //是否添加列空白
																			if (count % 2 == 1) {
																	%>
																	<td style="background: #bec7ce;">
																		&nbsp;
																	</td>
																	<%
																	    if (count == lastOne) {
																				System.out.println(count);
																	%>
																	<td width=50% align='center' height="270">
																	</td>
																	<%
																	    }
																			}
																	%>
																	<%
																	    //是否换新行开始 
																			if (count % 2 == 0 || count == lastOne) {
																	%>
																</tr>
															</table>
														</td>
													</tr>
													<tr style="height: 7px; background: #bec7ce;">
														<td>
															<div></div>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<%
																	    }//是否换新航结束
																	    }
																	    
																	%> 
																</tr>
															</table>
														</td>
													</tr>
													<% 
													    if (bigHashtable.get("告警信息").equals(1)) {
													%>
													<tr>
														<td>
															<table>
																<tr>
																	<!-- 告警信息汇总 -->
																	<td align='center' height="24" colspan="2">
																		<table id="content-header" class="content-header">
																			<tr>
																				<td align="left" width="5">
																					<img
																						src="<%=rootPath%>/common/images/right_t_1.jpg"
																						width="5" height="29" />
																				</td>
																				<td align='center' height="29" class="content-title"
																					style="text-align: center;">
																					<b>告警汇总信息</b>
																				</td>
																				<td align="right">
																					<img
																						src="<%=rootPath%>/common/images/right_t_3.jpg"
																						width="5" height="29" />
																				</td>
																			<tr>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td bgcolor="#FFFFFF">
																		<table>
																			<tr>
																				<td valign=top>
																					<table>
																						<tr>
																							<td align=center>
																								<div id="pie">
																									<!--  <strong>You need to upgrade your Flash Player</strong>-->
																								</div>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<table>
																									<tr>
																										<td width=60>
																										</td>
																										<td>
																											<img id="closedAlarmPicFile">
																										</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																					</table>
																				</td>
																				<td valign=top>
																					<table>
																						<tr>
																							<td id="alarmTypeSizeByDayTD">
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<div id="column">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																							</td>
																						<tr>
																					</table>
																				</td>
																				<td>
																					<table>
																						<tr>
																							<td>
																								<div id="lineOne">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<div id="lineTwo">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
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
													<tr style="height: 7px; background: #bec7ce;">
														<td>
															<div></div>
														</td>
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
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
			/**
			*跳转到性能页面
			*/
			function showTree(rightFramePath){
				//将等于和and转换一下  
				//rightFramePath = rightFramePath.replaceAll("&","-and-");
				//rightFramePath = rightFramePath.replaceAll("=","-equals-");
				//使用循环，将等于和and转换一下  
				while(rightFramePath.indexOf("&") != -1){
					rightFramePath = rightFramePath.replace("&","-and-");
				}
				while(rightFramePath.indexOf("=") != -1){
					rightFramePath = rightFramePath.replace("=","-equals-");
				}
				//alert(rightFramePath);
				window.location.href =  "<%=rootPath%>/performance/index.jsp?flag=1&rightFramePath="+rightFramePath;
			}
            
            <%
                if (bigHashtable.get("告警信息").equals(1)) {
            %>
            AlarmSummarizeService.getAlarmState({
                callback:function(data){
                    var alarmState = data;
                    if(alarmState != "0"){
                        var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","380", "280", "8", "#FFFFFF");
                        so.addVariable("path", "<%=rootPath%>/amchart/");
                        so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/alarmStatepie.xml"));
                        so.addVariable("chart_data",alarmState);
                        so.write("pie");
                    }else{
                        var _div=document.getElementById("pie");
                        var img=document.createElement("img");
                        img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
                        img.setAttribute("valign","center");
                        img.setAttribute("width","380");
                        img.setAttribute("height","280");
                        _div.appendChild(img);
                    }
                }
            });

            AlarmSummarizeService.getAlarmTypeSizeByWeek({
                callback:function(data){
                    var alarmTypeSizeByWeek = data;
                    var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","480", "320", "8", "#FFFFF");
                    so.addVariable("path", "<%=rootPath%>/amchart/");
                    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/lastweekColumn.xml"));
                    so.addVariable("chart_data", alarmTypeSizeByWeek);
                    so.write("column");
                }
            });
            
            AlarmSummarizeService.getAlarmSizeByDay({
                callback:function(data){
                    var alarmSizeByDay = data;
                    var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline","280", "300", "8", "#FFFFFF");
					so.addVariable("path", "<%=rootPath%>/amchart/");
					so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dayalarm_setting.xml"));
					so.addVariable("chart_data", alarmSizeByDay);
					so.write("lineOne");
                }
            });
            
            AlarmSummarizeService.getAlarmSizeByWeek({
                callback:function(data){
                    var alarmSizeByWeek = data;
                    var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline","280", "300", "8", "#FFFFFF");
                    so.addVariable("path", "<%=rootPath%>/amchart/");
                    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/weekalarm_settings.xml"));
                    so.addVariable("chart_data", alarmSizeByWeek);
                    so.write("lineTwo");
                }
            });
            
            AlarmSummarizeService.getAlarmSizeByWeek({
                callback:function(data){
                    var alarmSizeByWeek = data;
                    var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline","280", "300", "8", "#FFFFFF");
                    so.addVariable("path", "<%=rootPath%>/amchart/");
                    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/weekalarm_settings.xml"));
                    so.addVariable("chart_data", alarmSizeByWeek);
                    so.write("lineTwo");
                }
            });
            AlarmSummarizeService.getAlarmTypeSizeByDay({
                callback:function(data){
                    var alarmTypeSizeByDay = data;
                    var alarmTypeSizeByDay_var = '<table cellpadding=8 height=296 border="1" bordercolor="#7599d7" style="border: 1px solid #7599d7; border-collapse: collapse;">';
                    for (var i = 0; i < alarmTypeSizeByDay.length; i++) {
                        alarmTypeSizeByDay_var += '<tr>';
                        for (var j = 0; j < alarmTypeSizeByDay[i].length; j++) {
                            if (j == 0) {
                                alarmTypeSizeByDay_var += '<td width="80" height="25" align=center>';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 1) {
                                // 提示
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="blue">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 2) { 
                                // 普通
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="yellow">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 3) { 
                                // 严重
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="orange">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 4) {
                                // 紧急
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="red">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else {
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center>';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            }
                        }
                        alarmTypeSizeByDay_var += '</tr>';
                    }
                    alarmTypeSizeByDay_var += '</table>';
                    var alarmTypeSizeByDayTable = document.getElementById("alarmTypeSizeByDayTD");
                    alarmTypeSizeByDayTD.innerHTML = alarmTypeSizeByDay_var;
                }
            });
            AlarmSummarizeService.getClosedAlarmPic('<%=user.getUserid() + "_" + user.getId()%>', {
                callback:function(data){
                    var getClosedAlarmPic = data;
                    var closedAlarmPicFile = document.getElementById("closedAlarmPicFile");
                    closedAlarmPicFile.src="<%=rootPath%>/resource/image/jfreechart/reportimg/" + getClosedAlarmPic;
                }
            });
            <%
             }
            %>
            <%
		    if (smallHashtable.get("设备快照").equals(1)) {
			%>
				getDeviceSnapshotInfo();
				window.setInterval(getDeviceSnapshotInfo, 3 * 60 * 1000);
			<%
            }
           %>

            function getDeviceSnapshotInfo() {
                var routeInfoText = document.getElementById("routeInfoText");
		        var switchInfoText = document.getElementById("switchInfoText");
		        var hostInfoText = document.getElementById("hostInfoText");
		        var DBInfoText = document.getElementById("DBInfoText");
		        var middleInfoText = document.getElementById("middleInfoText");
		        var serviceInfoText = document.getElementById("serviceInfoText");
		        var secureInfoText = document.getElementById("secureInfoText");
		        
				var routeSnapStatusImage = document.getElementById("routeSnapStatusImage");
		        var switchSnapStatusImage = document.getElementById("switchSnapStatusImage");
		        var hostSnapStatusImage = document.getElementById("hostSnapStatusImage");
		        var DBSnapStatusImage = document.getElementById("DBSnapStatusImage");
		        var middleSnapStatusImage = document.getElementById("middleSnapStatusImage");
		        var serviceSnapStatusImage = document.getElementById("serviceSnapStatusImage");
		        var secureSnapStatusImage = document.getElementById("secureSnapStatusImage");
				DeviceSnapshotService.getAllInfo('<%=user.getBusinessids()%>' , {
					callback:function(data){
	                    var deviceSnapshotInfo = data;
	                    if (deviceSnapshotInfo && deviceSnapshotInfo.length > 0) {
	                    	var routeInfo = deviceSnapshotInfo[0];
	                    	var switchInfo = deviceSnapshotInfo[1];
	                    	var hostInfo = deviceSnapshotInfo[2];
	                    	var DBInfo = deviceSnapshotInfo[3];
	                    	var middleInfo = deviceSnapshotInfo[4];
	                    	var serviceInfo = deviceSnapshotInfo[5];
	                    	var secureInfo = deviceSnapshotInfo[6];
	                    	
	                    	routeInfoText.innerHTML = "路由器(" + routeInfo[0] + ")";
	            	        switchInfoText.innerHTML = "交换机(" + switchInfo[0] + ")";
	            	        hostInfoText.innerHTML = "服务器(" + hostInfo[0] + ")";
	            	        DBInfoText.innerHTML = "数据库(" + DBInfo[0] + ")";
	            	        middleInfoText.innerHTML = "中间件(" + middleInfo[0] + ")";
	            	        serviceInfoText.innerHTML = "服务(" + serviceInfo[0] + ")";
	            	        secureInfoText.innerHTML = "安全(" + secureInfo[0] + ")";
	                    	
	                    	routeSnapStatusImage.src = "<%=rootPath%>/resource/" + routeInfo[1];
	            	        switchSnapStatusImage.src = "<%=rootPath%>/resource/" + switchInfo[1];
	            	        hostSnapStatusImage.src = "<%=rootPath%>/resource/" + hostInfo[1];
	            	        DBSnapStatusImage.src = "<%=rootPath%>/resource/" + DBInfo[1];
	            	        middleSnapStatusImage.src = "<%=rootPath%>/resource/" + middleInfo[1];
	            	        serviceSnapStatusImage.src = "<%=rootPath%>/resource/" + serviceInfo[1];
	            	        secureSnapStatusImage.src = "<%=rootPath%>/resource/" + secureInfo[1];
	                    }
	                }	
				});
			}

           Ext.onReady(tabpanel_var('<%=rootPath%>'));
		</script>
	</body>
</html>
