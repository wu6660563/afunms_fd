<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.event.model.CheckEvent"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>

<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	
	String nodeid = (String)request.getAttribute("nodeid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");

	List allNodeDTOlist = (List)request.getAttribute("allNodeDTOlist");
	Hashtable<String, NodeDTO> allNodeDTOHashtable = (Hashtable<String, NodeDTO>)request.getAttribute("allNodeDTOHashtable");

	List list = (List)request.getAttribute("list");

	JspPage jp = (JspPage)request.getAttribute("page");
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<LINK href="<%=rootPath%>/resource/css/global/global.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<script language="javascript">	
    var curpage= <%=jp.getCurrentPage()%>;
    var totalpages = <%=jp.getPageTotal()%>;
    var listAction = "<%=rootPath%>/checkevent.do?action=list"; 

  
    // 根据设备类型信息进行告警查询
    function search() {
    	mainForm.action = "<%=rootPath%>/checkevent.do?action=list";
        mainForm.submit();
    }
  
    function showNodeDetails(nodeId){
   		mainForm.action = "<%=rootPath%>/detail/dispatcher.jsp?id=" + nodeId+"&fromtopo=true";
     	mainForm.submit();
  	}
  	
  	function addIP(sindex,alarmId){
  		mainForm.action = "<%=rootPath%>/ipmanage.do?action=addIp&sindex="+sindex+"&alarmId="+alarmId;
  		mainForm.submit();
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
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td id="td-container-main-content" class="td-container-main-content">
								<table id="container-main-content" class="container-main-content">
									<tr>
										<td>
											<table id="content-header" class="content-header">
							                	<tr>
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
								                	<td class="content-title">&nbsp;告警 >> 告警浏览 >> 当前告警 </td>
								                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
								       			</tr>
								        	</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="content-body" class="content-body">
												<tr>
													<td >
														<table>
															<tr >
																<td  class="detail-data-body-title">
																	<table>
																		<tr>
																		<td class="body-data-title" style="text-align: left;">
																			&nbsp;&nbsp;<b>类型：</b>
																		<select id="type" name="type" style="width:110px" onchange="changeType()">
																			<option value="-1">不限</option>
																			<option value="host">服务器</option>
																			<option value="net">网络设备</option>
																			<option value="firewall">防火墙</option>
																			<option value="gateway">邮件安全网关</option>
																			<option value="atm">ATM</option>
																			<option value="f5">F5</option>
																			<option value="db">数据库</option>
																			<option value="middleware">中间件</option>
																			<option value="service">服务</option>
																			<option value="ups">UPS</option>
																			<option value="air">空调</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<b>子类型：</b>
																		<select id="subtype" name="subtype" style="width:100px" onchange="changeNode()">
																			<option value="-1">不限</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;<b>设备：</b>
																		<select id="nodeid" name="nodeid" style="width:220px">
																		</select>
																		&nbsp;&nbsp;&nbsp;<input type="button" value="查  询" onclick="search()">
							        								</td>
																			<td width="15" bgcolor=red height=15 >&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;紧急告警</td>
																			<td width="15" bgcolor=orange height=15>&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;严重告警</td>
																			<td width="15" bgcolor=yellow height=15>&nbsp;&nbsp;</td>
																			<td  height=15>&nbsp;&nbsp;普通告警&nbsp;&nbsp;</td>
																		</tr>
																	</table>
						  										</td>
															</tr>
														</table>
			  										</td>
												</tr>
					        				
					        					<tr>
													<td>
														<table>
															<tr>
																<td class="detail-data-body-title">
											    					<jsp:include page="../../common/page.jsp">
											     						<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
											     						<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
											   						</jsp:include>
											          			</td>
											        		</tr>
														</table>
													</td>
												</tr> 
					        					<tr>
													<td>
														<table>
															<tr>
																<td class="detail-data-body-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">序号</td>
        														<td class="detail-data-body-title">等级</td>
        														<td class="detail-data-body-title">告警来源</td>
														    	<td class="detail-data-body-title">告警描述</td>
														    	<td class="detail-data-body-title">最新时间</td>
														    	<td class="detail-data-body-title">操作</td>
															</tr>
															<%
																if(list!=null && list.size()>0){
																	for(int i = 0 ; i < list.size(); i++){
																			CheckEvent checkEvent = (CheckEvent)list.get(i);
																			
																			String subtype_tmp = checkEvent.getType();
																			
																			int nodeid_tmp = Integer.parseInt(checkEvent.getNodeId());
																			
																			String dispatcher_id = "";
																			
																			if("host".equals(subtype_tmp)) {
																				dispatcher_id = "net" + nodeid_tmp;
																			} else if("network".equals(subtype_tmp) || "net".equals(subtype_tmp)) {
																				dispatcher_id = "net" + nodeid_tmp;
																			} else if("firewall".equals(subtype_tmp)) {
																				dispatcher_id = "saf" + nodeid_tmp;
																			} else if("db".equals(subtype_tmp)){
																				dispatcher_id = "dbs" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("bus")) {
																				dispatcher_id = "bus" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("web")) {
																			
																			} else if(subtype_tmp.equalsIgnoreCase("mail")) {
																				dispatcher_id = "mai" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("wasserver")) {
																				dispatcher_id = "was" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("grapes")) {
																				
																			} else if(subtype_tmp.equalsIgnoreCase("radar")) {
																				
																			} else if(subtype_tmp.equalsIgnoreCase("plot")) {
																				
																			} else if(subtype_tmp.equalsIgnoreCase("tomcat")) {
																				dispatcher_id = "tom" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("ftp")) {
																				dispatcher_id = "ftp" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("mq")) {
																				dispatcher_id = "mqs" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("domino")) {
																				dispatcher_id = "dom" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("iis")) {
																				dispatcher_id = "iis" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("socket")) {
																				dispatcher_id = "soc" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("cics")) {
																				dispatcher_id = "cic" + nodeid_tmp;
																			} else if(subtype_tmp.equalsIgnoreCase("weblogic")) {
																				dispatcher_id = "web" + nodeid_tmp;
																			}
																			// out.println("dispatcher_id"+dispatcher_id);
																			
																			String level = String.valueOf(checkEvent.getAlarmlevel());
																			String bgcolor = "";
																			if("0".equals(level)){
																		  		level="提示信息";
																		  		bgcolor = "bgcolor='blue'";
																		  	}
																			if("1".equals(level)){
																		  		level="普通告警";
																		  		bgcolor = "bgcolor='yellow'";
																		  	}
																		  	if("2".equals(level)){
																		  		level="严重告警";
																		  		bgcolor = "bgcolor='orange'";
																		  	}
																		  	if("3".equals(level)){
																		  		level="紧急告警";
																		  		bgcolor = "bgcolor='red'";
																		  	}
                                                                            
                                                                            NodeDTO nodeDTO = allNodeDTOHashtable.get(String.valueOf(checkEvent.getNodeId()) + ":"
                                                                                            + checkEvent.getType() + ":" + checkEvent.getSubtype());
																		%>
																		<tr <%=onmouseoverstyle%>>
																			<td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox"><%=jp.getStartRow()+i%></td>
			        														<td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
			        														<td class="detail-data-body-list"><a href="#"  onclick='showNodeDetails("<%=dispatcher_id%>")'><%=nodeDTO.getName() + "(" + nodeDTO.getIpaddress() + ")" %></a></td>
																	        <td class="detail-data-body-list"><%=checkEvent.getContent()%></td>
																	    	<td class="detail-data-body-list"><%=checkEvent.getCollecttime()%></td>
																	    	<%if("ipmac".equals(checkEvent.getName())) {
																	    		%>
																	    	<td class="detail-data-body-list"><input type="button" name="addIp" value="添加IP" onclick="addIP('<%=checkEvent.getSindex()%>','<%=checkEvent.getAlarmId()%>')"></td>	
																	    		<%
																	    	} else {
																	    		%>
																	    	<td class="detail-data-body-list"></td>	
																	    		<%
																	    	} %>
																		</tr>
																		<%
																	}
																} 
															%>
														</table>
													</td>
												</tr>				
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="content-footer" class="content-footer">
												<tr>
													<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                  									<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
		
	</form>
</BODY>

<script>
	
		
	
		var nodeArray = new Array();
		
		<%
			if(allNodeDTOlist != null){
				for(int j = 0 ; j < allNodeDTOlist.size(); j++){
				 	NodeDTO  nodeDTO = (NodeDTO)allNodeDTOlist.get(j);
					%>
						nodeArray.push({
							id:"<%=nodeDTO.getId()%>",
							nodeid:"<%=nodeDTO.getNodeid()%>",
							name:"<%=nodeDTO.getName()%>" + "<%=nodeDTO.getIpaddress()%>",
							ipaddress:"<%=nodeDTO.getIpaddress()%>",
							type:"<%=nodeDTO.getType()%>",
							subtype:"<%=nodeDTO.getSubtype()%>",
							businessId:"<%=nodeDTO.getBusinessId()%>",
							businessName:"<%=nodeDTO.getBusinessName()%>"
					});
					<%
				}
			}
			
		%>
		
		function changeType(){
			var hostArray = [
						["windows" , "windows"],
						["linux" , "linux"],
						["aix" , "aix"],
						["hpunix" , "hpunix"],
						["solaris" , "solaris"],
						["scounix" , "scounix"],
						["scoopenserver" , "scoopenserver"],
						["as400" , "as400"]
						];
			var netArray = new Array();
			netArray = 	[
						["cisco" , "cisco"],
						["h3c" , "h3c"],
						["entrasys" , "entrasys"],
						["maipu" , "maipu"],
						["redgiant" , "redgiant"],
						["northtel" , "northtel"],
						["dlink" , "dlink"],
                        ["DigitalChina" , "DigitalChina"]
						];
			var dbArray = new Array();
			dbArray = 	[
						["oracle" , "oracle"],
						["sqlserver" , "sqlserver"],
						["mysql" , "mysql"],
						["db2" , "db2"],
						["sybase" , "sybase"],
						["Informix" , "Informix"]
						];
			var middlewareArray = new Array();
			middlewareArray = 	[
						["mq" , "mq"],
						["domino" , "domino"],
						["was" , "was"],
						["tomcat" , "tomcat"],
						["iis" , "iis"],
						["jboss" , "jboss"],
						["apache" , "apache"],
						["tuxedo" , "tuxedo"],
						["cics" , "cics"],
						["dns" , "dns"],
						["weblogic" , "weblogic"]
								];
			var serviceArray = new Array();
			serviceArray = 	[
						["ftp" , "ftp"],
						["mail" , "mail"],
						["hostprocess" , "hostprocess"],
						["url" , "url"],
						["socket" , "socket"]
					];
		    var firewallArray = new Array();
			firewallArray = [
						["山石" , "hillstone"],
						["Netscreen" , "netscreen"],
						["NOKIA" , "nokia"]
								];
			var atmArray = new Array();
			atmArray = [
						["ATM" , "atm"]
					   ];
		    var f5Array = new Array();
			f5Array = [
						["F5" , "f5"]
					   ];
			var gatewayArray = new Array();
			gatewayArray = [
						    ["CISCO" , "cisco"]
						   ];
			var upsArray = new Array();
			upsArray = 	[
						["艾默生" , "ems"],
						["梅兰日兰" , "mge"]
								];
			var airArray = new Array();
			airArray = 	[
						["艾默生" , "ems"]  
								];
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			subtype.length = 0;
			if(type.value == "-1"){
				subtype.options[0] = new Option("不限" , "-1");      
			}else if(type.value == "net"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <netArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(netArray[i][0],netArray[i][1]);                     
				}	
			}else if(type.value == "host"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <hostArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(hostArray[i][0],hostArray[i][1]);                     
				}	
			}else if(type.value == "db"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <dbArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(dbArray[i][0],dbArray[i][1]);                     
				}	
			}else if(type.value == "middleware"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <middlewareArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(middlewareArray[i][0],middlewareArray[i][1]);                     
				}	
			}else if(type.value == "service"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <serviceArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(serviceArray[i][0],serviceArray[i][1]);  
				}                   
			}else if(type.value == "ups"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <upsArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(upsArray[i][0],upsArray[i][1]);                     
				}	
			}else if(type.value == "air"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <airArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(airArray[i][0],airArray[i][1]);                     
				}	
			}else if(type.value == "atm"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <atmArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(atmArray[i][0],atmArray[i][1]);                     
				}	
			}else if(type.value == "f5"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <f5Array.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(f5Array[i][0],f5Array[i][1]);                     
				}	
			}else if(type.value == "firewall"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <firewallArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(firewallArray[i][0],firewallArray[i][1]);                     
				}	
			}else if(type.value == "gateway"){
				subtype.options[0] = new Option("不限" , "-1");
				for (var i = 0 ;i <gatewayArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(gatewayArray[i][0],gatewayArray[i][1]);                     
				}	
			}
			
			changeNode();
		}
		
		
		function changeNode(){
			
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			nodeid.length = 0;
			nodeid.options[0] = new Option("不限","-1");
			if(type.value == "-1"){
				var k = 1;
				
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{                   
					nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      			k++;               
				}	
			}else if(type.value != "-1" && subtype.value == "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{      
	      			if(nodeArray[i].type == type.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}else if(type.value != "-1" && subtype.value != "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)
	      		{      
	      			if(nodeArray[i].type == type.value && nodeArray[i].subtype == subtype.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}
			
		}
		
		function initAttribute(){
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			
			type.value = "<%=type%>";
			changeType();
			subtype.value = "<%=subtype%>";
			changeNode();
			nodeid.value = "<%=nodeid%>";
		}
		
		initAttribute();
		
	</script>


</HTML>
