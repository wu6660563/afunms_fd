<%@page language="java" contentType="text/html;charset=GB2312"%>

<%
  String rootPath = request.getContextPath();
 
%>

<table class="container-main-tool">
	<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">�Ϲ������ò˵�</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													
													<li><img src="<%=rootPath%>/resource/image/toolbar/telnet.gif">&nbsp;<a href="javascript:void(null)" onClick='location.href="<%=rootPath%>/configRule.do?action=strategyList"'>��������</a></li>
													
													<li><img src="<%=rootPath%>/resource/image/toolbar/lygz.gif">&nbsp;<a href="javascript:void(null)" onClick='location.href="<%=rootPath%>//configRule.do?action=groupRuleList"'>����������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/snmpping.gif">&nbsp;<a href="javascript:void(null)" onClick='location.href="<%=rootPath%>/configRule.do?action=ruleDetailList"'>��������</a></li><!-- snow add at 2010-5-28 -->
	               									<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick=''>��������</a></li>
	               									<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick=''>���в���</a></li>
               										
												</ul>
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
	
	
	<!-- ����-->
	
</table>
