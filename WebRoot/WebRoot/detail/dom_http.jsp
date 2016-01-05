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
<%@page import="com.afunms.application.model.DominoMail"%>
<%@page import="com.afunms.application.dao.DominoEmailDao"%>
<%@page import="com.afunms.application.dao.DominoHttpDao"%>
<%@page import="com.afunms.application.model.DominoHttp"%>
<%@page import="com.afunms.temp.dao.PingTempDao"%>
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
	DominoHttp http=null;
	DominoMem mem=null;
	DominoServer dominoServer=null;
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getDominodata().get(
				ipaddress);
		
		if (ipAllData != null) {
		Pingcollectdata pingdata = (Pingcollectdata)ipAllData.get("Ping");
		 pingvalue = pingdata.getThevalue();
		if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
		http=(DominoHttp)ipAllData.get("HTTP");
		diskList = (List) ipAllData.get("Disk");
			 dominoServer= (DominoServer) ipAllData.get("Server");
			
			//当前物理和平台物理内存利用率
			 mem = (DominoMem) ipAllData.get("Mem");
			
	    ldap=(DominoLdap)ipAllData.get("LDAP");
		}
		

	} else {
	Pingcollectdata pingdata= (new PingTempDao()).getPingInfo("domPing", "ConnectUtilization");
	pingvalue=pingdata.getThevalue();
	DominoHttpDao httpDao=new DominoHttpDao();
	http=(DominoHttp)httpDao.findByIp(ipaddress);
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
		try {
			//ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "DominoPing", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash!=null&&ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	
	
	 }
	 if(avgcpu.equals("")||avgcpu==null){
	 avgcpu ="0";
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
	document.getElementById('hostDetailTitle-3').className='detail-data-title';
	document.getElementById('hostDetailTitle-3').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostDetailTitle-3').onmouseout="this.className='detail-data-title'";
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
										         <tr bgcolor="#FFFFFF"> 
                                                    <td height="28"  align="center" class="detail-data-body-title">序号</td>
			                                        <td height="28"  align="center" class="detail-data-body-title">名称</td>
				                                    <td height="28"  align="center" class="detail-data-body-title">数值</td> 
                                                </tr>
                                                <%if(http!=null){ %>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">1</td>
			                                        <td height=25 class="detail-data-body-list" align="center">HTTP连接请求总数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpAccept() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">2</td>
			                                        <td height=25 class="detail-data-body-list" align="center">被拒绝的HTTP连接请求数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpRefused() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">3</td>
			                                        <td height=25 class="detail-data-body-list" align="center">当前HTTP连接数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpCurrentCon() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">4</td>
			                                        <td height=25 class="detail-data-body-list" align="center">最大HTTP连接数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpMaxCon()%></td> 
                                                </tr>
										         
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">5</td>
			                                        <td height=25 class="detail-data-body-list" align="center">峰值HTTP连接数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpPeakCon() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">6</td>
			                                        <td height=25 class="detail-data-body-list" align="center">HTTP请求数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerRequest() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">7</td>
			                                        <td height=25 class="detail-data-body-list" align="center">HTTP请求时间</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerRequestTime() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">8</td>
			                                        <td height=25 class="detail-data-body-list" align="center">Worker总计读字节</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerBytesRead() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">9</td>
			                                        <td height=25 class="detail-data-body-list" align="center">Worker总计写字节</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerBytesWritten() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">10</td>
			                                        <td height=25 class="detail-data-body-list" align="center">Worker总计处理的请求总数</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerRequestProcess()%></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">11</td>
			                                        <td height=25 class="detail-data-body-list" align="center">Worker总计请求时间</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpWorkerTotalRequest() %></td> 
                                                </tr>
										         <tr bgcolor="#FFFFFF">
                                                    <td height=25 class="detail-data-body-list" align="center">11</td>
			                                        <td height=25 class="detail-data-body-list" align="center">HTTP错误URL</td>
				                                    <td height=25 class="detail-data-body-list" align="center"><%=http.getHttpErrorUrl() %></td> 
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