<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ page import="com.afunms.application.model.DBVo"%> 
<%@ page import="com.afunms.application.model.DBTypeVo"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>

<html>
<head>
<%	
  	String rootPath = request.getContextPath();
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	String ip = (String)session.getAttribute("_ip");
  	Integer pingvalue = (Integer)session.getAttribute("_pingvalue");
  	String dbname = (String)session.getAttribute("_dbname");
  	//HONGLI ADD
  	String id = (String) request.getAttribute("id");
    DBVo vo = (DBVo)request.getAttribute("vo");
    DBTypeVo typevo = (DBTypeVo)request.getAttribute("typevo");
    String downnum= (String)request.getAttribute("downnum");//崻���
    Integer count = (Integer)request.getAttribute("count");//��ø澯����
    
    int level1 = Integer.parseInt(request.getAttribute("level1")+"");
	int _status = Integer.parseInt(request.getAttribute("status")+"");
	
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
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<!-- HONGLI infomix -->
<title>�¼�����</title>
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
function ping_word_report()
{
	//mainForm.action = "<%=rootPath%>/dbreport.do?action=createOraEventDoc&ipaddress=<%=ip%>&typevo=<%=typevo%>&dbname=<%=dbname%>&pingvalue=<%=pingvalue%>";
	//HONGLI MODIFFY
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLEventReport&str=0&ipaddress=<%=ip%>&typevo=<%=typevo%>&dbname=<%=dbname%>&pingvalue=<%=pingvalue%>&startdate=<%=startdate%>&todate=<%=todate%>";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLEventReport&str=2&startdate=<%=startdate%>&todate=<%=todate%>&ipaddress=<%=ip%>&typevo=<%=typevo%>&dbname=<%=dbname%>&pingvalue=<%=pingvalue%>";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLEventReport&str=1&ipaddress=<%=ip%>&typevo=<%=typevo%>&dbname=<%=dbname%>&pingvalue=<%=pingvalue%>&startdate=<%=startdate%>&todate=<%=todate%>";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/mysql.do?action=mysqlManagerEventReportQuery&ipaddress=<%=ip%>&startdate="+starttime+"&todate="+endtime+"&id="+<%=id%>;
	mainForm.submit();
}
function ping_cancel()
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
								                	<td class="win-content-title" style="align:center">&nbsp;�¼�����</td>
								                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
												</tr>
										    
						       				</table>
					       				</td>
					       			</tr>
							       	<tr>
							       		<td>
							       			<table id="win-content-body" class="win-content-body">
												<!-- start -->
							       				<tr>
							                		<table class="application-detail-data-body">
																<tr bgcolor="#ECECEC" height="28">
																	<td>
																		<table>
																			<tr>
																				<td>
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
												       			<tr>
												       				<td>
																		<table border="1" width="90%" >
																			<tr>
																				<td>IP��ַ</td>
																				<td>���ݿ�����</td>
																				<td>���ݿ�����</td>
																				<td>������ͨ���¼����Σ�</td>
																				<td>��ռ䳬����ֵ�¼����Σ� </td>
																			</tr>
																			<tr>
																				<td><%=vo.getIpAddress() %></td>
																				<td><%=typevo.getDbtype() %></td>
																				<td><%=vo.getAlias() %></td>
																				<td><%=downnum %></td>
																				<td><%=count %></td>
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

																			<tr bgcolor="#FFFFFF" >

																				<td class="detail-data-body-list"><%=i+1%></td>
   	  																			<td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
																				<td class="application-detail-data-body-list">
																					<%=content%></td>
																				<td class="application-detail-data-body-list">
																					<%=rtime1%></td>
																				<td class="application-detail-data-body-list">
																					<%=rptman%></td>
																				<td class="application-detail-data-body-list" bgcolor=<%=bgcolorstr%>>
																					<%=status%></td>
																			</tr>
																			<%}
																			}//HONGLI ADD
 																			%>
																		</table>
																	</td>
																</tr>
															</table>
							       				</tr> 
							       				<!-- end -->
												
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