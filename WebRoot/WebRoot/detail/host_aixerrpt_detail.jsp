<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="java.text.*"%>
<%
%>
<%
	String rootPath = request.getContextPath();
	Errptlog errptlog = (Errptlog) request.getAttribute("errptlog");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Hashtable errpttypehashtable = new Hashtable();
	errpttypehashtable.put("pend" , "�豸����������ܶ�ʧ");
	errpttypehashtable.put("perf" , "���������½�");
	errpttypehashtable.put("perm" , "Ӳ���豸�����ģ����");
	errpttypehashtable.put("temp" , "��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����");
	errpttypehashtable.put("info" , "һ����Ϣ�����Ǵ���");
	errpttypehashtable.put("unkn" , "����ȷ�������������");
	errpttypehashtable.put("none" , "δ֪");
	Hashtable errptclasshashtable = new Hashtable();
	errptclasshashtable.put("h" , "Ӳ������ʹ���");
	errptclasshashtable.put("s" , "�������");
	errptclasshashtable.put("0" , "��Ϊ����");
	errptclasshashtable.put("u" , "����ȷ��");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
	</head>
	<body id="body" class="body" onload="">

		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="orderflag">
			<input type=hidden name="id">

			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-detail">
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												<table id="detail-content" class="detail-content">
													<tr>
														<td>
															<table id="detail-content-header"
																class="detail-content-header">
																<tr>
																	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																	<td class="detail-content-title">errpt��ϸ��Ϣ</td>
																	<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-body" class="detail-content-body">
																<tr>
																	<td>

																		<table>
																			<tr>
																				<td width="30%" height="26" align="left" nowrap
																					class=txtGlobal>
																					&nbsp;��ǩ:
																				</td>
																				<td width="70%"><%=errptlog.getLabels()%>
																				</td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td height="29" class=txtGlobal align="left" nowrap>
																					&nbsp;��ʶ:
																				</td>
																					<td width="70%"><%=errptlog.getIdentifier()%>
																				<td>
																				</td>
																			</tr>
																			<tr>
																				<td width="30%" height="26" align=left nowrap
																					class=txtGlobal>
																					&nbsp;ʱ��:
																				</td>
																				<td width="70%"><%=simpleDateFormat.format(errptlog.getCollettime().getTime())%></td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td height="29" class=txtGlobal align="left" nowrap>
																					&nbsp;���к�:
																				</td>
																				<td><%=errptlog.getSeqnumber()%></td>
																			</tr>
																			<tr>
																				<td width="30%" height="26" align=left nowrap
																					class=txtGlobal>
																					&nbsp;����id:
																				</td>
																				<td width="70%"><%=errptlog.getIdentifier()%></td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td width="30%" height="26" align=left nowrap
																					class=txtGlobal>
																					&nbsp;�ڵ�:
																				</td>
																				<td width="70%"><%=errptlog.getNodeid()%></td>
																			</tr>
																			<tr>
																				<td width="30%" height="26" align=left nowrap
																					class=txtGlobal>
																					&nbsp;�������:
																				</td>
																				<td width="70%"><%=errptclasshashtable.get(errptlog.getErrptclass().trim().toLowerCase())%></td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td height="29" class=txtGlobal valign=center nowrap>
																					&nbsp;��������:
																				</td>
																				<td><%=errpttypehashtable.get(errptlog.getErrpttype().trim().toLowerCase())%></td>
																			</tr>
																			<tr>
																				<td width="30%" height="26" align=left valign=center
																					nowrap class=txtGlobal>
																					&nbsp;��Դ����:
																				</td>
																				<td width="70%"><%=errptlog.getResourcename()%></td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td height="29" class=txtGlobal valign=center nowrap>
																					&nbsp;��Դ���:
																				</td>
																				<td><%=errptlog.getResourceclass()%></td>
																			</tr>
																			<tr>
																				<td height="29" class=txtGlobal valign=center nowrap>
																					&nbsp;��Դ����:
																				</td>
																				<td><%=errptlog.getRescourcetype()%></td>
																			</tr>
																			<tr bgcolor="#F1F1F1">
																				<td height="29" class=txtGlobal valign=center nowrap>
																					&nbsp;λ��:
																				</td>
																				<td><%=errptlog.getLocations()%></td>
																			</tr>


																			<tr>
																				<td height="29" class=txtGlobal>
																					&nbsp;VPD:
																				</td>
																				<td>
																					<textarea cols="100" rows="10"><%=errptlog.getVpd()%></textarea>
																				</td>
																			</tr>
																			<tr>
																				<td height="29" class=txtGlobal>
																					&nbsp;����:
																				</td>
																				<td>
																					<textarea cols="100" rows="10"><%=errptlog.getDescriptions()%></textarea>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-footer"
																class="detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
																				<td></td>
																				<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
								</td>
							<tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>