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
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
</head>
<BODY leftmargin="0" topmargin="0">
<br>
<form method="post" name="mainForm">
<table cellpadding="0" cellspacing="0" width=80% align='center'>
 <tr><td align='right'><a href="#" onclick="javascript:history.back(1)">返回</a>&nbsp;</td></tr>
</table> 

<table cellpadding="0" cellspacing="0" width=80% class="tborder" align='center'>
 <tr height="20"><td colspan='3' background="<%=rootPath%>/resource/image/td-bg.jpg" align='center'>
 <font color='white'><b><%out.print(host.getAlias() + "(" + host.getIpAddress() + ")");%>端口列表</b></font></td></tr>
</table> 
<table cellpadding="0" cellspacing="0" width=80% class="tborder" align='center'>
<%         
    Iterator it = host.getInterfaceHash().values().iterator();
    int total = 0;
    int perRow = 8;

    Hashtable chassisHash = new Hashtable();
    while(it.hasNext())
    {       
        IfEntity ifObj = (IfEntity)it.next();          
        if(ifObj.getType() != 6 && ifObj.getType() != 117 )continue;

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
				   }else{				   
					   //不存在SLOT数据
					   Hashtable uport_hash = new Hashtable();
					   int uport = ifObj.getUport();
					   uport_hash.put(uport, ifObj);
					   slot_hash = new Hashtable();
					   slot_hash.put(slot, uport_hash);
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
                
        
        if(total%perRow==0) out.print("<tr>");
        if(ifObj.getOperStatus()==1)
        {
        	out.print("<td align='center' width='80' height='60'>");
      	    out.print("<a href='" + rootPath + "/port.do?action=port_jsp&node_id=" + host.getId());
      		out.print("&index=" + ifObj.getIndex() + "'>");
            out.print("<img border=0 src='");        
            out.print(rootPath);
            out.print("/resource/image/topo/"); 
            out.print("ifOrPortClear.gif'><br>"); 
            out.print(ifObj.getDescr());
            out.print("</a></td>");  
        }
        else
        {
        	out.print("<td align='center' width='80' height='60'>");        
            out.print("<img border=0 src='");        
            out.print(rootPath);
            out.print("/resource/image/topo/"); 
      	    out.print("ifOrPortTrouble.gif'><br>"); 
      	    out.print(ifObj.getDescr());
            out.print("</td>");              
        }                  
        if(total%perRow==perRow-1) out.print("</tr>");
        total++;
    }
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
    	    	    	        		System.out.println((i+1)+"========"+k+"======="+(m+1));
    	    	    	        	}
    	    	    	        }
    	    	    	    }    
    	    	    	    
    	    	    	}
    	    	    }
    	    	}
    	    	
    	    }
    	}
    } 
    if(total%perRow!=0)
    {
       for(int j=total%perRow;j<perRow+1;j++)
         out.print("<td>&nbsp;</td>"); 
       out.print("</tr>");            
    }       
%>
</table>
</form>
</BODY>
</HTML>