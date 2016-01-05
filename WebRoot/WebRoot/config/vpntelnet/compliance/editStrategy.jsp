<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@page import="java.util.*"%>

<%@page import="com.afunms.config.model.CompGroupRule"%>
<%@page import="com.afunms.config.model.CompStrategy"%>


<% 

	String rootPath = request.getContextPath(); 
    List list=(List)request.getAttribute("list");
    CompStrategy vo=(CompStrategy)request.getAttribute("vo");
    int id=vo.getId();
    String violateType0="";
    String violateType1="";
    String type0="";
    String type1="";
    if(vo.getType()==0){
    type0="checked";
    }else if(vo.getType()==1){
    type1="checked";
    }
    if(vo.getViolateType()==0){
    violateType0="checked";
    }else if(vo.getViolateType()==0){
    violateType1="checked";
    }
    
    String[] temp=new String[vo.getGroupId().split(",").length];
    temp=vo.getGroupId().split(",");
    Vector<String> vector=new Vector<String>();
    for(int i=0;i<temp.length;i++){
      vector.add(temp[i]);
    }
%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/javascript">

function updateStrategy(){

 Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
 mainForm.action = "<%=rootPath%>/configRule.do?action=updateStrategy";
 mainForm.submit();
 parent.opener.location.href="<%=rootPath%>/configRule.do?action=strategyList";
 window.close();
}

function CreateWindow(url)
{
msgWindow=window.open(url,"_blank","toolbar=no,width=650,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
}    

function showDetail(id){
return CreateWindow("<%=rootPath%>/configRule.do?action=pureRuleList&id="+id);
}
</script>


</head>
<body id="body" >


	<!-- �Ҽ��˵�����-->
   <form name="mainForm" method="post">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td bgcolor="#FFFFFF">
										<input type="hidden" id="id" name="id" value="<%=id %>">
											<table id="add-content" class="add-content" border=1>
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header" >
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">&nbsp; �Զ���>> �����ļ�����>> ���Թ���>> ����>> �޸Ĳ���</td>
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
																			<tr>
																			    <td nowrap align="left" height="24" width="20%">�������ƣ�&nbsp;&nbsp;</td>
																			    <td width="80%" align="left"><input type="text" name="name" id="name" maxlength="100" size="41" class="formStyle" value="<%=vo.getName() %>"></td>
													                  		</tr>
																			<tr>
																			     <td nowrap align="left" height="24" width="20%" valign=top> &nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
																			     <td width="80%"><textarea name="description" id="description" rows="5" cols="40"><%=vo.getDescription() %></textarea></td>
																		   </tr>
																			<tr>
																			     <td nowrap align="left" height="24" width="20%" valign=top>�������ͣ�</td>
																			     <td width="80%"> 
																			        <input type="radio" name="type"  value="0" <%=type0 %>>��������&nbsp;&nbsp;
																			        <input type="radio" name="type"  value="1" <%=type1 %>>����
																			     </td>
																		   </tr>
																			<tr>
																			     <td nowrap align="left" height="24" width="20%" valign=top>����Υ����׼��</td>
																			     <td width="80%">
																			        <input type="radio" name="violateType"  value="0" <%=violateType0 %>>Υ�������е��������<br>
																			        <input type="radio" name="violateType"  value="1"<%=violateType1 %>>��Υ�������еĹؼ�����Ҫ����    
                                                                                 </td>
																		   </tr>
																		   <tr>
																			     <td align=left ><font size="2" color="red">����������</font></td>
																			     <td></td>
																		   </tr>
																		        <td colspan=2>
																		            <div id="title">
																		                 <table>
																		                        <tr>
																		                            <td class="report-data-body-title" width="10%">&nbsp;<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">���</td>
																		                            <td class="report-data-body-title" width="45%">��������</td>
																		                            <td class="report-data-body-title" width="45%">����</td>
																		                        </tr>
																		                 </table>     
																			        </div>
																			        <div id="groupRule" style="height:150px;overFlow:auto;">
																			             <table >
																			             <%if(list!=null&&list.size()>0){
																			                for(int i=0;i<list.size();i++){
																			                 CompGroupRule groupRule=(CompGroupRule)list.get(i);
																	                       String check="";
																	                       if(vector.contains(groupRule.getId()+""))
																	                       check="checked";
																			              %>
																			                    <tr >
																			                        <td align="center"  width="10%" height=30><INPUT type="checkbox" class=noborder name="checkbox" value="<%=groupRule.getId()%>" <%=check %>><%=i+1 %></td>
																			                        <td  align="left"  width="45%" height=30><span  onclick="showDetail('<%=groupRule.getId() %>')" >&nbsp;&nbsp;**&nbsp;&nbsp;</span><%=groupRule.getName() %></td>
																			                        <td align="left"  width="45%" height=30><%=groupRule.getDescription() %></td>
																			                    </tr>
																			                     
																			                    <%
																			                      } 
																			                    }    
																			                    %>
																			             </table>
																			        </div>
																		        </td>
																			
																			<tr>
																				<TD nowrap colspan="4" align=center colspan=2>
																				<br><input type="button" value="�� ��" style="width:50" id="process2" onclick="updateStrategy()">&nbsp;&nbsp;
																					
																					<input type="reset" style="width:50" value="ȡ ��" onclick="window.close();">
																				</TD>	
																			</tr>	
																		</TABLE>
										 							
										 							
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