package com.afunms.indicators.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.indicators.model.GatherIndicators;

/**
 * 基准性能监控指标 dao
 * @author Administrator
 *
 */

public class GatherIndicatorsDao extends BaseDao implements DaoInterface {

	public GatherIndicatorsDao() {
		super("nms_gather_indicators");
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		
		GatherIndicators gatherIndicators = new GatherIndicators();
		try {
			gatherIndicators.setId(rs.getInt("id"));
			gatherIndicators.setName(rs.getString("name"));
			gatherIndicators.setType(rs.getString("type"));
			gatherIndicators.setSubtype(rs.getString("subtype"));
			gatherIndicators.setAlias(rs.getString("alias"));
			gatherIndicators.setDescription(rs.getString("description"));
			gatherIndicators.setCategory(rs.getString("category"));
			gatherIndicators.setIsDefault(rs.getString("isDefault"));
			gatherIndicators.setIsCollection(rs.getString("isCollection"));
			gatherIndicators.setPoll_interval(rs.getString("poll_interval"));
			gatherIndicators.setInterval_unit(rs.getString("interval_unit"));
			gatherIndicators.setClasspath(rs.getString("classpath"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gatherIndicators;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		
		GatherIndicators gatherIndicators = (GatherIndicators)vo;
		
		StringBuffer sql = new StringBuffer(200);
		sql.append("insert into nms_gather_indicators(name, type, subtype, alias, description, category, isDefault, isCollection, poll_interval, interval_unit, classpath)values('");
		sql.append(gatherIndicators.getName());
		sql.append("','");
		sql.append(gatherIndicators.getType());
		sql.append("','");
		sql.append(gatherIndicators.getSubtype());
		sql.append("','");
		sql.append(gatherIndicators.getAlias());
		sql.append("','");
		sql.append(gatherIndicators.getDescription());  
		sql.append("','");
		sql.append(gatherIndicators.getCategory());     
		sql.append("','");
		sql.append(gatherIndicators.getIsDefault());     
		sql.append("','");
		sql.append(gatherIndicators.getIsCollection());  
		sql.append("','");
		sql.append(gatherIndicators.getPoll_interval());   
		sql.append("','");
		sql.append(gatherIndicators.getInterval_unit());    
		sql.append("','");
		sql.append(gatherIndicators.getClasspath());    
		sql.append("')");	
//		System.out.println(sql.toString());
		return saveOrUpdate(sql.toString());
		
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		GatherIndicators gatherIndicators = (GatherIndicators)vo;
		StringBuffer sql = new StringBuffer(200);
        sql.append("update nms_gather_indicators set name='");
        sql.append(gatherIndicators.getName());
        sql.append("',type='");
        sql.append(gatherIndicators.getType());
        sql.append("',subtype='");
        sql.append(gatherIndicators.getSubtype()); 
        sql.append("',alias='");
        sql.append(gatherIndicators.getAlias());   
        sql.append("',description='");
        sql.append(gatherIndicators.getDescription());
        sql.append("',category='");
        sql.append(gatherIndicators.getCategory());
        sql.append("',isDefault='");
        sql.append(gatherIndicators.getIsDefault());
        sql.append("',isCollection='");
        sql.append(gatherIndicators.getIsCollection());
        sql.append("',poll_interval='");
        sql.append(gatherIndicators.getPoll_interval());
        sql.append("',interval_unit='");
        sql.append(gatherIndicators.getInterval_unit());
        sql.append("',classpath='");
        sql.append(gatherIndicators.getClasspath());
        sql.append("' where id=");
        sql.append(gatherIndicators.getId());
		return saveOrUpdate(sql.toString());
		
	}
	public List updatelist(int id){
		String type = "";
		String subtype = "";
		try {
			rs = conn.executeQuery("select * from nms_gather_indicators where id='"
							+ id + "';");
			while(rs.next()){
			type = rs.getString("type");
			subtype = rs.getString("subtype");
			}
		} catch (Exception e) {
		}
		return findByCriteria("select * from nms_gather_indicators where type='"+type+"' and subtype='"+subtype+"';");
	}
	public String type(int id){
		String type="";
		try {
					rs=conn.executeQuery("select * from nms_gather_indicators where id='"
				+ id + "';");
		while(rs.next()){
			type=rs.getString("type");
		}
		} catch (Exception e) {
		}
		return type;
	}
	
	public String subtype(int id){
		String subtype="";
		try {
			rs=conn.executeQuery("select * from nms_gather_indicators where id='"
					+ id + "';");
			while(rs.next()){
				subtype=rs.getString("subtype");
			}
		} catch (Exception e) {
		}
		return subtype;
	}
	public List getByTypeAndSubtype(String type , String subtype){
		return findByCondition(" where type='" + type + "' and subtype='" + subtype + "'");
	}
	
	public List<GatherIndicators> getByTypeAndSubtype(String type , String subtype,String flag){
		return findByCondition(" where type='" + type + "' and subtype='" + subtype + "' and isDefault='"+flag+"'");
	}
	//根据采集方式查找对应的采集指标
	public List<GatherIndicators> getByTypeAndSubtypeAndcollecttype(String type , String subtype,String flag,int collecttype){
		return findByCondition(" where type='" + type + "' and subtype='" + subtype + "' and collecttype='"+collecttype+"'");
	}
	
	
	public List<GatherIndicators> getByTypeAndSubtype(String type , String subtype,String flag,String indiname){
		return findByCondition(" where type='" + type + "' and subtype='" + subtype + "' and isDefault='"+flag+"' and name='"+indiname+"'");
	}
	public List gatherfind(String con1,String con2){
		if(con1.equals("全部")&&con2.equals("全部")){
			return findByCriteria("select * from nms_gather_indicators;");
		}
		else if(!con1.equals("全部")&&con2.equals("全部")){
			return findByCriteria("select * from nms_gather_indicators where type='"+con1+"';");
		}
		else if(!con1.equals("全部")&&!con2.equals("全部")){
			return findByCriteria("select * from nms_gather_indicators where type='"+con1+"' and subtype='"+con2+"';");
		}
		else{
			return findByCriteria("select * from nms_gather_indicators;");
		}
	}

}
