/*
 * Created on 2005-3-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;


/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TaskFactory {

	/**
	 *  
	 */
	public TaskFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonitorTask getInstance(String tasktype) {
		//SysLogger.info(tasktype+"================");
		if (tasktype.equals("pingtask"))
			return new PingTask();
		if (tasktype.equals("netcollecttask"))
			return new NetCollectDataTask();
		if (tasktype.equals("hostcollecttask"))
			return new HostCollectDataTask();
		if (tasktype.equals("mqtask"))
			return new MqTask();
		if (tasktype.equals("dominoTask"))
			return new DominoTask();
		if (tasktype.equals("hostcollectdatahourtask"))
			return new HostCollectHourTask();
		if (tasktype.equals("hostcollectdatadaytask"))
			return new HostCollectDayTask();
		if (tasktype.equals("FTPTask"))
			return new FTPTask();	
		if (tasktype.equals("iistask"))
			return new IISTask();
		
		if (tasktype.equals("firewalltask"))
			return new FirewallCollectDataTask();
		if (tasktype.equals("cicstask"))
			return new CicsTask();
		if (tasktype.equals("iislogtask"))
			return new IISLogTask();
		if (tasktype.equals("checklinktask"))
			return new CheckLinkTask();
		if (tasktype.equals("updatexmltask"))
			return new UpdateXmlTask();
		if (tasktype.equals("temperatureHumidityTask"))
			return new TemperatureHumidityTask();
		if (tasktype.equals("bnodetask"))
			return new BussinessNodeTask();

		if (tasktype.equals("jbosstask"))
			return new JBossTask();
		if (tasktype.equals("dnstask"))
			return new DNSTask();
		if (tasktype.equals("sshpolltask"))
			return new SSHPollTask();
		if (tasktype.equals("apachetask"))
			return new ApacheTask();
		if (tasktype.equals("storagetask"))
			return new StorageTask();
		
		if (tasktype.equals("m1task"))
			return new M1Task();
		if (tasktype.equals("m2task"))
			return new M2Task();
		if (tasktype.equals("m3task"))
			return new M3Task();
		if (tasktype.equals("m4task"))
			return new M4Task();
		if (tasktype.equals("m5task"))
			return new M5Task();
		if (tasktype.equals("m10task"))
			return new M10Task();
		if (tasktype.equals("m30task"))
			return new M30Task();
		if (tasktype.equals("d1task"))
			return new D1Task();
		
		if (tasktype.equals("m1hosttask"))
			return new M1HostTask();
		if (tasktype.equals("m2hosttask"))
			return new M2HostTask();
		if (tasktype.equals("m3hosttask"))
			return new M3HostTask();
		if (tasktype.equals("m4hosttask"))
			return new M4HostTask();
		if (tasktype.equals("m5hosttask"))
			return new M5HostTask();
		if (tasktype.equals("m10hosttask"))
			return new M10HostTask();
		if (tasktype.equals("m30hosttask"))
			return new M30HostTask();
		if (tasktype.equals("d1hosttask"))
			return new D1HostTask();
		
		if (tasktype.equals("m5oracletask"))
			return new M5ORACLETask();
		if (tasktype.equals("m10oracletask"))
			return new M10ORACLETask();
		if (tasktype.equals("m30oracletask"))
			return new M30ORACLETask();
		
		if (tasktype.equals("m5sqlservertask"))
			return new M5SQLServerTask();
		if (tasktype.equals("m10sqlservertask"))
			return new M10SQLServerTask();
		if (tasktype.equals("m30sqlservertask"))
			return new M30SQLServerTask();
		
		if (tasktype.equals("m5sybasetask"))
			return new M5SybaseTask();
		if (tasktype.equals("m10sybasetask"))
			return new M10SybaseTask();
		if (tasktype.equals("m30sybasetask"))
			return new M30SybaseTask();
		
		if (tasktype.equals("m5informixtask"))
			return new M5InformixTask();
		if (tasktype.equals("m10informixtask"))
			return new M10InformixTask();
		if (tasktype.equals("m30informixtask"))
			return new M30InformixTask();
		
		if (tasktype.equals("m5db2task"))
			return new M5DB2Task();
		if (tasktype.equals("m10db2task"))
			return new M10DB2Task();
		if (tasktype.equals("m30db2task"))
			return new M30DB2Task();
		
		if (tasktype.equals("m5mysqltask"))
			return new M5MySqlTask();
		if (tasktype.equals("m10mysqltask"))
			return new M10MySqlTask();
		if (tasktype.equals("m30mysqltask"))
			return new M30MySqlTask();
		
		if (tasktype.equals("m5urltask"))
			return new M5URLTask();
		if (tasktype.equals("m10urltask"))
			return new M10URLTask();
		if (tasktype.equals("m30urltask"))
			return new M30URLTask();
		
		if (tasktype.equals("m5sockettask"))
			return new M5SocketTask();
		if (tasktype.equals("m10sockettask"))
			return new M10SocketTask();
		if (tasktype.equals("m30sockettask"))
			return new M30SocketTask();
		
		if (tasktype.equals("m5mailtask"))
			return new M5MailTask();
		if (tasktype.equals("m10mailtask"))
			return new M10MailTask();
		if (tasktype.equals("m30mailtask"))
			return new M30MailTask();
		
		if (tasktype.equals("m5ftptask"))
			return new M5FTPTask();
		if (tasktype.equals("m10ftptask"))
			return new M10FTPTask();
		if (tasktype.equals("m30ftptask"))
			return new M30FTPTask();
		
		if (tasktype.equals("m5weblogictask"))
			return new M5WeblogicTask();
		if (tasktype.equals("m10weblogictask"))
			return new M10WeblogicTask();
		if (tasktype.equals("m30weblogictask"))
			return new M30WeblogicTask();
		
		if (tasktype.equals("m5wastask"))
			return new M5WasTask();
		if (tasktype.equals("m10wastask"))
			return new M10WasTask();
		if (tasktype.equals("m30wastask"))
			return new M30WasTask();
		
		if (tasktype.equals("m5tomcattask"))
			return new M5TomcatTask();
		if (tasktype.equals("m10tomcattask"))
			return new M10TomcatTask();
		if (tasktype.equals("m30tomcattask"))
			return new M30TomcatTask();
		
		if (tasktype.equals("m1telnettask"))
			return new M1TelnetTask();
		if (tasktype.equals("m2telnettask"))
			return new M2TelnetTask();
		if (tasktype.equals("m3telnettask"))
			return new M3TelnetTask();
		if (tasktype.equals("m4telnettask"))
			return new M4TelnetTask();
		if (tasktype.equals("m5telnettask"))
			return new M5TelnetTask();
		if (tasktype.equals("m10telnettask"))
			return new M10TelnetTask();
		if (tasktype.equals("m30telnettask"))
			return new M30TelnetTask();
		if (tasktype.equals("d1telnettask"))
			return new D1TelnetTask();
		if (tasktype.equals("m_30_backupTelnetConfigTask")){
			return new M30BackupTelnetConfigTask();  
		}
		return null;
	}

}