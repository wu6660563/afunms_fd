<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String rootPath = request.getContextPath();
	BusinessDao bussdao = new BusinessDao();
	DBTypeDao typedao = new DBTypeDao();
	String flag = (String) request.getAttribute("flag");
	String ids = (String) request.getAttribute("ids");
	String type = (String) request.getAttribute("type");
%>
<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

		<!-- snow add for gatherTime at 2010-5-19 start -->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/addTimeConfig.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js"
			charset="gb2312"></script>
		<script>
	$(document).ready(function(){
		$('#saveBtn').bind('click',function(){
			if($('#recievers_name').val()==null || $('#recievers_name').val()=='')
				return;
			if($('#bidtext').val()==null || $('#bidtext').val()=='')
				return;
			if($('#devices_name').val()==null || $('#devices_name').val()=='')
				return;
			if($('#timeConfigTable tr').length<2)
				return;
			mainForm.action = "<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savePerforIndex";
        	mainForm.submit();
        	  
        var model = parent.opener.document.getElementById("newModel");
         model.innerHTML = "&nbsp;&nbsp;&nbsp; ģ������:"+($('#report_name').val())+"&nbsp;<input type='button' value='�༭'/>&nbsp;<input type='button' value='ɾ��'/>";
         window.close();
		});
	});
</script>
		<!-- snow add for gatherTime at 2010-5-19 end -->

		<!--nielin add for timeShareConfig at 2010-01-04 start-->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>
		<!--nielin add for timeShareConfig at 2010-01-04 end-->

		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";

  function toAdd()
  {
		mainForm.action = "<%=rootPath%>/subscribeReportConfig.do?action=add";
        mainForm.submit();
  }
  
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"_blank","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
//-- nielin modify at 2010-01-04 end ----------------


//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------
function CreateDeviceWindow(url)
{
	
msgWindow=window.open(url,"_blank","toolbar=no,width=900,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
function setReciever(ctrlId,hideCtrlId)
{
	return CreateWindow("<%=rootPath%>/subscribeReportConfig.do?action=user_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function setDevices(ctrlId,hideCtrlId)
{
	return CreateDeviceWindow("<%=rootPath%>/subscribeReportConfig.do?action=device_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
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
}
function timeType(obj){
	var type = obj.value;
	document.getElementById('td_sendtimehou').style.display='none';
	document.getElementById('td_sendtimeday').style.display='none';
	document.getElementById('td_sendtimeweek').style.display='none';
	document.getElementById('td_sendtimemonth').style.display='none';
	document.getElementById('td_sendtimehou').disabled="disabled";
	document.getElementById('td_sendtimeday').disabled="disabled";
	document.getElementById('td_sendtimeweek').disabled="disabled";
	document.getElementById('td_sendtimemonth').disabled="disabled";
	if(type==1){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimehou').disabled="";
	}else if(type==2){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeweek').style.display='';
		document.getElementById('td_sendtimehou').disabled="";
		document.getElementById('td_sendtimeweek').disabled="";
	}else if(type==3){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimehou').disabled="";
		document.getElementById('td_sendtimeday').disabled="";
	}else if(type==4){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('td_sendtimehou').disabled="";
		document.getElementById('td_sendtimeday').disabled="";
		document.getElementById('td_sendtimemonth').disabled="";
	}else if(type==5){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('td_sendtimehou').disabled="";
		document.getElementById('td_sendtimeday').disabled="";
		document.getElementById('td_sendtimemonth').disabled="";
	}
}
</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="category" name="category" value="">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">

			<table id="body-container" class="body-container">
				<tr>

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
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="add-content-title">
																		���� >> ������ >> �������� >> ����һ��
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
															<table id="detail-content-body"
																class="detail-content-body">
																<tr>
																	<td>
																		<table border="0" id="table1" cellpadding="0"
																			width="100%">
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					�ռ���:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="recievers_name"
																						title="��������ַ�ã��ָ�" id="recievers_name" size="50"
																						class="formStyle" size="50">
																					<input type="button" value="�����ռ���"
																						onclick="setReciever('recievers_name','recievers_id');" />
																					<input type="hidden" id="recievers_id"
																						name="recievers_id" value="">
																					<input type="hidden" id="ids" name="ids"
																						value="<%=ids%>">
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					�ʼ�����:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="bidtext" id="bidtext"
																						size="50" class="formStyle">
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					�ʼ�����:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="devices_name"
																						id="devices_name" size="50" class="formStyle">
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					��������:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<SELECT id="reporttype" name="reporttype">
																						<OPTION value=1 selected>
																							�ձ�
																						</OPTION>
																						<OPTION value=2>
																							�ܱ�
																						</OPTION>
																						<OPTION value=3>
																							�±�
																						</OPTION>
																						<OPTION value=4>
																							����
																						</OPTION>
																						<OPTION value=5>
																							�걨
																						</OPTION>
																					</SELECT>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24">
																					��������ʱ��:&nbsp;
																				</TD>
																				<td nowrap colspan="3">
																					<div id="formDiv" style="">
																						<table width="100%"
																							style="BORDER-COLLAPSE: collapse"
																							borderColor=#cedefa cellPadding=0 rules=none
																							border=1 align="center">
																							<tr>
																								<td align="left">
																									<br>
																									<table id="timeConfigTable"
																										style="width: 60%; padding: 0; background-color: #FFFFFF; position: relative; left: 15px;">
																										<TBODY>

																											<tr>
																												<TD style="WIDTH: 100px">
																													<span>����ʱ��:</span>
																												</TD>
																											</tr>
																											<tr>
																												<TD style="WIDTH: 100px">
																													&nbsp;
																												</TD>
																											</tr>
																											<TR>
																												<TD>
																													<SELECT style="WIDTH: 250px"
																														id=transmitfrequency
																														onchange="javascript:timeType(this)"
																														name=transmitfrequency>
																														<OPTION value=1 selected>
																															ÿ��
																														</OPTION>
																														<OPTION value=2>
																															ÿ��
																														</OPTION>
																														<OPTION value=3>
																															ÿ��
																														</OPTION>
																														<OPTION value=4>
																															ÿ��
																														</OPTION>
																														<OPTION value=5>
																															ÿ��
																														</OPTION>
																													</SELECT>
																												</TD>
																											</TR>
																											<tr>
																												<TD style="WIDTH: 100px">
																													&nbsp;
																												</TD>
																											</tr>
																											<TR>
																												<TD style="display: none;"
																													id=td_sendtimemonth>
																													<SELECT disabled="disabled"
																														style="WIDTH: 250px" id=sendtimemonth
																														multiple size=5 name=sendtimemonth>
																														<OPTION selected value=01>
																															01��
																														</OPTION>
																														<OPTION value=02>
																															02��
																														</OPTION>
																														<OPTION value=03>
																															03��
																														</OPTION>
																														<OPTION value=04>
																															04��
																														</OPTION>
																														<OPTION value=05>
																															05��
																														</OPTION>
																														<OPTION value=06>
																															06��
																														</OPTION>
																														<OPTION value=07>
																															07��
																														</OPTION>
																														<OPTION value=08>
																															08��
																														</OPTION>
																														<OPTION value=09>
																															09��
																														</OPTION>
																														<OPTION value=10>
																															10��
																														</OPTION>
																														<OPTION value=11>
																															11��
																														</OPTION>
																														<OPTION value=12>
																															12��
																														</OPTION>
																													</SELECT>
																												</TD>
																												<TD style="display: none;"
																													id=td_sendtimeweek>
																													<SELECT disabled="disabled"
																														style="WIDTH: 250px" id=sendtimeweek
																														multiple size=5 name=sendtimeweek>
																														<OPTION selected value=0>
																															������
																														</OPTION>
																														<OPTION value=1>
																															����һ
																														</OPTION>
																														<OPTION value=2>
																															���ڶ�
																														</OPTION>
																														<OPTION value=3>
																															������
																														</OPTION>
																														<OPTION value=4>
																															������
																														</OPTION>
																														<OPTION value=5>
																															������
																														</OPTION>
																														<OPTION value=6>
																															������
																														</OPTION>
																													</SELECT>
																												</TD>
																												<TD style="display: none;" id=td_sendtimeday>
																													<SELECT disabled="disabled"
																														style="WIDTH: 250px" id=sendtimeday
																														multiple size=5 name=sendtimeday>
																														<OPTION selected value=01>
																															01��
																														</OPTION>
																														<OPTION value=02>
																															02��
																														</OPTION>
																														<OPTION value=03>
																															03��
																														</OPTION>
																														<OPTION value=04>
																															04��
																														</OPTION>
																														<OPTION value=05>
																															05��
																														</OPTION>
																														<OPTION value=06>
																															06��
																														</OPTION>
																														<OPTION value=07>
																															07��
																														</OPTION>
																														<OPTION value=08>
																															08��
																														</OPTION>
																														<OPTION value=09>
																															09��
																														</OPTION>
																														<OPTION value=10>
																															10��
																														</OPTION>
																														<OPTION value=11>
																															11��
																														</OPTION>
																														<OPTION value=12>
																															12��
																														</OPTION>
																														<OPTION value=13>
																															13��
																														</OPTION>
																														<OPTION value=14>
																															14��
																														</OPTION>
																														<OPTION value=15>
																															15��
																														</OPTION>
																														<OPTION value=16>
																															16��
																														</OPTION>
																														<OPTION value=17>
																															17��
																														</OPTION>
																														<OPTION value=18>
																															18��
																														</OPTION>
																														<OPTION value=19>
																															19��
																														</OPTION>
																														<OPTION value=20>
																															20��
																														</OPTION>
																														<OPTION value=21>
																															21��
																														</OPTION>
																														<OPTION value=22>
																															22��
																														</OPTION>
																														<OPTION value=23>
																															23��
																														</OPTION>
																														<OPTION value=24>
																															24��
																														</OPTION>
																														<OPTION value=25>
																															25��
																														</OPTION>
																														<OPTION value=26>
																															26��
																														</OPTION>
																														<OPTION value=27>
																															27��
																														</OPTION>
																														<OPTION value=28>
																															28��
																														</OPTION>
																														<OPTION value=29>
																															29��
																														</OPTION>
																														<OPTION value=30>
																															30��
																														</OPTION>
																														<OPTION value=31>
																															31��
																														</OPTION>
																													</SELECT>
																												</TD>
																												<TD style="" id=td_sendtimehou>
																													<SELECT style="WIDTH: 250px" id=sendtimehou
																														multiple size=5 name=sendtimehou>
																														<OPTION value=00>
																															00AM
																														</OPTION>
																														<OPTION value=01>
																															01AM
																														</OPTION>
																														<OPTION value=02>
																															02AM
																														</OPTION>
																														<OPTION selected value=03>
																															03AM
																														</OPTION>
																														<OPTION value=04>
																															04AM
																														</OPTION>
																														<OPTION value=05>
																															05AM
																														</OPTION>
																														<OPTION value=06>
																															06AM
																														</OPTION>
																														<OPTION value=07>
																															07AM
																														</OPTION>
																														<OPTION value=08>
																															08AM
																														</OPTION>
																														<OPTION value=09>
																															09AM
																														</OPTION>
																														<OPTION value=10>
																															10AM
																														</OPTION>
																														<OPTION value=11>
																															11AM
																														</OPTION>
																														<OPTION value=12>
																															12AM
																														</OPTION>
																														<OPTION value=13>
																															01PM
																														</OPTION>
																														<OPTION value=14>
																															02PM
																														</OPTION>
																														<OPTION value=15>
																															03PM
																														</OPTION>
																														<OPTION value=16>
																															04PM
																														</OPTION>
																														<OPTION value=17>
																															05PM
																														</OPTION>
																														<OPTION value=18>
																															06PM
																														</OPTION>
																														<OPTION value=19>
																															07PM
																														</OPTION>
																														<OPTION value=20>
																															08PM
																														</OPTION>
																														<OPTION value=21>
																															09PM
																														</OPTION>
																														<OPTION value=22>
																															10PM
																														</OPTION>
																														<OPTION value=23>
																															11PM
																														</OPTION>
																													</SELECT>
																												</TD>
																											</TR>
																										</TBODY>
																									</table>
																								</td>
																							</tr>
																						</table>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="����" style="width: 50"
																						class="formStylebutton" id="saveBtn"">
																					&nbsp;&nbsp;
																					<input type=reset class="formStylebutton"
																						style="width: 50" value="����"
																						onclick="javascript:history.back(1)">
																				</TD>
																			</tr>
																		</TABLE>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-footer"
																class="detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_01.jpg"
																						width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_03.jpg"
																						width="5" height="12" />
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

	<script>

function unionSelect(){
	var type = document.getElementById("type");
	var nameFont = document.getElementById("nameFont");
	var db_nameTD = document.getElementById("db_nameTD");
	var db_nameInput = document.getElementById("db_nameInput");
	var category = document.getElementById("category");
	var port  = document.getElementById("port");
	if(type.value == 2){
		nameFont.style.display="inline";
		db_nameTD.style.display="none";
		db_nameInput.style.display="none";
	}else{
		nameFont.style.display="none";
		db_nameTD.style.display="inline";
		db_nameInput.style.display="inline";
		
	}
	var categoryvalue = "";
	var portvalue = "";
	if(type.value == 1){
		categoryvalue = 53;
		portvalue = 1521;
	}else if(type.value == 2){
		categoryvalue = 54;
		portvalue = 1433;
	}else if(type.value == 4){
		categoryvalue = 52;
		portvalue = 3306;
	}else if(type.value == 5){
		categoryvalue = 59;
		portvalue = 50000;
	}else if(type.value == 6){
		categoryvalue = 55;
		portvalue = 2638;
	}else if(type.value == 7){
		categoryvalue = 60;
		portvalue = 9088;
	}
	port.value = portvalue;
	category.value = categoryvalue;
}

//unionSelect();

</script>

</HTML>