<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.polling.node.IfEntity"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%  
   String rootPath = request.getContextPath();  
   
   List<Portconfig> it1 = (List<Portconfig>)request.getAttribute("start_if");  
   List<Portconfig> it2 = (List<Portconfig>)request.getAttribute("end_if");  
   String alias_start = (String)request.getAttribute("alias_start");  
   String ipAddress_start = (String)request.getAttribute("ipAddress_start");     
   String alias_end = (String)request.getAttribute("alias_end");  
   String ipAddress_end = (String)request.getAttribute("ipAddress_end");       
   String startId = (String)request.getAttribute("start_id");
   String endId = (String)request.getAttribute("end_id");
   String link_name = (String)request.getAttribute("link_name"); 
   String fileName = (String)request.getAttribute("xml"); 
%>
<html>
<head>
<base target="_self">

<meta http-equiv="Pragma" content="no-cache">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
var linkName='<%=link_name%>';
window.onload=function()
{
var linkNamrArray=linkName.split('/');
var part1=linkNamrArray[0];
var part2=linkNamrArray[1];
part1+='_0';
part2+='_0';
linkName=part1+'/'+part2;
document.getElementById("link_name").value=linkName;
}
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
function portChange1(obj)
{
 var nowLinkName=document.getElementById("link_name").value;
 if(nowLinkName==linkName)
 {
  var linkNameArray=nowLinkName.split('/');
  var part1=linkNameArray[0];
  var part2=linkNameArray[1];
  part1=part1.substr(0,part1.indexOf('_'));
  part1+='_'+obj.value;
  document.getElementById("link_name").value=part1+'/'+part2;
  linkName=part1+'/'+part2;
  changflag1=true;
 }
}
function portChange2(obj)
{
 var nowLinkName=document.getElementById("link_name").value;
 if(nowLinkName==linkName)
 {
  var linkNameArray=nowLinkName.split('/');
  var part1=linkNameArray[0];
  var part2=linkNameArray[1];
  part2=part2.substr(0,part1.indexOf('_'));
  part2+='_'+obj.value;
  document.getElementById("link_name").value=part1+'/'+part2;
  linkName=part1+'/'+part2;
  changflag2=true;
 }
}  

function save()
{
    var args = window.dialogArguments;   
    var direction1 = document.getElementById("direction1").value;
    var linkName = document.getElementById("link_name").value;
    //var maxSpeed = document.getElementById("max_speed").value;
    //var maxPer = document.getElementById("max_per").value;
    var maxSpeed = 100;
    var maxPer = 100;
    var xml = "<%=fileName%>";
    var start_id = "<%=startId%>";
    var start_index = document.getElementById("start_index").value;
    var end_id = "<%=endId%>";
    var end_index = document.getElementById("end_index").value;
    var cable_type = document.getElementById("cable_type").value;
    var cable_capacity = document.getElementById("cable_capacity").value;
    args.parent.mainFrame.addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index, cable_type, cable_capacity);
    window.close();    
}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type="hidden" name='xml_name' value="<%=fileName%>"/>
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=500px>
	<tr>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="500px" border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp;创建实体链路</td>
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
			   &nbsp; <input type="text" name="link_name" size="30" class="formStyle" value="<%=link_name%>">
			</TD>
			</tr>
			<!-- 
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">链路类型&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <input type="text" name="link_type" maxlength="50%" size="20" class="formStyle">
			</TD>
			</tr> 
			
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">链路流量阀值&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <input type="text" name="max_speed" maxlength="50%" size="20" class="formStyle">(KB/秒)
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">带宽利用率阀值&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <input type="text" name="max_per" maxlength="50%" size="20" class="formStyle">(%)
			</TD>
			</tr>
            -->
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备类型&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <select size=1 name='direction1' style='width:200px;' id="direction1" onchange="changeDirection(1)">
			       <option value="1" selected>上行设备</option>
				   <option value="2">下行设备</option>
			   </select>
			</TD>
			</tr>
			<tr >						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备名称&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <%=alias_start%></TD>	
            </tr>	
            <tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备IP&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <%=ipAddress_start%>
			</TD>
			<tr>
            <tr>				
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备端口索引&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
            <select name='start_index' style='width:200px;' onchange="portChange1(this)">
<%
  for(int i = 0; i < it1.size(); i++)
  {
     Portconfig portconfig = it1.get(i);
%> 			
		<option value='<%=portconfig.getPortindex()%>' title="<%=portconfig.getPortindex()%>(<%=portconfig.getName()%>)"><%=portconfig.getPortindex()%>(<%=portconfig.getName()%>)</option>
<%}%></select>			
			</TD>															
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备类型&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <select size=1 name='direction2' style='width:200px;' id="direction2" onchange="changeDirection(2)">
			       <option value="1">上行设备</option>
				   <option value="2" selected>下行设备</option>
			   </select>
			</TD>
			</tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备名称&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <%=alias_end%></TD>	
            </tr>
            <tr>
            <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备IP&nbsp;</TD>				
			<TD nowrap width="80%">
			   &nbsp; <%=ipAddress_end%>
			</TD>
			</tr>
            <tr>					
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">设备端口索引&nbsp;</TD>				
			<TD nowrap width="100%">&nbsp;
            <select name='end_index' style='width:200px;' onchange="portChange2(this)">
<%
 for(int i = 0; i < it2.size(); i++)
  {
     Portconfig portconfig = it2.get(i);
%> 			
		<option value='<%=portconfig.getPortindex()%>' title="<%=portconfig.getPortindex()%>(<%=portconfig.getName()%>)"><%=portconfig.getPortindex()%>(<%=portconfig.getName()%>)</option>
<%}%></select>	
			

</TD>															
			</tr>
            <tr><td style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">线缆类型&nbsp;</td>
            <td>&nbsp;&nbsp;<select size=1 name='cable_type' id="cable_type" style='width:200px;'>
              <option value="光缆">光缆</option>
              <option value="电缆">电缆</option>
              </select>
            </td></tr>
            <tr><td  style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">线缆容量&nbsp;</td>
            <td>&nbsp;&nbsp;<select size=1 name='cable_capacity' id="cable_capacity" style='width:200px;'>
              <option value="100M">100M</option>
              <option value="1000M">1000M</option>
              </select>
            </td></tr>
			<tr><td>
			  <input type="hidden" name='start_id' value="<%=startId%>"/>
			  <input type="hidden" name='end_id' value="<%=endId%>"/>
			  <input type="hidden" name='xml' value="<%=fileName%>"/>
			</td></tr>
			<tr>
				<TD nowrap colspan="4" align=center>
					<br>
					<input type="button" value="保存" style="width:50" class="formStylebutton" onclick="save()">
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