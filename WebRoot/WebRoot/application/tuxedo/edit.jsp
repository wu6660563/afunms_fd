<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.application.model.TuxedoConfig"%>
<%@page import="java.util.ArrayList"%>

<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List allbuss = (List)request.getAttribute("allbuss");
  
  TuxedoConfig tuxedoConfig = (TuxedoConfig)request.getAttribute("tuxedoConfig");
  
  if(tuxedoConfig==null){
  	tuxedoConfig = new TuxedoConfig();
  }
  
  	String bid = tuxedoConfig.getBid();
   	if(bid == null)bid="";
   	String id[] = bid.split(",");
   	List bidlist = new ArrayList();
   	if(id != null &&id.length>0){
	for(int i=0;i<id.length;i++){
	    bidlist.add(id[i]);
	}
	}
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

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->




<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     var chk1 = checkinput("name","string","����",30,false);
     var chk2 = checkinput("ipaddress","ip","IP��ַ",30,false);
     
     if(chk1&&chk2)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/tuxedo.do?action=edit";
        mainForm.submit();
     }  
       // mainForm.submit();
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
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
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// ��ȡ�豸�����ķ�ʱ�����б�,
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
	                timeShareConfigId:item.timeShareConfigId,
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
//---- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------------------
</script>


</head>
<body id="body" class="body" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">������Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->

	<form id="mainForm" method="post" name="mainForm">
		<input id="id" name="id" type="hidden" value="<%=tuxedoConfig.getId()%>">
		
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
											                	<td class="add-content-title"> Ӧ�� &gt;&gt; �м������ &gt;&gt; Tuxedo �༭</td>
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
																			<td align="right" height="24" width="10%">����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="name" name="name" value="<%=tuxedoConfig.getName()%>">
																				<font color="red">&nbsp;���� ����*</font>
																			</td>
																			<td align="right" height="24" width="10%">IP ��ַ:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="ipaddress" name="ipaddress" value="<%=tuxedoConfig.getIpAddress()%>">
																				<font color="red">&nbsp;���� Tuxedo ��������ַ*</font>
																			</td>
																		</tr>	
																		<tr>
																			<td align="right" height="24" width="10%">�Ƿ���:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select name="mon_flag" style="width: 132px">
																					<option value="0">��</option>
																					<option value="1">��</option>
																				</select>
																			</td>
																		</tr>
																		<!--<tr style="background-color: #ECECEC;"> 
												  							<TD nowrap align="right" height="24" width="10%">���Ž�����&nbsp;</TD>       
																			<TD nowrap width="40%"  colspan=3>&nbsp;
																			<input type="text" readonly="readonly" id="sendmobiles" name="sendmobiles" maxlength="32" size="50" value="">&nbsp;&nbsp; <input type=button value="���ö��Ž�����" onclick='setReceiver("sendmobiles");'> 
																			</td>
											 							</tr>-->
												    					<tr style="background-color: #ECECEC;"> 
												  							<TD nowrap align="right" height="24" width="10%">�ʼ�������&nbsp;</TD>       
																			<TD nowrap width="40%"  colspan=3>&nbsp;
																			<input type="text" readonly="readonly" id="sendemail" name="sendemail" maxlength="32" size="50" value="<%=tuxedoConfig.getSendemail()%>">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick='setReceiver("sendemail");'> 
																			</td>
												 						</tr> 
												 						<!--<tr> 
												  							<TD nowrap align="right" height="24" width="10%">�绰������&nbsp;</TD>       
																			<TD nowrap width="40%"  colspan=3>&nbsp;
																			<input type="text" readonly="readonly" id="sendphone" name="sendphone" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="���õ绰������" onclick='setReceiver("sendphone");'> 
																			</td>
												 						</tr> -->
																		<tr style="background-color: #ECECEC;">	
																			<TD nowrap align="right" height="24">����ҵ��&nbsp;</TD>
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
																			<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bussName%>">&nbsp;&nbsp;<input type=button value="��������ҵ��" onclick='setBid("bidtext" , "bid");'>
																			<input type="hidden" id="bid" name="bid" value="<%=tuxedoConfig.getBid()%>">
																			</td>
																		</tr>
																		<tr>	
																			<td nowrap colspan="8">
																			
																			    <!-- nielin modify begin (SMS div)*/ 2009-01-03-->
																			        <div id="formDiv" style="">         
																		                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																	                        <tr>
																	                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;���ŷ�����ϸ����</td>
																	                        </tr>
																	                        <tr>
																	                            <td align="center">           
																	                                <table id="smsConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
																                                        <tr>
																                                            <td colspan="0" height="50" align="center"> 
																                                                <span  onClick='addRow("smsConfigTable","sms");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
																                                            </td>
																                                        </tr>
																	                                </table>
																	                            </td>
																	                        </tr>
																		                </table>
																		            </div> 
																			      <!-- nielin modify end */ 2009-01-03--->					
																			
																			</td>
																		</tr>
																		<tr>
																			<td nowrap colspan="8">
																			
																			    <!-- nielin modify begin (Phone div)*/ 2009-01-03--->
																			        <div id="formDiv" style="">         
																		                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																	                        <tr>
																	                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;�绰������ϸ����</td>
																	                        </tr>
																	                        <tr>
																	                            <td align="center">           
																	                                <table id="phoneConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
																                                        <tr>
																                                            <td colspan="0" height="50" align="center"> 
																                                                <span  onClick='addRow("phoneConfigTable","phone");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
																                                            </td>
																                                        </tr>
																	                                </table>
																	                            </td>
																	                        </tr>
																		                </table>
																		            </div> 
																			      <!-- nielin modify end */ 2009-01-03--->					
																				
																			</td>
																		</tr>
																	
																		<tr>
																			<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																			<td><input type="hidden" id="rowNum" name="rowNum"></td>
																			<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
																		
																		</tr>
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