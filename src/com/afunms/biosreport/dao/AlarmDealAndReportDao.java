package com.afunms.biosreport.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.biosreport.model.AlarmDealAndReportModel;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;

public class AlarmDealAndReportDao extends BaseDao implements DaoInterface{

	public AlarmDealAndReportDao(){
		super("system_event_data");
	}
	
	/**
	 * 根据报告数来认定其是否被处理
	 * 
	 * @return
	 */
	public List findUndealReport(){
		return super.findByCondition(" where report_count < 1");
	}
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		AlarmDealAndReportModel vo = new AlarmDealAndReportModel();
		try {
			vo.setId(rs.getInt("id"));
			vo.setEventid(rs.getInt("eventid"));
			vo.setAlarmCount(rs.getInt("alarm_count"));
			vo.setBussinessid(rs.getString("businessid"));
			vo.setContext(rs.getString("context"));
			vo.setEnventlocation(rs.getString("eventlocation"));
			vo.setLevel1(rs.getInt("level1"));
			vo.setManagesign(rs.getInt("managesign"));
			vo.setManagetime(rs.getString("managetime"));
			vo.setNodeid(rs.getInt("nodeid"));
			vo.setRecordtime(rs.getString("recordtime"));
			vo.setReportContent(rs.getString("report_content"));
			vo.setReportCount(rs.getInt("report_count"));
			vo.setReportman(rs.getString("reportman"));
			vo.setReportTime(rs.getString("report_time"));
			vo.setSubentity(rs.getString("subentity"));
			vo.setSubtype(rs.getString("subtype"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public boolean save(BaseVo vo) {
		AlarmDealAndReportModel mo = (AlarmDealAndReportModel)vo;
		StringBuffer sql= new StringBuffer();
		sql.append("insert into system_event_data(eventid,eventlocation, context, recordtime, alarm_count,"
				+ "level1, nodeid, businessid, subtype, subentity, managesign, managetime, reportman," 
				+ " report_content, report_time, report_count) values(");
		sql.append(mo.getEventid());
		sql.append(",'");
		sql.append(mo.getEnventlocation());
		sql.append("','");
		sql.append(mo.getContext());
		sql.append("','");
		sql.append(mo.getRecordtime());
		sql.append("',");
		sql.append(mo.getAlarmCount());
		sql.append(",");
		sql.append(mo.getLevel1());
		sql.append(",");
		sql.append(mo.getNodeid());
		sql.append(",'");
		sql.append(mo.getBussinessid());
		sql.append("','");
		sql.append(mo.getSubtype());
		sql.append("','");
		sql.append(mo.getSubentity());
		sql.append("',");
		sql.append(mo.getManagesign());
		sql.append(",'");
		sql.append(mo.getManagetime());
		sql.append("','");
		sql.append(mo.getReportman());
		sql.append("','");
		sql.append(mo.getReportContent());
		sql.append("','");
		sql.append(mo.getReportTime());
		sql.append("',");
		sql.append(mo.getReportCount());
		sql.append(");");
		
		System.out.println(sql.toString());
		
		return saveOrUpdate(sql.toString());
	}

	/**
	 * 
	 * @return
	 */
	public boolean updateReportInfo(BaseVo vo) {
		
		AlarmDealAndReportModel mo = (AlarmDealAndReportModel)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("update system_event_data set report_count=");
		sql.append(mo.getReportCount());
		sql.append(",reportman='");
		sql.append(mo.getReportman());
		sql.append("',report_content='");
		sql.append(mo.getReportContent());
		sql.append("',report_time='");
		sql.append(mo.getRecordtime() + "'");
		sql.append(" where id=" + mo.getId());
		System.out.println(sql.toString());
		return saveOrUpdate(sql.toString());
	}
	
	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

}
