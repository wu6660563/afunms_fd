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

import com.afunms.common.util.Arith;
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
import com.afunms.topology.model.Link;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.util.PanelXmlOperator;
import com.afunms.common.util.ShareData;
import com.afunms.config.model.IpaddressPanel;
import com.ibm.db2.jcc.c.i;

/**
 * @author hukelei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdatePanelTaskTest extends MonitorTask {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private IpaddressPanel ippanel;
	
	public IpaddressPanel getIpaddressPanel() {
		return ippanel;
	}

	public void setIpaddressPanel(IpaddressPanel ippanel) {
		this.ippanel = ippanel;
	}
	
	
	public void run() {
		if(ippanel == null){//�ýڵ㲻����,nodeidδ����ʼ��
			SysLogger.info("����岻����,linkidδ����ʼ��");
			return;
		}
    	//��ʼ��������
		try {
			SysLogger.info(ippanel.getIpaddress()+" �������ɼ� ������ ");
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
		}finally{
	       	int panelCollectedSize = -1;
        	if(ippanel != null){
        		panelCollectedSize = ShareData.addPanelCollectedSize();
        	}
        	int needCollectNodesSize = ShareData.getPanelTimerMap().keySet().size();
        	//System.out.println("####linkid:"+linkid+"####needCollectNodesSize:"+needCollectNodesSize+"====ShareData.getLinkCollectedSize():"+linkCollectedSize);
        	//�ж�����Task�Ƿ��������
			if(needCollectNodesSize == panelCollectedSize){//��Ҫ�ɼ����豸���� �� �Ѳɼ����豸������ȣ��򱣴�
	    		ShareData.setPanelCollectedSize(0);
	    		SysLogger.info("##############################");
	    		SysLogger.info("### �������������  ####");
	    		SysLogger.info("##############################");

	    		//�ж���·�Ƿ��Ѿ�������ʱ����δ����������
	    		IpaddressPanel ippanel = null;
	    		Hashtable dohash = new Hashtable();
	    		TaskUtil taskutil = new TaskUtil();
	    		for(int i=0;i<PollingEngine.getInstance().getPanelList().size();i++){
	    			ippanel = (IpaddressPanel)PollingEngine.getInstance().getPanelList().get(i);
	    			dohash.put(ippanel.getId()+"", ippanel.getId());
	    			if(!ShareData.getPanelTimerMap().containsKey(ippanel.getId()+"")){
	    				//����
	    				Hashtable taskhash = taskutil.getInterval("updatepaneltask");
	    				if(taskhash != null && taskhash.size()>0){
	    					taskutil.addPanelTask(ippanel,taskhash);
	    				}
	    				//TaskUtil.addPanelTask(ippanel);
	    			}
	    		} 
	    		taskutil = null;
	    		
	    		//�ж���·�Ƿ�ɾ������ɾ������ȡ����ʱ
	    		Iterator iterator = ShareData.getPanelTimerMap().keySet().iterator();
	    		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    		while (iterator.hasNext()) {
	    			 String panelid = String.valueOf(iterator.next());
	    			 if(!dohash.containsKey(panelid)){
	    				 //ȡ��
	    				 ShareData.getPanelTimerMap().get(panelid).cancel();
	    				 ShareData.getPanelTimerMap().remove(panelid);
	    			 }
	    		}
				SysLogger.info("********UpdatePanelTaskTest Thread Count : "+Thread.activeCount());
			}
		}
        //������������
    
	}	
}
