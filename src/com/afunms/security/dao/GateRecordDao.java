/**
 * <p>Description: Gate System Record,connect to Firebird DB</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project ��³ʯ��
 * @date 2007-01-18
 */

package com.afunms.security.dao;

import java.sql.ResultSet;
import java.util.*;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.SysLogger;
import com.afunms.security.model.GateRecord;

public class GateRecordDao extends BaseDao
{
	private static final String globeSql = 
		"select a.*,b.xing_ming from nmj_log a left join human b on a.person_key=b.person_key order by a.logkey desc";
	private static HashMap eventsMap;
	static
	{
		eventsMap = new HashMap();
		eventsMap.put("8", "�Ϸ���");
		eventsMap.put("48","�Ƿ�����");
		eventsMap.put("3920","��������");
		eventsMap.put("3921","��������");
		eventsMap.put("3923","���ų�ʱ");
		eventsMap.put("3930","���Ű�ť����");
		eventsMap.put("3933","����");
		eventsMap.put("3934","����");
	}
	
	public GateRecordDao() 
	{
		 super("jdbc/firebird","");   
	}
	
    public List listByPage(int curpage)
    {
	    if(conn==null) return null;
	    
    	List list = new ArrayList();	
	    jspPage = null;
	    try 
	    {
		    ResultSet rs = conn.executeQuery("select count(*) from nmj_log");
		    if(rs.next())
			   jspPage = new JspPage(20,curpage,rs.getInt(1));

		    rs = conn.executeQuery(globeSql);
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
		    SysLogger.error("GateRecordDao.listByPage()",e);		
	    }
	    finally
	    {
		    conn.close();
	    }
	    return list;
    }
	
    public List loadTopN(int topN)
    {	      
    	return loadTopN(globeSql,10);    	
    }
    
    /**
     *  0=ʱ��,1=��Ա����,2=����,3=�¼� 
     */
    public List combinQuery(boolean[] itemSwitchs,String startTime,String endTime,String person,String io,String event)
    {
    	if(conn==null) return null;
    	
    	//-------------����sql���------------------
    	String[] itemsSql = new String[4];
    	itemsSql[0] = "io_date||' '||io_time>='" + startTime + "' and io_date||' '||io_time<='" + endTime + "'";
    	itemsSql[1] = "xing_ming='" + person + "'";
    	itemsSql[2] = "io=" + io;
    	itemsSql[3] = "events=" + event;
    	
    	StringBuffer sqlBuf = new StringBuffer(200);
    	sqlBuf.append("select a.*,b.xing_ming from nmj_log a left join human b on a.person_key=b.person_key where ");
    	for(int i=0;i<4;i++)
    	{
    		if(itemSwitchs[i])
    		{
    			sqlBuf.append(itemsSql[i]);
    			sqlBuf.append(" and ");
    		}      		
    	}    	
    	int loc = sqlBuf.toString().lastIndexOf(" and "); //get rid of the last 'and'
    	String sql = null;
    	if(loc!=-1)
    	   sql = sqlBuf.toString().substring(0,loc) + " order by a.logkey desc";
    	else
    	   sql = sqlBuf.toString() + " order by a.logkey desc";
    	    	
    	//-------------------ѡ��ǰN����¼--------------
    	return loadTopN(sql,300);
    }
    
    private List loadTopN(String sql,int topN)
    {
    	if(conn==null) return null;
    	
    	List list = new ArrayList();
    	try
    	{
    		ResultSet rs = conn.executeQuery(sql);
	        int loop = 0;	    
	        while(rs.next())
	        {
		       loop++;
		       list.add(loadFromRS(rs));
		       if(loop==topN) break; 
	        }
    	 }
	     catch (Exception e) 
	     {
		     SysLogger.error("GateRecordDao.loadTopN()",e);		
	     }
	     finally
	     {
		    conn.close();
	     }
	     return list;    	
    }
    
    public BaseVo loadFromRS(ResultSet rs)
    {
	   GateRecord vo = new GateRecord();
       try
       {
		   vo.setPerson(rs.getString("xing_ming")==null?"":rs.getString("xing_ming"));
		   vo.setLogTime(rs.getString("io_date") + " " + rs.getString("io_time"));
		   String event = rs.getString("events");
		   if(eventsMap.get(event)!=null)
		      vo.setEvent((String)eventsMap.get(event));
		   else
			  vo.setEvent("");
		   
		   int io = rs.getInt("io");    
		   if(io==0)
		      vo.setIo("����");
		   else if(io==1)
			  vo.setIo("����");
		   else
			  vo.setIo(""); 
       }
       catch(Exception e)
       {
   	       SysLogger.error("GateRecordDao.loadFromRS()",e); 
       }	   
       return vo;
    }
}