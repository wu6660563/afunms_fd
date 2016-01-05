<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.EmailMonitorConfig"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.base.*"%>
<%
	String rootPath = request.getContextPath();
	String flag_1 = (String) request.getAttribute("flag");
	String timeFormat = "yyyy-MM-dd";
	String fdate = (String) request.getAttribute("from_date1");
	String tdate = (String) request.getAttribute("to_date1");
	String fhour = (String) request.getAttribute("from_hour");
	String thour = (String) request.getAttribute("to_hour");
	
	Integer myId = (Integer) request.getAttribute("id");
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
			timeFormat);
	String from_date1 = "";
	if (fdate == null) {
		from_date1 = timeFormatter.format(new java.util.Date());
	} else {
		from_date1 = fdate;

	}
	String to_date1 = "";
	if (tdate == null) {
		to_date1 = timeFormatter.format(new java.util.Date());
	} else {
		to_date1 = tdate;

	}

	String from_hour = "";
	if (fhour == null) {
		from_hour = new java.util.Date().getHours() + "";
	} else {
		from_hour = fhour;

	}
	String to_hour = "";
	if (thour == null) {
		to_hour = new java.util.Date().getHours() + "";
	} else {
		to_hour = thour;

	}
	EmailMonitorConfig queryconf = (EmailMonitorConfig) request
			.getAttribute("initconf");

	String conn_name = request.getAttribute("conn_name").toString();
	String valid_name = request.getAttribute("valid_name").toString();
	String fresh_name = request.getAttribute("fresh_name").toString();
	String wave_name = request.getAttribute("wave_name").toString();
	String delay_name = request.getAttribute("delay_name").toString();
	String connrate = request.getAttribute("connrate").toString();
	String validrate = request.getAttribute("validrate").toString();
	String freshrate = request.getAttribute("freshrate").toString();
	String connsrc = rootPath + "/resource/image/jfreechart/"
			+ conn_name + ".png";
	String validsrc = rootPath + "/resource/image/jfreechart/"
			+ valid_name + ".png";
	String freshsrc = rootPath + "/resource/image/jfreechart/"
			+ fresh_name + ".png";
	String wavesrc = rootPath + "/resource/image/jfreechart/"
			+ wave_name + ".png";
	String delaysrc = rootPath + "/resource/image/jfreechart/"
			+ delay_name + ".png";
	List urllist = (List) request.getAttribute("urllist");

	int status = 0;
	Node node = (Node) PollingEngine.getInstance().getMailByID(
			queryconf.getId());
	String alarmmessage = "";
	if (node != null) {
		status = node.getStatus();
		List alarmlist = node.getAlarmMessage();
		if (alarmlist != null && alarmlist.size() > 0) {
			for (int k = 0; k < alarmlist.size(); k++) {
				alarmmessage = alarmmessage
						+ alarmlist.get(k).toString();
			}
		}
	}

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(queryconf.getSupperid()
				+ "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
%>

<%
	String menuTable = (String) request.getAttribute("menuTable");
%>


<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

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
}//��Ӳ˵�	
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
	document.getElementById('emailDetailTitle-0').className='detail-data-title';
	document.getElementById('emailDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('emailDetailTitle-0').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
		
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/web.do?action=detail";
      mainForm.submit();
} 


</script>


	</head>
	<body id="body" class="body" onload="initmenu();">

		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag_1%>">
			<input type="hidden" id="id" name="id" value="<%=myId%>">
			<table id="body-container" class="body-container">
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
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
														<td class="content-title">
															<b>Ӧ�� >> ������� >> �ʼ�������Ϣ</b>
														</td>
														<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body"
													style="text-align: center;" cellspacing="10">
													<tr>
														<td align=left>
															��������
															<select name="id">
																<%
																	if (urllist != null && urllist.size() > 0) {
																		for (int i = 0; i < urllist.size(); i++) {
																			EmailMonitorConfig webconfig = (EmailMonitorConfig) urllist
																					.get(i);
																%>

																<option value="<%=webconfig.getId()%>">
																	<%=webconfig.getName()%></option>

																<%
																	}
																	}
																%>
															</select>
															<input type="button" onclick="show_graph()" class=button
																value="��ѯ">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<br>
														</td>
													</tr>
													<tr style="text-align: center;">
														<td align="center" width="60%" style="text-align: center;"
															cellspacing="10">
															<table style="BORDER-COLLAPSE: collapse"
																bordercolor=#cedefa cellpadding=0 rules=none width=100%
																align=center border=1 algin="center">
																<tr bgcolor="#F1F1F1">
																	<td width="30%" height="26" align="left" nowrap>
																		&nbsp;����:
																	</td>
																	<td width="70%"><%=queryconf.getName()%></td>
																</tr>
																<tr>
																	<td width="30%" height="26" align="left" nowrap>
																		&nbsp;����:
																	</td>
																	<td width="70%">
																		�ʼ��������
																	</td>
																</tr>
																<tr bgcolor="#F1F1F1">
																	<td width="30%" height="26" align="left" nowrap>
																		&nbsp;״̬:
																	</td>
																	<td width="70%">
																		<img
																			src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>"
																			border="0" alt=<%=alarmmessage%>>
																	</td>
																</tr>
																<tr>
																	<td height="29" align="left">
																		&nbsp;�ʼ���ַ:
																	</td>
																	<td>
																		&nbsp;<%=queryconf.getAddress()%>
																	</td>
																</tr>
																<tr bgcolor="#F1F1F1">
																	<td width="30%" height="26" align=left nowrap>
																		&nbsp;�����ʺ�:
																	</td>
																	<td width="70%"><%=queryconf.getUsername()%></td>
																</tr>
																<tr>
																	<td height="29" align="left">
																		&nbsp;��������:
																	</td>
																	<td>
																		&nbsp;<%=queryconf.getRecivemail()%>
																	</td>
																</tr>
																<tr>
																	<td height="29" align="left">
																		&nbsp;�ϴ���ѯʱ��:
																	</td>
																	<td><%=request.getAttribute("lasttime")%></td>
																</tr>
																<tr>
																	<td height="29" class=txtGlobal valign=center nowrap>
																		&nbsp;��Ӧ��:
																	</td>
																	<td>
																		<%
																			if (supper != null) {
																		%>
																		<a href="#" style="cursor: hand"
																			onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
																		<%
																			}
																		%>
																	</td>
																</tr>

															</table>
														</td>
														<td align="center" width="40%" valign=top>
																		<table width="100%" cellspacing="1" cellpadding="1">
																			<tr>
																				<td width="100%">
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																			var so = new SWFObject("<%=rootPath%>/flex/Email_Ping_Pie.swf?id=<%=queryconf.getId()%>", "Email_Ping_Pie", "380", "220", "8", "#ffffff");
																			so.write("flashcontent2");
																		</script>
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
												<table id="content-footer" class="content-footer">
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0">
																<tr>
																	<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
																	<td></td>
																	<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
							<tr>
								<td>
									<table id="detail-data" class="detail-data">
										<tr>
											<td class="detail-data-header">
												<%=emailDetailTitleTable%>
											</td>
										</tr>
										<tr>
											<td>
												<table class="detail-data-body">
													<tr>
														<td align=center valign=top>
															<br>
															<table cellpadding="0" cellspacing="0" width=48%
																align=center>
																<tr>
																	<td width="48%" valign="top" align="center">
																		<table width="100%" cellspacing="1" cellpadding="1">
																			<tr>
																				<td width="100%">
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																			var so = new SWFObject("<%=rootPath%>/flex/Email_Ping_Line.swf?id=<%=queryconf.getId()%>", "Email_Ping_Line", "400", "250", "8", "#ffffff");
																			so.write("flashcontent3");
																		</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
															<br>
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
					<td width=15% valign=top>
						<jsp:include page="/include/mailtoolbar.jsp">
							<jsp:param value="<%=queryconf.getId()%>" name="id" />
						</jsp:include>
					</td>
				</tr>

			</table>
		</form>
		<script>			
Ext.onReady(function()
{  


	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/mail.do?action=sychronizeData&id=<%=queryconf.getId()%>&flag=<%=flag_1%>";
  mainForm.submit();
 });    
});
</script>
	</body>
</html>
