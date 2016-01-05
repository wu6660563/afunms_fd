<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
    var ht = new Array(2,2,1,1,2);
    var num = ht.length;
    var start = new Array(num);
    var print =new Array(num*2);
	function init(){
		for( var i=0 ; i < num; i++){
			document.getElementById("imgs").innerHTML += '<img id="imgid" style="visibility:visible" src="E://d54.GIF" align="middle"/><br></br>';	
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
			//alert("run: "+"server" + i +" status " + start[i]);
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
			document.getElementsByName("imgid")[j].src = "E://d58.GIF" ;
			return false;
		}
		if(start != 0){
			//这里应该设置彩色图片路径
			document.getElementsByName("imgid")[j].src = "E://d56.GIF" ;
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
			document.getElementsByName("imgid")[j].src = "E://d17.GIF" ;
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
  	setInterval('stepd()',5000)
  	setInterval('run()',1000)
  	

  	
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />

<title>demo</title>
</head>
<body onload= "init()">
<div id = "imgs" align="center">

</div>
<p></p>
<br />
<div align="center">	
		
	<textarea id="t1" cols="30" rows="14" value=" "></textarea>
</div>
</body>
</html>
