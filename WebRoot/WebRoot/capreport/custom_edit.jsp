<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.capreport.model.CustomReportVo"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  CustomReportVo vo = (CustomReportVo) request.getAttribute("vo");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>





<script language="JavaScript" type="text/javascript">


	Ext.onReady(function(){  

	 	Ext.get("process").on("click",function(){
	        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
	        mainForm.action = "<%=rootPath%>/customReport.do?action=update";
	        mainForm.submit();
	 	});	
	});
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

function timeType(obj){
	var type = obj.value;
	document.getElementById('td_sendtimehou').style.display='none';
	document.getElementById('td_sendtimeday').style.display='none';
	document.getElementById('td_sendtimeweek').style.display='none';
	document.getElementById('sendtimehou').disabled="disabled";
	document.getElementById('sendtimeday').disabled="disabled";
	document.getElementById('sendtimeweek').disabled="disabled";
	if(type=="day"){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('sendtimehou').disabled="";
	}else if(type=="week"){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeweek').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeweek').disabled="";
	}else if(type=="month"){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
	}
}

function setReciever(ctrlId,hideCtrlId){
	return CreateWindow("<%=rootPath%>/subscribeReportConfig.do?action=user_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function CreateWindow(url){
 msgWindow=window.open(url,"_blank","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
</script>

</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
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
											                	<td class="add-content-title"> 报表 >> 综合报表 >> 周报月报定制编辑 </td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        											<table cellspacing="1">
																		<tr style="background-color: #ECECEC;">						
																			<td align="right" height="24" width="10%">报表名称:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="hidden" id="id" name="id" value="<%=vo.getId()%>">
																				<input type="text" id="name" name="name" value="<%=vo.getName()%>">
																			</td>
																			<td align="right" height="24" width="10%">导出类型:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="fileType" name="fileType" style="width: 132px;">
																					<option value="excel">Excel</option>
																					<option value="word">Word</option>
																					<option value="pdf">Pdf</option>
																				</select>
																			</td>
																		</tr>	
																		<tr>
																			<td align="right" height="24" width="10%">报表编号:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="code" name="code" value="<%=vo.getCode()%>">
																			</td>
																			<td align="right" height="24" width="10%">接收人:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				&nbsp;
																				<input type="text" name="userName" id="userName" readonly="readonly" value="<%=vo.getUserId()%>">
																				<input type="button" value="设置报表接收人"
																					onclick="setReciever('userName','userId');" />
																				<input type="hidden" id="userId" name="userId" value="<%=vo.getUserId()%>">
																				<font color='red'>*</font>
																			</td>
																		</tr>
												    					<tr style="background-color: #ECECEC;"> 
																			<td align="right" height="24" width="10%">是否生成:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<select id="isCreate" name="isCreate" style="width: 132px;">
																					<option value="1" <%if("1".equals(vo.getIsCreate())){%> selected="selected"<%} %>>是</option>
																					<option value="0" <%if("0".equals(vo.getIsCreate())){%> selected="selected"<%} %>>否</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">是否发送:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<select id="isSend" name="isSend" style="width: 132px;">
																					<option value="1"  <%if("1".equals(vo.getIsSend())){%> selected="selected"<%} %>>是</option>
																					<option value="0"  <%if("0".equals(vo.getIsSend())){%> selected="selected"<%} %>>否</option>
																				</select>
																			</td>					
												 						</tr> 
												 						<tr> 
												 							<td align="right" height="24" width="10%">邮箱标题:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="mailTitle" name="mailTitle" size="50" value="<%=vo.getMailTitle()%>">
																			</td>
																			<td align="right" height="24" width="10%">邮箱描述:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="mailDesc" name="mailDesc" size="50" value="<%=vo.getMailDesc()%>">
																			</td>
												 						</tr> 
												 						<tr style="background-color: #ECECEC;">
												 							<td align="right" height="24" width="10%">报表生成时间:&nbsp;</td>
												 							<td colspan="3">
												 								<!--  div start -->
												 								<div id="formDiv" style="">
																					<table width="100%" style="BORDER-COLLAPSE: collapse"
																						borderColor=#cedefa cellPadding=0 rules=none border=1
																						align="center">
																						<tr>
																							<td align="left">
																								<br>
																								<table id="timeConfigTable"
																									style="width: 60%; padding: 0; background-color: #FFFFFF; left: 15px;">
																									<TBODY>
																										<tr>
																											<TD style="WIDTH: 100px">
																												&nbsp;
																											</TD>
																										</tr>
																										<TR>
																											<TD>
																												<SELECT style="WIDTH: 250px" id=type
																													onchange="javascript:timeType(this)"
																													name=type>
																													<OPTION value=day <%if("day".equals(vo.getType())){%> selected="selected"<%} %>>
																														每天
																													</OPTION>
																													<OPTION value=week <%if("week".equals(vo.getType())){%> selected="selected"<%} %>>
																														每周
																													</OPTION>
																													<OPTION value=month <%if("month".equals(vo.getType())){%> selected="selected"<%} %>>
																														每月
																													</OPTION>
																												</SELECT>
																											</TD>
																										</TR>
																										<TR>
																											<TD <%if("week".equals(vo.getType())&&!"".equals(vo.getSendDate())){%>style="display: block;"<%}else{%>style="display: none;"<%}%> id=td_sendtimeweek>
																												<SELECT <%if("week".equals(vo.getType())&&!"".equals(vo.getSendDate())){%><%}else{%>disabled="disabled"<%}%> style="WIDTH: 250px"
																													id=sendtimeweek multiple size=5 name=sendtimeweek>
																													<OPTION value=00 <%if("week".equals(vo.getType())&&"00".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期日
																													</OPTION>
																													<OPTION value=01 <%if("week".equals(vo.getType())&&"01".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期一
																													</OPTION>
																													<OPTION value=02 <%if("week".equals(vo.getType())&&"02".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期二
																													</OPTION>
																													<OPTION value=03 <%if("week".equals(vo.getType())&&"03".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期三
																													</OPTION>
																													<OPTION value=04 <%if("week".equals(vo.getType())&&"04".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期四
																													</OPTION>
																													<OPTION value=05 <%if("week".equals(vo.getType())&&"05".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期五
																													</OPTION>
																													<OPTION value=06 <%if("week".equals(vo.getType())&&"06".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														星期六
																													</OPTION>
																												</SELECT>
																											</TD>
																											<TD  <%if("month".equals(vo.getType())&&!"".equals(vo.getSendDate())){%>style="display: block;"<%}else{%>style="display: none;"<%}%> id=td_sendtimeday>
																												<SELECT  <%if("month".equals(vo.getType())&&!"".equals(vo.getSendDate())){%><%}else{%>disabled="disabled"<%}%> style="WIDTH: 250px"
																													id=sendtimeday size=5 name=sendtimeday>
																													<OPTION value=01 <%if("month".equals(vo.getType())&&"01".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														01日
																													</OPTION>
																													<OPTION value=02 <%if("month".equals(vo.getType())&&"02".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														02日
																													</OPTION>
																													<OPTION value=03 <%if("month".equals(vo.getType())&&"03".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														03日
																													</OPTION>
																													<OPTION value=04 <%if("month".equals(vo.getType())&&"04".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														04日
																													</OPTION>
																													<OPTION value=05 <%if("month".equals(vo.getType())&&"05".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														05日
																													</OPTION>
																													<OPTION value=06 <%if("month".equals(vo.getType())&&"06".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														06日
																													</OPTION>
																													<OPTION value=07 <%if("month".equals(vo.getType())&&"07".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														07日
																													</OPTION>
																													<OPTION value=08 <%if("month".equals(vo.getType())&&"08".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														08日
																													</OPTION>
																													<OPTION value=09 <%if("month".equals(vo.getType())&&"09".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														09日
																													</OPTION>
																													<OPTION value=10 <%if("month".equals(vo.getType())&&"10".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														10日
																													</OPTION>
																													<OPTION value=11 <%if("month".equals(vo.getType())&&"11".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														11日
																													</OPTION>
																													<OPTION value=12 <%if("month".equals(vo.getType())&&"12".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														12日
																													</OPTION>
																													<OPTION value=13 <%if("month".equals(vo.getType())&&"13".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														13日
																													</OPTION>
																													<OPTION value=14 <%if("month".equals(vo.getType())&&"14".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														14日
																													</OPTION>
																													<OPTION value=15 <%if("month".equals(vo.getType())&&"15".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														15日
																													</OPTION>
																													<OPTION value=16 <%if("month".equals(vo.getType())&&"16".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														16日
																													</OPTION>
																													<OPTION value=17 <%if("month".equals(vo.getType())&&"17".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														17日
																													</OPTION>
																													<OPTION value=18 <%if("month".equals(vo.getType())&&"18".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														18日
																													</OPTION>
																													<OPTION value=19 <%if("month".equals(vo.getType())&&"19".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														19日
																													</OPTION>
																													<OPTION value=20 <%if("month".equals(vo.getType())&&"20".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														20日
																													</OPTION>
																													<OPTION value=21 <%if("month".equals(vo.getType())&&"21".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														21日
																													</OPTION>
																													<OPTION value=22 <%if("month".equals(vo.getType())&&"22".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														22日
																													</OPTION>
																													<OPTION value=23 <%if("month".equals(vo.getType())&&"23".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														23日
																													</OPTION>
																													<OPTION value=24 <%if("month".equals(vo.getType())&&"24".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														24日
																													</OPTION>
																													<OPTION value=25 <%if("month".equals(vo.getType())&&"25".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														25日
																													</OPTION>
																													<OPTION value=26 <%if("month".equals(vo.getType())&&"26".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														26日
																													</OPTION>
																													<OPTION value=27 <%if("month".equals(vo.getType())&&"27".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														27日
																													</OPTION>
																													<OPTION value=28 <%if("month".equals(vo.getType())&&"28".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														28日
																													</OPTION>
																													<OPTION value=29 <%if("month".equals(vo.getType())&&"29".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														29日
																													</OPTION>
																													<OPTION value=30 <%if("month".equals(vo.getType())&&"30".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														30日
																													</OPTION>
																													<OPTION value=31 <%if("month".equals(vo.getType())&&"31".equals(vo.getSendDate())){%> selected="selected"<%} %>>
																														31日
																													</OPTION>
																												</SELECT>
																											</TD>
																											<TD style="" id=td_sendtimehou>
																												<SELECT style="WIDTH: 250px" id=sendtimehou
																													size=5 name=sendtimehou>
																													<OPTION value=00 <%if("00".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														00AM
																													</OPTION>
																													<OPTION value=01 <%if("01".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														01AM
																													</OPTION>
																													<OPTION value=02 <%if("02".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														02AM
																													</OPTION>
																													<OPTION value=03 <%if("03".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														03AM
																													</OPTION>
																													<OPTION value=04 <%if("04".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														04AM
																													</OPTION>
																													<OPTION value=05 <%if("05".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														05AM
																													</OPTION>
																													<OPTION value=06 <%if("06".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														06AM
																													</OPTION>
																													<OPTION value=07 <%if("07".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														07AM
																													</OPTION>
																													<OPTION value=08 <%if("08".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														08AM
																													</OPTION>
																													<OPTION value=09 <%if("09".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														09AM
																													</OPTION>
																													<OPTION value=10 <%if("10".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														10AM
																													</OPTION>
																													<OPTION value=11 <%if("11".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														11AM
																													</OPTION>
																													<OPTION value=12 <%if("12".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														12AM
																													</OPTION>
																													<OPTION value=13 <%if("13".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														01PM
																													</OPTION>
																													<OPTION value=14 <%if("14".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														02PM
																													</OPTION>
																													<OPTION value=15 <%if("15".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														03PM
																													</OPTION>
																													<OPTION value=16 <%if("16".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														04PM
																													</OPTION>
																													<OPTION value=17 <%if("17".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														05PM
																													</OPTION>
																													<OPTION value=18 <%if("18".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														06PM
																													</OPTION>
																													<OPTION value=19 <%if("19".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														07PM
																													</OPTION>
																													<OPTION value=20 <%if("20".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														08PM
																													</OPTION>
																													<OPTION value=21 <%if("21".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														09PM
																													</OPTION>
																													<OPTION value=22 <%if("22".equals(vo.getSendTime())){%> selected="selected"<%} %>>
																														10PM
																													</OPTION>
																													<OPTION value=23 <%if("23".equals(vo.getSendTime())){%> selected="selected"<%} %>>
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
																				<!-- div end -->
												 							</td>
												 						</tr>
																		<tr>
																			<TD nowrap colspan="4" align=center>
																			<br><input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																				<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
																			</TD>	
																		</tr>	
																	</table>
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
</BODY>
</HTML>