<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SnmpMibConstants"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@ page import="com.afunms.application.model.IISLog_history"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.application.dao.IISLog_historyDao"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  String ipaddress=(String)request.getAttribute("ipaddress");
  int rc = list.size();
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");

%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	
 
  var delAction = "<%=rootPath%>/iislog.do?action=delete";
  var listAction = "<%=rootPath%>/iislog.do?action=searchlist";

function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/iislog.do";	
	mainForm.submit();
}

  function query()
  {  
     mainForm.action = "<%=rootPath%>/iislog.do?action=todaylist&ipaddress="+'<%=ipaddress%>';
     mainForm.submit();
  }  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/iislog.do";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/iislog.do?action=ready_add";
      mainForm.submit();
  }
  
    function doDelete()
  {  
     mainForm.action = "<%=rootPath%>/iislog.do?action=dodelete";
     mainForm.submit();
  }  
  
// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//��Ӳ˵�	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	abc();

}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="eventid">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		
		<td bgcolor="#cedefa" align="center" >
			<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;�澯 >> �澯�б�</td>
				</tr>
				
				
							
  						<tr>
  						<td colspan=5>
	

		��ʼ����
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
	
		��ֹ����
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
		IP
		<input type="text" name="cip" />
		
		<input type="button" name="submitss" value="��ѯ" onclick="query()">
	
	
						</td>
						</tr>				
				<tr>
		
					<td height=300 bgcolor="#FFFFFF" valign="top" align=center>
					<br>
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">
						    <tr>
						    <td colspan="2" width="80%" align="center">
    
        </td></tr> 						

        						
							<tr>
								<td colspan="2">
  <table  cellSpacing="1"  cellPadding="0" border=0 width=100%>
	
  <tr  class="microsoftLook0" height=28>
    	<td width="6%">&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">���</td>
        <th width="16%"><strong>�ͻ�IP</strong></th>
        <th width="10%"><strong>�˿�</strong></th>
        <th width="10%"><strong>����</strong></th>
        <th width="20%"><strong>ҳ��</strong></th>
        <th width="11%"><strong>״̬</strong></th>
    	<th width="13%"><strong>WIN32״̬</strong></th>
	<th width="14%"><strong>��������</strong></th>
   </tr>
<%
    	IISLog_history vo = null;
    	
  	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	for(int i=0;i<list.size();i++){
  		String eventsrc = "";
  		vo = (IISLog_history)list.get(i);
  		Date cc = vo.getRecordtime().getTime();
      String rtime1 = sdf.format(cc);
    String value=SnmpMibConstants.scStatus.get(vo.getScstatus()+"")+"";
    String winvalue=null;
    if((vo.getScwin32status()+"").equals("2147483647"))
    {
    winvalue="��ָ������";
    }else
    {
    winvalue=SnmpMibConstants.SCWIN32STATUS.get(vo.getScwin32status()+"")+"";
   }
%>

 <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook">
    	<td height=25 >&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
		<td height=25 align=center>&nbsp;<%=vo.getCip()%></td> 
		<td height=25 align=center>&nbsp;<%=vo.getSport()%></td> 	
		<td height=25 align=center>&nbsp;<%=vo.getCsmethod()%></td>
		<td height=25 align=center>&nbsp;<%=vo.getCsagent()%></td>
		<td height=25 align=center>&nbsp;<%=value%></td>
		<td height=25 align=center>&nbsp;<%=winvalue%></td>
		<td height=25 align=center>&nbsp;<%=rtime1%></td>
		
  	</tr>
<%	}

%>
   
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
<script>
	
	function abc()
	{
	
		
		setInterval("fresh()",1000000);
	} 
	function fresh(){
		window.location.reload(true);
	}
</script>
</HTML>
