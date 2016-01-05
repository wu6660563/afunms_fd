/*
 * @(#)HpunixDiskByLogFile.java     v1.01, Mar 29, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Diskcollectdata;
/**
 * 
 * ClassName:   HpunixDiskByLogFile.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 29, 2013 9:23:07 AM
 */
public class HpunixDiskByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_DISK_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_DISK_BEGIN_KEYWORD;

	private static final String HPUNIX_DISK_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_DISK_END_KEYWORD;
	
	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_DISK_BEGIN_KEYWORD;
		String endStr = HPUNIX_DISK_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String diskContent = getLogFileContent(beginStr, endStr);
			String[] diskLineArr = diskContent.trim().split("\n");
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			
			Vector diskVector = new Vector();
			String[] tmpData1 = null;
			String[] tmpData2 = null;
			String[] tmpData3 = null;
			Diskcollectdata diskdata = null;
			String[] tmpData = null;
			int pi = 0;
			for (int i = 0; i < diskLineArr.length / 4; i++) {
				pi = i;
				try {
					tmpData = diskLineArr[pi * 4].split("\\s++");
					tmpData1 = diskLineArr[pi * 4 + 1].split("\\s++");
					tmpData2 = diskLineArr[pi * 4 + 2].split("\\s++");
					tmpData3 = diskLineArr[pi * 4 + 3].split("\\s++");

					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("Utilization");// 利用百分比
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");
					diskdata.setUnit("%");

					diskdata.setThevalue(Float.toString(Float
							.parseFloat(tmpData3[1])));
					diskVector.addElement(diskdata);

					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("AllSize");// 总空间
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");

					int allblocksize = 0;
					try {
						allblocksize = Integer.parseInt(tmpData[4]);
					} catch (Exception e) {
						try {
							allblocksize = Integer.parseInt(tmpData[3]);
						} catch (Exception ex) {

						}
					}
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

					try {
						String diskinc = "0.0";
						float pastutil = 0.0f;
						Vector disk_v = (Vector)ipAllData.get("disk");
						if (disk_v != null && disk_v.size() > 0) {
							for (int si = 0; si < disk_v.size(); si++) {
								Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
								if((tmpData[0]).equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
									pastutil = Float.parseFloat(disk_data.getThevalue());
								}
							}
						} else {
							pastutil = Float.parseFloat(tmpData3[1]);
						}
						if (pastutil == 0) {
							pastutil = Float
							.parseFloat(tmpData3[1]);
						}
						if(Float
								.parseFloat(tmpData3[1])-pastutil>0){
							diskinc = (Float
									.parseFloat(tmpData3[1])-pastutil)+"";
						}
						diskdata = new Diskcollectdata();
						diskdata.setIpaddress(ipaddress);
						diskdata.setCollecttime(date);
						diskdata.setCategory("Disk");
						diskdata.setEntity("UtilizationInc");// 利用增长率百分比
						diskdata.setSubentity(tmpData[0]);
						diskdata.setRestype("dynamic");
						diskdata.setUnit("%");
						diskdata.setThevalue(diskinc);
						diskVector.addElement(diskdata);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("UsedSize");// 使用大小
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");

					int UsedintSize = 0;
					UsedintSize = Integer.parseInt(tmpData2[1]);
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			objectValue.setObjectValue(diskVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}
