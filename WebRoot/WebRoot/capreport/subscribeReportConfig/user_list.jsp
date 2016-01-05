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
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String ctrlId = (String)request.getAttribute("ctrlId");
  String hideCtrlId = (String)request.getAttribute("hideCtrlId");
  String rootPath = request.getContextPath();
  String flag = (String)request.getAttribute("flag");
  List userList = (List)request.getAttribute("userList");
  //List list = (List)request.getAttribute("list");
  
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
		<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow函数在application/resource/js/addTimeConfig.js中 
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
  	if(document.getElementById("db_nameInput").style.display=="none"){
  		var chk1 = checkinput("alias","string","名称",30,false);
    	var chk2 = checkinput("ip_address","string","IP地址",15,false);
     	var chk4 = checkinput("password","string","密码",30,false);
   		var chk3 = true;
  	}else{
  		var chk1 = checkinput("alias","string","名称",30,false);
    	var chk2 = checkinput("ip_address","string","IP地址",15,false);
     	var chk3 = checkinput("db_name","string","数据库名",30,false);
     	var chk4 = checkinput("password","string","密码",30,false);
  	}
     
          
     if(chk1&&chk2&&chk3&&chk4)
     {
        mainForm.action = "<%=rootPath%>/db.do?action=add";
        mainForm.submit();
     }  
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
function setReciever()
{
	return CreateWindow("<%=rootPath%>/subscribeReportConfig.do?action=user_list");
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
}

</script>
<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js"
			charset="gb2312"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#saveBtn').bind('click',function(){
			if($("input:checked").length==0)
				return;
			var i=0;
			var checked_ctrl = $("input:checked");
			var div_sub_span_ctrl = checked_ctrl.parent().find("span");
			var checked_val="";
			var hide_val="";
			for(i=0;i<checked_ctrl.length;i++)
			{
				var checked_sub_ctrl=checked_ctrl.eq(i);
				var span_sub_ctrl=div_sub_span_ctrl.eq(i);
				//alert(span_sub_ctrl.text()+"="+checked_sub_ctrl.val());
				checked_val=checked_val+","+span_sub_ctrl.text();
				hide_val=hide_val+","+checked_sub_ctrl.val();
			}
			var parent_checked_ctrl = parent.opener.document.getElementById("<%=ctrlId%>");
			var parent_hide_ctrl = parent.opener.document.getElementById("<%=hideCtrlId%>");
 			parent_checked_ctrl.value=checked_val; 
 			parent_hide_ctrl.value = hide_val;
 			window.close();
		});
	});
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
																	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																	<td class="add-content-title">
																		用户一览
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
																					用户：&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<%
																					if(userList!=null)
																					{
																						for(int i=0;i<userList.size();i++)
																						{
																							User user = (User)userList.get(i);
																					%>
																					<div><input type="checkbox" name="user_name" value="<%=user.getId() %>" /><span><%=user.getName() %></span></div>
																					<%
																						}
																					}
		 																			%>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="保存" style="width: 50" class="formStylebutton" id="saveBtn">
																					&nbsp;&nbsp;
																					<input type=reset class="formStylebutton" style="width: 50" value="返回" onclick="javascript:window.close()">
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