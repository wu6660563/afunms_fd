/**
 * @author sunqichang/孙启昌
 * Created on May 30, 2011 11:16:30 AM
 */
package com.afunms.application.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import com.afunms.capreport.model.ReportValue;
import com.afunms.capreport.model.StatisNumer;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.report.export.Excel;
import com.afunms.report.export.ExportInterface;
import com.afunms.report.export.Pdf;
import com.afunms.report.export.Word;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.report.jfree.JFreeChartBrother;
import com.lowagie.text.DocumentException;

/**
 * @author sunqichang/孙启昌
 * 
 */
public class ReportExport {
	private final int xlabel = 12;

	private ReportHelper reportHelper = null;

	private int chartWith = 768;

	private int chartHigh = 238;

	/**
	 * 导出报表
	 * 
	 * @param ids
	 *            指标id
	 * @param type
	 *            类型
	 * @param filePath
	 *            保存路径
	 * @param startTime
	 *            开始时间
	 * @param toTime
	 *            结束时间
	 * @param exportType
	 *            导出文件类型
	 */
	public void exportReport(String ids, String type, String filePath, String startTime, String toTime,
			String exportType) {
		// startTime = "2011-05-31 00:00:00";
		// toTime = "2011-05-31 23:59:59";
		HashMap<?, ArrayList<?>> hm = null;
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = null;
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "报表";
		if ("net".equalsIgnoreCase(type) || "host".equalsIgnoreCase(type)) {
			// 网络设备、服务器
			if ("net".equalsIgnoreCase(type)) {
				title = "网络设备报表";
			} else {
				title = "服务器报表";
			}
			hm = exportNetHost(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("db".equalsIgnoreCase(type)) {
			// 数据库
			title = "数据库报表";
			hm = exportDb(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("midware".equalsIgnoreCase(type)) {
			// 中间件
			title = "中间件报表";
			hm = exportMidware(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if("node".equalsIgnoreCase(type)){
			//wupinlong add for node
			title = "设备报表";
			hm = exportNode(filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		}
		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);
		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		export(export, tableList, chartList, title, time);
		if (chartList != null) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					f.delete();
				} catch (Exception e) {
					SysLogger.error("删除图片：" + chartal + "失败！", e);
				}
			}
		}
	}
	
	/**
	 * 报表订阅用，导出报表
	 * 
	 * @param ids
	 *            指标id
	 * @param type
	 *            类型
	 * @param filePath
	 *            保存路径
	 * @param startTime
	 *            开始时间
	 * @param toTime
	 *            结束时间
	 * @param exportType
	 *            导出文件类型
	 */
	public void exportCustomReport(String ids, String type, String filePath, String startTime, String toTime,
			String exportType, String dataStr[][]) {
		// startTime = "2011-05-31 00:00:00";
		// toTime = "2011-05-31 23:59:59";
		
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "报表";
		if("node".equalsIgnoreCase(type)){
			//wupinlong add for node
			title = "设备报表";
//			hm = exportNode(filePath, startTime, toTime);
			
			List<String[]> resultList = new ArrayList<String[]>();
			for (int i = 1; i < dataStr.length; i++) {
				resultList.add(dataStr[i]);
			}
			
			String[] contentTitle = {"设备名称", "IP地址", "提示信息","普通事件", "严重事件", "紧急事件"};
			ArrayList<String[]> list = new ArrayList<String[]>();
			list.add(contentTitle);
			
			list.addAll(resultList);
			tableList.add(list);
		}
		String time = startTime + "~" + toTime;
		ExportInterface export = null;
		if ("xls".equals(exportType)) {
			chartWith = 768;
			chartHigh = 238;
			export = new Excel(filePath);
		} else if ("pdf".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Pdf(filePath);
		} else if ("doc".equals(exportType)) {
			chartWith = 540;
			chartHigh = 240;
			export = new Word(filePath);
		}
		export(export, tableList, chartList, title, time);
		if (chartList != null) {
			for (int i = 0; i < chartList.size(); i++) {
				String chartal = chartList.get(i);
				try {
					File f = new File(chartal);
					f.delete();
				} catch (Exception e) {
					SysLogger.error("删除图片：" + chartal + "失败！", e);
				}
			}
		}
	}
	

	/**
	 * 导出
	 * 
	 * @param export
	 * @param tableList
	 * @param chartList
	 * @param title
	 * @param time
	 */
	private void export(ExportInterface export, ArrayList<ArrayList<String[]>> tableList, ArrayList<String> chartList,
			String title, String time) {
		if (tableList != null) {
			if (tableList.get(0) != null) {
				try {
					export.insertTitle(title, tableList.get(0).get(0).length, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			} else {
				try {
					export.insertTitle(title, 0, time);
				} catch (Exception e) {
					SysLogger.error("", e);
				}
			}
			for (int i = 0; i < tableList.size(); i++) {
			    String chartal = null;
			    if (chartList != null && chartList.size() > i) {
			        chartal = chartList.get(i);
			    }
				if (chartal != null && !"".equals(chartal.trim())) {
					try {
						export.insertChart(chartal);
					} catch (MalformedURLException e) {
						SysLogger.error("", e);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (DocumentException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
				ArrayList<String[]> tableal = tableList.get(i);
				if (tableal != null && tableal.size() > 0) {
					try {
						export.insertTable(tableal);
					} catch (IOException e) {
						SysLogger.error("", e);
					} catch (Exception e) {
						SysLogger.error("", e);
					}
				}
			}
			try {
				export.save();
			} catch (Exception e) {
				SysLogger.error("------导出文件保存失败！------", e);
			}
		}
	}

	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportNetHost(String ids, String type, String filePath, String startTime,
			String toTime) {
		HashMap hm = new HashMap();
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		// 网络设备、服务器
//		HashMap all = reportHelper.getAllValue(ids, startTime, toTime);
//		
//		
//		
//		ReportValue ping = (ReportValue) all.get("ping");
//		ReportValue cpu = (ReportValue) all.get("cpu");
//		ReportValue mem = (ReportValue) all.get("mem");
//		ReportValue port = (ReportValue) all.get("port");
//		ReportValue disk = (ReportValue) all.get("disk");
//		List<StatisNumer> table = (List<StatisNumer>) all.get("gridVlue");
//		Iterator<StatisNumer> tableIt = table.iterator();
//		ArrayList<String[]> pingal = new ArrayList<String[]>();
//		String[] pingtitle = { "ip", "当前连通率", "最小连通率", "平均连通率" };
//		pingal.add(pingtitle);
//		ArrayList<String[]> cpual = new ArrayList<String[]>();
//		String[] cputitle = { "ip", "当前利用率", "最大利用率", "平均利用率" };
//		cpual.add(cputitle);
//		ArrayList<String[]> memal = new ArrayList<String[]>();
//		String[] memtitle = { "ip", "当前利用率", "最大利用率", "平均利用率" };
//		memal.add(memtitle);
//		ArrayList<String[]> diskal = new ArrayList<String[]>();
//
//		ArrayList<String[]> portal = new ArrayList<String[]>();
//		String[] porttitle = { "端口名称", "当前流速", "最大流速", "平均流速" };
//		portal.add(porttitle);
//		ArrayList<StatisNumer> portin = new ArrayList<StatisNumer>();
//		ArrayList<StatisNumer> portout = new ArrayList<StatisNumer>();
//		while (tableIt.hasNext()) {
//			StatisNumer sn = tableIt.next();
//			String tabletype = sn.getType();
//			if ("gridPing".equals(tabletype)) {
//				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(), sn.getAverage() };
//				pingal.add(arry);
//			} else if ("gridCpu".equals(tabletype)) {
//				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(), sn.getAverage() };
//				cpual.add(arry);
//			} else if ("gridMem".equals(tabletype)) {
//				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(), sn.getAverage() };
//				memal.add(arry);
//			} else if ("gridDisk".equals(tabletype)) {
//				String[] arry = { sn.getIp() + "(" + sn.getName() + ")", sn.getCurrent() };
//				diskal.add(arry);
//			} else if ("gridPortIn".equals(tabletype)) {
//				portin.add(sn);
//			} else if ("gridPortOut".equals(tabletype)) {
//				portout.add(sn);
//			}
//		}
//		ArrayList<ArrayList<String>> portName = new ArrayList<ArrayList<String>>();
//		ArrayList<String> portInName = new ArrayList<String>();
//		ArrayList<String> portOutName = new ArrayList<String>();
//		for (int i = 0; i < portin.size(); i++) {
//			StatisNumer snin = portin.get(i);
//			StatisNumer snout = portout.get(i);
//			portInName.add("(" + snin.getName() + "入口)");
//			portOutName.add("(" + snout.getName() + "出口)");
//			String[] arry1 = { snin.getIp() + "(" + snin.getName() + "入口)", snin.getCurrent(), snin.getMaximum(),
//					snin.getAverage() };
//			String[] arry2 = { snout.getIp() + "(" + snout.getName() + "出口)", snout.getCurrent(), snout.getMaximum(),
//					snout.getAverage() };
//			portal.add(arry1);
//			portal.add(arry2);
//		}
//		tableList.add(pingal);
//		tableList.add(cpual);
//		tableList.add(memal);
//		if (diskal != null && diskal.size() > 0) {
//			tableList.add(diskal);
//		}
//		tableList.add(portal);
//		// 连通率
//		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "连通率", "时间", "");
//		chartList.add(pingpath);
//		// cpu利用率
//		String cpupath = makeJfreeChartData(cpu.getListValue(), cpu.getIpList(), "cpu利用率", "时间", "");
//		chartList.add(cpupath);
//		// 内存
//		String mempath = makeJfreeChartData(mem.getListValue(), mem.getIpList(), "内存利用率", "时间", "");
//		chartList.add(mempath);
//		if (diskal != null && diskal.size() > 0) {
//			// 磁盘
//			String diskpath = makeDiskJfreeChartData(diskal, disk.getIpList(), "磁盘利用率", "时间", "");
//			chartList.add(diskpath);
//			if ("host".equalsIgnoreCase(type)) {
//				String[] disktitle = { "IP(磁盘名称)", "当前利用率" };
//				diskal.add(0, disktitle);
//			}
//		}
//		// 端口
//		ArrayList<List<?>> alport = new ArrayList<List<?>>();
//		alport.add(port.getListValue());
//		alport.add(port.getListTemp());
//		portName.add(portInName);
//		portName.add(portOutName);
//		String portpath = makePortJfreeChartData(alport, portName, port.getIpList(), "端口", "时间", "");
//		chartList.add(portpath);

		
		String[] title = {"ip", "名称", "连通率", "CPU利用率", "内存利用率", "普通事件", "严重事件", "紧急事件"};
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(title);
		list.addAll(reportHelper.getAllValueForSubscribe(ids, startTime, toTime));
		tableList.add(list);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}
	
	
	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportNode(String filePath, String startTime,
			String toTime) {
		Hashtable hash =(Hashtable)ShareData.getNodereportData().get("nodereportHash");
		List<String[]> resultList = new ArrayList<String[]>();
		String[][] dataStr =(String[][])hash.get("dataStr");
		for (int i = 1; i < dataStr.length; i++) {
			resultList.add(dataStr[i]);
		}
		HashMap hm = new HashMap();
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		
		String[] title = {"设备名称", "IP地址", "提示信息","普通事件", "严重事件", "紧急事件"};
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(title);
		
		list.addAll(resultList);
		tableList.add(list);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}
	

	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportMidware(String ids, String type, String filePath, String startTime,
			String toTime) {
		HashMap hm = new HashMap();
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		HashMap<?, ?> mwall = reportHelper.getMidwareValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) mwall.get("ping");
		ReportValue jvm = (ReportValue) mwall.get("jvm");
		List<StatisNumer> table = (List<StatisNumer>) mwall.get("gridVlue");
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "当前连通率", "最小连通率", "平均连通率" };
		pingal.add(pingtitle);
		ArrayList<String[]> jvmal = new ArrayList<String[]>();
		String[] jvmtitle = { "ip", "当前利用率", "最大利用率", "平均利用率" };
		jvmal.add(jvmtitle);
		Iterator<StatisNumer> tableIt = table.iterator();
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPing".equals(tabletype)) {
				String name = sn.getName();
				if ("IIS".equalsIgnoreCase(name)) {
					String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(), sn.getAverage() };
					pingal.add(arry);
				} else if ("Tomcat".equalsIgnoreCase(name)) {
					String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(), sn.getAverage() };
					pingal.add(arry);
				}
			} else if ("tomcat_jvm".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMaximum(), sn.getAverage() };
				jvmal.add(arry);
			}
		}
		tableList.add(pingal);
		tableList.add(jvmal);
		// 连通率
		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "连通率", "时间", "");
		chartList.add(pingpath);
		String jvmpath = makeJfreeChartData(jvm.getListValue(), jvm.getIpList(), "利用率", "时间", "");
		chartList.add(jvmpath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;
	}

	/**
	 * @param ids
	 * @param type
	 * @param filePath
	 * @param startTime
	 * @param toTime
	 * @return
	 */
	private HashMap<?, ArrayList<?>> exportDb(String ids, String type, String filePath, String startTime, String toTime) {
		HashMap hm = new HashMap();
		// 存放表格信息
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// 存放图片路径
		ArrayList<String> chartList = new ArrayList<String>();
		// 数据库报表
		HashMap<?, ?> dball = reportHelper.getDbValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) dball.get("ping");
		List<StatisNumer> val = (List<StatisNumer>) dball.get("val");// mysql性能信息
		ReportValue tablespace = (ReportValue) dball.get("tablespace");// oracle表空间
		List<StatisNumer> table = (List<StatisNumer>) dball.get("gridVlue");
		Iterator<StatisNumer> tableIt = table.iterator();
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "当前连通率", "最小连通率", "平均连通率" };
		pingal.add(pingtitle);
		ArrayList<String[]> valal = new ArrayList<String[]>();
		String[] valtite = { "ip", "名称", "性能指标" };
		valal.add(valtite);
		ArrayList<String[]> tablespaceal = new ArrayList<String[]>();
		// String[] tablespacetitle = { "ip", "当前利用率", "最大利用率", "平均利用率" };
		// tablespaceal.add(tablespacetitle);
		while (tableIt.hasNext()) {
			StatisNumer sn = tableIt.next();
			String tabletype = sn.getType();
			if ("gridPing".equals(tabletype)) {
				String[] arry = { sn.getIp(), sn.getCurrent(), sn.getMininum(), sn.getAverage() };
				pingal.add(arry);
			} else if ("gridTableSpace".equals(tabletype)) {
				// String[] arry = { sn.getIp(), sn.getCurrent(),
				// sn.getMaximum(), sn.getAverage() };
				// tablespaceal.add(arry);
			}
		}
		Iterator<StatisNumer> valit = val.iterator();
		while (valit.hasNext()) {
			StatisNumer sn = valit.next();
			String[] arry = { sn.getIp(), sn.getName(), sn.getCurrent() };
			valal.add(arry);
		}
		tableList.add(pingal);
		tableList.add(valal);
		tableList.add(tablespaceal);
		// 连通率
		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "连通率", "时间", "");
		chartList.add(pingpath);
		// mysql 性能信息
		// String valpath = makeJfreeChartData(val.getListValue(),
		// val.getIpList(), "mysql性能信息", "时间", "");
		chartList.add("");
		// oracle表空间
		String tablespacealspath = "";
		if (tablespace.getListValue() != null && tablespace.getListValue().size() > 0) {
			tablespacealspath = makeJfreeChartData((List) tablespace.getListValue().get(0), tablespace.getIpList(),
				"oracle表空间", "时间", "");
		} else {
			SysLogger.info("------oracle表空间数据为空，未生成图片！------");
		}
		chartList.add(tablespacealspath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;

	}

	/**
	 * 生成端口jfreechart图片
	 * 
	 * @param alport
	 * @param portName
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return
	 */
	private String makePortJfreeChartData(ArrayList<List<?>> alport, ArrayList<ArrayList<String>> portName,
			List<String> ipList, String title, String xdesc, String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
				+ System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < alport.size(); i++) {
			List<List> dataList = (List<List>) alport.get(i);
			ArrayList<String> pnlist = portName.get(i);
			if (dataList != null && dataList.size() > 0) {
				int xcount = 0;
				String x = "";
				for (int j = 0; j < dataList.size(); j++) {
					x = "";
					Vector v0 = null;
					List xList = (List) dataList.get(0);
					xcount = xList.size();
					List dataList1 = (List) dataList.get(j);
					String pn = pnlist.get(j);
					for (int m = 0; m < dataList1.size(); m++) {
						// TODO:x抽时间坐标不一样，多序列不能上线显示，暂时都设为序列1的x轴时间坐标
						if (xcount <= m) {
							continue;
						}
						v0 = (Vector) xList.get(m);
						Vector v = (Vector) dataList1.get(m);
						if (xcount > xlabel) {
							x = x + "\r";
							BigDecimal bd = new BigDecimal(xcount);
							int mod = Integer.parseInt(bd.divide(new BigDecimal(xlabel), BigDecimal.ROUND_CEILING)
									.toString());
							if (m % mod == 0) {
								dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String.valueOf(ipList
										.get(j)
										+ pn), String.valueOf(v0.get(1)));
							} else {
								// 解决x坐标太多挤在一起看不清
								dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String.valueOf(ipList
										.get(j)
										+ pn), x);
							}
						} else {
							dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String.valueOf(ipList.get(j)
									+ pn), String.valueOf(v0.get(1)));
						}
					}
				}
			}
			String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc, dataset, chartWith, chartHigh);
			JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(chartkey);
			try {
				File f = new File(chartPath);
				if (!f.exists()) {
					f.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(f);
				ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart.getWidth(), chart.getHeight());
			} catch (IOException ioe) {
				chartPath = "";
				SysLogger.error("", ioe);
			}
		}
		return chartPath;
	}

	/**
	 * 生成jfreechart图片
	 * 
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return 图片路径
	 */
	private String makeJfreeChartData(List dataList, List ipList, String title, String xdesc, String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
				+ System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if (dataList != null && dataList.size() > 0) {
			int xcount = 0;
			String x = "";
			for (int j = 0; j < dataList.size(); j++) {
				x = "";
				Vector v0 = null;
				List xList = (List) dataList.get(0);
				xcount = xList.size();
				List dataList1 = (List) dataList.get(j);
				for (int m = 0; m < dataList1.size(); m++) {
					// TODO:x抽时间坐标不一样，多序列不能上线显示，暂时都设为序列1的x轴时间坐标
					if (xcount <= m) {
						continue;
					}
					v0 = (Vector) xList.get(m);
					Vector v = (Vector) dataList1.get(m);
					if (xcount > xlabel) {
						x = x + "\r";
						BigDecimal bd = new BigDecimal(xcount);
						int mod = Integer.parseInt(bd.divide(new BigDecimal(xlabel), BigDecimal.ROUND_CEILING)
								.toString());
						if (m % mod == 0) {
							dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String
									.valueOf(ipList.get(j)), String.valueOf(v0.get(1)));
						} else {
							// 解决x坐标太多挤在一起看不清
							dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String
									.valueOf(ipList.get(j)), x);
						}
					} else {
						dataset.addValue(Double.parseDouble(String.valueOf(v.get(0))), String.valueOf(ipList.get(j)),
							String.valueOf(v0.get(1)));
					}
				}
			}
			String chartkey = ChartCreator.createLineChart(title, xdesc, ydesc, dataset, chartWith, chartHigh);
			JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(chartkey);
			try {
				File f = new File(chartPath);
				if (!f.exists()) {
					f.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(f);
				ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart.getWidth(), chart.getHeight());
			} catch (IOException ioe) {
				chartPath = "";
				SysLogger.error("", ioe);
			}
		}
		return chartPath;
	}

	/**
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return
	 */
	private String makeDiskJfreeChartData(ArrayList<String[]> dataList, List ipList, String title, String xdesc,
			String ydesc) {
		String chartPath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
				+ System.currentTimeMillis() + ".png";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if (dataList != null) {
			for (int j = 0; j < dataList.size(); j++) {
				String[] dataList1 = dataList.get(j);
				if (dataList1 != null) {
					String s = dataList1[1];
					s = s.replace("%", "");
					dataset.addValue(Double.parseDouble(s), "", dataList1[0]);
				}
			}
		}

		String chartkey = ChartCreator.createBarChart(title, xdesc, ydesc, dataset, chartWith, chartHigh);
		JFreeChartBrother chart = (JFreeChartBrother) ResourceCenter.getInstance().getChartStorage().get(chartkey);
		try {
			File f = new File(chartPath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ChartUtilities.writeChartAsPNG(fos, chart.getChart(), chart.getWidth(), chart.getHeight());
		} catch (IOException ioe) {
			chartPath = "";
			SysLogger.error("", ioe);
		}
		return chartPath;
	}
}
