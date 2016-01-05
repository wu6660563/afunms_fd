<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.TomcatManager"%>
<%  
  String rootPath = request.getContextPath(); 
  String tmp = request.getParameter("id");
   
  double jvm_memoryuiltillize=0;
  double tomcatping=0;

  String lasttime ;
  String nexttime;
  String jvm;

  Hashtable data_ht=new Hashtable();
  Hashtable pollingtime_ht=new Hashtable();
  TomcatManager tm= new TomcatManager();

   
  Tomcat tomcat = (Tomcat)PollingEngine.getInstance().getTomcatByID(Integer.parseInt(tmp));  
  Hashtable tomcatvalues = ShareData.getTomcatdata();
  if(tomcatvalues != null && tomcatvalues.size()>0){
  	data_ht = (Hashtable)tomcatvalues.get(tomcat.getIpAddress());
  }


	tomcatping=(double)tm.tomcatping(tomcat.getId());
	pollingtime_ht=tm.getCollecttime(tomcat.getIpAddress());

	 if(pollingtime_ht!=null){
	 lasttime=(String)pollingtime_ht.get("lasttime");
	 nexttime=(String)pollingtime_ht.get("nexttime");
	 
	 }else{
	 lasttime=null;
	 nexttime=null;	 
	 }
	if(data_ht!=null){
		jvm=data_ht.get("jvm").toString();
	}else{
			jvm="";
	}	
				double totalmemory = 0;
				double maxmemory= 0;
				double freememory=0;
				StringBuffer info = new StringBuffer(100);
				if(jvm != null && jvm.trim().length()>0){
					String [] temjvm=jvm.split(",");
					freememory=Double.parseDouble(temjvm[0].trim());
					totalmemory=(double)Double.parseDouble(temjvm[1].trim());
					maxmemory=(double)Double.parseDouble(temjvm[2].trim());					
    					info.append("可用内存：");
    					info.append(freememory);
    					info.append("MB,总内存：");
    					info.append(totalmemory);
    					info.append("MB,最大内存：");
    					info.append(maxmemory);
    					info.append("MB");    				
					jvm_memoryuiltillize=(totalmemory-freememory)*100/totalmemory;								
				}


  				String chart1 = null,chart2 = null,chart3 = null,responseTime = null;  
 
  				DefaultPieDataset dpd = new DefaultPieDataset();
  				dpd.setValue("可用时间",tomcatping);
  				dpd.setValue("不可用时间",100 - tomcatping);
  				chart1 = ChartCreator.createPieChart(dpd,"",120,120);  	    
		 
		 		chart2 = ChartCreator.createMeterChart(Math.rint(jvm_memoryuiltillize),"",100,100);  
	       	 
// chart3 = tm.getFreeJVMChart(tomcat.getId());

				chart3 = ChartCreator.createMeterChart(100-jvm_memoryuiltillize,"",100,100);  

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
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
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
					<td height="25" bgcolor="#397DBD" class="txtGlobalBold" style="HEIGHT: 25px"><div align="center">Tomcat详细信息</div></td>
   				</tr>
    			<tr>
                    <td>
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1>
                <tr>
                <td align="left" valign="middle" class=dashLeft><table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% height= border=1 align="center">
                   <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1>
					<tr bgcolor="#D1DDF5">
                      <td height="26" colspan="2" nowrap class=txtGlobal><div align="center" class="txtGlobalBigBold">设备信息</div></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align="left" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;名称:</td>
                      <td width="70%"><%=tomcat.getAlias()%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(tomcat.getStatus())%>"><%=NodeHelper.getStatusDescr(tomcat.getStatus())%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%"><%=tomcat.getIpAddress()%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align="left" nowrap>&nbsp;端口:</td>
                      <td><%=tomcat.getPort()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;Tomcat版本:</td>
                      <td width="70%"><%=tomcat.getVersion()%></td>
                    </tr>
                    <tr>
                      <td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;JVM版本:</td>
                      <td width="70%"><%=tomcat.getJvmversion()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;JVM供应商:</td>
                      <td width="70%"><%=tomcat.getJvmvender()%></td>
                    </tr>
                    <tr>
                      <td width="30%" height="26" align=left nowrap class=txtGlobal>&nbsp;服务器操作系统:</td>
                      <td width="70%"><%=tomcat.getOs()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" nowrap bgcolor="#EBF4F7" class=txtGlobal>&nbsp;操作系统版本:</td>
                      <td width="70%"><%=tomcat.getOsversion()%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal nowrap >&nbsp;最新告警信息:</td>
                      <td><%=tomcat.getLastAlarm()%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" 
                    align=left nowrap bgcolor="#EBF4F7"  class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%"><%=lasttime%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal nowrap>&nbsp;下一次轮询:</td>
                      <td><%=nexttime%></td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align=right nowrap colspan=3>&nbsp;<a href="<%=rootPath%>/tomcat.do?action=syncconfig&id=<%=tomcat.getId()%>" >同步配置</a>&nbsp;&nbsp;</td>
                    </tr>
				  </table>
				</td>
                <td width="200" align="left" valign="top" class=dashLeft>
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 >
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
				    <table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa 
                         cellPadding=0 rules=none width="100%" align=center border=1>
                      <tr>
						<td width="30%" height="25" bgcolor="#397DBD" class="txtGlobalBold" style="HEIGHT: 25px">
						 <div align="center">JVM内存利用率</div>
						</td>  
					  </tr>
					  <tr>
						<td align="center" valign="middle" height='30'>
					<%if(chart2!=null){%>                        
                        <img src="<%=rootPath%>/artist?series_key=<%=chart2%>">
						 <br><%=info.toString()%>
					<%} else out.print("无数据!");%>                        
                        </td>
					  </tr>
					</table>
				</td>
			</tr>
			</table>
			</td>
		</tr>
		
	  <tr>
		<td>
               <table border="0" cellpadding="0" cellspacing="0" align=center>
                <tr>
                    <td align=center >
                    <br>
                      <table cellpadding="0" cellspacing="0" width=98%>
              							<tr> 
                							<td width="100%" > 
                								<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Tomcat_Ping.swf?ipadress=<%=tomcat.getIpAddress()%>", "Tomcat_Ping", "346", "250", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  <td>&nbsp;</td>
                  <td align=center>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=98%>
              							<tr> 
                							<td width="100%" > 
                								<div id="flashcontent2">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Tomcat_JVM_Memory.swf?ipadress=<%=tomcat.getIpAddress()%>", "Tomcat_JVM_Memory", "346", "250", "8", "#ffffff");
													so.write("flashcontent2");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  </tr>                                      				
              </table>			
		</td>
      </tr>
   </table>
 </td>
</tr>
</table>
</BODY>
</HTML>
 