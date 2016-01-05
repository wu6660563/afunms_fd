/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

//import com.cn.dhcc.ens.model.DTO;

public interface PersistenceServiceable
{
    public void addOneNetworkItem(
    		String idName,
    		String ipAddress,
    		String netMask,
    		String community,
    		String writeCommunity,
    		String commonNodes,
    		String subNetList,
    		String routerList,
    		String switchList,
    		String snmpRespIpAddrList,
    		String pingRespIpList,
    		String unResponseIpAddrList,
    		String haveAddIpAddrMap,
    		String discoverStatus,
    		String containNetId,
    		String isSeedNetwork,
    		String reserved1,
    		String reserved2,
    		String reserved3,
    		String reserved4,
    		String reserved5,
    		String reserved6,
    		Connection outConnection) throws PersistException;
    	  			
	public void addOneAlarmItem(
	        String alarmId,
	        String ipAddress,
	        String idName,
	        String alarmDate,
	        String alarmTime,
	        String severityoftheAlarm,
	        String sourceOftheAlarm,
	        String alarmDetailInfor,
	        String alarmDealDoneState,
	        String reserved1,
	        String reserved2,
	        String reserved3,
	        Connection outConnection) throws PersistException;
	
	public void addOneMobileAlarmItem(String sqlStr,
			Connection outConnection) throws PersistException;
	      	        			
	public void addOneMonitorItem(
	        String idname,
	        String monitorOid,
			String monitorGroup,
			String monitorCategory,
			String monitorDeviceType,
			String monitorDisplayName,
			String monitorValue,
			String ipAddress,
			String snmpPort,
			String community,
			String isSnmpMonitorItem,
			String thresholdEnable,
			String thresholdLimit,
			String thresholdCheck,
			String severityoftheAlarm,
			String consecutivetimes,
			String reserved1, String reserved2, String reserved3,
			String reserved4, Connection outConnection) throws PersistException;
	
	public void addOneMonitorItem(
	        String idname,
	        String monitorOid,
			String monitorGroup,
			String monitorCategory,
			String monitorDeviceType,
			String monitDisplayOldName,
			String monitDisplayNewName,
			String monitorValue,
			String ipAddress,
			String snmpPort,
			String community,
			String isSnmpMonitorItem,
			String thresholdEnable,
			String thresholdLimit,
			String thresholdCheck,
			String severityoftheAlarm,
			String consecutivetimes,
			String reserved1, String reserved2, String reserved3,
			String reserved4, Connection outConnection) throws PersistException;
			
	public void addOneNodeObjItem(
	         String idName,
	         String sysObjectIdValue,
	         String sysName,
	         String sysDescr,
	         String ipAddress,
	         String netMask,
	         String community,
			 String writeCommunity,
			 String snmpVersion,
			 String snmpPort,
			 String containNetId,
			 String snmpSupport,
			 String deviceCategory,
			 String deviceType,
			 String performDataSnmpTimeOut,
			 String reserved1,
			 String reserved2,
			 String reserved3,
			 String reserved4,
			 String reserved5,
			 String reserved6,
			 Connection outConnection) throws PersistException;
		   			
	public void addOneRouterItem(
	        String idName,
	        String sysObjectIdValue,
	        String sysName,
	        String sysDescr,
	        String ipAddress,
	        String netMask,
	        String community,
			String writeCommunity,
			String snmpSupport,
	        String snmpVersion,
			String snmpPort,
			String containNetId,
			String deviceCategory,
		    String deviceType,
		    String theLayer,
		    String performDataSnmpTimeOut,
		    String reserved1,
		    String reserved2,
		    String reserved3,
			String reserved4,
			String reserved5,
			String reserved6,
		    Connection outConnection) throws PersistException;
		  
	public void addOneSwitchItem(
	         String idName,
	         String sysObjectIdValue,
	         String sysName,
	         String sysDescr,
	         String ipAddress,
	         String netMask,
	         String community,
		     String writeCommunity,
		     String snmpVersion,
		     String snmpPort,
		     String containNetId,
		     String snmpSupport,
		     String deviceCategory,
		     String deviceType,
		     String performDataSnmpTimeOut,
		     String reserved1,
		     String reserved2,
		     String reserved3,
			 String reserved4,
			 String reserved5,
			 String reserved6,
		     Connection outConnection) throws PersistException;
		   
	public void addOneMonitorItemCollet(
	         String colletIpaddress, 
	         String monitorColletDisplayName, 
	         String monitorColletOid, 
	         String monitorColletValue, 
	         String monitorColletDate, 
	         String monitorColletTime, 
	         String reserved1, 
	         String reserved2, 
	         String reserved3, 
	         String reserved4,
	         Connection outConnection) throws PersistException;
	
	public void addOneDispatchNet(
			String nodeIp, 
			String insertTime,
			int timeForSum, 
			String type, 
			Connection outConnection) throws PersistException;
	       
	public void addOneProducerItem(
		     String producerName, 
		     String reserved1, 
		     String reserved2, 
		     String reserved3, 
		     String reserved4,
		     Connection outConnection) throws PersistException;
	       
	public void addOneDeviceTypeCatalogItem(
	         String objIdValue, 
	         String typeDesc, 
	         String picture, 
	         String category, 
	         String producer, 
	         String reserved1, 
	         String reserved2, 
	         String reserved3, 
	         String reserved4,
	         Connection outConnection) throws PersistException;
	       
	public void addOneSpecSnmpCommunItem(
	         String ipaddress, 
	         String community, 
	         String writeCommunity, 
	         String snmpPort, 
	         String reserved1, 
	         String reserved2, 
	         String reserved3, 
	         String reserved4, 
	         String reserved5, 
	         String reserved6,
	         Connection outConnection) throws PersistException;
	         
    public void addOneIfCollectItem(
	          String idName, 
	          String ifindex, 
	          String ifutiliRate, 
	          String ifinErrorsRate, 
	          String ifoutErrorsRate, 
	          String ifinDiscardsRate, 
	          String ifoutDiscardsRate, 
	          String reserved1, 
	          String reserved2, 
	          String reserved3, 
	          String reserved4,
	          Connection outConnection) throws PersistException;
    
    public void addOneEnsCoreParamInfor(
    		  String paramName,
    		  String paramValue,
    		  String paramType,
			  String reserved1,
			  String reserved2,
			  String reserved3,
			  String reserved4,
			  String reserved5,
			  Connection outConnection) throws PersistException;
    
    public void addOneIfEntryInfor(
    		String idname,
    		String ifindex,
			String iftype,
			String ifspeed,
			String ifdescr,
			String Reserved1,
    		String Reserved2,
    		String Reserved3,
    		String Reserved4,
			Connection outConnection) throws PersistException;
	  
    public void addOneNodeParamInfor(
    		String idname,
			String ipaddress,
			String protocolDescr,
			String port,
			String useName,
			String password,
			String prompt,
			String reserved1,
			String reserved2,
			String reserved3,
			String reserved4,
			String reserved5,
			Connection outConnection) throws PersistException;
    
    public void addOneIfCollectTmpItem(
	          String idName, 
	          String ifindex, 
	          String ifutiliRate, 
	          String ifinErrorsRate, 
	          String ifoutErrorsRate, 
	          String ifinDiscardsRate, 
	          String ifoutDiscardsRate, 
	          String reserved1, 
	          String reserved2, 
	          String reserved3, 
	          String reserved4,
	          Connection outConnection) throws PersistException;
    
    public void addOneMonitorTmpItemCollet(
	         String colletIpaddress, 
	         String monitorColletDisplayName, 
	         String monitorColletOid, 
	         String monitorColletValue, 
	         String monitorColletDate, 
	         String monitorColletTime, 
	         String reserved1, 
	         String reserved2, 
	         String reserved3, 
	         String reserved4,
	         Connection outConnection) throws PersistException;
    
    public void addOneApplicInfor(
             String applicName,
         	 String nodeId,
         	 String applicType,
         	 String applicIpAddress,
         	 String appliNetMask,
         	 String loginUser,
         	 String loginPassword,
         	 String applicPort,
         	 String applicVersion,
			 String dbOrServiceName,
         	 String isSSL,
         	 String reserved1,
         	 String reserved2,
         	 String reserved3,
         	 String reserved4,
         	 String reserved5,
			 Connection outConnection) throws PersistException;
    
    public void addOneNotifyMessageInfor(
             String id,
             String user,
             String message,
             String createTime,
             String endTime,
             Connection outConnection) throws PersistException;
    
	//List��ΪNetworkInfor	   
    public List  searchNetworkItem(
             String idName, Connection outConnection);
	 
	//List��ΪAlarmInfor       
	public List searchAlarmItem(
	         String alarmId, Connection outConnection);
	
	//List��ΪMonitorItemInfor       
	public List searchMonitorItem(
				 String idName, Connection outConnection);
	
	//List��ΪNodeObjInfor	
	public List searchNodeObjItem(
			     String idName, Connection outConnection);	  
	
	//List��ΪRouterInfor			   
	public List searchRouterItem(
				 String idName, Connection outConnection);	
	
	//List��ΪSwitchInfor				   
	public List searchSwitchItem(
				 String idName, Connection outConnection);	
			   
	//List��ΪMonitorItemColletInfor		   
	public List searchMonitorItemCollet(
	             String colletIpaddress, Connection outConnection);
	
	//List��ΪProducerInfor          
	public List searchProducerItem(
	              String producerName, Connection outConnection); 
	            
    //List��ΪDeviceTypeCatalogInfor 
    public List searchDeviceTypeCatalogItem(
	              String objIdValue, Connection outConnection);	            
	            
	//List��ΪSpecSnmpCommunInfor
	public List searchSpecSnmpCommunItem(
	              String ipAddress, Connection outConnection);
	              
	//List��ΪIfCollectInfor
	public List searchIfCollectItem(
	              String idName,Connection outConnection); 
	
	//list��ΪEnsCoreParamInfor
	public List searchEnsCoreParamInfor(
	 		      String paramName,Connection outConnection);
	//list��ΪIfEntryInfor
	public List searchIfEntryInfor(
	 		      String idname,Connection outConnection);
	//list��ΪNodeParanInfor
	public List searchNodeParamInfor(
	 		      String idname,Connection outConnection);
	 
    //List��ΪMonitorItemColletTmpInfor		   
	public List searchMonitorItemColletTmpInfor(
		          String colletIpaddress, Connection outConnection);
		
    //List��ΪIfCollectTmpTable
	public List searchIfCollectTmpTable(
		          String idName,Connection outConnection); 

	//List ��ΪApplicInfor
	public List searchApplicInfor(
			      String applicName,Connection outConnection);
	
	//List ��ΪNotifyMessageInfor
	public List searchNotifyMessageInfor(
	              String id,Connection outConnection);
	 
	public List networkInforList(Connection outConnection); 
	
	public List alarmInforList(Connection outConnection); 
	 
	public List monitorItemInforList(Connection outConnection); 
	
	public List nodeObjInforList(Connection outConnection); 
	
	public List routerInforList(Connection outConnection); 
	
	public List switchInforList(Connection outConnection); 
	
	public List monitorItemColletList(Connection outConnection);
	
	public List producerInforList(Connection outConnection);
	
	public List deviceTypeCatalogInforList(Connection outConnection);
	
	public List specSnmpCommunInforList(Connection outConnection);	
	
    public List ifCollectInforList(Connection outConnection);
    
    public List ensCoreParamInforList(Connection outConnection);
    
    public List ifEntryInforList(Connection outConnection);
    
    public List nodeParamInforList(Connection outConnection);
    
    public List ifCollectTmpTableList(Connection outConnection);
    
    public List monitotItemColletTmpInfor(Connection outConnection);
    
    public List applicInforList(Connection outConnection);
    
    public List notifyMessageInforList(Connection outConnection);
	
	public Connection getDbPoolConnection();
    
	public void closeDbPoolConnection(Connection connection);
		 
	/**
     * ִ��һ�� SQL ��ѯ��䲢���ؽ�����÷������ж� SELECT ���ĺϷ��ԡ�<br>
     * ���磺�� String[][] queryResult Ϊ��ѯ���صĽ���������±����������Ϣ��<br>
     * for (int m = 0; m < queryResult.length; m++) {
     *      for (int n = 0; n < queryResult[0].length; n++) {
     *          System.out.print(queryResult[m][n] + "|");
     *      }
     *      System.out.println();
     *  }
     * @param query ָ������Ĳ�ѯ���
     * @param outConnection ָ����Ҫʹ�õ��ⲿ���ӣ�����ò���Ϊ null������ʹ�� Persistence ָ�����ڲ�JDBC����
     * @return ��ѯ���[m][n]��mΪ������nΪ������ע�����صĵ� [0][m] ά����Ϊ��ͷ��Lable��
     */
    public String[][] executeQuery(String query, Connection outConnection);
        
    /**
     * ִ��SQL���롢���¡�ɾ����¼�������÷������ж��������SQL���ĺϷ���
     * @param update ָ����Ҫִ�е�SQL���
     * @param outConnection ָ����Ҫʹ�õ��ⲿ���ӣ�����ò���Ϊ null������ʹ�� Persistence ָ�����ڲ�JDBC����
     */
    public boolean executeUpdate(String update, Connection outConnection);        
    
    /**
     * ���ݵ��������
     * @return
     */
    public boolean backupDayData(Connection outConnection);
    
    /**
     * ������ʷ����
     * @return
     */
    public boolean backupHistoryData(Connection outConnection);
        
    /**
     * ȡ�����ݱ���Ŀ¼
     * @return
     */
    public String getBackUpDirectory();
    
    /**
     * �������ݱ���Ŀ¼
     */
    public void setBackUpDirectory(String tmpStr);    
    
    /**
     * ���ظ澯�������޼�¼��
     * @return
     */
    public String getAlarmDataBkCount();
    
    /**
     * ���ø澯�������޼�¼��
     * @param alarmRecordCnt
     */
    public void setAlarmDataBkCount(long alarmRecordCntParam);
    
    /**
     * ������Դ��غͽӿ���ʷ��ı������޼�¼��
     * @return
     */
    public String getHisDataBkCount();
    
    /**
     * ������Դ��غͽӿ���ʷ��ı������޼�¼��
     * @param hisDataBkCountParam
     */
    public void setHisDataBkCount(long hisDataBkCountParam);
    /**
     * ����֪ͨͨ����Ϣ
     * @param pageNO
     * @param recentId
     * @return DTO
     */
    //public DTO loadMessages(int pageNO,String recentId);
    //********************kjchen ��������Ŀ��**********************  ��ʼ
	public String[][] executeQueryOracle(String sql, Connection conn);
	 //********************kjchen ��������Ŀ��**********************  ����
}

