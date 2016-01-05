<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.CustomXml"%>
<%@page import="com.afunms.topology.dao.CustomXmlDao"%>
<%
   String rootPath = request.getContextPath();
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
			
		//document.all.controller.value = "�رտ�����";
		//document.all.controller.title = "�ر���ʾ���ڵ���ͼ������";
		controller = false;
	}
	else {
		result = parent.mainFrame.showController(controller);
		
		if (result == false) {
			window.alert("��û��ѡ����ͼ���޿���������");
			return;
		}

		//document.all.controller.value = "����������";
		//document.all.controller.title = "������ʾ���ڵ���ͼ������";
		//controller = true;
		controller = false;
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
</form>
</body>
</html>
