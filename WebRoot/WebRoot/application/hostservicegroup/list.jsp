<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.portscan.model.PortScanConfig"%>
<%@page import="com.afunms.portscan.model.PortConfig"%>
<%@page import="com.afunms.application.model.ProcessGroup"%>
<%@page import="com.afunms.application.model.HostServiceGroup"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  String ipaddress = (String)request.getAttribute("ipaddress");
  String nodeid = (String)request.getAttribute("nodeid");
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
		<script language="javascript">
			var delAction = "<%=rootPath%>/hostservicegroup.do?action=delete";
		  	var listAction = "<%=rootPath%>/hostservicegroup.do?action=list";
		  
		  	function toAdd()
		  	{
		    	mainForm.action = "<%=rootPath%>/hostservicegroup.do?action=ready_add";
		    	mainForm.submit();
		  	}
		  
		  	function detail(id)
		  	{
				mainForm.action = "<%=rootPath%>/hostservicegroup.do?action=tomcat_jvm&id="+id;
				mainForm.submit();
		  	}
		  	
		  	function edit(id)
		  	{
				mainForm.action = "<%=rootPath%>/hostservicegroup.do?action=ready_edit&groupId="+id;
				mainForm.submit();
		  	}
		  
		</script>
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
	</head>
	<body id="body" class="body" onload="initmenu();" >
		<form id="mainForm" method="post" name="mainForm">
			<table>
				<tr>
					<td class="td-container-main" style="border: none; ">
						<table>
							<tr>
								<td >
									<table>
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> Ӧ�� >> ������������� >> �������б� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
		        										<td align="center" class="body-data-title" style="text-align: left;">
		        											IP��ַ��
		        											<input type="text" name="ipaddress" id="ipaddress" value="<%=ipaddress%>">
		        											<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
       													</td>
       													<td align="center" class="body-data-title" style="text-align: right;">
       														<a href="#" onclick="toAdd()">���</a>
       														<a href="#" onclick="toDelete();">ɾ��</a>&nbsp;&nbsp;&nbsp;
       													</td>
       												</tr>
		        									<tr>
		        										<td colspan="2">
		        											<table>
		        												<tr>
		        													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
		        													<td align="center" class="body-data-title">IP��ַ</td>
		        													<td align="center" class="body-data-title">����������</td>
		        													<td align="center" class="body-data-title">�澯�ȼ�</td>
		        													<td align="center" class="body-data-title">���״̬</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	HostServiceGroup hostServiceGroup = (HostServiceGroup)list.get(i);
					        									        	
					        									        	String alarmlevel = hostServiceGroup.getAlarm_level();
					        									        	
					        									        	String monflag = hostServiceGroup.getMon_flag();
					        									        	
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
					        									        	
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
					        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=hostServiceGroup.getId()%>" name="checkbox" id="checkbox"><%=i + 1%></td>
					        													<td align="center" class="body-data-list"><%=hostServiceGroup.getIpaddress()%></td>
					        													<td align="center" class="body-data-list"><a href="#" onclick='edit("<%=hostServiceGroup.getId()%>")'><%=hostServiceGroup.getName()%></a></td>
								        										<td align="center" class="body-data-list"><%=alarmlevel%></td>
								        										<td align="center" class="body-data-list"><%=monflag_str%></td>
								        									</tr>
					        									            
					        									            <% 
					        									            	}
					        									            }
					        									 %>
		        											</table>
		        										</td>
		        									</tr>
		        									<tr>
		        									<td align=center colspan=6><br>
		        										<input type="reset" style="width:50" value="�� ��" onclick="javascript:window.close()">&nbsp;&nbsp;
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
