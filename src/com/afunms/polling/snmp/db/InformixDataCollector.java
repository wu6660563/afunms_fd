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
import com.afunms.application.dao.InformixspaceconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Informixspaceconfig;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.DaoInterface;
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
import com.afunms.polling.snmp.LoadInformixFile;
import com.afunms.system.util.TimeGratherConfigUtil;

public class InformixDataCollector {

	private Hashtable sendeddata = ShareData.getSendeddata();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	public void collect_data(String dbid,Hashtable gatherHash) {
		DBDao dbdao = null;
		try{
			dbdao = new DBDao();
			DBVo dbmonitorlist = null;
			try{
				dbmonitorlist = (DBVo)dbdao.findByID(dbid);
			}catch(Exception e){
				
			}finally{
				dbdao.close();
			}
			if(dbmonitorlist == null)return;
			if(dbmonitorlist.getManaged() == 0)return;
			Hashtable monitorValue = new Hashtable();
			DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
			
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
			String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(serverip);
			Date d1 = new Date();
			//�жϸ����ݿ��Ƿ���������
			int allFlag = 0;
			SysLogger.info("begin collect informix--" + dbnames + " --------- " + serverip);
			boolean informixIsOK = false;
			if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
				// �ű��ɼ���ʽ
				String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip+ ".informix.log";
				File file = new File(filename);
				if (!file.exists()) {
					// �ļ�������,������澯
					try {
						createFileNotExistSMS(serverip);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				SysLogger.info("###��ʼ����Informix:" + serverip + "�����ļ�###");
				LoadInformixFile load = new LoadInformixFile(filename);
				Hashtable informixData = new Hashtable();
				try {
					informixData = load.getInformixFile();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (informixData != null && informixData.size() > 0) {
					if (informixData.containsKey("status")) {
						int status = Integer.parseInt(informixData.get("status").toString());
						if (status == 1)
							informixIsOK = true;
						if (!informixIsOK) {
							dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
							dbnode.setAlarm(true);
							createSMS("informix", dbmonitorlist);
							allFlag = 1;
						} else {
							// �������ϣ���������ݲɼ�
							Hashtable returnValue = new Hashtable();
							try {
								// returnValue =
								// dbdao.getInformixDBConfig(
								// serverip, port + "", username,
								// passwords, dbnames, dbservername);
								returnValue = (Hashtable) informixData.get("informix");
							} catch (Exception e) {
								e.printStackTrace();
							}
							// �Ա�ռ�ֵ���и澯�ж�
							if (returnValue != null && returnValue.size() > 0) {
								if (returnValue.containsKey("informixspaces")) {
									List spaceList = (List) returnValue.get("informixspaces");// �ռ���Ϣ
									if (spaceList != null && spaceList.size() > 0) {
										// �滻ԭ���ı�ռ�����
										SysLogger.info("add infromix space size=====" + spaceList.size());
										ShareData.setInformixspacedata(serverip, spaceList);
										InformixspaceconfigDao informixspaceconfigdao = new InformixspaceconfigDao();
										Hashtable informixspaces = new Hashtable();
										try {
											informixspaces = informixspaceconfigdao.getByAlarmflag(1);
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											informixspaceconfigdao.close();
										}
										Vector spaces = new Vector();
										for (int k = 0; k < spaceList.size(); k++) {
											Hashtable ht = (Hashtable) spaceList.get(k);
											String tablespace = ht.get("dbspace").toString();
											if (spaces.contains(tablespace))
												continue;
											spaces.add(tablespace);
											String percent = ht.get("percent_free").toString();
											if (informixspaces.containsKey(serverip + ":" + tablespace)) {
												// ������Ҫ�澯�ı�ռ�
												Integer free = 0;
												try {
													free = new Float(percent).intValue();
												} catch (Exception e) {
													e.printStackTrace();
												}
												// ���ݱ�ռ�澯�����ж��Ƿ�澯
												Informixspaceconfig informixspaceconfig = (Informixspaceconfig) informixspaces
														.get(serverip + ":" + tablespace);
												if (informixspaceconfig.getAlarmvalue() < (100 - free)) {
													// �澯
													dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
															dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															informixspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																	+ informixspaceconfig.getAlarmvalue());
													dbnode.setStatus(3);
													try {
														createInformixSpaceSMS(dbmonitorlist, informixspaceconfig);
													} catch (Exception e) {
														SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
														e.printStackTrace();
													}
												}
											}

										}
									}
								}
							}
							monitorValue.put(dbnames, informixData);

						}
						SysLogger.info("end collect informix--" + dbnames + " --------- " + serverip);
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
										String eventdesc = "Informix(" + dbmonitorlist.getDbName() + " IP:"
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
									hostdata.setCategory("INFORMIXPing");
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
										createSMS("informix", dbmonitorlist);
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
								hostdata.setCategory("INFORMIXPing");
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
									createSMS("informix", dbmonitorlist);
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
							hostdata.setCategory("INFORMIXPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
					if (allFlag == 0) {
						monitorValue.put("runningflag", "��������");
					} else {
						monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
					}

					if (monitorValue != null && monitorValue.size() > 0) {
						ShareData.setInfomixmonitordata(serverip, monitorValue);
					}
				}
				// ///////////////////////////////////////////////////////////////
			} else {
				//JDBC�ɼ���ʽ
				try {
					informixIsOK = dbdao.getInformixIsOk(serverip, port + "", username, passwords, dbnames, dbservername);
				} catch (Exception e) {
					e.printStackTrace();
					informixIsOK = false;
				}
				if (!informixIsOK) {
					dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(true);
					//createSMS("informix", dbmonitorlist);
					allFlag = 1;
				} else {
					//�������ϣ���������ݲɼ�
					Hashtable returnValue = new Hashtable();
					try {
						returnValue = dbdao.getInformixDBConfig(dbnode.getId()+"",serverip, port + "", username, passwords, dbnames,dbservername,gatherHash);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//�Ա�ռ�ֵ���и澯�ж�
					if (returnValue != null && returnValue.size() > 0) {
						if (returnValue.containsKey("informixspaces")) {
							List spaceList = (List) returnValue.get("informixspaces");//�ռ���Ϣ
							if (spaceList != null && spaceList.size() > 0) {
								//�滻ԭ���ı�ռ�����		
								//SysLogger.info("add infromix space size====="+spaceList.size());
								ShareData.setInformixspacedata(serverip, spaceList);
								InformixspaceconfigDao informixspaceconfigdao = new InformixspaceconfigDao();
								Hashtable informixspaces = new Hashtable();
								try {
									informixspaces = informixspaceconfigdao.getByAlarmflag(1);
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									informixspaceconfigdao.close();
								}
								Vector spaces = new Vector();
								for (int k = 0; k < spaceList.size(); k++) {
									Hashtable ht = (Hashtable) spaceList.get(k);
									String tablespace = ht.get("dbspace").toString();
									if (spaces.contains(tablespace))
										continue;
									spaces.add(tablespace);
									String percent = ht.get("percent_free").toString();
									if (informixspaces.containsKey(serverip + ":" + tablespace)) {
										//������Ҫ�澯�ı�ռ�								
										Integer free = 0;
										try {
											free = new Float(percent).intValue();
										} catch (Exception e) {
											e.printStackTrace();
										}
										//���ݱ�ռ�澯�����ж��Ƿ�澯
										Informixspaceconfig informixspaceconfig = (Informixspaceconfig) informixspaces
												.get(serverip + ":" + tablespace);
										if (informixspaceconfig.getAlarmvalue() < (100 - free)) {
											//�澯
											dbnode = (DBNode) PollingEngine.getInstance()
													.getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add(informixspaceconfig.getSpacename() + "��ռ䳬����ֵ"+ informixspaceconfig.getAlarmvalue());
											dbnode.setStatus(3);
											try {
												createInformixSpaceSMS(dbmonitorlist, informixspaceconfig);
											} catch (Exception e) {
												//SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
												e.printStackTrace();
											}
										}
									}

								}
							}
						}
					}

					//���log�ɼ�(JDBC�ɼ�����������)  HONGLI ADD
					LoadInformixFile loadInformixFile = new LoadInformixFile();
					Hashtable informixOtherData = loadInformixFile.loadInformixFile(dbnode.getIpAddress());
					Hashtable baractLogDate = loadInformixFile.loadInformixBarActLogFile(dbnode.getIpAddress());
					Hashtable informixData = new Hashtable();
					informixData.put("status", "1");
					informixData.put("informix", returnValue);
					informixData.put("informixOtherData", informixOtherData);
					informixData.put("baractLogDate", baractLogDate);
					monitorValue.put(dbnames, informixData);
					//���ɼ��������ݲ��뵽���ݿ�
					this.saveInformixData(hex+":"+dbnames, informixData);
				
				}
				SysLogger.info("#### end collect informix " + dbnames + " " + serverip+" ####");
				if (allFlag == 1) {
					//��һ�����ݿ��ǲ�ͨ��
					//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
					Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
					Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
					if (ipPingData != null) {
						Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
						Calendar tempCal = (Calendar)pingdata.getCollecttime();
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
								String eventdesc = "Informix(" + dbmonitorlist.getDbName() + " IP:"+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
								eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist.getAlias()+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping","���ڵķ��������Ӳ���");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("INFORMIXPing");
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
								createSMS("informix", dbmonitorlist);
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
						hostdata.setCategory("INFORMIXPing");
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
							createSMS("informix", dbmonitorlist);
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
					hostdata.setCategory("INFORMIXPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue("100");
					try {
						dbdao.createHostData(hostdata);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				
				if (allFlag == 0) {
					monitorValue.put("runningflag", "��������");
				} else {
					monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
				}

				if (monitorValue != null && monitorValue.size() > 0) {
					ShareData.setInfomixmonitordata(serverip, monitorValue);
				}
			}
			String status = "0";
			if(allFlag == 0){
				status = "1";
			}else {
				dbdao.updateNmsValueByUniquekeyAndTablenameAndKey("nms_informixstatus", "serverip", hex+":"+dbnames, "status", status);
			}
			
			SysLogger.info(" ### end collect informix " + serverip+" ###");					
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbdao!=null)
				dbdao.close();
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
				smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"+ "�����ݿ����ֹͣ");
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
					smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"+ "�����ݿ����ֹͣ");
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
					String eventdesc = db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"+ "�����ݿ����ֹͣ";
					SmscontentDao eventdao = new SmscontentDao();
					eventdao.createEvent("poll", dbmonitorlist.getId() + "", dbmonitorlist.getAlias() + "("+ dbmonitorlist.getIpAddress() + ")", eventdesc, 2, "db", "ping");
					/*----------------------------------------------------*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void createInformixSpaceSMS(DBVo dbmonitorlist, Informixspaceconfig informixspaceconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + informixspaceconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
						+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("informixspace");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + informixspaceconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + informixspaceconfig.getSpacename());
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
					smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
							+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("informixspace");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					sendeddata.put(ipaddress + ":" + informixspaceconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
							+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
					////
					String message=dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
					+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ";
					SmscontentDao content=new SmscontentDao();
					content.createEventWithReasion("poll",dbmonitorlist.getId()+"",dbmonitorlist.getAlias()+ "(" + dbmonitorlist.getIpAddress() + ")", message,
							2, "db", "informixspace", "��ռ䳬����ֵ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarminfomanager.close();
		}
	}
	
	/**
	 * ���ɼ�����Informix���ݿ���Ϣ���浽���ݿ���
	 * @param serverip
	 * @param sqlserverdata
	 */
	public void saveInformixData(String serverip,Hashtable informixdata){
		if(informixdata == null || informixdata.size() == 0){
			return ;
		} 
		String status = String.valueOf(informixdata.get("status"));
		Hashtable retValue = (Hashtable)informixdata.get("informix");
		Hashtable informixOtherData = (Hashtable)informixdata.get("informixOtherData");
		Hashtable baractLogDate = (Hashtable)informixdata.get("baractLogDate");
		List configList = (ArrayList)retValue.get("configList");
		List informixlog = (ArrayList)retValue.get("informixlog");
		List databaseList = (ArrayList)retValue.get("databaselist");
		List sessionList = (ArrayList)retValue.get("sessionList");
		List aboutList = (ArrayList)retValue.get("aboutlist");
		List ioList = (ArrayList)retValue.get("iolist");
		List spaceList = (ArrayList)retValue.get("informixspaces");
		List lockList = (ArrayList)retValue.get("lockList");
		DBDao dbDao = null;
		try {
			dbDao = new DBDao();
			if(informixOtherData != null && !informixOtherData.isEmpty()){
				dbDao.addInformix_nmsother(serverip, informixOtherData);//����127.0.0.1informix.log�ɼ�������
			}
			if(baractLogDate != null && !baractLogDate.isEmpty()){
				dbDao.addInformix_nmsbaractlog(serverip, baractLogDate);//����bar_act.log�ɼ�������
			} 
			if(configList != null && configList.size()>0){
				dbDao.clearTableData("nms_informixconfig", serverip);
				for(int i=0;i<configList.size();i++){
					Hashtable itemHash = (Hashtable)configList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmsconfig(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(informixlog != null && informixlog.size()>0){
				dbDao.clearTableData("nms_informixlog", serverip);
				for(int i=0;i<informixlog.size();i++){
					Hashtable itemHash = (Hashtable)informixlog.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmslog(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(databaseList != null && databaseList.size()>0){
				dbDao.clearTableData("nms_informixdatabase", serverip);
				for(int i=0;i<databaseList.size();i++){
					Hashtable itemHash = (Hashtable)databaseList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmsdatabase(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(sessionList != null && sessionList.size()>0){
				dbDao.clearTableData("nms_informixsession", serverip);
				for(int i=0;i<sessionList.size();i++){
					Hashtable itemHash = (Hashtable)sessionList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmssession(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(aboutList != null && aboutList.size()>0){
				dbDao.clearTableData("nms_informixabout", serverip);
				for(int i=0;i<aboutList.size();i++){
					Hashtable itemHash = (Hashtable)aboutList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmsabout(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(ioList != null && ioList.size()>0){
				dbDao.clearTableData("nms_informixio", serverip);
				for(int i=0;i<ioList.size();i++){
					Hashtable itemHash = (Hashtable)ioList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmsio(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(spaceList != null && spaceList.size()>0){
				dbDao.clearTableData("nms_informixspace", serverip);
				for(int i=0;i<spaceList.size();i++){
					Hashtable itemHash = (Hashtable)spaceList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmsspace(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(lockList != null && lockList.size()>0){
				dbDao.clearTableData("nms_informixlock", serverip);
				for(int i=0;i<lockList.size();i++){
					Hashtable itemHash = (Hashtable)lockList.get(i);
					if(itemHash != null && itemHash.size()>0){
						try {
							dbDao.addInformix_nmslock(serverip,itemHash);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(status != null && !status.equals("")){
				dbDao.clearTableData("nms_informixstatus", serverip);
				try {
					dbDao.addInformix_nmsstatus(serverip,status);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
			if(dbDao != null){
				dbDao.close();
			}
		}
	}
}
