/*
 * Created on 2005-4-8
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.impl;

/*
 * Created on 2005-4-8
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.detail.reomte.model.ProcessInfo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.Hostlastcollectdata;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.temp.dao.DiskTempDao;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.dao.MemoryTempDao;
import com.afunms.temp.dao.ProcessTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostLastCollectDataDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/* 
import com.dhcc.webnms.api.equipment.I_Equipment;
import com.dhcc.webnms.host.api.I_HostLastCollectData;
import com.dhcc.webnms.host.om.AllUtilHdx;
import com.dhcc.webnms.host.om.CPUcollectdata;
import com.dhcc.webnms.host.om.Diskcollectdata;
import com.dhcc.webnms.host.om.Hostlastcollectdata;
import com.dhcc.webnms.host.om.Hostcollectdata;
import com.dhcc.webnms.host.om.Interfacecollectdata;
import com.dhcc.webnms.host.om.Pingcollectdata;
import com.dhcc.webnms.host.om.Memorycollectdata;
import com.dhcc.webnms.host.om.Systemcollectdata;
import com.dhcc.webnms.host.om.Usercollectdata;
import com.dhcc.webnms.host.om.Processcollectdata;
import com.dhcc.webnms.host.om.UtilHdx;
import com.dhcc.webnms.util.A_BaseMap;
import com.dhcc.webnms.util.InitCoordinate;
import com.dhcc.webnms.host.ShareData;
import com.dhcc.webnms.impl.equipment.*;
import com.dhcc.webnms.om.equipment.*;
*/

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HostLastCollectDataManager
	implements I_HostLastCollectData {
	private Host host;
	private boolean isCpuTime = false;
	public void setCpuTime(boolean cpuTime)
	{
		this.isCpuTime = cpuTime;
	}
	/**
	 *
	 */
	//I_Equipment equipmentManager=new EquipmentManager();
	public HostLastCollectDataManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setHost(Host host)
	{
		this.host = host;
	}


	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#createHostData(com.dhcc.webnms.host.om.Hostcollectdata)
	 */
	public boolean createHostData(Hostlastcollectdata hostdata) throws Exception {
		/*
		try{
							Session session=this.beginTransaction();
							session.save(hostdata);
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
										return false;
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
												return false;
								}
				*/
				return true;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#createHostData(java.util.Vector)
	 */
	public boolean createHostData(Vector hostdatavec) throws Exception {
		/*
		try{
							Session session=this.beginTransaction();
							for(int i=0;i<hostdatavec.size();i++){
								Hostlastcollectdata hostdata=(Hostlastcollectdata)hostdatavec.elementAt(i);
								session.save(hostdata);
							}
							session.flush();
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
										return false;
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
												return false;
								}
				*/
				return true;
	}

	public synchronized boolean createHostData(String ip,Vector hostdatavec) throws Exception {
		/*
		try{
							Session session=this.beginTransaction();
							session.delete(
									"from Hostlastcollectdata hostlastcollectdata where hostlastcollectdata.ipaddress='"
										+ ip+ "' ");
								session.flush();							
							for(int i=0;i<hostdatavec.size();i++){
								Hostlastcollectdata hostdata=(Hostlastcollectdata)hostdatavec.elementAt(i);
								session.save(hostdata);
							}
							session.flush();
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
										return false;
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
												return false;
								}
				*/
				return true;
	}
	
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#deleteHostData(java.lang.String)
	 */
	public boolean deleteHostData(String deletetime) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getByHostdataid(java.lang.Integer)
	 */
	public Hostlastcollectdata getByHostdataid(Integer hostdataid)
		throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getByIpaddress(java.lang.String, java.lang.String)
	 */
	public List getByIpaddress(String ipaddress, String time)
		throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getByIpCategory(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List getByIpCategory(String ipaddress, String Category, String time)
		throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getByIpCategoryEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List getByIpCategoryEntity(
		String ipaddress,
		String Category,
		String entity,
		String time)
		throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getBySearch(java.lang.String, java.lang.String)
	 */
	public List getBySearch(String searchfield, String searchkeyword)
		throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getHostcollectdata()
	 */
	public List getHostcollectdata() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostCollectData#getHostcollectdata(java.lang.String)
	 */
	public List getHostcollectdata(String time) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public Hashtable getStatic(String ipaddress,String category[]) throws Exception{
		Hashtable hash=new Hashtable();
		/*
		try{
							Session session=this.beginTransaction();
							Query query;
							List list=new ArrayList();
							for(int i=0;i<category.length;i++){
								String sql="from Hostlastcollectdata hostlastcollectdata where hostlastcollectdata.ipaddress='"+ipaddress+"' and  hostlastcollectdata.category='"+category[i]+"' order by id";
								query=session.createQuery(sql);
								list=query.list();
								//System.out.println("in host last collectdata manager from show info------"+list.size());
								hash.put(category[i],list);
							}
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
							}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
									}
				*/
		return hash;
					}

	public List getAllPingdata(String thevalue) throws Exception{
		List list=new ArrayList();
		/*
		try{
			Session session=this.beginTransaction();
			Query query;			
			String sql="from Hostlastcollectdata hostlastcollectdata where hostlastcollectdata.category='Ping' and  hostlastcollectdata.entity='Utilization' and  hostlastcollectdata.thevalue='"+thevalue+"' order by id";
			query=session.createQuery(sql);
			list=query.list();
			this.endTransaction(true);
		}
		catch(HibernateException  e){
			this.endTransaction(false);
			this.error("createhostdata.error:"+e);
			e.printStackTrace();
		}
		catch(Exception  e){
			this.endTransaction(false);
			this.error("createhostdata.othererror:"+e);
			e.printStackTrace();
		}
		*/
		return list;
	}
	
	public Hostlastcollectdata getByipandsubentity(String ipaddress,String index) throws Exception{
		List list=new ArrayList();
		Hostlastcollectdata lastdata = null;
		/*
		try{
			Session session=this.beginTransaction();
			Query query;			
			String sql="from Hostlastcollectdata hostlastcollectdata where hostlastcollectdata.ipaddress='"+ipaddress+"' and  hostlastcollectdata.subentity='"+index+"' order by id";
			query=session.createQuery(sql);
			list=query.list();
			if (list != null && list.size()>0){
				lastdata = (Hostlastcollectdata)list.get(0);
			}
			this.endTransaction(true);
			return lastdata;
		}
		catch(HibernateException  e){
			e.printStackTrace();
			return null;
		}
		catch(Exception  e){
			e.printStackTrace();
			return null;
		}	
		*/
		return null;
	}

	public List getBysubentity(String thevalue) throws Exception{
		List list=new ArrayList();
		Hostlastcollectdata lastdata = null;
		/*
		try{
			Session session=this.beginTransaction();
			Query query;			
			String sql="from Hostlastcollectdata hostlastcollectdata where hostlastcollectdata.category='Interface' and hostlastcollectdata.entity='ifOperStatus' and hostlastcollectdata.thevalue='"+thevalue+"' order by id";			
			query=session.createQuery(sql);
			list=query.list();
			this.endTransaction(true);
			return list;
		}
		catch(HibernateException  e){
			e.printStackTrace();
			return null;
		}
		catch(Exception  e){
			e.printStackTrace();
			return null;
		}
		*/
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.api.I_HostLastCollectData#getByIpaddress(java.lang.String)
	 */
	public List getByIpaddress(String ipaddress) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable getbyCategories(String ip,String[] category,String starttime,String endtime)throws Exception{
		Hashtable hash=new Hashtable();
		DBManager dbmanager = new DBManager();
		//ResultSet rs = null;
		try{
			String sql="";
			StringBuffer sb=new StringBuffer();
			sb.append("select * from Hostlastcollectdata h where h.ipaddress='");
			sb.append(ip);
			sb.append("' and (");
			for(int i=0; i<category.length; i++){
			  if(i!=0){sb.append(" or ");}
			  sb.append("h.category='"+category[i]+"'");
			}
			sb.append(") ");
			if(!starttime.equals("") && !starttime.equals("")){
				sb.append(" and h.collecttime>='"+starttime+"' and h.collecttime <='"+endtime+"' ");
			 }
            sql = sb.toString();
            Vector vector = new Vector();
            
            
            HostLastCollectDataDao lastdao = new HostLastCollectDataDao();
			List list=lastdao.findByCriteria(sql);
			if(list == null)list = new ArrayList();
			if(list.size()==0){
				lastdao.close();
				lastdao = new HostLastCollectDataDao();
				sql = "select * from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='System'";
				list=lastdao.findByCriteria(sql);
				hash.put("flag",new Integer(0));
			}
			else{
				hash.put("flag",new Integer(1));
			}

			for(int i=0; i<list.size(); i++){
				Hostlastcollectdata obj = (Hostlastcollectdata)list.get(i);
				if(obj==null)continue;
				String c = obj.getCategory();
				if(obj.getThevalue()==null || obj.getThevalue().equals("")){
					continue;
				}
				else{
				  String value = obj.getThevalue();
				  String unit = obj.getUnit();
				  if(c.equalsIgnoreCase("user")){
					vector.add(value);
					continue;
				  }
				  if(c.equalsIgnoreCase("cpu")){
				    hash.put("cpu",dofloat(value)+unit);
					continue;
				  }
				  if(c.equalsIgnoreCase("ping")){
					String subentity = obj.getSubentity();
				    if(subentity.equalsIgnoreCase("responsetime")){
				    	hash.put("response",dofloat(value)+unit);
					    continue;
				     }
				    if(subentity.equalsIgnoreCase("ConnectUtilization")){
					    hash.put("ConnectUtilization",dofloat(value)+unit);
					    hash.put("time",obj.getCollecttime());
					    continue;
				    }
				  }
				  else if(c.equalsIgnoreCase("system")){
					hash.put(obj.getSubentity(),value);
					continue;
				  }
				}
			}
			hash.put("user",vector);
			//lastdao.close();
							//this.endTransaction(true);
		}catch(Exception  ex){
			ex.printStackTrace();
		}finally{
			dbmanager.close();
		}
				
		return hash;
	}

	public Hashtable getbyCategories_share(String ip,String[] category,String starttime,String endtime)throws Exception{
		Hashtable hash=new Hashtable();
		/*
		try{
							Session session=this.beginTransaction();
							Hashtable sharedata = ShareData.getSharedata();
							Hashtable pingdata = ShareData.getPingdata();
							Hashtable ipdata = (Hashtable)sharedata.get(ip);
							if (ipdata !=null && ipdata.size()>0){
								for(int i=0;i<category.length;i++){
									if (category[i].equalsIgnoreCase("System")){
										Vector sdata = (Vector)ipdata.get("system");
										if (sdata !=null && sdata.size()>0){											
											for(int j=0;j<sdata.size();j++){
												Systemcollectdata obj = (Systemcollectdata)sdata.get(j);												
												if (obj.getThevalue() != null)													
												hash.put(obj.getSubentity(),obj.getThevalue());
											}
										}
									}else if (category[i].equalsIgnoreCase("user")){
										Vector sdata = (Vector)ipdata.get("user");
										Vector vector = new Vector();
										if (sdata !=null && sdata.size()>0){											
											for(int j=0;j<sdata.size();j++){
												Usercollectdata obj = (Usercollectdata)sdata.get(j);												
												if (obj.getThevalue() != null)													
													vector.add(obj.getThevalue());
											}
										}
										hash.put("user",vector);
									}
								}
							}
							hash.put("flag",new Integer(1));
							*/
							/*
							Vector sdata = (Vector)sharedata.get(ip);
							
							Vector pdata = (Vector)pingdata.get(ip);
							if (pdata!=null && pdata.size()>0){
								for(int i=0;i<pdata.size();i++){
									if (pdata.get(i)!= null)
									sdata.add(pdata.get(i));
								}
							}
                            Vector vector = new Vector();
							for(int i=0; i<sdata.size(); i++){
								Hostcollectdata obj = (Hostcollectdata)sdata.get(i);							
								if(obj==null)continue;
								String c = obj.getCategory();
								int checkflag = 0;
								if(category != null && category.length > 0){
									for(int k=0;k<category.length;k++){										
										if (c.equalsIgnoreCase(category[k])){
											checkflag=1;
											break;
										}
									}
								}
								if (checkflag == 0)continue;
								if(obj.getThevalue()==null || obj.getThevalue().equals("")){
									continue;
								}
								else{
								  String value = obj.getThevalue();
								  String unit = obj.getUnit();
								  if(c.equalsIgnoreCase("user")){
									vector.add(value);
									continue;
								  }
								  if(c.equalsIgnoreCase("cpu")){								  	
								    hash.put("cpu",dofloat(value)+unit);
									continue;
								  }
								  if(c.equalsIgnoreCase("ping")){
									String subentity = obj.getSubentity();
								    if(subentity.equalsIgnoreCase("responsetime")){
								    	hash.put("response",dofloat(value)+unit);
									    continue;
								     }
								    if(subentity.equalsIgnoreCase("ConnectUtilization")){
									    hash.put("ConnectUtilization",dofloat(value)+unit);
									    hash.put("time",obj.getCollecttime());
									    continue;
								    }
								  }
								  else if(c.equalsIgnoreCase("system")){
									hash.put(obj.getSubentity(),value);
									continue;
								  }
								}
								
							}
							hash.put("user",vector);
							hash.put("flag",new Integer(1));
							*/
							/*
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
								}
								*/
				return hash;
	}
	
	
	public Hashtable getCategory(String ip,String category,String starttime,String endtime)throws Exception{
		Hashtable hash=new Hashtable();
		/*
		try{
			                String timelimit = "";
			                if(!starttime.equals("") && !endtime.equals("")){
			                   timelimit = " and h.collecttime >= to_date('"+starttime+"','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('"+endtime+"','YYYY-MM-DD HH24:MI:SS') ";
			                }
							Session session=this.beginTransaction();
							StringBuffer sb=new StringBuffer();
							sb.append("from Hostlastcollectdata h where h.ipaddress='");
							sb.append(ip);
			                sb.append("' and h.category='");
			                sb.append(category+"' ");
							sb.append(timelimit);
							sb.append(" order by h.category");

							String sql=sb.toString();
							List list=session.createQuery(sql).list();
			                for(int i=0; i<list.size(); i++){
				                 Hostlastcollectdata obj = (Hostlastcollectdata)list.get(i);
				                 String c = obj.getCategory();
				                 if(obj.getThevalue().equals("") || obj.getThevalue()==null){
					                   continue;
				                 }
								 hash.put(obj.getSubentity(),obj.getThevalue());
			                }
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							e.printStackTrace();
						}
				        catch(Exception  e){
							this.endTransaction(false);
							e.printStackTrace();
						}
				        */
				         return hash;
	}

	//memory可能有时间限制
	public Hashtable getMemory(String ip,String category,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		try{
	    	Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	if(host == null){
				//从数据库里获取
				HostNodeDao hostdao = new HostNodeDao();
				HostNode node = null;
				try{
					node = (HostNode)hostdao.findByIpaddress(ip);
				}catch(Exception e){
				}finally{
					hostdao.close();
				}
				HostLoader loader = new HostLoader();
				loader.loadOne(node);
				loader.close();
				host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	}
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			
			MemoryTempDao memorydao = new MemoryTempDao();
			List allmemorylist = new ArrayList();
			try{
				allmemorylist = memorydao.getCurrMemoryListInfo(host.getId()+"","host",nodedto.getSubtype());
			}catch(Exception e){
				
			}finally{
				memorydao.close();
			}
			Vector memoryVector = new Vector();
			if(allmemorylist != null && allmemorylist.size()>0){
				Memorycollectdata memorydata = null; 
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(int i=0;i<allmemorylist.size();i++){
					NodeTemp nodetemp = (NodeTemp)allmemorylist.get(i);
					memorydata=new Memorycollectdata();
					memorydata.setIpaddress(host.getIpAddress());
					Date d = sdf1.parse(nodetemp.getCollecttime());
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					memorydata.setCollecttime(c);
					memorydata.setCategory(nodetemp.getEntity());
					memorydata.setEntity(nodetemp.getSubentity());
					memorydata.setSubentity(nodetemp.getSindex());
					memorydata.setRestype(nodetemp.getRestype());
					memorydata.setUnit(nodetemp.getUnit());
					memorydata.setThevalue(nodetemp.getThevalue());
					memoryVector.addElement(memorydata);
				}
			}
			
//			Hashtable sharedata = ShareData.getSharedata();
//			Hashtable ipdata = (Hashtable)sharedata.get(ip);		
			//if (ipdata !=null && ipdata.size()>0){
				//Vector sdata = (Vector)ipdata.get(category.toLowerCase());
			Vector sdata = memoryVector;
				if (category.toLowerCase().equalsIgnoreCase("memory")){
					Vector keydata = new Vector();
					Vector alldata = new Vector();
					if(sdata!=null && sdata.size()>0){
						for(int i=0;i<sdata.size();i++){
							Memorycollectdata hdata = (Memorycollectdata)sdata.get(i);
							if (!hdata.getCategory().equalsIgnoreCase(category))continue;	
							alldata.add(hdata);
							if (keydata.contains(hdata.getSubentity()))continue;
							keydata.add(hdata.getSubentity());
						}
						String[] key = new String[keydata.size()];			
						Hashtable[] hashs = new Hashtable[keydata.size()];
						for(int i=0;i<keydata.size();i++){
							key[i] = (String)(keydata.get(i));
							hashs[i] = new Hashtable();
						}
						//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+"order by h.subentity";
						//List list2=session.find(sql);
						for(int j=0; j<alldata.size(); j++){
							Memorycollectdata h = (Memorycollectdata)alldata.get(j);
							for(int m=0; m<keydata.size(); m++){
								if(h.getSubentity().equals(key[m])){									
									hashs[m].put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
									break;
								}
							}
						}
						for(int i=0; i<keydata.size(); i++){
							hashs[i].put("name",key[i]);
							hash.put(new Integer(i),hashs[i]);
						}
					}					
				}else if (category.toLowerCase().equalsIgnoreCase("disk")){
					Vector keydata = new Vector();
					Vector alldata = new Vector();
					if(sdata!=null && sdata.size()>0){
						for(int i=0;i<sdata.size();i++){
							Diskcollectdata hdata = (Diskcollectdata)sdata.get(i);
							if (!hdata.getCategory().equalsIgnoreCase(category))continue;	
							alldata.add(hdata);
							if (keydata.contains(hdata.getSubentity()))continue;
							keydata.add(hdata.getSubentity());
						}
						String[] key = new String[keydata.size()];			
						Hashtable[] hashs = new Hashtable[keydata.size()];
						for(int i=0;i<keydata.size();i++){
							key[i] = (String)(keydata.get(i));
							hashs[i] = new Hashtable();
						}
						for(int j=0; j<alldata.size(); j++){
							Hostcollectdata h = (Hostcollectdata)alldata.get(j);
							for(int m=0; m<keydata.size(); m++){
								if(h.getSubentity().equals(key[m])){
									hashs[m].put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
									break;
								}
							}
						}
						for(int i=0; i<keydata.size(); i++){
							hashs[i].put("name",key[i]);
							hash.put(new Integer(i),hashs[i]);
						}
					}										
				}
			//}
			   }

		catch(Exception  e){
						e.printStackTrace();
					}
		
		return hash;
	}
	
	public Hashtable getMemory_share(String ip,String category,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		
		try{
			
			Hashtable sharedata = ShareData.getSharedata();
			Hashtable ipdata = (Hashtable)sharedata.get(ip);		
			if (ipdata !=null && ipdata.size()>0){
				Vector sdata = (Vector)ipdata.get(category.toLowerCase());
				if (category.toLowerCase().equalsIgnoreCase("memory")){
					Vector keydata = new Vector();
					Vector alldata = new Vector();
					if(sdata!=null && sdata.size()>0){
						for(int i=0;i<sdata.size();i++){
							Memorycollectdata hdata = (Memorycollectdata)sdata.get(i);
							if (!hdata.getCategory().equalsIgnoreCase(category))continue;	
							alldata.add(hdata);
							if (keydata.contains(hdata.getSubentity()))continue;
							keydata.add(hdata.getSubentity());
						}
						String[] key = new String[keydata.size()];			
						Hashtable[] hashs = new Hashtable[keydata.size()];
						for(int i=0;i<keydata.size();i++){
							key[i] = (String)(keydata.get(i));
							hashs[i] = new Hashtable();
						}
						//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+"order by h.subentity";
						//List list2=session.find(sql);
						for(int j=0; j<alldata.size(); j++){
							Memorycollectdata h = (Memorycollectdata)alldata.get(j);
							for(int m=0; m<keydata.size(); m++){
								if(h.getSubentity().equals(key[m])){									
									hashs[m].put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
									break;
								}
							}
						}
						for(int i=0; i<keydata.size(); i++){
							hashs[i].put("name",key[i]);
							hash.put(new Integer(i),hashs[i]);
						}
					}					
				}else if (category.toLowerCase().equalsIgnoreCase("disk")){
					Vector keydata = new Vector();
					Vector alldata = new Vector();
					if(sdata!=null && sdata.size()>0){
						for(int i=0;i<sdata.size();i++){
							Diskcollectdata hdata = (Diskcollectdata)sdata.get(i);
							if (!hdata.getCategory().equalsIgnoreCase(category))continue;	
							alldata.add(hdata);
							if (keydata.contains(hdata.getSubentity()))continue;
							keydata.add(hdata.getSubentity());
						}
						String[] key = new String[keydata.size()];			
						Hashtable[] hashs = new Hashtable[keydata.size()];
						for(int i=0;i<keydata.size();i++){
							key[i] = (String)(keydata.get(i));
							hashs[i] = new Hashtable();
						}
						//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+"order by h.subentity";
						//List list2=session.find(sql);
						for(int j=0; j<alldata.size(); j++){
							Hostcollectdata h = (Hostcollectdata)alldata.get(j);
							for(int m=0; m<keydata.size(); m++){
								if(h.getSubentity().equals(key[m])){
									hashs[m].put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
									break;
								}
							}
						}
						for(int i=0; i<keydata.size(); i++){
							hashs[i].put("name",key[i]);
							hash.put(new Integer(i),hashs[i]);
						}
					}										
				}
			}
			   }

		catch(Exception  e){
						e.printStackTrace();
					}
		
		return hash;
	}
	
	
	//disk没有时间限制
	public Hashtable getDisk(String ip,String category,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		try{
			Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	if(host == null){
				//从数据库里获取
				HostNodeDao hostdao = new HostNodeDao();
				HostNode node = null;
				try{
					node = (HostNode)hostdao.findByIpaddress(ip);
				}catch(Exception e){
				}finally{
					hostdao.close();
				}
				HostLoader loader = new HostLoader();
				loader.loadOne(node);
				loader.close();
				host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	}
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			
			DiskTempDao diskdao = new DiskTempDao();
			List alldisklist = new ArrayList();
			try{
				alldisklist = diskdao.getNodeTemp(host.getId()+"","host",nodedto.getSubtype());
			}catch(Exception e){
				
			}finally{
				diskdao.close();
			}
			Vector diskVector = new Vector();
			if(alldisklist != null && alldisklist.size()>0){
				Diskcollectdata diskdata = null; 
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(int i=0;i<alldisklist.size();i++){
					NodeTemp nodetemp = (NodeTemp)alldisklist.get(i);
					diskdata=new Diskcollectdata();
					diskdata.setIpaddress(host.getIpAddress());
					Date d = sdf1.parse(nodetemp.getCollecttime());
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					diskdata.setCollecttime(c);
					diskdata.setCategory(nodetemp.getEntity());
					diskdata.setEntity(nodetemp.getSubentity());
					diskdata.setSubentity(nodetemp.getSindex());
					diskdata.setRestype(nodetemp.getRestype());
					diskdata.setUnit(nodetemp.getUnit());
					diskdata.setThevalue(nodetemp.getThevalue());
					diskVector.addElement(diskdata);
				}
			}
			
			
			
			
			List list1 = new ArrayList();
			List list2=new ArrayList();
			Vector sdata = diskVector;
				if (category.toLowerCase().equalsIgnoreCase("disk")){
				    if (sdata != null && sdata.size()>0){
				    	for (int i=0;i<sdata.size();i++){
				    		Diskcollectdata hdata = (Diskcollectdata)sdata.get(i); 
				    		if (hdata.getCategory().equals(category)){		    			
				    			if (!list1.contains(hdata.getSubentity()))
				    				list1.add(hdata.getSubentity());	
				    			list2.add(hdata);
				    		}
				    	}
				    }
								
					if(list1.size()!=0){
					String[] key = new String[list1.size()];
					Hashtable[] hashs = new Hashtable[list1.size()];
					for(int i=0;i<list1.size();i++){
						key[i] = (String)(list1.get(i));
						hashs[i] = new Hashtable();
					 }
					//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+" order by h.subentity";
					
					for(int j=0; j<list2.size(); j++){
						Diskcollectdata h = (Diskcollectdata)list2.get(j);
						for(int m=0; m<list1.size(); m++){
							if(h.getSubentity().equals(key[m])){
								String s = "";
								if(h.getEntity().equalsIgnoreCase("Utilization")){
									s = h.getThevalue();
									hashs[m].put(h.getEntity()+"value",s);
									//SysLogger.info(h.getEntity()+" value"+"========"+s);
								}
								if(h.getEntity().equalsIgnoreCase("INodeUtilization")){
									s = h.getThevalue();
									hashs[m].put(h.getEntity()+"value",s);
								}								
								s = dofloat(h.getThevalue())+h.getUnit();
								hashs[m].put(h.getEntity(),s);
							    break;
							}
						}
					}
					for(int i=0; i<list1.size(); i++){
						hashs[i].put("name",key[i]);
						hash.put(new Integer(i),hashs[i]);
					}
					}					
				}
		}catch(Exception  e){
			e.printStackTrace();
		}
		
		return hash;
	}
	
	public Hashtable getDisk_share(String ip,String category,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		try{
			List list1 = new ArrayList();
			List list2=new ArrayList();
		    Hashtable sharedata = ShareData.getSharedata();
		    Hashtable ipdata = (Hashtable)sharedata.get(ip);
		    if (ipdata != null && ipdata.size()>0){
				Vector sdata = (Vector)ipdata.get(category.toLowerCase());
				if (category.toLowerCase().equalsIgnoreCase("disk")){
				    if (sdata != null && sdata.size()>0){
				    	for (int i=0;i<sdata.size();i++){
				    		Diskcollectdata hdata = (Diskcollectdata)sdata.get(i); 
				    		if (hdata.getCategory().equals(category)){		    			
				    			if (!list1.contains(hdata.getSubentity()))
				    				list1.add(hdata.getSubentity());	
				    			list2.add(hdata);
				    		}
				    	}
				    }
								
					if(list1.size()!=0){
					String[] key = new String[list1.size()];
					Hashtable[] hashs = new Hashtable[list1.size()];
					for(int i=0;i<list1.size();i++){
						key[i] = (String)(list1.get(i));
						hashs[i] = new Hashtable();
					 }
					//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+" order by h.subentity";
					
					for(int j=0; j<list2.size(); j++){
						Diskcollectdata h = (Diskcollectdata)list2.get(j);
						for(int m=0; m<list1.size(); m++){
							if(h.getSubentity().equals(key[m])){
								String s = "";
								if(h.getEntity().equalsIgnoreCase("Utilization")){
									s = h.getThevalue();
									hashs[m].put(h.getEntity()+"value",s);
									//SysLogger.info(h.getEntity()+" value"+"========"+s);
								}
								if(h.getEntity().equalsIgnoreCase("INodeUtilization")){
									s = h.getThevalue();
									hashs[m].put(h.getEntity()+"value",s);
								}								
								s = dofloat(h.getThevalue())+h.getUnit();
								hashs[m].put(h.getEntity(),s);
							    break;
							}
						}
					}
					for(int i=0; i<list1.size(); i++){
						hashs[i].put("name",key[i]);
						hash.put(new Integer(i),hashs[i]);
					}
					}					
				}
		    }
		    
		    /*
		    Vector sdata = (Vector)sharedata.get(ip);
		    
		    if (sdata != null && sdata.size()>0){
		    	for (int i=0;i<sdata.size();i++){
		    		Hostcollectdata hdata = (Hostcollectdata)sdata.get(i); 
		    		if (hdata.getCategory().equals(category)){		    			
		    			if (!list1.contains(hdata.getSubentity()))
		    				list1.add(hdata.getSubentity());	
		    			list2.add(hdata);
		    		}
		    	}
		    }
						
			if(list1.size()!=0){
			String[] key = new String[list1.size()];
			Hashtable[] hashs = new Hashtable[list1.size()];
			for(int i=0;i<list1.size();i++){
				key[i] = (String)(list1.get(i));
				hashs[i] = new Hashtable();
			 }
			//sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='"+category+"' "+timelimit+" order by h.subentity";
			
			for(int j=0; j<list2.size(); j++){
				Hostcollectdata h = (Hostcollectdata)list2.get(j);
				for(int m=0; m<list1.size(); m++){
					if(h.getSubentity().equals(key[m])){
						String s = "";
						if(h.getEntity().equalsIgnoreCase("Utilization")){
							s = h.getThevalue();
							hashs[m].put(h.getEntity()+"value",s);
						}
						s = dofloat(h.getThevalue())+h.getUnit();
						hashs[m].put(h.getEntity(),s);
					    break;
					}
				}
			 }
			for(int i=0; i<list1.size(); i++){
				hashs[i].put("name",key[i]);
				hash.put(new Integer(i),hashs[i]);
			}
			}
			*/

			}
		catch(Exception  e){
						e.printStackTrace();
					}
		
		return hash;
	}
	
	
	//process一定有时间限制
	public Hashtable getProcess(String ip,String category,String order,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		try{
			List list1 = new ArrayList();
			List list2=new ArrayList();  
			List orderList = new ArrayList();
			
			String sql;
			List list = new ArrayList();
			
	    	Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	if(host == null){
	    		//从数据库里获取
	    		HostNodeDao hostdao = new HostNodeDao();
	    		HostNode node = null;
	    		try{
	    			node = (HostNode)hostdao.findByIpaddress(ip);
	    		}catch(Exception e){
	    		}finally{
	    			hostdao.close();
	    		}
	    		HostLoader loader = new HostLoader();
	    		loader.loadOne(node);
	    		loader.close();
	    		host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	}
	    	NodeUtil nodeUtil = new NodeUtil();
	    	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	    	List allprocesslist = new ArrayList();
	    	ProcessTempDao processdao = new ProcessTempDao(host.getIpAddress());
	    	Vector processVector = new Vector();
			try{
				allprocesslist = processdao.getNodeTempList(host.getId()+"", nodedto.getType(), nodedto.getSubtype());
				if(allprocesslist != null && allprocesslist.size()>0){
					Interfacecollectdata interfacedata=null;
					//UtilHdx utilhdx=new UtilHdx();
					//InPkts inpacks = new InPkts();
					//OutPkts outpacks = new OutPkts();
					//UtilHdxPerc utilhdxperc=new UtilHdxPerc();
					//AllUtilHdx allutilhdx = new AllUtilHdx();
					Processcollectdata processdata = null;
					for(int i=0;i<allprocesslist.size();i++){
						NodeTemp vo = (NodeTemp)allprocesslist.get(i);
						processdata=new Processcollectdata();
						processdata.setIpaddress(host.getIpAddress());
						processdata.setCollecttime(null);
						processdata.setCategory(vo.getEntity());
						processdata.setEntity(vo.getSubentity());
						processdata.setSubentity(vo.getSindex());
						processdata.setRestype(vo.getRestype());
						processdata.setUnit(" ");
						processdata.setThevalue(vo.getThevalue());
						processdata.setChname(vo.getChname());
						processVector.addElement(processdata);
					}
				}
			}catch(Exception e){
				
			}finally{
				processdao.close();
			}
				Vector sdata = processVector;
				if (category.toLowerCase().equalsIgnoreCase("Process")){
				    if (sdata != null && sdata.size()>0){
				    	for (int i=0;i<sdata.size();i++){
				    		Processcollectdata hdata = (Processcollectdata)sdata.get(i); 
				    		if (hdata.getCategory().equals(category)){
				    			list2.add(hdata);
				    			if (hdata.getEntity().equals(order)){		    					
				    				if (!list1.contains(hdata.getSubentity())){
				    					list1.add(hdata.getSubentity());
				    					orderList.add(hdata);
				    				}
				    			}
				    		}
				    	}
				    }
					//按照thevalue进行排序
				    int aix_flag = 0;
				    host = (Host)PollingEngine.getInstance().getNodeByIP(ip);
				    if((host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL || host.getCollecttype() == SystemConstant.COLLECTTYPE_TELNET || host.getCollecttype() == SystemConstant.COLLECTTYPE_SSH) && host.getSysOid().indexOf("1.3.6.1.4.1.2.3.1.2.1.1")>=0)
				    {  
				    	aix_flag = 1;//aix
				    }
				    if(host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL && host.getSysOid().indexOf("1.3.6.1.4.1.2021")>=0 || nodedto.getSubtype().equals(Constant.TYPE_HOST_SUBTYPE_LINUX))
				    {
				    	aix_flag = 2;//linux
				    }
				    if(host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL && host.getSysOid().indexOf("1.3.6.1.4.1.11.2.3.10.1")>=0)
				    {
				    	aix_flag = 3;//hp-unix   //HONGLI ADD
				    }
				    if(host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL && host.getSysOid().indexOf("1.3.6.1.4.1.42.2.1.1")>=0)
				    {
				    	aix_flag = 4;//sun-solaris   //HONGLI ADD
				    }
				    if (nodedto.getSubtype().equals(Constant.TYPE_HOST_SUBTYPE_TRU64)){
				    	aix_flag = 5;//hp-tru64   //wupinlong add
				    }
				    list1 = null; 
				    list1 = new ArrayList();
				    if (orderList != null && orderList.size()>0)
				    {
				    	for(int m=0;m<orderList.size();m++)
				    	{
				    		Processcollectdata hdata = (Processcollectdata)orderList.get(m);				    		
				    		for(int n=m+1;n<orderList.size();n++)
				    		{
				    			if((aix_flag == 1 || aix_flag == 2 || aix_flag == 3 || aix_flag == 4) &&isCpuTime)
				    			{
				    				Processcollectdata hosdata = (Processcollectdata)orderList.get(n);
					    			String hdata_str = hdata.getThevalue();
					    			String hosdata_str = hosdata.getThevalue();
					    			if (hdata_str !=null && hosdata_str !=null&&hdata_str.trim().length()>0&&hosdata_str.trim().length()>0)
					    			{
					    				String tmp = hdata.getThevalue();
					    				String[] time_str = tmp.split(":");
					    				int hdata_int = new Integer(time_str[0]) * 60;
					    				if (time_str.length >= 2) {
					    					hdata_int += new Integer(time_str[1]);
					    				}
					    				time_str = hosdata.getThevalue().split(":");
					    				int hosdata_int = 0;
					    				if(time_str.length==2){
					    					hosdata_int  = new Integer(time_str[0]) * 60 + new Integer(time_str[1]); 
					    				}else if(time_str.length==1){
					    					hosdata_int  = new Integer(time_str[0]) * 60 + new Integer(0); 
					    				}
					    				
					    				if (hdata_int<hosdata_int)
					    				{
					    					orderList.remove(m);
					    					orderList.add(m,hosdata);
					    					orderList.remove(n);
					    					orderList.add(n,hdata);
					    					hdata = hosdata;
					    					hosdata = null;
					    				}
					    			}
				    			}else if(aix_flag == 5 && isCpuTime){
				    				Processcollectdata hosdata = (Processcollectdata)orderList.get(n);
					    			String hdata_str = hdata.getThevalue();
					    			String hosdata_str = hosdata.getThevalue();
					    			if (hdata_str !=null && hosdata_str !=null&&hdata_str.trim().length()>0&&hosdata_str.trim().length()>0)
					    			{
					    				String tmp = hdata.getThevalue();
					    				String[] time_str = tmp.split(":");
					    				float hdata_int = 0;
					    				if (time_str.length == 3) {
					    					hdata_int = new Float(time_str[0])*3600 + new Float(time_str[1])*60+new Float(time_str[2]);
					    				}
					    				time_str = hosdata.getThevalue().split(":");
					    				float hosdata_int = 0;
					    				if(time_str.length == 3){
					    					hosdata_int = new Float(time_str[0])*3600 + new Float(time_str[1])*60+new Float(time_str[2]);
					    				}
					    				
					    				if (hdata_int<hosdata_int)
					    				{
					    					orderList.remove(m);
					    					orderList.add(m,hosdata);
					    					orderList.remove(n);
					    					orderList.add(n,hdata);
					    					hdata = hosdata;
					    					hosdata = null;
					    				}
					    			}
				    			}
				    			else
				    			{
				    				Processcollectdata hosdata = (Processcollectdata)orderList.get(n);
					    			if (hdata.getThevalue()!=null && hosdata.getThevalue()!=null&&hdata.getThevalue().trim().length()>0&&hosdata.getThevalue().trim().length()>0)
					    			{
					    				if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue())
					    				{
					    					orderList.remove(m);
					    					orderList.add(m,hosdata);
					    					orderList.remove(n);
					    					orderList.add(n,hdata);
					    					hdata = hosdata;
					    					hosdata = null;
					    				}
					    			}
				    			}
				    			
				    		}	
				    		list1.add(hdata.getSubentity());
				    		hdata=null;
				    	}				    	
				    }
				    aix_flag = 0;
    				isCpuTime = false;
					
					if(list1.size()!=0){
					String[] key = new String[list1.size()];
					Hashtable[] hashs = new Hashtable[list1.size()];
					for(int i=0;i<list1.size();i++){
						key[i] = (String)(list1.get(i));
						hashs[i] = new Hashtable();
					 }
					for(int j=0; j<list2.size(); j++){
						Processcollectdata h = (Processcollectdata)list2.get(j);
						for(int m=0; m<list1.size(); m++){
							if(h.getSubentity().equals(key[m])){
								String value = h.getThevalue();
								if(h.getEntity().equalsIgnoreCase("MemoryUtilization")){
									if(value != null && value.trim().length()>0)
										value = dofloat(value);
								}
							   hashs[m].put(h.getEntity(),value+h.getUnit());
							   break;
							}
						}
					 }
					 for(int i=0; i<list1.size(); i++){
					 	//hashs[i].put("pid",key[i]);
						 hashs[i].put("process_id",key[i]);
						hash.put(new Integer(i),hashs[i]);
					  }
					}
					
				}
		    //}
		    
		    

			list1 = null;

			}
		catch(Exception  e){
						e.printStackTrace();
					}
		
		return hash;
	}

	//	process一定有时间限制
	public Hashtable getProcess_share(String ip,String category,String order,String starttime,String endtime)throws Exception{
		Hashtable hash = new Hashtable();
		try{
			List list1 = new ArrayList();
			List list2=new ArrayList();
			List orderList = new ArrayList();
		    Hashtable sharedata = ShareData.getSharedata();
		    Hashtable ipdata = (Hashtable)sharedata.get(ip);
		    if (ipdata !=null && ipdata.size()>0){
				Vector sdata = (Vector)ipdata.get(category.toLowerCase());
				if (category.toLowerCase().equalsIgnoreCase("Process")){
				    if (sdata != null && sdata.size()>0){
				    	for (int i=0;i<sdata.size();i++){
				    		Processcollectdata hdata = (Processcollectdata)sdata.get(i); 
				    		if (hdata.getCategory().equals(category)){
				    			list2.add(hdata);
				    			if (hdata.getEntity().equals(order)){		    					
				    				if (!list1.contains(hdata.getSubentity())){
				    					list1.add(hdata.getSubentity());
				    					orderList.add(hdata);
				    				}
				    			}
				    		}
				    	}
				    }
					//按照thevalue进行排序
				    int aix_flag = 0;
				    host = (Host)PollingEngine.getInstance().getNodeByIP(ip);
				    if((host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL || host.getCollecttype() == SystemConstant.COLLECTTYPE_TELNET || host.getCollecttype() == SystemConstant.COLLECTTYPE_SSH) && host.getSysOid().indexOf("1.3.6.1.4.1.2.3.1.2.1.1")>=0)
				    {  
				    	aix_flag = 1;//aix
				    }
				    if(host.getCollecttype() == SystemConstant.COLLECTTYPE_SHELL && host.getSysOid().indexOf("1.3.6.1.4.1.2021")>=0)
				    {
				    	aix_flag = 2;//linux
				    }
				    list1 = null; 
				    list1 = new ArrayList();
				    if (orderList != null && orderList.size()>0)
				    {
				    	for(int m=0;m<orderList.size();m++)
				    	{
				    		Processcollectdata hdata = (Processcollectdata)orderList.get(m);				    		
				    		for(int n=m+1;n<orderList.size();n++)
				    		{
				    			if((aix_flag == 1 || aix_flag == 2) &&isCpuTime)
				    			{
				    				Processcollectdata hosdata = (Processcollectdata)orderList.get(n);
					    			String hdata_str = hdata.getThevalue();
					    			String hosdata_str = hosdata.getThevalue();
					    			if (hdata_str !=null && hosdata_str !=null&&hdata_str.trim().length()>0&&hosdata_str.trim().length()>0)
					    			{
					    				String tmp = hdata.getThevalue();
					    				String[] time_str = tmp.split(":");
					    				int hdata_int = new Integer(time_str[0]) * 60 + new Integer(time_str[1]);
					    				time_str = hosdata.getThevalue().split(":");
					    				int hosdata_int = new Integer(time_str[0]) * 60 + new Integer(time_str[1]);
					    				if (hdata_int<hosdata_int)
					    				{
					    					orderList.remove(m);
					    					orderList.add(m,hosdata);
					    					orderList.remove(n);
					    					orderList.add(n,hdata);
					    					hdata = hosdata;
					    					hosdata = null;
					    				}
					    			}
				    			}
				    			else
				    			{
				    				Processcollectdata hosdata = (Processcollectdata)orderList.get(n);
					    			if (hdata.getThevalue()!=null && hosdata.getThevalue()!=null&&hdata.getThevalue().trim().length()>0&&hosdata.getThevalue().trim().length()>0)
					    			{
					    				if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue())
					    				{
					    					orderList.remove(m);
					    					orderList.add(m,hosdata);
					    					orderList.remove(n);
					    					orderList.add(n,hdata);
					    					hdata = hosdata;
					    					hosdata = null;
					    				}
					    			}
				    			}
				    			
				    		}	
				    		list1.add(hdata.getSubentity());
				    		hdata=null;
				    	}				    	
				    }
				    aix_flag = 0;
    				isCpuTime = false;
					
					if(list1.size()!=0){
					String[] key = new String[list1.size()];
					Hashtable[] hashs = new Hashtable[list1.size()];
					for(int i=0;i<list1.size();i++){
						key[i] = (String)(list1.get(i));
						hashs[i] = new Hashtable();
					 }
					for(int j=0; j<list2.size(); j++){
						Processcollectdata h = (Processcollectdata)list2.get(j);
						for(int m=0; m<list1.size(); m++){
							if(h.getSubentity().equals(key[m])){
								String value = h.getThevalue();
								if(h.getEntity().equalsIgnoreCase("MemoryUtilization")){
									if(value != null && value.trim().length()>0)
										value = dofloat(value);
								}
							   hashs[m].put(h.getEntity(),value+h.getUnit());
							   break;
							}
						}
					 }
					 for(int i=0; i<list1.size(); i++){
					 	//hashs[i].put("pid",key[i]);
						 hashs[i].put("process_id",key[i]);
						hash.put(new Integer(i),hashs[i]);
					  }
					}
					
				}
		    }
		    
		    /*
		    Vector sdata = (Vector)sharedata.get(ip);
		    if (sdata != null && sdata.size()>0){
		    	for (int i=0;i<sdata.size();i++){
		    		Hostcollectdata hdata = (Hostcollectdata)sdata.get(i); 
		    		if (hdata.getCategory().equals(category)){
		    			list2.add(hdata);
		    			if (hdata.getEntity().equals(order)){		    					
		    				if (!list1.contains(hdata.getSubentity())){
		    					list1.add(hdata.getSubentity());
		    					orderList.add(hdata);
		    				}
		    			}
		    		}
		    	}
		    }
			//按照thevalue进行排序
		    list1 = null;
		    list1 = new ArrayList();
		    if (orderList != null && orderList.size()>0){
		    	for(int m=0;m<orderList.size();m++){
		    		Hostcollectdata hdata = (Hostcollectdata)orderList.get(m);				    		
		    		for(int n=m+1;n<orderList.size();n++){
		    			Hostcollectdata hosdata = (Hostcollectdata)orderList.get(n);
		    			if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue()){
		    				orderList.remove(m);
		    				orderList.add(m,hosdata);
		    				orderList.remove(n);
		    				orderList.add(n,hdata);
		    				hdata = hosdata;
		    				hosdata = null;
		    			}
		    		}					    		
		    		list1.add(hdata.getSubentity());
		    		hdata=null;
		    	}				    	
		    }
		    
			
			if(list1.size()!=0){
			String[] key = new String[list1.size()];
			Hashtable[] hashs = new Hashtable[list1.size()];
			for(int i=0;i<list1.size();i++){
				key[i] = (String)(list1.get(i));
				hashs[i] = new Hashtable();
			 }
			for(int j=0; j<list2.size(); j++){
				Hostcollectdata h = (Hostcollectdata)list2.get(j);
				for(int m=0; m<list1.size(); m++){
					if(h.getSubentity().equals(key[m])){
						String value = h.getThevalue();
						if(h.getEntity().equalsIgnoreCase("MemoryUtilization")){
							value = dofloat(value);
						}
					   hashs[m].put(h.getEntity(),value+h.getUnit());
					   break;
					}
				}
			 }
			 for(int i=0; i<list1.size(); i++){
			 	hashs[i].put("pid",key[i]);
				hash.put(new Integer(i),hashs[i]);
			  }
			}
			*/

			list1 = null;

			}
		catch(Exception  e){
						e.printStackTrace();
					}
		
		return hash;
	}
	
	
	public List[] getParameter(String[] ip)throws Exception{//monitoriplisttask里用到
		int size=ip.length;
		List[] host=new ArrayList[size];
		/*
		try{ List list=new ArrayList();
			Session session=this.beginTransaction();
			StringBuffer sb=new StringBuffer();
			String sql0="from Hostlastcollectdata h where h.entity='Utilization' and h.ipaddress='";
            for(int i=0;i<size;i++){
            	    String sql=sql0+ip[i]+"' order by h.category,h.subentity";
					list=session.createQuery(sql).list();
					int count=list.size();
					if(count!=0){
                            host[i]=list;
						}
					else host[i]=new ArrayList();
					}
							this.endTransaction(true);
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("createhostdata.error:"+e);
							e.printStackTrace();
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("createhostdata.othererror:"+e);
									e.printStackTrace();
								}
				*/
    return host;
    }
	public Hashtable getIfOctets(String ip) throws Exception {
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();
			Session session=this.beginTransaction();
			String sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='Interface' and (h.entity='ifInOctets' or h.entity='ifOutOctets')";
			list=session.createQuery(sql).list();
			this.endTransaction(true);
			if(list.size()==0)return null;
			for(int i=0;i<list.size();i++){
				Hostlastcollectdata h=(Hostlastcollectdata)list.get(i);
				if(i==0)hash.put("collecttime",h.getCollecttime());
				hash.put((h.getEntity()+":"+h.getSubentity()),h.getThevalue());
			}
		}
		catch(HibernateException  e){
			this.endTransaction(false);
			this.error("getIfOctets.error:"+e);
			e.printStackTrace();
		}
		catch(Exception  e){
			this.endTransaction(false);
			this.error("getIfOctets.othererror:"+e);
			e.printStackTrace();
		}
		*/
		return hash;
	}

	public Hashtable getLastPacks(String ip) throws Exception {
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();
							Session session=this.beginTransaction();
							String sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='Interface' and (h.entity='AllInCastPkts' or h.entity='AllOutCastPkts')";
								list=session.createQuery(sql).list();
							this.endTransaction(true);
						if(list.size()==0)return null;
						for(int i=0;i<list.size();i++){
							Hostlastcollectdata h=(Hostlastcollectdata)list.get(i);
							if(i==0)hash.put("collecttime",h.getCollecttime());
							hash.put((h.getEntity()+":"+h.getSubentity()),h.getThevalue());							
						}
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("getIfOctets.error:"+e);
							e.printStackTrace();
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("getIfOctets.othererror:"+e);
									e.printStackTrace();
								}
				*/
				return hash;
	}
	public Hashtable getLastDiscards(String ip) throws Exception {
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();
							Session session=this.beginTransaction();
							String sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='Interface' and (h.entity='AllInDiscards' or h.entity='AllOutDiscards')";
								list=session.createQuery(sql).list();
							this.endTransaction(true);
						if(list.size()==0)return null;
						for(int i=0;i<list.size();i++){
							Hostlastcollectdata h=(Hostlastcollectdata)list.get(i);
							if(i==0)hash.put("collecttime",h.getCollecttime());
							hash.put((h.getEntity()+":"+h.getSubentity()),h.getThevalue());							
						}
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("getIfOctets.error:"+e);
							e.printStackTrace();
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("getIfOctets.othererror:"+e);
									e.printStackTrace();
								}
				*/
				return hash;
	}

	public Hashtable getLastErrors(String ip) throws Exception {
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();
							Session session=this.beginTransaction();
							String sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='Interface' and (h.entity='AllInErrors' or h.entity='AllOutErrors')";
								list=session.createQuery(sql).list();
							this.endTransaction(true);
						if(list.size()==0)return null;
						for(int i=0;i<list.size();i++){
							Hostlastcollectdata h=(Hostlastcollectdata)list.get(i);
							if(i==0)hash.put("collecttime",h.getCollecttime());
							hash.put((h.getEntity()+":"+h.getSubentity()),h.getThevalue());							
						}
						}
						catch(HibernateException  e){
							this.endTransaction(false);
							this.error("getIfOctets.error:"+e);
							e.printStackTrace();
						}
				catch(Exception  e){
									this.endTransaction(false);
									this.error("getIfOctets.othererror:"+e);
									e.printStackTrace();
								}
				*/
				return hash;
	}
	
	public Hashtable getIfOctets_share(String ip) throws Exception {
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();							
		    Hashtable sharedata = ShareData.getSharedata();
		    Vector sdata = (Vector)sharedata.get(ip);
		    if (sdata != null && sdata.size()>0){
		    	for (int i=0;i<sdata.size();i++){
		    		Hostcollectdata hdata = (Hostcollectdata)sdata.get(i); 
		    		if (hdata.getCategory().equalsIgnoreCase("Interface")&& (hdata.getEntity().equalsIgnoreCase("ifinoctets") || hdata.getEntity().equalsIgnoreCase("ifoutoctets"))){
		    			//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		    			list.add(hdata);			    			
		    		}
		    	}
		    }
			if(list.size()==0)return null;
			for(int i=0;i<list.size();i++){
				Hostcollectdata h=(Hostcollectdata)list.get(i);
				if(i==0)hash.put("collecttime",h.getCollecttime());
				hash.put((h.getEntity()+":"+h.getSubentity()),h.getThevalue());
			}																				
		}
		catch(Exception  e){
							this.endTransaction(false);
							this.error("getIfOctets.othererror:"+e);
							e.printStackTrace();
						}
		*/
		return hash;
}
	
	
	public Hashtable getBand(String ip,String index,String starttime,String endtime)throws Exception{//端口当前
		Hashtable hash=new Hashtable();
		/*
		try{
			String sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.subentity like '%BandwidthUtilHdx%'";
			//String timelimit = " and h.collecttime between '"+starttime+"' and '"+endtime+"' ";
			String timelimit = " and h.collecttime >= to_date('"+starttime+"','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('"+endtime+"','YYYY-MM-DD HH24:MI:SS') ";
			List list=new ArrayList();
			Session session=this.beginTransaction();
			if(index.equals("")){
			   sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.entity like 'All%'"+timelimit;
			}
			else{
			   sql="from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.entity like '%BandwidthUtilHdx%' and h.subentity='"+index+"'"+timelimit;
			}
				list=session.find(sql);
				if(list.size()!=0){
				   for(int j=0;j<list.size();j++){
					Hostlastcollectdata h=(Hostlastcollectdata)list.get(j);
					if(index.equals("")){
                      hash.put(h.getSubentity(),dofloat(h.getThevalue())+h.getUnit());
					}
					else{
						hash.put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
					}
					}
				  }
			    this.endTransaction(true);
			   }
		catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		catch(Exception  e){
						this.endTransaction(false);
						this.error("createhostdata.other error:"+e);
						e.printStackTrace();
					}
		*/
		return hash;
	}

	public Hashtable getBand_share(String ip,String index,String starttime,String endtime)throws Exception{//端口当前
		Hashtable hash=new Hashtable();
		/*
		try{
			List list=new ArrayList();
			Session session=this.beginTransaction();
			Hashtable sharedata = ShareData.getSharedata();
			Vector sdata = (Vector)sharedata.get(ip);
			if (sdata!=null && sdata.size()>0){
				for(int i=0;i<sdata.size();i++){
					Hostcollectdata hdata = (Hostcollectdata)sdata.get(i);
					if (index.equals("")){
						if(hdata.getEntity()!=null && hdata.getEntity().length()>=3 && hdata.getEntity().substring(0,3).equals("All")){
							list.add(hdata);
						}						
					}else{
						if(hdata.getEntity().indexOf("BandwidthUtilHdx")>=0){
							list.add(hdata);
						}												
					}
				}				
			}
			if(list.size()!=0){
				   for(int j=0;j<list.size();j++){
					Hostcollectdata h=(Hostcollectdata)list.get(j);
					if(index.equals("")){
                   hash.put(h.getSubentity(),dofloat(h.getThevalue())+h.getUnit());
					}
					else{
						hash.put(h.getEntity(),dofloat(h.getThevalue())+h.getUnit());
					}
					}
				  }
			    this.endTransaction(true);			
			}
		catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		catch(Exception  e){
						this.endTransaction(false);
						this.error("createhostdata.other error:"+e);
						e.printStackTrace();
					}
		*/
		return hash;
	}


	private String dofloat(String num){
		String snum="0.0";
		if(num!=null){
			int inum=(int)(Float.parseFloat(num)*100);
			snum=Double.toString(inum/100.0);
		}
		return snum;
	}
	
	public Vector getAllUtilHdxInterface(String ip,String starttime,String endtime)throws Exception{
		Vector allutilhdxVector = new Vector();
		try{
			String sql;
			List list = new ArrayList();
			
	    	Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	if(host == null){
	    		//从数据库里获取
	    		HostNodeDao hostdao = new HostNodeDao();
	    		HostNode node = null;
	    		try{
	    			node = (HostNode)hostdao.findByIpaddress(ip);
	    		}catch(Exception e){
	    		}finally{
	    			hostdao.close();
	    		}
	    		HostLoader loader = new HostLoader();
	    		loader.loadOne(node);
	    		loader.close();
	    		host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	}
	    	NodeUtil nodeUtil = new NodeUtil();
	    	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			
			//从数据库查出所有相关的接口数据
	    	List allinterfacelist = new ArrayList();
			
			
			InterfaceTempDao interfacedao = new InterfaceTempDao(nodedto.getIpaddress());
			try{
				allinterfacelist = interfacedao.getNodeTempList(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), null);
				if(allinterfacelist != null && allinterfacelist.size()>0){
					Interfacecollectdata interfacedata=null;
					AllUtilHdx allutilhdx = new AllUtilHdx();
					for(int i=0;i<allinterfacelist.size();i++){
						NodeTemp vo = (NodeTemp)allinterfacelist.get(i);
						if("AllInBandwidthUtilHdx".equals(vo.getSindex()) || "AllOutBandwidthUtilHdx".equals(vo.getSindex())){
							allutilhdx=new AllUtilHdx();
							allutilhdx.setIpaddress(host.getIpAddress());
							allutilhdx.setCollecttime(null);
							allutilhdx.setCategory("Interface");
							allutilhdx.setEntity(vo.getSubentity());
							allutilhdx.setSubentity(vo.getSindex());
							allutilhdx.setRestype("dynamic");
							allutilhdx.setUnit(vo.getUnit());
							if("AllInBandwidthUtilHdx".equals(vo.getSubentity())){
								allutilhdx.setChname("入口");
							}else{
								allutilhdx.setChname("出口");
							}
							allutilhdx.setThevalue(vo.getThevalue());
							allutilhdxVector.addElement(allutilhdx);
						}
						
					}
				}
			}catch(Exception e){
				
			}finally{
				interfacedao.close();
			}
		}catch(Exception e){
			
		}
		return allutilhdxVector;
	}
	
	/**
	   * 获取所有链路的端口信息集合
	   * @param ipList  链路ip集合
	   * @param netInterfaceItem  
	   * @param string
	   * @param string2
	   * @param string3
	   * @return
	   */
	  public Hashtable getInterfaces(List<String> ipList,String[] netInterfaceItem, String orderflag,String starttime,String endtime){
		  Hashtable retHashtable = new Hashtable();
		  for(int f=0; f<ipList.size(); f++){
			  String ip = ipList.get(f);
			  Vector vector=new Vector();
				try{
					//System.out.println("orderflag : "+orderflag);
					String sql;
					List list = new ArrayList();
			    	Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
			    	if(host == null){
			    		//从数据库里获取
			    		HostNodeDao hostdao = new HostNodeDao();
			    		HostNode node = null;
			    		try{
			    			node = (HostNode)hostdao.findByIpaddress(ip);
			    		}catch(Exception e){
			    		}finally{
			    			hostdao.close();
			    		}
			    		HostLoader loader = new HostLoader();
			    		loader.loadOne(node);
			    		loader.close();
			    		host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
			    	}
			    	NodeUtil nodeUtil = new NodeUtil();
			    	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
					
			    	InterfaceTempDao interfacedao = new InterfaceTempDao(nodedto.getIpaddress());
			          Hashtable nodeListHash = null;
			          try {
			              nodeListHash = interfacedao.getNodeTempListHash(ipList, netInterfaceItem);
			          } catch (Exception e1) {
			              e1.printStackTrace();
			          }finally{
			              interfacedao.close();
			          }
			          
					//从数据库查出所有相关的接口数据
			    	List allinterfacelist = new ArrayList();
					Vector interfacevector = new Vector();
					Vector utilhdxVector = new Vector();
					Vector allutilhdxVector = new Vector();
					Vector packsVector = new Vector();
					Vector inpksVector = new Vector();
					Vector outpksVector = new Vector();
					Vector discardspercVector = new Vector();
					Vector errorspercVector = new Vector();
					Vector allerrorspercVector = new Vector();
					Vector alldiscardspercVector = new Vector();
					Vector allutilhdxpercVector=new Vector();
					Vector utilhdxpercVector = new Vector();
					
//					InterfaceTempDao interfacedao = new InterfaceTempDao();
					try{
//						allinterfacelist = interfacedao.getNodeTempList(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), null);
						if(nodeListHash != null && nodeListHash.containsKey(ip)){
							allinterfacelist = (ArrayList)nodeListHash.get(ip);
						}
						if(allinterfacelist != null && allinterfacelist.size()>0){
							Interfacecollectdata interfacedata=null;
							UtilHdx utilhdx=new UtilHdx();
							InPkts inpacks = new InPkts();
							OutPkts outpacks = new OutPkts();
							UtilHdxPerc utilhdxperc=new UtilHdxPerc();
							AllUtilHdx allutilhdx = new AllUtilHdx();
							for(int i=0;i<allinterfacelist.size();i++){
								NodeTemp vo = (NodeTemp)allinterfacelist.get(i);
								if("AllInBandwidthUtilHdx".equals(vo.getSindex()) || "AllOutBandwidthUtilHdx".equals(vo.getSindex()) || "AllBandwidthUtilHdx".equals(vo.getSindex()))continue;
								if("index".equals(vo.getSubentity()) || "ifDescr".equals(vo.getSubentity()) || "ifType".equals(vo.getSubentity())
										||"ifMtu".equals(vo.getSubentity())||"ifSpeed".equals(vo.getSubentity())||"ifPhysAddress".equals(vo.getSubentity())
										||"ifAdminStatus".equals(vo.getSubentity()) || "ifOperStatus".equals(vo.getSubentity()) || "ifname".equals(vo.getSubentity()))
										{
									//接口配置数据
									interfacedata=new Interfacecollectdata();
									interfacedata.setIpaddress(host.getIpAddress());
									interfacedata.setCollecttime(null);
									interfacedata.setCategory("Interface");
									interfacedata.setSubentity(vo.getSindex());
									interfacedata.setRestype("static");
									interfacedata.setUnit(vo.getUnit());
									interfacedata.setThevalue(vo.getThevalue());
									interfacedata.setChname(vo.getChname());
									interfacedata.setBak("");
									interfacedata.setEntity(vo.getSubentity());
									interfacevector.add(interfacedata);
								}else if("ifInMulticastPkts".equals(vo.getSubentity()) || "ifInBroadcastPkts".equals(vo.getSubentity())){
									inpacks=new InPkts();
									inpacks.setIpaddress(host.getIpAddress());
									inpacks.setCollecttime(null);
									inpacks.setCategory("Interface");
									inpacks.setEntity(vo.getSubentity());
									inpacks.setSubentity(vo.getSindex());
									inpacks.setRestype("dynamic");
									inpacks.setUnit("");
									if("ifInMulticastPkts".equals(vo.getSubentity())){
										inpacks.setChname("多播");
									}else{
										inpacks.setChname("广播");
									}
									inpacks.setThevalue(vo.getThevalue());
									inpksVector.addElement(inpacks);
								}else if("ifOutMulticastPkts".equals(vo.getSubentity()) || "ifOutBroadcastPkts".equals(vo.getSubentity())){
									outpacks=new OutPkts();
									outpacks.setIpaddress(host.getIpAddress());
									outpacks.setCollecttime(null);
									outpacks.setCategory("Interface");
									outpacks.setEntity(vo.getSubentity());
									outpacks.setSubentity(vo.getSindex());
									outpacks.setRestype("dynamic");
									outpacks.setUnit("");
									if("ifOutMulticastPkts".equals(vo.getSubentity())){
										outpacks.setChname("多播");
									}else{
										outpacks.setChname("广播");
									}
									outpacks.setThevalue(vo.getThevalue());
									outpksVector.addElement(outpacks);
								}else if("InBandwidthUtilHdx".equals(vo.getSubentity()) || "OutBandwidthUtilHdx".equals(vo.getSubentity())){
									utilhdx=new UtilHdx();
									utilhdx.setIpaddress(host.getIpAddress());
									utilhdx.setCollecttime(null);
									utilhdx.setCategory("Interface");
									utilhdx.setEntity(vo.getSubentity());
									utilhdx.setSubentity(vo.getSindex());
									utilhdx.setRestype("dynamic");
									utilhdx.setUnit(vo.getUnit());
									if("InBandwidthUtilHdx".equals(vo.getSubentity())){
										utilhdx.setChname("入口");
									}else{
										utilhdx.setChname("出口");
									}
									utilhdx.setThevalue(vo.getThevalue());
									utilhdxVector.addElement(utilhdx);
								}else if("InBandwidthUtilHdxPerc".equals(vo.getSubentity()) || "OutBandwidthUtilHdxPerc".equals(vo.getSubentity())){
									utilhdxperc=new UtilHdxPerc();
									utilhdxperc.setIpaddress(host.getIpAddress());
									utilhdxperc.setCollecttime(null);
									utilhdxperc.setCategory("Interface");
									utilhdxperc.setEntity(vo.getSubentity());
									utilhdxperc.setSubentity(vo.getSindex());
									utilhdxperc.setRestype("dynamic");
									utilhdxperc.setUnit(vo.getUnit());
									if("InBandwidthUtilHdxPerc".equals(vo.getSubentity())){
										utilhdxperc.setChname("入口");
									}else{
										utilhdxperc.setChname("出口");
									}
									utilhdxperc.setThevalue(vo.getThevalue());
									utilhdxpercVector.addElement(utilhdxperc);
								}
							}
						}
					}catch(Exception e){
						
					}finally{
						interfacedao.close();
					}		
					
					//全部
					    Hashtable hash2 = new Hashtable();
					    Hashtable sharedata = ShareData.getSharedata();
					    Hashtable ipdata = (Hashtable)sharedata.get(ip);
					    Vector subentity = new Vector();
					    
					    if (interfacevector != null && interfacevector.size()>0){
							for(int i=0;i<interfacevector.size();i++){
								Interfacecollectdata interfacedata = (Interfacecollectdata)interfacevector.elementAt(i);							
								Hostlastcollectdata lastdata = new Hostlastcollectdata();
								lastdata.setIpaddress(interfacedata.getIpaddress());
								lastdata.setCategory(interfacedata.getCategory());
								lastdata.setEntity(interfacedata.getEntity());
								lastdata.setSubentity(interfacedata.getSubentity());
								lastdata.setThevalue(interfacedata.getThevalue());
								lastdata.setCollecttime(interfacedata.getCollecttime());
								lastdata.setRestype(interfacedata.getRestype());
								lastdata.setUnit(interfacedata.getUnit());
								lastdata.setChname(interfacedata.getChname());
								lastdata.setBak(interfacedata.getBak());					
								subentity.add(lastdata);
							}
						}
					    
					    if (utilhdxVector != null && utilhdxVector.size()>0){
							for(int i=0;i<utilhdxVector.size();i++){
								UtilHdx utilhdx = (UtilHdx)utilhdxVector.elementAt(i);							
								//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
								Hostlastcollectdata lastdata = new Hostlastcollectdata();
								lastdata.setIpaddress(utilhdx.getIpaddress());
								lastdata.setCategory(utilhdx.getCategory());
								lastdata.setEntity(utilhdx.getEntity());
								lastdata.setSubentity(utilhdx.getSubentity());
								lastdata.setThevalue(utilhdx.getThevalue());
								lastdata.setCollecttime(utilhdx.getCollecttime());
								lastdata.setRestype(utilhdx.getRestype());
								lastdata.setUnit(utilhdx.getUnit());
								lastdata.setChname(utilhdx.getChname());
								lastdata.setBak(utilhdx.getBak());							
								subentity.add(lastdata);
							}
						}
					    
						if (utilhdxpercVector != null && utilhdxpercVector.size()>0){
							for(int i=0;i<utilhdxpercVector.size();i++){
								UtilHdxPerc utilhdx = (UtilHdxPerc)utilhdxpercVector.elementAt(i);							
								//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
								Hostlastcollectdata lastdata = new Hostlastcollectdata();
								lastdata.setIpaddress(utilhdx.getIpaddress());
								lastdata.setCategory(utilhdx.getCategory());
								lastdata.setEntity(utilhdx.getEntity());
								lastdata.setSubentity(utilhdx.getSubentity());
								lastdata.setThevalue(utilhdx.getThevalue());
								lastdata.setCollecttime(utilhdx.getCollecttime());
								lastdata.setRestype(utilhdx.getRestype());
								lastdata.setUnit(utilhdx.getUnit());
								lastdata.setChname(utilhdx.getChname());
								lastdata.setBak(utilhdx.getBak());							
								subentity.add(lastdata);
							}
						}
						
						if (inpksVector != null && inpksVector.size()>0){
							for(int i=0;i<inpksVector.size();i++){
								InPkts inpacks=(InPkts)inpksVector.elementAt(i);							
								//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
								Hostlastcollectdata lastdata = new Hostlastcollectdata();
								lastdata.setIpaddress(inpacks.getIpaddress());
								lastdata.setCategory(inpacks.getCategory());
								lastdata.setEntity(inpacks.getEntity());
								lastdata.setSubentity(inpacks.getSubentity());
								lastdata.setThevalue(inpacks.getThevalue());
								lastdata.setCollecttime(inpacks.getCollecttime());
								lastdata.setRestype(inpacks.getRestype());
								lastdata.setUnit(inpacks.getUnit());
								lastdata.setChname(inpacks.getChname());
								lastdata.setBak(inpacks.getBak());							
								subentity.add(lastdata);
							}
						}
						
						if (outpksVector != null && outpksVector.size()>0){
							for(int i=0;i<outpksVector.size();i++){
								OutPkts outpacks=(OutPkts)outpksVector.elementAt(i);;							
								//SysLogger.info(outpacks.getIpaddress()+" ==== "+outpacks.getSubentity()+" "+outpacks.getThevalue());
								Hostlastcollectdata lastdata = new Hostlastcollectdata();
								lastdata.setIpaddress(outpacks.getIpaddress());
								lastdata.setCategory(outpacks.getCategory());
								lastdata.setEntity(outpacks.getEntity());
								lastdata.setSubentity(outpacks.getSubentity());
								lastdata.setThevalue(outpacks.getThevalue());
								lastdata.setCollecttime(outpacks.getCollecttime());
								lastdata.setRestype(outpacks.getRestype());
								lastdata.setUnit(outpacks.getUnit());
								lastdata.setChname(outpacks.getChname());
								lastdata.setBak(outpacks.getBak());							
								subentity.add(lastdata);
							}
						}
					    //Vector subentity = (Vector)sharedata.get(ip);
					    if (subentity != null && subentity.size()>0){
					    	for (int i=0;i<subentity.size();i++){
					    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
					    		if (hdata.getCategory().equals("Interface")&&hdata.getEntity().equals("index")){
					    			list.add(hdata.getSubentity());			    			
					    		}
					    	}
					    }
					    if(list!=null && list.size()!=0){
						    for(int i=0 ; i<list.size() ; i++){
							    hash2.put(list.get(i).toString() , "");
						    }
					    
						    //有出口流速的端口
						    list = null;
						    list = new ArrayList();
						    List orderList = new ArrayList();
						    if (subentity != null && subentity.size()>0){
						    	for (int i=0;i<subentity.size();i++){
						    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
						    		if (hdata.getEntity().equals(orderflag)){	
						    			if (list.contains(hdata.getSubentity()))continue;
						    			list.add(hdata.getSubentity());	
						    			orderList.add(hdata);
						    		}
						    	}
						    }
						    //对orderList根据theValue进行排序
						    //List tempList = new ArrayList();
						    list = null;
						    list = new ArrayList();	
						    if (orderList != null && orderList.size()>0){
						    	for(int m=0;m<orderList.size();m++){
						    		Hostlastcollectdata hdata = (Hostlastcollectdata)orderList.get(m);				    		
						    		for(int n=m+1;n<orderList.size();n++){
						    			Hostlastcollectdata hosdata = (Hostlastcollectdata)orderList.get(n);
						    			if (orderflag.equalsIgnoreCase("index")){
							    			if (new Double(hdata.getThevalue()).doubleValue()>new Double(hosdata.getThevalue()).doubleValue()){
							    				orderList.remove(m);
							    				orderList.add(m,hosdata);
							    				orderList.remove(n);
							    				orderList.add(n,hdata);
							    				hdata = hosdata;
							    				hosdata = null;
							    			}				    				
						    			}else{
							    			if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue()){
							    				orderList.remove(m);
							    				orderList.add(m,hosdata);
							    				orderList.remove(n);
							    				orderList.add(n,hdata);
							    				hdata = hosdata;
							    				hosdata = null;
							    			}				    				
						    			}
						    		}	
//						    		得到排序后的Subentity的列表
						    		list.add(hdata.getSubentity());
						    		hdata=null;
						    	}				    	
						    }
						    
							Vector order = new Vector();
							for(int i=0; i<list.size(); i++){
								hash2.remove(list.get(i).toString());
								if (order.contains(list.get(i).toString()))continue;
								order.add(list.get(i).toString());						
							}
			                Set key = hash2.keySet();
			                Iterator it = key.iterator();
			                while(it.hasNext()){	                	
			                	String ss = (String) it.next();
			                	order.add(ss);
			                }
							List list2 = new ArrayList();
							Hashtable hash = new Hashtable();
						    if (subentity != null && subentity.size()>0){
						    	for (int i=0;i<subentity.size();i++){
						    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
						    		if (hdata.getCategory().equals("Interface")){
						    			for(int k=0;k<netInterfaceItem.length;k++){
						    				if (hdata.getEntity()!=null && hdata.getEntity().equals(netInterfaceItem[k])){				    					
						    					list2.add(hdata);
						    					break;
						    				}
						    			}			    			
						    		}
						    	}
						    }
							if(list2.size() != 0){
								for(int i = 0 ; i < list2.size() ; i++){
									Hostlastcollectdata obj = (Hostlastcollectdata)(list2.get(i));
									 String value = obj.getThevalue();
									 if(obj.getEntity().equalsIgnoreCase("OutBandwidthUtilHdxPerc")||obj.getEntity().equalsIgnoreCase("InBandwidthUtilHdxPerc")){
										value = dofloat(value);
									 }
									 
									 hash.put(obj.getEntity() + obj.getSubentity(),value + obj.getUnit());
									 //hash.put(obj.getEntity() + obj.getSubentity(),value);
								 }
							}
							  for(int j=0;j<order.size();j++){
								   String[] strs= new String[netInterfaceItem.length];
								   for(int i=0; i<strs.length; i++){
									   strs[i]="";

									   if(i==2 || i==8 || i==9){
									   		strs[i] = "0kb/s";
									   }
									   if(i==4 || i==5 ||i==6 || i==7){
									   		strs[i] = "0";
									   }
								   }
								   String index = order.get(j).toString();						   
								   for(int i=0;i<netInterfaceItem.length;i++){
								   		if(hash.get(netInterfaceItem[i]+index)!=null){
								   			String value = hash.get(netInterfaceItem[i]+index).toString();	
								   			strs[i] = value;
								   		}
								   }						   
								   vector.add(strs);
							  }
						    }
						    //this.endTransaction(true);
				    }catch(Exception  e){
								e.printStackTrace();
				    }
				    
			 retHashtable.put(ip, vector);
		  }
		  
		  
		  return retHashtable;
	  }

	public Vector getInterface(String ip,String[] InterfaceItem,String orderflag,String starttime,String endtime)throws Exception{
		Vector vector=new Vector();
		try{
			//System.out.println("orderflag : "+orderflag);
			String sql;
			List list = new ArrayList();
			
	    	Host host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	if(host == null){
	    		//从数据库里获取
	    		HostNodeDao hostdao = new HostNodeDao();
	    		HostNode node = null;
	    		try{
	    			node = (HostNode)hostdao.findByIpaddress(ip);
	    		}catch(Exception e){
	    		}finally{
	    			hostdao.close();
	    		}
	    		HostLoader loader = new HostLoader();
	    		loader.loadOne(node);
	    		loader.close();
	    		host = (Host)PollingEngine.getInstance().getNodeByIP(ip); 
	    	}
	    	NodeUtil nodeUtil = new NodeUtil();
	    	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			
			//从数据库查出所有相关的接口数据
	    	List allinterfacelist = new ArrayList();
			Vector interfacevector = new Vector();
			Vector utilhdxVector = new Vector();
			Vector allutilhdxVector = new Vector();
			Vector packsVector = new Vector();
			Vector inpksVector = new Vector();
			Vector outpksVector = new Vector();
			Vector discardspercVector = new Vector();
			Vector errorspercVector = new Vector();
			Vector allerrorspercVector = new Vector();
			Vector alldiscardspercVector = new Vector();
			Vector allutilhdxpercVector=new Vector();
			Vector utilhdxpercVector = new Vector();
			
			InterfaceTempDao interfacedao = new InterfaceTempDao(nodedto.getIpaddress());
			try{
				allinterfacelist = interfacedao.getNodeTempList(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), null);
				if(allinterfacelist != null && allinterfacelist.size()>0){
					Interfacecollectdata interfacedata=null;
					UtilHdx utilhdx=new UtilHdx();
					InPkts inpacks = new InPkts();
					OutPkts outpacks = new OutPkts();
					UtilHdxPerc utilhdxperc=new UtilHdxPerc();
					AllUtilHdx allutilhdx = new AllUtilHdx();
					for(int i=0;i<allinterfacelist.size();i++){
						NodeTemp vo = (NodeTemp)allinterfacelist.get(i);
						if("AllInBandwidthUtilHdx".equals(vo.getSindex()) || "AllOutBandwidthUtilHdx".equals(vo.getSindex()) || "AllBandwidthUtilHdx".equals(vo.getSindex()))continue;
						if("index".equals(vo.getSubentity()) || "ifDescr".equals(vo.getSubentity()) || "ifType".equals(vo.getSubentity())
								||"ifMtu".equals(vo.getSubentity())||"ifSpeed".equals(vo.getSubentity())||"ifPhysAddress".equals(vo.getSubentity())
								||"ifAdminStatus".equals(vo.getSubentity()) || "ifOperStatus".equals(vo.getSubentity()) || "ifname".equals(vo.getSubentity())|| "ipmac".equals(vo.getSubentity()))
								{
							//接口配置数据
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(null);
							interfacedata.setCategory("Interface");
							interfacedata.setSubentity(vo.getSindex());
							interfacedata.setRestype("static");
							interfacedata.setUnit(vo.getUnit());
							interfacedata.setThevalue(vo.getThevalue());
							interfacedata.setChname(vo.getChname());
							interfacedata.setBak("");
							interfacedata.setEntity(vo.getSubentity());
							interfacevector.add(interfacedata);
						}else if("ifInMulticastPkts".equals(vo.getSubentity()) || "ifInBroadcastPkts".equals(vo.getSubentity())){
							inpacks=new InPkts();
							inpacks.setIpaddress(host.getIpAddress());
							inpacks.setCollecttime(null);
							inpacks.setCategory("Interface");
							inpacks.setEntity(vo.getSubentity());
							inpacks.setSubentity(vo.getSindex());
							inpacks.setRestype("dynamic");
							inpacks.setUnit("");
							if("ifInMulticastPkts".equals(vo.getSubentity())){
								inpacks.setChname("多播");
							}else{
								inpacks.setChname("广播");
							}
							inpacks.setThevalue(vo.getThevalue());
							inpksVector.addElement(inpacks);
						}else if("ifOutMulticastPkts".equals(vo.getSubentity()) || "ifOutBroadcastPkts".equals(vo.getSubentity())){
							outpacks=new OutPkts();
							outpacks.setIpaddress(host.getIpAddress());
							outpacks.setCollecttime(null);
							outpacks.setCategory("Interface");
							outpacks.setEntity(vo.getSubentity());
							outpacks.setSubentity(vo.getSindex());
							outpacks.setRestype("dynamic");
							outpacks.setUnit("");
							if("ifOutMulticastPkts".equals(vo.getSubentity())){
								outpacks.setChname("多播");
							}else{
								outpacks.setChname("广播");
							}
							outpacks.setThevalue(vo.getThevalue());
							outpksVector.addElement(outpacks);
						}else if("InBandwidthUtilHdx".equals(vo.getSubentity()) || "OutBandwidthUtilHdx".equals(vo.getSubentity())){
							utilhdx=new UtilHdx();
							utilhdx.setIpaddress(host.getIpAddress());
							utilhdx.setCollecttime(null);
							utilhdx.setCategory("Interface");
							utilhdx.setEntity(vo.getSubentity());
							utilhdx.setSubentity(vo.getSindex());
							utilhdx.setRestype("dynamic");
							utilhdx.setUnit(vo.getUnit());
							if("InBandwidthUtilHdx".equals(vo.getSubentity())){
								utilhdx.setChname("入口");
							}else{
								utilhdx.setChname("出口");
							}
							utilhdx.setThevalue(vo.getThevalue());
							utilhdxVector.addElement(utilhdx);
						}else if("InBandwidthUtilHdxPerc".equals(vo.getSubentity()) || "OutBandwidthUtilHdxPerc".equals(vo.getSubentity())){
							utilhdxperc=new UtilHdxPerc();
							utilhdxperc.setIpaddress(host.getIpAddress());
							utilhdxperc.setCollecttime(null);
							utilhdxperc.setCategory("Interface");
							utilhdxperc.setEntity(vo.getSubentity());
							utilhdxperc.setSubentity(vo.getSindex());
							utilhdxperc.setRestype("dynamic");
							utilhdxperc.setUnit(vo.getUnit());
							if("InBandwidthUtilHdxPerc".equals(vo.getSubentity())){
								utilhdxperc.setChname("入口");
							}else{
								utilhdxperc.setChname("出口");
							}
							utilhdxperc.setThevalue(vo.getThevalue());
							utilhdxpercVector.addElement(utilhdxperc);
						}
					}
				}
			}catch(Exception e){
				
			}finally{
				interfacedao.close();
			}		
			
			//全部
			    Hashtable hash2 = new Hashtable();
			    Hashtable sharedata = ShareData.getSharedata();
			    Hashtable ipdata = (Hashtable)sharedata.get(ip);
			    Vector subentity = new Vector();
			    
			    if (interfacevector != null && interfacevector.size()>0){
					for(int i=0;i<interfacevector.size();i++){
						Interfacecollectdata interfacedata = (Interfacecollectdata)interfacevector.elementAt(i);							
						Hostlastcollectdata lastdata = new Hostlastcollectdata();
						lastdata.setIpaddress(interfacedata.getIpaddress());
						lastdata.setCategory(interfacedata.getCategory());
						lastdata.setEntity(interfacedata.getEntity());
						lastdata.setSubentity(interfacedata.getSubentity());
						lastdata.setThevalue(interfacedata.getThevalue());
						lastdata.setCollecttime(interfacedata.getCollecttime());
						lastdata.setRestype(interfacedata.getRestype());
						lastdata.setUnit(interfacedata.getUnit());
						lastdata.setChname(interfacedata.getChname());
						lastdata.setBak(interfacedata.getBak());					
						subentity.add(lastdata);
					}
				}
			    
			    if (utilhdxVector != null && utilhdxVector.size()>0){
					for(int i=0;i<utilhdxVector.size();i++){
						UtilHdx utilhdx = (UtilHdx)utilhdxVector.elementAt(i);							
						//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
						Hostlastcollectdata lastdata = new Hostlastcollectdata();
						lastdata.setIpaddress(utilhdx.getIpaddress());
						lastdata.setCategory(utilhdx.getCategory());
						lastdata.setEntity(utilhdx.getEntity());
						lastdata.setSubentity(utilhdx.getSubentity());
						lastdata.setThevalue(utilhdx.getThevalue());
						lastdata.setCollecttime(utilhdx.getCollecttime());
						lastdata.setRestype(utilhdx.getRestype());
						lastdata.setUnit(utilhdx.getUnit());
						lastdata.setChname(utilhdx.getChname());
						lastdata.setBak(utilhdx.getBak());							
						subentity.add(lastdata);
					}
				}
			    
				if (utilhdxpercVector != null && utilhdxpercVector.size()>0){
					for(int i=0;i<utilhdxpercVector.size();i++){
						UtilHdxPerc utilhdx = (UtilHdxPerc)utilhdxpercVector.elementAt(i);							
						//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
						Hostlastcollectdata lastdata = new Hostlastcollectdata();
						lastdata.setIpaddress(utilhdx.getIpaddress());
						lastdata.setCategory(utilhdx.getCategory());
						lastdata.setEntity(utilhdx.getEntity());
						lastdata.setSubentity(utilhdx.getSubentity());
						lastdata.setThevalue(utilhdx.getThevalue());
						lastdata.setCollecttime(utilhdx.getCollecttime());
						lastdata.setRestype(utilhdx.getRestype());
						lastdata.setUnit(utilhdx.getUnit());
						lastdata.setChname(utilhdx.getChname());
						lastdata.setBak(utilhdx.getBak());							
						subentity.add(lastdata);
					}
				}
				
				if (inpksVector != null && inpksVector.size()>0){
					for(int i=0;i<inpksVector.size();i++){
						InPkts inpacks=(InPkts)inpksVector.elementAt(i);							
						//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
						Hostlastcollectdata lastdata = new Hostlastcollectdata();
						lastdata.setIpaddress(inpacks.getIpaddress());
						lastdata.setCategory(inpacks.getCategory());
						lastdata.setEntity(inpacks.getEntity());
						lastdata.setSubentity(inpacks.getSubentity());
						lastdata.setThevalue(inpacks.getThevalue());
						lastdata.setCollecttime(inpacks.getCollecttime());
						lastdata.setRestype(inpacks.getRestype());
						lastdata.setUnit(inpacks.getUnit());
						lastdata.setChname(inpacks.getChname());
						lastdata.setBak(inpacks.getBak());							
						subentity.add(lastdata);
					}
				}
				
				if (outpksVector != null && outpksVector.size()>0){
					for(int i=0;i<outpksVector.size();i++){
						OutPkts outpacks=(OutPkts)outpksVector.elementAt(i);;							
						//SysLogger.info(outpacks.getIpaddress()+" ==== "+outpacks.getSubentity()+" "+outpacks.getThevalue());
						Hostlastcollectdata lastdata = new Hostlastcollectdata();
						lastdata.setIpaddress(outpacks.getIpaddress());
						lastdata.setCategory(outpacks.getCategory());
						lastdata.setEntity(outpacks.getEntity());
						lastdata.setSubentity(outpacks.getSubentity());
						lastdata.setThevalue(outpacks.getThevalue());
						lastdata.setCollecttime(outpacks.getCollecttime());
						lastdata.setRestype(outpacks.getRestype());
						lastdata.setUnit(outpacks.getUnit());
						lastdata.setChname(outpacks.getChname());
						lastdata.setBak(outpacks.getBak());							
						subentity.add(lastdata);
					}
				}
			    //Vector subentity = (Vector)sharedata.get(ip);
			    if (subentity != null && subentity.size()>0){
			    	for (int i=0;i<subentity.size();i++){
			    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
			    		if (hdata.getCategory().equals("Interface")&&hdata.getEntity().equals("index")){
			    			list.add(hdata.getSubentity());			    			
			    		}
			    	}
			    }
			    if(list!=null && list.size()!=0){
				    for(int i=0 ; i<list.size() ; i++){
					    hash2.put(list.get(i).toString() , "");
				    }
			    
				    //有出口流速的端口
				    list = null;
				    list = new ArrayList();
				    List orderList = new ArrayList();
				    if (subentity != null && subentity.size()>0){
				    	for (int i=0;i<subentity.size();i++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
				    		if (hdata.getEntity().equals(orderflag)){	
				    			if (list.contains(hdata.getSubentity()))continue;
				    			list.add(hdata.getSubentity());	
				    			orderList.add(hdata);
				    		}
				    	}
				    }
				    //对orderList根据theValue进行排序
				    //List tempList = new ArrayList();
				    list = null;
				    list = new ArrayList();	
				    if (orderList != null && orderList.size()>0){
				    	for(int m=0;m<orderList.size();m++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)orderList.get(m);				    		
				    		for(int n=m+1;n<orderList.size();n++){
				    			Hostlastcollectdata hosdata = (Hostlastcollectdata)orderList.get(n);
				    			if (orderflag.equalsIgnoreCase("index")){
					    			if (new Double(hdata.getThevalue()).doubleValue()>new Double(hosdata.getThevalue()).doubleValue()){
					    				orderList.remove(m);
					    				orderList.add(m,hosdata);
					    				orderList.remove(n);
					    				orderList.add(n,hdata);
					    				hdata = hosdata;
					    				hosdata = null;
					    			}				    				
				    			}else{
					    			if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue()){
					    				orderList.remove(m);
					    				orderList.add(m,hosdata);
					    				orderList.remove(n);
					    				orderList.add(n,hdata);
					    				hdata = hosdata;
					    				hosdata = null;
					    			}				    				
				    			}
				    		}	
//				    		得到排序后的Subentity的列表
				    		list.add(hdata.getSubentity());
				    		hdata=null;
				    	}				    	
				    }
				    
					Vector order = new Vector();
					for(int i=0; i<list.size(); i++){
						hash2.remove(list.get(i).toString());
						if (order.contains(list.get(i).toString()))continue;
						order.add(list.get(i).toString());						
					}
	                Set key = hash2.keySet();
	                Iterator it = key.iterator();
	                while(it.hasNext()){	                	
	                	String ss = (String) it.next();
	                	order.add(ss);
	                }
					List list2 = new ArrayList();
					Hashtable hash = new Hashtable();
				    if (subentity != null && subentity.size()>0){
				    	for (int i=0;i<subentity.size();i++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
				    		if (hdata.getCategory().equals("Interface")){
				    			for(int k=0;k<InterfaceItem.length;k++){
				    				if (hdata.getEntity()!=null && hdata.getEntity().equals(InterfaceItem[k])){				    					
				    					list2.add(hdata);
				    					break;
				    				}
				    			}			    			
				    		}
				    	}
				    }
					if(list2.size() != 0){
						for(int i = 0 ; i < list2.size() ; i++){
							Hostlastcollectdata obj = (Hostlastcollectdata)(list2.get(i));
							 String value = obj.getThevalue();
							 if(obj.getEntity().equalsIgnoreCase("OutBandwidthUtilHdxPerc")||obj.getEntity().equalsIgnoreCase("InBandwidthUtilHdxPerc")){
								value = dofloat(value);
							 }
							 
							 hash.put(obj.getEntity() + obj.getSubentity(),value + obj.getUnit());
							 //hash.put(obj.getEntity() + obj.getSubentity(),value);
						 }
					}
					  for(int j=0;j<order.size();j++){
						   String[] strs= new String[InterfaceItem.length];
						   for(int i=0; i<strs.length; i++){
							   strs[i]="";

							   if(i==2 || i==8 || i==9){
							   		strs[i] = "0kb/s";
							   }
							   if(i==4 || i==5 ||i==6 || i==7){
							   		strs[i] = "0";
							   }
						   }
						   String index = order.get(j).toString();						   
						   for(int i=0;i<InterfaceItem.length;i++){
						   		if(hash.get(InterfaceItem[i]+index)!=null){
						   			String value = hash.get(InterfaceItem[i]+index).toString();	
						   			strs[i] = value;
						   		}
						   }						   
						   vector.add(strs);
					  }
				    }
				    //this.endTransaction(true);
		    }catch(Exception  e){
						e.printStackTrace();
					}
		return vector;
	}
	
	public Vector getInterface_share(String ip,String[] InterfaceItem,String orderflag,String starttime,String endtime)throws Exception{
		Vector vector=new Vector();
		
		try{
			//System.out.println("orderflag : "+orderflag);
			String sql;
			List list = new ArrayList();
			//全部
			    Hashtable hash2 = new Hashtable();
			    Hashtable sharedata = ShareData.getSharedata();
			    Hashtable ipdata = (Hashtable)sharedata.get(ip);
			    Vector subentity = new Vector();
			    if (ipdata != null && ipdata.size()>0){
					Vector tempv = (Vector)ipdata.get("interface");
					if (tempv != null && tempv.size()>0){
						for(int i=0;i<tempv.size();i++){
							Interfacecollectdata interfacedata = (Interfacecollectdata)tempv.elementAt(i);							
							Hostlastcollectdata lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(interfacedata.getIpaddress());
							lastdata.setCategory(interfacedata.getCategory());
							lastdata.setEntity(interfacedata.getEntity());
							lastdata.setSubentity(interfacedata.getSubentity());
							lastdata.setThevalue(interfacedata.getThevalue());
							//SysLogger.info(interfacedata.getIpaddress()+" " +interfacedata.getSubentity()+" "+interfacedata.getThevalue());
							lastdata.setCollecttime(interfacedata.getCollecttime());
							lastdata.setRestype(interfacedata.getRestype());
							lastdata.setUnit(interfacedata.getUnit());
							lastdata.setChname(interfacedata.getChname());
							lastdata.setBak(interfacedata.getBak());					
							subentity.add(lastdata);
						}
					}
					
					tempv = (Vector)ipdata.get("utilhdx");
					if (tempv != null && tempv.size()>0){
						for(int i=0;i<tempv.size();i++){
							UtilHdx utilhdx = (UtilHdx)tempv.elementAt(i);							
							//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
							Hostlastcollectdata lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(utilhdx.getIpaddress());
							lastdata.setCategory(utilhdx.getCategory());
							lastdata.setEntity(utilhdx.getEntity());
							lastdata.setSubentity(utilhdx.getSubentity());
							lastdata.setThevalue(utilhdx.getThevalue());
							lastdata.setCollecttime(utilhdx.getCollecttime());
							lastdata.setRestype(utilhdx.getRestype());
							lastdata.setUnit(utilhdx.getUnit());
							lastdata.setChname(utilhdx.getChname());
							lastdata.setBak(utilhdx.getBak());							
							subentity.add(lastdata);
						}
					}
					
					tempv = (Vector)ipdata.get("utilhdxperc");
					if (tempv != null && tempv.size()>0){
						for(int i=0;i<tempv.size();i++){
							UtilHdxPerc utilhdx = (UtilHdxPerc)tempv.elementAt(i);							
							//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
							Hostlastcollectdata lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(utilhdx.getIpaddress());
							lastdata.setCategory(utilhdx.getCategory());
							lastdata.setEntity(utilhdx.getEntity());
							lastdata.setSubentity(utilhdx.getSubentity());
							lastdata.setThevalue(utilhdx.getThevalue());
							lastdata.setCollecttime(utilhdx.getCollecttime());
							lastdata.setRestype(utilhdx.getRestype());
							lastdata.setUnit(utilhdx.getUnit());
							lastdata.setChname(utilhdx.getChname());
							lastdata.setBak(utilhdx.getBak());							
							subentity.add(lastdata);
						}
					}
					
					tempv = (Vector)ipdata.get("inpacks");
					if (tempv != null && tempv.size()>0){
						for(int i=0;i<tempv.size();i++){
							InPkts inpacks=(InPkts)tempv.elementAt(i);							
							//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
							Hostlastcollectdata lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(inpacks.getIpaddress());
							lastdata.setCategory(inpacks.getCategory());
							lastdata.setEntity(inpacks.getEntity());
							lastdata.setSubentity(inpacks.getSubentity());
							lastdata.setThevalue(inpacks.getThevalue());
							lastdata.setCollecttime(inpacks.getCollecttime());
							lastdata.setRestype(inpacks.getRestype());
							lastdata.setUnit(inpacks.getUnit());
							lastdata.setChname(inpacks.getChname());
							lastdata.setBak(inpacks.getBak());							
							subentity.add(lastdata);
						}
					}
					
					tempv = (Vector)ipdata.get("outpacks");
					if (tempv != null && tempv.size()>0){
						for(int i=0;i<tempv.size();i++){
							OutPkts outpacks=(OutPkts)tempv.elementAt(i);;							
							//SysLogger.info(utilhdx.getIpaddress()+" ==== "+utilhdx.getSubentity()+" "+utilhdx.getThevalue());
							Hostlastcollectdata lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(outpacks.getIpaddress());
							lastdata.setCategory(outpacks.getCategory());
							lastdata.setEntity(outpacks.getEntity());
							lastdata.setSubentity(outpacks.getSubentity());
							lastdata.setThevalue(outpacks.getThevalue());
							lastdata.setCollecttime(outpacks.getCollecttime());
							lastdata.setRestype(outpacks.getRestype());
							lastdata.setUnit(outpacks.getUnit());
							lastdata.setChname(outpacks.getChname());
							lastdata.setBak(outpacks.getBak());							
							subentity.add(lastdata);
						}
					}
					

			    }else{
			    	//设备连接不上,获取端口配置表里的数据
			    	PortconfigDao portconfigdao = new PortconfigDao();
			    	List portlist = new ArrayList();
			    	try{
			    		portlist = portconfigdao.loadByIpaddress(ip);
			    	}catch(Exception e){
			    		e.printStackTrace();
			    	}finally{
			    		portconfigdao.close();
			    	}
			    	Calendar date=Calendar.getInstance();
			    	final String[] desc=SnmpMibConstants.NetWorkMibInterfaceDesc0;
			    	final String[] unit=SnmpMibConstants.NetWorkMibInterfaceUnit0;
			    	final String[] chname=SnmpMibConstants.NetWorkMibInterfaceChname0;
			    	final int[] scale=SnmpMibConstants.NetWorkMibInterfaceScale0;
			    	if(portlist != null && portlist.size()>0){
			    		for(int i=0;i<portlist.size();i++){
			    			//端口配置
			    			Portconfig portconfig = (Portconfig)portlist.get(i);
			    			Hostlastcollectdata lastdata = new Hostlastcollectdata();
			    			
			    			for(int j=0;j<10;j++){
								//把预期状态和ifLastChange过滤掉
			    				lastdata = new Hostlastcollectdata();
								if (j==8)continue;
								String[] valueArray ={portconfig.getPortindex()+"",portconfig.getName()+"","0","0",portconfig.getSpeed(),"0","0","0","0","0","0",};
								String sValue=valueArray[j];	
								lastdata.setIpaddress(ip);
								lastdata.setCollecttime(date);
								lastdata.setCategory("Interface");
								lastdata.setEntity(desc[j]);
								lastdata.setSubentity(portconfig.getPortindex()+"");
								//端口状态不保存，只作为静态数据放到临时表里
								if(j==7)
									lastdata.setRestype("static");
								else {
									lastdata.setRestype("static");
								} 
								lastdata.setUnit(unit[j]);
								

								if((j==4)&&sValue!=null){//流速
//									long lValue=Long.parseLong(sValue)/scale[j];
									//long lValue=Long.parseLong(sValue);//yangjun								
										//hashSpeed.put(sIndex,Long.toString(lValue));
									//allSpeed=allSpeed+lValue;					
								}
								if((j==6 || j==7)&&sValue!=null){//预期状态和当前状态
									lastdata.setThevalue("down");
								} else if((j==2)&&sValue!=null){//断口类型
									//需要加上端口类型
									lastdata.setThevalue("");
								} else{
									if(j==1){
										//SysLogger.info(sValue+"==========");
										lastdata.setThevalue(sValue);
									}else{
										if(scale[j]==0){
											lastdata.setThevalue(sValue);
										}
										else{
											lastdata.setThevalue(Long.toString(Long.parseLong(sValue)/scale[j]));
										}
									}
									
								}
								lastdata.setChname(chname[j]);
								subentity.add(lastdata);
						   } //end for j
			    			
			    			
			    			
			    			
			    			
			    			
			    			
			    			
			    			
//							lastdata.setIpaddress(portconfig.getIpaddress());
//							lastdata.setCategory("Interface");
//							lastdata.setEntity(portconfig.getName());
//							lastdata.setSubentity(portconfig.getPortindex()+"");
//							lastdata.setThevalue("down");
//							//SysLogger.info(interfacedata.getIpaddress()+" " +interfacedata.getSubentity()+" "+interfacedata.getThevalue());
//							lastdata.setCollecttime(date);
//							lastdata.setRestype("static");
//							lastdata.setUnit("B/秒");
//							lastdata.setChname("");
//							lastdata.setBak("");
//							SysLogger.info("+++++++++++++"+portconfig.getPortindex());
//							subentity.add(lastdata);
							
							//流速
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("InBandwidthUtilHdx");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("0");
							lastdata.setCollecttime(date);
							lastdata.setRestype("");
							lastdata.setUnit("kb/s");
							lastdata.setChname(portconfig.getPortindex()+"端口入口流速");
							lastdata.setBak("dynamic");							
							subentity.add(lastdata);
							
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("OutBandwidthUtilHdx");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("0");
							lastdata.setCollecttime(date);
							lastdata.setRestype("");
							lastdata.setUnit("kb/s");
							lastdata.setChname(portconfig.getPortindex()+"端口出口流速");
							lastdata.setBak("dynamic");							
							subentity.add(lastdata);
							
							//带宽
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("InBandwidthUtilHdxPerc");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("0");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("%");
							lastdata.setChname(portconfig.getPortindex()+"端口入口带宽利用率");
							lastdata.setBak("");							
							subentity.add(lastdata);
							
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("OutBandwidthUtilHdxPerc");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("0");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("%");
							lastdata.setChname(portconfig.getPortindex()+"端口出口带宽利用率");
							lastdata.setBak("");							
							subentity.add(lastdata);
							
							//入口数据包
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("ifInUcastPkts");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("个");
							lastdata.setChname("单向");
							lastdata.setBak("");							
							subentity.add(lastdata);
							
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("ifInNUcastPkts");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("个");
							lastdata.setChname("非单向");
							lastdata.setBak("");							
							subentity.add(lastdata);
							
							//出口数据包
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("ifOutUcastPkts");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("个");
							lastdata.setChname("单向");
							lastdata.setBak("");							
							subentity.add(lastdata);
							
							lastdata = new Hostlastcollectdata();
							lastdata.setIpaddress(ip);
							lastdata.setCategory("Interface");
							lastdata.setEntity("ifOutNUcastPkts");
							lastdata.setSubentity(portconfig.getPortindex()+"");
							lastdata.setThevalue("");
							lastdata.setCollecttime(date);
							lastdata.setRestype("dynamic");
							lastdata.setUnit("个");
							lastdata.setChname("非单向");
							lastdata.setBak("");							
							subentity.add(lastdata);
			    		}
			    	}
			    	
			    	
			    	
			    	
			    }
			    
			    //Vector subentity = (Vector)sharedata.get(ip);
			    if (subentity != null && subentity.size()>0){
			    	for (int i=0;i<subentity.size();i++){
			    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
			    		if (hdata.getCategory().equals("Interface")&&hdata.getEntity().equals("index")){
			    			list.add(hdata.getSubentity());			    			
			    		}
			    	}
			    }
			    if(list!=null && list.size()!=0){
				    for(int i=0 ; i<list.size() ; i++){
					    hash2.put(list.get(i).toString() , "");
				    }
			    
				    //有出口流速的端口
				    list = null;
				    list = new ArrayList();
				    List orderList = new ArrayList();
				    if (subentity != null && subentity.size()>0){
				    	for (int i=0;i<subentity.size();i++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
				    		if (hdata.getEntity().equals(orderflag)){	
				    			if (list.contains(hdata.getSubentity()))continue;
				    			list.add(hdata.getSubentity());	
				    			orderList.add(hdata);
				    		}
				    	}
				    }
				    //对orderList根据theValue进行排序
				    //List tempList = new ArrayList();
				    list = null;
				    list = new ArrayList();	
				    if (orderList != null && orderList.size()>0){
				    	for(int m=0;m<orderList.size();m++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)orderList.get(m);				    		
				    		for(int n=m+1;n<orderList.size();n++){
				    			Hostlastcollectdata hosdata = (Hostlastcollectdata)orderList.get(n);
				    			if (orderflag.equalsIgnoreCase("index")){
					    			if (new Double(hdata.getThevalue()).doubleValue()>new Double(hosdata.getThevalue()).doubleValue()){
					    				orderList.remove(m);
					    				orderList.add(m,hosdata);
					    				orderList.remove(n);
					    				orderList.add(n,hdata);
					    				hdata = hosdata;
					    				hosdata = null;
					    			}				    				
				    			}else{
					    			if (new Double(hdata.getThevalue()).doubleValue()<new Double(hosdata.getThevalue()).doubleValue()){
					    				orderList.remove(m);
					    				orderList.add(m,hosdata);
					    				orderList.remove(n);
					    				orderList.add(n,hdata);
					    				hdata = hosdata;
					    				hosdata = null;
					    			}				    				
				    			}
				    		}	
//				    		得到排序后的Subentity的列表
				    		list.add(hdata.getSubentity());
				    		hdata=null;
				    	}				    	
				    }
				    
					Vector order = new Vector();
					for(int i=0; i<list.size(); i++){
						hash2.remove(list.get(i).toString());
						if (order.contains(list.get(i).toString()))continue;
						order.add(list.get(i).toString());						
					}
	                Set key = hash2.keySet();
	                Iterator it = key.iterator();
	                while(it.hasNext()){	                	
	                	String ss = (String) it.next();
	                	order.add(ss);
	                }
					List list2 = new ArrayList();
					Hashtable hash = new Hashtable();
				    if (subentity != null && subentity.size()>0){
				    	for (int i=0;i<subentity.size();i++){
				    		Hostlastcollectdata hdata = (Hostlastcollectdata)subentity.get(i); 
				    		if (hdata.getCategory().equals("Interface")){
				    			for(int k=0;k<InterfaceItem.length;k++){
				    				if (hdata.getEntity()!=null && hdata.getEntity().equals(InterfaceItem[k])){				    					
				    					list2.add(hdata);
				    					break;
				    				}
				    			}			    			
				    		}
				    	}
				    }
					if(list2.size() != 0){
						for(int i = 0 ; i < list2.size() ; i++){
							Hostlastcollectdata obj = (Hostlastcollectdata)(list2.get(i));
							 String value = obj.getThevalue();
							 if(obj.getEntity().equalsIgnoreCase("OutBandwidthUtilHdxPerc")||obj.getEntity().equalsIgnoreCase("InBandwidthUtilHdxPerc")){
								value = dofloat(value);
							 }
							 
							 hash.put(obj.getEntity() + obj.getSubentity(),value + obj.getUnit());
							 //hash.put(obj.getEntity() + obj.getSubentity(),value);
						 }
					}
					  for(int j=0;j<order.size();j++){
						   String[] strs= new String[InterfaceItem.length];
						   for(int i=0; i<strs.length; i++){
							   strs[i]="";

							   if(i==2 || i==8 || i==9){
							   		strs[i] = "0kb/s";
							   }
							   if(i==4 || i==5 ||i==6 || i==7){
							   		strs[i] = "0";
							   }
						   }
						   String index = order.get(j).toString();						   
						   for(int i=0;i<InterfaceItem.length;i++){
						   		if(hash.get(InterfaceItem[i]+index)!=null){
						   			String value = hash.get(InterfaceItem[i]+index).toString();	
						   			strs[i] = value;
						   		}
						   }						   
						   vector.add(strs);
					  }
				    }
				    //this.endTransaction(true);
		    }catch(Exception  e){
						e.printStackTrace();
					}
		
		return vector;
	}
	
	public Vector getIpMac(String ip)throws Exception{
		Vector vector=new Vector();
		/*
		try{
			String sql;
			List list = new ArrayList();
			Session session = this.beginTransaction();
			//从内存中获得当前的跟此IP相关的IP-MAC的ARP表信息
			Hashtable _relateIpMacHash = ShareData.getRelateipmacdata();
			Vector relateipmacV = new Vector();
			relateipmacV = (Vector)_relateIpMacHash.get(ip);						    
			this.endTransaction(true);
		    }//try
		catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		catch(Exception  e){
						this.endTransaction(false);
						this.error("createhostdata.other error:"+e);
						e.printStackTrace();
					}
		*/
		return vector;
	}
	
	
//网络设备的某端口详情
	public Hashtable getIfdetail(String ip,String index,String[] key,String starttime,String endtime)throws Exception{
		Hashtable hash=new Hashtable();
		/*
		try{
			String sql="";
			StringBuffer sb = new StringBuffer();
			sb.append(" and(");
			for(int i=0 ; i<key.length; i++){
				if(i!=0){sb.append("or");}
				sb.append(" h.entity='");
				sb.append(key[i]);
				sb.append("' ");
			}
			sb.append(")");

			//String timelimit = " and h.collecttime between '"+starttime+"' and '"+endtime+"' ";
			String timelimit = " and h.collecttime >= to_date('"+starttime+"','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('"+endtime+"','YYYY-MM-DD HH24:MI:SS') ";
			List list = new ArrayList();
			Session session = this.beginTransaction();
			sql = "from Hostlastcollectdata h where h.ipaddress='"+ip+"'"+" and h.subentity='"+index+"'"+sb.toString()+timelimit;
//System.out.println(sql);			
			list=session.find(sql);
			if(list.size()!=0){
				for(int j=0; j<list.size(); j++){
					Hostlastcollectdata h = (Hostlastcollectdata)list.get(j);
					String value = h.getThevalue();
					if(h.getEntity().equalsIgnoreCase("InBandwidthUtilHdxPerc")||h.getEntity().equalsIgnoreCase("OutBandwidthUtilHdxPerc")){
						value = dofloat(value);
					}
					if (h.getUnit()!=null){
						hash.put(h.getEntity(),value+h.getUnit());
					}else{
						hash.put(h.getEntity(),value);
					}
					}
				  }
				this.endTransaction(true);
			   }
		    catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		    catch(Exception  e){
				this.endTransaction(false);
				this.error("createhostdata.other error:"+e);
				e.printStackTrace();
			}
		    */
		    return hash;
	}
	
	public Hashtable getIfdetail_share(String ip,String index,String[] key,String starttime,String endtime)throws Exception{
		Hashtable hash=new Hashtable();
		try{
			List list = new ArrayList();
			//Session session = this.beginTransaction();			
			
			Hashtable sharedata = ShareData.getSharedata();
			Hashtable ipdata = (Hashtable)sharedata.get(ip);
			if (ipdata !=null && ipdata.size()>0){
				Vector sdata = (Vector)ipdata.get("interface");
				if (sdata !=null && sdata.size()>0){
					if (sdata!=null && sdata.size()>0){
						for(int i=0;i<sdata.size();i++){
							Interfacecollectdata hdata = (Interfacecollectdata)sdata.get(i);
							if (hdata.getSubentity()!=null && hdata.getSubentity().equals(index)){
								for(int k=0 ; k<key.length; k++){
									if(hdata.getEntity()!=null&&hdata.getEntity().equals(key[k])){
										list.add(hdata);
									}
								}
							}
						}				
					}
					
					
					if(list.size()!=0){
						for(int j=0; j<list.size(); j++){
							Interfacecollectdata h = (Interfacecollectdata)list.get(j);
							String value = h.getThevalue();
							if(h.getEntity().equalsIgnoreCase("InBandwidthUtilHdxPerc")||h.getEntity().equalsIgnoreCase("OutBandwidthUtilHdxPerc")){
								value = dofloat(value);
							}
							hash.put(h.getEntity(),value+h.getUnit());
							}
						  }
					
				}
			}
				//this.endTransaction(true);
			   }
		    catch(Exception  e){
				e.printStackTrace();
			}
		    
		    return hash;
	}

	
	public Hashtable getAllParam_share()throws Exception{
		Hashtable hash=new Hashtable();	
		/*
		try{
			Session session = this.beginTransaction();
			Hashtable sharedata = ShareData.getSharedata();
			Hashtable pingdata = ShareData.getPingdata();
			//得到所有的监视的IP
			String[] ips = new String[pingdata.size()];
			int k=0;
			if (pingdata != null && pingdata.size()>0){				
				for(Enumeration enumeration = pingdata.keys(); enumeration.hasMoreElements();){
					String ip = (String)enumeration.nextElement();
					ips[k] = ip;
					k++;
				}
			}	
			Vector[][] vector = new Vector[2][ips.length];
			Hashtable locationhash = new Hashtable();
			for(int i=0; i<ips.length; i++){
				String ip = (String)ips[i];				
				locationhash.put(ip,new Integer(i));
				vector[0][i] = new Vector();
				vector[1][i] = new Vector();
			}			
			
			//String sql="from Hostlastcollectdata h where h.entity='Utilization' or h.subentity ='AllOutBandwidthUtilHdxPerc' or h.subentity ='AllInBandwidthUtilHdxPerc' order by h.ipaddress,h.category,h.subentity";
			
			if (sharedata != null && sharedata.size()>0){
				for(int i=0;i<ips.length;i++){	
					//Vector hdata = (Vector)sharedata.get(ips[i]);
					Hashtable hdata = (Hashtable)sharedata.get(ips[i]);
					Vector pdata = (Vector)pingdata.get(ips[i]);
					if (hdata != null && hdata.size()>0){
						//system
						Vector systemVector = (Vector)hdata.get("system");
						if (systemVector != null && systemVector.size()>0){
							for(int si=0;si<systemVector.size();si++){
								Systemcollectdata systemdata = (Systemcollectdata)systemVector.elementAt(si);
								Equipment equipment=new Equipment();
								equipment=equipmentManager.getByip(systemdata.getIpaddress());				
								Roomlocation roomlocation = null;
								if (equipment!=null && equipment.getInfopoint() != null){
									roomlocation = equipment.getInfopoint();
									Equiptype equiptype = roomlocation.getType();
									//过滤掉网络设备的内存利用率
									if (!equiptype.getTypename().startsWith("host") && systemdata.getCategory().equals("Memory"))continue;
								}
								if(systemdata.getThevalue()!=null){									
				               		hash.put(systemdata.getIpaddress().trim()+":"+systemdata.getCategory().trim()+":"+systemdata.getSubentity().trim(),systemdata.getThevalue());
								}
							}
						}
						//memory
						Vector memoryVector = (Vector)hdata.get("memory");
						if (memoryVector == null)memoryVector = new Vector();						
						if (memoryVector != null && memoryVector.size()>0){							
							for(int si=0;si<memoryVector.size();si++){
								Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
								Equipment equipment=new Equipment();
								equipment=equipmentManager.getByip(memorydata.getIpaddress());				
								Roomlocation roomlocation = null;
								if (equipment!=null && equipment.getInfopoint() != null){
									roomlocation = equipment.getInfopoint();
									Equiptype equiptype = roomlocation.getType();
									//过滤掉网络设备的内存利用率
									if (!equiptype.getTypename().startsWith("host") && memorydata.getCategory().equals("Memory"))continue;
								}								
								if (memorydata.getEntity().equalsIgnoreCase("Utilization"))
				               	hash.put(memorydata.getIpaddress().trim()+":"+memorydata.getCategory().trim()+":"+memorydata.getSubentity().trim(),memorydata.getThevalue());								
								if(memorydata.getCategory().equalsIgnoreCase("Memory")){
									if (memorydata.getIpaddress()!=null && locationhash.get(memorydata.getIpaddress())!=null){
										int p = ((Integer)locationhash.get(memorydata.getIpaddress())).intValue();
										if (memorydata.getEntity().equalsIgnoreCase("Utilization"))
										vector[0][p].add(memorydata.getSubentity());
									}
								}
							}
						}
						
						//cpu
						Vector cpuVector = (Vector)hdata.get("cpu");
						if (cpuVector != null && cpuVector.size()>0){
							for(int si=0;si<cpuVector.size();si++){
								CPUcollectdata cpudata = (CPUcollectdata)cpuVector.elementAt(si);
								Equipment equipment=new Equipment();
								equipment=equipmentManager.getByip(cpudata.getIpaddress());				
								Roomlocation roomlocation = null;
								if (equipment!=null && equipment.getInfopoint() != null){
									roomlocation = equipment.getInfopoint();
									Equiptype equiptype = roomlocation.getType();
									//过滤掉网络设备的内存利用率
									if (!equiptype.getTypename().startsWith("host") && cpudata.getCategory().equals("Memory"))continue;
								}
				               	hash.put(cpudata.getIpaddress().trim()+":"+cpudata.getCategory().trim()+":"+cpudata.getSubentity().trim(),cpudata.getThevalue());
							}
						}
						
						//disk
						Vector diskVector = (Vector)hdata.get("disk");
						if (diskVector != null && diskVector.size()>0){
							for(int si=0;si<diskVector.size();si++){
								Diskcollectdata diskdata = (Diskcollectdata)diskVector.elementAt(si);
								Equipment equipment=new Equipment();
								equipment=equipmentManager.getByip(diskdata.getIpaddress());				
								Roomlocation roomlocation = null;
								if (equipment!=null && equipment.getInfopoint() != null){
									roomlocation = equipment.getInfopoint();
									Equiptype equiptype = roomlocation.getType();
									//过滤掉网络设备的内存利用率
									if (!equiptype.getTypename().startsWith("host") && diskdata.getCategory().equals("Memory"))continue;
								}
								if (diskdata.getEntity().equalsIgnoreCase("Utilization"))
				               	hash.put(diskdata.getIpaddress().trim()+":"+diskdata.getCategory().trim()+":"+diskdata.getSubentity().trim(),diskdata.getThevalue());
								if(diskdata.getCategory().equalsIgnoreCase("Disk")){
									if (diskdata.getIpaddress()!=null && locationhash.get(diskdata.getIpaddress())!=null){
										int p = ((Integer)locationhash.get(diskdata.getIpaddress())).intValue();
										if (diskdata.getEntity().equalsIgnoreCase("Utilization"))
										vector[1][p].add(diskdata.getSubentity());
									}
								}								
							}
						}						
						//AllUtilHdx			
						Vector allutilhdxVector = (Vector)hdata.get("allutilhdx");
						if (allutilhdxVector != null && allutilhdxVector.size()>0){
							for(int si=0;si<allutilhdxVector.size();si++){
								AllUtilHdx allutilhdx = (AllUtilHdx)allutilhdxVector.elementAt(si);
								Equipment equipment=new Equipment();
								equipment=equipmentManager.getByip(allutilhdx.getIpaddress());				
								Roomlocation roomlocation = null;
								if (equipment!=null && equipment.getInfopoint() != null){
									roomlocation = equipment.getInfopoint();
									Equiptype equiptype = roomlocation.getType();
									//过滤掉网络设备的内存利用率
									if (!equiptype.getTypename().startsWith("host") && allutilhdx.getCategory().equals("Memory"))continue;
								}								
				               	hash.put(allutilhdx.getIpaddress().trim()+":"+allutilhdx.getCategory().trim()+":"+allutilhdx.getSubentity().trim(),allutilhdx.getThevalue());
							}
						}
						
							
					}
					//把ping得到的数据加进去
					if (pdata != null && pdata.size()>0){
						for(int m=0;m<pdata.size();m++){
							Pingcollectdata hostdata = (Pingcollectdata)pdata.get(m);							
							if(hostdata.getSubentity().equals("ConnectUtilization")){								
								hash.put(hostdata.getIpaddress()+":time",hostdata.getCollecttime());
								hash.put(hostdata.getIpaddress().trim()+":"+hostdata.getCategory().trim()+":"+hostdata.getSubentity().trim(),hostdata.getThevalue());								
							}																	
						}						
					}					
				}
			}
			
            for(int i=0; i<ips.length; i++){
            	hash.put(ips[i]+":Memory",vector[0][i]);            	
            	hash.put(ips[i]+":Disk",vector[1][i]);            	
             }
            
			this.endTransaction(true);
		}
		catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		catch(Exception  e){
						this.endTransaction(false);
						this.error("createhostdata.other error:"+e);
						e.printStackTrace();
					}
		*/
		return hash;
	}
	
	public Hashtable getAllParam()throws Exception{
		Hashtable hash=new Hashtable();
		/*
		try{
			Session session = this.beginTransaction();
			String sql1="select distinct h.ipaddress from Hostlastcollectdata h";
			List list1 = session.createQuery(sql1).list();
			Vector[][] vector = new Vector[2][list1.size()];
			String[]  ips = new String[list1.size()];
			Hashtable locationhash = new Hashtable();
			for(int i=0; i<list1.size(); i++){
				String ip = (String)list1.get(i);
				ips[i] = ip;
				locationhash.put(ip,new Integer(i));
				vector[0][i] = new Vector();
				vector[1][i] = new Vector();
			}
			String sql="from Hostlastcollectdata h where h.entity='Utilization' or h.subentity ='AllOutBandwidthUtilHdxPerc' or h.subentity ='AllInBandwidthUtilHdxPerc' or h.subentity ='AllBandwidthUtilHdxPerc' or h.subentity ='AllOutBandwidthUtilHdx' or h.subentity ='AllInBandwidthUtilHdx' or h.subentity ='AllBandwidthUtilHdx' order by h.ipaddress,h.category,h.subentity";
            List list = session.createQuery(sql).list();
            for(int i=0; i<list.size(); i++){
				Hostlastcollectdata obj = (Hostlastcollectdata)list.get(i);
				Equipment equipment=new Equipment();
				equipment=equipmentManager.getByip(obj.getIpaddress());				
				Roomlocation roomlocation = null;
				if (equipment!=null && equipment.getInfopoint() != null){
					roomlocation = equipment.getInfopoint();
					Equiptype equiptype = roomlocation.getType();
					//过滤掉网络设备的内存利用率
					if (!equiptype.getTypename().equals("host") && obj.getCategory().equals("Memory"))continue;
				}
               	hash.put(obj.getIpaddress().trim()+":"+obj.getCategory().trim()+":"+obj.getSubentity().trim(),obj.getThevalue());
//System.out.println("ip:category:subentity="+obj.getIpaddress().trim()+":"+obj.getCategory().trim()+":"+obj.getSubentity().trim()+"---------value="+obj.getThevalue());
					if(obj.getCategory().equalsIgnoreCase("Memory")){
						if (obj.getIpaddress()!=null && locationhash.get(obj.getIpaddress())!=null){
							int p = ((Integer)locationhash.get(obj.getIpaddress())).intValue();
							vector[0][p].add(obj.getSubentity());
						}
					}
					if(obj.getCategory().equalsIgnoreCase("Disk")){
						if (obj.getIpaddress()!=null && locationhash.get(obj.getIpaddress())!=null){
							int p = ((Integer)locationhash.get(obj.getIpaddress())).intValue();
							vector[1][p].add(obj.getSubentity());
						}
					}
					if(obj.getSubentity().equalsIgnoreCase("ConnectUtilization")){
						hash.put(obj.getIpaddress()+":time",obj.getCollecttime());
					}
            }
            for(int i=0; i<list1.size(); i++){
            	hash.put(ips[i]+":Memory",vector[0][i]);
            	hash.put(ips[i]+":Disk",vector[1][i]);
             }
			this.endTransaction(true);
		}
		catch(HibernateException  e){
				this.endTransaction(false);
				this.error("createhostdata.error:"+e);
				e.printStackTrace();
			}
		catch(Exception  e){
						this.endTransaction(false);
						this.error("createhostdata.other error:"+e);
						e.printStackTrace();
					}
		*/
		return hash;
	}
	
	
	public Hashtable getIfStatus(String ip,String starttime,String endtime)throws Exception{
		Hashtable hash =new Hashtable();
		/*
		try{
			Session session = this.beginTransaction();
			
			//String timelimit = " and h.collecttime between '"+starttime+"' and '"+endtime+"' ";
			String timelimit = " and h.collecttime >= to_date('"+starttime+"','YYYY-MM-DD HH24:MI:SS') and h.collecttime <= to_date('"+endtime+"','YYYY-MM-DD HH24:MI:SS') ";
			String sql = "from Hostlastcollectdata h where h.ipaddress='"+ip+"' and h.category='Interface' and (h.entity='ifname' or h.entity='ifOperStatus' or h.entity='index')" +timelimit;
			Query query = session.createQuery(sql);
			List list = query.list();
			Vector indexv = new Vector();
			for(int i = 0 ; i < list.size() ; i++){
					 Hostlastcollectdata obj = (Hostlastcollectdata)(list.get(i));
                     if(obj.getEntity().equalsIgnoreCase("index")){
                     	indexv.add(obj.getThevalue());
                     }
                     else{
						hash.put(obj.getEntity()+obj.getSubentity(),obj.getThevalue());
                     }
				}
			hash.put("index",indexv);
			this.endTransaction(true);
		}catch(HibernateException  e){
		this.endTransaction(false);
		this.error("createhostdata.error:"+e);
		e.printStackTrace();
	    }
         catch(Exception  e){
				this.endTransaction(false);
				this.error("createhostdata.other error:"+e);
				e.printStackTrace();
			}
         */
		return hash;
	}
	
	   public BaseVo loadFromRS(ResultSet rs)
	   {
		   Hostlastcollectdata vo = new Hostlastcollectdata();
	       try
	       {
	    	   vo.setId(new Long(rs.getInt("id")));
	    	   vo.setIpaddress(rs.getString("ipaddress"));
	    	   vo.setRestype(rs.getString("restype"));
	    	   vo.setCategory(rs.getString("category"));
	    	   vo.setEntity(rs.getString("entity"));
	    	   vo.setSubentity(rs.getString("subentity"));
	    	   vo.setThevalue(rs.getString("thevalue"));
	    	   Date timestamp = rs.getTimestamp("collecttime");
	    	   Calendar date=Calendar.getInstance();
	    	   date.setTime(timestamp);
	    	   vo.setCollecttime(date);
	    	   vo.setUnit(rs.getString("unit"));
	       }
	       catch(Exception e)
	       {
	 	       SysLogger.error("HostNodeDao.loadFromRS()",e); 
	       }	   
	       return vo;
	   }
	   
	   public List loadAllFromRS(ResultSet rs)
	   {
		   List list = new ArrayList();
		   try{
	        while (rs.next()) {	
	        	try{
	        	Hostlastcollectdata vo = new Hostlastcollectdata();

		    	   vo.setId(new Long(rs.getInt("id")));
		    	   vo.setIpaddress(rs.getString("ipaddress"));
		    	   vo.setRestype(rs.getString("restype"));
		    	   vo.setCategory(rs.getString("category"));
		    	   vo.setEntity(rs.getString("entity"));
		    	   vo.setSubentity(rs.getString("subentity"));
		    	   vo.setThevalue(rs.getString("thevalue"));
		    	   Date timestamp = rs.getTimestamp("collecttime");
		    	   Calendar date=Calendar.getInstance();
		    	   date.setTime(timestamp);
		    	   vo.setCollecttime(date);
		    	   vo.setUnit(rs.getString("unit"));
		    	   list.add(vo);
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
	       return list;
	   }
}
