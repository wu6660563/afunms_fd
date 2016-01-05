<%@page language="java" contentType="text/html;charset=gb2312"%>
<%
 String rootPath = request.getContextPath();
 String contentTitle=request.getParameter("contentTitle")==null?"":request.getParameter("contentTitle");
%>
	<style type="text/css" media="print">
		    .noprint{display:none;}
		</style>
<table width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
	<tr>
	 	<td align="left"><div class="noPrint"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></div></td>
		<td class="layout_title"><b>设备详细信息</b></td>
	 	<td align="right"><div class="noPrint"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></div></td>
	</tr>
</table>