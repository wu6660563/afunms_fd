<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.application.model.URLConfig"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%
	String myflag = (String) request.getAttribute("flag");
	Integer myId = (Integer) request.getAttribute("id");
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

	URLConfig urlConfig = (URLConfig) request.getAttribute("urlConfig");
    
    NodeDTO node = (NodeDTO) request.getAttribute("node");
    String maxAlarmLevel = (String) request.getAttribute("maxAlarmLevel");

	String flag_1 = (String) request.getAttribute("flag");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

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
	setClass();
}
function setClass(){
	document.getElementById('urlDetailTitle-0').className='detail-data-title';
	document.getElementById('urlDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('urlDetailTitle-0').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
		
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/web.do?action=detail";
      mainForm.submit();
} 


</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag_1%>">
			<input type="hidden" id="id" name="id" value="<%=myId%>">
            <input type=hidden name="nodeid" value="<%=node.getNodeid()%>">
            <input type=hidden name="type" value="<%=node.getType()%>">
            <input type=hidden name="subtype" value="<%=node.getSubtype()%>">
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
					<td class="td-container-main-detail">
						<table id="container-main-detail" class="container-main-detail">
							<tr>
								<td>
									<table id="detail-content" class="detail-content">
										<tr>
											<td>
												<table id="detail-content-header"
													class="detail-content-header">
													<tr>
														<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
														<td class="detail-content-title">
															URL 详细信息
														</td>
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
															<table >
																<tr>
																	<td>

																		<table>
																			<tr>
																			</tr>
																			<tr>
																				<td>
																					<table cellspacing="10" border=0 >
																						<tr>
																							<td width="60%" align="center" valign=top
																								cellspacing="0" border=0>

																								<table style="BORDER-COLLAPSE: collapse"
																									bordercolor=#cedefa cellpadding=0 rules=none
																									width=100% align=center border=1 algin="center">
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;名称:
																										</td>
																										<td width="70%"><%=node.getName()%></td>
																									</tr>
																									<tr>
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;类型:
																										</td>
																										<td width="70%">
																											<%=node.getSubtype()%>
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;状态:
																										</td>
																										<td width="70%">
																											<img
																												src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(Integer.valueOf(maxAlarmLevel))%>"
																												border="0">
																										</td>
																									</tr>
																									<tr>
																										<td height="29" align="left">
																											&nbsp;访问地址:
																										</td>
																										<td>
																											<a href="<%=urlConfig.getUrl()%>"
																												target="_blank"><%=urlConfig.getUrl()%></a>
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align=left nowrap>
																											&nbsp;IP地址:
																										</td>
																										<td width="70%"><%=node.getIpaddress()%></td>
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
									<table id="detail-data" class="detail-data">
										<tr>
											<td class="detail-data-header">
												<%=urlDetailTitleTable%>
											</td>
										</tr>
										<tr>
											<td>
												<table class="detail-data-body">
													<tr>
														<td align=center valign=top>
															<br>
															<table cellpadding="0" cellspacing="0" width=48%
																align=center>
																<tr>
																	<td width="48%" align="center">
																		<table width="100%" cellspacing="0" cellpadding="0"
																			align="center">
																			<tr>
																				<td width="100%" align="center">
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Web_Ping_Line.swf?id=<%=node.getNodeid()%>", "Web_Ping_Line", "400", "250", "8", "#ffffff");
																											so.write("flashcontent2");
																										</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td width="48%" align="center">
																		<table width="100%" cellspacing="0" cellpadding="0"
																			align="center">
																			<tr>
																				<td width="100%" align="center">
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Web_time_area.swf?id=<%=node.getNodeid()%>", "Web_time_area", "400", "250", "8", "#ffffff");
																											so.write("flashcontent3");
																										</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
															<br>
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
		<script>			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  mainForm.action = "<%=rootPath%>/web.do?action=sychronizeData&id=<%=node.getNodeid()%>&flag=<%=flag_1%>&page=detail";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>