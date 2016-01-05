<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.TomcatHelper"%>
<%@page import="org.jdom.Element"%>
<%  
  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id");
  
  Tomcat tomcat = (Tomcat)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));  
  TomcatHelper th = new TomcatHelper(tomcat.getXmlUrl());
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;  
  CommonItem item1 = (CommonItem)tomcat.getItemByMoid(MoidConstants.TOMCAT_PING);
 
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用时间",tomcat.getAvailability());
  dpd.setValue("不可用时间",100 - tomcat.getAvailability());
  chart1 = ChartCreator.createPieChart(dpd,"",200,200);  
  
  if(item1.getSingleResult()!=-1)
  {
     responseTime = item1.getSingleResult() + " ms";  
     CommonItem item2 = (CommonItem)tomcat.getItemByMoid(MoidConstants.TOMCAT_JVM);
     if(item2!=null&&item2.getMultiResults()!=null)
     {
         MonitorResult mr = (MonitorResult)item2.getMultiResults().get(0);
         chart2 = ChartCreator.createMeterChart(mr.getPercentage(),"",150,150);  
         chart3 = th.getFreeJVMChart(tomcat.getId());
     }  
  }  
  else
    responseTime = "无响应"; 
%>
<html>
<head>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="img/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background="<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>　</td>
			  <td height=300 bgcolor="#FFFFFF" valign="top">
<!--======-->	            
<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
            cellPadding=0 rules=none align=center border=1 algin="center">
                  <tr>
                    <td width="619" height="25" class=tableMasterHeaderAlone 
    style="HEIGHT: 25px"><div align="center">Tomcat详细信息</div></td>
                  </tr>
                  <tr>
                    <td>
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 
            algin="center">
                        <tbody>
                          <tr>
                <td width="400" align="left" valign="middle" class=dashLeft><table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                    <tr bgcolor="#D1DDF5">
                      <td height="28" colspan="2" valign=center nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">设备信息</div></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;名称:</td>
                      <td width="70%"><%=tomcat.getAlias()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(tomcat.getStatus())%>"><%=NodeHelper.getStatusDescr(tomcat.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%"><%=tomcat.getIpAddress()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;端口:</td>
                      <td><%=tomcat.getPort()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;Tomcat版本:</td>
                      <td width="70%"><%=th.getTomcatVersion()%></td>
                    </tr>
                    <tr>
                      <td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;JVM版本:</td>
                      <td width="70%"><%=th.getJVMVersion()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;JVM供应商:</td>
                      <td width="70%"><%=th.getJVMVendor()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;服务器操作系统:</td>
                      <td width="70%"><%=th.getOSName()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;操作系统版本:</td>
                      <td width="70%"><%=th.getOSVersion()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap >&nbsp;最新告警信息:</td>
                      <td><%=tomcat.getLastAlarm()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%"><%=tomcat.getLastTime()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap>&nbsp;下一次轮询:</td>
                      <td><%=tomcat.getNextTime()%></td>
                    </tr>							
                </table>
				<br></td>																					
                <td width="272" align="center" valign="middle" class=dashLeft><table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=1 algin="center" >
                    
                    
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
                  <tr>
                    <td width="77%" height="32" class="txtGlobalBold">当前响应时间</td>
                    <td width="23%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td height="44"><div align="center" class="txtResponseTime"><strong><%=responseTime%></strong></div></td>
                    <td>&nbsp;</td>
                  </tr>
                </table>							  </td>
              </tr>
            </tbody>
                    </table>
					<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
            cellPadding=0 rules=none width=100% align=center border=1 
            algin="center">
			    <tr>
                    <td width="50%" height="25" class=tableMasterHeaderAlone style="HEIGHT: 25px">
                    <div align="center">JVM内存利用率</div></td>                    
                    <td width="50%" class=tableMasterHeaderAlone style="HEIGHT: 25px">
                    <div align="center">JVM可用内存</div></td>
                    <td width="50%" class=tableMasterHeaderAlone style="HEIGHT: 25px"></td>
			    </tr>
				<tr>
				<td><br>
				</td>
				</tr>
					<tr>
					<td align="center"><table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
                  cellPadding=0 rules=none width=90% align=center border=1 algin="center" height=95%>
                      <tr>
                        <td align="center" valign="middle" height='30'>
<%if(chart2!=null){%>                        
                        <img src="<%=rootPath%>/artist?series_key=<%=chart2%>">
                        <br><%=th.getMemoryInfo()%>
<%} else out.print("无数据!");%>                        
                        </td>
                      </tr>
                    </table></td>
					<td align="center"><table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
                  cellPadding=0 rules=none width=90% align=center border=1 algin="center" height=95%>
                      <tr>
                        <td align="center" valign="middle" height='30'>
<%if(chart3!=null){%>                           
                        <img src="<%=rootPath%>/artist?series_key=<%=chart3%>">
<%} else out.print("无数据!");%>   
                        </td>                       
                      </tr>
                    </table></td><td valign="top">                   
                    <a href="<%=rootPath%>/tomcat.do?action=report&node_id=<%=tmp%>"><img src="<%=rootPath%>/resource/image/graph1.png" border=0></a></td>
					</tr>								
					</table>
					</td>
                  </tr>
                </table>
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='4' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>
                  应用信息</td></tr>
                 <tr height='20'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>应用名称</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>当前会话</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>会话总数</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>会话最大数</td>
                 </tr>  
<%
   java.util.List list = th.getAppList().getChildren();
   for(int i=0;i<list.size();i++)
   {
      Element ele = (Element)list.get(i);
%>                 
           <tr height='20'>
                <td align='center' class=txtTbltdnch><%=ele.getChildText("web_module_name")%></td>
                <td align='center' class=txtTbltdnch><%=ele.getChildText("active_sessions")%></td>
                <td align='center' class=txtTbltdnch><%=ele.getChildText("session_count")%></td>
                <td align='center' class=txtTbltdnch><%=ele.getChildText("max_active_sessions")%></td>
             </tr>             
<%}%>                
               </table>                   
<!--=======-->                
	            </td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13>　</td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
</BODY>
</HTML>
 