package com.afunms.biosreport;

public class BiosConstant {

	public static BiosConstant instance;
	
	public static BiosConstant getInstance(){
		if(instance != null){
			instance = new BiosConstant();
		}
		return instance;
	}
	
	private BiosConstant(){
	}
	
	// 网络设备类型标示
	public static final String NET_DEVICE = "net_device";

	// windows服务器类型表示
	public static final String WINDOWS_SERVER = "windows_server";
	
	// aix服务器类型标示
	public static final String AIX_SERVAER = "aix_server";
	
	// oracle数据库类型标示
	public static final String DB_ORACLE = "db_oracle";
	
	// sql server数据库类型标示
	public static final String DB_SQLSERVER = "sql_server";
	
	// cpu
	public static final String DATA_CPU = "cpu";
	
	// memory
	public static final String DATA_MEMORY = "memory";
	
	// ping
	public static final String DATA_PING = "ping";

	public static final String HOUR = "hour";
	public static final String DAY = "day";

	// week time
	public static final long WEEK = 1000*60*60*24*7;
	
	
	
	// 告警数据小时表/天表
	public static final String TABLE_ALARM_HOUR = "system_alarm_archiving_hour";
	public static final String TABLE_ALARM_DAY = "system_alarm_archiving_day";
	
	// 网络设备报表模板路径
	public static final String NET_DEVICE_REPORT_LOCATION = "WEB-INF\\report\\netDeviceReport.brt";

	// Windows server 报表模板路径
	public static final String WINDOWS_SERVER_REPORT_LOCATION = "WEB-INF\\report\\windows_server.brt";
	
	// aix server 报表模板路径
	public static final String AIX_SERVER_REPORT_LOCATION = "WEB-INF\\report\\aix_server.brt";
	
	// db oracle 报表模板路径
	public static final String DB_ORACLE_REPORT_LOCATION = "WEB-INF\\report\\db_oracle.brt";
	
	
	

	public static String BIOS_NET_REPORT_DIR = "E:\\bios-net-report-files";
	public static String BIOS_WINDOWS_SERVER_REPORT_DIR = "E:\\bios-net-report-files";
	public static String BIOS__REPORT_DIR = "E:\\bios-net-report-files";
	public static String BIOS_N_REPORT_DIR = "E:\\bios-net-report-files";
	

	
	
	/**
	 * 
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	public String getCommonSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue signed)) as max_val, avg(cast(thevalue signed)) as avg_val, " +
		" min(cast(thevalue signed)) as min_val from " + table +
		" where to_days(collecttime) >= to_days("+ start + " )" +
		" and to_days(collecttime) <= to_days(" + end + ");";
		System.out.println(sql);
		return sql;
	}
	
	public String getAlarmSQL(int nodeid, String start, String end){
		return "select sum(count) as sums , level1 from " + TABLE_ALARM_DAY
				+ "system_alarm_archiving_hour where nodeid="+ nodeid  
				+ " and recordtime >= '"+ start 
				+ "' and recordtime <= '" + end + "' group by level1;";
	}
}
