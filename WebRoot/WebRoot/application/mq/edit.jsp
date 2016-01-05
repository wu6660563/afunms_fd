<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String rootPath = request.getContextPath();
  BusinessDao bussdao = new BusinessDao();
  MQConfig vo = (MQConfig)request.getAttribute("vo");
   List allbuss = bussdao.loadAll();  
  String bid = vo.getNetid();
  String id[] = bid.split(",");
  List bidlist = new ArrayList();
  if(id != null &&id.length>0){
	for(int i=0;i<id.length;i++){
		bidlist.add(id[i]);
	}
  }
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
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

<!-- snow add for gatherTime at 2010-5-12 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow������application/resource/js/addTimeConfig.js�� 
</script>
<!-- snow add for gatherTime at 2010-5-12 end -->



<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/mq.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/mq.do?action=delete";
     mainForm.submit();
  }
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     //var chk1 = checkinput("ip_address","ip","����",15,false);
     //var chk2 = checkinput("alias","string","����",30,false);
     //var chk3 = checkinput("community","string","�û���",30,false);
     
     //if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/mq.do?action=update";
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
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/db.do?action=check&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/db.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/db.do?action=cancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/db.do?action=addmanage&id="+node;
	}	
	function clickMenu()
	{
	}
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
	timeShareConfiginit(); // nielin add for timeShareConfig 2010-01-04
}

</script>

<script>
//-- nielin add for timeShareConfig 2010-01-04 end-------------------------------------------------------------
	/*
	* �˷������ڷ�ʱ��ϸ��Ϣ
	* ������ /application/resource/js/timeShareConfigdiv.js 
	*/
	//�����û����б�
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// ��ȡ��ʱ��ϸ��Ϣ��div
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
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
											                	<td class="add-content-title">Ӧ�� >> �м������ >> MQ�༭</td>
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
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="name" maxlength="50" size="20" class="formStyle" value="<%=vo.getName()%>">
							</TD>
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="ipaddress" maxlength="50" size="20" class="formStyle" value="<%=vo.getIpaddress()%>">
							</TD>
						</tr>
						<tr >						
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>���й���������&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="managername" maxlength="50" size="20" class="formStyle" value="<%=vo.getManagername()%>">
							</TD>
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>�˿�&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="portnum" maxlength="50" size="20" class="formStyle" value="<%=vo.getPortnum()%>">
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">					
							<TD nowrap align="right" height="24" width="10%">�Ƿ���&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<select   name="mon_flag"  class="formStyle">
								<%
									if(vo.getMon_flag() == 0){
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
						<!--<tr> 
  							<TD nowrap align="right" height="24" width="10%">���Ž�����&nbsp;</TD>       
							<TD nowrap width="40%">&nbsp;
							<input type="text" readonly="readonly" id="sendmobiles" name="sendmobiles" maxlength="32" size="50" value="<%=vo.getSendmobiles()%>">&nbsp;&nbsp; <input type=button value="���ö��Ž�����" onclick='setReceiver("sendmobiles")'> 
							</td>
 						</tr>-->
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
							<TD nowrap width="40%">&nbsp;
							<input type="text" readonly="readonly" id="sendemail" name="sendemail" maxlength="32" size="50" value="<%=vo.getSendemail()%>">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick='setReceiver("sendemail")'> 
							</td>
 						</tr>  
 						<!--<tr style="background-color: #ECECEC;"> 
  							<TD nowrap align="right" height="24" width="10%">�绰������&nbsp;</TD>       
							<TD nowrap width="40%">&nbsp;
							<input type="text" readonly="readonly" id="sendphone" name="sendphone" maxlength="32" size="50" value="<%=vo.getSendphone()%>">&nbsp;&nbsp;<input type=button value="���õ绰������" onclick='setReceiver("sendphone")'> 
							</td>
 						</tr>-->
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
							<input type="hidden" id="bid" name="bid" value="<%=vo.getNetid()%>">
							</td>
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
							<td nowrap colspan="8">
							
							    <!-- nielin modify begin (SMS div)*/ 2009-01-04-->
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
							   <!-- nielin modify end */ 2009-01-04--->					
							
							</td>
						</tr>
						<tr>
							<td nowrap colspan="8">
							
							    <!-- nielin modify begin (Phone div)*/ 2009-01-04--->
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
							    <!-- nielin modify end */ 2009-01-04--->					
								
							</td>
						</tr>
						
						<tr>
							<!-- nielin add (for timeShareConfig) start 2010-01-04 -->
							<td><input type="hidden" id="rowNum" name="rowNum"></td>
							<!-- nielin add (for timeShareConfig) end 2010-01-04 -->
						</tr>														
						<tr>
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">��Ҫһ��ʱ��,���Ժ�...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="4" align=center>
							<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
								<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
							</TD>	
						</tr>
				</TABLE>	</td>
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
</BODY>
</HTML>
