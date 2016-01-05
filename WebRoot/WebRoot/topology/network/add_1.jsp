<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%  
   String rootPath = request.getContextPath(); 
   List allbuss = (List)request.getAttribute("allbuss");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->


<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script language="javascript">
  function toAdd()
  {
     var chk1 = checkinput("ip_address","ip","IP地址",15,false);
     var chk2 = checkinput("alias","string","机器名",30,false);
     var chk3 = checkinput("community","string","共同体",30,false);
     
     if(chk1&&chk2&&chk3)
     {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/network.do?action=add";
        mainForm.submit();
     }
  }
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     var chk1 = checkinput("ip_address","ip","IP地址",15,false);
     var chk2 = checkinput("alias","string","机器名",30,false);
     var chk3 = checkinput("community","string","共同体",30,false);
     
     if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/network.do?action=add";
        mainForm.submit();
     }  
       // mainForm.submit();
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
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

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* 此方法用于短信分时详细信息
	* 需引入 /application/resource/js/timeShareConfigdiv.js 
	*/
	//接受用户的列表
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// 获取短信分时详细信息的div
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


<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=left>
	
	<%=menuTable%>
            				
	</td>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;设备维护 >> 添加设备</td>
				</tr>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
				<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">IP地址&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="ip_address" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>						
							<TD nowrap align="right" height="24" width="10%">机器名&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="alias" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>															
						</tr>
						<tr>						
							<TD nowrap align="right" height="24" width="10%">读共同体&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="community" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>						
							<TD nowrap align="right" height="24" width="10%">写共同体&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="writecommunity" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
						<tr style="background-color: #ECECEC;">												
							<TD nowrap align="right" height="24" width="10%">设备类型&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='type' style='width:100px;'>
            								<option value='1' selected>路由器</option>
            								<option value='2'>路由交换机</option>
            								<option value='3'>交换机</option>
            								<option value='4'>服务器</option>
            								<option value='8'>防火墙</option>
            							</select>&nbsp;</TD>
							<TD nowrap align="right" height="24" width="10%">采集方式&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='collecttype' style='width:100px;'>
            								<option value='1' selected>SNMP</option>
            								<option value='2'>脚本</option>
            								<option value='3'>Ping</option>
            							</select>&nbsp;</TD>            																						
						</tr>
						<tr >												
							<TD nowrap align="right" height="24" width="10%">操作系统&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='ostype' style='width:100px;'>
									<option value='0' selected> </option>
            								<option value='1'>Cisco</option>
            								<option value='2'>H3C</option>
            								<option value='3'>Entrasys</option>
            								<option value='4'>Radware</option>
            								<option value='5'>Windows</option>
            								<option value='6'>AIX</option>
            								<option value='7'>HP UNIX</option>
            								<option value='8'>SUN Solaris</option>
            								<option value='9'>Linux</option>
            								<option value='10'>MaiPu</option>
            								<option value='11'>RedGiant</option>
            								
            								
            							</select>&nbsp;</TD>
							<TD nowrap align="right" height="24" width="10%">是否监视&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='manage' style='width:100px;'>
            								<option value='1' selected>是</option>
            								<option value='0'>否</option>
            							</select>&nbsp;</TD>            																						
						</tr>												
						<tr>
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">需要一点时间,请稍候...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
			<!--<tr>
				<TD nowrap align="right" height="24">短信接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendmobiles" name="sendmobiles" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置短信接收人" onclick='setReceiver("sendmobiles");'>
				</td>
			</tr>-->
			<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">邮件接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendemail" name="sendemail" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置邮件接收人" onclick='setReceiver("sendemail");'>
				</td>
			</tr>
			<!--<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">电话接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendphone" name="sendphone" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置电话接收人" onclick='setReceiver("sendphone");'>
				</td>
			</tr>-->
			
			<tr>						
			<TD nowrap align="right" height="24" width="10%">所属业务&nbsp;</TD>				
			<TD nowrap width="40%" colspan=3>&nbsp;
											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center">
                        								<tbody>  
                        									<% if( allbuss.size()>0){
           												for(int i=0;i<allbuss.size();i++){
           													Business buss = (Business)allbuss.get(i);
                        									%>                  										                        								
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<INPUT type="checkbox" class=noborder name="bid" id="bid" value="<%=buss.getId() %>">&nbsp;<%=buss.getName()%></td>
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
            						<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center">
                        			<tbody>
							<tr align="left" valign="center" bgcolor=#ECECEC> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="0"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;紧急</td>
                    						<td height="28" align="right" width=10%%><INPUT type="checkbox" class=noborder name="fcheckbox" value="1"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;报警</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="2"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;关键</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="3"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;错误</td>
                    					</tr>
                    					<tr align="left" valign="center" bgcolor=#ffffff> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="4"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;警告</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="5"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;通知</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="6"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;提示</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="7"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;调试</td>
                    					</tr>
            					</tbody>
            					</table>
            					<br>
			</TD>																			
			</tr>	
			<tr>	
				<td nowrap colspan="8">
					
					    <!-- nielin modify begin (SMS div)*/ 2009-01-03-->
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
					      <!-- nielin modify end */ 2009-01-03--->					
					
					</td>
				</tr>
				<tr>
					<td nowrap colspan="8">
					
					    <!-- nielin modify begin (Phone div)*/ 2009-01-03--->
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
						<br>
						<input type="button" value="保存" style="width:50" class="formStylebutton" id="process" onclick="#" align=center>&nbsp;&nbsp;
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