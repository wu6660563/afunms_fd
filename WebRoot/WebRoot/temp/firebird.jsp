<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.sql.*"%>
<%
    Class.forName("org.firebirdsql.jdbc.FBDriver");
    Connection conn = DriverManager.getConnection("jdbc:firebirdsql:10.110.19.177/3050:/Human.gdb", "sysdba", "masterkey");
       
    
    if(conn==null)
    {
        out.print("can not connect Firebird db...555");
        return; 
    }
    else    
       out.print("Firebird db is useable...");
       
//    select a.*,b.xing_ming from nmj_log a left join human b
//    on a.person_key=b.person_key
//    order by a.logkey desc   

	Statement stmt = null;
	ResultSet rs = null;    
    try
    {  
        stmt = conn.createStatement();
        rs = stmt.executeQuery("select * from nmj_log order by logkey");
        while(rs.next())
           System.out.println(rs.getString("io_date")+" "+ rs.getString("io_time"));
    }
    catch(Exception e)
    {
        e.printStackTrace();    
    }
    finally
    {
        conn.close();
    }      
%>