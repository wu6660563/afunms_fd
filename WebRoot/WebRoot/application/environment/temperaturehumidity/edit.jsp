<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>

<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="com.afunms.application.model.SerialNode"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%  
   String rootPath = request.getContextPath();  
   String menuTable = (String)request.getAttribute("menuTable");
   SerialNode serialNode = (SerialNode)request.getAttribute("serialNode");
   TemperatureHumidityThresholdConfig temperatureHumidityThresholdConfig = (TemperatureHumidityThresholdConfig)request.getAttribute("temperatureHumidityThresholdConfig");
   List allbuss = (List)request.getAttribute("allbuss");
   String bid = serialNode.getBid();
   List bidlist = new ArrayList();
   if(bid !=null ){
       String[] id = bid.split(",");
	   if(id != null &&id.length>0){
		   for(int i=0;i<id.length;i++){
		   		bidlist.add(id[i]);
		   }
		} 
   }
%>


<html>
<head>

<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->

<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>


<script language="javascript">
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
    
     var chk1 = checkinput("name","string","名称",15,false);
     var chk2 = checkinput("address","string","地址",30,false);
     var chk3 = checkinput("description","string","描述",30,false);
     var chk4 = checkinput("serialPortId","string","串行端口",30,false);
     var chk5 = checkinput("baudRate","number","波特率",30,false);
     var chk6 = checkinput("databits","number","数据位",30,false);
     var chk7 = checkinput("stopbits","number","停止位",30,false);
     var chk8 = checkinput("parity","number","奇偶校验位",30,false);
     var chk9 = checkinput("minTemperature","number","最小温度阀值",30,false);
     var chk10 = checkinput("maxTemperature","number","最大温度阀值",30,false);
     var chk11 = checkinput("minHumidity","number","最小湿度阀值",30,false);
     var chk12 = checkinput("maxHumidity","number","最大湿度阀值",30,false);
     if(chk1&&chk2&&chk3&&chk4&&chk5&&chk6&&chk7&&chk8&&chk9&&chk10&&chk11&&chk12)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=edit";
        mainForm.submit();
     }  
      
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
	timeShareConfiginit(); // nielin add for timeShareConfig 2010-01-04
}
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

<script>
//-- nielin add for timeShareConfig 2010-01-04 end-------------------------------------------------------------
	/*
	* 此方法用于分时详细信息
	* 需引入 /application/resource/js/timeShareConfigdiv.js 
	*/
	//接受用户的列表
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// 获取分时详细信息的div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// 获取设备或服务的分时数据列表,
		var timeShareConfigs = new Array();
		var smsConfigs = new Array();
		var phoneConfigs = new Array();
		<%	
			List timeShareConfigList = (List) request.getAttribute("timeShareConfigList");
			if(timeShareConfigList!=null&&timeShareConfigList.size()>=0){
			for(int i = 0 ; i < timeShareConfigList.size(); i++){	        
	            TimeShareConfig timeShareConfig = (TimeShareConfig) timeShareConfigList.get(i);
	            int timeShareConfigId = timeShareConfig.getId();
	            String timeShareType = timeShareConfig.getTimeShareType();
	            String timeShareConfigbeginTime = timeShareConfig.getBeginTime();
	            String timeShareConfigendTime = timeShareConfig.getEndTime();
	            String timeShareConfiguserIds = timeShareConfig.getUserIds();
	            
	    %>
	            timeShareConfigs.push({
	                timeShareConfigId:"<%=timeShareConfigId%>",
	                timeShareType:"<%=timeShareType%>",
	                beginTime:"<%=timeShareConfigbeginTime%>",
	                endTime:"<%=timeShareConfigendTime%>",
	                userIds:"<%=timeShareConfiguserIds%>"
	            });
	    <%
	        }
	        }
	    %>   
	    for(var i = 0; i< timeShareConfigs.length; i++){
	    	var item = timeShareConfigs[i];
	    	if(item.timeShareType=="sms"){
	    		smsConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    	if(item.timeShareType=="phone"){
	    		
	    		phoneConfigs.push({
	                timeShareConfigId:item.timeShareConfig,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    }
		timeShareConfig("smsConfigTable",smsConfigs);
		timeShareConfig("phoneConfigTable",phoneConfigs);
	}
//---- nielin add for timeShareConfig 2010-01-04 end-------------------------------------------------------------------------
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="serialNodeId" value="<%=serialNode.getId()%>">
		<input type=hidden name="serialNodeThresoldId" value="<%=temperatureHumidityThresholdConfig.getId()%>">
		
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
											                	<td class="add-content-title">应用 >> 数据库管理 >> 数据库监视列表</td>
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
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>名称&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="name" name="name" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getName()%>>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>地址&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="address" name="address" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getAddress()%>>
																				</TD>
																			</tr>
																			<tr>					
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>描述&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="description" name="description" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getDescription()%>>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>串行端口&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="serialPortId" name="serialPortId" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getSerialPortId()%>>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">	
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>波特率&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="baudRate" name="baudRate" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getBaudRate()%>>
																				</TD>				
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>数据位&nbsp;</TD>				
																				<TD nowrap width="60%">&nbsp;
																					<input type="text" id="databits" name="databits" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getDatabits()%>>
																				</TD>
																			</tr>
																			<tr>	
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>停止位&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="stopbits" name="stopbits" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getStopbits()%>>
																				</TD>				
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>奇偶校验位&nbsp;</TD>				
																				<TD nowrap width="60%">&nbsp;
																					<input type="text" id="parity" name="parity" maxlength="50" size="20" class="formStyle" value=<%=serialNode.getParity()%>>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">	
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>温度最小阀值&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="minTemperature" name="minTemperature" maxlength="50" size="20" class="formStyle" value=<%=temperatureHumidityThresholdConfig.getMinTemperature()%>>
																				</TD>				
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>温度最大阀值&nbsp;</TD>				
																				<TD nowrap width="60%">&nbsp;
																					<input type="text" id="maxTemperature" name="maxTemperature" maxlength="50" size="20" class="formStyle" value=<%=temperatureHumidityThresholdConfig.getMaxTemperature()%>>
																				</TD>
																			</tr>
																			<tr>	
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>湿度最小阀值&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<input type="text" id="minHumidity" name="minHumidity" maxlength="50" size="20" class="formStyle" value=<%=temperatureHumidityThresholdConfig.getMinHumidity()%>>
																				</TD>				
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>湿度最大阀值&nbsp;</TD>				
																				<TD nowrap width="60%">&nbsp;
																					<input type="text" id="maxHumidity" name="maxHumidity" maxlength="50" size="20" class="formStyle" value=<%=temperatureHumidityThresholdConfig.getMaxHumidity()%>>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;"> 
													  							<TD nowrap align="right" height="24" width="10%">邮件接收人&nbsp;</TD>       
																				<TD nowrap width="40%" colspan=3>&nbsp;
																				<input type="text" readonly="readonly" name="sendemail" id="sendemail" maxlength="32" size="50" value="<%=serialNode.getSendMail()%>">&nbsp;&nbsp;<input type=button value="设置邮件接收人" onclick='setReceiver("sendemail")'>
																				</td>
													 						</tr> 
																			<tr>					
																				<TD nowrap align="right" height="24" width="10%">是否监控&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;
																					<select   name="monflag"  class="formStyle">
																					<%
																						if("0".equals(serialNode.getMonflag())){
																					%>
																						<option value=0 selected>否</option>
																						<option value=1>是</option>
																					<%
																						}else{
																					%>
																						<option value=0 >否</option>
																						<option value=1 selected>是</option>								
																					<%
																						}
																					%>
																					</select>
																				</TD>															
																			</tr>
																			<tr style="background-color: #ECECEC;">	
																				<TD nowrap align="right" height="24">所属业务&nbsp;</TD>
																				<td nowrap colspan="3" height="1">
																				<% 
																					String bussName = "";
																					if( allbuss.size()>0){
																						for(int i=0;i<allbuss.size();i++){
																							Business buss = (Business)allbuss.get(i);
																							
																							if(bidlist.contains(buss.getId()+"")){
																								bussName = bussName + ',' + buss.getName();
																							}
																							
																		 				}
																					}
																				%>   
																				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bussName%>">&nbsp;&nbsp;<input type=button value="设置所属业务" onclick='setBid("bidtext" , "bid");'>
																				<input type="hidden" id="bid" name="bid" value="<%=serialNode.getBid()%>">
																				</td>
																			</tr>
																			<tr>	
																			<td nowrap colspan="8">
																				
																				    <!-- nielin modify begin (SMS div)*/ 2009-01-04-->
																				        <div id="formDiv" style="">         
																			                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																		                        <tr>
																		                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;短信发送详细配置</td>
																		                        </tr>
																		                        <tr>
																		                            <td align="center">           
																		                                <table id="smsConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
																	                                        <tr>
																	                                            <td colspan="0" height="50" align="center"> 
																	                                                <span  onClick='addRow("smsConfigTable","sms");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
																	                                            </td>
																	                                        </tr>
																		                                </table>
																		                            </td>
																		                        </tr>
																			                </table>
																			            </div> 
																				      <!-- nielin modify end */ 2009-01-04--->					
																				
																				</td>
																			</tr>
																			<tr>
																				<td nowrap colspan="8">
																				
																				    <!-- nielin modify begin (Phone div)*/ 2009-01-04--->
																				        <div id="formDiv" style="">         
																			                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																		                        <tr>
																		                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;电话接收详细配置</td>
																		                        </tr>
																		                        <tr>
																		                            <td align="center">           
																		                                <table id="phoneConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
																	                                        <tr>
																	                                            <td colspan="0" height="50" align="center"> 
																	                                                <span  onClick='addRow("phoneConfigTable","phone");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
																	                                            </td>
																	                                        </tr>
																		                                </table>
																		                            </td>
																		                        </tr>
																			                </table>
																			            </div> 
																				      <!-- nielin modify end */ 2009-01-04--->					
																					
																				</td>
																			</tr>
																			
																			<tr>
																				<!-- nielin add (for timeShareConfig) start 2010-01-04 -->
																				<td><input type="hidden" id="rowNum" name="rowNum"></td>
																				<!-- nielin add (for timeShareConfig) end 2010-01-04 -->
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

unionSelect();

</script>

</HTML>