<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.CompRule"%>
<%@page import="com.afunms.config.model.CompGroupRule"%>
<%@page import="com.afunms.config.model.CompStrategy"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
 
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/JavaScript">
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
});

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow", "height=450, width=750, toolbar= no, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=100,left=300")

}   
function createStrategy(){
return CreateWindow("<%=rootPath%>/configRule.do?action=createStrategy");
}
function deleteStrategy(){
mainForm.action="<%=rootPath%>/configRule.do?action=deleteStrategy";
mainForm.submit();
}

function editStrategy(id){
return CreateWindow("<%=rootPath%>/configRule.do?action=editStrategy&id="+id);
}
function showDetailStrategy(id){
return location.href="<%=rootPath%>/configRule.do?action=showDetailStrategy&id="+id;
}
function toAdd(id)
  {
    var url="<%=rootPath%>/configRule.do?action=ready_addip&id="+id;
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
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
	setClass();
}

function setClass(){
	document.getElementById('configRuleTitle-0').className='detail-data-title';
	document.getElementById('configRuleTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('configRuleTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


</head>
<body id="body" class="body" onload="initmenu();" bgcolor="#cedefa">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
			</div>
		<div id="loading-mask" style=""></div>		
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
							<td class="td-container-main-report">
								<table id="container-main-report" class="container-main-report">
									<tr>
										<td>
											<table id="report-content" class="report-content">
												<tr>
													<td>
														<table id="report-content-header" class="report-content-header">
										                	<tr>
											    				<td>
														    		<%=configRuleTitleTable%>
														    	</td>
														  	</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="report-content-body" class="report-content-body">
				        									<tr>
				        										<td>
				        											
				        											<table id="report-data-header" class="report-data-header">
				        												<tr>
															  				<td>
																
																				<table id="report-data-header-title" class="report-data-header-title">
																					<tr>
																						<td >
																							 &nbsp;&nbsp;&nbsp;<input name="button"  onclick="createStrategy()" type="button"  value="新建策略" />
																							 <input name="button"  onclick="deleteStrategy()" type="button"  value="删除策略" />
																							 <input name="button"  onclick="" type="button"  value="Adhoc测试" />
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
				        											
				        											<table id="report-data-body" class="report-data-body">
				        												
				        												<tr height=28>
																	    	<td class="report-data-body-title" width="10%">&nbsp;<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">序号</td>
																	        <td class="report-data-body-title" width="10%">策略名称</td>
																	        <td class="report-data-body-title" width="40%">描述</td>
																	        <td class="report-data-body-title" width="10%">关联设备</td>
																	        <td class="report-data-body-title" width="10%">配置类型</td>
																	    	<td class="report-data-body-title" width="10%">创建人</td>
																	    	<td class="report-data-body-title" width="10%">编辑</td>
																	   </tr>
																	   <%
																	   if(list!=null){
																	   for(int i=0;i<list.size();i++){
																	    CompStrategy vo=(CompStrategy)list.get(i);
																	    String type="启动";
																	    if(vo.getType()==0){
																	    type="正在运行";
																	    }
																	    %>
																	    <tr <%=onmouseoverstyle%>>
																	    	<td align="center" class="body-data-list"><INPUT type="checkbox" class=noborder name="checkbox" value="<%=vo.getId()%>" ><%=i+1 %></td>
																	        <td align="center" class="body-data-list"><a href="#" onclick="showDetailStrategy(<%=vo.getId()%>)"><%=vo.getName() %></a></td>
																	        <td align="center" class="body-data-list"><%=vo.getDescription() %></td>
																	    	<td align="center" class="body-data-list"><a href="#" onclick="toAdd('<%=vo.getId() %>')">关联设备</a></td>
																	    	<td align="center" class="body-data-list"><%=type %></td>
																	    	<td align="center" class="body-data-list"><%=vo.getCreateBy() %></td>
																	    	<td align="center" class="body-data-list"><a href="#" onclick="editStrategy(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
																	   </tr>
																	    <% 
																	    }
																	   }
																	    %>
																	   
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