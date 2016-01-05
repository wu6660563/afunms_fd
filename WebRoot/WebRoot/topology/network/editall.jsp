<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>

<%
 String rootPath = request.getContextPath(); 
  BusinessDao bussdao = new BusinessDao();
   List allbuss = null;
   try{
  allbuss =  bussdao.loadAll();  
   }catch(Exception e){
   }finally{
   bussdao.close();
   }
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
</head>
<script language="javascript">
  function toUpdateBid()
  {
        mainForm.action = "<%=rootPath%>/network.do?action=updateBid";
        mainForm.submit();
		window.close();
		
		window.opener.location.href=window.opener.location.href;
		self.opener.location.reload();
		window.opener.location.reload(); 

        

    
  }
</script>


</script>
<BODY id="body" class="body" leftmargin="0" topmargin="0">
<form name="mainForm" method="post">



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
											                	<td class="add-content-title"><b>资源>>&nbsp;&nbsp;设备维护&nbsp;&nbsp;>>&nbsp;&nbsp;权限设置</b></td>
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
				        										
				        												<table border="0" cellpadding="0" cellspacing="0" width="100%">
										<tr align="right" > 
							  <td colspan=10 align=left height=18>
							  <table width="100%" cellSpacing="0"  cellPadding="0" border=0  class="noprint">
							  <tr>
					<TD align="right">
						<input type="hidden" name="ids" value="<%=(String)request.getAttribute("hostid")%>">
						<input type="button" value="保存" style="width:50" class="formStylebutton" onClick="toUpdateBid()" align=center>&nbsp;&nbsp;
						<input type=reset class="formStylebutton" style="width:50" value="取消" onClick="javascript:window.close()" align=center>&nbsp;&nbsp;					
					</TD>	
				</tr> </table>
							    </td>
							  </tr>
									<tr>									
			                     <td>
											       <table width="100%"   algin="center" bgcolor="#ECECEC">
                        					 
                        									<% 
															if( allbuss.size()>0){
           												for(int i=0;i<allbuss.size();i++){
           													Business buss = (Business)allbuss.get(i);
           													String checkflag = "";
           													//if(bidlist.contains(buss.getId()+""))checkflag="checked";
           													
                        									%>                  										                        								
                    										<tr> 
                    											<td height="28" align="center">&nbsp;<INPUT type="checkbox" class=noborder name=checkboxbid value="<%=buss.getId()%>">&nbsp;<%=buss.getName()%></td>
                    										</tr>  
                    										<%
                    											}
                    										}
                    										%>                  										                 										                      								
												 </table>			
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

</body>
</html>