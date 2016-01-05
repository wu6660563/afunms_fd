/*
 * Created on 2005-4-7
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.snmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.dao.ProcsDao;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.config.model.Procs;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.ProcessNetData;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Usercollectdata;
import com.afunms.polling.om.UtilHdx;
import com.afunms.temp.model.ServiceNodeTemp;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment eration&gt;Code and Comments
 */
public class LoadWindowsWMIFile {
	/**
	 * @param hostname
	 */
	private String ipaddress;

	//private ProcsDao procsManager = new ProcsDao();
	private Hashtable sendeddata = ShareData.getProcsendeddata();

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public LoadWindowsWMIFile(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Hashtable getTelnetMonitorDetail() {
		Host node = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
		if(node == null)return null;
		if(!node.isManaged())return null;
		//�õ�ǰһ�βɼ�������
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(ipaddress);
		if(ipAllData == null)ipAllData = new Hashtable();
		Hashtable returnHash = new Hashtable();
		StringBuffer fileContent = new StringBuffer();
		Vector cpuVector = new Vector();
		Vector memoryVector=new Vector();
		Vector systemVector = new Vector();
		Vector userVector = new Vector();
		Vector diskVector = new Vector();
		Vector processVector = new Vector();
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();
		
		Hashtable hostinfohash = new Hashtable();
		Hashtable diskconfighash = new Hashtable();
		Hashtable networkstatushash = new Hashtable();
		Hashtable alldiskperformancehash = new Hashtable();
		
		//����߼�������������
		List logicdiskperformancelist = new ArrayList();
		Hashtable oldalldiskperformancehash = new Hashtable();
		Hashtable newalldiskperformancehash = new Hashtable();
		List cpuconfiglist = new ArrayList();
		List physicalDisklist = new ArrayList();
		List networkconfiglist = new ArrayList();
		List networkstatuslist = new ArrayList();
		Hashtable memoryhash = new Hashtable();
		List diskperformancelist = new ArrayList();
		Hashtable pefmemoryhash = new Hashtable();
		Hashtable pefcpuhash = new Hashtable();
		List servicelist = new ArrayList();
		List userlist = new ArrayList();
		List iflist = new ArrayList();
		List diskpef=new ArrayList();//���̶�ȡ����

		CPUcollectdata cpudata = null;
		Systemcollectdata systemdata = null;
		Usercollectdata userdata = null;
		Processcollectdata processdata = null;
		
		FileInputStream fi = null;
		FileOutputStream fo = null;
		Document doc;
		Element root = null;	
		List elements = null;
		String cpupercValue = "0";

		Calendar date = Calendar.getInstance();
		
		try {
			
			String filename = ResourceCenter.getInstance().getSysPath()+ "linuxserver/" + ipaddress + ".xml";
			File file=new File(filename);
			if(!file.exists()){
				//�ļ�������
				return null;
			}
			file = null;
			fi = new FileInputStream(filename);
			SAXBuilder sb = new SAXBuilder();
			doc = sb.build(fi);
			root = doc.getRootElement();
			fi.close();
			elements = root.getChildren();
			
			if(elements == null) elements = new ArrayList();
			
			//��ȡ����������Ϣ
			for(int j=0;j<elements.size();j++){
				//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+((Element)elements.get(j)).getName());
				
				
				/**
				 * ����������Ϣ
				 * 
				 * 
				 */
				
				if("Networkconfigstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//����ӿ���Ϣ��ʼ
					//System.out.println("===============Networkconfigstart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
					//System.out.println(subelements.size());
					
					
				  	if(subelements != null && subelements.size()>0){
				  		Hashtable rValue = new Hashtable();
				  		for(int k=0;k<subelements.size();k++){
				  			
				  			//System.out.println("==========1=====================");
				  			if("Networkconfiginfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//����������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					
					  					rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
					  			networkconfiglist.add(rValue);
				  			}
				  		}
				  	}
				  	
				}
				
				
				
				/**
				 * ����������Ϣ
				 * 
				 * 
				 * 
				 */
				
				
				if("hostinfostart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//����������Ϣ��ʼ
					
					//System.out.println("===============hostinfostart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			//SysLogger.info(((Element)(subelements.get(k))).getName()+"==========");
				  			if("hostinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//����������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
				  				List cpuList = new ArrayList();
					  			if(endelements != null && endelements.size()>0){
					  				for(int m=0;m<endelements.size();m++){
					  					if("TotalPhysicalMemory".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						List rvalue = SysUtil.checkSize(((Element)(endelements.get(m))).getValue());
					  						hostinfohash.put(((Element)(endelements.get(m))).getName(), (String)rvalue.get(0)+(String)rvalue.get(1)+"");
					  					}else if ("CPUname".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						cpuList.add(((Element)(endelements.get(m))).getValue());
					  					}else
					  						hostinfohash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  						//SysLogger.info(SnmpMibConstants.HostInfoName.get(((Element)(endelements.get(m))).getName())+"=========="+((Element)(endelements.get(m))).getValue());
					  					
					  				}
					  			}
					  			if(cpuList != null && cpuList.size()>0){
					  				hostinfohash.put("CPUname", cpuList);
					  			}
				  			}
				  		}
				  	}
				} 
				
				
				/**
				 * 
				 * ������Ϣ
				 * 
				 * 
				 * 
				 */
				
				if("serviceinfostar".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//ϵͳ�����ķ�����Ϣ��ʼ
					
					//System.out.println("===============serviceinfostar============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			Hashtable subValue = new Hashtable();
				  			Hashtable rValue = new Hashtable();
				  			if("serviceinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//ϵͳ�����ķ�����Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					if("StartMode".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.ServiceStartModeConfig.get(((Element)(endelements.get(m))).getValue()));
					  					}else if("State".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.ServiceStateConfig.get(((Element)(endelements.get(m))).getValue()));
					  					}else if("ServiceType".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.ServiceTypeConfig.get(((Element)(endelements.get(m))).getValue()));
					  					}else
					  						rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
					  			servicelist.add(rValue);
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * ����ϵͳ�û���Ϣ
				 * �û���
				 * 
				 */
				
				if("UserAccountinfostart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//ϵͳ�û���Ϣ��ʼ
					
					//System.out.println("===============UserAccountinfostart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			Hashtable rValue = new Hashtable();
				  			if("UserAccountinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//ϵͳ�û���Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					if("LocalAccount".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.UserLocalAccountConfig.get(((Element)(endelements.get(m))).getValue()));
					  					}else if("Status".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.UserStatusConfig.get(((Element)(endelements.get(m))).getValue()));
					  					}else
					  						rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
					  			userlist.add(rValue);
				  			}
				  		}
				  	}
				}
				
				
				
				/**
				 * ������Ϣ
				 * 
				 * 
				 * 
				 */
				
				
				
				if("processinfostart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//������Ϣ��ʼ
					
					//System.out.println("===============processinfostart============");
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
					
					
					int totalPhySize = 0;
					if (memoryhash != null && memoryhash.size() > 0){
						String TotalVisibleMemorySize = (String)memoryhash.get("TotalVisibleMemorySize");
						totalPhySize = Integer.parseInt(Math.round(Float.parseFloat(TotalVisibleMemorySize.substring(0,TotalVisibleMemorySize.length()-1)))+"");
					}
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			Hashtable subValue = new Hashtable();
				  			Hashtable rValue = new Hashtable();
				  			//SysLogger.info(((Element)(subelements.get(k))).getName()+"==========");
				  			if("processinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  				//����ID
					  				String pid = (String)rValue.get("ProcessId");
					  				//�߳�����
					  				String threadCount = (String)rValue.get("ThreadCount");
					  				String percmem = "0";
					  				
					  				String memsize = Math.round(Float.parseFloat((String)rValue.get("WorkingSetSize"))* 1.0f / 1024)+"";
					  				if(totalPhySize > 0){
					  					percmem = Math.round(Float.parseFloat(memsize)/(totalPhySize*1024))+"";
					  				}
					  				
					  				
					  				
					  				//��������
					  				String name = (String)rValue.get("Name");
					  				//cputime
					  				String cputime = Math.round((Float.parseFloat((String)rValue.get("KernelModeTime"))+Float.parseFloat((String)rValue.get("UserModeTime")))/10000000)+"";
					  				String processstatus = "��������";
					  				
					  				String processtype="";
					  				
					  				//�������
					  				String handleCount = (String)rValue.get("HandleCount");
//					  				String vbstring0 = pro[0];// pid
//									String vbstring1 = pro[1];// command
//									String vbstring2 = "Ӧ�ó���";
//									String vbstring3 = "��������";
//									String vbstring4 = "";// memsize
//									String vbstring5 = pro[2];// cputime
//									String vbstring6 = pro[3];// %mem

									//�����ڴ�ʹ����
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("MemoryUtilization");
									processdata.setSubentity(pid);
									processdata.setRestype("dynamic");
									processdata.setUnit("%");
									processdata.setThevalue(percmem);
									processVector.addElement(processdata);

									//�����ڴ��С
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("Memory");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit("K");
									processdata.setThevalue(memsize);
									processVector.addElement(processdata);
									
									//��������
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("Type");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit(" ");
									processdata.setThevalue(processtype);
									processVector.addElement(processdata);
									
									//����״̬
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("Status");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit(" ");
									processdata.setThevalue(processstatus);
									processVector.addElement(processdata);

									//��������
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("Name");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit(" ");
									processdata.setThevalue(name);
									processVector.addElement(processdata);

									//����ռ�õ�CPUʱ��
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("CpuTime");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit("��");
									processdata.setThevalue(cputime);
									processVector.addElement(processdata);
									
									//�߳�����
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("ThreadCount");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit("");
									processdata.setThevalue(threadCount);
									processVector.addElement(processdata);
									
									//�������
									processdata = new Processcollectdata();
									processdata.setIpaddress(ipaddress);
									processdata.setCollecttime(date);
									processdata.setCategory("Process");
									processdata.setEntity("HandleCount");
									processdata.setSubentity(pid);
									processdata.setRestype("static");
									processdata.setUnit("");
									processdata.setThevalue(handleCount);
									processVector.addElement(processdata);
									// �ж��Ƿ�����Ҫ���ӵĽ��̣���ȡ�õ��б���������ӽ��̣����Vector��ȥ��
									if (procsV != null && procsV.size() > 0) {
										if (procsV.contains(name)) {
											// procshash.remove(vbstring1);
											procsV.remove(name);
											// �ж��Ѿ����͵Ľ��̶����б����Ƿ��иý���,����,����ѷ����б���ȥ���ö�����Ϣ
											if (sendeddata.containsKey(ipaddress + ":"
													+ name)) {
												sendeddata.remove(ipaddress + ":" + name);
											}
											// �жϽ��̶�ʧ�б����Ƿ��иý���,����,��Ӹ��б���ȥ������Ϣ
											Hashtable iplostprocdata = (Hashtable) ShareData
													.getLostprocdata(ipaddress);
											if (iplostprocdata == null)
												iplostprocdata = new Hashtable();
											if (iplostprocdata.containsKey(name)) {
												iplostprocdata.remove(name);
												ShareData.setLostprocdata(ipaddress,
														iplostprocdata);
											}

										}
									}
					  				
					  			}
					  			//�ж�ProcsV�ﻹ��û����Ҫ���ӵĽ��̣����У���˵����ǰû�������ý��̣������������������ý��̣�ͬʱд���¼�
								Vector eventtmpV = new Vector();
								if (procsV != null && procsV.size() > 0) {
									for (int i = 0; i < procsV.size(); i++) {
										Procs procs = (Procs) procshash.get((String) procsV
												.get(i));
										Host host = (Host) PollingEngine.getInstance()
												.getNodeByIP(ipaddress);
										try {
											Hashtable iplostprocdata = (Hashtable) ShareData
													.getLostprocdata(ipaddress);
											if (iplostprocdata == null)
												iplostprocdata = new Hashtable();
											iplostprocdata.put(procs.getProcname(), procs);
											ShareData.setLostprocdata(ipaddress, iplostprocdata);
											EventList eventlist = new EventList();
											eventlist.setEventtype("poll");
											eventlist.setEventlocation(host.getSysLocation());
											eventlist.setContent(procs.getProcname() + "���̶�ʧ");
											eventlist.setLevel1(1);
											eventlist.setManagesign(0);
											eventlist.setBak("");
											eventlist.setRecordtime(Calendar.getInstance());
											eventlist.setReportman("ϵͳ��ѯ");
											String bids = ","+host.getBid();
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
								}
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * ��������
				 * 
				 * 
				 * 
				 */
				
				
				if("diskconfigstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//������Ϣ��ʼ
					//System.out.println("===============diskconfigstart============");
					
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			Hashtable rValue = new Hashtable();
				  			if("diskconfiginfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					if("Size".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						List rvalue = SysUtil.checkSize(((Element)(endelements.get(m))).getValue());
					  						rValue.put(((Element)(endelements.get(m))).getName(), (String)rvalue.get(0)+(String)rvalue.get(1)+"");
					  					}else{
					  						rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  					}
					  				}
					  			}
					  			physicalDisklist.add(rValue);
				  			}
				  		}
				  		
				  	}
				}
				
				/**
				 *cpu ������Ϣ
				 * λ�������������С��cpuʹ���ʡ�cpu����Ƶ
				 * ��Ҫ����cpu�� ����
				 * ͳ���м���cpu��ÿ��cpu��ʹ�����Ƕ��٣���ƽ��ֵ
				 */
				
				
				if("cpuconfigstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//CPU������Ϣ��ʼ
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			Hashtable subValue = new Hashtable();
				  			Hashtable rValue = new Hashtable();
				  			if("cpuconfiginfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//CPU������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					//SysLogger.info(SnmpMibConstants.CPUConfigName.get(((Element)(endelements.get(m))).getName())+"=========="+((Element)(endelements.get(m))).getValue());
					  					rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
					  			cpuconfiglist.add(rValue);
				  			}
				  		}
				  	}
				}
				
				
				
				/**
				 * ϵͳ�ڴ�����
				 * 
				 * 
				 */
				
				if("PerfOSMemorystart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//�ڴ�������Ϣ��ʼ
					
					//System.out.println("===============PerfOSMemorystart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			if("OSMemorystartinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//�ڴ�������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				for(int m=0;m<endelements.size();m++){
					  					if("AvailableBytes".equalsIgnoreCase(((Element)(endelements.get(m))).getName())
					  							|| "CommittedBytes".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						float allblocksize = Float.parseFloat(((Element)(endelements.get(m))).getValue());
											float allsize = allblocksize * 1.0f / 1024;
											pefmemoryhash.put(((Element)(endelements.get(m))).getName(), Math.round(allsize)+"");
					  					}else
					  						pefmemoryhash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
				  			}
				  		}
				  	}
				}
				
				
				
				
				
				
				
				if("OSSystemstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//CPU������Ϣ��ʼ
					
					//System.out.println("===============OSSystemstart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			if("OSSysteminfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//CPU������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				for(int m=0;m<endelements.size();m++){
					  						pefcpuhash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  			}
				  			}
				  		}
				  	}
				}
				
				
				
				/**
				 * ����������Ϣ
				 * �������ݰ������͵����ݰ������յ��ֽڡ����͵��ֽڡ������������͵Ĵ����
				 * 
				 */
				
				
				if("NetworkInterfacestart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//����ӿ�������Ϣ��ʼ
					
					
					List oldiflist = new ArrayList();
					if(ipAllData != null){
						oldiflist = (List)ipAllData.get("iflist");
					}
					
					
					
					
					//System.out.println("===============NetworkInterfacestart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			
				  			Hashtable ifhash = new Hashtable();
				  			Hashtable oldifhash = new Hashtable();//����ȡ���ϼ�¼
							if(oldiflist != null && oldiflist.size()>0){
								oldifhash = (Hashtable)oldiflist.get(k);
							}
							
				  			
				  			Hashtable rValue = new Hashtable();
				  			if("NetworkInterfaceinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//����ӿ�������Ϣ
				  				Interfacecollectdata interfacedata = null;
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					//System.out.println(SnmpMibConstants.NetworkInterfaceinfoName.get(((Element)(endelements.get(m))).getName())+"=========="+((Element)(endelements.get(m))).getValue());
					  					rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				
					  				}
					  				
					  				
					  				
					  				if(rValue != null && rValue.size()>0){
					  					//�˿�����
					  					interfacedata=new Interfacecollectdata();
										interfacedata.setIpaddress(ipaddress);
										interfacedata.setCollecttime(date);
										interfacedata.setCategory("Interface");
										interfacedata.setEntity("index");
										interfacedata.setSubentity(k+"");
										//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
										interfacedata.setRestype("static");
										interfacedata.setUnit("");
										interfacedata.setThevalue(k+"");
										interfacedata.setChname("�˿�����");
										interfaceVector.addElement(interfacedata);
					  					//�˿�����
					  					interfacedata=new Interfacecollectdata();
										interfacedata.setIpaddress(ipaddress);
										interfacedata.setCollecttime(date);
										interfacedata.setCategory("Interface");
										interfacedata.setEntity("ifDescr");
										interfacedata.setSubentity(k+"");
										//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
										interfacedata.setRestype("static");
										interfacedata.setUnit("");
										interfacedata.setThevalue((String)rValue.get("Name"));
										interfacedata.setChname("�˿�����2");
										interfaceVector.addElement(interfacedata);
										//�˿ڴ���
										interfacedata=new Interfacecollectdata();
										interfacedata.setIpaddress(ipaddress);
										interfacedata.setCollecttime(date);
										interfacedata.setCategory("Interface");
										interfacedata.setEntity("ifSpeed");
										interfacedata.setSubentity(k+"");
										interfacedata.setRestype("static");
										interfacedata.setUnit("");
										interfacedata.setThevalue((String)rValue.get("CurrentBandwidth"));
										interfacedata.setChname("ÿ���ֽ���");
										interfaceVector.addElement(interfacedata);
										//��ǰ״̬
//										interfacedata=new Interfacecollectdata();
//										interfacedata.setIpaddress(ipaddress);
//										interfacedata.setCollecttime(date);
//										interfacedata.setCategory("Interface");
//										interfacedata.setEntity("ifOperStatus");
//										interfacedata.setSubentity(k+"");
//										interfacedata.setRestype("static");
//										interfacedata.setUnit("");
//										interfacedata.setThevalue("up");
//										interfacedata.setChname("��ǰ״̬");
//										interfaceVector.addElement(interfacedata);
										//��ǰ״̬
										interfacedata=new Interfacecollectdata();
										interfacedata.setIpaddress(ipaddress);
										interfacedata.setCollecttime(date);
										interfacedata.setCategory("Interface");
										interfacedata.setEntity("ifOperStatus");
										interfacedata.setSubentity(k+"");
										interfacedata.setRestype("static");
										interfacedata.setUnit("");
										//
										interfacedata.setThevalue(1+"");
										interfacedata.setChname("��ǰ״̬");
										interfaceVector.addElement(interfacedata);
										
											
										String outBytes ="0";
										String inBytes ="0";
										String oldOutBytes = "0";
										String oldInBytes = "0";
										
										
										String endOutBytes = "0";
										String endInBytes = "0";
										
										
										outBytes=(String)rValue.get("BytesSentPersec");//��ǰ���͵��ֽ���
										inBytes=(String)rValue.get("BytesReceivedPersec");//��ǰ���ܵ��ֽ���
										//������������
										
										if(oldifhash != null && oldifhash.size()>0){
											//���͵������ֽ���
											if(oldifhash.containsKey("outBytes")){
												oldOutBytes = (String)oldifhash.get("outBytes");
												try{
													endOutBytes = (Long.parseLong(outBytes)-Long.parseLong(oldOutBytes))+"";
												}catch(Exception e){
													e.printStackTrace();
											}
											
											}
											//���ܵ������ֽ���
											if(oldifhash.containsKey("inBytes")){
												oldInBytes = (String)oldifhash.get("inBytes");
												try{
													endInBytes = (Long.parseLong(inBytes)-Long.parseLong(oldInBytes))+"";
												}catch(Exception e){
													e.printStackTrace();
											}
											
											}
											
											
										}
						  				
										ifhash.put("inBytes", inBytes);
										ifhash.put("outBytes", outBytes);
										
										
										 iflist.add(ifhash);
										
										
										//�˿��������
										UtilHdx utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										String chnameBand="";
										utilhdx.setEntity("InBandwidthUtilHdx");
										utilhdx.setThevalue(Long.toString(Long.parseLong((String)endInBytes)*8/1024/300));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("Kb/��");	
										utilhdx.setChname(k+"�˿����"+"����");
										utilhdxVector.addElement(utilhdx);
										//�˿ڳ�������
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setEntity("OutBandwidthUtilHdx");
										utilhdx.setThevalue(Long.toString(Long.parseLong((String)endOutBytes)*8/1024/300));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("Kb/��");	
										utilhdx.setChname(k+"�˿ڳ���"+"����");
										utilhdxVector.addElement(utilhdx);
										//���������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("��վ�����������ݰ�");
										utilhdx.setEntity("ifInDiscards");
										utilhdx.setThevalue((String)rValue.get("PacketsReceivedDiscarded"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										//��վ�������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("��վ�������ݰ�");
										utilhdx.setEntity("ifInErrors");
										utilhdx.setThevalue((String)rValue.get("PacketsReceivedErrors"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										//��ڷǵ��������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("�ǵ��������ݰ�");
										utilhdx.setEntity("ifInNUcastPkts");
										utilhdx.setThevalue((String)rValue.get("PacketsReceivedNonUnicastPersec"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										//��ڵ��������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("���������ݰ�");
										utilhdx.setEntity("ifInUcastPkts");
										utilhdx.setThevalue((String)rValue.get("PacketsReceivedUnicastPersec"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										//���ڷǵ��������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("�ǵ��������ݰ�");
										utilhdx.setEntity("ifOutNUcastPkts");
										utilhdx.setThevalue((String)rValue.get("PacketsSentNonUnicastPersec"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										//���ڵ��������ݰ�
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(ipaddress);
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										utilhdx.setChname("���������ݰ�");
										utilhdx.setEntity("ifOutUcastPkts");
										utilhdx.setThevalue((String)rValue.get("PacketsSentUnicastPersec"));
										utilhdx.setSubentity(k+"");
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit("��");
										utilhdxVector.addElement(utilhdx);
										
					  				}
					  			}
					  			//subValue.put(((Element)(subelements.get(k))).getName(), rValue);
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * 
				 * ����״����Ϣ
				 * �������ơ�mac��ַ������״̬
				 * 
				 */
				
				
				if("NetworkStatusstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//������Ϣ��ʼ
					
					//System.out.println("===============NetworkStatusstart============");
					
					List subelements = ((Element)(elements.get(j))).getChildren();
					
					
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			
				  			//System.out.println("--------NetworkStatusinfo-----1--------------------");
				  			if("NetworkStatusinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//������Ϣ
				  				//System.out.println("--------NetworkStatusinfo-----2--------------------");
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  			//	System.out.println("--------NetworkStatusinfo-----3--------------------");
					  				Hashtable rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					if("NetConnectionStatus".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  					//	System.out.println("--------NetworkStatusinfo-----4--------------------"+((Element)(endelements.get(m))).getName());
					  						
					  						
					  						//System.out.println(((Element)(endelements.get(m))).getName()+"==="+((Element)(endelements.get(m))).getValue());
					  						
					  						//rValue.put(((Element)(endelements.get(m))).getName(), SnmpMibConstants.NetworkStatusConfig.get(((Element)(endelements.get(m))).getValue()));
					  						rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  					
					  					}else{
					  						
					  						
					  						rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  					}
					  				}
					  				networkstatuslist.add(rValue);
					  			}
				  			}
				  		}
				  	}
				}
				
				
				
				/**
				 * 
				 * �ڴ���Ϣ
				 * 
				 */
				
				
				if("SystemMemorystart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//�ڴ���Ϣ��ʼ
					
					//System.out.println("===============SystemMemorystart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			if("SystemMemoryinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//�ڴ���Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				Hashtable rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					//��M��λ��ʾ
					  					List rvalue = SysUtil.checkSize(Float.parseFloat(((Element)(endelements.get(m))).getValue())*1024+"");
					  					memoryhash.put(((Element)(endelements.get(m))).getName(), Float.parseFloat(((Element)(endelements.get(m))).getValue())/1024+"M");
					  					//memoryhash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  				}
					  				
					  			}
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * Ӳ������
				 * 
				 * 
				 */
				
				if("PhysicalDiskstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//�ܴ���������Ϣ��ʼ
					
					
					//System.out.println("===============PhysicalDiskstart============");
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			if("PhysicalDiskinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//�ܴ���������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				Hashtable rValue = new Hashtable();
					  				for(int m=0;m<endelements.size();m++){
					  					newalldiskperformancehash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  					alldiskperformancehash.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());
					  					
					  				}
					  			}
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * 
				 * ���̷�����д����
				 * 
				 * 
				 * 
				 */
				
				if("LogicalDiskstart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//�߼�����������Ϣ��ʼ
					
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			if("LogicalDiskinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//�߼�����������Ϣ
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
				  				Hashtable rValue = new Hashtable();
					  			if(endelements != null && endelements.size()>0){
					  				for(int m=0;m<endelements.size();m++){
					  					rValue.put(((Element)(endelements.get(m))).getName(), ((Element)(endelements.get(m))).getValue());					  					
					  				}
					  			}
					  			logicdiskperformancelist.add(rValue);
				  			}
				  		}
				  	}
				}
				
				
				/**
				 * ���̷���ʹ����Ϣ
				 * ��С�����ÿռ䡢�̷���������ʽ
				 * 
				 */
				if("diskinfostart".equalsIgnoreCase(((Element)(elements.get(j))).getName())){
					//������Ϣ��ʼ
					List subelements = ((Element)(elements.get(j))).getChildren();
				  	if(subelements != null && subelements.size()>0){
				  		for(int k=0;k<subelements.size();k++){
				  			
				  			
				  			
				  			if("diskinfo".equalsIgnoreCase(((Element)(subelements.get(k))).getName())){
				  				//������Ϣ
				  				
				  				String sizeunit = "G";
				  				//String freesizeunit = "";
				  				
				  				
				  				
				  				List endelements = ((Element)(subelements.get(k))).getChildren();
					  			if(endelements != null && endelements.size()>0){
					  				String caption = "";
					  				String FreeSpace = "";
					  				String Size = "";
					  				
					  				long allsize = 0;
					  				long freesize = 0;
					  				int utilper = 0;
					  				
					  				for(int m=0;m<endelements.size();m++){
					  					
					  					
					  					if("Size".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
	                                        //�Ѵ��̿ռ��С��ȡ����
					  						long allblocksize = Long.parseLong(((Element)(endelements.get(m))).getValue());
					  						
											allsize = allblocksize  / 1024;
											
											if(sizeunit.equals("")&& allsize>=1024*1024)
											{
												sizeunit="G";
											}else if(sizeunit.equals("")&& allsize>=1024)
											 {
												sizeunit="M";
											 }else  if(sizeunit.equals("")&& allsize<1024)
											 {
												 sizeunit="K";
											 }
											
											
											if (sizeunit.equals("G")) {
												allsize = allsize / 1024/1024;
												//sizeunit = "G";
											} else if(sizeunit.equals("M")){
												//sizeunit = "M";
												allsize = allsize / 1024;
											}else if(sizeunit.equals("k"))
											 {
												//sizeunit = "K";
											 }
											
					  					}else if("FreeSpace".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						
					  						
					  						
					  						freesize =Long.parseLong(((Element)(endelements.get(m))).getValue());
											freesize = freesize / 1024;
											
											if(sizeunit.equals("")&& freesize>=1024*1024)
											{
												sizeunit="G";
											}else if(sizeunit.equals("")&& freesize>=1024)
											 {
												//System.out.println("====MMM=====");
												sizeunit="M";
											 }else  if(sizeunit.equals("")&& freesize<1024)
											 {
												 sizeunit="K";
											 }
											
											
											if(sizeunit.equals("G"))
											 {
												freesize = freesize /1024/1024;
												//freesizeunit = "G";
											 }else if (sizeunit.equals("M")) {
												freesize = freesize / 1024;
												//freesizeunit = "M";
											} else if (sizeunit.equals("K"))
											  {
												
											  }
											
											
					  					}else if("Caption".equalsIgnoreCase(((Element)(endelements.get(m))).getName())){
					  						caption = ((Element)(endelements.get(m))).getValue();
					  					}
					  				}
					  			
					  				if(allsize-freesize>0){
					  					utilper = Math.round((allsize-freesize)*100/allsize);
					  				}
					  				
					  				Diskcollectdata diskdata = null;
					  				diskdata = new Diskcollectdata();
									diskdata.setIpaddress(ipaddress);
									diskdata.setCollecttime(date);
									diskdata.setCategory("Disk");
									diskdata.setEntity("Utilization");// ���ðٷֱ�
									diskdata.setSubentity(caption+"/");
									diskdata.setRestype("static");
									diskdata.setUnit("%");
									diskdata.setThevalue(utilper+"");
									diskVector.addElement(diskdata);

									diskdata = new Diskcollectdata();
									diskdata.setIpaddress(ipaddress);
									diskdata.setCollecttime(date);
									diskdata.setCategory("Disk");
									diskdata.setEntity("AllSize");// �ܿռ�
									diskdata.setSubentity(caption+"/");
									diskdata.setRestype("static");
									diskdata.setThevalue(Math.round(allsize)+"");
									diskdata.setUnit(sizeunit);
									diskVector.addElement(diskdata);

									diskdata = new Diskcollectdata();
									diskdata.setIpaddress(ipaddress);
									diskdata.setCollecttime(date);
									diskdata.setCategory("Disk");
									diskdata.setEntity("UsedSize");// ʹ�ô�С
									diskdata.setSubentity(caption+"/");
									diskdata.setRestype("static");
									diskdata.setUnit(sizeunit);
									diskdata.setThevalue(Math.round((allsize-freesize))+"");
									diskVector.addElement(diskdata);
									
									//yangjun 
									String diskinc = "0.0";
									int pastutil = 0;
									Vector disk_v = (Vector)ipAllData.get("disk");
									if (disk_v != null && disk_v.size() > 0) {
										for (int si = 0; si < disk_v.size(); si++) {
											Diskcollectdata disk_data = (Diskcollectdata) disk_v.elementAt(si);
											if((caption+"/").equals(disk_data.getSubentity())&&"Utilization".equals(disk_data.getEntity())){
												pastutil = Integer.parseInt(disk_data.getThevalue());
											}
										}
									} else {
										pastutil = utilper;
									}
									if (pastutil == 0) {
										pastutil = utilper;
									}
									if(utilper-pastutil>0){
					  					diskinc = (utilper-pastutil)+"";
					  				}
					  				diskdata = new Diskcollectdata();
									diskdata.setIpaddress(ipaddress);
									diskdata.setCollecttime(date);
									diskdata.setCategory("Disk");
									diskdata.setEntity("UtilizationInc");// ���������ʰٷֱ�
									diskdata.setSubentity(caption+"/");
									diskdata.setRestype("dynamic");
									diskdata.setUnit("%");
									diskdata.setThevalue(diskinc);
									diskVector.addElement(diskdata);
									//
					  			}
				  			}
				  		}
				  	}
				}
				
				
				
				
				
				
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
		//--------------��ɽ����ļ�
		
		//System.out.println("====================================�����ļ����==========================================");
		//������Ϣ��
		if (hostinfohash != null && hostinfohash.size() > 0)
			returnHash.put("hostinfo", hostinfohash);
		
		//����������Ϣ
		//System.out.println("==========����===================");
		
		
		if (networkconfiglist != null && networkconfiglist.size() > 0){
			
//			for(int i=0;i<networkconfiglist.size();i++){
//				Hashtable rValue = (Hashtable)networkconfiglist.get(i);
//				String index = (String)rValue.get("Index");
//				System.out.println("---------------------����״̬��Ϣ-----2222222222222222------------------");
//				if(networkstatuslist != null && networkstatuslist.size()>0){
//					for(int j=0;j<networkstatuslist.size();j++){
//						Hashtable statusvalue = (Hashtable)networkstatuslist.get(j);
//						if(index.equalsIgnoreCase((String)statusvalue.get("Index"))){
//							//�õ���������MAC��ַ��״̬
//							rValue.put("NetConnectionStatus", (String)statusvalue.get("NetConnectionStatus"));
//							break;
//						}
//					}
//				}
//				networklist.add(rValue);
//			}
			returnHash.put("networkconfig", networkconfiglist);
		}
		
		//����״̬��Ϣ
		if(networkstatuslist != null && networkstatuslist.size()>0){
			returnHash.put("networkstatus", networkstatuslist);
		}
		
		
	     //��Ӳ��������Ϣ�ŵ��ڴ�����
		if (diskconfighash != null && diskconfighash.size() > 0)
			returnHash.put("diskconfig", diskconfighash);
		
		
		//cpu ƽ������
		if (cpuconfiglist != null && cpuconfiglist.size() > 0){
			int cpuperc = 0;
			int cpuvalue = 0;
			int cpuflag = 0;
			for(int i=0;i<cpuconfiglist.size();i++){
				Hashtable cpuhash = (Hashtable)cpuconfiglist.get(i);
				if(cpuhash.containsKey("LoadPercentage")){
					int cpu = Integer.parseInt((String)cpuhash.get("LoadPercentage"));
					cpuvalue = cpuvalue +cpu;
					cpuflag = cpuflag+1;
				}
			}
			//if(cpuflag >0 && cpuconfiglist.size()>0){
			//	cpuperc = cpuvalue/cpuflag;
			//}
			cpuperc = cpuvalue/cpuflag;
			
			
			  cpudata=new CPUcollectdata();
			  cpudata.setIpaddress(ipaddress);
			  cpudata.setCollecttime(date);
			  cpudata.setCategory("CPU");
			  cpudata.setEntity("Utilization");
			  cpudata.setSubentity("Utilization");
			  cpudata.setRestype("dynamic");
			  cpudata.setUnit("%");		
			  cpudata.setThevalue(cpuperc+"");
			  cpuVector.add(0, cpudata);
			returnHash.put("cpuconfig", cpuconfiglist);
			cpupercValue = cpuperc+"";
		}
		
		//����Ӳ���б�
		if (physicalDisklist != null && physicalDisklist.size() > 0)
			returnHash.put("physicaldisklist", physicalDisklist);
		
		//�ڴ�
		if (memoryhash != null && memoryhash.size() > 0){
			String TotalVisibleMemorySize = (String)memoryhash.get("TotalVisibleMemorySize");
			String FreePhysicalMemory = (String)memoryhash.get("FreePhysicalMemory");
			String TotalVirtualMemorySize = (String)memoryhash.get("TotalVirtualMemorySize");
			String FreeVirtualMemory = (String)memoryhash.get("FreeVirtualMemory");
			
			int totalPhySize = Integer.parseInt(Math.round(Float.parseFloat(TotalVisibleMemorySize.substring(0,TotalVisibleMemorySize.length()-1)))+"");
			int freePhySize = Integer.parseInt(Math.round(Float.parseFloat(FreePhysicalMemory.substring(0,FreePhysicalMemory.length()-1)))+"");
			int totalVirSize = Integer.parseInt(Math.round(Float.parseFloat(TotalVirtualMemorySize.substring(0,TotalVirtualMemorySize.length()-1)))+"");
			int freeVirSize = Integer.parseInt(Math.round(Float.parseFloat(FreeVirtualMemory.substring(0,FreeVirtualMemory.length()-1)))+"");
			int phyperc = 0;
			int virperc = 0;
			if(totalPhySize-freePhySize>0){
				phyperc = (totalPhySize-freePhySize)*100/totalPhySize;
			}
			if(totalVirSize-freeVirSize>0){
				virperc = (totalVirSize-freeVirSize)*100/totalVirSize;
			}
			
			
			Memorycollectdata memorydata = null;
			memorydata=new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(phyperc+"");
			memoryVector.addElement(memorydata);
			
			memorydata=new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setRestype("static");
			memorydata.setSubentity("PhysicalMemory");
			memorydata.setUnit("M");
			memorydata.setThevalue(totalPhySize+"");
			memoryVector.addElement(memorydata);
			
			memorydata=new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("UsedSize");
			memorydata.setRestype("static");
			memorydata.setSubentity("PhysicalMemory");	
			memorydata.setUnit("M");
			memorydata.setThevalue((totalPhySize-freePhySize)+"");
			memoryVector.addElement(memorydata);
			
			memorydata=new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Capability");
			memorydata.setRestype("static");
			memorydata.setSubentity("VirtualMemory");
			memorydata.setUnit("M");
			memorydata.setThevalue(totalVirSize+"");
			memoryVector.addElement(memorydata);
			
			memorydata=new Memorycollectdata();
			memorydata.setIpaddress(ipaddress);
			memorydata.setCollecttime(date);
			memorydata.setCategory("Memory");
			memorydata.setEntity("Utilization");
			memorydata.setSubentity("VirtualMemory");
			memorydata.setRestype("dynamic");
			memorydata.setUnit("%");
			memorydata.setThevalue(virperc+"");
			memoryVector.addElement(memorydata);
			
			returnHash.put("memoryconfig", memoryhash);
		}

		if (memoryVector != null && memoryVector.size() > 0)
			returnHash.put("memory", memoryVector);
		
		
		
		
		//���̷�������
		if (logicdiskperformancelist != null && logicdiskperformancelist.size() > 0){
			List returnList = new ArrayList();
			Hashtable olddisk = new Hashtable();
			//����ʷ�аѽ���ó���
			List oldlogicdiskperformancelist = (List)ipAllData.get("logicdiskperformancelist");
			//�ж��Ƿ����ʷ�л�ȡ������
			if(oldlogicdiskperformancelist != null && oldlogicdiskperformancelist.size()>0){
				
				for(int k=0;k<oldlogicdiskperformancelist.size();k++){
					Hashtable prelogicdisk = (Hashtable)oldlogicdiskperformancelist.get(k);
					//��������Ϊk �����ݴ��ڴ���ȡ����
					olddisk.put((String)prelogicdisk.get("Name"), prelogicdisk);
				}
			}
			
			for(int i=0;i<logicdiskperformancelist.size();i++){
				Hashtable newlogicdisk = (Hashtable)logicdiskperformancelist.get(i);
				double result = 0;// Ϊ�������ݵ�ʱ���
				double newCountValue = Double.parseDouble((String)newlogicdisk.get("PercentDiskTime"));
				double newCountBase = Double.parseDouble((String)newlogicdisk.get("PercentDiskTime_Base"));
				
				
				if(olddisk.containsKey((String)newlogicdisk.get("Name"))){
					// ��ȡ��Ӧ����ʷ��¼
					Hashtable prelogicdisk = (Hashtable)olddisk.get((String)newlogicdisk.get("Name"));
					double prevCountValue = Double.parseDouble((String)prelogicdisk.get("PercentDiskTime"));
					double prevCountBase  = Double.parseDouble((String)prelogicdisk.get("PercentDiskTime_Base"));
					result = ((prevCountValue - newCountValue) / 
			                (prevCountBase - newCountBase)) * 100;	
					
					
					
					//���������ʱ����ڴ��̶�ȡ�Ĳ�ֵ
					long AvgDiskQueueLength=0;
					long AvgDiskReadQueueLength=0;
					long AvgDiskWriteQueueLength=0;
					long DiskWriteBytesPersec=0;
					long AvgDisksecPerWrite=0;
					long AvgDisksecPerRead=0;
					long DiskReadBytesPersec=0;
					long PercentDiskTime_Base=0;
			      
					PercentDiskTime_Base=(Long.parseLong((String)newlogicdisk.get("PercentDiskTime_Base"))-Long.parseLong((String)prelogicdisk.get("PercentDiskTime_Base")));
					
					AvgDiskQueueLength=(Long.parseLong((String)newlogicdisk.get("AvgDiskQueueLength"))-Long.parseLong((String)prelogicdisk.get("AvgDiskQueueLength")));
					
					AvgDiskReadQueueLength=(Long.parseLong((String)newlogicdisk.get("AvgDiskReadQueueLength"))-Long.parseLong((String)prelogicdisk.get("AvgDiskReadQueueLength")));
					
					AvgDiskWriteQueueLength=(Long.parseLong((String)newlogicdisk.get("AvgDiskWriteQueueLength"))-Long.parseLong((String)prelogicdisk.get("AvgDiskWriteQueueLength")));
					
					DiskWriteBytesPersec=(Long.parseLong((String)newlogicdisk.get("DiskWriteBytesPersec"))-Long.parseLong((String)prelogicdisk.get("DiskWriteBytesPersec")));
					
					AvgDisksecPerWrite=(Long.parseLong((String)newlogicdisk.get("AvgDisksecPerWrite"))-Long.parseLong((String)prelogicdisk.get("AvgDisksecPerWrite")));
					
					AvgDisksecPerRead=(Long.parseLong((String)newlogicdisk.get("AvgDisksecPerRead"))-Long.parseLong((String)prelogicdisk.get("AvgDisksecPerRead")));
					
					DiskReadBytesPersec=(Long.parseLong((String)newlogicdisk.get("DiskReadBytesPersec"))-Long.parseLong((String)prelogicdisk.get("DiskReadBytesPersec")));
					
					
					
					
					//System.out.println("===============================PercentDiskTime_Base="+PercentDiskTime_Base);
					Hashtable disklist=new Hashtable ();
					
					if(PercentDiskTime_Base==0)
					{
						disklist.put("AvgDiskQueueLength", 0);
						disklist.put("AvgDiskReadQueueLength", 0);
						disklist.put("AvgDiskWriteQueueLength", 0);
						disklist.put("DiskWriteBytesPersec", 0);
						disklist.put("AvgDisksecPerWrite",0);
						disklist.put("AvgDisksecPerRead", 0);
						disklist.put("DiskReadBytesPersec", 0);
					}
					else{
					disklist.put("AvgDiskQueueLength", AvgDiskQueueLength*100/PercentDiskTime_Base );
					disklist.put("AvgDiskReadQueueLength", AvgDiskReadQueueLength*100/PercentDiskTime_Base);
					disklist.put("AvgDiskWriteQueueLength", AvgDiskWriteQueueLength*100/PercentDiskTime_Base);
					disklist.put("DiskWriteBytesPersec", DiskWriteBytesPersec/300/1024*8);
					disklist.put("AvgDisksecPerWrite", AvgDisksecPerWrite/300/1024*8);
					disklist.put("AvgDisksecPerRead", AvgDisksecPerRead/300/1024*8);
					disklist.put("DiskReadBytesPersec", DiskReadBytesPersec/300/1024*8);
					}
					//disklist.put("PercentDiskTime_Base", PercentDiskTime_Base);
					disklist.put("Name", (String)newlogicdisk.get("Name"));
					diskpef.add(disklist);
					//returnHash.put("diskperforlist", disklist);
					
				}
				
				
				newlogicdisk.put("DiskBusy", Math.round(result)+"");
				double ReadBytesPersec = Double.parseDouble((String)newlogicdisk.get("DiskReadBytesPersec"))/1024;
				double WriteBytesPersec = Double.parseDouble((String)newlogicdisk.get("DiskWriteBytesPersec"))/1024;
				
				
				Diskcollectdata diskdata = null;
  				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("Busy");// ���ðٷֱ�
				diskdata.setSubentity((String)newlogicdisk.get("Name")+"/");
				diskdata.setRestype("static");
				diskdata.setUnit("%");
				diskdata.setThevalue(Math.round(result)+"");
				diskVector.addElement(diskdata);
				
				//��KBytes/s
				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("ReadBytesPersec");//��KBytes/s
				diskdata.setSubentity((String)newlogicdisk.get("Name")+"/");
				diskdata.setRestype("static");
				diskdata.setUnit("");
				diskdata.setThevalue(Math.round(ReadBytesPersec)+"");
				diskVector.addElement(diskdata);
				//дKBytes/s
				diskdata = new Diskcollectdata();
				diskdata.setIpaddress(ipaddress);
				diskdata.setCollecttime(date);
				diskdata.setCategory("Disk");
				diskdata.setEntity("WriteBytesPersec");//дKBytes/s
				diskdata.setSubentity((String)newlogicdisk.get("Name")+"/");
				diskdata.setRestype("static");
				diskdata.setUnit("");
				diskdata.setThevalue(Math.round(WriteBytesPersec)+"");
				diskVector.addElement(diskdata);
				
				
				//returnList.add(newlogicdisk);
			}
			returnHash.put("diskperforlist", diskpef);
			returnHash.put("logicdiskperformancelist", logicdiskperformancelist);
			//returnHash.put("logicdiskperformancelist", returnList);
		}
		
		
		
		
		
		
		
		//���̷�����д����
		if (alldiskperformancehash != null && alldiskperformancehash.size() > 0){
			oldalldiskperformancehash = (Hashtable)ipAllData.get("alldiskperformancehash");
			if(oldalldiskperformancehash != null && oldalldiskperformancehash.size()>0){
				double prevCountValue = Double.parseDouble((String)oldalldiskperformancehash.get("PercentDiskTime"));
				double prevCountBase  = Double.parseDouble((String)oldalldiskperformancehash.get("PercentDiskTime_Base"));
				double newCountValue = Double.parseDouble((String)alldiskperformancehash.get("PercentDiskTime"));
				double newCountBase = Double.parseDouble((String)alldiskperformancehash.get("PercentDiskTime_Base"));
				double result = ((prevCountValue - newCountValue) / 
		                (prevCountBase - newCountBase)) * 100;
				alldiskperformancehash.put("DiskBusy", Math.round(result)+"");
			}else{
				alldiskperformancehash.put("DiskBusy", "0");
			}
			
			returnHash.put("alldiskperformance", alldiskperformancehash);
			
		}
		
		//�澯���
	    try{
	        NodeUtil nodeUtil = new NodeUtil();
	        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_HOST, "windows");
			CheckEventUtil checkutil = new CheckEventUtil();
			for(int i = 0 ; i < list.size() ; i ++){
				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
				String alarmIndicatorsnodename = alarmIndicatorsnode.getName();
				//cpu�澯
				if(("cpu").equalsIgnoreCase(alarmIndicatorsnodename)){
					checkutil.checkEvent(node, alarmIndicatorsnode, cpupercValue);
				}
				//���̸澯
				if(("diskperc").equalsIgnoreCase(alarmIndicatorsnodename)){
					if(diskVector != null && diskVector.size() > 0){
					    checkutil.checkDisk(node,diskVector,alarmIndicatorsnode);
					}
				}
				//����澯 
				if("service".equalsIgnoreCase(alarmIndicatorsnodename)){
					if(servicelist != null && servicelist.size() > 0){
						Vector serviceVector = new Vector();
						for(int j=0; j<servicelist.size(); j++){
							Object obj = servicelist.get(i);
			   	   			if(obj instanceof Hashtable){
								Hashtable serviceItemHash = (Hashtable)obj;
			   	   				ProcessNetData processNetData = new ProcessNetData();
			   	   				ServiceNodeTemp serviceNodeTemp = processNetData.getServiceNodeTempByHashtable(serviceItemHash);
			   	   				Servicecollectdata servicecollectdata = new Servicecollectdata();
			   	   				try {
			   						BeanUtils.copyProperties(servicecollectdata,serviceNodeTemp);
			   					} catch (Exception e) {
			   						e.printStackTrace();
			   					}
			   					serviceVector.add(servicecollectdata);
			   	   			}
						}
						checkutil.createHostServiceGroupEventList(nodeDTO, serviceVector, alarmIndicatorsnode);
					}
				}
				//���̸澯
				if("process".equalsIgnoreCase(alarmIndicatorsnodename)){
					if(processVector != null && processVector.size() > 0){
						checkutil.createProcessGroupEventList(nodeDTO , processVector , alarmIndicatorsnode);
					}
				}
				//�����ڴ�
				if("physicalmemory".equalsIgnoreCase(alarmIndicatorsnodename)){
					Hashtable collectHash = new Hashtable();
					collectHash.put("physicalmem", memoryVector);//�����ڴ�
					checkutil.updateData(node,collectHash,"host","windows",alarmIndicatorsnode);
				}
				//�����ڴ�澯
				if("virtualmemory".equalsIgnoreCase(alarmIndicatorsnodename)){
					Hashtable collectHash = new Hashtable();
					collectHash.put("virtalmem", memoryVector);//�����ڴ�
					checkutil.updateData(node,collectHash,"host","windows",alarmIndicatorsnode);
				}
				//�������  ��������  ����������Ϣ
				if("AllInBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsnodename) || "AllOutBandwidthUtilHdx".equalsIgnoreCase(alarmIndicatorsnodename)
						|| "utilhdx".equalsIgnoreCase(alarmIndicatorsnodename)){
					Hashtable collectHash = new Hashtable();
					//����Ԫ�����Ͳ�һ�£���ת��һ��
					Vector utilhdxVectorTemp = new Vector();
					for(int j=0; j<utilhdxVector.size(); j++){
						UtilHdx utilHdx = (UtilHdx)utilhdxVector.get(j);
						AllUtilHdx allUtilHdx = new AllUtilHdx();
						BeanUtils.copyProperties(allUtilHdx, utilHdx);
						utilhdxVectorTemp.add(allUtilHdx);
					}
					collectHash.put("allutilhdx", utilhdxVectorTemp);
					checkutil.updateData(node,collectHash,"host","windows",alarmIndicatorsnode);
				}
				//Ӳ����Ϣ�澯   �����޸澯��
				//�洢�ĸ澯      �����޸澯��storage
				//arp�ĸ澯       �����޸澯��ipmac
				//��װ�������Ϣ�����޸澯�� software
				//�ӿ���Ϣ          �����޸澯��interface
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		
		
		
		if (processVector != null && processVector.size() > 0)
			returnHash.put("process", processVector);
		if (diskVector != null && diskVector.size() > 0){
			returnHash.put("disk", diskVector);
		}
		if (cpuVector != null && cpuVector.size() > 0){
			returnHash.put("cpu", cpuVector);
		}
		if (pefmemoryhash != null && pefmemoryhash.size() > 0)
			returnHash.put("pefmemory", pefmemoryhash);
		if (pefcpuhash != null && pefcpuhash.size() > 0)
			returnHash.put("pefcpu", pefcpuhash);
		if (servicelist != null && servicelist.size() > 0)
			returnHash.put("service", servicelist);
		if (utilhdxVector != null && utilhdxVector.size()>0){
			returnHash.put("utilhdx",utilhdxVector);
		}
		if (interfaceVector != null && interfaceVector.size()>0)
			returnHash.put("interface",interfaceVector);
		
		if (userlist != null && userlist.size()>0)
			returnHash.put("user",userlist);
		if (iflist != null && iflist.size()>0)returnHash.put("iflist",iflist);
		
		return returnHash;
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

	public void deleteFile(String ipAddress) {

		try {
			File delFile = new File(ResourceCenter.getInstance().getSysPath()
					+ "/linuxserver/" + ipAddress + ".log");
			// System.out.println("ɾ�����ļ�Ϊ��"+delFile);
			// delFile.delete();
		} catch (Exception e) {
		}
	}

	public void copyFile(String ipAddress, String max) {
		try {
			String cmd = "cmd   /c   copy   "
					+ ResourceCenter.getInstance().getSysPath()
					+ "\\linuxserver\\" + ipAddress + ".log" + " "
					+ ResourceCenter.getInstance().getSysPath()
					+ "\\linuxserver_bak\\" + ipAddress + ".log";
			// System.out.println(cmd);
			// String cmd = "E:\\Program Files\\Internet
			// Explorer\\IEXPLORE.EXE";
			Process child = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			System.err.println(e);
			// e.printStackTrace();
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

}
