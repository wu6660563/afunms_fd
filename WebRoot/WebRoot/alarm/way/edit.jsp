<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.alarm.model.AlarmWay"%>
<%@page import="com.afunms.alarm.model.AlarmWayDetail"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	AlarmWay alarmWay = (AlarmWay)request.getAttribute("alarmWay");
	List alarmWayDetailList = (List)request.getAttribute("alarmWayDetailList");
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

<script type="text/javascript" src="<%=rootPath%>/alarm/way/alarmWayConfig.js"></script>




<script language="JavaScript" type="text/javascript">


	Ext.onReady(function(){  

	 	Ext.get("process").on("click",function(){
	        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
	        //msg.style.display="block";
	        mainForm.action = "<%=rootPath%>/alarmWay.do?action=update";
	        mainForm.submit();
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



//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------





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
	
	initAlarmWayDetail();
	initData();
}

</script>

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* �˷������ڶ��ŷ�ʱ��ϸ��Ϣ
	* ������ /application/resource/js/timeShareConfigdiv.js 
	*/
	//�����û����б�
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// ��ȡ���ŷ�ʱ��ϸ��Ϣ��div
	function initAlarmWayDetail(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		var alarmWayDetail = new Array();
		// ��ȡ�豸�����ķ�ʱ�����б�,
		<%	
			List timeShareConfigList = (List) request.getAttribute("timeShareConfigList");
			if(alarmWayDetailList!=null&&alarmWayDetailList.size()>=0){
			for(int i = 0 ; i < alarmWayDetailList.size(); i++){	        
	           AlarmWayDetail alarmWayDetail = (AlarmWayDetail)alarmWayDetailList.get(i);
	    %>
	            alarmWayDetail.push({
	                id:"<%=alarmWayDetail.getId()%>",
	                category:"<%=alarmWayDetail.getAlarmCategory()%>",
	                dateType:"<%=alarmWayDetail.getDateType()%>",
	                sendTimes:"<%=alarmWayDetail.getSendTimes()%>",
	                sendIntervalTimes:"<%=alarmWayDetail.getSendIntervalTimes()%>",
	                startDate:"<%=alarmWayDetail.getStartDate()%>",
	                endDate:"<%=alarmWayDetail.getEndDate()%>",
	                startTime:"<%=alarmWayDetail.getStartTime()%>",
	                endTime:"<%=alarmWayDetail.getEndTime()%>",
	                userIds:"<%=alarmWayDetail.getUserIds()%>"
	            });
	    <%
	        }
	        }
	    %>
	    for(var i = 0; i< alarmWayDetail.length; i++){
	    	var item = alarmWayDetail[i];
	    	var alarmConfigDivId = "";
	    	if (item.category == "mail"){
	    		alarmConfigDivId = "mailAlarmTable";
	    	} else if (item.category == "sms"){
	    		alarmConfigDivId = "SMSAlarmTable";
	    	}
	    	initAlarmWay(alarmConfigDivId , item);
	    	
	    }
	}
//---- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------------------
</script>

<script type="text/javascript">
	function initData(){
		document.getElementById("description").value="<%=alarmWay.getDescription()%>";
		document.getElementById("isDefault").value="<%=alarmWay.getIsDefault()%>";
		<%
			if("1".equals(alarmWay.getIsMailAlarm())){
			%>
			var isMailAlarm = document.getElementById("isMailAlarm");
			isMailAlarm.checked = true;
			<%
			}
		%>
		<%
			if("1".equals(alarmWay.getIsSMSAlarm())){
			%>
			var isSMSAlarm = document.getElementById("isSMSAlarm");
			isSMSAlarm.checked = true;
			<%
			}
		%>
	}
</script>

</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" name="rowNum" id="rowNum">
		<input type="hidden" name="id" id="id" value="<%=alarmWay.getId()%>">
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
											                	<td class="add-content-title"> �澯 >> �澯���� >> �澯��ʽ���� >> �澯��ʽ��� </td>
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
				        											<table>
																		<tr style="background-color: #ECECEC;">						
																			<td align="right" height="24" width="10%" >����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="name" name="name" value="<%=alarmWay.getName()%>">
																			</td>
																			<td align="right" height="24" width="10%">�Ƿ�Ĭ�Ϸ�ʽ:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="isDefault" name="isDefault" style="width: 100px">
																					<option value="0">��</option>
																					<option value="1">��</option>
																				</select>
																			</td>
																		</tr>	
																		<tr>
																			<td align="right" height="24" width="10%" >����:&nbsp;</td>		
																			<td width="40%">&nbsp;
																				<textarea rows="5" cols="50" id="description" name="description"></textarea>
																			</td>
																			<!-- 
																			<td align="right" height="24" width="10%" >ҳ��澯:&nbsp;</td>		
																			<td width="40%" colspan="3">&nbsp;
																				<input type="checkbox" readonly="readonly" name="isPageAlarm" id="isPageAlarm" value="1">
																			</td> -->
																		</tr>
																		<!-- nielin modify begin (timeConfig div)*/ 2010-09-24 -->
																		<!-- 
																		<tr>
																		 	<td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;�����澯
																		 		&nbsp;&nbsp;&nbsp;
																		 		<input type="checkbox" name="isSoundAlarm" id="isSoundAlarm" value="1">
																		 		<input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('soundAlarmTable','sound')">
																		 	</td>	
																		</tr> 
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="soundAlarmTable">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
																			</td>
																		</tr>-->
																		<!-- nielin modify end */ 2010-09-24 -->
																		<!-- nielin modify begin (timeConfig div)*/ 2010-09-24 -->
																		<tr>
																		 	<td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;�ʼ��澯
																		 		&nbsp;&nbsp;&nbsp;
																		 		<input type="checkbox" name="isMailAlarm" id="isMailAlarm" value="1">
																		 		<input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('mailAlarmTable','mail')">
																		 	</td>	
																		</tr>
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="mailAlarmTable">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
																			</td>
																		</tr>
																		<!-- nielin modify end */ 2010-09-24 -->
																		<!-- nielin modify begin (timeConfig div)*/ 2010-09-24 -->
																		<tr>
																		 	<td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;���Ÿ澯
																		 		&nbsp;&nbsp;&nbsp;
																		 		<input type="checkbox" name="isSMSAlarm" id="isSMSAlarm" value="1">
																		 		<input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('SMSAlarmTable','sms')">
																		 	</td>	
																		</tr>
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="SMSAlarmTable">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
																			</td>
																		</tr>
																		<!-- nielin modify end */ 2010-09-24 -->
																		<!-- nielin modify begin (timeConfig div)*/ 2010-09-24 -->
																		<!-- 
																		<tr>
																		 	<td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;�绰�澯
																		 		&nbsp;&nbsp;&nbsp;
																		 		<input type="checkbox" name="isPhoneAlarm" id="isPhoneAlarm" value="1">
																		 		<input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('phoneAlarmTable','phone')">
																		 	</td>	
																		</tr>
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="phoneAlarmTable">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
																			</td>
																		</tr> -->
																		<!-- nielin modify end */ 2011-02-18 -->
																		<!-- nielin modify begin (timeConfig div)*/ 2011-02-18 -->
																		<!-- 
																		<tr>
																		 	<td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;����澯
																		 		&nbsp;&nbsp;&nbsp;
																		 		<input type="checkbox" name="isDesktopAlarm" id="isDesktopAlarm" value="1">
																		 		<input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('desktopAlarmTable','desktop')">
																		 	</td>	
																		</tr>
																		<tr>
																		 	<td nowrap  colspan="4" >
																		        <div id="formDiv" style="">         
																	                <table>
																                        <tr>
																                            <td align="left">  
																                                <table id="desktopAlarmTable">
																                                </table>
																                            </td>
																                        </tr>
																	                </table>
																	            </div> 
																			</td>
																		</tr> -->
																		<!-- nielin modify end */ 2010-09-24 -->
                                                                        <!-- nielin modify begin (timeConfig div)*/ 2011-02-18 -->
                                                                        <!-- 
                                                                        <tr>
                                                                            <td colspan="4" class="body-data-title" style="text-align: left;">&nbsp;&nbsp;&nbsp;����������̨
                                                                                &nbsp;&nbsp;&nbsp;
                                                                                <input type="checkbox" name="isFvsdAlarm" id="isFvsdAlarm" value="1">
                                                                                <input type="button" id="addTimeConfigRow" value="����һ��" onclick="addRow('fvsdAlarmTable','fvsd')">
                                                                            </td>   
                                                                        </tr>
                                                                        <tr>
                                                                            <td nowrap  colspan="4" >
                                                                                <div id="formDiv" style="">         
                                                                                    <table>
                                                                                        <tr>
                                                                                            <td align="left">  
                                                                                                <table id="fvsdAlarmTable">
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </div> 
                                                                            </td>
                                                                        </tr> -->
                                                                        <!-- nielin modify end */ 2010-09-24 -->
																		<!-- nielin modify end */ 2010-09-24 -->
																		<tr>
																			<TD nowrap colspan="4" align=center>
																			<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																				<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
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
</BODY>
</HTML>