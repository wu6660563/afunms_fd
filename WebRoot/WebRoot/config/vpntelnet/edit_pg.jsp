<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>

<%
	Huaweitelnetconf vo = (Huaweitelnetconf)request.getAttribute("vo");
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
	
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="gb2312" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="gb2312"></script>

		<!--nielin add for timeShareConfig at 2010-01-04 start-->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>
		<!--nielin add for timeShareConfig at 2010-01-04 end-->

<script language="JavaScript" type="text/javascript">

 Ext.onReady(function()
{  
setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("vpnadd").on("click",function(){
   	var chk1 = checkinput("user","string","用户",50,false);
   	var chk2 = checkinput("port","string","端口",50,false);
    var chk6 = checkinput("ipaddress","string","ip地址",50,false);  
     
      if(chk1&&chk6)
     {      
          	Ext.MessageBox.wait('数据加载中，请稍后.. ');
        	mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=update";
        	mainForm.submit();
        	window.close();
     }
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
	timeShareConfiginit();
	 // nielin add for time-sharing at 2010-01-04
}
</script>
	<script type="text/javascript">
			function showup(){
				window.open("<%=rootPath%>/config/vpntelnet/netip.jsp");
			}
		</script>


</head>
<body id="body" class="body" onload="initmenu();">



	<form id="mainForm" method="post" name="mainForm">
		
		<table id="body-container" class="body-container">
			<tr>
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
											                	<td class="add-content-title">远程登录设置 修改</td>
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
																
																	<tr style="background-color: #ECECEC;">
																	<input type="hidden"  name="id" value="<%=vo.getId()%>">
						    <TD nowrap align="right" height="24" width="10%">用户&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="user" size="40"  class="formStyle" value="<%=vo.getUser() %>"><font color="red">&nbsp;*</font></TD>
							</tr>
							<tr>
							<TD nowrap align="right" height="24">密码&nbsp;</TD>				
							<TD nowrap>&nbsp;<input type="text" name="password" size="40" class="formStyle" value="<%=vo.getPassword() %>"><font color="red">&nbsp;*</TD>	
						</tr>
						<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24">su用户&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input name="suuser" type="text" size="40"  class="formStyle" value="<%=vo.getSuuser() %>"><font color="red">&nbsp;* </font>
							</TD>						
							</tr>
							<tr>
							<TD nowrap align="right" height="24">su用户密码&nbsp;</TD>
							<TD nowrap>&nbsp;<input type="text" name="supassword" size="40"  class="formStyle" value="<%=vo.getSupassword() %>">	<font color="red">&nbsp;* </font>													</TD>	
						</tr>
						
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">ip地址&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="ipaddress" type="text" size="20" class="formStyle" value="<%=vo.getIpaddress() %>" readonly="readonly"></TD>								
						</tr>
						<tr>	
							<td align="right" height="20">端口&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="port" type="text" size="16" class="formStyle" value="<%=vo.getPort() %>"><font color="red">&nbsp;* </font></td>
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">提示符号&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="defaultpromtp" type="text" size="40" class="formStyle" value="<%=vo.getDefaultpromtp()%>"><font color="red">&nbsp;* </font></TD>								
							</tr>
							<tr>
							<td align="right" height="20">&nbsp;启用vpn采集</td>
							<td colspan="3" align="left">&nbsp;
							<select name="enablevpn">
                                  <option value="1" <%if(vo.getEnablevpn()==1){out.print("selected");} %>>采集</option>
                                  <option value="0" <%if(vo.getEnablevpn()==0){out.print("selected");} %>>不采集</option>
                            </select>
							
							
							</td>
						</tr>
															                 										                      								

									            							
															<tr style="background-color: #ECECEC;">
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="保 存" style="width: 50" id="vpnadd" onclick="#">
																					&nbsp;&nbsp;&nbsp;
																    <input type="reset" value="重 置" style="width: 50" id="vpnadd" onclick="#">
																					&nbsp;&nbsp;&nbsp;
																	<input type="reset" style="width: 50" value="关 闭" onclick="window.close()">
																</TD>	
															</tr>	
							
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
</body>
</HTML>