<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%
  String rootPath = request.getContextPath(); 
  String treeFlag = request.getParameter("treeflag");
  if(treeFlag==null) treeFlag = "0";
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<SCRIPT type=text/javascript>
//<!--
ygagt=navigator.userAgent.toLowerCase();ygd=document;ygdom=(document.getElementById)?1:0;ygns=(ygd.layers)?1:0;ygns6=(ygdom&&navigator.appName=="Netscape");ygie=(ygd.all);ygwin=((ygagt.indexOf("win")!=-1)||(ygagt.indexOf("16bit")!=-1));ygmac=(ygagt.indexOf("mac")!=-1);ygnix=((ygagt.indexOf("x11")!=-1)||(ygagt.indexOf("linux")!=-1));
var ygar = new Array();
ygar[0]="<style type=\"text/css\">\n";
// keywords: NN4 PC - 96dpi
if (ygns && ygwin){
ygar[1]="th,td{font-size:9pt;}";
ygar[2]="input{font-family:monospace;font-size:12px;}";
ygar[3]="big{font-size:10pt;line-height:1.5;}";
ygar[4]="p{font-size:12pt;line-height:1.5;}";
ygar[5]="small{font-size:9pt;}";}
// keywords: NN4 Mac - 72dpi
else if (ygns && ygmac){
ygar[1]="body,th,td{font-size:medium;}";
ygar[2]="big{font-size:big;}";
ygar[3]="p{font-size:medium;}";
ygar[4]="small{font-size:small;}";}
// keywords: NN4 *nix - 72dpi
else if (ygns && ygnix){
ygar[1]="body,th,td{font-size:small;}";
ygar[2]="input,select{line-height:7px;font-family:monospace;font-size:small;}";
ygar[3]="big{font-size:110%;}";
ygar[4]="p{font-size:medium;}";
ygar[5]="small{font-size:small;}";}
// pixels: NN6 PC/Mac, IE Mac
else if (ygns6||(ygie && ygmac)){
ygar[1]="body,table{font-size:12px;}";
ygar[2]="tr,th,td{font-size:12px;line-height:16px;}";
ygar[3]="big{font-size:14px;line-height:18px;}";
ygar[4]="p{font-size:14px;line-height:18px;}";
ygar[5]="small{font-size:12px;}";}
// percentages: IE PC
else{
ygar[1]="body,th,td,big{font-family:arial,helvetica,sans-serif;font-size:12px;line-height:1.5;}";
ygar[2]="td{line-height:1.25em;}";
ygar[3]="small{font-size:11px;line-height:1.5em;}";}
ygar[4]="big{font-size:14px;line-height:1.3;}";
ygar[5]="input,select{font-family:arial,helvetica,sans-serif;;font-size:12px;line-height:1.2;}";
var ygarjoin = ygar.join('');
ygd.write (ygarjoin);	
ygd.write(".ygcw{color:white;}.ygcb{color:black;}.ygfa{font-family:arial,sans-serif;}.ygfv{font-family:verdana,arial,sans-serif;}.ygft{font-family:times,serif;}.ygtb{font-size:18px;}.ygtbw{font-size:18px;color:white;}\n-->\n<\/style>");
function ImgResetSize(img)
{
    if(img.width > 450)
           img.width = "450";
    return true;
}
// -->
</SCRIPT>
<style>
<!--
.spcr{margin-bottom:10px}
.spcr2{margin-left:5px}
.line1{border:1px solid #76B0D6}
.line2{border-bottom:1px dashed #bfbfbf}
.line3{border:1px solid #B1E397}
.line4{border:1px solid #bfbfbf}
.f14{font-size:14px; line-height:22px}
.time{font-size:10px; font-family:artal; color:#999999}
.link1 {color:#333333}
.link2{color:#2667CA;text-decoration: none;cursor:hand;}
a.link2:hover{text-decoration: underline;}
a.link2:visited{text-decoration: none;}
.nav1{border-left:1px solid #B2E398;border-bottom:1px solid #76B0D6; background-color:#F4FFE5}
.nav2{border-right:1px solid #B2E398;border-bottom:1px solid #76B0D6; background-color:#F4FFE5}
.nav3{border-left:1px solid #B2E398; background-color:#F4FFE5}
.nav4{border-right:1px solid #B2E398;background-color:#F4FFE5}
.box1{border:1px solid #77B0D6; background-color:#F5FCFF}
.red{color:#ff6600}
.red2{color:#FF6600;font-family:????; font-size:20px; line-height:22px}

body {margin-left: 6px;margin-top: 0px;margin-right: 6px;margin-bottom: 6px;
scrollbar-face-color: #E0E3EB;
scrollbar-highlight-color: #E0E3EB;
scrollbar-shadow-color: #E0E3EB;
scrollbar-3dlight-color: #E0E3EB;
scrollbar-arrow-color: #7ED053;
scrollbar-track-color: #ffffff;
scrollbar-darkshadow-color: #9D9DA1;
}
body,td,th {color: #666666;line-height:20px}

</style>
<SCRIPT language="javascript">
pic1 = new Image
pic1.src = "<%=rootPath%>/resource/image/sub.gif"
pic2 = new Image
pic2.src = "<%=rootPath%>/resource/image/plus.gif"

function img_changer(ImgName, changepic) {
	document.images[ImgName].src = eval(changepic + ".src")
}
 
function initIts(){
	divColl=document.all.tags("DIV");	
	for(i=1; i<divColl.length; i++) {
		img_changer('s' +i + 'Image', 'pic2');
		whichEl=divColl(i);
		if(whichEl.className=="child")
			whichEl.style.display="none";
	}
}

function initIts1(s_cur){
	s_num=0;
	divColl=document.all.tags("DIV"); 
	if(s_cur!="0")
		for(i=1; i<divColl.length; i++) {
			if(s_cur==eval("document.f.tree"+i+".value")){
				s_num=i;
				break;
			}
		}
	for(i=1; i<divColl.length; i++) {
		if(i!=s_num)
			img_changer('s' +i + 'Image', 'pic2');
		whichEl=divColl(i);
		if(whichEl.className=="child"&&i!=s_num)
			whichEl.style.display="none";
	}
}
function expands(el) {
	whichEl1=document.getElementById(el + "childs");
	if (whichEl1.style.display=="none"){
		whichEl1.style.display="";
		img_changer(el+'Image', 'pic1');
	}else{
		img_changer(el+'Image', 'pic2');
		whichEl1.style.display="none";
	}
}
//------------search one device-------------
function SearchNode(ip)
{
	var coor = window.parent.mainFrame.mainFrame.getNodeCoor(ip);
	if (coor == null)
	{
		var msg = "没有在图中搜索到IP地址为 " + ip + " 的设备。";
		window.alert(msg);
		return;
	}
	else if (typeof coor == "string")
	{
		window.alert(coor);
		return;
	}
	window.parent.mainFrame.mainFrame.moveMainLayer(coor);
}
//--------------------end--------------------
</SCRIPT>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
</head>
<BODY leftmargin="0" topmargin="0">
<TABLE cellSpacing=0 cellPadding=0 width='100%' bgColor=#f7f7f7 border=0 ID="Table1">
  <TBODY>
   <TR>
     <TD style="BORDER-RIGHT: #bfbfbf 1px solid; BORDER-TOP: #bfbfbf 1px solid; BORDER-BOTTOM: #bfbfbf 1px solid">
       <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0 ID="Table2">
         <TBODY>
        <TR>
            <TD height=8 align="center"><IMG height=2 src="<%=rootPath%>/resource/image/spc.gif"></TD></TR>     
<TR>
    <TD>
   </TD>
</TR>
<TR>
    <TD>
<TABLE cellSpacing="0" cellPadding="0" width="96%">
  <TBODY>
 <tr>
   <td>
  <table  width="100%" cellpadding="0" cellspacing="0">
   <tbody>
   <TR>
  <TD align="right" width="16"><IMG height="11" src="<%=rootPath%>/resource/image/plus.gif"   name = "s28Image" style="CURSOR: pointer" onclick = "javascript:expands('s28');" width="11"></TD>
  <TD width="16">
  <TABLE height="1" cellSpacing="0" cellPadding="0" width="100%" bgColor="#76b0d6" border="0">
  <TBODY>
  </TBODY>
  </TABLE>
  </TD>
  <TD width="20"><IMG height="16" src='<%=rootPath%>/resource/image/nodelist.png' width="17" align="absMiddle" style="CURSOR: pointer" onclick = "javascript:expands('s28');"></TD>
  <TD><A class="link2" style="CURSOR: pointer" onclick="javascript:expands('s28');"><B>服务器</B></a></TD> 
  </TR>
 </tbody>
 </table>
 </td>
 </tr>
  <tr id="s28childs" style="display:none;">
    <td>
  <table cellSpacing="0" cellPadding="0" >
              <tbody>
<%  
  List hostList = PollingEngine.getInstance().getNodeList();
  for(int i=0;i<hostList.size();i++)
  {
     Node host = (Node)hostList.get(i);
     if(host.getCategory()!=4) continue;
     
     String showName = null;
     if(treeFlag.equals("0"))
        showName = host.getAlias();
     else
        showName = host.getIpAddress();   
%>                
                <TR> 
                  <TD width="20"></TD>
                  <TD width="20"></TD>
                  <TD width="20" style="BORDER-LEFT: #76b0d6 1px solid"><IMG height="1" src="<%=rootPath%>/resource/image/line_1.gif" width="19"></TD>
                  <TD> 
<%
				out.println("<A class=link2 onclick=SearchNode('" + host.getIpAddress() + "')>");
%>                  
                    <img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(host.getStatus())%>" border=0>&nbsp;<%=showName%></A> 
                  </TD>
                </TR>
 <%}%>
              </tbody>
            </table>
 </td>
 </tr>
 </TBODY>
</TABLE>
   </TD>
</TR>
  
   </TD>
</TR><TR> <TD height=15></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>
		<CENTER></CENTER>
	</body>
</html>
