<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.common.util.SysUtil" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.HostLastCollectDataManager"%>
<%@page import="com.afunms.polling.api.I_HostLastCollectData"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>

<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
Hashtable hash = (Hashtable)request.getAttribute("hash");
Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");




double cpuvalue = (Double)request.getAttribute("cpuvalue");
double avgpingcon = (Double)request.getAttribute("pingconavg");
String collecttime = null;
collecttime = (String)request.getAttribute("collecttime");
if(collecttime == null)collecttime="";

String sysuptime = "";
sysuptime = (String)request.getAttribute("sysuptime");
if(sysuptime == null)sysuptime="";

String sysservices = "";
sysservices = (String)request.getAttribute("sysservices");
if(sysservices == null)sysservices="";

String sysdescr = "";
sysdescr = (String)request.getAttribute("sysdescr");
if(sysdescr == null)sysdescr="";

  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id"); 
  Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));  
  
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

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");					  	 
         
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
function changeOrder(para){
      mainForm.orderflag.value = para;
      mainForm.id.value = <%=host.getId()%>;
      mainForm.action = "<%=rootPath%>/monitor.do?action=netif";
      mainForm.submit();
	
  	//location.href="MonitoriplistMgr.do";
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
		<td bgcolor="#9FB0C4" align="center">
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
                      											<td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;名称:</td>
                      											<td width="70%"><%=host.getAlias()%> [<img src="<%=rootPath%>/resource/image/editicon.gif">修改]</td>
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
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td width="30" height="22">&nbsp;</td>
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian.gif">
			<div align="center"><a href="<%=rootPath%>/monitor.do?action=netif&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#FFFFFF">流速信息</font></a></div></td>
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
			<div align="center"><a href="<%=rootPath%>/monitor.do?action=netcpu&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">性能信息</font></a></div></td>	
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
			<div align="center"><a href="<%=rootPath%>/monitor.do?action=netarp&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">ARP信息</font></a></div></td>
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
			<div align="center"><a href="<%=rootPath%>/monitor.do?action=netroute&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">路由表信息</font></a></div></td>	
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
			<div align="center"><a href="<%=rootPath%>/monitor.do?action=netping&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><font color="#397dbd">连通率</font></a></div></td>	
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
			<div align="center"><font color="#397dbd">事件信息</font></div></td>																		
                <td width="120" align=right>                					        		  
			&nbsp;&nbsp;	                       	
		</td>
              </tr>
            </table>
                            					
                      <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
  <tr align="center" bgcolor=#cedefa> 
    <td rowspan="2" width="6%" align="left" bgcolor=#cedefa>索引</td>
    <td rowspan="2" width="9%" align="left" bgcolor=#cedefa>描述</td>
    <td rowspan="2" width="9%" align="left" bgcolor=#cedefa>关联应用</td>
    <td rowspan="2" width="11%">每秒字节数(M)</td>
    <td rowspan="2" width="9%">当前状态</td>
    <!--<td colspan="2" height="23">带宽利用率</td>-->
    <td height="23" colspan="2">流速</td>
    <td rowspan="2"  align="center">查看详情</td>
    <!--<td rowspan="2" width="9%" align="center">查看相关</td>-->
  </tr>
  <tr bgcolor=#cedefa>
  <!-- 
    <td   width="9%"><html:submit  property="button1" styleClass="button" onclick="changeOrder('OutBandwidthUtilHdxPerc')">出口 </html:submit></td>
    <td   width="9%"><html:submit  property="button2" styleClass="button" onclick="changeOrder('InBandwidthUtilHdxPerc')">入口 </html:submit></td>
    -->
    <td   width="15%"><input type="submit"  name="button3" value="出口流速" styleClass="button" onclick="changeOrder('OutBandwidthUtilHdx')"></td>
    <td   width="15%"><input type="submit"  name="button4" value="入口流速" styleClass="button" onclick="changeOrder('InBandwidthUtilHdx')"></td>
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
            			if (hash.get(host.getIpAddress()+":"+index)!= null)
            				linkuse = (String)hash.get(host.getIpAddress()+":"+index); 
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
    <td  width="9%"align="center"><a  style="cursor:hand" onclick="openwin3('show_utilhdx','<%=index%>','<%=ifname%>')">查看详情</a></td>
    <!--<td  width="9%"align="center"><a  style="cursor:hand" onclick="openwin3('read_about','<%=index%>','<%=ifname%>')">查看相关</a></td>-->
            </tr>
            <%}%>                                 
                        </table>
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
 