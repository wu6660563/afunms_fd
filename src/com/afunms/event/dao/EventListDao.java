/**
 * <p>Description:operate table NMS_MENU and NMS_ROLE_MENU</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.event.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.EventList;
import com.afunms.system.vo.FlexVo;

public class EventListDao extends BaseDao implements DaoInterface
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public EventListDao()
  {
	  super("system_eventlist");	  
  }
  
  //-------------load all --------------
  public List loadAll()
  {
     List list = new ArrayList(5);
     try
     {
         rs = conn.executeQuery("select * from system_eventlist order by id");
         while(rs.next())
        	list.add(loadFromRS(rs)); 
     }
     catch(Exception e)
     {
         SysLogger.error("EventListDao:loadAll()",e);
         list = null;
     }
     finally
     {
         conn.close();
     }
     return list;
  }

  public List loadByWhere(String where){
	  List list = new ArrayList(); 
      try {
    	  rs = conn.executeQuery("select * from system_eventlist " + where);
		while(rs.next()){
			  list.add(loadFromRS(rs)); 
		  }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	  return list;
  }
  
    /**
     * 	GetWindowsData 性能页面windows服务器获取告警信息页面
     *  Aix\Linux\NET 性能页面告警信息获取
     *  
     * @param starttime
     * @param totime
     * @param status
     * @param level
     * @param businessid
     * @param nodeid
     * @return
     * @throws Exception
     */
	public List getQuery(String starttime,String totime,String status,String level,String businessid,Integer nodeid) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist e where e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");	
		if(!"99".equals(level)){
			s.append(" and e.level1="+level);
		}
		if(!"99".equals(status)){
			s.append(" and e.managesign="+status);
		}
		
		if (nodeid != null){
			if(nodeid.intValue()!=0 && nodeid.intValue()!=-1){
				s.append(" and nodeid="+nodeid);
			}	
		}
		int flag = 0;
		if (businessid != null){
			if(businessid !="-1"){
				String[] bids = businessid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								flag = 1;
							}else{
								//flag = 1;
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
				
			}	
		}
		s.append(" GROUP BY e.content order by e.recordtime,e.content desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next())
	        	list.add(loadFromRS(rs));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public List getQuery(String starttime,String totime,String status,String level,String businessid,Integer nodeid,String subType) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist e where e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");
		if(!"99".equals(level)){
			s.append(" and e.level1="+level);
		}
		if(!"99".equals(status)){
			s.append(" and e.managesign="+status);
		}
		
		if (nodeid != null){
			if(nodeid.intValue()!=0 && nodeid.intValue()!=-1){
				s.append(" and nodeid="+nodeid);
			}	
		}
		s.append(" and subtype='"+subType);
		s.append("'");
		int flag = 0;
		if (businessid != null){
			if(businessid !="-1"){
				String[] bids = businessid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								flag = 1;
							}else{
								//flag = 1;
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
				
			}	
		}
		s.append(" order by e.recordtime desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next())
	        	list.add(loadFromRS(rs));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public List getQuery_flex(String starttime,String totime,String status,String level,String businessid,Integer nodeid) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist e where e.recordtime>= '"+starttime+"'" +" and e.recordtime<='"+totime+"' ");
		if(!"99".equals(level)){
			s.append(" and e.level1="+level);
		}
		if(!"99".equals(status)){
			s.append(" and e.managesign="+status);
		}
		
		if (nodeid != null){
			if(nodeid.intValue()!=99){
				s.append(" and nodeid="+nodeid);
			}	
		}
		int flag = 0;
		if (businessid != null){
			if(businessid !="-1"){
				String[] bids = businessid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								flag = 1;
							}else{
								//flag = 1;
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
				
			}	
		}
		s.append(" order by e.recordtime desc LIMIT 8");		
		String sql = s.toString();
//		SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next())
	        	list.add(loadFromRS(rs));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 性能页面数据库相关告警获取
	 * @param starttime
	 * @param totime
	 * @param subtype
	 * @param status
	 * @param level
	 * @param businessid
	 * @param nodeid
	 * @return
	 * @throws Exception
	 */
	public List getQuery(String starttime,String totime,String subtype,String status,String level,String businessid,Integer nodeid) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist e where e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");	
		
//		s.append("select * from system_eventlist e where  " +
//				"e.content in (select distinct(s.content) as content " +
//				"from system_eventlist s where s.recordtime>= '"+starttime+"' " +
//				"and s.recordtime<='"+totime+"')");
		if(subtype != null && subtype.trim().length()>0){
			s.append(" and e.subtype='"+subtype+"' ");
		}
		if(!"99".equals(level)){
			s.append(" and e.level1="+level);
		}
		if(!"99".equals(status)){
			s.append(" and e.managesign="+status);
		}
		//SysLogger.info(nodeid+"==============================nodeid");
		if (nodeid != null){
			if(nodeid.intValue()!=-1){
				s.append(" and nodeid="+nodeid);
			}	
		}
		int flag = 0;
		if (businessid != null){
			if(businessid !="-1"){
				String[] bids = businessid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								flag = 1;
							}else{
								//flag = 1;
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
			}	
		}
		s.append(" GROUP BY e.content order by e.recordtime,e.content desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next())
	        	list.add(loadFromRS(rs));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
  
	public boolean save(BaseVo baseVo)
	{
		EventList vo = (EventList)baseVo;
		Calendar tempCal = (Calendar)vo.getRecordtime();							
		Date cc = tempCal.getTime();
		String recordtime = sdf.format(cc);
		StringBuffer sql = new StringBuffer(100);
		sql.append("insert into system_eventlist(eventtype,eventlocation,content,level1,managesign,bak,recordtime,reportman,nodeid,businessid,oid,subtype,managetime,subentity)values(");
		sql.append("'");
		sql.append(vo.getEventtype());
		sql.append("','");
		sql.append(vo.getEventlocation());	
		sql.append("','");
		sql.append(vo.getContent());
		sql.append("',");
		sql.append(vo.getLevel1());
		sql.append(",");
		sql.append(vo.getManagesign());
		sql.append(",'");
		sql.append(vo.getBak());
		sql.append("','");
		sql.append(recordtime);
		sql.append("','");
		sql.append(vo.getReportman());
		sql.append("',");
		sql.append(vo.getNodeid());
		sql.append(",'");
		sql.append(vo.getBusinessid());
		sql.append("',");
		sql.append(vo.getOid());
		sql.append(",'");
		sql.append(vo.getSubtype());
		sql.append("','");
		sql.append(vo.getManagetime());	
		sql.append("','");
		sql.append(vo.getSubentity());
		sql.append("')");
		//SysLogger.info(sql.toString());
		return saveOrUpdate(sql.toString());
	}
	
	public boolean delete(int nodeid,String subtype)
	{
		String sql = "delete from system_eventlist where nodeid="+nodeid+" and subtype='"+subtype+"'";
		//SysLogger.info(sql);
		return saveOrUpdate(sql);
	}
	
  //---------------update a business----------------
  public boolean update(BaseVo baseVo)
  {
	  EventList vo = (EventList)baseVo;
		Calendar tempCal = (Calendar)vo.getRecordtime();							
		Date cc = tempCal.getTime();
		String recordtime = sdf.format(cc);
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		sql.append("update system_eventlist set eventtype='");
		sql.append(vo.getEventtype());
		sql.append("',eventlocation='");
		sql.append(vo.getEventlocation());	
		sql.append("',content='");
		sql.append(vo.getContent());
		sql.append("',level1=");
		sql.append(vo.getLevel1());
		sql.append(",managesign=");
		sql.append(vo.getManagesign());
		sql.append(",bak='");
		sql.append(vo.getBak());
		sql.append("',recordtime='");
		sql.append(recordtime);
		sql.append("',reportman='");
		sql.append(vo.getReportman());
		sql.append("',nodeid=");
		sql.append(vo.getNodeid());
		sql.append(",businessid='");
		sql.append(vo.getBusinessid());
		sql.append("',oid=");
		sql.append(vo.getOid());
		sql.append(",subtype='");
		sql.append(vo.getSubtype());
		sql.append("',managetime='");
		sql.append(vo.getManagetime());  
		sql.append("',subentity='");
		sql.append(vo.getSubentity()); 
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
         SysLogger.error("EventListDao:update()",e);
     }
     finally
     {
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
	            conn.addBatch("delete from system_eventlist where id=" + id[i]);
	        }	         
	        conn.executeBatch();
	        result = true;
	    }
	    catch(Exception e)
	    {
	    	result = false;
	        SysLogger.error("EventListDao.delete()",e);	        
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
        rs = conn.executeQuery("select * from system_eventlist where id=" + id );
        if(rs.next())
           vo = loadFromRS(rs);
     }
     catch(Exception e)
     {
         SysLogger.error("EventListDao.findByID()",e);
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
	   EventList vo = new EventList();
      try
      {
    	  vo.setId(rs.getInt("id"));
          vo.setEventtype(rs.getString("eventtype"));
          vo.setEventlocation(rs.getString("eventlocation"));
          vo.setContent(rs.getString("content"));
          vo.setLevel1(rs.getInt("level1"));
          vo.setManagesign(rs.getInt("managesign"));
          vo.setBak(rs.getString("bak"));
          Calendar cal = Calendar.getInstance();
          Date newdate = new Date();
          newdate.setTime(rs.getTimestamp("recordtime").getTime());
          cal.setTime(newdate);
          vo.setRecordtime(cal);
         // vo.setReportman(rs.getString("reportman"));
          vo.setNodeid(rs.getInt("nodeid"));
          vo.setBusinessid(rs.getString("businessid"));
          //vo.setOid(rs.getInt("oid"));
          vo.setSubtype(rs.getString("subtype"));  
          //vo.setManagetime(rs.getString("managetime"));
          vo.setSubentity(rs.getString("subentity"));
    	  
      }
      catch(Exception e)
      {
          SysLogger.error("EventListDao.loadFromRS()",e);
          vo = null;
      }
      return vo;
   } 
   
   //yangjun add
   public List getEventList(String starttime,String totime) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select count(level1),e.level1 as level1 from system_eventlist e where e.level1<>0 and e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");
		s.append(" group by e.level1 order by e.level1 desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next()){
				FlexVo flexVo = new FlexVo();
				flexVo.setObjectName(rs.getString("level1"));
				flexVo.setObjectNumber(rs.getString("count(level1)"));
				list.add(flexVo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
   
   //yangjun add
   public List getQuery(String starttime,String totime,Integer nodeid) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist e where e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");
		if (nodeid != null){
		    s.append(" and nodeid="+nodeid);
		}
		s.append(" order by e.recordtime desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next())
	        	list.add(loadFromRS(rs));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
   public List getEventList(String starttime,String totime,String businessid) throws Exception{
		List list = new ArrayList();
		StringBuffer s = new StringBuffer();
		s.append("select count(level1),e.level1 as level1 from system_eventlist e where e.recordtime>= '"+starttime+"' " +
				"and e.recordtime<='"+totime+"'");
		int flag = 0;
		if (businessid != null){
			if(businessid !="-1"){
				String[] bids = businessid.split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(flag==0){
								s.append(" and ( businessid like '%,"+bids[i].trim()+",%' ");
								flag = 1;
							}else{
								//flag = 1;
								s.append(" or businessid like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}				
			}	
		}
		s.append(" group by e.level1 order by e.level1 desc");		
		String sql = s.toString();
		//SysLogger.info(sql);
		try{
			rs = conn.executeQuery(sql);
			while(rs.next()){
				FlexVo flexVo = new FlexVo();
				flexVo.setObjectName(rs.getString("level1"));
				flexVo.setObjectNumber(rs.getString("count(level1)"));
				list.add(flexVo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
   
   /**
    * 获取告警列表
    * @param starttime
    * @param totime
    * @param nodeid
    * @param type
    * @param level1
    * @param businessid
    * @param status
    * @return
    */
   public List<Object> getSummary(String starttime, String totime,
			String nodeid, String type, String level1, String businessid, String status) {
		StringBuffer sql = new StringBuffer();
		sql.append(" where recordtime>= '" + starttime
				+ "' " + "and recordtime<='"
				+ totime + "' ");
		if (nodeid != null && !"-1".equals(nodeid)) {
			sql.append(" and nodeid='" + nodeid + "'");
		}
		if (type != null && !"-1".equals(type)) {
			sql.append(" and subtype='" + type + "'");
		}
		if (level1 != null && !"-1".equals(level1)) {
			sql.append(" and level1='" + level1 + "'");
		}
		
		if (status != null && !"-1".equals(status)) {
			sql.append(" and managesign='" + status + "'");
		}
		
		int flag = 0;
		if (businessid != null) {
			if (businessid != "-1") {
				String[] bids = businessid.split(",");
				if (bids.length > 0) {
					for (int i = 0; i < bids.length; i++) {
						if (bids[i].trim().length() > 0) {
							if (flag == 0) {
								sql.append(" and ( businessid like '%,"
										+ bids[i].trim() + ",%' ");
								flag = 1;
							} else {
								sql.append(" or businessid like '%,"
										+ bids[i].trim() + ",%' ");
							}
						}
					}
					sql.append(") ");
				}
			}
		}
		//sql.append(" order by recordtime desc");
		
		//System.out.println(sql.toString());
		return getSummary(sql.toString(), -1, -1);
	}
   
   
   
   public List getSummary(String where,int curpage,int perpage){
	   	List list = new ArrayList();
  		StringBuffer sb = new StringBuffer();
		sb.append("select id,eventtype,eventlocation,content,level1,managesign,bak,recordtime," +
				"nodeid,businessid,subtype,subentity , count(*) , max(recordtime) from system_eventlist "+ where +" Group By level1,nodeid,subentity,subtype");
  	    
		String sql = sb.toString();
		
		
		try {

			rs = conn.executeQuery("select count(*) from (" + sql +") temp");
			if(rs.next())
				   jspPage = new JspPage(perpage,curpage,rs.getInt(1));
			rs = conn.executeQuery(sql);
			int loop = 0;
			while(rs.next()){
				loop++;
				if(loop<jspPage.getMinNum()) {
					continue;
				}
				
				List templist = new ArrayList();
				templist.add(loadFromRS(rs));
				templist.add(rs.getString("count(*)"));
				templist.add(rs.getString("max(recordtime)"));
				list.add(templist);
				
				if(loop==jspPage.getMaxNum()){
					break;
				} 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			conn.close();
		}
		return list;
  	}
   
   public boolean batchEditAlarmLevel(String[] ids , String alermlevel){
	   boolean result = false;
	   try
	   {
	       for(int i=0;i<ids.length;i++){
	    	   //System.out.println(alermlevel + "=================" + ids[i]);
	    	   if(ids[i] != null && !ids[i].equals("null") && !ids[i].equals("")){
	    		   conn.addBatch("update system_eventlist set level1='"+ alermlevel + "' where id=" + ids[i]);
	    	   }
	       }
	       conn.executeBatch();
	       result = true;
	   }
	   catch(Exception ex)
	   {
	       SysLogger.error("BaseDao.delete()",ex);
	       result = false;
	   }
	   return result;
   }
   
   /**
    * 获取告警列表
    * @param starttime
    * @param totime
    * @param nodeid
    * @param type
    * @param level1
    * @param businessid
    * @param status
    * @return
    */
   public List<EventList> getEventList(String starttime, String totime,
			String nodeid, String type, String level1, String eventlocation, String subentity, String businessid, String status) {
		StringBuffer sql = new StringBuffer();
		sql.append(" where recordtime>= '" + starttime
				+ "' " + "and recordtime<='"
				+ totime + "' ");
		if (nodeid != null && !"-1".equals(nodeid)) {
			sql.append(" and nodeid='" + nodeid + "'");
		}
		if (type != null && !"-1".equals(type)) {
			sql.append(" and subtype='" + type + "'");
		}
		if (level1 != null && !"-1".equals(level1)) {
			sql.append(" and level1='" + level1 + "'");
		}
		if (eventlocation != null && !"-1".equals(eventlocation)) {
			sql.append(" and eventlocation='" + eventlocation + "'");
		}
		if (subentity != null && !"-1".equals(subentity)) {
			sql.append(" and subentity='" + subentity + "'");
		}
		if (status != null && !"-1".equals(status)) {
			sql.append(" and managesign='" + status + "'");
		}
		
		int flag = 0;
		if (businessid != null) {
			if (businessid != "-1") {
				String[] bids = businessid.split(",");
				if (bids.length > 0) {
					for (int i = 0; i < bids.length; i++) {
						if (bids[i].trim().length() > 0) {
							if (flag == 0) {
								sql.append(" and ( businessid like '%,"
										+ bids[i].trim() + ",%' ");
								flag = 1;
							} else {
								sql.append(" or businessid like '%,"
										+ bids[i].trim() + ",%' ");
							}
						}
					}
					sql.append(") ");
				}
			}
		}
		
		return findByCondition(sql.toString());
	}

    /**
     * 根据 where 获取到告警数
     * @author nielin
     * @date 2010-08-05
     * @param where
     * @return
     */
    public String getCountByWhere(String where){
    	try {
    		String sql = "select count(*) as cnt from system_eventlist" + where;
    		rs = conn.executeQuery(sql);
    		if(rs.next()){
			   return rs.getString("cnt");
    		}
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return "0";
    }
    
    /**
     * 根据 id 来修改 managesign
     * @param managesign
     * @param id
     * @return
     */
    public boolean updateManagesignById(String managesign , String id){
    	String sql = "update system_eventlist set managesign='"+ managesign + "' where id=" + id;
    	return saveOrUpdate(sql);
    }
    
    /**
     * 根据ids 批量修改 managesign 
      * @param managesignFrom 原始状态
      * @param managesignTo   批量更改后的状态 
     * @param ids 
     * @return
     */
    public boolean batchUpdataManagesignByIds(String managesignFrom, String managesignTo ,String[] ids){
    	if(ids == null){
    		return false;
    	}
    	try {
			for(String id : ids){
				if(id != null && !id.equals("")){
					String sql = "update system_eventlist set managesign='"+ managesignTo + "' where id=" + id + " and managesign = '" + managesignFrom +"'";
					conn.addBatch(sql);
				}
			}
			conn.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			conn.close();
		}
    	return true;
    }

    public boolean deleteByRecordtime(String recordtime) {
        String deleteSql = "delete from system_eventlist where recordtime<'" + recordtime + "';";
        return saveOrUpdate(deleteSql);
    }
}
