<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.cn.dhcc.ens.util.SnmpUtility"%>
<%@page import="java.util.*"%>
<%
   String[][] tmpPortStrs = SnmpUtility.snmpOperator.
                         getTableDetails("192.168.5.253","161",
        		                 "1.3.6.1.4.1.9.9.68.1.2.2.1", 5,
        		                (byte)0x00, "public",1000);

   List communtiyList = new ArrayList();
   for (int i = 1; i < tmpPortStrs.length; i++)
   {	            
	   String tmpCommun = "public@"+tmpPortStrs[i][1];
       if(!communtiyList.contains(tmpCommun))
    	   communtiyList.add(tmpCommun);
   }

   for (int i = 1; i < communtiyList.size(); i++)
   {
       String tmpCommun = (String)communtiyList.get(i);
       tmpPortStrs = SnmpUtility.snmpOperator.
           				getTableDetails("192.168.5.253", "161",
                                           "1.3.6.1.2.1.17.1.4.1", 5,
                                           (byte)0x00, tmpCommun,2000);
       for(int j = 1;  j < tmpPortStrs.length; j++)
    	 for(int k = 1;  k < tmpPortStrs[0].length; k++)  
          System.out.println(tmpPortStrs[j][k]);     
   }
%>