/**
 * @author sunqichang/孙启昌
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
 * 订阅分发邮件
 * 
 * @author sunqichang/孙启昌
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
	 * 订阅
	 */
	private void subscribe() {
		DateTime dt = new DateTime();
		String time = dt.getMyDateTime(DateTime.Datetime_Format_14);
		log.info("-------------------------------订阅定时器执行时间：" + dt.getMyDateTime(DateTime.Datetime_Format_2)
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
					// 发送频率，0:全部发送;1:每天;2:每周;3:每月;4每季度;5每年
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
						log.info("订阅报表开始--subscribe_id=" + subscribe_id);
						String filePath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
								+ "report" + "_" + System.currentTimeMillis() + "." + exportType;
						if (doSubscribe(ssidAL, filePath)) {
							sendMail(ssidAL, filePath);
						} else {
							log.error("订阅" + subscribe_id + "失败！");
						}
					} else {
					    log.info("订阅报表不在当前发送时段" + subscribe_id + "===" + (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()));
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
	 * 发送邮件
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
					log.warn("------删除订阅文件:" + filePath + " 失败!------");
				}
			}
			updateSubscribeHistory(ssidAL, filePath, true);
			log.info("------发送邮件成功！------");
		} else {
			updateSubscribeHistory(ssidAL, filePath, false);
		}
	}

	/**
	 * 报表分类
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
	 * 日期格式转换
	 * 
	 * @param startTime
	 * @return
	 */
	public String startTime(String startTime) {
		return startTime + hms;
	}

	/**
	 * 日期格式转换
	 * 
	 * @param toTime
	 * @return
	 */
	public String toTime(String toTime) {
		String Millisecond = String.valueOf(DateTime.getMillisecond(toTime, DateTime.Datetime_Format_5) - 1000);
		return DateTime.getDateFromMillisecond(Millisecond, DateTime.Datetime_Format_2);
	}

	/**
	 * 更新订阅发送时间
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
			log.error("更新订阅发送时间失败！  sql: " + sql, e);
		}
	}

	/**
	 * 将订阅产生的文件记录插入到数据库中
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
			log.error("订阅文件记录插入失败！  sql: " + sql, e);
		}
	}

	/**
	 * 更新订阅历史记录
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
			log.error("更新订阅历史记录失败！  sql: " + sql, e);
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
	// // 实例化ie对象
	// IE ie = new IE();
	// try {
	// ie.start(url);// 打开新的IE浏览器
	// // ie.goTo(url); // 转到url
	// ie.bringToFront();// 将IE窗口置为最前
	// ie.maximize(); // 最大化窗口
	// ie.focus();// 设置为当前焦点
	// StringBuffer js = new StringBuffer();
	// js.append("if(document!=null&&document.getElementById('mystartdate')!=null){");
	// js.append("document.getElementById('mystartdate').value='");
	// js.append(startdate);
	// js.append("';document.getElementById('mytodate').value='");
	// js.append(todate);
	// js.append("';document.getElementById('ids').value='").append(ids).append("';");
	// js.append("query_ok();");
	// js.append("}");
	// // 执行js
	// ie.executeScript(js.toString());
	// long st = System.currentTimeMillis();
	// String imageName = dt.getMyDateTime(DateTime.Datetime_Format_1);
	// System.out.println("~~~~~~~~~~~~开始~~~~~~~~~~~~~~~~~");
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
	// log.info("------导出flash图片超时，请联系管理员查找原因！------");
	// break;
	// }
	// Thread.sleep(100);
	// }
	// ie.close();// 关闭ie游览器
	// filename = "D:\\tomcat6\\webapps\\afunms\\temp" + File.separator +
	// imageName + ".png";
	// } catch (Exception e) {
	// log.error("", e);
	// }
	// }
	// -------画图----------
	// String timeStamp = "yyyy-MM-dd HH:mm:ss";
	// TimeSeries[] series = new TimeSeries[2];
	// TimeSeries series1 = new TimeSeries("CPU利用率", Hour.class);
	// TimeSeries series2 = new TimeSeries("CPU利用率", Hour.class);
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
	// "HOUR", "X-时间(h)", "Y-CPU利用率(%)", "CPU利用率",
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
