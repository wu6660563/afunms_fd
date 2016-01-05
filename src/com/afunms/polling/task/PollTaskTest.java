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
public class PollTaskTest extends MonitorTask {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private Host host;
	
	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}
	
	
	public void run() {
		if(host == null){//�ýڵ㲻����,nodeidδ����ʼ��
			SysLogger.info("���豸������,hostδ����ʼ��");
			return;
		}
    	//��ʼ��������
		try {
			SysLogger.info(host.getIpAddress()+" �����豸���� ������ ");
			host.getAlarmMessage().clear();
			host.doPoll();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
	       	int pollCollectedSize = -1;
        	if(host != null){
        		pollCollectedSize = ShareData.addPollCollectedSize();
        	}
        	int needCollectNodesSize = ShareData.getPollTimerMap().keySet().size();
        	//System.out.println("####linkid:"+linkid+"####needCollectNodesSize:"+needCollectNodesSize+"====ShareData.getLinkCollectedSize():"+linkCollectedSize);
        	//�ж�����Task�Ƿ��������
			if(needCollectNodesSize == pollCollectedSize){//��Ҫ�ɼ����豸���� �� �Ѳɼ����豸������ȣ��򱣴�
	    		ShareData.setPollCollectedSize(0);
	    		SysLogger.info("##############################");
	    		SysLogger.info("### �����豸�������  ####");
	    		SysLogger.info("##############################");
	    		
	    		collectData(PollingEngine.getInstance().getNodeList());

	    		//�ж��豸�����Ƿ��Ѿ�������ʱ����δ����������
	    		Host host = null;
	    		Hashtable dohash = new Hashtable();
	    		TaskUtil taskutil = new TaskUtil();
	    		for(int i=0;i<PollingEngine.getInstance().getNodeList().size();i++){
	    			host = (Host)PollingEngine.getInstance().getNodeList().get(i);
	    			dohash.put(host.getId()+"", host.getId());
	    			if(!ShareData.getPollTimerMap().containsKey(host.getId()+"")){
	    				//����
	    				//ShareData.getPollTimerMap().get(host.getId()+"").
	    				//TaskUtil taskutil = new TaskUtil();
	    				Hashtable taskhash = taskutil.getInterval("updatexmltask");
	    				if(taskhash != null && taskhash.size()>0){
	    					taskutil.addPollTask(host,taskhash);
	    				}
	    				
	    			}
	    		}
	    		taskutil = null;
	    		
	    		//�ж��豸�����Ƿ�ɾ������ɾ������ȡ����ʱ
	    		Iterator iterator = ShareData.getPollTimerMap().keySet().iterator();
	    		while (iterator.hasNext()) {
	    			 String hostid = String.valueOf(iterator.next());
	    			 if(!dohash.containsKey(hostid)){
	    				 //ȡ��
	    				 ShareData.getPollTimerMap().get(hostid).cancel();
	    				 ShareData.getPollTimerMap().remove(hostid);
	    			 }
	    		}
				SysLogger.info("********PollTaskTest Thread Count : "+Thread.activeCount());
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
}
