/**
 * @author sunqichang/������
 * Created on May 13, 2011 4:41:35 PM
 */
package com.afunms.capreport.subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.afunms.application.util.ReportExport;
import com.afunms.capreport.common.DateTime;
import com.afunms.capreport.dao.BaseDaoImp;
import com.afunms.capreport.dao.UtilReportDao;
import com.afunms.capreport.mail.MailInfo;
import com.afunms.capreport.mail.MailSender;
import com.afunms.capreport.model.UtilReport;
import com.afunms.initialize.ResourceCenter;

/**
 * ���ķַ��ʼ�
 * 
 * @author sunqichang/������
 */
public class SubscribeTask extends TimerTask {
	private static Logger log = Logger.getLogger(SubscribeTask.class);

	private final String hms = " 00:00:00";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		subscribe();
		System.gc();
	}

	/**
	 * ����
	 */
	private void subscribe() {
		DateTime dt = new DateTime();
		String time = dt.getMyDateTime(DateTime.Datetime_Format_14);
		log.info("-------------------------------���Ķ�ʱ��ִ��ʱ�䣺" + dt.getMyDateTime(DateTime.Datetime_Format_2)
				+ "-------------------------------");
		String sql = "SELECT * FROM sys_subscribe_resources s WHERE s.REPORT_SENDDATE > 10000 AND s.REPORT_SENDDATE <= "
				+ time;
		BaseDaoImp cd = new BaseDaoImp();
        ArrayList<Map<String, String>> ssconfAL = null;
        try {
            ssconfAL = cd.executeQuery(sql);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            cd.close();
        }
		Map<String, String> ssidAL = null;
		if (ssconfAL != null) {
			try {
				for (int i = 0; i < ssconfAL.size(); i++) {
					ssidAL = ssconfAL.get(i);
					String subscribe_id = ssidAL.get("SUBSCRIBE_ID");
					String report_sendfrequency = ssidAL.get("REPORT_SENDFREQUENCY");
					String report_time_month = ssidAL.get("REPORT_TIME_MONTH");
					String report_time_week = ssidAL.get("REPORT_TIME_WEEK");
					String report_time_day = ssidAL.get("REPORT_TIME_DAY");
					String report_time_hou = ssidAL.get("REPORT_TIME_HOU");
					String exportType = ssidAL.get("ATTACHMENTFORMAT");
					boolean istrue = false;
					// ����Ƶ�ʣ�0:ȫ������;1:ÿ��;2:ÿ��;3:ÿ��;4ÿ����;5ÿ��
					if ("0".equals(report_sendfrequency)) {
						istrue = true;
					} else if ("1".equals(report_sendfrequency)) {
						if (report_time_hou.contains("/" + (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours())
								+ "/")) {
							istrue = true;
						}
					} else if ("2".equals(report_sendfrequency)) {
						if (report_time_week.contains("/" + (dt.getDay() - 1) + "/")
								&& report_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("3".equals(report_sendfrequency)) {
						if (report_time_day.contains("/" + (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate())
								+ "/")
								&& report_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("4".equals(report_sendfrequency)) {
						if (report_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
								+ "/")
								&& report_time_day.contains("/"
										+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
								&& report_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("5".equals(report_sendfrequency)) {
						if (report_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
								+ "/")
								&& report_time_day.contains("/"
										+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
								&& report_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					}
					if (istrue) {
						log.info("���ı���ʼ--subscribe_id=" + subscribe_id);
						String filePath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
								+ "report" + "_" + System.currentTimeMillis() + "." + exportType;
						if (doSubscribe(ssidAL, filePath)) {
							sendMail(ssidAL, filePath);
						} else {
							log.error("����" + subscribe_id + "ʧ�ܣ�");
						}
					} else {
					    log.info("���ı����ڵ�ǰ����ʱ��" + subscribe_id + "===" + (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()));
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		} else {
		    log.info("ssidAL is null");
		}
	}

	/**
	 * �����ʼ�
	 * 
	 * @param ssidAL
	 * @param filePath
	 */
	private void sendMail(Map<String, String> ssidAL, String filePath) {
		MailInfo mailinfo = new MailInfo();
		mailinfo.setAffixPath(filePath);
		mailinfo.setContent(ssidAL.get("EMAILCONTENT"));
		mailinfo.setSubject(ssidAL.get("EMAILTITLE"));
		mailinfo.setReceiver(ssidAL.get("EMAIL"));
		if (MailSender.send(mailinfo)) {
			File f = new File(filePath);
			if (f != null && f.canWrite()) {
				boolean b = f.delete();
				if (!b) {
					log.warn("------ɾ�������ļ�:" + filePath + " ʧ��!------");
				}
			}
			updateSubscribeHistory(ssidAL, filePath, true);
			log.info("------�����ʼ��ɹ���------");
		} else {
			updateSubscribeHistory(ssidAL, filePath, false);
		}
	}

	/**
	 * �������
	 * 
	 * @param ssidAL
	 * @param filePath
	 * @return
	 */
	private boolean doSubscribe(Map<String, String> ssidAL, String filePath) {
		boolean flag = true;
		String report_day_stop = ssidAL.get("REPORT_DAY_STOP");
		String report_week_stop = ssidAL.get("REPORT_WEEK_STOP");
		String report_month_stop = ssidAL.get("REPORT_MONTH_STOP");
		String report_season_stop = ssidAL.get("REPORT_SEASON_STOP");
		String report_year_stop = ssidAL.get("REPORT_YEAR_STOP");
		String reportType = ssidAL.get("REPORT_TYPE");
		String subscribe_id = ssidAL.get("SUBSCRIBE_ID");
		String exportType = ssidAL.get("ATTACHMENTFORMAT");
		DateTime dt = new DateTime();
		String startTime = null;
		String toTime = null;
		ReportExport rh = new ReportExport();
		UtilReportDao urd = new UtilReportDao();
		UtilReport ur = urd.findByBid(subscribe_id);
		if (ur != null) {
			if ("day".equalsIgnoreCase(reportType) && !"true".equalsIgnoreCase(report_day_stop)) {
				String daystr = dt.getLastDay();
				startTime = daystr.split("~")[0];
				toTime = daystr.split("~")[1];
				rh.exportReport(ur.getIds(), ur.getType(), filePath, startTime(startTime), toTime(toTime), exportType);
			} else if ("week".equalsIgnoreCase(reportType) && !"true".equalsIgnoreCase(report_week_stop)) {
				String weekstr = dt.getLastWeek(1);
				startTime = weekstr.split("~")[0];
				toTime = weekstr.split("~")[1];
				rh.exportReport(ur.getIds(), ur.getType(), filePath, startTime(startTime), toTime(toTime), exportType);
			} else if ("month".equalsIgnoreCase(reportType) && !"true".equalsIgnoreCase(report_month_stop)) {
				String monthstr = dt.getLastMonth();
				startTime = monthstr.split("~")[0];
				toTime = monthstr.split("~")[1];
				rh.exportReport(ur.getIds(), ur.getType(), filePath, startTime(startTime), toTime(toTime), exportType);
			} else if ("season".equalsIgnoreCase(reportType) && !"true".equalsIgnoreCase(report_season_stop)) {
				String seasonstr = dt.getLastSeason();
				startTime = seasonstr.split("~")[0];
				toTime = seasonstr.split("~")[1];
				rh.exportReport(ur.getIds(), ur.getType(), filePath, startTime(startTime), toTime(toTime), exportType);
			} else if ("year".equalsIgnoreCase(reportType) && !"true".equalsIgnoreCase(report_year_stop)) {
				String yearstr = dt.getLastYear();
				startTime = yearstr.split("~")[0];
				toTime = yearstr.split("~")[1];
				rh.exportReport(ur.getIds(), ur.getType(), filePath, startTime(startTime), toTime(toTime), exportType);
			} else {
				flag = false;
			}
			if (flag) {
				insertSubscribeHistory(ssidAL, filePath);
				updateSubscribeResources(subscribe_id);
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * ���ڸ�ʽת��
	 * 
	 * @param startTime
	 * @return
	 */
	public String startTime(String startTime) {
		return startTime + hms;
	}

	/**
	 * ���ڸ�ʽת��
	 * 
	 * @param toTime
	 * @return
	 */
	public String toTime(String toTime) {
		String Millisecond = String.valueOf(DateTime.getMillisecond(toTime, DateTime.Datetime_Format_5) - 1000);
		return DateTime.getDateFromMillisecond(Millisecond, DateTime.Datetime_Format_2);
	}

	/**
	 * ���¶��ķ���ʱ��
	 * 
	 * @param subscribe_id
	 */
	private void updateSubscribeResources(String subscribe_id) {
		DateTime dt = new DateTime();
		BaseDaoImp cd = new BaseDaoImp();
		String sql = null;
		try {
			sql = "update sys_subscribe_resources set report_senddate = '"
					+ dt.getMyDateTime(DateTime.Datetime_Format_14) + "' where	subscribe_id = '" + subscribe_id + "' ";
			cd.saveOrUpdate(sql);
		} catch (Exception e) {
			log.error("���¶��ķ���ʱ��ʧ�ܣ�  sql: " + sql, e);
		}
	}

	/**
	 * �����Ĳ������ļ���¼���뵽���ݿ���
	 * 
	 * @param ssidAL
	 * @param filepath
	 */
	private void insertSubscribeHistory(Map<String, String> ssidAL, String filepath) {
		BaseDaoImp cd = new BaseDaoImp();
		DateTime dt = new DateTime();
		String sql = null;
		try {
			String subscribe_id = ssidAL.get("SUBSCRIBE_ID");
			String report_type = ssidAL.get("REPORT_TYPE");
			filepath = filepath.replace("\\", "\\\\");
			sql = "insert into sys_subscribe_history (subscribe_id,file_name,report_type,create_date,success_post,repeat_post) values ("
					+ subscribe_id
					+ ",'"
					+ filepath
					+ "','"
					+ report_type
					+ "','"
					+ dt.getMyDateTime(DateTime.Datetime_Format_1) + "','0','0'	)";
			cd.saveOrUpdate(sql);
		} catch (Exception e) {
			log.error("�����ļ���¼����ʧ�ܣ�  sql: " + sql, e);
		}
	}

	/**
	 * ���¶�����ʷ��¼
	 * 
	 * @param ssidAL
	 * @param filepath
	 * @param flag
	 */
	private void updateSubscribeHistory(Map<String, String> ssidAL, String filepath, boolean flag) {
		BaseDaoImp cd = new BaseDaoImp();
		String sql = null;
		try {
			String subscribe_id = ssidAL.get("SUBSCRIBE_ID");
			filepath = filepath.replace("\\", "\\\\");
			if (flag) {
				sql = "update sys_subscribe_history set success_post = success_post+1 where subscribe_id = '"
						+ subscribe_id + "' and file_name = '" + filepath + "'";
			} else {
				sql = "update sys_subscribe_history set repeat_post = repeat_post+1 where subscribe_id = '"
						+ subscribe_id + "' and file_name = '" + filepath + "'";
			}
			cd.saveOrUpdate(sql);
		} catch (Exception e) {
			log.error("���¶�����ʷ��¼ʧ�ܣ�  sql: " + sql, e);
		}
	}
	// private void daySubscribe(Map<String, String> ssidAL, String
	// subscribe_id,
	// String filePath) {
	// DateTime dt = new DateTime();
	// String filename = "";
	// if (isWindows()) {
	// String ids = "cpu10.10.1.1,mem10.10.1.1";
	// String startdate = dt.getMyDateTime(DateTime.Datetime_Format_5);
	// String todate = dt.getMyDateTime(DateTime.Datetime_Format_5);
	// startdate = "2011-5-1";
	// todate = "2011-5-20";
	// String url =
	// "http://localhost:8090/afunms/netreport.do?action=networkReport&reportSubscribe=true&ids="
	// + ids + "&startdate=" + startdate + "&todate=" + todate + "&nowtime="
	// + new Date();
	// // ʵ����ie����
	// IE ie = new IE();
	// try {
	// ie.start(url);// ���µ�IE�����
	// // ie.goTo(url); // ת��url
	// ie.bringToFront();// ��IE������Ϊ��ǰ
	// ie.maximize(); // ��󻯴���
	// ie.focus();// ����Ϊ��ǰ����
	// StringBuffer js = new StringBuffer();
	// js.append("if(document!=null&&document.getElementById('mystartdate')!=null){");
	// js.append("document.getElementById('mystartdate').value='");
	// js.append(startdate);
	// js.append("';document.getElementById('mytodate').value='");
	// js.append(todate);
	// js.append("';document.getElementById('ids').value='").append(ids).append("';");
	// js.append("query_ok();");
	// js.append("}");
	// // ִ��js
	// ie.executeScript(js.toString());
	// long st = System.currentTimeMillis();
	// String imageName = dt.getMyDateTime(DateTime.Datetime_Format_1);
	// System.out.println("~~~~~~~~~~~~��ʼ~~~~~~~~~~~~~~~~~");
	// int status = 0;
	// Object result = null;
	// while (true) {
	// Object flag = ie.executeScript("isExport();");
	// System.out.println("~~~~~~~~~~~~~flag~~~~~~~~~~~~~~~~" + flag);
	// if ("true".equals(flag) && status == 0) {
	// result = ie.executeScript("exportImage(" + imageName + ");");
	// System.out.println("~~~~~~~~result~~~~~~~~~~~~~~~~~~~~~" + result);
	// status = 1;
	// }
	// if (!url.equals(ie.url()) || "false".equals(result)) {
	// break;
	// }
	// if (System.currentTimeMillis() - st >= 1000 * 60 * 10) {
	// log.info("------����flashͼƬ��ʱ������ϵ����Ա����ԭ��------");
	// break;
	// }
	// Thread.sleep(100);
	// }
	// ie.close();// �ر�ie������
	// filename = "D:\\tomcat6\\webapps\\afunms\\temp" + File.separator +
	// imageName + ".png";
	// } catch (Exception e) {
	// log.error("", e);
	// }
	// }
	// -------��ͼ----------
	// String timeStamp = "yyyy-MM-dd HH:mm:ss";
	// TimeSeries[] series = new TimeSeries[2];
	// TimeSeries series1 = new TimeSeries("CPU������", Hour.class);
	// TimeSeries series2 = new TimeSeries("CPU������", Hour.class);
	// Date curDate = new Date();
	// float[] tmpTable = { 1, 2, 3, 4, 5, 6 };
	// try {
	// curDate = DateFormat.getDateInstance().parse(timeStamp);
	// } catch (Exception e) {
	// }
	//
	// for (int i = 0; i < tmpTable.length; i++) {
	// series1.add(new Hour(i, new Day(curDate)), tmpTable[i]);
	// series2.add(new Hour(i, new Day(curDate)), tmpTable[i] + 1);
	// }
	// series[0] = series1;
	// series[1] = series2;
	//
	// String chartKey = ChartCreator.createMultiTimeSeriesChart(series,
	// "HOUR", "X-ʱ��(h)", "Y-CPU������(%)", "CPU������",
	// 500, 350);
	// JFreeChartBrother chart = (JFreeChartBrother)
	// ResourceCenter.getInstance().getChartStorage().get(chartKey);
	// try {
	// FileOutputStream fos = new FileOutputStream(new File("d://sqc.png"));
	// ChartUtilities.writeChartAsPNG(fos, chart.getChart(),
	// chart.getWidth(), chart.getHeight());
	// } catch (IOException ioe) {
	// log.error("", ioe);
	// }
	// Excel e = new Excel();
	// e.exportExcel(null, filename);
	// exportReport();
	// }

}
