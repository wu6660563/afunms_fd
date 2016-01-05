<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
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
<%@ page import="com.afunms.event.model.*"%>
<%@ page import="com.afunms.system.model.User"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
  //String rootPath = request.getContextPath();;
  
  String tmp = request.getParameter("id"); 
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		double cpuvalue = 0;
		String pingconavg ="0";
		String collecttime = null;
		String sysuptime = null;
		String sysservices = null;
		String sysdescr = null;
		
    	HostNodeDao hostdao = new HostNodeDao();
    	List hostlist = hostdao.loadHost();
    	
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
        
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
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
		
        //request.setAttribute("vector", vector);
        request.setAttribute("id", tmp);
        request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));




  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
Hashtable hash = (Hashtable)request.getAttribute("hash");
Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");

double avgpingcon = (Double)request.getAttribute("pingconavg");

  String rootPath = request.getContextPath();  
  
  //ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用率",avgpingcon);
  dpd.setValue("不可用率",100 - avgpingcon);
  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
  //if(item1.getSingleResult()!=-1)
  //{
     //responseTime = item1.getSingleResult() + " ms";
  
     //SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
     //if(item2!=null&&item2.getSingleResult()!=-1)
         //chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
         chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
  
     //SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
     //if(item3!=null&&item3.getSingleResult()!=-1)
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
  //}
  //else
     //responseTime = "无响应"; 
     
String[] iproutertype={"","","","direct(3)","indirect(4)"};
String[] iprouterproto={"","other(1)","local(2)","netmgmt(3)","icmp(4)","egp(5)","ggp(6)","hello(7)","rip(8)","is-is(9)","es-is(10)","ciscoIgrp(11)","bbnSpfIgp(12)","ospf(13)","bgp(14)"};
Vector iproutervector = (Vector)request.getAttribute("vector");	

String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");

int level1 = Integer.parseInt(request.getAttribute("level1")+"");
int _status = Integer.parseInt(request.getAttribute("status")+"");

String level1str="";
String level2str="";
String level3str="";
if(level1 == 1){
	level1str = "selected";
}else if(level1 == 2){
	level2str = "selected";
}else if(level1 == 3){
	level3str = "selected";	
}

String status0str="";
String status1str="";
String status2str="";
if(_status == 0){
	status0str = "selected";
}else if(_status == 1){
	status1str = "selected";
}else if(_status == 2){
	status2str = "selected";	
}

EventList eventdetail = (EventList)request.getAttribute("eventdetail");
EventReport eventreport = (EventReport)request.getAttribute("eventreport");

java.text.SimpleDateFormat sdf0 = new java.text.SimpleDateFormat("yyyy-MM-dd");         
String nowtime = sdf0.format(new Date());
User vo = (User)session.getAttribute(SessionConstant.CURRENT_USER);
String username = vo.getName();			  	   
%>
<html>
<head>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	
function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/monitor.do?action=accfi";
	mainForm.submit();
}
function fiReport(eventid){
	subforms = document.forms[0];
	subforms.operate.value="fireport";
	subforms.eventid.value=eventid;
	subforms.submit();
}
function viewReport(eventid){
	subforms = document.forms[0];
	subforms.operate.value="viewreport";
	subforms.eventid.value=eventid;
	subforms.submit();
}
   	function doReport(){
   		mainForm.eventid.value=<%=eventdetail.getId()%>;
		mainForm.action="<%=rootPath%>/monitor.do?action=doreport";
		mainForm.submit();   	
   	}  
  function query()
  {  
     mainForm.action = "<%=rootPath%>/monitor.do?action=netevent";
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
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="eventid">
<input type=hidden name="id" value=<%=tmp%>>
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=950>
	<tr>
		<td width="200" valign=top align=center>
			<table width="90%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none w align=center border=1 algin="center">
                        <tbody>                     										                        								
                    		<tr algin="left" valign="center">                      														
                      			<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;拓扑</td>
                    		</tr>
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/topology/network/index.jsp","fullScreenWindow", "toolbar=no,height="+ window.screen.height + ","+"width=" + (window.screen.width-8) + ",scrollbars=no"+"screenX=0,screenY=0")'>网络拓扑</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/topology/server/index.jsp","fullScreenWindow", "toolbar=no,height="+ window.screen.height + ","+"width=" + (window.screen.width-8) + ",scrollbars=no"+"screenX=0,screenY=0")'>主机服务器</td>
                    		</tr>                    										                 										                      								
            		</tbody>
            		</table>  
            		<br>
			<table width="90%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none w align=center border=1 algin="center">
                        <tbody>                     										                        								
                    		<tr algin="left" valign="center">                      														
                      			<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;设备维护</td>
                    		</tr>
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_add">添加设备</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=list">设备列表</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=list">IP/MAC</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/portconfig.do?action=list">端口配置</td>
                    		</tr>                     		                    		                   										                 										                      								
            		</tbody>
            		</table> 
            		<br>
			<table width="90%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none w align=center border=1 algin="center">
                        <tbody>                     										                        								
                    		<tr algin="left" valign="center">                      														
                      			<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;性能监视</td>
                    		</tr>
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=monitornodelist">监视对象一览表</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=list">指标全局阀值一览表</td>
                    		</tr> 
                    		<tr align="left" valign="center"> 
                    			<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=list">TopN</td>
                    		</tr>                     		                   										                 										                      								
            		</tbody>
            		</table>             		           				
		
		
		</td>
		<td bgcolor="#cedefa" align="center">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="0"></td>
		<td align="center">
		<table width="100%" border=0 cellpadding=0 cellspacing=0>
			<tr>
			  	<td height=385 bgcolor="#FFFFFF" valign="top">
			  	
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                  				<tr>
                    					<td>
								<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        					<tbody>
                          						<tr>
                								<td width="426" align="left" valign="top" class=dashLeft>
                									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                    										<tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="2" valign=center nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">设备信息</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;设备标签:</td>
                      											<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;系统名称:</td>
                      											<td width="70%"><%=host.getSysName()%> [<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>
                    										</tr>                    										
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap bgcolor="#EBF4F7" >&nbsp;状态:</td>
                      											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>"><%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;IP地址:</td>
                      											<td width="70%"><%=host.getIpAddress()%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap bgcolor="#EBF4F7" >&nbsp;子网掩码:</td>
                      											<td><%=host.getNetMask()%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;类别:</td>
                      											<td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;类型:</td>
                      											<td width="70%"><%=host.getType()%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;系统描述:</td>
                      											<td width="70%"><%=sysdescr%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;设备启动时间:</td>
                      											<td><%=sysuptime%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=center nowrap class=txtGlobal>&nbsp;数据采集时间:</td>
                      											<td width="70%"><%=collecttime%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;设备启动服务数:</td>
                      											<td><%=sysservices%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;设备MAC:</td>
                      											<td><%=host.getMac()%></td>
                    										</tr>    
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;系统OID:</td>
                      											<td><%=host.getSysOid()%></td>
                    										</tr>							
                									</table>
										</td>																					
                								<td width="200" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>
                          									<tr>
                											<td width="400" align="left" valign="middle" class=dashLeft>
                												<table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=1 algin="center" >
                    													<tr class="topNameRight">
                      														<td height="11" rowspan="2">&nbsp;</td>
                      														<td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">今天的可用率</td>
                      														<td height="11" rowspan="2">&nbsp;</td>
                    													</tr>
                    													<tr class="topNameRight">
                      														<td height="124" align="center"><img src="<%=rootPath%>/artist?series_key=<%=chart1%>"></td>
                    													</tr>
                    													<tr class="topNameRight">
                      														<td height="7">&nbsp;</td>
                      														<td height="7">&nbsp;</td>
                      														<td height="7">&nbsp;</td>
                    													</tr>
                												</table>				
														<br>
														<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=1 algin="center">
                    													<tr class="topNameRight">
                      														<td height="11" rowspan="2">&nbsp;</td>
                      														<td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">CPU利用率</td>
                      														<td height="11" rowspan="2">&nbsp;</td>
                    													</tr>
                  													<tr>
                    														<td height="44">
                    															<div align="center" class="txtResponseTime">
																	<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none width=90% align=center border=1 algin="center" height=95%>
                      																<tr>
                        																<td align="center" valign="middle" height='30'>
																				<%if(chart2!=null){%>                        
                        																		<img src="<%=rootPath%>/artist?series_key=<%=chart2%>">
																				<%} else out.print("无数据111!");%>                        
                        																</td>
                      																</tr>
                    															</table>                    
                    		    													</div>
                    		    												</td>
                    														<td> </td>
                  													</tr>
                												</table>											  
                											</td>
              											</tr>
            										</tbody>
            										</table>
            									</td>
                								<td width="200" align="left" valign="top" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>                     										                        								
                    										<tr algin="left" valign="center">                      														
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;工具</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/cancelMng.gif">&nbsp;<a href="<%=rootPath%>/tool/ping.jsp?ipaddress=<%=host.getIpAddress()%>" target=_blank>取消管理</td>
                    										</tr> 
                    										<!--<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/delete2.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=telnet&ipaddress=<%=host.getIpAddress()%>","onetelnet", "height=0, width= 0, top=0, left= 0")'>SSH</td>
                    										</tr>-->                    										
                    										<tr align="left" valign="center">
                      											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/nodelist.png">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=telnet&ipaddress=<%=host.getIpAddress()%>","onetelnet", "height=0, width= 0, top=0, left= 0")'>Telnet</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left"><img src="<%=rootPath%>/resource/image/topo/icon_ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/ping.jsp?ipaddress=<%=host.getIpAddress()%>","oneping", "height=400, width= 500, top=300, left=100")'>Ping</td>
                    										</tr> 
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/topo/traceroute.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=host.getIpAddress()%>","newtracerouter", "height=400, width= 500, top=300, left=100")'>路由跟踪</td>
                    										</tr>                     										                      								
            										</tbody>
            										</table>
            										
											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>                     										                        								
                    										<tr algin="left" valign="center">                      														
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;配置</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">修改设备标签</td>
                    										</tr> 
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">修改系统组属性</td>
                    										</tr>                    										
                    										<tr align="left" valign="center">
                      											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/modifySnmp32.gif" width=16>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">修改SNMP参数</td>
                    										</tr>                    										                      								
            										</tbody>
            										</table> 
            										
											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>                     										                        								
                    										<tr algin="left" valign="center">                      														
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;性能监视配置</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/moid.do?action=ready_editmoids&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">配置性能监视项</td>
                    										</tr> 
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/moid.do?action=showallmoids&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">监视指标阀值配置</td>
                    										</tr>                    										                 										                      								
            										</tbody>
            										</table>            										           										
            									</td>            									
            								</tr>
                						</tbody>
                						</table>
                					</td>
                				</tr>
                				<tr>
                					<td>

					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">

                  				<tr>
                    					<td>
            							<table width="100%" border="0" cellpadding="0" cellspacing="0">
              								<tr> 
                								<td width="30" height="22">&nbsp;</td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=tmp%>"><font color="#397dbd">流速信息</font></a></div></td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netcpu&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">性能信息</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netarp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">ARP信息</font></a></div></td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netroute&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">路由表信息</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netping&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">连通率</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netevent&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#FFFFFF">告警信息</font></a></div></td>															
                								<td width="120" align=right>&nbsp;&nbsp;</td>
              								</tr>
            							</table>                                 					
                      
                  					</td>
                  				</tr>
                  				<tr> 
                				<td>
                					<table width="100%" border="0" cellpadding="0" cellspacing="1">
                    						<tr align="left" bgcolor="397dbd"> 
                      							<td height="23" ><b><font color="#FFFFFF">&nbsp;设备告警信息&nbsp;&nbsp;</font></b></td>
                    						</tr>
                  					</table>
                  				</td>
                  				</tr>
  						<tr>
  						<td colspan=5>
	

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
	<input type="button" name="submitss" value="查询" onclick="query()">
						</td>
						</tr>
						
                    <tr bgcolor="DEEBF7"> 
                      <td>
  <table class="tab3"  cellSpacing="0"  cellPadding="0" border=0>
<%
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	Date cc = eventdetail.getRecordtime().getTime();
  	Integer eventid = eventdetail.getId();
  	String eventlocation = eventdetail.getEventlocation();
  	String content = eventdetail.getContent();
  	String level = String.valueOf(eventdetail.getLevel1());
  	String status = String.valueOf(eventdetail.getManagesign());
  	String s = status;
  	String act="处理报告";
  	if("1".equals(level)){
  		level="普通事件";
  	}
  	if("2".equals(level)){
  		level="紧急事件";
  	}
   	  	if("0".equals(status)){
  		status = "未处理";
  	}
  	if("1".equals(status)){
  		status = "处理中";  	
  	}
  	if("2".equals(status)){
  	  	status = "处理完成";
  	}
  	String rptman = eventdetail.getReportman();
  	String rtime1 = _sdf.format(cc);
%>
<% if (eventdetail.getManagesign() == 0){
%>
  <tr>
  <td colspan=2><br>该事件尚未被任何管理员受理，点击确定表明您已经了解该情况并且准备受理<br><br></td>
  </tr>	
  <tr>
        <td width="20%" align=right>事件等级</td>
        <td width="80%">&nbsp;&nbsp;<%=level%></td>
  </tr>
  <tr>     
    	<td width="20%" align=right>事件描述</td>
    	<td width="80%">&nbsp;&nbsp;<textarea name="content" cols=50 rows=5 readonly><%=content%></textarea></td>
  </tr>
  <tr>
	<td align=right>登记日期</td>
	<td>&nbsp;&nbsp;<%=rtime1%></td>
  </tr>
  <tr>
    	<td align=right>登记人</td>
    	<td>&nbsp;&nbsp;<%=rptman%></td>
   </tr>


 <tr>
       <td colspan=2 align=center>
       			<input type ="button" value="接受处理" class="formStylebutton" onclick="accEvent('<%=eventid%>')">
       			&nbsp;&nbsp;<input type=reset class="formStylebutton" style="width:50" value="返 回" onclick="javascript:history.back(1)">
       			<br><br>
       </td>
 </tr> 
 <%
 }
 %>
<% if (eventdetail.getManagesign() == 1){
%>
     	<tr width ="100%">
     		<td colspan=2><br>如果您已经解决了该问题，现在您可以填写事件处理报告了<br><br></td>
     	</tr>
	<tr>
		<td align=right>解决时间</td>
		<td>&nbsp;&nbsp;
		<input type="text" name="deal_time" size="10" value="<%=nowtime%>">
		<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].deal_time,null,0,330)">
		<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
		</td>
        </tr>
	<tr>
		<td align=right>报告内容 </td>
		<td>&nbsp;&nbsp;<textarea name="report_content" rows="10" cols="100"><%=eventdetail.getContent()%></textarea></td>
	</tr>
	<tr>
		<td align=right>填写人</td>
		<td>&nbsp;&nbsp;<input type="text" name="report_man" value="<%=username%>"></td>
	</tr>
		
     	<tr><td align=center colspan=2>
     		<br><input type="button" value="确定" onclick="doReport()">
     		<input type="button" value="取消" onclick="doBack()"><br><br></td>
     	</tr>
 <%
 }
 %>
 <% if (eventdetail.getManagesign() == 2){
%>
     	<tr width ="100%">
     		<td colspan=2><br>该问题已经解决，完成报告如下<br><br></td>
     	</tr>
	<tr>
			<%
				String deal_time = request.getAttribute("deal_time").toString();
				String report_time = request.getAttribute("report_time").toString();
			%>	
		<td align=right>解决时间</td>
		<td>&nbsp;&nbsp;<%=deal_time%>
		</td>
        </tr>
	<tr>
		<td align=right>报告内容 </td>
		<td>&nbsp;&nbsp;<textarea name="report_content" rows="10" cols="100" readonly><%=eventreport.getReport_content()%></textarea></td>
	</tr>
	<tr>
		<td align=right>填写人</td>
		<td>&nbsp;&nbsp;<%=eventreport.getReport_man()%></td>
	</tr>
	<tr>
		<td align=right>填写日期</td>
		<td>&nbsp;&nbsp;<%=report_time%></td>
	</tr>	
		
     	<tr><td align=center colspan=2>
     		<br><input type=reset class="formStylebutton" style="width:50" value="返 回" onclick="javascript:history.back(1)">
     		<br><br></td>
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
	
	</tr>
	</table> 
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>	                                                                            	                                                                                                 