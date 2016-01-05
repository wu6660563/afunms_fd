<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SnmpService"%>
<%
        String[] inputOids = new String[]{"1.3.6.1.4.1.705.1.7.2"};
    	
    	String[][] inputPhase = null;   
    	SnmpService snmp = new SnmpService(); 
    	try
    	{
    		inputPhase = snmp.getTableData("10.110.1.126","public",inputOids);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}   

        System.out.println("row=" + inputPhase.length + ",col=" + inputPhase[0].length);
        for(int i=0;i<3;i++)
        {
            System.out.println(inputPhase[i][0] + "," + inputPhase[i+3][0] + ","
              + inputPhase[i+6][0] + "," + inputPhase[i+12][0]);
        }
        
        String level = snmp.getMibValue("10.110.1.126","public","1.3.6.1.4.1.705.1.5.15.0");
        System.out.println("level=" + level);
%>