<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.model.Macconfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  List districtList = (List)request.getAttribute("districtList");
  String searchDistrictId = (String)request.getAttribute("searchDistrictId");
  Hashtable hashtable = (Hashtable)request.getAttribute("hashtable");
  JspPage jp = (JspPage)request.getAttribute("page");
  
  String searchMac = (String)request.getAttribute("searchMac");
  
  
  String condition = (String)request.getAttribute("condition");
  
  if(condition == null ){
  	condition = "";
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
 			var delAction = "<%=rootPath%>/macconfig.do?action=delete";
  			var listAction = "<%=rootPath%>/macconfig.do?action=list";
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
		<script language="JavaScript">

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
			
			function toAdd()
			{
				mainForm.action = "<%=rootPath%>/macconfig.do?action=ready_add";
				mainForm.submit();
			}
			
			function edit()
			{
				mainForm.action = "<%=rootPath%>/macconfig.do?action=ready_edit&id="+node;
				mainForm.submit();
			}
			
			function exportExcel()
			{
				mainForm.action = "<%=rootPath%>/macconfig.do?action=exportExcel&jp=1&condition="+"<%=condition%>" + "&searchMac=" + "<%=searchMac%>";
				mainForm.submit();
			}
			
			
			function toImportExcel()
			{
				mainForm.action = "<%=rootPath%>/macconfig.do?action=toImportExcel";
				mainForm.submit();
			}
			
			function search(condition){
			
			    mainForm.action = "<%=rootPath%>/macconfig.do?action=list&jp=1&condition="+condition;
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
						onclick="parent.edit()">
						�༭
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
									                	<td class="content-title"> ��Դ >> ip ���ι��� >> �����б� </td>
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
									    								&nbsp;&nbsp;&nbsp;�������ѯ&nbsp;&nbsp;
									    								<select id="" name="searchDistrictId" style="width: 150px;">
									    									<option value="-1">����</option>
									    									<%
									    									if(districtList!=null && districtList.size()>0){
									    										for(int i = 0 ; i < districtList.size() ; i++){
									    											DistrictConfig districtConfig = (DistrictConfig)districtList.get(i);
									    											String check = "";
									    											if(String.valueOf(districtConfig.getId()).equals(searchDistrictId)){
									    												check = "selected='selected'";
									    											}
									    										%>
									    										<option <%=check%>  value="<%=districtConfig.getId()%>" ><%=districtConfig.getName()%></option>
									    										<%
									    										}
									    									}
									    									 %>
									    								</select>
									    								<input type="button" value="�����ѯ"  onclick='search("district")'>
														    		</td>
														    		<td class="body-data-title" style="text-align: left;">
									    								&nbsp;&nbsp;&nbsp;��Mac��ѯ&nbsp;&nbsp;
									    								<input type="text" name="searchMac" id="searchMac">
									    								&nbsp;&nbsp;
									    								<input type="button" value="Mac��ѯ"  onclick='search("mac")'>
														    		</td>
														    		<td class="body-data-title" style="text-align: left;">
														    			&nbsp;&nbsp;&nbsp;
														    			<a href="#" onclick="exportExcel()" >
											                				<img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL
											                			</a>&nbsp;&nbsp;&nbsp;
														    			<a href="#" onclick="toImportExcel()" >
											                				<img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL
											                			</a>&nbsp;&nbsp;&nbsp;
									    								<a href="#" onclick="toAdd()">���</a>
									    								&nbsp;&nbsp;&nbsp;
									    								<a href="#" onclick="toDelete()">ɾ��</a>
									    								&nbsp;&nbsp;&nbsp;
														    		</td>
			        											</tr>
															</table>
														</td>
													</tr> 
		        									<tr>
														<td>
															<table>
																<tr>
									    							<td class="body-data-title">
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
		        													<td align="center" class="body-data-title">Mac</td>
		        													<td align="center" class="body-data-title">����</td>
		        													<td align="center" class="body-data-title">Mac����</td>
		        													<td align="center" class="body-data-title">����</td>
		        												</tr>
		        												<%
		        									    if(list!=null&& list.size()>0){
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									            Macconfig macconfig = (Macconfig)list.get(i);
		        									            String districtName = "";
		        									            if(hashtable!=null){
		        									            	districtName = ((DistrictConfig)hashtable.get(macconfig.getId())).getName();
		        									            }
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=macconfig.getId()%>" name="checkbox" onclick=""><%=i+jp.getStartRow()%></td>
		        													<td align="center" class="body-data-list"><%=macconfig.getMac()%></td>
		        													<td align="center" class="body-data-list"><%=districtName%></td>
		        													<td align="center" class="body-data-list"><%=macconfig.getMacdesc()%></td>
		        													<td align="center" class="body-data-list">
																		<img title="�Ҽ��鿴����" src="<%=rootPath%>/resource/image/status.gif"
																			border="0" width=15 oncontextmenu=showMenu('2','<%=macconfig.getId()%>','','1')>
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
