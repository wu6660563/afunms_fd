<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.CustomXml"%>
<%@page import="com.afunms.topology.dao.CustomXmlDao"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
   String rootPath = request.getContextPath();
   String fileName = (String)session.getAttribute(SessionConstant.CURRENT_CUSTOM_VIEW);
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
<script type="text/javascript" src="js/customview.js"></script>
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

	// ȡ���û�Ȩ��---�������Ʊ��桢ˢ�¡��༭�Ȳ���
	boolean admin = false;
	String user = "admin";

	if (user.equalsIgnoreCase("admin") || user.equalsIgnoreCase("superuser")) {
		out.println("var admin = true;"); //Ϊ�ˣ����༭����������ʹ��
		admin = true;
	}
	else {
		out.println("var admin = false;");	
		admin = false;
	}
	out.println("</script>");
	
	String disable = "";//���ư�ť�Ƿ񼤻�
	if (!admin) {
		disable = "disabled=\"disabled\"";
	}
%>
<script type="text/javascript">
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // �Ƿ���ʾ����б�
	var controller = false;		// �Ƿ���ʾ������
	
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

// ��������ͼ
function saveFile() {
	if (!admin) {
		window.alert("��û�б�����ͼ��Ȩ�ޣ�");
		return;
	}
	parent.mainFrame.saveFile();
}

// ˢ������ͼ
function refreshFile() 
{
	if (window.confirm("��ˢ�¡�ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
		parent.topFrame.saveFile();
	}
	parent.mainFrame.location.reload();
}

// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("custom.jsp?filename="+document.all.customview.value+"&fullscreen=yes&user=<%=user%>", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

// �л���ͼ
function changeName() 
{
	// ֮ǰ�����û�����
	if (admin) {
		if (window.confirm("���л���ͼ��ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
			saveFile();
		}
	}
	
	if (g_viewFlag == 0) {
		g_viewFlag = 1;
		parent.mainFrame.location = curTarget+"&viewflag=1";
	}
	else if (g_viewFlag == 1) {
		g_viewFlag = 0;	
		parent.mainFrame.location = curTarget+"&viewflag=0";
	}
	else {
		window.alert("��ͼ���ʹ���");
	}
}
//������·
function createEntityLink(){
    var objLinkAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objLinkAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objLinkAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    if(objLinkAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
    var lineArr = window.parent.frames['mainFrame'].lineMoveAry;
    var start_id = objLinkAry[0].id.replace("node_","");
    var end_id = objLinkAry[1].id.replace("node_","");
    var xml = "<%=fileName%>";
    var lineStr = start_id+"_"+end_id;
    for(var i=0;i<lineArr.length;i++){
        if(lineArr[i].id.replace("line_","")==lineStr){
            alert("ѡ�е���̨�豸�Ѿ�������·!");
            return;
        }
    }
    parent.mainFrame.location = "<%=rootPath%>/customview.do?action=addLines&xml="+xml+"&id1="+start_id+"&id2="+end_id;
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
	if(document.all.customview.value == "")return;

	parent.mainFrame.location = "change.jsp?customview=" + document.all.customview.value;
}

</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<form name="topviewForm">
<table width="100%" height="27" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	  <tr>
	  	<td width="10" style="font-size:9pt;"><img src="image/toolbar_head.gif" width="7" height="20" border="0" style="vertical-align:baseline;"/></td>
		<td width="56"><input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" value="����" title="����" <%=disable%>/></td>
		<td width="56"><input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" value="����" title="���浱ǰ����ͼ����" <%=disable%>/></td>
		<td width="56"><input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" value="ˢ��" title="ˢ�µ�ǰ����ͼ����"/></td>
		<td width="10" style="font-size:9pt;"><img src="image/line_vertical.gif" width="7" height="20" border="0" style="vertical-align:baseline;"/></td>
		<td width="56"><input type="button" name="view" class="button_view_out" onmouseover="javascript:buttonViewOver();" onmouseout="javascript:buttonViewOut();" onclick="javascript:changeName();" value="�л�" title="�ı��豸����ʾ��Ϣ"/></td>
		<td width="100"><input type="button" name="create1" class="button_create4_out" onmouseover="javascript:buttonCreate4Over();" onmouseout="javascript:buttonCreate4Out();" onclick="javascript:createEntityLink();" value="������·" title="������·"/></td>
		<td width="56">
	<%if (fullscreen.equals("0")) {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" value="ȫ��" title="ȫ���ۿ���ͼ"/>
	<%}else {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="�ر�" title="�رյ�ǰ����"/>
	<%}%>
		</td>
		<td width="80"><input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showController();" value="����������" title="�ر���ʾ���ڵ���ͼ������"/></td>
		<td width="100">
			<select width="320" name="customview" onchange="changeView()">
			<option value="testData.jsp">--ѡ����ͼ--</option>
<%
	CustomXmlDao dao = new CustomXmlDao();
	List list = dao.loadAll();
	for(int i=0; i<list.size(); i++)
	{
		CustomXml vo = (CustomXml)list.get(i);
		out.print("<option value='" + vo.getXmlName() + "'>" + vo.getViewName()+ "</option>");
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
</form>
</body>
</html>
