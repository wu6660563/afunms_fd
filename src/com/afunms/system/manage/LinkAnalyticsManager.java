package com.afunms.system.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.LinkSnmpUtil;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.discovery.AtInterface;
import com.afunms.discovery.BridgeStpInterface;
import com.afunms.discovery.CdpCachEntryInterface;
import com.afunms.discovery.DiscoverComplete_SOLO;
import com.afunms.discovery.DiscoverDataHelper;
import com.afunms.discovery.DiscoverEngine;
import com.afunms.discovery.DiscoverInitialize;
import com.afunms.discovery.DiscoverMonitor;
import com.afunms.discovery.DiscoverResource;
import com.afunms.discovery.Host;
import com.afunms.discovery.IPNetProbeThread_SOLO_Runable;
import com.afunms.discovery.IPRouterProbeThread_SOLO_Runable;
import com.afunms.discovery.IfEntity;
import com.afunms.discovery.IpAddress;
import com.afunms.discovery.IpRouter;
import com.afunms.discovery.Link;
import com.afunms.discovery.MacToNodeLink;
import com.afunms.discovery.NodeToNodeLink;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.task.ThreadPool;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;

public class LinkAnalyticsManager extends BaseManager implements ManagerInterface{
	
	   private Snmp snmp = null;
       private Address targetAddress = null;
       
       private static List hostlist = new ArrayList();
       
       private static List linkList = new ArrayList();
       
   	private List<NodeToNodeLink> links = new ArrayList<NodeToNodeLink>();

	private List<MacToNodeLink> maclinks = new ArrayList<MacToNodeLink>();

	private HashMap<Integer,Host> bridgeNodes = new HashMap<Integer,Host>();

	private List<Host> routerNodes = new ArrayList<Host>();

	private List<Host> cdpNodes = new ArrayList<Host>();
	
	private List<Host> ndpNodes = new ArrayList<Host>();
	
	private List<Host> atNodes = new ArrayList<Host>();

	// this is the list of mac address just parsed by discovery process
	private List<String> macsParsed = new ArrayList<String>();
	
	// this is the list of mac address excluded by discovery process
	private List<String> macsExcluded = new ArrayList<String>();
	
	private static List routelinkList = new ArrayList();
	private static List route_linkList = new ArrayList();
	private static List maclinkList = new ArrayList();

	// this is tha list of atinterfaces for which to be discovery link
	// here there aren't the bridge identifier becouse they should be discovered
	// by main processes. This is used by addlinks method.
	private Map<String,List<AtInterface>> macToAtinterface = new HashMap<String,List<AtInterface>>();
   	
       public void initComm(String ip) throws IOException {

              // ����Agent����IP�Ͷ˿�

              targetAddress = GenericAddress.parse("udp:"+ip+"/161");

              TransportMapping transport = new DefaultUdpTransportMapping();

              snmp = new Snmp(transport);

              transport.listen();

       }
       public int sendPDU(String name,int version) throws IOException {
    	   
    	   int flag = 1;
    	      String snmpping=null;

              // ���� target
             
              CommunityTarget target = new CommunityTarget();

              target.setCommunity(new OctetString(name));

              target.setAddress(targetAddress);

              // ͨ�Ų��ɹ�ʱ�����Դ���

              target.setRetries(2);

              // ��ʱʱ��

              target.setTimeout(1500);

              target.setVersion(version);

              // ���� PDU

              PDU pdu = new PDU();

              pdu.add(new VariableBinding(new OID(new int[] { 1, 3, 6, 1, 2, 1, 1, 1, 0 })));

              // MIB�ķ��ʷ�ʽ

              pdu.setType(PDU.GET);

              // ��Agent����PDU��������Response

              ResponseEvent respEvnt = snmp.send(pdu, target);

              // ����Response

              if (respEvnt != null && respEvnt.getResponse() != null) {
            	  snmpping="SNMP����������...";
                     Vector<VariableBinding> recVBs = respEvnt.getResponse()

                                   .getVariableBindings();

                     for (int i = 0; i < recVBs.size(); i++) {

                            VariableBinding recVB = recVBs.elementAt(i);

                            String snmp=recVB.getOid() + " : " + recVB.getVariable();
                     }

              }else
              {
            	  snmpping="SNMP����δ������";
            	  flag = 0;
              }
              return flag;
        
       }
       
       public Hashtable linkana(String ip1,String commu1,int version1,String ip2,String commu2,int version2){
    	   SysLogger.info("########## ��ʼ������·��ϵ ##########");
    	   Hashtable returnHash = new Hashtable();
    	   
    	   // �����̳߳�,��Ŀ���ݱ����ӵ��豸����
   		   ThreadPool threadPool = new ThreadPool(2);
   		   // ��������
   		   String ip = "";
   		   int version = 0;
   		   String commu = "";
   		   for(int i=0;i<2;i++){
   			   if(i==0){
   				   ip = ip1;
   				   version = version1;
   				   commu = commu1;
   			   }else if(i==1){
   				   ip = ip2;
   				   version = version2;
   				   commu = commu2;
   			   }
   			   threadPool.runTask(createTask(i+1,ip,version,commu));
   		   }
   		   // �ر��̳߳ز��ȴ������������
   		   threadPool.join();
   		   threadPool.close();
   		   threadPool = null;
   		   Host host1 = (Host)hostlist.get(0);
   		   Host host2 = (Host)hostlist.get(1);
     	   
     	   
     	   //����CDPЭ�����
     	   if(host1.getCdpList() != null && host1.getCdpList().size()>0 &&
     			  host2.getCdpList() != null && host2.getCdpList().size()>0 ){
  				try{
  					int curCdpNodeId = host1.getId();
  					String curCdpIpAddr = host1.getAdminIp();
  					List executedPort = new ArrayList();
  					Iterator<CdpCachEntryInterface> sub_ite = host1.getCdpList().iterator();
  					while (sub_ite.hasNext()) {
  						try{
  							CdpCachEntryInterface cdpIface = sub_ite.next();
  							//SysLogger.info(host.getIpAddress()+" remoteip:"+cdpIface.getIp()+" remoteDesc:"+cdpIface.getPortdesc());
  							String targetIpAddr = cdpIface.getIp();
  							//�ж�Host2���Ƿ���ڸ�IP
  							Host targetHost = null;
  							if(host2.getAliasIPs().contains(targetIpAddr)){
  								targetHost = host2;
  							}else{
  								continue;
  							}
  							//��ȡcdpIfIndex
  							int cdpIfIndex = -1;
  							if(targetHost.getCdpList() != null && targetHost.getCdpList().size()>0){
  								Iterator<CdpCachEntryInterface> target_ite = targetHost.getCdpList().iterator();
  								while (target_ite.hasNext()) {
  									CdpCachEntryInterface targetcdpIface = target_ite.next();
  									//SysLogger.info(host.getIpAddress()+" startip:"+targetcdpIface.getIp()+" startDesc:"+targetcdpIface.getPortdesc());
  									if(host1.getAliasIPs().contains(targetcdpIface.getIp())){
  										//��Ҫ���統ǰPORTDESC�Ƿ��Ѿ��������������
  									if(executedPort.contains(targetcdpIface.getPortdesc()))continue;
  									if(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()) != null){
  										cdpIfIndex = Integer.parseInt(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()).getIndex());
  										executedPort.add(targetcdpIface.getPortdesc());
  										break;
  									}	
  									}
  								}
  							}
  							int targetCdpNodeId = targetHost.getId();
  							if (cdpIfIndex <= 0) {
  								//���߼��˿ڴ���
  								cdpIfIndex = Integer.parseInt(host1.getIfEntityByIP(host1.getIpAddress()).getIndex());
  								SysLogger.info("���ǺϷ���CDP IfIndex�����߼��˿ڴ���");
  								//continue;
  							}else{
  								SysLogger.info("���ֺϷ��� CDP ifindex " + cdpIfIndex);
  							}
  							int cdpDestIfindex = -1;
  							if(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()) != null){
  								cdpDestIfindex = Integer.parseInt(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()).getIndex());
  								if(cdpDestIfindex < 0){
  									SysLogger.info("���У����Ϸ���CDP destination IfIndex "+cdpDestIfindex+". ����");
  									continue;
  								}
  							}else{
  								SysLogger.info("���У����Ϸ���CDP destination. ����");
  								continue;
  							}
  							SysLogger.info("���У� ���� CDP target ifindex " + cdpDestIfindex);

  							SysLogger.info("����: ���� CDP link: nodeid=" + curCdpNodeId
  								+ " ifindex=" + cdpIfIndex + " nodeparentid="
  								+ targetCdpNodeId + " parentifindex="
  								+ cdpDestIfindex);	
  							boolean add = false;
  							// now add the cdp link
  							SysLogger.info("����: no node is bridge node! Adding CDP link");	
  							
  							add = true;
  							if (add) {
  								NodeToNodeLink lk = new NodeToNodeLink(targetCdpNodeId,cdpDestIfindex);
  								lk.setFindtype(SystemConstant.ISCDP);
  								lk.setNodeparentid(curCdpNodeId);
  								lk.setParentifindex(cdpIfIndex);
  								addNodetoNodeLink(lk);
  								SysLogger.info("����: CDP link added: " + lk.toString());
  							}	
  						}catch(Exception e){
  							e.printStackTrace();
  							SysLogger.error(e.getMessage());
  						}
  					}
  				}catch(Exception e){
  					e.printStackTrace();
  					SysLogger.error(e.getMessage());
  				}
  				
  				//�����ڶ�̨�豸��CDPЭ��
  				try{
  					int curCdpNodeId = host2.getId();
  					String curCdpIpAddr = host2.getAdminIp();
  					List executedPort = new ArrayList();
  					Iterator<CdpCachEntryInterface> sub_ite = host2.getCdpList().iterator();
  					while (sub_ite.hasNext()) {
  						try{
  							CdpCachEntryInterface cdpIface = sub_ite.next();
  							//SysLogger.info(host.getIpAddress()+" remoteip:"+cdpIface.getIp()+" remoteDesc:"+cdpIface.getPortdesc());
  							String targetIpAddr = cdpIface.getIp();
  							//�ж�Host2���Ƿ���ڸ�IP
  							Host targetHost = null;
  							if(host1.getAliasIPs().contains(targetIpAddr)){
  								targetHost = host1;
  							}else{
  								continue;
  							}
  							//��ȡcdpIfIndex
  							int cdpIfIndex = -1;
  							if(targetHost.getCdpList() != null && targetHost.getCdpList().size()>0){
  								Iterator<CdpCachEntryInterface> target_ite = targetHost.getCdpList().iterator();
  								while (target_ite.hasNext()) {
  									CdpCachEntryInterface targetcdpIface = target_ite.next();
  									//SysLogger.info(host.getIpAddress()+" startip:"+targetcdpIface.getIp()+" startDesc:"+targetcdpIface.getPortdesc());
  									if(host2.getAliasIPs().contains(targetcdpIface.getIp())){
  										//��Ҫ���統ǰPORTDESC�Ƿ��Ѿ��������������
  									if(executedPort.contains(targetcdpIface.getPortdesc()))continue;
  									if(host2.getIfEntityByDesc(targetcdpIface.getPortdesc()) != null){
  										cdpIfIndex = Integer.parseInt(host2.getIfEntityByDesc(targetcdpIface.getPortdesc()).getIndex());
  										executedPort.add(targetcdpIface.getPortdesc());
  										break;
  									}	
  									}
  								}
  							}
  							int targetCdpNodeId = targetHost.getId();
  							if (cdpIfIndex <= 0) {
  								//���߼��˿ڴ���
  								cdpIfIndex = Integer.parseInt(host1.getIfEntityByIP(host1.getIpAddress()).getIndex());
  								SysLogger.info("���ǺϷ���CDP IfIndex�����߼��˿ڴ���");
  								//continue;
  							}else{
  								SysLogger.info("���ֺϷ��� CDP ifindex " + cdpIfIndex);
  							}
  							int cdpDestIfindex = -1;
  							if(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()) != null){
  								cdpDestIfindex = Integer.parseInt(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()).getIndex());
  								if(cdpDestIfindex < 0){
  									SysLogger.info("���У����Ϸ���CDP destination IfIndex "+cdpDestIfindex+". ����");
  									continue;
  								}
  							}else{
  								SysLogger.info("���У����Ϸ���CDP destination. ����");
  								continue;
  							}
  							SysLogger.info("���У� ���� CDP target ifindex " + cdpDestIfindex);

  							SysLogger.info("����: ���� CDP link: nodeid=" + curCdpNodeId
  								+ " ifindex=" + cdpIfIndex + " nodeparentid="
  								+ targetCdpNodeId + " parentifindex="
  								+ cdpDestIfindex);	
  							boolean add = false;
  							// now add the cdp link
  							SysLogger.info("����: no node is bridge node! Adding CDP link");	
  							
  							add = true;
  							if (add) {
  								NodeToNodeLink lk = new NodeToNodeLink(targetCdpNodeId,cdpDestIfindex);
  								lk.setFindtype(SystemConstant.ISCDP);
  								lk.setNodeparentid(curCdpNodeId);
  								lk.setParentifindex(cdpIfIndex);
  								addNodetoNodeLink(lk);
  								SysLogger.info("����: CDP link added: " + lk.toString());
  							}	
  						}catch(Exception e){
  							e.printStackTrace();
  							SysLogger.error(e.getMessage());
  						}
  					}
  				}catch(Exception e){
  					e.printStackTrace();
  					SysLogger.error(e.getMessage());
  				}
     	   }
     	   
     	   //����NDPЭ�����
     	   if(host1.getNdpHash() != null && host1.getNdpHash().size()>0 &&
     			  host2.getNdpHash() != null && host2.getNdpHash().size()>0 ){
     		  try{
					//Host host = ite.next();
					int curNdpNodeId = host1.getId();
					if(host1.getNdpHash() != null && host1.getNdpHash().size()>0){
					Iterator<String> sub_ite = host1.getNdpHash().keySet().iterator();
					while (sub_ite.hasNext()) {
						try{
						String endndpMac = sub_ite.next();
						String endndpDescr = (String)host1.getNdpHash().get(endndpMac);
						//��MAC�����ڣ������ѭ��
						if(!host2.getMac().equalsIgnoreCase(endndpMac))continue;
						
						Host endNode = host2;
						if(endNode == null){
							SysLogger.info("�Ҳ���MAC��ַ"+endndpMac+",���ѷ��ֵ������豸�����");
							continue;
						}
						IfEntity endIfEntity = endNode.getIfEntityByDesc(endndpDescr);
						IfEntity startIfEntity = null;
						if(endIfEntity == null){
							SysLogger.info("�Ҳ����˿�����Ϊ"+endndpDescr+",���ѷ��ֵ������豸�����");
							//continue;
						}
						//Ѱ�ҿ�ʼ�˵�����
						//Ĭ�������,endNode��NdpHash��Ϊ��
						Hashtable endNodeNdpHash = endNode.getNdpHash();
						if(endNodeNdpHash == null)endNodeNdpHash = new Hashtable();
						if(host1.getMac() == null)continue;
						if(endNodeNdpHash.containsKey(host1.getMac())){
							//���ڸ�IP
							String ndpDescr = (String)endNodeNdpHash.get(host1.getMac());
							startIfEntity = endNode.getIfEntityByDesc(ndpDescr);
						}
						
						if(startIfEntity == null){
							
							startIfEntity = host1.getIfEntityByIP(host1.getIpAddress());
							if(startIfEntity == null)continue;
							
						}
						if(endIfEntity == null){
							//��FDB������
							endIfEntity = endNode.getIfEntityByIP(endNode.getIpAddress());
							if(endIfEntity == null)continue;
							
							
							
						}

						SysLogger.info("����: ���� nodeid/NDP Ŀ��IP: " + endNode.getId()+ ":"+ endNode.getIpAddress());

						SysLogger.info("����: ���� NDP link: nodeid=" + host1.getId()
									+ " ifindex=" + startIfEntity.getIndex() + " nodeparentid="
									+ endNode.getId() + " parentifindex="
									+ endIfEntity.getIndex());

						
						boolean add = false;
						// now add the cdp link
						SysLogger.info("����: no node is bridge node! Adding NDP link");
						add = true;
						if (add) {
							NodeToNodeLink lk = new NodeToNodeLink(endNode.getId(),Integer.parseInt(endIfEntity.getIndex()));
							lk.setFindtype(SystemConstant.ISNDP);
							lk.setNodeparentid(host1.getId());
							lk.setParentifindex(Integer.parseInt(startIfEntity.getIndex()));
							addNodetoNodeLink(lk);
							SysLogger.info("����: NDP link added: " + lk.toString());
						}
						   }catch(Exception e){
							   e.printStackTrace();
							   SysLogger.error(e.getMessage());
						   }
					}
					}
				   }catch(Exception e){
					   e.printStackTrace();
					   SysLogger.error(e.getMessage());
				   }
				   //�����ڶ�̨�豸��NDPЭ��
				   try{
  					int curCdpNodeId = host2.getId();
  					String curCdpIpAddr = host2.getAdminIp();
  					List executedPort = new ArrayList();
  					Iterator<CdpCachEntryInterface> sub_ite = host2.getCdpList().iterator();
  					while (sub_ite.hasNext()) {
  						try{
  							CdpCachEntryInterface cdpIface = sub_ite.next();
  							//SysLogger.info(host.getIpAddress()+" remoteip:"+cdpIface.getIp()+" remoteDesc:"+cdpIface.getPortdesc());
  							String targetIpAddr = cdpIface.getIp();
  							//�ж�Host2���Ƿ���ڸ�IP
  							Host targetHost = null;
  							if(host1.getAliasIPs().contains(targetIpAddr)){
  								targetHost = host1;
  							}else{
  								continue;
  							}
  							//��ȡcdpIfIndex
  							int cdpIfIndex = -1;
  							if(targetHost.getCdpList() != null && targetHost.getCdpList().size()>0){
  								Iterator<CdpCachEntryInterface> target_ite = targetHost.getCdpList().iterator();
  								while (target_ite.hasNext()) {
  									CdpCachEntryInterface targetcdpIface = target_ite.next();
  									//SysLogger.info(host.getIpAddress()+" startip:"+targetcdpIface.getIp()+" startDesc:"+targetcdpIface.getPortdesc());
  									if(host1.getAliasIPs().contains(targetcdpIface.getIp())){
  										//��Ҫ���統ǰPORTDESC�Ƿ��Ѿ��������������
  									if(executedPort.contains(targetcdpIface.getPortdesc()))continue;
  									if(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()) != null){
  										cdpIfIndex = Integer.parseInt(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()).getIndex());
  										executedPort.add(targetcdpIface.getPortdesc());
  										break;
  									}	
  									}
  								}
  							}
  							int targetCdpNodeId = targetHost.getId();
  							if (cdpIfIndex <= 0) {
  								//���߼��˿ڴ���
  								cdpIfIndex = Integer.parseInt(host2.getIfEntityByIP(host2.getIpAddress()).getIndex());
  								SysLogger.info("���ǺϷ���CDP IfIndex�����߼��˿ڴ���");
  								//continue;
  							}else{
  								SysLogger.info("���ֺϷ��� CDP ifindex " + cdpIfIndex);
  							}
  							int cdpDestIfindex = -1;
  							if(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()) != null){
  								cdpDestIfindex = Integer.parseInt(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()).getIndex());
  								if(cdpDestIfindex < 0){
  									SysLogger.info("���У����Ϸ���CDP destination IfIndex "+cdpDestIfindex+". ����");
  									continue;
  								}
  							}else{
  								SysLogger.info("���У����Ϸ���CDP destination. ����");
  								continue;
  							}
  							SysLogger.info("���У� ���� CDP target ifindex " + cdpDestIfindex);

  							SysLogger.info("����: ���� CDP link: nodeid=" + curCdpNodeId
  								+ " ifindex=" + cdpIfIndex + " nodeparentid="
  								+ targetCdpNodeId + " parentifindex="
  								+ cdpDestIfindex);	
  							boolean add = false;
  							// now add the cdp link
  							SysLogger.info("����: no node is bridge node! Adding CDP link");	
  							
  							add = true;
  							if (add) {
  								NodeToNodeLink lk = new NodeToNodeLink(targetCdpNodeId,cdpDestIfindex);
  								lk.setFindtype(SystemConstant.ISCDP);
  								lk.setNodeparentid(curCdpNodeId);
  								lk.setParentifindex(cdpIfIndex);
  								addNodetoNodeLink(lk);
  								SysLogger.info("����: CDP link added: " + lk.toString());
  							}	
  						}catch(Exception e){
  							e.printStackTrace();
  							SysLogger.error(e.getMessage());
  						}
  					}
  				}catch(Exception e){
  					e.printStackTrace();
  					SysLogger.error(e.getMessage());
  				}
     	   }
     	   
     	   List routerlist1 = host1.getRouteList();
     	  List routerlist2 = host2.getRouteList();
     	  
     	   //������һ̨�豸��·�ɱ�
    	   	SysLogger.info("�豸"+host1.getIpAddress()+"·�ɱ��¼��Ϊ: "+routerlist1.size());
	  		for(int i=0;i<routerlist1.size();i++)
	  		{
	  			IpRouter ipr = (IpRouter)routerlist1.get(i);	
	  			SysLogger.info(host1.getIpAddress()+"·�ɱ�####ifindex:"+ipr.getIfIndex()+"    nexthop:"+ipr.getNextHop()+"   type:"+ipr.getType()+"   proto:"+ipr.getProto());
	  			IfEntity ifEntity = null;
	  			if(host1.getIfEntityList() != null){
						for(int j=0;j<host1.getIfEntityList().size();j++){
							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
							ifEntity = (IfEntity)host1.getIfEntityList().get(j);
							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
							if(ifEntity.getIndex().equals(ipr.getIfIndex()))	
								break;
						}
				}
	  			if(host2.getAliasIPs() != null && host2.getAliasIPs().size()>0){
						//���ڶ�̨�豸���ڸ�IP�����������
						if(host2.getAliasIPs().contains(ipr.getNextHop())){
							IfEntity endIfEntity = host2.getIfEntityByIP(ipr.getNextHop());
							Link link = new Link();		           
		 	  	         	link.setStartId(host1.getId());          
		 	  	         	link.setStartIndex(ifEntity.getIndex());
		 	  	         	link.setStartIp(ifEntity.getIpAddress());
		 	  	         	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
		 	  	         	link.setStartDescr(ifEntity.getDescr());
		 	  	         	link.setVlanStartIndex(ifEntity.getIndex());
		 	  	         	link.setEndIp(host2.getIpAddress());
		 	  	         	link.setEndId(host2.getId());
		 	  	         	link.setEndIndex(endIfEntity.getIndex());
		 	  	         	link.setEndDescr(endIfEntity.getDescr());
		 	  	         	link.setEndPhysAddress(endIfEntity.getPhysAddress());
		 	  	         	link.setVlanEndIp(ipr.getNextHop());//���õ�ǰVLAN��ַ
		 	  	         	link.setVlanEndIndex(endIfEntity.getIndex());
		 	  	         	link.setFindtype(SystemConstant.ISRouter);//����MAC����
		 	  	         	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
		 	  	         	dealRouteLinkList(link);
						}
	  			}      		       	   
	  		}	
     	   
     	   //�����ڶ�̨�豸��·�ɱ�
     	   	SysLogger.info("�豸"+host2.getIpAddress()+"·�ɱ��¼��Ϊ: "+routerlist2.size());
	  		for(int i=0;i<routerlist2.size();i++)
	  		{
	  			IpRouter ipr = (IpRouter)routerlist2.get(i);	
	  			SysLogger.info(host2.getIpAddress()+"·�ɱ�####ifindex:"+ipr.getIfIndex()+"    nexthop:"+ipr.getNextHop()+"   type:"+ipr.getType()+"   proto:"+ipr.getProto());
	  			IfEntity ifEntity = null;
	  			if(host2.getIfEntityList() != null){
						for(int j=0;j<host2.getIfEntityList().size();j++){
							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
							ifEntity = (IfEntity)host2.getIfEntityList().get(j);
							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
							if(ifEntity.getIndex().equals(ipr.getIfIndex()))	
								break;
						}
				}
	  			if(host1.getAliasIPs() != null && host1.getAliasIPs().size()>0){
						//���ڶ�̨�豸���ڸ�IP�����������
						if(host1.getAliasIPs().contains(ipr.getNextHop())){
							IfEntity endIfEntity = host1.getIfEntityByIP(ipr.getNextHop());
							Link link = new Link();		           
		 	  	         	link.setStartId(host2.getId());          
		 	  	         	link.setStartIndex(ifEntity.getIndex());
		 	  	         	link.setStartIp(ifEntity.getIpAddress());
		 	  	         	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
		 	  	         	link.setStartDescr(ifEntity.getDescr());
		 	  	         	link.setVlanStartIndex(ifEntity.getIndex());
		 	  	         	link.setEndIp(host1.getIpAddress());
		 	  	         	link.setEndId(host1.getId());
		 	  	         	link.setEndIndex(endIfEntity.getIndex());
		 	  	         	link.setEndDescr(endIfEntity.getDescr());
		 	  	         	link.setEndPhysAddress(endIfEntity.getPhysAddress());
		 	  	         	link.setVlanEndIp(ipr.getNextHop());//���õ�ǰVLAN��ַ
		 	  	         	link.setVlanEndIndex(endIfEntity.getIndex());
		 	  	         	link.setFindtype(SystemConstant.ISRouter);//����MAC����
		 	  	         	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
		 	  	         	dealRouteLinkList(link);
						}
	  			}      		       	   
	  		}

	  		List maclist1 = host1.getMacs();
	  		List maclist2 = host2.getMacs();
	  		//�Ե�һ̨�豸��IPMAC���з���
	  		if(maclist1 != null && maclist1.size()>0){
 	  			for(int i=0;i<maclist1.size();i++){
 	  				try{  	      	 
 	  					IpAddress ipAddr = (IpAddress)maclist1.get(i);
 	  					IfEntity ifEntity = null;
 	  					if(host1.getIfEntityList() != null){
 	  						for(int j=0;j<host1.getIfEntityList().size();j++){
 	  							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
 	  							ifEntity = (IfEntity)host1.getIfEntityList().get(j);
 	  							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
 	  							if(ifEntity.getIndex().equals(ipAddr.getIfIndex()))	
 	  								break;
 	  						}
 	  					} 
 	  					if(host2.getAliasIPs() != null && host2.getAliasIPs().size()>0){
 	  						//���ڶ�̨�豸���ڸ�IP�����������
  							if(host2.getAliasIPs().contains(ipAddr.getIpAddress())){
  								//����IP
  								Link link = new Link();		           
  		 	  	         	  	link.setStartId(host1.getId());          
  		 	  	         	  	link.setStartIndex(ifEntity.getIndex());
  		 	  	         	  	link.setStartIp(ifEntity.getIpAddress());
  		 	  	         	  	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
  		 	  	         	  	link.setStartDescr(ifEntity.getDescr());
  		 	  	         	  	link.setVlanStartIndex(ifEntity.getIndex());
  		 	  	         	  	link.setEndIp(host2.getIpAddress());
  		 	  	         	  	link.setEndId(host2.getId());
  		 	  	         	  	IfEntity endIfEntity = host2.getIfEntityByIP(ipAddr.getIpAddress());
  		 	  	         	  	
  		 	  	         	  	link.setEndIndex(endIfEntity.getIndex());
  		 	  	         	  	link.setEndDescr(endIfEntity.getDescr());
  		 	  	         	  	link.setEndPhysAddress(endIfEntity.getPhysAddress());
  		 	  	         	  	link.setVlanEndIp(ipAddr.getIpAddress());//���õ�ǰVLAN��ַ
  		 	  	         	  	link.setVlanEndIndex(endIfEntity.getIndex());
  		 	  	         	  	link.setFindtype(SystemConstant.ISMac);//����MAC����
  		 	  	         	  	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
  		 	  	         	  	dealMacLinkList(link);
  							}
  						}	        
 	  				}catch(Exception ex){
 	  					ex.printStackTrace();
 	  				}
 	  			}
 	  		}
	  		//�Եڶ�̨�豸��IPMAC���з���
	  		if(maclist2 != null && maclist2.size()>0){
 	  			for(int i=0;i<maclist2.size();i++){
 	  				try{  	      	 
 	  					IpAddress ipAddr = (IpAddress)maclist2.get(i);
 	  					IfEntity ifEntity = null;
 	  					if(host2.getIfEntityList() != null){
 	  						for(int j=0;j<host2.getIfEntityList().size();j++){
 	  							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
 	  							ifEntity = (IfEntity)host2.getIfEntityList().get(j);
 	  							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
 	  							if(ifEntity.getIndex().equals(ipAddr.getIfIndex()))	
 	  								break;
 	  						}
 	  					} 
 	  					if(host1.getAliasIPs() != null && host1.getAliasIPs().size()>0){
 	  						//���ڶ�̨�豸���ڸ�IP�����������
  							if(host1.getAliasIPs().contains(ipAddr.getIpAddress())){
  								//����IP
  								Link link = new Link();		           
  		 	  	         	  	link.setStartId(host2.getId());          
  		 	  	         	  	link.setStartIndex(ifEntity.getIndex());
  		 	  	         	  	link.setStartIp(ifEntity.getIpAddress());
  		 	  	         	  	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
  		 	  	         	  	link.setStartDescr(ifEntity.getDescr());
  		 	  	         	  	link.setVlanStartIndex(ifEntity.getIndex());
  		 	  	         	  	link.setEndIp(host1.getIpAddress());
  		 	  	         	  	link.setEndId(host1.getId());
  		 	  	         	  	IfEntity endIfEntity = host1.getIfEntityByIP(ipAddr.getIpAddress());
  		 	  	         	  	
  		 	  	         	  	link.setEndIndex(endIfEntity.getIndex());
  		 	  	         	  	link.setEndDescr(endIfEntity.getDescr());
  		 	  	         	  	link.setEndPhysAddress(endIfEntity.getPhysAddress());
  		 	  	         	  	link.setVlanEndIp(ipAddr.getIpAddress());//���õ�ǰVLAN��ַ
  		 	  	         	  	link.setVlanEndIndex(endIfEntity.getIndex());
  		 	  	         	  	link.setFindtype(SystemConstant.ISMac);//����MAC����
  		 	  	         	  	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
  		 	  	         	  	dealMacLinkList(link);
  							}
  						}	        
 	  				}catch(Exception ex){
 	  					ex.printStackTrace();
 	  				}
 	  			}
 	  		} 
     	  analyseTopoLinks();
     	   
    	   return returnHash;
       }
       
       public Hashtable linkanaAll(){
    	   SysLogger.info("########## ��ʼ������·��ϵ ##########");
    	   Hashtable returnHash = new Hashtable();
    	   if(hostlist != null && hostlist.size()>0){
    		   for(int i=0;i<hostlist.size();i++){
    			   Host host1 = (Host)hostlist.get(i);
    			   for(int k=0;k<hostlist.size();k++){
    				   Host host2 = (Host)hostlist.get(k);
    				   if(host1.getId() == host2.getId())continue;
    				   
    				   //����CDPЭ�����
    		     	   if(host1.getCdpList() != null && host1.getCdpList().size()>0 &&
    		     			  host2.getCdpList() != null && host2.getCdpList().size()>0 ){
    		  				try{
    		  					int curCdpNodeId = host1.getId();
    		  					String curCdpIpAddr = host1.getAdminIp();
    		  					List executedPort = new ArrayList();
    		  					Iterator<CdpCachEntryInterface> sub_ite = host1.getCdpList().iterator();
    		  					while (sub_ite.hasNext()) {
    		  						try{
    		  							CdpCachEntryInterface cdpIface = sub_ite.next();
    		  							//SysLogger.info(host.getIpAddress()+" remoteip:"+cdpIface.getIp()+" remoteDesc:"+cdpIface.getPortdesc());
    		  							String targetIpAddr = cdpIface.getIp();
    		  							//�ж�Host2���Ƿ���ڸ�IP
    		  							Host targetHost = null;
    		  							if(host2.getAliasIPs().contains(targetIpAddr)){
    		  								targetHost = host2;
    		  							}else{
    		  								continue;
    		  							}
    		  							//��ȡcdpIfIndex
    		  							int cdpIfIndex = -1;
    		  							if(targetHost.getCdpList() != null && targetHost.getCdpList().size()>0){
    		  								Iterator<CdpCachEntryInterface> target_ite = targetHost.getCdpList().iterator();
    		  								while (target_ite.hasNext()) {
    		  									CdpCachEntryInterface targetcdpIface = target_ite.next();
    		  									//SysLogger.info(host.getIpAddress()+" startip:"+targetcdpIface.getIp()+" startDesc:"+targetcdpIface.getPortdesc());
    		  									if(host1.getAliasIPs().contains(targetcdpIface.getIp())){
    		  										//��Ҫ���統ǰPORTDESC�Ƿ��Ѿ��������������
    		  									if(executedPort.contains(targetcdpIface.getPortdesc()))continue;
    		  									if(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()) != null){
    		  										cdpIfIndex = Integer.parseInt(host1.getIfEntityByDesc(targetcdpIface.getPortdesc()).getIndex());
    		  										executedPort.add(targetcdpIface.getPortdesc());
    		  										break;
    		  									}	
    		  									}
    		  								}
    		  							}
    		  							int targetCdpNodeId = targetHost.getId();
    		  							if (cdpIfIndex <= 0) {
    		  								//���߼��˿ڴ���
    		  								cdpIfIndex = Integer.parseInt(host1.getIfEntityByIP(host1.getIpAddress()).getIndex());
    		  								SysLogger.info("���ǺϷ���CDP IfIndex�����߼��˿ڴ���");
    		  								//continue;
    		  							}else{
    		  								SysLogger.info("���ֺϷ��� CDP ifindex " + cdpIfIndex);
    		  							}
    		  							int cdpDestIfindex = -1;
    		  							if(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()) != null){
    		  								cdpDestIfindex = Integer.parseInt(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()).getIndex());
    		  								if(cdpDestIfindex < 0){
    		  									SysLogger.info("���У����Ϸ���CDP destination IfIndex "+cdpDestIfindex+". ����");
    		  									continue;
    		  								}
    		  							}else{
    		  								SysLogger.info("���У����Ϸ���CDP destination. ����");
    		  								continue;
    		  							}
    		  							SysLogger.info("���У� ���� CDP target ifindex " + cdpDestIfindex);

    		  							SysLogger.info("����: ���� CDP link: nodeid=" + curCdpNodeId
    		  								+ " ifindex=" + cdpIfIndex + " nodeparentid="
    		  								+ targetCdpNodeId + " parentifindex="
    		  								+ cdpDestIfindex);	
    		  							boolean add = false;
    		  							// now add the cdp link
    		  							SysLogger.info("����: no node is bridge node! Adding CDP link");	
    		  							
    		  							add = true;
    		  							if (add) {
    		  								NodeToNodeLink lk = new NodeToNodeLink(targetCdpNodeId,cdpDestIfindex);
    		  								lk.setFindtype(SystemConstant.ISCDP);
    		  								lk.setNodeparentid(curCdpNodeId);
    		  								lk.setParentifindex(cdpIfIndex);
    		  								addNodetoNodeLink(lk);
    		  								SysLogger.info("����: CDP link added: " + lk.toString());
    		  							}	
    		  						}catch(Exception e){
    		  							e.printStackTrace();
    		  							SysLogger.error(e.getMessage());
    		  						}
    		  					}
    		  				}catch(Exception e){
    		  					e.printStackTrace();
    		  					SysLogger.error(e.getMessage());
    		  				}
    		     	   }
    		     	   
    		     	//����NDPЭ�����
    		     	   if(host1.getNdpHash() != null && host1.getNdpHash().size()>0 &&
    		     			  host2.getNdpHash() != null && host2.getNdpHash().size()>0 ){
    		     		  try{
    							//Host host = ite.next();
    							int curNdpNodeId = host1.getId();
    							if(host1.getNdpHash() != null && host1.getNdpHash().size()>0){
    							Iterator<String> sub_ite = host1.getNdpHash().keySet().iterator();
    							while (sub_ite.hasNext()) {
    								try{
    								String endndpMac = sub_ite.next();
    								String endndpDescr = (String)host1.getNdpHash().get(endndpMac);
    								//��MAC�����ڣ������ѭ��
    								if(!host2.getMac().equalsIgnoreCase(endndpMac))continue;
    								
    								Host endNode = host2;
    								if(endNode == null){
    									SysLogger.info("�Ҳ���MAC��ַ"+endndpMac+",���ѷ��ֵ������豸�����");
    									continue;
    								}
    								IfEntity endIfEntity = endNode.getIfEntityByDesc(endndpDescr);
    								IfEntity startIfEntity = null;
    								if(endIfEntity == null){
    									SysLogger.info("�Ҳ����˿�����Ϊ"+endndpDescr+",���ѷ��ֵ������豸�����");
    									//continue;
    								}
    								//Ѱ�ҿ�ʼ�˵�����
    								//Ĭ�������,endNode��NdpHash��Ϊ��
    								Hashtable endNodeNdpHash = endNode.getNdpHash();
    								if(endNodeNdpHash == null)endNodeNdpHash = new Hashtable();
    								if(host1.getMac() == null)continue;
    								if(endNodeNdpHash.containsKey(host1.getMac())){
    									//���ڸ�IP
    									String ndpDescr = (String)endNodeNdpHash.get(host1.getMac());
    									startIfEntity = endNode.getIfEntityByDesc(ndpDescr);
    								}
    								
    								if(startIfEntity == null){
    									
    									startIfEntity = host1.getIfEntityByIP(host1.getIpAddress());
    									if(startIfEntity == null)continue;
    									
    								}
    								if(endIfEntity == null){
    									//��FDB������
    									endIfEntity = endNode.getIfEntityByIP(endNode.getIpAddress());
    									if(endIfEntity == null)continue;
    									
    									
    									
    								}

    								SysLogger.info("����: ���� nodeid/NDP Ŀ��IP: " + endNode.getId()+ ":"+ endNode.getIpAddress());

    								SysLogger.info("����: ���� NDP link: nodeid=" + host1.getId()
    											+ " ifindex=" + startIfEntity.getIndex() + " nodeparentid="
    											+ endNode.getId() + " parentifindex="
    											+ endIfEntity.getIndex());

    								
    								boolean add = false;
    								// now add the cdp link
    								SysLogger.info("����: no node is bridge node! Adding NDP link");
    								add = true;
    								if (add) {
    									NodeToNodeLink lk = new NodeToNodeLink(endNode.getId(),Integer.parseInt(endIfEntity.getIndex()));
    									lk.setFindtype(SystemConstant.ISNDP);
    									lk.setNodeparentid(host1.getId());
    									lk.setParentifindex(Integer.parseInt(startIfEntity.getIndex()));
    									addNodetoNodeLink(lk);
    									SysLogger.info("����: NDP link added: " + lk.toString());
    								}
    								   }catch(Exception e){
    									   e.printStackTrace();
    									   SysLogger.error(e.getMessage());
    								   }
    							}
    							}
    						   }catch(Exception e){
    							   e.printStackTrace();
    							   SysLogger.error(e.getMessage());
    						   }
    		     	   }
//    		     	   //
//    		    	  List routerlist1 = host1.getRouteList();
//    		      	  List routerlist2 = host2.getRouteList();
//    		      	  
//    		      	   //������һ̨�豸��·�ɱ�
//    		     	   	SysLogger.info("�豸"+host1.getIpAddress()+"·�ɱ��¼��Ϊ: "+routerlist1.size());
//    		 	  		for(int m=0;m<routerlist1.size();m++)
//    		 	  		{
//    		 	  			IpRouter ipr = (IpRouter)routerlist1.get(m);	
//    		 	  			SysLogger.info(host1.getIpAddress()+"·�ɱ�####ifindex:"+ipr.getIfIndex()+"    nexthop:"+ipr.getNextHop()+"   type:"+ipr.getType()+"   proto:"+ipr.getProto());
//    		 	  			IfEntity ifEntity = null;
//    		 	  			if(host1.getIfEntityList() != null){
//    		 						for(int j=0;j<host1.getIfEntityList().size();j++){
//    		 							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
//    		 							ifEntity = (IfEntity)host1.getIfEntityList().get(j);
//    		 							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
//    		 							if(ifEntity.getIndex().equals(ipr.getIfIndex()))	
//    		 								break;
//    		 						}
//    		 				}
//    		 	  			if(host2.getAliasIPs() != null && host2.getAliasIPs().size()>0){
//    		 						//���ڶ�̨�豸���ڸ�IP�����������
//    		 						if(host2.getAliasIPs().contains(ipr.getNextHop())){
//    		 							IfEntity endIfEntity = host2.getIfEntityByIP(ipr.getNextHop());
//    		 							Link link = new Link();		           
//    		 		 	  	         	link.setStartId(host1.getId());          
//    		 		 	  	         	link.setStartIndex(ifEntity.getIndex());
//    		 		 	  	         	link.setStartIp(ifEntity.getIpAddress());
//    		 		 	  	         	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
//    		 		 	  	         	link.setStartDescr(ifEntity.getDescr());
//    		 		 	  	         	link.setVlanStartIndex(ifEntity.getIndex());
//    		 		 	  	         	link.setEndIp(host2.getIpAddress());
//    		 		 	  	         	link.setEndId(host2.getId());
//    		 		 	  	         	link.setEndIndex(endIfEntity.getIndex());
//    		 		 	  	         	link.setEndDescr(endIfEntity.getDescr());
//    		 		 	  	         	link.setEndPhysAddress(endIfEntity.getPhysAddress());
//    		 		 	  	         	link.setVlanEndIp(ipr.getNextHop());//���õ�ǰVLAN��ַ
//    		 		 	  	         	link.setVlanEndIndex(endIfEntity.getIndex());
//    		 		 	  	         	link.setFindtype(SystemConstant.ISRouter);//����MAC����
//    		 		 	  	         	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
//    		 		 	  	         	dealRouteLinkList(link);
//    		 						}
//    		 	  			}      		       	   
//    		 	  		}	
//    		      	   
//     		 	  		List maclist1 = host1.getMacs();
//    		 	  		List maclist2 = host2.getMacs();
//    		 	  		//�Ե�һ̨�豸��IPMAC���з���
//    		 	  		if(maclist1 != null && maclist1.size()>0){
//    		  	  			for(int p=0;p<maclist1.size();p++){
//    		  	  				try{  	      	 
//    		  	  					IpAddress ipAddr = (IpAddress)maclist1.get(p);
//    		  	  					IfEntity ifEntity = null;
//    		  	  					if(host1.getIfEntityList() != null){
//    		  	  						for(int j=0;j<host1.getIfEntityList().size();j++){
//    		  	  							//Ĭ�������,��Ҫһ��ȱʡ�����ӹ�ϵ,���Ҳ���������·��ʱ��,��VLAN�Ľӿ�������
//    		  	  							ifEntity = (IfEntity)host1.getIfEntityList().get(j);
//    		  	  							//SysLogger.info("�ж��豸IP "+ipAddr.getIpAddress()+"��ifIndex:"+ipAddr.getIfIndex()+" nodeIfIndex:"+ifEntity.getIndex()+"�豸"+node.getIpAddress());
//    		  	  							if(ifEntity.getIndex().equals(ipAddr.getIfIndex()))	
//    		  	  								break;
//    		  	  						}
//    		  	  					} 
//    		  	  					if(host2.getAliasIPs() != null && host2.getAliasIPs().size()>0){
//    		  	  						//���ڶ�̨�豸���ڸ�IP�����������
//    		   							if(host2.getAliasIPs().contains(ipAddr.getIpAddress())){
//    		   								//����IP
//    		   								Link link = new Link();		           
//    		   		 	  	         	  	link.setStartId(host1.getId());          
//    		   		 	  	         	  	link.setStartIndex(ifEntity.getIndex());
//    		   		 	  	         	  	link.setStartIp(ifEntity.getIpAddress());
//    		   		 	  	         	  	link.setStartPhysAddress(ifEntity.getPhysAddress()); //��¼������mac
//    		   		 	  	         	  	link.setStartDescr(ifEntity.getDescr());
//    		   		 	  	         	  	link.setVlanStartIndex(ifEntity.getIndex());
//    		   		 	  	         	  	link.setEndIp(host2.getIpAddress());
//    		   		 	  	         	  	link.setEndId(host2.getId());
//    		   		 	  	         	  	IfEntity endIfEntity = host2.getIfEntityByIP(ipAddr.getIpAddress());
//    		   		 	  	         	  	
//    		   		 	  	         	  	link.setEndIndex(endIfEntity.getIndex());
//    		   		 	  	         	  	link.setEndDescr(endIfEntity.getDescr());
//    		   		 	  	         	  	link.setEndPhysAddress(endIfEntity.getPhysAddress());
//    		   		 	  	         	  	link.setVlanEndIp(ipAddr.getIpAddress());//���õ�ǰVLAN��ַ
//    		   		 	  	         	  	link.setVlanEndIndex(endIfEntity.getIndex());
//    		   		 	  	         	  	link.setFindtype(SystemConstant.ISMac);//����MAC����
//    		   		 	  	         	  	link.setSublinktype(SystemConstant.NONEPHYSICALLINK);//���߼����ӹ�ϵ
//    		   		 	  	         	  	dealMacLinkList(link);
//    		   							}
//    		   						}	        
//    		  	  				}catch(Exception ex){
//    		  	  					ex.printStackTrace();
//    		  	  				}
//    		  	  			}
//    		  	  		}
    			   }
    		   }
    	   }
     	  analyseTopoLinks();
     	   
    	   return returnHash;
       }
   
       /**
        * �����̨�豸�����·
        * @return
        */
	public String linkanalytics()
	{  
		String ip1 = getParaValue("ipaddress1");
		String name1 = getParaValue("name1");
		int version1 = getParaIntValue("version1");
		
		
		String ip2 = getParaValue("ipaddress2");
		String name2 = getParaValue("name2");
		int version2 = getParaIntValue("version2");
		
		request.setAttribute("ipaddress1", ip1);
		request.setAttribute("name1", name1);
		request.setAttribute("version1", version1);
		
		request.setAttribute("ipaddress2", ip2);
		request.setAttribute("name2", name2);
		request.setAttribute("version2", version2);
		

		
		//LinkAnalyticsManager ping = new LinkAnalyticsManager();
		try {
			this.initComm(ip1);
			int flag1=this.sendPDU(name1,version1);
			this.initComm(ip2);
			int flag2=this.sendPDU(name2,version2);
			if(flag1 != 1 && flag2 != 1){
				//�����豸SNMP��û��,��ֹͣ����
				return "/tool/linkanalyticslist.jsp?flag=all";
			}else if(flag1 != 1){
				//��һ��IPû��SNMP
				return "/tool/linkanalyticslist.jsp?flag=1";
			}else if(flag2 != 1){	
				//�ڶ���IPû��SNMP
				return "/tool/linkanalyticslist.jsp?flag=2";
			}
			//�ж��Ƿ�Ϊ�����豸
			
			
			//��ʼ������·��ϵ
			linkana(ip1,name1,version1,ip2,name2,version2);
			
			if(linkList != null && linkList.size()>0){
				
			}
			
			request.setAttribute("linkList", linkList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/tool/linkanalyticslist.jsp?version="+version1;
	}
	
    /**
     * �ӵ����豸��ʼ�����豸����·
     * @return
     */
	public String analyticsFromNode()
	{  
		String id = "53";
		//��ȡ��ǰ�����豸
		HostNodeDao dao = new HostNodeDao();
		//List nodeList = new ArrayList();
		HostNode hostnode = null;
		try{
			hostnode = (HostNode)dao.findByID(id);
		}catch(Exception e){
			
		}finally{
			dao.close();
		}
		Host host = new Host();
		host.setCategory(hostnode.getCategory()); 
		host.setCommunity(hostnode.getCommunity());
		host.setWritecommunity(hostnode.getWriteCommunity());
		//host.setSnmpversion(hostnode.getSnmpversion());
		host.setSysOid(hostnode.getSysOid()); 
		host.setRouter(true);
		host.setSuperNode(-1);
		host.setLocalNet(1);				
		host.setIpAddress(hostnode.getIpAddress());
		host.setLayer(hostnode.getLayer() + 1);
		
		  DiscoverInitialize discoverInit = new DiscoverInitialize();
	      discoverInit.init();
	   	   String snmpversion = "";
	   	   snmpversion = ResourceCenter.getInstance().getSnmpversion();
	   	   int default_version = 0;
	   	   if(snmpversion.equals("v1")){
	  			default_version = org.snmp4j.mp.SnmpConstants.version1;
			  }else if(snmpversion.equals("v2")){
				default_version = org.snmp4j.mp.SnmpConstants.version2c;
			  }else if(snmpversion.equals("v1+v2")){
				default_version = org.snmp4j.mp.SnmpConstants.version1;
			  }else if(snmpversion.equals("v2+v1")){
	  			default_version = org.snmp4j.mp.SnmpConstants.version2c;
		   }
	   	   host.setSnmpversion(default_version);
	   	   DiscoverEngine.getInstance().setSnmpversion(default_version);
	   	   DiscoverResource.getInstance().getCommunitySet().add(hostnode.getCommunity());	      
	      String bid = getParaValue("45");
	      DiscoverEngine.getInstance().setDiscover_bid(bid);

	      DiscoverEngine.getInstance().addHost_SOLO(host,null);
	      
	      // �����̳߳�
		  ThreadPool threadPool = new ThreadPool(2);
		  try{
			  threadPool.runTask(IPRouterProbeThread_SOLO_Runable.createTask(host)); 
		       List ipNetTable = SnmpUtil.getInstance().getIpNetToMediaTable(host.getIpAddress(),host.getCommunity()); 

		   	   if(ipNetTable !=null && ipNetTable.size()>0)  //important  
		   	   {
		   		threadPool.runTask(IPRouterProbeThread_SOLO_Runable.createTask(host)); 
		   	   }
			  	
		  }catch(Exception ex){
			  ex.printStackTrace();
		  }
			// �ر��̳߳ز��ȴ������������
			threadPool.join();
			threadPool.close();
	   		   threadPool = null;
				 
	      
		//DiscoverEngine.getInstance().addThread(new IPRouterProbeThread_SOLO_Runable(host));
		

	   	   
//	   	   DiscoverMonitor.getInstance().setStartTime(SysUtil.getCurrentTime());
//	   	   DiscoverMonitor.getInstance().setEndTime(null);
//	   	   ThreadProbe_SOLO tp = new ThreadProbe_SOLO(DiscoverEngine.getInstance());
//		   tp.setDaemon(true); //����Ϊ�ػ��߳�
//		   tp.start();
		try {
			//��ʼ������·��ϵ
			analyseTopoLinks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
     * ����豸�����·
     * @return
     */
	public String linkAnalyticsAll()
	{  
		//��ȡ���������豸
		HostNodeDao dao = new HostNodeDao();
		List nodeList = new ArrayList();
		try{
			nodeList = dao.loadNetwork(-1);
		}catch(Exception e){
			
		}finally{
			dao.close();
		}
		if(nodeList != null && nodeList.size()>0){
			//�����������豸�������ݲɼ�
			// �����̳߳�,��Ŀ���ݱ����ӵ��豸����
	   		ThreadPool threadPool = new ThreadPool(nodeList.size());
			for(int i=0;i<nodeList.size();i++){
				HostNode hostnode = (HostNode) nodeList.get(i);
				threadPool.runTask(createTask(hostnode));
			}
			// �ر��̳߳ز��ȴ������������
			threadPool.join();
			threadPool.close();
	   		   threadPool = null;
		}
		DiscoverCompleteDao nodeDao = new DiscoverCompleteDao();
		try {	
			//��ʼ������·��ϵ
			linkanaAll();
			DiscoverDataHelper helper = new DiscoverDataHelper();
	        LinkDao linkdao = new LinkDao();
	        List linklist = new ArrayList();
	        Link link = null;
	        try{
	        	linklist = linkdao.loadAll();
	        	linkdao = new LinkDao();
	        	linkdao.delete(linklist);
	        }catch(Exception e){
	        	
	        }finally{
	        	linkdao.close();
	        }
	        
			nodeDao.addLinkData(linkList);
			//helper.memory2DB();
			helper.DB2NetworkXml();
			helper.DB2NetworkVlanXml();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			nodeDao.close();
		}
		return "/tool/isok.jsp";
	}
	
	public String execute(String action) {
		// TODO Auto-generated method stub
		if(action.equals("linkanalytics"))
            return linkanalytics();
		if(action.equals("linkAnalyticsAll"))
            return linkAnalyticsAll();
		if(action.equals("analyticsFromNode"))
            return analyticsFromNode();
		return null;
	}
	
	   private void analyseTopoLinks()
	   {
		   Iterator<Host> ite = null;
		   
		   if(hostlist != null && hostlist.size()>0){
			   for(int i=0;i<hostlist.size();i++){
				   try{
				   Host host = (Host)hostlist.get(i);
				   SysLogger.info(host.toString());
				   if (host == null) {
					   SysLogger.error("�ڵ�Ϊ��ֵ������������һ������");
					   continue;
				   }
				   //���˵��������豸
				   if(host.getCategory() != 1 && host.getCategory() != 2 && host.getCategory() != 3 && host.getCategory() != 7)continue;
				   if(host.getBridgestpList() != null && host.getBridgestpList().size()>0){
					   bridgeNodes.put(new Integer(host.getId()), host);
				   }
				   if(host.getCdpList() != null && host.getCdpList().size()>0){
					   cdpNodes.add(host);
				   }
				   if(host.getNdpHash() != null && host.getNdpHash().size()>0){
					   ndpNodes.add(host);
				   }
				   if(host.getRouteList() != null && host.getRouteList().size()>0){
					   routerNodes.add(host);
				   }
				   
					if (host.getAtInterfaces() != null && host.getAtInterfaces().size()>0) {
						List atInterfaces = host.getAtInterfaces();
						//��HOST��ID�����ȥ
						List atList = new ArrayList();
						for(int k=0;k<atInterfaces.size();k++){
							AtInterface at = (AtInterface)atInterfaces.get(k);
							AtInterface _at = new AtInterface(host.getId(),at.getIpAddress(), at.getMacAddress(),at.getIfindex());
							atList.add(_at);
						}
						if(atList != null && atList.size()>0)
							host.setAtInterfaces(atList);
						atNodes.add(host);
					}
			   }catch(Exception e){
				   e.printStackTrace();
				   SysLogger.error(e.getMessage());
			   }
			   }
		   }
		   
		   SysLogger.info("����: ��atNodes to populate macToAtinterface");

			ite = atNodes.iterator();
			while (ite.hasNext()) {
				Host host = ite.next();
				List atInterfaces = host.getAtInterfaces();
				if(atInterfaces != null && atInterfaces.size()>0){
					for(int k=0;k<atInterfaces.size();k++){
						try{
						AtInterface at = (AtInterface)atInterfaces.get(k);
						int nodeid = host.getId();
						String ipaddr = at.getIpAddress();
						String macAddress = at.getMacAddress();
						//SysLogger.info("���� at Interface nodeid/ipaddr/macaddr: " + nodeid + "/" + ipaddr +"/" + macAddress);
						if (isMacIdentifierOfBridgeNode(macAddress)) {
							//SysLogger.info("����: at interface "+ macAddress+ " belongs to bridge node! Not adding to discoverable atinterface.");
							macsExcluded.add(macAddress);
							continue;
						}
						List<AtInterface> ats = macToAtinterface.get(macAddress);
						if (ats == null) ats = new ArrayList<AtInterface>();
						SysLogger.info("parseAtNodes: Adding to discoverable atinterface.");
						ats.add(at);
						macToAtinterface.put(macAddress, ats);
						SysLogger.info("parseAtNodes: mac:" + macAddress + " has now atinterface reference: " + ats.size());
						   }catch(Exception e){
							   e.printStackTrace();
							   SysLogger.error(e.getMessage());
						   }
					}
				}		
			}

			//SysLogger.info("����: end populate macToAtinterface");

			// First of all use quick methods to get backbone ports for speeding
			// up the link discovery
			//SysLogger.info("����: finding links among nodes using Cisco Discovery Protocol");
		
		   
		   
		   SysLogger.info("����Cisco Discovery Protocol���ֽڵ�������");
		   // Try Cisco Discovery Protocol to found link among all nodes
		   // Add CDP info for backbones ports

//			ite = cdpNodes.iterator();
//			while (ite.hasNext()) {
//				try{
//				Host host = ite.next();
//				int curCdpNodeId = host.getId();
//				String curCdpIpAddr = host.getAdminIp();
//				List executedPort = new ArrayList();
//				Iterator<CdpCachEntryInterface> sub_ite = host.getCdpList().iterator();
//				while (sub_ite.hasNext()) {
//					try{
//					CdpCachEntryInterface cdpIface = sub_ite.next();
//					//SysLogger.info(host.getIpAddress()+" remoteip:"+cdpIface.getIp()+" remoteDesc:"+cdpIface.getPortdesc());
//					String targetIpAddr = cdpIface.getIp();
//					//�ж��Ƿ����Ѿ�������host�б����IP
//					
//					Host targetHost = getHostByAliasIP(targetIpAddr);
//					
//					if( targetHost == null){
//						SysLogger.info("IP��ַ"+targetIpAddr+"�����ѷ��ֵ������豸�����");
//						continue;
//					}
//
//					int targetCdpNodeId = targetHost.getId();
//					if (targetCdpNodeId == -1 || targetCdpNodeId == 0) {
//						SysLogger.info("û���������豸IP "+targetHost.getIpAddress()+"��ID������");
//						continue;
//					}
//					if (targetCdpNodeId == curCdpNodeId) {
//						SysLogger.info("����: ��IPΪ����IP "+ targetIpAddr+ " ����");
//						continue;
//					}
//					
//					//��ȡcdpIfIndex
//					//int cdpIfIndex = Integer.parseInt(cdpIface.getIfindex());
//					int cdpIfIndex = -1;
//					if(targetHost.getCdpList() != null && targetHost.getCdpList().size()>0){
//						Iterator<CdpCachEntryInterface> target_ite = targetHost.getCdpList().iterator();
//						while (target_ite.hasNext()) {
//							CdpCachEntryInterface targetcdpIface = target_ite.next();
//							//SysLogger.info(host.getIpAddress()+" startip:"+targetcdpIface.getIp()+" startDesc:"+targetcdpIface.getPortdesc());
//							if(host.getAliasIPs().contains(targetcdpIface.getIp())){
//								//��Ҫ���統ǰPORTDESC�Ƿ��Ѿ��������������
//								if(executedPort.contains(targetcdpIface.getPortdesc()))continue;
//								if(host.getIfEntityByDesc(targetcdpIface.getPortdesc()) != null){
//									cdpIfIndex = Integer.parseInt(host.getIfEntityByDesc(targetcdpIface.getPortdesc()).getIndex());
//									executedPort.add(targetcdpIface.getPortdesc());
//									break;
//								}	
//							}
//						}
//					}
//					if (cdpIfIndex <= 0) {
//						//���߼��˿ڴ���
//						cdpIfIndex = Integer.parseInt(host.getIfEntityByIP(host.getIpAddress()).getIndex());
//						SysLogger.info("���ǺϷ���CDP IfIndex�����߼��˿ڴ���");
//						//continue;
//					}else{
//						SysLogger.info("���ֺϷ��� CDP ifindex " + cdpIfIndex);
//					}
//					
//					SysLogger.info("����: ���� nodeid/CDP Ŀ��IP: " + targetCdpNodeId+ ":"+ targetIpAddr);
//
//
//					int cdpDestIfindex = -1;
//					if(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()) != null){
//						cdpDestIfindex = Integer.parseInt(targetHost.getIfEntityByDesc(cdpIface.getPortdesc()).getIndex());
//						if(cdpDestIfindex < 0){
//							SysLogger.info("���У����Ϸ���CDP destination IfIndex "+cdpDestIfindex+". ����");
//							continue;
//						}
//					}else{
//						SysLogger.info("���У����Ϸ���CDP destination. ����");
//						continue;
//					}
//					SysLogger.info("���У� ���� CDP target ifindex " + cdpDestIfindex);
//
//					SysLogger.info("����: ���� CDP link: nodeid=" + curCdpNodeId
//								+ " ifindex=" + cdpIfIndex + " nodeparentid="
//								+ targetCdpNodeId + " parentifindex="
//								+ cdpDestIfindex);
//
//					
//					boolean add = false;
//					/*
//					if (host.getBridgestpList()!=null && host.getBridgestpList().size()>0 
//							&& targetHost.getBridgestpList() != null && targetHost.getBridgestpList().size()>0) {
//						add = parseCdpLinkOn(host, cdpIfIndex,targetHost, cdpDestIfindex);
//						SysLogger.info("���У������ڵ㶼��STP�ڵ㣡���ING��"+add);
//					} else if (host.getBridgestpList()!=null && host.getBridgestpList().size()>0) {
//						SysLogger.info("����: source node is bridge node, target node is not bridge node! Adding: " + add);
//						add = parseCdpLinkOn(host,cdpIfIndex,targetCdpNodeId);
//					} else if (isBridgeNode(targetCdpNodeId)) {
//						SysLogger.info("����: source node is not bridge node, target node is bridge node! Adding: " + add);
//						Host targetNode = bridgeNodes.get(new Integer(targetCdpNodeId));
//						add = parseCdpLinkOn(targetNode,cdpDestIfindex,curCdpNodeId);
//					} else {
//						SysLogger.info("����: no node is bridge node! Adding CDP link");
//							add = true;
//					}
//					*/
//					// now add the cdp link
//					SysLogger.info("����: no node is bridge node! Adding CDP link");
//					add = true;
//					if (add) {
//						NodeToNodeLink lk = new NodeToNodeLink(targetCdpNodeId,cdpDestIfindex);
//						lk.setFindtype(SystemConstant.ISCDP);
//						lk.setNodeparentid(curCdpNodeId);
//						lk.setParentifindex(cdpIfIndex);
//						addNodetoNodeLink(lk);
//						SysLogger.info("����: CDP link added: " + lk.toString());
//					}
//					}catch(Exception e){
//						e.printStackTrace();
//						SysLogger.error(e.getMessage());
//					}
//				}
//				}catch(Exception e){
//					e.printStackTrace();
//					SysLogger.error(e.getMessage());
//				}
//			}

//			   SysLogger.info("����HUAWEI �� Network Discovery Protocol���ֽڵ�������");
//			   // Try Cisco Discovery Protocol to found link among all nodes
//			   // Add CDP info for backbones ports
//
//				ite = ndpNodes.iterator();
//				while (ite.hasNext()) {
//					try{
//					Host host = ite.next();
//					int curNdpNodeId = host.getId();
//					if(host.getNdpHash() != null && host.getNdpHash().size()>0){
//					Iterator<String> sub_ite = host.getNdpHash().keySet().iterator();
//					while (sub_ite.hasNext()) {
//						try{
//						String endndpMac = sub_ite.next();
//						String endndpDescr = (String)host.getNdpHash().get(endndpMac);
//						Host endNode = getNodeFromMacIdentifierOfNdpNode(endndpMac);
//						if(endNode == null){
//							SysLogger.info("�Ҳ���MAC��ַ"+endndpMac+",���ѷ��ֵ������豸�����");
//							continue;
//						}
//						IfEntity endIfEntity = endNode.getIfEntityByDesc(endndpDescr);
//						IfEntity startIfEntity = null;
//						if(endIfEntity == null){
//							SysLogger.info("�Ҳ����˿�����Ϊ"+endndpDescr+",���ѷ��ֵ������豸�����");
//							//continue;
//						}
//						//Ѱ�ҿ�ʼ�˵�����
//						//Ĭ�������,endNode��NdpHash��Ϊ��
//						Hashtable endNodeNdpHash = endNode.getNdpHash();
//						if(endNodeNdpHash == null)endNodeNdpHash = new Hashtable();
//						if(host.getMac() == null)continue;
//						if(endNodeNdpHash.containsKey(host.getMac())){
//							//���ڸ�IP
//							String ndpDescr = (String)endNodeNdpHash.get(host.getMac());
//							startIfEntity = endNode.getIfEntityByDesc(ndpDescr);
//						}
//						
//						if(startIfEntity == null){
//							/*
//							//��FDB������
//							List fdbList = host.getFdbList();
//							if(fdbList != null && fdbList.size()>0){
//								int flag = 0;
//								for(int k=0;k<fdbList.size();k++){
//									String[] item = new String[2];
//					    			 item = (String[])fdbList.get(k);
//					    			 if(item[1] != null && item[1].trim().length()>0){
//					    				 if(item[1].equalsIgnoreCase(endNode.getMac())){
//					    					 startIfEntity = host.getIfEntityByIndex(item[0]);
//					    					 flag = 1;
//					    					 break;
//					    				 }
//					    			 }
//					    			 //ipmac.setIfindex(item[0]);
//					    			 //ipmac.setMac(item[1]);
//								}
//								if(flag == 0){
//									//ͨ��FDB�ж�,û���������,��VLAN�������
//									startIfEntity = host.getIfEntityByIP(host.getIpAddress());
//									if(startIfEntity == null)continue;
//								}
//								
//							}else{
//								startIfEntity = host.getIfEntityByIP(host.getIpAddress());
//								if(startIfEntity == null)continue;
//							}
//							*/
//							startIfEntity = host.getIfEntityByIP(host.getIpAddress());
//							if(startIfEntity == null)continue;
//							
//						}
//						if(endIfEntity == null){
//							//��FDB������
//							/*
//							List fdbList = endNode.getFdbList();
//							if(fdbList != null && fdbList.size()>0){
//								int flag = 0;
//								for(int k=0;k<fdbList.size();k++){
//									String[] item = new String[2];
//					    			 item = (String[])fdbList.get(k);
//					    			 if(item[1] != null && item[1].trim().length()>0){
//					    				 if(item[1].equalsIgnoreCase(host.getMac())){
//					    					 startIfEntity = endNode.getIfEntityByIndex(item[0]);
//					    					 flag = 1;
//					    					 break;
//					    				 }
//					    			 }
//					    			 //ipmac.setIfindex(item[0]);
//					    			 //ipmac.setMac(item[1]);
//								}
//								if(flag == 0){
//									//ͨ��FDB�ж�,û���������,��VLAN�������
//									endIfEntity = endNode.getIfEntityByIP(endNode.getIpAddress());
//									if(endIfEntity == null)continue;
//								}
//								
//							}else{
//								endIfEntity = endNode.getIfEntityByIP(endNode.getIpAddress());
//								if(endIfEntity == null)continue;
//							}
//							*/
//							endIfEntity = endNode.getIfEntityByIP(endNode.getIpAddress());
//							if(endIfEntity == null)continue;
//							
//							
//							
//						}
//						if(startIfEntity != null && endIfEntity != null){
//							//�������Ӷ�����
//							if (host.getId() == endNode.getId()){
//								SysLogger.info("����: ������Ϊ����, ����");
//								continue;
//							}	
//						}
//
//						SysLogger.info("����: ���� nodeid/NDP Ŀ��IP: " + endNode.getId()+ ":"+ endNode.getIpAddress());
//
//						SysLogger.info("����: ���� NDP link: nodeid=" + host.getId()
//									+ " ifindex=" + startIfEntity.getIndex() + " nodeparentid="
//									+ endNode.getId() + " parentifindex="
//									+ endIfEntity.getIndex());
//
//						
//						boolean add = false;
//						/*
//						if (host.getBridgestpList()!=null && host.getBridgestpList().size()>0 
//								&& targetHost.getBridgestpList() != null && targetHost.getBridgestpList().size()>0) {
//							add = parseCdpLinkOn(host, cdpIfIndex,targetHost, cdpDestIfindex);
//							SysLogger.info("���У������ڵ㶼��STP�ڵ㣡���ING��"+add);
//						} else if (host.getBridgestpList()!=null && host.getBridgestpList().size()>0) {
//							SysLogger.info("����: source node is bridge node, target node is not bridge node! Adding: " + add);
//							add = parseCdpLinkOn(host,cdpIfIndex,targetCdpNodeId);
//						} else if (isBridgeNode(targetCdpNodeId)) {
//							SysLogger.info("����: source node is not bridge node, target node is bridge node! Adding: " + add);
//							Host targetNode = bridgeNodes.get(new Integer(targetCdpNodeId));
//							add = parseCdpLinkOn(targetNode,cdpDestIfindex,curCdpNodeId);
//						} else {
//							SysLogger.info("����: no node is bridge node! Adding CDP link");
//								add = true;
//						}
//						*/
//						// now add the cdp link
//						SysLogger.info("����: no node is bridge node! Adding NDP link");
//						add = true;
//						if (add) {
//							NodeToNodeLink lk = new NodeToNodeLink(endNode.getId(),Integer.parseInt(endIfEntity.getIndex()));
//							lk.setFindtype(SystemConstant.ISNDP);
//							lk.setNodeparentid(host.getId());
//							lk.setParentifindex(Integer.parseInt(startIfEntity.getIndex()));
//							addNodetoNodeLink(lk);
//							SysLogger.info("����: NDP link added: " + lk.toString());
//						}
//						   }catch(Exception e){
//							   e.printStackTrace();
//							   SysLogger.error(e.getMessage());
//						   }
//					}
//					}
//				   }catch(Exception e){
//					   e.printStackTrace();
//					   SysLogger.error(e.getMessage());
//				   }
//				}
//			// try get backbone links between switches using STP info
//			// and store information in Bridge class
//			SysLogger.info("����: try to found backbone ethernet links among bridge nodes using Spanning Tree Protocol");

			ite = bridgeNodes.values().iterator();
			while (ite.hasNext()) {
				try{
				Host curNode = ite.next();
				List curNodeStpList = curNode.getBridgestpList();
				if(curNodeStpList != null && curNodeStpList.size()>0){
					for(int k=0;k<curNodeStpList.size();k++){
						try{
						BridgeStpInterface bstp = (BridgeStpInterface)curNodeStpList.get(k);
						if (curNode.isBridgeIdentifier(bstp.getBridge().substring(5))) {
							SysLogger.info("����: STP designated root is the bridge itself. Skipping");
							continue;
						}
						Host designatedNode = getNodeFromMacIdentifierOfBridgeNode(bstp.getBridge().substring(5));
						if(designatedNode == null)continue;
						// if port is a backbone port continue
						SysLogger.info(curNode.getIpAddress()+"   Port "+bstp.getPort());
						if (curNode.isBackBoneBridgePort(Integer.parseInt(bstp.getPort()))) {
							SysLogger.info("����: bridge port " + bstp.getPort()+ " already found .... Skipping");
							continue;
						}
						String stpPortDesignatedPort = bstp.getBridgeport();
						stpPortDesignatedPort = stpPortDesignatedPort.replace(":", "");
						SysLogger.info(curNode.getIpAddress()+"   designatedbridgeport "+Integer.parseInt(stpPortDesignatedPort.substring(1), 16));
						int designatedbridgeport = Integer.parseInt(
								stpPortDesignatedPort.substring(1), 16);
						
						int designatedifindex = -1;
						if(designatedNode.getIfEntityByPort(designatedbridgeport+"") != null){
							designatedifindex = Integer.parseInt(designatedNode.getIfEntityByPort(designatedbridgeport+"").getIndex());
						}else{
							SysLogger.info("����: got invalid ifindex on designated node");
							continue;
						}
						
						
						if (designatedifindex == -1 || designatedifindex == 0) {
							SysLogger.info("����: got invalid ifindex on designated node");
							continue;
						}

						SysLogger.info("run: backbone port found for node "
									+ curNode.getId() + ". Adding to bridge"
									+ bstp.getPort());

						curNode.addBackBoneBridgePorts(Integer.parseInt(bstp.getPort()));
						bridgeNodes.put(new Integer(curNode.getId()), curNode);

						SysLogger.info("run: backbone port found for node "
									+ designatedNode.getId()
									+ " .Adding to helper class bb port "
									+ " bridge port " + designatedbridgeport);

						// test if there are other bridges between this link
						// USING MAC ADDRESS FORWARDING TABLE

						if (!isNearestBridgeLink(curNode, Integer.parseInt(bstp.getPort()),
								designatedNode, designatedbridgeport)) {
							SysLogger.info("run: other bridge found between nodes. No links to save!");
							continue; // no saving info if no nodeid
						}
						int curIfIndex = Integer.parseInt(curNode.getIfEntityByPort(bstp.getPort()+"").getIndex());

						if (curIfIndex == -1 || curIfIndex == 0) {
							SysLogger.info("����: got invalid ifindex");
							continue;
						}
						designatedNode.addBackBoneBridgePorts(designatedbridgeport);
						bridgeNodes.put(new Integer(designatedNode.getId()),designatedNode);
						
						SysLogger.info("run: adding links on bb bridge port " + designatedbridgeport);

						addLinks(getMacsOnBridgeLink(curNode,
								Integer.parseInt(bstp.getPort()), designatedNode,
								designatedbridgeport),curNode.getId(),curIfIndex);

						// writing to db using class
						// DbDAtaLinkInterfaceEntry
						NodeToNodeLink lk = new NodeToNodeLink(curNode.getId(),curIfIndex);
						lk.setFindtype(SystemConstant.ISBridge);
						lk.setNodeparentid(designatedNode.getId());
						lk.setParentifindex(designatedifindex);
						addNodetoNodeLink(lk);
					   }catch(Exception e){
						   e.printStackTrace();
						   SysLogger.error(e.getMessage());
					   }
					}
					
				}
			   }catch(Exception e){
				   e.printStackTrace();
				   SysLogger.error(e.getMessage());
			   }
			}
			
			// fourth find inter router links,
			// this part could have several special function to get inter router
			// links, but at the moment we worked much on switches.
			// In future we can try to extend this part.
			SysLogger.info("����: try to found  not ethernet links on Router nodes");

			//List routeLinkList = DiscoverEngine.getInstance().getRouteLinkList();
			if(routelinkList != null && routelinkList.size()>0){
				for(int k=0;k<routelinkList.size();k++){
					try{
					Link link = (Link)routelinkList.get(k);
					// Saving link also when ifindex = -1 (not found)
					NodeToNodeLink lk = new NodeToNodeLink(link.getEndId(),
							Integer.parseInt(link.getEndIndex()));
					lk.setFindtype(SystemConstant.ISRouter);
					lk.setNodeparentid(link.getStartId());
					lk.setParentifindex(Integer.parseInt(link.getStartIndex()));
					SysLogger.info("�������: ##########################");
					SysLogger.info("�������: "+link.getStartIp()+" --- "+link.getEndIp());
					addNodetoNodeLink(lk);
					   }catch(Exception e){
						   e.printStackTrace();
						   SysLogger.error(e.getMessage());
					   }
				}
			}
			//��maclinklist��������,���������CDP/NDP/STP/ROUTER������û�е����Ӽӽ�ȥ
			//List macLinks = DiscoverEngine.getInstance().getMacLinkList();
			if(maclinkList != null && maclinkList.size()>0){
				for(int k=0;k<maclinkList.size();k++){
					try{
					Link maclink = (Link)maclinkList.get(k);
					if(!NodeToNodeLinkExist(maclink)){
						//�������ڸ�����,����ӽ�ȥ
						NodeToNodeLink lk = new NodeToNodeLink(maclink.getEndId(),
								Integer.parseInt(maclink.getEndIndex()));
						lk.setFindtype(SystemConstant.ISMac);
						lk.setNodeparentid(maclink.getStartId());
						lk.setParentifindex(Integer.parseInt(maclink.getStartIndex()));
						SysLogger.info("�������: ##########################");
						SysLogger.info("�������: "+maclink.getStartIp()+" --- "+maclink.getEndIp());
						addNodetoNodeLink(lk);
					}
					   }catch(Exception e){
						   e.printStackTrace();
						   SysLogger.error(e.getMessage());
					   }
				}
			}
			
			
			//List allLinks = DiscoverEngine.getInstance().getLinkList();
			Hashtable existNode = new Hashtable();
			//DiscoverEngine.getInstance().getLinkList().clear();;
			if(links != null && links.size()>0){
				for(int i=0;i<links.size();i++){
					try{
					NodeToNodeLink link = (NodeToNodeLink)links.get(i);
					SysLogger.info("����: "+link.getNodeparentid()+" "+link.getParentifindex()+" "+link.getNodeId()+" "+link.getIfindex());
					Host startNode = null;
					//hostlist.getHostByID(link.getNodeparentid());
					Host endNode = null;
					for(int k=0;k<hostlist.size();k++){
						Host newHost = (Host)hostlist.get(k);
						if(newHost.getId() == link.getNodeparentid()){
							startNode = newHost;
							continue;
						}
						if(newHost.getId() == link.getNodeId()){
							endNode = newHost;
							continue;
						}
					}
					if(endNode == null || startNode == null)continue;
					//DiscoverEngine.getInstance().getHostByID(link.getNodeId());
					SysLogger.info("#### "+link.getParentifindex()+"   #### "+link.getIfindex());
					IfEntity startIfEntity = startNode.getIfEntityByIndex(link.getParentifindex()+"");
					IfEntity endIfEntity = endNode.getIfEntityByIndex(link.getIfindex()+"");
					Link addlink = new Link();
					addlink.setStartId(link.getNodeparentid());
					addlink.setStartIndex(link.getParentifindex()+"");
					addlink.setStartIp(startNode.getIpAddress());
					addlink.setStartDescr(startIfEntity.getDescr());
					addlink.setStartPort(startIfEntity.getPort());
					addlink.setStartPhysAddress(startNode.getBridgeAddress());
					
					addlink.setEndId(link.getNodeId());
					addlink.setEndIndex(link.getIfindex()+"");
					addlink.setEndIp(endNode.getIpAddress());
					addlink.setEndDescr(endIfEntity.getDescr());
					addlink.setEndPort(endIfEntity.getPort());
					addlink.setEndPhysAddress(endNode.getBridgeAddress());
					
					addlink.setAssistant(link.getAssistant());
					addlink.setFindtype(link.getFindtype());
					addlink.setLinktype(0);
					//DiscoverEngine.getInstance().getLinkList().add(addlink);
					dealLinkList(addlink);
					//linkList.add(addlink);
					if(!existNode.containsKey(addlink.getStartId())){
						existNode.put(addlink.getStartId(), addlink.getStartId());
					}
					if(!existNode.containsKey(addlink.getEndId())){
						existNode.put(addlink.getEndId(), addlink.getEndId());
					}
					//allLinks.add(addlink);
				   }catch(Exception e){
					   e.printStackTrace();
					   SysLogger.error(e.getMessage());
				   }
				}
			}
			
			
			
			//�ж��Ƿ���û�����ӵ�Node
			//��û�в������ӵĹ����Ľӵ����߼����Ӵ���
			//List macLinkList = DiscoverEngine.getInstance().getMacLinkList();
			if(maclinkList != null && maclinkList.size()>0){
				for(int k=0;k<maclinkList.size();k++){
					try{
					Link maclink = (Link)maclinkList.get(k);
					if(!existNode.containsKey(maclink.getStartId()) || !existNode.containsKey(maclink.getEndId())){
						//���и��˵㲻���Ѿ����ڵ������б���
						// Saving link also when ifindex = -1 (not found)
						NodeToNodeLink link = new NodeToNodeLink(maclink.getEndId(),
								Integer.parseInt(maclink.getEndIndex()));
						link.setFindtype(SystemConstant.ISMac);
						link.setNodeparentid(maclink.getStartId());
						link.setParentifindex(Integer.parseInt(maclink.getStartIndex()));
						SysLogger.info("�������: ##########################");
						SysLogger.info("�������: "+maclink.getStartIp()+" --- "+maclink.getEndIp());
						addNodetoNodeLink(link);
						//NodeToNodeLink link = (NodeToNodeLink)links.get(i);
						SysLogger.info("����: "+link.getNodeparentid()+" "+link.getParentifindex()+" "+link.getNodeId()+" "+link.getIfindex());
						Host startNode = DiscoverEngine.getInstance().getHostByID(link.getNodeparentid());
						Host endNode = DiscoverEngine.getInstance().getHostByID(link.getNodeId());
						IfEntity startIfEntity = startNode.getIfEntityByIndex(link.getParentifindex()+"");
						IfEntity endIfEntity = endNode.getIfEntityByIndex(link.getIfindex()+"");
						Link addlink = new Link();
						addlink.setStartId(link.getNodeparentid());
						addlink.setStartIndex(link.getParentifindex()+"");
						addlink.setStartIp(startNode.getIpAddress());
						addlink.setStartDescr(startIfEntity.getDescr());
						addlink.setStartPort(startIfEntity.getPort());
						addlink.setStartPhysAddress(startNode.getBridgeAddress());
						
						addlink.setEndId(link.getNodeId());
						addlink.setEndIndex(link.getIfindex()+"");
						addlink.setEndIp(endNode.getIpAddress());
						addlink.setEndDescr(endIfEntity.getDescr());
						addlink.setEndPort(endIfEntity.getPort());
						addlink.setEndPhysAddress(endNode.getBridgeAddress());
						
						addlink.setAssistant(link.getAssistant());
						addlink.setFindtype(link.getFindtype());
						addlink.setLinktype(0);//��������
						//DiscoverEngine.getInstance().getLinkList().add(addlink);
						linkList.add(addlink);
//						if(!existNode.containsKey(addlink.getStartId())){
//							existNode.put(addlink.getStartId(), addlink.getStartId());
//						}
//						if(!existNode.containsKey(addlink.getEndId())){
//							existNode.put(addlink.getEndId(), addlink.getEndId());
//						}
					}
				   }catch(Exception e){
					   e.printStackTrace();
					   SysLogger.error(e.getMessage());
				   }
				}
			}

			//��·��������ӽ�ȥ
			if(routelinkList != null && routelinkList.size()>0){
				for(int k=0;k<routelinkList.size();k++){
					try{
					Link routelink = (Link)routelinkList.get(k);
					routelink.setLinktype(-1);//��������		
					//linkList.add(routelink);
					dealLinkList(routelink);
					//DiscoverEngine.getInstance().getLinkList().add(routelink);
					   }catch(Exception e){
						   e.printStackTrace();
						   SysLogger.error(e.getMessage());
					   }
				}
			}
			//��VLAN������ӽ�ȥ
			if(maclinkList != null && maclinkList.size()>0){
				for(int k=0;k<maclinkList.size();k++){
					try{
					Link maclink = (Link)maclinkList.get(k);
					maclink.setLinktype(-1);//��������
					//linkList.add(maclink);
					dealLinkList(maclink);
					//DiscoverEngine.getInstance().getLinkList().add(maclink);
					   }catch(Exception e){
						   e.printStackTrace();
						   SysLogger.error(e.getMessage());
					   }
				}
			}
			
			//SysLogger.info("linkList=============================="+linkList.size());
			if(linkList != null && linkList.size()>0){
				for(int i=0;i<linkList.size();i++){
					Link link = (Link)linkList.get(i);
					SysLogger.info("IP:"+link.getStartIp()+" ����:"+link.getStartIndex()+" ���� IP:"+link.getEndIp()+"  ����:"+link.getEndIndex());
				}
			}
			
	   }
	   
		private boolean isMacIdentifierOfBridgeNode(String macAddress) {
			Iterator<Host> ite = bridgeNodes.values().iterator();
			while (ite.hasNext()) {
				Host curNode = ite.next();
				if (curNode.isBridgeIdentifier(macAddress))
					return true;
			}
			return false;
		}

		private void addNodetoNodeLink(NodeToNodeLink nnlink) {
			if (nnlink == null)
			{
				SysLogger.info("addNodetoNodeLink: node link is null.");
					return;
			}
			if (!links.isEmpty()) {
				Iterator<NodeToNodeLink> ite = links.iterator();
				while (ite.hasNext()) {
					NodeToNodeLink curNnLink = ite.next();
					if (curNnLink.equals(nnlink)) {
						SysLogger.info("��ӽڵ�����: link " + nnlink.toString() + " exists, not adding");
						return;
					}
				}
			}
			
			int assitantLink = countNodetoNodeLink(nnlink);
			if(assitantLink == 0){
				//�����ڸ�����,�����
				SysLogger.info("��ӽڵ�����: adding link " + nnlink.toString());
				links.add(nnlink);
			}else if(assitantLink == 1){
				//�Ѿ�����һ��,��Ҫ�Ѹ���������Ϊ��������
				SysLogger.info("��ӽڵ�����: adding link " + nnlink.toString());
				nnlink.setAssistant(1);
				links.add(nnlink);
			}else{
				return;
			}
		}
		
		private int countNodetoNodeLink(NodeToNodeLink nnlink) {
			int counts = 0;
			if (nnlink == null)
			{
				SysLogger.info("addNodetoNodeLink: node link is null.");
					return counts;
			}
			if (!links.isEmpty()) {
				Iterator<NodeToNodeLink> ite = links.iterator();
				while (ite.hasNext()) {
					NodeToNodeLink curNnLink = ite.next();
					if (curNnLink.assistantequals(nnlink)) {
						counts = counts+1;
						SysLogger.info("���Ӵ���: link " + nnlink.toString() + " exists, not adding");
						//return counts;
					}
				}
			}
			SysLogger.info("�����ӵ���: "+counts+" ��" + nnlink.toString());
			return counts;
		}
		
		private boolean NodeToNodeLinkExist(Link link){
			boolean flag = false;
			for(int i=0;i<links.size();i++){
				NodeToNodeLink nodelink = (NodeToNodeLink)links.get(i);
				if (nodelink.getNodeId() == link.getStartId()	&&
						nodelink.getNodeparentid() == link.getEndId() ){
					flag = true;
					break;
				}

				if (nodelink.getNodeparentid() == link.getStartId() && 
						nodelink.getNodeId() == link.getEndId() ){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		private Host getNodeFromMacIdentifierOfNdpNode(String macAddress) {
			Iterator<Host> ite = ndpNodes.iterator();
			while (ite.hasNext()) {
				Host curNode = ite.next();
				SysLogger.info(curNode.getMac()+"-------"+macAddress);
				if(curNode.getMac() == null)continue;
				if (curNode.getMac().equalsIgnoreCase(macAddress))
					return curNode;
			}
			return null;
		}
		
		/**
		 * 
		 * @param stpportdesignatedbridge
		 * @return Bridge Bridge Node if found else null
		 */

		private Host getNodeFromMacIdentifierOfBridgeNode(String macAddress) {
			Iterator<Host> ite = bridgeNodes.values().iterator();
			while (ite.hasNext()) {
				Host curNode = ite.next();
				if (curNode.isBridgeIdentifier(macAddress))
					return curNode;
			}
			return null;
		}
		
		private boolean isNearestBridgeLink(Host bridge1, int bp1,
				Host bridge2, int bp2) {

			boolean hasbridge2forwardingRule = false;
			Set<String> macsOnBridge2 = bridge2.getMacAddressesOnBridgePort(bp2);

			Set<String> macsOnBridge1 = bridge1.getMacAddressesOnBridgePort(bp1);

			if (macsOnBridge2 == null || macsOnBridge1 == null)
				return false;

			if (macsOnBridge2.isEmpty() || macsOnBridge1.isEmpty())
				return false;

			Iterator<String> macsonbridge1_ite = macsOnBridge1.iterator();

			while (macsonbridge1_ite.hasNext()) {
				String curMacOnBridge1 = macsonbridge1_ite.next();
				// if mac address is bridge identifier of bridge 2 continue
				
				if (bridge2.isBridgeIdentifier(curMacOnBridge1)) {
					hasbridge2forwardingRule = true;
					continue;
				}
				// if mac address is itself identifier of bridge1 continue
				if (bridge1.isBridgeIdentifier(curMacOnBridge1))
					continue;
				// then no identifier of bridge one no identifier of bridge 2
				// bridge 2 contains  
				if (macsOnBridge2.contains(curMacOnBridge1)
						&& isMacIdentifierOfBridgeNode(curMacOnBridge1))
					return false;
			}

			return hasbridge2forwardingRule;
		}
		
		private Set<String> getMacsOnBridgeLink(Host bridge1, int bp1,
				Host bridge2, int bp2) {

			Set<String> macsOnLink = new HashSet<String>();

	    	Set<String> macsOnBridge1 = bridge1.getMacAddressesOnBridgePort(bp1);

			Set<String> macsOnBridge2 = bridge2.getMacAddressesOnBridgePort(bp2);

			if (macsOnBridge2 == null || macsOnBridge1 == null)
				return null;

			if (macsOnBridge2.isEmpty() || macsOnBridge1.isEmpty())
				return null;

			Iterator<String> macsonbridge1_ite = macsOnBridge1.iterator();

			while (macsonbridge1_ite.hasNext()) {
				String curMacOnBridge1 = macsonbridge1_ite.next();
				if (bridge2.isBridgeIdentifier(curMacOnBridge1))
					continue;
				if (macsOnBridge2.contains(curMacOnBridge1))
					macsOnLink.add(curMacOnBridge1);
			}
			return macsOnLink;
		}
		
		private void addLinks(Set<String> macs,int nodeid,int ifindex) { 
			if (macs == null || macs.isEmpty()) {
				SysLogger.info("addLinks: mac's list on link is empty.");
			} else {
				Iterator<String> mac_ite = macs.iterator();

				while (mac_ite.hasNext()) {
					String curMacAddress = mac_ite.next();
					if (macsParsed.contains(curMacAddress)) {
						SysLogger.info("�������: MAC��ַ"+ curMacAddress+"�������Ŷ˿ڷ���!����...");
						continue;
					}
					
					if (macsExcluded.contains(curMacAddress)) {
						SysLogger.info("�������: MAC��ַ"+ curMacAddress+ " is excluded from discovery package! Skipping...");
						continue;
					}
					
					if (macToAtinterface.containsKey(curMacAddress)) {
						List<AtInterface> ats = macToAtinterface.get(curMacAddress);
						Iterator<AtInterface> ite = ats.iterator();
						while (ite.hasNext()) {
							AtInterface at = ite.next();
							
							NodeToNodeLink lNode = new NodeToNodeLink(at.getNodeId(),at.getIfindex());
							lNode.setNodeparentid(nodeid);
							lNode.setParentifindex(ifindex);
							
							addNodetoNodeLink(lNode);
						}
					} else {
						SysLogger.info("�������:not find nodeid for ethernet mac address "+ curMacAddress+ " found on node/ifindex" + nodeid+ "/" +ifindex);
						MacToNodeLink lMac = new MacToNodeLink(curMacAddress);
						lMac.setNodeparentid(nodeid);
						lMac.setParentifindex(ifindex);
						maclinks.add(lMac);
					}
					macsParsed.add(curMacAddress);
				}
			}
		}
		
		public synchronized Host getHostByAliasIP(String ip)
		{
			Host host = null;
			for(int i=0;i<hostlist.size();i++)
			{
				if(((Host)hostlist.get(i)).getAliasIPs() == null) continue;
				if(((Host)hostlist.get(i)).getAliasIPs().contains(ip))
				{
				    host = (Host)hostlist.get(i);
				    break;
				}			    	
			}
			return host;
		}
		
		public synchronized void dealLinkList(Link link){
			if(linkList != null && linkList.size()>0){
				for(int i=0;i<linkList.size();i++){
					Link newlink = (Link)linkList.get(i);
					if(newlink.equals(link)){
						break;
					}else{
						linkList.add(link);
					}
				}
			}else{
				linkList.add(link);
			}
		}
		
		public synchronized void dealMacLinkList(Link link){
			if(maclinkList != null && maclinkList.size()>0){
				for(int i=0;i<maclinkList.size();i++){
					Link newlink = (Link)maclinkList.get(i);
					if(newlink.equals(link)){
						break;
					}else{
						maclinkList.add(link);
					}
				}
			}else{
				maclinkList.add(link);
			}
		}

		public synchronized void dealRouteLinkList(Link link){
			if(routelinkList != null && routelinkList.size()>0){
				for(int i=0;i<routelinkList.size();i++){
					Link newlink = (Link)routelinkList.get(i);
					if(newlink.equals(link)){
						break;
					}else{
						routelinkList.add(link);
					}
				}
			}else{
				routelinkList.add(link);
			}
		}
		
		/**
	    ��������
		 */	
		private static Runnable createTask(final int id,final String ip,final int version,final String commu) {
	    return new Runnable() {
	        public void run() {
	        	try {                	
	        		Host host1 = new Host();
	       			host1.setId(id);
	       			host1.setIpAddress(ip);
	       			host1.setCommunity(commu);
	    		
	       			
	        	   LinkSnmpUtil snmputil1 =  new LinkSnmpUtil(version);
	        	   String sysOid = "";
	      			int deviceType = 0;
	      			sysOid = snmputil1.getSysOid(ip,commu);
	      			deviceType = snmputil1.checkDevice(ip,commu,sysOid);
	      			host1.setCategory(deviceType);
	      			host1.setSysOid(sysOid);
	      			
	        	   //��ȡ·�ɱ�
	        	   List routerlist1 = snmputil1.getIPRouterTable(ip, commu);
	        	   //MAC��ַ��
	        	   List maclist1 = snmputil1.getIpNetToMediaTable(ip, commu);
	        	   //NDP��ַ��
	        	   Hashtable ndphash1 = snmputil1.getNDPTable(ip, commu);
	        	   //CDP��ַ��
	        	   List cdplist1 = snmputil1.getCiscoCDPList(ip, commu);
	        	   //STP��ַ��
	        	   List stplist1 = snmputil1.getBridgeStpList(ip, commu);
	        	   //AT��ַ��
	        	   List atlist1 = snmputil1.getAtInterfaceTable(ip, commu);
	        	   
	        	   List ifEntityList1 = snmputil1.getIfEntityList(ip,commu,host1.getCategory());
	        	   
	        	   
	        	   host1.setRouteList(routerlist1);
	        	   host1.setIpNetTable(maclist1);
	        	   host1.setNdpHash(ndphash1);
	        	   host1.setCdpList(cdplist1);
	        	   host1.setAtInterfaces(atlist1);
	        	   host1.setBridgestpList(stplist1);
	        	   host1.setIfEntityList(ifEntityList1);
	        	   hostlist.add(host1);
	            }catch(Exception exc){
	            	
	            }
	        }
	    };
		}
		
		/**
	    ��������
		 */	
		private static Runnable createTask(final HostNode hostnode) {
	    return new Runnable() {
	        public void run() {
	        	try {    
	        		
	        		//��ȡ·�ɱ�
	        		List routerList = SnmpUtil.getInstance().getRouterList(hostnode.getIpAddress(),hostnode.getCommunity()); 
	        		//MAC��ַ��
	        		List ipNetTable = SnmpUtil.getInstance().getIpNetToMediaTable(hostnode.getIpAddress(),hostnode.getCommunity()); 
	        	   //NDP��ַ��
	        	   Hashtable ndphash1 = SnmpUtil.getInstance().getNDPTable(hostnode.getIpAddress(),hostnode.getCommunity());
	        	   //CDP��ַ��
	        	   List cdplist1 = SnmpUtil.getInstance().getCiscoCDPList(hostnode.getIpAddress(),hostnode.getCommunity());
	        	   //STP��ַ��
	        	   List stplist1 = SnmpUtil.getInstance().getBridgeStpList(hostnode.getIpAddress(),hostnode.getCommunity());
	        	   //AT��ַ��
	        	   List atlist1 = SnmpUtil.getInstance().getAtInterfaceTable(hostnode.getIpAddress(),hostnode.getCommunity());
	        	   
	        	   List ifEntityList1 = SnmpUtil.getInstance().getIfEntityList(hostnode.getIpAddress(),hostnode.getCommunity(),hostnode.getCategory());
	        	   
	        		Host host = new Host();
	       			host.setId(hostnode.getId());
	       			host.setIpAddress(hostnode.getIpAddress());
	       			host.setCommunity(hostnode.getCommunity());
	      			host.setCategory(hostnode.getCategory());
	      			host.setSysOid(hostnode.getSysOid());
	      			
	        	   host.setRouteList(routerList);
	        	   host.setIpNetTable(ipNetTable);
	        	   host.setNdpHash(ndphash1);
	        	   host.setCdpList(cdplist1);
	        	   host.setAtInterfaces(atlist1);
	        	   host.setBridgestpList(stplist1);
	        	   host.setIfEntityList(ifEntityList1);
	        	   hostlist.add(host);
	            }catch(Exception exc){
	            	
	            }
	        }
	    };
		}
}
class ThreadProbe_SOLO extends Thread
{
   private DiscoverEngine engine;  	
   public ThreadProbe_SOLO(DiscoverEngine engine)
   {
      this.engine = engine;
   }

   public void run()
   {
      try
      {
         Thread.sleep(50000);
      }
      catch(InterruptedException ie)
      {}
      
   	  while(!engine.isDiscovered())//������ܻ������ѭ��
      {
         try
         {
            Thread.sleep(30000);
         }
         catch(InterruptedException ie)
         {}
      }
   	  
   	  DiscoverMonitor.getInstance().setEndTime(SysUtil.getCurrentTime());
      DiscoverMonitor.getInstance().setCompleted(true);
      
      DiscoverComplete_SOLO dc = new DiscoverComplete_SOLO();
      dc.completed(true);      
      SysLogger.info("����ʱ��:" + SysUtil.getCurrentTime());
      /**/
  	//CollectIPDetail cfd = new CollectIPDetail();
	//cfd.findDirectDevices();       	
      
   }	  
}
