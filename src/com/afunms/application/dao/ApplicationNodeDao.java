package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.afunms.application.model.ApplicationNode;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;

public class ApplicationNodeDao extends BaseDao implements DaoInterface {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(
    	"yyyy-MM-dd HH:mm:ss");

	private static SysLogger logger = SysLogger.getLogger(ApplicationNodeDao.class.getName());

	public ApplicationNodeDao() {
		super("nms_application_node");
	}
	
	// -------------load all --------------
	public List loadAll() {
		List list = new ArrayList(5);
		try {
			rs = conn.executeQuery("select * from " + super.table + " order by id");
			while (rs.next())
				list.add(loadFromRS(rs));
		} catch (Exception e) {
			SysLogger.error("ApplicationNodeDao:loadAll()", e);
			list = null;
		} finally {
		    conn.close();
		}
		return list;
	}

	public List loadByWhere(String where) {
		List list = new ArrayList();
		try {
			rs = conn.executeQuery("select * from "+ super.table + " " + where);
	    while (rs.next()) {
	        list.add(loadFromRS(rs));
	    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return list;
	}
		
	public boolean save(BaseVo baseVo) {
		ApplicationNode vo = (ApplicationNode) baseVo;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into " + table + " (id,name,ip_address,unique_key,type,subtype," +
				"bid,descr,managed)values(");
		sql.append(vo.getId());
		sql.append(",'");
		sql.append(vo.getName());
		sql.append("','");
		sql.append(vo.getIpAddress());
		sql.append("','");
		sql.append(vo.getUniqueKey());
		sql.append("','");
		sql.append(vo.getType());
		sql.append("','");
		sql.append(vo.getSubtype());
		sql.append("','");
		sql.append(vo.getBid());
		sql.append("','");
		sql.append(vo.getDescr());
		sql.append("','");
		String isManaged = "0";
		if(vo.isManaged()) {
			isManaged = "1";
		}
		sql.append(isManaged);
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}
		
		
		public boolean update(BaseVo baseVo) {
			ApplicationNode node = (ApplicationNode)(baseVo);
			StringBuffer sql=new StringBuffer();
			sql.append("update " + table + " set name ='");
			sql.append(node.getName());
			sql.append("',ip_address='");
			sql.append(node.getIpAddress());
			sql.append("',unique_key='");
			sql.append(node.getUniqueKey());
			sql.append("',type='");
			sql.append(node.getType());
			sql.append("',subtype='");
			sql.append(node.getSubtype());
			sql.append("',bid='");
			sql.append(node.getBid());
			sql.append("',descr='");
			sql.append(node.getDescr());
			sql.append("',managed='");
			String isManaged = "0";
			if(node.isManaged()) {
				isManaged = "1";
			}
			sql.append(isManaged);
			sql.append("' where id="+node.getId());
			return saveOrUpdate(sql.toString());
		}
		
		public boolean delete(String[] id) {
			return super.delete(id);
		}
		
		public BaseVo loadFromRS(ResultSet rs) {
			ApplicationNode vo = new ApplicationNode();
			try {
				vo.setId(rs.getInt("id"));
				vo.setName(rs.getString("name"));
				vo.setIpAddress(rs.getString("ip_address"));
				vo.setUniqueKey(rs.getString("unique_key"));
				vo.setType(rs.getString("type"));
				vo.setSubtype(rs.getString("subtype"));
				vo.setBid(rs.getString("bid"));
				vo.setDescr(rs.getString("descr"));
				String isManaged = rs.getString("managed");
				if("1".equals(isManaged.trim())){
					vo.setManaged(true);
				} else {
					vo.setManaged(false);
				}
			} catch (Exception e) {
				logger.error("ApplicationNodeDao.loadFromRS()", e);
				vo = null;
			}
			return vo;
		}
		
		public BaseVo findByName(String name) {
			ApplicationNode vo = null;
			try {
			    rs = conn.executeQuery("select * from " + super.table + " where name='"
			            + name + "'");
			    if (rs.next()) {
			        vo = (ApplicationNode) loadFromRS(rs);
			    }
			} catch (Exception e) {
			    logger.error("ApplicationNodeDao.findByName()", e);
			}
			return vo;
		}
		
		public BaseVo findByUniquekey(String uniquekey) {
			ApplicationNode vo = null;
			try {
				rs = conn.executeQuery("select * from " + super.table + " where unique_key='"
						+ uniquekey + "'");
				if (rs.next()) {
					vo = (ApplicationNode) loadFromRS(rs);
				}
			} catch (Exception e) {
				logger.error("ApplicationNodeDao.findByUniquekey()", e);
			}
			return vo;
		}
		
		
}