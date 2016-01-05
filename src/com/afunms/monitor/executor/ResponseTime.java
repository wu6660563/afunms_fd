/**
 * <p>Description:ping,then get the response time</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-27
 */

package com.afunms.monitor.executor;

import java.io.*;
import java.util.Hashtable;

import com.afunms.common.util.SysUtil;
import com.afunms.monitor.executor.base.MonitorInterface;
import com.afunms.monitor.item.ResponseTimeItem;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.inform.model.Alarm;
import com.afunms.polling.base.*;
import com.afunms.topology.model.HostNode;

public class ResponseTime implements MonitorInterface
{
   public ResponseTime()
   {
   }
   public void collectData(HostNode node){
	   
   }
   public Hashtable collect_Data(HostNode node){
	   return null;
   }
   public void collectData(Node node,MonitoredItem monitoredItem)
   {
	  ResponseTimeItem item = (ResponseTimeItem)monitoredItem;   	    	
   	  item.setSingleResult(ping(node.getIpAddress()));
   }
   
   /**
    * ping Ȼ��õ���Ӧʱ��,���û����Ӧ�򷵻�-1
    */
   private static int ping(String ip)
   {
	  String line = null;
	  String pingInfo = null;

      try
      {
    	 StringBuffer sb = new StringBuffer(300); 
         Process process = Runtime.getRuntime().exec("ping -n 2 " + ip);
         BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
         while((line = in.readLine()) != null)
            sb.append(line);

         process.destroy();
         in.close();
         pingInfo = sb.toString();
      }
      catch (IOException ioe)
      {
         pingInfo = null;
      }
            
      if (pingInfo == null || pingInfo.indexOf("Destination host unreachable") != -1
    	  ||pingInfo.indexOf("Unknown host") != -1 ||pingInfo.indexOf("Request timed out.") != -1)
        return -1;

      int loc1 = 0, loc2 = 0, time = 0; 
      try
      {
         loc1 = pingInfo.indexOf("Average");
         loc2 = pingInfo.indexOf("ms",loc1);
         time = Integer.parseInt(pingInfo.substring(loc1 + 10, loc2)); 
      }
      catch (Exception e)
      {
    	  time = -1;
      }
      return time;
   } 
   
   public void analyseData(Node node,MonitoredItem monitoredItem)
   {
	  ResponseTimeItem item = (ResponseTimeItem)monitoredItem;  
	  if(item.getSingleResult()==-1)
	  {
		  node.setFailTimes(node.getFailTimes() + 1);
		  item.setViolateTimes(item.getViolateTimes() + 1);
	  }	 
	  else
	  {	  
		  node.setNormalTimes(node.getNormalTimes() + 1); 
		  item.setViolateTimes(0);	
		  node.setStatus(1);
	  }
	  
	  if(item.getViolateTimes()>1)
	  {	
		  node.setAlarm(true);
		  Alarm vo = new Alarm();
		  vo.setIpAddress(node.getIpAddress());
		  vo.setLevel(2);		  
		  vo.setMessage("���豸��ʱû��Ӧ,�����豸æ");		  
		  vo.setCategory(node.getCategory());
		  vo.setLogTime(SysUtil.getCurrentTime());
			
		  node.setStatus(2);
		  if(item.getViolateTimes()>1)
		  {
			  vo.setLevel(3);		  
			  vo.setMessage("���豸��������2��û��Ӧ��,�����ѹػ�");			  
			  node.setStatus(3);
		  }
		  if(item.getViolateTimes()==1||item.getViolateTimes()==2)
			  node.getAlarmMessage().add(vo); //���������α���(2006.10.20)	
	  }   
   }   
}
