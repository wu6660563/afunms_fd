package com.afunms.polling.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import com.afunms.application.model.ApacheConfig;
import com.afunms.detail.service.tomcatInfo.TomcatInfoService;
import com.afunms.polling.node.Tomcat;
import com.afunms.util.DataGate;
/**
 * <p>处理采集的Apache数据信息</p>
 * @author HONGLI  Mar 5, 2011
 */
public class ProcessApacheData {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 保存Apache Http Server的信息
	 * @param apacheConfigs   ApacheConfig的集合
	 * @param datas  的数据集合
	 */
	public void saveApacheData(List<ApacheConfig> apacheConfigs, Hashtable datas) {
		if(apacheConfigs == null || apacheConfigs.size() == 0 || datas == null || datas.isEmpty()){
			return;
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement deletePstmt = null;
		String sql = "insert into nms_apache_temp(nodeid, entity, value, collecttime) values(?, ?, ?, ?)";
		String deleteSql = "delete from nms_apache_temp where nodeid = ?";
		try {
			conn = DataGate.getCon();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql); 
			deletePstmt = conn.prepareStatement(deleteSql);
			Calendar tempCal=Calendar.getInstance();
			Date cc = tempCal.getTime();
			String time = sdf.format(cc);
			for (int i = 0; i < apacheConfigs.size(); i++) {
				ApacheConfig apacheConfig = (ApacheConfig)apacheConfigs.get(i);
				if(datas.containsKey("apache:"+apacheConfig.getIpaddress())){
					Hashtable apachedata = (Hashtable)datas.get("apache:"+apacheConfig.getIpaddress());
					if(apachedata == null){
						continue;
					}
					deletePstmt.setString(1, apacheConfig.getId()+"");
					deletePstmt.execute();
					Iterator iterator = apachedata.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String)iterator.next();
						String value = (String)apachedata.get(key);
						if(value != null){
							value = value.trim();
						}else{
							value = "--";
						}
						pstmt.setString(1, apacheConfig.getId()+"");
						pstmt.setString(2, key);
						pstmt.setString(3, value.trim());
						pstmt.setString(4, time);
						pstmt.addBatch();
					}
				}
			}
			pstmt.executeBatch();//批量插入
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
			if(deletePstmt != null){
				try {
					deletePstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
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
	}
}
