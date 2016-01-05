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

public class CdpCachEntryInterface
{    
    protected int id;           //id
    protected String ip; 		//IP��ַ
    protected String portdesc;  //�˿�dot1dStpPort
    protected String ifindex; 	//�ӿ�����

    
    public CdpCachEntryInterface()
    {   	  	
    }
    
  
   
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPortdesc() {
		return portdesc;
	}

	public void setPortdesc(String portdesc) {
		this.portdesc = portdesc;
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
}
