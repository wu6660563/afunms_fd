/*
 * @(#)PTimeDBStatusByLogFile.java     v1.01, May 13, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.ptimedb;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Pingcollectdata;

/**
 * 
 * ClassName: PTimeDBStatusByLogFile.java
 * <p>
 * O
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date May 13, 2013 8:31:14 PM
 */
public class PTimeDBPingByLogFile extends PTimeDBByLogFileAbstract {

	private static final String PTIMEDB_STATUS_BEGIN_KEYWORD = "cmdbegin:status";

	private static final String PTIMEDB_STATUS_END_KEYWORD = "cmdend:status";

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = PTIMEDB_STATUS_BEGIN_KEYWORD;
		String endStr = PTIMEDB_STATUS_END_KEYWORD;
		ObjectValue objectValue = new ObjectValue();
		Vector<Pingcollectdata> pingVector = new Vector<Pingcollectdata>();
		
		Pingcollectdata hostdata = null;
		try {
			String cpuContent = getLogFileContent(beginStr, endStr);
			String ipaddress = getNodeDTO().getIpaddress();
			Calendar date = getCalendarInstance();
			String status = "0";	//0Õ£÷π	100‘À––

			String[] status_LineArr = cpuContent.split("\n");
			for (int i = 1; i < status_LineArr.length; i++) {
				if (status_LineArr[i].indexOf("PTimeRS Service Stauts") != -1) {
					// PTimeRS Service Stauts:[OK].
					if (status_LineArr[i].substring(
							status_LineArr[i].indexOf(":[") + 2,
							status_LineArr[i].indexOf("]")).equals("OK")) {
                        status = "100";
                        break;
					} else {
						status = "0";
					}
				} else {
					status = "0";
				}
			}
			hostdata = new Pingcollectdata();
			hostdata.setCategory("Ping");
            hostdata.setEntity("Utilization");
            hostdata.setSubentity("ConnectUtilization");
            hostdata.setRestype("dynamic");
            hostdata.setUnit("%");
            hostdata.setThevalue(status);
            hostdata.setCollecttime(date);
            pingVector.add(hostdata);
			objectValue.setObjectValue(pingVector);
            objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}
}
