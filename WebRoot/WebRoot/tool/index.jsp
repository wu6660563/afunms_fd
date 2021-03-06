﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=path%>/resource/css/style.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/table.css">
		<style type="text/css">
</style>
		<title>查询主页</title>
		<script language="javascript" src="<%=path%>/js/jquery.min.js"></script>
		<script language="javascript" src="<%=path%>/js/query.js"></script>
		<script language="javascript">	
        </script>
	</head>
	<body class="headligreenBg">
		<table class="datalist">
			<tr>
				<td align="center">
					<h4>
						SQL查询工具
					</h4>
				</td>
			</tr>
			<tr>
				<td>
					<div id="table1">
						<table id="set">
							<tr>
								<td>
									数据库类型：
									<select id="dbType" onChange="setPort()">
										<option value="Oracle">
											Oracle
										</option>
										<option value="MySQL" selected>
											MySQL
										</option>
										<option value="SQL Server">
											SQL Server
										</option>
										<option value="Sybase">
											Sybase
										</option>
										<option value="Informix">
											Informix
										</option>
										<option value="DB2">
											DB2
										</option>
									</select>
								</td>
								<td>
									数据库IP：
									<input type="text" id="ip" value="127.0.0.1">
								</td>
								<td>
									端口号：
									<input type="text" id="port" value="3306">
								</td>
							</tr>
							<tr></tr>
							<tr>
								<td>
									数据库名称：
									<input type="text" id="dbName">
								</td>
								<td>
									用 户 名：
									<input type="text" id="user">
								</td>
								<td>
									密 码 ：
									<input type="password" id="pwd">
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<input type="button" id="test" value="测试连接" onclick="vertify()">
							</td>
							<td>
								<input type="button" id="show" value="隐 藏" onclick="fade()">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- 	<input type="button" id="execute" value="执行SQL语句窗口" onclick="openNewWindow()" > -->
			<tr>
				<td>
					<div id="msg" align=left></div>
				</td>
			</tr>
		</table>
	</body>
</html>
