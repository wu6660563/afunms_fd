<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.dhcnms.common.util.DBManager"%>
<%@page import="com.dhcnms.common.util.SnmpUtil"%>
<%@page import="java.sql.ResultSet"%>
<%
    DBManager conn = new DBManager();
    ResultSet rs = null;
    
    out.println("<table width='500' border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>");
    try
    {
       rs = conn.executeQuery("select * from nms_topo_node order by id");
       while(rs.next())
       {
          int services = SnmpUtil.getInstance().getSysServices(rs.getString("ip_address"),rs.getString("community"));
          out.println("<tr><td>" + rs.getString("ip_address") + "</td>");                      
          out.println("<td>" + rs.getString("category") + "</td>");            
          out.println("<td>" +  services + "</td></tr>");                      
       }
    } 
    catch(Exception e)
    {}
    out.println("</table>");            
%>