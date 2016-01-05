<%@page language="java" contentType="text/html;charset=gb2312"%>
<html>
	<head></head>
	<body>
		<table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border=1>
			<tr>
				<td width="100%" align=left>
					<div id="editmodel" style="display: none">
						<table border="0" id="table1" cellpadding="0" width="100%">
							<tr style="background-color: #FFFFFF;">
								<TD nowrap align="right" height="24" width="10%">
									报表名称&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<input type="text" name="report_name" id="report_name"
										size="50" class="formStyle" size="50">
									<font color='red'>*</font>
								</TD>
							</tr>
							<tr style="background-color: #ECECEC;">
								<TD nowrap align="right" height="24" width="10%">
									报表接收人&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<input type="text" name="recievers_name" id="recievers_name"
										size="50" class="formStyle" size="50" readonly>
									<input type="button" value="设置报表接收人"
										onclick="setReciever('recievers_name','recievers_id');" />
									<input type="hidden" id="recievers_id" name="recievers_id"
										value="">

									<font color='red'>*</font>
								</TD>
							</tr>
							<tr>
								<TD nowrap align="right" height="24" width="10%">
									邮件标题:&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<input type="text" name="tile" id="tile" size="50"
										class="formStyle">
								</TD>
							</tr>
							<tr style="background-color: #ECECEC;">
								<TD nowrap align="right" height="24" width="10%">
									邮件描述:&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<input type="text" name="desc" id="desc" size="50"
										class="formStyle">
								</TD>
							</tr>
							<tr>
								<TD nowrap align="right" height="24" width="10%">
									所属业务&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<input type="text" name="bidtext" id="bidtext" size="50"
										class="formStyle">
									<input type="button" value="设置所属业务"
										onclick='setBid("bidtext" , "bid");' />
									<input type="hidden" id="bid" name="bid" value="">
									<font color='red'>*</font>
								</TD>
							</tr>
							<tr style="background-color: #ECECEC;">
								<TD nowrap align="right" height="24" width="10%">
									报表类型:&nbsp;
								</TD>
								<TD nowrap width="40%">
									&nbsp;
									<SELECT id="reporttype" name="reporttype">
										<OPTION value="day" selected>
											日报
										</OPTION>
										<OPTION value="week">
											周报
										</OPTION>
										<OPTION value="month">
											月报
										</OPTION>
										<OPTION value="season">
											季报
										</OPTION>
										<OPTION value="year">
											年报
										</OPTION>
									</SELECT>
									&nbsp;&nbsp; 导出类型：
									<SELECT id="exporttype" name="exporttype">
										<OPTION value="xls" selected>
											Excel
										</OPTION>
										<OPTION value="doc">
											Word
										</OPTION>
										<OPTION value="pdf">
											Pdf
										</OPTION>
									</SELECT>
								</TD>
							</tr>
							<tr>
								<TD nowrap align="right" height="24">
									报表生成时间:&nbsp;
								</TD>
								<td nowrap colspan="3">
									<div id="formDiv" style="">
										<table width="100%" style="BORDER-COLLAPSE: collapse"
											borderColor=#cedefa cellPadding=0 rules=none border=1
											align="center">
											<tr>
												<td align="left">
													<br>
													<table id="timeConfigTable"
														style="width: 60%; padding: 0; background-color: #FFFFFF; left: 15px;">
														<TBODY>
															<tr>
																<TD style="WIDTH: 100px">
																	<span>发送时间:</span>
																</TD>
															</tr>
															<tr>
																<TD style="WIDTH: 100px">
																	&nbsp;
																</TD>
															</tr>
															<TR>
																<TD>
																	<SELECT style="WIDTH: 250px" id=transmitfrequency
																		onchange="javascript:timeType(this)"
																		name=transmitfrequency>
																		<OPTION value=1 selected>
																			每天
																		</OPTION>
																		<OPTION value=2>
																			每周
																		</OPTION>
																		<OPTION value=3>
																			每月
																		</OPTION>
																		<OPTION value=4>
																			每季
																		</OPTION>
																		<OPTION value=5>
																			每年
																		</OPTION>
																	</SELECT>
																</TD>
															</TR>
															<tr>
																<TD style="WIDTH: 100px">
																	&nbsp;
																</TD>
															</tr>
															<TR>
																<TD style="display: none;" id=td_sendtimemonth>
																	<SELECT disabled="disabled" style="WIDTH: 250px"
																		id=sendtimemonth multiple size=5 name=sendtimemonth>
																		<OPTION selected value=01>
																			01月
																		</OPTION>
																		<OPTION value=02>
																			02月
																		</OPTION>
																		<OPTION value=03>
																			03月
																		</OPTION>
																		<OPTION value=04>
																			04月
																		</OPTION>
																		<OPTION value=05>
																			05月
																		</OPTION>
																		<OPTION value=06>
																			06月
																		</OPTION>
																		<OPTION value=07>
																			07月
																		</OPTION>
																		<OPTION value=08>
																			08月
																		</OPTION>
																		<OPTION value=09>
																			09月
																		</OPTION>
																		<OPTION value=10>
																			10月
																		</OPTION>
																		<OPTION value=11>
																			11月
																		</OPTION>
																		<OPTION value=12>
																			12月
																		</OPTION>
																	</SELECT>
																</TD>
																<TD style="display: none;" id=td_sendtimeweek>
																	<SELECT disabled="disabled" style="WIDTH: 250px"
																		id=sendtimeweek multiple size=5 name=sendtimeweek>
																		<OPTION selected value=0>
																			星期日
																		</OPTION>
																		<OPTION value=1>
																			星期一
																		</OPTION>
																		<OPTION value=2>
																			星期二
																		</OPTION>
																		<OPTION value=3>
																			星期三
																		</OPTION>
																		<OPTION value=4>
																			星期四
																		</OPTION>
																		<OPTION value=5>
																			星期五
																		</OPTION>
																		<OPTION value=6>
																			星期六
																		</OPTION>
																	</SELECT>
																</TD>
																<TD style="display: none;" id=td_sendtimeday>
																	<SELECT disabled="disabled" style="WIDTH: 250px"
																		id=sendtimeday multiple size=5 name=sendtimeday>
																		<OPTION selected value=01>
																			01日
																		</OPTION>
																		<OPTION value=02>
																			02日
																		</OPTION>
																		<OPTION value=03>
																			03日
																		</OPTION>
																		<OPTION value=04>
																			04日
																		</OPTION>
																		<OPTION value=05>
																			05日
																		</OPTION>
																		<OPTION value=06>
																			06日
																		</OPTION>
																		<OPTION value=07>
																			07日
																		</OPTION>
																		<OPTION value=08>
																			08日
																		</OPTION>
																		<OPTION value=09>
																			09日
																		</OPTION>
																		<OPTION value=10>
																			10日
																		</OPTION>
																		<OPTION value=11>
																			11日
																		</OPTION>
																		<OPTION value=12>
																			12日
																		</OPTION>
																		<OPTION value=13>
																			13日
																		</OPTION>
																		<OPTION value=14>
																			14日
																		</OPTION>
																		<OPTION value=15>
																			15日
																		</OPTION>
																		<OPTION value=16>
																			16日
																		</OPTION>
																		<OPTION value=17>
																			17日
																		</OPTION>
																		<OPTION value=18>
																			18日
																		</OPTION>
																		<OPTION value=19>
																			19日
																		</OPTION>
																		<OPTION value=20>
																			20日
																		</OPTION>
																		<OPTION value=21>
																			21日
																		</OPTION>
																		<OPTION value=22>
																			22日
																		</OPTION>
																		<OPTION value=23>
																			23日
																		</OPTION>
																		<OPTION value=24>
																			24日
																		</OPTION>
																		<OPTION value=25>
																			25日
																		</OPTION>
																		<OPTION value=26>
																			26日
																		</OPTION>
																		<OPTION value=27>
																			27日
																		</OPTION>
																		<OPTION value=28>
																			28日
																		</OPTION>
																		<OPTION value=29>
																			29日
																		</OPTION>
																		<OPTION value=30>
																			30日
																		</OPTION>
																		<OPTION value=31>
																			31日
																		</OPTION>
																	</SELECT>
																</TD>
																<TD style="" id=td_sendtimehou>
																	<SELECT style="WIDTH: 250px" id=sendtimehou multiple
																		size=5 name=sendtimehou>
																		<OPTION value=00>
																			00AM
																		</OPTION>
																		<OPTION value=01>
																			01AM
																		</OPTION>
																		<OPTION value=02>
																			02AM
																		</OPTION>
																		<OPTION selected value=03>
																			03AM
																		</OPTION>
																		<OPTION value=04>
																			04AM
																		</OPTION>
																		<OPTION value=05>
																			05AM
																		</OPTION>
																		<OPTION value=06>
																			06AM
																		</OPTION>
																		<OPTION value=07>
																			07AM
																		</OPTION>
																		<OPTION value=08>
																			08AM
																		</OPTION>
																		<OPTION value=09>
																			09AM
																		</OPTION>
																		<OPTION value=10>
																			10AM
																		</OPTION>
																		<OPTION value=11>
																			11AM
																		</OPTION>
																		<OPTION value=12>
																			12AM
																		</OPTION>
																		<OPTION value=13>
																			01PM
																		</OPTION>
																		<OPTION value=14>
																			02PM
																		</OPTION>
																		<OPTION value=15>
																			03PM
																		</OPTION>
																		<OPTION value=16>
																			04PM
																		</OPTION>
																		<OPTION value=17>
																			05PM
																		</OPTION>
																		<OPTION value=18>
																			06PM
																		</OPTION>
																		<OPTION value=19>
																			07PM
																		</OPTION>
																		<OPTION value=20>
																			08PM
																		</OPTION>
																		<OPTION value=21>
																			09PM
																		</OPTION>
																		<OPTION value=22>
																			10PM
																		</OPTION>
																		<OPTION value=23>
																			11PM
																		</OPTION>
																	</SELECT>
																</TD>
															</TR>
														</TBODY>
													</table>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<TD nowrap colspan="4" align=center>
									<br>
									<input type="button" value="保存" style="width: 50"
										class="formStylebutton" id="saveBtn"">
									&nbsp;&nbsp;
									<input type=reset class="formStylebutton" style="width: 60"
										value=" 隐藏模板 " onclick="hiddenModel()">
								</TD>
							</tr>
						</TABLE>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>