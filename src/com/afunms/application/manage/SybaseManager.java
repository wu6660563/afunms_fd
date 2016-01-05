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
import com.afunms.common.*;
import com.afunms.common.util.*;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.event.dao.EventListDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.loader.*;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.SybaseVO;
import com.afunms.system.model.User;
import com.afunms.topology.util.*;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.util.DBPool;
import com.afunms.application.util.DBRefreshHelper;
import com.afunms.application.util.IpTranslation;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.polling.impl.*;
import com.afunms.polling.api.*;

import com.afunms.report.jfree.ChartCreator;

import org.jfree.data.general.DefaultPieDataset;

public class SybaseManager extends BaseManager implements ManagerInterface
{
	public static Hashtable rsc_type_ht = new Hashtable();
	public static Hashtable req_mode_ht = new Hashtable();
	public static Hashtable mode_ht = new Hashtable();
	public static Hashtable req_status_ht = new Hashtable();
	public static Hashtable req_ownertype_ht = new Hashtable();  

	
	private String sybaseping()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			
			/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
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
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybaseping.jsp";
    }
	
	private String sybasecap()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
//						SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
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
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybasecap.jsp";
    }
	
	private String sybasedb()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
		/*	dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
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
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybasedb.jsp";
    }
	
	private String sybasedevice()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
				/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}   
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybasedevice.jsp";
    }
	
	private String sybaseproc()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
				/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}   
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybaseprocess.jsp";
    }
	
	private String sybaseengine()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
				/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String status = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			if(status.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}   
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybaseengine.jsp";
    }
	
	private String sybaseevent()
    {    	   
		DBVo vo = new DBVo();
		
		Hashtable sysValue = new Hashtable();
		Hashtable sValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		//String flag = "";
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		String id = getParaValue("id");
		request.setAttribute("id", id);
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
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
				/*dao = new DBDao();
			try{
				if(dao.getSysbaseIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(), Integer.parseInt(vo.getPort()))){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			SybaseVO sysbaseVO = new SybaseVO();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			sysbaseVO = dao.getSybaseDataByServerip(serverip); 
			String statusStr = "0";
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				statusStr = (String)tempStatusHashtable.get("status");
			}
			if(statusStr.equals("1")){
				runstr = "��������";
			}
			if(dao != null){
				dao.close();
			}
			request.setAttribute("runstr", runstr);
			//sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			SybaseVO sysbaseVO = (SybaseVO)sValue.get("sysbaseVO");
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}   
			
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
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					EventListDao eventdao = new EventListDao();
					try{
						list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				request.setAttribute("list", list);
				request.setAttribute("startdate", b_time);
				request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("sysbaseVO",sysbaseVO);
			
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sybaseevent.jsp";
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
        if(action.equals("sybaseping"))
        	return sybasecap();
        if(action.equals("sybasecap"))
            return sybasecap();
        if(action.equals("sybasedb"))
            return sybasedb();
        if(action.equals("sybasedevice"))
            return sybasedevice();
        if(action.equals("sybaseproc"))
            return sybaseproc();
        if(action.equals("sybaseengine"))
            return sybaseengine();
        if(action.equals("sybaseevent"))
            return sybaseevent();
        if(action.equals("sychronizeData"))
        {
        	return sychronizeData();
        }
        if(action.equals("isDatabaseOK"))
        	return isDatabaseOK();
        if(action.equals("sybaseManagerPingReport")){
        	return sybaseManagerPingReport();
        }
        if(action.equals("sybaseManagerPingReportQuery")){
        	return sybaseManagerPingReportQuery();
        }
        if(action.equals("sybaseManagerNatureReport")){
        	return sybaseManagerNatureReport();
        }
        if(action.equals("sybaseManagerNatureReportQuery")){
        	return sybaseManagerNatureReportQuery();
        }
        if(action.equals("sybaseManagerCldReport")){
        	return sybaseManagerCldReport();
        }
        if(action.equals("sybaseManagerCldReportQuery")){
        	return sybaseManagerCldReportQuery();
        }
        if(action.equals("sybaseManagerEventReport")){
        	return sybaseManagerEventReport();
        }
        if(action.equals("sybaseManagerEventReportQuery")){
        	return sybaseManagerEventReportQuery();
        }
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
			if(dbPage.equals("sybasecap"))
			{
				return sybasecap();
			}
			if(dbPage.equals("sybasedb"))
			{
				return sybasedb();
			}
			if(dbPage.equals("sybasedevice"))
			{
				return sybasedevice();
			}
			if(dbPage.equals("sybaseevent"))
			{
				return sybaseevent();
			}
			
			return "/application/db/sybasecap.jsp";
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
			boolean sybaseIsOK = false;
			try {
				//ddddddd
				dbdao = new DBDao();
				vo = (DBVo)dbdao.findByID(id);
				String dbname = vo.getDbName();
				sybaseIsOK = dbdao.getSysbaseIsOk(myip, myUser, myPassword, port);
				request.setAttribute("dbname", dbname);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbdao != null)
					dbdao.close();
			}
			request.setAttribute("oracleIsOK", sybaseIsOK);
			return "/tool/dbisok.jsp";
		}
		/**
		 * @author HONGLI
		 * date 2010-11-11
		 * Sybase�����Ա���ҳ
		 * @return
		 */
		public String sybaseManagerPingReport(){
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
//				Hashtable allsqlserverdata = ShareData.getSysbasedata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									pingnow = "100.0";
//								}
//							}
//						}
//					}
//				}
				//��ȡsybase��Ϣ
				SybaseVO sysbaseVO = new SybaseVO();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				dao = new DBDao();
				String serverip = hex+":"+vo.getId();
				sysbaseVO = dao.getSybaseDataByServerip(serverip); 
				String status = "0";
				Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
				if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
					status = (String)tempStatusHashtable.get("status");
				}
				if(status.equals("1")){
					pingnow = "100.0";
				}
				if(dao != null){
					dao.close();
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime,totime);
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
			return "/capreport/db/showDbSybaseReport.jsp";
		}
		
		/**
		 * @author HONGLI 
		 * date 2010-11-11
		 * Sybase�����Ա��� ��ʱ���ѯ
		 * @return
		 */
		public String sybaseManagerPingReportQuery(){
			return sybaseManagerPingReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * ���ܱ��� 
		 * @return
		 */
		public String sybaseManagerNatureReport(){
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
			SybaseVO sysbaseVO = new SybaseVO();
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
//				Hashtable allsqlserverdata = ShareData.getSysbasedata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									pingnow = "100.0";
//								}
//							}
//						}
//						if(ipsqlserverdata.containsKey("dbValue")){
//							dbValue = (Hashtable)ipsqlserverdata.get("dbValue");
//						}
//						if(ipsqlserverdata.containsKey("sysbaseVO")){
//							sysbaseVO = (SybaseVO)ipsqlserverdata.get("sysbaseVO");
//						}
//					}
//				}
				//��ȡsybase��Ϣ
//				SybaseVO sysbaseVO = new SybaseVO();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				dao = new DBDao();
				String serverip = hex+":"+vo.getId();
				sysbaseVO = dao.getSybaseDataByServerip(serverip); 
				String status = "0";
				Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
				if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
					status = (String)tempStatusHashtable.get("status");
				}
				if(status.equals("1")){
					pingnow = "100.0";
				}
				if(dao != null){
					dao.close();
				}
				
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime,totime);
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
			request.setAttribute("sysbaseVO", sysbaseVO);
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbSybaseNatureReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * ���ܱ���  �����ڲ�ѯ
		 * @return
		 */
		public String sybaseManagerNatureReportQuery(){
			return sybaseManagerNatureReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �ۺϱ���
		 * @return
		 */
		public String sybaseManagerCldReport(){
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
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			String runstr = "<font color=red>����ֹͣ</font>";
			Hashtable dbValue = new Hashtable();
			String downnum = "";
//			���ݿ����еȼ�=====================
			String grade = "��";
			Hashtable mems = new Hashtable();//�ڴ���Ϣ
			Hashtable sysValue = new Hashtable();
			SybaseVO sysbaseVO = new SybaseVO();
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
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable allsqlserverdata = ShareData.getSysbasedata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									runstr = "��������";
//									pingnow = "100.0";
//								}
//							}
//						}
//						if(ipsqlserverdata.containsKey("dbValue")){
//							dbValue = (Hashtable)ipsqlserverdata.get("dbValue");
//						}
//						if(ipsqlserverdata.containsKey("retValue")){
//							mems = (Hashtable)((Hashtable)ipsqlserverdata.get("retValue")).get("mems");
//						}
//						if(ipsqlserverdata.containsKey("sysValue")){
//							sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//						}
//						if(ipsqlserverdata.containsKey("sysbaseVO")){
//							sysbaseVO = (SybaseVO)ipsqlserverdata.get("sysbaseVO");
//						}
//					}
//				}
				//��ȡsybase��Ϣ
//				SybaseVO sysbaseVO = new SybaseVO();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				dao = new DBDao();
				String serverip = hex+":"+vo.getId();
				sysbaseVO = dao.getSybaseDataByServerip(serverip); 
				String statusStr = "0";
				Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
				if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
					statusStr = (String)tempStatusHashtable.get("status");
				}
				if(statusStr.equals("1")){
					runstr = "��������";
					pingnow = "100.0";
				}
				if(dao != null){
					dao.close();
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newip", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime,totime);
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
				if (ConnectUtilizationhash.get("downnum") != null){
					downnum = (String) ConnectUtilizationhash.get("downnum");
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
				
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
				
				if (count>0) {
					grade = "��";
				}
				if (!"0".equals(downnum)) {
					grade = "��";
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
			request.setAttribute("sysbaseVO", sysbaseVO);
			request.setAttribute("sysValue", sysValue);
			request.setAttribute("downnum", downnum);
			request.setAttribute("mems", mems);
			request.setAttribute("grade", grade);
			request.setAttribute("vo", vo);
			request.setAttribute("runstr", runstr);
			request.setAttribute("typevo", typevo);
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbSybaseCldReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �ۺϱ���  �����ڲ�ѯ
		 * @return
		 */
		public String sybaseManagerCldReportQuery(){
			return sybaseManagerCldReport();
		}
		
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   
		 * @return
		 */
		public String sybaseManagerEventReport(){
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
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SYSPing","ConnectUtilization",starttime,totime);
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
			return "/capreport/db/showDbSybaseEventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String sybaseManagerEventReportQuery(){
			return sybaseManagerEventReport();
		}
		
}