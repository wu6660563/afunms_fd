/**
 * <p>Description:IP Locate Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-10-12
 */

package com.afunms.ipresource.manage;

import java.util.List;

import com.afunms.common.base.*;
import com.afunms.common.util.NetworkUtil;
import com.afunms.ipresource.dao.*;
import com.afunms.ipresource.model.IpResource;
import com.afunms.ipresource.util.DrawIPTable;
import com.afunms.ipresource.util.IpResourceReport;
import com.afunms.report.abstraction.ExcelReport;
import com.afunms.report.base.AbstractionReport;

public class IpResourceManager extends BaseManager implements ManagerInterface
{
   public IpResourceManager()
   {
   }
   private String list()
   {
	   IpResourceDao dao = new IpResourceDao();
	   List list = dao.listByPage(getCurrentPage());
	   
 	   request.setAttribute("page",dao.getPage());
 	   request.setAttribute("list",list);
 	      
       return "/ipresource/list.jsp";
   }

   private String find()
   {	   
	   String value = getParaValue("value");
	   String key = getParaValue("key");
	   IpResourceDao dao = new IpResourceDao();
	   IpResource ipr = dao.find(key, value);
	   request.setAttribute("vo",ipr);	   
	   return "/ipresource/find.jsp";	       
   }
   
   private String report()
   {
     	AbstractionReport report = new ExcelReport(new IpResourceReport());        
		report.createReport();
		
		return "/inform/report/download.jsp?filename=" + report.getFileName();
   }	
   
   /**
    * IP分布
    */
   private String detail()
   {
	  String jsp = null;  
      try
      {        
         String beginip = getParaValue("beginip");
         String endip = getParaValue("endip");
         if(beginip==null) beginip = "10.10.20.0";
         if(endip==null) endip = "10.10.20.255";

         String outPutInfo = null;
         if(!NetworkUtil.checkIp(beginip)||!NetworkUtil.checkIp(endip))
        	outPutInfo = "<font color='red'>无效IP地址,请正确输入IP地址!</font>";
         else
         {	 
            long temp1 = NetworkUtil.ip2long(beginip);
            long temp2 = NetworkUtil.ip2long(endip);
            if(temp1>=temp2) 
        	   outPutInfo = "<font color='red'>起点IP必须小于终点IP,请重新输入!</font>";            
            else if(temp2-temp1>255) 
        	   outPutInfo = "<font color='red'>输入的两个IP不在同一网段,请重新输入!</font>";
            else
            { 
               DrawIPTable ipTable = new DrawIPTable();
               outPutInfo = ipTable.drawTable(beginip,endip,request.getContextPath());
            }
         }   
         request.setAttribute("beginip",beginip);
         request.setAttribute("endip",endip);
         request.setAttribute("out_put_info",outPutInfo);
         jsp = "/ipresource/table.jsp";
      }
      catch(Exception sqle)
      {
         jsp = null;
      }
      return jsp;
  }   
//   private String detail()
//   {
//	   String jsp = "/ipresource/ip_locate.jsp";
//	   String address = getParaValue("address");
//	   if(address==null)
//	   {
//		   request.setAttribute("address","");
//		   request.setAttribute("result","");
//           return jsp;
//	   }	 
//	   if(!NetworkUtil.checkIp(address)) 
//	   {
//		   request.setAttribute("address",address);
//		   request.setAttribute("result","无效IP地址");
//           return jsp;
//	   }	 
//	   
//	   StringBuffer result = new StringBuffer(100);
//	   FdbTableDao dao = new FdbTableDao();
//	   FdbItem item = dao.locateIp(address);
//	   if(item==null)
//		  result.append("对不起,无法确定!");
//	   else 
//	   {
//		   Host host = (Host)PollingEngine.getInstance().getNodeByID(item.getNodeId());
//		   if(item.isPort())
//		   {
//			   result.append(address);
//			   result.append("是设备");
//			   result.append(host.getAlias());
//			   result.append("(");
//			   result.append(host.getIpAddress());
//			   result.append(")");
//			   result.append("上的一个接口,接口为");
//			   result.append(item.getIfDescr());
//			   result.append("(");
//			   result.append(item.getIfIndex());
//			   result.append(")");
//		   }
//		   else
//		   {
//		       IfEntity ifObj = host.getIfEntityByIndex(item.getIfIndex());
//		       result.append("与");
//		       result.append(address);
//		       result.append("直连的设备是:");
//		       result.append(host.getAlias());
//		       result.append("(");
//		       result.append(host.getIpAddress());
//		       result.append(")");
//		       result.append(",接口为");
//			   result.append(ifObj.getDescr());
//			   result.append("(");
//			   result.append(item.getIfIndex());
//			   result.append(")");
//		   }
//	   }
//	   dao.close();
//	   request.setAttribute("address",address);
//	   request.setAttribute("result",result.toString());
//	   
//	   return jsp;
//   }
   
   public String execute(String action)
   {
      if(action.equals("detail"))
      	 return detail();
      if(action.equals("list"))
	     return list();
      if(action.equals("find"))
	     return find();
      if(action.equals("report"))
	     return report();
      
      setErrorCode(ErrorMessage.ACTION_NO_FOUND);
      return null;
   }
}