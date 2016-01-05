<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>

<%  
   HostNode vo = (HostNode)request.getAttribute("vo");
   String rootPath = request.getContextPath();  
       BusinessDao bussdao = new BusinessDao();
       List allbuss = bussdao.loadAll();  
   	String bid = vo.getBid();
   	if(bid == null)bid="";
   	String id[] = bid.split(",");
   	List bidlist = new ArrayList();
   	if(id != null &&id.length>0){
	for(int i=0;i<id.length;i++){
	    bidlist.add(id[i]);
	}
  }            
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<% 
String str0="";
String str1="";
String str2="";
String str3="";
String str4="";
String str5="";
String str6="";
String str7="";
List nodeflist = (List)request.getAttribute("nodefacilitys");

if(nodeflist != null && nodeflist.size()>0){
	for(int i=0;i<nodeflist.size();i++){
		if("0".equals(nodeflist.get(i))) {
			str0="checked";
		}else if("1".equals(nodeflist.get(i))) {
			str1="checked";
		}else if("2".equals(nodeflist.get(i))) {
			str2="checked";	
		}else if("3".equals(nodeflist.get(i))) {
			str3="checked";
		}else if("4".equals(nodeflist.get(i))) {
			str4="checked";	
		}else if("5".equals(nodeflist.get(i))) {
			str5="checked";
		}else if("6".equals(nodeflist.get(i))) {
			str6="checked";	
		}else if("7".equals(nodeflist.get(i))) {
			str7="checked";								
		}
	}
}
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->


<script type="text/javascript" src="<%=rootPath%>/include/validation.js"></script>

<script language="javascript">
  function toAdd()
  {
     var chk = checkinput("alias","string","机器名",30,false);
     if(chk)
     {
        mainForm.action = "<%=rootPath%>/network.do?action=update";
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
            function init() {
               
                //define('ipaddress','string','<%=vo.getIpAddress()%>', 7, 50);
                
               
                /* lijun modify begin */
                 /*
                var smsConfigDiv=document.getElementById("smsConfigDiv");
                smsConfigDiv.style.display="";
                var equipmentid=<%=vo.getId()%>;
                alert(equipmentid);
                if(equipmentid!=null){
                    smsConfigDiv.style.display="";
                }
               */
                /* lijun modify end */
                
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=vo.getId()%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
	
	<%=menuTable%>				
	</td>
		<td bgcolor="#cedefa" align="left" valign=top>
			<table width="830" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top">
				 <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr>
							<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=4>&nbsp;&nbsp;设备维护 >> 设备列表 >> 编辑</td>
							
						</tr>
						<tr>
							<td nowrap colspan="4" height="25" ></td>
						</tr>
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="10%">机器名&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;
			<input type="text" name="alias" maxlength="50" size="20" class="formStyle" value="<%=vo.getAlias()%>">
			</TD>
			<TD nowrap align="right" height="24" width="10%">是否监视&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;
			<select name="managed">
			<%
				if(vo.isManaged()){
			%>
				<option value="1" selected>是</option> 
				<option value="0">否</option>
			<%
				} else {
			%>
				<option value="1" >是</option> 
				<option value="0" selected>否</option>
			<%
				}				
			%>
			</select>
			</TD>																			
			</tr>
			<!--<tr>
				<TD nowrap align="right" height="24">短信接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendmobiles" name="sendmobiles" size="50" maxlength="32" value="<%=vo.getSendmobiles()%>">&nbsp;&nbsp;<input type=button value="设置短信接收人" onclick='setReceiver("sendmobiles");'>
				</td>
			</tr>-->
			<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">邮件接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendemail" name="sendemail" size="50" maxlength="32" value="<%=vo.getSendemail()%>">&nbsp;&nbsp;<input type=button value="设置邮件接收人" onclick='setReceiver("sendemail");'>
				</td>
			</tr>
			<!--<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">电话接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendphone" name="sendphone" size="50" maxlength="32" value="<%=vo.getSendemail()%>">&nbsp;&nbsp;<input type=button value="电话接收人" onclick='setReceiver("sendphone");'>
				</td>
			</tr>-->
			
			<tr>						
			<TD nowrap align="right" height="24" width="10%">所属业务&nbsp;</TD>				
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
            <TD nowrap align="right" height="24" width="10%">SysLog过滤&nbsp;</TD>	
           <TD nowrap width="40%" colspan=3>&nbsp;
            										
            						<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        			<tbody>
                    					<tr align="left" valign="center" bgcolor=#ECECEC> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="0" <%=str0%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;紧急</td>
                    						<td height="28" align="right" width=10%%><INPUT type="checkbox" class=noborder value="1" <%=str1%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;报警</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="2" <%=str2%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;关键</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="3" <%=str3%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;错误</td>
                    					</tr>
                    					<tr align="left" valign="center" bgcolor=#ffffff> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="4" <%=str4%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;警告</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="5" <%=str5%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;通知</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="6" <%=str6%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;提示</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="7" <%=str7%> name="fcheckbox"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;调试</td>
                    					</tr>
            					</tbody>
            					</table>
            					<br>
			</TD>																			
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
						<br>
						<input type="button" value="保存" style="width:50" class="formStylebutton" onclick="toAdd()" align=center>&nbsp;&nbsp;
						<input type=reset class="formStylebutton" style="width:50" value="返回" onclick="javascript:history.back(1)" align=center>
					</TD>	
				</tr>
			</TBODY>
				</TABLE>		
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>