package com.afunms.polling.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.afunms.capreport.common.DateTime;
import com.afunms.capreport.dao.BaseDaoImp;
import com.afunms.config.dao.HaweitelnetconfDao;
import com.afunms.config.dao.Hua3VPNFileConfigDao;
import com.afunms.config.model.Hua3VPNFileConfig;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.telnet.CiscoTelnet;
import com.afunms.polling.telnet.Huawei3comvpn;

/**
 * 定时备份配置文件
 * @author HONGLI
 */
public class M30BackupTelnetConfigTask extends MonitorTask {
	private static Logger log = Logger.getLogger(M30BackupTelnetConfigTask.class);

	private final String hms = " 00:00:00";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		subscribe();
//		System.gc();
	}

	/**
	 * 定时备份配置文件
	 */
	private void subscribe() { 
		BaseDaoImp cd = new BaseDaoImp();
		DateTime dt = new DateTime();
		String time = dt.getMyDateTime(DateTime.Datetime_Format_14);
		log.info("-------------------------------(定时备份)定时器执行时间：" + dt.getMyDateTime(DateTime.Datetime_Format_2)
				+ "-------------------------------");
		String sql = "SELECT * FROM sys_timingbackup_telnetconfig s WHERE status = '1' and s.BACKUP_DATE > 10000 AND s.BACKUP_DATE < "
				+ time;
		ArrayList<Map<String, String>> ssconfAL = cd.executeQuery(sql);
		Map<String, String> ssidAL = null;
		if (ssconfAL != null) {
			try {
				for (int i = 0; i < ssconfAL.size(); i++) {
					ssidAL = ssconfAL.get(i);
					String status = ssidAL.get("status");
					String telnetconfigips = ssidAL.get("telnetconfigips");
					String backup_sendfrequency = ssidAL.get("BACKUP_SENDFREQUENCY");
					String backup_time_month = ssidAL.get("BACKUP_TIME_MONTH");
					String backup_time_week = ssidAL.get("BACKUP_TIME_WEEK");
					String backup_time_day = ssidAL.get("BACKUP_TIME_DAY");
					String backup_time_hou = ssidAL.get("BACKUP_TIME_HOU");
					String exportType = ssidAL.get("ATTACHMENTFORMAT");
					String bkpType = ssidAL.get("bkpType");
					boolean istrue = false;
					// 发送频率，0:全部发送;1:每天;2:每周;3:每月;4每季度;5每年
					if ("0".equals(backup_sendfrequency)) {
						istrue = true;
					} else if ("1".equals(backup_sendfrequency)) {
						if (backup_time_hou.contains("/" + (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours())
								+ "/")) {
							istrue = true;
						}
					} else if ("2".equals(backup_sendfrequency)) {
						if (backup_time_week.contains("/" + (dt.getDay() - 1) + "/")
								&& backup_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("3".equals(backup_sendfrequency)) {
						if (backup_time_day.contains("/" + (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate())
								+ "/")
								&& backup_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("4".equals(backup_sendfrequency)) {
						if (backup_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
								+ "/")
								&& backup_time_day.contains("/"
										+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
								&& backup_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					} else if ("5".equals(backup_sendfrequency)) {
						if (backup_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
								+ "/")
								&& backup_time_day.contains("/"
										+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
								&& backup_time_hou.contains("/"
										+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
							istrue = true;
						}
					}
					if (istrue) {
						log.info("定时备份配置文件开始--telnetconfigips=" + telnetconfigips);
//						String filePath = ResourceCenter.getInstance().getSysPath() + "temp" + File.separator
//								+ "backup" + "_" + System.currentTimeMillis() + "." + exportType;
//						if (doSubscribe(ssidAL, filePath)) {
//							sendMail(ssidAL, filePath);
//						} else {
//							log.error("订阅" + subscribe_id + "失败！");
//						}
						if(!telnetconfigips.equals("") && telnetconfigips != null){
							String[] ips = telnetconfigips.split(",");
							HaweitelnetconfDao dao = new HaweitelnetconfDao();
							for(String ip : ips){
								if(ip != null && !ip.equals("") && !ip.equals(",") && "1".equals(status)){
									Huaweitelnetconf vo = (Huaweitelnetconf)dao.loadByIp(ip);
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
									String b_time = sdf.format(new Date());
									String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "/");
									String fileName = prefix + "cfg/"+vo.getIpaddress()+"_"+b_time+"cfg.cfg";
									String descri = vo.getIpaddress()+"_"+b_time;
									bkpCfg_1(ip,fileName,descri,bkpType);
								}
							}
							dao.close();
						}
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
	
	/**
	 * 备份配置文件
	 * @param id
	 * @param fileName
	 */
	private void bkpCfg_1(String ip,String fileName,String descri,String bkptype){
//		String id = getParaValue("id");//Huaweitelnetconf 的主键ID
//		String fileName = this.getParaValue("fileName");
//		String fileDesc = this.getParaValue("fileDesc");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
		String bkptime = "";
		Date bkpDate = new Date();
		String reg = "_(.*)cfg.cfg";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(fileName);
		if(m.find())
		{
			bkptime = m.group(1);
		}
		try{
			bkpDate = sdf.parse(bkptime);
		}catch(Exception e){e.printStackTrace();}
		
		HaweitelnetconfDao dao = new HaweitelnetconfDao();
		Huaweitelnetconf vo = (Huaweitelnetconf)dao.loadByIp(ip);
		dao.close();
		
		String result = "";
		String jsp = null;
		if(vo.getDeviceRender().equals("h3c"))//h3c
		{
			Huawei3comvpn tvpn = new Huawei3comvpn();
			tvpn.setSuuser(vo.getSuuser());// su
			tvpn.setSupassword(vo.getSupassword());// su密码
			tvpn.setUser(vo.getUser());// 用户
			tvpn.setPassword(vo.getPassword());// 密码
			tvpn.setIp(vo.getIpaddress());// ip地址
			tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// 结束标记符号
			tvpn.setPort(vo.getPort());
			result = tvpn.Backupconffile(bkptype);
			if(!result.equals("user or password error"))
			{
				jsp = "/config/vpntelnet/status.jsp";
			}
			//System.out.println("############################################123");
			//System.out.println(result);
		}
		else if(vo.getDeviceRender().equals("cisco"))//cisco
		{
			CiscoTelnet telnet = new CiscoTelnet(vo.getIpaddress(), vo.getUser(),vo.getPassword());
			if(telnet.login())
			{
				result = telnet.getCfg(vo.getSupassword(),bkptype);
				jsp = "/config/vpntelnet/status.jsp";
			}
		}
		if(jsp != null)
		{
			File f = new File(fileName);
			int fileSize = 0;
			try{
				FileWriter fw = new FileWriter(f);
				fw.write(result);
				fw.flush();
				fw.close();
				FileInputStream fis = new FileInputStream(f);
				fileSize = fis.available();
				if(fileSize!=0)
				{
					fileSize = fileSize/1000;
					if(fileSize==0)
						fileSize = 1;
				}
			}catch(Exception e){e.printStackTrace();}

			Hua3VPNFileConfig h3vpn = new Hua3VPNFileConfig();
			h3vpn.setFileName(fileName);
			h3vpn.setDescri(descri);
			h3vpn.setIpaddress(vo.getIpaddress());
			h3vpn.setFileSize(fileSize);
			h3vpn.setBackupTime(new Timestamp(bkpDate.getTime()));
			Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
			h3Dao.save(h3vpn);
			h3Dao.close();
		}
//		return jsp;
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
}
