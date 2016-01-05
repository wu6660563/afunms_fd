<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.*"%>
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
            
            var controller = false;     // �Ƿ���ʾ������
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
			            window.alert("��û��ѡ����ͼ���޿���������");
			            return;
			        }
			        
			        document.getElementById("showControll").value = "����������";
        			document.getElementById("showControll").title = "������ʾ���ڵ���ͼ������";
    				controller = false;
    			} else {
    				result = self.frames('topo_Frame1').showController1(true);
    				if (result == false) {
			            window.alert("��û��ѡ����ͼ���޿���������");
			            return;
			        }
			        document.getElementById("showControll").value = "�رտ�����";
        			document.getElementById("showControll").title = "�ر���ʾ���ڵ���ͼ������";
			        controller = true;
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
																								<b>��������ͼ</b>
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
