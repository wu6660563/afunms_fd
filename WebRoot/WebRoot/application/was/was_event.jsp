<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.wasmonitor.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.application.manage.WasManager"%>
<%@page import="com.afunms.event.model.EventList"%>

<%
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
	 Hashtable hs = null;
  Hashtable JDBCProviderHst = null;
  Hashtable SessionManagerHst = null;
  Hashtable SystemMetricsHst = null;
  Hashtable ThreadPoolHst = null;
  Hashtable DynaCacheHst = null;
  Hashtable TransactionServiceHst = null;
  Hashtable ORBHst = null;
  Hashtable JvmHst = null;
  WasConfig vo  = (WasConfig)request.getAttribute("was");
  hs = (Hashtable)com.afunms.common.util.ShareData.getWasdata().get("was"+":"+vo.getIpaddress());

  if(hs == null)hs = new Hashtable();
  JvmHst = (Hashtable)hs.get("JvmHst");
  if(JvmHst == null)JvmHst = new Hashtable();
  JDBCProviderHst = (Hashtable)hs.get("JDBCProviderHst");
  if(JDBCProviderHst == null)JDBCProviderHst = new Hashtable();
  SessionManagerHst = (Hashtable)hs.get("SessionManagerHst");
  if(SessionManagerHst == null)SessionManagerHst = new Hashtable();
  SystemMetricsHst = (Hashtable)hs.get("SystemMetricsHst");
  if(SystemMetricsHst == null)SystemMetricsHst = new Hashtable();
  ThreadPoolHst = (Hashtable)hs.get("ThreadPoolHst");
  if(ThreadPoolHst == null)ThreadPoolHst = new Hashtable();
  DynaCacheHst = (Hashtable)hs.get("DynaCacheHst");
  if(DynaCacheHst == null)DynaCacheHst = new Hashtable();
  TransactionServiceHst = (Hashtable)hs.get("TransactionServiceHst");
  if(TransactionServiceHst == null)TransactionServiceHst = new Hashtable();
  ORBHst = (Hashtable)hs.get("ORBHst");
  if(ORBHst == null)ORBHst = new Hashtable();
   double wasping=0;
        WasManager wm = new WasManager();
        wasping=(double)wm.wasping(vo.getId());
		
		int percent1 = Double.valueOf(wasping).intValue();
		int percent2 = 100-percent1;



String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");

int level1 = Integer.parseInt(request.getAttribute("level1")+"");
int _status = Integer.parseInt(request.getAttribute("status")+"");

String level1str="";
String level2str="";
String level3str="";
if(level1 == 1){
	level1str = "selected";
}else if(level1 == 2){
	level2str = "selected";
}else if(level1 == 3){
	level3str = "selected";	
}

String status0str="";
String status1str="";
String status2str="";
if(_status == 0){
	status0str = "selected";
}else if(_status == 1){
	status1str = "selected";
}else if(_status == 2){
	status2str = "selected";	
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<script language="javascript">	

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/was.do?action=event";
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
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
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
	setClass();
}

function setClass(){
	document.getElementById('wasDetailTitle-7').className='detail-data-title';
	document.getElementById('wasDetailTitle-7').onmouseover="this.className='detail-data-title'";
	document.getElementById('wasDetailTitle-7').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

</script>
</head>
<body id="body" class="body" onload="initmenu();">
	<form method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id" value=<%=vo.getId()%>>
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
					width="32" height="32" style="margin-right: 8px;" align="middle" />
				Loading...
			</div>
		</div>
		<div id="loading-mask" style=""></div>
		<table border="0" id="table1" cellpadding="0" cellspacing="0"
			width=960>
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-application-detail">
								<table id="container-main-application-detail"
									class="container-main-application-detail">
									<tr>
										<td>
											<jsp:include page="/topology/includejsp/middleware_was.jsp">
												<jsp:param name="wasName" value="<%= vo.getName()%>"/> 
												<jsp:param name="wasIpaddress" value="<%= vo.getIpaddress()%>"/> 
												<jsp:param name="wasPort" value="<%= vo.getPortnum()%>"/> 
												<jsp:param name="wasFlag" value="<%= vo.getMon_flag()%>"/> 
												<jsp:param name="wasVersion" value="<%= vo.getVersion()%>"/> 
												<jsp:param name="percent1" value="<%=percent1%>"/> 
												<jsp:param name="percent2" value="<%=percent2%>"/>
											</jsp:include>
										</td>
									</tr>


									<tr>
										<td>
											<table id="application-detail-data"
												class="application-detail-data">
												<tr>
													<td class="detail-data-header">
														<%=wasDetailTitleTable%>
													</td>
												</tr>
												<tr>
													<td>
														<table class="application-detail-data-body">
															<tr bgcolor="#ECECEC" height="28">
																<td>
																	&nbsp;��ʼ����
																	<input type="text" name="startdate"
																		value="<%=startdate%>" size="10">
																	<a onclick="event.cancelBubble=true;"
																		href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																		<img id=imageCalendar1 align=absmiddle width=34
																			height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0>
																	</a> &nbsp;��ֹ����
																	<input type="text" name="todate" value="<%=todate%>"
																		size="10" />
																	<a onclick="event.cancelBubble=true;"
																		href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																		<img id=imageCalendar2 align=absmiddle width=34
																			height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0>
																	</a> &nbsp;�¼��ȼ�
																	<select name="level1">
																		<option value="99">
																			����
																		</option>
																		<option value="1" <%=level1str%>>
																			��ͨ�¼�
																		</option>
																		<option value="2" <%=level2str%>>
																			�����¼�
																		</option>
																		<option value="3" <%=level3str%>>
																			�����¼�
																		</option>
																	</select>

																	&nbsp;����״̬
																	<select name="status">
																		<option value="99">
																			����
																		</option>
																		<option value="0" <%=status0str%>>
																			δ����
																		</option>
																		<option value="1" <%=status1str%>>
																			���ڴ���
																		</option>
																		<option value="2" <%=status2str%>>
																			�Ѵ���
																		</option>
																	</select>
																	<input type="button" id="process" name="process"
																		value="�� ѯ" onclick="doQuery()">
																</td>
															</tr>

															<tr bgcolor="#ECECEC">
																<td>
																	<table width="100%" border="0" cellpadding="0"
																		cellspacing="1">

																		<tr>
																			<td class="application-detail-data-body-title">
																				&nbsp;
																			</td>
																			<td class="application-detail-data-body-title"
																				width="10%">
																				<strong>�¼��ȼ�</strong>
																			</td>
																			<td class="application-detail-data-body-title"
																				width="40%">
																				<strong>�¼�����</strong>
																			</td>
																			<td class="application-detail-data-body-title">
																				<strong>�Ǽ�����</strong>
																			</td>
																			<td class="application-detail-data-body-title">
																				<strong>�Ǽ���</strong>
																			</td>
																			<td class="application-detail-data-body-title">
																				<strong>����״̬</strong>
																			</td>
																			<td class="application-detail-data-body-title">
																				<strong></strong>
																			</td>
																		</tr>
																		<%
																			int index = 0;
																			java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat(
																					"MM-dd HH:mm");
																			List list = (List) request.getAttribute("list");
																			for (int i = 0; i < list.size(); i++) {
																				index++;
																				EventList eventlist = (EventList) list.get(i);
																				Date cc = eventlist.getRecordtime().getTime();
																				Integer eventid = eventlist.getId();
																				String eventlocation = eventlist.getEventlocation();
																				String content = eventlist.getContent();
																				String level = String.valueOf(eventlist.getLevel1());
																				String status = String.valueOf(eventlist.getManagesign());
																				String showlevel = null;
																				String s = status;
																				String act = "������";
																				if ("1".equals(level)) {
																					showlevel = "��ͨ�¼�";
																				}
																				if ("2".equals(level)) {
																					showlevel = "�����¼�";
																				} else {
																					showlevel = "�����¼�";
																				}
																				if ("0".equals(status)) {
																					status = "δ����";
																				}
																				if ("1".equals(status)) {
																					status = "������";
																				}
																				if ("2".equals(status)) {
																					status = "�������";
																				}
																				String rptman = eventlist.getReportman();
																				String rtime1 = _sdf.format(cc);
																		%>

																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height="28">

																			<td class="application-detail-data-body-list"
																				align="center">
																				&nbsp;<%=index%></td>
																			<%
																				if ("3".equals(level)) {
																			%>
																			<td
																				style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																				bgcolor=red align=center><%=showlevel%>&nbsp;
																			</td>
																			<%
																				} else if ("2".equals(level)) {
																			%>
																			<td
																				style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																				bgcolor=orange align=center><%=showlevel%>&nbsp;
																			</td>
																			<%
																				} else {
																			%>
																			<td
																				style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																				bgcolor=yellow align=center><%=showlevel%>&nbsp;
																			</td>
																			<%
																				}
																			%>
																			<td class="application-detail-data-body-list"
																				align="center">
																				<%=content%></td>
																			<td class="application-detail-data-body-list"
																				align="center">
																				<%=rtime1%></td>
																			<td class="application-detail-data-body-list"
																				align="center">
																				<%=rptman%></td>
																			<td class="application-detail-data-body-list"
																				align="center">
																				<%=status%></td>
																			<td class="application-detail-data-body-list"
																				align="center">
																				<%
																					if ("0".equals(s)) {
																				%>
																				<input type="button" value="���ܴ���" class="button"
																					onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																				<%
																					}
																						if ("1".equals(s)) {
																				%>
																				<input type="button" value="��д����" class="button"
																					onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																				<%
																					}
																						if ("2".equals(s)) {
																				%>
																				<input type="button" value="�鿴����" class="button"
																					onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																				<%
																					}
																				%>
																			</td>
																		</tr>
																		<%
																			}
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
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</BODY>
</HTML>