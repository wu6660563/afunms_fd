/*
 * @(#)HpunixProcByLogFile.java     v1.01, Mar 27, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Processcollectdata;

/**
 * 
 * ClassName: HpunixProcByLogFile.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 27, 2013 5:35:28 PM
 */
public class HpunixProcByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_PROC_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_PROC_BEGIN_KEYWORD;

	private static final String HPUNIX_PROC_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_PROC_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_PROC_BEGIN_KEYWORD;
		String endStr = HPUNIX_PROC_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String procContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();

			Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
			Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
			Hashtable<String, Processcollectdata> processCountHash = new Hashtable<String, Processcollectdata>();
			String[] processLineArr = procContent.split("\n");

			for (int i = 1; i < processLineArr.length; i++) {
				String[] tmpData = processLineArr[i].trim().split("\\s++");
				if ((tmpData != null) && (tmpData.length >= 8)) {
					if ("UID".equalsIgnoreCase(tmpData[0])) {
						// 标题行
						continue;
					}

					String[] procData = getProcess(processLineArr[i].trim());
					// 0:UID 1:PID 2:PPID 3:C 4:STIME 5:TTY 6:TIME 7:COMMAND
					String uid = procData[3]; // UID
					String pid = procData[0]; // PID
					// String ppid = null; // PPID
					// String c = null; // C
					// String stime = null; // STIME
					// String tty = null; // TTY
					String time = procData[2]; // TIME
					String cmd = procData[1]; // COMMAND
					String type = "应用程序";
					String STAT = "正在运行";

					Processcollectdata processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("MemoryUtilization");
					processdata.setSubentity(pid);
					processdata.setRestype("dynamic");
					processdata.setUnit("%");
					processdata.setThevalue("");
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Memory");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit("K");
					processdata.setThevalue(""); // 未采集到内存
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Type");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(type);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Status");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(STAT);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Name");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(cmd);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("USER");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(uid);
					processVector.addElement(processdata);

					// TIME 时间统一转成秒
					String[] timestrs = time.split(":"); // 753:05
					Integer minute = Integer.parseInt(timestrs[0]);
					Integer second = Integer.parseInt(timestrs[1]);
					String cpuhour = minute / 60 >= 10 ? String
							.valueOf(minute / 60) : "0" + minute / 60; // 小时数
					String cpuminute = minute % 60 >= 10 ? String
							.valueOf(minute % 60) : "0" + minute % 60;// 分钟数
					String cpusecond = String.valueOf(second);
					String cputime = cpuhour + ":" + cpuminute + ":"
							+ cpusecond;

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("CpuTime");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit("秒");
					processdata.setThevalue(String.valueOf(cputime));
					processVector.addElement(processdata);

					// 添加关键进程
					processdata = processCountHash.get(cmd);
					if (processdata != null) {
						Integer count = Integer.parseInt(processdata
								.getThevalue());
						count++;
						processdata.setThevalue(String.valueOf(count));
						processCountHash.put(cmd, processdata);
					} else {
						processdata = new Processcollectdata();
						processdata.setIpaddress(ipaddress);
						processdata.setCollecttime(date);
						processdata.setCategory("Process");
						processdata.setEntity("Num");
						processdata.setSubentity(pid);
						processdata.setRestype("dynamic");
						processdata.setUnit("个");
						processdata.setThevalue("1");
						processdata.setChname(cmd);
						processCountHash.put(cmd, processdata);
					}
				}
			}
			returnHash.put("processVector", processVector);
			returnHash.put("processCountHash", processCountHash);
			objectValue.setObjectValue(returnHash);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

	private String[] getProcess(String cpuLineArr) {
		String[] procTemp = cpuLineArr.trim().split("\\s++");
		// 0:UID 1:PID 2:PPID 3:C 4:STIME 5:TTY 6:TIME 7:CMD
		String[] _procdata = new String[4];
		if (procTemp.length == 9) {
			_procdata[0] = procTemp[1];
			if (procTemp[7].indexOf(":") >= 0) {
				_procdata[1] = procTemp[8];// CMD
				_procdata[2] = procTemp[7];// TIME

			} else {
				_procdata[1] = procTemp[7] + " " + procTemp[8];// CMD
				_procdata[2] = procTemp[6];// TIME
			}
			_procdata[3] = procTemp[0];
		} else if (procTemp.length > 9) {
			_procdata[0] = procTemp[1];
			String cmdstr = "";
			if (procTemp[4].indexOf(":") >= 0) {
				_procdata[2] = procTemp[6];// TIME
				for (int k = 7; k < procTemp.length - 1; k++) {
					cmdstr = cmdstr + " " + procTemp[k];
				}
				_procdata[1] = cmdstr.trim();// CMD
			} else {
				_procdata[2] = procTemp[7];// TIME
				for (int k = 8; k < procTemp.length - 1; k++) {
					cmdstr = cmdstr + " " + procTemp[k];
				}
				_procdata[1] = cmdstr.trim();// CMD
			}
			_procdata[3] = procTemp[0];
		} else if (procTemp.length == 8) {
			_procdata[0] = procTemp[1];// PID
			String cmdStr = procTemp[7];// CMD

			if (cmdStr.indexOf("<") >= 0) {
				cmdStr = cmdStr.replaceAll("<", "");
			}
			if (cmdStr.indexOf(">") >= 0) {
				cmdStr = cmdStr.replaceAll(">", "");
			}
			_procdata[1] = cmdStr;

			_procdata[2] = procTemp[6];// TIME
			_procdata[3] = procTemp[0];
		}
		return _procdata;
	}

}
