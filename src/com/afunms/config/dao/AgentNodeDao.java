package com.afunms.config.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.AgentNode;

/**
 * agent����������豸 dao
 * @author LiNan
 * 
 */

public class AgentNodeDao {

	public BaseVo loadFromRS(ResultSet rs) {
		AgentNode vo = new AgentNode();
		try {
			vo.setNodeid(rs.getInt("id"));
			vo.setIp_address(rs.getString("ip_address"));
			vo.setAlias(rs.getString("alias"));
		} catch (Exception e) {
			SysLogger.error("AgentNodeDao.loadFromRS()", e);
		}
		return vo;
	}

	/**
	 * linan ����nms_agent_config�е�agentID ��nms_node_agent���в�ѯ����Ӧ��nodeid
	 * �ڱ�topo_host_node�и���ID��ѯ�豸��Ӧip_address��alias��ʾ
	 */
	public List findbyid(int agentid) {
		List list = new ArrayList();
		ResultSet rs = null;
		DBManager conn = new DBManager();
		try {
			rs = conn
					.executeQuery("select a.id,a.ip_address,a.alias,b.agentid from topo_host_node a,nms_node_agent b where a.id=b.nodeid and b.agentid="
							+ agentid + ";");
			if (rs == null)
				return null;
			while (rs.next())
				list.add(loadFromRS(rs));
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("AgentNodeDao.findByid()", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			conn.close();
		}
		return list;
	}

	/**
	 * linan ����nms_agent_config�е�agentID ��nms_node_agent���в�ѯ����Ӧ��nodeid
	 * �ڱ�topo_host_node�и���ID��ѯδ��������豸�ڵ���Ϣ
	 */

	public List findfornode() {
		List list = new ArrayList();
		ResultSet rs = null;
		DBManager conn = new DBManager();
		try {
			rs = conn
					.executeQuery("select a.ip_address,a.alias,a.id from topo_host_node a where a.id not in (select nodeid from nms_node_agent);");
			if (rs == null)
				return null;
			while (rs.next())
				list.add(loadFromRS(rs));
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("AgentNodeDao.findByid()", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			conn.close();
		}

		return list;
	}

	/**
	 * linan ����nms_node_agent
	 * ��������������ӷ���
	 */
	public boolean save(String[] id,int agentid){
		boolean result = false;
		DBManager conn=new DBManager();
		try
		   {
		       for(int i=0;i<id.length;i++){
		           conn.addBatch("insert into nms_node_agent(agentid,nodeid)values('" +agentid+"','" +id[i]+"');");
		       		System.out.println("insert into nms_node_agent(agentid,nodeid)values('" +agentid+"','" +id[i]+"');");
			       conn.executeBatch();
			       result = true;
		       }
		   }
		   catch(Exception ex)
		   {
		       SysLogger.error("AgentNodeDao.save()",ex);
		       result = false;
		   }
		
		return result;
	}

	/**
	 * linan 
	 * ����nms_node_agent
	 * ������������ɾ������
	 */	
	public boolean delete(String[] nodeid){
		DBManager conn=new DBManager();
		boolean result = false;
		try{
			for(int i=0;i<nodeid.length;i++){
			conn.addBatch("delete from nms_node_agent where nodeid=" + nodeid[i]);
			conn.executeBatch();
			result = true;
	   }
		}
	   catch(Exception ex){
		    SysLogger.error("AgentNodeDao.delete()",ex);
	        result = false;
	   }finally{
		    conn.close();
	   }
	   return result;
	}

}
