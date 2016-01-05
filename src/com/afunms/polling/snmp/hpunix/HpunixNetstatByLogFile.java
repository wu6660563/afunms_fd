/*
 * @(#)HpunixNetstatByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.hpunix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.UtilHdx;
/**
 * 
 * ClassName:   HpunixNetstatByLogFile.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 26, 2013 8:51:05 PM
 */
public class HpunixNetstatByLogFile  extends HpunixByLogFile {

	private static final String HPUNIX_NETSTAT_BEGIN_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_NETSTAT_BEGIN_KEYWORD;

    private static final String HPUNIX_NETSTAT_END_KEYWORD = HpunixLogFileKeywordConstant.HPUNIX_NETSTAT_END_KEYWORD;
	
	@Override
	public ObjectValue getObjectValue() {
		String beginStr = HPUNIX_NETSTAT_BEGIN_KEYWORD;
        String endStr = HPUNIX_NETSTAT_END_KEYWORD;
        ObjectValue objectValue = new ObjectValue();
        String netstatContent = getLogFileContent(beginStr, endStr);

        try {
        	Hashtable result = new Hashtable();
        	Vector interfaceVector = new Vector();
            Vector utilhdxVector = new Vector();
            String ipaddress = getNodeDTO().getIpaddress();
            Calendar date = getCalendarInstance();
            List iflist = new ArrayList();
    		List oldiflist = new ArrayList();
    		String[] netstatLineArr = netstatContent.trim().split("\n");
    		String[] netstat_tmpData = null;

    		if (netstatLineArr != null && netstatLineArr.length > 0) {
    			Interfacecollectdata interfacedata = null;

    			// ��ʼѭ������ӿ�
    			for (int k = 1; k < netstatLineArr.length; k++) {
    				String portDesc = "";         	// Name
    				String mtu = "";				// Mtu			
    				String network = "";			// Network		δʹ��
    				String address = "";			// Address		δʹ��
    				String inPackets = "";			// Ipkts		
    				String ierrs = "";				// Ierrs		δʹ��
    				String outPackets = "";			// Opkts		
    				String oerrs = "";				// Oerrs		δʹ��
    				String coll = "";				// Coll			δʹ��
    				
    				netstat_tmpData = netstatLineArr[k].trim().split("\\s++");
    				
    				if(netstat_tmpData != null && netstat_tmpData.length>= 9){
    					portDesc = netstat_tmpData[0].trim();
    					mtu = netstat_tmpData[1].trim();
    					network = netstat_tmpData[2].trim();
    					address = netstat_tmpData[3].trim();
    					inPackets = netstat_tmpData[4].trim();
    					ierrs = netstat_tmpData[5].trim();
    					outPackets = netstat_tmpData[6].trim();
    					oerrs = netstat_tmpData[7].trim();
    					coll = netstat_tmpData[8].trim();
    					
    					Hashtable ifhash = new Hashtable();
    					Hashtable oldifhash = null;// ���������ϴβɼ����
    					
    					boolean hasOldFlag = false;
    					if (oldiflist != null && oldiflist.size() > 0) {
    						for(int j = 0 ; j < oldiflist.size(); j++){
    							Hashtable oldifhash_per = (Hashtable) oldiflist.get(j);
    							if(portDesc.equals(oldifhash_per.get("portDesc"))){
    								oldifhash = oldifhash_per;
    								hasOldFlag = true;
    							}
    						}
    					}
    					if(!hasOldFlag){
    						oldifhash = new Hashtable();
    						oldifhash.put("portDesc", portDesc);
    						oldifhash.put("mtu", mtu);
    						oldifhash.put("network", network);
    						oldifhash.put("address", address);
    						oldifhash.put("inPackets", inPackets);
    						oldifhash.put("ierrs", ierrs);
    						oldifhash.put("outPackets", outPackets);
    						oldifhash.put("oerrs", oerrs);
    						oldifhash.put("coll", coll);
    					}
    					
    					ifhash.put("portDesc", portDesc);
    					ifhash.put("mtu", mtu);
    					ifhash.put("network", network);
    					ifhash.put("address", address);
    					ifhash.put("inPackets", inPackets);
    					ifhash.put("ierrs", ierrs);
    					ifhash.put("outPackets", outPackets);
    					ifhash.put("oerrs", oerrs);
    					ifhash.put("coll", coll);
    					
    					String oldOutPackets = "0";
    					String oldInPackets = "0";
    					String endOutPackets = "0";
    					String endInPackets = "0";
    					
//    					String oldOutBytes = "0";
//    					String oldInBytes = "0";
    					String endOutBytes = "0";
    					String endInBytes = "0";
    					
    					if (oldifhash != null && oldifhash.size() > 0) {
    						if (oldifhash.containsKey("outPackets")) {
    							oldOutPackets = (String) oldifhash.get("outPackets");
    						}
    						try {
    							endOutPackets = (Long.parseLong(outPackets) - Long
    									.parseLong(oldOutPackets))
    									+ "";
    							endOutBytes = (Long.parseLong(outPackets) - Long
    									.parseLong(oldOutPackets)) / 1024 / 300
    									+ "";
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
    						if (oldifhash.containsKey("inPackets")) {
    							oldInPackets = (String) oldifhash.get("inPackets");
    						}
    						try {
    							endInPackets = (Long.parseLong(inPackets) - Long
    									.parseLong(oldInPackets))
    									+ "";
    							endInBytes = (Long.parseLong(inPackets) - Long
    									.parseLong(oldInPackets)) / 1024 / 300
    									+ "";
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
    					}
    					
    					// �˿�����
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("index");
    					interfacedata.setSubentity(k  + "");
    					// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(k + "");
    					interfacedata.setChname("�˿�����");
    					interfaceVector.addElement(interfacedata);
    					
    					// �˿�����
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifDescr");
    					interfacedata.setSubentity(k  + "");
    					// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(portDesc);
    					interfacedata.setChname("�˿�����2");
    					interfaceVector.addElement(interfacedata);
    					
    					// �˿�����
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifname");
    					interfacedata.setSubentity(k  + "");
    					// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(portDesc);
    					interfacedata.setChname("�˿�����2");
    					interfaceVector.addElement(interfacedata);
    					
    					// �˿ڴ���
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifSpeed");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("ÿ���ֽ���");
    					interfacedata.setThevalue(mtu);
    					interfacedata.setChname("");
    					interfaceVector.addElement(interfacedata);
    					
    					// ��ǰ״̬
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifOperStatus");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue("up");
    					interfacedata.setChname("��ǰ״̬");
    					interfaceVector.addElement(interfacedata);
    					
    					// ��ǰ״̬
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifOperStatus");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(1 + "");
    					interfacedata.setChname("��ǰ״̬");
    					interfaceVector.addElement(interfacedata);
    					
    					// �˿��������
    					UtilHdx utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					String chnameBand = "";
    					utilhdx.setEntity("InBandwidthUtilHdx");
    					utilhdx.setThevalue(endInBytes);
    					utilhdx.setSubentity(k  + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/��");
    					utilhdx.setChname(k + "�˿����" + "����");
    					utilhdxVector.addElement(utilhdx);
    					
    					// �˿ڳ�������
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("OutBandwidthUtilHdx");
    					utilhdx.setThevalue(endOutBytes);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/��");
    					utilhdx.setChname(k + "�˿ڳ���" + "����");
    					utilhdxVector.addElement(utilhdx);
    					
    					//������ݰ�
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("ifInPkts");
    					utilhdx.setThevalue(endInPackets);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/��");
    					utilhdx.setChname(k + "�˿�������ݰ�");
    					utilhdxVector.addElement(utilhdx);
    					
    					//�������ݰ�
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("ifOutPkts");
    					utilhdx.setThevalue(endOutPackets);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/��");
    					utilhdx.setChname(k + "�˿ڳ������ݰ�");
    					utilhdxVector.addElement(utilhdx);
    					
    					iflist.add(ifhash);
    					ifhash = null;
    				}
    			}
    		}
    		result.put("iflist", iflist);
    		result.put("interface", interfaceVector);
    		result.put("utilhdx", utilhdxVector);
    		objectValue.setObjectValue(result);
    		objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}

