<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<html>
	<head>
		<%
			String rootPath = request.getContextPath();
			//System.out.println(rootPath);
			List pinglist = (List) session.getAttribute("pinglist");
			List eventlist = (List) request.getAttribute("eventlist");
			String ipaddress = (String) request.getAttribute("ipaddress");
			String startdate = (String) request.getAttribute("startdate");
			String todate = (String) request.getAttribute("todate");
			String id = (String) request.getAttribute("id");
			int level1 = Integer.parseInt(request.getAttribute("level1") + "");
			int _status = Integer.parseInt(request.getAttribute("status") + "");

			String level1str = "";
			String level2str = "";
			String level3str = "";
			if (level1 == 1) {
				level1str = "selected";
			} else if (level1 == 2) {
				level2str = "selected";
			} else if (level1 == 3) {
				level3str = "selected";
			}
			String status0str = "";
			String status1str = "";
			String status2str = "";
			if (_status == 0) {
				status0str = "selected";
			} else if (_status == 1) {
				status1str = "selected";
			} else if (_status == 2) {
				status2str = "selected";
			}
		%>
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

		<title>�¼�����</title>
		<!-- snow add 2010-5-28 -->
		<style>
<!--
body {
	background-image:
		url(${pageContext.request.contextPath}/common/images/menubg_report.jpg)
		;
	TEXT-ALIGN: center;
}
-->
</style>
		<!-- snow add end -->
		<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
		<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
		<script type='text/javascript' src='/afunms/dwr/util.js'></script>
		<script language="javascript" src="/afunms/js/tool.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script language="JavaScript" type="text/JavaScript">
function network_event_word_report()
{
	mainForm.action = "<%=rootPath%>/servicecapreport.do?action=downloadEventReport&ipaddress=<%=ipaddress%>&str=0";
	mainForm.submit();
}
function network_event_excel_report()
{
	mainForm.action = "<%=rootPath%>/servicecapreport.do?action=downloadEventReport&ipaddress=<%=ipaddress%>&str=1";
	mainForm.submit();
}
function network_event_pdf_report()
{
	mainForm.action = "<%=rootPath%>/servicecapreport.do?action=downloadEventReport&ipaddress=<%=ipaddress%>&str=2";
	mainForm.submit();
}
function network_event_ok()
{

	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/web.do?action=showServiceEventReport&ipaddress=<%=ipaddress%>&id=<%=id%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
}
function network_event_cancel()
{
window.close();
}
function query(){
  	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;      
  	var oids ="";
  	var checkbox = document.getElementsByName("checkbox");
 		for (var i=0;i<checkbox.length;i++){
 			if(checkbox[i].checked==true){
 				if (oids==""){
 					oids=checkbox[i].value;
 				}else{
 					oids=oids+","+checkbox[i].value;
 				}
 			}
 		}
 	if(oids==null||oids==""){
 		alert("������ѡ��һ���豸");
 		return;
 	}
 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
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
	setClass();
}

function setClass(){
	document.getElementById('hostReportTitle-5').className='detail-data-title';
	document.getElementById('hostReportTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
	</head>
	<body id="body" class="body" onload="init();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden id="ipaddress" name="ipaddress"
				value=<%=request.getParameter("ipaddress")%>>
			<table id="container-main" class="container-main">
				<tr>
					<td>
						<table id="container-main-win" class="container-main-win">
							<tr>
								<td>
									<table id="win-content" class="win-content">
										<tr>
											<td>
												<table id="win-content-header" class="win-content-header">
													<tr>
														<td align="left" width="5">
															<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																width="5" height="29" />
														</td>
														<td class="win-content-title" style="align: center">
															&nbsp;�¼�����
														</td>
														<td align="right">
															<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																width="5" height="29" />
														</td>
													</tr>

												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="win-content-body" class="win-content-body">
													<tr>
														<td>
															<table bgcolor="#ECECEC">
																<tr align="left" valign="center">
																	<td height="28" align="left">
																		��ʼ����
																		<input type="text" id="mystartdate" name="startdate"
																			value="<%=startdate%>" size="10">
																		<a onclick="event.cancelBubble=true;"
																			href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																			<img id=imageCalendar1 align=absmiddle width=34
																				height=21
																				src="<%=rootPath%>/include/calendar/button.gif"
																				border=0>
																		</a> ��ֹ����
																		<input type="text" id="mytodate" name="todate"
																			value="<%=todate%>" size="10" />
																		<a onclick="event.cancelBubble=true;"
																			href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																			<img id=imageCalendar2 align=absmiddle width=34
																				height=21
																				src="<%=rootPath%>/include/calendar/button.gif"
																				border=0>
																		</a>
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

																		����״̬
																		<select id="status" name="status">
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
																		<input type="button" name="doprocess" value="ȷ��"
																			onclick="network_event_ok()">
																	</td>
																	<td height="28" align="left">
																		<a href="javascript:network_event_word_report()"><img
																				name="selDay1" alt='����word' style="CURSOR: hand"
																				src="<%=rootPath%>/resource/image/export_word.gif"
																				width=18 border="0">����WORLD</a>
																	</td>
																	<td height="28" align="left">
																		<a href="javascript:network_event_excel_report()"><img
																				name="selDay1" alt='����EXCEL' style="CURSOR: hand"
																				src="<%=rootPath%>/resource/image/export_excel.gif"
																				width=18 border="0">����EXCEL</a>
																	</td>
																	<td height="28" align="left">
																		&nbsp;
																		<a href="javascript:network_event_pdf_report()"><img
																				name="selDay1" alt='����word' style="CURSOR: hand"
																				src="<%=rootPath%>/resource/image/export_pdf.gif"
																				width=18 border="0">����PDF</a>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td class="win-data-title" style="height: 29px;"></td>
													</tr>
													<tr align="left" valign="center">
														<td height="28" align="left" border="0">
															<input type=hidden name="eventid">
															<div id="loading">
																<div class="loading-indicator">
																	<img
																		src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
																		width="32" height="32" style="margin-right: 8px;"
																		align="middle" />
																	Loading...
																</div>
															</div>
															<table align="center" border="1" width="90%">
																<tr>
																	<td colspan="12" align="center">
																		Web�����¼�����
																	</td>
																</tr>
																<tr>
																	<td>
																		&nbsp;
																	</td>
																	<td>
																		IP��ַ
																	</td>
																	<td>
																		��������
																	</td>
																	<td>
																		�¼�����(��)
																	</td>
																	<td>
																		��ͨ(��)
																	</td>
																	<td>
																		����(��)
																	</td>
																	<td>
																		����(��)
																	</td>
																</tr>
																<%
																	if (eventlist != null && eventlist.size() > 0) {
																		for (int i = 0; i < eventlist.size(); i++) {
																			List _eventlist = (List) eventlist.get(i);
																			String ip = (String) _eventlist.get(0);
																			String equname = (String) _eventlist.get(1);

																			String sum = (String) _eventlist.get(2);
																			String levelone = (String) _eventlist.get(3);
																			String leveltwo = (String) _eventlist.get(4);
																			String levelthree = (String) _eventlist.get(5);

																			String cell11 = i + 1 + "";
																			String cell12 = ip;
																			String cell13 = equname;

																			String cell14 = sum;
																			String cell15 = levelone;
																			String cell16 = leveltwo;
																			String cell17 = levelthree;
																%>
																<tr>
																	<td><%=cell11%></td>
																	<td><%=cell12%></td>
																	<td><%=cell13%></td>
																	<td><%=cell14%></td>
																	<td><%=cell15%></td>
																	<td><%=cell16%></td>
																	<td><%=cell17%></td>
																</tr>
																<%
																	}
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
				<tr>
					<td>
						<table width="100%" border="1" cellspacing="0" cellpadding="0">
							<tr height="28" bgcolor="#ECECEC">
								<td align=center>
									�¼��б�
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr bgcolor="DEEBF7">
					<td>
						<table width="100%" border="0" cellpadding="3" cellspacing="1"
							bgcolor="#FFFFFF">

							<tr height="28" bgcolor="#ECECEC">
								<td class="application-detail-data-body-title">
									&nbsp;
								</td>
								<td class="application-detail-data-body-title" width="10%">
									<strong>�¼��ȼ�</strong>
								</td>
								<td class="application-detail-data-body-title" width="40%">
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
							</tr>
							<%
								int index = 0;
								java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat(
										"MM-dd HH:mm");
								List list = (ArrayList) request.getAttribute("list");
								
								if (list != null && list.size() > 0) {
									for (int i = 0; i < list.size(); i++) {
										index++;
										EventList eventlist1 = (EventList) list.get(i);
										Date cc = eventlist1.getRecordtime().getTime();
										Integer eventid = eventlist1.getId();
										String eventlocation = eventlist1.getEventlocation();
										String content = eventlist1.getContent();
										String level = String.valueOf(eventlist1.getLevel1());
										String status = String.valueOf(eventlist1.getManagesign());
										String s = status;
										String showlevel = null;
										String act = "������";
										if ("1".equals(level)) {
											showlevel = "��ͨ�¼�";
										} else if ("2".equals(level)) {
											showlevel = "�����¼�";
										} else {
											showlevel = "�����澯";
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
										String rptman = eventlist1.getReportman();
										String rtime1 = _sdf.format(cc);
							%>

							<tr bgcolor="#FFFFFF">

								<td class="application-detail-data-body-list">
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
								<td class="application-detail-data-body-list">
									<%=content%></td>
								<td class="application-detail-data-body-list">
									<%=rtime1%></td>
								<td class="application-detail-data-body-list">
									<%=rptman%></td>
								<td class="application-detail-data-body-list">
									<%=status%></td>
							</tr>
							<%
								}
								}
							%>
						</table>
					</td>
				</tr>
			</table>

			<div align=center>
				<input type=button value="�رմ���" onclick="window.close()">
			</div>
			<br>
		</form>
	</body>
</html>