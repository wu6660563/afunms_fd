<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.system.model.*"%>

<%
AlertInfoServer vo = (AlertInfoServer)request.getAttribute("vo");
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
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
	
 Ext.get("process").on("click",function(){
  
     var chk1 = checkinput("ipaddress","string","IP��ַ",50,false);
     var chk2 = checkinput("port","string","�˿ں�",50,false);

     if(chk1&&chk2)
     {      
            Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
        	mainForm.action = "<%=rootPath%>/alertinfo.do?action=update";
            mainForm.submit();
     }
       // mainForm.submit();
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
//���Ӳ˵�	
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



		<form id="mainForm" method="post" name="mainForm">
<input type=hidden name="id" value="<%=vo.getId()%>">
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
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="add-content-title">
																		ϵͳ���� >> ϵͳ���� >> ��Ϣ���������ñ༭
																	</td>
																	<td align="right">
																		<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																			width="5" height="29" />
																	</td>
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
																				<TD nowrap align="right" height="24" width="10%">
																					IP��ַ&nbsp;
																				</TD>
																				<TD nowrap width="20%">
																					&nbsp;
																					<input type="text" name="ipaddress" size="16"
																						value="<%=vo.getIpaddress() %>" class="formStyle">
																					<font color="red">&nbsp;*</font>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					�˿ں�&nbsp;
																				</TD>
																				<TD nowrap width="20%">
																					&nbsp;
																					<input type="text" name="port" size="16" value="<%=vo.getPort() %>"
																						class="formStyle">
																					<font color="red">&nbsp;*</font>
																				</TD>
																				<TD nowrap height="24">
																					�Ƿ�����&nbsp;
																				</TD>
																				<TD nowrap width="20%">
																					<select name="flag" class="formStyle">
																						<%
									if(vo.getFlag() == 0){
								%>
									<option value=0 selected>��</option>
									<option value=1>��</option>
								<%
									}else{
								%>
									<option value=0>��</option>
									<option value=1 selected>��</option>								
								<%
									}
								%>
																					</select>
																				</TD>

																			</tr>

																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					��Ϣ����������&nbsp;
																				</TD>
																				<TD nowrap width="60%" colspan="5">
																					&nbsp;
																					<input type="text" name="desc" size="50"
																						value="<%=vo.getDesc() %>" class="formStyle">
																				</TD>
																			</tr>

																			<tr>
																				<TD nowrap colspan="6" align=center>
																					<br>
																					<input type="button" value="�� ��" style="width: 50"
																						id="process" onclick="#">
																					&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="����"
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
																				<td align="left" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_01.jpg"
																						width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_03.jpg"
																						width="5" height="12" />
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