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
     * δʹ�ã��ѹ�ʱ
     */
    @Deprecated
    public void updateData(Object vo, Object collectingData, String type,
            String subtype, AlarmIndicatorsNode alarmIndicatorsNode) {}

    /**
     * δʹ�ã��ѹ�ʱ
     */
    @Deprecated
    public void updatePortData(Object vo, Object collectingData, List list) {}

    /**
     * δʹ�ã��ѹ�ʱ
     */
    @Deprecated
    public void checkMiddlewareEvent(BaseVo node, AlarmIndicatorsNode nm,
         String value) {}


    /**
     * ����Ƿ�Ϊ�澯
     * <p>
     * �÷����Ժ������Ϊһ��ͨ�õķ��� ������Ƿ�Ϊһ���澯������ ����Ϊ�ɼ���ͳһ��� �÷���������
     * </p>
     * 
     * @see checkEvent(Host node,AlarmIndicatorsNode nm,String value, String
     *      sIndex)</a>
     *      <p>
     * @param node
     *            �豸
     * @param nm
     *            ָ��
     * @param pingvalue
     *            ֵ
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
     * ����Ƿ�Ϊ�澯
     * <p>
     * �÷����Ժ������Ϊһ��ͨ�õķ��� ������Ƿ�Ϊһ���澯������ ����Ϊ�ɼ���ͳһ��� �÷���������
     * </p>
     * 
     * @see checkEvent(Host node,AlarmIndicatorsNode nm,String value, String
     *      sIndex)</a>
     *      <p>
     * @param node
     *            �豸
     * @param nm
     *            ָ��
     * @param pingvalue
     *            ֵ
     */
    public void checkEvent(Node node, AlarmIndicatorsNode nm, String value) {
        NodeDTO nodeDTO = null;
        NodeUtil nodeUtil = new NodeUtil();
        nodeDTO = nodeUtil.conversionToNodeDTO(node);
        checkEvent(nodeDTO, nm, value, "");
        return;
    }

    /**
     * ����Ƿ�Ϊ�澯
     * <p>
     * �÷����Ժ������Ϊһ��ͨ�õķ��� ������Ƿ�Ϊһ���澯������ ����Ϊ�ɼ���ͳһ���
     * <p>
     * 
     * @param node
     *            �豸
     * @param nm
     *            ָ��
     * @param value
     *            ֵ
     * @param sIndex
     *            ���ֵʱ����Ϊ��־����
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
        int alarmLevel = 0; // �澯�ȼ�
        // ���ڴ�����صĸ澯���,����ʵ�ִ����ݿ����ɾ���������
        CheckEvent lastCheckEvent = deleteEvent(nm, sIndex);
        if (nm.getEnabled().equalsIgnoreCase("0")) {
            // �澯ָ��δ��� �����κ����� ����
            return;
        }
        if (!AlarmConstant.DATATYPE_NUMBER.equals(nm.getDatatype())) {
            // ���������͵ķ���
            return;
        }
        if (value == null || value.trim().length() == 0) {
            // δ�ɼ�ֵ �����κ��� ֱ�ӷ���
            return;
        }
        // �ж��Ƿ��͸澯 ������� >0 ����
        try {
            alarmLevel = checkAlarm(node, nm, Double.valueOf(value), sIndex);         
        } catch (Exception e) {
            SysLogger.error("�ж��Ƿ��͸澯����", e);
        }
        if (alarmLevel > 0) {
            // ��Ҫ���͸澯����
            try {
                sendAlarm(node, nm, value, alarmLevel, sIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // δ�����澯��ֵ ��ɾ���澯���͵�ʱ��ļ�¼
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

            // �ж�֮ǰ�Ƿ��и澯,�������͸澯�ָ���Ϣ
            if (lastCheckEvent != null) {

            }
        }
    }

    /**
     * �����̸澯��Ϣ
     * 
     * @param node
     * @param diskVector
     * @param nm
     */
    public void checkDisk(Host node, Vector diskVector, AlarmIndicatorsNode nm) {
        // �� diskVector ������Ϣ
        if ("0".equals(nm.getEnabled())) {
            // �澯ָ��δ��� �����κ����� ����
            return;
        }
        if (diskVector == null || diskVector.size() == 0) {
            // δ�ɼ������� �����κ����� ����
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
                // ������
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
                            + ":" + "��������ֵ");
                } else
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity()
                            + ":"
                            + "��������ֵ");
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
                // ������
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
                            + ":" + "��������ֵ");
                } else
                    diskconfig = (Diskconfig) alldiskalarmdata.get(node
                            .getIpAddress()
                            + ":"
                            + diskcollectdata.getSubentity()
                            + ":"
                            + "��������ֵ");
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
     * <p>��Ϊ Imanager2000 �� Trap �澯
     *
     * @param   node
     *          - {@link ApplicationNode}
     * @param   trap
     *          - Trap ��Ϣ
     * @param   nm
     *          - �澯ָ��
     *
     * @since   v1.01
     */
    public void checkImanagerTrapEvent(ApplicationNode node, IManagerTrap trap, AlarmIndicatorsNode nm) {
    	 deleteEvent(nm);
         if (nm.getEnabled().equalsIgnoreCase("0")) {
             // �澯ָ��δ��� �����κ����� ����
             return;
         }
         if (!AlarmConstant.DATATYPE_NUMBER.equals(nm.getDatatype())) {
             // ���������͵ķ���
             return;
         }
         if (trap == null || trap.getName().trim().length() == 0) {
             // δ�ɼ�ֵ �����κ��� ֱ�ӷ���
             return;
         }
         if ("Recovery".equals(trap.getAlarmFlag())) {
        	 // �澯�ָ���Ϣ�����账��
        	 return;
         }
         
         // �澯�ȼ�Ϊ�� Warning  Critical 3  Minor 1  Major 2
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
         content.append("SDH�澯��"+trap.getName());
         content.append("[" + trap.getDeviceType() + "]");
         // ������Ϣ��ֻ��ȡ�ۺźͶ˿ں���Ϣ
         String elementInstance = trap.getElementInstance();
         String[] elements = elementInstance.split(" ");
         content.append(" ������Ϣ��" + elements[1] + " " + elements[2]);
         elements = trap.getAlarmInfo().split(",");
         content.append(" " + elements[2]);
         elements = trap.getReasonOfCausingAlarm().split(",");
         content.append(" ԭ��" + elements[2]);

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
     * ������ٸ澯
     * 
     * @param node
     * @param diskVector
     * @param nm
     */
    public void checkUtilhdxEvent(NodeDTO node, Vector utilhdxVector, AlarmIndicatorsNode nm) {
        // �� utilhdxVector ������Ϣ
        if (nm == null || "0".equals(nm.getEnabled())) {
            // �澯ָ��δ��� �����κ����� ����
            return;
        }
        if (utilhdxVector == null || utilhdxVector.size() == 0) {
            // δ�ɼ������� �����κ����� ����
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
     * �������������澯 nielin add
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
                            // ��ԭ������ȣ���Ϊ"����"�Ϳ���Ϊ"��"��
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
                            // ����ĸ���������������Ϊ0
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
                            // ��ʧ�ĸ�������������������Ϊ1
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
                    String message = ip + " ������Ϊ��" + processGroup.getName()
                            + " ���ֽ����쳣!";
                    for (int j = 0; j < wrongList.size(); j++) {
                        List wrongProList = (List) wrongList.get(j);
                        String status = (String) wrongProList.get(2);
                        if ("1".equals(status)) {
                            message = message + "���̣�" + wrongProList.get(0)
                                    + "��������Ϊ��" + wrongProList.get(1) + ";";
                        } else {
                            message = message + "���̣�" + wrongProList.get(0)
                                    + "��ʧ����Ϊ��" + wrongProList.get(1) + ";";
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
    	List alarmArpList = new ArrayList();	//�澯�쳣ARP�б�
    	
    	if(arpVector == null || arpVector.size() == 0) {
    		return returnList;
    	}
    	
    	try {
    		//���ɼ��������������Ա�
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
					//���������治��������澯
					alarmArpList.add(arpVector.get(i));
				} 
			}
    		
    		deleteEvent(alarmIndicatorsNode);
    		if(alarmArpList.size() > 0) {
    			for (int i = 0; i < alarmArpList.size(); i++) {
    				IpMac macVo = (IpMac) alarmArpList.get(i);
    				
    				StringBuffer message = new StringBuffer();
    				message.append("IPMAC �����쳣IP��");
    				message.append(macVo.getIpaddress());
    				
    				String alarmId = String.valueOf(alarmIndicatorsNode.getId());
                    String name = alarmIndicatorsNode.getName();
                    String nodeId = alarmIndicatorsNode.getNodeid();
                    String type = alarmIndicatorsNode.getType();
                    String subtype = alarmIndicatorsNode.getSubtype();
                    int alarmLevel = 1;	//��ͨ�澯
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
     * �������������澯
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

                // �и澯�ĺ����������б�
                List blackWrongList = new ArrayList();

                for (int j = 0; j < hostServiceList.size(); j++) {
                    HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) hostServiceList
                            .get(j);
                    String status = hostServiceGroupConfiguration.getStatus();// 1:�����������
                                                                                // 0:�������������
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

                    if (!isLived && "0".equals(status)) {// ���Ӱ������ж����� (�����ڸý���
                                                            // ��澯)
                        wrongList.add(hostServiceGroupConfiguration);
                    }
                    if (isLived && "1".equals(status)) {// ���Ӻ������ж����� (���ڸý�����澯)
                        blackWrongList.add(hostServiceGroupConfiguration);
                    }
                }
                StringBuffer message = new StringBuffer();
                message.append(ip);
                message.append(" ����������Ϊ��");
                message.append(hostServiceGroup.getName());
                message.append("������������澯! ");
                if (wrongList.size() > 0) {
                    for (int j = 0; j < wrongList.size(); j++) {
                        HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) wrongList
                                .get(j);
                        message.append("��������");
                        message.append(hostServiceGroupConfiguration.getName());
                        message.append("��ʧ��; ");
                    }
                }
                if (blackWrongList.size() > 0) {
                    for (int j = 0; j < blackWrongList.size(); j++) {
                        HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) blackWrongList
                                .get(j);
                        message.append("��������");
                        message.append(hostServiceGroupConfiguration.getName());
                        message.append("��������; ");
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
     * ����as400������澯
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
                            // ��� ��ҵ���� ���� ��ҵ�ļ��״̬Ϊ��������� ��澯
                            perVector.add(jobForAS400GroupDetail);
                            perVector.add("��ҵ��"
                                    + jobForAS400GroupDetail.getName()
                                    + " ���ֻ,�Ҹ���Ϊ��" + jobForAS400List2.size()
                                    + ";");
                        } else if (jobForAS400GroupDetail.getStatus().equals(
                                "1")
                                && !isLived) {
                            // ��� ��ҵδ���� ���� ��ҵ�ļ��״̬Ϊ������� ��澯
                            perVector.add(jobForAS400GroupDetail);
                            perVector.add("��ҵ��"
                                    + jobForAS400GroupDetail.getName()
                                    + " δ�;");
                        } else if (!jobForAS400GroupDetail.getStatus().equals(
                                "0")
                                && isLived) {
                            // ��� ��ҵ���� ���� ��ҵ�ļ��״̬Ϊ������� ���һ���ж�
                            if (!"-1".equals(jobForAS400GroupDetail
                                    .getActiveStatusType())) {
                                // ��� ��ҵ�Ļ�ļ��״̬���ǲ��� ������ж�

                                try {
                                    int num = Integer
                                            .valueOf(jobForAS400GroupDetail
                                                    .getNum());
                                    if (num > jobForAS400List2.size()) {
                                        eventMessage = "��ҵ��"
                                                + jobForAS400GroupDetail
                                                        .getName()
                                                + " �����쳣,�������ڼ����Ŀ,��ʧ��"
                                                + (num - jobForAS400List2
                                                        .size()) + "��";
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
                                        // �ж�ÿһ�����ֵ���ҵ
                                        if ("1".equals(jobForAS400GroupDetail
                                                .getActiveStatusType()) != (activeStatus
                                                .indexOf(jobForAS400
                                                        .getActiveStatus()) != -1)) {
                                            // ��� ��ҵ�״̬����Ϊ������� �� �״̬�����ڵ�ǰ���״̬��
                                            // ��������Գ����쳣
                                            // ��� ��ҵ�״̬����Ϊ��������� ��
                                            // �״̬���ܳ����ڵ�ǰ���״̬�� �� ������� �����쳣
                                            eventMessage = eventMessage
                                                    + "��ҵ��"
                                                    + jobForAS400GroupDetail
                                                            .getName()
                                                    + " �����쳣״̬Ϊ; ��״̬Ϊ��"
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
                        String message = ip + " ����ҵ�飺"
                                + jobForAS400Group.getName() + " �����쳣!";
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
     * ����as400������澯
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
                        	// δ�����澯��ֵ ��ɾ���澯���͵�ʱ��ļ�¼
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
                    if ("��".equals(unit)) {
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
                                        + sIndexAndDescr.get(sIndex) + " " + alarmIndicatorsNode.getAlarm_info() + " ��ǰֵ:"
                                        + value + " " + unit + " ��ֵ:" + threshold + " " + unit;
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
     * ����SNMPδ�����ĸ澯
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
             if ("��".equals(unit)) {
                 unit = "";
             }
             String threshold = "up";
             if(value.trim().length()>0){
                 threshold = "����";
                 // δ�����澯��ֵ ��ɾ���澯���͵�ʱ��ļ�¼
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
                 threshold = "�ر�";
             }

             String alarmId = String.valueOf(alarmIndicatorsNode.getId());
             String name = alarmIndicatorsNode.getName();
             String nodeId = alarmIndicatorsNode.getNodeid();
             String type = alarmIndicatorsNode.getType();
             String subtype = alarmIndicatorsNode.getSubtype();
             String sIndex = null;
             int alarmLevel = 3;
             String content = node.getName() + "(IP: " + node.getIpaddress() + ") "
                                 + " " + alarmIndicatorsNode.getAlarm_info() + " ��ǰֵ:"
                                 + " �ر�" + unit + " ��ֵ:" + threshold + " " + unit;
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
     * ȷ���Ƿ�Ϊ�澯
     * <p>
     * �������¼��Ĵ������ڹ涨�Ĵ��� ����Ϊ�澯�������ظ澯�ĵȼ� ������澯�򷵻�
     * <p>
     * 
     * @param node
     *            �豸
     * @param nm
     *            ָ��
     * @param value
     *            ֵ
     * @param name
     *            �澯Ψһ��־
     * @return
     */
    private int checkAlarm(NodeDTO node, AlarmIndicatorsNode nm, double value,
            String sIndex) {
         //SysLogger.info("===========��ʼȷ���Ƿ�Ϊ�澯=============");
        int alarmLevel = 0;
        int eventLevel = 0; // �¼��ȼ�
        int eventTimes = 0; // �¼�����
        double limenvalue0 = Double.parseDouble(nm.getLimenvalue0());// һ����ֵ
        double limenvalue1 = Double.parseDouble(nm.getLimenvalue1());// ������ֵ
        double limenvalue2 = Double.parseDouble(nm.getLimenvalue2());// ������ֵ
        // ����¼��ȼ�
        eventLevel = checkEventLevel(value, limenvalue0, limenvalue1,
                limenvalue2, nm.getCompare());
        // SysLogger.info("========����¼��ȼ�===========" + eventLevel);
        // ����¼�����
        // ���������¼��ȼ�Ϊ 0 ��˵���¼��ָ� �������� �¼�����
        // ������ش������� 0 ��˵�����ڹ涨���¼����� ���¼�����Ϊ�澯 ������ش��������� 0 ��ֻ�ǽ��¼����� + 1;;
        eventTimes = checkEventTimes(nm, eventLevel, sIndex);
        // SysLogger.info("======== ����¼����� ===========" + eventTimes);
        if (eventTimes > 0) {
            // ������� 0 ���¼��ȼ����� Ϊ �澯�ȼ�
            alarmLevel = eventLevel;
        }
        return alarmLevel;

    }

    /**
     * ����澯����
     * 
     * @param node
     *            �豸
     * @param alarmIndicatorsNode
     *            ָ��
     * @param value
     *            �ɼ�����ֵ
     * @param alarmLevel
     *            �澯�ȼ�
     */
    public void sendAlarm(NodeDTO node,
            AlarmIndicatorsNode alarmIndicatorsNode, String value,
            int alarmLevel) {
        sendAlarm(node, alarmIndicatorsNode, value, alarmLevel, "");
    }

    /**
     * ����澯����
     * 
     * @param node
     *            �豸
     * @param alarmIndicatorsNode
     *            ָ��
     * @param value
     *            �ɼ�����ֵ
     * @param alarmLevel
     *            �澯�ȼ�
     */
    public void sendAlarm(NodeDTO node,
            AlarmIndicatorsNode alarmIndicatorsNode, String value,
            int alarmLevel, String sIndex) {
        // ���ø澯�Ա���Ϣ�ŵ��ڴ�ϵͳ��,������Ҫ�ŵ����ݿ����
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
                            + contentSindex + " " + alarmIndicatorsNode.getAlarm_info() + " ��ǰֵ:"
                            + value + " " + unit + " ��ֵ:" + threshold + " " + unit;
        String collecttime = sdf.format(new Date());
        CheckEvent checkEvent = createCheckEvent(alarmId, name, nodeId, type, subtype, sIndex, alarmLevel, content, collecttime);
        
        sendAlarm(node, checkEvent, alarmIndicatorsNode);
    }

    /**
     * sendAlarm:
     * <p>�����ո澯����ͬ�������������� #sendAlarm(CheckEvent, EventList, AlarmIndicatorsNode)
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
     * <p>���ո澯����
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
     * �����澯��
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
     * �����澯��
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
        // �����¼�
        EventList eventlist = new EventList();
        eventlist.setEventtype(eventtype);
        eventlist.setEventlocation(eventlocation);
        eventlist.setContent(content);
        eventlist.setLevel1(level1);
        eventlist.setManagesign(0);
        eventlist.setBak("");
        eventlist.setRecordtime(Calendar.getInstance());
        eventlist.setReportman("ϵͳ��ѯ");
        eventlist.setBusinessid(bid);
        eventlist.setNodeid(Integer.parseInt(objid));
        eventlist.setOid(0);
        eventlist.setSubtype(subtype);
        eventlist.setSubentity(subentity);
        return eventlist;
    }

    /**
     * 
     * ����¼��ȼ�
     * 
     * <p>
     * compare_type �ȽϷ�ʽ��1 Ϊ���� �����ڱȽϣ�0 Ϊ���� ��С�ڱȽ�
     * </p>
     * <p>
     * ���ظ澯�ȼ� ������澯�򷵻� 0
     * </p>
     * 
     * @author nielin
     * @param value
     *            ֵ
     * @param limenvalue0
     *            һ����ֵ
     * @param limenvalue1
     *            ������ֵ
     * @param limenvalue2
     *            ������ֵ
     * @param compare_type
     *            �ȽϷ�ʽ
     * 
     * @return level
     */
    private int checkEventLevel(double value, double limenvalue0,
            double limenvalue1, double limenvalue2, int compare_type) {
        int level = 0; // ��Ҫ���صĵȼ�
        // SysLogger.info(value + "=======" + compare_type);
        if (compare_type == 0) {
            // ����Ƚ�
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
            // ����Ƚ�
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
     * ���澯����
     * <p>
     * ������ڹ涨�ĸ澯����������һ������ 0���� ���򷵻� 0 ����ͬʱ���澯���� + 1
     * <p>;
     * 
     * @param alarmIndicatorsNode
     *            �澯ָ��
     * @param alarmLevel
     *            ��ǰ�澯�ȼ�
     * @return
     */
    private int checkEventTimes(AlarmIndicatorsNode alarmIndicatorsNode,
            int eventLevel, String sIndex) {
        int eventTimes = 0; // �����¼��Ĵ���
        int defineTimes = 0; // ��������������¼�����
        int lastEventTimes = 0; // ֮ǰ�������¼�����
        String name = "";
        if (sIndex!= null && sIndex.trim().length() > 0) {
            name = alarmIndicatorsNode.getId() + ":" + sIndex;
        } else {
            name = alarmIndicatorsNode.getId() + "";
        }
        if (eventLevel == 0) {
            // ����¼��ȼ� Ϊ 0 ˵���¼��ָ� ������¼�������Ϊ 0
            setEventTimes(name, 0);
            return eventTimes;
        }

        defineTimes = getTimesByLevel(alarmIndicatorsNode, eventLevel);
        lastEventTimes = getEventTimes(name, eventLevel);
        eventTimes = lastEventTimes + 1; // �ϴμ���� Ȼ�󱣴�
        setEventTimes(name, eventTimes);
        if (eventTimes < defineTimes) {
            // ���С�ڶ���Ĵ����򲻲����澯 ���� 0
            eventTimes = 0;
        }
        return eventTimes;
    }

    /**
     * �õ�֮ǰ�ĸ澯����
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
     * ���ø澯����
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
     * ����ָ��͸澯�ȼ����ط�ֵ
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
     * ����ָ��͸澯�ȼ����ض���ĸ澯����
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
     * �� ���ݿ��� ɾ���ϴεĸ澯 ����������ҳ��� ��ɾ�������� �� �����������null
     * <p>
     * ���ڴ�����صĸ澯���,����ʵ�ִ����ݿ����ɾ���������
     * <p>
     * 
     * @param name
     * @return CheckEvent
     */
    private CheckEvent deleteEvent(AlarmIndicatorsNode alarmIndicatorsNode) {
        return deleteEvent(alarmIndicatorsNode, null);
    }

    /**
     * �� ���ݿ��� ɾ���ϴεĸ澯 ����������ҳ��� ��ɾ�������� �� �����������null
     * <p>
     * ���ڴ�����صĸ澯���,����ʵ�ִ����ݿ����ɾ���������
     * <p>
     * 
     * @param name
     * @return CheckEvent
     */
    private CheckEvent deleteEvent(AlarmIndicatorsNode alarmIndicatorsNode, String sIndex) {
       
    	// ɾ��ѹ����澯��Ϣ
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
     * ɾ���澯ѹ�����е�ָ����Ϣ
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
     * �����θ澯��Ϣ���浽���ݿ���
     * <p>
     * ���ø澯�Ա���Ϣ�ŵ��ڴ�ϵͳ��,������Ҫ�ŵ����ݿ����
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
     * ���澯��Ϣ�����ڸ澯ѹ������ - nms_checkevent_compress
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
	 * CheckEventתΪCheckEventCompress
	 * 	
	 * 	firsttime�ֶ���collecttime��ͬ
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
     * �ѹ�ʱ
     * ָ�����жϹ�����
     * 
     * @param node
     *            ����ص��豸
     * @param target
     *            �澯ָ��
     * @param valueObj
     *            ָ��ֵ���ϻ����ַ���
     */
    @Deprecated
    public synchronized void hardwareInfo(Host node, String target,
            Object valueObj) {}

    public static void main(String[] args) {
    }
}
