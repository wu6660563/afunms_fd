<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
   String rootPath = request.getContextPath();
%>
<html>
<head>
<title>topo</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body bgColor="#f7f7f7">
<table width="90%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>

<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/router.jsp" target="mainFrame">ֱ��·��</a></td>  
</tr>  
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/subnet.jsp" target="mainFrame">ֱ������</a></td>  
</tr>  
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/if.jsp" target="mainFrame">�ӿڱ�</a></td>  
</tr> 
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/netmedia.jsp" target="mainFrame">ARP��</a></td>  
</tr> 
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/iprouter.jsp" target="mainFrame">·�ɱ�</a></td>  
</tr> 
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/check.jsp" target="mainFrame">�豸�ж�</a></td>  
</tr>
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/insert_data.jsp" target="mainFrame">��������</a></td>  
</tr> 
<tr class="othertr">
  <td><a href="<%=rootPath%>/tool/single.jsp" target="mainFrame">OID��ѯ</a></td>  
</tr> 
</table>
</body>
</html>