<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>

<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  DBVo vo = (DBVo)request.getAttribute("vo");
  BusinessDao bussdao = new BusinessDao();
  List allbuss = bussdao.loadAll(); 
  DBTypeDao typedao = new DBTypeDao();
  List typelist =   typedao.loadAll();
  String bid = vo.getBid();
  String id[] = bid.split(",");
  List bidlist = new ArrayList();
  if(id != null &&id.length>0){
	for(int i=0;i<id.length;i++){
		bidlist.add(id[i]);
	}
  } 
  String _flag = (String)request.getAttribute("flag"); 
  String sid = String.valueOf(request.getAttribute("sid"));
%>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

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

  function toUpdate()
  {
    if(document.getElementById("db_nameInput").style.display=="none"){
  		var chk1 = checkinput("alias","string","����",30,false);
    	var chk2 = checkinput("ip_address","string","IP��ַ",15,false);
     	var chk4 = checkinput("password","string","����",30,false);
   		var chk3 = true;
  	}else{
  		var chk1 = checkinput("alias","string","����",30,false);
    	var chk2 = checkinput("ip_address","string","IP��ַ",15,false);
     	var chk3 = checkinput("db_name","string","���ݿ���",30,false);
     	var chk4 = checkinput("password","string","����",30,false);
  	}
     
          
     if(chk1&&chk2&&chk3&&chk4)
     {
        mainForm.action = "<%=rootPath%>/db.do?action=update";
        mainForm.submit();
     }  
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
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="id" value="<%=vo.getId()%>">
		<input type="hidden" id="category" name="category" value="">
		<input type="hidden" id="flag" name="flag" value="<%=_flag%>">
		
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
											                	<td class="add-content-title">Ӧ�� >> ���ݿ���� >> ���ݿ�༭</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1"
						width="100%">
					<TBODY>
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>
			<TD nowrap width="40%">&nbsp;
				<select id="type" size=1 name='dbtype' style='width:125px;' onchange="unionSelect();">
				<%
					if(typelist != null && typelist.size()>0){
						for(int k=0;k<typelist.size();k++){
							DBTypeVo typevo = (DBTypeVo)typelist.get(k);
							String flag = "";
							if(typevo.getId() == vo.getDbtype()) flag="selected";
				%>
			<option value=<%=typevo.getId()%> <%=flag%>><%=typevo.getDbtype()%></option>
			<%
						}
					}
			%>
			</select></TD>				
																		
			<TD nowrap align="right" height="24">����&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="alias" size="20" class="formStyle" value="<%=vo.getAlias()%>"><font color='red' id="nameFont">���������ݿ������������</font></TD>						
			</tr>
			<tr>						
			<TD nowrap align="right" height="24" width="10%">IP��ַ&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="ip_address" size="20" class="formStyle" value="<%=vo.getIpAddress()%>"><font color='red'>*</font></TD>												
			<TD nowrap id="db_nameTD" align="right" height="24">���ݿ���&nbsp;</TD>				
			<TD nowrap id="db_nameInput" width="40%">&nbsp;<input type="text" name="db_name" size="20" class="formStyle" name="db_name" value="<%=vo.getDbName()%>"><font color='red'>*</font></TD>						
			</tr>			
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="10%">�û���&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="user" size="20" class="formStyle" value="<%=vo.getUser()%>"><font color='red'>*</font></TD>				
			<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="password" name="password" size="20" class="formStyle" value="<%=vo.getPassword()%>"><font color='red'>*</font></TD>				
	        	</tr>
			<tr>
		    	<TD nowrap align="right" height="24">�˿�&nbsp;</TD>				
            		<TD nowrap>&nbsp;<input type="text" name="port" size="20" class="formStyle" value="<%=vo.getPort()%>"></TD>
			<TD nowrap align="right" height="24" width="10%">���ݿ�Ӧ��&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="dbuse" size="20" class="formStyle" value="<%=vo.getDbuse()%>"></TD>
			</tr>
			<tr style="background-color: #ECECEC;">	
		    	<TD nowrap align="right" height="24">�Ƿ����&nbsp;</TD>				
            		<TD nowrap >&nbsp;
            			<select name="managed">
            			<%
            				if(vo.getManaged() == 0){
            			%>
            				<option value="1" >��</option>
            				<option value="0" selected>��</option>
            			<%
            				}else{
            			%>
            				<option value="1" selected>��</option>
            				<option value="0">��</option>            			
            			<%
            				}
            			%>            			
            			</select>
            		</TD>
		    	<TD nowrap align="right" height="24">�ɼ���ʽ&nbsp;</TD>				
            		<TD nowrap >&nbsp;
            			<select name="collecttype">
            			<%
            				if(vo.getCollecttype() == 1){
            			%>
            				<option value="1" selected>JDBC</option>
            			<%
            				}else{
            			%>
            				<option value="1" >JDBC</option>
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
			<!--<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">�绰������&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text id="sendphone" name="sendphone" size="50" maxlength="32" value="<%=vo.getSendphone()%>">&nbsp;&nbsp;<input type=button value="���õ绰������" onclick='setReceiver("sendphone")'>
				</td>
			</tr>-->			
			<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">������Ԫ��&nbsp;</TD>
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
				<input type="hidden" id="bid" name="bid" value="<%=vo.getBid()%>">
				<input type="hidden" id="sid" name="sid" value="<%=sid%>">
				</td>
			</tr>
			
				<tr>
					<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
					<td><input type="hidden" id="rowNum" name="rowNum"></td>
					<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
				
				</tr>
						<tr>
							<TD nowrap colspan="4" align=center>
								<br>
								<input type="button" value="����" style="width:50" class="formStylebutton" onclick="toUpdate()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="����" onclick="javascript:history.back(1)">
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
		
	}else if(type.value == 2){
		categoryvalue = 54;
		
	}else if(type.value == 4){
		categoryvalue = 52;
		
	}else if(type.value == 5){
		categoryvalue = 59;
		
	}else if(type.value == 6){
		categoryvalue = 55;
	}else if(type.value == 7){
		categoryvalue = 60;
	}
	
	category.value = categoryvalue;
	
	
}

unionSelect();

</script>

</HTML>