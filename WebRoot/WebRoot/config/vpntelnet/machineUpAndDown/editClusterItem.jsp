<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.application.model.Cluster"%>

<% 

	String rootPath = request.getContextPath(); 
	int id=(Integer)request.getAttribute("id");
	Cluster vo = (Cluster)request.getAttribute("cluster");
    List<Cluster> list=(List<Cluster>)request.getAttribute("clusterList");
%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

<script language="JavaScript" type="text/javascript">



function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    


</script>

<script language="JavaScript" type="text/JavaScript">
function editClusterName(){
  
var	clusterId=document.all.clusterId.value;
 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=editClusterName&id=<%=id%>&clusterId="+clusterId+"&nowtime="+(new Date()),
			success:function(data){
			if(data.value==0){
			alert("修改失败！！！");
			}else{
			 
		    var id = parent.opener.document.getElementById("name<%=id%>");
		    id.innerHTML = data.value;
            alert("修改成功！！！");
        	window.close();
			}
			}
		});
		
}
	function getValue() {
		data1=document.all.alias.value;
		alert(data1);
		var tree_id = parent.opener.document.getElementById("lable");
		tree_id.innerHTML = data1;
         
         window.close();

	}


</script>



</head>
<body id="body" class="body" >


	<!-- 右键菜单结束-->
   <form name="mainForm" method="post">
      <input type=hidden name="id" value="<%=vo.getId()%>">
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
											                	<td class="add-content-title">&nbsp; 修改所属服务器组</td>
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
													                  					<TD nowrap align="right" height="24" width="10%">所属服务器&nbsp;</TD>
													                  					<TD nowrap width="40%">&nbsp;
													                  					<select   name="clusterId" id="clusterId" class="formStyle" >
													                  					 <% 
													                  					if(vo.getId()==0){
													                  					 %>
													                  					    <option value='<%=vo.getId() %>'selected> </option>
													                  					    <%
													                  					   }else{
													                  					   %>
													                  					   
													                  					    <option value='<%=vo.getId() %>'> </option>
													                  					    <%
													                  					   }
													                  					
													                  					if(list!=null&&list.size()>0){
													                  					 
													                  					   for(int i=0;i<list.size();i++){
													                  					    Cluster cluster=list.get(i);
													                  					   
													                  					    if(vo.getId()==cluster.getId()){
													                  					   
													                  					    %>
													                  					    <option value='<%=cluster.getId() %>' selected><%=cluster.getName() %></option>
													                  					    <%
													                  					    }else {
													                  					   
													                  					   %>
													                  					    <option value='<%=cluster.getId() %>' ><%=cluster.getName() %></option>
													                  					    <% 
													                  					    }
													                  					  
													                  					}
													                  					}
													                  					 %>
																					    
																					    </select>
													                  					</TD>
													                				</tr>
																			<tr> 
													  							      
																				<td nowrap width="40%" colspan=2>&nbsp;
																			
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center colspan=2>
																				<br><input type="button" value="保 存" style="width:50" id="process1" onclick="editClusterName()">&nbsp;&nbsp;
																					
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