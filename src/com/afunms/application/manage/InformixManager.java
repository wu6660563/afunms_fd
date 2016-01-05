/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.util.*;
import java.text.*;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.common.base.*;
import com.afunms.common.util.*;
import com.afunms.event.dao.EventListDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.util.DBRefreshHelper;
import com.afunms.application.util.IpTranslation;
import com.afunms.system.model.User;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.polling.impl.*;
import com.afunms.polling.api.*;

import com.afunms.report.jfree.ChartCreator;
import com.sun.mail.imap.protocol.Status;

import org.jfree.data.general.DefaultPieDataset;

public class InformixManager extends BaseManager implements ManagerInterface
{
	public static Hashtable rsc_type_ht = new Hashtable();
	public static Hashtable req_mode_ht = new Hashtable();
	public static Hashtable mode_ht = new Hashtable();
	public static Hashtable req_status_ht = new Hashtable();
	public static Hashtable req_ownertype_ht = new Hashtable();  

	
	private String informixping()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
				runstr = "��������";
			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			
			//SysLogger.info(vo.getIpAddress()+"=============ipaddress");
//			dbValue = (Hashtable)informixData.get("informix");
			/*if(sValue != null && sValue.containsKey(vo.getDbName()))
				dbValue = (Hashtable)sValue.get(vo.getDbName());*/
			dbValue.put("databaselist", databaseList);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixping.jsp";
    }
	
	private String informixwaitingthread(){
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		String id = getParaValue("id");
		request.setAttribute("id", id);
		double avgpingcon = 0;
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			Hashtable informixOtherData = dao.getInformix_nmsother(serverip);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("informixOtherData", informixOtherData);
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			
//			dbValue = (Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
//			dbValue.put("sessionList", sessionList);
//			dbValue.put("lockList", lockList);
//			dbValue.put("informixspaces", spaceList);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixwaitingthread.jsp";
	}
	
	private String informixbaractlog(){
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		String id = getParaValue("id");
		String showAllLogFlag = getParaValue("showAllLogFlag");
		if(showAllLogFlag == null){
			showAllLogFlag = "0";
		}
		request.setAttribute("id", id);
		double avgpingcon = 0;
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			Hashtable informixbaractlog = dao.getInformix_nmsbaractlog(serverip,showAllLogFlag);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("informixbaractlog", informixbaractlog);
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			
//			dbValue = (Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
//			dbValue.put("sessionList", sessionList);
//			dbValue.put("lockList", lockList);
//			dbValue.put("informixspaces", spaceList);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			request.setAttribute("showAllLogFlag", showAllLogFlag);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixbaractlog.jsp";  
	}
	
	private String informixcap()
    {
 	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		String id = getParaValue("id");
		request.setAttribute("id", id);
		double avgpingcon = 0;
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData == null)informixData = new Hashtable();
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			List sessionList = dao.getInformix_nmssession(serverip);
			List lockList = dao.getInformix_nmslock(serverip);
			List spaceList = dao.getInformix_nmsspace(serverip);
			Hashtable informixOtherData = dao.getInformix_nmsother(serverip);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("informixOtherData", informixOtherData);
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			
//			dbValue = (Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("sessionList", sessionList);
			dbValue.put("lockList", lockList);
			dbValue.put("informixspaces", spaceList);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixcap.jsp";
    
    }
	
	private String informixdb()
    {
 	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
//			List sessionList = dao.getInformix_nmssession(serverip);
			List lockList = dao.getInformix_nmslock(serverip);
			List spaceList = dao.getInformix_nmsspace(serverip);
			List logList = dao.getInformix_nmslog(serverip);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			//dbValue = (Hashtable)sValue.get(vo.getDbName());
//			  dbValue=(Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("lockList", lockList);
			dbValue.put("informixspaces", spaceList);
			dbValue.put("informixlog", logList);

			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixdb.jsp";
    
    }
	
	private String informixdevice()
    {
	 	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//	Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			List configList = dao.getInformix_nmsconfig(serverip);
			if("1".equalsIgnoreCase(status)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			//dbValue = (Hashtable)sValue.get(vo.getDbName());
//			dbValue=(Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("configList", configList);

			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			//request.setAttribute("infromixVO",informixVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixdevice.jsp";
    
    }
	
	private String informixevent()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String pingconavg ="0";
		double avgpingcon = 0;
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//	Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData == null)informixData = new Hashtable();
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			if("1".equalsIgnoreCase(statusStr)){
				runstr = "��������";
			}

			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			dbValue = (Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			
			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			String strStartDay = getParaValue("startdate");
			String strToDay = getParaValue("todate");
			if(strStartDay!=null && !"".equals(strStartDay)){
			starttime1 = strStartDay+" 00:00:00";
			}
			if(strToDay!=null && !"".equals(strToDay)){
			totime1 = strToDay+" 23:59:59";
			}

			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
			
		    	status = getParaIntValue("status");
		    	level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
		    	
		    	b_time = getParaValue("startdate");
				t_time = getParaValue("todate");
		    	
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				if (b_time == null){
					b_time = sdf1.format(new Date());
				}
				if (t_time == null){
					t_time = sdf1.format(new Date());
				}
				EventListDao eventdao = new EventListDao();
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",
							user.getBusinessids(),vo.getId());
				}catch(Exception ex){
					ex.printStackTrace();
				}finally{
					eventdao.close();
				}
				
			request.setAttribute("list", list);
			request.setAttribute("startdate", b_time);
			request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixevent.jsp";
    }
	
	private String informixio()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
				runstr = "��������";
			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData == null)informixData = new Hashtable();
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			List ioList = dao.getInformix_nmsio(serverip);
			if("1".equalsIgnoreCase(statusStr)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			//dbValue = (Hashtable)sValue.get(vo.getDbName());
//			  dbValue=(Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("iolist", ioList);
			
			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  
			request.setAttribute("list", list);
			request.setAttribute("startdate", b_time);
			request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
			request.setAttribute("avgpingcon", avgpingcon);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dao != null){
				dao.close();
			}
		}
		return "/application/db/informixio.jsp";
    }
	
//	private String informixlog()
//    {    	   
//		DBVo vo = new DBVo();
//		DBDao dao = new DBDao();
//		
//		Hashtable sysValue = new Hashtable();
//		Hashtable sValue = new Hashtable();
//		Hashtable dbValue = new Hashtable();
//		Hashtable imgurlhash=new Hashtable();
//		Hashtable maxhash = new Hashtable();
//		List list = new ArrayList();
//		int status =99;
//		int level1 = 99;
//		String b_time ="";
//		String t_time = "";
//		String pingconavg ="0";
//		double avgpingcon = 0;
//		String id = getParaValue("id");
//		request.setAttribute("id", id);
//		try{
//			vo = (DBVo)dao.findByID(id);
//			DBTypeDao typedao = new DBTypeDao();
//			DBTypeVo typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
//			request.setAttribute("db", vo);
//			request.setAttribute("dbtye", typevo.getDbdesc());
//			String managed = "������";
//			if(vo.getManaged() == 0)managed = "δ����";
//			request.setAttribute("managed", managed);
//			String runstr = "<font color=red>����ֹͣ</font>";
////			Hashtable informixData=new Hashtable();
////			Hashtable mino=new Hashtable();
////			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
////			if(sysValue!=null&&sysValue.size()>0){
////				if(sysValue.containsKey(vo.getIpAddress())){
////					mino=(Hashtable)sysValue.get(vo.getIpAddress());
////					informixData=(Hashtable)mino.get(vo.getDbName());
////					if(informixData == null)informixData = new Hashtable();
////					if(informixData.containsKey("status")){
////						String p_status = (String)informixData.get("status");
////						if(p_status != null && p_status.length()>0){
////							if("1".equalsIgnoreCase(p_status)){
////								runstr = "��������";
////							}
////						}
////					}
////				}
////			}
//			IpTranslation tranfer = new IpTranslation();
//			String hex = tranfer.formIpToHex(vo.getIpAddress());
//			String serverip = hex+":"+vo.getDbName();
//			String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
//			List databaseList = dao.getInformix_nmsdatabase(serverip);
////			List ioList = dao.getInformix_nmsio(serverip);
//			List logList = dao.getInformix_nmslog(serverip);
//			if("1".equalsIgnoreCase(statusStr)){
//				runstr = "��������";
//			}
//			request.setAttribute("runstr", runstr);
//			//sysValue = ShareData.getInformixmonitordata();
//			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			//dbValue = (Hashtable)sValue.get(vo.getDbName());
////			  dbValue=(Hashtable)informixData.get("informix");
//			dbValue.put("databaselist", databaseList);
//			dbValue.put("logList", logList);
//			
//			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String time1 = sdf.format(new Date());
//			String newip=SysUtil.doip(vo.getIpAddress());						
//		
//			String starttime1 = time1 + " 00:00:00";
//			String totime1 = time1 + " 23:59:59";
//			
//			Hashtable ConnectUtilizationhash = new Hashtable();
//			I_HostCollectData hostmanager=new HostCollectDataManager();
//			try{
//				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			if (ConnectUtilizationhash.get("avgpingcon")!=null)
//				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
//			if(pingconavg != null){
//				pingconavg = pingconavg.replace("%", "");
//			}
//			//��ͼ
//			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
//			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);
//
//			//pingƽ��ֵ
//			maxhash = new Hashtable();
//			maxhash.put("avgpingcon",pingconavg);
//
//			//imgurlhash
//			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
//			
//			  avgpingcon = new Double(pingconavg+"").doubleValue();
//			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
//			  DefaultPieDataset dpd = new DefaultPieDataset();
//			  dpd.setValue("������",avgpingcon);
//			  dpd.setValue("��������",100 - avgpingcon);
//			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
//			  
//			request.setAttribute("list", list);
//			request.setAttribute("startdate", b_time);
//			request.setAttribute("todate", t_time);
//			request.setAttribute("imgurl",imgurlhash);
//			request.setAttribute("max",maxhash);
//			request.setAttribute("chart1",chart1);
//			//request.setAttribute("sValue",sValue);
//			request.setAttribute("dbValue",dbValue);
//			request.setAttribute("avgpingcon", avgpingcon);
//		        
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(dao != null){
//				dao.close();
//			}
//		}
//		return "/application/db/informixlog.jsp";
//    }
	
	private String informixabout()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String pingconavg ="0";
		try{
			vo = (DBVo)dao.findByID(getParaValue("id"));
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//	Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			List aboutList = dao.getInformix_nmsabout(serverip);
			if("1".equalsIgnoreCase(statusStr)){
				runstr = "��������";
			}

			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			dbValue = (Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("aboutlist", aboutList);

			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
			
		    	status = getParaIntValue("status");
		    	level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
		    	
		    	b_time = getParaValue("startdate");
				t_time = getParaValue("todate");
		    	
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				if (b_time == null){
					b_time = sdf1.format(new Date());
				}
				if (t_time == null){
					t_time = sdf1.format(new Date());
				}
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					EventListDao eventdao = new EventListDao();
					list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",
							user.getBusinessids(),vo.getId());
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			request.setAttribute("list", list);
			request.setAttribute("startdate", b_time);
			request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/application/db/informixabout.jsp";
    }
	
	/**
	 * �鿴informix��log��Ϣ
	 * @return
	 */
	private String informixlog()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		
		Hashtable sValue = new Hashtable();
		Hashtable dbValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String detail = "";
		String b_time ="";
		String t_time = "";
		String pingconavg ="0";
		double avgpingcon = 0;
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			vo = (DBVo)dao.findByID(id);
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			Hashtable informixData=new Hashtable();
			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//				runstr = "��������";
//			}*/
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData == null)informixData = new Hashtable();
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
			List databaseList = dao.getInformix_nmsdatabase(serverip);
			List ioList = dao.getInformix_nmsio(serverip);
			if("1".equalsIgnoreCase(statusStr)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
			//dbValue = (Hashtable)sValue.get(vo.getDbName());
//			  dbValue=(Hashtable)informixData.get("informix");
			dbValue.put("databaselist", databaseList);
			dbValue.put("iolist", ioList);
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getInformixmonitordata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			dbValue = (Hashtable)informixData.get("informix");

			
			//InformixVO informixVO = (InformixVO)sValue.get("infromixVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			String strStartDay = getParaValue("startdate");
			String strToDay = getParaValue("todate");
			if(strStartDay!=null && !"".equals(strStartDay)){
			starttime1 = strStartDay+" 00:00:00";
			}
			if(strToDay!=null && !"".equals(strToDay)){
			totime1 = strToDay+" 23:59:59";
			}

			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
			
		    	status = getParaIntValue("status");
		    	level1 = getParaIntValue("level1");
		    	detail = getParaValue("detail");
		    	if(detail == null){
		    		detail = "";
		    	}else{
		    		detail.trim();
		    	}
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
		    	
		    	
		    	b_time = getParaValue("startdate");
				t_time = getParaValue("todate");
		    	
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				if (b_time == null){
					b_time = sdf1.format(new Date());
				}
				if (t_time == null){
					t_time = sdf1.format(new Date());
				}
				try{
//					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
//					EventListDao eventdao = new EventListDao();
//					list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",
//							user.getBusinessids(),vo.getId());
					//dao = new DBDao();
					list = dao.getInformixLogList(starttime1,totime1,newip,detail);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			request.setAttribute("list", list);
			request.setAttribute("detail", detail);
			request.setAttribute("startdate", b_time);
			request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			//request.setAttribute("sValue",sValue);
			request.setAttribute("dbValue",dbValue);  
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/informixlog.jsp";
    }
	
	public String execute(String action) 
	{	    
        if(action.equals("ready_add"))
        	return "/application/db/add.jsp";
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new DBDao();
    	    setTarget("/application/db/edit.jsp");
            return readyEdit(dao);
        }
        if(action.equals("informixping"))
        	return informixcap();
        if(action.equals("informixcap"))
            return informixcap();
        if(action.equals("informixdb"))
            return informixdb();
        if(action.equals("informixdevice"))
            return informixdevice();
        if(action.equals("informixevent"))
            return informixevent();
        if(action.equals("informixio"))
            return informixio();
        if(action.equals("informixabout"))
            return informixabout();
        if(action.equals("informixlog")){
        	return informixlog();
        }
        if(action.equals("sychronizeData"))
        {
        	return sychronizeData();
        }
        if(action.equals("isDatabaseOK"))
		{
			return isDatabaseOK();
		}
        //HONGLI ADD START
        if(action.equals("informixManagerPingReport")){
        	return informixManagerPingReport();
        }
        if(action.equals("informixManagerPingReportQuery")){
        	return informixManagerPingReportQuery();
        }
        if(action.equals("informixManagerNatureReport")){
        	return informixManagerNatureReport();
        }
        if(action.equals("informixManagerNatureReportQuery")){
        	return informixManagerNatureReportQuery();
        }
        if(action.equals("informixManagerCldReport")){
        	return informixManagerCldReport();
        }
        if(action.equals("informixManagerCldReportQuery")){
        	return informixManagerCldReportQuery();
        }
        if(action.equals("informixManagerEventReport")){
        	return informixManagerEventReportQuery();
        }
        if(action.equals("informixManagerEventReportQuery")){
        	return informixManagerEventReportQuery();
        }
        if(action .equals("informixwaitingthread")){
        	return informixwaitingthread();
        }
        if(action.equals("informixbaractlog")){
        	return informixbaractlog();
        }
        //HONGLI ADD END
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	   private void p_draw_line(Hashtable hash,String title1,String title2,int w,int h){
	    	List list = (List)hash.get("list");
	    	try{
	    	if(list==null || list.size()==0){
	    		draw_blank(title1,title2,w,h);
	    	}
	    	else{
	    	String unit = (String)hash.get("unit");
	    	if (unit == null)unit="%";
	    	ChartGraph cg = new ChartGraph();
	    	
	    	TimeSeries ss = new TimeSeries(title1,Minute.class);
	    	TimeSeries[] s = {ss};
	    	for(int j=0; j<list.size(); j++){
	    			Vector v = (Vector)list.get(j);
	    			Double	d=new Double((String)v.get(0));			
	    			String dt = (String)v.get(1);
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    			Date time1 = sdf.parse(dt);				
	    			Calendar temp = Calendar.getInstance();
	    			temp.setTime(time1);
	    			Minute minute=new Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
	    			ss.addOrUpdate(minute,d);
	    	}
	    	cg.timewave(s,"x(ʱ��)","y("+unit+")",title1,title2,w,h);
	    	
	    	
	    	
	    	
	    	}
	    	hash = null;
	    	}
	    	catch(Exception e){e.printStackTrace();}
	    	}
	    
	    private void draw_blank(String title1,String title2,int w,int h){
	    	ChartGraph cg = new ChartGraph();
	    	TimeSeries ss = new TimeSeries(title1,Minute.class);
	    	TimeSeries[] s = {ss};
	    	try{
	    		Calendar temp = Calendar.getInstance();
	    		Minute minute=new Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
	    		ss.addOrUpdate(minute,null);
	    		cg.timewave(s,"x(ʱ��)","y",title1,title2,w,h);
	    	}
	    	catch(Exception e){e.printStackTrace();}
	    }
		static{
			rsc_type_ht.put("1","NULL ��Դ��δʹ�ã�");
			rsc_type_ht.put("2","���ݿ�");
			rsc_type_ht.put("3","�ļ�");
			rsc_type_ht.put("4","����");
			rsc_type_ht.put("5","��");
			rsc_type_ht.put("6","ҳ");
			rsc_type_ht.put("7","��");
			rsc_type_ht.put("8","��չ����");
			rsc_type_ht.put("9","RID���� ID)");
			rsc_type_ht.put("10","Ӧ�ó���");

			req_mode_ht.put("0","NULL");
			req_mode_ht.put("1","Sch-S");
			req_mode_ht.put("2","Sch-M");
			req_mode_ht.put("3","S");
			req_mode_ht.put("4","U");
			req_mode_ht.put("5","X");
			req_mode_ht.put("6","IS");
			req_mode_ht.put("7","IU");
			req_mode_ht.put("8","IX");
			req_mode_ht.put("9","SIU");
			req_mode_ht.put("10","SIX");
			req_mode_ht.put("11","UIX");
			req_mode_ht.put("12","BU");
			req_mode_ht.put("13","RangeS_S");
			req_mode_ht.put("14","RangeS_U");
			req_mode_ht.put("15","RangeI_N");
			req_mode_ht.put("16","RangeI_S");
			req_mode_ht.put("17","RangeI_U");
			req_mode_ht.put("18","RangeI_X");
			req_mode_ht.put("19","RangeX_S");
			req_mode_ht.put("20","RangeX_U");
			req_mode_ht.put("21","RangeX_X");
			
			req_status_ht.put("1","������	");
			req_status_ht.put("2","����ת��");
			req_status_ht.put("3","���ڵȴ�");
			
			req_ownertype_ht.put("1","����");
			req_ownertype_ht.put("2","�α�");
			req_ownertype_ht.put("3","�Ự");
			req_ownertype_ht.put("4","ExSession");
			
			mode_ht.put("0","����Ȩ������Դ");
			mode_ht.put("1","�ܹ��ȶ��ԣ�ȷ�������κλỰ���Ƽܹ�Ԫ���ϵļܹ��ȶ�����ʱ��ȥ�ܹ�Ԫ�أ�����������");
			mode_ht.put("2","�ܹ��޸ģ��������κ�Ҫ����ָ����Դ�ܹ��ĻỰ���п��ơ�ȷ��û�������ĻỰ��������ָ���Ķ���");
			mode_ht.put("3","������Ȩ���ƻỰ����Դ���й�����ʡ�");
			mode_ht.put("4","���£���ʾ�����տ��ܸ��µ���Դ�ϻ�ȡ�����������ڷ�ֹ������ʽ�����������������ڶ���Ự������Դ�����Ժ���ܸ�����Դʱ������");
			mode_ht.put("5","��������Ȩ���ƻỰ����Դ�����������ʡ�");
			mode_ht.put("6","��������ʾ���⽫ S ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("7","������£���ʾ���⽫ U ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("8","������������ʾ���⽫ X ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("9","����������£���ʾ������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ���й�����ʡ�");
			mode_ht.put("10","����������������ʾ������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ���й�����ʡ�");
			mode_ht.put("11","����������������ʾ��������������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ��");
			mode_ht.put("12","����������");
			mode_ht.put("13","�������Χ�͹�����Դ������ʾ�ɴ��з�Χɨ�衣");
			mode_ht.put("14","�������Χ�͸�����Դ������ʾ�ɴ��и���ɨ�衣");
			mode_ht.put("15","�������Χ�Ϳ���Դ���������������в����¼�֮ǰ���Է�Χ��");
			mode_ht.put("16","ͨ�� RangeI_N �� S �����ص������ļ���Χת����");
			mode_ht.put("17","ͨ�� RangeI_N �� U �����ص������ļ���Χת����");
			mode_ht.put("18","ͨ�� RangeI_N �� X �����ص������ļ���Χת����");
			mode_ht.put("19","ͨ�� RangeI_N �� RangeS_S �����ص������ļ���Χת����");
			mode_ht.put("20","ͨ�� RangeI_N �� RangeS_U �����ص������ļ���Χת����");
			mode_ht.put("21","��������Χ��������Դ������ת�����ڸ��·�Χ�еļ�ʱʹ�á�");
		}
		private String sychronizeData()
		{
			DBRefreshHelper dbRefreshHelper = new DBRefreshHelper();
			
			String dbvoId = request.getParameter("id");
			String dbPage = request.getParameter("dbPage");
			DBDao dbDao = new DBDao();
			DBVo dbVo = (DBVo)dbDao.findByID(dbvoId);
			dbRefreshHelper.execute(dbVo);
			if(dbPage.equals("informixping"))
			{
				return informixping();
			}
			if(dbPage.equals("informixdb"))
			{
				return informixdb();
			}
			if(dbPage.equals("informixdevice"))
			{
				return informixdevice();
			}
			if(dbPage.equals("informixevent"))
			{
				return informixevent();
			}
			
			return "/application/db/informixping.jsp";
		}
		private String isDatabaseOK()
		{
			DBDao dbdao = null;
			OraclePartsDao oraclePartsDao = null;
			String myip = request.getParameter("myip");
			String myport = request.getParameter("myport");
			int port = Integer.parseInt(myport);
			String myUser = request.getParameter("myUser");
			String myPassword = request.getParameter("myPassword");
			String id = request.getParameter("id");
			String dbType = request.getParameter("dbType");
			request.setAttribute("dbType", dbType);
			DBVo vo =  null;
			boolean informixIsOK = false;
			try {
				//ddddddd
				dbdao = new DBDao();
				vo = (DBVo)dbdao.findByID(id);
				String dbname = vo.getDbName();
				String alias = vo.getAlias();
				informixIsOK = dbdao.getInformixIsOk(myip, myport, myUser, myPassword, dbname, alias);
				request.setAttribute("dbname", dbname);
				request.setAttribute("alias", alias);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbdao != null)
					dbdao.close();
			}
			request.setAttribute("oracleIsOK", informixIsOK);
			return "/tool/dbisok.jsp";
		}
		
		/**
		 * @author HONGLI
		 * date 2010-11-12
		 * informix�����Ա���ҳ
		 * @return
		 */
		public String informixManagerPingReport(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable informixData=new Hashtable();
//				Hashtable mino=new Hashtable();
//				Hashtable sysValue = new Hashtable();
//				sysValue = ShareData.getInformixmonitordata();
//				if(sysValue!=null&&sysValue.size()>0){
//					if(sysValue.containsKey(vo.getIpAddress())){
//						mino=(Hashtable)sysValue.get(vo.getIpAddress());
//						if(mino.contains(vo.getDbName())){
//							informixData=(Hashtable)mino.get(vo.getDbName());
//						}
//						if(informixData.containsKey("status")){
//							String p_status = (String)informixData.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
////									runstr = "��������";
//									pingnow = "100";
//								}
//							}
//						}
//					}
//				}
				dao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getDbName();
				String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
				if("1".equalsIgnoreCase(status)){
					pingnow = "100";
				}
				dao.close();
				
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��

				if(pingmin != null){
					pingmin = pingmin.replace("%", "");//��С��ͨ��
				}
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("avgpingcon", avgpingcon);
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbInformixReport.jsp";
		}
		
		/**
		 * @author HONGLI 
		 * date 2010-11-12
		 * �����Ա��� ��ʱ���ѯ
		 * @return
		 */
		public String informixManagerPingReportQuery(){
			return informixManagerPingReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * ���ܱ��� 
		 * @return
		 */
		public String informixManagerNatureReport(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			Hashtable dbValue = new Hashtable();
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable informixData=new Hashtable();
//				Hashtable mino=new Hashtable();
//				Hashtable sysValue = new Hashtable();
//				sysValue = ShareData.getInformixmonitordata();
//				/*if(dao.getInformixIsOk(vo.getIpAddress(),vo.getPort()+"",vo.getUser(),vo.getPassword(),vo.getDbName(),vo.getAlias())){
//					runstr = "��������";
//				}*/
//				if(sysValue!=null&&sysValue.size()>0){
//					if(sysValue.containsKey(vo.getIpAddress())){
//						mino=(Hashtable)sysValue.get(vo.getIpAddress());
//						if(mino.contains(vo.getDbName())){
//							informixData=(Hashtable)mino.get(vo.getDbName());
//						}
//						if(informixData.containsKey("status")){
//							String p_status = (String)informixData.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
////									runstr = "��������";
//									pingnow = "100";
//								}
//							}
//						}
//						if(informixData.containsKey("informix")){
//							dbValue = (Hashtable)informixData.get("informix");
//						}
//					}
//				}
				dao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getDbName();
				String status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
				List databaseList = dao.getInformix_nmsdatabase(serverip);
				List sessionList = dao.getInformix_nmssession(serverip);
				List lockList = dao.getInformix_nmslock(serverip);
				List spaceList = dao.getInformix_nmsspace(serverip);
				dao.close();
				if("1".equalsIgnoreCase(status)){
					pingnow = "100";
				}
				dbValue.put("databaselist", databaseList);
				dbValue.put("sessionList", sessionList);
				dbValue.put("lockList", lockList);
				dbValue.put("informixspaces", spaceList);
				
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				if(pingmax == null || !"".equals(pingmax)){
					pingmax = pingmax.replace("%", "");//ƽ����ͨ��
				}
				if(pingmin != null || !"".equals(pingmin)){
					pingmin = pingmin.replace("%", "");//ƽ����ͨ��
				}
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbInformixNatureReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * ���ܱ���  �����ڲ�ѯ
		 * @return
		 */
		public String informixManagerNatureReportQuery(){
			return informixManagerNatureReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * �ۺϱ���
		 * @return
		 */
		public String informixManagerCldReport(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String runstr = "<font color=red>����ֹͣ</font>";
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			Hashtable dbValue = new Hashtable();
			String downnum = "";
			List eventList = new ArrayList();//�¼��б�
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable informixData=new Hashtable();
//				Hashtable mino=new Hashtable();
//				Hashtable sysValue = new Hashtable();
//				sysValue = ShareData.getInformixmonitordata();
//				if(sysValue!=null&&sysValue.size()>0){
//					if(sysValue.containsKey(vo.getIpAddress())){
//						mino=(Hashtable)sysValue.get(vo.getIpAddress());
//						if(mino.contains(vo.getDbName())){
//							informixData=(Hashtable)mino.get(vo.getDbName());
//						}
//						if(informixData.containsKey("status")){
//							String p_status = (String)informixData.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									runstr = "��������";
//									pingnow = "100";
//								}
//							}
//						}
//						if(informixData.containsKey("informix")){
//							dbValue = (Hashtable)informixData.get("informix");
//						}
//					}
//				}
				dao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getDbName();
				String statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
				List databaseList = dao.getInformix_nmsdatabase(serverip);
				List sessionList = dao.getInformix_nmssession(serverip);
				List lockList = dao.getInformix_nmslock(serverip);
				List logList = dao.getInformix_nmslog(serverip);
				List ioList = dao.getInformix_nmsio(serverip);
				List spaceList = dao.getInformix_nmsspace(serverip);
				List configList = dao.getInformix_nmsconfig(serverip);
				dao.close();
				if("1".equalsIgnoreCase(statusStr)){
					runstr = "��������";
				}
				dbValue.put("databaselist", databaseList);
				dbValue.put("sessionList", sessionList);
				dbValue.put("lockList", lockList);
				dbValue.put("informixspaces", spaceList);
				dbValue.put("informixlog", logList);
				dbValue.put("iolist", ioList);
				dbValue.put("configList", configList);
				
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				if(pingmax == null || !"".equals(pingmax)){
					pingmax = pingmax.replace("%", "");//ƽ����ͨ��
				}
				if(pingmin != null || !"".equals(pingmin)){
					pingmin = pingmin.replace("%", "");//ƽ����ͨ��
				}
				avgpingcon = new Double(pingconavg+"").doubleValue();
				if (ConnectUtilizationhash.get("downnum") != null){
					downnum = (String) ConnectUtilizationhash.get("downnum");
				}
				
				//�õ����еȼ�
				DBTypeDao dbTypeDao = new DBTypeDao();
				int count = 0;
				try {
					count = dbTypeDao.finddbcountbyip(vo.getIpAddress());
					request.setAttribute("count", count);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbTypeDao.close();
				}
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
				
//				�¼��б�
				int status = getParaIntValue("status");
		    	int level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
					
					//ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("list", eventList);
			request.setAttribute("downnum", downnum);
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbInformixCldReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * �ۺϱ���  �����ڲ�ѯ
		 * @return
		 */
		public String informixManagerCldReportQuery(){
			return informixManagerCldReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * �¼�����   
		 * @return
		 */
		public String informixManagerEventReport(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			DBTypeVo typevo = null;
			String id = (String)session.getAttribute("id");
			String downnum = "";
			List eventList = new ArrayList();//�¼��б�
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newip", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"INFORMIXPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if (ConnectUtilizationhash.get("downnum") != null){
					downnum = (String) ConnectUtilizationhash.get("downnum");
				}
				
				//�õ����еȼ�
				DBTypeDao dbTypeDao = new DBTypeDao();
				int count = 0;
				try {
					count = dbTypeDao.finddbcountbyip(vo.getIpAddress());
					request.setAttribute("count", count);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbTypeDao.close();
				}
				
//				�¼��б�
				int status = getParaIntValue("status");
		    	int level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
					
					//ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("list", eventList);
			request.setAttribute("downnum", downnum);
			request.setAttribute("vo", vo);
			request.setAttribute("typevo", typevo);
			return "/capreport/db/showDbInformixEventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-12
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String informixManagerEventReportQuery(){
			return informixManagerEventReport();
		}
		
}