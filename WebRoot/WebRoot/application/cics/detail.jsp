<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  String transactionId = (String)request.getAttribute("transactionId");
  String displayInfo = (String)request.getAttribute("displayInfo");
  String ipAddress = (String)request.getAttribute("ipAddress");
  String serverName = request.getParameter("serverName");
  String urlStr = request.getParameter("urlStr");
  String Port = request.getParameter("Port");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	 
function change(value){
    location.href="<%=rootPath%>/cics.do?action=displayData&serverName="+"<%=serverName%>"+"&transactionId="+value+"&urlStr="+"<%=urlStr%>"+"&ipAddress="+"<%=ipAddress%>"+"&Port="+"<%=Port%>";
}
</script>

</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>	
		</td>
		<td class="td-container-main" align="center" valign=top>
<table align="center" cellSpacing="0" cellPadding="0" id="tblListTitle" width="98%" border="0" >
      <tr>
		<!--<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;Ӧ�÷�����>>CICS���ݲɼ�</td> -->
	  </tr>
      
      <tr style="background-color: #ECECEC;">						
			<TD nowrap align="left" height="30" width="100%" colspan=2>
				<table id="application-detail-content-header" class="application-detail-content-header" width="100%">
                	<tr>
	                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
	                	<td class="application-detail-content-title">
	                	    ������&nbsp;
							<select name="transactionId"  class="formStyle" onchange="change(this.value)">
							    <option value="">��ѡ��...</option>
								<option value="CST1">TS �� TD ͳ��</option>
								<option value="CST2">�ش洢��ͳ��</option>
								<option value="CST3">����ͳ��</option>
								<option value="CST4">ISC��ϸͳ��</option>
								<option value="CST5">�ļ�ͳ��</option>
								<option value="CST6">�ն�ͳ��</option>
								<option value="CST7">����ͳ��</option>
								<option value="CST8">����ͳ��</option>
								<option value="CST9">����������ͳ��</option>
								<option value="CSTA">ISCժҪͳ��</option>
								<option value="CSTB">������/��������</option>
							</select>   
	                	</td>
	                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
	       			</tr>
	        	</table>
			</TD>
		</tr>
        <tr> 
          <td valign="top" align="left" bgcolor="#ffffff" >
            <table width="95%" border="0" cellspacing="1" cellpadding="3" class="lrtbdarkborder">
                 <tr style="background-color: #ECECEC;"> 
                    <td colspan="2" class="tableheadingbborder">������Ϣ</td>
                  </tr>
                  <tr> 
                    <td width="45%">����</td><td width="55%">CICS���ݲɼ�</td>
                  </tr>
                  <tr> 
                    <td width="45%">��������</td><td width="55%"><% out.print(serverName);%></td>
                  </tr>
                  <tr> 
                    <td width="45%">������</td><td width="55%"><% out.print(transactionId);%></td>
                  </tr>
                  <tr> 
                    <td width="45%">��������IP��ַ</td><td width="55%"><% out.print(ipAddress);%></td>
                  </tr>
                  <tr>
                    <td width="45%">�����˿�</td><td width="55%"><% out.print(Port);%></td>
                  </tr>
                  <tr>
                    <td width="45%">Gateway</td><td width="55%"><% out.print(urlStr);%></td>
                  </tr>
          </table>
        </td>  
        <td width="70%" valign="top" align="right" bgcolor="#ffffff" >
           <table width="100%" border="0" cellspacing="1" cellpadding="3" class="lrtbdarkborder">
                 <tr style="background-color: #ECECEC;"> 
                    <td class="tableheadingbborder">&nbsp;������Ϣ</td>
                  </tr>
                  <tr> 
                    <td >
                       <% out.print(displayInfo);%>
                    </td>
                    
                  </tr>
          </table>
        </td>      
      </tr>     
</table>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>
