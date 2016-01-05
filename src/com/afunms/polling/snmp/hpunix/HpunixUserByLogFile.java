/*
 * @(#)HpunixUserByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Usercollectdata;

/**
 * 
 * ClassName: HpunixUserByLogFile.java
 * <p>
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Mar 26, 2013 9:34:17 PM
 */
public class HpunixUserByLogFile extends HpunixByLogFile {

	private static final String HPUNIX_USER_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_USER_BEGIN_KEYWORD;

	private static final String HPUNIX_USER_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_USER_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_USER_BEGIN_KEYWORD;
		String endStr = HPUNIX_USER_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		try {
			String userContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			Vector userVector = new Vector();
			Usercollectdata userdata = null;

			String[] userLineArr = userContent.split("\n");
			for (int i = 0; i < userLineArr.length; i++) {
				String[] result = userLineArr[i].trim().split(":");
				if (result.length > 0) {
					userdata = new Usercollectdata();
					userdata.setIpaddress(ipaddress);
					userdata.setCollecttime(date);
					userdata.setCategory("User");
					userdata.setEntity("Sysuser");
					userdata.setSubentity(result[0]);
					userdata.setRestype("static");
					userdata.setUnit(" ");
					userdata.setThevalue(result[0]);
					userVector.addElement(userdata);
					continue;
				}
			}
			objectValue.setObjectValue(userVector);
			objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}