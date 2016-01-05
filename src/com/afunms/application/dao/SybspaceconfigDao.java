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
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;
import com.afunms.event.model.AlarmInfo;
import com.afunms.polling.om.*;
import com.afunms.polling.task.*;
public class SybspaceconfigDao extends BaseDao implements DaoInterface{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SybspaceconfigDao() {
		super("system_sybspaceconf");
	}
	
	public boolean save(BaseVo baseVo)
	{
		Sybspaceconfig vo = (Sybspaceconfig)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_sybspaceconf(ipaddress,spacename,linkuse,sms,bak,reportflag,alarmvalue)values(");
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
		return saveOrUpdate(sql.toString());
	}
	
	  public boolean update(BaseVo baseVo)
	  {
		  Sybspaceconfig vo = (Sybspaceconfig)baseVo;
			boolean result = false;
			
			StringBuffer sql = new StringBuffer();
			sql.append("update system_sybspaceconf set ipaddress='");
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
	         SysLogger.error("SybspaceconfigDao:update()",e);
	     }
	     finally
	     {
	    	 conn.close();
	     }  
	     
	     return result;
	  }
	  
	   public BaseVo loadFromRS(ResultSet rs)
	   {
		   Sybspaceconfig vo = new Sybspaceconfig();
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
				rs = conn.executeQuery("select * from system_sybspaceconf where sms="+smsflag+" order by ipaddress");
				//Query query=session.createQuery("from Oraspaceconfig oraspaceconfig where oraspaceconfig.sms="+smsflag+" order by oraspaceconfig.ipaddress");
				while(rs.next())
		        	list.add(loadFromRS(rs)); 	
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Sybspaceconfig oraspaceconfig = (Sybspaceconfig)list.get(i);					
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
		public List getByIp(String ipaddress,Integer smsflag) {
			List list = new ArrayList();
			Session session=null;
			Hashtable retValue = new Hashtable();
			try{
				rs = conn.executeQuery("select * from system_sybspaceconf where ipaddress = '"+ipaddress+"' and sms="+smsflag+" order by ipaddress");
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
				rs = conn.executeQuery("select * from system_sybspaceconf where ipaddress = '"+ipaddress+"' order by ipaddress");
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
		 * ���ڴ��������ݿ��ռ����ݴ���˿����ñ���
		 */
		public void fromLastToSybspaceconfig()
		throws Exception {		
			List list=new ArrayList();
			List list1=new ArrayList();
			List shareList = new ArrayList();
			Hashtable oraspacehash= new Hashtable();
			Session session=null;
			Sybspaceconfig sybspaceconfig = null;
			Vector configV = new Vector();
			try{
			
			//��SYBSPACCE���ñ����ȡ�б�
				rs = conn.executeQuery("select * from system_sybspaceconf order by ipaddress");
				while(rs.next())
		        	list1.add(loadFromRS(rs)); 
			
			if (list1 != null && list1.size()>0){			
				for(int i=0;i<list1.size();i++){
					sybspaceconfig = (Sybspaceconfig)list1.get(i);	
					//IP:��ռ�����				
					oraspacehash.put(sybspaceconfig.getIpaddress()+":"+sybspaceconfig.getSpacename(),sybspaceconfig);
				}
			}
			
			
			//���ڴ��еõ�����SYBSPACCE�ɼ���Ϣ
//			Hashtable sharedata = ShareData.getSysbasedata();
			
			/*//�����ݿ�õ�����SYBSPACCE�б�
			DBDao dbdao = new DBDao();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = typedao.findByDbtype("sybase");
			shareList = dbdao.getDbByTypeMonFlag(typevo.getId(), 1);
			if (shareList != null && shareList.size()>0){
				for(int i=0;i<shareList.size();i++){
					DBVo dbmonitorlist = (DBVo)shareList.get(i);				
					if(sharedata.get(dbmonitorlist.getIpAddress()) != null){
						Hashtable sysbaseVOHash = (Hashtable)sharedata.get(dbmonitorlist.getIpAddress());
						if(sysbaseVOHash.get("sysbaseVO")!=null){
							SybaseVO sysbaseVO = (SybaseVO)sysbaseVOHash.get("sysbaseVO");
							if(sysbaseVO != null){
								Hashtable spaces = new Hashtable();
								spaces.put("ip", dbmonitorlist.getIpAddress());
								spaces.put("sysbaseVO", sysbaseVO);							
								list.add(spaces);
							}
						}
					}
				}
			}*/
			//�����ݿ�õ�����SYBSPACCE�б�
			DBDao dbdao = new DBDao();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = typedao.findByDbtype("sybase");
			shareList = dbdao.getDbByTypeMonFlag(typevo.getId(), 1);
			if (shareList != null && shareList.size()>0){
				for(int i=0;i<shareList.size();i++){
					DBVo dbmonitorlist = (DBVo)shareList.get(i);				
					//��ȡsybase��Ϣ
					SybaseVO sysbaseVO = new SybaseVO();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbmonitorlist.getIpAddress());
					String serverip = hex+":"+dbmonitorlist.getId();
					sysbaseVO = dbdao.getSybaseDataByServerip(serverip); 
					if(sysbaseVO != null){
						Hashtable spaces = new Hashtable();
						spaces.put("ip", dbmonitorlist.getIpAddress());
						spaces.put("sysbaseVO", sysbaseVO);							
						list.add(spaces);
					}
				}
			}
			if(dbdao != null){
				dbdao.close();
			}
			//�жϲɼ�����ORASPACCE��Ϣ�Ƿ��Ѿ���ORASPACCE���ñ����Ѿ����ڣ��������������
			if (list != null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Hashtable spaces = (Hashtable)list.get(i);
					if(spaces != null && spaces.size()>0){
						String ip = (String)spaces.get("ip");					
						SybaseVO sysbaseVO = (SybaseVO)spaces.get("sysbaseVO");		
						if(sysbaseVO != null ){
							List ipallspace = sysbaseVO.getDbInfo();
							if(ipallspace == null)ipallspace = new ArrayList();
							for(int k=0;k<ipallspace.size();k++){
								TablesVO tvo = (TablesVO)ipallspace.get(k);
								String spacename = tvo.getDb_name();
								if (!oraspacehash.containsKey(ip+":"+spacename)){					
									sybspaceconfig = new Sybspaceconfig();
									sybspaceconfig.setSpacename(spacename);
									sybspaceconfig.setBak("");
									sybspaceconfig.setIpaddress(ip);
									sybspaceconfig.setLinkuse("");	
									sybspaceconfig.setAlarmvalue(90);
									sybspaceconfig.setSms(new Integer(0));//0�����澯 1���澯��Ĭ�ϵ�����ǲ����Ͷ���
									sybspaceconfig.setReportflag(new Integer(0));// 0���������ڱ��� 1�������ڱ���Ĭ�ϵ�����ǲ������ڱ���
									conn = new  DBManager();			
									save(sybspaceconfig);
									//configV.add(sybspaceconfig);
									oraspacehash.put(ip+":"+spacename,sybspaceconfig);					
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
			String sql="delete from system_sybspaceconf where ipaddress='"+ip+"'";
				try{
					conn.executeUpdate(sql);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
}   