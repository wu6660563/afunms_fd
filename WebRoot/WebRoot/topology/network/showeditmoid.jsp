<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.model.*"%>
<% 

  String rootPath = request.getContextPath(); 
  NodeMonitor nm = (NodeMonitor)request.getAttribute("nodemointor");
  String id = (String)request.getAttribute("id");
  Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
  String ipaddress = (String)request.getAttribute("ipaddress");
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
	String enable0="";
	if(nm.isEnabled())enable0="selected";
	String enable1="";
	if(!nm.isEnabled())enable1="selected";
	String sms0_0="";
	if(nm.getSms0() == 0){
		sms0_0="selected";
	}
	String sms0_1="";
	if(nm.getSms0() == 1){
		sms0_1="selected";
	}
	String sms1_0="";
	if(nm.getSms1() == 0){
		sms1_0="selected";
	}
	String sms1_1="";
	if(nm.getSms1() == 1){
		sms1_1="selected";
	}
	String sms2_0="";
	if(nm.getSms2() == 0){
		sms2_0="selected";
	}
	String sms2_1="";
	if(nm.getSms2() == 1){
		sms2_1="selected";
	}						  	 
         
%>
<html>
<head>


<title>阀值修改 -> <%=nm.getDescr()%></title>
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script language="JavaScript" type="text/javascript">

function toEdit()
  {  
     {
     	//if (confirm("确定要修改吗?")){
        	//mainForm.action = "<%=rootPath%>/moid.do?action=updatemoid";
        	//mainForm.submit();
        	window.opener.location.reload();
        	setTimeout(window.close(),3000);
        //}
     }
     
     //window.close();
  }
function openwin3(operate,ipaddress,index,ifname) 
{	//var ipaddress = document.forms[0].ipaddress.value;
  window.open ("<%=rootPath%>/monitor.do?action="+operate+"&ipaddress="+ipaddress+"&ifindex="+index+"&ifname="+ifname, "newwindow", "height=400, width=850, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function toMain() 
{	//var ipaddress = document.forms[0].ipaddress.value;
  window.location="<%=rootPath%>/monitor.do?action=netif&id=<%=id%>&ipaddress=<%=ipaddress%>"; 
}
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

</script>
</head>
<body id="body" class="body" >

	<!-- 右键菜单结束-->
   <form name="mainForm" method="post" action="<%=rootPath%>/moid.do?action=updatemoid">
   <input type=hidden name="id" value="<%=id%>">
   <input type=hidden name="moid" value="<%=nm.getId()%>">
   <input type=hidden name="ipaddress" value="<%=ipaddress%>">
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
											                	<td class="add-content-title">阀值修改 >> <%=nm.getDescr()%></td>
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
								<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">阀值名称&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;<%=nm.getDescr()%>
                  					
                  					</TD>
                				</tr>
                				<tr>
                  					<TD nowrap align="right" height="24" width="40%">监视&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<select name=enabled>
                  					<option value="0" <%=enable1%>>否</option>
                  					<option value="1" <%=enable0%>>是</option>
                  					</select>
                  					
                  					</TD>
                				</tr>
                  				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">一级阀值(普通)&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text"  name="limenvalue0" maxlength="50" size="20" class="formStyle" value="<%=nm.getLimenvalue0()%>"><%=nm.getUnit()%>
                  					</TD>
                				</tr>
                				<tr>
                  					<TD nowrap align="right" height="24" width="40%">次数&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text"  name="time0" maxlength="50" size="20" class="formStyle" value="<%=nm.getTime0()%>">
                  					</TD>
                				</tr>  
                				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">短信&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<select name=sms0>
                  					<option value="0" <%=sms0_0%>>否</option>
                  					<option value="1" <%=sms0_1%>>是</option>
                  					</select>
                  					
                  					</TD>
                				</tr> 
                  				<tr >
                  					<TD nowrap align="right" height="24" width="40%">二级阀值(严重)&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text" name="limenvalue1" maxlength="50" size="20" class="formStyle" value="<%=nm.getLimenvalue1()%>"><%=nm.getUnit()%>
                  					</TD>
                				</tr>
                				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">次数&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text" name="time1" maxlength="50" size="20" class="formStyle" value="<%=nm.getTime1()%>">
                  					</TD>
                				</tr>  
                				<tr >
                  					<TD nowrap align="right" height="24" width="40%">短信&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<select name=sms1>
                  					<option value="0" <%=sms1_0%>>否</option>
                  					<option value="1" <%=sms1_1%>>是</option>
                  					</select>                  					
                  					
                  					</TD>
                				</tr>  
                  				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">三级阀值(紧急)&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text" name="limenvalue2" maxlength="50" size="20" class="formStyle" value="<%=nm.getLimenvalue2()%>"><%=nm.getUnit()%>
                  					</TD>
                				</tr>
                				<tr>
                  					<TD nowrap align="right" height="24" width="40%">次数&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<input type="text" name="time2" maxlength="50" size="20" class="formStyle" value="<%=nm.getTime2()%>">
                  					</TD>
                				</tr>  
                				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="40%">短信&nbsp;</TD>
                  					<TD nowrap width="60%">&nbsp;
                  					<select name=sms2>
                  					<option value="0" <%=sms2_0%>>否</option>
                  					<option value="1" <%=sms2_1%>>是</option>
                  					</select>                  					
                  					
                  					</TD>
                				</tr> 
                				<tr align=center>
                					<td colspan=11 align=center><br>
								<input type="Submit" class="formStylebutton" style="width:50" value="修 改" onclick="toEdit()">&nbsp;&nbsp; 
								<input type="reset" class="formStylebutton" style="width:50" value="关 闭" onclick="window.close()">               					
                  					</td>
                  				</tr>
            					</tbody>
            					</table>
            					<br>
			</TD>																			
			</tr>			
															
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
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