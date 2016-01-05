<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.temp.model.TomcatNodeTemp"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>


<%
    String rootPath = request.getContextPath();
    String menuTable = (String) request.getAttribute("menuTable");//�˵�
    NodeDTO node = (NodeDTO) request.getAttribute("node");
    Tomcat tomcat = (Tomcat) request.getAttribute("vo");
    TomcatNodeTemp tomcatNodeTemp = (TomcatNodeTemp) request.getAttribute("tomcatNodeTemp");
    String maxAlarmLevel = (String) request
            .getAttribute("maxAlarmLevel"); // λ��
    String curJVMImg = (String) request.getAttribute("curJVMImg");
    String ipaddress = node.getIpaddress();
    String nodeid = node.getNodeid();
    String type = node.getType();
    String subtype = node.getSubtype();
    String name = node.getName(); // ����
    String port = tomcat.getPort();
    String tomcatVersion = tomcat.getVersion();
    String JVMVersion = tomcat.getJvmversion();
    String JVMVender = tomcat.getJvmvender();
    String OSName = tomcat.getOs();
    String OSVersion = tomcat.getOsversion();
    String ping = tomcatNodeTemp.getPing();
%>
<html>
    <head>


        <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
        <script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
        <script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

        <link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css" />

        <link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
        <script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
        <script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/detail/net/netdetail.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/NetRemoteService.js"></script>


        <script src="<%=rootPath%>/include/AC_OETags.js" language="javascript"></script>
        <script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

        <script>

/*
*��ʾ�ؼ��˿�
*/
function showPort(){
	var showAllPortFlag = document.getElementById("allportflag").value;
	mainForm.action = "<%=rootPath%>/topology/network/networkview.jsp?id=<%=nodeid%>&flag=<%=flag%>&showAllPortFlag="+showAllPortFlag;
    mainForm.submit();
}

function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
</script>
        <script language="javascript">	


  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function toGetConfigFile()
  {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=ipaddress%>";
        mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
function changeOrder(para){
    var orderflag = document.getElementById("orderflag");
    orderflag.value = para;
    submitForm();
} 

function changePort(para){
    var important = document.getElementById("important");
    important.value = para;
    submitForm();
}

function submitForm() {
    mainForm.action = "<%=rootPath%>//netData.do?action=getInterfaceInfo";
    mainForm.submit();
}

function openwin3(operate,index,ifname)
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=nodeid%>&ipaddress=<%=ipaddress%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
} 

//function modifyIpAlias(ipalias){
	//window.alert(ipalias);
	//document.getElementById('ipalias').innerHTML = ipalias;
	//var ipaddress = document.getElementById('ipaddress');
	//�첽���ú�̨���� DWR
//	IpAliasRemoteManager.modifyIpAlias(ipaddress,ipalias);
//}

//�����豸��ip��ַ
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("�޸ĳɹ���");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
//setInterval(modifyIpAliasajax,60000);
});

</script>
        <script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"11111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
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
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
       	    // alert("<%=nodeid%>"+"****"+"<%=ipaddress%>"+"****"+node+"******"+ipaddress);
	    window.open ("<%=rootPath%>/monitor.do?action=show_utilhdx&id=<%=nodeid%>&ipaddress=<%=ipaddress%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=350, width=840, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=ipaddress%>&ifindex="+node, "newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=no, location=yes, status=yes");
		//window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}
	function realtimeMonitor()
	{
		window.open ('<%=rootPath%>/monitor.do?action=portdetail&id=<%=nodeid%>&ip=<%=ipaddress%>&ifindex='+node,'�˿�����','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function bandwidth()
	{
		window.open ('<%=rootPath%>/monitor.do?action=bandwidthdetail&id=<%=nodeid%>&ip=<%=ipaddress%>&ifindex='+node,'����','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function portstatus()
	{
		window.open ('<%=rootPath%>/portconfig.do?action=showPortStatus&index='+node+'&ip=<%=ipaddress%>','�˿�״̬','top=200,left=300,height=435,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
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
	setClass();
}

function setClass(){
	document.getElementById('tomDetailTitle-0').className='detail-data-title';
	document.getElementById('tomDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('tomDetailTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}



</script>
    </head>
    <body id="body" class="body" onload="initmenu();">
        <!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
        <div id="itemMenu" style="display: none";>
            <table border="1" width="100%" height="100%" bgcolor="#F1F1F1" style="border: thin; font-size: 12px" cellspacing="0">
                <tr>
                    <td style="cursor: default; border: outset 1;" align="center" onclick="parent.detail()">
                        �鿴״̬
                    </td>
                </tr>

                <tr>
                    <td style="cursor: default; border: outset 1;" align="center" onclick="parent.portset();">
                        �˿�����
                    </td>
                </tr>
                <tr>
                    <td style="cursor: default; border: outset 1;" align="center" onclick="parent.realtimeMonitor();">
                        ʵʱ���
                    </td>
                </tr>

                <tr>
                    <td style="cursor: default; border: outset 1;" align="center" onclick="parent.bandwidth();">
                        ����
                    </td>
                </tr>
                <tr>
                    <td style="cursor: default; border: outset 1;" align="center" onclick="parent.portstatus();">
                        �˿�״̬
                    </td>
                </tr>
            </table>
        </div>
        <!-- �Ҽ��˵�����-->
        <form id="mainForm" method="post" name="mainForm">
            <input type=hidden name="flag" id="flag" value="<%=flag%>">
            <input type=hidden name="nodeid" id="nodeid" value="<%=nodeid%>">
            <input type=hidden name="type" id="type" value="<%=type%>">
            <input type=hidden name="subtype" id="subtype" value="<%=subtype%>">
            <table id="body-container" class="body-container">
                <tr>
                    <td class="td-container-menu-bar">
                        <table id="container-menu-bar" class="container-menu-bar">
                            <tr>
                                <td>
                                    <%=menuTable%>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="td-container-main">
                        <table id="container-main" class="container-main">
                            <tr>
                                <td class="td-container-main-detail" width=85%>
                                    <table id="container-main-detail" class="container-main-detail">
                                        <tr>
                                            <td>
                                                <jsp:include page="/topology/includejsp/middleware_tomcat.jsp">
												    <jsp:param name="nodeid" value="<%=nodeid%>"/>
                                                    <jsp:param name="name" value="<%=name%>"/> 
                                                    <jsp:param name="alarmLevel" value="<%=maxAlarmLevel%>"/> 
                                                    <jsp:param name="ipaddress" value="<%=ipaddress%>"/> 
                                                    <jsp:param name="port" value="<%=port%>"/> 
                                                    <jsp:param name="tomcatVersion" value="<%=tomcatVersion%>"/> 
                                                    <jsp:param name="JVMVersion" value="<%=JVMVersion%>"/> 
                                                    <jsp:param name="JVMVender" value="<%=JVMVender%>"/> 
                                                    <jsp:param name="OSName" value="<%=OSName%>"/> 
                                                    <jsp:param name="OSVersion" value="<%=OSVersion%>"/> 
                                                    <jsp:param name="ping" value="<%=ping%>"/> 
                                                    <jsp:param name="curJVMImg" value="<%=curJVMImg%>"/> 
                                                </jsp:include>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table id="detail-data" class="detail-data">
                                                    <tr>
                                                        <td class="detail-data-header">
                                                            <%=tomDetailTitleTable%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table class="detail-data-body">
                                                                <tr>
                                                                    <td>
                                                                        <table width="100%">
                                                                            <tr>
                                                                                <td align=center>
                                                                                    <table cellpadding="0" cellspacing="0">
                                                                                        <tr>
                                                                                            <td width="100%">
                                                                                                <div id="flashcontent1">
                                                                                                    <strong>You need to upgrade your Flash Player</strong>
                                                                                                </div>
                                                                                                <script type="text/javascript">
                                                                                                    var so = new SWFObject("<%=rootPath%>/flex/Tomcat_Ping.swf?ipadress=<%=nodeid%>", "Tomcat_Ping", "346", "250", "8", "#ffffff");
                                                                                                    so.write("flashcontent1");
                                                                                                </script>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                                <td align=center>
                                                                                    <table cellpadding="0" cellspacing="0">
                                                                                        <tr>
                                                                                            <td width="100%">
                                                                                                <div id="flashcontent2">
                                                                                                    <strong>You need to upgrade your Flash Player</strong>
                                                                                                </div>
                                                                                                <script type="text/javascript">
                                                                                                    var so = new SWFObject("<%=rootPath%>/flex/Tomcat_JVM_Memory.swf?ipadress=<%=nodeid%>", "Tomcat_JVM_Memory", "346", "250", "8", "#ffffff");
                                                                                                    so.write("flashcontent2");
                                                                                                </script>
                                                                                            </td>
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
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign=top>
                					<jsp:include page="/include/tomcattoolbar.jsp">
                						<jsp:param value="<%=nodeid%>" name="nodeid" />
                					</jsp:include>
                				</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </BODY>
</HTML>