<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>

<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>

<%  
   String rootPath = request.getContextPath();  
   HostNodeDao hostdao= new HostNodeDao();
   List hostnodelist = hostdao.loadHostByFlag(new Integer(1));
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
  
     var chk1 = checkinput("typeid","typeid","ID",30,false);
     var chk2 = checkinput("chname","string","����",30,false);
     var chk3 = checkinput("bak","string","��ע",30,false);
     
     if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/porttype.do?action=add";
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


function save() {
	var name = document.getElementById("name").value;
	var subtype = document.getElementById("subtype").value;
	var uniquekey = document.getElementById("uniquekey").value;
	
	if(name.replace(/(^\s*)|(\s*$)/g,'') == ""){
		alert("��������Ϊ�գ�");
		document.mainForm.name.focus();
		return;
	}
	
	if(subtype.replace(/(^\s*)|(\s*$)/g,'') == ""){
		alert("�����Ͳ���Ϊ�գ�");
		document.mainForm.subtype.focus();
		return;
	}
	
	if(uniquekey.replace(/(^\s*)|(\s*$)/g,'') == ""){
		alert("�ؼ���ʶ����Ϊ�գ�");
		document.mainForm.uniquekey.focus();
		return;
	}
	
	 mainForm.action = "<%=rootPath%>/applicationnode.do?action=add";
        mainForm.submit();
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

function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
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


</head>
<body id="body" class="body" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
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
											                	<td class="add-content-title"> Ӧ�� >> Ӧ��ϵͳ >> ���Ӧ�ýڵ���Ϣ</td>
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
																		<TD nowrap align="right" height="24" width="10%">IP��ַ&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type="text" name="ipaddress" maxlength="50" size="20" style="width:150px;" class="formStyle">
																		<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type="text" name="name" maxlength="50" size="20" style="width:150px;" class="formStyle">
																			<font color="red">&nbsp;*</font></TD>															
																		</tr>
																			     <tr >						
																		<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type="text" name="type" maxlength="50" size="20" style="width:150px;" class="formStyle" readonly="readonly" value="application">
																		<TD nowrap align="right" height="24" width="10%">������&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<select name="subtype" class="formStyle" style="width:150px;">
																				<option value="ptimedb">PTimeDB</option>
																				<option value="url">URL</option>
																			</select>
																		</TD>											
																		</tr>
																			     <tr style="background-color: #ECECEC;" >						
																		<TD nowrap align="right" height="24" width="10%">�ؼ���ʶ&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type="text" name="uniquekey" maxlength="50" size="20" style="width:150px;" class="formStyle">
																			<font color="red">&nbsp;*</font>
																		<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type="text" name="descr" maxlength="200" size="60" class="formStyle">
																			</TD>															
																		</tr>
																	<tr style="background-color: #ECECEC;" >						
																		<TD nowrap align="right" height="24" width="10%">����ҵ��&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="��������ҵ��" onclick='setBid("bidtext" , "bid");'>
																			<input type="hidden" id="bid" name="bid" value="">
																		</TD>
																		<TD nowrap align="right" height="24" width="10%">�Ƿ����&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																			<select name="managed" class="formStyle" style="width:150px;">
																				<option value="1">��</option>
																				<option value="0">��</option>
																			</select>
																		</TD>
																	</tr>

																	
																	<tr>
																		<TD  colspan="4" align=center>
																		<br><input type="button" value="�� ��" style="width:50" id="process" onclick="save()">&nbsp;&nbsp;
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
	</body>
					
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