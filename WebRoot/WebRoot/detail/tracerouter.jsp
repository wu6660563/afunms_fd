 <%@ page language="java" contentType="text/html; charset=GBK" %>
 <%@ page  import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
System.out.println("*************");
	String getIp=request.getParameter("ipaddress") ;
	if(getIp==null) getIp="";
%>
<html:html locale="true">
  <head>
    <html:base />
    
    <title><bean:message key="webnms.title"/></title>
<body>
<div align="center">
  <h2>TraceRouter IP <%=getIp%></h2>
</div> 

<%  
   //String os=System.getProperty("os.name");
//out.println(os);
   Runtime  runtime  =  Runtime.getRuntime();  
   Process  process  =null;  
   String  line=null;  
   InputStream  is  =null;  
   InputStreamReader  isr=null;  
   BufferedReader  br  =null;  
   String  ip=getIp;  //´ıPingµÄµØÖ·
    try  
   {  
  String cmd="";
  cmd = "tracert -h 20 -w 3000 " + ip;
       process  =runtime.exec(cmd);  
       is  =  process.getInputStream();  
       isr=new  InputStreamReader(is);  
       br  =new  BufferedReader(isr);  
       boolean b_connected=true;
       out.println("<pre>");  
       while(  (line  =  br.readLine())  !=  null  )  
       {  
       //if(line.indexOf("100% packet loss")>=0 && os.indexOf("Linux")>=0)b_connected=false;
       //if(line.indexOf("100% loss")>=0 && os.indexOf("Windows")>=0)b_connected=false;
      // out.println("index is "+line.indexOf("0% packet loss"));
           out.println(line);  
           out.flush();  
       }  
       
       out.println("</pre>");  
       is.close();  
       isr.close();  
       br.close();  
   }  
   catch(IOException  e  )  
   {  
       out.println(e);  
       runtime.exit(1);  
   }  
%>  
<br><center>
    <input type="button" value=" ¹Ø  ±Õ " class="button1" onclick="window.close()">
</center>
</body>
</html:html>

