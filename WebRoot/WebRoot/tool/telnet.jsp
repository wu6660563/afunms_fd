 <%@ page language="java" contentType="text/html; charset=GBK" %>
 <%@ page  import="java.io.*"%>
<%
	String getIp=(String)request.getAttribute("ipaddress") ;
if(getIp==null) getIp="";
String rootPath = request.getContextPath();
System.out.println(getIp);
    //Runtime runtime = Runtime.getRuntime();
    //runtime.exec("cmd.exe /c start telnet " + getIp);
%>
<html>
  <head>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="javascript">
  function startTelnet() {
  try {

   // var cmd = Wscript.CreateObject("WScript.Shell");
     //alert(cmd);
    //cmd.run("cmd.exe /c start telnet " + <%=getIp%>);
    // WshShell = WScript.CreateObject("WScript.Shell");
    // WshShell.Run("cmd.exe");
    //alert("aaa");
    //var shell = new ActiveXObject("wscript.shell");
    //alert("aaaa");
    //shell.run("net use z: /del & net use z: \\192.168.32.8\p$  'test'  /user:test ",0);
        //alert("aaa");
        var ws = new ActiveXObject("Wscript.Shell");
        //alert("----1");
        //alert("cmd.exe /c start telnet " + "<%=getIp%>");
        ws.Run("cmd.exe /c start telnet " + "<%=getIp%>");
        //alert("----2");
    //var href = "<%=rootPath%>/tool/telnet.html"
  	//newWin = window.open(href, "telnet", "height=1000, width= 500, top=0, left= 0");
  	//setTimeout(closeTelnet(), 20000);
  	//setTimeout(closeOldWin(), 20000);  
  	//parent.close();   
	window.close(); 
  } catch(e) {
  	e.printStackTrace();
  }
  }
  function closeOldWin() {
  	try {
  		//window.close();
  	} catch (e) {
  	}
  }
  function closeTelnet() {
  	try {
  		newWin.close();
  		parent.close();
  	} catch (e) {
  	}
  }
  

  
</script>
</head>
<body onload="startTelnet();" onunload="closeTelnet();">
</body>
</html>

