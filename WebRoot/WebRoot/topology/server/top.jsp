<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%
  String rootPath = request.getContextPath();   
%>
<html>
<head>
<title></title>
<script language="JavaScript">
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v3.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}
</script>
<script language="JavaScript" type="text/javascript">
  function doChange()
  {            
     if(mainForm.view_type.value==2)
     {
        parent.mainFrame.location = "services.jsp";
        hideTreeBN();
     }
     else if(mainForm.view_type.value==3)
     {
        parent.mainFrame.location = "<%=rootPath%>/server.do?action=list";   
        hideTreeBN();
     }
     else 
     {
      	parent.mainFrame.location="server.jsp"
      	showTreeBN();
     }
  }
  function showTreeBN()
  {
	document.all.treeBN.style.visibility = "visible";
  }
  function hideTreeBN()
  {
  	parent.search.cols='0,*';
	document.all.treeBN.style.visibility = "hidden";
  }  
  function cwin()
  {
     if(parent.search.cols!='220,*')
     {
        parent.search.cols='220,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/hide_menu.gif";
        document.all.dir.innerHTML="��������";
     }
     else
     {
        parent.search.cols='0,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/show_menu.gif";
        document.all.dir.innerHTML="��ʾ����";
     }
  }
</script>
</head>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/top.css" type="text/css">
<BODY leftmargin="0" topmargin="0">
<form method="POST" name="mainForm">
<table width="100%" align='left' bgcolor="#9FB0C4">
 <tr>
   <td width='10%'>
   <div id="treeBN" style="visibility:visible">
   <table>
	<tr><td background="<%=rootPath%>/resource/image/bg_03.gif" align="left" width='10%' onMouseUp="this.className='up'" onMouseDown="this.className='down'"
	    onMouseOver="this.className='up'" onMouseOut="this.className='m'" onClick=cwin() noWrap align=middle width=10>
	    <img id=pic height=16 hspace=4 src="<%=rootPath%>/resource/image/hide_menu.gif" width=16 align=absMiddle border=0><span id=dir>��ʾ����</span></td>
	   </tr>
	</table>   
   </div>     
   </td>   
   <td width='30%' align='center'>
   <b>������</b>(����:<%=PollingEngine.getInstance().getNodesTotal(4)%>)</td>
   <td width='30%'>ѡ����ͼ:&nbsp;<select size=1 name='view_type' style='width:100px;' onchange="doChange()">
       <option value='1' selected>����</option><option value='2'>����</option><option value='3'>�б�</option>
   </select></td>  
   <td width='30%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
 </tr>
</table> 
</form>
</body>
</html>