package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import com.afunms.application.dao.WasConfigDao;
import com.afunms.application.model.WasConfig;
import com.afunms.application.wasmonitor.UrlConncetWas;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.om.Pingcollectdata;

public class WasDataCollector {

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public WasDataCollector() {
    }

    public void collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        try {
            WasConfig wasconf = null;
            String nodeid = nodeGatherIndicators.getNodeid();
            WasConfigDao dao = new WasConfigDao();
            try {
                wasconf = (WasConfig) dao.findByID(nodeid);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            if (wasconf == null) {
                return;
            }
            String ipaddress = wasconf.getIpaddress();
            UrlConncetWas conWas = new UrlConncetWas();
            boolean collectWasIsOK = false;
            String pingValue = "0";
            try{
                collectWasIsOK = conWas.connectWasIsOK(wasconf.getIpaddress(),wasconf.getPortnum());
            }catch(Exception e){
                //e.printStackTrace();
            }
            if(collectWasIsOK){
                try {
                    pingValue = "100";
                    Hashtable gatherHash = new Hashtable();
                    gatherHash.put("system", gatherHash);
                    gatherHash.put("jdbc", gatherHash);
                    gatherHash.put("session", gatherHash);
                    gatherHash.put("jvm", gatherHash);
                    gatherHash.put("cache", gatherHash);
                    gatherHash.put("thread", gatherHash);
                    gatherHash.put("orb", gatherHash);
                    gatherHash.put("service", gatherHash);
                    conWas.ConncetWas(wasconf.getIpaddress(), String
                            .valueOf(wasconf.getPortnum()), "", "", wasconf
                            .getVersion(), gatherHash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Pingcollectdata hostdata = null;
            hostdata = new Pingcollectdata();
            hostdata.setIpaddress(ipaddress);
            Calendar date = Calendar.getInstance();
            hostdata.setCollecttime(date);
            hostdata.setCategory("WasPing");
            hostdata.setEntity("Utilization");
            hostdata.setSubentity("ConnectUtilization");
            hostdata.setRestype("dynamic");
            hostdata.setUnit("%");
            hostdata.setThevalue(pingValue);
            WasConfigDao wasconfigdao = new WasConfigDao();
            try {
                wasconfigdao.createHostData(wasconf, hostdata);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                wasconfigdao.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
