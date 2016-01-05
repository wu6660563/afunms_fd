<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.IPDistrictConfig"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.polling.*"%>

<%
  String rootPath = request.getContextPath();

	 
	List list = PollingEngine.getAddiplist();
  
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/ipdistrict.do?action=delete";
  var listAction = "<%=rootPath%>/ipdistrict.do?action=list";
   
  function toAdd(sign)
  {
     mainForm.action = "<%=rootPath%>/network.do?action=ipalladd&sign="+sign;
     mainForm.submit();
  }
    
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">


<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">资源 >> IP/MAC资源 >> IP地址段管理</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
      </tr>
			  
			        <tr>           
								<td>
								     <table width="100%" cellpadding="0" cellspacing="1" >
								<tr style="background-color: #ECECEC;">	
								     <td>	
									       &nbsp;IP地址段&nbsp;&nbsp;从 <input type="text" name="startip"> 到 <input type="text" name="endip">
									     <input type="button" value="添加" style="width:50" onclick="toAdd(2)">
								      </td>
					             </tr>
						
        						     </table>
								</td>
        			  </tr>		
	 					<tr>
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
                      <td width="50%" align="center"><strong>IP地址段</strong></td>
   
					</tr>
   
      <tr bgcolor="#FFFFFF"  <%=onmouseoverstyle%> height=25>
       <% 
	     for(int i = 0; i<list.size();i++) {
	       Hashtable hst = (Hashtable)list.get(i);
		  
	   %>
       <td align="center" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;">
       <%if(hst.get("endip")!=null && !"".equals(hst.get("endip")) && !"null".equals(hst.get("endip")))
       { %>
       <%=hst.get("startip")%>到<%=hst.get("endip") %>
       <%}else{ %>
        <%=hst.get("ipaddress")%>
        <%} %>
       &nbsp;</td>
        </tr> 
		
		<%
		// }
	  }
			%>	
			
			</table>

			</td>

		</tr>	
		      <table>
		<tr align=center>
		<td align=center><input type=reset style="width:50" value="关闭" onClick="javascript:window.close()">
		</td>
		</tr>
		</table>
			  
		<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
                  </tr>
              </table></td>
            </tr>	
	</table>
</td>
			</tr>
			
		</table>
		
</BODY>
</HTML>
