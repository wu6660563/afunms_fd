/*
 * @(#)HpunixDateByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.Calendar;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Systemcollectdata;
/**
 * 
 * ClassName:   HpunixDateByLogFile.java
 * <p>
 *
 * @author      ÎâÆ·Áú
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 26, 2013 5:38:58 PM
 */
public class HpunixDateByLogFile  extends HpunixByLogFile {
	
	private static final String HPUNIX_DATE_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_DATE_BEGIN_KEYWORD;

    private static final String HPUNIX_DATE_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_DATE_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
        String beginStr = HPUNIX_DATE_BEGIN_KEYWORD;
        String endStr = HPUNIX_DATE_END_KEYWORD;
        ObjectValue objectValue = new ObjectValue();
        
        try {
        	String dateContent = getLogFileContent(beginStr, endStr);
            String ipaddress = getNodeDTO().getIpaddress();
            Calendar date = getCalendarInstance();

            String dateString = "";
            if (dateContent != null && dateContent.length() > 0) {
                dateString = dateContent.trim();
            }

            Systemcollectdata systemdata = new Systemcollectdata();
            systemdata.setIpaddress(ipaddress);
            systemdata.setCollecttime(date);
            systemdata.setCategory("System");
            systemdata.setEntity("Systime");
            systemdata.setSubentity("Systime");
            systemdata.setRestype("static");
            systemdata.setUnit(" ");
            systemdata.setThevalue(dateString);

            objectValue.setObjectValue(systemdata);
            objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return objectValue;
    }

}

