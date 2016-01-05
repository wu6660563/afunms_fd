package com.afunms.polling.snmp.solaris;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Processcollectdata;

/**
 * Solaris Process 日志解析类
 * 
 * @author 聂林
 */
public class SolarisProcessByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisProcessByLogFile.class.getName());

    private static final String SOLARIS_PROCESS_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_PROCESS_BEGIN_KEYWORD;

    private static final String SOLARIS_PROCESS_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_PROCESS_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_PROCESS_BEGIN_KEYWORD;
        String endStr = SOLARIS_PROCESS_END_KEYWORD;
        String processContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] processLineArr = processContent.split("\n");
        Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
        for (int i = 1; i < processLineArr.length; i++) {
            String[] tmpData = processLineArr[i].trim().split("\\s++");
            if ((tmpData != null) && (tmpData.length == 7)) {
                if ("pid".equalsIgnoreCase(tmpData[0])) {
                    // 标题行
                    continue;
                }

                String pid = tmpData[0].trim();         // pid
                String cmd = tmpData[6].trim();         // command
                String type = "应用程序";
                String STAT = "正在运行";
                String cputime = tmpData[4].trim();     // cputime
                String memsize = tmpData[5].trim();     // memsize
                String mem = tmpData[3];                // %mem

                if ("".equals(memsize)) {
                    memsize = "0";
                }
                if (cputime.contains("-")) {
                    // cputime = "0.0";
                    String[] vbstrings = cputime.split("-"); // 3-05:56:42
                    String[] timestrs = vbstrings[1].split(":"); // 05:56:42
                    int hour = Integer.parseInt(vbstrings[0]) * 24
                                    + Integer.parseInt(timestrs[0]);// 3*24 + 5
                    timestrs[0] = hour + "";
                    StringBuffer cputimeBuffer = new StringBuffer();
                    for (int m = 0; m < timestrs.length; m++) {
                        cputimeBuffer.append(timestrs[m]);
                        if (m != timestrs.length - 1) {
                            cputimeBuffer.append(":");
                        }
                    }
                    cputime = cputimeBuffer.toString();
                }
                if (mem.contains("-")) {
                    mem = "0.0";
                }

                Processcollectdata processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("MemoryUtilization");
                processdata.setSubentity(pid);
                processdata.setRestype("dynamic");
                processdata.setUnit("%");
                processdata.setThevalue(mem);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Memory");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit("K");
                processdata.setThevalue(memsize);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Type");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(type);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Status");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(STAT);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("Name");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit(" ");
                processdata.setThevalue(cmd);
                processVector.addElement(processdata);

                processdata = new Processcollectdata();
                processdata.setIpaddress(ipaddress);
                processdata.setCollecttime(date);
                processdata.setCategory("Process");
                processdata.setEntity("CpuTime");
                processdata.setSubentity(pid);
                processdata.setRestype("static");
                processdata.setUnit("秒");
                processdata.setThevalue(cputime);
                processVector.addElement(processdata);
            }
        }

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(processVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
