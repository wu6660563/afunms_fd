<%@ page language="java"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//UserUISkin skin = (UserUISkin) request.getSession().getAttribute(
		//	"userUISkin");

	//String bgImgUrl = skin.getContentBGImgUrl();
	String bgImgUrl = "/ui/theme/default/content_bg.jpg";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow:hidden;">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<%
	//Boolean isDeBug = SystemParam.getInstance().isDebug();
	Boolean isDeBug = true;
    if (isDeBug) {
%>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="expires" content="0" />
<%
	}
%>
<title>机房视图</title>
<style>
body {
	margin: 0px;
	overflow: hidden
}
</style>
<script type="text/javascript"
	src="<%=path%>/widget/jquery/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/share/almShare.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/flex/swfjs/history/history.css" />
<script type="text/javascript"
	src="<%=path%>/flex/swfjs/history/history.js"></script>
<!-- END Browser History required section -->

<script type="text/javascript" src="<%=path%>/flex/swfjs/swfobject.js"></script>
<script charset="utf-8" type="text/javascript"
	src="<%=path%>/room/view/view.js"></script>
	<script charset="utf-8" type="text/javascript"
	src="<%=path%>/share/winCtrl.js"></script>
<script type="text/javascript">
		    var imagePath = "<%=bgImgUrl%>";
		    var isRefresh = "<%=isDeBug%>";
            <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
            var swfVersionStr = "10.0.0";
            <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
            var xiSwfUrlStr = "../../flex/swfjs/playerProductInstall.swf";
            var flashvars = {};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#aaaafc";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "viewswfObj";
            attributes.name = "viewswfObj";
            attributes.align = "middle";
            
            swfobject.embedSWF(
                "../../flex/itims-web-flex-room.swf?ver="+(isRefresh=="true"?Math.random():""), "flashContent", 
                "100%", "100%", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
			<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        
           window.resizeTo(window.screen.availWidth,window.screen.availHeight);        
		  function resize(){
			  if(document.getElementById("tempdiv").innerHTML!=""){
			      var h1 = document.getElementById("header_nav").clientHeight;
	   	          document.getElementById("viewswf").style.height = (document.documentElement.clientHeight -h1)+"px";
			  }
          }
          function load(){
              document.getElementById("tempdiv").innerText="temp";              
              document.getElementById("viewswf").style.height = document.documentElement.clientHeight+"px";
              autoSize('viewswf');
          }
        </script>
</head>
<body width="100%" height="100%" onload="load()"
	onresize="autoSize('viewswf')" style="overflow: hidden;">
	<input type="hidden" id="path" value="<%=path%>" />
	<div id="tempdiv" style="display: none;"></div>
	<div id="viewswf">
		<div id="flashContent"></div>
	</div>
	<form action="<%=path%>/loggerExport" method='post'
		style='display: none'>
		<input type='submit' id='exportSub' />
	</form>	
</body>
</html>
