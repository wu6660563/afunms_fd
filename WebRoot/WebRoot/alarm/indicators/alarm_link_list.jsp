<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.alarm.model.AlarmLinkNode"%>
<%@page import="com.afunms.alarm.model.AlarmLinkType"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	
	String menuTable = (String)request.getAttribute("menuTable");
	
	String nodeid = (String)request.getAttribute("linkid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
	JspPage jp = (JspPage)request.getAttribute("page");
	List nodelist = (List)request.getAttribute("nodelist");
	//List linkNodeList = (List)request.getAttribute("alarmLinkNodeList");
	List linkTypeList = (List)request.getAttribute("alarmLinkTypeList");
	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
//	List<AlarmLinkNode> list = (List<AlarmLinkNode>)request.getAttribute("alarmLinkNodeList");
	List<AlarmLinkNode> list = (List<AlarmLinkNode>)request.getAttribute("list");
	Hashtable<String , NodeDTO> nodeDTOHashtable = (Hashtable<String , NodeDTO>)request.getAttribute("nodeDTOHashtable");
	HashMap<Integer,AlarmLinkType> linkTypeMap = (HashMap<Integer,AlarmLinkType>)request.getAttribute("linkTypeMap");

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
			
			function add(){
				
				
				//var type = document.getElementById("type");
				//var subtype = document.getElementById("subtype");
				//if(type.value!="-1" && subtype.value !="-1" ){
				mainForm.action = "<%=rootPath%>/alarmlink.do?action=showAdd&jspFlag=add";
				mainForm.submit();
				//}
			/*	if(type.value=="-1"){
					alert("��ѡ������!");
				}
				if(subtype.value=="-1"){
					alert("��ѡ��������!");
				}
			*/
			}
			
			function edit(){
				//alert("edit" + node);
				mainForm.action = "<%=rootPath%>/alarmlink.do?action=showEdit&id=" + node;
				mainForm.submit();
			}
			
			function toChooseNode(){
				var nodeid = document.getElementById("nodeid");
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" && nodeid.value !="-1"){
					mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=showAdd&jspFlag=multi";
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
				mainForm.action = "<%=rootPath%>/alarmlink.do?action=showlist";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		<script type="text/javascript">
  			//var curpage= <%=jp.getCurrentPage()%>;
  			//var totalpages = <%=jp.getPageTotal()%>;			
  			var listAction = "<%=rootPath%>/alarmlink.do?action=showlist";  			
  			var delAction = "<%=rootPath%>/alarmlink.do?action=showDelete";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">�༭</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> ��Դ >> �澯��ֵ >> �澯��ֵ���б� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="body-data-title">
													<tr>
														<td>
															<table>
																<tr>
																	<td>
																		&nbsp;&nbsp;<b>�澯�飺</b>
																		<select id="linkid" name="linkid" style="width:150px">
																			<option value="-1">����</option>
																			
																			
																		</select>
																		&nbsp;&nbsp;&nbsp;<input type="button" value="��  ѯ" onclick="search()">
							        								</td>
													    			<td  class="body-data-title" style="text-align: right;">
							    										<a href="#" onclick="add()">���</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
							    										<!--  <a href="#" onclick="toChooseNode()">����Ӧ��</a>&nbsp;&nbsp;&nbsp; -->
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
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
															<td class="body-data-title" width=5%>
																	<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>���</td>
													    			<td class="body-data-title" width=8%>������</td>
													    			<td class="body-data-title" width=8%>�豸����</td>
													    			<td class="body-data-title" width=8%>IP</td>
													    			<td class="body-data-title" width=8%>��ֵ</td>
													    			<td class="body-data-title" width=8%>����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">������</td>												    		
													    		
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">һ����ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title" width=5%>����</td>
							        							<%
							        								if(list != null && list.size() > 0) {
							        									
							        									for(int i = 0; i < list.size(); i++){
							        										AlarmLinkNode alarmLinkNode = (AlarmLinkNode)list.get(i);
							        										
							        										String isMonitor = "��";
							        										String bcolor="gray";
							        										if("1".equals(alarmLinkNode.getIsEnabled()+"")){
							        											isMonitor = "��";
							        											bcolor ="#3399CC";
							        										}
							        	
							        										
							        										String sms0 = "��";
							        										if("1".equals(alarmLinkNode.getSms0()+"")){
							        											sms0 = "��";
							        										}
							        										
							        										String sms1 = "��";
							        										if("1".equals(alarmLinkNode.getSms1()+"")){
							        											sms1 = "��";
							        										}
							        										
							        										String cover = "��";
							        										if("1".equals(alarmLinkNode.getIsCover()+"")) {
							        											cover = "��";	
							        										}
							        										
							        										String sms2 = "��";
							        										if("1".equals(alarmLinkNode.getSms2()+"")){
							        											sms2 = "��";
							        										}
							        										String compare = "����";
							        										String bgcol = "#ffffff";
							        										if(alarmLinkNode.getCompareType() == 0){
							        											compare = "����";
							        											bgcol = "gray";
							        										}
							        										
							        										NodeDTO nodeDTO = new NodeDTO();
							        										String deviceName = "";
							        										String ipaddress = "";
							        										
							        										if(nodeDTOHashtable != null){
							        									//	System.out.println(alarmLinkNode.getNodeid());
							        										nodeDTO = (NodeDTO)nodeDTOHashtable.get(alarmLinkNode.getNodeid()+"");
							        										
							        										//if(nodeDTO != null)
							        											//System.out.println(nodeDTO.toString());
							        										
							        										if(nodeDTO == null){
							        											nodeDTO = new NodeDTO();
							        										}
							        										
							        										ipaddress = nodeDTO.getIpaddress();
							        										if(ipaddress == null){
							        											ipaddress = "";
							        										}
							        										deviceName = nodeDTO.getName();
							        										if(deviceName == null){
							        											deviceName = "";
							        										}
							        										}
							        										String linkName = "";
							        										if(null != linkTypeMap.get(alarmLinkNode.getLinkid()))
							        											linkName = linkTypeMap.get(alarmLinkNode.getLinkid()).getLinkName();
							        										
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list">
							        												<INPUT type="checkbox" class=noborder name=checkbox value="<%=alarmLinkNode.getId()%>" class=noborder>
							        											</td>
							        											<td class="body-data-list"><%=linkName%></td><!-- ������ -->
																	    		<td class="body-data-list"><%=deviceName%></td><!-- �豸���� -->
																    			<td class="body-data-list" ><%=ipaddress%></td><!-- IP -->
																    			<td class="body-data-list" ><%=alarmLinkNode.getName()%></td><!-- ��ֵ�� -->
																    			<td class="body-data-list" bgcolor="#33CC33"><%=alarmLinkNode.getAlarmDescr() %></td>
																    			<td class="body-data-list"><%=nodeDTO.getType()%></td>
																    			<td class="body-data-list"><%=nodeDTO.getSubtype()%></td>
																    			
																    			<td class="body-data-list" bgcolor="<%=bcolor%>"><%=isMonitor%></td>
																    			<td class="body-data-list" bgcolor="yellow"><%=alarmLinkNode.getLimenvalue0()%></td>
																    			<td class="body-data-list"><%=alarmLinkNode.getTime0()%></td>
																    			<td class="body-data-list"><%=sms0%></td>
																    			<td class="body-data-list" bgcolor="orange"><%=alarmLinkNode.getLimenvalue1()%></td>
																    			<td class="body-data-list"><%=alarmLinkNode.getTime1()%></td>
																    			<td class="body-data-list"><%=sms1%></td>
																    			<td class="body-data-list" bgcolor="red"><%=alarmLinkNode.getLimenvalue2()%></td>
																    			<td class="body-data-list"><%=alarmLinkNode.getTime2()%></td>
																    			<td class="body-data-list"><%=sms2%></td>
										        								<td align="center" class="body-data-list">
																					<img src="<%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<%=alarmLinkNode.getId()%>','','1111') title="�Ҽ��˵�">
						       													</td>
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
			if(linkTypeList != null){
				for(int j = 0 ; j < linkTypeList.size(); j++){
				 //	NodeDTO  nodeDTO = (NodeDTO)allNodeDTOlist.get(j);
				 	AlarmLinkType linkType = (AlarmLinkType)linkTypeList.get(j);
					%>
						nodeArray.push({
							id:"<%=linkType.getId()%>",
							linkname:"<%=linkType.getLinkName()%>"
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
		
		function chanageLinkNode() {
			var linkid = document.getElementById("linkid");
			linkid.length = 0;
			linkid.options[0] = new Option("����","-1");
			var k=1;
			for(var i=0; i<nodeArray.length; i++){
				if(nodeArray[i].id != null || nodeArray[i].id > 0){
					linkid.options[k] = new Option(nodeArray[i].linkname,nodeArray[i].id);
					k++;
				}
			}
		}
		
		function initAttribute(){
			//var type = document.getElementById("type");
			//var subtype = document.getElementById("subtype");
			var linkid = document.getElementById("linkid");
			
			chanageLinkNode();
			
		}
		
		initAttribute();
	</script>
	
</html>
