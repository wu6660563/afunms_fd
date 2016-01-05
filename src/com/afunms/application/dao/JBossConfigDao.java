package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.model.DnsConfig;
import com.afunms.application.model.JBossConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;

public class JBossConfigDao extends BaseDao implements DaoInterface{

	public JBossConfigDao() {
		super("nms_jbossconfig");
		// TODO Auto-generated constructor stub
	}

	 /**
	    * 
	    * ȡ������ֵ�ŵ�vo��
	    */
		public BaseVo loadFromRS(ResultSet rs) {
			JBossConfig vo=new JBossConfig();
			
			try {
				vo.setId(rs.getInt("id"));
				vo.setAlias(rs.getString("alias"));
				vo.setUsername(rs.getString("username"));
				vo.setPassword(rs.getString("password"));
				vo.setIpaddress(rs.getString("ipaddress"));
				vo.setPort(rs.getInt("port"));
				vo.setFlag(rs.getInt("flag"));
				vo.setSendmobiles(rs.getString("sendmobiles"));
				vo.setSendemail(rs.getString("sendemail"));
				vo.setNetid(rs.getString("netid"));
				vo.setSendphone(rs.getString("sendphone"));
				
			} catch (SQLException e) {
				
				SysLogger.error("JBossConfigDao.loadFromRS()",e);
			}
			
			return vo;
		}
	    /**
	     * DNS
	     * ���һ����¼
	     */
		public boolean save(BaseVo vo) {
			JBossConfig vo1=(JBossConfig)vo;
			StringBuffer sql=new StringBuffer();
			sql.append("insert into nms_jbossconfig(alias,username,password,ipaddress,port,flag,sendmobiles,sendemail,sendphone,netid) values('");
			sql.append(vo1.getAlias());
			sql.append("','");
			sql.append(vo1.getUsername());
			sql.append("','");
			sql.append(vo1.getPassword());
			sql.append("','");
			sql.append(vo1.getIpaddress());
			sql.append("',");
			sql.append(vo1.getPort());
			sql.append(",'");
			sql.append(vo1.getFlag());
			sql.append("','");
			sql.append(vo1.getSendmobiles());
			sql.append("','");
			sql.append(vo1.getSendemail());
			sql.append("','");
			sql.append(vo1.getSendphone());
			sql.append("','");
			sql.append(vo1.getNetid());
			sql.append("')");
		    System.out.println(sql+"11!!!!!!!!!!!!!!!!!!!!!!!!!");
			return saveOrUpdate(sql.toString());
			
		}  
		  /**
		   * DNS
		   * ����id �鴦����
		   * @param bids
		   * @return
		   */
		   public List getJBossByBID(Vector bids){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
			   String wstr = "";
			   if(bids != null && bids.size()>0){
				   for(int i=0;i<bids.size();i++){
					   if(wstr.trim().length()==0){
						   wstr = wstr+" where ( netid like '%,"+bids.get(i)+",%' "; 
					   }else{
						   wstr = wstr+" or netid like '%,"+bids.get(i)+",%' ";
					   }
					   
				   }
				   wstr=wstr+")";
			   }
			   sql.append("select * from nms_jbossconfig "+wstr);
			 
			   return findByCriteria(sql.toString());
		   }
		   /**
		    * DNS
		    * ����flag��ѯһ����¼
		    * @param flag
		    * @return
		    */
		   public List getJBossByFlag(int flag){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
		
			   sql.append("select * from nms_jbossconfig where flag = "+flag);
			  
			   return findByCriteria(sql.toString());
		   }
		   /**
		    * DNS
		    * ����id ��ѯĳ����¼
		    * @param id
		    * @return
		    */
		   public List getJBossById(int id){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
		
			   sql.append("select * from nms_jbossconfig where id = "+id);
			   
			   return findByCriteria(sql.toString());
		   }
		/**
		 * DNS
		 * �޸�һ����¼
		 */
		public boolean update(BaseVo vo) {
			JBossConfig vo1=(JBossConfig)vo;
			StringBuffer sql=new StringBuffer();
		
			sql.append("update nms_jbossconfig set alias ='");
			sql.append(vo1.getAlias());
			sql.append("',username='");
			sql.append(vo1.getUsername());
			sql.append("',password='");
			sql.append(vo1.getPassword());
			sql.append("',ipaddress='");
			sql.append(vo1.getIpaddress());
			sql.append("',port=");
			sql.append(vo1.getPort());;
			sql.append(",flag='");
			sql.append(vo1.getFlag());
			sql.append("',sendmobiles='");
			sql.append(vo1.getSendmobiles());
			sql.append("',sendemail='");
			sql.append(vo1.getSendemail());
			sql.append("',sendphone='");
			sql.append(vo1.getSendphone());
			sql.append("',netid='");
			sql.append(vo1.getNetid());
			sql.append("' where id="+vo1.getId());
		
			return saveOrUpdate(sql.toString());
			
		}
		
		public List<JBossConfig> getJBossConfigListByMonFlag(Integer flag){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from nms_jbossconfig where flag= ");
			sql.append(flag);
			return findByCriteria(sql.toString());
		}

		/**
		 * ��ȡ���ݿ��д洢�Ĳɼ���JBoss������Ϣ
		 * @param nodeid
		 * @return
		 * @throws SQLException
		 */
		public Hashtable<String, String> getJBossData(String nodeid) throws SQLException {
			Hashtable hm = new Hashtable();
			try {
				//SysLogger.info();
				StringBuffer sqlBuffer = new StringBuffer();
				sqlBuffer.append("select * from nms_jboss_temp where nodeid = '");
				sqlBuffer.append(nodeid);
				sqlBuffer.append("'");
				 rs = conn.executeQuery(sqlBuffer.toString());
				// ȡ������,
				while (rs.next()) {
					String entity = rs.getString("entity");
					String value = rs.getString("value");
					hm.put(entity, value);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				if(rs != null){
					rs.close();
				}
			}
			return hm;
		}
}
