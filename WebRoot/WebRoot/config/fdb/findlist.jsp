<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacBase"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%@ page import="com.afunms.temp.model.FdbNodeTemp"%>
<%@ include file="/include/globe.inc"%>


<%
	String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");
  String key = (String)request.getAttribute("key");
  String value = (String)request.getAttribute("value");
  System.out.println("key==="+key+"====value:"+value);
  String ipchecked = "";
  String ipaddresschecked = "";
  String macchecked = "";
  String valuestr = "";
  if(value != null)valuestr=value;
  if(key != null && key.equals("ip")){
  	ipchecked = "selected";
  }
  if(key != null && key.equals("ipaddress")){
  	ipaddresschecked = "selected";
  }
  if(key != null && key.equals("mac")){
  	macchecked = "selected";
  }
  String menuTable = (String)request.getAttribute("menuTable");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>


		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/fdb.do?action=delete";
  var listAction = "<%=rootPath%>/fdb.do?action=find";
  
  function doQuery()
  {  
     if(mainForm.value.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/fdb.do?action=find";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }
  function toRefresh()
  {
      mainForm.action = "<%=rootPath%>/fdb.do?action=refresh";
      mainForm.submit();
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  function toDelete()
  {
      mainForm.action = "<%=rootPath%>/fdb.do?action=delete";
      mainForm.submit();
  }
  function toDeleteAll()
  {
      mainForm.action = "<%=rootPath%>/fdb.do?action=deleteall";
      mainForm.submit();
  }
    function toSetBaseline()
  {
      mainForm.action = "<%=rootPath%>/ipmac.do?action=setlistbaseline";
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

	<body id="body" class="body" onload="initmenu();" leftmargin="0"
		topmargin="0">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						�༭
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.cancelmanage()">
						ȡ������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addmanage()">
						��ӹ���
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.cancelendpoint()">
						ȡ��ĩ��
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addendpoint()">
						����Ϊĩ��
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(1)">
						����ΪԶ��Ping������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(3)">
						����Telnet������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(4)">
						����SSH������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.deleteCollectionAgreement(0)">
						ȡ������Э��
					</td>
				</tr>
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content">
										<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											<td class="layout_title"><b>��Դ >> IP/MAC��Դ >> ��ǰFDB��Ϣ</b></td>
											<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td>
												<table width="100%" cellpadding="0" cellspacing="0">
													<tr>
														<td bgcolor="#ECECEC" width="50%" align='left'>
															&nbsp;&nbsp;&nbsp;&nbsp;
															<B>��ѯ:</B>
															<SELECT name="key" style="">
																<OPTION value="ip" <%=ipchecked%>>
																	�����豸IP
																</OPTION>
																<OPTION value="ipaddress" <%=ipaddresschecked%>>
																	ת����ַ
																</OPTION>
																<OPTION value="mac" <%=macchecked%>>
																	MAC
																</OPTION>

															</SELECT>
															&nbsp;
															<b>=</b>&nbsp;
															<INPUT type="text" name="value" width="15"
																class="formStyle" value='<%=valuestr%>'>
															<INPUT type="button" class="formStyle" value="��ѯ"
																onclick=" return doQuery()">
														</td>
														<td bgcolor="#ECECEC" width="50%" align='right'>
															<a href="#" onclick="toRefresh()">ͬ��</a>&nbsp;&nbsp;&nbsp;
															<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
															<a href="#" onclick="toDeleteAll()">ɾ��ȫ��</a>&nbsp;&nbsp;&nbsp;
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
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="80%" align="center">
												<jsp:include page="../../common/page.jsp">
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align=right bgcolor="#ECECEC">
									<a href="<%=rootPath%>/fdb.do?action=downloadfdbreport"
										target="_blank"><img name="selDay1" alt='����EXCEL'
											style="CURSOR: hand"
											src="<%=rootPath%>/resource/image/export_excel.gif" width=18
											border="0">����EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="<%=rootPath%>/fdb.do?action=downloadfdbreportall"
										target="_blank"><img name="selDay1" alt='����EXCEL'
											style="CURSOR: hand"
											src="<%=rootPath%>/resource/image/export_excel.gif" width=18
											border="0">����ȫ��EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;

								</td>
							</tr>

							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
										<tr class="microsoftLook0" height=25>
											<th width='5%' class="body-data-title">
												<INPUT type="checkbox" class=noborder name="checkall"
													onclick="javascript:chkall()">
												���
											</th>
											<th width='15%' class="body-data-title">
												�����豸(ip)
											</th>
											<th width='10%' class="body-data-title">
												����
											</th>
											<th width='10%' class="body-data-title">
												������
											</th>
											<th width='5%' class="body-data-title">
												�˿�
											</th>
											<th width='15%' class="body-data-title">
												ת��IP
											</th>
											<th width='20%' class="body-data-title">
												MAC
											</th>
											<th width='20%' class="body-data-title">
												����ʱ��
											</th>
											<!-- 
      											<th width='10%'>ifband</th>
      											<th width='10%'>ifsms</th>
      											 -->
										</tr>
										<%
											FdbNodeTemp vo = null;
											int startRow = jp.getStartRow();
											session.setAttribute("startRow", startRow);
											session.setAttribute("list", list);
											for (int i = 0; i < rc; i++) {
												vo = (FdbNodeTemp) list.get(i);
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
											<td>
												<INPUT type="checkbox" class=noborder name="checkbox"
													value="<%=vo.getNodeid()%>">
												<font color='blue'><%=startRow + i%></font>
											</td>
											<td align='center' style="cursor: hand"><%=vo.getIp()%></td>
											<td align='center'><%=vo.getType()%></td>
											<td align='center'><%=vo.getSubtype()%></td>
											<td align='center'><%=vo.getBak()%></td>
											<td align='center'><%=vo.getIpaddress()%></td>
											<td align='center'><%=vo.getMac()%></td>
											<td align='center'><%=vo.getCollecttime()%></td>
											<!-- 
    											<td  align='center'><%=vo.getIfband()%></td>
    											<td  align='center'><%=vo.getIfsms()%></td>
    											 -->
										</tr>
										<%
											}
										%>
									</table>
								</td>
							</tr>
										<tr>
											<td>
												<table id="content-footer" class="content-footer">
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
		</form>
	</body>


</html>
