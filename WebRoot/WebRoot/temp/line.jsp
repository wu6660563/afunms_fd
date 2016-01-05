<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%
     double[][] data = new double[1][5];
     data[0][0] = 2.4;
     data[0][1] = 1.4;
     data[0][2] = 3.8;
     data[0][3] = 4.9;
     data[0][4] = 5.7;
     
     String[] rowKeys = new String[]{"JVM¿ÉÓÃÄÚ´æ"};
     String[] colKeys = new String[]{"1:00","2:00","3:00","4:00","5:00"};

     String key = ChartCreator.createLineChart(data,rowKeys,colKeys,"x","y","title",300,200);
     String rootPath = request.getContextPath(); 
%>
<img src="<%=rootPath%>/artist?series_key=<%=key%>">