package com.afunms.cleaning.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;

public class PerformanceDataCleaningService {

	private static SysLogger logger = SysLogger
			.getLogger(PerformanceDataCleaningService.class.getName());

	private static String strDriver = "com.mysql.jdbc.Driver";

	private static String IP = "127.0.0.1";

	private static String USERNAME = "root";

	private static String PASSWORD = "root";

	private static String DBNAME = "afunms";

	private static String DBURL = "jdbc:mysql://" + IP + "/" + DBNAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&useUnicode=true&&characterEncoding=gbk&autoReconnect = true";

	private static String OPERATION_DROP = "drop";

	private static String OPERATION_TRUNCATE = "truncate";

	private static String OPERATION_DELETE = "delete";

	private static String OPERATION_OPTIMIZE = "optimize";

	private static String OPERATION_CREATE = "create";

	private static String TABLE_TYPE_ALL = "ALL";

	private static String TABLE_TYPE_MINUTE = "";

	private static String TABLE_TYPE_HOUR = "hour";

	private static String TABLE_TYPE_DAY = "day";

	private static String TABLENAME_PARAMETER = "?";
	private String ip = IP;

	private String username = USERNAME;

	private String password = PASSWORD;

	private String dbName = DBNAME;

	private String dburl = DBURL;

	private static List<String> TABLE_NAME_LIST = new ArrayList<String>();

	static {
		String performanceDataTableFile = "WEB-INF/classes/performanceDataTable.xml";
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new File(ResourceCenter.getInstance()
					.getSysPath() + performanceDataTableFile));
			@SuppressWarnings("unchecked")
			List<Element> tableNames = (List<Element>) doc.getRootElement()
					.getChildren("table-name");
			for (Element element : tableNames) {
				TABLE_NAME_LIST.add(element.getText());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Class.forName(strDriver).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the dburl
	 */
	public String getDburl() {
		return dburl;
	}

	/**
	 * @param dburl the dburl to set
	 */
	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dburl, username, password);
	}

	/**
	 * 删除所有表的性能数据
	 * @return
	 */
	public boolean dropAllTable() {
		boolean result = false;
		String operation = OPERATION_DROP +" table if exists ";
		List<String> tableTypes = new ArrayList<String>();
		tableTypes.add(TABLE_TYPE_ALL);
		execute(operation, tableTypes);
		return result;
	}

	/**
	 * 删除所有表的性能数据
	 * @return
	 */
	public boolean createAllTable() {
		boolean result = false;
		String operation = OPERATION_CREATE + " table if not exists";
		List<String> tableTypes = new ArrayList<String>();
		tableTypes.add(TABLE_TYPE_ALL);
		String where = "(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
	        + "THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
	        + " PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
		execute(operation, tableTypes, where);
		return result;
	}

	/**
	 * 删除所有表的性能数据
	 * @return
	 */
	public boolean truncateAllTable() {
		boolean result = false;
		String operation = OPERATION_TRUNCATE;
		List<String> tableTypes = new ArrayList<String>();
		tableTypes.add(TABLE_TYPE_ALL);
		execute(operation, tableTypes);
		return result;
	}

	/**
     * 删除所有表的性能数据
     * @return
     */
    public boolean optimizeAllTable() {
        boolean result = false;
        String operation = OPERATION_OPTIMIZE + " table";
        List<String> tableTypes = new ArrayList<String>();
        tableTypes.add(TABLE_TYPE_ALL);
        execute(operation, tableTypes);
        return result;
    }

    /**
     * 删除指定时间之前的所有表的性能数据
     * @return
     */
    public boolean DelateAllTable(String time) {
        boolean result = false;
        String operation = OPERATION_DELETE + " from";
        List<String> tableTypes = new ArrayList<String>();
        tableTypes.add(TABLE_TYPE_ALL);
        execute(operation, tableTypes, " where collecttime<='" + time + "'");
        return result;
    }

    public void execute(String operation, List<String> tableTypes) {
		execute(operation, tableTypes, "");
	}

	public boolean execute(String operation, List<String> tableTypes, String where) {
		boolean result = false;
		if (operation == null) {
			return result;
		}
		if (tableTypes == null || tableTypes.size() == 0) {
			return result;
		}
		if (where == null) {
			where = "";
		}
		String sql = "select * from topo_host_node";
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		Connection executeConn = null;
		try {
			conn = getConnection();
			executeConn = getConnection();
			rs = conn.createStatement().executeQuery(sql);
			statement = executeConn.createStatement();
			int i = 0;
			while (rs.next()) {
				i++;
				String ipaddress = rs.getString("ip_address");
				for (String tableName : TABLE_NAME_LIST) {
					String executeSql = "";
					if (tableTypes.contains(TABLE_TYPE_ALL) || tableTypes.contains(TABLE_TYPE_MINUTE)) {
						executeSql = operation + " " + tableName + TABLE_TYPE_MINUTE + CommonUtil.doip(ipaddress) + where.replace("?", tableName + TABLE_TYPE_MINUTE + CommonUtil.doip(ipaddress));
						logger.info("executeSql:" + executeSql);
						statement.addBatch(executeSql);
					}
					if (tableTypes.contains(TABLE_TYPE_ALL) || tableTypes.contains(TABLE_TYPE_HOUR)) {
						executeSql = operation + " " + tableName + TABLE_TYPE_HOUR + CommonUtil.doip(ipaddress) + where.replace("?", tableName + TABLE_TYPE_MINUTE + CommonUtil.doip(ipaddress));
						logger.info("executeSql:" + executeSql);
						statement.addBatch(executeSql);
					}
					if (tableTypes.contains(TABLE_TYPE_ALL) || tableTypes.contains(TABLE_TYPE_DAY)) {
						executeSql = operation + " " + tableName + TABLE_TYPE_DAY + CommonUtil.doip(ipaddress) + where.replace("?", tableName + TABLE_TYPE_MINUTE + CommonUtil.doip(ipaddress));
						logger.info("executeSql:" + executeSql);
						statement.addBatch(executeSql);
					}
				}
				statement.executeBatch();
				statement.clearBatch();
				logger.info("execute completion of " + i + " it's ipaddress:" + ipaddress);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (executeConn != null && !executeConn.isClosed()) {
					executeConn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
