/**
 * <p>Description:get mysql information</p>
 * <p>Company: afunms</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-6
 */

package com.afunms.application.util;

import java.sql.*;
import java.util.*;

import com.afunms.common.util.*;

public class MySQLHelper
{
	private Connection conn;
	private String basePath; //����·��
	private String dataPath; //����·��
	private String version;  //���ݿ�汾
	private String hostOS;   //����������ϵͳ
	private List tablesDetail;  //����ϸ
	private List sessionsDetail;  //��ǰ����
	
	public MySQLHelper(Connection conn)
    {
        this.conn = conn;	
    }
	
	public void init(String dbName) 
	{
		Statement stmt = null;
		ResultSet rs = null;
        try
        {
        	stmt = conn.createStatement();
        	rs = stmt.executeQuery("show variables");
        	while(rs.next())
        	{
        		if(rs.getString("variable_name").equals("basedir"))
        		   basePath = rs.getString("value");
        		else if(rs.getString("variable_name").equals("datadir"))
        		   dataPath = rs.getString("value");
        		else if(rs.getString("variable_name").equals("version"))
        		   version = rs.getString("value"); 
        		else if(rs.getString("variable_name").equals("version_compile_os"))
        		   hostOS = rs.getString("value");         		
        	}
        	rs.close();
        	rs = stmt.executeQuery("show table status from " + dbName);
        	tablesDetail = new ArrayList();
        	while(rs.next())
        	{
        		String[] item = new String[4];
        		item[0] = rs.getString("name"); //����
        		item[1] = rs.getString("rows"); //������
        		item[2] = rs.getLong("data_length")/1024 + " k"; //���С
        		item[3] = rs.getString("create_time").substring(0,16); //����ʱ��
        		tablesDetail.add(item);
        	}
        	rs.close();
        	rs = stmt.executeQuery("show processlist");
        	sessionsDetail = new ArrayList();
        	while(rs.next())
        	{
        		if(!rs.getString("db").equals(dbName)) continue;
        		
        		String[] item = new String[4];
        		item[0] = rs.getString("user");   //�û�
        		item[1] = rs.getString("host");   //����
        		item[2] = rs.getString("command"); //����
        		item[3] = rs.getString("time") + " s"; //����ʱ��
        		sessionsDetail.add(item);
        	}        	
        }
        catch(Exception e)
        {
        	SysLogger.error("Error in MySQLHelper.init(),dbName=" + dbName);
        }
        finally
        {
        	DBPool.getInstance().close(stmt,rs);
        }
	}
	
    public String getBasePath() {
		return basePath;
	}

	public String getDataPath() {
		return dataPath;
	}

	public List getSessionsDetail() {
		return sessionsDetail;
	}

	public List getTablesDetail() {
		return tablesDetail;
	}

	public String getVersion() {
		return version;
	}
	
	public String getHostOS() {
		return hostOS;
	}			
}