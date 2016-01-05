/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.JspPage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.DistrictDao;
import com.afunms.config.dao.IPDistrictDao;
import com.afunms.config.dao.MacconfigDao;
import com.afunms.config.model.DistrictConfig;
import com.afunms.config.model.IPDistrictConfig;
import com.afunms.config.model.Macconfig;
import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.portscan.dao.PortScanDao;
import com.afunms.portscan.model.PortConfig;
import com.afunms.portscan.model.PortScanConfig;
import com.afunms.portscan.util.PortScanUtil;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.topology.dao.IpDistrictMatchConfigDao;
import com.afunms.topology.dao.IpMacDao;
import com.afunms.topology.model.IpDistrictDetail;
import com.afunms.topology.model.IpDistrictMatchConfig;
import com.afunms.topology.model.NetDistrictDetail;
import com.afunms.topology.model.NetDistrictIpDetail;
import com.afunms.topology.util.ComparatorIpDistrictMatchConfig;
import com.afunms.topology.util.IPDistrictMatchUtil;

/**
 * ip���ι���
 */
public class IpDistrictMatchManager extends BaseManager implements ManagerInterface
{
    public String execute(String action)
    {
        if("list".equals(action)){
        	return list();
        }
        if("districtDetails".equals(action)){
        	return districtDetails();
        }
        if("netDistrictDetails".equals(action)){
        	return netDistrictDetails();
        }
        if("netDistrictIpDetails".equals(action)){
        	return netDistrictIpDetails();
        }
        if("searchNetDistrictIpByIp".equals(action)){
        	return searchNetDistrictIpByIp();
        }
        if("searchNetDistrictIpByWhere".equals(action)){
        	return searchNetDistrictIpByWhere();
        }
        if("createReport".endsWith(action)){
        	return createReport();
        }
        if("portscan".equals(action)){
        	return portScan();
        }
        if("savePortScan".equals(action)){
        	return savePortScan();
        }
        if("searchPortScanByIp".equals(action)){
        	return searchPortScanByIp();
        }
        if("ready_addPortScan".equals(action)){
        	return ready_addPortScan();
        }
        if("hostCompositeReport".equals(action))
        {
        	return hostCompositeReport();
        }
        if("hostPingReport".equals(action))
        {
        	return hostPingReport();
        }
        if("hostCapacityReport".equals(action))
        {
        	return hostCapacityReport();
        }
        if("hostDiskReport".equals(action))
        {
        	return hostDiskReport();
        }
        if("hostAnalyseReport".equals(action))
        {
        	return hostAnalyseReport();
        }
        if("networkPingReport".equals(action))
        {
        	return networkPingReport();
        }
        if("networkEventReport".equals(action))
        {
        	return networkEventReport();
        }
        if("networkCompositeReport".equals(action))
        {
        	return networkCompositeReport();
        }
        if("networkConfigReport".equals(action))
        {
        	return networkConfigReport();
        }
        if("addPortScan".equals(action)){
        	return addPortScan();
        }
        if("delete_portscan".equals(action)){
        	return delete_portscan();
        }
        if("createReport_portscan".equals(action)){
        	return createReport_portscan();
        }
        
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }
   
    public String list(){
    	
    	List list = new ArrayList();
    	
    	String refresh = getParaValue("refresh");
    	if("refresh".equals(refresh)){
    		//���ͬ��ˢ�� �����ˢ��
    		list = refresh();
    		
    		//ˢ����� �����õ�list ��Ϊnull�Ҵ�С����0 ��������� ��ˢ�µĺ�����ݴ������ݿ���
    		if(list != null && list.size() >0){
    			IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
    			try {
    				ipDistrictMatchConfigDao.deleteAll();
    				ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
    				ipDistrictMatchConfigDao.saveBath(list);
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					ipDistrictMatchConfigDao.close();
				}
    		}
    	}
//    	else{
//    		IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
//    		try {
//				list = ipDistrictMatchConfigDao.loadAll();
//			} catch (RuntimeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally{
//				ipDistrictMatchConfigDao.close();
//			}
//    	}
		//request.setAttribute("list", list);
    	
    	List districtList = null;
    	
    	DistrictDao districtDao = new DistrictDao();
    	try {
			districtList = districtDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			districtDao.close();
		}
    	
		request.setAttribute("districtList", districtList);
    	IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
		String jsp = "/topology/ipregionalmatch/list.jsp";
		setTarget(jsp);
        return list(ipDistrictMatchConfigDao);
    }
    
    private String districtDetails(){
    	DistrictDao districtDao = new DistrictDao();
//    	List districtList = null;
//		try {
//			districtList = districtDao.loadAll();
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	String jsp = "/topology/ipregional/list.jsp";
    	setTarget(jsp);
    	
    	String where = getWhere();
    	
    	// ���÷�ҳ���ҳ�����
    	list(districtDao , where);
    	
    	List districtList = (List)request.getAttribute("list");
    	
    	request.setAttribute("districtList", districtList);
    	
		List list = new ArrayList();
		if( districtList != null && districtList.size() > 0){
	    	for(int i =0 ; i < districtList.size() ; i++){
	    		
	    		long ipTotal = 0L;       // �������ip����
	    		long usedTotal = 0L;     // ������ʹ������
	    		long unusedTotal = 0L;   // ������δʹ������
	    		long isOnlineTotal = 0L; // ��������������
				long unOnlineTotal = 0L; // ��������������
				
	    		
	    		
	    		// ȡ��ÿ��������м���
	    		DistrictConfig districtConfig = (DistrictConfig)districtList.get(i);
	    		int districtId = districtConfig.getId();
	    		IPDistrictDao ipDistrictDao = new IPDistrictDao();
	    		
	    		List ipDistrictList = null;
				try {
					ipDistrictList = ipDistrictDao.loadByDistrictId(String.valueOf(districtId));
				} catch (RuntimeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally{
					ipDistrictDao.close();
				}
	    		
	    		
	    		for(int j = 0; j <ipDistrictList.size() ; j++ ){
	    			// ȡ��ÿ������ĸ������� ��������ip����
	    			IPDistrictConfig iPDistrictConfig = (IPDistrictConfig)ipDistrictList.get(j);
	    			String startip = iPDistrictConfig.getStartip();
	    			String endip = iPDistrictConfig.getEndip();
	    			if(startip!=null && endip!=null){
	    				long startipLong = ip2long(startip);
	    				long endipLong = ip2long(endip);
	    				ipTotal = ipTotal + endipLong - startipLong;
	    			}else if( startip!=null && endip==null ){
	    				ipTotal = ipTotal + 1;
	    			}
	    		}
	    		
	    		List ipDistrictMatchConfiglist = null;
	    		IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
	    		try {
	    			ipDistrictMatchConfiglist = ipDistrictMatchConfigDao.findByOriDistrictId(String.valueOf(districtId));
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					ipDistrictMatchConfigDao.close();
				}
				
				if( ipDistrictMatchConfiglist != null && ipDistrictMatchConfiglist.size() > 0){
					for(int k = 0 ; k < ipDistrictMatchConfiglist.size() ; k++){
						// ȡ��ÿ��ɨ�������ip ��������������δ��������
						IpDistrictMatchConfig ipDistrictMatchConfig = (IpDistrictMatchConfig)ipDistrictMatchConfiglist.get(k);
						String isOnline = ipDistrictMatchConfig.getIsOnline();
						if("1".equals(isOnline)){
							isOnlineTotal = isOnlineTotal + 1;
						}else{
							unOnlineTotal = unOnlineTotal + 1;
						}
					}
					
					usedTotal = ipDistrictMatchConfiglist.size();
				}
				
				if( ipTotal > usedTotal){
					// ��������ip��������ʹ�õ����������δʹ�õ� ����Ϊ0
					unusedTotal = ipTotal - usedTotal;
				}
				
				IpDistrictDetail ipDistrictDetail = new IpDistrictDetail();
				ipDistrictDetail.setId(districtConfig.getId());
				ipDistrictDetail.setDistrict(districtConfig.getName());
				ipDistrictDetail.setIpTotal(String.valueOf(ipTotal));
				ipDistrictDetail.setUsedTotal(String.valueOf(usedTotal));
				ipDistrictDetail.setUnusedTotal(String.valueOf(unusedTotal));
				ipDistrictDetail.setIsOnlineTotal(String.valueOf(isOnlineTotal));
				ipDistrictDetail.setUnOnlineToatl(String.valueOf(unOnlineTotal));
				list.add(ipDistrictDetail);
	    	}
		}
    	request.setAttribute("list", list);
    	return "/topology/ipregional/list.jsp";
    }
    
    private String netDistrictDetails(){
    	
    	List list = new ArrayList();
    	
    	String districtId = getParaValue("districtId");
    	
    	DistrictConfig districtConfig = null;
    	
    	DistrictDao districtDao = new DistrictDao();
		try {
			districtConfig =  (DistrictConfig)districtDao.findByID(districtId);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			districtDao.close();
		}
		
		if(districtConfig == null){
			return "/topology/ipregional/netDistrictDetail.jsp";
		}
		
		request.setAttribute("districtConfig", districtConfig);
    	
		IPDistrictDao ipDistrictDao = new IPDistrictDao();
		List ipDistrictList = null;
		try {
			ipDistrictList = ipDistrictDao.loadByDistrictId(districtId);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			ipDistrictDao.close();
		}
		
		
		
		
		List ipDistrictMatchConfiglist = null;
		IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
		try {
			ipDistrictMatchConfiglist = ipDistrictMatchConfigDao.findByOriDistrictId(String.valueOf(districtId));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ipDistrictMatchConfigDao.close();
		}
		
		if( ipDistrictList != null && ipDistrictList.size() > 0 ){
			for(int j = 0; j <ipDistrictList.size() ; j++ ){
				String netDistrictName = "";
				long ipTotal = 0L;
				long usedTotal = 0L;
				long unusedTotal = 0L;
				long isOnlineTotal = 0L;
				long unOnlineTotal = 0L;
				
				IPDistrictConfig iPDistrictConfig = (IPDistrictConfig)ipDistrictList.get(j);
				String startip = iPDistrictConfig.getStartip();
				String endip = iPDistrictConfig.getEndip();
				
				long startipLong = -1L;
				long endipLong = -1L;
				if(startip!=null && endip!=null){
					startipLong= ip2long(startip);
					endipLong = ip2long(endip);
					ipTotal = ipTotal + endipLong - startipLong;
					netDistrictName = startip + "---" + endip;
				}else if( startip!=null && endip==null ){
					startipLong= ip2long(startip);
					ipTotal = ipTotal + 1;
					netDistrictName = startip;
				}else{
					continue;
				}
				
				if(ipDistrictMatchConfiglist != null && ipDistrictMatchConfiglist.size() > 0){
					for( int i = 0 ; i < ipDistrictMatchConfiglist.size() ; i ++){
						IpDistrictMatchConfig ipDistrictMatchConfig = (IpDistrictMatchConfig)ipDistrictMatchConfiglist.get(i);
						String nodeIp = ipDistrictMatchConfig.getNodeIp();
						long nodeIpLong = ip2long(nodeIp);
						if( nodeIpLong >= startipLong || nodeIpLong < endipLong){
							usedTotal = usedTotal + 1;
							if("1".equals(ipDistrictMatchConfig.getIsOnline())){
								isOnlineTotal = isOnlineTotal + 1;
							}else{
								unOnlineTotal = unOnlineTotal + 1;
							}
						}
						
						
					}
				}
				unusedTotal = ipTotal - usedTotal;
				NetDistrictDetail netDistrictDetail = new NetDistrictDetail();
				netDistrictDetail.setId(iPDistrictConfig.getId());
				netDistrictDetail.setDistrictId(districtId);
				netDistrictDetail.setIpDistrictId(String.valueOf(iPDistrictConfig.getId()));
				netDistrictDetail.setDistrictName(districtConfig.getName());
				netDistrictDetail.setNetDistrictName(netDistrictName);
				netDistrictDetail.setIpTotal(String.valueOf(ipTotal));
				netDistrictDetail.setUsedTotal(String.valueOf(usedTotal));
				netDistrictDetail.setUnusedTotal(String.valueOf(unusedTotal));
				netDistrictDetail.setIsOnlineTotal(String.valueOf(isOnlineTotal));
				netDistrictDetail.setUnOnlineToatal(String.valueOf(unOnlineTotal));
				list.add(netDistrictDetail);
			}
		}
		request.setAttribute("list", list);
		
    	return "/topology/ipregional/netDistrictDetail.jsp";
    }
    
    private String netDistrictIpDetails(){
    	request.setAttribute("beforeAction", "netDistrictIpDetails");
    	request.setAttribute("list", getListByPage(getAllDistrictIp()));
    	return "/topology/ipregional/netDistrictIpDetail.jsp";
    }
    
    private String searchNetDistrictIpByIp(){
    	String searchIp = getParaValue("searchIp");
    	List list = getNetDistrictIpListByIp(searchIp);
    	
    	request.setAttribute("beforeAction", "searchNetDistrictIpByIp");
    	request.setAttribute("searchIp", searchIp);
    	request.setAttribute("list", getListByPage(list));
    	return "/topology/ipregional/netDistrictIpDetail.jsp";
    }
    
    private List getNetDistrictIpListByIp(String searchIp){
    	List allNetDistrictIplist = getAllDistrictIp();
    	List list = new ArrayList();
    	
    	if(searchIp !=null && searchIp.trim().length() > 0 && 
    			allNetDistrictIplist!= null && allNetDistrictIplist.size()>0){
    		for(int i = 0 ; i < allNetDistrictIplist.size() ; i++){
    			NetDistrictIpDetail netDistrictIpDetail = (NetDistrictIpDetail)allNetDistrictIplist.get(i);
    			if( searchIp.equals(netDistrictIpDetail.getIpaddress().trim())){
    				list.add(netDistrictIpDetail);
    			}
    		}
    	}
    	
    	return list;
    }
    
    
    private String searchNetDistrictIpByWhere(){
    	String isUsed = getParaValue("isUsed");
    	String isOnline = getParaValue("isOnline");
    	
    	if(isUsed==null || ("-1").equals(isUsed.trim())){
    		isUsed = "-1";
    	}
    	
    	if(isOnline==null || ("-1").equals(isOnline.trim())){
    		isOnline = "-1";
    	}
    	
    	String isUsed_trim = isUsed.trim();
    	String isOnline_trim = isOnline.trim();
    	
    	List list = getNetDistrictIpListByWhere(isUsed_trim, isOnline_trim);
    	
    	request.setAttribute("beforeAction", "searchNetDistrictIpByWhere");
    	request.setAttribute("isUsed", isUsed_trim);
    	request.setAttribute("isOnline", isOnline_trim);
    	request.setAttribute("list", getListByPage(list));
    	return "/topology/ipregional/netDistrictIpDetail.jsp";
    }
    
    private List getNetDistrictIpListByWhere(String isUsed_trim , String isOnline_trim){
    	boolean isUsed_trim_b = false;
    	boolean isOnline_trim_b = false;
    	
    	
    	List allNetDistrictIplist = getAllDistrictIp();
    	List list = new ArrayList();
    	
    	
    	if(allNetDistrictIplist!= null && allNetDistrictIplist.size()>0){
    		if("-1".equals(isUsed_trim)&&"-1".equals(isOnline_trim)){
    			list = allNetDistrictIplist;
    		}else if("-1".equals(isUsed_trim)&& !"-1".equals(isOnline_trim)){
    			for(int i = 0 ; i < allNetDistrictIplist.size() ; i++){
        			NetDistrictIpDetail netDistrictIpDetail = (NetDistrictIpDetail)allNetDistrictIplist.get(i);
        			isOnline_trim_b = isOnline_trim.equals(netDistrictIpDetail.getIsOnline().trim());
        			
        			if( isOnline_trim_b ){
        				list.add(netDistrictIpDetail);
        			}
        		}
    		}else if( !"-1".equals(isUsed_trim)&& "-1".equals(isOnline_trim)){
    			for(int i = 0 ; i < allNetDistrictIplist.size() ; i++){
        			NetDistrictIpDetail netDistrictIpDetail = (NetDistrictIpDetail)allNetDistrictIplist.get(i);
        			isUsed_trim_b = isUsed_trim.equals(netDistrictIpDetail.getIsUsed().trim());
        			if( isUsed_trim_b ){
        				list.add(netDistrictIpDetail);
        			}
        		}
    		}else if( !"-1".equals(isUsed_trim)&& !"-1".equals(isOnline_trim)){
    			for(int i = 0 ; i < allNetDistrictIplist.size() ; i++){
        			NetDistrictIpDetail netDistrictIpDetail = (NetDistrictIpDetail)allNetDistrictIplist.get(i);
        			
        			
        			isOnline_trim_b = isOnline_trim.equals(netDistrictIpDetail.getIsOnline().trim());
        			isUsed_trim_b = isUsed_trim.equals(netDistrictIpDetail.getIsUsed().trim());
        			if( isUsed_trim_b && isOnline_trim_b){
        				list.add(netDistrictIpDetail);
        			}
        		}
    		}
    		
    	}
    	
    	return list;
    }
    
    private String createReport(){
    	String beforeAction = getParaValue("beforeAction");
    	List list = null;
    	if( beforeAction == null || beforeAction.trim().length() == 0){
    		beforeAction = "netDistrictIpDetails";
    	}
    	
    	if( "netDistrictIpDetails".equals(beforeAction)){
    		list = getAllDistrictIp();
    	}else if("searchNetDistrictIpByIp".equals(beforeAction)){
    		String searchIp = getParaValue("searchIp");
    		list = getNetDistrictIpListByIp(searchIp);
    	}else if("searchNetDistrictIpByWhere".equals(beforeAction)){
    		String isUsed = getParaValue("isUsed");
        	String isOnline = getParaValue("isOnline");
        	if(isUsed==null || ("-1").equals(isUsed.trim())){
        		isUsed = "-1";
        	}
        	
        	if(isOnline==null || ("-1").equals(isOnline.trim())){
        		isOnline = "-1";
        	}
        	
        	String isUsed_trim = isUsed.trim();
        	String isOnline_trim = isOnline.trim();
        	
        	list = getNetDistrictIpListByWhere(isUsed_trim, isOnline_trim);
    	}
    	
    	String districtName = (String)request.getAttribute("districtName");
    	String netDistrictName = (String)request.getAttribute("netDistrictName");
    	AbstractionReport1 abstractionReport1 = new ExcelReport1(new IpResourceReport());
    	abstractionReport1.createReport_netDistrictIplist("temp/netDistrictIplist_report.xls", "����" + districtName +"    " + "���Σ�" + netDistrictName, list);
    	request.setAttribute("filename", abstractionReport1.getFileName());

    	
    	return "/topology/ipregional/download.jsp";
    }
    
    private String portScan(){
    	String allIpaddress_str = getParaValue("ipaddress");
    	String[] allIpaddress = null; 
    	
    	if(allIpaddress_str!=null && allIpaddress_str.trim().length()>0){
    		allIpaddress = allIpaddress_str.split("-");
    	}
    	
    	String times = "1";
    	
    	String refresh = getParaValue("refresh");
    	List list = new ArrayList();
    	
    	//ɨ���Ƿ����
    	boolean status = true;
    	
    	int scanTotal = 0;
    	
    	int isScannedTotal = 0;
    	
    	int unScannedTotal = 0;
    	
    	PortScanUtil portScanUtil = PortScanUtil.getInstance();
    	
    	if(refresh == null || !"refresh".equals(refresh)){
    		if(allIpaddress!=null && allIpaddress.length>0){
    	    	for(int i = 0 ; i < allIpaddress.length ; i ++){
    	    		String ipaddress = allIpaddress[i];
					portScanUtil.init(ipaddress);
					PortScanConfig  portScanConfig= (PortScanConfig)portScanUtil.getData().get(ipaddress);
		    		portScanConfig.setStatus("0");
		    		List isScannedList = portScanConfig.getIsScannedList();
		    		List unScannedList = portScanConfig.getUnScannedList();
		    		unScannedList.addAll(isScannedList);
					isScannedList.removeAll(isScannedList);
		    		portScanUtil.scan(ipaddress);
    	    	}
    	    	
    	    }
    		
    		times = "0";
    	}
    	
    	if(allIpaddress!=null && allIpaddress.length>0){
	    	for(int i = 0 ; i < allIpaddress.length ; i ++){
	    		String ipaddress = allIpaddress[i];
	    		PortScanConfig  portScanConfig= (PortScanConfig)portScanUtil.getData().get(ipaddress);
	    		if(!"1".equals(portScanConfig.getStatus())){
	    			status = false;
	    		}
	    		
	    		isScannedTotal = isScannedTotal + portScanConfig.getIsScannedList().size();
	    		
	    		unScannedTotal = unScannedTotal + portScanConfig.getUnScannedList().size();
	    		
	    		portScanConfig.setTotal(portScanConfig.getIsScannedList().size()+portScanConfig.getUnScannedList().size());
	    		
	    		scanTotal = scanTotal + portScanConfig.getTotal();
	    		list.add(portScanConfig);
	    	}
    	}
    	System.out.println(status);
    	request.setAttribute("status", status);
    	
    	request.setAttribute("isScannedTotal", isScannedTotal+"");
    	
    	request.setAttribute("unScannedTotal", unScannedTotal+"");
    	
    	request.setAttribute("times", times);
    	
    	request.setAttribute("scanTotal", scanTotal+"");
    	
    	request.setAttribute("ipaddress", allIpaddress_str);
    	
    	request.setAttribute("list", list);
    	return "/topology/ipregional/portscan.jsp";
    }
    
    public String savePortScan(){
    	
    	String allIpaddress_str = getParaValue("ipaddress");
    	String[] allIpaddress = null; 
    	
    	if(allIpaddress_str!=null){
    		allIpaddress = allIpaddress_str.split("-");
    	}
    	List list = new ArrayList();
    	List ipaddresslist = new ArrayList();
    	if(allIpaddress!=null && allIpaddress.length>0){
    		Hashtable data = PortScanUtil.getData();
    		for(int i = 0 ; i < allIpaddress.length ; i++){
    			String ipaddress = allIpaddress[i];
    			PortScanConfig portScanConfig =  (PortScanConfig)data.get(ipaddress);
    			List isScannedList = portScanConfig.getIsScannedList();
    			if(isScannedList!=null){
    				list.addAll(isScannedList);
    			}
    			
    			ipaddresslist.add(ipaddress);
    		}
    		
    	}
    	
    	PortScanDao portScanDao = new PortScanDao();
    	try {
			portScanDao.deleteByIpaddress(ipaddresslist);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
    	
    	portScanDao = new PortScanDao();
    	try {
			portScanDao.saveBatch(list);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
    	
    	return portScan();
    }
    
    private String searchPortScanByIp(){
    	List list = new ArrayList();
    	String ipaddress = getParaValue("ipaddress");
    	PortScanDao portScanDao = new PortScanDao();
    	try {
    		list = portScanDao.findByIpaddress(ipaddress);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
		request.setAttribute("list", list);
		request.setAttribute("ipaddress", ipaddress);
    	return "/topology/ipregional/portscanlist.jsp";
    }
    
    private String ready_addPortScan(){
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	return "/topology/ipregional/addportscan.jsp";
    }
    private String hostCompositeReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	request.setAttribute("type", 1);
    	return "/capreport/host/compositeReport.jsp";
    }
    private String hostPingReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	request.setAttribute("type", 1);
    	return "/capreport/host/pingReport.jsp";
    }
    private String hostCapacityReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	request.setAttribute("type", 1);
    	return "/capreport/host/capacityReport.jsp";
    }
    private String hostDiskReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	request.setAttribute("type", 1);
    	return "/capreport/host/diskReport.jsp";
    }
    private String hostAnalyseReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	request.setAttribute("type", 2);
    	return "/capreport/host/analyseReport.jsp";
    }
    private String networkPingReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	return "/capreport/net/networkPingReport.jsp";
    }
    private String networkEventReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	return "/capreport/net/networkEventReport.jsp";
    }
    private String networkCompositeReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	return "/capreport/net/networkCompositeReport.jsp";
    }
    private String networkConfigReport()
    {
    	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = new Date();
		String startdate = getParaValue("startdate");
		if(startdate==null){
			startdate = sdf0.format(d);
		}
		String todate = getParaValue("todate");
		if(todate==null){
			todate = sdf0.format(d);
		}
		request.setAttribute("startdate",startdate);
		request.setAttribute("todate",todate);
    	String ipaddress = getParaValue("ipaddress");
    	request.setAttribute("ipaddress", ipaddress);
    	return "/capreport/net/networkConfigReport.jsp";
    }
    
    private String addPortScan(){
    	String ipaddress = getParaValue("ipaddress");
    	String startport = getParaValue("startport");
    	String endport = getParaValue("endport");
    	String portName = getParaValue("portName");
    	String description = getParaValue("description");
    	String type = getParaValue("type");
    	String timeout = getParaValue("timeout");
    	
    	
    	if(portName==null || portName.trim().length() == 0){
    		portName = "δ����";
    	}
    	
    	if(description==null || description.trim().length() == 0){
    		description = "δ����";
    	}
    	
    	if(type==null || type.trim().length() == 0){
    		type = "δ����";
    	}
    	
    	int startport_int = 0 ;
    	
    	int endport_int = 0;
    	if(startport!=null && startport.trim().length()>0){
    		try {
				startport_int = Integer.parseInt(startport);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	if(endport!=null&& endport.trim().length()>0){
    		try {
    			endport_int = Integer.parseInt(endport) +1 ;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				endport_int = startport_int + 1;
			}
    	}else {
    		endport_int = startport_int + 1;
    	}
    	
    	PortScanDao portScanDao = new PortScanDao();
    	List list = null;
    	try {
			list = portScanDao.findByIpaddress(ipaddress);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
		
		if(list == null || list.size() == 0){
			list = new ArrayList();
		}
    	
    	for(int i = startport_int ; i < endport_int ; i++){
    		PortConfig portConfig = new PortConfig();
    		int add_port = i ;
    		for(int j = 0 ; j < list.size() ; j++){
    			PortConfig portConfig2 = (PortConfig)list.get(j);
    			if(add_port == Integer.parseInt(portConfig2.getPort())){
    				
    				list.remove(j);
    				break;
    			}
    			
    		}
    		portConfig.setIsScanned("0");
    		portConfig.setStatus("0");
			portConfig.setScantime("-- --");
    		portConfig.setIpaddress(ipaddress);
			portConfig.setPort(String.valueOf(add_port));
			portConfig.setDescription(description);
			portConfig.setPortName(portName);
			portConfig.setTimeout(timeout);
			portConfig.setType(type);
			list.add(portConfig);
    		
    	}
    	
    	portScanDao = new PortScanDao();
    	try {
			portScanDao.deleteByIpaddress(ipaddress);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
    	
		portScanDao = new PortScanDao();
		try {
			portScanDao.saveBatch(list);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
		
		
    	request.setAttribute("ipaddress", ipaddress);
    	return "/";
    }
    
    private String delete_portscan(){
    	String[] ids = getParaArrayValue("checkbox");
    	PortScanDao portScanDao = new PortScanDao();
    	try {
			portScanDao.delete(ids);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			portScanDao.close();
		}
    	return searchPortScanByIp();
    }
    
    private String createReport_portscan(){
    	
    	String allIpaddress_str = getParaValue("ipaddress");
    	String[] allIpaddress = null; 
    	
    	if(allIpaddress_str!=null){
    		allIpaddress = allIpaddress_str.split("-");
    	}
    	List list = new ArrayList();
    	List ipaddresslist = new ArrayList();
    	if(allIpaddress!=null && allIpaddress.length>0){
    		Hashtable data = PortScanUtil.getData();
    		for(int i = 0 ; i < allIpaddress.length ; i++){
    			String ipaddress = allIpaddress[i];
    			PortScanConfig portScanConfig =  (PortScanConfig)data.get(ipaddress);
    			List isScannedList = portScanConfig.getIsScannedList();
    			if(isScannedList!=null){
    				list.addAll(isScannedList);
    			}
    			
    			ipaddresslist.add(ipaddress);
    		}
    		
    	}
    	
    	AbstractionReport1 abstractionReport1 = new ExcelReport1(new IpResourceReport());
    	abstractionReport1.createReport_portscanlist("temp/portscan_report.xls", "IP ��ַ �� " + allIpaddress_str , list);
    	request.setAttribute("filename", abstractionReport1.getFileName());

    	return "/topology/ipregional/download.jsp";
    }
    
    private List getAllDistrictIp(){
    	String netDistrictId = getParaValue("netDistrictId");
    	request.setAttribute("netDistrictId", netDistrictId);
    	
    	String districtId = getParaValue("districtId");
    	String ipDistrictId = getParaValue("ipDistrictId");
    	
    	DistrictConfig districtConfig = null;
    	DistrictDao districtDao = new DistrictDao();
		try {
			// �ҳ���������������
			districtConfig =  (DistrictConfig)districtDao.findByID(districtId);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			districtDao.close();
		}
		
		if(districtConfig == null){
			return null;
		}
		
		request.setAttribute("districtConfig", districtConfig);
    	
		IPDistrictConfig ipDistrictConfig = null;
    	IPDistrictDao ipDistrictDao = new IPDistrictDao();
		try {
			// �ҳ���������������� ����
			ipDistrictConfig = (IPDistrictConfig)ipDistrictDao.findByID(ipDistrictId);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			ipDistrictDao.close();
		}
		
		request.setAttribute("ipDistrictConfig", ipDistrictConfig);
		
		List list = new ArrayList();
		
		if(ipDistrictConfig!=null){
			List ipDistrictMatchConfiglist = null;
			IpDistrictMatchConfigDao ipDistrictMatchConfigDao = new IpDistrictMatchConfigDao();
			try {
				// ������������ �ҳ� ipDistrictMatch
				ipDistrictMatchConfiglist = ipDistrictMatchConfigDao.findByOriDistrictId(String.valueOf(districtId));
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				ipDistrictMatchConfigDao.close();
			}
			
			String startip_str = ipDistrictConfig.getStartip();  // ��ȡ���εĿ�ʼip
			String endip_str = ipDistrictConfig.getEndip();      // ��ȡ���εĽ���ip
			
			String netDistrictName = "";
			
			long startip_long = 0L;
			long endip_long = 0L;
			if(startip_str!=null && endip_str==null){
				startip_long = ip2long(startip_str);
				endip_long = startip_long + 1;
				netDistrictName = startip_str;
			}else if( startip_str != null && endip_str != null){
				startip_long = ip2long(startip_str);
				endip_long = ip2long(endip_str);
				netDistrictName = startip_str + "---" + endip_str;
			}else{
				// ˵�����ݿ��ڵ������д��� Ӧ����������һ����list
			}
			
			long ipTotal= endip_long - startip_long;    // ip���� = ����ip - ��ʼip
			
			if(ipDistrictMatchConfiglist == null){
				ipDistrictMatchConfiglist = new ArrayList();
			}
			
			// ѭ�������ڵ�����ip
			for(int i = 0 ; i < ipTotal ; i ++){
				// ��ip��ַת����long�� + i ����ת����String���͵�ip �����Ϳ��� ѭ��ȡ�������ڵ�ÿ��ip
				long testip_long = startip_long + i;
				String testip_str = iplongToIp(testip_long);
				
				String isUsed = "0";
				String isOnline = "0";
				
				for( int j = 0 ; j < ipDistrictMatchConfiglist.size() ; j++){
					// ѭ��ȡ��ÿ�� ipDistrictMatchConfig  ��� ipDistrictMatchConfig.getNodeIp()�Ͳ��Ե�ip���
					// ��˵����ip�������� ����ʹ����
					// Ȼ��������״̬ ��ֵ�� isOnline
					IpDistrictMatchConfig ipDistrictMatchConfig = (IpDistrictMatchConfig)ipDistrictMatchConfiglist.get(j);
					if(testip_str.equals(ipDistrictMatchConfig.getNodeIp())){
						isUsed = "1";
						isOnline = ipDistrictMatchConfig.getIsOnline();
						break;
					}
					
				}
				
				NetDistrictIpDetail netDistrictIpDetail = new NetDistrictIpDetail();
				netDistrictIpDetail.setId(i);
				netDistrictIpDetail.setIpaddress(testip_str);
				netDistrictIpDetail.setDistrictId(districtId);
				netDistrictIpDetail.setDistrictName(districtConfig.getName());
				netDistrictIpDetail.setIpDistrictId(ipDistrictId);
				//netDistrictIpDetail.setIpTotal(ipTotal);
				netDistrictIpDetail.setNetDistrictName(netDistrictName);
				netDistrictIpDetail.setIsOnline(isOnline);
				netDistrictIpDetail.setIsUsed(isUsed);
				list.add(netDistrictIpDetail);
			}
			
			request.setAttribute("netDistrictName", netDistrictName);
			
		}
		
		//request.setAttribute("list", getListByPage(list));
		request.setAttribute("districtName", districtConfig.getName());
		
		return list;
    }
    
    private List getListByPage(List list){
    	List returnList = new ArrayList();
    	
    	int totalRecord = 0;               // ��ҳ��
    	int perpage = getPerPagenum();     // ÿҳ�����¼��
    	int curpage = getCurrentPage();    // ��ǰҳ��
    	
    	if(list == null || list.size() ==0){
    		totalRecord = 0;
    		JspPage jspPage = new JspPage(perpage,curpage,totalRecord);
    		request.setAttribute("page", jspPage);
    		return returnList;
    	}
    	totalRecord = list.size();
    	JspPage jspPage = new JspPage(perpage,curpage,totalRecord);
    	int loop = 0;
    	Iterator it = list.iterator();
	    while(it.hasNext())
	    {
		    loop++;
		    Object object= it.next();
		    if(loop<jspPage.getMinNum()) continue;
		    returnList.add(object);
		    if(loop==jspPage.getMaxNum()) break;
	    }
	    request.setAttribute("page", jspPage);
    	return returnList;
    }
    
    
    private List refresh(){
    	List ipMacList = new ArrayList();
    	IpMacDao ipMacDao = new IpMacDao();
    	try {
			ipMacList = ipMacDao.loadAll();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ipMacDao.close();
		}
		
		List list = new IPDistrictMatchUtil().pingUtil(ipMacList);
		ComparatorIpDistrictMatchConfig comparatorIpDistrictMatchConfig = new ComparatorIpDistrictMatchConfig();
		Collections.sort(list, comparatorIpDistrictMatchConfig);
		return list;
    }
    
    private DistrictConfig getOriginalDistrict(String mac){
    	DistrictConfig districtConfig = null;
    	MacconfigDao macconfigDao = new MacconfigDao();
		try {
			List MacConfigList = macconfigDao.findByMac(mac);
			if(MacConfigList!=null&&MacConfigList.size()>0){
				Macconfig macConfig = (Macconfig)MacConfigList.get(0);
				String districtConfigId = String.valueOf(macConfig.getDiscrictid());
				DistrictDao districtDao = new DistrictDao();
				try {
					districtConfig = (DistrictConfig)districtDao.findByID(districtConfigId);
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					districtDao.close();
					districtConfig = null;
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			districtConfig = null ;
		}finally{
			macconfigDao.close();
		}
		return districtConfig;
    }
    
    private boolean getIsOnline(String ipaddress){
    	boolean isOnline = false;
    	try {
			if(ipaddress == null){
				System.out.println("IpDistrictMatchManager.getIsOnline()�еĲ���ipaddressΪnull");
				return false;
			}
			PingUtil pingU=new PingUtil(ipaddress);
			Integer[] packet=pingU.ping();
			Vector vector=pingU.addhis(packet); 
			//��Vector�� ������Ԫ�� ���� Pingcollectdata ����
			//��һ��Ԫ��Ϊ��ֵͨ 
			//�ڶ���Ԫ��Ϊ��Ӧʱ��
			if(vector==null || vector.size()==0){
				return false;
			}
			Pingcollectdata pingcollectdata = (Pingcollectdata)vector.get(0);
			String thevalue = pingcollectdata.getThevalue();
			if(Double.valueOf(thevalue) > 50){
				isOnline = true;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			SysLogger.info("IpDistrictMatchManager.getIsOnline()�ж��Ƿ������г�������");
			e.printStackTrace();
		}
    	return isOnline;
    }
    
    private long ip2long(String ip) {
		long result = 0;
		try {
			StringTokenizer st = new StringTokenizer(ip, ".");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				int part = Integer.parseInt(token);
				result = result * 256 + part;
			}
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
    
    private String iplongToIp(long ipaddress) {  
        StringBuffer sb = new StringBuffer("");  
        sb.append(String.valueOf((ipaddress >>> 24)));  
        sb.append(".");  
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));  
        sb.append(".");  
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));  
        sb.append(".");  
        sb.append(String.valueOf((ipaddress & 0x000000FF)));  
        return sb.toString();  
    } 
    
    private DistrictConfig getCurrentDistrict(String ipaddress , List ipDistrictList){
    	DistrictConfig districtConfig = null ;
    	try {
			IPDistrictConfig ipDistrictConfig = getCurrentIPDistrictConfig(ipaddress, ipDistrictList);
			if(ipDistrictConfig == null){
				return districtConfig;
			}
			int districtid = ipDistrictConfig.getDistrictid();
			DistrictDao districtDao = new DistrictDao();
			try {
				districtConfig= (DistrictConfig)districtDao.findByID(String.valueOf(districtid));
			} catch (RuntimeException e) {
				districtConfig = null ;
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				districtDao.close();
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			districtConfig = null ;
		}
    	return districtConfig;
    }
    
    
    private IPDistrictConfig getCurrentIPDistrictConfig(String ipaddress , List ipDistrictList){
    	try {
			long ipaddresslong = ip2long(ipaddress);
			long startiplong = 0L; 
			long endiplong = 0L; 
			for(int i =0 ; i < ipDistrictList.size() ; i ++){
				IPDistrictConfig ipDistrictConfig = (IPDistrictConfig)ipDistrictList.get(i);
				String startip = ipDistrictConfig.getStartip();
				String endip = ipDistrictConfig.getEndip();
				startiplong = ip2long(startip);
				endiplong = ip2long(endip);
				if( startiplong!=0 && endiplong != 0){
					//�����ʼ���κͽ������ζ���Ϊ�� ���жϸõ�ַ�Ƿ�������������
					if(ipaddresslong>startiplong && ipaddresslong<endiplong){
						return ipDistrictConfig;
					}
				}else if( startiplong!=0 && endiplong == 0){
					if( ipaddresslong == startiplong ){
						return ipDistrictConfig;
					}
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return null;
    }
    
    private boolean getIsMatch(DistrictConfig originalDistrict , DistrictConfig currentDistrict){
    	boolean isMatch = false;
    	try {
			if(originalDistrict ==null || currentDistrict == null){
				isMatch = false;
				return isMatch;
			}
			int originalDistrictId = originalDistrict.getId();
			int currentDistrictId = currentDistrict.getId();
			
			if(originalDistrictId == currentDistrictId ){
				isMatch = true;
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isMatch = false;
		}
    	return isMatch;
    }
    
    /**
     * ��ȡ��ѯ����
     * @return
     */
    public String getWhere(){
    	String sql = " where ";
    	
    	String sqlDistrict = getSqlDistrict();
    	
    	sql = sql + sqlDistrict;
    	return sql;
    }
    
    /**
     * ƴ�� ��������ҵ� SQL ���
     * @return
     */
    public String getSqlDistrict(){
    	String sqlDistrict = "";
    	String searchDistrictId = getParaValue("searchDistrictId");
    	if(searchDistrictId == null || "-1".equals(searchDistrictId)){
    		sqlDistrict = "-1=-1";
    		searchDistrictId = "-1";
    	}else{
    		sqlDistrict = "id='" + searchDistrictId + "' ";
    	}
    	request.setAttribute("searchDistrictId", searchDistrictId);
    	return sqlDistrict;
    }
    
    
    
}
