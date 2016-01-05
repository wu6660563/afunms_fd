/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.capreport.manage;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;

import com.afunms.application.util.ReportExport;
import com.afunms.capreport.dao.SubscribeResourcesDao;
import com.afunms.capreport.dao.UtilReportDao;
import com.afunms.capreport.model.SubscribeResources;
import com.afunms.capreport.model.UtilReport;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DateE;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.detail.net.service.NetService;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostCollectDataDay;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataDayManager;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;
import com.afunms.system.model.User;
import com.afunms.system.util.UserView;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * 增加一个节点，要在server.jsp增加一个节点;
 * 删除或更新一个节点信息，在这则不直接操作server.jsp,而是让任务updateXml来更新xml.
 */
public class NetCapReportManager extends BaseManager implements
		ManagerInterface {
	DateE datemanager = new DateE();
	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
	I_HostCollectData hostmanager = new HostCollectDataManager();
	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String list() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/list.jsp";
	}

	private String find() {
		int netflag = 0;
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		String bid = getParaValue("bidtext");
		netflag = getParaIntValue("netflag");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetworkByBid(1, bid));
		if (netflag == 1) {
			return "/capreport/net/list.jsp";
		} else if (netflag == 2) {
			return "/capreport/net/neteventlist.jsp";
		} else if (netflag == 3) {
			return "/capreport/net/netmultilist.jsp";
		} else
			return "/capreport/net/list.jsp";
	}

	private String eventlist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/neteventlist.jsp";
	}

	private String netmultilist() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/netmultilist.jsp";
	}

	private String netping() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// 按排序标志取各端口最新记录的列表
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null
				&& !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids[i]));

				Hashtable pinghash = new Hashtable();
				try {
					pinghash = hostmanager.getCategory(node.getIpAddress(),
							"Ping", "ConnectUtilization", starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable responsehash = new Hashtable();
				try {
					responsehash = hostmanager.getCategory(node.getIpAddress(),
							"Ping", "ResponseTime", starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable ipmemhash = new Hashtable();
				ipmemhash.put("hostnode", node);
				ipmemhash.put("pinghash", pinghash);
				ipmemhash.put("responsehash", responsehash);
				orderList.add(ipmemhash);
			}

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping")
				|| orderflag.equalsIgnoreCase("downnum")
				|| orderflag.equalsIgnoreCase("responseavg")
				|| orderflag.equalsIgnoreCase("responsemax")) {
			returnList = (List) session.getAttribute("pinglist");
		} else {
			// 对orderList根据theValue进行排序

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					HostNode node = (HostNode) _pinghash.get("hostnode");
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					Hashtable responsehash = (Hashtable) _pinghash
							.get("responsehash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					String responseavg = "";
					String responsemax = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					// 获取响应时间
					if (responsehash.get("avgpingcon") != null)
						responseavg = (String) responsehash.get("avgpingcon");
					if (responsehash.get("pingmax") != null)
						responsemax = (String) responsehash.get("pingmax");

					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					ipdiskList.add(responseavg);
					ipdiskList.add(responsemax);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0,
								avgping.length() - 2)).doubleValue() < new Double(
								_avgping.substring(0, _avgping.length() - 2))
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responseavg")) {
						String avgping = "";
						if (ipdiskList.get(5) != null) {
							avgping = (String) ipdiskList.get(5);
						}
						String _avgping = "";
						if (ipdiskList.get(5) != null) {
							_avgping = (String) _ipdiskList.get(5);
						}
						if (new Double(avgping.substring(0,
								avgping.length() - 2)).doubleValue() < new Double(
								_avgping.substring(0, _avgping.length() - 2))
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responsemax")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// 得到排序后的Subentity的列表
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}

		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		System.out.println(list.size() + "===========================");
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("pinglist", list);
		return "/capreport/net/netping.jsp";
	}

	private String netevent() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// 按排序标志取各端口最新记录的列表
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null
				&& !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				HostNodeDao dao = new HostNodeDao();
				HostNode node = dao.loadHost(Integer.parseInt(ids[i]));
				if (node == null)
					continue;
				EventListDao eventdao = new EventListDao();
				// 得到事件列表
				StringBuffer s = new StringBuffer();
				s.append("select * from system_eventlist where recordtime>= '"
						+ starttime + "' " + "and recordtime<='" + totime
						+ "' ");
				s.append(" and nodeid=" + node.getId());

				List infolist = eventdao.findByCriteria(s.toString());

				// List infolist =
				// eventqueryManager.getQuery(starttime,totime,"99","99",99,node.getId());
				if (infolist != null && infolist.size() > 0) {
					// mainreport = mainreport+ " \r\n";
					int levelone = 0;
					int levletwo = 0;
					int levelthree = 0;
					int pingvalue = 0;
					int cpuvalue = 0;
					int updownvalue = 0;
					int utilvalue = 0;

					for (int j = 0; j < infolist.size(); j++) {
						EventList eventlist = (EventList) infolist.get(j);
						if (eventlist.getContent() == null)
							eventlist.setContent("");
						String content = eventlist.getContent();
						String subentity = eventlist.getSubentity();
						if (eventlist.getLevel1() == 1) {
							levelone = levelone + 1;
						} else if (eventlist.getLevel1() == 2) {
							levletwo = levletwo + 1;
						} else if (eventlist.getLevel1() == 3) {
							levelthree = levelthree + 1;
						}
						// if(content.indexOf("连通率")>=0){
						// pingvalue = pingvalue + 1;
						// }else if(content.indexOf("CPU利用率")>=0){
						// cpuvalue = cpuvalue + 1;
						// }else if(content.indexOf("up")>=0 ||
						// content.indexOf("down")>=0){
						// updownvalue = updownvalue + 1;
						// }else if(content.indexOf("流速")>=0){
						// utilvalue = utilvalue + 1;
						// }

						if (("ping").equals(subentity)) {// 联通率
							pingvalue = pingvalue + 1;
						} else if (("cpu").equals(subentity)) {// cpu
							cpuvalue = cpuvalue + 1;
						} else if (("interface").equals(subentity)
								&& content.indexOf("端口") >= 0) {// 端口
							updownvalue = updownvalue + 1;
						} else if (("interface").equals(subentity)
								&& content.indexOf("端口") < 0) {// 流速
							utilvalue = utilvalue + 1;
						}
					}
					String equname = node.getAlias();
					String ip = node.getIpAddress();
					List ipeventList = new ArrayList();
					ipeventList.add(ip);
					ipeventList.add(equname);
					ipeventList.add(node.getType());
					ipeventList.add((levelone + levletwo + levelthree) + "");
					ipeventList.add(levelone + "");
					ipeventList.add(levletwo + "");
					ipeventList.add(levelthree + "");
					ipeventList.add(pingvalue + "");
					ipeventList.add(cpuvalue + "");
					ipeventList.add(updownvalue + "");
					ipeventList.add(utilvalue + "");
					orderList.add(ipeventList);

				}
			}

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("one")
				|| orderflag.equalsIgnoreCase("two")
				|| orderflag.equalsIgnoreCase("three")
				|| orderflag.equalsIgnoreCase("ping")
				|| orderflag.equalsIgnoreCase("cpu")
				|| orderflag.equalsIgnoreCase("updown")
				|| orderflag.equalsIgnoreCase("util")
				|| orderflag.equalsIgnoreCase("sum")) {
			returnList = (List) session.getAttribute("eventlist");
		} else {
			returnList = orderList;
		}

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("sum")) {
						String sum = "";
						if (ipdiskList.get(3) != null) {
							sum = (String) ipdiskList.get(3);
						}
						String _sum = "";
						if (ipdiskList.get(3) != null) {
							_sum = (String) _ipdiskList.get(3);
						}
						if (new Double(sum).doubleValue() < new Double(_sum)
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("one")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("two")) {
						String downnum = "";
						if (ipdiskList.get(5) != null) {
							downnum = (String) ipdiskList.get(5);
						}
						String _downnum = "";
						if (ipdiskList.get(5) != null) {
							_downnum = (String) _ipdiskList.get(5);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("three")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(7) != null) {
							downnum = (String) ipdiskList.get(7);
						}
						String _downnum = "";
						if (ipdiskList.get(7) != null) {
							_downnum = (String) _ipdiskList.get(7);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("cpu")) {
						String downnum = "";
						if (ipdiskList.get(8) != null) {
							downnum = (String) ipdiskList.get(8);
						}
						String _downnum = "";
						if (ipdiskList.get(8) != null) {
							_downnum = (String) _ipdiskList.get(8);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("updown")) {
						String downnum = "";
						if (ipdiskList.get(9) != null) {
							downnum = (String) ipdiskList.get(9);
						}
						String _downnum = "";
						if (ipdiskList.get(9) != null) {
							_downnum = (String) _ipdiskList.get(9);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("util")) {
						String downnum = "";
						if (ipdiskList.get(10) != null) {
							downnum = (String) ipdiskList.get(10);
						}
						String _downnum = "";
						if (ipdiskList.get(10) != null) {
							_downnum = (String) _ipdiskList.get(10);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// 得到排序后的Subentity的列表
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}

		// setListProperty(capReportForm, request, list);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("eventlist", list);
		session.setAttribute("eventlist", list);
		return "/capreport/net/netevent.jsp";
	}

	private String downloadpingreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("pinglist");
		Hashtable reporthash = new Hashtable();
		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);

		ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);
		report.createReport_hostping("/temp/hostping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}
	
	private String downloadnetpingreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		
		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("pinglist");
		Hashtable reporthash = new Hashtable();
		reporthash.put("pinglist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		
		ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
			reporthash);
		report.createReport_netping("/temp/netping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private String downloadneteventreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();

		List returnList = new ArrayList();
		// I_MonitorIpList monitorManager=new MonitoriplistManager();
		List memlist = (List) session.getAttribute("eventlist");
		Hashtable reporthash = new Hashtable();
		reporthash.put("eventlist", memlist);
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);

		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);
		report.createReport_netevent("/temp/hostping_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	/*
	 * guzhiming 网络设备综合报表 报表订阅模块使用
	 */
	public void createselfnetreport(String startdate, String todate, String ip,
			String type, String str, WritableWorkbook wb, String filena) {
		Date d = new Date();
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String equipname = "";
		// zhushouzhi--------------------------start
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;

		try {
			HostNodeDao dao = new HostNodeDao();
			HostNode node = (HostNode) dao.findByCondition("ip_address", ip)
					.get(0);
			dao.close();
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";
			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			// String remoteip = request.getRemoteAddr();
			String newip = doip(ip);

			// 按排序标志取各端口最新记录的列表
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
					"ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

			vector = hostlastmanager.getInterface_share(ip, netInterfaceItem,
					orderflag, startdate, todate);
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = portdao.getIpsHash(ip);
			List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// 显示端口的流速图形
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "至" + totime + "端口流速";
				String[] banden3 = { "InBandwidthUtilHdx",
						"OutBandwidthUtilHdx" };
				String[] bandch3 = { "入口流速", "出口流速" };

				for (int i = 0; i < reportports.size(); i++) {
					SysLogger.info(reportports.get(i).getClass()
							+ "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports
								.get(i);
						// 按分钟显示报表
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed",
								portconfig.getPortindex() + "", banden3,
								bandch3, startdate, todate, "UtilHdx");
						String reportname = "第" + portconfig.getPortindex()
								+ "(" + portconfig.getName() + ")端口流速"
								+ startdate + "至" + todate + "报表(按分钟显示)";
						p_drawchartMultiLineMonth(value, reportname, newip
								+ portconfig.getPortindex() + "ifspeed_day",
								800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip
								+ portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
					"Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			// 从内存中获得当前的跟此IP相关的IP-MAC的FDB表信息
			Hashtable _IpRouterHash = ShareData.getIprouterdata();
			vector = (Vector) _IpRouterHash.get(ip);
			if (vector != null)
				reporthash.put("iprouterVector", vector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// 得到事件列表
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '"
					+ starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = eventdao.findByCriteria(s.toString());

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// 联通率
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") >= 0) {// 端口
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") < 0) {// 流速
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			Vector pdata = (Vector) pingdata.get(ip);
			// 把ping得到的数据加进去
			if (pdata != null && pdata.size() > 0) {
				for (int m = 0; m < pdata.size(); m++) {
					Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
					if (hostdata.getSubentity().equals("ConnectUtilization")) {
						reporthash.put("time", hostdata.getCollecttime());
						reporthash.put("Ping", hostdata.getThevalue());
						reporthash.put("ping", maxping);
					}
				}
			}

			// CPU
			Hashtable hdata = (Hashtable) sharedata.get(ip);
			if (hdata == null)
				hdata = new Hashtable();
			Vector cpuVector = new Vector();
			if (hdata.get("cpu") != null)
				cpuVector = (Vector) hdata.get("cpu");
			if (cpuVector != null && cpuVector.size() > 0) {
				// for(int si=0;si<cpuVector.size();si++){
				CPUcollectdata cpudata = (CPUcollectdata) cpuVector
						.elementAt(0);
				maxhash.put("cpu", cpudata.getThevalue());
				reporthash.put("CPU", maxhash);
				// }
			} else {
				reporthash.put("CPU", maxhash);
			}
			// 流速

			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "网络设备";

			}
			reporthash.put("typename", typename);

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
					reporthash);
			// 画图

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip,
					"Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash
						.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip
					+ "ConnectUtilization", 740, 120);

			// String str = request.getParameter("str");//
			// 从页面返回设定的str值进行判断，生成excel报表或者word报表
			// zhushouzhi--------------------------------
			if ("0".equals(str)) {
				report.createReport_networkWithoutClose(filena, wb);
				SysLogger.info("filename---" + report.getFileName());// excel综合报表
				// request.setAttribute("filename", report.getFileName());
			} else if ("1".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networknms_report.doc";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkDoc(fileName);// word综合报表

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("3".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkNewknms_report.doc";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkNewDoc(fileName);// word运行分析报表

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("4".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkNewknms_report.pdf";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkNewPdf(fileName);// pdf网络设备业务分析表

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("5".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkknms_report.pdf";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkPDF(fileName);

					// request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// zhushouzhi---------------------------end
		// return mapping.findForward("report_info");

	}

	private String downloadselfnetreport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";
		// zhushouzhi--------------------------start
		String type = "";
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;

		try {
			ip = getParaValue("ipaddress");
			type = getParaValue("type");
			HostNodeDao dao = new HostNodeDao();
			HostNode node = (HostNode) dao.findByCondition("ip_address", ip)
					.get(0);
			dao.close();
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";
			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			String remoteip = request.getRemoteAddr();
			String newip = doip(ip);

			// 按排序标志取各端口最新记录的列表
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
					"ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };

			vector = hostlastmanager.getInterface_share(ip, netInterfaceItem,
					orderflag, startdate, todate);
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = new Hashtable();

			List reportports = new ArrayList();
			try {
				portconfigHash = portdao.getIpsHash(ip);
				reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			} catch (Exception e) {

			} finally {
				portdao.close();
			}

			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// 显示端口的流速图形
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "至" + totime + "端口流速";
				String[] banden3 = { "InBandwidthUtilHdx",
						"OutBandwidthUtilHdx" };
				String[] bandch3 = { "入口流速", "出口流速" };

				for (int i = 0; i < reportports.size(); i++) {
					// SysLogger.info(reportports.get(i).getClass()+
					// "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports
								.get(i);
						// 按分钟显示报表
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed",
								portconfig.getPortindex() + "", banden3,
								bandch3, startdate, todate, "UtilHdx");
						String reportname = "第" + portconfig.getPortindex()
								+ "(" + portconfig.getName() + ")端口流速"
								+ startdate + "至" + todate + "报表(按分钟显示)";
						p_drawchartMultiLineMonth(value, reportname, newip
								+ portconfig.getPortindex() + "ifspeed_day",
								800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip
								+ portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
					"Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			// 从内存中获得当前的跟此IP相关的IP-MAC的FDB表信息
			Hashtable _IpRouterHash = ShareData.getIprouterdata();
			vector = (Vector) _IpRouterHash.get(ip);
			if (vector != null)
				reporthash.put("iprouterVector", vector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// 得到事件列表
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '"
					+ starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = new ArrayList();
			try {
				infolist = eventdao.findByCriteria(s.toString());
			} catch (Exception e) {

			} finally {
				eventdao.close();
			}

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// 联通率
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") >= 0) {// 端口
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") < 0) {// 流速
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			Vector pdata = (Vector) pingdata.get(ip);
			// 把ping得到的数据加进去
			if (pdata != null && pdata.size() > 0) {
				for (int m = 0; m < pdata.size(); m++) {
					Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
					if (hostdata.getSubentity().equals("ConnectUtilization")) {
						reporthash.put("time", hostdata.getCollecttime());
						reporthash.put("Ping", hostdata.getThevalue());
						reporthash.put("ping", maxping);
					}
				}
			}

			// CPU
			Hashtable hdata = (Hashtable) sharedata.get(ip);
			if (hdata == null)
				hdata = new Hashtable();
			Vector cpuVector = new Vector();
			if (hdata.get("cpu") != null)
				cpuVector = (Vector) hdata.get("cpu");
			if (cpuVector != null && cpuVector.size() > 0) {
				// for(int si=0;si<cpuVector.size();si++){
				CPUcollectdata cpudata = (CPUcollectdata) cpuVector
						.elementAt(0);
				maxhash.put("cpu", cpudata.getThevalue());
				reporthash.put("CPU", maxhash);
				// }
			} else {
				reporthash.put("CPU", maxhash);
			}
			// 流速

			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "网络设备";

			}
			reporthash.put("typename", typename);

			AbstractionReport1 report = new ExcelReport1(
					new IpResourceReport(), reporthash);
			// 画图

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip,
					"Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash
						.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip
					+ "ConnectUtilization", 740, 120);

			String str = request.getParameter("str");// 从页面返回设定的str值进行判断，生成excel报表或者word报表
			// zhushouzhi--------------------------------
			if ("0".equals(str)) {
				report.createReport_network("temp/networknms_report.xls");
				// SysLogger.info("filename---" +
				// report.getFileName());//excel综合报表
				request.setAttribute("filename", report.getFileName());
			} else if ("1".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networknms_report.doc";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkDoc(fileName);// word综合报表

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("3".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkNewknms_report.doc";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkNewDoc(fileName);// word运行分析报表

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("4".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkNewknms_report.pdf";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkNewPdf(fileName);// pdf网络设备业务分析表

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if ("5".equals(str)) {
				ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
						reporthash);
				try {
					String file = "temp/networkknms_report.pdf";// 保存到项目文件夹下的指定文件夹
					String fileName = ResourceCenter.getInstance().getSysPath()
							+ file;// 获取系统文件夹路径
					report1.createReport_networkPDF(fileName);

					request.setAttribute("filename", fileName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/capreport/net/download.jsp";
		// zhushouzhi---------------------------end
		// return mapping.findForward("report_info");
	}

	private String downloadmultinetreport() {
		String oids = getParaValue("ids");
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		Vector vector = new Vector();
		// Hashtable reporthash = new Hashtable();
		try {
			Hashtable allreporthash = new Hashtable();
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					// Monitoriplist monitor = monitorManager.getById(ids[i]);
					// Equipment equipment = (Equipment)hostMonitor.get(i);
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// 按排序标志取各端口最新记录的列表
					String orderflag = "index";

					String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
							"ifOperStatus", "OutBandwidthUtilHdx",
							"InBandwidthUtilHdx" };

					vector = hostlastmanager.getInterface_share(ip,
							netInterfaceItem, orderflag, startdate, todate);
					PortconfigDao portdao = new PortconfigDao();
					Hashtable portconfigHash = new Hashtable();
					List reportports = new ArrayList();
					try {
						portconfigHash = portdao.getIpsHash(ip);
						reportports = portdao.getByIpAndReportflag(ip,
								new Integer(1));
					} catch (Exception e) {

					} finally {
						portdao.close();
					}
					reporthash.put("portconfigHash", portconfigHash);
					reporthash.put("reportports", reportports);
					if (reportports != null && reportports.size() > 0) {
						// 显示端口的流速图形
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
						String unit = "kb/s";
						String title = startdate + "至" + todate + "端口流速";
						String[] banden3 = { "InBandwidthUtilHdx",
								"OutBandwidthUtilHdx" };
						String[] bandch3 = { "入口流速", "出口流速" };

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports
									.get(k);
							// 按分钟显示报表
							Hashtable value = new Hashtable();
							value = daymanager.getmultiHisHdx(ip, "ifspeed",
									portconfig.getPortindex() + "", banden3,
									bandch3, startdate, todate, "UtilHdx");
							String reportname = "第" + portconfig.getPortindex()
									+ "(" + portconfig.getName() + ")端口流速"
									+ startdate + "至" + todate + "报表(按分钟显示)";
							p_drawchartMultiLineMonth(value, reportname,
									newip + portconfig.getPortindex()
											+ "ifspeed_day", 800, 200,
									"UtilHdx");
							// String url1 =
							// "/resource/image/jfreechart/"+newip+portconfig.getPortindex()+"ifspeed_day.png";
						}
					}

					reporthash.put("netifVector", vector);

					Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
							"Utilization", starttime, totime);
					String pingconavg = "";

					maxhash = new Hashtable();
					String cpumax = "";
					String avgcpu = "";
					if (cpuhash.get("max") != null) {
						cpumax = (String) cpuhash.get("max");
					}
					if (cpuhash.get("avgcpucon") != null) {
						avgcpu = (String) cpuhash.get("avgcpucon");
					}

					maxhash.put("cpumax", cpumax);
					maxhash.put("avgcpu", avgcpu);
					// 画图
					p_draw_line(cpuhash, "", newip + "cpu", 740, 120);
					Hashtable ConnectUtilizationhash = hostmanager
							.getCategory(ip, "Ping", "ConnectUtilization",
									starttime, totime);
					if (ConnectUtilizationhash.get("avgpingcon") != null)
						pingconavg = (String) ConnectUtilizationhash
								.get("avgpingcon");
					String ConnectUtilizationmax = "";
					maxping.put("avgpingcon", pingconavg);
					if (ConnectUtilizationhash.get("max") != null) {
						ConnectUtilizationmax = (String) ConnectUtilizationhash
								.get("max");
					}
					maxping.put("pingmax", ConnectUtilizationmax);

					p_draw_line(ConnectUtilizationhash, "", newip
							+ "ConnectUtilization", 740, 120);

					// 从内存中获得当前的跟此IP相关的IP-MAC的FDB表信息
					Hashtable _IpRouterHash = ShareData.getIprouterdata();
					vector = (Vector) _IpRouterHash.get(ip);
					if (vector != null)
						reporthash.put("iprouterVector", vector);

					Vector pdata = (Vector) pingdata.get(ip);
					// 把ping得到的数据加进去
					if (pdata != null && pdata.size() > 0) {
						for (int m = 0; m < pdata.size(); m++) {
							Pingcollectdata hostdata = (Pingcollectdata) pdata
									.get(m);
							if (hostdata.getSubentity().equals(
									"ConnectUtilization")) {
								reporthash.put("time", hostdata
										.getCollecttime());
								reporthash.put("Ping", hostdata.getThevalue());
								reporthash.put("ping", maxping);
							}
						}
					}

					// CPU
					Hashtable hdata = (Hashtable) sharedata.get(ip);
					if (hdata == null)
						hdata = new Hashtable();
					Vector cpuVector = new Vector();
					if (hdata.get("cpu") != null)
						cpuVector = (Vector) hdata.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// for(int si=0;si<cpuVector.size();si++){
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector
								.elementAt(0);
						maxhash.put("cpu", cpudata.getThevalue());
						reporthash.put("CPU", maxhash);
						// }
					} else {
						reporthash.put("CPU", maxhash);
					}
					reporthash.put("equipname", equipname);
					reporthash.put("memmaxhash", memmaxhash);
					reporthash.put("memavghash", memavghash);
					allreporthash.put(ip, reporthash);
				}
			}
			AbstractionReport1 report = new ExcelReport1(
					new IpResourceReport(), allreporthash);
			report.createReport_networkall("/temp/networknms_report.xls");
			request.setAttribute("filename", report.getFileName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private void p_draw_line(Hashtable hash, String title1, String title2,
			int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();

				TimeSeries ss = new TimeSeries(title1, Minute.class);
				TimeSeries[] s = { ss };
				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);
					Minute minute = new Minute(temp.get(Calendar.MINUTE), temp
							.get(Calendar.HOUR_OF_DAY), temp
							.get(Calendar.DAY_OF_MONTH), temp
							.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
					ss.addOrUpdate(minute, d);
				}
				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);

			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void draw_blank(String title1, String title2, int w, int h) {
		ChartGraph cg = new ChartGraph();
		TimeSeries ss = new TimeSeries(title1, Minute.class);
		TimeSeries[] s = { ss };
		try {
			Calendar temp = Calendar.getInstance();
			Minute minute = new Minute(temp.get(Calendar.MINUTE), temp
					.get(Calendar.HOUR_OF_DAY),
					temp.get(Calendar.DAY_OF_MONTH),
					temp.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
			ss.addOrUpdate(minute, null);
			cg.timewave(s, "x(时间)", "y", title1, title2, w, h);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String readyEdit() {
		DaoInterface dao = new HostNodeDao();
		setTarget("/topology/network/edit.jsp");
		return readyEdit(dao);
	}

	private String update() {
		HostNode vo = new HostNode();
		vo.setId(getParaIntValue("id"));
		vo.setAlias(getParaValue("alias"));
		vo.setManaged(getParaIntValue("managed") == 1 ? true : false);

		// 更新内存
		Host host = (Host) PollingEngine.getInstance().getNodeByID(vo.getId());
		host.setAlias(vo.getAlias());
		host.setManaged(vo.isManaged());

		// 更新数据库
		DaoInterface dao = new HostNodeDao();
		setTarget("/network.do?action=list");
		return update(dao, vo);
	}

	private String refreshsysname() {
		HostNodeDao dao = new HostNodeDao();
		String sysName = "";
		sysName = dao.refreshSysName(getParaIntValue("id"));

		// 更新内存
		Host host = (Host) PollingEngine.getInstance().getNodeByID(
				getParaIntValue("id"));
		if (host != null) {
			host.setSysName(sysName);
			host.setAlias(sysName);
		}

		return "/network.do?action=list";
	}

	private String delete() {
		String id = getParaValue("radio");

		PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
		HostNodeDao dao = new HostNodeDao();
		dao.delete(id);
		return "/network.do?action=list";
	}

	// zhushouzhi12-18服务器连通率报表

	public void createDocContext(String file) throws DocumentException,
			IOException {
		// 设置纸张大小
		Document document = new Document(PageSize.A4);
		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// 设置中文字体
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",
				BaseFont.NOT_EMBEDDED);
		// 标题字体风格
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// 正文字体风格
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("主机服务器连通率报表",titleFont);
		// 设置标题格式对齐方式
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// 设置 Table 表格
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		Table aTable = new Table(8);
		int width[] = { 30, 50, 50, 70, 50, 50, 60, 60 };
		aTable.setWidths(width);
		aTable.setWidth(100); // 占页面宽度 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// 居中显示
		aTable.setAutoFillEmptyCells(true); // 自动填满
		aTable.setBorderWidth(1); // 边框宽度
		aTable.setBorderColor(new Color(0, 125, 255)); // 边框颜色
		aTable.setPadding(2);// 衬距，看效果就知道什么意思了
		aTable.setSpacing(0);// 即单元格之间的间距
		aTable.setBorder(2);// 边框
		aTable.endHeaders();

		Cell cell0 = new Cell("");
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		Cell cell1 = new Cell("IP地址");
		Cell cell11 = new Cell("设备名称");
		Cell cell2 = new Cell("操作系统");
		Cell cell3 = new Cell("平均连通率");
		Cell cell4 = new Cell("宕机次数");
		Cell cell13 = null;
		cell13 = new Cell("平均响应时间(ms)");
		Cell cell14 = new Cell("最大响应时间(ms)");
		cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell13.setBackgroundColor(Color.LIGHT_GRAY);
		cell14.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell13);
		aTable.addCell(cell14);
		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String osname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				String responseavg = (String) _pinglist.get(5);
				String responsemax = (String) _pinglist.get(6);
				Cell cell5 = new Cell(i + 1 + "");
				Cell cell6 = new Cell(ip);
				Cell cell7 = new Cell(equname);
				Cell cell8 = new Cell(osname);
				Cell cell9 = new Cell(avgping);
				Cell cell10 = new Cell(downnum);
				Cell cell15 = new Cell(responseavg.replace("毫秒", ""));
				Cell cell16 = new Cell(responsemax.replace("毫秒", ""));
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER); // 居中
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell16.setHorizontalAlignment(Element.ALIGN_CENTER);

				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell15);
				aTable.addCell(cell16);

			}
		}
		document.add(aTable);
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		// Image img =
		// Image.getInstance(ResourceCenter.getInstance().getSysPath()
		// + "/resource/image/jfreechart/" + newip + "ConnectUtilization"
		// + ".png");
		// img.setAbsolutePosition(0, 0);
		// img.setAlignment(Image.LEFT);// 设置图片显示位置
		// document.add(img);
		document.close();
	}

	// zhushouzhi调用服务器连通率报表方法
	public String createdoc() {
		String file = "/temp/liantonglvbaobiao.doc";// 保存到项目文件夹下的指定文件夹
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
		try {
			createDocContext(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// zhushouzhi服务器事件报表
	public void createDocContextEvent(String file) throws DocumentException,
			IOException {
		// 设置纸张大小
		Document document = new Document(PageSize.A4);
		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
		RtfWriter2.getInstance(document, new FileOutputStream(file));
		document.open();
		// 设置中文字体
		BaseFont bfChinese = BaseFont.createFont("Times-Roman", "",
				BaseFont.NOT_EMBEDDED);
		// 标题字体风格
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// 正文字体风格
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("主机服务器事件报表",titleFont);
		// 设置标题格式对齐方式
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		// 设置 Table 表格
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List eventlist = (List) session.getAttribute("eventlist");
		Table aTable = new Table(12);
//		int width[] = { 30, 50, 55, 55, 55, 40, 40, 40, 55, 55, 55, 55 };
//		aTable.setWidths(width);
		aTable.setWidth(100); // 占页面宽度 90%
		aTable.setAlignment(Element.ALIGN_CENTER);// 居中显示
		aTable.setAutoFillEmptyCells(true); // 自动填满
		aTable.setBorderWidth(1); // 边框宽度
		aTable.setBorderColor(new Color(0, 125, 255)); // 边框颜色
		aTable.setPadding(2);// 衬距，看效果就知道什么意思了
		aTable.setSpacing(0);// 即单元格之间的间距
		aTable.setBorder(2);// 边框
		aTable.endHeaders();

		Cell cell0 = new Cell("");
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		Cell cell1 = new Cell("IP地址");
		Cell cell2 = new Cell("设备名称");
		Cell cell3 = new Cell("操作系统");
		Cell cell4 = new Cell("事件总数(个)");
		Cell cell5 = new Cell("普通(个)");
		Cell cell6 = new Cell("紧急(个)");
		Cell cell7 = new Cell("严重(个)");
		Cell cell8 = new Cell("连通率事件(个)");
		Cell cell9 = new Cell("cpu事件(个)");
		Cell cell10 = new Cell("端口事件(个)");
		Cell cell11 = new Cell("流速事件(个)");
		
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell9.setBackgroundColor(Color.LIGHT_GRAY);
		cell10.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);

		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell5);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		aTable.addCell(cell9);
		aTable.addCell(cell10);
		aTable.addCell(cell11);

		if (eventlist != null && eventlist.size() > 0) {
			for (int i = 0; i < eventlist.size(); i++) {
				List _eventlist = (List) eventlist.get(i);
				String ip = (String) _eventlist.get(0);
				String equname = (String) _eventlist.get(1);
				String osname = (String) _eventlist.get(2);
				String sum = (String) _eventlist.get(3);
				String levelone = (String) _eventlist.get(4);
				String leveltwo = (String) _eventlist.get(5);
				String levelthree = (String) _eventlist.get(6);
				String pingvalue = (String) _eventlist.get(7);
				String memvalue = (String) _eventlist.get(8);
				String diskvalue = (String) _eventlist.get(9);
				String cpuvalue = (String) _eventlist.get(10);
				Cell cell13 = new Cell(i + 1 + "");
				Cell cell14 = new Cell(ip);
				Cell cell15 = new Cell(equname);
				Cell cell16 = new Cell(osname);
				Cell cell17 = new Cell(sum);
				Cell cell18 = new Cell(levelone);
				Cell cell19 = new Cell(leveltwo);
				Cell cell20 = new Cell(levelthree);
				Cell cell21 = new Cell(pingvalue);
				Cell cell22 = new Cell(memvalue);
				Cell cell23 = new Cell(diskvalue);
				Cell cell24 = new Cell(cpuvalue);

				/*
				 * cell5.setHorizontalAlignment(Element.ALIGN_CENTER); //居中
				 * cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				 */
				/*
				 * if(i == 1){ Cell cell12 = new Cell(i + 1 + ""+ip);
				 * cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
				 * cell12.setColspan(2);//合并两列的单元格 aTable.addCell(cell12);
				 * aTable.addCell(cell7); aTable.addCell(cell8);
				 * aTable.addCell(cell9); aTable.addCell(cell10); }
				 */

				aTable.addCell(cell13);
				aTable.addCell(cell14);
				aTable.addCell(cell15);
				aTable.addCell(cell16);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
				aTable.addCell(cell19);
				aTable.addCell(cell20);
				aTable.addCell(cell21);
				aTable.addCell(cell22);
				aTable.addCell(cell23);
				aTable.addCell(cell24);
			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	// zhushouzhi调用服务器事件报表方法
	public String createdocEvent() {
		String file = "/temp/shijianbaobiao.doc";// 保存到项目文件夹下的指定文件夹
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
		try {
			createDocContextEvent(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// 之上为zhushouzhi
	// wxy add
	public String networkReport() {
		StringBuffer s = new StringBuffer();

		User current_user = (User) session
				.getAttribute(SessionConstant.CURRENT_USER);
		if (current_user.getBusinessids() != null) {
			if (current_user.getBusinessids() != "-1") {
				String[] bids = current_user.getBusinessids().split(",");
				if (bids.length > 0) {
					for (int ii = 0; ii < bids.length; ii++) {
						if (bids[ii].trim().length() > 0) {
							s.append(" bid like '%").append(bids[ii]).append(
									"%' ");
							if (ii != bids.length - 1)
								s.append(" or ");
						}
					}

				}

			}
		}
		// InterfaceTempDao interfaceDao = new InterfaceTempDao();
		PortconfigDao portconfigDao = new PortconfigDao();
		List interfaceList = new ArrayList();
		try {
			interfaceList = portconfigDao.getAllBySms();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			portconfigDao.close();
		}
		HostNodeDao dao = new HostNodeDao();
		try {
            request.setAttribute("list", dao.loadNetworkByBid(1, current_user
            		.getBusinessids()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
		request.setAttribute("interfaceList", interfaceList);
		return "/capreport/net/netWorkReport.jsp";
	}

	private String networkReportConfig() {
		String id = this.getParaValue("id");

		UtilReportDao dao = new UtilReportDao();
		UtilReport report = dao.findByBid(id);
		String ids = report.getIds();
		String[] idValue = null;
		if (ids != null && !ids.equals("null") && !ids.equals("")) {
			idValue = new String[ids.split(",").length];
			idValue = ids.split(",");
		}
		List<String> list = new ArrayList<String>();
		if (idValue != null) {
			for (int i = 0; i < idValue.length; i++) {
				list.add(idValue[i]);
			}
		}
		SubscribeResources vo = new SubscribeResources();
		SubscribeResourcesDao subDao = new SubscribeResourcesDao();

		vo = subDao.findById(id);
		// ////////////
		String[] frequencyName = { "每天", "每周", "每月", "每季", "每年" };
		String[] monthCh = { " 1月", " 2月", " 3月", " 4月", " 5月", " 6月", " 7月", " 8月",
				" 9月", " 10月", " 11月", " 12月" };
		String[] weekCh = { " 星期日", " 星期一", " 星期二", " 星期三", " 星期四", " 星期五", " 星期六" };
		String[] dayCh = null;
		String[] hourCh =null;

		StringBuffer sb = new StringBuffer();
		int frequency = vo.getReport_sendfrequency();
		String month = splitDate(vo.getReport_time_month(), monthCh, "month");
		String week = splitDate(vo.getReport_time_week(), weekCh, "week");
		
		String day = splitDate(vo.getReport_time_day(), dayCh, "day");
		String hour = splitDate(vo.getReport_time_hou(), hourCh, "hour");
		sb.append(frequencyName[frequency - 1] + " ");
		if (month != null && !month.equals(""))
			sb.append(" 月份：(" + month + ")");
		if (week != null && !week.equals(""))
			sb.append(" 星期:(" + week + ")");
		if (day != null && !day.equals(""))
			sb.append(" 日期：(" + day + ")");
		if (hour != null && !hour.equals(""))
			sb.append(" 时间：(" + hour + ")");
		String sendDate = sb.toString();
		// ///////////
		Vector vector = new Vector();
		vector.add(0, report);
		vector.add(1, vo);
		request.setAttribute("vector", vector);
		request.setAttribute("list", list);
		request.setAttribute("sendDate", sendDate);
		return "/capreport/net/netWorkReportDetail.jsp";
	}

	public String splitDate(String item, String[] itemCh, String type) {
		String[] idValue = null;
		String value = "";
		idValue = new String[item.split("/").length];
		idValue = item.split("/");
		
		for (int i = 0; i < idValue.length; i++) {
			if (!idValue[i].equals("")){
				if (type.equals("week")) {
							value += itemCh[Integer.parseInt(idValue[i])];
				}else if (type.equals("day")) {
							value += (idValue[i]+"日 ");
				}else if (type.equals("hour")) {
							value += (idValue[i]+"时 ");
				}else {
					       value += itemCh[Integer.parseInt(idValue[i]) - 1];
				}
			}
		}
		
		return value;
	}

	public String executeReport() {
		Hashtable cpuhash = new Hashtable();

		String id = request.getParameter("id");
		String startTime = request.getParameter("startTime");
		if (startTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startTime = sdf.format(new Date()) + " 00:00:00";
		}
		String toTime = request.getParameter("toTime");
		if (toTime == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			toTime = sdf.format(new Date()) + " 23:59:59";
		}
		HostNodeDao hostdao = new HostNodeDao();
		id = "1";
		Host host = (Host) PollingEngine.getInstance().getNodeByID(
				Integer.parseInt(id));
		try {
			cpuhash = hostmanager.getCategory(host.getIpAddress(), "CPU",
					"Utilization", startTime, toTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("cpuhash", cpuhash);
		return "/capreport/net/netWorkReport.jsp";
	}
	private String downloadReport(){
		String ids=getParaValue("ids");
		String type=getParaValue("type");
		String exportType=getParaValue("exportType");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (ids==null||ids.equals("")||ids.equals("null")) {
    		String id = request.getParameter("id");
    		
    		if(id.equals("null"))return null;
    		UtilReport report=new UtilReport();
    		UtilReportDao dao=new UtilReportDao();
    		report=(UtilReport) dao.findByBid(id);
    		ids=report.getIds();
		}
		String filename="";
		if (type.equals("net")) {
			if (exportType.equals("xls")) {
				filename="/temp/network_report.xls";
			}else if (exportType.equals("doc")) {
				filename="/temp/network_report.doc";
			}else if (exportType.equals("pdf")) {
				filename="/temp/network_report.pdf";
			}
		}else if (type.equals("host")) {
			if (exportType.equals("xls")) {
				filename="/temp/host_report.xls";
			}else if (exportType.equals("doc")) {
				filename="/temp/host_report.doc";
			}else if (exportType.equals("pdf")) {
				filename="/temp/host_report.pdf";
			}
		}else if (type.equals("db")) {
			if (exportType.equals("xls")) {
				filename="/temp/db_report.xls";
			}else if (exportType.equals("doc")) {
				filename="/temp/db_report.doc";
			}else if (exportType.equals("pdf")) {
				filename="/temp/db_report.pdf";
			}
		}else if (type.equals("midware")) {
			if (exportType.equals("xls")) {
				filename="/temp/db_report.xls";
			}else if (exportType.equals("doc")) {
				filename="/temp/db_report.doc";
			}else if (exportType.equals("pdf")) {
				filename="/temp/db_report.pdf";
			}
		}
		String filePath=ResourceCenter.getInstance().getSysPath()+filename;
		String startTime=getParaValue("startdate");
		String toTime=getParaValue("todate");
		if (startTime == null) {
			startTime = sdf.format(new Date()) + " 00:00:00";
		} else {
			startTime = startTime + " 00:00:00";
		}
		if (toTime == null) {
			toTime = sdf.format(new Date()) + " 23:59:59";
		} else {
			toTime = toTime + " 23:59:59";
		}
		ReportExport export=new ReportExport();
		export.exportReport(ids, type, filePath, startTime, toTime,exportType);
		request.setAttribute("filename", filePath);
		return "/capreport/net/download.jsp";
	}
	public String execute(String action) {

		if (action.equals("list"))
			return list();
		if (action.equals("eventlist"))
			return eventlist();
		if (action.equals("netmultilist"))
			return netmultilist();
		if (action.equals("netping"))
			return netping();
		if (action.equals("netevent"))
			return netevent();
		if (action.equals("downloadpingreport"))
			return downloadpingreport();
		if (action.equals("downloadnetpingreport"))
			return downloadnetpingreport();
		if (action.equals("downloadneteventreport"))
			return downloadneteventreport();
		if (action.equals("downloadselfnetreport"))
			return downloadselfnetreport();
		if (action.equals("downloadmultinetreport"))
			return downloadmultinetreport();
		if (action.equals("createdoc"))
			return createdoc();
		if (action.equals("createdocEvent"))
			return createdocEvent();
		if (action.equals("createpdf"))
			return createpdf();
		if (action.equals("createdocEventPDF"))
			return createdocEventPDF();
		if (action.equals("choceDoc"))
			return choceDoc();
		if (action.equals("downloadselfnetchocereport"))
			return downloadselfnetchocereport();

		// 山西移动oa网络接口平均流速统计报表（konglq）
		if (action.equals("netIfxls")) {
			return downloadsxoaIfreport();
		}
		if (action.equals("showPingReport")) {
			return showNetworkPingReport();
		}
		if (action.equals("showEventReport")) {
			return showNetworkEventReport();
		}
		if (action.equals("showCompositeReport")) {
			return showNetworkCompositeReport();
		}
		if (action.equals("showNetworkConfigReport")) {
			return showNetworkConfigReport();
		}
		if (action.equals("downloadNetworkPingReport")) {
			return downloadNetworkPingReport();
		}
		if (action.equals("downloadNetworkEventReport")) {
			return downloadNetworkEventReport();
		}
		if (action.equals("downloadNetworkCompositeReport")) {
			return downloadNetworkCompositeReport();
		}
		if (action.equals("downloadNetworkConfigReport")) {
			return downloadNetworkConfigReport();
		}
		if (action.equals("find")) {
			return find();
		}
		if (action.equals("networkReport"))
			return networkReport();
		if (action.equals("executeReport"))
			return executeReport();
		if (action.equals("networkReportConfig"))
			return networkReportConfig();
		if (action.equals("downloadReport"))
			return downloadReport();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	private void getTime(HttpServletRequest request, String[] time) {
		Calendar current = new GregorianCalendar();
		String key = getParaValue("beginhour");
		if (getParaValue("beginhour") == null) {
			Integer hour = new Integer(current.get(Calendar.HOUR_OF_DAY));
			request.setAttribute("beginhour", new Integer(hour.intValue() - 1));
			request.setAttribute("endhour", hour);
			// mForm.setBeginhour(new Integer(hour.intValue()-1));
			// mForm.setEndhour(hour);
		}
		if (getParaValue("begindate") == null) {
			current.set(Calendar.MINUTE, 59);
			current.set(Calendar.SECOND, 59);
			time[1] = datemanager.getDateDetail(current);
			current.add(Calendar.HOUR_OF_DAY, -1);
			current.set(Calendar.MINUTE, 0);
			current.set(Calendar.SECOND, 0);
			time[0] = datemanager.getDateDetail(current);

			java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
					"yyyy-M-d");
			String begindate = "";
			begindate = timeFormatter.format(new java.util.Date());
			request.setAttribute("begindate", begindate);
			request.setAttribute("enddate", begindate);
			// mForm.setBegindate(begindate);
			// mForm.setEnddate(begindate);
		} else {
			String temp = getParaValue("begindate");
			time[0] = temp + " " + getParaValue("beginhour") + ":00:00";
			temp = getParaValue("enddate");
			time[1] = temp + " " + getParaValue("endhour") + ":59:59";
		}
		if (getParaValue("startdate") == null) {
			current.set(Calendar.MINUTE, 59);
			current.set(Calendar.SECOND, 59);
			time[1] = datemanager.getDateDetail(current);
			current.add(Calendar.HOUR_OF_DAY, -1);
			current.set(Calendar.MINUTE, 0);
			current.set(Calendar.SECOND, 0);
			time[0] = datemanager.getDateDetail(current);

			java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
					"yyyy-M-d");
			String startdate = "";
			startdate = timeFormatter.format(new java.util.Date());
			request.setAttribute("startdate", startdate);
			request.setAttribute("todate", startdate);
			// mForm.setStartdate(startdate);
			// mForm.setTodate(startdate);
		} else {
			String temp = getParaValue("startdate");
			time[0] = temp + " " + getParaValue("beginhour") + ":00:00";
			temp = getParaValue("todate");
			time[1] = temp + " " + getParaValue("endhour") + ":59:59";
		}

	}

	private String doip(String ip) {
		// String newip="";
		// for(int i=0;i<3;i++){
		// int p=ip.indexOf(".");
		// newip+=ip.substring(0,p);
		// ip=ip.substring(p+1);
		// }
		// newip+=ip;
		// //System.out.println("newip="+newip);
		// return newip;
		ip = ip.replaceAll("\\.", "_");
		return ip;
	}

	private void p_drawchartMultiLineMonth(Hashtable hash, String title1,
			String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String unit = "";
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					// TimeSeries ss = new TimeSeries(key,Hour.class);
					TimeSeries ss = new TimeSeries(key, Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						unit = "y(kb/s)";
					} else {
						unit = "y(%)";
					}
					// 流速
					for (int j = 0; j < value.length; j++) {
						String val = value[j];
						if (val != null && val.indexOf("&") >= 0) {
							String[] splitstr = val.split("&");
							String splittime = splitstr[0];
							Double v = new Double(splitstr[1]);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date da = sdf.parse(splittime);
							Calendar tempCal = Calendar.getInstance();
							tempCal.setTime(da);
							// UtilHdx obj = (UtilHdx)vector.get(j);
							// Double v=new Double(obj.getThevalue());
							// Calendar temp = obj.getCollecttime();
							// new org.jfree.data.time.Hour(newTime)

							// Hour hour=new
							// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// Day day=new
							// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// ss.addOrUpdate(new
							// org.jfree.data.time.Day(da),v);
							// ss.addOrUpdate(hour,v);
							Minute minute = new Minute(tempCal
									.get(Calendar.MINUTE), tempCal
									.get(Calendar.HOUR_OF_DAY), tempCal
									.get(Calendar.DAY_OF_MONTH), tempCal
									.get(Calendar.MONTH) + 1, tempCal
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					}
					// }
					s[i] = ss;
				}
				cg.timewave(s, "x(时间)", unit, title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void p_drawchartMultiLineYear(Hashtable hash, String title1,
			String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String unit = "";
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Hour.class);
					// TimeSeries ss = new TimeSeries(key,Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						unit = "y(kb/s)";
					} else {
						unit = "y(%)";
					}
					// 流速
					for (int j = 0; j < value.length; j++) {
						String val = value[j];
						if (val != null && val.indexOf("&") >= 0) {
							String[] splitstr = val.split("&");
							String splittime = splitstr[0];
							Double v = new Double(splitstr[1]);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date da = sdf.parse(splittime);
							Calendar tempCal = Calendar.getInstance();
							tempCal.setTime(da);
							// UtilHdx obj = (UtilHdx)vector.get(j);
							// Double v=new Double(obj.getThevalue());
							// Calendar temp = obj.getCollecttime();
							// new org.jfree.data.time.Hour(newTime)

							// Hour hour=new
							// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// Day day=new
							// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							ss.addOrUpdate(new org.jfree.data.time.Hour(da), v);
							// Minute minute=new
							// Minute(tempCal.get(Calendar.MINUTE),tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
							// ss.addOrUpdate(day,v);
						}
					}
					// }
					s[i] = ss;
				}
				cg.timewave(s, "x(时间)", unit, title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void drawchartMultiLineMonth(Hashtable hash, String title1,
			String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			// String unit = (String)hash.get("unit");
			// hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					// TimeSeries ss = new TimeSeries(key,Hour.class);
					TimeSeries ss = new TimeSeries(key, Minute.class);
					String[] value = (String[]) hash.get(key);
					if (flag.equals("UtilHdx")) {
						// 流速
						for (int j = 0; j < value.length; j++) {
							String val = value[j];
							if (val != null && val.indexOf("&") >= 0) {
								String[] splitstr = val.split("&");
								String splittime = splitstr[0];
								Double v = new Double(splitstr[1]);
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date da = sdf.parse(splittime);
								Calendar tempCal = Calendar.getInstance();
								tempCal.setTime(da);
								// UtilHdx obj = (UtilHdx)vector.get(j);
								// Double v=new Double(obj.getThevalue());
								// Calendar temp = obj.getCollecttime();
								// new org.jfree.data.time.Hour(newTime)

								// Hour hour=new
								// Hour(tempCal.get(Calendar.HOUR_OF_DAY),tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
								// Day day=new
								// Day(tempCal.get(Calendar.DAY_OF_MONTH),tempCal.get(Calendar.MONTH)+1,tempCal.get(Calendar.YEAR));
								// ss.addOrUpdate(new
								// org.jfree.data.time.Day(da),v);
								// ss.addOrUpdate(hour,v);
								Minute minute = new Minute(tempCal
										.get(Calendar.MINUTE), tempCal
										.get(Calendar.HOUR_OF_DAY), tempCal
										.get(Calendar.DAY_OF_MONTH), tempCal
										.get(Calendar.MONTH) + 1, tempCal
										.get(Calendar.YEAR));
								ss.addOrUpdate(minute, v);
							}
						}
					}
					s[i] = ss;
				}
				cg.timewave(s, "x(时间)", "y(kb/s)", title1, title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private void p_drawchartMultiLine(Hashtable hash, String title1,
			String title2, int w, int h, String flag) {
		if (hash.size() != 0) {
			String unit = (String) hash.get("unit");
			hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			if (keys == null) {
				draw_blank(title1, title2, w, h);
				return;
			}
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Minute.class);
					Vector vector = (Vector) (hash.get(key));
					if (flag.equals("AllUtilHdxPerc")) {
						// 综合带宽利用率
						for (int j = 0; j < vector.size(); j++) {
							/*
							 * //if
							 * (title1.equals("带宽利用率")||title1.equals("端口流速")){
							 * AllUtilHdxPerc obj =
							 * (AllUtilHdxPerc)vector.get(j); Double v=new
							 * Double(obj.getThevalue()); Calendar temp =
							 * obj.getCollecttime(); Minute minute=new
							 * Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
							 * ss.addOrUpdate(minute,v); //}
							 */
						}
					} else if (flag.equals("AllUtilHdx")) {
						// 综合流速
						for (int j = 0; j < vector.size(); j++) {
							// if
							// (title1.equals("带宽利用率")||title1.equals("端口流速")){
							AllUtilHdx obj = (AllUtilHdx) vector.get(j);
							Double v = new Double(obj.getThevalue());
							Calendar temp = obj.getCollecttime();
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
							// }
						}
					} else if (flag.equals("UtilHdxPerc")) {
						// 带宽利用率
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}

					} else if (flag.equals("UtilHdx")) {
						// 流速
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("ErrorsPerc")) {
						// 流速
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("DiscardsPerc")) {
						// 流速
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					} else if (flag.equals("Packs")) {
						// 数据包
						for (int j = 0; j < vector.size(); j++) {
							Vector obj = (Vector) vector.get(j);
							Double v = new Double((String) obj.get(0));
							String dt = (String) obj.get(1);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date time1 = sdf.parse(dt);
							Calendar temp = Calendar.getInstance();
							temp.setTime(time1);
							Minute minute = new Minute(temp
									.get(Calendar.MINUTE), temp
									.get(Calendar.HOUR_OF_DAY), temp
									.get(Calendar.DAY_OF_MONTH), temp
									.get(Calendar.MONTH) + 1, temp
									.get(Calendar.YEAR));
							ss.addOrUpdate(minute, v);
						}
					}
					s[i] = ss;
				}
				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	private static CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(10, "", "values1");
		dataset.addValue(20, "", "values2");
		dataset.addValue(30, "", "values3");
		dataset.addValue(40, "", "values4");
		dataset.addValue(50, "", "values5");
		return dataset;
	}

	public void draw_column(Hashtable bighash, String title1, String title2,
			int w, int h) {
		if (bighash.size() != 0) {
			ChartGraph cg = new ChartGraph();
			int size = bighash.size();
			double[][] d = new double[1][size];
			String c[] = new String[size];
			Hashtable hash;
			for (int j = 0; j < size; j++) {
				hash = (Hashtable) bighash.get(new Integer(j));
				c[j] = (String) hash.get("name");
				d[0][j] = Double.parseDouble((String) hash.get("Utilization"
						+ "value"));
			}
			String rowKeys[] = { "" };
			CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
					rowKeys, c, d);// .createCategoryDataset(rowKeys,
									// columnKeys, data);
			cg.zhu(title1, title2, dataset, w, h);
		} else {
			draw_blank(title1, title2, w, h);
		}
		bighash = null;
	}

	private void p_drawchartMultiLine(Hashtable hash, String title1,
			String title2, int w, int h) {
		if (hash.size() != 0) {
			String unit = (String) hash.get("unit");
			hash.remove("unit");
			String[] keys = (String[]) hash.get("key");
			if (keys == null) {
				draw_blank(title1, title2, w, h);
				return;
			}
			ChartGraph cg = new ChartGraph();
			TimeSeries[] s = new TimeSeries[keys.length];
			try {
				for (int i = 0; i < keys.length; i++) {
					String key = keys[i];
					TimeSeries ss = new TimeSeries(key, Minute.class);
					Vector vector = (Vector) (hash.get(key));
					for (int j = 0; j < vector.size(); j++) {
						// if (title1.equals("内存利用率")){
						Vector obj = (Vector) vector.get(j);
						// Memorycollectdata obj =
						// (Memorycollectdata)vector.get(j);
						Double v = new Double((String) obj.get(0));
						String dt = (String) obj.get(1);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date time1 = sdf.parse(dt);
						Calendar temp = Calendar.getInstance();
						temp.setTime(time1);
						// Calendar temp = obj.getCollecttime();
						Minute minute = new Minute(temp.get(Calendar.MINUTE),
								temp.get(Calendar.HOUR_OF_DAY), temp
										.get(Calendar.DAY_OF_MONTH), temp
										.get(Calendar.MONTH) + 1, temp
										.get(Calendar.YEAR));
						ss.addOrUpdate(minute, v);
						// }
					}
					s[i] = ss;
				}
				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);
				hash = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			draw_blank(title1, title2, w, h);
		}
	}

	// zhushouzhi--------------------------start pdf
	public void createPdfContextPing(String file) throws DocumentException,
			IOException {

		// com.lowagie.text.Font FontChinese = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);

		// 设置纸张大小

		Document document = new Document(PageSize.A4);
		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// 设置中文字体
		BaseFont bfChinese = BaseFont.createFont("STSong-Light",
				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// 标题字体风格
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// com.lowagie.text.Font titleFont = new
		// com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.NORMAL);
		// 正文字体风格
		Font contextFont = new com.lowagie.text.Font(bfChinese, 11, Font.NORMAL);
		Paragraph title = new Paragraph("主机服务器连通率报表", titleFont);
		// 设置标题格式对齐方式
		title.setAlignment(Element.ALIGN_CENTER);
		// title.setFont(titleFont);
		document.add(title);
		document.add(new Paragraph("\n"));
		// 设置 Table 表格
		Font fontChinese = new Font(bfChinese, 11, Font.NORMAL, Color.black);
		List pinglist = (List) session.getAttribute("pinglist");
		PdfPTable aTable = new PdfPTable(8);

		int width[] = { 30, 50, 50, 70, 50, 50, 60, 60 };
		aTable.setWidthPercentage(100);
		aTable.setWidths(width);

		PdfPCell c = new PdfPCell(new Phrase("", fontChinese));
		c.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(c);
		PdfPCell cell1 = new PdfPCell(new Phrase("IP地址", fontChinese));
		PdfPCell cell11 = new PdfPCell(new Phrase("设备名称", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("操作系统", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("平均连通率", contextFont));
		PdfPCell cell4 = new PdfPCell(new Phrase("宕机次数", contextFont));
		PdfPCell cell15 = new PdfPCell(new Phrase("平均响应时间(ms)", contextFont));
		PdfPCell cell16 = new PdfPCell(new Phrase("最大响应时间(ms)", contextFont));
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell4.setBackgroundColor(Color.LIGHT_GRAY);
		cell15.setBackgroundColor(Color.LIGHT_GRAY);
		cell16.setBackgroundColor(Color.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell15.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell16.setVerticalAlignment(Element.ALIGN_MIDDLE);
		aTable.addCell(cell1);
		aTable.addCell(cell11);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell4);
		aTable.addCell(cell15);
		aTable.addCell(cell16);
		if (pinglist != null && pinglist.size() > 0) {
			for (int i = 0; i < pinglist.size(); i++) {
				List _pinglist = (List) pinglist.get(i);
				String ip = (String) _pinglist.get(0);
				String equname = (String) _pinglist.get(1);
				String osname = (String) _pinglist.get(2);
				String avgping = (String) _pinglist.get(3);
				String downnum = (String) _pinglist.get(4);
				String responseavg = (String) _pinglist.get(5);
				String responsemax = (String) _pinglist.get(6);
				PdfPCell cell5 = new PdfPCell(new Phrase(i + 1 + ""));
				PdfPCell cell6 = new PdfPCell(new Phrase(ip));
				PdfPCell cell7 = new PdfPCell(new Paragraph(equname,
						fontChinese));
				PdfPCell cell8 = new PdfPCell(new Phrase(osname, contextFont));
				PdfPCell cell9 = new PdfPCell(new Phrase(avgping));
				PdfPCell cell10 = new PdfPCell(new Phrase(downnum));

				PdfPCell cell17 = new PdfPCell(new Phrase(responseavg.replace(
						"毫秒", "")));
				PdfPCell cell18 = new PdfPCell(new Phrase(responsemax.replace(
						"毫秒", "")));

				cell5.setHorizontalAlignment(Element.ALIGN_CENTER); // 居中
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell18.setHorizontalAlignment(Element.ALIGN_CENTER);

				aTable.addCell(cell5);
				aTable.addCell(cell6);
				aTable.addCell(cell7);
				aTable.addCell(cell8);
				aTable.addCell(cell9);
				aTable.addCell(cell10);
				aTable.addCell(cell17);
				aTable.addCell(cell18);

			}
		}
		document.add(aTable);
		// document.add(new Paragraph("\n"));
		// String ipaddress = (String)request.getParameter("ipaddress");
		// String newip = doip(ipaddress);
		// Image img =
		// Image.getInstance(ResourceCenter.getInstance().getSysPath()
		// + "/resource/image/jfreechart/" + newip + "ConnectUtilization"
		// + ".png");
		// img.setAlignment(Image.LEFT);// 设置图片显示位置
		// img.scalePercent(75);
		// document.add(img);
		document.close();
	}

	public void createDocContexxtEventPDF() {

	}

	// zhushouzhi调用服务器连通率报表方法
	public String createpdf() {
		String file = "/temp/liantonglvbaobiao.PDF";// 保存到项目文件夹下的指定文件夹
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
		try {
			createPdfContextPing(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	// zhushouzhi调用服务器连通率报表方法---pdf

	// zhushouzhi-----------------------pdf--eventlist
	public void createDocContextEventPDF(String file) throws DocumentException,
			IOException {
		// 设置纸张大小
		Document document = new Document(PageSize.A4);
		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		// 设置中文字体
		BaseFont bfChinese = BaseFont.createFont("STSong-Light",
				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

		// 标题字体风格
		Font titleFont = new Font(bfChinese, 12, Font.BOLD);
		// 正文字体风格
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph title = new Paragraph("主机服务器事件报表", titleFont);
		// 设置标题格式对齐方式
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(10);
		// title.setFont(titleFont);
		document.add(title);
		// 设置 Table 表格
		Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.black);
		List eventlist = (List) session.getAttribute("eventlist");
		PdfPTable aTable = new PdfPTable(12);
//		int width[] = { 30, 70, 55, 55, 55, 40, 40, 40, 55, 55, 55, 55 };
//		aTable.setWidths(width);
		aTable.setWidthPercentage(100);

		PdfPCell cell0 = new PdfPCell(new Phrase(""));
		cell0.setBackgroundColor(Color.LIGHT_GRAY);
		aTable.addCell(cell0);
		PdfPCell cell1 = new PdfPCell(new Phrase("IP地址", contextFont));
		PdfPCell cell2 = new PdfPCell(new Phrase("设备名称", contextFont));
		PdfPCell cell3 = new PdfPCell(new Phrase("操作系统", contextFont));
		PdfPCell cell5 = new PdfPCell(new Phrase("事件总数(个)", contextFont));
		PdfPCell cell61 = new PdfPCell(new Phrase("普通（个）", contextFont));
		PdfPCell cell6 = new PdfPCell(new Phrase("紧急(个)", contextFont));
		PdfPCell cell7 = new PdfPCell(new Phrase("严重(个)", contextFont));
		PdfPCell cell8 = new PdfPCell(new Phrase("连通率事件(个)", contextFont));
		PdfPCell cell9 = new PdfPCell(new Phrase("cpu事件(个)", contextFont));
		PdfPCell cell10 = new PdfPCell(new Phrase("端口事件(个)", contextFont));
		PdfPCell cell11 = new PdfPCell(new Phrase("流速事件(个)", contextFont));

		cell1.setBackgroundColor(Color.LIGHT_GRAY);
		cell2.setBackgroundColor(Color.LIGHT_GRAY);
		cell3.setBackgroundColor(Color.LIGHT_GRAY);
		cell5.setBackgroundColor(Color.LIGHT_GRAY);
		cell61.setBackgroundColor(Color.LIGHT_GRAY);
		cell6.setBackgroundColor(Color.LIGHT_GRAY);
		cell7.setBackgroundColor(Color.LIGHT_GRAY);
		cell8.setBackgroundColor(Color.LIGHT_GRAY);
		cell9.setBackgroundColor(Color.LIGHT_GRAY);
		cell10.setBackgroundColor(Color.LIGHT_GRAY);
		cell11.setBackgroundColor(Color.LIGHT_GRAY);
		
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell61.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell61.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell11.setHorizontalAlignment(Element.ALIGN_CENTER);

		aTable.addCell(cell1);
		aTable.addCell(cell2);
		aTable.addCell(cell3);
		aTable.addCell(cell5);
		aTable.addCell(cell61);
		aTable.addCell(cell6);
		aTable.addCell(cell7);
		aTable.addCell(cell8);
		aTable.addCell(cell9);
		aTable.addCell(cell10);
		aTable.addCell(cell11);
		document.add(aTable);
		aTable = new PdfPTable(12);
//		aTable.setWidths(width);
		aTable.setWidthPercentage(100);
		if (eventlist != null && eventlist.size() > 0) {
			for (int i = 0; i < eventlist.size(); i++) {
				List _eventlist = (List) eventlist.get(i);
				String ip = (String) _eventlist.get(0);
				String equname = (String) _eventlist.get(1);
				String osname = (String) _eventlist.get(2);
				String sum = (String) _eventlist.get(3);
				String levelone = (String) _eventlist.get(4);
				String leveltwo = (String) _eventlist.get(5);
				String levelthree = (String) _eventlist.get(6);
				String pingvalue = (String) _eventlist.get(7);
				String memvalue = (String) _eventlist.get(8);
				String diskvalue = (String) _eventlist.get(9);
				String cpuvalue = (String) _eventlist.get(10);
				PdfPCell cell13 = new PdfPCell(new Phrase(i + 1 + ""));
				PdfPCell cell14 = new PdfPCell(new Phrase(ip));
				PdfPCell cell15 = new PdfPCell(new Phrase(equname, contextFont));
				PdfPCell cell16 = new PdfPCell(new Phrase(osname, contextFont));
				PdfPCell cell17 = new PdfPCell(new Phrase(sum));
				PdfPCell cell18 = new PdfPCell(new Phrase(levelone));
				PdfPCell cell19 = new PdfPCell(new Phrase(levelone));
				PdfPCell cell20 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell21 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell22 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell23 = new PdfPCell(new Phrase(levelthree));
				PdfPCell cell24 = new PdfPCell(new Phrase(levelthree));

				aTable.addCell(cell13);
				aTable.addCell(cell14);
				aTable.addCell(cell15);
				aTable.addCell(cell16);
				aTable.addCell(cell17);
				aTable.addCell(cell18);
				aTable.addCell(cell19);
				aTable.addCell(cell20);
				aTable.addCell(cell21);
				aTable.addCell(cell22);
				aTable.addCell(cell23);
				aTable.addCell(cell24);
			}
		}
		document.add(aTable);
		document.add(new Paragraph("\n"));
		document.close();
	}

	public String createdocEventPDF() {
		String file = "/temp/shijianbaobiao.pdf";// 保存到项目文件夹下的指定文件夹
		String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
		try {
			createDocContextEventPDF(fileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.setAttribute("filename", fileName);
		return "/capreport/net/download.jsp";
	}

	public String choceDoc() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		HostNodeDao dao = new HostNodeDao();
		request.setAttribute("list", dao.loadNetwork(1));
		return "/capreport/net/choce.jsp";
	}

	// zhushouzhi------------------------------------end pdf

	// zhushouzhi-----------------------------start
	private String downloadselfnetchocereport() {
		String oids = getParaValue("ids");
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}

		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Hashtable sharedata = ShareData.getSharedata();
		User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		UserView view = new UserView();

		String positionname = view.getPosition(vo.getPosition());
		String username = vo.getName();
		// String position = vo.getPosition();
		Vector vector = new Vector();
		// Hashtable reporthash = new Hashtable();
		try {
			Hashtable allreporthash = new Hashtable();

			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					if (node == null)
						continue;
					EventListDao eventdao = new EventListDao();
					// 得到事件列表
					StringBuffer s = new StringBuffer();
					s
							.append("select * from system_eventlist where recordtime>= '"
									+ starttime
									+ "' "
									+ "and recordtime<='"
									+ totime + "' ");
					s.append(" and nodeid=" + node.getId());

					List infolist = eventdao.findByCriteria(s.toString());
					int levelone = 0;
					int levletwo = 0;
					int levelthree = 0;
					if (infolist != null && infolist.size() > 0) {
						for (int j = 0; j < infolist.size(); j++) {
							EventList eventlist = (EventList) infolist.get(j);
							if (eventlist.getContent() == null)
								eventlist.setContent("");
							String content = eventlist.getContent();
							String subentity = eventlist.getSubentity();

							if (eventlist.getContent() == null)
								eventlist.setContent("");

							if (eventlist.getLevel1() != 1) {
								levelone = levelone + 1;
							} else if (eventlist.getLevel1() == 2) {
								levletwo = levletwo + 1;
							} else if (eventlist.getLevel1() == 3) {
								levelthree = levelthree + 1;
							}
						}
					}
					reporthash.put("levelone", levelone + "");
					reporthash.put("levletwo", levletwo + "");
					reporthash.put("levelthree", levelthree + "");
					// ------------------------事件end
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// 按排序标志取各端口最新记录的列表
					String orderflag = "index";

					String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
							"ifOperStatus", "OutBandwidthUtilHdx",
							"InBandwidthUtilHdx" };

					vector = hostlastmanager.getInterface_share(ip,
							netInterfaceItem, orderflag, startdate, todate);
					PortconfigDao portdao = new PortconfigDao();
					Hashtable portconfigHash = null;
					try {
						portconfigHash = portdao.getIpsHash(ip);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						portdao.close();
					}
					// Hashtable portconfigHash =
					// portconfigManager.getIpsHash(ip);
					reporthash.put("portconfigHash", portconfigHash);

					List reportports = null;
					portdao = new PortconfigDao();
                    try {
                        reportports = portdao.getByIpAndReportflag(ip,
                        		new Integer(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portdao.close();
                    }
					reporthash.put("reportports", reportports);
					if (reportports != null && reportports.size() > 0) {
						// 显示端口的流速图形
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
						String unit = "kb/s";
						String title = startdate + "至" + todate + "端口流速";
						String[] banden3 = { "InBandwidthUtilHdx",
								"OutBandwidthUtilHdx" };
						String[] bandch3 = { "入口流速", "出口流速" };

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports
									.get(k);
							// 按分钟显示报表
							Hashtable value = new Hashtable();
							value = daymanager.getmultiHisHdx(ip, "ifspeed",
									portconfig.getPortindex() + "", banden3,
									bandch3, startdate, todate, "UtilHdx");
							String reportname = "第" + portconfig.getPortindex()
									+ "(" + portconfig.getName() + ")端口流速"
									+ startdate + "至" + todate + "报表(按分钟显示)";
							p_drawchartMultiLineMonth(value, reportname,
									newip + portconfig.getPortindex()
											+ "ifspeed_day", 800, 200,
									"UtilHdx");
							// String url1 =
							// "/resource/image/jfreechart/"+newip+portconfig.getPortindex()+"ifspeed_day.png";
						}
					}

					reporthash.put("netifVector", vector);

					Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
							"Utilization", starttime, totime);
					String pingconavg = "";

					maxhash = new Hashtable();
					String cpumax = "";
					String avgcpu = "";
					if (cpuhash.get("max") != null) {
						cpumax = (String) cpuhash.get("max");
					}
					if (cpuhash.get("avgcpucon") != null) {
						avgcpu = (String) cpuhash.get("avgcpucon");
					}

					maxhash.put("cpumax", cpumax);
					maxhash.put("avgcpu", avgcpu);
					// 画图
					p_draw_line(cpuhash, "", newip + "cpu", 740, 120);
					Hashtable ConnectUtilizationhash = hostmanager
							.getCategory(ip, "Ping", "ConnectUtilization",
									starttime, totime);
					if (ConnectUtilizationhash.get("avgpingcon") != null)
						pingconavg = (String) ConnectUtilizationhash
								.get("avgpingcon");
					String ConnectUtilizationmax = "";
					maxping.put("avgpingcon", pingconavg);
					if (ConnectUtilizationhash.get("max") != null) {
						ConnectUtilizationmax = (String) ConnectUtilizationhash
								.get("max");
					}
					maxping.put("pingmax", ConnectUtilizationmax);

					p_draw_line(ConnectUtilizationhash, "", newip
							+ "ConnectUtilization", 740, 120);

					// 从内存中获得当前的跟此IP相关的IP-MAC的FDB表信息
					Hashtable _IpRouterHash = ShareData.getIprouterdata();
					vector = (Vector) _IpRouterHash.get(ip);
					if (vector != null)
						reporthash.put("iprouterVector", vector);

					Vector pdata = (Vector) pingdata.get(ip);
					// 把ping得到的数据加进去
					if (pdata != null && pdata.size() > 0) {
						for (int m = 0; m < pdata.size(); m++) {
							Pingcollectdata hostdata = (Pingcollectdata) pdata
									.get(m);
							if (hostdata.getSubentity().equals(
									"ConnectUtilization")) {
								reporthash.put("time", hostdata
										.getCollecttime());
								reporthash.put("Ping", hostdata.getThevalue());
								reporthash.put("ping", maxping);
							}
						}
					}

					// CPU
					Hashtable hdata = (Hashtable) sharedata.get(ip);
					if (hdata == null)
						hdata = new Hashtable();
					Vector cpuVector = new Vector();
					if (hdata.get("cpu") != null)
						cpuVector = (Vector) hdata.get("cpu");
					if (cpuVector != null && cpuVector.size() > 0) {
						// for(int si=0;si<cpuVector.size();si++){
						CPUcollectdata cpudata = (CPUcollectdata) cpuVector
								.elementAt(0);
						maxhash.put("cpu", cpudata.getThevalue());
						reporthash.put("CPU", maxhash);
						// }
					} else {
						reporthash.put("CPU", maxhash);
					}// -----流速
					Hashtable streaminHash = hostmanager.getAllutilhdx(ip,
							"AllInBandwidthUtilHdx", starttime, totime, "avg");
					Hashtable streamoutHash = hostmanager.getAllutilhdx(ip,
							"AllOutBandwidthUtilHdx", starttime, totime, "avg");
					String avgput = "";
					if (streaminHash.get("avgput") != null) {
						avgput = (String) streaminHash.get("avgput");
						reporthash.put("avginput", avgput);
					}
					if (streamoutHash.get("avgput") != null) {
						avgput = (String) streamoutHash.get("avgput");
						reporthash.put("avgoutput", avgput);
					}
					Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip,
							"AllInBandwidthUtilHdx", starttime, totime, "max");
					Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip,
							"AllOutBandwidthUtilHdx", starttime, totime, "max");
					String maxput = "";
					if (streammaxinHash.get("max") != null) {
						maxput = (String) streammaxinHash.get("max");
						reporthash.put("maxinput", maxput);
					}
					if (streammaxoutHash.get("max") != null) {
						maxput = (String) streammaxoutHash.get("max");
						reporthash.put("maxoutput", maxput);
					}
					// ------流速end

					reporthash.put("starttime", starttime);
					reporthash.put("totime", totime);

					reporthash.put("equipname", equipname);
					reporthash.put("memmaxhash", memmaxhash);
					reporthash.put("memavghash", memavghash);

					allreporthash.put(ip, reporthash);
				}
			}
			String file = "temp/networknms_report.doc";// 保存到项目文件夹下的指定文件夹
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
			ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
					allreporthash);

			report.createReport_networkchoce(starttime, totime, fileName,
					username, positionname);
			request.setAttribute("filename", fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	// zhushouzhi-------------------------------end

	// ===============================生成oa需要报表数据

	/**
	 * 
	 * 调用此方法来解决山西移动oa 统计网络设备接口流速报表 方法已经写好就差生成报表的表格方法
	 * 
	 */
	public String downloadsxoaIfreport() {
		String oids = getParaValue("ids");// 获取要检查的id
		if (oids == null)
			oids = "";
		Integer[] ids = null;
		if (oids.split(",").length > 0) {
			String[] _ids = oids.split(",");
			if (_ids != null && _ids.length > 0)
				ids = new Integer[_ids.length];
			for (int i = 0; i < _ids.length; i++) {
				ids[i] = new Integer(_ids[i]);
			}
		}
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";

		Hashtable allreporthash = new Hashtable();
		Vector vector = new Vector();

		try {

			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					Hashtable reporthash = new Hashtable();
					HostNodeDao dao = new HostNodeDao();
					HostNode node = (HostNode) dao.loadHost(ids[i]);
					dao.close();
					// Monitoriplist monitor = monitorManager.getById(ids[i]);
					// Equipment equipment = (Equipment)hostMonitor.get(i);
					ip = node.getIpAddress();
					equipname = node.getAlias();
					String remoteip = request.getRemoteAddr();
					String newip = doip(ip);
					// 按排序标志取各端口最新记录的列表
					String orderflag = "index";

					// 获取端口列表
					PortconfigDao portdao = new PortconfigDao();
					// Hashtable portconfigHash = portdao.getIpsHash(ip);

					// 需要做报表的列表
					List reportports = null;
                    try {
                        reportports = portdao.getByIpAndReportflag(ip,
                        		new Integer(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        portdao.close();
                    }

					if (reportports != null && reportports.size() > 0) {
						// 显示端口的流速图形
						I_HostCollectDataDay daymanager = new HostCollectDataDayManager();

						for (int k = 0; k < reportports.size(); k++) {
							com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports
									.get(k);

							Hashtable value = new Hashtable();
							value = ((HostCollectDataDayManager) daymanager)
									.getmultiHisHdx_OA(ip, portconfig
											.getPortindex()
											+ "", startdate, todate, "UtilHdx",
											portconfig.getName(), portconfig
													.getLinkuse());
							vector.add(value);
						}
					}

				}
			}

			// reporthash.put("netifVector",vector);

			allreporthash.put("sxoanetifreport", vector);
			// 报表接口需要改一下

			// -----------------需要协助修改---------------------
			AbstractionReport1 report = new ExcelReport1(
					new IpResourceReport(), allreporthash);
			// -----------------------------------------------

			report.createReport_oawork("/temp/networknms_report.xls",
					startdate, todate);
			// report.createReport_networkall("/temp/networknms_report.xls");
			request.setAttribute("filename", report.getFileName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/capreport/net/download.jsp";
		// return mapping.findForward("report_info");
	}

	private String showNetworkPingReport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		String newip = doip(ipaddress);
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("newip", newip);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// 按排序标志取各端口最新记录的列表
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null
				&& !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		List selectedIPNodeList = dao.findBynode("ip_address", ipaddress);
		if (selectedIPNodeList != null && selectedIPNodeList.size() > 0) {
			HostNode node = (HostNode) selectedIPNodeList.get(0);
			Hashtable pinghash = new Hashtable();
			try {
				pinghash = hostmanager.getCategory(node.getIpAddress(), "Ping",
						"ConnectUtilization", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable responsehash = new Hashtable();
			try {
				responsehash = hostmanager.getCategory(node.getIpAddress(),
						"Ping", "ResponseTime", starttime, totime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable ipmemhash = new Hashtable();
			ipmemhash.put("hostnode", node);
			ipmemhash.put("pinghash", pinghash);
			ipmemhash.put("responsehash", responsehash);
			orderList.add(ipmemhash);

		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("avgping")
				|| orderflag.equalsIgnoreCase("downnum")
				|| orderflag.equalsIgnoreCase("responseavg")
				|| orderflag.equalsIgnoreCase("responsemax")) {
			returnList = (List) session.getAttribute("pinglist");
		} else {
			// 对orderList根据theValue进行排序

			// **********************************************************
			List pinglist = orderList;
			if (pinglist != null && pinglist.size() > 0) {
				for (int i = 0; i < pinglist.size(); i++) {
					Hashtable _pinghash = (Hashtable) pinglist.get(i);
					HostNode node = (HostNode) _pinghash.get("hostnode");
					Hashtable pinghash = (Hashtable) _pinghash.get("pinghash");
					Hashtable responsehash = (Hashtable) _pinghash
							.get("responsehash");
					if (pinghash == null)
						continue;
					String equname = node.getAlias();
					String ip = node.getIpAddress();

					String pingconavg = "";
					String downnum = "";
					String responseavg = "";
					String responsemax = "";
					if (pinghash.get("avgpingcon") != null)
						pingconavg = (String) pinghash.get("avgpingcon");
					if (pinghash.get("downnum") != null)
						downnum = (String) pinghash.get("downnum");
					// 获取响应时间
					if (responsehash.get("avgpingcon") != null)
						responseavg = (String) responsehash.get("avgpingcon");
					if (responsehash.get("pingmax") != null)
						responsemax = (String) responsehash.get("pingmax");

					List ipdiskList = new ArrayList();
					ipdiskList.add(ip);
					ipdiskList.add(equname);
					ipdiskList.add(node.getType());
					ipdiskList.add(pingconavg);
					ipdiskList.add(downnum);
					ipdiskList.add(responseavg);
					ipdiskList.add(responsemax);
					returnList.add(ipdiskList);
				}
			}
		}
		// **********************************************************

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("avgping")) {
						String avgping = "";
						if (ipdiskList.get(3) != null) {
							avgping = (String) ipdiskList.get(3);
						}
						String _avgping = "";
						if (ipdiskList.get(3) != null) {
							_avgping = (String) _ipdiskList.get(3);
						}
						if (new Double(avgping.substring(0,
								avgping.length() - 2)).doubleValue() < new Double(
								_avgping.substring(0, _avgping.length() - 2))
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("downnum")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responseavg")) {
						String avgping = "";
						if (ipdiskList.get(5) != null) {
							avgping = (String) ipdiskList.get(5);
						}
						String _avgping = "";
						if (ipdiskList.get(5) != null) {
							_avgping = (String) _ipdiskList.get(5);
						}
						if (new Double(avgping.substring(0,
								avgping.length() - 2)).doubleValue() < new Double(
								_avgping.substring(0, _avgping.length() - 2))
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("responsemax")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// 得到排序后的Subentity的列表
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}
		String pingconavg = "";
		Hashtable ConnectUtilizationhash = new Hashtable();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(ipaddress, "Ping",
					"ConnectUtilization", starttime, totime);
		} catch (Exception e) {

		}
		if (ConnectUtilizationhash.get("avgpingcon") != null)
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		String ConnectUtilizationmax = "";
		// maxping.put("avgpingcon", pingconavg);
		if (ConnectUtilizationhash.get("max") != null) {
			ConnectUtilizationmax = (String) ConnectUtilizationhash.get("max");
		}
		// maxping.put("pingmax", ConnectUtilizationmax);

		// Hashtable ConnectUtilizationhash = new Hashtable();
		// try{
		// ConnectUtilizationhash =
		// hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",getStartTime(),getToTime());
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }

		Hashtable ResponseTimehash = new Hashtable();
		try {
			ResponseTimehash = hostmanager.getCategory(ipaddress, "Ping",
					"ResponseTime", starttime, totime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String[] bandch = { "连通率", "响应时间" };
		String[] bandch1 = { "连通率(%)", "响应时间(ms)" };
		String returnStr = area_chooseDrawLineType("minute",
				ConnectUtilizationhash, ResponseTimehash, bandch, bandch1, 350,
				200, 110, 20);
		SysLogger.info("returnStr######## " + returnStr + " ############");
		JFreeChartBrother jfb = (JFreeChartBrother) ResourceCenter
				.getInstance().getChartStorage().get(returnStr);
		String imgpath = ResourceCenter.getInstance().getSysPath()
				+ "/resource/image/jfreechart/";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(imgpath + returnStr + ".png");// 将统计图标输出成JPG文件
			ChartUtilities.writeChartAsJPEG(fos, // 输出到哪个输出流
					1,// JPEG图片的质量，0~1之间
					jfb.getChart(), // 统计图标对象
					740, // 宽
					200,// 宽
					null// ChartRenderingInfo 信息
					);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		p_draw_line(ConnectUtilizationhash, "", newip + "ConnectUtilization",
				740, 120);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		session.setAttribute("pinglist", list);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("pinglist", list);
		request.setAttribute("imgpath", returnStr + ".png");
		return "/capreport/net/showNetworkPingReport.jsp";
	}

	private String showNetworkEventReport() {
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";

		String[] ids = getParaArrayValue("checkbox");
		Hashtable allcpuhash = new Hashtable();

		// 按排序标志取各端口最新记录的列表
		String orderflag = "ipaddress";
		if (getParaValue("orderflag") != null
				&& !getParaValue("orderflag").equals("")) {
			orderflag = getParaValue("orderflag");
		}

		List orderList = new ArrayList();
		HostNodeDao dao = new HostNodeDao();
		List selectedIPNodeList = dao.findBynode("ip_address", ipaddress);
		if (selectedIPNodeList != null && selectedIPNodeList.size() > 0) {
			HostNode node = (HostNode) selectedIPNodeList.get(0);
			EventListDao eventdao = new EventListDao();
			// 得到事件列表
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '"
					+ starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = eventdao.findByCriteria(s.toString());

			// List infolist =
			// eventqueryManager.getQuery(starttime,totime,"99","99",99,node.getId());
			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";
				int levelone = 0;
				int levletwo = 0;
				int levelthree = 0;
				int pingvalue = 0;
				int cpuvalue = 0;
				int updownvalue = 0;
				int utilvalue = 0;

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (eventlist.getLevel1() == 1) {
						levelone = levelone + 1;
					} else if (eventlist.getLevel1() == 2) {
						levletwo = levletwo + 1;
					} else if (eventlist.getLevel1() == 3) {
						levelthree = levelthree + 1;
					}
					// if(content.indexOf("连通率")>=0){
					// pingvalue = pingvalue + 1;
					// }else if(content.indexOf("CPU利用率")>=0){
					// cpuvalue = cpuvalue + 1;
					// }else if(content.indexOf("up")>=0 ||
					// content.indexOf("down")>=0){
					// updownvalue = updownvalue + 1;
					// }else if(content.indexOf("流速")>=0){
					// utilvalue = utilvalue + 1;
					// }

					if (("ping").equals(subentity)) {// 联通率
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") >= 0) {// 端口
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") < 0) {// 流速
						utilvalue = utilvalue + 1;
					}
				}
				String equname = node.getAlias();
				String ip = node.getIpAddress();
				List ipeventList = new ArrayList();
				ipeventList.add(ip);
				ipeventList.add(equname);
				ipeventList.add(node.getType());
				ipeventList.add((levelone + levletwo + levelthree) + "");
				ipeventList.add(levelone + "");
				ipeventList.add(levletwo + "");
				ipeventList.add(levelthree + "");
				ipeventList.add(pingvalue + "");
				ipeventList.add(cpuvalue + "");
				ipeventList.add(updownvalue + "");
				ipeventList.add(utilvalue + "");
				orderList.add(ipeventList);

			}
		}
		List returnList = new ArrayList();
		if (orderflag.equalsIgnoreCase("one")
				|| orderflag.equalsIgnoreCase("two")
				|| orderflag.equalsIgnoreCase("three")
				|| orderflag.equalsIgnoreCase("ping")
				|| orderflag.equalsIgnoreCase("cpu")
				|| orderflag.equalsIgnoreCase("updown")
				|| orderflag.equalsIgnoreCase("util")
				|| orderflag.equalsIgnoreCase("sum")) {
			returnList = (List) session.getAttribute("eventlist");
		} else {
			returnList = orderList;
		}

		List list = new ArrayList();
		if (returnList != null && returnList.size() > 0) {
			for (int m = 0; m < returnList.size(); m++) {
				List ipdiskList = (List) returnList.get(m);
				for (int n = m + 1; n < returnList.size(); n++) {
					List _ipdiskList = (List) returnList.get(n);
					if (orderflag.equalsIgnoreCase("ipaddress")) {
					} else if (orderflag.equalsIgnoreCase("sum")) {
						String sum = "";
						if (ipdiskList.get(3) != null) {
							sum = (String) ipdiskList.get(3);
						}
						String _sum = "";
						if (ipdiskList.get(3) != null) {
							_sum = (String) _ipdiskList.get(3);
						}
						if (new Double(sum).doubleValue() < new Double(_sum)
								.doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("one")) {
						String downnum = "";
						if (ipdiskList.get(4) != null) {
							downnum = (String) ipdiskList.get(4);
						}
						String _downnum = "";
						if (ipdiskList.get(4) != null) {
							_downnum = (String) _ipdiskList.get(4);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("two")) {
						String downnum = "";
						if (ipdiskList.get(5) != null) {
							downnum = (String) ipdiskList.get(5);
						}
						String _downnum = "";
						if (ipdiskList.get(5) != null) {
							_downnum = (String) _ipdiskList.get(5);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("three")) {
						String downnum = "";
						if (ipdiskList.get(6) != null) {
							downnum = (String) ipdiskList.get(6);
						}
						String _downnum = "";
						if (ipdiskList.get(6) != null) {
							_downnum = (String) _ipdiskList.get(6);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("ping")) {
						String downnum = "";
						if (ipdiskList.get(7) != null) {
							downnum = (String) ipdiskList.get(7);
						}
						String _downnum = "";
						if (ipdiskList.get(7) != null) {
							_downnum = (String) _ipdiskList.get(7);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("cpu")) {
						String downnum = "";
						if (ipdiskList.get(8) != null) {
							downnum = (String) ipdiskList.get(8);
						}
						String _downnum = "";
						if (ipdiskList.get(8) != null) {
							_downnum = (String) _ipdiskList.get(8);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("updown")) {
						String downnum = "";
						if (ipdiskList.get(9) != null) {
							downnum = (String) ipdiskList.get(9);
						}
						String _downnum = "";
						if (ipdiskList.get(9) != null) {
							_downnum = (String) _ipdiskList.get(9);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					} else if (orderflag.equalsIgnoreCase("util")) {
						String downnum = "";
						if (ipdiskList.get(10) != null) {
							downnum = (String) ipdiskList.get(10);
						}
						String _downnum = "";
						if (ipdiskList.get(10) != null) {
							_downnum = (String) _ipdiskList.get(10);
						}
						if (new Double(downnum).doubleValue() < new Double(
								_downnum).doubleValue()) {
							returnList.remove(m);
							returnList.add(m, _ipdiskList);
							returnList.remove(n);
							returnList.add(n, ipdiskList);
							ipdiskList = _ipdiskList;
							_ipdiskList = null;
						}
					}
				}
				// 得到排序后的Subentity的列表
				list.add(ipdiskList);
				ipdiskList = null;
			}
		}

		// setListProperty(capReportForm, request, list);
		request.setAttribute("starttime", starttime);
		request.setAttribute("totime", totime);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		request.setAttribute("eventlist", list);
		session.setAttribute("eventlist", list);
		return "/capreport/net/showNetworkEventReport.jsp";
	}

	private String showNetworkCompositeReport() {
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		// dddddddddddddddddd
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String equipname = "";
		// zhushouzhi--------------------------start
		String type = "";
		String typename = "";
		String equipnameNetDoc = "";
		// zhushouzhi-----------------------------end
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max

		Vector vector = new Vector();
		Vector iprouterVector = new Vector();
		List routerList = null;
		Hashtable reporthash = new Hashtable();
		int pingvalue = 0;
		int cpuvalue = 0;
		int updownvalue = 0;
		int utilvalue = 0;
		String runmodel = PollingEngine.getCollectwebflag();

		try {
			ip = getParaValue("ipaddress");
			// System.out.println("##########################ipaddress="+ip);
			type = getParaValue("type");
			HostNodeDao dao = new HostNodeDao();
			HostNode node = null;
			try {
				node = (HostNode) dao.findByCondition("ip_address", ip).get(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			// ip=node.getIpAddress();
			equipname = node.getAlias() + "(" + ip + ")";

			// zhushouzhi---------------------start
			equipnameNetDoc = node.getAlias();
			// zhushouzhi-----------------------end
			String remoteip = request.getRemoteAddr();
			String newip = doip(ip);
			// System.out.println("#####################newip"+newip);
			request.setAttribute("newip", newip);
			// 按排序标志取各端口最新记录的列表
			String orderflag = "index";

			String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
					"ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };
			if ("0".equals(runmodel)) {
				// 采集与访问是集成模式
				Hashtable pingdata = ShareData.getPingdata();
				Hashtable sharedata = ShareData.getSharedata();
				vector = hostlastmanager.getInterface_share(ip,
						netInterfaceItem, orderflag, startdate, todate);
				Vector pdata = (Vector) pingdata.get(ip);
				// 把ping得到的数据加进去
				if (pdata != null && pdata.size() > 0) {
					for (int m = 0; m < pdata.size(); m++) {
						Pingcollectdata hostdata = (Pingcollectdata) pdata
								.get(m);
						if (hostdata.getSubentity()
								.equals("ConnectUtilization")) {
							reporthash.put("time", hostdata.getCollecttime());
							reporthash.put("Ping", hostdata.getThevalue());
							reporthash.put("ping", maxping);
						}
					}
				}
				// 从内存中获得当前的跟此IP相关的IP-MAC的FDB表信息
				Hashtable _IpRouterHash = ShareData.getIprouterdata();
				iprouterVector = (Vector) _IpRouterHash.get(ip);

				Hashtable hdata = (Hashtable) sharedata.get(ip);
				if (hdata == null)
					hdata = new Hashtable();
				Vector cpuVector = new Vector();
				if (hdata.get("cpu") != null)
					cpuVector = (Vector) hdata.get("cpu");
				if (cpuVector != null && cpuVector.size() > 0) {
					// for(int si=0;si<cpuVector.size();si++){
					CPUcollectdata cpudata = (CPUcollectdata) cpuVector
							.elementAt(0);
					maxhash.put("cpu", cpudata.getThevalue());
					reporthash.put("CPU", maxhash);
					// }
				} else {
					reporthash.put("CPU", maxhash);
				}
			} else {
				// 采集与访问是分离模式
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);

				// 获取当前内存值
				MemoryInfoService memoryInfoService = new MemoryInfoService(
						String.valueOf(nodeDTO.getId()), nodeDTO.getType(),
						nodeDTO.getSubtype());
				List memoryInfoList = memoryInfoService
						.getCurrPerMemoryListInfo();
				// 端口流速
				try {
					vector = hostlastmanager.getInterface(ip, netInterfaceItem,
							orderflag, starttime, totime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// CPU
				CpuInfoService cpuInfoService = new CpuInfoService(String
						.valueOf(nodeDTO.getId()), nodeDTO.getType(), nodeDTO
						.getSubtype());
				String currCpuAvgInfo = cpuInfoService.getCurrCpuAvgInfo();
				maxhash.put("cpu", currCpuAvgInfo);
				// iprout
				NetService netService = new NetService(String.valueOf(nodeDTO
						.getId()), nodeDTO.getType(), nodeDTO.getSubtype());
				routerList = netService.getRouterInfo();
				// iprouterVector =
				// netService.getIpRouterVectorByList(routerList);
				// ******当前连通率start
				String pingnow = "0.0";
				String pingconavg = "0";
				Vector pingData = new PingInfoService(String.valueOf(nodeDTO
						.getId()), nodeDTO.getType(), nodeDTO.getSubtype())
						.getPingInfo();
				;
				if (pingData != null && pingData.size() > 0) {
					Pingcollectdata pingdata = (Pingcollectdata) pingData
							.get(0);
					Calendar tempCal = (Calendar) pingdata.getCollecttime();
					Date cc = tempCal.getTime();
					pingnow = pingdata.getThevalue();// 当前连通率
				}
				Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip,
						"Ping", "ConnectUtilization", starttime, totime);
				if (ConnectUtilizationhash.get("avgpingcon") != null) {
					pingconavg = (String) ConnectUtilizationhash
							.get("avgpingcon");
				}
				String ConnectUtilizationmax = "";
				maxping.put("avgpingcon", pingconavg);
				if (ConnectUtilizationhash.get("max") != null) {
					ConnectUtilizationmax = (String) ConnectUtilizationhash
							.get("max");
				}
				maxping.put("pingmax", ConnectUtilizationmax);
				reporthash.put("ping", maxping);
				reporthash.put("Ping", pingnow);
				// ******当前连通率end
			}
			PortconfigDao portdao = new PortconfigDao();
			Hashtable portconfigHash = portdao.getIpsHash(ip);
			List reportports = portdao.getByIpAndReportflag(ip, new Integer(1));
			if (reportports != null && reportports.size() > 0) {
				// SysLogger.info("reportports size
				// ##############"+reportports.size());
				// 显示端口的流速图形
				I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
				String unit = "kb/s";
				String title = starttime + "至" + totime + "端口流速";
				String[] banden3 = { "InBandwidthUtilHdx",
						"OutBandwidthUtilHdx" };
				String[] bandch3 = { "入口流速", "出口流速" };

				for (int i = 0; i < reportports.size(); i++) {
					// SysLogger.info(reportports.get(i).getClass()+
					// "===============");
					com.afunms.config.model.Portconfig portconfig = null;
					try {
						portconfig = (com.afunms.config.model.Portconfig) reportports
								.get(i);
						// 按分钟显示报表
						Hashtable value = new Hashtable();
						value = daymanager.getmultiHisHdx(ip, "ifspeed",
								portconfig.getPortindex() + "", banden3,
								bandch3, startdate, todate, "UtilHdx");
						String reportname = "第" + portconfig.getPortindex()
								+ "(" + portconfig.getName() + ")端口流速"
								+ startdate + "至" + todate + "报表(按分钟显示)";
						p_drawchartMultiLineMonth(value, reportname, newip
								+ portconfig.getPortindex() + "ifspeed_day",
								800, 200, "UtilHdx");
						String url1 = "../images/jfreechart/" + newip
								+ portconfig.getPortindex() + "ifspeed_day.png";
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			reporthash.put("routerList", routerList);
			reporthash.put("portconfigHash", portconfigHash);
			reporthash.put("reportports", reportports);
			reporthash.put("netifVector", vector);
			// zhushouzhi--------------------start
			reporthash.put("startdate", startdate);
			reporthash.put("todate", todate);
			reporthash.put("totime", totime);
			reporthash.put("starttime", starttime);
			reporthash.put("time", Calendar.getInstance());
			// zhushouzhi-------------------------end
			Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
					"Utilization", starttime, totime);
			Hashtable streaminHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "avg");
			Hashtable streamoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "avg");
			String pingconavg = "";
			String avgput = "";
			// String avgoutput = "";
			// maxhash = new Hashtable();
			String cpumax = "";
			String avgcpu = "";
			String maxput = "";
			if (cpuhash.get("max") != null) {
				cpumax = (String) cpuhash.get("max");
			}
			if (cpuhash.get("avgcpucon") != null) {
				avgcpu = (String) cpuhash.get("avgcpucon");
			}
			// zhushouzhi-----------------------start
			if (streaminHash.get("avgput") != null) {
				avgput = (String) streaminHash.get("avgput");
				reporthash.put("avginput", avgput);
			}
			if (streamoutHash.get("avgput") != null) {
				avgput = (String) streamoutHash.get("avgput");
				reporthash.put("avgoutput", avgput);
			}
			Hashtable streammaxinHash = hostmanager.getAllutilhdx(ip,
					"AllInBandwidthUtilHdx", starttime, totime, "max");
			Hashtable streammaxoutHash = hostmanager.getAllutilhdx(ip,
					"AllOutBandwidthUtilHdx", starttime, totime, "max");
			if (streammaxinHash.get("max") != null) {
				maxput = (String) streammaxinHash.get("max");
				reporthash.put("maxinput", maxput);
			}
			if (streammaxoutHash.get("max") != null) {
				maxput = (String) streammaxoutHash.get("max");
				reporthash.put("maxoutput", maxput);
			}
			// zhushouzhi--------------------------------------end
			maxhash.put("cpumax", cpumax);
			maxhash.put("avgcpu", avgcpu);

			if (iprouterVector != null)
				reporthash.put("iprouterVector", iprouterVector);
			// zhushouzhi--------------------start
			EventListDao eventdao = new EventListDao();
			// 得到事件列表
			StringBuffer s = new StringBuffer();
			s.append("select * from system_eventlist where recordtime>= '"
					+ starttime + "' " + "and recordtime<='" + totime + "' ");
			s.append(" and nodeid=" + node.getId());

			List infolist = eventdao.findByCriteria(s.toString());

			if (infolist != null && infolist.size() > 0) {
				// mainreport = mainreport+ " \r\n";

				for (int j = 0; j < infolist.size(); j++) {
					EventList eventlist = (EventList) infolist.get(j);
					if (eventlist.getContent() == null)
						eventlist.setContent("");
					String content = eventlist.getContent();
					String subentity = eventlist.getSubentity();
					if (("ping").equals(subentity)) {// 联通率
						pingvalue = pingvalue + 1;
					} else if (("cpu").equals(subentity)) {// cpu
						cpuvalue = cpuvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") >= 0) {// 端口
						updownvalue = updownvalue + 1;
					} else if (("interface").equals(subentity)
							&& content.indexOf("端口") < 0) {// 流速
						utilvalue = utilvalue + 1;
					}
				}
			}
			reporthash.put("CPU", maxhash);
			reporthash.put("pingvalue", pingvalue);
			reporthash.put("cpuvalue", cpuvalue);
			reporthash.put("updownvalue", updownvalue);
			reporthash.put("utilvalue", utilvalue);
			// zhushouzhi-------------------------end

			// Vector pdata = (Vector) pingdata.get(ip);
			// // 把ping得到的数据加进去
			// if (pdata != null && pdata.size() > 0) {
			// for (int m = 0; m < pdata.size(); m++) {
			// Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
			// if (hostdata.getSubentity().equals("ConnectUtilization")) {
			// reporthash.put("time", hostdata.getCollecttime());
			// reporthash.put("Ping", hostdata.getThevalue());
			// reporthash.put("ping", maxping);
			// }
			// }
			// }

			// CPU
			// Hashtable hdata = (Hashtable) sharedata.get(ip);
			// if (hdata == null)
			// hdata = new Hashtable();
			// Vector cpuVector = new Vector();
			// if (hdata.get("cpu") != null)
			// cpuVector = (Vector) hdata.get("cpu");
			// if (cpuVector != null && cpuVector.size() > 0) {
			// // for(int si=0;si<cpuVector.size();si++){
			// CPUcollectdata cpudata = (CPUcollectdata) cpuVector.elementAt(0);
			// maxhash.put("cpu", cpudata.getThevalue());
			// reporthash.put("CPU", maxhash);
			// // }
			// } else {
			// reporthash.put("CPU", maxhash);
			// }
			// 流速

			reporthash.put("Memory", memhash);
			reporthash.put("Disk", diskhash);
			reporthash.put("equipname", equipname);
			reporthash.put("equipnameNetDoc", equipnameNetDoc);
			reporthash.put("ip", ip);
			if ("network".equals(type)) {
				typename = "网络设备";

			}
			reporthash.put("typename", typename);

			AbstractionReport1 report = new ExcelReport1(
					new IpResourceReport(), reporthash);
			// 画图

			p_draw_line(cpuhash, "", newip + "cpu", 740, 120);

			Hashtable ConnectUtilizationhash = hostmanager.getCategory(ip,
					"Ping", "ConnectUtilization", starttime, totime);
			if (ConnectUtilizationhash.get("avgpingcon") != null)
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (ConnectUtilizationhash.get("max") != null) {
				ConnectUtilizationmax = (String) ConnectUtilizationhash
						.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);

			p_draw_line(ConnectUtilizationhash, "", newip
					+ "ConnectUtilization", 740, 120);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.setAttribute("reporthash", reporthash);
		return "/capreport/net/showNetworkCompositeReport.jsp";
	}

	private String showNetworkConfigReport() {
		String startdate = this.getParaValue("startdate");
		String todate = this.getParaValue("todate");
		String ipaddress = getParaValue("ipaddress");
		request.setAttribute("ipaddress", ipaddress);
		request.setAttribute("startdate", startdate);
		request.setAttribute("todate", todate);
		return "/capreport/net/showNetworkConfigReport.jsp";
	}

	private String downloadNetworkPingReport() {
		String str = request.getParameter("str");
		if (str.equals("1")) {
			String file = "/temp/liantonglvbaobiao.doc";// 保存到项目文件夹下的指定文件夹
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
			try {
				createDocContext(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			Hashtable allcpuhash = new Hashtable();

			List returnList = new ArrayList();
			// I_MonitorIpList monitorManager=new MonitoriplistManager();
			List memlist = (List) session.getAttribute("pinglist");
			Hashtable reporthash = new Hashtable();
			reporthash.put("pinglist", memlist);
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);

			ExcelReport1 report = new ExcelReport1(new IpResourceReport(),
					reporthash);
			report.setRequest(request);
			report.createReport_hostping("/temp/hostping_report.xls");
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/liantonglvbaobiao.PDF";// 保存到项目文件夹下的指定文件夹
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
			try {
				createPdfContextPing(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}

		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkEventReport() {
		String str = request.getParameter("str");
		if (str.equals("1")) {
			String file = "/temp/shijianbaobiao.doc";// 保存到项目文件夹下的指定文件夹
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
			try {
				createDocContextEvent(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		if (str.equals("0")) {
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if (startdate == null) {
				startdate = sdf0.format(d);
			}
			String todate = getParaValue("todate");
			if (todate == null) {
				todate = sdf0.format(d);
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			Hashtable allcpuhash = new Hashtable();

			List returnList = new ArrayList();
			// I_MonitorIpList monitorManager=new MonitoriplistManager();
			List memlist = (List) session.getAttribute("eventlist");
			Hashtable reporthash = new Hashtable();
			reporthash.put("eventlist", memlist);
			reporthash.put("starttime", starttime);
			reporthash.put("totime", totime);

			AbstractionReport1 report = new ExcelReport1(
					new IpResourceReport(), reporthash);
			report.createReport_netevent("/temp/shijianbiao_report.xls");
			request.setAttribute("filename", report.getFileName());
		}
		if (str.equals("4")) {
			String file = "/temp/shijianbaobiao.pdf";// 保存到项目文件夹下的指定文件夹
			String fileName = ResourceCenter.getInstance().getSysPath() + file;// 获取系统文件夹路径
			try {
				createDocContextEventPDF(fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			request.setAttribute("filename", fileName);
		}
		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkCompositeReport() {
		Hashtable reporthash = (Hashtable) session.getAttribute("reporthash");
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);
		String str = request.getParameter("str");// 从页面返回设定的str值进行判断，生成excel报表或者word报表
		// zhushouzhi--------------------------------
		if ("0".equals(str)) {
			report.createReport_network("temp/networknms_report.xls");
			// SysLogger.info("filename---" + report.getFileName());//excel综合报表
			request.setAttribute("filename", report.getFileName());
		} else if ("1".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/networknms_report.doc";// 保存到项目文件夹下的指定文件夹
				String fileName = ResourceCenter.getInstance().getSysPath()
						+ file;// 获取系统文件夹路径
				report1.createReport_networkDoc(fileName);// word综合报表

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("3".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/networkNewknms_report.doc";// 保存到项目文件夹下的指定文件夹
				String fileName = ResourceCenter.getInstance().getSysPath()
						+ file;// 获取系统文件夹路径
				report1.createReport_networkNewDoc(fileName);// word运行分析报表

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ("4".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/networkNewknms_report.pdf";// 保存到项目文件夹下的指定文件夹
				String fileName = ResourceCenter.getInstance().getSysPath()
						+ file;// 获取系统文件夹路径
				report1.createReport_networkNewPdf(fileName);// pdf网络设备业务分析表

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if ("5".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/networkknms_report.pdf";// 保存到项目文件夹下的指定文件夹
				String fileName = ResourceCenter.getInstance().getSysPath()
						+ file;// 获取系统文件夹路径
				report1.createReport_networkPDF(fileName);

				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "/capreport/net/download.jsp";
	}

	private String downloadNetworkConfigReport() {
		return "/capreport/net/download.jsp";
	}

	private String area_chooseDrawLineType(String timeType, Hashtable hash,
			Hashtable hash1, String[] brand1, String[] brand2, int w, int h,
			int range1, int range2) {
		String returnStr = "";
		if (timeType == null) {
			timeType = "minute";
		}
		if ("minute".equals(timeType)) {
			returnStr = area_p_draw_line(hash, hash1, brand1, brand2, w, h,
					range1, range2);
		} else if ("hour".equals(timeType)) {
			// p_draw_lineByHour(hash, title1, title2, w, h);
		} else if ("day".equals(timeType)) {
			// p_draw_lineByDay(hash, title1, title2, w, h);
		} else if ("month".equals(timeType)) {
			// p_draw_lineByMonth(hash,title1,title2,w,h);
		} else if ("year".equals(timeType)) {
			// p_draw_line(hash,title1,title2,w,h);
		} else {
			// p_draw_line(hash,title1,title2,w,h);
		}
		// SysLogger.info("###########"+returnStr);
		return returnStr;
	}

	public String area_p_draw_line(Hashtable hash, Hashtable hash1,
			String[] brand1, String[] brand2, int w, int h, int range1,
			int range2) {
		String seriesKey = SysUtil.getLongID();
		List list = (List) hash.get("list");
		// SysLogger.info("list size ============ "+list.size());
		try {
			if (list == null || list.size() == 0) {
				// draw_blank(title1,title2,w,h);
				seriesKey = "";
			} else {
				// 初始化数据
				final JFreeChart chart = ChartFactory.createTimeSeriesChart(
						brand1[0] + brand1[1] + "趋势图", "时间", brand2[0],
						ChartCreator.createForceDataset(hash, brand1[0]), true,
						true, false);

				// 设置全图背景色为白色
				chart.setBackgroundPaint(Color.WHITE);

				final XYPlot plot = chart.getXYPlot();
				plot.getDomainAxis().setLowerMargin(0.0);
				plot.getDomainAxis().setUpperMargin(0.0);
				plot.setRangeCrosshairVisible(true);
				plot.setDomainCrosshairVisible(true);
				plot.setBackgroundPaint(Color.WHITE);
				plot.setForegroundAlpha(0.8f);
				plot.setRangeGridlinesVisible(true);
				plot.setRangeGridlinePaint(Color.darkGray);
				plot.setDomainGridlinesVisible(true);
				plot.setDomainGridlinePaint(new Color(139, 69, 19));
				XYLineAndShapeRenderer render0 = (XYLineAndShapeRenderer) plot
						.getRenderer(0);
				render0.setSeriesPaint(0, Color.BLUE);

				// configure the range axis to display directions...
				final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
				rangeAxis.setAutoRangeIncludesZero(true);
				rangeAxis.setRange(0, range1);

				plot.setRangeAxis(rangeAxis);
				final XYItemRenderer renderer2 = new XYAreaRenderer();
				final ValueAxis axis2 = new NumberAxis(brand2[1]);
				axis2.setRange(0.0, range2);

				// 设置面积图颜色
				XYAreaRenderer xyarearenderer = new XYAreaRenderer();
				xyarearenderer.setSeriesPaint(1, new Color(0, 204, 0));
				xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
				xyarearenderer.setPaint(Color.GREEN);
				plot.setDataset(1, ChartCreator.createForceDataset(hash1,
						brand1[1]));
				plot.setRenderer(1, xyarearenderer);
				plot.setRangeAxis(1, axis2);
				plot.mapDatasetToRangeAxis(1, 1);

				LegendTitle legend = chart.getLegend();
				// legend.setItemFont(new Font("Verdena", 0, 9));
				JFreeChartBrother jfb = new JFreeChartBrother();
				jfb.setChart(chart);
				jfb.setWidth(w);
				jfb.setHeight(h);

				ResourceCenter.getInstance().getChartStorage().put(seriesKey,
						jfb);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seriesKey;
	}

	public String area_p_draw_multiline(Hashtable hash, String title,
			String[] bandch, String[] bandch1, int w, int h, int range1,
			int range2) {
		String seriesKey = SysUtil.getLongID();
		Vector datasetV = ChartCreator
				.createMultiDataset(hash, bandch, bandch1);
		try {
			if (datasetV == null || datasetV.size() == 0) {
				seriesKey = "";
			} else {
				// 初始化数据
				final JFreeChart chart = ChartFactory.createTimeSeriesChart(
						title + "趋势图", "时间", bandch1[0], (XYDataset) datasetV
								.get(0), true, true, false);

				// 设置全图背景色为白色
				chart.setBackgroundPaint(Color.WHITE);
				chart.setBorderPaint(new Color(30, 144, 255));
				chart.setBorderVisible(true);

				final XYPlot plot = chart.getXYPlot();
				plot.getDomainAxis().setLowerMargin(0.0);
				plot.getDomainAxis().setUpperMargin(0.0);
				plot.setRangeCrosshairVisible(true);
				plot.setDomainCrosshairVisible(true);
				plot.setBackgroundPaint(Color.WHITE);
				plot.setForegroundAlpha(0.8f);
				plot.setRangeGridlinesVisible(true);
				plot.setRangeGridlinePaint(Color.darkGray);
				plot.setDomainGridlinesVisible(true);
				plot.setDomainGridlinePaint(new Color(139, 69, 19));
				XYLineAndShapeRenderer render0 = (XYLineAndShapeRenderer) plot
						.getRenderer(0);
				render0.setSeriesPaint(0, Color.BLUE);

				// configure the range axis to display directions...
				final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
				rangeAxis.setAutoRangeIncludesZero(true);
				rangeAxis.setRange(0, range1);

				plot.setRangeAxis(rangeAxis);
				final XYItemRenderer renderer2 = new XYAreaRenderer();
				final ValueAxis axis2 = new NumberAxis(bandch1[1]);
				axis2.setRange(0.0, range2);

				// 设置面积图颜色
				XYAreaRenderer xyarearenderer = new XYAreaRenderer();
				xyarearenderer.setSeriesPaint(1, new Color(0, 204, 0));
				xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
				xyarearenderer.setPaint(Color.GREEN);
				plot.setDataset(1, (XYDataset) datasetV.get(1));
				plot.setRenderer(1, xyarearenderer);
				plot.setRangeAxis(1, axis2);
				plot.mapDatasetToRangeAxis(1, 1);

				LegendTitle legend = chart.getLegend();
				// legend.setItemFont(new Font("Verdena", 0, 9));
				// legend.setMargin(0, 0, 0, 0);
				JFreeChartBrother jfb = new JFreeChartBrother();
				jfb.setChart(chart);
				jfb.setWidth(w);
				jfb.setHeight(h);

				ResourceCenter.getInstance().getChartStorage().put(seriesKey,
						jfb);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seriesKey;
	}

	private void p_draw_lineByHour(Hashtable hash, String title1,
			String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Hour.class);
				TimeSeries[] s = { ss };
				System.out.println(list.size());
				Vector v0 = (Vector) list.get(0);
				String dt0 = (String) v0.get(1);
				SimpleDateFormat sdf0 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date time0 = sdf0.parse(dt0);
				Calendar temp0 = Calendar.getInstance();
				temp0.setTime(time0);
				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Hour hour = new Hour(temp.get(Calendar.HOUR_OF_DAY), temp
							.get(Calendar.DAY_OF_MONTH), temp
							.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));
					if (temp.get(Calendar.HOUR_OF_DAY) > temp0
							.get(Calendar.HOUR_OF_DAY)) {
						ss.addOrUpdate(hour, d);
					}

					// ss.addOrUpdate(hour,d);
				}

				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void p_draw_lineByDay(Hashtable hash, String title1, String title2,
			int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Day.class);
				TimeSeries[] s = { ss };

				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Day day = new Day(temp.get(Calendar.DAY_OF_MONTH), temp
							.get(Calendar.MONTH) + 1, temp.get(Calendar.YEAR));

					ss.addOrUpdate(day, d);
				}

				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void p_draw_lineByMonth(Hashtable hash, String title1,
			String title2, int w, int h) {
		List list = (List) hash.get("list");
		try {
			if (list == null || list.size() == 0) {
				draw_blank(title1, title2, w, h);
			} else {
				String unit = (String) hash.get("unit");
				if (unit == null)
					unit = "%";
				ChartGraph cg = new ChartGraph();
				TimeSeries ss = new TimeSeries(title1, Month.class);
				TimeSeries[] s = { ss };

				for (int j = 0; j < list.size(); j++) {
					Vector v = (Vector) list.get(j);
					Double d = new Double((String) v.get(0));
					String dt = (String) v.get(1);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date time1 = sdf.parse(dt);
					Calendar temp = Calendar.getInstance();
					temp.setTime(time1);

					Month month = new Month(temp.get(Calendar.MONTH) + 1, temp
							.get(Calendar.YEAR));

					ss.addOrUpdate(month, d);
				}

				cg
						.timewave(s, "x(时间)", "y(" + unit + ")", title1,
								title2, w, h);
			}
			hash = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
