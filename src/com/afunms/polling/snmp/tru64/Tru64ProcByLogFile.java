/*
 * @(#)Tru64ProcByLogFile.java     v1.01, Mar 27, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;

/**
 * 
 * ClassName: Tru64ProcByLogFile.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 27, 2013 5:35:28 PM
 */
public class Tru64ProcByLogFile extends Tru64ByLogFile {

	private static SysLogger logger = SysLogger
			.getLogger(Tru64ProcByLogFile.class.getName());

	private static final String TRU64_PROC_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_PROC_BEGIN_KEYWORD;

	private static final String TRU64_PROC_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_PROC_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = TRU64_PROC_BEGIN_KEYWORD;
		String endStr = TRU64_PROC_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();

		String procContent = getLogFileContent(beginStr, endStr);
		String ipaddress = getNodeDTO().getIpaddress();
		Calendar date = getCalendarInstance();

		Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
		Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
		Hashtable<String, Processcollectdata> processCountHash = new Hashtable<String, Processcollectdata>();
		String[] processLineArr = procContent.split("\n");

		// 进程物理内存利用率
		Float processMemUtil = 0f;
		// 进程CPU利用率
		Float processcpuUtil = 0f;
		Float vSizeTotal = 0f;
		Float rSizeTotal = 0f;

		int startedStart = 0;
		int timeEnd = 0;
		int cmdStart = 0;

		Vector cpuVector = new Vector();
		Vector memoryVector = new Vector();

		try {
			startedStart = processLineArr[0].indexOf("STARTED");
			timeEnd = processLineArr[0].indexOf("TIME");
			cmdStart = processLineArr[0].indexOf("COMMAND");
			for (int i = 1; i < processLineArr.length; i++) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				int cmdStart_ = cmdStart;

				String tempStr1 = processLineArr[i].substring(0, cmdStart_)
						.trim(); // 去掉COMMAND
//				hash.put("COMMAND", processLineArr[i].substring(cmdStart_).trim());
				String cmd = processLineArr[i].substring(cmdStart_).trim();
				String tempStr2 = tempStr1.substring(0,
						tempStr1.lastIndexOf(" ")).trim(); // 去掉TIME
//				hash.put("TIME", tempStr1.substring(tempStr1.lastIndexOf(" ")).trim());
				String time = tempStr1.substring(tempStr1.lastIndexOf(" ")).trim();
				String tempStr3 = tempStr2.substring(0, startedStart).trim(); // 去掉STARTED
//				hash.put("STARTED", tempStr2.substring(startedStart).trim());
				String started = tempStr2.substring(startedStart).trim();
				
				// 去掉STARTED之后，可以直接分割
				String[] tempstrs = tempStr3.split("\\s++");
//				hash.put("TTY", tempstrs[6]);
//				hash.put("RSS", tempstrs[5]);
//				hash.put("VSZ", tempstrs[4]);
//				hash.put("MEM", tempstrs[3]);
//				hash.put("CPU", tempstrs[2]);
//				hash.put("PID", tempstrs[1]);
//				hash.put("USER", tempstrs[0]);
				
				String tty = tempstrs[6];
				String rss = tempstrs[5];
				String vsz = tempstrs[4];
				String mem = tempstrs[3];
				String cpu = tempstrs[2];
				String pid = tempstrs[1];
				String uid = tempstrs[0];

				// USER PID %CPU %MEM VSZ RSS TTY S STARTED TIME COMMAND
//				String uid = hash.get("USER"); // USER
//				String pid = hash.get("PID"); // PID
//				String cpu = hash.get("CPU"); // CPU
//				String mem = hash.get("MEM"); // MEM
//				String vsz = hash.get("VSZ"); // VSZ
//				String rss = hash.get("RSS"); // RSS
//				String time = hash.get("TIME"); // TIME
//				String cmd = hash.get("COMMAND"); // COMMAND
//				String started = hash.get("STARTED"); // STARTED
				String type = "应用程序";
				String STAT = "正在运行";

//				SysLogger.info(uid + "===" + pid + "===" + cpu + "==="
//						+ mem + "====" + vsz + "===" + rss + "=====" + started
//						+ "=====" + time + "=====" + cmd);

				processMemUtil += Float.parseFloat(mem);
				processcpuUtil += Float.parseFloat(cpu);
				vSizeTotal += toFloatSize(vsz);
				rSizeTotal += toFloatSize(rss);

				Processcollectdata processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("MemoryUtilization");
				processdata.setSubentity(pid);
				processdata.setRestype("dynamic");
				processdata.setUnit("%");
				processdata.setThevalue(String.valueOf(Float.parseFloat(mem)));
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("Memory");
				processdata.setSubentity(pid);
				processdata.setRestype("static");
				processdata.setUnit("K");
				processdata.setThevalue(String.valueOf(toFloatSize(rss)));
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
				processdata.setEntity("StartTime");
				processdata.setSubentity(pid);
				processdata.setRestype("static");
				processdata.setUnit(" ");
				processdata.setThevalue(started);
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

				// 处理时间
				String[] timestrs = time.split("-"); // 1-13:19:30
				Integer minute = 0;
				Float second = 0f;
				Integer cpuhour = 0;
				String cputime = "0";
				if (timestrs.length == 2) { // 1-13:19:30
					try {
						String[] temps = timestrs[1].split(":");
						cpuhour = cpuhour + Integer.parseInt(timestrs[0]) * 24
								+ Integer.parseInt(temps[0]);
						minute = minute + Integer.parseInt(temps[1]);
						second = second + Float.parseFloat(temps[2]);
						cputime = cpuhour + ":" + minute + ":" + second;
					} catch (Exception e) {
						logger.error("TRU64PROC-->CHANGETIME-->ipaddress:" + getNodeDTO().getIpaddress(), e);
					}
				} else { // 01:31:28 0:00.29
					cputime = timestrs[0];
				}

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
					Integer count = Integer.parseInt(processdata.getThevalue());
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

			// 内存、CPU保留两位小数
			processMemUtil = (float) (Math.round(processMemUtil * 100)) / 100;
			processcpuUtil = (float) (Math.round(processcpuUtil * 100)) / 100;
			vSizeTotal = (float) (Math.round(vSizeTotal * 100)) / 100;
			rSizeTotal = (float) (Math.round(rSizeTotal * 100)) / 100;
			// 物理内存总量
			Float allPhysicalMemory = rSizeTotal / processMemUtil * 100;
			allPhysicalMemory = (float) (Math.round(allPhysicalMemory * 100)) / 100;
			// 虚拟内存暂未总量

			// CPU、内存利用率、物理内存等

			CPUcollectdata cpudata = new CPUcollectdata();
			cpudata.setIpaddress(ipaddress);
			cpudata.setCollecttime(date);
			cpudata.setCategory("CPU");
			cpudata.setEntity("Utilization");
			cpudata.setSubentity("Utilization");
			cpudata.setRestype("dynamic");
			cpudata.setUnit("%");
			cpudata.setThevalue(Float.toString(processcpuUtil));
			cpuVector.addElement(cpudata);

			Memorycollectdata memorydata = new Memorycollectdata();

			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Float.toString(allPhysicalMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsedSize");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");// (allPhysicalMemory-freePhysicalMemory)
			memorydata.setThevalue(Float.toString(rSizeTotal));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(Float.toString(processMemUtil));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Integer.toString(0));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsdeSize");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Float.toString(vSizeTotal));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue("0");
			memoryVector.addElement(memorydata);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"TRU64PROC-->ipaddress:" + getNodeDTO().getIpaddress(), e);
		}
		returnHash.put("cpuVector", cpuVector);
//		returnHash.put("memoryVector", memoryVector);
		returnHash.put("processVector", processVector);
		returnHash.put("processCountHash", processCountHash);
		objectValue.setObjectValue(returnHash);
		objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		return objectValue;
	}

	private Hashtable<String, String> getProcess(String processLineArr,
			int startedStart, int timeEnd, int cmdStart) {
		// 前7都是一个标题对应一个值，后面的不一样
		Hashtable<String, String> hash = new Hashtable<String, String>();
		String tempStr1 = processLineArr.substring(0, cmdStart - 1); // 去掉COMMAND
		hash.put("COMMAND", processLineArr.substring(cmdStart).trim());
		String tempStr2 = tempStr1.substring(0, tempStr1.lastIndexOf(" "))
				.trim(); // 去掉TIME
		hash.put("TIME", tempStr1.substring(tempStr1.lastIndexOf(" ") + 1,
				timeEnd).trim());
		String tempStr3 = tempStr2.substring(0, startedStart).trim(); // 去掉STARTED
		hash.put("STARTED", tempStr2.substring(startedStart));

		// 去掉STARTED之后，可以直接分割
		String[] tempstrs = tempStr3.split("\\s++");
		hash.put("S", tempstrs[7]);
		hash.put("TT", tempstrs[6]);
		hash.put("RSS", tempstrs[5]);
		hash.put("VSZ", tempstrs[4]);
		hash.put("MEM", tempstrs[3]);
		hash.put("CPU", tempstrs[2]);
		hash.put("PID", tempstrs[1]);
		hash.put("USER", tempstrs[0]);
		return hash;
	}

	public static void test(String content) {
		// String content = loadFileContent();
		List<Hashtable> list = new ArrayList<Hashtable>();
		String[] processLineArr = content.split("\n");

		int startedStart = 0;
		int timeEnd = 0;
		int cmdStart = 0;

		startedStart = processLineArr[0].indexOf("STARTED");
		timeEnd = processLineArr[0].indexOf("TIME"); // 因为下面的含有两个中文，而一个中文占用两个占位符，而里面的内容含有两个中文，原本为+4，现为+2
		cmdStart = processLineArr[0].indexOf("COMMAND"); // 因为下面的含有两个中文，而一个中文占用两个占位符，而里面的内容含有两个中文，故需要减二
		for (int i = 1; i < processLineArr.length; i++) {
			Hashtable<String, String> hash = new Hashtable<String, String>();

			int fullNumber = getFullNumber(processLineArr[i]);
			int cmdStart_ = cmdStart;
			cmdStart_ = cmdStart_ - fullNumber;

			String tempStr1 = processLineArr[i].substring(0, cmdStart_).trim(); // 去掉COMMAND
			hash.put("COMMAND", processLineArr[i].substring(cmdStart_).trim());
			String tempStr2 = tempStr1.substring(0, tempStr1.lastIndexOf(" "))
					.trim(); // 去掉TIME
			hash.put("TIME", tempStr1.substring(tempStr1.lastIndexOf(" "))
					.trim());
			String tempStr3 = tempStr2.substring(0, startedStart).trim(); // 去掉STARTED
			hash.put("STARTED", tempStr2.substring(startedStart).trim());
			// 去掉STARTED之后，可以直接分割

			String[] tempstrs = tempStr3.split("\\s++");
			hash.put("TTY", tempstrs[6]);
			hash.put("RSS", tempstrs[5]);
			hash.put("VSZ", tempstrs[4]);
			hash.put("MEM", tempstrs[3]);
			hash.put("CPU", tempstrs[2]);
			hash.put("PID", tempstrs[1]);
			hash.put("USER", tempstrs[0]);
			list.add(hash);
		}

		for (int i = 0; i < list.size(); i++) {
			Hashtable<String, String> hash = list.get(i);
			System.out.println("USER:" + hash.get("USER") + "    PID:"
					+ hash.get("PID") + "    CPU:" + hash.get("CPU")
					+ "    MEM:" + hash.get("MEM") + "    VSZ:"
					+ hash.get("VSZ") + "    RSS:" + hash.get("RSS") + "    "
					+ "TTY:" + hash.get("TTY") + "    STARTED:"
					+ hash.get("STARTED") + "    TIME:" + hash.get("TIME")
					+ "    COMMAND:" + hash.get("COMMAND"));
		}

	}

	private Float toFloatSize(String memSize) {
		Float tempSize = 0f;
		if (memSize.endsWith("G")) {
			tempSize = Float
					.valueOf(memSize.substring(0, memSize.indexOf("G"))) * 1024;
		} else if (memSize.endsWith("M")) {
			tempSize = Float
					.valueOf(memSize.substring(0, memSize.indexOf("M")));
		} else if (memSize.endsWith("K")) {
			tempSize = Float
					.valueOf(memSize.substring(0, memSize.indexOf("K")));
			if ("0".equals(tempSize)) {
				tempSize = 0f;
			} else {
				tempSize = tempSize / 1024;
			}
		}
		tempSize = (float) (Math.round(tempSize * 100)) / 100;
		return tempSize;
	}

	// 判断字符串中含有中文个数
	public static int getFullNumber(String str) {
		String temp = null;
		Pattern p = Pattern.compile("[\u4E00-\u9FA5]+");
		Matcher m = p.matcher(str);
		int count = 0;
		while (m.find()) {
			temp = m.group(0);
			count += temp.length();
		}
		return count;
	}

	// public static void main(String[] args) {
	// String content = "USER PID %CPU %MEM VSZ RSS TTY S STARTED TIME
	// COMMAND\n"
	// + "ems 456279 8.1 5.4 322M 110M ?? R N 4月19日 1-20:05:33 SCADA_Rep -mode
	// r\n"
	// + "ems 457030 5.8 1.5 414M 31M ?? R N 4月19日 12:48:09 AVCServer -mode r
	// -rep\n"
	// + "ems 120690 2.4 3.5 337M 142M ?? R N Aug 19 10:51:18 scada_misc\n"
	// + "root 93381 1.7 0.5 55.0M 21M ?? S < Mar 09 3-03:29:52
	// /df8003/bin/sysmd\n"
	// + "ems 94840 1.5 3.6 326M 146M ?? S N Jul 23 14:27:20 SCADA_Save\n"
	// + "ems 520115 1.2 0.0 2.95M 520K ?? S N Aug 28 54:10.92 ksh
	// /df8003/bat/hutlftp\n"
	// + "ems 93747 0.4 0.9 183M 35M ?? S N Mar 09 1-00:04:29 comServer\n"
	// + "root 0 0.2 3.7 5.07G 152M ?? R < Dec 05 2-08:01:00 [kernel idle]\n"
	// + "ems 141777 0.1 0.0 2.16M 176K ?? S N 16:49:59 0:00.00 sleep 3\n"
	// + "ems 224501 0.1 0.3 39.2M 14M ?? S N Aug 26 22:15.70 hisWrite\n"
	// + "root 93362 0.1 0.0 21.6M 1.6M ?? S < Mar 09 4-11:18:52
	// /df8003/bin/applogd\n"
	// + "root 141650 0.0 0.0 2.68M 224K ?? S 16:50:00 0:00.02
	// /home/nms/collection.sh\n"
	// + "ems 1698 0.0 0.0 2.99M 0K pts/3 IW + Dec 05 0:00.08 -ksh (ksh)\n"
	// + "root 141676 0.0 0.0 2.68M 216K ?? S 16:50:00 0:00.02 sh -c
	// /home/nms/collection.sh >/dev/null\n"
	// + "ems 141700 0.0 0.0 0K 0K ?? < - 0:00.00 <defunct>\n"
	// + "ems 141718 0.0 0.0 2.68M 216K ?? S N 16:50:00 0:00.01 sh -c
	// /df8003/bat/hutlcron\n"
	// + "root 141771 0.0 0.2 11.8M 7.8M ?? R 16:50:00 0:00.07 ps aux\n"
	// + "ems 141803 0.0 0.0 2.88M 440K ?? S N 16:50:00 0:00.03
	// /df8003/bat/hutlcron\n"
	// + "ems 295879 0.0 0.0 313M 1.8M ?? I N Apr 22 0:02.44 Recalc\n"
	// + "ems 291694 0.0 1.7 105M 71M ?? S N 20:53:27 0:02.96 rdbmon\n"
	// + "ems 1697 0.0 0.0 2.80M 0K pts/2 IW + Dec 05 0:00.01 /bin/ksh\n"
	// + "ems 1696 0.0 0.0 2.94M 0K pts/1 IW + Dec 05 0:00.07 -ksh (ksh)\n"
	// + "ems 1681 0.0 0.0 11.8M 0K ?? IW Dec 05 0:00.13 /usr/dt/bin/dtterm
	// -session dtrenvLi -ls\n"
	// + "ems 1680 0.0 0.0 11.8M 0K ?? IW Dec 05 1:35.80 /usr/dt/bin/dtterm
	// -session dtO2LUWX -ls\n"
	// + "ems 1679 0.0 0.0 12.7M 0K ?? IW Dec 05 0:00.15 dtterm -session
	// dtLaOTnt -sl 10000\n"
	// + "root 1668 0.0 0.0 7.29M 0K ?? IW Dec 05 0:00.07 rpc.ttdbserverd\n"
	// + "ems 1676 0.0 0.0 14.0M 2.0M ?? S Dec 05 5:29.84 dtwm\n"
	// + "root 1678 0.0 0.0 8.22M 568K ?? I Dec 05 0:05.60
	// /usr/bin/X11/dxconsole\n"
	// + "ems 1667 0.0 0.0 7.12M 0K ?? IW Dec 05 0:00.03 /usr/dt/bin/ttsession
	// -s\n"
	// + "root 1077 0.0 0.0 29.4M 1.0M ?? I Dec 05 5:35.67 /usr/bin/X11/X :0
	// -auth /var/dt/authdir/authfiles/A:0-hnCIZt\n"
	// + "root 1079 0.0 0.0 5.33M 0K ?? IW Dec 05 0:00.02 dtlogin <:0>
	// -daemon\n"
	// + "root 1081 0.0 0.1 16.5M 2.9M ?? S Dec 05 2:17.16 /usr/sbin/smsd -d\n"
	// + "root 1083 0.0 0.0 584K 0K console IW + Dec 05 0:00.03 /usr/sbin/getty
	// console console vt100\n"
	// + "root 1107 0.0 0.0 6.50M 552K ?? S Dec 05 1:18.04 sendmail: accept -bd
	// -q15m -om\n"
	// + "ems 1601 0.0 0.0 11.4M 0K ?? IW Dec 05 0:00.20
	// /usr/dt/bin/dtsession\n"
	// + "ems 1626 0.0 0.0 14.6M 0K ?? IW Dec 05 0:00.31 /usr/bin/X11/dxhanziim
	// -xrm *fontList: -dt-interface
	// system-medium-r-normal-m-*-*-*-*-*-*-*-*:\n"
	// + "root 1051 0.0 0.2 14.0M 6.2M ?? S Dec 05 0:44.05
	// /usr/opt/java131/bin/../bin/alpha/native_threads/java -classic -mx2m
	// -Dos.version=boot authentication.server.AuthenticationServer\n"
	// + "root 861 0.0 0.0 4.59M 864K ?? S Dec 05 0:30.56
	// /usr/sbin/config_hmmod\n"
	// + "root 834 0.0 0.0 3.06M 200K ?? S Dec 05 0:09.12
	// /usr/sbin/cpqthresh_mib\n"
	// + "root 725 0.0 0.0 4.93M 736K ?? S Dec 05 0:39.67 /usr/sbin/cpq_mibs\n"
	// + "root 723 0.0 0.0 4.48M 160K ?? S < Dec 05 0:00.56 /usr/sbin/pmgrd\n"
	// + "root 720 0.0 0.0 4.09M 160K ?? S Dec 05 0:40.15 /usr/sbin/os_mibs\n"
	// + "root 711 0.0 0.0 3.12M 48K ?? I Dec 05 0:02.44
	// /usr/sbin/svrSystem_mib\n"
	// + "root 863 0.0 0.0 4.36M 712K ?? S Dec 05 1:01.05
	// /usr/sbin/sysman_hmmod\n"
	// + "root 865 0.0 0.0 8.45M 2.0M ?? S Dec 05 1:39.96 /usr/sbin/insightd\n"
	// + "root 866 0.0 0.0 6.59M 568K ?? I Dec 05 01:53:53 /usr/sbin/envmond -ui
	// cli\n"
	// + "root 908 0.0 0.0 5.89M 960K ?? S < Dec 05 0:08.39 /usr/sbin/advfsd\n"
	// + "root 919 0.0 0.0 2.33M 0K ?? IW Dec 05 0:00.00 /usr/sbin/inetd\n"
	// + "root 920 0.0 0.0 2.45M 80K ?? I Dec 05 0:06.48 -child (inetd)\n"
	// + "root 931 0.0 0.0 3.93M 216K ?? S Dec 05 2:53.83 /usr/sbin/cron\n"
	// + "root 956 0.0 0.0 3.19M 0K ?? IW Dec 05 0:00.01 /usr/lbin/lpd\n"
	// + "root 975 0.0 0.0 2.20M 0K ?? IW Dec 05 0:00.01 /usr/sbin/utxd\n"
	// + "root 993 0.0 0.0 5.34M 232K ?? I Dec 05 10:41.95 /usr/dt/bin/dtlogin
	// -daemon\n"
	// + "root 710 0.0 0.0 3.11M 144K ?? S Dec 05 0:07.84
	// /usr/sbin/svrMgt_mib\n"
	// + "root 60 0.0 0.0 2.16M 56K ?? S Dec 05 0:01.06 /sbin/update\n"
	// + "root 49 0.0 0.0 2.93M 0K ?? IW Dec 05 0:00.04 /usr/sbin/esmd\n"
	// + "root 5 0.0 0.0 3.11M 88K ?? S Dec 05 0:02.86 /sbin/hotswapd\n"
	// + "root 3 0.0 0.0 1.70M 680K ?? I Dec 05 0:00.24 /sbin/kloadsrv\n"
	// + "root 1 0.0 0.0 608K 40K ?? SL Dec 05 17:39.96 /sbin/init -a\n"
	// + "root 188 0.0 0.0 4.02M 632K ?? I Dec 05 1:38.45 /usr/sbin/evmd\n"
	// + "root 229 0.0 0.0 3.38M 296K ?? I Dec 05 0:27.24 /usr/sbin/evmlogger -o
	// /var/run/evmlogger.info -l /var/evm/adm/logfiles/evmlogger.log\n"
	// + "root 230 0.0 0.0 2.86M 192K ?? I Dec 05 0:06.10 /usr/sbin/evmchmgr -l
	// /var/evm/adm/logfiles/evmchmgr.log\n"
	// + "ems 510458 0.0 1.8 124M 72M ?? S N Aug 07 0:06.36 limit\n"
	// + "root 116053 0.0 2.3 376M 94M ?? S N Aug 26 1:40.91 ssrd\n"
	// + "ems 502123 0.0 1.7 330M 68M ?? S N Jul 04 1:36.64 hisDel\n"
	// + "ems 101996 0.0 0.2 41.6M 7.2M ?? S N Jul 23 1:05.48 almd_server\n"
	// + "ems 363518 0.0 3.3 618M 136M ?? S N Aug 28 39:09.38 pasServer -mode r
	// -rep\n"
	// + "ems 94933 0.0 2.7 324M 111M ?? S N Jul 23 17:21.68 CTRL_Server -mode
	// r\n"
	// + "root 357 0.0 0.0 2.82M 112K ?? I Dec 05 0:05.60 /usr/sbin/niffd\n"
	// + "root 93818 0.0 35.3 1.88G 1.4G ?? R N Mar 09 04:35:54 netmond -s
	// 3600\n"
	// + "ems 93798 0.0 0.0 30.5M 1.3M ?? IWN Mar 09 0:00.16 hisStat\n"
	// + "ems 93781 0.0 0.8 338M 34M ?? S N Mar 09 0:07.53 alm_save -mode r\n"
	// + "ems 93772 0.0 7.7 1.02G 317M ?? S N Mar 09 0:10.04 RECUR_Server\n"
	// + "root 391 0.0 0.0 2.94M 224K ?? I Dec 05 0:05.27 /usr/sbin/syslogd
	// -e\n"
	// + "root 395 0.0 0.0 4.00M 160K ?? I Dec 05 0:07.30 /usr/sbin/binlogd\n"
	// + "ems 93684 0.0 0.8 322M 31M ?? S N Mar 09 8:30.61 SSM_Server -dts\n"
	// + "ems 93671 0.0 1.2 336M 49M ?? S N Mar 09 53:28.99 SCADA_Server -mode
	// s1\n"
	// + "root 93647 0.0 0.7 88.1M 29M ?? S N Mar 09 2:01.57
	// /df8003/bin/procmd\n"
	// + "root 93643 0.0 0.0 87.6M 0K ?? IWN Mar 09 0:00.40 proc_watchd\n"
	// + "root 481 0.0 0.0 2.79M 104K ?? I Dec 05 0:00.03 /usr/sbin/portmap\n"
	// + "root 484 0.0 0.0 2.27M 0K ?? I Dec 05 0:00.25 /usr/sbin/nfsiod 7\n"
	// + "root 93371 0.0 0.0 21.2M 952K ?? S < Mar 09 02:42:50
	// /df8003/bin/netmd\n"
	// + "root 666 0.0 0.0 3.38M 256K ?? S Dec 05 0:52.74 /usr/sbin/xntpd -g -c
	// /etc/ntp.conf\n"
	// + "root 93260 0.0 0.0 23.2M 656K ?? I N Mar 09 0:01.22
	// /df8003/bin/dbmd\n"
	// + "ems 205444 0.0 2.8 656M 114M ?? S N Jun 24 5:01.76 gapClient\n"
	// + "root 701 0.0 0.0 2.59M 176K ?? S Dec 05 0:20.40 /usr/sbin/snmpd\n";
	// test(content);
	// }

}
