<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.dhcnms.report.jfree.ChartCreator"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="org.jfree.data.category.CategoryDataset"%>
<%@page import="org.jfree.data.general.DatasetUtilities"%>
<%
    String rootPath = request.getContextPath();
    
    //----------------钟图----------------
    String key1 = ChartCreator.createMeterChart(72f,"CPU利用率",200,200);        
    
    //----------------饼图----------------
    DefaultPieDataset dpd = new DefaultPieDataset();
    dpd.setValue("可用时间",80);
    dpd.setValue("不可用时间",20);
    String key2 = ChartCreator.createPieChart(dpd,"今天的可用率",200,200);  
    
    //--------------重叠柱状图 ----------------
    String[] xKeys = new String[]{"C","D","E","F"};
    String[] yKeys = new String[]{"已用","未用"};
    double[][] data = new double[][]{{10,20,30,40},{90,80,70,60}};
    CategoryDataset dataset = DatasetUtilities.createCategoryDataset(yKeys,xKeys,data);
    String key3 = ChartCreator.createStackeBarChart(dataset,"硬盘","利用率(%)","硬盘利用率",200,200);      
%>
<html>
<head>
<title></title>
</head>
<body>
<form name="FrmList" method="post" action="">
<table width='700' border='1' cellspacing='0' cellpadding='0' align='center' bordercolorlight='#000000' bordercolordark='#FFFFFF'>
<tr><td width="40%"><img src="<%=rootPath%>/artist?series_key=<%=key1%>"></td></tr>   
<tr><td width="40%"><img src="<%=rootPath%>/artist?series_key=<%=key2%>"></td></tr> 
<tr><td width="40%"><img src="<%=rootPath%>/artist?series_key=<%=key3%>"></td></tr> 
</table>
</form> 　
</body>
</html>