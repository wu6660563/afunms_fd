<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>   
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<html xmlns="http://www.w3.org/1999/xhtml">   
<%   
   String rootPath = request.getContextPath();
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   //System.out.println(current_user.getBusinessids());
   String bids[] = current_user.getBusinessids().split(",");
   String viewFile = (String)session.getAttribute(SessionConstant.CURRENT_TOPO_VIEW);   
   User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService  roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyOperationPermission());
    boolean isCreateOperation = roleOperationPermissionService.isCreateOperationPermission();
    boolean isUpdateOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteOperation = roleOperationPermissionService.isDeleteOperationPermission();
    String createOperationDisable = "inline";
    if (!isCreateOperation) {
        createOperationDisable = "none";
    }
    String updateOperationDisable = "inline";
    if (!isUpdateOperation) {
        updateOperationDisable = "none";
    }
    String deleteOperationDisable = "inline";
    if (!isDeleteOperation) {
        deleteOperationDisable = "none";
    }
    roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyHinNodeOperationPermission());
    boolean isCreateTopologyHinNodeOperation = roleOperationPermissionService.isCreateOperationPermission();
    String createTopologyHinNodeOperationDisable = "inline";
    if (!isCreateTopologyHinNodeOperation) {
        createTopologyHinNodeOperationDisable = "none";
    }
     roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyLineOperationPermission());
    boolean isCreateTopologyLineOperation = roleOperationPermissionService.isCreateOperationPermission();
    String createTopologyLineOperationDisable = "inline";
    if (!isCreateTopologyLineOperation) {
        createTopologyLineOperationDisable = "none";
    }
    roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyHinLineOperationPermission());
    boolean isCreateTopologyHinLineOperation = roleOperationPermissionService.isCreateOperationPermission();
    String createTopologyHinLineOperationDisable = "inline";
    if (!isCreateTopologyHinLineOperation) {
        createTopologyHinLineOperationDisable = "none";
    }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css" />
<title>topFrame</title>
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<%
    out.println("<script type=\"text/javascript\">");
    
    // �ж�ȫ����ʾ״̬
    String fullscreen = request.getParameter("fullscreen"); 
    
    if (fullscreen.equals("0")) 
       fullscreen = "0";
    else 
    {
        fullscreen = "1";
        out.println("viewWidth = 0;");
    }
    out.println("</script>");
%>
<script type="text/javascript">
    window.parent.frames['mainFrame'].location.reload();//������·��ˢ������ͼ
    var curTarget = "showMap.jsp?filename=<%=viewFile%>&fullscreen=<%=fullscreen%>";
    var display = false;        // �Ƿ���ʾ����б�
    var controller = false;     // �Ƿ���ʾ������
    //ת��Ŀ��(jsp)�ļ�
    function updateState(target) {
        curTarget = target;
    }

function searchNode()
{   
    var ip = window.prompt("��������Ҫ�������豸IP��ַ", "�ڴ������豸IP��ַ");
    if (ip == null)
        return true;
    else if (ip == "�ڴ������豸IP��ַ")
        return;

    if (!checkIPAddress(ip))
        searchNode();

    var coor = window.parent.mainFrame.getNodeCoor(ip);
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

    // �ƶ��豸�����ı�Ǵ�
    window.parent.mainFrame.moveMainLayer(coor);
}

function searchNodeByIp(ip)
{   
    if (!checkIPAddress(ip)){
        return;
    }
    var coor = window.parent.mainFrame.getNodeCoor(ip);
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

    // �ƶ��豸�����ı�Ǵ�
    window.parent.mainFrame.moveMainLayer(coor);
}

// ��������ͼ
function saveFile() {
    parent.mainFrame.saveFile();
}

// ˢ������ͼ
function refreshFile() 
{
    <%
        if (isUpdateOperation) {
    
    %>
    if (window.confirm("��ˢ�¡�ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
        parent.topFrame.saveFile();
    }
    
    <%
    }
    %>
    parent.mainFrame.location.reload();
}

// ȫ���ۿ�
function gotoFullScreen() {
    parent.mainFrame.resetProcDlg();
    var status = "toolbar=no,height="+ window.screen.height + ",";
    status += "width=" + (window.screen.width-8) + ",scrollbars=no";
    status += "screenX=0,screenY=0";
    window.open("index.jsp?fullscreen=yes", "fullScreenWindow", status);
    parent.mainFrame.zoomProcDlg("out");
}

//����ʵ����·
function createEntityLink(){
    var objLinkAry = window.parent.frames['mainFrame'].topologyMapObject.getAllSelectedNodes();
    var xml = "<%=viewFile%>";
    /*
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objLinkAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objLinkAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    */
    if(objLinkAry==null||objLinkAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
    
    if(objLinkAry[0].imageElement.name.substring(objLinkAry[0].imageElement.name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var start_id = objLinkAry[0].imageElement.id.replace("node_","");
    
    if(objLinkAry[1].imageElement.name.substring(objLinkAry[1].imageElement.name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var end_id = objLinkAry[1].imageElement.id.replace("node_","");
    
    if(start_id.indexOf("net")==-1||end_id.indexOf("net")==-1){
        alert("��ѡ�������豸!");
        return;
    }
    var url="<%=rootPath%>/link.do?action=addLink&start_id="+start_id+"&end_id="+end_id+"&xml="+xml;
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:360px; status:no; help:no;resizable:0');
}
//������ͼ
function createSubMap(){
    var objEntityAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objEntityAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objEntityAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    var lineArr = window.parent.frames['mainFrame'].lineMoveAry; 
    var asslineArr = window.parent.frames['mainFrame'].assLineMoveAry; 
    var objEntityStr = "";//�ڵ���Ϣ
    var linkStr = "";//��·��Ϣ
    var asslinkStr = "";//��·��Ϣ
    if(objEntityAry!=null&&objEntityAry.length>0){
        for(var i=0;i<objEntityAry.length;i++){
            objEntityStr += objEntityAry[i].id.replace("node_","") +",";
        }
    }
    if(lineArr!=null&&lineArr.length>0){
        for(var i=0;i<lineArr.length;i++){
            linkStr += lineArr[i].id.replace("line_","") + "," + lineArr[i].lineid + ";";
        }
    }
    if(asslineArr!=null&&asslineArr.length>0){
        for(var i=0;i<asslineArr.length;i++){
            asslinkStr += asslineArr[i].id.split("#")[0].replace("line_","") + "," + asslineArr[i].lineid + ";";
        }
    }
    var url="<%=rootPath%>/submap.do?action=createSubMap&objEntityStr="+objEntityStr+"&linkStr="+linkStr+"&asslinkStr="+asslinkStr;
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
//����ʾ����·
function createDemoLink(){
    var objEntityAry = window.parent.frames['mainFrame'].topologyMapObject.getAllSelectedNodes();
    /*
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objEntityAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objEntityAry = window.parent.frames['mainFrame'].objEntityAry;
    } 
    */
    if(objEntityAry==null||objEntityAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
     
    var start_id = objEntityAry[0].imageElement.id.replace("node_","");
    var end_id = objEntityAry[1].imageElement.id.replace("node_","");
    var xml = "<%=viewFile%>";
    var lineArr = window.parent.frames['mainFrame'].topologyMapObject.topologyMapDemoLines;
    var demoLineNum = 0;
    if (lineArr) {
        demoLineNum = lineArr.length;
    }
    var k = 0;
    while (k < demoLineNum) {
        var topologyMapDemoLine = lineArr[k];
        if ((topologyMapDemoLine.a == start_id && topologyMapDemoLine.b == end_id)
            ||(topologyMapDemoLine.a == end_id && topologyMapDemoLine.b == start_id)) {
            alert("ѡ�е���̨�豸�Ѿ�����ʾ����·!");
            return;
        }
        k = k + 1;
    }
    var start_x_y=objEntityAry[0].imageElement.style.left+","+objEntityAry[0].imageElement.style.top;
    var end_x_y=objEntityAry[1].imageElement.style.left+","+objEntityAry[1].imageElement.style.top;
    var url="<%=rootPath%>/link.do?action=readyAddLine&xml="+xml+"&start_id="+start_id+"&end_id="+end_id+"&start_x_y="+start_x_y+"&end_x_y="+end_x_y;
    showModalDialog(url,window,'dialogwidth:510px; dialogheight:350px; status:no; help:no;resizable:0');
    //parent.mainFrame.location = "<%=rootPath%>/link.do?action=addDemoLink&xml="+xml+"&id1="+start_id+"&id2="+end_id;
    //alert("��·�����ɹ���");
    //parent.mainFrame.location.reload();
}

//����ʾ��ͼԪ
function createDemoObj(){
    //window.parent.mainFrame.ShowHide("1",null);��ק��ʽ
    var url="<%=rootPath%>/submap.do?action=readyAddHintMeta&xml=<%=viewFile%>";
    var returnValue = showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
    //parent.mainFrame.location.reload();
}
//�ؽ�����ͼ
function rebuild(){
    if (window.confirm("ע��ò��������¹�������ͼ���ݣ�ԭ����ͼ���ݻᶪʧ����������")) {
        window.location = "<%=rootPath%>/submap.do?action=reBuild";
        alert("�����ɹ�!");
        parent.location.reload();
    }
}

// �л���ͼ
function changeName() 
{
    // ֮ǰ�����û�����
     <%
        if (isUpdateOperation) {
    %>
        if (window.confirm("���л���ͼ��ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
            saveFile();
        }
    <%
        }
    %>
    
    if (g_viewFlag == 0) {
        g_viewFlag = 1;
        window.parent.parent.leftFrame.location = "tree.jsp?treeflag=1";
        parent.mainFrame.location = curTarget+"&viewflag=1";
    }
    else if (g_viewFlag == 1) {
        g_viewFlag = 0;
        window.parent.parent.leftFrame.location = "tree.jsp?treeflag=0";        
        parent.mainFrame.location = curTarget+"&viewflag=0";
    }
    else {
        window.alert("��ͼ���ʹ���");
    }
}

// ��ʾ��ͼ������
function showController(flag) {
    var result;
    if (flag == false)
        controller = false;
    if (controller) {
        result = parent.mainFrame.showController(controller);
        
        if (result == false) {
            window.alert("��û��ѡ����ͼ���޿���������");
            return;
        }
            
        document.all.controller.value = "�رտ�����";
        document.all.controller.title = "�ر���ʾ���ڵ���ͼ������";
        controller = false;
    }
    else {
        result = parent.mainFrame.showController(controller);
        
        if (result == false) {
            window.alert("��û��ѡ����ͼ���޿���������");
            return;
        }

        document.all.controller.value = "����������";
        document.all.controller.title = "������ʾ���ڵ���ͼ������";
        controller = true;
    }
}
    function autoRefresh() 
    {
        window.clearInterval(freshTimer);
        freshTimer = window.setInterval("refreshFile()",60000);
    }

// ����ͼƬ
function swapImage(imageID, imageSrc) {
    document.all(imageID).src = imageSrc;
}
//ѡ����ͼ
function changeView()
{
    if(document.all.submapview.value == "")return;
    //parent.location = "../submap/submap.jsp?submapXml=" + document.all.submapview.value;
    window.parent.parent.location = "../submap/index.jsp?submapXml=" + document.all.submapview.value;
}
//����ͼ����
function editMap(){
    var url="<%=rootPath%>/submap.do?action=readyEditMap";
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<table width="100%" height="27" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td width="10" style="font-size:9pt;"><img src="image/toolbar_head.gif" width="7" height="20" border="0" style="vertical-align:baseline;"/></td>
        <td width="56"><input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" value="����" title="����"/></td>
        <td width="56" style="display: <%=updateOperationDisable %>;"><input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" value="����" title="���浱ǰ����ͼ����"/></td>
        <td width="56"><input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" value="ˢ��" title="ˢ�µ�ǰ����ͼ����"/></td>
        <td width="10" style="font-size:9pt;"><img src="image/line_vertical.gif" width="7" height="20" border="0" style="vertical-align:baseline;"/></td>
        <td width="56" ><input type="button" name="view" class="button_view_out" onmouseover="javascript:buttonViewOver();" onmouseout="javascript:buttonViewOut();" onclick="javascript:changeName();" value="�л�" title="�ı��豸����ʾ��Ϣ"/></td>
        <td width="100" style="display: <%=updateOperationDisable %>;"><input type="button" name="editmap" class="button_editmap_out" onmouseover="javascript:buttonEditMapOver();" onmouseout="javascript:buttonEditMapOut();" onclick="javascript:editMap();" value="����ͼ����" title="����ͼ����"/></td>
        <td width="100" style="display: <%=createTopologyLineOperationDisable%>;"><input type="button" name="create1" class="button_create1_out" onmouseover="javascript:buttonCreate1Over();" onmouseout="javascript:buttonCreate1Out();" onclick="javascript:createEntityLink();" value="����ʵ����·" title="����ʵ����·"/></td>
        <td width="100" style="display: <%=createTopologyHinLineOperationDisable%>;"><input type="button" name="create2" class="button_create2_out" onmouseover="javascript:buttonCreate2Over();" onmouseout="javascript:buttonCreate2Out();" onclick="javascript:createDemoLink();" value="����ʾ����·" title="����ʾ����·"/></td>
        <td width="100" style="display: <%=createTopologyHinNodeOperationDisable%>;"><input type="button" name="create3" class="button_create3_out" onmouseover="javascript:buttonCreate3Over();" onmouseout="javascript:buttonCreate3Out();" onclick="javascript:createDemoObj();" value="����ʾ��ͼԪ" title="����ʾ��ͼԪ"/></td>
        <td width="100" style="display: <%=createOperationDisable %>;"><input type="button" name="create4" class="button_create4_out" onmouseover="javascript:buttonCreate4Over();" onmouseout="javascript:buttonCreate4Out();" onclick="javascript:createSubMap();" value="������ͼ" title="������ͼ"/></td>
        <td width="100"><input type="button" name="create5" class="button_create5_out" onmouseover="javascript:buttonCreate5Over();" onmouseout="javascript:buttonCreate5Out();" onclick="javascript:rebuild();" value="�ؽ�����ͼ" title="�ؽ�����ͼ"/></td>
        <td width="56">
        
    <%if (fullscreen.equals("0")) {%>
    <!-- 
        <input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" value="ȫ��" title="ȫ���ۿ���ͼ"/>
         -->
    <%}else {%>
        <!-- 
        <input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="�ر�" title="�رյ�ǰ����"/>
       -->
    <%}%>
   
        </td>
        <td width="80"><input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showController();" value="����������" title="�ر���ʾ���ڵ���ͼ������"/></td>
        <td width="100">
            <select width="320" name="submapview" onchange="changeView()">
            <option value="">--ѡ����ͼ--</option>
<%
    ManageXmlDao dao = new ManageXmlDao();
    List list = dao.loadAll();
    for(int i=0; i<list.size(); i++)
    {
        ManageXml vo = (ManageXml)list.get(i);
        int tag = 0;
        //System.out.println("vo.getBid()======"+vo.getBid());
        if(bids!=null&&bids.length>0){
            for(int j=0;j<bids.length;j++){
                if(vo.getBid()!=null&&!"".equals(vo.getBid())&&!"".equals(bids[j])&&vo.getBid().indexOf(bids[j])!=-1){
                    tag++;
                }
            }
        }
        //System.out.println("tag======"+tag);
        if(tag>0&&!"network.jsp".equals(vo.getXmlName())){
            out.print("<option value='" + vo.getXmlName()+ "'>" + vo.getTopoName()+ "</option>");
        }
        
    }   
%>
            </select>
        </td>
        <td width="80%"></td>       
      </tr>
    </table>
    </td>
  </tr>
</table>
</body>
</html>
