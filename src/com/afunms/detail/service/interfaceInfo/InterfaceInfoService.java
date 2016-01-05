package com.afunms.detail.service.interfaceInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostCollectDataDay;
import com.afunms.polling.impl.HostCollectDataDayManager;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

public class InterfaceInfoService {

    private String type;

    private String subtype;

    private String nodeid;

    private HostNode hostNode;

    /**
     * @param type
     * @param subtype
     * @param nodeid
     */
    public InterfaceInfoService(String nodeid, String type, String subtype) {
        this.nodeid = nodeid;
        this.type = type;
        this.subtype = subtype;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            hostNode = (HostNode)hostNodeDao.findByID(nodeid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
    }

    public InterfaceInfoService() {
    }

    public List<InterfaceInfo> getCurrAllInterfaceInfo() {
        return getCurrAllInterfaceInfo(null);
    }

    public List<InterfaceInfo> getCurrAllInterfaceInfo(String[] subentities) {
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        List<InterfaceInfo> interfaceInfoList = null;
        try {
            interfaceInfoList = interfaceTempDao.getInterfaceInfoList(nodeid,
                    type, subtype, subentities);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return interfaceInfoList;
    }

    /**
     * 
     * @param subentities
     * @param sindexs
     *            索引
     * @return
     */
    public List<InterfaceInfo> getCurrAllInterfaceInfo(String[] subentities,
            String[] sindexs, String starttime, String totime) {
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        List<InterfaceInfo> interfaceInfoList = null;
        try {
            interfaceInfoList = interfaceTempDao.getInterfaceInfoList(nodeid,
                    type, subtype, subentities, sindexs, starttime, totime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return interfaceInfoList;
    }

    /**
     * 
     * @param subentities
     * @return
     */
    public List<InterfaceInfo> getCurrAllInterfaceInfos(String[] subentities) {
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        List<InterfaceInfo> interfaceInfoList = null;
        try {
            interfaceInfoList = interfaceTempDao.getInterfaceInfosList(nodeid,
                    type, subtype, subentities);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return interfaceInfoList;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public List<InterfaceInfo> getInterfaceInfoBySindes(String[] sindexs,
            String starttime, String totime) {
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        List<InterfaceInfo> interfaceInfoList = null;
        try {
            interfaceInfoList = interfaceTempDao.getInterfaceInfoBySindes(
                    nodeid, type, subtype, sindexs, starttime, totime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return interfaceInfoList;
    }

    /**
     * 得到端口流速信息
     * 
     * @return
     */
    public Vector getInterfaceInfo(String[] subentitys) {
        Vector retVector = new Vector();
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        try {
            retVector = interfaceTempDao.getInterfaceInfo(nodeid, type,
                    subtype, subentitys);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return retVector;
    }

    /**
     * 得到端口流速信息 solaris
     * 
     * @return
     */
    public List getNetflowInfo() {
        List retList = new ArrayList();
        InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
        try {
            retList = interfaceTempDao.getNetflowInfo(nodeid, type, subtype);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfaceTempDao.close();
        }
        return retList;
    }

    /**
     * 根据监控的Node列表得到平均端口流速列表
     * 
     * @param monitorNodelist
     * @return
     */
    public List<NodeTemp> getInterfaceInfo(List monitorNodelist) {
        if (monitorNodelist == null || monitorNodelist.size() == 0) {
            return null;
        }
        List<NodeTemp> nodeTempList = new ArrayList<NodeTemp>();
        for (Object object : monitorNodelist) {
            HostNode hostNode = (HostNode) object;
            InterfaceTempDao interfaceTempDao = new InterfaceTempDao(hostNode.getIpAddress());
            
            try {
                nodeTempList.addAll(interfaceTempDao
                        .getCurrAllInterfaceInfo(monitorNodelist));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                interfaceTempDao.close();
            }
        }
        return nodeTempList;
    }

    public Hashtable<String, Integer> getCurDayAllBandwidthUtilHdxHashtableInfo(String ipaddress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDay = simpleDateFormat.format(new Date());
        String startTime = currDay + " 00:00:00";
        String toTime = currDay + " 23:59:59";
        return getAllBandwidthUtilHdxHashtableInfo(ipaddress, startTime, toTime);
    }

    public Hashtable<String, Integer> getAllBandwidthUtilHdxHashtableInfo(String ipaddress,
            String startTime, String toTime) {
        I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
        //获取上下行流量的最大值和平均口值
        Hashtable curDayAllBandwidthUtilHdxHashtable = new Hashtable();
        try {
            curDayAllBandwidthUtilHdxHashtable = daymanager.getAllAvgAndMaxHisHdx(ipaddress, startTime, toTime);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return curDayAllBandwidthUtilHdxHashtable;
    }
}
