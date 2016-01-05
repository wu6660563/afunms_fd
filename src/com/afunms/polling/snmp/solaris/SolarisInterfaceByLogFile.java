package com.afunms.polling.snmp.solaris;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.UtilHdx;

/**
 * Solaris Interface ��־������
 * 
 * @author ����
 */
public class SolarisInterfaceByLogFile extends SolarisByLogFile {

    /**
     * ��־
     */
    private static SysLogger logger = SysLogger
                    .getLogger(SolarisInterfaceByLogFile.class.getName());

    private static final String SOLARIS_MAC_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MAC_BEGIN_KEYWORD;

    private static final String SOLARIS_MAC_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_MAC_END_KEYWORD;

    private static final String SOLARIS_NETCONF_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_NETCONF_BEGIN_KEYWORD;

    private static final String SOLARIS_NETCONF_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_NETCONF_END_KEYWORD;

    private static final String SOLARIS_INTERFACE_BEGIN_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_INTERFACE_BEGIN_KEYWORD;

    private static final String SOLARIS_INTERFACE_END_KEYWORD = SolarisLogFileKeywordConstant.SOLARIS_INTERFACE_END_KEYWORD;

    @Override
    public ObjectValue getObjectValue() {
        
        String beginStr = SOLARIS_MAC_BEGIN_KEYWORD;
        String endStr = SOLARIS_MAC_END_KEYWORD;
        String macContent = getLogFileContent(beginStr, endStr);
        int nodeid = Integer.valueOf(getNodeDTO().getNodeid());

        Hashtable<String,String> machash = new Hashtable<String,String>();
        boolean infflg = false;
        String allMac = "";
        String[] macLineArr = macContent.split("\n");
        if (macLineArr != null && macLineArr.length > 0) {
            // ����ѭ����¼
            for (int i = 0; i < macLineArr.length; i++) {
                String infname = ""; // ��������
                if (macLineArr[i].indexOf("flags=") > 0) {
                    infname = macLineArr[i].substring(0,
                                    macLineArr[i].indexOf("flags="));
                    infname = infname.trim().replaceAll(":", "");
                    infflg = true;
                }

                if (infflg = true && macLineArr[i].indexOf("ether") > 0) {// ������mac��ַ
                    String macstr = macLineArr[i].trim();
                    macstr = macstr.replace("ether", "");
                    machash.put(infname.trim(), macstr.trim());// ������
                    if (i != macLineArr.length - 1) {
                        allMac = allMac + macstr.trim() + ",";
                    } else {
                        allMac = allMac + macstr;
                    }
                    infflg = false;
                }
            }
        }
        
        beginStr = SOLARIS_NETCONF_BEGIN_KEYWORD;
        endStr = SOLARIS_NETCONF_END_KEYWORD;
        String netconfContent = getLogFileContent(beginStr, endStr);

        String[] netconfLineArr = netconfContent.split("\n");
        List<Hashtable<String, String>> netconfList = new ArrayList<Hashtable<String,String>>();
        Hashtable<String,String> netstatfhash = new Hashtable<String,String>(); //����״̬
        Hashtable<String,String> netspeedhash = new Hashtable<String,String>(); //�����������
        for (int i = 0; i < netconfLineArr.length; i++) {
            String[] tmpData = netconfLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length == 8) {
                if (!tmpData[2].equalsIgnoreCase("unknown")) {
                    Hashtable<String, String> netconfhash = new Hashtable<String, String>();
                    String desc = tmpData[0].trim();
                    String speed = tmpData[4].trim() + " " + tmpData[5].trim();
                    String mac = "";
                    String status = tmpData[2].trim();
                    netconfhash.put("desc", desc);          // ����
                    netconfhash.put("speed", speed);        // ����
                    netconfhash.put("mac", mac);                    
                    netconfhash.put("status", status);      // ����״̬
                    netconfList.add(netconfhash);
                    //������״̬����
                    netstatfhash.put(desc, status);
                    //��������
                    netspeedhash.put(desc, speed);
                }
            }
        }
        
        beginStr = SOLARIS_INTERFACE_BEGIN_KEYWORD;
        endStr = SOLARIS_INTERFACE_END_KEYWORD;
        String interfaceContent = getLogFileContent(beginStr, endStr);
        String ipaddress = getNodeDTO().getIpaddress();
        Calendar date = getCalendarInstance();

        String[] interfaceLineArr = interfaceContent.split("\n");

        int ifindex = 0;
        List<Hashtable<String, String>> netflowlist = new ArrayList<Hashtable<String,String>>();
        Vector<Interfacecollectdata> interfaceVector = new Vector<Interfacecollectdata>();
        Vector<UtilHdx> utilhdxVector = new Vector<UtilHdx>();

        // ��������� ������
        Float AllInBandwidthUtilHdxFloat = 0F;
        // ���������� ������
        Float AllOutBandwidthUtilHdxFloat = 0F;
        for (int i = 1; i < interfaceLineArr.length; i++) {
            String[] tmpData = interfaceLineArr[i].trim().split("\\s++");
            // ��������������Ϣ
            if (tmpData != null && tmpData.length == 10) {
                Hashtable<String, String> netflowfhash = new Hashtable<String, String>();
                // ����������9������
                // Int(��������) rKB(�������kB/s) wKB(��������kb/s) rPk
                // wPk(������ݰ�/s) rAvs(���ƽ��kb/s) wAvs(����ƽ��kb/s)
                // Util(������kb/s) Sat state(����״̬) speed(��������)
                
                String Int =  tmpData[1].trim();
                String rKB =  tmpData[2].trim();
                String wKB =  tmpData[3].trim();
                String rPk =  tmpData[4].trim();
                String wPk =  tmpData[5].trim();
                String rAvs =  tmpData[6].trim();
                String wAvs =  tmpData[7].trim();
                String Util =  tmpData[8].trim();
                String Sat =  tmpData[9].trim();
                
                String state =  netstatfhash.get(Int);
                String speed =  netspeedhash.get(Int);
                
                // ������� ������
                Float InBandwidthUtilHdxFloat = Float.parseFloat(rKB);
                // �������� ������
                Float OutBandwidthUtilHdxFloat = Float.parseFloat(wKB);
                // ������� �ַ�����
                String InBandwidthUtilHdx = String.valueOf((Math.round(InBandwidthUtilHdxFloat)));
                // �������� �ַ�����
                String OutBandwidthUtilHdx = String.valueOf((Math.round(OutBandwidthUtilHdxFloat)));

                AllInBandwidthUtilHdxFloat += InBandwidthUtilHdxFloat;
                AllOutBandwidthUtilHdxFloat += OutBandwidthUtilHdxFloat;

                if (!"lo0".equals(Int)) {// �������� lo0

                    netflowfhash.put("Int", Int);// ������
                    netflowfhash.put("rKB", rKB);
                    netflowfhash.put("wKB", wKB);
                    netflowfhash.put("rPk", rPk);
                    netflowfhash.put("wPk", wPk);
                    netflowfhash.put("rAvs", rAvs);
                    netflowfhash.put("wAvs", wAvs);
                    netflowfhash.put("Util", Util);
                    netflowfhash.put("Sat", Sat);
                    netflowfhash.put("state", state);
                    netflowfhash.put("speed", speed);

                    // �˿�����
                    Interfacecollectdata interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("index");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(ifindex + "");
                    interfacedata.setChname("�˿�����");
                    interfaceVector.addElement(interfacedata);

                    // �˿�����
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifDescr");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(Int);
                    interfacedata.setChname("�˿�����");
                    interfaceVector.addElement(interfacedata);

                    // �˿ڴ���
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifSpeed");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(speed);
                    interfacedata.setChname("");
                    interfaceVector.addElement(interfacedata);

                    // ��ǰ״̬
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifOperStatus");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(state);
                    interfacedata.setChname("��ǰ״̬");
                    interfaceVector.addElement(interfacedata);

                    // �˿��������
                    UtilHdx utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    String chnameBand = "";
                    utilhdx.setEntity("InBandwidthUtilHdx");
                    utilhdx.setThevalue(InBandwidthUtilHdx);
                    utilhdx.setSubentity(ifindex + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/��");
                    utilhdx.setChname(ifindex + "�˿����" + "����");
                    utilhdxVector.addElement(utilhdx);
                    // �˿ڳ�������
                    utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("OutBandwidthUtilHdx");
                    utilhdx.setThevalue(OutBandwidthUtilHdx);
                    utilhdx.setSubentity(ifindex + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/��");
                    utilhdx.setChname(ifindex + "�˿ڳ���" + "����");
                    utilhdxVector.addElement(utilhdx);
                    ifindex = ifindex + 1;

                    netflowlist.add(netflowfhash);
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

        Hashtable<String, Object> interfaceHashtable = new Hashtable<String, Object>();
        interfaceHashtable.put("netflowlist", netflowlist);
        interfaceHashtable.put("interfaceVector", interfaceVector);
        interfaceHashtable.put("utilhdxVector", utilhdxVector);
        interfaceHashtable.put("netconfList", netconfList);
        interfaceHashtable.put("allMac", allMac);
        
        ObjectValue objectValue = new ObjectValue();
        objectValue.setObjectValue(interfaceHashtable);
        objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
        return objectValue;
    }
}
