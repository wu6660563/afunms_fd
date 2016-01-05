/*
 * @(#)HpunixCpuByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;

/**
 * 
 * ClassName: Tru64CpuByLogFile.java
 * <p>该类未被使用，CPU改为从内存中读取
 *
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:41:45 PM
 */
public class Tru64CpuByLogFile extends Tru64ByLogFile {

	private static final String TRU64_CPU_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_CPU_BEGIN_KEYWORD;

	private static final String TRU64_CPU_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_CPU_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = TRU64_CPU_BEGIN_KEYWORD;
		String endStr = TRU64_CPU_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String cpuContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			
			String[] cpu_LineArr = cpuContent.split("\n");
			String[] tmpData = null;
			CPUcollectdata cpudata = null;
			Vector cpuVector = new Vector();

//			for (int i = 1; i < cpu_LineArr.length; i++) {
//				tmpData = cpu_LineArr[i].split("\\s++");
//				if ((tmpData != null)
//						&& ((tmpData.length == 5) || (tmpData.length == 6))) {
//					if (tmpData[0] != null
//							&& tmpData[0].equalsIgnoreCase("Average")) {
//						cpudata = new CPUcollectdata();
//						cpudata.setIpaddress(ipaddress);
//						cpudata.setCollecttime(date);
//						cpudata.setCategory("CPU");
//						cpudata.setEntity("Utilization");
//						cpudata.setSubentity("Utilization");
//						cpudata.setRestype("dynamic");
//						cpudata.setUnit("%");
//						cpudata.setThevalue(Float.toString(100 - Float
//								.parseFloat(tmpData[4])));
//						cpuVector.addElement(cpudata);
//						System.out
//								.println("----------------CU---------------------==="
//										+ Float.toString(100 - Float
//												.parseFloat(tmpData[4])));
//					}
//				}
//			}
			
			boolean flag = false;
			for (int i = 2; i < cpu_LineArr.length; i++) {
				tmpData = cpu_LineArr[i].trim().split("\\s++");
				if(i == 2 && tmpData[17] != null && "id".equals(tmpData[17])) {
					flag = true;
				}
				if(i == 3 && tmpData[17] != null && flag == true) {
					cpudata = new CPUcollectdata();
					cpudata.setIpaddress(ipaddress);
					cpudata.setCollecttime(date);
					cpudata.setCategory("CPU");
					cpudata.setEntity("Utilization");
					cpudata.setSubentity("Utilization");
					cpudata.setRestype("dynamic");
					cpudata.setUnit("%");
					cpudata.setThevalue(Float.toString(100 - Float
							.parseFloat(tmpData[17])));
					cpuVector.addElement(cpudata);
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
