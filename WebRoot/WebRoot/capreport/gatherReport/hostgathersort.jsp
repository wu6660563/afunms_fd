<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>

<%@page import="com.afunms.topology.model.MonitorNodeDTO"%>
<%@page import="com.afunms.polling.om.Diskcollectdata"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.*"%>



<%
  String runmodel = PollingEngine.getCollectwebflag();
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  String menuTable = (String) request.getAttribute("menuTable");
  
  
  String nameImg = "";
  
  String categoryImg = "";
  
  String ipaddressImg = "";
  
  String pingImg = "";
  
  String cpuImg = "";
  
  String memoryImg = "";
  
  String inutilhdxImg = "";
  
  String oututilhdxImg = "";
  
  String imgSrc = "";
  
  	String memcollecttime = "";
	String pingcollecttime = "";
	String responsevalue = "";
	String collecttime = "";
	String pingvalue= "0";
	String cpuUsed = "0";
	String cpuUnUsed = "";
	if(cpuUsed.equals("0"))cpuUnUsed="100";
	String memeryUsed = "0";
	
	String memeryUnUsed = "";
	if(memeryUsed.equals("0"))memeryUnUsed="100";
	String pingValue = "0";
  
  
  
  String treeBid = (String)request.getAttribute("treeBid");
  
  Hashtable<String,String> hashtable = (Hashtable<String,String>) request.getAttribute("sindexHash");
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
			function detail()
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node+"&flag=1";
			}
		</script>
		<script type="text/javascript">
	  		
  			
  			function search(){  
				mainForm.action = "<%=rootPath%>/gatherReport.do?action=hostsortList";
     			mainForm.submit();
  			}
  			
  			function test(){
  				//���Ÿ澯����
				mainForm.action = "<%=rootPath%>/gatherReport.do?action=test";
     			mainForm.submit();
  			}
  			
  			function download() {
  				//download
  				mainForm.action = "<%=rootPath%>/gatherReport.do?action=download";
     			mainForm.submit();
  			}
			
			$(document).ready(function(){
			});
		</script>
	</head>
	<%
	%>
	<body id="body" class="body" onload="initmenu();" leftmargin="0" topmargin="0">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden id="treeBid" name="treeBid" value="<%=treeBid%>">
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
									                	<td class="content-title">&nbsp;���� >> �ۺϱ��� >> ����������ͳ�� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
													<tr>
														<td >
															<table>
																<tr>
																	<td><B>����(�������Ϊ������������ֵ):</B></td>
																</tr>
																<tr>
																	<td class="body-data-title" style="text-align: left;" >
								          								CPU������(%):<INPUT type="text" name="cpu" id="cpu" style="width:50px;">
								          								˳��<select name="cpuorder" id="cpuorder">
								          									<option value="1" selected="selected">1</option>
								          									<option value="2">2</option>
								          									<option value="3">3</option>
								          								</select>&nbsp;&nbsp;
								          								�ڴ�������(%):<INPUT type="text" name="memory" id="memory" style="width:50px;">
								          								˳��<select name="memoryorder" id="memoryorder">
								          										<option value="1">1</option>
								          										<option value="2" selected="selected">2</option>
								          										<option value="3">3</option>
								          									</select>&nbsp;&nbsp;
								          								����������(%):<INPUT type="text" name="disk" id="disk" style="width:50px;">
								          								˳��<select name="diskorder" id="diskorder">
								          									<option value="1">1</option>
								          									<option value="2">2</option>
								          									<option value="3" selected="selected">3</option>
								          								</select>&nbsp;&nbsp;
								          								<INPUT type="button" class="formStyle" value="��  ѯ" onclick="search();">
								          								<INPUT type="button" class="formStyle" value="��  ��" onclick="download();">
								          							</td>       
																</tr>
															</table>
											  			</td>
													</tr>
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<td align="center" class="body-data-title" width="5%"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
		        													<td align="center" class="body-data-title" width="10%">����</td>
		        													<td align="center" class="body-data-title" width="15%">����</td>
		        													<td align="center" class="body-data-title" width="15%">IP��ַ</td>
		        													<td align="center" class="body-data-title" width="5%">������</td>
		        													<td align="center" class="body-data-title" width="15%">CPU(%)</td>
		        													<td align="center" class="body-data-title" width="10%">�ڴ�(%)</td>
		        													<td align="center" class="body-data-title" width="30%">����(%)</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO)list.get(i);
					        									        	pingValue = monitorNodeDTO.getPingValue();
					        									        	cpuUsed = monitorNodeDTO.getCpuValue();
					        									        	cpuUnUsed = String.valueOf(100 - Double.valueOf(cpuUsed));
					        									        	memeryUsed = monitorNodeDTO.getMemoryValue();
					        									        	memeryUnUsed = String.valueOf(100 - Double.valueOf(memeryUsed));
					        									        	String cpuValueColor = monitorNodeDTO.getCpuValueColor();
					        									        	String memeryValueColor = monitorNodeDTO.getMemoryValueColor();
					        									        	String status = monitorNodeDTO.getStatus();
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
						        									            <td align="center" class="body-data-list"><INPUT type="checkbox" id="checkbox" name="checkbox" value="<%=monitorNodeDTO.getId()%>"><%=i+1%></td>
					        													<td align="left" class="body-data-list"><a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=monitorNodeDTO.getId()%>&flag=1"><%=monitorNodeDTO.getAlias()%></a></td>
					        													<td align="center" class="body-data-list"><%=monitorNodeDTO.getCategory()%></td>
					        													<td align="left" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td><%=monitorNodeDTO.getIpAddress() %></td>
                    																	</tr>
												                      				</table>
                    															</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=pingValue%>%</td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center"><%=cpuUsed%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      				
																                      					<td width="<%=cpuUsed%>%" bgcolor="<%=cpuValueColor%>"></td>
																                      					<td width="<%=cpuUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list" >
					        														<table>
													                      				<tr>
													                      					<td  align="center"><%=memeryUsed%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=memeryUsed%>%" bgcolor="<%=memeryValueColor%>"></td>
																                      					<td width="<%=memeryUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list" >
					        														<%if(monitorNodeDTO.getHardDisk() != null && monitorNodeDTO.getHardDisk().size() > 0) {
					        															for(int j = 0;j < monitorNodeDTO.getHardDisk().size();j++){
					        																Diskcollectdata diskcollectdata = (Diskcollectdata) monitorNodeDTO.getHardDisk().get(j);
					        																Double unUsedDisk = 100 - Double.parseDouble(diskcollectdata.getThevalue());
					        																%>
					        														<table>
													                      				<tr>
													                      					<td align="left"><%=diskcollectdata.getSubentity()%></td>
													                      					<td align="right"><%=diskcollectdata.getThevalue()%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr title="<%=diskcollectdata.getThevalue()%>%">
																                      					<td width="<%=diskcollectdata.getThevalue()%>%" bgcolor="<%=hashtable.get(diskcollectdata.getId()+":"+diskcollectdata.getSubentity())%>"></td>
																                      					<td width="<%=unUsedDisk%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      				</tr>
												                      				</table>
					        																<%
					        															}
					        															
					        														} %>
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
