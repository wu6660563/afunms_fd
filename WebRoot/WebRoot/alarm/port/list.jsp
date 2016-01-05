<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.alarm.model.AlarmIndicators"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmPort"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  String nodeid = (String)request.getAttribute("nodeid");
  String type = (String)request.getAttribute("type");
  String subtype = (String)request.getAttribute("subtype");
  String ipaddress = (String)request.getAttribute("ipaddress");
 
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
			        popMenu(itemMenu,100,"1000");
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
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=add";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/alarmport.do?action=edit&id=" + node;
				mainForm.submit();
			}
			
			function viewAlarmNode(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=add";
				mainForm.submit();
			}
			
			
			function addmanage()
			{
				mainForm.action ="<%=rootPath%>/alarmport.do?action=changeManage&value=1&id="+ node + "&nodeid=" + "<%=nodeid%>"; 
				mainForm.submit();
			}
			
			function cancelmanage()
			{
				mainForm.action ="<%=rootPath%>/alarmport.do?action=changeManage&value=0&ipaddress=<%=ipaddress%>&nodeid=" + "<%=nodeid%>";
				mainForm.submit();
				
			}
			function todelete()
			{
				mainForm.action ="<%=rootPath%>/alarmIndicatorsNode.do?action=delete&value=0&id="+ node + "&nodeid=<%=nodeid%>";
				mainForm.submit();
			}
			
			  function toAdd()
  			{
      				mainForm.action = "<%=rootPath%>/alarmport.do?action=list";
      				mainForm.submit();
  			}
  			  function toMultiAdd()
  			{
      				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showtomultiaddlist";
      				mainForm.submit();
  			}
			
            function doNodeFromlastoconfig()
  {  
     mainForm.action = "<%=rootPath%>/alarmport.do?action=fromLastToAlarmPort";
     mainForm.submit();
  }  
  
  
function doEmptyNode()
  {  
     mainForm.action = "<%=rootPath%>/alarmport.do?action=delete";
     mainForm.submit();
  } 
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
					onclick="parent.cancelmanage()">ȡ���ɼ�</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addmanage()">��Ӳɼ�</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.todelete()">ɾ��</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
			<input type="hidden" name="ipaddress" id="ipaddress" value="<%=ipaddress%>">
			<input type="hidden" name="type" id="type" value="<%=type%>">
	
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
									                	<td class="content-title"> �澯 >> �澯���� >> �澯��ֵ�б�(<%=ipaddress %>) </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
													<tr>
														<td class="body-data-title" style="text-align:right">
                                                            <INPUT type="button" class="formStyle" value="ˢ ��" onclick="doNodeFromlastoconfig()"/>
                                                                   <INPUT type="button" class="formStyle" value="�� ��" onclick="doEmptyNode()"/>
		        												<a href="#" onclick="toAdd()"></a>&nbsp;&nbsp;<a href="#" onclick="toMultiAdd()"></a>&nbsp;&nbsp;
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
																    <td class="body-data-title" width=5%>���</td>
													    			<td class="body-data-title" width=10%>����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">�ȽϷ�ʽ</td>
													    			<td class="body-data-title">һ����ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">������ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">һ�����ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">�������ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title">�������ڷ�ֵ</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title" width=3%>�澯</td>
													    			<td class="body-data-title" width=5%>����</td>
							        							</tr>
							        							<%
							        							
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										AlarmPort alarmPort = (AlarmPort)list.get(i);
							        										
							        										String isMonitor = "��";
							        										String bcolor="gray";
							        										if("1".equals(alarmPort.getEnabled())){
							        											isMonitor = "��";
							        											bcolor ="#3399CC";
							        										}
							        										
							        										String smsin1 = "��";
							        										if(alarmPort.getSmsin1()==1){
							        											smsin1 = "��";
							        										}
							        										
							        										String smsin2 = "��";
							        										if(alarmPort.getSmsin2()==1){
							        											smsin2 = "��";
							        										}
							        										
							        										String smsin3 = "��";
							        										if(alarmPort.getSmsin3()==1){
							        											smsin3 = "��";
							        										}
							        										
							        										String smsout1 = "��";
							        										if(alarmPort.getSmsout1()==1){
							        											smsout1 = "��";
							        										}
							        										
							        										String smsout2 = "��";
							        										if(alarmPort.getSmsin2()==1){
							        											smsout2 = "��";
							        										}
							        										
							        										String smsout3 = "��";
							        										if(alarmPort.getSmsin3()==1){
							        											smsout3 = "��";
							        										}
							        										String compare = "����";
							        										String bgcol = "#ffffff";
							        										if(alarmPort.getCompare()== 0){
							        											compare = "����";
							        											bgcol = "gray";
							        										}
							        										%>
							        										
							        										<tr <%=onmouseoverstyle%>>
							        											<td class="body-data-list"><font color='blue'>&nbsp;<%= 1+i%></font></td>
							        											
																    			<td class="body-data-list" ><%=alarmPort.getName()%></td>
																    			
																    			<td class="body-data-list" bgcolor="<%=bcolor%>"><%=isMonitor%></td>
																    			<td class="body-data-list" bgcolor="<%=bgcol%>"><%=compare%></td>
																    			<td class="body-data-list" bgcolor="yellow"><%=alarmPort.getLevelinvalue1()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelintimes1()%></td>
																    			<td class="body-data-list"><%=smsin1%></td>
																    			<td class="body-data-list" bgcolor="orange"><%=alarmPort.getLevelinvalue2()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelintimes2()%></td>
																    			<td class="body-data-list"><%=smsin2%></td>
																    			<td class="body-data-list" bgcolor="red"><%=alarmPort.getLevelinvalue3()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelintimes3()%></td>
																    			<td class="body-data-list"><%=smsin3%></td>
																    			<td class="body-data-list" bgcolor="yellow"><%=alarmPort.getLeveloutvalue1()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelouttimes1()%></td>
																    			<td class="body-data-list"><%=smsout1%></td>
																    			<td class="body-data-list" bgcolor="orange"><%=alarmPort.getLeveloutvalue2()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelouttimes2()%></td>
																    			<td class="body-data-list"><%=smsout2%></td>
																    			<td class="body-data-list" bgcolor="red"><%=alarmPort.getLeveloutvalue3()%>kb</td>
																    			<td class="body-data-list"><%=alarmPort.getLevelouttimes3()%></td>
																    			<td class="body-data-list"><%=smsout3%></td>
										        								<td align="center" class="body-data-list">
																					<img src="<%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<%=alarmPort.getId()%>','','1000') title="�Ҽ��˵�">
						       													</td>
										        							</tr>
							        										<%
							        									}
							        								}%>
							        								<tr>
																			<TD nowrap colspan="20" align=center><br>
																			<input type="button" value="�� ��" style="width:50" onclick="window.close()">
																			</TD>	
																		</tr>
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
