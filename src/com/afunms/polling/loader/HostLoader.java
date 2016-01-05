/**
 * <p>Description:loading host node</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-28
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.ArrayList;
import java.util.List;

import com.afunms.application.dao.StorageDao;
import com.afunms.common.base.BaseVo;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.UPSNode;
import com.afunms.sysset.dao.DBBackupDao;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.dao.TelnetDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.model.TelnetConfig;

public class HostLoader extends NodeLoader {
    // private HostInterfaceDao niDao;
    private List TelnetConfigs;

    public HostLoader() {
        // niDao = new HostInterfaceDao();
    }

    public void loading() {

        List nodeList = new ArrayList();

        HostNodeDao nodeDao = new HostNodeDao();

        try {
            nodeList = nodeDao.loadOrderByIP();
            clearRubbish(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeDao.close();
        }

        for (int i = 0; i < nodeList.size(); i++) {
            HostNode node = (HostNode) nodeList.get(i);
            // SysLogger.info(node.getIpAddress()+"===="+node.getStatus());
            if (node.getCategory() == 5)
                continue; // 暂时不对打印机进行监控

            loadOne(node);
        }// all hosts are loaded
        // niDao.close();
        close();

        loadLinks();
    }

    /**
     * 清空内存中垃圾
     * 
     * @param baseVoList
     * @author makewen
     * @date Apr 20, 2011
     */
    public void clearRubbish(List baseVoList) {

        List nodeList = PollingEngine.getInstance().getNodeList();
        for (int index = 0; index < nodeList.size(); index++) {
            if (nodeList.get(index) instanceof Host) {
                Host node = (Host) nodeList.get(index);
                if (baseVoList == null) {
                    nodeList.remove(node);
                } else {
                    boolean flag = false;
                    for (int j = 0; j < baseVoList.size(); j++) {
                        HostNode hostNode = (HostNode) baseVoList.get(j);
                        if (node.getId() == hostNode.getId()) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        nodeList.remove(node);
                    }
                }
            }
        }
    }

    public Host loadOneByID(String nodeid) {
        HostNode hostNode = null;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            hostNode = (HostNode) hostNodeDao.findByID(nodeid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        if (hostNode == null) {
            return null;
        }
        return loadOneByBaseVo(hostNode);
    }

    public Host loadOneByIP(String ipaddress) {
        HostNode hostNode = null;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            hostNode = (HostNode) hostNodeDao.findByIpaddress(ipaddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        if (hostNode == null) {
            return null;
        }
        return loadOneByBaseVo(hostNode);
    }

    public Host loadOneByBaseVo(BaseVo baseVo) {
     // --------------(1)加载基础数据-------------------
        HostNode hostNode = (HostNode) baseVo;
        Host host = new Host();
        host.setId(hostNode.getId());
        host.setAssetid(hostNode.getAssetid());
        host.setLocation(hostNode.getLocation());
        host.setSysName(hostNode.getSysName());
        host.setCategory(hostNode.getCategory());
        host.setCommunity(hostNode.getCommunity());
        host.setWritecommunity(hostNode.getWriteCommunity());
        host.setSnmpversion(hostNode.getSnmpversion());
        host.setTransfer(hostNode.getTransfer());
        host.setIpAddress(hostNode.getIpAddress());
        host.setLocalNet(hostNode.getLocalNet());
        host.setNetMask(hostNode.getNetMask());
        host.setAlias(hostNode.getAlias());
        host.setSysDescr(hostNode.getSysDescr());
        host.setSysOid(hostNode.getSysOid());
        host.setType(hostNode.getType());
        host.setManaged(hostNode.isManaged());
        host.setDiscoverstatus(hostNode.getDiscovertatus());
        host.setOstype(hostNode.getOstype());
        host.setCollecttype(hostNode.getCollecttype());
        host.setSysLocation(hostNode.getSysLocation());
        host.setSendemail(hostNode.getSendemail());
        host.setSendmobiles(hostNode.getSendmobiles());
        host.setSendphone(hostNode.getSendphone());
        host.setBid(hostNode.getBid());
        host.setEndpoint(hostNode.getEndpoint());
        host.setMac(hostNode.getBridgeAddress());
        host.setStatus(0);
        host.setSupperid(hostNode.getSupperid());

        // 注释该下面已未使用
//        TelnetConfig tc = findTelnetConfig(host.getId());
//        if (tc != null) {
//            host.setUser(tc.getUser());
//            host.setPassword(tc.getPassword());
//            host.setPrompt(tc.getPrompt());
//        }
//
//        // ---------------(2)加载接口数据-------------------
//        HostInterfaceDao niDao = new HostInterfaceDao();
//        try {
//            host.setInterfaceHash(niDao.loadInterfaces(host.getId()));
//        } catch (Exception e) {
//
//        } finally {
//            niDao.close();
//        }
//
//        // ---------------(3)加载被监视对象-------------------
//        List moidList = new ArrayList(5);
//        List nmList = new ArrayList();
//        NodeMonitorDao nodeMonitorDao = getNmDao();
//        try {
//            nmList = nodeMonitorDao.loadByNodeID(host.getId());
//        } catch (Exception e) {
//
//        } finally {
//            nodeMonitorDao.close();
//        }
//
//        // List nmList = getNmDao().loadByNodeID(host.getId());
//        for (int j = 0; j < nmList.size(); j++) {
//            NodeMonitor nm = (NodeMonitor) nmList.get(j);
//            // MonitoredItem item = MonitorFactory.createItem(nm.getMoid());
//            // item.loadSelf(nm);
//            // moidList.add(item);
//        } // all monitor items are loaded
//        // host.setMoidList(moidList);
//        host.setMoidList(nmList);
//        // SysLogger.info("add============"+host.getId()+"==="+host.getIpAddress());
//        // PollingEngine.getInstance().addNode(host);

        Node node = PollingEngine.getInstance().getNodeByID(host.getId());
        if (node != null) {
            try {
                PollingEngine.getInstance().getNodeList().remove(node);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        PollingEngine.getInstance().addNode(host);
        return host;
    }
    
    
    
    
    
    
    
    
    
    public void loadOne(BaseVo baseVo) {
        // --------------(1)加载基础数据-------------------
        HostNode hostNode = (HostNode) baseVo;
        Host host = new Host();
        host.setId(hostNode.getId());
        host.setAssetid(hostNode.getAssetid());
        host.setLocation(hostNode.getLocation());
        host.setSysName(hostNode.getSysName());
        host.setCategory(hostNode.getCategory());
        host.setCommunity(hostNode.getCommunity());
        host.setWritecommunity(hostNode.getWriteCommunity());
        host.setSnmpversion(hostNode.getSnmpversion());
        host.setTransfer(hostNode.getTransfer());
        host.setIpAddress(hostNode.getIpAddress());
        host.setLocalNet(hostNode.getLocalNet());
        host.setNetMask(hostNode.getNetMask());
        host.setAlias(hostNode.getAlias());
        host.setSysDescr(hostNode.getSysDescr());
        host.setSysOid(hostNode.getSysOid());
        host.setType(hostNode.getType());
        host.setManaged(hostNode.isManaged());
        host.setDiscoverstatus(hostNode.getDiscovertatus());
        host.setOstype(hostNode.getOstype());
        host.setCollecttype(hostNode.getCollecttype());
        host.setSysLocation(hostNode.getSysLocation());
        host.setSendemail(hostNode.getSendemail());
        host.setSendmobiles(hostNode.getSendmobiles());
        host.setSendphone(hostNode.getSendphone());
        host.setBid(hostNode.getBid());
        host.setEndpoint(hostNode.getEndpoint());
        host.setMac(hostNode.getBridgeAddress());
        host.setStatus(0);
        host.setSupperid(hostNode.getSupperid());
        // SysLogger.info(host.getIpAddress()+"==========="+host.isManaged());

        TelnetConfig tc = findTelnetConfig(host.getId());
        if (tc != null) {
            host.setUser(tc.getUser());
            host.setPassword(tc.getPassword());
            host.setPrompt(tc.getPrompt());
        }

        // ---------------(2)加载接口数据-------------------
        HostInterfaceDao niDao = new HostInterfaceDao();
        try {
            host.setInterfaceHash(niDao.loadInterfaces(host.getId()));
        } catch (Exception e) {

        } finally {
            niDao.close();
        }

        // ---------------(3)加载被监视对象-------------------
        List moidList = new ArrayList(5);
        List nmList = new ArrayList();
        NodeMonitorDao nodeMonitorDao = getNmDao();
        try {
            nmList = nodeMonitorDao.loadByNodeID(host.getId());
        } catch (Exception e) {

        } finally {
            nodeMonitorDao.close();
        }

        // List nmList = getNmDao().loadByNodeID(host.getId());
        for (int j = 0; j < nmList.size(); j++) {
            NodeMonitor nm = (NodeMonitor) nmList.get(j);
            // MonitoredItem item = MonitorFactory.createItem(nm.getMoid());
            // item.loadSelf(nm);
            // moidList.add(item);
        } // all monitor items are loaded
        // host.setMoidList(moidList);
        host.setMoidList(nmList);
        // SysLogger.info("add============"+host.getId()+"==="+host.getIpAddress());
        // PollingEngine.getInstance().addNode(host);

        Node node = PollingEngine.getInstance().getNodeByID(host.getId());
        if (node != null) {
            PollingEngine.getInstance().getNodeList().remove(node);
        }
        PollingEngine.getInstance().addNode(host);
    }

    public void close() {
        super.close();
        // niDao.close();
    }

    private TelnetConfig findTelnetConfig(int nodeId) {
        if (TelnetConfigs == null) {
            TelnetDao td = new TelnetDao();
            TelnetConfigs = td.loadAll();
        }

        TelnetConfig result = null;
        for (int i = 0; i < TelnetConfigs.size(); i++) {
            TelnetConfig tc = (TelnetConfig) TelnetConfigs.get(i);
            if (nodeId == tc.getNodeID()) {
                result = tc;
                break;
            }
        }
        return result;
    }

    /**
     * 载入链路信息
     */
    private void loadLinks() {
        LinkDao dao = new LinkDao();
        List list = dao.loadAll();
        dao.close();
        List linkList = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            Link vo = (Link) list.get(i);
            LinkRoad link = new LinkRoad();
            link.setId(vo.getId());
            link.setStartId(vo.getStartId());
            link.setLinkName(vo.getLinkName());// yangjun add
            link.setStartIp(vo.getStartIp());
            link.setStartIndex(vo.getStartIndex());
            link.setStartDescr(vo.getStartDescr());

            link.setEndId(vo.getEndId());
            link.setEndIp(vo.getEndIp());
            link.setEndIndex(vo.getEndIndex());
            link.setEndDescr(vo.getEndDescr());
            link.setAssistant(vo.getAssistant());
            link.setType(vo.getType());
            link.setMaxSpeed(vo.getMaxSpeed());// yangjun add
            link.setMaxPer(vo.getMaxPer());// yangjun add
            linkList.add(link);
        }
        PollingEngine.getInstance().setLinkList(null);
        PollingEngine.getInstance().setLinkList(linkList);
        // System.out.println(PollingEngine.getInstance().getLinkList().size()+"----");

    }
}