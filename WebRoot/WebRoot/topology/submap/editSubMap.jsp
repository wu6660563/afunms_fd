<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="java.io.File"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.util.ReadFile"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="com.afunms.config.model.Business"%>

<%  
   String rootPath = request.getContextPath();  
   ManageXml vo = (ManageXml)request.getAttribute("vo");
   BusinessDao bussdao = new BusinessDao();
   List allbuss = bussdao.loadAll();
   String bid = vo.getBid();
   List bidlist = new ArrayList();
   if(bid!=null){
       String id[] = bid.split(",");
   
	   if(id != null &&id.length>0){
		for(int i=0;i<id.length;i++){
			bidlist.add(id[i]);
		}
	  }
   }
    
%>
<html>
<head>
<base target="_self">
<meta http-equiv="Pragma" content="no-cache">

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
function edit(){
    if(checkinput("topo_name","string","������ͼ����",30,false)&&
       checkinput("topo_title","string","������ͼ����",50,true)){
        var args = window.dialogArguments;
        mainForm.action = "<%=rootPath%>/submap.do?action=editSubMap";
        mainForm.submit();
        window.close(); 
        args.location.reload();
    }   
}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=vo.getId()%>">
<input type=hidden name="xml_name" value="<%=vo.getXmlName()%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=500px>
	<tr>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="500px" border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp;��ͼ����</td>
				</tr>
			<tr>
				<td height=250 bgcolor="#FFFFFF" valign="top">
				 <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%" align="center">
					<TBODY>
						<tr>
							<td nowrap colspan="2" height="3" bgcolor="#8EADD5"></td>
						</tr>
			            <tr>						
			                <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">������ͼ����&nbsp;</TD>				
			                <TD nowrap width="80%">
			                 &nbsp; <input type="text" name="topo_name" size="30" class="formStyle" value="<%=vo.getTopoName()%>"><font color="red">&nbsp;*</font>(��20�������ַ�)
			               </TD>
			            </tr>
			            <!--  
			            <tr>						
			                <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">������ͼ����&nbsp;</TD>				
			                <TD nowrap width="80%">
			                 &nbsp; <input type="text" name="alias_name" size="20" class="formStyle" value="<%=vo.getAliasName()%>"><font color="red">&nbsp;*</font>(��6�������ַ�)
			               </TD>
			            </tr>
			            -->
			            <tr>						
			                <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">������ͼ����&nbsp;</TD>				
			                <TD nowrap width="80%">
			                 &nbsp; <input type="text" name="topo_title" size="30" class="formStyle" value="<%=vo.getTopoTitle()%>">&nbsp;(��32�������ַ�)
			               </TD>
			            </tr>
			            <!--
			            <tr>						
			                <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">����&nbsp;</TD>				
			                <TD nowrap width="80%">
			                <%  
			                    ///String selected1 = "";
			                    //String selected2 = "";
			                    //if("1".equals(vo.getTopoArea())){
			                    //    selected1 = "selected"; 
			                    //    selected2 = ""; 
			                    //}
			                    //if("2".equals(vo.getTopoArea())){
			                    //    selected1 = ""; 
			                    //    selected2 = "selected"; 
			                    //}
			                %>
			                 &nbsp; <select name='topo_area' style='width:100px;'>
            								<option value='1' >����</option>
            								<option value='2' >�Ϻ�</option>
            					    </select>&nbsp;
			               </TD>
			            </tr>  -->
			            <tr>						
			                <TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">����ͼƬ&nbsp;</TD>				
			                <TD nowrap width="80%">
			                <%  String selected3 = "";
			                    if("0".equals(vo.getTopoBg())){
			                        selected3 = "selected"; 
			                    }
			                    if("Blue.jpg".equals(vo.getTopoBg())){
			                        selected3 = ""; 
			                    }
			                %>
			                 &nbsp; <select name='topo_bg' style='width:100px;'>
            								<option value='0' <%=selected3%>>��</option>
            								<%
	            							   ReadFile readFile = new ReadFile();
	            							   String  path=application.getRealPath(request.getRequestURI());
	                                           String  dir=new File(path).getParent(); 
	            							   List<String> fileList = readFile.readfile(dir.substring(0,dir.lastIndexOf("afunms"))+"\\resource\\image\\bg");
	            							   if(fileList.size()>0){
	            							       for(int i = 0;i<fileList.size();i++){
	            							       String selected = "";
	            							       if(fileList.get(i).equals(vo.getTopoBg()))selected="selected";
	            							 %>
	            								<option value='<%=fileList.get(i)%>' <%=selected%>><%=fileList.get(i)%></option>
	            							<% 
	            							       }
	            							    }
	            							%>
            					    </select>&nbsp;
			               </TD>
			            </tr>
			            <tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">����ҵ��&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
				<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1>
      		    <tbody>  
      				<% if( allbuss.size()>0){
					for(int i=0;i<allbuss.size();i++){
					    int flag = 0;
						Business buss = (Business)allbuss.get(i);
						for(int j=0;j<allbuss.size();j++){
						    Business bvo = (Business)allbuss.get(j);
						    if(buss.getId().equals(bvo.getPid())){
						        flag++;
						    }
						}
						String checkflag = "";
						if(bidlist.contains(buss.getId()+""))checkflag="checked";
						if(flag == 0){
      									%>                  										                        								
  										<tr align="left"> 
  											<td height="28" align="left">&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=buss.getId()%>" <%=checkflag%>>&nbsp;<%=buss.getName()%></td>
  										</tr>  
  										<%
  										    }
  											}
  										}
  										%>                  										                 										                      								
				</tbody>
				</table>
			</TD>																			
			</tr>			
			            <tr align="left"> 
 						    <td height="28" colspan="2">
 						    <table>
 						        <tr>
 						        <%  
			                         if(vo.getTopoType()==4){
			                         
			                    %>
 						            <td><INPUT type="radio" class=noborder name="topo_type" value="4" checked>��Ϊ��ͼ����(Ĭ��)</td>
 						        <% 
 						            } else if(vo.getTopoType()==0) {
 						            
 						        %>
 						            <td><INPUT type="radio" class=noborder name="topo_type" value="0" checked>�����ͼ</td>
 						        <%
 						            } else {
 						         //   if("2".equals(vo.getTopoType())){
 						         %> 
 						   
 						         <td><INPUT type="radio" class=noborder name="topo_type" value="1" checked>��Ϊҵ����ͼ����</td>
 						        <%
 						            }
 						         //   } else {
 						         %> 
 						           <!--     <td><INPUT type="radio" class=noborder name="topo_type" value="2" >��Ϊʾ������ͼ����</td>
 						         <% 
 						         //    }
			                     //    if("3".equals(vo.getTopoType())){
 						        %>
 						             <td><INPUT type="radio" class=noborder name="topo_type" value="3" checked>��Ϊ��������ͼ����</td>
 						        <%
 						         //   } else {
 						        %> 
 						             <td><INPUT type="radio" class=noborder name="topo_type" value="3" >��Ϊ��������ͼ����</td>
 						         <%// } %>-->
 						        </tr>
 						    </table>
 						    </td>
 						</tr>
 						<tr align="left"> 
 						    <td height="28" colspan="2">
	 						    <table>
	 						        <% 
	 						         String check = "";
 						             if(vo.getHome_view()==1||vo.getBus_home_view()==1) {
 						                 check = "checked";
 						             } else {
 						                 check = "";
 						             }
 						             String selected1 = "";
 						             String selected2 = "";
 						             String selected = "";
 						             String selected4 = "";
 						             String selected5 = "";
 						             String selected6 = "";
 						             String selected7 = "";
 						             String selected8 = "";
 						             String selected9 = "";
 						             String selected10 = "";
 						             if(("0.1").equals(vo.getPercent()+"")) {
 						                 selected1 = "selected";
 						             } 
 						             if(("0.2").equals(vo.getPercent()+"")) {
 						                 selected2 = "selected";
 						             }
 						             if(("0.3").equals(vo.getPercent()+"")) {
 						                 selected = "selected";
 						             }
 						             if(("0.4").equals(vo.getPercent()+"")) {
 						                 selected4 = "selected";
 						             }
 						             if(("0.5").equals(vo.getPercent()+"")) {
 						                 selected5 = "selected";
 						             }
 						             if(("0.6").equals(vo.getPercent()+"")) {
 						                 selected6 = "selected";
 						             }
 						             if(("0.7").equals(vo.getPercent()+"")) {
 						                 selected7 = "selected";
 						             }
 						             if(("0.8").equals(vo.getPercent()+"")) {
 						                 selected8 = "selected";
 						             }
 						             if(("0.9").equals(vo.getPercent()+"")) {
 						                 selected9 = "selected";
 						             }
 						             if(("1").equals(vo.getPercent()+"")) {
 						                 selected10 = "selected";
 						             }
 						            %>
	 						        <tr>
	 						            <td><INPUT type="checkbox" class=noborder name="home_view" value="1" <%=check%>>��ҳ��ʾ</td>
	 						            <td nowrap>&nbsp;���ű���:
								        <select name='zoom_percent' style='width:50px;'>
								            <option value='1' <%=selected10%>>1</option>
            								<option value='0.9' <%=selected9%>>0.9</option>
            								<option value='0.8' <%=selected8%>>0.8</option>
            								<option value='0.7' <%=selected7%>>0.7</option>
            								<option value='0.6' <%=selected6%>>0.6</option>
            								<option value='0.5' <%=selected5%>>0.5</option>
            								<option value='0.4' <%=selected4%>>0.4</option>
            								<option value='0.3' <%=selected%>>0.3</option>
            								<option value='0.2' <%=selected2%>>0.2</option>
            								<option value='0.1' <%=selected1%>>0.1</option>
            							</select>&nbsp;
            						</td> 
	 						        </tr>
	 						    </table>
 						    </td>
 						</tr>
						<tr>
							<TD nowrap colspan="2" align=center>
								<br>
								<input type="button" value="����" style="width:50" class="formStylebutton" onclick="edit()">
								<input type="button" value="�ر�" style="width:50" class="formStylebutton" onclick="window.close();">
							</TD>	
						</tr>
			         </TBODY>
				   </TABLE>				
				</td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>