package com.afunms.system.manage;

import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;

public class SnmpPingManager extends BaseManager implements ManagerInterface{
	
	   private Snmp snmp = null;
       private Address targetAddress = null;
       public void initComm(String ip) throws IOException {

              // ����Agent����IP�Ͷ˿�

              targetAddress = GenericAddress.parse("udp:"+ip+"/161");

              TransportMapping transport = new DefaultUdpTransportMapping();

              snmp = new Snmp(transport);

              transport.listen();

       }
       public String sendPDU(String name,int version) throws IOException {
    	   
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
              }
              return snmpping;
        
       }
   
       /**
        * ����Ƿ���SNMP����
        * @return
        */
	public String snmpPing()
	{  
		String ip = getParaValue("ipaddress");
		String name = getParaValue("name");
		int version = getParaIntValue("version");
		SnmpPingManager ping = new SnmpPingManager();
		try {
			ping.initComm(ip);
			String snmpping=ping.sendPDU(name,version);
			request.setAttribute("ipaddress", ip);
			request.setAttribute("name", name);
			request.setAttribute("snmpping", snmpping);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/tool/snmppinglist.jsp?version="+version;
	}
	public String execute(String action) {
		// TODO Auto-generated method stub
		if(action.equals("ping"))
            return snmpPing();
		return null;
	}
	

}
