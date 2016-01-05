package com.afunms.polling.snmp.solaris;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * Solaris Swap 日志解析类
 * 
 * @author 聂林
 */
public class SolarisSwapByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisSwapByLogFile.class.getName());

    private static final String SOLARIS_SWAP_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_SWAP_BEGIN_KEYWORD;

    private static final String SOLARIS_SWAP_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_SWAP_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_SWAP_BEGIN_KEYWORD;
        String endStr = SOLARIS_SWAP_END_KEYWORD;
        String swapContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] swapLineArr = swapContent.split("\n");

        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        float SwapMemCap = 0f;
        float freeSwapMemory =0f;
        float usedSwapMemory =0f;
        for (int i = 0; i < swapLineArr.length; i++) {
            String[] tmpData = swapLineArr[0].trim().split("\\s++");
            if (tmpData != null && tmpData.length == 12) {
                try {
                    String swap1 = tmpData[10].replaceAll("k", "");
                    String swap2 = tmpData[8].replaceAll("k", "");
                    freeSwapMemory = Float.parseFloat(swap1.trim());
                    usedSwapMemory = Float.parseFloat(swap2.trim());
                    SwapMemCap = freeSwapMemory + usedSwapMemory;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (SwapMemCap > 0) {
            // 交换内存总大小
            Memorycollectdata memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Capability");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(SwapMemCap / 1024 + "");
            memoryVector.addElement(memorydata);

            // 交换内存使用大小 
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("UsedSize");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("static");
            memorydata.setUnit("M");
            memorydata.setThevalue(usedSwapMemory / 1024 + "");
            memoryVector.addElement(memorydata);

            // 交换内存使用率
            memorydata = new Memorycollectdata();
            memorydata.setIpaddress(ipaddress);
            memorydata.setCollecttime(date);
            memorydata.setCategory("Memory");
            memorydata.setEntity("Utilization");
            memorydata.setSubentity("SwapMemory");
            memorydata.setRestype("dynamic");
            memorydata.setUnit("%");
            memorydata.setThevalue(Math
                            .round(usedSwapMemory * 100 / SwapMemCap) + "");
            memoryVector.addElement(memorydata);
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(memoryVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
