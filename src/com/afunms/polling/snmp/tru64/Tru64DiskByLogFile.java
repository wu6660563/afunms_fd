/*
 * @(#)HpunixDiskByLogFile.java     v1.01, Mar 29, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Diskcollectdata;
/**
 * 
 * ClassName:   Tru64DiskByLogFile.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 29, 2013 9:23:07 AM
 */
public class Tru64DiskByLogFile extends Tru64ByLogFile {

	private static final String TRU64_DISK_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_DISK_BEGIN_KEYWORD;

	private static final String TRU64_DISK_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_DISK_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = TRU64_DISK_BEGIN_KEYWORD;
		String endStr = TRU64_DISK_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String diskContent = getLogFileContent(beginStr, endStr);
			String[] diskLineArr = diskContent.trim().split("\n");
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			
			Diskcollectdata diskdata = null;
			Vector diskVector = new Vector();
			for (int i = 1; i < diskLineArr.length; i++) {
				//i从1开始，第一行为title
				String[] tmpData = diskLineArr[i].split("\\s++");
				//FileSystem TotalSize UsedSize FreeSize Utilization Path
				String Utilization = tmpData[4].substring(0, tmpData[4].lastIndexOf("%"));
				String path = tmpData[5];
				
				int allblocksize = 0;
				try {
					allblocksize = Integer.parseInt(tmpData[1]);
					if(allblocksize == 0){
						Utilization = "0";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("Utilization");// 利用百分比
				diskdata.setSubentity(path);	//path
				diskdata.setRestype("static");
				diskdata.setUnit("%");
				diskdata.setThevalue(Utilization);
				diskVector.addElement(diskdata);

				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("AllSize");// 总空间
				diskdata.setSubentity(path);
				diskdata.setRestype("static");
				
				float allsize = 0.0f;
				allsize = allblocksize * 1.0f / 1024;
				if (allsize >= 1024.0f) {
					allsize = allsize / 1024;
					diskdata.setUnit("G");
				} else {
					diskdata.setUnit("M");
				}
				diskdata.setThevalue(Float.toString(allsize));
				diskVector.addElement(diskdata);
				
				String diskinc = "0.0";
				float pastutil = 0.0f;
				Vector disk_v = (Vector)ipAllData.get("disk");
				if (disk_v != null && disk_v.size() > 0) {
					for (int si = 0; si < disk_v.size(); si++) {
						Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
						if((path).equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
							pastutil = Float.parseFloat(disk_data.getThevalue());
						}
					}
				} else {
					pastutil = Float.parseFloat(Utilization);
				}
				if (pastutil == 0) {
					pastutil = Float.parseFloat(Utilization);
				}
				if(Float.parseFloat(Utilization)-pastutil>0){
					diskinc = (Float
							.parseFloat(Utilization)-pastutil)+"";
				}
				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("UtilizationInc");// 利用增长率百分比
				diskdata.setSubentity(path);
				diskdata.setRestype("dynamic");
				diskdata.setUnit("%");
				diskdata.setThevalue(diskinc);
				diskVector.addElement(diskdata);
				
				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("UsedSize");// 使用大小
				diskdata.setSubentity(path);
				diskdata.setRestype("static");
				int UsedintSize = 0;
				UsedintSize = Integer.parseInt(tmpData[2]);
				float usedfloatsize = 0.0f;
				usedfloatsize = UsedintSize * 1.0f / 1024;
				if (usedfloatsize >= 1024.0f) {
					usedfloatsize = usedfloatsize / 1024;
					diskdata.setUnit("G");
				} else {
					diskdata.setUnit("M");
				}
				diskdata.setThevalue(Float.toString(usedfloatsize));
				diskVector.addElement(diskdata);
			}
			
			objectValue.setObjectValue(diskVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}
