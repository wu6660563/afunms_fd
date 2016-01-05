<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>

<%@page import="com.afunms.application.model.MonitorMiddlewareDTO"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>


<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  String flag = (String)request.getAttribute("flag");
  
  String category = (String)request.getAttribute("category");
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
			var category = "";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip,category_flag)
			{	
				ipaddress=ip;
				category = category_flag;
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
				if("tomcat" == category){
					mainForm.action = "<%=rootPath%>/tomcat.do?action=tomcat_jvm&id="+node;
				}else if("mq" == category){
					mainForm.action = "<%=rootPath%>/mq.do?action=detail&id="+node;
				}else if("domino" == category){
					mainForm.action = "<%=rootPath%>/domino.do?action=detail&id="+node;
				}else if("was" == category){
					mainForm.action = "<%=rootPath%>/was.do?action=detail&id="+node;
				}else if("weblogic" == category){
					mainForm.action = "<%=rootPath%>/weblogic.do?action=detail&id="+node;
				}else if("jboss" == category){
					mainForm.action = "<%=rootPath%>/jboss.do?action=detail&id="+node;
				}else if("apache" == category){
					mainForm.action = "<%=rootPath%>/apache.do?action=server&id="+node;
				}else if("tuxedo" == category){
					mainForm.action = "<%=rootPath%>/tuxedo.do?action=toDetail&id=" + node;
				}else if("iis" == category){
					mainForm.action = "<%=rootPath%>/iis.do?action=detail&id="+node;
				}else if("cics" == category){
					mainForm.action = "<%=rootPath%>/cics.do?action=detail&id="+node;
				}else if("dns" == category){
					mainForm.action = "<%=rootPath%>/dns.do?action=detail&id="+node;
				}
				
				mainForm.submit();
			}
			function edit()
			{
				if("tomcat" == category){
					mainForm.action = "<%=rootPath%>/tomcat.do?action=ready_edit&id="+node;
				}else if("mq" == category){
					mainForm.action = "<%=rootPath%>/mq.do?action=ready_edit&id="+node;
				}else if("domino" == category){
					mainForm.action = "<%=rootPath%>/domino.do?action=ready_edit&id="+node;
				}else if("was" == category){
					mainForm.action = "<%=rootPath%>/was.do?action=ready_edit&id="+node;
				}else if("weblogic" == category){
					mainForm.action = "<%=rootPath%>/weblogic.do?action=ready_edit&id="+node;
				}else if("jboss" == category){
					mainForm.action = "<%=rootPath%>/jboss.do?action=ready_edit&id="+node;
				}else if("apache" == category){
					mainForm.action = "<%=rootPath%>/apache.do?action=ready_edit&id="+node;
				}else if("tuxedo" == category){
					mainForm.action = "<%=rootPath%>/tuxedo.do?action=ready_edit&id=" + node;
				}else if("iis" == category){
					mainForm.action = "<%=rootPath%>/iis.do?action=ready_edit&id="+node;
				}else if("cics" == category){
					mainForm.action = "<%=rootPath%>/cics.do?action=ready_edit&id="+node;
				}else if("dns" == category){
					mainForm.action = "<%=rootPath%>/dns.do?action=detail&id="+node;
				}
				mainForm.submit();
			}
			
			function addmanage()
			{
				mainForm.action ="<%=rootPath%>/middleware.do?action=changeManage&value=1&id="+ node + "&type=" + category + "&category=" + "<%=category%>";
				mainForm.submit();
			}
			
			function cancelmanage()
			{
				mainForm.action ="<%=rootPath%>/middleware.do?action=changeManage&value=0&id="+ node + "&type=" + category + "&category=" + "<%=category%>";
				mainForm.submit();
				
			}
			
			
			function clickMenu()
			{
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
									                	<td class="content-title"> Ӧ�� &gt;&gt; �м����� &gt;&gt; �м������б�</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">IP��ַ</td>
       													<td align="center" class="body-data-title">�˿�</td>
       													<td align="center" class="body-data-title">�Ƿ���</td>
       													<td align="center" class="body-data-title">״̬</td>
       													<!-- <td align="center" class="body-data-title">�澯</td>
       													<td align="center" class="body-data-title">����</td> -->
		        									</tr>
		        									<%
		        									    if(list!=null&& list.size()>0){
		        									    	
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									        	MonitorMiddlewareDTO monitorMiddlewareDTO = (MonitorMiddlewareDTO)list.get(i);
		        									        	
		        									        	
		        									        	String status = monitorMiddlewareDTO.getStatus();
					        									        	
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
		        									        	
		        									        	Hashtable eventListSummary = monitorMiddlewareDTO.getEventListSummary();
		        									        	String generalAlarm = (String)eventListSummary.get("generalAlarm");
		        									        	String urgentAlarm = (String)eventListSummary.get("urgentAlarm");
		        									        	String seriousAlarm = (String)eventListSummary.get("seriousAlarm");
		        									        	
		        									        	
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        									            	<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=monitorMiddlewareDTO.getId()%>"><%=i+1%></td>
			       													<td align="center" class="body-data-list"><%=monitorMiddlewareDTO.getAlias()%></td>
			       													<td align="center" class="body-data-list"><%=monitorMiddlewareDTO.getCategory()%></td>
			       													<td align="center" class="body-data-list"><%=monitorMiddlewareDTO.getIpAddress()%></td>
			       													<td align="center" class="body-data-list"><%=monitorMiddlewareDTO.getPort()%></td>
																	<td align="center" class="body-data-list"><%=monitorMiddlewareDTO.getMonflag()%></td>			       													<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>"></td>
			       													<!-- <td align="center" class="body-data-list">
                                                                        <table>
                                                                            <tr>
                                                                                <td align="center">
                                                                                    <table border=1 height=15 bgcolor=#ffffff>
                                                                                        <tr>
                                                                                            <td width="30%" bgcolor="#ffff00" align="center"><%=generalAlarm%></td>
                                                                                            <td width="30%" bgcolor="orange" align="center"><%=urgentAlarm%></td>
                                                                                            <td width="30%" bgcolor="#ff0000" align="center"><%=seriousAlarm%></td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
			       													<td align="center" class="body-data-list">
			       														<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=monitorMiddlewareDTO.getId()%>','<%=monitorMiddlewareDTO.getIpAddress()%>','<%=monitorMiddlewareDTO.getCategory()%>') alt="�Ҽ�����">
			       													</td>
                                                                     -->
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
