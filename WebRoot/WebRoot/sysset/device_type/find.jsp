<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.sysset.model.DeviceType"%>
<%@page import="com.afunms.sysset.util.DeviceTypeView"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  DeviceType vo = null;
  try
  {
     vo = (DeviceType)request.getAttribute("vo");
  }
  catch(Exception e)
  {
     vo = null;
  }
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
		
		
		<script type="text/javascript">
			  function doFind()
			  {
			     mainForm.action = "<%=rootPath%>/devicetype.do?action=find";
			     mainForm.submit();     
			  }
		
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
		<script>
			function refresh(){
				if(window.confirm("ͬ��ˢ�������ʱ��Ƚϳ�! ���Ὣ��������Ϣȫ�����! �Ƿ����?")){
					Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
					mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=list&refresh=refresh"; 
					mainForm.submit();
				}
				
			}
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
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
									                	<td class="content-title"> ϵͳ���� >> �豸�ͺ� >> �豸�ͺŲ�ѯ�б�</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr >
														<td class="body-data-title" colspan="7">
															<table width="100%" >
																<tr>
									    							<td class="body-data-title" style="text-align: left">��[<b>ϵͳOID</b>]��ѯ&nbsp;<input type="text" name="sys_oid" size="30" class="formStyle">
        															<input type="button" value="��ѯ" style="width:60" class="button" onclick="doFind()">
        															</td>
			        											</tr>
															</table>
														</td>
													</tr> 
		        									<tr>
       													<td align="center" class="body-data-title">ͼ��</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">ϵͳOID</td>
       													<td align="center" class="body-data-title">������</td>
       													<td align="center" class="body-data-title">����</td>
       													<td align="center" class="body-data-title">�༭</td>
		        									</tr>
		        									<%
														if(vo!=null){
													    	DeviceTypeView view = new DeviceTypeView();
													%>           
													<tr <%=onmouseoverstyle%>>
       													<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/device/<%=vo.getImage()%>"></td>
       													<td align="center" class="body-data-list"><%=vo.getDescr()%></td>
       													<td align="center" class="body-data-list"><%=vo.getSysOid()%></td>
       													<td align="center" class="body-data-list"><%=view.getProducer(vo.getProducer())%></td>
       													<td align="center" class="body-data-list"><%=view.getCategory(vo.getCategory())%></td>
       													<td align="center" class="body-data-list"><a href="<%=rootPath%>/devicetype.do?action=ready_edit&id=<%=vo.getId()%>">
														<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
		        									</tr>
		        									            
		        									<% 
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
