<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.util.IpTranslation"%>
<%
  String rootPath = request.getContextPath();
  IpTranslation transfer=new IpTranslation();
  List list = (List)request.getAttribute("list");
  if(list == null)list = new ArrayList();
    int rc = list.size();
    JspPage jp = (JspPage)request.getAttribute("page");
    String ipaddress = (String)request.getAttribute("ipaddress");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
   var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/oraspace.do?action=delete";
  var listAction = "<%=rootPath%>/oraspace.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/db.do?action=ready_add";
     mainForm.submit();
  }
  function createSpaceConfig()
  {
     mainForm.action = "<%=rootPath%>/oraspace.do?action=createspaceconfig";
     mainForm.submit();
  }
  function search()
  {
     mainForm.action = "<%=rootPath%>/oraspace.do?action=search";
     mainForm.submit();
  }  
</script>
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var dbid="";
	var operate="";
	var sid="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip,db)
	{	
		ipaddress=ip;
		node=nodeid;
		dbid=db;
		//operate=oper;
		if(ip.indexOf(":")!=-1){
		  sid=ip.split(":")[1];
		}
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
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
	    location.href="<%=rootPath%>/db.do?action=check&id="+dbid+"&sid="+sid;
	}
	function edit()
	{
		location.href="<%=rootPath%>/oraspace.do?action=ready_edit&id="+node+"&sid="+sid;
	}	
	function clickMenu()
	{
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

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">������Ϣ</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>             		           				
		
		
		</td>
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">Ӧ�� >> ���ݿ���� >> ORACLE�澯����</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;��ѯIP��ַ��
		    	<select name="ipaddress" >
		    	<%
		    		List iplist = (List)request.getAttribute("iplist");
		    		if (iplist != null && iplist.size()>0){
		    			for(int i=0;i<iplist.size();i++){
		    				String ip = (String)iplist.get(i);
		    				if (ip.equals(ipaddress)){
		    	%>
		    				<option value='<%=ip%>' selected><%=ip%></option>		    	
			<%		    				
						}else{
			%>
						<option value='<%=ip%>'><%=ip%></option>
			<%						
						}
		    			}
		    		}
		    	%>
		    	</select>   
		    	&nbsp;&nbsp;<input type="button" value="����" onclick="search()" class="button">     							
          							</td>            
								<td bgcolor="#ECECEC" width="50%" align='right'>
									
									<a href="#" onclick="createSpaceConfig()">ˢ�±�ռ䷧ֵ</a>&nbsp;&nbsp;&nbsp;
									</td>
										</tr>
								</table>
		  						</td>
									</tr>
								</table>
		  						</td>                       
        						</tr>

						    <tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td>
        </tr>
		</table>
		</td>
		</tr>         										
				
		<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
    					<th width='5%'>���</th>
    					<th width='20%'>IP��ַ</th>
    					<th width='20%'>��ռ�����</th>
    					<th width='20%'>�澯ֵ</th>
    					<th width='10%'>�Ƿ�澯</th>
    					<th width='10%'>��ע</th>
    					<th width='15%'>����</th>
</tr>
<%
       DBDao dbdao = new DBDao();
       DBTypeDao typedao = new DBTypeDao();
       DBTypeVo typevo = typedao.findByDbtype("oracle");
	
if(rc > 0){
    for(int i=0;i<rc;i++)
    {  OraclePartsDao oracleDao=new OraclePartsDao();
    try{
       Oraspaceconfig vo = (Oraspaceconfig)list.get(i);
       String flag = "�澯";
       if(vo.getSms() == 0)flag = "δ�澯";
       dbdao = new DBDao();
      
       String ipAddr=vo.getIpaddress();
       String []ipArr=ipAddr.split(":");
       String []ips=transfer.getIpFromHex(ipArr[0]);
	   List shareList = dbdao.getDbByTypeAndIpaddress(typevo.getId(), ips[0]+"."+ips[1]+"."+ips[2]+"."+ips[3]);
	  // oracleDao
	   if(shareList == null || shareList.size()==0)continue;
	   DBVo dbvo = (DBVo)shareList.get(0);
	   OracleEntity ora=(OracleEntity)oracleDao.getOracleById(Integer.parseInt(ipArr[1]));
       
%>
       <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook">
    		<td height=25>&nbsp;<font color='blue'><%=1 + i%></font></td>
		<td height=25>&nbsp;<%=ips[0]+"."+ips[1]+"."+ips[2]+"."+ips[3]+":"+ora.getSid()%></td> 
		<td height=25>&nbsp;<%=vo.getSpacename()%></td> 
		<td height=25>&nbsp;<%=vo.getAlarmvalue()%></td> 
		<td height=25>&nbsp;
			<%
				if(vo.getSms()==0){
			%>
			<a href="<%=rootPath%>/oraspace.do?action=addalert&id=<%=vo.getId()%>"><font color=#397DBD>�澯</font></a>
			<%
				}else{
			%>
			<a href="<%=rootPath%>/oraspace.do?action=cancelalert&id=<%=vo.getId()%>"><font color=#397DBD>ȡ���澯</font></a>
			<%	}
			%>
		
		</td> 
		<td height=25>&nbsp;<%=vo.getBak()%></td>				
		<td height=25>&nbsp;
		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpaddress()%>','<%=dbvo.getId()%>')>
		</td>
  	</tr>
<%	}catch(Exception e){
 // e.printStatckTrace();
 System.out.println(e);
}finally{
  oracleDao.close();
  dbdao.close();
}}
}


%>				
			</table>
			</td>

		</tr>
		<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
                  </tr>
              </table></td>
            </tr>	
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
