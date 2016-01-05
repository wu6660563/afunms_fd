<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.topology.model.NetDistrictDetail"%>
<%@page import="com.afunms.topology.model.NetDistrictIpDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.model.IPDistrictConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");

  JspPage jp = (JspPage)request.getAttribute("page");
  
  DistrictConfig districtConfig = (DistrictConfig)request.getAttribute("districtConfig");
  
  IPDistrictConfig ipDistrictConfig = (IPDistrictConfig)request.getAttribute("ipDistrictConfig");
  
  String netDistrictId = (String)request.getAttribute("netDistrictId");
  
  String netDistrictName = (String)request.getAttribute("netDistrictName");
  
  String districtName = (String)request.getAttribute("districtName");
  
  String districtId = null;
  String ipDistrictId = null;
  if( ipDistrictConfig!=null ){
  	districtId = String.valueOf(ipDistrictConfig.getDistrictid());
  	ipDistrictId = String.valueOf(ipDistrictConfig.getId());
  }
  
  String searchIp = (String)request.getAttribute("searchIp");
  
  String isUsed = (String)request.getAttribute("isUsed");
  if(isUsed==null||isUsed.trim().length()==0){
  	isUsed="-1";
  }
  
  String isOnline = (String)request.getAttribute("isOnline");
  if(isOnline==null||isOnline.trim().length()==0){
  	isOnline="-1";
  }
  
  String beforeAction = (String)request.getAttribute("beforeAction");
  if(beforeAction==null||beforeAction.trim().length()==0){
  	beforeAction = "netDistrictIpDetails";
  }
  
  
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
 			var totalpages = <%=jp.getPageTotal()%>;
  			var listAction = "<%=rootPath%>/ipDistrictMatch.do?action=" + "<%=beforeAction%>"+"&netDistrictId="
  						   + "<%=netDistrictId%>" +"&districtId="+"<%=districtId%>" 
  						   + "&ipDistrictId=" + "<%=ipDistrictId%>";
  						   
		</script>
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
		<script type="text/javascript">
			
			function searchByIp(){
				mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=searchNetDistrictIpByIp&jp=1&netDistrictId="
  						   + "<%=netDistrictId%>" +"&districtId="+"<%=districtId%>" 
  						   + "&ipDistrictId=" + "<%=ipDistrictId%>";
  			    mainForm.submit();
			}
			
			function searchByWhere(){
				mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=searchNetDistrictIpByWhere&jp=1&netDistrictId="
  						   + "<%=netDistrictId%>" +"&districtId="+"<%=districtId%>" 
  						   + "&ipDistrictId=" + "<%=ipDistrictId%>";
  			    mainForm.submit();
			}
			
			function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}    
			
			
			
			function createReport(){
				CreateWindow(  "<%=rootPath%>/ipDistrictMatch.do?action=createReport&beforeAction=" + "<%=beforeAction%>" + "&netDistrictId="
  						     + "<%=netDistrictId%>" +"&districtId="+"<%=districtId%>" + "&ipDistrictId=" + "<%=ipDistrictId%>" 
  						     + "&searchIp=" +  "<%=searchIp%>" +"&isUsed=" + "<%=isUsed%>" + "&isOnline=" + "<%=isOnline%>");
			}
		</script>
		<script type="text/javascript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip,showItemMenu)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        return false;
			    }
			    else{
			    	popMenu(itemMenu,100,showItemMenu);
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    
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
			            this.style.background="#99CCFF";
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
			    pop.show(event.clientX-1,event.clientY,width,rowCount*30,document.body);
			    return true;
			}
			 
			
			function portscanforOne(){
				
					CreateWindow(  "<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=" + ipaddress );
				
			}
			
			function portscan(){
				var checkboxes = document.getElementsByName("checkbox");
				var ipaddress = "";
				var result = false;
				for(var i = 0 ; i < checkboxes.length ; i++){
					var checkbox = checkboxes[i];
					if(checkbox.checked){
						result = true;
						ipaddress = ipaddress + checkbox.value + "-"; 
					}
				}
					CreateWindow(  "<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=" + ipaddress );
				
			}
			
			function addPortScan(){
				
				CreateWindow(  "<%=rootPath%>/ipDistrictMatch.do?action=ready_addPortScan&ipaddress=" + ipaddress );
			}
			
			function searchbyip(){
				CreateWindow(  "<%=rootPath%>/ipDistrictMatch.do?action=searchPortScanByIp&ipaddress=" + ipaddress );
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
						onclick="parent.portscanforOne()">
						ɨ��˿�
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.searchbyip()">
						�鿴��ʷ����
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addPortScan()">
						���ɨ��˿�
					</td>
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
									                	<td class="content-title"> ��Դ >> ip ���ι��� >> ip�б� </td>
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
															<table class="body-data-title">
											                	<tr class="body-data-title">
											                		<td class="body-data-title" style="text-align: left;height: 25px">
											                			&nbsp;&nbsp;&nbsp;<%=districtName%>&nbsp;&nbsp;���Σ�<%=netDistrictName%>
											                			&nbsp;&nbsp;&nbsp;��IP��ѯ
											                			&nbsp;&nbsp;&nbsp;<input type="text" name="searchIp" width="150px" value="" >
											                			&nbsp;&nbsp;<input type="button" style="width: 80px" value="��ѯ" onclick="searchByIp()">
											                		</td>	
											                		<td class="body-data-title" style="text-align: right;height: 25px">
											                			<a href="#" onclick="portscan()" >�˿�ɨ��</a>&nbsp;&nbsp;&nbsp;&nbsp;
											                		</td>
											                		<td class="body-data-title" style="text-align: right;height: 25px">
											                			<a href="#" onclick="createReport()" >
											                				<img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL
											                			</a>&nbsp;&nbsp;&nbsp;&nbsp;
											                		</td>
												                </tr>
												                <tr class="body-data-title">
											                		<td class="body-data-title" colspan="2" style="text-align: left;height: 25px">&nbsp;&nbsp;&nbsp;�Ƿ���䣺&nbsp;&nbsp;
											                			<select id="isUsed" name="isUsed" style="width: 150px;">
											                				<option value="-1">����</option>
											                				<option value="0">δ����</option>
											                				<option value="1">�ѷ���</option>
											                			</select>
											                			&nbsp;&nbsp;&nbsp;�Ƿ�����
											                			<select id="isOnline" name="isOnline" style="width: 150px;">
											                				<option value="-1">����</option>
											                				<option value="0">����</option>
											                				<option value="1">����</option>
											                			</select>
											                			&nbsp;&nbsp;<input type="button" style="width: 80px" value="��ѯ" onclick="searchByWhere()">
											                		</td>	
												                </tr>
												        	</table>
					        							</td>
					        						</tr>
		        									<tr>
														<td>
															<table class="body-data-title">
											                	<tr>
											                		<td>
													                	<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
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
		        													<td align="center" class="body-data-title">ip</td>
		        													<td align="center" class="body-data-title">�Ƿ����</td>
		        													<td align="center" class="body-data-title">����״̬</td>
		        													<td align="center" class="body-data-title">����</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									            NetDistrictIpDetail netDistrictIpDetail = (NetDistrictIpDetail)list.get(i);
					        									            
					        									            String isUsed_str = "��";
					        									            String isUsed_img = "resource/image/topo/status6.png";
					        									            if("1".equals(netDistrictIpDetail.getIsUsed())){
					        									            	isUsed_str = "��";
					        									            	isUsed_img = "resource/image/topo/status_ok_1.gif";
					        									            }
					        									            
					        									            String isOnline_str = "����";
					        									            String isOnline_img = "resource/image/topo/status6.png";
					        									            if("1".equals(netDistrictIpDetail.getIsOnline())){
					        									            	isOnline_str = "����";
					        									            	isOnline_img = "resource/image/topo/status_ok_1.gif";
					        									            }
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
					        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=netDistrictIpDetail.getIpaddress()%>" name="checkbox"><%=jp.getStartRow()+i%></td>
					        													<td align="center" class="body-data-list"><%=netDistrictIpDetail.getIpaddress()%></td>
					        													<td align="center" class="body-data-list"><img src="<%=isUsed_img%>">&nbsp;<%=isUsed_str%></td>
					        													<td align="center" class="body-data-list"><img src="<%=isOnline_img%>">&nbsp;<%=isOnline_str%></td>
								        										<td align="center" class="body-data-list">
																					<img title="�Ҽ��鿴����" src="<%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','','<%=netDistrictIpDetail.getIpaddress()%>','111')>
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
	<script type="text/javascript">
		function selectWhere(){
			document.getElementById("isUsed").value = "<%=isUsed%>";
			document.getElementById("isOnline").value = "<%=isOnline%>";
		}
		selectWhere();	
	</script>
</html>
