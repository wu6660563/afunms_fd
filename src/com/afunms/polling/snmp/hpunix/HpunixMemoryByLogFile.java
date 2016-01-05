/*
 * @(#)HpunixMemoryByLogFile.java     v1.01, Mar 28, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * 
 * ClassName: HpunixMemoryByLogFile.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Mar 28, 2013 10:53:04 PM
 */

public class HpunixMemoryByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_FREEMEMORY_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_FREEMEMORY_BEGIN_KEYWORD;
	private static final String HPUNIX_FREEMEMORY_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_FREEMEMORY_END_KEYWORD;

	private static final String HPUNIX_MEMORY_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_MEMORY_BEGIN_KEYWORD;
	private static final String HPUNIX_MEMORY_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_MEMORY_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_FREEMEMORY_BEGIN_KEYWORD;
		String endStr = HPUNIX_FREEMEMORY_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String freememoryContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();

			int allPhysicalMemory = 0;
			String[] vmstat_LineArr = null;
			String[] vmstat_tmpData = null;
			int freePhysicalMemory = 0;

			vmstat_LineArr = freememoryContent.split("\n");

			for (int i = 1; i < vmstat_LineArr.length; i++) {
				vmstat_tmpData = vmstat_LineArr[i].trim().split("\\s++");
				if ((vmstat_tmpData != null && vmstat_tmpData.length == 18)) {
					if (vmstat_tmpData[0] != null
							&& !vmstat_tmpData[0].equalsIgnoreCase("r")) {
						// freeMemory
						freePhysicalMemory = Integer
								.parseInt(vmstat_tmpData[4]) * 4 / 1024;
					}
				}
			}

			beginStr = HPUNIX_MEMORY_BEGIN_KEYWORD;
			endStr = HPUNIX_MEMORY_END_KEYWORD;
			String memoryContent = getLogFileContent(beginStr, endStr);
			String[] mLineArr = memoryContent.split("\n");
			// int usedPhysicalMemory = 0;
			int allswapMemory = 0;
			int usedswapMemory = 0;
			float usedswapPerc = 0;
			String[] tmpData = null;
			Vector memoryVector = new Vector();
			Memorycollectdata memorydata = null;

			for (int i = 1; i < mLineArr.length; i++) {
				try {
					tmpData = mLineArr[i].trim().split("\\s++");
					if ((tmpData != null)) {
						if (tmpData.length > 3) {

							String memname = tmpData[0];
							if (memname.equalsIgnoreCase("Memory:"
									.toLowerCase())) {
								int oenValue = Integer.parseInt(tmpData[1]
										.substring(0, tmpData[1].length() - 1)) / 1024;
								int twoValue = Integer.parseInt(tmpData[4]
										.substring(0, tmpData[4].length() - 1)) / 1024;
								freePhysicalMemory = (Integer
										.parseInt(tmpData[7].substring(0,
												tmpData[7].length() - 1)) + Integer
										.parseInt(tmpData[4].substring(0,
												tmpData[4].length() - 1))) / 1024;
								allPhysicalMemory = oenValue
										+ freePhysicalMemory;
								// usedPhysicalMemory = oenValue + twoValue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			float PhysicalMemUtilization = 0;
			try {
				PhysicalMemUtilization = Float.parseFloat(Integer
						.toString(allPhysicalMemory - freePhysicalMemory))
						* 100
						/ Float.parseFloat(Integer.toString(allPhysicalMemory));
			} catch (Exception e) {
				e.printStackTrace();
			}

			memorydata = new Memorycollectdata();
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
			memorydata.setThevalue(Integer.toString(allPhysicalMemory
					- freePhysicalMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(Float.toString(PhysicalMemUtilization));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Integer.toString(allswapMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsdeSize");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Integer.toString(usedswapMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(Float.toString(usedswapPerc));
			memoryVector.addElement(memorydata);

			objectValue.setObjectValue(memoryVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}
