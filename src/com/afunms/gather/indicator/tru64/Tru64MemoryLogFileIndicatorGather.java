/*
 * @(#)Tru64MemoryLogFileIndicatorGather.java     v1.01, 2014 1 15
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.tru64;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.snmp.tru64.Tru64LogFileKeywordConstant;

/**
 * ClassName:   Tru64MemoryLogFileIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 15 16:46:53
 */
public class Tru64MemoryLogFileIndicatorGather extends LogFileIndicatorGather {

    /**
     * BEGIN_KEYWORD:
     * <p>��ʼ�ַ���
     *
     * @since   v1.01
     */
    private static final String BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_MEMORY_BEGIN_KEYWORD;

    /**
     * END_KEYWORD:
     * <p>�����ַ���
     *
     * @since   v1.01
     */
    private static final String END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_MEMORY_END_KEYWORD;

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
        Vector<Memorycollectdata> vector = new Vector<Memorycollectdata>();
        try {
            Calendar date = getCalendar();

            Double allswapMemory = 0D;
            Double usedswapMemory = 0D;
            Double usedswapPerc = 0D;
            Double allPhysicalMemory = 0D;          //���������ڴ�
            Double usedPhysicalMemory = 0D;         //�����ڴ�ʹ����
            Double PhysicalMemUtilization = 0D;     //�����ڴ�������
            
            Double allPages = 0D;                   //��page
            Double totalUsePages = 0D;              //������page����������managedFreePages
            Double managedFreePages = 0D;

            String memoryContent = getLogFileContent(beginStr, endStr);
            String[] mLineArr = memoryContent.split("\n");
            for (int i = 0; i < mLineArr.length; i++) {
                if(mLineArr[i].indexOf("Total Physical Memory") >= 0 && mLineArr[i].indexOf("Total Physical Memory Use") == -1) {
                    //Total Physical Memory =  2048.00 M
                    String totalSizeStr = mLineArr[i].substring(mLineArr[i].indexOf("=") + 1);
                    allPhysicalMemory = Double.parseDouble(totalSizeStr.substring(0,totalSizeStr.indexOf("M")));
                    
                    //��һ��Ϊ��pages
                    String allPagesStr = mLineArr[i+1].substring(mLineArr[i+1].indexOf("=") + 1);
                    allPages = Double.parseDouble(allPagesStr.substring(0,allPagesStr.indexOf("pages")));
                }
                if(mLineArr[i].indexOf("Total Physical Memory Use") >= 0){
                    //Total Physical Memory Use:     261784 / 2045.19M
                    String[] usedSizeStr =  mLineArr[i].split(":");
                    String[] temp = usedSizeStr[1].trim().split("\\s++");
                    if(temp[0] != null && !"".equals(temp[0])) {
                        totalUsePages = Double.parseDouble(temp[0]);
                    }
                    if(temp[2] != null && !"".equals(temp[2])) {
                        usedPhysicalMemory = Double.parseDouble(temp[2].substring(0, temp[2].indexOf("M")));
                    }
                }
                if(mLineArr[i].indexOf("free pages") >= 0) {
                    //free pages = 272220
                    String[] temp = mLineArr[i].trim().split("\\s++");
                    if(temp[3] != null && !"".equals(temp[3])) {
                        managedFreePages = Double.parseDouble(temp[3]);
                    }
                }
                
            }
            
            if(allPages > 0D){
                PhysicalMemUtilization =(totalUsePages - managedFreePages) / allPages * 100;
                usedPhysicalMemory = allPhysicalMemory * PhysicalMemUtilization / 100;
            }

            // �ڴ��ܴ�С ������
            String sizeString = String.valueOf(format(allPhysicalMemory));
            // �ڴ�ʹ�ô�С ������ (ת���� M Ϊ��λ)
            String usedString = String.valueOf(format(usedPhysicalMemory));
            String utilizationString = String.valueOf(format(PhysicalMemUtilization));
            // �����ڴ��ܴ�С
            vector.add(createMemorycollectdata(date, "Capability", "PhysicalMemory", sizeString, "M"));
            // �����ڴ�ʹ�ô�С
            vector.add(createMemorycollectdata(date, "UsedSize", "PhysicalMemory", usedString, "M"));
            // �����ڴ�ʹ����
            vector.add(createMemorycollectdata(date, "Utilization", "PhysicalMemory", utilizationString, "%"));

            
            // �ڴ��ܴ�С ������
            sizeString = String.valueOf(format(allswapMemory));
            // �ڴ�ʹ�ô�С ������ (ת���� M Ϊ��λ)
            usedString = String.valueOf(format(usedswapMemory));
            utilizationString = String.valueOf(format(usedswapPerc));
            // �����ڴ��ܴ�С
            vector.add(createMemorycollectdata(date, "Capability", "SwapMemory", sizeString, "M"));

            // �����ڴ�ʹ�ô�С
            vector.add(createMemorycollectdata(date, "UsedSize", "SwapMemory", usedString, "M"));

            // �����ڴ�ʹ����
            vector.add(createMemorycollectdata(date, "Utilization", "SwapMemory", utilizationString, "%"));
        } catch (Exception e) {
            e.printStackTrace();
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

