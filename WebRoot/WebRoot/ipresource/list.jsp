<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.ipresource.model.IpMac"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");  
  String rootPath = request.getContextPath();    
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var listAction = "<%=rootPath%>/ipmac.do?action=list";
  var delAction = "<%=rootPath%>/ipmac.do?action=delete";
  
  function doChange()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=detail&jp=1";
     mainForm.submit();
  }
  
  function doFind()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=find";
     mainForm.submit();     
  }

  function findIdleIp()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=idle_ips";
     mainForm.submit();     
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=ready_add";
     mainForm.submit();     
  }
  
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>			
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
					<table cellSpacing="1" cellPadding="0" width="95%" border="0" align="center">
                     <tr><td width="60%" align='left'><font color="blue">���ؼ��ֲ�ѯ:</font>&nbsp;
                         <select size=1 name='key' style='width:80px;'>
                           <option value="ip_address">IP</option>
                           <option value="mac">MAC</option>
                           <option value="net">����</option>
						   <option value="person">�û���</option>
						   <option value="dept">����</option>
						   <option value="room">�����</option>
						   <option value="tel">�绰</option>                        
                         </select>
                       =<input name="value" type="text" style="width:110" class="formStyle" maxLength="20">
                       &nbsp;<input type="button" class="button" value="��ѯ" name="B1" onclick="doFind()"> 
                       &nbsp;&nbsp;
                       <input type="button" class="button" value="��ʱ�䲻�õ�IP" name="B1" onclick="findIdleIp()">
                       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                       <a href="<%=rootPath%>/ipmac.do?action=report" 
                       target="_blank"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" 
                       src="<%=rootPath%>/resource/image/export_excel.gif" width=30 height=32 border="0"></a></td></tr>
              <tr><td height='20'>&nbsp;</td>&nbsp;
              <td>&nbsp;</td></tr>
			  <tr><td colspan="2" width="80%" align="center">
    <jsp:include page="../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td></tr> 
						<tr>
							<td colspan="2">
								<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%" align='center'>
								<tr><th width='22'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    <th width='5%'>���</th>
    <th width='15%'>IP��ַ</th>
    <th width='20%'>MAC��ַ</th>
    <th width='15%'>�û���</th>
    <th width='15%'>����</th>
    <th width='15%'>�����</th>
    <th width='10%'>�绰</th>
    <th width='5%'>�༭</th>    
</tr>
<%
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       IpMac  vo = (IpMac)list.get(i);
%>
       <tr class="microsoftLook0" >
    	<td class="microsoftLook0"><INPUT type="checkbox" class=noborder name="checkbox" value="<%=vo.getId()%>"/></td>
    	<td class="microsoftLook0"><font color='blue'><%=startRow + i%></font></td>
<%
   if(vo.isIdle())
   {
%>    	
		<td class="microsoftLook0" align='center'><font color='blue'><%=vo.getIpAddress()%></font></td> 				
<%
   }else{
%>		
		<td class="microsoftLook0" align='center'><%=vo.getIpAddress()%></td> 				
<%}%>		
		<td class="microsoftLook0" align='center'><%=vo.getMac()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getPerson()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getDept()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getRoom()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getTel()%></td>	
        <td class="microsoftLook0"><a href="<%=rootPath%>/ipmac.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>		
  	</tr>
<%}%>  	  				
							</table>
							</td>
						</tr>	
					</table>
					<table cellspacing="5" border="0"><tr>
					<td>&nbsp;&nbsp;</td>
			<td><a href="#" onclick="toAdd()">���</a></td>
			<td><a href="#" onclick="toDelete()">ɾ��</a></td></tr>
			</table>									
			</td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13></td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>