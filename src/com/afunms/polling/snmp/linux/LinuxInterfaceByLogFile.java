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
 * Linux Interface 日志解析类
 * 
 * @author 聂林
 */
public class LinuxInterfaceByLogFile extends LinuxByLogFile {

    /**
     * 日志
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
        
        // 入口总流速 浮点型
        Float AllInBandwidthUtilHdxFloat = 0F;
        // 出口总流速 浮点型
        Float AllOutBandwidthUtilHdxFloat = 0F;
        for (int i = 0; i < interfaceLineArr.length; i++) {
            String[] interface_tmpData = interfaceLineArr[i].trim().split(
                    "\\s++");
            if (interface_tmpData != null && interface_tmpData.length == 9) {
                if (interfaceLineArr[i].contains("Average:")
                        || interfaceLineArr[i].contains("平均时间:")) {
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
                    // 入口流速 浮点型
                    Float InBandwidthUtilHdxFloat = Float.parseFloat(interface_tmpData[4]) * 8;
                    // 出口流速 浮点型
                    Float OutBandwidthUtilHdxFloat = Float.parseFloat(interface_tmpData[5]) * 8;
                    // 入口流速 字符串型
                    String InBandwidthUtilHdx = String.valueOf((Math.round(InBandwidthUtilHdxFloat)));
                    // 出口流速 字符串型
                    String OutBandwidthUtilHdx = String.valueOf((Math.round(OutBandwidthUtilHdxFloat)));

                    AllInBandwidthUtilHdxFloat += InBandwidthUtilHdxFloat;
                    AllOutBandwidthUtilHdxFloat += OutBandwidthUtilHdxFloat;

                    // 端口索引
                    Interfacecollectdata interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("index");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(i + "");
                    interfacedata.setChname("端口索引");
                    interfaceVector.addElement(interfacedata);

                    // 端口描述
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifDescr");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(ifDescr);
                    interfacedata.setChname("端口描述2");
                    interfaceVector.addElement(interfacedata);

                    // 端口带宽 (为空)
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifSpeed");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue("");
                    interfacedata.setChname("每秒字节数");
                    interfaceVector.addElement(interfacedata);

                    // 当前状态 (为空)
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifOperStatus");
                    interfacedata.setSubentity(i + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue("up");
                    interfacedata.setChname("当前状态");
                    interfaceVector.addElement(interfacedata);

                    // 端口入口流速
                    UtilHdx utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("InBandwidthUtilHdx");
                    utilhdx.setThevalue(InBandwidthUtilHdx);
                    utilhdx.setSubentity(i + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/秒");
                    utilhdx.setChname(i + "端口入口" + "流速");
                    utilhdxVector.addElement(utilhdx);

                    // 端口出口流速
                    utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("OutBandwidthUtilHdx");
                    utilhdx.setThevalue(OutBandwidthUtilHdx);
                    utilhdx.setSubentity(i + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/秒");
                    utilhdx.setChname(i + "端口出口" + "流速");
                    utilhdxVector.addElement(utilhdx);
                }

            }
        }

        String AllInBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat));
        String AllOutBandwidthUtilHdx = String.valueOf(Math.round(AllOutBandwidthUtilHdxFloat));
        String AllBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat) + Math.round(AllOutBandwidthUtilHdxFloat));

        // 端口入口总流速
        UtilHdx utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllInBandwidthUtilHdx");
        utilhdx.setThevalue(AllInBandwidthUtilHdx);
        utilhdx.setSubentity("AllInBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/秒");
        utilhdx.setChname("入口总流速");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx端口入口总流速
        AllUtilHdx allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllInBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllInBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllInBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/秒");
        allUtilHdx.setChname("入口总流速");
        allutilhdxVector.addElement(allUtilHdx);

        // 端口出口总流速
        utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllOutBandwidthUtilHdx");
        utilhdx.setThevalue(AllOutBandwidthUtilHdx);
        utilhdx.setSubentity("AllOutBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/秒");
        utilhdx.setChname("出口总流速");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx端口出口总流速
        allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllOutBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllOutBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllOutBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/秒");
        allUtilHdx.setChname("出口总流速");
        allutilhdxVector.addElement(allUtilHdx);

        // 端口综合流速
        utilhdx = new UtilHdx();
        utilhdx.setIpaddress(ipaddress);
        utilhdx.setCollecttime(date);
        utilhdx.setCategory("Interface");
        utilhdx.setEntity("AllBandwidthUtilHdx");
        utilhdx.setThevalue(AllBandwidthUtilHdx);
        utilhdx.setSubentity("AllBandwidthUtilHdx");
        utilhdx.setRestype("dynamic");
        utilhdx.setUnit("Kb/秒");
        utilhdx.setChname("综合流速");
        utilhdxVector.addElement(utilhdx);
        
        // AllUtilHdx端口综合流速
        allUtilHdx = new AllUtilHdx();
        allUtilHdx.setIpaddress(ipaddress);
        allUtilHdx.setCollecttime(date);
        allUtilHdx.setCategory("Interface");
        allUtilHdx.setEntity("AllBandwidthUtilHdx");
        allUtilHdx.setThevalue(AllBandwidthUtilHdx);
        allUtilHdx.setSubentity("AllBandwidthUtilHdx");
        allUtilHdx.setRestype("dynamic");
        allUtilHdx.setUnit("Kb/秒");
        allUtilHdx.setChname("综合流速");
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
