/*
 * @(#)HpunixUptimeByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Systemcollectdata;

/**
 * 
 * ClassName: HpunixUptimeByLogFile.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 4:58:14 PM
 */
public class HpunixUptimeByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_UPTIME_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_UPTIME_BEGIN_KEYWORD;

	private static final String HPUNIX_UPTIME_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_UPTIME_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_UPTIME_BEGIN_KEYWORD;
		String endStr = HPUNIX_UPTIME_END_KEYWORD;
		
		ObjectValue objectValue = new ObjectValue();
		try {
			String uptimeContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			
			if (uptimeContent == null) {
				uptimeContent = "";
			}

			Systemcollectdata systemdata = new Systemcollectdata();
			systemdata.setIpaddress(ipaddress);
			systemdata.setCollecttime(date);
			systemdata.setCategory("System");
			systemdata.setEntity("SysUptime");
			systemdata.setSubentity("SysUptime");
			systemdata.setRestype("static");
			systemdata.setUnit(" ");
			systemdata.setThevalue(uptimeContent);

			objectValue.setObjectValue(systemdata);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}
