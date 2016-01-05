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
	// 判断全屏显示状态
	String fullscreen = request.getParameter("fullscreen");	
	if (fullscreen.equals("0")) 
	   fullscreen = "0";
	else 
	{
		fullscreen = "1";
		out.println("viewWidth = 0;");
	}

	// 取得用户权限---用来限制保存、刷新、编辑等操作
	boolean admin = false;
	String user = "admin";

	if (user.equalsIgnoreCase("admin") || user.equalsIgnoreCase("superuser")) {
		out.println("var admin = true;"); //为了－－编辑－－能正常使用
		admin = true;
	}
	else {
		out.println("var admin = false;");	
		admin = false;
	}
	out.println("</script>");
	
	String disable = "";//控制按钮是否激活
	if (!admin) {
		disable = "disabled=\"disabled\"";
	}
%>
<script type="text/javascript">
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // 是否显示快捷列表
	var controller = false;		// 是否显示控制器
	
function searchNode()
{	
	var ip = window.prompt("请输入需要搜索的设备IP地址", "在此输入设备IP地址");
	if (ip == null)
		return true;
	else if (ip == "在此输入设备IP地址")
		return;

	if (!checkIPAddress(ip))
		searchNode();

	var coor = window.parent.mainFrame.getNodeCoor(ip);
	if (coor == null)
	{
		var msg = "没有在图中搜索到IP地址为 "+ ip +" 的设备。";
		window.alert(msg);
		return;
	}
	else if (typeof coor == "string")
	{
		window.alert(coor);
		return;
	}

	// 移动设备到中心标记处
	window.parent.mainFrame.moveMainLayer(coor);
}

// 保存拓扑图
function saveFile() {
	if (!admin) {
		window.alert("您没有保存视图的权限！");
		return;
	}
	parent.mainFrame.saveFile();
}

// 刷新拓扑图
function refreshFile() 
{
	if (window.confirm("“刷新”前是否需要保存当前拓扑图？")) {
		parent.topFrame.saveFile();
	}
	parent.mainFrame.location.reload();
}

// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("custom.jsp?filename="+document.all.customview.value+"&fullscreen=yes&user=<%=user%>", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

// 切换视图
function changeName() 
{
	// 之前提醒用户保存
	if (admin) {
		if (window.confirm("“切换视图”前是否需要保存当前拓扑图？")) {
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
		window.alert("视图类型错误");
	}
}
//创建链路
function createEntityLink(){
    var objLinkAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry.length>0){//框选
        objLinkAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrl选
        objLinkAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    if(objLinkAry.length!=2){
        alert("请选择两个设备！");
        return;
    }
    var lineArr = window.parent.frames['mainFrame'].lineMoveAry;
    var start_id = objLinkAry[0].id.replace("node_","");
    var end_id = objLinkAry[1].id.replace("node_","");
    var xml = "<%=fileName%>";
    var lineStr = start_id+"_"+end_id;
    for(var i=0;i<lineArr.length;i++){
        if(lineArr[i].id.replace("line_","")==lineStr){
            alert("选中的两台设备已经存在链路!");
            return;
        }
    }
    parent.mainFrame.location = "<%=rootPath%>/customview.do?action=addLines&xml="+xml+"&id1="+start_id+"&id2="+end_id;
}
// 显示视图控制器
function showController(flag) {

	var result;
	if (flag == false)
		controller = false;
	if (controller) {
		result = parent.mainFrame.showController(controller);
		
		if (result == false) {
			window.alert("您没有选择视图，无控制器可用");
			return;
		}
			
		document.all.controller.value = "关闭控制器";
		document.all.controller.title = "关闭显示框内的视图控制器";
		controller = false;
	}
	else {
		result = parent.mainFrame.showController(controller);
		
		if (result == false) {
			window.alert("您没有选择视图，无控制器可用");
			return;
		}

		document.all.controller.value = "开启控制器";
		document.all.controller.title = "开启显示框内的视图控制器";
		controller = true;
	}
}
	function autoRefresh() 
	{
		window.clearInterval(freshTimer);
		freshTimer = window.setInterval("refreshFile()",60000);
	}

// 交换图片
function swapImage(imageID, imageSrc) {
	document.all(imageID).src = imageSrc;
}

//选择视图
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
		<td width="56"><input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" value="搜索" title="搜索" <%=disable%>/></td>
		<td width="56"><input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" value="保存" title="保存当前拓扑图数据" <%=disable%>/></td>
		<td width="56"><input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" value="刷新" title="刷新当前拓扑图数据"/></td>
		<td width="10" style="font-size:9pt;"><img src="image/line_vertical.gif" width="7" height="20" border="0" style="vertical-align:baseline;"/></td>
		<td width="56"><input type="button" name="view" class="button_view_out" onmouseover="javascript:buttonViewOver();" onmouseout="javascript:buttonViewOut();" onclick="javascript:changeName();" value="切换" title="改变设备名显示信息"/></td>
		<td width="100"><input type="button" name="create1" class="button_create4_out" onmouseover="javascript:buttonCreate4Over();" onmouseout="javascript:buttonCreate4Out();" onclick="javascript:createEntityLink();" value="创建链路" title="创建链路"/></td>
		<td width="56">
	<%if (fullscreen.equals("0")) {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" value="全屏" title="全屏观看视图"/>
	<%}else {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="关闭" title="关闭当前窗口"/>
	<%}%>
		</td>
		<td width="80"><input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showController();" value="开启控制器" title="关闭显示框内的视图控制器"/></td>
		<td width="100">
			<select width="320" name="customview" onchange="changeView()">
			<option value="testData.jsp">--选择视图--</option>
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
