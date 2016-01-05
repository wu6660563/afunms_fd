/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.config.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.model.Diskconfig;

/**
 * 
 * 
 */
public class DiskconfigManager extends BaseManager implements ManagerInterface
{
	private String list()
	{
		DiskconfigDao dao = new DiskconfigDao();
		List ips = null;
        try {
            ips = dao.getIps();
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dao.close();
        }
		request.setAttribute("ips", ips);
		dao = new DiskconfigDao();
		try {
            setTarget("/config/diskconfig/list.jsp");
            list(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/diskconfig/list.jsp";
	}
	
	private String toolbarlist()
	{
		DiskconfigDao dao = new DiskconfigDao();
		String nodeid = getParaValue("nodeid");
		String ipaddress = getParaValue("ipaddress");
		List disklist = new ArrayList();
		try{
			disklist = dao.findByCondition(" where ipaddress='"+ipaddress+"'");
		}catch(Exception e){
			
		}finally{
			dao.close();
		}
		request.setAttribute("list", disklist);
		request.setAttribute("nodeid", nodeid);
		request.setAttribute("ipaddress", ipaddress);
		return "/config/diskconfig/toolbarlist.jsp";
	}
	
	private String empty() //yangjun
	{
		DiskconfigDao dao = new DiskconfigDao();
		try {
            dao.empty();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }
		dao = new DiskconfigDao();
		List ips = null;
		try {
			ips = dao.getIps();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("ips", ips);
		dao = new DiskconfigDao();
		try {
            setTarget("/config/diskconfig/list.jsp");
            list(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/diskconfig/list.jsp";
	}
	
	private String monitornodelist()
	{
		DiskconfigDao dao = new DiskconfigDao();	 
		setTarget("/config/diskconfig/portconfiglist.jsp");
        return list(dao," where managed=1");
	}
	
	private String fromlasttoconfig()
	{
		DiskconfigDao dao = new DiskconfigDao();
		DiskconfigDao _dao = new DiskconfigDao();
		try{
			dao.fromLastToDiskconfig();
			Hashtable allDiskAlarm = (Hashtable)_dao.getByAlarmflag(new Integer(99));
			ShareData.setAlldiskalarmdata(allDiskAlarm);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
			_dao.close();
		}
		dao = new DiskconfigDao();
		List ips = null;
		try {
			ips = dao.getIps();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("ips", ips);
		dao = new DiskconfigDao();
		setTarget("/config/diskconfig/list.jsp");
        return list(dao);
	}
	
	private String toolbarrefresh()
	{
		DiskconfigDao dao = new DiskconfigDao();
		DiskconfigDao _dao = new DiskconfigDao();
		String nodeid = getParaValue("nodeid");
		String ipaddress = getParaValue("ipaddress");
		try{
			dao.fromLastToDiskconfig(ipaddress);
			Hashtable allDiskAlarm = (Hashtable)_dao.getByAlarmflag(new Integer(99));
			ShareData.setAlldiskalarmdata(allDiskAlarm);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
			_dao.close();
		}
		return "/disk.do?action=toolbarlist&nodeid="+nodeid+"&ipaddress="+ipaddress;
//		dao = new DiskconfigDao();
//		List ips = null;
//		try {
//			ips = dao.getIps();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			dao.close();
//		}
//		request.setAttribute("ips", ips);
//		dao = new DiskconfigDao();
//		setTarget("/config/diskconfig/list.jsp");
//        return list(dao);
	}

    
	private String readyEdit()
	{
		DiskconfigDao dao = new DiskconfigDao();
		Diskconfig vo = new Diskconfig();
	    vo = dao.loadDiskconfig(getParaIntValue("id"));
	    request.setAttribute("vo", vo);
	    return "/config/diskconfig/edit.jsp";
	}
	
	private String update()
	{  
		Diskconfig vo = new Diskconfig();
		int id = getParaIntValue("id");
		DiskconfigDao dao = new DiskconfigDao(); 	
		vo = dao.loadDiskconfig(id);
		dao.close();
		String linkuse = getParaValue("linkuse");
		int sms = getParaIntValue("sms");
		int reportflag = getParaIntValue("reportflag");
		vo.setLinkuse(linkuse);
		if(sms > -1)
			vo.setSms(sms);
		if(reportflag > -1)
			vo.setReportflag(reportflag);
		dao = new DiskconfigDao();
		try{
			dao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		} finally {
		    dao.close();
		}
		dao = new DiskconfigDao();
		List ips = null;
        try {
            ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            dao.close();
        }
		request.setAttribute("ips", ips);
		String flag = getParaValue("flag");
		if(flag == null ){
			return "/disk.do?action=list";
		}else{
			return "/disk.do?action=toolbarlist&ipaddress="+vo.getIpaddress();
		}
		//dao = new DiskconfigDao();
		
    }
	
	private String updatedisk()
	{  
		Diskconfig vo = new Diskconfig();
		int id = getParaIntValue("id");
		DiskconfigDao dao = new DiskconfigDao(); 	
		vo = dao.loadDiskconfig(id);
		dao.close();
		int monflag = getParaIntValue("monflag");
		int limenvalue = getParaIntValue("limenvalue");
		int sms = getParaIntValue("sms");
		int limenvalue1 = getParaIntValue("limenvalue1");
		int sms1 = getParaIntValue("sms1");
		int limenvalue2 = getParaIntValue("limenvalue2");
		int sms2 = getParaIntValue("sms2");
		int reportflag = getParaIntValue("reportflag");
		vo.setMonflag(monflag);
		vo.setLimenvalue(limenvalue);
		vo.setSms(sms);
		vo.setLimenvalue1(limenvalue1);
		vo.setSms1(sms1);
		vo.setLimenvalue2(limenvalue2);
		vo.setSms2(sms2);
		vo.setReportflag(reportflag);
		dao = new DiskconfigDao();
		try{
			dao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		} finally {
            dao.close();
        }
		//dao = new DiskconfigDao();
		//List ips = dao.getIps();
		//dao.close();
		try{
			//request.setAttribute("ips", ips);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/disk.do?action=list";
    }
  

    private String updateselect()
    {
	   String key = getParaValue("key");
	   String value = getParaValue("value");	
	   DiskconfigDao dao = new DiskconfigDao();
	   request.setAttribute("key", key);
	   request.setAttribute("value", value);
	   int id = getParaIntValue("id");
	   Diskconfig vo = new Diskconfig();
	   vo = dao.loadDiskconfig(id);
	   
		dao.close();
		String linkuse = getParaValue("linkuse");
		int sms = getParaIntValue("sms");
		int reportflag = getParaIntValue("reportflag");
		vo.setLinkuse(linkuse);
		
		vo.setSms(sms);
		vo.setReportflag(reportflag);
		dao = new DiskconfigDao();
		try{
			dao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}  finally {
            dao.close();
        }
		dao = new DiskconfigDao();
	   try {
        setTarget("/config/diskconfig/findlist.jsp");
           list(dao," where "+key+" = '"+value+"'");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }  finally {
            dao.close();
        }
       return "/config/diskconfig/findlist.jsp";
   }

    private String find()
    {
	   String ipaddress = getParaValue("ipaddress");	 
	   DiskconfigDao dao = new DiskconfigDao();
	   List ips = null;
        try {
            request.setAttribute("ipaddress", ipaddress);
            	ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
		request.setAttribute("ips", ips);
		dao = new DiskconfigDao();
	   setTarget("/config/diskconfig/findlist.jsp");
       return list(dao," where ipaddress = '"+ipaddress+"'");
   }
    
   public String execute(String action)
   {
	  if(action.equals("list"))
	     return list(); 
	  if(action.equals("monitornodelist"))
		 return monitornodelist();
	  if(action.equals("fromlasttoconfig"))
		 return fromlasttoconfig();
	  if(action.equals("toolbarlist"))
		     return toolbarlist();
	  if(action.equals("toolbarrefresh"))
			 return toolbarrefresh(); 
	  if(action.equals("showedit"))
         return readyEdit();
      if(action.equals("update"))
         return update(); 
      if(action.equals("updatedisk"))
          return updatedisk();
      if(action.equals("find"))
         return find();  
      if(action.equals("updateselect"))
          return updateselect();
      if(action.equals("empty"))
          return empty();
	  if(action.equals("ready_add"))
	     return "/config/diskconfig/add.jsp";
	  if (action.equals("delete"))
      {	  
	      DiskconfigDao dao = new DiskconfigDao();
    	    try {
                setTarget("/disk.do?action=list");
                delete(dao);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            return "/disk.do?action=list";
        }
      setErrorCode(ErrorMessage.ACTION_NO_FOUND);
      return null;
   }
}
