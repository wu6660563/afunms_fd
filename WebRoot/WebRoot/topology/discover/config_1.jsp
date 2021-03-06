<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.DiscoverConfig"%>
<%@page import="com.afunms.discovery.*"%>
<%@page import="com.afunms.topology.manage.*"%>

<%
  String rootPath = request.getContextPath();  
  List communityList = (List)request.getAttribute("community_list");
  List othercoreList = (List)request.getAttribute("othercore_list");
  List specifiedList = (List)request.getAttribute("specified_list"); 
  List shieldList = (List)request.getAttribute("shield_list"); 
  List netshieldList = (List)request.getAttribute("netshield_list");  
  List includeList = (List)request.getAttribute("include_list");
		
  DiscoverDataHelper helper = new DiscoverDataHelper();
  //helper.DB2NetworkXml();
  //helper.DB2NetworkVlanXml();
         try{
    	   Runtime.getRuntime().exec("ping 10.95.240.2");
    	   System.out.println("===========");
       }catch(Exception ex){
    	   ex.printStackTrace();
       }
%>
<HTML>
<HEAD>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">

<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
 
  function doAdd1()
  {
     var chk1 = checkinput("globe_community","string","共同体名",20,false);
     if(chk1)
     {
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=community";
        mainForm.submit();
     }  
  } 
  
  function doAdd2()
  {
     var chk1 = checkinput("special_ip","ip","IP地址",15,false);
     var chk2 = checkinput("special_community","string","共同体名",20,false);
     if(chk1&&chk2)
     {          
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=specified";
        mainForm.submit();        
     }        
  }
  
    function doAdd5()
  {
     var chk1 = checkinput("othercore_ip","ip","IP地址",15,false);
     var chk2 = checkinput("othercore_community","string","共同体名",20,false);
     if(chk1&&chk2)
     {          
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=othercore";
        mainForm.submit();        
     }        
  } 

  function doAdd3()
  {     
     var len = mainForm.net_address.value.length;
     if(len==0)
     {
         alert("请输入网段地址!");
         mainForm.net_address.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<网段地址不正确>请正确输入网段地址!");
         mainForm.net_address.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.net_address.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<网段地址不正确>请正确输入网段地址");
              mainForm.net_address.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=shield";
     mainForm.submit();     
  } 
  
    function doAdd4()
  {     
     var len = mainForm.shieldnetstart.value.length;
     if(len==0)
     {
         alert("请输入网段地址!");
         mainForm.shieldnetstart.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<网段地址不正确>请正确输入网段地址!");
         mainForm.shieldnetstart.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.shieldnetstart.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<网段地址不正确>请正确输入网段地址");
              mainForm.shieldnetstart.focus();
              return false;
	       }
        }
     }
     len = mainForm.shieldnetend.value.length;
     if(len==0)
     {
         alert("请输入网段地址!");
         mainForm.shieldnetend.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<网段地址不正确>请正确输入网段地址!");
         mainForm.shieldnetend.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.shieldnetend.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<网段地址不正确>请正确输入网段地址");
              mainForm.shieldnetend.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=netshield";
     mainForm.submit();     
  }
  
    function doAdd6()
  {     
     var len = mainForm.includenetstart.value.length;
     if(len==0)
     {
         alert("请输入网段地址!");
         mainForm.includenetstart.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<网段地址不正确>请正确输入网段地址!");
         mainForm.includenetstart.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.includenetstart.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<网段地址不正确>请正确输入网段地址");
              mainForm.includenetstart.focus();
              return false;
	       }
        }
     }
     len = mainForm.includenetend.value.length;
     if(len==0)
     {
         alert("请输入网段地址!");
         mainForm.includenetend.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<网段地址不正确>请正确输入网段地址!");
         mainForm.includenetend.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.includenetend.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<网段地址不正确>请正确输入网段地址");
              mainForm.includenetend.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=includenet";
     mainForm.submit();     
  }  
  
  function doDelete(id)
  {
     if (window.confirm("确实要删除吗?"))
     {
        mainForm.id.value = id;
        mainForm.action = "<%=rootPath%>/discover.do?action=delete";
        mainForm.submit();      
     }   
  }
  
  function doDiscover()
  {
     var result;
     if(mainForm.core_ip.disabled == false)
     {
        var chk1 = checkinput("core_ip","ip","设备IP",15,false);
        var chk2 = checkinput("community","string","共同体",20,false);
        result = chk1&&chk2
     }   
     //else
        //result = checkinput("net_ip","ip","网段IP",15,false);
     
     if(result)
     {        
        mainForm.action = "<%=rootPath%>/discover.do?action=do_discover";
        mainForm.submit();        
        window.showModalDialog("<%=rootPath%>/topology/discover/monitor.jsp","发现进程监视","dialogHeight:700px;dialogWidth:550px;status:0;help:0;edge:sunken;scroll:1"); 
     }
  }     

  function fromCore()
  {
      mainForm.core_ip.disabled = false;
      mainForm.community.disabled = false;
      //mainForm.net_ip.disabled = true;
      //mainForm.netmask.disabled = true;
      initmenu();
  }
  
  function fromNet()
  {
      mainForm.core_ip.disabled = true;
      mainForm.community.disabled = true;
      //mainForm.net_ip.disabled = false;
      //mainForm.netmask.disabled = false;
  }
</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	m1 =new Menu("menu1",'menu1_child','dtu','100',show,my_on,my_off);
	m1.init();
}
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="fromCore();">
<form name="mainForm" method="post">
<input type=hidden name="id">
<table border="0" id="table1" cellpadding="0" cellspacing="0" >
	<tr>
		<td width="200" valign=top align=center>               		           				
			<div class="tit" id="menu1" title="菜单标题">		
				<a href="#nojs" title="折叠菜单" target="" class="on" id="menu1_a" tabindex="1" >快捷功能</a> 
			</div>
			<div class="list" id="menu1_child" title="菜单功能区" >
			<ul>
			<li id="m1_1" ><a href="<%=rootPath%>/network.do?action=ready_add">&nbsp;增加设备</a></li>
			<li id="m1_2" ><a href="<%=rootPath%>/discover.do?action=config">&nbsp;自动发现</a></li>
			<li id="m1_3" ><a href="<%=rootPath%>/user.do?action=list&jp=1">&nbsp;用户管理</a></li>
			<li id="m1_4" ><a href="<%=rootPath%>/snmp.do?action=list" >&nbsp;SNMP设置</a></li>
			<li id="m1_5" ><a href="<%=rootPath%>/system/user/inputpwd.jsp">&nbsp;密码设置</a></li>

			</ul>
			</div>
		</td>
		<td width="2" valign=top align=center bgcolor="#397dbd">&nbsp;</td>
		<td bgcolor="#ffffff" valign="top"  align="left" width=100%>
		<!--发现配置主页面开始-->
		
		
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>

				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;<font color=#ffffff>自动发现 >> 发现参数配置</font></td>
				</tr>
	<tr>
		<td width="16"></td>
		<td align="left">
		<br>
		<table width="100%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top">				
					<table cellSpacing="1" cellPadding="0" width="90%" border="0" align='left'>
<!--================核心设备===========================-->
										
						<tr>
							<td colspan="2" align='center'>
								<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
								<tr class="microsoftLook0"><th width='70%' class="microsoftLook0">种子地址</th></tr>
     <tr class="microsoftLook0" height=30>
    	<td class="microsoftLook0">&nbsp;&nbsp;
           核心IP:<input type="text" name="core_ip" size="20" class="formStyle" value=""><font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
           读共同体:&nbsp;&nbsp;<input type="text" name="community" size="20" class="formStyle" value=""><font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
           写共同体:&nbsp;&nbsp;<input type="text" name="writecommunity" size="20" class="formStyle" value="">&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 
     <tr height=30>
    	<td>&nbsp;&nbsp;
           <INPUT type="radio" class=noborder name=discovermodel value="0" checked>全新发现&nbsp;&nbsp;&nbsp;&nbsp;
           <INPUT type="radio" class=noborder name=discovermodel value="1">补充发现&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	   	 
<!--================其他种子地址与共同体===========================-->  	 
  	 <tr class="microsoftLook0"><th width='70%' class="microsoftLook0">其他种子地址</th></tr>
     <tr >
    	<td >&nbsp;           
           种子IP:<input type="text" name="othercore_ip" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           共同体:&nbsp;&nbsp;<input type="text" name="othercore_community" size="20" class="formStyle" value="">
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd5()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<othercoreList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)othercoreList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='30%'><%=vo.getAddress()%>&nbsp;,&nbsp;</td>
            <td width='30%'>&nbsp;<%=vo.getCommunity()%>&nbsp;<td>
            <td width='20%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>  	 
<!--================全局共同体===========================-->  	 
  	 <tr><th width='70%'>全局共同体</th></tr>
     <tr >
    	<td >&nbsp;           
           共同体名:<input type="text" name="globe_community" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd1()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr>
    	<td  align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<communityList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)communityList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getCommunity()%>&nbsp;<td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>  
<!--================指定设备节点共同体===========================--> 
  	 <tr><td nowrap colspan="2" height="2" bgcolor="#8EADD5"></td></tr>
  	 <tr><th width='60%'>特定设备节点共同体</th></tr>
     <tr >
    	<td >&nbsp;&nbsp;&nbsp;
    	  IP地址:<input type="text" name="special_ip" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           共同体名:<input type="text" name="special_community" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd2()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<specifiedList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)specifiedList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='30%'><%=vo.getAddress()%>&nbsp;,&nbsp;</td>
            <td width='30%'>&nbsp;<%=vo.getCommunity()%>&nbsp;<td>
            <td width='20%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 

<!--================只发现以下网段设备===========================-->  	
  	 <tr><th width='70%'>只发现以下的网段</th></tr>
     <tr>
    	<td >&nbsp;           
           <!--网段地址:<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;-->
           起始网段:<input type="text" name="includenetstart" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           结束网段:<input type="text" name="includenetend" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd6()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<includeList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)includeList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getIncludenetstart()%>&nbsp;至&nbsp;<%=vo.getIncludenetend()%><td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>


 
<!--================要屏蔽的IP===========================-->  	
  	 <tr><th width='70%'>要屏蔽的IP</th></tr>
     <tr >
    	<td >&nbsp;           
           网段地址:<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd3()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr>
    	<td align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<shieldList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)shieldList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getAddress()%>&nbsp;<td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 


<!--================要屏蔽的网段===========================-->  	 
  	 <tr><th width='70%'>要屏蔽的网段1</th></tr>
     <tr >
    	<td >&nbsp;           
           <!--网段地址:<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;-->
           起始网段:<input type="text" name="shieldnetstart" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           结束网段:<input type="text" name="shieldnetend" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="增加" style="width:50" class="button" onclick="doAdd4()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>&nbsp;&nbsp;
    	  <table width="50%">
  	 
<%
   for(int i=0;i<netshieldList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)netshieldList.get(i);
       alert(vo.getId());
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getShieldnetstart()%>&nbsp;至&nbsp;<%=vo.getShieldnetend()%><td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="删除" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 

 	   	<tr><td height='20'></td></tr>   				
							</table>
							</td>
						</tr>	
				<tr><td colspan="2" align='center'>
				<input type="button" value="开始发现" style="width:100" class="formStylebutton" onclick="doDiscover()">
				</td></tr>		
					</table>			
			</td>
			</tr>
		</table>
		</td>
	</tr>
</table>		
		
		
		
		
		<!--发现配置页面结束-->
		</td>
		<td width="2" valign=top align=center bgcolor="#397dbd">&nbsp;</td>	
	</tr>

</table>
</form>	
</body>
</HTML>
