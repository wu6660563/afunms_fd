/**
 * <p>Description:operate table NMS_SYS_LOG</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.afunms.common.util.SysLogger;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.BaseVo;
import com.afunms.system.model.SysLog;
import com.afunms.common.base.JspPage;

public class SysLogDao extends BaseDao implements DaoInterface
{
   public SysLogDao()
   {
	   super("system_sys_log");
   }
   
   public List listByPage(int curpage)
   {
	   List list = new ArrayList();
	   int rc = 0;
	   try
	   {
		   rs = conn.executeQuery("select count(*) from system_sys_log ");
		   if(rs.next())
		       rc = rs.getInt(1);
		   jspPage = new JspPage(curpage,rc);
		     
		   rs = conn.executeQuery("select * from system_sys_log order by id desc");		   
		   int loop = 0;
		   while(rs.next())
		   {
		       loop++;
		       if(loop<jspPage.getMinNum())
		          continue;
		       list.add((SysLog)loadFromRS(rs));
		       if(loop==jspPage.getMaxNum()) break;	         
		   }
		}   	     
	    catch (Exception ex)
	    {
	        SysLogger.error("Error in SysLogDao.loadByPage()");
	    }
	    finally
	    {
	        conn.close();
	    }
	    return list;	   
   }
         
   /**
    * 增加一条记录
    */
   public boolean save(BaseVo baseVo)
   {
	   SysLog vo = (SysLog)baseVo;	      	   
	   StringBuffer sb = new StringBuffer(200);
       sb.append("insert into system_sys_log(userid,event,log_time,ip,username,type)values(");
       sb.append(vo.getUserid());
       sb.append(",'");
       sb.append(vo.getEvent());
       sb.append("','");
       sb.append(vo.getLogTime());
       sb.append("','");
       sb.append(vo.getIp());
       sb.append("','");
       sb.append(vo.getUser());
       sb.append("',");
       sb.append(vo.getType());
       sb.append(")");
       return saveOrUpdate(sb.toString());
   }
   
   public BaseVo loadFromRS(ResultSet rs)
   {
       SysLog vo = new SysLog();
       try
       {
           vo.setId(rs.getInt("id"));
           vo.setUserid(rs.getInt("userid"));
           vo.setEvent(rs.getString("event"));
           vo.setLogTime(rs.getString("log_time"));
           vo.setIp(rs.getString("ip"));
           vo.setUser(rs.getString("username"));
           vo.setType(rs.getInt("type"));
       }
       catch(Exception e)
       {
    	   SysLogger.error("SysLogDao.loadFromRS()",e); 
       }	   
       return vo;
   }
   
   public boolean update(BaseVo baseVo)
   {
	   return false;
   }
   
   /**
    * 	保存用户退出系统时间
    * 
    * @param sysLog
    * @return boolean
    */
   public boolean saveQuitTime(SysLog sysLog){
	   if(null != sysLog)
		   return saveQuitTime(sysLog.getQuittime(),sysLog.getId());
	   return false;
   }
   
   /**
    * 	保存用户退出系统时间
    * @param quitTime 退出时间
    * @param id 记录id
    * @return boolean
    */
   public boolean saveQuitTime(String quitTime, int id){
	   
	   // 对传入参数进行验证
	   if(null == quitTime || quitTime.trim().length() <= 0)
		   return false;
	   if(id < 0)
		   return false;
	   
	   String sql = "update "+ super.table +" set quit_time='"+quitTime 
	   			+"' where id="+id+ ";";
	   return saveOrUpdate(sql);
	   
   }
   
   public int getIdByUsername(String username){
	   if (null == username || username.trim().length() <= 0)
		   return -1;
	   String sql = "select * from " + super.table + " where username='"+ username 
	   		+"' and log_time=( select MAX(log_time) as log_time from "+ super.table 
	   		+" where username='"+username+"');";
	   
	   rs = conn.executeQuery(sql);
	   int id = 0;
	   try {
		   		while(rs.next()){
		   			// 根据quit_time 来保证存入的quit_time不被二次修改
		   			String quit_time = rs.getString("quit_time");
		   			if(null == quit_time || quit_time.trim().length() <= 0)
		   				id = rs.getInt("id");
		   			else 
		   				id = -1;
		   		}
	   	} catch (SQLException e) {
	   		// TODO Auto-generated catch block
	   		e.printStackTrace();
	   		id = -1;
	   	} finally{
	   		try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				conn.close();
			}
	   	}
	   	return id;
   }
   
   public static void main(String[] args){
   }
}
