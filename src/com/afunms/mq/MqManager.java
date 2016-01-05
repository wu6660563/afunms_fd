package com.afunms.mq;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.Calendar;

import com.ibm.mq.*;
import com.ibm.mq.pcf.CMQC;
import com.ibm.mq.pcf.CMQCFC;
import com.ibm.mq.pcf.CMQXC;
import com.ibm.mq.pcf.PCFException;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import com.afunms.common.util.*;

import com.icss.ro.de.connector.mqimpl.MQNode;

public class MqManager {

	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private I_Smscontent smsmanager=new SmscontentManager();
	private Hashtable properties = new Hashtable();;
	private MQQueueManager mqqueuemanager;
	private String transport = DEServerConstants.MQ_TRANSPORT;
	private PCFMessageAgent agent;
	private PCFMessage request;
	private PCFMessage responses[];
	private String ipaddress = "";

	private MQPoolToken token;

	public MqManager() throws Exception {

//System.out.println("MQManagerUtil init succeed!");
		try {
			agent = new PCFMessageAgent();
			agent.setCharacterSet(DEServerConstants.MQ_CHARATERSET);
			agent.setEncoding(DEServerConstants.MQ_ENCODING);

			/*
			 * properties.put("port", new
			 * Integer(Integer.parseInt(node.getPort())));
			 * properties.put("CCSID", new Integer(1381));
			 * properties.put("hostname", node.getHost());
			 * properties.put("transport", transport); properties.put("channel",
			 * "RONEDE.SVRCONN"); token =
			 * MQEnvironment.addConnectionPoolToken(); mqqueuemanager = new
			 * MQQueueManager(node.getQmanager(), properties);
			 */
		} catch (Exception e) {
			throw new Exception("mq.connection.init.failed");
		}
	}

	public boolean connMQ(MQNode node) {
		try {
			if (node == null)
				throw new Exception("Init failed! MQnode is null.");
			ipaddress =  node.getHost();
			// ����properties��
			properties.put("port",
					new Integer(Integer.parseInt(node.getPort())));
			properties.put("CCSID", new Integer(
					DEServerConstants.MQ_CHARATERSET));
			properties.put("hostname", node.getHost());
			properties.put("transport", transport);
			properties.put("channel", DEServerConstants.MQ_SVRCONN);
			// properties.put(MQC.TRANSPORT_PROPERTY,
			// MQC.TRANSPORT_MQSERIES_BINDINGS);
			// ʹ�û����
			token = MQEnvironment.addConnectionPoolToken();
//System.out.println("*****!!!!!!!!*******" + node.getQmanager());
			mqqueuemanager = new MQQueueManager(node.getQmanager(), properties);
//System.out.println("************" + node.getQmanager());
			agent.connect(mqqueuemanager);
			
//System.out.println("Connect to MQ " + node.getHost() + " succeed!");
		
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("can not connect");
			return false;
		}

		return true;
	}

	// ȡ���е�����

	public Vector inquireQNames(String queueName, int queueType) {
		Vector qNames = new Vector();
		int qType;
		switch (queueType) {
		case 0:
			qType = CMQC.MQQT_ALL;
			break;
		case 1:
			qType = CMQC.MQQT_LOCAL;
			break;
		case 2:
			qType = CMQC.MQQT_REMOTE;
			break;
		default:
			qType = CMQC.MQQT_ALL;
			break;
		}
		
		if (queueName != null && !queueName.equals("")
				&& !"null".equalsIgnoreCase(queueName)) {
			queueName += "*";
		} else {
			queueName = "*";
		}
		try {
			
			request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_Q_NAMES);
			request.addParameter(CMQC.MQCA_Q_NAME, queueName);
			request.addParameter(CMQC.MQIA_Q_TYPE, qType);
			responses = agent.send(request);			
			String[] qNameStrs = (String[]) responses[0]
					.getParameterValue(CMQCFC.MQCACF_Q_NAMES);
			String name;
			
			for (int i = 0; i < qNameStrs.length; i++) {
					name = qNameStrs[i];
					qNames.add(name);
//System.out.println("***********==========" + qNames.get(i));
			}
			//agent.disconnect();
		} catch (PCFException pcfe) {
			// Debug.error("Error in response: ",module);
			PCFMessage[] responses = (PCFMessage[]) pcfe.exceptionSource;
			for (int i = 0; i < responses.length; i++) {
				// Debug.error(""+responses[i],module);
			}
			// Debug.error(pcfe,pcfe.getMessage(),module);
			return null;
		} catch (MQException mqe) {
			// Debug.error(mqe,mqe.getMessage(),module);
			return null;
		} catch (IOException ioe) {
			// Debug.error(ioe,ioe.getMessage(),module);
			return null;
		}
		return qNames;
	}

	/**
     * ȡ��ͨ��״̬
     * @param channelName       ͨ��������
     * @return int              ״̬����
     */
    public int getChlStatus(String channelName)
    {
        Integer status = new Integer(-1);
        PCFMessage[] responses_status;
        try
        {
            request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL_STATUS);
            request.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, channelName);
            responses_status = agent.send(request);
            status = (Integer) responses_status[0].getParameterValue(CMQCFC.MQIACH_CHANNEL_STATUS);
//System.out.println("channel status ======= "+status.intValue());
        }
        catch (MQException e)
        {
            if (e.reasonCode == CMQCFC.MQRCCF_CHL_STATUS_NOT_FOUND) //3065
            {
                return -1;
            }
            return -1;
        }
        catch (Exception e)
        {
            return -1;
        }
        return status.intValue();
    }
    
	/**
     * ȡ��ͨ��״̬
     * @param channelName       ͨ��������
     * @return int              ״̬����
     */
    public void freeConn()
    {
        try
        {
        	mqqueuemanager.disconnect();
        }
        catch (MQException e)
        {
            if (e.reasonCode == CMQCFC.MQRCCF_CHL_STATUS_NOT_FOUND) //3065
            {
            }
        }
        catch (Exception e)
        {

        }
    }
    
    /**
     * ���ض��в����б�
     * @param queueName         ��ѯ��������
     * @param queueType         �������� 
     * @return Vector          �������Լ���
     */
    public List inquireQueue(String queueName, int queueType)
    {
        String qn = "";
        if (queueName!=null && !queueName.equals("") && !queueName.equalsIgnoreCase("null"))
        {
            qn += queueName + "*";
        }
        else
        {
            qn = "*";
        }
        List queueAttrs = new ArrayList();

        try
        {
            request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_Q);
            request.addParameter(CMQC.MQCA_Q_NAME, qn);
            request.addParameter(CMQC.MQIA_Q_TYPE, queueType);
            request.addParameter(CMQCFC.MQIACF_Q_ATTRS, new int[] { CMQC.MQCA_Q_NAME, CMQC.MQIA_Q_TYPE, CMQC.MQIA_DEF_PERSISTENCE, CMQC.MQIA_USAGE, //���ض���
                CMQC.MQIA_CURRENT_Q_DEPTH, CMQC.MQCA_REMOTE_Q_NAME, //Զ�̶���
                CMQC.MQCA_REMOTE_Q_MGR_NAME, CMQC.MQCA_XMIT_Q_NAME });
            responses = agent.send(request);
            for (int i = 0; i < responses.length; i++)
            {
                String name = responses[i].getStringParameterValue(CMQC.MQCA_Q_NAME);
                int type = responses[i].getIntParameterValue(CMQC.MQIA_Q_TYPE);
                int persistent = responses[i].getIntParameterValue(CMQC.MQIA_DEF_PERSISTENCE); //
                String qDepth = "";
                MqQueue qAttr = new MqQueue();
                if (type == CMQC.MQQT_LOCAL) // 1
                {
                	
                    int depth = responses[i].getIntParameterValue(CMQC.MQIA_CURRENT_Q_DEPTH);
                    int usage = responses[i].getIntParameterValue(CMQC.MQIA_USAGE);
                    if ((!name.startsWith("SYSTEM")) && (!name.startsWith("AMQ")) && (!name.startsWith("MQAI")))
                    {                    	
                        qAttr.setQname(name);
//System.out.println("name ================"+qAttr.getQname());
                        qAttr.setQtype(Integer.toString(type));
                        qAttr.setPersistent(Integer.toString(persistent));
                        qAttr.setQdepth(Integer.toString(depth));
                        qAttr.setUsage(Integer.toString(usage));
                        queueAttrs.add(qAttr);
                    }
                }
                else if (type == CMQC.MQQT_REMOTE)
                {
                    String remoteQName = responses[i].getStringParameterValue(CMQC.MQCA_REMOTE_Q_NAME);
                    String remoteQM = responses[i].getStringParameterValue(CMQC.MQCA_REMOTE_Q_MGR_NAME);
                    String xmitQName = responses[i].getStringParameterValue(CMQC.MQCA_XMIT_Q_NAME);
                    if ((!name.startsWith("SYSTEM")) && (!name.startsWith("AMQ")) && (!name.startsWith("MQAI")))
                    {
                        qAttr.setQname(name);
                        qAttr.setQtype(Integer.toString(type));
                        
                        qAttr.setPersistent(Integer.toString(persistent));
                        
                        qAttr.setRemoteQName(remoteQName);
                        qAttr.setRemoteQM(remoteQM);
                        qAttr.setXmitQName(xmitQName);
                        
                        queueAttrs.add(qAttr);
//System.out.println("test ================"+qAttr.getQname());
                    }
                }
                else {
                	System.out.println("it is an error!!!!");
                }
            }
            //agent.disconnect();  
        }
        catch (PCFException pcfe)
        {            
            
            PCFMessage[] responses = (PCFMessage[]) pcfe.exceptionSource;
            for (int i = 0; i < responses.length; i++)
            {
               
            }
            return null;
        }
        catch (MQException mqe)
        {
            
            return null;
        }
        
        catch (Exception ioe)
        {
           
            return null;
        }
        return queueAttrs;
    }
	
	/**
	 * ����ͨ�������б�
	 * 
	 * @param chlName
	 *            ��ѯͨ������
	 * @param chlType
	 *            ͨ������
	 * @return Vector ͨ�����Ƽ���
	 */
	public Vector inquireChlNames(String chlName, int chlType) {
		
		Vector chlNames = new Vector();
		try {
			
			request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL_NAMES);
			request.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, chlName);
			request.addParameter(CMQCFC.MQIACH_CHANNEL_TYPE, chlType);
			
			responses = agent.send(request);
//System.out.println("quzhi =============" + request);
			String[] chlNameStrs = (String[]) responses[0]
					.getParameterValue(CMQCFC.MQCACH_CHANNEL_NAME);
			String name;
			if (chlNameStrs != null) {
				for (int i = 0; i < chlNameStrs.length; i++) {
//System.out.println("length ======= "+chlNameStrs.length);
					name = chlNameStrs[i];
					chlNames.add(name);
//System.out.println("^^^^^^^^^^^^======");
				}
			} else {
				return null;
			}
			//agent.disconnect();

		} catch (PCFException pcfe) {
			PCFMessage[] responses = (PCFMessage[]) pcfe.exceptionSource;
			for (int i = 0; i < responses.length; i++) {
			}

			if (pcfe.completionCode == 2
					&& pcfe.reasonCode == CMQCFC.MQRCCF_CHANNEL_NOT_FOUND) {

			}
			return null;
		} catch (MQException mqe) {

			return null;
		} catch (IOException ioe) {
			return null;
		}
		return chlNames;
	}
	
	
  /**
  * ����ͨ�������б�
  * @param chlName         ��ѯͨ������
  * @param chlType         ͨ������
  * @return Vector         ͨ�����Լ���
  */
 public List inquireChannel(String chlName, int chlType, boolean isFullName)
 {
//System.out.println("channel type ==============" + CMQXC.MQCHT_RECEIVER);
	 List chlAttrs = new ArrayList();
     if (chlName==null || chlName.equals("") || chlName.equalsIgnoreCase("null"))
     {
         chlName = "*";
     }

     if ((!isFullName) && (!chlName.equals("*")))
     {
         chlName = chlName + "*";
     }

     try
     {
         request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL);
         request.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, chlName);
         request.addParameter(CMQCFC.MQIACH_CHANNEL_TYPE, chlType);
         responses = agent.send(request);
         int responsesLength = 0;
         if (responses != null)
         {
             responsesLength = responses.length;
             for (int i = 0; i < responsesLength; i++)
             {
                 String name = responses[i].getStringParameterValue(CMQCFC.MQCACH_CHANNEL_NAME);
                 int type = responses[i].getIntParameterValue(CMQCFC.MQIACH_CHANNEL_TYPE);
                 String desc = responses[i].getStringParameterValue(CMQCFC.MQCACH_DESC);
                 String connName = "";
                 String host = "";
                 String port = "";
                 String xmitQName = "";
                 int status = -1;
                 if (type == CMQXC.MQCHT_SENDER)
                 {
                     connName = responses[i].getStringParameterValue(CMQCFC.MQCACH_CONNECTION_NAME);
                     int start = connName.indexOf("(");
                     int end = connName.indexOf(")");
                     if (start == -1)
                     {
                         host = connName;
                     }
                     else
                     {
                         host = connName.substring(0, start);
                         port = connName.substring((start + 1), end);
                     }
                     xmitQName = responses[i].getStringParameterValue(CMQCFC.MQCACH_XMIT_Q_NAME);
                 }
                 if ((!name.startsWith("SYSTEM")) && (!name.startsWith("AMQ")) && (!name.startsWith("MQAI")))
                 {
                     MqChannel cAttr = new MqChannel();
                     cAttr.setName(name);
                     cAttr.setType(Integer.toString(type));
                     cAttr.setDesc(desc);
                     cAttr.setConnName(connName);
                     cAttr.setHost(host);
                     cAttr.setPort(port);
//System.out.println("port =============== "+cAttr.getPort());
                     cAttr.setXmitQName(xmitQName);
                     status = getChlStatus(name);
                     cAttr.setStatus(Integer.toString(status));
                     chlAttrs.add(cAttr);
//System.out.println("port =============== "+cAttr);
                 }
             }
         }
         else
         {
             return null;
         }
         //agent.disconnect();
     }
     catch (PCFException pcfe)
     {
        
         PCFMessage[] responses = (PCFMessage[]) pcfe.exceptionSource;
         for (int i = 0; i < responses.length; i++)
         {
            
         }
       
         return null;
     }
     catch (MQException mqe)
     {
         
         return null;
     }
     catch (IOException ioe)
     {
        
         return null;
     }
     return chlAttrs;
 }
 
/**
* ����ͨ��״̬�����б�
* @param chlName         ��ѯͨ������
* @param chlType         ͨ������
* @return Vector         ͨ�����Լ���
*/
 
 
public Vector inquireChannelStatus(String chlName, int chlType)
{
  String chlN = "";
  if (chlName==null || chlName.equals("") || chlName.equalsIgnoreCase("null"))
  {
      chlN += chlName + "*";
  }
  else
  {
      chlN = "*";
  }

  Vector chlAttrs = new Vector();

  try
  {
	  Hashtable allMqChannelAlarm = ShareData.getAllmqalarmdata();
      request = new PCFMessage(CMQCFC.MQCMD_INQUIRE_CHANNEL_STATUS);
      request.addParameter(CMQCFC.MQCACH_CHANNEL_NAME, chlN);
      //��status������û�а����Ͳ��ҵĲ���
      //request.addParameter(CMQCFC.MQIACH_CHANNEL_TYPE, chlType);            
      //request.addParameter(CMQCFC.MQIACH_CHANNEL_INSTANCE_TYPE, CMQC.MQOT_SAVED_CHANNEL);
      request.addParameter(CMQCFC.MQIACH_CHANNEL_INSTANCE_TYPE, CMQC.MQOT_CURRENT_CHANNEL);  
      //request.addParameter(CMQCFC.MQIACH_CHANNEL_INSTANCE_TYPE, CMQC.MQOT_CHANNEL);
      /*
      Hashtable cAttr = new Hashtable();
      cAttr.put("name", "name");	//ͨ������
      cAttr.put("type", Integer.toString(1));
      cAttr.put("connName", "connName");//������
      cAttr.put("xmitQName", "xmitQName");//�����������
      cAttr.put("status", Integer.toString(1));//״̬
      chlAttrs.add(cAttr);
      */
      
      Vector sameName = new Vector();
      responses = agent.send(request);      
      for (int i = 0; i < responses.length; i++)
      {
    	  //SysLogger.info(responses[i]+"==="+i);
    	  String statusStr = "";
          String name = "";
          try{
        	  name = responses[i].getStringParameterValue(CMQCFC.MQCACH_CHANNEL_NAME);
          }catch(Exception e){
        	  e.printStackTrace();
          }
          int type = 0;
          try{
        	  responses[i].getIntParameterValue(CMQCFC.MQIACH_CHANNEL_TYPE);
          }catch(Exception e){
        	  e.printStackTrace();
          }
          String typeStr = "";
          try{
          	typeStr = DEServerConstants.ChaneltypeStr[DEServerConstants.Chaneltypes[type]];
          }catch(Exception e){
        	  e.printStackTrace();
          }
          int status = 0;
          try{
        	  status = responses[i].getIntParameterValue(CMQCFC.MQIACH_CHANNEL_STATUS);
          }catch(Exception e){
        	  e.printStackTrace();
          }
          String connName = "";
          String xmitQName = "";
          //                if(type == CMQXC.MQCHT_SENDER)
          //                {{
          try{
        	  connName = responses[i].getStringParameterValue(CMQCFC.MQCACH_CONNECTION_NAME);
          }catch(Exception e){
        	  e.printStackTrace();
          }
          try{
        	  xmitQName = responses[i].getStringParameterValue(CMQCFC.MQCACH_XMIT_Q_NAME);
          }catch(Exception e){
        	  e.printStackTrace();
          }
          //                }
          if ((!name.startsWith("SYSTEM")) && (!name.startsWith("AMQ")) && (!name.startsWith("MQAI")))
          {
//        	  SysLogger.info(name+"==="+type+"==="+connName+"==="+xmitQName+"==="+status);
              Hashtable cAttr = new Hashtable();              
              cAttr.put("name", name);	//ͨ������
              cAttr.put("type", Integer.toString(type));
              cAttr.put("connName", connName);//������
              cAttr.put("xmitQName", xmitQName);//�����������
              cAttr.put("status", Integer.toString(status));//״̬
              chlAttrs.add(cAttr);
              
              if (status == CMQCFC.MQCHS_RUNNING){
            	  //��������״̬
            	  statusStr = "��������";
              }else{
            	  //ͨ�����ڲ��״̬,��Ҫ�澯
            	  //����MQ��ͨ���澯�����ж��Ƿ���Ҫ�澯
              }
                  
          }
      }
		 //mqqueuemanager.close();
		 //mqqueuemanager.disconnect();      
		 //agent.disconnect();
  }
  
  catch (PCFException pcfe)
  {
	  pcfe.printStackTrace();
      //PCFMessage[] responses = (PCFMessage[]) pcfe.exceptionSource;
      //for (int i = 0; i < responses.length; i++)
      //{
          
      //}      
      return null;
  }
  catch (MQException mqe)
  {
	  mqe.printStackTrace();
      return null;
  }
  
  catch (Exception ioe)
  {      
	  ioe.printStackTrace();
      return null;
  }
//System.out.println("chlAttrs size ----------"+chlAttrs.size());
  return chlAttrs;
}
 


	// ���������

	public static void main(String[] args) {
		try {
			MqManager mq = new MqManager();
			MQNode node = new MQNode();
			node.setQmanager("QM_WL_MES");
			//node.setQmanager("dhcc");
		    node.setHost("192.168.50.131");
			//node.setHost("localhost");
			node.setPort("1415");
			node.setQueue("");
			mq.connMQ(node);
			mq.inquireQNames("",0);
			//mq.getChlStatus("C_WL_MES");
			//mq.inquireQueue("",6);
			mq.inquireQueue("", CMQC.MQQT_REMOTE);
			//mq.inquireChlNames("C_WL_MES",3);
			mq.inquireChannelStatus("", 0);
			//mq.inquireChannel("",3,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
