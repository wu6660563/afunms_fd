package com.afunms.polling.snmp.solaris;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * Solaris Memory 日志解析类
 * 
 * @author 聂林
 */
public class SolarisMemoryByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisMemoryByLogFile.class.getName());

    private static final String SOLARIS_MEMORYPERF_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MEMORYPERF_BEGIN_KEYWORD;

    private static final String SOLARIS_MEMORYPERF_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MEMORYPERF_END_KEYWORD;

    private static final String SOLARIS_MEMORY_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MEMORY_BEGIN_KEYWORD;

    private static final String SOLARIS_MEMORY_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MEMORY_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_MEMORYPERF_BEGIN_KEYWORD;
        String endStr = SOLARIS_MEMORYPERF_END_KEYWORD;
        String memoryperfContent = getLogFileContent(beginStr, endStr);
        
        beginStr = SOLARIS_MEMORY_BEGIN_KEYWORD;
        endStr = SOLARIS_MEMORY_END_KEYWORD;
        String memoryContent = getLogFileContent(beginStr, endStr);
        
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] memoryperfLineArr = memoryperfContent.split("\n");
        String[] memoryLineArr = memoryContent.split("\n");

        int freePhysicalMemory = 0;
        List<Hashtable<String, String>> memperflist = new ArrayList<Hashtable<String,String>>();
        try {
            for (int i = 0; i < memoryperfLineArr.length; i++) {
                int j = memoryperfLineArr.length;
                String[] tmpData =  memoryperfLineArr[j - 1].trim().split("\\s++");
                if (tmpData != null && tmpData.length >= 22) {
                    if (tmpData[0] != null
                                    && !tmpData[0].equalsIgnoreCase("r")) {
                        freePhysicalMemory = Integer.parseInt(tmpData[4]) * 4 / 1024 / 1024;
                    }
                    Hashtable<String, String> memperfhash = new Hashtable<String, String>();
                    memperfhash.put("free", String.valueOf(freePhysicalMemory));
                    memperflist.add(memperfhash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        float PhysicalMemCap = 0f;
        try {
            for (int i = 0; i < memoryLineArr.length; i++) {
                if (memoryLineArr[i].trim().contains(":")) {
                    String[] tmpData = memoryLineArr[i].trim().split(":");
                    if (tmpData[0].trim().equalsIgnoreCase("Memory size")) {
                        String allphy = tmpData[1].trim().trim();
                        allphy = allphy.replaceAll("Megabytes", "");
                        PhysicalMemCap = Float.parseFloat(allphy);
                    }
                }
    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PhysicalMemCap > 0) {
            float PhysicalMemUtilization = (PhysicalMemCap - freePhysicalMemory)
                            * 100 / PhysicalMemCap;

            // 物理内存大小
            Memorycollectdata memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Capability");
            memorydata.setSubentity("PhysicalMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(Float.toString(PhysicalMemCap));
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
            memorydata.setThevalue(Float.toString(PhysicalMemCap
                            - freePhysicalMemory));
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
            memorydata.setThevalue(Math.round(PhysicalMemUtilization) + "");
            memoryVector.addElement(memorydata);
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(memoryVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
