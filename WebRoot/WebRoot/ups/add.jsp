<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>      
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>   
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
  String rootPath = request.getContextPath();
  String flag = (String)request.getAttribute("flag");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!-- snow add for gatherTime at 2010-5-19 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow函数在application/resource/js/addTimeConfig.js中 
</script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="JavaScript" type="text/javascript">

  function toAdd()
  {
  	var chk1 = checkinput("alias","string","名称",20,false);
    var chk2 = checkinput("ipadress","ip","IP地址",15,false);
    var chk3 = checkinput("type","string","类型",30,false);
    var chk4 = checkinput("community","string","团体名",20,false);
    if(chk1&&chk2&&chk3&&chk4)
    {
    	mainForm.action = "<%=rootPath%>/ups.do?action=add";
        mainForm.submit();
    }
  }
  
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

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
}
</script>

</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" id="category" name="category" value="">
		<input type="hidden" id="flag" name="flag" value="<%=flag%>">
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
											                	<td class="add-content-title">环境动力设备监视 >> 添加</td>
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
																			<tr>						
																			    <TD nowrap align="right" height="24">名称&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<input type="text" name="alias" size="20" class="formStyle"><font color='red'>*</font></TD>		
																				<TD nowrap align="right" height="24" width="10%">IP地址&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<input type="text" name="ipadress" size="20" class="formStyle"><font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">						
																				<TD nowrap align="right" height="24">类型&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<select size=1 name='type' style='width:100px;'>
											            								<option value='ups' selected>UPS</option>
											            								<option value='air'>空调</option>
											            							</select>&nbsp;
																				    <font color='red'>*</font>
																				</TD>
																				<TD nowrap align="right" height="24">子类型&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<select size=1 name='subtype' style='width:100px;'>
											            								<option value='ems' selected>艾默生</option>
											            								<option value='mge'>梅兰日兰</option>
											            							</select>&nbsp;
																				    <font color='red'>*</font>
																				</TD>
																	        </tr>
																	        <tr>
																	            <TD nowrap align="right" height="24" width="10%">团体名&nbsp;</TD>
																				<TD nowrap width="40%">&nbsp;<input type="text" name="community" size="20" class="formStyle"><font color='red'>*</font></TD>
																				<TD nowrap align="right" height="24" width="10%">采集方式&nbsp;</TD>				
																				<TD nowrap width="40%" >&nbsp;<select size=1 name='collecttype' style='width:100px;'>
											            								<option value='1' selected>SNMP</option>
											            								<option value='2'>其他</option>
											            							</select>&nbsp;</TD>    
																	        </tr>
																	        <tr style="background-color: #ECECEC;">	
																	            <TD nowrap align="right" height="24" width="10%">是否监视&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<select name="ismanaged">
																            									<option value="1" selected>是</option>
																            									<option value="0">否</option>
																            								 </select><font color='red'>*</font>
																            	</TD>
																            	<TD nowrap align="right" height="24" width="10%">设备位置&nbsp;</TD>
																	        	<TD nowrap width="40%">&nbsp;<input type="text" name="location" size="20" class="formStyle"></TD>	
																	        </tr>
																	        <tr>	
																            	<TD nowrap align="right" height="24">所属业务&nbsp;</TD>
																            	<TD nowrap height="1">&nbsp;
																				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="40" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置所属业务" onclick='setBid("bidtext" , "bid");'>
																				<input type="hidden" id="bid" name="bid" value="">
																				</TD>
																	        </tr>
																	        <tr>
							 													<td nowrap align="right" width="10%" style="height:35px;">信息采集时间&nbsp;</td>		
							 													<td nowrap  colspan="3">
							        												<div id="formDiv" style="">         
																		                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																	                        <tr>
																	                            <td align="left">  
																		                            <br>
																	                                <table id="timeConfigTable" style="width:70%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
																                                        <tr>
																                                            <td colspan="7" height="50" align="center"> 
																                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
																                                            </td>
																                                        </tr>
																	                                </table>
																	                            </td><td valign="top"><br></td>
																	                        </tr>
																		                </table>
																		            </div> 
																				</td>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="保存" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
																					<input type=reset class="formStylebutton" style="width:50" value="返回" onclick="javascript:history.back(1)">
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