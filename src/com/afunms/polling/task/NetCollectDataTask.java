/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.BDComSnmp;
import com.afunms.polling.snmp.CiscoSnmp;
import com.afunms.polling.snmp.DLinkSnmp;
import com.afunms.polling.snmp.DigitalChinaSnmp;
import com.afunms.polling.snmp.EnterasysSnmp;
import com.afunms.polling.snmp.H3CSnmp;
import com.afunms.polling.snmp.HarbourSnmp;
import com.afunms.polling.snmp.MaipuSnmp;
import com.afunms.polling.snmp.NortelSnmp;
import com.afunms.polling.snmp.RedGiantSnmp;
import com.afunms.polling.snmp.ZTESnmp;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

//import com.dhcc.webnms.host.ShareData;

/**
 * @author hukelei@dhcc.com.cn
 *
 */
public class NetCollectDataTask extends MonitorTask {
		
	/**
	 * 
	 */
	public NetCollectDataTask() {
		super();
	}

	public void run() {
		try{
	    	HostNodeDao nodeDao = new HostNodeDao(); 
	    	//�õ������ӵ������豸
	    	List nodeList = new ArrayList();
	    	try{
	    		nodeList = nodeDao.loadNetwork(1);   	    	
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		nodeDao.close();
	    	}
        		int numThreads = 200;
        		
        		try {
        			List numList = new ArrayList();
        			TaskXml taskxml = new TaskXml();
        			numList = taskxml.ListXml();
        			for (int i = 0; i < numList.size(); i++) {
        				Task task = new Task();
        				BeanUtils.copyProperties(task, numList.get(i));
        				if (task.getTaskname().equals("netthreadnum")){
        					numThreads = task.getPolltime().intValue();
        				}
        			}

        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        		

        		// �����̳߳�
        		ThreadPool threadPool = new ThreadPool(numThreads);														
        		// ��������
        		for (int i=0; i<nodeList.size(); i++) {
        			HostNode node = (HostNode)nodeList.get(i);
        			//��ֻ��PING TELNET SSH��ʽ��������,���������ݲ��ɼ�,����
        			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_PING ||
        					node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT||
        					node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT){
        				SysLogger.info(node.getIpAddress()+" ֻPING TELNET SSH��ʽ��������,�������ݲ��ɼ�,����");
        			}else{
        				if(node.getEndpoint() != 2){
                			//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
                			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
                			int result = 0;
                			result = timeconfig.isBetween(node.getId()+"", "equipment");
                			if(result ==1 ){
                				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" ������Ϣ##########");
                				threadPool.runTask(createTask(node));
                			}else if(result == 2){
                				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" ������Ϣ##########");
                				threadPool.runTask(createTask(node));
                			}else {
                				//SysLogger.info("######## "+node.getIpAddress()+" ���ڲɼ�ʱ�����,�˳�##########");
                				//continue;
                			}       				
            			}
        			}			
        		}
        		// �ر��̳߳ز��ȴ������������
        		threadPool.join(); 
        		threadPool.close();
        		threadPool = null;
											
		}catch(Exception e){					 	
			e.printStackTrace();
		}
			finally{
			System.out.println("********Net Thread Count : "+Thread.activeCount());
		}
				
	}
	
    /**
        ��������
    */	
    private static Runnable createTask(final HostNode node) {
        return new Runnable() {
            public void run() {
                try {                	
                	Vector vector=null;
                	Hashtable hashv = null;
                	I_HostCollectData hostdataManager=new HostCollectDataManager();
                	if(node.getSysOid().startsWith("1.3.6.1.4.1.9.")){
                		//cisco
                		CiscoSnmp sCisco=new CiscoSnmp();
                		try{
                			
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                				hashv=sCisco.collect_Data(node);          				
                			}
                				
                			hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		sCisco=null;
                		vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.25506.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪH3C�豸������");
                		//huawei or h3c
    					H3CSnmp h3c=new H3CSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=h3c.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
    					h3c=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.3320.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ�����豸������");
                		//�����豸
                		BDComSnmp bdcom=new BDComSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=bdcom.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		bdcom=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.171.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ D-Link �豸������");
                		//D-Link
    					DLinkSnmp dlinksnmp=new DLinkSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=dlinksnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		dlinksnmp=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.6339.99.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ Digital China �豸������");
                		//��������
                		DigitalChinaSnmp digitalsnmp=new DigitalChinaSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=digitalsnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		digitalsnmp=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.5567.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ ZTE �豸������");
                		//����
                		ZTESnmp ztesnmp=new ZTESnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=ztesnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		ztesnmp=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.8212.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ Harbour �豸������");
                		//����
                		HarbourSnmp harboursnmp=new HarbourSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=harboursnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		harboursnmp=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.3902.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ ZTE �豸������");
                		//����
                		ZTESnmp ztesnmp=new ZTESnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=ztesnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		ztesnmp=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪHuaWei�豸������");
                		//huawei or h3c
    					H3CSnmp h3c=new H3CSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=h3c.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
    					h3c=null;
    					vector=null; 
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.4881.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ����豸������");
                		//���
                		RedGiantSnmp redgiant=new RedGiantSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=redgiant.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		redgiant=null;
    					vector=null;
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.52.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ�����豸������");
                		//Enterasys
                		EnterasysSnmp enterasys=new EnterasysSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=enterasys.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		enterasys=null;
    					vector=null; 
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.45.")){
                		//SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ�����豸������");
                		//Nortel(�����豸)
                		NortelSnmp nortelsnmp=new NortelSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=nortelsnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		nortelsnmp=null;
    					vector=null; 
                	}else if(node.getSysOid().startsWith("1.3.6.1.4.1.5651.")){
                		SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����Ϊ�����豸������");
                		//Maipu
                		MaipuSnmp maipusnmp=new MaipuSnmp();
    					try{
    						//�Բɼ���ʽΪSNMP���豸�������ݲɼ�
                			if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SNMP){
                				hashv=maipusnmp.collect_Data(node);
                			}
    						hostdataManager.createHostData(node.getIpAddress(),hashv);
                		}catch(Exception ex){
                			ex.printStackTrace();
                		}
                		maipusnmp=null;
    					vector=null;
                	}
				
				
				/*
				if(monitorIp.getOssource().getOsname().toLowerCase().indexOf("enterasys")>=0 && monitorIp.getMethoddic().getMethodname().indexOf("snmp")>=0){
					EnterasysSnmp sCisco=new EnterasysSnmp(monitorIp.getIpaddress(),monitorIp.getCommunity(),monitorIp.getPort());
					hashv=sCisco.collectData();
					//if(vector!=null){					
						hostdataManager.createHostData(monitorIp.getIpaddress(),hashv);
						//hostlastdataManager.createHostData(monitorIp.getIpaddress(),vector);
						//ShareData.setSharedata(monitorIp.getIpaddress(),vector);
					//}
					sCisco=null;
					vector=null;
				}

				if(monitorIp.getOssource().getOsname().toLowerCase().indexOf("fundry")>=0 && monitorIp.getMethoddic().getMethodname().indexOf("snmp")>=0){
					FundrySnmp sCisco=new FundrySnmp(monitorIp.getIpaddress(),monitorIp.getCommunity(),monitorIp.getPort());
					hashv=sCisco.collectData();
					//if(vector!=null){					
						hostdataManager.createHostData(monitorIp.getIpaddress(),hashv);
						//hostlastdataManager.createHostData(monitorIp.getIpaddress(),vector);
						//ShareData.setSharedata(monitorIp.getIpaddress(),vector);
					//}
					sCisco=null;
					vector=null;
				}
				
				if(monitorIp.getOssource().getOsname().toLowerCase().indexOf("alcatel")>=0 && monitorIp.getMethoddic().getMethodname().indexOf("snmp")>=0){
					AlcatelSnmp sCisco=new AlcatelSnmp(monitorIp.getIpaddress(),monitorIp.getCommunity(),monitorIp.getPort());
					hashv=sCisco.collectData();
					//if(vector!=null){					
						hostdataManager.createHostData(monitorIp.getIpaddress(),hashv);
						//hostlastdataManager.createHostData(monitorIp.getIpaddress(),vector);
						//ShareData.setSharedata(monitorIp.getIpaddress(),vector);
					//}
					sCisco=null;
					vector=null;
				}                    
				if(monitorIp.getOssource().getOsname().toLowerCase().indexOf("radware")>=0 && monitorIp.getMethoddic().getMethodname().indexOf("snmp")>=0){
					RadwareSnmp sCisco=new RadwareSnmp(monitorIp.getIpaddress(),monitorIp.getCommunity(),monitorIp.getPort());
					hashv=sCisco.collectData();
					//if(vector!=null){					
						hostdataManager.createHostData(monitorIp.getIpaddress(),hashv);
						//hostlastdataManager.createHostData(monitorIp.getIpaddress(),vector);
						//ShareData.setSharedata(monitorIp.getIpaddress(),vector);
					//}
					sCisco=null;
					vector=null;
				}                    
				
				if(monitorIp.getOssource().getOsname().toLowerCase().indexOf("hirschmann")>=0 && monitorIp.getMethoddic().getMethodname().indexOf("snmp")>=0){
					HirschmannSnmp sCisco=new HirschmannSnmp(monitorIp.getIpaddress(),monitorIp.getCommunity(),monitorIp.getPort());
					hashv=sCisco.collectData();
					//if(vector!=null){					
						hostdataManager.createHostData(monitorIp.getIpaddress(),hashv);
						//hostlastdataManager.createHostData(monitorIp.getIpaddress(),vector);
						//ShareData.setSharedata(monitorIp.getIpaddress(),vector);
					//}
					sCisco=null;
					vector=null;
				}                    
				*/
                }catch(Exception exc){
                	
                }

                //System.out.println("Task " + taskID + ": end");
            }
        };
    }
	
}
