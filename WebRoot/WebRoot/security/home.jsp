<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
  String rootPath = request.getContextPath();  
    String menuTable = (String)request.getAttribute("menuTable");   
  String flag = (String)request.getAttribute("flag");
%>
<html>    
<head>
<title>��ȫ���</title>  
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script> 
</head>  
<body id="body" class="body">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="flag" id="flag" value="<%=flag%>">
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
									                	<td class="content-title">��ȫ���</td>
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
		        								            <table width=100% cellpadding="5" border="0">
		        								                <tr>
		        								                    <td colspan="3" align=center><b>��ȫ��Ӫ�����Ѿ�����<a href="#">10</a>�죬��ǰ�ۺ�״̬<a href="#">��ȫ</a></b><img src="<%=rootPath%>/resource/image/topo/service_up.png"></td>
		        								                </tr>
									 							<tr>
																	<td width="23%" align=left>
																		<table width="100%" cellpadding="10" cellspacing="10" border="0">
																			<tr> 
																				<td align=left>��ʷ����״����</td>
																			</tr>
																			<tr> 
																				<td align=left>��ȫ <a href="#">165</a> ��</td>
																			</tr>
																			<tr> 
																				<td align=left> ���� <a href="#">17</a> ��</td>
																			</tr>
																			<tr> 
																				<td align=left> ��Σ <a href="#">38</a> ��</td>
																			</tr>
																		</table>
																	</td>
																	<td width="23%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="200">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/P.swf?defaultview=pie&piexml=pie.xml&projectName=afunms", "P", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent");
																					</script>			
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="54%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="200">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/L.swf?defaultview=line&linexml=line.xml&projectName=afunms", "L", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent3");
																					</script>				
																				</td>
																			</tr>             
																		</table> 
																	</td>
																</tr>
															</table>
		        								        </td>
		        								    </tr>	
		        								    <tr>
		        								        <td>
		        								            <table width=100% cellpadding="5" border="0">
									 							<tr>
																	<td width="25%" align=left>
																		<table width="100%" cellpadding="10" cellspacing="10" border="0">
																			<tr> 
																				<td align=left>ϵͳ����״��</td>
																			</tr>
																			<tr> 
																				<td align=left>�¼�����(��)</td>
																			</tr>
																			<tr> 
																				<td align=left>�¼�����(��)</td>
																			</tr>
																			<tr> 
																				<td align=left>�¼�����(��)</td>
																			</tr>
																		</table>
																	</td>
																	<td width="25%" align=left>
																		<table width="100%" cellpadding="10" cellspacing="10" border="0">
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																		</table>
																	</td>
																	<td width="25%" align=center>
																		<table width="100%" cellpadding="10" cellspacing="10" border="0">
																			<tr> 
																				<td align=left>ϵͳ������״��</td>
																			</tr>
																			<tr> 
																				<td align=left>�¼�����(�е�)</td>
																			</tr>
																			<tr> 
																				<td align=left>�¼�����(�и�)</td>
																			</tr>
																			<tr> 
																				<td align=left></td>
																			</tr>
																		</table>
																	</td>
																	<td width="25%" align=left>
																		<table width="100%" cellpadding="10" cellspacing="10" border="0">
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/service_up.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																				<td align=center><img src="<%=rootPath%>/resource/image/topo/small7.png"><br></td>
																			</tr>
																			<tr> 
																				<td align=left></td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
		        								        </td>
		        								    </tr>
		        								    <tr>
       								                    <td colspan="3" align=center><b>�ܼ�����<a href="#">10</a>�죬��ǰ��������<a href="#">9</a>�죬����޹�������ʱ��Ϊ<a href="#">10</a>��</b></td>
       								                </tr>
		        								    <tr>
		        								        <td>
		        								            <table width=100%>
									 							<tr>
																	<td width="45%" align=center>
																	    <table width="100%" height="300" cellpadding="0" cellspacing="0">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent1">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=line&linexml=line1.xml&projectName=afunms&l_title=�¼��ϱ�����&p_title=�¼��ϱ�����&c_title=�¼��ϱ�����", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent1");
																					</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td width="45%" align=center>
																	    <table width="100%" height="300" cellpadding="0" cellspacing="0">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent4">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=column&columnxml=column4.xml&projectName=afunms&p_title=�豸�¼��ֲ�&c_title=�豸�¼��ֲ�&l_title=�豸�¼��ֲ�", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent4");
																					</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
		        								        </td>
		        								    </tr>
		        								     <tr>
		        								        <td>
		        								            <table width=100%>
									 							<tr>
																	<td width="45%" align=center>
																	    <table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent5">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=column&columnxml=column5.xml&projectName=afunms&p_title=ԴIP����TOP10&c_title=ԴIP����TOP10&l_title=ԴIP����TOP10", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent5");
																					</script>			
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="45%" align=center>
																	    <table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent6">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=column&columnxml=column6.xml&projectName=afunms&p_title=Ŀ��IP����TOP10&c_title=Ŀ��IP����TOP10&l_title=Ŀ��IP����TOP10", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent6");
																					</script>				
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
