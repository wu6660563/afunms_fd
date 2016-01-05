package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.SystemConstant;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.Task;
import com.afunms.polling.telnet.SSHWrapper;
import com.afunms.polling.telnet.TelnetWrapper;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.RemotePingHostDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.RemotePingHost;

public class SSHPollTask extends MonitorTask 
{
	
	public SSHPollTask()
	{
		super();
	}

	public void run()
	{
		System.out.println("########################### begin to execute SSH polling task");
		
		HostNodeDao nodeDao = new HostNodeDao(); 
    	//�õ������ӵ������������豸
    	List nodeList = new ArrayList();
    	try{
    		nodeList = nodeDao.loadHostByFlag(1);   	    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		nodeDao.close();
    	}
    	if(nodeList != null)
    	{
	    	for(int i = nodeList.size() - 1 ; i >= 0 ; i--)
	    	{    		
	    		HostNode node = (HostNode)nodeList.get(i);	
	    		// 4 ����Ϊ �� SSH Э����вɼ�
	    		if(node.getEndpoint() != 4)
	    		{
	    			nodeList.remove(i);
	    		}
	    	}
    	}
    	
    	
    	try
		{
			if(nodeList != null && nodeList.size() > 0)
			{
				
				System.out.println("there have " + nodeList.size() + " node to collect by SSH");
				
				int numTasks = nodeList.size();
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
				ThreadPool threadPool = null;	
				if(nodeList != null && nodeList.size()>0){
					threadPool = new ThreadPool(nodeList.size());
					// ��������
					for (int i=0; i<numTasks; i++) {
						HostNode node = (HostNode)nodeList.get(i);
						
						threadPool.runTask(createTask(node));
					}
					// �ر��̳߳ز��ȴ������������
					threadPool.join(); 
					threadPool.close();
					threadPool = null;
				}
				 		        		
											
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			System.out.println("********SSH Poll Thread Count : "+Thread.activeCount());
		}
    	
    	System.out.println("######################### end of execute SSH polling task");
    	
    	
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
            	
            	
            	I_HostLastCollectData hostlastdataManager=new HostLastCollectDataManager();
            	if(node.getCollecttype() == SystemConstant.COLLECTTYPE_SSH){
            		//SSH ��ȡ��ʽ        		
            		try{
            			
            			//  ���Ȼ�ȡЭ����Ϣ
            			System.out.println("begin to collect info of " + node.getIpAddress() + " by ssh");
            			int nodeId = node.getId();
            			
            			RemotePingHostDao hostDao = new RemotePingHostDao();
            			RemotePingHost params = hostDao.findByNodeId(String.valueOf(nodeId)); 
            			hostDao.close();
            			if(params != null)
            			{
            				SSHWrapper ssh = new SSHWrapper();
            				try
							{
								ssh.connect(node.getIpAddress(), 22 , params.getUsername() , params.getPassword());
								ssh.log("connected to " + node.getIpAddress() + " at " + new Date().toLocaleString());
								hashv = ssh.getTelnetMonitorDetail();
								ssh.log("end of collect by ssh at " + new Date().toLocaleString() + "\n\n\n\n\n");
								
								
								if(hashv != null)
								{
									I_HostCollectData hostdataManager=new HostCollectDataManager();
									hostdataManager.createHostData(node.getIpAddress() , hashv);
									hostdataManager = null;
								}
								
							} catch (Exception e)
							{
								e.printStackTrace();
							}
							finally
							{
								try
								{
									ssh.disconnect();
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
            				
            				
            			}
            			else
            			{
            				System.out.println("can not find the parameters of SSH protocol by nodeId " + nodeId);
            			}
            			

            		}catch(Exception e){
            			e.printStackTrace();
            		}
					
					hashv=null;
            	}

            }catch(Exception exc){
            	
            }

            
        }
    };
}
	
	
	
	
	

}
