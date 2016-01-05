<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.polling.node.*"%>
<%
  String rootPath = request.getContextPath();  
  List list = PollingEngine.getInstance().getNodeList();
  int num = 1;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background="<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top" align='center'>				
<br>
	<table cellSpacing="1" cellPadding="0" width="90%" border="0">
		<tr>
			<td colspan="2">
<!-- ----------------------------- -->			
<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
<tr>
    <th width='5%'>序号</th>				
    <th width='15%'>设备IP</th>
    <th width='15%'>设备名称</th>
    <th width='40%'>告警信息</th>
</tr>
<%
	for(int i=0;i<list.size();i++)
	{
	   Node node = (Node)list.get(i);	   
       if(node.getCategory()>3) continue;
       
       Host host = (Host)node;
       if(host.getStatus()==3) 
       {
%>
			<tr class="microsoftLook0">
				<td class="microsoftLook0"><font color='blue'><%=num++%></font></td>
		    	<a href="<%=rootPath%>/topology/network/detail.jsp?id=<%=host.getId()%>">
		    	<td class="microsoftLook0" style="cursor:hand"><%=host.getIpAddress() %></td></a>
		    	<td class="microsoftLook0"><%=host.getAlias()%></td>
		    	<td class="microsoftLook0">Ping不通！</td>
		    </tr>
<%
       }
       else if(host.isAlarm())
       {
 %>
 	   			<tr class="microsoftLook0">
 	   				<td class="microsoftLook0"><font color='blue'><%=num++%></font></td>
 	   		    	<a href="<%=rootPath%>/topology/network/detail.jsp?id=<%=host.getId()%>">
 	   		    	<td class="microsoftLook0" style="cursor:hand"><%=host.getIpAddress() %></td></a>
 	   		    	<td class="microsoftLook0"><%=host.getAlias()%></td>
 	   		    	<td class="microsoftLook0"><%=host.getLastAlarm() %></td>
 	   		    </tr>
<%
 	   }else
       {
           Iterator it = host.getInterfaceHash().values().iterator();
           while(it.hasNext())
           {
               IfEntity ifObj = (IfEntity)it.next();
               if(ifObj.getPort().equals("")) continue;
               
               if(ifObj.getDiscards()>2)
               {
%>
	        <tr class="microsoftLook0">
				<td class="microsoftLook0"><font color='blue'><%=num++%></font></td>
		    	<a href="<%=rootPath%>/port.do?action=port_error_jsp&menu=0502&node_id=<%=host.getId()%>&index=<%=ifObj.getIndex()%>&day=<%=SysUtil.getCurrentDate()%>">
		    	<td class="microsoftLook0" style="cursor:hand"><%=host.getIpAddress() %></td></a>
		    	<td class="microsoftLook0"><%=host.getAlias()%></td>
		    	<td class="microsoftLook0">接口:<%=ifObj.getPort()%> 丢包率大于3%</td>
	        </tr>
<%
               }
               if(ifObj.getErrors()>2) 
               {
%>
	        <tr class="microsoftLook0">
				<td class="microsoftLook0"><font color='blue'><%=num++%></font></td>
		    	<a href="<%=rootPath%>/port.do?action=port_error_jsp&menu=0502&node_id=<%=host.getId()%>&index=<%=ifObj.getIndex()%>&day=<%=SysUtil.getCurrentDate()%>">
		    	<td class="microsoftLook0" style="cursor:hand"><%=host.getIpAddress() %></td></a>
		    	<td class="microsoftLook0"><%=host.getAlias()%></td>
		    	<td class="microsoftLook0">接口:<%=ifObj.getPort()%>错误率大于3%</td>
		    </tr>
<%
               }
           }//end of while
       }//end of else
	}//end of for
%> 				
	</table>
<!-- ------------------------------------ -->
			</td>
		</tr>	
	</table>
</td>
		<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13></td>
	</tr>
	<tr>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
		<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
	</tr>
</table>
</form>		
</BODY>
</HTML>
