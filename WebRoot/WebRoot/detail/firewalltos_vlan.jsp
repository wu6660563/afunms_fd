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

<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>

<%@page import="org.jfree.data.category.CategoryDataset"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="org.jfree.chart.*"%>
<%@page import="org.jfree.chart.plot.*"%>
<% 
  	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id");
  	Hashtable conns = new Hashtable();
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		double cpuvalue = 0;
		String pingconavg ="0";
		String collecttime = null;
		String sysuptime = null;
		String sysservices = null;
		String sysdescr = null;
	Vector vlans = new Vector();
    	
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
        String starttime = time[0];
        String endtime = time[1];
        
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		int currentcon = 0;
		int newconpersec = 0;
		if(ipAllData != null){
			conns = (Hashtable)ipAllData.get("conns");
			if(conns != null && conns.size()>0){
				if(conns.containsKey("currentcon"))currentcon=(Integer)conns.get("currentcon");
				if(conns.containsKey("newconpersec"))newconpersec=(Integer)conns.get("newconpersec");
			}
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
     
     	vlans = (Vector)request.getAttribute("vector");	
         			  	   
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
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

}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=950>
	<tr>
		<td width="200" valign=top align=center>
	
			<%=menuTable %>
	</td>
		<td bgcolor="#cedefa" align="center" valign=top>
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="0"></td>
		<td align="center" valign=top>
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
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;当前连接数:</td>
                      											<td><%=currentcon%></td>
                    										</tr>
                    										<tr>
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;新建连接数/秒:</td>
                      											<td><%=newconpersec%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;设备MAC:</td>
                      											<td><%=host.getMac()%></td>
                    										</tr>    
                    										<tr>
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
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallcpu&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">性能信息</font></a></div></td>
										<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallarpproxcy&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&trustflag=0"><font color="#397dbd">ARPProxy</font></a></div></td>
										<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallvlan&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&trustflag=0"><font color="#FFFFFF">Vlan表</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallarp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">ARP信息</font></a></div></td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallroute&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">路由表信息</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallping&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">连通率</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewalllogin&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">登陆信息</font></a></div></td>															
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=firewallevent&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">告警信息</font></a></div></td>
                								<td align=right>&nbsp;&nbsp;</td>
              								</tr>
            							</table>
            							
           <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr> 
                <td valign="top">
                <table width="100%" height="1" border="1" cellpadding="0" cellspacing="0" bordercolor="397DBD">
                    <tr align="left" bgcolor="397dbd"> 
                      <td width="50%" height="23">&nbsp;<b><font color="#FFFFFF">>> VLAN信息</b></td> 
                    </tr>
                    <tr> 
                      <td align=center>
			<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
                        <tbody> 
                        		<tr style="background-color: #ECECEC;" height=28>	
          					<td align=center width=10%>索引</td>
          					<td align=center width=10%>Valn Id</td>
            					<td align=center width=30%>状态</td>
            					<td align=center width=30%>端口</td>
            				</tr>                          
                    <%
                    	if(vlans != null && vlans.size()>0){
                    		for(int k=0;k<vlans.size();k++){
                    			Hashtable vlan = (Hashtable)vlans.get(k);
                    %>                                           										                        								
                    		<tr height=25 align="left" valign="center" class="othertr"  <%=onmouseoverstyle%>> 
                    			<td><%=(String)vlan.get("index")%></td>
                      			<td valign="center">&nbsp;&nbsp;<%=(String)vlan.get("valnid")%></td>
                      			<td valign="center">&nbsp;&nbsp;<%=(String)vlan.get("status")%></td>
                      			<td valign="center">&nbsp;&nbsp;<%=(String)vlan.get("ports")%></td>
                   <%
                   		}
                   	}
                   %>                     		                   										                 										                      								
            		</tbody>
            		</table>             		          		                     
                      </td>
                    </tr>
                   <%
                   
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
</form>
</BODY>
</HTML>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               