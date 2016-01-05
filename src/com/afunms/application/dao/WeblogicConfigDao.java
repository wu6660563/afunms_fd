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

import com.afunms.application.model.Tomcat;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.*;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.system.model.User;

public class WeblogicConfigDao extends BaseDao implements DaoInterface {

	public WeblogicConfigDao() {
		super("nms_weblogicconfig");
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
			   WeblogicConfig pvo = (WeblogicConfig)findByID(id+"");
			   String ipstr = pvo.getIpAddress();
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
	  			ctable.deleteTable(conn,"weblogicping",allipstr,"weblogicping");//Ping
	  			ctable.deleteTable(conn,"weblogicpinghour",allipstr,"weblogicpinghour");//Ping
	  			ctable.deleteTable(conn,"weblogicpingday",allipstr,"weblogicpingday");//Ping             	
			   conn.addBatch("delete from nms_weblogicconfig where id=" + id);
			   conn.executeBatch();
			   result = true;
		   }
		   catch(Exception e)
		   {
			   SysLogger.error("TomcatDao.delete()",e); 
		   }
		   finally
		   {
			   //conn.close();
		   }
		   return result;
	   }
	public List getByFlag(Integer flag){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from nms_weblogicconfig where mon_flag= ");
		sql.append(flag);
		return findByCriteria(sql.toString());
	}
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		
		WeblogicConfig vo=new WeblogicConfig();
		
		try {
			vo.setId(rs.getInt("id"));
			vo.setAlias(rs.getString("name"));
			vo.setIpAddress(rs.getString("ipaddress"));
			vo.setCommunity(rs.getString("community"));
			vo.setPortnum(rs.getInt("portnum"));
			vo.setSendmobiles(rs.getString("sendmobiles"));
			vo.setMon_flag(rs.getInt("mon_flag"));
			vo.setNetid(rs.getString("netid"));
			vo.setSendemail(rs.getString("sendemail"));
			vo.setSendphone(rs.getString("sendphone"));
			vo.setSupperid(rs.getInt("supperid"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	return vo;
	}

public boolean save(BaseVo vo) {
		
		boolean flag = true;
		
		WeblogicConfig vo1=(WeblogicConfig)vo;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into nms_weblogicconfig(id,name,ipaddress,community,portnum,sendmobiles,mon_flag,netid,sendemail,sendphone,supperid) values(");
		sql.append(vo1.getId());
		sql.append(",'");
		sql.append(vo1.getAlias());
		sql.append("','");
		sql.append(vo1.getIpAddress());
		sql.append("','");
		sql.append(vo1.getCommunity());
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
		sql.append(vo1.getSendphone());
		sql.append("','");
		sql.append(vo1.getSupperid());
		sql.append("')");
		 try{
			saveOrUpdate(sql.toString());
			CreateTableManager ctable = new CreateTableManager();         
			//测试生成表
			String ip = vo1.getIpAddress();
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
				ctable.createTable(conn,"weblogicping",allipstr,"weblogicping");//Ping
				ctable.createTable(conn,"weblogicpinghour",allipstr,"weblogicpinghour");//Ping
				ctable.createTable(conn,"weblogicpingday",allipstr,"weblogicpingday");//Ping   
	     
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
	
		WeblogicConfig vo1=(WeblogicConfig)vo;
		WeblogicConfig pvo = (WeblogicConfig)findByID(vo1.getId()+"");
		
		StringBuffer sql=new StringBuffer();
		sql.append("update nms_weblogicconfig set name='");
		sql.append(vo1.getAlias());
		sql.append("',ipaddress='");
		sql.append(vo1.getIpAddress());
		sql.append("',community='");
		sql.append(vo1.getCommunity());
		sql.append("',portnum='");
		sql.append(vo1.getPortnum());
		sql.append("',sendmobiles='");
		sql.append(vo1.getSendmobiles());
		sql.append("',mon_flag='");
		sql.append(vo1.getMon_flag());
		sql.append("',netid='");
		sql.append(vo1.getNetid());
		sql.append("',sendemail='");
		sql.append(vo1.getSendemail());
		sql.append("',sendphone='");
		sql.append(vo1.getSendphone());
		sql.append("',supperid='");
		sql.append(vo1.getSupperid());
		sql.append("' where id="+vo1.getId());

		try {
				
		    		saveOrUpdate(sql.toString());
				
				if (!vo1.getIpAddress().equals(pvo.getIpAddress())){
		           	//修改了IP
					//若IP地址发生改变,先把表删除，然后在重新建立
						String ipstr = pvo.getIpAddress();
//						String ip1 ="",ip2="",ip3="",ip4="";
//						String[] ipdot = ipstr.split(".");	
//						String tempStr = "";
//						String allipstr = "";
//						if (ipstr.indexOf(".")>0){
//							ip1=ipstr.substring(0,ipstr.indexOf("."));
//							ip4=ipstr.substring(ipstr.lastIndexOf(".")+1,ipstr.length());			
//							tempStr = ipstr.substring(ipstr.indexOf(".")+1,ipstr.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ipstr);

						CreateTableManager ctable = new CreateTableManager();
		               
		            	conn = new DBManager();
		       			ctable.deleteTable(conn,"weblogicping",allipstr,"weblogicping");//Ping
		       			//conn = new DBManager();
		       			ctable.deleteTable(conn,"weblogicpinghour",allipstr,"weblogicpinghour");//Ping
		       			//conn = new DBManager();
		       			ctable.deleteTable(conn,"weblogicpingday",allipstr,"weblogicpingday");//Ping    
		                 			               
		           	
						//测试生成表
						String ip = vo1.getIpAddress();
//						ip1 ="";ip2="";ip3="";ip4="";
//						ipdot = ip.split(".");	
//						tempStr = "";
//						allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						allipstr = SysUtil.doip(ip);

						ctable = new CreateTableManager();
			            	//conn = new DBManager();
			    			ctable.createTable(conn,"weblogicping",allipstr,"weblogicping");//Ping
			    			//conn = new DBManager();
			    			ctable.createTable(conn,"weblogicpinghour",allipstr,"weblogicpinghour");//Ping
			    			//conn = new DBManager();
			    			ctable.createTable(conn,"weblogicpingday",allipstr,"weblogicpingday");//Ping
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
	 //处理Ping得到的数据，放到历史表里
	public synchronized boolean createHostData(Pingcollectdata pingdata) {
		if (pingdata == null )
			return false;	
		//DBManager dbmanager = new DBManager();
		try{			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Vector v = new Vector();
			//for (int i = 0; i < hostdatavec.size(); i++) {
				//Pingcollectdata pingdata = (Pingcollectdata)hostdatavec.elementAt(i);	
				String ip = pingdata.getIpaddress();				
				if (pingdata.getRestype().equals("dynamic")) {						
//					String ip1 ="",ip2="",ip3="",ip4="";
//					String[] ipdot = ip.split(".");	
//					String tempStr = "";
					String allipstr = "";
//					if (ip.indexOf(".")>0){
//						ip1=ip.substring(0,ip.indexOf("."));
//						ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//						tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//					}
//					ip2=tempStr.substring(0,tempStr.indexOf("."));
//					ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//					allipstr=ip1+ip2+ip3+ip4;
					allipstr = SysUtil.doip(ip);
					Calendar tempCal = (Calendar)pingdata.getCollecttime();							
					Date cc = tempCal.getTime();
					String time = sdf.format(cc);
					String tablename = "";

					tablename = "weblogicping"+allipstr;
									
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
			
		}
		return true;
	}
	   public List getWeblogicByBID(Vector bids){
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
		   sql.append("select * from nms_weblogicconfig "+wstr);
		   //SysLogger.info(sql.toString());
		   return findByCriteria(sql.toString());
	   }
	   
	   //zhushouzhi-----------------
	  	   public int getidByIp(String ip) 
	  		{
	  			String string = "select id from nms_weblogicconfig where ipaddress ="+"'"+ip+"'";
	  			int id = 0;
	  			ResultSet rSet = null;
	  			rSet = conn.executeQuery(string);
	  			 try {
	  				while(rSet.next())
	  				 {
	  					 id = rSet.getInt(1);
	  				 }
	  			} catch (SQLException e) {
	  				// TODO Auto-generated catch block
	  				e.printStackTrace();
	  			}finally{
	  				conn.close();
	  			}
	  			return id;
	  		}

}