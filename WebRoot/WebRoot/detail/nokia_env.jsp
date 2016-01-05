<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.util.*" %>
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
 	String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	String menuTable = (String) request.getAttribute("menuTable");//菜单
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = ""; 
	
	//内存利用率和响应时间 begin 
	Vector memoryVector = new Vector(); 
	String memoryvalue="0";
	//end
	
    //ping和响应时间begin
    String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0"; 
	String pingconavg ="0";
 	String maxpingvalue = "0";
	String pingvalue = "0";
    //end
    
    //时间设置begin
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 
	String time1 = sdf2.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	//end
    	

	String curin = "0";
	String curout = "0";
    	
   	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	if(host == null){
   		//从数据库里获取
   		HostNodeDao hostdao = new HostNodeDao();
   		HostNode node = null;
   		try{
   			node = (HostNode)hostdao.findByID(tmp);
   		}catch(Exception e){
   		}finally{
   			hostdao.close();
   		}
   		HostLoader loader = new HostLoader();
   		loader.loadOne(node);
   		loader.close();
   		host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	}
   	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);    	
    	 
	//存放环境信息
	Vector powervector = new Vector();
	Vector envvector = new Vector();
        
        if("0".equals(runmodel)){
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			//2.内存获取
			memoryVector = (Vector) ipAllData.get("memory");
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
				}
			}
			//得到电源信息
			powervector = (Vector)ipAllData.get("power");
			envvector = (Vector)ipAllData.get("fan");
			if(envvector != null && envvector.size()>0){
				for(int i=0;i<envvector.size();i++){
					Interfacecollectdata interfacedata = (Interfacecollectdata)envvector.get(i);
					powervector.add(interfacedata);
				}
			}
		}
		
		//----ping值和响应时间begin
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
				avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end
		
		
		Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
	}else{
		//采集与访问是分离模式
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List arpList = new ArrayList();
		List envlist = new ArrayList();
		
		//3.
		List memoryList = new ArrayList();
		try{
			systemList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrCpuAvgInfo());
			pingconavg = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
			envlist = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrENVInfo();
		//4.
			memoryList = new NetService(host.getId() + "", nodedto.getType(), nodedto.getSubtype()).getCurrMemoryInfo();
		}catch(Exception e){
		}
		//5.
		if (memoryList != null && memoryList.size() > 0) {
			for (int i = 0; i < memoryList.size(); i++) {
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp) memoryList.get(i);
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}
		
		if(systemList != null && systemList.size()>0){
			for(int i=0;i<systemList.size();i++){
				NodeTemp nodetemp = (NodeTemp)systemList.get(i);
				if("sysUpTime".equals(nodetemp.getSindex()))sysuptime = nodetemp.getThevalue();
				if("sysDescr".equals(nodetemp.getSindex()))sysdescr = nodetemp.getThevalue();
				if("sysLocation".equals(nodetemp.getSindex()))syslocation = nodetemp.getThevalue();
			}
		}
		if(pingList != null && pingList.size()>0){
			for(int i=0;i<pingList.size();i++){
				NodeTemp nodetemp = (NodeTemp)pingList.get(i);
				if("ConnectUtilization".equals(nodetemp.getSindex())){
					collecttime = nodetemp.getCollecttime();
				}
			}
		}
		if(envlist != null && envlist.size()>0){
			for(int i=0;i<envlist.size();i++){
				NodeTemp nodetemp = (NodeTemp)envlist.get(i);
				Interfacecollectdata interfacedata = new Interfacecollectdata();
				interfacedata.setIpaddress(host.getIpAddress());
				SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date da = _sdf.parse(nodetemp.getCollecttime());
				Calendar tempCal = Calendar.getInstance();
				tempCal.setTime(da);
				interfacedata.setCollecttime(tempCal);
				interfacedata.setCategory(nodetemp.getEntity());
				interfacedata.setEntity(nodetemp.getSindex());
				interfacedata.setSubentity(nodetemp.getSubentity());
				interfacedata.setRestype("dynamic");
				interfacedata.setUnit("");		
				interfacedata.setThevalue(nodetemp.getThevalue());
				//System.out.println(nodetemp.getEntity()+"======"+nodetemp.getSindex()+"====="+nodetemp.getThevalue());
				powervector.add(interfacedata);
			}
		}
		//----ping值和响应时间begin
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
				avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end
	}

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();
  	String rootPath = request.getContextPath();   

     	IpAliasDao ipdao = new IpAliasDao();
     	List iplist = ipdao.loadByIpaddress(host.getIpAddress());
     	if(iplist == null)iplist = new ArrayList();
     	ipdao.close();
     
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
         		
	String systype = "";
	if(sysdescr!=null){
	    if(sysdescr.toLowerCase().indexOf("h3c")!=-1){
	        systype = "h";
	    }
	    if(sysdescr.toLowerCase().indexOf("cisco")!=-1){
	        systype = "c";
	    }
	}
    String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	
	//6.
	int allmemoryvalue = 0;
	if (memoryVector != null && memoryVector.size() > 0) {
		for (int i = 0; i < memoryVector.size(); i++) {
			Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
			allmemoryvalue = allmemoryvalue + Integer.parseInt(memorycollectdata.getThevalue());
		}
		memoryvalue = (allmemoryvalue/memoryVector.size())+"";
	}
    	//生成当天平均连通率图形
		CreatePiePicture _cpp = new CreatePiePicture();
	        TitleModel _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//外环向左的位置
	        _titleModel.setX2(60);//外环向上的位置
	        _titleModel.setX3(65);
	        _titleModel.setX4(30);
	        _titleModel.setX5(75);
	        _titleModel.setX6(70);
	        _titleModel.setX7(10);
	        _titleModel.setX8(115);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(picip+"pingavg");
	        _titleModel.setTopTitle("");
	        
	        double[] _data1 = {avgpingcon, 100-avgpingcon};
	        String[] p_labels = {"连通", "未连通"};
	        int[] _colors = {0x66ff66, 0xff0000}; 
	        _cpp.createOneRingChart(_data1,p_labels,_colors,_titleModel);
	        
	   //生成CPU仪表盘 
		CreateMetersPic cmp = new CreateMetersPic();
		cmp.createCpuPic(picip, cpuper); 
%> 
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />

<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
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
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=netAjaxUpdate&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "平均连通率" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "当前CPU利用率" }); 
			
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
     	alert("请输入查询条件");
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
	document.getElementById('nokiaDetailTitle-5').className='detail-data-title';
	document.getElementById('nokiaDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('nokiaDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}


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


</head>
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
											<jsp:include page="/topology/includejsp/systeminfo_net.jsp">
												 <jsp:param name="rootPath" value="<%= rootPath %>"/>
												 <jsp:param name="tmp" value="<%= tmp %>"/>
												 <jsp:param name="sysdescr" value="<%= sysdescr %>"/>
												 <jsp:param name="sysuptime" value="<%= sysuptime %>"/>
												 <jsp:param name="collecttime" value="<%= collecttime %>"/>
												 <jsp:param name="picip" value="<%= picip %>"/>
												 <jsp:param name="syslocation" value="<%= syslocation %>"/>
												 <jsp:param name="flag1" value="<%= flag1 %>"/>   
												 <jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg)) %>"/>
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
												      			<td valign=top>
												         
												                <br>
												                      <table cellpadding="0" cellspacing="0" width=48% align=center>
									              							<tr> 
									                							<td width="100%" align=center> 
									                							<!-- 电源 -->
									                								<div id="flashcontent1">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Envoriment_component.swf?ipadress=<%=host.getIpAddress()%>&key=power&table=power&type=<%=systype%>&title=电源", "Envoriment_component_1", "346", "250", "8", "#ffffff");
																						so.write("flashcontent1");
																				</script>				
																                </td>
																	</tr> 
																	<tr>
																		<td width=100% align=center >
																		<table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29;align:center">名称</td>
																				<td class="detail-data-body-title" style="height:29;align:center">当前状态</td>
																				<td class="detail-data-body-title" style="height:29;align:center">采集时间</td>
																			</tr>
																			<%
																				if(powervector != null && powervector.size()>0){
																					for(int i=0;i<powervector.size();i++){
																						Interfacecollectdata interfacedata = (Interfacecollectdata)powervector.get(i);
																						if(!"Power".equals(interfacedata.getCategory().trim()))continue;
																						String statusstr = "";
																						if("1".equals(interfacedata.getThevalue())){
																							statusstr = "正常";
																						}
																						Calendar tempCal = (Calendar)interfacedata.getCollecttime();							
																						Date cc = tempCal.getTime();
																			%>
																			<tr>
																				<td class="detail-data-body-list" style="align:center">&nbsp;电源模块<%=interfacedata.getSubentity()%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=statusstr%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=sdf1.format(cc)%></td>
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
												                  <td align=center valign=top>
												                  <br>
												                      <table cellpadding="0" cellspacing="0" width=48% align=center>
									              							<tr> 
									                							<td width="100%"  align=center> 
									                							<!-- 电压 -->
									                								<div id="flashcontent2">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/Envoriment_component.swf?ipadress=<%=host.getIpAddress()%>&key=vol&table=vol&type=<%=systype%>&title=电压", "Envoriment_component_2", "346", "250", "8", "#ffffff");
																						so.write("flashcontent2");
																					</script>				
																                </td>
																	</tr> 
																	<tr>
																		<td width=100% align=center >
																		<table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29;align:center">名称</td>
																				<td class="detail-data-body-title" style="height:29;align:center">当前状态</td>
																			</tr>
																			<%
																				if(powervector != null && powervector.size()>0){
																					for(int i=0;i<powervector.size();i++){
																						Interfacecollectdata interfacedata = (Interfacecollectdata)powervector.get(i);
																						if(!"Voltage".equals(interfacedata.getCategory().trim()))continue;
																						String statusstr = "";
																						if("1".equals(interfacedata.getThevalue())){
																							statusstr = "正常";
																						}
																			%>
																			<tr>
																				<td class="detail-data-body-list" style="align:center">&nbsp;电压模块<%=interfacedata.getSubentity()%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=statusstr%></td>
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
												                    <%if(host.getCollecttype() != 3 && host.getCollecttype()!=4){%>
												                    <tr>
												                    <td align=center valign=top>
												                    <br>
												                      <table cellpadding="0" cellspacing="0" width=48% align=center>
												              							<tr> 
												                							<td width="100%"  align=center> 
												                							<!-- 风扇 -->
												                								<div id="flashcontent3">
																									<strong>You need to upgrade your Flash Player</strong>
																								</div>
																								<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Envoriment_component.swf?ipadress=<%=host.getIpAddress()%>&key=fan&table=fan&type=<%=systype%>&title=风扇", "Envoriment_component_3", "346", "250", "8", "#ffffff");
																									so.write("flashcontent3");
																								</script>				
																			                </td>
																				</tr>
																				<tr>
																		<td width=100% align=center >
																		<table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29;align:center">名称</td>
																				<td class="detail-data-body-title" style="height:29;align:center">当前状态</td>
																				<td class="detail-data-body-title" style="height:29;align:center">采集时间</td>
																			</tr>
																			<%
																				if(powervector != null && powervector.size()>0){
																				
																					for(int i=0;i<powervector.size();i++){
																						Interfacecollectdata interfacedata = (Interfacecollectdata)powervector.get(i);
																						//System.out.println("=====================1111"+interfacedata.getCategory());
																						if(!"Fan".equals(interfacedata.getCategory().trim()))continue;
																						String statusstr = "";
																						if("1".equals(interfacedata.getThevalue())){
																							statusstr = "正常";
																						}
																						Calendar tempCal = (Calendar)interfacedata.getCollecttime();							
																						Date cc = tempCal.getTime();
																			%>
																			<tr>
																				<td class="detail-data-body-list" style="align:center">&nbsp;风扇模块<%=interfacedata.getSubentity()%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=statusstr%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=sdf1.format(cc)%></td>
																			</tr>
																			<%
																					}
																				}
																			%>           
														   				</table>
														   				</td>
																		</tr>             
															</table>
																					<br>
												                  </td>
												                  <td align=center valign=top>
												                  <br>
												                      <table cellpadding="0" cellspacing="0" width=48%>
												              							<tr> 
												                							<td width="100%"  align=center> 
												                							<!-- 温度 -->
												                								<div id="flashcontent4">
																									<strong>You need to upgrade your Flash Player</strong>
																								</div>
																								<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Envoriment_component.swf?ipadress=<%=host.getIpAddress()%>&key=temper&table=temper&type=<%=systype%>&title=温度", "Envoriment_component_4", "346", "250", "8", "#ffffff");
																									so.write("flashcontent4");
																								</script>				
																			                </td>
																				</tr> 
																				<tr>
																		<td width=100% align=center >
																		<table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29;align:center">名称</td>
																				<td class="detail-data-body-title" style="height:29;align:center">当前状态</td>
																				<td class="detail-data-body-title" style="height:29;align:center">采集时间</td>
																			</tr>
																			<%
																				if(powervector != null && powervector.size()>0){
																					for(int i=0;i<powervector.size();i++){
																						Interfacecollectdata interfacedata = (Interfacecollectdata)powervector.get(i);
																						if(!"Temperature".equals(interfacedata.getCategory().trim()))continue;
																						String statusstr = "";
																						if("1".equals(interfacedata.getThevalue())){
																							statusstr = "正常";
																						}
																						Calendar tempCal = (Calendar)interfacedata.getCollecttime();							
																						Date cc = tempCal.getTime();
																			%>
																			<tr>
																				<td class="detail-data-body-list" style="align:center">&nbsp;温度模块<%=interfacedata.getSubentity()%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=statusstr%></td>
																				<td class="detail-data-body-list" style="align:center">&nbsp;<%=sdf1.format(cc)%></td>
																			</tr>
																			<%
																					}
																				}
																			%>           
														   				</table>
														   				</td>
																		</tr>            
															</table> 
															<br>
												                     
														       	</td>
												      		</tr>
												  
												      		<%} %>
												      		
											    		</table>
											    		
											    </td>
											  </tr>
											</table>
											
										</td>
									</tr>
								</table>
							</td>
							<td class="td-container-main-tool" width=14%>
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="network" name="category"/>
									<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype"/>
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
//Ext.MessageBox.wait('数据加载中，请稍后.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=bkpCfg&ipaddress=<%=host.getIpAddress()%>&page=netenv&id="+<%=tmp %>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp %>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
</BODY>
</HTML>