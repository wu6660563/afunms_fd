package com.afunms.polling.snmp.linux;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.CPUcollectdata;

/**
 * Linux CPU 日志解析类
 * 
 * @author 聂林
 */
public class LinuxCpuByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxCpuByLogFile.class.getName());

    private static final String LINUX_CPU_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPU_BEGIN_KEYWORD;

    private static final String LINUX_CPU_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPU_END_KEYWORD;

    private static DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_CPU_BEGIN_KEYWORD;
        String endStr = LINUX_CPU_END_KEYWORD;
        String cpuContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] cpuLineArr = cpuContent.split("\n");

        List<Hashtable<String, String>> cpuperflist = new ArrayList<Hashtable<String, String>>();
        Hashtable<String, String> cpuperfhash = new Hashtable<String, String>();

        Vector<CPUcollectdata> cpuVector = new Vector<CPUcollectdata>();
        for (int i = 0; i < cpuLineArr.length; i++) {
            String[] tmpData = cpuLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length >= 5) {
                if ("Average:".equalsIgnoreCase(tmpData[0].trim())
                        || "平均时间:".equalsIgnoreCase(tmpData[0].trim())) {
                    String user = tmpData[2].trim();
                    String sys = tmpData[4].trim();
                    String wio = tmpData[5].trim();
                    String idle = tmpData[7].trim();

                    String used = String.valueOf(df.format(100.0 - Double.parseDouble(idle)));

                    cpuperfhash.put("%usr", user);
                    cpuperfhash.put("%sys", sys);
                    cpuperfhash.put("%wio", wio);
                    cpuperfhash.put("%idle", wio);
                    cpuperflist.add(cpuperfhash);

                    CPUcollectdata cpudata = new CPUcollectdata();
                    cpudata.setIpaddress(ipaddress);
                    cpudata.setCollecttime(date);
                    cpudata.setCategory("CPU");
                    cpudata.setEntity("Utilization");
                    cpudata.setSubentity("Utilization");
                    cpudata.setRestype("dynamic");
                    cpudata.setUnit("%");
                    cpudata.setThevalue(used);
                    cpuVector.addElement(cpudata);

                }
            }
        }
        Hashtable<String, Object> cpuHashtable = new Hashtable<String, Object>();
        cpuHashtable.put("cpuperflist", cpuperflist);
        cpuHashtable.put("cpuVector", cpuVector);
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(cpuHashtable);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
