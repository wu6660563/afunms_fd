<%@page language="java" contentType="text/html;charset=utf-8"%>
<%@ page  import="com.afunms.common.util.CEIString"%>
<%@ page  import="com.jspsmart.upload.SmartUpload"%>
<%@ page  import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
%>
<html>
<head>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	 
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 50);	
	
});
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
<form name="mainForm" method="post">
<%    
    Hashtable hash = (Hashtable)request.getAttribute("hash");
    String index = "";
    String ifdesc= "";
    String iftype= "";
    String ifspeed = "";
    String adminvalue = "";
    String opervalue = "";
  String adminup = "";
  String admindown = "";
    	String operup = "";
  	String operdown = "";
  if(hash != null && hash.size()>0){
  	index = (String)hash.get("index");
  	ifdesc = (String)hash.get("ifDescr");
  	iftype = (String)hash.get("ifType");
  	ifspeed = (String)hash.get("ifSpeed");
  	adminvalue = (String)hash.get("ifAdminStatus");
  	opervalue = (String)hash.get("ifOperStatus");
  	if(adminvalue.equals("up")){
  		adminup="selected";
  	}else{
  		admindown="selected";
  	}

  	if(opervalue.equals("up")){
  		operup="selected";
  	}else{
  		operdown="selected";
  	}  
  }  
    
%>
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<br>
<br>
						<table cellspacing="1" cellpadding="0" width="100%" bgcolor=#397DBD>
							<tr bgcolor="#397DBD" height=28>
								<td colspan="4" align="center" height='28'><font color=#ffffff><b>端口设置</b></font></td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
  								<td align="right">端口索引</td>
  								<td align="left">&nbsp;<%=index%></td>
  								<td align="right">端口描述</td> 
  								<td align="left">&nbsp;<%=ifdesc%></td> 
							</tr>	
							<tr bgcolor="#F1F1F1" height=25>
  								<td align="right">端口类型</td>
  								<td align="left">&nbsp;<%=iftype%></td>
  								<td align="right">速率</td> 
  								<td align="left">&nbsp;<%=ifspeed%></td> 
							</tr>
							<tr height=25 bgcolor=#ffffff>
  								<td align="right">当前状态</td>
  								<td align="left">&nbsp;
  									<select operstatus>
  										<option value="up" <%=operup%>>up</option>
  										<option value="down" <%=operdown%>>down</option>
  									</select>  								
  								</td>
  								<td align="right">管理状态</td> 
  								<td align="left">&nbsp;
  									<select adminstatus>
  										<option value="up" <%=adminup%>>up</option>
  										<option value="down" <%=admindown%>>down</option>
  									</select>
  								</td> 
							</tr>
							<tr height=30 align=center bgcolor=#ffffff>
  								<td align="center" colspan=4>
  								<br>
  								<br>
  									<input type=button value="配 置" onclick="window.close()">&nbsp;&nbsp;
  									<input type=button value="关 闭" onclick="window.close()">
  								<br>
  								<br>
  								</td> 
							</tr>																														
						</table>
</form>	
</BODY>
</HTML>