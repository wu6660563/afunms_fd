package com.afunms.polling.snmp.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.Db2spaceconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Db2spaceconfig;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadDB2File;
import com.afunms.system.util.TimeGratherConfigUtil;

public class DB2DataCollector{

	private Hashtable sendeddata = ShareData.getSendeddata();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	public void collect_data(String dbid,Hashtable gatherHash) {
		DBDao dbdao = null;
		try{	
			DBVo dbmonitorlist = new DBVo(); 
			try{
				dbdao = new DBDao();
				dbmonitorlist = (DBVo)dbdao.findByID(dbid);
			}catch(Exception e){
				
			}finally{
				dbdao.close();
			}
			//δ����
			if(dbmonitorlist.getManaged() == 0)return;
			
			DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
			
			//if(!dbnode.isManaged())return;
			
			//��ʵʱ���ݽ������
			dbnode.setAlarm(false);
			dbnode.setStatus(0);
			Calendar _tempCal = Calendar.getInstance();
			Date _cc = _tempCal.getTime();
			String _time = sdf.format(_cc);
			dbnode.setLastTime(_time);
			dbnode.getAlarmMessage().clear();
			
			//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
			int result = 0;
			result = timeconfig.isBetween(dbnode.getId()+"", "db");
			if(result == 0){
				SysLogger.info("###### "+dbnode.getIpAddress()+" ���ڲɼ�ʱ�����,����######");
				return;
			}
			

			String serverip = dbmonitorlist.getIpAddress();
			String username = dbmonitorlist.getUser();
			String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
			int port = Integer.parseInt(dbmonitorlist.getPort());
			String dbnames = dbmonitorlist.getDbName();
			Date d1 = new Date();
			//�жϸ����ݿ��Ƿ���������
			String[] dbs = dbnames.split(",");
			//SysLogger.info("process db2 ====== "+serverip);
			int allFlag = 0;
			
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						//	// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".db2.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							System.out.println("�ļ�������");
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						SysLogger.info("###��ʼ����DB2:" + serverip + "�����ļ�###");
						LoadDB2File load = new LoadDB2File(filename);
						Hashtable db2Data = new Hashtable();
						try {
							db2Data = load.getDB2Init();
						} catch (Exception e) {
							e.printStackTrace();
						}
						boolean db2IsOK = false;
						if (db2Data != null && db2Data.size() > 0) {
							if (db2Data.containsKey("status")) {
								System.out.println(db2Data.get("status"));
								int status = Integer.parseInt((String) db2Data.get("status"));
								if (status == 1)
									db2IsOK = true;
								if (!db2IsOK) {
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									// createSMS("db2",dbmonitorlist);
									allFlag = 1;
								}
								if (allFlag == 1) {
									// ��һ�����ݿ��ǲ�ͨ��
									// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											// �������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "DB2(" + dbmonitorlist.getDbName() + " IP:"
														+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
												eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
														.getAlias()
														+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
														"���ڵķ��������Ӳ���");
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("DB2Ping");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												// ���Ͷ���
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("db2", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("DB2Ping");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										try {
											dbdao.createHostData(hostdata);
											// ���Ͷ���
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

											dbnode.setStatus(3);
											createSMS("db2", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

								} else {
									// �������ݿ�����ͨ��
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("DB2Ping");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									try {
										dbdao.createHostData(hostdata);
										if (sendeddata.containsKey("db2" + ":" + serverip))
											sendeddata.remove("db2" + ":" + serverip);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								Hashtable spaceHash = (Hashtable) db2Data.get("spaceInfo");
								String[] alldbs = dbnames.split(",");
								Hashtable alltype6spaceHash = new Hashtable();
								Hashtable type6spaceHash = new Hashtable();
								for (int k = 0; k < alldbs.length; k++) {
									String dbStr = alldbs[k];
									List type6space = new ArrayList();
									if (spaceHash.containsKey(dbStr)) {
										List retList = (List) spaceHash.get(dbStr);
										if (retList != null && retList.size() > 0) {
											for (int j = 0; j < retList.size(); j++) {
												Hashtable sys_hash = (Hashtable) retList.get(j);
												if (sys_hash != null && sys_hash.size() > 0) {
													// if(sys_hash.get("container_type")!=null){
													// ֻ�е�container_type=6��ʱ������ݿ�����û�����ı��С
													// if(sys_hash.get("container_type").toString().equals("6")){
													type6space.add(sys_hash);
													// �жϸ澯
													Db2spaceconfigDao db2spaceconfigManager = new Db2spaceconfigDao();
													Hashtable db2alarm = db2spaceconfigManager.getByAlarmflag(1);
													db2spaceconfigManager.close();
													if (db2alarm != null && db2alarm.size() > 0) {
														if (db2alarm.containsKey(serverip + ":" + dbStr + ":"
																+ sys_hash.get("tablespace_name").toString())) {
															// �ж�ֵ�Ƿ�Խ��
															Db2spaceconfig db2spaceconfig = (Db2spaceconfig) db2alarm
																	.get(serverip + ":" + dbStr + ":"
																			+ sys_hash.get("tablespace_name").toString());
															String usableper = (String) sys_hash.get("usableper");
															if (usableper.trim().length() == 0)
																usableper = "0";
															float usablefloatper = new Float(usableper);

															if (db2spaceconfig.getAlarmvalue() < (100 - new Float(usablefloatper)
																	.intValue())) {
																// �澯
																dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																		dbmonitorlist.getId());
																dbnode.setAlarm(true);
																dbnode.setStatus(3);
																List alarmList = dbnode.getAlarmMessage();
																if (alarmList == null)
																	alarmList = new ArrayList();
																dbnode.getAlarmMessage().add(
																		db2spaceconfig.getSpacename() + "��ռ䳬����ֵ"
																				+ db2spaceconfig.getAlarmvalue());
																createDb2SpaceSMS(dbmonitorlist, db2spaceconfig);
															}
														}
													}
												}
											}
										}
									}
									if (type6space != null && type6space.size() > 0) {
										// ��typeΪ6�ı�ռ�ӽ�����
										type6spaceHash.put(dbStr, type6space);
									}
								}
								if (type6spaceHash != null && type6spaceHash.size() > 0) {
									alltype6spaceHash.put(serverip, type6spaceHash);
									ShareData.setDb2type6spacedata(serverip, alltype6spaceHash);
								}
								ShareData.setAlldb2data(serverip, db2Data);
							}
						}
						// ////////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						Hashtable allDb2Data = new Hashtable();
						for (int k = 0; k < dbs.length; k++) {
							//SysLogger.info("begin collect db2--"+dbs[k]+" --------- "+serverip);
							String dbStr = dbs[k];
							boolean db2IsOK = false;
							try {
								db2IsOK = dbdao.getDB2IsOK(serverip, port, dbStr, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
								db2IsOK = false;
							}
							if (!db2IsOK) {
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
								//createSMS("db2",dbmonitorlist);
								allFlag = 1;
							}
							//SysLogger.info("end collect db2--"+dbs[k]+" --------- "+serverip);
						}
						if (allFlag == 1) {
							
							//��һ�����ݿ��ǲ�ͨ��
							allDb2Data.put("status", "0");
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "DB2(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("DB2Ping");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("db2", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								Calendar date = Calendar.getInstance();
								hostdata.setCollecttime(date);
								hostdata.setCategory("DB2Ping");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

									dbnode.setStatus(3);
									createSMS("db2", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						} else {
							//�������ݿ�����ͨ��
							allDb2Data.put("status", "1");
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("DB2Ping");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
								if (sendeddata.containsKey("db2" + ":" + serverip))
									sendeddata.remove("db2" + ":" + serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						// ��DB2���ݽ��вɼ�
						
						allDb2Data = dbdao.getDB2Data(serverip, port, dbnames, username, passwords,gatherHash);
						Hashtable spaceHash = new Hashtable();
						if(allDb2Data != null && allDb2Data.containsKey("spaceInfo")){
							spaceHash = (Hashtable)allDb2Data.get("spaceInfo");
						}
						if (allFlag == 1){
							allDb2Data.put("status", "0");
						}else{
							allDb2Data.put("status", "1");
						}
						IpTranslation tranfer = new IpTranslation();
						String hex = tranfer.formIpToHex(dbmonitorlist.getIpAddress());
						String sip = hex+":"+dbmonitorlist.getId();
						//����״̬��Ϣ
						dbdao.addOrUpdateDB2_nmsstatus(sip,String.valueOf(allDb2Data.get("status")));
						
						String[] alldbs = dbnames.split(",");
							
//						// ��space��Ϣ���вɼ�
//						Hashtable spaceHash = dbdao.getDB2Space(serverip, port, dbnames, username, passwords);
//						allDb2Data.put("spaceInfo", spaceHash);
//
//						// ��sys��Ϣ���вɼ�
//						Hashtable sysHash = dbdao.getDB2Sys(serverip, port, dbnames, username, passwords);
//						allDb2Data.put("sysInfo", sysHash);
//
//						// ��pool��Ϣ���вɼ�
//						Hashtable poolHash = dbdao.getDB2Pool(serverip, port, dbnames, username, passwords);
//						allDb2Data.put("poolInfo", poolHash);
//
//						// ��session��Ϣ���вɼ�
//						Hashtable sessionHash = dbdao.getDB2Session(serverip, port, dbnames, username, passwords);
//						allDb2Data.put("session", sessionHash);

//						String[] alldbs = dbnames.split(",");
//
//						// ��lock��Ϣ���вɼ�
//						Hashtable lockInfo = new Hashtable<String, List>();
//						for (String db : alldbs) {
//							List lockHash = dbdao.getDB2Lock(serverip, port, dbnames, username, passwords);
//							lockInfo.put(db, lockHash);
//						}
//						allDb2Data.put("lock", lockInfo);
						
						Hashtable alltype6spaceHash = new Hashtable();
						Hashtable type6spaceHash = new Hashtable();
						for (int k = 0; k < alldbs.length; k++) {
							String dbStr = alldbs[k];
							List type6space = new ArrayList();
							if (spaceHash.containsKey(dbStr)) {
								List retList = (List) spaceHash.get(dbStr);
								if (retList != null && retList.size() > 0) {
									for (int j = 0; j < retList.size(); j++) {
										Hashtable sys_hash = (Hashtable) retList.get(j);
										if (sys_hash != null && sys_hash.size() > 0) {
											//if(sys_hash.get("container_type")!=null){
											//ֻ�е�container_type=6��ʱ������ݿ�����û�����ı��С
											//if(sys_hash.get("container_type").toString().equals("6")){
											type6space.add(sys_hash);
											//�жϸ澯
											Db2spaceconfigDao db2spaceconfigManager = new Db2spaceconfigDao();
											Hashtable db2alarm = db2spaceconfigManager.getByAlarmflag(1);
											db2spaceconfigManager.close();
											if (db2alarm != null && db2alarm.size() > 0) {
												if (db2alarm.containsKey(serverip + ":" + dbStr + ":"
														+ sys_hash.get("tablespace_name").toString())) {
													//�ж�ֵ�Ƿ�Խ��
													Db2spaceconfig db2spaceconfig = (Db2spaceconfig) db2alarm.get(serverip + ":"
															+ dbStr + ":" + sys_hash.get("tablespace_name").toString());
													String usableper = (String) sys_hash.get("usableper");
													if (usableper.trim().length() == 0)
														usableper = "0";
													float usablefloatper = new Float(usableper);

													if (db2spaceconfig.getAlarmvalue() < (100 - new Float(usablefloatper)
															.intValue())) {
														//�澯
														dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																dbmonitorlist.getId());
														dbnode.setAlarm(true);
														dbnode.setStatus(3);
														List alarmList = dbnode.getAlarmMessage();
														if (alarmList == null)
															alarmList = new ArrayList();
														dbnode.getAlarmMessage().add(
																db2spaceconfig.getSpacename() + "��ռ䳬����ֵ"
																		+ db2spaceconfig.getAlarmvalue());
														createDb2SpaceSMS(dbmonitorlist, db2spaceconfig);
													}
												}
											}

											//}
											//}
										}
									}
								}
							}
							if (type6space != null && type6space.size() > 0) {
								//��typeΪ6�ı�ռ�ӽ�����
								type6spaceHash.put(dbStr, type6space);
							}
						}
						if (type6spaceHash != null && type6spaceHash.size() > 0) {
							alltype6spaceHash.put(serverip, type6spaceHash);
							ShareData.setDb2type6spacedata(serverip, alltype6spaceHash);
						}
						ShareData.setAlldb2data(serverip, allDb2Data);
						Hashtable monitorDB2Data = new Hashtable();
						monitorDB2Data.put("allDb2Data", allDb2Data);
						monitorDB2Data.put("alltype6spaceHash", alltype6spaceHash);
						monitorDB2Data.put("ip", serverip);
						if("1".equals(allDb2Data.get("status"))){
							//ɾ��֮ǰ�ɼ���DB2������Ϣ
							String[] tableNames = {"nms_db2tablespace","nms_db2common","nms_db2conn","nms_db2variable",
									"nms_db2spaceinfo","nms_db2log","nms_db2write","nms_db2pool","nms_db2lock",
									"nms_db2read","nms_db2session","nms_db2cach"};
							dbdao.clearTablesData(tableNames, sip);
							//����ɼ���DB2������Ϣ
							dbdao.addDB2_nmsinfo(sip, monitorDB2Data, alldbs);
						}
					}
					

					SysLogger.info("#### �����ɼ� db2 " + serverip+" ####");
				//}
			//}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbdao!=null)
				dbdao.close();
			SysLogger.info("#### DB2�ɼ������������ ####");
		}
	}

	public void createFileNotExistSMS(String ipaddress) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			Host host = (Host) PollingEngine.getInstance().getNodeByIP(ipaddress);
			if (host == null)
				return;

			if (!sendeddata.containsKey(ipaddress + ":file:" + host.getId())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("3");
				smscontent.setObjid(host.getId() + "");
				smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("host");
				smscontent.setSubentity("ftp");
				smscontent.setIp(host.getIpAddress());// ���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(ipaddress + ":file" + host.getId(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":file:" + host.getId());
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

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					// ����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("3");
					smscontent.setObjid(host.getId() + "");
					smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("host");
					smscontent.setSubentity("ftp");
					smscontent.setIp(host.getIpAddress());// ���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					smsmanager.sendURLSmscontent(smscontent);
					// �޸��Ѿ����͵Ķ��ż�¼
					sendeddata.put(ipaddress + ":file:" + host.getId(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createSMS(String db, DBVo dbmonitorlist) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			if (!sendeddata.containsKey(db + ":" + dbmonitorlist.getIpAddress())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
						+ "�����ݿ����ֹͣ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("ping");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				// ���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(db + ":" + dbmonitorlist.getIpAddress());
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

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					// ����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
							+ "�����ݿ����ֹͣ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("ping");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					// smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+"
					// IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
					// ���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					// �޸��Ѿ����͵Ķ��ż�¼
					sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
				} else {
					/*-------modify  zhao--------------------------*/
					String eventdesc = db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
							+ "�����ݿ����ֹͣ";
					SmscontentDao eventdao = new SmscontentDao();
					eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist.getAlias() + "("
							+ dbmonitorlist.getIpAddress() + ")", eventdesc, 2, "db", "ping", "���ڵķ��������Ӳ���");
					/*----------------------------------------------------*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void createDb2SpaceSMS(DBVo dbmonitorlist, Db2spaceconfig db2spaceconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		//EventListDao eventmanager=new EventListDao();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = dbmonitorlist.getIpAddress() + "��" + db2spaceconfig.getDbname() + "��"
				+ db2spaceconfig.getSpacename() + "�ı�ռ䳬��" + db2spaceconfig.getAlarmvalue() + "%��ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("db2space");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + db2spaceconfig.getDbname() + ":"
						+ db2spaceconfig.getSpacename());
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

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("db2space");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
//					SmscontentDao content=new SmscontentDao();
//					content.createEventWithReasion("poll",dbmonitorlist.getId()+"",dbmonitorlist.getAlias()+ "(" + dbmonitorlist.getIpAddress() + ")", errorcontent,
//							2, "db", "db2space", "��ռ䳬����ֵ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
