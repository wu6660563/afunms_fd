/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.dao.NetworkDao;
import com.afunms.common.util.Arith;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.util.PanelXmlOperator;
import com.afunms.topology.util.XmlOperator;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.model.IpaddressPanel;
import com.ibm.db2.jcc.c.i;

/**
 * @author hukelei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdateXmlTaskTest extends MonitorTask {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private static UpdateXmlTaskTest updateXmlTaskTest;
	private ManageXml xml;
	private XmlOperator xmlOpr;
	
	public ManageXml getManageXml() {
		return xml;
	}

	public void setManageXml(ManageXml xml) {
		this.xml = xml;
	}
	
	public XmlOperator getXmlOperator() {
		return xmlOpr;
	}

	public void setXmlOperator(XmlOperator xmlOpr) {
		this.xmlOpr = xmlOpr;
	}
	
	public static synchronized UpdateXmlTaskTest getInstance(){
		if(updateXmlTaskTest == null){
			updateXmlTaskTest = new UpdateXmlTaskTest();
		}
		return updateXmlTaskTest;
	}
	
	
	public void run() {
		if(xml == null || xmlOpr == null){//�ýڵ㲻����,nodeidδ����ʼ��
			SysLogger.info("��xml������,xmlδ����ʼ��");
			return;
		}
    	//��ʼ��������
		SysLogger.info("��ʼ����xml ......"+xml.getId());
		try {
			try {
				xmlOpr.setFile(xml.getXmlName());
				xmlOpr.init4updateXml();
				xmlOpr.updateInfo(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(xml.getTopoType()==1){//ҵ����ͼ����Ҫ����ҵ����ͼ��flex-xml�ļ�
//				SysLogger.info("--��ʼ����ҵ����ͼ��flex-xml�ļ�--");
//				NodeDependDao nodeDependDao = new NodeDependDao();
//				try{
//				    List list = nodeDependDao.findByXml(xml.getXmlName());
//				    ChartXml chartxml = null;
//					chartxml = new ChartXml("NetworkMonitor","/"+xml.getXmlName().replace("jsp", "xml"));
//					chartxml.addBussinessXML(xml.getTopoName(),list);
//					chartxml = null;
//					ChartXml chartxmlList = null;
//					chartxmlList = new ChartXml("NetworkMonitor","/"+xml.getXmlName().replace("jsp", "xml").replace("businessmap", "list"));
//					chartxmlList.addListXML(xml.getTopoName(),list);
//					chartxmlList = null;
//					list = null;
//				}catch(Exception e){
//				    e.printStackTrace();   	
//				}finally{
//					nodeDependDao.close();
//                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
	       	int xmlCollectedSize = -1;
        	if(xml != null){
        		xmlCollectedSize = ShareData.addXmlCollectedSize();
        	}
        	int needCollectNodesSize = ShareData.getXmlTimerMap().keySet().size();
//        	System.out.println("####xml.getXmlName():"+xml.getXmlName()+"####needCollectNodesSize:"+needCollectNodesSize+"====ShareData.xmlCollectedSize():"+xmlCollectedSize);
        	//�ж�����Task�Ƿ��������
			if(needCollectNodesSize == xmlCollectedSize){//��Ҫ�ɼ����豸���� �� �Ѳɼ����豸������ȣ��򱣴�
	    		ShareData.setXmlCollectedSize(0);
	    		SysLogger.info("##############################");
	    		SysLogger.info("### ����XML�������  ####");
	    		SysLogger.info("##############################");
	    		xmlOpr.alarmNode(xmlOpr.getAlarmMapList());
	    		
	    		//collectData(PollingEngine.getInstance().getNodeList());

	    		//�ж�XML�����Ƿ��Ѿ�������ʱ����δ����������
	    		ManageXml _xml = null;
	    		Hashtable dohash = new Hashtable();
	    		TaskUtil taskutil = new TaskUtil();
	    		for(int i=0;i<PollingEngine.getInstance().getXmlList().size();i++){
	    			_xml = (ManageXml)PollingEngine.getInstance().getXmlList().get(i);
	    			dohash.put(_xml.getId()+"", _xml.getId());
	    			if(!ShareData.getXmlTimerMap().containsKey(_xml.getId()+"")){
	    				//����
	    				
	    				Hashtable taskhash = taskutil.getInterval("updatexmltask");
	    				if(taskhash != null && taskhash.size()>0){
	    					taskutil.addXmlTask(xml,xmlOpr,taskhash);
	    				}
	    				
	    			}
	    		} 
	    		taskutil = null;
	    		
	    		//�ж��豸�����Ƿ�ɾ������ɾ������ȡ����ʱ
	    		Iterator iterator = ShareData.getXmlTimerMap().keySet().iterator();
	    		while (iterator.hasNext()) {
	    			 String xmlid = String.valueOf(iterator.next());
	    			 if(!dohash.containsKey(xmlid)){
	    				 //ȡ��
	    				 ShareData.getXmlTimerMap().get(xmlid).cancel();
	    				 ShareData.getXmlTimerMap().remove(xmlid);
	    			 }
	    		}
				SysLogger.info("********UpdateXmlTaskTest Thread Count : "+Thread.activeCount());
			}
		}
        //������������
    
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
			networkDao.collectAllNetworkData(PollingEngine.getInstance().getNodeList());
			SysLogger.info("######�ɼ�������Ƿ���ģʽ �����ڴ�######");
		}
	}
	
	/**
	 * �����豸���ͼ
	 * @param ipaddress
	 */
	public void updatePanel(String ipaddress){
		IpaddressPanelDao paneldao = new IpaddressPanelDao();
		try {
			IpaddressPanel ippanel = (IpaddressPanel)paneldao.findByIpaddress(ipaddress);
//			String ipaddress = ippanel.getIpaddress();
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
		} finally{
			paneldao.close();
		}
	}
}
