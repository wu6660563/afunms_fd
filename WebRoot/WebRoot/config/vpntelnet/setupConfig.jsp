<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.Hua3VPNFileConfig"%>
<%
String rootPath = request.getContextPath();
String ipaddress = (String)request.getAttribute("ipaddress");
String id = (String)request.getAttribute("id");
List list = (List)request.getAttribute("list");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

<script type="text/javascript">
Ext.onReady(function(){  
 Ext.get("process").on("click",function(){
     	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
        mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=setupConfig";
		mainForm.submit();
     });
 });
 function download(configId)
{
	window.open ("<%=rootPath%>/vpntelnetconf.do?action=download&id="+configId, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
}
</script>

	</head>
	<body id="body" class="body">


		<!-- �Ҽ��˵�����-->
		<form name="mainForm" method="post">
			<input type=hidden name="ipaddress" value="<%=ipaddress%>">
			<input type=hidden name="id" value="<%=id%>">
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
																	<td class="add-content-title">
																		&nbsp;Ӧ������
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
																			<tr>
																				<td>
																					<table>
																						<tr>
																							<td align="center" class="body-data-title"
																								width="5%">
																								<!-- 
																								<INPUT type="radio" id="checkall"
																									name="checkall" onclick="javascript:chkall()">
																								 -->
																								���
																							</td>
																							<td align="center" class="body-data-title"
																								width="11%">
																								<a href="#" onclick="">�ļ�����</a>
																							</td>
																							<td align="center" class="body-data-title" width="5%">
																								<a href="#" onclick="">�ļ���С</a>
																							</td>
																							<td align="center" class="body-data-title" width="8%">
																								<a href="#" onclick="">�ļ�����</a>
																							</td>
																							<td align="center" class="body-data-title" width="8%">
																								<a href="#" onclick="">����ʱ��</a>
																							</td>
																							<td align="center" class="body-data-title" width="8%">
																								<a href="#" onclick="">����</a>
																							</td>
																						</tr>
																						<%
		        													if(list!=null)
		        													{
		        														if(list.size()>0)
		        														{
		        															for(int i =0;i<list.size();i++)
		        															{
		        																Hua3VPNFileConfig vo = (Hua3VPNFileConfig)list.get(i);
		        																String fullName = vo.getFileName();
		        																String fileName = fullName.substring(fullName.lastIndexOf("/")+1);
		        																String backup_time = vo.getBackupTime().toString().substring(0,vo.getBackupTime().toString().length()-5);
		        																
		        												%>
																						<tr <%=onmouseoverstyle%>>
																							<td align="center" class="body-data-list">
																								<INPUT type="radio" id="radio" name="radio" value="<%=vo.getId()%>"><%=i+1 %></td>
																							<td align="left" class="body-data-list"><%=fileName %></td>
																							<td align="center" class="body-data-list"><%=vo.getFileSize() %>KB</td>
																							<td align="center" class="body-data-list"><%=vo.getDescri() %></td>
																							<td align="center" class="body-data-list"><%=backup_time %></td>
																							<td align="center" class="body-data-list"><input type="button" value="����" onclick="download('<%=vo.getId() %>')"/></td>
																							<!-- 
					        											<td align="center" class="body-data-list">
					        												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','','') alt="�Ҽ�����">
					        											</td>
					        											 -->
																						</tr>
																						<%
		        															}
		        														}
		        													}
		        												 %>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="Ӧ  ��" style="width: 50" id="process">
																					&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="��  ��">
																					&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="��  ��"
																						onclick="window.close();">
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
																				<td align="left" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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