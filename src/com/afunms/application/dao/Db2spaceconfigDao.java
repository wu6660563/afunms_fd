/**
 * <p>Description: app_db_node</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.model.*;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;
import com.afunms.event.model.AlarmInfo;
import com.afunms.polling.om.*;
import com.afunms.polling.task.*;
public class Db2spaceconfigDao extends BaseDao implements DaoInterface{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Db2spaceconfigDao() {
		super("system_db2spaceconf");
	}
	
	public boolean save(BaseVo baseVo)
	{
		Db2spaceconfig vo = (Db2spaceconfig)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_db2spaceconf(ipaddress,spacename,linkuse,sms,bak,reportflag,alarmvalue,dbname)values(");
		sql.append("'");
		sql.append(vo.getIpaddress());
		sql.append("','");
		sql.append(vo.getSpacename());	
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
		sql.append(",'");
		sql.append(vo.getDbname());
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}
	
	  public boolean update(BaseVo baseVo)
	  {
		  Db2spaceconfig vo = (Db2spaceconfig)baseVo;
			boolean result = false;
			
			StringBuffer sql = new StringBuffer();
			sql.append("update system_db2spaceconf set ipaddress='");
			sql.append(vo.getIpaddress());
			sql.append("',spacename='");
			sql.append(vo.getSpacename());	
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
			sql.append(",dbname='");
			sql.append(vo.getDbname());
			sql.append("' where id=");
			sql.append(vo.getId());
	     
	     try
	     {
	         conn.executeUpdate(sql.toString());
	         result = true;
	     }
	     catch(Exception e)
	     {
	    	 result = false;
	         SysLogger.error("Db2spaceconfigDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }  
	     
	     return result;
	  }
	  
	   public BaseVo loadFromRS(ResultSet rs)
	   {
		   Db2spaceconfig vo = new Db2spaceconfig();
	      try
	      {
	          vo.setId(rs.getInt("id"));
	          vo.setIpaddress(rs.getString("ipaddress"));
	          vo.setSpacename(rs.getString("spacename"));
	          vo.setDbname(rs.getString("dbname"));
	          vo.setLinkuse(rs.getString("linkuse"));
	          vo.setAlarmvalue(rs.getInt("alarmvalue"));
	          vo.setBak(rs.getString("bak"));
	          vo.setReportflag(rs.getInt("reportflag"));
	          vo.setSms(rs.getInt("sms"));
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
		 * ����IP���Ƿ�Ҫ��ʾ���ձ���ı�־λ��ѯ
		 * 
		 */
		public Hashtable getByAlarmflag(Integer smsflag) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_db2spaceconf where sms="+smsflag+" order by ipaddress");
				//Query query=session.createQuery("from Oraspaceconfig oraspaceconfig where oraspaceconfig.sms="+smsflag+" order by oraspaceconfig.ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Db2spaceconfig oraspaceconfig = (Db2spaceconfig)list.get(i);					
						retValue.put(oraspaceconfig.getIpaddress()+":"+oraspaceconfig.getSpacename(), oraspaceconfig);					
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
		 * ����IP��ѯ
		 * 
		 */
		public List getByIp(String ipaddress) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_db2spaceconf where ipaddress = '"+ipaddress+"' order by ipaddress");
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
		 * 
		 * ���ڴ�����ݿ�����ȡÿ��IP�Ķ˿���Ϣ������˿����ñ���
		 */
		public void fromLastToDb2spaceconfig()
		throws Exception {		
			List list=new ArrayList();
			List list1=new ArrayList();
			List shareList = new ArrayList();
			Hashtable oraspacehash= new Hashtable();
			Session session=null;
			Db2spaceconfig db2spaceconfig = null;
			Vector configV = new Vector();
			try{
			
			//��DB2SPACCE���ñ����ȡ�б�
				rs = conn.executeQuery("select * from system_db2spaceconf order by ipaddress");
				while(rs.next())
		        	list1.add(loadFromRS(rs));
			
			if (list1 != null && list1.size()>0){			
				for(int i=0;i<list1.size();i++){
					db2spaceconfig = (Db2spaceconfig)list1.get(i);	
					//IP:���ݿ���:��ռ�����				
					oraspacehash.put(db2spaceconfig.getIpaddress()+":"+db2spaceconfig.getDbname()+":"+db2spaceconfig.getSpacename(),db2spaceconfig);
				}
			}
			
			
			//���ڴ��еõ�����DB2SPACCE�ɼ���Ϣ
			Hashtable sharedata = ShareData.getDb2type6spacedata();
			
			//�����ݿ�õ�����DB2SPACCE�б�
			DBDao dbdao = new DBDao();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = typedao.findByDbtype("db2");
			shareList = dbdao.getDbByTypeMonFlag(typevo.getId(), 1);
			//shareList = dbmonitorlistManager.getDbByTypeMonFlag("db2",1);
			if (shareList != null && shareList.size()>0){
				for(int i=0;i<shareList.size();i++){
					DBVo dbmonitorlist = (DBVo)shareList.get(i);				
					if(sharedata.get(dbmonitorlist.getIpAddress()) != null){
						Hashtable alltype6spacedata = (Hashtable)sharedata.get(dbmonitorlist.getIpAddress());
						if (alltype6spacedata == null )continue;
						Hashtable spaces = new Hashtable();
						spaces.put("dbmonitorlist", dbmonitorlist);
						spaces.put("alltype6spacedata", alltype6spacedata);
						list.add(spaces);
						
						/*
						List serverlist = (List)weblogicData.get("serverValue");
						if (serverlist !=null && serverlist.size()>0){
							for(int k=0;k<serverlist.size();k++){
								WeblogicServer server = (WeblogicServer)serverlist.get(k);
								server.setIpaddress(weblogicconf.getIpaddress());
								list.add(server);								
							}
						}
						*/
					}
				}
			}
			//�жϲɼ�����DB2SPACCE��Ϣ�Ƿ��Ѿ���DB2SPACCE���ñ����Ѿ����ڣ��������������
			if (list != null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Hashtable spaces = (Hashtable)list.get(i);				
					if(spaces != null && spaces.size()>0){
						DBVo dbmonitorlist = (DBVo)spaces.get("dbmonitorlist");
						String ip = dbmonitorlist.getIpAddress();		
						Hashtable alltype6spacedata = (Hashtable)spaces.get("alltype6spacedata");		
						if(alltype6spacedata != null && alltype6spacedata.size()>0){
							Hashtable iptype6spacedata = (Hashtable)alltype6spacedata.get(ip);
							if(iptype6spacedata!= null && iptype6spacedata.size()>0){
								//����DBStr�ж��Ƿ����
								String[] alldbs = dbmonitorlist.getDbName().split(",");							
								if(alldbs != null && alldbs.length>0){
									for(int j=0;j<alldbs.length;j++){
										if(iptype6spacedata.get(alldbs[j])!= null){
											List dbstrspaces = (List)iptype6spacedata.get(alldbs[j]);										
											if(dbstrspaces != null && dbstrspaces.size()>0){											
												for(int m=0;m<dbstrspaces.size();m++){												
													Hashtable dbspace = (Hashtable)dbstrspaces.get(m);
													String spacename = (String)dbspace.get("tablespace_name");
													spacename = spacename.trim();
													if (!oraspacehash.containsKey(ip+":"+alldbs[j]+":"+spacename)){													
														db2spaceconfig = new Db2spaceconfig();
														db2spaceconfig.setSpacename(spacename.trim());
														db2spaceconfig.setDbname(alldbs[j]);
														db2spaceconfig.setBak("");
														db2spaceconfig.setIpaddress(ip);
														db2spaceconfig.setLinkuse("");	
														db2spaceconfig.setAlarmvalue(90);
														db2spaceconfig.setSms(new Integer(0));//0�����澯 1���澯��Ĭ�ϵ�����ǲ����Ͷ���
														db2spaceconfig.setReportflag(new Integer(0));// 0���������ڱ��� 1�������ڱ���Ĭ�ϵ�����ǲ������ڱ���
														conn = new  DBManager();			
														save(db2spaceconfig);													
														oraspacehash.put(ip+":"+alldbs[j]+":"+spacename,db2spaceconfig);					
													}																									
												}
											}
											
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
		//zhushouzhi--------------new getbyip(ip,flag)
		public List getByIp(String ipaddress,Integer smsflag) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_db2spaceconf where ipaddress = '"+ipaddress+"' and sms="+smsflag+" order by ipaddress");
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
		//zhushouzhi----------------------end
		public void  deleteByIP(String ip){
			String sql="delete from system_db2spaceconf where ipaddress='"+ip+"'";
				try{
					conn.executeUpdate(sql);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
}   