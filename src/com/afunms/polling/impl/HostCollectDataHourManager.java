/*
 * Created on 2005-4-8
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.afunms.application.dao.Urlmonitor_historyDao;
import com.afunms.application.dao.Urlmonitor_realtimeDao;
import com.afunms.application.manage.WebManager;
import com.afunms.common.base.BaseManager;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.DateInformation;
import com.afunms.common.util.ProjectProperties;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.discovery.Host;
import com.afunms.polling.api.I_HostCollectDataHour;
import com.afunms.system.dao.UserDao;
import com.afunms.system.manage.UserManager;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HostCollectDataHourManager extends BaseManager implements
		I_HostCollectDataHour {

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	/**
	 * 
	 */

	public HostCollectDataHourManager() {
		super();
	}

	public boolean schemeTask() {
		
		// �鲢һСʱǰ������
		//Connection con = null;
		//PreparedStatement stmt = null;
		// ��һ��������������ʱ�򣬲��鵵����
		String reporttime = ProjectProperties.getReporttime();
		Calendar now = Calendar.getInstance();
		int hourint = now.get(Calendar.HOUR_OF_DAY);
		/*
		List opList = new ArrayList();
		//�����ǰʱ����ͳ�Ʊ����ʱ�䣬���ͳ�Ʊ���
		if (Integer.parseInt(reporttime) == hourint) {
			// �豸���ܵı���ͳ��
			try {
				opList = userdao.getAllUser();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (opList != null && opList.size() > 0) {
				for (int i = 0; i < opList.size(); i++) {
					User op = (User) opList.get(i);
					hostreportAll(op);
					netreportAll(op);
//					webreportAll(op);
				}
			}
		}
		*/
		if (ShareData.getCount() == 1) {
			DBManager dbmanager = new DBManager();
			try {
				List list = new ArrayList();
				HostNodeDao hostdao = new HostNodeDao();
				try{
					list = hostdao.findByCondition("1","1");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					hostdao.close();
				}
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						HostNode equipment = (HostNode) list.get(i);
						// �������ɱ�
						String ip = equipment.getIpAddress();
						String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
						// String[] ipdot = ip.split(".");
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".") > 0) {
//							ip1 = ip.substring(0, ip.indexOf("."));
//							ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip
//									.length());
//							tempStr = ip.substring(ip.indexOf(".") + 1, ip
//									.lastIndexOf("."));
//						}
//						ip2 = tempStr.substring(0, tempStr.indexOf("."));
//						ip3 = tempStr.substring(tempStr.indexOf(".") + 1,
//								tempStr.length());
//						//����:10.10.152.28  ----->101015228
//						allipstr = ip1 + ip2 + ip3 + ip4;
						String allipstr = SysUtil.doip(ip);
						if ((equipment.getCategory()>0&&equipment.getCategory()<4) ||equipment.getCategory()==7) {
							// ���������豸��
							// Ping
							String sql = "insert into pinghour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) " +
											"SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) " +
											"FROM ping"	+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Memory
							sql = "insert into memoryhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) " +
											"SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) " +
											"FROM memory"	+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// CPU
							sql = "insert into cpuhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM cpu"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							// System.out.println("3 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// UtilHdx ��Сʱ�鵵��HOSTUTILHDXHOUR����
							sql = "insert into utilhdxhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM utilhdx"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// AllUtilHdx ��Сʱ�鵵��HOSTALLUTILHDXHOUR����
							sql = "insert into allutilhdxhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM allutilhdx"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// UtilHdxPerc ��Сʱ�鵵��HOSTUTILHDXPERCHOUR����
							sql = "insert into hdxperchour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM utilhdxperc"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							// DiscardsPerc
							sql = "insert into dcardperchour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM discardsperc"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// ErrorsPerc
							sql = "insert into errperchour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM errorsperc"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Packs
							sql = "insert into packshour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM packs"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// InPacks
							sql = "insert into inpackshour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM inpacks"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// InPacks
							sql = "insert into outpackshour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM outpacks"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Temper
							sql = "insert into temperhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM temper"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (equipment.getCategory() == 4) {
							// ����������
							// Ping
							String sql = "insert into pinghour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM ping"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							 //System.out.println("9 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Memory
							sql = "insert into memoryhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM memory"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							 //System.out.println("10 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// CPU
							sql = "insert into cpuhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM cpu"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							 //System.out.println("11 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							//disk  yangjun 
							sql = "insert into diskincrehour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,avg(hostcollectdata.thevalue) FROM diskincre"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit";
							 //System.out.println("9 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//disk  yangjun 
							sql = "insert into diskhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,avg(hostcollectdata.thevalue) FROM disk"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit";
							 //System.out.println("9 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							// Process
							sql = "insert into prohour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM pro"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							 //System.out.println("12 : "+sql);
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// UtilHdx ��Сʱ�鵵��HOSTUTILHDXHOUR����
							sql = "insert into utilhdxhour"
									+ allipstr
									+ "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue) SELECT hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count,avg(hostcollectdata.thevalue) FROM utilhdx"
									+ allipstr
									+ " hostcollectdata where (DATE_FORMAT(CURRENT_TIMESTAMP,'%H') - DATE_FORMAT(hostcollectdata.collecttime,'%H')=1) and (DATE_FORMAT(CURRENT_TIMESTAMP,'%Y-%m-%d')=DATE_FORMAT(hostcollectdata.collecttime,'%Y-%m-%d')) group by hostcollectdata.ipaddress,hostcollectdata.restype,hostcollectdata.category,hostcollectdata.entity,hostcollectdata.subentity,hostcollectdata.unit,hostcollectdata.chname,hostcollectdata.bak,hostcollectdata.count";
							try {
								dbmanager.addBatch(sql);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}
				try{
					dbmanager.executeBatch();
				}catch(Exception e){
					e.printStackTrace();
				}
			}catch(Exception ex){
				ex.printStackTrace();
				return false;
			}finally{
				dbmanager.close();
				dbmanager = null;
			}
		}else
			// ��һ������
			ShareData.setCount(1);
		return true;
	}

	private String dofloat(String num) {
		String snum = "0.0";
		if (num != null) {
			int inum = (int) (Float.parseFloat(num) * 100);
			snum = Double.toString(inum / 100.0);
		}
		return snum;
	}

	public String[] gethourHis(String ip, String category, String entity,
			String subentity, String starttime, String totime) throws Exception {
		String[] returnVal = new String[24];
		ResultSet rs = null;
		DBManager dbmanager = new DBManager();
		try {
//			String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//			String[] ipdot = ip.split(".");
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".") > 0) {
//				ip1 = ip.substring(0, ip.indexOf("."));
//				ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//				tempStr = ip
//						.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//			}
//			ip2 = tempStr.substring(0, tempStr.indexOf("."));
//			ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//			allipstr = ip1 + ip2 + ip3 + ip4;
			String allipstr = SysUtil.doip(ip);
			/*
			 * for (int i = 0; i < returnVal.length; i++) { returnVal[i] = "0"; }
			 */
			String consql = "";
			String entity1 = entity;
			String subentity1 = subentity;
			String tablename = "";
			if (entity.equalsIgnoreCase("utilization")) {
				entity1 = "Utilization";
			} else if (entity.equalsIgnoreCase("ResponseTime")) {
				entity1 = "ResponseTime";
			}
			if (subentity.equalsIgnoreCase("utilization")) {
				subentity1 = "Utilization";
			} else if (subentity.equalsIgnoreCase("ResponseTime")) {
				subentity1 = "ResponseTime";
			}
			if (category.equalsIgnoreCase("CPU")) {
				consql = " and a.category='" + category + "' ";
				tablename = "cpuhour";
			}
			if (category.equalsIgnoreCase("Ping")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "pinghour";
			}
			if (category.equalsIgnoreCase("Process")) {
				consql = " and a.category='" + category + "' and a.subentity='"
						+ subentity1 + "' ";
				tablename = "prohour";
			}
			String sql = "select a.thevalue,DATE_FORMAT(a.collecttime,'%Y-%m-%d %H:%i:%s') as colltime from "
					+ tablename
					+ allipstr
					+ " a "
					+ "where "
					+ " a.collecttime >=DATE_FORMAT('"
					+ starttime
					+ "','%Y-%m-%d %H:%i:%s') and a.collecttime<=DATE_FORMAT('"
					+ totime + "','%Y-%m-%d %H:%i:%s') order by a.collecttime";
			rs = dbmanager.executeQuery(sql);
			List list = new ArrayList();
			while (rs.next()) {
				Vector v = new Vector();
				String thevalue = rs.getString("thevalue");
				String collecttime = rs.getString("colltime");
				v.add(0, thevalue);
				v.add(1, collecttime);
				list.add(v);
			}
			rs.close();

			for (int i = 0; i < list.size(); i++) {
				Vector row = (Vector) list.get(i);
				String time = (String) row.get(1);
				returnVal[Integer.parseInt(time.substring(11, 13))] = dofloat((String) row
						.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}
		return returnVal;
	}

	public Hashtable gethourHis1(String ip, String category, String entity,
			String subentity, String starttime, String totime) throws Exception {
		String[] returnVal = new String[24];
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Hashtable returnV = new Hashtable();
		DBManager dbmanager = new DBManager();
		try {
			//			con = DataGate.getCon();
			// con=DataGate.getCon();
//			String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//			String[] ipdot = ip.split(".");
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".") > 0) {
//				ip1 = ip.substring(0, ip.indexOf("."));
//				ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//				tempStr = ip
//						.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//			}
//			ip2 = tempStr.substring(0, tempStr.indexOf("."));
//			ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//			allipstr = ip1 + ip2 + ip3 + ip4;
			String allipstr = SysUtil.doip(ip);
			/*
			 * for (int i = 0; i < returnVal.length; i++) { returnVal[i] = "0"; }
			 */
			String consql = "";
			String entity1 = entity;
			String subentity1 = subentity;
			String tablename = "";
			if (entity.equalsIgnoreCase("utilization")) {
				entity1 = "Utilization";
			} else if (entity.equalsIgnoreCase("ResponseTime")) {
				entity1 = "ResponseTime";
			}
			if (subentity.equalsIgnoreCase("utilization")) {
				subentity1 = "Utilization";
			} else if (subentity.equalsIgnoreCase("ResponseTime")) {
				subentity1 = "ResponseTime";
			}
			if (category.equalsIgnoreCase("CPU")) {
				consql = " and a.category='" + category + "' ";
				tablename = "cpuhour";
			}
			if (category.equalsIgnoreCase("Ping")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "pinghour";
			}
			if (category.equalsIgnoreCase("ORAPing")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "orapinghour";
			}
			if (category.equalsIgnoreCase("SQLPing")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "sqlpinghour";
			}
			if (category.equalsIgnoreCase("DB2Ping")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "db2pinghour";
			}
			if (category.equalsIgnoreCase("SYSPing")) {
				consql = " and a.category='" + category + "' and a.entity='"
						+ entity1 + "' ";
				tablename = "syspinghour";
			}

			if (category.equalsIgnoreCase("Process")) {
				consql = " and a.category='" + category + "' and a.subentity='"
						+ subentity1 + "' ";
				tablename = "prohour";
			}
			String sql = "select a.thevalue,DATE_FORMAT(a.collecttime,'%Y-%m-%d %H:%i:%s') as colltime from "
					+ tablename
					+ allipstr
					+ " a "
					+ "where "
					// + consql
					+ " a.collecttime >=DATE_FORMAT('"
					+ starttime
					+ "','%Y-%m-%d %H:%i:%s') and a.collecttime<=DATE_FORMAT('"
					+ totime
					+ "','%Y-%m-%d %H:%i:%s')"
					+ consql
					+ " order by a.collecttime";
			// + " and a.collecttime between '"+starttime+"' and '"+totime+"'"
			// + " order by a.collecttime ";
			// System.out.println("sql="+sql);
			//			stmt = con.prepareStatement(sql);
			rs = dbmanager.executeQuery(sql);
			List list = new ArrayList();
			double pingcon = 0;
			double allcpu = 0;
			double maxcpu = 0;
			double minping = 100;
			while (rs.next()) {
				Vector v = new Vector();
				String thevalue = rs.getString("thevalue");
				String collecttime = rs.getString("colltime");
				v.add(0, thevalue);
				v.add(1, collecttime);
				list.add(v);
				if ((category.equals("Ping") || category.equals("ORAPing") || category
						.equals("SQLPing"))
						&& subentity.equalsIgnoreCase("ConnectUtilization")) {
					pingcon = pingcon + getfloat(thevalue);
					if (minping > getfloat(thevalue))
						minping = getfloat(thevalue);

				} else {
					allcpu += getfloat(thevalue);
					if (maxcpu < getfloat(thevalue))
						maxcpu = getfloat(thevalue);
				}
			}
			rs.close();
			if ((category.equals("Ping") || category.equals("ORAPing")
					|| category.equals("SYSPing") || category.equals("SQLPing"))
					&& subentity.equalsIgnoreCase("ConnectUtilization")) {
				if (list != null && list.size() > 0) {
					returnV.put("avgpingcon", CEIString.round(pingcon
							/ list.size(), 2)
							+ "%");
				} else {
					returnV.put("avgpingcon", "0.0%");
				}
				returnV.put("minping", minping + "%");
			} else {
				if (list != null && list.size() > 0) {
					returnV.put("avgcpu", CEIString.round(allcpu / list.size(),
							2)
							+ "%");
				} else {
					returnV.put("avgcpu", "0.0%");
				}
				returnV.put("cpumax", maxcpu + "%");
			}
			returnV.put("list", list);
		} catch (Exception e) {
			//this.endTransaction(false);
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}
		return returnV;
	}

	public Hashtable getmultiHis(String ip, String category, String starttime,
			String totime) throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DBManager dbmanager = new DBManager();
		try {
			//			con = DataGate.getCon();
//			String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//			String[] ipdot = ip.split(".");
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".") > 0) {
//				ip1 = ip.substring(0, ip.indexOf("."));
//				ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//				tempStr = ip
//						.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//			}
//			ip2 = tempStr.substring(0, tempStr.indexOf("."));
//			ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//			allipstr = ip1 + ip2 + ip3 + ip4;
			String allipstr = SysUtil.doip(ip);
			String tablename = "";
			if (category.equals("Memory")) {
				tablename = "memoryhour";
			} else if (category.equals("Disk")) {
				tablename = "diskhour";
			}
			//			Session session = this.beginTransaction();
			String sql1 = "select distinct h.subentity from " + tablename
					+ allipstr + " h ";
			stmt = con.prepareStatement(sql1);
			rs = stmt.executeQuery();
			List list1 = new ArrayList();
			while (rs.next()) {
				Vector v = new Vector();
				String subentity = rs.getString("subentity");
				v.add(0, subentity);
				list1.add(v);
			}
			rs.close();
			stmt.close();
			if (list1.size() != 0) {
				int size = list1.size();
				String[] key = new String[list1.size()];
				Vector[] vector = new Vector[key.length];
				// String[][] value = new String[size][24];
				for (int i = 0; i < size; i++) {
					vector[i] = new Vector();
					Vector row = ((Vector) list1.get(i));
					key[i] = (String) row.get(0);
				}

				String sql = "";
				StringBuffer sb = new StringBuffer();
				sb
						.append("select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as colltime,h.subentity from "
								+ tablename
								+ allipstr
								+ " h where h.ipaddress='");
				sb.append(ip);
				sb.append("' and h.category='");
				sb.append(category);
				sb.append("' and h.collecttime >=DATE_FORMAT('");
				sb.append(starttime);
				sb
						.append("','%Y-%m-%d %H:%i:%s') and h.collecttime<=DATE_FORMAT('");
				sb.append(totime);
				sb.append("','%Y-%m-%d %H:%i:%s') order by h.collecttime");
				sql = sb.toString();
				List list2 = new ArrayList();
				//				stmt = con.prepareStatement(sql);
				rs = dbmanager.executeQuery(sql);
				while (rs.next()) {
					Vector v = new Vector();
					String thevalue = rs.getString("thevalue");
					String collecttime = rs.getString("colltime");
					String subentity = rs.getString("subentity");
					v.add(0, thevalue);
					v.add(1, collecttime);
					v.add(2, subentity);
					list2.add(v);
				}

				for (int k = 0; k < list2.size(); k++) {
					Vector obj = (Vector) list2.get(k);
					for (int i = 0; i < key.length; i++) {
						if (((String) obj.get(2)).equalsIgnoreCase(key[i])) {
							vector[i].add(obj);
							break;
						}
					}
				}
				String unit = "";

				for (int i = 0; i < size; i++) {
					// hash.put(key[i],value[i]);
					hash.put(key[i], vector[i]);
				}
				hash.put("key", key);
			}
		} catch (Exception e) {
			//this.endTransaction(false);
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}
		return hash;
	}

	public Hashtable getmultiHis(String ip, String category, String subentity,
			String[] bandkey, String[] bandch, String starttime, String totime)
			throws Exception {
		Hashtable hash = new Hashtable();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DBManager dbmanager = new DBManager();
		try {
			//			con = DataGate.getCon();
//			String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//			String[] ipdot = ip.split(".");
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".") > 0) {
//				ip1 = ip.substring(0, ip.indexOf("."));
//				ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//				tempStr = ip
//						.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//			}
//			ip2 = tempStr.substring(0, tempStr.indexOf("."));
//			ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//			allipstr = ip1 + ip2 + ip3 + ip4;
			String allipstr = SysUtil.doip(ip);
			String checkband = bandkey[0];
			String tablename = "";
			if (checkband.equals("InBandwidthUtilHdxPerc")) {
				tablename = "hdxperchour";
			} else if (checkband.equals("InBandwidthUtilHdx")) {
				tablename = "utilhdxhour";
			} else if (checkband.equals("InDiscardsPerc")) {
				tablename = "dcardperchour";
			} else if (checkband.equals("InErrorsPerc")) {
				tablename = "errperchour";
			} else if (checkband.equals("InCastPkts")) {
				tablename = "packshour";
			}
			StringBuffer sb = new StringBuffer();
			//			Session session = this.beginTransaction();
			int size = bandkey.length;
			// String[][] value = new String[size][24];
			/*
			 * for(int i=0; i<size; i++){ for(int j=0; j<24; j++){ value[i][j] =
			 * "0"; } }
			 */
			String sql2 = "";
			if (category.indexOf("all") != -1) {
				sb.append(" and(");
				for (int j = 0; j < bandkey.length; j++) {
					if (j != 0) {
						sb.append("or");
					}
					sb.append(" h.subentity='");
					sb.append(bandkey[j]);
					sb.append("' ");
				}
				sb.append(") ");
				sql2 = "select DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as colltime,h.thevalue,h.subentity from Hostcollectdatahour h where h.ipaddress='"
						+ ip
						+ "' and h.category='Interface'"
						+ sb.toString()
						+ " and h.collecttime >=DATE_FORMAT('"
						+ starttime
						+ "','%Y-%m-%d %H:%i:%s') and h.collecttime<=DATE_FORMAT('"
						+ totime
						+ "','%Y-%m-%d %H:%i:%s') order by h.collecttime";
			} else {
				sb.append(" and h.subentity='" + subentity + "' and (");
				for (int j = 0; j < size; j++) {
					if (j != 0) {
						sb.append("or");
					}
					sb.append(" h.entity='");
					sb.append(bandkey[j]);
					sb.append("' ");
				}
				sb.append(") ");
				sql2 = "select DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as colltime,h.thevalue,h.entity from "
						+ tablename
						+ allipstr
						+ " h where 1=1 "
						+ sb.toString()
						+ " and h.collecttime >=DATE_FORMAT('"
						+ starttime
						+ "','%Y-%m-%d %H:%i:%s') and h.collecttime<=DATE_FORMAT('"
						+ totime
						+ "','%Y-%m-%d %H:%i:%s') order by h.collecttime";
			}
			// Query query=session.createQuery(sql2);
			// List list2=new ArrayList();
			Vector v2 = new Vector();
			// System.out.println(sql2);
			//			stmt = con.prepareStatement(sql2);
			rs = dbmanager.executeQuery(sql2);
			while (rs.next()) {
				Vector v = new Vector();
				v.add(0, rs.getString("thevalue"));
				v.add(1, rs.getString("colltime"));
				v.add(2, rs.getString("entity"));
				// list2.add(v);
				v2.add(v);
			}
			rs.close();
			stmt.close();
			/*
			 * for(int k=0;k<list2.size();k++){ Vector row =
			 * (Vector)list2.get(k); for(int i=0; i<bandkey.length; i++){
			 * if(((String)row.get(2)).equals(bandkey[i])){ //SimpleDateFormat
			 * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Calendar
			 * tempCal = (Calendar)row[0]; //String time =
			 * sdf.format(tempCal.getTime()); String time = (String)row.get(1);
			 * value[i][Integer.parseInt(time.substring(11,13))] =
			 * dofloat((String)row.get(0)); } } }
			 */
			for (int i = 0; i < bandch.length; i++) {
				// hash.put(bandch[i],value[i]);
				hash.put(bandch[i], v2);
			}
			hash.put("key", bandch);

		} catch (Exception e) {
			//this.endTransaction(false);
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}
		return hash;
	}

	public Hashtable[] getMemory_month(String ip, String category,
			String starttime, String endtime) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Hashtable[] hash = new Hashtable[3];
		hash[0] = new Hashtable(); // ��ͼ��yֵ
		hash[1] = new Hashtable(); // �����ֵ
		hash[2] = new Hashtable(); // ��ƽ��ֵ
		DBManager dbmanager = new DBManager();
		try {
			//			con = DataGate.getCon();
			//			Session session = this.beginTransaction();
			// con=DataGate.getCon();
//			String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//			String[] ipdot = ip.split(".");
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".") > 0) {
//				ip1 = ip.substring(0, ip.indexOf("."));
//				ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//				tempStr = ip
//						.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//			}
//			ip2 = tempStr.substring(0, tempStr.indexOf("."));
//			ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//			allipstr = ip1 + ip2 + ip3 + ip4;
			String allipstr = SysUtil.doip(ip);
			String sql1 = "select max(h.thevalue) as value,h.subentity,avg(h.thevalue) as avgvalue from memoryhour"
					+ allipstr
					+ " h where "
					// + ip
					+ " h.category='"
					+ category
					+ "' and h.collecttime >= DATE_FORMAT('"
					+ starttime
					+ "','%Y-%m-%d %H:%i:%s') and h.collecttime <= DATE_FORMAT('"
					+ endtime
					+ "','%Y-%m-%d %H:%i:%s') group by h.subentity order by h.subentity";
			//			stmt = con.prepareStatement(sql1);
			rs = dbmanager.executeQuery(sql1);
			List list1 = new ArrayList();
			while (rs.next()) {
				Vector v = new Vector();
				String thevalue = rs.getString("value");
				String subentity = rs.getString("subentity");
				String avgvalue = rs.getString("avgvalue");
				v.add(0, thevalue);
				v.add(1, subentity);
				v.add(2, avgvalue);
				list1.add(v);
			}
			// List list1 = session.find(sql1);
			if (list1.size() != 0) {
				String[] key = new String[list1.size()];
				String[] max = new String[list1.size()];
				String[] avg = new String[list1.size()];
				Vector[] vector = new Vector[list1.size()];
				for (int i = 0; i < list1.size(); i++) {
					Vector row = (Vector) list1.get(i);
					key[i] = (String) row.get(1);
					max[i] = (String) row.get(0);
					avg[i] = (String) row.get(2);
					vector[i] = new Vector();
				}
				List list2 = new ArrayList();
				String sql = "";
				StringBuffer sb = new StringBuffer();
				sb
						.append("select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as colltime,h.unit,h.subentity from memoryhour"
								+ allipstr + " h where ");
				// sb.append(ip);
				sb.append(" h.category='");
				sb.append(category);
				sb.append("' and h.collecttime >= DATE_FORMAT('");
				sb.append(starttime);
				sb
						.append("','%Y-%m-%d %H:%i:%s') and h.collecttime <= DATE_FORMAT('");
				sb.append(endtime);
				sb.append("','%Y-%m-%d %H:%i:%s') order by h.collecttime");
				sql = sb.toString();
				// System.out.println(sql);
				//				stmt = con.prepareStatement(sql);
				rs = dbmanager.executeQuery(sql1);
				while (rs.next()) {
					Vector v = new Vector();
					String thevalue = rs.getString("thevalue");
					String collecttime = rs.getString("colltime");
					v.add(0, thevalue);
					v.add(1, collecttime);
					v.add(2, rs.getString("unit"));
					v.add(3, rs.getString("subentity"));
					list2.add(v);
				}
				rs.close();
				stmt.close();
				// List list2 = session.createQuery(sql).list();
				for (int i = 0; i < list2.size(); i++) {
					Vector obj = (Vector) list2.get(i);
					for (int j = 0; j < list1.size(); j++) {
						if (((String) obj.get(3)).equals(key[j])) {
							vector[j].add(obj);
						}
					}
				}
				String unit = "";
				if (list2.get(0) != null) {
					Vector obj = (Vector) list2.get(0);
					unit = (String) obj.get(2);
				}
				for (int i = 0; i < list1.size(); i++) {
					hash[0].put(key[i], vector[i]);
					hash[1].put(key[i], dofloat(max[i]) + unit);
					hash[2].put(key[i], dofloat(avg[i]) + unit);
				}
				hash[0].put("unit", unit);
				hash[0].put("key", key);
				if (category.equalsIgnoreCase("disk")) {
					hash[1].put("key", key);
				}
			}
		} catch (Exception e) {
			//this.endTransaction(false);
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			dbmanager.close();
		}
		return hash;
	}

	public Hashtable getmultiHis(String ip, String category, String subentity,
			String[] bandkey, String[] bandch, String starttime, String totime,
			String tablename) throws Exception {
		Hashtable hash = new Hashtable();
		/**
		try {
			StringBuffer sb = new StringBuffer();
			Session session = this.beginTransaction();
			int size = bandkey.length;
			String[][] value = new String[size][24];
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < 24; j++) {
					value[i][j] = "0";
				}
			}
			String sql2 = "";
			if (category.indexOf("all") != -1) {
				sb.append(" and(");
				for (int j = 0; j < bandkey.length; j++) {
					if (j != 0) {
						sb.append("or");
					}
					sb.append(" h.subentity='");
					sb.append(bandkey[j]);
					sb.append("' ");
				}
				sb.append(") ");
				sql2 = "select h.collecttime,h.thevalue,h.subentity from "
						+ tablename
						+ " h where h.ipaddress='"
						+ ip
						+ "' and h.category='Interface'"
						+ sb.toString()
						+ " and h.collecttime >=to_date('"
						+ starttime
						+ "','YYYY-MM-DD HH24:MI:SS') and h.collecttime<=to_date('"
						+ totime
						+ "','YYYY-MM-DD HH24:MI:SS') order by h.collecttime";
			} else {
				sb.append(" and h.subentity='" + subentity + "' and (");
				for (int j = 0; j < size; j++) {
					if (j != 0) {
						sb.append("or");
					}
					sb.append(" h.entity='");
					sb.append(bandkey[j]);
					sb.append("' ");
				}
				sb.append(") ");
				sql2 = "select h.collecttime,h.thevalue,h.entity from "
						+ tablename
						+ " h where h.ipaddress='"
						+ ip
						+ "' and h.category='Interface'"
						+ sb.toString()
						+ " and h.collecttime >=to_date('"
						+ starttime
						+ "','YYYY-MM-DD HH24:MI:SS') and h.collecttime<=to_date('"
						+ totime
						+ "','YYYY-MM-DD HH24:MI:SS') order by h.collecttime";
			}
			Query query = session.createQuery(sql2);
			
			List list2 = query.list();
			for (int k = 0; k < list2.size(); k++) {
				Object[] row = (Object[]) list2.get(k);
				for (int i = 0; i < bandkey.length; i++) {
					if (row[2].equals(bandkey[i])) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Calendar tempCal = (Calendar) row[0];
						String time = sdf.format(tempCal.getTime());
						value[i][Integer.parseInt(time.substring(11, 13))] = dofloat(row[1]
								.toString());
					}
				}
			}
			for (int i = 0; i < bandch.length; i++) {
				hash.put(bandch[i], value[i]);
			}
			hash.put("key", bandch);

			this.endTransaction(true);
		} catch (HibernateException e) {
			this.endTransaction(false);
			e.printStackTrace();
		} catch (Exception e) {
			this.endTransaction(false);
			e.printStackTrace();
		}*/
		return hash;
	}

	private double getfloat(String num) {
		double snum = 0.0;
		if (num != null) {
			if (num.indexOf(".") >= 0) {
				if (num.substring(num.indexOf(".") + 1).length() > 7) {
					String tempStr = num.substring(num.indexOf(".") + 1);
					num = num.substring(0, num.indexOf(".") + 1)
							+ tempStr.substring(0, 7);
				}
			}
			int inum = (int) (Float.parseFloat(num) * 100);
			snum = new Double(inum / 100.0).doubleValue();
		}
		return snum;
	}

	public void createDir(String commonPath) {
		File dir = new File(commonPath);
		if (!dir.exists()) {// ���SubĿ¼�Ƿ����
			dir.mkdir();
			// ����FLASH�ļ���ҳ�浽���Ŀ¼
			// copyFile("init.txt",commonPath,"");
			// copyFile("0004.swf",commonPath,"");
			// copyFile("hostframe.jsp",commonPath,"");
		}
	}

	/**
	 public boolean hostreportAll(User operator) {
	 Hashtable imgurlhash = new Hashtable();
	 Hashtable hash = new Hashtable();// "Cpu",--current
	 Hashtable memhash = new Hashtable();// mem--current
	 Hashtable diskhash = new Hashtable();
	 Hashtable memmaxhash = new Hashtable();// mem--max
	 Hashtable memavghash = new Hashtable();// mem--avg
	 Hashtable maxhash = new Hashtable();// "Cpu"--max
	 Hashtable maxping = new Hashtable();// Ping--max
	 String equipname = "";
	 String ip = "";
	 Hashtable pingdata = ShareData.getPingdata();
	 Hashtable sharedata = ShareData.getSharedata();
	 String[] time = { "", "", "" };
	 try {
	 if (operator == null) {
	 return false;
	 }
	 //���Ѽ�ص�������Ϣȫ�������
	 List hostMonitor = hostdao.loadHostByFlag(1);
	 Hashtable allreporthash = new Hashtable();
	 if (hostMonitor != null && hostMonitor.size() > 0) {
	 for (int i = 0; i < hostMonitor.size(); i++) {
	 Host equipment = (Host) hostMonitor.get(i);
	 ip = equipment.getIpAddress();
	 equipname = equipment.getSysName();
	 //ȥ��ip�е�.
	 String newip = doip(ip);
	 //�����һ���µ����һ��ʱ��
	 time = getBeforeTime();
	 String starttime = time[0];
	 String endtime = time[1];
	 // ��lastcollectdata��ȡ���µ�cpu�����ʣ��ڴ������ʣ���������������
	 String[] item = { "CPU" };
	 hash = hostlastmanager.getbyCategories(ip, item, starttime,
	 endtime);
	 memhash = hostlastmanager.getMemory_share(ip, "Memory",
	 starttime, endtime);
	 // ������Ϊû����ʷ����,���Բ��õ�ǰ�Ĵ������� 
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 String time1 = sdf.format(new Date());
	 String starttime1 = time1 + " 00:00:00";
	 String totime1 = time1 + " 23:59:59";
	 diskhash = hostlastmanager.getDisk_share(ip, "Disk",
	 starttime1, totime1);

	 // ��collectdata��ȡһ��ʱ���cpu�����ʣ��ڴ������ʵ���ʷ�����Ի�����ͼ��ͬʱȡ�����ֵ
	 Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
	 "Utilization", starttime, endtime);
	 Hashtable[] memoryhash = hostmanager.getMemory(ip,
	 "Memory", starttime, endtime);
	 // ��memory���ֵ
	 memmaxhash = memoryhash[1];
	 memavghash = memoryhash[2];
	 // cpu���ֵ
	 maxhash = new Hashtable();
	 String cpumax = "";
	 String avgcpu = "";
	 if (cpuhash.get("max") != null) {
	 cpumax = (String) cpuhash.get("max");
	 }
	 if (cpuhash.get("avgcpucon") != null) {
	 avgcpu = (String) cpuhash.get("avgcpucon");
	 }

	 maxhash.put("cpumax", cpumax);
	 maxhash.put("avgcpu", avgcpu);

	 Hashtable ConnectUtilizationhash = hostmanager.getCategory(
	 ip, "Ping", "ConnectUtilization", starttime,
	 endtime);
	 String pingconavg = "";
	 if (ConnectUtilizationhash.get("avgpingcon") != null)
	 pingconavg = (String) ConnectUtilizationhash
	 .get("avgpingcon");
	 String ConnectUtilizationmax = "";
	 maxping.put("avgpingcon", pingconavg);
	 if (ConnectUtilizationhash.get("max") != null) {
	 ConnectUtilizationmax = (String) ConnectUtilizationhash
	 .get("max");
	 }
	 maxping.put("pingmax", ConnectUtilizationmax);
	 ChartObject chartObject = new ChartObject();
	 chartObject.p_draw_line(ConnectUtilizationhash, "", newip
	 + "ConnectUtilization", 740, 120);
	 chartObject.p_draw_line(cpuhash, "", newip + "cpu", 750,
	 150);
	 chartObject.draw_column(diskhash, "", newip + "disk", 750,
	 150);
	 chartObject.p_drawchartMultiLine(memoryhash[0], "", newip
	 + "memory", 750, 150);
	 chartObject.draw_column(diskhash, "", newip + "disk", 750,
	 150);

	 Hashtable reporthash = new Hashtable();

	 Vector pdata = (Vector) pingdata.get(ip);
	 // ��ping�õ������ݼӽ�ȥ
	 if (pdata != null && pdata.size() > 0) {
	 for (int m = 0; m < pdata.size(); m++) {
	 Pingcollectdata hostdata = (Pingcollectdata) pdata
	 .get(m);
	 if (hostdata.getSubentity().equals(
	 "ConnectUtilization")) {
	 reporthash.put("time", hostdata
	 .getCollecttime());
	 reporthash.put("Ping", hostdata.getThevalue());
	 reporthash.put("ping", maxping);
	 }
	 }
	 }

	 // CPU
	 Hashtable hdata = (Hashtable) sharedata.get(ip);
	 Vector cpuVector = (Vector) hdata.get("cpu");
	 if (cpuVector != null && cpuVector.size() > 0) {
	 for (int si = 0; si < cpuVector.size(); si++) {
	 CPUcollectdata cpudata = (CPUcollectdata) cpuVector
	 .elementAt(si);
	 maxhash.put("cpu", cpudata.getThevalue());
	 reporthash.put("CPU", maxhash);
	 }
	 }
	 reporthash.put("Memory", memhash);
	 reporthash.put("Disk", diskhash);
	 reporthash.put("equipname", equipname);
	 reporthash.put("memmaxhash", memmaxhash);
	 reporthash.put("memavghash", memavghash);
	 reporthash.put("reporttime", time[2]);
	 allreporthash.put(ip, reporthash);
	 }

	 AbstractionReport report = new ExcelReport(
	 new IpResourceReport(), allreporthash);

	 java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
	 "yyyy-MM-dd");
	 String reportdate = timeFormatter.format(new java.util.Date());

	 java.text.SimpleDateFormat timeFormatter1 = new java.text.SimpleDateFormat(
	 "yyyy-MM");
	 String reportdate1 = timeFormatter1
	 .format(new java.util.Date());
	 createDir(CommonAppUtil.getAppName() + "/capability_report/");
	 createDir(CommonAppUtil.getAppName() + "/capability_report/"
	 + operator.getOid());
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1);
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1 + "\\"
	 + reportdate);
	 String filename = "\\capability_report\\" + operator.getOid()
	 + "\\" + reportdate1 + "\\" + reportdate
	 + "\\hostnms_report.xls";

	 File file = new File(CommonAppUtil.getAppName() + filename);
	 if (!file.exists()) {
	 file.createNewFile();
	 } else {
	 if (file.exists()) {
	 file.delete();
	 }
	 try {
	 file.createNewFile();
	 } catch (Exception exp) {
	 exp.printStackTrace();
	 }
	 }

	 report.createReport_hostall(filename);
	 // request.setAttribute("filename", report.getFileName());

	 }

	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return true;
	 }

	 public boolean netreportAll(Operator operator) {
	 Hashtable imgurlhash = new Hashtable();
	 Hashtable hash = new Hashtable();// "Cpu",--current
	 Hashtable memhash = new Hashtable();// mem--current
	 Hashtable diskhash = new Hashtable();
	 Hashtable memmaxhash = new Hashtable();// mem--max
	 Hashtable memavghash = new Hashtable();// mem--avg
	 Hashtable maxhash = new Hashtable();// "Cpu"--max
	 Hashtable maxping = new Hashtable();// Ping--max
	 String equipname = "";
	 String ip = "";
	 Hashtable pingdata = ShareData.getPingdata();
	 Hashtable sharedata = ShareData.getSharedata();
	 String[] time = { "", "", "" };
	 try {
	 if (operator == null) {
	 return false;
	 }
	 Vector vector = new Vector();
	 Netlocation netlocation = operator.getNetlocation();
	 List hostMonitor = equipmentManager.getByNetAndTypeAndMonitor(
	 operator.getNetlocation().getId(), "network",
	 new Integer(1));
	 Hashtable allreporthash = new Hashtable();
	 if (hostMonitor != null && hostMonitor.size() > 0) {
	 for (int i = 0; i < hostMonitor.size(); i++) {
	 Hashtable reporthash = new Hashtable();
	 Equipment equipment = (Equipment) hostMonitor.get(i);
	 ip = equipment.getIpaddress();
	 equipname = equipment.getEquipname();
	 //ȥ��ip���е�.
	 String newip = doip(ip);
	 //�����һ���µ����һ��
	 time = getBeforeTime();
	 String starttime = time[0];
	 String endtime = time[1];

	 // �������־ȡ���˿����¼�¼���б�
	 String orderflag = "index";

	 String[] netInterfaceItem = { "index", "ifname", "ifSpeed",
	 "ifOperStatus", "OutBandwidthUtilHdx",
	 "InBandwidthUtilHdx" };

	 vector = hostlastmanager.getInterface_share(ip,
	 netInterfaceItem, orderflag, starttime, endtime);
	 Hashtable portconfigHash = portconfigManager.getIpsHash(ip);
	 reporthash.put("portconfigHash", portconfigHash);
	 reporthash.put("netifVector", vector);
	 ChartObject chartObject = new ChartObject();
	 List reportports = portconfigManager.getByIpAndReportflag(
	 ip, new Integer(1));
	 reporthash.put("reportports", reportports);
	 if (reportports != null && reportports.size() > 0) {
	 // ��ʾ�˿ڵ�����ͼ��
	 I_HostCollectDataDay daymanager = new HostCollectDataDayManager();
	 String unit = "kb/s";
	 String title = "����24Сʱ�˿�����";
	 String[] banden3 = { "InBandwidthUtilHdx",
	 "OutBandwidthUtilHdx" };
	 String[] bandch3 = { "�������", "��������" };

	 for (int k = 0; k < reportports.size(); k++) {
	 Portconfig portconfig = (Portconfig) reportports
	 .get(k);
	 // ��������ʾ����
	 Hashtable value = new Hashtable();
	 value = daymanager.getmultiHisHdx(ip, "ifspeed",
	 portconfig.getPortindex() + "", banden3,
	 bandch3, time[2], time[2], "UtilHdx");
	 String reportname = "��" + portconfig.getPortindex()
	 + "(" + portconfig.getName() + ")�˿�����"
	 + time[2] + "�ձ���(��������ʾ)";
	 chartObject.p_drawchartMultiLineMonth(value,
	 reportname, newip
	 + portconfig.getPortindex()
	 + "ifspeed_day", 800, 200,
	 "UtilHdx");
	 String url1 = "../images/jfreechart/" + newip
	 + portconfig.getPortindex()
	 + "ifspeed_day.png";
	 }
	 }

	 Hashtable cpuhash = hostmanager.getCategory(ip, "CPU",
	 "Utilization", starttime, endtime);
	 String pingconavg = "";

	 maxhash = new Hashtable();
	 String cpumax = "";
	 String avgcpu = "";
	 if (cpuhash.get("max") != null) {
	 cpumax = (String) cpuhash.get("max");
	 }
	 if (cpuhash.get("avgcpucon") != null) {
	 avgcpu = (String) cpuhash.get("avgcpucon");
	 }

	 maxhash.put("cpumax", cpumax);
	 maxhash.put("avgcpu", avgcpu);
	 // ��ͼ

	 chartObject.p_draw_line(cpuhash, "", newip + "cpu", 740,
	 120);

	 Hashtable ConnectUtilizationhash = hostmanager.getCategory(
	 ip, "Ping", "ConnectUtilization", starttime,
	 endtime);
	 if (ConnectUtilizationhash.get("avgpingcon") != null)
	 pingconavg = (String) ConnectUtilizationhash
	 .get("avgpingcon");
	 String ConnectUtilizationmax = "";
	 maxping.put("avgpingcon", pingconavg);
	 if (ConnectUtilizationhash.get("max") != null) {
	 ConnectUtilizationmax = (String) ConnectUtilizationhash
	 .get("max");
	 }
	 maxping.put("pingmax", ConnectUtilizationmax);

	 chartObject.p_draw_line(ConnectUtilizationhash, "", newip
	 + "ConnectUtilization", 740, 120);

	 // ���ڴ��л�õ�ǰ�ĸ���IP��ص�IP-MAC��FDB����Ϣ
	 Hashtable _IpRouterHash = ShareData.getIprouterdata();
	 vector = (Vector) _IpRouterHash.get(ip);
	 if (vector != null)
	 reporthash.put("iprouterVector", vector);
	 Vector pdata = (Vector) pingdata.get(ip);
	 // ��ping�õ������ݼӽ�ȥ
	 if (pdata != null && pdata.size() > 0) {
	 for (int m = 0; m < pdata.size(); m++) {
	 Pingcollectdata hostdata = (Pingcollectdata) pdata
	 .get(m);
	 if (hostdata.getSubentity().equals(
	 "ConnectUtilization")) {
	 reporthash.put("time", hostdata
	 .getCollecttime());
	 reporthash.put("Ping", hostdata.getThevalue());
	 reporthash.put("ping", maxping);
	 }
	 }
	 }

	 // CPU
	 Hashtable hdata = (Hashtable) sharedata.get(ip);
	 Vector cpuVector = (Vector) hdata.get("cpu");
	 if (cpuVector != null && cpuVector.size() > 0) {
	 for (int si = 0; si < cpuVector.size(); si++) {
	 CPUcollectdata cpudata = (CPUcollectdata) cpuVector
	 .elementAt(si);
	 maxhash.put("cpu", cpudata.getThevalue());
	 reporthash.put("CPU", maxhash);
	 }
	 }
	 reporthash.put("equipname", equipname);
	 reporthash.put("memmaxhash", memmaxhash);
	 reporthash.put("memavghash", memavghash);
	 reporthash.put("reporttime", time[2]);
	 allreporthash.put(ip, reporthash);
	 }
	 AbstractionReport report = new ExcelReport(
	 new IpResourceReport(), allreporthash);
	 java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
	 "yyyy-MM-dd");
	 String reportdate = timeFormatter.format(new java.util.Date());
	 java.text.SimpleDateFormat timeFormatter1 = new java.text.SimpleDateFormat(
	 "yyyy-MM");
	 String reportdate1 = timeFormatter1
	 .format(new java.util.Date());
	 createDir(CommonAppUtil.getAppName() + "/capability_report/");
	 createDir(CommonAppUtil.getAppName() + "/capability_report/"
	 + operator.getOid());
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1);
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1 + "\\"
	 + reportdate);
	 String filename = "\\capability_report\\" + operator.getOid()
	 + "\\" + reportdate1 + "\\" + reportdate
	 + "\\networknms_report.xls";
	 File file = new File(CommonAppUtil.getAppName() + filename);
	 if (!file.exists()) {
	 file.createNewFile();
	 } else {
	 if (file.exists()) {
	 file.delete();
	 }
	 try {
	 file.createNewFile();
	 } catch (Exception exp) {
	 exp.printStackTrace();
	 }
	 }

	 report.createReport_networkall(filename);

	 }

	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return true;
	 }

	 //��WEB�������ɱ���
	
	 public boolean webreportAll(Operator operator) {
	 Hashtable imgurlhash = new Hashtable();
	 Hashtable hash = new Hashtable();// "Cpu",--current
	 Hashtable memhash = new Hashtable();// mem--current
	 Hashtable diskhash = new Hashtable();
	 Hashtable memmaxhash = new Hashtable();// mem--max
	 Hashtable memavghash = new Hashtable();// mem--avg
	 Hashtable maxhash = new Hashtable();// "Cpu"--max
	 Hashtable maxping = new Hashtable();// Ping--max
	 String webname = "";
	 String weburl = "";
	 Hashtable pingdata = ShareData.getPingdata();
	 Hashtable sharedata = ShareData.getSharedata();
	 String[] time = { "", "", "" };
	 try {
	 if (operator == null) {
	 return false;
	 }
	 Netlocation netlocation = operator.getNetlocation();
	 List webMonitor = urlconfManager.getListByNetlocation(operator
	 .getNetlocation().getId(), new Integer(1));
	 Hashtable allreporthash = new Hashtable();
	 if (webMonitor != null && webMonitor.size() > 0) {
	 for (int i = 0; i < webMonitor.size(); i++) {
	 URLConf urlconf = (URLConf) webMonitor.get(i);
	 weburl = urlconf.getUrl_str();
	 webname = urlconf.getUrl_name();
	 String conn_name = "";
	 String wave_name = "";
	 String delay_name = "";
	 conn_name = urlconf.getUrl_id() + "urlmonitor-conn";
	 wave_name = urlconf.getUrl_id() + "urlmonitor-rec";
	 delay_name = urlconf.getUrl_id() + "urlmonitor-delay";

	 time = getBeforeTime();
	 String starttime = time[0];
	 String endtime = time[1];

	 TimeSeries ss1 = new TimeSeries("", Minute.class);
	 TimeSeries ss2 = new TimeSeries("", Minute.class);

	 TimeSeries[] s = new TimeSeries[1];
	 TimeSeries[] s_ = new TimeSeries[1];
	 Vector wave_v = historyManager.getByUrlid(urlconf
	 .getUrl_id(), starttime, endtime, 0);

	 double maxcondelay = 0;
	 double allcondelay = 0;
	 double avgcondelay = 0;
	 double minconn = 1;
	 double allconn = 0;
	 double avgconn = 0;
	 for (int k = 0; k < wave_v.size(); k++) {
	 Hashtable ht = (Hashtable) wave_v.get(k);
	 double conn = Double.parseDouble(ht.get("conn")
	 .toString());
	 if (minconn > conn)
	 minconn = conn;
	 allconn += conn;
	 double fresh = Double.parseDouble(ht.get("refresh")
	 .toString());
	 double condelay = Double.parseDouble(ht.get("condelay")
	 .toString());
	 if (maxcondelay < condelay)
	 maxcondelay = condelay;
	 allcondelay += condelay;
	 String _time = ht.get("mon_time").toString();
	 ss1.addOrUpdate(new Minute(sdf1.parse(_time)), conn);
	 ss2
	 .addOrUpdate(new Minute(sdf1.parse(_time)),
	 condelay);
	 }
	 if (wave_v != null && wave_v.size() > 0) {
	 avgcondelay = CEIString.round(allcondelay
	 / wave_v.size(), 2);
	 avgconn = CEIString.round(allconn / wave_v.size(), 2);
	 }
	 s[0] = ss1;
	 s_[0] = ss2;
	 ChartGraph cg = new ChartGraph();
	 cg.timewave(s, "ʱ��", "��ͨ", "", wave_name, 600, 120);
	 cg.timewave(s_, "ʱ��", "ʱ��(ms)", "", delay_name, 600, 120);

	 // �Ƿ���ͨ
	 String conn[] = new String[2];
	 conn = historyManager.getAvailability(urlconf.getUrl_id(),
	 starttime, endtime, "is_canconnected");
	 String[] key1 = { "��ͨ", "δ��ͨ" };
	 drawPiechart(key1, conn, "", conn_name);

	 Hashtable reporthash = new Hashtable();
	 reporthash.put("urlname", urlconf.getUrl_name());
	 reporthash.put("urlstr", urlconf.getUrl_str());
	 reporthash.put("wave_name", wave_name);
	 reporthash.put("delay_name", delay_name);
	 reporthash.put("conn_name", conn_name);

	 reporthash.put("avgconn", avgconn + "");
	 reporthash.put("minconn", minconn + "");
	 reporthash.put("maxcondelay", maxcondelay + "");
	 reporthash.put("avgcondelay", avgcondelay + "");

	 reporthash.put("reporttime", time[2]);
	 allreporthash.put(urlconf.getUrl_id() + "", reporthash);
	 }

	 AbstractionReport report = new ExcelReport(
	 new IpResourceReport(), allreporthash);

	 java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
	 "yyyy-MM-dd");
	 String reportdate = timeFormatter.format(new java.util.Date());
	 java.text.SimpleDateFormat timeFormatter1 = new java.text.SimpleDateFormat(
	 "yyyy-MM");
	 String reportdate1 = timeFormatter1
	 .format(new java.util.Date());
	 createDir(CommonAppUtil.getAppName() + "/capability_report/");
	 createDir(CommonAppUtil.getAppName() + "/capability_report/"
	 + operator.getOid());
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1);
	 createDir(CommonAppUtil.getAppName() + "\\capability_report\\"
	 + operator.getOid() + "\\" + reportdate1 + "\\"
	 + reportdate);
	 String filename = "\\capability_report\\" + operator.getOid()
	 + "\\" + reportdate1 + "\\" + reportdate
	 + "\\webnms_report.xls";
	 File file = new File(CommonAppUtil.getAppName() + filename);
	 if (!file.exists()) {
	 file.createNewFile();
	 } else {
	 if (file.exists()) {
	 file.delete();
	 }
	 try {
	 file.createNewFile();
	 } catch (Exception exp) {
	 exp.printStackTrace();
	 }
	 }
	 report.createReport_weball(filename);
	 // request.setAttribute("filename", report.getFileName());

	 }

	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return true;
	 }
	 */
	private String doip(String ip) {
//		String newip = "";
//		for (int i = 0; i < 3; i++) {
//			int p = ip.indexOf(".");
//			newip += ip.substring(0, p);
//			ip = ip.substring(p + 1);
//		}
//		newip += ip;
		// System.out.println("newip="+newip);
		String allipstr = SysUtil.doip(ip);
		return allipstr;
	}

	private String[] getBeforeTime() {
		String[] returnStr = { "", "", "" };
		//String[] returnStr = new String[2]; 
		Calendar now = Calendar.getInstance();
		int hourint = now.get(Calendar.HOUR_OF_DAY);
		int dayint = now.get(Calendar.DAY_OF_MONTH);
		int monthint = now.get(Calendar.MONTH);
		int yearint = now.get(Calendar.YEAR);

		String report_starttime = "";
		String report_totime = "";
		DateInformation di = new DateInformation();
		String reporttime = "";
		if (dayint == 1) {
			//������ϸ��µ����һ��
			if (monthint == 0) {
				//�����ȥ�����һ��
				yearint = yearint - 1;
				report_starttime = di.getLastDayOfMonth(yearint + "", 12 + "")
						+ " 00:00:00";
				report_totime = di.getLastDayOfMonth(yearint + "", 12 + "")
						+ " 23:59:59";
				reporttime = di.getLastDayOfMonth(yearint + "", 12 + "");
			} else {
				report_starttime = di.getLastDayOfMonth(yearint + "", monthint
						+ "")
						+ " 00:00:00";
				report_totime = di.getLastDayOfMonth(yearint + "", monthint
						+ "")
						+ " 23:59:59";
				reporttime = di.getLastDayOfMonth(yearint + "", monthint + "");
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			now.set(yearint, monthint, dayint - 1);
			String time1 = sdf.format(now.getTime());
			reporttime = time1;
			report_starttime = time1 + " 00:00:00";
			report_totime = time1 + " 23:59:59";
		}
		returnStr[0] = report_starttime;
		returnStr[1] = report_totime;
		returnStr[2] = reporttime;
		return returnStr;
	}

	private void drawPiechart(String[] keys, String[] values, String chname,
			String enname) {
		ChartGraph cg = new ChartGraph();
		DefaultPieDataset piedata = new DefaultPieDataset();
		for (int i = 0; i < keys.length; i++) {
			piedata.setValue(keys[i], new Double(values[i]).doubleValue());
		}
		cg.pie(chname, piedata, enname, 300, 120);
	}

	public boolean netreportAll(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hostreportAll(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
