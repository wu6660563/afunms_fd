<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import="java.util.Hashtable" %>
<%@ page import=" java.util.Vector"%> 
<%@ page import=" java.util.List"%> 
<%@ page import=" java.util.ArrayList"%> 
<%@ page import=" java.util.Date"%> 
<%@ page import="com.afunms.event.model.EventList"%>
<html>
<head>
<%	
	String ip_id = (String)request.getAttribute("ipaddresid");
	String conMemoy = (String)request.getAttribute("conMemory");//�����ڴ�
	String totalMemory = (String)request.getAttribute("totalMemory");//total
	String lockMem = (String)request.getAttribute("lockMem");//  �����ڴ�
	String optMemory = (String)request.getAttribute("optMemory");// �ڴ��Ż�
	String sqlMem = (String)request.getAttribute("sqlMem");// sql����
	Integer count = (Integer)request.getAttribute("count");//��ø澯����
	String maxping =(String)request.getAttribute("ConnectUtilizationmax");//
	String downnum= (String)request.getAttribute("downnum");//崻���
	String IsSingleUser = (String)request.getAttribute("IsSingleUser");//���û�ģʽ
	String SecurityOnly = (String)request.getAttribute("SecurityOnly");//���ɰ�ȫ��ģʽ
	String buding = (String)request.getAttribute("buding");//����
	String IsClustered = (String)request.getAttribute("IsClustered");//�ڹ���ת��Ⱥ�������÷�����ʵ��
	String grade =  (String)request.getAttribute("grade");
	String runstar = "";
	runstar = (String)request.getAttribute("runstr");
//	if(runstar==null){
///		runstar = "����ֹͣ";
//	}
	String avgpingcon =(String) request.getAttribute("avgpingcon");
  	String rootPath = request.getContextPath();
  	String newip = (String)request.getAttribute("newip");
  	String ip = (String)request.getAttribute("ipaddress");
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	Hashtable dbValue = new Hashtable();
  	Hashtable alldatabase = new Hashtable();
  	Vector names = new Vector();
  	//HONGLI ADD START1
  	String id = (String)request.getAttribute("id");
    dbValue	= (Hashtable)request.getAttribute("reportHash");
    String pingmin = (String)request.getAttribute("pingmin");//HONGLI ADD ��С��ͨ��
	String pingnow = (String)request.getAttribute("pingnow");//HONGLI ADD ��ǰ��ͨ��
	Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");//HONGLI ADD
	Hashtable dbio = (Hashtable)request.getAttribute("dbio");//HONGLI ADD
	Hashtable memValue = (Hashtable)request.getAttribute("memValue");//HONGLI ADD ���ݿ���ڴ�������Ϣ
	String dbtye = (String)request.getAttribute("dbtye");//���ݿ�����
	System.out.println("######HONGLI memValue00000--"+memValue);
	String[] sysItem1={"shared_pool","large_pool","DEFAULT_buffer_cache","java_pool"};
	String[] sysItemch1={"�����","���ͳ�","���������ٻ���","Java��"};
	String[] sysItem2={"aggregate_PGA_target_parameter","total_PGA_allocated","maximum_PGA_allocated"};
	String[] sysItemch2={"�ܼ�PGAĿ��","����ĵ�ǰPGA","��������PGA"};
	DBVo vo = (DBVo)request.getAttribute("db");
	Hashtable memPerfValue = (Hashtable)request.getAttribute("memPerfValue"); 
	Hashtable cursors = (Hashtable)request.getAttribute("cursors");
	if(cursors == null){
		cursors = new Hashtable();
	}
	String buffercache = "0";//������������ 
	String dictionarycache = "0";//�����ֵ������� 
	String librarycache = "0";//�⻺�������� 
	String pctmemorysorts = "0";//�ڴ��е����� 
	String pctbufgets = "0";//���˷��ڴ��ǰ10�����ռȫ���ڴ��ȡ���ı���
	String opencurstr = "0";//�򿪵��α��� 
	
	if(memPerfValue == null)memPerfValue = new Hashtable();
	if(memPerfValue!=null){
		if(memPerfValue.containsKey("buffercache") &&  memPerfValue.get("buffercache") != null){
			buffercache = (String)memPerfValue.get("buffercache");
		}
		if(memPerfValue.containsKey("dictionarycache") &&  memPerfValue.get("dictionarycache") != null){
			dictionarycache = (String)memPerfValue.get("dictionarycache");
		}
		if(memPerfValue.containsKey("librarycache") &&  memPerfValue.get("librarycache") != null){
			librarycache = (String)memPerfValue.get("librarycache");	
		}		
		if(memPerfValue.containsKey("pctmemorysorts") &&  memPerfValue.get("pctmemorysorts") != null){
			pctmemorysorts = (String)memPerfValue.get("pctmemorysorts");
		}
		if(memPerfValue.containsKey("pctbufgets") &&  memPerfValue.get("pctbufgets") != null){
			pctbufgets = (String)memPerfValue.get("pctbufgets");
		}
		if(cursors.containsKey("opencur") &&  cursors.get("opencur") != null){
			opencurstr = (String)cursors.get("opencur");
		}
	}
	
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
	//HONGLI ADD END1
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<!--  oracle �ۺϱ���  HONGLI 2010-10-28 -->
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
	//mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=0";
	mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=6";//HONGLI MODIFY
	mainForm.submit();
}
function ping_word_report(){
	//mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=3";
	mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=5";//HONGLI MODIFY
	mainForm.submit();
}
function ping_pdf_report(){
	//mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=4";
	mainForm.action = "<%=rootPath%>/dbreport.do?action=downloadoraselfreport&ipaddress=<%=ip_id%>&str=7";//HONGLI MODIFY
	mainForm.submit();
}
function ping_ok(){
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/hostreport.do?action=showDbOracleCldReportByDate&ipaddress=<%=vo.getIpAddress()%>&id=<%=id%>&startdate="+starttime+"&todate="+endtime;//HONGLI MODIFY
	mainForm.submit();
}
function ping_cancel(){
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
function openwin(str,operate,ip) {	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
 Ext.onReady(function(){  
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
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
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
	<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
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
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
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
															<tr bgcolor="#ECECEC">
																<td>���ݿ�����</td><td>IP</td><td>����</td><td>��ǰ״̬</td><td>��������</td>
															</tr>
															<tr>
																<td><%=vo.getDbName() %></td><td><%=vo.getIpAddress() %></td><td><%=dbtye %></td><td><%=runstar %></td><td><%=grade %></td>
															</tr>
														</table>
													</td>
												</tr> 
												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr> 
							       				<tr>
							       					<td>
							       					<table border="1" width="90%">
															<tr bgcolor="#ECECEC"> 
																<td>��ͨ��</td><td>��ǰ��ͨ��</td><td>��С��ͨ��</td><td>ƽ����ͨ��</td>
															</tr>
															<tr>
															<td>&nbsp;</td>
															<td><%=pingnow%>%</td>
															<td><%=pingmin%>%</td>
															<td><%=avgpingcon %>%</td>
															</tr>
															<tr>
																<td colspan="5" align="center">
																	<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip%>ConnectUtilization.png"/>
																</td>
															</tr>
														</table>
							       					
							       					</td>
							       				</tr>
												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr> 
							       				<tr>
							       					<td>
							       						<table class="application-detail-data-body" width="989" height="149">
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"cellpadding="0">
																		<tr height="28" bgcolor="#ECECEC">
																			<td align=center >���ݿ���Ϣ</td>
																		</tr>
																	</table>
																</td>
															</tr>
							       							<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td>������������</td><td>�����ֵ�������</td><td>�⻺��������</td><td>�ڴ��е�����</td><td>���˷��ڴ��ǰ10�����ռȫ���ڴ��ȡ���ı���</td><td>�򿪵��α��� </td>
																		</tr>
																		<tr>
																			<td><%=buffercache %>%</td>
																			<td><%=dictionarycache %>%</td>
																			<td><%=librarycache %>%</td>
																			<td><%=pctmemorysorts %>%</td>
																			<td><%=pctbufgets %>%</td>
																			<td><%=opencurstr %></td>
																		</tr>
																		
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
																			<td align=center>�ڴ����ã�PGA��Ϣ��</td>
																			<td align=center>�ڴ����ã�FGA��Ϣ��</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td><%=sysItemch2[0]%></td><td><%=sysItemch2[1]%></td><td><%=sysItemch2[2]%></td><td><%=sysItemch1[0]%></td><td><%=sysItemch1[1]%></td><td><%=sysItemch1[2]%></td><td><%=sysItemch1[3]%></td>
																		</tr>
																		<%
																			if(memValue != null && memValue.size() != 0){
																		 %>
																		<tr>
																			<td><%=memValue.get(sysItem2[0]) %>MB</td>
																			<td><%=memValue.get(sysItem2[1]) %>MB</td>
																			<td><%=memValue.get(sysItem2[2]) %>MB</td>
																			<td><%=memValue.get(sysItem1[0]) %>MB</td>
																			<td><%=memValue.get(sysItem1[1]) %>MB</td>
																			<td><%=memValue.get(sysItem1[2]) %>MB</td>
																			<td><%=memValue.get(sysItem1[3]) %>MB</td>
																		</tr>
																		<%} %>
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
																			<td align=center>��ռ���Ϣ
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table class="application-detail-data-body">
																		<tr>
																			<td>
																				<table width="100%" border="0" cellspacing="0"
																					cellpadding="0">

																					<tr height="28" bgcolor="#ECECEC">
																						<td align=center>
																							&nbsp;
																						</td>
																						<td align=center>
																							�ļ���
																						</td>
																						<td align=center>
																							��ռ�
																						</td>
																						<td align=center>
																							�ռ��С��MB��
																						</td>
																						<td align=center>
																							���д�С��MB��
																						</td>
																						<td align=center>
																							���б���
																						</td>
																						<td align=center>
																							�����
																						</td>
																						<td align=center>
																							������
																						</td>
																						<td align=center>
																							����д
																						</td>
																						<td align=center>
																							�����д
																						</td>
																						<td align=center>
																							�ļ�״̬
																						</td>

																					</tr>
																					<% 
																					    if(tableinfo_v != null && tableinfo_v.size() != 0){
																							for(int i=0;i<tableinfo_v.size();i++){ 
																								Hashtable ht = (Hashtable)tableinfo_v.get(i);    
																								String filename = ht.get("file_name").toString();
																								String tablespace = ht.get("tablespace").toString();
																								String size = ht.get("size_mb").toString();
																								String free = ht.get("free_mb").toString();
																								String percent = ht.get("percent_free").toString();
																								String status = ht.get("status").toString();
																								String pyr = "";
																								String pbr = "";
																								String pyw = "";
																								String pbw = "";
																								if(dbio.containsKey(filename)){
																									Hashtable iodetail = (Hashtable)dbio.get(filename);
																									if(iodetail != null && iodetail.size()>0){
																										pyr = (String)iodetail.get("pyr");
																										pbr = (String)iodetail.get("pbr");
																										pyw = (String)iodetail.get("pyw");
																										pbw = (String)iodetail.get("pbw");
																									}
																								}
																													
																					%>
																					<tr height="28" bgcolor="#FFFFFF">
																						<td class="application-detail-data-body-list"
																							align=center><%=i+1%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=filename%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=tablespace%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=size%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=free%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=percent%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=pyr%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=pbr%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=pyw%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=pbw%></td>
																						<td class="application-detail-data-body-list"
																							align=center>
																							&nbsp;<%=status%></td>
																					</tr>
																					<%}}%>
																				</table>
																			</td>
																		</tr>
																	</table>
			             										</td>
															</tr> 
															<tr>
							                					<td class="win-data-title" style="height:29px;"></td>
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
							                					<td class="win-data-title" style="height: 29px;" ></td>
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