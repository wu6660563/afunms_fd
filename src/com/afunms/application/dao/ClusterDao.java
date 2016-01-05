package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.afunms.application.model.Cluster;
import com.afunms.application.model.UpAndDownMachine;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;

public class ClusterDao extends BaseDao implements DaoInterface{
    public ClusterDao(){
    	super("nms_remote_up_down_cluster");
    }
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		Cluster cluster=new Cluster();
		try {
			cluster.setId(rs.getInt("id"));
			cluster.setName(rs.getString("name"));
			cluster.setServerType(rs.getString("serverType"));
			cluster.setCreatetime(rs.getTimestamp("createtime"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cluster;
	}

	public boolean save(BaseVo vo) {
		Cluster cluster=(Cluster)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_remote_up_down_cluster(name,serverType,createtime) values('");
		sql.append(cluster.getName());
		sql.append("','");
		sql.append(cluster.getServerType());
		sql.append("','");
		sql.append(cluster.getCreatetime());
	
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		Cluster cluster = (Cluster)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_remote_up_down_cluster set name='");
		sql.append(cluster.getName());
		
		sql.append("',serverType='");
		sql.append(cluster.getServerType());
		sql.append("',createtime='");
		sql.append(cluster.getCreatetime());
		sql.append("' where id=");
		sql.append(cluster.getId());
		System.out.println(sql.toString());
		return this.saveOrUpdate(sql.toString());
	}

}
