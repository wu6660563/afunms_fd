<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.CompRule"%>
<%@page import="com.afunms.config.dao.DetailCompRuleDao"%>
<%@page import="com.afunms.config.model.DetailCompRule"%>
<%@page import="com.afunms.config.model.CompGroupRule"%>


<% 

	String rootPath = request.getContextPath(); 
    List list=(List)request.getAttribute("list");
    CompGroupRule vo=(CompGroupRule)request.getAttribute("compGroupRule");
    int id=vo.getId();
     Vector<String> vector=new Vector<String>();
    if(vo!=null){
      String ruleId=vo.getRuleId();
      String[] ruleIds=new String[ruleId.split(",").length];
     ruleIds=ruleId.split(",");
    
     for(int i=0;i<ruleIds.length;i++){
       vector.add(ruleIds[i]);
     }
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

function updateGroupRule(){

 Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
 mainForm.action = "<%=rootPath%>/configRule.do?action=updateGroupRule";
 mainForm.submit();
 parent.opener.location.href="<%=rootPath%>/configRule.do?action=groupRuleList";
 window.close();
}

function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
}    

function showDetail(id,divId){

document.getElementById(id).innerHTML="<span  onclick=\"hiddenDetail(\'"+id+"\',\'"+divId+"\')\" >&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;</span>";
document.getElementById(divId).style.display = "block";

}
function hiddenDetail(id,divId){
document.getElementById(id).innerHTML="<span  onclick=\"showDetail(\'"+id+"\',\'"+divId+"\')\">&nbsp;&nbsp;+&nbsp;&nbsp;&nbsp;</span>";
document.getElementById(divId).style.display = "none";

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
											<table id="add-content" class="add-content" border=1>
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header" >
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">&nbsp; �Զ���>> �����ļ�����>> ���Թ���>> ������>> �޸Ĺ�����</td>
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
				        										<input type="hidden" id="id" name="id" value="<%=id %>">
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																			<tr>
																			    <td nowrap align="left" height="24" width="20%">���������ƣ�&nbsp;&nbsp;</td>
																			    <td width="80%" align="left"><input type="text" name="name" id="name" maxlength="100" size="41" class="formStyle" value="<%=vo.getName() %>"></td>
													                  		</tr>
																			<tr>
																			     <td nowrap align="left" height="24" width="20%" valign=top> &nbsp;&nbsp;��&nbsp;&nbsp;��&nbsp;&nbsp;��</td>
																			     <td width="80%"><textarea name="description" id="description" rows="5" cols="40"><%=vo.getDescription() %></textarea></td>
																		   </tr>
																		   <tr>
																			     <td align=left ><font size="2" color="red">��������</font></td>
																			     <td></td>
																		   </tr>
																		        <td colspan=2>
																		            <div id="groupRuleTitle">
																		                 <table>
																		                        <tr>
																		                            <td class="report-data-body-title" width="10%">&nbsp;<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">���</td>
																		                            <td class="report-data-body-title" width="45%">��������</td>
																		                            <td class="report-data-body-title" width="45%">����</td>
																		                        </tr>
																		                 </table>     
																			        </div>
																			        <div id="groupRule" style="height:200px;overFlow:auto;">
																			             <table >
																			             <%if(list!=null&&list.size()>0){
																			                for(int i=0;i<list.size();i++){
																			                 CompRule compRule=(CompRule)list.get(i);
																	                         String severity="";
																	                         String type="";
																	                         DetailCompRuleDao dao=new DetailCompRuleDao();
																	                          List detailList=dao.findByCondition(" where RULEID="+compRule.getId());
																	                          dao.close();
																	                          DetailCompRule detailCompRule=null;
																	                          String content="";
																	                          String express="";
																	                          String relation="";
																	                          String contain="";
																	                          String expression=""; 

																	                          
																	                       if(compRule.getViolation_severity()==0){
																	                          severity="��ͨ";
																	                         }else if(compRule.getViolation_severity()==1){
																	                          severity="��Ҫ";
																	                         }else if(compRule.getViolation_severity()==2){
																	                          severity="����";
																	                          }
																	                          if(compRule.getSelect_type()==0){
																	                            type="�򵥱�׼";
																	                            if(detailList!=null&&detailList.size()>0){
																	                            detailCompRule=(DetailCompRule)detailList.get(0);
																	                            if(detailCompRule.getIsContain()==0){
																	                            	content="�����ļ� ����������";
																	                            }else if(detailCompRule.getIsContain()==1){
																	                            	content="�����ļ� �������κ���";
																	                            }else if(detailCompRule.getIsContain()==2){
																	                            	content="�����ļ� Ӧ�ð�������";
																	                            }else if(detailCompRule.getIsContain()==3){
																	                            	content="�����ļ� ��Ӧ�ð�������";
																	                            }
																	                            express=detailCompRule.getExpression();
																	                            express=express.replaceAll(";;;;", "\r\n");
																	                            }
																	                          }else if(compRule.getSelect_type()==2){
																	                          
																	                            type="�߼��Զ����׼";
																	                            
																	                          }
                                                                                              if(compRule.getSelect_type()==1||compRule.getSelect_type()==2){
																	                          
																	                          if(compRule.getSelect_type()==1)
																	                            type="�߼���׼";
																	                      
																	                          }
																	                          String check="";
																	                          if(vector.contains(compRule.getId()+"")){ 
																	                          check="checked";
																	                          }
																			              %>
																			                    <tr >
																			                        <td align="center"  width="10%" height=30><INPUT type="checkbox" class=noborder name="checkbox" value="<%=compRule.getId()%>" <%=check %>><%=i+1 %></td>
																			                        <td  align="left"  width="45%" height=30><span id="show<%=i %>"><span  onclick="showDetail('show<%=i %>','group<%=i %>')" >&nbsp;&nbsp;+&nbsp;&nbsp;</span></span><%=compRule.getComprule_name() %></td>
																			                        <td align="left"  width="45%" height=30><%=compRule.getDescription() %></td>
																			                    </tr>
																			                     <tr>
																			                         <td colspan=3>
																			                             <div id="group<%=i %>" style="display:none;background:#8470FF"> 
																			                                 <table>
																			                                        <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;Υ�����ض�</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=severity %></td>
																			                                        </tr>
																			                                        <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;��׼����</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=type %></td>
																			                                        </tr>
																			                                        <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;��������</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=compRule.getRemediation_descr() %></td>
																			                                        </tr>
																			                                        <%if(compRule.getSelect_type()==2){
																			                                         if(detailList!=null&&detailList.size()>0){
																			                                         detailCompRule=(DetailCompRule)detailList.get(0);
																			                                         String beginBlock=detailCompRule.getBeginBlock();
																			                                         String endBlock=detailCompRule.getEndBlock();
																			                                         String extraBlock=detailCompRule.getExtraBlock();
																			                                         int isExtra=detailCompRule.getIsExtraContain();
																			                                         String isExtraContain="";
																			                                         if(isExtra==0){
																			                                         isExtraContain="���ӿ� ��������";
																			                                          }else if(isExtra==1){
																			                                          isExtraContain="���ӿ� ������";
																			                                          }
																			                                         if(beginBlock!=null&&!beginBlock.equals("null")){
																			                                          %>
																			                                            <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;��ʼ��</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=beginBlock %></td>
																			                                        </tr>
																			                                          <%
																			                                          }
																			                                           if(endBlock!=null&&!endBlock.equals("null")){
																			                                          %>
																			                                            <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;��ʼ��</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=beginBlock %></td>
																			                                        </tr>
																			                                          <%
																			                                          }
																			                                           if(isExtra!=-1){
																			                                          %>
																			                                            <tr>
																			                                            <td nowrap align="left" height="24" width="20%"><%=isExtraContain %></td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=extraBlock %></td>
																			                                        </tr>
																			                                          <%
																			                                            }
																			                                           }
																			                                          }
																			                                        if(compRule.getSelect_type()==0){ %>
																			                                        <tr>
																			                                            <td nowrap align="left" height="24" width="20%">&nbsp;&nbsp;&nbsp;&nbsp;�����׼</td>
																			                                            <td width="80%" align="left">��&nbsp;&nbsp;<%=content %></td>
																			                                        </tr>
																			                                        <tr>
																			                                            <td nowrap align="left" height="24" width="100%" colspan=2>
																			                                            <textarea name="content" id="content" rows="8" cols="113"><%=express %></textarea>
																			                                            </td>
																			                                            
																			                                        </tr>
																			                                        <%}else if(compRule.getSelect_type()==1||compRule.getSelect_type()==2){ %>
																			                                        
																			                                           <tr>
																			                                            <td colspan=2 width="100%">
																			                                                <table>
																			                                                <br/>
																			                                                        <tr>
																			                                                             <td width="10%" align="center">��ϵ</td>
																			                                                             <td width="25%" align="center">����</td>
																			                                                             <td width="65%" align="center">ģʽ</td>
																			                                                        </tr>
																			                                                        <%    
																			                                                           if(detailList!=null&&detailList.size()>0){
																	                                                                      for(int j=0;j<detailList.size();j++){
																	                                                                     detailCompRule=(DetailCompRule)detailList.get(j);
																	                                                                   if(detailCompRule.getRelation()==-1){
																	                                                                        relation="";
																	                                                                     }else if(detailCompRule.getRelation()==0){
																	                                                                        relation="��";
																	                                                                     }else if(detailCompRule.getRelation()==1){
																	                                                                        relation="��";
																	                                                                     }
																	                                                                     
																	                                                                 if(detailCompRule.getIsContain()==0){
																	                            	                                       contain="������";
																	                                                                   }else if(detailCompRule.getIsContain()==1){
																	                            	                                       contain="����";
																	                                                                   }
																	                                                                       expression=detailCompRule.getExpression();
																	                                                                   %>
																			                                                        <tr>
																			                                                             <td width="10%" align="center"><%=relation %></td>
																			                                                             <td width="25%" align="center"><%=contain %></td>
																			                                                             <td width="65%" align="center"><%=expression %></td>
																			                                                        </tr>
																			                                                        <%
																			                                                        }
																	                                                             } 
																	                                                             %>
																			                                                </table>
																			                                            </td>
																			                                        </tr> 
																			                                        <%} %>
																			                                 </table>
																			                             </div>
																			                         </td>
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
																				<br><input type="button" value="�� ��" style="width:50" id="process2" onclick="updateGroupRule()">&nbsp;&nbsp;
																					
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