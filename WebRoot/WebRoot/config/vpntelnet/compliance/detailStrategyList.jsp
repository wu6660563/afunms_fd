<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.config.model.CompCheckResultModel"%>
<%@page import="com.afunms.config.model.CheckResult"%>
<%@page import="com.afunms.config.dao.CheckResultDao"%>
<%@page import="com.afunms.config.model.StrategyIp"%>
<%@page import="com.afunms.config.dao.StrategyIpDao"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%
	String rootPath = request.getContextPath();
	List<CheckResult> checkList = (List<CheckResult>) request
			.getAttribute("list");
	String id = (String) request.getAttribute("id");
	List ipList = (List) request.getAttribute("ipList");
	String common = "<img src='/afunms/img/common.gif'>";
	String serious = "<img src='/afunms/img/serious.gif'>";
	String urgency = "<img src='/afunms/img/urgency.gif'>";
	String correct = "<img src='/afunms/img/correct.gif'>";
	String disable = "<img src='/afunms/img/blue.gif'>";
	String error = "<img src='/afunms/img/error.gif'>";
	String statusImg = "<img src='/afunms/img/correct.gif'>";
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />

		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<meta http-equiv="Page-Enter"
			content="revealTrans(duration=x, transition=y)">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";
  function toAddCluster(){
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=delOrAdd&type=1";
     mainForm.submit();
  }
  function toAdd()
  {
    var url="<%=rootPath%>/configRule.do?action=ready_addip&id=<%=id%>";
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
  }
  function exeRule()
  {
     mainForm.action = "<%=rootPath%>/configRule.do?action=exeRule&id=<%=id%>";
     mainForm.submit();
     
  } 
  function viewDetail(id,ip)
  {
    var url="<%=rootPath%>/configRule.do?action=viewDetail&id="+id+"&ip="+ip;
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
  }  
   function showDetail(id,divId){

document.getElementById(id).innerHTML="<span  onclick=\"hiddenDetail(\'"+id+"\',\'"+divId+"\')\" >-&nbsp;</span>";
document.getElementById(divId).style.display = "block";

}
function hiddenDetail(id,divId){
document.getElementById(id).innerHTML="<span  onclick=\"showDetail(\'"+id+"\',\'"+divId+"\')\">&nbsp;+&nbsp;&nbsp;</span>";
document.getElementById(divId).style.display = "none";

}
function showRule(strategyId,groupId,ruleId,ip,isVor){
 var url="<%=rootPath%>/configRule.do?action=showRule&strategyId="+strategyId+"&groupId="+groupId+"&ruleId="+ruleId+"&ip="+ip+"&isVor="+isVor;
	CreateWindow(url);
}
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow", "height=450, width=750, toolbar= no, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=100,left=300")

}  
</script>
		<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/FTP.do?action=detail&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/FTP.do?action=changeMonflag&value=0&id="+node;
	}
	
	function openNewWindow(id) {
	
		window.open("<%=rootPath%>/serverUpAndDown.do?action=ready_editName&id="+id,"_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
	
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

}

</script>
	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">

		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						�޸���Ϣ
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.reboot()">
						��������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.shutdown()">
						�رշ�����
					</td>
				</tr>
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form method="post" name="mainForm">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>

					</td>
					<td align="center" valign=top>
						<table width="98%" cellpadding="0" cellspacing="0" algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="layout_title">
												Ӧ�� >> �Ϲ��� >> ����
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="100%" align='right'>

												<a href="#" onclick="toAdd()">�����豸</a>
												<a href="#" onclick="exeRule()">���в���</a>
												<a href="#" onclick="javascript:history.back(1)">����</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
										<tr class="microsoftLook0" height=28>

											<th width='5%'>
												���
											</th>

											<th width='10%'>
												IP��ַ
											</th>
											<th width='10%'>
												�Ϲ���״̬
											</th>
											<th width='30%'>
												Υ��������
											</th>
											<th width='15%'>
												�Ϲ��Թ�����
											</th>
											<th width='30%'>
												�ϴμ��ʱ��
											</th>

										</tr>
										<%
											CheckResult vo = null;
											int count = 0;
											if (checkList != null && checkList.size() > 0) {
												for (int i = 0; i < checkList.size(); i++) {
													CheckResult result = checkList.get(i);
													String status = "�Ϲ�";

													if (result.getCount0() > 0 || result.getCount1() > 0
															|| result.getCount2() > 0) {
														status = "Υ��";
														statusImg = "<img src='/afunms/img/error.gif'>";
													}
													CheckResultDao dao = new CheckResultDao();
													vo = dao.getExtraCount1(id, result.getIp());
													if (vo != null)
														count = vo.getExactCount();
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook">

											<td height=25 align=center>
												<font color='blue'><%=1 + i%></font>
											</td>
											<td height=25 align=center>
											&nbsp;<span id="show<%=i%>"><span onclick="showDetail('show<%=i%>','group<%=i%>')">&nbsp;&nbsp;+&nbsp;&nbsp;</span> </span><%=result.getIp()%></td>
											<td height=25 align=center>
												&nbsp;<%=statusImg%>
												<%=status%></td>
											<td height=25 align=center>
												<%=common%>
												<%=result.getCount0()%>&nbsp;&nbsp;<%=serious%>
												<%=result.getCount1()%>&nbsp;&nbsp;<%=urgency%>
												<%=result.getCount2()%></td>

											<td height=25 align=center><%=correct%>
												<%=count%></td>
											<td height=25 align=center><%=result.getCheckTime()%></td>

										</tr>
										<tr bgcolor="#FFFFFF">
											<td colspan=6>
												<div id="group<%=i%>" style="display: none;">
													<table>
														<tr>
															<td nowrap align="center" height="25" width="20%">
																&nbsp;<%=error%>
																&nbsp;Υ���Ĺ���
															</td>
															<td width="80%" align="left">
																&nbsp;&nbsp;
															</td>

														</tr>

														<%
															CheckResultDao resultDao2 = new CheckResultDao();
																	List list = resultDao2.getReslutByIdAndIp(id, result
																			.getIp());
																	if (list != null && list.size() > 0) {
																		boolean flag1 = true;
																		for (int k = 0; k < list.size(); k++) {
																			String level = "";
																			String levelStr = "";
																			CompCheckResultModel model = (CompCheckResultModel) list
																					.get(k);

																			if (model.getViolationSeverity() == 2) {
																				level = "<img src='/afunms/img/urgency.gif'>";
																				levelStr = "���ص�";
																			} else if (model.getViolationSeverity() == 1) {
																				level = "<img src='/afunms/img/serious.gif'>";
																				levelStr = "��Ҫ��";
																			} else if (model.getViolationSeverity() == 0) {
																				level = "<img src='/afunms/img/common.gif'>";
																				levelStr = "��ͨ��";
																			}
																			if (model.getIsViolation() == 0) {
														%>
														<tr>
															<td nowrap align="center" height="32" width="10%">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=level%><%=levelStr%></td>
															<td width="90%" height="32" align="left">
																&nbsp;&nbsp;
																<a href="#"
																	onclick="showRule('<%=model.getStrategyId()%>','<%=model.getGroupId()%>','<%=model.getRuleId()%>','<%=model.getIp()%>','0')"><%=model.getRuleName()%></a>
																[<%=model.getGroupName()%>]-
																<%=model.getDescription()%></td>

														</tr>
														<%
															} else if (model.getIsViolation() == 1) {
																				if (flag1) {
																					flag1 = false;
														%>
														<tr>
															<td nowrap align="center" height="32" width="20%">
																&nbsp;<%=correct%>&nbsp; �Ϲ�Ĺ���
															</td>
															<td height="32" width="80%" align="left">
																&nbsp;&nbsp;
															</td>

														</tr>
														<%
															}
														%>
														<tr>
															<td nowrap align="center" height="32" width="10%">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=level%><%=levelStr%></td>
															<td height="32" width="90%" align="left">
																&nbsp;&nbsp;
																<a href="#"
																	onclick="showRule('<%=model.getStrategyId()%>','<%=model.getGroupId()%>','<%=model.getRuleId()%>','<%=model.getIp()%>','1')"><%=model.getRuleName()%></a>
																[<%=model.getGroupName()%>]-
																<%=model.getDescription()%></td>

														</tr>
														<%
															}
																		}
																	}
														%>
													</table>

												</div>
											</td>
										</tr>
										<%
											}

											}
											int j = 0;
											if (ipList != null && ipList.size() > 0) {
												int k = 0;
												if (checkList != null)
													k = checkList.size();
												for (int i = 0; i < ipList.size(); i++) {
													StrategyIp strategyIp = (StrategyIp) ipList.get(i);
													CheckResultDao dao = new CheckResultDao();
													vo = dao.getExtraCount(id, strategyIp.getIp());
													if (vo != null)
														count = vo.getExactCount();
													if (count > 0) {

														j = 1 + i + k;
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook">

											<td height=25 align=center>
												&nbsp;
												<font color='blue'><%=j%></font>
											</td>
											<td height=25 align=center>&nbsp;<span id="show<%=j%>"><span onclick="showDetail('show<%=j%>','group<%=j%>')">&nbsp;&nbsp;+&nbsp;&nbsp;</span> </span><%=strategyIp.getIp()%></td>
											<td height=25 align=center>
												&nbsp;�Ϲ�
											</td>
											<td height=25 align=center>
												��ͨ:0&nbsp;&nbsp;��Ҫ:0&nbsp;&nbsp;����:0
											</td>

											<td height=25 align=center><%=correct%>
												<%=count%></td>
											<td height=25 align=center><%=vo.getCheckTime()%></td>

										</tr>
										<tr bgcolor="#FFFFFF">
											<td colspan=6>
												<div id="group<%=j%>" style="display: none;">
													<table>

														<tr>
															<td nowrap align="center" height="32" width="20%">
																&nbsp;<%=correct%>&nbsp; �Ϲ�Ĺ���
															</td>
															<td height="32" width="80%" align="left">
																&nbsp;&nbsp;
															</td>

														</tr>
														<%
															CheckResultDao resultDao2 = new CheckResultDao();
																		List list = resultDao2.getReslutByIdAndIp(id + "",
																				strategyIp.getIp());
																		if (list != null && list.size() > 0) {
																			boolean flag1 = true;
																			for (int k1 = 0; k1 < list.size(); k1++) {
																				String level = "";
																				String levelStr = "";
																				CompCheckResultModel model = (CompCheckResultModel) list
																						.get(k1);

																				if (model.getViolationSeverity() == 2) {
																					level = "<img src='/afunms/img/urgency.gif'>";
																					levelStr = "���ص�";
																				} else if (model.getViolationSeverity() == 1) {
																					level = "<img src='/afunms/img/serious.gif'>";
																					levelStr = "��Ҫ��";
																				} else if (model.getViolationSeverity() == 0) {
																					level = "<img src='/afunms/img/common.gif'>";
																					levelStr = "��ͨ��";
																				}
														%>
														<tr>
															<td nowrap align="center" height="32" width="10%">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=level%><%=levelStr%></td>
															<td height="32" width="90%" align="left">
																&nbsp;&nbsp;
																<a href="#"
																	onclick="showRule('<%=model.getStrategyId()%>','<%=model.getGroupId()%>','<%=model.getRuleId()%>','<%=model.getIp()%>','1')"><%=model.getRuleName()%></a>
																[<%=model.getGroupName()%>]-
																<%=model.getDescription()%></td>

														</tr>
														<%
															}
																		}
														%>
													</table>

												</div>
											</td>
										</tr>
										<%
											}
												}

											}
											StrategyIpDao ipDao = new StrategyIpDao();
											List deviceList = ipDao
													.findByCondition(" where AVAILABILITY=0 and STRATEGY_ID="
															+ id);
											if (deviceList != null && deviceList.size() > 0) {

												int k = 0;
												for (int i = 0; i < deviceList.size(); i++) {
													StrategyIp vo1 = (StrategyIp) deviceList.get(i);
													if (checkList != null && j == 0)
														k = checkList.size();
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook">

											<td height=25 align=center>

												<font color='blue'><%=1 + i + k + j%></font>
											</td>
											<td height=25 align=center><%=vo1.getIp()%></td>
											<td height=25 align=center>
												���ݲ�����
											</td>
											<td height=25 align=center>
												[������]
											</td>
											<td height=25 align=center>
												[������]
											</td>
											<td height=25 align=center>
												--NA--
											</td>

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
	</BODY>
</HTML>
