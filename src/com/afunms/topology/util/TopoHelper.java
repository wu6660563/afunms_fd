/**
 * <p>Description:topo hepler</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-09-20
 */

package com.afunms.topology.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.node.model.NodeDomain;
import com.afunms.node.service.DataSendService;
import com.afunms.node.service.NodeDomainService;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.loader.HostLoader;
import com.afunms.portscan.dao.PortScanDao;
import com.afunms.system.model.User;
import com.afunms.topology.model.*;
import com.afunms.topology.dao.*;
import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.util.ProcessGroupConfigurationUtil;
import com.afunms.common.util.*;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.dao.NetNodeCfgFileDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.dataArchiving.service.DataArchivingService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;

public class TopoHelper {
    private com.afunms.discovery.Host host;
    
    public static List<String> TABLE_NAME_LIST = DataArchivingService.TABLE_NAME_LIST;
    
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 增加一台主机
     */
    public int addHost(String ipAddress, String alias, String community,
            String writecommunity, int category) {
        HostNodeDao tmpDao = new HostNodeDao();
        List tmpList = null;
        try {
            tmpList = tmpDao.findByCondition("ip_address", ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmpDao.close();
        }
        if (tmpList.size() > 0)
            return -1; // IP已经存在
        if (NetworkUtil.ping(ipAddress) == 0)
            return -2; // ping不通
        if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
            return -3; // 不支持snmp

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // 以下把新增加的主机的数据加入数据库
        {
            id = KeyGenerator.getInstance().getNextKey();
            host.setId(id);

            host.setCategory(category);
            host.setCommunity(community);
            host.setWritecommunity(writecommunity);
            host.setAlias(alias);
            host.setIpAddress(ipAddress);
            host.setSysOid(SnmpUtil.getInstance().getSysOid(ipAddress,
                    community));
            // host.setSysDescr(SnmpUtil.getInstance().getSysDescr(ipAddress,community));
            host.setIfEntityList(SnmpUtil.getInstance().getIfEntityList(
                    ipAddress, community, category));
            // host.setSysName(SnmpUtil.getInstance().getSysName(ipAddress,community));
            host.setLocalNet(0);
            host.setNetMask("255.255.255.0");
            host.setDiscoverstatus(-1);
            SnmpUtil snmp = SnmpUtil.getInstance();
            SysLogger.info("开始获取设备:" + host.getIpAddress() + "的系统名称");
            Hashtable sysGroupProperty = snmp.getSysGroup(host.getIpAddress(),
                    host.getCommunity());
            if (sysGroupProperty != null) {
                host.setSysDescr((String) sysGroupProperty.get("sysDescr"));
                // newNode.setsys((String)sysGroupProperty.get("sysUpTime"));
                host.setSysContact((String) sysGroupProperty.get("sysContact"));
                host.setSysName((String) sysGroupProperty.get("sysName"));
                host.setSysLocation((String) sysGroupProperty
                        .get("sysLocation"));
            }

            SubnetDao netDao = new SubnetDao();
            List netList = null;
            try {
                netList = netDao.loadAll(); // 找出它属于哪个子网
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                netDao.close();
            }
            for (int i = 0; i < netList.size(); i++) {
                Subnet net = (Subnet) netList.get(i);
                if (NetworkUtil.isValidIP(net.getNetAddress(),
                        net.getNetMask(), ipAddress)) {
                    host.setLocalNet(net.getId());
                    host.setNetMask(net.getNetMask());
                    break;
                }
            }
            List hostList = new ArrayList(1);
            hostList.add(host);
            DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
            try {
                dcDao.addHostDataByHand(hostList);
                dcDao.addInterfaceData(hostList);
                dcDao.addMonitor(hostList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dcDao.close();
            }

            result = id; // 数据成功插入,返回ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // 如果增加数据失败则不继续
        HostNodeDao dao = new HostNodeDao();
        try // 以下把新数据载入内存(与PollingInitializtion.loadHost()差不多)
        {

            HostNode vo = (HostNode) dao.findByID(String.valueOf(id));
            HostLoader loader = new HostLoader();
            loader.loadOne(vo);
            loader.close();
            SysLogger.info("成功增加一台主机,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        } finally {
            dao.close();
        }
        return result;
    }

    /**
     * 增加一台主机
     */
    public int addHost(String ipAddress, String alias, String community,
            String writecommunity, int category, int ostype, int collecttype) {
        HostNodeDao tmpDao = new HostNodeDao();
        List tmpList = tmpDao.findByCondition("ip_address", ipAddress);
        if (tmpList.size() > 0)
            return -1; // IP已经存在
        /*
         * 这里为了测试用,目前PING不通的设备也可以加进来
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping不通
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP采集方式
            String snmpversion = "";
            snmpversion = ResourceCenter.getInstance().getSnmpversion();
            int default_version = 0;
            if (snmpversion.equals("v1")) {
                default_version = org.snmp4j.mp.SnmpConstants.version1;
            } else if (snmpversion.equals("v2")) {
                default_version = org.snmp4j.mp.SnmpConstants.version2c;
            } else if (snmpversion.equals("v1+v2")) {
                default_version = org.snmp4j.mp.SnmpConstants.version1;
            } else if (snmpversion.equals("v2+v1")) {
                default_version = org.snmp4j.mp.SnmpConstants.version2c;
            }

            // SnmpUtil.getInstance().getSysOid(coreIp,community);
            if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
                return -3; // 不支持snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // 以下把新增加的主机的数据加入数据库
        {
            id = KeyGenerator.getInstance().getNextKey();
            host.setId(id);
            host.setCategory(category);
            host.setOstype(ostype);
            host.setCollecttype(collecttype);
            if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
                host.setCommunity(community);
                host.setWritecommunity(writecommunity);
                host.setAlias(alias);
                host.setIpAddress(ipAddress);
                host.setSysOid(SnmpUtil.getInstance().getSysOid(ipAddress,
                        community));
                host.setMac(SnmpUtil.getInstance().getBridgeAddress(ipAddress,
                        community));
                host.setBridgeAddress(SnmpUtil.getInstance().getBridgeAddress(
                        ipAddress, community));
                host.setIfEntityList(SnmpUtil.getInstance().getIfEntityList(
                        ipAddress, community, category));
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SnmpUtil snmp = SnmpUtil.getInstance();
                SysLogger.info("开始获取设备:" + host.getIpAddress() + "的系统名称");
                Hashtable sysGroupProperty = snmp.getSysGroup(host
                        .getIpAddress(), host.getCommunity());
                if (sysGroupProperty != null) {
                    host.setSysDescr((String) sysGroupProperty.get("sysDescr"));
                    host.setSysContact((String) sysGroupProperty
                            .get("sysContact"));
                    host.setSysName((String) sysGroupProperty.get("sysName"));
                    host.setSysLocation((String) sysGroupProperty
                            .get("sysLocation"));
                }
                if (ostype == 6) {
                    // AIX
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 22) {
                    host.setSysOid("tru64");
                    host.setType("TRU64 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setType("BDCom");
                } else if (ostype == 16) {
                    // ZTE
                    host.setType("ZTE");
                }
                SubnetDao netDao = new SubnetDao();
                List netList = null;
                try {
                    netList = netDao.loadAll(); // 找出它属于哪个子网
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    netDao.close();
                }
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else {
                // 主机服务器
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            }
            host.setSendemail("");
            host.setSendmobiles("");
            host.setSendphone("");
            host.setBid("");
            List hostList = new ArrayList(1);
            hostList.add(host);

            DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
            try {
                dcDao.addHostDataByHand(hostList);
                dcDao.addInterfaceData(hostList);
                dcDao.addMonitor(hostList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dcDao.close();
            }

            result = id; // 数据成功插入,返回ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // 如果增加数据失败则不继续

        HostNodeDao dao = new HostNodeDao();
        try // 以下把新数据载入内存(与PollingInitializtion.loadHost()差不多)
        {
            HostNode vo = (HostNode) dao.findByID(String.valueOf(id));
            HostLoader loader = new HostLoader();
            loader.loadOne(vo);
            loader.close();
            SysLogger.info("成功增加一台主机,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        } finally {
            dao.close();
        }
        return result;
    }

    /**
     * @author nielin add for time-sharing
     * @since 2009-12-29
     * @param String
     *            ipAddress,String alias,String community,String
     *            writecommunity,int category, int ostype,int collecttype,String
     *            bid,String sendmobiles,String sendemail,String sendphone
     *            ip,,读共同体,写共同体, 增加一台主机
     */
    public int addHost(String assetid, String location, String ipAddress,
            String alias, int snmpversion, String community,
            String writecommunity, int transfer, int category, int ostype,
            int collecttype, String bid, String sendmobiles, String sendemail,
            String sendphone) {
        HostNodeDao tmpDao = new HostNodeDao();
        // List tmpList = tmpDao.findByCondition1("ip_address",ipAddress);
        List tmpList = tmpDao.findBynode("ip_address", ipAddress);
        if (tmpList.size() > 0)
            return -1; // IP已经存在
        /*
         * 这里为了测试用,目前PING不通的设备也可以加进来
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping不通
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP采集方式
            if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
                return -3; // 不支持snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // 以下把新增加的主机的数据加入数据库
        {
            id = KeyGenerator.getInstance().getNextKey();
            host.setId(id);
            host.setCategory(category);
            host.setOstype(ostype);
            host.setCollecttype(collecttype);
            host.setAssetid(assetid);
            host.setLocation(location);
            host.setTransfer(transfer);
            if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
                host.setCommunity(community);
                host.setWritecommunity(writecommunity);
                host.setAlias(alias);
                host.setSnmpversion(snmpversion);

                host.setIpAddress(ipAddress);
                host.setSysOid(SnmpUtil.getInstance().getSysOid(ipAddress,
                        community));
                host.setMac(SnmpUtil.getInstance().getBridgeAddress(ipAddress,
                        community));
                host.setBridgeAddress(SnmpUtil.getInstance().getBridgeAddress(
                        ipAddress, community));
                host.setIfEntityList(SnmpUtil.getInstance().getIfEntityList(
                        ipAddress, community, category));
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SnmpUtil snmp = SnmpUtil.getInstance();
                SysLogger.info("开始获取设备:" + host.getIpAddress() + "的系统名称");
                Hashtable sysGroupProperty = snmp.getSysGroup(host
                        .getIpAddress(), host.getCommunity());
                if (sysGroupProperty != null) {
                    host.setSysDescr((String) sysGroupProperty.get("sysDescr"));
                    host.setSysContact((String) sysGroupProperty
                            .get("sysContact"));
                    host.setSysName((String) sysGroupProperty.get("sysName"));
                    host.setSysLocation((String) sysGroupProperty
                            .get("sysLocation"));
                }
                if (ostype == 6) {
                    // AIX
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setType("BDCom");
                } else if (ostype == 16) {
                    // ZTE
                    host.setType("ZTE");
                }
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_SHELL
                    || collecttype == SystemConstant.COLLECTTYPE_TELNET
                    || collecttype == SystemConstant.COLLECTTYPE_SSH) {
                // 主机服务器
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setType("AS400 服务器");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_PING) {
                // 主机服务器
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setSysOid("1.3.6.1.4.1.9");
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setSysOid("1.3.6.1.4.1.2011");
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setSysOid("1.3.6.1.4.1.9.2.1.57");
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setSysOid("1.3.6.1.4.1.89");
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setSysOid("1.3.6.1.4.1.5651");
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setSysOid("1.3.6.1.4.1.4881");
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setSysOid("1.3.6.1.4.1.45");
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setSysOid("1.3.6.1.4.1.171");
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setSysOid("1.3.6.1.4.1.3320");
                    host.setType("BDCom");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_REMOTEPING) {
                // 远程PING的设备
                // 主机服务器
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setSysOid("1.3.6.1.4.1.9");
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setSysOid("1.3.6.1.4.1.2011");
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setSysOid("1.3.6.1.4.1.9.2.1.57");
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setSysOid("1.3.6.1.4.1.89");
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setSysOid("1.3.6.1.4.1.5651");
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setSysOid("1.3.6.1.4.1.4881");
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setSysOid("1.3.6.1.4.1.45");
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setSysOid("1.3.6.1.4.1.171");
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setSysOid("1.3.6.1.4.1.3320");
                    host.setType("BDCom");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            }
            host.setSendemail(sendemail);
            host.setSendmobiles(sendmobiles);
            host.setSendphone(sendphone);
            host.setBid(bid);
            List hostList = new ArrayList(1);
            hostList.add(host);

            DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
            try {
                dcDao.addHostDataByHand(hostList);
                dcDao.addInterfaceData(hostList);
                dcDao.addMonitor(hostList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dcDao.close();
            }

            // nielin add for as400 start

            if (ostype == 15) {
                // as400服务器
                DiscoverCompleteDao dcDao2 = new DiscoverCompleteDao();

                dcDao2.createTableForAS400(host);
            }

            // nielin add for as400 end

            result = id; // 数据成功插入,返回ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // 如果增加数据失败则不继续

        try // 以下把新数据载入内存(与PollingInitializtion.loadHost()差不多)
        {
            HostNodeDao dao = new HostNodeDao();
            HostNode vo = null;
            try {
                vo = (HostNode) dao.findByID(String.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            HostLoader loader = new HostLoader();
            try {
                loader.loadOne(vo);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loader.close();
            }

            SysLogger.info("成功增加一台主机,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        }
        return result;
    }

    /**
     * @author snow
     * @param assetid
     * @param location
     * @param ipAddress
     * @param alias
     * @param community
     * @param writecommunity
     * @param category
     * @param ostype
     * @param collecttype
     * @param bid
     * @param sendmobiles
     * @param sendemail
     * @param sendphone
     * @param supperid
     * @return
     * @date 2010-05-18
     */
    public int addHost(String assetid, String location, String ipAddress,
            String alias, int snmpversion, String community,
            String writecommunity, int transfer, int category, int ostype,
            int collecttype, String bid, String sendmobiles, String sendemail,
            String sendphone, int supperid) {
        HostNodeDao tmpDao = new HostNodeDao();
        List tmpList = new ArrayList();
        try {
            tmpList = tmpDao.findBynode("ip_address", ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmpDao.close();
        }
        // SysLogger.info("ipAddress:"+ipAddress+"#############");
        if (tmpList.size() > 0)
            return -1; // IP已经存在
        /*
         * 这里为了测试用,目前PING不通的设备也可以加进来
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping不通
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP采集方式
            if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
                return -3; // 不支持snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // 以下把新增加的主机的数据加入数据库
        {
            id = KeyGenerator.getInstance().getNextKey();
            host.setId(id);
            host.setCategory(category);
            host.setOstype(ostype);
            host.setCollecttype(collecttype);
            host.setSupperid(supperid);
            host.setAssetid(assetid);
            host.setLocation(location);
            host.setTransfer(transfer);
            if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
                host.setCommunity(community);
                host.setWritecommunity(writecommunity);
                host.setSnmpversion(snmpversion);
                host.setAlias(alias);
                host.setIpAddress(ipAddress);
                host.setSysOid(SnmpUtil.getInstance().getSysOid(ipAddress,
                        community));
                host.setMac(SnmpUtil.getInstance().getBridgeAddress(ipAddress,
                        community));
                // host.setBridgeAddress(SnmpUtil.getInstance().getBridgeAddress(
                // ipAddress, community));
                if (host.getCategory() == 4) {
                    host.setBridgeAddress(SnmpUtil.getInstance()
                            .getHostBridgeAddress(ipAddress, community));
                } else {
                    host.setBridgeAddress(SnmpUtil.getInstance()
                            .getBridgeAddress(ipAddress, community));
                }
                if (host.getSysOid().startsWith("1.3.6.1.4.1.1588.2")) {// 博科的网络设备
                    host.setIfEntityList(SnmpUtil.getInstance()
                            .getIfEntityList_brocade(ipAddress, community,
                                    category));
                } else {
                    host.setIfEntityList(SnmpUtil.getInstance()
                            .getIfEntityList(ipAddress, community, category));
                }
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SnmpUtil snmp = SnmpUtil.getInstance();
                SysLogger.info("开始获取设备:" + host.getIpAddress() + "的系统名称");
                Hashtable sysGroupProperty = snmp.getSysGroup(host
                        .getIpAddress(), host.getCommunity());
                if (sysGroupProperty != null) {
                    host.setSysDescr((String) sysGroupProperty.get("sysDescr"));
                    host.setSysContact((String) sysGroupProperty
                            .get("sysContact"));
                    host.setSysName((String) sysGroupProperty.get("sysName"));
                    host.setSysLocation((String) sysGroupProperty
                            .get("sysLocation"));
                }
                if (ostype == 6) {
                    // AIX
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setType("BDCom");
                } else if (ostype == 16) {
                    // ZTE
                    host.setType("ZTE");
                }
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_SHELL) {
                // 主机服务器
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE 服务器");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER 服务器");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_WMI) {
                // 主机服务器
                host.setAlias(alias);
                // SysLogger.info(alias+"================");

                if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_TELNET
                    || collecttype == SystemConstant.COLLECTTYPE_SSH) {
                // 主机服务器
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE 服务器");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER 服务器");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_PING
                    || collecttype == SystemConstant.COLLECTTYPE_TELNETCONNECT
                    || collecttype == SystemConstant.COLLECTTYPE_SSHCONNECT
                    || collecttype == SystemConstant.COLLECTTYPE_DATAINTERFACE) {
                // PING TELNET或SSH连通检测 或 数据接口采集方式
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE 服务器");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setSysOid("1.3.6.1.4.1.9.");
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setSysOid("1.3.6.1.4.1.2011.");
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setSysOid("1.3.6.1.4.1.9.2.1.57.");
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setSysOid("1.3.6.1.4.1.89.");
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setSysOid("1.3.6.1.4.1.5651.");
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setSysOid("1.3.6.1.4.1.4881.");
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setSysOid("1.3.6.1.4.1.45.");
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setSysOid("1.3.6.1.4.1.171.");
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setSysOid("1.3.6.1.4.1.3320.");
                    host.setType("BDCom");
                } else if (ostype == 16) {
                    // ZTE
                    host.setSysOid("1.3.6.1.4.1.3902.");
                    host.setType("ZTE");
                } else if (ostype == 17) {
                    // ATM
                    host.setSysOid("net_atm");
                    host.setType("ATM");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            } else if (collecttype == SystemConstant.COLLECTTYPE_REMOTEPING) {
                // 远程PING的设备
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX 服务器");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX 服务器");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS 服务器");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX 服务器");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows 服务器");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 服务器");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE 服务器");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER 服务器");
                } else if (ostype == 1) {
                    // CISCO
                    host.setSysOid("1.3.6.1.4.1.9");
                    host.setType("Cisco");
                } else if (ostype == 2) {
                    // H3C
                    host.setSysOid("1.3.6.1.4.1.2011");
                    host.setType("H3C");
                } else if (ostype == 3) {
                    // Entrasys
                    host.setSysOid("1.3.6.1.4.1.9.2.1.57");
                    host.setType("Entrasys");
                } else if (ostype == 4) {
                    // Radware
                    host.setSysOid("1.3.6.1.4.1.89");
                    host.setType("Radware");
                } else if (ostype == 10) {
                    // MaiPu
                    host.setSysOid("1.3.6.1.4.1.5651");
                    host.setType("MaiPu");
                } else if (ostype == 11) {
                    // RedGiant
                    host.setSysOid("1.3.6.1.4.1.4881");
                    host.setType("RedGiant");
                } else if (ostype == 12) {
                    // NorthTel
                    host.setSysOid("1.3.6.1.4.1.45");
                    host.setType("NorthTel");
                } else if (ostype == 13) {
                    // D-Link
                    host.setSysOid("1.3.6.1.4.1.171");
                    host.setType("DLink");
                } else if (ostype == 14) {
                    // BDCom
                    host.setSysOid("1.3.6.1.4.1.3320");
                    host.setType("BDCom");
                } else if (ostype == 16) {
                    // ZTE
                    host.setSysOid("1.3.6.1.4.1.3902");
                    host.setType("ZTE");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // 找出它属于哪个子网
                for (int i = 0; i < netList.size(); i++) {
                    Subnet net = (Subnet) netList.get(i);
                    if (NetworkUtil.isValidIP(net.getNetAddress(), net
                            .getNetMask(), ipAddress)) {
                        host.setLocalNet(net.getId());
                        host.setNetMask(net.getNetMask());
                        break;
                    }
                }
            }
            host.setSendemail(sendemail);
            host.setSendmobiles(sendmobiles);
            host.setSendphone(sendphone);
            host.setBid(bid);
            List hostList = new ArrayList(1);
            hostList.add(host);

            DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
            try {
                dcDao.addHostDataByHand(hostList);
                dcDao.addInterfaceData(hostList);
                dcDao.addMonitor(hostList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dcDao.close();
            }

            // nielin add for as400 start

            if (ostype == 15) {
                // as400服务器
                DiscoverCompleteDao dcDao2 = new DiscoverCompleteDao();

                dcDao2.createTableForAS400(host);
            }

            // nielin add for as400 end

            result = id; // 数据成功插入,返回ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // 如果增加数据失败则不继续

        try // 以下把新数据载入内存(与PollingInitializtion.loadHost()差不多)
        {
            HostNodeDao dao = new HostNodeDao();
            HostNode vo = null;
            try {
                vo = (HostNode) dao.findByID(String.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            HostLoader loader = new HostLoader();
            try {
                loader.loadOne(vo);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                loader.close();
            }

            SysLogger.info("成功增加一台主机,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        }
        return result;
    }

    /**
     * 测试该IP地址的设备是否存在
     * 
     * @param ipAddress
     * @return
     */
    private int isExist(String ipAddress) {
        int addResult = 0;
        HostNodeDao tmpDao = new HostNodeDao();
        List tmpList = new ArrayList();
        try {
            tmpList = tmpDao.findBynode("ip_address", ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmpDao.close();
        }
        if (tmpList == null || tmpList.size() == 0) {
        } else {
            // IP已经存在
            addResult = -1;
        }
        return addResult;
    }

    /**
     * 测试SNMP是否打开
     * 
     * @param ipAddress
     * @param community
     * @return
     */
    public int isOpenSNMP(String ipAddress, String community) {
        int addResult = 0;
        if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null) {
            addResult = -3; // 未打开 SNMP
        }
        return addResult;
    }

    /**
     * 设置SNMP参数
     * 
     * @param hostNode
     */
    public void setSNMPParameter(HostNode hostNode) {
        String ipAddress = hostNode.getIpAddress();
        String community = hostNode.getCommunity();
        SnmpUtil snmp = SnmpUtil.getInstance();
        String sysOid = "";
        String sysDescr = "";
        String sysContact = "";
        String sysName = "";
        String sysLocation = "";
        String mac = "";
        String bridgeAddress = "";
        int category = hostNode.getCategory();
        List ifEntityList = null;

        // OID
        sysOid = SnmpUtil.getInstance().getSysOid(ipAddress, community);

        // 系统数据
        Hashtable sysGroupProperty = snmp.getSysGroup(ipAddress, community);
        if (sysGroupProperty != null) {
            sysDescr = (String) sysGroupProperty.get("sysDescr");
            sysContact = (String) sysGroupProperty.get("sysContact");
            sysName = (String) sysGroupProperty.get("sysName");
            sysLocation = (String) sysGroupProperty.get("sysLocation");
        }

        // MAC
        mac = SnmpUtil.getInstance().getBridgeAddress(ipAddress, community);
        if (mac == null) {
            mac = "";
        }

        // bridgeAddress
        if (category == 4) {
            bridgeAddress = SnmpUtil.getInstance().getHostBridgeAddress(
                    ipAddress, community);
        } else {
            bridgeAddress = SnmpUtil.getInstance().getBridgeAddress(ipAddress,
                    community);
        }

        // 端口
        if (sysOid != null && sysOid.startsWith("1.3.6.1.4.1.1588.2")) {// 博科的网络设备
            ifEntityList = SnmpUtil.getInstance().getIfEntityList_brocade(
                    ipAddress, community, category);
        } else {
            ifEntityList = SnmpUtil.getInstance().getIfEntityList(ipAddress,
                    community, category);
        }

        hostNode.setSysOid(sysOid);
        hostNode.setIpAddress(ipAddress);
        hostNode.setCommunity(community);
        hostNode.setSysDescr(sysDescr);
        hostNode.setSysName(sysName);
        hostNode.setSysContact(sysContact);
        hostNode.setSysLocation(sysLocation);
        hostNode.setMac(mac);
        hostNode.setBridgeAddress(bridgeAddress);
        hostNode.setLocalNet(0);
        hostNode.setNetMask("255.255.255.0");
        hostNode.setDiscovertatus(-1);
        hostNode.setIfEntityList(ifEntityList);
    }

    public String getTypeByOsType(int ostype) {
        String type = "";
        if (ostype == 6) {
            // AIX
            type = Constant.TYPE_HOST_SUBTYPE_AIX;
        } else if (ostype == 7) {
            // HP UNIX
            type = Constant.TYPE_HOST_SUBTYPE_HPUNIX;
        } else if (ostype == 8) {
            // SUN SOLARIS
            type = Constant.TYPE_HOST_SUBTYPE_SOLARIS;
        } else if (ostype == 9) {
            // LINUX
            type = Constant.TYPE_HOST_SUBTYPE_LINUX;
        } else if (ostype == 5) {
            // WINDOWS
            type = Constant.TYPE_HOST_SUBTYPE_WINDOWS;
        } else if (ostype == 15) {
            // AS400
            type = Constant.TYPE_HOST_SUBTYPE_AS400;
        } else if (ostype == 20) {
            // SCOUNIX
            type = Constant.TYPE_HOST_SUBTYPE_SCOUNIX;
        } else if (ostype == 21) {
            // SCOOPENSERVER
            type = Constant.TYPE_HOST_SUBTYPE_SCOOPENSERVER;
        } else if (ostype == 22) {
            // TRU64
            type = Constant.TYPE_HOST_SUBTYPE_TRU64;
        } else if (ostype == 1) {
            // CISCO
            type = Constant.TYPE_NET_SUBTYPE_CISCO;
        } else if (ostype == 2) {
            // H3C交换机
            type = Constant.TYPE_NET_SUBTYPE_H3C;
        } else if (ostype == 3) {
            // Entrasys
            type = Constant.TYPE_NET_SUBTYPE_ENTRASYS;
        } else if (ostype == 4) {
            // Radware
            type = Constant.TYPE_NET_SUBTYPE_RADWARE;
        } else if (ostype == 10) {
            // MaiPu
            type = Constant.TYPE_NET_SUBTYPE_MAIPU;
        } else if (ostype == 11) {
            // RedGiant
            type = Constant.TYPE_NET_SUBTYPE_REDGIANT;
        } else if (ostype == 12) {
            // NorthTel
            type = Constant.TYPE_NET_SUBTYPE_NORTHTEL;
        } else if (ostype == 13) {
            // D-Link
            type = Constant.TYPE_NET_SUBTYPE_DLINK;
        } else if (ostype == 14) {
            // BDCom
            type = Constant.TYPE_NET_SUBTYPE_BDCOM;
        } else if (ostype == 16) {
            // ZTE
            type = Constant.TYPE_NET_SUBTYPE_ZTE;
        } else if (ostype == 25) {
        	//PIX
        	type = Constant.TYPE_FIREWALL_SUBTYPE_PIX;
        } else if (ostype == 38) {
        	//TOPSEC
        	type = Constant.TYPE_FIREWALL_SUBTYPE_TOPSEC;
        } else if (ostype == 32) {
        	//VENUS
        	type = Constant.TYPE_FIREWALL_SUBTYPE_VENUS;
        } else if (ostype == 33) {
        	//FIREWALL_H3C	H3C防火墙
        	type = Constant.TYPE_FIREWALL_SUBTYPE_FWH3C;
        }
        return type;
    }

    public String getSysOidByOsType(int ostype) {
        String sysOid = "";
        if (ostype == 6) {
            // AIX
            sysOid = "1.3.6.1.4.1.2.3.1.2.1.1";
        } else if (ostype == 7) {
            // HP UNIX
            sysOid = "1.3.6.1.4.1.11.2.3.10.1";
        } else if (ostype == 8) {
            // SUN SOLARIS
            sysOid = "1.3.6.1.4.1.42.2.1.1";
        } else if (ostype == 9) {
            // LINUX
            sysOid = "1.3.6.1.4.1.2021.250.10";
        } else if (ostype == 5) {
            // WINDOWS
            sysOid = "1.3.6.1.4.1.311.1.1.3";
        } else if (ostype == 15) {
            // AS400
            sysOid = "as400";
        } else if (ostype == 20) {
            // SCOUNIX
            sysOid = "scounix";
        } else if (ostype == 21) {
            // SCOOPENSERVER
            sysOid = "scoopenserver";
        } else if (ostype == 22) {
            // TRU64
            sysOid = "tru64";
        } else if (ostype == 1) {
            // CISCO
            sysOid = "1.3.6.1.4.1.9";
        } else if (ostype == 2) {
            // H3C
            sysOid = "1.3.6.1.4.1.2011";
        } else if (ostype == 3) {
            // Entrasys
            sysOid = "1.3.6.1.4.1.9.2.1.57";
        } else if (ostype == 4) {
            // Radware
            sysOid = "1.3.6.1.4.1.89";
        } else if (ostype == 10) {
            // MaiPu
            sysOid = "1.3.6.1.4.1.5651";
        } else if (ostype == 11) {
            // RedGiant
            sysOid = "1.3.6.1.4.1.4881";
        } else if (ostype == 12) {
            // NorthTel
            sysOid = "1.3.6.1.4.1.45";
        } else if (ostype == 13) {
            // D-Link
            sysOid = "1.3.6.1.4.1.171";
        } else if (ostype == 14) {
            // BDCom
            sysOid = "1.3.6.1.4.1.3320";
        } else if (ostype == 16) {
            // ZTE
            sysOid = "1.3.6.1.4.1.3902";
        } else if (ostype == 38) {
            // Topsec
            sysOid = "1.3.6.1.4.1.14331.1.4";
        } else if (ostype == 25) {
            // PIX
            sysOid = "pix";
        } else if (ostype == 32) {
        	// Venus
            sysOid = "1.3.6.1.4.1.15227.6";
        } else if (ostype == 33){
        	//Firewall_h3c
        	sysOid = "1.3.6.1.4.1.25506.1.114";
        }
        return sysOid;
    }

    public int addHost(HostNode hostNode) {
        try {
            host = createHost(hostNode);
            HostNodeDao hostNodeDao = new HostNodeDao();
            try {
                hostNodeDao.save(hostNode);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hostNodeDao.close();
            }
            DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
            try {
                dcDao.addHostDataByHand(hostNode);
                dcDao.addInterfaceData(hostNode);
                // dcDao.addMonitor(hostList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dcDao.close();
            }
            HostLoader loader = new HostLoader();
            loader.loadOne(hostNode);
            loader.close();
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(hostNode);
            try {
                NodeGatherIndicatorsUtil gatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                gatherIndicatorsUtil.addGatherIndicatorsForNode(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (hostNode.getCategory() == 4){
                //主机服务器
                XmlOperator opr = new XmlOperator();
                opr.setFile("server.jsp");
                opr.init4updateXml();
                opr.addNode(host);   
                opr.writeXml();
            } else if(hostNode.getCategory() < 4) {
                //网络设备
                XmlOperator opr = new XmlOperator();
                opr.setFile("network.jsp");
                opr.init4updateXml();
                opr.addNode(host);   
                opr.writeXml();
            }
            DataSendService.sendAddNode(hostNode);
            SysLogger.info("成功增加一台主机,id=" + hostNode.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostNode.getId();
    }

    /**
     * 返回增加的主机
     */
    public com.afunms.discovery.Host getHost() {
        return host;
    }
    
    public int checkHostNode(HostNode hostNode) {
        int addResult = 0;
        int id = KeyGenerator.getInstance().getNextKey();
        hostNode.setId(id);
        String alias = hostNode.getAlias();
        String ipAddress = hostNode.getIpAddress();
        int collecttype = hostNode.getCollecttype();
        String community = hostNode.getCommunity();
        boolean managed = hostNode.isManaged();
        int status = hostNode.getStatus();
        int supperNode = hostNode.getSuperNode();
        int ostype = hostNode.getOstype();
        String type = "";
        int snmpversion = hostNode.getSnmpversion();
        int category = hostNode.getCategory();
        String assetid = hostNode.getAssetid();
        String location = hostNode.getLocation();
        String bid = hostNode.getBid();
        String sendemail = hostNode.getSendemail();
        String sendmobiles = hostNode.getSendmobiles();
        String sendphone = hostNode.getSendphone();
        addResult = isExist(ipAddress);
        if (addResult < 0) {
            // IP已经存在
            return addResult;
        }
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            addResult = isOpenSNMP(ipAddress, community);
            if (addResult < 0) {
                // 未打开 SNMP
                return addResult;
            }
            setSNMPParameter(hostNode);
        }
        type = getTypeByOsType(ostype);
        hostNode.setType(type);
        if (collecttype != SystemConstant.COLLECTTYPE_SNMP) {
            String sysOid = getSysOidByOsType(ostype);
            hostNode.setSysOid(sysOid);
        }
        addResult = id;
        return addResult;
    }

    public void delete(HostNode host, User user) {

        // 进行修改

//        HostNode host = null;
//        HostNodeDao hostNodeDao = new HostNodeDao();
//        try {
//            host = (HostNode) hostNodeDao.findByID(id);
//            hostNodeDao.delete(id);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            hostNodeDao.close();
//        }
        if (host == null) {
            return;
        }
        String id = String.valueOf(host.getId());
        NodeUtil nodeUtil = new NodeUtil();
        PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
        SysLogger.info("开始删除设备................" + host.getIpAddress());
        NodeDTO node = nodeUtil.conversionToNodeDTO(host);
        
        NodeDomainService domainService = new NodeDomainService();
		NodeDomain nodeDomain = domainService.getNodeDomain(node);
        domainService.delete(node);

        String ip = host.getIpAddress();
        String allipstr = SysUtil.doip(ip);

        // 删除表
        DBManager conn = new DBManager();
        CreateTableManager ctable = null;
        try {
            ctable = new CreateTableManager();

            for (String tableName : TABLE_NAME_LIST) {
                ctable.deleteTable(conn, tableName, allipstr, tableName);// Ping
                ctable.deleteTable(conn, tableName + "hour", allipstr, tableName + "hour");// Ping
                ctable.deleteTable(conn, tableName + "day", allipstr, tableName + "day");// Ping
            }
//          ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
//          ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// Ping
//          ctable.deleteTable(conn, "pingday", allipstr, "pingday");// Ping
//
//          ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
//          ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
//          ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
//
//          ctable.deleteTable(conn, "memory", allipstr, "mem");// 内存
//          ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// 内存
//          ctable.deleteTable(conn, "memoryday", allipstr, "memday");// 内存
//          // 不在采集
//          // ctable.deleteTable(conn,"flash",allipstr,"flash");//闪存
//          // ctable.deleteTable(conn,"flashhour",allipstr,"flashhour");//闪存
//          // ctable.deleteTable(conn,"flashday",allipstr,"flashday");//闪存
//
//          ctable.deleteTable(conn, "buffer", allipstr, "buffer");// 缓存
//          ctable.deleteTable(conn, "bufferhour", allipstr, "bufferhour");// 缓存
//          ctable.deleteTable(conn, "bufferday", allipstr, "bufferday");// 缓存
//          // 不在采集
//          // ctable.deleteTable(conn,"temper",allipstr,"temper");//温度
//          // ctable.deleteTable(conn,"temperhour",allipstr,"temperhour");//温度
//          // ctable.deleteTable(conn,"temperday",allipstr,"temperday");//温度
//          //
//          // ctable.deleteTable(conn,"fan",allipstr,"fan");//风扇
//          // ctable.deleteTable(conn,"fanhour",allipstr,"fanhour");//风扇
//          // ctable.deleteTable(conn,"fanday",allipstr,"fanday");//风扇
//          //                
//          // ctable.deleteTable(conn,"power",allipstr,"power");//电源
//          // ctable.deleteTable(conn,"powerhour",allipstr,"powerhour");//电源
//          // ctable.deleteTable(conn,"powerday",allipstr,"powerday");//电源
//          //                
//          // ctable.deleteTable(conn,"vol",allipstr,"vol");//电压
//          // ctable.deleteTable(conn,"volhour",allipstr,"volhour");//电压
//          // ctable.deleteTable(conn,"volday",allipstr,"volday");//电压
//
//          ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");// 单个端口流速
//          ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");// 单个端口流速
//          ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");// 单个端口流速
//
//          ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");// 端口总流速
//          ctable.deleteTable(conn, "allutilhdxhour", allipstr,
//                  "allhdxhour");// 端口总流速
//          ctable
//                  .deleteTable(conn, "allutilhdxday", allipstr,
//                          "allhdxday");// 端口总流速
//
//          ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");// 带宽
//          ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");// 带宽
//          ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");// 带宽
//
//          ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");// 丢包率
//          ctable.deleteTable(conn, "dcardperchour", allipstr,
//                  "dcardperchour");// 丢包率
//          ctable.deleteTable(conn, "dcardpercday", allipstr,
//                  "dcardpercday");// 丢包率
//
//          ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");// 错包率
//          ctable
//                  .deleteTable(conn, "errperchour", allipstr,
//                          "errperchour");// 错包率
//          ctable.deleteTable(conn, "errpercday", allipstr, "errpercday");// 错包率
//
//          ctable.deleteTable(conn, "packs", allipstr, "packs");// 数据包总数
//          ctable.deleteTable(conn, "packshour", allipstr, "packshour");// 数据包总数
//          ctable.deleteTable(conn, "packsday", allipstr, "packsday");// 数据包总数
//
//          ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");// 数据包总数
//          ctable
//                  .deleteTable(conn, "inpackshour", allipstr,
//                          "inpackshour");// 数据包总数
//          ctable.deleteTable(conn, "inpacksday", allipstr, "inpacksday");// 数据包总数
//
//          ctable.deleteTable(conn, "outpacks", allipstr, "outpacks"); // 数据包总数
//          ctable.deleteTable(conn, "outpackshour", allipstr,
//                  "outpackshour");// 数据包总数
//          ctable
//                  .deleteTable(conn, "outpacksday", allipstr,
//                          "outpacksday");// 数据包总数
//
//          ctable.deleteTable(conn, "pro", allipstr, "pro");// 进程
//          ctable.deleteTable(conn, "prohour", allipstr, "prohour");// 进程小时
//          ctable.deleteTable(conn, "proday", allipstr, "proday");// 进程天
//
//          ctable.deleteTable(conn, "cpudtl", allipstr, "cpudtl");
//          ctable.deleteTable(conn, "cpudtlhour", allipstr, "cpudtlhour");
//          ctable.deleteTable(conn, "cpudtlday", allipstr, "cpudtlday");
//
//          ctable.deleteTable(conn, "disk", allipstr, "disk");// 磁盘利用率
//          ctable.deleteTable(conn, "diskhour", allipstr, "diskhour");// 磁盘利用率
//          ctable.deleteTable(conn, "diskday", allipstr, "diskday");// 磁盘利用率
//
//          ctable.deleteTable(conn, "diskincre", allipstr, "diskincre");// 磁盘增长率yangjun
//          ctable.deleteTable(conn, "diskincrehour", allipstr,
//                  "diskincrehour");// 磁盘增长率小时
//          ctable.deleteTable(conn, "diskincreday", allipstr,
//                  "diskincreday");// 磁盘增长率天
//
//          ctable.deleteTable(conn, "pgused", allipstr, "pgused");// 生成换页率
//          ctable.deleteTable(conn, "pgusedhour", allipstr, "pgusedhour");// 生成换页率
//          ctable.deleteTable(conn, "pgusedday", allipstr, "pgusedday");// 生成换页率
//
            ctable.deleteTable(conn, "portstatus", allipstr, "port");// 端口状态
            // ctable.deleteTable(conn,"portstatushour",allipstr,"porthour");
            // ctable.deleteTable(conn,"portstatusday",allipstr,"portday");

            ctable.deleteTable(conn, "nms_interface_data_temp", allipstr,
                    "interface");// 端口状态

            ctable.deleteTable(conn, "nms_process_data_temp", allipstr,
                    "process");// 端口状态

            ctable.deleteTable(conn, "systemasp", allipstr, "systemasp");
            ctable.deleteTable(conn, "dbcapability", allipstr,
                    "dbcapability");
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            conn.close();
        }

        EventListDao eventdao = new EventListDao();
        try {
            // 同时删除事件表里的相关数据
            eventdao.delete(host.getId(), node.getType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventdao.close();
        }

        PortconfigDao portconfigdao = new PortconfigDao();
        try {
            // 同时删除端口配置表里的相关数据
            portconfigdao.deleteByIpaddress(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigdao.close();
        }

        // 删除nms_ipmacchange表里的对应的数据
        IpMacChangeDao macchangebasedao = new IpMacChangeDao();
        try {
            // delte后,conn已经关闭
            macchangebasedao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            macchangebasedao.close();
        }

        // 删除网络设备配置文件表里的对应的数据
        NetNodeCfgFileDao configdao = new NetNodeCfgFileDao();
        try {
            // delte后,conn已经关闭
            configdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }

        // 删除网络设备SYSLOG接收表里的对应的数据
        NetSyslogDao syslogdao = new NetSyslogDao();
        try {
            // delte后,conn已经关闭
            syslogdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            syslogdao.close();
        }

        // 删除网络设备端口扫描表里的对应的数据
        PortScanDao portscandao = new PortScanDao();
        try {
            // delte后,conn已经关闭
            portscandao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portscandao.close();
        }

        // 删除网络设备面板图表里的对应的数据
        IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
        try {
            // delte后,conn已经关闭
            addresspaneldao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            addresspaneldao.close();
        }

        // 删除网络设备接口表里的对应的数据
        HostInterfaceDao interfacedao = new HostInterfaceDao();
        try {
            // delte后,conn已经关闭
            interfacedao.deleteByHostId(host.getId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfacedao.close();
        }

        // 删除网络设备IP别名表里的对应的数据
        IpAliasDao ipaliasdao = new IpAliasDao();
        try {
            // delte后,conn已经关闭
            ipaliasdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipaliasdao.close();
        }

        // 删除网络设备手工配置的链路表里的对应的数据
        RepairLinkDao repairdao = new RepairLinkDao();
        try {
            // delte后,conn已经关闭
            repairdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repairdao.close();
        }

        // 删除网络设备IPMAC表里的对应的数据
        IpMacDao ipmacdao = new IpMacDao();
        try {
            // delte后,conn已经关闭
            ipmacdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipmacdao.close();
        }

        // 删除该设备的采集指标
        NodeGatherIndicatorsDao gatherdao = new NodeGatherIndicatorsDao();
        try {
            gatherdao.deleteByNodeIdAndTypeAndSubtype(
                    node.getId() + "", node.getType(), node
                            .getSubtype());
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            gatherdao.close();
        }

        // 删除网络设备指标采集表里的对应的数据
        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
        try {
            // delte后,conn已经关闭
            indicatdao.deleteByNodeId(host.getId() + "",
                    node.getType(), node.getSubtype());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indicatdao.close();
        }

        // 删除IP-MAC-BASE表里的对应的数据
        IpMacBaseDao macbasedao = new IpMacBaseDao();
        try {
            // delte后,conn已经关闭
            macbasedao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            macbasedao.close();
        }

        // 删除diskconfig
        String[] otherTempData = new String[] { "nms_diskconfig" };
        String[] ipStrs = new String[] { host.getIpAddress() };
        ctable.clearTablesData(otherTempData, "ipaddress", ipStrs);
        
        // 删除进程组的数据
        ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
        processGroupConfigurationUtil
                .deleteProcessGroupAndConfigurationByNodeid(host
                        .getId()
                        + "");

        
        // 2.更新xml
        if (host.getCategory() < 4) {
            // 网络设备
            XmlOperator opr = new XmlOperator();
            opr.setFile("network.jsp");
            opr.init4updateXml();
            opr.deleteNodeByID(host.getId() + "");
            // opr.addNode(helper.getHost());
            opr.writeXml();
        } else if (host.getCategory() == 4) {
            // 主机服务器
            XmlOperator opr = new XmlOperator();
            opr.setFile("server.jsp");
            opr.init4updateXml();
            opr.deleteNodeByID(host.getId() + "");
            // opr.addNode(helper.getHost());
            opr.writeXml();
        }

        LinkDao linkdao = new LinkDao();
        List linklist = null;
        try {
            linklist = linkdao.findByNodeId(host.getId() + "");
        } catch (Exception e) {

        } finally {
            linkdao.close();
        }
        // 将连接删除(guangfei edite)
        if (linklist != null && linklist.size() > 0) {
            for (int l = 0; l < linklist.size(); l++) {
                Link link = (Link) linklist.get(l);
                if (link != null) {
                    LinkDao ldao = new LinkDao();
                    try {
                        ldao.delete(link.getId() + "");
                    } catch (Exception e) {

                    } finally {
                        ldao.close();
                    }
                }
            }
        }

        // 更新业务视图
        NodeDependDao nodedependao = new NodeDependDao();
        List list = nodedependao.findByNode("net" + id);
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                NodeDepend vo = (NodeDepend) list.get(j);
                if (vo != null) {
                    LineDao lineDao = new LineDao();
                    lineDao.deleteByidXml("net" + id, vo.getXmlfile());
                    NodeDependDao nodeDependDao = new NodeDependDao();
                    if (nodeDependDao.isNodeExist("net" + id, vo
                            .getXmlfile())) {
                        nodeDependDao.deleteByIdXml("net" + id, vo
                                .getXmlfile());
                    } else {
                        nodeDependDao.close();
                    }

                    // yangjun
                    ManageXmlDao mXmlDao = new ManageXmlDao();
                    List xmlList = new ArrayList();
                    try {
                        xmlList = mXmlDao.loadByPerAll(user
                                .getBusinessids());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mXmlDao.close();
                    }
                    try {
                        ChartXml chartxml;
                        chartxml = new ChartXml("tree");
                        chartxml.addViewTree(xmlList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ManageXmlDao subMapDao = new ManageXmlDao();
                    ManageXml manageXml = (ManageXml) subMapDao
                            .findByXml(vo.getXmlfile());
                    if (manageXml != null) {
                        NodeDependDao nodeDepenDao = new NodeDependDao();
                        try {
                            List lists = nodeDepenDao.findByXml(vo
                                    .getXmlfile());
                            ChartXml chartxml;
                            chartxml = new ChartXml("NetworkMonitor",
                                    "/"
                                            + vo.getXmlfile().replace(
                                                    "jsp", "xml"));
                            chartxml.addBussinessXML(manageXml
                                    .getTopoName(), lists);
                            ChartXml chartxmlList;
                            chartxmlList = new ChartXml(
                                    "NetworkMonitor",
                                    "/"
                                            + vo
                                                    .getXmlfile()
                                                    .replace("jsp",
                                                            "xml")
                                                    .replace(
                                                            "businessmap",
                                                            "list"));
                            chartxmlList.addListXML(manageXml
                                    .getTopoName(), lists);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            nodeDepenDao.close();
                        }
                    }
                }
            }
        }

        // 用户操作审计
        Calendar tempCal = Calendar.getInstance();
        Date cc = tempCal.getTime();
        String time = sdf1.format(cc);
        UserAuditUtil useraudit = new UserAuditUtil();
        String useraction = "";
        useraction = useraction + "删除设备 IP:" + host.getIpAddress()
                + " 别名:" + host.getAlias() + " 类型:" + host.getType();
        try {
            useraudit.saveUserAudit(user, time, useraction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        // 删除设备在临时表里中存储的数据
        String[] nmsTempDataTables = { "nms_cpu_data_temp",
                "nms_device_data_temp", "nms_disk_data_temp",
                "nms_diskperf_data_temp", "nms_envir_data_temp",
                "nms_fdb_data_temp", "nms_fibrecapability_data_temp",
                "nms_fibreconfig_data_temp", "nms_flash_data_temp",
                "nms_interface_data_temp", "nms_lights_data_temp",
                "nms_memory_data_temp", "nms_other_data_temp",
                "nms_ping_data_temp", "nms_process_data_temp",
                "nms_route_data_temp", "nms_sercice_data_temp",
                "nms_software_data_temp", "nms_storage_data_temp",
                "nms_system_data_temp", "nms_user_data_temp",
                "nms_nodeconfig", "nms_nodecpuconfig",
                "nms_nodediskconfig", "nms_nodememconfig" };
        CreateTableManager createTableManager = new CreateTableManager();
        createTableManager.clearNmsTempDatas(nmsTempDataTables, new String[] {id});

        DataSendService.sendDeleteNode(node, user, nodeDomain);
    }
    
    public com.afunms.discovery.Host createHost(HostNode hostNode) {
        com.afunms.discovery.Host host = new com.afunms.discovery.Host();
        host.setSysOid(hostNode.getSysOid());
        host.setIpAddress(hostNode.getIpAddress());
        host.setCommunity(hostNode.getCommunity());
        host.setSysDescr(hostNode.getSysDescr());
        host.setSysName(hostNode.getSysName());
        host.setSysContact(hostNode.getSysContact());
        host.setSysLocation(hostNode.getSysLocation());
        host.setMac(hostNode.getMac());
        host.setBridgeAddress(hostNode.getBridgeAddress());
        host.setLocalNet(0);
        host.setNetMask("255.255.255.0");
        host.setDiscoverstatus(-1);
        host.setIfEntityList(hostNode.getIfEntityList());
        host.setCategory(hostNode.getCategory());
        host.setAlias(hostNode.getAlias());
        host.setId(hostNode.getId());
        return host;
    }
}