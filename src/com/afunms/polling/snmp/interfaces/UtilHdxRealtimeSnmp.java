package com.afunms.polling.snmp.interfaces;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.Arith;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.model.HostNode;
public class UtilHdxRealtimeSnmp extends SnmpMonitor {
	public UtilHdxRealtimeSnmp() {
	}
   public void collectData(Node node,MonitoredItem item){
	   
   }
   public void collectData(HostNode node){ 
   }
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
			Hashtable returnHash=new Hashtable();
			Vector utilhdxVector = new Vector();
			Vector allutilhdxVector = new Vector();
			Vector utilhdxpercVector = new Vector();
			AllUtilHdx allutilhdx = new AllUtilHdx();
			Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
			Hashtable hash=ShareData.getRealOctetsdata(host.getIpAddress());
			long allInOctetsSpeed=0;
			long allOutOctetsSpeed=0;
			long allOctetsSpeed=0;
			Calendar date=Calendar.getInstance();
			try {
				Interfacecollectdata interfacedata=null;
				UtilHdx utilhdx=new UtilHdx();
				UtilHdxPerc utilhdxperc=new UtilHdxPerc();
			  try{
				
				//取得轮询间隔时间
				TaskXml taskxml=new TaskXml();
				Task task=taskxml.GetXml("netcollecttask");
				Hashtable<String, Comparable> realoctetsHash = new Hashtable();
				if (hash==null)hash=new Hashtable();
				String[] oids1=                
					 new String[] {     
					"1.3.6.1.2.1.2.2.1.1",	//index
					"1.3.6.1.2.1.2.2.1.10",  //ifInOctets 1        
					"1.3.6.1.2.1.2.2.1.16", //ifOutOctets 2		
					"1.3.6.1.2.1.2.2.1.5"//ifSpeed
					};				 
				final String[] desc={"index","ifInOctets","ifOutOctets","ifSpeed"};
				String[][] valueArray = null;   	  
				try {
					valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids1, host.getSnmpversion(), 3, 1000*30);
				} catch(Exception e){
				}
				if(valueArray!= null){
				for(int i=0;i<valueArray.length;i++){																		
					if(valueArray[i][0] == null)
						continue;
					String sIndex=valueArray[i][0].toString();
						for(int j=0;j<4;j++){																														
							if(valueArray[i][j]!=null){
								String sValue=valueArray[i][j];
								interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(host.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Interface");
								interfacedata.setEntity(desc[j]);
								interfacedata.setSubentity(sIndex);
								if(j==0){//索引
									interfacedata.setThevalue(sValue);
								}
								else{
									interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/1024));
									//interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)));
								}
								
								Calendar cal=(Calendar)hash.get("collecttime");//上次采集时间
								long timeInMillis=0;
								if(cal!=null)timeInMillis=cal.getTimeInMillis();
								long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;																			
								
								if(j==1 || j==2){
									utilhdx=new UtilHdx();
									utilhdx.setIpaddress(host.getIpAddress());
									utilhdx.setCollecttime(date);
									utilhdx.setCategory("Interface");
									utilhdx.setSubentity(sIndex);
									utilhdx.setRestype("dynamic");
									utilhdx.setUnit("kb/s");	
									
									utilhdxperc=new UtilHdxPerc();
									utilhdxperc.setIpaddress(host.getIpAddress());
									utilhdxperc.setCollecttime(date);
									utilhdxperc.setCategory("Interface");
									utilhdxperc.setSubentity(sIndex);
									utilhdxperc.setRestype("dynamic");
									utilhdxperc.setUnit("%");	
									
									String chnameBand="";
									if(j==1){
										utilhdx.setEntity("InBandwidthUtilHdx");
										utilhdxperc.setEntity("InBandwidthUtilHdxPerc");
										chnameBand="入口";
									}
									if(j==2){
										utilhdx.setEntity("OutBandwidthUtilHdx");
										utilhdxperc.setEntity("OutBandwidthUtilHdxPerc");
										chnameBand="出口";
										}
									utilhdx.setChname(sIndex+"端口"+chnameBand+"流速");
									utilhdxperc.setChname(sIndex+"端口"+chnameBand+"带宽利用率");	
									
									long currentOctets=Long.parseLong(sValue)/1024;//当前量KB
									long lastOctets=0;
									long octetsBetween=0;
									long ifspeed=0;
									long bandwidth=0;
									double per=0;//带宽利用率
									String lastvalue="";
									if(null!=hash.get(desc[j]+":"+sIndex)){
										lastvalue=hash.get(desc[j]+":"+sIndex).toString();
									}
									if(null!=lastvalue&&!"".endsWith(lastvalue)){
										lastOctets=Long.parseLong(lastvalue);
									}
									
									if(null!=valueArray[i][3]){
										bandwidth=Long.parseLong(valueArray[i][3]);
									}
									
									if(longinterval>0){
										if(currentOctets<lastOctets){
											currentOctets=currentOctets+4294967296L/1024;
										} 
										octetsBetween=currentOctets-lastOctets;//现流量-前流量	
										ifspeed=octetsBetween/longinterval;//计算端口速率
										//SysLogger.info("sIndex:"+sIndex+"  longinterval:"+longinterval+" octetsBetween:"+octetsBetween+" ifspeed:"+ifspeed);
										if(j==1){
											allInOctetsSpeed=allInOctetsSpeed+ifspeed;//入口综合流速
										}else if(j==2){
											allOutOctetsSpeed=allOutOctetsSpeed+ifspeed;//出口综合流速
										}
										allOctetsSpeed=allOctetsSpeed+ifspeed;//综合流速
										if(bandwidth>0){
											//SysLogger.info("octetsBetween is "+octetsBetween+" bandwidth"+bandwidth);

											per=Arith.div(ifspeed*8*100*1024,bandwidth);
											//SysLogger.info("sIndex:"+sIndex+" ifspeed:"+ifspeed*8+" bandwidth:"+bandwidth+"      per is "+per);
										}
									}
									DecimalFormat df=new DecimalFormat("#.##");
									utilhdx.setThevalue(df.format(ifspeed*8));	
									utilhdxVector.addElement(utilhdx);
									
									utilhdxperc.setThevalue(df.format(per));
									utilhdxpercVector.addElement(utilhdxperc);
								} 
								realoctetsHash.put(desc[j]+":"+sIndex,interfacedata.getThevalue());
								realoctetsHash.put("collecttime",date);	
							} 
						} 
				}
				}
				
				ShareData.setRealOctetsdata(host.getIpAddress(),realoctetsHash);				
			}catch(Exception e){e.printStackTrace();}
			}catch(Exception e){
			}
			AllUtilHdx allInutilhdx = new AllUtilHdx();
			allInutilhdx = new AllUtilHdx();
			allInutilhdx.setIpaddress(host.getIpAddress());
			allInutilhdx.setCollecttime(date);
			allInutilhdx.setCategory("Interface");
			allInutilhdx.setEntity("AllInBandwidthUtilHdx");
			allInutilhdx.setSubentity("AllInBandwidthUtilHdx");
			allInutilhdx.setRestype("dynamic");
			allInutilhdx.setUnit("kb/秒");	
			allInutilhdx.setChname("入口流速");
			allInutilhdx.setThevalue(Long.toString(allInOctetsSpeed*8));	
			allutilhdxVector.addElement(allInutilhdx);
			
			
			AllUtilHdx alloututilhdx = new AllUtilHdx();
			alloututilhdx = new AllUtilHdx();
			alloututilhdx.setIpaddress(host.getIpAddress());
			alloututilhdx.setCollecttime(date);
			alloututilhdx.setCategory("Interface");
			alloututilhdx.setEntity("AllOutBandwidthUtilHdx");
			alloututilhdx.setSubentity("AllOutBandwidthUtilHdx");
			alloututilhdx.setRestype("dynamic");
			alloututilhdx.setUnit("kb/秒");
			alloututilhdx.setChname("出口流速");	
			alloututilhdx.setThevalue(Long.toString(allOutOctetsSpeed*8));	
			allutilhdxVector.addElement(alloututilhdx);
			
			
			allutilhdx = new AllUtilHdx();
			allutilhdx.setIpaddress(host.getIpAddress());
			allutilhdx.setCollecttime(date);
			allutilhdx.setCategory("Interface");
			allutilhdx.setEntity("AllBandwidthUtilHdx");
			allutilhdx.setSubentity("AllBandwidthUtilHdx");
			allutilhdx.setRestype("dynamic");
			allutilhdx.setUnit("kb/秒");	
			allutilhdx.setChname("综合流速");
			allutilhdx.setThevalue(Long.toString(allOctetsSpeed*8));	
			allutilhdxVector.addElement(allutilhdx);
	    returnHash.put("allutilhdx",allutilhdxVector);
	    returnHash.put("utilhdxperc",utilhdxpercVector);
	    returnHash.put("utilhdx",utilhdxVector);	
	    
	    return returnHash;
	}
	
	public int getInterval(float d,String t){
		int interval=0;
		  if(t.equals("d"))
			 interval =(int) d*24*60*60; //天数
		  else if(t.equals("h"))
			 interval =(int) d*60*60;    //小时
		  else if(t.equals("m"))
			 interval = (int)d*60;       //分钟
		else if(t.equals("s"))
					 interval =(int) d;       //秒
		return interval;
	}
}





