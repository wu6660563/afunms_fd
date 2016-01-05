/*
 * @(#)HpunixMemoryByLogFile.java     v1.01, Mar 28, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * 
 * ClassName: Tru64MemoryByLogFile.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 28, 2013 10:53:04 PM
 */

public class Tru64MemoryByLogFile extends Tru64ByLogFile {

	private static final String TRU64_MEMORY_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_MEMORY_BEGIN_KEYWORD;
	private static final String TRU64_MEMORY_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_MEMORY_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = TRU64_MEMORY_BEGIN_KEYWORD;
		String endStr = TRU64_MEMORY_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String freememoryContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();

			String memoryContent = getLogFileContent(beginStr, endStr);
			String[] mLineArr = memoryContent.split("\n");
			// int usedPhysicalMemory = 0;
			float allswapMemory = 0f;
			float usedswapMemory = 0f;
			float usedswapPerc = 0f;
			float allPhysicalMemory = 0f;	//所有物理内存
			float usedPhysicalMemory = 0f;	//物理内存使用量
			float PhysicalMemUtilization = 0f;	//物理内存利用率
			
			float allPages = 0f;	//总page
			float totalUsePages = 0f;	//已用总page，包含空闲managedFreePages
			float managedFreePages = 0f;
			
			String[] tmpData = null;
			Vector memoryVector = new Vector();
			Memorycollectdata memorydata = null;

			for (int i = 0; i < mLineArr.length; i++) {
				if(mLineArr[i].indexOf("Total Physical Memory") >= 0 && mLineArr[i].indexOf("Total Physical Memory Use") == -1) {
					//Total Physical Memory =  2048.00 M
					String totalSizeStr = mLineArr[i].substring(mLineArr[i].indexOf("=") + 1);
					allPhysicalMemory = Float.parseFloat(totalSizeStr.substring(0,totalSizeStr.indexOf("M")));
					
					//下一行为总pages
					String allPagesStr = mLineArr[i+1].substring(mLineArr[i+1].indexOf("=") + 1);
					allPages = Float.parseFloat(allPagesStr.substring(0,allPagesStr.indexOf("pages")));
				}
				if(mLineArr[i].indexOf("Total Physical Memory Use") >= 0){
					//Total Physical Memory Use:     261784 / 2045.19M
					String[] usedSizeStr =  mLineArr[i].split(":");
					String[] temp = usedSizeStr[1].trim().split("\\s++");
					if(temp[0] != null && !"".equals(temp[0])) {
						totalUsePages = Float.parseFloat(temp[0]);
					}
					if(temp[2] != null && !"".equals(temp[2])) {
						usedPhysicalMemory = Float.parseFloat(temp[2].substring(0, temp[2].indexOf("M")));
					}
				}
				if(mLineArr[i].indexOf("free pages") >= 0) {
					//free pages = 272220
					String[] temp = mLineArr[i].trim().split("\\s++");
					if(temp[3] != null && !"".equals(temp[3])) {
						managedFreePages = Float.parseFloat(temp[3]);
					}
				}
				
			}
			
			if(allPages!=((float)0)){
				PhysicalMemUtilization =(totalUsePages - managedFreePages) / allPages * 100;
				PhysicalMemUtilization = (float)(Math.round(PhysicalMemUtilization * 100)) / 100;
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
			memorydata.setUnit("M");
			memorydata.setThevalue(Float.toString(usedPhysicalMemory));
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
			memorydata.setThevalue(Float.toString(allswapMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsdeSize");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Float.toString(usedswapMemory));
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
