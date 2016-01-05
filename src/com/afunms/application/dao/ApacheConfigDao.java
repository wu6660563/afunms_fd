package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.model.ApacheConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;

public class ApacheConfigDao extends BaseDao implements DaoInterface{

	public ApacheConfigDao() {
		super("nms_apacheconfig");
		// TODO Auto-generated constructor stub
	}

	   /**
	    * 
	    * ȡ������ֵ�ŵ�vo��
	    */
		public BaseVo loadFromRS(ResultSet rs) {
			ApacheConfig vo=new ApacheConfig();
			
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
				
				SysLogger.error("ApacheConfigDao.loadFromRS()",e);
			}
			
			return vo;
		}
	    /**
	     * apache
	     * ���һ����¼
	     */
		public boolean save(BaseVo vo) {
			ApacheConfig vo1=(ApacheConfig)vo;
			StringBuffer sql=new StringBuffer();
			sql.append("insert into nms_apacheconfig(alias,username,password,ipaddress,port,flag,sendmobiles,sendemail,sendphone,netid) values('");
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
			return saveOrUpdate(sql.toString());
			
		}  
		  /**
		   * apache
		   * ����id �鴦����
		   * @param bids
		   * @return
		   */
		   public List getApacheByBID(Vector bids){
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
			   sql.append("select * from nms_apacheconfig "+wstr);
			 
			   return findByCriteria(sql.toString());
		   }
		   /**
		    * apache
		    * ����flag��ѯһ����¼
		    * @param flag
		    * @return
		    */
		   public List getApacheByFlag(int flag){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
		
			   sql.append("select * from nms_apacheconfig where flag = "+flag);
			  
			   return findByCriteria(sql.toString());
		   }
		   /**
		    * apache
		    * ����id ��ѯĳ����¼
		    * @param id
		    * @return
		    */
		   public List getApacheById(int id){
			   List rlist = new ArrayList();
			   StringBuffer sql = new StringBuffer();
		
			   sql.append("select * from nms_apacheconfig where id = "+id);
			   
			   return findByCriteria(sql.toString());
		   }
		/**
		 * apache
		 * �޸�һ����¼
		 */
		public boolean update(BaseVo vo) {
			ApacheConfig vo1=(ApacheConfig)vo;
			StringBuffer sql=new StringBuffer();
		
			sql.append("update nms_apacheconfig set alias ='");
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
		
		public List<ApacheConfig> getApacheConfigListByMonFlag(Integer flag){
			StringBuffer sql = new StringBuffer();
			sql.append("select * from nms_apacheconfig where flag= ");
			sql.append(flag);
			return findByCriteria(sql.toString());
		}

		public Hashtable getApacheDataHashtable(String nodeid) throws Exception {
			Hashtable hm = new Hashtable();
			try {
				//SysLogger.info();
				StringBuffer sqlBuffer = new StringBuffer();
				sqlBuffer.append("select * from nms_apache_temp where nodeid = '");
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
