<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.application.model.ProcessGroup"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  	String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
  	List list = (List)request.getAttribute("list");
  	JspPage jp = (JspPage)request.getAttribute("page");
  	Hashtable hostNodeHashtable = (Hashtable)request.getAttribute("hostNodeHashtable");
  	
  	String ipaddress = (String)request.getAttribute("ipaddress");
  	String processgroupname = (String)request.getAttribute("processgroupname");
  	
  	if(null == ipaddress || ipaddress == "null"){
  		ipaddress = "";
  	}
  	
  	if(null == processgroupname || processgroupname == "null"){
  		processgroupname = "";
  	}
    
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getProcessOperationPermission());
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
			//���Ӳ˵�	
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
		<script language="javascript">
			var delAction = "<%=rootPath%>/processgroup.do?action=delete&foward=showlist";
		  	var listAction = "<%=rootPath%>/processgroup.do?action=showlist";
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
			*width:����ʾ�Ŀ���
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
			function toAdd()
		  	{
		    	mainForm.action = "<%=rootPath%>/processgroup.do?action=ready_add&foward=showadd";
		    	mainForm.submit();
		  	}
		  
			function edit()
			{
				mainForm.action ="<%=rootPath%>/processgroup.do?action=ready_edit&forward=showedit&groupId="+node;
				mainForm.submit();
			}
			
			function showHistory()
			{
				mainForm.action ="";
				mainForm.submit();
			}
			
			function search()
			{
				mainForm.action ="<%=rootPath%>/processgroup.do?action=showlist";
				mainForm.submit();
			}
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();" >
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
						onclick="parent.showHistory()">�鿴����</td>
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
									                	<td class="content-title">&nbsp;Ӧ�� >> ��������� >> �������б� </td>
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
					        										<td align="center" class="body-data-title" style="text-align: left;">&nbsp;
			       														IP��ַ��<input type="text" name="ipaddress" id="ipaddress" value="<%=ipaddress%>">
			       														���������ƣ�<input type="text" name="processgroupname" id="processgroupname" value="<%=processgroupname%>">
			       														<input type="button" value="��  ��" onclick="search()">
			       													</td>
			       													<td align="center" class="body-data-title" style="text-align: right;">
			       														<a href="#" onclick="toAdd()" style="display: <%=createOperationDisable%>">����</a>
			       														<a href="#" onclick="toDelete();" style="display: <%=deleteOperationDisable%>">ɾ��</a>&nbsp;&nbsp;&nbsp;
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
		        												<tr>
		        													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
		        													<td align="center" class="body-data-title">�豸����</td>
		        													<td align="center" class="body-data-title">�豸IP��ַ</td>
		        													<td align="center" class="body-data-title">����������</td>
		        													<td align="center" class="body-data-title">�澯�ȼ�</td>
		        													<td align="center" class="body-data-title">���״̬</td>
		        													<td align="center" class="body-data-title">����</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	ProcessGroup processGroup = (ProcessGroup)list.get(i);
					        									        	
					        									        	String alarmlevel = processGroup.getAlarm_level();
					        									        	
					        									        	String monflag = processGroup.getMon_flag();
					        									        	
					        									        	if("1".equals(alarmlevel)){
					        									        		alarmlevel = "��ͨ�澯";
					        									        	} else if("2".equals(alarmlevel)){
					        									        		alarmlevel = "���ظ澯";
					        									        	} else if("3".equals(alarmlevel)){
					        									        		alarmlevel = "�����澯";
					        									        	} 
					        									        	String monflag_str = "��";
					        									        	if("1".equals(monflag)){
					        									        		monflag_str = "��";
					        									        	}
					        									        	
					        									        	String nodeName = "";
					        									        	HostNode hostNode = (HostNode)hostNodeHashtable.get(processGroup.getNodeid()+ "");
					        									        	if( hostNode != null){
					        									        		nodeName = hostNode.getAlias();
					        									        	}
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
					        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=processGroup.getId()%>" name="checkbox" id="checkbox"><%=i + 1%></td>
					        													<td align="center" class="body-data-list"><%=nodeName%></td>
					        													<td align="center" class="body-data-list"><%=processGroup.getIpaddress()%></td>
					        													<td align="center" class="body-data-list"><%=processGroup.getName()%></td>
								        										<td align="center" class="body-data-list"><%=alarmlevel%></td>
								        										<td align="center" class="body-data-list"><%=monflag_str%></td>
								        										<!-- 
								        										<td align="center" class="body-data-list"><img style="display: <//%=updateOperationDisable%>" src="<//%=rootPath%>/resource/image/status.gif" border="0" width="15" alt="�Ҽ�����" oncontextmenu=showMenu('2','<//%=processGroup.getId()%>','<//%=processGroup.getNodeid()%>')></td>
								        										 -->
								        										 <td  align='center'  class="body-data-list"><a href="<%=rootPath%>/processgroup.do?action=ready_edit&forward=showedit&groupId=<%=processGroup.getId()%>">
																					<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="�༭"/></a>
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