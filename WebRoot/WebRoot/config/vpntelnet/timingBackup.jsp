<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="com.afunms.config.model.TimingBackupTelnetConfig"%>
<%@page import="com.afunms.config.manage.HaweitelnetconfManager"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*" %>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = (ArrayList<TimingBackupTelnetConfig>)request.getAttribute("timingBackupTelnetConfigList");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">

		<!--nielin add for timeShareConfig at 2010-01-04 start-->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>
		<!--nielin add for timeShareConfig at 2010-01-04 end-->

		<script language="JavaScript" type="text/javascript">

 Ext.onReady(function()
{
 Ext.get("process").on("click",function(){
  
    var chk1 = checkinput("ipaddress","string","�����豸",50,false);
    if(chk1)
    {
    	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
    	mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=timingBackup";
    	mainForm.submit();
    }
 });
});

//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------
function showup(){
	var url="<%=rootPath%>/vpntelnetconf.do?action=multi_netip";
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
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
	timeShareConfiginit();
	 // nielin add for time-sharing at 2010-01-04
}
function timeType(obj){
	var type = obj.value;
	document.getElementById('td_sendtimehou').style.display='none';
	document.getElementById('td_sendtimeday').style.display='none';
	document.getElementById('td_sendtimeweek').style.display='none';
	document.getElementById('td_sendtimemonth').style.display='none';
	if(type==1){
		document.getElementById('td_sendtimehou').style.display='';
	}else if(type==2){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeweek').style.display='';
	}else if(type==3){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
	}else if(type==4){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
	}else if(type==5){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
	}
}
</script>
		<script type="text/javascript">
		//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"100");
			    }
			    else
			    {
			        popMenu(itemMenu,100,"11111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			
			function showTelnetNetList(){
				var url="<%=rootPath%>/vpntelnetconf.do?action=multi_telnet_netip";
				window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
			}
			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=addTimingBackupTelnetConfig";
			    mainForm.submit();
			}
			
			function toDelete(){  
     				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=deleteTimingBackupTelnetConfig";
     				mainForm.submit();
	  		}
	  		
	  		function edit()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=ready_editTimingBackupTelnetConfig&id="+node;
				mainForm.submit();
			}
	  		function addBackup()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=addBackup&id="+node;
				mainForm.submit();
			}
	  		function disBackup()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=disBackup&id="+node;
				mainForm.submit();
			}
		</script>

	</head>
	<body id="body" class="body" onload="initmenu();">
<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">�༭</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addBackup()">����</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.disBackup()">ȡ������</td>
			</tr>
		</table>
	</div>

		<form name="mainForm" method="post">

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
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">&nbsp;�Զ���  >> �����ļ�����  >> ��ʱ�����б�</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
														<td>
															<table width="100%" cellpadding="0" cellspacing="1">
																<tr>
																	<td bgcolor="#ECECEC" width="60%">
																	&nbsp;&nbsp;&nbsp;
																		<!--  ip��ַ��<input type="text" size="30" name="ipfindaddress">
																		<input type="button" value="��ѯ" onclick="toFindIp()">-->
																	</td>
																	<td bgcolor="#ECECEC" width="40%" align='right'>
																		<a href="#" onclick="toAdd()">���</a>&nbsp;
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
				        													<tr>
				        														<td colspan="2">
						        													<table cellspacing="0" border="1" bordercolor="#ababab">
								        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
								        													<td align="center"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()" class=noborder></td>
								        													<td align="center">���</td>
								        													<td align="center" width="20%">�����豸IP</td>
								        													<td align="center">������������(yyyyMMddhh)</td>
								        													<td align="center">��ʱ��������</td>
								        													<td align="center">�Ƿ�ʱ</td>
								        													<td align="center" width="20%">����</td>
								        												</tr>
								        												<%
								        												
																							if(timingBackupTelnetConfigList != null){
																								String[] frequencyName = { "ÿ��", "ÿ��", "ÿ��", "ÿ��", "ÿ��" };
																								String[] monthCh = { " 1��", " 2��", " 3��", " 4��", " 5��", " 6��", " 7��", " 8��",
																										" 9��", " 10��", " 11��", " 12��" };
																								String[] weekCh = { " ������", " ����һ", " ���ڶ�", " ������", " ������", " ������", " ������" };
																								String[] dayCh = null;
																								String[] hourCh =null;
																								for(int i=0; i<timingBackupTelnetConfigList.size(); i++){
																									TimingBackupTelnetConfig timingBackupTelnetConfig = (TimingBackupTelnetConfig)timingBackupTelnetConfigList.get(i);
																									StringBuffer sb = new StringBuffer();
																									int frequency = timingBackupTelnetConfig.getBackup_sendfrequency();
																									String month = HaweitelnetconfManager.splitDate(timingBackupTelnetConfig.getBackup_time_month(), monthCh, "month");
																									String week = HaweitelnetconfManager.splitDate(timingBackupTelnetConfig.getBackup_time_week(), weekCh, "week");
																									
																									String day = HaweitelnetconfManager.splitDate(timingBackupTelnetConfig.getBackup_time_day(), dayCh, "day");
																									String hour = HaweitelnetconfManager.splitDate(timingBackupTelnetConfig.getBackup_time_hou(), hourCh, "hour");
																									sb.append(frequencyName[frequency - 1] + " ");
																									//if (month != null && !month.equals("")){
																										//sb.append(" �·ݣ�(" + month + ")");
																									//}
																									//if (week != null && !week.equals("")){
																										//sb.append(" ����:(" + week + ")");
																									//}
																									//if (day != null && !day.equals("")){
																										//sb.append(" ���ڣ�(" + day + ")");
																									//}
																									//if (hour != null && !hour.equals("")){
																										//sb.append(" ʱ�䣺(" + hour + ")");
																									//}
																									if(frequency == 1){//ÿ��
																										sb.append(" ʱ�䣺(" + hour + ")");
																									}
																									if(frequency == 2){//ÿ��
																										sb.append(" ����:(" + week + ")");
																										sb.append(" ʱ�䣺(" + hour + ")");
																									}
																									if(frequency == 3){//ÿ��
																										sb.append(" ���ڣ�(" + day + ")");
																										sb.append(" ʱ�䣺(" + hour + ")");
																									}
																									if(frequency == 4){//ÿ��
																										sb.append(" �·ݣ�(" + month + ")");
																										sb.append(" ���ڣ�(" + day + ")");
																										sb.append(" ʱ�䣺(" + hour + ")");
																									}
																									if(frequency == 5){//ÿ��
																										sb.append(" �·ݣ�(" + month + ")");
																										sb.append(" ���ڣ�(" + day + ")");
																										sb.append(" ʱ�䣺(" + hour + ")");
																									}
																									String status = timingBackupTelnetConfig.getStatus();
																						 %>
									        													<tr <%=onmouseoverstyle%>>
																	        						<td align='center'>
																									<INPUT type="checkbox" class=noborder name="checkbox"
																											value="<%=timingBackupTelnetConfig.getId() %>">
																									</td>
																									<td align='center'>
																										<font color='blue'><%=i+1 %></font>
									        														</td>
									        														<td align='center'><%=timingBackupTelnetConfig.getTelnetconfigips() %></td>
									        														<td align='center'><%=timingBackupTelnetConfig.getBackup_date() %></td>
									        														<td align='center'><%=sb.toString()+"" %></td>
									        														<td align='center'><%="1".equals(status)?"����":"δ����" %></td>
									        														<td align='center'>
																										<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=timingBackupTelnetConfig.getId() %>') alt="�Ҽ�����">
																									</td>
									        													</tr>
									        													<%
									        												}}
								        												%>
								        											</table>
							        											</td>
				        													</tr>
																		</TABLE>
				        										</td>
				        									</tr>
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
								</table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
			</table>
		</form>
	</body>
</HTML>