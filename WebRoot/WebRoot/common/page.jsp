<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%
  	String rootPath = request.getContextPath();
	JspPage jp = (JspPage)request.getAttribute("page");
%>
<link rel="stylesheet" href="../resource/css/style.css" type="text/css">

<table width="100%" border="0" cellspacing="1" cellpadding="0">
  <tr class="pagetr">
    <td width="60%" height="30" align="left"><font color='#000000'>&nbsp;��<%=jp.getRecordTotal()%>��&nbsp;&nbsp;��<%=request.getParameter("curpage")%>ҳ / ��<%=request.getParameter("pagetotal")%>ҳ
     &nbsp;����&nbsp;<input name="jp" type="text" style="width:30" class="formStyle" maxLength="3">&nbsp;ҳ
      <input type="button" value="GO" name="bjp" class="button" onclick="goto_page()">
    </font>
      <input type="hidden" name="perpagenum"  
	value="<%=request.getParameter("perpagenum") == null ? "" : request.getParameter("perpagenum")%>" >
	<td width="25%"><div align="center">ÿҳ��ʾ����&nbsp;[<a href="#" onclick="gotoPerPage(30)"><font color="<%if(jp.getPerPage()==30){out.print("#CDAA7D");}else{out.print("#0000FF");}%>" face="Times New Roman, Times, serif"><strong>30</strong></font></a>
	<a href="#" onclick="gotoPerPage(50)"><font color="<%if(jp.getPerPage()==50){out.print("#CDAA7D");}else{out.print("#0000FF");}%>" face="Times New Roman, Times, serif"><strong>50</strong></font></a>
	<a href="#" onclick="gotoPerPage(80)"><font color="<%if(jp.getPerPage()==80){out.print("#CDAA7D");}else{out.print("#0000FF");}%>" face="Times New Roman, Times, serif"><strong>80</strong></font></a>
	<a href="#" onclick="gotoPerPage(100)"><font color="<%if(jp.getPerPage()==100){out.print("#CDAA7D");}else{out.print("#0000FF");}%>" face="Times New Roman, Times, serif"><strong>100</strong></font></a>
	<a href="#" onclick="gotoPerPage(200)"><font color="<%if(jp.getPerPage()==200){out.print("#CDAA7D");}else{out.print("#0000FF");}%>" face="Times New Roman, Times, serif"><strong>200</strong></font></a>]</div></td>
    <td width="15%" align="right">
      <img src="<%=rootPath%>/resource/image/microsoftLook/first(on).gif" border="0" onclick="firstPage()" style="CURSOR:hand" alt="��ҳ"/>
      <img src="<%=rootPath%>/resource/image/microsoftLook/previous(on).gif" border="0" onclick="precedePage()" style="CURSOR:hand" alt="��һҳ"/>
      <img src="<%=rootPath%>/resource/image/microsoftLook/forward(on).gif" border="0" onclick="nextPage()" style="CURSOR:hand" alt="��һҳ"/>
      <img src="<%=rootPath%>/resource/image/microsoftLook/last(on).gif" border="0" onclick="lastPage()" style="CURSOR:hand" alt="βҳ"/>    </td>
  </tr>
</table>
