<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.inform.dao.NewDataDao"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%
   NewDataDao dao = new NewDataDao();
   double[][] dataSet = dao.multiStat("2007-01-08",10,"051002",true,3);
   String[] rowKeys = dao.getRowKeys();
   String[] colKeys = dao.getColKeys();
 
   for(int i=0;i<rowKeys.length;i++)
   {
      System.out.println(rowKeys[i] + "-----------");
      for(int j=0;j<colKeys.length;j++)
         System.out.println(colKeys[j] + "=" + dataSet[i][j] + ",");   
   } 
        
   String[] colKeys2 = null;
   double[][] dataSet2 = null;
   if(colKeys.length>5)  //只取5个
   {
       colKeys2 = new String[5];    
       dataSet2 = new double[rowKeys.length][5];
       for(int j=0;j<5;j++)
           colKeys2[j] = colKeys[j];
           
       for(int i=0;i<rowKeys.length;i++)
          for(int j=0;j<5;j++)
             dataSet2[i][j] = dataSet[i][j];
   }
     
   String key = ChartCreator.createLineChart(dataSet2,rowKeys,colKeys2,"时间","可用内存(M)","",300,200);
   String rootPath = request.getContextPath(); 
%>
<img src="<%=rootPath%>/artist?series_key=<%=key%>">