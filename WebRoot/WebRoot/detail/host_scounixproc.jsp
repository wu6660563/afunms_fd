<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%
String selectedProcess = (String)request.getAttribute("processName");
if(selectedProcess == null)
	selectedProcess = "";
String layer = (String)request.getAttribute("layer");
if(layer == null)
	layer="";
  	String menuTable = (String)request.getAttribute("menuTable");
String[] processItem={"Name","Count","Type","CpuTime","CpuUtilization","MemoryUtilization","Memory","Status"};
String[] processItemch={"进程名称","进程个数","进程类型","cpu时间","CPU占用率","内存占用率","内存占用量","当前状态"};
String[] second_processItem={"Pid","User","Name","Type","CpuTime","CpuUtilization","Memory","MemoryUtilization","Status"};
String[] second_processItemch={"进程ID","用户名","进程名称","进程类型","cpu时间","CPU占用率","内存占用量","内存占用率","当前状态"};
List list = (List)request.getAttribute("list");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = null;
	String sysuptime = null;
	String sysservices = null;
	String sysdescr = null;
	
	String hostname = "";
	String sysname = "";
	String CSDVersion = "";
	String mac = "";
    	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
    	String ipaddress = host.getIpAddress();
    	String orderflag = request.getParameter("orderflag");
    	if(orderflag == null || orderflag.trim().length()==0)
    		orderflag="index";
        String[] time = {"",""};
        DateE datemanager = new DateE();
        Calendar current = new GregorianCalendar();
        current.set(Calendar.MINUTE,59);
        current.set(Calendar.SECOND,59);
        time[1] = datemanager.getDateDetail(current);
        current.add(Calendar.HOUR_OF_DAY,-1);
        current.set(Calendar.MINUTE,0);
        current.set(Calendar.SECOND,0);
        time[0] = datemanager.getDateDetail(current);
        String starttime = time[0];
        String endtime = time[1];
        
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        
        Vector vector = new Vector();
        String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
        try{
        	vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        Nodeconfig nodeconfig = new Nodeconfig();
        String processornum = "";
	Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	if(ipAllData != null){
		nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
		if(nodeconfig != null){
			mac = nodeconfig.getMac();
			processornum = nodeconfig.getNumberOfProcessors();
			CSDVersion = nodeconfig.getCSDVersion();
			hostname = host.getAlias();//nodeconfig.getHostname();
		}
		
		Vector cpuV = (Vector)ipAllData.get("cpu");
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			cpuvalue = new Double(cpu.getThevalue());
		}
		//得到数据采集时间
		collecttime = (String)ipAllData.get("collecttime");
		//得到系统启动时间
		Vector systemV = (Vector)ipAllData.get("system");
		if(systemV != null && systemV.size()>0){
			for(int i=0;i<systemV.size();i++){
				Systemcollectdata systemdata=(Systemcollectdata)systemV.get(i);
				if(systemdata.getSubentity().equalsIgnoreCase("sysUpTime")){
					sysuptime = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("sysServices")){
					sysservices = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("sysDescr")){
					sysdescr = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
					sysname = systemdata.getThevalue();
				}
			}
		}
	}
		
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
		
		
	Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
	if(pingData != null && pingData.size()>0){
		Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
		Calendar tempCal = (Calendar)pingdata.getCollecttime();							
		Date cc = tempCal.getTime();
		collecttime = sdf1.format(cc);
	}
		
        request.setAttribute("vector", vector);
        request.setAttribute("id", tmp);
        request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));
	double avgpingcon = (Double)request.getAttribute("pingconavg");

	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	int cpuper = Double.valueOf(cpuvalue).intValue();
	
  	String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  	DefaultPieDataset dpd = new DefaultPieDataset();
  	dpd.setValue("可用率",avgpingcon);
  	dpd.setValue("不可用率",100 - avgpingcon);
  	chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
    chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
    chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
    SupperDao supperdao = new SupperDao();
   	Supper supper = null;
   	String suppername = "";
   	try{
   		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
   		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
   	}catch(Exception e){
   		e.printStackTrace();
   	}finally{
   		supperdao.close();
   	} 
    String zProcess = "";
    int zNumber = 0; 
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=可用&percent2="+data.percent2+"&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
				so.write("flashcontent00");
				var so1 = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent="+data.cpuper, "Pie_Component1", "160", "160", "8", "#ffffff");
				so1.write("flashcontent01");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
function doDetail(pro_name,layer)
		{
			mainForm.action='<%=rootPath%>/monitor.do?action=hostproc&&id=<%=tmp%>&&processName='+pro_name+"&&layer="+layer;
			mainForm.submit();
		}
</script>
<script language="javascript">	
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
	
});

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
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
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
  	location.href="<%=rootPath%>/detail/host_linuxutilhdx.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function reportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostping_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
function order(para,layer){
	mainForm.orderflag.value = para;
	mainForm.id.value = <%=tmp%>;
	//alert(mainForm.id.value);
	//mainForm.ipaddress.value = "<%=host.getIpAddress()%>";
	mainForm.action = "<%=rootPath%>/monitor.do?action=hostproc&id=<%=host.getId()%>&orderflag=" + para+"&&layer="+layer;
	mainForm.submit();
	//location.href="<%=rootPath%>/monitor.do?action=hostproc&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
}
function second_order(para,layer,processName){
	mainForm.orderflag.value = para;
	mainForm.id.value = <%=tmp%>;
	mainForm.action = "<%=rootPath%>/monitor.do?action=hostproc&id=<%=host.getId()%>&orderflag=" + para+"&&layer="+layer+"&&processName="+processName;
	mainForm.submit();
}
 
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

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
	        popMenu(itemMenu,100,"1111");
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
	    window.open ("<%=rootPath%>/monitor.do?action=show_hostutilhdx&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=host.getIpAddress()%>&ifindex="+node, "newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
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
	document.getElementById('scounixDetailTitle-2').className='detail-data-title';
	document.getElementById('scounixDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('scounixDetailTitle-2').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


</head>
<body id="body" class="body" onload="initmenu();">

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
	</table>
	</div>

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		
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
							<td class="td-container-main-detail">
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td>
											<table id="detail-content" class="detail-content">
												<tr>
													<td>
														<table id="detail-content-header" class="detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="detail-content-title">设备详细信息</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										
				        										
				        										<table>	
																			<tr>
                      											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;主机名:</td>
                      											<td width="70%"><%=hostname%> </td>
                    										</tr>                   										
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap   >&nbsp;状态:</td>
                      											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>">&nbsp;<%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;系统名称:</td>
                      											<td width="70%"><%=sysname%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap   >&nbsp;版本:</td>
                      											<td><%=CSDVersion%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;类别:</td>
                      											<td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left nowrap   class=txtGlobal>&nbsp;类型:</td>
                      											<td width="70%"><%=host.getType()%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;CPU个数:</td>
                      											<td width="70%"><%=processornum%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap   >&nbsp;设备启动时间:</td>
                      											<td><%=sysuptime%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=center nowrap class=txtGlobal>&nbsp;数据采集时间:</td>
                      											<td width="70%"><%=collecttime%></td>
                    										</tr>
                    										<tr  bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap   >&nbsp;IP地址:</td>
                      											<td><%=host.getIpAddress()%></td>
                    										</tr>
                    										<tr>
                      											<td height="29" class=txtGlobal valign=center nowrap>&nbsp;MAC地址:</td>
                      											<td><%=mac%></td>
                    										</tr>
                    										<tr  bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap  >&nbsp;系统OID:</td>
                      											<td><%=host.getSysOid()%></td>
                    										</tr>
                    										
                    										
												 <tr>
										       				<td height="29" class=txtGlobal valign=center nowrap>&nbsp;设备供应商:</td>
										       				<td>
										       				<% if(supper != null){
										       				%>
										       				<a href="#"  style="cursor:hand" onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
										       				<%
										       				}
										       				%>
										       				</td>
												 </tr>								
                									</table>
										</td>																					
                								<td width="200" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=0 algin="center">
                        								<tbody>
                          									<tr>
                											<td width="400" align="left" valign="middle" class=dashLeft>
                												<table width=80% align=center border=0 algin="center" >
                    													<tr class="topNameRight">
                      														<td height="30" align="center">
                      														<div id="flashcontent00">
																						<strong>You need to upgrade your Flash Player</strong>																							<strong>You need to upgrade your Flash Player</strong>
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																							so.write("flashcontent00");
																							</script>
                      														</td>
                    													</tr>
                    													<tr>
                    														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
                    													</tr>
                												</table>				
														<br>
														<table align=center border=0 algin="center">
                  													<tr>
                    														<!--<td height="20">
                    															<div align="center" class="txtResponseTime">
																	<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none width=90% align=center border=1 algin="center" height=95%>
                      																<tr>-->
                        																<td align="center" valign="middle" height='20' colspan=3>
																				<div id="flashcontent01">
																							<strong>You need to upgrade your Flash Player</strong>
																						</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent=<%=cpuper%>", "Pie_Component1", "160", "160", "8", "#ffffff");
																							so.write("flashcontent01");
																							</script>                       
                        																</td>
                      																<!--</tr>
                    															</table>                    
                    		    													</div>
                    		    												</td>-->
                  													</tr>
                  													<tr>
                  													<td align=center><img src="<%=rootPath%>/resource/image/Loading.gif">
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
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=scounixDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		  <tr>
                                                                <td align=center >
											    	
                  
                                              <table width="750" border="0" cellpadding="0" cellspacing="0">
            <tr bgcolor="#ECECEC" height=28>
              <%
		if(layer.equals("two"))
		{
		System.out.println("biaoti yes");
            for(int i=0;i<second_processItemch.length;i++)
            {
            	String title=second_processItemch[i];
            	if(i>=4 && i<=7)
            	{
        %>
																				<td align="center">
																					<input type="button"
																						onClick="second_order('<%=second_processItem[i]%>','two','<%=selectedProcess %>')"
																						Class="button" value="<%=second_processItemch[i]%>">
																				</td>
		<%
		  		}
            	else
            	{
         %>
																				<td align="center"><%=title%></td>
		 <%
		 		}
             }
         }
         else if(layer.equals("one") || layer.equals(""))
         {
         System.out.println("biaoti no");//detalHash wei kong
         	for(int i=0;i<processItemch.length;i++)
         	{
            	String title=processItemch[i];
            	if(i>=3 && i<=6)
            	{
         %>
              																	<td align="center">
																					<input type="button"
																						onClick="order('<%=processItem[i]%>','one')"
																						Class="button" value="<%=processItemch[i]%>">
																				</td>
              <%
                }
                else
                {
            %>
            																	<td align="center"><%=title%></td>
            <%
                }
            }
         }
             %>
																				<td align="center">
																					查看详细情况
																				</td>
																			</tr>

																			<%
																			if(layer.equals("") || layer.equals("one"))
																			{
																			System.out.println("neirong no");
																			Hashtable phash;
																			ProcessInfo processInfo = null;
			//Collection processhashes = newProcessHash.values();
			//Iterator it = processhashes.iterator();
            //while(it.hasNext()){
               //processInfo=(ProcessInfo)it.next();
               for(int i = 0; i < list.size(); i++)
               {
               	processInfo = (ProcessInfo)list.get(i);
               	if("僵尸进程".equals(processInfo.getStatus())){
	               	    zProcess = zProcess + processInfo.getName() + ";";
	               	    zNumber ++;
	               	}
            %>
																			<tr height="28" bgcolor="#FFFFFF"
																				<%=onmouseoverstyle%>>
																				
																				<td class="detail-data-body-list" align="center"><%=processInfo.getName()%></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getCount() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getType()%></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getCpuTime() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getCpuUtilization() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getMemoryUtilization() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getMemory() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getStatus() %></td>
																				<td class="detail-data-body-list" align="center">
																					<a href="javascript:doDetail('<%=processInfo.getName()%>','two')">查看详情</a>
																					<!-- <input type="button" Class="button"
																						onclick='window.open("<%=rootPath%>/monitor.do?action=look_prohis&ipaddress=<%=ipaddress%>&pid=<%=processInfo.getName()%>&pname=<%=processInfo.getName()%>","newWindow", "toolbar=no,height=300, width=830, top=300, left=200")'
																						value="查看详情"> -->
																				</td>
																			</tr>
																			<%
             phash = null;
            }
            }
            else if(layer.equals("two"))
            {
            System.out.println("neirong yes");
            	for(int i = 0;i<list.size();i++)
            	{
            		ProcessInfo processInfo = (ProcessInfo)list.get(i);
            		if("僵尸进程".equals(processInfo.getStatus())){
	               	    zProcess = zProcess + processInfo.getName() + ";";
	               	    zNumber ++;
	               	}
            %>
            		<tr height="28" bgcolor="#FFFFFF"
																				<%=onmouseoverstyle%>>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getPid() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getUSER() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getName() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getType() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getCpuTime() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getCpuUtilization() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getMemory() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getMemoryUtilization() %></td>
																				<td class="detail-data-body-list" align="center"><%=processInfo.getStatus() %></td>
																				<td class="detail-data-body-list" align="center">
																					<!-- <a href="javascript:doDetail('<%=processInfo.getName()%>')">查看详情</a> -->
																					<input type="button" Class="button"
																						onclick='window.open("<%=rootPath%>/monitor.do?action=look_prohis&ipaddress=<%=ipaddress%>&pid=<%=processInfo.getName()%>&pname=<%=processInfo.getName()%>","newWindow", "toolbar=no,height=300, width=830, top=300, left=200")'
																						value="查看详情"> 
																				</td>
																			</tr>
            <%
           		}
            }
             %>                
            </table>     
                               		 


														       	</td>
												      		</tr>
												      		<tr bgcolor="#ECECEC" height="28">
															    <td align=left>
															     僵死进程(<%=zNumber+"个"%>):<%=zProcess%>
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
								<jsp:include page="/include/aixtoolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="aix" name="subtype"/>
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