/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.PingInfoParser;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.model.IpAlias;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.telnet.SSHWrapper;
import com.afunms.polling.telnet.TelnetWrapper;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.ConnectTypeConfigDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.RemotePingHostDao;
import com.afunms.topology.dao.RemotePingNodeDao;
import com.afunms.topology.model.ConnectTypeConfig;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.RemotePingHost;
import com.afunms.topology.model.RemotePingNode;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PingTask extends MonitorTask {
	private String ip;
	private static Hashtable connectConfigHashtable = new Hashtable();
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try{
//			Vector v=null;
//			
//			//��ʼ����ͨ�Լ�ⷽʽ����
//			ConnectTypeConfigDao connectTypeConfigDao = new ConnectTypeConfigDao();
//			List configList = new ArrayList();
//			try{
//				configList = connectTypeConfigDao.findByCondition(" where flag = 1");
//			}catch(Exception e){
//				
//			}finally{
//				connectTypeConfigDao.close();
//			}
//			if(configList != null && configList.size()>0){
//				for(int i=0;i<configList.size();i++){
//					ConnectTypeConfig connectTypeConfig = (ConnectTypeConfig)configList.get(i);
//					connectConfigHashtable.put(connectTypeConfig.getNode_id(), connectTypeConfig);
//				}
//			}
//	    	HostNodeDao nodeDao = new HostNodeDao(); 
//	    	//�õ������ӵ��豸
//	    	List nodeList = new ArrayList();
//	    	try{
//	    		nodeList = nodeDao.loadIsMonitored(1);
//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}finally{
//	    		nodeDao.close();
//	    	}
//    		int numTasks = nodeList.size();
//    		int numThreads = 200;
//    		try {
//    			List numList = new ArrayList();
//    			TaskXml taskxml = new TaskXml();
//    			numList = taskxml.ListXml();
//    			for (int i = 0; i < numList.size(); i++) {
//    				Task task = new Task();
//    				BeanUtils.copyProperties(task, numList.get(i));
//    				if (task.getTaskname().equals("pingthreadnum")){
//    					numThreads = task.getPolltime().intValue();
//    				}
//    			}
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}					  
//		  
//  		// �����̳߳�
//  		ThreadPool threadPool = new ThreadPool(numThreads);														
//  		// ��������
//  		TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
//  		for (int i=0; i<nodeList.size(); i++) {
//  			HostNode node = (HostNode)nodeList.get(i);
//			//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
//			
//			int result = 0;
//			result = timeconfig.isBetween(node.getId()+"", "equipment");
//			if(result ==1 ){
//				SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
//				//threadPool.runTask(createTask(node));
//			}else if(result == 2){
//				SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
//				//threadPool.runTask(createTask(node));
//			}else {
//				SysLogger.info("######## "+node.getIpAddress()+" ���ڲɼ�PINGʱ�����,�˳�##########");
//				return;
//			}
//  		}
//		
//  		// �ر��̳߳ز��ȴ������������
//  		threadPool.join();
		}
		catch(Exception e){
			//SysLogger.info("error in ExecutePing!"+e.getMessage());
	  	}
		finally{
			//SysLogger.info("********Ping Thread Count : "+Thread.activeCount());
		}		
	}

/**
��������
*/	
private static Runnable createTask(final HostNode hostnode) {
return new Runnable() {
    public void run() {
    	I_HostCollectData hostdataManager=new HostCollectDataManager(); 
    	Vector vector=null;
        try {    
        	/*
        	 * ���ж��Ƿ�Ϊremoteping����������,����,����˽���PING֮��,��Ҫ���������ӽڵ����REMOTEPING
        	 */
        	if(hostnode.getEndpoint()==1){
    			PingUtil pingU=new PingUtil(hostnode.getIpAddress());
    			Integer[] packet=pingU.ping();
    			vector=pingU.addhis(packet); 
//    			if(vector!=null){				
//    				hostdataManager.createHostData(vector);					
//    				ShareData.setPingdata(hostnode.getIpAddress(),vector);
//    			}
    			vector=null;
    			
    			if(hostnode.getCategory() < 4 ){
    				//�����豸����ҪPING�����Ľڵ��ַ
    				IpAliasDao ipdao = new IpAliasDao();
    			     List iplist = new ArrayList();
    			     try{
    			    	 iplist = ipdao.loadByIpaddress(hostnode.getIpAddress());
    			     }catch(Exception e){
    			    	 e.printStackTrace();
    			     }finally{
    			    	 ipdao.close();
    			     }
    			     if(iplist!=null && iplist.size()>0){
	    			     try{
	    			    	 int numThreads = iplist.size();
	    			     
	    			    	 // �����̳߳�
	    			    	 ThreadPool threadPool = new ThreadPool(numThreads);														
	    			    	 // ��������
	    			    	 for (int i=0; i<iplist.size(); i++) {
	    			    		 threadPool.runTask(createTask2((IpAlias)iplist.get(i)));
	    			    	 }    					
	    			    	 // �ر��̳߳ز��ȴ������������
	    			    	 threadPool.join();
	    			    	 threadPool.close();
	    			    	 threadPool = null;
	    			     }catch(Exception e){
	    			    	 SysLogger.info("error in ExecutePing!"+e.getMessage());
	    			     }finally{
							//SysLogger.info("********Ping Thread Count : "+Thread.activeCount());
	    			     }
    			     }
    			}			
        		//��Ҫ�����������ӽڵ����PING����			
    			List list = null ;   			
    			RemotePingNodeDao remotePingNodeDao = new RemotePingNodeDao();
    			try {
    				list = remotePingNodeDao.findByNodeId(String.valueOf(hostnode.getId()));
    			} catch (RuntimeException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} finally{
    				remotePingNodeDao.close();
    			} 
    			HostNodeDao hostNodeDao = new HostNodeDao();   			
    			try{
    				RemotePingHost remotePingHost = null ;
    				RemotePingHostDao remotePingHostDao = new RemotePingHostDao();
    				try {
						remotePingHost =  remotePingHostDao.findByNodeId(String.valueOf(hostnode.getId()));
					} catch (RuntimeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally{
						remotePingHostDao.close();
					}
    				TelnetWrapper telnet = new TelnetWrapper();
    				try {
						telnet.connect(hostnode.getIpAddress(), 23);
						telnet.login(remotePingHost.getUsername(), remotePingHost.getPassword(),
								remotePingHost.getLoginPrompt(), remotePingHost.getPasswordPrompt() , 
								remotePingHost.getShellPrompt());						
						// ��������
						for (int i=0; i<list.size(); i++) {
							String result = "";
							RemotePingNode remotePingNode = (RemotePingNode)list.get(i);
							HostNode hostNodeTemp = (HostNode)hostNodeDao.findByID(remotePingNode.getChildNodeId());
			    			result = telnet.send("ping " + hostNodeTemp.getIpAddress());
			    			setData(result , hostNodeTemp);
						}
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}     	  		
    			}catch(Exception e){
    				SysLogger.info("error in ExecutePing!"+e.getMessage());
    		  	}
    			finally{
    				hostNodeDao.close();
    				
    			}       		
        	}else if(hostnode.getEndpoint()==2){
        		//��ΪREMOTEPING�ӽڵ㣬�򷵻�
        		//return;
        	}else{
        		//�������豸
        		if(connectConfigHashtable.containsKey(hostnode.getId()+"")){
        			//��������ͨ�Լ������,Ϊֻͨ��TELNET��SSH�����ͨ��
        			ConnectTypeConfig connectTypeConfig = (ConnectTypeConfig)connectConfigHashtable.get(hostnode.getId()+"");
        			if("telnet".equalsIgnoreCase(connectTypeConfig.getConnecttype())){
        				//��TELNET��ʽ���м��
        				Pingcollectdata hostdata=null;
    					Calendar date=Calendar.getInstance();
    					hostdata=new Pingcollectdata();
    					hostdata.setIpaddress(hostnode.getIpAddress());
    					hostdata.setCollecttime(date);
    					hostdata.setCategory("Ping");
    					hostdata.setEntity("Utilization");
    					hostdata.setSubentity("ConnectUtilization");
    					hostdata.setRestype("dynamic");
    					hostdata.setUnit("%");
        				int flag = 0;
        				Vector<Pingcollectdata> _vector = new Vector<Pingcollectdata>();
        				TelnetWrapper telnet = new TelnetWrapper();
        				long starttime = 0;
        				long endtime = 0;
        				long condelay = 0;
        				try
						{
        					starttime = System.currentTimeMillis();
							telnet.connect(hostnode.getIpAddress(), 23);
							telnet.login(connectTypeConfig.getUsername(), EncryptUtil.decode(connectTypeConfig.getPassword()), connectTypeConfig.getLoginPrompt(), connectTypeConfig.getPasswordPrompt(), connectTypeConfig.getShellPrompt());
							endtime = System.currentTimeMillis();
						} catch (Exception e){
							endtime = System.currentTimeMillis();
							e.printStackTrace();
							flag = 1;
						}finally{
							try{
								telnet.disconnect();
							} catch (Exception e){
								e.printStackTrace();
							}
						}
						condelay = endtime - starttime;
						if(flag == 0){
							hostdata.setThevalue("100");
						}else{
							hostdata.setThevalue("0");
						}
						_vector.add(0, hostdata);
						
						//��Ӧʱ��
						hostdata=new Pingcollectdata();
						hostdata.setIpaddress(hostnode.getIpAddress());
						hostdata.setCollecttime(date);
						hostdata.setCategory("Ping");
						hostdata.setEntity("ResponseTime");
						hostdata.setSubentity("ResponseTime");
						hostdata.setRestype("dynamic");
						hostdata.setUnit("����");
						hostdata.setThevalue(condelay+"");
						_vector.add(1, hostdata);
            			if(_vector!=null){				
//            				hostdataManager.createHostData(_vector);					
//            				ShareData.setPingdata(hostnode.getIpAddress(),_vector);
            			}
            			vector=null;
            			
        			}else if("ssh".equalsIgnoreCase(connectTypeConfig.getConnecttype())){
        				//��SSH��ʽ���м��
        				Pingcollectdata hostdata=null;
    					Calendar date=Calendar.getInstance();
    					hostdata=new Pingcollectdata();
    					hostdata.setIpaddress(hostnode.getIpAddress());
    					hostdata.setCollecttime(date);
    					hostdata.setCategory("Ping");
    					hostdata.setEntity("Utilization");
    					hostdata.setSubentity("ConnectUtilization");
    					hostdata.setRestype("dynamic");
    					hostdata.setUnit("%");
        				int flag = 0;
        				Vector<Pingcollectdata> _vector = new Vector<Pingcollectdata>();
        				SSHWrapper ssh = new SSHWrapper();
        				long starttime = 0;
        				long endtime = 0;
        				long condelay = 0;
        				try
						{
        					starttime = System.currentTimeMillis();
        					ssh.connect(hostnode.getIpAddress(), 22 , connectTypeConfig.getUsername() , EncryptUtil.decode(connectTypeConfig.getPassword()));
							endtime = System.currentTimeMillis();
						} catch (Exception e){
							endtime = System.currentTimeMillis();
							e.printStackTrace();
							flag = 1;
						}finally{
							try{
								ssh.disconnect();
							} catch (Exception e){
								e.printStackTrace();
							}
						}
						condelay = endtime - starttime;
						if(flag == 0){
							hostdata.setThevalue("100");
						}else{
							hostdata.setThevalue("0");
						}
						_vector.add(0, hostdata);
						
						//��Ӧʱ��
						hostdata=new Pingcollectdata();
						hostdata.setIpaddress(hostnode.getIpAddress());
						hostdata.setCollecttime(date);
						hostdata.setCategory("Ping");
						hostdata.setEntity("ResponseTime");
						hostdata.setSubentity("ResponseTime");
						hostdata.setRestype("dynamic");
						hostdata.setUnit("����");
						hostdata.setThevalue(condelay+"");
						_vector.add(1, hostdata);
            			if(_vector!=null){				
//            				hostdataManager.createHostData(_vector);					
//            				ShareData.setPingdata(hostnode.getIpAddress(),_vector);
            			}
            			vector=null;
        			}else{
        				//������ʽ������������չ
        			}
        		}else{
        			//ͨ��PING����������ͨ�Լ��
    				PingUtil pingU=new PingUtil(hostnode.getIpAddress());
        			Integer[] packet=pingU.ping();
        			vector=pingU.addhis(packet); 
        			if(vector!=null){				
//        				hostdataManager.createHostData(vector);					
//        				ShareData.setPingdata(hostnode.getIpAddress(),vector);
        			}
        			vector=null;
        			if(hostnode.getCategory() < 4 ){
        				//�����豸����ҪPING�����Ľڵ��ַ
        				IpAliasDao ipdao = new IpAliasDao();
        			     List iplist = new ArrayList();
        			     try{
        			    	 iplist = ipdao.loadByIpaddress(hostnode.getIpAddress());
        			     }catch(Exception e){
        			    	 e.printStackTrace();
        			     }finally{
        			    	 ipdao.close();
        			     }
        			     if(iplist!=null && iplist.size()>0){
    	    			     try{
    	    			    	 int numThreads = iplist.size();  	    			     
    	    			    	 // �����̳߳�
    	    			    	 ThreadPool threadPool = new ThreadPool(numThreads);														
    	    			    	 // ��������
    	    			    	 for (int i=0; i<iplist.size(); i++) {
    	    			    		 threadPool.runTask(createTask2((IpAlias)iplist.get(i)));
    	    			    	 }      	    					
    	    			    	 // �ر��̳߳ز��ȴ������������
    	    			    	 threadPool.join();
    	    			     }catch(Exception e){
    	    			    	 SysLogger.info("error in ExecutePing!"+e.getMessage());
    	    			     }finally{
    							//SysLogger.info("********Ping Thread Count : "+Thread.activeCount());
    	    			     }
        			     }    
        			}
        		}
    			
        	}
        }catch(Exception exc){
        	
        }
    }
};
}
	private static Runnable createTask2(final IpAlias vo){
		return new Runnable(){
			public void run(){
		    	PingUtil pingU=new PingUtil(vo.getAliasip());
				Integer[] packet=pingU.ping();
				Vector vector=pingU.addhis(packet); 
				Vector endV = new Vector();
				if(vector!=null&&vector.size()>0){
					for(int j=0;j<vector.size();j++){
						Pingcollectdata hostdata = (Pingcollectdata)vector.get(j);
						if(hostdata.getEntity().equals("Utilization")){
							endV.add(hostdata);
							break;
						}
					}							
					ShareData.setRelateippingdata(vo.getAliasip(),endV);

   					}
   				vector=null;
			}
		};
	}

	private static void setData(String result ,HostNode hostnode){
		Vector vector = null ;
		int[] pingresult;
		if(result != null && result.length()>0){
			I_HostCollectData hostdataManager=new HostCollectDataManager(); 
			pingresult = PingInfoParser.parsePingInfo(result);
			if(pingresult!=null){
				Integer[] integer = new Integer[pingresult.length];
				for(int i = 0 ; i < pingresult.length ; i ++){
					integer[i] = pingresult[i];
				}
				
				PingUtil pingU=new PingUtil(hostnode.getIpAddress());
				vector=pingU.addhis(integer); 
				if(vector!=null){				
					try {
//						hostdataManager.createHostData(vector);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ShareData.setPingdata(hostnode.getIpAddress(),vector);
				}
				vector=null;
			}
		}
	}
}
