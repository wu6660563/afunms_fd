<%@page language="java" contentType="text/html;charset=GB2312" import="com.jspsmart.upload.SmartUpload"%><%    
    String fileName = request.getParameter("filename");
	try
	{
		SmartUpload download = new SmartUpload();    
	    download.initialize(pageContext);
	    download.downloadFile(fileName);
	    	
	}
	catch(Exception e)
	{
		System.out.println("Error in download!");
	}
%>