package com.afunms.polling.snmp.linux;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Diskcollectdata;

/**
 * Linux Diskperf 日志解析类
 * 
 * @author 聂林
 */
public class LinuxDiskperfByLogFile extends LinuxByLogFile {

    /**
     * 日志
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxDiskperfByLogFile.class.getName());

    private static final String LINUX_DISKPERF_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISKPERF_BEGIN_KEYWORD;

    private static final String LINUX_DISKPERF_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISKPERF_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_DISKPERF_BEGIN_KEYWORD;
        String endStr = LINUX_DISKPERF_END_KEYWORD;
        String diskperfContent = getLogFileContent(beginStr, endStr);

        String[] diskperfLineArr = diskperfContent.split("\n");
        String[] diskperf_tmpData = null;
        List<Hashtable<String,String>> alldiskperf = new ArrayList<Hashtable<String,String>>();
        
        for(int i=0; i<diskperfLineArr.length;i++){
            diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
            if(diskperf_tmpData != null && diskperf_tmpData.length==10){
                if("Average:".equalsIgnoreCase(diskperf_tmpData[0].trim())
                        || "平均时间:".equalsIgnoreCase(diskperf_tmpData[0].trim())){
                    if(diskperf_tmpData[1].trim().equalsIgnoreCase("DEV")){
                        //处理第一行标题
                        continue;
                    }else{
                        Hashtable<String,String> diskperfhash = new Hashtable<String,String>();
                        diskperfhash.put("tps", diskperf_tmpData[2].trim());
                        diskperfhash.put("rd_sec/s", diskperf_tmpData[3].trim());
                        diskperfhash.put("wr_sec/s", diskperf_tmpData[4].trim());
                        diskperfhash.put("avgrq-sz", diskperf_tmpData[5].trim());
                        diskperfhash.put("avgqu-sz", diskperf_tmpData[6].trim());
                        diskperfhash.put("await", diskperf_tmpData[7].trim());
                        diskperfhash.put("svctm", diskperf_tmpData[8].trim());
                        diskperfhash.put("%util", diskperf_tmpData[9].trim());
                        diskperfhash.put("%busy",Math.round(Float.parseFloat(diskperf_tmpData[8].trim())*100/(Float.parseFloat(diskperf_tmpData[7].trim())+Float.parseFloat(diskperf_tmpData[8].trim())))+"");
                        diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
                        alldiskperf.add(diskperfhash);
                    }
                }
            }               
        }
        
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(alldiskperf);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
