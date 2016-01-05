/**
 * <p>Description:node of topology</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-10
 */

package com.afunms.discovery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.afunms.common.util.SysLogger;

public class Node
{
    protected int id;           //id
    protected int category;     //���
    protected int superNode;    //��һ���ڵ��id
    protected int localNet;     //��������
    protected String ipAddress; //IP��ַ
    protected String adminIp; 	//����IP��ַ
    protected String netMask;   //��������
    protected String community; //��ͬ��
    protected String writecommunity; //��ͬ��
    protected int snmpversion;//SNMP�汾
    protected int transfer;//���䷽��0���ޣ�1������
    protected String assetid;    //�ʲ����
    protected String location;    //����λ��
    protected String sysOid;    //ϵͳoid
    protected String sysName;   //ϵͳ����
    protected String sysDescr;  //ϵͳ���� 
    protected String sysLocation;  //ϵͳ����
    protected String sysContact;  //ϵͳ����
    protected String mac;       //MAC��ַ
    protected List macs;       //MAC��ַ
    protected int layer;        //�� 
    protected int status;       //״̬
    protected List ifEntityList; //���ж˿�
    protected List fdbList; //���ж˿�
    protected List routeList; //���ж˿�
    protected List aliasIPs;//IP����
    protected List aliasIfEntitys;//IP����
    protected List<BridgeStpInterface> bridgestpList; //����STP������
    protected Hashtable portVSBridgeHash;//�Ŷ˿�:�ö˿ڵ���MACS
    protected List<CdpCachEntryInterface> cdpList; //����STP������
    protected Hashtable ndpHash;//MAC:�˿���
    protected boolean managed;
    protected String type;
    protected long ipLong;
    protected HashMap<Integer,Set<String>> portMacs = new HashMap<Integer,Set<String>>();
    protected List<String> bridgeIdentifiers = new java.util.ArrayList<String>();
    protected List<AtInterface> m_atinterfaces = new ArrayList<AtInterface>();
    protected HashMap<String,List<BridgeStpInterface>> BridgeStpInterfaces = new HashMap<String,List<BridgeStpInterface>>();
    protected int discoverstatus;       //��η��ֵ�״̬
    protected int collecttype;//���ݲɼ���ʽ  1:snmp 2:shell
    protected int ostype;//����ϵͳ����  1:snmp 2:shell
	private String sendmobiles;
	private String sendemail;
	private String sendphone;
	private String bid;
	private int endpoint;//ĩ���豸
	private int supperid;//��Ӧ��id snow add at 2010-5-18
    
    
    
	/**
	 * the list of bridge port that are backbone bridge ports ou that are
	 * link between switches
	 */
	protected List<Integer> backBoneBridgePorts = new java.util.ArrayList<Integer>();
	protected List ipNetTable ;
    
    public Node()
    {
   	    category = -1; //��ʼ��ʱΪδ֪�豸
   	    ifEntityList = null;
   	    fdbList = null;
    }
    
	List<Integer> getBridgePortsFromMac(String macAddress) {
		List<Integer> ports = new ArrayList<Integer>();
		Iterator<Integer> ite = portMacs.keySet().iterator();
		while (ite.hasNext()) {
			Integer intePort = ite.next();
			Set<String> macs = portMacs.get(intePort);
			if (macs.contains(macAddress)) {
				ports.add(intePort);
			}
		}
		return ports;
	}
	
	boolean hasMacAddress(String macAddress) {
		Set<String> macs = new HashSet<String>();
		Iterator<Set<String>> ite = portMacs.values().iterator();
		while (ite.hasNext()) {
			macs = ite.next();
			if (macs.contains(macAddress))
				return true;
		}
		return false;
	}
	
	/**
	 * @return Returns the stpInterfaces.
	 */
	public HashMap<String,List<BridgeStpInterface>> getStpInterfaces() {
		return BridgeStpInterfaces;
	}
	/**
	 * @param stpInterfaces The stpInterfaces to set.
	 */
	public void setStpInterfaces(HashMap<String,List<BridgeStpInterface>> stpInterfaces) {
		BridgeStpInterfaces = stpInterfaces;
	}
	
	public void addStpInterface(BridgeStpInterface stpIface) {
		String vlanindex = stpIface.getVlan();
		List<BridgeStpInterface> stpifs = new ArrayList<BridgeStpInterface>();;
		if (BridgeStpInterfaces.containsKey(vlanindex)) {
			stpifs = BridgeStpInterfaces.get(vlanindex);
		}
		stpifs.add(stpIface);
		BridgeStpInterfaces.put(vlanindex, stpifs);
	}
	/**
	 * @return Returns the m_routeinterfaces.
	 */
	public List<AtInterface> getAtInterfaces() {
		return m_atinterfaces;
	}
	/**
	 * @param m_cdpinterfaces The m_cdpinterfaces to set.
	 */
	public void setAtInterfaces(List<AtInterface> m_atinterfaces) {
		if (m_atinterfaces == null || m_atinterfaces.isEmpty()) return;
		//this.m_hasatinterfaces = true;
		this.m_atinterfaces = m_atinterfaces;
	}
	/**
	 * add bridgeport to backbone ports
	 * @param bridgeport
	 */
	public void addBackBoneBridgePorts(int bridgeport) {
		if (backBoneBridgePorts.contains(new Integer(bridgeport)))
			return;
		backBoneBridgePorts.add(new Integer(bridgeport));
	}
    
	/**
	 * @return Returns the bridgeIdentifiers.
	 */
	List<String> getBridgeIdentifiers() {
		return bridgeIdentifiers;
	}

	/**
	 * @param bridgeIdentifiers
	 *            The bridgeIdentifiers to set.
	 */
	void setBridgeIdentifiers(List<String> bridgeIdentifiers) {
		if (bridgeIdentifiers == null || bridgeIdentifiers.isEmpty() ) return;
		this.bridgeIdentifiers = bridgeIdentifiers;
		//isBridgeNode = true;
	}


	public boolean isBridgeIdentifier(String bridge) {
		return bridgeIdentifiers.contains(bridge);
	}

	void addBridgeIdentifier(String bridge) {
		if (bridgeIdentifiers.contains(bridge))
			return;
		bridgeIdentifiers.add(bridge);
		//isBridgeNode = true;
	}
    
	public Set<String> getMacAddressesOnBridgePort(int bridgeport) {
		return  portMacs.get(new Integer(bridgeport));
	}
	/**
	 * @return Returns the portMacs.
	 */
	HashMap<Integer, Set<String>> getPortMacs() {
		return portMacs;
	}

	/**
	 * @param portMacs
	 *            The portMacs to set.
	 */
	void setPortMacs(HashMap<Integer,Set<String>> portMacs) {
		this.portMacs = portMacs;
	}
	/**
	 * @param backBoneBridgePorts
	 *            The backBoneBridgePorts to set.
	 */
	void setBackBoneBridgePorts(List<Integer> backBoneBridgePorts) {
		this.backBoneBridgePorts = backBoneBridgePorts;
	}

	/**
	 * return true if bridgeport is a backbone port
	 * @param bridgeport
	 * @return
	 */
	public boolean isBackBoneBridgePort(int bridgeport) {
		return backBoneBridgePorts.contains(new Integer(bridgeport));
	}
	public List getRouteList() {
		return routeList;
	}
	public void setRouteList(List routeList) {
		this.routeList = routeList;
	}
	
	public List getIpNetTable() {
		return ipNetTable;
	}
	public void setIpNetTable(List ipNetTable) {
		this.ipNetTable = ipNetTable;
	}
	
	public long getIpLong() {
		return ipLong;
	}
	public void setIpLong(long ipLong) {
		this.ipLong = ipLong;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public List getMacs() {
		return macs;
	}

	public void setMacs(List macs) {
		this.macs = macs;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
	public String getWritecommunity() {
		return writecommunity;
	}

	public void setWritecommunity(String writecommunity) {
		this.writecommunity = writecommunity;
	}
	
	public int getSnmpversion() {
		return snmpversion;
	}

	public void setSnmpversion(int snmpversion) {
		this.snmpversion = snmpversion;
	}

	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

	public String getAssetid() {
		return assetid;
	}

	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		setAdminIp(ipAddress);
	}
	
	public String getAdminIp() {
		return adminIp;
	}

	public void setAdminIp(String adminIp) {
		this.adminIp = adminIp;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLocalNet() {
		return localNet;
	}

	public void setLocalNet(int localNet) {
		this.localNet = localNet;
	}

	public String getNetMask() {
		return netMask;
	}

	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}

	public int getSuperNode() {
		return superNode;
	}

	public void setSuperNode(int superNode) {
		this.superNode = superNode;
	}

	public String getSysDescr() {
		return sysDescr;
	}

	public void setSysDescr(String sysDescr) {
		this.sysDescr = sysDescr;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getSysContact() 
	{
		return sysContact;
	}

	public void setSysContact(String sysContact) 
	{
		this.sysContact = sysContact;
	}
	
	public String getSysLocation() 
	{
		return sysLocation;
	}

	public void setSysLocation(String sysLocation) 
	{
		this.sysLocation = sysLocation;
	}
	public String getSysOid() {
		return sysOid;
	}
	
	public void setSysOid(String sysOid) {
		this.sysOid = sysOid;
	}

	public List getIfEntityList() {
		return ifEntityList;
	}
	public List getFdbList() {
		return fdbList;
	}
	public void setFdbList(List fdbList) {
		this.fdbList = fdbList;
	}

	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getDiscoverstatus() {
		return this.discoverstatus;
	}
	public void setDiscoverstatus(int discoverstatus) {
		this.discoverstatus = discoverstatus;
	}
	
	public int getCollecttype() {
		return this.collecttype;
	}
	public void setCollecttype(int collecttype) {
		this.collecttype = collecttype;
	}
	
	public int getOstype() {
		return this.ostype;
	}
	public void setOstype(int ostype) {
		this.ostype = ostype;
	}
	public int getSupperid() {
		return supperid;
	}
	public void setSupperid(int supperid) {
		this.supperid = supperid;
	}
	
	//�����豸�Ľӿڱ�,�������豸��IP�����б�,�����ù���IP
	public void setIfEntityList(List ifEntityList) {
		this.ifEntityList = ifEntityList;
		if(ifEntityList != null && ifEntityList.size()>0){
			aliasIPs = new ArrayList();
			aliasIfEntitys = new ArrayList();
			for(int i=0;i<ifEntityList.size();i++){
				IfEntity ifEntity = (IfEntity)ifEntityList.get(i);
				if(ifEntity == null)continue;
				if(mac == null ){
					if(!ifEntity.getPhysAddress().equals("00:00:00:00:00:00") && ifEntity.getType()==117 )mac = ifEntity.getPhysAddress();
				}
				if(ifEntity.getIpAddress() != null && ifEntity.getIpAddress().trim().length()>0){
					aliasIPs.add(ifEntity.getIpAddress());
					//SysLogger.info(ifEntity.getIpAddress()+"===========");
					aliasIfEntitys.add(ifEntity);
					SysLogger.info(ipAddress+"��IP����Ϊ:"+ifEntity.getIpAddress());
//		    		if(ifEntity.getType() == 24){
//		    			//ΪLOOPBACK��ַ
//		    			if(ifEntity.getIpAddress().indexOf("127.0")<0){
//		    				adminIp = ifEntity.getIpAddress();
//		    				SysLogger.info(ipAddress+"�����ַΪ:"+adminIp);
//		    				//ifEntity = ifEntity;
//		    				//return ifEntity.getIpAddress();
//		    			}
//		    		}
				}
					
				

			}
		}
	}
	
	public List getAliasIPs() {
		return aliasIPs;
	}

	public void setAliasIPs(List aliasIPs) {
		this.aliasIPs = aliasIPs;
	}
	
	public List getAliasIfEntitys() {
		return aliasIfEntitys;
	}

	public void setAliasIfEntitys(List aliasIfEntitys) {
		this.aliasIfEntitys = aliasIfEntitys;
	}
	
	public List getBridgestpList() {
		return bridgestpList;
	}

	public void setBridgestpList(List bridgestpList) {
		this.bridgestpList = bridgestpList;
		if(bridgestpList != null && bridgestpList.size()>0){
			portVSBridgeHash = new Hashtable();
			for(int i=0;i<bridgestpList.size();i++){
				BridgeStpInterface host_bstp = (BridgeStpInterface)bridgestpList.get(i);
				if(portVSBridgeHash.get(host_bstp.getBridgeport())!= null){
					List macs = (List)portVSBridgeHash.get(host_bstp.getBridgeport());
					macs.add(host_bstp.getBridgeport());
					portVSBridgeHash.put(host_bstp.getBridgeport(), macs);
				}else{
					List macs = new ArrayList();
					macs.add(host_bstp.getBridgeport());
					portVSBridgeHash.put(host_bstp.getBridgeport(), macs);
				}
			}
		}
	}
	
	public Hashtable getPortVSBridgeHash() {
		return portVSBridgeHash;
	}

	public void setPortVSBridgeHash(Hashtable portVSBridgeHash) {
		this.portVSBridgeHash = portVSBridgeHash;
	}
	
	public List getCdpList() {
		return cdpList;
	}

	public void setCdpList(List cdpList) {
		this.cdpList = cdpList;
	}
	
	public Hashtable getNdpHash() {
		return ndpHash;
	}

	public void setNdpHash(Hashtable ndpHash) {
		this.ndpHash = ndpHash;
	}
	
	public String toString()
	{
		StringBuffer info = new StringBuffer(100);
		info.append(id);
		if(category==1)
		   info.append(".·����:");
		else if(category==2)
		   info.append(".·�ɽ�����:");
		else if(category==3)
		   info.append(".������:");
		else if(category==4)
		   info.append(".������:");
		else if(category==5)
		   info.append(".��ӡ��:");
		else if(category==6)
		   info.append(".����ǽ:");
		else if(category==100)
		   info.append(".����:");		
		info.append("ip=");
		info.append(ipAddress);
		info.append(",sys_oid=");
		info.append(sysOid);
		info.append(",sys_name=");
		info.append(sysName);
		info.append(",community=");
		info.append(community);	
		info.append(",layer=");
		info.append(layer);	
		return info.toString();		
	}
	public String getSendmobiles() {
		return sendmobiles;
	}	
	public void setSendmobiles(String sendmobiles) {
		this.sendmobiles = sendmobiles;
	}
	public String getSendemail() {
		return sendemail;
	}	
	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}
	public String getSendphone() {
		return sendphone;
	}	
	public void setSendphone(String sendphone) {
		this.sendphone = sendphone;
	}
	public String getBid() {
		return bid;
	}	
	public void setBid(String bid) {
		this.bid = bid;
	}
	public int getEndpoint() {
		return this.endpoint;
	}
	public void setEndpoint(int endpoint) {
		this.endpoint = endpoint;
	}
}
