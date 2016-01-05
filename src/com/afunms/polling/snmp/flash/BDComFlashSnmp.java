package com.afunms.polling.snmp.flash;


/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Flashcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetflashResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BDComFlashSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public BDComFlashSnmp() {
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
		Vector flashVector=new Vector();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if(node == null)return null;
		
		try {
			Calendar date=Calendar.getInstance();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			  try {
				//-------------------------------------------------------------------------------------------���� start
		   		  String temp = "0";
//		   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
//		   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
		   			String[][] valueArray = null;
		   			String[] oids =                
						  new String[] {               
							"1.3.6.1.4.1.3320.2.10.4",//bdflashCard
							"1.3.6.1.4.1.3320.2.10.1",//bdflashSize
							"1.3.6.1.4.1.3320.2.10.2"//bdflashFree
		   			};
		   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
		   			int allvalue=0;
		   			int flag = 0;
					if(valueArray != null){
					   	  for(int i=0;i<valueArray.length;i++)
					   	  {
					   		String cardname = valueArray[i][0];
					   		String allflashvalue = valueArray[i][1];
					   		String freevalue = valueArray[i][2];
					   		String index = valueArray[i][3];
					   		float value=0.0f;
					   		String usedperc = "0";
					   		if(Long.parseLong(allflashvalue) > 0)
					   			value = (Long.parseLong(allflashvalue)-Long.parseLong(freevalue))*100/(Long.parseLong(allflashvalue));
					   		
							if( value >0){
								int intvalue = Math.round(value); 
								flag = flag +1;
						   		List alist = new ArrayList();
						   		alist.add("");
						   		alist.add(usedperc);
						   		//�ڴ�
						   		//memoryList.add(alist);	
						   		Flashcollectdata flashcollectdata = new Flashcollectdata();
						   		flashcollectdata.setIpaddress(node.getIpAddress());
						   		flashcollectdata.setCollecttime(date);
						   		flashcollectdata.setCategory("Flash");
						   		flashcollectdata.setEntity("Utilization");
						   		flashcollectdata.setSubentity(cardname);
						   		flashcollectdata.setRestype("dynamic");
						   		flashcollectdata.setUnit("");
						   		flashcollectdata.setThevalue(intvalue+"");
								SysLogger.info(node.getIpAddress()+" "+cardname+" "+index +" ���棺 "+Integer.parseInt(intvalue+""));
								flashVector.addElement(flashcollectdata);
						   		
							}
					   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
					   	  }
					}
		   		  //}
		   		//memoryVector.add(memoryList);
		   	  }
		   	  catch(Exception e)
		   	  {
		   	  }	   	  
		   	  //-------------------------------------------------------------------------------------------���� end
		}
		catch(Exception e){
			//returnHash=null;
			//e.printStackTrace();
			//return null;
		}
		
		Hashtable ipAllData = new Hashtable();
		try{
			ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
		}catch(Exception e){
			
		}
		if(ipAllData == null)ipAllData = new Hashtable();
		if (flashVector != null && flashVector.size()>0)ipAllData.put("flash",flashVector);
	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
	    returnHash.put("flash",flashVector);
	    ipAllData=null;
	    flashVector=null;
	    
	    
	    
	    
	    //�Ѳɼ��������sql
	    NetflashResultTosql tosql=new NetflashResultTosql();
	    tosql.CreateResultTosql(returnHash, node.getIpAddress());
	    
	    return returnHash;
	}
}





