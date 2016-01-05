<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.Cluster"%>

<%
	String rootPath = request.getContextPath();
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
    var ht = new Array(1,0,0);
    var num = ht.length;
    var start = new Array(num);
    var print =new Array(num*2);
	function init(){
		for( var i=0 ; i < num; i++){
			document.getElementById("imgs").innerHTML += '<img id="imgid" style="visibility:visible" src="<%=rootPath%>/resource/image/topo/server.gif" align="middle"/><br></br>';	
			start[i] = ht[i];
			print[2*i] = true;
			print[2*i+1] = true; 
		}
	}
	function stepd(){
		for(var f =0  ; f < num; f++){
		
			start[f] = ht[f];
		
		}	
	}	
	function run(){
		for(i = 0; i<start.length; i++){
			if(i == 0){
				show(start[i],i);
			}
			if(i > 0){
				if(!print[2*i-1]){
					show(start[i],i);
				}
			}
		}
	}
	function show(start,j){
		if(start == -1){
			document.getElementsByName("imgid")[j].src = "<%=rootPath%>/resource/image/topo/server.gif" ;
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
			document.getElementsByName("imgid")[j].src = "<%=rootPath%>/resource/image/topo/server_alarm.gif" ;
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

  	function change(){
		for(var i=0;i<num-1;i++){
			if(!print[2*i]){
				if(ht[i]==0)
					ht[i] = 1;
				if(ht[i]==1)
					ht[i] = 2;
			}
			if(!print[2*i+1]){
				if(ht[i]==2)
					ht[i+1] = 1;
			}
		}
	}
	setInterval('stepd()',2000)
  	setInterval('run()',800)
	setInterval('change()',5000)
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title>远程关机效果演示</title>
</head>
<body id="body" class="body" onload="init();">
		<table id="body-container" class="body-container" align="center" >
			<tr>
				<td class="td-container-main">	
					<table id="add-content" class="add-content" align="center">
						<tr>
							<td>
								<table id="add-content-header" class="add-content-header" align="center">
									<tr>
										<td align="center" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										<td class="add-content-title">
											自动化 >> 自动化控制 >> 远程关机效果演示
										</td>
										<td align="center"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td style="background:url(<%=rootPath%>/common/images/updown_bg.jpg) top repeat-x;">
								<table id="detail-content-body" class="detail-content-body" style="background:url(<%=rootPath%>/common/images/updown_bg.jpg) top repeat-x;">
									<tr>
										<td><br>
											<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
												<tr>
													<td nowrap align="center">
														<div id = "imgs" align="center"></div>
													</td>
												</tr>
												<tr>
													<td nowrap align="center">
														<div align="center">			
															<textarea id="t1" cols="55" rows="10" value=" " style="overflow:auto;"></textarea>
														</div>
													</td>
												</tr>
												<tr><td>&nbsp;</td></tr>
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
</html>
