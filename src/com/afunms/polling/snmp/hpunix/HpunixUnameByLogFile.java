/*
 * @(#)HpunixUnameByLogFile.java     v1.01, Mar 27, 2013
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
 * ClassName:   HpunixUnameByLogFile.java
 * <p>
 *
 * @author      Œ‚∆∑¡˙
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 27, 2013 4:00:46 PM
 */
public class HpunixUnameByLogFile  extends HpunixByLogFile {
	
	private static final String HPUNIX_UNAME_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_UNAME_BEGIN_KEYWORD;

	private static final String HPUNIX_UNAME_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_UNAME_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_UNAME_BEGIN_KEYWORD;
		String endStr = HPUNIX_UNAME_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		
		try {
			String unameContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			Vector systemVector = new Vector();
			String[] unameLineArr = unameContent.split("\n");
			String[] uname_tmpData = null;
			
			Systemcollectdata systemdata = new Systemcollectdata();
			for (int i = 0; i < unameLineArr.length; i++) {
				uname_tmpData = unameLineArr[i].split("\\s++");
				if (uname_tmpData.length == 2) {
					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("operatSystem");
					systemdata.setSubentity("operatSystem");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(uname_tmpData[0]);
					systemVector.addElement(systemdata);

					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("SysName");
					systemdata.setSubentity("SysName");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(uname_tmpData[1]);
					systemVector.addElement(systemdata);
				}
			}
			
			objectValue.setObjectValue(systemVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}

