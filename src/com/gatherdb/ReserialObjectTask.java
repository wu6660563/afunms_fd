/*
 * @(#)ReserialObjectTask.java     v1.01, Jun 28, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.gatherdb;

import java.io.File;
import java.util.TimerTask;

import com.afunms.common.util.SysLogger;

/**
 * 
 * ClassName: ReserialObjectTask.java
 * <p>
 * 
 * @author Œ‚∆∑¡˙
 * @version v1.01
 * @since v1.01
 * @Date Jul 1, 2013 9:48:02 AM
 */
public class ReserialObjectTask extends TimerTask {

	@Override
	public void run() {
		try {
			String path = "E:/third-tomcat/filePath/";
			File dir = new File(path);
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					DBAttribute attribute = (DBAttribute) SerialObejctUtil
							.reserialObject(files[i].getAbsolutePath());
					if (attribute != null) {
						String className = (String) attribute
								.getAttribute("className");
						ResultToDB resultToDB = new ResultToDB();
						resultToDB.setAttribute(attribute);
						resultToDB.setResultTosql((ResultTosql) Class.forName(
								className).newInstance());
						GathersqlListManager.getInstance().addToQueue(
								resultToDB);
					}
				}
			}

		} catch (InstantiationException e) {
			SysLogger.error("ReserialObjectTask-->InstantiationException", e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			SysLogger.error("ReserialObjectTask-->IllegalAccessException", e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			SysLogger.error("ReserialObjectTask-->ClassNotFoundException", e);
			e.printStackTrace();
		}
	}

}
