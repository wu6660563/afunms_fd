package com.afunms.polling.snmp;

/*
 * @author hukelei@dhcc.com.cn
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

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.Arith;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Procs;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Systemcollectdata;

/**
 * @author Administrator   
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadScoUnixWareFile {       
	/**     
	 * @param hostname         
	 */
	private String ipaddress;
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public LoadScoUnixWareFile(String ipaddress) {
		this.ipaddress = ipaddress;  
	}
	
	public Hashtable getTelnetMonitorDetail()
    {
		
		//yangjun
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(ipaddress);
		if(ipAllData == null)ipAllData = new Hashtable();
		
		Hashtable returnHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		Vector cpuVector=new Vector();
		Vector systemVector=new Vector();
		Vector diskVector=new Vector();
		Vector processVector=new Vector();
		Nodeconfig nodeconfig = new Nodeconfig();
		String collecttime = "";
		
		
		CPUcollectdata cpudata=null;
		Systemcollectdata systemdata=null;
		Processcollectdata processdata=null;
		Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
		if(host == null)return null;
		nodeconfig.setNodeid(host.getId());
		nodeconfig.setHostname(host.getAlias());
		float PhysicalMemCap = 0;
		float freePhysicalMemory =0; 
		float SwapMemCap = 0;
		float freeSwapMemory =0;
		float usedSwapMemory =0;
		
    	try 
		{
			String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/"+ipaddress+".log";				
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
		tmpPt = Pattern.compile("(cmdbegin:version)(.*)(cmdbegin:swap)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			versionContent = mr.group(2);
//			System.out.println("================================version=====");
//			System.out.println(versionContent);
//			System.out.println("================================version===end==");
		}
		String[] versionLineArr = versionContent.split("\n");
		if (versionLineArr != null && versionLineArr.length >0){
			for(int i=1; i<versionLineArr.length;i++){
				String[] vstr = versionLineArr[i].split("=");
				if(vstr[0]!=null&&"version".equalsIgnoreCase(vstr[0])){
					nodeconfig.setCSDVersion(vstr[1]);
				}
				if(vstr[0]!=null&&"hostname".equalsIgnoreCase(vstr[0])){
					nodeconfig.setHostname(vstr[1]);
				}
				if(vstr[0]!=null&&"sysname".equalsIgnoreCase(vstr[0])){
					nodeconfig.setSysname(vstr[1]);
				}
				if(vstr[0]!=null&&"hw_serial".equalsIgnoreCase(vstr[0])){
					nodeconfig.setSerialNumber(vstr[1]);
				}
			}
		}
		else
		{
			nodeconfig.setCSDVersion("");
			nodeconfig.setHostname("");
			nodeconfig.setSysname("");
			nodeconfig.setSerialNumber("");
		}
		nodeconfig.setNumberOfProcessors("1");
		nodeconfig.setMac("");
		//��----------------����swap����--���������---------------------        	
		String swap_Content = "";
		tmpPt = Pattern.compile("(cmdbegin:swap)(.*)(cmdbegin:process)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			try{
				swap_Content = mr.group(2);
				
//				System.out.println("================================swap=====");
//				System.out.println(swap_Content);
//				System.out.println("================================swap===end==");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} 
		String[] swap_LineArr = null;
		String[] swap_tmpData = null;
		try{
			swap_LineArr = swap_Content.trim().split("\n");
			if(swap_LineArr != null && swap_LineArr.length>0){
				swap_tmpData = swap_LineArr[0].trim().split("\\s++");	
				if(swap_tmpData != null && swap_tmpData.length==12){
					
					try{
//						System.out.println("==============================�����ɹ�swap");
						SwapMemCap = Float.parseFloat(swap_tmpData[4]);
						freeSwapMemory = Float.parseFloat(swap_tmpData[10]);
						usedSwapMemory = Float.parseFloat(swap_tmpData[7]);
						SysLogger.info(SwapMemCap+"===="+freeSwapMemory+"===="+usedSwapMemory);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//��----------------����disk����--���������---------------------
		String diskContent = "";
		String diskLabel;
		List disklist = new ArrayList();
		tmpPt = Pattern.compile("(cmdbegin:disk\n)(.*)(cmdbegin:diskperf)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			diskContent = mr.group(2);
//			System.out.println("==================(cmdbegin:disk\n)(.*)(cmdbegin:diskperf)======================");
//			System.out.println(diskContent);
//			System.out.println("==================(cmdbegin:disk\n)(.*)(cmdbegin:diskperf)======================");
			
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

				//yangjun 
				try {
					String diskinc = "0.0";
					float pastutil = 0.0f;
					Vector disk_v = (Vector)ipAllData.get("disk");
					if (disk_v != null && disk_v.size() > 0) {
						for (int si = 0; si < disk_v.size(); si++) {
							Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
							if((tmpData[5]).equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
								pastutil = Float.parseFloat(disk_data.getThevalue());
							}
						}
					} else {
						pastutil = Float.parseFloat(tmpData[4].substring(0,tmpData[4].indexOf("%")));
					}
					if (pastutil == 0) {
						pastutil = Float.parseFloat(
								tmpData[4].substring(
										0,
										tmpData[4].indexOf("%")));
					}
					if(Float.parseFloat(
									tmpData[4].substring(
									0,
									tmpData[4].indexOf("%")))-pastutil>0){
						diskinc = (Float.parseFloat(
										tmpData[4].substring(
										0,
										tmpData[4].indexOf("%")))-pastutil)+"";
					}
					diskdata = new Diskcollectdata();
					diskdata.setIpaddress(host.getIpAddress());
					diskdata.setCollecttime(date);
					diskdata.setCategory("Disk");
					diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
					diskdata.setSubentity(tmpData[5]);
					diskdata.setRestype("dynamic");
					diskdata.setUnit("%");
					diskdata.setThevalue(diskinc);
					diskVector.addElement(diskdata);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				
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
				if(allsize>=1024.0f){
					allsize=allsize/(1024*1024);
					diskdata.setUnit("G");
				}
				else{
					allsize=allsize/1024;
					diskdata.setUnit("M");
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

				float FreeintSize=0;
				FreeintSize=Float.parseFloat(tmpData[3]);
				
				
				float usedfloatsize=0.0f;
				usedfloatsize = allblocksize - FreeintSize;
				if(usedfloatsize>=1024.0f){
					usedfloatsize=usedfloatsize/(1024*1024);
					diskdata.setUnit("G");
				}
				else{
					usedfloatsize=usedfloatsize/1024;
					diskdata.setUnit("M");
				}
				diskdata.setThevalue(Float.toString(usedfloatsize));
				diskVector.addElement(diskdata);
				disklist.add(diskflag,diskLabel);
				diskflag = diskflag +1;
			}
		}
		//----------------����diskperf����--���������---------------------        	
		String diskperfContent = "";
		String average = "";
		tmpPt = Pattern.compile("(cmdbegin:diskperf)(.*)(cmdbegin:netperf)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			diskperfContent = mr.group(2);
			
		} 
		String[] diskperfLineArr = diskperfContent.split("\n");
		String[] diskperf_tmpData = null;
		List alldiskperf = new ArrayList();
		Hashtable<String,String> diskperfhash = new Hashtable<String,String>();
		int flag = 0;
		for(int i=0; i<diskperfLineArr.length;i++){
			diskperf_tmpData = diskperfLineArr[i].trim().split("\\s++");
			if(diskperf_tmpData != null && (diskperf_tmpData.length==8 || diskperf_tmpData.length==9)){
				if(diskperf_tmpData[0].trim().equalsIgnoreCase("Average")){
					flag = 1;
					diskperfhash.put("%busy", diskperf_tmpData[3].trim());
					diskperfhash.put("avque", diskperf_tmpData[4].trim());
					diskperfhash.put("r+w/s", diskperf_tmpData[5].trim());
					diskperfhash.put("Kbs/s", diskperf_tmpData[6].trim());
					diskperfhash.put("avwait", diskperf_tmpData[7].trim());
					diskperfhash.put("avserv", diskperf_tmpData[8].trim());
					diskperfhash.put("disklebel", diskperf_tmpData[1].trim());
					alldiskperf.add(diskperfhash);
				}else if(flag == 1){
					diskperfhash.put("%busy", diskperf_tmpData[2].trim());
					diskperfhash.put("avque", diskperf_tmpData[3].trim());
					diskperfhash.put("r+w/s", diskperf_tmpData[4].trim());
					diskperfhash.put("Kbs/s", diskperf_tmpData[5].trim());
					diskperfhash.put("avwait", diskperf_tmpData[6].trim());
					diskperfhash.put("avserv", diskperf_tmpData[7].trim());
					diskperfhash.put("disklebel", diskperf_tmpData[0].trim());
					alldiskperf.add(diskperfhash);
				}
				
				diskperfhash = new Hashtable();
			}				
		}
		//----------------����netperf����--���������---------------------        	
		String netperfContent = "";
		tmpPt = Pattern.compile("(cmdbegin:netperf)(.*)(cmdbegin:netallperf)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			netperfContent = mr.group(2);
			
//			System.out.println("==================(cmdbegin:netperf)(.*)(cmdbegin:netallperf)==============");
//			System.out.println(netperfContent);
//			System.out.println("==================(cmdbegin:netperf)(.*)(cmdbegin:netallperf)==============");
		} 
		String[] netperfLineArr = netperfContent.split("\n");
		String[] netperf_tmpData = null;
		List netperf = new ArrayList();
		//Hashtable<String,String> netnamehash = new Hashtable<String,String>();
		//int flag = 0;
		for(int i=0; i<netperfLineArr.length;i++){
			netperf_tmpData = netperfLineArr[i].trim().split("\\s++");
			//System.out.println("=============================����==="+netperf_tmpData.length);
			if(netperf_tmpData != null && (netperf_tmpData.length==9||netperf_tmpData.length==10)){
				if(netperf_tmpData[0].trim().indexOf("net")>=0){
					
					System.out.println("="+netperf_tmpData[0].trim());
					netperf.add(netperf_tmpData[0].trim());
				}
			}
		}

		//----------------����cpu����--���������---------------------        	
		String cpuperfContent = "";
		//String average = "";
		tmpPt = Pattern.compile("(cmdbegin:cpu)(.*)(cmdbegin:allconfig)",Pattern.DOTALL);
		mr = tmpPt.matcher(fileContent.toString());
		if(mr.find())
		{
			cpuperfContent = mr.group(2);
//			System.out.println("===============cpu=================");
//			System.out.println(cpuperfContent);
//			System.out.println("===============cpu=================");
			
			
		} 
		String[] cpuperfLineArr = cpuperfContent.split("\n");
		List cpuperflist = new ArrayList();
		Hashtable<String,String> cpuperfhash = new Hashtable<String,String>();
		for(int i=0; i<cpuperfLineArr.length;i++){
			diskperf_tmpData = cpuperfLineArr[i].trim().split("\\s++");
			if(diskperf_tmpData != null && (diskperf_tmpData.length ==5 || diskperf_tmpData.length==6 || diskperf_tmpData.length==7)){
				
				
				if(diskperf_tmpData[0].trim().equalsIgnoreCase("Average")){
						cpuperfhash.put("%usr", diskperf_tmpData[1].trim());
						cpuperfhash.put("%sys", diskperf_tmpData[2].trim());
						cpuperfhash.put("%wio", diskperf_tmpData[3].trim());
						cpuperfhash.put("%idle", diskperf_tmpData[4].trim());
						if(diskperf_tmpData.length==6||diskperf_tmpData.length==7)
						{
						cpuperfhash.put("%intr", diskperf_tmpData[5].trim());
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
				   		cpudata.setThevalue(Arith.round((100.0-Double.parseDouble(diskperf_tmpData[4].trim())),0)+"");
				   		cpuVector.addElement(cpudata);
					
				}
			}				
		}
		//��memory����д��ȥ
		//�����ڴ�������һ������
		Vector memoryVector=new Vector();
		Memorycollectdata memorydata=null;
		if(PhysicalMemCap > 0){
			//usedPhyPagesSize������ڴ�ʹ����
			//freePhysicalMemory ���������ڴ����У������ڴ���û��
			//�����ڴ�ʹ����
			//System.out.println("============���������ڴ�========================"+freePhysicalMemory);
			float PhysicalMemUtilization =(PhysicalMemCap-freePhysicalMemory)* 100/ PhysicalMemCap;
			//System.out.println("============ʹ����=2======================="+PhysicalMemUtilization);
			
			//�������ڴ��С
			memorydata=new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("Capability");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("static");
				memorydata.setUnit("M");
				memorydata.setThevalue(Float.toString(PhysicalMemCap));
				memoryVector.addElement(memorydata);
				//�Ѿ��õ������ڴ�
				memorydata=new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("UsedSize");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("static");
				memorydata.setUnit("M");
				memorydata.setThevalue(
					//Float.toString(PhysicalMemCap*(1-usedPhyPagesSize/100)));
					Float.toString(PhysicalMemCap-freePhysicalMemory));
				memoryVector.addElement(memorydata);
				//�ڴ�ʹ����
				memorydata=new Memorycollectdata();
				memorydata.setIpaddress(ipaddress);
				memorydata.setCollecttime(date);
				memorydata.setCategory("Memory");
				memorydata.setEntity("Utilization");
				memorydata.setSubentity("PhysicalMemory");
				memorydata.setRestype("dynamic");
				memorydata.setUnit("%");
				memorydata.setThevalue(Math.round(PhysicalMemUtilization)+"");
				//memorydata.setThevalue(Math.round(usedPhyPagesSize)+"");
				memoryVector.addElement(memorydata);
		}
		if(SwapMemCap > 0){
			//Swap
  			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("Capability");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("static");
  			memorydata.setUnit("M");
  			//һ��BLOCK��512byte
  			//��������ʹ�ô�С
  			memorydata.setThevalue(Math.round(SwapMemCap*4/1024)+"");
  			memoryVector.addElement(memorydata);
  			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("UsedSize");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("static");
  			memorydata.setUnit("M");
  			memorydata.setThevalue(Math.round(usedSwapMemory*4/1024)+"");
  			memoryVector.addElement(memorydata);
			//��������ʹ����
  			memorydata=new Memorycollectdata();
  			memorydata.setIpaddress(ipaddress);
  			memorydata.setCollecttime(date);
  			memorydata.setCategory("Memory");
  			memorydata.setEntity("Utilization");
  			memorydata.setSubentity("SwapMemory");
  			memorydata.setRestype("dynamic");
  			memorydata.setUnit("%");
  			memorydata.setThevalue(Math.round(usedSwapMemory*100/SwapMemCap)+"");
  			//System.out.println("ʹ�ô�С="+usedSwapMemory);
  			//System.out.println("�ܴ�С="+SwapMemCap);
  			//System.out.println("��������ʹ����  "+Math.round(usedSwapMemory*100/SwapMemCap)+"");
  			memoryVector.addElement(memorydata);
		}
		
		//��----------------����process����--���������---------------------        	
		String processContent = "";
		tmpPt = Pattern.compile("(cmdbegin:process)(.*)(cmdbegin:cpu)",Pattern.DOTALL);
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
			//System.out.println("processtmpData.length==="+processtmpData.length);
			if((processtmpData != null) && (processtmpData.length >= 10)){
				
				//SysLogger.info(processtmpData[0]+"-----------------");
				String USER=processtmpData[0];//USER
				String pid=processtmpData[1];//pid
				if("UID".equalsIgnoreCase(USER))continue;
				String cmd = processtmpData[9];
				String vbstring8 = processtmpData[6];
				String vbstring5=processtmpData[8];//cputime
				if(processtmpData.length >= 11){
					if(processtmpData[9].indexOf(":")!=-1){
						cmd = processtmpData[10];
						vbstring8 = processtmpData[6]+processtmpData[7];//STIME
						vbstring5=processtmpData[9];//cputime
					}
				}
				String vbstring2="Ӧ�ó���";
				String vbstring3="";
				String vbstring4=processtmpData[4];//memsize
				if (vbstring4 == null)vbstring4="0";
				String vbstring6=processtmpData[5];//%mem
				String vbstring7=processtmpData[5];//%CPU
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
		systemdata.setThevalue(process_LineArr.length+"");
		systemVector.addElement(systemdata);	
		
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
		tmpPt = Pattern.compile("(cmdbegin:usergroup)(.*)(cmdbegin:date)",Pattern.DOTALL);
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
			systemdata.setEntity("sysUpTime");
			systemdata.setSubentity("sysUpTime");
			systemdata.setRestype("static");
			systemdata.setUnit(" ");
			systemdata.setThevalue(uptimeContent.trim());
			systemVector.addElement(systemdata);
		}

		try{
			deleteFile(ipaddress);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (diskVector != null && diskVector.size()>0)returnHash.put("disk",diskVector);
		if (cpuVector != null && cpuVector.size()>0)returnHash.put("cpu",cpuVector);
		if (memoryVector != null && memoryVector.size()>0)returnHash.put("memory",memoryVector);
		if (processVector != null && processVector.size()>0)returnHash.put("process",processVector);	
		if (systemVector != null && systemVector.size()>0)returnHash.put("system",systemVector);	
		if (nodeconfig != null)returnHash.put("nodeconfig",nodeconfig);
		if (alldiskperf != null && alldiskperf.size()>0)returnHash.put("alldiskperf",alldiskperf);
		if (cpuperflist != null && cpuperflist.size()>0)returnHash.put("cpuperflist",cpuperflist);
		returnHash.put("collecttime",collecttime);
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
			System.out.println("###��ʼɾ���ļ���"+delFile);
			//delFile.delete();
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
		 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String filename = "E:/MyWork/Tomcat5.0/webapps/afunms/linuxserver/200.1.1.218.log";				
		File file=new File(filename);
		long lasttime = file.lastModified();
		long size = file.length();
		Date date = new Date(lasttime);
		Date date2 = new Date();
		String times = sdf.format(date);
		long btmes = (date2.getTime()-date.getTime())/1000;
		System.out.println(btmes);
		System.out.println(size/1024);
	 }
}






