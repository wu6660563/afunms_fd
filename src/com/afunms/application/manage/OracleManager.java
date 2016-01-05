/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.model.OracleLockInfo;
import com.afunms.application.util.DBPool;
import com.afunms.application.util.DBRefreshHelper;
import com.afunms.application.util.IpTranslation;
import com.afunms.capreport.manage.DBCapReportManager;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DateE;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.manage.PollMonitorManager;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.system.model.User;
import com.afunms.system.vo.FlexVo;
import com.afunms.topology.util.KeyGenerator;
import com.lowagie.text.DocumentException;
import com.sybase.jdbc2.timedio.Dbio;

public class OracleManager extends BaseManager implements ManagerInterface
{
//	HONGLI 
	//jhl add
	DateE datemanager = new DateE();  
	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
	I_HostCollectData hostmanager=new HostCollectDataManager();
	I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//jhl end
	private String list()
	{
		DBDao dao = new DBDao();
		List list = new ArrayList();  
		try{
			list = dao.loadAll();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i=0;i<list.size();i++)
		{
			DBVo vo = (DBVo)list.get(i);
			Node DBNode = PollingEngine.getInstance().getNodeByID(vo.getId());
			if(DBNode==null)
			   vo.setStatus(0);
			else
			   vo.setStatus(DBNode.getStatus());	
		}
		request.setAttribute("list",list);				
		return "/application/db/list.jsp";
	}
	private String doip(String ip){
//		  String newip="";
//		  for(int i=0;i<3;i++){
//			int p=ip.indexOf(".");
//			newip+=ip.substring(0,p);
//			ip=ip.substring(p+1);
//		  }
//		 newip+=ip;
		 //System.out.println("newip="+newip);
		 ip = SysUtil.doip(ip);
		 return ip;
	}
	//oracle�����Ա���  
	private String dboraReportdownusableload(){
		Date d = new Date();
		DBDao dao = null;
		Hashtable memValue = new Hashtable();
		String runstr = "����ֹͣ";
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String dbname = "";
		String dbnamestr = "";
		String typename = "ORACLE";
		Vector tableinfo_v = new Vector();
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable pingdata = ShareData.getPingdata();
		Vector vector = new Vector();
		DBVo vo = null;
		int row = 0;
		String sid = "";
		String pingmin = "0.0%";//HONG ADD ��С��ͨ��
		String pingnow = "0.0%";//HONGLI ADD ��ǰ��ͨ��
		try {
			ip = getParaValue("ipaddress");
			dao = new DBDao();
			vo = (DBVo) dao.findByCondition("ip_address", ip, 1).get(0);
			OraclePartsDao oracledao = new OraclePartsDao();
			List sidlist = new ArrayList();
			try{
				sidlist = oracledao.findOracleParts(vo.getId());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			if(sidlist != null){
				for (int j = 0; j < sidlist.size(); j++) {
					OracleEntity ora = (OracleEntity) sidlist.get(j);
					sid = ora.getId()+"";
					break;
				}
			}
			dbname = vo.getDbName() + "(" + ip + ")";
			dbnamestr = vo.getDbName();
//			String remoteip = request.getRemoteAddr();
			String newip = doip(ip);
			request.setAttribute("newip", newip);
			//���ڴ���ȡ��sga����Ϣ
//			dao = new DBDao();
//			try{
//				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), EncryptUtil.decode(vo.getPassword()))){
//					runstr = "��������";
//					pingnow = "100.0%";//HONGLI ADD
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				String statusStr = String.valueOf(statusHashtable.get("status"));
				if("1".equals(statusStr)){
					runstr = "��������";
					pingnow = "100.0%";//HONGLI ADD 
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			
//			dao = new DBDao();
//			try{
//				memValue = dao.getOracleMem(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),EncryptUtil.decode(vo.getPassword()));
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			Hashtable pinghash = hostmanager.getCategory(ip+":"+sid, "ORAPing","ConnectUtilization", starttime, totime);
			p_draw_line(pinghash, "", newip + "ConnectUtilization", 740, 120);
			String pingconavg = "";
			//HONGLI MODIFY START1
			if (pinghash.get("avgpingcon") != null){
				pingconavg = (String) pinghash.get("avgpingcon");
				pingmin = (String) pinghash.get("pingmax")+"%";
				maxping.put("pingmin", pingmin);
			}
			maxping.put("pingnow", pingnow);//���ӵ�ǰ��ͨ��
			p_draw_line(pinghash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			//HONGLI MODIFY END1
			String ConnectUtilizationmax = "";
			maxping.put("avgpingcon", pingconavg);
			if (pinghash.get("max") != null) {
				ConnectUtilizationmax = (String) pinghash.get("max");
			}
			maxping.put("pingmax", ConnectUtilizationmax);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("hash", hash);
		request.setAttribute("max", maxhash);
		request.setAttribute("memmaxhash", memmaxhash);
		request.setAttribute("memavghash", memavghash);
		request.setAttribute("diskhash", diskhash);
		request.setAttribute("memhash", memhash);
		Hashtable reporthash = new Hashtable();
		Vector pdata = (Vector) pingdata.get(ip);
		if (pdata != null && pdata.size() > 0) {
			for (int m = 0; m < pdata.size(); m++) {
				Pingcollectdata hostdata = (Pingcollectdata) pdata.get(m);
				if (hostdata.getSubentity().equals("ConnectUtilization")) {
					reporthash.put("time", hostdata.getCollecttime());
					reporthash.put("Ping", hostdata.getThevalue());
					reporthash.put("ping", maxping);
				}
			}
		} else {
			reporthash.put("ping", maxping);
		}
//		String username = vo.getUser();
//		String userpw = vo.getPassword();
//		String servername = vo.getDbName();
//		int serverport = Integer.parseInt(vo.getPort());
//		dao = new DBDao();
//		try {
//			vector = dao.getOracleTableinfo(ip, serverport, servername,username, EncryptUtil.decode(userpw));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			dao.close();
//		}
		//��oracle崻�����
		String downnum = "0";
		Hashtable pinghash = new Hashtable();
		try {
			pinghash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing", "ConnectUtilization", starttime,totime);
			if (pinghash.get("downnum") != null)
				downnum = (String) pinghash.get("downnum");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//��ռ�==========�澯
		DBTypeDao dbTypeDao = new DBTypeDao();
		int count = 0;
		try {
			count = dbTypeDao.finddbcountbyip(ip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbTypeDao.close();
		}
		//���ݿ����еȼ�=====================
		String grade = "��";
		if (count>0) {
			grade = "��";
		}
		if (!"0".equals(downnum)) {
			grade = "��";
		}
		reporthash.put("dbname", dbname);
		reporthash.put("dbnamestr", dbnamestr);
		reporthash.put("starttime", starttime);//HONGLI MODIFY
		reporthash.put("totime", totime);//HONGLI MODIFY
		reporthash.put("memvalue", memValue);
		reporthash.put("typename", typename);
		reporthash.put("runstr", runstr);
		reporthash.put("downnum", downnum);
		reporthash.put("count", count+"");
		reporthash.put("grade", grade);
		reporthash.put("ip", ip);
		if (vector == null)
			vector = new Vector();
		reporthash.put("tableinfo_v", vector);
		String str = request.getParameter("str");// ��ҳ�淵���趨��strֵ�����жϣ�����excel�������word����
		if ("0".equals(str)) {
			AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),reporthash);
			report.createReportusa_oraXls("temp/dborausa_report.xls");
			request.setAttribute("filename", report.getFileName());
			SysLogger.info("filename" + report.getFileName());
			request.setAttribute("filename", report.getFileName());
		} else if ("1".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/dborausa_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath() + file;// ��ȡϵͳ�ļ���·��
				report1.createReportusa_oraDoc(fileName);
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("2".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),reporthash);
			try {
				String file = "temp/dborausa_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath()+ file;// ��ȡϵͳ�ļ���·��
				report1.createReportusa_oraPDF(fileName);
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "/capreport/db/download.jsp";
	}
	//oracle �����Ա���
	private String dboraReportdownusable(){
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Hashtable dbio = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		//String sid=this.getParaValue("sid");
		String id = (String) session.getAttribute("id");
		String sid = (String) session.getAttribute("sid");
		String pingmin = "";//HONGLI ADD ��С��ͨ��
		String pingnow = "0.0";//HONGLI ADD ��ǰ��ͨ��
		try{
			DBDao dao = new DBDao();
			try{
				request.setAttribute("id",id);
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
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*  modify  zhao ----------------------------*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//							pingnow = "100.0";//HONGLI ADD 
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("tableinfo_v")){
//					tableinfo_v=(Vector)iporacledata.get("tableinfo_v");
//				}
//				if(iporacledata.containsKey("dbio")){
//					dbio=(Hashtable)iporacledata.get("dbio");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				String statusStr = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
//			waitv = dao.getOracle_nmsorawait(serverip);
//			userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
				if("1".equals(statusStr)){
					runstr = "��������";
					pingnow = "100.0";//HONGLI ADD 
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				dao.close();
			}
			request.setAttribute("cursors", cursors);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}  
//			request.setAttribute("memPerfValue", memPerfValue);
//			if(iporacledata.containsKey("sysValue")){
//				sysValue=(Hashtable)iporacledata.get("sysValue");
//			}
//			request.setAttribute("sysvalue", sysValue);
			request.setAttribute("runstr", runstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
			request.setAttribute("newip", newip);
			session.setAttribute("Mytime1", time1);
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			session.setAttribute("Mystarttime1", time1);//HONGLI MODIFY
			session.setAttribute("Mytotime1", time1);
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			
			pingmin = (String)ConnectUtilizationhash.get("pingmax");//HONGLI ADD ��С��ͨ��
			
			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  //��ͼ
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);
			  //HONGLI ADD START1
//			  //��ͼ----------------------
//				String timeType = "minute";
//				PollMonitorManager pollMonitorManager = new PollMonitorManager();
//				pollMonitorManager.chooseDrawLineType(timeType,
//						ConnectUtilizationhash, "��ͨ��",
//						newip + "ConnectUtilization", 740, 150);
//				// pollMonitorManager.p_draw_line(cpuhash, "", newip + "cpu", 750,
//				// 150);
//				// pollMonitorManager.draw_column(diskhash, "", newip + "disk",
//				// 750,150);
//				// pollMonitorManager.p_drawchartMultiLine(memoryhash[0], "", newip
//				// + "memory", 750, 150);
//				// ��ͼ-----------------------------
				//HONGLI ADD START2
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("dbio",dbio);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		request.setAttribute("pingmin", pingmin);//HONGLI ADD ��С��ͨ��
		request.setAttribute("pingnow", pingnow);//HONGLI ADD ��ǰ��ͨ��
		return "/capreport/db/showDbPingReport.jsp";
	}
	// oracle ���ܱ���
	private String dboraReportdown(){
		Date d = new Date();
		DBVo vo = new DBVo();
		Hashtable memValue = new Hashtable();
		String runstr = "����ֹͣ";
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String dbname = "";
		String dbnamestr = "";
		String typename = "ORACLE";
		Vector tableinfo_v = new Vector();
		Hashtable hash = new Hashtable();// "Cpu",--current
		Hashtable memhash = new Hashtable();// mem--current
		Hashtable diskhash = new Hashtable();
		Hashtable memmaxhash = new Hashtable();// mem--max
		Hashtable memavghash = new Hashtable();// mem--avg
		Hashtable maxhash = new Hashtable();// "Cpu"--max
		Hashtable maxping = new Hashtable();// Ping--max
		Hashtable ConnectUtilizationhash = new Hashtable();
		DBDao dao = new DBDao();
		String id = (String) session.getAttribute("id");
		String sid = (String) session.getAttribute("sid");
		String pingconavg ="0";
		double avgpingcon = 0;
		String pingnow = "0.0";//��ǰ��ͨ��
		String pingmin = "0.0";//��С��ͨ��
		String pingmax = "0.0";//�����ͨ��
		Hashtable reporthash = new Hashtable();
		try{
			request.setAttribute("id",id);
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
		OraclePartsDao oracledao=new OraclePartsDao();
		try{
			OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
			vo.setDbName(oracle.getSid());
			vo.setCollecttype(oracle.getCollectType());
			vo.setManaged(oracle.getManaged());
			vo.setBid(oracle.getBid());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			oracledao.close();
		}
		I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash= hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime,totime);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if (ConnectUtilizationhash.get("avgpingcon")!=null)
			pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");//ƽ����ͨ��
		if(pingconavg != null){
			pingconavg = pingconavg.replace("%", "");
		}
		if (ConnectUtilizationhash.get("pingMax")!=null){
			pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
		}
		//HONGLI ADD START1
		pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
		//HONGLI ADD END1
		String newip=SysUtil.doip(vo.getIpAddress());
		//��ͼ
		p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);
		//2010-HONGLI
		Hashtable dbio = new Hashtable();
		Hashtable memPerfValue = new Hashtable();
		IpTranslation tranfer = new IpTranslation();
		String hex = tranfer.formIpToHex(vo.getIpAddress());
		String serverip = hex+":"+sid;
		try {
			dao = new DBDao();
			//ȡ״̬��Ϣ
			memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
			dbio = dao.getOracle_nmsoradbio(serverip);
			tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
			Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
			String status = String.valueOf(statusHashtable.get("status"));
			if("1".equals(status)){
				pingnow = "100";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			dao.close();
		}
		maxping.put("pingmax", pingmin+"%");//��С��ͨ��
		maxping.put("pingnow", pingnow+"%");
		maxping.put("avgpingcon", pingconavg+"%");//ƽ����ͨ��
		reporthash.put("dbname", typevo.getDbtype()+"("+vo.getIpAddress()+")");
		reporthash.put("starttime", starttime);
		reporthash.put("totime", totime);
		reporthash.put("dbio", dbio);
		reporthash.put("tableinfo_v", tableinfo_v);
		reporthash.put("ip", vo.getIpAddress());
		reporthash.put("ping", maxping);
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),reporthash);
		String str = request.getParameter("str");// ��ҳ�淵���趨��strֵ�����жϣ�����excel�������word����
		if ("0".equals(str)) {
			report.createReport_ora("temp/dbora_report.xls");
			request.setAttribute("filename", report.getFileName());
			SysLogger.info("filename" + report.getFileName());
			request.setAttribute("filename", report.getFileName());
		} else if ("1".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),reporthash);
			try { 
				String file = "temp/dbora_report.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath()+ file;// ��ȡϵͳ�ļ���·��
				report1.createReport_oraDoc(fileName);//word�ۺϱ���
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("2".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/dbora_report.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath()+ file;// ��ȡϵͳ�ļ���·��
				report1.createReport_oraPDF(fileName);//wordҵ�������
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		else if ("3".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/dbora_reportcheck.doc";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath()+ file;// ��ȡϵͳ�ļ���·��
				report1.createReport_oraNewDoc(fileName);//wordҵ�������
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("4".equals(str)) {
			ExcelReport1 report1 = new ExcelReport1(new IpResourceReport(),
					reporthash);
			try {
				String file = "temp/dbora_reportcheck.pdf";// ���浽��Ŀ�ļ����µ�ָ���ļ���
				String fileName = ResourceCenter.getInstance().getSysPath()+ file;// ��ȡϵͳ�ļ���·��
				report1.createReport_oraNewPDF(fileName,"pdf");//wordҵ�������
				request.setAttribute("filename", fileName);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		return "/capreport/db/download.jsp";
	}
	/**
	 * oracle ��ת�����ܱ���
	 * @return
	 */
    private String dbReport(){
    	String ip = getParaValue("ipaddress");
    	DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Hashtable dbio = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		//String sid=this.getParaValue("sid");
		String id = (String) session.getAttribute("id");
		String sid = (String) session.getAttribute("sid");
		String pingnow = "0.0";//HONGLI ADD ��ǰ��ͨ��
		try{
			DBDao dao = new DBDao();
			try{
				request.setAttribute("id",id);
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
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*  modify  zhao ----------------------------*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//							pingnow = "100.0";//HONGLI ADD ��ǰ��ͨ��
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("tableinfo_v")){
//					tableinfo_v=(Vector)iporacledata.get("tableinfo_v");
//				}
//				if(iporacledata.containsKey("dbio")){
//					dbio=(Hashtable)iporacledata.get("dbio");
//				}
////				if(iporacledata.containsKey("cursors")){
////					cursors=(Hashtable)iporacledata.get("cursors");
////				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				String statusStr = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				dbio = dao.getOracle_nmsoradbio(serverip);
				tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
//			waitv = dao.getOracle_nmsorawait(serverip);
//			userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
				if("1".equals(statusStr)){
					runstr = "��������";
					pingnow = "100.0";//HONGLI ADD ��ǰ��ͨ��
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				dao.close();
			}
			request.setAttribute("cursors", cursors);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
//			request.setAttribute("memPerfValue", memPerfValue);
//			if(iporacledata.containsKey("sysValue")){
//				sysValue=(Hashtable)iporacledata.get("sysValue");
//			}
			request.setAttribute("sysvalue", sysValue);
			request.setAttribute("runstr", runstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());
			request.setAttribute("newip", newip);
			session.setAttribute("Mytime1", time1);
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			session.setAttribute("Mystarttime1", time1);//HONGLI MODIFY
			session.setAttribute("Mytotime1", time1);
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash= hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");//ƽ����ͨ��
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//HONGLI ADD START1
			String pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
			//HONGLI ADD END1
			//��ͼ
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);
			
			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  request.setAttribute("avgpingcon", avgpingcon);
			  //HONGLI ADD START2
			  request.setAttribute("pingnow", pingnow);//��ǰ��ͨ��
			  request.setAttribute("pingmin", pingmin);//��С��ͨ��
			  //HONGLI ADD END2
			  //double notpingcon = 100 - avgpingcon;
			  request.setAttribute("notpingcon", 100 - avgpingcon);
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);       
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("dbio",dbio);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);//ƽ����ͨ��
    	return "/capreport/db/showDbReport.jsp";
    }   
    
  //HONGLI END#####
	private String add()
    {    	   
		DBVo vo = new DBVo();
    	vo.setId(KeyGenerator.getInstance().getNextKey());
    	vo.setUser(getParaValue("user"));
    	vo.setPassword(getParaValue("password"));        
        vo.setAlias(getParaValue("alias"));
        vo.setIpAddress(getParaValue("ip_address"));
        vo.setPort(getParaValue("port"));
        vo.setDbName(getParaValue("db_name"));
        vo.setCategory(getParaIntValue("category"));
        vo.setDbuse(getParaValue("dbuse"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setSendemail(getParaValue("sendemail"));
        String allbid = "";
        String[] businessids = getParaArrayValue("checkbox");
        if(businessids != null && businessids.length>0){
        	for(int i=0;i<businessids.length;i++){
        		
        		String bid = businessids[i];
        		allbid= allbid+bid+",";
        	}
        } 
        vo.setBid(allbid);
        vo.setManaged(getParaIntValue("managed"));
        vo.setDbtype(getParaIntValue("dbtype"));
        //�����ݿ������ӱ����ָ��
        //DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
        //dcDao.addDBMonitor(vo.getId(),vo.getIpAddress(),"mysql");
        
        //����ѯ�߳������ӱ����ӽڵ�
        //DBLoader loader = new DBLoader();
        //loader.loadOne(vo);
        //loader.close();
        
        DBDao dao = new DBDao();
        try{
        	dao.save(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }    
	
	public String delete()
	{
		String id = getParaValue("radio"); 
		DBDao dao = new DBDao();
		try{
			dao.delete(id);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		int nodeId = Integer.parseInt(id);
        PollingEngine.getInstance().deleteNodeByID(nodeId);
        DBPool.getInstance().removeConnect(nodeId);
        
        return "/db.do?action=list";
	}
	
	private String update()
    {    	   
		DBVo vo = new DBVo();
    	vo.setId(getParaIntValue("id"));
    	vo.setUser(getParaValue("user"));
    	vo.setPassword(getParaValue("password"));        
        vo.setAlias(getParaValue("alias"));
        vo.setIpAddress(getParaValue("ip_address"));
        vo.setPort(getParaValue("port"));
        vo.setDbName(getParaValue("db_name"));
        vo.setCategory(getParaIntValue("category"));
        vo.setDbuse(getParaValue("dbuse"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setSendemail(getParaValue("sendemail"));
        String allbid = "";
        String sid=getParaValue("sid");
        String[] businessids = getParaArrayValue("checkbox");
        if(businessids != null && businessids.length>0){
        	for(int i=0;i<businessids.length;i++){
        		
        		String bid = businessids[i];
        		allbid= allbid+bid+",";
        	}
        } 
        vo.setBid(allbid);
        vo.setManaged(getParaIntValue("managed"));
        vo.setDbtype(getParaIntValue("dbtype"));
        OracleEntity oracle=new OracleEntity();
        oracle.setAlias(vo.getAlias());
        oracle.setCollectType(vo.getCollecttype());
        oracle.setDbid(vo.getId());
        oracle.setGzerid(vo.getSendemail());
        oracle.setId(Integer.parseInt(sid));
        oracle.setManaged(vo.getManaged());
        oracle.setPassword(vo.getPassword());
        oracle.setSid(vo.getDbName());
        oracle.setUser(vo.getUser());
        oracle.setBid(vo.getBid());
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        /*--------- modify  by zhao  --------------------*/
        /*DBDao dao = new DBDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}*/
        OraclePartsDao oraDao=new OraclePartsDao();
        try{
        	 oraDao.update(oracle);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	oraDao.close();
        }
       
		/*------------modify end-------------*/
        return "/db.do?action=list";
    }
	
	private String cancelmanage()
    {    	   
		OracleEntity vo = new OracleEntity();
		//DBDao dao = new DBDao();
		OraclePartsDao dao=new OraclePartsDao();
		int sid=getParaIntValue("sid");
		try{
			vo = (OracleEntity)dao.getOracleById(sid);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		vo.setManaged(0);
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        dao = new OraclePartsDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }
	
	private String oracleping()
    {
		DBVo vo = new DBVo();
		
		Hashtable sysValue = new Hashtable();
		Hashtable memPerfValue = new Hashtable();
		Hashtable maxhash = new Hashtable();
		
		Vector contrFile_v = new Vector();
		Vector logFile_v = new Vector();
		Vector keepObj_v = new Vector();
		Vector extent_v = new Vector();
		Hashtable isArchive_h = new Hashtable();
		Hashtable cursors = new Hashtable();
		Hashtable memValue = new Hashtable();
		Vector tableinfo = new Vector();
		
		String lstrnStatu = "";
		
		String pingconavg ="0";
		try{
			DBDao dao = new DBDao();
			try{
				String id=getParaValue("id");
				request.setAttribute("id", id);
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
			String sid=getParaValue("sid");
		//SysLogger.info("sid-----------------------------"+sid);
			request.setAttribute("dbtye", typevo.getDbdesc());
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
//			/*-----modify  zhao------------------------------------------------ */
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("memPerfValue")){
//					memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//				}
//				if(iporacledata.containsKey("contrFile_v")){
//					contrFile_v=(Vector)iporacledata.get("contrFile_v");
//				}
//				if(iporacledata.containsKey("keepObj_v")){
//					keepObj_v=(Vector)iporacledata.get("keepObj_v");
//				}
//				if(iporacledata.containsKey("extent_v")){
//					extent_v=(Vector)iporacledata.get("extent_v");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("logFile_v")){
//					logFile_v=(Vector)iporacledata.get("logFile_v");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//			}
			/*-------------modify end--------------------------------------------*/
		/*modify  ע�͵�   by zhao*/
//			if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
//				runstr = "��������";
//			}
//			try{
//				dao = new DBDao();
//				try{
//					sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//				}catch(Exception e){
//					e.printStackTrace();
//				}finally{
//					dao.close();
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
		/* modify  end */
			//2010-HONGLI
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
				logFile_v = dao.getOracle_nmsoralogfile(serverip);
				extent_v = dao.getOracle_nmsoraextent(serverip);
				keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
				memValue = dao.getOracle_nmsoramemvalue(serverip);
				tableinfo = dao.getOracle_nmsoraspaces(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
//			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
//			imgurlhash.put("ConnectUtilization","resource\\image\\jfreechart\\"+newip+"ConnectUtilization"+".png");    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			request.setAttribute("sid", sid);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sysvalue",sysValue);
			request.setAttribute("memPerfValue",memPerfValue);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h",isArchive_h);
			request.setAttribute("contrFile_v",contrFile_v);
			request.setAttribute("logFile_v",logFile_v);
			request.setAttribute("extent_v",extent_v);
			request.setAttribute("keepObj_v",keepObj_v);
			request.setAttribute("avgpingcon",avgpingcon);
			//SysLogger.info(cursors.size()+"----------------------");
			request.setAttribute("cursors",cursors);
			request.setAttribute("memValue",memValue);
			request.setAttribute("tableinfo",tableinfo);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/application/db/oracleping.jsp";
    }
	
	private String oraclespace()
    {    	   
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Hashtable dbio = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String sid=this.getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id",id);
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
			
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*  modify  zhao ----------------------------*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("tableinfo_v")){
//					tableinfo_v=(Vector)iporacledata.get("tableinfo_v");
//				}
//				if(iporacledata.containsKey("dbio")){
//					dbio=(Hashtable)iporacledata.get("dbio");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
				dbio = dao.getOracle_nmsoradbio(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("cursors", cursors);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
//			if(iporacledata.containsKey("sysValue")){
//				sysValue=(Hashtable)iporacledata.get("sysValue");
//			}
			request.setAttribute("sysvalue", sysValue);
			/*---------------modify  end ------------------*/
			/*---modify  zhao ע�͵�*/
			/*dao = new DBDao();
			try{
			if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
				runstr = "��������";
			}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*dao = new DBDao();
			try{
				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*---------------modify end ------------------------*/
			request.setAttribute("runstr", runstr);
			
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			/*----------modify  zhao-------------*/
		/*	dao = new DBDao();
			try{
				tableinfo_v = dao.getOracleTableinfo(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}  */
			/*-----------modify  end---------------*/
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			

		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("dbio",dbio);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclespace.jsp";
    }
	private String oracletopsql()
    {    	   
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector sql_v = new Vector();
		Vector sql_readwrite_v = new Vector();
		Vector sql_sort_v = new Vector();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null;
		String sid=this.getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*  modify  zhao ----------------------------*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("sql_v")){
//					sql_v=(Vector)iporacledata.get("sql_v");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				sql_v = dao.getOracle_nmsoratopsql(serverip);
				sql_readwrite_v = dao.getOracle_nmsoratopsql_readwrite(serverip);
				sql_sort_v = dao.getOracle_nmsoratopsql_sort(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
//			if(iporacledata.containsKey("sysValue")){
//				sysValue=(Hashtable)iporacledata.get("sysValue");
//			}
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("sysvalue", sysValue);
			/*dao = new DBDao();
			try{
				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*---------------modify end ------------------------*/
			request.setAttribute("runstr", runstr);
			
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("sql_v",sql_v);
		request.setAttribute("sql_readwrite_v",sql_readwrite_v);
		request.setAttribute("sql_sort_v",sql_sort_v);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oracletopsql.jsp";
    }
	
	private String oraclesession()
    {    	   
		Hashtable cursors = new Hashtable();
		String lstrnStatu = "";
		DBVo vo = new DBVo();
		Hashtable isArchive_h = new Hashtable();
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector sessioninfo_v = new Vector();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String sid=getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/* ----------modify zhao -------------------*/
		/*	dao = new DBDao();
			try{
				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//					
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("info_v")){
//					sessioninfo_v = (Vector)iporacledata.get("info_v");
//				}
//				if(iporacledata.containsKey("cursors"))
//					cursors=(Hashtable)iporacledata.get("cursors");
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			/*------------modify  end ----------------------*/
			request.setAttribute("runstr", runstr);
			
			
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress())){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
//					
//				}
//				
//			}
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

//			dao = new DBDao();
//			try{
//				sessioninfo_v = dao.getOracleSession(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}   
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			

		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("sessioninfo_v",sessioninfo_v);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclesession.jsp";
    }
	private String oracletable(){//yangjun
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable cursors = new Hashtable();
		DBVo vo = new DBVo();
		Hashtable sysValue = new Hashtable();
//		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector table_v = new Vector();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null;
		String sid=getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("table_v")){
//					table_v=(Vector)iporacledata.get("table_v");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				table_v = dao.getOracle_nmsoratables(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			request.setAttribute("runstr", runstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);     
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
//		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("table_v",table_v);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oracletable.jsp";
		
	}
	private String oraclerollback()
    {    	   
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector rollbackinfo_v = new Vector();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String sid=getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*-----------modify by zhao ע�͵���δ���*/
			//dao = new DBDao();
			/*try{
				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//					if(iporacledata.containsKey("lstrnStatu")){
//						lstrnStatu=(String)iporacledata.get("lstrnStatu");
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("rollbackinfo_v")){
//					rollbackinfo_v = (Vector)iporacledata.get("rollbackinfo_v");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//				
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);//?
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
				rollbackinfo_v = dao.getOracle_nmsorarollback(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			/*-------------------modify end ------------*/
			request.setAttribute("runstr", runstr);
			
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress())){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
//					
//				}
//				
//			}
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

//			dao = new DBDao();
//			try{
//				rollbackinfo_v = dao.getOracleRollbackinfo(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);     
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("rollbackinfo_v",rollbackinfo_v);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclerollback.jsp";
    }
	
	private String oraclelock()
    {    	   
		Hashtable cursors = new Hashtable();
		String lstrnStatu = "";
		DBVo vo = new DBVo();
		Hashtable isArchive_h = new Hashtable();
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector lockinfo_v = new Vector();
		OracleLockInfo oracleLockInfo = null;
		String pingconavg ="0";
		double avgpingcon =0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String id = "";
		String sid=getParaValue("sid");
		try{
			DBDao dao = new DBDao();
			try{
				id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//				
//				}
//				request.setAttribute("cursors", cursors);
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("lockinfo_v")){
//					lockinfo_v = (Vector)iporacledata.get("lockinfo_v");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				lockinfo_v = dao.getOracle_nmsoralock(serverip);
				oracleLockInfo = dao.getOracle_nmsoralockinfo(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip); 
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip); 
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("oracleLockInfo", oracleLockInfo);
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("lstrnStatu", lstrnStatu);
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			/*Hashtable alloracledata = ShareData.getAlloracledata();
			Hashtable iporacledata = new Hashtable();
			
			if(alloracledata != null && alloracledata.size()>0){
				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
					if(iporacledata.containsKey("status")){
						String sta=(String)iporacledata.get("status");
						if("1".equals(sta)){
							runstr = "��������";
						}
					}
				
				}
				if(iporacledata.containsKey("sysValue")){
					sysValue = (Hashtable)iporacledata.get("sysValue");
				}
				if(iporacledata.containsKey("lockinfo_v")){
					lockinfo_v = (Vector)iporacledata.get("lockinfo_v");
				}
			}*/
			/*------  modify  zhao ----------------------ע�͵���*/
			/*dao = new DBDao();
			try{
				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			
			request.setAttribute("runstr", runstr);
			
			/*Hashtable alloracledata = ShareData.getAlloracledata();
			Hashtable iporacledata = new Hashtable();*/
		/*	if(alloracledata != null && alloracledata.size()>0){
				if(alloracledata.containsKey(vo.getIpAddress())){
					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
					
				}
				
			}*/
			/*----------------modify  end*/
//			dao = new DBDao();
//			try{
//				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

//			dao = new DBDao();
//			try{
//				lockinfo_v = dao.getOracleLockinfo(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			

		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("id", id);
		request.setAttribute("sid", sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("lockinfo_v",lockinfo_v);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclelock.jsp";
    }
	
	private String oraclemem()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable memValue = new Hashtable();
		//Hashtable sysValue = new Hashtable();
		String pingconavg ="0";
		String sid=getParaValue("sid");
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(getParaValue("id"));
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/* --------------modify  zhao--------------------��δ���ע�͵���*/
			/*dao = new DBDao();
			try{
				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("memValue")){
//					memValue = (Hashtable)iporacledata.get("memValue");
//				}
//				
//			}
			//2010-HONGLI
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String statusStr = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				sysValue = dao.getOracle_nmsorasys(serverip);
				statusStr = String.valueOf(statusHashtable.get("status"));
				memValue = dao.getOracle_nmsoramemvalue(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(statusStr)){
				runstr = "��������";
			}
			/*---------modify  end -------------------*/
			request.setAttribute("runstr", runstr);
			/*Hashtable alloracledata = ShareData.getAlloracledata();
			Hashtable iporacledata = new Hashtable();
			if(alloracledata != null && alloracledata.size()>0){
				if(alloracledata.containsKey(vo.getIpAddress())){
					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
					
				}
				
			}*/
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

//			dao = new DBDao();
//			try{
//				memValue = dao.getOracleMem(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			//sessioninfo_v = oraclemonManager.getOracleSession(serverip,serverport,servername,username,userpw);
			//rollbackinfo_v = oraclemonManager.getOracleRollbackinfo(serverip,serverport,servername,username,userpw);    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			

		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("oramem",memValue);
		request.setAttribute("sysvalue",sysValue);
		return "/application/db/oraclemem.jsp";
    }
//HONGLI START#####
	// jhl add  �¼�չ��
	private String oraEventReport(){
		String ip = request.getParameter("ipaddress");
		String id = request.getParameter("id");
		//HONGLI ADD
		request.setAttribute("id", id);
		
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
			request.setAttribute("startdate",startdate);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
			request.setAttribute("todate",todate);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		//DBCapReportManager dbcrm = new DBCapReportManager();
		Sdbevent(startdate,todate,id);
		return "/capreport/db/showOraEventReport.jsp";
	}
	
	
	private void Sdbevent(String str1,String str2,String id){
		String startdate = str1;
		String todate = str2;
		String ids = id;	
		String ip = null;
		String tyname = null;
		int pingvalue = 0;
		String dbname = null;
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		//�������־ȡ���˿����¼�¼���б�
		DBTypeDao typedao = new DBTypeDao();
		DBDao dao = new DBDao();
		DBVo vo = null;
		DBTypeVo typevo = null;
		try{
			vo = (DBVo)dao.findByID(ids);
			typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			tyname = typevo.getDbtype();
			ip = vo.getIpAddress();
			dbname = vo.getDbName();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
			typedao.close();
		}
		EventListDao eventdao = new EventListDao();
        //�õ��¼��б�
		StringBuffer s = new StringBuffer();
		s.append("select * from system_eventlist where recordtime>= '"+starttime+"' " + "and recordtime<='"+totime+"' ");
		s.append(" and nodeid="+vo.getId());
		List infolist = eventdao.findByCriteria(s.toString());
		if (infolist != null && infolist.size()>0){
        	for(int j=0;j<infolist.size();j++){
        		EventList eventlist = (EventList)infolist.get(j);
        		if(eventlist.getContent()==null)eventlist.setContent("");
        			String content = eventlist.getContent();
        			if(content.indexOf("���ݿ����ֹͣ")>0){
        				pingvalue = pingvalue + 1;
        			}
        	}
		}
		session.setAttribute("_tyname", tyname);  
		session.setAttribute("_ip", ip);  
		session.setAttribute("_dbname", dbname);  
		session.setAttribute("_pingvalue", pingvalue);  
	}
	private String oracleevent()
    {    	   
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		String lstrnStatu = "";
		Hashtable isArchive_h = new Hashtable();
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable memValue = new Hashtable();
		//Hashtable sysValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon =0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String time = null;
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String sid=getParaValue("sid");
		int voId=0;
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			voId=vo.getId();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				SysLogger.info(vo.getId()+"============"+sid);
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*-----modify  by zhao ��δ���ע�͵���--------------*/
			/*dao = new DBDao();
			try{
				if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("memValue")){
//					memValue = (Hashtable)iporacledata.get("memValue");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String statusStr = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				statusStr = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
//			waitv = dao.getOracle_nmsorawait(serverip);
//			userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(statusStr)){
				runstr = "��������";
			}
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("cursors", cursors);
			/*Hashtable alloracledata = ShareData.getAlloracledata();
			Hashtable iporacledata = new Hashtable();
			if(alloracledata != null && alloracledata.size()>0){
				if(alloracledata.containsKey(vo.getIpAddress())){
					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
					if(iporacledata.containsKey("sysValue")){
						sysValue = (Hashtable)iporacledata.get("sysValue");
					}
				}
				
			}*/
			/*-------------------------------------------*/
			
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			request.setAttribute("runstr", runstr);
		
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			b_time = getParaValue("startdate");//startdate
			t_time = getParaValue("todate");
	    	
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			if (b_time == null){
				b_time = sdf1.format(new Date());
			}
			if (t_time == null){
				t_time = sdf1.format(new Date());
			}
			String starttime1 = b_time + " 00:00:00";
			String totime1 = t_time + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			/* modify by zhao ---------���ע�͵�*/
			/*dao = new DBDao();
			try{
				memValue = dao.getOracleMem(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*-------------------modify end------------*/
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  
		    	//tmp = request.getParameter("id");
		    	status = getParaIntValue("status");
		    	level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
		    	
		    
				//String starttime1 = b_time + " 00:00:00";
				//String totime1 = t_time + " 23:59:59";
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",user.getBusinessids(),Integer.parseInt(sid));
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("oramem",memValue);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("list", list);
		request.setAttribute("startdate", b_time);
		request.setAttribute("todate", t_time);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oracleevent.jsp";
    }
	
	private String addmanage()
    {    	   
		OracleEntity vo = new OracleEntity();
		//DBDao dao = new DBDao();
		OraclePartsDao dao=new OraclePartsDao();
		int sid=getParaIntValue("sid");
		try{
			vo = (OracleEntity)dao.getOracleById(sid);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		vo.setManaged(1);
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        dao = new OraclePartsDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }
	
	private String oracleuser(){
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		DBVo vo = new DBVo();
		Hashtable sysValue = new Hashtable();
		Hashtable userinfo_h = new Hashtable();
		String pingconavg ="0";
		String chart1 = null;
		String sid=getParaValue("sid");
		double avgpingcon = 0;
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("userinfo_h")){
//					userinfo_h = (Hashtable)iporacledata.get("userinfo_h");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}

			//pingƽ��ֵ
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
		request.setAttribute("chart1",chart1);
		request.setAttribute("userinfo_h",userinfo_h);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oracleuser.jsp";
	}
	
	private String oraclewait(){
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		DBVo vo = new DBVo();
		Hashtable sysValue = new Hashtable();
		Vector waitv = new Vector();
		String pingconavg ="0";
		String chart1 = null;
		String sid=getParaValue("sid");
		double avgpingcon = 0;
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//						}
//					}
//				
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue = (Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("userinfo_h")){
//					waitv = (Vector)iporacledata.get("wait");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//			}
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				waitv = dao.getOracle_nmsorawait(serverip);
//			userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}

			//pingƽ��ֵ
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
		request.setAttribute("chart1",chart1);
		request.setAttribute("waitv",waitv);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclewait.jsp";
	}
	
	private String oraclebaseinfo(){
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		DBVo vo = new DBVo();
		Hashtable sysValue = new Hashtable();
		Hashtable baseinfoHash = new Hashtable();
		String pingconavg ="0";
		String chart1 = null;
		String sid=getParaValue("sid");
		double avgpingcon = 0;
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id", id);
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
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String status = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				status = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				baseinfoHash = dao.getOracle_nmsorabaseinfo(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e) { 
				e.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(status)){
				runstr = "��������";
			}
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
			request.setAttribute("cursors", cursors);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}

			//pingƽ��ֵ
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("sid", sid);
		request.setAttribute("chart1",chart1);
		request.setAttribute("baseinfoHash",baseinfoHash);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon);
		return "/application/db/oraclebaseinfo.jsp";
	}
	
	/**
	 * @author HONGLI MODIFY 2010-10-28
	 * oracle �ۺϱ���չ��
	 */	
	private String oracleCldReport(){
		Date d = new Date();
		String startdate = getParaValue("startdate");
		if (startdate == null) {
			startdate = sdf0.format(d);
			request.setAttribute("startdate",startdate);
		}
		String todate = getParaValue("todate");
		if (todate == null) {
			todate = sdf0.format(d);
			request.setAttribute("todate",todate);
		}
		String starttime = startdate + " 00:00:00";
		String totime = todate + " 23:59:59";
		Hashtable allcpuhash = new Hashtable();
		String ip = "";
		String ip2 ="";
		String dbname = "";
		
		DBVo vo = new DBVo();
		Hashtable cursors = new Hashtable();
		Hashtable isArchive_h = new Hashtable();
		String lstrnStatu = "";
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Hashtable dbio = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
//		String sid=this.getParaValue("sid");
		String sid = (String)session.getAttribute("sid");//HONGLI ADD 
		Hashtable memValue = new Hashtable();//HONGLI ADD  ���ݿ���ڴ�������Ϣ
		String pingnow = "0.0";//HONGLI ADD ��ǰ��ͨ��
		String pingmin = "0.0";//HONGLI ADD ��С��ͨ��
		List eventList = new ArrayList();//�¼��б�
		try{
			DBDao dao = new DBDao();
			try{
				String id = getParaValue("id");
				request.setAttribute("id",id);
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
			
			
			OraclePartsDao oracledao=new OraclePartsDao();
			try{
				OracleEntity oracle=(OracleEntity)oracledao.getOracleById(Integer.parseInt(sid));
				vo.setDbName(oracle.getSid());
				vo.setCollecttype(oracle.getCollectType());
				vo.setManaged(oracle.getManaged());
				vo.setBid(oracle.getBid());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				oracledao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "����ֹͣ";
			/*  modify  zhao ----------------------------*/
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			Hashtable iporacledata = new Hashtable();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress()+":"+sid)){
//					iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress()+":"+sid);
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							runstr = "��������";
//							pingnow = "100.0";//HONGLI ADD
//						}
//					}
//					if(iporacledata.containsKey("cursors")){
//						cursors=(Hashtable)iporacledata.get("cursors");
//					}
//				}
//				if(iporacledata.containsKey("sysValue")){
//					sysValue=(Hashtable)iporacledata.get("sysValue");
//				}
//				if(iporacledata.containsKey("tableinfo_v")){
//					tableinfo_v=(Vector)iporacledata.get("tableinfo_v");
//				}
//				if(iporacledata.containsKey("dbio")){
//					dbio=(Hashtable)iporacledata.get("dbio");
//				}
//				if(iporacledata.containsKey("cursors")){
//					cursors=(Hashtable)iporacledata.get("cursors");
//				}
//				if(iporacledata.containsKey("lstrnStatu")){
//					lstrnStatu=(String)iporacledata.get("lstrnStatu");
//				}
//				if(iporacledata.containsKey("isArchive_h")){
//					isArchive_h=(Hashtable)iporacledata.get("isArchive_h");
//				}
//				//HONGLI ADD START1
//				if(iporacledata.containsKey("memValue")){
//					memValue = (Hashtable)iporacledata.get("memValue");
//				}
//				//HONGLI ADD END1
//			}
			
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
			String statusStr = "0";
			try {
				dao = new DBDao();
				Hashtable statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				sysValue = dao.getOracle_nmsorasys(serverip);
				statusStr = String.valueOf(statusHashtable.get("status"));
				lstrnStatu = String.valueOf(statusHashtable.get("lstrnstatu"));
				isArchive_h = dao.getOracle_nmsoraisarchive(serverip);
				memValue = dao.getOracle_nmsoramemvalue(serverip);
				dbio = dao.getOracle_nmsoradbio(serverip);
				tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
//			waitv = dao.getOracle_nmsorawait(serverip);
//			userinfo_h = dao.getOracle_nmsorauserinfo(serverip);
//			sessioninfo_v = dao.getOracle_nmsorasessiondata(serverip);
//			lockinfo_v = dao.getOracle_nmsoralock(serverip);
//			table_v = dao.getOracle_nmsoratables(serverip);
//			contrFile_v = dao.getOracle_nmsoracontrfile(serverip);
//			logFile_v = dao.getOracle_nmsoralogfile(serverip);
//			extent_v = dao.getOracle_nmsoraextent(serverip);
//			keepObj_v = dao.getOracle_nmsorakeepobj(serverip);
				cursors = dao.getOracle_nmsoracursors(serverip);
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally{
				dao.close();
			}
			if("1".equals(statusStr)){
				runstr = "��������";
				pingnow = "100.0";//HONGLI ADD
			}
			//HONGLI ADD START2
			//ȥ����λMB\KB
			String[] sysItem={"shared_pool","large_pool","buffer_cache","java_pool","aggregate_PGA_target_parameter","total_PGA_allocated","maximum_PGA_allocated"};
			DecimalFormat df=new DecimalFormat("#.##");
			if(memValue!=null){
                for(int i=0; i<sysItem.length; i++){
                    String value = "";
                    if(memValue.get(sysItem[i])!=null){
                       value = (String)memValue.get(sysItem[i]);
                    }
                    if(!value.equals("")){
                    	if(value.indexOf("MB")!=-1){
                    		value = value.replace("MB", "");
                    	}
                    	if(value.indexOf("KB")!=-1){
                    		value = value.replace("KB", "");
                    	}
                    }else{
                    	value = "0";
                    }
                    memValue.put(sysItem[i], df.format(Double.parseDouble(value)));
                }
			}
			request.setAttribute("memValue", memValue);
			//HONGLI ADD END2
			request.setAttribute("cursors", cursors);
			request.setAttribute("lstrnStatu", lstrnStatu);
			request.setAttribute("isArchive_h", isArchive_h);
//			Hashtable memPerfValue = new Hashtable();
//			if(iporacledata.containsKey("memPerfValue"))
//			{
//				memPerfValue=(Hashtable)iporacledata.get("memPerfValue");
//			}
			request.setAttribute("memPerfValue", memPerfValue);
//			if(iporacledata.containsKey("sysValue")){
//				sysValue=(Hashtable)iporacledata.get("sysValue");
//			}
			request.setAttribute("sysvalue", sysValue);
			/*---------------modify  end ------------------*/
			/*---modify  zhao ע�͵�*/
			/*dao = new DBDao();
			try{
			if(dao.getOracleIsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword())){
				runstr = "��������";
			}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*dao = new DBDao();
			try{
				sysValue = dao.getOracleSys(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}*/
			/*---------------modify end ------------------------*/
			request.setAttribute("runstr", runstr);
			
		
			/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";*/
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime,totime);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null){
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				pingmin = (String)ConnectUtilizationhash.get("pingmax");
			}
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
				pingmin = pingmin.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);
			/*----------modify  zhao-------------*/
		/*	dao = new DBDao();
			try{
				tableinfo_v = dao.getOracleTableinfo(vo.getIpAddress(),Integer.parseInt(vo.getPort()),vo.getDbName(),vo.getUser(),vo.getPassword());
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}  */
			/*-----------modify  end---------------*/
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
    		//��oracle�澯����
    		String downnum = "0";
    		Hashtable pinghash = new Hashtable();
    		try {
    			pinghash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,
    					"ORAPing", "ConnectUtilization", starttime,
    					totime);
    			if (pinghash.get("downnum") != null)
    				downnum = (String) pinghash.get("downnum");
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		//========end downnum
    		//��ռ�==========�澯
    		DBTypeDao dbTypeDao = new DBTypeDao();
    		int count = 0;
    		try {
    			count = dbTypeDao.finddbcountbyip(ip);
    			request.setAttribute("count", count);
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			dbTypeDao.close();
    		}
    		//���ݿ����еȼ�=====================
    		String grade = "��";
    		if (count>0) {
    			grade = "��";
			}
    		
    		if (!"0".equals(downnum)) {
    			grade = "��";
			}
    		request.setAttribute("downnum", downnum);
    		request.setAttribute("grade", grade);
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
					eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),Integer.parseInt(sid));
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
		String newip=SysUtil.doip(vo.getIpAddress());//HONGLI ADD
		request.setAttribute("list", eventList);
		request.setAttribute("newip", newip);//HONGLI ADD
		request.setAttribute("ipaddresid", vo.getIpAddress()+":"+sid);//HONGLI ADD
		request.setAttribute("sid",sid);
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("dbio",dbio);
		request.setAttribute("sysvalue",sysValue);
		request.setAttribute("avgpingcon",avgpingcon+"");
		request.setAttribute("pingmin", pingmin);//HONGLI ADD ��С��ͨ��
		request.setAttribute("pingnow", pingnow);//HONGLI ADD ��ǰ��ͨ��
		
		return "/capreport/db/showDbOracleCldReport.jsp";
	}
	
	public String execute(String action) 
	{	
        if(action.equals("list"))
            return list();    
        if(action.equals("ready_add"))
        	return "/application/db/add.jsp";
        if(action.equals("add"))
        	return add();
        if(action.equals("delete"))
            return delete();
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new DBDao();
    	    setTarget("/application/db/edit.jsp");
            return readyEdit(dao);
        }
        if(action.equals("update"))
            return update();
        if(action.equals("cancelmanage"))
            return cancelmanage();
        if(action.equals("addmanage"))
            return addmanage();
        if(action.equals("oracleping"))
            return oracleping();
        if(action.equals("oraclespace"))
            return oraclespace();
        if(action.equals("oracletopsql"))
            return oracletopsql();
        if(action.equals("oraclesession"))
            return oraclesession();
        if(action.equals("oraclerollback"))
            return oraclerollback();
        if(action.equals("oracletable"))
            return oracletable();
        if(action.equals("oraclelock"))
            return oraclelock();
        if(action.equals("oraclemem"))
            return oraclemem();
        if(action.equals("oracleevent"))
            return oracleevent();
        if(action.equals("oracleuser"))
            return oracleuser();
        if(action.equals("oraclewait"))
            return oraclewait();
        if(action.equals("isOracleOK"))
        	return isOracleOK();
        if(action.equals("sychronizeData"))
        	return sychronizeData();
//HONGLI START#####
     // jhl add
        if(action.equals("dboraReportdownusable")) 
        	return dboraReportdownusable();
        if(action.equals("oracleCldReport"))
        	return oracleCldReport();
        if(action.equals("dbReport")) 
        	return dbReport();
        if(action.equals("dboraReportdown"))
        	return dboraReportdown();
//        if(action.equals("createOraMultipleReport"))
//        	return createOraMultipleReport();
        if(action.equals("oraEventReport"))
        	return oraEventReport();
        //jhl end
        if(action.equals("oracleManagerEventReport")){
        	return oracleManagerEventReport();
        }
        if(action.equals("oracleManagerEventReportQuery")){
        	return oracleManagerEventReportQuery();
        }
        if(action.equals("oraclebaseinfo")){
        	return oraclebaseinfo();
        }
//HONGLI END#####
        if(action.equals("dboraReportdownusableload"))
        	return dboraReportdownusableload();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	private String sychronizeData()
	{
		DBRefreshHelper dbRefreshHelper = new DBRefreshHelper();
		
		String dbvoId = request.getParameter("id");
		String dbPage = request.getParameter("dbPage");
		DBDao dbDao = new DBDao();
		DBVo dbVo = null;
        try {
            dbVo = (DBVo)dbDao.findByID(dbvoId);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dbDao.close();
        }
		dbRefreshHelper.execute(dbVo);
		if(dbPage.equals("oracleping"))
		{
			return oracleping();
		}
		if(dbPage.equals("oraclelock"))
		{
			return oraclelock();
		}
		if(dbPage.equals("oraclesession"))
		{
			return oraclesession();
		}
		if(dbPage.equals("oraclerollback"))
		{
			return oraclerollback();
		}
		if(dbPage.equals("oracletable"))
		{
			return oracletable();
		}
		if(dbPage.equals("oraclespace"))
		{
			return oraclespace();
		}
		if(dbPage.equals("oracletopsql"))
		{
			return oracletopsql();
		}
		if(dbPage.equals("oracleuser"))
		{
			return oracleuser();
		}
		if(dbPage.equals("oraclewait"))
		{
			return oraclewait();
		}
		if(dbPage.equals("oracleevent"))
		{
			return oracleevent();
		}
		if(dbPage.equals("oraclebaseinfo")){
            return oraclebaseinfo();
        }
		return "/application/db/oracleping";
	}
	private String isOracleOK()
	{
		DBDao dbdao = null;
		OraclePartsDao oraclePartsDao = null;
		String id = request.getParameter("id");
		String myip = request.getParameter("myip");
		String myport = request.getParameter("myport");
		int port = Integer.parseInt(myport);
		String myUser = request.getParameter("myUser");
		String myPassword = request.getParameter("myPassword");
		String sid = request.getParameter("sid");
		
		boolean isOK = false;
		try {
			dbdao = new DBDao();
			
			if(sid != null && sid.trim().length()>0){
				//ORACLE���ݿ�
				String realSid = "";
				try{
					oraclePartsDao = new OraclePartsDao();
					OracleEntity oracle = (OracleEntity)oraclePartsDao.getOracleById(Integer.parseInt(sid));
					isOK = dbdao.getOracleIsOK(myip, port, oracle.getSid(), myUser, myPassword);
					realSid = oracle.getSid();
				}catch(Exception e){
					
				}finally{
					oraclePartsDao.close();
				}
				request.setAttribute("dbtype", "Oracle");
				request.setAttribute("realSid", realSid);
			}else{
				//�������ݿ�
				DBVo dbvo = (DBVo)dbdao.findByID(id);
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try {
					typevo = (DBTypeVo) typedao.findByID(dbvo.getDbtype() + "");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					typedao.close();
				}
				
				String ip = dbvo.getIpAddress();
				String dbName = dbvo.getDbName();
				
				if (typevo.getDbtype().equalsIgnoreCase("mysql")) {
					isOK = dbdao.getMySqlIsOk(ip, myUser, myPassword, dbvo.getDbName());
				}else if (typevo.getDbtype().equalsIgnoreCase("sqlserver")) {
					isOK = dbdao.getSqlserverIsOk(ip, myUser, myPassword);
				}else if (typevo.getDbtype().equalsIgnoreCase("sybase")) {
					isOK = dbdao.getSysbaseIsOk(ip, myUser, myPassword, port);
				}else if (typevo.getDbtype().equalsIgnoreCase("informix")) {
					isOK = dbdao.getInformixIsOk(ip, port+"", myUser, myPassword, dbvo.getDbName(), dbvo.getAlias());
				}else if (typevo.getDbtype().equalsIgnoreCase("db2")) {
					isOK = dbdao.getDB2IsOK(ip, port, dbvo.getDbName(), myUser, myPassword);
				}
				request.setAttribute("dbtype", typevo.getDbtype());
				
			}
			

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbdao != null)
				dbdao.close();
		}
		request.setAttribute("oracleIsOK", isOK);
		return "/tool/oracleisok.jsp";
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
	    
	    /**
		 *@author HONGLI 
		 * date 2010-11-17
		 * �¼�����   
		 * @return
		 */
		public String oracleManagerEventReport(){
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
			String sid= (String)session.getAttribute("sid");
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
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress()+":"+sid,"ORAPing","ConnectUtilization",starttime,totime);
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
						eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),Integer.parseInt(sid));
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
			return "/capreport/db/showOraEventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-17
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String oracleManagerEventReportQuery(){
			return oracleManagerEventReport();
		}
		/**
		 * ����ip��ַ�Ͷ˿ڵõ�oracle��sysvalue��Ϣ
		 * @param ipAddress
		 * @param port
		 * @return
		 * @author makewen
		 * @date   Apr 13, 2011
		 */
		public  Hashtable geHashtable(String ipAddress,String sid){ 
			Hashtable sysValue = new Hashtable(); 
			DBDao dao = new DBDao(); 
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(ipAddress);
			String serverip = hex+":"+sid; 
			try { 
				sysValue = dao.getOracle_nmsorasys(serverip); 
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
			return sysValue;
		}
}