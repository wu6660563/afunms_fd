package com.afunms.ipresource.dao;

import java.sql.ResultSet;
import java.util.*;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.*;
import com.afunms.ipresource.model.*;

public class IpResourceDao extends BaseDao
{
   public IpResourceDao()
   {
	   super("ip_resource");
   }

   public void update(List<IpResource> list)
   {	
	   try{
		   conn.executeUpdate("delete from ip_resource"); //delete all first
		   int id = 1;
		   for(IpResource ipr:list)
		   {		   
			   conn.addBatch("insert into ip_resource(id,ip_address,ip_long,mac,if_index,if_descr,node)values(" + id
					  + ",'" + ipr.getIpAddress() + "','" + ipr.getIpLong() + "','" + ipr.getMac() + "','" + 
					  ipr.getIfIndex() + "','" + ipr.getIfDescr() + "','" + ipr.getNode() + "')");
			   id++;
		   }
		   conn.executeBatch();
	   }catch(Exception e){
		   e.printStackTrace();
	   }finally{
		   conn.close(); 
	   }
	   
   }
   
   /**
    * 分页显示,每页列出25条记录
    */
   public List listByPage(int curpage)
   {
	   List list = new ArrayList();	   
	   try 
	   {
		   rs = conn.executeQuery("select count(*) from ip_resource");
		   if(rs.next())
			   jspPage = new JspPage(25,curpage,rs.getInt(1));

		   rs = conn.executeQuery("select * from ip_resource order by ip_long");
		   int loop = 0;
		   while(rs.next())
		   {
			  loop++;
			  if(loop<jspPage.getMinNum()) continue;
			  list.add(loadFromRS(rs));
			  if(loop==jspPage.getMaxNum()) break;
		   }
	   } 
	   catch (Exception e) 
	   {
		   SysLogger.error("IpResourceDao.listByPage()",e);
		   list = null;
	   }
	   finally
	   {
		   conn.close();
	   }
	   return list;
   }
   
   public IpResource find(String key,String value)
   {
	   String sql = "select * from ip_resource where " + key + "='" + value + "'";
	   System.out.println("sql==" + sql);
	   List list = findByCriteria(sql);
	   if(list!=null && list.size()>0 )
		  return (IpResource)list.get(0);
	   else
		  return null; 
   }
   
   /**
    * 按起点ip和终点ip选出这个区间所有记录
    * @param beginip 起点ip
    * @param endip long 终点ip
    */
   public Hashtable loadByIPRange(long beginip,long endip)
   {
      Hashtable voHash = new Hashtable();
      try
      {
         //从区间内的选出正在用的ip,因为正在用的ip是不连续的,所以用hashtable比较方便
         String sql = "select * from ip_resource where ip_long>=" + beginip + " and ip_long<=" 
		            + endip + " order by ip_long";
         rs = conn.executeQuery(sql);
         while(rs.next())
            voHash.put(new Long(rs.getLong("ip_long")),loadFromRS(rs));
      }
      catch(Exception ex)
      {
          SysLogger.error("Error in ScanIPDao.loadByIPRange()",ex);          
      }
      finally
      {
         conn.close();
      }
      return voHash;
   }   
//   public FdbItem locateIp(String ip)
//   {
//	   String sql = null;	   
//	   FdbItem vo = null;
//	   boolean hasFind = false;
//	   try
//	   {
//		   sql = "select * from nms_fdb_table where ip_address='" + ip + "'";
//		   rs = conn.executeQuery(sql);
//		   if(rs.next())
//		   {   
//		       vo = new FdbItem();
//		       vo.setNodeId(rs.getInt("node_id"));		      	
//			   vo.setIfIndex(rs.getString("ifindex"));			   
//			   vo.setPort(false);
//			   hasFind = true;
//		   }  
//		   if(!hasFind)
//		   {
//		       sql = "select * from topo_interface where ip_address like '%" + ip + ",' or ip_address like '%" + ip + "'";
//		       rs = conn.executeQuery(sql);
//		       if(rs.next())
//		       {
//			       vo = new FdbItem();
//			       vo.setNodeId(rs.getInt("node_id"));
//			       vo.setIfDescr(rs.getString("descr"));
//			       vo.setIfIndex(rs.getString("entity"));
//			       vo.setPort(true);			    
//		       }
//		   }
//	   }
//	   catch(Exception e)
//	   {
//		   SysLogger.error("FdbTableDao.locateIp()",e); 
//	   }
////	   finally
////	   {
////		   conn.close();
////	   }
//	   return vo;
//   }
      
   public BaseVo loadFromRS(ResultSet rs)
   {
	   IpResource vo = new IpResource();
	   try
	   {
	      
		  vo.setNode(rs.getString("node"));
	      vo.setMac(rs.getString("mac"));
          vo.setIfDescr(rs.getString("if_descr"));
          vo.setIfIndex(rs.getString("if_index"));
          vo.setIpAddress(rs.getString("ip_address"));		    
	   }
	   catch(Exception e)
	   {		   
	   }
	   return vo;
   }	   
}   