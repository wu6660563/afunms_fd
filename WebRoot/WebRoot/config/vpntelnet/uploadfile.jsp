<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.model.HostNode"%>

<% 
	String rootPath = request.getContextPath();    
	String mypage = (String)request.getAttribute("page");
	String ipaddress = (String)request.getAttribute("ipaddress");
	String id = (String)request.getAttribute("id");
	String fileName = (String)request.getAttribute("fileName");
	String filena = fileName.substring(fileName.lastIndexOf("\\")+1);
	System.out.println(fileName+"1111111111111111111111");
%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->

<script language="JavaScript" type="text/javascript">


Ext.onReady(function(){  	
 Ext.get("process").on("click",function(){
  
    //var chk = checkinput("alias","string","机器名",30,false);
     //if(chk)
     {
     	Ext.MessageBox.wait('数据加载中，请稍后.. ');
        mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=uploadFile";
        mainForm.submit();
        //window.close();
     }
       // mainForm.submit();
 });	
	
});
</script>
</head>
<body id="body" class="body">


	<!-- 右键菜单结束-->
   <form name="mainForm" method="post" ENCTYPE="multipart/form-data">
      <input type=hidden name="id" value="<%=id %>">
      <input type=hidden name="ipaddress" value="<%=ipaddress %>">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">() >>&nbsp; 设备详细信息>>&nbsp;备份配置</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																			<tr style="background-color: #ECECEC;">
													                  					<TD nowrap align="right" height="24" width="10%">文件名&nbsp;</TD>
													                  					<TD nowrap width="40%">&nbsp;
													                  					<input type="text" name="filena" maxlength="50" size="50" class="formStyle" value="<%=filena %>" readonly="readonly">
													                  					<input type="hidden" name="fileName" value="<%=fileName %>">
													                  					</TD>
													                				</tr>
																			<tr >
																			<tr>
													                  					<TD nowrap align="right" height="24" width="10%">文件路径&nbsp;</TD>
													                  					<TD nowrap width="40%">&nbsp;
													                  					<input type="file" name="filePath" maxlength="50" size="50" class="formStyle" style="height:22px">
													                  					</TD>
													                		</tr>
																			<tr style="background-color: #ECECEC;"> 
													  							<TD nowrap align="right" height="24" width="10%">文件备注&nbsp;</TD>       
																				<TD nowrap width="40%" colspan=3>&nbsp;
																					<input type="text" id="fileDesc" name="fileDesc" maxlength="32" size="50" value="">&nbsp;&nbsp; 
																				</td>
													 						</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																				<br><input type="button" value="上  传" style="width:50" id="process">&nbsp;&nbsp;
																					<input type="reset" style="width:50" value="重  置">&nbsp;&nbsp;
																					<input type="reset" style="width:50" value="关  闭" onclick="window.close();">
																				</TD>	
																			</tr>	
																		</TABLE>
										 							
										 							
				        										</td>
				        									</tr>
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
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
				</td>
			</tr>
		</table>
		
	</form>
</BODY>


</HTML>