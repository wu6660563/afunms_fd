/**
 * <p>Description:operate table NMS_DISCOVER_CONDITION</p>
 * ��Ҫ���ڷ�����֮��������� 
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.NetworkUtil;
import com.afunms.common.util.PollDataUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.dataArchiving.service.DataArchivingService;
import com.afunms.discovery.DiscoverEngine;
import com.afunms.discovery.Host;
import com.afunms.discovery.IfEntity;
import com.afunms.discovery.KeyGenerator;
import com.afunms.discovery.Link;
import com.afunms.discovery.RepairLink;
import com.afunms.discovery.SubNet;
import com.afunms.event.dao.NetSyslogRuleDao;
import com.afunms.event.model.NetSyslogRule;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.monitor.executor.base.MonitorFactory;
import com.afunms.monitor.item.base.MonitorObject;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.snmp.LoadAixFile;
import com.afunms.polling.snmp.LoadHpUnixFile;
import com.afunms.polling.snmp.LoadLinuxFile;
import com.afunms.polling.snmp.LoadSunOSFile;
import com.afunms.polling.snmp.LoadWindowsWMIFile;
import com.afunms.topology.model.ConnectTypeConfig;
import com.afunms.topology.model.HostNode;

public class DiscoverCompleteDao extends BaseDao {
    private int nmID = 0;
    private int telnetID = 0;
    private static final List moList = MonitorFactory.getMonitorObjectList();

    public static List<String> TABLE_NAME_LIST = DataArchivingService.TABLE_NAME_LIST;

    public DiscoverCompleteDao() {
        super("topo_host_node");
        nmID = getNextID("topo_node_monitor");
    }

    /**
     * �������
     */
    public void clear() {

        // ��ʼ������еĽڵ���ʷ������Ϣ �޸���:hukelei �޸�ʱ��: 2010-07-28
        conn.addBatch("delete from topo_host_node");

        // ��ʼ������е���·��ʷ������Ϣ �޸���:hukelei �޸�ʱ��: 2010-07-28
        LinkDao linkdao = new LinkDao();
        List linkList = new ArrayList();
        try {
            linkList = linkdao.loadByTpye(0);
            linkdao = new LinkDao();
            linkdao.deleteutils(linkList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            linkdao.close();
        }
        // ����������е���·��ʷ������Ϣ �޸���:hukelei �޸�ʱ��: 2010-07-28
        conn.addBatch("delete from topo_network_link");

        conn.addBatch("delete from topo_subnet");
        conn.addBatch("delete from topo_node_monitor");
        conn.addBatch("delete from topo_node_multi_data");
        conn.addBatch("delete from topo_node_single_data");
        conn.addBatch("delete from topo_interface");
        conn.addBatch("delete from topo_interface_data");
        conn.addBatch("delete from topo_custom_xml");
        conn.addBatch("delete from nms_alarm_message");
        conn.addBatch("delete from server_telnet_config");
        conn.addBatch("delete from app_ip_node");
        conn.addBatch("delete from app_tomcat_node");
        conn.addBatch("delete from app_db_node");
        conn
                .addBatch("delete from system_eventlist where subtype='host' or subtype='net' or subtype='db'");
        conn.executeBatch();
        nmID = 0;
    }

    public void addID() {
        conn.executeUpdate("update topo_node_id set id="
                + KeyGenerator.getInstance().getHostKey());
    }

    /**
     * ������·����
     */
    public void addLinkData(List linkList) {
        if (linkList == null)
            return;

        RepairLinkDao repairdao = new RepairLinkDao();
        List repairlist = repairdao.loadAll();
        if (repairlist == null)
            repairlist = new ArrayList();
        for (int i = 0; i < linkList.size(); i++) {
            Link link = (Link) linkList.get(i);

            for (int k = 0; k < repairlist.size(); k++) {
                RepairLink repairlink = (RepairLink) repairlist.get(k);
                Host starthost = DiscoverEngine.getInstance().getHostByID(
                        link.getStartId());
                Host endhost = DiscoverEngine.getInstance().getHostByID(
                        link.getEndId());
                SysLogger.info("================================");
                SysLogger.info(starthost.getIpAddress() + "=="
                        + repairlink.getStartIp() + "==" + link.getStartIndex()
                        + "==" + repairlink.getStartIndex());
                SysLogger.info(endhost.getIpAddress() + "=="
                        + repairlink.getEndIp() + "==" + link.getEndIndex()
                        + "==" + repairlink.getEndIndex());
                SysLogger.info("================================");
                if (starthost.getIpAddress().equalsIgnoreCase(
                        repairlink.getStartIp())
                        && link.getStartIndex().equalsIgnoreCase(
                                repairlink.getStartIndex())
                        && endhost.getIpAddress().equalsIgnoreCase(
                                repairlink.getEndIp())
                        && link.getEndIndex().equalsIgnoreCase(
                                repairlink.getEndIndex())) {
                    // �����޸Ĺ������ӹ�ϵ
                    SysLogger.info("�����޸Ĺ������ӹ�ϵ!" + starthost.getIpAddress()
                            + "===" + endhost.getIpAddress());
                    link.setStartIndex(repairlink.getNewStartIndex());
                    link.setEndIndex(repairlink.getNewEndIndex());
                    link.setStartDescr(starthost.getIfEntityByIndex(
                            repairlink.getNewStartIndex()).getDescr());
                    link.setEndDescr(endhost.getIfEntityByIndex(
                            repairlink.getNewEndIndex()).getDescr());
                    link.setStartPort(starthost.getIfEntityByIndex(
                            repairlink.getNewStartIndex()).getPort());
                    link.setEndPort(endhost.getIfEntityByIndex(
                            repairlink.getNewEndIndex()).getPort());
                    linkList.set(i, link);
                } else if (starthost.getIpAddress().equalsIgnoreCase(
                        repairlink.getEndIp())
                        && link.getStartIndex().equalsIgnoreCase(
                                repairlink.getEndIndex())
                        && endhost.getIpAddress().equalsIgnoreCase(
                                repairlink.getStartIp())
                        && link.getEndIndex().equalsIgnoreCase(
                                repairlink.getStartIndex())) {
                    // �����޸Ĺ������ӹ�ϵ
                    SysLogger.info("�����޸Ĺ������ӹ�ϵ!" + starthost.getIpAddress()
                            + "===" + endhost.getIpAddress());
                    link.setStartIndex(repairlink.getNewEndIndex());
                    link.setEndIndex(repairlink.getNewStartIndex());
                    link.setStartDescr(endhost.getIfEntityByIndex(
                            repairlink.getNewEndIndex()).getDescr());
                    link.setEndDescr(starthost.getIfEntityByIndex(
                            repairlink.getNewStartIndex()).getDescr());
                    link.setStartPort(endhost.getIfEntityByIndex(
                            repairlink.getNewEndIndex()).getPort());
                    link.setEndPort(starthost.getIfEntityByIndex(
                            repairlink.getNewStartIndex()).getPort());
                    linkList.set(i, link);
                }
            }
        }

        int id = getNextID("topo_network_link");
        for (int i = 0; i < linkList.size(); i++) {
            id = id + 1;
            Link link = (Link) linkList.get(i);
            String startPhysAddress = link.getStartPhysAddress();
            String endPhysAddress = link.getEndPhysAddress();
            if (startPhysAddress != null) {
                startPhysAddress = CommonUtil
                        .removeIllegalStr(startPhysAddress);
            } else {
                startPhysAddress = "";
            }
            if (endPhysAddress != null) {
                endPhysAddress = CommonUtil.removeIllegalStr(endPhysAddress);
            } else {
                endPhysAddress = "";
            }
            StringBuffer sql = new StringBuffer(300);
            sql
                    .append("insert into topo_network_link(id,link_name,start_id,end_id,start_ip,end_ip,start_index,");
            sql
                    .append("end_index,start_descr,end_descr,start_port,end_port,start_mac,end_mac,assistant,type,findtype,linktype)values(");
            sql.append(id);
            sql.append(",'");
            // sql.append(link.getStartIp()+"_"+link.getStartPort()+"/"+link.getEndIp()+"_"+link.getEndPort());
            sql.append(link.getStartIp() + "_" + link.getStartIndex() + "/"
                    + link.getEndIp() + "_" + link.getEndIndex());
            sql.append("',");
            sql.append(link.getStartId());
            sql.append(",");
            sql.append(link.getEndId());
            sql.append(",'");
            sql.append(link.getStartIp());
            sql.append("','");
            sql.append(link.getEndIp());
            sql.append("','");
            sql.append(link.getStartIndex());
            sql.append("','");
            sql.append(link.getEndIndex());
            sql.append("','");
            sql.append(link.getStartDescr());
            sql.append("','");
            sql.append(link.getEndDescr());
            sql.append("','");
            sql.append(link.getStartPort() == null ? "" : link.getStartPort());
            sql.append("','");
            sql.append(link.getEndPort() == null ? "" : link.getEndPort());
            sql.append("','");
            sql.append(startPhysAddress);
            sql.append("','");
            sql.append(endPhysAddress);
            sql.append("',");
            sql.append(link.getAssistant());
            sql.append(",");
            sql.append(1);
            sql.append(",");
            sql.append(link.getFindtype());
            sql.append(",");
            sql.append(link.getLinktype());
            sql.append(")");
            SysLogger.info(sql.toString());
            conn.executeUpdate(sql.toString(), false);

            // ������Ӧ����·��ʷ���ݱ� �޸���:hukelei 2010-07-28
            CreateTableManager ctable = new CreateTableManager();
            try {
                ctable.createTable(conn, "lkping", (id) + "", "lkping");// ��·״̬
                ctable.createTable(conn, "lkpinghour", (id) + "", "lkpinghour");// ��·״̬��Сʱ
                ctable.createTable(conn, "lkpingday", (id) + "", "lkpingday");// ��·״̬����

                ctable.createTable(conn, "lkuhdx", (id) + "", "lkuhdx");// ��·����
                ctable.createTable(conn, "lkuhdxhour", (id) + "", "lkuhdxhour");// ��·����
                ctable.createTable(conn, "lkuhdxday", (id) + "", "lkuhdxday");// ��·����

                ctable.createTable(conn, "lkuhdxp", (id) + "", "lkuhdxp");// ��·����������
                ctable.createTable(conn, "lkuhdxphour", (id) + "",
                        "lkuhdxphour");// ��·����������
                ctable.createTable(conn, "lkuhdxpday", (id) + "", "lkuhdxpday");// ��·����������

                conn.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // conn.close();
            }
        }
        conn.commit();
    }

    /**
     * ���������ӿ�����
     */
    public void addInterfaceData(List hostList) {
        int id = getNextID("topo_interface");
        for (int i = 0; i < hostList.size(); i++) {
            com.afunms.discovery.Host host = (com.afunms.discovery.Host) hostList
                    .get(i);
            List ifList = host.getIfEntityList();
            if (ifList == null)
                continue;

            for (int j = 0; j < ifList.size(); j++) {
                com.afunms.discovery.IfEntity ifEntity = (com.afunms.discovery.IfEntity) ifList
                        .get(j);
                String physAddress = ifEntity.getPhysAddress();
                physAddress = CommonUtil.removeIllegalStr(physAddress).replace("\"", "");
                StringBuffer sql = new StringBuffer(300);
                sql.append("insert into topo_interface(id,node_id,entity,descr,port,speed,phys_address,ip_address,oper_status,type,chassis,slot,uport)values(");
                sql.append(id++);
                sql.append(",");
                sql.append(host.getId());
                sql.append(",\"");
                sql.append(ifEntity.getIndex());
                sql.append("\",\"");
                sql.append(replace(ifEntity.getDescr()));
                sql.append("\",\"");
                sql
                        .append(ifEntity.getPort() == null ? "" : ifEntity
                                .getPort());
                sql.append("\",\"");
                sql.append(ifEntity.getSpeed());
                sql.append("\",\"");
                sql.append(physAddress);
                sql.append("\",\"");
                sql.append(ifEntity.getIpList()); // ����IP��ַ
                sql.append("\",");
                sql.append(ifEntity.getOperStatus());
                sql.append(",");
                sql.append(ifEntity.getType()); // �˿�����
                sql.append(",");
                sql.append(ifEntity.getChassis()); // ���
                sql.append(",");
                sql.append(ifEntity.getSlot()); // ��
                sql.append(",");
                sql.append(ifEntity.getUport()); // ��
                sql.append(")");
                conn.executeUpdate(sql.toString(), false);
            }// end_for_j
            conn.commit();
            conn.executeUpdate("update topo_interface set alias=descr");
        }// end_for_i
    }

    /**
     * ������������,ͬʱ���������豸IP�ı���
     */
    public void addHostData(List hostList) {
        Hashtable donehost = new Hashtable();
        for (int i = 0; i < hostList.size(); i++) {
            try {
                hostList.get(i).toString();
                Host node = (Host) hostList.get(i);
                if (donehost.containsKey(node.getId()))
                    continue;
                node.setBid(DiscoverEngine.getInstance().getDiscover_bid());
                // �������ɱ�
                String ip = node.getIpAddress();
                // String ip1 ="",ip2="",ip3="",ip4="";
                // String[] ipdot = ip.split(".");
                // String tempStr = "";
                String allipstr = SysUtil.doip(ip);
                // if (ip.indexOf(".")>0){
                // ip1=ip.substring(0,ip.indexOf("."));
                // ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());
                // tempStr =
                // ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
                // }
                // ip2=tempStr.substring(0,tempStr.indexOf("."));
                // ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
                // allipstr=ip1+ip2+ip3+ip4;
                CreateTableManager ctable = new CreateTableManager();
                if ((node.getCategory() > 0 && node.getCategory() < 4)
                        || node.getCategory() == 7) {
                    try {
                        if (DiscoverEngine.getInstance() == null) {
                            // SysLogger.info("DiscoverEngine ===== null");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // SysLogger.info(DiscoverEngine.getInstance().getDiscovermodel()+"=====");
                    if (DiscoverEngine.getInstance().getDiscovermodel() == 1) {
                        // ���䷢��
                        if (node.getDiscoverstatus() == -1) {
                            // �·��ֵ��豸

                            // ���������豸��
                            // ��ͨ��
                            ctable.createTable(conn, "ping", allipstr, "ping");// Ping
                            ctable.createTable(conn, "pinghour", allipstr,
                                    "pinghour");// Ping
                            ctable.createTable(conn, "pingday", allipstr,
                                    "pingday");// Ping

                            // �ڴ�
                            ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                            ctable.createTable(conn, "memoryhour", allipstr,
                                    "memhour");// �ڴ�
                            ctable.createTable(conn, "memoryday", allipstr,
                                    "memday");// �ڴ�

                            ctable
                                    .createTable(conn, "flash", allipstr,
                                            "flash");// ����
                            ctable.createTable(conn, "flashhour", allipstr,
                                    "flashhour");// ����
                            ctable.createTable(conn, "flashday", allipstr,
                                    "flashday");// ����

                            ctable.createTable(conn, "buffer", allipstr,
                                    "buffer");// ����
                            ctable.createTable(conn, "bufferhour", allipstr,
                                    "bufferhour");// ����
                            ctable.createTable(conn, "bufferday", allipstr,
                                    "bufferday");// ����

                            ctable.createTable(conn, "fan", allipstr, "fan");// ����
                            ctable.createTable(conn, "fanhour", allipstr,
                                    "fanhour");// ����
                            ctable.createTable(conn, "fanday", allipstr,
                                    "fanday");// ����

                            ctable
                                    .createTable(conn, "power", allipstr,
                                            "power");// ��Դ
                            ctable.createTable(conn, "powerhour", allipstr,
                                    "powerhour");// ��Դ
                            ctable.createTable(conn, "powerday", allipstr,
                                    "powerday");// ��Դ

                            ctable.createTable(conn, "vol", allipstr, "vol");// ��ѹ
                            ctable.createTable(conn, "volhour", allipstr,
                                    "volhour");// ��ѹ
                            ctable.createTable(conn, "volday", allipstr,
                                    "volday");// ��ѹ

                            // CPU
                            ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                            ctable.createTable(conn, "cpuhour", allipstr,
                                    "cpuhour");// CPU
                            ctable.createTable(conn, "cpuday", allipstr,
                                    "cpuday");// CPU

                            // ����������
                            ctable.createTable(conn, "utilhdxperc", allipstr,
                                    "hdperc");
                            ctable.createTable(conn, "hdxperchour", allipstr,
                                    "hdperchour");
                            ctable.createTable(conn, "hdxpercday", allipstr,
                                    "hdpercday");

                            // ÿ���˿�����
                            ctable
                                    .createTable(conn, "utilhdx", allipstr,
                                            "hdx");
                            ctable.createTable(conn, "utilhdxhour", allipstr,
                                    "hdxhour");
                            ctable.createTable(conn, "utilhdxday", allipstr,
                                    "hdxday");

                            // �ۺ�����
                            ctable.createTable(conn, "allutilhdx", allipstr,
                                    "allhdx");
                            ctable.createTable(conn, "allutilhdxhour",
                                    allipstr, "allhdxhour");
                            ctable.createTable(conn, "allutilhdxday", allipstr,
                                    "allhdxday");

                            // �ؼ��˿�״̬
                            ctable.createTable(conn, "portstatus", allipstr,
                                    "port");

                            // ������
                            ctable.createTable(conn, "discardsperc", allipstr,
                                    "dcardperc");
                            ctable.createTable(conn, "dcardperchour", allipstr,
                                    "dcardperchour");
                            ctable.createTable(conn, "dcardpercday", allipstr,
                                    "dcardpercday");

                            // ������
                            ctable.createTable(conn, "errorsperc", allipstr,
                                    "errperc");
                            ctable.createTable(conn, "errperchour", allipstr,
                                    "errperchour");
                            ctable.createTable(conn, "errpercday", allipstr,
                                    "errpercday");

                            // ���ݰ�
                            ctable
                                    .createTable(conn, "packs", allipstr,
                                            "packs");
                            ctable.createTable(conn, "packshour", allipstr,
                                    "packshour");
                            ctable.createTable(conn, "packsday", allipstr,
                                    "packsday");

                            // ������ݿ��
                            ctable.createTable(conn, "inpacks", allipstr,
                                    "inpacks");
                            ctable.createTable(conn, "inpackshour", allipstr,
                                    "inpackshour");
                            ctable.createTable(conn, "inpacksday", allipstr,
                                    "inpacksday");

                            // �������ݰ�
                            ctable.createTable(conn, "outpacks", allipstr,
                                    "outpacks");
                            ctable.createTable(conn, "outpackshour", allipstr,
                                    "outpackshour");
                            ctable.createTable(conn, "outpacksday", allipstr,
                                    "outpacksday");

                            // �¶�
                            ctable.createTable(conn, "temper", allipstr,
                                    "temper");
                            ctable.createTable(conn, "temperhour", allipstr,
                                    "temperhour");
                            ctable.createTable(conn, "temperday", allipstr,
                                    "temperday");
                        }
                    } else {
                        // ���·���
                        // ��ɾ�������豸��

                        // //��ͨ�ʱ�
                        // ctable.deleteTable(conn,"ping",allipstr,"ping");//Ping
                        // ctable.deleteTable(conn,"pinghour",allipstr,"pinghour");//Ping
                        // ctable.deleteTable(conn,"pingday",allipstr,"pingday");//Ping
                        //					
                        // //�ڴ��
                        // ctable.deleteTable(conn,"memory",allipstr,"mem");//�ڴ�
                        // ctable.deleteTable(conn,"memoryhour",allipstr,"memhour");//�ڴ�
                        // ctable.deleteTable(conn,"memoryday",allipstr,"memday");//�ڴ�
                        //					
                        // ctable.deleteTable(conn,"flash",allipstr,"flash");//����
                        // ctable.deleteTable(conn,"flashhour",allipstr,"flashhour");//����
                        // ctable.deleteTable(conn,"flashday",allipstr,"flashday");//����
                        //					
                        // ctable.deleteTable(conn,"buffer",allipstr,"buffer");//����
                        // ctable.deleteTable(conn,"bufferhour",allipstr,"bufferhour");//����
                        // ctable.deleteTable(conn,"bufferday",allipstr,"bufferday");//����
                        //					
                        // ctable.deleteTable(conn,"fan",allipstr,"fan");//����
                        // ctable.deleteTable(conn,"fanhour",allipstr,"fanhour");//����
                        // ctable.deleteTable(conn,"fanday",allipstr,"fanday");//����
                        //					
                        // ctable.deleteTable(conn,"power",allipstr,"power");//��Դ
                        // ctable.deleteTable(conn,"powerhour",allipstr,"powerhour");//��Դ
                        // ctable.deleteTable(conn,"powerday",allipstr,"powerday");//��Դ
                        //					
                        // ctable.deleteTable(conn,"vol",allipstr,"vol");//��ѹ
                        // ctable.deleteTable(conn,"volhour",allipstr,"volhour");//��ѹ
                        // ctable.deleteTable(conn,"volday",allipstr,"volday");//��ѹ
                        //					
                        // //CPU��
                        // ctable.deleteTable(conn,"cpu",allipstr,"cpu");//CPU
                        // ctable.deleteTable(conn,"cpuhour",allipstr,"cpuhour");//CPU
                        // ctable.deleteTable(conn,"cpuday",allipstr,"cpuday");//CPU
                        //					
                        // //���������ʱ�
                        // ctable.deleteTable(conn,"utilhdxperc",allipstr,"hdperc");
                        // ctable.deleteTable(conn,"hdxperchour",allipstr,"hdperchour");
                        // ctable.deleteTable(conn,"hdxpercday",allipstr,"hdpercday");
                        //					
                        // //���ٱ�
                        // ctable.deleteTable(conn,"utilhdx",allipstr,"hdx");
                        // ctable.deleteTable(conn,"utilhdxhour",allipstr,"hdxhour");
                        // ctable.deleteTable(conn,"utilhdxday",allipstr,"hdxday");
                        //					
                        // //�ۺ�����
                        // ctable.deleteTable(conn,"allutilhdx",allipstr,"allhdx");
                        // ctable.deleteTable(conn,"allutilhdxhour",allipstr,"allhdxhour");
                        // ctable.deleteTable(conn,"allutilhdxday",allipstr,"allhdxday");
                        //					
                        // //�����ʱ�
                        // ctable.deleteTable(conn,"discardsperc",allipstr,"dcardperc");
                        // ctable.deleteTable(conn,"dcardperchour",allipstr,"dcardperchour");
                        // ctable.deleteTable(conn,"dcardpercday",allipstr,"dcardpercday");
                        //					
                        // //�����ʱ�
                        // ctable.deleteTable(conn,"errorsperc",allipstr,"errperc");
                        // ctable.deleteTable(conn,"errperchour",allipstr,"errperchour");
                        // ctable.deleteTable(conn,"errpercday",allipstr,"errpercday");
                        //					
                        // //���ݰ���
                        // ctable.deleteTable(conn,"packs",allipstr,"packs");
                        // ctable.deleteTable(conn,"packshour",allipstr,"packshour");
                        // ctable.deleteTable(conn,"packsday",allipstr,"packsday");
                        //					
                        // //������ݰ���
                        // ctable.deleteTable(conn,"inpacks",allipstr,"inpacks");
                        // ctable.deleteTable(conn,"inpackshour",allipstr,"inpackshour");
                        // ctable.deleteTable(conn,"inpacksday",allipstr,"inpacksday");
                        //					
                        // //�������ݰ���
                        // ctable.deleteTable(conn,"outpacks",allipstr,"outpacks");
                        // ctable.deleteTable(conn,"outpackshour",allipstr,"outpackshour");
                        // ctable.deleteTable(conn,"outpacksday",allipstr,"outpacksday");
                        //					
                        // //�¶ȱ�
                        // ctable.deleteTable(conn,"temper",allipstr,"temper");
                        // ctable.deleteTable(conn,"temperhour",allipstr,"temperhour");
                        // ctable.deleteTable(conn,"temperday",allipstr,"temperday");

                        // ���������豸��
                        // ��ͨ�ʱ�
                        ctable.createTable(conn, "ping", allipstr, "ping");// Ping
                        ctable.createTable(conn, "pinghour", allipstr,
                                "pinghour");// Ping
                        ctable
                                .createTable(conn, "pingday", allipstr,
                                        "pingday");// Ping

                        // �ڴ��
                        ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.createTable(conn, "memoryhour", allipstr,
                                "memhour");// �ڴ�
                        ctable.createTable(conn, "memoryday", allipstr,
                                "memday");// �ڴ�

                        ctable.createTable(conn, "flash", allipstr, "flash");// ����
                        ctable.createTable(conn, "flashhour", allipstr,
                                "flashhour");// ����
                        ctable.createTable(conn, "flashday", allipstr,
                                "flashday");// ����

                        ctable.createTable(conn, "buffer", allipstr, "buffer");// ����
                        ctable.createTable(conn, "bufferhour", allipstr,
                                "bufferhour");// ����
                        ctable.createTable(conn, "bufferday", allipstr,
                                "bufferday");// ����

                        ctable.createTable(conn, "fan", allipstr, "fan");// ����
                        ctable
                                .createTable(conn, "fanhour", allipstr,
                                        "fanhour");// ����
                        ctable.createTable(conn, "fanday", allipstr, "fanday");// ����

                        ctable.createTable(conn, "power", allipstr, "power");// ��Դ
                        ctable.createTable(conn, "powerhour", allipstr,
                                "powerhour");// ��Դ
                        ctable.createTable(conn, "powerday", allipstr,
                                "powerday");// ��Դ

                        ctable.createTable(conn, "vol", allipstr, "vol");// ��ѹ
                        ctable
                                .createTable(conn, "volhour", allipstr,
                                        "volhour");// ��ѹ
                        ctable.createTable(conn, "volday", allipstr, "volday");// ��ѹ

                        // CPU
                        ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable
                                .createTable(conn, "cpuhour", allipstr,
                                        "cpuhour");// CPU
                        ctable.createTable(conn, "cpuday", allipstr, "cpuday");// CPU

                        // ���������ʱ�
                        ctable.createTable(conn, "utilhdxperc", allipstr,
                                "hdperc");
                        ctable.createTable(conn, "hdxperchour", allipstr,
                                "hdperchour");
                        ctable.createTable(conn, "hdxpercday", allipstr,
                                "hdpercday");

                        // ����
                        ctable.createTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.createTable(conn, "utilhdxhour", allipstr,
                                "hdxhour");
                        ctable.createTable(conn, "utilhdxday", allipstr,
                                "hdxday");

                        // �ۺ�����
                        ctable.createTable(conn, "allutilhdx", allipstr,
                                "allhdx");
                        ctable.createTable(conn, "allutilhdxhour", allipstr,
                                "allhdxhour");
                        ctable.createTable(conn, "allutilhdxday", allipstr,
                                "allhdxday");

                        // �ؼ��˿�״̬
                        ctable
                                .createTable(conn, "portstatus", allipstr,
                                        "port");

                        // ����
                        ctable.createTable(conn, "discardsperc", allipstr,
                                "dcardperc");
                        ctable.createTable(conn, "dcardperchour", allipstr,
                                "dcardperchour");
                        ctable.createTable(conn, "dcardpercday", allipstr,
                                "dcardpercday");

                        // ������
                        ctable.createTable(conn, "errorsperc", allipstr,
                                "errperc");
                        ctable.createTable(conn, "errperchour", allipstr,
                                "errperchour");
                        ctable.createTable(conn, "errpercday", allipstr,
                                "errpercday");

                        // ���ݰ�
                        ctable.createTable(conn, "packs", allipstr, "packs");
                        ctable.createTable(conn, "packshour", allipstr,
                                "packshour");
                        ctable.createTable(conn, "packsday", allipstr,
                                "packsday");

                        // ������ݰ�
                        ctable
                                .createTable(conn, "inpacks", allipstr,
                                        "inpacks");
                        ctable.createTable(conn, "inpackshour", allipstr,
                                "inpackshour");
                        ctable.createTable(conn, "inpacksday", allipstr,
                                "inpacksday");

                        // �������ݰ�
                        ctable.createTable(conn, "outpacks", allipstr,
                                "outpacks");
                        ctable.createTable(conn, "outpackshour", allipstr,
                                "outpackshour");
                        ctable.createTable(conn, "outpacksday", allipstr,
                                "outpacksday");

                        // �¶�
                        ctable.createTable(conn, "temper", allipstr, "temper");
                        ctable.createTable(conn, "temperhour", allipstr,
                                "temperhour");
                        ctable.createTable(conn, "temperday", allipstr,
                                "temperday");
                    }
                } else if (node.getCategory() == 4) {
                    // �����豸
                    try {
                        if (DiscoverEngine.getInstance() == null) {
                            // SysLogger.info("DiscoverEngine ===== null");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // SysLogger.info(DiscoverEngine.getInstance().getDiscovermodel()+"=====");
                    if (DiscoverEngine.getInstance().getDiscovermodel() == 1) {
                        // ���䷢��
                        if (node.getDiscoverstatus() == -1) {
                            // �·��ֵ��豸
                            // ���������豸��
                            ctable.createTable(conn, "pro", allipstr, "pro");// ����
                            ctable.createTable(conn, "prohour", allipstr,
                                    "prohour");// ����Сʱ
                            ctable.createTable(conn, "proday", allipstr,
                                    "proday");// ������

                            ctable.createSyslogTable(conn, "log", allipstr,
                                    "log");// ������

                            ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                            ctable.createTable(conn, "memoryhour", allipstr,
                                    "memhour");// �ڴ�
                            ctable.createTable(conn, "memoryday", allipstr,
                                    "memday");// �ڴ�

                            ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                            ctable.createTable(conn, "cpuhour", allipstr,
                                    "cpuhour");// CPU
                            ctable.createTable(conn, "cpuday", allipstr,
                                    "cpuday");// CPU

                            ctable.createTable(conn, "diskincre", allipstr,
                                    "diskincre");// ����������yangjun
                            ctable.createTable(conn, "diskincrehour", allipstr,
                                    "diskincrehour");// ����������Сʱ
                            ctable.createTable(conn, "diskincreday", allipstr,
                                    "diskincreday");// ������������

                            ctable.createTable(conn, "disk", allipstr, "disk");// yangjun
                            ctable.createTable(conn, "diskhour", allipstr,
                                    "diskhour");
                            ctable.createTable(conn, "diskday", allipstr,
                                    "diskday");

                            /*
                             * ctable.createTable("disk",allipstr,"disk");
                             * ctable.createTable("diskhour",allipstr,"diskhour");
                             * ctable.createTable("diskday",allipstr,"diskday");
                             */
                            ctable.createTable(conn, "ping", allipstr, "ping");
                            ctable.createTable(conn, "pinghour", allipstr,
                                    "pinghour");
                            ctable.createTable(conn, "pingday", allipstr,
                                    "pingday");

                            ctable.createTable(conn, "utilhdxperc", allipstr,
                                    "hdperc");
                            ctable.createTable(conn, "hdxperchour", allipstr,
                                    "hdperchour");
                            ctable.createTable(conn, "hdxpercday", allipstr,
                                    "hdpercday");

                            ctable
                                    .createTable(conn, "utilhdx", allipstr,
                                            "hdx");
                            ctable.createTable(conn, "utilhdxhour", allipstr,
                                    "hdxhour");
                            ctable.createTable(conn, "utilhdxday", allipstr,
                                    "hdxday");

                            ctable.createTable(conn, "allutilhdx", allipstr,
                                    "allhdx");
                            ctable.createTable(conn, "allutilhdxhour",
                                    allipstr, "allhdxhour");
                            ctable.createTable(conn, "allutilhdxday", allipstr,
                                    "allhdxday");

                            ctable.createTable(conn, "discardsperc", allipstr,
                                    "dcardperc");
                            ctable.createTable(conn, "dcardperchour", allipstr,
                                    "dcardperchour");
                            ctable.createTable(conn, "dcardpercday", allipstr,
                                    "dcardpercday");

                            ctable.createTable(conn, "errorsperc", allipstr,
                                    "errperc");
                            ctable.createTable(conn, "errperchour", allipstr,
                                    "errperchour");
                            ctable.createTable(conn, "errpercday", allipstr,
                                    "errpercday");

                            ctable
                                    .createTable(conn, "packs", allipstr,
                                            "packs");
                            ctable.createTable(conn, "packshour", allipstr,
                                    "packshour");
                            ctable.createTable(conn, "packsday", allipstr,
                                    "packsday");

                            ctable.createTable(conn, "inpacks", allipstr,
                                    "inpacks");
                            ctable.createTable(conn, "inpackshour", allipstr,
                                    "inpackshour");
                            ctable.createTable(conn, "inpacksday", allipstr,
                                    "inpacksday");

                            ctable.createTable(conn, "outpacks", allipstr,
                                    "outpacks");
                            ctable.createTable(conn, "outpackshour", allipstr,
                                    "outpackshour");
                            ctable.createTable(conn, "outpacksday", allipstr,
                                    "outpacksday");

                        }
                    } else {
                        // ���·���
                        // ��ɾ���������豸��
                        ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
                        ctable
                                .deleteTable(conn, "prohour", allipstr,
                                        "prohour");// ����Сʱ
                        ctable.deleteTable(conn, "proday", allipstr, "proday");// ������

                        ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
                        ctable.deleteTable(conn, "pinghour", allipstr,
                                "pinghour");// Ping
                        ctable
                                .deleteTable(conn, "pingday", allipstr,
                                        "pingday");// Ping

                        ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.deleteTable(conn, "memoryhour", allipstr,
                                "memhour");// �ڴ�
                        ctable.deleteTable(conn, "memoryday", allipstr,
                                "memday");// �ڴ�

                        ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable
                                .deleteTable(conn, "cpuhour", allipstr,
                                        "cpuhour");// CPU
                        ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU

                        ctable.deleteTable(conn, "log", allipstr, "log");// CPU

                        ctable.deleteTable(conn, "disk", allipstr, "disk");// yangjun
                        ctable.deleteTable(conn, "diskhour", allipstr,
                                "diskhour");
                        ctable
                                .deleteTable(conn, "diskday", allipstr,
                                        "diskday");

                        ctable.deleteTable(conn, "diskincre", allipstr,
                                "diskincre");// ����������yangjun
                        ctable.deleteTable(conn, "diskincrehour", allipstr,
                                "diskincrehour");// ����������Сʱ
                        ctable.deleteTable(conn, "diskincreday", allipstr,
                                "diskincreday");// ������������

                        ctable.deleteTable(conn, "utilhdxperc", allipstr,
                                "hdperc");
                        ctable.deleteTable(conn, "hdxperchour", allipstr,
                                "hdperchour");
                        ctable.deleteTable(conn, "hdxpercday", allipstr,
                                "hdpercday");

                        ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.deleteTable(conn, "utilhdxhour", allipstr,
                                "hdxhour");
                        ctable.deleteTable(conn, "utilhdxday", allipstr,
                                "hdxday");

                        ctable.deleteTable(conn, "allutilhdx", allipstr,
                                "allhdx");
                        ctable.deleteTable(conn, "allutilhdxhour", allipstr,
                                "allhdxhour");
                        ctable.deleteTable(conn, "allutilhdxday", allipstr,
                                "allhdxday");

                        ctable.deleteTable(conn, "temper", allipstr, "temper");
                        ctable.deleteTable(conn, "temperhour", allipstr,
                                "temperhour");
                        ctable.deleteTable(conn, "temperday", allipstr,
                                "temperday");

                        // ���������豸��
                        ctable.createTable(conn, "pro", allipstr, "pro");// ����
                        ctable
                                .createTable(conn, "prohour", allipstr,
                                        "prohour");// ����Сʱ
                        ctable.createTable(conn, "proday", allipstr, "proday");// ������

                        ctable.createSyslogTable(conn, "log", allipstr, "log");// ������

                        ctable.createTable(conn, "memory", allipstr, "mem");// �ڴ�
                        ctable.createTable(conn, "memoryhour", allipstr,
                                "memhour");// �ڴ�
                        ctable.createTable(conn, "memoryday", allipstr,
                                "memday");// �ڴ�

                        ctable.createTable(conn, "cpu", allipstr, "cpu");// CPU
                        ctable
                                .createTable(conn, "cpuhour", allipstr,
                                        "cpuhour");// CPU
                        ctable.createTable(conn, "cpuday", allipstr, "cpuday");// CPU

                        ctable.createTable(conn, "disk", allipstr, "disk");// yangjun
                        ctable.createTable(conn, "diskhour", allipstr,
                                "diskhour");
                        ctable
                                .createTable(conn, "diskday", allipstr,
                                        "diskday");

                        ctable.createTable(conn, "diskincre", allipstr,
                                "diskincre");// ����������yangjun
                        ctable.createTable(conn, "diskincrehour", allipstr,
                                "diskincrehour");// ����������Сʱ
                        ctable.createTable(conn, "diskincreday", allipstr,
                                "diskincreday");// ������������
                        /*
                         * ctable.createTable("disk",allipstr,"disk");
                         * ctable.createTable("diskhour",allipstr,"diskhour");
                         * ctable.createTable("diskday",allipstr,"diskday");
                         */
                        ctable.createTable(conn, "ping", allipstr, "ping");
                        ctable.createTable(conn, "pinghour", allipstr,
                                "pinghour");
                        ctable
                                .createTable(conn, "pingday", allipstr,
                                        "pingday");

                        ctable.createTable(conn, "utilhdxperc", allipstr,
                                "hdperc");
                        ctable.createTable(conn, "hdxperchour", allipstr,
                                "hdperchour");
                        ctable.createTable(conn, "hdxpercday", allipstr,
                                "hdpercday");

                        ctable.createTable(conn, "utilhdx", allipstr, "hdx");
                        ctable.createTable(conn, "utilhdxhour", allipstr,
                                "hdxhour");
                        ctable.createTable(conn, "utilhdxday", allipstr,
                                "hdxday");

                        ctable.createTable(conn, "allutilhdx", allipstr,
                                "allhdx");
                        ctable.createTable(conn, "allutilhdxhour", allipstr,
                                "allhdxhour");
                        ctable.createTable(conn, "allutilhdxday", allipstr,
                                "allhdxday");

                        ctable.createTable(conn, "temper", allipstr, "temper");
                        ctable.createTable(conn, "temperhour", allipstr,
                                "temperhour");
                        ctable.createTable(conn, "temperday", allipstr,
                                "temperday");
                    }
                }
                try {
                    conn.executeBatch();
                } catch (Exception e) {

                }
                String bridgeAddress = node.getBridgeAddress();
                bridgeAddress = CommonUtil.removeIllegalStr(bridgeAddress);
                StringBuffer sql = new StringBuffer(300);
                sql
                        .append("insert into topo_host_node(id,ip_address,ip_long,net_mask,category,community,sys_oid,sys_name,super_node,");
                sql
                        .append("local_net,layer,sys_descr,sys_location,sys_contact,alias,type,managed,bridge_address,status,discoverstatus,write_community,snmpversion,ostype,collecttype,bid,sendemail,sendmobiles,sendphone)values(");
                sql.append(node.getId());
                sql.append(",'");
                sql.append(node.getIpAddress());
                sql.append("',");
                sql.append(NetworkUtil.ip2long(node.getIpAddress()));
                sql.append(",'");
                sql.append(node.getNetMask());
                sql.append("',");
                sql.append(node.getCategory());
                sql.append(",'");
                sql.append(node.getCommunity());
                sql.append("','");
                sql.append(node.getSysOid());
                sql.append("','");
                sql.append(replace(node.getSysName()));
                sql.append("',");
                sql.append(node.getSuperNode());
                sql.append(",");
                sql.append(node.getLocalNet());
                sql.append(",");
                sql.append(node.getLayer());
                sql.append(",'");
                sql.append(replace(node.getSysDescr()));
                sql.append("','");
                sql.append(replace(node.getSysLocation()));
                sql.append("','");
                sql.append(replace(node.getSysContact()));
                sql.append("','");
                if (node.getAlias() == null)
                    sql.append(replace(node.getSysName()));
                else
                    sql.append(replace(node.getAlias()));
                sql.append("','',0,'");// Ĭ������²�����
                sql.append(bridgeAddress);
                sql.append("',");
                sql.append(node.getStatus());
                sql.append(",");
                sql.append(node.getDiscoverstatus());
                sql.append(",'");
                sql.append(node.getWritecommunity());
                sql.append("',");
                sql.append(node.getSnmpversion());
                sql.append(",");
                sql.append(node.getOstype());
                sql.append(",1");// Ĭ���������SNMP�ɼ���ʽ
                // sql.append(node.getCollecttype());
                sql.append(",'");
                sql.append(node.getBid());
                sql.append("','");
                sql.append("");
                sql.append("','");
                sql.append("");
                sql.append("','");
                sql.append("");
                sql.append("')");
                SysLogger.info(sql.toString());
                conn.executeUpdate(sql.toString(), false);
                donehost.put(node.getId(), node);

                // ���òɼ�ָ��
                // Host node =
                // (Host)PollingEngine.getInstance().getNodeByIP(ipAddress);
                // �ɼ��豸��Ϣ
                try {
                    SysLogger.info("endpoint: " + node.getEndpoint()
                            + "====collecttype: " + node.getCollecttype()
                            + "====category: " + node.getCategory());
                    try {
                        // pingData(node);
                        // PingSnmp pingsnmp = new PingSnmp();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (node.getEndpoint() == 2) {
                        // REMOTEPING���ӽڵ㣬����
                        // return;
                    } else {
                        if (node.getCategory() == 4) {
                            // ��ʼ���������ɼ�ָ��ͷ�ֵ
                            SysLogger.info(node.getSysOid());
                            if (node.getSysOid().startsWith("1.3.6.1.4.1.311.")) {
                                // windows������
                                // ��ֵ
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "windows");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                // �ɼ�ָ��
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "windows", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.2021")
                                    || node.getSysOid().startsWith(
                                            "1.3.6.1.4.1.8072")) {
                                // LINUX������
                                SysLogger.info(node.getSysOid()
                                        + "### ��ʼ��ʼ���ɼ�ָ�� ###");
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "linux");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "linux", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith("as400")) {
                                // AS400������
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "as400");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_HOST,
                                                    "as400", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                        } else if (node.getCategory() < 4
                                || node.getCategory() == 7
                                || node.getCategory() == 8) {
                            // ��ʼ�������豸�ɼ�ָ��
                            if (node.getSysOid().startsWith("1.3.6.1.4.1.9.")) {
                                // cisco�����豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "cisco");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "cisco", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.25506.")) {
                                // h3c�����豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "h3c");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "h3c", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.2011.")) {
                                // h3c�����豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "h3c");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "h3c", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.4881.")) {
                                // ��������豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "redgiant");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "redgiant", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.5651.")) {
                                // ���������豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "maipu");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "maipu", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.171.")) {
                                // DLink�����豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "dlink");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "dlink", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.2272.")) {
                                // ���������豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "northtel");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "northtel", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.89.")) {
                                // RADWARE�����豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "radware");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "radware", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else if (node.getSysOid().startsWith(
                                    "1.3.6.1.4.1.3320.")) {
                                // ���������豸
                                try {
                                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                                    alarmIndicatorsUtil
                                            .saveAlarmInicatorsThresholdForNode(
                                                    node.getId() + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "bdcom");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                                    nodeGatherIndicatorsUtil
                                            .addGatherIndicatorsForNode(node
                                                    .getId()
                                                    + "",
                                                    AlarmConstant.TYPE_NET,
                                                    "bdcom", "1");
                                } catch (RuntimeException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }

                        // ��ֻ��PING TELNET SSH��ʽ��������,���������ݲ��ɼ�,����
                        if (node.getCollecttype() == SystemConstant.COLLECTTYPE_PING
                                || node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT
                                || node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT) {
                            SysLogger
                                    .info("ֻPING TELNET SSH��ʽ��������,�������ݲ��ɼ�,����");
                        } else {
                            // threadPool.runTask(createTask(node));
                            if (node.getCategory() < 4
                                    || node.getCategory() == 7) {
                                // collectNetData(node);
                                PollDataUtil polldata = new PollDataUtil();
                                polldata.collectNetData(node.getId() + "");
                            } else if (node.getCategory() == 4) {
                                collectHostData(node);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // if(node.getCategory() == 4){
                // //��ʼ���������ɼ�ָ��
                // if(node.getSysOid().startsWith("1.3.6.1.4.1.311.")){
                // //windows������
                // try {
                // AlarmIndicatorsUtil alarmIndicatorsUtil = new
                // AlarmIndicatorsUtil();
                // alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId()+"",
                // AlarmConstant.TYPE_HOST, "windows");
                // } catch (RuntimeException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }
                //				
                // }else if(node.getCategory() < 4 || node.getCategory() == 7 ||
                // node.getCategory() == 8){
                // //��ʼ�������豸�ɼ�ָ��
                // if(node.getSysOid().startsWith("1.3.6.1.4.1.9.")){
                // //cisco�����豸
                // try {
                // AlarmIndicatorsUtil alarmIndicatorsUtil = new
                // AlarmIndicatorsUtil();
                // alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(node.getId()+"",
                // AlarmConstant.TYPE_NET, "cisco");
                // } catch (RuntimeException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }
                // }

                // ������ͨ�Լ�����ͱ�,����PING����򲻼���
                if (node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT
                        || node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT) {
                    StringBuffer configsql = new StringBuffer(200);
                    configsql
                            .append("insert into nms_connecttypeconfig(node_id,connecttype,username,password,login_prompt,password_prompt,shell_prompt)"
                                    + "values('");
                    configsql.append(node.getId());
                    configsql.append("','");
                    if (node.getCollecttype() == SystemConstant.COLLECTTYPE_TELNETCONNECT) {
                        configsql.append("telnet");
                    } else if (node.getCollecttype() == SystemConstant.COLLECTTYPE_SSHCONNECT) {
                        configsql.append("ssh");
                    } else
                        configsql.append("ping");
                    configsql.append("','");
                    configsql.append("");
                    configsql.append("','");
                    configsql.append("");
                    configsql.append("','");
                    configsql.append("");
                    configsql.append("','");
                    configsql.append("");
                    configsql.append("','");
                    configsql.append("");
                    configsql.append("')");
                    SysLogger.info(configsql.toString());
                    conn.executeUpdate(configsql.toString(), false);
                }

                NetSyslogRuleDao ruledao = new NetSyslogRuleDao();
                NetSyslogNodeRuleDao netlog = new NetSyslogNodeRuleDao();
                try {
                    String strFacility = "";
                    List rulelist = new ArrayList();
                    try {
                        rulelist = ruledao.loadAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ruledao.close();
                    }
                    if (rulelist != null && rulelist.size() > 0) {
                        NetSyslogRule logrule = (NetSyslogRule) rulelist.get(0);
                        strFacility = logrule.getFacility();
                    }

                    String strSql = "";
                    strSql = "insert into nms_netsyslogrule_node(id,nodeid,facility)values(0,'"
                            + node.getId() + "','" + strFacility + "')";
                    try {
                        netlog.saveOrUpdate(strSql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        netlog.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ruledao.close();
                    netlog.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        conn.commit();
        conn
                .executeUpdate("update topo_host_node a,nms_device_type b set a.type=b.descr where a.sys_oid=b.sys_oid");
        // ���������豸IP�ı���
        donehost = new Hashtable();
        for (int i = 0; i < hostList.size(); i++) {
            try {
                Host node = (Host) hostList.get(i);
                if (donehost.containsKey(node.getId()))
                    continue;
                if (node.getAliasIfEntitys() != null
                        && node.getAliasIfEntitys().size() > 0) {
                    for (int k = 0; k < node.getAliasIfEntitys().size(); k++) {
                        IfEntity ifEntity = (IfEntity) node.getAliasIfEntitys()
                                .get(k);
                        // String aliasip = (String)node.getAliasIPs().get(k);
                        StringBuffer sql = new StringBuffer(300);

                        sql
                                .append("insert into topo_ipalias(ipaddress,aliasip,indexs,descr,speeds,types) value('");
                        if (node.getAdminIp() != null
                                && node.getAdminIp().trim().length() > 0) {
                            sql.append(node.getAdminIp());
                            // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                            // SysLogger.info(node.getAdminIp()+"----"+aliasip);
                            if (node.getAdminIp().equalsIgnoreCase(
                                    ifEntity.getIpAddress()))
                                continue;
                        } else {
                            // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                            sql.append(node.getIpAddress());
                            // SysLogger.info(node.getIpAddress()+"----"+aliasip);
                            if (node.getIpAddress().equalsIgnoreCase(
                                    ifEntity.getIpAddress()))
                                continue;
                        }

                        sql.append("','");
                        sql.append(ifEntity.getIpAddress());
                        sql.append("','");
                        sql.append(ifEntity.getIndex());
                        sql.append("','");
                        sql.append(ifEntity.getDescr());
                        sql.append("','");
                        sql.append(ifEntity.getSpeed());
                        sql.append("',");
                        sql.append(ifEntity.getType());

                        sql.append(")");
                        SysLogger.info(sql.toString());
                        conn.executeUpdate(sql.toString(), false);
                        donehost.put(node.getId(), node);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �ֹ�������������,ͬʱ���������豸IP�ı���
     */
    public void addHostDataByHand(List hostList) {
        SysLogger.info("����: ��ʼ�ֹ������豸...");
        Hashtable donehost = new Hashtable();
        for (int i = 0; i < hostList.size(); i++) {
            try {
                Host node = (Host) hostList.get(i);
                if (donehost.containsKey(node.getId())) {
                    continue;
                }
                
                // �������ɱ�
                String ip = node.getIpAddress();
                String allipstr = SysUtil.doip(ip);
                CreateTableManager ctable = new CreateTableManager();
                
//                // ��ɾ���������ӱ�
//                ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
//                ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// Ping
//                ctable.deleteTable(conn, "pingday", allipstr, "pingday");// Ping
//
//                ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
//                ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
//                ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
//
//                ctable.deleteTable(conn, "memory", allipstr, "memory");// �ڴ�
//                ctable.deleteTable(conn, "memoryhour", allipstr, "memoryhour");// �ڴ�
//                ctable.deleteTable(conn, "memoryday", allipstr, "memoryday");// �ڴ�
//                // ���ڲɼ�
//                // ctable.deleteTable(conn,"flash",allipstr,"flash");//����
//                // ctable.deleteTable(conn,"flashhour",allipstr,"flashhour");//����
//                // ctable.deleteTable(conn,"flashday",allipstr,"flashday");//����
//
//                ctable.deleteTable(conn, "buffer", allipstr, "buffer");// ����
//                ctable.deleteTable(conn, "bufferhour", allipstr, "bufferhour");// ����
//                ctable.deleteTable(conn, "bufferday", allipstr, "bufferday");// ����
//                // ���ڲɼ�
//                // ctable.deleteTable(conn,"temper",allipstr,"temper");//�¶�
//                // ctable.deleteTable(conn,"temperhour",allipstr,"temperhour");//�¶�
//                // ctable.deleteTable(conn,"temperday",allipstr,"temperday");//�¶�
//                //
//                // ctable.deleteTable(conn,"fan",allipstr,"fan");//����
//                // ctable.deleteTable(conn,"fanhour",allipstr,"fanhour");//����
//                // ctable.deleteTable(conn,"fanday",allipstr,"fanday");//����
//                //                
//                // ctable.deleteTable(conn,"power",allipstr,"power");//��Դ
//                // ctable.deleteTable(conn,"powerhour",allipstr,"powerhour");//��Դ
//                // ctable.deleteTable(conn,"powerday",allipstr,"powerday");//��Դ
//                //                
//                // ctable.deleteTable(conn,"vol",allipstr,"vol");//��ѹ
//                // ctable.deleteTable(conn,"volhour",allipstr,"volhour");//��ѹ
//                // ctable.deleteTable(conn,"volday",allipstr,"volday");//��ѹ
//
//                ctable.deleteTable(conn, "utilhdx", allipstr, "utilhdx");// �����˿�����
//                ctable.deleteTable(conn, "utilhdxhour", allipstr, "utilhdxhour");// �����˿�����
//                ctable.deleteTable(conn, "utilhdxday", allipstr, "utilhdxday");// �����˿�����
//
//                ctable.deleteTable(conn, "allutilhdx", allipstr, "allutilhdx");// �˿�������
//                ctable.deleteTable(conn, "allutilhdxhour", allipstr,
//                        "allutilhdxhour");// �˿�������
//                ctable
//                        .deleteTable(conn, "allutilhdxday", allipstr,
//                                "allutilhdxday");// �˿�������
//
//                ctable.deleteTable(conn, "utilhdxperc", allipstr, "utilhdxperc");// ����
//                ctable.deleteTable(conn, "utilhdxperchour", allipstr, "utilhdxperchour");// ����
//                ctable.deleteTable(conn, "utilhdxpercday", allipstr, "utilhdxpercday");// ����
//
//                ctable.deleteTable(conn, "discardsperc", allipstr, "discardsperc");// ������
//                ctable.deleteTable(conn, "discardsperchour", allipstr,
//                        "discardsperchour");// ������
//                ctable.deleteTable(conn, "discardspercday", allipstr,
//                        "discardspercday");// ������
//
//                ctable.deleteTable(conn, "errorsperc", allipstr, "errorsperc");// �����
//                ctable
//                        .deleteTable(conn, "errorsperchour", allipstr,
//                                "errorsperchour");// �����
//                ctable.deleteTable(conn, "errorspercday", allipstr, "errorspercday");// �����
//
//                ctable.deleteTable(conn, "packs", allipstr, "packs");// ���ݰ�����
//                ctable.deleteTable(conn, "packshour", allipstr, "packshour");// ���ݰ�����
//                ctable.deleteTable(conn, "packsday", allipstr, "packsday");// ���ݰ�����
//
//                ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");// ���ݰ�����
//                ctable
//                        .deleteTable(conn, "inpackshour", allipstr,
//                                "inpackshour");// ���ݰ�����
//                ctable.deleteTable(conn, "inpacksday", allipstr, "inpacksday");// ���ݰ�����
//
//                ctable.deleteTable(conn, "outpacks", allipstr, "outpacks"); // ���ݰ�����
//                ctable.deleteTable(conn, "outpackshour", allipstr,
//                        "outpackshour");// ���ݰ�����
//                ctable
//                        .deleteTable(conn, "outpacksday", allipstr,
//                                "outpacksday");// ���ݰ�����
//
//                ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
//                ctable.deleteTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
//                ctable.deleteTable(conn, "proday", allipstr, "proday");// ������
//
//                ctable.deleteTable(conn, "cpudtl", allipstr, "cpudtl");
//                ctable.deleteTable(conn, "cpudtlhour", allipstr, "cpudtlhour");
//                ctable.deleteTable(conn, "cpudtlday", allipstr, "cpudtlday");
//
//                ctable.deleteTable(conn, "disk", allipstr, "disk");// ����������
//                ctable.deleteTable(conn, "diskhour", allipstr, "diskhour");// ����������
//                ctable.deleteTable(conn, "diskday", allipstr, "diskday");// ����������
//
//                ctable.deleteTable(conn, "diskincre", allipstr, "diskincre");// ����������yangjun
//                ctable.deleteTable(conn, "diskincrehour", allipstr,
//                        "diskincrehour");// ����������Сʱ
//                ctable.deleteTable(conn, "diskincreday", allipstr,
//                        "diskincreday");// ������������
//
//                ctable.deleteTable(conn, "pgused", allipstr, "pgused");// ���ɻ�ҳ��
//                ctable.deleteTable(conn, "pgusedhour", allipstr, "pgusedhour");// ���ɻ�ҳ��
//                ctable.deleteTable(conn, "pgusedday", allipstr, "pgusedday");// ���ɻ�ҳ��

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
                
                
//                // ɾ������ɺ� ���ӱ�
//                ctable.createTable(conn,"ping",allipstr,"ping");//Ping
//                ctable.createTable(conn,"pinghour",allipstr,"pinghour");//Ping
//                ctable.createTable(conn,"pingday",allipstr,"pingday");//Ping
//                
//                ctable.createTable(conn,"cpu",allipstr,"cpu");//CPU
//                ctable.createTable(conn,"cpuhour",allipstr,"cpuhour");//CPU
//                ctable.createTable(conn,"cpuday",allipstr,"cpuday");//CPU
//
//                ctable.createTable(conn,"memory",allipstr,"mem");//�ڴ�
//                ctable.createTable(conn,"memoryhour",allipstr,"memhour");//�ڴ�
//                ctable.createTable(conn,"memoryday",allipstr,"memday");//�ڴ�
//                // ���ڲɼ�
////                ctable.createTable(conn,"flash",allipstr,"flash");//����
////                ctable.createTable(conn,"flashhour",allipstr,"flashhour");//����
////                ctable.createTable(conn,"flashday",allipstr,"flashday");//����
//                
//                ctable.createTable(conn,"buffer",allipstr,"buffer");//����
//                ctable.createTable(conn,"bufferhour",allipstr,"bufferhour");//����
//                ctable.createTable(conn,"bufferday",allipstr,"bufferday");//����
//             // ���ڲɼ�
////                ctable.createTable(conn,"temper",allipstr,"temper");//�¶�
////                ctable.createTable(conn,"temperhour",allipstr,"temperhour");//�¶�
////                ctable.createTable(conn,"temperday",allipstr,"temperday");//�¶�
////
////                ctable.createTable(conn,"fan",allipstr,"fan");//����
////                ctable.createTable(conn,"fanhour",allipstr,"fanhour");//����
////                ctable.createTable(conn,"fanday",allipstr,"fanday");//����
////                
////                ctable.createTable(conn,"power",allipstr,"power");//��Դ
////                ctable.createTable(conn,"powerhour",allipstr,"powerhour");//��Դ
////                ctable.createTable(conn,"powerday",allipstr,"powerday");//��Դ
////                
////                ctable.createTable(conn,"vol",allipstr,"vol");//��ѹ
////                ctable.createTable(conn,"volhour",allipstr,"volhour");//��ѹ
////                ctable.createTable(conn,"volday",allipstr,"volday");//��ѹ
//                
//                ctable.createTable(conn,"utilhdx",allipstr,"hdx");//�����˿�����
//                ctable.createTable(conn,"utilhdxhour",allipstr,"hdxhour");//�����˿�����
//                ctable.createTable(conn,"utilhdxday",allipstr,"hdxday");//�����˿�����
//                
//                ctable.createTable(conn,"allutilhdx",allipstr,"allhdx");//�˿�������
//                ctable.createTable(conn,"allutilhdxhour",allipstr,"allhdxhour");//�˿�������
//                ctable.createTable(conn,"allutilhdxday",allipstr,"allhdxday");//�˿�������
//                
//                ctable.createTable(conn,"utilhdxperc",allipstr,"hdperc");//����
//                ctable.createTable(conn,"hdxperchour",allipstr,"hdperchour");//����
//                ctable.createTable(conn,"hdxpercday",allipstr,"hdpercday");//����
//                
//                ctable.createTable(conn,"discardsperc",allipstr,"dcardperc");//������
//                ctable.createTable(conn,"dcardperchour",allipstr,"dcardperchour");//������
//                ctable.createTable(conn,"dcardpercday",allipstr,"dcardpercday");//������
//                
//                ctable.createTable(conn,"errorsperc",allipstr,"errperc");//�����
//                ctable.createTable(conn,"errperchour",allipstr,"errperchour");//�����
//                ctable.createTable(conn,"errpercday",allipstr,"errpercday");//�����
//                
//                ctable.createTable(conn,"packs",allipstr,"packs");//���ݰ�����
//                ctable.createTable(conn,"packshour",allipstr,"packshour");//���ݰ�����
//                ctable.createTable(conn,"packsday",allipstr,"packsday");//���ݰ�����
//                
//                ctable.createTable(conn,"inpacks",allipstr,"inpacks");//���ݰ�����
//                ctable.createTable(conn,"inpackshour",allipstr,"inpackshour");//���ݰ�����
//                ctable.createTable(conn,"inpacksday",allipstr,"inpacksday");//���ݰ�����
//                
//                ctable.createTable(conn,"outpacks",allipstr,"outpacks"); //���ݰ�����   
//                ctable.createTable(conn,"outpackshour",allipstr,"outpackshour");//���ݰ�����
//                ctable.createTable(conn,"outpacksday",allipstr,"outpacksday");//���ݰ�����
//
//                ctable.createTable(conn,"pro",allipstr,"pro");//����
//                ctable.createTable(conn,"prohour",allipstr,"prohour");//����Сʱ
//                ctable.createTable(conn,"proday",allipstr,"proday");//������
//
//                ctable.createTable(conn,"cpudtl",allipstr,"cpudtl");    
//                ctable.createTable(conn,"cpudtlhour",allipstr,"cpudtlhour");
//                ctable.createTable(conn,"cpudtlday",allipstr,"cpudtlday");
//
//                ctable.createTable(conn,"disk",allipstr,"disk");//����������
//                ctable.createTable(conn,"diskhour",allipstr,"diskhour");//����������
//                ctable.createTable(conn,"diskday",allipstr,"diskday");//����������
//                
//                ctable.createTable(conn,"diskincre",allipstr,"diskincre");//����������yangjun
//                ctable.createTable(conn,"diskincrehour",allipstr,"diskincrehour");//����������Сʱ
//                ctable.createTable(conn,"diskincreday",allipstr,"diskincreday");//������������ 
//                
//                ctable.createTable(conn,"pgused",allipstr,"pgused");//���ɻ�ҳ��  
//                ctable.createTable(conn,"pgusedhour",allipstr,"pgusedhour");//���ɻ�ҳ��
//                ctable.createTable(conn,"pgusedday",allipstr,"pgusedday");//���ɻ�ҳ��
                
                for (String tableName : TABLE_NAME_LIST) {
                    // ��ɾ�� �����
                    ctable.deleteTable(conn, tableName, allipstr, tableName);
                    ctable.deleteTable(conn, tableName + "hour", allipstr, tableName + "hour");
                    ctable.deleteTable(conn, tableName + "day", allipstr, tableName + "day");
                    
                    ctable.createTable(conn, tableName, allipstr, tableName);
                    ctable.createTable(conn, tableName + "hour", allipstr, tableName + "hour");
                    ctable.createTable(conn, tableName + "day", allipstr, tableName + "day");
                }

                ctable.createTable(conn,"portstatus",allipstr,"port");//�˿�״̬
                //  ctable.createTable(conn,"portstatushour",allipstr,"porthour");
                //  ctable.createTable(conn,"portstatusday",allipstr,"portday");
                
                ctable.createTable(conn,"nms_interface_data_temp",allipstr,"interface");//�˿���ʱ
                
                ctable.createTable(conn,"nms_process_data_temp",allipstr,"process");//������ʱ
                
                ctable.createTable(conn, "systemasp", allipstr, "systemasp");
                ctable.createTable(conn, "dbcapability", allipstr,
                        "dbcapability");

                String bridgeAddress = node.getBridgeAddress();
                bridgeAddress = CommonUtil.removeIllegalStr(bridgeAddress);
                StringBuffer sql = new StringBuffer(300);
                sql.append("insert into topo_host_node(id,asset_id,location,ip_address,ip_long,net_mask,category,community,sys_oid,sys_name,super_node,");
                sql.append("local_net,layer,sys_descr,sys_location,sys_contact,alias,type,managed,bridge_address,status,discoverstatus,write_community,snmpversion,ostype,transfer,collecttype,bid,sendemail,sendmobiles,sendphone,supperid)values(");
                sql.append(node.getId());
                sql.append(",'");
                sql.append(node.getAssetid());
                sql.append("','");
                sql.append(node.getLocation());
                sql.append("','");
                sql.append(node.getIpAddress());
                sql.append("',");
                sql.append(NetworkUtil.ip2long(node.getIpAddress()));
                sql.append(",'");
                sql.append(node.getNetMask());
                sql.append("',");
                sql.append(node.getCategory());
                sql.append(",'");
                sql.append(node.getCommunity());
                sql.append("','");
                sql.append(node.getSysOid());
                sql.append("','");
                sql.append(replace(node.getSysName()));
                sql.append("',");
                sql.append(node.getSuperNode());
                sql.append(",");
                sql.append(node.getLocalNet());
                sql.append(",");
                sql.append(node.getLayer());
                sql.append(",'");
                sql.append(replace(node.getSysDescr()));
                sql.append("','");
                sql.append(replace(node.getSysLocation()));
                sql.append("','");
                sql.append(replace(node.getSysContact()));
                sql.append("','");
                if (node.getAlias() == null)
                    sql.append(replace(node.getSysName()));
                else
                    sql.append(replace(node.getAlias()));
                sql.append("','");
                sql.append(node.getType());
                sql.append("',1,'");
                sql.append(bridgeAddress);
                sql.append("',");
                sql.append(node.getStatus());
                sql.append(",");
                sql.append(node.getDiscoverstatus());
                sql.append(",'");
                sql.append(node.getWritecommunity());
                sql.append("',");
                sql.append(node.getSnmpversion());
                sql.append(",");
                sql.append(node.getOstype());
                sql.append(",");
                sql.append(node.getTransfer());
                sql.append(",");
                sql.append(node.getCollecttype());
                sql.append(",'");
                sql.append(node.getBid());
                sql.append("','");
                sql.append(node.getSendemail());
                sql.append("','");
                sql.append(node.getSendmobiles());
                sql.append("','");
                sql.append(node.getSendphone());
                sql.append("','");
                sql.append(node.getSupperid());
                sql.append("')");
                conn.addBatch(sql.toString());
                donehost.put(node.getId(), node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            conn.executeBatch();
        } catch (Exception e) {
            conn.rollback();
        }
        conn.commit();
        conn.addBatch("update topo_host_node a,nms_device_type b set a.type=b.descr where a.sys_oid=b.sys_oid");
        // ���������豸IP�ı���
        donehost = new Hashtable();
        for (int i = 0; i < hostList.size(); i++) {
            try {
                Host node = (Host) hostList.get(i);
                if (donehost.containsKey(node.getId()))
                    continue;
                if (node.getAliasIfEntitys() != null
                        && node.getAliasIfEntitys().size() > 0) {
                    for (int k = 0; k < node.getAliasIfEntitys().size(); k++) {
                        IfEntity ifEntity = (IfEntity) node.getAliasIfEntitys()
                                .get(k);
                        StringBuffer sql = new StringBuffer(300);
                        sql.append("insert into topo_ipalias(ipaddress,aliasip,indexs,descr,speeds,types) value('");
                        if (node.getAdminIp() != null
                                && node.getAdminIp().trim().length() > 0) {
                            sql.append(node.getAdminIp());
                            // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                            if (node.getAdminIp().equalsIgnoreCase(
                                    ifEntity.getIpAddress()))
                                continue;
                        } else {
                            // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                            sql.append(node.getIpAddress());
                            if (node.getIpAddress().equalsIgnoreCase(
                                    ifEntity.getIpAddress()))
                                continue;
                        }
                        sql.append("','");
                        sql.append(ifEntity.getIpAddress());
                        sql.append("','");
                        sql.append(ifEntity.getIndex());
                        sql.append("','");
                        sql.append(ifEntity.getDescr());
                        sql.append("','");
                        sql.append(ifEntity.getSpeed());
                        sql.append("',");
                        sql.append(ifEntity.getType());
                        sql.append(")");
                        conn.addBatch(sql.toString());
                        donehost.put(node.getId(), node);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            conn.executeBatch();
        } catch (Exception e) {
            conn.rollback();
        }
    }

    /**
     * ������������
     */
    public void addSubNetData(List netList) {
        if (netList == null)
            return;

        for (int i = 0; i < netList.size(); i++) {
            SubNet subnet = (SubNet) netList.get(i);
            StringBuffer sql = new StringBuffer(200);
            sql
                    .append("insert into topo_subnet(id,net_address,net_mask,net_long,managed)values(");
            sql.append(subnet.getId());
            sql.append(",'");
            sql.append(subnet.getNetAddress());
            sql.append("','");
            sql.append(subnet.getNetMask());
            sql.append("',");
            sql.append(NetworkUtil.ip2long(subnet.getNetAddress()));
            sql.append(",1)");
            conn.executeUpdate(sql.toString(), false);
        }
        conn.commit();
    }

    /**
     * �������������
     */
    public void addMonitor(List hostList) {
        for (int i = 0; i < hostList.size(); i++) {
            Host node = (Host) hostList.get(i);
            try {
                // addPingMonitor(node.getId());
                // System.out.println("sysoid====="+node.getSysOid());
                if (node.getCategory() == 4) {
                    addMonitor(node.getId(), node.getIpAddress(), "host");
                }
                // addServiceMonitor(node.getId());
                if (node.getCategory() < 4) {
                    // �����豸
                    addMonitor(node.getId(), node.getIpAddress(), "net");
                }
                /*
                 * if(node.getCategory()<5) //1,2,3,4 ����ӿڼ�����
                 * addMonitor(node.getId(),node.getIpAddress(),"traffic");
                 * 
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.311.")) //windows
                 * addMonitor(node.getId(),node.getIpAddress(),"windows"); else
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.9.")) //cisco
                 * addMonitor(node.getId(),node.getIpAddress(),"cisco");
                 * 
                 * else if(node.getSysOid().startsWith("1.3.6.1.4.1.25506"))
                 * //huawei {
                 * addMonitor(node.getId(),node.getIpAddress(),"huawei"); } else
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.2.")) //ibm_aix
                 * addMonitor(node.getId(),node.getIpAddress(),"aix"); else
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.42."))
                 * //sun_solaris
                 * addMonitor(node.getId(),node.getIpAddress(),"solaris");
                 * //linux else
                 * if(node.getSysOid().equals("1.3.6.1.4.1.2021.250.10")||
                 * node.getSysOid().equals("1.3.6.1.4.1.8072.3.2.10"))
                 * addMonitor(node.getId(),node.getIpAddress(),"linux"); else
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.11.2.3."))
                 * addMonitor(node.getId(),node.getIpAddress(),"hp-ux"); else
                 * if(node.getSysOid().startsWith("1.3.6.1.4.1.36."))
                 * addMonitor(node.getId(),node.getIpAddress(),"tru64");
                 */

            } catch (Exception e) {
                SysLogger.error("DiscoverCompleteDao.addMonitor(),node_id="
                        + node.getId());
            }
        } // end_for
    }

    private void addPingMonitor(int node_id) {
        nmID++;
        StringBuffer sql = new StringBuffer(200);
        sql
                .append("insert into topo_node_monitor(id,node_id,moid,threshold,compare,compare_type,upper_times,");
        sql
                .append("alarm_info,enabled,alarm_level,poll_interval,interval_unit,threshold_unit)values(");
        sql.append(nmID);
        sql.append(",");
        sql.append(node_id);
        sql.append(",'999001',0,2,1,3,'ping��ͨ',1,3,5,'m','')");
        conn.executeUpdate(sql.toString());
    }

    private void addServiceMonitor(int node_id) {
        nmID++;
        StringBuffer sql = new StringBuffer(200);
        sql
                .append("insert into topo_node_monitor(id,node_id,moid,threshold,compare,compare_type,upper_times,");
        sql
                .append("alarm_info,enabled,alarm_level,poll_interval,interval_unit,threshold_unit)values(");
        sql.append(nmID);
        sql.append(",");
        sql.append(node_id);
        sql.append(",'999002',0,2,1,1,'��Щ���񲻿���',0,3,1,'h','')");
        conn.executeUpdate(sql.toString());
    }

    private void addTelnetConfig(int node_id) {
        if (telnetID == 0)
            telnetID = getNextID("server_telnet_config");
        else
            telnetID++;
        conn
                .executeUpdate("insert into server_telnet_config(id,node_id)values("
                        + telnetID + "," + node_id + ")");
    }

    /**
     * �������ݿ�����
     */
    public void addDBMonitor(int nodeId, String ip, String category) {
        nmID++;
        StringBuffer sql = new StringBuffer(200);
        sql
                .append("insert into topo_node_monitor(id,node_id,moid,threshold,compare,compare_type,upper_times,");
        sql
                .append("alarm_info,enabled,alarm_level,poll_interval,interval_unit,threshold_unit)values(");
        sql.append(nmID);
        sql.append(",");
        sql.append(nodeId);
        sql.append(",'052001',-1,1,1,2,'���ݿⲻ����',1,3,20,'m','')");
        conn.executeUpdate(sql.toString());

        addMonitor(nodeId, ip, category);
    }

    public void addMonitor(int node_id, String ip, String category) {
        try {
            for (int i = 0; i < moList.size(); i++) {
                MonitorObject moid = (MonitorObject) moList.get(i);
                // System.out.println("category---"+category+"
                // mo----"+moid.getCategory());
                // if(!moid.getCategory().equals(category)) continue;
                if (!moid.isDefault())
                    continue; // ֻ����Ĭ����Ҫ�ļ�����
                if (!moid.getNodetype().equalsIgnoreCase(category))
                    continue;
                nmID++;
                StringBuffer sql = new StringBuffer(200);
                sql
                        .append("insert into topo_node_monitor(id,node_id,node_ip,category,moid,unit,threshold,compare,compare_type,upper_times,");
                sql
                        .append("alarm_info,enabled,alarm_level,poll_interval,interval_unit,threshold_unit,descr,nodetype,subentity,limenvalue0,limenvalue1,limenvalue2,");
                sql.append("time0,time1,time2,sms0,sms1,sms2) values(");
                sql.append(nmID);
                sql.append(",");
                sql.append(node_id);
                sql.append(",'");
                sql.append(ip);
                sql.append("','");
                sql.append(moid.getCategory());
                sql.append("','");
                sql.append(moid.getMoid());
                sql.append("','");
                sql.append(moid.getUnit());
                sql.append("',");
                sql.append(moid.getThreshold());
                sql.append(",");
                sql.append(moid.getCompare());
                sql.append(",");
                sql.append(moid.getCompareType());
                sql.append(",");
                sql.append(moid.getUpperTimes());
                sql.append(",'");
                sql.append(moid.getAlarmInfo());
                sql.append("',");
                sql.append(moid.isEnabled() ? 1 : 0);
                sql.append(",");
                sql.append(moid.getAlarmLevel());
                sql.append(",");
                sql.append(moid.getPollInterval());
                sql.append(",'");
                sql.append(moid.getIntervalUnit());
                sql.append("','");
                sql.append(moid.getUnit());
                sql.append("','");
                sql.append(moid.getDescr());
                sql.append("','");
                sql.append(moid.getNodetype());
                sql.append("','");
                sql.append(moid.getSubentity());
                sql.append("',");
                sql.append(moid.getLimenvalue0());
                sql.append(",");
                sql.append(moid.getLimenvalue1());
                sql.append(",");
                sql.append(moid.getLimenvalue2());
                sql.append(",");
                sql.append(moid.getTime0());
                sql.append(",");
                sql.append(moid.getTime1());
                sql.append(",");
                sql.append(moid.getTime2());
                sql.append(",");
                sql.append(moid.getSms0());
                sql.append(",");
                sql.append(moid.getSms1());
                sql.append(",");
                sql.append(moid.getSms2());
                sql.append(")");
                conn.executeUpdate(sql.toString(), false);
            }
            conn.commit();
            /*
             * if(category.equals("aix")||category.equals("solaris")
             * ||category.equals("hp-ux")||category.equals("linux")
             * ||category.equals("tru64")) addTelnetConfig(node_id);
             */
        } catch (Exception e) {
            SysLogger.error("NodeMonitorDao.addMonitor()", e);
        }
    }

    /*
     * ɾ��������
     */
    public void deleteMonitor(int node_id, String ip) {
        try {
            String sql = "delete from topo_node_monitor where node_id="
                    + node_id;
            conn.executeUpdate(sql.toString(), false);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            SysLogger.error("NodeMonitorDao.deleteMonitor()", e);
        }
    }

    /*
     * ɾ��������
     */
    public void deleteIpAlias(int node_id, String ip) {
        try {
            String sql = "delete from topo_node_monitor where node_id="
                    + node_id;
            conn.executeUpdate(sql.toString(), false);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            SysLogger.error("NodeMonitorDao.deleteMonitor()", e);
        }
    }

    /**
     * ��'����_,��֤sql������
     */
    private String replace(String oldStr) {
        if (oldStr == null)
            return "";

        if (oldStr.length() > 45)
            oldStr = oldStr.substring(0, 45);
        if (oldStr.indexOf("'") >= 0)
            return oldStr.replace('\'', '_');
        else
            return oldStr;
    }

    public BaseVo loadFromRS(ResultSet rs) {
        return null;
    }

    public boolean createTableForAS400(Host host) {
        boolean result = false;

        try {
            CreateTableManager ctable = new CreateTableManager();

            String ip = host.getIpAddress();
            // SysLogger.info("IP: ====="+ip);
            // String ip1 ="",ip2="",ip3="",ip4="";
            // String tempStr = "";
            String allipstr = "";
            // if (ip.indexOf(".")>0){
            // ip1=ip.substring(0,ip.indexOf("."));
            // ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());
            // tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
            // }
            // ip2=tempStr.substring(0,tempStr.indexOf("."));
            // ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
            // allipstr=ip1+ip2+ip3+ip4;
            allipstr = SysUtil.doip(ip);

            ctable.createRootTable(conn, "systemasp", allipstr);

            ctable.createRootTable(conn, "dbcapability", allipstr);

            result = true;
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            result = false;
        }

        return result;
    }

    private void collectHostData(Host node) {
        try {
            Vector vector = null;
            Hashtable hashv = null;
            LoadAixFile aix = null;
            LoadLinuxFile linux = null;
            LoadHpUnixFile hpunix = null;
            LoadSunOSFile sununix = null;
            LoadWindowsWMIFile windowswmi = null;
            I_HostCollectData hostdataManager = new HostCollectDataManager();

            I_HostLastCollectData hostlastdataManager = new HostLastCollectDataManager();
            if (node.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL) {
                // SHELL��ȡ��ʽ
                try {
                    if (node.getOstype() == 6) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress()
                                + "����ΪAIX����������������");
                        // AIX������
                        try {
                            // aix = new LoadAixFile(node.getIpAddress());
                            // hashv=aix.getTelnetMonitorDetail();
                            // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (node.getOstype() == 9) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress()
                                + "����ΪLINUX����������������");
                        // LINUX������
                        try {
                            // linux = new LoadLinuxFile(node.getIpAddress());
                            // hashv=linux.getTelnetMonitorDetail();
                            // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (node.getOstype() == 7) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress()
                                + "����ΪHPUNIX����������������");
                        // HPUNIX������
                        try {
                            // hpunix = new LoadHpUnixFile(node.getIpAddress());
                            // hashv=hpunix.getTelnetMonitorDetail();
                            // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (node.getOstype() == 8) {
                        SysLogger.info("�ɼ�: ��ʼ�ɼ�IP��ַΪ" + node.getIpAddress()
                                + "����ΪSOLARIS����������������");
                        // WINDOWS������
                        // try{
                        // sununix = new LoadSunOSFile(node.getIpAddress());
                        // hashv=sununix.getTelnetMonitorDetail();
                        // hostdataManager.createHostData(node.getIpAddress(),hashv);
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }
                    } else if (node.getOstype() == 5) {
                        SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ"
                                + node.getIpAddress() + "����ΪWINDOWS����������������");
                        try {
                            windowswmi = new LoadWindowsWMIFile(node
                                    .getIpAddress());
                            hashv = windowswmi.getTelnetMonitorDetail();
                            hostdataManager.createHostData(node.getIpAddress(),
                                    hashv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                aix = null;
                hashv = null;
            }

            else if (node.getCollecttype() == SystemConstant.COLLECTTYPE_WMI) {
                // WINDOWS�µ�WMI�ɼ���ʽ
                SysLogger.info("�ɼ�: ��ʼ��WMI��ʽ�ɼ�IP��ַΪ" + node.getIpAddress()
                        + "����ΪWINDOWS����������������");
                try {
                    windowswmi = new LoadWindowsWMIFile(node.getIpAddress());
                    hashv = windowswmi.getTelnetMonitorDetail();
                    hostdataManager.createHostData(node.getIpAddress(), hashv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                aix = null;
                hashv = null;
            }

            else {
                // SNMP�ɼ���ʽ
                HostNode hostnode = new HostNode();
                // Host host = new Host();
                hostnode.setId(node.getId());
                hostnode.setSysName(node.getSysName());
                hostnode.setCategory(node.getCategory());
                hostnode.setCommunity(node.getCommunity());
                // hostnode.setWritecommunity(node.getWritecommunity());
                hostnode.setSnmpversion(node.getSnmpversion());
                hostnode.setIpAddress(node.getIpAddress());
                hostnode.setLocalNet(node.getLocalNet());
                hostnode.setNetMask(node.getNetMask());
                hostnode.setAlias(node.getAlias());
                hostnode.setSysDescr(node.getSysDescr());
                hostnode.setSysOid(node.getSysOid());
                hostnode.setType(node.getType());
                hostnode.setManaged(node.isManaged());
                hostnode.setOstype(node.getOstype());
                hostnode.setCollecttype(node.getCollecttype());
                hostnode.setSysLocation(node.getSysLocation());
                hostnode.setSendemail(node.getSendemail());
                hostnode.setSendmobiles(node.getSendmobiles());
                hostnode.setSendphone(node.getSendphone());
                hostnode.setBid(node.getBid());
                hostnode.setEndpoint(node.getEndpoint());
                hostnode.setStatus(0);
                hostnode.setSupperid(node.getSupperid());

                try {
                    NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
                    List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
                    try {
                        // ��ȡ�����õ����б�����ָ��
                        monitorItemList = indicatorsdao
                                .findByNodeIdAndTypeAndSubtype(hostnode.getId()
                                        + "", "host", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        indicatorsdao.close();
                    }
                    if (monitorItemList != null && monitorItemList.size() > 0) {
                        for (int i = 0; i < monitorItemList.size(); i++) {
                            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList
                                    .get(i);
                            PollDataUtil polldatautil = new PollDataUtil();
                            polldatautil.collectHostData(nodeGatherIndicators);
                        }
                    }
                } catch (Exception e) {

                }

                // if(node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.1")
                // ||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.2")||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1.3")||
                // node.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.311.1.1.3.1")){
                // SysLogger.info("�ɼ�:
                // ��ʼ�ɼ�IP��ַΪ"+node.getIpAddress()+"����ΪWINDOWS����������������");
                // //windows
                // WindowsSnmp windows=new WindowsSnmp();
                // try{
                // hashv=windows.collect_Data(hostnode);
                // hostdataManager.createHostData(node.getIpAddress(),hashv);
                // }catch(Exception ex){
                // ex.printStackTrace();
                // }
                // windows=null;
                // vector=null;
                //           		
                // }else if(node.getOstype() == 9){
                // if(node.getCollecttype() == 1){
                // //System.out.println("==================linux================");
                // LinuxSnmp linuxSnmp = new LinuxSnmp();
                // hashv = linuxSnmp.collect_Data(hostnode);
                // //System.out.println("==================linux SNMP
                // end================");
                // hostdataManager.createHostData(node.getIpAddress(),hashv);
                // }else{
                //						
                // }
                // }
            }
        } catch (Exception exc) {

        }
    }

    
    /**
     * ���������ӿ�����
     */
    public void addInterfaceData(HostNode hostNode) {
        int id = getNextID("topo_interface");
        List ifList = hostNode.getIfEntityList();
        if (ifList == null)
            return;

        for (int j = 0; j < ifList.size(); j++) {
            com.afunms.discovery.IfEntity ifEntity = (com.afunms.discovery.IfEntity) ifList
                    .get(j);
            String physAddress = ifEntity.getPhysAddress();
            physAddress = CommonUtil.removeIllegalStr(physAddress).replace("\"", "");
            StringBuffer sql = new StringBuffer(300);
            sql.append("insert into topo_interface(id,node_id,entity,descr,port,speed,phys_address,ip_address,oper_status,type,chassis,slot,uport)values(");
            sql.append(id++);
            sql.append(",");
            sql.append(hostNode.getId());
            sql.append(",\"");
            sql.append(ifEntity.getIndex());
            sql.append("\",\"");
            sql.append(replace(ifEntity.getDescr()));
            sql.append("\",\"");
            sql
                    .append(ifEntity.getPort() == null ? "" : ifEntity
                            .getPort());
            sql.append("\",\"");
            sql.append(ifEntity.getSpeed());
            sql.append("\",\"");
            sql.append(physAddress);
            sql.append("\",\"");
            sql.append(ifEntity.getIpList()); // ����IP��ַ
            sql.append("\",");
            sql.append(ifEntity.getOperStatus());
            sql.append(",");
            sql.append(ifEntity.getType()); // �˿�����
            sql.append(",");
            sql.append(ifEntity.getChassis()); // ���
            sql.append(",");
            sql.append(ifEntity.getSlot()); // ��
            sql.append(",");
            sql.append(ifEntity.getUport()); // ��
            sql.append(")");
            conn.executeUpdate(sql.toString(), false);
        }// end_for_j
        conn.commit();
        conn.executeUpdate("update topo_interface set alias=descr");
    }
    
    
    /**
     * �ֹ�������������,ͬʱ���������豸IP�ı���
     */
    public void addHostDataByHand(HostNode hostNode) {
        String ip = hostNode.getIpAddress();
        String allipstr = SysUtil.doip(ip);
        CreateTableManager ctable = new CreateTableManager();
        ctable.deleteTable(conn, "portstatus", allipstr, "port");// �˿�״̬

        ctable.deleteTable(conn, "nms_interface_data_temp", allipstr,
                "interface");// �˿�״̬

        ctable.deleteTable(conn, "nms_process_data_temp", allipstr,
                "process");// �˿�״̬

        ctable.deleteTable(conn, "systemasp", allipstr, "systemasp");
        ctable.deleteTable(conn, "dbcapability", allipstr,
                "dbcapability");
        
        for (String tableName : TABLE_NAME_LIST) {
            // ��ɾ�� �����
            ctable.deleteTable(conn, tableName, allipstr, tableName);
            ctable.deleteTable(conn, tableName + "hour", allipstr, tableName + "hour");
            ctable.deleteTable(conn, tableName + "day", allipstr, tableName + "day");
            
            ctable.createTable(conn, tableName, allipstr, tableName);
            ctable.createTable(conn, tableName + "hour", allipstr, tableName + "hour");
            ctable.createTable(conn, tableName + "day", allipstr, tableName + "day");
        }

        ctable.createTable(conn,"portstatus",allipstr,"port");//�˿�״̬
        
        ctable.createTable(conn,"nms_interface_data_temp",allipstr,"interface");//�˿���ʱ
        
        ctable.createTable(conn,"nms_process_data_temp",allipstr,"process");//������ʱ

        ctable.createTable(conn, "systemasp", allipstr, "systemasp");
        ctable.createTable(conn, "dbcapability", allipstr,
                "dbcapability");

        conn.addBatch("update topo_host_node a,nms_device_type b set a.type=b.descr where a.sys_oid=b.sys_oid");
        // ���������豸IP�ı���
        if (hostNode.getAliasIfEntitys() != null && hostNode.getAliasIfEntitys().size() > 0 ) {
            for (int k = 0; k < hostNode.getAliasIfEntitys().size(); k++) {
                IfEntity ifEntity = (IfEntity) hostNode.getAliasIfEntitys()
                        .get(k);
                StringBuffer sql = new StringBuffer(300);
                sql.append("insert into topo_ipalias(ipaddress,aliasip,indexs,descr,speeds,types) value('");
                if (hostNode.getIpAddress() != null
                        && hostNode.getIpAddress().trim().length() > 0) {
                    sql.append(hostNode.getIpAddress());
                    // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                    if (hostNode.getIpAddress().equalsIgnoreCase(
                            ifEntity.getIpAddress()))
                        continue;
                } else {
                    // ���˵������ַ�ͱ���IP��ͬ�ļ�¼
                    sql.append(hostNode.getIpAddress());
                    if (hostNode.getIpAddress().equalsIgnoreCase(
                            ifEntity.getIpAddress()))
                        continue;
                }
                sql.append("','");
                sql.append(ifEntity.getIpAddress());
                sql.append("','");
                sql.append(ifEntity.getIndex());
                sql.append("','");
                sql.append(ifEntity.getDescr());
                sql.append("','");
                sql.append(ifEntity.getSpeed());
                sql.append("',");
                sql.append(ifEntity.getType());
                sql.append(")");
                conn.addBatch(sql.toString());
            }
        }
        conn.executeBatch();
    }
}
