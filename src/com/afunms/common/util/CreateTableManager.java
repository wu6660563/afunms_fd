/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.afunms.polling.PollingEngine;

import net.sf.hibernate.HibernateException;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CreateTableManager {
		/*
		protected DBManager conn;
		
		   public CreateTableManager()
		   {
			   conn = new DBManager();
		   }
		   public void close(){
			   conn.close();
		   }
		*/
		 public void createTable(DBManager conn,String tablename,String ipstr,String tablestr){
		 	//Connection con = null;
		 	PreparedStatement stmt = null;
		 	try {	 		
				createRootTable(conn,tablename,ipstr);
				//create index
				/*
				createIndex(con,"I_"+tablestr,ipstr,"CA",tablename,"CATEGORY");
				createIndex(con,"I_"+tablestr,ipstr,"CT",tablename,"COLLECTTIME");
				createIndex(con,"I_"+tablestr,ipstr,"EN",tablename,"ENTITY");
				createIndex(con,"I_"+tablestr,ipstr,"IP",tablename,"IPADDRESS");
				createIndex(con,"I_"+tablestr,ipstr,"SEN",tablename,"SUBENTITY");
				createIndex(con,"I_"+tablestr,ipstr,"VAL",tablename,"THEVALUE");	
				*/
				//create sequence
				//createSeq(con,tablestr,ipstr);
				//create trigger
				//createTriger(con,tablestr,ipstr,tablename);		 		
			}catch(Exception ex){	
				//conn.rollback();
			}finally{
				//conn.close();
			}			
		}
		 
		 public void createInformixLogTable(DBManager conn,String tablename,String ipstr,String tablestr){
			 	//Connection con = null;
			 	PreparedStatement stmt = null;
			 	try {	 		
					createInformixLogRootTable(conn,tablename,ipstr);
					//create index
					/*
					createIndex(con,"I_"+tablestr,ipstr,"CA",tablename,"CATEGORY");
					createIndex(con,"I_"+tablestr,ipstr,"CT",tablename,"COLLECTTIME");
					createIndex(con,"I_"+tablestr,ipstr,"EN",tablename,"ENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"IP",tablename,"IPADDRESS");
					createIndex(con,"I_"+tablestr,ipstr,"SEN",tablename,"SUBENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"VAL",tablename,"THEVALUE");	
					*/
					//create sequence
					//createSeq(con,tablestr,ipstr);
					//create trigger
					//createTriger(con,tablestr,ipstr,tablename);		 		
				}catch(Exception ex){	
					//conn.rollback();
				}finally{
					//conn.close();
				}			
			}
		 
		 public void createBNodeTable(DBManager conn,String tablename,String ipstr,String tablestr){
			 	//Connection con = null;
			 	PreparedStatement stmt = null;
			 	try {	 		
					createBNodeRootTable(conn,tablename,ipstr);
					//create index
					/*
					createIndex(con,"I_"+tablestr,ipstr,"CA",tablename,"CATEGORY");
					createIndex(con,"I_"+tablestr,ipstr,"CT",tablename,"COLLECTTIME");
					createIndex(con,"I_"+tablestr,ipstr,"EN",tablename,"ENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"IP",tablename,"IPADDRESS");
					createIndex(con,"I_"+tablestr,ipstr,"SEN",tablename,"SUBENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"VAL",tablename,"THEVALUE");	
					*/
					//create sequence
					//createSeq(con,tablestr,ipstr);
					//create trigger
					//createTriger(con,tablestr,ipstr,tablename);		 		
				}catch(Exception ex){	
					conn.rollback();
				}finally{
					//conn.close();
				}			
			}
		 
		 public void createGrapesTable(DBManager conn,String tablename,String ipstr){
			 	//Connection con = null;
			 	PreparedStatement stmt = null;
			 	try {	 		
					createRootTable(conn,tablename,ipstr);	 		
				}catch(Exception ex){	
					conn.rollback();
				}finally{
					//conn.close();
				}			
			}
		 
		 public void createSyslogTable(DBManager conn,String tablename,String ipstr,String tablestr){
			 	Connection con = null;
			 	PreparedStatement stmt = null;
			 	try {
			 		//con=DataGate.getCon();		 		
			 		createSyslogRootTable(conn,tablename,ipstr);
					//create index
			 		/*
					createIndex(con,"I_"+tablestr,ipstr,"CA",tablename,"CATEGORY");
					createIndex(con,"I_"+tablestr,ipstr,"CT",tablename,"COLLECTTIME");
					createIndex(con,"I_"+tablestr,ipstr,"EN",tablename,"ENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"IP",tablename,"IPADDRESS");
					createIndex(con,"I_"+tablestr,ipstr,"SEN",tablename,"SUBENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"VAL",tablename,"THEVALUE");	
					*/
					//create sequence
					//createSeq(con,tablestr,ipstr);
					//create trigger
					//createTriger(con,tablestr,ipstr,tablename);		 		
				}catch(Exception ex){	
					ex.printStackTrace();
				}finally{
				}			
			}	
		 
		 public void deleteTable(DBManager conn,String tablename,String ipstr,String tablestr){
		 	try {
				dropRootTable(conn,tablename,ipstr);
			}catch(Exception ex){	
			}finally{
			}			
		}
		 
		 public void createSyslogRootTable(DBManager conn,String tablename,String ipstr){
			 	PreparedStatement stmt = null;
			 	String sql = "";
			 	//if (tablename.indexOf("hour")>=0){
			 		//创建SYSLOG表格
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),hostname VARCHAR(20),message VARCHAR(2500),"
				 	+"facility bigint(10),priority bigint(10),priorityName VARCHAR(100),facilityName VARCHAR(60),"
				 	+"processId bigint(10),processName VARCHAR(100),processIdStr VARCHAR(30),"
				 	+"recordtime timestamp,username VARCHAR(100),eventid bigint(10), PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
				 	System.out.println(sql);		 	
				 	try{
				 		conn.executeUpdate(sql);
				 	}catch(Exception e){
				 		e.printStackTrace();
				 		try{		
				 			conn.rollback();		 			
				 		}catch(Exception ex){
 			
				 		}
 		
				 	}finally{
				 	}		 	
			 }		 
		 
		 
		 public void createRootTable(DBManager conn,String tablename,String ipstr){
		 	//PreparedStatement stmt = null;
		 	String sql = "";
		 	if (tablename.indexOf("hour")>=0){
		 		//创建小时表格
			 	sql="create table "+tablename+ipstr
			 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
			 	+"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
			 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
		 	}else if (tablename.indexOf("day")>=0){
		 		//创建天的表
			 	sql="create table "+tablename+ipstr
			 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
			 	+"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
			 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	
		 	}else if (tablename.indexOf("software")>=0){
			 		sql="CREATE TABLE "+tablename+ipstr
			 		+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),name varchar(200),swid varchar(100),"
			 		+"type varchar(100),insdate varchar(100),PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
		 	}else if (tablename.indexOf("interface")>=0){
		 	    sql = "CREATE TABLE "+tablename+ipstr
                    +"(`nodeid` varchar(10) DEFAULT NULL,`ip` varchar(20) DEFAULT NULL,`type` varchar(30) DEFAULT NULL,`subtype` varchar(30) DEFAULT NULL,`entity` varchar(30) DEFAULT NULL,`subentity` varchar(30) DEFAULT NULL,`sindex` varchar(30) DEFAULT NULL,`thevalue` varchar(300) DEFAULT NULL,`chname` varchar(50) DEFAULT NULL,`restype` varchar(20) DEFAULT NULL,`collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,`unit` varchar(10) DEFAULT NULL,`bak` varchar(20) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
 	        }else if (tablename.indexOf("process")>=0){
                sql = "CREATE TABLE "+tablename+ipstr
                    +"(`nodeid` varchar(10) DEFAULT NULL,`ip` varchar(20) DEFAULT NULL,`type` varchar(30) DEFAULT NULL,`subtype` varchar(30) DEFAULT NULL,`entity` varchar(30) DEFAULT NULL,`subentity` varchar(30) DEFAULT NULL,`sindex` varchar(30) DEFAULT NULL,`thevalue` varchar(500) DEFAULT NULL,`chname` varchar(50) DEFAULT NULL,`restype` varchar(20) DEFAULT NULL,`collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,`unit` varchar(10) DEFAULT NULL,`bak` varchar(20) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=gb2312";
 	        }else{
		 		//创建按分钟采集数据的表
			 	sql="create table "+tablename+ipstr
			 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
			 	+"THEVALUE    VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
			 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
		 	}
		 	SysLogger.info(sql);		 	
		 	try{
		 		conn.addBatch(sql);
		 	}catch(Exception e){
		 		try{		
		 			//conn.rollback();		 			
		 		}catch(Exception ex){
		 			
		 		}
		 		
		 	}finally{
		 	}		 	
		 }	
		 
		 public void createBNodeRootTable(DBManager conn,String tablename,String ipstr){
			 	//PreparedStatement stmt = null;
			 	String sql = "";
			 	if (tablename.indexOf("hour")>=0){
			 		//创建小时表格
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,THEVALUE VARCHAR(255),RESPONSETIME VARCHAR(100),COLLECTTIME timestamp, "
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
			 	}else if (tablename.indexOf("day")>=0){
			 		//创建天的表
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,THEVALUE VARCHAR(255),RESPONSETIME VARCHAR(100),COLLECTTIME timestamp, "
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	}else{
			 		//创建按分钟采集数据的表
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,THEVALUE VARCHAR(255),RESPONSETIME VARCHAR(100),COLLECTTIME timestamp, "
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	}
			 	SysLogger.info(sql);		 	
			 	try{
			 		conn.executeUpdate(sql);
			 	}catch(Exception e){
			 		try{		
			 			conn.rollback();		 			
			 		}catch(Exception ex){
			 			
			 		}
			 		
			 	}finally{
			 	}		 	
			 }
		 
		 public void createGrapesRootTable(DBManager conn,String tablename,String ipstr){
			 	PreparedStatement stmt = null;
			 	String sql = "";
			 	if (tablename.indexOf("hour")>=0){
			 		//创建小时表格
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
				 	+"THEVALUE VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
			 	}else if (tablename.indexOf("day")>=0){
			 		//创建天的表
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
				 	+"THEVALUE VARCHAR(255),COLLECTTIME timestamp ,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
			 	}else{
			 		//创建按分钟采集数据的表
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),RESTYPE VARCHAR(20),CATEGORY VARCHAR(50),ENTITY VARCHAR(100),SUBENTITY VARCHAR(60),"
				 	+"THEVALUE    VARCHAR(255),COLLECTTIME timestamp,UNIT VARCHAR(30),COUNT bigint(20),BAK VARCHAR(100),CHNAME VARCHAR(100),"
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
			 	}
			 	SysLogger.info(sql);		 	
			 	try{
			 		conn.executeUpdate(sql);
			 	}catch(Exception e){
			 		try{		
			 			//conn.rollback();		 			
			 		}catch(Exception ex){
			 			
			 		}
			 		
			 	}finally{
			 	}		 	
		 }
		 
		 public void createInformixLogRootTable(DBManager conn,String tablename,String ipstr){
			 	PreparedStatement stmt = null;
			 	String sql = "";
			 		//创建按分钟采集数据的表
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,DBNODEID VARCHAR(30),DETAIL VARCHAR(500),"
				 	+"COLLECTTIME timestamp,"
				 	+" PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";		 		
			 	SysLogger.info(sql);		 	
			 	try{
			 		conn.executeUpdate(sql);
			 	}catch(Exception e){
			 		try{		
			 			//conn.rollback();		 			
			 		}catch(Exception ex){
			 			
			 		}
			 		
			 	}finally{
			 	}		 	
		 }
		 
		 public void dropRootTable(DBManager conn,String tablename,String ipstr){
		 	PreparedStatement stmt = null;
		 	String sql = "";
		 	sql="drop table if exists "+tablename+ipstr;
		 	//SysLogger.info(sql);		 	
		 	try{
		 		conn.executeUpdate(sql);
		 	}catch(Exception e){
		 		
		 		try{		 			
		 			//conn.rollback();		 			
		 		}catch(Exception ex){
		 			
		 		}
		 		
		 	}finally{
		 		//conn.commit();
		 	}		 	
		 }		 

		 
		 public void createIndex(Connection con, String tname,String ipstr,String indexsub,String tablename,String fieldname){
		 	PreparedStatement stmt = null;
		 	String indexstr="";
		 	indexstr="create index "+tname+ipstr+indexsub+" on "+tablename+ipstr+" ("+fieldname+") tablespace DHCC_ITTABSPACE";
System.out.println(indexstr);		 	
		 	try{
		 		stmt = con.prepareStatement(indexstr);
		 		stmt.execute();
		 		stmt.close();
		 	}catch(Exception e){
		 		e.printStackTrace();
		 		try{		 			
		 			con.rollback();
		 			//DataGate.freeCon(con);		 			
		 		}catch(Exception ex){
		 			
		 		}		 		
		 	}finally{
		 		try{
		 			if (stmt != null)stmt.close();
		 		}catch(Exception e){
		 			
		 		}
		 	}
		 }

		 public void createSeq(Connection con,String tablestr,String ipstr){
		 	PreparedStatement stmt = null;
		 	String createSeqStr="";
		 	createSeqStr="create sequence "+tablestr+"_"+ipstr+"SEQ minvalue 1 maxvalue 999999999999999999999999999 start with 5413921 increment by 1 cache 10";
System.out.println(createSeqStr);		 	
		 	try{
		 		stmt = con.prepareStatement(createSeqStr);
		 		stmt.execute();
		 		stmt.close();
		 	}catch(Exception e){
		 		e.printStackTrace();
		 		try{		 			
		 			//con.rollback();
		 			//DataGate.freeCon(con);		 			
		 		}catch(Exception ex){
		 			
		 		}
		 		
		 	}finally{
		 		try{
		 			if (stmt != null)stmt.close();
		 		}catch(Exception e){
		 			
		 		}
		 	}		 	
		 	
		 }
		 public void dropSeq(Connection con,String tablestr,String ipstr){
		 	PreparedStatement stmt = null;
		 	String createSeqStr="";
		 	createSeqStr="drop sequence "+tablestr+"_"+ipstr+"SEQ";
System.out.println(createSeqStr);		 	
		 	try{
		 		stmt = con.prepareStatement(createSeqStr);
		 		stmt.execute();
		 		stmt.close();
		 	}catch(Exception e){
		 		e.printStackTrace();
		 		try{		 			
		 			//con.rollback();
		 			//DataGate.freeCon(con);		 			
		 		}catch(Exception ex){
		 			
		 		}
		 		
		 	}finally{
		 		try{
		 			if (stmt != null)stmt.close();
		 		}catch(Exception e){
		 			
		 		}
		 	}		 	
		 	
		 }
		 
	
		 public void createTriger(Connection con,String tablestr,String ipstr,String tablename){
		 	PreparedStatement stmt = null;
		 	String trigerstr="";
		 	trigerstr="create or replace trigger "+tablestr+ipstr+"id before insert on "+tablename+ipstr+" for each row when (new.id is null) begin "
					+" select "+tablestr+"_"+ipstr+"SEQ.nextval into :new.id from dual; end;";
		 	System.out.println(trigerstr);
		 	try{
		 		stmt = con.prepareStatement(trigerstr);
		 		stmt.execute();
		 		stmt.close();
		 	}catch(Exception e){
		 		e.printStackTrace();
		 		try{		 			
		 			//con.rollback();
		 			//DataGate.freeCon(con);		 			
		 		}catch(Exception ex){
		 			
		 		}		 		
		 	}finally{
		 		try{
		 			if (stmt != null)stmt.close();
		 		}catch(Exception e){
		 			
		 		}
		 	}		 	
		 }
		 
		 public void dropDbconfigInfo(DBManager conn,String tablename,String ipstr){
			 	PreparedStatement stmt = null;
			 	String sql = "";
			 	sql="delete from "+tablename+" where ipaddress = '"+ipstr+"'";	 	
			 	try{
			 		conn.executeUpdate(sql);
			 	}catch(Exception e){
			 		try{		 			
			 			conn.rollback();		 			
			 		}catch(Exception ex){
			 			
			 		}
			 		
			 	}finally{
			 	}		 	
			 }
		 
		 public void createWasTable(DBManager conn,String tablename,String ipstr){
			 	Connection con = null;
			 	PreparedStatement stmt = null;
			 	try {
			 		//con=DataGate.getCon();		 		
			 		createWasRootTable(conn,tablename,ipstr);
					//create index
			 		/*
					createIndex(con,"I_"+tablestr,ipstr,"CA",tablename,"CATEGORY");
					createIndex(con,"I_"+tablestr,ipstr,"CT",tablename,"COLLECTTIME");
					createIndex(con,"I_"+tablestr,ipstr,"EN",tablename,"ENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"IP",tablename,"IPADDRESS");
					createIndex(con,"I_"+tablestr,ipstr,"SEN",tablename,"SUBENTITY");
					createIndex(con,"I_"+tablestr,ipstr,"VAL",tablename,"THEVALUE");	
					*/
					//create sequence
					//createSeq(con,tablestr,ipstr);
					//create trigger
					//createTriger(con,tablestr,ipstr,tablename);		 		
				}catch(Exception ex){	
					ex.printStackTrace();
				}finally{
				}			
			}	





		 public void createWasRootTable(DBManager conn,String tablename,String ipstr){
			 	PreparedStatement stmt = null;
			 	String sql = "";
			 	if(tablename.indexOf("system")>=0){
			 		
				 	sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),freeMemory VARCHAR(200),cpuUsageSinceServerStarted VARCHAR(200),"
				 	+"cpuUsageSinceLastMeasurement VARCHAR(200),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("jdbc")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),freePoolSize VARCHAR(200),useTime VARCHAR(200),"
				 	+"prepStmtCacheDiscardCount VARCHAR(200),waitingThreadCount VARCHAR(200),allocateCount VARCHAR(200),"
				 	+"faultCount VARCHAR(200),waitTime VARCHAR(200),createCount VARCHAR(200),jdbcTime VARCHAR(200),percentUsed VARCHAR(200),"
				 	+"poolSize VARCHAR(200),closeCount VARCHAR(200),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("session")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),activeCount VARCHAR(20),createCount VARCHAR(20),"
				 	+"invalidateCount VARCHAR(20),lifeTime VARCHAR(20),liveCount VARCHAR(20),"
				 	+"timeoutInvalidationCount VARCHAR(20),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("jvminfo")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),freeMemory VARCHAR(20),heapSize VARCHAR(20),"
				 	+"upTime VARCHAR(20),usedMemory VARCHAR(20),memPer VARCHAR(20),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("cache")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),inMemoryCacheCount VARCHAR(20),maxInMemoryCacheCount VARCHAR(20),"
				 	+"timeoutInvalidationCount VARCHAR(20),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
//			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("thread")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),activeCount VARCHAR(20),createCount VARCHAR(20),"
				 	+"destroyCount VARCHAR(20),poolSize VARCHAR(20),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}else if(tablename.indexOf("trans")>=0){
			 		sql="create table "+tablename+ipstr
				 	+"(ID bigint(20) not null auto_increment,IPADDRESS VARCHAR(30),activeCount VARCHAR(20),committedCount VARCHAR(20),"
				 	+"globalBegunCount VARCHAR(20),globalTimeoutCount VARCHAR(20),globalTranTime VARCHAR(20),"
				 	+"localActiveCount VARCHAR(20),localBegunCount VARCHAR(20),localRolledbackCount VARCHAR(20),localTimeoutCount VARCHAR(20),localTranTime VARCHAR(20),"
				 	+"rolledbackCount VARCHAR(20),recordtime timestamp,PRIMARY KEY  (ID)) ENGINE=InnoDB DEFAULT CHARSET=gb2312";	 		
			 	//}
//				 	System.out.println(sql);
			 	}
				 	try{
				 		conn.executeUpdate(sql);
				 	}catch(Exception e){
				 		e.printStackTrace();
				 		try{		
				 			conn.rollback();		 			
				 		}catch(Exception ex){
			
				 		}
		
				 	}finally{
				 	}		 	
			 }	
		 
		   
	 /**
	 * 删除多个设备的临时表中的数据
	 * @param tableName 表名称
	 * @param nodeid
	 * @return
	 */
	public Boolean clearNmsTempDatas(String[] tableNames,String[] ids){
		DBManager dbmanager = new DBManager();
		Boolean returnFlag = false;
		if(ids != null && ids.length > 0){
			try {
				//进行修改
				for(int i=0;i<ids.length;i++){
					String id = ids[i];
				    PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));          
					for(String tableName : tableNames){
						String sql = "delete from "+tableName+" where nodeid='"+id+"'";
						System.out.println(sql);
						dbmanager.addBatch(sql);
					}
				}
				dbmanager.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dbmanager.close();
			}
			returnFlag = true;
		}
		return returnFlag;
	}
	
	/**
	 * 清空多张指定表的数据
	 * @param tableNames 表名称
	 * @param uniqueKey  唯一键  如：nodeid
	 * @param nodeids    唯一键对应的值  如：结点ID数组
	 * @return
	 */
	public Boolean clearTablesData(String[] tableNames,String uniqueKey, String[] uniqueKeyValues){
		DBManager dbmanager = new DBManager();
		try {
			for(String uniqueValue:uniqueKeyValues){
				for(String tableName : tableNames){
					String sql = "delete from "+tableName+" where "+uniqueKey+" = '"+uniqueValue+"'";
					dbmanager.addBatch(sql);
				}
			}
			////System.out.println(sql);
			dbmanager.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbmanager.close();
		}
		return true;
	}

	public static void main(String [] args){
		//PingUtil pingU=new PingUtil("10.40.30.133");
		//		Integer[] packet=pingU.ping();
		//		pingU.addhis(packet);
	}
	
}
