<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.application.model.DominoDisk"%>
<%@page import="com.afunms.application.model.DominoServer"%>
<%@page import="com.afunms.application.model.DominoMem"%>
<%@page import="com.afunms.application.model.DominoLdap"%>
<%@page import="com.afunms.polling.om.Pingcollectdata"%>
<%@page import="com.afunms.application.dao.DominoMemDao"%>
<%@page import="com.afunms.application.dao.DominoServerDao"%>
<%@page import="com.afunms.application.dao.DominoLdapDao"%>
<%@page import="com.afunms.application.dao.DominoDiskDao"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>

<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>

<%@ page import="com.afunms.polling.om.*"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.temp.dao.PingTempDao"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>



<%
	
	String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	String menuTable = (String) request.getAttribute("menuTable");//菜单
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String collecttime = "";
	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "容量", "当前", "最大", "平均" };
	String temp="";
	
	double memoryvalue=0;
	double platformMemvalue=0;
	String memPhysical="";//物理内存总量
	String platformMemPhysical="";//平台物理内存
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
    	

	
      Domino domino = (Domino)PollingEngine.getInstance().getDominoByID(Integer.parseInt(tmp));	
	

	String ip = domino.getIpAddress();
	String picip = CommonUtil.doip(ip);
	
	
	String ipaddress = domino.getIpAddress();
 
	
	DominoDisk disk=new DominoDisk();
	List diskList=new ArrayList();
	String status="";
	DominoLdap ldap=null;
	DominoMem mem=null;
	DominoServer dominoServer=null;
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getDominodata().get(
				ipaddress);
		
		
		if (ipAllData != null) {
			Pingcollectdata pingdata = (Pingcollectdata)ipAllData.get("Ping");
		if(pingdata!=null){
		 pingvalue = pingdata.getThevalue();
		 }
		if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
		diskList = (List) ipAllData.get("Disk");
		dominoServer= (DominoServer) ipAllData.get("Server");
			
			//当前物理和平台物理内存利用率
			 mem = (DominoMem) ipAllData.get("Mem");
			
	    ldap=(DominoLdap)ipAllData.get("LDAP");
	  
		}
		

	} else {
	
	Pingcollectdata pingdata= (new PingTempDao()).getPingInfo("domPing", "ConnectUtilization");
	if(pingdata!=null)
	pingvalue=pingdata.getThevalue();
	//磁盘
	DominoDiskDao diskDao=new DominoDiskDao();
	diskList=diskDao.findByIp(ipaddress);
	
	//内存
	DominoMemDao memDao=new DominoMemDao();
	mem = (DominoMem)memDao.findByIp(ipaddress);
	
	//服务器
	DominoServerDao serverDao=new DominoServerDao();
	dominoServer=(DominoServer)serverDao.findByIp(ipaddress);
	//LDAP
	DominoLdapDao ldapDao=new DominoLdapDao();
	 ldap=(DominoLdap)ldapDao.findByIp(ipaddress);
	
	}
	if(ldap!=null)
	status=ldap.getLdapRunning();
		//----ping值和响应时间begin
		
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		
	try {
			ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "DomPing", "ConnectUtilization",starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
			maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
			maxpingvalue =maxpingvalue.replaceAll("%",""); 
		}
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}  
		//try {
			//ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "DominoPing", "ResponseTime", starttime1, totime1);
		//	if (ConnectUtilizationhash!=null&&ConnectUtilizationhash.get("avgpingcon") != null) {
		//		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		//		avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
		//		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		//		maxresponse=maxresponse.replaceAll("%","");		
		//	}
		//} catch (Exception ex) {
		//	ex.printStackTrace();
		//}
		//----ping值和响应时间end
		
     if (dominoServer != null) {
		    cpuvalue = new Double(dominoServer.getCpupctutil());
		}
	//cpu 平均最大值
	Hashtable cpuMaxAvg = (Hashtable) request.getAttribute("cpuMaxAvg");
	
	String avgcpu ="0";
	String cpumax="0";
	if(cpuMaxAvg!=null&&cpuMaxAvg.size()>0){
	 avgcpu=(String) cpuMaxAvg.get("cpuavg");
	 cpumax = (String) cpuMaxAvg.get("cpu");
	
	 }
	 if(avgcpu.equals("")||avgcpu==null){
	 avgcpu ="0";
	 }
	 if(cpumax.equals("")||cpumax==null){
	 cpumax="0";
	 }
	
	 //物理内存信息
	 if(mem!=null){
	 String memUtil=mem.getMempctutil();
	 if(memUtil.equals(""))
	   memUtil="0";
			memoryvalue= new Double(memUtil);//服务器内存利用率
			memoryvalue=CEIString.round(memoryvalue,2);
			String platMem=mem.getPlatformMemPhyPctUtil();
			if(platMem.equals(""))
			platMem="0";
			platformMemvalue=new Double(platMem);
			platformMemvalue=CEIString.round(platformMemvalue,2);//平台内存利用率
			memPhysical=mem.getMemPhysical();
			platformMemPhysical=mem.getPlatformMemPhysical();
			}
	 //服务器物理内存平均最大值
	 Hashtable sermemMaxAvg = (Hashtable) request.getAttribute("serMemMaxAvg");
	
	String serMemAvg ="0";
	String serMemMax="0";
	if(sermemMaxAvg!=null&&sermemMaxAvg.size()>0){
	 serMemAvg=(String) sermemMaxAvg.get("serMemAvg");
	 serMemMax = (String) sermemMaxAvg.get("serMemMax");
	
	 }
	 if(serMemAvg.equals("")||serMemAvg==null){
	 serMemAvg ="0";
	 }
	 if(serMemMax.equals("")||serMemMax==null){
	 serMemMax="0";
	  }
	
	  //平台物理内存平均最大值
	 Hashtable platmemMaxAvg = (Hashtable) request.getAttribute("platMemMaxAvg");
	
	String platMemAvg ="0";
	String platMemMax="0";
	if(platmemMaxAvg!=null&&platmemMaxAvg.size()>0){
	 platMemAvg=(String) platmemMaxAvg.get("platMemAvg");
	 platMemMax = (String) platmemMaxAvg.get("platMemMax");
	
	 }
	 if(platMemAvg.equals("")||platMemAvg==null){
	 platMemAvg ="0";
	 }
	 if(platMemMax.equals("")||platMemMax==null){
	 platMemMax="0";
	  }
	
	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

	
  request.setAttribute("server",dominoServer);

  String pingValue="0";
 if(pingvalue==null){
    pingvalue="0";
    }else{
   pingValue=pingvalue.replaceAll("%", "").replace("null","0");
   }
	//生成连通率图形
	Double realValue = Double.valueOf(pingValue);
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createPingPic(picip, realValue); //当天
	_cpp.createAvgPingPic(picip, avgpingcon); //平均
	_cpp.createMinPingPic(picip, maxpingvalue.replaceAll("%", ""));//最小 
	//生成CPU仪表盘
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
	cmp.createAvgCpuPic(picip, avgcpu);
	cmp.createMaxCpuPic(picip, cpumax);

	CreateBarPic cbp = new CreateBarPic();
	String[] labels = { "物理内存", "虚拟内存" };
	TitleModel tm = new TitleModel();
	cbp = new CreateBarPic();
	double[] r_data1 = { new Double(responsevalue.replaceAll("%", "")),
			new Double(maxresponse.replaceAll("%", "")), new Double(avgresponse.replaceAll("%", "")) };
	String[] r_labels = { "当前响应时间(ms)", "最大响应时间(ms)", "平均响应时间(ms)" };
	tm = new TitleModel();
	tm.setPicName(picip + "response");//
	tm.setBgcolor(0xffffff);
	tm.setXpic(450);//图片长度
	tm.setYpic(180);//图片高度
	tm.setX1(30);//左面距离
	tm.setX2(20);//上面距离
	tm.setX3(400);//内图宽度
	tm.setX4(130);//内图高度
	tm.setX5(10);
	tm.setX6(115);
	cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
	
	
	StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("连通;").append(Math.round(realValue)).append(";false;7CFC00\\n");
	dataStr1.append("未连通;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
	String realdata = dataStr1.toString();
	
	StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
	dataStr2.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
	
	StringBuffer dataStr3 = new StringBuffer();
	String maxping=maxpingvalue.replaceAll("%", "");
	dataStr3.append("连通;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
	dataStr3.append("未连通;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
	String maxdata = dataStr3.toString();
	//内存
	CreateAmColumnPic pic=new CreateAmColumnPic();
	String serMax=serMemMax.replaceAll("%","");
	String serAvg=serMemAvg.replaceAll("%","");
	String serMemData=pic.createDominoMem(memoryvalue+"",serMax,serAvg);
	String platMax=platMemMax.replaceAll("%","");
	String platAvg=platMemAvg.replaceAll("%","");
	String platMemData=pic.createDominoMem(platformMemvalue+"",platMax,platAvg);
	//磁盘
	String dataStr=pic.createDominoDiskPic(diskList);
	
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
	document.getElementById('netDetailTitle-1').className='detail-data-title';
	document.getElementById('netDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('netDetailTitle-1').onmouseout="this.className='detail-data-title'";
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
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=ipaddress%>";
  mainForm.submit();
 });
 
  Ext.get("process1").on("click",function(){
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  mainForm.action = "<%=rootPath%>/cfgfile.do?action=nodelist&ipaddress=<%=ipaddress%>";
  mainForm.submit();
 });	
	
});
function setClass(){
	document.getElementById('hostDetailTitle-0').className='detail-data-title';
	document.getElementById('hostDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
											<jsp:include page="/topology/includejsp/middleware_domino.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="status" value="<%=status%>" />
												    
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="memoryvalue" value="<%=memoryvalue%>" />
													<jsp:param name="platformMemvalue" value="<%=platformMemvalue%>" />
													
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>
													<jsp:param name="avgresponse" value="<%=avgresponse%>" />
												</jsp:include>		
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=dominoDetailTitleTable%>

														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																
																<tr>

																	<td align=center width=43% valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="pingcontent">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Ping_Domino.swf?ipadress=<%=ipaddress%>", "Ping_Domino", "346", "191", "8", "#ffffff");
																					so.write("pingcontent");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 346"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29">
																								名称
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								当前（%）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								最小（%）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								平均（%）
																							</td>
																							
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								连通率
																							</td>
																							<td class="detail-data-body-list" align=center><%=pingvalue%></td>
																							<td class="detail-data-body-list" align=center><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%","")))%></td>
																							<td class="detail-data-body-list" align=center><%=Math.round(Float.parseFloat(pingconavg.replaceAll("%","")))%></td>
																							
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=left valign=top width=57%>

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
																																	<b>连通率实时</b>
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



																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						<td valign=top>
																																						<!--  	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">-->
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
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">-->
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
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">-->
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
																																					<tr height=60>
																																						<td valign=middle align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=middle align=center>
																																							<b>最小</b>
																																						</td>
																																						<td valign=middle align=center>
																																							<b>平均</b>
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
																					var so = new SWFObject("<%=rootPath%>/flex/Line_CPU_Domino.swf?ipadress=<%=ipaddress%>", "Line_CPU_Domino", "346", "230", "8", "#ffffff");
																					so.write("flashcontent1");
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
																								style="height: 29">
																								名称
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								当前
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								最大
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								平均
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								&nbsp;CPU利用率
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpuper%>%
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpumax%></td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=avgcpu%></td>
																						</tr>
																					</table>
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
																																	<b>CPU利用率详情</b>
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
																																					<tr height=60>
																																						<td valign=middle align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=middle align=center>
																																							<b>最大</b>
																																						</td>
																																						<td valign=middle align=center>
																																							<b>平均</b>
																																						</td>
																																					</tr>
																																					<tr height=20>
																																						<td colspan=3>
																																							&nbsp;
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
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center valign=top>
																			<tr>
																				<td width="100%" align=center>
																					<div id="Domino_Ser">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Memory_Domino_Ser.swf?ipadress=<%=ipaddress%>", "Area_Memory_Domino_Ser", "346", "276", "8", "#ffffff");
																					so.write("Domino_Ser");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td align=center valign=top>

																					<table width="90%" cellpadding="0" cellspacing="0"
																						style="align: center; valign: top; width: 346">
																						<tr align="center" height="28"
																							style="background-color: #EEEEEE;">
																							<td width="21%" class="detail-data-body-title"
																								style="height: 29">
																								内存名
																							</td>
																							<%
																								
																									for (int j = 0; j < memoryItemch.length; j++) {
																										String item = memoryItemch[j];
																							%>
																							<td class="detail-data-body-title"><%=item%></td>
																							<%
																								}
																							%>
																						</tr>
																						
																						<tr>
																							<td width="20%" class="detail-data-body-list" height="29">&nbsp;物理内存</td>
																							<td width="20%" class="detail-data-body-list"> &nbsp;<%=memPhysical%></td>
																							<td width="17%" class="detail-data-body-list">&nbsp;<%=memoryvalue%>%</td>
																							<td width="17%" class="detail-data-body-list">&nbsp;<%=serMemMax%></td>
																							<td width="20%" class="detail-data-body-list"> &nbsp;<%=serMemAvg%></td>
																						</tr>
																						
																						
																					</table>

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
																																	<b>服务器内存明细</b>
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
																													<td valign=top>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td valign=top>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					
																																					<tr>
																																						<td colspan=1>
																																							 <div id="sermemory">
							                                                                                                                                   
						                                                                                                                                       </div>
						                                                                                                                                       
						                                                                                                                                       <script type="text/javascript"
							                                                                                                                                   src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                                       <script type="text/javascript">
						                                                                                                                                       <% if(!serMemData.equals("0")){ %>	
						                                                                                                                                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","420", "278", "8", "#FFFFFF");
						                                                                                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netmemory_settings.xml"));
						                                                                                                                                             so.addVariable("chart_data","<%=serMemData%>");
						                                                                                                                                             so.write("sermemory");
						                                                                                                                                          <%}else{%>
																			                                                                                          var _div=document.getElementById("sermemory");
																			                                                                                          var img=document.createElement("img");
																			                                                                                              img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																			                                                                                              _div.appendChild(img);
																			                                                                                       <%}%>
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
																																										<b>物理内存总量</b>：<%=memPhysical %></td>
																																									
																																								</tr>
																																							</table>

																																						</td>
																																					</tr>
																																					<tr height=15>
																																					
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
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center valign=top>
																			<tr>
																				<td width="100%" align=center>
																					<div id="platMem">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Meory_Domino.swf?ipadress=<%=ipaddress%>", "Area_Meory_Domino", "346", "276", "8", "#ffffff");
																					so.write("platMem");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td align=center valign=top>

																					<table width="90%" cellpadding="0" cellspacing="0"
																						style="align: center; valign: top; width: 346">
																						<tr align="center" height="28"
																							style="background-color: #EEEEEE;">
																							<td width="21%" class="detail-data-body-title"
																								style="height: 29">
																								内存名
																							</td>
																							<%
																								
																									for (int j = 0; j < memoryItemch.length; j++) {
																										String item = memoryItemch[j];
																							%>
																							<td class="detail-data-body-title"><%=item%></td>
																							<%
																								}
																							%>
																						</tr>
																						
																						
																						<tr>
																							<td width="20%" class="detail-data-body-list" height="29">&nbsp;平台内存</td>
																							<td width="20%" class="detail-data-body-list"> &nbsp;<%=platformMemPhysical%></td>
																							<td width="17%" class="detail-data-body-list">&nbsp;<%=platformMemvalue%>%</td>
																							<td width="17%" class="detail-data-body-list">&nbsp;<%=platMemMax%></td>
																							<td width="20%" class="detail-data-body-list"> &nbsp;<%=platMemAvg%></td>
																						</tr>
																						
																					</table>

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
																																	<b>平台内存明细</b>
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
																													<td valign=top>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td valign=top>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					
																																					<tr>
																																						<td colspan=1>
																																							 <div id="platmemory">
							                                                                                                                                   
						                                                                                                                                       </div>
						                                                                                                                                       
						                                                                                                                                       <script type="text/javascript"
							                                                                                                                                   src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                                       <script type="text/javascript">
						                                                                                                                                       <% if(!platMemData.equals("0")){ %>	
						                                                                                                                                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","420", "278", "8", "#FFFFFF");
						                                                                                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netmemory_settings.xml"));
						                                                                                                                                             so.addVariable("chart_data","<%=platMemData%>");
						                                                                                                                                             so.write("platmemory");
						                                                                                                                                          <%}else{%>
																			                                                                                          var _div=document.getElementById("platmemory");
																			                                                                                          var img=document.createElement("img");
																			                                                                                              img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																			                                                                                              _div.appendChild(img);
																			                                                                                       <%}%>
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
																																										<b>平台内存总量</b>：<%=platformMemPhysical %></td>
																																								</tr>
																																							</table>

																																						</td>
																																					</tr>
																																					<tr height=15>
																																					
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
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="Area_Disk_Domino">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Disk_Domino.swf?ipadress=<%=ipaddress%>", "Area_Disk_Domino", "346", "360", "8", "#ffffff");
																					so.write("Area_Disk_Domino");
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
																																	<b>磁盘利用率详情</b>
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
																																						<td align=center colspan=1>
																																						<!-- 	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>disk.png">
																																						-->
																																						<div id="diskUtil">
																																								<strong></strong>
																																							</div>

																																							<script type="text/javascript">
																																							 <% if(!dataStr.equals("0")){ %>
		                                                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "480", "210", "8", "#FFFFFF");
		                                                                                                                                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                                                                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
	                                                                                                                                                                 //	so.addVariable("data_file",  escape("<%=rootPath%>/amcharts_data/memorypercent_data.xml"));
	                                                                                                                                                                   so.addVariable("chart_data", "<%=dataStr%>");
		                                                                                                                                                               so.addVariable("preloader_color", "#999999");
		                                                                                                                                                               so.write("diskUtil");
		                                                                                                                                                               <%}else{%>
																			                                                                                          var _div=document.getElementById("diskUtil");
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
																																										磁盘名
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										总容量
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										空闲容量
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										利用率
																																									</td>

																																								</tr>
																																								
																																								
																																									<%for(int i=0;i<diskList.size();i++){ 
																																									DominoDisk dominoDisk=(DominoDisk)diskList.get(i);
																																									%>
																																									<tr>
																																									<td class="detail-data-body-list" align=center height="29;width=10%"><%=dominoDisk.getDiskname()%></td>
																																								
																																									<td class="detail-data-body-list" align=center height="29;width=10%"><%=dominoDisk.getDisksize()%></td>
																																									<td class="detail-data-body-list" align=center height="29;width=10%"><%=dominoDisk.getDiskfree()%></td>
																																									<td class="detail-data-body-list" align=center height="29;width=10%"><%=dominoDisk.getDiskusedpctutil()%>%</td>
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
								<td class="td-container-main-tool" width="15%">
										<jsp:include page="/include/dominoToolbar.jsp">
										<jsp:param value="<%=ipaddress%>" name="ipaddress" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="domino" name="category" />
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