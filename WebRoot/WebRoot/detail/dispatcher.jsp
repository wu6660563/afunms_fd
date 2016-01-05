<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.polling.*"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.application.dao.OraclePartsDao"%>
<%@page import="com.afunms.application.model.OracleEntity"%>
<%@page import="com.afunms.topology.util.TopoHelper"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.indicators.util.Constant"%>
<%@page import="com.afunms.polling.loader.HostLoader"%>
<%
	String nodeId = "";
	String temp = null;
	String belong = request.getParameter("belong");
	String fromtopo = request.getParameter("fromtopo");
    if(fromtopo == null) fromtopo = "false";
	//String flag = request.getParameter("flag");
	String flag = "1";
	if (flag == null) {
		flag = "0";
	}
	temp = request.getParameter("id");
    System.out.println(temp);
	if (temp != null)
		nodeId = temp;
	NodeDTO node = null;

	String nodeTag = nodeId.substring(0, 3);
	String node_id = nodeId.substring(3);
	
	String rootPath = request.getContextPath();

	TreeNodeDao treeNodeDao = new TreeNodeDao();
	TreeNode vo = (TreeNode) treeNodeDao.findByNodeTag(nodeTag);
	String rightFramePath = "";
	if("bus".equals(nodeTag)){
		String treeBid = request.getParameter("treeBid");
		rightFramePath = "/businessview.do?action=showViewNode&bid=" + treeBid + "&viewId="
							+ nodeId.substring(3) + "&flag=" + flag +"&belong="+belong ;
	} else if ("soc".equals(nodeTag)){
		rightFramePath = "/pstype.do?action=detail&id=" + nodeId.substring(3)
						+ "&flag=" + flag;
	}
	NodeUtil nodeUtil = new NodeUtil();
	if (vo != null && vo.getName() != null && !"".equals(vo.getName())) {
        List<NodeDTO> list = nodeUtil.conversionToNodeDTO(nodeUtil.getByNodeTag(vo.getNodeTag(), null));
        for(NodeDTO nodeDTO : list) {
            if (String.valueOf(nodeDTO.getId()).equals(node_id)) {
                node = nodeDTO;
                break;
            }
        }
	}
	if (node == null) {
		if (nodeTag.equals("app")) {
			rightFramePath = "/appsystem.do?action=list_app&id=" + node_id;

		} else if (nodeTag.equals("sit")) {
			rightFramePath = "/website.do?action=list_web&id=" + node_id;
		} else if (nodeTag.equals("oas")) {
			rightFramePath = "/oasys.do?action=list&jp=1";
		} else if (nodeTag.equals("was")) {		
			rightFramePath = "was.do?action=detail&id="
					+ nodeId.substring(3) + "&flag=" + flag;
		} else if("bus".equals(nodeTag)){
            String treeBid = request.getParameter("treeBid");
            rightFramePath = "/businessview.do?action=showViewNode&bid=" + treeBid + "&viewId="
                    + nodeId.substring(3) + "&flag=" + flag +"&belong="+belong ;
        } else {
			%>
			<script type="text/javascript">
			alert("没有该节点的信息！");
			history.go(-1);
			</script>
			<%
		}
	} else {
		String paraString = ".jsp?id=" + node.getId() + "&flag="
				+ flag +"&belong="+belong;
		String paraStringTemp = "&nodeid=" + node.getId() + "&type=" + node.getType()
                + "&subtype=" + node.getSubtype() + "&flag=" + flag;
        HostLoader hostLoader = new HostLoader();
		if (Constant.TYPE_HOST_SUBTYPE_AS400.equals(node.getSubtype())) {
            rightFramePath = "/monitor.do?action=hostping&id="
                            + node.getId() + "&flag=" + flag +"&belong="+belong;
		} else if (Constant.TYPE_HOST.equals(node.getType())) {
            Host host = hostLoader.loadOneByID(String.valueOf(node.getId()));
                if (Constant.TYPE_HOST_SUBTYPE_WINDOWS.equals(node.getSubtype())) {
                    rightFramePath = "/windowsData.do?action=getPerformanceInfo"
                                    + paraStringTemp;
                } else if (Constant.TYPE_HOST_SUBTYPE_AIX.equals(node.getSubtype())) {
                    rightFramePath = "/aixData.do?action=getPerformanceInfo"
                                    + paraStringTemp;
                } else if (Constant.TYPE_HOST_SUBTYPE_LINUX.equals(node.getSubtype())) {
                    rightFramePath = "/linuxData.do?action=getPerformanceInfo"
                                    + paraStringTemp;
                } else if (Constant.TYPE_HOST_SUBTYPE_HPUNIX.equals(node.getSubtype())) {
                    rightFramePath = "/hpunixData.do?action=getPerformanceInfo"
                        + paraStringTemp;
                } else if (Constant.TYPE_HOST_SUBTYPE_TRU64.equals(node.getSubtype())) {
                    rightFramePath = "/tru64Data.do?action=getPerformanceInfo"
                        + paraStringTemp;
                } else if (Constant.TYPE_HOST_SUBTYPE_SOLARIS.equals(node.getSubtype())) {
                    rightFramePath = "/solarisData.do?action=getPerformanceInfo"
                        + paraStringTemp;
                } else if (host.getCollecttype() == 3
                                || host.getCollecttype() == 4
                                || host.getCollecttype() == 8
                                || host.getCollecttype() == 9) {
                            rightFramePath = "/monitor.do?action=hostping&id="
                                            + node.getId() + "&flag=" + flag +"&belong="+belong;
                } else {
                    rightFramePath = "/windowsData.do?action=getPerformanceInfo"
                            + paraStringTemp;
                }
        } else if (Constant.TYPE_NET.equals(node.getType())) {
            //rightFramePath = "/topology/network/networkview" + paraString;
            rightFramePath = "/netData.do?action=getInterfaceInfo" + paraStringTemp;
        }  else if (Constant.TYPE_FIREWALL_SUBTYPE_TOPSEC.equals(node.getSubtype())) {
            //rightFramePath = "/topology/network/networkview" + paraString;
            rightFramePath = "/topsecData.do?action=getInterfaceInfo" + paraStringTemp;
        } else if(Constant.TYPE_FIREWALL_SUBTYPE_VENUS.equals(node.getSubtype())){
        	 rightFramePath = "/venusData.do?action=getInterfaceInfo" + paraStringTemp;
        } else if(Constant.TYPE_FIREWALL_SUBTYPE_FWH3C.equals(node.getSubtype())){
        	 rightFramePath = "/h3cfirewallData.do?action=getInterfaceInfo" + paraStringTemp;
        } else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_TOMCAT.equals(
            node.getSubtype())){
			rightFramePath = "/tomcatData.do?action=getPerformanceInfo&id="
					+ paraStringTemp;
		} else if (Constant.TYPE_APPLICATION_SUBTYPE_PTIMEDB.equals(
            node.getSubtype())){
			rightFramePath = "/ptimedbData.do?action=getPerformanceInfo&id="
					+ paraStringTemp;
		} else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_WEBLOGIC.equals(
            node.getSubtype())){
            rightFramePath = "/weblogic.do?action=detail&id="
                    + node.getId() + "&flag=" + flag;
        }else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_WAS.equals(
            node.getSubtype())){
            rightFramePath = "/was.do?action=detail&id="
                    + node.getId() + "&flag=" + flag;
        }else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_IIS.equals(
            node.getSubtype())){
            rightFramePath = "/iis.do?action=detail&id="
                    + node.getId() + "&flag=" + flag;
        }else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_MQ.equals(
            node.getSubtype())){
            rightFramePath = "/mq.do?action=detail&id="
                    + nodeId.substring(3) + "&flag=" + flag;
        }else if (Constant.TYPE_SERVICE_SUBTYPE_WEB.equals(
            node.getSubtype())){
			rightFramePath = "/web.do?action=detail&id="
                    + node.getId() + "&flag=" + flag;
		}else if (Constant.TYPE_SERVICE_SUBTYPE_URL.equals(
            node.getSubtype())){
            System.out.println(node.getId());
            rightFramePath = "/urlData.do?action=getPerformanceInfo&id="
                    + paraStringTemp;
        }else if (Constant.TYPE_SERVICE_SUBTYPE_FTP.equals(
            node.getSubtype())){
			rightFramePath = "/FTP.do?action=detail&id="
                    + node.getId() + "&flag=" + flag;
		}else if (Constant.TYPE_SERVICE_SUBTYPE_EMAIL.equals(
            node.getSubtype())){
			rightFramePath = "/mail.do?action=detail&id="
					+ node.getId() + "&flag=" + flag;
		}else if (Constant.TYPE_MIDDLEWARE_SUBTYPE_DOMINO.equals(
            node.getSubtype())){
			rightFramePath = "/domino.do?action=domcpu&id="
					+ nodeId.substring(3) + "&flag=" + flag;
		}else if (Constant.TYPE_DB_SUBTYPE_ORACLE.equals(node.getSubtype())) {
			OraclePartsDao oraclePartsDao = new OraclePartsDao();
			OracleEntity oracleEntity = (OracleEntity) oraclePartsDao
					.getOracleById(Integer
							.parseInt(nodeId.substring(3)));
			oraclePartsDao.close();
			String sids = "";
			sids = oracleEntity.getDbid() + "";
			rightFramePath = "/db.do?action=check&id="
					+ sids + "&flag=" + flag + "&sid="
					+ nodeId.substring(3);
		} else if (Constant.TYPE_DB.equals(node.getType())){
			rightFramePath = "/db.do?action=check&id="
					+ nodeId.substring(3) + "&flag=" + flag;
        }
		//将等于和and转换一下
		String path = "";
		
		if(fromtopo.equals("true")){ 
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
			path = rootPath + "/performance/index.jsp?flag=1&rightFramePath="+rightFramePath;
		} else {
			path = rootPath +rightFramePath;
		}
		
		response.sendRedirect(path);
	}
%>