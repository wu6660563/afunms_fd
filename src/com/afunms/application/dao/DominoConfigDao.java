/**
 * <p>Description: app_db_node</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.afunms.application.model.DominoConfig;
import com.afunms.application.model.MQConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.system.model.User;

public class DominoConfigDao extends BaseDao implements DaoInterface {
	
	
	public DominoConfigDao() {
		super("nms_dominoconfig");
	}
	public boolean delete(String []ids){
		boolean result = true;
		try{
		if(ids!=null && ids.length>0){
			for(int i=0;i<ids.length;i++){
				   try
				   {
					   DominoConfig pvo = (DominoConfig)findByID(ids[i]);
					   String ipstr = pvo.getIpaddress();
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ipstr.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ipstr.indexOf(".")>0){
//							ip1=ipstr.substring(0,ipstr.indexOf("."));
//							ip4=ipstr.substring(ipstr.lastIndexOf(".")+1,ipstr.length());			
//							tempStr = ipstr.substring(ipstr.indexOf(".")+1,ipstr.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
					    String allipstr = SysUtil.doip(ipstr);
						conn = new DBManager();
						CreateTableManager ctable = new CreateTableManager();
			  			ctable.deleteTable(conn,"dominoping",allipstr,"dominoping");//Ping
			  			ctable.deleteTable(conn,"dominopinghour",allipstr,"dominopinghour");//Ping
			  			ctable.deleteTable(conn,"dominopingday",allipstr,"dominopingday");//Ping  
			  			
						ctable.deleteTable(conn,"dominocpu",allipstr,"cpu");//cpu
						ctable.deleteTable(conn,"dominocpuhour",allipstr,"dominocpuhour");//cpu
						ctable.deleteTable(conn,"dominocpuday",allipstr,"dominocpuday");//cpu 
						
						ctable.deleteTable(conn,"domstatus",allipstr,"domstatus");//״̬ IMAP/LDAP/POP3/SMTP
						ctable.deleteTable(conn,"domstatushour",allipstr,"domstatushour");//status
						ctable.deleteTable(conn,"domstatusday",allipstr,"domstatusday");//status 
						
						ctable.deleteTable(conn,"domservmem",allipstr,"domservmem");//�������ڴ�������
						ctable.deleteTable(conn,"domservmemhour",allipstr,"domservmemhour");//domservmem
						ctable.deleteTable(conn,"domservmemday",allipstr,"domservmemday");//domservmem 
						
						ctable.deleteTable(conn,"domplatmem",allipstr,"domplatmem");//ƽ̨�ڴ�������
						ctable.deleteTable(conn,"domplatmemhour",allipstr,"domplatmemhour");//domplatmem
						ctable.deleteTable(conn,"domplatmemday",allipstr,"domplatmemday");//domplatmem
						
			  			conn.addBatch("delete from nms_dominoconfig where id=" + ids[i]);
			  			conn.executeBatch();
			  			result = true;
				   }
				   catch(Exception e)
				   {
					   SysLogger.error("DominoConfigDao.delete()",e); 
				   }
				   finally
				   {
					   //conn.close();
				   }
			}
		}
		}catch(Exception e){			
		}finally{
			conn.close();
		}
		return result;
	}
	
	@Override
	public BaseVo findByID(String id)
	  {
	     BaseVo vo = null;
	     try
	     {
	        rs = conn.executeQuery("select * from nms_dominoconfig where id=" + id );
	        if(rs.next())
	           vo = loadFromRS(rs);
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("DominoConfigDao.findByID()",e);
	         vo = null;
	     }
	     finally
	     {
	        conn.close();
	     }
	     return vo;
	  } 
	
	
	public BaseVo loadFromRS(ResultSet rs) {
		DominoConfig vo=new DominoConfig();
		
		
			try {
				vo.setId(rs.getInt("id"));
				vo.setName(rs.getString("name"));
				vo.setIpaddress(rs.getString("ipaddress"));
				vo.setCommunity(rs.getString("community"));
				vo.setSendmobiles(rs.getString("sendmobiles"));
				vo.setMon_flag(rs.getInt("mon_flag"));
				vo.setNetid(rs.getString("netid"));
				vo.setSendemail(rs.getString("sendemail"));
				vo.setSendphone(rs.getString("sendphone"));
				vo.setSupperid(rs.getInt("supperid"));// snow add supperid at 2010-5-20
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return vo;
	}

	public boolean save(BaseVo vo) {
		boolean flag = true;
		DominoConfig vo1=(DominoConfig)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_dominoconfig(id,name,ipaddress,community,sendmobiles,mon_flag,netid,sendemail,sendphone,supperid) values(");
		sql.append(vo1.getId());
		sql.append(",'");
		sql.append(vo1.getName());
		sql.append("','");
		sql.append(vo1.getIpaddress());
		sql.append("','");
		sql.append(vo1.getCommunity());
		sql.append("','");
		sql.append(vo1.getSendmobiles());
		sql.append("',");
		sql.append(vo1.getMon_flag());
		sql.append(",'");
		sql.append(vo1.getNetid());
		sql.append("','");
		sql.append(vo1.getSendemail());
		sql.append("','");
		sql.append(vo1.getSendphone());
		sql.append("','");
		sql.append(vo1.getSupperid());
		sql.append("')");
		try{
			saveOrUpdate(sql.toString());
		   CreateTableManager ctable = new CreateTableManager();         
			//�������ɱ�
			String ip = vo1.getIpaddress();
//			String ip1 ="",ip2="",ip3="",ip4="";
//			String[] ipdot = ip.split(".");	
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".")>0){
//				ip1=ip.substring(0,ip.indexOf("."));
//				ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//				tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//			}
//			ip2=tempStr.substring(0,tempStr.indexOf("."));
//			ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//			allipstr=ip1+ip2+ip3+ip4;
			String allipstr = SysUtil.doip(ip);
			try{
				conn = new DBManager();
				ctable.createTable(conn,"dominoping",allipstr,"dominoping");//Ping
				ctable.createTable(conn,"dominopinghour",allipstr,"dominopinghour");//Ping
				ctable.createTable(conn,"dominopingday",allipstr,"dominopingday");//Ping 
				
				ctable.createTable(conn,"dominocpu",allipstr,"cpu");//cpu
				ctable.createTable(conn,"dominocpuhour",allipstr,"dominocpuhour");//cpu
				ctable.createTable(conn,"dominocpuday",allipstr,"dominocpuday");//cpu 
				
				ctable.createTable(conn,"domstatus",allipstr,"domstatus");//״̬ IMAP/LDAP/POP3/SMTP
				ctable.createTable(conn,"domstatushour",allipstr,"domstatushour");//status
				ctable.createTable(conn,"domstatusday",allipstr,"domstatusday");//status 
				
				ctable.createTable(conn,"domservmem",allipstr,"domservmem");//�������ڴ�������
				ctable.createTable(conn,"domservmemhour",allipstr,"domservmemhour");//domservmem
				ctable.createTable(conn,"domservmemday",allipstr,"domservmemday");//domservmem 
				
				ctable.createTable(conn,"domplatmem",allipstr,"domplatmem");//ƽ̨�ڴ�������
				ctable.createTable(conn,"domplatmemhour",allipstr,"domplatmemhour");//domplatmem
				ctable.createTable(conn,"domplatmemday",allipstr,"domplatmemday");//domplatmem
				
				ctable.createTable(conn,"domdisk",allipstr,"domdisk");//����������
				ctable.createTable(conn,"domdiskhour",allipstr,"domdiskhour");//disk
				ctable.createTable(conn,"domdiskmemday",allipstr,"domdiskday");//disk
				
			}catch(Exception e){
				
			}
			
			
		}catch(Exception e){
			flag = false;
		}finally{
			try{
				conn.executeBatch();
			}catch(Exception e){
				
			}
			conn.close();
		}
		return flag;
		
	}

	public boolean update(BaseVo vo) {
		boolean flag = true;
		DominoConfig vo1=(DominoConfig)vo;
		DominoConfig pvo = (DominoConfig)findByID(vo1.getId()+"");
		boolean result=false;
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_dominoconfig set name='");
		sql.append(vo1.getName());
		sql.append("',ipaddress='");
		sql.append(vo1.getIpaddress());
		sql.append("',community='");
		sql.append(vo1.getCommunity());
		sql.append("',sendmobiles='");
		sql.append(vo1.getSendmobiles());
		sql.append("',mon_flag=");
		sql.append(vo1.getMon_flag());
		sql.append(",netid='");
		sql.append(vo1.getNetid());
		sql.append("',sendemail='");
		sql.append(vo1.getSendemail());
		sql.append("',sendphone='");
		sql.append(vo1.getSendphone());
		sql.append("',supperid='");
		sql.append(vo1.getSupperid());
		sql.append("'where id=");
		sql.append(vo1.getId());
	    try {
	    	conn = new DBManager();
    		saveOrUpdate(sql.toString());
		
		if (!vo1.getIpaddress().equals(pvo.getIpaddress())){
           	//�޸���IP
				//��IP��ַ�����ı�,�Ȱѱ�ɾ����Ȼ�������½���
				String ipstr = pvo.getIpaddress();
//				String ip1 ="",ip2="",ip3="",ip4="";
//				String[] ipdot = ipstr.split(".");	
//				String tempStr = "";
//				String allipstr = "";
//				if (ipstr.indexOf(".")>0){
//					ip1=ipstr.substring(0,ipstr.indexOf("."));
//					ip4=ipstr.substring(ipstr.lastIndexOf(".")+1,ipstr.length());			
//					tempStr = ipstr.substring(ipstr.indexOf(".")+1,ipstr.lastIndexOf("."));
//				}
//				ip2=tempStr.substring(0,tempStr.indexOf("."));
//				ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//				allipstr=ip1+ip2+ip3+ip4;
			    String allipstr = SysUtil.doip(ipstr);
				conn = new DBManager();
				CreateTableManager ctable = new CreateTableManager();
       			ctable.deleteTable(conn,"dominoping",allipstr,"dominoping");//Ping
       			ctable.deleteTable(conn,"dominopinghour",allipstr,"dominopinghour");//Ping
       			ctable.deleteTable(conn,"dominopingday",allipstr,"dominopingday");//Ping  
       			
				ctable.deleteTable(conn,"dominocpu",allipstr,"cpu");//cpu
				ctable.deleteTable(conn,"dominocpuhour",allipstr,"dominocpuhour");//cpu
				ctable.deleteTable(conn,"dominocpuday",allipstr,"dominocpuday");//cpu 
				
				ctable.deleteTable(conn,"domstatus",allipstr,"domstatus");//״̬ IMAP/LDAP/POP3/SMTP
				ctable.deleteTable(conn,"domstatushour",allipstr,"domstatushour");//status
				ctable.deleteTable(conn,"domstatusday",allipstr,"domstatusday");//status 
				
				ctable.deleteTable(conn,"domservmem",allipstr,"domservmem");//�������ڴ�������
				ctable.deleteTable(conn,"domservmemhour",allipstr,"domservmemhour");//domservmem
				ctable.deleteTable(conn,"domservmemday",allipstr,"domservmemday");//domservmem 
				
				ctable.deleteTable(conn,"domplatmem",allipstr,"domplatmem");//ƽ̨�ڴ�������
				ctable.deleteTable(conn,"domplatmemhour",allipstr,"domplatmemhour");//domplatmem
				ctable.deleteTable(conn,"domplatmemday",allipstr,"domplatmemday");//domplatmem
                 			               
				ctable.deleteTable(conn,"domdisk",allipstr,"domdisk");//����������
				ctable.deleteTable(conn,"domdiskhour",allipstr,"domdiskhour");//disk
				ctable.deleteTable(conn,"domdiskmemday",allipstr,"domdiskday");//disk
				//�������ɱ�
				String ip = vo1.getIpaddress();
//				ip1 ="";ip2="";ip3="";ip4="";
//				ipdot = ip.split(".");	
//				tempStr = "";
//				allipstr = "";
//				if (ip.indexOf(".")>0){
//					ip1=ip.substring(0,ip.indexOf("."));
//					ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//					tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//				}
//				ip2=tempStr.substring(0,tempStr.indexOf("."));
//				ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//				allipstr=ip1+ip2+ip3+ip4;
			    allipstr = SysUtil.doip(ip);

				ctable = new CreateTableManager();
	    		ctable.createTable(conn,"dominoping",allipstr,"dominoping");//Ping
	    		ctable.createTable(conn,"dominopinghour",allipstr,"dominopinghour");//Ping
	    		ctable.createTable(conn,"dominopingday",allipstr,"dominopingday");//Ping
	    		
	    		ctable.createTable(conn,"dominocpu",allipstr,"cpu");//cpu
				ctable.createTable(conn,"dominocpuhour",allipstr,"dominocpuhour");//cpu
				ctable.createTable(conn,"dominocpuday",allipstr,"dominocpuday");//cpu 
				
				ctable.createTable(conn,"domstatus",allipstr,"domstatus");//״̬ IMAP/LDAP/POP3/SMTP
				ctable.createTable(conn,"domstatushour",allipstr,"domstatushour");//status
				ctable.createTable(conn,"domstatusday",allipstr,"domstatusday");//status 
				
				ctable.createTable(conn,"domservmem",allipstr,"domservmem");//�������ڴ�������
				ctable.createTable(conn,"domservmemhour",allipstr,"domservmemhour");//domservmem
				ctable.createTable(conn,"domservmemday",allipstr,"domservmemday");//domservmem 
				
				ctable.createTable(conn,"domplatmem",allipstr,"domplatmem");//ƽ̨�ڴ�������
				ctable.createTable(conn,"domplatmemhour",allipstr,"domplatmemhour");//domplatmem
				ctable.createTable(conn,"domplatmemday",allipstr,"domplatmemday");//domplatmem
                
				ctable.createTable(conn,"domdisk",allipstr,"domdisk");//����������
				ctable.createTable(conn,"domdiskhour",allipstr,"domdiskhour");//disk
				ctable.createTable(conn,"domdiskmemday",allipstr,"domdiskday");//disk
		}
		
	    } catch (Exception e) {
			flag=false;
			e.printStackTrace();
	    }finally{
			try{
				conn.executeBatch();
			}catch(Exception e){
				
			}
	    	conn.close();
	    }
	    return flag;
	}

	   public List getDominoByBID(Vector bids){
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
		   sql.append("select * from nms_dominoconfig "+wstr);
		   //SysLogger.info(sql.toString());
		   return findByCriteria(sql.toString());
	   }
	   
	   public List getDominoByFlag(int flag){
		   List rlist = new ArrayList();
		   StringBuffer sql = new StringBuffer();
		   sql.append("select * from nms_dominoconfig where mon_flag = "+flag);
		   return findByCriteria(sql.toString());
	   }
	   
		 //����Ping�õ������ݣ��ŵ���ʷ����
		public synchronized boolean createHostData(Pingcollectdata pingdata) {
			if (pingdata == null )
				return false;	
			//SysLogger.info(pingdata.getIpaddress()+"==============");
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
				//for (int i = 0; i < hostdatavec.size(); i++) {
					//Pingcollectdata pingdata = (Pingcollectdata)hostdatavec.elementAt(i);	
					String ip = pingdata.getIpaddress();				
					if (pingdata.getRestype().equals("dynamic")) {						
						String ip1 ="",ip2="",ip3="",ip4="";
						String[] ipdot = ip.split(".");	
						String tempStr = "";
						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						allipstr = SysUtil.doip(ip);
				
						Calendar tempCal = (Calendar)pingdata.getCollecttime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";
						String type=pingdata.getCategory();
						tablename = "dominoping"+allipstr;
						String sql = "insert into "+tablename+"(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
								+"values('"+ip+"','"+pingdata.getRestype()+"','"+pingdata.getCategory()+"','"+pingdata.getEntity()+"','"
								+pingdata.getSubentity()+"','"+pingdata.getUnit()+"','"+pingdata.getChname()+"','"+pingdata.getBak()+"',"
								+pingdata.getCount()+",'"+pingdata.getThevalue()+"','"+time+"')";
						
						conn.executeUpdate(sql);
																										
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				
			}
			return true;
		}
}
