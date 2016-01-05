<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.initialize.ResourceCenter"%>
<%@page import="com.afunms.sysset.model.Service"%>
<%@page import="com.afunms.monitor.item.ServiceItem"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();   
  int flag = 1;
  String tmp = request.getParameter("flag");
  if(tmp!=null) flag = Integer.parseInt(tmp);
  
  List hostList = PollingEngine.getInstance().getNodeList();
%>
<html>
<head>
<title></title>
<script language="JavaScript" type="text/javascript">
  function doChange()
  {
     if(mainForm.view_type.value==1)
        mainForm.action = "index.jsp";
     else
        mainForm.action = "<%=rootPath%>/server.do?action=list";
     mainForm.submit();  
  }
  
  function exchange()
  {
     if(<%=flag%>==1)
        mainForm.action = "services.jsp?flag=0";
     else
        mainForm.action = "services.jsp?flag=1";
     mainForm.submit();
  }
  
  function refresh()
  {
     mainForm.action = "services.jsp?flag=<%=flag%>";
     mainForm.submit();
  }
</script>  
<link rel="stylesheet" href="<%=rootPath%>/resource/css/top.css" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
</head>
<body leftmargin="0" topmargin="0">
<form method="post" name="mainForm">
<table width="100%">
	<tr height="20">
	<td>
		<table width="100%" height="5%" align='left'>
		 <tr>
		   <td width='75%'>&nbsp;&nbsp;&nbsp;</td>
		   <td width='25%'>&nbsp;&nbsp;
		   <input type="button" class="button" value="切换" onclick="exchange()">
		   <input type="button" class="button" value="刷新" onclick="refresh()">
		   </td>
		 </tr>
		</table> 
	</td>
	</tr>
	<tr>
	<td>
		<table width="80%" height="95%" border=1 align='center' cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
		  <tr>
		  <td background="<%=rootPath%>/resource/image/td-bg.jpg">&nbsp;</td>
		  <td background="<%=rootPath%>/resource/image/td-bg.jpg">&nbsp;&nbsp;<b><%if(flag==1) out.print("机器名"); else out.print("IP地址");%></b></td>
<%
		  List servicesList = ResourceCenter.getInstance().getServiceList();     
		  for(int i=0;i<servicesList.size();i++)
		  {
		       Service vo = (Service)servicesList.get(i);
		       out.print("<td background='");
		       out.print(rootPath);
		       out.print("/resource/image/td-bg.jpg'><b>&nbsp;");
		       out.print(vo.getService());
		       out.print("&nbsp;</b></td>");
		  }
		  out.print("&nbsp;</tr>\n");
		
		  int k = 0;
		  for(int i=0;i<hostList.size();i++)
		  {
		      if(((Node)hostList.get(i)).getCategory()!=4) continue;
		      Host host = (Host)hostList.get(i);		      		  
		      k++;
		      if(k%2==0)
		    	  out.print("<tr class=\"dark\">");
		      else
		    	  out.print("<tr class=\"light\">");  
		      
		      if(host.getSysOid().startsWith("1.3.6.1.4.1.311.")) //windows      
		      {
		         out.print("<td><IMG height='20' width='20' src='");
		         out.print(rootPath);
		         out.print("/resource/image/topo/windowslist.png'></td>");
		      }
		      else if(host.getSysOid().startsWith("1.3.6.1.4.1.42.")) //solaris     
		      {
		         out.print("<td><IMG height='20' width='20' src='");
		         out.print(rootPath);
		         out.print("/resource/image/topo/solarislist.png'></td>");
		      }
		      else if(host.getSysOid().equals("1.3.6.1.4.1.2021.250.10")||
			          host.getSysOid().equals("1.3.6.1.4.1.8072.3.2.10")) //linux     
		      {
		         out.print("<td><IMG height='20' width='20' src='");
		         out.print(rootPath);
		         out.print("/resource/image/topo/linuxlist.png'></td>");
		      }
		      else
		      {
		         out.print("<td><IMG height='20' width='20' src='");
		         out.print(rootPath);
		         out.print("/resource/image/topo/other_server.png'></td>");
		      }     		                    
		      out.print("<td width='200'><IMG height='15' width='15' src='");			      
              out.print(rootPath);
              out.print("/resource/");
		      out.print(NodeHelper.getStatusImage(host.getStatus()));
		      out.print("'>");
		      if(flag == 1)
		         out.print(host.getAlias());
		      else  
		         out.print(host.getIpAddress());
		     out.print("</td>");
		     		      
             ServiceItem item = (ServiceItem)host.getItemByMoid(MoidConstants.TEST_SERVICE);
		     for(int j=0;j<servicesList.size();j++)
		     {
		         if(item.getServicesStatus()[j]==1)
		         {
		             out.print("<td width=50 align='center'><IMG height='12' width='12' src='");
		             out.print(rootPath);
		             out.print("/resource/image/topo/service_up.png'></td>");
		         }
		         else
		           out.print("<td width=50>&nbsp;&nbsp;</td>");                             
		     }
		     out.print("</tr>\n");
		  }   
		%>      
		</table>
	</td>
	</tr>
</table>
</form>
</body>
</html>