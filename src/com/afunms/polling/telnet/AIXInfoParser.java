package com.afunms.polling.telnet;

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
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ReadErrptlog;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.Errptlog;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.config.model.Procs;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.om.UtilHdx;

public class AIXInfoParser
{

	public static void main(String[] args)
	{
		
		String cpuperfContent  = "AIX appserv 3 5 0006B30FD600    04/13/10\n"+

"System configuration: lcpu=8  mode=Capped \n\n" +


"12:54:29    %usr    %sys    %wio   %idle   physc\n"+

"12:54:30       0       0       0     100    4.01\n" +
"12:54:31       0       0       0     100    4.00\n"+
"12:54:32       0       0       0     100    4.00\n"+

"\nAverage        0       0       0     100    4.00";
		
		
		String[] cpuperfLineArr = cpuperfContent.split("\n");
		
		String[] diskperf_tmpData = null;
		
		for (int i = 0; i < cpuperfLineArr.length; i++)
		{
			diskperf_tmpData = cpuperfLineArr[i].trim().split("\\s++");
			if (diskperf_tmpData != null && diskperf_tmpData.length == 6)
			{

				Hashtable cpuperfhash = new Hashtable();
				if (diskperf_tmpData[0].trim().equalsIgnoreCase("Average"))
				{
					cpuperfhash.put("%usr", diskperf_tmpData[1].trim());
					cpuperfhash.put("%sys", diskperf_tmpData[2].trim());
					cpuperfhash.put("%wio", diskperf_tmpData[3].trim());
					cpuperfhash.put("%idle", diskperf_tmpData[4].trim());
					cpuperfhash.put("physc", diskperf_tmpData[5].trim());
					
					System.out.println(cpuperfhash);
				}
			}
		}	
	}
	
	
	
	
	public static Hashtable getTelnetMonitorDetail(Wrapper telnet)
	{		
		//yangjun
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(telnet.getHost());
		if(ipAllData == null)ipAllData = new Hashtable();
		
		Hashtable returnHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		Vector cpuVector = new Vector();
		Vector systemVector = new Vector();
		Vector userVector = new Vector();
		Vector diskVector = new Vector();
		Vector processVector = new Vector();
		Nodeconfig nodeconfig = new Nodeconfig();
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();
		Vector errptlogVector = new Vector();
		Vector volumeVector=new Vector();
		List routeList=new ArrayList();
		Vector memoryVector = new Vector();
		List iflist = new ArrayList();
		List alldiskperf = new ArrayList();
		List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
		List netmedialist = new ArrayList();
		List servicelist = new ArrayList();
		List cpuperflist = new ArrayList();

		String ipaddress = telnet.getHost();
		List monitorItemList = telnet.getMonitorItemList();

		CPUcollectdata cpudata = null;
		Systemcollectdata systemdata = null;
		Usercollectdata userdata = null;
		Processcollectdata processdata = null;
		Host host = (Host) PollingEngine.getInstance().getNodeByIP(
				telnet.getHost());
		if (host == null)
			return null;
		nodeconfig.setNodeid(host.getId());
		nodeconfig.setHostname(host.getAlias());
		float PhysicalMemCap = 0;
		float freePhysicalMemory = 0;
		float allPhyPagesSize = 0;
		float usedPhyPagesSize = 0;
		float SwapMemCap = 0;
		float freeSwapMemory = 0;
		float usedSwapMemory = 0;
		Hashtable pagehash = new Hashtable();
		Hashtable paginghash = new Hashtable();

		Hashtable networkconfig = new Hashtable();

		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");							
		Date cc = date.getTime();
		String collecttime = sdf1.format(cc);
		
		Hashtable gatherHash = new Hashtable();
		if(monitorItemList != null && monitorItemList.size()>0){
			for(int i=0;i<monitorItemList.size();i++){
				NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
				gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators.getName());
			}
		}
		if(gatherHash == null)return null;
		
		if(gatherHash.containsKey("pageingspace")){
			//----------------����Paging Space����--���������---------------------        	
			String Paging_Content = "";
			try
			{
				Paging_Content = telnet.send("lsps -s");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] Paging_LineArr = null;
			String[] Paging_tmpData = null;
			
			try{
				Paging_LineArr = Paging_Content.split("\n");
				if(Paging_LineArr!=null&&Paging_LineArr.length>1)
				{    	
					for(int i=0;i<Paging_LineArr.length;i++){
						String tempstr = Paging_LineArr[i];
						if(tempstr != null){
							if(tempstr.contains("%")){
								Paging_tmpData = tempstr.trim().split("\\s++");          			
								if(Paging_tmpData != null)
								{							
									String Total_Paging_Space = Paging_tmpData[0];
									String Percent_Used = Paging_tmpData[1];
									paginghash.put("Total_Paging_Space", Total_Paging_Space);
									paginghash.put("Percent_Used", Percent_Used);
									
									//SysLogger.info("Total_Paging_Space:"+Total_Paging_Space+"====Percent_Used:"+Percent_Used);
									Hashtable collectHash = new Hashtable();
									collectHash.put("pagingusage", paginghash);
								    try{
										AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
										List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "aix","pagingusage");
										for(int k = 0 ; k < list.size() ; k ++){
											AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
											//�Ի�ҳ�ʽ��и澯���
											CheckEventUtil checkutil = new CheckEventUtil();
											checkutil.updateData(host,collectHash,"host","aix",alarmIndicatorsnode);
											//}
										}
								    }catch(Exception e){
								    	e.printStackTrace();
								    }
								    
									//�ܻ�ҳ
									try
									{
										Total_Paging_Space = Total_Paging_Space.replaceAll("MB", "");
										allPhyPagesSize = Float.parseFloat(Total_Paging_Space);
									} catch (Exception e)
									{
										e.printStackTrace();
									}
									//��ʹ����
									try
									{
										Percent_Used = Percent_Used.replaceAll("%", "");
										usedPhyPagesSize = Float.parseFloat(Percent_Used);
									} catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							}
						}
						
					}

				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		

		if(gatherHash.containsKey("swapmemory")){
			// ----------------����swap����--���������---------------------
			String swap_Content = "";
			try
			{
				swap_Content = telnet.send("swap -l");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] swap_LineArr = null;
			String[] swap_tmpData = null;
			try
			{
				swap_LineArr = swap_Content.trim().split("\n");
				if (swap_LineArr != null && swap_LineArr.length > 0)
				{
					for(int i=0;i<swap_LineArr.length;i++){
						String temp = swap_LineArr[i].trim();
						if(temp.contains("/")){
							swap_tmpData = temp.trim().split("\\s++");
							if (swap_tmpData != null && swap_tmpData.length == 5)
							{
								try
								{
									// System.out.println("==============================�����ɹ�swap");
									SwapMemCap = Float.parseFloat(swap_tmpData[3].replace("MB", ""));
									freeSwapMemory = Float.parseFloat(swap_tmpData[4].replace("MB", ""));
									usedSwapMemory = SwapMemCap-freeSwapMemory;
									//SysLogger.info(SwapMemCap+"===="+freeSwapMemory+"===="+usedSwapMemory);
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
							break;
						}
					}

				}
				// ��memory����д��ȥ	
				if (SwapMemCap > 0)
				{
					// Swap
					Memorycollectdata memorydata = null;
					memorydata = new Memorycollectdata();
					memorydata.setIpaddress(ipaddress);
					memorydata.setCollecttime(date);
					memorydata.setCategory("Memory");
					memorydata.setEntity("Capability");
					memorydata.setSubentity("SwapMemory");
					memorydata.setRestype("static");
					memorydata.setUnit("M");
					// һ��BLOCK��512byte
					// ��������ʹ�ô�С
					memorydata.setThevalue(Math.round(SwapMemCap) + "");
					memoryVector.addElement(memorydata);
					memorydata = new Memorycollectdata();
					memorydata.setIpaddress(ipaddress);
					memorydata.setCollecttime(date);
					memorydata.setCategory("Memory");
					memorydata.setEntity("UsedSize");
					memorydata.setSubentity("SwapMemory");
					memorydata.setRestype("static");
					memorydata.setUnit("M");
					memorydata.setThevalue(Math.round(usedSwapMemory ) + "");
					memoryVector.addElement(memorydata);
					// ��������ʹ����
					memorydata = new Memorycollectdata();
					memorydata.setIpaddress(ipaddress);
					memorydata.setCollecttime(date);
					memorydata.setCategory("Memory");
					memorydata.setEntity("Utilization");
					memorydata.setSubentity("SwapMemory");
					memorydata.setRestype("dynamic");
					memorydata.setUnit("%");
					memorydata.setThevalue(Math
							.round(usedSwapMemory * 100 / SwapMemCap)
							+ "");
//					System.out.println("ʹ�ô�С=" + usedSwapMemory);
//					System.out.println("�ܴ�С=" + SwapMemCap);
//					System.out.println("��������ʹ����  "+ Math.round(usedSwapMemory * 100 / SwapMemCap) + "");
					memoryVector.addElement(memorydata);
					
		  			Vector swapmemV = new Vector();
		  			swapmemV.add(memorydata);				
				    Hashtable collectHash = new Hashtable();
					collectHash.put("swapmem", swapmemV);				
					//�Խ����ڴ�ֵ���и澯���
				    try{
						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
						List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "aix","swapmemory");
						for(int i = 0 ; i < list.size() ; i ++){
							AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
							//�Խ����ڴ�ֵ���и澯���
							CheckEventUtil checkutil = new CheckEventUtil();
							checkutil.updateData(host,collectHash,"host","aix",alarmIndicatorsnode);
							//}
						}
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
				}


			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// ----------------����cpuconfig����--���������---------------------
		String cpuconfigContent = "";

		try
		{
			cpuconfigContent = telnet.send("prtconf");
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		String[] cpuconfigLineArr = cpuconfigContent.split("\n");
		String[] cpuconfig_tmpData = null;
		
		Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
		String procesors = "";
		String processorType = "";
		String processorSpeed = "";
		for (int i = 0; i < cpuconfigLineArr.length; i++)
		{
			String[] result = cpuconfigLineArr[i].trim().split(":");
			if (result.length > 0)
			{
				if (result[0].trim().equalsIgnoreCase("Number Of Processors"))
				{
					// ����������
					// ���ýڵ��CPU���ø���
					nodeconfig.setNumberOfProcessors(result[1].trim() + "");
				} else if (result[0].trim().equalsIgnoreCase("CPU Type"))
				{
					// CPU����λ
					if (nodeconfig.getNumberOfProcessors() != null
							&& nodeconfig.getNumberOfProcessors().trim()
									.length() > 0)
					{
						int pnum = Integer.parseInt(nodeconfig
								.getNumberOfProcessors());
						for (int k = 0; k < pnum; k++)
						{
							nodecpuconfig.setDataWidth(result[1].trim() + "");
							nodecpuconfig.setProcessorId(k + "");
							nodecpuconfig.setName("");
							nodecpuconfig.setNodeid(host.getId());
							nodecpuconfig.setL2CacheSize("");
							nodecpuconfig.setL2CacheSpeed("");
							nodecpuconfig.setProcessorType(processorType);
							nodecpuconfig.setProcessorSpeed(processorSpeed);
							cpuconfiglist.add(nodecpuconfig);
							nodecpuconfig = new Nodecpuconfig();
						}
					}
				}else if(result[0].trim().equalsIgnoreCase("Processor Type")){
					//CPU����
					processorType = result[1].trim()+"";
				}else if(result[0].trim().equalsIgnoreCase("Processor Clock Speed")){
					//CPU�ں���Ƶ
					processorSpeed = result[1].trim()+"";
				} else if (result[0].trim()
						.equalsIgnoreCase("Good Memory Size"))
				{
					String allphy = result[1].trim().trim();
					try
					{
						allphy = allphy.replaceAll("MB", "");
						PhysicalMemCap = Float.parseFloat(allphy);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					// nodecpuconfig.setDataWidth(result[1].trim()+"");
				} else if (result[0].trim().equalsIgnoreCase("IP Address"))
				{
					// IP��ַ
					networkconfig.put("IP", result[1].trim() + "");
				} else if (result[0].trim().equalsIgnoreCase("Sub Netmask"))
				{
					// ��������
					networkconfig.put("NETMASK", result[1].trim() + "");
				} else if (result[0].trim().equalsIgnoreCase("Gateway"))
				{
					// ����
					networkconfig.put("GATEWAY", result[1].trim() + "");
				}

			}

		}
		nodecpuconfig = null;
		
		if(gatherHash.containsKey("systemconfig")){			
			// ----------------����uname����--���������---------------------

			String unameContent = "";

			try
			{
				unameContent = telnet.send("uname -sn");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}

			String[] unameLineArr = unameContent.split("\n");
			String[] uname_tmpData = null;
			for (int i = 0; i < unameLineArr.length; i++)
			{
				uname_tmpData = unameLineArr[i].split("\\s++");
				if (uname_tmpData.length == 2)
				{
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
			
			// ----------------����usergroup����--���������---------------------
			Hashtable usergrouphash = new Hashtable();
			String usergroupContent = "";

			try
			{
				usergroupContent = telnet.send("cat /etc/group");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] usergroupLineArr = usergroupContent.split("\n");
			String[] usergroup_tmpData = null;
			for (int i = 0; i < usergroupLineArr.length; i++)
			{
				usergroup_tmpData = usergroupLineArr[i].split(":");
				if (usergroup_tmpData.length >= 3)
				{
					usergrouphash.put((String) usergroup_tmpData[2],
							usergroup_tmpData[0]);
				}
			}

			// ----------------����user����--���������---------------------
			String userContent = "";

			try
			{
				userContent = telnet.send("lsuser ALL");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}

			String[] userLineArr = userContent.split("\n");
			String[] user_tmpData = null;
			for (int i = 0; i < userLineArr.length; i++)
			{
				String[] result = userLineArr[i].trim().split("\\s++");
				if (result.length >= 4)
				{
					String userName = result[0];
					String groupStr = result[3];
					String[] groups = groupStr.split("=");
					String group = "";
					if (groups != null && groups.length == 2)
					{
						group = groups[1];
					}
					// String userid = result[1];
					// int usergroupid = Integer.parseInt(result[3]);
					// С��500��Ϊϵͳ���û�,����
					// if(userid < 500)continue;

					userdata = new Usercollectdata();
					userdata.setIpaddress(ipaddress);
					userdata.setCollecttime(date);
					userdata.setCategory("User");
					userdata.setEntity("Sysuser");
					userdata.setSubentity(group);
					userdata.setRestype("static");
					userdata.setUnit(" ");
					userdata.setThevalue(userName);
					userVector.addElement(userdata);
				}

			}

			// ----------------����date����--���������---------------------
			String dateContent = "";

			try
			{
				dateContent = telnet.send("date");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (dateContent != null && dateContent.length() > 0)
			{
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

			// ----------------����uptime����--���������---------------------
			String uptimeContent = "";
			try
			{
				uptimeContent = telnet.send("uptime");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (uptimeContent != null && uptimeContent.length() > 0)
			{
				//SysLogger.info("===uptime==="+uptimeContent);
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
			
			// ----------------����version����--���������---------------------
			String versionContent = "";
			try
			{
				versionContent = telnet.send("oslevel -q");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			System.out.println("-------------------------------------------------"+versionContent);
			
			String[] oslevel_LineArr = null;
			if (versionContent != null && versionContent.length() > 0)
			{
				oslevel_LineArr = versionContent.trim().split("\n");
				//SysLogger.info(oslevel_LineArr[1]+"====version");
				for(int i=0;i<oslevel_LineArr.length;i++){
					String temp = oslevel_LineArr[i];
					if(temp.contains(".")){
						nodeconfig.setCSDVersion(temp);
						break;
					}
				}
				
			}
		}


		if(gatherHash.containsKey("disk")){
			// ----------------����disk����--���������---------------------
			String diskContent = "";
			String diskLabel;
			List disklist = new ArrayList();
			try
			{
				diskContent = telnet.send("df -m");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] diskLineArr = diskContent.split("\n");
			String[] tmpData = null;
			Diskcollectdata diskdata = null;
			int diskflag = 0;
			for (int i = 1; i < diskLineArr.length; i++)
			{

				tmpData = diskLineArr[i].split("\\s++");
				if ((tmpData != null) && (tmpData.length == 7))
				{
					diskLabel = tmpData[6];

					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("Utilization");// ���ðٷֱ�
					diskdata.setSubentity(tmpData[6]);
					diskdata.setRestype("static");
					diskdata.setUnit("%");
					try
					{
						diskdata.setThevalue(Float.toString(Float
								.parseFloat(tmpData[3].substring(0, tmpData[3]
										.indexOf("%")))));
					} catch (Exception ex)
					{
						continue;
					}
					diskVector.addElement(diskdata);

					
					//yangjun 
					try {
						String diskinc = "0.0";
						float pastutil = 0.0f;
						Vector disk_v = (Vector)ipAllData.get("disk");
						if (disk_v != null && disk_v.size() > 0) {
							for (int si = 0; si < disk_v.size(); si++) {
								Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
								if((tmpData[6]).equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
									pastutil = Float.parseFloat(disk_data.getThevalue());
								}
							}
						} else {
							pastutil = Float.parseFloat(tmpData[3].substring(0,tmpData[3].indexOf("%")));
						}
						if (pastutil == 0) {
							pastutil = Float.parseFloat(
									tmpData[3].substring(
											0,
											tmpData[3].indexOf("%")));
						}
						if(Float.parseFloat(
										tmpData[3].substring(
										0,
										tmpData[3].indexOf("%")))-pastutil>0){
							diskinc = (Float.parseFloat(
											tmpData[3].substring(
											0,
											tmpData[3].indexOf("%")))-pastutil)+"";
						}
						diskdata = new Diskcollectdata();
						diskdata.setIpaddress(host.getIpAddress());
						diskdata.setCollecttime(date);
						diskdata.setCategory("Disk");
						diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
						diskdata.setSubentity(tmpData[6]);
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
					diskdata.setEntity("AllSize");// �ܿռ�
					diskdata.setSubentity(tmpData[6]);
					diskdata.setRestype("static");

					float allblocksize = 0;
					allblocksize = Float.parseFloat(tmpData[1]);
					float allsize = 0.0f;
					allsize = allblocksize;
					if (allsize >= 1024.0f)
					{
						allsize = allsize / 1024;
						diskdata.setUnit("G");
					} else
					{
						diskdata.setUnit("M");
					}

					diskdata.setThevalue(Float.toString(allsize));
					diskVector.addElement(diskdata);

					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(ipaddress);
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("UsedSize");// ʹ�ô�С
					diskdata.setSubentity(tmpData[6]);
					diskdata.setRestype("static");

					float FreeintSize = 0;
					FreeintSize = Float.parseFloat(tmpData[2]);

					float usedfloatsize = 0.0f;
					usedfloatsize = allblocksize - FreeintSize;
					if (usedfloatsize >= 1024.0f)
					{
						usedfloatsize = usedfloatsize / 1024;
						diskdata.setUnit("G");
					} else
					{
						diskdata.setUnit("M");
					}
					diskdata.setThevalue(Float.toString(usedfloatsize));
					diskVector.addElement(diskdata);
					disklist.add(diskflag, diskLabel);
					diskflag = diskflag + 1;
				}
			}
			
		    //���д��̸澯���
		    //SysLogger.info("### ��ʼ���м������Ƿ�澯### ... ###");
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "aix");
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
		}
		
		if(gatherHash.containsKey("diskio")){
			// ----------------����diskperf����--���������---------------------

			String diskperfContent = "";
			String average = "";

			try
			{
				diskperfContent = telnet.send("sar -d 1 2");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] diskperfLineArr = diskperfContent.split("\n");
			String[] diskperf_tmpData = null;
			
			Hashtable<String, String> diskperfhash = new Hashtable<String, String>();
			int flag = 0;
			for (int i = 0; i < diskperfLineArr.length; i++)
			{
				diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
				if (diskperf_tmpData != null
						&& (diskperf_tmpData.length == 7 || diskperf_tmpData.length == 8))
				{
					if (diskperf_tmpData[0].trim().equalsIgnoreCase("Average"))
					{
						flag = 1;
						diskperfhash.put("%busy", diskperf_tmpData[2].trim());
						diskperfhash.put("avque", diskperf_tmpData[3].trim());
						diskperfhash.put("r+w/s", diskperf_tmpData[4].trim());
						diskperfhash.put("Kbs/s", diskperf_tmpData[5].trim());
						diskperfhash.put("avwait", diskperf_tmpData[6].trim());
						diskperfhash.put("avserv", diskperf_tmpData[7].trim());
						diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
						alldiskperf.add(diskperfhash);
					} else if (flag == 1)
					{
						diskperfhash.put("%busy", diskperf_tmpData[1].trim());
						diskperfhash.put("avque", diskperf_tmpData[2].trim());
						diskperfhash.put("r+w/s", diskperf_tmpData[3].trim());
						diskperfhash.put("Kbs/s", diskperf_tmpData[4].trim());
						diskperfhash.put("avwait", diskperf_tmpData[5].trim());
						diskperfhash.put("avserv", diskperf_tmpData[6].trim());
						diskperfhash.put("disklebel", diskperf_tmpData[0].trim());
						alldiskperf.add(diskperfhash);
					}

					diskperfhash = new Hashtable();
				}
			}
		}
		

		if(gatherHash.containsKey("netperf")){
			// ----------------����netperf����--���������---------------------
			String netperfContent = "";

			try
			{
				netperfContent = telnet.send("netstat -ian");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] netperfLineArr = netperfContent.split("\n");
			String[] netperf_tmpData = null;
			List netperf = new ArrayList();
			// Hashtable<String,String> netnamehash = new
			// Hashtable<String,String>();
			// int flag = 0;
			for (int i = 0; i < netperfLineArr.length; i++)
			{
				netperf_tmpData = netperfLineArr[i].trim().split("\\s++");
				// System.out.println("=============================����==="+netperf_tmpData.length);
				if (netperf_tmpData != null && netperf_tmpData.length == 9)
				{
					if (netperf_tmpData[0].trim().indexOf("en") >= 0
							&& netperf_tmpData[2].trim().indexOf("link") >= 0)
					{
						netperf.add(netperf_tmpData[0].trim());
					}
				} else if (netperf_tmpData != null && netperf_tmpData.length == 10)
				{
					if (netperf_tmpData[0].trim().indexOf("en") >= 0
							&& netperf_tmpData[2].trim().indexOf("link") >= 0)
					{
						netperf.add(netperf_tmpData[0].trim());
					}
				}

			}

			
			// ----------------����netallperf����--���������---------------------
			
			List oldiflist = new ArrayList();
			
			Hashtable netmediahash = new Hashtable();
			String netallperfContent = "";

			try
			{
				netallperfContent = "start-en0\n";
				netallperfContent = netallperfContent +telnet.send("entstat -d en0 |egrep 'Hardware Address|Link Status|Media Speed Running|Packets|Bytes'");
				//netallperfContent = netallperfContent +telnet.send("entstat -d en0 |egrep 'Bytes'");
				netallperfContent = netallperfContent +"\nend-en0\n";
				netallperfContent = netallperfContent +"start-en1\n";
				netallperfContent = netallperfContent +telnet.send("entstat -d en1 |egrep 'Hardware Address|Link Status|Media Speed Running|Packets|Bytes'");
				netallperfContent = netallperfContent +"\nend-en1\n";
				netallperfContent = netallperfContent +"start-en2\n";
				netallperfContent = netallperfContent +telnet.send("entstat -d en2 |egrep 'Hardware Address|Link Status|Media Speed Running|Packets|Bytes'");
				netallperfContent = netallperfContent +"\nend-en2\n";
				netallperfContent = netallperfContent +"start-en3\n";
				netallperfContent = netallperfContent +telnet.send("entstat -d en3 |egrep 'Hardware Address|Link Status|Media Speed Running|Packets|Bytes'");
				netallperfContent = netallperfContent +"\nend-en3\n";
				//netallperfContent = telnet.send("entstat -d en0 |egrep 'Bytes|Hardware Address|Link Status|Media Speed Running|Packets|Bytes|Interrupts' |egrep -v 'XOFF|XON';entstat -d en2 |egrep 'Bytes|Hardware Address|Link Status|Media Speed Running|Packets|Bytes|Interrupts' |egrep -v 'XOFF|XON';entstat -d en3 |egrep 'Bytes|Hardware Address|Link Status|Media Speed Running|Packets|Bytes|Interrupts' |egrep -v 'XOFF|XON'");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			//SysLogger.info(netallperfContent);
			String[] netallperfLineArr = netallperfContent.trim().split("\n");
			String[] netallperf_tmpData = null;
			List netalldiskperf = new ArrayList();
			Hashtable<String,String> netallperfhash = new Hashtable<String,String>();
			int macflag = 0;
			String MAC = "";
			//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			
			if(ipAllData != null){
				oldiflist = (List)ipAllData.get("iflist");
			}
			
			if(netperf != null && netperf.size()>0){
				//System.out.println("-------------------�������-----------------------------");
				Interfacecollectdata interfacedata = null;
				
				//��ʼѭ������ӿ�
				for(int k=0;k<netperf.size();k++){
					
					
					Hashtable ifhash = new Hashtable();
					Hashtable oldifhash = new Hashtable();//���������ϴβɼ����
					if(oldiflist != null && oldiflist.size()>0){
						oldifhash = (Hashtable)oldiflist.get(k);
					}
					
					
					String portDesc = (String)netperf.get(k);//en1
					//SysLogger.info("==========="+portDesc+"===========");
					//int index=0;//������λ
					
					//ͨ��
					  
					 /**
					  * ����aix ͬһ������ɼ������ݳ��ֲ�ͬ�����ݸ�ʽ��Ҫ����������ж�
					  * ��һ�������������ģʽ
					  * ģʽһ 
					  * ETHERNET STATISTICS (en1
					  * netflg="neten"
					  * ģʽ����
					  * Hardware Address:
					  * netflg="netmac"
					  * ʹ��һ�� netflg �ж���ʲôģʽ
					  * 
					  */
					
					 String netflg="";
					 
//					 if(netallperfContent.indexOf("ETHERNET STATISTICS ("+portDesc)>0){
//						 netflg="neten";
//						 
//					 }
					 int beginindex = 0;
					 for(int i=0;i<netallperfLineArr.length;i++)
					 { 
						 int tempid = i;
						 //SysLogger.info(tempid+"======="+netallperfLineArr[tempid]);
						 if(netallperfLineArr[tempid].indexOf("Hardware Address:")>=0)
						 {
							 netflg="netmac";
							// beginindex = tempid;
							 //index=k*13;
							 break;	 
						 }
						 
					 }
					
					//IDC�汾��ȥmac��ַ
					 
					 String mideaspeed = "";//��������
					 String status = "";//����״̬
					 String Bytes = "";//������������ֽ���
					 String Packets = "";//����������ݰ�
					 String LinkStatus = "";//״̬
					 //ETHERNET STATISTICS (en1 ģʽ
					 Pattern tmpPt = null;
				     Matcher mr = null;
					//if(netflg.equals("neten")){
						
						//System.out.println("=====neten=====================neten=====neten=="+portDesc);
						
						tmpPt = Pattern.compile("(start-"+portDesc+")(.*)(end-"+portDesc+")",Pattern.DOTALL);
						mr = tmpPt.matcher(netallperfContent.toString());
						String netenContent="";
						if(mr.find())
						{
							netenContent = mr.group(2);

						} 
						String [] netLineArr=null;
						netLineArr=netenContent.trim().split("\n");
						//SysLogger.info("---"+netenContent.trim());
//						System.out.println("&&&&&&&"+netLineArr[3]);
//				        MAC = netLineArr[3].trim().substring(netLineArr[3].trim().indexOf("Hardware Address:"));  
					try{
						
						for(int i=0;i<netLineArr.length;i++){
						String tempStr = netLineArr[i];
						//SysLogger.info("====="+tempStr);
						if(tempStr.indexOf("Hardware Address:")>=0)
						 {
							 beginindex = i;
							 break;	 
						 }
//						if(tempStr != null && tempStr.length()>0){
//							if(tempStr.indexOf("Hardware Address:")>=0){
//								MAC = tempStr.trim().substring(tempStr.trim().indexOf("Hardware Address:")+17);
//							}else if(tempStr.indexOf("Media Speed Running:")>=0){
//								mideaspeed = tempStr.trim().substring(tempStr.trim().indexOf("Media Speed Running:")+20);
//							}else if(tempStr.indexOf("Link Status :")>=0){
//								status = tempStr.trim().substring(tempStr.trim().indexOf("Link Status :"));
//								LinkStatus=status;
//							}else if(tempStr.startsWith("Packets:")){
//								Packets = tempStr.trim();
//							}else if(tempStr.startsWith("Bytes:")){
//								Bytes = tempStr.trim();
//								//SysLogger.info("---------------"+Bytes);
//							}
//						}
					}
						
						
						//SysLogger.info("===beginindex===="+beginindex);
						MAC = netLineArr[beginindex].trim().substring(netLineArr[beginindex].trim().indexOf("Hardware Address:")+17);
						Packets = netLineArr[beginindex+1].trim();
						Bytes = netLineArr[beginindex+2].trim();
						status = netLineArr[beginindex+9].trim().substring(netLineArr[beginindex+9].trim().indexOf(":"));
						LinkStatus=status;
						mideaspeed = netLineArr[beginindex+10].trim().substring(netLineArr[beginindex+10].trim().indexOf(":"));
						
//						for(int i=0;i<netLineArr.length;i++){
//							String tempStr = netLineArr[i];
//							SysLogger.info("==="+tempStr);
//							if(tempStr != null && tempStr.length()>0){
//								if(tempStr.indexOf("Hardware Address:")>=0){
//									MAC = tempStr.trim().substring(tempStr.trim().indexOf("Hardware Address:")+17);
//								}else if(tempStr.indexOf("Media Speed Running:")>=0){
//									mideaspeed = tempStr.trim().substring(tempStr.trim().indexOf("Media Speed Running:")+20);
//								}else if(tempStr.indexOf("Link Status :")>=0){
//									status = tempStr.trim().substring(tempStr.trim().indexOf("Link Status :"));
//									LinkStatus=status;
//								}else if(tempStr.startsWith("Packets:")){
//									Packets = tempStr.trim();
//								}else if(tempStr.startsWith("Bytes:")){
//									Bytes = tempStr.trim();
//									//SysLogger.info("---------------"+Bytes);
//								}
//							}
//						}
//						mideaspeed = netLineArr[45].trim().substring(netLineArr[45].trim().indexOf("Media Speed Running:")); 
//						status = netLineArr[43].trim().substring(netLineArr[43].trim().indexOf("Link Status :")); 						
//						Packets = netLineArr[8].trim(); 
//						LinkStatus=status;
//						Bytes = netLineArr[9].trim(); 

					 
					}catch(Exception e){
						e.printStackTrace();
					}	
//					SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$MAC==="+MAC);
//					SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$status==="+status);
//					SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$mideaspeed==="+mideaspeed);
					String mac_ = nodeconfig.getMac();
					if(mac_ != null && mac_.trim().length()>0){
						
						//�����������࣬�����ֵ���ѽ���Ū����
						if(k<3)
						{
							mac_=mac_+","+MAC;
						}
						
						nodeconfig.setMac(mac_);
					}else{
						nodeconfig.setMac(MAC);
					}
				
					
					
					status=	status.replaceAll("Link Status :", "").trim();
					netmediahash.put("desc", portDesc);//����
					netmediahash.put("speed", mideaspeed);//����
					netmediahash.put("mac", MAC);
					netmediahash.put("status", status);//����״��
					netmedialist.add(netmediahash);
					netmediahash = new Hashtable();	
					
					
					//=================�������ݰ�==================
					String outPackets ="0";
					String inPackets ="0";
					if(Packets.indexOf("Packets:")>=0)
					{
					String[] packsperf_tmpData = Packets.split("\\s++");
					
					outPackets = packsperf_tmpData[1];//���͵����ݰ�
					inPackets = packsperf_tmpData[3];//���ܵ����ݰ�
					}
					String oldOutPackets = "0";
					String oldInPackets = "0";
					String endOutPackets = "0";
					String endInPackets = "0";
					if(oldifhash != null && oldifhash.size()>0){
						if(oldifhash.containsKey("outPackets")){
							oldOutPackets = (String)oldifhash.get("outPackets");
						}
						try{
							endOutPackets = (Long.parseLong(outPackets)-Long.parseLong(oldOutPackets))+"";
						}catch(Exception e){
							e.printStackTrace();
						}
						if(oldifhash.containsKey("inPackets")){
							oldInPackets = (String)oldifhash.get("inPackets");
						}
						try{
							endInPackets = (Long.parseLong(inPackets)-Long.parseLong(oldInPackets))+"";
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					String outBytes ="0";
					String inBytes ="0";
					if(Bytes.indexOf("Bytes:")>=0)
					{
					String[] bytes_tmpData = Bytes.split("\\s++");
					outBytes = bytes_tmpData[1];//���͵��ֽ�
					inBytes = bytes_tmpData[3];//���ܵ��ֽ�
					
					//===�����ֽ���=====================
					
					String oldOutBytes = "0";
					String oldInBytes = "0";
					String endOutBytes = "0";
					String endInBytes = "0";
					
					if(oldifhash != null && oldifhash.size()>0){
						if(oldifhash.containsKey("outBytes")){
							oldOutBytes = (String)oldifhash.get("outBytes");
						}
						try{
							endOutBytes = (Long.parseLong(outBytes)-Long.parseLong(oldOutBytes))*8/1024/300+"";
						}catch(Exception e){
							e.printStackTrace();
						}
						if(oldifhash.containsKey("inBytes")){
							oldInBytes = (String)oldifhash.get("inBytes");
						}
						try{
							endInBytes = (Long.parseLong(inBytes)-Long.parseLong(oldInBytes))*8/1024/300+"";
						}catch(Exception e){
							e.printStackTrace();
						}
					}				
					String linkstatus ="";
					//System.out.println("&&&&&&&&&&**************************"+LinkStatus);
					linkstatus=LinkStatus.replaceAll("Link Status :", "").trim();
				    if (linkstatus.equals("Up"))
				    {
				    	//System.out.println("---------- Up");
				    	linkstatus="1";
				    }else if(linkstatus.equals("Down"))
				     {
				    	//System.out.println("---------- Down");
				    	linkstatus="2";
				     }
					//System.out.println("7788877=="+linkstatus);
					//============����===============
					String MediaSpeedRunning =mideaspeed;
					String speedunit = "";
					String speedstr = "";
					String mspeed ="0";
					if(MediaSpeedRunning.indexOf(":")>=0)
					{
					String[] speed_tmpData = MediaSpeedRunning.split(":");
					
					
				    mspeed = speed_tmpData[1].trim();
					String[] speed = mspeed.split("\\s++");
					
					if(speed.length>0){
					speedstr = speed[0];
					}else{
					speedstr = "0";
					}
					
					if(speed.length>1){
					speedunit = speed[1];
					}else{
					speedunit = "Mbps";
					}
					}
					
					
					ifhash.put("outPackets", outPackets);
					ifhash.put("inPackets", inPackets);
					ifhash.put("outBytes", outBytes);
					ifhash.put("inBytes", inBytes);
					
					
				   	//�˿�����
					interfacedata=new Interfacecollectdata();
					interfacedata.setIpaddress(ipaddress);
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("index");
					interfacedata.setSubentity(k+1+"");
					//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
					interfacedata.setRestype("static");
					interfacedata.setUnit("");
					interfacedata.setThevalue(k+1+"");
					interfacedata.setChname("�˿�����");
					interfaceVector.addElement(interfacedata);
					//�˿�����
					interfacedata=new Interfacecollectdata();
					interfacedata.setIpaddress(ipaddress);
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("ifDescr");
					interfacedata.setSubentity(k+1+"");
					//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
					interfacedata.setRestype("static");
					interfacedata.setUnit("");
					interfacedata.setThevalue(portDesc);
					interfacedata.setChname("�˿�����2");
					interfaceVector.addElement(interfacedata);
					//�˿ڴ���
					interfacedata=new Interfacecollectdata();
					interfacedata.setIpaddress(ipaddress);
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("ifSpeed");
					interfacedata.setSubentity(k+1+"");
					interfacedata.setRestype("static");
					interfacedata.setUnit(speedunit);
					interfacedata.setThevalue(speedstr);
					interfacedata.setChname("");
					interfaceVector.addElement(interfacedata);
					//��ǰ״̬
					interfacedata=new Interfacecollectdata();
					interfacedata.setIpaddress(ipaddress);
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("ifOperStatus");
					interfacedata.setSubentity(k+1+"");
					interfacedata.setRestype("static");
					interfacedata.setUnit("");
					interfacedata.setThevalue(linkstatus);
					interfacedata.setChname("��ǰ״̬");
					interfaceVector.addElement(interfacedata);
					//��ǰ״̬
					interfacedata=new Interfacecollectdata();
					interfacedata.setIpaddress(ipaddress);
					interfacedata.setCollecttime(date);
					interfacedata.setCategory("Interface");
					interfacedata.setEntity("ifOperStatus");
					interfacedata.setSubentity(k+1+"");
					interfacedata.setRestype("static");
					interfacedata.setUnit("");
					interfacedata.setThevalue(1+"");
					interfacedata.setChname("��ǰ״̬");
					interfaceVector.addElement(interfacedata);
					//�˿��������
					UtilHdx utilhdx=new UtilHdx();
					utilhdx.setIpaddress(ipaddress);
					utilhdx.setCollecttime(date);
					utilhdx.setCategory("Interface");
					String chnameBand="";
					utilhdx.setEntity("InBandwidthUtilHdx");
					utilhdx.setThevalue(endInBytes);
					utilhdx.setSubentity(k+1+"");
					utilhdx.setRestype("dynamic");
					utilhdx.setUnit("Kb/��");	
					utilhdx.setChname(k+1+"�˿����"+"����");
					utilhdxVector.addElement(utilhdx);
					//�˿ڳ�������
					utilhdx=new UtilHdx();
					utilhdx.setIpaddress(ipaddress);
					utilhdx.setCollecttime(date);
					utilhdx.setCategory("Interface");
					utilhdx.setEntity("OutBandwidthUtilHdx");
					utilhdx.setThevalue(endOutBytes);
					utilhdx.setSubentity(k+1+"");
					utilhdx.setRestype("dynamic");
					utilhdx.setUnit("Kb/��");	
					utilhdx.setChname(k+1+"�˿ڳ���"+"����");
					utilhdxVector.addElement(utilhdx);
					
				   iflist.add(ifhash);
				   ifhash = new Hashtable();
					
					
				}
			}
			systemdata=new Systemcollectdata();
			systemdata.setIpaddress(ipaddress);
			systemdata.setCollecttime(date);
			systemdata.setCategory("System");
			systemdata.setEntity("MacAddr");
			systemdata.setSubentity("MacAddr");
			systemdata.setRestype("static");
			systemdata.setUnit(" ");			  
			systemdata.setThevalue(MAC);
			systemVector.addElement(systemdata);
		}	

		}
		
		if(gatherHash.containsKey("physicalmemory")){
			// �����ڴ�������һ������
			// ----------------����vmstat����--���������---------------------
			String vmstat_Content = "";
			try
			{
				vmstat_Content = telnet.send("vmstat");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] vmstat_LineArr = null;
			String[] vmstat_tmpData = null;

			try
			{
				vmstat_LineArr = vmstat_Content.split("\n");

				for (int i = 1; i < vmstat_LineArr.length; i++)
				{
					vmstat_tmpData = vmstat_LineArr[i].trim().split("\\s++");
					if ((vmstat_tmpData != null && vmstat_tmpData.length == 17))
					{
						if (vmstat_tmpData[0] != null
								&& !vmstat_tmpData[0].equalsIgnoreCase("r"))
						{
							// freeMemory
							freePhysicalMemory = Integer.parseInt(vmstat_tmpData[3]) * 4 / 1024;
							String re = vmstat_tmpData[4];
							String pi = vmstat_tmpData[5];
							String po = vmstat_tmpData[6];
							String fr = vmstat_tmpData[7];
							String sr = vmstat_tmpData[8];
							String cy = vmstat_tmpData[9];
							pagehash.put("re", re);
							pagehash.put("pi", pi);
							pagehash.put("po", po);
							pagehash.put("fr", fr);
							pagehash.put("sr", sr);
							pagehash.put("cy", cy);
						}

					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			Memorycollectdata memorydata = null;
			if (PhysicalMemCap > 0)
			{
				// usedPhyPagesSize������ڴ�ʹ����
				// freePhysicalMemory ���������ڴ����У������ڴ���û��
				// �����ڴ�ʹ����
				//System.out.println("============���������ڴ�========================"+ freePhysicalMemory);
				float PhysicalMemUtilization = (PhysicalMemCap - freePhysicalMemory)
						* 100 / PhysicalMemCap;
				//System.out.println("============ʹ����=2======================="+ PhysicalMemUtilization);

				// �������ڴ��С
				memorydata = new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("Capability");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("static");
				memorydata.setUnit("M");
				memorydata.setThevalue(Float.toString(PhysicalMemCap));
				memoryVector.addElement(memorydata);
				// �Ѿ��õ������ڴ�
				memorydata = new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("UsedSize");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("static");
				memorydata.setUnit("M");
				memorydata.setThevalue(
				// Float.toString(PhysicalMemCap*(1-usedPhyPagesSize/100)));
						Float.toString(PhysicalMemCap - freePhysicalMemory));
				memoryVector.addElement(memorydata);
				// �ڴ�ʹ����
				memorydata = new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("Utilization");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("dynamic");
				memorydata.setUnit("%");
				memorydata.setThevalue(Math.round(PhysicalMemUtilization) + "");
				// memorydata.setThevalue(Math.round(usedPhyPagesSize)+"");
				memoryVector.addElement(memorydata);
				
				Vector phymemV = new Vector();
				phymemV.add(memorydata);				
			    Hashtable collectHash = new Hashtable();
				collectHash.put("physicalmem", phymemV);				
				//�������ڴ�ֵ���и澯���
			    try{
					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
					List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "aix","physicalmemory");
					for(int i = 0 ; i < list.size() ; i ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
						//�������ڴ�ֵ���и澯���
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.updateData(host,collectHash,"host","aix",alarmIndicatorsnode);
						//}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		}
		
			
			

		
		if(gatherHash.containsKey("cpudetail")){
			// ----------------����cpu����--���������---------------------
			String cpuperfContent = "";		
			try
			{
				cpuperfContent = telnet.send("sar -u 1 3");
			} catch (Exception e1)
			{
				telnet.log("error = " + e1.toString());
				e1.printStackTrace();
			}
			//SysLogger.info(cpuperfContent);
			String[] cpuperfLineArr = cpuperfContent.split("\n");
			String[] cpuperf_tmpData = null;
			
			Hashtable<String,String> cpuperfhash = new Hashtable<String,String>();
			for(int i=0; i<cpuperfLineArr.length;i++){
				cpuperf_tmpData = cpuperfLineArr[i].trim().split("\\s++");
				if(cpuperf_tmpData != null && cpuperf_tmpData.length ==5 || cpuperf_tmpData.length==6 || cpuperf_tmpData.length==7){
					
					
					if(cpuperf_tmpData[0].trim().equalsIgnoreCase("Average")){
							cpuperfhash.put("%usr", cpuperf_tmpData[1].trim());
							cpuperfhash.put("%sys", cpuperf_tmpData[2].trim());
							cpuperfhash.put("%wio", cpuperf_tmpData[3].trim());
							cpuperfhash.put("%idle", cpuperf_tmpData[4].trim());
							if(cpuperf_tmpData.length==6||cpuperf_tmpData.length==7)
							{
								cpuperfhash.put("physc", cpuperf_tmpData[5].trim());
							}
							cpuperflist.add(cpuperfhash);
							
							cpudata=new CPUcollectdata();
					   		cpudata.setIpaddress(ipaddress);
					   		cpudata.setCollecttime(date);
					   		cpudata.setCategory("CPU");
					   		cpudata.setEntity("Utilization");
					   		cpudata.setSubentity("Utilization");
					   		cpudata.setRestype("dynamic");
					   		cpudata.setUnit("%");
					   		cpudata.setThevalue(Arith.round((100.0-Double.parseDouble(cpuperf_tmpData[4].trim())),0)+"");
					   		cpuVector.addElement(cpudata);
					   		
							//��CPUֵ���и澯���
					   		Hashtable collectHash = new Hashtable();
							collectHash.put("cpu", cpuVector);
						    try{
								AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
								List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "aix","cpu");
								for(int k = 0 ; k < list.size() ; k ++){
									AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
									//��CPUֵ���и澯���
									CheckEventUtil checkutil = new CheckEventUtil();
									checkutil.updateData(host,collectHash,"host","aix",alarmIndicatorsnode);
									//}
								}
						    }catch(Exception e){
						    	e.printStackTrace();
						    }
					}
				}				
			}
		}

		if(gatherHash.containsKey("process")){
			// ----------------����process����--���������---------------------
			String processContent = "";
			try
			{
				processContent = telnet.send("ps guc | egrep -v \"RSS\" | sort +6b -7 -n -r");
						//.send("ps gv | head -n 1; ps gv | egrep -v \"RSS\" | sort +6b -7 -n -r");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			List procslist = new ArrayList();
			ProcsDao procsdaor = new ProcsDao();
			try
			{
				procslist = procsdaor.loadByIp(ipaddress);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			} finally
			{
				procsdaor.close();
			}
			List procs_list = new ArrayList();
			Hashtable procshash = new Hashtable();
			Vector procsV = new Vector();
			if (procslist != null && procslist.size() > 0)
			{
				for (int i = 0; i < procslist.size(); i++)
				{
					Procs procs = (Procs) procslist.get(i);
					procshash.put(procs.getProcname(), procs);
					procsV.add(procs.getProcname());
				}
			}
			String[] process_LineArr = processContent.split("\n");
			String[] processtmpData = null;
			float cpuusage = 0.0f;
			for (int i = 1; i < process_LineArr.length; i++)
			{
				processtmpData = process_LineArr[i].trim().split("\\s++");

				if((processtmpData != null) && (processtmpData.length >= 11)){
					
					//SysLogger.info(processtmpData[0]+"-----------------");
					String USER=processtmpData[0];//USER
					String pid=processtmpData[1];//pid
					if("USER".equalsIgnoreCase(USER))continue;
					String cmd = processtmpData[10];
					String vbstring8 = processtmpData[8];
					String vbstring5=processtmpData[9];//cputime
					if(processtmpData.length > 11){
						cmd = processtmpData[11];
						vbstring8 = processtmpData[8]+processtmpData[9];//STIME
						vbstring5=processtmpData[10];//cputime
					}
					String vbstring2="Ӧ�ó���";
					String vbstring3="";
					String vbstring4=processtmpData[4];//memsize
					if (vbstring4 == null)vbstring4="0";
					String vbstring6=processtmpData[3];//%mem
					String vbstring7=processtmpData[2];//%CPU
					String vbstring9=processtmpData[7];//STAT
					if("Z".equals(vbstring9)){
						vbstring3="��ʬ����";
					} else {
						vbstring3="��������";
					}
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("process_id");
					processdata.setSubentity(pid);
					processdata.setRestype("dynamic");
					processdata.setUnit(" ");
					processdata.setThevalue(pid);
					processVector.addElement(processdata);	
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("USER");
					processdata.setSubentity(pid);
					processdata.setRestype("dynamic");
					processdata.setUnit(" ");
					processdata.setThevalue(USER);
					processVector.addElement(processdata);	
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("MemoryUtilization");
					processdata.setSubentity(pid);
					processdata.setRestype("dynamic");
					processdata.setUnit("%");
					processdata.setThevalue(vbstring6);
					processVector.addElement(processdata);	
			
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Memory");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit("K");
					processdata.setThevalue(vbstring4);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Type");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring2);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Status");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring3);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Name");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(cmd);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("CpuTime");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit("��");
					processdata.setThevalue(vbstring5);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("StartTime");
					processdata.setSubentity(pid);
					processdata.setRestype("static");
					processdata.setUnit(" ");
					processdata.setThevalue(vbstring8);
					processVector.addElement(processdata);
					
					processdata=new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("CpuUtilization");
					processdata.setSubentity(pid);
					processdata.setRestype("dynamic");
					processdata.setUnit("%");
					processdata.setThevalue(vbstring7);
					processVector.addElement(processdata);
					/*
					//�ж��Ƿ�����Ҫ���ӵĽ��̣���ȡ�õ��б���������ӽ��̣����Vector��ȥ��
					if (procshash !=null && procshash.size()>0){
						if (procshash.containsKey(vbstring1)){
							procshash.remove(vbstring1);
							procsV.remove(vbstring1);
						}
					}
					*/
					
					
				}
			}
			//�ж�ProcsV�ﻹ��û����Ҫ���ӵĽ��̣����У���˵����ǰû�������ý��̣������������������ý��̣�ͬʱд���¼�
		     Vector eventtmpV = new Vector();
		     if (procsV !=null && procsV.size()>0){
		     	for(int i=0;i<procsV.size();i++){		     		
		     		Procs procs = (Procs)procshash.get((String)procsV.get(i));	
		     		//Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
		     		try{
			     		Hashtable iplostprocdata = (Hashtable)ShareData.getLostprocdata(ipaddress);
			     		if(iplostprocdata == null)iplostprocdata = new Hashtable();
			     		iplostprocdata.put(procs.getProcname(), procs);
			     		ShareData.setLostprocdata(ipaddress, iplostprocdata);
			    		EventList eventlist = new EventList();
			    		eventlist.setEventtype("poll");
			    		eventlist.setEventlocation(host.getSysLocation());
			    		eventlist.setContent(procs.getProcname()+"���̶�ʧ");
			    		eventlist.setLevel1(1);
			    		eventlist.setManagesign(0);
			    		eventlist.setBak("");
			    		eventlist.setRecordtime(Calendar.getInstance());
			    		eventlist.setReportman("ϵͳ��ѯ");
			    		NodeToBusinessDao ntbdao = new NodeToBusinessDao();
			    		List ntblist = ntbdao.loadByNodeAndEtype(host.getId(), "equipment");
			    		String bids = ",";
			    		if(ntblist != null && ntblist.size()>0){
			    			
			    			for(int k=0;k<ntblist.size();k++){
			    				NodeToBusiness ntb = (NodeToBusiness)ntblist.get(k);
			    				bids=bids+ntb.getBusinessid()+",";
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
						//�����ֻ����Ų�д�¼��������澯
						//createSMS(procs);		     			
		     		}catch(Exception e){
		     			e.printStackTrace();
		     		}
		     	}
		     }

			systemdata = new Systemcollectdata();
			systemdata.setIpaddress(ipaddress);
			systemdata.setCollecttime(date);
			systemdata.setCategory("System");
			systemdata.setEntity("ProcessCount");
			systemdata.setSubentity("ProcessCount");
			systemdata.setRestype("static");
			systemdata.setUnit(" ");
			systemdata.setThevalue(process_LineArr + "");
			systemVector.addElement(systemdata);
		}
		
		if(gatherHash.containsKey("service")){
			// ----------------����service����--���������---------------------
			
			Hashtable service = new Hashtable();
			String serviceContent = "";

			try
			{
				serviceContent = telnet.send("lssrc -a");
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			String[] serviceLineArr = serviceContent.split("\n");
			String[] service_tmpData = null;
			for (int i = 0; i < serviceLineArr.length; i++)
			{
				service_tmpData = serviceLineArr[i].trim().split("\\s++");
				if (service_tmpData != null && service_tmpData.length >= 3)
				{
					if ("Subsystem".equalsIgnoreCase(service_tmpData[0]))
						continue;
					if (service_tmpData.length == 4)
					{
						// �����������,��PID
						try
						{
							service.put("name", service_tmpData[0]);
							service.put("group", service_tmpData[1]);
							service.put("pid", service_tmpData[2]);
							service.put("status", service_tmpData[3]);
							servicelist.add(service);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						service = new Hashtable();
					} else
					{
						// δ���������û��PID
						try
						{
							service.put("name", service_tmpData[0]);
							service.put("group", service_tmpData[1]);
							service.put("status", service_tmpData[2]);
							service.put("pid", "");
							servicelist.add(service);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						service = new Hashtable();
					}

				}
				if (service_tmpData.length == 2)
				{
					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("operatSystem");
					systemdata.setSubentity("operatSystem");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(service_tmpData[0]);
					systemVector.addElement(systemdata);

					systemdata = new Systemcollectdata();
					systemdata.setIpaddress(ipaddress);
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity("SysName");
					systemdata.setSubentity("SysName");
					systemdata.setRestype("static");
					systemdata.setUnit(" ");
					systemdata.setThevalue(service_tmpData[1]);
					systemVector.addElement(systemdata);

				}
			}
		}
		

		
		if(gatherHash.containsKey("errpt")){
			// ----------------����errpt����--���������---------------------
			Calendar nowtime = Calendar.getInstance();
			Date nowdate = nowtime.getTime();
			long beforetime = nowdate.getTime()-5*60*1000;
			nowdate.setTime(beforetime);
			nowtime.setTimeInMillis(beforetime);
			String lastdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowdate);
			String[] datestr = lastdate.split(" ");
			String newdate = datestr[0];
			String newtime = datestr[1];
			
			String[] newdatestr = newdate.split("-");
			String[] newtimestr = newtime.split(":");
			String errptdate = newdatestr[1]+newdatestr[2]+newtimestr[0]+newtimestr[1]+newdatestr[0].substring(2);
			String errptlogContent = "";
			try
			{
				errptlogContent = telnet.send("errpt -a -s "+errptdate);
				//errptlogContent = telnet.send("errpt -a -s 0101121010");
				ReadErrptlog readErrptlog = new ReadErrptlog();
				List list = null;
				try {
					list = readErrptlog.praseErrptlog(errptlogContent);
					if(list == null){
						list = new ArrayList();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i = 0 ; i < list.size() ; i++){
					Errptlog errptlog = (Errptlog)list.get(i);
					errptlog.setHostid(host.getId()+"");
					errptlogVector.add(list.get(i));
				}
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		
		if(gatherHash.containsKey("volume")){
			//��----------------����volume����--���������---------------------
			String volumeContent = "";
			try{
				volumeContent = telnet.send("lspv");		
				String[] volumeLineArr = volumeContent.split("\n");
				String[] volumetmpData = null;
				for(int i=1; i<volumeLineArr.length;i++)
				{			
					volumetmpData = volumeLineArr[i].split("\\s++");
					if((volumetmpData != null) && (volumetmpData.length == 4 || volumetmpData.length == 3))
					{
						Hashtable volumeHash = new Hashtable();
						volumeHash.put("disk", volumetmpData[0]);
						volumeHash.put("pvid", volumetmpData[1]);
						volumeHash.put("vg", volumetmpData[2]);
						if(volumetmpData.length == 4){
							volumeHash.put("status", volumetmpData[3]);
						}else{
							volumeHash.put("status", "-");
						}
						
						volumeVector.addElement(volumeHash);
					}
				}
			}catch(Exception e){
				
			}
		}
		
		if(gatherHash.containsKey("route")){
			//��----------------����·������--���������---------------------
			String routeContent = "";
			try{
				routeContent = telnet.send("netstat -rn");		
				String[] routeLineArr = routeContent.split("\n");
				String[] routetmpData = null;
				for(int i=1; i<routeLineArr.length;i++)
				{		
					routeList.add(routeLineArr[i]);
				}
			}catch(Exception e){
				
			}
		}
		


		if (diskVector != null && diskVector.size() > 0)
			returnHash.put("disk", diskVector);
		if (cpuVector != null && cpuVector.size() > 0)
			returnHash.put("cpu", cpuVector);
		if (memoryVector != null && memoryVector.size() > 0)
			returnHash.put("memory", memoryVector);
		if (userVector != null && userVector.size() > 0)
			returnHash.put("user", userVector);
		if (processVector != null && processVector.size() > 0)
			returnHash.put("process", processVector);
		if (systemVector != null && systemVector.size() > 0)
			returnHash.put("system", systemVector);
		if (nodeconfig != null)
			returnHash.put("nodeconfig", nodeconfig);
		if (iflist != null && iflist.size() > 0)
			returnHash.put("iflist", iflist);
		if (utilhdxVector != null && utilhdxVector.size() > 0)
			returnHash.put("utilhdx", utilhdxVector);
		if (interfaceVector != null && interfaceVector.size() > 0)
			returnHash.put("interface", interfaceVector);
		if (alldiskperf != null && alldiskperf.size() > 0)
			returnHash.put("alldiskperf", alldiskperf);
		if (cpuconfiglist != null && cpuconfiglist.size() > 0)
			returnHash.put("cpuconfiglist", cpuconfiglist);
		if (netmedialist != null && netmedialist.size() > 0)
			returnHash.put("netmedialist", netmedialist);
		if (servicelist != null && servicelist.size() > 0)
			returnHash.put("servicelist", servicelist);
		if (cpuperflist != null && cpuperflist.size() > 0)
			returnHash.put("cpuperflist", cpuperflist);
		if (pagehash != null && pagehash.size()>0)
			returnHash.put("pagehash",pagehash);
		if (paginghash != null && paginghash.size()>0)
			returnHash.put("paginghash",paginghash);
		if (errptlogVector != null && errptlogVector.size()>0)
			returnHash.put("errptlog",errptlogVector);
		if (volumeVector != null && volumeVector.size()>0)
			returnHash.put("volume",volumeVector);
		if (routeList != null && routeList.size()>0)returnHash.put("routelist",routeList);
		returnHash.put("collecttime",collecttime);
		SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		SysLogger.info("&&&&&  �����ɼ� IP:"+host.getIpAddress()+"&&&&&&");
		SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return returnHash;

	}
	
	public void createSMS(Procs procs){
	 	Procs lastprocs = null;
	 	//��������	
	 	procs.setCollecttime(Calendar.getInstance());
	 	//���Ѿ����͵Ķ����б����õ�ǰ��PROC�Ѿ����͵Ķ���
	 	//lastprocs = (Procs)sendeddata.get(procs.getIpaddress()+":"+procs.getProcname());	
	 	/*
	 	try{		 				 		
	 		if (lastprocs==null){
	 			//�ڴ��в�����	,����û��������,�򷢶���
	 			Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(procs.getCollecttime().getTime());
	 			smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&���̶�ʧ&level=2");
	 			//���Ͷ���
	 			Vector tosend = new Vector();
	 			tosend.add(smscontent);		 			
	 			smsmanager.sendSmscontent(tosend);
	 			//�Ѹý�����Ϣ��ӵ��Ѿ����͵Ľ��̶����б���,��IP:��������Ϊkey
	 			sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);		 						 				
	 		}else{
	 			//���Ѿ����͵Ķ����б�������IP��PROC����
	 			//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���		 				
	 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	 			Date last = null;
	 			Date current = null;
	 			Calendar sendcalen = (Calendar)lastprocs.getCollecttime();
	 			Date cc = sendcalen.getTime();
	 			String tempsenddate = formatter.format(cc);
	 			
	 			Calendar currentcalen = (Calendar)procs.getCollecttime();
	 			cc = currentcalen.getTime();
	 			last = formatter.parse(tempsenddate);
	 			String currentsenddate = formatter.format(cc);
	 			current = formatter.parse(currentsenddate);
	 			
	 			long subvalue = current.getTime()-last.getTime();			 			
	 			
	 			if (subvalue/(1000*60*60*24)>=1){
	 				//����һ�죬���ٷ���Ϣ
		 			Smscontent smscontent = new Smscontent();
		 			String time = sdf.format(procs.getCollecttime().getTime());
		 			Equipment equipment = equipmentManager.getByip(procs.getIpaddress());
		 			if (equipment == null){
		 				return;
		 			}else
		 				smscontent.setMessage(time+"&"+equipment.getEquipname()+"&"+equipment.getIpaddress()+"&"+procs.getProcname()+"&���̶�ʧ&level=2");
		 			
		 			//���Ͷ���
		 			Vector tosend = new Vector();
		 			tosend.add(smscontent);		 			
		 			smsmanager.sendSmscontent(tosend);
		 			//�Ѹý�����Ϣ��ӵ��Ѿ����͵Ľ��̶����б���,��IP:��������Ϊkey
		 			sendeddata.put(procs.getIpaddress()+":"+procs.getProcname(),procs);		 						 				
		 		}else{
		 			//û����һ��,��ֻд�¼�
		 			Vector eventtmpV = new Vector();
					EventList event = new EventList();
					  Monitoriplist monitoriplist = (Monitoriplist)monitormanager.getByIpaddress(procs.getIpaddress());
					  event.setEventtype("host");
					  event.setEventlocation(procs.getIpaddress());
					  event.setManagesign(new Integer(0));
					  event.setReportman("monitorpc");
					  event.setRecordtime(Calendar.getInstance());
					  event.setLevel1(new Integer(1));
					  event.setEquipment(equipmentManager.getByip(monitoriplist.getIpaddress()));
					  event.setNetlocation(equipmentManager.getByip(monitoriplist.getIpaddress()).getNetlocation());
					  String time = sdf.format(Calendar.getInstance().getTime());
					  event.setContent(monitoriplist.getEquipname()+"&"+monitoriplist.getIpaddress()+"&"+time+"����"+procs.getProcname()+"��ʧ&level=1");
					  eventtmpV.add(event);
					  try{
						  eventmanager.createEventlist(eventtmpV);
					  }catch(Exception e){
						  e.printStackTrace();
					  }						  
		 		}
	 		}
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 	*/
	 }

}
