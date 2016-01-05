/*
 * @(#)Indicators.java     v1.01, 2013 11 8
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com;

import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.config.dao.DiskconfigDao;
import com.afunms.config.model.Diskconfig;
import com.afunms.detail.reomte.model.DiskInfo;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.report.export.ExportInterface;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/**
 * ClassName:   Indicators.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 11 8 15:02:32
 */
public class Indicators {

    public void create() {
        List<HostNode> list = null;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            list = hostNodeDao.loadAll();
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        ExportInterface exportInterface = new Excel(ResourceCenter.getInstance().getSysPath() + "/WEB-INF/" + "告警阀值统计表.xls");
        NodeUtil nodeUtil = new NodeUtil();
        ArrayList<String[]> tableal = new ArrayList<String[]>();
        int i = 0;
        for (HostNode hostNode : list) {
            NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(hostNode);
            String nodeid = nodeDTO.getNodeid();
            String type = nodeDTO.getType();
            String subtype = nodeDTO.getSubtype();
            List<AlarmIndicatorsNode> list2 = null;
            AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
            try {
                list2 = alarmIndicatorsNodeDao.getByNodeIdAndTypeAndSubType(nodeid, type, subtype);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                alarmIndicatorsNodeDao.close();
            }
            for (AlarmIndicatorsNode alarmIndicatorsNode : list2) {
                String[] strings = new String[18];
                strings[0] = String.valueOf(i++);
                strings[1] = nodeDTO.getIpaddress();
                strings[2] = nodeDTO.getName();
                strings[3] = nodeDTO.getType();
                strings[4] = nodeDTO.getSubtype();
                strings[5] = alarmIndicatorsNode.getName();
                strings[6] = alarmIndicatorsNode.getDescr();
                strings[7] = alarmIndicatorsNode.getThreshlod_unit();
                strings[8] = String.valueOf(alarmIndicatorsNode.getCompare() == 1 ? "升序" : "降序");
                strings[9] = alarmIndicatorsNode.getEnabled();
                strings[10] = alarmIndicatorsNode.getLimenvalue0();
                strings[11] = alarmIndicatorsNode.getTime0();
                strings[12] = alarmIndicatorsNode.getLimenvalue1();
                strings[13] = alarmIndicatorsNode.getTime1();
                strings[14] = alarmIndicatorsNode.getLimenvalue2();
                strings[15] = alarmIndicatorsNode.getTime2();
                
                strings[17] = "否";
                if ("cpu".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    CpuInfoService cpuInfoService = new CpuInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<CPUcollectdata> cpu = cpuInfoService.getCpuInfo();
                    for (CPUcollectdata  nodeTemp: cpu) {
                        strings[16] = nodeTemp.getThevalue();
                    }
                } else if ("ping".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    PingInfoService pingInfoService = new PingInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<Pingcollectdata> ping = pingInfoService.getPingInfo();
                    for (Pingcollectdata  nodeTemp: ping) {
                        strings[16] = nodeTemp.getThevalue();
                    }
                } else if ("memory".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    MemoryInfoService memoryInfoService = new MemoryInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<Memorycollectdata> memory = memoryInfoService.getMemoryInfo();
                    for (Memorycollectdata nodeTemp : memory) {
                        System.out.println(nodeTemp.getThevalue());
                        if (nodeTemp.getSubentity().equals("avg")) {
                            strings[16] = nodeTemp.getThevalue();
                        }
                    }
                } else if ("physicalmemory".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    MemoryInfoService memoryInfoService = new MemoryInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<Memorycollectdata> memory = memoryInfoService.getMemoryInfo();
                    for (Memorycollectdata nodeTemp : memory) {
                        System.out.println(nodeTemp.getThevalue());
                        if (nodeTemp.getSubentity().equals("PhysicalMemory")) {
                            strings[16] = nodeTemp.getThevalue();
                        }
                        
                    }
                } else if ("virtualmemory".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    MemoryInfoService memoryInfoService = new MemoryInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<Memorycollectdata> memory = memoryInfoService.getMemoryInfo();
                    for (Memorycollectdata nodeTemp : memory) {
                        System.out.println(nodeTemp.getThevalue());
                        if (nodeTemp.getSubentity().equals("VirtualMemory") || nodeTemp.getSubentity().equals("SwapMemory")) {
                            strings[16] = nodeTemp.getThevalue();
                        }
                    }
                } else if ("AllInBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    InterfaceInfoService interfaceInfoService = new InterfaceInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<InterfaceInfo> interfaceInfo = interfaceInfoService.getCurrAllInterfaceInfos(new String[]{"AllInBandwidthUtilHdx", "AllOutBandwidthUtilHdx"});
                    for (InterfaceInfo interfaceInfo2 : interfaceInfo) {
                        strings[16] = interfaceInfo2.getInBandwidthUtilHdx();
                    }
                } else if ("AllOutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    InterfaceInfoService interfaceInfoService = new InterfaceInfoService(alarmIndicatorsNode.getNodeid(), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
                    List<InterfaceInfo> interfaceInfo = interfaceInfoService.getCurrAllInterfaceInfos(new String[]{"AllInBandwidthUtilHdx", "AllOutBandwidthUtilHdx"});
                    for (InterfaceInfo interfaceInfo2 : interfaceInfo) {
                        strings[16] = interfaceInfo2.getOutBandwidthUtilHdx();
                    }
                } else {
                    strings[17] = "是";
                }
                
                tableal.add(strings);
                for (String string : strings) {
                    System.out.print(string + "\t" + "\t");
                }
                System.out.println();
            }
            
        }
        try {
            exportInterface.insertTable(tableal);
            exportInterface.save();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        createDisk();
    }

    public void createDisk() {
        List<HostNode> list = null;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            list = hostNodeDao.loadAll();
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        ExportInterface exportInterface = new Excel(ResourceCenter.getInstance().getSysPath() + "/WEB-INF/" + "告警磁盘阀值统计表.xls");
        NodeUtil nodeUtil = new NodeUtil();
        ArrayList<String[]> tableal = new ArrayList<String[]>();
        int i = 0;
        for (HostNode hostNode : list) {
            NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(hostNode);
            String nodeid = nodeDTO.getNodeid();
            String type = nodeDTO.getType();
            String subtype = nodeDTO.getSubtype();
            String ipaddress = nodeDTO.getIpaddress();
            List<AlarmIndicatorsNode> list2 = null;
            AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
            try {
                list2 = alarmIndicatorsNodeDao.getByNodeIdAndTypeAndSubType(nodeid, type, subtype);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                alarmIndicatorsNodeDao.close();
            }
            
            
            DiskconfigDao diskconfigDao = new DiskconfigDao();
            List<Diskconfig> diskConfigList = null;
            try {
                diskConfigList = diskconfigDao.loadByIpaddress(ipaddress);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                diskconfigDao.close();
            }
            
            DiskInfoService diskInfoService = new DiskInfoService(nodeid, type, subtype);
            List<DiskInfo> diskInfoList = diskInfoService.getCurrDiskInfo();
            for (Diskconfig diskconfig : diskConfigList) {
                if ("增长率阈值".equals(diskconfig.getBak())) {
                    continue;
                }
                String[] strings = new String[9];
                strings[0] = String.valueOf(i++);
                strings[1] = nodeDTO.getIpaddress();
                strings[2] = nodeDTO.getName();
                strings[3] = diskconfig.getName();
                strings[4] = String.valueOf(diskconfig.getLimenvalue());
                strings[5] = String.valueOf(diskconfig.getLimenvalue1());
                strings[6] = String.valueOf(diskconfig.getLimenvalue2());
                strings[7] = String.valueOf(diskconfig.getSms() == 1 ? "是" : "否");
                for (DiskInfo diskInfo : diskInfoList) {
                    if (diskInfo.getSindex().equalsIgnoreCase(diskconfig.getName())) {
                        strings[8] = diskInfo.getUtilization();
                    }
                }
                tableal.add(strings);
                for (String string : strings) {
                    System.out.print(string + "\t" + "\t");
                }
                System.out.println();
            }
        }
        try {
            exportInterface.insertTable(tableal);
            exportInterface.save();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

