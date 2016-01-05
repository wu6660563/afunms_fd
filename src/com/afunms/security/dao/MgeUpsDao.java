/**
 * <p>Description: mge ups system</p>
 * <p>Company: dhcc.com</p>
 * @author yangjun 
 * @project ºº¿ÚÒøÐÐ
 * @date 2011-05-18
 */

package com.afunms.security.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.SysLogger;
import com.afunms.security.model.MgeUps;

public class MgeUpsDao extends BaseDao implements DaoInterface
{
	public MgeUpsDao()
	{
		super("app_ups_node");		
	}
		
    public List findByIP(String ipAddress)
    {	  
	    return findByCriteria("select * from app_ups_node where ip_address='" + ipAddress + "'");   
    }
    
    public List loadByType(String type)
    {
 	   List list = new ArrayList();
 	   try 
 	   {
 		   rs = conn.executeQuery("select * from app_ups_node where type='" + type + "' order by id");
 		   if(rs == null)return null;
 		   while(rs.next())
 			  list.add(loadFromRS(rs));				
 	   } 
 	   catch(Exception e) 
 	   {
 		   e.printStackTrace();
           list = null;
 		   SysLogger.error("MgeUpsDao.loadByType()",e);
 	   }
 	   finally
 	   {
 		   conn.close();
 	   }
 	   return list;	
    }
    
    public List loadByTypeAndSubtype(String type,String subtype)
    {
 	   List list = new ArrayList();
 	   try 
 	   {
 		   rs = conn.executeQuery("select * from app_ups_node where type='" + type + "' and subtype='" + subtype + "' order by id");
 		   if(rs == null)return null;
 		   while(rs.next())
 			  list.add(loadFromRS(rs));				
 	   } 
 	   catch(Exception e) 
 	   {
 		   e.printStackTrace();
           list = null;
 		   SysLogger.error("MgeUpsDao.loadByType()",e);
 	   }
 	   finally
 	   {
 		   conn.close();
 	   }
 	   return list;	
    }

    public BaseVo loadFromRS(ResultSet rs)
    {
        MgeUps vo = new MgeUps();
        try
        {
		    vo.setId(rs.getInt("id"));
		    vo.setAlias(rs.getString("alias"));
		    vo.setIpAddress(rs.getString("ip_address"));		   
		    vo.setLocation(rs.getString("location"));		   		   
		    vo.setCommunity(rs.getString("community"));
		    vo.setSysOid(rs.getString("sys_oid"));
		    vo.setSysName(rs.getString("sys_name"));
		    vo.setSysDescr(rs.getString("sys_descr"));
		    vo.setType(rs.getString("type"));
		    vo.setSubtype(rs.getString("subtype"));
		    vo.setIsmanaged(rs.getString("ismanaged"));
		    vo.setCollecttype(rs.getString("collecttype"));
		    vo.setBid(rs.getString("bid"));
        }
        catch(Exception e)
        {
   	        SysLogger.error("MgeUpsDao.loadFromRS()",e); 
        }	   
        return vo;
    }
    
    public boolean update(BaseVo baseVo)
    {
    	MgeUps vo = (MgeUps)baseVo;
 	    StringBuffer sql = new StringBuffer();
 	    sql.append("update app_ups_node set alias='");
 	    sql.append(vo.getAlias());
 	    sql.append("',location='");
 	    sql.append(vo.getLocation());
 	    sql.append("',ismanaged='");
	    sql.append(vo.getIsmanaged());
	    sql.append("',community='");
 	    sql.append(vo.getCommunity());
 	    sql.append("',bid='");
	    sql.append(vo.getBid());
 	    sql.append("' where id="); 	   
 	    sql.append(vo.getId()); 	     	   
 	    return saveOrUpdate(sql.toString());    	
    }
    
    public boolean update(String id , String ismanaged)
    {
 	    StringBuffer sql = new StringBuffer();
 	    sql.append("update app_ups_node set ismanaged='");
	    sql.append(ismanaged);
 	    sql.append("' where id="); 	   
 	    sql.append(id); 	     	   
 	    return saveOrUpdate(sql.toString());    	
    }
    
    public boolean save(BaseVo baseVo)
    {
    	MgeUps vo = (MgeUps)baseVo;
 	   
 	    StringBuffer sql = new StringBuffer();
 	    sql.append("insert into app_ups_node(id,alias,location,ip_address,type,community,sys_oid,sys_name,sys_descr,ismanaged,bid,collecttype,subtype)values(");
 	    sql.append(vo.getId());
 	    sql.append(",'");
 	    sql.append(vo.getAlias());
 	    sql.append("','");
 	    sql.append(vo.getLocation());
 	    sql.append("','"); 	   
 	    sql.append(vo.getIpAddress());
 	    sql.append("','");
 	    sql.append(vo.getType());
 	    sql.append("','");	   
 	    sql.append(vo.getCommunity());
 	    sql.append("','");
 	    sql.append(vo.getSysOid());
 	    sql.append("','");
 	    sql.append(vo.getSysName());
 	    sql.append("','");
 	    sql.append(vo.getSysDescr()); 	
 	    sql.append("','");
	    sql.append(vo.getIsmanaged()); 	
	    sql.append("','");
	    sql.append(vo.getBid()); 
	    sql.append("','");
	    sql.append(vo.getCollecttype()); 
	    sql.append("','");
	    sql.append(vo.getSubtype()); 
 	    sql.append("')");
 	    String ip = vo.getIpAddress();
 	    String ip1 ="",ip2="",ip3="",ip4="";
		String tempStr = "";
 	    String allipstr = "";
		if (ip.indexOf(".")>0){
			ip1=ip.substring(0,ip.indexOf("."));
			ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
			tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
		}
		ip2=tempStr.substring(0,tempStr.indexOf("."));
		ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
		allipstr=ip1+ip2+ip3+ip4;
		CreateTableManager ctable = new CreateTableManager();
		ctable.createTable(conn,"ping",allipstr,"ping");//Ping
		
		ctable.createTable(conn,"pinghour",allipstr,"pinghour");//Ping
		
		ctable.createTable(conn,"pingday",allipstr,"pingday");//Ping
		if("ups".equalsIgnoreCase(vo.getType())){
			ctable.createTable(conn,"input",allipstr,"input");//input
			
			ctable.createTable(conn,"inputhour",allipstr,"inputhour");//input
			
			ctable.createTable(conn,"inputday",allipstr,"inputday");//input
			
			ctable.createTable(conn,"output",allipstr,"output");//output
			
			ctable.createTable(conn,"outputhour",allipstr,"outputhour");//output
			
			ctable.createTable(conn,"outputday",allipstr,"outputday");//output
		}
 	    return saveOrUpdate(sql.toString());
    }
    
    public List loadMonitorUps()
    {
 	   return findByCriteria("select * from app_ups_node where ismanaged=1 and type ='ups' order by id"); 
    }
    
    public List loadMonitorAir()
    {
 	   return findByCriteria("select * from app_ups_node where ismanaged=1 and type ='air' order by id"); 
    }
}