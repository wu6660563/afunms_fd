<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
  String rootPath = request.getContextPath();  
%>
<html>    
<head>
<title>°²È«¼à¿Ø</title>  
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script> 
</head>  
<body id="body" class="body">
    <table width="100%" cellpadding="0" cellspacing="0" height="100%">
       <tr> 
           <td>
             <div align="center">
             <table cellpadding="0" cellspacing="0" width="100%" height="100%">
       	    <tr> 
        	    <td width="100%" height="100%"> 
	        		<div id="flashcontent">
						<strong>You need to upgrade your Flash Player</strong>
					</div>
					<script type="text/javascript">
						var so = new SWFObject("<%=rootPath%>/flex/soc_event.swf?gridxml=egrid.xml&treexml=eventtree.xml&projectName=afunms", "soc_event", "100%", "100%", "8", "#ffffff");
						so.write("flashcontent");
					</script>				
               </td>
			</tr>             
		 </table>
            </div>
          </td>
       </tr>
     </table>
</body>
</html>
