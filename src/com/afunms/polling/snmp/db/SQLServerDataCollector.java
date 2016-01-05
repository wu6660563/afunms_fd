package com.afunms.polling.snmp.db;

import java.awt.Label;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import montnets.SmsDao;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.SqldbconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Sqldbconfig;
import com.afunms.application.model.Sqlserver_processdata;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.CheckEventUtil;
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
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadSQLServerFile;
import com.afunms.system.util.TimeGratherConfigUtil;

public class SQLServerDataCollector {

	private Hashtable sendeddata = ShareData.getSendeddata();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	public SQLServerDataCollector() {

	}
	public void collect_Data(NodeGatherIndicators nodeGatherIndicators) {
	    try {
            DBDao dbdao = new DBDao();
            DBVo DBVo = null; 
            try{
                DBVo = (DBVo)dbdao.findByID(nodeGatherIndicators.getNodeid());
            }catch(Exception e){
                
            }finally{
                dbdao.close();
            }
            if(DBVo == null)return;
            if(DBVo.getManaged() == 0)return;

            String serverIP = DBVo.getIpAddress();
            String userName = DBVo.getUser();
            String password = null;
            try {
                password = EncryptUtil.decode(DBVo.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int port = Integer.parseInt(DBVo.getPort());
            String DBNames = DBVo.getDbName();
            // ��ʼ�����ݿ�ڵ�״̬
            boolean sqlserverIsOK = false;
            try {
                sqlserverIsOK = dbdao.getSqlserverIsOk(serverIP, userName, password);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbdao.close();
            }
            
            IpTranslation tranfer = new IpTranslation();
            String hex = tranfer.formIpToHex(serverIP);
            Hashtable sqlserverdata = new Hashtable();
            if (sqlserverIsOK) {
                sqlserverdata.put("status", "1");//nms_sqlserverstatus
                
                Vector info_v = new Vector();
                Hashtable sysValue = new Hashtable();
                // Vector altfiles_v = new Vector();
                Vector process_v = new Vector();
                Vector sysuser_v = new Vector();
                Vector lockinfo_v = new Vector();
                
                Hashtable gatherHash = new Hashtable();
                gatherHash.put("sysvalue", nodeGatherIndicators);
                gatherHash.put("lock", nodeGatherIndicators);
                gatherHash.put("process", nodeGatherIndicators);
                gatherHash.put("db", nodeGatherIndicators);
                gatherHash.put("bufferhit", nodeGatherIndicators);
                gatherHash.put("planhit", nodeGatherIndicators);
                gatherHash.put("cursorhit", nodeGatherIndicators);
                gatherHash.put("cataloghit", nodeGatherIndicators);
                gatherHash.put("error", nodeGatherIndicators);
                gatherHash.put("cursor", nodeGatherIndicators);
                gatherHash.put("page", nodeGatherIndicators);
                gatherHash.put("wait", nodeGatherIndicators);
                gatherHash.put("connect", nodeGatherIndicators);
                gatherHash.put("cache", nodeGatherIndicators);
                gatherHash.put("sql", nodeGatherIndicators);
                gatherHash.put("scan", nodeGatherIndicators);
                
                Hashtable retValue = new Hashtable();
                retValue = dbdao.collectSQLServerMonitItemsDetail(serverIP, "", userName, password, gatherHash);
                /*
                 * nms_sqlserverpages ��nms_sqlserverconns ��nms_sqlserverlocks��nms_sqlservercaches��
                 * nms_sqlservermems��nms_sqlserversqls��nms_sqlserverscans��nms_sqlserverstatisticshash
                 */
                sqlserverdata.put("retValue", retValue);
                Hashtable sqlserverDataHash = new Hashtable();
                //��ȡSQLSERVER������Ϣ
                try{
                    sqlserverDataHash = dbdao.getSqlServerData(serverIP, userName, password, gatherHash);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (sqlserverDataHash == null)
                    sqlserverDataHash = new Hashtable();
                
                //����Ϣ
                Hashtable dbValue = new Hashtable();
                if(sqlserverDataHash.get("dbValue") != null){
                    dbValue = (Hashtable)sqlserverDataHash.get("dbValue");
                    sqlserverdata.put("dbValue", (Hashtable)sqlserverDataHash.get("dbValue"));//nms_sqlserverdbvalue
                    ShareData.setSqldbdata(serverIP, (Hashtable)sqlserverDataHash.get("dbValue"));
                }
                //ϵͳ��Ϣ
                if(sqlserverDataHash.get("sysValue") != null){
                    sqlserverdata.put("sysValue", (Hashtable)sqlserverDataHash.get("sysValue"));//nms_sqlserversysvalue
                }
                //����Ϣ
                if(sqlserverDataHash.get("lockinfo_v") != null){
                    sqlserverdata.put("lockinfo_v", (Vector)sqlserverDataHash.get("lockinfo_v"));//nms_sqlserverlockinfo_v
                }
                //������Ϣ
                if(sqlserverDataHash.get("info_v") != null){
                    sqlserverdata.put("info_v", (Vector)sqlserverDataHash.get("info_v"));//nms_sqlserverinfo_v
                }
                
                updateData(DBVo,sqlserverdata);
                saveSqlServerData(hex+":"+ DBVo.getAlias(), sqlserverdata);
            } else {
                try {
                    dbdao.updateNmsValueByUniquekeyAndTablenameAndKey("nms_sqlserverstatus", "serverip", hex+":"+DBVo.getAlias(), "status", "0");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dbdao.close();
                }
                
            }
            Calendar date = Calendar.getInstance();
            Pingcollectdata hostdata = null;
            hostdata = new Pingcollectdata();
            hostdata.setIpaddress(serverIP);
            hostdata.setCollecttime(date);
            hostdata.setCategory("SQLPing");
            hostdata.setEntity("Utilization");
            hostdata.setSubentity("ConnectUtilization");
            hostdata.setRestype("dynamic");
            hostdata.setUnit("%");
            if (sqlserverIsOK) {
                hostdata.setThevalue("100");
            } else {
                hostdata.setThevalue("0");
            }
            try {
                dbdao.createHostData(hostdata);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbdao.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * ���¸澯��Ϣ    HONGLI
	 * @param vo ���ݿ�ʵ��
	 * @param collectingData ���ݿ�ʵ���еĸ���������Ϣ
	 */
	public void updateData(Object vo , Object collectingData){
		if(collectingData == null || vo == null){
			return ;
		}
		DBVo sqlserver = (DBVo)vo;		
		Hashtable datahashtable = (Hashtable)collectingData;
		
		Hashtable sqlserverHashtable = (Hashtable)datahashtable.get("retValue");//�õ��ɼ�sqlserver���ݿ����Ϣ
		
		Hashtable memeryHashtable = (Hashtable)sqlserverHashtable.get("pages");//�õ��������ͳ����Ϣ
		
		Hashtable locksHashtable = (Hashtable)sqlserverHashtable.get("locks");//�õ�����ϸ��Ϣ
		
		Hashtable connsHashtable = (Hashtable)sqlserverHashtable.get("conns");//�õ����ݿ�ҳ����ͳ��
		
		Hashtable dbValue = (Hashtable)sqlserverHashtable.get("dbValue");
		
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(sqlserver.getId()), AlarmConstant.TYPE_DB, "sqlserver");//��ȡ�ɼ�ָ���б�
		CheckEventUtil checkEventUtil = new CheckEventUtil();
		for(int i = 0 ; i < list.size() ; i ++){
			AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
			if("1".equals(alarmIndicatorsNode.getEnabled())){
				String indicators = alarmIndicatorsNode.getName();
				String value = "";//value ��ָʵ�����ݿ��е�ֵ���� ������������    HONGLI
				String sindex = null;
				if("buffercache".equals(indicators)){
					value = (String)memeryHashtable.get("bufferCacheHitRatio");//key ��DBDao collectSQLServerMonitItemsDetail �����е�pages��keyһ��
				}else if("plancache".equals(indicators)){
					value = (String)memeryHashtable.get("planCacheHitRatio");
				}else if("cursormanager".equals(indicators)){
					value = (String)memeryHashtable.get("cursorManagerByTypeHitRatio");
				}else if("catalogMetadata".equals(indicators)){
					value = (String)memeryHashtable.get("catalogMetadataHitRatio");
				}else if("deadLocks".equals(indicators)){
					value = (String)locksHashtable.get("deadLocks");//key ��DBDao collectSQLServerMonitItemsDetail �����е�locks��keyһ��
				}else if("connections".equals(indicators)){
					value = (String)connsHashtable.get("connections");//key ��DBDao collectSQLServerMonitItemsDetail �����е�conns��keyһ��
				}else if("tablespace".equals(indicators)){
                    if (dbValue == null) {
                        continue;
                    }
				    Hashtable database = (Hashtable) dbValue.get("database");
                    Vector names = (Vector) dbValue.get("names");
                    if (database == null)
                        database = new Hashtable();
                    if (names != null && names.size() > 0) {
                        for (int k = 0; k < names.size(); k++) {
                            String dbname = (String) names.get(k);
                            if (database.get(dbname) != null) {
                                Hashtable db = (Hashtable) database.get(dbname);
                                String usedperc = (String) db.get("usedperc");
                                sindex = dbname;
                                value = usedperc;
                            }
                            checkEventUtil.checkEvent(sqlserver, alarmIndicatorsNode, value, sindex);
                        }
                        continue;
                    }
                }else if("logfile".equals(indicators)){
                    if (dbValue == null) {
                        continue;
                    }
                    Hashtable logfile = (Hashtable) dbValue.get("logfile");
                    Vector names = (Vector) dbValue.get("names");
                    if (logfile == null)
                        logfile = new Hashtable();
                    if (names != null && names.size() > 0) {
                        for (int k = 0; k < names.size(); k++) {
                            String dbname = (String) names.get(k);
                            if (logfile.get(dbname) != null) {
                                Hashtable db = (Hashtable) logfile.get(dbname);
                                String usedperc = (String) db.get("usedperc");
                                sindex = dbname;
                                value = usedperc;
                            }
                            checkEventUtil.checkEvent(sqlserver, alarmIndicatorsNode, value, sindex);
                        }
                        continue;
                    }
                } else {					
					continue;
				}
				checkEventUtil.checkEvent(sqlserver, alarmIndicatorsNode, value, sindex);
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
		SysLogger.info("##############��ʼ�����¼�############");
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

	public static void createSqldbSMS(DBVo dbmonitorlist, Sqldbconfig sqldbconfig) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = "";
		if (sqldbconfig.getLogflag() == 0) {
			// ���ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "�Ŀ�ռ䳬��" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		} else {
			// ��־�ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "����־����" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		}

		try {
			if (!sendeddata.containsKey(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				// String time1 = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("sqldb");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				// ���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
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
					// ����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					// String time1 = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("sqldb");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					// ���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					// �޸��Ѿ����͵Ķ��ż�¼
					sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
				} else {
					// ��д�����澯����
					// �������澯����д����
					/* modify by zhao 2010-5-7 */
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
					// ///
//					 SmscontentDao content=new SmscontentDao();
//					 content.createEventWithReasion("poll",dbmonitorlist.getId()+"",dbmonitorlist.getAlias()+
//					 "(" + dbmonitorlist.getIpAddress() + ")", errorcontent,
//					 2, "db", "sqldb", "��ռ䳬����ֵ");
					/* modify end --------------- */

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
	
	/**
	 * ���ɼ�����SqlServer���ݿ���Ϣ���浽���ݿ���
	 * @param serverip
	 * @param sqlserverdata
	 */
	public void saveSqlServerData(String serverip,Hashtable sqlserverdata){
		if(sqlserverdata == null || sqlserverdata.size() == 0){
			return ;
		}
		String status = String.valueOf(sqlserverdata.get("status"));//״̬��Ϣ
		Hashtable retValue = (Hashtable)sqlserverdata.get("retValue");
		Hashtable dbValue = (Hashtable)sqlserverdata.get("dbValue");//����Ϣ
		Hashtable sysValue = (Hashtable)sqlserverdata.get("sysValue");//ϵͳ��Ϣ
		Vector lockinfo_v = (Vector)sqlserverdata.get("lockinfo_v");//����Ϣ
		Vector info_v = (Vector)sqlserverdata.get("info_v");//������Ϣ
		DBDao dbDao = null;
		try {
			dbDao = new DBDao();
			if(sqlserverdata.containsKey("status")){
				try {
					dbDao.clearTableData("nms_sqlserverstatus", serverip);
					dbDao.addSqlserver_nmsstatus(serverip,status);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("pages")){
				Hashtable pages = (Hashtable)retValue.get("pages");
				dbDao.clearTableData("nms_sqlserverpages", serverip);
				try {
					dbDao.addSqlserver_nmspages(serverip,pages);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("conns")){
				Hashtable conns = (Hashtable)retValue.get("conns");
				dbDao.clearTableData("nms_sqlserverconns", serverip);
				try {
					dbDao.addSqlserver_nmsconns(serverip,conns);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("locks")){
				Hashtable locks = (Hashtable)retValue.get("locks");
				dbDao.clearTableData("nms_sqlserverlocks", serverip);
				try {
					dbDao.addSqlserver_nmslocks(serverip,locks);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("caches")){
				Hashtable caches = (Hashtable)retValue.get("caches");
				dbDao.clearTableData("nms_sqlservercaches", serverip);
				try {
					dbDao.addSqlserver_nmscaches(serverip,caches);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("mems")){
				Hashtable mems = (Hashtable)retValue.get("mems");
				dbDao.clearTableData("nms_sqlservermems", serverip);
				try {
					dbDao.addSqlserver_nmsmems(serverip,mems);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("sqls")){
				Hashtable sqls = (Hashtable)retValue.get("sqls");
				dbDao.clearTableData("nms_sqlserversqls", serverip);
				try {
					dbDao.addSqlserver_nmssqls(serverip,sqls);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("scans")){
				Hashtable scans = (Hashtable)retValue.get("scans");
				dbDao.clearTableData("nms_sqlserverscans", serverip);
				try {
					dbDao.addSqlserver_nmsscans(serverip,scans);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(retValue.containsKey("statisticsHash")){
				Hashtable statisticsHash = (Hashtable)retValue.get("statisticsHash");
				dbDao.clearTableData("nms_sqlserverstatisticshash", serverip);
				try {
					dbDao.addSqlserver_nmsstatisticsHash(serverip,statisticsHash);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(dbValue != null && dbValue.size() != 0){
				dbDao.clearTableData("nms_sqlserverdbvalue", serverip);
				try {
					Hashtable logfile = (Hashtable)dbValue.get("logfile");
					Hashtable database = (Hashtable)dbValue.get("database");
					Vector names = (Vector)dbValue.get("names");
					Iterator iter = logfile.entrySet().iterator(); 
					String label = "0";
					while (iter.hasNext()) { 
					    Map.Entry entry = (Map.Entry) iter.next(); 
					    String key = String.valueOf(entry.getKey()); 
					    Hashtable val = (Hashtable)entry.getValue(); 
					    dbDao.addSqlserver_nmsdbvalue(serverip,val,"",label);
					} 
					label = "1";
					iter = database.entrySet().iterator(); 
					while (iter.hasNext()) { 
					    Map.Entry entry = (Map.Entry) iter.next(); 
					    String key = String.valueOf(entry.getKey()); 
					    Hashtable val = (Hashtable)entry.getValue(); 
					    dbDao.addSqlserver_nmsdbvalue(serverip,val,"",label);
					} 
					label = "2";
					for(int i=0;i<names.size();i++){
						dbDao.addSqlserver_nmsdbvalue(serverip, null, String.valueOf(names.get(i)), label);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(sysValue != null && sysValue.size()>0){
				dbDao.clearTableData("nms_sqlserversysvalue", serverip);
				try {
					dbDao.addSqlserver_nmssysvalue(serverip,sysValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(lockinfo_v != null && lockinfo_v.size()>0){
				dbDao.clearTableData("nms_sqlserverlockinfo_v", serverip);
				try {
					Hashtable lockinfoHash = null;
					for(int i=0;i<lockinfo_v.size();i++){
						lockinfoHash = (Hashtable)lockinfo_v.get(i);
						dbDao.addSqlserver_nmslockinfo_v(serverip,lockinfoHash);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(info_v != null && info_v.size()>0){
				dbDao.clearTableData("nms_sqlserverinfo_v", serverip);
				try {
					Hashtable infoHash = null;
					for(int i=0;i<info_v.size();i++){
						infoHash = (Hashtable)info_v.get(i);
						dbDao.addSqlserver_nmsinfo_v(serverip,infoHash);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbDao.close();
		}
	}
	
	/**
	 * ���ָ��serverip��sqlserver������ʱ�������
	 * @param dbdao
	 * @param serverip
	 */
	private void clearSqlserverNmsTableData(DBDao dbdao, String serverip) {
		dbdao.clearTableData("nms_sqlservercaches", serverip);
		dbdao.clearTableData("nms_sqlserverconns", serverip);
		dbdao.clearTableData("nms_sqlserverdbvalue", serverip);
		dbdao.clearTableData("nms_sqlserverinfo_v", serverip);
		dbdao.clearTableData("nms_sqlserverlockinfo_v", serverip);
		dbdao.clearTableData("nms_sqlserverlocks", serverip);
		dbdao.clearTableData("nms_sqlservermems", serverip);
		dbdao.clearTableData("nms_sqlserverpages", serverip);
		dbdao.clearTableData("nms_sqlserverscans", serverip);
		dbdao.clearTableData("nms_sqlserversqls", serverip);
		dbdao.clearTableData("nms_sqlserverstatisticshash", serverip);
	}
}
