<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%
String path= request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	System.out.println(basePath);
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
	var _start1 = 0;
	var _start2 = 0;
	var _start3 = 0;
	var print1 = true;
	var print2 = true;
	var print3 = true;
	var print4 = true;
	var print5 = true;
	var print6 = true;
	function getStep(){
		//document.getElementById("userId1").value = "<%=((Hashtable)session.getAttribute("step")).get(1) %>";
		//document.getElementById("userId2").value = "<%=((Hashtable)session.getAttribute("step")).get(2) %>";
		//document.getElementById("userId3").value = "<%=((Hashtable)session.getAttribute("step")).get(3) %>";
		
		//step session中hashtable

		_start1 = "<%=((Hashtable)session.getAttribute("step")).get(1) %>";
		_start2 = "<%=((Hashtable)session.getAttribute("step")).get(2) %>";
		_start3 = "<%=((Hashtable)session.getAttribute("step")).get(3) %>";
	} 

	function show1(){
		if(_start1 != 0){
			//这里应该设置彩色图片路径
			//document.getElementById("imgid1").src = ;
		}
		if(_start1 == -1){
			document.getElementById("imgid1").style.visibility = "visible";
			return false;
		}
		if(_start1 == 1){
			var imgid1=document.getElementById("imgid1");
			if(imgid1.style.visibility == "visible")
				imgid1.style.visibility = "hidden";
			else
				imgid1.style.visibility = "visible";
			if(print1){
				document.getElementById("t1").value+="BEGIN \nnow start first step......\n";
				print1 = false;
			}
			//setTimeout('show1()',300);		
		}
		if(_start1 == 2){
			document.getElementById("imgid1").style.visibility = "visible";
			if(print2){
				document.getElementById("t1").value += "now stop first step......\n";
				print2 = false;
			}
			show2();
		}
 	}
	function show2(){
		if(_start2 != 0){
			//这里应该设置彩色图片路径
			//document.getElementById("imgid2").src = ;
		}
		if(_start2 == -1){
			document.getElementById("imgid2").style.visibility = "visible";
			return false;
		}
		if(_start2 == 1){
			var imgid2=document.getElementById("imgid2");
			if(imgid2.style.visibility == "visible")
				imgid2.style.visibility = "hidden";
			else
				imgid2.style.visibility = "visible";
			if(print3){	
				document.getElementById("t1").value+="now start second step......\n";
				print3 = false;
			}
			//setTimeout('show2()',300);
		}
		if(_start2 == 2){
			document.getElementById("imgid2").style.visibility = "visible";
			if(print4){
				document.getElementById("t1").value += "now stop second step......\n";
				print4 = false;
			}
			show3();
		}
	}
  	function show3(){
  		if(_start3 != 0){
			//这里应该设置彩色图片路径
			//document.getElementById("imgid3").src = ;
		}
  		if(_start3 == -1){
  			document.getElementById("imgid3").style.visibility = "visible";
			return false;
		}
		if(_start3 == 1){
			var imgid3=document.getElementById("imgid3");
			if(imgid3.style.visibility == "visible")
				imgid3.style.visibility = "hidden";
			else
				imgid3.style.visibility = "visible";
			if(print5){	
				document.getElementById("t1").value+="now start third step......\n";
				print5 = false;
			}
			//setTimeout('show3()',300);
		}
		if(_start3 == 2){
			document.getElementById("imgid3").style.visibility = "visible";
			if(print6){
				document.getElementById("t1").value += "now stop third step......\n";
				print6 = false;
			}
		}
	}
	
  	setInterval('getStep()',3000)
  	setInterval('show1()',500)
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title>demo</title>
</head>
<body>
<div align="center">
	<img id="imgid1" style="visibility:visible" src="E://d17.GIF" align="middle"/><!-- 这里路径配灰色图片 -->
	<br></br>
	<img id="imgid2" style="visibility:visible" src="E://d54.GIF" align="middle"/><!-- 这里路径配灰色图片 -->
	<br></br>
	<img id="imgid3" style="visibility:visible" src="E://d56.GIF" align="middle"/><!-- 这里路径配灰色图片 -->
	<br></br>
</div>
<p></p>
<br />
<div align="center">	
		<!--  <input id="userId1" type="text" value = '<%=((Hashtable)session.getAttribute("step")).get(1) %>' />
		<input id="userId2" type="text" value = '<%=((Hashtable)session.getAttribute("step")).get(2) %>' />
		<input id="userId3" type="text" value = '<%=((Hashtable)session.getAttribute("step")).get(3) %>' />
	<p></p>
	-->
	<textarea id="t1" cols="30" rows="14" value=" "></textarea>
</div>
</body>
</html>
