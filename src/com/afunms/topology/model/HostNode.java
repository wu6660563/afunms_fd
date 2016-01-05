/**
 * <p>Description:mapping table NMS_TOPO_NODE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.model;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.discovery.IfEntity;

public class HostNode extends BaseVo
{
	private int id; 
	private String assetid;
	private String location;
	private String ipAddress;
	private long ipLong;	
	private String sysName;
	private String alias;
	private String netMask;  
	private String sysDescr;
	protected String sysLocation;  //系统位置
    protected String sysContact;  //系统联系人
	
	private String sysOid;  
	private String community;
	private String writeCommunity; 
	private int snmpversion;
	private int transfer;
	private String type;     //类型
	private int category;  
	private int localNet;	
	private boolean managed;
	private String bridgeAddress;
	private int status;//状态
	private int superNode;    //上一级节点的id
	private int layer;        //层
	private int discoverstatus;//存储多次重复发现的状态
	private int collecttype;//数据采集方式  1:snmp 2:shell
	private int ostype;//操作系统类型  1:snmp 2:shell
	private String sendmobiles;
	private String sendemail;
	private String sendphone;
	private String bid;
	private int endpoint;//末端设备
	private int supperid;//供应商id snow add at 2010-5-18

	protected List ifEntityList; //所有端口
	protected List aliasIPs;//IP别名
	protected List aliasIfEntitys;//IP别名
	protected String mac;       //MAC地址
	
	public int getSupperid() {
		return supperid;
	}

	public void setSupperid(int supperid) {
		this.supperid = supperid;
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
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public int getSuperNode() {
		return superNode;
	}

	public void setSuperNode(int superNode) {
		this.superNode = superNode;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
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
	}
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	public String getNetMask() {
		return netMask;
	}
	public void setNetMask(String netMask) {
		this.netMask = netMask;
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
	public String getWriteCommunity() {
		return writeCommunity;
	}
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
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

	public int getLocalNet() {
		return localNet;
	}
	public void setLocalNet(int localNet) {
		this.localNet = localNet;
	} 
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getIpLong() {
		return ipLong;
	}
	public void setIpLong(long ipLong) {
		this.ipLong = ipLong;
	}	
	public String getBridgeAddress() {
		return bridgeAddress;
	}

	public void setBridgeAddress(String bridgeAddress) {
		this.bridgeAddress = bridgeAddress;
	}
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getDiscovertatus() {
		return this.discoverstatus;
	}
	public void setDiscovertatus(int discoverstatus) {
		this.discoverstatus = discoverstatus;
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

	
	/**
     * getMac:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getMac() {
        return mac;
    }

    /**
     * setMac:
     * <p>
     *
     * @param   mac
     *          -
     * @since   v1.01
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    
    /**
     * getDiscoverstatus:
     * <p>
     *
     * @return  int
     *          -
     * @since   v1.01
     */
    public int getDiscoverstatus() {
        return discoverstatus;
    }

    /**
     * getIfEntityList:
     * <p>
     *
     * @return  List
     *          -
     * @since   v1.01
     */
    public List getIfEntityList() {
        return ifEntityList;
    }

    /**
     * getAliasIPs:
     * <p>
     *
     * @return  List
     *          -
     * @since   v1.01
     */
    public List getAliasIPs() {
        return aliasIPs;
    }

    /**
     * getAliasIfEntitys:
     * <p>
     *
     * @return  List
     *          -
     * @since   v1.01
     */
    public List getAliasIfEntitys() {
        return aliasIfEntitys;
    }

    //设置设备的接口表,并设置设备的IP别名列表,并设置管理IP
    public void setIfEntityList(List ifEntityList) {
        this.ifEntityList = ifEntityList;
        if (ifEntityList != null && ifEntityList.size() > 0) {
            aliasIPs = new ArrayList();
            aliasIfEntitys = new ArrayList();
            for(int i=0;i<ifEntityList.size();i++){
                IfEntity ifEntity = (IfEntity)ifEntityList.get(i);
                if (ifEntity == null)continue;
                if (mac == null ){
                    if(!ifEntity.getPhysAddress().equals("00:00:00:00:00:00") && ifEntity.getType()==117 )mac = ifEntity.getPhysAddress();
                }
                if(ifEntity.getIpAddress() != null && ifEntity.getIpAddress().trim().length()>0){
                    aliasIPs.add(ifEntity.getIpAddress());
                    aliasIfEntitys.add(ifEntity);
                    SysLogger.info(ipAddress+"的IP别名为:"+ifEntity.getIpAddress());
                }
            }
        }
    }
}