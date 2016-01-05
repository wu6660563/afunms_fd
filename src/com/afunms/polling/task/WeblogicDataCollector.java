package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.manage.WeblogicManager;
import com.afunms.application.model.WebConfig;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.application.weblogicmonitor.WeblogicNormal;
import com.afunms.application.weblogicmonitor.WeblogicServer;
import com.afunms.application.weblogicmonitor.WeblogicSnmp;

import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.Constant;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessWeblogicData;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WeblogicDataCollector {
    /**
     * 
     */
    private Hashtable sendeddata = ShareData.getSendeddata();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WeblogicDataCollector() {
    }

    public void collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        WeblogicConfig weblogicconf = null;
        WeblogicConfigDao configdao = new WeblogicConfigDao();
        try {
            weblogicconf = (WeblogicConfig) configdao.findByID(nodeGatherIndicators.getNodeid());
        } catch (Exception e) {

        } finally {
            configdao.close();
        }
        if (weblogicconf == null || weblogicconf.getMon_flag() == 0) {
            return;
        }
        WeblogicSnmp weblogicsnmp = null;
        weblogicsnmp = new WeblogicSnmp(weblogicconf.getIpAddress(),
                weblogicconf.getCommunity(), weblogicconf.getPortnum());
        String ipaddress = weblogicconf.getIpAddress();
        Hashtable gatherHash = new Hashtable();
        gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
        Hashtable hash = null;
        hash=weblogicsnmp.collectData(gatherHash);
        if(hash == null) {
            hash = new Hashtable();
        }
        List normalValue = (List)hash.get("normalValue");
        int flag = 0;
        if(normalValue != null && normalValue.size()>0){
            for(int i=0;i<normalValue.size();i++){
                WeblogicNormal normal = (WeblogicNormal)normalValue.get(i);
                if(!normal.getDomainActive().equals("2")){
                    flag = 1;
                }
            }
        }
        Pingcollectdata hostdata=null;
        hostdata=new Pingcollectdata();
        hostdata.setIpaddress(ipaddress);
        Calendar date=Calendar.getInstance();
        hostdata.setCollecttime(date);
        hostdata.setCategory("WeblogicPing");
        hostdata.setEntity("Utilization");
        hostdata.setSubentity("ConnectUtilization");
        hostdata.setRestype("dynamic");
        hostdata.setUnit("%");
        if (flag == 1) {
            hostdata.setThevalue("100");  
        } else {
            hostdata.setThevalue("100");  
        }
        
        WeblogicConfigDao weblogicconfigdao=new WeblogicConfigDao();
        try{
            weblogicconfigdao.createHostData(hostdata);                                         
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            weblogicconfigdao.close();
        }
        Hashtable weblogicdatas = new Hashtable();
        weblogicdatas.put(ipaddress, hash);
        List weblogicConfigs = new ArrayList();
        weblogicConfigs.add(weblogicconf);
        ProcessWeblogicData processWeblogicData = new ProcessWeblogicData();
        processWeblogicData.saveWeblogicData(weblogicConfigs, weblogicdatas);

        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(weblogicconf.getId() + "", Constant.TYPE_MIDDLEWARE, Constant.TYPE_MIDDLEWARE_SUBTYPE_WEBLOGIC);
        CheckEventUtil checkEventUtil = new CheckEventUtil();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                AlarmIndicatorsNode indicatorsNode = (AlarmIndicatorsNode) list.get(i);
                if ("ping".equals(indicatorsNode.getName())) {
                    checkEventUtil.checkEvent(weblogicconf, indicatorsNode, hostdata.getThevalue());
                }
            }
        }
    }

}
