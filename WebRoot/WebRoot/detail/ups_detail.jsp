<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.security.util.MachineProtectHelper"%>
<%@page import="com.afunms.polling.node.UPSNode" %>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.security.dao.UpsLogDao"%>
<%@page import="com.afunms.security.model.UpsLog"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="java.util.List"%>
<%  
  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id");  
  UPSNode node = (UPSNode)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));  
  
  String chart1 = null,responseTime = null;  
  ResponseTimeItem item1 = (ResponseTimeItem)node.getMoidList().get(0); 
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用时间",node.getAvailability());
  dpd.setValue("不可用时间",100 - node.getAvailability());
  chart1 = ChartCreator.createPieChart(dpd,"",200,200);  
  UPSItem upsItem = (UPSItem)node.getItemByMoid("101001");
    
  if(item1.getSingleResult()!=-1)
     responseTime = item1.getSingleResult() + " ms"; 
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
    style="HEIGHT: 25px"><div align="center">UPS详细信息</div></td>
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
                      <td width="70%"><%=node.getAlias()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=MachineProtectHelper.getStatusImage(node.getStatus())%>"><%=MachineProtectHelper.getUPSStatusDescr(node.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%"><%=node.getIpAddress()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;位置:</td>
                      <td><%=node.getLocation()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;被保护设备数量:</td>
                      <td width="70%"><%=upsItem.getDevicesNumber()%>台</td>
                    </tr>                    
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;系统描述:</td>
                      <td><%=node.getSysDescr()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;最新告警信息:</td>
                      <td width="70%"><%=node.getLastAlarm()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap >&nbsp;上一次轮询:</td>
                      <td><%=node.getLastTime()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;下一次轮询:</td>
                      <td width="70%"><%=node.getNextTime()%></td>
                    </tr>
                </table>
				<br></td>																					
                <td width="272" align="center" valign="middle" class=dashLeft><table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=1 algin="center" >
                    <tr class="topNameRight">
                      <td width="3%" height="7" bgcolor="#FFFFFF">&nbsp;</td>
                      <td width="94%" background="OpManager.files/1spacer.gif" bgcolor="#FFFFFF"><div align="center"></div></td>
                      <td width="3%" bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>
                    <tr class="topNameRight">
                      <td height="223" rowspan="2">&nbsp;</td>
                      <td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">今天的可用率</td>
                      <td height="223" rowspan="2">&nbsp;</td>
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
                    <td width="100%" height="25" colspan='4' class=tableMasterHeaderAlone style="HEIGHT: 25px"><div align="center">UPS输入</div></td>                                        
			    </tr>			    
				<tr>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>相位</td>				  
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输入电压</td>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输入电流</td>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输入频率</td>
				</tr>
<% 
   List list = upsItem.getPhasesList();
   if(list==null||list.size()==0) return;
   
   for(int i=0;i<3;i++)
   {
       UPSPhase phase = (UPSPhase)list.get(i);
%>			
        <tr height='20'>	
		  <td align='center' class=txtTbltdnch><%=i+1%></td>
		  <td align='center' class=txtTbltdnch><%=phase.getVoltage()%> V</td>
		  <td align='center' class=txtTbltdnch><%=phase.getCurrent()%> A</td>
		  <td align='center' class=txtTbltdnch><%=phase.getFrequency()%> Hz</td>
        </tr>		  
<%}%>				
					</table>
					</td>
                  </tr>
                </table>
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='5' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>UPS输出</td></tr>
                 <tr height='20'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>相位</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输出电压</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输出电流</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输出频率</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>输出负载</td>
                 </tr>  
<% 
   for(int i=3;i<6;i++)
   {
       UPSPhase phase = (UPSPhase)list.get(i);
%>				
			<tr height='20'>
			  <td align='center' class=txtTbltdnch><%=i-2%></td>				  
			  <td align='center' class=txtTbltdnch><%=phase.getVoltage()%> V</td>
			  <td align='center' class=txtTbltdnch><%=phase.getCurrent()%> A</td>
			  <td align='center' class=txtTbltdnch><%=phase.getFrequency()%> Hz</td>
			  <td align='center' class=txtTbltdnch><%=phase.getLoad()%> W (<%=phase.getLoadPercent()%>%)</td>
			</tr>
<%}%>				                
               </table> 
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='3' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>UPS关键参数</td></tr>
                 <tr height='25'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center' widht="30%">参数</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center' widht="30%">值</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center' widht="40%">说明</td>
                 </tr>  
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>输出负载</td>				  
			  <td align='center' class=txtTbltdnch><%=upsItem.getUpsLoad()%> W</td>
			  <td align='center' class=txtTbltdnch></td>
			</tr>                 
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>额定负载</td>				  
			  <td align='center' class=txtTbltdnch><%=upsItem.getUpsRatedLoad()%> W</td>
			  <td align='center' class=txtTbltdnch>若输出负载大于额定负载，则UPS超载</td>
			</tr>                 			
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池蓄电</td>				  
			  <td align='center' class=txtTbltdnch><%=upsItem.getBatteryLevel()%>%</td>
			  <td align='center' class=txtTbltdnch>&nbsp;</td>
			</tr>
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池提供电压</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.getBatteryVoltage()%> V</td>
			  <td align='center' class=txtTbltdnch>&nbsp;</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池支持时间</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.getBatteryTime()%>分钟</td>
			  <td align='center' class=txtTbltdnch>市电断电后，UPS能支持的时间</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池最低蓄电</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.getLowBatteryLevel()%> %</td>
			  <td align='center' class=txtTbltdnch>电池蓄电下降到该值时，UPS准备关机</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池是否有故障</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.isBatteryFault()?"<font color='red'>是</font>":"否"%></td>
			  <td align='center' class=txtTbltdnch>电池可能错位、不可用或没有充满电等</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池充电器是否有故障</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.isBatteryChargerFault()?"<font color='red'>是</font>":"否"%></td>
			  <td align='center' class=txtTbltdnch>&nbsp;</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池是否低电量</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.isBatteryLow()?"<font color='red'>是</font>":"否"%></td>
			  <td align='center' class=txtTbltdnch>当电池低电量时，UPS准备关机</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>电池是否启用</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.isOutputOnBattery()?"<font color='red'>是</font>":"否"%></td>
			  <td align='center' class=txtTbltdnch>当市电断电时，电池启用</td>
			</tr>	                
			<tr height='20'>
			  <td align='center' class=txtTbltdnch>旁路是否启用</td>
			  <td align='center' class=txtTbltdnch><%=upsItem.isOutputOnByPass()?"<font color='red'>是</font>":"否"%></td>
			  <td align='center' class=txtTbltdnch>当UPS有故障时，旁路启用</td>
			</tr>	                			
               </table>
<%
   UpsLogDao dao = new UpsLogDao();
   List exceptionList = dao.findAlarm();
   if(exceptionList.size()!=0)
   {
%>                                  
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='3' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>UPS异常记录</td></tr>
                 <tr height='25'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center' widht="60%">异常描述</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center' widht="40%">发生时间</td>
                 </tr>  
<% 
   for(int i=0;i<exceptionList.size();i++)
   {
       UpsLog vo = (UpsLog)exceptionList.get(i);
%>                 
			<tr height='20'>
			  <td align='center' class=txtTbltdnch><%=vo.getMessage()%></td>				  
			  <td align='center' class=txtTbltdnch><%=vo.getLogTime()%></td>
			</tr>                 
<%}%> 
  </table>
<%} //if(exceptionList.size()!=0)%>                                   
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
 