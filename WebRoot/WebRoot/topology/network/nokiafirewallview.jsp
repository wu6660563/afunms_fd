<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
	String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String collecttime = "";
	String sysuptime = "";
	String sysdescr = "";
	String syslocation = ""; 
	//�ڴ������ʺ���Ӧʱ�� begin 
	Vector memoryVector = new Vector(); 
	String memoryvalue="0";
	//end
    //ping����Ӧʱ��begin
    String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0"; 
	String pingconavg ="0";
 	String maxpingvalue = "0";
	String pingvalue = "0";
    //end
    //ʱ������begin
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
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 
	String time1 = sdf2.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	//end
	String curin = "0";
	String curout = "0";

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));

	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	if (host == null) {
		//�����ݿ����ȡ
		HostNodeDao hostdao = new HostNodeDao();
		HostNode node = null;
		try {
			node = (HostNode) hostdao.findByID(tmp);
		} catch (Exception e) {
		} finally {
			hostdao.close();
		}
		HostLoader loader = new HostLoader();
		loader.loadOne(node);
		loader.close();
		host = (Host) PollingEngine.getInstance().getNodeByID(
				Integer.parseInt(tmp));
	}
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	if ("null".equals(host.getMac()))
		host.setMac("");
	String ipaddress = host.getIpAddress();
 
	//��Ż�����Ϣ
	Vector powervector = new Vector();
	Vector envvector = new Vector();
	Vector deviceV = new Vector();
	if ("0".equals(runmodel)) {
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		
		if (ipAllData != null) {
		deviceV = (Vector) ipAllData.get("device");
			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {

				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			memoryVector = (Vector) ipAllData.get("memory");
			//�õ�������Ϣ
			envvector = (Vector) ipAllData.get("flash");
			//flashVector = (Vector)ipAllData.get("flash");
			//�õ�ϵͳ����ʱ��
			Vector systemV = (Vector) ipAllData.get("system");
			if (systemV != null && systemV.size() > 0) {
				for (int i = 0; i < systemV.size(); i++) {
					Systemcollectdata systemdata = (Systemcollectdata) systemV.get(i);
					if (systemdata.getSubentity().equalsIgnoreCase("sysUpTime")) {
						sysuptime = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase("sysDescr")) {
						sysdescr = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase("sysLocation")) {
						syslocation = systemdata.getThevalue();
					}
				}
			}
			//��ȡ��ǰ����
			Vector allutilhdxVector = (Vector) ipAllData.get("allutilhdx");
			if (allutilhdxVector != null && allutilhdxVector.size() > 0) {
				for (int i = 0; i < allutilhdxVector.size(); i++) {
					AllUtilHdx allutilhdx = (AllUtilHdx) allutilhdxVector.get(i);
					if ("AllInBandwidthUtilHdx".equals(allutilhdx.getSubentity())) {
						curin = allutilhdx.getThevalue();
					}
					if ("AllOutBandwidthUtilHdx".equals(allutilhdx.getSubentity())) {
						curout = allutilhdx.getThevalue();
					}
				}
			}
		}
		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization",starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
			maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
			maxpingvalue =maxpingvalue.replaceAll("%",""); 
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}  
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host .getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end
		

		Vector pingData = (Vector) ShareData.getPingdata().get(
				host.getIpAddress());
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData
					.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();
			pingdata = (Pingcollectdata) pingData.get(1);
			responsevalue = pingdata.getThevalue();
		}
		if (envvector != null && envvector.size() > 0) {
			for (int i = 0; i < envvector.size(); i++) {
				Flashcollectdata nodetemp = (Flashcollectdata) envvector
						.get(i);
				Interfacecollectdata interfacedata = new Interfacecollectdata();
				interfacedata.setIpaddress(host.getIpAddress());
				interfacedata.setCategory(nodetemp.getCategory());
				interfacedata.setEntity(nodetemp.getEntity());
				interfacedata.setSubentity(nodetemp.getSubentity());
				interfacedata.setRestype("dynamic");
				interfacedata.setUnit("");
				interfacedata.setThevalue(nodetemp.getThevalue());
				powervector.add(interfacedata);
			}
		}

	} else {
		//�ɼ�������Ƿ���ģʽ
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List memoryList = new ArrayList();
		List envlist = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrPingInfo();
					if(host.getCategory() != 9)
			cpuvalue = new Double(new NetService(host.getId() + "",
					nodedto.getType(), nodedto.getSubtype())
					.getCurrCpuAvgInfo());
					if(host.getCategory() != 9)
			memoryList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrMemoryInfo();
			envlist = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrENVInfo();
		} catch (Exception e) {
		}
		if (memoryList != null && memoryList.size() > 0) {
			for (int i = 0; i < memoryList.size(); i++) {
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp) memoryList.get(i);
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}
		if (envlist != null && envlist.size() > 0) {
			for (int i = 0; i < envlist.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) envlist.get(i);
				Interfacecollectdata interfacedata = new Interfacecollectdata();
				interfacedata.setIpaddress(host.getIpAddress());
				SimpleDateFormat _sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date da = _sdf.parse(nodetemp.getCollecttime());
				Calendar tempCal = Calendar.getInstance();
				tempCal.setTime(da);
				interfacedata.setCollecttime(tempCal);
				interfacedata.setCategory(nodetemp.getEntity());
				interfacedata.setEntity(nodetemp.getSindex());
				interfacedata.setSubentity(nodetemp.getSindex());
				interfacedata.setRestype("dynamic");
				interfacedata.setUnit("");
				interfacedata.setThevalue(nodetemp.getThevalue());
				powervector.add(interfacedata);
			}
		}
		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("sysUpTime".equals(nodetemp.getSindex()))
					sysuptime = nodetemp.getThevalue();
				if ("sysDescr".equals(nodetemp.getSindex()))
					sysdescr = nodetemp.getThevalue();
				if ("sysLocation".equals(nodetemp.getSindex()))
					syslocation = nodetemp.getThevalue();
			}
		}
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
				}
			}
		}
		//��ȡ��ǰ����
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		Vector allutilhdxVector = (Vector) hostlastmanager
				.getAllUtilHdxInterface(host.getIpAddress(), starttime,
						endtime);
		
		if (allutilhdxVector != null && allutilhdxVector.size() > 0) {
			for (int i = 0; i < allutilhdxVector.size(); i++) {
				AllUtilHdx allutilhdx = (AllUtilHdx) allutilhdxVector
						.get(i);
				if ("AllInBandwidthUtilHdx".equals(allutilhdx
						.getSubentity())) {
					curin = allutilhdx.getThevalue();
				}
				if ("AllOutBandwidthUtilHdx".equals(allutilhdx
						.getSubentity())) {
					curout = allutilhdx.getThevalue();
				}
			}
		}

		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization",starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
			maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
			maxpingvalue =maxpingvalue.replaceAll("%",""); 
		}
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}  
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
				}
				if ("ResponseTime".equals(nodetemp.getSindex())) {
					responsevalue = nodetemp.getThevalue();
				}
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					pingvalue = nodetemp.getThevalue();
				}
			}
		}
	}

	Hashtable hash = (Hashtable) request.getAttribute("hash");
	Hashtable hash1 = (Hashtable) request.getAttribute("hash1");
	Hashtable max = (Hashtable) request.getAttribute("max");

	Hashtable allavgandmaxHash = (Hashtable) request
			.getAttribute("allavgandmaxHash");

	int avgin = 0;
	int avgout = 0;
	int maxin = 0;
	int maxout = 0;
	if (allavgandmaxHash != null && allavgandmaxHash.size() > 0) {
		if (allavgandmaxHash.containsKey("agvin")) {
			avgin = (Integer) allavgandmaxHash.get("agvin");
		}
		if (allavgandmaxHash.containsKey("maxin")) {
			maxin = (Integer) allavgandmaxHash.get("maxin");
		}
		if (allavgandmaxHash.containsKey("agvout")) {
			avgout = (Integer) allavgandmaxHash.get("agvout");
		}
		if (allavgandmaxHash.containsKey("maxout")) {
			maxout = (Integer) allavgandmaxHash.get("maxout");
		}
	}
 
	String avgcpu ="0";
	String cpumax="0";
	if(max!=null){
	 avgcpu=(String) max.get("cpuavg");
	 cpumax = (String) max.get("cpu");
	
	 }
	 
	 if(avgcpu.equals("")||avgcpu==null){
	 avgcpu ="0";
	 }
	 if(cpumax.equals("")||cpumax==null){
	 cpumax="0";
	 }
	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

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

	if (syslocation == null || "null".equals(syslocation))
		syslocation = "";
	if (sysdescr == null || "null".equals(sysdescr))
		sysdescr = "";
	if (sysuptime == null || "null".equals(sysuptime))
		sysuptime = "";
	
	int allmemoryvalue = 0;
	if (memoryVector != null && memoryVector.size() > 0) {
		for (int i = 0; i < memoryVector.size(); i++) {
			Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
			allmemoryvalue = allmemoryvalue + Integer.parseInt(memorycollectdata.getThevalue());
		}
		memoryvalue = (allmemoryvalue/memoryVector.size())+"";
	}
	//System.out.println(memoryvalue+"-----------------------");

	//������ͨ��ͼ��
	Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createPingPic(picip, realValue); //����
	_cpp.createAvgPingPic(picip, avgpingcon); //ƽ��
	_cpp.createMinPingPic(picip, maxpingvalue.replaceAll("%", ""));//��С 
	//����CPU�Ǳ���
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
	cmp.createAvgCpuPic(picip, avgcpu);
	cmp.createMaxCpuPic(picip, cpumax);

	CreateBarPic cbp = new CreateBarPic();
	String[] labels = { "�����ڴ�", "�����ڴ�" };
	TitleModel tm = new TitleModel();
	cbp = new CreateBarPic();
	double[] r_data1 = { new Double(responsevalue.replaceAll("%", "")),
			new Double(maxresponse.replaceAll("%", "")), new Double(avgresponse.replaceAll("%", "")) };
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
	//������״ͼ

	TitleModel tm1 = new TitleModel();
	tm1.setPicName(picip + "flow");//
	tm1.setBgcolor(0xffffff);
	tm1.setXpic(410);//ͼƬ���
	tm1.setYpic(200);//ͼƬ�߶�
	tm1.setX1(60);//ͼ��߾���
	tm1.setX2(15);//ͼ�ϱ߾���
	tm1.setX3(360);//��ͼ���
	tm1.setX4(140);//��ͼ�߶�
	tm1.setX5(90);//����ͼ�������֮��ľ���
	tm1.setX6(175);//����ͼ���붥��֮��ľ���
	int barwidth = 100;//ͼƬ�����ӵĿ��
	int color1 = 0x80ff80;
	int color2 = 0x8080ff;
	double[] data0 = { Double.valueOf(curin), Double.valueOf(avgin),
			Double.valueOf(maxin) };//����
	double[] data1 = { Double.valueOf(curout), Double.valueOf(avgout),
			Double.valueOf(maxout) };
	String[] dataName = { "��ǰ����", "ƽ������", "�������" };//ÿ�����ݶ�Ӧ������
	String[] labels1 = { "�������", "��������" };//���ݵ�����
	int[] color = { 0x7FFF00, 0xFFFF00, 0xFF0000 };//ÿ�����ݶ�Ӧ����ɫ
	cbp.createCompareThreeBarPic(data0, data1, dataName, labels1,
			color, tm1, barwidth);
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
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
        <script type="text/javascript"
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
		<script>

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
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=netAjaxUpdate&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				//$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
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
  
  function toOpen(){
 
	    window.open ("<%=rootPath%>/monitor.do?action=netcpu_report&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
		
}
  
function changeOrder(para){
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
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
	document.getElementById('nokiaDetailTitle-0').className='detail-data-title';
	document.getElementById('nokiaDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('nokiaDetailTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
  mainForm.submit();
 });
 
  Ext.get("process1").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/cfgfile.do?action=nodelist&ipaddress=<%=host.getIpAddress()%>";
  mainForm.submit();
 });	
	
});

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


// zhubinhua  add   
//�����ڷ����������ɶ�ӦFlexͼƬ,,Ȼ������word�ĵ�  ����
function outputWord() {
	//alert("YES!");
	//������ͼƬ
	//alert(window["Response_time"]);
	window["Response_time"].createImage("net");
	window["Ping"].createImage("net");
	window["Line_CPU"].createImage("net");
	window["Area_flux"].createImage("net");
	window["Net_Memory"].createImage("net");
	window["Net_flash_Memory"].createImage("net");
	
	//����word ������
	//alert("======");
	var a = document.getElementById("outputWord");
	//alert("<%=rootPath%>/monitor.do?action=createWord&whattype=net&id=<%=tmp%>");
	a.href="<%=rootPath%>/monitor.do?action=createWord&whattype=net&id=<%=tmp%>";
}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();" leftmargin="0"
		topmargin="0">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
												<jsp:include page="/topology/includejsp/systeminfo_net.jsp">
													 <jsp:param name="rootPath" value="<%= rootPath %>"/>
													 <jsp:param name="tmp" value="<%= tmp %>"/>
													 <jsp:param name="sysdescr" value="<%= sysdescr %>"/>
													 <jsp:param name="sysuptime" value="<%= sysuptime %>"/>
													 <jsp:param name="collecttime" value="<%= collecttime %>"/>
													 <jsp:param name="picip" value="<%= picip %>"/>
													 <jsp:param name="syslocation" value="<%= syslocation %>"/>
													 <jsp:param name="flag1" value="<%= flag1 %>"/>
													 <jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>   
											 		 <jsp:param name="avgresponse" value="<%=avgresponse%>"  />  
													 <jsp:param name="memoryvalue" value="<%= memoryvalue %>"/>	 
												 </jsp:include>	
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=nokiaDetailTitleTable%>

														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<tr>
																	<td>
																		&nbsp;
																	</td>
																	<td align="right">
																		<a id="outputWord" onclick="outputWord()" href=""><img
																				name="selDay1" alt='����word' style="CURSOR: hand"
																				src="<%=rootPath%>/resource/image/export_word.gif"
																				width=18 border="0">����word</a>&nbsp;&nbsp;
																	</td>
																</tr>
																<tr>
																	<td valign=top>

																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "295", "8", "#ffffff");
																						so.write("flashcontent2");
																					</script>
																				</td>

																			</tr>

																		</table>
																	</td>
																	<td align=center valign=top >

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
																										<td style="background-color: #FFFFFF;">
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
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table width="100%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																					  
																																						<td valign=top>
																																							<!-- <img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">
																																						      -->
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
																																						<!--  
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">
																																						-->
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
																																						<!--
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">
																																						-->
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
																																					<tr>
																																						<td width=100% align=center
																																							colspan=3>
																																							<table cellpadding="1"
																																								cellspacing="1"
																																								style="align: center; width: 100%"
																																								bgcolor="#FFFFFF">
																																								<tr>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29">
																																										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										��ǰ��%��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										��С��%��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										ƽ����%��
																																									</td>
																																								</tr>
																																								<tr>
																																									<td
																																										class="detail-data-body-list"
																																										height="29">
																																										&nbsp;&nbsp;&nbsp;&nbsp;��ͨ��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=pingvalue%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%","")))%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=Math.round(Float.parseFloat(pingconavg.replaceAll("%","")))%></td>
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
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent1">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "305", "8", "#ffffff");
																						so.write("flashcontent1");
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
																										<td style="background-color: #FFFFFF;">
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
																															class="detail-content-body" border='1'>
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
																																					<tr height=24>
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
																																					<tr>
																																						<td width=100% align=center
																																							colspan=3>
																																							<table cellpadding="1"
																																								cellspacing="1"
																																								style="align: center; width: 100%"
																																								bgcolor="#FFFFFF">
																																								<tr bgcolor="#FFFFFF">
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										����
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										��ǰ��ms��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										���ms��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										ƽ����ms��
																																									</td>
																																								</tr>
																																								<tr>
																																									<td
																																										class="detail-data-body-list"
																																										height="29" align=center>
																																										��Ӧʱ��
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=responsevalue%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=maxresponse%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=avgresponse%></td>
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

																<%
																	if (host.getCollecttype() != 3 && host.getCollecttype() != 4
																			&& host.getCollecttype() != 8 && host.getCollecttype() != 9) {
																%>
																<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "350", "300", "8", "#ffffff");
																									so.write("flashcontent3");
																								</script>
																				</td>
																			</tr>
																			
																		</table>
																		<br>
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
																										<td style="background-color: #FFFFFF;">
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
																															class="detail-content-body" border='1'>
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
																																					<tr height=24>
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
																																					<tr>
																				<td width=100% align=center colspan=3>
																					<table cellpadding="0" cellspacing="1"
																						style="align: center; width: 100%"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								����
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								��ǰ
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								���
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								ƽ��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								CPU������
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=cpuper%>%
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=cpumax%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=avgcpu%></td>
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
																																	<table cellpadding="1" cellspacing="1"
																																		width=80%>
																																		<tr>
																																			<td valign=top width=80%>
																																				<table cellpadding="0"
																																					cellspacing="0" width=80%
																																					align=center>
																																					<%
																																						if (deviceV != null) {
																																								for (int m = 0; m < deviceV.size(); m++) {
																																									Devicecollectdata devicedata = (Devicecollectdata) deviceV
																																											.get(m);
																																									String name = devicedata.getName();
																																									String type = devicedata.getType();
																																									if (!"CPU".equals(type))
																																										continue;
																																									String status = devicedata.getStatus();
																																					%>
																																					<tr height=20>
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
																																			<td  align=right
																																				colspan=3>
																																				>><a href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=cpudetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','CPUʵʱ����','top=180,left=300,height=450 ,width=720, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">ʵʱ</a>
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
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent5">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Net_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Net_Memory", "346", "250", "8", "#ffffff");
																									so.write("flashcontent5");
																								</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="0" cellspacing="1"
																						style="align: center; width: 346"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29; align: center">
																								����
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29; align: center">
																								��ǰ������(%)
																							</td>
																						</tr>
																						<%
																						StringBuffer sb=new StringBuffer();
																						String netdata="";
																						String[] colorStr=new String[] {"#33CCFF","#003366","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#23f266"};
																						
																							if (memoryVector != null && memoryVector.size() > 0) {
																							sb.append("<chart><series>");
																							for (int i = 0; i < memoryVector.size(); i++) {
																							Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
																							sb.append("<value xid='").append(i).append("'>").append("�ڴ�ģ��"+memorycollectdata.getSubentity()).append("</value>");
																						}
																							sb.append("</series><graphs><graph gid='0'>");
																							for (int i = 0; i < memoryVector.size(); i++) {
																										Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
																										sb.append("<value xid='").append(i).append("' color='").append(colorStr[i]).append("'>"+memorycollectdata.getThevalue()).append("</value>");
																						
																						%>
																						<tr>
																							<td class="detail-data-body-list"
																								style="align: center">
																								&nbsp;�ڴ�ģ��<%=memorycollectdata.getSubentity()%></td>
																							<td class="detail-data-body-list"
																								style="align: center">
																								&nbsp;<%=memorycollectdata.getThevalue()%></td>
																						</tr>
																						<%
																							}
																							sb.append("</graph></graphs></chart>");
																								}else{
																								sb.append("0");
																								}
																								
																								
																								netdata=sb.toString();
																								
																						%>
																					</table>
																				</td>
																			</tr>
																		</table>
																		<br>
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
																										<td style="background-color: #FFFFFF;">
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
																																	<b>�ڴ�����������</b>
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
																															class="detail-content-body" border='1'>
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
																																							<br>
																		                                                                                <table cellpadding="0" cellspacing="0" width=48%>
																			                                                                               <tr>
																				                                                                             <td width="100%" align=center>
																				                                                                              <div id="netmemory">
							                                                                                                                                   
						                                                                                                                                       </div>
						                                                                                                                                       
						                                                                                                                                       <script type="text/javascript"
							                                                                                                                                   src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                                       <script type="text/javascript">
						                                                                                                                                       <% if(!netdata.equals("0")){ %>	
						                                                                                                                                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","420", "288", "8", "#FFFFFF");
						                                                                                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netmemory_settings.xml"));
						                                                                                                                                             so.addVariable("chart_data","<%=netdata%>");
						                                                                                                                                             so.write("netmemory");
						                                                                                                                                          <%}else{%>
																			                                                                                          var _div=document.getElementById("netmemory");
																			                                                                                          var img=document.createElement("img");
																			                                                                                              img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																			                                                                                              _div.appendChild(img);
																			                                                                                       <%}%>
						                                                                                                                                          </script>	
																				                                                                                   </td>
																			                                                                                         </tr>
																			
																		                                                                                              </table>
																		                                                                                             <br>
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
								<td class="td-container-main-tool" width="14%">
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="network" name="category" />
										<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype" />
									</jsp:include>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

		</form>
		<script type="text/javascript">
Ext.onReady(function()
{
Ext.get("bkpCfg").on("click",function(){
//Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=bkpCfg&ipaddress=<%=host.getIpAddress()%>&page=netcpu&id="+<%=tmp%>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp%>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
	</BODY>
</HTML>