<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	
	String menuTable = (String)request.getAttribute("menuTable");
	
	
	String nodeid = (String)request.getAttribute("nodeid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
	
	List nodelist = (List)request.getAttribute("nodelist");
	
	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
	List<AlarmIndicatorsNode> list = (List<AlarmIndicatorsNode>)request.getAttribute("list");
	Hashtable<String , NodeDTO> nodeDTOHashtable = (Hashtable<String , NodeDTO>)request.getAttribute("nodeDTOHashtable");
	
	JspPage jp = (JspPage)request.getAttribute("page");
    
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getThresholdOperationPermission());
    boolean isCreateOperation = roleOperationPermissionService.isCreateOperationPermission();
    boolean isUpdateOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteOperation = roleOperationPermissionService.isDeleteOperationPermission();
    String createOperationDisable = "inline";
    if (!isCreateOperation) {
        createOperationDisable = "none";
    }
    String updateOperationDisable = "inline";
    if (!isUpdateOperation) {
        updateOperationDisable = "none";
    }
    String deleteOperationDisable = "inline";
    if (!isDeleteOperation) {
        deleteOperationDisable = "none";
    }
    
    String str = (String)request.getAttribute("str");
    if(str == null || str.trim().length()<1){
    	str = "-1";
    }
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
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
			
			
			function toChooseNode(){
				var nodeid = document.getElementById("nodeid");
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" && nodeid.value !="-1"){
					mainForm.action = "<%=rootPath%>/alarmlink.do?action=openOldIndicators&jspFlag=multi";
					mainForm.submit();
				}
				if(type.value=="-1"){
					alert("��ѡ������!");
				}
				if(subtype.value=="-1"){
					alert("��ѡ��������!");
				}
				if(nodeid.value=="-1"){
					alert("��ѡ���豸!");
				}
				
			}
			
			function search(){
				mainForm.action = "<%=rootPath%>/alarmlink.do?action=openOldIndicators";
				mainForm.submit();
			}
			
			// �����Ƿ����ظ�ѡ��ĸ澯��Ϣ
			function checkSameAlarmID(newid) {
				var old_alarm_ids = window.opener.document.getElementsByName("alarmid");
				//alert(newid +":"+ old_alarm_ids.length);
				if(old_alarm_ids != null){
					for(var i=0; i<old_alarm_ids.length; i++){
						//alert(old_alarm_ids[i].value);
						if(newid == old_alarm_ids[i].value) {
							alert("�����ظ�����Ѿ�ѡ��ĸ澯��������ѡ�񣡣���");
							return false;
						}
					}
				}
				return true;
			}
			
			// ȷ���ύѡ��
			function sure() {
				
				var oids ="";
				var formItem=document.forms["mainForm"];
				var formElms=formItem.elements;
				var l=formElms.length;
				var rowNum = 0;
				var str = "" + rowNum;
				
				while(l--){
					
					if(formElms[l].type=="radio"){
						
						var checkbox=formElms[l];
						if(checkbox.name == "checkbox" && checkbox.checked==true){
						//alert(checkbox.name + ":" + checkbox.value);
			 				if (oids==""){
			 					oids=checkbox.value;
			 				}else{
			 					oids=oids+","+checkbox.value;
			 				}
			 				var str_ = "<%=str%>";
			 				//alert("ip-"+str_);
			 				//var par_ipaddress = parent.opener.document.getElementById("ip-"+str_);
			 				//alert(""+par_ipaddress);
			 				var ipaddress = document.getElementById("ip-" + checkbox.value).innerHTML;
			 				var deviceName = document.getElementById("name-" + checkbox.value).innerHTML;
			 				var indicatorName = document.getElementById("indi-" + checkbox.value).innerHTML;
			 				var descr = document.getElementById("descr-" + checkbox.value).innerHTML;
			 				var compare = document.getElementById("compare-" + checkbox.value).innerHTML;
			 				var limen0 = document.getElementById("limen0-" + checkbox.value).innerHTML;
			 				var limen1 = document.getElementById("limen1-" + checkbox.value).innerHTML;
			 				var limen2 = document.getElementById("limen2-" + checkbox.value).innerHTML;
			 				var time0 = document.getElementById("time0-" + checkbox.value).innerHTML;
			 				var time1 = document.getElementById("time1-" + checkbox.value).innerHTML;
			 				var time2 = document.getElementById("time2-" + checkbox.value).innerHTML;
			 				
			 				var alarmid = checkbox.value;
			 				if(!checkSameAlarmID(alarmid)){
			 					return ;
			 				}
			 				
			 				var compare = document.getElementById("compare-hidden-" + checkbox.value).value;
			 				var deviceid = document.getElementById("deviceid-hidden-" + checkbox.value).value;
			 				var sms0 = document.getElementById("sms0-hidden-" + checkbox.value).value;
			 				var sms1 = document.getElementById("sms1-hidden-" + checkbox.value).value;
			 				var sms2 = document.getElementById("sms2-hidden-" + checkbox.value).value;
			 				var way0 = document.getElementById("way0-hidden-" + checkbox.value).value;
			 				var way1 = document.getElementById("way1-hidden-" + checkbox.value).value;
			 				var way2 = document.getElementById("way2-hidden-" + checkbox.value).value;
			 				
			 			
			 			
			 				var par_ip = window.opener.document.getElementById("ip-"+str_);	// ip
			 				var par_name = window.opener.document.getElementById("name-"+str_); //�豸����
			 				var par_indiName = window.opener.document.getElementById("indiName-"+str_); // ��ֵ��
			 				var par_descr = window.opener.document.getElementById("descr-"+str_); // ����
			 				var par_limen0 = window.opener.document.getElementById("limen0-"+str_); // һ����ֵ
			 				var par_limen1 = window.opener.document.getElementById("limen1-"+str_); // ������ֵ
			 				var par_limen2 = window.opener.document.getElementById("limen2-"+str_); // ������ֵ
			 				var par_time0 = window.opener.document.getElementById("time0-"+str_); // ����
			 				var par_time1 = window.opener.document.getElementById("time1-"+str_); // ����
			 				var par_time2 = window.opener.document.getElementById("time2-"+str_); // ����
			 				
			 				// ������Ϣ
			 				var par_deviceid = window.opener.document.getElementById("deviceid-"+str_); // �豸id
			 				var par_compare = window.opener.document.getElementById("compare-"+str_); // ��ֵ�ȽϷ�ʽ
			 				var par_alarmid = window.opener.document.getElementById("alarmid-"+str_); // ��ֵ�ȽϷ�ʽ
			 				var par_alarmids = window.opener.document.getElementById("alarmids-"+str_); // ��ֵ�ȽϷ�ʽ
			 				var par_sms0 = window.opener.document.getElementById("sms0-"+str_); // һ���澯
			 				var par_sms1 = window.opener.document.getElementById("sms1-"+str_); // ����
			 				var par_sms2 = window.opener.document.getElementById("sms2-"+str_); // ����
			 				var par_way0 = window.opener.document.getElementById("way0-"+str_); // ����
			 				var par_way1 = window.opener.document.getElementById("way1-"+str_); // ����
			 				var par_way2 = window.opener.document.getElementById("way2-"+str_); // ����
			 				
			 				
			 				
		 					par_ip.value = ipaddress;
		 					par_name.value = deviceName;
		 					par_indiName.value = indicatorName;
		 					par_descr.value = descr;
		 					par_limen0.value = limen0;
		 					par_limen1.value = limen1;
		 					par_limen2.value = limen2;
		 					par_time0.value = time0;
		 					par_time1.value = time1;
		 					par_time2.value = time2;
		 					par_deviceid.value = deviceid;
		 					par_compare.value = compare;
		 					par_alarmid.value = alarmid;
		 					par_alarmids.value = alarmid;
		 					par_sms0.value = sms0;
		 					par_sms1.value = sms1;
		 					par_sms2.value = sms2;
		 					par_way0.value = way0;
		 					par_way1.value = way1;
		 					par_way2.value = way2;
		 					
		 					rowNum++;
		 				}
					}
				}
				//var event = parent.opener.document.getElementById("");
 				//event.value=oids;      
				window.close();
			}
			
			  
			
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/alarmlink.do?action=showDelete";
  			var listAction = "<%=rootPath%>/alarmlink.do?action=openOldIndicators";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		
		<form id="mainForm" method="post" name="mainForm">
		
			<input type="hidden" id="ip_from_par" />
			<input type="hidden" id="str" name="str" value="<%=str%>"/><!--  -->
			
		
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-add">
									<table id="container-main-add" class="container-main-add">
										<tr>
											<td>
												<table id=add-content class="add-content">
													
								                	<tr>
									           		<td>
									           			<table id="content-header" class="content-header">
									           				<tr >
									           					<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">ѡ��Ҫ��ӵĸ澯��</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									
									           				</tr>
									           			</table>
									           		</td>
									       			</tr>
									       			
									       			<tr align="right" height=18>
														<td >
															<input type="submit" value="ȷ ��" name="delbutton" class=button onclick="return sure()">
														
															<input id="cancle" name="cancle" type="button" onclick="window.close()" class="button" value="�رմ���" ></input>
														</td>
													</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
													<tr>
														<td>
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: left;">
																		&nbsp;&nbsp;<b>���ͣ�</b>
																		<select id="type" name="type" style="width:150px" onchange="changeType()">
																			<option value="-1">����</option>
																			<option value="host">������</option>
																			<option value="net">�����豸</option>
																			<option value="firewall">����ǽ</option>
																			<option value="gateway">�ʼ���ȫ����</option>
																			<option value="atm">ATM</option>
																			<option value="f5">F5</option>
																			<option value="db">���ݿ�</option>
																			<option value="middleware">�м��</option>
																			<option value="service">����</option>
																			<option value="ups">UPS</option>
																			<option value="air">�յ�</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<b>�����ͣ�</b>
																		<select id="subtype" name="subtype" style="width:150px" onchange="changeNode()">
																			<option value="-1">����</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<b>�豸��</b>
																		<select id="nodeid" name="nodeid" style="width:150px">
																		</select>
																		&nbsp;&nbsp;&nbsp;<input type="button" value="��  ѯ" onclick="search()">
							        								</td>
													    			
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
													    			<td  class="body-data-title">
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
															<td class="body-data-title" width=5%>
																	<INPUT type="radio" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>���</td>
													    			<td class="body-data-title" width=8%>�豸����</td>
													    			<td class="body-data-title" width=8%>IP</td>
													    			<td class="body-data-title" width=8%>��ֵ</td>
													    			<td class="body-data-title" width=8%>����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">������</td>
													    			<td class="body-data-title" width=7%>��������</td>
													    			<td class="body-data-title">��λ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">�ȽϷ�ʽ</td>
													    			<td class="body-data-title">һ����ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			
							        							<%
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
							        										
							        										String isMonitor = "��";
							        										String bcolor="gray";
							        										if("1".equals(alarmIndicatorsNode.getEnabled())){
							        											isMonitor = "��";
							        											bcolor ="#3399CC";
							        										}
							        										
							        										String datatype = "�ַ���";
							        										if("Number".equals(alarmIndicatorsNode.getDatatype())){
							        											datatype = "����";
							        										}
							        										
							        										String sms0 = "��";
							        										if("1".equals(alarmIndicatorsNode.getSms0())){
							        											sms0 = "��";
							        										}
							        										
							        										String sms1 = "��";
							        										if("1".equals(alarmIndicatorsNode.getSms1())){
							        											sms1 = "��";
							        										}
							        										
							        										
							        										String sms2 = "��";
							        										if("1".equals(alarmIndicatorsNode.getSms2())){
							        											sms2 = "��";
							        										}
							        										String compare = "����";
							        										String bgcol = "#ffffff";
							        										if(alarmIndicatorsNode.getCompare() == 0){
							        											compare = "����";
							        											bgcol = "gray";
							        										}
							        										NodeDTO nodeDTO = (NodeDTO)nodeDTOHashtable.get(String.valueOf(alarmIndicatorsNode.getNodeid()) + ":" + alarmIndicatorsNode.getType() + ":" + alarmIndicatorsNode.getSubtype());
							        										if(nodeDTO == null){
							        											nodeDTO = new NodeDTO();
							        										}
							        										String ipaddress = nodeDTO.getIpaddress();
							        										if(ipaddress == null){
							        											ipaddress = "";
							        										}
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list">
							        												<INPUT type="radio" class=noborder name=checkbox value="<%=alarmIndicatorsNode.getId()%>" class=noborder><%=jp.getStartRow()+i%>
							        											</td>
							        											<td class="body-data-list" id="name-<%=alarmIndicatorsNode.getId()%>" name="name-<%=alarmIndicatorsNode.getId()%>" ><%=nodeDTO.getName()%></td>
																	    		<td class="body-data-list" id="ip-<%=alarmIndicatorsNode.getId()%>" name="ip-<%=alarmIndicatorsNode.getId()%>" ><%=ipaddress%></td>
																    			<td class="body-data-list" id="indi-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getName()%></td>
																    			<td class="body-data-list" bgcolor="#33CC33" id="descr-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getDescr()%></td>
																    			<td class="body-data-list" id="type-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getType()%></td>
																    			<td class="body-data-list" id="subtype-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getSubtype()%></td>
																    			<td class="body-data-list"><%=datatype%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getThreshlod_unit()%></td>
																    			<td class="body-data-list" bgcolor="<%=bcolor%>"><%=isMonitor%></td>
																    			<td class="body-data-list" bgcolor="<%=bgcol%>" id="compare-<%=alarmIndicatorsNode.getId()%>" ><%=compare%></td>
																    						
																    			<td class="body-data-list" bgcolor="yellow" id="limen0-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getLimenvalue0()%></td>
																    			<td class="body-data-list" id="time0-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getTime0()%></td>
																    			<td class="body-data-list" id="sms0-<%=alarmIndicatorsNode.getId()%>" ><%=sms0%></td>
																    			<td class="body-data-list" bgcolor="orange" id="limen1-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getLimenvalue1()%></td>
																    			<td class="body-data-list" id="time1-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getTime1()%></td>
																    			<td class="body-data-list" id="sms1-<%=alarmIndicatorsNode.getId()%>" ><%=sms1%></td>
																    			<td class="body-data-list" bgcolor="red" id="limen2-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getLimenvalue2()%></td>
																    			<td class="body-data-list" id="time2-<%=alarmIndicatorsNode.getId()%>" ><%=alarmIndicatorsNode.getTime2()%></td>
																    			<td class="body-data-list" id="sms2-<%=alarmIndicatorsNode.getId()%>" ><%=sms2%></td>
										        								
										        								<input type="hidden" id="compare-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getCompare() %>" />
										        								<input type="hidden" id="deviceid-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getNodeid() %>" />
										        								<input type="hidden" id="sms0-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getSms0() %>" />
										        								<input type="hidden" id="sms1-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getSms1() %>" />
										        								<input type="hidden" id="sms2-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getSms2() %>" />
										        								<input type="hidden" id="way0-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getWay0() %>" />
										        								<input type="hidden" id="way1-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getWay1() %>" />
										        								<input type="hidden" id="way2-hidden-<%=alarmIndicatorsNode.getId()%>" value="<%=alarmIndicatorsNode.getWay2() %>" />
										        								
											        							</tr>
							        										<%
							        									}
							        								}%>
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
	
	<script>
	
		
	
		var nodeArray = new Array();
		
		<%
			if(allNodeDTOlist != null){
				for(int j = 0 ; j < allNodeDTOlist.size(); j++){
				 	NodeDTO  nodeDTO = (NodeDTO)allNodeDTOlist.get(j);
					%>
						nodeArray.push({
							id:"<%=nodeDTO.getId()%>",
							nodeid:"<%=nodeDTO.getNodeid()%>",
							name:"<%=nodeDTO.getName()%>",
							ipaddress:"<%=nodeDTO.getIpaddress()%>",
							type:"<%=nodeDTO.getType()%>",
							subtype:"<%=nodeDTO.getSubtype()%>",
							businessId:"<%=nodeDTO.getBusinessId()%>",
							businessName:"<%=nodeDTO.getBusinessName()%>"
					});
					<%
				}
			}
			
		%>
		
		function changeType(){
			var hostArray = [
						["windows" , "windows"],
						["linux" , "linux"],
						["aix" , "aix"],
						["hpunix" , "hpunix"],
						["solaris" , "solaris"],
						["scounix" , "scounix"],
						["scoopenserver" , "scoopenserver"],
						["as400" , "as400"]
						];
			var netArray = new Array();
			netArray = 	[
						["cisco" , "cisco"],
						["h3c" , "h3c"],
						["entrasys" , "entrasys"],
						["maipu" , "maipu"],
						["redgiant" , "redgiant"],
						["northtel" , "northtel"],
						["dlink" , "dlink"],
						["bdcom" , "bdcom"],
						["zte" , "zte"]
						];
			var dbArray = new Array();
			dbArray = 	[
						["oracle" , "oracle"],
						["sqlserver" , "sqlserver"],
						["mysql" , "mysql"],
						["db2" , "db2"],
						["sybase" , "sybase"],
						["Informix" , "Informix"]
						];
			var middlewareArray = new Array();
			middlewareArray = 	[
						["mq" , "mq"],
						["domino" , "domino"],
						["was" , "was"],
						["tomcat" , "tomcat"],
						["iis" , "iis"],
						["jboss" , "jboss"],
						["apache" , "apache"],
						["tuxedo" , "tuxedo"],
						["cics" , "cics"],
						["dns" , "dns"],
						["weblogic" , "weblogic"]
								];
			var serviceArray = new Array();
			serviceArray = 	[
						["ftp" , "ftp"],
						["email" , "email"],
						["hostprocess" , "hostprocess"],
						["web" , "web"],
						["port" , "port"]
					];
			var upsArray = new Array();
			upsArray = 	[
						["��Ĭ��" , "ems"],
						["÷������" , "mge"]
								];
			var firewallArray = new Array();
			firewallArray = [
						["ɽʯ" , "hillstone"],
						["Netscreen" , "netscreen"],
						["NOKIA" , "nokia"]
								];
			var atmArray = new Array();
			atmArray = [
						["ATM" , "atm"]
					   ];
		    var f5Array = new Array();
			f5Array = [
						["F5" , "f5"]
					   ];
			var gatewayArray = new Array();
			gatewayArray = [
						    ["CISCO" , "cisco"]
						   ];
			var airArray = new Array();
			airArray = 	[
						["��Ĭ��" , "ems"]
								];
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			subtype.length = 0;
			if(type.value == "-1"){
				subtype.options[0] = new Option("����" , "-1");      
			}else if(type.value == "net"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <netArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(netArray[i][0],netArray[i][1]);                     
				}	
			}else if(type.value == "host"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <hostArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(hostArray[i][0],hostArray[i][1]);                     
				}	
			}else if(type.value == "db"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <dbArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(dbArray[i][0],dbArray[i][1]);                     
				}	
			}else if(type.value == "middleware"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <middlewareArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(middlewareArray[i][0],middlewareArray[i][1]);                     
				}	
			}else if(type.value == "service"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <serviceArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(serviceArray[i][0],serviceArray[i][1]);            
				}         
			}else if(type.value == "ups"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <upsArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(upsArray[i][0],upsArray[i][1]);                     
				}	
			}else if(type.value == "air"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <airArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(airArray[i][0],airArray[i][1]);                     
				}	
			}else if(type.value == "atm"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <atmArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(atmArray[i][0],atmArray[i][1]);                     
				}	
			}else if(type.value == "f5"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <f5Array.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(f5Array[i][0],f5Array[i][1]);                     
				}	
			}else if(type.value == "firewall"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <firewallArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(firewallArray[i][0],firewallArray[i][1]);                     
				}	
			}else if(type.value == "gateway"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <gatewayArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(gatewayArray[i][0],gatewayArray[i][1]);                     
				}	
			}
			
			changeNode();
		}
		
		
		function changeNode(){
			
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			nodeid.length = 0;
			nodeid.options[0] = new Option("����","-1");
			if(type.value == "-1"){
				var k = 1;
				
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{                   
					nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      			k++;               
				}	
			}else if(type.value != "-1" && subtype.value == "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{      
	      			if(nodeArray[i].type == type.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}else if(type.value != "-1" && subtype.value != "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)
	      		{      
	      			if(nodeArray[i].type == type.value && nodeArray[i].subtype == subtype.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}
			
		}
		
		function initAttribute(){
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			
			type.value = "<%=type%>";
			changeType();
			subtype.value = "<%=subtype%>";
			changeNode();
			nodeid.value = "<%=nodeid%>";
			
		}
		
		initAttribute();
	</script>
	
</html>
