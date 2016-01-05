<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.MonitorDBDTO"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>


<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  String flag = (String)request.getAttribute("flag");
  
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getDBOperationPermission());
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
			var sid="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip)
			{	
				//alert(id+"==="+nodeid+"=="+ip);
				ipaddress=ip;
				var arr=ipaddress.split(":");
				if(arr.length>1){
				  sid=arr[1];
				}else{
				  sid="-1";
				}
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"0000");
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
			    mainForm.action="<%=rootPath%>/db.do?action=check&id="+node+"&sid="+sid;
			    mainForm.submit();
			}
			function edit()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=ready_edit&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function cancelmanage()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=cancelmanage&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function addmanage()
			{
				 mainForm.action="<%=rootPath%>/db.do?action=addmanage&id="+node+"&sid="+sid;
				 mainForm.submit();
			}	
			function clickMenu()
			{
			}
			
			function toAdd(){
			     mainForm.action = "<%=rootPath%>/db.do?action=ready_add";
			     mainForm.submit();
			}
			function toDelete(){
				mainForm.action = "<%=rootPath%>/db.do?action=delete";
				mainForm.submit();
			}  
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
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<table id="body-container" class="body-container" height="100%">
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
									                	<td class="content-title">&nbsp;Ӧ�� &gt;&gt; ���ݿ���� &gt;&gt; ���ݿ��б�</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<%if(flag == null) {
		        									%>
		        									<tr>
        												<td colspan="11">
											        		<table>
											        			<tr>
																	<td style="text-align: right;" class="body-data-title">
                                                                        <a href="#" onclick="toAdd()" style="display: <%=createOperationDisable%>">���</a>
																		<a href="#" onclick="toDelete()" style="display: <%=deleteOperationDisable%>">ɾ��</a>&nbsp;&nbsp;&nbsp;
											  						</td>  
					        									</tr>
					        								</table>
					        							</td>                     
					        						</tr>
		        									<%
		        									} %>
		        									<tr>
       													<td align="center" class="body-data-title">���</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">���ݿ���</td>
       													<td align="center" class="body-data-title">IP��ַ</td>
       													<td align="center" class="body-data-title">�˿�</td>
       													<td align="center" class="body-data-title">����״̬</td>
       													<td align="center" class="body-data-title">״̬</td>
       													<td align="center" class="body-data-title">������</td>
       													<td align="center" class="body-data-title">����</td>
		        									</tr>
		        									<%
		        									    if(list!=null&& list.size()>0){
		        									    	
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									        	MonitorDBDTO monitorDBDTO = (MonitorDBDTO)list.get(i);
		        									        	Hashtable eventListSummary = monitorDBDTO.getEventListSummary();
		        									        	String generalAlarm = (String)eventListSummary.get("generalAlarm");
	        									        		String urgentAlarm = (String)eventListSummary.get("urgentAlarm");
		        									        	String seriousAlarm = (String)eventListSummary.get("seriousAlarm");
		        									        	
		        									        	
		        									        	String status = monitorDBDTO.getStatus();
					        									  
		        									        	String statusImg = "";
		        									        	if("1".equals(status)){
		        									        		statusImg = "alarm_level_1.gif";
		        									        	}else if("2".equals(status)){
		        									        		statusImg = "alarm_level_2.gif";
		        									        	}else if("3".equals(status)){
		        									        		statusImg = "alert.gif";
		        									        	}else{
		        									        		statusImg = "status_ok.gif";
		        									        	}
		        									        	
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        									            	<td align="center" class="body-data-list"><INPUT type="radio" class=noborder name=radio value="<%=monitorDBDTO.getId()+ ":" + monitorDBDTO.getSid()%>"><%=i+1%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getAlias()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getDbtype()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getDbname()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getIpAddress()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getPort()%></td>
			       													<td align="center" class="body-data-list"><%=monitorDBDTO.getMon_flag()%></td>
			       													<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>"><%=NodeHelper.getStatusDescr(Integer.valueOf(status))%></td>
												                    <td align="center" class="body-data-list"><%=monitorDBDTO.getPingValue()%></td>
			       													<!-- 
			       													<td align="center" class="body-data-list">
			       														<img style="display: <//%=updateOperationDisable%>;" src="<//%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<//%=monitorDBDTO.getId()%>','<//%=monitorDBDTO.getIpAddress()%>:<//%=monitorDBDTO.getSid()%>') alt="�Ҽ�����">
			       													</td>
			       													 -->
			       													 <td  align='center'  class="body-data-list"><a href="<%=rootPath%>/db.do?action=ready_edit&id=<%=monitorDBDTO.getId()%>&sid=<%=monitorDBDTO.getSid()%>">
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
