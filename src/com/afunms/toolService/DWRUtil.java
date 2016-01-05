package com.afunms.toolService;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import com.afunms.toolService.traceroute.TraceRouteExecute;
import com.afunms.toolService.traceroute.TraceRouteManager;




public class DWRUtil {

	private TraceRouteExecute tre=new TraceRouteExecute();
	public boolean ifExecute(String kind,String ip){
		//��õ�ǰ��session
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
		HttpSession session = req.getSession();
		if(kind.equals("ping")){
			//SysLogger.info("�ж�PING�����Ƿ�ִ��!");
			if(pe.readResult(ip,session.getId())==null) {
				//SysLogger.info("����ִ��PING����!");
				return true;
			}
		}
		
		if(kind.equals("traceroute")){
			//SysLogger.info("�ж�TraceRoute�����Ƿ�ִ��!");
			//System.out.println("�ж�TraceRoute�����Ƿ�ִ��!");
			if(tre.readResult(ip,session.getId())==null) {
				//SysLogger.info("����ִ��TraceRoute����!");
				//System.out.println("����ִ��TraceRoute����!");
				return true;
			}
		}
		//SysLogger.info("������ִ������!");
		return false;
	}
	

	/**
	 * @param String ip ,int timeout,int maxttl
	 * @return ����String
	 * �˷���Ϊִ��TraceRoute����ӿ�
	 */
	private static StringBuilder tRRsStr = new StringBuilder();
	public void executeTRRsStr(String ip ,int timeout,int maxttl) {
		if(ifExecute("traceroute",ip)){
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
			HttpSession session = req.getSession();
			TraceRouteManager trm = new TraceRouteManager(ip,timeout,maxttl);
			trm.executeTraceRouteResult(session.getId());
		}
		else {
			//SysLogger.info("TraceRoute��������ִ�У��ܾ�TraceRoute����");
		}
	}

	/**
	 * ping����Ĳ���,n:ִ�д��� l:�����ȣ��ֽڣ�w:��ʱʱ�䣨���룩
	 * @return ����String
	 * �˷���Ϊִ��ping����ӿ�
	 * **/
	private static StringBuilder pingRsStr = new StringBuilder();
	public void executePingRsStr(String ip ,int n ,int l ,int w){
		if(ifExecute("ping",ip)){
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
			HttpSession session = req.getSession();
			PingManager pm = new PingManager(ip,n,l,w);
			pm.executePingResult(session.getId());
		}
		else {
			//SysLogger.info("PING��������ִ�У��ܾ�PING����");
		}
	}
	
	
	static PingExecute pe = new PingExecute();
	public String readPingRsStr(String ip){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
		HttpSession session = req.getSession();
		pingRsStr.delete(0, pingRsStr.length());
		List resultList = pe.readResult(ip,session.getId());
		if(resultList==null) return null;
		for(int i =0;i<resultList.size();i++){
			if(resultList.get(i).toString().trim().length() > 0)
				pingRsStr.append("\r\n"+resultList.get(i));
		}
		return pingRsStr.toString();
	}

	
	public String readTRRsStr(String ip){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
		HttpSession session = req.getSession();
		tRRsStr.delete(0, tRRsStr.length());
		List resultList = tre.readResult(ip,session.getId());
		if(resultList==null){
			//SysLogger.info("TraceRoute DWRUtil.readTRRsStr��ȡΪ�գ�����null");
			return null;
		}
		for(int i =0;i<resultList.size();i++){
			if(resultList.get(i).toString().trim().length() > 0)
				tRRsStr.append("\r\n"+resultList.get(i));
		}
		return tRRsStr.toString();
	}

	public void closePro(String kind,String ip){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest(); 
		HttpSession session = req.getSession();
		if(kind.trim().equals("traceroute")) this.tre.closeTracert(ip,session.getId());
		if(kind.trim().equals("ping")) this.pe.closePing(ip,session.getId());
	}
//	private Logger log = Logger.getLogger(DWRUtil.class);
}
