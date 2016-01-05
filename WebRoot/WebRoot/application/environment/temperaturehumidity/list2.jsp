<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.SerialNode"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/JavaScript">
			var show = true;
			var hide = false;
			//修改菜单的上下箭头符号
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
			//添加菜单	
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


	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">
		<form method="post" name="mainForm">
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
					<td id="td-container-main" class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td>
									<table id="container-main-content" class="container-main-content">
										<%for(int i =0; i < list.size(); i++){
											int id = ((SerialNode)list.get(i)).getId();
											%>
												<tr style="margin-bottom: 15px;">
													<td width="50%">
														<table width="100%">
															<tr>
										                		<td>
										                			<table width="100%" border="0" cellpadding="0" cellspacing="0">
											              				<tr> 
											                				<td  align=center> 
								                								<div id="flashcontent<%=i%>">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Com_Temp.swf?id=<%=id%>", "Com_Temp", "500", "300", "8", "#ffffff");
																					so.write("flashcontent"+"<%=i%>");
																				</script>
																			</td>
																		</tr>
																	</table>
				        										</td>
							        						</tr>
														</table>
													</td>
												<%
												if(i<list.size()-1){
												%>
													<td width="50%">
														<table width="100%">
															<tr>
										                		<td>
										                			<table width="100%" border="0" cellpadding="0" cellspacing="0">
											              				<tr> 
											                				<td  align=center> 
								                								<div id="flashcontent<%=i+1%>">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Com_Temp.swf?id=<%=((SerialNode)list.get(i+1)).getId()%>", "Com_Temp", "500", "300", "8", "#ffffff");
																					so.write("flashcontent"+"<%=i+1%>");
																				</script>
																			</td>
																		</tr>
																	</table>
				        										</td>
							        						</tr>
														</table>
													</td>
												<%
												}else{
												%>
													<td>&nbsp;</td>
												<%
												} 
												%>
												</tr>
												<tr>
													<td><br/></td>
												</tr>
										<%
										i=i+1;
										} 
										%>
										
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
