/*
 * Created on 2005-4-14
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.dao.NetworkDao;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.model.IpaddressPanel;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Task;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.util.PanelXmlOperator;
import com.afunms.topology.util.XmlOperator;



/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdateXmlTask extends MonitorTask {

	/**
	 * 
	 */
	public UpdateXmlTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		SysLogger.info("######��ʼ����XML######");
		
//		IpaddressPanelDao paneldao = new IpaddressPanelDao();
//		List list = null;
//		try{
//			list = paneldao.loadAll();
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			paneldao.close();
//		}
		
//		int numThreads = 200;
//		
//		try {
//			List numList = new ArrayList();
//			TaskXml taskxml = new TaskXml();
//			numList = taskxml.ListXml();
//			for (int i = 0; i < numList.size(); i++) {
//				Task task = new Task();
//				BeanUtils.copyProperties(task, numList.get(i));
//				if (task.getTaskname().equals("updatexmlthreadnum")){
//					numThreads = task.getPolltime().intValue();
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		//ִ����ѯ��������
//		if (list != null && list.size() > 0) {
//    		// �����̳߳�
//    		ThreadPool threadPool = new ThreadPool(list.size());														
//    		// ��������
//    		for (int i=0; i<list.size(); i++) {
//        			threadPool.runTask(createUpdatePanelTask((IpaddressPanel)list.get(i)));
//    		}
//    		// �ر��̳߳ز��ȴ������������
//    		threadPool.join(); 
//    		threadPool.close();
//    		threadPool = null;
//		}
		
		// �����ͼ�б�����ͼ����� yangjun add 2009-10-14
		ManageXmlDao subMapDao = new ManageXmlDao();
		List subfileList = null;
		try{
			subfileList = subMapDao.loadAll();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			subMapDao.close();
		}
		List nodelist = PollingEngine.getInstance().getNodeList();
		if(nodelist != null && nodelist.size()>0){ 
			collectData(nodelist);
		}
//		if(subfileList != null && subfileList.size()>0){
//			XmlOperator xmlOpr = new XmlOperator();
//			for (int j = 0; j < subfileList.size(); j++) {
//	    		// �����̳߳�
//		    		ThreadPool threadPool = new ThreadPool(nodelist.size());	
////		    		�Ȳɼ��ڵ�����CPU���ڴ��
//		    		// ��������
//		    		for (int i=0; i<nodelist.size(); i++) {
//		        	    threadPool.runTask(createPollTask((Host)nodelist.get(i)));
//		    		}
//	    		// �ر��̳߳ز��ȴ������������
//	    		threadPool.join(); 
//	    		threadPool.close();
//	    		threadPool = null;
//		    	createSubViewTask((ManageXml) subfileList.get(j),xmlOpr);
//			}
//			xmlOpr.alarmNode(xmlOpr.getAlarmMapList());
//		}
		
		//�������нڵ��״̬
		if(nodelist != null && nodelist.size()>0){   
            //�����̳߳�
    		ThreadPool threadPool = new ThreadPool(nodelist.size());	
    		// ��������
    		for (int i=0; i<nodelist.size(); i++) {
        	    threadPool.runTask(createPollRefreshStateTask((Host)nodelist.get(i)));
    		}
    		// �ر��̳߳ز��ȴ������������
    		threadPool.join(); 	
    		threadPool.close();
    		threadPool = null;
		}	
		
		// ��������ͼ��Ϣ
//		try {
//			XmlOperator xmlOpr = new XmlOperator();
//			xmlOpr.setFile("server.jsp");
//			xmlOpr.init4updateXml();
//			xmlOpr.updateInfo(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		SysLogger.info("######��������XML######");
	}
	
	/**
	 * <P>�ȸ��½ڵ�����(CPU���ڴ��)</p>
	 * HONGLI
	 * @param monitornodelist
	 */
	public void collectData(List nodeList){
		String runmodel = PollingEngine.getCollectwebflag(); 
		if("1".equals(runmodel)){
	       	//�ɼ�������Ƿ���ģʽ
			NetworkDao networkDao = new NetworkDao();
			networkDao.collectAllNetworkData(nodeList);
			SysLogger.info("######�ɼ�������Ƿ���ģʽ �����ڴ�######");
		}
	}
	
	
    /**
     *	������ѯ��������
     */	
	private Runnable createUpdatePanelTask(final IpaddressPanel ippanel) {
    return new Runnable() {
        public void run() {
			try {
				String ipaddress = ippanel.getIpaddress();
				Host host = (Host) PollingEngine.getInstance().getNodeByIP(ipaddress);
				if(host == null)return;
				//����ֻͨ��PING�ɼ����ݵ��豸
				if(host.getCollecttype()==SystemConstant.COLLECTTYPE_PING || host.getCollecttype()==SystemConstant.COLLECTTYPE_SSHCONNECT
						|| host.getCollecttype()==SystemConstant.COLLECTTYPE_TELNETCONNECT || "as400".equals(host.getSysOid()))return;
				String oid = host.getSysOid();
				oid = oid.replaceAll("\\.", "-");
				PanelXmlOperator panelxmlOpr = new PanelXmlOperator();
				String filename = SysUtil.doip(host.getIpAddress())+ ".jsp";
				panelxmlOpr.setFile(filename, 2);
				panelxmlOpr.setOid(oid);
				panelxmlOpr.setImageType(ippanel.getImageType());   // nielin add at 2010-01-12
				panelxmlOpr.setIpaddress(host.getIpAddress());
				// дXML
				panelxmlOpr.init4createXml();
				panelxmlOpr.createXml(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    };
	}
	
    /**
     *	������ѯ�ڵ������
     */	
	private Runnable createPollTask(final Host host) {
    return new Runnable() {
        public void run() {
			try{
				//Host host = (Host)nodelist.get(i);
				host.getAlarmMessage().clear();
				host.doPoll();
			}catch(Exception e){
				e.printStackTrace();
			}
        }
    };
	}
	
	/**
     *	������ѯ�ı�ڵ��״̬����
     */	
	private Runnable createPollRefreshStateTask(final Host host) {
    return new Runnable() {
        public void run() {
			try{
				host.refreshNodeState();
			}catch(Exception e){
				e.printStackTrace();
			}
        }
    };
	}
	
//    /**
//     *	������ѯ����ͼ������
//     */	
//	private Runnable createChildViewTask(final CustomXml xml) {
//    return new Runnable() {
//        public void run() {
//        	XmlOperator xmlOpr = new XmlOperator();
//			try {
//				xmlOpr.setFile(xml.getXmlName());
//				xmlOpr.init4updateXml();
//				xmlOpr.updateInfo(true);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//        }
//    };
//	}
	
    /**
     *	������ѯ����ͼ������
     */	
	private void createSubViewTask(ManageXml xml,XmlOperator xmlOpr) {
		try {
			xmlOpr.setFile(xml.getXmlName());
			xmlOpr.init4updateXml();
			xmlOpr.updateInfo(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(xml.getTopoType()==1){//ҵ����ͼ����Ҫ����ҵ����ͼ��flex-xml�ļ�
			SysLogger.info("--��ʼ����ҵ����ͼ��flex-xml�ļ�--");
			NodeDependDao nodeDependDao = new NodeDependDao();
			try{
			    List list = nodeDependDao.findByXml(xml.getXmlName());
			    ChartXml chartxml;
				chartxml = new ChartXml("NetworkMonitor","/"+xml.getXmlName().replace("jsp", "xml"));
				chartxml.addBussinessXML(xml.getTopoName(),list);
				ChartXml chartxmlList;
				chartxmlList = new ChartXml("NetworkMonitor","/"+xml.getXmlName().replace("jsp", "xml").replace("businessmap", "list"));
				chartxmlList.addListXML(xml.getTopoName(),list);
			}catch(Exception e){
			    e.printStackTrace();   	
			}finally{
				nodeDependDao.close();
            }
		}
	}

}
