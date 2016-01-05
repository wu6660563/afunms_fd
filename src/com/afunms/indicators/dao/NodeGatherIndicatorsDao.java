package com.afunms.indicators.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;

/**
 * �豸���ܼ��ָ�� dao
 * @author Administrator
 *
 */

public class NodeGatherIndicatorsDao extends BaseDao implements DaoInterface {

	public NodeGatherIndicatorsDao() {
		super("nms_gather_indicators_node");
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		
		NodeGatherIndicators nodeGatherIndicators = new NodeGatherIndicators();
		try {
			nodeGatherIndicators.setId(rs.getInt("id"));
			nodeGatherIndicators.setNodeid(rs.getString("nodeid"));
			nodeGatherIndicators.setName(rs.getString("name"));
			nodeGatherIndicators.setType(rs.getString("type"));
			nodeGatherIndicators.setSubtype(rs.getString("subtype"));
			nodeGatherIndicators.setAlias(rs.getString("alias"));
			nodeGatherIndicators.setDescription(rs.getString("description"));
			nodeGatherIndicators.setCategory(rs.getString("category"));
			nodeGatherIndicators.setIsDefault(rs.getString("isDefault"));
			nodeGatherIndicators.setIsCollection(rs.getString("isCollection"));
			nodeGatherIndicators.setPoll_interval(rs.getString("poll_interval"));
			nodeGatherIndicators.setInterval_unit(rs.getString("interval_unit"));
			nodeGatherIndicators.setClasspath(rs.getString("classpath"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nodeGatherIndicators;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		
		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)vo;
		
		StringBuffer sql = new StringBuffer(200);
		sql.append("insert into nms_gather_indicators_node(nodeid, name, type, subtype, alias, description, category, isDefault, isCollection, poll_interval, interval_unit, classpath)values('");
		sql.append(nodeGatherIndicators.getNodeid());
		sql.append("','");
		sql.append(nodeGatherIndicators.getName());
		sql.append("','");
		sql.append(nodeGatherIndicators.getType());
		sql.append("','");
		sql.append(nodeGatherIndicators.getSubtype());
		sql.append("','");
		sql.append(nodeGatherIndicators.getAlias());
		sql.append("','");
		sql.append(nodeGatherIndicators.getDescription());  
		sql.append("','");
		sql.append(nodeGatherIndicators.getCategory());     
		sql.append("','");
		sql.append(nodeGatherIndicators.getIsDefault());     
		sql.append("','");
		sql.append(nodeGatherIndicators.getIsCollection());  
		sql.append("','");
		sql.append(nodeGatherIndicators.getPoll_interval());  
		sql.append("','");
		sql.append(nodeGatherIndicators.getInterval_unit());  
		sql.append("','");
		sql.append(nodeGatherIndicators.getClasspath());  
		sql.append("')");	
		return saveOrUpdate(sql.toString());
		
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)vo;
		StringBuffer sql = new StringBuffer(200);
        sql.append("update nms_gather_indicators_node set name='");
        sql.append(nodeGatherIndicators.getName());
        sql.append("',nodeid='");
        sql.append(nodeGatherIndicators.getNodeid());
        sql.append("',type='");
        sql.append(nodeGatherIndicators.getType());
        sql.append("',subtype='");
        sql.append(nodeGatherIndicators.getSubtype()); 
        sql.append("',alias='");
        sql.append(nodeGatherIndicators.getAlias());   
        sql.append("',description='");
        sql.append(nodeGatherIndicators.getDescription());
        sql.append("',category='");
        sql.append(nodeGatherIndicators.getCategory());
        sql.append("',isDefault='");
        sql.append(nodeGatherIndicators.getIsDefault());
        sql.append("',isCollection='");
        sql.append(nodeGatherIndicators.getIsCollection());
        sql.append("',poll_interval='");
        sql.append(nodeGatherIndicators.getPoll_interval());
        sql.append("',interval_unit='");
        sql.append(nodeGatherIndicators.getInterval_unit());
        sql.append("',classpath='");
        sql.append(nodeGatherIndicators.getClasspath());
        sql.append("' where id=");
        sql.append(nodeGatherIndicators.getId());
		return saveOrUpdate(sql.toString());
		
	}
	
	public boolean saveBatch(List<NodeGatherIndicators> list) {
		// TODO Auto-generated method stub
		try {
			Iterator<NodeGatherIndicators> iterator = list.iterator();
			while (iterator.hasNext()) {
				NodeGatherIndicators nodeGatherIndicators = iterator.next();
				StringBuffer sql = new StringBuffer(200);
				sql.append("insert into nms_gather_indicators_node(nodeid, name, type, subtype, alias, description, category, isDefault, isCollection, poll_interval, interval_unit, classpath)values('");
				sql.append(nodeGatherIndicators.getNodeid());
				sql.append("','");
				sql.append(nodeGatherIndicators.getName());
				sql.append("','");
				sql.append(nodeGatherIndicators.getType());
				sql.append("','");
				sql.append(nodeGatherIndicators.getSubtype());
				sql.append("','");
				sql.append(nodeGatherIndicators.getAlias());
				sql.append("','");
				sql.append(nodeGatherIndicators.getDescription());  
				sql.append("','");
				sql.append(nodeGatherIndicators.getCategory());     
				sql.append("','");
				sql.append(nodeGatherIndicators.getIsDefault());     
				sql.append("','");
				sql.append(nodeGatherIndicators.getIsCollection());  
				sql.append("','");
				sql.append(nodeGatherIndicators.getPoll_interval());   
				sql.append("','");
				sql.append(nodeGatherIndicators.getInterval_unit());   
				sql.append("','");
				sql.append(nodeGatherIndicators.getClasspath());    
				sql.append("')");	
				try {
					conn.addBatch(sql.toString());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			conn.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public boolean updateBatch(List<NodeGatherIndicators> list) {
		// TODO Auto-generated method stub
		try {
			Iterator<NodeGatherIndicators> iterator = list.iterator();
			while (iterator.hasNext()) {
				NodeGatherIndicators nodeGatherIndicators = iterator.next();
				StringBuffer sql = new StringBuffer(200);
		        sql.append("update nms_gather_indicators_node set name='");
		        sql.append(nodeGatherIndicators.getName());
		        sql.append("',nodeid='");
		        sql.append(nodeGatherIndicators.getNodeid());
		        sql.append("',type='");
		        sql.append(nodeGatherIndicators.getType());
		        sql.append("',subtype='");
		        sql.append(nodeGatherIndicators.getSubtype()); 
		        sql.append("',alias='");
		        sql.append(nodeGatherIndicators.getAlias());   
		        sql.append("',description='");
		        sql.append(nodeGatherIndicators.getDescription());
		        sql.append("',category='");
		        sql.append(nodeGatherIndicators.getCategory());
		        sql.append("',isDefault='");
		        sql.append(nodeGatherIndicators.getIsDefault());
		        sql.append("',isCollection='");
		        sql.append(nodeGatherIndicators.getIsCollection());
		        sql.append("',poll_interval='");
		        sql.append(nodeGatherIndicators.getPoll_interval());
		        sql.append("',interval_unit='");
		        sql.append(nodeGatherIndicators.getInterval_unit());
		        sql.append("',classpath='");
		        sql.append(nodeGatherIndicators.getClasspath());
		        sql.append("' where id=");
		        sql.append(nodeGatherIndicators.getId());
				try {
					conn.addBatch(sql.toString());
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			conn.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean deleteByNodeIdAndTypeAndSubtype(String nodeid , String type , String subtype){
		String sql = "delete from nms_gather_indicators_node where nodeid='" + nodeid + "' and type='" + type + "'";
		if(subtype == null || "null".equalsIgnoreCase(subtype) || subtype.trim().length() == 0){
			
		}else{
			sql = sql+" and subtype='" + subtype + "'";
		}
		//String sql = "delete from nms_gather_indicators_node where nodeid='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'";
		return saveOrUpdate(sql);
	}
	
	public List findByNodeIdAndTypeAndSubtype(String nodeid , String type , String subtype){
		if(subtype == null || "null".equalsIgnoreCase(subtype) || subtype.trim().length() == 0){
			return findByCondition(" where nodeid ='" + nodeid + "' and type='" + type + "'");
		}else
		//SysLogger.info("select * from nms_gather_indicators_node where nodeid ='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
		return findByCondition(" where nodeid ='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
	}
	public List findByNodeIdAndTypeAndSubtype(String nodeid , String type , String subtype,String isDefault){
		if(subtype == null || "null".equalsIgnoreCase(subtype) || subtype.trim().length() == 0){
			return findByCondition(" where nodeid ='" + nodeid + "' and type='" + type + "' and isDefault='"+isDefault+"'");
		}else
		//SysLogger.info("select * from nms_gather_indicators_node where nodeid ='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
		return findByCondition(" where nodeid ='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "' and isDefault='"+isDefault+"'");
	}
	public List getByInterval(String interval ,String unit,int enabled){
		String sql = "select * from nms_gather_indicators_node where poll_interval='" + interval + "' and interval_unit='" + unit + "' and isCollection='"+enabled+"'";
		List list = findByCriteria(sql);
		return list;
	}
	public List getByInterval(String interval ,String unit,int enabled,String type , String subtype){
		String sql = "select * from nms_gather_indicators_node where poll_interval='" + interval + "' and interval_unit='" + unit + "' and isCollection='"+enabled+ "' and type='" + type + "' and subtype='" + subtype + "'";
		List list = findByCriteria(sql);
		//SysLogger.info(sql);
		return list;
	}
	
	public List getByNodeidAndInterval(String nodeid,String interval ,String unit,int enabled,String type){
		String sql = "select * from nms_gather_indicators_node where nodeid=" + nodeid + " and poll_interval='" + interval + "' and interval_unit='" + unit + "' and isCollection='"+enabled+ "' and type='" + type + "'";
		SysLogger.info(sql);
		List list = findByCriteria(sql);
		return list;
	}
	
	public List getByNodeidAndType(String nodeid,int enabled,String type){
		String sql = "select * from nms_gather_indicators_node where nodeid=" + nodeid  + " and isCollection='"+enabled+ "' and type='" + type + "'";
		List list = findByCriteria(sql);
		return list;
	}
	
	public List getByNodeId(String nodeid ,int enabled,String type , String subtype){
		String sql = "select * from nms_gather_indicators_node where nodeid=" + nodeid + " and isCollection='"+enabled+ "' and type='" + type + "' and subtype='" + subtype + "'";
		List list = findByCriteria(sql);
		return list;
	}
	
	public List getByIntervalAndType(String interval ,String unit,int enabled,String type ){
		String sql = "select * from nms_gather_indicators_node where poll_interval='" + interval + "' and interval_unit='" + unit + "' and isCollection='"+enabled+ "' and type='" + type + "'";
		//SysLogger.info(sql);
		List list = findByCriteria(sql);
		return list;
	} 

	public List getByIntervalAndType(String nodeid, String interval ,String unit,int enabled,String type ){
		String sql = "select * from nms_gather_indicators_node where nodeid ='"+nodeid+ "'and poll_interval='" + interval + "' and interval_unit='" + unit + "' and isCollection='"+enabled+ "' and type='" + type + "'";
		List list = findByCriteria(sql);
		return list;
	}
	
	public Hashtable getAllGather(){
		Hashtable rethash = new Hashtable();
		String sql = "select * from nms_gather_indicators_node where isCollection='1'" ;
		List list = findByCriteria(sql);
		if(list != null && list.size()>0){
			for(int i=0;i<list.size();i++){
				NodeGatherIndicators vo = (NodeGatherIndicators)list.get(i);
				if(rethash.containsKey(vo.getNodeid()+":"+vo.getType()+":"+vo.getPoll_interval()+":"+vo.getInterval_unit())){
					((List)rethash.get(vo.getNodeid()+":"+vo.getType()+":"+vo.getPoll_interval()+":"+vo.getInterval_unit())).add(vo);
				}else{
					List gatherlist = new ArrayList();
					gatherlist.add(vo);
					rethash.put(vo.getNodeid()+":"+vo.getType()+":"+vo.getPoll_interval()+":"+vo.getInterval_unit(),gatherlist);
					
				}
			}
		}
		return rethash;
	}
}
