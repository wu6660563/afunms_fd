<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.Cluster"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list=(List)request.getAttribute("clusterList");
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
			charset="utf-8"></script>

		<!--nielin add for timeShareConfig at 2010-01-04 start-->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>
		<!--nielin add for timeShareConfig at 2010-01-04 end-->

		<script language="JavaScript" type="text/javascript">

 Ext.onReady(function()
{  

 Ext.get("remoteMachineadd").on("click",function(){
   	//var chk1 = checkinput("user","string","用户",50,false);
   	//var chk2 = checkinput("port","string","端口",50,false);
    //var chk6 = checkinput("ipaddress","string","ip地址",50,false);  
     
      //if(chk1&&chk2&&chk6)
     {      
          	Ext.MessageBox.wait('数据加载中，请稍后.. ');
        	mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=add";
        	mainForm.submit();
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
				var url="<%=rootPath%>/vpntelnetconf.do?action=serverip";
				window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
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
																	<td class="add-content-title">
																		远程登录设置 添加
																	</td>
																	<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-body"
																class="detail-content-body">
																<tr>
																	<td>

																		<table border="0" id="table1" cellpadding="0"
																			cellspacing="1" width="100%">

																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">名  称&nbsp;</TD>				
																				<TD nowrap width="40%" >&nbsp;
																					<input type="text" id="name" name="name" maxlength="50" size="22" class="formStyle">
																					<font color="red">*&nbsp;</font>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>&nbsp;操作系统</TD>		
																				<TD nowrap width="40%" colspan=3>&nbsp;
																					<select   name="serverType"  class="formStyle">
																						<option value='windows'>Windows</option>
																						<option value='aix'>AIX</option>
																						<option value='unix'>HP UNIX</option>
																						<option value='solarix'>SUN Solaris</option>
																						<option value='linux'>Linux</option>
																						<option value='as400'>AS400</option>
																					</select>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					用户名&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="user" size="22"
																						class="formStyle">
																					<font color="red">&nbsp;*</font>
																				</TD>
																				<TD nowrap align="right" height="24">
																					密  码&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<input type="password" name="password" size="22"
																						class="formStyle">
																					<font color="red">&nbsp;*
																				</TD>
																			</tr>

																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24">
																					IP地址&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<input name="ipaddress" type="text" size="22"
																						class="formStyle" id="ipaddress">
																					<input type="button" value="选择服务器"
																						onclick="showup()">
																					<font color="red">&nbsp;* </font>
																				</TD>
																				<TD align="right" height="20">
																					服务器组
																				</TD>
																				<TD nowrap width="40%" colspan=3>&nbsp;
																					<select   name="clusterId"  class="formStyle">
																					<option value='0'> </option>
																					<%
																					if(list!=null&&list.size()>0){
																					for(int i=0;i<list.size();i++){ 
																					Cluster cluster=(Cluster)list.get(i);
																					int id=cluster.getId();
																					String name=cluster.getName();
												       
																					%>
																						<option value='<%=id %>'><%=name %></option>
																				    <%
																				          }
																				    } %>
																				    
																				    
																					</select>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="保 存" style="width: 50"
																						id="remoteMachineadd" onclick="#">
																					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="返回"
																						onclick="javascript:history.back(1)">
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
															<table id="detail-content-footer"
																class="detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
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