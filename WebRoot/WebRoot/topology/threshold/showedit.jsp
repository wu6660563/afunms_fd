<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.alarm.model.AlarmWay"%>
<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)request.getAttribute("alarmIndicatorsNode");
    String nodeid = (String)request.getAttribute("nodeid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
  	String onesel = "";
  	String twosel = "";
  	if(alarmIndicatorsNode.getCompare()==1)onesel = "selected";
  	if(alarmIndicatorsNode.getCompare()==0)twosel = "selected";
  	
  	Hashtable alarmWayHashtable = (Hashtable)request.getAttribute("alarmWayHashtable");
  	
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
	        mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showUpdate";
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
	
	initAttribute();
}

</script>

<script type="text/javascript">

	//-- nielin modify at 2010-01-04 start ----------------
	function CreateWindow(url){
		msgWindow=window.open(url,"protypeWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no")
	}  


	function initAttribute(){
		document.getElementById("enabled").value = "<%=alarmIndicatorsNode.getEnabled()%>";
		document.getElementById("sms0").value = "<%=alarmIndicatorsNode.getSms0()%>";
		document.getElementById("sms1").value = "<%=alarmIndicatorsNode.getSms1()%>";
		document.getElementById("sms2").value = "<%=alarmIndicatorsNode.getSms2()%>";
		document.getElementById("datatype").value = "<%=alarmIndicatorsNode.getDatatype()%>";
		
		<%
			if(alarmWayHashtable != null){
				AlarmWay alarmWay0 = (AlarmWay)alarmWayHashtable.get("way0");
				if(alarmWay0!=null){
					%>
					document.getElementById("way0-id").value = "<%=alarmIndicatorsNode.getWay0()%>";
					document.getElementById("way0-name").value = "<%=alarmWay0.getName()%>";
					<%
				}
				
				AlarmWay alarmWay1 = (AlarmWay)alarmWayHashtable.get("way1");
				if(alarmWay1!=null){
					%>
					document.getElementById("way1-id").value = "<%=alarmIndicatorsNode.getWay1()%>";
					document.getElementById("way1-name").value = "<%=alarmWay1.getName()%>";
					<%
				}
				
				AlarmWay alarmWay2 = (AlarmWay)alarmWayHashtable.get("way2");
				if(alarmWay2!=null){
					%>
					document.getElementById("way2-id").value = "<%=alarmIndicatorsNode.getWay2()%>";
					document.getElementById("way2-name").value = "<%=alarmWay2.getName()%>";
					<%
				}
			}
			
		%>
	}
	
	function chooseAlarmWay(value,alarmWayIdEvent,alarmWayNameEvent){
		CreateWindow("<%=rootPath%>/alarmWay.do?action=chooselist&jp=1&alarmWayNameEvent=" + alarmWayNameEvent + "&alarmWayIdEvent=" + alarmWayIdEvent);
	}
	
</script>

</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" name="type" id="type" value="<%=type%>">
		<input type="hidden" name="subtype" id="subtype" value="<%=subtype%>">
		<input type="hidden" name="isDefault" id="isDefault">
		<input type="hidden" id="name" name="name"  value="<%=alarmIndicatorsNode.getName()%>">
		<input type="hidden" id="id" name="id" value="<%=alarmIndicatorsNode.getId()%>">
		<input type="hidden" id="nodeid" name="nodeid" value="<%=alarmIndicatorsNode.getNodeid()%>">
		
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
											                	<td class="add-content-title"> 资源 >> 性能监视 >> 性能指标 >> 性能指标添加 </td>
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
																		<tr>						
																			<td align="right" height="24" width="10%">名称:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="name" name="name" readonly="readonly" value="<%=alarmIndicatorsNode.getName()%>">
																			</td>
																			<td align="right" height="24" width="10%">类型:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="type" name="type" readonly="readonly" value="<%=alarmIndicatorsNode.getType()%>">
																			</td>
																		</tr>	
																		<tr style="background-color: #ECECEC;">
																			<td align="right" height="24" width="10%">子类型:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="subtype" name="subtype" readonly="readonly" value="<%=alarmIndicatorsNode.getSubtype()%>">
																			</td>
																			<td align="right" height="24" width="10%">描述:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="descr" name="descr" value="<%=alarmIndicatorsNode.getDescr()%>">
																			</td>
																		</tr>
												    						<tr> 
												  							<td align="right" height="24" width="10%">是否监视:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="enabled" name="enabled">
																					<option value="0">否</option>
																					<option value="1">是</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">告警描述:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<input type="text" name="alarm_info" id="alarm_info" value="<%=alarmIndicatorsNode.getAlarm_info()%>">
																			</td>					
												 						</tr> 
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">阀值比较方式:&nbsp;</td>				
																			<td width="90%" colspan=3>&nbsp;
																				<select id="compare" name="compare">
																					<option value="1" <%=onesel%>>升序</option>
																					<option value="0" <%=twosel%>>降序</option>
																				</select>
																			</td>				
												 						</tr>
												 						<tr> 
												  							<td align="right" height="24" width="10%">阀值单位:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" name="threshold_unit" id="threshold_unit" value="<%=alarmIndicatorsNode.getThreshlod_unit()%>">
																			</td>
																			<td align="right" height="24" width="10%">数据类型:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select name="datatype" id="datatype">
																					<option value="String">字符串</option>
																					<option value="Number">数字</option>
																				</select>
																			</td>
												 						</tr> 
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">一级阀值:&nbsp;</td>				
																			<td width="90%" colspan=3>&nbsp;
																				<input type="text" name="limenvalue0" id="limenvalue0" value="<%=alarmIndicatorsNode.getLimenvalue0()%>">
																			</td>
												 						</tr> 
												 						<tr> 
												  							<td align="right" height="24" width="10%">连续次数:&nbsp;</td>				
																			<td width="40%" colspan=3>&nbsp;
																				<input type="text" name="time0" id="time0" value="<%=alarmIndicatorsNode.getTime0()%>">
																			</td>
												 						</tr> 
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">告警:&nbsp;</td>				
																			<td width="40%" >&nbsp;
																				<select name="sms0" id="sms0">
																					<option value="0">否</option>
																					<option value="1">是</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">告警方式:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" disabled="disabled" value="" name="way0-name" id="way0-name">
																				<input type="hidden" value="" name="way0-id" id="way0-id">
																				<a href="#" onclick="chooseAlarmWay('','way0-id','way0-name')">浏览</a>
																			</td>
												 						</tr>
												 						<tr> 
												  							<td align="right" height="24" width="10%">二级阀值:&nbsp;</td>				
																			<td width="40%" colspan=3>&nbsp;
																				<input type="text" name="limenvalue1" id="limenvalue1" value="<%=alarmIndicatorsNode.getLimenvalue1()%>">
																			</td>
												 						</tr>
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">连续次数:&nbsp;</td>				
																			<td width="40%" colspan=3>&nbsp;
																				<input type="text" name="time1" id="time1" value="<%=alarmIndicatorsNode.getTime1()%>">
																			</td>
												 						</tr>
												 						<tr> 
												  							<td align="right" height="24" width="10%">告警:&nbsp;</td>				
																			<td width="40%" >&nbsp;
																				<select name="sms1" id="sms1">
																					<option value="0">否</option>
																					<option value="1">是</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">告警方式:&nbsp;</td>				
																			<td width="40%" >&nbsp;
																				<input type="text" disabled="disabled" value="" name="way1-name" id="way1-name">
																				<input type="hidden" value="" name="way1-id" id="way1-id">
																				<a href="#" onclick="chooseAlarmWay('','way1-id','way1-name')">浏览</a>
																			</td>
												 						</tr>
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">三级阀值:&nbsp;</td>				
																			<td width="40%" colspan=3>&nbsp;
																				<input type="text" name="limenvalue2" id="limenvalue2" value="<%=alarmIndicatorsNode.getLimenvalue2()%>">
																			</td>
												 						</tr>
												 						<tr> 
												  							<td align="right" height="24" width="10%">连续次数:&nbsp;</td>				
																			<td width="40%" colspan=3>&nbsp;
																				<input type="text" name="time2" id="time2" value="<%=alarmIndicatorsNode.getTime2()%>">
																			</td>
												 						</tr>
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">告警:&nbsp;</td>				
																			<td width="40%" >&nbsp;
																				<select name="sms2" id="sms2">
																					<option value="0">否</option>
																					<option value="1">是</option>
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">告警方式:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" disabled="disabled" value="" name="way2-name" id="way2-name">
																				<input type="hidden" value="" name="way2-id" id="way2-id">
																				<a href="#" onclick="chooseAlarmWay('','way2-id','way2-name')">浏览</a>
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