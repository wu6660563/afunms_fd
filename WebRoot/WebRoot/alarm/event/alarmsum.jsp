<%/**Title:�澯����JSP dhcc zcw 2011-3-29 18:39:44***/%>
<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	HashMap datamap=(HashMap)request.getAttribute("datamap");
	String[][] tableData=(String[][])datamap.get("tabledata");
	String piedata=(String)datamap.get("piedata");
	String columndata=(String)datamap.get("columndata");
	String closedAlarmPicFile=(String)datamap.get("closedAlarmPicFile");
	String dayalarmData=(String)datamap.get("dayalarmData");
	String weekalarmData=(String)datamap.get("weekalarmData");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
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
//��Ӳ˵�	
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
<body leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
	<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>              		            		           				
		</td><!--���˵���-->	
		<td align="center" valign=top><%--�Ҳ�������--%>	
				<table width="98%"  cellpadding="0" cellspacing="0" align="center"><%--����һ�� ͷ�����塢β��--%>
					<tr>
						<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
							<table width="100%" cellspacing="0" cellpadding="0">
								  <tr>
									<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									<td class="layout_title"><b> �澯 >> �澯��� >>�澯����</b></td>
									<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
								  </tr>
							  </table>
						</td>
					</tr><%--�Ҳ������� ͷ����--%>
<%------------------------------------�Ҳ������� ����----------------------------------%>					
<tr>
<td bgcolor="#FFFFFF">
<table><%-- --%>
	<tr><td valign=top>
		<table>
		<tr>
		<td align="center" width="45%" >
		  		<table border="1" align="right" bordercolor= "#7599d7" style="border:1px solid #7599d7;border-collapse:collapse;">
		  		<%for(int i=0;i<tableData.length;i++){%>
					<tr>
					<%for(int j=0;j<tableData[i].length;j++){
						if(j==0){%>
							<td width="90" height="25" align=center><%=tableData[i][j]%></td>
						<%}
						else if(i==0&&j==1){//��ͨ%>
							<td width="60" height="25" align=center bgcolor="yellow"><%=tableData[i][j]%></td>
						<%}
						else if(i==0&&j==2){//����%>
							<td width="60" height="25" align=center bgcolor="orange"><%=tableData[i][j]%></td>
						<%}
						else if(i==0&&j==3){//����%>
							<td width="60" height="25" align=center bgcolor="red"><%=tableData[i][j]%></td>
						<%}else{%>
						<td width="60" height="25" align=center><%=tableData[i][j]%></td>
					<%}}%>
					</tr>
				<%}%>
		  	 	</table></td>
		  		<td align="right">
		  		<div id="pie">
					<strong>You need to upgrade your Flash Player</strong>
				</div>
				<script type="text/javascript">
					// <![CDATA[		
					var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","380", "280", "8", "#FFFFFF");
					so.addVariable("path", "<%=rootPath%>/amchart/");
					so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/alarmStatepie.xml"));
					so.addVariable("chart_data","<%=piedata%>");
					so.write("pie");
					// ]]>
				</script>
		  	</td>
	  </tr>
	  <tr><td colspan="2" bgcolor="#FFFFFF" align="right">
			  	<table>
			  	 	<tr>
			  	 		<td>
			  	 			<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=closedAlarmPicFile%>" id="closedAlarmPicFile">
			  	 		</td>
			  	 		<td align="right">
			  	 			<div id="column">
								<strong>You need to upgrade your Flash Player</strong>
							</div>
							<script type="text/javascript">
								// <![CDATA[		
								var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","480", "320", "8", "#FFFFF");
								so.addVariable("path", "<%=rootPath%>/amchart/");
								so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/lastweekColumn.xml"));
								so.addVariable("chart_data", "<%=columndata%>");
								so.write("column");
								// ]]>
							</script>
			  	 		</td>
			  	 	</tr>
			  	</table>
	  	  </td>
	</tr>
</table>	
</td>
	<td><%--1*2  --%>
		 <table>
					  <tr>
					  <td>
					  	<div id="lineOne">
							<strong>You need to upgrade your Flash Player</strong>
						</div>
						<script type="text/javascript">
						// <![CDATA[		
						var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline","280", "300", "8", "#FFFFFF");
						so.addVariable("path", "<%=rootPath%>/amchart/");
						so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dayalarm_setting.xml"));
						so.addVariable("chart_data", "<%=dayalarmData%>");
						so.write("lineOne");
						// ]]>
					</script>
				  </td>
	 			 </tr>
	 			 <tr>
				  	<td>
					  	<div id="lineTwo">
							<strong>You need to upgrade your Flash Player</strong>
						</div>
						<script type="text/javascript">
							// <![CDATA[		
							var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline","280", "300", "8", "#FFFFFF");
							so.addVariable("path", "<%=rootPath%>/amchart/");
							so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/weekalarm_settings.xml"));
							so.addVariable("chart_data", "<%=weekalarmData%>");
							so.write("lineTwo");
							// ]]>
						</script>
					</td>
			</tr>
 	</table>
	</td>
	</tr>
	</table><!-- 1*2 -->
</td>
</tr>
<%------------------------------�Ҳ������� �������------------------%>
					<tr><!--�Ҳ������� β��ʼ-->
				         <td background="<%=rootPath%>/common/images/right_b_02.jpg" >
				           <table width="100%" border="0" cellspacing="0" cellpadding="0">
				                  <tr><td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td><td></td>
				                       <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
				                  </tr>
				            </table>
	           			</td>
	    			</tr><%--�Ҳ������� β����--%>	
				</table>
		</td><%--�Ҳ������ڽ���--%>	
	</tr>
</table>
</body>
</html>
