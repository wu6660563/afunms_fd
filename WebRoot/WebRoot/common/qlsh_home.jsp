<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.inform.model.SystemSnap" %>
<%@page import="com.afunms.inform.dao.SystemSnapDao" %>
<%@page import="com.afunms.security.dao.GateRecordDao"%>
<%@page import="com.afunms.security.model.GateRecord"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.polling.node.UPSNode" %>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%
  String rootPath = request.getContextPath();
  SystemSnapDao snapDao = new SystemSnapDao();
  SystemSnap snap = snapDao.getSystemSnap();  
%>
<html>
<head>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/home.css" type="text/css">
<link rel="stylesheet" href="../resource/css/style.css" type="text/css">
<script type="text/javascript">
function doInit()
{
	var autoR = setInterval(autoRefresh,1000*60*5);
}

function autoRefresh()
{
   window.location = "qlsh_home.jsp";
}
</script>
</head>
<body bgcolor="#E6E6FA" onload="doInit()">
<form name="mainForm" method="post">
<div id="bodydiv">
<a href="<%=rootPath+snap.urlsTbl.get("network")%>">
	<div id="network" class="<%=snap.getNetworkClass() %>">网络设备</div>
</a>
<a href="<%=rootPath+snap.urlsTbl.get("server")%>">
	<div id="server" class="<%=snap.getServerClass() %>">服务器</div>
</a>
<a href="<%=rootPath+snap.urlsTbl.get("virus")%>">
	<div id="virus" class="<%=snap.getVirusClass() %>">病毒情况</div>
</a>
<a href="<%=rootPath+snap.urlsTbl.get("governclient")%>">
	<div id="governclient" class="<%=snap.getGovernClientClass() %>">机关客户端</div>
</a>
<a href="<%=rootPath+snap.urlsTbl.get("internetstatus")%>">
	<div id="internetstatus" class="<%=snap.getInternetClass() %>">Internet状态</div>
</a>
<a href="<%=rootPath+snap.urlsTbl.get("oastatus")%>">
<div id="oastatus" class="<%=snap.getOaStatusClass() %>">OA状态</div>
</a>
<div id="doorsystem" class="doorsystem"><br>门禁系统<br>
<table width="500" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#000000" bordercolordark="#FFFFFF">
    <th width='20%'><font color='blue'>人员</font></th>
    <th width='20%'><font color='blue'>事件</font></th>
    <th width='30%'><font color='blue'>发生时间</font></th>
    <th width='25%'><font color='blue'>进/出</font></th>

<% 
  GateRecordDao grDao = new GateRecordDao();
  List list = grDao.loadTopN(10);

  if(list!=null)
  { 
      for(int i=0;i<list.size();i++)
      {
          GateRecord vo = (GateRecord)list.get(i);
%>
  <tr>
    <td align='center'>&nbsp;<%=vo.getPerson()%></td>
    <td align='center'>&nbsp;<%=vo.getEvent()%></td>
    <td align='center'>&nbsp;<%=vo.getLogTime()%></td>
    <td align='center'>&nbsp;<%=vo.getIo()%></td>
  </tr>
<%}}%>
</table>
</div>
<div id="roomsystem" class="roomsystem"><br>UPS<br>
<table width="500" border="1" cellspacing="0" cellpadding="0" bordercolorlight="#000000" bordercolordark="#FFFFFF">
      <th width='20%'><font color='blue'>相位</font></th>
      <th width='25%'><font color='blue'>输出电压</font></th>
      <th width='25%'><font color='blue'>输出电流</font></th>
      <th width='30%'><font color='blue'>输出负载</font></th>
    </tr>  
<% 
try
{
   UPSNode ups = (UPSNode)PollingEngine.getInstance().getNodeByID(222);
   UPSItem upsItem = (UPSItem)ups.getItemByMoid("101001");
   List phaseslist = upsItem.getPhasesList();
   for(int i=3;i<6;i++)
   {
       UPSPhase phase = (UPSPhase)phaseslist.get(i);
%>				
			<tr height='20'>
			  <td align='center' class=txtTbltdnch><%=i-2%></td>				  
			  <td align='center' class=txtTbltdnch><%=phase.getVoltage()%> V</td>
			  <td align='center' class=txtTbltdnch><%=phase.getCurrent()%> A</td>
			  <td align='center' class=txtTbltdnch><%=phase.getLoad()%> W (<%=phase.getLoadPercent()%>%)</td>
			</tr>
<%}
}
catch(Exception e){}%>				                
</table></div> 
</form>
</body>
</html>