/*
 * @(#)LinuxMemoryLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.snmp.linux.LinuxLogFileKeywordConstant;

/**
 * ClassName:   LinuxMemoryLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 16:41:56
 */
public class LinuxMemoryLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>开始字符串
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MEMORY_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>结束字符串
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_MEMORY_END_KEYWORD;

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String beginStr = BEGIN_KEYWORD;
        String endStr = END_KEYWORD;
        String memoryContent = getLogFileContent(beginStr, endStr);
        Calendar date = getCalendar();

        String[] memoryLineArr = memoryContent.split("\n");
        List<Hashtable<String, String>> memperflist = new ArrayList<Hashtable<String, String>>();
        Vector<Memorycollectdata> vector = new Vector<Memorycollectdata>();
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
                    Double sizeDouble = Double.parseDouble(total);
                    // 内存使用大小 浮点型
                    Double usedDouble = Double.parseDouble(used) - Double.parseDouble(buffers) - Double.parseDouble(cached);
                    // Memory
                    Double utilization = usedDouble * 100 / sizeDouble;
                    // 内存总大小 整数型 (转换成 M 为单位)
                    String sizeString = String.valueOf(format(sizeDouble / 1024));
                    // 内存使用大小 整数型 (转换成 M 为单位)
                    String usedString = String.valueOf(format(usedDouble / 1024));
                    String utilizationString = String.valueOf(format(utilization));

                    // 物理内存总大小
                    vector.add(createMemorycollectdata(date, "Capability", "PhysicalMemory", sizeString, "M"));

                    // 物理内存使用大小
                    vector.add(createMemorycollectdata(date, "UsedSize", "PhysicalMemory", usedString, "M"));

                    // 物理内存使用率
                    vector.add(createMemorycollectdata(date, "Utilization", "PhysicalMemory", utilizationString, "%"));
                } else if (tmpData[0].trim().equalsIgnoreCase("Swap:")) {
                    // Swap 虚拟内存
                    String total = tmpData[1].trim();
                    String used = tmpData[2].trim();
                    // String free = tmpData[3].trim();
                    
                    Hashtable<String, String> memperfhash = new Hashtable<String, String>();
                    memperfhash.put("total", tmpData[1].trim());
                    memperfhash.put("used", tmpData[2].trim());
                    memperfhash.put("free", tmpData[3].trim());
                    memperflist.add(memperfhash);

                    // 内存总大小 浮点型
                    Double sizeDouble = Double.parseDouble(total);
                    // 内存使用大小 浮点型
                    Double usedDouble = Double.parseDouble(used);
                    // Memory
                    Double utilization = usedDouble * 100 / sizeDouble;
                    // 内存总大小 整数型 (转换成 M 为单位)
                    String sizeString = String.valueOf(format(sizeDouble / 1024));
                    // 内存使用大小 整数型 (转换成 M 为单位)
                    String usedString = String.valueOf(format(usedDouble / 1024));
                    String utilizationString = String.valueOf(format(utilization));

                    // 交换内存总大小
                    vector.add(createMemorycollectdata(date, "Capability", "SwapMemory", sizeString, "M"));

                    // 交换内存使用大小
                    vector.add(createMemorycollectdata(date, "UsedSize", "SwapMemory", usedString, "M"));

                    // 交换内存使用率
                    vector.add(createMemorycollectdata(date, "Utilization", "SwapMemory", utilizationString, "%"));
                }
            }
        }

        return createSimpleIndicatorValue(vector);
    }

    public Memorycollectdata createMemorycollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        Memorycollectdata memorydata = new Memorycollectdata();
        memorydata.setIpaddress(getIpAddress());
        memorydata.setCollecttime(date);
        memorydata.setCategory("Memory");
        memorydata.setEntity(entity);
        memorydata.setSubentity(subentity);
        memorydata.setRestype("dynamic");
        memorydata.setThevalue(thevalue);
        memorydata.setUnit(unit);
        return memorydata;
    }
}

