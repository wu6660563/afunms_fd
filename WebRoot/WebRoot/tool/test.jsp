<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.dhcnms.polling.*"%>
<%@page import="java.util.*"%>
<%
   List list = PollingEngine.getInstance().getHostList();
   for(int i=0;i<list.size();i++)
   {
       Host host = (Host)list.get(i);
       if(host.getCategory()>3) continue;
       
       if(host.getStatus()==3) 
          out.println("设备" + host.getIpAddress() + " Ping不通！<br>");
       else
       {
           Iterator it = host.getInterfaceHash().values().iterator();
           while(it.hasNext())
           {
               IfEntity ifObj = (IfEntity)it.next();
               if(ifObj.getPort().equals("")) continue;
               
               if(ifObj.getDiscards()>2) 
                  out.println("设备" + host.getIpAddress() + " 的接口 " + ifObj.getPort() + " 丢包率>3%<br>");
               if(ifObj.getErrors()>2) 
                  out.println("设备" + host.getIpAddress() + " 的接口 " + ifObj.getPort() + " 错误率>3%<br>");                  
           }
       }  
   } 

%>