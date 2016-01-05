<%@page language="java" contentType="text/html;charset=gb2312" %>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
%>
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





<script language="JavaScript" type="text/javascript">


	Ext.onReady(function(){  

	 	Ext.get("process").on("click",function(){
	        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
	        mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=save";
	        mainForm.submit();
	 	});	
	});
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
											                	<td class="add-content-title"> ��Դ >> ���ܼ��� >> ����ָ�� >> ����ָ����� </td>
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
				        											<table cellspacing="1">
																		<tr style="background-color: #ECECEC;">						
																			<td align="right" height="24" width="10%">����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="name" name="name">
																			</td>
																			<td align="right" height="24" width="10%">����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="alias" name="alias">
																			</td>
																		</tr>	
																		<tr>
																			<td align="right" height="24" width="10%">����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="type" name="type">
																			</td>
																			<td align="right" height="24" width="10%">������:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<input type="text" id="subtype" name="subtype">
																			</td>
																		</tr>
												    					<tr style="background-color: #ECECEC;"> 
																			<td align="right" height="24" width="10%">����:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<input type="text" name="description" id="description">
																			</td>
																			<td align="right" height="24" width="10%">ָ������:&nbsp;</td>	
																			<td width="40%">&nbsp;
																				<input type="text" name="category" id="category">
																			</td>					
												 						</tr> 
												 						<tr> 
												 							<td align="right" height="24" width="10%">�Ƿ�ɼ�:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="isCollection" name="isCollection" style="width: 132px;">
																					<option value="1">��</option>
																					<option value="0">��</option>
																					
																				</select>
																			</td>
																			<td align="right" height="24" width="10%">Ӧ����Ĭ������:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="isDefault" name="isDefault" style="width: 132px;">
																					<option value="1">��</option>
																					<option value="0">��</option>
																				</select>
																			</td>
												 						</tr> 
												 						<tr style="background-color: #ECECEC;"> 
												  							<td align="right" height="24" width="10%">�ɼ����:&nbsp;</td>				
																			<td width="40%">&nbsp;
																				<select id="poll_interval" name="poll_interval">
																					<option value="5-m">5����</option>
																					<option value="10-m">10����</option>
																					<option value="30-m">30����</option>
																					<option value="1-h">1Сʱ</option>
																					<option value="4-h">4Сʱ</option>
																					<option value="8-h">8Сʱ</option>
																					<option value="12-h">12Сʱ</option>
																					<option value="1-d">1��</option>
																					<option value="1-w">1��</option>
																					<option value="1-mt">1��</option>
																					<option value="1-y">1��</option>
																				</select>

																			</td>
																			<td align="right" height="24" width="10%"></td>				
																			<td width="40%">&nbsp;
																			</td>
												 						</tr> 
																		<tr>
																			<TD nowrap colspan="4" align=center>
																			<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																				<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
																			</TD>	
																		</tr>	
																	</table>
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
</HTML>