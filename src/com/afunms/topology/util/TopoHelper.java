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
     * ����һ̨����
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
            return -1; // IP�Ѿ�����
        if (NetworkUtil.ping(ipAddress) == 0)
            return -2; // ping��ͨ
        if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
            return -3; // ��֧��snmp

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // ���°������ӵ����������ݼ������ݿ�
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
            SysLogger.info("��ʼ��ȡ�豸:" + host.getIpAddress() + "��ϵͳ����");
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
                netList = netDao.loadAll(); // �ҳ��������ĸ�����
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

            result = id; // ���ݳɹ�����,����ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // �����������ʧ���򲻼���
        HostNodeDao dao = new HostNodeDao();
        try // ���°������������ڴ�(��PollingInitializtion.loadHost()���)
        {

            HostNode vo = (HostNode) dao.findByID(String.valueOf(id));
            HostLoader loader = new HostLoader();
            loader.loadOne(vo);
            loader.close();
            SysLogger.info("�ɹ�����һ̨����,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        } finally {
            dao.close();
        }
        return result;
    }

    /**
     * ����һ̨����
     */
    public int addHost(String ipAddress, String alias, String community,
            String writecommunity, int category, int ostype, int collecttype) {
        HostNodeDao tmpDao = new HostNodeDao();
        List tmpList = tmpDao.findByCondition("ip_address", ipAddress);
        if (tmpList.size() > 0)
            return -1; // IP�Ѿ�����
        /*
         * ����Ϊ�˲�����,ĿǰPING��ͨ���豸Ҳ���Լӽ���
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping��ͨ
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP�ɼ���ʽ
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
                return -3; // ��֧��snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // ���°������ӵ����������ݼ������ݿ�
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
                SysLogger.info("��ʼ��ȡ�豸:" + host.getIpAddress() + "��ϵͳ����");
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
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
                } else if (ostype == 22) {
                    host.setSysOid("tru64");
                    host.setType("TRU64 ������");
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
                    netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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

            result = id; // ���ݳɹ�����,����ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // �����������ʧ���򲻼���

        HostNodeDao dao = new HostNodeDao();
        try // ���°������������ڴ�(��PollingInitializtion.loadHost()���)
        {
            HostNode vo = (HostNode) dao.findByID(String.valueOf(id));
            HostLoader loader = new HostLoader();
            loader.loadOne(vo);
            loader.close();
            SysLogger.info("�ɹ�����һ̨����,id=" + id);
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
     *            ip,,����ͬ��,д��ͬ��, ����һ̨����
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
            return -1; // IP�Ѿ�����
        /*
         * ����Ϊ�˲�����,ĿǰPING��ͨ���豸Ҳ���Լӽ���
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping��ͨ
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP�ɼ���ʽ
            if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
                return -3; // ��֧��snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // ���°������ӵ����������ݼ������ݿ�
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
                SysLogger.info("��ʼ��ȡ�豸:" + host.getIpAddress() + "��ϵͳ����");
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
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setType("AS400 ������");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // Զ��PING���豸
                // ����������
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // as400������
                DiscoverCompleteDao dcDao2 = new DiscoverCompleteDao();

                dcDao2.createTableForAS400(host);
            }

            // nielin add for as400 end

            result = id; // ���ݳɹ�����,����ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // �����������ʧ���򲻼���

        try // ���°������������ڴ�(��PollingInitializtion.loadHost()���)
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

            SysLogger.info("�ɹ�����һ̨����,id=" + id);
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
            return -1; // IP�Ѿ�����
        /*
         * ����Ϊ�˲�����,ĿǰPING��ͨ���豸Ҳ���Լӽ���
         */
        // if(NetworkUtil.ping(ipAddress)==0)
        // return -2; //ping��ͨ
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            // SNMP�ɼ���ʽ
            if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null)
                return -3; // ��֧��snmp
        }

        host = new com.afunms.discovery.Host();
        int result = 0;
        int id = 0;
        try // ���°������ӵ����������ݼ������ݿ�
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
                if (host.getSysOid().startsWith("1.3.6.1.4.1.1588.2")) {// ���Ƶ������豸
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
                SysLogger.info("��ʼ��ȡ�豸:" + host.getIpAddress() + "��ϵͳ����");
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
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE ������");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER ������");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                // SysLogger.info(alias+"================");

                if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // ����������
                host.setAlias(alias);
                // SysLogger.info(alias+"================");
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE ������");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER ������");
                }
                host.setIpAddress(ipAddress);
                host.setLocalNet(0);
                host.setNetMask("255.255.255.0");
                host.setDiscoverstatus(-1);
                SubnetDao netDao = new SubnetDao();
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // PING TELNET��SSH��ͨ��� �� ���ݽӿڲɼ���ʽ
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE ������");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // Զ��PING���豸
                host.setAlias(alias);
                if (ostype == 6) {
                    // AIX
                    host.setSysOid("1.3.6.1.4.1.2.3.1.2.1.1");
                    host.setType("IBM AIX ������");
                } else if (ostype == 7) {
                    // HP UNIX
                    host.setSysOid("1.3.6.1.4.1.11.2.3.10.1");
                    host.setType("HP UNIX ������");
                } else if (ostype == 8) {
                    // SUN SOLARIS
                    host.setSysOid("1.3.6.1.4.1.42.2.1.1");
                    host.setType("SUN SOLARIS ������");
                } else if (ostype == 9) {
                    // LINUX
                    host.setSysOid("1.3.6.1.4.1.2021.250.10");
                    host.setType("LINUX ������");
                } else if (ostype == 5) {
                    // WINDOWS
                    host.setSysOid("1.3.6.1.4.1.311.1.1.3");
                    host.setType("Windows ������");
                } else if (ostype == 15) {
                    host.setSysOid("as400");
                    host.setType("AS400 ������");
                } else if (ostype == 20) {
                    host.setSysOid("scounix");
                    host.setType("SCOUNIXWARE ������");
                } else if (ostype == 21) {
                    host.setSysOid("scoopenserver");
                    host.setType("SCOOPENSERVER ������");
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
                List netList = netDao.loadAll(); // �ҳ��������ĸ�����
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
                // as400������
                DiscoverCompleteDao dcDao2 = new DiscoverCompleteDao();

                dcDao2.createTableForAS400(host);
            }

            // nielin add for as400 end

            result = id; // ���ݳɹ�����,����ID
        } catch (Exception e) {
            SysLogger.error("TopoHelper.addHost(),insert db", e);
        }
        if (result == 0)
            return 0; // �����������ʧ���򲻼���

        try // ���°������������ڴ�(��PollingInitializtion.loadHost()���)
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

            SysLogger.info("�ɹ�����һ̨����,id=" + id);
        } catch (Exception e) {
            SysLogger.error("TopoUtil.addHost(),insert memory", e);
            result = 0;
        }
        return result;
    }

    /**
     * ���Ը�IP��ַ���豸�Ƿ����
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
            // IP�Ѿ�����
            addResult = -1;
        }
        return addResult;
    }

    /**
     * ����SNMP�Ƿ��
     * 
     * @param ipAddress
     * @param community
     * @return
     */
    public int isOpenSNMP(String ipAddress, String community) {
        int addResult = 0;
        if (SnmpUtil.getInstance().getSysOid(ipAddress, community) == null) {
            addResult = -3; // δ�� SNMP
        }
        return addResult;
    }

    /**
     * ����SNMP����
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

        // ϵͳ����
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

        // �˿�
        if (sysOid != null && sysOid.startsWith("1.3.6.1.4.1.1588.2")) {// ���Ƶ������豸
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
            // H3C������
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
        	//FIREWALL_H3C	H3C����ǽ
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
                //����������
                XmlOperator opr = new XmlOperator();
                opr.setFile("server.jsp");
                opr.init4updateXml();
                opr.addNode(host);   
                opr.writeXml();
            } else if(hostNode.getCategory() < 4) {
                //�����豸
                XmlOperator opr = new XmlOperator();
                opr.setFile("network.jsp");
                opr.init4updateXml();
                opr.addNode(host);   
                opr.writeXml();
            }
            DataSendService.sendAddNode(hostNode);
            SysLogger.info("�ɹ�����һ̨����,id=" + hostNode.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostNode.getId();
    }

    /**
     * �������ӵ�����
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
            // IP�Ѿ�����
            return addResult;
        }
        if (collecttype == SystemConstant.COLLECTTYPE_SNMP) {
            addResult = isOpenSNMP(ipAddress, community);
            if (addResult < 0) {
                // δ�� SNMP
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

        // �����޸�

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
        SysLogger.info("��ʼɾ���豸................" + host.getIpAddress());
        NodeDTO node = nodeUtil.conversionToNodeDTO(host);
        
        NodeDomainService domainService = new NodeDomainService();
		NodeDomain nodeDomain = domainService.getNodeDomain(node);
        domainService.delete(node);

        String ip = host.getIpAddress();
        String allipstr = SysUtil.doip(ip);

        // ɾ����
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
//          ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
//          ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
//          ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�
//          // ���ڲɼ�
//          // ctable.deleteTable(conn,"flash",allipstr,"flash");//����
//          // ctable.deleteTable(conn,"flashhour",allipstr,"flashhour");//����
//          // ctable.deleteTable(conn,"flashday",allipstr,"flashday");//����
//
//          ctable.deleteTable(conn, "buffer", allipstr, "buffer");// ����
//          ctable.deleteTable(conn, "bufferhour", allipstr, "bufferhour");// ����
//          ctable.deleteTable(conn, "bufferday", allipstr, "bufferday");// ����
//          // ���ڲɼ�
//          // ctable.deleteTable(conn,"temper",allipstr,"temper");//�¶�
//          // ctable.deleteTable(conn,"temperhour",allipstr,"temperhour");//�¶�
//          // ctable.deleteTable(conn,"temperday",allipstr,"temperday");//�¶�
//          //
//          // ctable.deleteTable(conn,"fan",allipstr,"fan");//����
//          // ctable.deleteTable(conn,"fanhour",allipstr,"fanhour");//����
//          // ctable.deleteTable(conn,"fanday",allipstr,"fanday");//����
//          //                
//          // ctable.deleteTable(conn,"power",allipstr,"power");//��Դ
//          // ctable.deleteTable(conn,"powerhour",allipstr,"powerhour");//��Դ
//          // ctable.deleteTable(conn,"powerday",allipstr,"powerday");//��Դ
//          //                
//          // ctable.deleteTable(conn,"vol",allipstr,"vol");//��ѹ
//          // ctable.deleteTable(conn,"volhour",allipstr,"volhour");//��ѹ
//          // ctable.deleteTable(conn,"volday",allipstr,"volday");//��ѹ
//
//          ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");// �����˿�����
//          ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");// �����˿�����
//          ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");// �����˿�����
//
//          ctable.deleteTable(conn, "allutilhdx", allipstr, "allhdx");// �˿�������
//          ctable.deleteTable(conn, "allutilhdxhour", allipstr,
//                  "allhdxhour");// �˿�������
//          ctable
//                  .deleteTable(conn, "allutilhdxday", allipstr,
//                          "allhdxday");// �˿�������
//
//          ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");// ����
//          ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");// ����
//          ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");// ����
//
//          ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");// ������
//          ctable.deleteTable(conn, "dcardperchour", allipstr,
//                  "dcardperchour");// ������
//          ctable.deleteTable(conn, "dcardpercday", allipstr,
//                  "dcardpercday");// ������
//
//          ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");// �����
//          ctable
//                  .deleteTable(conn, "errperchour", allipstr,
//                          "errperchour");// �����
//          ctable.deleteTable(conn, "errpercday", allipstr, "errpercday");// �����
//
//          ctable.deleteTable(conn, "packs", allipstr, "packs");// ���ݰ�����
//          ctable.deleteTable(conn, "packshour", allipstr, "packshour");// ���ݰ�����
//          ctable.deleteTable(conn, "packsday", allipstr, "packsday");// ���ݰ�����
//
//          ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");// ���ݰ�����
//          ctable
//                  .deleteTable(conn, "inpackshour", allipstr,
//                          "inpackshour");// ���ݰ�����
//          ctable.deleteTable(conn, "inpacksday", allipstr, "inpacksday");// ���ݰ�����
//
//          ctable.deleteTable(conn, "outpacks", allipstr, "outpacks"); // ���ݰ�����
//          ctable.deleteTable(conn, "outpackshour", allipstr,
//                  "outpackshour");// ���ݰ�����
//          ctable
//                  .deleteTable(conn, "outpacksday", allipstr,
//                          "outpacksday");// ���ݰ�����
//
//          ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
//          ctable.deleteTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
//          ctable.deleteTable(conn, "proday", allipstr, "proday");// ������
//
//          ctable.deleteTable(conn, "cpudtl", allipstr, "cpudtl");
//          ctable.deleteTable(conn, "cpudtlhour", allipstr, "cpudtlhour");
//          ctable.deleteTable(conn, "cpudtlday", allipstr, "cpudtlday");
//
//          ctable.deleteTable(conn, "disk", allipstr, "disk");// ����������
//          ctable.deleteTable(conn, "diskhour", allipstr, "diskhour");// ����������
//          ctable.deleteTable(conn, "diskday", allipstr, "diskday");// ����������
//
//          ctable.deleteTable(conn, "diskincre", allipstr, "diskincre");// ����������yangjun
//          ctable.deleteTable(conn, "diskincrehour", allipstr,
//                  "diskincrehour");// ����������Сʱ
//          ctable.deleteTable(conn, "diskincreday", allipstr,
//                  "diskincreday");// ������������
//
//          ctable.deleteTable(conn, "pgused", allipstr, "pgused");// ���ɻ�ҳ��
//          ctable.deleteTable(conn, "pgusedhour", allipstr, "pgusedhour");// ���ɻ�ҳ��
//          ctable.deleteTable(conn, "pgusedday", allipstr, "pgusedday");// ���ɻ�ҳ��
//
            ctable.deleteTable(conn, "portstatus", allipstr, "port");// �˿�״̬
            // ctable.deleteTable(conn,"portstatushour",allipstr,"porthour");
            // ctable.deleteTable(conn,"portstatusday",allipstr,"portday");

            ctable.deleteTable(conn, "nms_interface_data_temp", allipstr,
                    "interface");// �˿�״̬

            ctable.deleteTable(conn, "nms_process_data_temp", allipstr,
                    "process");// �˿�״̬

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
            // ͬʱɾ���¼�������������
            eventdao.delete(host.getId(), node.getType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventdao.close();
        }

        PortconfigDao portconfigdao = new PortconfigDao();
        try {
            // ͬʱɾ���˿����ñ�����������
            portconfigdao.deleteByIpaddress(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigdao.close();
        }

        // ɾ��nms_ipmacchange����Ķ�Ӧ������
        IpMacChangeDao macchangebasedao = new IpMacChangeDao();
        try {
            // delte��,conn�Ѿ��ر�
            macchangebasedao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            macchangebasedao.close();
        }

        // ɾ�������豸�����ļ�����Ķ�Ӧ������
        NetNodeCfgFileDao configdao = new NetNodeCfgFileDao();
        try {
            // delte��,conn�Ѿ��ر�
            configdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }

        // ɾ�������豸SYSLOG���ձ���Ķ�Ӧ������
        NetSyslogDao syslogdao = new NetSyslogDao();
        try {
            // delte��,conn�Ѿ��ر�
            syslogdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            syslogdao.close();
        }

        // ɾ�������豸�˿�ɨ�����Ķ�Ӧ������
        PortScanDao portscandao = new PortScanDao();
        try {
            // delte��,conn�Ѿ��ر�
            portscandao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portscandao.close();
        }

        // ɾ�������豸���ͼ����Ķ�Ӧ������
        IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
        try {
            // delte��,conn�Ѿ��ر�
            addresspaneldao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            addresspaneldao.close();
        }

        // ɾ�������豸�ӿڱ���Ķ�Ӧ������
        HostInterfaceDao interfacedao = new HostInterfaceDao();
        try {
            // delte��,conn�Ѿ��ر�
            interfacedao.deleteByHostId(host.getId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfacedao.close();
        }

        // ɾ�������豸IP��������Ķ�Ӧ������
        IpAliasDao ipaliasdao = new IpAliasDao();
        try {
            // delte��,conn�Ѿ��ر�
            ipaliasdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipaliasdao.close();
        }

        // ɾ�������豸�ֹ����õ���·����Ķ�Ӧ������
        RepairLinkDao repairdao = new RepairLinkDao();
        try {
            // delte��,conn�Ѿ��ر�
            repairdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            repairdao.close();
        }

        // ɾ�������豸IPMAC����Ķ�Ӧ������
        IpMacDao ipmacdao = new IpMacDao();
        try {
            // delte��,conn�Ѿ��ر�
            ipmacdao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipmacdao.close();
        }

        // ɾ�����豸�Ĳɼ�ָ��
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

        // ɾ�������豸ָ��ɼ�����Ķ�Ӧ������
        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
        try {
            // delte��,conn�Ѿ��ر�
            indicatdao.deleteByNodeId(host.getId() + "",
                    node.getType(), node.getSubtype());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indicatdao.close();
        }

        // ɾ��IP-MAC-BASE����Ķ�Ӧ������
        IpMacBaseDao macbasedao = new IpMacBaseDao();
        try {
            // delte��,conn�Ѿ��ر�
            macbasedao.deleteByHostIp(host.getIpAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            macbasedao.close();
        }

        // ɾ��diskconfig
        String[] otherTempData = new String[] { "nms_diskconfig" };
        String[] ipStrs = new String[] { host.getIpAddress() };
        ctable.clearTablesData(otherTempData, "ipaddress", ipStrs);
        
        // ɾ�������������
        ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
        processGroupConfigurationUtil
                .deleteProcessGroupAndConfigurationByNodeid(host
                        .getId()
                        + "");

        
        // 2.����xml
        if (host.getCategory() < 4) {
            // �����豸
            XmlOperator opr = new XmlOperator();
            opr.setFile("network.jsp");
            opr.init4updateXml();
            opr.deleteNodeByID(host.getId() + "");
            // opr.addNode(helper.getHost());
            opr.writeXml();
        } else if (host.getCategory() == 4) {
            // ����������
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
        // ������ɾ��(guangfei edite)
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

        // ����ҵ����ͼ
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

        // �û��������
        Calendar tempCal = Calendar.getInstance();
        Date cc = tempCal.getTime();
        String time = sdf1.format(cc);
        UserAuditUtil useraudit = new UserAuditUtil();
        String useraction = "";
        useraction = useraction + "ɾ���豸 IP:" + host.getIpAddress()
                + " ����:" + host.getAlias() + " ����:" + host.getType();
        try {
            useraudit.saveUserAudit(user, time, useraction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        // ɾ���豸����ʱ�����д洢������
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