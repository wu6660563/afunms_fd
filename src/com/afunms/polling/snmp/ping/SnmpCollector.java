package com.afunms.polling.snmp.ping;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SnmpUtil;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;

public class SnmpCollector {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public SnmpCollector() {
    }
    
    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        
     //   System.out.println("������������������������ʼִ��SNMP���"+nodeGatherIndicators.getAlias());
        
        Hashtable returnhash = new Hashtable();
        HostCollectDataManager hostdataManager = new HostCollectDataManager();
        Vector vector = null;
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }

        int result = 0;
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
            
        String ipAddress = nodeDTO.getIpaddress();
        if(ipAddress == null || "".equals(ipAddress)){
            return null;
        }
       
        // ��ȡ�豸��������
        String community =  node.getCommunity();
        if(community == null || "".equals(community)){
            return null;
        }
        
        String value = SnmpUtil.getInstance().getSysOid(ipAddress, community);
        if (value == null) {
            value = "";
        }
        value = value.trim();
        
     //   System.out.println("SNMP�ɼ���-----SnmpCollector collect_Data");
        /**
         * �ж�snmp�Ƿ�򿪣�δ��������澯��Ϣ
         */
           try{
                  AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                  List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                          String.valueOf(node.getId()), nodeDTO.getType(),
                          nodeDTO.getSubtype(), "snmp");
                  for (int i = 0; i < list.size(); i++) {
                      AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode) list
                              .get(i);
                      // ��SNMP�澯���
                      
                    //  System.out.println("SNMP�ɼ���-----SnmpCollector"+alarmIndicatorsnode.getName());
                      CheckEventUtil checkutil = new CheckEventUtil();
//                       checkutil.checkEvent(node, alarmIndicatorsnode, alarmIndicatorsnode.getAlarm_info());
                      checkutil.createSNMPEventList(node, alarmIndicatorsnode, value);
                  }
           }
           catch(Exception e){
               
           }
        
        return returnhash;
    }

}
