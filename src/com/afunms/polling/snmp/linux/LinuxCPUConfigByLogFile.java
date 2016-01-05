package com.afunms.polling.snmp.linux;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;

/**
 * Linux CPUConfig 日志解析类
 * 
 * @author 聂林
 */
public class LinuxCPUConfigByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxCPUConfigByLogFile.class.getName());

    private static final String LINUX_CPUCONFIG_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPUCONFIG_BEGIN_KEYWORD;

    private static final String LINUX_CPUCONFIG_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_CPUCONFIG_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_CPUCONFIG_BEGIN_KEYWORD;
        String endStr = LINUX_CPUCONFIG_END_KEYWORD;
        String cpuconfigContent = getLogFileContent(beginStr, endStr);
        int nodeid = Integer.valueOf(getNodeDTO().getNodeid());
        String[] cpuconfigLineArr = cpuconfigContent.split("\n");

        List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
        String procesors = "";
        Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
        
        int procesorsnum = 0;
        try {
        	for (int i = 0; i < cpuconfigLineArr.length; i++) {
                String[] tmpData = cpuconfigLineArr[i].trim().split(":");
                if (tmpData != null && tmpData.length > 0) {
                    String key = tmpData[0].trim();
                    String value = tmpData[1].trim();
                    if ("processor".equalsIgnoreCase(key)) {
                        nodecpuconfig = new Nodecpuconfig();
                        nodecpuconfig.setNodeid(nodeid);
                        nodecpuconfig.setProcessorId(value);
                        procesors = value;
                    } else if ("model name".equalsIgnoreCase(key)) {
                        nodecpuconfig.setName(value);
                    } else if ("cpu MHz".equalsIgnoreCase(key)) {
                        nodecpuconfig.setProcessorSpeed(value);
                    } else if ("cache size".equalsIgnoreCase(key)) {
                        nodecpuconfig.setL2CacheSize(value);
                        cpuconfiglist.add(nodecpuconfig);
                    }
                }
            }
            
        	// 计算节点的CPU配置个数
            if (procesors != null && procesors.trim().length() > 0) {
                try {
                    procesorsnum = Integer.parseInt(procesors) + 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LINUXCPU-->ipaddress:"+getNodeDTO().getIpaddress(), e);
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
