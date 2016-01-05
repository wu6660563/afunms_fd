<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.initialize.ResourceCenter"%>
<%@page import="java.io.File"%>
<%
   String rootPath = request.getContextPath();
%>
<html>
  <head>  
  <title>选择设备图例</title>    
  <script language="javascript">
    function assign(x)
    {
        window.opener.document.mainForm.image.value=x;
        window.close();
    }
  </script>
</head>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<body>
<%
    String imagePath = ResourceCenter.getInstance().getSysPath() + "resource/image/device/";
    File imageFolder = new File(imagePath);
    
    String deviceImages[] = imageFolder.list();
    if(deviceImages.length == 0)
         out.println("<div class=font3Black>无系统定义图片</div>");
    else
    {
        out.println("<table border=0 cellspacing=0 cellpadding=8 width='100%'>");
      
        int totalImages = deviceImages.length;
        int columns = 5;
        int rows = totalImages/columns + 1;
        int index = 0;
        for(int i = 0;i<rows;i++)
        { 
           out.println("<tr>");
           for(int j=0;j<columns && (i*columns+j)<totalImages;j++)
           {
              out.println("<td align=center>");    
              out.println("<table border=0>");
              out.println("<tr><td align=center><a href=javascript:assign(\"" + deviceImages[index] + "\")>"); 
              out.println("<img src=" + rootPath +"/resource/image/device/" + deviceImages[index]+ " border=0>");
              out.println("</a></td></tr>");
              out.println("<tr><td class=font1Black align=center>");
              out.println("<a href=javascript:assign(\"" + deviceImages[index] + "\")>"); 
              out.println(deviceImages[index]);
              out.println("</a></td></tr>");
              out.println("</table>");
              out.println("</td>");
              index++;
           }
           out.println("</tr>");
        }
        out.println("</table>");
        deviceImages = null;
    }       
    %>  
  </body>
</html>