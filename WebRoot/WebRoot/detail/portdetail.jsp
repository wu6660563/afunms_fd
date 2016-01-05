<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.util.SessionConstant,com.afunms.system.model.User"%>
<%@page import="com.afunms.initialize.ResourceCenter;"%> 
<html>
	<head>
		<style type="text/css" media="print">
		    .noprint{display:none;} 
		</style>  
		<%
			String rootPath = request.getContextPath();
			User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
			String ip = request.getParameter("ip");
			String ifindex = request.getParameter("ifindex");
			String fileName = user.getUserid() + "_" + ip + "_" + ifindex+ "_port.xml";    
			
			  
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/dwr/interface/PortControler.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/MKDateTime.js"></script>
		<script type="text/javascript"> 
		 	// <![CDATA[ 
			//暂停
			function suspendBtn(){
				//window.open ('<%=rootPath%>/amChart.do?action=portDetailImage&ip=<%=ip%>&ifindex=<%=ifindex%>','amchart','top=200,left=300,height=430,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
				if(oTimer!=null){ 
				    document.getElementById('suspendTime').innerHTML=GetNowDate(); 
					window.clearInterval(oTimer); 
					oTimer=null;
					//暂停时候将图片写到指定目录下
					//var flashMovie = document.getElementById('amline');   
		 			//if (flashMovie) {   
	                //	flashMovie.exportImage('armChartServlet'); 
	               	//}   
	              }else{
					//alert("已经在运行"); 
				}
				
			}
			//继续
			function continueBtn(){
				if(oTimer==null){ 
					oTimer=window.setInterval('generateData()',regenerate_data_interval);	 
				}else{
					//alert("已经在运行");
				}
			}  
			//导出
			function excelBtn(){
				var flashMovie = document.getElementById('amline');   
		 		if (flashMovie) {   
	                flashMovie.exportImage('armChartServlet?ip=<%=ip%>&ifindex=<%=ifindex%>&imageName=端口实时监控'); 
	               	//flashMovie.exportImage('test2.jsp?ip=<%=ip%>&ifindex=<%=ifindex%>&imageName=端口实时监控''); 
	            }   
				//window.open ('<%=rootPath%>/amChart.do?action=portDetailDoc&ip=<%=ip%>&ifindex=<%=ifindex%>','amchart','top=200,left=300,height=430,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
			}
			//打印
			function printBtn(){ 
				window.print();
			}  
			//推出
			function exitBtn(){ 
			 	 this.window.opener=null;
			 	 window.close();
			}
			// ]]>
		</script> 

		<title>端口实时监控</title>
		
	</head>

	<body onload="generateData();" id="body" class="body" bgcolor="#0c2869">
	 	<table align=center id="table1">
			<tr height="2"><td style="font-size:1">&nbsp;</td></tr>
			<tr> 
				<td>
					<table width="100%" border=0 cellpadding=0 cellspacing=0 bordercolor=#727272 border=1> 
						<tr style="background-color: #FFFFFF;">
							<td> 
								<jsp:include page="/topology/includejsp/detail_content_top.jsp">
									<jsp:param name="contentTitle" value="端口实时监控" />
								</jsp:include> 
							</td>
						</tr>
						
						<tr style="background-color: #FFFFFF;">
							<td>
								<table style="BORDER-COLLAPSE: collapse" bordercolor=#727272
									cellpadding=0 rules=none width=100% align=center border=1>  
									<tr style="background-color: #FFFFFF;" align="right">
										<td >
											<table >
												<tr>
													<td> 
														<div class="noPrint">
															&nbsp;<input type="button" value="暂 停" name="suspend"  onclick="suspendBtn();"/>
															&nbsp;<input type="button" value="继 续" name="continue" onclick="continueBtn();"/> 
															&nbsp;<input type="button" value="导 出" name="excel"  onclick="excelBtn();"/> 
															&nbsp;<input type="button" value="打 印" name="print"  onclick="printBtn()"/> 
															&nbsp;<input type="button" value="退 出" name="exit" onclick="exitBtn()" /> 
														</div>
													</td>
												</tr>
											</table> 
										</td>
									</tr>
									<tr  width=100%>
										<td  width=100%>
											<table id="table2" width=100%>
												<tr style="background-color: #F1F1F1; font-size:12"  height="26" > 
													<td width="30%" style="font-weight:bold" nowrap>
														&nbsp;&nbsp;&nbsp;IP地址:
													</td>
													<td width="70%" nowrap>
														&nbsp;&nbsp;&nbsp;<%=ip%></td>
												</tr>
												<tr style="background-color: #FFFFFF; font-size:12"  height="26"> 
													<td  style="font-weight:bold" nowrap>
														&nbsp;&nbsp;&nbsp;端口索引:
													</td>
													<td nowrap>
														&nbsp;&nbsp;&nbsp;<%=ifindex%></td> 
												</tr>
												<tr style="background-color: #F1F1F1; font-size:12"  height="26" > 
													<td width="30%" style="font-weight:bold" nowrap>&nbsp;&nbsp;&nbsp;开始时间:</td>
													<td width="70%" nowrap>&nbsp;&nbsp;&nbsp;<label id="startTime"/></td>
													
												</tr>
												<tr style="background-color: #FFFFFF; font-size:12"  height="26"> 
													<td style="font-weight:bold" nowrap>&nbsp;&nbsp;&nbsp;暂停时间:</td>
													<td nowrap>&nbsp;&nbsp;&nbsp;<label id="suspendTime"/></td> 
												</tr>
											</table>
											<script type="text/javascript">
												document.getElementById('startTime').innerHTML=GetNowDate(); 
											</script>
										</td>
									</tr>
									<tr>
										<td  align="center">  
											<div id="flashcontent"></div> 
											<script type="text/javascript">
										 // <![CDATA[ 
											var regenerate_data_interval = 5000;
											var delayPrint_time = 500;
											var datafileName="<%=fileName%>";
											var ifindex="<%=ifindex%>";
											var blackFlag=true; //用来表示是否要生成空文件 
											oTimer=window.setInterval('generateData()',regenerate_data_interval); 
											window.setTimeout('printChart()',delayPrint_time); 
										    var nodeID=<%=request.getParameter("id")%>;
											function generateData(){   
										      	DWREngine.setAsync(false);//设置同步
										      	PortControler.generateData(datafileName,blackFlag,nodeID,ifindex,{
						      						callback:callback,//回调函数
						      						timeout:5000,//超时时间
						      						errorHandler:function(message) { alert("ERROR: " + message);}
					      						}); 
											}  
											function callback(result){
												if(null!=result&&"success"==result){//生成文件成功
													blackFlag=false;//不再生成空文件 
												}else{
													//生成文件失败
													window.clearInterval(oTimer);
													alert("生成数据文件失败:"+result);
												}
											}
											function printChart(){
												var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "amline", "650", "300", "8", "#fffff");
												so.addVariable("path", "<%=rootPath%>/amchart/");
												so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/port_setting.xml"));
												so.addVariable("data_file", "./amcharts_data/"+datafileName);
												so.write("flashcontent"); 
											}	
										
											 // ]]>
										</script>
										</td>
									</tr> 
								</table>
							</td>
						</tr>
						<tr>
							<td><jsp:include page="/topology/includejsp/detail_content_footer.jsp" /> 
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table> 
	</body>
</html>
