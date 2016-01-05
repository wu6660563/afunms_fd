<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath(); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<head>
<title>dhcnms</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/list.js"></script>
<script language="JavaScript">var tabImageBase = "<%=rootPath%>/chart/report/tabs";</script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/dhtml.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/graph.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/print.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script language="JavaScript" src="<%=rootPath%>/include/validation.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="JavaScript" fptype="dynamicoutline">
function query(){
	subforms = document.forms[0];
	mainForm.action = "<%=rootPath%>/monitor.do?action=show_hostutilhdx";
	subforms.submit();
}
function checkdetail(){
	subform = document.forms[0];
	subform.operate.value = "show_utilhdx";
  subform.submit();
}
function closewin(){
	window.close();
}
function openwin(operate,category,entity,subentity) 
{	subform = document.forms[0];
  var ipaddress = subform.ipaddress.value;
  var equipname = subform.equipname.value;
  window.open ("MonitoriplistMgr.do?operate="+operate+"&category="+category+"&entity="+entity+"&subentity="+subentity+"&ipaddress="+ipaddress+"&equipname="+equipname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
</script>
</head>
<%
String[] banden1 = {"InBandwidthUtilHdxPerc","OutBandwidthUtilHdxPerc"};
String[] bandch1 = {"入口带宽利用率","出口带宽利用率"};
String[] banden2 = {"InBandwidthUtilHdx","OutBandwidthUtilHdx"};
String[] bandch2 = {"入口流速","出口流速"};
String[] netIfdetail={"index","ifDescr","ifname","ifType","ifMtu","ifSpeed","ifPhysAddress","ifAdminStatus","ifOperStatus","ifLastChange"};
String[] netIfdetailch={"端口索引","描述","描述2","类型","最大数据包","每秒字节数(M)","端口Mac地址","预期状态","当前状态","系统sysUpTime评估"};
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
Hashtable hash = (Hashtable)request.getAttribute("hash");

String operate = request.getParameter("operate");
if(operate == null) {operate="netsys";}
String ipaddress = (String)request.getAttribute("ipaddress");
String index = (String)request.getAttribute("index");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
String ifname = (String)request.getAttribute("ifname");
String perelement = (String)request.getAttribute("perelement");
String miniuteflag = "";
String hourflag = "";
String dayflag = "";
if(perelement.equalsIgnoreCase("minutes")){
	miniuteflag="selected";
}else if(perelement.equalsIgnoreCase("hours")){
	hourflag="selected";
}else if(perelement.equalsIgnoreCase("days")){
	dayflag="selected";
}
%>
<body class="WorkWin_Body">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="ipaddress" value=<%=ipaddress%>>
<input type=hidden name="ifindex" value=<%=index%>>
<input type=hidden name="ifname" value=<%=ifname%>>  

<table width="760" border="0" align="center" cellpadding="0" cellspacing="0">

  <tr> 
    <td colspan="8">
        <table cellpadding="0" cellspacing="0" width=98%>
         					<tr> 
           						<td width="100%" > 
           						<div id="flashcontent1">
								<strong>You need to upgrade your Flash Player</strong>
							</div>
							<script type="text/javascript">
								var so = new SWFObject("<%=rootPath%>/flex/Host_Area_flux.swf?ipadress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>&hostname=<%=request.getAttribute("hostname")%>", "Show_port", "760", "260", "8", "#ffffff");
								so.write("flashcontent1");
							</script>				
		                </td>
					</tr>             
				</table> 
      </td>
  </tr>
</table>
    </form>
</body>
</html> 