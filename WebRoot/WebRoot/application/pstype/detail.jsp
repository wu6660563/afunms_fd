<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>



<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>

<%@page import="java.util.*"%>
<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

	List socketlist = (List) request.getAttribute("socketlist");
	PSTypeVo queryconf = (PSTypeVo) request.getAttribute("initconf");

	int status = 0;
	Node node = (Node) PollingEngine.getInstance().getSocketByID(queryconf.getId());
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

	String flag_1 = (String) request.getAttribute("flag");
	Integer myId = (Integer) request.getAttribute("id");
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
	document.getElementById('psDetailTitle-0').className='detail-data-title';
	document.getElementById('psDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('psDetailTitle-0').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
		
}


function show_graph(){
      mainForm.action = "<%=rootPath%>/pstype.do?action=detail";
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
								<td class="td-container-main-application-detail">
									<table id="container-main-application-detail"
										class="container-main-application-detail">
										<tr>
											<td>
												<table id="application-detail-content"
													class="application-detail-content">
													<tr>
														<td>
															<table id="application-detail-content-header"
																class="application-detail-content-header">
																<tr>
																	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																	<td class="application-detail-content-title">
																		�˿� ��ϸ��Ϣ
																	</td>
																	<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="application-detail-content-body"
																class="application-detail-content-body">
																<tr>
																	<td>

																		<table>
																			<tr>
																				<td>

																					&nbsp;&nbsp;��������
																					<select name="id">
																						<%
																							if (socketlist != null && socketlist.size() > 0) {
																								for (int i = 0; i < socketlist.size(); i++) {
																									PSTypeVo webconfig = (PSTypeVo) socketlist.get(i);
																									if (webconfig.getId() == queryconf.getId()) {
																						%>

																						<option value="<%=webconfig.getId()%>"
																							selected="selected">
																							<%=webconfig.getIpaddress()%>:<%=webconfig.getPortdesc()%></option>

																						<%
																							} else {
																						%>
																						<option value="<%=webconfig.getId()%>">
																							<%=webconfig.getIpaddress()%>:<%=webconfig.getPortdesc()%></option>
																						<%
																							}
																								}
																							}
																						%>
																					</select>
																					<input type="button" onclick="show_graph()"
																						class=button value="��ѯ">
																					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																					<br>
																				</td>


																			</tr>
																			<tr>
																				<td>
																					<table cellspacing="20">
																						<tr>
																							<td width="52%" align="center" valign=top>
																								<table style="BORDER-COLLAPSE: collapse"
																									bordercolor=#cedefa cellpadding=0 rules=none
																									width=100% align=center border=1 algin="center">
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;����:
																										</td>
																										<td width="70%"><%=queryconf.getPortdesc()%></td>
																									</tr>
																									<tr>
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;����:
																										</td>
																										<td width="70%">
																											�˿ڷ������
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
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
																											&nbsp;�˿�:
																										</td>
																										<td>
																											<%=queryconf.getPort()%></td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align=left nowrap>
																											&nbsp;IP��ַ:
																										</td>
																										<td width="70%"><%=queryconf.getIpaddress()%></td>
																									</tr>
																									<tr>
																										<td height="29" align="left">
																											&nbsp;���ݲɼ�ʱ��:
																										</td>
																										<td><%=request.getAttribute("lasttime")%></td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td height="29" align="left">
																											&nbsp;���ݲɼ���ʽ:
																										</td>
																										<td>
																											Socket
																										</td>
																									</tr>
																									<tr>
																										<td height="29" class=txtGlobal valign=center
																											nowrap>
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
																									<tr>
																										<td height="29" align="left">
																											&nbsp;
																										</td>
																										<td>
																											&nbsp;
																										</td>
																									</tr>

																								</table>

																								<!--<table width="100%" cellspacing="0" cellpadding="0" align="center">
																					            	<tr> 
																					         		<td width="100%" align="center"> 
																					         			<div id="flashcontent1">
																											<strong>You need to upgrade your Flash Player</strong>
																										</div>
																										<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Port_Info.swf?ip=<%=queryconf.getIpaddress()%>&alias=<%=queryconf.getPortdesc()%>&port=<%=queryconf.getPort()%>&ip=<%=queryconf.getIpaddress()%>&lasttime=<%=request.getAttribute("lasttime")%>&nexttime=<%=request.getAttribute("nexttime")%>", "Port_Info", "400", "250", "8", "#ffffff");
																											so.write("flashcontent1");
																										</script>				
																					                	</td>
																							</tr> 
																				          		</table>-->

																							</td>
																							<td width="48%" align="center">
																								<table width="100%" cellspacing="0"
																									cellpadding="0" align="center">
																									<tr>
																										<td width="100%" align="center">
																											<div id="flashcontent2">
																												<strong>You need to upgrade your
																													Flash Player</strong>
																											</div>
																											<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Port_Ping_Pie.swf?ip=<%=queryconf.getIpaddress()%>&port=<%=queryconf.getPort()%>", "Port_Ping_Pie", "400", "250", "8", "#ffffff");
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
																		</table>


																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="application-detail-content-footer"
																class="application-detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
															<%=psDetailTitleTable%>
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
																				<td width="48%" align="center">
																					<table width="100%" cellspacing="0" cellpadding="0"
																						align="center">
																						<tr>
																							<td width="100%" align="center">
																								<div id="flashcontent3">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Port_Ping_Line.swf?ip=<%=queryconf.getIpaddress()%>&port=<%=queryconf.getPort()%>", "Port_Ping_Line", "400", "250", "8", "#ffffff");
																											so.write("flashcontent3");
																										</script>
																							</td>
																						</tr>
																					</table>
																				</td>
																				<td>&nbsp;</td>
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
							</tr>
						</table>
					</td>
					<td width=15% valign=top>
						<jsp:include page="/include/sockettoolbar.jsp">
							<jsp:param value="<%=queryconf.getId()%>" name="id" />
						</jsp:include>
					</td>
				</tr>
			</table>

		</form>
		<script>			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/pstype.do?action=sychronizeData&id=<%=queryconf.getId()%>&flag=<%=flag_1%>";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>