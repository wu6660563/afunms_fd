/*
 * @(#)HpunixNetstatByLogFile.java     v1.01, Mar 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tru64;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.UtilHdx;
/**
 * 
 * ClassName:   Tru64NetstatByLogFile.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 26, 2013 8:51:05 PM
 */
public class Tru64NetstatByLogFile  extends Tru64ByLogFile {
	
	private static final String TRU64_NETSTAT_BEGIN_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_BEGIN_KEYWORD;

    private static final String TRU64_NETSTAT_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_NETSTAT_END_KEYWORD;
    
    private static final String TRU64_IPCONFIG_START_KEYWORD = Tru64LogFileKeywordConstant.TRU64_IPCONFIG_START_KEYWORD;
    
    private static final String TRU64_IPCONFIG_END_KEYWORD = Tru64LogFileKeywordConstant.TRU64_IPCONFIG_END_KEYWORD;
	
	@Override
	public ObjectValue getObjectValue() {
		String beginStr = TRU64_NETSTAT_BEGIN_KEYWORD;
        String endStr = TRU64_NETSTAT_END_KEYWORD;
        
        String beginStr2 = TRU64_IPCONFIG_START_KEYWORD;
        String endStr2 = TRU64_IPCONFIG_END_KEYWORD;
        String ipconfigContent = getLogFileContent(beginStr2, endStr2);
        Hashtable ipconfigHash = new Hashtable();
		Hashtable hashtable = null;
        try {
    		
    		String name = "";
    		String status = "";
    		String ipaddress = "";
			String[] ipconfigLineArr = ipconfigContent.trim().split("\n");
			for (int i = 0; i < ipconfigLineArr.length; i++) {
				String lineArr = ipconfigLineArr[i].trim();
				if (lineArr.indexOf("flags") >= 0) {
					//ee0: flags=4000c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX,RESERVED>
					hashtable = new Hashtable();
					
					String[] tmpData = lineArr.split(":");
					name = tmpData[0].trim();
					
					if (hashtable != null) {
						ipconfigHash.put(name, hashtable);
					}
					
					hashtable.put("name", name);
					if(tmpData[1].indexOf("<")>0&&tmpData[1].indexOf(",")>0){
						status = tmpData[1].substring(tmpData[1].indexOf("<") +1 , tmpData[1].indexOf(",")).trim();
						hashtable.put("status", status);
					}
				}
				if(lineArr.indexOf("inet") >= 0){
					//inet 127.0.0.1 netmask ff000000 ipmtu 4096 
					ipaddress = lineArr.substring(lineArr.indexOf("inet")+4, lineArr.indexOf("netmask")).trim();
					hashtable.put("ipaddress", ipaddress);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        ObjectValue objectValue = new ObjectValue();
        String netstatContent = getLogFileContent(beginStr, endStr);
        Hashtable currentNetstatHash = new Hashtable();	//用于加入内存当中，用于计算流量

        try {
        	Hashtable result = new Hashtable();
        	Vector interfaceVector = new Vector();
            Vector<UtilHdx> utilhdxVector = new Vector<UtilHdx>();
            Vector<AllUtilHdx> allutilhdxVector = new Vector<AllUtilHdx>();
            String ipaddress = getNodeDTO().getIpaddress();
            Calendar date = getCalendarInstance();
            List iflist = new ArrayList();
    		List oldiflist = new ArrayList();
    		
    		Hashtable  netstatHash = new Hashtable();	//用于保存接口Name
    		String[] netstatLineArr = netstatContent.trim().split("\n");
    		String[] netstat_tmpData = null;
    		
    		Float AllInBandwidthUtilHdxFloat = 0f;
			Float AllOutBandwidthUtilHdxFloat = 0f;

    		if (netstatLineArr != null && netstatLineArr.length > 0) {
    			Interfacecollectdata interfacedata = null;

    			// 开始循环网络接口
    			for (int k = 1; k < netstatLineArr.length; k++) {
    				String portDesc = "";         	// Name
    				String mtu = "";				// Mtu			
    				String network = "";			// Network		未使用
    				String address = "";			// Address		未使用
    				String inPackets = "";			// Ipkts		入口流量包	
    				String ierrs = "";				// Ierrs		入口错误包	未使用
    				String outPackets = "";			// Opkts		出口流量包
    				String oerrs = "";				// Oerrs		出口错误包	未使用
    				String coll = "";				// Coll			未使用
    				
    				netstat_tmpData = netstatLineArr[k].trim().split("\\s++");
    				
    				if(netstat_tmpData != null && netstat_tmpData.length>= 9){
    					portDesc = netstat_tmpData[0].trim();
    					mtu = netstat_tmpData[1].trim();
    					network = netstat_tmpData[2].trim();
    					address = netstat_tmpData[3].trim();
    					inPackets = netstat_tmpData[4].trim();
    					ierrs = netstat_tmpData[5].trim();
    					outPackets = netstat_tmpData[6].trim();
    					oerrs = netstat_tmpData[7].trim();
    					coll = netstat_tmpData[8].trim();
    					
    					if(netstatHash.get(portDesc)==null&&!"".equals(netstatHash.get(portDesc))){
    						netstatHash.put(portDesc, portDesc);
    					} else {
    						continue;
    					}
    					Hashtable iphash = (Hashtable) ipconfigHash.get(portDesc);
    					
    					Hashtable ifhash = new Hashtable();
    					Hashtable oldifhash = null;// 用来保存上次采集结果
    					
    					
    					ifhash.put("portDesc", portDesc);
    					ifhash.put("mtu", mtu);
    					ifhash.put("network", network);
    					ifhash.put("address", address);
    					ifhash.put("inPackets", inPackets);
    					ifhash.put("ierrs", ierrs);
    					ifhash.put("outPackets", outPackets);
    					ifhash.put("oerrs", oerrs);
    					ifhash.put("coll", coll);
    					
    					
    					currentNetstatHash.put(portDesc, ifhash);	//保存当前端口流量信息，放进内存中
    					
    					String oldOutPackets = "0";
    					String oldInPackets = "0";
    					String endOutPackets = "0";
    					String endInPackets = "0";
    					
    					String endOutBytes = "0";
    					String endInBytes = "0";
    					
    					Hashtable oldNetstatHash = (Hashtable) ShareData.getTru64netstatData().get(ipaddress);
    					if(oldNetstatHash != null && oldNetstatHash.get(portDesc) != null) {
    						Hashtable oldportDescHash = (Hashtable) oldNetstatHash.get(portDesc);
    						//oldNetstatHash不为空，说明之前已经保存了值
    						oldOutPackets = (String) oldportDescHash.get("outPackets");
    						oldInPackets = (String) oldportDescHash.get("inPackets");
    						
    						endOutPackets = (Long.parseLong(outPackets) - Long.parseLong(oldOutPackets))+ "";
    						endOutBytes = (Long.parseLong(outPackets) - Long.parseLong(oldOutPackets)) / 1024 / 300+ "";
    						
    						endInPackets = (Long.parseLong(inPackets) - Long.parseLong(oldInPackets))+ "";
    						endInBytes = (Long.parseLong(inPackets) - Long.parseLong(oldInPackets)) / 1024 / 300+ "";
    					}
    					
    					// 端口索引
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("index");
    					interfacedata.setSubentity(k  + "");
    					// 端口状态不保存，只作为静态数据放到临时表里
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(k + "");
    					interfacedata.setChname("端口索引");
    					interfaceVector.addElement(interfacedata);
    					
    					// 端口描述
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifDescr");
    					interfacedata.setSubentity(k  + "");
    					// 端口状态不保存，只作为静态数据放到临时表里
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(portDesc);
    					interfacedata.setChname("端口描述2");
    					interfaceVector.addElement(interfacedata);
    					
    					// 端口名称
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifname");
    					interfacedata.setSubentity(k  + "");
    					// 端口状态不保存，只作为静态数据放到临时表里
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					interfacedata.setThevalue(portDesc);
    					interfacedata.setChname("端口描述2");
    					interfaceVector.addElement(interfacedata);
    					
    					// 端口MAC或者IP
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ipmac");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					String ipmac = "";
    					if(iphash!=null && iphash.get("ipaddress")!=null&&!"".equals(iphash.get("ipaddress"))){
    						ipmac = (String) iphash.get("ipaddress");
    					}
    					interfacedata.setThevalue(ipmac);
    					interfacedata.setChname("端口对应的IPMAC");
    					interfaceVector.addElement(interfacedata);
    					
    					// 端口带宽
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifSpeed");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("每秒字节数");
    					interfacedata.setThevalue(mtu);
    					interfacedata.setChname("端口带宽");
    					interfaceVector.addElement(interfacedata);
    					
    					// 当前状态
    					interfacedata = new Interfacecollectdata();
    					interfacedata.setIpaddress(ipaddress);
    					interfacedata.setCollecttime(date);
    					interfacedata.setCategory("Interface");
    					interfacedata.setEntity("ifOperStatus");
    					interfacedata.setSubentity(k  + "");
    					interfacedata.setRestype("static");
    					interfacedata.setUnit("");
    					String status = "up";
    					if(iphash!=null && iphash.get("status")!=null&&!"".equals(iphash.get("status"))){
    						status = (String) iphash.get("status");
    					}
    					interfacedata.setThevalue(status);
    					interfacedata.setChname("当前状态");
    					interfaceVector.addElement(interfacedata);
    					
    					// 端口入口流速
    					UtilHdx utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					String chnameBand = "";
    					utilhdx.setEntity("InBandwidthUtilHdx");
    					utilhdx.setThevalue(endInBytes);
    					utilhdx.setSubentity(k  + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/秒");
    					utilhdx.setChname(k + "端口入口" + "流速");
    					utilhdxVector.addElement(utilhdx);
    					
    					//计算总入口流速
    					AllInBandwidthUtilHdxFloat = AllInBandwidthUtilHdxFloat + Float.parseFloat(endInBytes);
    					
    					// 端口出口流速
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("OutBandwidthUtilHdx");
    					utilhdx.setThevalue(endOutBytes);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/秒");
    					utilhdx.setChname(k + "端口出口" + "流速");
    					utilhdxVector.addElement(utilhdx);
    					
    					//计算总出口流速
    					AllOutBandwidthUtilHdxFloat = AllOutBandwidthUtilHdxFloat + Float.parseFloat(endOutBytes);
    					
    					//入口数据包
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("ifInPkts");
    					utilhdx.setThevalue(endInPackets);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/秒");
    					utilhdx.setChname(k + "端口入口数据包");
    					utilhdxVector.addElement(utilhdx);
    					
    					//出口数据包
    					utilhdx = new UtilHdx();
    					utilhdx.setIpaddress(ipaddress);
    					utilhdx.setCollecttime(date);
    					utilhdx.setCategory("Interface");
    					utilhdx.setEntity("ifOutPkts");
    					utilhdx.setThevalue(endOutPackets);
    					utilhdx.setSubentity(k + "");
    					utilhdx.setRestype("dynamic");
    					utilhdx.setUnit("Kb/秒");
    					utilhdx.setChname(k + "端口出口数据包");
    					utilhdxVector.addElement(utilhdx);
    					
    					iflist.add(ifhash);
    				}
    			}
    		}
    		String AllInBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat));
    		// AllInBandwidthUtilHdx端口入口流速
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
			
            String AllOutBandwidthUtilHdx = String.valueOf(Math.round(AllOutBandwidthUtilHdxFloat));
			// AllOutBandwidthUtilHdx端口出口流速
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
			
            String AllBandwidthUtilHdx = String.valueOf(Math.round(AllInBandwidthUtilHdxFloat) + Math.round(AllOutBandwidthUtilHdxFloat));
			// AllBandwidthUtilHdx端口出口流速
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
    		
    		result.put("iflist", iflist);
    		result.put("interface", interfaceVector);
    		result.put("utilhdx", utilhdxVector);
    		result.put("allutilhdxVector", allutilhdxVector);
    		
    		ShareData.getTru64netstatData().put(ipaddress, currentNetstatHash);
    		objectValue.setObjectValue(result);
    		objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectValue;
	}

}

