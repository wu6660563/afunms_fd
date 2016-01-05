<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.ipresource.dao.IpManageBasicDao"%>
<%@page import="com.afunms.ipresource.model.IpManageBasicVo"%>

<html>
<head>
<%
  String rootPath = request.getContextPath();
  String ipaddress= request.getParameter("ipaddress");
  
  if(ipaddress==null){
  	ipaddress = "";
  }
  String name = "";
  if(!"".equals(ipaddress)) {
	  HostNodeDao dao = new HostNodeDao();
	  HostNode node = null;
	  try{
		  node = (HostNode) dao.findByIpaddress(ipaddress);
	  } catch (Exception e){
		  e.printStackTrace();
	  } finally {
		  dao.close();
	  }
	  if(node == null) {
		  IpManageBasicDao basicDao = new IpManageBasicDao();
		  IpManageBasicVo vo = null;
		  try{
			  vo = (IpManageBasicVo) basicDao.findByIpaddress(ipaddress);
		  } catch(Exception e) {
			  e.printStackTrace();
		  } finally {
			  basicDao.close();
		  }
		  if(vo != null){
			  name = vo.getBak();
		  }
	  } else {
		  name = node.getAlias();	//����
	  }
  }
  
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<title>Ping</title>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/resource/image/bg4.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
<!-- snow add end -->
  <script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
  <script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>

<script language="javascript" src="/afunms/js/tool.js"></script>
<script language="javascript">
function init(){
	document.getElementById("ipaddress").value='<%=ipaddress%>';
	<%
		if(ipaddress!=null&&!"".equals(ipaddress.trim())){
		%>
		ifExecute('ping','execute');
		<%
		}
	%>
	
}
window.onbeforeunload  =  function()   
       {   
       //alert("----------------");
            if((event.clientX>document.body.clientWidth&&event.clientY<0)||event.altKey)   
            {          
           //      alert("�رմ���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('ping',ip);
            }
            else
            {
              //  alert("ˢ�´���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('ping',ip);
            }
        } 
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<body id="body" class="body" onload="init();">
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title">�豸��ϸ��&nbsp;<%=name%></td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td><b>IP</b>��<input type="text" id="ipaddress" name="ipaddress" value="<%=ipaddress%>"></td>
														<td height="28" align="left"><b>&nbsp;����С(Btye)</b></td>
														<td height="28" align="left">&nbsp;
															<select id="packagelength" name=packagelength>
																<option value="0">0</option>
																<option value="1">1</option>
																<option value="2">2</option>
																<option value="4">4</option>
																<option value="8">8</option>
																<option value="16">16</option>
																<option value="32" selected>32</option>
																<option value="64">64</option>
																<option value="128">128</option>
																<option value="256">256</option>
																<option value="1024">1024</option>
																<option value="2048">2048</option>
															</select>
														</td>
														<td height="28" align="left"><b>&nbsp;ִ�д���</b></td>
														<td height="28" align="left">&nbsp;
															<select id="executenumber" name=executenumber>
															<%
																for(int i=1;i<101;i++){
																	if(i==5){
															%>
																		<option value="<%=i%>" selected><%=i%></option>
															<%		} else{
															%>
																<option value="<%=i%>"><%=i%></option>
															<%
																	}
																}
															%>
															</select>                    			
														</td>
														<td height="28" align="left">&nbsp;<input type="button" name="execute" style="height: auto" size="10" value="ִ��" id="execute" onClick="ifExecute('ping','execute')" /></td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" >&nbsp;Ping[<%=ipaddress%>]</td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													<textarea id="resultofping" name="showList" rows="15" cols="76" readonly="readonly" ></textarea>                    			
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
			
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div>  
					<br>
</body>         		 
</html>