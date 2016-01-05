/*
 * Created on 2005-4-7
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.snmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.*;
import com.afunms.polling.PollingEngine;
import com.afunms.config.dao.*;
import com.afunms.config.model.*;

import com.afunms.event.dao.*;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempiflistRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatemputilhdxRtosql;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.HostcpuResultTosql;
import com.gatherResulttosql.HostdiskResultosql;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetHostDatatempSystemRttosql;
import com.gatherResulttosql.NetHostMemoryRtsql;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadHpUnixFile {
	/**
	 * @param hostname
	 */
	private String ipaddress;
	private static int i;

	//private ProcsDao procsManager = new ProcsDao();

	// private EventListDao eventmanager=new EventListDao();
	private Hashtable sendeddata = ShareData.getProcsendeddata();

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public LoadHpUnixFile(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	
	public LoadHpUnixFile() {
		
	}

	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode)
    {
		
		
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		ipaddress=host.getIpAddress();
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(ipaddress);
		if(ipAllData == null)ipAllData = new Hashtable();
		
		Hashtable returnHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		Vector cpuVector = new Vector();
		Vector systemVector = new Vector();
		Vector userVector = new Vector();
		Vector diskVector = new Vector();
		Vector processVector = new Vector();
		// �޸�
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();

		CPUcollectdata cpudata = null;
		Systemcollectdata systemdata = null;
		Usercollectdata userdata = null;
		Processcollectdata processdata = null;

		try {
			// String filename = CommonAppUtil.getAppName() + "unixserver" +
			// File.separator + this.getTheOwnerNode().getIpAddress()+ "-" +
			// getMaxNum(this.getTheOwnerNode().getIpAddress()) +".log";
			// if (getMaxNum(ipaddress) != null){
			String filename = ResourceCenter.getInstance().getSysPath()
					+ "linuxserver/" + ipaddress + ".log";
			File file=new File(filename);
			if(!file.exists()){
				//�ļ�������,������澯
				try{
					createFileNotExistSMS(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			file = null;
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String strLine = null;
			// �����ļ�����
			while ((strLine = br.readLine()) != null) {
				fileContent.append(strLine + "\n");
			}
			isr.close();
			fis.close();
			br.close();
    		try{
    			copyFile(ipaddress,getMaxNum(ipaddress));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		Pattern tmpPt = null;
		Matcher mr = null;
		Calendar date = Calendar.getInstance();

		// ----------------����uptime����--���������---------------------
		try {
			String uptimeContent = "";
			tmpPt = Pattern.compile("(cmdbegin:uptime)(.*)(cmdbegin:netstat)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				uptimeContent = mr.group(2);
			}
			// String[] uptimeLineArr = uptimeContent.split("\n");
			// String[] date_tmpData = null;
			if (uptimeContent != null && uptimeContent.length() > 0) {
				systemdata = new Systemcollectdata();
				systemdata.setIpaddress(ipaddress);
				systemdata.setCollecttime(date);
				systemdata.setCategory("System");
				systemdata.setEntity("SysUptime");
				systemdata.setSubentity("SysUptime");
				systemdata.setRestype("static");
				systemdata.setUnit(" ");
				systemdata.setThevalue(uptimeContent.trim());
				systemVector.addElement(systemdata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ----------------����date����--���������---------------------
		try {
			String dateContent = "";
			tmpPt = Pattern.compile("(cmdbegin:date)(.*)(cmdbegin:uptime)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				dateContent = mr.group(2);
			}
			// String[] dateLineArr = dateContent.split("\n");
			// String[] date_tmpData = null;
			if (dateContent != null && dateContent.length() > 0) {
				systemdata = new Systemcollectdata();
				systemdata.setIpaddress(ipaddress);
				systemdata.setCollecttime(date);
				systemdata.setCategory("System");
				systemdata.setEntity("Systime");
				systemdata.setSubentity("Systime");
				systemdata.setRestype("static");
				systemdata.setUnit(" ");
				systemdata.setThevalue(dateContent.trim());
				systemVector.addElement(systemdata);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ----------------����netstat����--���������---------------------
		List iflist = null;
		try {
			iflist = new ArrayList();
			List oldiflist = new ArrayList();
			String netstatContent = "";
			tmpPt = Pattern.compile("(cmdbegin:netstat)(.*)(cmdbegin:end)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				netstatContent = mr.group(2);
				// System.out.println("========================netstatContent======================");
				//System.out.println(netstatContent + "========================netstatContent======================");
				//SysLogger.info(netstatContent + "========================netstatContent======================");
				// System.out.println("==========================netstatContent====================");
			}
			String[] netstatLineArr = netstatContent.trim().split("\n");
			String[] netstat_tmpData = null;
			//Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipaddress);
			if (ipAllData != null) {
				oldiflist = (List) ipAllData.get("iflist");
			}

			if (netstatLineArr != null && netstatLineArr.length > 0) {
				// System.out.println("-------------------�������-----------------------------");
				Interfacecollectdata interfacedata = null;

				// ��ʼѭ������ӿ�
				for (int k = 1; k < netstatLineArr.length; k++) {
					
					String portDesc = "";         	// Name
					String mtu = "";				// Mtu			
					String network = "";			// Network		δʹ��
					String address = "";			// Address		δʹ��
					String inPackets = "";			// Ipkts		
					String ierrs = "";				// Ierrs		δʹ��
					String outPackets = "";			// Opkts		
					String oerrs = "";				// Oerrs		δʹ��
					String coll = "";				// Coll			δʹ��
					
					
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
						
//						if("0".equals(inPackets) && "0".equals(outPackets)){
//							continue;
//						}
						
						Hashtable ifhash = new Hashtable();
						Hashtable oldifhash = null;// ���������ϴβɼ����
						
						boolean hasOldFlag = false;
						if (oldiflist != null && oldiflist.size() > 0) {
							for(int j = 0 ; j < oldiflist.size(); j++){
								Hashtable oldifhash_per = (Hashtable) oldiflist.get(j);
								if(portDesc.equals(oldifhash_per.get("portDesc"))){
									oldifhash = oldifhash_per;
									hasOldFlag = true;
								}
							}
						}
						if(!hasOldFlag){
							oldifhash = new Hashtable();
							oldifhash.put("portDesc", portDesc);
							oldifhash.put("mtu", mtu);
							oldifhash.put("network", network);
							oldifhash.put("address", address);
							oldifhash.put("inPackets", inPackets);
							oldifhash.put("ierrs", ierrs);
							oldifhash.put("outPackets", outPackets);
							oldifhash.put("oerrs", oerrs);
							oldifhash.put("coll", coll);
						}
						
						ifhash.put("portDesc", portDesc);
						ifhash.put("mtu", mtu);
						ifhash.put("network", network);
						ifhash.put("address", address);
						ifhash.put("inPackets", inPackets);
						ifhash.put("ierrs", ierrs);
						ifhash.put("outPackets", outPackets);
						ifhash.put("oerrs", oerrs);
						ifhash.put("coll", coll);
						
						String oldOutPackets = "0";
						String oldInPackets = "0";
						String endOutPackets = "0";
						String endInPackets = "0";
						
//						String oldOutBytes = "0";
//						String oldInBytes = "0";
						String endOutBytes = "0";
						String endInBytes = "0";
						
						
						if (oldifhash != null && oldifhash.size() > 0) {
							if (oldifhash.containsKey("outPackets")) {
								oldOutPackets = (String) oldifhash.get("outPackets");
							}
							try {
								endOutPackets = (Long.parseLong(outPackets) - Long
										.parseLong(oldOutPackets))
										+ "";
								endOutBytes = (Long.parseLong(outPackets) - Long
										.parseLong(oldOutPackets)) / 1024 / 300
										+ "";
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (oldifhash.containsKey("inPackets")) {
								oldInPackets = (String) oldifhash.get("inPackets");
							}
							try {
								endInPackets = (Long.parseLong(inPackets) - Long
										.parseLong(oldInPackets))
										+ "";
								endInBytes = (Long.parseLong(inPackets) - Long
										.parseLong(oldInPackets)) / 1024 / 300
										+ "";
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						
						//SysLogger.info(endOutBytes + "===========endOutBytes=====" +portDesc);
						//SysLogger.info(endInBytes + "============endInBytes======="+portDesc);
						//SysLogger.info(endOutBytes + "===========endOutBytes==========="+portDesc);
						//SysLogger.info(endInBytes + "============endInBytes======="+portDesc);
						
						// �˿�����
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("index");
						interfacedata.setSubentity(k  + "");
						// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(k + "");
						interfacedata.setChname("�˿�����");
						interfaceVector.addElement(interfacedata);
						// �˿�����
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifDescr");
						interfacedata.setSubentity(k  + "");
						// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(portDesc);
						interfacedata.setChname("�˿�����2");
						interfaceVector.addElement(interfacedata);
						
						
						// �˿�����
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifname");
						interfacedata.setSubentity(k  + "");
						// �˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(portDesc);
						interfacedata.setChname("�˿�����2");
						interfaceVector.addElement(interfacedata);
						
						// �˿ڴ���
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifSpeed");
						interfacedata.setSubentity(k  + "");
						interfacedata.setRestype("static");
						interfacedata.setUnit("ÿ���ֽ���");
						interfacedata.setThevalue(mtu);
						interfacedata.setChname("");
						interfaceVector.addElement(interfacedata);
						// ��ǰ״̬
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifOperStatus");
						interfacedata.setSubentity(k  + "");
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue("up");
						interfacedata.setChname("��ǰ״̬");
						interfaceVector.addElement(interfacedata);
						// ��ǰ״̬
						interfacedata = new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifOperStatus");
						interfacedata.setSubentity(k  + "");
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(1 + "");
						interfacedata.setChname("��ǰ״̬");
						interfaceVector.addElement(interfacedata);
						// �˿��������
						UtilHdx utilhdx = new UtilHdx();
						utilhdx.setIpaddress(ipaddress);
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						String chnameBand = "";
						utilhdx.setEntity("InBandwidthUtilHdx");
						utilhdx.setThevalue(endInBytes);
						utilhdx.setSubentity(k  + "");
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("Kb/��");
						utilhdx.setChname(k + "�˿����" + "����");
						utilhdxVector.addElement(utilhdx);
						
						
						
						// �˿ڳ�������
						utilhdx = new UtilHdx();
						utilhdx.setIpaddress(ipaddress);
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						utilhdx.setEntity("OutBandwidthUtilHdx");
						utilhdx.setThevalue(endOutBytes);
						utilhdx.setSubentity(k + "");
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("Kb/��");
						utilhdx.setChname(k + "�˿ڳ���" + "����");
						utilhdxVector.addElement(utilhdx);
						
						iflist.add(ifhash);
						ifhash = null;
						
						
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			SysLogger.info(e1.getMessage() + "===============================e1.getMessage()");
			SysLogger.info(e1.toString()+"===============================e1.toString();");
		}
		
		System.out.println(iflist.size()+"=================iflist.size()================");

		// ----------------����user����--���������---------------------
		try {
			String userContent = "";
			tmpPt = Pattern.compile("(cmdbegin:memoryend)(.*)(cmdbegin:user)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				userContent = mr.group(2);
			}
			String[] userLineArr = userContent.split("\n");
			String[] user_tmpData = null;
			for (int i = 0; i < userLineArr.length; i++) {
				// user_tmpData = userLineArr[i].split("\\s++");
				String[] result = userLineArr[i].trim().split(":");
				if (result.length > 0) {
					userdata = new Usercollectdata();
					userdata.setIpaddress(ipaddress);
					userdata.setCollecttime(date);
					userdata.setCategory("User");
					userdata.setEntity("Sysuser");
					userdata.setSubentity(result[0]);
					userdata.setRestype("static");
					userdata.setUnit(" ");
					userdata.setThevalue(result[0]);
					userVector.addElement(userdata);
					continue;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// ----------------����uname����--���������---------------------
		try {
			String unameContent = "";
			tmpPt = Pattern.compile("(cmdbegin:uname)(.*)(cmdbegin:date)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				unameContent = mr.group(2);
			}
			String[] unameLineArr = unameContent.split("\n");
			String[] uname_tmpData = null;
			for (int i = 0; i < unameLineArr.length; i++) {
				uname_tmpData = unameLineArr[i].split("\\s++");
				if (uname_tmpData.length == 2) {
					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("operatSystem");
					systemdata.setSubentity("operatSystem");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(uname_tmpData[0]);
					systemVector.addElement(systemdata);

					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("SysName");
					systemdata.setSubentity("SysName");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(uname_tmpData[1]);
					systemVector.addElement(systemdata);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ----------------����mac����--���������---------------------
		/*
		 * try{ String macContent = ""; tmpPt =
		 * Pattern.compile("(cmdbegin:mac)(.*)(cmdbegin:uname)",Pattern.DOTALL);
		 * mr = tmpPt.matcher(fileContent.toString()); if(mr.find()) {
		 * macContent = mr.group(2); } String[] macLineArr =
		 * macContent.split("\n"); String[] mac_tmpData = null; String MAC = "";
		 * for(int i=0; i<macLineArr.length;i++){ mac_tmpData =
		 * macLineArr[i].trim().split("\\s++"); if (mac_tmpData.length==9){
		 * if(mac_tmpData[2].indexOf("link")>=0 && mac_tmpData[3] != null &&
		 * mac_tmpData[3].trim().length()>0){ if (i==0){
		 * MAC=MAC+mac_tmpData[3]+","; }else if (i==macLineArr.length-1){
		 * MAC=MAC+mac_tmpData[3]; }else MAC=MAC+mac_tmpData[3]+","; } } }
		 * systemdata=new Systemcollectdata();
		 * systemdata.setIpaddress(ipaddress); systemdata.setCollecttime(date);
		 * systemdata.setCategory("System"); systemdata.setEntity("MacAddr");
		 * systemdata.setSubentity("MacAddr"); systemdata.setRestype("static");
		 * systemdata.setUnit(" "); systemdata.setThevalue(MAC);
		 * systemVector.addElement(systemdata); }catch(Exception e){
		 * e.printStackTrace(); }
		 */
		// ----------------����cpu����--���������---------------------
		Hashtable proHash = new Hashtable();
		List pro_list = new ArrayList();
		try {
			String cpuContent = "";
			tmpPt = Pattern.compile("(cmdbegin:proc)(.*)(cmdbegin:vmstat)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				cpuContent = mr.group(2);
			}
			String[] cpuLineArr = cpuContent.split("\n");
			String[] cpu_tmpData = null;
			for (int i = 0; i < cpuLineArr.length; i++) {
				cpu_tmpData = cpuLineArr[i].trim().split("\\s++");
				// 0:UID 1:PID 2:PPID 3:C 4:STIME 5:TTY 6:TIME 7:CMD
				if (cpu_tmpData.length == 9) {
					String[] _cpudata = new String[4];
					if (cpu_tmpData[0].equalsIgnoreCase("PID"))
						continue;
					if (cpu_tmpData[1].equalsIgnoreCase("PID"))
						continue;
					_cpudata[0] = cpu_tmpData[1];// PID
					if (cpu_tmpData[7].indexOf(":") >= 0) {
						_cpudata[1] = cpu_tmpData[8];// CMD
						_cpudata[2] = cpu_tmpData[7];// TIME

					} else {
						_cpudata[1] = cpu_tmpData[7] + " " + cpu_tmpData[8];// CMD
						_cpudata[2] = cpu_tmpData[6];// TIME
					}

					_cpudata[3] = "";
					// proHash.put(_cpudata[0], _cpudata);
					pro_list.add(_cpudata);
				} else if (cpu_tmpData.length > 9) {
					String[] _cpudata = new String[4];
					if (cpu_tmpData[0].equalsIgnoreCase("PID"))
						continue;
					if (cpu_tmpData[1].equalsIgnoreCase("PID"))
						continue;
					_cpudata[0] = cpu_tmpData[1];// PID
					String cmdstr = "";
					if (cpu_tmpData[4].indexOf(":") >= 0) {
						_cpudata[2] = cpu_tmpData[6];// TIME
						for (int k = 7; k < cpu_tmpData.length - 1; k++) {
							cmdstr = cmdstr + " " + cpu_tmpData[k];
						}
						_cpudata[1] = cmdstr.trim();// CMD
					} else {
						_cpudata[2] = cpu_tmpData[7];// TIME
						for (int k = 8; k < cpu_tmpData.length - 1; k++) {
							cmdstr = cmdstr + " " + cpu_tmpData[k];
						}
						_cpudata[1] = cmdstr.trim();// CMD
					}

					_cpudata[3] = "";
					// proHash.put(_cpudata[0], _cpudata);
					pro_list.add(_cpudata);
				} else if (cpu_tmpData.length == 8) {
					String[] _cpudata = new String[4];
					if (cpu_tmpData[0].equalsIgnoreCase("PID"))
						continue;
					if (cpu_tmpData[1].equalsIgnoreCase("PID"))
						continue;
					_cpudata[0] = cpu_tmpData[1];// PID
					String cmdStr = cpu_tmpData[7];// CMD
					if (cmdStr.indexOf("<") >= 0) {
						cmdStr = cmdStr.replaceAll("<", "");
					}
					if (cmdStr.indexOf(">") >= 0) {
						cmdStr = cmdStr.replaceAll(">", "");
					}
					_cpudata[1] = cmdStr;

					_cpudata[2] = cpu_tmpData[6];// TIME
					_cpudata[3] = "";
					// proHash.put(_cpudata[0], _cpudata);
					pro_list.add(_cpudata);
				}
				/*
				 * if((cpu_tmpData != null) && (cpu_tmpData.length == 4)){
				 * String[] _cpudata = new String[4]; if
				 * (cpu_tmpData[0].equalsIgnoreCase("PID"))continue; _cpudata[0] =
				 * cpu_tmpData[0];//PID _cpudata[1] = cpu_tmpData[1];//CMD
				 * _cpudata[2] = cpu_tmpData[2];//TIME _cpudata[3] =
				 * cpu_tmpData[3];//MEM proHash.put(_cpudata[0], _cpudata); }
				 */
			}

			// ----------------����cpu����--���������---------------------
			cpuContent = "";
			List procslist = new ArrayList();
			ProcsDao procsdaor=new ProcsDao();
			try{
				procslist = procsdaor.loadByIp(ipaddress);
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				procsdaor.close();
			}
			List procs_list = new ArrayList();
			Hashtable procshash = new Hashtable();
			Vector procsV = new Vector();
			if (procslist != null && procslist.size() > 0) {
				for (int i = 0; i < procslist.size(); i++) {
					Procs procs = (Procs) procslist.get(i);
					procshash.put(procs.getProcname(), procs);
					procsV.add(procs.getProcname());
				}
			}
			/*
			 * float cpuusage = 0.0f;
			 * if(cpuusage>100.0f)cpuusage=(float)(97f+Math.random()*3.0);
			 * cpudata=new CPUcollectdata(); cpudata.setIpaddress(ipaddress);
			 * cpudata.setCollecttime(date); cpudata.setCategory("CPU");
			 * cpudata.setEntity("Utilization");
			 * cpudata.setSubentity("Utilization");
			 * cpudata.setRestype("dynamic"); cpudata.setUnit("%");
			 * cpudata.setThevalue(Float.toString(cpuusage));
			 * cpuVector.addElement(cpudata);
			 */
			systemdata = new Systemcollectdata();
			systemdata.setIpaddress(ipaddress);
			systemdata.setCollecttime(date);
			systemdata.setCategory("System");
			systemdata.setEntity("ProcessCount");
			systemdata.setSubentity("ProcessCount");
			systemdata.setRestype("static");
			systemdata.setUnit(" ");
			systemdata.setThevalue(cpuLineArr.length - 1 + "");
			systemVector.addElement(systemdata);
			// if (proHash != null&& proHash.size()>0){
			if (pro_list != null && pro_list.size() > 0) {
				// Iterator keys=proHash.keySet().iterator();
				// String key="";
				for (int i = 0; i < pro_list.size(); i++) {
					// while(keys.hasNext()){
					// key=keys.next().toString();
					// String[] pro = (String[])proHash.get(key);
					String[] pro = (String[]) pro_list.get(i);
					String vbstring0 = pro[0];// pid
					String vbstring1 = pro[1];// command
					String vbstring2 = "Ӧ�ó���";
					String vbstring3 = "��������";
					String vbstring4 = "";// memsize
					String vbstring5 = pro[2];// cputime
					String vbstring6 = pro[3];// %mem

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("MemoryUtilization");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("dynamic");
					processdata.setUnit("%");
					processdata.setThevalue("");
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Memory");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("static");
					processdata.setUnit("K");
					processdata.setThevalue(pro[3]);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Type");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring2);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Status");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring3);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Name");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring1);
					processVector.addElement(processdata);

					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("CpuTime");
					processdata.setSubentity(vbstring0);
					processdata.setRestype("static");
					processdata.setUnit("��");
					processdata.setThevalue(vbstring5);
					processVector.addElement(processdata);
					/*
					 * //�ж��Ƿ�����Ҫ���ӵĽ��̣���ȡ�õ��б���������ӽ��̣����Vector��ȥ�� if (procshash
					 * !=null && procshash.size()>0){ if
					 * (procshash.containsKey(vbstring1)){
					 * procshash.remove(vbstring1); procsV.remove(vbstring1); } }
					 */

					// �ж��Ƿ�����Ҫ���ӵĽ��̣���ȡ�õ��б���������ӽ��̣����Vector��ȥ��
					if (procsV != null && procsV.size() > 0) {
						if (procsV.contains(vbstring1)) {
							// procshash.remove(vbstring1);
							procsV.remove(vbstring1);
							// �ж��Ѿ����͵Ľ��̶����б����Ƿ��иý���,����,����ѷ����б���ȥ���ö�����Ϣ
							if (sendeddata.containsKey(ipaddress + ":"
									+ vbstring1)) {
								sendeddata.remove(ipaddress + ":" + vbstring1);
							}
							// �жϽ��̶�ʧ�б����Ƿ��иý���,����,��Ӹ��б���ȥ������Ϣ
							Hashtable iplostprocdata = (Hashtable) ShareData
									.getLostprocdata(ipaddress);
							if (iplostprocdata == null)
								iplostprocdata = new Hashtable();
							if (iplostprocdata.containsKey(vbstring1)) {
								iplostprocdata.remove(vbstring1);
								ShareData.setLostprocdata(ipaddress,
										iplostprocdata);
							}

						}
					}

				}

				/*
				 * //�ж�ProcsV�ﻹ��û����Ҫ���ӵĽ��̣����У���˵����ǰû�������ý��̣������������������ý��̣�ͬʱд���¼�
				 * Vector eventtmpV = new Vector(); if (procsV !=null &&
				 * procsV.size()>0){ try{ for(int i=0;i<procsV.size();i++){
				 * Procs procs = (Procs)procshash.get((String)procsV.get(i));
				 * Eventlist event = new Eventlist(); Monitoriplist
				 * monitoriplist =
				 * (Monitoriplist)monitormanager.getByIpaddress(ipaddress);
				 * 
				 * event.setEventtype("host");
				 * event.setEventlocation(ipaddress); event.setManagesign(new
				 * Integer(0)); event.setReportman("monitorpc");
				 * event.setNetlocation(equipmentManager.getByip(monitoriplist.getIpaddress()).getNetlocation());
				 * event.setRecordtime(date); event.setLevel1(new Integer(1));
				 * event.setEquipment(equipmentManager.getByip(monitoriplist.getIpaddress()));
				 * String time = sdf.format(date.getTime());
				 * event.setContent(monitoriplist.getEquipname()+"&"+ipaddress+"&"+time+"����"+procsV.get(i)+"������!&level=1");
				 * eventtmpV.add(event); } if (eventtmpV != null &&
				 * eventtmpV.size()>0) eventmanager.createEventlist(eventtmpV);
				 * }catch(Exception ex){ ex.printStackTrace(); } }
				 */

				// �ж�ProcsV�ﻹ��û����Ҫ���ӵĽ��̣����У���˵����ǰû�������ý��̣������������������ý��̣�ͬʱд���¼�
				Vector eventtmpV = new Vector();
				if (procsV != null && procsV.size() > 0) {
					for (int i = 0; i < procsV.size(); i++) {
						Procs procs = (Procs) procshash.get((String) procsV
								.get(i));
						//Host host = (Host) PollingEngine.getInstance()
							//	.getNodeByIP(ipaddress);
						try {
							Hashtable iplostprocdata = (Hashtable) ShareData
									.getLostprocdata(ipaddress);
							if (iplostprocdata == null)
								iplostprocdata = new Hashtable();
							iplostprocdata.put(procs.getProcname(), procs);
							ShareData
									.setLostprocdata(ipaddress, iplostprocdata);
							EventList eventlist = new EventList();
							// Monitoriplist monitoriplist =
							// (Monitoriplist)monitormanager.getByIpaddress(ipaddress);
							eventlist.setEventtype("poll");
							eventlist.setEventlocation(host.getSysLocation());
							eventlist.setContent(procs.getProcname() + "���̶�ʧ");
							eventlist.setLevel1(1);
							eventlist.setManagesign(0);
							eventlist.setBak("");
							eventlist.setRecordtime(Calendar.getInstance());
							eventlist.setReportman("ϵͳ��ѯ");
							NodeToBusinessDao ntbdao = new NodeToBusinessDao();
							List ntblist = ntbdao.loadByNodeAndEtype(host
									.getId(), "equipment");
							String bids = ",";
							if (ntblist != null && ntblist.size() > 0) {

								for (int k = 0; k < ntblist.size(); k++) {
									NodeToBusiness ntb = (NodeToBusiness) ntblist
											.get(k);
									bids = bids + ntb.getBusinessid() + ",";
								}
							}
							eventlist.setBusinessid(bids);
							eventlist.setNodeid(host.getId());
							eventlist.setOid(0);
							eventlist.setSubtype("host");
							eventlist.setSubentity("proc");
							EventListDao eventlistdao = new EventListDao();
							eventlistdao.save(eventlist);
							eventtmpV.add(eventlist);
							// �����ֻ����Ų�д�¼��������澯
							createSMS(procs);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// if (eventtmpV != null && eventtmpV.size()>0)
					// eventmanager.createEventlist(eventtmpV);
				}

			}

			// ----------------����memory����--���������---------------------

			String memory_Content = "";
			tmpPt = Pattern.compile("(cmdbegin:memory)(.*)(cmdbegin:vmstat)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				try {
					memory_Content = mr.group(2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// String[] memory_LineArr = memory_Content.split("\n");
			// String[] memory_tmpData = null;
			// Vector memoryVector=new Vector();
			// Memorycollectdata memorydata=null;
			int allPhysicalMemory = 0;
			/*
			 * try{ for(int i=1; i<memory_LineArr.length;i++) { memory_tmpData =
			 * memory_LineArr[i].split("\\s++"); if((memory_tmpData != null)) {
			 * if (memory_tmpData[0]!=null &&
			 * memory_tmpData[0].equalsIgnoreCase("Good")){ //Memory
			 * allPhysicalMemory = Integer.parseInt(memory_tmpData[3]); for(int
			 * j=0;j<memory_tmpData.length;j++){
			 * //System.out.println(memory_tmpData[j]+"==============="); } } } }
			 * }catch(Exception e){ e.printStackTrace(); }
			 */
			// ----------------����vmstat����--���������---------------------
			String vmstat_Content = "";
			// tmpPt =
			// Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:swapinfo)",Pattern.DOTALL);
			tmpPt = Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:mac)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				try {
					vmstat_Content = mr.group(2);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			String[] vmstat_LineArr = null;
			String[] vmstat_tmpData = null;
			int freePhysicalMemory = 0;
			// Vector memoryVector=new Vector();
			// Memorycollectdata memorydata=null;

			try {
				vmstat_LineArr = vmstat_Content.split("\n");

				for (int i = 1; i < vmstat_LineArr.length; i++) {
					vmstat_tmpData = vmstat_LineArr[i].trim().split("\\s++");
					if ((vmstat_tmpData != null && vmstat_tmpData.length == 18)) {
						if (vmstat_tmpData[0] != null
								&& !vmstat_tmpData[0].equalsIgnoreCase("r")) {
							// freeMemory
							freePhysicalMemory = Integer
									.parseInt(vmstat_tmpData[4]) * 4 / 1024;
							System.out.println("freePhysicalMemory--------"
									+ freePhysicalMemory);
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ----------------����swapinfo����--���������---------------------

			String swapinfo_Content = "";
			// tmpPt =
			// Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:swapinfo)",Pattern.DOTALL);//yangjun
			// add
			// tmpPt =
			// Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:mac)",Pattern.DOTALL);
			tmpPt = Pattern.compile("(cmdbegin:swapinfo)(.*)(cmdbegin:uname)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				try {
					swapinfo_Content = mr.group(2);
					// System.out.println(swapinfo_Content);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// String[] swapinfo_LineArr = null;
			// String[] swapinfo_tmpData = null;
			// int freeswapinfoMemory =0;
			int allswapMemory = 0;
			int usedswapMemory = 0;
			float usedswapPerc = 0;
			// Vector memoryVector=new Vector();
			// Memorycollectdata memorydata=null;
			try {
				vmstat_LineArr = swapinfo_Content.split("\n");
				for (int i = 1; i < vmstat_LineArr.length; i++) {
					// System.out.println(vmstat_LineArr[i].trim());
					vmstat_tmpData = vmstat_LineArr[i].trim().split("\\s++");
					if ((vmstat_tmpData != null)) {
						System.out.println(vmstat_tmpData[0]
								+ "------vmstat_tmpData[0]-------"
								+ vmstat_tmpData[0]);
						if (vmstat_tmpData[0] != null
								&& vmstat_tmpData[0].equalsIgnoreCase("dev")) {
							// swapMemory
							allswapMemory = Integer.parseInt(vmstat_tmpData[1]) / 1024;
						}
						if(vmstat_tmpData[0] != null
								&& vmstat_tmpData[0].equalsIgnoreCase("reserve")){
							usedswapMemory = Integer.parseInt(vmstat_tmpData[2]) / 1024;
						}

					}
				}
//				usedswapPerc = Integer.parseInt(vmstat_tmpData[4]
//				                   							.substring(0,
//				                   									vmstat_tmpData[4].length() - 1));
				usedswapPerc = Float.parseFloat(Integer
						.toString(usedswapMemory))
						* 100
						/ Float.parseFloat(Integer.toString(allswapMemory));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ----------------����lsps����--���������---------------------

			String lsps_Content = "";
			tmpPt = Pattern.compile("(cmdbegin:lsps)(.*)(cmdbegin:mac)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				lsps_Content = mr.group(2);
			}
			String[] lsps_LineArr = lsps_Content.split("\n");
			String[] lsps_tmpData = null;
			int allSwapMemory = 0;
			int usedPercent = 0;
			/*
			 * for(int i=1; i<lsps_LineArr.length;i++) { lsps_tmpData =
			 * lsps_LineArr[i].trim().split("\\s++"); if((lsps_tmpData != null &&
			 * lsps_tmpData.length==2)) { String allSwapStr = lsps_tmpData[0];
			 * String swapUsedPer = lsps_tmpData[1]; usedPercent =
			 * Integer.parseInt(swapUsedPer.substring(0,swapUsedPer.length()-1));
			 * allSwapMemory =
			 * Integer.parseInt(allSwapStr.substring(0,allSwapStr.length()-2)); } }
			 */
			Vector memoryVector = new Vector();
			String[] tmpData = null;

			Memorycollectdata memorydata = null;

			// ----------------�����ڴ�--���������---------------------
			String mContent = "";
			// String diskLabel;
			tmpPt = Pattern.compile("(cmdbegin:end)(.*)(cmdbegin:memory)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				mContent = mr.group(2);
			}
			String[] mLineArr = mContent.split("\n");
			// String[] memData = null;
			int usedPhysicalMemory = 0;
			// tmpPt =
			// Pattern.compile("(^/[\\w/#]*)(\\s++)(\\d++)(\\s++)((\\d++)(\\s++)){2}+(\\d++)%");
			// Diskcollectdata mdata=null;
			// List allSyslogList = new ArrayList();
			for (int i = 1; i < mLineArr.length; i++) {
				try {
					tmpData = mLineArr[i].trim().split("\\s++");
					if ((tmpData != null)) {
						if (tmpData.length > 3) {

							String memname = tmpData[0];

							if (memname.equalsIgnoreCase("Memory:"
									.toLowerCase())) {
								int oenValue = Integer.parseInt(tmpData[1]
										.substring(0, tmpData[1].length() - 1)) / 1024;
								int twoValue = Integer.parseInt(tmpData[4]
										.substring(0, tmpData[4].length() - 1)) / 1024;
								freePhysicalMemory = (Integer
										.parseInt(tmpData[7].substring(0,
												tmpData[7].length() - 1)) + Integer
										.parseInt(tmpData[4].substring(0,
												tmpData[4].length() - 1))) / 1024;
								allPhysicalMemory = oenValue
										+ freePhysicalMemory;
								usedPhysicalMemory = oenValue + twoValue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			float PhysicalMemUtilization = 0;
			try {
				PhysicalMemUtilization = Float.parseFloat(Integer
						.toString(allPhysicalMemory - freePhysicalMemory))
						* 100
						/ Float.parseFloat(Integer.toString(allPhysicalMemory));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ----------------�����ڴ�--���������---------------------
			String _mContent = "";
			// String diskLabel;
			tmpPt = Pattern
					.compile("(cmdbegin:memory)(.*)(cmdbegin:memoryend)",
							Pattern.DOTALL);
			// // tmpPt =
			// //
			// Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:mac)",Pattern.DOTALL);
			// mr = tmpPt.matcher(fileContent.toString());
			// if (mr.find()) {
			// _mContent = mr.group(2);
			// }
			// String[] _mLineArr = _mContent.trim().split("\n");
			// // String[] _memData = null;
			// // int _usedPhysicalMemory =0;
			// System.out.println(ipaddress + "==============" +
			// _mLineArr.length);
			//
			// if (_mLineArr.length == 1) {
			// // System.out.println(_mLineArr[0]);
			// try {
			// tmpData = _mLineArr[0].trim().split("\\s++");
			// if ((tmpData != null)) {
			// String memname = tmpData[0];
			// System.out.println(ipaddress + "=============="
			// + memname);
			// if (memname.equalsIgnoreCase("Total".toLowerCase())) {
			// allPhysicalMemory = Integer
			// .parseInt(tmpData[tmpData.length - 2]);
			// } else {
			// if (tmpData.length > 6)
			// allPhysicalMemory = Integer
			// .parseInt(tmpData[6]) / 1024;
			//
			// }
			// // System.out.println(ipaddress+"-----"+tmpData[6]);
			// }
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
			// } else {
			// for (int i = 1; i < _mLineArr.length; i++) {
			//
			// try {
			// tmpData = _mLineArr[i].trim().split("\\s++");
			// if ((tmpData != null)) {
			// if (tmpData.length > 3) {
			//
			// String memname = tmpData[0];
			// if (memname.equalsIgnoreCase("Total"
			// .toLowerCase())) {
			// allPhysicalMemory = Integer
			// .parseInt(tmpData[tmpData.length - 2]);
			// // System.out.println(ipaddress+"-----"+allPhysicalMemory);
			// break;
			// }
			// if (memname.equalsIgnoreCase("Physmem"
			// .toLowerCase())) {
			// allPhysicalMemory = Integer
			// .parseInt(tmpData[2]) / 1024;
			// // System.out.println(ipaddress+"-----"+tmpData[2]);
			// // System.out.println(mLineArr);
			// } else if (memname.equalsIgnoreCase("Freemem"
			// .toLowerCase())) {
			// freePhysicalMemory = Integer
			// .parseInt(tmpData[2]) / 1024;
			// // System.out.println(mLineArr);
			// } else if (memname.equalsIgnoreCase("Used"
			// .toLowerCase())) {
			// usedPhysicalMemory = Integer
			// .parseInt(tmpData[2]) / 1024;
			// // System.out.println(mLineArr);
			// }
			// }
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
			// }
			// // float PhysicalMemUtilization =0;
			// try {
			// PhysicalMemUtilization = Float.parseFloat(Integer
			// .toString(allPhysicalMemory - freePhysicalMemory))
			// * 100
			// / Float.parseFloat(Integer.toString(allPhysicalMemory));
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			System.out.println(allPhysicalMemory + "----------------"
					+ freePhysicalMemory);
			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Float.toString(allPhysicalMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsedSize");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");// (allPhysicalMemory-freePhysicalMemory)
			memorydata.setThevalue(Integer.toString(allPhysicalMemory
					- freePhysicalMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(Float.toString(PhysicalMemUtilization));
			memoryVector.addElement(memorydata);

			
			//�����ڴ淧ֵ�Ƚ�
			Vector phymemV = new Vector();
			phymemV.add(memorydata);				
		    Hashtable collectHash = new Hashtable();
			collectHash.put("physicalmem", phymemV);				
			//�������ڴ�ֵ���и澯���
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "hpunix","physicalmemory");
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					//�������ڴ�ֵ���и澯���
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.updateData(host,collectHash,"host","hpunix",alarmIndicatorsnode);
					//}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    collectHash=null;
		    phymemV=null;
			
			
			
			
			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Integer.toString(allswapMemory));
			memoryVector.addElement(memorydata);
			

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsdeSize");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("static");
			memorydata.setUnit("M");
			memorydata.setThevalue(Integer.toString(usedswapMemory));
			memoryVector.addElement(memorydata);

			memorydata = new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("SwapMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(Float.toString(usedswapPerc));
			memoryVector.addElement(memorydata);

			

			//�����ڴ淧ֵ�Ƚ�
		    phymemV = new Vector();
			phymemV.add(memorydata);				
		    collectHash = new Hashtable();
			collectHash.put("swapmem", phymemV);				
			//�������ڴ�ֵ���и澯���
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "hpunix","swapmemory");
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					//�������ڴ�ֵ���и澯���
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.updateData(host,collectHash,"host","hpunix",alarmIndicatorsnode);
					//}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    collectHash=null;
		    phymemV=null;
			
			
			// ----------------����disk����--���������---------------------
			// System.out.println(ipaddress+"----------------------disk");
			String diskContent = "";
			String diskLabel;
			tmpPt = Pattern.compile("(cmdbegin:disk)(.*)(cmdbegin:cpu)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				diskContent = mr.group(2);
			}
			String[] diskLineArr = diskContent.trim().split("\n");

			String[] tmpData1 = null;
			String[] tmpData2 = null;
			String[] tmpData3 = null;
			// tmpPt =
			// Pattern.compile("(^/[\\w/#]*)(\\s++)(\\d++)(\\s++)((\\d++)(\\s++)){2}+(\\d++)%");
			Diskcollectdata diskdata = null;
			int pi = 0;
			for (int i = 0; i < diskLineArr.length / 4; i++) {
				pi = i;
				try {
					tmpData = diskLineArr[pi * 4].split("\\s++");
					tmpData1 = diskLineArr[pi * 4 + 1].split("\\s++");
					tmpData2 = diskLineArr[pi * 4 + 2].split("\\s++");
					tmpData3 = diskLineArr[pi * 4 + 3].split("\\s++");
					// i=i+3;

					// diskLabel = tmpData3[0];
					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("Utilization");// ���ðٷֱ�
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");
					diskdata.setUnit("%");

					diskdata.setThevalue(Float.toString(Float
							.parseFloat(tmpData3[1])));
					diskVector.addElement(diskdata);

					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("AllSize");// �ܿռ�
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");

					int allblocksize = 0;
					try {
						allblocksize = Integer.parseInt(tmpData[4]);
					} catch (Exception e) {
						try {
							allblocksize = Integer.parseInt(tmpData[3]);
						} catch (Exception ex) {

						}
					}
					float allsize = 0.0f;
					allsize = allblocksize * 1.0f / 1024;
					if (allsize >= 1024.0f) {
						allsize = allsize / 1024;
						diskdata.setUnit("G");
					} else {
						diskdata.setUnit("M");
					}

					diskdata.setThevalue(Float.toString(allsize));
					diskVector.addElement(diskdata);

//					yangjun 
					try {
						String diskinc = "0.0";
						float pastutil = 0.0f;
						Vector disk_v = (Vector)ipAllData.get("disk");
						if (disk_v != null && disk_v.size() > 0) {
							for (int si = 0; si < disk_v.size(); si++) {
								Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
								if((tmpData[0]).equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
									pastutil = Float.parseFloat(disk_data.getThevalue());
								}
							}
						} else {
							pastutil = Float.parseFloat(tmpData3[1]);
						}
						if (pastutil == 0) {
							pastutil = Float
							.parseFloat(tmpData3[1]);
						}
						if(Float
								.parseFloat(tmpData3[1])-pastutil>0){
							diskinc = (Float
									.parseFloat(tmpData3[1])-pastutil)+"";
						}
						diskdata = new Diskcollectdata();
						diskdata.setIpaddress(ipaddress);
						diskdata.setCollecttime(date);
						diskdata.setCategory("Disk");
						diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
						diskdata.setSubentity(tmpData[0]);
						diskdata.setRestype("dynamic");
						diskdata.setUnit("%");
						diskdata.setThevalue(diskinc);
						diskVector.addElement(diskdata);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//
					
					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("UsedSize");// ʹ�ô�С
					diskdata.setSubentity(tmpData[0]);
					diskdata.setRestype("static");

					int UsedintSize = 0;
					UsedintSize = Integer.parseInt(tmpData2[1]);
					float usedfloatsize = 0.0f;
					usedfloatsize = UsedintSize * 1.0f / 1024;
					if (usedfloatsize >= 1024.0f) {
						usedfloatsize = usedfloatsize / 1024;
						diskdata.setUnit("G");
					} else {
						diskdata.setUnit("M");
					}
					diskdata.setThevalue(Float.toString(usedfloatsize));
					diskVector.addElement(diskdata);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			 //���д��̸澯���
		    //SysLogger.info("### ��ʼ���м������Ƿ�澯### ... ###");
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "hpunix");
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					//SysLogger.info("alarmIndicatorsnode name ======"+alarmIndicatorsnode.getName());
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskperc")){
						
						CheckEventUtil checkutil = new CheckEventUtil();
					    checkutil.checkDisk(host,diskVector,alarmIndicatorsnode);
					    break;
					}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			
			
			
			// System.out.println(ipaddress+"----------------cpu");
			// ----------------����cpu����--���������---------------------
			try {
				String cpu_Content = "";
				tmpPt = Pattern.compile("(cmdbegin:cpu)(.*)(cmdbegin:proc)",
						Pattern.DOTALL);
				mr = tmpPt.matcher(fileContent.toString());
				if (mr.find()) {
					cpu_Content = mr.group(2);
				}
				String[] cpu_LineArr = cpu_Content.split("\n");
				String[] tmpcpuData = null;
				// tmpPt =
				// Pattern.compile("(^/[\\w/#]*)(\\s++)(\\d++)(\\s++)((\\d++)(\\s++)){2}+(\\d++)%");
				// Diskcollectdata diskdata=null;
				for (int i = 1; i < cpu_LineArr.length; i++) {
					tmpData = cpu_LineArr[i].split("\\s++");
					if ((tmpData != null)
							&& ((tmpData.length == 5) || (tmpData.length == 6))) {
						if (tmpData[0] != null
								&& tmpData[0].equalsIgnoreCase("Average")) {
							// System.out.println(tmpData[0]+":"+tmpData[4]+"----------------");
							cpudata = new CPUcollectdata();
							cpudata.setIpaddress(ipaddress);
							cpudata.setCollecttime(date);
							cpudata.setCategory("CPU");
							cpudata.setEntity("Utilization");
							cpudata.setSubentity("Utilization");
							cpudata.setRestype("dynamic");
							cpudata.setUnit("%");
							cpudata.setThevalue(Float.toString(100 - Float
									.parseFloat(tmpData[4])));
							cpuVector.addElement(cpudata);
							System.out
									.println("----------------CU---------------------==="
											+ Float.toString(100 - Float
													.parseFloat(tmpData[4])));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//��CPUֵ���и澯���
	   	    collectHash = new Hashtable();
			collectHash.put("cpu", cpuVector);
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "hpunix","cpu");
				for(int k = 0 ; k < list.size() ; k ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
					//��CPUֵ���и澯���
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.updateData(host,collectHash,"host","hpunix",alarmIndicatorsnode);
					//}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			
			
			

			// ----------------�����û���½��ʷ����--���������---------------------
			String syslogContent = "";
			// String diskLabel;
			tmpPt = Pattern.compile("(cmdbegin:end)(.*)(cmdbegin:syslog)",
					Pattern.DOTALL);
			mr = tmpPt.matcher(fileContent.toString());
			if (mr.find()) {
				syslogContent = mr.group(2);
			}
			String[] syslogLineArr = syslogContent.split("\n");
			String[] syslogData = null;
			// tmpPt =
			// Pattern.compile("(^/[\\w/#]*)(\\s++)(\\d++)(\\s++)((\\d++)(\\s++)){2}+(\\d++)%");
			Diskcollectdata syslogdata = null;
			List allSyslogList = new ArrayList();
			for (int i = 1; i < syslogLineArr.length; i++) {
				try {
					tmpData = syslogLineArr[i].split("\\s++");
					if ((tmpData != null)) {
						List sysloglist = new ArrayList();
						if (tmpData.length == 5) {
							// ϵͳ��������
							String username = tmpData[0];
							String tools = tmpData[1];
							String timedetail = tmpData[2] + tmpData[3]
									+ tmpData[4];
							sysloglist.add(username);
							sysloglist.add(tools);
							sysloglist.add("");
							sysloglist.add(timedetail);
							allSyslogList.add(sysloglist);
						} else if (tmpData.length == 4) {
							// ϵͳ��������
							String username = tmpData[0];
							String tools = tmpData[1];
							String timedetail = tmpData[2] + tmpData[3];
							sysloglist.add(username);
							sysloglist.add(tools);
							sysloglist.add("");
							sysloglist.add(timedetail);
							allSyslogList.add(sysloglist);
						} else if (tmpData.length > 7) {
							// ��������
							String username = tmpData[0];
							String tools = tmpData[1];
							String ip = tmpData[2];
							String timedetail = "";
							for (int k = 3; k < tmpData.length; k++) {
								timedetail = timedetail + " " + tmpData[k];
							}
							sysloglist.add(username);
							sysloglist.add(tools);
							sysloglist.add(ip);
							sysloglist.add(timedetail);
							allSyslogList.add(sysloglist);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try{
				//deleteFile(ipaddress);
			}catch(Exception e){
				e.printStackTrace();
			}
			if (diskVector != null && diskVector.size() > 0)
			{
				returnHash.put("disk", diskVector);
				
				 //�Ѳɼ��������sql
			    HostdiskResultosql tosql=new HostdiskResultosql();
			    tosql.CreateResultTosql(returnHash, host.getIpAddress());
		
			    HostDatatempDiskRttosql temptosql=new HostDatatempDiskRttosql();
			    temptosql.CreateResultTosql(returnHash, host);
			    tosql=null;
			    temptosql=null;
			}
			if (cpuVector != null && cpuVector.size() > 0)
			{
				returnHash.put("cpu", cpuVector);
				
				HostcpuResultTosql rtosql=new HostcpuResultTosql();
				rtosql.CreateLinuxResultTosql(returnHash, host.getIpAddress());
				
				 NetHostDatatempCpuRTosql totempsql=new NetHostDatatempCpuRTosql();
				 totempsql.CreateResultTosql(returnHash, host);
				 totempsql=null;
			}
				
			if (memoryVector != null && memoryVector.size() > 0)
			{
				returnHash.put("memory", memoryVector);
				
				//�Ѳɼ��������sql
			    HostPhysicalMemoryResulttosql  tosql=new HostPhysicalMemoryResulttosql();
			    tosql.CreateResultTosql(returnHash, host.getIpAddress());
			    NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
			    totempsql.CreateResultTosql(returnHash, host);
		    }
			if (userVector != null && userVector.size() > 0)
			{
				returnHash.put("user", userVector);
				
				HostDatatempUserRtosql tosql=new HostDatatempUserRtosql();
				tosql.CreateResultTosql(returnHash, host);
			}
			if (processVector != null && processVector.size() > 0)
			{
				returnHash.put("process", processVector);
				
				//�ѽ������sql
				HostDatatempProcessRtTosql temptosql=new HostDatatempProcessRtTosql();
				temptosql.CreateResultTosql(returnHash, host);
			}
			if (systemVector != null && systemVector.size() > 0)
			{
				returnHash.put("system", systemVector);
				
				NetHostDatatempSystemRttosql tosql=new NetHostDatatempSystemRttosql();
				tosql.CreateResultTosql(returnHash, host);
			}
			if (allSyslogList != null && allSyslogList.size() > 0)
			{
				returnHash.put("syslog", allSyslogList);
				
				
			}
			
			if (iflist != null && iflist.size() > 0)
			{
				returnHash.put("iflist", iflist);
				
				HostDatatempiflistRtosql tosql=new HostDatatempiflistRtosql();
				tosql.CreateResultTosql(returnHash, host);
			}
			if (utilhdxVector != null && utilhdxVector.size() > 0)
			{
				returnHash.put("utilhdx", utilhdxVector);
				
				HostDatatemputilhdxRtosql tosql=new HostDatatemputilhdxRtosql();
				tosql.CreateResultTosql(returnHash, host);
			}
			if (interfaceVector != null && interfaceVector.size() > 0)
			{
				returnHash.put("interface", interfaceVector);
				
				HostDatatempinterfaceRtosql tosql=new HostDatatempinterfaceRtosql();
				tosql.CreateResultTosql(returnHash, host);
			}
			
			
			 ShareData.getSharedata().put(host.getIpAddress(), returnHash);
			
			return returnHash;
		} finally {
		}
	}

	public String getMaxNum(String ipAddress) {
		String maxStr = null;
		File logFolder = new File(ResourceCenter.getInstance().getSysPath()
				+ "/linuxserver/");
		String[] fileList = logFolder.list();

		for (int i = 0; i < fileList.length; i++) // ��һ�����µ��ļ�
		{
			if (!fileList[i].startsWith(ipAddress))
				continue;

			return ipAddress;
		}
		return maxStr;
	}

	   public void deleteFile(String ipAddress){

			try
			{
				File delFile = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/" + ipAddress + ".log");
			System.out.println("###��ʼɾ���ļ���"+delFile);
			delFile.delete();
			System.out.println("###�ɹ�ɾ���ļ���"+delFile);
			}
			catch(Exception e)		
			{}
   }
   public void copyFile(String ipAddress,String max){
	try   { 
		String currenttime = SysUtil.getCurrentTime();
		currenttime = currenttime.replaceAll("-", "");
		currenttime = currenttime.replaceAll(" ", "");
		currenttime = currenttime.replaceAll(":", "");
		String ipdir = ipAddress.replaceAll("\\.", "-");
		String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver_bak/"+ipdir;
		File file=new File(filename);
		if(!file.exists())file.mkdir();
       String cmd   =   "cmd   /c   copy   "+ResourceCenter.getInstance().getSysPath() + "linuxserver\\" + ipAddress + ".log"+" "+ResourceCenter.getInstance().getSysPath() + "linuxserver_bak\\" +ipdir+"\\"+ ipAddress+"-" +currenttime+ ".log";             
       //SysLogger.info(cmd);
       Process   child   =   Runtime.getRuntime().exec(cmd);   
     }catch (IOException e){    
       e.printStackTrace();
   }
   }

	public void createSMS(Procs procs) {
		Procs lastprocs = null;
		// ��������
		procs.setCollecttime(Calendar.getInstance());
		// ���Ѿ����͵Ķ����б����õ�ǰ��PROC�Ѿ����͵Ķ���
		lastprocs = (Procs) sendeddata.get(procs.getIpaddress() + ":"
				+ procs.getProcname());
		// try{
		// if (lastprocs==null){
		// //�ڴ��в����� ,����û��������,�򷢶���
		// Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
		// Smscontent smscontent = new Smscontent();
		// String time = sdf.format(procs.getCollecttime().getTime());
		// smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&���̶�ʧ&level=2");
		// //���Ͷ���
		// Vector tosend = new Vector();
		// tosend.add(smscontent);
		// smsmanager.sendSmscontent(tosend);
		// //�Ѹý�����Ϣ��ӵ��Ѿ����͵Ľ��̶����б���,��IP:��������Ϊkey
		// sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);
		// }else{
		// //���Ѿ����͵Ķ����б�������IP��PROC����
		// //���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// Date last = null;
		// Date current = null;
		// Calendar sendcalen = (Calendar)lastprocs.getCollecttime();
		// Date cc = sendcalen.getTime();
		// String tempsenddate = formatter.format(cc);
		//		 			
		// Calendar currentcalen = (Calendar)procs.getCollecttime();
		// cc = currentcalen.getTime();
		// last = formatter.parse(tempsenddate);
		// String currentsenddate = formatter.format(cc);
		// current = formatter.parse(currentsenddate);
		//		 			
		// long subvalue = current.getTime()-last.getTime();
		//		 			
		// if (subvalue/(1000*60*60*24)>=1){
		// //����һ�죬���ٷ���Ϣ
		// Smscontent smscontent = new Smscontent();
		// String time = sdf.format(procs.getCollecttime().getTime());
		// Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
		// if (equipment == null){
		// return;
		// }else
		// smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&���̶�ʧ&level=2");
		//			 			
		// //���Ͷ���
		// Vector tosend = new Vector();
		// tosend.add(smscontent);
		// smsmanager.sendSmscontent(tosend);
		// //�Ѹý�����Ϣ��ӵ��Ѿ����͵Ľ��̶����б���,��IP:��������Ϊkey
		// sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);
		// }else{
		// //û����һ��,��ֻд�¼�
		// Vector eventtmpV = new Vector();
		// Eventlist event = new Eventlist();
		// Monitoriplist monitoriplist =
		// (Monitoriplist)monitormanager.getByIpaddress(procs.getIpaddress());
		// event.setEventtype("host");
		// event.setEventlocation(procs.getIpaddress());
		// event.setManagesign(new Integer(0));
		// event.setReportman("monitorpc");
		// event.setRecordtime(Calendar.getInstance());
		// event.setLevel1(new Integer(1));
		// event.setEquipment(equipmentManager.getByip(monitoriplist.getIpaddress()));
		// event.setNetlocation(equipmentManager.getByip(monitoriplist.getIpaddress()).getNetlocation());
		// String time = sdf.format(Calendar.getInstance().getTime());
		// event.setContent(monitoriplist.getEquipname()+"&"+monitoriplist.getIpaddress()+"&"+time+"����"+procs.getProcname()+"��ʧ&level=1");
		// eventtmpV.add(event);
		// try{
		// eventmanager.createEventlist(eventtmpV);
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// }
	}
	
	public void createFileNotExistSMS(String ipaddress){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
			Calendar date=Calendar.getInstance();
			try{
				Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
				if(host == null)return;
				
				if (!sendeddata.containsKey(ipaddress+":file:"+host.getId())){
					//�����ڣ��������ţ�������ӵ������б���
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("3");
					smscontent.setObjid(host.getId()+"");
					smscontent.setMessage(host.getAlias()+" ("+host.getIpAddress()+")"+"����־�ļ��޷���ȷ�ϴ������ܷ�����");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("host");
					smscontent.setSubentity("ftp");
					smscontent.setIp(host.getIpAddress());//���Ͷ���
					SmscontentDao smsmanager=new SmscontentDao();
					smsmanager.sendURLSmscontent(smscontent);	
					sendeddata.put(ipaddress+":file"+host.getId(),date);		 					 				
				}else{
					//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
					Calendar formerdate =(Calendar)sendeddata.get(ipaddress+":file:"+host.getId());		 				
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date last = null;
					Date current = null;
					Calendar sendcalen = formerdate;
					Date cc = sendcalen.getTime();
					String tempsenddate = formatter.format(cc);
	 			
					Calendar currentcalen = date;
					cc = currentcalen.getTime();
					last = formatter.parse(tempsenddate);
					String currentsenddate = formatter.format(cc);
					current = formatter.parse(currentsenddate);
	 			
					long subvalue = current.getTime()-last.getTime();			 			
					if (subvalue/(1000*60*60*24)>=1){
						//����һ�죬���ٷ���Ϣ
						Smscontent smscontent = new Smscontent();
						String time = sdf.format(date.getTime());
						smscontent.setLevel("3");
						smscontent.setObjid(host.getId()+"");
						smscontent.setMessage(host.getAlias()+" ("+host.getIpAddress()+")"+"����־�ļ��޷���ȷ�ϴ������ܷ�����");
						smscontent.setRecordtime(time);
						smscontent.setSubtype("host");
						smscontent.setSubentity("ftp");
						smscontent.setIp(host.getIpAddress());//���Ͷ���
						SmscontentDao smsmanager=new SmscontentDao();
						smsmanager.sendURLSmscontent(smscontent);
						//�޸��Ѿ����͵Ķ��ż�¼	
						sendeddata.put(ipaddress+":file:"+host.getId(),date);	
					}	
				}	 			 			 			 			 	
			}catch(Exception e){
				e.printStackTrace();
			}
	 	}
	public static void main(String[] args){
		LoadHpUnixFile loadHpUnixFile = new LoadHpUnixFile("143.24.16.13");
		//loadHpUnixFile.getTelnetMonitorDetail();
	}

}
