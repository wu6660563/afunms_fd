<%@page language="java" contentType="text/html;charset=gb2312" %>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
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
	        mainForm.action = "<%=rootPath%>/customReport.do?action=save";
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
											                	<td class="add-content-title"> 报表 >> 综合报表 >> 周报月报添加 </td>
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
																				<input type="text" id="name" name="name">
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
																				<input type="text" id="code" name="code">
																			</td>
																			<td align="right" height="24" width="10%">接收人:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				&nbsp;
																				<input type="text" name="userName" id="userName" readonly="readonly">
																				<input type="button" value="设置报表接收人"
																					onclick="setReciever('userName','userId');" />
																				<input type="hidden" id="userId" name="userId" value="">
																				<font color='red'>*</font>
																			</td>
																		</tr>
												    					<tr style="background-color: #ECECEC;"> 
																			<td align="right" height="24" width="10%">是否生成:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<select id="isCreate" name="isCreate" style="width: 132px;">
																					<option value="1">是</option>
																					<option value="0" >否</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">是否发送:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<select id="isSend" name="isSend" style="width: 132px;">
																					<option value="1">是</option>
																					<option value="0">否</option>
																				</select>
																			</td>					
												 						</tr> 
												 						<tr> 
												 							<td align="right" height="24" width="10%">邮箱标题:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="mailTitle" name="mailTitle" size="50">
																			</td>
																			<td align="right" height="24" width="10%">邮箱描述:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="mailDesc" name="mailDesc" size="50">
																			</td>
												 						</tr> 
												 						<tr style="background-color: #ECECEC;">
												 							<td align="right" height="24" width="10%">报表生成时间:&nbsp;</td>
												 							<td colspan="3">
												 								<!-- div start -->
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
																										<!-- 
																										<tr>
																											<TD style="WIDTH: 100px">
																												<span>发送时间:</span>
																											</TD>
																										</tr> -->
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
																													<OPTION value="day" selected>
																														每天
																													</OPTION>
																													<OPTION value="week">
																														每周
																													</OPTION>
																													<OPTION value="month">
																														每月
																													</OPTION>
																												</SELECT>
																											</TD>
																										</TR>
																										<TR>
																											<TD style="display: none;" id=td_sendtimeweek>
																												<SELECT disabled="disabled" style="WIDTH: 250px"
																													id=sendtimeweek multiple size=5 name=sendtimeweek>
																													<OPTION selected value=0>
																														星期日
																													</OPTION>
																													<OPTION value=1>
																														星期一
																													</OPTION>
																													<OPTION value=2>
																														星期二
																													</OPTION>
																													<OPTION value=3>
																														星期三
																													</OPTION>
																													<OPTION value=4>
																														星期四
																													</OPTION>
																													<OPTION value=5>
																														星期五
																													</OPTION>
																													<OPTION value=6>
																														星期六
																													</OPTION>
																												</SELECT>
																											</TD>
																											<TD style="display: none;" id=td_sendtimeday>
																												<SELECT disabled="disabled" style="WIDTH: 250px"
																													id=sendtimeday size=5 name=sendtimeday>
																													<OPTION selected value=01>
																														01日
																													</OPTION>
																													<OPTION value=02>
																														02日
																													</OPTION>
																													<OPTION value=03>
																														03日
																													</OPTION>
																													<OPTION value=04>
																														04日
																													</OPTION>
																													<OPTION value=05>
																														05日
																													</OPTION>
																													<OPTION value=06>
																														06日
																													</OPTION>
																													<OPTION value=07>
																														07日
																													</OPTION>
																													<OPTION value=08>
																														08日
																													</OPTION>
																													<OPTION value=09>
																														09日
																													</OPTION>
																													<OPTION value=10>
																														10日
																													</OPTION>
																													<OPTION value=11>
																														11日
																													</OPTION>
																													<OPTION value=12>
																														12日
																													</OPTION>
																													<OPTION value=13>
																														13日
																													</OPTION>
																													<OPTION value=14>
																														14日
																													</OPTION>
																													<OPTION value=15>
																														15日
																													</OPTION>
																													<OPTION value=16>
																														16日
																													</OPTION>
																													<OPTION value=17>
																														17日
																													</OPTION>
																													<OPTION value=18>
																														18日
																													</OPTION>
																													<OPTION value=19>
																														19日
																													</OPTION>
																													<OPTION value=20>
																														20日
																													</OPTION>
																													<OPTION value=21>
																														21日
																													</OPTION>
																													<OPTION value=22>
																														22日
																													</OPTION>
																													<OPTION value=23>
																														23日
																													</OPTION>
																													<OPTION value=24>
																														24日
																													</OPTION>
																													<OPTION value=25>
																														25日
																													</OPTION>
																													<OPTION value=26>
																														26日
																													</OPTION>
																													<OPTION value=27>
																														27日
																													</OPTION>
																													<OPTION value=28>
																														28日
																													</OPTION>
																													<OPTION value=29>
																														29日
																													</OPTION>
																													<OPTION value=30>
																														30日
																													</OPTION>
																													<OPTION value=31>
																														31日
																													</OPTION>
																												</SELECT>
																											</TD>
																											<TD style="" id=td_sendtimehou>
																												<SELECT style="WIDTH: 250px" id=sendtimehou
																													size=5 name=sendtimehou>
																													<OPTION value=00 selected="selected">
																														00AM
																													</OPTION>
																													<OPTION value=01>
																														01AM
																													</OPTION>
																													<OPTION value=02>
																														02AM
																													</OPTION>
																													<OPTION value=03>
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