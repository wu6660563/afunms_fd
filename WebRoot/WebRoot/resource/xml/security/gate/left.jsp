<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%
  String rootPath = request.getContextPath();
  String currentTime = SysUtil.getCurrentTime();
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/calendar/calendar.css" type="text/css" rel="stylesheet">
<script language="JavaScript" type="text/javascript">
  function selectTime()
  {
      if(mainForm.start_time.disabled == true)
      {
          mainForm.start_time.disabled = false;
          mainForm.end_time.disabled = false;
      }
      else
      {
          mainForm.start_time.disabled = true;
          mainForm.end_time.disabled = true;           
      }
  }
  
  function selectPerson()
  {
      if(mainForm.person.disabled == true)
         mainForm.person.disabled = false;
      else
         mainForm.person.disabled = true;           
  }
  
  function selectIO()
  {
      if(mainForm.io.disabled == true)
         mainForm.io.disabled = false;
      else
         mainForm.io.disabled = true;           
  }
  
  function selectEvent()
  {
      if(mainForm.event.disabled == true)
         mainForm.event.disabled = false;
      else
         mainForm.event.disabled = true;           
  }
  
  function doQuery()
  {  
     var hasChecked = false;
     for( var i=0; i < mainForm.query_item.length; i++ )
     {
          if(mainForm.query_item[i].checked == true)
          {
              hasChecked = true;
              break;              
          }
     }
     if(hasChecked==false)
     {
      	 alert("�������ѯ����!");
     	 return false;
     }
     if(mainForm.person.disabled == false && mainForm.person.value=="")
     {
      	 alert("��������Ա����!");
      	 mainForm.person.focus();
     	 return false;     
     }
     mainForm.action = "<%=rootPath%>/gate.do?action=find";
     mainForm.submit();     
  }
  
  function excel()
  {
     var hasChecked = false;
     for( var i=0; i < mainForm.query_item.length; i++ )
     {
          if(mainForm.query_item[i].checked == true)
          {
              hasChecked = true;
              break;              
          }
     }
     if(hasChecked==false)
     {
      	 alert("�������ѯ����!");
     	 return false;
     }
     if(mainForm.person.disabled == false && mainForm.person.value=="")
     {
      	 alert("��������Ա����!");
      	 mainForm.person.focus();
     	 return false;     
     }
     mainForm.action = "<%=rootPath%>/gate.do?action=report";
     mainForm.submit();       
  }
  
  function goBack()
  {
      mainForm.action = "<%=rootPath%>/gate.do?action=list&jp=1";
      mainForm.submit();           
  }  
</script>
<script type="text/javascript" src="<%=rootPath%>/resource/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/calendar/calendar-cn.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/calendar/calendar-setup.js"></script>
</head>
<body bgcolor='white'>
<form method="post" name="mainForm" target='rightFrame'>
<table cellpadding="0" cellspacing="0" width=100% align='center'>
  <tr height='30'>
    <td><INPUT type="checkbox" class=noborder name="query_item" onclick="javascript:selectTime()" value="0" checked></td>
    <td><font color='blue'>ʱ��</font></td>
  </tr>  
  <tr height='30'>
    <td>&nbsp;</td>
    <td>����<input type="text" name="start_time" size="20" class="formStyle" readonly value="<%=currentTime%>"><a href="#"><img id="date_img1" height="15" hspace="0" src="<%=rootPath%>/resource/image/date.gif" width="20" border="0"></a>
		<script type="text/javascript">
	    Calendar.setup({
	        inputField     :    "start_time",		
	        ifFormat       :    "%Y-%m-%d %H:%M:%S",	
	        showsTime      :    true,			
	        button         :    "date_img1",		
	        singleClick    :    false,			
	        step           :    1				
	    });
		</script></td>
  </tr>         
  <tr height='30'>
    <td>&nbsp;</td>
    <td>��&nbsp;&nbsp;<input type="text" name="end_time" size="20" class="formStyle" readonly value="<%=currentTime%>"><a href="#"><img id="date_img2" height="15" hspace="0" src="<%=rootPath%>/resource/image/date.gif" width="20" border="0"></a>
					<script type="text/javascript">
				    Calendar.setup({
				        inputField     :    "end_time",		
				        ifFormat       :    "%Y-%m-%d %H:%M:%S",	
				        showsTime      :    true,			
				        button         :    "date_img2",		
				        singleClick    :    false,			
				        step           :    1				
				    });
					</script></td>
  </tr> 
  <tr height='30'>
    <td><INPUT type="checkbox" class=noborder name="query_item" onclick="javascript:selectPerson()" value="1"></td>
    <td><font color='blue'>��Ա����</font></td>
  </tr>    
  <tr height='30'>
    <td>&nbsp;</td>
    <td><input type="text" name="person" size="18" class="formStyle" disabled></td>
  </tr>          
  <tr height='30'>
    <td><INPUT type="checkbox" class=noborder name="query_item" onclick="javascript:selectIO()"  value="2"></td>
    <td><font color='blue'>��/��</font></td>
  </tr>    
  <tr height='30'>
    <td>&nbsp;</td>
    <td><select size=1 name='io' style='width:120px;' disabled>
        <option value="0">��</option>
        <option value="1">��</option></td>
  </tr>            
  <tr height='30'>
    <td><INPUT type="checkbox" class=noborder name="query_item" onclick="javascript:selectEvent()" value="3"></td>
    <td><font color='blue'>�¼�</font></td>
  </tr>    
  <tr height='30'>
    <td>&nbsp;</td>
    <td><select size=1 name='event' style='width:120px;' disabled>
            <option value="8">�Ϸ���</option>
            <option value="48">�Ƿ�����</option>
            <option value="3920">��������</option>
		    <option value="3921">��������</option>
		    <option value="3923">���ų�ʱ</option>
		    <option value="3930">���Ű�ť����</option>
		    <option value="3933">����</option>                        
		    <option value="3934">����</option></select></td>
  </tr>
  <tr height='50'>
    <td colspan='2' align='center' valign='middle'>
    <INPUT type="button" class="formStyle" value="��ѯ" onclick="doQuery()">
    <INPUT type="button" class="formStyle" value="����" onclick="goBack()">
    <INPUT type="button" class="formStyle" value="����Excel" onclick="excel()">
    </td>
  </tr>                    
</table>
</form>
</body>
</html>