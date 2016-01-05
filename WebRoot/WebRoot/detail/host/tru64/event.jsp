<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.temp.model.DeviceNodeTemp"%>
<%@page import="com.afunms.event.model.EventList"%>
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
    
    String CSDVersion = (String) request.getAttribute("CSDVersion");  // 位置
    String processorNum = (String) request.getAttribute("processorNum");  // 类别
    String pageingUsed = (String) request.getAttribute("pageingUsed");  // 类别
    String totalPageing = (String) request.getAttribute("totalPageing");  // 类别
    String mypage = "liusu";

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
    String curVirtualMemoryValue = (String) request.getAttribute("curVirtualMemoryValue");
    String curPhysicalMemoryValue = (String) request.getAttribute("curPhysicalMemoryValue");

    String curPingImage = (String) request.getAttribute("curPingImage");          // 名称
    String curMemoryImage = (String) request.getAttribute("curMemoryImage");  // 位置
    String curCPUImage = (String) request.getAttribute("curCPUImage");  // 位置
    
    // 连通率
    String curDayPingMaxValue = (String) request.getAttribute("curDayPingMaxValue");
    String curDayPingAvgValue = (String) request.getAttribute("curDayPingAvgValue"); 

    // 响应时间
    String curDayResponseTimeBarImage = (String) request.getAttribute("curDayResponseTimeBarImage");
    String curDayResponseTimeMaxValue = (String) request.getAttribute("curDayResponseTimeMaxValue");
    String curDayResponseTimeAvgValue = (String) request.getAttribute("curDayResponseTimeAvgValue"); 

    // CPU
    String curDayCPUMaxValue = (String) request.getAttribute("curDayCPUMaxValue");
    String curDayCPUAvgValue = (String) request.getAttribute("curDayCPUAvgValue");
    String curDayCPUMaxImageInfo = (String) request.getAttribute("curDayCPUMaxImageInfo"); 
    String curDayCPUAvgImageInfo = (String) request.getAttribute("curDayCPUAvgImageInfo"); 

    // 设备信息
    List<DeviceNodeTemp> currDeviceNodeTempList = (List<DeviceNodeTemp>) request.getAttribute("currDeviceNodeTempList"); 

    // 内存柱状图AMCHART的XML
    Hashtable<Integer, Hashtable<String, String>> curDayMemoryValueHashtable = (Hashtable<Integer, Hashtable<String, String>>) request.getAttribute("curDayMemoryValueHashtable"); 
    Hashtable<Integer, Hashtable<String, String>> curDayDiskValueHashtable = (Hashtable<Integer, Hashtable<String, String>>) request.getAttribute("curDayDiskValueHashtable"); 
    String curDayAmMemoryChartInfo = (String) request.getAttribute("curDayAmMemoryChartInfo"); 
    String curDayAmDiskChartInfo = (String) request.getAttribute("curDayAmDiskChartInfo"); 
    
    Hashtable curDayMaxMemoryValueHashtable = (Hashtable) request.getAttribute("curDayMaxMemoryValueHashtable"); 
    Hashtable curDayAvgMemoryValueHashtable = (Hashtable) request.getAttribute("curDayAvgMemoryValueHashtable"); 

    List list = (List) request.getAttribute("list");
    String startdate = (String) request.getAttribute("startdate");
    String todate = (String) request.getAttribute("todate");

    int level1 = Integer.parseInt(request.getAttribute("level1") + "");
    int _status = Integer.parseInt(request.getAttribute("status") + "");

    String level0str = "";
    String level1str = "";
    String level2str = "";
    String level3str = "";
    if (level1 == 0) {
        level0str = "selected";
    } else if (level1 == 1) {
        level1str = "selected";
    } else if (level1 == 2) {
        level2str = "selected";
    } else if (level1 == 3) {
        level3str = "selected";
    }

    String status0str = "";
    String status1str = "";
    String status2str = "";
    if (_status == 0) {
        status0str = "selected";
    } else if (_status == 1) {
        status1str = "selected";
    } else if (_status == 2) {
        status2str = "selected";
    }
%> 
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css"  href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript"  src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
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
    location.href="<%=rootPath%>/tru64Data.do?action=getInterfaceInfo&nodeid=<%=nodeid%>&type=<%=type%>&subtype=<%=subtype%>&ipaddress=<%=ipaddress%>&flag=<%=flag%>&orderflag="+para;
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
//  IpAliasRemoteManager.modifyIpAlias(ipaddress,ipalias);
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
    //  gzmajax();
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
    
    function batchAccfiEvent(){
         var eventids = ''; 
         for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
                eventids = eventids + mainForm.checkbox[i].value;
                eventids = eventids + ",";
           }
        }
        //把eventids放到attribute中
        if(eventids == ""){
            alert("未选中");
            return ;
        }
        window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
    }
    
    //batchDoReport();
    function batchDoReport(){
         var eventids = ''; 
         for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
                eventids = eventids + mainForm.checkbox[i].value;
                eventids = eventids + ",";
           }
        }
        //把eventids放到attribute中
        if(eventids == ""){
            alert("未选中");
            return ;
        }
        window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
    }
    
    function batchEditAlarmLevel(){
         var eventids = ''; 
         for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
                eventids = eventids + mainForm.checkbox[i].value;
                eventids = eventids + ",";
           }
        }
        //把eventids放到attribute中
        if(eventids == ""){
            alert("未选中");
            return ;
        }
        window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
    }
    

function fiReport(eventid){
    mainForm.eventid.value=eventid;
    mainForm.action="<%=rootPath%>/monitor.do?action=fireport"; 
    mainForm.submit();  ;
}
function viewReport(eventid){
    mainForm.eventid.value=eventid;
    mainForm.action="<%=rootPath%>/monitor.do?action=viewreport";   
    mainForm.submit();
}  
function query()
   {  
     mainForm.action = "<%=rootPath%>/tru64Data.do?action=getEventInfo";
     mainForm.submit();
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
    document.getElementById('tru64DetailTitle-9').className='detail-data-title';
    document.getElementById('tru64DetailTitle-9').onmouseover="this.className='detail-data-title'";
    document.getElementById('tru64DetailTitle-9').onmouseout="this.className='detail-data-title'";
}

function refer(action){
        var mainForm = document.getElementById("mainForm");
        mainForm.action = '<%=rootPath%>' + action;
        mainForm.submit();
}



</script>  
</head>
<body id="body" class="body" onload="initmenu();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
        <input type=hidden name="flag" value="<%=flag%>">
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
                                            <jsp:include page="/topology/includejsp/systeminfo_tru64.jsp">
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
                                                 <jsp:param name="curDayAmMemoryChartInfo" value="<%= curDayAmMemoryChartInfo %>"/>
                                                 <jsp:param name="curDayAmDiskChartInfo" value="<%= curDayAmDiskChartInfo %>"/>
                                                 <jsp:param name="CSDVersion" value="<%= CSDVersion %>"/>
                                                 <jsp:param name="processorNum" value="<%= processorNum %>"/>
                                                 <jsp:param name="pageingUsed" value="<%= pageingUsed %>"/>
                                                 <jsp:param name="totalPageing" value="<%= totalPageing %>"/>
                                             </jsp:include> 
                                        </td>
                                    </tr>
                                    <tr>
                                            <td>
                                                <table id="detail-data" class="detail-data">
                                                    <tr>
                                                        <td class="detail-data-header">
                                                            <%=tru64DetailTitleTable%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table class="detail-data-body">
                                                                 <tr>
                                                                <td align=left height="28" bgcolor="#ECECEC">
																
     开始日期
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	
		截止日期
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
		事件等级
		<select name="level1">
		<option value="99">不限</option>
		<option value="0" <%=level0str%>>提示信息</option>
		<option value="1" <%=level1str%>>普通事件</option>
		<option value="2" <%=level2str%>>严重事件</option>
		<option value="3" <%=level3str%>>紧急事件</option>
		</select>
					
		处理状态
		<select name="status">
		<option value="99">不限</option>
		<option value="0" <%=status0str%>>未处理</option>
		<option value="1" <%=status1str%>>正在处理</option>
		<option value="2" <%=status2str%>>已处理</option>
		</select>	
	<input type="button" name="submitss" value="查询" onclick="query()"><hr>
						</td>
						</tr> 
						<tr align="right" bgcolor="#ECECEC">
							<td><table><tr>
							<td width="75%">&nbsp;</td>
							<td width="15" height=15 >&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="接受处理" onclick='batchAccfiEvent();'/></td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="填写报告" onclick='batchDoReport();'/></td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="修改等级" onclick="batchEditAlarmLevel();">&nbsp;&nbsp;</td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							</tr></table></td>
						</tr>
                    <tr> 
                      <td>
  <table cellSpacing="0"  cellPadding="0" border=0 height=28>
	
  <tr bgcolor="#ECECEC" height=28>
    	<td class="detail-data-body-title" align="center"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">序号</td>
        <td width="10%" class="detail-data-body-title" align="center"><strong>事件等级</strong></td>
    	<td width="40%" class="detail-data-body-title" align="center"><strong>事件描述</strong></td>
		<td class="detail-data-body-title" align="center"><strong>最近告警时间</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>处理状态</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>操作</strong></td>
   </tr>
<%
	int index = 0;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
  	for(int i=0;i<list.size();i++){
 	index++;
  	EventList eventlist = (EventList)list.get(i);
  	Date cc = eventlist.getRecordtime().getTime();
  	Integer eventid = eventlist.getId();
  	String eventlocation = eventlist.getEventlocation();
  	String content = eventlist.getContent();
  	String level = String.valueOf(eventlist.getLevel1());
  	String status = String.valueOf(eventlist.getManagesign());
  	String s = status;
  	String showlevel = null;
	String bgcolor = "";
  	String act="处理报告";
  	if("0".equals(level)){
  		level="提示信息";
  		bgcolor = "bgcolor='blue'";
  	}
	if("1".equals(level)){
  		level="普通告警";
  		bgcolor = "bgcolor='yellow'";
  	}
  	if("2".equals(level)){
  		level="严重告警";
  		bgcolor = "bgcolor='orange'";
  	}
  	if("3".equals(level)){
  		level="紧急告警";
  		bgcolor = "bgcolor='red'";
  	}
   	String bgcolorstr="";
  	if("0".equals(status)){
  		status = "未处理";
  		bgcolorstr="#9966FF";
  	}
  	if("1".equals(status)){
  		status = "处理中";  
  		bgcolorstr="#3399CC";	
  	}
  	if("2".equals(status)){
  	  	status = "处理完成";
  	  	bgcolorstr="#33CC33";
  	}
  	String rptman = eventlist.getReportman();
  	String rtime1 = _sdf.format(cc);
%>

 <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>

      <td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	  <td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
      <td class="detail-data-body-list" align="center">
      <%=content%></td>
       <td class="detail-data-body-list" align="center">
      <%=rtime1%></td>
       <td class="detail-data-body-list" align="center"  bgcolor=<%=bgcolorstr%>>
      <%=status%></td>
       <td class="detail-data-body-list" align="center">
       <%
       		if("0".equals(s)){
       		%>
       			<input type ="button" value="接受处理" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("1".equals(s)){
       		%>
       			<input type ="button" value="填写报告" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("2".equals(s)){
       		%>
       			<input type ="button" value="查看报告" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       %>
       </td>
 </tr>
 <%}

 %>  
   
 </table>

														   

														       	</td>
												      		</tr>
											    		</table>
											    </td>
											  </tr>
											</table>
											
										</td>
									</tr>
								</table>
							</td>
							<td class="td-container-main-tool" width=15%>
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=ipaddress%>" name="ipaddress"/>
									<jsp:param value="<%=sysObjectID%>" name="sys_oid"/>
									<jsp:param value="<%=nodeid%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="tru64" name="subtype"/>
								</jsp:include>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>