<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.capreport.model.ReportConfigNode"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String rootPath = request.getContextPath();
  ReportConfigNode node =(ReportConfigNode)request.getAttribute("node");
  String json_timeArr = (String)request.getAttribute("json_time");
  String flag = (String)request.getAttribute("flag");
  //List list = (List)request.getAttribute("list");
  
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
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
		<script type="text/javascript" src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script>
	$(document).ready(function(){
	var timeList=eval('<%=json_timeArr %>');
	var dayOfWeek=["星期日",'星期一','星期二','星期三','星期四','星期五','星期六'];
	function get_week_select(name,value)
	{
		var j_select=$('<select></select>',{'id':name,'name':name}).attr("name",name);
		for(i=1;i<=7;i++)
		{
			if(value==i)
				$('<option></option>').text(dayOfWeek[i-1]).val(i).appendTo(j_select).attr("selected", true);
			else
				$('<option></option>').text(dayOfWeek[i-1]).val(i).appendTo(j_select);
		}
		return j_select;
	}
	function get_hour_select(name,value)
	{
		var j_select=$('<select></select>',{'id':name,'name':name}).attr("name",name);
		for(var i=0;i<24;i++)
		{
			if(value==i)
				$('<option></option>').text(i).val(i).appendTo(j_select).attr("selected", true);
			else
				$('<option></option>').text(i).val(i).appendTo(j_select)
		}
		return j_select;
	}
	
	
		$('#addTimeConfigSpan').bind('click',function(){
			var i=0;
			//var num = $("#timeConfigTable tr").length-1;
			var num = Date.parse(new Date());
			var weekSelectCtrl = $("<select>",{"name":"week"+num,"id":"week"+num});
			var hourSelectCtrl = $("<select>",{"name":"hour"+num,"id":"hour"+num});
			var dayOfWeek=["星期日",'星期一','星期二','星期三','星期四','星期五','星期六'];
			for(i=1;i<=7;i++)
			{
				$("<option>").attr("value",i).html(dayOfWeek[i-1]).appendTo(weekSelectCtrl);
			}
			for(i=0;i<24;i++)
			{
				$("<option>").attr("value",i).html(i).appendTo(hourSelectCtrl);
			}
			var t = $("<tr>").css({"border":"1"});
			t.append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(weekSelectCtrl)).append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(hourSelectCtrl).append("时")).append($("<td>").append($("<a href='javascript:;'>删除</a>").css({"border":"1 solid #000000","width":"70px"}).bind('click',function(){$(this).parent().parent().remove();})));
			t.insertBefore("#timeConfigTable tr:last");
			//alert($('#timeConfigTable').html());
		});
		$('#updateBtn').bind('click',function(){
			if($('#recievers_name').val()==null || $('#recievers_name').val()=='')
				return;
			if($('#bidtext').val()==null || $('#bidtext').val()=='')
				return;
			if($('#devices_name').val()==null || $('#devices_name').val()=='')
				return;
			mainForm.action = "<%=rootPath%>/subscribeReportConfig.do?action=update";
        	mainForm.submit();
		});
		function doit()
		{
			var i=0;
		for(i=0;i<timeList.length;i++)
		{
			var dayofweek=timeList[i].dayofweek;
			var hour=timeList[i].hour;
			var time=Date.parse(new Date())+i;
			
			var t = $("<tr>").css({"border":"1"});
			t.append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(get_week_select('week'+time,dayofweek))).append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(get_hour_select('hour'+time,hour)).append("时")).append($("<td>").append($("<a href='javascript:;'>删除</a>").css({"border":"1 solid #000000","width":"70px"}).bind('click',function(){$(this).parent().parent().remove();})));
			//t.append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(get_week_select('week'+time,dayofweek))).append($("<td>").css({"border":"1 solid #000000","width":"70px"}).append(get_hour_select('hour'+time,hour)).append("时")).append($("<td>").append($("<a href='javascript:;'>删除</a>").css({"border":"1 solid #000000","width":"70px"}).bind('click',function(){$(this).parent().parent().remove();})));
			t.insertBefore("#timeConfigTable tr:last");
		}
		//alert($('#timeConfigTable').html());
		}
		window.doit=doit;
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
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
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
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=900,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
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
//修改菜单的上下箭头符号
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
//添加菜单	
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
	doit();
}

</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="category" name="category" value="">
			<input type="hidden" id="id" name="id" value="<%=node.getConfig().getId() %>">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">

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
																	<td class="add-content-title">
																		报表 >> 报表订阅 >> 订阅设置 >> 设置一览
																	</td>
																	<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
																		<table border="0" id="table1" cellpadding="0" width="100%">
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					报表接收人&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="recievers_name" id="recievers_name" size="50" class="formStyle" size="50" value="<%=node.getRecieverNames() %>">
																					<input type="button" value="设置报表接收人" onclick="setReciever('recievers_name','recievers_id');"/>
																					<input type="hidden" id="recievers_id" name="recievers_id" value="<%=node.getConfig().getCollectionOfRecieverId() %>">
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					所属业务&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="bidtext" id="bidtext" size="50" class="formStyle" value="<%=node.getBidNames() %>">
																					<input type="button" value="设置所属业务" onclick='setBid("bidtext" , "bid");'/>
																					<input type="hidden" id="bid" name="bid" value="<%=node.getConfig().getBids() %>">
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					设备列表&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="devices_name" id="devices_name" size="50" class="formStyle" value="<%=node.getDeviceNames() %>">
																					<input type="button" value="设置设备列表" onclick="setDevices('devices_name','devices_id');"/>
																					<input type="hidden" id="devices_id" name="devices_id" value="<%=node.getConfig().getCollectionOfdeviceId() %>">
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24">报表生成时间&nbsp;</TD>
																				<td nowrap  colspan="3">
																			        <div id="formDiv" style="">         
																		                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																	                        <tr>
																	                            <td align="left">  
																		                            <br>
																	                                <table id="timeConfigTable" style="width:60%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
																                                        <tr>
																                                            <td colspan="7" height="50" align="center"> 
																                                                <span id="addTimeConfigSpan" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
																                                            </td>
																                                        </tr>
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
																					<input type="button" value="保存" style="width: 50" class="formStylebutton" id="updateBtn"">
																					&nbsp;&nbsp;
																					<input type=reset class="formStylebutton" style="width: 50" value="返回" onclick="javascript:history.back(1)">
																				</TD>
																			</tr>
																			<tr>
																				<td><div id="mydiv"></div></td>
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