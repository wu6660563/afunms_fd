package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.NetHostMemoryRtsql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinuxPhysicalMemorySnmp extends SnmpMonitor {
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public LinuxPhysicalMemorySnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		Hashtable returnHash=new Hashtable();
		Vector memoryVector=new Vector();
		String memerySize = "";
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		
		try {
			Memorycollectdata memorydata=new Memorycollectdata();
			Calendar date=Calendar.getInstance();
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			//-------------------------------------------------------------------------------------------memory start			
				try{
				
					String[] oids = { 
							"1.3.6.1.2.1.25.2.3.1.2", 
							"1.3.6.1.2.1.25.2.3.1.5", 
							"1.3.6.1.2.1.25.2.3.1.6" 
						};
					
					String[][] results = null;
					
					Float result1 = null;
					
					Float result2 = null;
					
					try {
						results = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000*30);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (results != null) {
						for (int i = 0; i < results.length; ++i) {
							String type = results[i][0];
				            if ("1.3.6.1.2.1.25.2.1.2".equals(type)) {
				            	float size = Float.parseFloat(results[i][1]);
				            	//System.out.println(size);
				            	
				            	memerySize = size+"";
				            	float used = Float.parseFloat(results[i][2]);
				            	//System.out.println(used);
				            	result1 = Float.valueOf(100.0F * used / size);
				            }
				            
				            if ("1.3.6.1.2.1.25.2.1.3".equals(type)) {
				            	float size = Float.parseFloat(results[i][1]);
				            	//System.out.println(size);
				            	float used = Float.parseFloat(results[i][2]);
				            	//System.out.println(used);
				            	result2 = Float.valueOf(100.0F * used / size);
				            }
						}
					}
					
					
					Vector memeryVector = new Vector();
					
					date=Calendar.getInstance();
					//Memorycollectdata memorydata = null;
					if(result1!=null){
						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Utilization");
						memorydata.setSubentity("PhysicalMemory");
						memorydata.setRestype("dynamic");
						memorydata.setUnit("%");
						memorydata.setThevalue(Float.toString(result1));
						
						memeryVector.add(memorydata);
					}
					
					if(result2 != null){
						memorydata=new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Utilization");
						memorydata.setSubentity("VirtualMemory");
						memorydata.setRestype("dynamic");
						memorydata.setUnit("%");
						memorydata.setThevalue(Float.toString(result2));
						
						memeryVector.add(memorydata);
					}
				}
				catch(Exception e){
					//System.out.println(e.getMessage());
					//e.printStackTrace();
				}
				//-------------------------------------------------------------------------------------------memory end
			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		Vector toAddVector = new Vector();
		Hashtable formerHash = new Hashtable();
		if(ipAllData.containsKey("memory")){
			Vector formerMemoryVector = (Vector)ipAllData.get("memory");
			if(formerMemoryVector != null && formerMemoryVector.size()>0){
				for(int i=0;i<formerMemoryVector.size();i++){
					Memorycollectdata memorydata=(Memorycollectdata)formerMemoryVector.get(i);
					formerHash.put(memorydata.getSubentity()+":"+memorydata.getEntity(), memorydata);
				}
			}
			
		}
		if(memoryVector != null && memoryVector.size()>0){
			for(int j=0;j<memoryVector.size();j++){
				Memorycollectdata memorydata=(Memorycollectdata)memoryVector.get(j);
				if(formerHash.containsKey(memorydata.getSubentity()+":"+memorydata.getEntity())){
					//若存在,则要用新的替换原来的数据
					//SysLogger.info("存在   "+memorydata.getSubentity()+":"+memorydata.getEntity());
					formerHash.remove(memorydata.getSubentity()+":"+memorydata.getEntity());
					formerHash.put(memorydata.getSubentity()+":"+memorydata.getEntity(), memorydata);
				}else{
					//若不存在,在直接加入
					//SysLogger.info("添加   "+memorydata.getSubentity()+":"+memorydata.getEntity());
					toAddVector.add(memorydata);
				}
			}
		}
		if(formerHash.elements() != null && formerHash.size()>0){
			for(Enumeration enumeration = formerHash.keys(); enumeration.hasMoreElements();){
				String keys = (String)enumeration.nextElement();
				Memorycollectdata memorydata=(Memorycollectdata)formerHash.get(keys);
				//SysLogger.info("添加   "+memorydata.getSubentity()+":"+memorydata.getEntity());
				toAddVector.add(memorydata);
			}
		}
		ipAllData.put("memory",toAddVector);
	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	    returnHash.put("memory", toAddVector);
	    
	    
	    ipAllData=null;
	    memoryVector=null;
	    toAddVector=null;
	    //把采集结果生成sql
	    HostPhysicalMemoryResulttosql  tosql=new HostPhysicalMemoryResulttosql();
	    tosql.CreateResultTosql(returnHash, host.getIpAddress());
	    NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
	    totempsql.CreateResultTosql(returnHash, host);
	    
	    return returnHash;
	}
}





