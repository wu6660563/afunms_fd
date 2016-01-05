<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="java.util.*"%>
<%
  String rootPath = request.getContextPath();
  
  String tmp = request.getParameter("id");
  int id = Integer.parseInt(tmp);
  Host host = (Host)PollingEngine.getInstance().getNodeByID(id);     
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="content-type" content="text/html; charset=gb2312" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>显示服务器视图</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
</head>
<BODY leftmargin="0" topmargin="0">
<br>
<form method="post" name="mainForm">
<%         
    Iterator it = host.getInterfaceHash().values().iterator();

    Hashtable chassisHash = new Hashtable();
    while(it.hasNext())
    {       
        IfEntity ifObj = (IfEntity)it.next();          
        if(ifObj.getType() != 6 && ifObj.getType() != 117 )continue;
//System.out.println(ifObj.getType()+"-----"+ifObj.getChassis()+"/"+ifObj.getSlot()+"/"+ifObj.getUport());
			   int intchassis = ifObj.getChassis();
			   if(chassisHash.containsKey(intchassis)){
			   
				   //已经存在CHASSIS数据
				   Hashtable slot_hash = (Hashtable)chassisHash.get(intchassis);
				   int slot = ifObj.getSlot();			   
				   if(slot_hash.containsKey(slot)){
				   				   
					   //已经存在SLOT数据
					   Hashtable uport_hash = (Hashtable)slot_hash.get(slot);
					   int uport = ifObj.getUport();
					   uport_hash.put(uport, ifObj);
					   slot_hash.put(slot, uport_hash);
					   chassisHash.put(intchassis, slot_hash);
					   //System.out.println(intchassis+"##########"+slot+"&&&&&&&&&&&&&&"+uport);
				   }else{	
				   			   
					   //不存在SLOT数据
					   Hashtable uport_hash = new Hashtable();
					   int uport = ifObj.getUport();
					   uport_hash.put(uport, ifObj);
					   slot_hash = (Hashtable)chassisHash.get(intchassis);
					   slot_hash.put(slot, uport_hash);
					   //System.out.println("slot~~~~~~~~"+slot);
					   //System.out.println(intchassis+"##########"+slot+"&&&&&&&&&&&&&&"+uport);
					   chassisHash.put(intchassis,slot_hash);
				   }  
			   }else{			   
				   //不存在CHASSIS数据
				   Hashtable slot_hash = new Hashtable();
				   int slot = ifObj.getSlot();
				   Hashtable uport_hash = new Hashtable();
				   int uport = ifObj.getUport();
				   uport_hash.put(uport, ifObj);
				   slot_hash.put(slot, uport_hash);
				   chassisHash.put(intchassis, slot_hash);				   
			   }
    }
    /*
    if(chassisHash != null && chassisHash.size()>0){
    	for(int i=0;i<chassisHash.size();i++){
    	    if(chassisHash.containsKey(i+1)){
    	    
    	    	Hashtable slot_hash = (Hashtable)chassisHash.get(i+1); 
    	    	if(slot_hash != null && slot_hash.size()>0){
    	    	    for(int k=0;k<slot_hash.size();k++){
    	    	    
    	    	    	if(slot_hash.containsKey(k)){
    	    	    	    Hashtable uport_hash = (Hashtable)slot_hash.get(k);
    	    	    	    
    	    	    	    if(uport_hash != null && uport_hash.size()>0){
    	    	    	    
    	    	    	        for(int m=0;m<uport_hash.size();m++){
    	    	    	        	if(uport_hash.containsKey(m+1)){
    	    	    	        		IfEntity ifObj = (IfEntity)uport_hash.get(m+1);
    	    	    	        		//System.out.println((i+1)+"========"+k+"======="+(m+1)+"----"+ifObj.getDescr());
    	    	    	        	}
    	    	    	        }
    	    	    	    }    
    	    	    	    
    	    	    	}
    	    	    }
    	    	}
    	    	
    	    }
    	}
    }
    */        
%>
<table class="tborder" cellpadding="0" cellspacing="0" width=515 border=0 align='center' >
<tr>
<td width=100% colspan=3 align='center'>
	设备<%=host.getAlias() %>(IP:<%=host.getIpAddress()%>)的端口面板图
</td>
</tr>
<%
if(chassisHash != null && chassisHash.size()>0){
for(int i=0;i<chassisHash.size();i++){	
%>
<tr>
<td align=right width=5 bgcolor=#013741>
&nbsp;
</td>
<%
	//判断是几个槽
	if(chassisHash.containsKey(i+1)){
		Hashtable slot_hash = (Hashtable)chassisHash.get(i+1); 
		if(slot_hash != null && slot_hash.size()>0){
		//System.out.println("slot size ----------------------"+slot_hash.size());
			for(int k=0;k<slot_hash.size();k++){
%>
<td align=right width=500 bgcolor=#013741>
<table bgcolor=#013741>
 <tr>
 <td align='center' bgcolor=#88A0BD>
 <%
 				if(slot_hash.containsKey(k)){
 					Hashtable uport_hash = (Hashtable)slot_hash.get(k);
 					if(uport_hash != null && uport_hash.size()>0){
 						for(int m=0;m<uport_hash.size();m++){
 							if(uport_hash.containsKey(m+1)){
 								IfEntity ifObj = (IfEntity)uport_hash.get(m+1);
 								if(ifObj.getOperStatus()==1){
 %>
 <a href='<%=rootPath%>/port.do?action=port_jsp&node_id=<%=host.getId()%>&index=<%=ifObj.getIndex()%>'><img src="ifOrPortClear_2.gif" border=0></a>
 <%
 								}else{
%> 								
 <img src="ifOrPortNormal_2.gif" border=0> 								

<% 								
 								}
 							}
 						}
 					}
 				}
 %>
 </td>
 </tr>
</table>
</td>
<%
			}
		}
	}//结束判断是几个槽
%>

<td align=right width=5 bgcolor=#013741>
&nbsp;
</td>
</tr> 
<%
}
}
%>
</table>
</form>
</BODY>
</html>