<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.alarm.model.AlarmIndicators"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmWay"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  JspPage jp = (JspPage)request.getAttribute("page");
  
  String alarmWayNameEvent = (String)request.getAttribute("alarmWayNameEvent");
  String alarmWayIdEvent = (String)request.getAttribute("alarmWayIdEvent");
  
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
				mainForm.action = "<%=rootPath%>/alarmWay.do?action=add";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/alarmWay.do?action=edit&id=" + node;
				mainForm.submit();
			}
			
			function toDetermine(){
				var radioes = document.getElementsByName("radio");
				for(var i = 0 ; i < radioes.length; i++){
					var radio = radioes[i];
					if(radio.checked){
						var alarmWayIdEvent = window.parent.opener.document.getElementById("<%=alarmWayIdEvent%>");
						var alarmWayNameEvent = window.parent.opener.document.getElementById("<%=alarmWayNameEvent%>");
						alarmWayIdEvent.value = radio.value;
						alarmWayNameEvent.value = document.getElementById("alarmWayName-" + radio.value).value;
						window.close();
					}
				}
			}
			  
			
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/alarmWay.do?action=delete";
  			var listAction = "<%=rootPath%>/alarmWay.do?action=chooselist";
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
									                	<td class="content-title"> �澯 >> �澯���� >> �澯��ʽ���� >> �澯��ʽ�б� </td>
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
													    			<td  class="body-data-title" style="text-align: right;">
							    										<a href="#" onclick="toDetermine()">ȷ��</a>&nbsp;&nbsp;&nbsp;
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
													    			<td class="body-data-title">���</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">Ĭ�ϸ澯��ʽ</td>
													    			<td class="body-data-title">ҳ���Ƿ�澯</td>
													    			<td class="body-data-title">�����Ƿ�澯</td>
													    			<td class="body-data-title">�ʼ��Ƿ�澯</td>
													    			<td class="body-data-title">�绰�Ƿ�澯</td>
													    			<td class="body-data-title">�����Ƿ�澯</td>
													    			<td class="body-data-title">���͹����Ƿ�澯</td>
							        							</tr>
							        							<%
							        							
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										AlarmWay alarmWay = (AlarmWay)list.get(i);
							        										
							        										String isDefault = "��";
							        										if("1".equals(alarmWay.getIsDefault())){
							        											isDefault = "��";
							        										}
							        										
							        										String isPageAlarm = "��";
							        										if("1".equals(alarmWay.getIsPageAlarm())){
							        											isPageAlarm = "��";
							        										}
							        										
							        										String isSoundAlarm = "��";
							        										if("1".equals(alarmWay.getIsSoundAlarm())){
							        											isSoundAlarm = "��";
							        										}
							        										
							        										String isMailAlarm = "��";
							        										if("1".equals(alarmWay.getIsMailAlarm())){
							        											isMailAlarm = "��";
							        										}
							        										
							        										String isPhoneAlarm = "��";
							        										if("1".equals(alarmWay.getIsPhoneAlarm())){
							        											isPhoneAlarm = "��";
							        										}
							        										
							        										String isSMSAlarm = "��";
							        										if("1".equals(alarmWay.getIsSMSAlarm())){
							        											isSMSAlarm = "��";
							        										}
                                                                            
                                                                            String isFvsdAlarm = "��";
                                                                            if("1".equals(alarmWay.getIsFvsdAlarm())){
                                                                                isFvsdAlarm = "��";
                                                                            }
							        										%>
							        										
							        										<tr>
																    			<td class="body-data-list"><INPUT type="radio" name="radio" value="<%=alarmWay.getId()%>"><%=jp.getStartRow() + i%></td>
																    			<td class="body-data-list"><%=alarmWay.getName()%><input type="hidden" name="alarmWayName-<%=alarmWay.getId()%>" id="alarmWayName-<%=alarmWay.getId()%>" value="<%=alarmWay.getName()%>"></td>
																    			<td class="body-data-list"><%=isDefault%></td>
																    			<td class="body-data-list"><%=isPageAlarm%></td>
																    			<td class="body-data-list"><%=isSoundAlarm%></td>
																    			<td class="body-data-list"><%=isMailAlarm%></td>
																    			<td class="body-data-list"><%=isPhoneAlarm%></td>
																    			<td class="body-data-list"><%=isSMSAlarm%></td>
                                                                                <td class="body-data-list"><%=isFvsdAlarm%></td>
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
