package com.afunms.polling.snmp.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Diskcollectdata;

/**
 * Linux Disk ��־������
 * 
 * @author ����
 */
public class LinuxDiskByLogFile extends LinuxByLogFile {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxDiskByLogFile.class.getName());

    private static final String LINUX_DISK_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISK_BEGIN_KEYWORD;

    private static final String LINUX_DISK_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_DISK_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_DISK_BEGIN_KEYWORD;
        String endStr = LINUX_DISK_END_KEYWORD;
        String diskContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();
        Hashtable ipAllData = getIpAllData();

        Vector<Diskcollectdata> diskVector = new Vector<Diskcollectdata>();
        String[] diskLineArr = diskContent.split("\n");
        for (int i = 1; i < diskLineArr.length; i++) {
            String[] tmpData = diskLineArr[i].split("\\s++");
            if ((tmpData != null) && (tmpData.length == 6)) {
                String allBlockSizeStr = tmpData[1];
                String diskLabel = tmpData[5];

                // ������ �ַ�������
                String diskUtilizationStr = tmpData[4].substring(0, tmpData[4].indexOf("%"));
                // ������ ����������
                Float diskUtilizationFloat = Float.parseFloat(diskUtilizationStr);

                // ���ܴ�С ����������
                float allBlockSizeFloat = Float.parseFloat(allBlockSizeStr);
                // �����ܴ�С
                float allsizeFloat = allBlockSizeFloat * 1.0f / 1024;
                // �����ܴ�С��λ
                String sizeUnit = "M";
                if (allsizeFloat >= 1024.0f) {
                    allsizeFloat = allsizeFloat / 1024;
                    sizeUnit = "G";
                }

                // ���ܴ�С ����������
                String usedSizeStr = tmpData[2];
                // �����ܴ�С
                float usedSizeFloat = Float.parseFloat(usedSizeStr);
                // �����ܴ�С��λ
                String usedUnit = "M";
                usedSizeFloat = usedSizeFloat / 1024;
                if (usedSizeFloat >= 1024.0f) {
                    usedSizeFloat = usedSizeFloat / 1024;
                    usedUnit = "G";
                }

                // ������
                Diskcollectdata diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("Utilization");
                diskdata.setSubentity(diskLabel);
                diskdata.setRestype("static");
                diskdata.setUnit("%");
                diskdata.setThevalue(diskUtilizationStr);
                diskVector.addElement(diskdata);

                // �ܿռ��С
                diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("AllSize");
                diskdata.setSubentity(diskLabel);
                diskdata.setRestype("static");
                diskdata.setUnit(sizeUnit);
                diskdata.setThevalue(Float.toString(allsizeFloat));
                diskVector.addElement(diskdata);

                // ʹ�ô�С
                diskdata = new Diskcollectdata();
                diskdata.setIpaddress(ipaddress);
                diskdata.setCollecttime(date);
                diskdata.setCategory("Disk");
                diskdata.setEntity("UsedSize");
                diskdata.setSubentity(diskLabel);
                diskdata.setRestype("static");
                diskdata.setThevalue(Float.toString(usedSizeFloat));
                diskdata.setUnit(usedUnit);
                diskVector.addElement(diskdata);

                // ������
                try {
                    String diskinc = "0.0";
                    float lastDiskUtilizationFloat = 0f;
                    Vector<Diskcollectdata> lastDiskVector = (Vector<Diskcollectdata>) ipAllData.get("disk");
                    if (lastDiskVector != null && lastDiskVector.size() > 0) {
                        for (Diskcollectdata diskcollectdata : lastDiskVector) {
                            if ((tmpData[5]).equals(diskcollectdata.getSubentity())
                                    && "Utilization".equals(diskcollectdata
                                            .getEntity())) {
                                lastDiskUtilizationFloat = Float.parseFloat(diskcollectdata
                                        .getThevalue());
                            }
                        }
                    }
                    if (lastDiskUtilizationFloat == 0) {
                        lastDiskUtilizationFloat = diskUtilizationFloat;
                    }
                    if (diskUtilizationFloat - lastDiskUtilizationFloat > 0) {
                        diskinc = (diskUtilizationFloat - lastDiskUtilizationFloat) + "";
                    }
                    diskdata = new Diskcollectdata();
                    diskdata.setIpaddress(ipaddress);
                    diskdata.setCollecttime(date);
                    diskdata.setCategory("Disk");
                    diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
                    diskdata.setSubentity(diskLabel);
                    diskdata.setRestype("dynamic");
                    diskdata.setUnit("%");
                    diskdata.setThevalue(diskinc);
                    diskVector.addElement(diskdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(diskVector);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
