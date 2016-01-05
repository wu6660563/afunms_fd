/**
 * <p>Description:operate table NMS_MENU and NMS_ROLE_MENU</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.config.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

import com.afunms.common.util.SysLogger;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.DaoInterface;
import com.afunms.config.model.IpAlias;
import com.afunms.common.base.BaseVo;
import com.afunms.system.model.Department;
import com.afunms.discovery.IfEntity;

public class IpAliasDao extends BaseDao implements DaoInterface
{
  public IpAliasDao()
  {
	  super("topo_ipalias");	  
  }
  
  public List loadAll()
  {
     List list = new ArrayList(5);
     try
     {
         rs = conn.executeQuery("select * from topo_ipalias order by id");
         while(rs.next())
        	list.add(loadFromRS(rs)); 
     }
     catch(Exception e)
     {
         SysLogger.error("IpAliasDao:loadAll()",e);
         list = null;
     }
     finally
     {
         conn.close();
     }
     return list;
  }
  
  public boolean deleteByHostIp(String hostip)
  {	
	   String sql = "delete from nms_portscan_config where ipaddress='"+hostip+"'";
	   return saveOrUpdate(sql);			
  }
  public List loadByIpaddress(String ipaddress)
  {
     List list = new ArrayList(5);
     try
     {
         rs = conn.executeQuery("select * from topo_ipalias where ipaddress='"+ipaddress+"'  order by id");
         while(rs.next())
        	list.add(loadFromRS(rs)); 
     }
     catch(Exception e)
     {
         SysLogger.error("IpAliasDao:loadByIpaddress()",e);
         list = null;
     }
     finally
     {
         conn.close();
     }
     return list;
  }

	public boolean save(BaseVo baseVo)
	{
		IpAlias vo = (IpAlias)baseVo;
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_business(name,descr)values(");
		sql.append("'");
		sql.append(vo.getDescr());
		sql.append("','");
		sql.append(vo.getDescr());		
		sql.append("')");
		return saveOrUpdate(sql.toString());
	}
	
  //---------------update a business----------------
  public boolean update(BaseVo baseVo)
  {
	  IpAlias vo = (IpAlias)baseVo;
     boolean result = false;
     StringBuffer sql = new StringBuffer();
     sql.append("update system_business set name='");
     sql.append(vo.getDescr());
     sql.append("',descr='");
     sql.append(vo.getDescr());
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
         SysLogger.error("BusinessDao:update()",e);
     }
     finally
     {
    	 conn.close();
     }     
     return result;
  }
  
  /**
   * 根据IP地址和是否显示别名标志位来得到IpAlias
   * @param ip
   * @param usedflag
   * @return
   */
  public IpAlias getByIpAndUsedFlag(String ip,String usedflag){
	  List list = new ArrayList();
	     try
	     {
	         rs = conn.executeQuery("select * from topo_ipalias where ipaddress='"+ip+"' and usedflag='"+usedflag+"' order by id");
	         while(rs.next())
	        	list.add(loadFromRS(rs)); 
	     }
	     catch(Exception e)
	     {
	         SysLogger.error("IpAliasDao:getByIpAndUsedFlag()",e);
	         list = null;
	     }
	     finally
	     {
	         conn.close();
	     }
	    if(list != null && list.size()>0){
	    	return (IpAlias)list.get(0);
	    }else{
	    	return null;
	    }
  }
  
  /**
   * @param ipaddress 
   * @param ipalias 使用的ipalias
   * @return
   */
  public boolean updateIpAlias(String ipaddress,String ipalias){
	  boolean result = false;
	  StringBuffer sBuffer = new StringBuffer();
	  sBuffer.append("update topo_ipalias set usedflag = '1' where ipaddress='");
	  sBuffer.append(ipaddress);
	  sBuffer.append("' and aliasip = '"); 
	  sBuffer.append(ipalias);
	  sBuffer.append("'");
	 System.out.println(sBuffer.toString());
	  conn.addBatch(sBuffer.toString());
	  sBuffer = new StringBuffer();
	  sBuffer.append("update topo_ipalias set usedflag = '0' where ipaddress='");
	  sBuffer.append(ipaddress);
	  sBuffer.append("'and aliasip <> '");
	  sBuffer.append(ipalias);
	  sBuffer.append("'");
	  System.out.println(sBuffer.toString());
	  conn.addBatch(sBuffer.toString());
	  try {
		  conn.executeBatch();
		  result = true;
	  } catch (Exception e) {
		  result = false;
		  SysLogger.error("BusinessDao.updateIpAlias()",e);	
	  } finally {
	      conn.close();
	  }
	  return result;
  }
  
	public boolean delete(String[] id)
	{
		boolean result = false;
	    try
	    {	    
	        for(int i=0;i<id.length;i++)
	        {
	            conn.addBatch("delete from topo_ipalias where id=" + id[i]);
	        }	         
	        conn.executeBatch();
	        result = true;
	    }
	    catch(Exception e)
	    {
	    	result = false;
	        SysLogger.error("BusinessDao.delete()",e);	        
	    }
	    finally
	    {
	         conn.close();
	    }
	    return result;
	}
  
  public BaseVo findByID(String id)
  {
     BaseVo vo = null;
     try
     {
        rs = conn.executeQuery("select * from topo_ipalias where id=" + id );
        if(rs.next())
           vo = loadFromRS(rs);
     }
     catch(Exception e)
     {
         SysLogger.error("BusinessDao.findByID()",e);
         vo = null;
     }
     finally
     {
        conn.close();
     }
     return vo;
  }
  
   public BaseVo loadFromRS(ResultSet rs)
   {
	   IpAlias vo = new IpAlias();
      try
      {
          vo.setId(rs.getString("id"));
          vo.setIpaddress(rs.getString("ipaddress"));
          vo.setAliasip(rs.getString("aliasip"));
          vo.setDescr(rs.getString("descr"));  
          vo.setIndexs(rs.getString("indexs"));
          vo.setSpeed(rs.getString("speeds"));
          vo.setTypes(rs.getString("types"));
          
      }
      catch(Exception e)
      {
          SysLogger.error("BusinessDao.loadFromRS()",e);
          vo = null;
      }
      return vo;
   }  
}
