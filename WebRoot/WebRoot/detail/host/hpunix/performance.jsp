<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.temp.model.DeviceNodeTemp"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="java.text.DecimalFormat"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>


<%  DecimalFormat df = new DecimalFormat("#.##");

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
    System.out.println("sysUpTime"+sysUpTime);
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
    String curDayAmWindowsDiskChartInfo = (String) request.getAttribute("curDayAmWindowsDiskChartInfo"); 
    
    Hashtable curDayMaxMemoryValueHashtable = (Hashtable) request.getAttribute("curDayMaxMemoryValueHashtable"); 
    Hashtable curDayAvgMemoryValueHashtable = (Hashtable) request.getAttribute("curDayAvgMemoryValueHashtable"); 
   
    String[] memoryItem = { "Capability", "Utilization" };
    String[] memoryItemch = { "容量", "当前", "最大", "平均" };

    String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
    String[] diskItemch = { "总容量", "已用容量", "利用率" };

    
    List<NodeTemp> curMemoryValueList = (List<NodeTemp>) request.getAttribute("curMemoryValueList"); 

    Hashtable<String, String> curCPUPerValueHashtable = (Hashtable<String, String>) request.getAttribute("curCPUPerValueHashtable"); 
    Hashtable<String, Hashtable<String, String>> curCPUDetailValueHashtable = (Hashtable<String, Hashtable<String, String>>) request.getAttribute("curCPUDetailValueHashtable"); 
    String curCPUDetailAmChartInfo = (String) request.getAttribute("curCPUDetailAmChartInfo"); 

    String curDayPageMaxValue = (String) request.getAttribute("curDayPageMaxValue"); 
    String curDayPageAvgValue = (String) request.getAttribute("curDayPageAvgValue"); 
    String curPageValueImageInfo = (String) request.getAttribute("curPageValueImageInfo"); 
    String curDayPageMaxValueImageInfo = (String) request.getAttribute("curDayPageMaxValueImageInfo"); 
    String curDayPageAvgValueImageInfo = (String) request.getAttribute("curDayPageAvgValueImageInfo"); 
                
    StringBuffer curPingValueDataSB = new StringBuffer();
    curPingValueDataSB.append("连通;").append(Math.round(Float.parseFloat(curPingValue))).append(";false;7CFC00\\n");
    curPingValueDataSB.append("未连通;").append(100-Math.round(Float.parseFloat(curPingValue))).append(";false;FF0000\\n");
    String curPingValueData = curPingValueDataSB.toString();

    StringBuffer curDayPingMaxValueDataSB = new StringBuffer();
    curDayPingMaxValueDataSB.append("连通;").append(Math.round(Float.parseFloat(curDayPingMaxValue))).append(";false;7CFC00\\n");
    curDayPingMaxValueDataSB.append("未连通;").append(100-Math.round(Float.parseFloat(curDayPingMaxValue))).append(";false;FF0000\\n");
    String curDayPingMaxValueData = curDayPingMaxValueDataSB.toString();

    StringBuffer curDayPingAvgValueDataSB = new StringBuffer();
    curDayPingAvgValueDataSB.append("连通;").append(Math.round(Float.parseFloat(curDayPingAvgValue))).append(";false;7CFC00\\n");
    curDayPingAvgValueDataSB.append("未连通;").append(100-Math.round(Float.parseFloat(curDayPingAvgValue))).append(";false;FF0000\\n");
    String curDayPingAvgValueData = curDayPingAvgValueDataSB.toString();

%> 
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

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
    location.href="<%=rootPath%>/netData.do?action=getInterfaceInfo&nodeid=<%=nodeid%>&type=<%=type%>&subtype=<%=subtype%>&ipaddress=<%=ipaddress%>&flag=<%=flag%>&orderflag="+para;
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
    document.getElementById('hpunixDetailTitle-0').className='detail-data-title';
    document.getElementById('hpunixDetailTitle-0').onmouseover="this.className='detail-data-title'";
    document.getElementById('hpunixDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
                                            <jsp:include page="/topology/includejsp/systeminfo_hpunix.jsp">
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
                                                            <%=hpunixDetailTitleTable%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table class="detail-data-body">
                                                                <tr>
                                                                    <td align=center >
                                                                        <br>
                                                                        <table cellpadding="0" cellspacing="0" width=48% align=center>
                                                                        <tr> 
                                                                            <td width="100%"  align=center> 
                                                                                        <div id="flashcontent3">
                                                                                    <strong>You need to upgrade your Flash Player</strong>
                                                                                </div>
                                                                                <script type="text/javascript">
                                                                                    var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=ipaddress%>", "Ping", "346", "180", "8", "#ffffff");
                                                                                    so.write("flashcontent3");
                                                                                </script>               
                                                                            </td>
                                                                        </tr> 
                                                                        <tr>
                                                                            <td width=100% align=center>
                                                                                <table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
                                                                                    <tr bgcolor="#FFFFFF">
                                                                                        <td height="29" class="detail-data-body-title" style="height:29">名称</td>
                                                                                        <td class="detail-data-body-title" style="height:29">当前（%）</td>
                                                                                        <td class="detail-data-body-title" style="height:29">最大（%）</td>
                                                                                        <td class="detail-data-body-title" style="height:29">平均（%）</td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td class="detail-data-body-list" height="29">连通率</td>
                                                                                        <td class="detail-data-body-list"><%=curPingValue%></td>
                                                                                        <td class="detail-data-body-list"><%=curDayPingMaxValue%></td>
                                                                                        <td class="detail-data-body-list"><%=curDayPingAvgValue%></td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>            
                                                                   </table> 
                                                                </td>
                  <td align=left valign=top width=57%>
                                                                        
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;">
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>连通率实时</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table id="detail-content-body" class="detail-content-body" border=1>
                                                            <tr>
                                                                <td>
                                                                <table  cellpadding="1" cellspacing="1" width=48% >
                                        
                                <tr>
                                    <td valign=top>
                                       
                                        

            <table width="90%" cellpadding="0" cellspacing="0"  >
                <tr>
                    <td valign=top>
                    <div id="realping">
                       <strong>You need to upgrade your Flash Player</strong>
                    </div>
                    <script type="text/javascript"
                         src="<%=rootPath%>/include/swfobject.js"></script>
                          <script type="text/javascript">
                         var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
                           so.addVariable("path", "<%=rootPath%>/amchart/");
                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                           so.addVariable("chart_data","<%=curPingValueData%>");
                           so.write("realping");
                    </script>
                </td>
                <td valign=top>
                <div id="maxping">
                     <strong>You need to upgrade your Flash Player</strong>
                </div>
                    <script type="text/javascript"
                         src="<%=rootPath%>/include/swfobject.js"></script>
                    <script type="text/javascript">
                        var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
                            so.addVariable("path", "<%=rootPath%>/amchart/");
                            so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                            so.addVariable("chart_data","<%=curDayPingMaxValueData%>");
                            so.write("maxping");
                    </script>
                </td>
                <td valign=top>
                <div id="avgping">
                  <strong>You need to upgrade your Flash Player</strong>
                </div>
                <script type="text/javascript"
                                   src="<%=rootPath%>/include/swfobject.js"></script>
                    <script type="text/javascript">
                          var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
                              so.addVariable("path", "<%=rootPath%>/amchart/");
                              so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                              so.addVariable("chart_data","<%=curDayPingAvgValueData%>");
                              so.write("avgping");
                    </script>
                </td>
                </tr>
                <tr height=50>
                    <td valign=top align=center><b>当前</b></td>
                <td valign=top align=center><b>最大</b></td>
                <td valign=top align=center><b>平均</b></td>
                </tr>               
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
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>                                                                        
                                                                        
    </td>
    </tr>
    <tr>
                  <td align=center >
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% >
                                        <tr> 
                                            <td width="100%"  align=center> 
                                                <div id="flashcontent4">
                                                    <strong>You need to upgrade your Flash Player</strong>
                                                </div>
                                                <script type="text/javascript">
                                                    var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=ipaddress%>", "Response_time", "346", "180", "8", "#ffffff");
                                                    so.write("flashcontent4");
                                                </script>               
                                            </td>
                                        </tr>  
                                <tr>
                                                                    <td width=100% align=center>
                                                                        <table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
                                                                            <tr bgcolor="#FFFFFF">
                                                                                <td class="detail-data-body-title" style="height:29" height="29">名称</td>
                                                                                <td class="detail-data-body-title" style="height:29">当前（ms）</td>
                                                                                <td class="detail-data-body-title" style="height:29">最大（ms）</td>
                                                                                <td class="detail-data-body-title" style="height:29">平均（ms）</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td class="detail-data-body-list" height="29">响应时间</td>
                                                                                <td class="detail-data-body-list"><%=curResponseTimeValue%></td>
                                                                                <td class="detail-data-body-list"><%=curDayResponseTimeMaxValue%></td>
                                                                                <td class="detail-data-body-list"><%=curDayResponseTimeAvgValue%></td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    </tr>           
                                    </table> 
                      </td>
                      <td align=center>
                                                                        <br>
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%" >
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;">
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>响应时间详情</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table id="detail-content-body" class="detail-content-body" border='1'>
                                                            <tr>
                                                                <td>
                                                                <table  cellpadding="1" cellspacing="1" width=48%>
                                        
                                                                <tr>
                                                                    <td valign=top>
                                       
                                        

                                                                    <table cellpadding="0" cellspacing="0" width=48% align=center >
                                                                    <tr>
                                                                    <td align=center colspan=3>
                                                                        <img src="<%=rootPath%>/<%=curDayResponseTimeBarImage%>">   
                                                                    </td>
                                                                    </tr> 
                                                                    <tr height=30>
                                                                        <td valign=top align=center><b>当前</b></td>
                                                                    <td valign=top align=center><b>最大</b></td>
                                                                    <td valign=top align=center><b>平均</b></td>
                                                                    </tr>                                                                              
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
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>                                                                            
                                                                </td>
                  </tr>                                                 
                                                              <tr>
                    <td align=center valign=top>
                    
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
                      
                                        <tr> 
                                            <td width="100%" align=center> 
                                                <div id="flashcontent1">
                                                    <strong>You need to upgrade your Flash Player</strong>
                                                </div>
                                                <script type="text/javascript">
                                                    var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=ipaddress%>", "Line_CPU", "346", "200", "8", "#ffffff");
                                                    so.write("flashcontent1");
                                                </script>               
                                            </td>
                                        </tr>  
                                 <tr>
                                                                    <td width=100% align=center>
                                                                        <table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
                                                                            <tr bgcolor="#FFFFFF">
                                                                                <td height="29" class="detail-data-body-title" style="height:29">名称</td>
                                                                                <td class="detail-data-body-title" style="height:29">当前</td>
                                                                                <td class="detail-data-body-title" style="height:29">最大</td>
                                                                                <td class="detail-data-body-title" style="height:29">平均</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td class="detail-data-body-list" height="29">&nbsp;CPU利用率</td>
                                                                                <td class="detail-data-body-list">&nbsp;<%=curCPUValue%>%</td>
                                                                                <td class="detail-data-body-list">&nbsp;<%=curDayCPUMaxValue%></td>
                                                                                <td class="detail-data-body-list">&nbsp;<%=curDayCPUAvgValue%></td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    </tr>           
                                    </table> 
                  </td>
                  <td align=center valign=top>
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;">
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>CPU利用率详情</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table id="detail-content-body" class="detail-content-body" border='1'>
                                                            <tr>
                                                                <td>
                                                                <table  cellpadding="1" cellspacing="1" width=48%>
                                        
                                                                <tr>
                                                                    <td valign=top>
                                       
                                        

                                                                    <table cellpadding="0" cellspacing="0" width=48% align=center>
                                                                    <tr>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curCPUImage %>"> 
                                                                    </td>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curDayCPUMaxImageInfo %>">   
                                                                    </td>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curDayCPUAvgImageInfo %>">   
                                                                    </td>
                                                                    </tr> 
                                                                    <tr height=20>
                                                                        <td valign=top align=center><b>当前</b></td>
                                                                    <td valign=top align=center><b>最大</b></td>
                                                                    <td valign=top align=center><b>平均</b></td>
                                                                    </tr>
                                                                    <tr height=20>
                                                                        <td colspan=3>&nbsp;</td>
                                                                    </tr>
                                                                                                                                                   
                                                                    </table>

                                                                    </td>
                   
                                                                    </tr> 
                                        
                                                                </table> 
                                                                        
                                                                                </td>
                                                                            </tr>
                                                                            
                                                                            <tr>
                                                                <td>
                                                                
                                                                
                                                                <table  cellpadding="1" cellspacing="1" width=80%>
                                        
                                                                <tr>
                                                                    <td valign=top width=80%>
                                                                    <table cellpadding="0" cellspacing="0" width=80% align=center>
<%
                                                                                                                                                        if (currDeviceNodeTempList != null) {
                                                                                                                                                            for (int m = 0; m < currDeviceNodeTempList.size(); m++) {
                                                                                                                                                                DeviceNodeTemp deviceNodeTemp =  currDeviceNodeTempList
                                                                                                                                                                        .get(m);
                                                                                                                                                                String deviceNodeTempName = deviceNodeTemp.getName();
                                                                                                                                                                String deviceNodeTempType = deviceNodeTemp.getType();
                                                                                                                                                                if (!"CPU".equals(deviceNodeTempType)) {
                                                                                                                                                                    continue;
                                                                                                                                                                }
                                                                                                                                                                String deviceNodeTempNameStatus = deviceNodeTemp.getStatus();
                                                                                                                                                    %>
                                                                                                                                                    <tr height=20>
                                                                                                                                                        <td valign=top align=center>
                                                                                                                                                            <b>类型</b>：<%=deviceNodeTempName%></td>
                                                                                                                                                        <td valign=top align=center>
                                                                                                                                                            <b>描述</b>：<%=deviceNodeTempName%></td>
                                                                                                                                                        <td valign=top align=center>
                                                                                                                                                            <b>状态</b>：<%=deviceNodeTempNameStatus%></td>
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
                                                </tr>
                                                
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
</tr>
                <tr>
                  <td align=center valign=top>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center border='1'>
                                        <tr> 
                                            <td width="100%"  align=center> 
                                                <div id="flashcontent2">
                                                    <strong>You need to upgrade your Flash Player</strong>
                                                </div>
                                                <script type="text/javascript">
                                                    var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=ipaddress%>", "Area_flux", "346", "200", "8", "#ffffff");
                                                    so.write("flashcontent2");
                                                </script>               
                                            </td>
                                </tr>
                                <tr>
                                                                    <td align=center>
                                                                    
                                                                    <table width="90%" cellpadding="0" cellspacing="0" style="align:center;width:346">
                  <tr align="center" height="28" style="background-color:#EEEEEE;"> 
                    <td width="21%" class="detail-data-body-title" style="height:29">内存名</td>
                    
                    <%
                                                                                                String pmem = "";
                                                                                                    String vmem = "";
                                                                                                    String pcurmem = "";
                                                                                                    String pmaxmem = "";
                                                                                                    String pavgmem = "";
                                                                                                    String vcurmem = "";
                                                                                                    String vmaxmem = "";
                                                                                                    String vavgmem = "";
                                                                                                    for (int j = 0; j < memoryItemch.length; j++) {
                                                                                                        String item = memoryItemch[j];
                                                                                            %>
                                                                                            <td class="detail-data-body-title"><%=item%></td>
                                                                                            <%
                                                                                                }
                                                                                            %>
                                                                                        </tr>
                                                                                        <%
                                                                                            for (int k = 0; k < curDayMemoryValueHashtable.size(); k++) {
                                                                                                    Hashtable mhash = (Hashtable) (curDayMemoryValueHashtable.get(new Integer(k)));
                                                                                                    String name = (String) mhash.get("name");
                                                                                        %>
                                                                                        <tr>
                                                                                            <td width="20%" class="detail-data-body-list"
                                                                                                height="29">
                                                                                                &nbsp;<%=name%></td>
                                                                                            <%
                                                                                                for (int j = 0; j < memoryItem.length; j++) {
                                                                                                            String value = "";
                                                                                                            if (mhash.get(memoryItem[j]) != null) {
                                                                                                                value = (String) mhash.get(memoryItem[j]);
                                                                                                                if (j == 0) {
                                                                                                                    if ("PhysicalMemory".equals(name))
                                                                                                                        pmem = value;
                                                                                                                    if ("SwapMemory".equals(name))
                                                                                                                        vmem = value;
                                                                                                                } else {
                                                                                                                    if ("PhysicalMemory".equals(name))
                                                                                                                        pcurmem = value;
                                                                                                                    if ("SwapMemory".equals(name))
                                                                                                                        vcurmem = value;
                                                                                                                }
                                                                                                            }
                                                                                            %>
                                                                                            <td width="17%" class="detail-data-body-list">
                                                                                                &nbsp;<%=value%></td>
                                                                                            <%
                                                                                                }
                                                                                                        String value = "";
                                                                                                        if (curDayMaxMemoryValueHashtable.get(name) != null) {
                                                                                                            value = (String) curDayMaxMemoryValueHashtable.get(name);
                                                                                                            if ("PhysicalMemory".equals(name))
                                                                                                                pmaxmem = value;
                                                                                                            if ("VirtualMemory".equals(name))
                                                                                                                vmaxmem = value;
                                                                                                        }
                                                                                                        String avgvalue = "";
                                                                                                        if (curDayAvgMemoryValueHashtable.get(name) != null) {
                                                                                                            avgvalue = (String) curDayAvgMemoryValueHashtable.get(name);
                                                                                                            if ("PhysicalMemory".equals(name))
                                                                                                                pavgmem = avgvalue;
                                                                                                            if ("VirtualMemory".equals(name))
                                                                                                                vavgmem = avgvalue;
                                                                                                        }
                                                                                            %>
                                                                                            <td width="20%" class="detail-data-body-list">
                                                                                                &nbsp;<%=value%></td>
                                                                                            <td width="20%" class="detail-data-body-list">
                                                                                                &nbsp;<%=avgvalue%></td>
                                                                                        </tr>
                                                                                        <%
                                                                                            }
                                                                                        %>
                </table>
                                                                        
                                                                    </td>
                                                                    </tr> 
                                
                                
                                             
                                    </table> 
                      </td>
                      <td align=center valign=top>
<br>                                                                            
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;">
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>设备内存明细</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign=top>
                                                        <table id="detail-content-body" class="detail-content-body" border='1'>
                                                            <tr>
                                                                <td valign=top>
                                                                <table  cellpadding="1" cellspacing="1" width=48%>
                                        
                                <tr>
                                    <td valign=top>

            <table width="90%" cellpadding="0" cellspacing="0"  border=0>
            <tr>
                  <td colspan=1>
                            <div id="memorypercent">
                                <strong>You need to upgrade your Flash Player</strong>
                            </div>
                            <script type="text/javascript">
                                var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
                                so.addVariable("path", "<%=rootPath%>/amchart/");
                                so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
                                so.addVariable("chart_data", "<%=curDayAmMemoryChartInfo%>");
                                so.addVariable("preloader_color", "#999999");
                                so.write("memorypercent");
                            </script>
            </tr>
            
            <tr height=20>
                  <td valign=middle align=center colspan=3>
                
                <table cellpadding="0" cellspacing="0" width=48% align=center>
                    <tr height=20>
                        <td valign=top align=center><b>物理内存总量</b>：<%=pmem%></td>
                        <td valign=top align=center><b>交换内存总量</b>：<%=vmem%></td>
                    </tr>                                                                      
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
                                                </tr>
                                                
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
                    </tr>
                    <!--  
                   <tr>
                    <td align=center valign=top>
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
                                        <tr> 
                                            <td width="100%"  align=center> 
                                                        <div id="flashcontent3_cpudtl">
                                                    <strong>You need to upgrade your Flash Player</strong>
                                                </div>
                                                <script type="text/javascript">
                                                    var so = new SWFObject("<%=rootPath%>/flex/Cpu_detail.swf?ipadress=<%=ipaddress%>", "CPU详细", "346", "370", "8", "#ffffff");
                                                    so.write("flashcontent3_cpudtl");
                                                </script>               
                                            </td>
                                </tr> 
                                            
                                    </table> 
                  </td>
                  
<td align=center>
                                                                        <br>
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;">
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>CPU信息详情</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table id="detail-content-body" class="detail-content-body" border='1'>
                                                            <tr>
                                                                <td>
                                                                <table  cellpadding="1" cellspacing="1" width=48%>
                                        
                                                                <tr>
                                                                    <td valign=top>
                                                                    <table cellpadding="0" cellspacing="0" width=48% align=center>
                                                                    <tr>
                                                                    <td align=center colspan=1>
                                                                    <div id="hpunixCpuDetail">
                                                                        <strong></strong>
                                                                    </div>
                                                                    <script type="text/javascript">
                                                                  <%  if(!curCPUDetailAmChartInfo.equals("0")){%>
                                                                       var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "485", "210", "8", "#FFFFFF");
                                                                           so.addVariable("path", "<%=rootPath%>/amchart/");
                                                                           so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
                                                                           so.addVariable("chart_data", "<%=curCPUDetailAmChartInfo%>");
                                                                           so.addVariable("preloader_color", "#999999");
                                                                           so.write("hpunixCpuDetail");
                                                                           <%}else{%>
                                                                           var _div=document.getElementById("hpunixCpuDetail");
                                                                            var img=document.createElement("img");
                                                                            img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
                                                                            _div.appendChild(img);
                                                                            <%}%>
                                                                    </script>
                                                                    </td>
                                                                    </tr> 
                                                                     <tr>
                                                                    <td width=100% align=center valign=top>
                                                                        <table cellpadding="1" cellspacing="1" style="align:center;width:100%" bgcolor="#FFFFFF">
                                                                            <tr bgcolor="#FFFFFF">
                                                                                <td align=center class="detail-data-body-list" style="align:center;height:29">名称</td>
                                                                                <td align=center class="detail-data-body-list" style="align:center;height:29">当前（%）</td>
                                                                                <td align=center class="detail-data-body-list" style="align:center;height:29">最大（%）</td>
                                                                                <td align=center class="detail-data-body-list" style="align:center;height:29">平均（%）</td>
                                                                            </tr>
                                                                            <% if(curCPUPerValueHashtable != null && curCPUPerValueHashtable.size()>0){ 
                                                                                String usr = (String)curCPUPerValueHashtable.get("%usr");
                                                                                
                                                                                String sys = (String)curCPUPerValueHashtable.get("%sys");
                                                                                String wio = (String)curCPUPerValueHashtable.get("%wio");
                                                                                String idle = (String)curCPUPerValueHashtable.get("%idle");
                                                                                String maxusr = "0";
                                                                                String maxsys = "0";
                                                                                String maxwio = "0";
                                                                                String maxidle = "0";
                                                                                String avgusr = "0";
                                                                                String avgsys = "0";
                                                                                String avgwio = "0";
                                                                                String avgidle = "0";
                                                                                if(curCPUDetailValueHashtable != null){
                                                                                    if(curCPUDetailValueHashtable.containsKey("usr")){
                                                                                        Hashtable usrhash = (Hashtable)curCPUDetailValueHashtable.get("usr");
                                                                                        if(usrhash != null){
                                                                                            if(usrhash.containsKey("maxvalue"))maxusr = (String)usrhash.get("maxvalue");
                                                                                            if(usrhash.containsKey("avgvalue"))avgusr = (String)usrhash.get("avgvalue");
                                                                                        }
                                                                                    }
                                                                                    if(curCPUDetailValueHashtable.containsKey("sys")){
                                                                                        Hashtable syshash = (Hashtable)curCPUDetailValueHashtable.get("sys");
                                                                                        if(syshash != null){
                                                                                            if(syshash.containsKey("maxvalue"))maxsys = (String)syshash.get("maxvalue");
                                                                                            if(syshash.containsKey("avgvalue"))avgsys = (String)syshash.get("avgvalue");
                                                                                        }
                                                                                    }
                                                                                    if(curCPUDetailValueHashtable.containsKey("wio")){
                                                                                        Hashtable wiohash = (Hashtable)curCPUDetailValueHashtable.get("wio");
                                                                                        if(wiohash != null){
                                                                                            if(wiohash.containsKey("maxvalue"))maxwio = (String)wiohash.get("maxvalue");
                                                                                            if(wiohash.containsKey("avgvalue"))avgwio = (String)wiohash.get("avgvalue");
                                                                                        }
                                                                                    }
                                                                                    if(curCPUDetailValueHashtable.containsKey("idle")){
                                                                                        Hashtable idlehash = (Hashtable)curCPUDetailValueHashtable.get("idle");
                                                                                        if(idlehash != null){
                                                                                            if(idlehash.containsKey("maxvalue"))maxidle = (String)idlehash.get("maxvalue");
                                                                                            if(idlehash.containsKey("avgvalue"))avgidle = (String)idlehash.get("avgvalue");
                                                                                        }
                                                                                    }
                                                                                }
                                                                            %>
                                                                            <tr>
                                                                                <td align=center class="detail-data-body-list" style="height:29">%用户（usr）</td>
                                                                                <td align=center class="detail-data-body-list"><%=usr%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxusr))%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgusr))%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align=center class="detail-data-body-list" style="height:29">%系统（sys）</td>
                                                                                <td align=center class="detail-data-body-list"><%=sys%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxsys))%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgsys))%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align=center class="detail-data-body-list" style="height:29">%IO等待（wio）</td>
                                                                                <td align=center class="detail-data-body-list"><%=wio%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxwio))%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgwio))%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align=center class="detail-data-body-list" style="height:29">%空闲（idle）</td>
                                                                                <td align=center class="detail-data-body-list"><%=idle%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxidle))%></td>
                                                                                <td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgidle))%></td>
                                                                            </tr>
                                                                            <% } %>
                                                                            
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
                                                </tr>
                                                
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
                <tr>
                  <td align=left valign=top>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% >
                                        <tr> 
                                            <td width="100%"  align=center> 
                                                <div id="flashcontent4_page">
                                                    <strong>You need to upgrade your Flash Player</strong>
                                                </div>
                                                <script type="text/javascript">
                                                    var so = new SWFObject("<%=rootPath%>/flex/Line.swf?ipadress=<%=ipaddress%>", "PageSpace", "346", "300", "8", "#ffffff");
                                                    so.write("flashcontent4_page");
                                                </script>               
                                            </td>
                                        </tr>  
                                           
                                                                    </table> 
                                                                            </td>
                                                                <td align=center valign=top>
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
            <tr>
                <td class="td-container-main">
                    <table id="container-main" class="container-main">
                        <tr>
                            <td class="td-container-main-add">
                                <table id="container-main-add" class="container-main-add">
                                    <tr>
                                        <td style="background-color:#FFFFFF;" >
                                            <table id="add-content" class="add-content">
                                                <tr>
                                                    <td>
                                                        <table id="add-content-header" class="add-content-header">
                                                            <tr>
                                                                <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                <td class="add-content-title"><b>换页率详情</b></td>
                                                                <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table id="detail-content-body" class="detail-content-body" border='1'>
                                                            <tr>
                                                                <td>
                                                                <table  cellpadding="1" cellspacing="1" width=48%>
                                        
                                                                <tr>
                                                                    <td valign=top>

                                                                    <table cellpadding="0" cellspacing="0" width=48% align=center>
                                                                    <tr>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curPageValueImageInfo %>">   
                                                                    </td>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curDayPageMaxValueImageInfo %>"> 
                                                                    </td>
                                                                    <td align=center>
                                                                        <img src="<%=rootPath%>/<%=curDayPageAvgValueImageInfo %>"> 
                                                                    </td>
                                                                    </tr> 
                                                                    <tr height=20>
                                                                        <td valign=top align=center><b>当前</b></td>
                                                                    <td valign=top align=center><b>最大</b></td>
                                                                    <td valign=top align=center><b>平均</b></td>
                                                                    </tr>
                                                                                                                                                   
                                                                    </table>

                                                                    </td>
                   
                                                                    </tr> 
                                                                  <tr>
                                                                    <td valign=top >
                                                                        <table cellpadding="0" cellspacing="0" width=48% align=center>
                                                                            <tr bgcolor="#FFFFFF">
                                                                                <td align="center" class="detail-data-body-list" style="height:29" >名称</td>
                                                                                <td align="center" class="detail-data-body-list" style="height:29">容量</td>
                                                                                <td align="center" class="detail-data-body-list" style="height:29">当前（%）</td>
                                                                                <td align="center" class="detail-data-body-list" style="height:29">最大（%）</td>
                                                                                <td align="center" class="detail-data-body-list" style="height:29">平均（%）</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="center" class="detail-data-body-list" height="29">换页率</td>
                                                                                <td align="center" class="detail-data-body-list">&nbsp;<%=totalPageing%></td>
                                                                                <td align="center" class="detail-data-body-list">&nbsp;<%=pageingUsed%></td>
                                                                                <td align="center" class="detail-data-body-list">&nbsp;<%=df.format(Float.parseFloat(curDayPageMaxValue))%></td>
                                                                                <td align="center" class="detail-data-body-list">&nbsp;<%=df.format(Float.parseFloat(curDayPageAvgValue))%></td>
                                                                            </tr>
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
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>                                     
    </td>           
                                                                            
  </tr>
   -->                                                                         
            
                                                    </table>
                                                </td>
                                              </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td class="td-container-main-tool" width="15%">
                                <jsp:include page="/include/toolbar.jsp">
                                    <jsp:param value="<%=ipaddress%>" name="ipaddress"/>
                                    <jsp:param value="<%=sysObjectID%>" name="sys_oid"/>
                                    <jsp:param value="<%=nodeid%>" name="tmp"/>
                                    <jsp:param value="host" name="category"/>
                                    <jsp:param value="hpunix" name="subtype"/>
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
