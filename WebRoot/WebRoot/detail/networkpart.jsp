<%@ page language="java" import="java.util.*" contentType="text/html;charset=gb2312"%>
<%
	String rootPath="";
	String picip="";
	rootPath=request.getParameter("rootPath")==null?"":request.getParameter("rootPath");
	picip=request.getParameter("picip")==null?"":request.getParameter("picip");
 %>
<table cellPadding=0 cellspacing="0" align="center">
		<tr height="50%" >
			<td width="50%" align="center" valign="top">
				<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
					<tr bgcolor=#F1F1F1 height="26">
						<td align="center">
							连通率
						</td>
					</tr>
					<tr>
						<td align="center">
							<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">
						</td>
					</tr>
					<tr height="7">
						<td align=center>
							<img src="<%=rootPath%>/resource/image/Loading_2.gif">
						</td>
					</tr> 
				</table>
			</td>
			<td width="50%" align="center" valign="top">
				<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
					<tr bgcolor=#F1F1F1 height="26">
						<td align="center">
							响应时间
						</td>
					</tr>
					<tr>
						<td align="center">
							<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">
						</td>
					</tr> 
					<tr height="7">
						<td align=center>
							<img src="<%=rootPath%>/resource/image/Loading_2.gif">
						</td>
					</tr> 
				</table>
			</td>
		</tr> 		
		<tr height="50%">	
			<td width="50%" align="center" valign="top">
				<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
					<tr bgcolor=#F1F1F1 height="26">
						<td align="center">
							CPU利用率
						</td>
					</tr>
					<tr>
						<td align="center">
							<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">
						</td>
					</tr> 
					<tr height="7">
						<td align=center>
							<img src="<%=rootPath%>/resource/image/Loading_2.gif">
						</td>
					</tr> 
				</table>
			</td>																	
			
			<td width="50%" align="center" valign="top">
				<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
					<tr bgcolor=#F1F1F1 height="26">
						<td align="center">
							内存利用率
						</td>
					</tr>
					<tr>
						<td align="center">
							<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">
						</td>
					</tr>
					<tr height="7">
						<td align=center>
							<img src="<%=rootPath%>/resource/image/Loading_2.gif">
						</td>
					</tr>  
				</table>
			</td>
		</tr>
	</table>

