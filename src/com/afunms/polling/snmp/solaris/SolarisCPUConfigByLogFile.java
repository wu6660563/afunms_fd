package com.afunms.polling.snmp.solaris;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.Arith;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Solaris CPUConfig 日志解析类
 * 
 * @author 聂林
 */
public class SolarisCPUConfigByLogFile extends SolarisByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisCPUConfigByLogFile.class.getName());

    private static final String SOLARIS_CPUCONFIG_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_CPUCONFIG_BEGIN_KEYWORD;

    private static final String SOLARIS_CPUCONFIG_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_CPUCONFIG_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = SOLARIS_CPUCONFIG_BEGIN_KEYWORD;
        String endStr = SOLARIS_CPUCONFIG_END_KEYWORD;
        String cpuconfigContent = getLogFileContent(beginStr, endStr);
        int nodeid = Integer.valueOf(getNodeDTO().getNodeid());

        List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
        int procesorsnum = 0;
        if (cpuconfigContent.trim().length() > 30) {
            // 判断是否有采集到的cpu配置信息没有信息不做处理
            String[] cpuconfigLineArr = cpuconfigContent.split("\n");
            if (cpuconfigLineArr != null && cpuconfigLineArr.length > 0) {
                int pid = 0;
                for (int i = 0; i < cpuconfigLineArr.length; i++) {
                    String tmpData = cpuconfigLineArr[i].trim();
                    if (tmpData.startsWith("The physical processor")) {
                        Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
                        nodecpuconfig.setNodeid(nodeid);
                        procesorsnum++;
                        if (tmpData.contains("has 64 virtual processors")) {
                            nodecpuconfig.setL2CacheSpeed("");
                            nodecpuconfig.setL2CacheSize("");
                            nodecpuconfig.setDataWidth("64");
                        } else {
                            nodecpuconfig.setL2CacheSpeed("32");
                            nodecpuconfig.setL2CacheSize("32");
                            nodecpuconfig.setDataWidth("");
                        }
                        if (cpuconfigLineArr[i + 1] != null) {
                            String twotempstr = cpuconfigLineArr[i + 1].trim();
                            String[] processorsresult = twotempstr
                                            .split("\\s++");
                            if (processorsresult != null
                                            && processorsresult.length > 0) {
                                nodecpuconfig.setDescrOfProcessors(processorsresult[0]);
                                nodecpuconfig.setName(processorsresult[0]);
                                nodecpuconfig.setProcessorId(pid + "");
                                pid++;
                            }
                            i++;
                        }
                        cpuconfiglist.add(nodecpuconfig);
                    }
                }
            }
        }

        Hashtable<String, Object> CPUConfigHashtable = new Hashtable<String, Object>();
        CPUConfigHashtable.put("cpuconfiglist", cpuconfiglist);
        CPUConfigHashtable.put("procesorsnum", String.valueOf(procesorsnum));

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(CPUConfigHashtable);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
