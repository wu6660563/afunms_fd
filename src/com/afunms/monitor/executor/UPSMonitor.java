/**
 * <p>Description:collect ups status</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project ��³ʯ��
 * @date 2007-01-24
 */

package com.afunms.monitor.executor;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.inform.model.Alarm;
import com.afunms.monitor.item.*;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.monitor.executor.base.*;
import com.afunms.polling.node.UPSNode;
import com.afunms.polling.base.*;
import com.afunms.topology.model.HostNode;
import com.afunms.inform.dao.MachineRoomExceptionDao;

public class UPSMonitor extends SnmpMonitor implements MonitorInterface
{      
    public UPSMonitor()
    {       	
    }
    public void collectData(HostNode node){
 	   
    }
    public Hashtable collect_Data(HostNode node){
 	   return null;
    }
    public void collectData(Node node,MonitoredItem monitoredItem)
    {  
    	UPSNode upsNode = (UPSNode)node;
    	UPSItem item = (UPSItem)monitoredItem;
    	item.setSingleResult(-1); //��Ϊû��������Ҫ�������ݿ�
    	
    	String[] inputOids = new String[]{"1.3.6.1.4.1.705.1.6.2"};
    	String[] outputOids = new String[]{"1.3.6.1.4.1.705.1.7.2"};
    	
    	String[][] inputPhase = null;   
    	String[][] outputPhase = null;
    	try
    	{
    		/**
    		 * ע�⣺mge ups��table��һ��Ľڵ�������,ȡ������input table��18�У�1��
    		 * output table��15�У�1��
    		 */
    		inputPhase = snmp.getTableData(upsNode.getIpAddress(),upsNode.getCommunity(),inputOids);
    		outputPhase = snmp.getTableData(upsNode.getIpAddress(),upsNode.getCommunity(),outputOids);
    	}
    	catch(Exception e)
    	{
    		SysLogger.error(upsNode.getIpAddress() + "_UPSMonitor");
    	}   
    	if(inputPhase==null||outputPhase==null||outputPhase.length==0||inputPhase.length==0)
    		return;
	    	
    	List list = new ArrayList(6);
    	for(int i=0;i<3;i++) //ֻ��������λ
    	{
    		UPSPhase phase = new UPSPhase();
    		phase.setIo(1);
    		phase.setIndex(Integer.parseInt(inputPhase[i][0]));
    		phase.setVoltage(Integer.parseInt(inputPhase[i + 3][0]) / 10);
    		phase.setFrequency(Integer.parseInt(inputPhase[i + 6][0]) / 10);
    		phase.setCurrent(Integer.parseInt(inputPhase[i + 15][0]) / 10);
    		list.add(phase);
    	}
    	int upsLoad = 0;
    	for(int i=0;i<3;i++) 
    	{
    		UPSPhase phase = new UPSPhase();
    		phase.setIo(0);
    		phase.setIndex(Integer.parseInt(outputPhase[i][0]));
    		phase.setVoltage(Integer.parseInt(outputPhase[i + 3][0]) / 10 );
    		phase.setFrequency(Integer.parseInt(outputPhase[i + 6][0]) / 10);
    		phase.setCurrent(Integer.parseInt(outputPhase[i + 12][0]) / 10);
    		phase.setLoad(phase.getVoltage() * phase.getCurrent()); //��λ��W
    		upsLoad += phase.getLoad();
    		
    		list.add(phase);
    	}
    	for(int i=3;i<6;i++) 
    	{
    		UPSPhase phase = (UPSPhase)list.get(i);
    		phase.setLoadPercent( phase.getLoad()*100 / upsLoad);
    	}    	
    	
    	item.setPhasesList(list);
    	item.setUpsLoad(upsLoad); //ups�������
    	
    	String temp = null;
        //------��UPS�����豸������(%)-----------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.4.21.0");
    	item.setDevicesNumber(Integer.parseInt(temp));
        //------������(%)-----------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.2.0");
    	item.setBatteryLevel(Integer.parseInt(temp));
        //------������ṩ�ĵ�ѹ(dV)------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.5.0");
    	item.setBatteryVoltage(Integer.parseInt(temp) / 10); 
        //------(�е�ϵ��)�����֧�ֵ�ʱ��(s)------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.1.0"); 
    	item.setBatteryTime(Integer.parseInt(temp) / 60); //��λ����
        //------�������½�������ʱ��UPS׼���ػ�------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.4.8.0"); 
    	item.setLowBatteryLevel(Integer.parseInt(temp)); 
        //------����й���------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.9.0");
    	item.setBatteryFault(str2Boolean(temp)); 
        //------��س�����й���------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.15.0");
    	item.setBatteryChargerFault(str2Boolean(temp)); 
        //------��ص͵���------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.5.16.0");
    	item.setBatteryLow(str2Boolean(temp));     	    	    	
        //------�������------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.7.3.0");
    	item.setOutputOnBattery(str2Boolean(temp));
        //------��·����------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.7.4.0");
    	item.setOutputOnByPass(str2Boolean(temp));
        //------�����------------
    	temp = snmp.getMibValue(upsNode.getIpAddress(),upsNode.getCommunity(),"1.3.6.1.4.1.705.1.4.12.0");
    	item.setUpsRatedLoad(Integer.parseInt(temp));  
    	//-------UPS����---------------
    	if(item.getUpsLoad() > item.getUpsRatedLoad())  
    	   item.setOverLoad(true);
    }
    
    public void analyseData(Node node,MonitoredItem monitoredItem)
    {    
    	UPSNode upsNode = (UPSNode)node;
    	UPSItem item = (UPSItem)monitoredItem;
    	
    	if(item.isOverLoad())    		
    		addAlarmMsg(upsNode,"UPS�������");
		if(item.isBatteryLow())
			addAlarmMsg(upsNode,"UPS��ص͵���");
		if(item.isBatteryChargerFault())
			addAlarmMsg(upsNode,"UPS��س�����й���");
		if(item.isBatteryFault())
			addAlarmMsg(upsNode,"UPS����й���");
        if(item.isOutputOnBattery())
        	addAlarmMsg(upsNode,"�е�ϵ�,UPS�������");        
        if(item.isOutputOnByPass())	
        	addAlarmMsg(upsNode,"UPS�й���,UPS��·����");
        
        if(upsNode.getAlarmMessage().size()!=0)
        {
            MachineRoomExceptionDao dao = new MachineRoomExceptionDao();
            dao.insert(upsNode.getAlarmMessage());
        }
    } 
    
    private boolean str2Boolean(String value)
    {
    	if("2".equals(value))
    	   return false;
    	else
    	   return true;	
    }
    
    private void addAlarmMsg(UPSNode upsNode,String message)
    {
    	SysLogger.info("UPS�����쳣��=" + message);
    	
    	upsNode.setAlarm(true);
    	Alarm vo = new Alarm();
		vo.setIpAddress(upsNode.getIpAddress());
		vo.setLevel(3);  //����UPS���쳣������߱�������
		vo.setMessage(message);
		vo.setLogTime(SysUtil.getCurrentTime());
		vo.setCategory(upsNode.getCategory());		
		upsNode.getAlarmMessage().add(vo);
		
		if("�е�ϵ�,UPS�������".equals(message))
		   upsNode.setStatus(5);
		else
		   upsNode.setStatus(4);	
    }
}