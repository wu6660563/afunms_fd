<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.dao.TreeNodeDao"%>
<%@page import="com.afunms.topology.model.TreeNode"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>   
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.Constant"%>
<%@page import="com.afunms.system.dao.SystemConfigDao"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@page import="com.afunms.flex.networkTopology.NetworkMonitor"%>
<%
	String rootPath = request.getContextPath();
	StringBuffer url = request.getRequestURL();
	String urlPath = url.substring(0, url.lastIndexOf("afunms"));
	User current_user = (User) session
			.getAttribute(SessionConstant.CURRENT_USER);
	List bussinessList = null;
	BusinessDao dao = new BusinessDao();
	try {
		bussinessList = dao.loadAll();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		dao.close();
	}
    if (bussinessList == null) {
        bussinessList = new ArrayList();
    }
	List userBussinessList = new ArrayList();
	for (int i = 0; i < bussinessList.size(); i++) {
		Business business = (Business) bussinessList.get(i);
		if (current_user.getBusinessids() != null 
			&& current_user.getBusinessids().contains("," + business.getId() + ",")) {
			userBussinessList.add(business);
		}
	}
	if(current_user.getRole() == 0) {
        // 如果是超级用户 则具有所有的资源权限
		userBussinessList = bussinessList;
	}

%>
<html>
<head>
<title></title>
<link rel="StyleSheet" href="../performance/dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="../performance/dtree/dtree.js"></script>
<style>
body {
margin-left: 6px;
margin-top: 0px;
margin-right: 6px;
margin-bottom: 6px;
scrollbar-face-color: #E0E3EB;
scrollbar-highlight-color: #E0E3EB;
scrollbar-shadow-color: #E0E3EB;
scrollbar-3dlight-color: #E0E3EB;
scrollbar-arrow-color: #7ED053;
scrollbar-track-color: #ffffff;
scrollbar-darkshadow-color: #9D9DA1;
}
body,td,th {color: #666666;line-height:20px}
.div_RightMenu
{
    z-index:30000;
    text-align:left;    
    cursor: default;
    position: absolute;
    background-color:#FAFFF8;
    width:100px;
    height:auto;
    border: 1px solid #333333;
    display:none;
    filter:progid:DXImageTransform.Microsoft.Shadow(Color=#333333,Direction=120,strength=5);    
}
.divMenuItem
{
    height:17px;
    color:Black;
    font-family:宋体;
    vertical-align:middle;
    font-size:10pt;
    margin-bottom:3px;
    cursor:hand;
    padding-left:10px;
    padding-top:2px;
}
</style>
</head>
<body>
<div class="dtree" style="">
<p><a href="javascript: d.openAll();"></a></p><p><a href="javascript: d.openAll();"></a></p><p><a href="javascript: d.openAll();">展开</a> | <a href="javascript: d.closeAll();">合闭</a></p>
<script type="text/javascript">
        var currTreeNodeId = '';		// 当前树的节点 Id
        var treeNodeFatherId = '';		// 当前树的节点的父 Id	
        var key = 0 ;
		d = new dTree('d');
		d.add(0,-1,' 设备资源树');
		<%
			//String treeshowflag = "0";
			String treeshowflag_str = "0";
			SystemConfigDao systemConfigDao = new SystemConfigDao();
			try{
				treeshowflag_str = systemConfigDao.getSystemCollectByVariablename("treeshowflag");
			} catch(Exception e){
				
			} finally {
				systemConfigDao.close();
			}
			boolean treeshowflag = false;
			if("1".equals(treeshowflag_str)){
				treeshowflag = true;			// 树的显示模式 以后 可以改成  0，1， 2，。。。。。 
			}
					
			Business currBusiness = null;		// 当前业务
			String currbid = "";				// 当前业务id
			String currBusinessNodeId = "";		// 当前业务节点的Id
			List treeNodeList = null;			// 所有节点列表
			String currTreeNodeId = "0";		// 当前节点Id
			boolean isShowTreeNodeFlag = true;	// 是否显示该节点 	
			boolean rightFrameFlag = true;		// 右边框架显示页面的模式 暂时只能为 true 以后添加其他模式 
            // 用于获取当前设备状态
            NodeAlarmService nodeAlarmService = new NodeAlarmService();
            // 用于获取业务视图
            NetworkMonitor networkMonitor = new NetworkMonitor();

			String currTreeNodeFatherId = "";			// 当前节点父 Id
			TreeNodeDao treeNodeDao = new TreeNodeDao();
			try{
				treeNodeList = treeNodeDao.loadAll(); // 获取所有的节点
			}catch(Exception e){
				
			} finally {
				treeNodeDao.close();
			}
			
			NodeUtil nodeUtil = new NodeUtil();
			nodeUtil.setSetedMonitorFlag(true);
			nodeUtil.setMonitorFlag("1");
			int treeNodeNum = 0;				// 树节点中 第几个
				
			if(treeNodeList == null || treeNodeList.size() == 0){
				return;
			}
			// 将当前节点id 赋值给 父节点
			currTreeNodeFatherId = currTreeNodeId;
			L2: for(Object treeNodeObject : treeNodeList){
				// 循环每一个设备树节点
				TreeNode currTreeNode = (TreeNode)treeNodeObject;
		   		List nodeList = nodeUtil.getByNodeTag(currTreeNode.getNodeTag(), currTreeNode.getCategory());
		   		List nodeDTOList = nodeUtil.conversionToNodeDTO(nodeList);

		   		if(nodeDTOList == null){
		   			nodeDTOList = new ArrayList();
		   		}
		   		
		   		List tempNodeDTOList = new ArrayList();		// 临时存储node
		   		for (Object nodeDTOObject : nodeDTOList) {
		   			NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
                    for (Object userBussinessObject : userBussinessList) {
                        Business userBussiness = (Business) userBussinessObject;
    		   			if(nodeDTO.getBusinessId().contains("," +  userBussiness.getId() + ",")) {
    		   				tempNodeDTOList.add(nodeDTO);
                            break;
    		   			}
                    }
		   		}
		   		nodeDTOList = tempNodeDTOList;
		   		currTreeNode.setDeceiveNum(nodeDTOList.size()+"");
			 	
			 	isShowTreeNodeFlag = true;
			 	if("0".equals(currTreeNode.getDeceiveNum())){
			 		// 如果设备数为 0 则 将 显示模式的赋值给
			 		isShowTreeNodeFlag = treeshowflag;
				}
				
				// 给当前节点赋值 为 该父节点 + "_" + 该节点id;
				currTreeNodeId = "0" + "_" + currTreeNode.getId();
				currTreeNodeFatherId = "0" + "_" + currTreeNode.getFatherId();
				if (0 == currTreeNode.getFatherId()) {
					currTreeNodeFatherId = "0";
				}
				if (isShowTreeNodeFlag) {
					%>
				 	currTreeNodeId = '<%=currTreeNodeId%>';
      				currTreeNodeFatherId = '<%=currTreeNodeFatherId%>';
				    var imagestr = "<%=rootPath%>/performance/<%=NodeHelper.getTypeImage(currTreeNode
									.getName())%>";
				    d.add(currTreeNodeId,currTreeNodeFatherId,'<%=" " + currTreeNode.getText() + "("
							+ currTreeNode.getDeceiveNum() + ")"%>',"<%=rootPath + currTreeNode.getUrl()%>"
                            ,"","","rightFrame",imagestr,imagestr);
                    if(0 == "<%=treeNodeNum%>" && "<%=rightFrameFlag%>"){
                        <%
                            //首页和拓扑图点击设备时跳转的页面链接
                            String rightFramePath = rootPath + request.getParameter("rightFramePath");
                            rightFramePath = rightFramePath.replaceAll("-equals-","=");
                            rightFramePath = rightFramePath.replaceAll("-and-","&");
                            if(request.getParameter("rightFramePath") == null || request.getParameter("rightFramePath").equals("null")){
                                rightFramePath = rootPath + currTreeNode.getUrl();
                            }
                        %>
                        parent.document.getElementById("rightFrame").src="<%=rightFramePath%>";
                    }
					<%
					treeNodeNum++;
					currTreeNodeFatherId = currTreeNodeId;
					if("1".equals(currTreeNode.getIsHaveChild())) {
			    	    // 不干任何事
			    	} else {
						L3:for(Object nodeDTOObject : nodeDTOList){
				   			NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
				   			int alermlevel = 0;
				   			
				   			currTreeNodeId = currTreeNode.getNodeTag() + "_" + nodeDTO.getId();
                            
                            if (Constant.TYPE_TOPO_SUBTYPE_BUSINESSVIEW.equals(nodeDTO.getSubtype())) {
                                List<NodeDTO> manageXMLNodeList = networkMonitor.getBussinessviewNode(nodeDTO);
                                for(NodeDTO manageXMLNode : manageXMLNodeList) {
                                    System.out.println(manageXMLNode.getIpaddress() + "===" + manageXMLNode.getType()+ "==="  + manageXMLNode.getSubtype()+ "==="  + nodeAlarmService.getMaxAlarmLevel(manageXMLNode));
                                }
                                alermlevel = nodeAlarmService.getMaxAlarmLevel(manageXMLNodeList);
                                System.out.println(alermlevel + ": alermlevel");
                            } else {
                                alermlevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
                            }
                            
				   			String imagestr = rootPath + "/resource/" + NodeHelper.getCurrentStatusImage(alermlevel);
				   			if(Constant.TYPE_HOST.equals(nodeDTO.getType()) || Constant.TYPE_NET.equals(nodeDTO.getType()) || Constant.TYPE_DB.equals(nodeDTO.getType())){
				   				imagestr = rootPath + "/performance/" + NodeHelper.getSubTypeImage(nodeDTO.getSubtype());
				   			}
				   			
				   			%>
						 	currTreeNodeId = '<%=currTreeNodeId%>';
		      				currTreeNodeFatherId = '<%=currTreeNodeFatherId%>';
						    var imagestr = "<%=imagestr%>";
						    d.add(currTreeNodeId,currTreeNodeFatherId,'<%=" " + nodeDTO.getName()%>',"<%=rootPath
									+ "/detail/dispatcher.jsp?flag=1&id="
									+ currTreeNode.getNodeTag()
									+ nodeDTO.getId()%>","","","rightFrame",imagestr,imagestr);
							<%
				   		} // 完成 每一个设备 循环 (L3)
				   	}
				}
			}	// 完成每一个设备树节点循环 (L2)
				
		%>
		document.write(d);
</script>
</div>
</body>
</html>