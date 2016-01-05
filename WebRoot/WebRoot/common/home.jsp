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
    // ��Ŀ·��
    String rootPath = request.getContextPath();
    // ͳһ�Ż�
    response.setHeader("P3P","CP=CAO PSA OUR");
    // ·��������
    String routeSize = (String) request.getAttribute("routeSize");
    // ����������
    String switchSize = (String) request.getAttribute("switchSize");
    // ����������
    String hostSize = (String) request.getAttribute("hostSize");
    // ���ݿ�����
    String DBSize = (String) request.getAttribute("DBSize");
	// �м������
    String middleSize = (String) request.getAttribute("middleSize");
    // ��������
    String serviceSize = (String) request.getAttribute("serviceSize");
    // ��ȫ����
    String secureSize = (String) request.getAttribute("secureSize");

    // ·����ϵͳ����״̬ͼƬ
    String routeSnapStatusImage = (String) request.getAttribute("routeSnapStatusImage");
    // ������ϵͳ����״̬ͼƬ
    String switchSnapStatusImage = (String) request.getAttribute("switchSnapStatusImage");
    // ������ϵͳ����״̬ͼƬ
    String hostSnapStatusImage = (String) request.getAttribute("hostSnapStatusImage");
    // ���ݿ�ϵͳ����״̬ͼƬ
    String DBSnapStatusImage = (String) request.getAttribute("DBSnapStatusImage");
    // �м��ϵͳ����״̬ͼƬ
    String middleSnapStatusImage = (String) request.getAttribute("middleSnapStatusImage");
    // ����ϵͳ����״̬ͼƬ
    String serviceSnapStatusImage = (String) request.getAttribute("serviceSnapStatusImage");
    // ��ȫϵͳ����״̬ͼƬ
    String secureSnapStatusImage = (String) request.getAttribute("secureSnapStatusImage");

    // �ؼ�ҵ��
    Hashtable bussinessviewHash = (Hashtable) request.getAttribute("bussinessviewHash");

    // ����ͼ
    ManageXml manageXml = (ManageXml) request.getAttribute("manageXml");

    // ����ͼ����
    String topoTitle = "�����ͼ";
    // ����ͼ����
    String xmlName = "network.jsp";
    String zoom = "1";
    if (manageXml != null) {
        topoTitle = manageXml.getTopoTitle();
        xmlName = manageXml.getXmlName();
        zoom = manageXml.getPercent() + "";
    }

    // �澯״̬
    String alarmState = (String) request.getAttribute("alarmState");
    // ����澯�������
    String[][] alarmTypeSizeByDay = (String[][])request.getAttribute("alarmTypeSizeByDay");
    // ���ܸ澯�������
    String alarmTypeSizeByWeek = (String)request.getAttribute("alarmTypeSizeByWeek");
    // ���ո澯��
    String alarmSizeByDay = (String) request.getAttribute("alarmSizeByDay");
    // ���ܸ澯��
    String alarmSizeByWeek = (String) request.getAttribute("alarmSizeByWeek");
    // �澯�ر���ͼƬ
    String closedAlarmPicFile = (String) request.getAttribute("closedAlarmPicFile");

    Hashtable smallHashtable = (Hashtable) request.getAttribute("smallHashtable");
    Hashtable bigHashtable = (Hashtable) request.getAttribute("bigHashtable");
    int lastOne = 0;//�Ƿ����һ��Ԫ��
    Enumeration RLKey = smallHashtable.elements();
    while (RLKey.hasMoreElements()) {
		String accRole = RLKey.nextElement().toString();
		if (accRole.equals("1")) {
		    lastOne++;
		}
    }

    //Ĭ��ѡ����û���һ������ҵ����Ϊ��ת��treeBid

    //������ҵ����û��·�������Ҹ��û�û����һ������ҵ�񣬲���ת��
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
    //������ҵ����û��·�������Ҹ��û���һ������ҵ���д���·����������ҵ��id��ΪtreeBid,��ת��
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

    //�洢����·(��ʱ����·����������)   �޲��Ի���<Taskδ���>
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
        <!-- EXT�����ص���Դ��ǩҳhome.js  css��ʽ�޸� -->
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
		<!-- ����һ����div -->
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
																	    //��һ����ʾ��ʼ 
																	    if (smallHashtable.get("�豸����").equals(1)) {
																			count++;
																	%>
																	<!-- �豸���� -->
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
																								<b>�豸����</b>
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
																								<a id="routeInfoText" href="javascript:void(0);" onClick="showTree('<%=routepath%>');">·����(<%=routeSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="switchSnapStatusImage" src="<%=rootPath%>/resource/<%=switchSnapStatusImage%>">
																								<br>
																								<a id="switchInfoText" href="javascript:void(0);" onClick="showTree('<%=switchpath%>');">������(<%=switchSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="hostSnapStatusImage" src="<%=rootPath%>/resource/<%=hostSnapStatusImage%>">
																								<br>
																								<a id="hostInfoText" href="javascript:void(0);" onClick="showTree('<%=hostpath%>');">������(<%=hostSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="DBSnapStatusImage" src="<%=rootPath%>/resource/<%=DBSnapStatusImage%>">
																								<br>
																								<a id="DBInfoText" href="javascript:void(0);" onClick="showTree('<%=dbpath%>');">���ݿ�(<%=DBSize%>)</a>
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
																								<a id="middleInfoText" href="javascript:void(0);" onClick="showTree('<%=midpath%>');">�м��(<%=middleSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="serviceSnapStatusImage" src="<%=rootPath%>/resource/<%=serviceSnapStatusImage%>">
																								<br>
																								<a id="serviceInfoText" href="javascript:void(0);" onClick="showTree('<%=servicepath%>');">����(<%=serviceSize%>)</a>
																							</td>
																							<td align="center">
																								<img id="secureSnapStatusImage" src="<%=rootPath%>/resource/<%=secureSnapStatusImage%>">
																								<br>
																								<a id="secureInfoText" href="javascript:void(0);" onClick="showTree('<%=securepath%>');">��ȫ(<%=secureSize%>)</a>
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
																	    //�Ƿ�����пհ�
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
																	    //�Ƿ����п�ʼ 
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
																	    }//�Ƿ��º�����
																	    }
																	%>
																	<%
																	    //�ڶ�����ʾ��ʼ
																	    if (smallHashtable.get("�ؼ�ҵ��").equals(1)) {
																			count++;
																	%>
																	<!-- �ؼ�ҵ�� -->
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
																								<b>�ؼ�ҵ��</b>
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
																							<!-- ���ݿ⡢�����豸���������� -->
																							<%
																							    Iterator iterator = bussinessviewHash.keySet().iterator();
                                                                                                System.out.println(bussinessviewHash.size() + "===bussinessviewHash");
																									int num = 0;
																									int maxnum = 9;//���������ʾ�ĸ��� 
																									int index = 0;//�Ѿ���ʾ�ĸ���
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
																						<!-- ÿ������ҵ����ͼ -->
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
																	    //�Ƿ�����пհ�
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
																	    //�Ƿ����п�ʼ 
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
																	    }//�Ƿ��º�����
																	    }
																	%>
																	<%
																	    if (smallHashtable.get("��������ͼ").equals(1)) {
																			count++;
																	%>
																	<!-- ��������ͼ  -->
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
																								<b>��������ͼ-<%=topoTitle%></b>
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
																	    //�Ƿ�����пհ�
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
																	    //�Ƿ����п�ʼ 
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
																	    }//�Ƿ��º�����
																	    }
																	%>
																	<%
																	    if (smallHashtable.get("�豸����").equals(1)) {
																			count++;
																	%>
																	<!-- �豸���� -->
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
																								<b>����</b>
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
																					<!-- EXT������TAB ���home.js�ļ� -->
																					<!-- ���ص���ԴTabҳ -->
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
																	    //�Ƿ�����пհ�
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
																	    //�Ƿ����п�ʼ 
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
																	    }//�Ƿ��º�����
																	    }
																	    
																	%> 
																</tr>
															</table>
														</td>
													</tr>
													<% 
													    if (bigHashtable.get("�澯��Ϣ").equals(1)) {
													%>
													<tr>
														<td>
															<table>
																<tr>
																	<!-- �澯��Ϣ���� -->
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
																					<b>�澯������Ϣ</b>
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
			*��ת������ҳ��
			*/
			function showTree(rightFramePath){
				//�����ں�andת��һ��  
				//rightFramePath = rightFramePath.replaceAll("&","-and-");
				//rightFramePath = rightFramePath.replaceAll("=","-equals-");
				//ʹ��ѭ���������ں�andת��һ��  
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
                if (bigHashtable.get("�澯��Ϣ").equals(1)) {
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
                                // ��ʾ
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="blue">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 2) { 
                                // ��ͨ
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="yellow">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 3) { 
                                // ����
                                alarmTypeSizeByDay_var += '<td width="60" height="25" align=center bgcolor="orange">';
                                alarmTypeSizeByDay_var += alarmTypeSizeByDay[i][j];
                                alarmTypeSizeByDay_var += '</td>';
                            } else if (i == 0 && j == 4) {
                                // ����
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
		    if (smallHashtable.get("�豸����").equals(1)) {
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
	                    	
	                    	routeInfoText.innerHTML = "·����(" + routeInfo[0] + ")";
	            	        switchInfoText.innerHTML = "������(" + switchInfo[0] + ")";
	            	        hostInfoText.innerHTML = "������(" + hostInfo[0] + ")";
	            	        DBInfoText.innerHTML = "���ݿ�(" + DBInfo[0] + ")";
	            	        middleInfoText.innerHTML = "�м��(" + middleInfo[0] + ")";
	            	        serviceInfoText.innerHTML = "����(" + serviceInfo[0] + ")";
	            	        secureInfoText.innerHTML = "��ȫ(" + secureInfo[0] + ")";
	                    	
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
