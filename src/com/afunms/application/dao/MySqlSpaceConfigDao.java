package com.afunms.application.dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import net.sf.hibernate.Session;

import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.MySqlSpaceConfig;
import com.afunms.application.model.Sqldbconfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;

public class MySqlSpaceConfigDao extends BaseDao implements DaoInterface{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public MySqlSpaceConfigDao() {
		super("system_mysqlspaceconf");
	}
	
	public boolean save(BaseVo baseVo)
	{
		MySqlSpaceConfig vo = (MySqlSpaceConfig)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_mysqlspaceconf(ipaddress,dbname,linkuse,sms,bak,reportflag,alarmvalue,logflag)values(");
		sql.append("'");
		sql.append(vo.getIpaddress());
		sql.append("','");
		sql.append(vo.getDbname());	
		sql.append("','");
		sql.append(vo.getLinkuse());
		sql.append("',");
		sql.append(vo.getSms());
		sql.append(",'");
		sql.append(vo.getBak());
		sql.append("',");
		sql.append(vo.getReportflag());
		sql.append(",");
		sql.append(vo.getAlarmvalue());
		sql.append(",");
		sql.append(vo.getLogflag());
		sql.append(")");
		return saveOrUpdate(sql.toString());
	}
	
	  public boolean update(BaseVo baseVo)
	  {
		  MySqlSpaceConfig vo = (MySqlSpaceConfig)baseVo;
			boolean result = false;
			
			StringBuffer sql = new StringBuffer();
			sql.append("update system_mysqlspaceconf set ipaddress='");
			sql.append(vo.getIpaddress());
			sql.append("',dbname='");
			sql.append(vo.getDbname());	
			sql.append("',linkuse='");
			sql.append(vo.getLinkuse());
			sql.append("',sms=");
			sql.append(vo.getSms());
			sql.append(",bak='");
			sql.append(vo.getBak());
			sql.append("',reportflag=");
			sql.append(vo.getReportflag());
			sql.append(",alarmvalue=");
			sql.append(vo.getAlarmvalue());
			sql.append(",logflag=");
			sql.append(vo.getLogflag());
			sql.append(" where id=");
			sql.append(vo.getId());
	     
	     try
	     {
	         conn.executeUpdate(sql.toString());
	         result = true;
	     }
	     catch(Exception e)
	     {
	    	 e.printStackTrace();
	    	 result = false;
	         SysLogger.error("MySqlSpaceConfigDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }  
	     
	     return result;
	  }
	  
	   public BaseVo loadFromRS(ResultSet rs)
	   {
		   MySqlSpaceConfig vo = new MySqlSpaceConfig();
	      try
	      {
	          vo.setId(rs.getInt("id"));
	          vo.setIpaddress(rs.getString("ipaddress"));
	          vo.setDbname(rs.getString("dbname"));
	          vo.setLinkuse(rs.getString("linkuse"));
	          vo.setAlarmvalue(rs.getInt("alarmvalue"));
	          vo.setBak(rs.getString("bak"));
	          vo.setReportflag(rs.getInt("reportflag"));
	          vo.setSms(rs.getInt("sms"));
	          vo.setLogflag(rs.getInt("logflag"));
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
	          //SysLogger.info("EventListDao.loadFromRS()",e);
	          vo = null;
	      }
	      return vo;
	   } 
		/*
		 * ����IP��ѯ
		 * 
		 */
		public List getByIp(String ipaddress) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_mysqlspaceconf where ipaddress = '"+ipaddress+"' order by ipaddress");
				//Query query=session.createQuery("from Oraspaceconfig oraspaceconfig where oraspaceconfig.sms="+smsflag+" order by oraspaceconfig.ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
			}
			catch(Exception e){
				e.printStackTrace();
			}
		// TODO Auto-generated method stub
			return list;
	}
		public List getList() {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_mysqlspaceconf ");
				//Query query=session.createQuery("from Oraspaceconfig oraspaceconfig where oraspaceconfig.sms="+smsflag+" order by oraspaceconfig.ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
			}
			catch(Exception e){
				e.printStackTrace();
			}
		// TODO Auto-generated method stub
			return list;
	}
		
		
		/*
		 * ����IP���Ƿ�Ҫ��ʾ���ձ���ı�־λ��ѯ
		 * 
		 */
		public Hashtable getByAlarmflag(Integer smsflag) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_mysqlspaceconf where sms="+smsflag+" order by ipaddress");
				//Query query=session.createQuery("from Oraspaceconfig oraspaceconfig where oraspaceconfig.sms="+smsflag+" order by oraspaceconfig.ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						MySqlSpaceConfig MySqlSpaceConfig = (MySqlSpaceConfig)list.get(i);	
						SysLogger.info(MySqlSpaceConfig.getIpaddress()+":"+MySqlSpaceConfig.getDbname()+":"+MySqlSpaceConfig.getLogflag()+"    value:"+MySqlSpaceConfig.getAlarmvalue());
						retValue.put(MySqlSpaceConfig.getIpaddress()+":"+MySqlSpaceConfig.getDbname()+":"+MySqlSpaceConfig.getLogflag(), MySqlSpaceConfig);				
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		// TODO Auto-generated method stub
			return retValue;
	}
		
		/*
		 * 
		 * ���ڴ�����ݿ�����ȡÿ��IP�Ķ˿���Ϣ������˿����ñ���
		 */
		public void fromLastToMySqlSpaceConfig()
		throws Exception {		
			List list=new ArrayList();
			List list1=new ArrayList();
			List shareList = new ArrayList();
			int dbflag = 0;
			int logflag = 1;
			Hashtable oraspacehash= new Hashtable();
			Session session=null;
			MySqlSpaceConfig MySqlSpaceConfig = null;
			Vector configV = new Vector();
			try{
			
				//��sqldb���ñ����ȡ�б�
				rs = conn.executeQuery("select * from system_mysqlspaceconf order by ipaddress");
				while(rs.next())
		        	list1.add(loadFromRS(rs)); 
			
			if (list1 != null && list1.size()>0){			
				for(int i=0;i<list1.size();i++){
					MySqlSpaceConfig = (MySqlSpaceConfig)list1.get(i);	
					//IP:���ݿ�����:LOGFLAG				
					oraspacehash.put(MySqlSpaceConfig.getIpaddress()+":"+MySqlSpaceConfig.getDbname()+":"+MySqlSpaceConfig.getLogflag(),MySqlSpaceConfig);
				}
			}
					
			//���ڴ��еõ�����SQLDB�ɼ���Ϣ
			Hashtable sharedata = ShareData.getSqldbdata();		
			//�����ݿ�õ�����SQLDB�б�
			DBDao dbdao = new DBDao();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = typedao.findByDbtype("mysql");
			shareList = dbdao.getDbByTypeMonFlag(typevo.getId(), 1);
			if (shareList != null && shareList.size()>0){
				for(int i=0;i<shareList.size();i++){
					DBVo dbmonitorlist = (DBVo)shareList.get(i);					
					if(sharedata.get(dbmonitorlist.getIpAddress()) != null){
						Hashtable dbs = (Hashtable)sharedata.get(dbmonitorlist.getIpAddress());
						if (dbs == null )continue;					
						Hashtable spaces = new Hashtable();
						spaces.put("ip", dbmonitorlist.getIpAddress());
						spaces.put("dbs", dbs);
						list.add(spaces);
					}
				}
			}
			//�жϲɼ�����SQLDB��Ϣ�Ƿ��Ѿ���SQLDB���ñ����Ѿ����ڣ��������������
			if (list != null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Hashtable dbs = (Hashtable)list.get(i);
					if(dbs != null && dbs.size()>0){
						String ip = (String)dbs.get("ip");					
						Hashtable db_log = (Hashtable)dbs.get("dbs");		
						if(db_log != null && db_log.size()>0){
							//db_log��������database��logfile
							Hashtable database = (Hashtable)db_log.get("database");
							Hashtable logfile = (Hashtable)db_log.get("logfile");
							Vector names = (Vector)db_log.get("names");						
							//����DB����
							if(names != null && names.size()>0){
								for(int k=0;k<names.size();k++){
									String dbname = (String)names.get(k);
									if(database.get(dbname) != null){
										Hashtable db = (Hashtable)database.get(dbname);
										if (!oraspacehash.containsKey(ip+":"+dbname+":"+dbflag)){
											//�����ڸ����ݿ�,�����
											MySqlSpaceConfig = new MySqlSpaceConfig();
											MySqlSpaceConfig.setDbname(dbname);
											MySqlSpaceConfig.setBak("");
											MySqlSpaceConfig.setIpaddress(ip);
											MySqlSpaceConfig.setLinkuse("");	
											MySqlSpaceConfig.setAlarmvalue(90);
											MySqlSpaceConfig.setLogflag(0);
											MySqlSpaceConfig.setSms(new Integer(0));//0�����澯 1���澯��Ĭ�ϵ�����ǲ����Ͷ���
											MySqlSpaceConfig.setReportflag(new Integer(0));// 0���������ڱ��� 1�������ڱ���Ĭ�ϵ�����ǲ������ڱ���
											//configV.add(MySqlSpaceConfig);
											conn = new  DBManager();
											save(MySqlSpaceConfig);
											oraspacehash.put(ip+":"+dbname+":"+dbflag,MySqlSpaceConfig);																							
										}									
									}
								}
							}

							
							//����LOG����
							if(names != null && names.size()>0){
								for(int k=0;k<names.size();k++){
									String dbname = (String)names.get(k);
									if(logfile.get(dbname) != null){
										Hashtable db = (Hashtable)logfile.get(dbname);
										if (!oraspacehash.containsKey(ip+":"+dbname+":"+logflag)){
											//�����ڸ����ݿ�,�����
											MySqlSpaceConfig = new MySqlSpaceConfig();
											MySqlSpaceConfig.setDbname(dbname);
											MySqlSpaceConfig.setBak("");
											MySqlSpaceConfig.setIpaddress(ip);
											MySqlSpaceConfig.setLinkuse("");	
											MySqlSpaceConfig.setAlarmvalue(90);
											MySqlSpaceConfig.setLogflag(1);
											MySqlSpaceConfig.setSms(new Integer(0));//0�����澯 1���澯��Ĭ�ϵ�����ǲ����Ͷ���
											MySqlSpaceConfig.setReportflag(new Integer(0));// 0���������ڱ��� 1�������ڱ���Ĭ�ϵ�����ǲ������ڱ���
											conn = new  DBManager();
											save(MySqlSpaceConfig);
											oraspacehash.put(ip+":"+dbname+":"+dbflag,MySqlSpaceConfig);																							
										}									
									}
								}
							}																
						}
						
					}
				}
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		// TODO Auto-generated method stub
		}
		
		public void  deleteByIP(String ip){
			String sql="delete from system_mysqlspaceconf where ipaddress='"+ip+"'";
				try{
					conn.executeUpdate(sql);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}

}
