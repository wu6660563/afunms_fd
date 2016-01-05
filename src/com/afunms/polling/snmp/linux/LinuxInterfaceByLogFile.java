package com.afunms.polling.snmp.linux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.UtilHdx;

/**
 * Linux Interface ��־������
 * 
 * @author ����
 */
public class LinuxInterfaceByLogFile extends LinuxByLogFile {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
            .getLogger(LinuxInterfaceByLogFile.class.getName());

    private static final String LINUX_INTERFACE_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_INTERFACE_BEGIN_KEYWORD;

    private static final String LINUX_INTERFACE_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_INTERFACE_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        String beginStr = LINUX_INTERFACE_BEGIN_KEYWORD;
        String endStr = LINUX_INTERFACE_END_KEYWORD;
        String interfaceContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] interfaceLineArr = interfaceContent.split("\n");
        
        ArrayList<Hashtable<String, String>> iflist = new ArrayList<Hashtable<String, String>>();
        Vector<Interfacecollectdata> interfaceVector = new Vector<Interfacecollectdata>();
        Vector<UtilHdx> utilhdxVector = new Vector<UtilHdx>();
        Vector<AllUtilHdx> allutilhdxVector = new Vector<AllUtilHdx>();
        
        // ��������� ������
        Float AllInBandwidthUtilHdxFloat = 0F;
        // ���������� ������
        Float AllOutBandwidthUtilHdxFloat = 0F;
        for (int i = 0; i < interfaceLineArr.length; i++) {
            String[] interface_tmpData = interfaceLineArr[i].trim().split(
                    "\\s++");
            if (interface_tmpData != null && interface_tmpData.length == 9) {
                if (interfaceLineArr[i].contains("Average:")
                        || interfaceLineArr[i].contains("ƽ��ʱ��:")) {
                    if (interface_tmpData[1].trim().equalsIgnoreCase("IFACE")) {
                        continue;
                    }
                    Hashtable<String, String> ifhash = new Hashtable<String, String>();
                    ifhash.put("IFACE", interface_tmpData[1]);
                    ifhash.put("rxpck/s", interface_tmpData[2]);
                    ifhash.put("txpck/s", interface_tmpData[3]);
                    ifhash.put("rxbyt/s", interface_tmpData[4]);
                    ifhash.put("txbyt/s", interface_tmpData[5]);
                    ifhash.put("rxcmp/s", interface_tmpData[6]);
                    ifhash.put("txcmp/s", interface_tmpData[7]);
                    ifhash.put("rxmcst/s", interface_tmpData[8]);
                    iflist.add(ifhash);
                    
                    String ifDescr = interface_tmpData[1];
                    // ������� ������
                    Float InBandwidthUtilHdxFloat = Float.parseFloat(interface_tmpData[4]) * 8;
                    // �������� ������
                    Float OutBandwidthUtilHdxFloat = Float.parseFloat(interface_tmpData[5]) * 8;
                    // ������� �ַ�����
                    String InBandwidthUtilHdx = String.valueOf((Math.round(InBandwidthUtilHdxFloat)));
                    // �������� �ַ�����
                    String OutBandwidthUtilHdx = String.valueOf((Math.round(OutBandwidthUtilHdxFloat)));

                    AllInBandwidthUtilHdxFloat += InBandwidthUtilHdxFloat;
                    AllOutBandwidthUtilHdxFloat += OutBandwidthUtilHdxFloat;

                    // �˿�����
                    Interfacecollectdata interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("index");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(i + "");
                    interfacedata.setChname("�˿�����");
                    interfaceVector.addElement(interfacedata);

                    // �˿�����
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifDescr");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(ifDescr);
                    interfacedata.setChname("�˿�����2");
                    interfaceVector.addElement(interfacedata);

                    // �˿ڴ��� (Ϊ��)
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifSpeed");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue("");
                    interfacedata.setChname("ÿ���ֽ���");
                    interfaceVector.addElement(interfacedata);

                    // ��ǰ״̬ (Ϊ��)
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifOperStatus");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue("up");
                    interfacedata.setChname("��ǰ״̬");
                    interfaceVector.addElement(interfacedata);

                    // �˿��������
                    UtilHdx utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("InBandwidthUtilHdx");
                    utilhdx.setThevalue(InBandwidthUtilHdx);
                    utilhdx.setSubentity(i + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/��");
                    utilhdx.setChname(i + "�˿����" + "����");
                    utilhdxVector.addElement(utilhdx);

                    // �˿ڳ�������
                    utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("OutBandwidthUtilHdx");
                    utilhdx.setThevalue(OutBandwidthUtilHdx);
                    utilhdx.setSubentity(i + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/��");
                    utilhdx.setChname(i + "�˿ڳ���" + "����");
                    utilhdxVector.addElement(utilhdx);
                }

            }
        }

        String AllInBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat));
        String AllOutBandwidthUtilHdx = String.valueOf(Math.round(AllOutBandwidthUtilHdxFloat));
        String AllBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat) + Math.round(AllOutBandwidthUtilHdxFloat));

        // �˿����������
        UtilHdx utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllInBandwidthUtilHdx");
        utilhdx.setThevalue(AllInBandwidthUtilHdx);
        utilhdx.setSubentity("AllInBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/��");
        utilhdx.setChname("���������");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx�˿����������
        AllUtilHdx allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllInBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllInBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllInBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/��");
        allUtilHdx.setChname("���������");
        allutilhdxVector.addElement(allUtilHdx);

        // �˿ڳ���������
        utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllOutBandwidthUtilHdx");
        utilhdx.setThevalue(AllOutBandwidthUtilHdx);
        utilhdx.setSubentity("AllOutBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/��");
        utilhdx.setChname("����������");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx�˿ڳ���������
        allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllOutBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllOutBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllOutBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/��");
        allUtilHdx.setChname("����������");
        allutilhdxVector.addElement(allUtilHdx);

        // �˿��ۺ�����
        utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllBandwidthUtilHdx");
        utilhdx.setThevalue(AllBandwidthUtilHdx);
        utilhdx.setSubentity("AllBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/��");
        utilhdx.setChname("�ۺ�����");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx�˿��ۺ�����
        allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/��");
        allUtilHdx.setChname("�ۺ�����");
        allutilhdxVector.addElement(allUtilHdx);

        Hashtable<String, Object> interfaceHashtable = new Hashtable<String, Object>();
        interfaceHashtable.put("iflist", iflist);
        interfaceHashtable.put("interfaceVector", interfaceVector);
        interfaceHashtable.put("utilhdxVector", utilhdxVector);
        interfaceHashtable.put("allutilhdxVector", allutilhdxVector);

        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(interfaceHashtable);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
