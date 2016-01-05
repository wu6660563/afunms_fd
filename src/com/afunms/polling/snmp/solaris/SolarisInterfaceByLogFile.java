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
 * Solaris Interface 日志解析类
 * 
 * @author 聂林
 */
public class SolarisInterfaceByLogFile extends SolarisByLogFile {

    /**
     * 日志
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
            // 迭代循环记录
            for (int i = 0; i < macLineArr.length; i++) {
                String infname = ""; // 网卡名称
                if (macLineArr[i].indexOf("flags=") > 0) {
                    infname = macLineArr[i].substring(0,
                                    macLineArr[i].indexOf("flags="));
                    infname = infname.trim().replaceAll(":", "");
                    infflg = true;
                }

                if (infflg = true && macLineArr[i].indexOf("ether") > 0) {// 解析出mac地址
                    String macstr = macLineArr[i].trim();
                    macstr = macstr.replace("ether", "");
                    machash.put(infname.trim(), macstr.trim());// 网卡名
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
        Hashtable<String,String> netstatfhash = new Hashtable<String,String>(); //网卡状态
        Hashtable<String,String> netspeedhash = new Hashtable<String,String>(); //网卡最大速率
        for (int i = 0; i < netconfLineArr.length; i++) {
            String[] tmpData = netconfLineArr[i].trim().split("\\s++");
            if (tmpData != null && tmpData.length == 8) {
                if (!tmpData[2].equalsIgnoreCase("unknown")) {
                    Hashtable<String, String> netconfhash = new Hashtable<String, String>();
                    String desc = tmpData[0].trim();
                    String speed = tmpData[4].trim() + " " + tmpData[5].trim();
                    String mac = "";
                    String status = tmpData[2].trim();
                    netconfhash.put("desc", desc);          // 描述
                    netconfhash.put("speed", speed);        // 带宽
                    netconfhash.put("mac", mac);                    
                    netconfhash.put("status", status);      // 连接状态
                    netconfList.add(netconfhash);
                    //把网卡状态保存
                    netstatfhash.put(desc, status);
                    //网卡带宽
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

        // 入口总流速 浮点型
        Float AllInBandwidthUtilHdxFloat = 0F;
        // 出口总流速 浮点型
        Float AllOutBandwidthUtilHdxFloat = 0F;
        for (int i = 1; i < interfaceLineArr.length; i++) {
            String[] tmpData = interfaceLineArr[i].trim().split("\\s++");
            // 解析网卡流速信息
            if (tmpData != null && tmpData.length == 10) {
                Hashtable<String, String> netflowfhash = new Hashtable<String, String>();
                // 网卡流速有9个参数
                // Int(网卡名称) rKB(入口流速kB/s) wKB(出口流速kb/s) rPk
                // wPk(入口数据包/s) rAvs(入口平均kb/s) wAvs(出口平均kb/s)
                // Util(总流速kb/s) Sat state(网卡状态) speed(网卡带宽)
                
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
                
                // 入口流速 浮点型
                Float InBandwidthUtilHdxFloat = Float.parseFloat(rKB);
                // 出口流速 浮点型
                Float OutBandwidthUtilHdxFloat = Float.parseFloat(wKB);
                // 入口流速 字符串型
                String InBandwidthUtilHdx = String.valueOf((Math.round(InBandwidthUtilHdxFloat)));
                // 出口流速 字符串型
                String OutBandwidthUtilHdx = String.valueOf((Math.round(OutBandwidthUtilHdxFloat)));

                AllInBandwidthUtilHdxFloat += InBandwidthUtilHdxFloat;
                AllOutBandwidthUtilHdxFloat += OutBandwidthUtilHdxFloat;

                if (!"lo0".equals(Int)) {// 过滤网卡 lo0

                    netflowfhash.put("Int", Int);// 网卡名
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

                    // 端口索引
                    Interfacecollectdata interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("index");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(ifindex + "");
                    interfacedata.setChname("端口索引");
                    interfaceVector.addElement(interfacedata);

                    // 端口描述
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifDescr");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(Int);
                    interfacedata.setChname("端口描述");
                    interfaceVector.addElement(interfacedata);

                    // 端口带宽
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

                    // 当前状态
                    interfacedata = new Interfacecollectdata();
                    interfacedata.setIpaddress(ipaddress);
                    interfacedata.setCollecttime(date);
                    interfacedata.setCategory("Interface");
                    interfacedata.setEntity("ifOperStatus");
                    interfacedata.setSubentity(ifindex + "");
                    interfacedata.setRestype("static");
                    interfacedata.setUnit("");
                    interfacedata.setThevalue(state);
                    interfacedata.setChname("当前状态");
                    interfaceVector.addElement(interfacedata);

                    // 端口入口流速
                    UtilHdx utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    String chnameBand = "";
                    utilhdx.setEntity("InBandwidthUtilHdx");
                    utilhdx.setThevalue(InBandwidthUtilHdx);
                    utilhdx.setSubentity(ifindex + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/秒");
                    utilhdx.setChname(ifindex + "端口入口" + "流速");
                    utilhdxVector.addElement(utilhdx);
                    // 端口出口流速
                    utilhdx = new UtilHdx();
                    utilhdx.setIpaddress(ipaddress);
                    utilhdx.setCollecttime(date);
                    utilhdx.setCategory("Interface");
                    utilhdx.setEntity("OutBandwidthUtilHdx");
                    utilhdx.setThevalue(OutBandwidthUtilHdx);
                    utilhdx.setSubentity(ifindex + "");
                    utilhdx.setRestype("dynamic");
                    utilhdx.setUnit("Kb/秒");
                    utilhdx.setChname(ifindex + "端口出口" + "流速");
                    utilhdxVector.addElement(utilhdx);
                    ifindex = ifindex + 1;

                    netflowlist.add(netflowfhash);
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
