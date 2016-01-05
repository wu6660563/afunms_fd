/**
 * ϵͳ����,������ҳ
 * ���ɺ�
 */

package com.afunms.inform.dao;

import java.util.Iterator;
import java.util.List;
import java.sql.ResultSet;

import com.afunms.polling.*;
import com.afunms.polling.node.*;
import com.afunms.common.util.*;
import com.afunms.inform.model.SystemSnap;
/**
 * ȡ״̬�ĺ������ص���home.css�ж����CSS����,��������ʾ״̬
 */
public class SystemSnapDao
{
	private DBManager conn;
	
	public SystemSnapDao()
	{
		try
		{
		    conn = new DBManager();
		}
		catch(Exception e)
		{
			conn = null;
		}
	}
    public void close()
    {
    	conn.close();
    }
	/**
	 * �����豸��״̬
	 */
    private String getNetworkStatus()
    {
         List netList = PollingEngine.getInstance().getNodeList();
         boolean hasError = false; //��û�ػ���
         boolean hasAlarm = false; //��û������
         for(int i=0;i<netList.size();i++)
         {
        	 if(!(netList.get(i) instanceof Host)) continue;        	 
        	 Host host = (Host)netList.get(i);
        	 if(host.getCategory()>3) continue;

        	 if(host.getStatus()==3)
        	 {
   	             hasError =true; //�豸" host.getIpAddress() Ping��ͨ��<br>");
   	             break;
        	 }
   	         else if(host.isAlarm())
        		hasAlarm = true;
   	         else
   	         {
   	             Iterator it = host.getInterfaceHash().values().iterator();
   	             while(it.hasNext())
   	             {
   	                 IfEntity ifObj = (IfEntity)it.next();
   	                 if(ifObj.getPort().equals("")) continue;
   	                 if(ifObj.getDiscards()>2 || ifObj.getErrors()>2) 
   	                	 hasError = true; //�豸host.getIpAddress()�Ľӿ�ifObj.getPort()������>3%;
   	             }
   	         } 
         }
         if(hasError) //ȡ������ߵ�
        	return "network_red"; //"status3.png";
         else if(hasAlarm)
        	return "network_brown"; //"status2.png";
         else
        	return "network"; //"status1.png"; 
    }
    
	/**
	 * ��������״̬
	 */
    private String getServerStatus()
    {
         List netList = PollingEngine.getInstance().getNodeList();
         boolean hasError = false; //��û�ػ���
         boolean hasAlarm = false; //��û������
         for(int i=0;i<netList.size();i++)
         {
        	 if(!(netList.get(i) instanceof Host)) continue;        	 
        	 Host host = (Host)netList.get(i);
        	 if(host.getCategory()!=4) continue;

        	 if(host.isAlarm())
         		hasAlarm = true;        	 
        	 if(host.getStatus()==3)
        	 {
        		 hasError = true;
        		 break;
        	 }
         }
         if(hasError) //ȡ������ߵ�
        	return "server_red"; //"status3.png";
         else if(hasAlarm)
        	return "server_brown"; //"status2.png";
         else
        	return "server"; //"status1.png"; 
    }   
    
    /**
     * ���ؿͻ���״̬
     */
    private String getClientStatus()
    {    	
    	long startIp = NetworkUtil.ip2long("10.110.1.0");
    	long endIp = NetworkUtil.ip2long("10.110.63.0");    	
    	String result = "governclient"; //"status1.png";
    	try
    	{
    		StringBuffer sql = new StringBuffer(200);
    		sql.append("select * from nms_ip_change where ip_long>");
    		sql.append(startIp);
    		sql.append(" and ip_long<");
    		sql.append(endIp);
    		sql.append(" and substring(log_time,1,10)='");
    		sql.append(SysUtil.getCurrentDate());
    		sql.append("'");
    		ResultSet rs = conn.executeQuery(sql.toString());    		
    		if(rs.next())
    			result = "governclient_red"; //"status2.png";
    	}
    	catch(Exception e)
    	{
    		
    	}
    	return result;
    }
    
    /**
     * ������״̬
     */
    private String getVirusStatus()
    { 	
    	String result = "virus"; 
    	try
    	{
    		ResultSet rs = conn.executeQuery("select machine_ip,count(virus_file) files from nms_symantec" 
    				+ " where substring(begintime,1,10)='" + SysUtil.getCurrentDate() + "' group by machine_ip" 
    				+ " order by files desc");
    		while(rs.next())
    		{
    			if(rs.getInt("files")>5000)
    				result = "virus_brown"; //"status2.png";
    			if(rs.getInt("files")>10000)
                {
                	result = "virus_red"; //"status3.png";
                	break;
                }    			
    		}	
    	}
    	catch(Exception e)
    	{}
    	return result;
    }
    
    private String getInternetStatus()
    {
    	int result = 0;
    	try
    	{
    		ResultSet rs = conn.executeQuery("select status from test_status where id=2");
    		if(rs.next());
    			result = rs.getInt("status");
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	if(result == 1)
    	   return "internetstatus";
    	else
    	   return "internetstatus_red";
    }

    private String getOaStatus()
    {
    	int result = 0;
    	try
    	{
    		ResultSet rs = conn.executeQuery("select status from test_status where id=1");
    		if(rs.next());
    			result = rs.getInt("status");
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	if(result == 1)
    		return "oastatus";
    	else
    		return "oastatus_red";
    }
    
    private String getDatabaseStatus()
    {
    	return "database";
    }
    
    private String getLinksStatus()
    {
    	return "linkstatus";
    }
    
    private String getDoorSystemStatus()
    {
    	return "doorsystem";
    }
    
    private String getRoomStatus()
    {
    	return "roomstatus";
    }
    
    //============================================
    public SystemSnap getSystemSnap()
    {
    	SystemSnap snap = new SystemSnap();
    	snap.setNetworkClass(getNetworkStatus());
    	snap.setServerClass(getServerStatus());
    	snap.setGovernClientClass(getClientStatus());
    	snap.setVirusClass(getVirusStatus());
    	snap.setInternetClass(getInternetStatus());
    	snap.setOaStatusClass(getOaStatus());
    	snap.setDatabaseClass(getDatabaseStatus());
    	snap.setLinksClass(getLinksStatus());
    	snap.setDoorSystemClass(getDoorSystemStatus());
    	snap.setRoomStatusClass(getRoomStatus());
    	close(); //�ر����ݿ�����

    	//1 ����
    	if("network".equals(snap.getNetworkClass()))
    		snap.urlsTbl.put("network", "/topology/network/index.jsp?menu=0201");
    	else
    		snap.urlsTbl.put("network", "/inform/alarm/networksnap.jsp");
    	//2 ������
    	if("server".equals(snap.getServerClass()))
    		snap.urlsTbl.put("server", "/topology/server/index.jsp?menu=0202");
    	else
    		snap.urlsTbl.put("server", "/inform/alarm/serversnap.jsp");
    	//3 ���ؿͻ���
    	if("governclient".equals(snap.getGovernClientClass()))
    		snap.urlsTbl.put("governclient", "/ipmac.do?action=list&jp=1&menu=0401");
    	else
    		snap.urlsTbl.put("governclient", "/ipmac.do?action=list_msg&jp=1&menu=0404");
    	//4 ����,�����ж�,��ͬһ��ҳ--ע�����Ĳ���,����˵��䶯,���ҲҪ��
    	snap.urlsTbl.put("virus","/inform.do?action=virus_jsp&menu=0502");
    	//5 Internet״̬,����ҳ
    	snap.urlsTbl.put("internetstatus", "/common/qlsh_home.jsp");
    	//6 oa ״̬,����ҳ
    	snap.urlsTbl.put("oastatus","/common/qlsh_home.jsp");
    	    	
    	//7 database��ʱû��,����ҳ
    	snap.urlsTbl.put("database","/common/qlsh_home.jsp");
    	//8 ������·,����ҳ
    	snap.urlsTbl.put("linkstatus", "/common/qlsh_home.jsp");
    	//9 �����¶���ʱû��,����ҳ
    	snap.urlsTbl.put("roomstatus", "/common/qlsh_home.jsp");
    	//10 �Ž�ϵͳ,����ҳ
    	snap.urlsTbl.put("doorsystem", "/common/qlsh_home.jsp");
    	
    	return snap;
    }
}

