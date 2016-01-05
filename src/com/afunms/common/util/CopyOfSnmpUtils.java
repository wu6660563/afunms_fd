/**
 * <p>Description:snmp tool</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class CopyOfSnmpUtils
{
  private static String proType = "udp";

  private static String proPort = "161";
  public static final int OCTSTRING = 1;
  public static final int INTEGER32 = 2;
  private static TransportMapping transport = null;

  private static Snmp snmp = null;
  
  public static String[][] getCpuTableData(String ip,String community,String[] oids,int version,int retries,int timeout){
	  String[][] tablevalues = null; 
	  CommunityTarget target = null;
	  List rowvalues = null;
      TableEvent row = null;
      VariableBinding[] columnvalues = null;
      VariableBinding columnvalue = null;
	  try{
		  target = createCommunityTarget(ip, community, version, retries, timeout);
		  PDUFactory pf = new DefaultPDUFactory(PDU.GET);
		  TableUtils tableUtils = new TableUtils(snmp,pf );
	      OID[] columns = new OID[oids.length];
	       for (int i = 0; i < oids.length; i++)
	          columns[i] = new OID(oids[i]);
	      rowvalues = tableUtils.getTable(target,columns,null,null);
	         if(rowvalues == null){
	        	 return tablevalues;
	         }
	         tablevalues = new String[rowvalues.size()][oids.length+1];
	         for (int i = 0; i < rowvalues.size(); i++)
	         {
	            row = (TableEvent) rowvalues.get(i);
	            columnvalues = row.getColumns();
	            if(columnvalues!=null)
	            {
	               for (int j = 0; j < columnvalues.length; j++)
	               {     
	                  columnvalue = columnvalues[j];
	                  if(columnvalue == null)continue;
	                  
	                  String value=columnvalue.toString().substring(columnvalue.toString().indexOf("=")+1,columnvalue.toString().length()).trim();
	                  tablevalues[i][j] = value;
	                  tablevalues[i][j+1] = row.getIndex().toString();
	                  //SysLogger.info(ip+" columnvalue.getOid()=== "+columnvalue.getOid()+" index="+row.getIndex()+" value "+value);
	                  //if(columnvalue!=null) tablevalues[i][j] = columnvalue.getVariable().toString();
	               }
	            }
	         }
	  }catch(Exception e){ 
		  
	  }
	  
	  return tablevalues;
  }
  
  public static String[][] getTableData(String ip,String community,String[] oids,int version,int retries,int timeout) throws Exception
  {
	  String[][] tablevalues = null; 
	  PDU pdu = null;
	  CommunityTarget target = null;
	  List rowvalues = null;
      TableEvent row = null;
      VariableBinding[] columnvalues = null;
      VariableBinding columnvalue = null;
     
     try
     {   	 
		  target = createCommunityTarget(ip, community, version, retries, timeout);
		  PDUFactory pf = new DefaultPDUFactory(PDU.GET);
		  TableUtils tableUtils = new TableUtils(snmp,pf );
	      OID[] columns = new OID[oids.length];
	      for (int i = 0; i < oids.length; i++)
	          columns[i] = new OID(oids[i]);
	      rowvalues = tableUtils.getTable(target,columns,null,null);
	         if(rowvalues == null){
	        	 return tablevalues;
	         }
	      tablevalues = new String[rowvalues.size()][oids.length+1];

        for (int i = 0; i < rowvalues.size(); i++)
        {
           row = (TableEvent) rowvalues.get(i);
           columnvalues = row.getColumns();
           
           if(columnvalues!=null)
           {
              for (int j = 0; j < columnvalues.length; j++)
              {     
           	  //SysLogger.info(ip+" columnvalue==="+columnvalues[j]);
                 columnvalue = columnvalues[j];
                 if(columnvalue == null)continue;
                 
                 String value=columnvalue.toString().substring(columnvalue.toString().indexOf("=")+1,columnvalue.toString().length()).trim();
                 tablevalues[i][j] = value;
                 //SysLogger.info(address+" columnvalue.getOid()=== "+columnvalue.getOid()+" index="+row.getIndex()+" value "+value);
                 //if(columnvalue!=null) tablevalues[i][j] = columnvalue.getVariable().toString();
              }
           }
        }
     }
     catch (Exception e)
     {
   	  e.printStackTrace();
   	  SysLogger.error("Error in getTableData,ip=" + ip + ",community=" + community);
    	  tablevalues = null;
     }
     row = null;
     columnvalues = null;
     columnvalue = null;
     return tablevalues;
  }
  
  public static String[][] getTemperatureTableData(String ip,String community,String[] oids,int version,int retries,int timeout) throws Exception
  {
	  String[][] tablevalues = null; 
	  PDU pdu = null;
	  CommunityTarget target = null;
	  List rowvalues = null;
      TableEvent row = null;
      VariableBinding[] columnvalues = null;
      VariableBinding columnvalue = null;
     
     try
     {   	  
		  target = createCommunityTarget(ip, community, version, retries, timeout);
		  PDUFactory pf = new DefaultPDUFactory(PDU.GET);
		  TableUtils tableUtils = new TableUtils(snmp,pf );
	      OID[] columns = new OID[oids.length];
	      for (int i = 0; i < oids.length; i++)
	          columns[i] = new OID(oids[i]);
	      rowvalues = tableUtils.getTable(target,columns,null,null);
	         if(rowvalues == null){
	        	 return tablevalues;
	         }
	      tablevalues = new String[rowvalues.size()][oids.length+1];

        for (int i = 0; i < rowvalues.size(); i++)
        {
           row = (TableEvent) rowvalues.get(i);
           columnvalues = row.getColumns();
           
           if(columnvalues!=null)
           {
              for (int j = 0; j < columnvalues.length; j++)
              {     
           	  //SysLogger.info(address+" columnvalue==="+columnvalues[j]);
                 columnvalue = columnvalues[j];
                 if(columnvalue == null)continue;
                 
                 String value=columnvalue.toString().substring(columnvalue.toString().indexOf("=")+1,columnvalue.toString().length()).trim();
                 tablevalues[i][j] = value;
                 tablevalues[i][oids.length] = row.getIndex().toString();
                 //SysLogger.info(address+" columnvalue.getOid()=== "+columnvalue.getOid()+" index="+row.getIndex()+" value "+value);
                 //if(columnvalue!=null) tablevalues[i][j] = columnvalue.getVariable().toString();
              }
           }
        }
     }
     catch (Exception e)
     {
   	  e.printStackTrace();
   	  SysLogger.error("Error in getTableData,ip=" + ip + ",community=" + community);
    	  tablevalues = null;
     }
     row = null;
     columnvalues = null;
     columnvalue = null;
     return tablevalues;
  }
  
  
  public static List getFdbTable(String ip,String community,int version,int retries,int timeout)
  {
     String[] oids1 = new String[]
                    {"1.3.6.1.2.1.17.4.3.1.1",  //1.mac   		       
                     "1.3.6.1.2.1.17.4.3.1.2",  //2.port
                     "1.3.6.1.2.1.17.4.3.1.3"}; //3.type
     
     String[] oids2 = new String[]
           		 {"1.3.6.1.2.1.17.1.4.1.2",  //1.index
           		  "1.3.6.1.2.1.17.1.4.1.1"}; //2.port
                      
     String[][] ipArray1 = null;
     String[][] ipArray2 = null;
     List tableValues = new ArrayList(30);
     try
     {         
   	  HashMap portMap = new HashMap();
   	  
   	  ipArray2 = getTableData(ip,community,oids2,version,retries,timeout);
   	  for(int i=0;i<ipArray2.length;i++){
   		  //SysLogger.info(address+" FDB port:"+ipArray2[i][1]+"   index:"+ipArray2[i][0]);
   	     portMap.put(ipArray2[i][1], ipArray2[i][0]);
   	  }
   	      	  
   	  ipArray1 = getTableData(ip,community,oids1,version,retries,timeout);
   	  for(int i=0;i<ipArray1.length;i++)
   	  {
   		  //SysLogger.info("FDB====>ip : "+address+" type========="+ipArray1[i][2]+" port "+ipArray1[i][1]+" MAC : "+ipArray1[i][0]);
             if(!"3".equals(ipArray1[i][2])) continue; //only type=learned 
   		  if(portMap.get(ipArray1[i][1])==null) continue;
   		  
  		      String ifIndex = (String)portMap.get(ipArray1[i][1]);     	       	      
  		      String[] item = new String[3];
  		      item[0] = ifIndex;
  		      item[1] = ipArray1[i][0];
  		      item[2] = ipArray1[i][1];
  		      //SysLogger.info(address+" FDB "+" ifIndex : "+ifIndex+" MAC : "+ipArray1[i][0]) ;  		      
    	      tableValues.add(item);     	      
   	  }    	  
     }
     catch(Exception e)
     {
   	  e.printStackTrace();
   	  SysLogger.info("getFdbTable(),ip=" + ip + ",community=" + community);         
     }
     return tableValues;
  }
  
  
  private static CommunityTarget createCommunityTarget(String address,String community,int version,int retries,int timeout)
  {
	  //SysLogger.info(address+"==="+community+"==="+version);
      CommunityTarget target = new CommunityTarget();
      target.setCommunity(new OctetString(community));     
      target.setVersion(version);
      target.setRetries(retries);
      target.setAddress(GenericAddress.parse(address + "/161"));
      target.setTimeout(timeout);
     
      return target;
  }
  
  public PDU createPDU(Target target)
  {
     PDU request = new PDU();
     request.setType(PDU.GET);
     return request;
  }
  private static PDU createPdu(int version)
  {
    if (version == 0)
      return new PDUv1();
    if (version == 1) {
      return new PDU();
    }
    return new PDU();
  }

  private static CommunityTarget createCommunityTarget(Address targetAddress, String community, int retries, int timeout, int version)
  {
    CommunityTarget target = new CommunityTarget();
    target.setCommunity(new OctetString(community));
    target.setAddress(targetAddress);
    target.setRetries(retries);
    target.setTimeout(timeout);
    target.setVersion(version);
    return target;
  }

//  public static String getSimpleValueByV1(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getSimpleValue(oid, ip, community, port, timeout, retries, 0);
//  }
//
//  public static String getNextSimpleValueByV1(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getNextSimpleValue(oid, ip, community, port, timeout, retries, 0);
//  }
//
//  public static String[] getNextSimpleValueWithOidByV1(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getNextSimpleWithOidValue(oid, ip, community, port, timeout, retries, 0);
//  }
//
//  public static String getSimpleValueByV2(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getSimpleValue(oid, ip, community, port, timeout, retries, 1);
//  }
//
//  public static String getNextSimpleValueByV2(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getNextSimpleValue(oid, ip, community, port, timeout, retries, 1);
//  }
//
//  public static String[] getNextSimpleValueWithOidByV2(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    return getNextSimpleWithOidValue(oid, ip, community, port, timeout, retries, 1);
//  }
//
//  public static String[] getSimpleValueListByV1(String[] oids, String ip, String community, String port, int timeout, int retries)
//  {
//    return getSimpleValueList(oids, ip, community, port, timeout, retries, 0);
//  }
//
//  public static String[] getSimpleValueListByV2(String[] oids, String ip, String community, String port, int timeout, int retries)
//  {
//    return getSimpleValueList(oids, ip, community, port, timeout, retries, 1);
//  }
//
//  public static String[][] getTableListByMultiOidGetNext(String[] oids, String ip, String community, String port, int timeout, int retries)
//  {
//    Vector vctVB = getTableListByMultiOidGetNext(oids, ip, community, port, timeout, retries, 1);
//
//    if (vctVB == null) {
//      return (String[][])null;
//    }
//
//    int maxLength = 0;
//    for (VariableBinding[] bind : vctVB) {
//      if ((bind != null) && (bind.length > maxLength)) {
//        maxLength = bind.length;
//      }
//    }
//
//    if ((vctVB != null) && (vctVB.size() > 0)) {
//      String[][] result = new String[vctVB.size()][maxLength];
//      int i = 0;
//      for (VariableBinding[] bindings : vctVB) {
//        if (bindings != null) {
//          int j = 0;
//          for (VariableBinding binding : bindings) {
//            if (binding != null) {
//              result[i][j] = StringUtil.hexToChinese(binding.getVariable().toString(), binding.getOid().toString());
//            }
//
//            ++j;
//          }
//        }
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String[] getTableListByV1(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    Vector vctVB = getTableListByGetNext(oid, ip, community, port, timeout, retries, 0);
//
//    if ((vctVB != null) && (vctVB.size() > 0)) {
//      String[] result = new String[vctVB.size()];
//      int i = 0;
//      Iterator iter = vctVB.iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        result[i] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      return result;
//    }
//    return null;
//  }
//
//  public static String[] getTableListByV2(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    Vector vctVB = getTableListByGetNext(oid, ip, community, port, timeout, retries, 1);
//
//    if ((vctVB != null) && (vctVB.size() > 0)) {
//      String[] result = new String[vctVB.size()];
//      int i = 0;
//      Iterator iter = vctVB.iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding == null) {
//          continue;
//        }
//        Variable variable = variableBinding.getVariable();
//        boolean isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          result[i] = null;
//          ++i;
//        }
//
//        result[i] = StringUtil.hexToChinese(variable.toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      return result;
//    }
//    return null;
//  }
//
//  public static String[] getTableListByV2Bulk(String oid, String ip, String community, String port, int timeout, int retries, int bulksize)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0) || (bulksize < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, 1);
//
//      pdu = createPdu(1);
//      pdu.add(new VariableBinding(new OID(oid)));
//
//      pdu.setMaxRepetitions(bulksize);
//
//      ResponseEvent responseEvent = snmp.getBulk(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      String[] result = new String[response.size()];
//      int i = 0;
//      Iterator iter = response.getVariableBindings().iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding == null) {
//          continue;
//        }
//        Variable variable = variableBinding.getVariable();
//        boolean isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          result[i] = null;
//          ++i;
//        }
//
//        if (variableBinding.getOid().toString().startsWith(oid)) {
//          result[i] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//          ++i;
//        }
//      }
//      iter = result;
//
//      return iter;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  public static String[][] getTableListWithOidByV1(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    Vector vctVB = getTableListByGetNext(oid, ip, community, port, timeout, retries, 0);
//
//    if ((vctVB != null) && (vctVB.size() > 0)) {
//      String[][] result = new String[vctVB.size()][2];
//      int i = 0;
//      Iterator iter = vctVB.iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        result[i][0] = StringUtil.hexToChinese(variableBinding.getOid().toString(), variableBinding.getOid().toString());
//
//        result[i][1] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String[][] getTableListWithOidByV2(String oid, String ip, String community, String port, int timeout, int retries)
//  {
//    Vector vctVB = getTableListByGetNext(oid, ip, community, port, timeout, retries, 1);
//
//    if ((vctVB != null) && (vctVB.size() > 0)) {
//      String[][] result = new String[vctVB.size()][2];
//      int i = 0;
//      Iterator iter = vctVB.iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding == null) {
//          continue;
//        }
//        result[i][0] = StringUtil.hexToChinese(variableBinding.getOid().toString(), variableBinding.getOid().toString());
//
//        Variable variable = variableBinding.getVariable();
//        boolean isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          result[i][1] = null;
//          ++i;
//        }
//
//        result[i][1] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String[][] getTableListWithOidByV2Bulk(String oid, String ip, String community, String port, int timeout, int retries, int bulksize)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0) || (bulksize < 0))
//    {
//      return (String[][])null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, 1);
//
//      pdu = createPdu(1);
//      pdu.add(new VariableBinding(new OID(oid)));
//
//      pdu.setMaxRepetitions(bulksize);
//
//      ResponseEvent responseEvent = snmp.getBulk(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        String[][] arrayOfString1 = (String[][])null;
//        return arrayOfString1;
//      }
//      String[][] result = new String[response.size()][2];
//      int i = 0;
//      Iterator iter = response.getVariableBindings().iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding == null) {
//          continue;
//        }
//        if (!variableBinding.getOid().toString().startsWith(oid)) {
//          continue;
//        }
//        result[i][0] = StringUtil.hexToChinese(variableBinding.getOid().toString(), variableBinding.getOid().toString());
//
//        Variable variable = variableBinding.getVariable();
//        boolean isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          result[i][1] = null;
//          ++i;
//        }
//
//        result[i][1] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      iter = result;
//
//      return iter;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return (String[][])null;
//  }
//
//  public static String[][] getTableMultiListByV1(String[] oids, String ip, String community, String port, int timeout, int retries)
//  {
//    if ((oids == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return (String[][])null;
//    }
//
//    int maxLenth = 0;
//    List lst = new ArrayList();
//    for (int i = 0; i < oids.length; ++i) {
//      Vector vct = getTableListByGetNext(oids[i], ip, community, port, timeout, retries, 0);
//
//      if (vct == null)
//      {
//        lst.clear();
//        break;
//      }
//      lst.add(vct);
//      if (i == 0)
//      {
//        maxLenth = vct.size();
//      }
//
//      if (vct.size() == 0) {
//        return (String[][])null;
//      }
//      if (vct.size() < maxLenth) {
//        maxLenth = vct.size();
//      }
//    }
//
//    if (lst.size() > 0)
//    {
//      String[][] result = new String[maxLenth][oids.length];
//      int i = 0;
//      Iterator iterl = lst.iterator();
//      while (iterl.hasNext()) {
//        int j = 0;
//        Vector vct = (Vector)iterl.next();
//        Iterator iterv = vct.iterator();
//        while ((iterv.hasNext()) && (j < maxLenth)) {
//          VariableBinding vbTmp = (VariableBinding)iterv.next();
//          result[j][i] = StringUtil.hexToChinese(vbTmp.getVariable().toString(), vbTmp.getOid().toString());
//
//          ++j;
//        }
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String[][] getTableMultiListByV2(String[] oids, String ip, String community, String port, int timeout, int retries)
//  {
//    if ((oids == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return (String[][])null;
//    }
//
//    int maxLenth = 0;
//    List lst = new ArrayList();
//    for (int i = 0; i < oids.length; ++i) {
//      Vector vct = getTableListByGetNext(oids[i], ip, community, port, timeout, retries, 1);
//
//      if (vct == null)
//      {
//        lst.clear();
//        break;
//      }
//      lst.add(vct);
//      if (i == 0)
//      {
//        maxLenth = vct.size();
//      }
//
//      if (vct.size() == 0) {
//        return (String[][])null;
//      }
//      if (vct.size() < maxLenth) {
//        maxLenth = vct.size();
//      }
//    }
//
//    if (lst.size() > 0)
//    {
//      String[][] result = new String[maxLenth][oids.length];
//      int i = 0;
//      Iterator iterl = lst.iterator();
//      while (iterl.hasNext()) {
//        int j = 0;
//        Vector vct = (Vector)iterl.next();
//        Iterator iterv = vct.iterator();
//        while ((iterv.hasNext()) && (j < maxLenth)) {
//          VariableBinding variableBinding = (VariableBinding)iterv.next();
//          if (variableBinding == null) {
//            result[j][i] = null;
//            ++j;
//          }
//
//          Variable variable = variableBinding.getVariable();
//          boolean isValid = checkResponseForV2(variable);
//          if (!isValid) {
//            result[j][i] = null;
//            ++j;
//          }
//
//          result[j][i] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//          ++j;
//        }
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String[][] getTableMultiListByV2Bulk(String[] oids, String ip, String community, String port, int timeout, int retries, int bulksize)
//  {
//    if ((oids == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (community.equals("")) || (timeout < 0) || (retries < 0) || (bulksize < 0))
//    {
//      return (String[][])null;
//    }
//
//    int maxLenth = 0;
//    List lst = new ArrayList();
//    for (int i = 0; i < oids.length; ++i) {
//      Vector vct = getTableListByGetBulk(oids[i], ip, community, port, timeout, retries, 1, bulksize);
//
//      if (vct == null)
//      {
//        lst.clear();
//        break;
//      }
//      lst.add(vct);
//      if (i == 0)
//      {
//        maxLenth = vct.size();
//      }
//
//      if (vct.size() == 0) {
//        return (String[][])null;
//      }
//      if (vct.size() < maxLenth) {
//        maxLenth = vct.size();
//      }
//    }
//
//    if (lst.size() > 0)
//    {
//      String[][] result = new String[maxLenth][oids.length];
//      int i = 0;
//      Iterator iterl = lst.iterator();
//      while (iterl.hasNext()) {
//        int j = 0;
//        Vector vct = (Vector)iterl.next();
//        Iterator iterv = vct.iterator();
//        while ((iterv.hasNext()) && (j < maxLenth)) {
//          VariableBinding variableBinding = (VariableBinding)iterv.next();
//          if (variableBinding == null) {
//            result[j][i] = null;
//            ++j;
//          }
//
//          Variable variable = variableBinding.getVariable();
//          boolean isValid = checkResponseForV2(variable);
//          if (!isValid) {
//            result[j][i] = null;
//            ++j;
//          }
//
//          result[j][i] = StringUtil.hexToChinese(variableBinding.getVariable().toString(), variableBinding.getOid().toString());
//
//          ++j;
//        }
//        ++i;
//      }
//      return result;
//    }
//    return (String[][])null;
//  }
//
//  public static String setOidValueByV1(String oid, String ip, String community, String port, int timeout, int retries, String value, int type)
//  {
//    return setOidValue(oid, ip, community, port, timeout, retries, value, 0, type);
//  }
//
//  public static String setOidValueByV2(String oid, String ip, String community, String port, int timeout, int retries, String value, int type)
//  {
//    return setOidValue(oid, ip, community, port, timeout, retries, value, 1, type);
//  }
//
//  private static String createAddressString(String proType, String ip, String porPort)
//  {
//    if ((porPort == null) || (porPort.equals(""))) {
//      porPort = proPort;
//    }
//    try
//    {
//      Integer.parseInt(porPort);
//    } catch (Exception e) {
//      porPort = proPort;
//    }
//
//    StringBuffer sbAddr = new StringBuffer();
//    sbAddr.append(proType).append(":").append(ip).append("/").append(porPort);
//
//    return sbAddr.toString();
//  }
//
//
//
//  private static String getSimpleValue(String oid, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      pdu.add(new VariableBinding(new OID(oid)));
//      pdu.setType(-96);
//
//      ResponseEvent responseEvent = snmp.send(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      VariableBinding variableBinding = response.get(0);
//      if (variableBinding == null) {
//        Object localObject2 = null;
//        return localObject2;
//      }
//      Variable variable = variableBinding.getVariable();
//      boolean isValid;
//      if (version == 1) {
//        isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          Object localObject3 = null;
//          return localObject3;
//        }
//      }
//      if (variableBinding.getOid().toString().startsWith(oid)) {
//        isValid = StringUtil.hexToChinese(variable.toString(), variableBinding.getOid().toString());
//        return isValid;
//      }
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static String getNextSimpleValue(String oid, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      pdu.add(new VariableBinding(new OID(oid)));
//      pdu.setType(-95);
//
//      ResponseEvent responseEvent = snmp.send(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      VariableBinding variableBinding = response.get(0);
//      if (variableBinding == null) {
//        Object localObject2 = null;
//        return localObject2;
//      }
//      Variable variable = variableBinding.getVariable();
//      if (version == 1) {
//        isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          Object localObject3 = null;
//          return localObject3;
//        }
//      }
//      boolean isValid = StringUtil.hexToChinese(variable.toString(), variableBinding.getOid().toString());
//
//      return isValid;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static String[] getNextSimpleWithOidValue(String oid, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      pdu.add(new VariableBinding(new OID(oid)));
//      pdu.setType(-95);
//
//      ResponseEvent responseEvent = snmp.send(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      VariableBinding variableBinding = response.get(0);
//      if (variableBinding == null) {
//        Object localObject2 = null;
//        return localObject2;
//      }
//      String[] result = new String[2];
//      result[0] = variableBinding.getOid().toString();
//      Variable variable = variableBinding.getVariable();
//      if (version == 1) {
//        isValid = checkResponseForV2(variable);
//        if (!isValid) {
//          result[1] = null;
//          String[] arrayOfString1 = result;
//          return arrayOfString1;
//        }
//      }
//      result[1] = StringUtil.hexToChinese(variable.toString(), variableBinding.getOid().toString());
//
//      boolean isValid = result;
//
//      return isValid;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static Vector<VariableBinding> getTableListByGetBulk(String oid, String ip, String community, String port, int timeout, int retries, int version, int bulkSize)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0) || (bulkSize < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, 1);
//
//      pdu = createPdu(1);
//      pdu.add(new VariableBinding(new OID(oid)));
//      pdu.setMaxRepetitions(bulkSize);
//
//      ResponseEvent responseEvent = snmp.getBulk(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      Vector vct = new Vector();
//      Iterator iter = response.getVariableBindings().iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding.getOid().toString().startsWith(oid)) {
//          vct.add(variableBinding);
//        }
//      }
//      iter = vct;
//
//      return iter;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static String[] getSimpleValueList(String[] oids, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oids == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      for (int i = 0; i < oids.length; ++i) {
//        if (oids[i].startsWith(".")) {
//          oids[i] = oids[i].substring(1);
//        }
//        pdu.add(new VariableBinding(new OID(oids[i])));
//      }
//      pdu.setType(-96);
//
//      ResponseEvent responseEvent = snmp.send(pdu, target);
//      PDU response = responseEvent.getResponse();
//      if ((response == null) || (response.getErrorStatus() != 0)) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      String[] result = new String[response.size()];
//      int i = 0;
//      Iterator iter = response.getVariableBindings().iterator();
//      while (iter.hasNext()) {
//        VariableBinding variableBinding = (VariableBinding)iter.next();
//        if (variableBinding == null) {
//          result[i] = null;
//          ++i;
//        }
//
//        Variable variable = variableBinding.getVariable();
//        if (version == 1) {
//          boolean isValid = checkResponseForV2(variable);
//          if (!isValid) {
//            result[i] = null;
//            ++i;
//          }
//
//        }
//
//        result[i] = StringUtil.hexToChinese(variable.toString(), variableBinding.getOid().toString());
//
//        ++i;
//      }
//      iter = result;
//
//      return iter;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static Vector<VariableBinding[]> getTableListByMultiOidGetNext(String[] oids, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oids == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      for (String oid : oids) {
//        if ((oid != null) && (oid.startsWith("."))) {
//          oid = oid.substring(1);
//        }
//        pdu.add(new VariableBinding(new OID(oid)));
//      }
//      pdu.setType(-95);
//
//      Vector vctVB = getNextPdu(new Vector(), snmp, pdu, target, oids);
//
//      ??? = vctVB;
//
//      return ???;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static Vector<VariableBinding> getTableListByGetNext(String oid, String ip, String community, String port, int timeout, int retries, int version)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      pdu = createPdu(version);
//      pdu.add(new VariableBinding(new OID(oid)));
//      pdu.setType(-95);
//
//      Vector vctVB = getNextPdu(new Vector(), snmp, pdu, target, oid);
//
//      Vector localVector1 = vctVB;
//
//      return localVector1;
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static Vector<VariableBinding[]> getNextPdu(Vector<VariableBinding[]> result, Snmp snmp, PDU pdu, Target target, String[] colOid)
//  {
//    if ((pdu == null) || (target == null) || (colOid == null) || (result == null)) {
//      return null;
//    }
//
//    try
//    {
//      ResponseEvent responseEvent = snmp.getNext(pdu, target);
//      PDU reponse = responseEvent.getResponse();
//      int responseSize = pdu.getVariableBindings().size();
//      if ((reponse != null) && (reponse.getErrorStatus() == 0)) {
//        VariableBinding[] bings = new VariableBinding[responseSize];
//
//        String lastOid = "";
//        if (responseSize <= 1)
//        {
//          lastOid = pdu.get(responseSize - 1).getOid().toString();
//        }
//        for (int i = 0; i < bings.length; ++i) {
//          VariableBinding vbTmp = reponse.get(i);
//          if ((vbTmp != null) && (vbTmp.getOid() != null) && (vbTmp.getVariable() != null) && (vbTmp.getVariable().getSyntax() != 5) && (vbTmp.getVariable().getSyntax() != 130) && (vbTmp.getOid().toString().startsWith(colOid[i])))
//          {
//            if (lastOid.equals(vbTmp.getOid().toString())) {
//              return result;
//            }
//            lastOid = vbTmp.getOid().toString();
//
//            if (result.size() > 1000) {
//              return result;
//            }
//            bings[i] = vbTmp;
//          } else {
//            return result;
//          }
//        }
//        result.add(bings);
//      } else {
//        return result;
//      }
//
//      getNextPdu(result, snmp, reponse, target, colOid);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return result;
//    }
//    return result;
//  }
//
//  private static Vector<VariableBinding> getNextPdu(Vector<VariableBinding> result, Snmp snmp, PDU pdu, Target target, String colOid)
//  {
//    if ((pdu == null) || (target == null) || (colOid == null) || (result == null)) {
//      return null;
//    }
//
//    VariableBinding variableBinding = pdu.get(0);
//    if (variableBinding == null) {
//      return null;
//    }
//    try
//    {
//      String nextOid = variableBinding.getOid().toString();
//      PDU newPdu = new PDU();
//      newPdu.add(new VariableBinding(new OID(nextOid)));
//
//      ResponseEvent responseEvent = snmp.getNext(pdu, target);
//      PDU reponse = responseEvent.getResponse();
//      if ((reponse != null) && (reponse.getErrorStatus() == 0)) {
//        VariableBinding vbTmp = reponse.get(0);
//
//        if ((vbTmp != null) && (vbTmp.getVariable() != null) && (vbTmp.getOid() != null) && (vbTmp.getVariable().getSyntax() != 5) && (vbTmp.getVariable().getSyntax() != 128) && (vbTmp.getVariable().getSyntax() != 129) && (vbTmp.getVariable().getSyntax() != 130) && (vbTmp.getOid().toString().startsWith(colOid)) && (!vbTmp.getOid().toString().equals(nextOid)))
//        {
//          if (result.size() > 1000) {
//            return result;
//          }
//          result.add(vbTmp);
//        } else {
//          return result;
//        }
//      } else {
//        return result;
//      }
//
//      getNextPdu(result, snmp, reponse, target, colOid);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return result;
//    }
//    return result;
//  }
//
//  public static String setOidValue(String oid, String ip, String community, String port, int timeout, int retries, String variableStr, int version, int variableType)
//  {
//    if ((oid == null) || (!Validator.checkIPWildcard(ip)) || (community == null) || (oid.equals("")) || (community.equals("")) || (timeout < 0) || (retries < 0) || (variableStr == null) || (variableStr.equals("")))
//    {
//      return null;
//    }
//
//    if (oid.startsWith(".")) {
//      oid = oid.substring(1);
//    }
//
//    PDU pdu = null;
//    CommunityTarget target = null;
//    try
//    {
//      String addr = createAddressString(proType, ip, port);
//      Address targetAddress = GenericAddress.parse(addr);
//
//      target = createCommunityTarget(targetAddress, community, retries, timeout, version);
//
//      if (version == 0)
//        pdu = new PDUv1();
//      else {
//        pdu = new PDU();
//      }
//
//      Variable variable = null;
//      if (variableType == 1)
//        variable = new OctetString(variableStr);
//      else if (variableType == 2) {
//        variable = new Integer32(Integer.parseInt(variableStr));
//      }
//
//      if (variable == null) {
//        Object localObject1 = null;
//        return localObject1;
//      }
//      VariableBinding vb = new VariableBinding(new OID(oid));
//      vb.setVariable(variable);
//      pdu.add(vb);
//      pdu.setType(-93);
//
//      ResponseEvent responseEvent = snmp.send(pdu, target);
//      PDU reponse = responseEvent.getResponse();
//      if ((reponse != null) && (reponse.getErrorStatus() == 0)) {
//        VariableBinding variableBinding = reponse.get(0);
//        if (variableBinding != null) {
//          String value = variableBinding.getVariable().toString();
//
//          String str1 = value;
//          return str1;
//        }
//      }
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//    finally {
//      pdu.clear();
//
//      pdu = null;
//
//      target = null;
//    }
//    return null;
//  }
//
//  private static boolean checkResponseForV2(Variable variable)
//  {
//    int syntax = variable.getSyntax();
//
//    return (syntax != 5) && (syntax != 128) && (syntax != 129) && (syntax != 130);
//  }

  static
  {
    try
    {
      transport = new DefaultUdpTransportMapping();
      transport.listen();
      snmp = new Snmp(transport);
      //snmp.listen();
      //SysLogger.info("##########################");
      //SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}