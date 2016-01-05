/**
 * <p>Description:node of topology,all devices are hosts</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-10
 */

package com.afunms.discovery;

import java.util.*;

import com.afunms.common.util.*;

public class BridgeStpInterface
{    
    protected int id;           //id
    protected String moid; 		//IP��ַ
    protected String port;   	//�˿�dot1dStpPort
    protected String ifindex; 	//�ӿ�����
    protected String bridge;    //�ŵ�ַdot1dStpPortDesignatedBridge
    protected String bridgeport;   //�Ŷ˿�dot1dStpPortDesignatedPort
    protected String vlan;

    
    public BridgeStpInterface()
    {   	  	
    }
    
	BridgeStpInterface(String bridgeport, String vlan) {
		this.bridgeport = bridgeport;
		this.vlan = vlan;
	}
   
	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIfindex() {
		return ifindex;
	}

	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getBridgeport() {
		return bridgeport;
	}

	public void setBridgeport(String bridgeport) {
		this.bridgeport = bridgeport;
	}
	
	public String getVlan() {
		return vlan;
	}

}
