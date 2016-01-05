<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.common.util.SysUtil" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="com.afunms.report.jfree.Transformer"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="org.jfree.data.general.DatasetUtilities"%>
<%@page import="org.jfree.data.category.CategoryDataset"%> 
<%@page import="java.util.Iterator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%  
  String rootPath = request.getContextPath(); 
  int nodeId = 1;
  String temp = null;
   
  temp = request.getParameter("id");
  if(temp!=null)  nodeId = Integer.parseInt(temp);

  Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);  

  String chart1 = null,chart2 = null,chart3 = null,chart4 = null,responseTime = null;  
  ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用时间",host.getAvailability());
  dpd.setValue("不可用时间",100 - host.getAvailability());
  chart1 = ChartCreator.createPieChart(dpd,"",200,200);  
  
  if(item1.getSingleResult()!=-1)
  {
     responseTime = item1.getSingleResult() + " ms";
     Transformer transformer = new Transformer();
  
     SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.WINDOWS_CPU);
     if(item2!=null&&item2.getMultiResults()!=null)
     {
        if(item2.getMultiResults().size()==1) //如果只有一个
        {
           MonitorResult mr = (MonitorResult)item2.getMultiResults().get(0);
           chart2 = ChartCreator.createMeterChart(mr.getPercentage(),"",150,150);  
        }
        else
        {
           transformer.item2Array(item2.getMultiResults());
           CategoryDataset dataset = DatasetUtilities.createCategoryDataset(transformer.getYKey(),transformer.getXKey(),transformer.getData());
           chart2 = ChartCreator.createStackeBarChart(dataset,"CPU","利用率(%)","CPU利用率",200,200);                 
        }   
     }  
     SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.WINDOWS_MEMORY);
     if(item3!=null&&item3.getSingleResult()!=-1)
     chart3 = ChartCreator.createMeterChart(item3.getSingleResult(),"",150,150);  
     
     SnmpItem item4 = (SnmpItem)host.getItemByMoid(MoidConstants.WINDOWS_DISK);     
     if(item4!=null&&item4.getMultiResults()!=null)
     {
         transformer.item2Array(item4.getMultiResults());
         CategoryDataset dataset = DatasetUtilities.createCategoryDataset(transformer.getYKey(),transformer.getXKey(),transformer.getData());
         chart4 = ChartCreator.createStackeBarChart(dataset,"硬盘","利用率(%)","硬盘利用率",200,200);            
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
    style="HEIGHT: 25px"><div align="center">设备详细信息</div></td>
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
                      <td width="70%"><%=host.getAlias()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>"><%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
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
                      <td width="70%"><%=host.getSysDescr()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap >&nbsp;最新告警信息:</td>
                      <td><%=host.getLastAlarm()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%"><%=SysUtil.longToTime(item1.getLastTime())%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap>&nbsp;下一次轮询:</td>
                      <td><%=SysUtil.longToTime(item1.getNextTime())%></td>
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
                    <td width="30%" height="25" class=tableMasterHeaderAlone style="HEIGHT: 25px">
                    <div align="center">CPU利用率</div></td>                    
                    <td width="30%" class=tableMasterHeaderAlone style="HEIGHT: 25px">
                    <div align="center">内存利用率</div></td>
                    <td width="40%" class=tableMasterHeaderAlone style="HEIGHT: 25px">
                    <div align="center">硬盘利用率</div></td>                    
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
                    </table></td>
<td align="center"><table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
                  cellPadding=0 rules=none width=90% align=center border=1 algin="center" height=95%>
                      <tr>
                        <td align="center" valign="middle" height='30'>
<%if(chart4!=null){%>                           
                        <img src="<%=rootPath%>/artist?series_key=<%=chart4%>">
<%} else out.print("无数据!");%>                                                
                        </td>                       
                      </tr>
                    </table></td>                    
					</tr>								
					</table>
					</td>
                  </tr>
                </table>
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='7' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>
                  接口详细</td></tr>
                 <tr height='20'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>索引</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>描述</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>速度(kbps)</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>状态</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>入口利用率(%)</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>出口利用率(%)</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>IP地址</td>
                 </tr>  
<%
   Iterator it = host.getInterfaceHash().values().iterator();
   while(it.hasNext())
   {
      IfEntity ifObj = (IfEntity)it.next();
      String status = null;
      if(ifObj.getOperStatus()==1)
         status = "<img src='" + rootPath + "/resource/image/topo/small5.png' alt='up'>";
      else
         status = "<img src='" + rootPath + "/resource/image/topo/small3.png' alt='down'>";  
%>                 
              <tr height='20'>
                   <td align='center'><%=ifObj.getIndex()%></td>
                   <td align='center'><%=ifObj.getDescr()%></td>
                   <td align='center'><%=ifObj.getSpeed()/1000%></td>
                   <td align='center'><%=status%></td>
                   <td align='center'><%=ifObj.getRxUtilization()%></td>
                   <td align='center'><%=ifObj.getTxUtilization()%> </td>
                   <td align='center'><%=ifObj.getIpAddress().replaceAll(",","<br>")%></td>
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
 