<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.dhcnms.inform.util.PortPdfReport"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%
    String rootPath = request.getContextPath();
    
	PortPdfReport ppr = new PortPdfReport();
	String path = request.getRealPath("/");
	ppr.setFullPath(path + "pdfTest.pdf");
	ppr.setBeginDate("2006-11-07");
	ppr.setNodeId(69);
	ppr.setIndexOfPort("4227665");
	
	ppr.createPdf();
	ppr.drawHead("端口详细信息表");
	ppr.drawHeadTable();
	ppr.drawTrafficPdf();
	//ppr.newPage();
	ppr.drawPercentagePdf();
	ppr.savePdf();

%>
<html>
<head>
<title></title>
</head>
<body>
　
</body>
</html>