<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.SystemConstant"%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.topology.dao.NetSyslogNodeRuleDao"%>
<%@page import="com.afunms.topology.model.NetSyslogNodeRule"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int rc = list.size();
String actionlist=(String)request.getAttribute("actionlist");
  JspPage jp = (JspPage)request.getAttribute("page");
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
		
		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/network.do?action=delete";
  var listAction = "<%=rootPath%>/network.do?action=<%=actionlist%>";
   function toListByNameasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbynameasc";
     mainForm.submit();
  }
  function toListByNamedesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbynamedesc";
     mainForm.submit();
  }
   function toListByIpasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbyipasc";
     mainForm.submit();
  }
  function toListByIpdesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbyipdesc";
     mainForm.submit();
  }
  function toListByBrIpasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbybripasc";
     mainForm.submit();
  }
  function toListByBrIpdesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbybripdesc";
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
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
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
  function toEditBid(){
     var bidnum = document.getElementsByName("checkbox").length;
     var k=0;
     for(var p = 0 ; p <bidnum ; p++){
		 if(document.getElementsByName("checkbox")[p].checked ==true){
			 k=k+1;
			 }
	}
     if(k==0){
		 alert ("��ѡ���豸��");
		 }
     else {
		 
		 //alert("ѡ�����豸��Ϊ"+k);
		 mainForm.target="editall";
		 window.open("","editall","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		 mainForm.action='<%=rootPath%>/netsyslogalarm.do?action=editall';
		 mainForm.submit();
		
		
		 }

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
	function showMenu(id,nodeid,ip,showItemMenu)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        return false;
	    }
	    else{
	    	popMenu(itemMenu,100,showItemMenu);
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    
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
	            this.style.background="#99CCFF";
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
	    pop.show(event.clientX-1,event.clientY,width,rowCount*30,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	}
	function edit()
	{
		//location.href="<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid="+node;
		window.open('<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid='+node);
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menucancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menuaddmanage&id="+node;
	}
	function cancelendpoint()
	{
		location.href="<%=rootPath%>/network.do?action=menucancelendpoint&id="+node;
	}
	function addendpoint()
	{
		location.href="<%=rootPath%>/network.do?action=menuaddendpoint&id="+node;
	}
	function setCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=setCollectionAgreement&id="+node + "&endpoint="+endpoint;
	}
	function deleteCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=deleteCollectionAgreement&id="+node + "&endpoint="+endpoint;
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
				style="border: thin;font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						����
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
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b>�澯 >> SYSLOG���� >> �澯�����б�</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
							<tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
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
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="70%" align='right'>
				
												<a href="#" onclick="toEditBid()" align="right"><b>��������</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											
											</td>
										</tr>
								</table>
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
															<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>���
														</th>
														<th width='10%'>
															<table width="100%" height="100%">
																<tr>
																	<td width="85%" align='center' style="padding-left:6px;font-weight:bold;"><a href="#" onclick="toListByNameasc()">����</a></td>
																	<td>
																		<img id="nameasc" src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																			border="0" onclick="toListByNameasc()" style="CURSOR:hand;margin-left:4px;" />
																			<img id="namedesc" src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																			border="0" onclick="toListByNamedesc()" style="CURSOR:hand;margin-left:4px;" />
																		
																	</td>
																</tr>
															</table>
														</th>
														<th width='10%'>
															<table width="100%" height="100%">
																<tr>
																	<td width="85%" align='center' style="padding-left:6px;font-weight:bold;"><a href="#" onclick="toListByIpasc()">IP��ַ</a></td>
																	<td>
																		<img id="ipasc" src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																			border="0" onclick="toListByIpasc()" style="CURSOR:hand;margin-left:4px;" />
																			<img id="ipasc" src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																			border="0" onclick="toListByIpdesc()" style="CURSOR:hand;margin-left:4px;" />
																		
																	</td>
																</tr>
															</table>
														</th>
														<th width='10%'>
															�ͺ�
														</th>
														<th width='60%'>
															���˹���
														</th>
														<th width='5%'>
															����
														</th>
													</tr>
													<%
														HostNode vo = null;
														int startRow = jp.getStartRow();
														for (int i = 0; i < rc; i++) {
															String showItemMenu = "";
															vo = (HostNode) list.get(i);
															if(vo.getCategory()!=4&&vo.getEndpoint()==0){
																showItemMenu = "111111000";
															}else if(vo.getEndpoint()!=0){
																showItemMenu = "111110001";
															}else{
																showItemMenu = "111111110";
															}
															String mac="--";
															if(vo.getBridgeAddress() != null && !vo.getBridgeAddress().equals("null"))mac = vo.getBridgeAddress();
													%>
													<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>
														<td>
															<INPUT type="checkbox" class=noborder name=checkbox
																value="<%=vo.getId()%>" class=noborder>
															<font color='blue'><%=startRow + i%>
															</font>
														</td>
														<!--<a href="<%=rootPath%>/network.do?action=read&id=<%=vo.getId()%>">-->
														<td align='center'>
															<%=vo.getAlias()%>
														</td>
														<!--</a>-->
														<td align='center'>
															<%=vo.getIpAddress()%>
														</td>
														<td align='center'>
															<%=vo.getType()%>
														</td>
														<%
													
														List flist = new ArrayList();
														NetSyslogNodeRuleDao ruledao = new NetSyslogNodeRuleDao();
																NetSyslogNodeRule logrule = (NetSyslogNodeRule)ruledao.findByID(String.valueOf(vo.getId()));
																ruledao.close();
                                                                if(logrule != null ){
																	
																	String facility = logrule.getFacility();
																
																	String[] facilitys = facility.split(",");
																	
																	if(facilitys != null && facilitys.length>0){
																		for(int a = 0;a<facilitys.length;a++){
																			flist.add(facilitys[a]);
																		}
																	}
																}

														    String str0="";
															String str1="";
															String str2="";
															String str3="";
															String str4="";
															String str5="";
															String str6="";
															String str7="";
															String alarmstr="";
                                                        if(flist != null && flist.size()>0){
	                                                    for(int j=0;j<flist.size();j++){
		                                                if("0".equals(flist.get(j))) {
															str0="����";
														}else if("1".equals(flist.get(j))) {
															str1="����";
														}else if("2".equals(flist.get(j))) {
															str2="�ؼ�";	
														}else if("3".equals(flist.get(j))) {
															str3="����";
														}else if("4".equals(flist.get(j))) {
															str4="����";	
														}else if("5".equals(flist.get(j))) {
															str5="֪ͨ";
														}else if("6".equals(flist.get(j))) {
															str6="��ʾ";	
														}else if("7".equals(flist.get(j))) {
															str7="����";								
														}
													}
                                                    alarmstr=str0+"  "+str1+"  "+str2+"  "+str3+"  "+str4+"  "+str5+"  "+str6+"  "+str7;
												}
														%>
														<td align='center'>
															<%=alarmstr%>
														</td>
														<td>
															&nbsp;&nbsp;
															<img src="<%=rootPath%>/resource/image/status.gif"
																border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpAddress()%>','<%=showItemMenu%>') alt="�Ҽ�����">
															<!--<a href="<%=rootPath%>/network.do?action=ready_edit&id=<%=vo.getId()%>">
												<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a>-->


														</td>
													</tr>
													<%
													}
													%>
												</table>
											</td>
										</tr>
								<tr>
					              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                  <tr>
					                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
					                    <td></td>
					                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
					                  </tr>
					              </table></td>
					            </tr>	
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
