<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@ include file="/include/globe.inc"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.alarm.model.AlarmLinkType"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  
  ArrayList<AlarmLinkType> linkTypeList = (ArrayList<AlarmLinkType>)request.getAttribute("linkTypeList"); 
  
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
	 		
	 		if(checkBeforeSave()<1){
	 			return;
	 		}
	        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
	        //msg.style.display="block";
	        mainForm.action = "<%=rootPath%>/alarmlink.do?action=save&str="+str;
	        mainForm.submit();
	 	});	
	});
	
	
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------



//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------


function checkBeforeSave(){
	var linktype = document.getElementById("linktype");
	if(linktype.value < 0){
		alert("请选择或添加告警组名称！");
		return -1;
	}
	if(rowNum < 1) { // 没有增加告警信息
		alert("请增加一行告警信息");
		return -1;
	}
	
	for(var i=0; i<=rowNum; i++){
		
	}
	
	
	return 1;
}


function addLinkType(){
	mainForm.action = "<%=rootPath%>/alarmlink.do?action=showLinkType";
	mainForm.submit();
}


function openOldIndicators() {

	//addAlarmRowNode();
	if(rowNum <1){
		alert("请选择增加一行！");
		return;
	}
	window.open('<%=rootPath%>/alarmlink.do?action=openOldIndicators&str='+str);
}

var delRow = 0;


function delrow(tr0){
	//alert(tr0);
	//if(tr0 >= delRow){
	//	tr0=tr0-1;
	//}
	var flag = 0;
	var table ;
	if(0==flag) {
	table = document.getElementById("alarmLinkNodeTable");
	falg = 1;
	}
	delRow = tr0;
	//alert(tr0);
	//document.all.test.deleteRow(window.event.srcElement.parentElement.parentElement.rowIndex);
	var i=0;
	while(table.rows[i].firstChild.firstChild >0) {
		alert(table.rows[i].firstChild.firstChild.id);
	}
	
//	while (alarmConfigDivId.rows[i].firstChild.firstChild.id != "table"
//			+ str) {
//		i++;
//	}
	
	var index = table.rows[tr0].parentNode.parentNode.rowIndex;
	alert(index);
	//table.deleteRow(index);
	//table.deleteRow(tr0);
	//table.deleteRow(tr0+1);
	//delrow(tr0);
	//mainForm.deleteRow(row.id);
}

function deleteRow(str,rowNum) {
	var table = document.getElementById("alarmLinkNodeTable");
	var i = 0;
	while (table.rows[i].id != "alarmLinkNodeTable-tr0-"
			+ str) {
		i++;
	}
	table.deleteRow(i); // 删除第一个tr
	
	i = 0;
	while (table.rows[i].id != "alarmLinkNodeTable-tr1-"
			+ str) {
		i++;
	}
	table.deleteRow(i); // 删除第二个tr
	
	i = 0;
	while (table.rows[i].id != "alarmLinkNodeTable-tr2-"
			+ str) {
		i++;
	}
	table.deleteRow(i); // 删除第三个tr
	
}

var rowNum = 0;
var str = "";
function addAlarmRowNode(){
	var table = document.getElementById("alarmLinkNodeTable");	// 取得table节点
	var tr0 = table.insertRow();	// 取得行
	tr0.bgColor = "#ECECEC";
	rowNum += 1;
	str = "" + rowNum;
	tr0.id = "alarmLinkNodeTable-tr0-" + str;
	// 设备名称
	var td02 = tr0.insertCell();
	var nameText = '&nbsp;&nbsp;设备名称 <input id="name-' + str +'" name="name-'+ str + '" type="text" size="20" class="input-text" readonly="readonly"> ';
	td02.innerHTML = nameText;
	// 设备IP	
	var td03 = tr0.insertCell();
	var ipaddressText = '设备IP <input id="ip-' + str +'" name="ip-'+ str + '" type="text" size="14"  class="input-text" readonly="readonly"> ';
	td03.innerHTML = ipaddressText;
	// 阀值名
	var td04 = tr0.insertCell();
	var indiNameText = '阀值名 <input id="indiName-' + str +'" name="indiName-'+ str + '" type="text" size="14"  class="input-text" readonly="readonly" > ';
	td04.innerHTML = indiNameText;
	
	// 描述
	var td05 = tr0.insertCell();
	var descrText = '描  述 <input id="descr-' + str +'" name="descr-'+ str + '" type="text" size="21"  class="input-text" readonly="readonly" > ';
	td05.innerHTML = descrText;
	
	var names = "name-" +str;
	var ips = "ip-" + str;
	var td06 = tr0.insertCell();
	td06.innerHTML = '<a href="javascript:deleteRow('+ str +')">删除</a>'
	
	//---------------------------tr1
	var tr1 = table.insertRow();
	tr1.bgColor = "#ECECEC";
	tr1.id = "alarmLinkNodeTable-tr1-" + str;
	// 一级阀值
	var td100 = tr1.insertCell();
	var indicatorsNameText = '&nbsp;&nbsp;一级阀值 <input id="limen0-' + str +'" name="limen0-'+ str + '" type="text" size="10"  class="input-text"  >';
	td100.innerHTML = indicatorsNameText;
	// 次数
	var td101 = tr1.insertCell();
	var indicatorsNameText = '次数 <input id="time0-' + str +'" name="time0-'+ str + '" type="text" size="10"  class="input-text" >';
	td101.innerHTML = indicatorsNameText;
	// 二级阀值
	var td102 = tr1.insertCell();
	var indicatorsNameText = '二级阀值 <input id="limen1-' + str +'" name="limen1-'+ str + '" type="text" size="10"  class="input-text"  >';
	td102.innerHTML = indicatorsNameText;
	// 次数
	var td103 = tr1.insertCell();
	var indicatorsNameText = '次数 <input id="time1-' + str +'" name="time1-'+ str + '" type="text" size="10"   class="input-text">';
	td103.innerHTML = indicatorsNameText;
	
	// 三级阀值
	var td104 = tr1.insertCell();
	var indicatorsNameText = '三级阀值 <input id="limen2-' + str +'" name="limen2-'+ str + '" type="text" size="10"  class="input-text"  >';
	td104.innerHTML = indicatorsNameText;
	
	// 次数
	var td105 = tr1.insertCell();
	var indicatorsNameText = '次数 <input id="time2-' + str +'" name="time2-'+ str + '" type="text" size="10"  class="input-text"  >';
	td105.innerHTML = indicatorsNameText;
	
	//----------------------------tr2
	var tr2 = table.insertRow();
	tr2.id = "alarmLinkNodeTable-tr2-" + str;
	
	// 设备ID号
	var td200 = tr2.insertCell();
	var deviceid = ' &nbsp; <input id="deviceid-' + str +'" name="deviceid-'+ str + '" type="hidden" >';
	td200.innerHTML = deviceid;
	// 比较方式
	var td201 = tr2.insertCell();
	var compare = ' &nbsp; <input id="compare-' + str +'" name="compare-'+ str + '" type="hidden" >';
	td201.innerHTML = compare;	
	// 告警ID号
	var td202 = tr2.insertCell();
//	var alarmid = ' &nbsp; <input id="alarmid-' + str +'" name="alarmid-'+ str + '" type="hidden" >';
	var alarmid = ' &nbsp; <input id="alarmid-' + str +'" name="alarmid" type="hidden" >';
	td202.innerHTML = alarmid;
	
	// 告警ID号
	var td203 = tr2.insertCell();
	var alarmids = ' &nbsp; <input id="alarmids-' + str +'" name="alarmids-'+ str +'" type="hidden" >';
	td203.innerHTML = alarmids;
	
	// 告警ID号
	var td204 = tr2.insertCell();
	var sms0 = ' &nbsp; <input id="sms0-' + str +'" name="sms0-'+ str +'" type="hidden" >';
	td204.innerHTML = sms0;
	
	// 告警ID号
	var td205 = tr2.insertCell();
	var sms1 = ' &nbsp; <input id="sms1-' + str +'" name="sms1-'+ str +'" type="hidden" >';
	td205.innerHTML = sms1;
	// 告警ID号
	var td206 = tr2.insertCell();
	var sms2 = ' &nbsp; <input id="sms2-' + str +'" name="sms2-'+ str +'" type="hidden" >';
	td206.innerHTML = sms2;

	// way0
	var td207 = tr2.insertCell();
	var way0 = ' &nbsp; <input id="way0-' + str +'" name="way0-'+ str +'" type="hidden" >';
	td207.innerHTML = way0;

	// way1
	var td208 = tr2.insertCell();
	var way1 = ' &nbsp; <input id="way1-' + str +'" name="way1-'+ str +'" type="hidden" >';
	td208.innerHTML = way1;
	
	// way2
	var td209 = tr2.insertCell();
	var way2 = ' &nbsp; <input id="way2-' + str +'" name="way2-'+ str +'" type="hidden" >';
	td209.innerHTML = way2;
	
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
											                	<td class="add-content-title"> 告警 >> 告警配置 >> 告警组指标配置 >> 告警组指标添加 </td>
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
																			<td align="right" height="24" width="10%">组名称:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="linktype" name="linktype">
																					<option value="-1">不限</option>
																				</select>
																				&nbsp;&nbsp;<input type="button" value="添  加" onclick="addLinkType()"></input>
																			</td>
																						
																			<td width="50%">&nbsp;
																				<input type="button" id="addRow" name="addRow" value="增加一行" onclick="addAlarmRowNode()" ></input>
																			</td>
																			<td width="50%">&nbsp;
																				<input id="oldIndicators" name="oldIndicators" type="button" onclick="openOldIndicators()" value="原来告警" />
																			</td>
																		</tr>
																		<tr>
																		</tr>
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="alarmLinkNodeTable" width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
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
	<script type="text/javascript">
	
	function init() {
		addLinkTypeToSelect();
	}
	
	// 添加链路组选项
	function addLinkTypeToSelect() {
		var nodeArray = new Array();
		
		<%
			if(!linkTypeList.isEmpty()){
				for(int j = 0 ; j < linkTypeList.size(); j++){
				 //	NodeDTO  nodeDTO = (NodeDTO)allNodeDTOlist.get(j);
				 	AlarmLinkType linkType = (AlarmLinkType)linkTypeList.get(j);
					%>
						nodeArray.push({
							id:"<%=linkType.getId()%>",
							linkname:"<%=linkType.getLinkName()%>"
					});
					<%
				}
			}
			
		%>


	var linktype = document.getElementById("linktype");
	linktype.length = 0;
	linktype.options[0] = new Option("                  ","-1");
	
	var k=1;
	for(var i=0; i<nodeArray.length; i++){
		if(nodeArray[i].id != null || nodeArray[i].id > 0){
			linktype.options[k] = new Option(nodeArray[i].linkname,nodeArray[i].id);
			k++;
		}
	}
	
}
init();
	</script>
</BODY>

</HTML>