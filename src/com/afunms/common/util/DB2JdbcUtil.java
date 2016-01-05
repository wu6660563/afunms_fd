/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB2JdbcUtil {
	String strconn;// = "jdbc:jtds:sqlserver://localhost;DatabaseName=CenterDB;charset=GBK;SelectMethod=CURSOR";

	String strDriver = "com.ibm.db2.jcc.DB2Driver";
	//jdbc:oracle:thin:@MyDbComputerNameOrIP:1521:ORCL
	String name;
	String pass;
	Connection conn = null;

	public Statement stmt = null;

	ResultSet rs = null;
	
	public DB2JdbcUtil(String url,String name,String pass){
		this.strconn = url;
		this.name = name;
		this.pass = pass;
	}
	public java.sql.Connection jdbc() {
		file: //connection
		try {
			Class.forName(strDriver).newInstance();
			//SysLogger.info(strconn+"==="+name+"==="+pass);
			conn = DriverManager.getConnection(strconn, name, pass);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public ResultSet executeQuery(String sql) {
		file: //select
		try {
			
			conn = DriverManager.getConnection(strconn, name,pass);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

		} catch (SQLException ex) {
			System.err.println("ִ��SQL������select��" + ex.getMessage());
		}
		return rs;
	}

	public ResultSet executeUpdate(String sql) {
		file: //insert ,update
		try {
			conn = DriverManager.getConnection(strconn,name,pass);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
		} catch (SQLException ex) {
			System.err.println("ִ��SQL������insert,update:" + ex.getMessage());
		}
		return rs;
	}

	public void closeStmt() {
		file: //close statement
		try {
			if(stmt != null)
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConn() {
		file: //close connection
		try {
			if(conn != null)
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}