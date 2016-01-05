<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.detail.service.userInfo.UserInfoService"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.config.model.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String) request.getAttribute("menuTable");
	List cpuconfiglist = new ArrayList();
	Hashtable max = (Hashtable) request.getAttribute("max");
	String tmp = request.getParameter("id");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";

	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";

	HostNodeDao hostdao = new HostNodeDao();
	List hostlist = hostdao.loadHost();

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	String ipaddress = host.getIpAddress();
	String orderflag = request.getParameter("orderflag");
	if (orderflag == null || orderflag.trim().length() == 0)
		orderflag = "index";
	String[] time = { "", "" };
	DateE datemanager = new DateE();
	Calendar current = new GregorianCalendar();
	current.set(Calendar.MINUTE, 59);
	current.set(Calendar.SECOND, 59);
	time[1] = datemanager.getDateDetail(current);
	current.add(Calendar.HOUR_OF_DAY, -1);
	current.set(Calendar.MINUTE, 0);
	current.set(Calendar.SECOND, 0);
	time[0] = datemanager.getDateDetail(current);
	String starttime = time[0];
	String endtime = time[1];

	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

	Vector vector = new Vector();
	String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
			"ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };
	try {
		vector = hostlastmanager.getInterface_share(
				host.getIpAddress(), netInterfaceItem, orderflag,
				starttime, endtime);
	} catch (Exception e) {
		e.printStackTrace();
	}

	List networkconfiglist = new ArrayList();
	List networkstatuslist = new ArrayList();//networkstatus
	List diskconfig = new ArrayList(); //Ӳ������

	List cpulist = new ArrayList();
	Nodeconfig nodeconfig = new Nodeconfig();
	Vector memoryVector = new Vector();
	List userVector = new ArrayList();//�û�������Ϣ
	Hashtable memoryhash = new Hashtable();//�ڴ�������Ϣ
	String processornum = "";
	String TotalVisibleMemorySize = "";//�����ڴ��ܴ�С
	String TotalVirtualMemorySize = "";//�����ڴ��ܴ�С
	Vector cpuV = null;
	Vector systemV = null;
	Vector pingData = null;
	if("0".equals(runmodel)){
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
		if (ipAllData != null) {
			//���ڴ��л�ȡ����   
			cpuconfiglist = (List) ipAllData.get("cpuconfig");//cpu ������Ϣ
			networkconfiglist = (ArrayList) ipAllData.get("networkconfig");//����������Ϣ
			networkstatuslist = (ArrayList) ipAllData.get("networkstatus");//����״̬��Ϣ
			diskconfig = (ArrayList) ipAllData.get("physicaldisklist");//Ӳ����Ϣ
			memoryhash = (Hashtable) ipAllData.get("memoryconfig");//�ڴ�������Ϣ
			cpuV = (Vector) ipAllData.get("cpu");
			userVector = (List) ipAllData.get("user");//�û�
			//�õ�ϵͳ����ʱ��
			systemV = (Vector) ipAllData.get("system");
		}
		pingData = (Vector) ShareData.getPingdata().get(host.getIpAddress());
	}else{
		//�ɼ�������Ƿ���ģʽ
		NodeUtil nodeUtil = new NodeUtil();
	    NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
		OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
		cpuconfiglist = otherInfoService.getlistInfo("cpuconfig");
		networkconfiglist = otherInfoService.getlistInfo("networkconfig");
		networkstatuslist = otherInfoService.getlistInfo("networkstatus");
		diskconfig = otherInfoService.getlistInfo("physicaldisklist");
		memoryhash = otherInfoService.getHashInfo("memoryconfig"); 
		//�õ�cpu��Ϣ
	    CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
		cpuV = cpuInfoService.getCpuInfo(); 
		//�õ�ϵͳ����ʱ��
		SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		systemV = systemInfoService.getSystemInfo();
		//�û���Ϣ
		UserInfoService userInfoService = new UserInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		userVector = userInfoService.getUserInfoList();
		//�õ����ݲɼ�ʱ��
		pingData = new PingInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()).getPingInfo();
	} 
	if (memoryhash != null && memoryhash.size() > 0) {
		TotalVisibleMemorySize = (String) memoryhash.get("TotalVisibleMemorySize");
		TotalVirtualMemorySize = (String) memoryhash.get("TotalVirtualMemorySize");
	}
	if (cpuV != null && cpuV.size() > 0) {
		CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
		if(cpu != null && cpu.getThevalue() != null){
			cpuvalue = new Double(cpu.getThevalue());
		}
	}
	if (systemV != null && systemV.size() > 0) {
		for (int i = 0; i < systemV.size(); i++) {
			Systemcollectdata systemdata = (Systemcollectdata) systemV
					.get(i);
			if (systemdata.getSubentity().equalsIgnoreCase(
					"sysUpTime")) {
				sysuptime = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase(
					"sysServices")) {
				sysservices = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase(
					"sysDescr")) {
				sysdescr = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase(
					"SysName")) {
				sysname = systemdata.getThevalue();
			}
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());

	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";

	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager = new HostCollectDataManager();
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ConnectUtilization",
				starttime1, totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}

	if (pingData != null && pingData.size() > 0) {
		Pingcollectdata pingdata = (Pingcollectdata) pingData.get(0);
		Calendar tempCal = (Calendar) pingdata.getCollecttime();
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

	String[] sysItem = { "sysName", "sysUpTime", "sysContact",
			"sysLocation", "sysServices", "sysDescr" };
	String[] sysItemch = { "�豸��", "�豸����ʱ��", "�豸��ϵ", "�豸λ��", "�豸����",
			"�豸����" };

	Hashtable hash = (Hashtable) request.getAttribute("hash");
	Hashtable hash1 = (Hashtable) request.getAttribute("hash1");
	Hashtable imgurl = (Hashtable) request.getAttribute("imgurl");
	double avgpingcon = (Double) request.getAttribute("pingconavg");
	
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();
	Vector ipmacvector = (Vector) request.getAttribute("vector");
	if (ipmacvector == null){
		ipmacvector = new Vector();
	}

	//ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
	String chart1 = null, chart2 = null, chart3 = null, responseTime = null;
	DefaultPieDataset dpd = new DefaultPieDataset();
	dpd.setValue("������", avgpingcon);
	dpd.setValue("��������", 100 - avgpingcon);
	chart1 = ChartCreator.createPieChart(dpd, "", 130, 130);

	//if(item1.getSingleResult()!=-1)
	//{
	//responseTime = item1.getSingleResult() + " ms";

	//SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
	//if(item2!=null&&item2.getSingleResult()!=-1)
	//chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
	chart2 = ChartCreator.createMeterChart(cpuvalue, "", 120, 120);

	//SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
	//if(item3!=null&&item3.getSingleResult()!=-1)
	chart3 = ChartCreator.createMeterChart(40.0, "", 120, 120);
	//}
	//else
	//responseTime = "����Ӧ"; 

	Vector ifvector = (Vector) request.getAttribute("vector");
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />


		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
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
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=����&percent2="+data.percent2+"&percentStr2=������", "Pie_Component", "160", "160", "8", "#ffffff");
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
     	alert("�������ѯ����");
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
  
  function query()
  {  
  	mainForm.id.value = <%=tmp%>;
     mainForm.action = "<%=rootPath%>/monitor.do?action=hostevent";
     mainForm.submit();
  }
  
// ȫ���ۿ�
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

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
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
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
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
//�޸Ĳ˵������¼�ͷ����
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

//��Ӳ˵�	
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
	document.getElementById('windowsDetailTitle-5').className='detail-data-title';
	document.getElementById('windowsDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('windowsDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

//�����豸��ip��ַ
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("�޸ĳɹ���");
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


	</head>
	<body id="body" class="body" onload="initmenu();">

		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.detail()">
						�鿴״̬
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.portset();">
						�˿�����
					</td>
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
											<td width=85%>
												<table id="detail-content" class="detail-content">
													<tr>
														<td>
															<jsp:include page="/topology/includejsp/systeminfo_hostwmi.jsp">
																 <jsp:param name="rootPath" value="<%= rootPath %>"/>
																 <jsp:param name="tmp" value="<%= tmp %>"/> 
																 <jsp:param name="collecttime" value="<%= collecttime %>"/> 
																 <jsp:param name="sysuptime" value="<%= sysuptime %>"/> 
																 <jsp:param name="sysdescr" value="<%= sysdescr %>"/> 
																 <jsp:param name="percent1" value="<%= percent1 %>"/> 
																 <jsp:param name="percent2" value="<%= percent2 %>"/> 
																 <jsp:param name="cpuper" value="<%= cpuper %>"/>  
															 </jsp:include>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-data" class="detail-data">
																<tr>
																	<td class="detail-data-header">
																		<%=windowsDetailTitleTable%>
																	</td>
																</tr>
																<tr>
																	<td>
																		<table class="detail-data-body">
																			<tr>
																				<td align=center>
																					<table width="100%" border="0" cellpadding="0"
																						cellspacing="1">
																						<tr>
																							<td align=center>
																								<table  border="0" cellpadding="0"
																									cellspacing="1" bgcolor="#FFFFFF">
																									<tr>
																										<td height="28" align="left" bgcolor="#ECECEC"
																											colspan=5>
																											&nbsp;&nbsp;
																											<b>CPU��Ϣ</b>
																										</td>
																									</tr>
																									<tr align="center" bgcolor="#ECECEC">
																										<td width="10%" align="left"
																											class="detail-data-body-title">
																											����λ
																										</td>
																										<td width="20%" align="left"
																											class="detail-data-body-title">
																											������ID
																										</td>
																										<td width="40%" align="left"
																											class="detail-data-body-title">
																											����
																										</td>
																										<td width="15%" align="left"
																											class="detail-data-body-title">
																											���������С
																										</td>
																										<td width="15%" align="left"
																											class="detail-data-body-title">
																											��Ƶ
																										</td>
																									</tr>

																									<%
																										if (cpuconfiglist != null && cpuconfiglist.size() > 0) {
																											for (int i = 0; i < cpuconfiglist.size(); i++) {
																												Hashtable cpuconfig = (Hashtable) cpuconfiglist.get(i);
																									%>
																									<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																										<td class="detail-data-body-list"
																											align="center">
																											&nbsp;<%=(String) cpuconfig.get("DataWidth")%></td>
																										<td class="detail-data-body-list"
																											align="center"><%=(String) cpuconfig.get("ProcessorId")%></td>
																										<td class="detail-data-body-list"
																											align="center"><%=(String) cpuconfig.get("Name")%></td>
																										<td class="detail-data-body-list"
																											align="center"><%=(String) cpuconfig.get("L2CacheSize")+ "kb"%></td>
																										<td class="detail-data-body-list"
																											align="center"><%=(String) cpuconfig.get("L2CacheSpeed")+ " HZ"%></td>
																									</tr>
																									<%
																										}
																										}
																									%>
																								</table>
																							</td>
																						</tr>
																					</table>

																					<center>
																						<table border="0" cellpadding="0"
																							cellspacing="1" bgcolor="#FFFFFF">
																							<tr>
																								<td height="28" align="left" bgcolor="#ECECEC"
																									colspan=2>
																									&nbsp;&nbsp;
																									<b>�ڴ���Ϣ</b>
																								</td>
																							</tr>
																							<tr align="center" bgcolor="#ECECEC">
																								<td width="50%" align="center"
																									class="detail-data-body-title">
																									����
																								</td>
																								<td width="50%" align="center"
																									class="detail-data-body-title">
																									��С
																								</td>
																							</tr>

																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list" align="center">
																									�����ڴ�
																								</td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=TotalVisibleMemorySize%>M</td>
																							</tr>
																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list" align="center">
																									�����ڴ�
																								</td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=TotalVirtualMemorySize%>M</td>
																							</tr>
																						</table>


																						<table  border="0" cellpadding="0"
																							cellspacing="1" bgcolor="#FFFFFF">
																							<tr>
																								<td height="28" align="left" bgcolor="#ECECEC"
																									colspan="5">
																									&nbsp;&nbsp;
																									<b>�û���Ϣ</b>
																								</td>
																							</tr>
																							<tr align="center" bgcolor="#ECECEC">
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									����
																								</td>
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									����
																								</td>
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									������
																								</td>
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									״̬
																								</td>
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									�����û�
																								</td>
																							</tr>

																							<%
																								if (userVector != null && userVector.size() > 0) {
																									for (int i = 0; i < userVector.size(); i++) {
																										Hashtable user = (Hashtable) userVector.get(i);
																							%>
																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=user.get("Name")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=user.get("Description")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=user.get("Domain")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=user.get("Status")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=user.get("LocalAccount")%></td>

																							</tr>
																							<%
																								}
																								}
																							%>
																						</table>





																						<table border="0" cellpadding="0"
																							cellspacing="1" bgcolor="#FFFFFF">
																							<tr>
																								<td height="28" align="left" bgcolor="#ECECEC"
																									colspan="7">
																									&nbsp;&nbsp;
																									<b>������Ϣ</b>
																								</td>
																							</tr>
																							<tr align="center" bgcolor="#ECECEC">
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									����
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									IP
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									��������
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									����
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									DNS
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									MAC
																								</td>
																								<td width="5%" align="center"
																									class="detail-data-body-title">
																									����״̬
																								</td>
																							</tr>

																							<%
																								if (null != networkconfiglist && null != networkstatuslist) {

																									for (int i = 0; i < networkconfiglist.size(); i++) {
																										Hashtable ht = (Hashtable) networkconfiglist.get(i);
																										String url = "";
																										for (int b = 0; b < networkstatuslist.size(); b++) {

																											Hashtable hb = (Hashtable) networkstatuslist.get(b);

																											if (hb.get("Index").equals(ht.get("Index"))) {

																												if (hb.get("NetConnectionStatus").equals("2")) {
																													url = rootPath + "/resource/image/topo/up.gif";
																												} else if (hb.get("NetConnectionStatus")
																														.equals("7")) {
																													url = rootPath
																															+ "/resource/image/topo/down.gif";
																												} else {
																													url = rootPath
																															+ "/resource/image/topo/testing.gif";
																												}

																												break;
																											}
																										}
																							%>

																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list" align="center"><%=ht.get("Description")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("IPAddress")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("IPSubnet")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("DefaultIPGateway")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("DNSServerSearchOrder")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("MACAddress")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;
																									<img src="<%=url%>">
																								</td>

																							</tr>

																							<%
																								}

																								}
																							%>
																						</table>


																						<table  border="0" cellpadding="0"
																							cellspacing="1" bgcolor="#FFFFFF">
																							<tr>
																								<td height="28" align="left" bgcolor="#ECECEC"
																									colspan="5">
																									&nbsp;&nbsp;
																									<b>Ӳ����Ϣ</b>
																								</td>
																							</tr>
																							<tr align="center" bgcolor="#ECECEC">
																								<td width="20%" align="center"
																									class="detail-data-body-title">
																									ID
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									������
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									�ӿ�����
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									��������
																								</td>
																								<td width="10%" align="center"
																									class="detail-data-body-title">
																									Ӳ�̴�С
																								</td>

																							</tr>

																							<%
																								if (null != diskconfig) {

																									for (int i = 0; i < diskconfig.size(); i++) {
																										Hashtable ht = (Hashtable) diskconfig.get(i);
																							%>

																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list" align="center"><%=ht.get("Index")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("Caption")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("InterfaceType")%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("BytesPerSector") + "kb"%></td>
																								<td class="detail-data-body-list" align="center">
																									&nbsp;<%=ht.get("Size")%></td>

																							</tr>

																							<%
																								}

																								}
																							%>
																						</table>






																						<!--         �м�������Ϣ  end       -->
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
											<td width="15%" class="td-container-main-tool">
												<jsp:include page="/include/toolbar.jsp">
													<jsp:param value="<%=host.getIpAddress()%>"
														name="ipaddress" />
													<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
													<jsp:param value="<%=tmp%>" name="tmp" />
													<jsp:param value="host" name="category" />
													<jsp:param value="windows" name="subtype"/>
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