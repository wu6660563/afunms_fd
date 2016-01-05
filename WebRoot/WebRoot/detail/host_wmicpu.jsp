<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*" %>
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
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%

	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "0";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String responsevalue = "0";
	String pingvalue = "0";	
	String maxpingvalue="0";
	Vector deviceV = new Vector();
	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "����", "��ǰ", "���", "ƽ��" };
	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");
	Hashtable diskhash = new Hashtable();
	Hashtable memhash = new Hashtable();
	
	String avgcpu = "";
	String cpumax = "";
	
	//cpu������
	Hashtable max = (Hashtable) request.getAttribute("max");
	if (max != null) {
		avgcpu = (String) max.get("cpuavg");
		cpumax = (String) max.get("cpu");
	}
	if (avgcpu.equals("") || avgcpu == null)
		avgcpu = "0";
	if (cpumax.equals("") || cpumax == null)
		cpumax = "0";
  	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
  	String ipaddress = host.getIpAddress();
  	
  	String picip = CommonUtil.doip(ipaddress);
  	String orderflag = request.getParameter("orderflag");
  	if(orderflag == null || orderflag.trim().length()==0){
  		orderflag="index";
  	}
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

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
    Vector cpuV = null;
    Vector systemV = null;
    //kongllq ���
	List alldiskperf=null;
    Vector pingData  = null;
    if("0".equals(runmodel)){
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			cpuV = (Vector)ipAllData.get("cpu");
			systemV = (Vector)ipAllData.get("system");
			alldiskperf = (List)ipAllData.get("diskperforlist");
			deviceV = (Vector) ipAllData.get("device");
		}
		pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		memhash = (Hashtable) request.getAttribute("memhash");
	   diskhash = (Hashtable) request.getAttribute("diskhash");
	}else{
       	//�ɼ�������Ƿ���ģʽ
       	NodeUtil nodeUtil = new NodeUtil();
       	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
       	CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
		cpuV = cpuInfoService.getCpuInfo(); 
		//�õ�ϵͳ����ʱ��
		SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		systemV = systemInfoService.getSystemInfo();
		//������Ϣ
		DiskPerfInfoService diskPerfInfoService = new DiskPerfInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		alldiskperf = diskPerfInfoService.getDiskperflistInfo();
		//�õ����ݲɼ�ʱ��
		pingData = new PingInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()).getPingInfo();
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		try {
			memhash = hostlastmanager.getMemory(host.getIpAddress(),
					"Memory", starttime, endtime);
			diskhash = hostlastmanager.getDisk(host.getIpAddress(),
					"Disk", starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	if(cpuV != null && cpuV.size()>0){
		CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
		if(cpu != null && cpu.getThevalue() != null){
			cpuvalue = new Double(cpu.getThevalue());
		}
	}
	//�õ�ϵͳ����ʱ��
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
		}
	}
	

	
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
		maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
		maxpingvalue = maxpingvalue.replaceAll("%", "");
	}
	//----�õ���Ӧʱ�� 
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	
	String avgresponse = "0";//-----------
	String maxresponse = "0";//----------- 

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("����", "").replaceAll("%", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		maxresponse = maxresponse.replaceAll("%", "");
	}
	
	if(pingData != null && pingData.size()>0){
		Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
		Calendar tempCal = (Calendar)pingdata.getCollecttime();							
		Date cc = tempCal.getTime();
		collecttime = sdf1.format(cc);
		pingvalue = pingdata.getThevalue();
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();
	}
   	request.setAttribute("id", tmp);
   	request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));
	String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
	String[] sysItemch={"�豸��","�豸����ʱ��","�豸��ϵ","�豸λ��","�豸����","�豸����"};
	Hashtable hash = (Hashtable)request.getAttribute("hash");
	Hashtable hash1 = (Hashtable)request.getAttribute("hash1");

	double avgpingcon = (Double)request.getAttribute("pingconavg");

	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

  	String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  	DefaultPieDataset dpd = new DefaultPieDataset();
  	dpd.setValue("������",avgpingcon);
  	dpd.setValue("��������",100 - avgpingcon);
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
   	
//   	CreateBarPic disk_cbp = new CreateBarPic();
//  	double[] disk_data1 = new double[diskhash.size()];
//	double[] disk_data2 = new double[diskhash.size()];
//	String[] disk_labels = new String[diskhash.size()];

//	for (int k = 0; k < diskhash.size(); k++) {
//		Hashtable dhash = (Hashtable) (diskhash.get(new Integer(k)));

//		String name = "";
//		if (dhash.get("name") != null) {
	//		name = (String) dhash.get("name");
			// System.out.println("====="+name);
//		}
//		for (int j = 0; j < diskItem.length; j++) {
//			String value = "";
//			if (dhash.get(diskItem[j]) != null) {
//
//				value = (String) dhash.get(diskItem[j]);
//				disk_data1[k] = new Double(value.replaceAll("G", "")
//						.replaceAll("%", "").replaceAll("M", ""));
//				disk_data2[k] = 100 - disk_data1[k];
//				disk_labels[k] = name;
//
//			}
//
//		}
//	}

//	TitleModel disk_tm = new TitleModel();
//	disk_tm.setPicName(picip + "disk");//
//	disk_tm.setBgcolor(0x000000);
//	disk_tm.setXpic(450);
//	disk_tm.setYpic(170);
//	disk_tm.setX1(50);
//	disk_tm.setX2(20);
//	disk_tm.setX3(360);
//	disk_tm.setX4(100);
//	disk_tm.setX5(160);
//	disk_tm.setX6(140);
//	int disk_color1 = 0x80ff80;
//	int disk_color2 = 0x8080ff;
//	disk_cbp.createCylindricalPic(disk_data1, disk_data2, disk_labels,
//			disk_tm, "��ʹ��", "δʹ��", disk_color1, disk_color2);
   	
   	//cpu�Ǳ���
   	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper); //����CPU��ǰ�Ǳ���
	cmp.createMaxCpuPic(picip, cpumax); //����CPU���ֵ�Ǳ���
	cmp.createAvgCpuPic(picip, avgcpu); //����CPUƽ��ֵ�Ǳ���
	
   	//��Ӧʱ����״ͼ
   	CreateBarPic cbp = new CreateBarPic();
	TitleModel tm = new TitleModel();

	cbp = new CreateBarPic();

	double[] r_data1 = { new Double(responsevalue),
			new Double(maxresponse), new Double(avgresponse) };
	String[] r_labels = { "��ǰ��Ӧʱ��(ms)", "�����Ӧʱ��(ms)", "ƽ����Ӧʱ��(ms)" };
	tm = new TitleModel();
	tm.setPicName(picip + "response");//
	tm.setBgcolor(0xffffff);
	tm.setXpic(450);//ͼƬ����
	tm.setYpic(180);//ͼƬ�߶�
	tm.setX1(30);//�������
	tm.setX2(20);//�������
	tm.setX3(400);//��ͼ���
	tm.setX4(130);//��ͼ�߶�
	tm.setX5(10);
	tm.setX6(115);
	cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
	tm.setPicName(picip + "response1");//
	cbp.createRoundTimeBarPic(r_data1, r_labels, tm, 40);
	
   	//amchar ��ͨ��
Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("��ͨ;").append(Math.round(realValue)).append(";false;7CFC00\\n");
	dataStr1.append("δ��ͨ;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
	String realdata = dataStr1.toString();
			StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
	dataStr2.append("δ��ͨ;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
			StringBuffer dataStr3 = new StringBuffer();
			String maxping=maxpingvalue.replaceAll("%", "");
	dataStr3.append("��ͨ;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
	dataStr3.append("δ��ͨ;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
	String maxdata = dataStr3.toString();
	
	//���̵�������	
	 CreateAmColumnPic amColumnPic=new CreateAmColumnPic();
	 String dataStr=amColumnPic.createWinDiskChart(diskhash);
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function cpuReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostcpu_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
  
  
  function memoryReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostmemory_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
   
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
	document.getElementById('windowsDetailTitle-1').className='detail-data-title';
	document.getElementById('windowsDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('windowsDetailTitle-1').onmouseout="this.className='detail-data-title'";
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
<%


 %>
<body id="body" class="body" onload="initmenu();">
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
							<td class="td-container-main-detail" width=85%>
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td>
											<jsp:include page="/topology/includejsp/systeminfo_hostwmi.jsp">
												 <jsp:param name="rootPath" value="<%=rootPath%>"/>
												 <jsp:param name="tmp" value="<%=tmp%>"/> 
												 <jsp:param name="collecttime" value="<%=collecttime%>"/> 
												 <jsp:param name="sysuptime" value="<%=sysuptime%>"/> 
												 <jsp:param name="sysdescr" value="<%=sysdescr%>"/> 
												 <jsp:param name="percent1" value="<%=percent1%>"/> 
												 <jsp:param name="percent2" value="<%=percent2%>"/> 
												 <jsp:param name="cpuper" value="<%=cpuper%>"/>  
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
                    <td align=center valign=top width=40%>
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent3">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "250", "8", "#ffffff");
													so.write("flashcontent3");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  <td align=left valign=top width=58%>

																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td>
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>��ͨ��ʵʱ</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body">
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
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
						                                                                                                                           so.addVariable("chart_data","<%=realdata%>");
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
						                                                                                                                           so.addVariable("chart_data","<%=maxdata%>");
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
						                                                                                                                           so.addVariable("chart_data","<%=avgdata%>");
						                                                                                                                           so.write("avgping");
						                                                                                                                            </script>
																																						</td>
																																					</tr>
																																					<tr height=40>
																																						<td  align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td  align=center>
																																							<b>��С</b>
																																						</td>
																																						<td  align=center>
																																							<b>ƽ��</b>
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
																														<table id="detail-content-footer"
																															class="detail-content-footer">
																															<tr>
																																<td>
																																	<table width="100%" border="0"
																																		cellspacing="0" cellpadding="0">
																																		<tr>
																																			<td align="left" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_01.jpg"
																																					width="5" height="12" />
																																			</td>
																																			<td></td>
																																			<td align="right" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_03.jpg"
																																					width="5" height="12" />
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
                  <td align=center valign=top width=40%>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48%>
              							<tr> 
                							<td width="100%"  align=center> 
                							<div id="flashcontent5">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "260", "8", "#ffffff");
													so.write("flashcontent5");
												</script>
                												
							                </td>
										</tr>             
									</table> 
                      </td>
                      <td align=center width=58%>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td>
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>��Ӧʱ������</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body">
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center colspan=3>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">
																																						</td>
																																					</tr>
																																					<tr height=30>
																																						<td valign=top align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>���</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>ƽ��</b>
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
																														<table id="detail-content-footer"
																															class="detail-content-footer">
																															<tr>
																																<td>
																																	<table width="100%" border="0"
																																		cellspacing="0" cellpadding="0">
																																		<tr>
																																			<td align="left" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_01.jpg"
																																					width="5" height="12" />
																																			</td>
																																			<td></td>
																																			<td align="right" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_03.jpg"
																																					width="5" height="12" />
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
													var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "346", "260", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  <td align=center width=58%>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td>
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>CPU����������</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body">
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>
																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpumax.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpuavg.png">
																																						</td>
																																					</tr>
																																					<tr height=30>
																																						<td valign=top align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>���</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>ƽ��</b>
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


																																	<table cellpadding="1" cellspacing="1"
																																		width=80%>

																																		<tr>
																																			<td valign=top width=80%>
																																				<table cellpadding="0"
																																					cellspacing="0" width=80%
																																					align=center>
																																					<%
																																					if(deviceV!=null){
																																						for (int m = 0; m < deviceV.size(); m++) {
																																								Devicecollectdata devicedata = (Devicecollectdata) deviceV
																																										.get(m);
																																								String name = devicedata.getName();
																																								String type = devicedata.getType();
																																								if (!"CPU".equals(type))
																																									continue;
																																								String status = devicedata.getStatus();
																																					%>
																																					<tr height=15>
																																						<td valign=top align=center>
																																							<b>����</b>��<%=type%></td>
																																						<td valign=top align=center>
																																							<b>����</b>��<%=name%></td>
																																						<td valign=top align=center>
																																							<b>״̬</b>��<%=status%></td>
																																					</tr>
																																					<%
																																						}
																																						}
																																					%>
																																				</table>

																																			</td>

																																		</tr>
																																		<tr height=20>
																																			<td valign=center align=right
																																				colspan=3>
																																				>>
																																				<a
																																					href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=cpudetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','�ڴ�ʵʱ����','top=200,left=300,height=350 ,width=720, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">ʵ
																																					ʱ</a> &nbsp;&nbsp;

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
																														<table id="detail-content-footer"
																															class="detail-content-footer">
																															<tr>
																																<td>
																																	<table width="100%" border="0"
																																		cellspacing="0" cellpadding="0">
																																		<tr>
																																			<td align="left" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_01.jpg"
																																					width="5" height="12" />
																																			</td>
																																			<td></td>
																																			<td align="right" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_03.jpg"
																																					width="5" height="12" />
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
                							<td width="100%"  align=center> 
                								<div id="flashcontent2">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "346", "290", "8", "#ffffff");
													so.write("flashcontent2");
												</script>				
							                </td>
										</tr>             
									</table> 
                      </td>
                      <%
                                                                                            String pmem = "";
					                                                                        String vmem = "";
  					                                                                        String pcurmem = "";
  					                                                                        String pmaxmem = "";
  					                                                                        String pavgmem = "";
  					                                                                        String vcurmem = "";
  					                                                                        String vmaxmem = "";
  					                                                                        String vavgmem = "";
  					                                                                       for (int k = 0; k < memhash.size(); k++) {
					                                                                            Hashtable mhash = (Hashtable) (memhash.get(new Integer(k)));
					                                                                            String name = (String) mhash.get("name");
  					                                                                           for (int j = 0; j < memoryItem.length; j++) {
																											String value = "";
																											if (mhash.get(memoryItem[j]) != null) {
																												value = (String) mhash.get(memoryItem[j]);
																												if (j == 0) {
																													if ("PhysicalMemory".equals(name))
																														pmem = value;
																													if ("VirtualMemory".equals(name))
																														vmem = value;
																												} else {
																													if ("PhysicalMemory".equals(name))
																														pcurmem = value;
																													if ("VirtualMemory".equals(name))
																														vcurmem = value;
																												}
																											}
																											}
																										String value = "";
																										if (memmaxhash.get(name) != null) {
																											value = (String) memmaxhash.get(name);
																											if ("PhysicalMemory".equals(name))
																												pmaxmem = value;
																											if ("VirtualMemory".equals(name))
																												vmaxmem = value;
																										}
																										String avgvalue = "";
																										if (memavghash.get(name) != null) {
																											avgvalue = (String) memavghash.get(name);
																											if ("PhysicalMemory".equals(name))
																												pavgmem = avgvalue;
																											if ("VirtualMemory".equals(name))
																												vavgmem = avgvalue;
																										}
																										}
                                                                                                                                             if (pcurmem == null || pcurmem.equals(""))
																																							pcurmem = "0";
																																						if (pmaxmem == null || pmaxmem.equals(""))
																																							pmaxmem = "0";
																																						if (pavgmem == null || pavgmem.equals(""))
																																							pavgmem = "0";
																																						if (vcurmem == null || vcurmem.equals(""))
																																							vcurmem = "0";
																																						if (vmaxmem == null || vmaxmem.equals(""))
																																							vmaxmem = "0";
																																						if (vavgmem == null || vavgmem.equals(""))
																																							vavgmem = "0";
																																						pcurmem = pcurmem.replaceAll("%", "");
																																						pmaxmem = pmaxmem.replaceAll("%", "");
																																						pavgmem = pavgmem.replaceAll("%", "");
																																						vcurmem = vcurmem.replaceAll("%", "");
																																						vmaxmem = vmaxmem.replaceAll("%", "");
																																						vavgmem = vavgmem.replaceAll("%", "");

																																						double dpcurmem = new Double(pcurmem);
																																						double dpmaxmem = new Double(pmaxmem);
																																						double dpavgmem = new Double(pavgmem);

																																						double dvcurmem = new Double(vcurmem);
																																						double dvmaxmem = new Double(vmaxmem);
																																						double dvavgmem = new Double(vavgmem);	
																																						double[] d_data1 = { dpcurmem, 100 - dpcurmem };
																																						double[] d_data2 = { dvcurmem, 100 - dvcurmem };
																																						double[] dmax_data1 = { dpmaxmem, 100 - dpmaxmem };
																																						double[] dmax_data2 = { dvmaxmem, 100 - dvmaxmem };
																																						double[] davg_data1 = { dpavgmem, 100 - dpavgmem };
																																						double[] davg_data2 = { dvavgmem, 100 - dvavgmem };
																																						StringBuffer xmlStr = new StringBuffer();
																																						xmlStr.append("<?xml version='1.0' encoding='gb2312'?>");
																																						xmlStr.append("<chart><series>");
																																						String[] titleStr = new String[] { "��ǰ����", "��ǰ����", "ƽ������",
																																								"ƽ������", "�������", "�������" };
																																						String[] title = new String[] { "��ǰ����", "��ǰδ��", "ƽ������", "ƽ��δ��",
																																								"�������", "���δ��" };

																																						for (int i = 0; i < 6; i++) {
																																							xmlStr.append("<value xid='").append(i).append("'>")
																																									.append(titleStr[i]).append("</value>");

																																						}
																																						xmlStr.append("</series><graphs>");
																																						long curp = Math.round(d_data1[0]);
																																						long curv = Math.round(d_data2[0]);
																																						long maxp = Math.round(dmax_data1[0]);
																																						long maxv = Math.round(dmax_data2[0]);
																																						long avgp = Math.round(davg_data1[0]);
																																						long avgv = Math.round(davg_data2[0]);

																																						long[] data = { curp, curv, 100 - curp, 100 - curv, avgp, avgv,
																																								100 - avgp, 100 - avgv, maxp, maxv, 100 - maxp,
																																								100 - maxv };
																																						int tempInt = 0, tempId = 0;
																																						for (int i = 0; i < 6; i++) {
																																							if (i == 1)
																																								tempId = 0;
																																							if (i == 3)
																																								tempId = 2;
																																							if (i == 5)
																																								tempId = 4;
																																							xmlStr.append("<graph gid='").append(i).append("' title='")
																																									.append(title[i]).append("'>").append(
																																											"<value xid='" + tempId + "'> "
																																													+ data[tempInt]).append("</value>");
																																							xmlStr.append("<value xid='" + (++tempId) + "'>"
																																									+ data[++tempInt] + "</value>");
																																							xmlStr.append("</graph>");
																																							tempId++;
																																							tempInt++;
																																						}

																																						xmlStr.append("</graphs></chart>");
																																						
																																													
                       %>
                       <td align=center width=58%>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td>
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>�豸�ڴ���ϸ</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body">
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center colspan=3>
																																								<div id="flashcontent">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
				
		                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                        so.addVariable("path", "<%=rootPath%>/amchart/");
		                        so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
	                        //	so.addVariable("data_file",  escape("<%=rootPath%>/amcharts_data/memorypercent_data.xml"));
	                            so.addVariable("chart_data", "<%=xmlStr.toString()%>");
		                        so.addVariable("preloader_color", "#999999");
		                        so.write("flashcontent");
	                             </script>
	                             </td>
																																					</tr>
																																				<tr height=20>
																																						<td valign=center align=center
																																							colspan=1>

																																							<table cellpadding="0"
																																								cellspacing="0" width=48%
																																								align=center>
																																								<tr height=20>
																																									<td valign=top align=center>
																																										<b>�����ڴ�����</b>��<%=pmem%></td>
																																									<td valign=top align=center>
																																										<b>�����ڴ�����</b>��<%=vmem%></td>
																																								</tr>
																																							</table>

																																						</td>
																																					</tr>
																																					<tr height=15>
																																						<td valign=center align=right
																																							colspan=3>
																																							>>
																																							<a
																																								href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=memorydetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','�ڴ�ʵʱ����','top=200,left=300,height=350 ,width=720, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">ʵ
																																								ʱ</a> &nbsp;&nbsp;

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
																														<table id="detail-content-footer"
																															class="detail-content-footer">
																															<tr>
																																<td>
																																	<table width="100%" border="0"
																																		cellspacing="0" cellpadding="0">
																																		<tr>
																																			<td align="left" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_01.jpg"
																																					width="5" height="12" />
																																			</td>
																																			<td></td>
																																			<td align="right" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_03.jpg"
																																					width="5" height="12" />
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
            <!--  
            <td align=center>
            <br>
                <table cellpadding="0" cellspacing="0" width=48%>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent4">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_Disk.swf?ipadress=<%=host.getIpAddress()%>", "Column_Disk", "346", "250", "8", "#ffffff");
													so.write("flashcontent4");
												</script>				
							                </td>
										</tr>             
									</table> 
            </td>
            -->
            <td align=center valign=top>
            <br>
            <table cellpadding="0" cellspacing="0" width=48%>
								              							<tr> 
								                							<td width="100%"  align=center> 
								                								<div id="flashcontent6">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Disk.swf?ipadress=<%=host.getIpAddress()%>", "Area_Disk", "346", "360", "8", "#ffffff");
																					so.write("flashcontent6");
																				</script>				
															                </td>
																		</tr>             
																	</table> 
            </td>
            <td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td>
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>��������������</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body">
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>
																																		<tr>
																																			<td valign=top>
																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center colspan=1>
																																						<!-- <img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>disk.png">-->
																																						<div id="wmiDiskUtil">
																																								
																																							</div>

																																							<script type="text/javascript">
																																							  <% if(!dataStr.equals("0")){%>
		                                                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                                                                                                                                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                                                                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
	                                                                                                                                                                   so.addVariable("chart_data", "<%=dataStr%>");
		                                                                                                                                                               so.addVariable("preloader_color", "#999999");
		                                                                                                                                                               so.write("wmiDiskUtil");
		                                                                                                                                                               <%}else{%>
				                                                                                                                                                     var _div=document.getElementById("wmiDiskUtil");
												                                                                                                                     var img=document.createElement("img");
													                                                                                                                     img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													                                                                                                                     _div.appendChild(img);
				                                                                                                                                                          <%}%>
	                                                                                                                                                        </script>		
																																						</td>
																																					</tr>


																																					<tr>
																																						<td>
																																							<table cellpadding="0"
																																								cellspacing="0" width=30%
																																								align=center>
																																								<tr>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										������
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										������
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										��������
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										������
																																									</td>

																																								</tr>
																																								<%
																																									if (diskhash != null && diskhash.size() > 0) {
																																											// д����
																																											for (int i = 0; i < diskhash.size(); i++) {
																																								%>
																																								<tr>
																																									<%
																																										Hashtable diskhash1 = (Hashtable) (diskhash
																																															.get(new Integer(i)));
																																													String name = (String) diskhash1.get("name");
																																									%>
																																									<td
																																										class="detail-data-body-list"
																																										align=center
																																										height="29;width=10%"><%=name%></td>
																																									<%
																																										for (int j = 0; j < diskItem.length; j++) {
																																														String value = "";
																																														if (diskhash1.get(diskItem[j]) != null) {
																																															value = (String) diskhash1.get(diskItem[j]);
																																														}
																																									%>
																																									<td
																																										class="detail-data-body-list"
																																										align=center
																																										height="29;width=10%"><%=value%></td>
																																									<%
																																										}
																																									%>
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
																												<tr>
																													<td>
																														<table id="detail-content-footer"
																															class="detail-content-footer">
																															<tr>
																																<td>
																																	<table width="100%" border="0"
																																		cellspacing="0" cellpadding="0">
																																		<tr>
																																			<td align="left" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_01.jpg"
																																					width="5" height="12" />
																																			</td>
																																			<td></td>
																																			<td align="right" valign="bottom">
																																				<img
																																					src="<%=rootPath%>/common/images/right_b_03.jpg"
																																					width="5" height="12" />
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
					
					
					<%


 %>						    		
											    		
											
							 <table width="80%" border="0" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
                      		<tr>
					<td height="28" align="left" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;<b>���̶�ȡ����</b></td>
				</tr>
  <tr align="center" bgcolor="#ECECEC"> 
    <td  width="5%" align="center" class="detail-data-body-title">����</td>
    <td  width="10%" align="center" class="detail-data-body-title">ƽ��ÿ���д����</td>
    <td  width="10%" align="center" class="detail-data-body-title">ƽ��ÿ�������</td>
    <td  width="10%" align="center" class="detail-data-body-title">ƽ��ÿ��д����</td>
    <td  width="10%" align="center" class="detail-data-body-title">ƽ��ÿ��д�ֽ���</td>
    <td  width="10%" align="center" class="detail-data-body-title">ƽ��ÿ����ֽ���</td>
    <td  width="10%" align="center" class="detail-data-body-title">ÿ����ֽ���</td>
    <td  width="10%" align="center" class="detail-data-body-title">ÿ��д�ֽ���</td>
  </tr>

             <%
           
                if(alldiskperf == null)alldiskperf = new ArrayList();
                
                for(int i=0;i<alldiskperf.size();i++)
                {
                  Hashtable disk=(Hashtable)alldiskperf.get(i);
             %>
            <tr bgcolor="#FFFFFF" >
            <td  class="detail-data-body-list" align="center">&nbsp;<%=disk.get("Name")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("AvgDiskQueueLength")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("AvgDiskReadQueueLength")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("AvgDiskWriteQueueLength")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("AvgDisksecPerWrite")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("AvgDisksecPerRead")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("DiskReadBytesPersec")%></td>
            <td  class="detail-data-body-list" align="center"> &nbsp;<%=disk.get("DiskWriteBytesPersec")%></td>
            </tr>
            <%
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
							<td class="td-container-main-tool" width=15%>
								<table class="container-main-tool">
								    <tr>
										<jsp:include page="/include/toolbar.jsp">
										 <jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
										<jsp:param value="<%=tmp%>" name="tmp"/>
										<jsp:param value="host" name="category"/>
										<jsp:param value="windows" name="subtype"/>
									    </jsp:include>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>