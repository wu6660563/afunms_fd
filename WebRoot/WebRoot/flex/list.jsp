<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.ChartXml"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%         
  String rootPath = request.getContextPath();
  //User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  //HostNodeDao hostNodeDao =new HostNodeDao();
  //List hostList = new ArrayList();
	//	if(current_user.getRole() == 0){
			//hostList = hostNodeDao.loadByPerAll("-1");
		//}else {
		//	hostList = hostNodeDao.loadByPerAll(current_user.getBusinessids());
	//		}
  //try{
		//ChartXml chartxml;
		//chartxml = new ChartXml("equip");
	//	chartxml.addequipXML(hostList);
  //}catch(Exception e){
	//	e.printStackTrace();   	
 // }
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>  
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="history/history.css" />
<script src="AC_OETags.js" language="javascript"></script>
<script src="history/history.js" language="javascript"></script>
<script type="text/javascript" src="swfobject.js"></script>
<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 124;
// -----------------------------------------------------------------------------
// -->
</script>
<!--  END Browser History required section -->
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
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
//��Ӳ˵�	
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

function listShow(){
    //history.back(1);
    window.location = "<%=rootPath%>/network.do?action=list&jp=1";
}
function toadd(){
    window.location = "<%=rootPath%>/network.do?action=ready_add";
}
</script>

</head>
<BODY leftmargin="0" topmargin="0" onload="initmenu();">
<table id="table1" width=100%>
	<tr>
		<td width="200" valign=top align=center>
	        <%=menuTable%>       				
	    </td>
		<td align="center" valign=top width=1000>
			<table width="100%" rules=none align=center>
			    <tr>
			        <td bgcolor="#FFFFFF" align=right>
			        <INPUT type="button" class="formStyle" value="�б�չʾ" onclick="listShow();">
			        <INPUT type="button" class="formStyle" value="���" onclick="toadd();">
			        </td>
			    </tr>
		     	<tr>
					<td height=620 bgcolor="#FFFFFF" valign="top" align=center>
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">			
							<tr>
								<td colspan="1">
									 <div id="flashcontent"><strong>You need to upgrade your Flash Player</strong></div>
								</td>
							</tr>	
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<script language="JavaScript" type="text/javascript">
<!--
// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
/*var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

if ( hasProductInstall && !hasRequestedVersion ) {
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "playerProductInstall",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
		"width", "83%",
		"height", "100%",
		"align", "right",
		"id", "flexid",
		"quality", "high",
		"bgcolor", "#869ca7",
		"name", "flexstore",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
} else if (hasRequestedVersion) {
	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed
	AC_FL_RunContent(
			"src", "flexstore",
			"width", "83%",
			"height", "100%",
			"align", "right",
			"id", "flexid",
			"quality", "high",
			"bgcolor", "#869ca7",
			"name", "flexstore",
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }*/
// -->
//swfobject.embedSWF("flexstore.swf", "flashcontent", "100%", "100%", "9.0.0", "#336699"); 
var so = new SWFObject("flexstore.swf", "flexstore", "840", "620", "9", "#ffffff");
so.write("flashcontent");
</script>
<noscript>
 	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		id="flexstore" width="100%" height="100%"
		codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
		<param name="movie" value="flexstore.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="#869ca7" />
		<param name="allowScriptAccess" value="sameDomain" />
		<embed src="flexstore.swf" quality="high" bgcolor="#869ca7"
			width="100%" height="100%" name="flexstore" align="middle"
			play="true"
			loop="false"
			quality="high"
			allowScriptAccess="sameDomain"
			type="application/x-shockwave-flash"
			pluginspage="http://www.adobe.com/go/getflashplayer">
		</embed>
    </object>
</noscript>
</BODY>
</HTML>
