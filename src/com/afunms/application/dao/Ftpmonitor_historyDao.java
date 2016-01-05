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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;



import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;

import com.afunms.application.model.*;

public class Ftpmonitor_historyDao extends BaseDao implements DaoInterface
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   public Ftpmonitor_historyDao()
   {
	   super("nms_ftp_history");
   }

   public boolean update(BaseVo baseVo)
   {
	   return false;
   }
   
   public boolean save(BaseVo baseVo)
   {
	   Ftpmonitor_history vo = (Ftpmonitor_history)baseVo;	  
	   Calendar tempCal = (Calendar)vo.getMon_time();	
	  
	   Date cc = tempCal.getTime();
	   String time = sdf.format(cc);
	   StringBuffer sql = new StringBuffer();
	   sql.append("insert into nms_ftp_history(ftp_id,is_canconnected,reason,mon_time)values(");
	   sql.append("'");
	   sql.append(vo.getFtp_id());
	   sql.append("','");
	   sql.append(vo.getIs_canconnected());
	   sql.append("','");
	   sql.append(vo.getReason());
	   sql.append("','");
	   sql.append(time);
	   sql.append("'");
	   sql.append(")");
	   
	   return saveOrUpdate(sql.toString());
   }
   
   public boolean delete(String id)
   {
	   boolean result = false;
	   try
	   {
		   conn.addBatch("delete from nms_ftp_history where id=" + id);
		   conn.executeBatch();
		   result = true;
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("Ftpmonitor_historyDao.delete()",e); 
	   }
	   finally
	   {
		   conn.close();
	   }
	   return result;
   }
   /*
   public String[] getAvailability(Integer url_id,InitCoordinate initer,String type)throws Exception{
		String[] value={"",""};
		String starttime=initer.getBefore();
		String totime=initer.getNow();
		try {
			
			String parm=" aa.mon_time >= '";
			parm=parm+starttime;
			parm=parm+"' and aa.mon_time <= '";
			parm=parm+totime;
			parm=parm+"'";
			String sql = "select sum(aa."+type+") as stype ,COUNT(aa.url_id) as countid from nms_web_history aa where aa.url_id="+url_id+" and "+parm;
			rs = conn.executeQuery(sql);
			while(rs.next()){
				value[0] = rs.getInt("stype")+"";
				value[1] = rs.getInt("countid")+"";
				value[1]=new Integer(new Integer(value[1]).intValue()-new Integer(value[0]).intValue()).toString();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		}
		*/
   /*
	public String[] getAvailability(Integer url_id,String starttime,String totime,String type)throws Exception{
		String[] value={"",""};
		try {
			String parm=" aa.mon_time >= '";
			parm=parm+starttime;
			parm=parm+"' and aa.mon_time <= '";
			parm=parm+totime;
			parm=parm+"'";
			String sql = "select sum(aa."+type+") as stype ,COUNT(aa.url_id) as countid from nms_web_history aa where aa.url_id="+url_id+" and "+parm;
			rs = conn.executeQuery(sql);
			while(rs.next()){
				value[0] = rs.getInt("stype")+"";
				value[1] = rs.getInt("countid")+"";
				value[1]=new Integer(new Integer(value[1]).intValue()-new Integer(value[0]).intValue()).toString();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		}   
		
		*/
	//��flag = 0 ʱ,��ȡ�����Ƿ�����Ϊ0/1,��flag = 1ʱ,ȡ��������Ϊ���ĵ��Ƿ����ӳɹ�
	/*
	public Vector getByUrlid(Integer urlid,String starttime,String totime,int flag) throws Exception{
		List list = new ArrayList();
   	Vector returnVal = new Vector();
   	Session session =null;
   	try{
   		WebConfigDao configdao = new WebConfigDao();
   		WebConfig webconfig = (WebConfig)configdao.findByID(urlid+"");
   		
   		String sql = "select a.is_canconnected,a.is_valid,a.is_refresh," +
   					"a.reason,a.mon_time,a.condelay from nms_web_history a where " +
   					"a.url_id="+urlid+" and (a.mon_time >= '"+starttime +"' and  a.mon_time <= '"+totime+"')";
   		rs = conn.executeQuery(sql);
		while(rs.next()){
			Object[] obj = new Object[7];
			Hashtable ht = new Hashtable();
			if (flag ==0){
				obj[0] = rs.getInt("is_canconnected")+"";
				obj[1] = rs.getInt("is_valid")+"";
				obj[2] = rs.getInt("is_refresh")+"";
			}else{
				if(0==(rs.getInt("is_canconnected"))){
					obj[0] = "����ʧ��";
				}else{
					obj[0] = "���ӳɹ�";
				}
				if(0==(rs.getInt("is_valid"))){
					obj[1] ="������Ч";
				}else{
					obj[1] = "������Ч";
				}				
				if(0==(rs.getInt("is_refresh"))){
					obj[2] = "δˢ��";
				}else{
					obj[2] = "ҳ��ˢ��";
				}
			}
			obj[3] = rs.getString("reason");
				Calendar cal = Calendar.getInstance();
		       Date newdate = new Date();
		       newdate.setTime(rs.getTimestamp("mon_time").getTime());
		       cal.setTime(newdate);
		       obj[4] = sdf.format(cal.getTime());
		       obj[5] = rs.getInt("condelay");
		       obj[6] = webconfig.getName();
				ht.put("conn",obj[0]);
				ht.put("valid",obj[1]);
				ht.put("refresh",obj[2]);
				ht.put("reason",obj[3]);
				ht.put("mon_time",obj[4]);
				if (obj[5] == null)obj[5]="0";
				ht.put("condelay",obj[5]);
				ht.put("url_name",obj[6]);
				returnVal.addElement(ht);
				ht = null;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
		
	}
	*/
	public Vector getByFTPid(Integer ftpid,String starttime,String totime,Integer isconnected) throws Exception{
		List list = new ArrayList();
    	Vector returnVal = new Vector();
    	try{
    		String sql="select a.is_canconnected,a.reason,a.mon_time from nms_ftp_history a where " +
					"a.ftp_id="+ftpid+" and (a.mon_time >= '"+starttime +"' and  a.mon_time <= '"+totime+"')";
//			System.out.println(sql);
//			SysLogger.info(sql);
			rs = conn.executeQuery(sql);
			while(rs.next()){
				Object[] obj = new Object[3];
				obj[0] = rs.getString("is_canconnected");
				Hashtable ht = new Hashtable();
				/*
				if("0".equals(obj[0].toString())){
					obj[0] = "����ʧ��";
				}else{
					obj[0] = "����ɹ�";
				}
				*/
				obj[1] = rs.getString("reason");	
				
				Calendar cal = Calendar.getInstance();
			       Date newdate = new Date();
			       newdate.setTime(rs.getTimestamp("mon_time").getTime());
			       cal.setTime(newdate);
			       obj[2] = sdf.format(cal.getTime());
				
				
				//Calendar c = (Calendar)obj[2];
				//obj[2] = sdf.format(c.getTime());
				ht.put("conn",obj[0]);
				ht.put("reason",obj[1]);
				ht.put("mon_time",obj[2]);
				returnVal.addElement(ht);
				ht = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public String[] getAvailability(Integer ftp_id,String starttime,String totime,String type)throws Exception{
		String[] value={"",""};
		try {
			String parm=" aa.mon_time >= '";
			parm=parm+starttime;
			parm=parm+"' and aa.mon_time <= '";
			parm=parm+totime;
			parm=parm+"'";
			String sql = "select sum(aa."+type+") as stype ,COUNT(aa.ftp_id) as countid from nms_ftp_history aa where aa.ftp_id="+ftp_id+" and "+parm;
//			System.out.println(sql);
			rs = conn.executeQuery(sql);
			while(rs.next()){
				value[0] = rs.getInt("stype")+"";
				value[1] = rs.getInt("countid")+"";
				value[1]=new Integer(new Integer(value[1]).intValue()-new Integer(value[0]).intValue()).toString();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		}
	
	
   public BaseVo loadFromRS(ResultSet rs)
   {
	   Ftpmonitor_history vo = new Ftpmonitor_history();
       try
       {
		   vo.setId(rs.getInt("id"));
		   vo.setIs_canconnected(rs.getInt("is_canconnected"));
		   Calendar cal = Calendar.getInstance();
	       Date newdate = new Date();
	       newdate.setTime(rs.getTimestamp("mon_time").getTime());
	       cal.setTime(newdate);
		   vo.setMon_time(cal);
		   vo.setReason(rs.getString("reason"));
		   vo.setFtp_id(rs.getInt("ftp_id"));
       }
       catch(Exception e)
       {
   	       SysLogger.error("Urlmonitor_historyDao.loadFromRS()",e); 
       }	   
       return vo;
   }
   //yangjun add
   public Hashtable getPingData(Integer ftp_id,String starttime,String endtime) {
		Hashtable hash = new Hashtable();
		if (!starttime.equals("") && !endtime.equals("")) {
			List list1 = new ArrayList();
			String sql = "select a.is_canconnected,a.reason,a.mon_time from nms_ftp_history a where " +
				         "a.ftp_id="+ftp_id+" and (a.mon_time >= '"+starttime +"' and  a.mon_time <= '"+endtime+"')";;
//			SysLogger.info(sql);
			int i = 0;
			double avgput1 = 0;
			rs = conn.executeQuery(sql);
			try {
				while (rs.next()) {
					i = i + 1;
					Vector v = new Vector();
					String thevalue = rs.getString("is_canconnected");
					String collecttime = rs.getString("mon_time");
					String reason = rs.getString("reason");
					v.add(0, thevalue);
					v.add(1, collecttime);
					v.add(2, reason);
					avgput1 = avgput1 + Float.parseFloat(thevalue);
					list1.add(v);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			hash.put("list", list1);
			if (list1 != null && list1.size() > 0) {
				hash.put("avgput1", CEIString.round(avgput1/ list1.size(), 2)+"");
			} else {
				hash.put("avgput1", "0");
			}
		}
		return hash;
	}
   public Hashtable getPingDataById(Integer ftp_id,String starttime,String endtime) {
	   Hashtable hash = new Hashtable();
	   if (!starttime.equals("") && !endtime.equals("")) {
		   List list1 = new ArrayList();
		   String sql = "select a.is_canconnected,a.reason,a.mon_time from nms_ftp_history a where " +
		   "a.ftp_id="+ftp_id+" and (a.mon_time >= '"+starttime +"' and  a.mon_time <= '"+endtime+"') order by id";;
//		   SysLogger.info(sql);
		   int i = 0;
		   double curPing=0;
		   double avgPing = 0;
		  double  minPing=0;
		   rs = conn.executeQuery(sql);
		   try {
			   while (rs.next()) {
				   i = i + 1;
				   Vector v = new Vector();
				   String thevalue = rs.getString("is_canconnected");
				   String collecttime = rs.getString("mon_time");
				  // String reason = rs.getString("reason");
				   thevalue=String.valueOf(Integer.parseInt(thevalue)*100);
				   v.add(0, thevalue);
				   v.add(1, collecttime);
				   v.add(2, "%");
				   avgPing = avgPing + Float.parseFloat(thevalue);
				   curPing=Float.parseFloat(thevalue);
				   if (curPing<minPing)
					 minPing=curPing;
				    list1.add(v);
			   }
			   
		   } catch (SQLException e) {
			   e.printStackTrace();
		   } finally {
			   try {
				   if (rs!=null)
				   rs.close();
				   if (conn!=null)
					conn.close();
				
			   } catch (SQLException e) {
				   e.printStackTrace();
			   }
		   }
		   hash.put("list", list1);
		   if (list1 != null && list1.size() > 0) {
			   hash.put("avgPing", CEIString.round(avgPing/ list1.size(), 2)+"");
		   } else {
			   hash.put("avgPing", "0");
		   }
		   hash.put("minPing", minPing+"");
		   hash.put("curPing", curPing+"");
	   }
	   return hash;
   }
}   