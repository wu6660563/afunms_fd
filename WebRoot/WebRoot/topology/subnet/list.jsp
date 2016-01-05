<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.Subnet"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int perRowNum = 8; //每行的节点个数
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<SCRIPT>
<!--
function getDiv(left,top,layer)
{
	divid=document.getElementById(layer);
	divid.style.left=left-10;
	divid.style.top=top-20;
	divid.style.visibility="visible";
	return false;
}
function HideDiv(pName){
    var obj = event.toElement;
    while( obj!=null && obj.id!=pName ){
        obj = obj.parentElement;
   }
   if( obj==null ){ document.all[pName].style.visibility = 'hidden'; }
}
function shonone()
{
	return false;
}
function click1(layer)
{  var tempx = event.clientX + document.body.scrollLeft;
   var tempy = event.clientY + document.body.scrollTop;
    if(event.button == 2)
    {
       document.oncontextmenu = shonone;
       getDiv(tempx,tempy,layer);
       return false;
    }
}

function doDelete(id)
{
    if (window.confirm("删除子网,同时将删除子网内的所有设备,确定要删除吗?"))
    {
         mainForm.action = "subnet.do?action=delete&id=" + id;
         mainForm.submit();
    }   
}

function unmanaged(id)
{
    if (window.confirm("确定要解除管理吗?"))
    {
         mainForm.action = "subnet.do?action=unmanaged&id=" + id;
         mainForm.submit();
    }   
}

function managed(id)
{
    if (window.confirm("确定要恢复管理吗?"))
    {
         mainForm.action = "subnet.do?action=managed&id=" + id;
         mainForm.submit();
    }   
}

//-->
</SCRIPT>
</head>
<BODY>
<form method="post" name="mainForm">
<table cellpadding="0" cellspacing="0" width=90% class="tborder" align='center'>
 <tr height="20"><td colspan='<%=perRowNum*2%>' background="<%=rootPath%>/resource/image/td-bg.jpg" align='center'> 
 <font color='white'><b>子网</b>&nbsp;&nbsp;(总数:<%=list.size()%>)</font></td></tr>
<%
   for(int i=0;i<list.size();i++)
   {
      Subnet subnet = (Subnet)list.get(i);
      if(i%perRowNum==0) out.print("<tr>");
%>
      <td>&nbsp;&nbsp;&nbsp;</td>
      <td width='12%' height='100' align='center'>
      	<table border='0' width='100%' cellpadding="0" cellspacing="0" align="center">
      	  <tr>
      		<td width='84' height="25" rowspan='2' valign="top" align="left">
      		<a href='<%=rootPath%>/subnet.do?action=list_device&id=<%=subnet.getId()%>&ip=<%=subnet.getNetAddress()%>'>
      		<img height='48' width='48' border=0 src='<%=rootPath%>/resource/image/topo/subnet.png' alt='网络地址:<%=subnet.getNetAddress()%>	<%out.print("\n");%>子网掩码:<%=subnet.getNetMask()%>'>
      		</a><br><%=subnet.getNetAddress()%></td>
      	</tr>
      	</table>
   <!--=============================div begin====================================-->   	
   <div id="Layer<%=i%>" style="cursor:hand;visibility:hidden;position:absolute; left:119px; top:100px; width:90px; height:100px; z-index:3; background: white" onmouseout="HideDiv('Layer<%=i%>')">
    <table border="0" cellpadding="0" cellspacing="0" class="tborder" height="100%" width="100%">
      <tr onmouseover="this.bgColor='#C0C0C0'" onmouseout="this.bgColor='white'" height="10">
	    <td width="20%"><img src="<%=rootPath%>/resource/image/open.gif"></td>
	     <a href="subnet.do?action=list_device&id=<%=subnet.getId()%>">
	     <td width="80%" align="center">网段设备</td></a>
      </tr>
      <tr onmouseover="this.bgColor='#C0C0C0'" onmouseout="this.bgColor='white'" height="10">
	    <td><img src="<%=rootPath%>/resource/image/unmanage.gif"></td>	
<%  
    if(subnet.isManaged()==true)
	   out.print("<a href='#' onclick='unmanaged(" + subnet.getId()+ ")'><td align='center'>解除管理</td></a>");
	else
       out.print("<a href='#' onclick='managed(" + subnet.getId()+ ")'><td align='center'>恢复管理</td></a>");
%>       
      </tr>
      <tr onmouseover="this.bgColor='#C0C0C0'" onmouseout="this.bgColor='white'" height="10">
	    <td><img src="<%=rootPath%>/resource/image/del.gif"></td>
	    <a href="#" onclick="doDelete(<%=subnet.getId()%>)"><td align="center">删除网段</td></a>
	  </tr>
    </table>      	
   </div>
   <!--=============================div end====================================-->       	
      </td>
<%       
      if(i%perRowNum==perRowNum-1) out.print("</tr>");         
   }//end_for
%>
</table>
</form>
</BODY>
</HTML>
