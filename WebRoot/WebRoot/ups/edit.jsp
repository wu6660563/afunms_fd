<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.security.model.MgeUps"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String rootPath = request.getContextPath();
    MgeUps vo = (MgeUps)request.getAttribute("vo");
    List allbuss = (List)request.getAttribute("allbuss");
    String menuTable = (String)request.getAttribute("menuTable");
    String bid = vo.getBid();
   	if(bid == null)bid="";
   	String id[] = bid.split(",");
   	List bidlist = new ArrayList();
    if(id != null &&id.length>0){
		for(int i=0;i<id.length;i++){
		    bidlist.add(id[i]);  
		}
    }
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow������application/resource/js/addTimeConfig.js�� 
</script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="JavaScript" type="text/javascript">

  function modify()
  {
    var chk1 = checkinput("alias","string","����",20,false);
    var chk2 = checkinput("ipadress","ip","IP��ַ",15,false);
    var chk3 = checkinput("community","string","������",20,false);
    if(chk1&&chk2&&chk3)
    {
    	mainForm.action = "<%=rootPath%>/ups.do?action=update";
        mainForm.submit();
    }
  }
  
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------
  
</script>
<script language="JavaScript" type="text/JavaScript">
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

</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" id="id" name="id" value="<%=vo.getId() %>">
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
											                	<td class="add-content-title">���������豸���� >> �༭</td>
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
																	 <table border="0" id="table1" cellpadding="0" cellspacing="1"
																			width="100%">
																		<TBODY>
																			<tr>				
																			    <TD nowrap id="db_nameTD" align="right" height="24">����&nbsp;</TD>				
																				<TD nowrap id="db_nameInput" width="40%">&nbsp;<input type="text" name="alias" size="20" class="formStyle" value="<%=vo.getAlias() %>"><font color='red'>*</font></TD>				
																				<TD nowrap align="right" height="24" width="10%">IP��ַ&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<input type="text" name="ipadress" size="20" class="formStyle" value="<%=vo.getIpAddress() %>" readonly><font color='red'>*</font></TD>												
																			</tr>
																	        
																	        <tr style="background-color: #ECECEC;">						
																				<TD nowrap align="right" height="24" width="10%">�豸λ��&nbsp;</TD>
																	        	<TD nowrap width="40%">&nbsp;<input type="text" name="location" size="20" class="formStyle" value="<%=vo.getLocation() %>"><font color='red'>*</font></TD>	
																				<TD nowrap align="right" height="24" width="10%">�Ƿ����&nbsp;</TD>				
																				<TD nowrap width="40%">&nbsp;<select name="ismanaged">
																				<%
																					if("1".equals(vo.getIsmanaged()))
																					{
																				 %>
																            									<option value="1" selected>��</option>
																            									<option value="0">��</option>
																            								 
																            	<%
																            		}
																            		else
																            		{
																            	 %>
																            	 								<option value="1">��</option>
																            									<option value="0" selected>��</option>
																            	 <%
																            	 	}
																            	  %>
																            	  </select><font color='red'>*</font>
																            	</TD>
																	        </tr>
																	        <tr>
																            	<TD nowrap align="right" height="24">����ҵ��&nbsp;</TD>
																            	<TD nowrap height="1">
																            	<% 
																					String bussName = "";
																					//System.out.println(allbuss.size());
																					if(allbuss.size()>0){
																						for(int i=0;i<allbuss.size();i++){
																							Business buss = (Business)allbuss.get(i);
																							if(bidlist.contains(buss.getId()+"")){
																								bussName = bussName + ',' + buss.getName();
																							}
																		 				}
																					}
																					//System.out.println(bussName);
																				%>   
																				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bussName%>">&nbsp;&nbsp;<input type=button value="��������ҵ��" onclick='setBid("bidtext" , "bid");'>
																				<input type="hidden" id="bid" name="bid" value="<%=vo.getBid()%>">
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">������&nbsp;</TD>
																				<TD nowrap width="40%">&nbsp;<input type="text" name="community" size="20" class="formStyle" value="<%=vo.getCommunity() %>"><font color='red'>*</font></TD>
																	        </tr>
																	        <tr>
							 													<td nowrap align="right" width="10%" style="height:35px;">��Ϣ�ɼ�ʱ��&nbsp;</td>		
							 													<td nowrap  colspan="3">
							        												<div id="formDiv" style="">         
						                												<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
					                        												<tr>
					                            												<td align="left">  
						                            												<br>
					                                												<table id="timeConfigTable" style="width:60%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
																                                        <c:forEach items="${timeGratherConfigList}" var="tg" varStatus="tgindex">
																                                        	<script type="text/javascript">
																                                        		$(document).ready(function(){
																                                        			$("#addTimeConfigRow").click();
																                                        			selectedAll(${tg.startHour},${tg.startMin},${tg.endHour},${tg.endMin},${tgindex.index});
																                                        		});
																                                        	</script>
																                                        </c:forEach>
																                                        <tr>
																                                            <td colspan="7" height="50" align="center"> 
																                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
																                                            </td>
																                                        </tr>
																	                                </table>
					                            												</td>
					                       	 												</tr>
						                												</table>
						            												</div> 
																				</td>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="����" style="width:50" class="formStylebutton" onclick="modify();">&nbsp;&nbsp;
																					<input type=reset class="formStylebutton" style="width:50" value="����" onclick="javascript:history.back(1)">
																				</TD>	
																			</tr>
																		</TBODY>
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