<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.*"%>
<%@page import="org.jdom.Element"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.IISManager"%>
<%@page import="com.afunms.application.model.*"%>

<%@page import="java.text.DateFormat"%>


<%  
  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id");
   
  double jvm_memoryuiltillize=0;
  double iisping=0;

  String lasttime ;
  String nexttime;
	String totalBytesSentHighWord= "";
	String totalBytesSentLowWord= "";
	String totalBytesReceivedHighWord= "";
	String totalBytesReceivedLowWord = "";
	
	String totalFilesSent = "";
	String totalFilesReceived = "";
	String currentAnonymousUsers = "";
	String totalAnonymousUsers = "";
	
	String maxAnonymousUsers = "";
	String currentConnections = "";
	String maxConnections = "";
	String connectionAttempts = "";
	
	String logonAttempts = "";
	String totalGets = "";
	String totalPosts = "";
	String totalNotFoundErrors = "";

  List data_list=new ArrayList();
  Hashtable pollingtime_ht=new Hashtable();
  Hashtable imgurlhash=new Hashtable();
  
  IISManager tm= new IISManager();

   
  IIS iis = (IIS)PollingEngine.getInstance().getIisByID(Integer.parseInt(tmp));  
  Hashtable iisvalues = ShareData.getIisdata();
  if(iisvalues != null && iisvalues.size()>0){
  	data_list = (List)iisvalues.get(iis.getIpAddress());
  }
	imgurlhash =(Hashtable) request.getAttribute("imgurlhash");
	//ping连通率
	String image_ping =(String)imgurlhash.get("IISPing").toString();

	iisping=(double)tm.iisping(iis.getId());
	pollingtime_ht=tm.getCollecttime(iis.getIpAddress());

	 if(pollingtime_ht!=null){
	 lasttime=(String)pollingtime_ht.get("lasttime");
	 nexttime=(String)pollingtime_ht.get("nexttime");
	 
	 }else{
	 	lasttime=null;
	 	nexttime=null;	 
	 }
	if(data_list!=null && data_list.size()>0){
		
		IISVo iisvo = (IISVo)data_list.get(0);
		totalBytesSentHighWord= iisvo.getTotalBytesSentHighWord();
		totalBytesSentLowWord= iisvo.getTotalBytesSentLowWord();
		totalBytesReceivedHighWord= iisvo.getTotalBytesReceivedHighWord();
		totalBytesReceivedLowWord = iisvo.getTotalBytesReceivedLowWord();
	
		totalFilesSent = iisvo.getTotalFilesSent();
		totalFilesReceived = iisvo.getTotalFilesReceived();
		currentAnonymousUsers = iisvo.getCurrentAnonymousUsers();
		totalAnonymousUsers = iisvo.getTotalAnonymousUsers();
	
		maxAnonymousUsers = iisvo.getMaxAnonymousUsers();
		currentConnections = iisvo.getCurrentConnections();
		maxConnections = iisvo.getMaxConnections();
		connectionAttempts = iisvo.getConnectionAttempts();
	
		logonAttempts = iisvo.getLogonAttempts();
		totalGets = iisvo.getTotalGets();
		totalPosts = iisvo.getTotalPosts();
		totalNotFoundErrors = iisvo.getTotalNotFoundErrors();
	
	}	


  				String chart1 = null,chart2 = null,chart3 = null,responseTime = null;  
 
  				DefaultPieDataset dpd = new DefaultPieDataset();
  				dpd.setValue("可用时间",iisping);
  				dpd.setValue("不可用时间",100 - iisping);
  				chart1 = ChartCreator.createPieChart(dpd,"",120,120);  	    
		 
		 		//chart2 = ChartCreator.createMeterChart(Math.rint(jvm_memoryuiltillize),"",100,100);  
	       	 
// chart3 = tm.getFreeJVMChart(iis.getId());

				//chart3 = ChartCreator.createMeterChart(100-jvm_memoryuiltillize,"",100,100);  

//     }  
//  }  
// else
//	responseTime = "0ms"; 
// responseTime = "无响应"; 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="img/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
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
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}

}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width="100%">
	<tr bgcolor="#ffffff">
		<td width="200" valign=top align=center>
			<%=menuTable%>		
		
		</td>
		<td  align="left" valign="top" bgcolor="#ffffff">
			<table border="0" id="table1" cellpadding="0" cellspacing="0" width="80%" bgcolor="#ffffff">
    				<tr>
					<td height="25" bgcolor="#397DBD" class="txtGlobalBold" style="HEIGHT: 25px"><div align="center">IIS详细信息</div></td>
   				</tr>
    				<tr>
        <td>
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 
            algin="center">
                <tr>
                <td align="left" valign="middle" class=dashLeft><table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% height= border=1 align="center">
                   <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 
                   algin="center">
					<tr bgcolor="#D1DDF5">
                      <td height="26" colspan="2" valign=center nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">设备信息</div></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" valign=center align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;名称:</td>
                      <td width="70%"><%=iis.getAlias()%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align="left" valign=center nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(iis.getStatus())%>"><%=NodeHelper.getStatusDescr(iis.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=center nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%"><%=iis.getIpAddress()%></td>
                    </tr>
                    <tr>
                      <td width="30%" height="26" align=left valign=center nowrap class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%"><%=lasttime%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td height="26" class=txtGlobal valign=center nowrap>&nbsp;下一次轮询:</td>
                      <td><%=nexttime%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align=right nowrap colspan=3>&nbsp;<a href="<%=rootPath%>/iis.do?action=syncconfig&id=<%=iis.getId()%>" >同步配置</a>&nbsp;&nbsp;</td>
                    </tr>
				  </table>
				</td>
                <td width="169" align="left" valign="top" class=dashLeft>
				<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 >
                    <tr class="topNameRight">
                      <td width="3%" height="7" bgcolor="#FFFFFF">&nbsp;</td>
                      <td width="94%" background="OpManager.files/1spacer.gif" bgcolor="#FFFFFF"><div align="center"></div></td>
                      <td width="3%" bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>
                    <tr class="topNameRight">
                      <td height="70" rowspan="2">&nbsp;</td>
                      <td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#FFFFFF" class="txtGlobalBold">今天的可用率</td>
                      <td height="70" rowspan="2">&nbsp;</td>
                    </tr>
                    <tr class="topNameRight">
                      <td height="70" align="center"><img src="<%=rootPath%>/artist?series_key=<%=chart1%>">
                    </tr>
                    <tr class="topNameRight">
                      <td height="0">&nbsp;</td>
                      <td height="0">&nbsp;</td>
                      <td height="0">&nbsp;</td>
                    </tr>
                </table>
				</td>
			</tr>
			</table>
			</td>
		</tr>
		
	  <tr>
			<td>
			
			
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">

                  				<tr>
                    					<td>
            							<table width="100%" border="0" cellpadding="0" cellspacing="0">
              								<tr> 
                								<td width="30" height="22">&nbsp;</td>
                								<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=iis.getId()%>"><font color="#397dbd">连通率</font></a></div></td>
										<td width="80" height="22" background="<%=rootPath%>/resource/image/anjian_1.gif">
											<div align="center"><a href="<%=rootPath%>/detail/hostutilhdx.jsp?id=<%=iis.getId()%>"><font color="#397dbd">监视信息</font></a></div></td>															
                								<td align=right>&nbsp;&nbsp;</td>
              								</tr>
						<tr> 
                				<td colspan=7>
                					<table width="100%" border="0" cellpadding="0" cellspacing="1">
                    						<tr align="left" bgcolor="397dbd"> 
                      							<td height="23" background="images/yemian_16.gif"><b><font color="#FFFFFF">&nbsp;设备Syslog信息&nbsp;&nbsp;</font></b></td>
                    						</tr>
                  					</table>
                  				</td>
                  				</tr>              								
            							</table>			
			
				<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa  cellPadding=0 rules=none width="100%" align=center border=1 algin="center">
			    <tr>
                    <td width="100%" height="25" bgcolor="#397DBD" class="txtGlobalBold" style="HEIGHT: 25px">
                    <div align="center">连通率</div></td>                    
                    <td width="50%" bgcolor="#397DBD" class="txtGlobalBold" style="HEIGHT: 25px"></td>
			    </tr>
				<tr>
				<td><br>
				</td>
				</tr>
				<tr>
					<td align="left">
					<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
                  cellPadding=0 rules=none width=100% align="left" border=1 algin="center" height=100%>
                      <tr>
                        <td align="left" valign="top" height='30'>
<%if(image_ping!=null){%>                        
                        <img src="<%=rootPath%>/<%=image_ping%>">
                    <br>
<%} else out.print("无数据!");%>                        
                        </td>
                      </tr>
                    </table>
					</td>                 
				</tr>								
			</table>
		</td>
		<td width="">
		</td>
      </tr>
   </table>
 </td>
</tr>
</table>
</form>
</BODY>
</HTML>
 