<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
  String rootPath = request.getContextPath();  
  User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
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
		        								            <table width=100% cellpadding="5">
									 							<tr>
																	<td width="33%" align=center>
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
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=column&linexml=eventline.xml&piexml=pie2.xml&columnxml=column2.xml&projectName=afunms&p_title=�¼����ͷ���&l_title=��״ͼ&c_title=�¼����ͷ���", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent2");
																					</script>			
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=pie&piexml=pie3.xml&projectName=afunms&p_title=�¼��ȼ��ֲ�&c_title=�¼��ȼ��ֲ�&l_title=�¼��ȼ��ֲ�", "CLP", "99%", "99%", "8", "#ffffff");
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
		        								            <table width=100%>
									 							<tr>
																	<td width="33%" align=center>
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
																	<td width="33%" align=center>
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
																	<td width="33%" align=center>
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
		        								     <tr>
		        								        <td>
		        								            <table width=100%>
									 							<tr>
																	<td width="33%" align=center>
																		<table width="100%" height="300" cellpadding="0" cellspacing="0">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent7">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=line&linexml=line7.xml&projectName=afunms&p_title=���������&c_title=���������&l_title=���������", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent7");
																					</script>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent8">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=line&linexml=line8.xml&projectName=afunms&p_title=ϵͳ����&c_title=ϵͳ����&l_title=ϵͳ����", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent8");
																					</script>			
																				</td>
																			</tr>             
																		</table> 
																	</td>
																	<td width="33%" align=center>
																		<table cellpadding="0" cellspacing="0" width=100% height="300">
																			<tr> 
																				<td align=center> 
																					<div id="flashcontent9">
																						<strong>You need to upgrade your Flash Player</strong>
																					</div>
																					<script type="text/javascript">
																						var so = new SWFObject("<%=rootPath%>/flex/CLP.swf?defaultview=column&columnxml=column9.xml&projectName=afunms&p_title=�����TOP10&c_title=�����TOP10&l_title=�����TOP10", "CLP", "99%", "99%", "8", "#ffffff");
																						so.write("flashcontent9");
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
