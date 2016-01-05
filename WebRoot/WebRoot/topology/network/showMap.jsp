<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires", 0);
    response.flushBuffer();
	String rootPath = request.getContextPath();	
	String viewFile = (String)session.getAttribute(SessionConstant.CURRENT_TOPO_VIEW);
	String homeFlag = request.getParameter("homeFlag");
	//if(homeFlag == true) home.jsp show the topology
	if("true".equals(homeFlag)) {
		viewFile = request.getParameter("fileName");
	}
	ManageXmlDao dao = new ManageXmlDao();
	ManageXml vo = (ManageXml)dao.findByXml(viewFile);
	String bg = "";
	String Title = "";
	if(vo!=null){
	    bg = vo.getTopoBg();
	    Title = vo.getTopoTitle();
	}
	int bgWidth = 0;
	int bgHeight = 0;
	if(bg != null && !"".equals(bg)){
		String path = application.getRealPath("/");
		File picture = new File(path+"resource"+File.separator+"image"+File.separator+"bg"+File.separator+bg);
		FileInputStream fileInputStream = null;
		try{
			fileInputStream = new FileInputStream(picture);
			BufferedImage sourceImg =ImageIO.read(fileInputStream);
			if(sourceImg != null) {
				bgWidth = sourceImg.getWidth();
				bgHeight = sourceImg.getHeight();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(fileInputStream != null)fileInputStream.close();
		}
	}
    
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyNodeOperationPermission());
    boolean isDeleteTopologyNodeOperation = roleOperationPermissionService.isDeleteOperationPermission();

    roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyHinNodeOperationPermission());
    boolean isDeleteTopologyHinNodeOperation = roleOperationPermissionService.isDeleteOperationPermission();
    
    roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyLineOperationPermission());
    boolean isUpdateTopologyLineOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteTopologyLineOperation = roleOperationPermissionService.isDeleteOperationPermission();
    
    roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getTopologyHinLineOperationPermission());
    boolean isUpdateTopologyHinLineOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteTopologyHinLineOperation = roleOperationPermissionService.isDeleteOperationPermission();
    
%> 
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="content-type" content="text/html; charset=gb2312" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>显示服务器视图</title>   
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/profile.js"></script>
<%
	//-----------判断全屏显示状态----------------
	String fullscreen = request.getParameter("fullscreen");
	if (fullscreen == null || fullscreen.equals("0")) {
		out.println("<script type=\"text/javascript\">var fullscreen = 0;</script>");
	}
	else {
	// 如果是全屏显示，修改 viewWidth
		out.println("<script type=\"text/javascript\">var fullscreen = 1;");
		out.println("viewWidth = window.screen.width;</script>");
	}
%>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/TopoRemoteService.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/LinkRemoteService.js"></script>
<script type="text/javascript" src="js/topology.js"></script>
<%
	// 它控制设备名称显示信息（IP还是名字text）
	// g_viewflag在global.js中定义，默认为0，所以要在其后
	String viewflag = request.getParameter("viewflag");	
	if (viewflag == null) 
		out.print("<script type=\"text/javascript\">g_viewFlag = 0;</script>");
	else 
		out.print("<script type=\"text/javascript\">g_viewFlag = " + viewflag + ";</script>");	
	
%>
<script type="text/javascript">
	window.onerror = new Function('return true;');		// 容错
	var fatherXML = "<%=viewFile%>";//yangjun add 关联拓扑图时获得父页xml
	openProcDlg();  //显示闪屏
	function saveFile() {
		resetProcDlg();
		var target = "showMap.jsp?filename=<%=viewFile%>&fullscreen=1";
		parent.topFrame.updateState(target);
		topologyMapObject.save();  //topoloty.js中的函数,用于保存图数据--->String串
	}

	function doInit() {
        window.setTimeout(loadInit, 1000);
	}
    var isloadFlag = false;
    function loadInit() {
        topologyMapObject = new TopologyMapObject();
        topologyMapObject.xmlName = fatherXML;
       <%
            // 删除设备
            if (isDeleteTopologyNodeOperation) {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapNode = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapNode = false;
            <%
            }
        %>
        <%
            // 删除示意设备
            if (isDeleteTopologyHinNodeOperation) {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapHinNode = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapHinNode = false;
            <%
            }
        %>
        <%
            // 删除链路
            if (isDeleteTopologyLineOperation) {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapLine = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapLine = false;
            <%
            }
        %>
        <%
            // 删除示意链路
            if (isDeleteTopologyHinLineOperation) {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapHinLine = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.deleteTopolgyMapHinLine = false;
            <%
            }
        %>
        <%
            // 修改链路
            if (isUpdateTopologyLineOperation) {
            %>
                topologyMapObject.topologyMapOperation.updateTopolgyMapLine = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.updateTopolgyMapLine = false;
            <%
            }
        %>
        <%
            // 修改示意链路
            if (isUpdateTopologyHinLineOperation) {
            %>
                topologyMapObject.topologyMapOperation.updateTopolgyMapHinLine = true;
            <%
            } else {
            %>
                topologyMapObject.topologyMapOperation.updateTopolgyMapHinLine = false;
            <%
            }
        %>
        var divLayer = topologyMapObject.divLayer;
        while(divLayer && divLayer.hasChildNodes()) {
            var oChild = divLayer.children(0);
            if (oChild) {
                divLayer.removeChild(oChild);
            }
        }
        topologyMapObject.loadXML("<%=rootPath%>/resource/xml/<%=viewFile%>?" + "<%=Math.random()%>");
        <%if ("networkvlan.jsp".equalsIgnoreCase(viewFile)){ %>
            // document.all("vlan").value = 1;
        <%
            }else{
        %>
            // document.all("vlan").value = 0;
        <%
            }
        %>
        
        
        isloadFlag = true;
        var autoR = setInterval(autoRefresh,1000*60*3);
        autoRefresh();
    }
	
	function autoRefresh()
	{
	     TopoRemoteService.refreshImage(topologyMapObject.nodeobjArray,{
				callback:function(data){
					if(data){
						topologyMapObject.replaceNodeobj(data);
					}
				}
			});
			LinkRemoteService.refreshLink(topologyMapObject.linkobjArray,{
				callback:function(data){
					if(data){
						topologyMapObject.replaceLinkobj(data);
					}
				}
			});
	}
    
    function getNodeId(id) {
        return topologyMapObject.getNodeId(id);
    }
    
	function deleteLink(id) {
	    if (window.confirm("确定删除该链路吗？")) {
	        window.location = "<%=rootPath%>/link.do?action=deleteLink&lineId="+id+"&xml=<%=viewFile%>";
	    }
    }
    function editLink(id) {
	    var url="<%=rootPath%>/link.do?action=editLink&lineId="+id;
        showModalDialog(url,window,'dialogwidth:500px; dialogheight:360px; status:no; help:no;resizable:0');
    }
    //删除示意链路
    function deleteLine(id){
        window.location = "<%=rootPath%>/link.do?action=deleteDemoLink&id="+id+"&xml=<%=viewFile%>";
        //alert("删除成功！");
        //autoRefresh();
    }
    //删除示意设备
    function deleteHintMeta(id) {
        var xml = "<%=viewFile%>";
        if (window.confirm("确定删除该设备吗？")) {
            window.location = "<%=rootPath%>/submap.do?action=deleteHintMeta&nodeId="+id+"&xml="+xml;
            alert("删除成功！");
	        window.location = "showMap.jsp";
	    }
    }
    //添加实体设备
    function addEquip(nodeid,nodeCategory){
        var xml = "<%=viewFile%>";
        var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";
        TopoRemoteService.addEquipToMap(xml, nodeid, nodeCategory,{
				callback:function(data){
					if(data){
						topologyMapObject.addNode(nodeid,url);
					}
				}
			});
    }
    //删除实体设备
    function deleteEquip(nodeid,category){
        if (window.confirm("此操作会将该设备从系统彻底删除,确定删除该设备吗？")) {
            window.location="<%=rootPath%>/submap.do?action=deleteEquipFromSubMap&xml=<%=viewFile%>&node="+nodeid+"&category="+category;
            alert("删除成功！");
            window.location = "showMap.jsp";
        }
    }
    //只从拓扑图移除实体设备
    function removeEquip(nodeid){
        if (window.confirm("此操作会将该设备从当前拓扑图删除,确定删除该设备吗？")) {
            window.location="<%=rootPath%>/submap.do?action=removeEquipFromSubMap&xml=<%=viewFile%>&node="+nodeid;
            alert("删除成功！");
            window.location = "showMap.jsp";
           // var xml = "<%=viewFile%>";
           // var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";
           // TopoRemoteService.removeEquipFromSubMap(xml, nodeid, {
			//	callback:function(data){
			//		if(data){
						//removeNode(nodeid,url);  
			//			autoRefresh(); 
			//		}
			//	}
			//});
        }
    }
    //服务器设备相关应用添加
    function addApplication(nodeid,ip){
        //alert(nodeid+"_"+ip);
        window.location="<%=rootPath%>/submap.do?action=addApplications&xml=<%=viewFile%>&node="+nodeid+"&ip="+ip;
        alert("获取该服务器相关应用成功！");
        window.location = "showMap.jsp";
    }
    //查看设备面板图
    function showpanel(ip,width,height){
        window.open("<%=rootPath%>/submap.do?action=showpanel&ip="+ip,"panelfullScreenWindow", "toolbar=no,height="+height+",width="+width + ",scrollbars=no,"+"screenX=0,screenY=0");
    }
    //创建实体链路
    function addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index, cable_type, cable_capacity){
        var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";
        LinkRemoteService.addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index, cable_type, cable_capacity, {
				callback:function(data){
					if(data=="error"){
						alert("实体链路创建失败！");
					} else if(data=="error1"){
					    alert("实体链路创建失败:相同端口的链路已经存在!");
					} else if(data=="error2"){
					    alert("实体链路创建失败:已经创建双链路!");
					} else {
					    if(data){
					        var arr=data.split(":");
					        if(arr[1]=="0"){
					            topologyMapObject.addlink(arr[0],url);
					        } else {
					            topologyMapObject.addAssLink(arr[0],url)
					        }
					    }
					}
				}
			});
    }
    //创建示意链路
    function addline(direction1,xml,line_name,link_width,start_id,start_x_y,s_alias,end_id,end_x_y,e_alias){
        var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";
        LinkRemoteService.addDemoLink(direction1,xml,line_name, link_width, start_id, start_x_y, s_alias, end_id, end_x_y, e_alias, {
				callback:function(data){
					if(data=="error"){
						alert("示意链路创建失败！");
					} else {
					    if(data){
					        topologyMapObject.addLine(data,url);
					    }
					}
				}
			});
    }
    //添加示意设备
    function addHintMeta(setting){
        var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";
        TopoRemoteService.addHintMeta(setting,{
				callback:function(data){
				    if(data=="error"){
						alert("添加示意图元失败！");
					} else {
						topologyMapObject.addNode(data,url);
					}
				}
			});
    }
    
    function showalert(id) {
		//window.parent.parent.opener.location="/afunms/detail/dispatcher.jsp?id="+id;
		window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
	}
</script>
<style>
v\:*{ behavior:url(#default#VML); }
</style>
</head>

<!--画框选择时，用的上下左右四根彩线-->
<img src="<%=rootPath%>/resource/image/topo/line_top.gif" id="imgTop" class="tmpImg" style="width:10; height:10 " />
<img src="<%=rootPath%>/resource/image/topo/line_left.gif" id="imgLeft" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_bottom.gif" id="imgBottom" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_right.gif" id="imgRight" class="tmpImg" style="width:10; height:10 "/>


<body class="main_body"  onLoad="doInit();window.parent.changeFlags();" onmousewheel="window.parent.parent.document.body.scrollTop -= event.wheelDelta/2;" onmousedown="bodyDown()" onselectstart="return false" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">
    <form name="frmMap" method="post" action="<%=rootPath%>/network.do?action=save&curTime=" + "<%=(new Date().getTime()) + Math.random()%>">
        <div id="divLayer" style="background-position: center;background-attachment:scroll;background-repeat: no-repeat;background-image:url(<%=rootPath%>/resource/image/bg/<%=bg%>);width:<%=bgWidth%>;height:<%=bgHeight%>;color:black;position:absolute;top:0px;left:0px;background-color:#FFFFFF;border:#FfFfFF; 1px solid;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();">
        </div>
        <input type="hidden" name="hidXml"/>
        <input type="hidden" name="vlan"/>
        <input type="hidden" name="urlpath" value="' + urlpath + '"/>
        <input type="hidden" name="filename" value="' + filename + '"/>
    </form>
</body>

<script type="text/javascript">
<!--
// 调整 divLayer 大小
function resizeTopDiv() {
		//document.all.divLayer.style.width = maxWidth + 800;
		//document.all.divLayer.style.height = maxHeight + 650;
		zoomProcDlg("out");
}
	
	setTimeout("resizeTopDiv()", 1000);	
	parent.topFrame.showController(false);
	function showDevice(action) {
		parent.location = action;
	}
//-->   
</script>
</html>
