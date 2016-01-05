/**
 * <p>Description:logger,writes error and debug information within system running</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.dao.AlarmPortDao;
import com.afunms.alarm.dao.SendAlarmTimeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmPort;
import com.afunms.alarm.send.SendAlarmUtil;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.application.model.ApplicationNode;
import com.afunms.application.model.HostServiceGroup;
import com.afunms.application.model.HostServiceGroupConfiguration;
import com.afunms.application.model.JobForAS400Group;
import com.afunms.application.model.JobForAS400GroupDetail;
import com.afunms.application.model.ProcessGroup;
import com.afunms.application.model.ProcessGroupConfiguration;
import com.afunms.application.util.HostServiceGroupConfigurationUtil;
import com.afunms.application.util.JobForAS400GroupDetailUtil;
import com.afunms.application.util.ProcessGroupConfigurationUtil;
import com.afunms.common.base.BaseVo;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Diskconfig;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.CheckEventCompressDao;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.CheckEventCompress;
import com.afunms.event.model.EventList;
import com.afunms.event.model.IManagerTrap;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.ipresource.dao.IpManageBasicDao;
import com.afunms.ipresource.model.IpManageBasicVo;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.snmp.interfaces.InterfaceSnmp;
import com.afunms.topology.model.JobForAS400;

public class CheckEventUtil {

    private static SysLogger logger = SysLogger.getLogger(CheckEventUtil.class
            .getName());

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public CheckEventUtil() {
    }

    /**
     * 未使用，已过时
     */
    @Deprecated
    public void updateData(Object vo, Object collectingData, String type,
            String subtype, AlarmIndicatorsNode alarmIndicatorsNode) {}

    /**
     * 未使用，已过时
     */
    @Deprecated
    public void updatePortData(Object vo, Object collectingData, List list) {}

    /**
     * 未使用，已过时
     */
    @Deprecated
    public void checkMiddlewareEvent(BaseVo node, AlarmIndicatorsNode nm,
         String value) {}


    /**
     * 检查是否为告警
     * <p>
     * 该方法以后可以做为一个通用的方法 来检查是否为一个告警并发送 可作为采集的统一入口 该方法最后调用
     * </p>
     * 
     * @see checkEvent(Host node,AlarmIndicatorsNode nm,String value, String
     *      sIndex)</a>
     *      <p>
     * @param node
     *            设备
     * @param nm
     *            指标
     * @param pingvalue
     *            值
     */
    public void checkEvent(BaseVo baseVo, AlarmIndicatorsNode nm, String value) {
        NodeDTO node = null;
        if (!(baseVo instanceof NodeDTO)) {
            NodeUtil nodeUtil = new NodeUtil();
            node = nodeUtil.conversionToNodeDTO(baseVo);
        } else {
            node = (NodeDTO) baseVo;
        }
        checkEvent(node, nm, value, "");
        return;
    }

    /**
     * 检查是否为告警
     * <p>
     * 该方法以后可以做为一个通用的方法 来检查是否为一个告警并发送 可作为采集的统一入口 该方法最后调用
     * </p>
     * 
     * @see checkEvent(Host node,AlarmIndicatorsNode nm,String value, String
     *      sIndex)</a>
     *      <p>
     * @param node
     *            设备
     * @param nm
     *            指标
     * @param pingvalue
     *            值
     */
    public void checkEvent(Node node, AlarmIndicatorsNode nm, String value) {
        NodeDTO nodeDTO = null;
        NodeUtil nodeUtil = new NodeUtil();
        nodeDTO = nodeUtil.conversionToNodeDTO(node);
        checkEvent(nodeDTO, nm, value, "");
        return;
    }

    /**
     * 检查是否为告警
     * <p>
     * 该方法以后可以做为一个通用的方法 来检查是否为一个告警并发送 可作为采集的统一入口
     * <p>
     * 
     * @param node
     *            设备
     * @param nm
     *            指标
     * @param value
     *            值
     * @param sIndex
     *            多个值时可作为标志存入
     */
    public void checkEvent(BaseVo baseVo, AlarmIndicatorsNode nm, String value,
            String sIndex) {
        NodeDTO node = null;
        if (!(baseVo instanceof NodeDTO)) {
            NodeUtil nodeUtil = new NodeUtil();
            node = nodeUtil.conversionToNodeDTO(baseVo);
        } else {
            node = (NodeDTO) baseVo;
        }
        int alarmLevel = 0; // 告警等级
        // 将内存中相关的告警清除,最终实现从数据库表里删除相关数据
        CheckEvent lastCheckEvent = deleteEvent(nm, sIndex);
        if (nm.getEnabled().equalsIgnoreCase("0")) {
            // 告警指标未监控 不做任何事情 返回
            return;
        }
        if (!AlarmConstant.DATATYPE_NUMBER.equals(nm.getDatatype())) {
            // 非数字类型的返回
            return;
        }
        if (value == null || value.trim().length() == 0) {
            // 未采集值 不做任何事 直接返回
            return;
        }
        // 判断是否发送告警 如果返回 >0 则发送
        try {
            alarmLevel = checkAlarm(node, nm, Double.valueOf(value), sIndex);         
        } catch (Exception e) {
            SysLogger.error("判断是否发送告警出错", e);
        }
        if (alarmLevel > 0) {
            // 需要发送告警产生
            try {
                sendAlarm(node, nm, value, alarmLevel, sIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 未超过告警阀值 则删除告警发送的时间的记录
            SendAlarmTimeDao sendAlarmTimeDao = new SendAlarmTimeDao();
            try {
                if (sIndex != null && sIndex.trim().length() > 0) {
                    sendAlarmTimeDao.delete(nm.getId() + ":" + sIndex);
                } else {
                    sendAlarmTimeDao.delete(nm.getId() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sendAlarmTimeDao.close();
            }

            // 判断之前是否有告警,若有则发送告警恢复信息
            if (lastCheckEvent != null) {

            }
        }
    }

    /**
     * 检查磁盘告警信息
     * 
     * @param node
     * @param diskVector
     * @param nm
     */
    public void checkDisk(Host node, Vector diskVector, AlarmIndicatorsNode nm) {
        // 对 diskVector 磁盘信息
        if ("0".equals(nm.getEnabled())) {
            // 告警指标未监控 不做任何事情 返回
            return;
        }
        if (diskVector == null || diskVector.size() == 0) {
            // 未采集到数据 不做任何事情 返回
            return;
        }
        String ip = node.getIpAddress();
        Hashtable alldiskalarmdata = new Hashtable();
        DiskconfigDao diskconfigDao = new DiskconfigDao();
        try {
            List list = diskconfigDao.loadByIpaddress(ip);
            if (list != null) {
                for (Object object : list) {
                    Diskconfig diskconfig = (Diskconfig) object;
                    alldiskalarmdata.put(ip + ":" + diskconfig.getName() + ":" + diskconfig.getBak(), diskconfig);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        NodeUtil nodeUtil = new NodeUtil();
        for (int i = 0; i < diskVector.size(); i++) {
            Diskcollectdata diskcollectdata = null;
            diskcollectdata = (Diskcollectdata) diskVector.get(i);
            if (diskcollectdata.getEntity().equalsIgnoreCase("Utilization") && "diskperc".equals(nm.getName())) {
                // 利用率
                String diskname = diskcollectdata.getSubentity();
                if (node.getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskname = diskcollectdata.getSubentity().substring(0, 3);
                }
                Diskconfig diskconfig = null;
                if (node.getOstype() == 4
                        || node.getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity().substring(0, 3)
                            + ":" + "利用率阈值");
                } else
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity()
                            + ":"
                            + "利用率阈值");
                if (diskconfig != null) {
                    int limevalue0 = diskconfig.getLimenvalue();
                    int limevalue1 = diskconfig.getLimenvalue1();
                    int limevalue2 = diskconfig.getLimenvalue2();
                    nm.setLimenvalue0(limevalue0 + "");
                    nm.setLimenvalue1(limevalue1 + "");
                    nm.setLimenvalue2(limevalue2 + "");
                    NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
                    checkEvent(nodeDTO, nm, diskcollectdata.getThevalue(), diskname);
                }
            } else if (diskcollectdata.getEntity().equalsIgnoreCase(
                    "UtilizationInc") && "diskinc".equals(nm.getName())) {
                // 增长率
                String diskname = diskcollectdata.getSubentity();
                if (node.getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskname = diskcollectdata.getSubentity().substring(0, 3);
                }
                Diskconfig diskconfig = null;
                if (node.getOstype() == 4
                        || node.getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")) {
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity().substring(0, 3)
                            + ":" + "增长率阈值");
                } else
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity()
                            + ":"
                            + "增长率阈值");
                if (diskconfig != null) {
                    int limevalue0 = diskconfig.getLimenvalue();
                    int limevalue1 = diskconfig.getLimenvalue1();
                    int limevalue2 = diskconfig.getLimenvalue2();
                    nm.setLimenvalue0(limevalue0 + "");
                    nm.setLimenvalue1(limevalue1 + "");
                    nm.setLimenvalue2(limevalue2 + "");
                    NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
                    checkEvent(nodeDTO, nm, diskcollectdata.getThevalue(),
                            diskname);
                }

            }
        }
    }

    /**
     * checkImanagerTrapEvent:
     * <p>华为 Imanager2000 的 Trap 告警
     *
     * @param   node
     *          - {@link ApplicationNode}
     * @param   trap
     *          - Trap 信息
     * @param   nm
     *          - 告警指标
     *
     * @since   v1.01
     */
    public void checkImanagerTrapEvent(ApplicationNode node, IManagerTrap trap, AlarmIndicatorsNode nm) {
    	 deleteEvent(nm);
         if (nm.getEnabled().equalsIgnoreCase("0")) {
             // 告警指标未监控 不做任何事情 返回
             return;
         }
         if (!AlarmConstant.DATATYPE_NUMBER.equals(nm.getDatatype())) {
             // 非数字类型的返回
             return;
         }
         if (trap == null || trap.getName().trim().length() == 0) {
             // 未采集值 不做任何事 直接返回
             return;
         }
         if ("Recovery".equals(trap.getAlarmFlag())) {
        	 // 告警恢复信息，无需处理
        	 return;
         }
         
         // 告警等级为： Warning  Critical 3  Minor 1  Major 2
         int alarmLevel = 0;
         if("Critical".equals(trap.getAlarmLevel())) {
        	 alarmLevel = 3;
         } else if("Major".equals(trap.getAlarmLevel())) {
        	 alarmLevel = 2;
         } else if("Minor".equals(trap.getAlarmLevel())) {
        	 alarmLevel = 1;
         }
         
         //String content = trap.getName();
         StringBuffer content = new StringBuffer();
         content.append("SDH告警："+trap.getName());
         content.append("[" + trap.getDeviceType() + "]");
         // 部件信息，只提取槽号和端口号信息
         String elementInstance = trap.getElementInstance();
         String[] elements = elementInstance.split(" ");
         content.append(" 部件信息：" + elements[1] + " " + elements[2]);
         elements = trap.getAlarmInfo().split(",");
         content.append(" " + elements[2]);
         elements = trap.getReasonOfCausingAlarm().split(",");
         content.append(" 原因：" + elements[2]);

         NodeDTO nodeDTO = new NodeUtil().conversionToNodeDTO(node);
         
         String alarmId = nm.getId() + "";
         String name = nm.getName();
         String nodeId = nm.getNodeid();
         String type = nm.getType();
         String subtype = nm.getSubtype();
         String sIndex = "";
         String collecttime = trap.getCollecttime();
         CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content.toString(), collecttime);
         
         int eventTimes = checkEventTimes(nm, alarmLevel, sIndex);
         if (eventTimes > 0) {
             sendAlarm(nodeDTO, checkEvent, nm);
         } else {
             deleteEvent(nm, sIndex);
         }
    }
    
    /**
     * 检查流速告警
     * 
     * @param node
     * @param diskVector
     * @param nm
     */
    public void checkUtilhdxEvent(NodeDTO node, Vector utilhdxVector, AlarmIndicatorsNode nm) {
        // 对 utilhdxVector 流速信息
        if (nm == null || "0".equals(nm.getEnabled())) {
            // 告警指标未监控 不做任何事情 返回
            return;
        }
        if (utilhdxVector == null || utilhdxVector.size() == 0) {
            // 未采集到数据 不做任何事情 返回
            return;
        }
        String ip = node.getIpaddress();
        Hashtable alarmPortHashtable = new Hashtable();
        AlarmPortDao alarmPortDao = new AlarmPortDao();
        try {
            List list = alarmPortDao.getAllByEnabledAndIp(ip);
            if (list != null) {
                for (Object object : list) {
                    AlarmPort alarmPort = (AlarmPort) object;
                    alarmPortHashtable.put(alarmPort.getIpaddress() + ":" + alarmPort.getPortindex(), alarmPort);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            alarmPortDao.close();
        }
        for (int i = 0; i < utilhdxVector.size(); i++) {
            UtilHdx utilHdx = null;
            utilHdx = (UtilHdx) utilhdxVector.get(i);
            String name = ip + ":" + utilHdx.getSubentity();
            AlarmPort alarmPort = (AlarmPort) alarmPortHashtable.get(name);
            if (alarmPort == null || "0".equals(alarmPort.getEnabled())) {
                continue;
            }
            if ("InBandwidthUtilHdx".equalsIgnoreCase(nm.getName())) {
                nm.setLimenvalue0(alarmPort.getLevelinvalue1()+"");
                nm.setLimenvalue1(alarmPort.getLevelinvalue2()+"");
                nm.setLimenvalue2(alarmPort.getLevelinvalue3()+"");
                nm.setWay0(alarmPort.getWayin1());
                nm.setWay1(alarmPort.getWayin2());
                nm.setWay2(alarmPort.getWayin3());
                nm.setTime0(alarmPort.getLevelintimes1() + "");
                nm.setTime1(alarmPort.getLevelintimes2() + "");
                nm.setTime2(alarmPort.getLevelintimes3() + "");
            } else if ("OutBandwidthUtilHdx".equalsIgnoreCase(nm.getName())) {
                nm.setLimenvalue0(alarmPort.getLeveloutvalue1()+"");
                nm.setLimenvalue1(alarmPort.getLeveloutvalue2()+"");
                nm.setLimenvalue2(alarmPort.getLeveloutvalue3()+"");
                nm.setWay0(alarmPort.getWayout1());
                nm.setWay1(alarmPort.getWayout2());
                nm.setWay2(alarmPort.getWayout3());
                nm.setTime0(alarmPort.getLevelouttimes1() + "");
                nm.setTime1(alarmPort.getLevelouttimes2() + "");
                nm.setTime2(alarmPort.getLevelouttimes3() + "");
            }
            checkEvent(node, nm, utilHdx.getThevalue(), utilHdx.getSubentity());
        }
    }

    /**
     * 
     * 检查主机进程组告警 nielin add
     * 
     * @date 2010-08-18
     * @param ip
     * @param proVector
     */
    public List createProcessGroupEventList(NodeDTO node, Vector proVector,
            AlarmIndicatorsNode alarmIndicatorsNode) {
        if (alarmIndicatorsNode == null|| "0".equals(alarmIndicatorsNode)) {
            return null;
        }
        List retList = new ArrayList();
        if (proVector == null || proVector.size() == 0)
            return retList;
        try {
            String ip = node.getIpaddress();
            ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
            List list = processGroupConfigurationUtil
                    .getProcessGroupByIpAndMonFlag(ip, "1");

            if (list == null || list.size() == 0) {
                return null;
            }
            for (int i = 0; i < list.size(); i++) {
                ProcessGroup processGroup = (ProcessGroup) list.get(i);
                List processGroupConfigurationList = processGroupConfigurationUtil
                        .getProcessGroupConfigurationByGroupId(String
                                .valueOf(processGroup.getId()));

                if (processGroupConfigurationList == null
                        || processGroupConfigurationList.size() == 0) {
                    continue;
                }
                List wrongList = new ArrayList();
                for (int j = 0; j < processGroupConfigurationList.size(); j++) {
                    int num = 0;
                    ProcessGroupConfiguration processGroupConfiguration = (ProcessGroupConfiguration) processGroupConfigurationList
                            .get(j);
                    for (int k = 0; k < proVector.size(); k++) {
                        Processcollectdata processdata = (Processcollectdata) proVector
                                .elementAt(k);
                        if ("Name".equals(processdata.getEntity())) {
                            // -----
                            // 由原来的相等，改为"包含"就可认为"有"。
                            if (processdata.getThevalue().trim().toLowerCase().contains(processGroupConfiguration.getName().trim().toLowerCase())) {
                                num++;
                            }
                            // ----
                            
                        }
                    }
                    int times = Integer.parseInt(processGroupConfiguration
                            .getTimes());
                    String status = processGroupConfiguration.getStatus();
                    if ("1".equals(status)) {
                        if (num > times) {
                            // 多出的个数，黑名单设置为0
                            num = num - times;
                            List wrongProlist = new ArrayList();
                            wrongProlist.add(processGroupConfiguration
                                    .getName());
                            wrongProlist.add(num);
                            wrongProlist.add(status);
                            wrongList.add(wrongProlist);
                        }
                    } else {
                        if (num < times) {
                            // 丢失的个数，白名单至少设置为1
                            num = times - num;
                            List wrongProlist = new ArrayList();
                            wrongProlist.add(processGroupConfiguration
                                    .getName());
                            wrongProlist.add(num);
                            wrongProlist.add(status);

                            wrongList.add(wrongProlist);

                        }
                    }
                }

                String sIndex = processGroup.getName();
                deleteEvent(alarmIndicatorsNode, sIndex);
                if (wrongList.size() > 0) {
                    String message = ip + " 进程组为：" + processGroup.getName()
                            + " 出现进程异常!";
                    for (int j = 0; j < wrongList.size(); j++) {
                        List wrongProList = (List) wrongList.get(j);
                        String status = (String) wrongProList.get(2);
                        if ("1".equals(status)) {
                            message = message + "进程：" + wrongProList.get(0)
                                    + "超出个数为：" + wrongProList.get(1) + ";";
                        } else {
                            message = message + "进程：" + wrongProList.get(0)
                                    + "丢失个数为：" + wrongProList.get(1) + ";";
                        }
                    }
                    
                    String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                    String name = alarmIndicatorsNode.getName();
                    String nodeId = alarmIndicatorsNode.getNodeid();
                    String type = alarmIndicatorsNode.getType();
                    String subtype = alarmIndicatorsNode.getSubtype();
                    int alarmLevel = Integer.valueOf(processGroup.getAlarm_level());
                    String content = message;
                    String collecttime = sdf.format(new Date());
                    CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);
                    
                    retList.add(checkEvent);
                    int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
                    if (eventTimes > 0) {
                        sendAlarm(node, checkEvent, alarmIndicatorsNode);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }
    
    public List createArpEventList(NodeDTO node, Vector arpVector, AlarmIndicatorsNode alarmIndicatorsNode) {
    	if (alarmIndicatorsNode == null|| "".equals(alarmIndicatorsNode)) {
            return null;
        }
    	
    	List returnList = new ArrayList();
    	List ipmacList = new ArrayList();
    	List alarmArpList = new ArrayList();	//告警异常ARP列表
    	
    	if(arpVector == null || arpVector.size() == 0) {
    		return returnList;
    	}
    	
    	try {
    		//将采集的数据与基础表对比
    		String ip = node.getIpaddress();
    		IpManageBasicDao dao = new IpManageBasicDao();
    		List<IpManageBasicVo> list = dao.loadAll();
    		
    		if(list != null && list.size() > 0) {
    			for (int i = 0; i < list.size(); i++) {
    				ipmacList.add(list.get(i).getIpaddress());
    			}
    		}
    		
    		
    		for (int i = 0; i < arpVector.size(); i++) {
    			IpMac ipmac = (IpMac) arpVector.get(i);
				if(!ipmacList.contains(ipmac.getIpaddress())) {
					//基础表里面不包含的则告警
					alarmArpList.add(arpVector.get(i));
				} 
			}
    		
    		deleteEvent(alarmIndicatorsNode);
    		if(alarmArpList.size() > 0) {
    			for (int i = 0; i < alarmArpList.size(); i++) {
    				IpMac macVo = (IpMac) alarmArpList.get(i);
    				
    				StringBuffer message = new StringBuffer();
    				message.append("IPMAC 出现异常IP：");
    				message.append(macVo.getIpaddress());
    				
    				String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                    String name = alarmIndicatorsNode.getName();
                    String nodeId = alarmIndicatorsNode.getNodeid();
                    String type = alarmIndicatorsNode.getType();
                    String subtype = alarmIndicatorsNode.getSubtype();
                    int alarmLevel = 1;	//普通告警
                    String content = message.toString();
                    String collecttime = sdf.format(new Date());
                    String sIndex = macVo.getIpaddress();
                    
                    CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);
                    returnList.add(checkEvent);
                    int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
                    if (eventTimes > 0) {
                        sendAlarm(node, checkEvent, alarmIndicatorsNode);
                    }
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return returnList;
    }

    /**
     * 检查主机服务组告警
     * 
     * @param ip
     * @param hostServiceVector
     * @return
     */
    public List createHostServiceGroupEventList(NodeDTO node,
            Vector hostServiceVector, AlarmIndicatorsNode alarmIndicatorsNode) {
        if (alarmIndicatorsNode == null) {
            return null;
        }
        List returnList = new ArrayList();
        if (hostServiceVector == null || hostServiceVector.size() == 0)
            return returnList;

        try {

            String ip = node.getIpaddress();

            HostServiceGroupConfigurationUtil hostServiceGroupConfigurationUtil = new HostServiceGroupConfigurationUtil();
            List list = hostServiceGroupConfigurationUtil
                    .gethostservicegroupByIpAndMonFlag(ip, "1");

            if (list == null || list.size() == 0) {
                return returnList;
            }

            for (int i = 0; i < list.size(); i++) {
                HostServiceGroup hostServiceGroup = (HostServiceGroup) list
                        .get(i);
                List hostServiceList = hostServiceGroupConfigurationUtil
                        .gethostservicegroupConfigurationByGroupId(String
                                .valueOf(hostServiceGroup.getId()));

                if (hostServiceList == null || hostServiceList.size() == 0) {
                    continue;
                }

                List wrongList = new ArrayList();

                // 有告警的黑名单服务列表
                List blackWrongList = new ArrayList();

                for (int j = 0; j < hostServiceList.size(); j++) {
                    HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) hostServiceList
                            .get(j);
                    String status = hostServiceGroupConfiguration.getStatus();// 1:活动（黑名单）
                                                                                // 0:不活动（白名单）
                    boolean isLived = false;

                    if (hostServiceVector != null) {
                        for (int k = 0; k < hostServiceVector.size(); k++) {
                            Servicecollectdata servicedata = (Servicecollectdata) hostServiceVector
                                    .get(k);
                            if (hostServiceGroupConfiguration.getName().trim()
                                    .equals(servicedata.getName())) {
                                isLived = true;
                                break;
                            }
                        }
                    }

                    if (!isLived && "0".equals(status)) {// 增加白名单判断条件 (不存在该进程
                                                            // 则告警)
                        wrongList.add(hostServiceGroupConfiguration);
                    }
                    if (isLived && "1".equals(status)) {// 增加黑名单判断条件 (存在该进程则告警)
                        blackWrongList.add(hostServiceGroupConfiguration);
                    }
                }
                StringBuffer message = new StringBuffer();
                message.append(ip);
                message.append(" 主机服务组为：");
                message.append(hostServiceGroup.getName());
                message.append("出现主机服务告警! ");
                if (wrongList.size() > 0) {
                    for (int j = 0; j < wrongList.size(); j++) {
                        HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) wrongList
                                .get(j);
                        message.append("主机服务：");
                        message.append(hostServiceGroupConfiguration.getName());
                        message.append("丢失！; ");
                    }
                }
                if (blackWrongList.size() > 0) {
                    for (int j = 0; j < blackWrongList.size(); j++) {
                        HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) blackWrongList
                                .get(j);
                        message.append("主机服务：");
                        message.append(hostServiceGroupConfiguration.getName());
                        message.append("已启动！; ");
                    }
                }
                String sIndex = hostServiceGroup.getName();
                deleteEvent(alarmIndicatorsNode, sIndex);
                if (wrongList.size() > 0 || blackWrongList.size() > 0) {
                    String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                    String name = alarmIndicatorsNode.getName();
                    String nodeId = alarmIndicatorsNode.getNodeid();
                    String type = alarmIndicatorsNode.getType();
                    String subtype = alarmIndicatorsNode.getSubtype();
                    int alarmLevel = Integer.valueOf(hostServiceGroup.getAlarm_level());
                    String content = message.toString();
                    String collecttime = sdf.format(new Date());
                    CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);

                    returnList.add(checkEvent);
                    int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
                    if (eventTimes > 0) {
                        sendAlarm(node, checkEvent, alarmIndicatorsNode);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    /**
     * 创建as400工作组告警
     * 
     * @param ip
     * @param hostServiceVector
     * @return
     */
    public List createJobForAS400GroupEventList(String ip,
            List jobForAS400list, AlarmIndicatorsNode alarmIndicatorsNode) {
        if (alarmIndicatorsNode == null) {
            return null;
        }
        List returnList = new ArrayList();
        if (jobForAS400list == null || jobForAS400list.size() == 0)
            return returnList;

        try {

            Node hostNode = PollingEngine.getInstance().getNodeByIP(ip);
            NodeUtil nodeUtil = new NodeUtil();
            NodeDTO node = nodeUtil.conversionToNodeDTO(hostNode);

            JobForAS400GroupDetailUtil jobForAS400GroupDetailUtil = new JobForAS400GroupDetailUtil();
            List list = jobForAS400GroupDetailUtil
                    .getJobForAS400GroupByIpAndMonFlag(ip, "1");

            if (list == null || list.size() == 0) {
                return returnList;
            }

            for (int i = 0; i < list.size(); i++) {
                try {
                    JobForAS400Group jobForAS400Group = (JobForAS400Group) list
                            .get(i);
                    List jobForAS400DetailList = jobForAS400GroupDetailUtil
                            .getJobForAS400GroupDetailByGroupId(String
                                    .valueOf(jobForAS400Group.getId()));

                    if (jobForAS400DetailList == null
                            || jobForAS400DetailList.size() == 0) {
                        continue;
                    }

                    List wrongList = new ArrayList();

                    for (int j = 0; j < jobForAS400DetailList.size(); j++) {
                        JobForAS400GroupDetail jobForAS400GroupDetail = (JobForAS400GroupDetail) jobForAS400DetailList
                                .get(j);

                        boolean isLived = false;
                        List jobForAS400List2 = new ArrayList();
                        if (jobForAS400list != null) {
                            for (int k = 0; k < jobForAS400list.size(); k++) {
                                JobForAS400 jobForAS400 = (JobForAS400) jobForAS400list
                                        .get(k);
                                if (jobForAS400GroupDetail.getName().trim()
                                        .equals(jobForAS400.getName())) {
                                    jobForAS400List2.add(jobForAS400);
                                    isLived = true;
                                }
                            }
                        }

                        String eventMessage = "";

                        Vector perVector = new Vector();
                        if (jobForAS400GroupDetail.getStatus().equals("0")
                                && isLived) {
                            // 如果 作业出现 并且 作业的监控状态为不允许出现 则告警
                            perVector.add(jobForAS400GroupDetail);
                            perVector.add("作业："
                                    + jobForAS400GroupDetail.getName()
                                    + " 出现活动,且个数为：" + jobForAS400List2.size()
                                    + ";");
                        } else if (jobForAS400GroupDetail.getStatus().equals(
                                "1")
                                && !isLived) {
                            // 如果 作业未出现 并且 作业的监控状态为必须出现 则告警
                            perVector.add(jobForAS400GroupDetail);
                            perVector.add("作业："
                                    + jobForAS400GroupDetail.getName()
                                    + " 未活动;");
                        } else if (!jobForAS400GroupDetail.getStatus().equals(
                                "0")
                                && isLived) {
                            // 如果 作业出现 并且 作业的监控状态为允许出现 则进一步判断
                            if (!"-1".equals(jobForAS400GroupDetail
                                    .getActiveStatusType())) {
                                // 如果 作业的活动的监控状态不是不限 则进行判断

                                try {
                                    int num = Integer
                                            .valueOf(jobForAS400GroupDetail
                                                    .getNum());
                                    if (num > jobForAS400List2.size()) {
                                        eventMessage = "作业："
                                                + jobForAS400GroupDetail
                                                        .getName()
                                                + " 出现异常,个数少于监控数目,丢失："
                                                + (num - jobForAS400List2
                                                        .size()) + "个";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String activeStatus = jobForAS400GroupDetail
                                        .getActiveStatus();
                                if (activeStatus != null) {
                                    for (int m = 0; m < jobForAS400List2.size(); m++) {
                                        JobForAS400 jobForAS400 = (JobForAS400) jobForAS400List2
                                                .get(m);
                                        // 判断每一个出现的作业
                                        if ("1".equals(jobForAS400GroupDetail
                                                .getActiveStatusType()) != (activeStatus
                                                .indexOf(jobForAS400
                                                        .getActiveStatus()) != -1)) {
                                            // 如果 作业活动状态类型为必须出现 则 活动状态必须在当前监控状态中
                                            // ；如果不对出现异常
                                            // 如果 作业活动状态类型为不允许出现 则
                                            // 活动状态不能出现在当前监控状态中 ； 如果不对 出现异常
                                            eventMessage = eventMessage
                                                    + "作业："
                                                    + jobForAS400GroupDetail
                                                            .getName()
                                                    + " 出现异常状态为; 其状态为："
                                                    + jobForAS400
                                                            .getActiveStatus()
                                                    + ";";
                                        }
                                    }
                                }

                                if (eventMessage.trim().length() > 1) {
                                    perVector.add(jobForAS400GroupDetail);
                                    perVector.add(eventMessage);
                                }
                            }
                        }
                        if (perVector.size() > 1) {
                            wrongList.add(perVector);
                        }
                    }
                    String sIndex = jobForAS400Group.getName();
                    deleteEvent(alarmIndicatorsNode, sIndex);
                    if (wrongList.size() > 0) {
                        String message = ip + " 的作业组："
                                + jobForAS400Group.getName() + " 出现异常!";
                        for (int j = 0; j < wrongList.size(); j++) {
                            Vector perVector = (Vector) wrongList.get(j);
                            JobForAS400GroupDetail jobForAS400GroupDetail = (JobForAS400GroupDetail) perVector
                                    .get(0);
                            message = message + perVector.get(1);

                        }
                        
                        String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                        String name = alarmIndicatorsNode.getName();
                        String nodeId = alarmIndicatorsNode.getNodeid();
                        String type = alarmIndicatorsNode.getType();
                        String subtype = alarmIndicatorsNode.getSubtype();
                        int alarmLevel = Integer.valueOf(jobForAS400Group.getAlarm_level());
                        String content = message;
                        String collecttime = sdf.format(new Date());
                        CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);

                        int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
                        if (eventTimes > 0) {
                            sendAlarm(node, checkEvent, alarmIndicatorsNode);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * 创建as400工作组告警
     * 
     * @param ip
     * @param hostServiceVector
     * @return
     */
    public List createInterfaceEventList(NodeDTO node,
            Vector interfaceVector, AlarmIndicatorsNode alarmIndicatorsNode) {
        if (alarmIndicatorsNode == null || "0".equals(alarmIndicatorsNode.getEnabled())) {
            return null;
        }
        if (node == null) {
            return null;
        }
        List returnList = new ArrayList();
        if (interfaceVector == null || interfaceVector.size() == 0)
            return returnList;
        try {
            PortconfigDao portconfigDao = new PortconfigDao();
            List list = null;
            try {
                list = portconfigDao.loadByIpaddress(node.getIpaddress());
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                portconfigDao.close();
            }

            if (list == null || list.size() == 0) {
                return returnList;
            }
            Hashtable portconfigHash = new Hashtable();

            if(list != null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    Portconfig portconfig = (Portconfig)list.get(i);
                    portconfigHash.put(portconfig.getPortindex()+"", portconfig);
                }
            }
            List wrongList = new ArrayList();
            Hashtable<String, String> sIndexAndDescr = new Hashtable<String, String>();
            for (Object object : interfaceVector) {
                Interfacecollectdata interfacecollectdata = (Interfacecollectdata) object;
                String sIndex = interfacecollectdata.getSubentity();
                String value = interfacecollectdata.getThevalue();
                if (sIndex == null) {
                    continue;
                }
                if (InterfaceSnmp.DESC_IFDESCR.equals(interfacecollectdata.getEntity())) {
                    sIndexAndDescr.put(sIndex, value);
                    continue;
                }
                if (!InterfaceSnmp.DESC_IFOPERSTATUS.equals(interfacecollectdata.getEntity())) {
                    continue;
                }
                deleteEvent(alarmIndicatorsNode, sIndex);
                Portconfig portconfig = (Portconfig)portconfigHash.get(sIndex);
                try {
                    if (portconfig != null && portconfig.getSms().intValue() == 1) {
                        if (!"up".equalsIgnoreCase(value)){
                            wrongList.add(interfacecollectdata);
                        } else {
                        	// 未超过告警阀值 则删除告警发送的时间的记录
                            SendAlarmTimeDao sendAlarmTimeDao = new SendAlarmTimeDao();
                            try {
                                if (sIndex != null && sIndex.trim().length() > 0) {
                                    sendAlarmTimeDao.delete(alarmIndicatorsNode.getId() + ":" + sIndex);
                                } else {
                                    sendAlarmTimeDao.delete(alarmIndicatorsNode.getId() + "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                sendAlarmTimeDao.close();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (wrongList.size() > 0) {
                for (int j = 0; j < wrongList.size(); j++) {
                    Interfacecollectdata interfacecollectdata = (Interfacecollectdata)wrongList.get(j);
                    String sIndex = interfacecollectdata.getSubentity();
                    String value = interfacecollectdata.getThevalue();
                    if (sIndex == null) {
                        continue;
                    }
                    String unit = alarmIndicatorsNode.getThreshlod_unit();
                    if ("无".equals(unit)) {
                        unit = "";
                    }
                    String threshold = "up";

                    String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                    String name = alarmIndicatorsNode.getName();
                    String nodeId = alarmIndicatorsNode.getNodeid();
                    String type = alarmIndicatorsNode.getType();
                    String subtype = alarmIndicatorsNode.getSubtype();
                    int alarmLevel = 3;
                    String content = node.getName() + "(IP: " + node.getIpaddress() + ") "
                                        + sIndexAndDescr.get(sIndex) + " " + alarmIndicatorsNode.getAlarm_info() + " 当前值:"
                                        + value + " " + unit + " 阈值:" + threshold + " " + unit;
                    String collecttime = sdf.format(new Date());
                    CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);

                    int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
                    if (eventTimes > 0) {
                        sendAlarm(node, checkEvent, alarmIndicatorsNode);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }
    
    /**
     * 创建SNMP未开启的告警
     * 
     * @param ip
     * @param hostServiceVector
     * @return
     */
    public boolean createSNMPEventList(Host host,AlarmIndicatorsNode alarmIndicatorsNode, String value) {
        deleteEvent(alarmIndicatorsNode);
        if (alarmIndicatorsNode == null || "0".equals(alarmIndicatorsNode.getEnabled())) {
            return false;
        }
        if (host == null) {
            return false;
        }
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO node = nodeUtil.conversionToNodeDTO(host);
        List returnList = new ArrayList();
        try {
             String unit = alarmIndicatorsNode.getThreshlod_unit();
             if ("无".equals(unit)) {
                 unit = "";
             }
             String threshold = "up";
             if(value.trim().length()>0){
                 threshold = "开启";
                 // 未超过告警阀值 则删除告警发送的时间的记录
                 SendAlarmTimeDao sendAlarmTimeDao = new SendAlarmTimeDao();
                 try {
                      sendAlarmTimeDao.delete(alarmIndicatorsNode.getId() + "");
                 } catch (Exception e) {
                     e.printStackTrace();
                 } finally {
                     sendAlarmTimeDao.close();
                 }
                 return false;
             }else{
                 threshold = "关闭";
             }

             String alarmId = String.valueOf(alarmIndicatorsNode.getId());
             String name = alarmIndicatorsNode.getName();
             String nodeId = alarmIndicatorsNode.getNodeid();
             String type = alarmIndicatorsNode.getType();
             String subtype = alarmIndicatorsNode.getSubtype();
             String sIndex = null;
             int alarmLevel = 3;
             String content = node.getName() + "(IP: " + node.getIpaddress() + ") "
                                 + " " + alarmIndicatorsNode.getAlarm_info() + " 当前值:"
                                 + " 关闭" + unit + " 阈值:" + threshold + " " + unit;
             String collecttime = sdf.format(new Date());
             CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);

             int eventTimes = checkEventTimes(alarmIndicatorsNode, alarmLevel, sIndex);
             if (eventTimes > 0) {
                 sendAlarm(node, checkEvent, alarmIndicatorsNode);
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 确认是否为告警
     * <p>
     * 当发生事件的次数大于规定的次数 则作为告警，并返回告警的等级 如果不告警则返回
     * <p>
     * 
     * @param node
     *            设备
     * @param nm
     *            指标
     * @param value
     *            值
     * @param name
     *            告警唯一标志
     * @return
     */
    private int checkAlarm(NodeDTO node, AlarmIndicatorsNode nm, double value,
            String sIndex) {
         //SysLogger.info("===========开始确认是否为告警=============");
        int alarmLevel = 0;
        int eventLevel = 0; // 事件等级
        int eventTimes = 0; // 事件次数
        double limenvalue0 = Double.parseDouble(nm.getLimenvalue0());// 一级阀值
        double limenvalue1 = Double.parseDouble(nm.getLimenvalue1());// 二级阀值
        double limenvalue2 = Double.parseDouble(nm.getLimenvalue2());// 三级阀值
        // 检查事件等级
        eventLevel = checkEventLevel(value, limenvalue0, limenvalue1,
                limenvalue2, nm.getCompare());
        // SysLogger.info("========检查事件等级===========" + eventLevel);
        // 检查事件次数
        // 如果传入的事件等级为 0 则说明事件恢复 则进行清楚 事件次数
        // 如果返回次数大于 0 则说明大于规定的事件次数 将事件升级为告警 如果返回次数不大于 0 则只是将事件次数 + 1;;
        eventTimes = checkEventTimes(nm, eventLevel, sIndex);
        // SysLogger.info("======== 检查事件次数 ===========" + eventTimes);
        if (eventTimes > 0) {
            // 如果大于 0 将事件等级升级 为 告警等级
            alarmLevel = eventLevel;
        }
        return alarmLevel;

    }

    /**
     * 进入告警发送
     * 
     * @param node
     *            设备
     * @param alarmIndicatorsNode
     *            指标
     * @param value
     *            采集到的值
     * @param alarmLevel
     *            告警等级
     */
    public void sendAlarm(NodeDTO node,
            AlarmIndicatorsNode alarmIndicatorsNode, String value,
            int alarmLevel) {
        sendAlarm(node, alarmIndicatorsNode, value, alarmLevel, "");
    }

    /**
     * 进入告警发送
     * 
     * @param node
     *            设备
     * @param alarmIndicatorsNode
     *            指标
     * @param value
     *            采集到的值
     * @param alarmLevel
     *            告警等级
     */
    public void sendAlarm(NodeDTO node,
            AlarmIndicatorsNode alarmIndicatorsNode, String value,
            int alarmLevel, String sIndex) {
        // 将该告警对比信息放到内存系统里,最终需要放到数据库表里
        if (sIndex == null) {
            sIndex = "";
        }
        // --------------------
        // --------------------
        String contentSindex = sIndex;
        if ("InBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())
                || "OutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
            AlarmPortDao alarmPortDao = new AlarmPortDao();
            try {
                List list = alarmPortDao.getAllByEnabledAndIp(node.getIpaddress());
                if (list != null) {
                    for (Object object : list) {
                        AlarmPort alarmPort = (AlarmPort) object;
                        if (sIndex.equalsIgnoreCase(String.valueOf(alarmPort.getPortindex()))) {
                            contentSindex = alarmPort.getName();
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                alarmPortDao.close();
            }
        }

        String unit = alarmIndicatorsNode.getThreshlod_unit();
        String threshold = getThresholdByLevel(alarmIndicatorsNode, alarmLevel);
        String alarmId = String.valueOf(alarmIndicatorsNode.getId());
        String name = alarmIndicatorsNode.getName();
        String nodeId = alarmIndicatorsNode.getNodeid();
        String type = alarmIndicatorsNode.getType();
        String subtype = alarmIndicatorsNode.getSubtype();
        String content = node.getName() + "(IP: " + node.getIpaddress() + ") "
                            + contentSindex + " " + alarmIndicatorsNode.getAlarm_info() + " 当前值:"
                            + value + " " + unit + " 阀值:" + threshold + " " + unit;
        String collecttime = sdf.format(new Date());
        CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);
        
        sendAlarm(node, checkEvent, alarmIndicatorsNode);
    }

    /**
     * sendAlarm:
     * <p>与最终告警出口同名方法，最后调用 #sendAlarm(CheckEvent, EventList, AlarmIndicatorsNode)
     *
     * @param   checkEvent
     *          - 
     * @param   eventList
     *          - 
     * @param   alarmIndicatorsNode
     *          -
     *
     * @since   v1.01
     * 
     * @see     #sendAlarm(CheckEvent, EventList, AlarmIndicatorsNode)
     */
    public void sendAlarm(NodeDTO node, CheckEvent checkEvent,
                    AlarmIndicatorsNode alarmIndicatorsNode) {
        EventList eventList = createEventList(node, checkEvent);
        sendAlarm(checkEvent, eventList, alarmIndicatorsNode);
    }

    /**
     * sendAlarm:
     * <p>最终告警出口
     *
     * @param   checkEvent
     *          - 
     * @param   eventList
     *          - 
     * @param   alarmIndicatorsNode
     *          -
     *
     * @since   v1.01
     */
    public void sendAlarm(CheckEvent checkEvent, EventList eventList,
                    AlarmIndicatorsNode alarmIndicatorsNode) {
        SendAlarmUtil alarmUtil = new SendAlarmUtil();
        saveEvent(checkEvent);
        alarmUtil.sendAlarm(checkEvent, eventList, alarmIndicatorsNode);
    }

    /**
     * 创建告警类
     * 
     * @param eventtype
     * @param eventlocation
     * @param bid
     * @param content
     * @param level1
     * @param subtype
     * @param subentity
     * @param ipaddress
     * @param objid
     * @return
     */
    private CheckEvent createCheckEvent(String alarmId, String name,
            String nodeId, String type, String subtype, String sIndex,
            int alarmLevel, String content, String collecttime) {
        CheckEvent checkEvent = new CheckEvent();
        checkEvent.setAlarmId(alarmId);
        checkEvent.setName(name);
        checkEvent.setNodeId(nodeId);
        checkEvent.setType(type);
        checkEvent.setSubtype(subtype);
        checkEvent.setSindex(sIndex);
        checkEvent.setAlarmlevel(alarmLevel);
        checkEvent.setContent(content);
        checkEvent.setCollecttime(collecttime);
        return checkEvent;
    }

    private EventList createEventList(NodeDTO node, CheckEvent checkEvent) {
        String eventtype = "poll";
        String eventlocation = node.getName() + "(" + node.getIpaddress() + ")";
        String bid = node.getBusinessId();
        String content = checkEvent.getContent();
        Integer level1 = checkEvent.getAlarmlevel();
        String subtype = checkEvent.getType();
        String subentity = checkEvent.getName();
        String objid = checkEvent.getNodeId();
        String ipaddress = node.getIpaddress();
        return createEventList(eventtype, eventlocation, bid, content, level1, subtype, subentity, ipaddress, objid);
    }

    /**
     * 创建告警类
     * 
     * @param eventtype
     * @param eventlocation
     * @param bid
     * @param content
     * @param level1
     * @param subtype
     * @param subentity
     * @param ipaddress
     * @param objid
     * @return
     */
    private EventList createEventList(String eventtype, String eventlocation,
            String bid, String content, int level1, String subtype,
            String subentity, String ipaddress, String objid) {
        // 生成事件
        EventList eventlist = new EventList();
        eventlist.setEventtype(eventtype);
        eventlist.setEventlocation(eventlocation);
        eventlist.setContent(content);
        eventlist.setLevel1(level1);
        eventlist.setManagesign(0);
        eventlist.setBak("");
        eventlist.setRecordtime(Calendar.getInstance());
        eventlist.setReportman("系统轮询");
        eventlist.setBusinessid(bid);
        eventlist.setNodeid(Integer.parseInt(objid));
        eventlist.setOid(0);
        eventlist.setSubtype(subtype);
        eventlist.setSubentity(subentity);
        return eventlist;
    }

    /**
     * 
     * 检查事件等级
     * 
     * <p>
     * compare_type 比较方式，1 为升序 即大于比较，0 为降序 即小于比较
     * </p>
     * <p>
     * 返回告警等级 如果不告警则返回 0
     * </p>
     * 
     * @author nielin
     * @param value
     *            值
     * @param limenvalue0
     *            一级阀值
     * @param limenvalue1
     *            二级阀值
     * @param limenvalue2
     *            三级阀值
     * @param compare_type
     *            比较方式
     * 
     * @return level
     */
    private int checkEventLevel(double value, double limenvalue0,
            double limenvalue1, double limenvalue2, int compare_type) {
        int level = 0; // 需要返回的等级
        // SysLogger.info(value + "=======" + compare_type);
        if (compare_type == 0) {
            // 降序比较
            if (value <= limenvalue2) {
                level = 3;
            } else if (value <= limenvalue1) {
                level = 2;
            } else if (value <= limenvalue0) {
                level = 1;
            } else {
                level = 0;
            }
        } else {
            // 升序比较
            if (value >= limenvalue2) {
                level = 3;
            } else if (value >= limenvalue1) {
                level = 2;
            } else if (value >= limenvalue0) {
                level = 1;
            } else {
                level = 0;
            }
        }
        return level;
    }

    /**
     * 检查告警次数
     * <p>
     * 如果大于规定的告警次数，返回一个大于 0的数 否则返回 0 ，并同时将告警次数 + 1
     * <p>;
     * 
     * @param alarmIndicatorsNode
     *            告警指标
     * @param alarmLevel
     *            当前告警等级
     * @return
     */
    private int checkEventTimes(AlarmIndicatorsNode alarmIndicatorsNode,
            int eventLevel, String sIndex) {
        int eventTimes = 0; // 发生事件的次数
        int defineTimes = 0; // 定义的连续发生事件次数
        int lastEventTimes = 0; // 之前发生的事件次数
        String name = "";
        if (sIndex!= null && sIndex.trim().length() > 0) {
            name = alarmIndicatorsNode.getId() + ":" + sIndex;
        } else {
            name = alarmIndicatorsNode.getId() + "";
        }
        if (eventLevel == 0) {
            // 如果事件等级 为 0 说明事件恢复 则清除事件次数归为 0
            setEventTimes(name, 0);
            return eventTimes;
        }

        defineTimes = getTimesByLevel(alarmIndicatorsNode, eventLevel);
        lastEventTimes = getEventTimes(name, eventLevel);
        eventTimes = lastEventTimes + 1; // 上次加这次 然后保存
        setEventTimes(name, eventTimes);
        if (eventTimes < defineTimes) {
            // 如果小于定义的次数则不产生告警 返回 0
            eventTimes = 0;
        }
        return eventTimes;
    }

    /**
     * 得到之前的告警次数
     * 
     * @param name
     * @param alarmLevel
     * @return
     */
    private int getEventTimes(String name, int alarmLevel) {
        int times = 0;
        try {
            String num = (String) AlarmResourceCenter.getInstance()
                    .getAttribute(name);
            if (num != null && num.length() > 0) {
                times = Integer.parseInt(num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 设置告警次数
     * 
     * @param name
     * @param times
     * @return
     */
    private int setEventTimes(String name, int times) {
        try {
            AlarmResourceCenter.getInstance().setAttribute(name,
                    String.valueOf(times));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 根据指标和告警等级返回阀值
     * 
     * @param nm
     * @param alarmLevel
     * @return
     */
    private String getThresholdByLevel(AlarmIndicatorsNode nm, int alarmLevel) {
        String threshold = "";
        if (alarmLevel == 1) {
            threshold = nm.getLimenvalue0();
        } else if (alarmLevel == 2) {
            threshold = nm.getLimenvalue1();
        } else if (alarmLevel == 3) {
            threshold = nm.getLimenvalue2();
        }
        return threshold;
    }

    /**
     * 根据指标和告警等级返回定义的告警次数
     * 
     * @param nm
     * @param alarmLevel
     * @return
     */
    private int getTimesByLevel(AlarmIndicatorsNode nm, int eventLevel) {
        int times_int = 0;
        String times_str = "0";
        if (eventLevel == 1) {
            times_str = nm.getTime0();
        } else if (eventLevel == 2) {
            times_str = nm.getTime1();
        } else if (eventLevel == 3) {
            times_str = nm.getTime2();
        }
        try {
            times_int = Integer.parseInt(times_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times_int;
    }

    /**
     * 从 数据库中 删除上次的告警 如果存在则找出后 在删除并返回 ， 如果不存在则null
     * <p>
     * 将内存中相关的告警清除,最终实现从数据库表里删除相关数据
     * <p>
     * 
     * @param name
     * @return CheckEvent
     */
    private CheckEvent deleteEvent(AlarmIndicatorsNode alarmIndicatorsNode) {
        return deleteEvent(alarmIndicatorsNode, null);
    }

    /**
     * 从 数据库中 删除上次的告警 如果存在则找出后 在删除并返回 ， 如果不存在则null
     * <p>
     * 将内存中相关的告警清除,最终实现从数据库表里删除相关数据
     * <p>
     * 
     * @param name
     * @return CheckEvent
     */
    private CheckEvent deleteEvent(AlarmIndicatorsNode alarmIndicatorsNode, String sIndex) {
       
    	// 删除压缩表告警信息
    	deleteFromCompress(alarmIndicatorsNode,sIndex);	// add by yag 
    	
    	if (alarmIndicatorsNode == null) {
            return null;
        }
        String alarmId = alarmIndicatorsNode.getId() + "";
        boolean flag = false;
        if (sIndex == null || sIndex.trim().length() == 0) { 
            sIndex = "";
            flag = true;
        }
        CheckEvent checkEvent = null;
        CheckEventDao checkeventdao = new CheckEventDao();
        try {
            if (flag) {
                checkEvent = (CheckEvent) checkeventdao.findCheckEventByAlarmId(alarmId);
            } else {
                checkEvent = (CheckEvent) checkeventdao.findCheckEventByAlarmIdAndSindex(alarmId, sIndex);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            checkeventdao.close();
        }
        checkeventdao = new CheckEventDao();
        try {
            if (flag) {
                checkeventdao.deleteByAlarmId(alarmId);
            } else {
                checkeventdao.deleteByAlarmIdAndSindex(alarmId, sIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkeventdao.close();
        }
        return checkEvent;
    }
    
    /**
     * 删除告警压缩表中的指定信息
     * 
     * deleteFromCompress:
     * <p>
     *
     * @param alarmIndicatorsNode
     * @param sIndex
     *
     * @since   v1.01
     */
    private void deleteFromCompress(AlarmIndicatorsNode alarmIndicatorsNode,String sIndex) {
    	 if (alarmIndicatorsNode == null) {
             return ;
         }
         String alarmId = alarmIndicatorsNode.getId() + "";
         boolean flag = false;
         if (sIndex == null || sIndex.trim().length() == 0) { 
             sIndex = "";
             flag = true;
         }
         CheckEventCompress checkEvent = null;
         CheckEventCompressDao checkeventCompressdao = new CheckEventCompressDao();
         try {
             if (flag) {
                 checkEvent = (CheckEventCompress) checkeventCompressdao.findCheckEventCompressByAlarmId(alarmId);
             } else {
                 checkEvent = (CheckEventCompress) checkeventCompressdao.findCheckEventCompressByAlarmIdAndSindex(alarmId, sIndex);
             }
         } catch (Exception e1) {
             e1.printStackTrace();
         } finally {
             checkeventCompressdao.close();
         }
         checkeventCompressdao = new CheckEventCompressDao();
         try {
             if (flag) {
            	 checkeventCompressdao.deleteByAlarmId(alarmId);
             } else {
            	 checkeventCompressdao.deleteByAlarmIdAndSindex(alarmId, sIndex);
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
        	 checkeventCompressdao.close();
         }
    	
    }

    /**
     * 将本次告警信息保存到数据库中
     * <p>
     * 将该告警对比信息放到内存系统里,最终需要放到数据库表里
     * <p>
     * 
     * @param name
     */
    private void saveEvent(CheckEvent checkEvent) {  	
        CheckEventDao checkeventdao = new CheckEventDao();
        try {
            if (checkEvent.getSindex() != null && checkEvent.getSindex().length() > 0) {
                checkeventdao.deleteByAlarmIdAndSindex(checkEvent.getAlarmId(), checkEvent.getSindex());
            } else {
                checkeventdao.deleteByAlarmId(checkEvent.getAlarmId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkeventdao.close();
        }
        checkeventdao = new CheckEventDao();
        try {
            checkeventdao.save(checkEvent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkeventdao.close();
        }
    }

    /**
     * saveCheckEventIntoCompress:
     * 将告警信息保存在告警压缩表中 - nms_checkevent_compress
     * 
     * <p>
     *
     * @param checkEvent
     *
     * @since   v1.01
     */
    private void saveCheckEventIntoCompress(CheckEvent checkEvent) {
		CheckEventCompress compress = eventToCompress(checkEvent);
		CheckEventCompressDao checkEventCompressDao = new CheckEventCompressDao();
		try{
			checkEventCompressDao.save(compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkEventCompressDao.close();
		}
    	
    }
    
    /**
	 * CheckEvent转为CheckEventCompress
	 * 	
	 * 	firsttime字段与collecttime相同
	 * 	
	 * @param event
	 * @return
	 */
	private CheckEventCompress eventToCompress(CheckEvent event) {
		CheckEventCompress compress = new CheckEventCompress();
		compress.setAlarmId(event.getAlarmId());
		compress.setAlarmlevel(event.getAlarmlevel());
		compress.setCollecttime(event.getCollecttime());
		compress.setContent(event.getContent());
		compress.setFirsttime(event.getCollecttime());	//
		compress.setName(event.getName());
		compress.setNodeId(event.getNodeId());
		compress.setSindex(event.getSindex());
		compress.setSubtype(event.getSubtype());
		compress.setType(event.getType());
		
		return compress;
	}

    /**
     * 已过时
     * 指标变更判断工具类
     * 
     * @param node
     *            被监控的设备
     * @param target
     *            告警指标
     * @param valueObj
     *            指标值集合或者字符串
     */
    @Deprecated
    public synchronized void hardwareInfo(Host node, String target,
            Object valueObj) {}

    public static void main(String[] args) {
    }
}
