/*
 * Created on 2005-4-14
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.api.I_HostCollectDataHour;
import com.afunms.polling.impl.HostCollectDataHourManager;



/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HostCollectHourTask extends MonitorTask {

	/**
	 * 
	 */
	public HostCollectHourTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		SysLogger.info("######��ʼִ��Сʱ�鵵����######");
		I_HostCollectDataHour hostdataManager = new HostCollectDataHourManager();

		try {
			hostdataManager.schemeTask();
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.info(e.getMessage());
		} finally {
			SysLogger.info("********HostCollectHourTask Thread Count : "+Thread.activeCount());
		}
		SysLogger.info("######����ִ��Сʱ�鵵����######");
	}

}
