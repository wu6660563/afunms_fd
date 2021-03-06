<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.Function"%>
<%@page import="com.afunms.system.util.CreateRoleFunctionTable"%>

<%
	String rootPath = request.getContextPath();
	User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
	if(user==null){
		response.sendRedirect("/common/error.jsp?errorcode=3003");
	}
   	List<Function> menuRoot = (List<Function>)request.getAttribute("menuRoot");
   	
   	List<Function> role_Function_list= (List<Function>)request.getAttribute("roleFunction");
   	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<title>IT运维监控系统</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	background-color: #ababab;
}
-->
</style>
<link href="css/css.css" rel="stylesheet" type="text/css" />

<script type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
   showMenu(a[1]);
}
//-->
  function toHome()
  {
     parent.mainFrame.location = "<%=rootPath%>/common/home.jsp";
  }

  function doQuit()
  {
     if (confirm("你真的要退出吗?"))
     {
         parent.location = "<%=rootPath%>/user.do?action=logout";
     }
  } 
</script>

<script type="text/javascript">
	function initWindow(){
		MM_preloadImages('images/menu01b.jpg','images/menu02b.jpg','images/menu03b.jpg','images/menu04b.jpg','images/menu05b.jpg','images/menu06b.jpg');
	}

</script>

<script type="text/javascript">
	
	function menuHref(url , isCurrentWindow , width , height){
		
		if(isCurrentWindow == 0){
			parent.mainFrame.location = "<%=rootPath%>/" + url;
		}else{
			if(!width){
				width =window.screen.width;
			}
			if(!height){
				height =window.screen.height;
			}
			window.open("<%=rootPath%>/" + url,"fullScreenWindow", "toolbar=no,height=" + height + ",width=" + width+ ",scrollbars=yes,resizable=yes,screenX=0,screenY=0");
		}
	}
</script>



<SCRIPT LANGUAGE="JScript">
// +------------------------
//  用popup来实现菜单
//  宝玉
// --------------------------



var pops = new Array();
function CreatePopup(degree)
{
    if (degree < 0)
        return null;
    if (pops[degree] != null)
        return pops[degree];

    if (degree == 0){
        pops[0] = window.createPopup();
    }
    else{
        if (pops[degree - 1] == null){
            pops[degree - 1] = CreatePopup(degree - 1);    //递归哦
            }
        pops[degree] = pops[0].document.parentWindow.createPopup();
    }
    pops[degree].document.body.setAttribute("degree", degree);
    return pops[degree];
}

CreatePopup(1);


//Creating the popup window object
var oPopup = pops[0];
var timer = null;
var timerMenu = null;
var switchFlag = false;


function showMenu(id)
{
    //var lefter = event.offsetY+10;
    //var topper = event.offsetX+10;
    var elmt = event.srcElement; 
    var offsetTop = elmt.offsetTop; 
	var offsetLeft = elmt.offsetLeft; 
    var offsetWidth = elmt.offsetWidth; 
    var offsetHeight = elmt.offsetHeight; 
    while( elmt = elmt.offsetParent ) 
    { 
          // add this judge 
        if ( elmt.style.position == 'absolute' || elmt.style.position == 'relative'  
            || ( elmt.style.overflow != 'visible' && elmt.style.overflow != '' ) ) 
        { 
            break; 
        }  
        offsetTop += elmt.offsetTop; 
        offsetLeft += elmt.offsetLeft; 
    }
    
    var lefter = offsetTop + offsetHeight;
   	var topper = offsetLeft ;
    
    oPopup.document.body.innerHTML = document.getElementById(id).innerHTML; 
    //This popup is displayed relative to the body of the document.
   
    oPopup.show(topper, lefter, 150, parseInt(document.getElementById(id).style.height.replace("px" , ""))+1, document.body);
	pops[1].hide();
}

function ShowSubMenu(j , id)
{
    ClearTimer();
    pops[1].document.body.innerHTML = document.getElementById(id).innerHTML; 
    
    //This popup is displayed relative to the body of the document.
    //alert(document.getElementById(id).style.height.replace("px" , ""));
    pops[1].show( 150, j*30 , 150 , parseInt(document.getElementById(id).style.height.replace("px" , ""))+1, pops[0].document.body);
}



function HideSubMenu()
{
    ClearTimer();
    timer = window.setTimeout("pops[1].hide()", 3000);
}

function ClearTimer()
{
    if (timer != null)
        window.clearTimeout(timer)
    timer = null;
}

function ClearTimer2()
{
    if (timerMenu != null)
        window.clearTimeout(timerMenu)
    timerMenu = null;
}

function HideMenu()
{
    ClearTimer2();
    timerMenu = window.setTimeout("pops[0].hide()", 1000);
}

function ShowBlackArrow(id){
	document.getElementById(id).style.display = 'inline';
}


</SCRIPT>



</head>

<body onload="initWindow()" style="background:#bec7ce;">
	<%
		
		CreateRoleFunctionTable crft = new CreateRoleFunctionTable();
		if(menuRoot != null && role_Function_list!=null){
			for(int i = 0 ; i < menuRoot.size() ; i++){
				Function perRootMenu = menuRoot.get(i);
				List secondFunctionList = crft.getFunctionChild(perRootMenu , role_Function_list);
				%>
				<DIV ID="<%=perRootMenu.getId()%>" STYLE="display:none;height: <%=secondFunctionList.size()*30%>;"
					 onmouseout="this.style.background='#e4e4e4';parent.HideMenu()" >
					 <DIV style="border: 1px solid #CECECE;">
				<%
				if( secondFunctionList!= null){
					for(int j =0 ; j < secondFunctionList.size() ; j++){
						Function perSecondFunction = (Function)secondFunctionList.get(j);
						//if(perSecondFunction.getId() == 10){
						//	continue;
						//}
						String perSecondFunctionOnClick = "";
						if(perSecondFunction.getUrl() != null && perSecondFunction.getUrl().trim().length()>0){
							perSecondFunctionOnClick = "ONCLICK=parent.menuHref('" + perSecondFunction.getUrl() +
							"','" + perSecondFunction.getIsCurrentWindow() + 
							"','" + perSecondFunction.getWidth() +
							"','" + perSecondFunction.getHeight() +"');";
						}
						
						String showBlackArrow = "document.getElementById('per-" + perSecondFunction.getId() +"-black-arrow').style.display ='inline';";
						
						List thirdFunctionList = crft.getFunctionChild(perSecondFunction , role_Function_list);
						if(thirdFunctionList==null || thirdFunctionList.size()==0){
							showBlackArrow = "";
						}
					%>
					<DIV id="per-<%=perSecondFunction.getId()%>" onmouseover="<%=showBlackArrow%>this.style.backgroundImage='url(images/menubg.jpg)';parent.ShowSubMenu('<%=j%>','<%=perSecondFunction.getId()%>');parent.ClearTimer2();" onmouseout="this.style.background='#F0F1F4';parent.HideSubMenu();parent.HideMenu();document.getElementById('per-<%=perSecondFunction.getId()%>-black-arrow').style.display ='none';" <%=perSecondFunctionOnClick%> STYLE="font-family:verdana; font-size:50%; height:30px;background:#F0F1F4;border-top: 1px solid #FAFAFA;border-bottom: 1px solid #CECECE; padding:4px; cursor:hand ">
				    <SPAN ONCLICK="" STYLE="font-family:verdana; font-size:12;">
				    <%=perSecondFunction.getCh_desc()%></SPAN> 
				    <DIV id="per-<%=perSecondFunction.getId()%>-black-arrow" align="right" style="display:none;position: absolute;left: 139px;vertical-align:middle;"><img src="images/black_arrow.gif"></DIV>
				    </DIV>
					<%
					}
				}
				%>
				</DIV>
				</DIV>
				<%
			}
		}
		%>
		
		
		<%
		
		CreateRoleFunctionTable crft2 = new CreateRoleFunctionTable();
		if(menuRoot != null && role_Function_list!=null){
			for(int i = 0 ; i < menuRoot.size() ; i++){
				Function perRootMenu = menuRoot.get(i);
				List secondFunctionList = crft.getFunctionChild(perRootMenu , role_Function_list);
				if( secondFunctionList!= null){
					for(int j =0 ; j < secondFunctionList.size() ; j++){
						Function perSecondFunction = (Function)secondFunctionList.get(j);
						List thirdFunctionList = crft.getFunctionChild(perSecondFunction , role_Function_list);
						%>
						<DIV ID="<%=perSecondFunction.getId()%>" STYLE="display:none;height: <%=thirdFunctionList.size()*30%>;border:1px solid #CECECE;"
						onmouseover=parent.parent.ClearTimer2();>
						<DIV style="border: 1px solid #CECECE;">
						<%
						if(thirdFunctionList!=null){
							for(int k = 0 ; k < thirdFunctionList.size() ; k++){
								Function perThirdFunction = (Function)thirdFunctionList.get(k);
								int id = perThirdFunction.getId();
								String ch_desc = perThirdFunction.getCh_desc();
								String url = perThirdFunction.getUrl();
								int isCurrentWindow = perThirdFunction.getIsCurrentWindow();
								%>
								<DIV onmouseover="this.style.backgroundImage='url(images/menubg.jpg)';parent.parent.ClearTimer2();" 
							         onmouseout="this.style.background='#F0F1F4';parent.parent.HideMenu();" 
							         ONCLICK="parent.parent.menuHref('<%=url%>' , '<%=isCurrentWindow%>')"
							         STYLE="font-family:verdana; font-size:50%; height:30px; background:#F0F1F4;border-top: 1px solid #FAFAFA;border-bottom: 1px solid #CECECE; padding:4px; cursor:hand ">
							    <SPAN STYLE="font-family:verdana; font-size:12;">
							    <%=ch_desc%></SPAN> 
							    </DIV>
								<%
							}
						}
						%>
						</DIV>
						</DIV>
						<%
					}
				}
			}
		}
		%>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr >
    <td width="33%" >
        <table width="100%" height="80" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="8%" style="background-image: url('images/top_bg_left.jpg');background-repeat: repeat-x;"></td>
                <td width="400px" style="background-image: url('images/top_bg_right.jpg');background-repeat: repeat-x;"><img src="images/top_logo_2.jpg" alt="logo" width="400" height="80" /></td>
            </tr>
        </table>
    </td>
    <td width="67%" valign="top" style="background-image: url('images/top_bg_right.jpg');background-repeat: repeat-x;">
    <table width="100%" height="80" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
        	<table border="0" cellpadding="0" cellspacing="0" >
          <tr>
          	<%for(int i =0 ; i < menuRoot.size(); i++){
          		
          		%>
          		<td width="1px"></td>
            	<td><div onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('<%=menuRoot.get(i).getCh_desc()%>','<%=menuRoot.get(i).getId()%>','<%=menuRoot.get(i).getImg_url()%>b.jpg',1)"><a href="<%=rootPath%>/<%=menuRoot.get(i).getUrl() %>" target=mainFrame><img src="<%=menuRoot.get(i).getImg_url()%>a.jpg" title="<%=menuRoot.get(i).getCh_desc()%>" name="<%=menuRoot.get(i).getCh_desc()%>" height="30" border="0" id="<%=menuRoot.get(i).getCh_desc()%>" /></a></div></td>
          		<%
          		
          	} %>
          	<tr><td><div id="div1"></div></td>
          	
          	</tr>
        </table></td>
        <td width="7%" valign="top" style="background-image: url('images/top_bg_right.jpg');background-repeat: repeat-x;">
			<input type="image" value="退出" onclick="doQuit()" alt="退出按钮" src="<%=rootPath%>/common/images/login_out.png" width="20px" height="24px">
		</td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<!-- 
<map name="Map" id="Map"><area shape="rect" coords="32,9,52,27" onclick="initWindow()" alt="声音窗口"/><area shape="rect" coords="70,8,92,27" onclick="toHome()" alt="返回首页"/><area shape="rect" coords="109,8,128,27" onclick="doQuit()" alt="退出按钮"/></map> -->
</body>
</html>
