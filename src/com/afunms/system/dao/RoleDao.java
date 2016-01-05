/**
 * <p>Description:operate table NMS_ROLE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.system.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

import com.afunms.common.util.SysLogger;
import com.afunms.system.model.Role;
import com.afunms.common.base.*;

public class RoleDao extends BaseDao implements DaoInterface
{
   public RoleDao()
   {
	   super("system_role");
   }

   /**
    * ����һ����ɫ,����role_menu��������n����¼(n=�˵�������)
    */
   public boolean save(BaseVo baseVo)
   {
       Role vo = (Role)baseVo;
       boolean result = false;
       try
       {         
    	  int role_id = getNextID();
    	  
    	  String sql = null;
          //�ڱ�system_role���һ����¼
          sql = "insert into system_role(id,role)values(" + role_id + ",'" + vo.getRole() + "')";
          conn.addBatch(sql);
          
          int id = 1;
          sql = "select max(id) from system_role_menu";
          rs = conn.executeQuery(sql);
          if (rs.next())
             id = rs.getInt(1) + 1;

          rs = conn.executeQuery("select a.*,b.operate from system_menu a,system_role_menu b where b.role_id=0 and a.id=b.menu_id");
          while(rs.next())	
          {
        	  StringBuffer sb = new StringBuffer(100);
        	  sb.append("insert into system_role_menu(id,role_id,menu_id,operate)values(");
        	  sb.append(id);
        	  sb.append(",");
        	  sb.append(role_id);
        	  sb.append(",'");
        	  sb.append(rs.getString("id"));
        	  sb.append("',");
        	  sb.append(rs.getInt("operate"));
        	  sb.append(")");
        	  id++;
              conn.addBatch(sb.toString());
          }         
          //��� ��ɫ��Ӧ�Ĺ���ģ��
          rs = conn.executeQuery("select * from nms_home_module");
          while(rs.next())	
          {
              StringBuffer sb = new StringBuffer(100);
              sb.append("insert into nms_home_module_role(enName, chName, role_id, dept_id, visible, note,type)values('");
              sb.append(rs.getString("enName"));
              sb.append("','");
              sb.append(rs.getString("chName"));
              sb.append("','");
              sb.append(role_id);//��ɫid
              sb.append("','");
              sb.append("0");//����Ĭ��Ϊ��
              sb.append("','");
              sb.append(1);//Ĭ�϶�Ϊ�ɼ�
              sb.append("','");
              java.util.Date currentTime = new java.util.Date(); 
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
              String dateString = formatter.format(currentTime);  
              sb.append(dateString);
              sb.append("','");
              sb.append(rs.getInt("type"));
              sb.append("')"); 
              conn.addBatch(sb.toString()); 
          }
          //��� ��ɫ��Ӧ�Ĺ���ģ�飨������
          
          
          conn.executeBatch();
          result = true;
     }
     catch(Exception e)
     {
          
    	 result = false;
    	 conn.rollback();
    	 SysLogger.error("RoleDao.save()",e);                  
     }
     finally
     {
    	 conn.close();    	 
     }
     return result;
   }

   /**
    * ��ҳ��ʾ,���ǵ�����ͬ������
    */
   public List listByPage(int curpage,int perpage)
   {
	   return listByPage(curpage,"where id>0",perpage);
   }

   /**
    * �г����н�ɫ
    */
   public List loadAll(boolean includeAdmin)
   {
      List list = new ArrayList();
      try
      {
         String sql = null;
         if(includeAdmin)
            sql = "select * from system_role order by id";
         else
            sql = "select * from system_role where id<>0 order by id";
         rs = conn.executeQuery(sql);
         while(rs.next())
            list.add(loadFromRS(rs));
      }
      catch(Exception e)
      {
         SysLogger.error("RoleDao.loadAll()",e);
         list = null;
      }
      finally
      {
    	  conn.close();    	 
      }
      return list;
   }

   public boolean update(BaseVo baseVo)
   {
	   Role vo = (Role)baseVo;
	   boolean result = false;
       try
       {
           conn.executeUpdate("update system_role set role='" + vo.getRole() + "' where id=" + vo.getId());
           result = true;
       }
       catch(Exception e)
       {
           SysLogger.error("RoleDao.update()",e);
           result = false;
       }
       finally
       {
           conn.close();
       }
       return result;
   }

   /**
    * ɾ��һ����ɫ,����ɾ���������еļ�¼
    * 1.ɾ��role���е�һ����¼;
    * 2.ɾ��user������ý�ɫ�йص������û�
    * 3.ɾ��role_menu������ý�ɫ�йص����й�ϵ
    */
   public boolean delete(String id)
   {
	  boolean result = false;
      try
      {
          conn.addBatch("delete from system_role_menu where role_id=" + id); //ɾ����ɫ�˵���ϵ������ؼ�¼
          conn.addBatch("delete from system_user where role_id=" + id);      //ɾ���û����иý�ɫ�µ��û�         
          conn.addBatch("delete from system_role where id=" + id);      
          conn.addBatch("delete from nms_home_module_role where role_id=" + id);// ɾ�� ��ɫģ������
          conn.addBatch("delete from nms_home_module_user where role_id=" + id);// ɾ�� �û���ҳģ������
          conn.executeBatch();
          result = true;
      }
      catch(Exception ex)
      {
          conn.rollback();
          SysLogger.error("Error in RoleDao.delete()",ex);
          result = false;
      }
      finally
      {
         conn.close();
      }
      return result;
   }
   
   public BaseVo loadFromRS(ResultSet rs)
   {
       Role vo = new Role();
       try
       {
           vo.setId(rs.getInt("id"));
           vo.setRole(rs.getString("role"));
       }
       catch(Exception e)
       {
    	   SysLogger.error("RoleDao.loadFromRS()",e); 
       }	   
       return vo;
   }   
}
