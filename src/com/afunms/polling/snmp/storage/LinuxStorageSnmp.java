package com.afunms.polling.snmp.storage;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.Arith;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Storagecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempstorageRttosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinuxStorageSnmp extends SnmpMonitor {
	
	private static Hashtable storage_Type = null;
	static {
		storage_Type = new Hashtable();
		storage_Type.put("1.3.6.1.2.1.25.2.1.1", "����");
		storage_Type.put("1.3.6.1.2.1.25.2.1.2", "�����ڴ�");
		storage_Type.put("1.3.6.1.2.1.25.2.1.3", "�����ڴ�");
		storage_Type.put("1.3.6.1.2.1.25.2.1.4", "Ӳ��");
		storage_Type.put("1.3.6.1.2.1.25.2.1.5", "�ƶ�Ӳ��");
		storage_Type.put("1.3.6.1.2.1.25.2.1.6", "����");
		storage_Type.put("1.3.6.1.2.1.25.2.1.7", "����");
		storage_Type.put("1.3.6.1.2.1.25.2.1.8", "�ڴ���");
	};
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public LinuxStorageSnmp() {
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
		Vector storageVector=new Vector();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		
		try {
			Storagecollectdata storagedata=null;
			Calendar date=Calendar.getInstance();
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			//-------------------------------------------------------------------------------------------Storage start
			  try{
			  String[] oids =                
				  new String[] {               
					  "1.3.6.1.2.1.25.2.3.1.1",  //hrStorageIndex
					  "1.3.6.1.2.1.25.2.3.1.2",  //hrStorageType
					  "1.3.6.1.2.1.25.2.3.1.3",    //hrStorageDescr
					  "1.3.6.1.2.1.25.2.3.1.4",    //hrStorageAllocationUnits
					  "1.3.6.1.2.1.25.2.3.1.5" };   //hrStorageSize

			  String[][] valueArray = null;   	  
				try {
					valueArray = SnmpUtils.getTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
				} catch(Exception e){
					valueArray = null;
					//SysLogger.error(node.getIpAddress() + "_WindowsSnmp");
				}	
				for(int i=0;i<valueArray.length;i++){
					storagedata = new Storagecollectdata();
					String storageindex = valueArray[i][0];
					String type = valueArray[i][1];
					String name = valueArray[i][2];
					String byteunit = valueArray[i][3];
					String cap = valueArray[i][4];
					int allsize=0;
					try{
						allsize = Integer.parseInt(cap.trim());
					}catch(Exception e){
						
					}
					
					float size=0.0f;
					  try{
						  size=allsize*Long.parseLong(byteunit)*1.0f/1024/1024;
					  }catch(Exception e){
						  
					  }
					  String unit = ""; 
					  if(size>=1024.0f){
						  size=size/1024;
						  //diskdata.setUnit("G");
						  unit = "G"; 
					  }
					  else{
					  	//diskdata.setUnit("M");
					  	unit = "M"; 
					  }
					storagedata.setStorageindex(storageindex);
					storagedata.setIpaddress(node.getIpAddress());
					storagedata.setName(name);
					storagedata.setCap(Arith.floatToStr(size+"", 1, 0)+unit);
					//SysLogger.info("Type====="+type);
					try{
						storagedata.setType((String)storage_Type.get(type));
					}catch(Exception e){
						
					}
					storageVector.addElement(storagedata);
					//SysLogger.info(name+"######"+storageindex+"######"+(String)storage_Type.get(type)+"######"+size+unit);					    
				}  
		   }catch(Exception e){
				  e.printStackTrace();
		   }			   
		   //-------------------------------------------------------------------------------------------Storage end
			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		ipAllData.put("storage",storageVector);
	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
	    returnHash.put("storage", storageVector);
	    storageVector=null;
	    ipAllData=null;
	    
	    
	    //�Ѳɼ��������sql
	    HostDatatempstorageRttosql totempsql=new HostDatatempstorageRttosql();
	    totempsql.CreateResultTosql(returnHash, node);
	    return returnHash;
	}
}





