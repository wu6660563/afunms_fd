<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>


<%
  String rootPath = request.getContextPath();
  List<UpAndDownMachine> machineList = (List<UpAndDownMachine>)request.getAttribute("machineList");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";
  function toAddCluster(){
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=delOrAdd&type=1";
     mainForm.submit();
  }
function toMovement(){
  	var path = "<%=rootPath%>/serverUpAndDown.do?action=to_movement";
  	<%
  		if(machineList.size() > 0){
  			System.out.println(")))))))))))))))))))))))))))IPADDR(((((((((((((((((((");
    		for(int i=0;i<machineList.size();i++){
       				UpAndDownMachine vo = machineList.get(i);
       				System.out.println("(((((((((((((((((((((((((((((IPADDR))))))))))))))))))))))))))"+vo.getIpaddress());
       		}
       	}
  		request.setAttribute("showMachineList",machineList); 
  	%>
  	window.open (path, '��̬Ч��', 'height=330, width=400, top=110,left=220, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
  }
  function toStatic(){
  	var path = "<%=rootPath%>/serverUpAndDown.do?action=to_static";
  	window.open (path, '��̬Ч��', 'height=330, width=400, top=110,left=220, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
  }
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=delete";
     mainForm.submit();
  } 
  function toRefresh()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=refresh";
     mainForm.submit();
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
	function reboot()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=reboot&value=1&id="+node;
	}
	function shutdown()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=shutdown&value=1&id="+node;
	}
	function shutdown()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=shutdown&value=1&id="+node;
	}
	function clickMenu()
	{
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

	<body id="body" class="body" onload="initmenu();" leftmargin="0"
		topmargin="0">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.reboot()">��������</td>
		</tr>		
	   <tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.shutdown()">�رշ�����</td>
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
                    <td class="layout_title">Ӧ�� >> ������� >> Զ�̿��ػ�����</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
					           <!-- <a href="#" onclick="toAddCluster()">��ӵ���Ⱥ</a>  -->
									<a href="#" onclick="toStatic()">��̬Ч��</a>
					           			<a href="#" onclick="toMovement()">��̬Ч��</a>
									<a href="#" onclick="toAdd()">���</a>
									<a href="#" onclick="toDelete()">ɾ��</a>
									<a href="#" onclick="toRefresh()">ˢ��</a>
		  						</td>
									</tr>
        								</table>
										</td>
        						</tr>						
				
		<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
					<th width='5%' class="body-data-title">&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    					<th width='5%' class="body-data-title">���</th>
      					<th width='15%' class="body-data-title">����</th>
      					<th width='10%' class="body-data-title">IP��ַ</th>	
      					<th width='10%' class="body-data-title">����������</th>
      					<th width='17%' class="body-data-title">Զ�̻���״̬</th> 
      					<th width='17%' class="body-data-title">���һ�β���ʱ��</th>    
      					<th width='16%' class="body-data-title">������������</th>    
    					<th width='5%' class="body-data-title">����</th>
</tr>
<%
	if(machineList.size() > 0){
    		for(int i=0;i<machineList.size();i++){
       				UpAndDownMachine vo = machineList.get(i);
       				int status = 0;
       				Node node = (Node)PollingEngine.getInstance().getFtpByID(vo.getId());
       				String alarmmessage = "";
       				if(node != null){
       					status = node.getStatus();
       					System.out.println("poll status===="+status); 
       					List alarmlist = node.getAlarmMessage();
       					if(alarmlist!= null && alarmlist.size()>0){
						for(int k=0;k<alarmlist.size();k++){
							alarmmessage = alarmmessage+alarmlist.get(k).toString();
						}
					}
       				}  
       				System.out.println("ftp status===="+status);  
       
%>
       <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook">
    		<td height=25 align=center>&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    		<td height=25 align=center>&nbsp;<font color='blue'><%=1 + i%></font></td>
		<td height=25 align=center>&nbsp;<%=vo.getName()%></td> 
		<td height=25 align=center>&nbsp;<%=vo.getIpaddress()%></td> 
		<td height=25 align=center> &nbsp;<%=vo.getServerType()%></td> 
			<%
		int monitorStatus = vo.getMonitorStatus();
		if(monitorStatus == 0)
		{
		 %>
		<td height=25 align=center>&nbsp;������δ��⵽Զ�˿ͻ���</td>
		<%
		}
		else
		{
		 %> 
		 <td height=25 align=center>&nbsp;�����</td>
		 <%
		 }
		  %>
		<td height=25 align=center><%=vo.getLasttime() %></td>	
		<td height=25 align=center><span id="name<%=vo.getId() %>"><%=vo.getClusterName() %></span>&nbsp;<span title='�޸�������������'><a href="#" onclick="openNewWindow(<%=vo.getId() %>)"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0></a></span></td>
		<td height=25 align=center>&nbsp;
		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getName()%>') alt="�Ҽ�����">
		</td>
  	</tr>
<%	}
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
