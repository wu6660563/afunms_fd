<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.Cluster"%>

<%
	String rootPath = request.getContextPath();
	int[] steps = (int[])session.getAttribute("steps");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="gb2312" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>

		<!--nielin add for timeShareConfig at 2010-01-04 start-->
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<!--nielin add for timeShareConfig at 2010-01-04 end-->
		
<script type="text/javascript">
    var num = <%=steps.length%>;
    var ht = new Array(num);
    var print =new Array(num*2);
    var node = <%=session.getAttribute("clusterId")%>;
  	$(document).ready(function(){
		sendAjax();
	});
	
    function sendAjax(){
    	$.ajax({
			type:"GET",
			dataType:"text",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=showMovement&clusterId="+node+"&nowtime="+(new Date()),
			success:function(obj){
				var step = obj.split(",");
				for(var i=0; i<ht.length;i++){
					ht[i]=step[i];
				}
				sendAjax();
			}
		});
    }
    
	function init(){
		for( var i=0 ; i < num; i++){
			document.getElementById("imgs").innerHTML += '<img id="imgid" style="visibility:visible" src="<%=rootPath%>/resource/image/topo/server_alarm.gif" align="middle"/><br></br>';	
			ht[i] = 0;
			print[2*i] = true;
			print[2*i+1] = true; 
		}
	}
	function run(){
		for(i = 0; i<ht.length; i++){
			//alert("run: "+"server" + i +" status " + ht[i]);
			if(i == 0){
				show(ht[i],i);
			}
			if(i > 0){
				if(!print[2*i-1]){
					show(ht[i],i);
				}
			}
		}
	}
	function show(start,j){
		if(start == -1){
			document.getElementsByName("imgid")[j].src = "<%=rootPath%>/resource/image/topo/server_alarm.gif" ;
			return false;
		}
		if(start != 0){
			//这里应该设置彩色图片路径
			document.getElementsByName("imgid")[j].src = "<%=rootPath%>/resource/image/topo/server_alarm.gif" ;
		}

		if(start == 1){
			var imgid1=document.getElementsByName("imgid")[j];
			printed(imgid1);
			if(print[2*j]){
				document.getElementById("t1").value += "now start "+(j+1)+" step......\n";
				print[2*j] = false;
			}		
		}
		if(start == 2){
			document.getElementsByName("imgid")[j].src = "<%=rootPath%>/resource/image/topo/server_alarm.gif";
			document.getElementsByName("imgid")[j].style.visibility = "visible";
			if(print[2*j]){
				document.getElementById("t1").value += "now start "+(j+1)+" step......\n";
				print[2*j] = false;
			}
			if(print[2*j+1]){
				document.getElementById("t1").value += "now stop "+(j+1)+" step......\n";
				print[2*j+1] = false;
			}
		}
 	}
 	function printed(obj){
 		if(obj.style.visibility == "visible")
			obj.style.visibility = "hidden";
		else
			obj.style.visibility = "visible";
 	}

  		setInterval('run()',800);
		setInterval('getSteps()',10000);

	</script>
	</head>
	<body id="body" class="body" onload="init();">
		<table id="body-container" class="body-container" align="center">
			<tr>
				<td class="td-container-main">	
					<table id="add-content" class="add-content" align="center">
						<tr>
							<td>
								<table id="add-content-header" class="add-content-header" align="center">
									<tr>
										<td align="center" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										<td class="add-content-title">
											自动化 >> 自动化控制 >> 设备组关机管理 >> 关机操作
										</td>
										<td align="center"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table id="detail-content-body" class="detail-content-body">
									<tr>
										<td>
											<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
												<tr style="background-color: #ECECEC;">
													<td nowrap align="center">
														<div id = "imgs" align="center"></div>
													</td>
												</tr>
												<tr style="background-color: #ECECEC;">
													<TD nowrap align="center">
														<div align="center">			
															<textarea id="t1" cols="55" rows="7" value=" " style="overflow:auto;"></textarea>
														</div>
													</TD>
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
	</body>
</HTML>