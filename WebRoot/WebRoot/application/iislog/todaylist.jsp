<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SnmpMibConstants"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@ page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  String ipaddress=(String)request.getAttribute("ipaddress");
  int rc = list.size();

  //JspPage jp = (JspPage)request.getAttribute("page");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");

%>

<%
	String chart1 = null;
	chart1 = (String)request.getAttribute("chart1");
	
	String chart2 = (String)request.getAttribute("chart2");

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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
	onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form method="post" name="mainForm">
		<input type=hidden name="eventid">
		<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
			<tr>
				<td width="200" valign=top align=center>
					<%=menuTable%>
				</td>
				<td align="center" valign=top>
					<table width="98%" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<table width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										<td class="layout_title"><b>Ӧ�� >> �м������ >> IISLOG��Ϣ</b></td>
										<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellpadding="0" cellspacing="1">
									<tr>
										<td>
											<table width="100%" cellpadding="0" cellspacing="0">
												<tr>
													<td bgcolor="#ECECEC" width="50%" align='left'>
														&nbsp;&nbsp;��ʼ����
														<input type="text" name="startdate" value="<%=startdate%>" size="10">
														<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)"><img id=imageCalendar1 width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> ��ֹ����
														<input type="text" name="todate" value="<%=todate%>" size="10" />
														<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)"><img id=imageCalendar2 width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> ����
														<select name="sip">
															<option value=" "></option>
															<%
																IISLogConfigDao dao = new IISLogConfigDao();
																List arr = dao.getAllIISLog();
																for (int i = 0; i < arr.size(); i++) {
																	IISLogConfig vo = (IISLogConfig) arr.get(i);
															%>
															<option value="<%=vo.getIpaddress()%>"><%=vo.getName()%></option>
															<%
																}
															%>
														</select>
														IP
														<input type="text" name="cip" />
														<input type="button" name="submitss" value="��ѯ" onclick="query()">
														<br>
															<a href="<%=rootPath%>/iislog.do?action=downloadiislogip&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='����EXCEL' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0">IP�ֲ�ͼ����EXCEL</a>
															<a href="<%=rootPath%>/iislog.do?action=downloadiislogipword&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='����EXCEL' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0">IP�ֲ�ͼ����Word</a>
															<a href="<%=rootPath%>/iislog.do?action=downloadiislogstate&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='����EXCEL' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0">״̬�ֲ�ͼ����EXCEL</a>
															<a href="<%=rootPath%>/iislog.do?action=downloadiislogstateword&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='����EXCEL' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0">״̬�ֲ�ͼ����Word</a>
														<br>
													</td>
												</tr>

											</table>
										</td>

									</tr>

								</table>

							</td>
						</tr>
						<tr>
							<td>
								<img align=left src="<%=rootPath%>/artist?series_key=<%=chart1%>">
								<img align=right src="<%=rootPath%>/artist?series_key=<%=chart2%>">
							</td> 
						</tr> 
						<tr>
							<td colspan="5">
								<table cellSpacing="1" cellPadding="0" border=0 width=100%>

									<tr class="microsoftLook0" height=28>
										<th nowrap width=5%>
											&nbsp;
											<INPUT type="checkbox" class=noborder name="checkall"
												onclick="javascript:chkall()">
											���
										</th>
										<th nowrap width=10%>
											�ͻ�IP
										</th>
										<th nowrap width=10%>
											�˿�
										</th>
										<th nowrap width=10%>
											����
										</th>
										<th nowrap width=30%>
											ҳ��
										</th>
										<th nowrap width=12%>
											״̬
										</th>
										<th nowrap width=15%>
											WIN32״̬
										</th>
										<th nowrap width=8%>
											��������
										</th>
									</tr>
									<%
										IISLog_history vo = null;

										java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
										for (int i = 0; i < list.size(); i++) {
											String eventsrc = "";
											vo = (IISLog_history) list.get(i);
											Date cc = vo.getRecordtime().getTime();
											String rtime1 = sdf.format(cc);
											String value = SnmpMibConstants.scStatus.get(vo.getScstatus()+ "")+ "";
											String winvalue = null;
											if ((vo.getScwin32status() + "").equals("2147483647")) {
												winvalue = "��ָ������";
											} else {
												winvalue = SnmpMibConstants.SCWIN32STATUS.get(vo.getScwin32status()+ "")+ "";
											}
									%>

									<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
										<td width=5%>
											&nbsp;
											<INPUT type="checkbox" class=noborder name=checkbox
												value="<%=vo.getId()%>">
										</td>
										<td width=10% align=center>
											&nbsp;<%=vo.getCip()%></td>
										<td width=10% align=center>
											&nbsp;<%=vo.getSport()%></td>
										<td width=10% align=center>
											&nbsp;<%=vo.getCsmethod()%></td>
										<td style="word-break: break-all;" width=30% align=center>
											&nbsp;<%=vo.getCsagent()%></td>
										<td style="word-break: break-all;" width=12% align=center>
											&nbsp;<%=value%></td>
										<td width=15% align=center>
											&nbsp;<%=winvalue%></td>
										<td width=8% align=center>
											&nbsp;<%=rtime1%></td>

									</tr>
									<%
										}
									%>

								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" background="<%=rootPath%>/common/images/right_b_02.jpg" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12"/></td>
										<td></td>
										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12"/></td>
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
