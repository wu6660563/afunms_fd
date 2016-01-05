/**
 * <p>Description:core device,seed node</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.discovery;

import com.afunms.common.util.*;
import com.afunms.initialize.ResourceCenter;
import com.afunms.ipresource.util.CollectIPDetail;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.DiscoverConfig;
import java.util.List;

public class Seed
{  
   public static boolean discoverOK;
   private String coreIp;
   private String community;
   private int discovermodel;
   
   public Seed(String coreIp,String community,int discovermodel)
   {
	   this.coreIp = coreIp;	   
   	   this.community = community;
   	   this.discovermodel = discovermodel;
	   discoverOK = true;
   }
   
   public void startDiscover()
   {
	   SysLogger.info("��ʼʱ��:" + SysUtil.getCurrentTime());
	   //�洢֮ǰ���ֻ���ӵ��豸
	   HostNodeDao nodeDao = new HostNodeDao();
	   List formerNodeList = nodeDao.loadHost();
	   //�洢֮ǰ���ֻ���ӵ����ӹ�ϵ
	   LinkDao linkDao = new LinkDao();
	   List formerNodeLinkList = linkDao.loadAll();
	  // linkDao.close();
	   if(discovermodel == 1)
	   {
		   //���䷢��
		   DiscoverEngine.getInstance().setFormerNodeList(formerNodeList);
		   DiscoverEngine.getInstance().setFormerNodeLinkList(formerNodeLinkList);
	   }
	   DiscoverMonitor.getInstance().setCompleted(false);
	   

	   try{
		   DiscoverEngine.getInstance().setDiscovermodel(discovermodel);
		   DiscoverEngine.getInstance().setStopStatus(0);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   /*
	   //���ԭ�������˷��ֱ�
	   DiscoverConfigDao dao = new DiscoverConfigDao();
	   dao.cleanTOPOTable();
	   */
	   discoverFromNode();
	   
   	   DiscoverMonitor.getInstance().setStartTime(SysUtil.getCurrentTime());
   	   DiscoverMonitor.getInstance().setEndTime(null);
	   ThreadProbe tp = new ThreadProbe(DiscoverEngine.getInstance());
	   tp.setDaemon(true); //����Ϊ�ػ��߳�
	   tp.start();
   }
      
   private void discoverFromNode()
   {
	   //������б��
	   DiscoverConfigDao cleandao = new DiscoverConfigDao();
	   try{
		   cleandao.cleanTOPOTable();
	   }catch(Exception e){
		   
	   }finally{
		   cleandao.close();
	   }
	   
	   SysLogger.info("����IP:"+coreIp+"   SysOid:############");
   	   String sysOid = SnmpUtil.getInstance().getSysOid(coreIp,community);
   	   SysLogger.info("����IP:"+coreIp+"   SysOid:"+sysOid);   	                                                          
   	   int deviceType = SnmpUtil.getInstance().checkDevice(coreIp,community,sysOid);
   	   SysLogger.info("����IP:"+coreIp+"   SysOid:"+sysOid+" �豸����:"+deviceType);   	   
   	   if(deviceType==0||deviceType>3)
   	   {
   	       SysLogger.info("�����豸" + coreIp + "," + community + "����һ̨�����豸,���ֲ��ܼ���!");
   	       discoverOK = false;
   	       return;
   	   }   	     	  
   	   Host seed = new Host();   	  
   	   seed.setCategory(deviceType);
	   seed.setIpAddress(coreIp);
   	   seed.setCommunity(community);
   	   seed.setWritecommunity(DiscoverEngine.getInstance().getWritecommunity());
   	   String snmpversion = "";
   	   snmpversion = ResourceCenter.getInstance().getSnmpversion();
   	   int default_version = 0;
   	   if(snmpversion.equals("v1")){
  			default_version = org.snmp4j.mp.SnmpConstants.version1;
		  }else if(snmpversion.equals("v2")){
			default_version = org.snmp4j.mp.SnmpConstants.version2c;
		  }else if(snmpversion.equals("v1+v2")){
			default_version = org.snmp4j.mp.SnmpConstants.version1;
		  }else if(snmpversion.equals("v2+v1")){
  			default_version = org.snmp4j.mp.SnmpConstants.version2c;
	   }
   	   seed.setSnmpversion(default_version);
   	   DiscoverEngine.getInstance().setSnmpversion(default_version);
   	   seed.setSysOid(sysOid);
   	   seed.setSuperNode(-1);
   	   seed.setLayer(1);
   	   seed.setLocalNet(-1);  
   	   
   	   /**
   	    * @param Host
   	    * @param Link
   	    */
   	   DiscoverEngine.getInstance().addHost(seed,null);
   	   	   
   	   DiscoverResource.getInstance().getCommunitySet().add(community);
   	   
   	   //����ǰ������������Ϊȱʡ����������
   	   DiscoverResource.getInstance().setCommunity(community);
   	   DiscoverConfigDao dao = new DiscoverConfigDao();
   	   dao.saveCore(coreIp,community);
   	   List othercoreList = dao.listByFlag("othercore");
   	   dao.close();
   	   if(othercoreList != null && othercoreList.size()>0)
   	   {
   		   for(int i=0;i<othercoreList.size();i++)
   		   {
   			   DiscoverConfig vo = (DiscoverConfig)othercoreList.get(i);
   			   sysOid = SnmpUtil.getInstance().getSysOid(vo.getAddress(),vo.getCommunity());
   			   SysLogger.info("��������IP:"+vo.getAddress()+"   SysOid:"+sysOid);   	                                                          
   			   deviceType = SnmpUtil.getInstance().checkDevice(vo.getAddress(),vo.getCommunity(),sysOid);
   			   SysLogger.info("��������IP:"+vo.getAddress()+"   SysOid:"+sysOid+" �豸����:"+deviceType);
   			   /*
   			    * 	<option value='1' selected>·����</option>
            		<option value='2'>·�ɽ�����</option>
            		<option value='3'>������</option>
            		<option value='4'>������</option>
            		<option value='8'>����ǽ</option>
   			    */
   			   if(deviceType==0||deviceType>3)
   			   {
   				   SysLogger.info("���������豸" + vo.getAddress() + "," + vo.getCommunity() + "����һ̨�����豸,���ֲ��ܼ���!");
   				   //discoverOK = false;
   				   continue;
   			   }
   			   Host otherseed = new Host();
   			   otherseed.setIpAddress(vo.getAddress());
   			   otherseed.setCommunity(vo.getCommunity());
   			   otherseed.setCategory(deviceType);
   			   otherseed.setSysOid(sysOid);
   			   otherseed.doDiscover();
   		   }
   	   }
   }
}

class ThreadProbe extends Thread
{
   private DiscoverEngine engine;  	
   public ThreadProbe(DiscoverEngine engine)
   {
      this.engine = engine;
   }

   public void run()
   {
      try
      {
         Thread.sleep(50000);
      }
      catch(InterruptedException ie)
      {}
      
   	  while(!engine.isDiscovered())//������ܻ������ѭ��
      {
         try
         {
            Thread.sleep(30000);
         }
         catch(InterruptedException ie)
         {}
      }
   	  
   	  DiscoverMonitor.getInstance().setEndTime(SysUtil.getCurrentTime());
      DiscoverMonitor.getInstance().setCompleted(true);
      
      DiscoverComplete dc = new DiscoverComplete();
      dc.completed(Seed.discoverOK);      
      SysLogger.info("����ʱ��:" + SysUtil.getCurrentTime());
      /**/
  	//CollectIPDetail cfd = new CollectIPDetail();
	//cfd.findDirectDevices();       	
      
   }	  
}