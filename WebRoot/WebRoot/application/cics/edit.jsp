<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>

<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%  
   String rootPath = request.getContextPath();  
   BusinessDao bussdao = new BusinessDao();
   CicsConfig vo = (CicsConfig)request.getAttribute("vo");
   List allbuss = bussdao.loadAll();  
   String bid = vo.getNetid();
   String id[] = bid.split(",");
   List bidlist = new ArrayList();
   if(id != null &&id.length>0){
	   for(int i=0;i<id.length;i++){
		   bidlist.add(id[i]);
	   }
   }  
  String menuTable = (String)request.getAttribute("menuTable");%>
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
<!-- snow add for gatherTime at 2010-5-20 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow������application/resource/js/addTimeConfig.js�� 
</script>
<!-- snow add for gatherTime at 2010-5-20 end -->

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
  
     //var chk1 = checkinput("alias","string","����",15,false);
     //var chk2 = checkinput("str","string","·��",30,false);
     //var chk3 = checkinput("ipaddress","ip","IP��ַ",30,false);
     //var chk4 = checkinput("timeout","number","��ʱ",30,false);
     
     //if(chk1&&chk2&&chk3&&chk4)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cics.do?action=update";
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
      <form name="mainForm" method="post">
               <input type=hidden name="id" value="<%=vo.getId()%>">
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
											                	<td class="add-content-title">Ӧ�� >> �м������ >> CICS����༭</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">��������&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="region_name" maxlength="50" size="20" class="formStyle" value=<%=vo.getRegion_name()%>>
							</TD>
						</tr>
						<tr >						
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="alias" maxlength="50" size="20" class="formStyle" value=<%=vo.getAlias()%>>
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">					
							<TD nowrap align="right" height="24" width="10%">������IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="ipaddress" maxlength="50" size="50" class="formStyle" value="<%=vo.getIpaddress()%>">
							</TD>
						</tr>
						<tr >						
							<TD nowrap align="right" height="24" width="10%">�����˿�&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="port_listener" maxlength="50" size="20" class="formStyle" value="<%=vo.getPort_listener()%>">
							</TD>		
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">GateWay&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="gateway" maxlength="50" size="20" class="formStyle" value="<%=vo.getGateway()%>">
							</TD>		
						</tr>
						<tr >						
							<TD nowrap align="right" height="24" width="10%">���ӳ�ʱ(��)&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="conn_timeout" maxlength="50" size="20" class="formStyle" value="<%=vo.getConn_timeout()%>">
							</TD>		
						</tr>
						<tr style="background-color: #ECECEC;">					
							<TD nowrap align="right" height="24" width="10%">����Э��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select   name="network_protocol"  class="formStyle">
									<%
									if("TCP/IP".equals(vo.getNetwork_protocol())){
								%>
									<option value="TCP/IP" selected>TCP/IP</option>
									<option value="SNA">SNA</option>
								<%
									} else {
								%>
									<option value="TCP/IP">TCP/IP</option>
									<option value="SNA" selected>SNA</option>							
								<%
									}
								%>
								</select>
							</TD>															
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">�Ƿ���&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select   name="flag"  class="formStyle">
								<%
									if(vo.getFlag() == 0){
								%>
									<option value=0 selected>��</option>
									<option value=1>��</option>
								<%
									}else{
								%>
									<option value=0 >��</option>
									<option value=1 selected>��</option>								
								<%
									}
								%>
								</select>
							</TD>															
						</tr>
                        <!-- snow modify begin */ 2010-05-18 -->
						<tr>												
							<TD nowrap align="right" height="24" width="10%">��Ӧ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='supperid' style="width:260px;">
									<option value='0' ></option>
									<c:forEach items="${allSupper}" var="al">
										<c:choose>
											<c:when test="${vo.supperid eq al.su_id }">
												<option value='${al.su_id }' selected>${al.su_name }��${al.su_dept}��</option>
											</c:when>
											<c:otherwise>
						      							<option value='${al.su_id }'>${al.su_name }��${al.su_dept}��</option>
											</c:otherwise>
										</c:choose>
						    					</c:forEach>
						      			</select>
						      		</TD>	
						      		<TD nowrap align="right" height="24" width="10%"></TD>				
							<TD nowrap width="40%">&nbsp;
						      		</TD>																					
						</tr>						
						<!-- snow modify end -->
    						<tr style="background-color: #ECECEC;"> 
  							<TD nowrap align="right" height="24" width="10%">�ʼ�������&nbsp;</TD>       
							<TD nowrap width="40%" colspan=3>&nbsp;
							<input type="text" name="sendemail" maxlength="32" size="50" value="<%=vo.getSendemail()%>">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setCicsEmail&id=<%=vo.getId()%>')">
							</td>
 						</tr>						 											
                        <tr>						
							<TD nowrap align="right" height="24" width="10%">����ҵ��&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        					<tbody>  
                        						<% if( allbuss.size()>0){
           												for(int i=0;i<allbuss.size();i++){
           													Business buss = (Business)allbuss.get(i);
           													String checkflag = "";
           													if(bidlist.contains(buss.getId()+""))checkflag="checked";
           													
                        									%>                  										                        								
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=buss.getId()%>" <%=checkflag%>>&nbsp;<%=buss.getName()%></td>
                    										</tr>  
                    										<%
                    											}
                    										}
                    										%>                 										                 										                      								
            							</tbody>
            							</table>
																</TD>																			
															</tr>
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
															</tr>
															
															<!-- snow modify begin (timeConfig div)*/ 2010-05-20 -->
															<tr>
															 	<td nowrap align="right" width="10%" style="height:35px;">��Ϣ�ɼ�ʱ��&nbsp;</td>		
															 	<td nowrap  colspan="3">
															        <div id="formDiv" style="">         
														                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
													                        <tr>
													                            <td align="left">  
														                            <br>
													                                <table id="timeConfigTable" style="width:60%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
												                                        <c:forEach items="${timeGratherConfigList}" var="tg" varStatus="tgindex">
												                                        	<script type="text/javascript">
												                                        		$(document).ready(function(){
												                                        			$("#addTimeConfigRow").click();
												                                        			selectedAll(${tg.startHour},${tg.startMin},${tg.endHour},${tg.endMin},${tgindex.index});
												                                        		});
												                                        	</script>
												                                        </c:forEach>
												                                        <tr>
												                                            <td colspan="7" height="50" align="center"> 
												                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
												                                            </td>
												                                        </tr>
													                                </table>
													                            </td>
													                        </tr>
														                </table>
														            </div> 
																</td>
															</tr>
															<!-- snow modify end */ 2010-05-20 -->
															
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
																</TD>	
															</tr>	
							</TBODY>
						</TABLE>
										 							
										 							
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