<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.event.model.EventList"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	List list = (List)request.getAttribute("list");
	List businessList = (List)request.getAttribute("businessList");
	String subtype = (String)request.getAttribute("subtype");
	String level1 = (String)request.getAttribute("level1");
	String managesign = (String)request.getAttribute("managesign");
	String businessid = (String)request.getAttribute("businessid");
	Hashtable hashtable = (Hashtable)request.getAttribute("hashtable");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");
	JspPage jp = (JspPage)request.getAttribute("page");
	String ipaddress = (String)request.getAttribute("ipaddress");
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<LINK href="<%=rootPath%>/resource/css/global/global.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<script language="javascript">	
	var curpage= <%=jp.getCurrentPage()%>;
  	var totalpages = <%=jp.getPageTotal()%>;
  	var delAction = "<%=rootPath%>/event.do?action=delete";
  	var listAction = "<%=rootPath%>/event.do?action=summary"; 
 

function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/event.do?action=accit";	
	mainForm.submit();
}

  function query()
  {  
     mainForm.action = "<%=rootPath%>/event.do?action=summary&jp=1";
     mainForm.submit();
  }  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
  function showDetails(nodeId , subentity , subtype , level1){
  	document.getElementById("subtype").value=subtype;
  	document.getElementById("level1").value=level1;
  	document.getElementById("businessid").value="<%=businessid%>";
  	document.getElementById("level1").value=level1;
  	document.getElementById("managesign").value="<%=managesign%>";
  	
  	mainForm.action = "<%=rootPath%>/event.do?action=showDetails&jp=1&nodeId=" + nodeId +"&subentity=" + subentity;
     mainForm.submit();
  }
  
  function showNodeDetails(nodeId){
  	mainForm.action = "<%=rootPath%>/detail/dispatcher.jsp?id=" + nodeId+"&fromtopo=true";
     mainForm.submit();
  }
  
  
  
  
 

 
  
    function doDelete()
  {  
     mainForm.action = "<%=rootPath%>/event.do?action=dodelete";
     mainForm.submit();
  }  
  
// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
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
	
	initValue();

}

function initValue(){
  	document.getElementById("subtype").value="<%=subtype%>";
  	document.getElementById("level1").value="<%=level1%>";
  	document.getElementById("businessid").value="<%=businessid%>";
  	document.getElementById("managesign").value="<%=managesign%>";
  }

</script>

<script>
	
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
							<td id="td-container-main-content" class="td-container-main-content">
								<table id="container-main-content" class="container-main-content">
									<tr>
										<td>
											<table id="content-header" class="content-header">
							                	<tr>
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
								                	<td class="content-title">&nbsp;�澯 >> �澯��� >> �澯ͳ�� </td>
								                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
								       			</tr>
								        	</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="content-body" class="content-body">
												<tr>
													<td >
														<table>
															<tr >
																<td  class="detail-data-body-title">
																	<table>
																		<tr>
																			<td width="75%">&nbsp;</td>
																			<td width="15" bgcolor=red height=15 >&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;�����澯</td>
																			<td width="15" bgcolor=orange height=15>&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;���ظ澯</td>
																			<td width="15" bgcolor=yellow height=15>&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;��ͨ�澯&nbsp;&nbsp;</td>
																		</tr>
																	</table>
						  										</td>
															</tr>
														</table>
			  										</td>
												</tr>
												<tr>
													<td>
														<table>
															<tr>
																<td class="detail-data-body-title" style="text-align: left;">
																	&nbsp;��ʼ����
																	<input type="text" name="startdate" value="<%=startdate%>" size="10">
																	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																	<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																
																	��ֹ����
																	<input type="text" name="todate" value="<%=todate%>" size="10"/>
																	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																	<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																</td>
															</tr>
														</table>
							  						</td>                       
					        					</tr>
					        					<tr>
					        						<td>
														<table>
															<tr>
																<td  class="detail-data-body-title" style="text-align: left;">	
																	&nbsp;�¼�����
																	<select name="subtype">
																	<option value="value">����</option>
																	<option value="db" >db</option>
																	<option value="host" >host</option>
																	<option value="network" >network</option>
																	<option value="firewall" >firewall</option>
																	</select>
																	�¼��ȼ�
																	<select name="level1">
																	<option value="-1">����</option>
																	<option value="0" >��ʾ��Ϣ</option>
																	<option value="1" >��ͨ�¼�</option>
																	<option value="2" >�����¼�</option>
																	<option value="3" >�����¼�</option>
																	</select>
																	ҵ������
																	<select name="businessid">
																	<option value="-1">����</option>
																	<%
																		for(int i = 0 ; i < businessList.size(); i++){
																			Business business = (Business)businessList.get(i);
																			%>
																				<option value="<%=business.getId()%>"><%=business.getDescr()%></option>
																			<%
																		} 
																	%>
																	</select>
																	����״̬
																	<select name="managesign">
																	<option value="-1">����</option>
																	<option value="0" >δȷ��</option>
																	<option value="1" >��ȷ��</option>
																	<option value="2" >�Ѵ���</option>
																	</select>
																	IP��ַ��<input type="text" name="ipaddress" id="ipaddress" value="<%=ipaddress%>">
																	<input type="button" name="submitss" value="��ѯ" onclick="query()">
																	<INPUT type="button" class="formStyle" value="ʵʱ" onclick="window.open('<%=rootPath %>/event.do?action=todaylist&jsp=1')">
																</td>
															</tr>
															</table>
							  						</td>                       
					        					</tr>
					        					<tr>
													<td>
														<table>
															<tr>
																<td class="detail-data-body-title">
											    					<jsp:include page="../../common/page.jsp">
											     						<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
											     						<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
											   						</jsp:include>
											          			</td>
											        		</tr>
														</table>
													</td>
												</tr> 
					        					<tr>
													<td>
														<table>
															<tr>
																<td class="detail-data-body-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">���</td>
        														<td class="detail-data-body-title">�ȼ�</td>
        														<td class="detail-data-body-title">�澯��Դ</td>
														    	<td class="detail-data-body-title">�澯����</td>
														    	<td class="detail-data-body-title">�澯����</td>
														    	<td class="detail-data-body-title">����ʱ��</td>
														    	<td class="detail-data-body-title">�鿴�澯����</td>
															</tr>
															<%
																if(list!=null && list.size()>0){
																	for(int i = 0 ; i < list.size(); i++){
																			List tempList = (List)list.get(i);
																			EventList eventList = (EventList)tempList.get(0);
																			
																			String subtype_tmp = eventList.getSubtype();
																			
																			int nodeid_tmp = eventList.getNodeid();
																			
																			
																			// ���� ��ת�� dispatcher.jsp ��id  ��ʾ��ϸ��Ϣ
																			
																			// see  db --> nms_manage_nodetype
																			
																			String dispatcher_id = "";
																			
																			if("host".equals(subtype_tmp)){
																				dispatcher_id = "net" + nodeid_tmp;
																			}else if("network".equals(subtype_tmp) || "net".equals(subtype_tmp)){
																				dispatcher_id = "net" + nodeid_tmp;
																			}else if("db".equals(subtype_tmp)){
																				dispatcher_id = "dbs" + nodeid_tmp;
																			}else if (subtype_tmp.equalsIgnoreCase("bus")) {
																				dispatcher_id = "bus" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("web")) {
																			
																			} else if (subtype_tmp.equalsIgnoreCase("mail")) {
																				dispatcher_id = "mai" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("wasserver")) {
																				dispatcher_id = "was" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("grapes")) {
																				
																			} else if (subtype_tmp.equalsIgnoreCase("radar")) {
																				
																			} else if (subtype_tmp.equalsIgnoreCase("plot")) {
																				
																			} else if (subtype_tmp.equalsIgnoreCase("tomcat")) {
																				dispatcher_id = "tom" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("ftp")) {
																				dispatcher_id = "ftp" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("mq")) {
																				dispatcher_id = "mqs" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("domino")) {
																				dispatcher_id = "dom" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("iis")) {
																				dispatcher_id = "iis" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("socket")) {
																				dispatcher_id = "soc" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("cics")) {
																				dispatcher_id = "cic" + nodeid_tmp;
																			} else if (subtype_tmp.equalsIgnoreCase("weblogic")) {
																				dispatcher_id = "web" + nodeid_tmp;
																			}
																			
																			String level = String.valueOf(eventList.getLevel1());
																			String bgcolor = "";
																			if("0".equals(level)){
																		  		level="��ʾ��Ϣ";
																		  		bgcolor = "bgcolor='blue'";
																		  	}
																			if("1".equals(level)){
																		  		level="��ͨ�澯";
																		  		bgcolor = "bgcolor='yellow'";
																		  	}
																		  	if("2".equals(level)){
																		  		level="���ظ澯";
																		  		bgcolor = "bgcolor='orange'";
																		  	}
																		  	if("3".equals(level)){
																		  		level="�����澯";
																		  		bgcolor = "bgcolor='red'";
																		  	}
																		%>
																		<tr <%=onmouseoverstyle%>>
																			<td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox"><%=jp.getStartRow()+i%></td>
			        														<td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
			        														<td class="detail-data-body-list"><a href="#"  onclick='showNodeDetails("<%=dispatcher_id%>")'><%=hashtable.get(eventList.getId()+"")%></a></td>
																	        <td class="detail-data-body-list"><%=eventList.getContent()%></td>
																	    	<td class="detail-data-body-list"><%=tempList.get(1)%></td>
																	    	<td class="detail-data-body-list"><%=tempList.get(2)%></td>
																	    	<td class="detail-data-body-list"><a href="#"  onclick='showDetails("<%=eventList.getNodeid()%>","<%=eventList.getSubentity()%>","<%=eventList.getSubtype()%>","<%=eventList.getLevel1()%>" )'>�鿴�澯����</a></td>
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
									<tr>
										<td>
											<table id="content-footer" class="content-footer">
												<tr>
													<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
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
		
	</form>
</BODY>
</HTML>
