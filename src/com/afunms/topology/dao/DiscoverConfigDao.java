/**
 * <p>Description:operate table NMS_DISCOVER_CONDITION</p>
 * <p>Company:miiwill soft</p>
 * @author afu
 * @project LanKeeper2.0
 * @date 2007-03-11
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.util.*;

import com.afunms.common.base.*;
import com.afunms.common.util.SysLogger;
import com.afunms.discovery.DiscoverEngine;
import com.afunms.topology.model.DiscoverConfig;

public class DiscoverConfigDao extends BaseDao implements DaoInterface
{
   public DiscoverConfigDao()
   {
	   super("topo_discover_config");	   	  
   }
   
   public boolean update(BaseVo baseVo)
   {
	   return false;   
   }
   
   public boolean save(BaseVo baseVo)
   {
	   DiscoverConfig vo = (DiscoverConfig)baseVo;		
       StringBuffer sb = new StringBuffer(200);
       sb.append("insert into topo_discover_config(id,address,community,flag,shieldnetstart,shieldnetend,includenetstart,includenetend)values(");
       sb.append(getNextID());
       sb.append(",'");
       sb.append(vo.getAddress());
       sb.append("','");
       sb.append(vo.getCommunity());
       sb.append("','");
       sb.append(vo.getFlag());
       sb.append("','");
       sb.append(vo.getShieldnetstart());
       sb.append("','");
       sb.append(vo.getShieldnetend());
       sb.append("','");
       sb.append(vo.getIncludenetstart());
       sb.append("','");
       sb.append(vo.getIncludenetend());
       sb.append("')");	        
       return saveOrUpdate(sb.toString());
   }
   
   public boolean delete(String id)
   {
	   boolean result = false;
	   try
	   {
		   conn.executeUpdate("delete from topo_discover_config where id=" + id);
		   SysLogger.info("delete from topo_discover_config where id=" + id);
		   result = true;
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.delete()"+e.getMessage());
		   result = false;
	   }
	   return result;	   
   }
   
   public List listByFlag(String flag)
   {
	   List list = new ArrayList(10);
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='" + flag + "' order by id");
		   while(rs.next())
			  list.add(loadFromRS(rs)); 
		   
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.delete()"+e.getMessage());
	   }

	   return list;
   }

   public Set loadCommunity()
   {
	   Set set = new HashSet(3);
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='community' order by id");
		   while(rs.next())
			  set.add(rs.getString("community"));
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.loadCommunity()",e);
	   }
	   return set;
   }

   public Set loadShield()
   {
	   Set set = new HashSet(3);
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='shield' order by id");
		   while(rs.next())
			  set.add(rs.getString("address"));
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.loadShield()",e);
	   }
	   return set;
   }
   
   //װ����Ҫ���ε�����
   public List loadNetShield()
   {
	   List retValue = new ArrayList();
	   //Set set = new HashSet(3);
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='netshield' order by id");
		   while(rs.next()){
			   Vector netV = new Vector();
			   String startIP = rs.getString("shieldnetstart");
			   String endIP = rs.getString("shieldnetend");
			   if(isIP(startIP)&& isIP(endIP)){
				   if(ip2long(endIP)>ip2long(startIP)){
					   netV.add(0,ip2long(startIP));
					   netV.add(1,ip2long(endIP));
					   retValue.add(netV);
				   }
			   }
		   }
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.loadNetShield()",e);
	   }
	   return retValue;
   }
   //װ��ֻ��Ҫ���ֵ�����
   public List loadIncludeNet()
   {
	   List retValue = new ArrayList();
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='includenet' order by id");
		   while(rs.next()){
			   Vector netV = new Vector();
			   String startIP = rs.getString("includenetstart");
			   String endIP = rs.getString("includenetend");
			   if(isIP(startIP)&& isIP(endIP)){
				   if(ip2long(endIP)>ip2long(startIP)){
					   netV.add(0,ip2long(startIP));
					   netV.add(1,ip2long(endIP));
					   retValue.add(netV);
				   }
			   }
		   }
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.loadIncludeNet()",e);
	   }
	   return retValue;
   }

   public Map loadSpecified()
   {
	   Map map = new Hashtable(3);
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='specified' order by id");
		   while(rs.next())
			  map.put(rs.getString("address"),rs.getString("community"));
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.loadSpecified()",e);
	   }
	   return map;
   }
   
   /*
    * ����������ӵ�ַ
    */
   public void saveCore(String ip,String community)
   {
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='core'");
		   if(rs.next())  //����������
		      conn.executeUpdate("update topo_discover_config set address='" + ip + "',community='" + community + "' where flag='core'");
		   else
			  conn.executeUpdate("insert into topo_discover_config(id,address,community,flag)values(" + getNextID() 
			  +	",'" + ip + "','" + community + "','core')"); 
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.saveCore()",e);
	   }
   }
   
   /*
    * ���������������ӵ�ַ
    */
   public void saveOtherCore(String ip,String community)
   {
	   try
	   {
		   rs = conn.executeQuery("select * from topo_discover_config where flag='othercore' and address='"+ip+"'");
		   if(rs.next())  //���û�������
		      conn.executeUpdate("update topo_discover_config set community='" + community + "' where flag='othercore' and address='"+ip+"'");
		   else
			  conn.executeUpdate("insert into topo_discover_config(id,address,community,flag)values(" + getNextID() 
			  +	",'" + ip + "','" + community + "','othercore')"); 
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.saveOtherCore()",e);
	   }
   }

   /*
    * ������˷��ֱ��Ա����·���
    * Ŀǰ��Ҫ�޸ĳɶ��ϴη��ֵ��豸���б���,�Ա���·��ֵ��豸���бȽ�
    */
   public void cleanTOPOTable()
   {
	   try
	   {
		   if(DiscoverEngine.getInstance().getDiscovermodel() == 1){
			   //���䷢��
			   //conn.executeUpdate("delete from topo_repair_link");
		   }else
			   conn.executeUpdate("delete from topo_repair_link");
		   
		   conn.executeUpdate("delete from topo_subnet");
		   conn.executeUpdate("delete from topo_node_single_data");
		   conn.executeUpdate("delete from topo_node_multi_data");
		   conn.executeUpdate("delete from topo_node_id");
		   conn.executeUpdate("delete from topo_network_link");
		   conn.executeUpdate("delete from topo_interface_data");
		   conn.executeUpdate("delete from topo_interface");
		   conn.executeUpdate("delete from topo_host_node");	
		   conn.executeUpdate("delete from topo_node_monitor");//���������Ԫ��
		   conn.executeUpdate("delete from topo_interface_threshold");
		   conn.executeUpdate("delete from topo_ipalias");
		   //���MAC��ַ��صı�
		   conn.executeUpdate("delete from nms_ip_mac");
		   conn.executeUpdate("delete from nms_ipmacbase");
		   conn.executeUpdate("delete from nms_ipmacchange");
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("DiscoverConfigDao.cleanTOPOTable()",e);
	   }
	   finally
	   {
		   conn.close();
	   }
   }

   
   public BaseVo loadFromRS(ResultSet rs)
   {
	  DiscoverConfig vo = new DiscoverConfig();
      try
      {
		   vo.setId(rs.getInt("id"));
		   vo.setAddress(rs.getString("address"));
		   vo.setCommunity(rs.getString("community"));
		   vo.setFlag(rs.getString("flag"));	
		   vo.setShieldnetstart(rs.getString("shieldnetstart"));
		   vo.setShieldnetend(rs.getString("shieldnetend"));
		   vo.setIncludenetstart(rs.getString("includenetstart"));
		   vo.setIncludenetend(rs.getString("includenetend"));
      }
      catch(Exception e)
      {
 	       SysLogger.error("DiscoverConfigDao.loadFromRS()",e); 
      }	   
      return vo;
   }
   
	/**
	 * ��һ���ַ�����ʽ��ip��ַת����һ��������������ǷǷ����ݣ��򷵻�0
	 * 
	 * @param ip
	 * @return
	 */
	// ��֤��������Ƿ���IP��ַ
	static public boolean isIP(String ip) {
		boolean result = true;
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			if (st.countTokens() < 4) {
				return false;
			}

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				if (part >= 0 && part <= 255) {
					continue;
				} else {
					result = false;
					break;
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	/**
	 * ��һ���ַ�����ʽ��ip��ַת����һ��������������ǷǷ����ݣ��򷵻�0
	 * 
	 * @param ip
	 * @return
	 */
	static public long ip2long(String ip) {
		long result = 0;
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				result = result * 256 + part;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * ��һ��long��ʽ��ip��ַת����һ���ַ���������ǷǷ����ݣ��򷵻ؿմ�
	 * 
	 * @param ip
	 * @return
	 */
	static public String ip2String(long ip) {
		String result = "";
		try {
			result = "" + (int) (ip >> 24) + "." + (((int) (ip >> 16)) & 0xff)
					+ "." + (((int) (ip >> 8)) & 0xff) + "."
					+ (((int) ip) & 0xff);
		} catch (Exception e) {
			result = "";
		}
		return result;
	}
}
