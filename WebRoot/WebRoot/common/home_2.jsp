<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.inform.model.Performance"%>
<%@page import="com.afunms.inform.dao.PerformanceDao"%>
<%@page import="com.afunms.inform.model.Alarm"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.event.model.*"%>
<%@page import="com.afunms.inform.dao.AlarmDao"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="java.util.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.CustomXml"%>
<%@page import="com.afunms.topology.dao.CustomXmlDao"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = null;
  Date c1=new Date();
	String timeFormat = "MM-dd HH:mm:ss";
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
	List networklist = (List)session.getAttribute("networklist");
	List hostlist = (List)session.getAttribute("hostlist");
%>
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

<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/xml/flush/amcolumn/FusionCharts.js"></script>



<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.3.2.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.2.6.js"></script>
<script language="JavaScript" src="<%=rootPath%>/resource/js/JSClass/FusionCharts.js"></script>
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
// 获取Flex参数
function getFlexParam(param)
{

}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" >
	<tr>
		<td width="200" valign=top align=center>               		           				
<%=menuTable %>
		
		</td>
		<td width="2" valign=top align=center bgcolor="#397dbd">&nbsp;</td>
		<td bgcolor="#ffffff" valign="top"  align="center" width=1100>
			<table cellpadding="0" cellspacing="0" width=100% bgcolor=#ffffff align='center'>
				<tr>
					<td width=2></td>
					<td align=left width=100%>
						<table cellpadding="0" cellspacing="0" width="100%" class="tborder" align='left'>
							<tr style="background-color: #ECECEC;"><td colspan="8" align="center" height='28'><b>系统快照</b></td></tr>
							<tr>
  								<td align="center">
   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getRouterStatus(),1)%>" ><br>
   									<a href="<%=rootPath%>/network.do?action=monitornodelist&jp=1">路由器</a>
   								</td>
  								<td align="center">
   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getSwitchStatus(),2)%>" ><br>
   									<a href="<%=rootPath%>/network.do?action=monitorswitchlist&jp=1">交换机</a>
   								</td>   
  								<td align="center">
   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServerStatus(),3)%>"><br>
   									<a href="<%=rootPath%>/network.do?action=monitorhostlist&jp=1">服务器</a>
   								</td>
   								<td align="center">
   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),4)%>"><br>
   									<a href="<%=rootPath%>/db.do?action=list&jp=1">数据库</a>
   								</td>
  								<td align="center">
  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getAppStatus())%>"><br>
  									<a href="#">中间件</a>
  								</td>
  								<td align="center">
  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),6)%>" ><br>
  									<a href="#">服务</a>
  								</td>
  								<td align="center">
  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),7)%>"><br>
  									<a href="#">安全</a>
  								</td>
  								<td align="center">
  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),8)%>"><br>
  									<a href="#">环境</a>
  								</td>
							</tr>		
						</table>
					</td>

					<!--<td align=right>
<table cellpadding="0" cellspacing="0" width=95% class="tborder" align='right'>
<tr style="background-color: #ECECEC;">
<td  align="center" height='28'><b>自定义视图快照</b></td></tr>
<%
	CustomXmlDao cdao = new CustomXmlDao();
	List clist = cdao.loadAll();
	cdao.close();
	for(int i=0; i<clist.size(); i++)
	{
		if(i>1)break;
		CustomXml vo = (CustomXml)clist.get(i);
		//out.print("<option value='" + vo.getXmlName() + "'>" + vo.getViewName()+ "</option>");
		if(i==0){
%>
<tr>
  <td align="left">
   	&nbsp;&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/topology/view/custom.jsp","fullScreenWindow", "toolbar=no,height="+ window.screen.height + ","+"width=" + (window.screen.width-8) + ",scrollbars=no"+"screenX=0,screenY=0")'><%=vo.getViewName()%></a>
   </td>

</tr>
<%
}else{
%>
<tr>
  <td align="left" >
   	&nbsp;&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/topology/view/custom.jsp","fullScreenWindow", "toolbar=no,height="+ window.screen.height + ","+"width=" + (window.screen.width-8) + ",scrollbars=no"+"screenX=0,screenY=0")'><%=vo.getViewName()%></a>
   </td>

</tr>
<%
}
}
%>		
</table>
</td>
-->
<td width=2></td>
</tr>
</table>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
          	<tr>
          	<td width=30%>
          	
            		<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
              			<tr style="background-color: #ECECEC;">
              				<td align="center" height='28'>
              					<b>被监视对象汇总图</b>
              				</td>
              			</tr>
              			<tr> 
                			<td> 
                				<div id="flashcontent1">
							<strong>You need to upgrade your Flash Player</strong>
						</div>
      <script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Pie2D.swf", "ChartId", "300", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/Pie2D.xml");		   
		   chart.render("flashcontent1");
		</script> 			
			
                			</td>
              			</tr>
            		</table>
            	</td>
            	<td width=70% valign=top colspan=2 id="device_list">
            
            		<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" style="border:1px solid #0000FF;">
                    		<tr align="center" style="background-color:#ECECEC;" height=28> 
                      			<td width="7%" word-wrap:break-word;word-break: normal;> 
                        			<div align="center"><b>&nbsp;等级</b></div>
                        		</td>  
                      			<td width="18%" word-wrap:break-word;word-break: normal;> 
                        			<div align="center"><b>&nbsp;来源</b></div>
                        		</td>                                           
                      			<td width="60%" style="word-wrap:break-word"> 
                        			<b>&nbsp;事件描述</b>
                        		</td>

                      			<td width="15%" word-wrap:break-word;word-break: normal;> 
                        			<div align="center"><b>&nbsp;时间</b></div>
                        		</td>
                    		</tr>
  		 	<%
  			  int index3 = 0;
  			List _rpceventlist = (List)session.getAttribute("rpceventlist");
  			if(_rpceventlist != null && _rpceventlist.size()>0){
  			for(int i=0;i<_rpceventlist.size();i++){
  			String eventsrc = "";
  			  index3 ++;
  			  if(index3 == 11)break;	 
  			  EventList e2 = (EventList)_rpceventlist.get(i); 			  
  			  if(e2.getManagesign() != 0)continue;
  			  
  		if(e2.getSubtype().equalsIgnoreCase("network") || e2.getSubtype().equalsIgnoreCase("host")){
  			HostNode node = new HostNode();
			HostNodeDao _dao = new HostNodeDao();
			node = _dao.loadHost(e2.getNodeid()); 
			_dao.close();
			eventsrc = node.getAlias()+"("+node.getIpAddress()+")";
  		}else{
  			eventsrc = e2.getEventlocation();
  		}  			  
  			  
  			  Date d2 = e2.getRecordtime().getTime();
  			  String time2 = timeFormatter.format(d2);
  			String l1 = String.valueOf(e2.getLevel1());
  			  if("1".equals(l1)){
  			  	l1="普通事件";
  			  }
  			  if("2".equals(l1)){
  			  	l1="紧急事件";
  			  }
  			  if("3".equals(l1)){
  			  	l1="严重事件";
  			  }
  			%>
                    		<tr bgcolor="deebf7">
                      			<td align=center><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(e2.getLevel1())%>" alt="<%=e2.getContent()%>" border="0"/></td>
                      			<td><%=eventsrc%>&nbsp;</td>
                      			<td style="word-wrap:break-word"><%=e2.getContent()%></td>
                      			<td style="word-wrap:break-word"><%=time2%></td>
                    		</tr>
                    <%
                    		}
                    	}
                    %>
                    		<tr align="right" bgcolor="deebf7"> 
                      			<td colspan="5" align="right" nowrap>&nbsp;<a href="<%=rootPath%>/event.do?action=list&jp=1"><font color="397dbd"><em>>>查看更多扫描事件...</em></font></a>&nbsp;</td>
                    		</tr>                   
                  	</table> 
          	</td>
          	</tr> 
          	<% 
          		if(networklist != null && networklist.size()>0){
          	%>
           	<tr>
          	<td width=30%>
            		<table width="100%" border="0" cellpadding="0" cellspacing="0">
              			<tr> 
                			<td align=left> 
 
		 				<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
		 					<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>网络设备CPU利用率TOP10</b>
              							</td>
              						</tr>
              						<tr> 
                						<td width="100%" > 
                							<div id="flashcontent2">
										<strong>You need to upgrade your Flash Player</strong>
									</div>
	<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "350", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data.xml");
		   chart.render("flashcontent2");
	</script>				
                </td>
              </tr>              
            </table> 			
			
                </td>
              </tr>
            </table>
            </td>
            <td width=30%>
            
            					<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
            						<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>网络设备入口流速TOP10</b>
              							</td>
              						</tr>
              <tr> 
                <td width="100%" > 
                	<div id="flashcontent4">
				<strong>You need to upgrade your Flash Player</strong>
			</div>
	<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "350", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data1.xml");
		   chart.render("flashcontent4");
	</script>				
                </td>
              </tr>             
            </table> 
            
                       
          	</td>
          	
          	<td width=40%>
            
            					<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
            						<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>网络设备出口流速TOP10</b>
              							</td>
              						</tr>
              <tr> 
                <td width="100%" > 
                	<div id="flashcontent5">
				<strong>You need to upgrade your Flash Player</strong>
			</div>
	<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "350", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data2.xml");
		   chart.render("flashcontent5");
	</script>				
                </td>
              </tr>             
            </table> 
            
                       
          	</td>
          	
          	
          	
          	</tr>
 		<%
 			}
 			if(hostlist != null && hostlist.size()>0){
 		%>
 		<tr>
          	<td width=30%>
            		<table width="100%" border="0" cellpadding="0" cellspacing="0">
              			<tr> 
                			<td align=left> 
 
		 				<table cellpadding="0" cellspacing="0" width=95% class="tborder" align='left'>
		 					<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>服务器CPU利用率TOP10</b>
              							</td>
              						</tr>
              						<tr> 
                						<td width="100%" >
                						<div id="chartdiv" align="center"> FusionCharts. </div>
      		<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "400", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data3.xml");
		   chart.render("chartdiv");
		</script>				
                </td>
              </tr>              
            </table> 			
			
                </td>
              </tr>
            </table>
            </td>
            <td width=30%>
            
            					<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
            						<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>服务器内存利用率TOP10</b>
              							</td>
              						</tr>
              <tr> 
                <td width="100%" > 
                	<div id="flashcontent7">
				<strong>You need to upgrade your Flash Player</strong>
			</div>
      		<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "400", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data4.xml");
		   chart.render("flashcontent7");
		</script>				
                </td>
              </tr>             
            </table> 
            
                       
          	</td>
          	
          	<td width=40%>
            
            					<table cellpadding="0" cellspacing="0" width=100% class="tborder" align='left'>
            						<tr style="background-color: #ECECEC;">
              							<td align="center" height='28'>
              								<b>服务器磁盘利用率TOP10</b>
              							</td>
              						</tr>
              <tr> 
                <td width="100%" > 
                	<div id="flashcontent8">
				<strong>You need to upgrade your Flash Player</strong>
			</div>
      		<script type="text/javascript">
		   var chart = new FusionCharts("<%=rootPath%>/resource/xml/flush/amcolumn/FCF_Column2D.swf", "ChartId", "400", "300");
		   chart.setDataURL("<%=rootPath%>/resource/xml/flush/amcolumn/amcolumn_data5.xml");
		   chart.render("flashcontent8");
		</script>			
				
                </td>
              </tr>             
            </table> 
            
                       
          	</td>
          	
          	
          	
          	</tr>
 		<%
 			}
 		%>
                          	         	
                 	        	
          </table>            				
				</td>
				<td width="2" valign=top align=center bgcolor="#397dbd">&nbsp;</td>
			</tr>			
		</table>
		</td>
		
	</tr>

</table>
</form>	
</body>
</html>
