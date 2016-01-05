<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String) request.getAttribute("menuTable");

    String startdate = (String) request.getAttribute("startdate");
    String todate = (String) request.getAttribute("todate");

    List list = (List) request.getAttribute("list");
    
    Hashtable nodeDependListHashtable = (Hashtable) request.getAttribute("nodeDependListHashtable");

%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>

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



		<script language="JavaScript" type="text/JavaScript">
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);

 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/netreport.do?action=netping";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=400,height=400,directories=no,status=no,scrollbars=no,menubar=no")
}   

function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBidbyuser&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
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
	setClass();
	
}

function setClass(){
	document.getElementById('netReportTitle-0').className='detail-data-title';
	document.getElementById('netReportTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('netReportTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function ManageXMLNode() {

	this.id = null;
	
	this.name = null;	 // ҵ������
	 
	this.nodeDTOArray = new Array();
}

var manageXMLNodeArray = new Array();
function init2() {
	<%
	
	for(int i = 0; i < list.size(); i++) {
		ManageXml manageXml = (ManageXml)list.get(i);
		String manageXmlId = (manageXml.getId())+"";
		String topoName = manageXml.getTopoName();
		List<NodeDTO> list2 = (List<NodeDTO>)nodeDependListHashtable.get(manageXml);
		%>
		var manageXMLNode = new ManageXMLNode();
		manageXMLNode.id = "<%=manageXmlId%>";
		manageXMLNode.name = "<%=topoName%>";
		<%
		for(int j = 0; j < list2.size(); j++) {
			NodeDTO nodeDTO = list2.get(j);
			String name = nodeDTO.getName();
			String nodeid = nodeDTO.getNodeid();	// �豸id ���豸typeȷ��һ���澯��¼
			String nodeType = nodeDTO.getType();    // �豸type
			String nodeSubType = nodeDTO.getSubtype();
			String nodeIP = nodeDTO.getIpaddress();
			%>
			var nodeDTO = new Array();
			nodeDTO[0] = "<%=name%>";
			nodeDTO[1] = "<%=nodeid%>";
			nodeDTO[2] = "<%=nodeType%>";
			nodeDTO[3] = "<%=nodeSubType%>";
			nodeDTO[4] = "<%=nodeIP%>";
			
			manageXMLNode.nodeDTOArray[<%=j%>] = nodeDTO;
			<%
		}
		%>
		manageXMLNodeArray.push(manageXMLNode);
		<%
	}
%>
}
init2();

// �����û�ѡ���豸���ɱ���
function popupReport(reportName){
	 
     // ��ȡ�û�����Ĳ�ѯʱ��
	 var startdate = document.getElementById("startdate").value;
	 var todate = document.getElementById("todate").value;
	 var starttime = startdate + " 00:00:00";
	 var endtime = todate + " 23:59:59";
     
     var busName = new Array();  // ҵ������
	
	// ��ȡ�û�ѡ�е�ҵ����Ϣ
	var checkList = document.getElementsByName("checkboxNode"); 
	var nodeNum = 0;
	var busName = "";
	var nodeId = new Array();
	var nodeType = new Array(); // �豸����
	var nodeIp = new Array();	// �豸IP
	var nodeName = new Array();	// �豸����
	var nodeSubtype = new Array();	// �豸������
	var sqlCondition = "";	// ��ѯSQL����
	
	if (checkList && checkList.length > 0){
		for(var i=0; i<checkList.length; i++){
			var checkbox = checkList[i];
			if(checkbox.checked) {
				var checkboxValue = checkbox.value;
				for(var j=0; j<manageXMLNodeArray.length; j++) {
					
					var manageXMLNode = manageXMLNodeArray[j];
					if (checkboxValue == manageXMLNode.id) {
						busName= manageXMLNode.name;
						busName="";
						 var nodeDTOArray = manageXMLNode.nodeDTOArray;
						  for(var k=0; k<nodeDTOArray.length; k++) {
						    nodeName[nodeNum] = "nodeNum";
						 	nodeId[nodeNum] = nodeDTOArray[k][1];
							nodeType[nodeNum] = nodeDTOArray[k][2];
							nodeIp[nodeNum] = nodeDTOArray[k][4];
							nodeSubtype[nodeNum] = nodeDTOArray[k][3];
							sqlCondition += " or (nodeid="+nodeId[nodeNum]+" and subtype='"+nodeType[nodeNum]+"')";
							
							nodeNum++;	
									
						 }// for
						
					}// if
				}//for 
							
			}
			
		}
		sqlCondition = sqlCondition.substring(4);
		if(reportName=='nodeAlarmPerHour'){
			window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' 
					+ starttime + ';endtime=' + endtime +';query=' + sqlCondition+';nodeNum='+nodeNum ,'�豸�澯����','height=500,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes');		
	
		}else if(reportName=='bussinessview'){
			window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&busName='+ busName +';starttime=' + starttime + ';endtime=' + endtime +';nodeId='+ nodeId +';nodeIp=' + nodeIp + ';nodeName=' + nodeName + ';nodeType='+ nodeType +';nodeSubtype='+ nodeSubtype +';query=' + sqlCondition ,'�豸�澯����','height=500,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes');
		}else if(reportName == 'nodeAlarmPerDay'){
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' 
					+ starttime + ';endtime=' + endtime +';query=' + sqlCondition ,'�豸�澯����','height=500,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes');		
		}
	}else{
		alert("��ѡ���豸!!!");
	}

}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
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
															<table id="report-content-body"
																class="report-content-body">
																<tr>
																	<td>

																		<table id="report-data-header"
																			class="report-data-header">
																			<tr>
																				<td>

																					<table id="report-data-header-title"
																						class="report-data-header-title">
																						<tr>
																							<td>
																								��ʼ����
																								<input type="text" name="startdate"
																									value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;"
																									href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																									<img id=imageCalendar1 align=absmiddle width=34
																										height=21
																										src="<%=rootPath%>/include/calendar/button.gif"
																										border=0> </a> ��ֹ����
																								<input type="text" name="todate"
																									value="<%=todate%>" size="10" />
																								<a onclick="event.cancelBubble=true;"
																									href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																									<img id=imageCalendar2 align=absmiddle width=34
																										height=21
																										src="<%=rootPath%>/include/calendar/button.gif"
																										border=0> </a>


																							</td>
																						</tr><tr><td valign="top"><br></td></tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td>

																		<table id="report-data-body" class="report-data-body">
																			<tr height=50>
																				<td class="report-data-body-title">
																					<input type="button" name="nodeAlarmCnt"
																						value="ҵ����ͼ�豸�澯��ͳ�Ʊ���"
																						onclick="popupReport('bussinessview')">
																				</td>
																				<td class="report-data-body-title">
																					<input type="button" name="nodeAlarmPerDay" value="ҵ����ͼ�豸ÿ�ո澯��ͳ�Ʊ���"
																							onclick="popupReport('nodeAlarmPerDay')">
																				</td>
																				<td class="report-data-body-title">
																					<input type="button" name="nodeAlarmPerHour"
																						value="ҵ����ͼ�豸ÿʱ�澯��ͳ�Ʊ���"
																						onclick="popupReport('nodeAlarmPerHour')">
																				</td>
																				<td class="report-data-body-title">
																				</td>
																				<td class="report-data-body-title">
																				</td>
																				<td class="report-data-body-title">
																				</td>
																			</tr>
																			<tr height=28>
																				<td class="report-data-body-title">
																					&nbsp;
																					<INPUT type="radio" name="checkall"
																						onclick="javascript:chkall()">
																					���
																				</td>
																				<td class="report-data-body-title">
																					����
																				</td>
																				<td class="report-data-body-title">
																					״̬
																				</td>
																				<td class="report-data-body-title">
																					��Դ��
																				</td>

																			</tr>
																			<%
																			    if (list != null) {
																			        NodeAlarmService nodeAlarmService = new NodeAlarmService();
																			        for (int i = 0; i < list.size(); i++) {
																			            ManageXml manageXml = (ManageXml) list.get(i);
																			            String manageXmlBid = manageXml.getBid();

																			            List nodeDependList = (List) nodeDependListHashtable
																			                    .get(manageXml);
																			            String size = "0";
																			            if (nodeDependList != null) {
																			                size = nodeDependList.size() + "";
																			            }
																			            int maxAlarmLevel = nodeAlarmService
																			                    .getMaxAlarmLevel(nodeDependList);
																			%>

																			<tr bgcolor="#ffffff" <%=onmouseoverstyle%> height=25>
																				<td class="report-data-body-list">
																					&nbsp;&nbsp;
																					<INPUT type="radio" name="checkboxNode"
																						id="checkboxNode" value=<%=manageXml.getId()%>>
																				</td>
																				<td class="report-data-body-list"><%=manageXml.getTopoName()%></td>
																				<td class="report-data-body-list">
																					<img
																						src="<%=rootPath + "/resource/"
                            + NodeHelper.getCurrentStatusImage(maxAlarmLevel)%>">
																				</td>
																				<td class="report-data-body-list"><%=size%></td>
																			<tr>
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