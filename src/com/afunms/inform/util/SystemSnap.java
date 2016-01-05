/**
 * ϵͳ����,������ҳ
 */

package com.afunms.inform.util;

import java.util.List;

import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;

public class SystemSnap
{
	private SystemSnap()
	{
	}
	
	/**
	 * �����豸��״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getNetworkStatus()
    {
         List nodeList = PollingEngine.getInstance().getNodeList();
         int status = 0; //��ʼ��״̬
         boolean hasAlarm = false; //��û������
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);        	 
        	 if(node.getCategory()>3) continue;

        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}
         }
         return status; 
    }
    
    public static int getNetworkStatus(String nodeid){
        List nodeList = PollingEngine.getInstance().getNodeList();
        int status = 0; //��ʼ��״̬
        boolean hasAlarm = false; //��û������
        for(int i=0;i<nodeList.size();i++) {
       	    Node node = (Node)nodeList.get(i);   
       	    if(nodeid.equals(node.getId()+"")){
       	    	if(node.isAlarm()){
          	    	hasAlarm = true; 
          	    	if(node.getStatus()>status)status = node.getStatus();
           	    }
       	    }
        }
        return status; 
    }
	/**
	 * ����·�����豸��״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getRouterStatus()
    {
         List nodeList = PollingEngine.getInstance().getNodeList();
         int status = 0; //��ʼ��״̬
         boolean hasAlarm = false; //��û������
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);        	 
        	 if(node.getCategory() != 1) continue;

        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}
         }
         return status; 
    }
    
	/**
	 * ���罻�����豸��״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getSwitchStatus()
    {
         List nodeList = PollingEngine.getInstance().getNodeList();
         int status = 0; //��ʼ��״̬
         boolean hasAlarm = false; //��û������
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);        	 
        	 if(node.getCategory() != 2 && node.getCategory() != 3) continue;

        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}
         }
         return status; 
    }
    
	/**
	 * ���罻�����豸��״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getFirewallStatus()
    {
         List nodeList = PollingEngine.getInstance().getNodeList();
         int status = 0; //��ʼ��״̬
         boolean hasAlarm = false; //��û������
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);        	 
        	 if(node.getCategory() != 8) continue;

        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}
         }
         return status; 
    }
    
	/**
	 * ��������״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getServerStatus()
    {
         List nodeList = PollingEngine.getInstance().getNodeList();
         int status = 0; //��ʼ��״̬
         boolean hasAlarm = false; //��û������
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);
        	 if(node.getCategory()!=4) continue;

        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}       	 
         }
         return status; 
    } 
    
	/**
	 * ���ݿ��״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getDbStatus()
    {
         List nodeList = PollingEngine.getInstance().getDbList();
         boolean hasAlarm = false; //��û������
         int status = 0; //��ʼ��״̬
         
         for(int i=0;i<nodeList.size();i++)
         {
        	 Node node = (Node)nodeList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         return status;
    }
    
	/**
	 * �м����״̬
	 * @auth ������
	 * date 2010-01-11
	 */
    public static int getMiddleStatus()
    {
         List cicsList = PollingEngine.getInstance().getCicsList();
         List dominoList = PollingEngine.getInstance().getDominoList();
         List iisList = PollingEngine.getInstance().getIisList();
         List mqList = PollingEngine.getInstance().getMqList();
         List tomcatList = PollingEngine.getInstance().getTomcatList();
         List wasList = PollingEngine.getInstance().getWasList();
         List weblogicList = PollingEngine.getInstance().getWeblogicList();
         
         boolean hasDown = false; //��û�ػ���
         boolean hasAlarm = false; //��û������
         int status = 0; //��ʼ��״̬
         
         for(int i=0;i<cicsList.size();i++)
         {
        	 Node node = (Node)cicsList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         for(int i=0;i<dominoList.size();i++)
         {
        	 Node node = (Node)dominoList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<iisList.size();i++)
         {
        	 Node node = (Node)iisList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<mqList.size();i++)
         {
        	 Node node = (Node)mqList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<tomcatList.size();i++)
         {
        	 Node node = (Node)tomcatList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         for(int i=0;i<wasList.size();i++)
         {
        	 Node node = (Node)wasList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<weblogicList.size();i++)
         {
        	 Node node = (Node)weblogicList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         return status;
    }
    
	/**
	 * �����״̬
	 * @auth ������
	 * date 2010-01-11 
	 */
    public static int getServiceStatus()
    {
         List ftpList = PollingEngine.getInstance().getFtpList();
         List mailList = PollingEngine.getInstance().getMailList();
         List socketList = PollingEngine.getInstance().getSocketList();
         List webList = PollingEngine.getInstance().getWebList();
         
         boolean hasDown = false; //��û�ػ���
         boolean hasAlarm = false; //��û������
         int status = 0; //��ʼ��״̬
         
         for(int i=0;i<ftpList.size();i++)
         {
        	 Node node = (Node)ftpList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         for(int i=0;i<mailList.size();i++)
         {
        	 Node node = (Node)mailList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<socketList.size();i++)
         {
        	 Node node = (Node)socketList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
    	 for(int i=0;i<webList.size();i++)
         {
        	 Node node = (Node)webList.get(i);
        	    if(node.isAlarm()){
           	    	hasAlarm = true; 
           	    	if(node.getStatus()>status)status = node.getStatus();
            	}      	 
         }
         return status;
    }
    
    /**
     * Ӧ��״̬
     */
    public static int getAppStatus()
    {    	
        List nodeList = PollingEngine.getInstance().getNodeList();
        boolean hasDown = false; //��û�ػ���
        boolean hasAlarm = false; //��û������
        for(int i=0;i<nodeList.size();i++)
        {
            Node node = (Node)nodeList.get(i);
       	    if(node.getCategory()<50) continue;

       	    if(node.isAlarm()){
       	    	hasAlarm = true; 
       	    	break;
        	}
       	    
//       	    if(node.getStatus()==3)
//       	    {
//       		    hasDown = true;
//       		    break;
//       	     }
        }
        if(hasDown) //ȡ������ߵ�
       	   return 3;
        else if(hasAlarm)
       	   return 2;
        else
       	   return 1; 
    }   
    
    public static String getTableItem(String id,String ip,double value)
    {
		StringBuffer sb = new StringBuffer(500);
    	sb.append("<tr><td align=left width=70% height=20><a href=\"#\" onclick=\"javascript:window.open('");
    	sb.append("/afunms/detail/dispatcher.jsp?id="); 
    	sb.append(id);
    	sb.append("','window', 'toolbar=no,height=700,width=820,scrollbars=yes,center=yes,screenY=0')\">");
    	sb.append(ip); 
    	sb.append("</a></td><td align=right width='30%'>&nbsp;");
    	sb.append(value);
    	sb.append("%</td></tr>");

    	return sb.toString();
    }
}