package com.afunms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.AgentConfig;

/**
 * ��agent���ݶ�Ӧ��nms_agent_config�е����ݽ��д���
 * @author LiNan
 *
 */

public class AgentConfigDao extends BaseDao implements DaoInterface {
	public AgentConfigDao(){
		super("nms_agent_config");
	}
/**
 * ������ת����model
 */
	public BaseVo loadFromRS(ResultSet rs) {
		AgentConfig vo=new AgentConfig();
		try {
			vo.setAgentid(rs.getInt("id"));
			vo.setAgentname(rs.getString("agentname"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setAgentdesc(rs.getString("agentdesc"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vo;
	}
/**
 * ���µ�agent�������ݿ�
 */
	public boolean save(BaseVo vo) {
		AgentConfig agentconfig=(AgentConfig) vo;
		StringBuffer sql = new StringBuffer(100);
		
		sql.append("insert into nms_agent_config(agentname,ipaddress,agentdesc)values(");
		sql.append("'");
		sql.append(agentconfig.getAgentname());
		sql.append("','");
		sql.append(agentconfig.getIpaddress());
		sql.append("','");
		sql.append(agentconfig.getAgentdesc());
		sql.append("')");
		
		return saveOrUpdate(sql.toString());
	}
/**
 * �����ݿ��е�agent�����޸�
 */
	public boolean update(BaseVo vo) {
		AgentConfig agentconfig=(AgentConfig) vo;
		StringBuffer sql = new StringBuffer(100);
		
		sql.append("update nms_agent_config set agentname='");
		sql.append(agentconfig.getAgentname());
		sql.append("',ipaddress='");
		sql.append(agentconfig.getIpaddress());
		sql.append("',agentdesc='");
		sql.append(agentconfig.getAgentdesc());
		sql.append("'where id=");
		sql.append(agentconfig.getAgentid());
		
		return saveOrUpdate(sql.toString());
	}
/**
 * ����ɾ��agent��¼
 * @param agentid
 * @return
 */
	public boolean deleteall(String[] agentid){
		boolean result = false;
		try{
			for(int i=0;i<agentid.length;i++){
			conn.addBatch("delete from nms_agent_config where id=" + agentid[i]);
			conn.addBatch("delete from nms_node_agent where agentid=" + agentid[i]);
			conn.executeBatch();
			result = true;
	   }
		}
	   catch(Exception ex){
		    SysLogger.error("AgentConfigDao.delete()",ex);
	        result = false;
	   }finally{
		    conn.close();
	   }
	   return result;
	}	
}
