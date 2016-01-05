package com.afunms.polling.snmp.solaris;

/*
 * @author quhi@dhcc.com.cn
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.config.model.Procs;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
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
import com.gatherResulttosql.HostDatatempCpuconfiRtosql;
import com.gatherResulttosql.HostDatatempCpuperRtosql;
import com.gatherResulttosql.HostDatatempDiskRttosql;
import com.gatherResulttosql.HostDatatempNodeconfRtosql;
import com.gatherResulttosql.HostDatatempProcessRtTosql;
import com.gatherResulttosql.HostDatatempUserRtosql;
import com.gatherResulttosql.HostDatatempinterfaceRtosql;
import com.gatherResulttosql.HostDatatempnDiskperfRtosql;
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
public class LoadSunOSFile {
	/**
	 * @param hostname
	 */
	
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	private String ipaddress;
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public LoadSunOSFile(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	/**
	 * Ĭ�Ϲ��췽��
	 */
	public LoadSunOSFile() {
		
	}
	
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode)
    {
		
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		
		ipaddress=host.getIpAddress();
		
		Hashtable returnHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		Vector cpuVector=new Vector();
		Vector systemVector=new Vector();
		Vector userVector=new Vector();
		Vector diskVector=new Vector();
		Vector processVector=new Vector();
		Nodeconfig nodeconfig = new Nodeconfig();
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();
		List netmedialist = new ArrayList();
		
		CPUcollectdata cpudata=null;
		Systemcollectdata systemdata=null;
		Usercollectdata userdata=null;
		Processcollectdata processdata=null;
		//Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
		if(host == null)return null;
		nodeconfig.setNodeid(host.getId());
		nodeconfig.setHostname(host.getAlias());
		float PhysicalMemCap = 0;
		float allPhyPagesSize = 0;
		float freePhysicalMemory =0; 
		float usedPhyPagesSize = 0;
		Hashtable networkconfig = new Hashtable();
		float SwapMemCap = 0;
		float freeSwapMemory =0;
		float usedSwapMemory =0;
		
		
    	try 
		{
			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/"+ipaddress+".log";				
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
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String strLine = null;
    		//�����ļ�����
    		while((strLine=br.readLine())!=null)
    		{
    			fileContent.append(strLine + "\n");
    			//SysLogger.info(strLine);
    		}
    		isr.close();
    		fis.close();
    		br.close();
    		try{
    			copyFile(ipaddress,getMaxNum(ipaddress));
    		}catch(Exception e){
    			e.printStackTrace();
    		}
		} 
    	catch (Exception e)
		{
			e.printStackTrace();
		}

    	Pattern tmpPt = null;
    	Matcher mr = null;
    	Calendar date = Calendar.getInstance();
    	
    	
	     //----------------����version����--���������---------------------        	
		String versionContent = "";
		tmpPt = Pattern.compile("(cmdbegin:version)(.*)(cmdbegin:vmstat)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			versionContent = mr.group(2);
			//System.out.println("=========cmdbegin:version========");
			//System.out.println(versionContent);
			//System.out.println("=========cmdbegin:version========");
			
		}
		if (versionContent != null && versionContent.length()>0){
			nodeconfig.setCSDVersion(versionContent.trim());
		}
		
		//----------------����cpuconfig����--���������---------------------        	
		String cpuconfigContent = "";
		tmpPt = Pattern.compile("(cmdbegin:cpuconfig)(.*)(cmdbegin:disk\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			cpuconfigContent = mr.group(2);
			
			//System.out.println("=========cmdbegin:cpuconfig========");
			//System.out.println(cpuconfigContent);
			//System.out.println("=========cmdbegin:cpuconfig========");
		}	
		
		List<Nodecpuconfig> cpuconfiglist = new ArrayList<Nodecpuconfig>();
		
		if(cpuconfigContent.trim().length()>30)
		{//�ж��Ƿ��вɼ�����cpu������Ϣû����Ϣ��������
			
			//System.out.println("+=======================cpuconfig=============================");
		String[] cpuconfigLineArr = cpuconfigContent.split("\n");
		String[] cpuconfig_tmpData = null;
		
		Nodecpuconfig nodecpuconfig = new Nodecpuconfig();
		//String procesors = "";
		//����������
		if(cpuconfigLineArr != null && cpuconfigLineArr.length>0){
			int processornum = 0;
			for(int i=0; i<cpuconfigLineArr.length;i++){
				
				String tempstr = cpuconfigLineArr[i].trim(); 
				if(tempstr.startsWith("The physical processor")){
					processornum = processornum +1;
				}
			}
			//����������
			nodeconfig.setNumberOfProcessors(processornum+"");
			
			int pid = 0;
			for(int i=0; i<cpuconfigLineArr.length;i++){
				
				String tempstr = cpuconfigLineArr[i].trim(); 
				if(tempstr.startsWith("The physical processor")){
					if(tempstr.contains("has 64 virtual processors")){
						nodecpuconfig.setL2CacheSpeed("");
						nodecpuconfig.setL2CacheSize("");
						nodecpuconfig.setDataWidth("64");
					}else{
						nodecpuconfig.setL2CacheSpeed("32");
						nodecpuconfig.setL2CacheSize("32");
						nodecpuconfig.setDataWidth("");
					}
					if(cpuconfigLineArr[i+1] != null){
						String twotempstr = cpuconfigLineArr[i+1].trim();
						String[] processorsresult = twotempstr.split("\\s++");
						if(processorsresult != null && processorsresult.length>0){
							nodecpuconfig.setDescrOfProcessors(processorsresult[0]);
							nodecpuconfig.setName(processorsresult[0]);
							nodecpuconfig.setProcessorId(pid+"");
							pid = pid+1;
						}
						i++;
					}
					nodecpuconfig.setNodeid(host.getId());
					
					cpuconfiglist.add(nodecpuconfig);
					nodecpuconfig = new Nodecpuconfig();
				}
			}
		}
		
		
//		nodeconfig.setDescrOfProcessors(cpuconfigLineArr[3].trim());
//		String[] processorsresult = cpuconfigLineArr[1].trim().split("\\s++");
//
//		//����������
//		nodeconfig.setNumberOfProcessors(processorsresult[4].trim());
//		cpuconfiglist.add(nodecpuconfig);
		}
		
		
		
		//��----------------����disk����--���������---------------------
		String diskContent = "";
		String diskLabel;
		List disklist = new ArrayList();
		//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&******************++++++++++++++++++++=");
		
		//�ű���û�ж�Ӧ�ĸ�ʽ
		tmpPt = Pattern.compile("(cmdbegin:disk\n)(.*)(cmdbegin:diskperf)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			diskContent = mr.group(2);
		    
			//System.out.println("=========cmdbegin:disk========");
			//System.out.println(diskContent);
			//System.out.println("=========cmdbegin:disk========");
		}
		String[] diskLineArr = diskContent.split("\n");
		String[] tmpData = null;
		Diskcollectdata diskdata=null;
		int diskflag = 0;
		for(int i=1; i<diskLineArr.length;i++)
		{
			
			tmpData = diskLineArr[i].split("\\s++");
			if((tmpData != null) && (tmpData.length == 6))
			{
				diskLabel = tmpData[5];
				
				diskdata=new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("Utilization");//���ðٷֱ�
				diskdata.setSubentity(tmpData[5]);
				diskdata.setRestype("static");
				diskdata.setUnit("%");
				try{
				diskdata.setThevalue(
						Float.toString(
								Float.parseFloat(
								tmpData[4].substring(
								0,
								tmpData[4].indexOf("%")))));
				}catch(Exception ex){
					continue;
				}
				diskVector.addElement(diskdata);

				diskdata=new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("AllSize");//�ܿռ�
				diskdata.setSubentity(tmpData[5]);
				diskdata.setRestype("static");

				float allblocksize=0;
				allblocksize=Float.parseFloat(tmpData[1]);
				float allsize=0.0f;
				allsize=allblocksize;
				if(allsize>=1024.0f&&allsize<1048576.0f){
					allsize=allsize/1024;
					diskdata.setUnit("M");
				}
				else if(allsize>=1048576.0f){
					allsize=allsize/1048576;
					diskdata.setUnit("G");
				}else{
					diskdata.setUnit("KB");
				}

				diskdata.setThevalue(Float.toString(allsize));
				diskVector.addElement(diskdata);

				diskdata=new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("UsedSize");//ʹ�ô�С
				diskdata.setSubentity(tmpData[5]);
				diskdata.setRestype("static");

				
				float usedfloatsize=0.0f;
				usedfloatsize = Float.parseFloat(tmpData[2]);
				if(usedfloatsize>=1024.0f&&allsize<1048576.0f){
					usedfloatsize=usedfloatsize/1024;
					diskdata.setUnit("M");
				}
				else if(usedfloatsize>=1048576.0f){
					usedfloatsize=usedfloatsize/1048576;
					diskdata.setUnit("G");
				}else{
					diskdata.setUnit("KB");
				}
				diskdata.setThevalue(Float.toString(usedfloatsize));
				diskVector.addElement(diskdata);
				disklist.add(diskflag,diskLabel);
				diskflag = diskflag +1;
			}
		}
		
		 //���д��̸澯���
	    //SysLogger.info("### ��ʼ���м������Ƿ�澯### ... ###");
	    try{
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "solaris");
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
		
		
		//----------------����diskperf����--���������---------------------        	
		String diskperfContent = "";
		String average = "";
		tmpPt = Pattern.compile("(cmdbegin:diskperf\n)(.*)(cmdbegin:netconf\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			diskperfContent = mr.group(2);
			//System.out.println("*****************************************************"+diskperfContent);
		} 
		String[] diskperfLineArr = diskperfContent.split("\n");
		String[] diskperf_tmpData = null;
		List alldiskperf = new ArrayList();
		Hashtable<String,String> diskperfhash = new Hashtable<String,String>();
		int flag = 0;
		for(int i=0; i<diskperfLineArr.length;i++){
			diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
			
			if(diskperf_tmpData != null && (diskperf_tmpData.length==7 || diskperf_tmpData.length==8)){
				
				if(diskperf_tmpData[0].trim().equalsIgnoreCase("Average")){
					flag = 1;
					
					diskperfhash.put("%busy", diskperf_tmpData[2].trim());
					diskperfhash.put("avque", diskperf_tmpData[3].trim());
					diskperfhash.put("r+w/s", diskperf_tmpData[4].trim());
					diskperfhash.put("Kbs/s", diskperf_tmpData[5].trim());
					diskperfhash.put("avwait", diskperf_tmpData[6].trim());
					diskperfhash.put("avserv", diskperf_tmpData[7].trim());
					diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
					alldiskperf.add(diskperfhash);
				}else if(flag == 1){
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
		
		
		//----------------����netconf����--���������---------------------        	
		String netconfContent = "";
		//String average = "";
		tmpPt = Pattern.compile("(cmdbegin:netconf)(.*)(cmdbegin:netperf)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			netconfContent = mr.group(2);
			//System.out.println("*****************************************************"+diskperfContent);
		} 
		String[] netconfLineArr = netconfContent.split("\n");
		String[] netconf_tmpData = null;
		List allnetconf = new ArrayList();
		Hashtable<String,String> netconfhash = new Hashtable<String,String>();
		flag = 0;
		for(int i=0; i<netconfLineArr.length;i++){
			netconf_tmpData = netconfLineArr[i].trim().split("\\s++");
			
			if(netconf_tmpData != null && netconf_tmpData.length==8){
				if(!netconf_tmpData[2].equalsIgnoreCase("unknown")){
					Hashtable netmediahash = new Hashtable(); 
					netmediahash.put("desc", netconf_tmpData[0]);//����
					netmediahash.put("speed", netconf_tmpData[4]+" "+netconf_tmpData[5] );//����
					netmediahash.put("mac", "");
					netmediahash.put("status", netconf_tmpData[2]);//����״̬
					netmedialist.add(netmediahash);
				}
			}				
		}
					
		
		//----------------����cpu����--���������---------------------        	
		String cpuperfContent = "";
		//String average = "";
		tmpPt = Pattern.compile("(cmdbegin:cpu)(.*)(cmdbegin:allmemory)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			cpuperfContent = mr.group(2);
			//System.out.println("================cmdbegin:cpu====================");
			//System.out.println(cpuperfContent);
			//System.out.println("================cmdbegin:cpu====================");
			
		} 
		String[] cpuperfLineArr = cpuperfContent.split("\n");
		List cpuperflist = new ArrayList();
		Hashtable<String,String> cpuperfhash = new Hashtable<String,String>();
		for(int i=0; i<cpuperfLineArr.length;i++){
			diskperf_tmpData = cpuperfLineArr[i].trim().split("\\s++");
			if(diskperf_tmpData != null && diskperf_tmpData.length==5){
				if(diskperf_tmpData[0].trim().equalsIgnoreCase("Average")){
					    
						cpuperfhash.put("%usr", diskperf_tmpData[1].trim());
						cpuperfhash.put("%sys", diskperf_tmpData[2].trim());
						cpuperfhash.put("%wio", diskperf_tmpData[3].trim());
						cpuperfhash.put("%idle", diskperf_tmpData[4].trim());
						cpuperflist.add(cpuperfhash);
						
						cpudata=new CPUcollectdata();
				   		cpudata.setIpaddress(ipaddress);
				   		cpudata.setCollecttime(date);
				   		cpudata.setCategory("CPU");
				   		cpudata.setEntity("Utilization");
				   		cpudata.setSubentity("Utilization");
				   		cpudata.setRestype("dynamic");
				   		cpudata.setUnit("%");
				   		cpudata.setThevalue(Arith.round((100.0-Double.parseDouble(diskperf_tmpData[4].trim())),0)+"");
				   		cpuVector.addElement(cpudata);
						//��CPUֵ���и澯���
				   		Hashtable collectHash = new Hashtable();
						collectHash.put("cpu", cpuVector);
					    try{
							AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
							List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "solaris","cpu");
							for(int k = 0 ; k < list.size() ; k ++){
								AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
								//��CPUֵ���и澯���
								CheckEventUtil checkutil = new CheckEventUtil();
								checkutil.updateData(host,collectHash,"host","solaris",alarmIndicatorsnode);
								//}
							}
					    }catch(Exception e){
					    	e.printStackTrace();
					    }
					
				}
			}				
		}
		
		//----------------����memory����--���������---------------------        	
		String memperfContent = "";
		tmpPt = Pattern.compile("(cmdbegin:vmstat)(.*)(cmdbegin:swapinfo)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			memperfContent = mr.group(2);
			//System.out.println("================cmdbegin:vmstat=======================");
			//System.out.println(memperfContent);
			//System.out.println("================cmdbegin:vmstat=======================");
			
		} 
		String[] memperfLineArr = memperfContent.split("\n");
		List memperflist = new ArrayList();
		Vector memoryVector=new Vector();
		String[] memory_tempData = null;
		Memorycollectdata memorydata=null;
		Hashtable<String,String> memperfhash = new Hashtable<String,String>();
		for(int i=0; i<memperfLineArr.length;i++){
			
			
			int j = memperfLineArr.length;
			diskperf_tmpData = memperfLineArr[j-1].trim().split("\\s++");
			if(diskperf_tmpData != null && diskperf_tmpData.length>=22){
//				if(memperfLineArr[i].trim().contains(":")){
//					memory_tempData = memperfLineArr[i].trim().split(":");
//					
//						if(memory_tempData[0].trim().equalsIgnoreCase("Memory size")){
//							String allphy = memory_tempData[1].trim().trim();
//							try{
//								allphy = allphy.replaceAll("Megabytes", "");
//								PhysicalMemCap = Float.parseFloat(allphy);
//							}catch(Exception e){
//								e.printStackTrace();
//							}
//						}
//					}
				if (diskperf_tmpData[0]!=null && !diskperf_tmpData[0].equalsIgnoreCase("r")){
					//freeMemory
					freePhysicalMemory = Integer.parseInt(diskperf_tmpData[4])*4/1024/1024;	
					
					
					
				}
					memperfhash.put("free",String.valueOf(freePhysicalMemory));
					memperflist.add(memperfhash);
					memperfhash = new Hashtable();
					//Memory
					
				
			}
		}
		
		//System.out.println("+++++===============+freePhysicalMemory="+freePhysicalMemory);
		
		String mem_Content = "";
		tmpPt = Pattern.compile("(cmdbegin:allmemory)(.*)(cmdbegin:cpuconfig)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			mem_Content = mr.group(2);
		} 
		String[] mem_fLineArr = mem_Content.split("\n");
		List mem_list = new ArrayList();
		Vector mem_Vector=new Vector();
		String[] mem_tempData = null;
		Memorycollectdata mem_data=null;
		Hashtable<String,String> mem_fhash = new Hashtable<String,String>();
		for(int i=0; i<mem_fLineArr.length;i++){
			
			
				if(mem_fLineArr[i].trim().contains(":")){
					memory_tempData = mem_fLineArr[i].trim().split(":");
					
						if(memory_tempData[0].trim().equalsIgnoreCase("Memory size")){
							String allphy = memory_tempData[1].trim().trim();
							try{
								allphy = allphy.replaceAll("Megabytes", "");
								PhysicalMemCap = Float.parseFloat(allphy);
								//System.out.println("==================PhysicalMemCap====================="+PhysicalMemCap);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					
				
			}
		if(PhysicalMemCap > 0){    
			//System.out.println("========================"+(PhysicalMemCap-freePhysicalMemory));
		float PhysicalMemUtilization =(PhysicalMemCap-freePhysicalMemory)* 100/ PhysicalMemCap;
					
					memorydata=new Memorycollectdata();
		  			memorydata.setIpaddress(ipaddress);
		  			memorydata.setCollecttime(date);
		  			memorydata.setCategory("Memory");
		  			memorydata.setEntity("Capability");
		  			memorydata.setSubentity("PhysicalMemory");
		  			memorydata.setRestype("static");
		  			memorydata.setUnit("M");
		  			memorydata.setThevalue(
		  					Float.toString(PhysicalMemCap));
		  			memoryVector.addElement(memorydata);
		  			
		  			memorydata=new Memorycollectdata();
		  			memorydata.setIpaddress(ipaddress);
		  			memorydata.setCollecttime(date);
		  			memorydata.setCategory("Memory");
		  			memorydata.setEntity("UsedSize");
		  			memorydata.setSubentity("PhysicalMemory");
		  			memorydata.setRestype("static");
		  			memorydata.setUnit("M");
		  			memorydata.setThevalue(
		  					Float.toString(PhysicalMemCap-freePhysicalMemory));
		  			memoryVector.addElement(memorydata);
		  			
		  			memorydata=new Memorycollectdata();
		  			memorydata.setIpaddress(ipaddress);
		  			memorydata.setCollecttime(date);
		  			memorydata.setCategory("Memory");
		  			memorydata.setEntity("Utilization");
		  			memorydata.setSubentity("PhysicalMemory");
		  			memorydata.setRestype("dynamic");
		  			memorydata.setUnit("%");
		  			memorydata.setThevalue(Math.round(PhysicalMemUtilization)+"");
		  			memoryVector.addElement(memorydata);
		  			
		  			Vector phymemV = new Vector();
					phymemV.add(memorydata);
		  			Hashtable collectHash = new Hashtable();
					collectHash.put("physicalmem", phymemV);
		  			//�������ڴ�ֵ���и澯���
				    try{
						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
						List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "solaris","physicalmemory");
						for(int i = 0 ; i < list.size() ; i ++){
							AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
							//�������ڴ�ֵ���и澯���
							CheckEventUtil checkutil = new CheckEventUtil();
							checkutil.updateData(host,collectHash,"host","solaris",alarmIndicatorsnode);
							//}
						}
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
		}
			
		  		//��----------------����swap����--���������---------------------        	
		  			String swap_Content = "";
		  			tmpPt = Pattern.compile("(cmdbegin:swapinfo)(.*)(cmdbegin:process)",
		  					Pattern.DOTALL);
		  			mr = tmpPt.matcher(fileContent.toString());
		  			if(mr.find())
		  			{
		  				try{
		  					swap_Content = mr.group(2);
		  				}catch(Exception e){
		  					e.printStackTrace();
		  				}
		  				
		  			} 
		  			String[] swap_LineArr = null;
		  			String[] swap_tmpData = null;
		  			
		  			try{
		  				swap_LineArr = swap_Content.trim().split("\n");
		  				for(int i=0; i<swap_LineArr.length;i++)
		  				{    			
		  					
		  					swap_tmpData = swap_LineArr[0].trim().split("\\s++");
		  					if(swap_tmpData != null && swap_tmpData.length==12){
		  						try{
		  							String swap1=swap_tmpData[10].replaceAll("k", "");
		  							String swap2=swap_tmpData[8].replaceAll("k", "");
		  							
		  							freeSwapMemory = Float.parseFloat(swap1.trim());
		  							usedSwapMemory = Float.parseFloat(swap2.trim());
		  							SwapMemCap = freeSwapMemory + usedSwapMemory;
		  							//SysLogger.info(SwapMemCap+"===="+freeSwapMemory+"===="+usedSwapMemory);
		  						}catch(Exception e){
		  							e.printStackTrace();
		  						}
		  					}
		  				}
		  				if(SwapMemCap > 0){		
			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("Capability");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("static");
  			memorydata.setUnit("M");
  			memorydata.setThevalue(SwapMemCap/1024+"");
  			memoryVector.addElement(memorydata);
  			
  			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("UsedSize");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("static");
  			memorydata.setUnit("M");
  			memorydata.setThevalue(usedSwapMemory/1024+"");
  			memoryVector.addElement(memorydata);
			
  			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("Utilization");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("dynamic");
  			memorydata.setUnit("%");
  			memorydata.setThevalue(Math.round(usedSwapMemory*100/SwapMemCap)+"");
  			memoryVector.addElement(memorydata);
  			
  			Vector swapmemV = new Vector();
  			swapmemV.add(memorydata);
  			Hashtable collectHash = new Hashtable();
			collectHash.put("swapmem", swapmemV);				
			//�Խ����ڴ�ֵ���и澯���
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "solaris","swapmemory");
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					//�Խ����ڴ�ֵ���и澯���
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.updateData(host,collectHash,"host","solaris",alarmIndicatorsnode);
					//}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		  }
	
		}catch(Exception e){
			e.printStackTrace();
	  	}	
		//��----------------����process����--���������---------------------        	
		String processContent = "";
		tmpPt = Pattern.compile("(cmdbegin:process)(.*)(cmdbegin:cpu\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			processContent = mr.group(2);
		} 
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
		if (procslist != null && procslist.size()>0){
			for(int i=0;i<procslist.size();i++){
				Procs procs = (Procs)procslist.get(i);
				procshash.put(procs.getProcname(),procs);
				procsV.add(procs.getProcname());
			}
		}		
		String[] process_LineArr = processContent.split("\n");
		String[] processtmpData = null;
		float cpuusage = 0.0f;
		for(int i=1; i<process_LineArr.length;i++)
		{    			
			processtmpData = process_LineArr[i].trim().split("\\s++");
			///System.out.println("####="+process_LineArr[i]);
			//System.out.println("#######=����"+processtmpData.length);
			
			if((processtmpData != null) && (processtmpData.length == 7)){
				String pid=processtmpData[0];//pid
				if("pid".equalsIgnoreCase(pid))continue;
				String cmd = "";
				
				
				if(processtmpData.length == 7){
					//for(int k=12;k<processtmpData.length;k++){
						cmd = cmd+" "+processtmpData[6];
					//}
				}
				cmd = cmd.trim();//command
				//System.out.println("&&&&&&&&&&&&&&&&&**************************"+cmd);
				
				String vbstring1=processtmpData[6];//command
				String vbstring2="Ӧ�ó���";
				String vbstring3="��������";
				String vbstring4=processtmpData[5];//memsize
				if (vbstring4 == null)vbstring4="0";
				String vbstring5=processtmpData[4];//cputime
				if(vbstring5.contains("-")){
					//vbstring5 = "0.0";
					String[] vbstrings = vbstring5.split("-");//3-05:56:42
					String[] timestrs = vbstrings[1].split(":");//05:56:42
					int hour = Integer.parseInt(vbstrings[0])*24+Integer.parseInt(timestrs[0]);//3*24 + 5
					timestrs[0] = hour+"";
					StringBuffer cputimeBuffer = new StringBuffer();
					for(int m=0; m<timestrs.length; m++){
						cputimeBuffer.append(timestrs[m]);
						if(m != timestrs.length-1){
							cputimeBuffer.append(":");
						}
					}
					vbstring5 = cputimeBuffer.toString();
				}
				String vbstring6=processtmpData[3];//%mem
				if(vbstring6.contains("-")){
					vbstring6 = "0.0";
				}
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
					createSMS(procs);		     			
	     		}catch(Exception e){
	     			e.printStackTrace();
	     		}
	     	}
	
    	
		systemdata=new Systemcollectdata();
		systemdata.setIpaddress(ipaddress);
		systemdata.setCollecttime(date);
		systemdata.setCategory("System");
		systemdata.setEntity("ProcessCount");
		systemdata.setSubentity("ProcessCount");
		systemdata.setRestype("static");
		systemdata.setUnit(" ");
		systemdata.setThevalue(process_LineArr+"");
		systemVector.addElement(systemdata);	
	     }
		
		//��----------------����uname����--���������---------------------        	
		String unameContent = "";
		tmpPt = Pattern.compile("(cmdbegin:uname)(.*)(cmdbegin:usergroup)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			unameContent = mr.group(2);
		} 
		String[] unameLineArr = unameContent.split("\n");
		String[] uname_tmpData = null;
		for(int i=0; i<unameLineArr.length;i++){
			uname_tmpData = unameLineArr[i].split("\\s++");				
			if (uname_tmpData.length==2){	
				  systemdata=new Systemcollectdata();
				  systemdata.setIpaddress(ipaddress);
				  systemdata.setCollecttime(date);
				  systemdata.setCategory("System");
				  systemdata.setEntity("operatSystem");
				  systemdata.setSubentity("operatSystem");
				  systemdata.setRestype("static");
				  systemdata.setUnit(" ");
				  systemdata.setThevalue(uname_tmpData[0]);
				  systemVector.addElement(systemdata);
				  
					systemdata=new Systemcollectdata();
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
		
		//��----------------����usergroup����--���������--------------------- 
		Hashtable usergrouphash = new Hashtable();
		String usergroupContent = "";
		tmpPt = Pattern.compile("(cmdbegin:usergroup)(.*)(cmdbegin:user)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			usergroupContent = mr.group(2);
		} 
		String[] usergroupLineArr = usergroupContent.split("\n");
		String[] usergroup_tmpData = null;
		for(int i=0; i<usergroupLineArr.length;i++){
			usergroup_tmpData = usergroupLineArr[i].split(":");				
			if (usergroup_tmpData.length>=3){	
				usergrouphash.put((String)usergroup_tmpData[2], usergroup_tmpData[0]);
			}				
		}
		
		//��----------------����user����--���������---------------------        	
		String userContent = "";
		tmpPt = Pattern.compile("(cmdbegin:user)(.*)(cmdbegin:date)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			userContent = mr.group(2);
		} 
		String[] userLineArr = userContent.split("\n");
		String[] user_tmpData = null;
		for(int i=0; i<userLineArr.length;i++){
			String[] result = userLineArr[i].trim().split(":x:");
			if(result.length>=2){
				String userName = result[0];
	
				String group = result[1];
//				String[] groups = groupStr.split("=");
//				String group ="";
//				if(groups != null && groups.length==2){
//					group = groups[1];
//				}
				//String userid = result[1];
				//int usergroupid = Integer.parseInt(result[3]);
				//С��500��Ϊϵͳ���û�,����
				//if(userid < 500)continue;
				
				userdata=new Usercollectdata();
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
		
		//��----------------����date����--���������---------------------        	
		String dateContent = "";
		tmpPt = Pattern.compile("(cmdbegin:date)(.*)(cmdbegin:uptime)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			dateContent = mr.group(2);
		}
		if (dateContent != null && dateContent.length()>0){
			systemdata=new Systemcollectdata();
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

		//��----------------����uptime����--���������---------------------        	
		String uptimeContent = "";
		tmpPt = Pattern.compile("(cmdbegin:uptime)(.*)(cmdbegin:end)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			uptimeContent = mr.group(2);
		}
		if (uptimeContent != null && uptimeContent.length()>0){
			systemdata=new Systemcollectdata();
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
		
		
		

		//----------------------------����mac ��ַ��Ϣ-----------------------------------
		String netmac= "";
		//String average = "";
		tmpPt = Pattern.compile("(netmac:start\n)(.*)(netmac:end\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			netmac = mr.group(2);
		} 
		String[] netmacLineArr = netmac.split("\n");
		String[] netmmac_tmpData = null;
		Hashtable<String,String> netmacfhash = new Hashtable<String,String>();
		
		boolean infflg=false;
		boolean macflg=false;
		String infname="";//��������
		String macstr="";//mac��ַ
		String macindex="";//��ϸ��Ϣ��mac��Ϣ
		
		//������1������ key Ϊ�����Ľ�����ƣ�valueΪ������mac��ַ
		
		
		//����ѭ����¼
		for(int i=0; i<netmacLineArr.length;i++){
			//System.out.println("=====����mac����������"+netmacLineArr[i]);
			
			
			if(netmacLineArr[i].indexOf("flags=")>0)
			{
				
				infname=netmacLineArr[i].substring(0,netmacLineArr[i].indexOf("flags="));
				infname=infname.trim().replaceAll(":", "");
				//System.out.println("======"+infname);
				infflg=true;
			}
			
			if(infflg=true && netmacLineArr[i].indexOf("ether")>0)
			{//������mac��ַ
				macstr=netmacLineArr[i].trim();
				macstr=macstr.replace("ether", "");
				netmacfhash.put(infname.trim(), macstr.trim());//������
				if(i!=netmacLineArr.length-1)
				{
				macindex=macindex+macstr+",";
				}else
				{
					macindex=macindex+macstr;
				}
				//System.out.println("=======�ҵ�mac��ַ===="+netmacfhash.toString());
				infflg=false;
			}
						 
						
		}
		//netmacfhash.put("lo0", "");//���һ��Ĭ�ϵ�û��mac������
		//System.out.println("=="+netmacfhash.toString());
		
		
		
		nodeconfig.setMac(macindex);//��������mac��Ϣ
		
		
		//----------------------------����������Ϣ-----------------------------------
		String netconf = "";
		//String average = "";
		tmpPt = Pattern.compile("(cmdbegin:netconf\n)(.*)(cmdbegin:netperf\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			netconf = mr.group(2);
			//System.out.println("************����������Ϣ************************************"+netconf);
		} 
		String[] netconfigLineArr = netconf.split("\n");
		String[] netconfig_tmpData = null;
		List netconflist = new ArrayList();
		
		Hashtable<String,String> netstatfhash = new Hashtable<String,String>();//����״̬
		Hashtable<String,String> netspeedhash = new Hashtable<String,String>();//����״̬
		//int flag = 0;
		
		//String liststat="";//����״̬
		for(int i=0; i<netconfigLineArr.length;i++){
			//System.out.println("=====��������״̬��Ϣ����������"+netconfigLineArr[i]);
			netconfig_tmpData = netconfigLineArr[i].trim().split("\\s++");
			Hashtable<String,String> netconfigfhash = new Hashtable<String,String>();
			//System.out.println("==="+netconfig_tmpData.length);
			//����������Ҫ��3������ifname(��������),linkstat(����״̬ up��down��unknown)
			//speed(��������) mac��ַ
			if(netconfig_tmpData != null && (netconfig_tmpData.length==8)){
				  
					netconfigfhash.put("ifname", netconfig_tmpData[0].trim());//������
					//������״̬����
					netstatfhash.put(netconfig_tmpData[0].trim(), netconfig_tmpData[2].trim());
					//��������
					netspeedhash.put(netconfig_tmpData[0].trim(), netconfig_tmpData[4].trim()+netconfig_tmpData[5].trim());
					//��������״̬����
					//liststat=;	
					netconfigfhash.put("linkstat", netconfig_tmpData[2].trim());//����״̬
//					System.out.println("=============="+netmacfhash.toString());
//					System.out.println("==============="+netconfig_tmpData[0].trim());
					try{
					netconfigfhash.put("mac", netmacfhash.get(netconfig_tmpData[0].trim()));//mac��ַ
					}catch(Exception e){//û���ҵ���Ӧ��������Ϣ
						
						netconfigfhash.put("mac", "");//mac��ַ
					}
					
					
					
					netconfigfhash.put("speed", netconfig_tmpData[4].trim()+netconfig_tmpData[5].trim());//��������
					//System.out.println("=="+netconfigfhash.toString());
					
					if(!"lo0".equals(netconfig_tmpData[0].trim()))
					{//�������� lo0
					netconflist.add(netconfigfhash);
					}
			}				
		}
		
//		System.out.println("-----------------����״̬��Ϣ��---"+netconflist.size());
		
		//----------------------------����������Ϣ-----------------------------------
		String netflow = "";
		//String average = "";
		tmpPt = Pattern.compile("(netflow:start\n)(.*)(netflow:end\n)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			netconf = mr.group(2);
			//System.out.println("************����������Ϣ************************************"+netconf);
		} 
		String[] netflowLineArr = netconf.split("\n");
		String[] netflow_tmpData = null;
		List netflowlist = new ArrayList();
		
		//int flag = 0;
		
		//String liststat="";//����״̬
		int ifindex = 0;
		for(int i=1; i<netflowLineArr.length;i++){
			
			//System.out.println(netflowLineArr[i].trim());
			netflow_tmpData = netflowLineArr[i].trim().split("\\s++");
			//System.out.println(netflow_tmpData.length);
			//��������������Ϣ
			if(netconfig_tmpData != null && (netflow_tmpData.length==10)){
				  
				Hashtable<String,String> netflowfhash = new Hashtable<String,String>();
				    //����������9������
				    // Int(��������) rKB(�������kB/s) wKB(��������kb/s)  rPk 
				   //wPk(������ݰ�/s)    rAvs(���ƽ��kb/s)    wAvs(����ƽ��kb/s)  
				   //Util(������kb/s)  Sat state(����״̬) speed(��������)
				    netflowfhash.put("Int", netflow_tmpData[1].trim());//������
				   // System.out.println("-----"+netstatfhash.toString());
				    
				   // String infstat=;
				   // System.out.println("-----"+infstat);
					//��������״̬����
					//liststat=;	
					netflowfhash.put("rKB", netflow_tmpData[2].trim());
					netflowfhash.put("wKB", netflow_tmpData[3].trim());
					netflowfhash.put("rPk", netflow_tmpData[4].trim());
					netflowfhash.put("wPk", netflow_tmpData[5].trim());
					netflowfhash.put("rAvs", netflow_tmpData[6].trim());
					netflowfhash.put("wAvs", netflow_tmpData[7].trim());
					netflowfhash.put("Util", netflow_tmpData[8].trim());
					netflowfhash.put("Sat", netflow_tmpData[9].trim());
					
					netflowfhash.put("state", netstatfhash.get(netflow_tmpData[1].trim())+"");
					netflowfhash.put("speed", netspeedhash.get(netflow_tmpData[1].trim())+"");
					
					if(!"lo0".equals(netflow_tmpData[1].trim()))
					{//�������� lo0
						
					   	//�˿�����
						Interfacecollectdata interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("index");
						interfacedata.setSubentity(ifindex+"");
						//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(ifindex+"");
						interfacedata.setChname("�˿�����");
						interfaceVector.addElement(interfacedata);
						//�˿�����
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifDescr");
						interfacedata.setSubentity(ifindex+"");
						//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue(netflow_tmpData[1].trim());
						interfacedata.setChname("�˿�����2");
						interfaceVector.addElement(interfacedata);
						//�˿ڴ���
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifSpeed");
						interfacedata.setSubentity(ifindex+"");
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue("");
						interfacedata.setChname("");
						interfaceVector.addElement(interfacedata);
						//��ǰ״̬
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(ipaddress);
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("ifOperStatus");
						interfacedata.setSubentity(ifindex+"");
						interfacedata.setRestype("static");
						interfacedata.setUnit("");
						interfacedata.setThevalue("");
						interfacedata.setChname("��ǰ״̬");
						interfaceVector.addElement(interfacedata);
//						//��ǰ״̬
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(ipaddress);
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("ifOperStatus");
//						interfacedata.setSubentity(ifindex+"");
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("");
//						interfacedata.setThevalue(1+"");
//						interfacedata.setChname("��ǰ״̬");
//						interfaceVector.addElement(interfacedata);
						//�˿��������
						UtilHdx utilhdx=new UtilHdx();
						utilhdx.setIpaddress(ipaddress);
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						String chnameBand="";
						utilhdx.setEntity("InBandwidthUtilHdx");
						utilhdx.setThevalue(netflow_tmpData[2].trim());
						utilhdx.setSubentity(ifindex+"");
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("Kb/��");	
						utilhdx.setChname(ifindex+"�˿����"+"����");
						utilhdxVector.addElement(utilhdx);
						//�˿ڳ�������
						utilhdx=new UtilHdx();
						utilhdx.setIpaddress(ipaddress);
						utilhdx.setCollecttime(date);
						utilhdx.setCategory("Interface");
						utilhdx.setEntity("OutBandwidthUtilHdx");
						utilhdx.setThevalue(netflow_tmpData[3].trim());
						utilhdx.setSubentity(ifindex+"");
						utilhdx.setRestype("dynamic");
						utilhdx.setUnit("Kb/��");	
						utilhdx.setChname(ifindex+"�˿ڳ���"+"����");
						utilhdxVector.addElement(utilhdx);
						ifindex = ifindex + 1;
						netflowlist.add(netflowfhash);
					}
			}				
		}
		try{
			deleteFile(ipaddress);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (diskVector != null && diskVector.size()>0)
			{
			returnHash.put("disk",diskVector);
			
			 //�Ѳɼ��������sql
		    HostdiskResultosql tosql=new HostdiskResultosql();
		    tosql.CreateResultTosql(returnHash, host.getIpAddress());
	
		    HostDatatempDiskRttosql temptosql=new HostDatatempDiskRttosql();
		    temptosql.CreateResultTosql(returnHash, host);
		    tosql=null;
		    temptosql=null;
			}
		if (cpuVector != null && cpuVector.size()>0)
			{
			returnHash.put("cpu",cpuVector);
			
			NetHostDatatempCpuRTosql totempsql=new NetHostDatatempCpuRTosql();
			 totempsql.CreateResultTosql(returnHash, host);
			 totempsql=null;
			}
		if (memoryVector != null && memoryVector.size()>0)
			{
			returnHash.put("memory",memoryVector);
			
			//�Ѳɼ��������sql
		    HostPhysicalMemoryResulttosql  tosql=new HostPhysicalMemoryResulttosql();
		    tosql.CreateResultTosql(returnHash, host.getIpAddress());
		    NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
		    totempsql.CreateResultTosql(returnHash, host);
			}
		if (userVector != null && userVector.size()>0)
			{
			returnHash.put("user",userVector);
			
			HostDatatempUserRtosql tosql=new HostDatatempUserRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (processVector != null && processVector.size()>0)
			{
			returnHash.put("process",processVector);
			
			//�ѽ������sql
			HostDatatempProcessRtTosql temptosql=new HostDatatempProcessRtTosql();
			temptosql.CreateResultTosql(returnHash, host);
			}
		if (systemVector != null && systemVector.size()>0)
			{
			returnHash.put("system",systemVector);
			
			NetHostDatatempSystemRttosql tosql=new NetHostDatatempSystemRttosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (nodeconfig != null)
			{
			returnHash.put("nodeconfig",nodeconfig);
			
			HostDatatempNodeconfRtosql tosql=new HostDatatempNodeconfRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		//if (iflist != null && iflist.size()>0)returnHash.put("iflist",iflist);
		if (utilhdxVector != null && utilhdxVector.size()>0)
			{
			returnHash.put("utilhdx",utilhdxVector);
			
			HostDatatemputilhdxRtosql tosql=new HostDatatemputilhdxRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (interfaceVector != null && interfaceVector.size()>0)
			{
			returnHash.put("interface",interfaceVector);
			
			HostDatatempinterfaceRtosql tosql=new HostDatatempinterfaceRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (alldiskperf != null && alldiskperf.size()>0)
			{
			returnHash.put("alldiskperf",alldiskperf);
			
			HostDatatempnDiskperfRtosql tosql=new HostDatatempnDiskperfRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (cpuconfiglist != null && cpuconfiglist.size()>0)
			{
			returnHash.put("cpuconfiglist",cpuconfiglist);
			
			HostDatatempCpuconfiRtosql tosql=new HostDatatempCpuconfiRtosql();
			tosql.CreateResultTosql(returnHash, host);
			}
		if (cpuperflist != null && cpuperflist.size()>0)
			{
			returnHash.put("cpuperflist",cpuperflist);
			
			HostcpuResultTosql rtosql=new HostcpuResultTosql();
			rtosql.CreateLinuxResultTosql(returnHash, host.getIpAddress());
			
			HostDatatempCpuperRtosql tmptosql=new HostDatatempCpuperRtosql();
			tmptosql.CreateResultTosql(returnHash, host);
			}
		if (netmedialist != null && netmedialist.size()>0)
			{
			returnHash.put("netmedialist",netmedialist);
			}
		if (netconflist != null && netconflist.size()>0)
			{
			returnHash.put("netconflist",netconflist);
			
			
			}
		if (netflowlist != null && netflowlist.size()>0)
			{
			returnHash.put("netflowlist",netflowlist);
			}
		
		ShareData.getSharedata().put(host.getIpAddress(), returnHash);
		
		
		return returnHash;
		  		
		  			}	  		
		
    public String getMaxNum(String ipAddress){
    	String maxStr = null;
		File logFolder = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/");
   		String[] fileList = logFolder.list();
   		
   		for(int i=0;i<fileList.length;i++) //��һ�����µ��ļ�
   		{
   			if(!fileList[i].startsWith(ipAddress)) continue;
   			
   			return ipAddress;
   		}
   		return maxStr;
    }
	
    public void deleteFile(String ipAddress){

			try
			{
				File delFile = new File(ResourceCenter.getInstance().getSysPath() + "linuxserver/" + ipAddress + ".log");
			//System.out.println("###��ʼɾ���ļ���"+delFile);
			//delFile.delete();
			//System.out.println("###�ɹ�ɾ���ļ���"+delFile);
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
	 public void createSMS(Procs procs){
		 	Procs lastprocs = null;
		 	//��������	
		 	procs.setCollecttime(Calendar.getInstance());
		 	//���Ѿ����͵Ķ����б����õ�ǰ��PROC�Ѿ����͵Ķ���
		 	lastprocs = (Procs)sendeddata.get(procs.getIpaddress()+":"+procs.getProcname());	
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
	 private String changStr(String str){
			String ss="";
			char all[]=str.toCharArray();
			for(int t=0;t<all.length;t++){
				if(all[t]!='k'){
					ss+=all[t];
				}
			}
			return ss;
		}
}







