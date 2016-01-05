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
        // ����ǳ����û� ��������е���ԴȨ��
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
    font-family:����;
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
<p><a href="javascript: d.openAll();"></a></p><p><a href="javascript: d.openAll();"></a></p><p><a href="javascript: d.openAll();">չ��</a> | <a href="javascript: d.closeAll();">�ϱ�</a></p>
<script type="text/javascript">
        var currTreeNodeId = '';		// ��ǰ���Ľڵ� Id
        var treeNodeFatherId = '';		// ��ǰ���Ľڵ�ĸ� Id	
        var key = 0 ;
		d = new dTree('d');
		d.add(0,-1,' �豸��Դ��');
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
				treeshowflag = true;			// ������ʾģʽ �Ժ� ���Ըĳ�  0��1�� 2������������ 
			}
					
			Business currBusiness = null;		// ��ǰҵ��
			String currbid = "";				// ��ǰҵ��id
			String currBusinessNodeId = "";		// ��ǰҵ��ڵ��Id
			List treeNodeList = null;			// ���нڵ��б�
			String currTreeNodeId = "0";		// ��ǰ�ڵ�Id
			boolean isShowTreeNodeFlag = true;	// �Ƿ���ʾ�ýڵ� 	
			boolean rightFrameFlag = true;		// �ұ߿����ʾҳ���ģʽ ��ʱֻ��Ϊ true �Ժ��������ģʽ 
            // ���ڻ�ȡ��ǰ�豸״̬
            NodeAlarmService nodeAlarmService = new NodeAlarmService();
            // ���ڻ�ȡҵ����ͼ
            NetworkMonitor networkMonitor = new NetworkMonitor();

			String currTreeNodeFatherId = "";			// ��ǰ�ڵ㸸 Id
			TreeNodeDao treeNodeDao = new TreeNodeDao();
			try{
				treeNodeList = treeNodeDao.loadAll(); // ��ȡ���еĽڵ�
			}catch(Exception e){
				
			} finally {
				treeNodeDao.close();
			}
			
			NodeUtil nodeUtil = new NodeUtil();
			nodeUtil.setSetedMonitorFlag(true);
			nodeUtil.setMonitorFlag("1");
			int treeNodeNum = 0;				// ���ڵ��� �ڼ���
				
			if(treeNodeList == null || treeNodeList.size() == 0){
				return;
			}
			// ����ǰ�ڵ�id ��ֵ�� ���ڵ�
			currTreeNodeFatherId = currTreeNodeId;
			L2: for(Object treeNodeObject : treeNodeList){
				// ѭ��ÿһ���豸���ڵ�
				TreeNode currTreeNode = (TreeNode)treeNodeObject;
		   		List nodeList = nodeUtil.getByNodeTag(currTreeNode.getNodeTag(), currTreeNode.getCategory());
		   		List nodeDTOList = nodeUtil.conversionToNodeDTO(nodeList);

		   		if(nodeDTOList == null){
		   			nodeDTOList = new ArrayList();
		   		}
		   		
		   		List tempNodeDTOList = new ArrayList();		// ��ʱ�洢node
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
			 		// ����豸��Ϊ 0 �� �� ��ʾģʽ�ĸ�ֵ��
			 		isShowTreeNodeFlag = treeshowflag;
				}
				
				// ����ǰ�ڵ㸳ֵ Ϊ �ø��ڵ� + "_" + �ýڵ�id;
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
                            //��ҳ������ͼ����豸ʱ��ת��ҳ������
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
			    	    // �����κ���
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
				   		} // ��� ÿһ���豸 ѭ�� (L3)
				   	}
				}
			}	// ���ÿһ���豸���ڵ�ѭ�� (L2)
				
		%>
		document.write(d);
</script>
</div>
</body>
</html>