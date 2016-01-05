<!--全局变量函数-->
<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.polling.loader.HostLoader"%>
<%
String tmp1 = (String)request.getAttribute("nodeid"); 
String flag = (String) request.getAttribute("flag"); 
if(flag==null){flag="0";}
Host hostDetail = null;
if(tmp1 !=null){
    HostLoader hostLoader = new HostLoader();
	hostDetail = hostLoader.loadOneByID(tmp1);
} 

String[][] hostDetailTitleFlag= {
							{"性能信息" ,"/windowsData.do?action=getPerformanceInfo&flag="+flag , "0"} ,
							{"流速信息" ,"/windowsData.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"进程信息","/windowsData.do?action=getProcessInfo&flag="+flag , "2" } ,
							{"ARP信息","/windowsData.do?action=getArpInfo&flag="+flag , "3" } ,
							{"软件信息","/windowsData.do?action=getSoftwareInfo&flag="+flag , "4" } , 
            				{"服务信息","/windowsData.do?action=getServiceInfo&flag="+flag , "5" } ,
							//{"SYSLOG信息" ,"/windowsData.do?action=getSyslogInfo&flag="+flag ,"6"},
							{"设备信息" ,"/windowsData.do?action=getDeviceInfo&flag="+flag ,"7"},
							{"存储信息" ,"/windowsData.do?action=getStorageInfo&flag="+flag ,"8"},
							{"告警信息" ,"/windowsData.do?action=getEventInfo&flag="+flag,"9" }
							};
String[][] hostDetailTitleFlag1 = {
							//{"流速信息" ,"/monitor.do?action=hostping" , "0" }, 
							{"性能信息" ,"/monitor.do?action=hostcpu&flag="+flag , "1"} , 
							//{"进程信息","/monitor.do?action=hostproc" , "2" } , 
							//{"SYSLOG信息" ,"/monitor.do?action=hostsyslog" ,"3"},
							{"告警信息" ,"/monitor.do?action=hostevent&flag="+flag,"9" }
							};
String[][] hostDetailTitle = null;
if(hostDetail != null && hostDetail.getCollecttype()!=3 && hostDetail.getCollecttype()!=4 && hostDetail.getCollecttype()!=8 && hostDetail.getCollecttype()!=9){
	hostDetailTitle = hostDetailTitleFlag;
}else{
	hostDetailTitle = hostDetailTitleFlag1;
}
StringBuffer hostDetailTitleSB = new StringBuffer();

hostDetailTitleSB.append("<table>");
hostDetailTitleSB.append("<tr>");
hostDetailTitleSB.append("<td width=\""+hostDetailTitle.length*72+"\">");
hostDetailTitleSB.append("<table>");
hostDetailTitleSB.append("<tr>");

for(int i = 0 ; i < hostDetailTitle.length ; i++){
	hostDetailTitleSB.append("<td id=\"hostDetailTitle-"+ hostDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + hostDetailTitle[i][1] + "\")'>" + hostDetailTitle[i][0]+ "</a></td>" );
}
hostDetailTitleSB.append("</tr>");
hostDetailTitleSB.append("</table>");
hostDetailTitleSB.append("</td>");
hostDetailTitleSB.append("</tr>");
hostDetailTitleSB.append("</table>");

String hostDetailTitleTable = hostDetailTitleSB.toString();



String[][] webDetailTitleFlag= {
							{"连通性" ,"/web.do?action=liantong" , "0" }, 
							{"页面信息" ,"/web.do?action=page" , "1"} , 
							{"告警信息","/web.do?action=alarm" , "2" }
							};
String[][] webDetailTitle = webDetailTitleFlag;
StringBuffer webDetailTitleSB = new StringBuffer();
webDetailTitleSB.append("<table>");
webDetailTitleSB.append("<tr>");
webDetailTitleSB.append("<td width=\""+webDetailTitle.length*72+"\">");
webDetailTitleSB.append("<table>");
webDetailTitleSB.append("<tr>");
for(int i = 0 ; i < webDetailTitle.length ; i++){
	webDetailTitleSB.append("<td id=\"webDetailTitle-"+ webDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + webDetailTitle[i][1] + "\")'>" + webDetailTitle[i][0]+ "</a></td>" );
}
webDetailTitleSB.append("</tr>");
webDetailTitleSB.append("</table>");
webDetailTitleSB.append("</td>");
webDetailTitleSB.append("</tr>");
webDetailTitleSB.append("</table>");
String webDetailTitleTable = webDetailTitleSB.toString();

String[][] urlDetailTitleFlag= {
                            {"连通性" ,"/urlData.do?action=getPerformanceInfo" , "0" }, 
                            {"页面信息" ,"/urlData.do?action=getPageInfo" , "1"} , 
                            {"告警信息","/urlData.do?action=getEventInfo" , "2" }
                            };
String[][] urlDetailTitle = urlDetailTitleFlag;
StringBuffer urlDetailTitleSB = new StringBuffer();
urlDetailTitleSB.append("<table>");
urlDetailTitleSB.append("<tr>");
urlDetailTitleSB.append("<td width=\""+urlDetailTitle.length*72+"\">");
urlDetailTitleSB.append("<table>");
urlDetailTitleSB.append("<tr>");
for(int i = 0 ; i < urlDetailTitle.length ; i++){
    urlDetailTitleSB.append("<td id=\"urlDetailTitle-"+ urlDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + urlDetailTitle[i][1] + "\")'>" + urlDetailTitle[i][0]+ "</a></td>" );
}
urlDetailTitleSB.append("</tr>");
urlDetailTitleSB.append("</table>");
urlDetailTitleSB.append("</td>");
urlDetailTitleSB.append("</tr>");
urlDetailTitleSB.append("</table>");
String urlDetailTitleTable = urlDetailTitleSB.toString();
//portService
String[][] psDetailTitleFlag= {
							{"连通性" ,"/pstype.do?action=detail" , "0" }, 
							{"告警" ,"/pstype.do?action=alarm" , "1"},
							};
String[][] psDetailTitle = psDetailTitleFlag;
StringBuffer psDetailTitleSB = new StringBuffer();
psDetailTitleSB.append("<table>");
psDetailTitleSB.append("<tr>");
psDetailTitleSB.append("<td width=\""+psDetailTitle.length*72+"\">");
psDetailTitleSB.append("<table>");
psDetailTitleSB.append("<tr>");
for(int i = 0 ; i < psDetailTitle.length ; i++){
	psDetailTitleSB.append("<td id=\"psDetailTitle-"+ psDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + psDetailTitle[i][1] + "\")'>" + psDetailTitle[i][0]+ "</a></td>" );
}
psDetailTitleSB.append("</tr>");
psDetailTitleSB.append("</table>");
psDetailTitleSB.append("</td>");
psDetailTitleSB.append("</tr>");
psDetailTitleSB.append("</table>");
String psDetailTitleTable = psDetailTitleSB.toString();

//tomcat
String[][] tomDetailTitleFlag= {
							{"性能信息", "/tomcatData.do?action=getPerformanceInfo", "0" }, 
							{"告警信息", "/tomcatData.do?action=getEventInfo", "1"}
							};
String[][] tomDetailTitle = tomDetailTitleFlag;
StringBuffer tomDetailTitleSB = new StringBuffer();
tomDetailTitleSB.append("<table>");
tomDetailTitleSB.append("<tr>");
tomDetailTitleSB.append("<td width=\""+tomDetailTitle.length*72+"\">");
tomDetailTitleSB.append("<table>");
tomDetailTitleSB.append("<tr>");
for(int i = 0 ; i < tomDetailTitle.length ; i++){
	tomDetailTitleSB.append("<td id=\"tomDetailTitle-"+ tomDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + tomDetailTitle[i][1] + "\")'>" + tomDetailTitle[i][0]+ "</a></td>" );
}
tomDetailTitleSB.append("</tr>");
tomDetailTitleSB.append("</table>");
tomDetailTitleSB.append("</td>");
tomDetailTitleSB.append("</tr>");
tomDetailTitleSB.append("</table>");
String tomDetailTitleTable = tomDetailTitleSB.toString();


//PTimeDB fsgdj
String[][] ptimedbDetailTitleFlag= {
							{"性能信息", "/ptimedbData.do?action=getPerformanceInfo", "0" }, 
							{"告警信息", "/ptimedbData.do?action=getEventInfo", "1"}
							};
String[][] ptimedbDetailTitle = ptimedbDetailTitleFlag;
StringBuffer ptimedbDetailTitleSB = new StringBuffer();
ptimedbDetailTitleSB.append("<table>");
ptimedbDetailTitleSB.append("<tr>");
ptimedbDetailTitleSB.append("<td width=\""+ptimedbDetailTitle.length*72+"\">");
ptimedbDetailTitleSB.append("<table>");
ptimedbDetailTitleSB.append("<tr>");
for(int i = 0 ; i < ptimedbDetailTitle.length ; i++){
	ptimedbDetailTitleSB.append("<td id=\"ptimedbDetailTitle-"+ ptimedbDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + ptimedbDetailTitle[i][1] + "\")'>" + ptimedbDetailTitle[i][0]+ "</a></td>" );
}
ptimedbDetailTitleSB.append("</tr>");
ptimedbDetailTitleSB.append("</table>");
ptimedbDetailTitleSB.append("</td>");
ptimedbDetailTitleSB.append("</tr>");
ptimedbDetailTitleSB.append("</table>");
String ptimedbDetailTitleTable = ptimedbDetailTitleSB.toString();

//pix firewall
String[][] pixDetailTitleFlag= {
							{"性能信息", "/pixData.do?action=getPerformanceInfo", "0" }, 
							{"告警信息", "/pixData.do?action=getEventInfo", "1"}
							};
String[][] pixDetailTitle = pixDetailTitleFlag;
StringBuffer pixDetailTitleSB = new StringBuffer();
pixDetailTitleSB.append("<table>");
pixDetailTitleSB.append("<tr>");
pixDetailTitleSB.append("<td width=\""+pixDetailTitle.length*72+"\">");
pixDetailTitleSB.append("<table>");
pixDetailTitleSB.append("<tr>");
for(int i = 0 ; i < pixDetailTitle.length ; i++){
	pixDetailTitleSB.append("<td id=\"pixDetailTitle-"+ pixDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + pixDetailTitle[i][1] + "\")'>" + pixDetailTitle[i][0]+ "</a></td>" );
}
pixDetailTitleSB.append("</tr>");
pixDetailTitleSB.append("</table>");
pixDetailTitleSB.append("</td>");
pixDetailTitleSB.append("</tr>");
pixDetailTitleSB.append("</table>");
String pixDetailTitleTable = pixDetailTitleSB.toString();


String[][] emailDetailTitleFlag= {
							{"连通性" ,"/mail.do?action=detail" , "0" }, 
							{"告警" ,"/mail.do?action=alarm" , "1"}
							};
String[][] emailDetailTitle = emailDetailTitleFlag;
StringBuffer emailDetailTitleSB = new StringBuffer();
emailDetailTitleSB.append("<table>");
emailDetailTitleSB.append("<tr>");
emailDetailTitleSB.append("<td width=\""+emailDetailTitle.length*72+"\">");
emailDetailTitleSB.append("<table>");
emailDetailTitleSB.append("<tr>");
for(int i = 0 ; i < emailDetailTitle.length ; i++){
	emailDetailTitleSB.append("<td id=\"emailDetailTitle-"+ emailDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + emailDetailTitle[i][1] + "\")'>" + emailDetailTitle[i][0]+ "</a></td>" );
}
emailDetailTitleSB.append("</tr>");
emailDetailTitleSB.append("</table>");
emailDetailTitleSB.append("</td>");
emailDetailTitleSB.append("</tr>");
emailDetailTitleSB.append("</table>");
String emailDetailTitleTable = emailDetailTitleSB.toString();


String[][] ftpDetailTitleFlag= {
							{"连通性" ,"/FTP.do?action=detail" , "0" }, 
							{"告警" ,"/FTP.do?action=alarm" , "1"}
							};
String[][] ftpDetailTitle = ftpDetailTitleFlag;
StringBuffer ftpDetailTitleSB = new StringBuffer();
ftpDetailTitleSB.append("<table>");
ftpDetailTitleSB.append("<tr>");
ftpDetailTitleSB.append("<td width=\""+ftpDetailTitle.length*72+"\">");
ftpDetailTitleSB.append("<table>");
ftpDetailTitleSB.append("<tr>");
for(int i = 0 ; i < ftpDetailTitle.length ; i++){
	ftpDetailTitleSB.append("<td id=\"ftpDetailTitle-"+ ftpDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + ftpDetailTitle[i][1] + "\")'>" + ftpDetailTitle[i][0]+ "</a></td>" );
}
ftpDetailTitleSB.append("</tr>");
ftpDetailTitleSB.append("</table>");
ftpDetailTitleSB.append("</td>");
ftpDetailTitleSB.append("</tr>");
ftpDetailTitleSB.append("</table>");
String ftpDetailTitleTable = ftpDetailTitleSB.toString();


String[][] netDetailTitleFlag= {
							{"流速信息", "/netData.do?action=getInterfaceInfo&flag="+flag , "0" }, 
							{"性能信息", "/netData.do?action=getPerformanceInfo&flag="+flag , "1"} , 
							{"ARP信息", "/netData.do?action=getARPInfo&flag="+flag , "2" } , 
							//{"FDB信息" ,"/netData.do?action=getFDBInfo&flag="+flag ,"3"},
							{"路由表" ,"/netData.do?action=getRouterInfo&flag="+flag,"4" },
							{"IP列表" ,"/netData.do?action=getIpListInfo&flag="+flag,"5" },
							//{"动力环境" ,"/netData.do?action=getPowerEnvironmentInfo&flag="+flag,"6" },
							{"告警信息" ,"/netData.do?action=getEventInfo&flag="+flag,"7" },
							};
String[][] netDetailTitleFlag1 = {
							//{"流速信息" ,"/topology/network/networkview.jsp" , "0" }, 
							{"性能信息" ,"/monitor.do?action=netcpu&flag="+flag , "1"} , 
							//{"ARP信息","//monitor.do?action=netarp" , "2" } , 
							//{"FDB信息" ,"/monitor.do?action=netfdb" ,"3"},
							//{"路由表" ,"/monitor.do?action=netroute","4" },
							//{"IP列表" ,"/monitor.do?action=netiplist","5" },
							{"告警信息" ,"/monitor.do?action=netevent&flag="+flag,"7" }
							};
String[][] netDetailTitle = null;
if(hostDetail != null && hostDetail.getCollecttype()!=3 && hostDetail.getCollecttype()!=4 && hostDetail.getCollecttype()!=8 && hostDetail.getCollecttype()!=9){
	netDetailTitle = netDetailTitleFlag;
}else{
	netDetailTitle = netDetailTitleFlag1;
}
StringBuffer netDetailTitleSB = new StringBuffer();

netDetailTitleSB.append("<table>");
netDetailTitleSB.append("<tr>");
netDetailTitleSB.append("<td width=\""+netDetailTitle.length*72+"\">");
netDetailTitleSB.append("<table>");
netDetailTitleSB.append("<tr>");

for(int i = 0 ; i < netDetailTitle.length ; i++){
	netDetailTitleSB.append("<td id=\"netDetailTitle-"+ netDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + netDetailTitle[i][1] + "\")'>" + netDetailTitle[i][0]+ "</a></td>" );
}
netDetailTitleSB.append("</tr>");
netDetailTitleSB.append("</table>");
netDetailTitleSB.append("</td>");
netDetailTitleSB.append("</tr>");
netDetailTitleSB.append("</table>");

String netDetailTitleTable = netDetailTitleSB.toString();

//防火墙
String[][] firewallDetailTitleFlag= {
							{"流速信息" ,"/topology/network/firewallview.jsp?flag="+flag , "0" }, 
							{"性能信息" ,"/monitor.do?action=firewallcpu&flag="+flag , "1"} , 
							{"ARP信息","//monitor.do?action=firewallarp&flag="+flag , "2" } , 
							{"路由表" ,"/monitor.do?action=firewallroute&flag="+flag,"4" },
							{"IP列表" ,"/monitor.do?action=firewalliplist&flag="+flag,"5" },
							{"告警信息" ,"/monitor.do?action=firewallevent&flag="+flag,"7" },
							};
String[][] firewallDetailTitleFlag1 = {
							//{"流速信息" ,"/topology/network/networkview.jsp" , "0" }, 
							{"性能信息" ,"/monitor.do?action=netcpu&flag="+flag , "1"} , 
							//{"ARP信息","//monitor.do?action=netarp" , "2" } , 
							//{"FDB信息" ,"/monitor.do?action=netfdb" ,"3"},
							//{"路由表" ,"/monitor.do?action=netroute","4" },
							//{"IP列表" ,"/monitor.do?action=netiplist","5" },
							{"告警信息" ,"/monitor.do?action=netevent&flag="+flag,"7" }
							};
String[][] firewallDetailTitle = null;
if(hostDetail != null && hostDetail.getCollecttype()!=3 && hostDetail.getCollecttype()!=4 && hostDetail.getCollecttype()!=8 && hostDetail.getCollecttype()!=9){
	firewallDetailTitle = firewallDetailTitleFlag;
}else{
	firewallDetailTitle = firewallDetailTitleFlag1;
}
StringBuffer firewallDetailTitleSB = new StringBuffer();

firewallDetailTitleSB.append("<table>");
firewallDetailTitleSB.append("<tr>");
firewallDetailTitleSB.append("<td width=\""+firewallDetailTitle.length*72+"\">");
firewallDetailTitleSB.append("<table>");
firewallDetailTitleSB.append("<tr>");

for(int i = 0 ; i < firewallDetailTitle.length ; i++){
	firewallDetailTitleSB.append("<td id=\"firewallDetailTitle-"+ firewallDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + firewallDetailTitle[i][1] + "\")'>" + firewallDetailTitle[i][0]+ "</a></td>" );
}
firewallDetailTitleSB.append("</tr>");
firewallDetailTitleSB.append("</table>");
firewallDetailTitleSB.append("</td>");
firewallDetailTitleSB.append("</tr>");
firewallDetailTitleSB.append("</table>");

String firewallDetailTitleTable = firewallDetailTitleSB.toString(); 
 
// 天融信防火墙
String[][] topsecDetailTitle= {
                            {"流速信息", "/topsecData.do?action=getInterfaceInfo&flag="+flag , "0" }, 
                            {"性能信息", "/topsecData.do?action=getPerformanceInfo&flag="+flag , "1"} , 
                            {"ARP信息", "/topsecData.do?action=getARPInfo&flag="+flag , "2" } , 
                            {"路由表" ,"/topsecData.do?action=getRouterInfo&flag="+flag,"4" },
                            {"IP列表" ,"/topsecData.do?action=getIpListInfo&flag="+flag,"5" },
                            {"告警信息" ,"/topsecData.do?action=getEventInfo&flag="+flag,"7" },
                            };
StringBuffer topsecDetailTitleSB = new StringBuffer();

topsecDetailTitleSB.append("<table>");
topsecDetailTitleSB.append("<tr>");
topsecDetailTitleSB.append("<td width=\""+topsecDetailTitle.length*72+"\">");
topsecDetailTitleSB.append("<table>");
topsecDetailTitleSB.append("<tr>");

for(int i = 0 ; i < topsecDetailTitle.length ; i++){
    topsecDetailTitleSB.append("<td id=\"topsecDetailTitle-"+ topsecDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + topsecDetailTitle[i][1] + "\")'>" + topsecDetailTitle[i][0]+ "</a></td>" );
}
topsecDetailTitleSB.append("</tr>");
topsecDetailTitleSB.append("</table>");
topsecDetailTitleSB.append("</td>");
topsecDetailTitleSB.append("</tr>");
topsecDetailTitleSB.append("</table>");

String topsecDetailTitleTable = topsecDetailTitleSB.toString();

// H3C防火墙
String[][] h3cfirewallDetailTitle= {
                            {"流速信息", "/h3cfirewallData.do?action=getInterfaceInfo&flag="+flag , "0" }, 
                            {"性能信息", "/h3cfirewallData.do?action=getPerformanceInfo&flag="+flag , "1"} , 
                            {"ARP信息", "/h3cfirewallData.do?action=getARPInfo&flag="+flag , "2" } , 
                            {"路由表" ,"/h3cfirewallData.do?action=getRouterInfo&flag="+flag,"4" },
                            {"IP列表" ,"/h3cfirewallData.do?action=getIpListInfo&flag="+flag,"5" },
                            {"告警信息" ,"/h3cfirewallData.do?action=getEventInfo&flag="+flag,"7" },
                            };
StringBuffer h3cfirewallDetailTitleSB = new StringBuffer();

h3cfirewallDetailTitleSB.append("<table>");
h3cfirewallDetailTitleSB.append("<tr>");
h3cfirewallDetailTitleSB.append("<td width=\""+h3cfirewallDetailTitle.length*72+"\">");
h3cfirewallDetailTitleSB.append("<table>");
h3cfirewallDetailTitleSB.append("<tr>");

for(int i = 0 ; i < topsecDetailTitle.length ; i++){
    h3cfirewallDetailTitleSB.append("<td id=\"h3cfirewallDetailTitle-"+ h3cfirewallDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + h3cfirewallDetailTitle[i][1] + "\")'>" + h3cfirewallDetailTitle[i][0]+ "</a></td>" );
}
h3cfirewallDetailTitleSB.append("</tr>");
h3cfirewallDetailTitleSB.append("</table>");
h3cfirewallDetailTitleSB.append("</td>");
h3cfirewallDetailTitleSB.append("</tr>");
h3cfirewallDetailTitleSB.append("</table>");

String h3cfirewallDetailTitleTable = h3cfirewallDetailTitleSB.toString();



// 启明星辰USG防火墙
String[][] venusDetailTitle= {
                            {"流速信息", "/venusData.do?action=getInterfaceInfo&flag="+flag , "0" }, 
                            {"性能信息", "/venusData.do?action=getPerformanceInfo&flag="+flag , "1"} , 
                            {"ARP信息", "/venusData.do?action=getARPInfo&flag="+flag , "2" } , 
                            //{"路由表" ,"/venusData.do?action=getRouterInfo&flag="+flag,"4" },	//取不到信息
                            {"IP列表" ,"/venusData.do?action=getIpListInfo&flag="+flag,"5" },
                            {"告警信息" ,"/venusData.do?action=getEventInfo&flag="+flag,"7" },
                            };
StringBuffer venusDetailTitleSB = new StringBuffer();

venusDetailTitleSB.append("<table>");
venusDetailTitleSB.append("<tr>");
venusDetailTitleSB.append("<td width=\""+venusDetailTitle.length*72+"\">");
venusDetailTitleSB.append("<table>");
venusDetailTitleSB.append("<tr>");

for(int i = 0 ; i < venusDetailTitle.length ; i++){
    venusDetailTitleSB.append("<td id=\"venusDetailTitle-"+ venusDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + venusDetailTitle[i][1] + "\")'>" + venusDetailTitle[i][0]+ "</a></td>" );
}
venusDetailTitleSB.append("</tr>");
venusDetailTitleSB.append("</table>");
venusDetailTitleSB.append("</td>");
venusDetailTitleSB.append("</tr>");
venusDetailTitleSB.append("</table>");

String venusDetailTitleTable = venusDetailTitleSB.toString();


String[][] oraDetailTitle = {
							{"性能信息" ,"/oracle.do?action=oracleping&urlflag=1&flag="+flag , "0" }, 
							{"锁信息" ,"/oracle.do?action=oraclelock&urlflag=1&flag="+flag , "1"} , 
							{"SESSION信息","/oracle.do?action=oraclesession&urlflag=1&flag="+flag , "2" } , 
							{"回滚段信息" ,"/oracle.do?action=oraclerollback&urlflag=1&flag="+flag ,"3"},
							{"表信息" ,"/oracle.do?action=oracletable&urlflag=1&flag="+flag ,"4"},
							{"表空间信息" ,"/oracle.do?action=oraclespace&urlflag=1&flag="+flag ,"5"},
							{"Top10Sql" ,"/oracle.do?action=oracletopsql&urlflag=1&flag="+flag ,"6"},
							{"用户信息" ,"/oracle.do?action=oracleuser&urlflag=1&flag="+flag ,"7"},
							{"WAIT信息" ,"/oracle.do?action=oraclewait&urlflag=1&flag="+flag ,"8"},
							{"基本信息" ,"/oracle.do?action=oraclebaseinfo&urlflag=1&flag="+flag ,"10"},
							{"ASM信息" ,"/oracle.do?action=oracleasminfo&urlflag=1&flag="+flag ,"11"},
							{"告警信息" ,"/oracle.do?action=oracleevent&urlflag=1&flag="+flag,"9" }
							};
StringBuffer oraDetailTitleSB = new StringBuffer();
 
oraDetailTitleSB.append("<table>");
oraDetailTitleSB.append("<tr>");
oraDetailTitleSB.append("<td width=\""+oraDetailTitle.length*72+"\">");
oraDetailTitleSB.append("<table>");
oraDetailTitleSB.append("<tr>");

for(int i = 0 ; i < oraDetailTitle.length ; i++){
	oraDetailTitleSB.append("<td id=\"oraDetailTitle-"+ oraDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + oraDetailTitle[i][1] + "\")'>" + oraDetailTitle[i][0]+ "</a></td>" );
}
oraDetailTitleSB.append("</tr>");
oraDetailTitleSB.append("</table>");
oraDetailTitleSB.append("</td>");
oraDetailTitleSB.append("</tr>");
oraDetailTitleSB.append("</table>");

String oraDetailTitleTable = oraDetailTitleSB.toString();



String[][] mysqlDetailTitle = {
							{"连通率" ,"/mysql.do?action=mysqlping&flag="+flag , "0" }, 
							{"连接信息" ,"/mysql.do?action=mysqlconnect&flag="+flag , "1"} , 
							{"表信息","/mysql.do?action=mysqltables&flag="+flag , "2" } , 
							{"性能信息" ,"/mysql.do?action=mysqlspace&flag="+flag ,"3"},
							{"状态信息" ,"/mysql.do?action=mysqlstatus&flag="+flag,"4" },
							{"配置信息" ,"/mysql.do?action=mysqlvariables&flag="+flag ,"5"},
							{"告警信息" ,"/mysql.do?action=mysqlevent&flag="+flag ,"6"}
							};
StringBuffer mysqlDetailTitleSB = new StringBuffer();

mysqlDetailTitleSB.append("<table>");
mysqlDetailTitleSB.append("<tr>");
mysqlDetailTitleSB.append("<td width=\""+mysqlDetailTitle.length*72+"\">");
mysqlDetailTitleSB.append("<table>");
mysqlDetailTitleSB.append("<tr>");

for(int i = 0 ; i < mysqlDetailTitle.length ; i++){
	mysqlDetailTitleSB.append("<td id=\"mysqlDetailTitle-"+ mysqlDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + mysqlDetailTitle[i][1] + "\")'>" + mysqlDetailTitle[i][0]+ "</a></td>" );
}
mysqlDetailTitleSB.append("</tr>");
mysqlDetailTitleSB.append("</table>");
mysqlDetailTitleSB.append("</td>");
mysqlDetailTitleSB.append("</tr>");
mysqlDetailTitleSB.append("</table>");

String mysqlDetailTitleTable = mysqlDetailTitleSB.toString();




String[][] sqlDetailTitle = {
							{"性能信息" ,"/sqlserver.do?action=sqlserverping&flag="+flag , "0" }, 
							{"系统信息" ,"/sqlserver.do?action=sqlserversys&flag="+flag , "1"} , 
							{"数据库信息","/sqlserver.do?action=sqlserverdb&flag="+flag , "2" } , 
							{"进程信息" ,"/sqlserver.do?action=sqlserverproc&flag="+flag ,"3"},
							{"锁信息" ,"/sqlserver.do?action=sqlserverlock&flag="+flag,"4" },
							{"WAIT信息" ,"/sqlserver.do?action=sqlserverwait&flag="+flag,"5" },
							{"告警信息" ,"/sqlserver.do?action=sqlserverevent&flag="+flag ,"6"}
							};
StringBuffer sqlDetailTitleSB = new StringBuffer();

sqlDetailTitleSB.append("<table>");
sqlDetailTitleSB.append("<tr>");
sqlDetailTitleSB.append("<td width=\""+sqlDetailTitle.length*72+"\">");
sqlDetailTitleSB.append("<table>");
sqlDetailTitleSB.append("<tr>");

for(int i = 0 ; i < sqlDetailTitle.length ; i++){
	sqlDetailTitleSB.append("<td id=\"sqlDetailTitle-"+ mysqlDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + sqlDetailTitle[i][1] + "\")'>" + sqlDetailTitle[i][0]+ "</a></td>" );
}
sqlDetailTitleSB.append("</tr>");
sqlDetailTitleSB.append("</table>");
sqlDetailTitleSB.append("</td>");
sqlDetailTitleSB.append("</tr>");
sqlDetailTitleSB.append("</table>");

String sqlDetailTitleTable = sqlDetailTitleSB.toString();




String[][] sybDetailTitle = {
							{"性能信息" ,"/sybase.do?action=sybasecap&flag="+flag , "0" },
							{"数据库信息","/sybase.do?action=sybasedb&flag="+flag , "1" } , 
							{"设备信息" ,"/sybase.do?action=sybasedevice&flag="+flag ,"2"},
							{"告警信息" ,"/sybase.do?action=sybaseevent&flag="+flag ,"3"}
							};
StringBuffer sybDetailTitleSB = new StringBuffer();

sybDetailTitleSB.append("<table>");
sybDetailTitleSB.append("<tr>");
sybDetailTitleSB.append("<td width=\""+sybDetailTitle.length*72+"\">");
sybDetailTitleSB.append("<table>");
sybDetailTitleSB.append("<tr>");

for(int i = 0 ; i < sybDetailTitle.length ; i++){
	sybDetailTitleSB.append("<td id=\"sybDetailTitle-"+ sybDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + sybDetailTitle[i][1] + "\")'>" + sybDetailTitle[i][0]+ "</a></td>" );
}
sybDetailTitleSB.append("</tr>");
sybDetailTitleSB.append("</table>");
sybDetailTitleSB.append("</td>");
sybDetailTitleSB.append("</tr>");
sybDetailTitleSB.append("</table>");

String sybDetailTitleTable = sybDetailTitleSB.toString();




String[][] inforDetailTitle = {
							{"性能信息" ,"/informix.do?action=informixcap&flag="+flag , "0" },
							{"日志文件","/informix.do?action=informixdb&flag="+flag , "1" } , 
							{"配置信息" ,"/informix.do?action=informixdevice&flag="+flag ,"2"},
							{"IO信息" ,"/informix.do?action=informixio&flag="+flag ,"3"},
							{"日志信息" ,"/informix.do?action=informixlog&flag="+flag ,"4"},
							{"等待队列" ,"/informix.do?action=informixwaitingthread&flag="+flag ,"6"},
							{"onbar日志" ,"/informix.do?action=informixbaractlog&flag="+flag ,"7"},
							{"告警信息" ,"/informix.do?action=informixevent&flag="+flag ,"5"}
							};
StringBuffer inforDetailTitleSB = new StringBuffer();

inforDetailTitleSB.append("<table>");
inforDetailTitleSB.append("<tr>");
inforDetailTitleSB.append("<td width=\""+inforDetailTitle.length*72+"\">");
inforDetailTitleSB.append("<table>");
inforDetailTitleSB.append("<tr>");

for(int i = 0 ; i < inforDetailTitle.length ; i++){
	inforDetailTitleSB.append("<td id=\"inforDetailTitle-"+ inforDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + inforDetailTitle[i][1] + "\")'>" + inforDetailTitle[i][0]+ "</a></td>" );
}
inforDetailTitleSB.append("</tr>");
inforDetailTitleSB.append("</table>");
inforDetailTitleSB.append("</td>");
inforDetailTitleSB.append("</tr>");
inforDetailTitleSB.append("</table>");

String inforDetailTitleTable = inforDetailTitleSB.toString();




String[][] db2DetailTitle = {
							{"性能信息" ,"/db2.do?action=db2ping&flag="+flag , "0" }, 
							{"连接信息" ,"/db2.do?action=db2conn&flag="+flag , "1" }, 
							{"缓存信息" ,"/db2.do?action=db2pool&flag="+flag , "2"} , 
							{"锁信息" ,"/db2.do?action=db2lock&flag="+flag , "3"} , 
							{"日志信息" ,"/db2.do?action=db2log&flag="+flag , "4"} , 
							{"表性能信息" ,"/db2.do?action=db2table&flag="+flag , "5"} ,
							{"SESSION信息","/db2.do?action=db2session&flag="+flag , "6" } , 
							{"告警信息" ,"/db2.do?action=db2event&flag="+flag ,"7"}
							};
StringBuffer db2DetailTitleSB = new StringBuffer();

db2DetailTitleSB.append("<table>");
db2DetailTitleSB.append("<tr>");
db2DetailTitleSB.append("<td width=\""+db2DetailTitle.length*72+"\">");
db2DetailTitleSB.append("<table>");
db2DetailTitleSB.append("<tr>");

for(int i = 0 ; i < db2DetailTitle.length ; i++){
	db2DetailTitleSB.append("<td id=\"db2DetailTitle-"+ db2DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + db2DetailTitle[i][1] + "\")'>" + db2DetailTitle[i][0]+ "</a></td>" );
}
db2DetailTitleSB.append("</tr>");
db2DetailTitleSB.append("</table>");
db2DetailTitleSB.append("</td>");
db2DetailTitleSB.append("</tr>");
db2DetailTitleSB.append("</table>");

String db2DetailTitleTable = db2DetailTitleSB.toString();




String[][] netReportTitle = {
							{"连通率报表" ,"/netreport.do?action=list" , "0" }, 
							{"事件报表" ,"/netreport.do?action=eventlist" , "1"} , 
							{"综合报表","/netreport.do?action=netmultilist" , "2" } , 
							};
StringBuffer netReportTitleSB = new StringBuffer();

netReportTitleSB.append("<table>");
netReportTitleSB.append("<tr>");
netReportTitleSB.append("<td width=\""+netReportTitle.length*72+"\">");
netReportTitleSB.append("<table>");
netReportTitleSB.append("<tr>");

for(int i = 0 ; i < netReportTitle.length ; i++){
	netReportTitleSB.append("<td id=\"netReportTitle-"+ netReportTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + netReportTitle[i][1] + "\")'>" + netReportTitle[i][0]+ "</a></td>" );
}
netReportTitleSB.append("</tr>");
netReportTitleSB.append("</table>");
netReportTitleSB.append("</td>");
netReportTitleSB.append("</tr>");
netReportTitleSB.append("</table>");

String netReportTitleTable = netReportTitleSB.toString();

String[][] hostReportTitle = {
							{"连通率报表" ,"/hostreport.do?action=list" , "0" }, 
							{"内存报表" ,"/hostreport.do?action=memorylist" , "1"} , 
							{"磁盘报表","/hostreport.do?action=disklist" , "2" } ,
							{"CPU报表","/hostreport.do?action=cpulist" , "3" } ,
							{"事件报表","/hostreport.do?action=eventlist" , "4" } ,
							{"综合报表","/hostreport.do?action=multilist" , "5" } , 
							};
StringBuffer hostReportTitleSB = new StringBuffer();

hostReportTitleSB.append("<table>");
hostReportTitleSB.append("<tr>");
hostReportTitleSB.append("<td width=\""+hostReportTitle.length*72+"\">");
hostReportTitleSB.append("<table>");
hostReportTitleSB.append("<tr>");

for(int i = 0 ; i < hostReportTitle.length ; i++){
	hostReportTitleSB.append("<td id=\"hostReportTitle-"+ hostReportTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + hostReportTitle[i][1] + "\")'>" + hostReportTitle[i][0]+ "</a></td>" );
}
hostReportTitleSB.append("</tr>");
hostReportTitleSB.append("</table>");
hostReportTitleSB.append("</td>");
hostReportTitleSB.append("</tr>");
hostReportTitleSB.append("</table>");

String hostReportTitleTable = hostReportTitleSB.toString();

String[][] dbReportTitle = {
							{"连通率" ,"/dbreport.do?action=list&flag="+flag , "0" }, 
							{"ORACLE" ,"/dbreport.do?action=oraclelist&flag="+flag , "1"} , 
							{"DB2","/dbreport.do?action=db2list&flag="+flag , "2" } ,
							{"SQLSERVER","/dbreport.do?action=sqlserverlist&flag="+flag , "3" } ,
							{"SYBASE","/dbreport.do?action=sybaselist&flag="+flag , "4" } ,
							{"INFORMIX","/dbreport.do?action=informixlist&flag="+flag , "5" } ,
							{"MYSQL","/dbreport.do?action=mysqllist&flag="+flag , "7" } ,
							{"事件报表","/dbreport.do?action=eventlist&flag="+flag , "6" } , 
							};
StringBuffer dbReportTitleSB = new StringBuffer();

dbReportTitleSB.append("<table>");
dbReportTitleSB.append("<tr>");
dbReportTitleSB.append("<td width=\""+dbReportTitle.length*72+"\">");
dbReportTitleSB.append("<table>");
dbReportTitleSB.append("<tr>");

for(int i = 0 ; i < dbReportTitle.length ; i++){
	dbReportTitleSB.append("<td id=\"dbReportTitle-"+ dbReportTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + dbReportTitle[i][1] + "\")'>" + dbReportTitle[i][0]+ "</a></td>" );
}
dbReportTitleSB.append("</tr>");
dbReportTitleSB.append("</table>");
dbReportTitleSB.append("</td>");
dbReportTitleSB.append("</tr>");
dbReportTitleSB.append("</table>");

String dbReportTitleTable = dbReportTitleSB.toString();




String[][] linuxDetailTitle = {
							{"性能信息", "/linuxData.do?action=getPerformanceInfo&flag="+flag , "0"} ,
							{"流速信息", "/linuxData.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"系统信息", "/linuxData.do?action=getFileSystemInfo&flag="+flag , "2" } , 
							{"配置信息", "/linuxData.do?action=getConfigInfo&flag="+flag ,"3"},
							{"进程信息", "/linuxData.do?action=getProcessInfo&flag="+flag ,"4" },
							//{"服务信息", "/linuxData.do?action=getServiceInfo&flag="+flag ,"5" },
							//{"SYSLOG信息", "/linuxData.do?action=getSyslogInfo&flag="+flag ,"6"},
							{"告警信息", "/linuxData.do?action=getEventInfo&flag="+flag,"7" }
							};
StringBuffer linuxDetailTitleSB = new StringBuffer();  

linuxDetailTitleSB.append("<table>");
linuxDetailTitleSB.append("<tr>");
linuxDetailTitleSB.append("<td width=\""+linuxDetailTitle.length*72+"\">");
linuxDetailTitleSB.append("<table>");
linuxDetailTitleSB.append("<tr>");

for(int i = 0 ; i < linuxDetailTitle.length ; i++){
	linuxDetailTitleSB.append("<td id=\"linuxDetailTitle-"+ linuxDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + linuxDetailTitle[i][1] + "\")'>" + linuxDetailTitle[i][0]+ "</a></td>" );
}
linuxDetailTitleSB.append("</tr>");
linuxDetailTitleSB.append("</table>");
linuxDetailTitleSB.append("</td>");
linuxDetailTitleSB.append("</tr>");
linuxDetailTitleSB.append("</table>");

String linuxDetailTitleTable = linuxDetailTitleSB.toString();




String[][] aixDetailTitle = {
							{"性能信息","/aixData.do?action=getPerformanceInfo&flag="+flag , "0" } ,
							//{"流速信息" ,"/aixData.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"系统信息" ,"/aixData.do?action=getFileSystemInfo&flag="+flag , "2"} ,
							{"配置信息" ,"/aixData.do?action=getConfigInfo&flag="+flag ,"3"},
							{"进程信息" ,"/aixData.do?action=getProcessInfo&flag="+flag ,"4" },
							{"服务信息" ,"/aixData.do?action=getServiceInfo&flag="+flag ,"5" },
							{"SYSLOG信息" ,"/aixData.do?action=getSyslogInfo&flag="+flag ,"6"},
							//{"errpt信息" ,"/monitor.do?action=hosterrpt&flag="+flag ,"7"},
							//{"路由信息" ,"/monitor.do?action=hostroute&flag="+flag ,"8"},
							{"告警信息" ,"/aixData.do?action=getEventInfo&flag="+flag,"9" }
							};
StringBuffer aixDetailTitleSB = new StringBuffer();

aixDetailTitleSB.append("<table>");
aixDetailTitleSB.append("<tr>");
aixDetailTitleSB.append("<td width=\""+aixDetailTitle.length*72+"\">");
aixDetailTitleSB.append("<table>");
aixDetailTitleSB.append("<tr>");

for(int i = 0 ; i < aixDetailTitle.length ; i++){
	aixDetailTitleSB.append("<td id=\"aixDetailTitle-"+ aixDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + aixDetailTitle[i][1] + "\")'>" + aixDetailTitle[i][0]+ "</a></td>" );
}
aixDetailTitleSB.append("</tr>");
aixDetailTitleSB.append("</table>");
aixDetailTitleSB.append("</td>");
aixDetailTitleSB.append("</tr>");
aixDetailTitleSB.append("</table>");

String aixDetailTitleTable = aixDetailTitleSB.toString();



String[][] solarisDetailTitle = {  
							{"性能信息" ,"/solarisData.do?action=getPerformanceInfo&flag="+flag , "0"} ,
							{"流速信息" ,"/solarisData.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"系统信息","/solarisData.do?action=getFileSystemInfo&flag="+flag , "2" } , 
							{"配置信息" ,"/solarisData.do?action=getConfigInfo&flag="+flag ,"3"},
							{"进程信息" ,"/solarisData.do?action=getProcessInfo&flag="+flag ,"4" },
							{"SYSLOG信息" ,"/solarisData.do?action=getSyslogInfo&flag="+flag ,"5"},
							{"告警信息" ,"/solarisData.do?action=getEventInfo&flag="+flag,"6" }
							};
StringBuffer solarisDetailTitleSB = new StringBuffer();

solarisDetailTitleSB.append("<table>");
solarisDetailTitleSB.append("<tr>");
solarisDetailTitleSB.append("<td width=\""+solarisDetailTitle.length*72+"\">");
solarisDetailTitleSB.append("<table>");
solarisDetailTitleSB.append("<tr>");

for(int i = 0 ; i < solarisDetailTitle.length ; i++){
	solarisDetailTitleSB.append("<td id=\"solarisDetailTitle-"+ solarisDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + solarisDetailTitle[i][1] + "\")'>" + solarisDetailTitle[i][0]+ "</a></td>" );
}
solarisDetailTitleSB.append("</tr>");
solarisDetailTitleSB.append("</table>");
solarisDetailTitleSB.append("</td>");
solarisDetailTitleSB.append("</tr>");
solarisDetailTitleSB.append("</table>");

String solarisDetailTitleTable = solarisDetailTitleSB.toString();


// hpunix
String[][] hpunixDetailTitle = {
							{"性能信息","/hpunixData.do?action=getPerformanceInfo&flag="+flag , "0" } ,
							{"流速信息" ,"/hpunixData.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"系统信息" ,"/hpunixData.do?action=getFileSystemInfo&flag="+flag , "2"} ,
							{"配置信息" ,"/hpunixData.do?action=getConfigInfo&flag="+flag ,"3"},
							{"进程信息" ,"/hpunixData.do?action=getProcessInfo&flag="+flag ,"4" },
							//{"服务信息" ,"/hpunixData.do?action=getServiceInfo&flag="+flag ,"5" },
							//{"SYSLOG信息" ,"/hpunixData.do?action=getSyslogInfo&flag="+flag ,"6"},
							//{"errpt信息" ,"/monitor.do?action=hosterrpt&flag="+flag ,"7"},
							//{"路由信息" ,"/monitor.do?action=hostroute&flag="+flag ,"8"},
							{"告警信息" ,"/hpunixData.do?action=getEventInfo&flag="+flag,"9" }
							};
StringBuffer hpunixDetailTitleSB = new StringBuffer();

hpunixDetailTitleSB.append("<table>");
hpunixDetailTitleSB.append("<tr>");
hpunixDetailTitleSB.append("<td width=\""+hpunixDetailTitle.length*72+"\">");
hpunixDetailTitleSB.append("<table>");
hpunixDetailTitleSB.append("<tr>");

for(int i = 0 ; i < hpunixDetailTitle.length ; i++){
	hpunixDetailTitleSB.append("<td id=\"hpunixDetailTitle-"+ hpunixDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + hpunixDetailTitle[i][1] + "\")'>" + hpunixDetailTitle[i][0]+ "</a></td>" );
}
hpunixDetailTitleSB.append("</tr>");
hpunixDetailTitleSB.append("</table>");
hpunixDetailTitleSB.append("</td>");
hpunixDetailTitleSB.append("</tr>");
hpunixDetailTitleSB.append("</table>");

String hpunixDetailTitleTable = hpunixDetailTitleSB.toString();

// tru64 wupinlong add
String[][] tru64DetailTitle = {
							{"性能信息","/tru64Data.do?action=getPerformanceInfo&flag="+flag , "0" } ,
							{"流速信息" ,"/tru64Data.do?action=getInterfaceInfo&flag="+flag , "1" }, 
							{"系统信息" ,"/tru64Data.do?action=getFileSystemInfo&flag="+flag , "2"} ,
							{"配置信息" ,"/tru64Data.do?action=getConfigInfo&flag="+flag ,"3"},
							{"进程信息" ,"/tru64Data.do?action=getProcessInfo&flag="+flag ,"4" },
							//{"服务信息" ,"/tru64Data.do?action=getServiceInfo&flag="+flag ,"5" },
							//{"SYSLOG信息" ,"/tru64Data.do?action=getSyslogInfo&flag="+flag ,"6"},
							//{"errpt信息" ,"/monitor.do?action=hosterrpt&flag="+flag ,"7"},
							//{"路由信息" ,"/monitor.do?action=hostroute&flag="+flag ,"8"},
							{"告警信息" ,"/tru64Data.do?action=getEventInfo&flag="+flag,"9" }
							};
StringBuffer tru64DetailTitleSB = new StringBuffer();

tru64DetailTitleSB.append("<table>");
tru64DetailTitleSB.append("<tr>");
tru64DetailTitleSB.append("<td width=\""+tru64DetailTitle.length*72+"\">");
tru64DetailTitleSB.append("<table>");
tru64DetailTitleSB.append("<tr>");

for(int i = 0 ; i < tru64DetailTitle.length ; i++){
	tru64DetailTitleSB.append("<td id=\"tru64DetailTitle-"+ tru64DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + tru64DetailTitle[i][1] + "\")'>" + tru64DetailTitle[i][0]+ "</a></td>" );
}
tru64DetailTitleSB.append("</tr>");
tru64DetailTitleSB.append("</table>");
tru64DetailTitleSB.append("</td>");
tru64DetailTitleSB.append("</tr>");
tru64DetailTitleSB.append("</table>");

String tru64DetailTitleTable = tru64DetailTitleSB.toString();


String[][] compareDetailTitle = {
							{"网络设备" ,"/network.do?action=netChoce" , "0" }, 
							{"服务器" ,"/network.do?action=hostChoce" , "1" }
							};
StringBuffer compareDetailTitleSB = new StringBuffer();

compareDetailTitleSB.append("<table>");
compareDetailTitleSB.append("<tr>");
compareDetailTitleSB.append("<td width=\""+compareDetailTitle.length*72+"\">");
compareDetailTitleSB.append("<table>");
compareDetailTitleSB.append("<tr>");

for(int i = 0 ; i < compareDetailTitle.length ; i++){
	compareDetailTitleSB.append("<td id=\"compareDetailTitle-"+ compareDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + compareDetailTitle[i][1] + "\")'>" + compareDetailTitle[i][0]+ "</a></td>" );
}
compareDetailTitleSB.append("</tr>");
compareDetailTitleSB.append("</table>");
compareDetailTitleSB.append("</td>");
compareDetailTitleSB.append("</tr>");
compareDetailTitleSB.append("</table>");

String compareDetailTitleTable =compareDetailTitleSB.toString();

String[][] windowsDetailTitle = {
							{"流速信息" ,"/monitor.do?action=hostping&flag="+flag , "0" }, 
							{"性能信息" ,"/monitor.do?action=hostcpu&flag="+flag , "1"} , 
							{"ARP信息" ,"/monitor.do?action=hostarp&flag="+flag , "2"} , 
							{"进程信息","/monitor.do?action=hostproc&flag="+flag , "3" } , 
							{"服务信息","/monitor.do?action=hostservice&flag="+flag , "4" } ,
							{"配置信息","/monitor.do?action=hostconfig&flag="+flag , "5" } ,
							{"SYSLOG信息" ,"/monitor.do?action=hostsyslog&flag="+flag ,"6"},
							{"告警信息" ,"/monitor.do?action=hostevent&flag="+flag,"7" }
							};
StringBuffer windowDetailTitleSB  = new StringBuffer();

windowDetailTitleSB.append("<table>");
windowDetailTitleSB.append("<tr>");
windowDetailTitleSB.append("<td width=\""+windowsDetailTitle.length*72+"\">");
windowDetailTitleSB.append("<table>");
windowDetailTitleSB.append("<tr>");

for(int i = 0 ; i < windowsDetailTitle.length ; i++){
	windowDetailTitleSB.append("<td id=\"windowsDetailTitle-"+ windowsDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + windowsDetailTitle[i][1] + "\")'>" + windowsDetailTitle[i][0]+ "</a></td>" );
}
windowDetailTitleSB.append("</tr>");
windowDetailTitleSB.append("</table>");
windowDetailTitleSB.append("</td>");
windowDetailTitleSB.append("</tr>");
windowDetailTitleSB.append("</table>");

String windowsDetailTitleTable  = windowDetailTitleSB.toString();


String[][] apacheDetailTitle = {
							{"服务流程" ,"/apache.do?action=server&flag="+flag , "0" }, 
							{"性能状态" ,"/apache.do?action=status&flag="+flag , "1"} , 
							{"进程信息" ,"/apache.do?action=process&flag="+flag , "2"} ,
							{"连通曲线图","/apache.do?action=ping&flag="+flag , "3" } ,
							};
StringBuffer apacheDetailTitleSB = new StringBuffer();

apacheDetailTitleSB.append("<table>");
apacheDetailTitleSB.append("<tr>");
apacheDetailTitleSB.append("<td width=\""+apacheDetailTitle.length*72+"\">");
apacheDetailTitleSB.append("<table>");
apacheDetailTitleSB.append("<tr>");

for(int i = 0 ; i < apacheDetailTitle.length ; i++){
	apacheDetailTitleSB.append("<td id=\"apacheDetailTitle-"+ apacheDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + apacheDetailTitle[i][1] + "\")'>" + apacheDetailTitle[i][0]+ "</a></td>" );
}
apacheDetailTitleSB.append("</tr>");
apacheDetailTitleSB.append("</table>");
apacheDetailTitleSB.append("</td>");
apacheDetailTitleSB.append("</tr>");
apacheDetailTitleSB.append("</table>");

String apacheDetailTitleTable = apacheDetailTitleSB.toString();

String[][] tuxedoDetailTitle = {
							//{"连通率信息"		,  	"/tuxedo.do?action=pingDetail" 		, 	"0" }, 
							{"Server信息" 	, 	"/tuxedo.do?action=serverDetail&flag="+flag 	, 	"1"} , 
							{"Service信息"	,	"/tuxedo.do?action=serviceDetail&flag="+flag 	, 	"2" } , 
							{"Queue信息"		,"/tuxedo.do?action=queueDetail&flag="+flag 	, 	"3" } , 
							{"Client信息" 	,	"/tuxedo.do?action=clientDetail&flag="+flag 	,	"4"},
							};
StringBuffer tuxedoDetailTitleSB = new StringBuffer();

tuxedoDetailTitleSB.append("<table>");
tuxedoDetailTitleSB.append("<tr>");
tuxedoDetailTitleSB.append("<td width=\""+tuxedoDetailTitle.length*72+"\">");
tuxedoDetailTitleSB.append("<table>");
tuxedoDetailTitleSB.append("<tr>");

for(int i = 0 ; i < tuxedoDetailTitle.length ; i++){
	tuxedoDetailTitleSB.append("<td id=\"tuxedoDetailTitle-"+ tuxedoDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + tuxedoDetailTitle[i][1] + "\")'>" + tuxedoDetailTitle[i][0]+ "</a></td>" );
}
tuxedoDetailTitleSB.append("</tr>");
tuxedoDetailTitleSB.append("</table>");
tuxedoDetailTitleSB.append("</td>");
tuxedoDetailTitleSB.append("</tr>");
tuxedoDetailTitleSB.append("</table>");

String tuxedoDetailTitleTable = tuxedoDetailTitleSB.toString();

String[][] weblogicDetailTitle = {
                            {"队列信息" ,"/weblogic.do?action=detail&flag="+flag , "0" },
							{"JDBC连接池" ,"/weblogic.do?action=jdbcdetail&flag="+flag , "1" }, 
							{"Web应用信息" ,"/weblogic.do?action=webappdetail&flag="+flag , "2"} , 
							{"JVM堆信息","/weblogic.do?action=jvmheapdetail&flag="+flag , "3" } , 
							{"服务信息" ,"/weblogic.do?action=serverdetail&flag="+flag ,"4"},
							{"Servlet信息" ,"/weblogic.do?action=servletdetail&flag="+flag ,"5"},
							//{"交易信息" ,"/weblogic.do?action=transdetail&flag="+flag ,"6"},
							{"告警信息" ,"/weblogic.do?action=eventdetail&flag="+flag ,"7"}
							};
StringBuffer weblogicDetailTitleSB = new StringBuffer();

weblogicDetailTitleSB.append("<table>");
weblogicDetailTitleSB.append("<tr>");
weblogicDetailTitleSB.append("<td width=\""+weblogicDetailTitle.length*72+"\">");
weblogicDetailTitleSB.append("<table>");
weblogicDetailTitleSB.append("<tr>");

for(int i = 0 ; i < weblogicDetailTitle.length ; i++){
	weblogicDetailTitleSB.append("<td id=\"weblogicDetailTitle-"+ weblogicDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + weblogicDetailTitle[i][1] + "\")'>" + weblogicDetailTitle[i][0]+ "</a></td>" );
}
weblogicDetailTitleSB.append("</tr>");
weblogicDetailTitleSB.append("</table>");
weblogicDetailTitleSB.append("</td>");
weblogicDetailTitleSB.append("</tr>");
weblogicDetailTitleSB.append("</table>");

String weblogicDetailTitleTable = weblogicDetailTitleSB.toString();

String[][] storageDetailTitle = {
							{"连通率" ,"/storage.do?action=pingDetail&flag="+flag , "8" },
                            				{"矩阵位置" ,"/storage.do?action=lsarraysiteDetail&flag="+flag , "0" },                           				
							{"矩阵" ,"/storage.do?action=lsarrayDetail&flag="+flag , "1" }, 
							{"列" ,"/storage.do?action=lsrankDetail&flag="+flag , "2"} , 
							{"扩展域","/storage.do?action=lsextpoolDetail&flag="+flag , "3" } , 
							{"逻辑卷" ,"/storage.do?action=lsfbvolDetail&flag="+flag,"4"},
							{"卷组" ,"/storage.do?action=lsvolgrpDetail&flag="+flag ,"5"},
							{"I/O 端口" ,"/storage.do?action=lsioportDetail&flag="+flag ,"6"},
							{"连接主机" ,"/storage.do?action=lshostconnectDetail&flag="+flag ,"7"},
							};
StringBuffer storageDetailTitleSB = new StringBuffer();

storageDetailTitleSB.append("<table>");
storageDetailTitleSB.append("<tr>");
storageDetailTitleSB.append("<td width=\""+storageDetailTitle.length*72+"\">");
storageDetailTitleSB.append("<table>");
storageDetailTitleSB.append("<tr>");

for(int i = 0 ; i < storageDetailTitle.length ; i++){
	storageDetailTitleSB.append("<td id=\"storageDetailTitle-"+ storageDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + storageDetailTitle[i][1] + "\")'>" + storageDetailTitle[i][0]+ "</a></td>" );
}
storageDetailTitleSB.append("</tr>");
storageDetailTitleSB.append("</table>");
storageDetailTitleSB.append("</td>");
storageDetailTitleSB.append("</tr>");
storageDetailTitleSB.append("</table>");

String storageDetailTitleTable = storageDetailTitleSB.toString();

//用于中间件报表
String[][] middlewareReportTitle = {
							{"连通率信息"		,  	"/midcapreport.do?action=midlist" 				, 	"0" }, 
							{"Tomcat" 		, 	"/tomcatreport.do?action=tomcatlist" 			, 	"1"} , 
							{"IIS"			,	"/iiscatreport.do?action=iislist" 				, 	"2" } , 
							{"WEBLOGIC"		,	"/weblogiccapreport.do?action=weblogiclist" 	, 	"3" } , 
							{"事件报表" 		,	"/midcapreport.do?action=eventlist" 			,	"4"},
							};
StringBuffer middlewareReportTitleSB = new StringBuffer();

middlewareReportTitleSB.append("<table>");
middlewareReportTitleSB.append("<tr>");
middlewareReportTitleSB.append("<td width=\""+middlewareReportTitle.length*72+"\">");
middlewareReportTitleSB.append("<table>");
middlewareReportTitleSB.append("<tr>");

for(int i = 0 ; i < middlewareReportTitle.length ; i++){
	middlewareReportTitleSB.append("<td id=\"middlewareReportTitle-"+ middlewareReportTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + middlewareReportTitle[i][1] + "\")'>" + middlewareReportTitle[i][0]+ "</a></td>" );
}
middlewareReportTitleSB.append("</tr>");
middlewareReportTitleSB.append("</table>");
middlewareReportTitleSB.append("</td>");
middlewareReportTitleSB.append("</tr>");
middlewareReportTitleSB.append("</table>");

String middlewareReportTitleTable = middlewareReportTitleSB.toString();

String[][] AS400DetailTitle = {
							{"连通率" ,"/monitor.do?action=hostping&flag="+flag , "0" },
                            				{"性能信息" ,"/monitor.do?action=AS400SystemStatusDetail&flag="+flag , "1" },
							{"池信息" ,"/monitor.do?action=AS400PoolDetail&flag="+flag , "2" }, 
							{"磁盘" ,"/monitor.do?action=AS400DiskDetail&flag="+flag , "3"} , 
							{"任务信息","/monitor.do?action=AS400JobsDetail&flag="+flag , "4" } , 
							{"子系统信息","/monitor.do?action=AS400SubsystemDetail&flag="+flag , "5" } ,
							{"告警信息","/monitor.do?action=AS400EventDetail&flag="+flag , "6" } , 
							};
StringBuffer AS400DetailTitleSB = new StringBuffer();

AS400DetailTitleSB.append("<table>");
AS400DetailTitleSB.append("<tr>");
AS400DetailTitleSB.append("<td width=\""+AS400DetailTitle.length*72+"\">");
AS400DetailTitleSB.append("<table>");
AS400DetailTitleSB.append("<tr>");

for(int i = 0 ; i < AS400DetailTitle.length ; i++){
	AS400DetailTitleSB.append("<td id=\"AS400DetailTitle-"+ AS400DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + AS400DetailTitle[i][1] + "\")'>" + AS400DetailTitle[i][0]+ "</a></td>" );
}
AS400DetailTitleSB.append("</tr>");
AS400DetailTitleSB.append("</table>");
AS400DetailTitleSB.append("</td>");
AS400DetailTitleSB.append("</tr>");
AS400DetailTitleSB.append("</table>");

String AS400DetailTitleTable = AS400DetailTitleSB.toString();

String[][] wasDetailTitle = {
                                                        {"JVM信息" ,"/was.do?action=detail&flag="+flag , "0" },
							{"JDBC信息" ,"/was.do?action=jdbcdetail&flag="+flag , "1" }, 
							{"Session信息" ,"/was.do?action=session&flag="+flag , "2"} , 
							{"System信息","/was.do?action=system&flag="+flag , "3" } , 
							{"Cache信息" ,"/was.do?action=cache&flag="+flag ,"4"},
							{"Service信息" ,"/was.do?action=service&flag="+flag ,"5"},
							{"ORB信息" ,"/was.do?action=orb&flag="+flag ,"6"},
							{"告警信息" ,"/was.do?action=event&flag="+flag ,"7"}
							};
StringBuffer wasDetailTitleSB = new StringBuffer();

wasDetailTitleSB.append("<table>");
wasDetailTitleSB.append("<tr>");
wasDetailTitleSB.append("<td width=\""+wasDetailTitle.length*72+"\">");
wasDetailTitleSB.append("<table>");
wasDetailTitleSB.append("<tr>");

for(int i = 0 ; i < wasDetailTitle.length ; i++){
	wasDetailTitleSB.append("<td id=\"wasDetailTitle-"+ wasDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + wasDetailTitle[i][1] + "\")'>" + wasDetailTitle[i][0]+ "</a></td>" );
}
wasDetailTitleSB.append("</tr>");
wasDetailTitleSB.append("</table>");
wasDetailTitleSB.append("</td>");
wasDetailTitleSB.append("</tr>");
wasDetailTitleSB.append("</table>");

String wasDetailTitleTable = wasDetailTitleSB.toString();

String[][] was5DetailTitle = {
                                                        {"System信息" ,"/was.do?action=detail&flag="+flag , "0" },
							{"JDBC信息" ,"/was.do?action=jdbcdetail&flag="+flag , "1" }, 
							{"Session信息" ,"/was.do?action=session&flag="+flag , "2"} , 
							{"JVM信息","/was.do?action=system&flag="+flag , "3" } , 
							{"Cache信息" ,"/was.do?action=cache&flag="+flag ,"4"},
							{"Thread信息" ,"/was.do?action=service&flag="+flag ,"5"},
							{"事务信息" ,"/was.do?action=orb&flag="+flag ,"6"},
							{"告警信息" ,"/was.do?action=event&flag="+flag ,"7"}
							};
StringBuffer was5DetailTitleSB = new StringBuffer();

was5DetailTitleSB.append("<table>");
was5DetailTitleSB.append("<tr>");
was5DetailTitleSB.append("<td width=\""+was5DetailTitle.length*72+"\">");
was5DetailTitleSB.append("<table>");
was5DetailTitleSB.append("<tr>");

for(int i = 0 ; i < was5DetailTitle.length ; i++){
	was5DetailTitleSB.append("<td id=\"was5DetailTitle-"+ was5DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + was5DetailTitle[i][1] + "\")'>" + was5DetailTitle[i][0]+ "</a></td>" );
}
was5DetailTitleSB.append("</tr>");
was5DetailTitleSB.append("</table>");
was5DetailTitleSB.append("</td>");
was5DetailTitleSB.append("</tr>");
was5DetailTitleSB.append("</table>");

String was5DetailTitleTable = was5DetailTitleSB.toString();


String[][] was7DetailTitle = {
                                                        {"System信息" ,"/was.do?action=detail&flag="+flag , "0" },
							{"JDBC信息" ,"/was.do?action=jdbcdetail&flag="+flag , "1" }, 
							{"Session信息" ,"/was.do?action=session&flag="+flag , "2"} , 
							{"JVM信息","/was.do?action=system&flag="+flag , "3" } , 
							{"缓存信息" ,"/was.do?action=cache&flag="+flag ,"4"},
							{"Thread信息" ,"/was.do?action=service&flag="+flag ,"5"},
							{"事务信息" ,"/was.do?action=orb&flag="+flag ,"6"},
							{"告警信息" ,"/was.do?action=event&flag="+flag ,"7"}
							};
StringBuffer was7DetailTitleSB = new StringBuffer();

was7DetailTitleSB.append("<table>");
was7DetailTitleSB.append("<tr>");
was7DetailTitleSB.append("<td width=\""+was7DetailTitle.length*72+"\">");
was7DetailTitleSB.append("<table>");
was7DetailTitleSB.append("<tr>");

for(int i = 0 ; i < was7DetailTitle.length ; i++){
	was7DetailTitleSB.append("<td id=\"was7DetailTitle-"+ was7DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + was7DetailTitle[i][1] + "\")'>" + was7DetailTitle[i][0]+ "</a></td>" );
}
was7DetailTitleSB.append("</tr>");
was7DetailTitleSB.append("</table>");
was7DetailTitleSB.append("</td>");
was7DetailTitleSB.append("</tr>");
was7DetailTitleSB.append("</table>");

String was7DetailTitleTable = was7DetailTitleSB.toString();

String[][] knowledgeDetailTitle = {
								  {"告警信息","myEvent()","0"},
								  {"参考方案","myKnowledge()","1"},
								  {"相关知识","myKnowledgebase()","2"}
								  };
StringBuffer knowledgeDetailTitleSB = new StringBuffer();

knowledgeDetailTitleSB.append("<table>");
knowledgeDetailTitleSB.append("<tr>");
knowledgeDetailTitleSB.append("<td width=\""+knowledgeDetailTitle.length*72+"\">");
knowledgeDetailTitleSB.append("<table>");
knowledgeDetailTitleSB.append("<tr>");

for(int i=0;i<knowledgeDetailTitle.length;i++){
	knowledgeDetailTitleSB.append("<td id=\"knowledgeDetailTitle-"+ knowledgeDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='" + knowledgeDetailTitle[i][1] + "'>" + knowledgeDetailTitle[i][0]+ "</a></td>" );
}
knowledgeDetailTitleSB.append("</tr>");
knowledgeDetailTitleSB.append("</table>");
knowledgeDetailTitleSB.append("</td>");
knowledgeDetailTitleSB.append("</tr>");
knowledgeDetailTitleSB.append("</table>");

String knowledgeDetailTitleTable = knowledgeDetailTitleSB.toString();



String[][] f5DetailTitle = {
                                                        {"流速信息" ,"/topology/network/f5serverview.jsp?flag="+flag , "0" }, 
						 
							{"Pool信息","/monitor.do?action=f5poolinfo&flag="+flag , "1" } , 
							
							{"策略信息" ,"/monitor.do?action=f5rulesinfo&flag="+flag,"2" },
							
							
							
							};
StringBuffer f5DetailTitleSB = new StringBuffer();

f5DetailTitleSB.append("<table>");
f5DetailTitleSB.append("<tr>");
f5DetailTitleSB.append("<td width=\""+f5DetailTitle.length*72+"\">");
f5DetailTitleSB.append("<table>");
f5DetailTitleSB.append("<tr>");

for(int i = 0 ; i < f5DetailTitle.length ; i++){
	f5DetailTitleSB.append("<td id=\"f5DetailTitle-"+ f5DetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + f5DetailTitle[i][1] + "\")'>" + f5DetailTitle[i][0]+ "</a></td>" );
}
f5DetailTitleSB.append("</tr>");
f5DetailTitleSB.append("</table>");
f5DetailTitleSB.append("</td>");
f5DetailTitleSB.append("</tr>");
f5DetailTitleSB.append("</table>");

String f5DetailTitleTable = f5DetailTitleSB.toString();

//domino
String[][] dominoDetailTitle = {
							{"性能信息" ,"/domino.do?action=domcpu&flag="+flag , "0" }, 
							{"邮件" ,"/domino.do?action=dommail&flag="+flag , "1"} , 
							{"LDAP","/domino.do?action=domldap&flag="+flag , "2" } , 
							{"HTTP" ,"/domino.do?action=domhttp&flag="+flag ,"3"},
							{"数据库" ,"/domino.do?action=domdb&flag="+flag,"4" },
							{"Cache" ,"/domino.do?action=domcache&flag="+flag ,"5"},
							{"服务器" ,"/domino.do?action=domserver&flag="+flag ,"6"}
							};
StringBuffer dominoDetailTitleSB = new StringBuffer();

dominoDetailTitleSB.append("<table>");
dominoDetailTitleSB.append("<tr>");
dominoDetailTitleSB.append("<td width=\""+dominoDetailTitle.length*72+"\">");
dominoDetailTitleSB.append("<table>");
dominoDetailTitleSB.append("<tr>");

for(int i = 0 ; i < dominoDetailTitle.length ; i++){
	dominoDetailTitleSB.append("<td id=\"hostDetailTitle-"+ dominoDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + dominoDetailTitle[i][1] + "\")'>" + dominoDetailTitle[i][0]+ "</a></td>" );
}
dominoDetailTitleSB.append("</tr>");
dominoDetailTitleSB.append("</table>");
dominoDetailTitleSB.append("</td>");
dominoDetailTitleSB.append("</tr>");
dominoDetailTitleSB.append("</table>");

String dominoDetailTitleTable = dominoDetailTitleSB.toString();
//配置规则 wxy add
String[][] configRuleTitle = {
							{"策略" ,"/configRule.do?action=strategyList" , "0" }, 
							{"规则组" ,"/configRule.do?action=groupRuleList" , "1"} , 
							{"规则","/configRule.do?action=ruleDetailList" , "2" } , 
							};
StringBuffer configRuleTitleSB = new StringBuffer();

configRuleTitleSB.append("<table>");
configRuleTitleSB.append("<tr>");
configRuleTitleSB.append("<td width=\""+configRuleTitle.length*72+"\">");
configRuleTitleSB.append("<table>");
configRuleTitleSB.append("<tr>");

for(int i = 0 ; i < configRuleTitle.length ; i++){
	configRuleTitleSB.append("<td id=\"configRuleTitle-"+ configRuleTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + configRuleTitle[i][1] + "\")'>" + configRuleTitle[i][0]+ "</a></td>" );
}
configRuleTitleSB.append("</tr>");
configRuleTitleSB.append("</table>");
configRuleTitleSB.append("</td>");
configRuleTitleSB.append("</tr>");
configRuleTitleSB.append("</table>");

String configRuleTitleTable = configRuleTitleSB.toString();

//yangjun add
String[][] scounixDetailTitle = {
							{"性能信息" ,"/monitor.do?action=hostping&flag="+flag , "0"} ,
							{"系统信息","/monitor.do?action=hostcpu&flag="+flag , "1" } , 
							{"进程信息" ,"/monitor.do?action=hostproc&flag="+flag ,"2" },
							{"SYSLOG信息" ,"/monitor.do?action=hostsyslog&flag="+flag ,"3"},
							{"告警信息" ,"/monitor.do?action=hostevent&flag="+flag,"4" }
							};
StringBuffer scounixDetailTitleSB = new StringBuffer();

scounixDetailTitleSB.append("<table>");
scounixDetailTitleSB.append("<tr>");
scounixDetailTitleSB.append("<td width=\""+scounixDetailTitle.length*72+"\">");
scounixDetailTitleSB.append("<table>");
scounixDetailTitleSB.append("<tr>");

for(int i = 0 ; i < scounixDetailTitle.length ; i++){
	scounixDetailTitleSB.append("<td id=\"scounixDetailTitle-"+ scounixDetailTitle[i][2]+ "\" class=\"detail-data-title-out\" onmouseover=\"this.className='detail-data-title-over'\" onmouseout=\"this.className='detail-data-title-out'\"><a href=\"#\" onclick='refer(\"" + scounixDetailTitle[i][1] + "\")'>" + scounixDetailTitle[i][0]+ "</a></td>" );
}
scounixDetailTitleSB.append("</tr>");
scounixDetailTitleSB.append("</table>");
scounixDetailTitleSB.append("</td>");
scounixDetailTitleSB.append("</tr>");
scounixDetailTitleSB.append("</table>");

String scounixDetailTitleTable = scounixDetailTitleSB.toString();
%>