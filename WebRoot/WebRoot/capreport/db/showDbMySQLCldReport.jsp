<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<html>
<head>
<%	
	DBVo vo  = (DBVo)request.getAttribute("db");
	Vector val=(Vector)request.getAttribute("Val");
	List sessionlist=(List)request.getAttribute("sessionlist");
	String downnum= (String)request.getAttribute("downnum");//崻���
    Integer count = (Integer)request.getAttribute("count");//��ø澯���� 
    Hashtable tablesHash=(Hashtable)request.getAttribute("tablesHash");
    Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");
	//double avgpingcon = (Double)request.getAttribute("avgpingcon");
	//int percent1 = Double.valueOf(avgpingcon).intValue();
	//int percent2 = 100-percent1;	
	
	String avgpingcon =(String) request.getAttribute("avgpingcon");
  	String rootPath = request.getContextPath();
  	String newip = (String)request.getAttribute("newIp");
  	String ip = (String)request.getAttribute("ipaddress");
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	Hashtable dbValue = new Hashtable();
  	Hashtable alldatabase = new Hashtable();
  	Vector names = new Vector();
    dbValue	= (Hashtable)request.getAttribute("dbValue");
    request.setAttribute("ipaddress",ip);
  	String pingmin = (String) request.getAttribute("pingmin");
  	String pingmax = (String) request.getAttribute("pingmax");
  	String pingnow = (String) request.getAttribute("pingnow");
    System.out.println("avgpingcon =  "+avgpingcon+"rootPath =  "+rootPath+"newip =  "+newip+"ip =  "+ip+"dbValue =  "+dbValue);
    
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
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<!-- �ۺϱ��� mysql-->
<title>�ۺϱ���</title>
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function ping_excel_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_word_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_ok(){
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	var status = document.getElementById("status").value;
	var level1 = document.getElementById("level1").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/mysql.do?action=mysqlManagerCldReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime+"&status="+status+"&level1="+level1;
	mainForm.submit();
}
function ping_cancel()
{
window.close();
}
//function query(){
  //	var startdate = mainForm.startdate.value;
  //	var todate = mainForm.todate.value;      
  //	var oids ="";
  //	var checkbox = document.getElementsByName("checkbox");
 	//	for (var i=0;i<checkbox.length;i++){
 			//if(checkbox[i].checked==true){
 			//	if (oids==""){
 			//		oids=checkbox[i].value;
 			//	}else{
 			//		oids=oids+","+checkbox[i].value;
 			//	}
 			//}
 	//	}
 	//if(oids==null||oids==""){
 	//	alert("������ѡ��һ���豸");
 	//	return;
 	//}
// 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
//}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
//function changeOrder(para){
//	mainForm.orderflag.value = para;
//	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
 // 	mainForm.submit();
//}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
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

//function refer(action){
//		var mainForm = document.getElementById("mainForm");
//		mainForm.action = '<%=rootPath%>' + action;
//		mainForm.submit();
//}
</script>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<body id="body" class="body" onload="init();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type="hidden"id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
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
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" ></td>
								                	<td class="win-content-title" style="align:center">&nbsp;�ۺϱ���</td>
								                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
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
																					<input type="text" id="mystartdate" name="startdate" value="<%=startdate%>" size="10">
																					<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																					<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																													
																					��ֹ����
																					<input type="text" id="mytodate" name="todate" value=" <%=todate%>" size="10"/>
																					<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																					<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																					
																					
																					 �¼��ȼ�
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
																					<input type="button" id="process" name="process"
																						value="�� ѯ" onclick="ping_ok();">
															</td>
															<td height="28" align="left">
																<a href="javascript:ping_word_report()"><img name="selDay1" alt='����word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">����WORLD</a>
															</td>
															<td height="28" align="left">
																<a href="javascript:ping_excel_report()"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL</a>
															</td>
															<td height="28" align="left">&nbsp;
																<a href="javascript:ping_pdf_report()"><img name="selDay1" alt='����word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">����PDF</a>
															</td>
															</tr>  
														</table>
						       						</td>
						       					</tr>
												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr>
							       				<tr align="left" valign="center"> 
			             							<td height="28" align="left" border="0">
														<input type=hidden name="eventid">
														<div id="loading">
														<div class="loading-indicator">
															<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
														</div>
														<table border="1" width="90%">
															<tr>
																<td>��ͨ��</td><td>��ǰ��ͨ��</td><td>��С��ͨ��</td><td>ƽ����ͨ��</td>
															</tr>
															<tr>
																<td>&nbsp;</td><td><%=pingnow %>%</td><td><%=pingmin %>%</td><td><%=avgpingcon %>%</td>
															</tr>
														</table>
														<tr>
																<td colspan="4" align="center">
																<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip%>ConnectUtilization.png"/>
																</td>
															</tr>

												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr> 
							       				<tr>
							       					<td>
							       						<table class="application-detail-data-body" width="989" height="149">
							       						
							       							<tr align="left" bgcolor="#ECECEC">
																<td height="28">
																	<table cellspacing="1" cellpadding="0" width="100%">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					&nbsp;&nbsp;
																					<b>���ݿ�������Ϣ</b>
																				</td>
																			</tr>
																			<%   
																            	if(val != null && val.size()>0){
																            		for(int i=0;i<val.size();i++){
																            			Hashtable return_value = (Hashtable)val.get(i);
																            			if(return_value != null && return_value.size()>0){
																            				String name=return_value.get("variable_name").toString();
																            				String value=return_value.get("value").toString();
																            				if(name.equalsIgnoreCase("Max_used_connections"))
																					{
																					 name="��������Ӧ�����������";
																					}
																					if(name.equalsIgnoreCase("Handler_read_first"))
																					{
																					 name="�����е�һ�������Ĵ���";
																					}
																					if(name.equalsIgnoreCase("Handler_read_key"))
																					{
																					 name="���ݼ���һ�е�������";
																					}
																					if(name.equalsIgnoreCase("Handler_read_next"))
																					{
																					 name="���ռ�˳�����һ�е�������";
																					}
																					if(name.equalsIgnoreCase("Handler_read_prev"))
																					{
																					 name="���ռ�˳���ǰһ�е�������";
																					}
																					if(name.equalsIgnoreCase("Handler_read_rnd"))
																					{
																					 name="H���ݹ̶�λ�ö�һ�е�������";
																					}
																					if(name.equalsIgnoreCase("Handler_read_rnd_next"))
																					{
																					 name="�������ļ��ж���һ�е�������";
																					}
																					if(name.equalsIgnoreCase("Open_tables"))
																					{
																					 name="��ǰ�򿪵ı������";
																					}
																					if(name.equalsIgnoreCase("Opened_tables"))
																					{
																					 name="�Ѿ��򿪵ı������";
																					}
																					if(name.equalsIgnoreCase("Threads_cached"))
																					{
																					 name="�̻߳����ڵ��̵߳�����";
																					}
																					if(name.equalsIgnoreCase("Threads_connected"))
																					{
																					 name="��ǰ�򿪵����ӵ�����";
																					}
																					if(name.equalsIgnoreCase("Threads_created"))
																					{
																					 name="���������������ӵ��߳���";
																					}
																					if(name.equalsIgnoreCase("Threads_running"))
																					{
																					 name="����ķ�˯��״̬���߳���";
																					}
																					if(name.equalsIgnoreCase("Table_locks_immediate"))
																					{
																					 name="������õı�����Ĵ���";
																					}
																					if(name.equalsIgnoreCase("Table_locks_waited"))
																					{
																					 name="����������õı�����Ĵ���";
																					}
																					if(name.equalsIgnoreCase("Key_read_requests"))
																					{
																					 name="�ӻ�����������ݿ��������";
																					}
																					if(name.equalsIgnoreCase("Key_reads"))
																					{
																					 name="��Ӳ�̶�ȡ�������ݿ�Ĵ���";
																					}
																					if(name.equalsIgnoreCase("log_slow_queries"))
																					{
																					 name="�Ƿ��¼����ѯ";
																					}
																					if(name.equalsIgnoreCase("slow_launch_time"))
																					{
																					 name="�����̵߳�ʱ�䳬��������������������Slow_launch_threads״̬����";
																					}
																             %>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																						height=28>
																				<td width=10%
																					class="application-detail-data-body-list">
																					&nbsp;
																				</td>
																				<td align=left width=50%
																					class="application-detail-data-body-list">
																					&nbsp;<%=name%></td>
																				<td align=left
																					class="application-detail-data-body-list">
																					&nbsp;<%=value%></td>
																				<td width=10%
																					class="application-detail-data-body-list">
																					&nbsp;
																				</td>
																			</tr>
																				<%		}
																	         		}
																	          	}
																	           %>
																		</table>
																</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																	<td align=center>
																		<table border="0" id="table1" cellpadding="0"
																			cellspacing="0" width="80%">
																			<tr border="1">
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6 border="1">
																					&nbsp;&nbsp;
																					<b>������Ϣ</b>
																				</td>
																			</tr>
																			<TBODY>
																				<tr style="background-color: #ECECEC;" height=28>
																					<td align=center>
																						&nbsp;
																					</td>
																					<td align=center>
																						���ݿ�
																					</td>
																					<td align=center>
																						�û���
																					</td>
																					<td align=center>
																						����
																					</td>
																					<td align=center>
																						����
																					</td>
																					<td align=center>
																						����ʱ��
																					</td>
																				</tr>
																				<% 
																                  if(sessionlist != null && sessionlist.size()>0){
																                  	for(int i=0;i<sessionlist.size();i++){
																                  		List ipsessionlist = (List)sessionlist.get(i);
																                  		if(ipsessionlist != null && ipsessionlist.size()>0){
																                  			for(int k=0;k<ipsessionlist.size();k++){
																                  				String[] sessions = (String[])ipsessionlist.get(k);
																                  				if(sessions != null && sessions.length ==5){
																                  %>
																				<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																					height="28">
																					<td align=center
																						class="application-detail-data-body-list"><%=k+1%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[4]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[0]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[1]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[2]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[3]%></td>

																				</tr>
																				<%
																	          					}
																	          				}
																	          			}
																	          		}
																	          	}
																	          	%>
																			</TBODY>
																		</table>
																	</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																<td align=center>
																	<table border="0" id="table1" cellpadding="0"
																		cellspacing="0" width="80%">
																		<tr border="1">
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6 border="1">
																					&nbsp;&nbsp;
																					<b>����Ϣ</b>
																				</td>
																		</tr>
																		<%
														                  String[] dbs = vo.getDbName().split(","); 
														                  if(dbs != null && dbs.length>0){
														                  	for(int i=0;i<dbs.length;i++){
														                  		String dbStr = dbs[i];
														                  		//System.out.println(dbStr+"====="+tablesHash.size());
														                  		if(tablesHash.containsKey(dbStr)){
														                  			List tableslist = (List)tablesHash.get(dbStr);
														                  %>
																		<TBODY>
																			<tr style="background-color: #ECECEC;" height=28>
																				<td align=center>
																					&nbsp;
																				</td>
																				<td align=center>
																					����
																				</td>
																				<td align=center>
																					������
																				</td>
																				<td align=center>
																					���С
																				</td>
																				<td align=center>
																					����ʱ��
																				</td>
																			</tr>
																			<%
              			
																              		if(tableslist != null && tableslist.size()>0){
																              			for(int k=0;k<tableslist.size();k++){
																              				String[] tables = (String[])tableslist.get(k);
																              				if(tables != null && tables.length ==4){
																              %>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"
																					align="center"><%=k+1%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[0]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[1]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[2]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[3]%></td>

																			</tr>
																			<%
																	      					}
																	      				}
																	      			}
																	
																	       		}
																	       	%>
																		</TBODY>
																		<tr>
																			<td heigh=10>
																				&nbsp;
																			</td>
																		</tr>
																		<%
															          	}
															          	}
															          	%>

																	</table>
																</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
							       							<tr>
																	<td>
																		<table cellspacing="1" cellpadding="0" width="100%">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					<b> ���ݿ�״̬��Ϣ</b>
																				</td>
																			</tr>

																			<%
			if(tableinfo_v != null)
			{
				for(int i=0;i<tableinfo_v.size();i++){
					Hashtable ht = (Hashtable)tableinfo_v.get(i);
					String size = ht.get("variable_name").toString();
					String free = ht.get("value").toString();
					if(size.equalsIgnoreCase("auto_increment_increment"))
					{
					 size="�������е�ֵ������ֵ";
					}
					if(size.equalsIgnoreCase("auto_increment_offset"))
					{
					 size="ȷ��AUTO_INCREMENT��ֵ�����";
					}
					if(size.equalsIgnoreCase("automatic_sp_privileges"))
					{
					 size="automatic_sp_privileges";
					}
					if(size.equalsIgnoreCase("back_log"))
					{
					 size="�������������";
					}
					if(size.equalsIgnoreCase("basedir"))
					{
					 size="MySQL��װ��׼Ŀ¼";
					}
					if(size.equalsIgnoreCase("binlog_cache_size"))
					{
					 size="���ɶ�������־SQL���Ļ����С";
					}
					if(size.equalsIgnoreCase("bulk_insert_buffer_size"))
					{
					 size="ÿ�̵߳��ֽ������ƻ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("character_set_client"))
					{
					 size="���Կͻ��˵������ַ���";
					}
					if(size.equalsIgnoreCase("character_set_connection"))
					{
					 size="û���ַ�����������ַ���ת��";
					}
					if(size.equalsIgnoreCase("character_set_database"))
					{
					 size="Ĭ�����ݿ�ʹ�õ��ַ���";
					}
					if(size.equalsIgnoreCase("character_set_filesystem"))
					{
					 size="character_set_filesystem";
					}
					if(size.equalsIgnoreCase("character_set_results"))
					{
					 size="������ͻ��˷��ز�ѯ������ַ���";
					}
					if(size.equalsIgnoreCase("character_set_server"))
					{
					 size="��������Ĭ���ַ���";
					}
					if(size.equalsIgnoreCase("character_set_system"))
					{
					 size="��������������ʶ������ַ���";
					}
					if(size.equalsIgnoreCase("character_sets_dir"))
					{
					 size="�ַ�����װĿ¼";
					}
					if(size.equalsIgnoreCase("collation_connection"))
					{
					 size="�����ַ�����У�Թ���";
					}
					if(size.equalsIgnoreCase("collation_database"))
					{
					 size="Ĭ�����ݿ�ʹ�õ�У�Թ���";
					}
					if(size.equalsIgnoreCase("collation_server"))
					{
					 size="��������Ĭ��У�Թ���";
					}
					if(size.equalsIgnoreCase("completion_type"))
					{
					 size="�����������";
					}
					if(size.equalsIgnoreCase("concurrent_insert"))
					{
					 size="�洢ֵ���";
					}
					if(size.equalsIgnoreCase("connect_timeout"))
					{
					 size="��������Bad handshake��Ӧǰ�ȴ����Ӱ�������";
					}
					if(size.equalsIgnoreCase("datadir"))
					{
					 size="MySQL����Ŀ¼";
					}
					if(size.equalsIgnoreCase("date_format"))
					{
					 size="date_format(Ϊ��ʹ��)";
					}
					if(size.equalsIgnoreCase("datetime_format"))
					{
					 size="datetime_format(Ϊ��ʹ��)";
					}
					if(size.equalsIgnoreCase("default_week_format"))
					{
					 size="WEEK() ����ʹ�õ�Ĭ��ģʽ";
					}
					if(size.equalsIgnoreCase("delay_key_write"))
					{
					 size="ʹ�õ�DELAY_KEY_WRITE��ѡ��Ĵ���";
					}
					if(size.equalsIgnoreCase("delayed_insert_limit"))
					{
					 size="INSERT DELAYED�������̼߳���Ƿ��й����SELECT���";
					}
					if(size.equalsIgnoreCase("delayed_insert_timeout"))
					{
					 size="INSERT DELAYED�������߳���ֹǰӦ�ȴ�INSERT����ʱ��";
					}
					if(size.equalsIgnoreCase("delayed_queue_size"))
					{
					 size="����INSERT DELAYED���ʱ�������е���������";
					}
					if(size.equalsIgnoreCase("div_precision_increment"))
					{
					 size="��/������ִ�г������Ľ�������ӵľ�ȷ�ȵ�λ��";
					}
					if(size.equalsIgnoreCase("engine_condition_pushdown"))
					{
					 size="������NDB�ļ��";
					}
					if(size.equalsIgnoreCase("expire_logs_days"))
					{
					 size="��������־�Զ�ɾ��������";
					}
					if(size.equalsIgnoreCase("flush"))
					{
					 size="flushѡ������mysqldֵ";
					}
					if(size.equalsIgnoreCase("flush_time"))
					{
					 size="�鿴�ͷ���Դ���";
					}
					if(size.equalsIgnoreCase("ft_boolean_syntax"))
					{
					 size="ʹ��IN BOOLEAN MODEִ�еĲ���ȫ������֧�ֵĲ�����ϵ��";
					}
					if(size.equalsIgnoreCase("ft_max_word_len"))
					{
					 size="FULLTEXT���������������ֵ���󳤶�";
					}
					if(size.equalsIgnoreCase("ft_min_word_len"))
					{
					 size="FULLTEXT���������������ֵ���С����";
					}
					if(size.equalsIgnoreCase("ft_query_expansion_limit"))
					{
					 size="ʹ��WITH QUERY EXPANSION����ȫ�����������ƥ����";
					}
					if(size.equalsIgnoreCase("ft_stopword_file"))
					{
					 size="���ڶ�ȡȫ��������ֹͣ���嵥���ļ�";
					}
					if(size.equalsIgnoreCase("group_concat_max_len"))
					{
					 size="�����GROUP_CONCAT()�����������󳤶�";
					}
					if(size.equalsIgnoreCase("have_archive"))
					{
					 size="mysqld֧��ARCHIVE��֧�ֱ����";
					}
					if(size.equalsIgnoreCase("have_bdb"))
					{
					 size="mysqld֧��BDB�����";
					}
					if(size.equalsIgnoreCase("have_blackhole_engine"))
					{
					 size="mysqld֧��BLACKHOLE�����";
					}
					if(size.equalsIgnoreCase("have_compress"))
					{
					 size="�Ƿ�zlibѹ�����ʺϸ÷�����";
					}
					if(size.equalsIgnoreCase("have_crypt"))
					{
					 size="�Ƿ�crypt()ϵͳ�����ʺϸ÷�����";
					}
					if(size.equalsIgnoreCase("have_csv"))
					{
					 size="mysqld֧��ARCHIVE�����";
					}
					if(size.equalsIgnoreCase("have_example_engine"))
					{
					 size="mysqld֧��EXAMPLE�����";
					}
					if(size.equalsIgnoreCase("have_federated_engine"))
					{
					 size="mysqld֧��FEDERATED�����";
					}
					if(size.equalsIgnoreCase("have_geometry"))
					{
					 size="�Ƿ������֧�ֿռ���������";
					}
					if(size.equalsIgnoreCase("have_innodb"))
					{
					 size="mysqld֧��InnoDB�����";
					}
					if(size.equalsIgnoreCase("have_isam"))
					{
					 size="������";
					}
					if(size.equalsIgnoreCase("have_ndbcluster"))
					{
					 size="mysqld֧��NDB CLUSTER�����";
					}
					if(size.equalsIgnoreCase("have_openssl"))
					{
					 size="mysqld֧�ֿͻ���/������Э���SSL(����)���";
					}
					if(size.equalsIgnoreCase("have_query_cache"))
					{
					 size="mysqld֧�ֲ�ѯ�������";
					}
					if(size.equalsIgnoreCase("have_raid"))
					{
					 size="mysqld֧��RAIDѡ�����";
					}
					if(size.equalsIgnoreCase("have_rtree_keys"))
					{
					 size="RTREE�����Ƿ����";
					}
					if(size.equalsIgnoreCase("have_symlink"))
					{
					 size="�Ƿ����÷�������֧��";
					}
					if(size.equalsIgnoreCase("init_connect"))
					{
					 size="�ַ�������";
					}
					if(size.equalsIgnoreCase("init_file"))
					{
					 size="����������ʱ��--init-fileѡ��ָ�����ļ���";
					}
					if(size.equalsIgnoreCase("init_slave"))
					{
					 size="SQL�߳�����ʱ�ӷ�����Ӧִ�и��ַ���";
					}
					if(size.equalsIgnoreCase("innodb_additional_mem_pool_size"))
					{
					 size="InnoDB�����洢�����ڴ��С���";
					}
					if(size.equalsIgnoreCase("innodb_autoextend_increment"))
					{
					 size="��ռ䱻����֮ʱ��չ��ռ�ĳߴ�";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_awe_mem_mb"))
					{
					 size="����ر�����32λWindows��AWE�ڴ��ﻺ��ش�С";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_size"))
					{
					 size="InnoDB���������������ݺ��������ڴ滺�����Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_checksums"))
					{
					 size="InnoDB�����жԴ��̵�ҳ���ȡ�ϵ�״̬";
					}
					if(size.equalsIgnoreCase("innodb_commit_concurrency"))
					{
					 size="innodb_commit_concurrency";
					}
					if(size.equalsIgnoreCase("innodb_concurrency_tickets"))
					{
					 size="innodb_concurrency_tickets";
					}
					if(size.equalsIgnoreCase("innodb_data_file_path"))
					{
					 size="���������ļ������ǳߴ��·��";
					}
					if(size.equalsIgnoreCase("innodb_data_home_dir"))
					{
					 size="Ŀ¼·��������InnoDB�����ļ��Ĺ�ͬ����";
					}
					if(size.equalsIgnoreCase("innodb_doublewrite"))
					{
					 size="InnoDB�洢�����������";
					}
					if(size.equalsIgnoreCase("innodb_fast_shutdown"))
					{
					 size="InnoDB�ڹر������ֵѡ��";
					}
					if(size.equalsIgnoreCase("innodb_file_io_threads"))
					{
					 size="InnoDB���ļ�I/O�̵߳���";
					}
					if(size.equalsIgnoreCase("innodb_file_per_table"))
					{
					 size="ȷ���Ƿ�InnoDB���Լ���.ibd�ļ�Ϊ�洢���ݺ���������ÿһ���±�";
					}
					if(size.equalsIgnoreCase("innodb_flush_log_at_trx_commit"))
					{
					 size="InnoDB����־�������";
					}
					if(size.equalsIgnoreCase("innodb_flush_method"))
					{
					 size="InnoDBʹ��fsync()��ˢ�����ݺ���־�ļ�";
					}
					if(size.equalsIgnoreCase("innodb_force_recovery"))
					{
					 size="�𻵵����ݿ�ת����ķ���";
					}
					if(size.equalsIgnoreCase("innodb_lock_wait_timeout"))
					{
					 size="InnoDB�����ڱ��ع�֮ǰ���Եȴ�һ�������ĳ�ʱ����";
					}
					if(size.equalsIgnoreCase("innodb_locks_unsafe_for_binlog"))
					{
					 size="InnoDB����������ɨ���йر���һ������";
					}
					if(size.equalsIgnoreCase("innodb_log_arch_dir"))
					{
					 size="ʹ����־���� ������д�����־�ļ����ڵ�Ŀ¼�Ĺ鵵ֵ";
					}
					if(size.equalsIgnoreCase("innodb_log_archive"))
					{
					 size="��־�������";
					}
					if(size.equalsIgnoreCase("innodb_log_buffer_size"))
					{
					 size="InnoDB�����������ϵ���־�ļ�д�����Ļ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_log_file_size"))
					{
					 size="��־����ÿ����־�ļ��Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_log_files_in_group"))
					{
					 size="��־������־�ļ�����Ŀ";
					}
					if(size.equalsIgnoreCase("innodb_log_group_home_dir"))
					{
					 size="InnoDB��־�ļ���Ŀ¼·��";
					}
					if(size.equalsIgnoreCase("innodb_max_dirty_pages_pct"))
					{
					 size="InnoDB�д�����ҳ�����";
					}
					if(size.equalsIgnoreCase("innodb_max_purge_lag"))
					{
					 size="�����������ͺ�֮ʱ������ӳ�INSERT,UPDATE��DELETE����";
					}
					if(size.equalsIgnoreCase("innodb_mirrored_log_groups"))
					{
					 size="Ϊ���ݿⱣ�ֵ���־����ͬ������������";
					}
					if(size.equalsIgnoreCase("innodb_open_files"))
					{
					 size="��InnoDBһ�ο��Ա��ִ򿪵�.ibd�ļ��������";
					}
					if(size.equalsIgnoreCase("innodb_support_xa"))
					{
					 size="InnoDB֧����XA�����е�˫���ύ���";
					}
					if(size.equalsIgnoreCase("innodb_sync_spin_loops"))
					{
					 size="innodb_sync_spin_loops";
					}
					if(size.equalsIgnoreCase("innodb_table_locks"))
					{
					 size="InnoDB�Ա���������";
					}
					if(size.equalsIgnoreCase("innodb_thread_concurrency"))
					{
					 size="InnoDB������InnoDB�ڱ��ֲ���ϵͳ�̵߳��������ڻ��������������������Ʒ�Χ";
					}
					if(size.equalsIgnoreCase("innodb_thread_sleep_delay"))
					{
					 size="��InnoDBΪ���ڵ�SHOW INNODB STATUS�������һ���ļ�<datadir>/innodb_status";
					}
					if(size.equalsIgnoreCase("interactive_timeout"))
					{
					 size="�������رս���ʽ����ǰ�ȴ��������";
					}
					if(size.equalsIgnoreCase("join_buffer_size"))
					{
					 size="������ȫ���ӵĻ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("key_buffer_size"))
					{
					 size="�����黺�����Ĵ�С";
					}
					if(size.equalsIgnoreCase("key_cache_age_threshold"))
					{
					 size="���ƽ��������Ӽ�ֵ����������(sub-chain)������������(sub-chain)��ֵ";
					}
					if(size.equalsIgnoreCase("key_cache_block_size"))
					{
					 size="��ֵ�����ڿ���ֽڴ�С";
					}
					if(size.equalsIgnoreCase("key_cache_division_limit"))
					{
					 size="��ֵ���滺���������������������Ļ��ֵ�";
					}
					if(size.equalsIgnoreCase("language"))
					{
					 size="������Ϣ��������";
					}
					if(size.equalsIgnoreCase("large_files_support"))
					{
					 size="mysqld����ʱ�Ƿ�ʹ���˴��ļ�֧��ѡ��";
					}
					if(size.equalsIgnoreCase("large_page_size"))
					{
					 size="large_page_size";
					}
					if(size.equalsIgnoreCase("large_pages"))
					{
					 size="�Ƿ������˴�ҳ��֧��";
					}
					if(size.equalsIgnoreCase("license"))
					{
					 size="���������������";
					}
					if(size.equalsIgnoreCase("local_infile"))
					{
					 size="�Ƿ�LOCAL֧��LOAD DATA INFILE���";
					}
					if(size.equalsIgnoreCase("log"))
					{
					 size="�Ƿ����ý����в�ѯ��¼�������ѯ��־��";
					}
					if(size.equalsIgnoreCase("log_bin"))
					{
					 size="�Ƿ����ö�������־";
					}
					if(size.equalsIgnoreCase("log_bin_trust_function_creators"))
					{
					 size="�Ƿ�������α���ĳ�������߲��ᴴ�����������־д�벻��ȫ�¼��ĳ���";
					}
					if(size.equalsIgnoreCase("log_error"))
					{
					 size="������־��λ��";
					}
					if(size.equalsIgnoreCase("log_slave_updates"))
					{
					 size="�Ƿ�ӷ����������������յ��ĸ���Ӧ����ӷ������Լ��Ķ�������־";
					}
					if(size.equalsIgnoreCase("log_slow_queries"))
					{
					 size="�Ƿ��¼����ѯ";
					}
					if(size.equalsIgnoreCase("log_warnings"))
					{
					 size="�Ƿ��������������Ϣ";
					}
					if(size.equalsIgnoreCase("long_query_time"))
					{
					 size="��ѯʱ�䳬����ֵ��������Slow_queries״̬����";
					}
					if(size.equalsIgnoreCase("low_priority_updates"))
					{
					 size="��ʾsql���ȴ���佫�ȴ�ֱ����Ӱ��ı�û�й����SELECT��LOCK TABLE READ";
					}
					if(size.equalsIgnoreCase("lower_case_file_system"))
					{
					 size="˵���Ƿ�����Ŀ¼���ڵ��ļ�ϵͳ���ļ����Ĵ�Сд����";
					}
					if(size.equalsIgnoreCase("lower_case_table_names"))
					{
					 size="Ϊ1��ʾ������Сд���浽Ӳ���ϣ����ұ����Ƚ�ʱ���Դ�Сд����";
					}
					if(size.equalsIgnoreCase("max_allowed_packet"))
					{
					 size="�����κ����ɵ�/�м��ַ���������С";
					}
					if(size.equalsIgnoreCase("max_binlog_cache_size"))
					{
					 size="�����������Ҫ������ڴ�ʱ���ֵ����";
					}
					if(size.equalsIgnoreCase("max_binlog_size"))
					{
					 size="�����������Ҫ������ڴ�ʱ���ֵ����";
					}
					if(size.equalsIgnoreCase("max_connect_errors"))
					{
					 size="�ϵ������������ӵ����������";
					}
					if(size.equalsIgnoreCase("max_connections"))
					{
					 size="����Ĳ��пͻ���������Ŀ";
					}
					if(size.equalsIgnoreCase("max_delayed_threads"))
					{
					 size="�����߳�������INSERT DELAYED����������";
					}
					if(size.equalsIgnoreCase("max_error_count"))
					{
					 size="����SHOW ERRORS��SHOW WARNINGS��ʾ�Ĵ��󡢾����ע��������Ŀ";
					}
					if(size.equalsIgnoreCase("max_heap_table_size"))
					{
					 size="����MEMORY (HEAP)����������������ռ��С";
					}
					if(size.equalsIgnoreCase("max_insert_delayed_threads"))
					{
					 size="�����߳�������INSERT DELAYED����������(ͬmax_delayed_threads)";
					}
					if(size.equalsIgnoreCase("max_join_size"))
					{
					 size="�����������Ҫ������max_join_size�е����";
					}
					if(size.equalsIgnoreCase("max_length_for_sort_data"))
					{
					 size="ȷ��ʹ�õ�filesort�㷨������ֵ��С����ֵ";
					}
					if(size.equalsIgnoreCase("max_prepared_stmt_count"))
					{
					 size="max_prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("max_relay_log_size"))
					{
					 size="������ƴӷ�����д���м���־ʱ��������ֵ��������м���";
					}
					if(size.equalsIgnoreCase("max_seeks_for_key"))
					{
					 size="���Ƹ��ݼ�ֵѰ����ʱ�����������";
					}
					if(size.equalsIgnoreCase("max_sort_length"))
					{
					 size="����BLOB��TEXTֵʱʹ�õ��ֽ���";
					}
					if(size.equalsIgnoreCase("max_sp_recursion_depth"))
					{
					 size="max_sp_recursion_depth";
					}
					if(size.equalsIgnoreCase("max_tmp_tables"))
					{
					 size="�ͻ��˿���ͬʱ�򿪵���ʱ��������";
					}
					if(size.equalsIgnoreCase("max_user_connections"))
					{
					 size="������MySQL�˻���������ͬʱ������";
					}
					if(size.equalsIgnoreCase("max_write_lock_count"))
					{
					 size="����д�������ƺ������ֶ�����";
					}
					if(size.equalsIgnoreCase("multi_range_count"))
					{
					 size="multi_range_count";
					}
					if(size.equalsIgnoreCase("myisam_data_pointer_size"))
					{
					 size="Ĭ��ָ���С��ֵ";
					}
					if(size.equalsIgnoreCase("myisam_max_sort_file_size"))
					{
					 size="�ؽ�MyISAM����ʱ������MySQLʹ�õ���ʱ�ļ������ռ��С";
					}
					if(size.equalsIgnoreCase("myisam_recover_options"))
					{
					 size="myisam-recoverѡ���ֵ";
					}
					if(size.equalsIgnoreCase("myisam_repair_threads"))
					{
					 size="�����ֵ����1����Repair by sorting�����в��д���MyISAM������";
					}
					if(size.equalsIgnoreCase("myisam_sort_buffer_size"))
					{
					 size="��REPAIR TABLE����CREATE INDEX����������ALTER TABLE����������MyISAM��������Ļ�����";
					}
					if(size.equalsIgnoreCase("myisam_stats_method"))
					{
					 size="MyISAM���Ѽ���������ֵ�ַ���ͳ����Ϣʱ��������δ���NULLֵ";
					}
					if(size.equalsIgnoreCase("named_pipe"))
					{
					 size="���������Ƿ�֧�������ܵ�����";
					}
					if(size.equalsIgnoreCase("net_buffer_length"))
					{
					 size="�ڲ�ѯ֮�佫ͨ�Ż���������Ϊ��ֵ";
					}
					if(size.equalsIgnoreCase("net_read_timeout"))
					{
					 size="�ж϶�ǰ�ȴ����ӵ��������ݵ�����";
					}
					if(size.equalsIgnoreCase("net_retry_count"))
					{
					 size="��ʾĳ��ͨ�Ŷ˿ڵĶ������ж��ˣ��ڷ���ǰ���Զ��";
					}
					if(size.equalsIgnoreCase("net_write_timeout"))
					{
					 size="�ж�д֮ǰ�ȴ���д�����ӵ�����";
					}
					if(size.equalsIgnoreCase("new"))
					{
					 size="��ʾ��MySQL 4.0��ʹ�øñ�������4.1�е�һЩ��Ϊ����������������";
					}
					if(size.equalsIgnoreCase("old_passwords"))
					{
					 size="�Ƿ������ӦΪMySQL�û��˻�ʹ��pre-4.1-style������";
					}
					if(size.equalsIgnoreCase("open_files_limit"))
					{
					 size="����ϵͳ����mysqld�򿪵��ļ�������";
					}
					if(size.equalsIgnoreCase("optimizer_prune_level"))
					{
					 size="�ڲ�ѯ�Ż����Ż��������ռ�ü���ϣ���ֲ��ƻ���ʹ�õĿ��Ʒ��� 0��ʾ���÷���";
					}
					if(size.equalsIgnoreCase("optimizer_search_depth"))
					{
					 size="��ѯ�Ż������е�������������";
					}
					if(size.equalsIgnoreCase("pid_file"))
					{
					 size="����ID (PID)�ļ���·����";
					}
					if(size.equalsIgnoreCase("prepared_stmt_count"))
					{
					 size="prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("port"))
					{
					 size="������֡��TCP/IP�������ö˿�";
					}
					if(size.equalsIgnoreCase("preload_buffer_size"))
					{
					 size="��������ʱ����Ļ�������С";
					}
					if(size.equalsIgnoreCase("protocol_version"))
					{
					 size="MySQL������ʹ�õĿͻ���/������Э��İ汾";
					}
					if(size.equalsIgnoreCase("query_alloc_block_size"))
					{
					 size="Ϊ��ѯ������ִ�й����д����Ķ��������ڴ���С";
					}
					if(size.equalsIgnoreCase("query_cache_limit"))
					{
					 size="��Ҫ������ڸ�ֵ�Ľ��";
					}
					if(size.equalsIgnoreCase("query_cache_min_res_unit"))
					{
					 size="��ѯ����������С��Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("query_cache_size"))
					{
					 size="Ϊ�����ѯ���������ڴ������";
					}
					if(size.equalsIgnoreCase("query_cache_type"))
					{
					 size="���ò�ѯ��������";
					}
					if(size.equalsIgnoreCase("query_cache_wlock_invalidate"))
					{
					 size="�Ա����WRITE����������ֵ";
					}
					if(size.equalsIgnoreCase("query_prealloc_size"))
					{
					 size="���ڲ�ѯ������ִ�еĹ̶��������Ĵ�С";
					}
					if(size.equalsIgnoreCase("range_alloc_block_size"))
					{
					 size="��Χ�Ż�ʱ����Ŀ�Ĵ�С";
					}
					if(size.equalsIgnoreCase("read_buffer_size"))
					{
					 size="ÿ���߳�����ɨ��ʱΪɨ���ÿ�������Ļ������Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="�����Ը��ƴӷ���������ΪONʱ���������Ƿ��������";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="�����Ը��ƴӷ���������ΪONʱ���ӷ��������������";
					}
					if(size.equalsIgnoreCase("relay_log_purge"))
					{
					 size="��������Ҫ�м���־ʱ���û������Զ�����м���־";
					}
					if(size.equalsIgnoreCase("read_rnd_buffer_size"))
					{
					 size="�������������˳���ȡ��ʱ����ͨ���û�������ȡ�У���������Ӳ��";
					}
					if(size.equalsIgnoreCase("secure_auth"))
					{
					 size="�����--secure-authѡ��������MySQL���������Ƿ������оɸ�ʽ(4.1֮ǰ)����������˻������������";
					}
					if(size.equalsIgnoreCase("shared_memory"))
					{
					 size="(ֻ����Windows)�������Ƿ��������ڴ�����";
					}
					if(size.equalsIgnoreCase("shared_memory_base_name"))
					{
					 size="(ֻ����Windows)˵���������Ƿ��������ڴ����ӣ���Ϊ�����ڴ�����ʶ���";
					}
					if(size.equalsIgnoreCase("server_id"))
					{
					 size="���������Ʒ������ʹӸ��Ʒ�����";
					}
					if(size.equalsIgnoreCase("skip_external_locking"))
					{
					 size="mysqld�Ƿ�ʹ���ⲿ����";
					}
					if(size.equalsIgnoreCase("skip_networking"))
					{
					 size="���������ֻ������(��TCP/IP)����";
					}
					if(size.equalsIgnoreCase("skip_show_database"))
					{
					 size="��ֹ������SHOW DATABASESȨ�޵�����ʹ��SHOW DATABASES���";
					}
					if(size.equalsIgnoreCase("slave_compressed_protocol"))
					{
					 size="��������ӷ�������֧�֣�ȷ���Ƿ�ʹ�ô�/��ѹ��Э��";
					}
					if(size.equalsIgnoreCase("slave_load_tmpdir"))
					{
					 size="�ӷ�����Ϊ����LOAD DATA INFILE��䴴����ʱ�ļ���Ŀ¼��";
					}
					if(size.equalsIgnoreCase("slave_net_timeout"))
					{
					 size="����������ǰ�ȴ���/�����ӵĸ������ݵĵȴ�����";
					}
					if(size.equalsIgnoreCase("slave_skip_errors"))
					{
					 size="�ӷ�����Ӧ����(����)�ĸ��ƴ���";
					}
					if(size.equalsIgnoreCase("slave_transaction_retries"))
					{
					 size="���ƴӷ�����SQL�߳�δ��ִ����������ʾ����ֹͣǰ���Զ��ظ�slave_transaction_retries��";
					}
					if(size.equalsIgnoreCase("slow_launch_time"))
					{
					 size="��������̵߳�ʱ�䳬��������������������Slow_launch_threads״̬����";
					}
					if(size.equalsIgnoreCase("sort_buffer_size"))
					{
					 size="ÿ�������̷߳���Ļ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("sql_mode"))
					{
					 size="��ǰ�ķ�����SQLģʽ�����Զ�̬����";
					}
					if(size.equalsIgnoreCase("storage_engine"))
					{
					 size="�ñ�����table_typeis��ͬ��ʡ���MySQL 5.1��,��ѡstorage_engine";
					}
					if(size.equalsIgnoreCase("sync_binlog"))
					{
					 size="���Ϊ������ÿ��sync_binlog'thд��ö�������־��MySQL�����������Ķ�������־ͬ����Ӳ����";
					}
					if(size.equalsIgnoreCase("sync_frm"))
					{
					 size="����ñ�����Ϊ1,����������ʱ��ʱ����.frm�ļ��Ƿ�ͬ����Ӳ����";
					}
					if(size.equalsIgnoreCase("system_time_zone"))
					{
					 size="������ϵͳʱ��";
					}
					if(size.equalsIgnoreCase("table_cache"))
					{
					 size="�����̴߳򿪵ı����Ŀ";
					}
					if(size.equalsIgnoreCase("table_type"))
					{
					 size="Ĭ�ϱ�����(�洢����)";
					}
					if(size.equalsIgnoreCase("thread_cache_size"))
					{
					 size="������Ӧ��������߳��Ա�����ʹ��";
					}
					if(size.equalsIgnoreCase("thread_stack"))
					{
					 size="ÿ���̵߳Ķ�ջ��С";
					}
					if(size.equalsIgnoreCase("time_format"))
					{
					 size="�ñ���Ϊʹ��";
					}
					if(size.equalsIgnoreCase("time_zone"))
					{
					 size="��ǰ��ʱ��";
					}
					if(size.equalsIgnoreCase("tmp_table_size"))
					{
					 size="����ڴ��ڵ���ʱ������ֵ��MySQL�Զ�����ת��ΪӲ���ϵ�MyISAM��";
					}
					if(size.equalsIgnoreCase("tmpdir"))
					{
					 size="������ʱ�ļ�����ʱ���Ŀ¼";
					}
					if(size.equalsIgnoreCase("transaction_alloc_block_size"))
					{
					 size="Ϊ���潫���浽��������־�е�����Ĳ�ѯ��������ڴ��Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("transaction_prealloc_size"))
					{
					 size="transaction_alloc_blocks����Ĺ̶��������Ĵ�С���ֽڣ��������β�ѯ֮�䲻���ͷ�";
					}
					if(size.equalsIgnoreCase("tx_isolation"))
					{
					 size="Ĭ��������뼶��";
					}
					if(size.equalsIgnoreCase("updatable_views_with_limit"))
					{
					 size="�ñ�������������°���LIMIT�Ӿ䣬�Ƿ�����ڵ�ǰ����ʹ�ò��������ؼ��ֵ���ͼ���и���";
					}
					if(size.equalsIgnoreCase("version"))
					{
					 size="�������汾��";
					}
					if(size.equalsIgnoreCase("version_bdb"))
					{
					 size="BDB�洢����汾";
					}
					if(size.equalsIgnoreCase("version_comment"))
					{
					 size="configure�ű���һ��--with-commentѡ�������MySQLʱ���Խ���ע��";
					}
					if(size.equalsIgnoreCase("version_compile_machine"))
					{
					 size="MySQL�����Ļ�����ܹ�������";
					}
					if(size.equalsIgnoreCase("version_compile_os"))
					{
					 size="MySQL�����Ĳ���ϵͳ������";
					}
					if(size.equalsIgnoreCase("wait_timeout"))
					{
					 size="�������رշǽ�������֮ǰ�ȴ��������";
					}
			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td align=left
																					class="application-detail-data-body-list">
																					&nbsp;<%=size%></td>
																				<td class="application-detail-data-body-list"
																					align=left width=20%>
																					&nbsp;<%=free%></td>
																			</tr>
																			<%
          }
       }
          %>

																		</table>
																	</td>
																</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"cellpadding="0">
																		<tr height="28" bgcolor="#ECECEC">
																			<td align=center> �� �� �� �� </td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td>������ͨ���¼����Σ�</td>
																			<td>��ռ䳬����ֵ�¼����Σ�
																			</td>
																		</tr>
																		<tr>
																		<td><%=downnum %></td><td><%=count %></td>
																		</tr>
																	</table>
			             										</td>
															</tr>
															
															<tr>
																	<td>
																		<table width="100%" border="1" cellspacing="0"cellpadding="0">
																			<tr height="28" bgcolor="#ECECEC">
																				<td align=center >�¼��б�</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr bgcolor="DEEBF7">
																	<td>
																		<table width="100%" border="0" cellpadding="3"
																			cellspacing="1" bgcolor="#FFFFFF">

																			<tr height="28" bgcolor="#ECECEC">
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
																			</tr>  
																			<%
																				int index = 0;
																			  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
																			  	List list = (ArrayList)request.getAttribute("list");
																			  	if(list == null){
																			  	System.out.println("list---null");
																			  	}
																			  	if(list != null && list.size()>0){
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
																				  	String act="������";
																				  	if("1".equals(level)){
																				  		showlevel="��ͨ�¼�";
																				  	}else if("2".equals(level)){
																				  		showlevel="�����¼�";
																				  	}else{
																					    showlevel="�����澯";
																					}
																				   	  	if("0".equals(status)){
																				  		status = "δ����";
																				  	}
																				  	if("1".equals(status)){
																				  		status = "������";  	
																				  	}
																				  	if("2".equals(status)){
																				  	  	status = "�������";
																				  	}
																				  	String rptman = eventlist.getReportman();
																				  	String rtime1 = _sdf.format(cc);
																			%>

																			<tr bgcolor="#FFFFFF" >

																				<td class="application-detail-data-body-list">
																					&nbsp;<%=index%></td>
																				<%
																			    	if("3".equals(level)){
																			    %>
																				<td
																					style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																					bgcolor=red align=center><%=showlevel%>&nbsp;
																				</td>
																				<%
																			       }else if("2".equals(level)){
																			       %>
																				<td
																					style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																					bgcolor=orange align=center><%=showlevel%>&nbsp;
																				</td>
																				<%
																			       }else{
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
																			<%}
																			}//HONGLI ADD
 																			%>
																		</table>
																	</td>
																</tr>
															<tr>
							                					<td class="win-data-title" style="height:29px;"></td>
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
            	<div align=center>
            		<input type=button value="�رմ���" onclick="window.close()">
            	</div>  
				<br>
		</form>  
	</body>
</html>