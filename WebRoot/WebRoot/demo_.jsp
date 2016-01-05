<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%
String path= request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
	var http_request = false;
	function send_request(url){
 		http_request= false;
 		if(window.XMLHttpRequest){
 			http_request = new XMLHttpRequest();
 			if(http_request.overrideMimeType){
 				http_request.overrideMimeType("text/xml");
 			}
 		}
 		else if(window.ActiveXObject){
 			try{
 				http_request= new ActiveXObject("Msxml2.XMLHTTP");
 			}catch(e){
 				try{
 					http_request= new ActiveXObject("Microsoft.XMLHTTP");
 				}catch(e){}
 			}
 		}
 		if(!http_request){
 			window.alert("NO CREATE");
 			return false;
 		}
 		http_request.onreadystatechange = function(){processRequest()};
 		http_request.open("POST",url,true);
 		http_request.send(null);
 	}
 	function processRequest(){
 		if(http_request.readyState == 4){
 			if(http_request.status == 200){
 				var divhtml = http_request.responseText;
 				document.getElementById("userId").value=divhtml;
 				step =  divhtml;
 			}
 		}
 	}
	function getTopic(){
 		var str ='<%=path%>/ajaxDemo?status='+ document.getElementById("userId").value;
 		send_request(str);
	}
	function getStep(){
		var str = '<%=path%>/ajaxDemo?';
 		send_request(str);
	} 
	var step=0;
	var _stop1=false;
	var _stop2=false;
	var _stop3=false;
	var _start1 = false;
	var _start2 = false;
	var _start3 = false;
	var _start4 = false;
	function show1(){
		_start1 = true;
		var imgid1=document.getElementById("imgid1");
		if(imgid1.style.visibility == "visible")
			imgid1.style.visibility = "hidden";
		else
			imgid1.style.visibility = "visible";
		if(!_stop1){
			setTimeout('show1()',300);
		}else{
			document.getElementById("imgid1").style.visibility = "visible";
			return false;
		}
 	}
	function show2(){
		_start2 = true;
		_stop1 = true;
		var imgid2=document.getElementById("imgid2");
		if(imgid2.style.visibility == "visible")
			imgid2.style.visibility = "hidden";
		else
			imgid2.style.visibility = "visible";
		if(!_stop2)
			setTimeout('show2()',300);
		else{
			document.getElementById("imgid2").style.visibility = "visible";
			return false;
		}
	}
  	function show3(){
		_start3 = true;
		_stop2 = true;
		var imgid3=document.getElementById("imgid3");
		if(imgid3.style.visibility == "visible")
			imgid3.style.visibility = "hidden";
		else
			imgid3.style.visibility = "visible";
		if(!_stop3)
			setTimeout('show3()',300);
		else{
			document.getElementById("imgid3").style.visibility = "visible";
			return false;
		}
	}
	function stop(){
		_start4 = true;
		_stop3 = true;
	}
  	function show(){
		if (step==1 &&!_start1){
			show1();
			document.getElementById("t1").value+="BEGIN \nnow start first step......\n";
		}
		if (step==2 && !_start2){
			show2();
			document.getElementById("t1").value += "now stop first step......\n";
			document.getElementById("t1").value+="now start second step......\n";
		}
		if (step==3 && !_start3 ){
			show3();
			document.getElementById("t1").value += "now stop second step......\n";
			document.getElementById("t1").value+="now start third step......\n";
		}
		if (step==4 && !_start4 ){
			stop();
			document.getElementById("t1").value += "now stop third step......\n";
			document.getElementById("t1").value += "END!\n";
		}
  	}
  	setInterval('getStep()',8000)
  	setInterval('show()',2000)
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>demo</title>
</head>
<body>
<div align="center">
	<img id="imgid1" style="visibility:hidden" src="E:/d17.GIF" align="middle"/>
	<br></br>
	<img id="imgid2" style="visibility:hidden" src="E:/d54.GIF" align="middle"/>
	<br></br>
	<img id="imgid3" style="visibility:hidden" src="E:/d56.GIF" align="middle"/>
	<br></br>
</div>
<br />
<div align="center">
	<input id="userId" type="text" value = "0" onclick="getTopic()" />
	<p></p>
	<textarea id="t1" cols="30" rows="14" value=" "></textarea>
</div>
</body>
</html>
