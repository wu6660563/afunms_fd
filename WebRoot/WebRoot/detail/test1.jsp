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
String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
Hashtable hash = (Hashtable)request.getAttribute("hash");
Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");




//double cpuvalue = (Double)request.getAttribute("cpuvalue");
double avgpingcon = (Double)request.getAttribute("pingconavg");
//String collecttime = null;
//collecttime = (String)request.getAttribute("collecttime");
//if(collecttime == null)collecttime="";

//String sysuptime = "";
//sysuptime = (String)request.getAttribute("sysuptime");
//if(sysuptime == null)sysuptime="";

//String sysservices = "";
//sysservices = (String)request.getAttribute("sysservices");
//if(sysservices == null)sysservices="";

//String sysdescr = "";
//sysdescr = (String)request.getAttribute("sysdescr");
//if(sysdescr == null)sysdescr="";

  String rootPath = request.getContextPath(); 
  //String tmp = request.getParameter("id"); 
  //Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));  
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用率",avgpingcon);
  dpd.setValue("不可用率",100 - avgpingcon);
  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
  //if(item1.getSingleResult()!=-1)
  //{
     responseTime = item1.getSingleResult() + " ms";
  
     SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
     //if(item2!=null&&item2.getSingleResult()!=-1)
         //chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
         chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
  
     //SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
     //if(item3!=null&&item3.getSingleResult()!=-1)
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
  //}
  //else
     //responseTime = "无响应"; 
     
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

</script>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<title>dhcnms</title>

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
		<!--<br>-->
		<table width="100%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background="<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>　</td>
			  	<td height=300 bgcolor="#FFFFFF" valign="top">
			  	
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                  				<tr>
                    					<td width="619" height="25" class=tableMasterHeaderAlone style="HEIGHT: 25px"><div align="center">设备详细信息</div></td>
                  				</tr>
                  				<tr>
                    					<td>
								<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        					<tbody>
                          						<tr>
                								<td width="426" align="left" valign="middle" class=dashLeft>
                									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                    										<tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="2" valign=center nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">设备信息</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;设备标签:</td>
                      											<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;系统名称:</td>
                      											<td width="70%"><%=host.getSysName()%> [<img src="<%=rootPath%>/resource/image/editicon.gif">修改]</td>
                    										</tr>                    										
                    										<tr>
                      											<td height="29" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
                      											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>"><%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;IP地址:</td>
                      											<td width="70%"><%=host.getIpAddress()%></td>
                    										</tr>
                    										<tr>
                      											<td height="29" class=txtGlobal align="left" nowrap>&nbsp;子网掩码:</td>
                      											<td><%=host.getNetMask()%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;类别:</td>
                      											<td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
                    										</tr>
                    										<tr>
                      											<td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;类型:</td>
                      											<td width="70%"><%=host.getType()%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;系统描述:</td>
                      											<td width="70%"><%=sysdescr%></td>
                    										</tr>
                    										<tr>
                      											<td height="29" class=txtGlobal valign=center nowrap >&nbsp;设备启动时间:</td>
                      											<td><%=sysuptime%></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="30%" height="26" align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;数据采集时间:</td>
                      											<td width="70%"><%=collecttime%></td>
                    										</tr>
                    										<tr>
                      											<td height="29" class=txtGlobal valign=center nowrap>&nbsp;设备服务:</td>
                      											<td><%=sysservices%></td>
                    										</tr>							
                									</table>
										</td>																					
                								<td width="246" align="center" valign="middle" class=dashLeft>

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
            								</tr>
                						</tbody>
                						</table>
                					</td>
                				</tr>
                				<tr>
                					<td>
                            					
                      
                  					</td>
                  				</tr>
                  			</table>			  	
			  	
			  	</td>
                  		<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13>　</td>
                  	</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr> 
		</table> 
	</td>
	
	</tr>
	</table>                	              
                
                                          
</form>
</BODY>
</HTML>
 