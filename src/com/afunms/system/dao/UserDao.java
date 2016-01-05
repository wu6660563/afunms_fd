/**
 * <p>Description:operate table NMS_USER</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.system.model.User;

public class UserDao extends BaseDao implements DaoInterface
{
   public UserDao()
   {
	   super("system_user");
   }

   public List listByPage(int curpage,int perpage)
   {
	   //不显示超级管理员
	   return listByPage(curpage,"",perpage);
   }
   public User loadAllByUser(String user_id)
   {

	   User vo = null;
   
      try
      {
          rs = conn.executeQuery("select * from system_user where user_id ='"+ user_id+"'");
          if(rs.next())
              vo = (User)loadFromRS(rs);
      }
      catch(Exception e)
      {     
          SysLogger.error("UserDao:loadAll()",e);
          vo = null;
      }
      finally
      {
          conn.close();
      }
      return vo;
   }
   
   /**
    * 按用户ID和密码找一条记录,用于登录
    */
   public User findByLogin(String id,String pwd)
   {
      User vo = null;
      try
      {
          rs = conn.executeQuery("select * from system_user where user_id='" + id + "' and password='" + pwd + "'");
          if(rs.next())
             vo = (User)loadFromRS(rs);
      }
      catch(Exception e)
      {
          SysLogger.error("UserDao.findByLogin",e);
          vo = null;
      }
      finally
      {
          conn.close();
      }
      return vo;
   }

   public boolean save(BaseVo baseVo)
   {
	   return false;	   
   }
   
   public int save(User vo)
   {	   
       int result = -1;
       String sql = null;
	   try
	   {
	       sql = "select * from system_user where user_id='" + vo.getUserid() + "'";
	       rs = conn.executeQuery(sql);
	       if(rs.next())  //用户已经存在
	          return 0;

	       StringBuffer sqlBf = new StringBuffer(100);
	       sqlBf.append("insert into system_user(id,name,user_id,password,sex,dept_id,position_id,role_id,phone,email,mobile,businessids)");
	       sqlBf.append("values(");
	       sqlBf.append(getNextID());
	       sqlBf.append(",'");
	       sqlBf.append(vo.getName());
	       sqlBf.append("','");
	       sqlBf.append(vo.getUserid());
	       sqlBf.append("','");
	       sqlBf.append(vo.getPassword());
	       sqlBf.append("',");
	       sqlBf.append(vo.getSex());
	       sqlBf.append(",");
	       sqlBf.append(vo.getDept());
	       sqlBf.append(",");
	       sqlBf.append(vo.getPosition());
	       sqlBf.append(",");	       
	       sqlBf.append(vo.getRole());
	       sqlBf.append(",'");
	       sqlBf.append(vo.getPhone());
	       sqlBf.append("','");
	       sqlBf.append(vo.getEmail());
	       sqlBf.append("','");
	       sqlBf.append(vo.getMobile());
	       sqlBf.append("','");
	       sqlBf.append(vo.getBusinessids());
	       sqlBf.append("')");
	       conn.executeUpdate(sqlBf.toString());
	       result = 1;
	   }
	   catch (Exception e)
	   {
	    	result = -1;
	        SysLogger.error("Error in UserDao.save()",e);
	   }
	   finally
	   {
	       conn.close();
	   }
	   return result;
   }
   public List findbyIDs(String IDs)
   {
	   List list = new ArrayList();
		  try{
			  rs = conn.executeQuery("select * from system_user where id in(" + IDs +")");
			  while(rs.next())
			  {
				  BaseVo vo = loadFromRS(rs);
				  list.add(vo);
			  }
		  }catch(Exception e){e.printStackTrace();}
		  return list;
   }
   public boolean update(BaseVo baseVo)
   {
	   User vo = (User)baseVo;

	   StringBuffer sql = new StringBuffer(200);
       sql.append("update system_user set name='");
       sql.append(vo.getName());
       sql.append("',sex=");
       sql.append(vo.getSex());
       sql.append(",dept_id=");
       sql.append(vo.getDept());
       sql.append(",position_id=");
       sql.append(vo.getPosition());
       sql.append(",role_id=");
       sql.append(vo.getRole());
       sql.append(",phone='");
       sql.append(vo.getPhone());
       sql.append("',mobile='");
       sql.append(vo.getMobile());
       sql.append("',email='");
       sql.append(vo.getEmail());
       sql.append("',businessids='");
       sql.append(vo.getBusinessids());          
       if(vo.getPassword()!=null) //密码要修改
       {
           sql.append("',password='");
           sql.append(vo.getPassword());
       }
	   sql.append("' where id=");
       sql.append(vo.getId());
       return saveOrUpdate(sql.toString());
   }

   public BaseVo loadFromRS(ResultSet rs)
   {
      User vo = new User();
      try
      {
         vo.setId(rs.getInt("id"));
         vo.setName(rs.getString("name"));
         vo.setUserid(rs.getString("user_id"));
         vo.setPassword(rs.getString("password"));
         vo.setSex(rs.getInt("sex"));
         vo.setRole(rs.getInt("role_id"));
         vo.setDept(rs.getInt("dept_id"));
         vo.setPosition(rs.getInt("position_id"));
         vo.setEmail(rs.getString("email"));
         vo.setPhone(rs.getString("phone"));
         vo.setMobile(rs.getString("mobile"));
         vo.setBusinessids(rs.getString("businessids"));
         //SysLogger.info("bids===="+rs.getString("businessids"));
      }
      catch(Exception ex)
      {
          SysLogger.error("Error in UserDAO.loadFromRS()",ex);
          vo = null;
      }
      return vo;
   }   
}
