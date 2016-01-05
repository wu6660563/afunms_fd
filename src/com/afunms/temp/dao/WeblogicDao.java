package com.afunms.temp.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.application.weblogicmonitor.WeblogicHeap;
import com.afunms.application.weblogicmonitor.WeblogicJdbc;
import com.afunms.application.weblogicmonitor.WeblogicNormal;
import com.afunms.application.weblogicmonitor.WeblogicQueue;
import com.afunms.application.weblogicmonitor.WeblogicServer;
import com.afunms.application.weblogicmonitor.WeblogicServlet;
import com.afunms.application.weblogicmonitor.WeblogicWeb;
import com.afunms.util.DataGate;


/**
 * @author HONGLI  Mar 3, 2011
 */
public class WeblogicDao {

	
	/**
	 * 取出指定weblogic的信息
	 * @param labels  要取的数据信息的key组成的List
	 * @param nodeid  weblogic的ID
	 * @return
	 */
	public Hashtable getWeblogicData(List labels, String nodeid){
		Hashtable weblogicData = new Hashtable();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataGate.getCon();
			stmt = conn.createStatement();
			if(labels.indexOf("heapValue") != -1){
				List<WeblogicHeap> weblogicHeaps = new ArrayList<WeblogicHeap>();
				String sql = "select * from nms_weblogic_heap where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicHeap weblogicHeap = new WeblogicHeap();
					weblogicHeap.setJvmRuntimeHeapFreeCurrent(rs.getString("jvmRuntimeHeapFreeCurrent"));
					weblogicHeap.setJvmRuntimeHeapSizeCurrent(rs.getString("jvmRuntimeHeapSizeCurrent"));
					weblogicHeap.setJvmRuntimeName(rs.getString("jvmRuntimeName"));
					weblogicHeaps.add(weblogicHeap);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("heapValue", weblogicHeaps);
			}
			if(labels.indexOf("queueValue") != -1){
				List<WeblogicQueue> weblogicQueues = new ArrayList<WeblogicQueue>();
				String sql = "select * from nms_weblogic_queue where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicQueue weblogicQueue = new WeblogicQueue();
					weblogicQueue.setExecuteQueueRuntimeName(rs.getString("executeQueueRuntimeName"));
					weblogicQueue.setExecuteQueueRuntimePendingRequestCurrentCount(rs.getString("executeQueueRuntimePendingRequestCurrentCount"));
					weblogicQueue.setExecuteQueueRuntimePendingRequestOldestTime(rs.getString("executeQueueRuntimePendingRequestOldestTime"));
					weblogicQueue.setExecuteQueueRuntimePendingRequestTotalCount(rs.getString("executeQueueRuntimePendingRequestTotalCount"));
					weblogicQueue.setThreadPoolRuntimeExecuteThreadIdleCount(rs.getString("threadPoolRuntimeExecuteThreadIdleCount"));
					weblogicQueues.add(weblogicQueue);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("queueValue", weblogicQueues);
			}
			if(labels.indexOf("jdbcValue") != -1){
				List<WeblogicJdbc> weblogicJdbcs = new ArrayList<WeblogicJdbc>();
				String sql = "select * from nms_weblogic_jdbc where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicJdbc weblogicJdbc = new WeblogicJdbc();
					weblogicJdbc.setJdbcConnectionPoolName(rs.getString("jdbcConnectionPoolName"));
					weblogicJdbc.setJdbcConnectionPoolRuntimeActiveConnectionsAverageCount(rs.getString("jdbcConnectionPoolRuntimeActiveConnectionsAverageCount"));
					weblogicJdbc.setJdbcConnectionPoolRuntimeActiveConnectionsCurrentCount(rs.getString("jdbcConnectionPoolRuntimeActiveConnectionsCurrentCount"));
					weblogicJdbc.setJdbcConnectionPoolRuntimeHighestNumAvailable(rs.getString("jdbcConnectionPoolRuntimeHighestNumAvailable"));
					weblogicJdbc.setJdbcConnectionPoolRuntimeMaxCapacity(rs.getString("jdbcConnectionPoolRuntimeMaxCapacity"));
					weblogicJdbc.setJdbcConnectionPoolRuntimeVersionJDBCDriver(rs.getString("jdbcConnectionPoolRuntimeVersionJDBCDriver"));
					weblogicJdbcs.add(weblogicJdbc);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("jdbcValue", weblogicJdbcs);
			}
			if(labels.indexOf("webappValue") != -1){
				List<WeblogicWeb> weblogicWebs = new ArrayList<WeblogicWeb>();
				String sql = "select * from nms_weblogic_webapps where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicWeb weblogicWeb = new WeblogicWeb();
					weblogicWeb.setWebAppComponentRuntimeComponentName(rs.getString("webAppComponentRuntimeComponentName"));
					weblogicWeb.setWebAppComponentRuntimeOpenSessionsCurrentCount(rs.getString("webAppComponentRuntimeOpenSessionsCurrentCount"));
					weblogicWeb.setWebAppComponentRuntimeOpenSessionsHighCount(rs.getString("webAppComponentRuntimeOpenSessionsHighCount"));
					weblogicWeb.setWebAppComponentRuntimeSessionsOpenedTotalCount(rs.getString("webAppComponentRuntimeSessionsOpenedTotalCount"));
					weblogicWeb.setWebAppComponentRuntimeStatus(rs.getString("webAppComponentRuntimeStatus"));
					weblogicWebs.add(weblogicWeb);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("webappValue", weblogicWebs);
			}
			if(labels.indexOf("serverValue") != -1){
				List<WeblogicServer> weblogicServers = new ArrayList<WeblogicServer>();
				String sql = "select * from nms_weblogic_server where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicServer weblogicServer = new WeblogicServer();
					weblogicServer.setIpaddress(rs.getString("ipaddress"));
					weblogicServer.setServerRuntimeListenAddress(rs.getString("serverRuntimeListenAddress"));
					weblogicServer.setServerRuntimeListenPort(rs.getString("serverRuntimeListenPort"));
					weblogicServer.setServerRuntimeName(rs.getString("serverRuntimeName"));
					weblogicServer.setServerRuntimeOpenSocketsCurrentCount(rs.getString("serverRuntimeOpenSocketsCurrentCount"));
					weblogicServer.setServerRuntimeState(rs.getString("serverRuntimeState"));
					weblogicServers.add(weblogicServer);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("serverValue", weblogicServers);
			}
			if(labels.indexOf("servletValue") != -1){
				List<WeblogicServlet> weblogicServlets = new ArrayList<WeblogicServlet>();
				String sql = "select * from nms_weblogic_servlet where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicServlet weblogicServlet = new WeblogicServlet();
					weblogicServlet.setServletRuntimeExecutionTimeAverage(rs.getString("servletRuntimeExecutionTimeAverage"));
					weblogicServlet.setServletRuntimeExecutionTimeHigh(rs.getString("servletRuntimeExecutionTimeHigh"));
					weblogicServlet.setServletRuntimeExecutionTimeLow(rs.getString("servletRuntimeExecutionTimeLow"));
					weblogicServlet.setServletRuntimeExecutionTimeTotal(rs.getString("servletRuntimeExecutionTimeTotal"));
					weblogicServlet.setServletRuntimeInvocationTotalCount(rs.getString("servletRuntimeInvocationTotalCount"));
					weblogicServlet.setServletRuntimeName(rs.getString("servletRuntimeName"));
					weblogicServlet.setServletRuntimePoolMaxCapacity(rs.getString("servletRuntimePoolMaxCapacity"));
					weblogicServlet.setServletRuntimeReloadTotalCount(rs.getString("servletRuntimeReloadTotalCount"));
					weblogicServlet.setServletRuntimeServletName(rs.getString("servletRuntimeServletName"));
					weblogicServlet.setServletRuntimeType(rs.getString("servletRuntimeType"));
					weblogicServlet.setServletRuntimeURL(rs.getString("servletRuntimeURL"));
					weblogicServlets.add(weblogicServlet);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("servletValue", weblogicServlets);
			}
			if(labels.indexOf("normalValue") != -1){
				List<WeblogicNormal> weblogicNormals = new ArrayList<WeblogicNormal>();
				String sql = "select * from nms_weblogic_normal where nodeid = '"+nodeid+"'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					WeblogicNormal weblogicNormal = new WeblogicNormal();
					weblogicNormal.setDomainActive(rs.getString("domainActive"));
					weblogicNormal.setDomainAdministrationPort(rs.getString("domainAdministrationPort"));
					weblogicNormal.setDomainConfigurationVersion(rs.getString("domainConfigurationVersion"));
					weblogicNormal.setDomainName(rs.getString("domainName"));
					weblogicNormals.add(weblogicNormal);
				}
				if(rs != null){
					rs.close();
				}
				weblogicData.put("normalValue", weblogicNormals);
			}
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return weblogicData;
	}
}
