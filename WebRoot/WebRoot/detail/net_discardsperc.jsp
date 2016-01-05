
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


<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script language="JavaScript" src="<%=rootPath%>/include/validation.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="JavaScript" fptype="dynamicoutline">
function query(){
	subforms = document.forms[0];
	mainForm.action = "<%=rootPath%>/monitor.do?action=show_utilhdx";
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
String[] banden2 = {"InDiscardsPerc","OutDiscardsPerc"};
String[] bandch2 = {"入口丢包率","出口丢包率"};
String[] netIfdetail={"index","ifDescr","ifname","ifType","ifMtu","ifSpeed","ifPhysAddress","ifAdminStatus","ifOperStatus","ifLastChange"};
String[] netIfdetailch={"端口索引","描述","描述2","类型","最大数据包","每秒字节数(M)","端口Mac地址","预期状态","当前状态","系统sysUpTime评估"};
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
Hashtable hash = (Hashtable)request.getAttribute("hash");

String ipaddress = (String)request.getAttribute("ipaddress");
String index = (String)request.getAttribute("index");
String startdate = (String)request.getAttribute("startdate");
String ifname = (String)request.getAttribute("ifname");

%>
<body class="WorkWin_Body">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="ipaddress" value=<%=ipaddress%>>
<input type=hidden name="ifindex" value=<%=index%>>
<input type=hidden name="ifname" value=<%=ifname%>>  

<table width="760" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="1">&nbsp;</td>
    <td width="74" height="24" background="<%=rootPath%>/resource/image/anjian_1.gif" align=center valign=bottom> 
      <a href="<%=rootPath%>/monitor.do?action=show_utilhdx&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">端口流速</font></a></td>
    <td width="74" height="24" background="<%=rootPath%>/resource/image/anjian_1.gif"> 
      <div align="center"><a href="<%=rootPath%>/monitor.do?action=read_detail&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">端口状态</font></a></div></td>
    <!--<td width="88" background="../images/anjian_1.gif"> 
      <div align="center"><a href="<%=rootPath%>/monitor.do?action=show_utilhdxperc&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#ffffff">带宽利用率</font></a></div></td>-->
    <td width="74" background="<%=rootPath%>/resource/image/anjian.gif">
<div align="center"><a href="<%=rootPath%>/monitor.do?action=show_discardsperc&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#ffffff">端口丢包率</font></a></div></td>
    <td width="74" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/monitor.do?action=show_errorsperc&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">端口错误率</font></a></div></td>
    <td width="74" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/monitor.do?action=show_packs&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">收发信息数</font></a></div></td>
    <td width="74" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/monitor.do?action=show_inpacks&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">入口数据包</font></a></div></td>
    <td width="74" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/monitor.do?action=show_outpacks&ipaddress=<%=ipaddress%>&ifindex=<%=index%>&ifname=<%=ifname%>"><font color="#397dbd">出口数据包</font></a></div></td>
    <td width=248>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="10">
    <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="98C4F1" id="table2">
        <tr> 
          <td><div align="center">
              <table width="100%" border="0" cellpadding="0" cellspacing="0" id="table3">
                <tr>
                  <td align=right><font size="2">&nbsp;端口监控信息&nbsp;&nbsp;<%=request.getAttribute("hostname")%>( IP:<%=ipaddress%> )&nbsp;&nbsp;端口号&nbsp;<%=index%>&nbsp;&nbsp;端口名称&nbsp;<%=ifname%></font>&nbsp;&nbsp;</td>
                </tr>
              </table>
            </div>
            </td>
        </tr>

        <tr> 
          <td>
                <table width="1" height="1" border="0" cellpadding="0" cellspacing="0" bordercolor="397DBD">
                    <tr align="left" bgcolor="397dbd"> 
                      <td width="100%" height="23" background="<%=rootPath%>/resource/image/yemian_16.gif">&nbsp;<b><font color="#FFFFFF">丢包率&nbsp;&nbsp; 
                        &gt;&gt;<a  style="cursor:hand" onclick="openwin('day','discardsperc','','<%=index%>')"><font color="#FFFFFF">查看日报表</font></a>
                        &gt;&gt;<a  style="cursor:hand" onclick="openwin('month','discardsperc','','<%=index%>')"><font color="#FFFFFF">查看月报表</font></a></font></b>
                        </td>
                    </tr>
                    <tr> 
                      <td ><i> 
                        <table width="742" border="0" cellpadding="0" cellspacing="0">
                          <tr> 
                            <td bgcolor="#FFFFCC" align=center><img src="<%=rootPath%>/<%=imgurl.get("ifdescperc").toString()%>"></td>
                          </tr>
                        </table>
                        </i></td>
                    </tr>
                  </table>
            </td>
        </tr>
      </table>
      </td>
  </tr>
</table>
    </form>
</body>
</html> 