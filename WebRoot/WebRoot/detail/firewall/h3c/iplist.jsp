<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.config.model.IpAlias"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>


<%
    String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");//菜单
	NodeDTO node = (NodeDTO) request.getAttribute("node");
    String nodeid = (String) request.getAttribute("nodeid");
    HostNode hostNode = (HostNode) request.getAttribute("hostNode");
    String maxAlarmLevel = (String) request.getAttribute("maxAlarmLevel");  // 位置
    String category = (String) request.getAttribute("category");  // 类别
    String mac = (String) request.getAttribute("mac");  // 类别
    String supperName = (String) request.getAttribute("supperName");  // 类别

    String ipaddress = node.getIpaddress();
    String type = node.getType();
    String subtype = node.getSubtype();                         
    String alias = node.getName();        // 名称
    String netMask = hostNode.getNetMask();

    String sysDescr = (String) request.getAttribute("sysDescr");        // 描述
    String sysObjectID = (String) request.getAttribute("sysObjectID");  // OID
    String sysUpTime = (String) request.getAttribute("sysUpTime");      // 启动时间
    String sysName = (String) request.getAttribute("sysName");          // 名称
    String sysLocation = (String) request.getAttribute("sysLocation");  // 位置

    String curPingValue = (String) request.getAttribute("curPingValue");
    String curResponseTimeValue = (String) request.getAttribute("curResponseTimeValue");          // 名称
    String curCPUValue = (String) request.getAttribute("curCPUValue");
    String curMemoryValue = (String) request.getAttribute("curMemoryValue");
    
    String curPingImage = (String) request.getAttribute("curPingImage");          // 名称
    String curMemoryImage = (String) request.getAttribute("curMemoryImage");  // 位置
    String curCPUImage = (String) request.getAttribute("curCPUImage");  // 位置

    List<IpAlias> ipAliasList = (List<IpAlias>) request.getAttribute("ipAliasList");  // 位置
%> 
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/js/detail/net/netdetail.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/dwr/interface/NetRemoteService.js"></script>

 
<script src="<%=rootPath%>/include/AC_OETags.js" language="javascript"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

<script>

/*
*显示关键端口
*/
function showPort(){
	var showAllPortFlag = document.getElementById("allportflag").value;
	mainForm.action = "<%=rootPath%>/topology/network/networkview.jsp?id=<%=nodeid%>&flag=<%=flag%>&showAllPortFlag="+showAllPortFlag;
    mainForm.submit();
}

function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
</script>
<script language="javascript">	


  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function toGetConfigFile()
  {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=ipaddress%>";
        mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
function changeOrder(para){
  	location.href="<%=rootPath%>/h3cfirewallData.do?action=getInterfaceInfo&nodeid=<%=nodeid%>&type=<%=type%>&subtype=<%=subtype%>&ipaddress=<%=ipaddress%>&flag=<%=flag%>&orderflag="+para;
} 
function openwin3(operate,index,ifname)
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=nodeid%>&ipaddress=<%=ipaddress%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
} 

//function modifyIpAlias(ipalias){
	//window.alert(ipalias);
	//document.getElementById('ipalias').innerHTML = ipalias;
	//var ipaddress = document.getElementById('ipaddress');
	//异步调用后台动作 DWR
//	IpAliasRemoteManager.modifyIpAlias(ipaddress,ipalias);
//}

//网络设备的ip地址
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//获取下拉框的值
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("修改成功！");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
//setInterval(modifyIpAliasajax,60000);
});

</script>
<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"11111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	   
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	      
	           rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
       	    // alert("<%=nodeid%>"+"****"+"<%=ipaddress%>"+"****"+node+"******"+ipaddress);
	    window.open ("<%=rootPath%>/monitor.do?action=show_utilhdx&id=<%=nodeid%>&ipaddress=<%=ipaddress%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=350, width=840, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=ipaddress%>&ifindex="+node, "newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=no, location=yes, status=yes");
		//window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}
	function realtimeMonitor()
	{
		window.open ('<%=rootPath%>/monitor.do?action=portdetail&id=<%=nodeid%>&ip=<%=ipaddress%>&ifindex='+node,'端口流速','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function bandwidth()
	{
		window.open ('<%=rootPath%>/monitor.do?action=bandwidthdetail&id=<%=nodeid%>&ip=<%=ipaddress%>&ifindex='+node,'带宽','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function portstatus()
	{
		window.open ('<%=rootPath%>/portconfig.do?action=showPortStatus&index='+node+'&ip=<%=ipaddress%>','端口状态','top=200,left=300,height=435,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	setClass();
}

function setClass(){
	document.getElementById('h3cfirewallDetailTitle-5').className='detail-data-title';
	document.getElementById('h3cfirewallDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('h3cfirewallDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}



</script>  
</head>
<body id="body" class="body" onload="initmenu();">
<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">查看状态</td>
		</tr>
			
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portset();">端口设置</td>
		</tr>	
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.realtimeMonitor();">实时监控</td>
		</tr>
			
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.bandwidth();">带宽</td>
		</tr>
	<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portstatus();">端口状态</td>
		</tr>
	</table>
	</div>
<!-- 右键菜单结束-->
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="nodeid" value="<%=nodeid%>">
		<input type=hidden name="type" value="<%=type%>">
        <input type=hidden name="subtype" value="<%=subtype%>">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-detail" width=85%>
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td> 
											<jsp:include page="/topology/includejsp/systeminfo_net.jsp">
												 <jsp:param name="rootPath" value="<%= rootPath %>"/>
												 <jsp:param name="nodeid" value="<%= nodeid %>"/>
												 <jsp:param name="alias" value="<%= alias %>"/>
                                                 <jsp:param name="ipaddress" value="<%= ipaddress %>"/>
                                                 <jsp:param name="maxAlarmLevel" value="<%= maxAlarmLevel %>"/>
                                                 <jsp:param name="category" value="<%= category %>"/>
                                                 <jsp:param name="subtype" value="<%= subtype %>"/>
                                                 <jsp:param name="netMask" value="<%= netMask %>"/>
                                                 <jsp:param name="mac" value="<%= mac %>"/>
                                                 <jsp:param name="supperName" value="<%= supperName %>"/>
												 <jsp:param name="sysDescr" value="<%= sysDescr %>"/>
                                                  <jsp:param name="sysObjectID" value="<%= sysObjectID %>"/>
												 <jsp:param name="sysUpTime" value="<%= sysUpTime %>"/>
												 <jsp:param name="sysName" value="<%= sysName %>"/>
												 <jsp:param name="sysLocation" value="<%=sysLocation %>"/>  
										 		 <jsp:param name="curPingImage" value="<%=curPingImage%>"  />  
												 <jsp:param name="curResponseTimeValue" value="<%= curResponseTimeValue %>"/>	 
                                                 <jsp:param name="curMemoryImage" value="<%=curMemoryImage%>"  />  
                                                 <jsp:param name="curCPUImage" value="<%= curCPUImage %>"/> 
											 </jsp:include>	
										</td>
									</tr>
									<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=h3cfirewallDetailTitleTable%>

														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
                                                                <tr>
                                                                    <td align="center" class="body-data-title">序号</td>
                                                                    <td align="center" class="body-data-title">端口索引</td>
                                                                    <td align="center" class="body-data-title">描述</td>
                                                                    <td align="center" class="body-data-title">每秒字节数</td>
                                                                    <td align="center" class="body-data-title">IP</td>
                                                                    <td align="center" class="body-data-title">类型</td>
                                                                </tr>
                                                                <%
                                                                    if (ipAliasList != null || ipAliasList.size() > 0) {
                                                                        int i = 0;
                                                                        for (IpAlias ipAlias : ipAliasList) {
                                                                            i++;
                                                                        %>
                                                                        
                                                                <tr style=bgcolor:#FFFFF" bgcolor="#FFFFFF" height=28 <%=onmouseoverstyle%>>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=i%></td>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=ipAlias.getIndexs()%></td>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=ipAlias.getDescr()%></td>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=ipAlias.getSpeed()%></td>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=ipAlias.getAliasip()%></td>
                                                                    <td  class="detail-data-body-list" style=bgcolor:#FFFFFF"><%=ipAlias.getTypes()%></td>
                                                                </tr>   
                                                                        
                                                                        
                                                                        <%
                                                                        }
                                                                    } 
                                                                 %>
                                                            </table>
                                                        </td>
													</tr>
												</table>

											</td>
										</tr>
									</table>
								</td>
								<td class="td-container-main-tool" width="14%">
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=ipaddress%>" name="ipaddress" />
										<jsp:param value="<%=sysObjectID%>" name="sys_oid" />
										<jsp:param value="<%=nodeid%>" name="tmp" />
										<jsp:param value="network" name="category" />
										<jsp:param value="<%=subtype%>" name="subtype" />
									</jsp:include>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
    <script type="text/javascript">
Ext.onReady(function()
{
Ext.get("bkpCfg").on("click",function(){
//Ext.MessageBox.wait('数据加载中，请稍后.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=bkpCfg&ipaddress=<%=ipaddress%>&page=netcpu&id="+<%=ipaddress%>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=ipaddress%>&page=liusu&id="+<%=nodeid%>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
</HTML>