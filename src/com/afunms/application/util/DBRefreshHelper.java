/**
 * <p>Description:tomcat util</p>
 * <p>Company: afunms</p>
 * @author miiwill
 * @project afunms
 * @date 2006-12-06
 */

package com.afunms.application.util;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.Db2spaceconfigDao;
import com.afunms.application.dao.InformixspaceconfigDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.dao.OraspaceconfigDao;
import com.afunms.application.dao.SqldbconfigDao;
import com.afunms.application.dao.SybspaceconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Db2spaceconfig;
import com.afunms.application.model.Informixspaceconfig;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.model.Oracle_sessiondata;
import com.afunms.application.model.Oraspaceconfig;
import com.afunms.application.model.Sqldbconfig;
import com.afunms.application.model.Sqlserver_processdata;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.model.Sybspaceconfig;
import com.afunms.application.model.TablesVO;
import com.afunms.common.util.*;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.inform.dao.NewDataDao;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadDB2File;
import com.afunms.polling.snmp.LoadInformixFile;
import com.afunms.polling.snmp.LoadMySqlFile;
import com.afunms.polling.snmp.LoadOracleFile;
import com.afunms.polling.snmp.LoadSQLServerFile;
import com.afunms.polling.snmp.LoadSysbaseFile;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.system.util.TimeGratherConfigUtil;

public class DBRefreshHelper
{
    private Element root;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private Hashtable sendeddata = ShareData.getSendeddata();
    public DBRefreshHelper()
    {
    } 
    
    /**
	 * nielin add 2010-08-05
	 */
	public void execute(DBVo vo){
		//System.out.println("=====================��ʼ�ɼ�");
		DBDao dbdao = null;
		try {

			List mslist = null;
			
			List oclist = null;
			
			List sysbaselist = null;
			
			List informixlist = null;
			
			List db2list = null;

			List mysqllist = null;
			
			if(vo != null){
				dbdao = new DBDao();
				try {
					//vo = (DBVo)dbdao.findByID(String.valueOf(vo.getId()));
					String password = EncryptUtil.decode(vo.getPassword());
					vo.setPassword(password);
					
				} catch (RuntimeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					dbdao.close();
				}
				if(vo != null ){
					DBTypeDao typeDao = new DBTypeDao();
					DBTypeVo type = null;
					try {
						type = (DBTypeVo)typeDao.findByID(String.valueOf(vo.getDbtype()));
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						typeDao.close();
					}
					//SysLogger.info("type================="+type.getDbtype());
					if("MySql".equals(type.getDbtype())){
						mysqllist = new ArrayList();
						mysqllist.add(vo);
					}else if("SQLServer".equals(type.getDbtype())){
						mslist = new ArrayList();
						mslist.add(vo);
					}
					else if("Oracle".equals(type.getDbtype())){
						oclist = new ArrayList();
						oclist.add(vo);
					}
					else if("Sybase".equals(type.getDbtype())){
						sysbaselist = new ArrayList();
						sysbaselist.add(vo);
					}
					else if("Informix".equals(type.getDbtype())){
						informixlist = new ArrayList();
						informixlist.add(vo);
					}
					else if("DB2".equals(type.getDbtype())){
						db2list = new ArrayList();
						db2list.add(vo);
					}
				}
			}
			
			//sqlserver�ɼ�
			if (mslist != null) {
				for (int i = 0; i < mslist.size(); i++) {
					Hashtable sqlserverdata = new Hashtable();
					DBVo dbmonitorlist = (DBVo) mslist.get(i);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					//��ʼ�����ݿ�ڵ�״̬
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();

					Calendar date = Calendar.getInstance();
					Date d = new Date();
					//�жϸ����ݿ��Ƿ���������
					boolean sqlserverIsOK = false;

					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						//�ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip
								+ ".sqlserver.log";
						File file = new File(filename);
						if (!file.exists()) {
							//�ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("###��ʼ����SQLSERVER:" + serverip + "�����ļ�###");
						LoadSQLServerFile loadsqlserver = new LoadSQLServerFile(filename);

						try {
							sqlserverdata = loadsqlserver.getSQLInital();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (sqlserverdata != null && sqlserverdata.size() > 0) {
							//�ж����ݿ�����״̬
							if (sqlserverdata.containsKey("status")) {
								int status = Integer.parseInt((String) sqlserverdata.get("status"));
								if (status == 1)
									sqlserverIsOK = true;
								if (!sqlserverIsOK) {
									//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);//.format(cc);		
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											//�������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setLastTime(lastTime);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "SQLSERVER(" + dbmonitorlist.getDbName() + " IP:"
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
											hostdata.setCollecttime(date);
											hostdata.setCategory("SQLPing");
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
												createSMS("sqlserver", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										hostdata.setCollecttime(date);
										hostdata.setCategory("SQLPing");
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
											createSMS("sqlserver", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									//��ͨ�������,����ͨ�����ݲ������
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									hostdata.setCollecttime(date);
									hostdata.setCategory("SQLPing");
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
								if (sqlserverIsOK) {
									//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
									Vector info_v = new Vector();
									Hashtable sysValue = new Hashtable();
									Vector altfiles_v = new Vector();
									Vector process_v = new Vector();
									Vector sysuser_v = new Vector();
									Vector lockinfo_v = new Vector();
									Hashtable sqlservervalue = new Hashtable();
									try {
										if (sqlserverdata.containsKey("info_v")) {
											info_v = (Vector) sqlserverdata.get("info_v");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									try {
										if (sqlserverdata.containsKey("sysValue")) {
											sysValue = (Hashtable) sqlserverdata.get("sysValue");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									if (info_v == null)
										info_v = new Vector();

									for (int j = 0; j < info_v.size(); j++) {
										Sqlserver_processdata sp = new Sqlserver_processdata();
										Hashtable ht = (Hashtable) info_v.get(j);
										String spid = ht.get("spid").toString();
										String dbname = ht.get("dbname").toString();
										String usernames = ht.get("username").toString();
										String cpu = ht.get("cpu").toString();
										String memusage = ht.get("memusage").toString();
										String physical_io = ht.get("physical_io").toString();

										String p_status = ht.get("status").toString();
										String hostname = ht.get("hostname").toString();
										String program_name = ht.get("program_name").toString();
										String login_time = ht.get("login_time").toString();
										sp.setCpu(Integer.parseInt(cpu));
										sp.setDbname(dbname);
										sp.setHostname(hostname);
										sp.setMemusage(Integer.parseInt(memusage));
										sp.setMon_time(d);
										sp.setPhysical_io(Long.parseLong(physical_io));
										sp.setProgram_name(program_name);
										sp.setSpid(spid);
										sp.setStatus(p_status);
										sp.setUsername(usernames);
										sp.setLogin_time(sdf.parse(login_time));
										sp.setServerip(serverip);
										try {
											dbdao.addSqlserver_processdata(sp);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									//								Hashtable retValue = new Hashtable();				
									//								retValue = dbdao.collectSQLServerMonitItemsDetail(serverip, "", username, passwords);
									//								sqlserverdata.put("retValue", retValue);

									Hashtable dbValue = new Hashtable();
									//�õ����ݿ�����Ϣ
									try {
										if (sqlserverdata.containsKey("dbValue")) {
											dbValue = (Hashtable) sqlserverdata.get("dbValue");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									//dbValue = dbdao.getSqlserverDB(serverip, username, passwords);
									//ShareData.setSqldbdata(serverip, dbValue);

									//�����ݿ�ռ���и澯���
									if (dbValue != null && dbValue.size() > 0) {
										SqldbconfigDao sqldbconfigManager = new SqldbconfigDao();
										Hashtable alarmdbs = sqldbconfigManager.getByAlarmflag(1);
										sqldbconfigManager.close();
										Hashtable database = (Hashtable) dbValue.get("database");
										//SysLogger.info("database size===="+database.size());
										Hashtable logfile = (Hashtable) dbValue.get("logfile");
										Vector names = (Vector) dbValue.get("names");
										if (alarmdbs == null)
											alarmdbs = new Hashtable();
										if (database == null)
											database = new Hashtable();
										if (logfile == null)
											logfile = new Hashtable();
										if (names != null && names.size() > 0) {
											for (int k = 0; k < names.size(); k++) {
												String dbname = (String) names.get(k);
												if (database.get(dbname) != null) {
													Hashtable db = (Hashtable) database.get(dbname);
													String usedperc = (String) db.get("usedperc");
													if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
														Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":"
																+ dbname + ":0");
														if (sqldbconfig == null)
															continue;
														if (usedperc == null)
															continue;
														if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
															//�澯
															SysLogger.info("### ��ʼ�澯 ###");
															dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																	dbmonitorlist.getId());
															dbnode.setAlarm(true);
															dbnode.setStatus(3);
															List alarmList = dbnode.getAlarmMessage();
															if (alarmList == null)
																alarmList = new ArrayList();
															dbnode.getAlarmMessage().add(
																	sqldbconfig.getDbname() + "��ռ䳬����ֵ"
																			+ sqldbconfig.getAlarmvalue());
															createSqldbSMS(dbmonitorlist, sqldbconfig);
														}
													}
												}
												if (logfile.get(dbname) != null) {
													Hashtable db = (Hashtable) logfile.get(dbname);
													String usedperc = (String) db.get("usedperc");
													if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
														Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":"
																+ dbname + ":1");
														if (sqldbconfig == null)
															continue;
														if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
															//�澯
															SysLogger.info("$$$ ��ʼ�澯 $$$");
															dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																	dbmonitorlist.getId());
															dbnode.setAlarm(true);
															dbnode.setStatus(3);
															List alarmList = dbnode.getAlarmMessage();
															if (alarmList == null)
																alarmList = new ArrayList();
															dbnode.getAlarmMessage().add(
																	sqldbconfig.getDbname() + "��ռ䳬����ֵ"
																			+ sqldbconfig.getAlarmvalue());
															createSqldbSMS(dbmonitorlist, sqldbconfig);
														}
													}
												}

											}
										}
									}
									ShareData.setSqlserverdata(serverip, sqlserverdata);
								}
							}
						}
					} else {
						//JDBC�ɼ���ʽ
						try {
							sqlserverIsOK = dbdao.getSqlserverIsOk(serverip, username, passwords);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							//dbdao.close();
						}
						if (!sqlserverIsOK) {
							sqlserverdata.put("status", "0");
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);//.format(cc);		
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setLastTime(lastTime);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "SQLSERVER(" + dbmonitorlist.getDbName() + " IP:"
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
									hostdata.setCollecttime(date);
									hostdata.setCategory("SQLPing");
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
										createSMS("sqlserver", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								hostdata.setCollecttime(date);
								hostdata.setCategory("SQLPing");
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
									createSMS("sqlserver", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							//��ͨ�������,����ͨ�����ݲ������
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							hostdata.setCollecttime(date);
							hostdata.setCategory("SQLPing");
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

						if (sqlserverIsOK) {//�����ݿ��������ϣ���������ݿ����ݵĲɼ�

							Vector info_v = new Vector();
							Hashtable sysValue = new Hashtable();
							//Vector altfiles_v = new Vector();
							Vector process_v = new Vector();
							Vector sysuser_v = new Vector();
							Vector lockinfo_v = new Vector();
							//�õ�ϵͳ��Ϣ
							try {
								sysValue = dbdao.getSqlServerSys(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (sysValue == null)
								sysValue = new Hashtable();

							//��ȡ����Ϣ
							try {
								lockinfo_v = dbdao.getSqlserverLockinfo(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (lockinfo_v == null)
								lockinfo_v = new Vector();

							//��ȡ������Ϣ
							try {
								info_v = dbdao.getSqlserverProcesses(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (info_v == null)
								info_v = new Vector();

							//����ȡ��������ӵķ���ֵ��
							sqlserverdata.put("sysValue", sysValue);
							sqlserverdata.put("lockinfo_v", lockinfo_v);
							sqlserverdata.put("info_v", info_v);
							sqlserverdata.put("status", "1");

							for (int j = 0; j < info_v.size(); j++) {
								Sqlserver_processdata sp = new Sqlserver_processdata();
								Hashtable ht = (Hashtable) info_v.get(j);
								String spid = ht.get("spid").toString();
								String dbname = ht.get("dbname").toString();
								String usernames = ht.get("username").toString();
								String cpu = ht.get("cpu").toString();
								String memusage = ht.get("memusage").toString();
								String physical_io = ht.get("physical_io").toString();

								String status = ht.get("status").toString();
								String hostname = ht.get("hostname").toString();
								String program_name = ht.get("program_name").toString();
								String login_time = ht.get("login_time").toString();

								sp.setCpu(Integer.parseInt(cpu));
								sp.setDbname(dbname);
								sp.setHostname(hostname);
								sp.setMemusage(Integer.parseInt(memusage));
								sp.setMon_time(d);
								sp.setPhysical_io(Long.parseLong(physical_io));
								sp.setProgram_name(program_name);
								sp.setSpid(spid);
								sp.setStatus(status);
								sp.setUsername(usernames);
								sp.setLogin_time(sdf.parse(login_time));
								sp.setServerip(serverip);
								try {
									dbdao.addSqlserver_processdata(sp);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Hashtable retValue = new Hashtable();
							retValue = dbdao.collectSQLServerMonitItemsDetail(serverip, "", username, passwords);
							sqlserverdata.put("retValue", retValue);

							Hashtable dbValue = new Hashtable();
							//�õ����ݿ�����Ϣ
							dbValue = dbdao.getSqlserverDB(serverip, username, passwords);
							sqlserverdata.put("dbValue", dbValue);

							ShareData.setSqldbdata(serverip, dbValue);
							//�����ݿ�ռ���и澯���
							if (dbValue != null && dbValue.size() > 0) {
								SqldbconfigDao sqldbconfigManager = new SqldbconfigDao();
								Hashtable alarmdbs = sqldbconfigManager.getByAlarmflag(1);
								sqldbconfigManager.close();
								Hashtable database = (Hashtable) dbValue.get("database");
								Hashtable logfile = (Hashtable) dbValue.get("logfile");
								Vector names = (Vector) dbValue.get("names");
								if (alarmdbs == null)
									alarmdbs = new Hashtable();
								if (database == null)
									database = new Hashtable();
								if (logfile == null)
									logfile = new Hashtable();
								if (names != null && names.size() > 0) {
									for (int k = 0; k < names.size(); k++) {
										String dbname = (String) names.get(k);
										if (database.get(dbname) != null) {
											Hashtable db = (Hashtable) database.get(dbname);
											String usedperc = (String) db.get("usedperc");
											if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
												Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":" + dbname
														+ ":0");
												if (sqldbconfig == null)
													continue;
												if (usedperc == null)
													continue;
												if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
													//�澯
													SysLogger.info("### ��ʼ�澯 ###");
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sqldbconfig.getDbname() + "��ռ䳬����ֵ" + sqldbconfig.getAlarmvalue());
													createSqldbSMS(dbmonitorlist, sqldbconfig);
												}
											}
										}
										if (logfile.get(dbname) != null) {
											Hashtable db = (Hashtable) logfile.get(dbname);
											String usedperc = (String) db.get("usedperc");
											if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
												Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":" + dbname
														+ ":1");
												if (sqldbconfig == null)
													continue;
												if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
													//�澯
													SysLogger.info("$$$ ��ʼ�澯 $$$");
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sqldbconfig.getDbname() + "��ռ䳬����ֵ" + sqldbconfig.getAlarmvalue());
													createSqldbSMS(dbmonitorlist, sqldbconfig);
												}
											}
										}

									}
								}
							}

							ShareData.setSqlserverdata(serverip, sqlserverdata);
						}
					}
				}
			}

			//ȡ��oracle�ɼ�
			if (oclist != null) {
				for (int i = 0; i < oclist.size(); i++) {
					Object obj = oclist.get(i);
					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					if (dbnode != null) {
						
						dbnode.setStatus(0);
						dbnode.setAlarm(false);
						dbnode.getAlarmMessage().clear();
						Calendar _tempCal = Calendar.getInstance();
						Date _cc = _tempCal.getTime();
						String _time = sdf.format(_cc);
						dbnode.setLastTime(_time);
						
						//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
	        			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
	        			int result = 0;
	        			result = timeconfig.isBetween(dbnode.getId()+"", "db");
						if(result == 0){
							SysLogger.info("###### "+dbnode.getIpAddress()+" ���ڲɼ�ʱ�����,����######");
							continue;
						}
						
					}else 
						continue;

					/*
					 * modify
					 * zhao-------------------------------------------------------
					 */
					OraclePartsDao partdao = new OraclePartsDao();
					List<OracleEntity> oracleparts = new ArrayList<OracleEntity>();
					try {
						oracleparts = partdao.findOracleParts(dbmonitorlist.getId(), 1);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (partdao != null)
							partdao.close();
					}

					/*------------------------------modify end---------------------------------*/
					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					int allFlag = 0;
					Date d1 = new Date();
					for (OracleEntity oracle : oracleparts) {
						// �жϸ����ݿ��Ƿ���������
						boolean oracleIsOK = false;
						dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());

						if (dbnode != null) {
							dbnode.setStatus(0);
							dbnode.setAlarm(false);
							dbnode.getAlarmMessage().clear();
							Calendar _tempCal = Calendar.getInstance();
							Date _cc = _tempCal.getTime();
							String _time = sdf.format(_cc);
							dbnode.setLastTime(_time);
						}
						if (oracle.getCollectType() == SystemConstant.DBCOLLECTTYPE_SHELL) {
							// �ű��ɼ���ʽ
							String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".oracle."
									+ oracle.getSid() + ".log";
							File file = new File(filename);
							if (!file.exists()) {
								// �ļ�������,������澯
								try {
									createFileNotExistSMS(serverip);
								} catch (Exception e) {
									e.printStackTrace();
								}
								continue;
							}
							SysLogger.info("###��ʼ����ORACLE:" + serverip + "�����ļ�###");
							LoadOracleFile loadoracle = new LoadOracleFile(filename);
							Hashtable oracledata = new Hashtable();
							try {
								oracledata = loadoracle.getOracleInit();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (oracledata != null && oracledata.size() > 0) {
								// �ж����ݿ�����״̬
								if (oracledata.containsKey("status")) {
									int status = Integer.parseInt((String) oracledata.get("status"));
									if (status == 1)
										oracleIsOK = true;
									if (!oracleIsOK) {
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
												String sysLocation = "";
												// eventdao=null;
												try {
													SmscontentDao eventdao = new SmscontentDao();

													dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ȫ��ֹͣ");
													String eventdesc = "ORACLE(" + " IP:" + dbmonitorlist.getIpAddress() + ")"
															+ "�����ݿ����ֹͣ";
													eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + ":"
															+ oracle.getId(), dbmonitorlist.getAlias() + "("
															+ dbmonitorlist.getIpAddress() + ":" + oracle.getSid() + ")",
															eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													// eventdao.close();
												}
											} else {
												try {
													Pingcollectdata hostdata = null;
													hostdata = new Pingcollectdata();
													hostdata.setIpaddress(serverip + ":" + oracle.getId());
													Calendar date = Calendar.getInstance();
													hostdata.setCollecttime(date);
													hostdata.setCategory("ORAPing");
													hostdata.setEntity("Utilization");
													hostdata.setSubentity("ConnectUtilization");
													hostdata.setRestype("dynamic");
													hostdata.setUnit("%");
													hostdata.setThevalue("0");
													dbdao.createHostData(hostdata);
													dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

													// String
													// ip=hostdata.getIpaddress();
													// hostdata.setIpaddress(ip+":"+)

													// ���Ͷ���

													createSMS("oracle", dbmonitorlist, oracle);
//													Hashtable hash = new Hashtable();
//													hash.elements()
//													while(hash.)
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										} else {
											try {
												// for (OracleEntity or : stops)
												// {
												Pingcollectdata hostdata = null;
												hostdata = new Pingcollectdata();
												hostdata.setIpaddress(serverip + ":" + oracle.getId());
												Calendar date = Calendar.getInstance();
												hostdata.setCollecttime(date);
												hostdata.setCategory("ORAPing");
												hostdata.setEntity("Utilization");
												hostdata.setSubentity("ConnectUtilization");
												hostdata.setRestype("dynamic");
												hostdata.setUnit("%");
												hostdata.setThevalue("0");
												dbdao.createHostData(hostdata);
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												// }
												// Set set=allConnec.entrySet();
												// ���Ͷ���
												createSMS("oracle", dbmonitorlist, oracle);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										try {
											// for (OracleEntity or :
											// oracleparts) {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip + ":" + oracle.getId());
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("ORAPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("100");
											dbdao.createHostData(hostdata);
											if (sendeddata.containsKey("oracle" + ":" + serverip + ":" + oracle.getSid()))
												sendeddata.remove("oracle" + ":" + serverip + ":" + oracle.getSid());

										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}
									if(oracleIsOK){
										//�����ݿ�������,�������ص����ݲɼ�
										ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
//										Hashtable alldatas = (Hashtable) ShareData.getAlloracledata();
										Vector tableinfo_v = null;
//										Hashtable datas = (Hashtable) alldatas.get(serverip + ":" + oracle.getId());
										IpTranslation tranfer = new IpTranslation();
										String hex = tranfer.formIpToHex(vo.getIpAddress());
										String serveripstr = hex+":"+oracle.getSid();
										try {

//											tableinfo_v = (Vector) datas.get("tableinfo_v");
											tableinfo_v = dbdao.getOracle_nmsoraspaces(serveripstr);
										} catch (Exception e) {
											e.printStackTrace();
										}
										if (tableinfo_v != null && tableinfo_v.size() > 0) {
											// �滻ԭ����SESSSION����
											ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
											OraspaceconfigDao oraspaceconfigManager = new OraspaceconfigDao();
											Hashtable oraspaces = oraspaceconfigManager.getByAlarmflag(1);
											oraspaceconfigManager.close();
											Vector spaces = new Vector();
											for (int k = 0; k < tableinfo_v.size(); k++) {
												Hashtable ht = (Hashtable) tableinfo_v.get(k);
												String tablespace = ht.get("tablespace").toString();
												if (spaces.contains(tablespace))
													continue;
												spaces.add(tablespace);
												String percent = ht.get("percent_free").toString();
												if (oraspaces.containsKey(serverip + ":" + oracle.getId() + ":" + tablespace)) {
													// ������Ҫ�澯�ı�ռ�
													Integer free = 0;
													try {
														free = new Float(percent).intValue();
													} catch (Exception e) {
														e.printStackTrace();
													}
													// ���ݱ�ռ�澯�����ж��Ƿ�澯
													Oraspaceconfig oraspaceconfig = (Oraspaceconfig) oraspaces.get(serverip + ":"
															+ oracle.getId() + ":" + tablespace);
													if (oraspaceconfig.getAlarmvalue() < (100 - free)) {
														// �澯
														dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
														dbnode.setAlarm(true);
														List alarmList = dbnode.getAlarmMessage();
														if (alarmList == null)
															alarmList = new ArrayList();
														dbnode.getAlarmMessage().add(
																oraspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																		+ oraspaceconfig.getAlarmvalue());
														dbnode.setStatus(3);
														createSpaceSMS(dbmonitorlist, oraspaceconfig, oracle);
													}
												}

											}
										}
										if (tableinfo_v != null && tableinfo_v.size() > 0)
											ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
										Vector info_v = null;
//										info_v = (Vector) datas.get("info_v");
										info_v = dbdao.getOracle_nmsorarollback(serveripstr); 
										for (int j = 0; j < info_v.size(); j++) {
											Oracle_sessiondata os = new Oracle_sessiondata();
											Hashtable ht = (Hashtable) info_v.get(j);
											String machine = ht.get("machine").toString();
											String usernames = ht.get("username").toString();
											String program = ht.get("program").toString();
											String status1 = ht.get("status").toString();
											String sessiontype = ht.get("sessiontype").toString();
											String command = ht.get("command").toString();
											String logontime = ht.get("logontime").toString();
											os.setCommand(command);
											os.setLogontime(sdf1.parse(logontime));
											os.setMachine(machine);
											os.setMon_time(d1);
											os.setProgram(program);
											os.setSessiontype(sessiontype);
											os.setStatus(status1);
											os.setUsername(usernames);
//											IpTranslation tranfer = new IpTranslation();
											hex = tranfer.formIpToHex(serverip);
											os.setServerip(hex + ":" + oracle.getId());
											os.setDbname(dbnames);
											try {
												dbdao.addOracle_sessiondata(os);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
									// Hashtable oracledata = new Hashtable();

								}	
							}
							if(oracledata!=null)
								 ShareData.setAlloracledata(serverip,
								 oracledata);

						} else {
							// JDBC�ɼ���ʽ
							Hashtable oracledata = new Hashtable();
							allFlag = 0;
							/* modify zhao--------------------- */
							// List<OracleEntity> stops = new
							// LinkedList<OracleEntity>();
							// Hashtable<String, OracleEntity> allConnec = new
							// Hashtable<String, OracleEntity>();
							// for (OracleEntity ora : oracleparts) {
							try {
								oracleIsOK = dbdao.getOracleIsOK(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
									.getPassword()));
							}catch(Exception e){
								e.printStackTrace();
							}

							if (!oracleIsOK) {
								// allstatus.put(ora.getSid(),ora);
								allFlag = 1;
								// stops.add(ora);
								//dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getDbid());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
							}
							// }
							//				
							/* modify end --------------- */
							if (allFlag == 1) {
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
										String sysLocation = "";
										// eventdao=null;
										try {
											SmscontentDao eventdao = new SmscontentDao();

											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ȫ��ֹͣ");
											String eventdesc = "ORACLE(" + " IP:" + dbmonitorlist.getIpAddress() + ")"
													+ "�����ݿ����ֹͣ";
											eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + ":" + oracle.getId(),
													dbmonitorlist.getAlias() + "(" + dbmonitorlist.getIpAddress() + ":"
															+ oracle.getSid() + ")", eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											// eventdao.close();
										}
									} else {
										try {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip + ":" + oracle.getId());
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("ORAPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											dbdao.createHostData(hostdata);
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

											// String
											// ip=hostdata.getIpaddress();
											// hostdata.setIpaddress(ip+":"+)

											// ���Ͷ���

											createSMS("oracle", dbmonitorlist, oracle);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

								} else {
									try {
										// for (OracleEntity or : stops) {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip + ":" + oracle.getId());
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("ORAPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										dbdao.createHostData(hostdata);
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getDbid());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										// }
										// Set set=allConnec.entrySet();
										// ���Ͷ���
										createSMS("oracle", dbmonitorlist, oracle);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								try {
									// for (OracleEntity or : oracleparts) {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip + ":" + oracle.getId());
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("ORAPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									dbdao.createHostData(hostdata);
									if (sendeddata.containsKey("oracle" + ":" + serverip + ":" + oracle.getSid()))
										sendeddata.remove("oracle" + ":" + serverip + ":" + oracle.getSid());
									// }
								} catch (Exception e) {
									e.printStackTrace();
								}
								/* modify zhao ---------------------------- */
							}
							if(allFlag == 0){
								passwords = EncryptUtil.decode(oracle.getPassword());
								Hashtable allOraData =  dbdao.getAllOracleData(serverip, port, oracle.getSid(), oracle.getUser(), passwords);
								Vector info = (Vector) allOraData.get("session");
								Vector tableinfo = (Vector) allOraData.get("tablespace");
								Vector rollbackinfo_v = (Vector) allOraData.get("rollback");
								Hashtable sysValue = (Hashtable) allOraData.get("sysinfo");
								Hashtable memValue = (Hashtable) allOraData.get("ga_hash");
								Vector lockinfo_v = (Vector) allOraData.get("lock");
								Hashtable memPerfValue = (Hashtable) allOraData.get("memoryPerf");
								Vector table_v = (Vector) allOraData.get("table");
								Vector sql_v = (Vector) allOraData.get("topsql");
								Vector contrFile_v = (Vector) allOraData.get("controlfile");
								Hashtable isArchive_h = (Hashtable) allOraData.get("sy_hash");
								Vector logFile_v = (Vector) allOraData.get("log");
								Vector keepObj_v = (Vector) allOraData.get("keepobj");  
								String lstrnStatu = (String) allOraData.get("open_mode");
								Vector extent_v = (Vector) allOraData.get("extent");
								Hashtable userinfo_h = (Hashtable) allOraData.get("user");
								
//								Vector info = dbdao.getOracleSession(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle.getPassword()));
//								Vector tableinfo = dbdao.getOracleTableinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector rollbackinfo_v = dbdao.getOracleRollbackinfo(serverip, port, oracle.getSid(),
//										oracle.getUser(), EncryptUtil.decode(oracle.getPassword()));
//								Hashtable sysValue = dbdao.getOracleSys(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable memValue = dbdao.getOracleMem(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable memPerfValue = dbdao.getOracleMemPerf(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector lockinfo_v = dbdao.getOracleLockinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector sql_v = dbdao.getOracleTop10Sql(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector table_v = dbdao.getOracleTable(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector contrFile_v = dbdao.getOracleControlFile(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable isArchive_h = dbdao.getOracleIsArchive(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector logFile_v = dbdao.getOracleLogFile(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector keepObj_v = dbdao.getOracleKeepObj(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								String lstrnStatu = dbdao.getOracleLstn(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector extent_v = dbdao.getOracleExtent(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable userinfo_h = dbdao.getOracleUserinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
								
								if (info == null)
									info = new Vector();
								if (sysValue == null)
									sysValue = new Hashtable();
								if (memValue == null)
									memValue = new Hashtable();
								if (memPerfValue == null)
									memPerfValue = new Hashtable();
								if (tableinfo == null)
									tableinfo = new Vector();
								if (rollbackinfo_v == null)
									rollbackinfo_v = new Vector();
								if (lockinfo_v == null)
									lockinfo_v = new Vector();
								if (table_v == null)
									table_v = new Vector();
								if (sql_v == null)
									sql_v = new Vector();
								if (contrFile_v == null)
									contrFile_v = new Vector();
								if (logFile_v == null)
									logFile_v = new Vector();
								if (keepObj_v == null)
									keepObj_v = new Vector();
								if (isArchive_h == null)
									isArchive_h = new Hashtable();
								if(lstrnStatu == null)
									lstrnStatu = "";
								if(extent_v == null)
									extent_v = new Vector();
								if(userinfo_h == null)
									userinfo_h = new Hashtable();
								oracledata.put("sysValue", sysValue);
								oracledata.put("memValue", memValue);
								oracledata.put("memPerfValue", memPerfValue);
								oracledata.put("tableinfo_v", tableinfo);
								oracledata.put("rollbackinfo_v", rollbackinfo_v);
								oracledata.put("lockinfo_v", lockinfo_v);
								oracledata.put("info_v", info);
								oracledata.put("table_v", table_v);
								oracledata.put("sql_v", sql_v);
								oracledata.put("contrFile_v", contrFile_v);
								oracledata.put("isArchive_h", isArchive_h);
								oracledata.put("keepObj_v", keepObj_v);
								oracledata.put("lstrnStatu", lstrnStatu);
								oracledata.put("extent_v", extent_v);
								oracledata.put("logFile_v", logFile_v);
								oracledata.put("userinfo_h", userinfo_h);
								oracledata.put("status", "1");
								ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
								// }
								/*-----------modify  end  ------------------------------*/

//								Hashtable alldatas = (Hashtable) ShareData.getAlloracledata();
								Vector tableinfo_v = null;
								//Hashtable datas = (Hashtable) alldatas.get(serverip + ":" + oracle.getId());
								IpTranslation tranfer = new IpTranslation();
								String hex = tranfer.formIpToHex(vo.getIpAddress());
								String serveripstr = hex+":"+oracle.getSid();
								Hashtable datas=oracledata;
								try {

//									tableinfo_v = (Vector) datas.get("tableinfo_v");
									tableinfo_v = dbdao.getOracle_nmsoraspaces(serveripstr);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (tableinfo_v != null && tableinfo_v.size() > 0) {
									// �滻ԭ����SESSSION����
									ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
									OraspaceconfigDao oraspaceconfigManager = new OraspaceconfigDao();
									Hashtable oraspaces = oraspaceconfigManager.getByAlarmflag(1);
									oraspaceconfigManager.close();
									Vector spaces = new Vector();
									for (int k = 0; k < tableinfo_v.size(); k++) {
										Hashtable ht = (Hashtable) tableinfo_v.get(k);
										String tablespace = ht.get("tablespace").toString();
										if (spaces.contains(tablespace))
											continue;
										spaces.add(tablespace);
										String percent = ht.get("percent_free").toString();
										if (oraspaces.containsKey(serverip + ":" + oracle.getId() + ":" + tablespace)) {
											// ������Ҫ�澯�ı�ռ�
											Integer free = 0;
											try {
												free = new Float(percent).intValue();
											} catch (Exception e) {
												e.printStackTrace();
											}
											// ���ݱ�ռ�澯�����ж��Ƿ�澯
											Oraspaceconfig oraspaceconfig = (Oraspaceconfig) oraspaces.get(serverip + ":"
													+ oracle.getId() + ":" + tablespace);
											if (oraspaceconfig.getAlarmvalue() < (100 - free)) {
												// �澯
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add(
														oraspaceconfig.getSpacename() + "��ռ䳬����ֵ" + oraspaceconfig.getAlarmvalue());
												dbnode.setStatus(3);
												createSpaceSMS(dbmonitorlist, oraspaceconfig, oracle);
											}
										}

									}
								}
								if (tableinfo_v != null && tableinfo_v.size() > 0)
									ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
								Vector info_v = null;
								info_v = (Vector) datas.get("info_v");
								for (int j = 0; j < info_v.size(); j++) {
									Oracle_sessiondata os = new Oracle_sessiondata();
									Hashtable ht = (Hashtable) info_v.get(j);
									String machine = ht.get("machine").toString();
									String usernames = ht.get("username").toString();
									String program = ht.get("program").toString();
									String status = ht.get("status").toString();
									String sessiontype = ht.get("sessiontype").toString();
									String command = ht.get("command").toString();
									String logontime = ht.get("logontime").toString();
									os.setCommand(command);
									os.setLogontime(sdf1.parse(logontime));
									os.setMachine(machine);
									os.setMon_time(d1);
									os.setProgram(program);
									os.setSessiontype(sessiontype);
									os.setStatus(status);
									os.setUsername(usernames);
//									IpTranslation tranfer = new IpTranslation();
									hex = tranfer.formIpToHex(serverip);
									os.setServerip(hex + ":" + oracle.getId());
									os.setDbname(dbnames);
									try {
										dbdao.addOracle_sessiondata(os);
									} catch (Exception e) {
										e.printStackTrace();
									}
							}
						}
							//allFlag == 1
							if(oracledata!=null)
							   ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
						}
					}
				}

			}

			//ȡ��sysbase�ɼ�
			if (sysbaselist != null) {
				SybspaceconfigDao sybspaceconfigManager = new SybspaceconfigDao();
				Hashtable sybspaceconfig = new Hashtable();
				try {
					sybspaceconfig = sybspaceconfigManager.getByAlarmflag(1);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sybspaceconfigManager.close();
				}

				for (int i = 0; i < sysbaselist.size(); i++) {
					Object obj = sysbaselist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
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
							continue;
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
								sysbaseVO = dbdao.getSysbaseInfo(serverip, port, username, passwords);
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
						}
					}

					SysLogger.info("end collect sysbase --------- " + serverip);
				}
			}

			//ȡ��informix�ɼ�
			if (informixlist != null) {
				for (int i = 0; i < informixlist.size(); i++) {
					Hashtable monitorValue = new Hashtable();

					Object obj = informixlist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					int allFlag = 0;
					SysLogger.info("begin collect informix--" + dbnames + " --------- " + serverip);
					boolean informixIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip
								+ ".informix.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
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
							createSMS("informix", dbmonitorlist);
							allFlag = 1;
						} else {
							//�������ϣ���������ݲɼ�
							Hashtable returnValue = new Hashtable();
							try {
								returnValue = dbdao.getInformixDBConfig(serverip, port + "", username, passwords, dbnames,
										dbservername);
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
													dbnode.getAlarmMessage().add(
															informixspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																	+ informixspaceconfig.getAlarmvalue());
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
							Hashtable informixData = new Hashtable();
							informixData.put("status", "1");
							informixData.put("informix", returnValue);
							monitorValue.put(dbnames, informixData);

						}
						SysLogger.info("end collect informix--" + dbnames + " --------- " + serverip);
						if (allFlag == 1) {
							//��һ�����ݿ��ǲ�ͨ��
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

					SysLogger.info("end collect informix --------- " + serverip);
				}
			}

			//ȡ��db2�ɼ�
			if (db2list != null) {
				for (int i = 0; i < db2list.size(); i++) {
					Object obj = db2list.get(i);
					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
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
							continue;
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
						allDb2Data.put("status", "1");
						// ��DB2���ݽ��вɼ�

						// ��space��Ϣ���вɼ�
						Hashtable spaceHash = dbdao.getDB2Space(serverip, port, dbnames, username, passwords);
						allDb2Data.put("spaceInfo", spaceHash);

						// ��sys��Ϣ���вɼ�
						Hashtable sysHash = dbdao.getDB2Sys(serverip, port, dbnames, username, passwords);
						allDb2Data.put("sysInfo", sysHash);

						// ��pool��Ϣ���вɼ�
						Hashtable poolHash = dbdao.getDB2Pool(serverip, port, dbnames, username, passwords);
						allDb2Data.put("poolInfo", poolHash);

						// ��session��Ϣ���вɼ�
						Hashtable sessionHash = dbdao.getDB2Session(serverip, port, dbnames, username, passwords);
						allDb2Data.put("session", sessionHash);

						String[] alldbs = dbnames.split(",");

						// ��lock��Ϣ���вɼ�
						Hashtable lockInfo = new Hashtable<String, List>();
						for (String db : alldbs) {
							List lockHash = dbdao.getDB2Lock(serverip, port, dbnames, username, passwords);
							lockInfo.put(db, lockHash);
						}
						allDb2Data.put("lock", lockInfo);
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
					}

					SysLogger.info("end process db2 ====== " + serverip);
				}
			}

			//ȡ��mysql�ɼ�
			if (mysqllist != null) {
				//SybspaceconfigDao sybspaceconfigManager=new SybspaceconfigDao();
				//Hashtable sybspaceconfig = sybspaceconfigManager.getByAlarmflag(1);
				//sybspaceconfigManager.close();
				for (int i = 0; i < mysqllist.size(); i++) {
					Hashtable monitorValue = new Hashtable();

					Object obj = mysqllist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					String[] dbs = dbnames.split(",");
					//�жϸ����ݿ��Ƿ���������
					int allFlag = 0;
					boolean mysqlIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						System.out.println("-------mysql���ýű���ʽ�ɼ�-----");
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".mysql.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("#######################��ʼ����Mysql:" + serverip + "�����ļ�###########");
						LoadMySqlFile loadmysql = new LoadMySqlFile(filename);
						Hashtable mysqlData = new Hashtable();
						try {
							// sqlserverdata = loadsqlserver.getSQLInital();
							mysqlData = loadmysql.getMySqlCongfig();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mysqlData != null && mysqlData.size() > 0) {
							System.out.println(mysqlData.containsKey("status"));
							if (mysqlData.containsKey("status")) {
								int status = Integer.parseInt((String) mysqlData.get("status"));
								if (status == 1)
									mysqlIsOK = true;
								if (!mysqlIsOK) {
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									createSMS("mysql", dbmonitorlist);
									allFlag = 1;
								} else {
									// �������ϣ���������ݲɼ�
									for (int k = 0; k < dbs.length; k++) {
										String dbStr = dbs[k];
										Hashtable returnValue = new Hashtable();
										returnValue = (Hashtable) mysqlData.get(dbStr);
										if(returnValue!=null)
										   monitorValue.put(dbStr, returnValue);
										System.out.println("------jjjjjjjj---------");
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
													String eventdesc = "MYSQL(" + dbmonitorlist.getDbName() + " IP:"
															+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
													eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "",
															dbmonitorlist.getAlias() + "(" + dbmonitorlist.getIpAddress() + ")",
															eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
												} catch (Exception e) {
													e.printStackTrace();
												}
											} else {
												Pingcollectdata hostdata = null;
												hostdata = new Pingcollectdata();
												hostdata.setIpaddress(serverip);
												Calendar date = Calendar.getInstance();
												hostdata.setCollecttime(date);
												hostdata.setCategory("MYPing");
												hostdata.setEntity("Utilization");
												hostdata.setSubentity("ConnectUtilization");
												hostdata.setRestype("dynamic");
												hostdata.setUnit("%");
												hostdata.setThevalue("0");
												try {
													dbdao.createHostData(hostdata);
													// ���Ͷ���
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
													dbnode.setStatus(3);
													createSMS("mysql", dbmonitorlist);
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
											hostdata.setCategory("MYPing");
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
												createSMS("mysql", dbmonitorlist);
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
										hostdata.setCategory("MYPing");
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
										// �����ݿ��������ϣ���������ݿ����ݵĲɼ�
										/*
										 * SybaseVO sysbaseVO = new SybaseVO();
										 * try{ sysbaseVO =
										 * dbdao.getSysbaseInfo(serverip,
										 * port,username, passwords);
										 * }catch(Exception e){
										 * e.printStackTrace(); } if (sysbaseVO ==
										 * null)sysbaseVO = new SybaseVO();
										 * Hashtable retValue = new Hashtable();
										 * retValue.put("sysbaseVO", sysbaseVO);
										 * ShareData.setSysbasedata(serverip,
										 * retValue); List allspace =
										 * sysbaseVO.getDbInfo(); if(allspace !=
										 * null && allspace.size()>0){ for(int
										 * k=0;k<allspace.size();k++){ TablesVO
										 * tvo = (TablesVO)allspace.get(k);
										 * if(sybspaceconfig
										 * .containsKey(serverip
										 * +":"+tvo.getDb_name())){ //�澯�ж�
										 * Sybspaceconfig sybconfig =
										 * (Sybspaceconfig
										 * )sybspaceconfig.get(serverip
										 * +":"+tvo.getDb_name()); Integer
										 * usedperc =
										 * Integer.parseInt(tvo.getDb_usedperc
										 * ());
										 * if(usedperc>sybconfig.getAlarmvalue
										 * ()){ //������ֵ�澯 dbnode =
										 * (DBNode)PollingEngine
										 * .getInstance().getDbByID
										 * (dbmonitorlist.getId());
										 * dbnode.setAlarm(true); List alarmList =
										 * dbnode.getAlarmMessage();
										 * if(alarmList == null)alarmList = new
										 * ArrayList();
										 * dbnode.getAlarmMessage().
										 * add(sybconfig
										 * .getSpacename()+"��ռ䳬����ֵ"
										 * +sybconfig.getAlarmvalue());
										 * //dbnode.setStatus(3);
										 * createSybSpaceSMS
										 * (dbmonitorlist,sybconfig); } } } }
										 */
									}
									if (allFlag == 0) {
										monitorValue.put("runningflag", "��������");
									} else {
										monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
									}

									if (monitorValue != null && monitorValue.size() > 0) {
										ShareData.setMySqlmonitordata(serverip, monitorValue);
									}
								}
							}
						}
						// //////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						for (int k = 0; k < dbs.length; k++) {
							SysLogger.info("begin collect mysql--" + dbs[k] + " --------- " + serverip);
							String dbStr = dbs[k];

							try {
								mysqlIsOK = dbdao.getMySqlIsOk(serverip, username, passwords, dbStr);
							} catch (Exception e) {
								e.printStackTrace();
								mysqlIsOK = false;
							}
							if (!mysqlIsOK) {
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
								createSMS("mysql", dbmonitorlist);
								allFlag = 1;
							} else {
								//�������ϣ���������ݲɼ�
								Hashtable returnValue = new Hashtable();
								try {
									returnValue = dbdao.getMySqlDBConfig(serverip, username, passwords, dbStr);
									Vector vector = dbdao.getStatus(serverip, username, passwords, dbStr);
									Vector vector1 = dbdao.getVariables(serverip, username, passwords, dbStr);
									returnValue.put("variables", vector);
									returnValue.put("global_status", vector1);
									Vector dispose = dbdao.getDispose(serverip, username, passwords, dbStr);
									Vector dispose1 = dbdao.getDispose1(serverip, username, passwords, dbStr);
									Vector dispose2 = dbdao.getDispose2(serverip, username, passwords, dbStr);
									Vector dispose3 = dbdao.getDispose3(serverip, username, passwords, dbStr);
									returnValue.put("dispose", dispose);
									returnValue.put("dispose1", dispose1);
									returnValue.put("dispose2", dispose2);
									returnValue.put("dispose3", dispose3);
								} catch (Exception e) {
									e.printStackTrace();
								}
								monitorValue.put(dbStr, returnValue);

							}
							SysLogger.info("end collect mysql--" + dbs[k] + " --------- " + serverip);
						}
						if (allFlag == 1) {
							//��һ�����ݿ��ǲ�ͨ��
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
										String eventdesc = "MYSQL(" + dbmonitorlist.getDbName() + " IP:"
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
									hostdata.setCategory("MYPing");
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
										createSMS("mysql", dbmonitorlist);
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
								hostdata.setCategory("MYPing");
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
									createSMS("mysql", dbmonitorlist);
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
							hostdata.setCategory("MYPing");
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
							//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
							/*
							SybaseVO sysbaseVO = new SybaseVO();
							try{
								sysbaseVO = dbdao.getSysbaseInfo(serverip, port,username, passwords);
							}catch(Exception e){
								e.printStackTrace();
							}
							if (sysbaseVO == null)sysbaseVO = new SybaseVO();
							Hashtable retValue = new Hashtable();
							retValue.put("sysbaseVO", sysbaseVO);
							ShareData.setSysbasedata(serverip, retValue);
							List allspace = sysbaseVO.getDbInfo();
							if(allspace != null && allspace.size()>0){						
								for(int k=0;k<allspace.size();k++){
									TablesVO tvo = (TablesVO)allspace.get(k); 
									if(sybspaceconfig.containsKey(serverip+":"+tvo.getDb_name())){
										//�澯�ж�
										Sybspaceconfig sybconfig = (Sybspaceconfig)sybspaceconfig.get(serverip+":"+tvo.getDb_name());
										Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
										if(usedperc>sybconfig.getAlarmvalue()){
											//������ֵ�澯
											dbnode = (DBNode)PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if(alarmList == null)alarmList = new ArrayList();
											dbnode.getAlarmMessage().add(sybconfig.getSpacename()+"��ռ䳬����ֵ"+sybconfig.getAlarmvalue());
											//dbnode.setStatus(3);
											createSybSpaceSMS(dbmonitorlist,sybconfig);
										}
									}
								}
							}
							 */
						}
						if (allFlag == 0) {
							monitorValue.put("runningflag", "��������");
						} else {
							monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
						}

						if (monitorValue != null && monitorValue.size() > 0) {
							ShareData.setMySqlmonitordata(serverip, monitorValue);
						}
					}

					SysLogger.info("end collect mysql --------- " + serverip);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbdao.close();
			//System.out.println("********DB Thread Count : " + Thread.activeCount());
		}
	} 
	
	/*--------modify   zhao -------------------------*/
	public void createSMS(String db, DBVo dbmonitorlist, OracleEntity oracle) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		// for (OracleEntity oracle : oracles) {
		try {
			if (!sendeddata.containsKey(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
				smscontent.setMessage(db + "(" + oracle.getSid() + " IP:" + dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("ping");
				smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
				// ���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendeddata.put(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
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
					smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
					// smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+"
					// IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
					// ���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Throwable e) {

					}
					// �޸��Ѿ����͵Ķ��ż�¼
					sendeddata.put(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }

	}

	public void createSMS(String db, DBVo dbmonitorlist) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			if (!sendeddata.containsKey(db + ":" + dbmonitorlist.getIpAddress())) {
				//�����ڣ��������ţ�������ӵ������б���
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
				//���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
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
					//����һ�죬���ٷ���Ϣ
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
					//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
					//���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createSpaceSMS(DBVo dbmonitorlist, Oraspaceconfig oraspaceconfig, OracleEntity oracle) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		// String
		// errorcontent="oraspace&"+time+"&"+dbmonitorlist.getId()+"&"+oraspaceconfig.getSpacename()+"("+dbmonitorlist.getDbName()+"
		// IP:"+dbmonitorlist.getIpAddress()+")"+"�ı�ռ䳬����ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
				smscontent.setMessage(dbmonitorlist.getIpAddress() + ":" + oracle.getSid() + "�����ݿ��"
						+ oraspaceconfig.getSpacename() + "��ռ䳬��" + oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("oraspace");
				smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
				// smscontent.setMessage(errorcontent);
				// ���Ͷ���
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + oracle.getId() + ":"
						+ oraspaceconfig.getSpacename());
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
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
					smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + oraspaceconfig.getSpacename() + "��ռ䳬��"
							+ oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("oraspace");
					smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getId());
					// ���Ͷ���
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					sendeddata.put(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename(), date);
				} else {
					// ��д�����澯����
					// �������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(dbmonitorlist.getIpAddress() + "�����ݿ��" + oraspaceconfig.getSpacename() + "��ռ䳬��"
							+ oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarminfomanager.close();
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
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarminfomanager.close();
		}
	}

	public static void createSqldbSMS(DBVo dbmonitorlist, Sqldbconfig sqldbconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = "";
		if (sqldbconfig.getLogflag() == 0) {
			//���ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "�Ŀ�ռ䳬��" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		} else {
			//��־�ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "����־����" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		}

		try {
			if (!sendeddata.containsKey(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				//String time1 = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("sqldb");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + sqldbconfig.getDbname() + ":"
						+ sqldbconfig.getLogflag());
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
					//String time1 = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("sqldb");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);

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
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFileNotExistSMS(String ipaddress) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			Host host = (Host) PollingEngine.getInstance().getNodeByIP(ipaddress);
			if (host == null)
				return;

			if (!sendeddata.containsKey(ipaddress + ":file:" + host.getId())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("3");
				smscontent.setObjid(host.getId() + "");
				smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("host");
				smscontent.setSubentity("ftp");
				smscontent.setIp(host.getIpAddress());//���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(ipaddress + ":file" + host.getId(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
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
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("3");
					smscontent.setObjid(host.getId() + "");
					smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("host");
					smscontent.setSubentity("ftp");
					smscontent.setIp(host.getIpAddress());//���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					smsmanager.sendURLSmscontent(smscontent);
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":file:" + host.getId(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
