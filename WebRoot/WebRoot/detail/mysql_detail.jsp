<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.MySQLHelper"%>
<%@page import="com.afunms.application.util.DBPool"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.List"%>
<%  
  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id");
  DBNode node = (DBNode)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));  
  Connection conn = DBPool.getInstance().getConnection(node.getId());   
  if(conn==null)  
  {
      out.print("无法接连数据库");
      return;  
  }

  String chart1 = null,responseTime = null;  
  CommonItem item1 = (CommonItem)node.getItemByMoid(MoidConstants.DB_PING);
 
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用时间",node.getAvailability());
  dpd.setValue("不可用时间",100 - node.getAvailability());
  chart1 = ChartCreator.createPieChart(dpd,"",200,200);  
  
  if(item1.getSingleResult()!=-1)
     responseTime = item1.getSingleResult() + " ms";  
  else
     responseTime = "无响应"; 
     
  MySQLHelper helper = new MySQLHelper(conn); 
  helper.init(node.getDbName());  
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
    style="HEIGHT: 25px"><div align="center">MySQL详细信息</div></td>
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
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(node.getStatus())%>"><%=NodeHelper.getStatusDescr(node.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%"><%=node.getIpAddress()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;端口:</td>
                      <td><%=node.getPort()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;MySQL版本:</td>
                      <td width="70%"><%=helper.getVersion()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal align="left" nowrap>&nbsp;操作系统:</td>
                      <td><%=helper.getHostOS()%></td>
                    </tr>                    
                    <tr>
                      <td width="30%" height="26" align=left valign=left nowrap class=txtGlobal>&nbsp;安装路径:</td>
                      <td width="70%"><%=helper.getBasePath()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;数据路径:</td>
                      <td width="70%"><%=helper.getDataPath()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap >&nbsp;最新告警信息:</td>
                      <td><%=node.getLastAlarm()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%"><%=node.getLastTime()%></td>
                    </tr>
                    <tr>
                      <td height="29" class=txtGlobal valign=center nowrap>&nbsp;下一次轮询:</td>
                      <td><%=node.getNextTime()%></td>
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
                    <td width="100%" height="25" colspan='4' class=tableMasterHeaderAlone style="HEIGHT: 25px"><div align="center">表信息</div></td>                                        
			    </tr>			    
				<tr>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>表名</td>				  
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>行数</td>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>文件长度</td>
				  <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>创建时间</td>
				</tr>
<% 
   List tablesDetail = helper.getTablesDetail();
   for(int i=0;i<tablesDetail.size();i++)
   {
      String[] item = (String[])tablesDetail.get(i);
%>				
				<tr>
				  <td align='left' class=txtTbltdnch>&nbsp;&nbsp;<%=item[0]%></td>				  
				  <td align='center' class=txtTbltdnch><%=item[1]%></td>
				  <td align='center' class=txtTbltdnch><%=item[2]%></td>
				  <td align='center' class=txtTbltdnch><%=item[3]%></td>
				</tr>
<%}%>				
					</table>
					</td>
                  </tr>
                </table>
<br>
               <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
               cellPadding=0 rules=none width=100% align=center border=1 algin="center">                
               <tr><td width="80%" height="25" colspan='4' class=tableMasterHeaderAlone style="HEIGHT: 25px" align='center'>
                  连接信息</td></tr>
                 <tr height='20'>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>用户</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>主机</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>命令</td>
                   <td bgcolor="#EBF4F7" nowrap class=txtGlobal align='center'>连接时间</td>
                 </tr>  
<% 
   List sessionsDetail = helper.getSessionsDetail();
   for(int i=0;i<sessionsDetail.size();i++)
   {
      String[] item = (String[])sessionsDetail.get(i);
%>				
				<tr>
				  <td align='center' class=txtTbltdnch><%=item[0]%></td>				  
				  <td align='center' class=txtTbltdnch><%=item[1]%></td>
				  <td align='center' class=txtTbltdnch><%=item[2]%></td>
				  <td align='center' class=txtTbltdnch><%=item[3]%></td>
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
 