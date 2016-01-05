<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.common.util.CreatePiePicture"%>

<%
  String rootPath = request.getContextPath();
  String _flag = (String)request.getAttribute("flag");
  DBVo vo  = (DBVo)request.getAttribute("db");
  String myip = vo.getIpAddress();
  String myport = vo.getPort();
  String myUser = vo.getUser();
  String myPassword = EncryptUtil.decode(vo.getPassword());
  String dbPage = "oraclespace";
        
	double avgpingcon = (Double)request.getAttribute("avgpingcon");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
		
	String id = (String)request.getAttribute("id");
  	String mysid = (String)request.getAttribute("sid");
	
	
	String lstrnStatu = (String)request.getAttribute("lstrnStatu");
	if("READ WRITE".equalsIgnoreCase(lstrnStatu)){lstrnStatu="������";}else{lstrnStatu="δ����";}
	Hashtable isArchive_h = (Hashtable)request.getAttribute("isArchive_h"); 
	String created = "";
	if(isArchive_h!=null){created=(String)isArchive_h.get("CREATED");}
	Hashtable memPerfValue = (Hashtable)request.getAttribute("memPerfValue"); 
	if(memPerfValue == null)memPerfValue = new Hashtable();
	String buffercache = "0";

String[] sysItem3={"HOST_NAME","DBNAME","VERSION","INSTANCE_NAME","STATUS","STARTUP_TIME","ARCHIVER"};
String[] sysItemch3={"��������","DB ����","DB �汾","������","����״̬","���̿�ʼʱ��","�鵵��־ģʽ"};


  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"������","��������","��ǰ������","���������"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"�豸��","�豸����ʱ��","�豸��ϵ","�豸λ��","�豸����","�豸����"};
//Hashtable hash = (Hashtable)request.getAttribute("hash");
//Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
//Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
String chart1 = (String)request.getAttribute("chart1");
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");
Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");
Hashtable dbio = (Hashtable)request.getAttribute("dbio");
  
  
  Hashtable orasys = (Hashtable)request.getAttribute("sysvalue");
  Hashtable cursors = (Hashtable)request.getAttribute("cursors");
if(cursors == null)cursors = new Hashtable();
     			  	   
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<%
 int intbuffercache = 0;
 String opencurstr = "0";
 String dictionarycache = "0";
 String librarycache = "0";
 String pctmemorysorts = "";
 String pctbufgets = "0";
 String unpctmemorysorts = "0";
 String undictionarycache = "0";
 String unlibrarycache = "0";
 String unbuffercache = "0";
 String unpctbufgets = "0";
	if(memPerfValue!=null){
		if(memPerfValue.containsKey("buffercache") &&  memPerfValue.get("buffercache") != null)
			buffercache = (String)memPerfValue.get("buffercache");
																	                  		
		try{
			intbuffercache = Math.round(Float.parseFloat(buffercache));
		}catch(Exception e){
			e.printStackTrace();
		}
																
		if(memPerfValue.containsKey("dictionarycache") &&  memPerfValue.get("dictionarycache") != null)
			dictionarycache = (String)memPerfValue.get("dictionarycache");													
		if(memPerfValue.containsKey("librarycache") &&  memPerfValue.get("librarycache") != null)
			librarycache = (String)memPerfValue.get("librarycache");	
		if(memPerfValue.containsKey("pctmemorysorts") &&  memPerfValue.get("pctmemorysorts") != null)
			pctmemorysorts = (String)memPerfValue.get("pctmemorysorts");
		if(memPerfValue.containsKey("pctbufgets") &&  memPerfValue.get("pctbufgets") != null)
			pctbufgets = (String)memPerfValue.get("pctbufgets");
		if(cursors.containsKey("opencur") &&  cursors.get("opencur") != null)
			opencurstr = (String)cursors.get("opencur");
		if(pctmemorysorts.equals(""))
			pctmemorysorts = "0";
		if (pctmemorysorts == null || pctmemorysorts.equals("") || "null".equals(pctmemorysorts))
			pctmemorysorts = "0";
		unpctmemorysorts = String.valueOf(100 - Math.round(Float
				.parseFloat(pctmemorysorts)));
		if (dictionarycache.equals("") || "null".equals(dictionarycache))
			dictionarycache = "0";
		undictionarycache = String.valueOf(100 - Integer
				.parseInt(dictionarycache));
		if (librarycache.equals("") || "null".equals(librarycache))
			librarycache = "0";
		unlibrarycache = String.valueOf(100 - Integer
				.parseInt(librarycache));
		if (buffercache.equals("") || "null".equals(buffercache))
			buffercache = "0";
		unbuffercache = String.valueOf(100 - Integer
				.parseInt(buffercache));
		if (pctbufgets.equals("") || "null".equals(pctbufgets))
			pctbufgets = "0";
		unpctbufgets = String.valueOf(100 - Double.valueOf(pctbufgets));
	}
	
	
	String picip = CommonUtil.doip(myip);
	
    	//���ɵ���ƽ����ͨ��ͼ��
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createAvgPingPic(picip,avgpingcon); 
	
       //����������
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createpubliccpuPic(picip,intbuffercache,"����������");
	
	StringBuffer dataStr = new StringBuffer();
		dataStr.append("��ͨ;").append(Math.round(avgpingcon)).append(
				";false;7CFC00\\n");
		dataStr.append("δ��ͨ;").append(100 - Math.round(avgpingcon))
				.append(";false;FF0000\\n");
		String avgdata = dataStr.toString();
	
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />



		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script>
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
  
// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
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
	go();
	setClass();
}

function setClass(){
	document.getElementById('oraDetailTitle-5').className='detail-data-title';
	document.getElementById('oraDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('oraDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		document.getElementById("sid").value="<%=request.getAttribute("sid")%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
		<script type="text/javascript">
        var xmlHttp;
        var bar_color = '#00FA9A';      /*��������ɫ*/
        var span_id = "block";
        var clear = "&nbsp;&nbsp;&nbsp;"

        function createXMLHttpRequest() {
            if (window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            else if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();                
            }
        }

        function go() {     /*����ProgresBarJsp.jsp*/
            createXMLHttpRequest();
            checkDiv();
            var url = "<%=rootPath%>/application/db/ProgressBarJsp.jsp?task=create";
            var button = document.getElementById("go");
            button.disabled = true;
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = goCallback;     //������Ӧ�󣬵��õ�goCallback����
            xmlHttp.send(null);
        }

        function goCallback() {     
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    setTimeout("pollServer()", 500);
                }
            }
        }
        
        function pollServer() {
            createXMLHttpRequest();
            var url = "<%=rootPath%>/application/db/ProgressBarJsp.jsp?task=poll";
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = pollCallback;
            xmlHttp.send(null);
        }
        
        function pollCallback() {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    var percent_complete = xmlHttp.responseXML.getElementsByTagName("percent")[0].firstChild.data;                 
                    var index = processResult(percent_complete);                  
                    for (var i = 1; i <= index; i++) {
                        var elem = document.getElementById("block" + i);
                        if(i != 5){                        
                        elem.innerHTML = clear;
                        }
                        elem.style.backgroundColor = bar_color;
                        
                        //var next_cell = i + 1;
                        //if (next_cell > index && next_cell <= 9) {
                        //    document.getElementById("block" + next_cell).innerHTML = percent_complete + "%";
                        //}
                    }                    
                    if (index < 9) {
                        setTimeout("pollServer()", 500);   //ÿ������ִ��һ��pollServer����������ִֻ��һ��
                    } else {
                        setTimeout("go()", 500);            //��1����������һ��go�������ﵽѭ����ʾ��Ŀ��
                        //document.getElementById("complete").innerHTML = "Complete!";
                        //document.getElementById("go").disabled = false;
                    }
                }
            }
        }
        
        function processResult(percent_complete) {
            var ind;
            if (percent_complete.length == 2) {
                ind = percent_complete.substring(0, 1);   /*ȡ��λ���ĵ�һλ��*/
            } else {
                ind = 9;
            }
            return ind;
        }

        function checkDiv() {
            var progress_bar = document.getElementById("progressBar");
            //var rightbar = document.getElementById("rightBar");           
            if (progress_bar.style.visibility == "visible") {
                clearBar();
                document.getElementById("complete").innerHTML = "";
            } else {
                progress_bar.style.visibility = "visible";
                //rightbar.style.visibility = "visible";               
                //document.getElementById("rightb").style.backgroundColor = "#ECECEC";              
            }
        }
        
        function clearBar() {
            for (var i = 1; i < 10; i++) {
                var elem = document.getElementById("block" + i);
                elem.innerHTML = clear;
                elem.style.backgroundColor = "#ececec";                
                if(i == 5)
                {
                   elem.innerHTML = "<%=pctbufgets%>%";
                }
            }
        }
    </script>
   
   
 <script>
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=id%>&nowtime="+(new Date()),
			success:function(data){
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "ƽ����ͨ��" });
				$("#public").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>public.png?nowtime="+(new Date()), alt: "����������" }); 
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
</script>



	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="orderflag">
			<input type=hidden name="id">
			<input type="hidden" name="sid" id="sid" />
			<table id="body-container" class="body-container">
				<tr>
					<!-- ���ҽṹ421-->
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
								<td class="td-container-main-application-detail">
									<table>
										<!-- ���½ṹ -->
										<tr>
											<td>
												<!-- !!!!!! -->
												<table>
													<tr>
														<td width=85%>
															<table id="container-main-application-detail"
																class="container-main-application-detail">
																<tr> 
																	<td>
																		<jsp:include page="/topology/includejsp/db_oracle.jsp">
																			<jsp:param name="Id" value="<%=vo.getId()%>" />
																			<jsp:param name="IpAddress" value="<%=vo.getIpAddress()%>" />
																			<jsp:param name="_flag" value="<%=_flag%>" />
																			<jsp:param name="Alias" value="<%=vo.getAlias()%>" />
																			<jsp:param name="Port" value="<%=vo.getPort()%>" />
																			<jsp:param name="DbName" value="<%=vo.getDbName()%>" />
																			<jsp:param name="dbtye" value="<%=dbtye%>" />
																			<jsp:param name="runstr" value="<%=runstr%>" />
																			<jsp:param name="created" value="<%=created%>" />
																			<jsp:param name="buffercache" value="<%=buffercache%>" />
																			<jsp:param name="lstrnStatu" value="<%=lstrnStatu%>" />
																			<jsp:param name="dictionarycache" value="<%=dictionarycache%>" />
																			<jsp:param name="unbuffercache" value="<%=unbuffercache%>" />
																			<jsp:param name="unpctmemorysorts" value="<%=unpctmemorysorts%>" />
																			<jsp:param name="librarycache" value="<%=librarycache%>" />
																			<jsp:param name="unlibrarycache" value="<%=unlibrarycache%>" />
																			<jsp:param name="pctmemorysorts" value="<%=pctmemorysorts%>" />
																			<jsp:param name="opencurstr" value="<%=opencurstr%>" />
																			<jsp:param name="picip" value="<%=picip%>" />
																			<jsp:param name="avgdata" value="<%=avgdata%>" />
																			<jsp:param name="memPerfValue" value="<%=memPerfValue%>" />
																			
																			<jsp:param name="sid"  value="<%=mysid %>" />
																		</jsp:include> 
																	</td>
																</tr>
															</table>
														</td>
														<td class="td-container-main-tool" width=15%>
															<jsp:include page="/include/dbtoolbar.jsp">
																<jsp:param value="<%=myip %>" name="myip" />
																<jsp:param value="<%=myport %>" name="myport" />
																<jsp:param value="<%=myUser %>" name="myUser" />
																<jsp:param value="<%=myPassword %>" name="myPassword" />
																<jsp:param value="<%=mysid  %>" name="sid" />
																<jsp:param value="<%=id %>" name="id" />
																<jsp:param value="<%=dbPage %>" name="dbPage" />
																<jsp:param value="Oracle" name="subtype"/>
															</jsp:include>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="container-main-application-detail"
													class="container-main-application-detail">
													<tr>
														<td width=70%>
															<table id="application-detail-content"
																class="application-detail-content">
																<tr>
																	<td>
																		<table id="application-detail-data"
																			class="application-detail-data">
																			<tr>
																				<td class="detail-data-header">
																					<%=oraDetailTitleTable%>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<table class="application-detail-data-body">
																						<tr>
																							<td>
																								<table width="100%" border="0" cellspacing="0"
																									cellpadding="0">

																									<tr height="28" bgcolor="#ECECEC">
																										<td align=center>
																											&nbsp;
																										</td>
																										<td align=center>
																											�ļ���
																										</td>
																										<td align=center>
																											��ռ�
																										</td>
																										<td align=center>
																											�ռ��С��MB��
																										</td>
																										<td align=center>
																											���д�С��MB��
																										</td>
																										<td align=center>
																											���б���
																										</td>
																										<td align=center>
																											�����
																										</td>
																										<td align=center>
																											������
																										</td>
																										<td align=center>
																											����д
																										</td>
																										<td align=center>
																											�����д
																										</td>
																										<td align=center>
																											�ļ�״̬
																										</td>

																									</tr>
																									<%
				for(int i=0;i<tableinfo_v.size();i++){
					Hashtable ht = (Hashtable)tableinfo_v.get(i);
					String filename = ht.get("file_name").toString();
					String tablespace = ht.get("tablespace").toString();
					String size = ht.get("size_mb").toString();
					String free = ht.get("free_mb").toString();
					String percent = ht.get("percent_free").toString();
					String status = ht.get("status").toString();
					String pyr = "";
					String pbr = "";
					String pyw = "";
					String pbw = "";
					if(dbio.containsKey(filename)){
						Hashtable iodetail = (Hashtable)dbio.get(filename);
						if(iodetail != null && iodetail.size()>0){
							pyr = (String)iodetail.get("pyr");
							pbr = (String)iodetail.get("pbr");
							pyw = (String)iodetail.get("pyw");
							pbw = (String)iodetail.get("pbw");
						}
					}
										
			%>
																									<tr height="28" bgcolor="#FFFFFF"
																										<%=onmouseoverstyle%>>
																										<td class="application-detail-data-body-list"
																											align=center><%=i+1%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=filename%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=tablespace%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=size%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=free%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=percent%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=pyr%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=pbr%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=pyw%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=pbw%></td>
																										<td class="application-detail-data-body-list"
																											align=center>
																											&nbsp;<%=status%></td>
																									</tr>
																									<%}%>
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
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
		<script>
			
			Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/oracle.do?action=sychronizeData&dbPage=<%=dbPage %>&id=<%=id %>&myip=<%=myip %>&myport=<%=myport %>&myUser=<%=myUser %>&myPassword=<%=myPassword %>&sid=<%=mysid %>&flag=1";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>