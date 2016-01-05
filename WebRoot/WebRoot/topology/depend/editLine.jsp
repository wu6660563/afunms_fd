<%@page language="java" contentType="text/html;charset=GB2312"%>
<%  
   String rootPath = request.getContextPath();  
      
   String father_id = (String)request.getAttribute("father_id");  
   String child_id = (String)request.getAttribute("child_id");     
   String xml = (String)request.getAttribute("xml");
   String line_name = (String)request.getAttribute("line_name");
   int id = ((Integer)request.getAttribute("id")).intValue();
   int width = ((Integer)request.getAttribute("line_width")).intValue();
   String line_id = (String)request.getAttribute("line_id");
   String father_x_y = (String)request.getAttribute("father_x_y"); 
   String child_x_y = (String)request.getAttribute("child_x_y"); 
   String f_alias = (String)request.getAttribute("f_alias"); 
   String c_alias = (String)request.getAttribute("c_alias"); 
%>
<html>
<head>
<base target="_self">
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Pragma" content="no-cache">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
function changeDirection(temp)
{
 var direction1=document.getElementById("direction1");
 var direction2=document.getElementById("direction2");
 var direction1value=direction1.value;
 var direction2value=direction2.value;
 if(temp==1)
 {
  if(direction1value==1)
  {
   direction2.value="2";
  }else
  {
   direction2.value="1";
  }
 }else
 {
  if(direction2value==1)
  {
    direction1.value="2";
  }else
  {
    direction1.value="1";
  }
 }
 
}
function toEdit()
{
    var args = window.dialogArguments;   
    mainForm.action = "<%=rootPath%>/link.do?action=editDemoLink";
    mainForm.submit();
    window.close(); 
    args.location.reload(); 
}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=500px>
	<tr>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="500px" border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp; 编辑示意链路</td>
				</tr>
			<tr>
				<td height=250 bgcolor="#FFFFFF" valign="top">
				 <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%" align="center">
					<TBODY>
						<tr>
							<td nowrap colspan="2" height="3" bgcolor="#8EADD5"></td>
						</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">链路名称&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <input type="text" name="line_name" size="30" class="formStyle" value="<%=line_name%>">
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点类型&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <select size=1 name='direction1' style='width:200px;' id="direction1" onchange="changeDirection(1)">
			       <option value="1">父节点</option>
				   <option value="2" selected>子节点</option>
			   </select>
			</TD>
			</tr>
			<tr >						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点名称&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <%=c_alias%></TD>	
            </tr>	
            <tr >						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点坐标&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <input type="text" name="child_x_y" size="20" class="formStyle" value="<%=child_x_y%>" disabled=true></TD>	
            </tr>
            <tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">描述&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <%=child_id%>
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点类型&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <select size=1 name='direction2' style='width:200px;' id="direction2" onchange="changeDirection(2)">
			       <option value="1" selected>父节点</option>
				   <option value="2">子节点</option>
			   </select>
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点名称&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <%=f_alias%></TD>	
            </tr>
            <tr >						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">节点坐标&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <input type="text" name="father_x_y" size="20" class="formStyle" value="<%=father_x_y%>" disabled=true></TD>	
            </tr>
            <tr>
            <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">描述&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <%=father_id%>
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">链路宽度&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <% 
			     String selected1="";
			     String selected2="";
			     String selected3="";
			     if(width==1){
			         selected1="selected";
			     } else if(width==2){
			         selected2="selected";
			     } else if(width==3){
			         selected3="selected";
			     }
			   
			   %>
			   <select size=1 name='line_width' style='width:50px;' onchange="">
			       <option value="1" <%=selected1%>>1</option>
				   <option value="2" <%=selected2%>>2</option>
				   <option value="3" <%=selected3%>>3</option>
			   </select>
			</TD>
			</tr>
			<tr>
			<td>
			  <input type="hidden" name='f_alias' value="<%=f_alias%>"/>
			  <input type="hidden" name='c_alias' value="<%=c_alias%>"/>
			  <input type="hidden" name='father_id' value="<%=father_id%>"/>
			  <input type="hidden" name='child_id' value="<%=child_id%>"/>
			  <input type="hidden" name='line_id' value="<%=line_id%>"/>
			  <input type="hidden" name='xml' value="<%=xml%>"/>
			  <input type="hidden" name='id' value="<%=id%>"/>
			</td></tr>
			<tr>
				<TD nowrap colspan="4" align=center>
					<br>
					<input type="button" value="修改" style="width:50" class="formStylebutton" onclick="toEdit()">
					<input type="button" value="关闭" style="width:50" class="formStylebutton" onclick="window.close();">
				</TD>	
			</tr>
			</TBODY>
				</TABLE>				
				</td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>