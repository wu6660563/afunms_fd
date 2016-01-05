<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.config.dao.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<% 
	String rootPath = request.getContextPath(); 
	List alluser = (List)request.getAttribute("alluser"); 
	List bidIsSelected = (List)request.getAttribute("bidIsSelected");
	String eventText = (String)request.getAttribute("eventText");
	String event = (String)request.getAttribute("event");

%>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Insert title here</title>

<link type="text/css" href="<%=rootPath%>/config/business/businessTree.css" rel="stylesheet"/>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/config/business/checkboxTree.js"></script>

<script>
	var tree;
	var dataArray = new Array();
	<%
		if(alluser != null ){
			for(int i = 0 ; i < alluser.size() ; i++){
				String usrid = (String)alluser.get(i);
				boolean isSelected = false;
				if(bidIsSelected!=null && bidIsSelected.contains(usrid)){
					isSelected = true;
				}
				if(usrid.trim()!=null&&usrid.trim()!=""){
				%>
				dataArray[<%=i%>] = ['<%=usrid.trim()%>' , '<%=usrid.trim()%>' , '1' ,  <%=isSelected%>];
				<%
				}
			}
		}
	
	%>
	
	function showTree(){
		tree = new Tree();
		tree.init(dataArray);
		tree.show("businessTree");
	}

	function allChoose(){
		tree.allChoose();
	}

	function allNotChoose(){
		tree.allNotChoose();
	}
	
	function antiChoose(){
		tree.antiChoose();
	}

	function confirm(){
		var oids ="";  
		var formItem=document.forms["mainForm"];  
		var formElms=formItem.elements;  
		var l=formElms.length;  
		while(l--){   
			if(formElms[l].type=="checkbox"){    
				var checkbox=formElms[l];    
				if(checkbox.name == "checkbox" && checkbox.checked==true){      
					if (oids==""){       
						oids=","+checkbox.value+",";      
					}else{       
						oids=oids+","+checkbox.value;      
					}     
				}   
			}  
		}  
		var event = parent.opener.document.getElementById("bidtext"); 
		event.value=oids;        
		window.close();
	}
	
	function cancel(){
		window.close();
	}
	
	
	function reset(){
		tree.reset();
	}
</script>

<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<body style="margin:0px">
<!--	<div id="div"><input type="button" value="showTree" onclick="show('businessTree')"></div>-->
		
		<form id="mainForm" method="post" name="mainForm">
		<%
		if(alluser != null ){
			for(int i = 0 ; i < alluser.size() ; i++){
				String usrid = (String)alluser.get(i);
				BusinessDao bdao = new BusinessDao();
				Business bvo = (Business)bdao.loadBidbyID(usrid);
			    String bidname = bvo.getName();
				if(usrid.trim()!=null&&usrid.trim()!=""){
				%>
				<div id="businessTree"  class="businessTree" style=" margin-left: 30%; margin-top: 10px; width: 300px; height: 30px;margin-bottom: 0px;TEXT-ALIGN: left;"><INPUT type="checkbox" name=checkbox value="<%=usrid%>"><%=bidname%><br></div>
				<%
				}
			}
		}
	
	%>
		
		<div style="margin-bottom: 5px">
		<br>
			<input type="button" value="确  定" onclick="confirm()">
			<input type="button" value="取  消" onclick="cancel()">
		</div>
	</form>
</body>
</html>