<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="java.util.List"%>      
<%@page import="com.afunms.topology.dao.TreeNodeDao"%>
<%@page import="com.afunms.topology.model.TreeNode"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.manage.UserManager"%>
<%@page import="com.afunms.application.dao.OraclePartsDao"%>
<%@page import="com.afunms.application.model.OracleEntity"%>
<%@page import="com.afunms.polling.node.DBNode"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.system.dao.SystemConfigDao"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@page import="com.afunms.flex.networkTopology.NetworkMonitor"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.Constant"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
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
    String treeFlag = request.getParameter("treeflag");
    if(treeFlag==null) treeFlag = "0";
    String typeStr = (String)request.getAttribute("typeStr");
    
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyNodeOperationPermission());
    boolean isCreateOperation = roleOperationPermissionService.isCreateOperationPermission();
    String createOperationDisable = "inline";
    if (!isCreateOperation) {
        createOperationDisable = "none";
    }
%>
<html>
<head>
<title></title>
<link rel="StyleSheet" href="../dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="../dtree/dtree.js"></script>
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
<div class="dtree">
<p><a href="javascript: d.openAll();">չ��</a> | <a href="javascript: d.closeAll();">�ϱ�</a></p>
<script type="text/javascript">
        var key = 0;
        var ffid = 0;
        var fid = 0;
        var fids = 0;
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
                treeshowflag = true;            // ������ʾģʽ �Ժ� ���Ըĳ�  0��1�� 2������������ 
            }
                    
            Business currBusiness = null;       // ��ǰҵ��
            String currbid = "";                // ��ǰҵ��id
            String currBusinessNodeId = "";     // ��ǰҵ��ڵ��Id
            List treeNodeList = null;           // ���нڵ��б�
            String currTreeNodeId = "0";        // ��ǰ�ڵ�Id
            boolean isShowTreeNodeFlag = true;  // �Ƿ���ʾ�ýڵ�  
            boolean rightFrameFlag = true;      // �ұ߿����ʾҳ���ģʽ ��ʱֻ��Ϊ true �Ժ��������ģʽ 
            // ���ڻ�ȡ��ǰ�豸״̬
            NodeAlarmService nodeAlarmService = new NodeAlarmService();
            // ���ڻ�ȡҵ����ͼ
            NetworkMonitor networkMonitor = new NetworkMonitor();

            String currTreeNodeFatherId = "";           // ��ǰ�ڵ㸸 Id
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
            int treeNodeNum = 0;                // ���ڵ��� �ڼ���
                
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
                
                List tempNodeDTOList = new ArrayList();     // ��ʱ�洢node
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
                    var imagestr = "<%=rootPath%>/performance/<%=NodeHelper.getTypeImage(currTreeNode.getName())%>";
                    d.add(currTreeNodeId,currTreeNodeFatherId,'<%=" " + currTreeNode.getText() + "("+ currTreeNode.getDeceiveNum() + ")"%>',"","","","",imagestr,imagestr);
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
                                }
                                alermlevel = nodeAlarmService.getMaxAlarmLevel(manageXMLNodeList);
                            } else {
                                nodeAlarmService.getMaxAlarmLevel(nodeDTO);
                            }
                            
                            
                            String imagestr = rootPath + "/resource/" + NodeHelper.getCurrentStatusImage(alermlevel);
                            if(Constant.TYPE_HOST.equals(nodeDTO.getType()) || Constant.TYPE_NET.equals(nodeDTO.getType()) || Constant.TYPE_DB.equals(nodeDTO.getType())){
                                imagestr = rootPath + "/performance/" + NodeHelper.getSubTypeImage(nodeDTO.getSubtype());
                            }
                            
                            %>
                            currTreeNodeId = '<%=currTreeNodeId%>';
                            currTreeNodeFatherId = '<%=currTreeNodeFatherId%>';
                            var imagestr = "<%=imagestr%>";
                            
                            d.add(currTreeNodeId,currTreeNodeFatherId,'<%=treeFlag.equals("0")?nodeDTO.getName().trim():nodeDTO.getIpaddress()%>',"",'<%=nodeDTO.getIpaddress()%>',"<%=currTreeNode.getNodeTag()+nodeDTO.getId()+";"+currTreeNode.getName()%>","rightFrame","<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(alermlevel)%>");
                            <%
                        } // ��� ÿһ���豸 ѭ�� (L3)
                    }
                }
            }   // ���ÿһ���豸���ڵ�ѭ�� (L2)
        %>
        document.write(d);
        

//------------search one device-------------����ѡ�е����ڵ��ڵ�ͼ��������Ӧ���豸

function SearchNode(ip)
{
    var coor = window.parent.mainFrame.mainFrame.getNodeCoor(ip);
    if (coor == null)
    {
        var msg = "û����ͼ��������IP��ַΪ "+ ip +" ���豸��";
        window.alert(msg);
        return;
    }
    else if (typeof coor == "string")
    {
        window.alert(coor);
        return;
    }
    window.parent.mainFrame.mainFrame.moveMainLayer(coor);
}
//--------------------end--------------------
//--------------------beginѡ���豸��ʾ�Ҽ��˵�--------------------
var nodeid="";
var nodeip="";
var nodecategory="";
function showMenu(id,ip){
    nodeid = id.split(";")[0];
    nodecategory = id.split(";")[1];
    nodeip = ip;
    /**/
    if(document.getElementById("div_RightMenu") == null)
    {    
        CreateMenu();
        document.oncontextmenu = ShowMenu
        document.body.onclick  = HideMenu    
    }
    else
    {
        document.oncontextmenu = ShowMenu
        document.body.onclick  = HideMenu    
    } 

}
function add(){
    var nodeId = nodeid;//Ҫ��֤nodeid�ĳ��ȴ���3
    var coor = window.parent.mainFrame.mainFrame.getNodeId(nodeId);
    if (coor == null)
    {
         window.parent.mainFrame.mainFrame.addEquip(nodeId,nodecategory);
    }
    else if (typeof coor == "string")
    {
        window.alert(coor);
        return;
    }
    window.parent.mainFrame.mainFrame.moveMainLayer(coor);
    window.alert("���豸�Ѿ�������ͼ�д��ڣ�");
}
function detail(){
    showalert(nodeid);
    window.parent.parent.opener.focus();
}
function showalert(id) {
    //window.parent.parent.opener.location="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
    window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
}
function evtMenuOnmouseMove()
{
    this.style.backgroundColor='#8AAD77';
    this.style.paddingLeft='10px';    
}
function evtOnMouseOut()
{
    this.style.backgroundColor='#FAFFF8';
}
function CreateMenu()
{    
        var div_Menu          = document.createElement("Div");
        div_Menu.id           = "div_RightMenu";
        div_Menu.className    = "div_RightMenu";
        
        var div_Menu1         = document.createElement("Div");
        div_Menu1.id          = "div_Menu1";
        div_Menu1.className   = "divMenuItem";
        div_Menu1.onclick     = add;
        div_Menu1.onmousemove = evtMenuOnmouseMove;
        div_Menu1.onmouseout  = evtOnMouseOut;
        div_Menu1.innerHTML   = "��ӵ�����ͼ";
        var div_Menu2         = document.createElement("Div");
        div_Menu2.id          = "div_Menu2";
        div_Menu2.className   = "divMenuItem";
        div_Menu2.onclick     = detail;
        div_Menu2.onmousemove = evtMenuOnmouseMove;
        div_Menu2.onmouseout  = evtOnMouseOut;
        div_Menu2.innerHTML   = "��ϸ��Ϣ";
        
        div_Menu1.style.display = "<%=createOperationDisable%>";
        
        div_Menu.appendChild(div_Menu1);
        div_Menu.appendChild(div_Menu2);
        document.body.appendChild(div_Menu);
}
// �жϿͻ��������
function IsIE() 
{
    if (navigator.appName=="Microsoft Internet Explorer") 
    {
        return true;
    } 
    else 
    {
        return false;
    }
}

function ShowMenu()
{
    
    if (IsIE())
    {
        document.body.onclick  = HideMenu;
        var redge=document.body.clientWidth-event.clientX;
        var bedge=document.body.clientHeight-event.clientY;
        var menu = document.getElementById("div_RightMenu");
        if (redge<menu.offsetWidth)
        {
            menu.style.left=document.body.scrollLeft + event.clientX-menu.offsetWidth
        }
        else
        {
            menu.style.left=document.body.scrollLeft + event.clientX
            //�����иĶ�
            menu.style.display = "block";
        }
        if (bedge<menu.offsetHeight)
        {
            menu.style.top=document.body.scrollTop + event.clientY - menu.offsetHeight
        }
        else
        {
            menu.style.top = document.body.scrollTop + event.clientY
            menu.style.display = "block";
        }
    }
    return false;
}
function HideMenu()
{
    if (IsIE())  document.getElementById("div_RightMenu").style.display="none";    
}
</script>
</div>
</body>
</html>