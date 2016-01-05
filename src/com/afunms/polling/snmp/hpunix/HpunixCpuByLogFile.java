/*
 * @(#)HpunixCpuByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;

/**
 * 
 * ClassName: HpunixCpuByLogFile.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:41:45 PM
 */
public class HpunixCpuByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_CPU_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_CPU_BEGIN_KEYWORD;

	private static final String HPUNIX_CPU_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_CPU_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_CPU_BEGIN_KEYWORD;
		String endStr = HPUNIX_CPU_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String cpuContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();

			String[] cpu_LineArr = cpuContent.split("\n");
			String[] tmpData = null;
			CPUcollectdata cpudata = null;
			Vector cpuVector = new Vector();

			for (int i = 1; i < cpu_LineArr.length; i++) {
				tmpData = cpu_LineArr[i].split("\\s++");
				if ((tmpData != null)
						&& ((tmpData.length == 5) || (tmpData.length == 6))) {
					if (tmpData[0] != null
							&& tmpData[0].equalsIgnoreCase("Average")) {
						cpudata = new CPUcollectdata();
						cpudata.setIpaddress(ipaddress);
						cpudata.setCollecttime(date);
						cpudata.setCategory("CPU");
						cpudata.setEntity("Utilization");
						cpudata.setSubentity("Utilization");
						cpudata.setRestype("dynamic");
						cpudata.setUnit("%");
						cpudata.setThevalue(Float.toString(100 - Float
								.parseFloat(tmpData[4])));
						cpuVector.addElement(cpudata);
//						System.out
//								.println("----------------CU---------------------==="
//										+ Float.toString(100 - Float
//												.parseFloat(tmpData[4])));
					}
				}
			}
			objectValue.setObjectValue(cpuVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}
