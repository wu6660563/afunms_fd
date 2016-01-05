<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.*"%>
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
    ManageXml xmlVo = null; 
    String xmlName = "network.jsp";
    String zoom = "1";
    ManageXmlDao xmlDao = new ManageXmlDao();
    try{
    	xmlVo = xmlDao.findByHomeView();
    } catch(Exception e) {
    	e.printStackTrace();
    } finally {
    	xmlDao.close();
    }
    if (manageXml != null) {
        topoTitle = xmlVo.getTopoTitle();
        xmlName = xmlVo.getXmlName();
        zoom = xmlVo.getPercent() + "";
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
            
            var controller = false;     // 是否显示控制器
    		function redirectUrl() {
                if (fatherXML=="network.jsp") {
		            window.open('<%=rootPath%>/topology/network/index.jsp','window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
		        } else {
		            window.open('<%=rootPath%>/topology/submap/index.jsp?submapXml='+fatherXML,'window', 'toolbar=no,height=screen.height,width=screen.width,scrollbars=no,center=yes,resizable=yes');
    	        }
    		}
    		
    		function showController() {
    			if(controller) {
    				result = self.frames('topo_Frame1').showController1(false);
    				if (result == false) {
			            window.alert("您没有选择视图，无控制器可用");
			            return;
			        }
			        
			        document.getElementById("showControll").value = "开启控制器";
        			document.getElementById("showControll").title = "开启显示框内的视图控制器";
    				controller = false;
    			} else {
    				result = self.frames('topo_Frame1').showController1(true);
    				if (result == false) {
			            window.alert("您没有选择视图，无控制器可用");
			            return;
			        }
			        document.getElementById("showControll").value = "关闭控制器";
        			document.getElementById("showControll").title = "关闭显示框内的视图控制器";
			        controller = true;
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
								<td class="td-container-main-content" id="td-container-main-content">
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
																	
																	<td width=100% align='center' height="100%" id="td1">
																		<table width=100% height="100%" border="0" align='center'>
																		    <tr>
																				<td id='devicexn_title' align='center' height="24">
																					<table id="content-header" class="content-header">
																						<tr>
																							<td align='center' height="29"
																								class="content-title"
																								style="text-align: center;">
																								<b>网络拓扑图</b>
																							</td>
																						<tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td align='center'>
																					<div style="overflow: hidden; width: 1680; height: 920;background-color: black;">
																						<iframe name="topo_Frame1" id="topo_Frame1"  
																							src="<%=rootPath%>/topology/network/showMap.jsp?zoom=<%=zoom%>&fileName=<%=xmlName%>&homeFlag=true"
																							width="100%" height="100%" scrolling="No"
																							frameborder="0" noresize></iframe>
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
