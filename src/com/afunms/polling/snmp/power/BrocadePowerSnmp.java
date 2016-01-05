package com.afunms.polling.snmp.power;

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
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetDatatemppowerRtosql;
import com.gatherResulttosql.NetpowerResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BrocadePowerSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public BrocadePowerSnmp() {
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
		Vector powerVector=new Vector();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if(node == null)return null;
		try {
			Interfacecollectdata interfacedata = new Interfacecollectdata();
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
			  try {
				  //-------------------------------------------------------------------------------------------电源 start
		   		  String temp = "0";
		   		  if(node.getSysOid().startsWith("1.3.6.1.4.1.1588.2.1.1.")){
		   			String[][] valueArray = null;
//		   			String[] oids =                
//						  new String[] {               
//							"1.3.6.1.4.1.9.9.13.1.5.1.2",//描述
//							"1.3.6.1.4.1.9.9.13.1.5.1.3"//状态
//		   			};
		   			String[] oids =                
						  new String[] {               
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.1",//swSensorIndex   温度、风扇、电源
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.2",//swSensorType    类别  temperature(1),fan(2),power-supply(3)
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.3",//swSensorStatus  状态
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.4",//swSensorValue   值
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.5",//swSensorInfo
		   			};
		   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
		   			int flag = 0;
					if(valueArray != null){
					   	  for(int i=0;i<valueArray.length;i++)
					   	  {
					   		String swSensorIndex = valueArray[i][0];
					   		String swSensorType = valueArray[i][1];
					   		String swSensorStatus = valueArray[i][2];
					   		String swSensorValue = valueArray[i][3];
					   		String swSensorInfo = valueArray[i][4];
						   	String index = valueArray[i][5];
					   		int value=0;
							if(swSensorValue != null){
								value=Integer.parseInt(swSensorValue);
								if(value > 0 && "3".equals(swSensorType)){// 电源
									//powerList.add(alist);				   		
									if(swSensorStatus.equals("1")){//unknown
										swSensorStatus = "未知";
									}else if(swSensorStatus.equals("2")){//faulty
										swSensorStatus = "错误";
									}else if(swSensorStatus.equals("3")){//below-min
										swSensorStatus = "低于最小值";
									}else if(swSensorStatus.equals("4")){//nominal
										swSensorStatus = "正常";
									}else if(swSensorStatus.equals("5")){//above-max
										swSensorStatus = "超过最大值";
									}else if(swSensorStatus.equals("6")){//absent
										swSensorStatus = "缺失";
									}
							   		  interfacedata = new Interfacecollectdata();
							   		  interfacedata.setIpaddress(node.getIpAddress());
							   		  interfacedata.setCollecttime(date);
							   		  interfacedata.setCategory("Power");
							   		  interfacedata.setEntity(index);
							   		  interfacedata.setSubentity(swSensorInfo);
							   		  interfacedata.setRestype("dynamic");
							   		  interfacedata.setUnit("");		
							   		  interfacedata.setThevalue(swSensorValue);
							   		  interfacedata.setBak(swSensorStatus);
									  SysLogger.info(node.getIpAddress()+" index:"+swSensorIndex+"  描述:"+swSensorInfo+" 电源状态： "+swSensorValue);
									  powerVector.addElement(interfacedata);	
								}
							}
					   	  }
					}
		   		  } 
				  //powerVector.add(powerList);
		   	  }
		   	  catch(Exception e)
		   	  {
		   	  }	   	  
		   	  //-------------------------------------------------------------------------------------------电源 end
			}catch(Exception e){
			}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		ipAllData.put("power",powerVector);
	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
	    returnHash.put("power", powerVector);
	    
	    powerVector=null;
	    ipAllData=null;
	    
	    
	    //把采集结果生成sql
	    NetpowerResultTosql tosql=new NetpowerResultTosql();
	    tosql.CreateResultTosql(returnHash,node.getIpAddress());
	    NetDatatemppowerRtosql totempsql=new NetDatatemppowerRtosql();
	    totempsql.CreateResultTosql(returnHash, node);
	    return returnHash;
	}
}





