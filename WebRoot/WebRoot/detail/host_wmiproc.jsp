<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@include file="/include/globe.inc"%>
<%@include file="/include/globeChinese.inc"%>
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
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%
  response.setHeader("Cache-Control","no-cache"); 
  response.setHeader("Pragma","no-cache"); 
  response.setDateHeader("Expires",0); 
%> 

<%
	String _flag = (String)request.getAttribute("flag");
	String runmodel = PollingEngine.getCollectwebflag();
	String layer = (String)request.getAttribute("layer");
	if(layer == null)
	layer="";
	String[] processItem={"Name","Count","Type","CpuTime","MemoryUtilization","Memory","Status"};
	String[] processItemch={"��������","���̸���","��������","cpuʱ��","�ڴ�ռ����","�ڴ�ռ����","��ǰ״̬"};
	String[] second_processItem={"Pid","Name","Type","CpuTime","Memory","MemoryUtilization","Status"};
	String[] second_processItemch={"����ID","��������","��������","cpuʱ��","�ڴ�ռ����","�ڴ�ռ����","��ǰ״̬"};
	Vector detailVect = (Vector)request.getAttribute("detailVect");
	List list = (List)request.getAttribute("list");//��method�л�ȡprocess list��Ϣ   
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	
   	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	String ipaddress = host.getIpAddress();
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
    Vector systemV = null;
    Vector cpuV = null;
    Vector pingData = null;
   	try{	
   		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData != null){
				//�õ�ϵͳ����ʱ��
				systemV = (Vector)ipAllData.get("system");
				cpuV = (Vector)ipAllData.get("cpu");
			}
			pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		}else{
       		//�ɼ�������Ƿ���ģʽ
       		NodeUtil nodeUtil = new NodeUtil();
	       	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	       	//�õ�cpu��Ϣ
	       	CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			cpuV = cpuInfoService.getCpuInfo(); 
			//�õ�ϵͳ����ʱ��
			SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			systemV = systemInfoService.getSystemInfo();
			//�õ����ݲɼ�ʱ��
			pingData = new PingInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()).getPingInfo();
       	}
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
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
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
       	request.setAttribute("id", tmp);
       	request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));
		
	}catch(Exception e){
		e.printStackTrace();
	}

	double avgpingcon = (Double)request.getAttribute("pingconavg");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

  	String rootPath = request.getContextPath();  
  
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
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para+"&flag=1";
} 
function openwin3(operate,index,ifname) 
{	
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
        mainForm.submit();
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
function order(para,layer){
	mainForm.orderflag.value = para;
	mainForm.id.value = <%=tmp%>;
	//alert(mainForm.id.value);
	//mainForm.ipaddress.value = "<%=host.getIpAddress()%>";
	mainForm.action = "<%=rootPath%>/monitor.do?action=hostproc&id=<%=host.getId()%>&orderflag=" + para+"&layer="+layer+"&flag=<%=_flag%>"; 
	mainForm.submit();
	//location.href="<%=rootPath%>/monitor.do?action=hostproc&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
}
function doDetail(pro_name,layer)
{
	mainForm.action='<%=rootPath%>/monitor.do?action=hostproc&&id=<%=tmp%>&&processName='+pro_name+"&&layer="+layer;
	mainForm.submit();
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

//���Ӳ˵�	
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
	
	document.getElementById('windowsDetailTitle-3').className='detail-data-title';
	document.getElementById('windowsDetailTitle-3').onmouseover="this.className='detail-data-title'";
	document.getElementById('windowsDetailTitle-3').onmouseout="this.className='detail-data-title'";
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
												<jsp:param name="rootPath" value="<%=rootPath%>" />
												<jsp:param name="tmp" value="<%=tmp%>" />
												<jsp:param name="collecttime" value="<%=collecttime%>" />
												<jsp:param name="sysuptime" value="<%=sysuptime%>" />
												<jsp:param name="sysdescr" value="<%=sysdescr%>" />
												<jsp:param name="percent1" value="<%=percent1%>" />
												<jsp:param name="percent2" value="<%=percent2%>" />
												<jsp:param name="cpuper" value="<%=cpuper%>" />
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
																	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#ECECEC">
																		<tr>
																			<%
																				if (detailVect != null) {
																					System.out.println("biaoti yes");
																					for (int i = 0; i < second_processItemch.length; i++) {
																						String title = second_processItemch[i];
																						if (i >= 3 && i <= 5) {
																			%>
																			<td align="center">
																				<input type="button" onClick="order('<%=second_processItem[i]%>','two')" Class="button" value="<%=second_processItemch[i]%>">
																			</td>
																			<%
																				} else {
																			%>
																			<td align="center"><%=title%></td>
																			<%
																				}
																					}
																				} else {
																					System.out.println("biaoti no");//detalHash wei kong
																					for (int i = 0; i < processItemch.length; i++) {
																						String title = processItemch[i];
																						if (i >= 3 && i <= 5) {
																			%>
																			<td align="center">
																				<input type="button" onClick="order('<%=processItem[i]%>','one')" Class="button" value="<%=processItemch[i]%>">
																			</td>
																			<%
																				} else {
																			%>
																			<td align="center"><%=title%></td>
																			<%
																				}
																					}
																				}
																			%>
																			<td align="center">
																				�鿴��ϸ���
																			</td>
																		</tr>
								
																		<%
																			if (detailVect == null) {
																				System.out.println("neirong no");
																				Hashtable phash;
																				ProcessInfo processInfo = null; 
																				for (int i = 0; i < list.size(); i++) {
																					processInfo = (ProcessInfo) list.get(i);
																		%>
																		<tr height="28" bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getName()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getCount()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getType()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getCpuTime()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getMemoryUtilization()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getMemory()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getStatus()%></td>
																			<td class="detail-data-body-list" align="center">
																				<a href="javascript:doDetail('<%=processInfo.getName()%>','two')">�鿴����</a>
																			</td>
																		</tr>
																		<%
																			phash = null;
																				}
																			} else {
																				System.out.println("neirong yes");
																				for (int i = 0; i < detailVect.size(); i++) {
																					ProcessInfo processInfo = (ProcessInfo) detailVect.get(i);
																		%>
																		<tr height="28" bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getPid()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getName()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getType()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getCpuTime()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getMemory()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getMemoryUtilization()%></td>
																			<td class="detail-data-body-list" align="center"><%=processInfo.getStatus()%></td>
																			<td class="detail-data-body-list" align="center">
																				<input type="button" Class="button" onclick='window.open("<%=rootPath%>/monitor.do?action=look_prohis&ipaddress=<%=ipaddress%>&pid=<%=processInfo.getName()%>&pname=<%=processInfo.getName()%>","newWindow", "toolbar=no,height=300, width=830, top=300, left=200")' value="�鿴��ʷ">
																			</td>
																		</tr>
																		<%
																			}
																			}
																		%>
																	</table>
																	
																</td>
																<td>
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
							<td class="td-container-main-tool" width=15%>
								<table class="container-main-tool">
								    <jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
										<jsp:param value="<%=tmp%>" name="tmp"/>
										<jsp:param value="host" name="category"/>
										<jsp:param value="windows" name="subtype"/>
									</jsp:include>
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