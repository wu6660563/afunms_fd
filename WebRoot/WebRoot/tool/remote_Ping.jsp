<%@ page language="java" contentType="text/html;charset=gb2312" %>
<html>
<head>
<title>SNMP RemotePing</title>
<link href="${pageContext.request.contextPath}/resource/css/global/global.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/tool.js"></script>
<script type="text/javascript">
<!--
	$(function(){
		$("#sub").click(function(){
		    
			var ipAddress = $('#ipinput').val();
			if(!checkIPAddress(ipAddress)){
				$("#resultofping").html("目标ip地址不正确");
				return;
			}
			
			var remoteIpAddress = $('#remoteipinput').val();
			if(!checkIPAddress(remoteIpAddress)){
				$("#resultofping").html("远程服务器ip地址不正确");
				return;
			}
			
			var writeCommunity = $('#communityinput').val();
			if(writeCommunity != null && len(writeCommunity) == 0){
				$("#resultofping").html("请输入写团体");
				return;
			}
			
			versionCmb = document.getElementById("selectVersion");   
			
			var version = "v1";
			if(versionCmb.selectedIndex == 1)
			{
			    version = "v2";
			}
			
			$("#resultofping").html("正在ping，请稍后");
			$("#resultofping").load("network.do?action=remotePing",{ip:ipAddress,ipaddress:remoteIpAddress,community:writeCommunity,version:version});
			
		});
	});
//-->
</script>
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/resource/image/bg4.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<body class="body" >
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
					                			<td align="left" width="5"><img src="${pageContext.request.contextPath}/common/images/right_t_01.jpg" width="5" height="29" /></td>
				                				<td class="win-content-title">&nbsp;SNMP RemotePing &gt;&gt;&nbsp;${param.ipaddress}</td>
				                    			<td align="right"><img src="${pageContext.request.contextPath}/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
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
															<td height="28" align="left" style="width: 150px;"><b>&nbsp;&nbsp;目标IP地址</b></td>
															<td height="28" align="left"><input type="text" maxlength="15" name="ip" id="ipinput" style="width: 150px;" /></td>
															<td height="28" align="left"><input type="button" value="测试" id="sub"></td>
														 </tr>
													</table>
								       			</td>
											</tr>
									    </table>
									</td>
								</tr>
								<tr>
				       				<td>
					       				<table id="win-content-body2" class="win-content-body">
						       				<tr>
						       					<td>
													<table bgcolor="#ECECEC" style="" width="320" height="34">
														<tr align="left" valign="center"> 
															<td height="28" align="left" style="width: 150px;"><b>&nbsp;&nbsp;服务器IP地址</b></td>
															<td height="28" align="left"><input type="text" maxlength="15" name="serverip" id="serveripinput" style="width: 150px;" /></td>
														</tr> 
													</table> 
												</td>
											</tr>
										</table>
					       			</td>
								</tr>
								
								<tr>
				       				<td>
					       				<table id="win-content-body3" class="win-content-body">
						       				<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
															<td height="28" align="left" style="width: 150px;"><b>&nbsp;&nbsp;写团体</b></td>
															<td height="28" align="left"><input type="text" maxlength="20" name="community" id="communityinput" style="width: 150px;" /></td>
											            </tr>
											        </table>
											    </td>
											</tr>  
										</table>
					       			</td>
								</tr>
								
								<tr>
				       				<td>
					       				<table id="win-content-body4" class="win-content-body">
						       				<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
															<td height="28" align="left" style="width: 150px;"><b>&nbsp;&nbsp;SNMP版本</b></td>
															<td height="28" align="left">
															    <select name="selectVersion" id="selectVersion">  
															        <option value="v1">v1</option>  
															        <option value="v2">v2</option>   
															      </select>
															</td>
														</tr>
													</table>
												</td>
											</tr>  
										</table>
					       			</td>
								</tr>
								
								
		
					       		<tr align="left" > 
	                    			<td height="28" align="left" border="0" >
										<textarea id="resultofping" name="showList" rows="15" cols="76" readonly="readonly" >
								
										</textarea>                    			
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
		<input type=button value="关闭窗口" onclick="window.close()">
	</div>
</body>
</html>