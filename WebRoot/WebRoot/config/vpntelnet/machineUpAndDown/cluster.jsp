<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="java.util.HashMap"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>


<%
	String rootPath = request.getContextPath();
	List<Cluster> machineList = (List<Cluster>) request.getAttribute("clusterList");
	HashMap<Integer, Integer> totalMap = (HashMap<Integer, Integer>) request.getAttribute("totalMap");
	String isOper = "";
	String menuTable = (String) request.getAttribute("menuTable");
%>
<%
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/table.js"></script>
		<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
		<script language="JavaScript" type="text/javascript">
  
  			function add()
  			{
     			mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=ready_addCluster";
      			mainForm.submit();
  			}
  			function deleteCluster()
  			{
    			mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=deleteCluster";
     			mainForm.submit();
  			}  
  			function refreshCluster()
  			{
     			mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=clusterList";
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
	        		if(hide)
	        		{
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
	        rowObjs[i].cells[0].onmouseout=function()
	        {
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
	
		function editCluster()
  {
    location.href="<%=rootPath%>/serverUpAndDown.do?action=editCluster&id="+node;
  }   
	
	function reboot()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=reboot&value=1&id="+node;
	}
	
	//�ر�
	function shutdown()
	{
	   
		location.href="<%=rootPath%>/serverUpAndDown.do?action=shutdown&value=1&id="+node;
	}
	function toMovement(){
  		
  		var path = "<%=rootPath%>/serverUpAndDown.do?action=to_movement&id="+node+"&nowtime="+(new Date());
  		<%
  		request.setAttribute("showMachineList",machineList); 
  		%>
  		window.open (path, '��̬Ч��', 'height=330, width=400, top=110,left=220, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
  	}
	//�رռ�Ⱥ
	function shutdownAll(){
	toMovement();
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=shutdownAll&clusterId="+node+"&nowtime="+(new Date()),
			success:function(obj){
				alert("ָ���ѷ��������Ժ�...");
			}
		});
	
	}
	
	//������Ⱥ
	function rebootAll(){
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=rebootAll&clusterId="+node+"&nowtime="+(new Date()),
			success:function(obj){
				alert("ָ���ѷ��������Ժ�...");
			}
		});
	
	}
	function updateSequence(name,obj){
	
	var value=obj.options[obj.selectedIndex].text;
	
	}
	//���ӵ��ض������������ϸ�б�
	function toDetailList(id){
	location.href="<%=rootPath%>/serverUpAndDown.do?action=clusterDetailList&id="+id;
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
					<td style="cursor: default; border: outset 1;" align="center" onclick="parent.editCluster()">�޸���Ϣ</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center" onclick="parent.rebootAll()">������Ⱥ</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center" onclick="parent.shutdownAll()">�رռ�Ⱥ</td>
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
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
												<table width="100%" cellspacing="0" cellpadding="0">
													<tr>
														<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
														<td class="layout_title">Ӧ�� >> ������� >> Զ�̷������鿪�ػ�����</td>
														<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table width="100%" cellpadding="0" cellspacing="0">
													<tr>
														<td bgcolor="#ECECEC" width="50%" align='right'>
															<a href="#" onclick="add()">������</a>
															<a href="#" onclick="deleteCluster()">ɾ����</a>&nbsp;&nbsp;&nbsp;
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<table cellspacing="1" cellpadding="0" width="100%" id="flextb">
													<tr class="microsoftLook0" height=28>
														<th width='6%' class="body-data-title">&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
														<th width='12%' class="body-data-title">���</th>
														<th width='24%' class="body-data-title">����</th>
														<th width='20%' class="body-data-title">����������</th>
														<th width='10%' class="body-data-title">����������</th>
														<th width='20%' class="body-data-title">����ʱ��</th>
														<th width='10%' class="body-data-title">����</th>
													</tr>
													<%
														StringBuffer items = new StringBuffer();
														if (machineList.size() > 0) {
															for (int i = 0; i < machineList.size(); i++) {
																int j = 0;
																Cluster vo = machineList.get(i);
													%>
													<tr bgcolor="#FFFFFF" class="microsoftLook"
														id="line<%=1 + i%>">
														<%
															// if(vo.getId()==1){ 
																	// isOper="disabled='disabled'";
																	// }else{
																	// isOper="";
																	//  }
																	int id = vo.getId();
																	int count = 0;
																	if (totalMap.containsKey(id)) {
																		count = totalMap.get(id);
																	}
														%>
														<td height=25 align=center>
															&nbsp;
															<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
														</td>
														<td height=25 align=center>
															&nbsp;<%=i + 1%></td>
														<td height=25 align=center>
															&nbsp;
															<a href="#" onclick="toDetailList(<%=id%>)"><%=vo.getName()%></a>
														</td>
														<td height=25 align=center>
															&nbsp;<%=vo.getServerType()%></td>
														<td height=25 align=center>
															&nbsp;<%=count%></td>
														<td height=25 align=center><%=vo.getCreatetime()%></td>
														<td height=25 align=center>
															&nbsp;
															<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getName()%>') alt="�Ҽ�����">
														</td>
													</tr>
													<%
														}
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
