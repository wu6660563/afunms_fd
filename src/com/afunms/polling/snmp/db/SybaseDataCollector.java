package com.afunms.polling.snmp.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.SybspaceconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.model.Sybspaceconfig;
import com.afunms.application.model.TablesVO;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadSysbaseFile;
import com.afunms.system.util.TimeGratherConfigUtil;

public class SybaseDataCollector {
	
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
			if(dbmonitorlist.getManaged() == 0)return;
			//ȡ��sysbase�ɼ�
			SybspaceconfigDao sybspaceconfigManager = new SybspaceconfigDao();
			Hashtable sybspaceconfig = new Hashtable();
			try {
				sybspaceconfig = sybspaceconfigManager.getByAlarmflag(1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sybspaceconfigManager.close();
			}
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
//					SysLogger.info("#######################################################");
//					SysLogger.info("username: "+username+"===============passwords:"+passwords);
//					SysLogger.info("#######################################################");
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					boolean sysbaseIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".sysbase.log";
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
						SysLogger.info("###��ʼ����SysBase:" + serverip + "�����ļ�###");

						LoadSysbaseFile loadsysbase = new LoadSysbaseFile(filename);
						Hashtable allSysbaseDatas = new Hashtable();
						try {
							allSysbaseDatas = loadsysbase.getSysbaseConfig();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (allSysbaseDatas != null && allSysbaseDatas.size() > 0) {
							if (allSysbaseDatas.containsKey("status")) {
								int status = Integer.parseInt(allSysbaseDatas.get("status").toString());
								if (status == 1)
									sysbaseIsOK = true;
								if (!sysbaseIsOK) {
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
												String eventdesc = "SYBASE(" + dbmonitorlist.getDbName() + " IP:"
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
											hostdata.setCategory("SYSPing");
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
												createSMS("sybase", dbmonitorlist);
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
										hostdata.setCategory("SYSPing");
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
											createSMS("sybase", dbmonitorlist);
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
									hostdata.setCategory("SYSPing");
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
								Hashtable retValue = new Hashtable();
								if (sysbaseIsOK) {// �����ݿ��������ϣ���������ݿ����ݵĲɼ�
									SybaseVO sysbaseVO = new SybaseVO();

									try {
										// sysbaseVO =
										// dbdao.getSysbaseInfo(serverip,
										// port, username, passwords);
										sysbaseVO = (SybaseVO) allSysbaseDatas.get("sysbase");
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (sysbaseVO == null)
										sysbaseVO = new SybaseVO();
									
									retValue.put("status", "1");
									retValue.put("sysbaseVO", sysbaseVO);
									
									List allspace = sysbaseVO.getDbInfo();
									if (allspace != null && allspace.size() > 0) {
										for (int k = 0; k < allspace.size(); k++) {
											TablesVO tvo = (TablesVO) allspace.get(k);
											if (sybspaceconfig.containsKey(serverip + ":" + tvo.getDb_name())) {
												// �澯�ж�
												Sybspaceconfig sybconfig = (Sybspaceconfig) sybspaceconfig.get(serverip + ":"
														+ tvo.getDb_name());
												Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
												if (usedperc > sybconfig.getAlarmvalue()) {
													// ������ֵ�澯
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sybconfig.getSpacename() + "��ռ䳬����ֵ" + sybconfig.getAlarmvalue());
													dbnode.setStatus(3);
													createSybSpaceSMS(dbmonitorlist, sybconfig);
												}
											}
										}
									}
								}
								if(retValue!=null)
								   ShareData.setSysbasedata(serverip, retValue);
							}
						}
						// ////////////////////////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						try {
							sysbaseIsOK = dbdao.getSysbaseIsOk(serverip, username, passwords, port);
						} catch (Exception e) {
							e.printStackTrace();
							sysbaseIsOK = false;
						}
						if (!sysbaseIsOK) {
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
										String eventdesc = "SYBASE(" + dbmonitorlist.getDbName() + " IP:"
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
									hostdata.setCategory("SYSPing");
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
										createSMS("sybase", dbmonitorlist);
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
								hostdata.setCategory("SYSPing");
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
									createSMS("sybase", dbmonitorlist);
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
							hostdata.setCategory("SYSPing");
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
						Hashtable retValue = new Hashtable();
						if (sysbaseIsOK) {//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
							SybaseVO sysbaseVO = new SybaseVO();
							try {
								sysbaseVO = dbdao.getSysbaseInfo(serverip, port, username, passwords,gatherHash);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (sysbaseVO == null)
								sysbaseVO = new SybaseVO();
						
							retValue.put("sysbaseVO", sysbaseVO);
							retValue.put("status", "1");
							
							List allspace = sysbaseVO.getDbInfo();
							if (allspace != null && allspace.size() > 0) {
								for (int k = 0; k < allspace.size(); k++) {
									TablesVO tvo = (TablesVO) allspace.get(k);
									if (sybspaceconfig.containsKey(serverip + ":" + tvo.getDb_name())) {
										//�澯�ж�
										Sybspaceconfig sybconfig = (Sybspaceconfig) sybspaceconfig.get(serverip + ":"
												+ tvo.getDb_name());
										Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
										if (usedperc > sybconfig.getAlarmvalue()) {
											//������ֵ�澯
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add(
													sybconfig.getSpacename() + "��ռ䳬����ֵ" + sybconfig.getAlarmvalue());
											dbnode.setStatus(3);
											createSybSpaceSMS(dbmonitorlist, sybconfig);
										}
									}
								}
							}
						}
						if(retValue!=null){
							ShareData.setSysbasedata(serverip, retValue);
							//����sybase��Ϣ�����ݿ�
							IpTranslation tranfer = new IpTranslation();
							String hex = tranfer.formIpToHex(serverip);
							saveSybaseData(hex+":"+dbmonitorlist.getId(), retValue);
						}
					}
					// �������������ݿ⣬��Ӹ澯��Ϣ   HONGLI 
					if(sysbaseIsOK){
						SysLogger.info("#### ����������sybase���ݿ�"+serverip+"�����ָ��澯��Ϣ   HONGLI####");
						updateData(dbmonitorlist,ShareData.getSysbasedata());
					}else{
						IpTranslation tranfer = new IpTranslation();
						String hex = tranfer.formIpToHex(serverip);
						//����״̬��Ϣ
						dbdao.updateNmsValueByUniquekeyAndTablenameAndKey("nms_sybasestatus", "serverip", hex+":"+dbmonitorlist.getId(), "status", "0");
					}
					SysLogger.info("#### �����ɼ�SYBASE���ݿ�"+serverip+" ####" );
				//}
			//}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbdao!=null)
				dbdao.close();
			SysLogger.info("#### sysbasetask������� ####");
		}
	}

	/**
	 * @author HONGLI ���¸澯��Ϣ    
	 * @param vo ���ݿ�ʵ��
	 * @param collectingData ���ݿ�ʵ���еĸ���������Ϣ
	 */
	public void updateData(Object vo , Object collectingData){
//		SysLogger.info("##############updateDate  ��ʼ###########");
		DBVo sybaseServer = (DBVo)vo;		
		Hashtable datahashtable = (Hashtable)collectingData;
		
//		SysLogger.info("######HONG  datahashtable--"+datahashtable);
//		SysLogger.info("######HONG  sybaseServer.getIpAddress()--"+sybaseServer.getIpAddress());
		
		Hashtable sysbasehashtable = (Hashtable)datahashtable.get(sybaseServer.getIpAddress());//�õ��ɼ�sysbaseVO���ݿ����Ϣ
		
		SybaseVO sybaseVO = (SybaseVO)sysbasehashtable.get("sysbaseVO");
//		SysLogger.info("######HONG sybaseVO--"+sybaseVO.getProcedure_hitrate());
		
		
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(sybaseServer.getId()), AlarmConstant.TYPE_DB, "sybase");//��ȡ�ɼ�ָ���б�
//		SysLogger.info("##############HONG Sybase--list.size--"+list.size()+"###########");
		for(int i = 0 ; i < list.size() ; i ++){
			AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
//			SysLogger.info("##############HONG alarmIndicatorsNode.getEnabled--"+alarmIndicatorsNode.getEnabled()+"###########");
			if("1".equals(alarmIndicatorsNode.getEnabled())){
				String indicators = alarmIndicatorsNode.getName();
				String value = "";//value ��ָʵ�����ݿ��е�ֵ���� ������������    HONGLI
//				SysLogger.info("##############HONG sybase-indicators--"+indicators+"##########");
				if("procedure_cache".equals(indicators)){
					value = sybaseVO.getProcedure_hitrate();
//					SysLogger.info("#######HONG sybase-sybaseVO.getProcedure_hitrate()-->  "+sybaseVO.getProcedure_hitrate()+"");
				}else if("cpu_busy_rate".equals(indicators)){
					value = sybaseVO.getCpu_busy_rate();
				}else if("io_busy_rate".equals(indicators)){
					value = sybaseVO.getIo_busy_rate();
				}else if("locks_count".equals(indicators)){
					value = sybaseVO.getLocks_count();
				}else if("data_hitrate".equals(indicators)){
					value = sybaseVO.getData_hitrate();//key ��nms_alarm_indicators_node ��sybase��Ӧ��nameһ��
				}else {					
					continue;
				}
//				SysLogger.info("#######HONG sybase-indicator��value--"+indicators+"��"+value+"####");
				if(value == null)continue;
				if( AlarmConstant.DATATYPE_NUMBER.equals(alarmIndicatorsNode.getDatatype())){
					
					try {
						double value_int = Double.valueOf(value);//ʵ��ֵ  HONGLI
						double Limenvalue2 = Double.valueOf(alarmIndicatorsNode.getLimenvalue2());//��ֵ2 
						double Limenvalue1 = Double.valueOf(alarmIndicatorsNode.getLimenvalue1());//��ֵ1
						double Limenvalue0 = Double.valueOf(alarmIndicatorsNode.getLimenvalue0());//��ֵ0
						
//						SysLogger.info("#######HONG sybase-indicator��value_int--"+indicators+"��"+value_int+"####");
						
						String level = "";
						String alarmTimes = "";
						if(value_int > Limenvalue2){
							level = "3";
							alarmTimes = alarmIndicatorsNode.getTime2();
						}else if(value_int > Limenvalue1){
							level = "2";
							alarmTimes = alarmIndicatorsNode.getTime1();
						}else if(value_int > Limenvalue0){
							level = "1";
							alarmTimes = alarmIndicatorsNode.getTime0();
						}else{
							continue;
						}
						//?
						String num = (String)AlarmResourceCenter.getInstance().getAttribute(String.valueOf(alarmIndicatorsNode.getId()));
						
						if(num == null || "".equals(num)){
							num = "0";
						}
						
						int num_int = Integer.valueOf(num);
						
						
						int alarmTimes_int = Integer.valueOf(alarmTimes);
						
//						SysLogger.info("#######HONG sybase-indicators��num_int��level��alarmTimes--"+indicators+":"+num_int+"��"+level+"��"+alarmTimes+"##");
						
						if(num_int >= alarmTimes_int){//ʵ�ʸ澯���� >= �澯��ֵ   ������ʾ������Ϣ �� ���Ͷ���   HONGLI
							// �澯
							DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(sybaseServer.getId());
							dbnode.setAlarm(true);
							List alarmList = dbnode.getAlarmMessage();
							if (alarmList == null)
								alarmList = new ArrayList();
							dbnode.getAlarmMessage().add(alarmIndicatorsNode.getAlarm_info() + " ��ǰֵΪ123��" + value +  alarmIndicatorsNode.getThreshlod_unit());
							//������֮ǰ�ĸ澯����,������󼶱�
							if(Integer.valueOf(level)> dbnode.getStatus())dbnode.setStatus(Integer.valueOf(level));
//							SysLogger.info("##############updatedate--Sybase���Ͷ���############");
							//HONGLI
							createSMS(alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype(), sybaseServer.getAlias() , sybaseServer.getId() + "", alarmIndicatorsNode.getAlarm_info() + " ��ǰֵΪ��" + value +  alarmIndicatorsNode.getThreshlod_unit() , Integer.valueOf(level) , 1 , sybaseServer.getAlias() , sybaseServer.getBid(),sybaseServer.getAlias() + "(" + sybaseServer.getAlias() + ")");
						}else{
							num_int = num_int + 1;
							AlarmResourceCenter.getInstance().setAttribute(String.valueOf(alarmIndicatorsNode.getId()), String.valueOf(num_int));
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			
		}
	}
	
	/**
	 * @author HONG ���澯ָ�귢�͸澯��ʾ�͸澯����
	 * @param subtype
	 * @param subentity
	 * @param ipaddress
	 * @param objid
	 * @param content
	 * @param flag
	 * @param checkday
	 * @param sIndex
	 * @param bids
	 * @param sysLocation
	 */
	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids,String sysLocation){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	//System.out.println("�˿��¼�--------------------");
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
				
 			} else {
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				SmsDao smsDao = new SmsDao();
 				List list = new ArrayList();
 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 				try {
 					list = smsDao.findByEvent(content,startTime,endTime);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					smsDao.close();
				}
				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress+":"+sIndex);		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			Date ccc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(ccc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();	
		 			if(checkday == 1){
		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
		 				if (subvalue/(1000*60*60*24)>=1){
			 				//����һ�죬���ٷ���Ϣ
				 			Smscontent smscontent = new Smscontent();
				 			String time = sdf.format(date.getTime());
				 			smscontent.setLevel(flag+"");
				 			smscontent.setObjid(objid);
				 			smscontent.setMessage(content);
				 			smscontent.setRecordtime(time);
				 			smscontent.setSubtype(subtype);
				 			smscontent.setSubentity(subentity);
				 			smscontent.setIp(ipaddress);//���Ͷ���
				 			SmscontentDao smsmanager=new SmscontentDao();
				 			smsmanager.sendURLSmscontent(smscontent);
							//�޸��Ѿ����͵Ķ��ż�¼	
							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
				 		} else {
	                        //��ʼд�¼�
			 	            //String sysLocation = "";
			 				createEvent("poll",sysLocation,bids,content,flag,subtype,subentity,ipaddress,objid);
				 		}
		 			}
				} else {
 					Smscontent smscontent = new Smscontent();
 		 			String time = sdf.format(date.getTime());
 		 			smscontent.setLevel(flag+"");
 		 			smscontent.setObjid(objid);
 		 			smscontent.setMessage(content);
 		 			smscontent.setRecordtime(time);
 		 			smscontent.setSubtype(subtype);
 		 			smscontent.setSubentity(subentity);
 		 			smscontent.setIp(ipaddress);
 		 			//���Ͷ���
 		 			SmscontentDao smsmanager=new SmscontentDao();
 		 			smsmanager.sendURLSmscontent(smscontent);	
 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
 				}
 				
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	
	/**
	 * @author HONGLI ���ɸ澯�¼�
	 * @param eventtype
	 * @param eventlocation
	 * @param bid
	 * @param content
	 * @param level1
	 * @param subtype
	 * @param subentity
	 * @param ipaddress
	 * @param objid
	 */
	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//�����¼�
		SysLogger.info("##############HONG createEvent��ʼ�����¼�############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("ϵͳ��ѯ");
		eventlist.setBusinessid(bid);
		eventlist.setNodeid(Integer.parseInt(objid));
		eventlist.setOid(0);
		eventlist.setSubtype(subtype);
		eventlist.setSubentity(subentity);
		EventListDao eventlistdao = new EventListDao();
		try{
			eventlistdao.save(eventlist);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			eventlistdao.close();
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
					eventdao.createEvent("poll", dbmonitorlist.getId() + "", dbmonitorlist.getAlias() + "("+ dbmonitorlist.getIpAddress() + ")", eventdesc, 2, "db", "ping");
					/*----------------------------------------------------*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public static void createSybSpaceSMS(DBVo dbvo, Sybspaceconfig sybconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbvo.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		//I_Eventlist eventmanager=new EventlistManager();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = dbvo.getIpAddress() + "��" + dbvo.getDbName() + "��" + sybconfig.getSpacename() + "�ı�ռ䳬��"
				+ sybconfig.getAlarmvalue() + "%��ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + sybconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				//String time1 = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbvo.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("sybspace");
				smscontent.setIp(dbvo.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + sybconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + sybconfig.getSpacename());
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
					smscontent.setObjid(dbvo.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("sybspace");
					smscontent.setIp(dbvo.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + sybconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					/*------------------modify by zhao-------------------*/
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
					//////
					SmscontentDao content=new SmscontentDao();
					content.createEventWithReasion("poll",dbvo.getId()+"",dbvo.getAlias()+ "(" + dbvo.getIpAddress() + ")", errorcontent,
							2, "db", "sybspace", "��ռ䳬����ֵ");
					/*-------------------------------*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����sybase��������Ϣ
	 * @param serverip  ��ת����IP��ַ�����ݿ�ID��  ���
	 * @param sybaseData 
	 */
	public void saveSybaseData(String serverip,Hashtable sybaseData){
		if(sybaseData == null || sybaseData.size() == 0){
			return ;
		}
		DBDao dbDao = new DBDao();
		try {
			String status = null;//���ݿ��״̬��Ϣ
			SybaseVO sybaseVO = null;
			List dbInfo = null;//���ݿ���Ϣ
			List deviceInfo = null;//�豸��Ϣ
			List userInfo = null;//�û���Ϣ
			List serversInfo = null;//��������Ϣ
			List processInfo = null;//������ϢengineInfo
			List dbsInfo = null;//������Ϣ
			List engineInfo = null;//������Ϣ
			if(sybaseData != null && sybaseData.containsKey("sysbaseVO")){
				sybaseVO = (SybaseVO)sybaseData.get("sysbaseVO");
			}
			if(sybaseData != null && sybaseData.containsKey("status")){
				status = (String)sybaseData.get("status");
				dbDao.clearTableData("nms_sybasestatus", serverip);
				dbDao.addSybase_nmsstatus(serverip, status);
			}
			
			if(sybaseVO != null){
				//����������Ϣ
				dbDao.clearTableData("nms_sybaseperformance", serverip);
				dbDao.addSybase_nmsperformance(serverip, sybaseVO);
				dbInfo = sybaseVO.getDbInfo();
				deviceInfo = sybaseVO.getDeviceInfo();
				userInfo = sybaseVO.getUserInfo();
				serversInfo = sybaseVO.getServersInfo();
				processInfo = sybaseVO.getProcessInfo();
				dbsInfo = sybaseVO.getDbsInfo();
				engineInfo = sybaseVO.getEngineInfo();
				
				if(dbInfo != null && dbInfo.size() > 0){
					dbDao.clearTableData("nms_sybasedbinfo", serverip);
					dbDao.addSybase_nmsdbinfo(serverip, dbInfo);
				}
				if(dbsInfo != null && dbsInfo.size() > 0){
					dbDao.clearTableData("nms_sybasedbdetailinfo", serverip);
					dbDao.addSybase_nmsdbdetailinfo(serverip, dbsInfo);
				}
				if(deviceInfo != null && deviceInfo.size() > 0){
					dbDao.clearTableData("nms_sybasedeviceinfo", serverip);
					dbDao.addSybase_nmsdeviceinfo(serverip, deviceInfo);
				}
				if(processInfo != null && processInfo.size() > 0){
					dbDao.clearTableData("nms_sybaseprocessinfo", serverip);
					dbDao.addSybase_nmsprocessinfo(serverip, processInfo);
				}
				if(userInfo != null && userInfo.size() > 0){
					dbDao.clearTableData("nms_sybaseuserinfo", serverip);
					dbDao.addSybase_nmsuserinfo(serverip, userInfo);
				}
				if(serversInfo != null && serversInfo.size() > 0){
					dbDao.clearTableData("nms_sybaseserversinfo", serverip);
					dbDao.addSybase_nmsserversinfo(serverip, serversInfo);
				}
				if(engineInfo != null && engineInfo.size() > 0){
					dbDao.clearTableData("nms_sybaseengineinfo", serverip);
					dbDao.addSybase_nmsengineinfo(serverip, engineInfo);
				}
			}
			//ȡ����
			testGetSybaseDataFormDB(serverip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			dbDao.close();
		}
	}
	
	public void testGetSybaseDataFormDB(String serverip){
		String status = null;//���ݿ��״̬��Ϣ
		SybaseVO sybaseVO = null;
		List dbInfo = null;//���ݿ���Ϣ
		List deviceInfo = null;//�豸��Ϣ
		List userInfo = null;//�û���Ϣ
		List serversInfo = null;//��������Ϣ
		DBDao dao = new DBDao();
		try {
			Hashtable tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				status = (String)tempStatusHashtable.get("status");
			}
			sybaseVO = dao.getSybase_nmssybaseperformance(serverip);
			dbInfo = dao.getSybase_nmsdbinfo(serverip);
			deviceInfo = dao.getSybase_nmsdeviceinfo(serverip);
			userInfo = dao.getSybase_nmsuserinfo(serverip);
			serversInfo = dao.getSybase_nmsserversinfo(serverip);
			sybaseVO.setDbInfo(dbInfo);
			sybaseVO.setDeviceInfo(deviceInfo);
			sybaseVO.setUserInfo(userInfo);
			sybaseVO.setServersInfo(serversInfo);
			System.out.println("aaaaa");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		
		
		
	}
}
