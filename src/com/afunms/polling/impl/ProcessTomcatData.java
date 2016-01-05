package com.afunms.polling.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.detail.service.tomcatInfo.TomcatInfoService;
import com.afunms.polling.node.Tomcat;
import com.afunms.util.DataGate;
import com.mysql.jdbc.Statement;
/**
 * <p>����ɼ���Tomcat������Ϣ</p>
 * @author HONGLI  Mar 5, 2011
 */
public class ProcessTomcatData {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * ����tomcat����Ϣ
	 * @param tomcats   tomcat�ļ���
	 * @param tomcatdatas  tomcat�����ݼ���
	 */
	public void saveTomcatData(List<Tomcat> tomcats, Hashtable tomcatdatas) {
		if(tomcats == null || tomcats.size() == 0 || tomcatdatas == null || tomcatdatas.isEmpty()){
			return ;
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement deletePstmt = null;
		String sql = "insert into nms_tomcat_temp(nodeid, entity, value, collecttime) values(?, ?, ?, ?)";
		String deleteSql = "delete from nms_tomcat_temp where nodeid = ?";
		try {
			conn = DataGate.getCon();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			deletePstmt = conn.prepareStatement(deleteSql);
			Calendar tempCal=Calendar.getInstance();
			Date cc = tempCal.getTime();
			String time = sdf.format(cc);
			for (int i = 0; i < tomcats.size(); i++) {
				Tomcat tomcat = (Tomcat)tomcats.get(i);
				if(tomcatdatas.containsKey(tomcat.getIpAddress())){
					Hashtable tomcatData = (Hashtable)tomcatdatas.get(tomcat.getIpAddress());
					if(tomcatData == null){
						continue;
					}
					deletePstmt.setString(1, tomcat.getId()+"");
					deletePstmt.execute();
					Iterator iterator = tomcatData.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String)iterator.next();
						String value = (String)tomcatData.get(key);
						if("portdetail1".equalsIgnoreCase(key)){//���ڶ˿���ϸ����̫��   ��ʱ�Ƚ�ȡһ����
							if(value.length() > 200){
								value = value.substring(0, 200);
							}
						}
						pstmt.setString(1, tomcat.getId()+"");
						pstmt.setString(2, key);
						pstmt.setString(3, value.trim());
						pstmt.setString(4, time);
						pstmt.addBatch();
//						System.out.println("entity:value--->"+key+":"+value);
					}
				}
			}
			pstmt.executeBatch();//��������
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
