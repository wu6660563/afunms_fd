<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.application.model.ApplicationNode"%>
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
	
	
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
	//String subtype = (String)request.getAttribute("subtype");
	
	List nodelist = (List)request.getAttribute("nodelist");
	
	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
	List<ApplicationNode> list = (List<ApplicationNode>)request.getAttribute("list");
	
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
%>





<html>
	<head>
	
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
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
				mainForm.action = "<%=rootPath%>/applicationnode.do?action=showAdd";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/applicationnode.do?action=showUpdate&id=" + node;
				mainForm.submit();
			}
			
			function toChooseNode(){
				var nodeid = document.getElementById("nodeid");
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" && nodeid.value !="-1"){
					mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showAdd&jspFlag=multi";
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
				mainForm.action = "<%=rootPath%>/applicationnode.do?action=list";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var listAction = "<%=rootPath%>/applicationnode.do?action=list";
  			var delAction = "<%=rootPath%>/applicationnode.do?action=delete";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();" leftmargin="0" topmargin="0">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail();">������Ϣ</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.cancelmanage()">ȡ������</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addmanage()">��Ӽ���</td>
			</tr>	
		</table>
		</div>
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container" height="100%">
				<tr>
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
									                	<td class="content-title">&nbsp;Ӧ�� >> Ӧ��ϵͳ >> Ӧ���б� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
																		&nbsp;&nbsp;<b>�����ͣ�</b>
																		<input type="text" id="subtype" name="subtype" style="width:150px" >
																			
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
							    										<jsp:include page="/common/page.jsp">
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
																	<td class="body-data-title" width=15%><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
													    			<td class="body-data-title" width=15%>����</td>
													    			<td class="body-data-title" width=15%>������</td>
													    			<td class="body-data-title" width=15%>�ؼ���ʶ</td>
													    			<td class="body-data-title" width=20%>����</td>
													    			<td class="body-data-title" width=20%>����</td>
													    		</tr>
							        							<%
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										ApplicationNode node = (ApplicationNode)list.get(i);
							        										
							        									
							        										%>
							        											<tr <%=onmouseoverstyle%>>
							        											<td class="body-data-list"><INPUT type="checkbox" value="<%=node.getId()%>" name="checkbox" id="checkbox"><%=i + 1%></td>
							        											<td class="body-data-list"><%=node.getName()== null ? "" : node.getName()%></td>
																    			<td class="body-data-list"><%=node.getSubtype()== null ? "" : node.getSubtype() %></td>
																    			<td class="body-data-list"><%=node.getUniqueKey()== null ? "" : node.getUniqueKey() %></td>
																    			<td class="body-data-list"><%=node.getDescr()==null ? "" : node.getDescr() %></td>
																    			<!-- 
																    			<td class="body-data-list">
																					<img src="<//%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<//%=node.getId()%>','<//%=node.getUniqueKey() %>')>

																				</td> -->
																				<td  align='center'  class="body-data-list"><a href="<%=rootPath%>/applicationnode.do?action=showUpdate&id=<%=node.getId()%>">
																					<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="�༭"/></a>
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
</html>
