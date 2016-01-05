<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>



<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
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
<%@ page import="com.afunms.event.model.EventList"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.common.util.CreatePiePicture"%>


<%
try{
 String rootPath = request.getContextPath();;
 DBVo vo  = (DBVo)request.getAttribute("db");	
 String id = (String)request.getAttribute("id");
  String myip = vo.getIpAddress();
  String myport = vo.getPort();
  String myUser = vo.getUser();
  String myPassword = EncryptUtil.decode(vo.getPassword());
  String mysid = "";
  String dbPage = "informixevent";
  String dbType = "informix"; 
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
String chart1 = (String)request.getAttribute("chart1");
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");
Hashtable dbinfo = new Hashtable();
dbinfo = (Hashtable)request.getAttribute("dbValue");
String name="";
String dbserver="";
String createuser="";
String createtime="";
ArrayList dbdatabase = new ArrayList();
if(dbinfo!=null) 
   dbdatabase = (ArrayList)dbinfo.get("databaselist");//���ݿ������Ϣ
Hashtable databaseinfo = new Hashtable();
 
String _flag = (String)request.getAttribute("flag");

if(dbdatabase!=null&&dbdatabase.size()>0){
databaseinfo = (Hashtable)dbdatabase.get(0); 
  databaseinfo = (Hashtable)dbdatabase.get(0);  
  name=(String)databaseinfo.get("dbname");
  dbserver=(String)databaseinfo.get("dbserver");
  createuser=(String)databaseinfo.get("createuser");
  createtime=(String)databaseinfo.get("createtime");
}  
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
int level1 = 0;
if(request.getAttribute("level1") != null)level1=Integer.parseInt(request.getAttribute("level1")+"");
int _status = -1;
if(request.getAttribute("status") != null)_status = Integer.parseInt(request.getAttribute("status")+"");

String level0str="";
String level1str="";
String level2str="";
String level3str="";
if(level1 == 0){
	level0str = "selected";
}else if(level1 == 1){
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
double avgpingcon = (Double)request.getAttribute("avgpingcon");
int percent1 = Double.valueOf(avgpingcon).intValue();
int percent2 = 100-percent1;	

	String picip = CommonUtil.doip(myip);
	
    	//���ɵ���ƽ����ͨ��ͼ��
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createAvgPingPic(picip,avgpingcon); 

StringBuffer dataStr2 = new StringBuffer();
		dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(
				";false;7CFC00\\n");
		dataStr2.append("δ��ͨ;").append(100 - Math.round(avgpingcon))
				.append(";false;FF0000\\n");
		String avgdata = dataStr2.toString(); 

		  	   
%>


<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

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
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}

</script>
		<script language="javascript">	

function batchAccfiEvent(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	//batchDoReport();
	function batchDoReport(){
		 var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	function batchEditAlarmLevel(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
	 mainForm.action = "<%=rootPath%>/informix.do?action=informixevent&id=<%=vo.getId()%>"; 
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
	document.getElementById('inforDetailTitle-5').className='detail-data-title';
	document.getElementById('inforDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('inforDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>

 <script>
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=id%>&nowtime="+(new Date()),
			success:function(data){
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "ƽ����ͨ��" });
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
</script>



	</head>
	<body id="body" class="body" onload="initmenu();">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
					width="32" height="32" style="margin-right: 8px;" align="middle" />
				Loading...
			</div>
		</div>
		<div id="loading-mask" style=""></div>
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="id" id="id" value="<%=vo.getId()%>">
			<input type=hidden name="flag" id="flag" value="<%=_flag%>">

			<table id="body-container" class="body-container"  height="100%">
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
												<table class="container-main-application-detail">
													<tr>
														
														<td valign="top">
															<jsp:include page="/topology/includejsp/db_informix.jsp" >
																<jsp:param name="dbtye" value="<%=dbtye %>"/>
																<jsp:param name="managed" value="<%=managed %>"/>
																<jsp:param name="runstr" value="<%=runstr %>"/>
																<jsp:param name="ipAddress" value="<%=vo.getIpAddress() %>"/>
																<jsp:param name="name" value="<%=name %>"/>
																<jsp:param name="dbserver" value="<%=dbserver %>"/>
																<jsp:param name="createuser" value="<%=createuser %>"/>
																<jsp:param name="createuser" value="<%=createuser %>"/>
																<jsp:param name="createtime" value="<%=createtime %>"/>
																<jsp:param name="picip" value="<%=picip %>"/>
																<jsp:param name="avgdata" value="<%=avgdata %>"/> 
															</jsp:include>
														</td>
														
														<script>
														Ext.onReady(function()
														{  
														
														setTimeout(function(){
															        Ext.get('loading').remove();
															        Ext.get('loading-mask').fadeOut({remove:true});
															    }, 250);
															    Ext.get("process").on("click",function(){
														
														  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
														  mainForm.action = "<%=rootPath%>/informix.do?action=sychronizeData&dbPage=<%=dbPage %>&id=<%=id %>&myip=<%=myip %>&myport=<%=myport %>&myUser=<%=myUser %>&myPassword=<%=myPassword %>&sid=<%=mysid %>&flag=<%=_flag%>";
														  mainForm.submit();
														 });    
														});
														</script>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="application-detail-data"
													class="application-detail-data">
													<tr>
														<td class="detail-data-header">
															<%=inforDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr bgcolor="#ECECEC" height="28">
																	<td colspan=5>


																		��ʼ����
																		<input type="text" name="startdate" value="<%=startdate%>" size="10">
																		<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																			<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0>
																		</a> ��ֹ����
																		<input type="text" name="todate" value="<%=todate%>" size="10" />
																		<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																			<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0>
																		</a> �¼��ȼ�
																		<select name="level1">
																			<option value="99">
																				����
																			</option>
																			<option value="0" <%=level0str%>>
																				��ʾ��Ϣ
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
																			value="�� ѯ" onclick="#"><hr>
																	</td>
																</tr>
																<tr align="right" bgcolor="#ECECEC">
																	<td><table><tr>
																	<td width="75%">&nbsp;</td>
																	<td width="15" height=15 >&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="���ܴ���" onclick='batchAccfiEvent();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="��д����" onclick='batchDoReport();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="�޸ĵȼ�" onclick="batchEditAlarmLevel();">&nbsp;&nbsp;</td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	</tr></table></td>
																</tr>
																<tr bgcolor="#ECECEC">
																	<td>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1" bgcolor="#ECECEC">

																			<tr bgcolor="#ECECEC" height="28">
																				<td class="application-detail-data-body-title">
																					<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">
																				</td>
																				<td width="10%"
																					class="application-detail-data-body-title">
																					<strong>�¼��ȼ�</strong>
																				</td>
																				<td width="40%"
																					class="application-detail-data-body-title">
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
																				<td class="application-detail-data-body-title"
																					align="center">
																					<strong>����</strong>
																				</td>
																			</tr>
<%
	int index = 0;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	List list = (List)request.getAttribute("list");
  	if(list == null)list = new ArrayList();
  	for(int i=0;i<list.size();i++){
 	index++;
  	EventList eventlist = (EventList)list.get(i);
  	Date cc = eventlist.getRecordtime().getTime();
  	Integer eventid = eventlist.getId();
  	String eventlocation = eventlist.getEventlocation();
  	String content = eventlist.getContent();
  	String level = String.valueOf(eventlist.getLevel1());
  	String status = String.valueOf(eventlist.getManagesign());
  	String s = status;
	String showlevel = null;
  	String bgcolor = "";
  	String act="������";
  	if("0".equals(level)){
  		level="��ʾ��Ϣ";
  		bgcolor = "bgcolor='blue'";
  	}
	if("1".equals(level)){
  		level="��ͨ�澯";
  		bgcolor = "bgcolor='yellow'";
  	}
  	if("2".equals(level)){
  		level="���ظ澯";
  		bgcolor = "bgcolor='orange'";
  	}
  	if("3".equals(level)){
  		level="�����澯";
  		bgcolor = "bgcolor='red'";
  	}
   	String bgcolorstr="";
  	if("0".equals(status)){
  		status = "δ����";
  		bgcolorstr="#9966FF";
  	}
  	if("1".equals(status)){
  		status = "������";  
  		bgcolorstr="#3399CC";	
  	}
  	if("2".equals(status)){
  	  	status = "�������";
  	  	bgcolorstr="#33CC33";
  	}
  	String rptman = eventlist.getReportman();
  	String rtime1 = _sdf.format(cc);
%>

																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">

																				<td class="application-detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	  																			<td class="application-detail-data-body-list" <%=bgcolor%>><%=level%></td>
																				<td class="application-detail-data-body-list">
																					<%=content%></td>
																				<td class="application-detail-data-body-list">
																					<%=rtime1%></td>
																				<td class="application-detail-data-body-list">
																					<%=rptman%></td>
																				<td class="application-detail-data-body-list" bgcolor=<%=bgcolorstr%>>
																					<%=status%></td>
																				<td class="application-detail-data-body-list"
																					align="center">
																					<%
       		if("0".equals(s)){
       		%>
																					<input type="button" value="���ܴ���" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																					<%
       		}
       		if("1".equals(s)){
       		%>
																					<input type="button" value="��д����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																					<%
       		}
       		if("2".equals(s)){
       		%>
																					<input type="button" value="�鿴����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
																					<%
       		}
       %>
																				</td>
																			</tr>
																			<%}

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
					<td valign=top width=15%>
															<jsp:include page="/include/dbtoolbar.jsp">
																<jsp:param value="<%=myip %>" name="myip" />
																<jsp:param value="<%=myport %>" name="myport" />
																<jsp:param value="<%=myUser %>" name="myUser" />
																<jsp:param value="<%=myPassword %>" name="myPassword" />
																<jsp:param value="<%=mysid  %>" name="sid" />
																<jsp:param value="<%=id %>" name="id" />
																<jsp:param value="<%=dbPage %>" name="dbPage" />
																<jsp:param value="<%=dbType %>" name="dbType" />
																<jsp:param value="informix" name="subtype" />
															</jsp:include>
														</td>
				</tr>
			</table>

		</form>
		<%}catch(Exception e){e.printStackTrace();} %>
		
	</BODY>
</HTML>