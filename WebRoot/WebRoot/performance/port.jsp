<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="java.util.*"%>
<%
  String rootPath = request.getContextPath();
  List list = PollingEngine.getInstance().getNodeList();  
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
</head>
<BODY leftmargin="0" topmargin="0">
<br>
<form method="post" name="mainForm">
<table cellpadding="0" cellspacing="0" width=80% class="tborder" align='center'>
 <tr height="20"><td colspan='3' background="<%=rootPath%>/resource/image/td-bg.jpg" align='center'>
 <font color='white'><b>网络设备端口列表</b></font></td></tr>
</table> 
<table cellpadding="0" cellspacing="0" width=80% class="porttbl" align='center'>
<% 
   Iterator it = null;
   int total = 0;
   for(int i=0;i<list.size();i++)
   {
      Node node = (Node)list.get(i); 
      if(node.getCategory()>3) continue;
      
      Host host = (Host)node;
      it = host.getInterfaceHash().values().iterator();
      total = 0;
      while(it.hasNext())
      {
          it.next();          
          total++;
      }      
      if(total==0) continue;
      
      String imgFile = null;      
      if(host.getCategory()==1) imgFile = "router.png";
      else if(host.getCategory()==2) imgFile = "switch_router.gif";
      else if(host.getCategory()==3) imgFile = "switch.png";
             
	  out.print("<tr><td valign='top'><img height='15' src='");			      
      out.print(rootPath);
      out.print("/resource/");
      out.print(NodeHelper.getStatusImage(host.getStatus()));
      out.print("'>");        
      out.print("<td>&nbsp;<img border=0 src='");
      out.print(rootPath);
      out.print("/resource/image/device/");
      out.print(imgFile); 
      out.print("'><br>");      
      out.print(host.getAlias());
      out.print("<br>(");
      out.print(host.getIpAddress());
      out.print(")");
      out.print("</td>");
      
      it = host.getInterfaceHash().values().iterator();
      total = 0;      
      while(it.hasNext())
      {          
          IfEntity ifObj = (IfEntity)it.next();
          if(ifObj.getType() != 6 && ifObj.getType() != 117 )continue;
          total++;
          if(ifObj.getOperStatus()==1)
          {
        	  out.print("<td align='left' width='80'>");
        	  out.print("<a href='" + rootPath + "/port.do?action=port_jsp&node_id=" + host.getId());
        	  out.print("&index=" + ifObj.getIndex() + "'>");
              out.print("<img border=0 src='");        
              out.print(rootPath);
              out.print("/resource/image/topo/"); 
              out.print("ifOrPortClear.gif'></a><br>"); 
              out.print(ifObj.getDescr());
              out.print("</td>"); 
          }
          else
          {
        	  out.print("<td align='left' width='80'>");        
              out.print("<img border=0 src='");        
              out.print(rootPath);
              out.print("/resource/image/topo/"); 
        	  out.print("ifOrPortTrouble.gif'><br>"); 
        	  out.print(ifObj.getDescr());
              out.print("</td>");
          }  
          
          if(total>=5)
          {
             out.print("<td><a href='more_port.jsp?id=");
             out.print(host.getId());
             out.print("'>更多...</a></td>");
             
             out.print("<td><a href='panel_port.jsp?id=");
             out.print(host.getId());
             out.print("'>端口面板</a></td>");             
             break;                    
          }
      } 
      for(int j=total;j<5;j++)
      {
         out.print("<td>&nbsp;</td>"); 
      }
      out.print("</tr>");            
   }
%>
</table>
</form>
</BODY>
</HTML>