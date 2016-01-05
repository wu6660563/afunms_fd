package com.afunms.polling.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;


import com.afunms.application.model.IISConfig;
import com.afunms.application.model.IISVo;
import com.afunms.common.util.ReflactUtil;
import com.afunms.util.DataGate;
/**
 * <p>����ɼ���IIS������Ϣ</p>
 * @author HONGLI  Mar 7, 2011
 */
public class ProcessIISData {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * ����IIS����Ϣ
	 * @param iisconfigs   IIS�ļ���
	 * @param iisdatas  IIS�����ݼ���
	 */
	public void saveIISData(List<IISConfig> iisconfigs, Hashtable iisdatas) {
		if(iisconfigs == null || iisconfigs.size() == 0 || iisdatas == null || iisdatas.isEmpty()){
			return ;
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement deletePstmt = null;
		String sql = "insert into nms_iis_temp(nodeid, entity, value, collecttime) values(?, ?, ?, ?)";
		String deleteSql = "delete from nms_iis_temp where nodeid = ?";
		try {
			conn = DataGate.getCon();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			deletePstmt = conn.prepareStatement(deleteSql);
			Calendar tempCal=Calendar.getInstance();
			Date cc = tempCal.getTime();
			String time = sdf.format(cc);
			for (int i = 0; i < iisconfigs.size(); i++) {
				IISConfig iisconfig = (IISConfig)iisconfigs.get(i);
				String iisconfigid = iisconfig.getId()+"";
				if(iisdatas.containsKey(iisconfig.getIpaddress())){
					List<IISVo> iisvoList = (ArrayList<IISVo>)iisdatas.get(iisconfig.getIpaddress());
					IISVo iisVo = (IISVo)iisvoList.get(0);
					if(iisVo == null){
						continue;
					}
					deletePstmt.setString(1, iisconfig.getId()+"");
					deletePstmt.execute();
					pstmt = setPreperstatements(pstmt, time, iisconfigid, iisVo);
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

	/**
	 * ���÷������ ���IISVO������Ϣ���
	 * @param pstmt
	 * @param time
	 * @param iisconfigid
	 * @param iisVo
	 * @return
	 * @throws Exception
	 */
	private PreparedStatement setPreperstatements(PreparedStatement pstmt, String time,
			String iisconfigid, IISVo iisVo) throws Exception {
		//�õ����еķ�����
		Field[] fields = iisVo.getClass().getDeclaredFields();
		for(Field field:fields){
			String fieldname = field.getName();
			String value = (String)ReflactUtil.invokeGet(iisVo, fieldname);
			pstmt.setString(1, iisconfigid);
			pstmt.setString(2, fieldname);
			pstmt.setString(3, value);
			pstmt.setString(4, time);
			pstmt.addBatch();
		}
		return pstmt;
	}
}
