/**
 * <p>Description: app_db_node</p>
 * <p>Company:dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.model.WasConfig;
import com.afunms.application.wasmonitor.Was5cache;
import com.afunms.application.wasmonitor.Was5jdbc;
import com.afunms.application.wasmonitor.Was5jvminfo;
import com.afunms.application.wasmonitor.Was5session;
import com.afunms.application.wasmonitor.Was5system;
import com.afunms.application.wasmonitor.Was5thread;
import com.afunms.application.wasmonitor.Was5trans;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.event.dao.EventListDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Pingcollectdata;

public class WasConfigDao extends BaseDao implements DaoInterface {

	public WasConfigDao() {
		super("nms_wasconfig");
	}
	
	public boolean delete(String []ids){
		if(ids != null && ids.length>0){
			for(int i=0;i<ids.length;i++){
				delete(ids[i]);
			}
		}
		return true;
		//return super.delete(ids);
	}
	
	public boolean delete(String id)
	   {
		   boolean result = false;
		   try
		   {
			   WasConfig pvo = (WasConfig)findByID(id+"");
			   String ipstr = pvo.getIpaddress();
//				String ip1 ="",ip2="",ip3="",ip4="";
//				String[] ipdot = ipstr.split(".");	
//				String tempStr = "";
//				String allipstr = "";
//				if (ipstr.indexOf(".")>0){
//					ip1=ipstr.substring(0,ipstr.indexOf("."));
//					ip4=ipstr.substring(ipstr.lastIndexOf(".")+1,ipstr.length());			
//					tempStr = ipstr.substring(ipstr.indexOf(".")+1,ipstr.lastIndexOf("."));
//				}
//				ip2=tempStr.substring(0,tempStr.indexOf("."));
//				ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//				allipstr=ip1+ip2+ip3+ip4;
			    String allipstr = SysUtil.doip(ipstr);

				CreateTableManager ctable = new CreateTableManager();
	         
				//conn = new DBManager();
	  			ctable.deleteTable(conn,"wasping",allipstr,"wasping");//Ping
	  			ctable.deleteTable(conn,"waspinghour",allipstr,"waspinghour");//Ping
	  			ctable.deleteTable(conn,"waspingday",allipstr,"waspingday");//Ping   
	  			
	  			//�Է���������������CPUƽ��ʹ����
				ctable.deleteTable(conn,"wasrcpu",allipstr,"wasrcpu");//Ping
				ctable.deleteTable(conn,"wasrcpuhour",allipstr,"wasrcpuhour");//Ping
				ctable.deleteTable(conn,"wasrcpuday",allipstr,"wasrcpuday");//Ping
				
				//���ϴβ�ѯ������ƽ��CPUʹ����
				ctable.deleteTable(conn,"wasscpu",allipstr,"wasscpu");//Ping
				ctable.deleteTable(conn,"wasscpuhour",allipstr,"wasscpuhour");//Ping
				ctable.deleteTable(conn,"wasscpuday",allipstr,"wasscpuday");//Ping
				
				//����������
				ctable.deleteTable(conn,"wasrate",allipstr,"wasrate");//Ping
				ctable.deleteTable(conn,"wasratehour",allipstr,"wasratehour");//Ping
				ctable.deleteTable(conn,"wasrateday",allipstr,"wasrateday");//Ping
				
				//JVM�ڴ�������
				ctable.deleteTable(conn,"wasjvm",allipstr,"wasjvm");
				ctable.deleteTable(conn,"wasjvmhour",allipstr,"wasjvmhour");
				ctable.deleteTable(conn,"wasjvmday",allipstr,"wasjvmday");
				
				
			   //���ܱ�
					ctable.deleteTable(conn,"wassystem",allipstr,"wassystem");
					ctable.deleteTable(conn,"wassystemhour",allipstr,"wassystemhour");
					ctable.deleteTable(conn,"wassystemday",allipstr,"wassystemday");
					
					ctable.deleteTable(conn,"wasjdbc",allipstr,"wasjdbc");
					ctable.deleteTable(conn,"wasjdbchour",allipstr,"wasjdbchour");
					ctable.deleteTable(conn,"wasjdbcday",allipstr,"wasjdbcday");
					
					ctable.deleteTable(conn,"wassession",allipstr,"wassession");
					ctable.deleteTable(conn,"wassessionhour",allipstr,"wassessionhour");
					ctable.deleteTable(conn,"wassessionday",allipstr,"wassessionday");
					
					ctable.deleteTable(conn,"wasjvminfo",allipstr,"wasjvminfo");
					ctable.deleteTable(conn,"wasjvminfohour",allipstr,"wasjvminfohour");
					ctable.deleteTable(conn,"wasjvminfoday",allipstr,"wasjvminfoday");
					
					ctable.deleteTable(conn,"wascache",allipstr,"wascache");
					ctable.deleteTable(conn,"wascachehour",allipstr,"wascachehour");
					ctable.deleteTable(conn,"wascacheday",allipstr,"wascacheday");
					
					ctable.deleteTable(conn,"wasthread",allipstr,"wasthread");
					ctable.deleteTable(conn,"wasthreadhour",allipstr,"wasthreadhour");
					ctable.deleteTable(conn,"wasthreadday",allipstr,"wasthreadday");
					
					ctable.deleteTable(conn,"wastrans",allipstr,"wastrans");
					ctable.deleteTable(conn,"wastranshour",allipstr,"wastranshour");
					ctable.deleteTable(conn,"wastransday",allipstr,"wastransday");
			
			   conn.addBatch("delete from nms_wasconfig where id=" + id);
			   conn.executeBatch();
			   result = true;
			   try{
	              	//ͬʱɾ���¼�������������
					EventListDao eventdao = new EventListDao();
					eventdao.delete(Integer.parseInt(id), "wasserver");
	              }catch(Exception e){
	            	  e.printStackTrace();
	              }
		   }
		   catch(Exception e)
		   {
			   SysLogger.error("WasconfigDao.delete()",e); 
		   }
		   finally
		   {
			   conn.close();
		   }
		   
		   return result;
	   }
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		
		WasConfig vo=new WasConfig();
		
		try {
			vo.setId(rs.getInt("id"));
			vo.setName(rs.getString("name"));
			vo.setNodename(rs.getString("nodename"));
			vo.setServername(rs.getString("servername"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setPortnum(rs.getInt("portnum"));
			vo.setSendmobiles(rs.getString("sendmobiles"));
			vo.setMon_flag(rs.getInt("mon_flag"));
			vo.setNetid(rs.getString("netid"));
			vo.setSendemail(rs.getString("sendemail"));
			vo.setVersion(rs.getString("version"));
			vo.setSupperid(rs.getInt("supperid"));// snow add supperid at 2010-5-20
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	return vo;
	}

	public boolean save(BaseVo vo) {
		boolean flag = true;
		WasConfig vo1=(WasConfig)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_wasconfig(id,name,ipaddress,nodename,servername,portnum,sendmobiles,mon_flag,netid,sendemail,supperid,version) values(");
		sql.append(vo1.getId());
		sql.append(",'");
		sql.append(vo1.getName());
		sql.append("','");
		sql.append(vo1.getIpaddress());
		sql.append("','");
		sql.append(vo1.getNodename());
		sql.append("','");
		sql.append(vo1.getServername());
		sql.append("','");
		sql.append(vo1.getPortnum());
		sql.append("','");
		sql.append(vo1.getSendmobiles());
		sql.append("','");
		sql.append(vo1.getMon_flag());
		sql.append("','");
		sql.append(vo1.getNetid());
		sql.append("','");
		sql.append(vo1.getSendemail());
		sql.append("','");
		sql.append(vo1.getSupperid());
		sql.append("','");
		sql.append(vo1.getVersion());
		sql.append("')");
		try{
			saveOrUpdate(sql.toString());
			CreateTableManager ctable = new CreateTableManager();         
			//�������ɱ�
			String ip = vo1.getIpaddress();
//			String ip1 ="",ip2="",ip3="",ip4="";
//			String[] ipdot = ip.split(".");	
//			String tempStr = "";
//			String allipstr = "";
//			if (ip.indexOf(".")>0){
//				ip1=ip.substring(0,ip.indexOf("."));
//				ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//				tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//			}
//			ip2=tempStr.substring(0,tempStr.indexOf("."));
//			ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//			allipstr=ip1+ip2+ip3+ip4;
			String allipstr = SysUtil.doip(ip);
			
			conn = new DBManager();
			//������
			ctable.createTable(conn,"wasping",allipstr,"wasping");//Ping
			ctable.createTable(conn,"waspinghour",allipstr,"waspinghour");//Ping
			ctable.createTable(conn,"waspingday",allipstr,"waspingday");//Ping
			
			//�Է���������������CPUƽ��ʹ����
			ctable.createTable(conn,"wasrcpu",allipstr,"wasrcpu");//Ping
			ctable.createTable(conn,"wasrcpuhour",allipstr,"wasrcpuhour");//Ping
			ctable.createTable(conn,"wasrcpuday",allipstr,"wasrcpuday");//Ping
			
			//���ϴβ�ѯ������ƽ��CPUʹ����
			ctable.createTable(conn,"wasscpu",allipstr,"wasscpu");//Ping
			ctable.createTable(conn,"wasscpuhour",allipstr,"wasscpuhour");//Ping
			ctable.createTable(conn,"wasscpuday",allipstr,"wasscpuday");//Ping
			
			//����������
			ctable.createTable(conn,"wasrate",allipstr,"wasrate");//Ping
			ctable.createTable(conn,"wasratehour",allipstr,"wasratehour");//Ping
			ctable.createTable(conn,"wasrateday",allipstr,"wasrateday");//Ping
     
			//JVM�ڴ�������
			ctable.createTable(conn,"wasjvm",allipstr,"wasjvm");
			ctable.createTable(conn,"wasjvmhour",allipstr,"wasjvmhour");
			ctable.createTable(conn,"wasjvmday",allipstr,"wasjvmday");
			
			//�������ܱ�
			ctable.createWasTable(conn,"wassystem",allipstr);
			ctable.createWasTable(conn,"wassystemhour",allipstr);
			ctable.createWasTable(conn,"wassystemday",allipstr);
			
			ctable.createWasTable(conn,"wasjdbc",allipstr);
			ctable.createWasTable(conn,"wasjdbchour",allipstr);
			ctable.createWasTable(conn,"wasjdbcday",allipstr);
			
			ctable.createWasTable(conn,"wassession",allipstr);
			ctable.createWasTable(conn,"wassessionhour",allipstr);
			ctable.createWasTable(conn,"wassessionday",allipstr);
			
			ctable.createWasTable(conn,"wasjvminfo",allipstr);
			ctable.createWasTable(conn,"wasjvminfohour",allipstr);
			ctable.createWasTable(conn,"wasjvminfoday",allipstr);
			
			ctable.createWasTable(conn,"wascache",allipstr);
			ctable.createWasTable(conn,"wascachehour",allipstr);
			ctable.createWasTable(conn,"wascacheday",allipstr);
			
			ctable.createWasTable(conn,"wasthread",allipstr);
			ctable.createWasTable(conn,"wasthreadhour",allipstr);
			ctable.createWasTable(conn,"wasthreadday",allipstr);
			
			ctable.createWasTable(conn,"wastrans",allipstr);
			ctable.createWasTable(conn,"wastranshour",allipstr);
			ctable.createWasTable(conn,"wastransday",allipstr);
	
			
	   }catch(Exception e){
		   e.printStackTrace();
		   flag = false;
	   }finally{
			try{
				conn.executeBatch();
			}catch(Exception e){
				
			}
		   conn.close();
	   }
	   return flag;
	}

	public boolean update(BaseVo vo) {
		boolean flag = true;
		WasConfig vo1=(WasConfig)vo;
		WasConfig pvo = (WasConfig)findByID(vo1.getId()+"");
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_wasconfig set name='");
		sql.append(vo1.getName());
		sql.append("',ipaddress='");
		sql.append(vo1.getIpaddress());
		sql.append("',portnum='");
		sql.append(vo1.getPortnum());
		sql.append("',nodename='");
		sql.append(vo1.getNodename());
		sql.append("',servername='");
		sql.append(vo1.getServername());
		sql.append("',sendmobiles='");
		sql.append(vo1.getSendmobiles());
		sql.append("',mon_flag='");
		sql.append(vo1.getMon_flag());
		sql.append("',netid='");
		sql.append(vo1.getNetid());
		sql.append("',sendemail='");
		sql.append(vo1.getSendemail());
		sql.append("',supperid='");
		sql.append(vo1.getSupperid());
		sql.append("',version='");
		sql.append(vo1.getVersion());
		sql.append("' where id="+vo1.getId());
		SysLogger.info(sql.toString());
		try {		
			saveOrUpdate(sql.toString());	
		if (!vo1.getIpaddress().equals(pvo.getIpaddress())){
           	//�޸���IP
			//��IP��ַ�����ı�,�Ȱѱ�ɾ����Ȼ�������½���
				String ipstr = pvo.getIpaddress();
//				String ip1 ="",ip2="",ip3="",ip4="";
//				String[] ipdot = ipstr.split(".");	
//				String tempStr = "";
//				String allipstr = "";
//				if (ipstr.indexOf(".")>0){
//					ip1=ipstr.substring(0,ipstr.indexOf("."));
//					ip4=ipstr.substring(ipstr.lastIndexOf(".")+1,ipstr.length());			
//					tempStr = ipstr.substring(ipstr.indexOf(".")+1,ipstr.lastIndexOf("."));
//				}
//				ip2=tempStr.substring(0,tempStr.indexOf("."));
//				ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//				allipstr=ip1+ip2+ip3+ip4;
				String allipstr = SysUtil.doip(ipstr);

				CreateTableManager ctable = new CreateTableManager();
		         
				conn = new DBManager();
	  			ctable.deleteTable(conn,"wasping",allipstr,"wasping");//Ping
	  			ctable.deleteTable(conn,"waspinghour",allipstr,"waspinghour");//Ping
	  			ctable.deleteTable(conn,"waspingday",allipstr,"waspingday");//Ping   
	  			
	  			//�Է���������������CPUƽ��ʹ����
				ctable.deleteTable(conn,"wasrcpu",allipstr,"wasrcpu");//Ping
				ctable.deleteTable(conn,"wasrcpuhour",allipstr,"wasrcpuhour");//Ping
				ctable.deleteTable(conn,"wasrcpuday",allipstr,"wasrcpuday");//Ping
				
				//���ϴβ�ѯ������ƽ��CPUʹ����
				ctable.deleteTable(conn,"wasscpu",allipstr,"wasscpu");//Ping
				ctable.deleteTable(conn,"wasscpuhour",allipstr,"wasscpuhour");//Ping
				ctable.deleteTable(conn,"wasscpuday",allipstr,"wasscpuday");//Ping
				
				//����������
				ctable.deleteTable(conn,"wasrate",allipstr,"wasrate");//Ping
				ctable.deleteTable(conn,"wasratehour",allipstr,"wasratehour");//Ping
				ctable.deleteTable(conn,"wasrateday",allipstr,"wasrateday");//Ping
				
				//JVM�ڴ�������
				ctable.deleteTable(conn,"wasjvm",allipstr,"wasjvm");
				ctable.deleteTable(conn,"wasjvmhour",allipstr,"wasjvmhour");
				ctable.deleteTable(conn,"wasjvmday",allipstr,"wasjvmday");
				
				
			   //���ܱ�
					ctable.deleteTable(conn,"wassystem",allipstr,"wassystem");
					ctable.deleteTable(conn,"wassystemhour",allipstr,"wassystemhour");
					ctable.deleteTable(conn,"wassystemday",allipstr,"wassystemday");
					
					ctable.deleteTable(conn,"wasjdbc",allipstr,"wasjdbc");
					ctable.deleteTable(conn,"wasjdbchour",allipstr,"wasjdbchour");
					ctable.deleteTable(conn,"wasjdbcday",allipstr,"wasjdbcday");
					
					ctable.deleteTable(conn,"wassession",allipstr,"wassession");
					ctable.deleteTable(conn,"wassessionhour",allipstr,"wassessionhour");
					ctable.deleteTable(conn,"wassessionday",allipstr,"wassessionday");
					
					ctable.deleteTable(conn,"wasjvminfo",allipstr,"wasjvminfo");
					ctable.deleteTable(conn,"wasjvminfohour",allipstr,"wasjvminfohour");
					ctable.deleteTable(conn,"wasjvminfoday",allipstr,"wasjvminfoday");
					
					ctable.deleteTable(conn,"wascache",allipstr,"wascache");
					ctable.deleteTable(conn,"wascachehour",allipstr,"wascachehour");
					ctable.deleteTable(conn,"wascacheday",allipstr,"wascacheday");
					
					ctable.deleteTable(conn,"wasthread",allipstr,"wasthread");
					ctable.deleteTable(conn,"wasthreadhour",allipstr,"wasthreadhour");
					ctable.deleteTable(conn,"wasthreadday",allipstr,"wasthreadday");
					
					ctable.deleteTable(conn,"wastrans",allipstr,"wastrans");
					ctable.deleteTable(conn,"wastranshour",allipstr,"wastranshour");
					ctable.deleteTable(conn,"wastransday",allipstr,"wastransday");
			
			        conn.executeBatch();
			   try{
	              	//ͬʱɾ���¼�������������
					EventListDao eventdao = new EventListDao();
					eventdao.delete(vo1.getId(), "wasserver");
	              }catch(Exception e){
	            	  e.printStackTrace();
	              }
                 			               
           	
				//�������ɱ�
				String ip = vo1.getIpaddress();
//				ip1 ="";ip2="";ip3="";ip4="";
//				ipdot = ip.split(".");	
//				tempStr = "";
//				allipstr = "";
//				if (ip.indexOf(".")>0){
//					ip1=ip.substring(0,ip.indexOf("."));
//					ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//					tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//				}
//				ip2=tempStr.substring(0,tempStr.indexOf("."));
//				ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//				allipstr=ip1+ip2+ip3+ip4;
				allipstr = SysUtil.doip(ip);
				ctable = new CreateTableManager();
				//������
				ctable.createTable(conn,"wasping",allipstr,"wasping");//Ping
				ctable.createTable(conn,"waspinghour",allipstr,"waspinghour");//Ping
				ctable.createTable(conn,"waspingday",allipstr,"waspingday");//Ping
				
				//�Է���������������CPUƽ��ʹ����
				ctable.createTable(conn,"wasrcpu",allipstr,"wasrcpu");//Ping
				ctable.createTable(conn,"wasrcpuhour",allipstr,"wasrcpuhour");//Ping
				ctable.createTable(conn,"wasrcpuday",allipstr,"wasrcpuday");//Ping
				
				//���ϴβ�ѯ������ƽ��CPUʹ����
				ctable.createTable(conn,"wasscpu",allipstr,"wasscpu");//Ping
				ctable.createTable(conn,"wasscpuhour",allipstr,"wasscpuhour");//Ping
				ctable.createTable(conn,"wasscpuday",allipstr,"wasscpuday");//Ping
				
				//����������
				ctable.createTable(conn,"wasrate",allipstr,"wasrate");//Ping
				ctable.createTable(conn,"wasratehour",allipstr,"wasratehour");//Ping
				ctable.createTable(conn,"wasrateday",allipstr,"wasrateday");//Ping
	     
				//JVM�ڴ�������
				ctable.createTable(conn,"wasjvm",allipstr,"wasjvm");
				ctable.createTable(conn,"wasjvmhour",allipstr,"wasjvmhour");
				ctable.createTable(conn,"wasjvmday",allipstr,"wasjvmday");
				
				//�������ܱ�
				ctable.createWasTable(conn,"wassystem",allipstr);
				ctable.createWasTable(conn,"wassystemhour",allipstr);
				ctable.createWasTable(conn,"wassystemday",allipstr);
				
				ctable.createWasTable(conn,"wasjdbc",allipstr);
				ctable.createWasTable(conn,"wasjdbchour",allipstr);
				ctable.createWasTable(conn,"wasjdbcday",allipstr);
				
				ctable.createWasTable(conn,"wassession",allipstr);
				ctable.createWasTable(conn,"wassessionhour",allipstr);
				ctable.createWasTable(conn,"wassessionday",allipstr);
				
				ctable.createWasTable(conn,"wasjvminfo",allipstr);
				ctable.createWasTable(conn,"wasjvminfohour",allipstr);
				ctable.createWasTable(conn,"wasjvminfoday",allipstr);
				
				ctable.createWasTable(conn,"wascache",allipstr);
				ctable.createWasTable(conn,"wascachehour",allipstr);
				ctable.createWasTable(conn,"wascacheday",allipstr);
				
				ctable.createWasTable(conn,"wasthread",allipstr);
				ctable.createWasTable(conn,"wasthreadhour",allipstr);
				ctable.createWasTable(conn,"wasthreadday",allipstr);
				
				ctable.createWasTable(conn,"wastrans",allipstr);
				ctable.createWasTable(conn,"wastranshour",allipstr);
				ctable.createWasTable(conn,"wastransday",allipstr);
           }
		
	} catch (Exception e) {
			flag=false;
		e.printStackTrace();
	}finally{
		try{
			conn.executeBatch();
		}catch(Exception e){
			
		}
		conn.close();
	}
	return flag;

	}
	
	   public List getWasByBID(Vector bids){
		   List rlist = new ArrayList();
		   StringBuffer sql = new StringBuffer();
		   String wstr = "";
		   if(bids != null && bids.size()>0){
			   for(int i=0;i<bids.size();i++){
				   if(wstr.trim().length()==0){
					   wstr = wstr+" where ( netid like '%,"+bids.get(i)+",%' "; 
				   }else{
					   wstr = wstr+" or netid like '%,"+bids.get(i)+",%' ";
				   }
				   
			   }
			   wstr=wstr+")";
		   }
		   sql.append("select * from nms_wasconfig "+wstr);
		   //SysLogger.info(sql.toString());
		   return findByCriteria(sql.toString());
	   }
	   
	   
	   
	   //����Ping�õ������ݣ��ŵ���ʷ����
		public synchronized boolean createHostData(WasConfig wasconf,Pingcollectdata pingdata) {
			if (pingdata == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = pingdata.getIpaddress();				
					if (pingdata.getRestype().equals("dynamic")) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)pingdata.getCollecttime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = "wasping"+allipstr;
										
						String sql = "insert into "+tablename+"(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
								+"values('"+ip+"','"+pingdata.getRestype()+"','"+pingdata.getCategory()+"','"+pingdata.getEntity()+"','"
								+pingdata.getSubentity()+"','"+pingdata.getUnit()+"','"+pingdata.getChname()+"','"+pingdata.getBak()+"',"
								+pingdata.getCount()+",'"+pingdata.getThevalue()+"','"+time+"')";
						conn.executeUpdate(sql);
						//����PING�������
						//com.afunms.polling.node.Was node = (com.afunms.polling.node.Was)PollingEngine.getInstance().getWasByIP(pingdata.getIpaddress());
						if(pingdata.getSubentity().equalsIgnoreCase("ConnectUtilization")){
							//��ͨ�ʽ����ж�
							AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
							
							List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(wasconf.getId()), "middleware", "was");
							for(int k = 0 ; k < list.size() ; k ++){
								AlarmIndicatorsNode _alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(k);
								if("1".equals(_alarmIndicatorsNode.getEnabled())){
									if(_alarmIndicatorsNode.getName().equalsIgnoreCase("ping")){
										CheckEventUtil checkeventutil = new CheckEventUtil();
										//SysLogger.info(_alarmIndicatorsNode.getName()+"=====_alarmIndicatorsNode.getName()=========");
										checkeventutil.checkMiddlewareEvent(wasconf, _alarmIndicatorsNode, pingdata.getThevalue());
									}
								}
							}
							
						}
																										
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				
			}
			return true;
		}
		
		//����Ping�õ������ݣ��ŵ���ʷ����
		public synchronized boolean createHostData(Interfacecollectdata pingdata,String tablesub) {
			if (pingdata == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = pingdata.getIpaddress();				
					if (pingdata.getRestype().equals("dynamic")) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)pingdata.getCollecttime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
										
						String sql = "insert into "+tablename+"(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) "
								+"values('"+ip+"','"+pingdata.getRestype()+"','"+pingdata.getCategory()+"','"+pingdata.getEntity()+"','"
								+pingdata.getSubentity()+"','"+pingdata.getUnit()+"','"+pingdata.getChname()+"','"+pingdata.getBak()+"',"
								+pingdata.getCount()+",'"+pingdata.getThevalue()+"','"+time+"')";
						conn.executeUpdate(sql);
																										
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
	   
		
		public synchronized boolean createHostData(Was5system was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();				
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);				
						String sql = "insert into "+tablename+"(ipaddress,freeMemory,cpuUsageSinceServerStarted,cpuUsageSinceLastMeasurement,recordtime) "
								+"values('"+ip+"','"+was5.getFreeMemory()+"','"+was5.getCpuUsageSinceServerStarted()+"','"+was5.getCpuUsageSinceLastMeasurement()+"','"+time+"')";
						conn.executeUpdate(sql);
																										
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
	   
		public synchronized boolean createHostData(Was5trans was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();				
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);				
						String sql = "insert into "+tablename+"(ipaddress,activeCount,committedCount,rolledbackCount,globalTranTime,globalBegunCount,localBegunCount,localActiveCount,localTranTime,localTimeoutCount,localRolledbackCount,globalTimeoutCount,recordtime) "
								+"values('"+ip+"','"+was5.getActiveCount()+"','"+was5.getCommittedCount()+"','"+was5.getRolledbackCount()+"','"+was5.getGlobalTranTime()+"','"+was5.getGlobalBegunCount()+"','"+was5.getLocalBegunCount()+"','"+was5.getLocalActiveCount()+"','"+was5.getLocalTranTime()+"','"+was5.getLocalTimeoutCount()+"','"+was5.getLocalRolledbackCount()+"','"+was5.getGlobalTimeoutCount()+"','"+time+"')";
						conn.executeUpdate(sql);
																		
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		
		public synchronized boolean createHostData(Was5jdbc was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);
						String sql = "insert into "+tablename+"(ipaddress,createCount,closeCount,poolSize,freePoolSize,waitingThreadCount,percentUsed,useTime,waitTime,allocateCount,prepStmtCacheDiscardCount,jdbcTime,faultCount,recordtime) "
								+"values('"+ip+"','"+was5.getCreateCount()+"','"+was5.getCloseCount()+"','"+was5.getPoolSize()+"','"+was5.getFreePoolSize()+"','"+was5.getWaitingThreadCount()+"','"+was5.getPercentUsed()+"','"+was5.getUseTime()+"','"+was5.getWaitTime()+"','"+was5.getAllocateCount()+"','"+was5.getPrepStmtCacheDiscardCount()+"','"+was5.getJdbcTime()+"','"+was5.getFaultCount()+"','"+time+"')";
						conn.executeUpdate(sql);
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		
		public synchronized boolean createHostData(Was5session was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);				
						String sql = "insert into "+tablename+"(ipaddress,liveCount,createCount,invalidateCount,lifeTime,activeCount,timeoutInvalidationCount,recordtime) "
								+"values('"+ip+"','"+was5.getLiveCount()+"','"+was5.getCreateCount()+"','"+was5.getInvalidateCount()+"','"+was5.getLifeTime()+"','"+was5.getActiveCount()+"','"+was5.getInvalidateCount()+"','"+time+"')";
						conn.executeUpdate(sql);
					}				
					
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		
		public synchronized boolean createHostData(Was5jvminfo was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);				
						String sql = "insert into "+tablename+"(ipaddress,heapSize,freeMemory,usedMemory,upTime,memPer,recordtime) "
								+"values('"+ip+"','"+was5.getHeapSize()+"','"+was5.getFreeMemory()+"','"+was5.getUsedMemory()+"','"+was5.getUpTime()+"','"+was5.getMemPer()+"','"+time+"')";
						conn.executeUpdate(sql);
					}				
																														
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		public synchronized boolean createHostData(Was5cache was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);			
						String sql = "insert into "+tablename+"(ipaddress,inMemoryCacheCount,maxInMemoryCacheCount,timeoutInvalidationCount,recordtime)"
								+"values('"+ip+"','"+was5.getInMemoryCacheCount()+"','"+was5.getMaxInMemoryCacheCount()+"','"+was5.getTimeoutInvalidationCount()+"','"+time+"')";
						conn.executeUpdate(sql);
					}				
																								
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		public synchronized boolean createHostData(Was5thread was5,String tablesub) {
			if (was5 == null )
				return false;	
			try{			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Vector v = new Vector();
					String ip = was5.getIpaddress();
					if (ip!=null) {						
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ip.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
						Calendar tempCal = (Calendar)was5.getRecordtime();							
						Date cc = tempCal.getTime();
						String time = sdf.format(cc);
						String tablename = "";

						tablename = tablesub+allipstr;
						String delsql = "delete from "+tablename;	
						conn.executeUpdate(delsql);				
						String sql = "insert into "+tablename+"(ipaddress,createCount,destroyCount,poolSize,activeCount,recordtime)"
								+"values('"+ip+"','"+was5.getCreateCount()+"','"+was5.getDestroyCount()+"','"+was5.getPoolSize()+"','"+was5.getActiveCount()+"','"+time+"')";
						conn.executeUpdate(sql);
					}				
																								
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				conn.close();
				System.gc();
			}
			return true;
		}
		
		
	 //quzhi  
	   public List getWasByFlag(int flag){
		   List rlist = new ArrayList();
		   StringBuffer sql = new StringBuffer();
		   sql.append("select * from nms_wasconfig where mon_flag = "+flag);
		   return findByCriteria(sql.toString());
	   }
	   public BaseVo findByID(String id)
		  {
		     BaseVo vo = null;
		     try
		     {
		        rs = conn.executeQuery("select * from nms_wasconfig where id=" + id );
		        if(rs.next())
		           vo = loadFromRS(rs);
		     }
		     catch(Exception e)
		     {
		         SysLogger.error("WasConfigDao.findByID()",e);
		         vo = null;
		     }
		     finally
		     {
		        //conn.close();
		     }
		     return vo;
		  } 
	   public static void main(String args[]) throws SQLException{
			  WasConfigDao wasconf = new WasConfigDao();
			   wasconf.findByID("137");
			 
		   }
	   
	  
}