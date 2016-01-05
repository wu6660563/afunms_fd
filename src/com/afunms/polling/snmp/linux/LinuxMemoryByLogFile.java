package com.afunms.polling.snmp.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;

/**
 * Linux Memory 日志解析类
 * 
 * @author 聂林
 */
public class LinuxMemoryByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxMemoryByLogFile.class.getName());

    private static final String LINUX_MEMORY_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MEMORY_BEGIN_KEYWORD;

    private static final String LINUX_MEMORY_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MEMORY_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_MEMORY_BEGIN_KEYWORD;
        String endStr = LINUX_MEMORY_END_KEYWORD;
        String memoryContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] memoryLineArr = memoryContent.split("\n");

        Hashtable<String, Vector<Memorycollectdata>> collectHash = new Hashtable<String, Vector<Memorycollectdata>>();

        List<Hashtable<String, String>> memperflist = new ArrayList<Hashtable<String, String>>();
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        for (int i = 0; i < memoryLineArr.length; i++) {
            String[] tmpData = memoryLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length >= 4) {
                if (tmpData[0].trim().equalsIgnoreCase("Mem:")) {
                    Hashtable<String, String> memperfhash = new Hashtable<String, String>();
                    String total = tmpData[1].trim();
                    String used = tmpData[2].trim();
                    String free = tmpData[3].trim();
                    String shared = tmpData[4].trim();
                    String buffers = tmpData[5].trim();
                    String cached = tmpData[6].trim();
                    memperfhash.put("total", total);
                    memperfhash.put("used", used);
                    memperfhash.put("free", free);
                    memperfhash.put("shared", shared);
                    memperfhash.put("buffers", buffers);
                    memperfhash.put("cached", cached);
                    memperflist.add(memperfhash);
                    
                    // 内存总大小 浮点型
                    Float totalFloat = Float.parseFloat(total);
                    // 内存使用大小 浮点型
                    Float usedFloat = Float.parseFloat(used) - Float.parseFloat(buffers) - Float.parseFloat(cached);
                    // 空闲内存大小 浮点型
                    Float freeFloat = totalFloat - usedFloat;
                    // Memory
                    float PhysicalMemUtilization = 100 - freeFloat * 100 / totalFloat;
                    // 内存总大小 整数型 (转换成 M 为单位)
                    int totalInt = Math.round(totalFloat / 1024);
                    // 内存使用大小 整数型 (转换成 M 为单位)
                    int usedInt = Math.round(usedFloat / 1024);

                    // 物理内存总大小
                    Memorycollectdata memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("Capability");
                    memorydata.setSubentity("PhysicalMemory");
                    memorydata.setRestype("static");
                    memorydata.setUnit("M");
                    memorydata.setThevalue(String.valueOf(totalInt));
                    memoryVector.addElement(memorydata);

                    // 物理内存使用大小
                    memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("UsedSize");
                    memorydata.setSubentity("PhysicalMemory");
                    memorydata.setRestype("static");
                    memorydata.setUnit("M");
                    memorydata.setThevalue(String.valueOf(usedInt));
                    memoryVector.addElement(memorydata);

                    // 物理内存使用率
                    memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("Utilization");
                    memorydata.setSubentity("PhysicalMemory");
                    memorydata.setRestype("dynamic");
                    memorydata.setUnit("%");
                    memorydata.setThevalue(String.valueOf(Math.round(PhysicalMemUtilization)));
                    memoryVector.addElement(memorydata);
                } else if (tmpData[0].trim().equalsIgnoreCase("Swap:")) {
                    // Swap 虚拟内存
                    String total = tmpData[1].trim();
                    String used = tmpData[2].trim();
                    String free = tmpData[3].trim();
                    
                    Hashtable<String, String> memperfhash = new Hashtable<String, String>();
                    memperfhash.put("total", tmpData[1].trim());
                    memperfhash.put("used", tmpData[2].trim());
                    memperfhash.put("free", tmpData[3].trim());
                    memperflist.add(memperfhash);

                    // 内存总大小 浮点型
                    Float totalFloat = Float.parseFloat(total);
                    // 内存使用大小 浮点型
                    Float usedFloat = Float.parseFloat(used);
                    // Memory
                    float SwapMemUtilization = usedFloat / totalFloat;
                    // 内存总大小 整数型 (转换成 M 为单位)
                    int totalInt = Math.round(totalFloat / 1024);
                    // 内存使用大小 整数型 (转换成 M 为单位)
                    int usedInt = Math.round(usedFloat / 1024);

                    Memorycollectdata memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("Capability");
                    memorydata.setSubentity("SwapMemory");
                    memorydata.setRestype("static");
                    memorydata.setUnit("M");
                    memorydata.setThevalue(String.valueOf(totalInt));
                    memoryVector.addElement(memorydata);

                    memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("UsedSize");
                    memorydata.setSubentity("SwapMemory");
                    memorydata.setRestype("static");
                    memorydata.setUnit("M");
                    memorydata.setThevalue(String.valueOf(usedInt));
                    memoryVector.addElement(memorydata);

                    memorydata = new Memorycollectdata();
                    memorydata.setIpaddress(ipaddress);
                    memorydata.setCollecttime(date);
                    memorydata.setCategory("Memory");
                    memorydata.setEntity("Utilization");
                    memorydata.setSubentity("SwapMemory");
                    memorydata.setRestype("dynamic");
                    memorydata.setUnit("%");
                    memorydata.setThevalue(String.valueOf(Math.round(SwapMemUtilization)));
                    memoryVector.addElement(memorydata);
                }
            }
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(memoryVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
