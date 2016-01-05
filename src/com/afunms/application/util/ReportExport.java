/**
 * @author sunqichang/������
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
 * @author sunqichang/������
 * 
 */
public class ReportExport {
	private final int xlabel = 12;

	private ReportHelper reportHelper = null;

	private int chartWith = 768;

	private int chartHigh = 238;

	/**
	 * ��������
	 * 
	 * @param ids
	 *            ָ��id
	 * @param type
	 *            ����
	 * @param filePath
	 *            ����·��
	 * @param startTime
	 *            ��ʼʱ��
	 * @param toTime
	 *            ����ʱ��
	 * @param exportType
	 *            �����ļ�����
	 */
	public void exportReport(String ids, String type, String filePath, String startTime, String toTime,
			String exportType) {
		// startTime = "2011-05-31 00:00:00";
		// toTime = "2011-05-31 23:59:59";
		HashMap<?, ArrayList<?>> hm = null;
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = null;
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "����";
		if ("net".equalsIgnoreCase(type) || "host".equalsIgnoreCase(type)) {
			// �����豸��������
			if ("net".equalsIgnoreCase(type)) {
				title = "�����豸����";
			} else {
				title = "����������";
			}
			hm = exportNetHost(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("db".equalsIgnoreCase(type)) {
			// ���ݿ�
			title = "���ݿⱨ��";
			hm = exportDb(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if ("midware".equalsIgnoreCase(type)) {
			// �м��
			title = "�м������";
			hm = exportMidware(ids, type, filePath, startTime, toTime);
			tableList = (ArrayList<ArrayList<String[]>>) hm.get("table");
			chartList = (ArrayList<String>) hm.get("chart");
		} else if("node".equalsIgnoreCase(type)){
			//wupinlong add for node
			title = "�豸����";
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
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}
	
	/**
	 * �������ã���������
	 * 
	 * @param ids
	 *            ָ��id
	 * @param type
	 *            ����
	 * @param filePath
	 *            ����·��
	 * @param startTime
	 *            ��ʼʱ��
	 * @param toTime
	 *            ����ʱ��
	 * @param exportType
	 *            �����ļ�����
	 */
	public void exportCustomReport(String ids, String type, String filePath, String startTime, String toTime,
			String exportType, String dataStr[][]) {
		// startTime = "2011-05-31 00:00:00";
		// toTime = "2011-05-31 23:59:59";
		
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		reportHelper = new ReportHelper();
		String title = "����";
		if("node".equalsIgnoreCase(type)){
			//wupinlong add for node
			title = "�豸����";
//			hm = exportNode(filePath, startTime, toTime);
			
			List<String[]> resultList = new ArrayList<String[]>();
			for (int i = 1; i < dataStr.length; i++) {
				resultList.add(dataStr[i]);
			}
			
			String[] contentTitle = {"�豸����", "IP��ַ", "��ʾ��Ϣ","��ͨ�¼�", "�����¼�", "�����¼�"};
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
					SysLogger.error("ɾ��ͼƬ��" + chartal + "ʧ�ܣ�", e);
				}
			}
		}
	}
	

	/**
	 * ����
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
				SysLogger.error("------�����ļ�����ʧ�ܣ�------", e);
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
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		// �����豸��������
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
//		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
//		pingal.add(pingtitle);
//		ArrayList<String[]> cpual = new ArrayList<String[]>();
//		String[] cputitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
//		cpual.add(cputitle);
//		ArrayList<String[]> memal = new ArrayList<String[]>();
//		String[] memtitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
//		memal.add(memtitle);
//		ArrayList<String[]> diskal = new ArrayList<String[]>();
//
//		ArrayList<String[]> portal = new ArrayList<String[]>();
//		String[] porttitle = { "�˿�����", "��ǰ����", "�������", "ƽ������" };
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
//			portInName.add("(" + snin.getName() + "���)");
//			portOutName.add("(" + snout.getName() + "����)");
//			String[] arry1 = { snin.getIp() + "(" + snin.getName() + "���)", snin.getCurrent(), snin.getMaximum(),
//					snin.getAverage() };
//			String[] arry2 = { snout.getIp() + "(" + snout.getName() + "����)", snout.getCurrent(), snout.getMaximum(),
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
//		// ��ͨ��
//		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "��ͨ��", "ʱ��", "");
//		chartList.add(pingpath);
//		// cpu������
//		String cpupath = makeJfreeChartData(cpu.getListValue(), cpu.getIpList(), "cpu������", "ʱ��", "");
//		chartList.add(cpupath);
//		// �ڴ�
//		String mempath = makeJfreeChartData(mem.getListValue(), mem.getIpList(), "�ڴ�������", "ʱ��", "");
//		chartList.add(mempath);
//		if (diskal != null && diskal.size() > 0) {
//			// ����
//			String diskpath = makeDiskJfreeChartData(diskal, disk.getIpList(), "����������", "ʱ��", "");
//			chartList.add(diskpath);
//			if ("host".equalsIgnoreCase(type)) {
//				String[] disktitle = { "IP(��������)", "��ǰ������" };
//				diskal.add(0, disktitle);
//			}
//		}
//		// �˿�
//		ArrayList<List<?>> alport = new ArrayList<List<?>>();
//		alport.add(port.getListValue());
//		alport.add(port.getListTemp());
//		portName.add(portInName);
//		portName.add(portOutName);
//		String portpath = makePortJfreeChartData(alport, portName, port.getIpList(), "�˿�", "ʱ��", "");
//		chartList.add(portpath);

		
		String[] title = {"ip", "����", "��ͨ��", "CPU������", "�ڴ�������", "��ͨ�¼�", "�����¼�", "�����¼�"};
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
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		
		String[] title = {"�豸����", "IP��ַ", "��ʾ��Ϣ","��ͨ�¼�", "�����¼�", "�����¼�"};
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
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		HashMap<?, ?> mwall = reportHelper.getMidwareValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) mwall.get("ping");
		ReportValue jvm = (ReportValue) mwall.get("jvm");
		List<StatisNumer> table = (List<StatisNumer>) mwall.get("gridVlue");
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
		pingal.add(pingtitle);
		ArrayList<String[]> jvmal = new ArrayList<String[]>();
		String[] jvmtitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
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
		// ��ͨ��
		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "��ͨ��", "ʱ��", "");
		chartList.add(pingpath);
		String jvmpath = makeJfreeChartData(jvm.getListValue(), jvm.getIpList(), "������", "ʱ��", "");
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
		// ��ű����Ϣ
		ArrayList<ArrayList<String[]>> tableList = new ArrayList<ArrayList<String[]>>();
		// ���ͼƬ·��
		ArrayList<String> chartList = new ArrayList<String>();
		// ���ݿⱨ��
		HashMap<?, ?> dball = reportHelper.getDbValue(ids, startTime, toTime);
		ReportValue ping = (ReportValue) dball.get("ping");
		List<StatisNumer> val = (List<StatisNumer>) dball.get("val");// mysql������Ϣ
		ReportValue tablespace = (ReportValue) dball.get("tablespace");// oracle��ռ�
		List<StatisNumer> table = (List<StatisNumer>) dball.get("gridVlue");
		Iterator<StatisNumer> tableIt = table.iterator();
		ArrayList<String[]> pingal = new ArrayList<String[]>();
		String[] pingtitle = { "ip", "��ǰ��ͨ��", "��С��ͨ��", "ƽ����ͨ��" };
		pingal.add(pingtitle);
		ArrayList<String[]> valal = new ArrayList<String[]>();
		String[] valtite = { "ip", "����", "����ָ��" };
		valal.add(valtite);
		ArrayList<String[]> tablespaceal = new ArrayList<String[]>();
		// String[] tablespacetitle = { "ip", "��ǰ������", "���������", "ƽ��������" };
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
		// ��ͨ��
		String pingpath = makeJfreeChartData(ping.getListValue(), ping.getIpList(), "��ͨ��", "ʱ��", "");
		chartList.add(pingpath);
		// mysql ������Ϣ
		// String valpath = makeJfreeChartData(val.getListValue(),
		// val.getIpList(), "mysql������Ϣ", "ʱ��", "");
		chartList.add("");
		// oracle��ռ�
		String tablespacealspath = "";
		if (tablespace.getListValue() != null && tablespace.getListValue().size() > 0) {
			tablespacealspath = makeJfreeChartData((List) tablespace.getListValue().get(0), tablespace.getIpList(),
				"oracle��ռ�", "ʱ��", "");
		} else {
			SysLogger.info("------oracle��ռ�����Ϊ�գ�δ����ͼƬ��------");
		}
		chartList.add(tablespacealspath);
		hm.put("table", tableList);
		hm.put("chart", chartList);
		return hm;

	}

	/**
	 * ���ɶ˿�jfreechartͼƬ
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
						// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
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
								// ���x����̫�༷��һ�𿴲���
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
	 * ����jfreechartͼƬ
	 * 
	 * @param dataList
	 * @param ipList
	 * @param title
	 * @param xdesc
	 * @param ydesc
	 * @return ͼƬ·��
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
					// TODO:x��ʱ�����겻һ���������в���������ʾ����ʱ����Ϊ����1��x��ʱ������
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
							// ���x����̫�༷��һ�𿴲���
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
