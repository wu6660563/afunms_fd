<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.Keeper"%>
<%@page import="java.sql.*"%>
<%
    Keeper keeper1 = new Keeper();
    Keeper keeper2 = new Keeper();

    ResultSet rs1 = keeper1.executeQuery("select * from topo_interface");
    int i = 0,j=0;
    while(rs1.next())
       System.out.println(i++);
    
    ResultSet rs2 = keeper2.executeQuery("select * from topo_interface_data");
    while(rs2.next())
        System.out.println(j++);
    
    
%>