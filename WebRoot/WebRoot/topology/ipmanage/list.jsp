<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@page import="com.afunms.ipresource.model.IpManageBasicVo"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	
	List nodelist = (List)request.getAttribute("nodelist");
	
	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
	List<IpManageBasicVo> list = (List<IpManageBasicVo>)request.getAttribute("list");
	
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
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=toAdd";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=toEdit&id=" + node;
				mainForm.submit();
			}
			
			function search(){
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=list";
				mainForm.submit();
			}
			
			function refresh(){
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=refresh";
				mainForm.submit();
			}
			
			function addManage(){
				//��ӹ���
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=addManage&id="+node;
				mainForm.submit();
			}
			
			function cancelManage(){
				//ȡ������
				mainForm.action = "<%=rootPath%>/ipmanage.do?action=cancelManage&id="+node;
				mainForm.submit();
			}
			
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var listAction = "<%=rootPath%>/ipmanage.do?action=list";
  			var delAction = "<%=rootPath%>/ipmanage.do?action=delete";
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
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addManage()">��ӹ���</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.cancelManage()">ȡ������</td>
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
									                	<td class="content-title">&nbsp;��Դ >> IP/MAC��Դ >> IP��ַ���� </td>
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
																		&nbsp;&nbsp;<b>IP��ַ</b>
																		<input type="text" id="ipaddress" name="ipaddress" style="width:150px" >
																		&nbsp;&nbsp;&nbsp;<input type="button" value="��  ѯ" onclick="search()">
							        								</td>
							        								<td  class="body-data-title" style="text-align: right;">
							        									<a href="#" onclick="refresh()">ˢ������</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="add()">���</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
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
															<td class="body-data-title" width=5%>
																	<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>���</td>
													    			<td class="body-data-title" width=20%>�豸����</td>
													    			<td class="body-data-title" width=20%>IP��ַ</td>
													    			<td class="body-data-title" width=20%>MAC��ַ</td>
													    			<td class="body-data-title" width=20%>�Ƿ����</td>
													    			<td class="body-data-title" width=20%>�༭</td>
													    			
							        							<%
							        								HostNodeDao dao = null;
							        								HostNode hostNode = null;
							        								String aliasName = null;
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										IpManageBasicVo vo = (IpManageBasicVo)list.get(i);
							        										
							        										dao = new HostNodeDao();
							        										aliasName = "";
							        										try {
							        											hostNode = (HostNode) dao.findByIpaddress(vo.getIpaddress());
							        										} catch(Exception e) {
							        											e.printStackTrace();
							        										} finally {
							        											dao.close();
							        										}
							        										if(hostNode != null && hostNode.getAlias() != null && !"null".equals(hostNode.getAlias().trim())) {
							        											System.out.println( hostNode.getAlias() + ">>>>>>>>>>>>>>" + vo.getIpaddress());
							        											aliasName = hostNode.getAlias();
							        										}
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list">
							        												<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>" class=noborder><%=jp.getStartRow()+i%>
							        											</td>
							        											<td class="body-data-list"><%=aliasName%></td>
							        											<td class="body-data-list"><%=vo.getIpaddress()== null ? "" : vo.getIpaddress()%></td>
																    			<td class="body-data-list"><%=vo.getMac()== null ? "" : vo.getMac()%></td>
																    			<td class="body-data-list">
																    			<%
																    			if("1".equals(vo.getIfmanage())) {
																    				out.print("��");
																    			} else {
																    				out.print("��");
																    			}
																    			%>
																    			</td>
																    			<td class="body-data-list">
																					<img src="<%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>')>
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
