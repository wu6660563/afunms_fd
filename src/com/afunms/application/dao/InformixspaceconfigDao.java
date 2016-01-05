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

public class InformixspaceconfigDao extends BaseDao implements DaoInterface{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public InformixspaceconfigDao() {
		super("system_infomixspaceconf");
	}
	
	public boolean save(BaseVo baseVo)
	{
		Informixspaceconfig vo = (Informixspaceconfig)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_infomixspaceconf(ipaddress,spacename,linkuse,sms,bak,reportflag,alarmvalue)values(");
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
		sql.append(")");
		//SysLogger.info(sql.toString());
		return saveOrUpdate(sql.toString());
	}
	
	  public boolean update(BaseVo baseVo)
	  {
		  Informixspaceconfig vo = (Informixspaceconfig)baseVo;
			boolean result = false;
			
			StringBuffer sql = new StringBuffer();
			sql.append("update system_infomixspaceconf set ipaddress='");
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
			sql.append(" where id=");
			sql.append(vo.getId());
	     
	     try
	     {
	         conn.executeUpdate(sql.toString());
	         result = true;
	     }
	     catch(Exception e)
	     {
	    	 result = false;
	         SysLogger.error("InformixspaceconfigDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }  
	     
	     return result;
	  }
	  
	   public BaseVo loadFromRS(ResultSet rs)
	   {
		   Informixspaceconfig vo = new Informixspaceconfig();
	      try
	      {
	          vo.setId(rs.getInt("id"));
	          vo.setIpaddress(rs.getString("ipaddress"));
	          vo.setSpacename(rs.getString("spacename"));
	          vo.setLinkuse(rs.getString("linkuse"));
	          vo.setAlarmvalue(rs.getInt("alarmvalue"));
	          vo.setBak(rs.getString("bak"));
	          vo.setReportflag(rs.getInt("reportflag"));
	          vo.setSms(rs.getInt("sms"));
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
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
				rs = conn.executeQuery("select * from system_infomixspaceconf where sms="+smsflag+" order by ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Informixspaceconfig informixspaceconfig = (Informixspaceconfig)list.get(i);					
						retValue.put(informixspaceconfig.getIpaddress()+":"+informixspaceconfig.getSpacename(), informixspaceconfig);					
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
		public List getByIp(String ipaddress,Integer smsflag) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_infomixspaceconf where ipaddress = '"+ipaddress+"' and sms="+smsflag+" order by ipaddress");
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
		 * ����IP��ѯ
		 * 
		 */
		public List getByIp(String ipaddress) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_infomixspaceconf where ipaddress = '"+ipaddress+"' order by ipaddress");
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
		 * ���ڴ�����ݿ�����ȡÿ��Informix���ݿ��ռ���Ϣ�������ռ����ñ���
		 */
		public void fromLastToInformixspaceconfig()
		throws Exception {		
			List list=new ArrayList();
			List list1=new ArrayList();
			List shareList = new ArrayList();
			Hashtable informixspacehash= new Hashtable();
			Session session=null;
			Vector configV = new Vector();
			try{
				//��INFORMIXSPACCE���ñ����ȡ�б�
				rs = conn.executeQuery("select * from system_infomixspaceconf order by ipaddress");
				while(rs.next())
		        	list1.add(loadFromRS(rs)); 	
				if(list1!=null && list1.size()>0){
					for(int i=0;i<list1.size();i++){
						Informixspaceconfig informixspaceconfig = (Informixspaceconfig)list1.get(i);	
						//IP:��ռ�����				
						informixspacehash.put(informixspaceconfig.getIpaddress()+":"+informixspaceconfig.getSpacename(),informixspaceconfig);					
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			try{

			//���ڴ��еõ�����INFORMIXSPACCE�ɼ���Ϣ
			Hashtable sharedata = ShareData.getInformixspacedata();
			
			//�����ݿ�õ�����INFORMIXSPACCE�б�
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = typedao.findByDbtype("informix");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			DBDao dbdao = new DBDao();
			try{
				shareList = dbdao.getDbByTypeMonFlag(typevo.getId(), 1);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dbdao.close();
			}
			if (shareList != null && shareList.size()>0){
				for(int i=0;i<shareList.size();i++){
					DBVo dbmonitorlist = (DBVo)shareList.get(i);				
					if(sharedata.get(dbmonitorlist.getIpAddress()) != null){
						List tableinfo_v = (List)sharedata.get(dbmonitorlist.getIpAddress());
						if (tableinfo_v == null )continue;
						//SysLogger.info("###############");
						Hashtable spaces = new Hashtable();
						spaces.put("ip", dbmonitorlist.getIpAddress());
						spaces.put("tableinfo_v", tableinfo_v);
						list.add(spaces);
					}
				}
			}
			//�жϲɼ�����INFROMIXSPACCE��Ϣ�Ƿ��Ѿ���INFORMIXSPACCE���ñ����Ѿ����ڣ��������������
			if (list != null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Hashtable spaces = (Hashtable)list.get(i);
					if(spaces != null && spaces.size()>0){
						String ip = (String)spaces.get("ip");					
						List tableinfo_v = (List)spaces.get("tableinfo_v");	
						//SysLogger.info("&&&&&&&&&&&&&");
						if(tableinfo_v != null && tableinfo_v.size()>0){
							Informixspaceconfig informixspaceconfig = null;
							for(int k=0;k<tableinfo_v.size();k++){
								Hashtable return_value = (Hashtable)tableinfo_v.get(k);
								String spacename = (String)return_value.get("dbspace");
								if (!informixspacehash.containsKey(ip+":"+spacename)){					
									informixspaceconfig = new Informixspaceconfig();
									informixspaceconfig.setSpacename(spacename);
									informixspaceconfig.setBak("");
									informixspaceconfig.setIpaddress(ip);
									informixspaceconfig.setLinkuse("");	
									informixspaceconfig.setAlarmvalue(90);
									informixspaceconfig.setSms(new Integer(0));//0�����澯 1���澯��Ĭ�ϵ�����ǲ����Ͷ���
									informixspaceconfig.setReportflag(new Integer(0));// 0���������ڱ��� 1�������ڱ���Ĭ�ϵ�����ǲ������ڱ���
									//if(conn)
												
									try{
										conn.close();
										conn = new  DBManager();
										save(informixspaceconfig);
										
										informixspacehash.put(ip+":"+spacename,informixspaceconfig);
									}catch(Exception e){
										e.printStackTrace();
									}
														
								}													
							}
							conn.close();
						}					
					}
				}
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

   
public void  deleteByIP(String ip){
	String sql="delete from system_infomixspaceconf where ipaddress='"+ip+"'";
		try{
			conn.executeUpdate(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}   