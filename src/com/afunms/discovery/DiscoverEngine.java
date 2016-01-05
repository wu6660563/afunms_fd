/**
 * <p>Description:discovery engine</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.discovery;

import java.util.*;
import java.util.concurrent.*;

import com.afunms.common.util.*;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.model.HostNode;
import com.afunms.common.util.SysLogger;

public class DiscoverEngine
{
	private static List subNetList;	
	private static List hostList;
	private static List faildIpList;//����ʧ�ܵ��豸IP��Ŀǰ����ǵ�һ�η���ʧ�ܺ�֮��Ͳ��ڷ��ָ��豸
	private static List existIpList; //�Ѿ������豸��IP	
	private static List linkList;
	private static List routelinkList;
	private static List maclinkList;
	private static List threadList;
	private static List futureList;
	private static List formerNodeList;//�洢��ǰ�Ѿ����ֵ��豸
	private static List formerNodeLinkList;//�洢֮ǰ���ֻ���ӵ����ӹ�ϵ
	private static int discovermodel;
	private static String writecommunity;
	private static int snmpversion;
	private static String discover_bid;
	private static int stopStatus;
	
	public static int threads;
	public static int discoverdcount;
	private static SnmpUtil snmp = SnmpUtil.getInstance();	
	private static final int MAX_THREADS = DiscoverResource.getInstance().getMaxThreads(); //����߳���,����tomcat����   
	private static ExecutorService threadExecutor = Executors.newFixedThreadPool(MAX_THREADS);		
    private static DiscoverEngine instance = new DiscoverEngine();
	// �����̳߳�
	private static ThreadPool threadPool = new ThreadPool(200);
    
	public synchronized int getDiscoverdcount(){
		return this.discoverdcount;
	}
	public synchronized void addDiscoverdcount(){
		discoverdcount++;
	}
	
	public synchronized void setDiscoverdcount(int count){
		this.discoverdcount = count ;
	}
	
	public static DiscoverEngine getInstance()
	{	
		//SysLogger.info("#############################");
		try{
		if(instance == null){
			instance = new DiscoverEngine();//�������·���,��Ҫ���³�ʼ������
			//threadExecutor.shutdownNow();
			//threadExecutor = null;
			
			//threadPool = new ThreadPool(200);

			threadExecutor = Executors.newFixedThreadPool(MAX_THREADS);	
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}
		if(threadExecutor == null)threadExecutor = Executors.newFixedThreadPool(MAX_THREADS);	
        return instance;       
    }
   
	public void unload() 
	{		
		//threadExecutor.shutdownNow();
		threadPool.interrupt();
		threadPool.close();
		/*
		if (!threadExecutor.isShutdown()) {

			List<Runnable> threads = threadExecutor.shutdownNow();

			for (int i = 0; i < threads.size(); ++i) {
				try {
					((Runnable)threads.get(i));
				} catch (Exception e) {

				}
			}
		}
		*/
		instance = null;
		threadList = null;
		subNetList = null;
		hostList = null;
		faildIpList = null;
		existIpList = null;
		linkList= null;
		routelinkList = null;
		maclinkList = null;
		formerNodeList = null;
		formerNodeLinkList= null;
		threadExecutor = null;
		futureList = null;
		threads = 0;
		discoverdcount = 0;
		threadPool = null;
		SysLogger.info("DiscoverEngine.unload()");
	}

    private DiscoverEngine()
    {        	
    	subNetList = new ArrayList(100);
    	hostList = new ArrayList(50);
    	existIpList = new ArrayList(200);
        linkList = new ArrayList(50);
        routelinkList = new ArrayList(50);
        maclinkList = new ArrayList(50);
        threadList = new ArrayList(50);
        faildIpList = new ArrayList(50);
        futureList = new ArrayList(200);
        formerNodeList = new ArrayList(200);
        formerNodeLinkList = new ArrayList(200);
        threadExecutor = Executors.newFixedThreadPool(MAX_THREADS);	
        threadPool = new ThreadPool(200);
		threads = 0;
		discoverdcount = 0;
    }
    
    public synchronized void addThread(Thread newThread)  
    {
    	threadExecutor.execute(newThread);  
    	threadList.add(newThread);    	
    	newThread.setName("afunms[" + threads + "]");
    	threads++;
    }
    /*
    public synchronized void addThread(Runnable newThread)  
    {
    	threadPool.runTask(newThread);
    	//threadPool.join();
    	//threadExecutor.execute(newThread);  
    	//futureList.add(future);
    	//threadList.add(newThread);    	
    	//newThread.setName("afunms[" + threads + "]");
    	threads++;
    }
    */
         
    /**
     * ���豸�����Ѿ�ȷ�����豸��������,deviceType=1,2,3,4,5,7,8 
     */
    public void addHost(Host newNode,Link link)
    {
    	/**
    	 * 
    	 * <select size=1 name='type' style='width:100px;' onchange="unionSelect();">
            								<option value='1' selected>·����</option>
            								<option value='2'>·�ɽ�����</option>
            								<option value='3'>������</option>
            								<option value='4'>������</option>
            								<option value='8'>����ǽ</option>
            							</select>
    	 */
    	int flag = 0;
    	if(newNode.getSysOid() == null)
    	{
    		newNode.setDiscovered(true);//�о���������Ϊtrue�����������豸�Ѿ��������ˣ�����Ϊ����������һ��״̬��ʹ���沿�ִ��벻ִ�С�
    		return;
    	}
    	//������Ҫ��Ӵ��Ѿ����ڵ��豸�б��л�ȡ�豸,�������Ա����ظ�ȡ�豸����
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&******"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    		if(tmpNode.getIpAddress().equalsIgnoreCase(newNode.getIpAddress()))//�鿴�Ƿ��Ǵ���ָ��IP�������豸
    		{
    			newNode = tmpNode;
    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
    			flag = 1;
    			break;
    		}
    		else
    		{
    			List aliasIPS = tmpNode.getAliasIPs();
    			if(aliasIPS == null)continue;
    			if(aliasIPS.contains(newNode.getIpAddress()))//�鿴��IP�Ƿ��������豸�ڶ����ڵ�����һ�����ڣ�����ǣ�˵����IP������������豸�Ѿ������ֹ���
    			{
        			newNode = tmpNode;
        			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
        			flag = 1;
        			break;
    			}
    		}
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}
    	
    	
    	SysLogger.info("��ʼ��������Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress());    	
    	//·�ɽ�������㽻��,���������˷���ǽ
    	if(newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)
    	{
    		newNode.setBridgeAddress(snmp.getBridgeAddress(newNode.getIpAddress(),newNode.getCommunity()));//���ø�IP��Ӧ��mac��ַ
    		SysLogger.info("&&&&&&&&&&&&&&&&&&");
    		SysLogger.info(newNode.getIpAddress()+" "+newNode.getBridgeAddress());
    		SysLogger.info("&&&&&&&&&&&&&&&&&&");
       	 	//�õ�STP��������(STPֻ�Զ��㽻��������)
    		if(newNode.getCategory()!=7)
    		{
    			if(newNode.getBridgestpList() == null || newNode.getBridgestpList().size()==0)
    				//snmp.getBridgeStpList(newNode.getIpAddress(),newNode.getCommunity())��õ���com.afunms.discovery.BridgeStpInterface���͵�List
        			//��List��size��getIfEntityList���ص�List��size��С���Բ�һ������ò�������������������������й����ġ�
    				newNode.setBridgestpList(snmp.getBridgeStpList(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    	}
    	//��ȡ���豸�Ľӿڵ�ַ
        if(newNode.getCategory() <5 || newNode.getCategory()==7)
        {
        	//����·�ɺͷ����� 	
        	SysLogger.info("��ʼ��ȡ�豸:"+newNode.getIpAddress()+"�Ľӿڱ�");  
        	if(newNode.getIfEntityList() == null)
        		//snmp.getIfEntityList(newNode.getIpAddress(),newNode.getCommunity(),newNode.getCategory())����com.afunms.discovery.IfEntity���͵�List����IfEntity����ethernet�������Ƕ˿���Ϣ��Ҳ��vlan��˿���Ϣ
        		newNode.setIfEntityList(snmp.getIfEntityList(newNode.getIpAddress(),newNode.getCommunity(),newNode.getCategory()));
        }

//        //��ȡ���豸�Ĺ����ַ
//        if(newNode.getAdminIp() != null){
//        	newNode.setIpAddress(newNode.getAdminIp());
//        	newNode.setAdminIp(newNode.getAdminIp());
//        	//link.setStartIp(newNode.getIpAddress());
//        }
        
        if(newNode.getSysOid() != null )
        {
            if(newNode.getSysOid().indexOf("1.3.6.1.4.1.25506")>=0 &&(newNode.getCategory()==2 || newNode.getCategory()==3))
            {
            	//H3C�Ľ����豸
            	if(newNode.getNdpHash() == null)
            		//���ص�hashtable������ key ����mac��ַ��value ����mac��ַ��Ӧ�Ķ˿ڵ����֣���ethernet1/1/1
            		newNode.setNdpHash(snmp.getNDPTable(newNode.getIpAddress(),newNode.getCommunity()));
            }
        }
        
    	//��һ���豸�Ѿ�����,�Ƿ���Է����ڶ���????
        SysLogger.info("��ʼ�ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"=======�Ƿ��Ѿ�����"); 
        
       
        for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&@@@@@"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}
        
        
        
        
        
        if(link != null)
        	SysLogger.info(newNode.getIpAddress()+" "+newNode.getAdminIp()+"   link:startip: "+link.getStartIp()+" endip:"+link.getEndIp());
    	//�÷�����Ȼ����link���󣬵�ȴδʹ�õ��ö��󣬴˷���ֻ�Ǽ���豸�б����Ƿ��Ѿ�������newNode�ڵ㣬ͨ���Ƚ��豸IP��ַ���
//        if(isHostExist(newNode))
//        {
//    		SysLogger.info("�����ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"�Ƿ��Ѿ�����,��ǰΪ�Ѿ����ڲ�����");
//    		return;
//    	}
    	SysLogger.info("�����ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"======�Ƿ��Ѿ�����");
    	
    	newNode.setId(KeyGenerator.getInstance().getHostKey());
    	//���������豸
    	if(newNode.getCategory()==1 || newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)
    	{
    		if(newNode.getIpNetTable() == null || newNode.getIpNetTable().size()==0)
    		{
    			//����com.afunms.discovery.IpAddress���͵�List��
    			newNode.setIpNetTable(snmp.getIpNetToMediaTable(newNode.getIpAddress(),newNode.getCommunity()));//�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip
    		}
    		if(newNode.getPortMacs() == null || newNode.getPortMacs().size()==0)
    		{
    			//����hashtable��50=[00:0f:e2:49:fc:47, 00:0f:e2:49:fd:fa]����������һ���ļ�ֵ��
    			newNode.setPortMacs(snmp.getDtpFdbTable(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		SysLogger.info(newNode.getIpAddress()+" ################# PortMacs size "+newNode.getPortMacs().size());
    		if(newNode.getAdminIp() == null)
    		{
    			newNode.setAtInterfaces(snmp.getAtInterfaceTable(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		if(newNode.getBridgeIdentifiers() == null){
    			newNode.addBridgeIdentifier(snmp.getBridgeAddress(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		//����fdb�ӿ�����
       	 if(newNode.getFdbList() == null)
       	 {
       		 try{
       			 //����ֵ�� String[]���͵�List�����е�һ��String[]Ϊ [67108897, 00:0f:e2:49:fc:47]
       			newNode.setFdbList(snmp.getFdbTable(newNode.getIpAddress(), newNode.getCommunity()));
       		 }catch(Exception e){
       			 SysLogger.info("��ȡ�豸"+newNode.getIpAddress()+"��FDB���ݳ���"+e.getMessage()); 
       		 }
       	 }
 		//����CISCO���͵��豸,�õ�CDP����
 		if(newNode.getSysOid().indexOf("1.3.6.1.4.1.9")>=0)
 		{
     		if(newNode.getCdpList() == null || newNode.getCdpList().size()==0)
         		newNode.setCdpList(snmp.getCiscoCDPList(newNode.getIpAddress(),newNode.getCommunity()));
 		}
    }
       	       	
    	
    	if(newNode.getSuperNode()==-1)
    	{
    		newNode.setSuperNode(newNode.getId());
    	}
    	else if(newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)//�Ѹý����豸�ӵ����������ڵ�Ľ����б���
    	{
    		try{
    			getHostByID(newNode.getSuperNode()).addSwitchId(newNode.getId());    		
    		}catch(Exception e){
    			
    		}
    	}
    	SysLogger.info("��ʼ��ȡ�豸:"+newNode.getIpAddress()+"��ϵͳ����");
    	Hashtable sysGroupProperty = snmp.getSysGroup(newNode.getIpAddress(),newNode.getCommunity());
    	if(sysGroupProperty != null)
    	{
        	newNode.setSysDescr((String)sysGroupProperty.get("sysDescr"));
        	//newNode.setsys((String)sysGroupProperty.get("sysUpTime"));
        	newNode.setSysContact((String)sysGroupProperty.get("sysContact"));
        	newNode.setSysName((String)sysGroupProperty.get("sysName"));
        	newNode.setSysLocation((String)sysGroupProperty.get("sysLocation"));
    	}
        	                                    
        //
        existIpList.add(newNode.getIpAddress());  
        
        if(newNode.getCategory()==4||newNode.getCategory()==5||newNode.getCategory()==6) //���ڷ������ʹ�ӡ��,����ǽ,���߽���  
        {
        	newNode.setDiscovered(true);
        	//��ʼ�����豸�ķ���״̬,��Ϊ�·����豸,��Ϊ-1
        	setStatus(newNode);
        	//newNode.setDiscovered(true);
        	//hostList.add(newNode);
        	addHost(newNode);//��ӽ�hostList
        	SysLogger.info(newNode.toString());        	        	        	       
            return;        	
        }
        

        //��ʼ�����豸�ķ���״̬,��Ϊ�·����豸,��Ϊ-1����formerNodeList�����еĶ�����бȽ�
        setStatus(newNode);
        SysLogger.info("�����ӽӵ�"+newNode.getIpAddress()+" Layer:"+newNode.getLayer());
        
        //��Ҫ�жϽڵ��Ƿ����
        //������Ҫ��Ӵ��Ѿ����ڵ��豸�б��л�ȡ�豸,�������Ա����ظ�ȡ�豸����
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&#####"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    		if(tmpNode.getIpAddress().equalsIgnoreCase(newNode.getIpAddress()))//�鿴�Ƿ��Ǵ���ָ��IP�������豸
    		{
    			newNode = tmpNode;
    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
    			flag = 1;
    			break;
    		}
    		else
    		{
    			List aliasIPS = tmpNode.getAliasIPs();
    			if(aliasIPS == null)continue;
    			if(aliasIPS.contains(newNode.getIpAddress()))//�鿴��IP�Ƿ��������豸�ڶ����ڵ�����һ�����ڣ�����ǣ�˵����IP������������豸�Ѿ������ֹ���
    			{
        			newNode = tmpNode;
        			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
        			flag = 1;
        			break;
    			}
    		}
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}  
        
    	//hostList.add(newNode);
    	addHost(newNode);
    	
        //���¶��������豸
        dealLink(newNode,link);//link = null;
        
        SysLogger.info(newNode.toString());
        
        //�Խӿ�list��Ϊ�յ������豸�ٷ���
        if(newNode.getCategory() != 7){
        	if(newNode.getIfEntityList()!=null&&newNode.getIfEntityList().size()!=0)
        		newNode.doDiscover(); 
        }
    }
    
    /**
     * ���豸�����Ѿ�ȷ�����豸��������,deviceType=1,2,3,4,5,7,8 
     */
    public void addHost_SOLO(Host newNode,Link link)
    {
    	/**
    	 * 
    	 * <select size=1 name='type' style='width:100px;' onchange="unionSelect();">
            								<option value='1' selected>·����</option>
            								<option value='2'>·�ɽ�����</option>
            								<option value='3'>������</option>
            								<option value='4'>������</option>
            								<option value='8'>����ǽ</option>
            							</select>
    	 */
    	int flag = 0;
    	if(newNode.getSysOid() == null)
    	{
    		newNode.setDiscovered(true);//�о���������Ϊtrue�����������豸�Ѿ��������ˣ�����Ϊ����������һ��״̬��ʹ���沿�ִ��벻ִ�С�
    		return;
    	}
    	//������Ҫ��Ӵ��Ѿ����ڵ��豸�б��л�ȡ�豸,�������Ա����ظ�ȡ�豸����
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&******"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    		if(tmpNode.getIpAddress().equalsIgnoreCase(newNode.getIpAddress()))//�鿴�Ƿ��Ǵ���ָ��IP�������豸
    		{
    			newNode = tmpNode;
    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
    			flag = 1;
    			break;
    		}
    		else
    		{
    			List aliasIPS = tmpNode.getAliasIPs();
    			if(aliasIPS == null)continue;
    			if(aliasIPS.contains(newNode.getIpAddress()))//�鿴��IP�Ƿ��������豸�ڶ����ڵ�����һ�����ڣ�����ǣ�˵����IP������������豸�Ѿ������ֹ���
    			{
        			newNode = tmpNode;
        			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
        			flag = 1;
        			break;
    			}
    		}
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}
    	
    	
    	SysLogger.info("��ʼ��������Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress());    	
    	//·�ɽ�������㽻��,���������˷���ǽ
    	if(newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)
    	{
    		newNode.setBridgeAddress(snmp.getBridgeAddress(newNode.getIpAddress(),newNode.getCommunity()));//���ø�IP��Ӧ��mac��ַ
    		SysLogger.info("&&&&&&&&&&&&&&&&&&");
    		SysLogger.info(newNode.getIpAddress()+" "+newNode.getBridgeAddress());
    		SysLogger.info("&&&&&&&&&&&&&&&&&&");
       	 	//�õ�STP��������(STPֻ�Զ��㽻��������)
    		if(newNode.getCategory()!=7)
    		{
    			if(newNode.getBridgestpList() == null || newNode.getBridgestpList().size()==0)
    				//snmp.getBridgeStpList(newNode.getIpAddress(),newNode.getCommunity())��õ���com.afunms.discovery.BridgeStpInterface���͵�List
        			//��List��size��getIfEntityList���ص�List��size��С���Բ�һ������ò�������������������������й����ġ�
    				newNode.setBridgestpList(snmp.getBridgeStpList(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    	}
    	//��ȡ���豸�Ľӿڵ�ַ
        if(newNode.getCategory() <5 || newNode.getCategory()==7)
        {
        	//����·�ɺͷ����� 	
        	SysLogger.info("��ʼ��ȡ�豸:"+newNode.getIpAddress()+"�Ľӿڱ�");  
        	if(newNode.getIfEntityList() == null)
        		//snmp.getIfEntityList(newNode.getIpAddress(),newNode.getCommunity(),newNode.getCategory())����com.afunms.discovery.IfEntity���͵�List����IfEntity����ethernet�������Ƕ˿���Ϣ��Ҳ��vlan��˿���Ϣ
        		newNode.setIfEntityList(snmp.getIfEntityList(newNode.getIpAddress(),newNode.getCommunity(),newNode.getCategory()));
        }
        
        if(newNode.getSysOid() != null )
        {
            if(newNode.getSysOid().indexOf("1.3.6.1.4.1.25506")>=0 &&(newNode.getCategory()==2 || newNode.getCategory()==3))
            {
            	//H3C�Ľ����豸
            	if(newNode.getNdpHash() == null)
            		//���ص�hashtable������ key ����mac��ַ��value ����mac��ַ��Ӧ�Ķ˿ڵ����֣���ethernet1/1/1
            		newNode.setNdpHash(snmp.getNDPTable(newNode.getIpAddress(),newNode.getCommunity()));
            }
        }
        
    	//��һ���豸�Ѿ�����,�Ƿ���Է����ڶ���????
        SysLogger.info("��ʼ�ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"=======�Ƿ��Ѿ�����"); 
        
       
        for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&@@@@@"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}
        
        
        
        
        
        if(link != null)
        	SysLogger.info(newNode.getIpAddress()+" "+newNode.getAdminIp()+"   link:startip: "+link.getStartIp()+" endip:"+link.getEndIp());
    	//�÷�����Ȼ����link���󣬵�ȴδʹ�õ��ö��󣬴˷���ֻ�Ǽ���豸�б����Ƿ��Ѿ�������newNode�ڵ㣬ͨ���Ƚ��豸IP��ַ���
//        if(isHostExist(newNode))
//        {
//    		SysLogger.info("�����ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"�Ƿ��Ѿ�����,��ǰΪ�Ѿ����ڲ�����");
//    		return;
//    	}
    	SysLogger.info("�����ж�����Ϊ"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+"======�Ƿ��Ѿ�����");
    	
    	newNode.setId(KeyGenerator.getInstance().getHostKey());
    	//���������豸
    	if(newNode.getCategory()==1 || newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)
    	{
    		if(newNode.getIpNetTable() == null || newNode.getIpNetTable().size()==0)
    		{
    			//����com.afunms.discovery.IpAddress���͵�List��
    			newNode.setIpNetTable(snmp.getIpNetToMediaTable(newNode.getIpAddress(),newNode.getCommunity()));//�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip
    		}
    		if(newNode.getPortMacs() == null || newNode.getPortMacs().size()==0)
    		{
    			//����hashtable��50=[00:0f:e2:49:fc:47, 00:0f:e2:49:fd:fa]����������һ���ļ�ֵ��
    			newNode.setPortMacs(snmp.getDtpFdbTable(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		SysLogger.info(newNode.getIpAddress()+" ################# PortMacs size "+newNode.getPortMacs().size());
    		if(newNode.getAdminIp() == null)
    		{
    			newNode.setAtInterfaces(snmp.getAtInterfaceTable(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		if(newNode.getBridgeIdentifiers() == null){
    			newNode.addBridgeIdentifier(snmp.getBridgeAddress(newNode.getIpAddress(),newNode.getCommunity()));
    		}
    		//����fdb�ӿ�����
       	 if(newNode.getFdbList() == null)
       	 {
       		 try{
       			 //����ֵ�� String[]���͵�List�����е�һ��String[]Ϊ [67108897, 00:0f:e2:49:fc:47]
       			newNode.setFdbList(snmp.getFdbTable(newNode.getIpAddress(), newNode.getCommunity()));
       		 }catch(Exception e){
       			 SysLogger.info("��ȡ�豸"+newNode.getIpAddress()+"��FDB���ݳ���"+e.getMessage()); 
       		 }
       	 }
 		//����CISCO���͵��豸,�õ�CDP����
 		if(newNode.getSysOid().indexOf("1.3.6.1.4.1.9")>=0)
 		{
     		if(newNode.getCdpList() == null || newNode.getCdpList().size()==0)
         		newNode.setCdpList(snmp.getCiscoCDPList(newNode.getIpAddress(),newNode.getCommunity()));
 		}
    }
       	       	
    	
    	if(newNode.getSuperNode()==-1)
    	{
    		newNode.setSuperNode(newNode.getId());
    	}
    	else if(newNode.getCategory()==2 || newNode.getCategory()==3 || newNode.getCategory()==7 || newNode.getCategory()==8)//�Ѹý����豸�ӵ����������ڵ�Ľ����б���
    	{
    		try{
    			getHostByID(newNode.getSuperNode()).addSwitchId(newNode.getId());    		
    		}catch(Exception e){
    			
    		}
    	}
    	SysLogger.info("��ʼ��ȡ�豸:"+newNode.getIpAddress()+"��ϵͳ����");
    	Hashtable sysGroupProperty = snmp.getSysGroup(newNode.getIpAddress(),newNode.getCommunity());
    	if(sysGroupProperty != null)
    	{
        	newNode.setSysDescr((String)sysGroupProperty.get("sysDescr"));
        	//newNode.setsys((String)sysGroupProperty.get("sysUpTime"));
        	newNode.setSysContact((String)sysGroupProperty.get("sysContact"));
        	newNode.setSysName((String)sysGroupProperty.get("sysName"));
        	newNode.setSysLocation((String)sysGroupProperty.get("sysLocation"));
    	}
        	                                    
        //
        existIpList.add(newNode.getIpAddress());  
        
        if(newNode.getCategory()==4||newNode.getCategory()==5||newNode.getCategory()==6) //���ڷ������ʹ�ӡ��,����ǽ,���߽���  
        {
        	newNode.setDiscovered(true);
        	//��ʼ�����豸�ķ���״̬,��Ϊ�·����豸,��Ϊ-1
        	setStatus(newNode);
        	//newNode.setDiscovered(true);
        	//hostList.add(newNode);
        	addHost(newNode);//��ӽ�hostList
        	SysLogger.info(newNode.toString());        	        	        	       
            return;        	
        }
        

        //��ʼ�����豸�ķ���״̬,��Ϊ�·����豸,��Ϊ-1����formerNodeList�����еĶ�����бȽ�
        setStatus(newNode);
        SysLogger.info("�����ӽӵ�"+newNode.getIpAddress()+" Layer:"+newNode.getLayer());
        
        //��Ҫ�жϽڵ��Ƿ����
        //������Ҫ��Ӵ��Ѿ����ڵ��豸�б��л�ȡ�豸,�������Ա����ظ�ȡ�豸����
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&&&&#####"+newNode.getIpAddress()+"----BRIDGE  "+newNode.getBridgeAddress()+"======="+tmpNode.getIpAddress()+"===BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(newNode.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+newNode.getCategory()+"���豸"+newNode.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    		if(tmpNode.getIpAddress().equalsIgnoreCase(newNode.getIpAddress()))//�鿴�Ƿ��Ǵ���ָ��IP�������豸
    		{
    			newNode = tmpNode;
    			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
    			flag = 1;
    			break;
    		}
    		else
    		{
    			List aliasIPS = tmpNode.getAliasIPs();
    			if(aliasIPS == null)continue;
    			if(aliasIPS.contains(newNode.getIpAddress()))//�鿴��IP�Ƿ��������豸�ڶ����ڵ�����һ�����ڣ�����ǣ�˵����IP������������豸�Ѿ������ֹ���
    			{
        			newNode = tmpNode;
        			SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+newNode.getCategory()+"���豸:"+newNode.getIpAddress()+",���б��еõ����豸");
        			flag = 1;
        			break;
    			}
    		}
    	}
    	if(flag == 1)//�����豸�Ѿ������ֹ�һ��
    	{
    		newNode.setDiscovered(true);
    		return;
    	}  
    	addHost(newNode);
    	
        //���¶��������豸
        dealLink(newNode,link);//link = null;
        
        SysLogger.info(newNode.toString());
    }
    
    /**
     * ����һ������ 
     */
    public synchronized void addSubNet(SubNet subnet)
    {
    	//�ж��Ƿ��Ѿ�����
    	if(subNetList.contains(subnet)) return;
    	
    	int id = KeyGenerator.getInstance().getSubNetKey();
    	subnet.setId(id);
    	subNetList.add(subnet);
    }
     
    /**
     * �ڷ����꽻������ֱ����ϵ�󣬰�����·����
     */
    public synchronized void addLinks(List temporaryLinks)
    {
    	if(temporaryLinks==null) return;
    	
    	for(int i=0;i<temporaryLinks.size();i++)
    	{
    		TemporaryLink templink = (TemporaryLink)temporaryLinks.get(i);
    		if(templink.isDel()) continue;
    		
    		Host startNode = getHostByID(templink.getStart().getId());
    		IfEntity if1 = startNode.getIfEntityByIndex(templink.getStart().getIfIndex());
            if(if1==null) continue;
            
            if(if1.getDescr() != null){
            	String descr = if1.getDescr();
            	if(descr.indexOf("GigabitEthernet")>=0){
            		String allchassis = descr.substring(descr.lastIndexOf("t"));
            		String[] chassis = allchassis.split("/");
            		if(chassis.length>1){
            			String printstr = "";
            			for(int k=0;k<chassis.length;k++){
            				printstr=printstr+"=="+chassis[k];
            			}
            			System.out.println(printstr);
            		}
            		System.out.println(descr.substring(descr.lastIndexOf("t"))); 
            	}else if(descr.indexOf("Ethernet")>=0){
            		
            	}
            }
    		
    		Host endNode = getHostByID(templink.getEnd().getId());
    		IfEntity if2 = endNode.getIfEntityByIndex(templink.getEnd().getIfIndex());
    		if(if2==null) continue;
    		
    		Link link = new Link();
    		link.setStartId(templink.getStart().getId());
    		link.setStartIndex(if1.getIndex());
    		if(if1.getIpAddress().equals(""))
    		   link.setStartIp(startNode.getIpAddress());	
    		else    			
    		   link.setStartIp(if1.getIpAddress());
    		link.setStartPhysAddress(if1.getPhysAddress());
    		link.setStartPort(if1.getPort());
    		link.setStartDescr(if1.getDescr());

    		link.setEndId(templink.getEnd().getId());
    		link.setEndIndex(if2.getIndex());
    		link.setEndIp(if2.getIpAddress());
    		if(if2.getIpAddress().equals(""))
     		   link.setEndIp(endNode.getIpAddress());	
     		else    			
     		   link.setEndIp(if2.getIpAddress());    		
    		link.setEndPhysAddress(if2.getPhysAddress());
    		link.setEndPort(if2.getPort());
    		link.setEndDescr(if2.getDescr());
    		
    		if(linkList.contains(link)) continue;    		
    		for(int j=0;j<linkList.size();j++)
    		{
    			Link tempLink = (Link)linkList.get(j);
    			
    			if(tempLink.getStartId()==link.getStartId() 
    			   && tempLink.getEndId()==link.getEndId())
    			{
    				System.out.println("Temp˫��·:" + link.getStartIp() + "|" + link.getStartIndex() + "<--->" + link.getEndIp() + "|" + link.getEndIndex());
    				System.out.println("��" + tempLink.getStartIp() + "|" + tempLink.getStartIndex() + "<--->" + tempLink.getEndIp() + "|" + tempLink.getEndIndex());
                    link.setAssistant(1);				
    				break;
    			} 	
    		}       		
    		linkList.add(link);
    	}    		
    }
    
    private synchronized void addHost(Host node){
    	int flag = 0;
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		//SysLogger.info("&&**"+node.getIpAddress()+"--BRIDGE  "+node.getBridgeAddress()+"   "+tmpNode.getIpAddress()+"  BRIDGE "+tmpNode.getBridgeAddress());
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(node.getBridgeAddress()))	
		    {
		    	SysLogger.info("����Ϊ"+node.getCategory()+"���豸"+node.getIpAddress()+"�Ѿ�����");
		    	//exist = true;
		    	flag = 1;
		    	break;	
		    }
    		if(tmpNode.getIpAddress().equalsIgnoreCase(node.getIpAddress())){
    			flag = 1;
    			break;
    		}
    		//if(tmpNode.getCategory()==5 || tmpNode.getCategory()==6 ) continue; //��ӡ�������ǽ
    	}
    	if(flag == 0){
    		hostList.add(node);
        	//���豸�������ݿ�,ͬʱ������·��ϵ
        	DiscoverCompleteDao nodeDao = new DiscoverCompleteDao();
        	List tempList = new ArrayList();
        	tempList.add(node);
        	try{
        		nodeDao.addHostData(tempList);
        	}catch(Exception e){
        		
        	}finally{
        		nodeDao.close();
        	}
    	}

    	
    }
    /**
     * ȷ��һ���豸�Ƿ��Ѿ�����
     * ��ǰLinkֻ����ʼIP����ʼ�˿�����ID
     */
    private boolean isHostExist(Host node)
    {
    	boolean exist = false;
    	int flag = 0;
    	List node_ipalias = node.getAliasIPs();
    	for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		
    		if(tmpNode.getCategory()==5 || tmpNode.getCategory()==6 ) continue; //��ӡ�������ǽ
    		
    		//SysLogger.info("&&&&&******"+node.getIpAddress()+"----Bridge  "+node.getBridgeAddress()+"   "+tmpNode.getIpAddress()+"===Bridge:"+tmpNode.getBridgeAddress());
    		
		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(node.getBridgeAddress()))	
		    {
		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
		    	SysLogger.info("����Ϊ"+node.getCategory()+"���豸"+node.getIpAddress()+"�Ѿ�����");
		    	exist = true;
		    	flag = 1;
		    	break;	
		    }
    		//if(flag == 1)return exist;
    		
    		if(tmpNode.getIpAddress().equalsIgnoreCase(node.getIpAddress()))
			{
				//existHost = tmpNode;
				SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+node.getIpAddress());	
				exist = true;
				break;
			}else{
				//�жϱ���IP�Ƿ����
				List aliasIPs = tmpNode.getAliasIPs();
				if(aliasIPs != null && aliasIPs.size()>0){
					if(aliasIPs.contains(node.getIpAddress())){
						SysLogger.info("�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+"���豸:"+node.getIpAddress());
						exist = true;
						break;
					}
					for(int k=0;k<aliasIPs.size();k++){
						String temp_ip = (String)aliasIPs.get(k);						
						if(node_ipalias != null && node_ipalias.size()>0){
							for(int m=0;m<node_ipalias.size();m++){								
								if(temp_ip.equalsIgnoreCase((String)node_ipalias.get(m))){
									//SysLogger.info("@*"+node.getIpAddress()+"-����  "+node_ipalias.get(m)+"="+tmpNode.getIpAddress()+"=temp_ip:"+temp_ip+" nodeBRIDGE:"+node.getBridgeAddress()+" tmpNodeBRIDGE:"+tmpNode.getBridgeAddress());
									//ͬʱ�ж�BRIDGE
									
									
					    		    if(tmpNode.getBridgeAddress()!=null && tmpNode.getBridgeAddress().equalsIgnoreCase(node.getBridgeAddress()))	
					    		    {
					    		    	//SysLogger.info("&&&&&******"+node.getIpAddress()+"----����  "+node_ipalias.get(m)+"======="+tmpNode.getIpAddress()+"===temp_ip:"+temp_ip);
					    		    	SysLogger.info("����Ϊ"+node.getCategory()+"���豸"+node.getIpAddress()+"�Ѿ�����");
					    		    	exist = true;
					    		    	flag = 1;
					    		    	break;	
					    		    }
								}
							}
							if(flag == 1)break;
							
//							if(node_ipalias.contains(temp_ip)){
//								SysLogger.info("###-----�ѷ��ֵ��豸�б����Ѿ�����"+tmpNode.getCategory()+" "+tmpNode.getIpAddress()+" ���豸:"+node.getIpAddress());
//								exist = true;
//								flag = 1;
//								break;
//							}
						}	
					}
					if(flag == 1)break;
				}
			}
    		
    	}
    	SysLogger.info("����Ϊ"+node.getCategory()+"���豸"+node.getIpAddress()+"Ŀǰ����Ϊ--"+exist);
    	  	
       	return exist;
    } 
    
    public boolean isDiscovered()
    {
    	/*
    	SysLogger.info("############��ǰ��"+threads+"���߳�������##############"+"�Ѿ�����"+discoverdcount+"���߳�");
    	//Runnable test = (Runnable)threadList.get(0);
    	if(this.discoverdcount == this.threads){
    		//threadPool.join();
    		//threadPool.interrupt();
    		//threadPool.destroy();
    		return true;
    	}else
    		return false;
    	*/
    	
    	
    	boolean finish = true;
    	for(int i=0;i<threadList.size();i++)
    	{
    		BaseThread bt = (BaseThread)threadList.get(i); 
    		if(!bt.isCompleted())
    	    {
    			SysLogger.info(((BaseThread)threadList.get(i)).getName() + "(" + ((BaseThread)threadList.get(i)).getClass().getName() + ") has not complete");
    			finish = false;
    	        break;
    	    }
    		else
    		{
    			threadList.remove(bt);//gzm
    	    	bt = null;
    	    }
    	}
    	if(finish)
    	{
    		SysLogger.info("----�������----");
    		//threadExecutor.notifyAll();
    		threadExecutor.shutdown();
			if (!threadExecutor.isShutdown()) 
			{

				List<Runnable> threads = threadExecutor.shutdownNow();

				for (int i = 0; i < threads.size(); ++i) {
					try {
						((Thread)threads.get(i)).interrupt();
					} catch (Exception e) {

					}
				}
			}	
    	}	
    	return finish;
    	
    }
    	    
    /**
     * nodeΪ��·���յ�
     */
	private void dealLink(Host node,Link link)
	{        
		if(link==null) return;
		if(link.getStartId() == link.getEndId())return;
		//���˵��˿ھۺϵ����(�߼����ӹ�ϵ)
		if(link.getStartDescr().toLowerCase().indexOf("channel")>=0) return;
		Hashtable hostidHash = new Hashtable();
		for(int i=0;i<hostList.size();i++)
    	{
    		Host tmpNode = (Host)hostList.get(i);
    		hostidHash.put(tmpNode.getId()+"", tmpNode.getId()+"");
    	}
		if( !hostidHash.containsKey(node.getId()+"") || !hostidHash.containsKey(link.getStartIndex()+""))return;
		//hostList
		
		
		//���⴦��RouterLink
		if(link.getFindtype() == SystemConstant.ISRouter){
			//·�ɱ��õ�����
		    IfEntity ipObj = node.getIfEntityByIP(link.getEndIp());
		    //if(ipObj==null)ipObj = node.getIfEntityByIP(link.getStartIp());
	        if(ipObj==null)	{ 
	        	SysLogger.info("======================");
	        	SysLogger.info(node.getIpAddress()+"�豸��,�Ҳ���"+link.getEndIp()+"��"+link.getStartIp()+"��Ӧ�Ľӿ�,����");
	        	SysLogger.info("======================");
	        	return;				
	        }
			link.setEndDescr(ipObj.getDescr());
			link.setEndIndex(ipObj.getIndex());
			link.setEndPort(ipObj.getIndex());	
			link.setEndPhysAddress(ipObj.getPhysAddress());        	
			link.setEndId(node.getId());
			//link.setFindtype(5);//MAC����
			link.setLinktype(SystemConstant.LOGICALLINK);//��������
			addRouteLink(routelinkList,link);//��鲢������ӹ�ϵ
			//checkAssistantLink(linkList,link,1);
		}else if(link.getFindtype() == SystemConstant.ISMac){
			//MAC��ַת�����õ�����
		    IfEntity ipObj = node.getIfEntityByIP(link.getEndIp());
	        if(ipObj==null)	{
	        	SysLogger.info("======================");
	        	SysLogger.info(node.getIpAddress()+"�豸��,�Ҳ���"+link.getEndIp()+"��"+link.getStartIp()+"��Ӧ�Ľӿ�,����");
	        	SysLogger.info("======================");
	        	return;				
	        }
			link.setEndDescr(ipObj.getDescr());
			link.setEndIndex(ipObj.getIndex());
			link.setEndPort(ipObj.getIndex());	
			link.setEndPhysAddress(ipObj.getPhysAddress());        	
			link.setEndId(node.getId());
			link.setLinktype(SystemConstant.LOGICALLINK);//�߼�����
			addMacLink(maclinkList,link);//��鲢������ӹ�ϵ
		}
	}
	
	public synchronized void addLink(List linkList,Link link){
		if(linkList != null && linkList.size()>0){
			List existList = new ArrayList();
			int existFlag = 0;
			if(link.getLinktype() != -1){
				//���ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() == -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}else{
				//ֻ�ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() != -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}

			if(existFlag == 0){
				linkList.add(link);
				SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
			}
		}else{
			linkList.add(link);
			SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
		}
		
	}
	
	public synchronized void addRouteLink(List linkList,Link link){
		if(linkList != null && linkList.size()>0){
			List existList = new ArrayList();
			int existFlag = 0;
			if(link.getLinktype() != -1){
				//���ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() == -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}else{
				//ֻ�ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() != -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}

			if(existFlag == 0){
				linkList.add(link);
				SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
			}
		}else{
			linkList.add(link);
			SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
		}
		
	}
	
	public synchronized void addMacLink(List linkList,Link link){
		if(linkList != null && linkList.size()>0){
			List existList = new ArrayList();
			int existFlag = 0;
			if(link.getLinktype() != -1){
				//���ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() == -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}else{
				//ֻ�ж�VLAN���ӹ�ϵ
				for(int i=0;i<linkList.size();i++){
					Link tempLink = (Link)linkList.get(i);
					if(tempLink.getLinktype() != -1)continue;
					if(isSameLink(link,tempLink)){
						//�Ѿ����ڸ�����
						existFlag = 1;
						break;
						//existList.add(tempLink);
					}
				}
			}

			if(existFlag == 0){
				linkList.add(link);
				SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
			}
		}else{
			linkList.add(link);
			SysLogger.info("����������·��ϵ "+link.getStartIp()+ "|"+link.getStartIndex()+"<--->" + link.getEndIp() + "|" + link.getEndIndex());
		}
		
	}
	/*
	 * �ж�˫��·��ϵ
	 */
	public synchronized void checkAssistantLink(List linkList,Link link,int linktype){
		if(linkList != null && linkList.size()>0){
			for(int i=0;i<linkList.size();i++)
			{
				Link tempLink = (Link)linkList.get(i);
				if(linktype != -1){
					if(tempLink.getLinktype() < 0 )continue;
				}else{
					if(tempLink.getLinktype() != linktype)continue;
				}
				
				if((tempLink.getStartId()==link.getStartId() 
				   && tempLink.getEndId()==link.getEndId())||
				   (tempLink.getStartId()==link.getEndId() 
						   && tempLink.getEndId()==link.getStartId()))
				{
					if((link.getStartIp().equalsIgnoreCase(tempLink.getStartIp())
							&& link.getStartIndex().equalsIgnoreCase(tempLink.getStartIndex())) ||
							(link.getEndIp().equalsIgnoreCase(tempLink.getEndIp())
									&& link.getEndIndex().equalsIgnoreCase(tempLink.getEndIndex()))
							) continue;
					
					SysLogger.info("======================");
					SysLogger.info("dealLink�ҵ�˫��·:" + link.getStartIp() + "|" + link.getStartIndex() + "<--->" + link.getEndIp() + "|" + link.getEndIndex());
					SysLogger.info("��" + tempLink.getStartIp() + "|" + tempLink.getStartIndex() + "<--->" + tempLink.getEndIp() + "|" + tempLink.getEndIndex());
	                link.setAssistant(1);				
					break;
				} 	
			}
		}
	}
	
	/*
	 * �ж����������Ƿ���ͬ
	 * @param (Link)sourceLink 
	 * @param (Link)destLink 
	 */
	public boolean isSameLink(Link sourceLink,Link destLink){
		if((destLink.getStartIp().equalsIgnoreCase(sourceLink.getStartIp())&&destLink.getStartIndex().equalsIgnoreCase(sourceLink.getStartIndex())&&destLink.getEndIp().equalsIgnoreCase(sourceLink.getEndIp()) && destLink.getEndIndex().equalsIgnoreCase(sourceLink.getEndIndex()))
			||(destLink.getStartIp().equalsIgnoreCase(sourceLink.getEndIp())&&destLink.getStartIndex().equalsIgnoreCase(sourceLink.getEndIndex())&&destLink.getEndIp().equalsIgnoreCase(sourceLink.getStartIp()) && destLink.getEndIndex().equalsIgnoreCase(sourceLink.getStartIndex())))
			return true;
		return false;
	}
	
	/**
	 * �ҵ������豸�Ĺ����ַ
	 */
	private void findManageAddress(Host node)
	{
		if(node.getIfEntityList()==null) return;
		
		List ifList = node.getIfEntityList();
		for(int i=0;i<ifList.size();i++)
		{
			IfEntity ifObj = (IfEntity)ifList.get(i);
			if(ifObj.getType()==24&&!"".equals(ifObj.getIpAddress())&&!"127.0.0.1".equals(ifObj.getIpAddress()))
			{
				SysLogger.info(node.getIpAddress() + " �Ĺ����ַ�� " + ifObj.getIpAddress());
				node.setIpAddress(ifObj.getIpAddress());				
				break;
			}
		}
	}
	
	/**
	 * �����豸�ķ���״̬
	 */
	private void setStatus(Host node)
	{
		//�õ��ϴ��Ѿ����ֹ����豸
		List formerNodeList = DiscoverEngine.getInstance().getFormerNodeList();
		if(formerNodeList != null && formerNodeList.size()>0){
			int flag = 0;
			for(int k=0;k<formerNodeList.size();k++){
				   Host formernode = (Host)formerNodeList.get(k);
				   //if(formernode.getIpAddress().equals(node.getIpAddress())&& formernode.getBridgeAddress().equals(node.getBridgeAddress())){
				   if(formernode.getIpAddress().equals(node.getIpAddress())){
					   int status = formernode.getDiscoverstatus();
					   if(status == -1){
						   node.setDiscoverstatus(status+1);
					   }else if(status > 0){
						   node.setDiscoverstatus(-2);
					   }
					   flag = 1;
					   formerNodeList.remove(k);//ɾ�����豸
					   break;
				   }
			}
			if(flag == 0){
				//�·��ֵ��豸
				node.setDiscoverstatus(-1);
			}
		}else{
			//��һ���Զ�����
			node.setDiscoverstatus(-1);
		}
	}
	
	public synchronized Host getHostByID(int id)
	{
		Host host = null;
		for(int i=0;i<hostList.size();i++)
		{
			SysLogger.info("�����豸"+((Host)hostList.get(i)).getIpAddress()+" ID:"+((Host)hostList.get(i)).getId()+" id:"+id);
			if(((Host)hostList.get(i)).getId()==id)
			{
			    host = (Host)hostList.get(i);
			    break;
			}			    	
		}
		return host;
	}
	
	public synchronized Host getHostByIP(String ip)
	{
		Host host = null;
		for(int i=0;i<hostList.size();i++)
		{
			//SysLogger.info("IP:"+ip+"-------host:"+((Host)hostList.get(i)).getIpAddress());			
			if(((Host)hostList.get(i)).getIpAddress().equalsIgnoreCase(ip))
			{
			    host = (Host)hostList.get(i);
			    break;
			}			    	
		}
		return host;
	}
	
	public synchronized Host getHostByAliasIP(String ip)
	{
		Host host = null;
		for(int i=0;i<hostList.size();i++)
		{
			if(((Host)hostList.get(i)).getAliasIPs() == null) continue;
			if(((Host)hostList.get(i)).getAliasIPs().contains(ip))
			{
			    host = (Host)hostList.get(i);
			    break;
			}			    	
		}
		return host;
	}
	
    public List getThreadList()
    {
       return threadList;	
    }
    public List getExistIpList()
    {
        return existIpList;
    }
    
    public List getSubNetList()
    {
       return subNetList;	
    }

    public List getHostList()
    {
       return hostList;	
    }
    
    public int getSnmpversion()
    {
       return snmpversion;	
    }
    public void setSnmpversion(int snmpversion)
    {
       this.snmpversion = snmpversion;	
    }
    
    public int getDiscovermodel()
    {
       return discovermodel;	
    }
    public void setDiscovermodel(int discovermodel)
    {
       this.discovermodel = discovermodel;	
    }
    
    public String getWritecommunity()
    {
       return writecommunity;	
    }
    public void setWritecommunity(String writecommunity)
    {
       this.writecommunity = writecommunity;	
    }
    
    public String getDiscover_bid()
    {
       return discover_bid;	
    }
    public void setDiscover_bid(String discover_bid)
    {
       this.discover_bid = discover_bid;	
    }
    
    public List getFaildIpList()
    {
       return faildIpList;	
    }

    public List getLinkList()
    {    	
    	return linkList;
    }
    public List getRouteLinkList()
    {    	
    	return routelinkList;
    }
    public List getMacLinkList()
    {    	
    	return maclinkList;
    }
    public List getFormerNodeList()
    {    	
    	return formerNodeList;
    }
    public void setFormerNodeList(List formerNodeList)
    {    	
    	this.formerNodeList = formerNodeList;
    }
    
    public List getFormerNodeLinkList()
    {    	
    	return formerNodeLinkList;
    }
    public void setFormerNodeLinkList(List formerNodeLinkList)
    {    	
    	this.formerNodeLinkList = formerNodeLinkList;
    }
    
    
    
	/**
	 * ��һ���ַ�����ʽ��ip��ַת����һ��������������ǷǷ����ݣ��򷵻�0
	 * 
	 * @param ip
	 * @return
	 */
	static public long ip2long(String ip) {
		long result = 0;
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				result = result * 256 + part;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
	public static int getStopStatus() {
		return stopStatus;
	}
	public static void setStopStatus(int stopStatus) {
		DiscoverEngine.stopStatus = stopStatus;
	}
}
