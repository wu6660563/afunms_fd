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
          out.println("�豸" + host.getIpAddress() + " Ping��ͨ��<br>");
       else
       {
           Iterator it = host.getInterfaceHash().values().iterator();
           while(it.hasNext())
           {
               IfEntity ifObj = (IfEntity)it.next();
               if(ifObj.getPort().equals("")) continue;
               
               if(ifObj.getDiscards()>2) 
                  out.println("�豸" + host.getIpAddress() + " �Ľӿ� " + ifObj.getPort() + " ������>3%<br>");
               if(ifObj.getErrors()>2) 
                  out.println("�豸" + host.getIpAddress() + " �Ľӿ� " + ifObj.getPort() + " ������>3%<br>");                  
           }
       }  
   } 

%>