package com.afunms.detail.service.cpuInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.temp.dao.CpuTempDao;
import com.afunms.temp.model.NodeTemp;

public class CpuInfoService {

    private String type;

    private String subtype;

    private String nodeid;

    /**
     * @param type
     * @param subtype
     * @param nodeid
     */
    public CpuInfoService(String nodeid, String type, String subtype) {
        this.nodeid = nodeid;
        this.type = type;
        this.subtype = subtype;
    }

    public CpuInfoService() {
        super();
    }

    public String getCurrCpuAvgInfo() {
        String currCpuAvgInfo = "0";
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            List<NodeTemp> nodeTempList = cpuTempDao.getNodeTempList(nodeid,
                    type, subtype);
            if (nodeTempList != null && nodeTempList.size() > 0) {
                for (int i = 0; i < nodeTempList.size(); i++) {
                    NodeTemp nodeTemp = nodeTempList.get(i);
                    // if("Utilization".equals(nodeTemp.getSubentity()) &&
                    // "avg".equals(nodeTemp.getSindex())){
                    if ("Utilization".equals(nodeTemp.getSubentity())) {
                        currCpuAvgInfo = nodeTemp.getThevalue();
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return currCpuAvgInfo;
    }

    public List<NodeTemp> getCurrPerCpuListInfo() {
        List<NodeTemp> nodeTempList = null;
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            nodeTempList = cpuTempDao.getCurrPerCpuList(nodeid, type, subtype);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return nodeTempList;
    }

    public List getCpuPerListInfo() {
        List cpuperList = null;
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            cpuperList = cpuTempDao.getCpuPerListInfo(nodeid, type, subtype);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return cpuperList;
    }

    public Vector getCpuInfo() {
        Vector cpuInfoVector = null;
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            cpuInfoVector = cpuTempDao.getCpuInfo(nodeid, type, subtype);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return cpuInfoVector;
    }

    public List getPerCpuList(String nodeids) {
        List cpuList = null;
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            cpuList = cpuTempDao.getPerCpuList(nodeids);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return cpuList;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * 根据监控的Node列表得到平均cpu利用率列表
     * 
     * @param monitornodelist
     * @return
     */
    public List<NodeTemp> getCpuPerListInfo(List monitornodelist) {
        if (monitornodelist == null || monitornodelist.size() == 0) {
            return null;
        }
        List<NodeTemp> nodeTempList = null;
        CpuTempDao cpuTempDao = new CpuTempDao();
        try {
            nodeTempList = cpuTempDao.getCurrPerCpuList(monitornodelist);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            cpuTempDao.close();
        }
        return nodeTempList;
    }

    public Hashtable<String, String> getCurDayCPUValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getCPUValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<String, String> getCPUValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostCollectData hostmanager = new HostCollectDataManager();
        Hashtable CPUValueHashtable = new Hashtable();
        try {
            CPUValueHashtable = hostmanager.getCategory(ipaddress, "CPU", "Utilization", startTime, toTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CPUValueHashtable;
    }

    public Hashtable<String, Hashtable<String, String>> getCPUDetailValueHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getCPUDetailValueHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<String, Hashtable<String, String>> getCPUDetailValueHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostCollectData hostmanager = new HostCollectDataManager();
        Hashtable CPUDetailValueHashtable = new Hashtable();
        try {
            CPUDetailValueHashtable = hostmanager.getCpuDetail(ipaddress,  startTime, toTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CPUDetailValueHashtable;
    }
}
