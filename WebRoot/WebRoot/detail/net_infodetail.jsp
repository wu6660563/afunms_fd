<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		double cpuvalue = 0;
		String pingconavg ="0";
		String collecttime = null;
		String sysuptime = null;
		String sysservices = null;
		String sysdescr = null;
		
    	String tmp = request.getParameter("id");
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
			//�õ�ϵͳ����ʱ��
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
		
        request.setAttribute("vector", vector);
        request.setAttribute("id", tmp);
        request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));




  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"������","��������","��ǰ������","���������"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"�豸��","�豸����ʱ��","�豸��ϵ","�豸λ��","�豸����","�豸����"};
Hashtable hash = (Hashtable)request.getAttribute("hash");
Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");

double avgpingcon = (Double)request.getAttribute("pingconavg");

  String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  //ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("������",avgpingcon);
  dpd.setValue("��������",100 - avgpingcon);
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
     //responseTime = "����Ӧ"; 
     
     Vector ifvector = (Vector)request.getAttribute("vector"); 				  	   
%>
<html>
<head>
<script language="javascript">
function showdiv(id)
{
	var obj=document.getElementsByName("tab");
    j=0;
	var tagname=document.getElementsByTagName("DIV");
	for(var i=0;i<tagname.length;i++)
	{
		if(tagname[i].id==id)
		{
		   obj[j].className="selectedtab";
		   tagname[i].style.display="";
		   j++;
		}
		else if(tagname[i].id.indexOf("contentData")==0)
		{
		   obj[j].className="1";
		   j++;
		   tagname[i].style.display="none";
		}
	}
}
  function startTelnet() {
  try {

  
  	var href = 'telnet://<%=host.getIpAddress()%>';
  	newWin = window.open(href, "telnet", "height=0, width= 0, top=0, left= 0");
  	setTimeout(closeTelnet(), 5000);
  	setTimeout(closeOldWin(), 2000);  
  	parent.close();    
  } catch(e) {
  }
  }
  function closeOldWin() {
  	try {
  		window.close();
  	} catch (e) {
  	}
  }
  function closeTelnet() {
  	try {
  		newWin.close();
  	} catch (e) {
  	}
  }
</script>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id">
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
                      											<td height="28" colspan="2" valign=center nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">�豸��Ϣ</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;�豸��ǩ:</td>
                      											<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>�޸�</a>]</td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;ϵͳ����:</td>
                      											<td width="70%"><%=host.getSysName()%> [<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>�޸�</a>]</td>
                    										</tr>                    										
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap bgcolor="#EBF4F7" >&nbsp;״̬:</td>
                      											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>"><%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;IP��ַ:</td>
                      											<td width="70%"><%=host.getIpAddress()%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal align="left" nowrap bgcolor="#EBF4F7" >&nbsp;��������:</td>
                      											<td><%=host.getNetMask()%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;���:</td>
                      											<td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;����:</td>
                      											<td width="70%"><%=host.getType()%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;ϵͳ����:</td>
                      											<td width="70%"><%=sysdescr%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;�豸����ʱ��:</td>
                      											<td><%=sysuptime%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=center nowrap class=txtGlobal>&nbsp;���ݲɼ�ʱ��:</td>
                      											<td width="70%"><%=collecttime%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td height="29" class=txtGlobal valign=center nowrap bgcolor="#EBF4F7" >&nbsp;�豸����:</td>
                      											<td><%=sysservices%></td>
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
                      														<td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">����Ŀ�����</td>
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
                      														<td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">CPU������</td>
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
																				<%} else out.print("������111!");%>                        
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
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;����</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/cancelMng.gif">&nbsp;<a href="<%=rootPath%>/tool/ping.jsp?ipaddress=<%=host.getIpAddress()%>" target=_blank>ȡ������</td>
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
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/topo/traceroute.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=host.getIpAddress()%>","newtracerouter", "height=400, width= 500, top=300, left=100")'>·�ɸ���</td>
                    										</tr>  
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/topo/traceroute.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=host.getIpAddress()%>","newtracerouter", "height=400, width= 500, top=300, left=100")'>�豸���</td>
                    										</tr>                      										                   										                      								
            										</tbody>
            										</table>
            										
											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>                     										                        								
                    										<tr algin="left" valign="center">                      														
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;����</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">�޸��豸��ǩ</td>
                    										</tr> 
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">�޸�ϵͳ������</td>
                    										</tr>                    										
                    										<tr align="left" valign="center">
                      											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/modifySnmp32.gif" width=16>&nbsp;<a href="<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">�޸�SNMP����</td>
                    										</tr>                    										                      								
            										</tbody>
            										</table> 
            										
											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        								<tbody>                     										                        								
                    										<tr algin="left" valign="center">                      														
                      											<td height="28" align="left" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">&nbsp;���ܼ�������</td>
                    										</tr>
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/editicon.gif" border=0>&nbsp;<a href="<%=rootPath%>/moid.do?action=ready_editmoids&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">�������ܼ�����</td>
                    										</tr> 
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<img src="<%=rootPath%>/resource/image/manageDev.gif" border=0>&nbsp;<a href="<%=rootPath%>/moid.do?action=showallmoids&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>">����ָ�귧ֵ����</td>
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
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian.gif">
											<div align="center"><a href="<%=rootPath%>/detail/net_interface.jsp?id=<%=tmp%>"><font color="#FFFFFF">������Ϣ</font></a></div></td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netcpu&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">������Ϣ</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netarp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">ARP��Ϣ</font></a></div></td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netroute&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">·�ɱ���Ϣ</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netping&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">��ͨ��</font></a></div></td>	
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/monitor.do?action=netevent&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">�¼���Ϣ</font></a></div></td>															
                								<td width="120" align=right>&nbsp;&nbsp;</td>
              								</tr>
            							</table>
            							
                      <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
  <tr align="center"> 
    <td rowspan="2" width="6%" align="left">����</td>
    <td rowspan="2" width="9%" align="left">����</td>
    <td rowspan="2" width="9%" align="left">����Ӧ��</td>
    <td rowspan="2" width="11%">ÿ���ֽ���(M)</td>
    <td rowspan="2" width="9%">��ǰ״̬</td>
    <!--<td colspan="2" height="23">����������</td>-->
    <td height="23" colspan="2">����</td>
    <td rowspan="2"  align="center">�鿴����</td>
    <!--<td rowspan="2" width="9%" align="center">�鿴���</td>-->
  </tr>
  <tr>
  <!-- 
    <td   width="9%"><html:submit  property="button1" styleClass="button" onclick="changeOrder('OutBandwidthUtilHdxPerc')">���� </html:submit></td>
    <td   width="9%"><html:submit  property="button2" styleClass="button" onclick="changeOrder('InBandwidthUtilHdxPerc')">��� </html:submit></td>
    -->
    <td   width="15%"><input type=button  name="button3" styleClass="button" value="��������" onclick="changeOrder('OutBandwidthUtilHdx')"></td>
    <td   width="15%"><input type=button  name="button4" styleClass="button" value="�������" onclick="changeOrder('InBandwidthUtilHdx')"></td>
  </tr>
             <%
             int[]  width = {6,18,11,15,15,15,15};
             for(int i=0 ;i<ifvector.size() ; i++){
                String[] strs = (String[])ifvector.get(i);
                String ifname = strs[1];
                String index = strs[0];
             %>
            <tr bgcolor="DEEBF7" <%=onmouseoverstyle%>>
            <%for(int j=0 ; j<strs.length ; j++){
               if(j==3){
                 String status = strs[j];
                 String url = "";
                 if(status.equals("up")){
                   url =rootPath+"/resource/image/topo/up.gif";
                 }
                 else if(status.equals("down")){
                   url =rootPath+"/resource/image/topo/down.gif";
                 }
                 else {
                   url =rootPath+"/resource/image/topo/testing.gif";
                 }
            %>
               <td width="<%=width[j]%>%" align="center"><img src="<%=url%>"></td>
            <%
              }
            else{
            	if (j==1){
            	if (hash != null && hash.size()>0){
            		String linkuse="";
            		if (hash.get(ipaddress+":"+index)!= null)
            			linkuse = (String)hash.get(ipaddress+":"+index); 
            		%>
              			<td width="<%=width[j]/2%>%"><%=strs[j]%></td>
              			<td width="<%=width[j]/2%>%"><%=linkuse%></td>            		
              		<%            		
            	}else{
            	%>
              			<td width="<%=width[j]/2%>%"><%=strs[j]%></td>
              			<td width="<%=width[j]/2%>%"></td>            		            	
            	<%
            	}
              }else{
              %>
              <td width="<%=width[j]%>%"><%=strs[j]%></td>
              <%
              }
              }
            }%>
    <td  width="9%"align="center"><a  style="cursor:hand" onclick="openwin3('show_utilhdx','<%=index%>','<%=ifname%>')">�鿴����</a></td>
    <!--<td  width="9%"align="center"><a  style="cursor:hand" onclick="openwin3('read_about','<%=index%>','<%=ifname%>')">�鿴���</a></td>-->
            </tr>
            <%}%>            
                      
                      
                      
                      
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
 