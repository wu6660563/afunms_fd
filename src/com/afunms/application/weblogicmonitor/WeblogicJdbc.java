package com.afunms.application.weblogicmonitor;

public class WeblogicJdbc {
	  String jdbcConnectionPoolName = null; // ����
	  String jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount = null;//	��ǰ�������
	  String jdbcConnectionPoolRuntimeVersionJDBCDriver = null;//	���ӳ������İ汾
	  String jdbcConnectionPoolRuntimeMaxCapacity = null;//	���ӳ��������
	  String jdbcConnectionPoolRuntimeActiveConnectionsAverageCount = null;//	���ӳ�ƽ���������
	  String jdbcConnectionPoolRuntimeHighestNumAvailable = null;//	��߿ɻ������
	  String jdbcLeaked = null;
	  String jdbcWaitMaxTime = null;
	  String jdbcWaitCurrent = null;
	  
	public String getJdbcLeaked() {
		return jdbcLeaked;
	}
	public void setJdbcLeaked(String jdbcLeaked) {
		this.jdbcLeaked = jdbcLeaked;
	}
	public String getJdbcWaitMaxTime() {
		return jdbcWaitMaxTime;
	}
	public void setJdbcWaitMaxTime(String jdbcWaitMaxTime) {
		this.jdbcWaitMaxTime = jdbcWaitMaxTime;
	}
	public String getJdbcWaitCurrent() {
		return jdbcWaitCurrent;
	}
	public void setJdbcWaitCurrent(String jdbcWaitCurrent) {
		this.jdbcWaitCurrent = jdbcWaitCurrent;
	}
	public String getJdbcConnectionPoolName() {
		return jdbcConnectionPoolName;
	}
	public void setJdbcConnectionPoolName(String jdbcConnectionPoolName) {
		this.jdbcConnectionPoolName = jdbcConnectionPoolName;
	}
	public String getJdbcConnectionPoolRuntimeActiveConnectionsCurrentCount() {
		return jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount;
	}
	public void setJdbcConnectionPoolRuntimeActiveConnectionsCurrentCount(
			String jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount) {
		this.jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount = jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount;
	}
	public String getJdbcConnectionPoolRuntimeVersionJDBCDriver() {
		return jdbcConnectionPoolRuntimeVersionJDBCDriver;
	}
	public void setJdbcConnectionPoolRuntimeVersionJDBCDriver(
			String jdbcConnectionPoolRuntimeVersionJDBCDriver) {
		this.jdbcConnectionPoolRuntimeVersionJDBCDriver = jdbcConnectionPoolRuntimeVersionJDBCDriver;
	}
	public String getJdbcConnectionPoolRuntimeMaxCapacity() {
		return jdbcConnectionPoolRuntimeMaxCapacity;
	}
	public void setJdbcConnectionPoolRuntimeMaxCapacity(
			String jdbcConnectionPoolRuntimeMaxCapacity) {
		this.jdbcConnectionPoolRuntimeMaxCapacity = jdbcConnectionPoolRuntimeMaxCapacity;
	}
	public String getJdbcConnectionPoolRuntimeActiveConnectionsAverageCount() {
		return jdbcConnectionPoolRuntimeActiveConnectionsAverageCount;
	}
	public void setJdbcConnectionPoolRuntimeActiveConnectionsAverageCount(
			String jdbcConnectionPoolRuntimeActiveConnectionsAverageCount) {
		this.jdbcConnectionPoolRuntimeActiveConnectionsAverageCount = jdbcConnectionPoolRuntimeActiveConnectionsAverageCount;
	}
	public String getJdbcConnectionPoolRuntimeHighestNumAvailable() {
		return jdbcConnectionPoolRuntimeHighestNumAvailable;
	}
	public void setJdbcConnectionPoolRuntimeHighestNumAvailable(
			String jdbcConnectionPoolRuntimeHighestNumAvailable) {
		this.jdbcConnectionPoolRuntimeHighestNumAvailable = jdbcConnectionPoolRuntimeHighestNumAvailable;
	}

}
