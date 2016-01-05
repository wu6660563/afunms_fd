package com.afunms.biosreport.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.afunms.alarm.dao.AlarmPortDao;
import com.afunms.alarm.model.AlarmPort;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.sysset.util.DeviceTypeView;
import com.afunms.system.dao.DepartmentDao;
import com.afunms.system.model.Department;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;

/**
 * @author admin email:
 * @version time: Dec 6, 20119:30:26 AM description:
 */
public class PerformanceReportManager extends BaseManager implements
        ManagerInterface {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    public PerformanceReportManager() {

        // TODO Auto-generated constructor stub
    }

    public String execute(String action) {

        // TODO Auto-generated method stub
        if ("net".equals(action)) {
            return netReport();
        }
        else if ("host".equals(action)) {
            return hostReport();
        }
        else if ("db".equals(action)) {
            return dbReport();
        }
        else if ("middleware".equals(action)) {
            return middlewareReport();
        }else if ("port".equals(action)){
        	return netInterFace();
        }else if("all".equals(action)){
        	// 综合报表首页
        	return allInfo();
        }else if("onlinetime".equals(action)) {
        	return onlinetime();
        }
        return null;
    }

    /**
     * 皕杰报表--综合报表
     * 	包括 网络设备综合报表、服务器、数据库综合报表
     * 
     * @return
     */
    private String allInfo(){
    	
    	User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
    	
    	/**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        /**
         * 判断是超级用户还是正常用户
         */
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        }
        else if (bid == null || bid.trim().length() == 0) {
            result = false;
        }
        else {
            /*
             * 设定正常用户在权限范围内可访问的设备
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        /*
         * 获取设备信息
         */
        List<NodeDTO> nodeList = new ArrayList<NodeDTO>();
        
        if (result) {
            List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
                    Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            nodeList = nodeUtil.conversionToNodeDTO(list);
        }
        
        StringBuffer query = new StringBuffer();
       
        query.append("nodeid in(");
        if(nodeList.size() > 0){
        	for(NodeDTO n : nodeList){
        		query.append(n.getNodeid()+",");
        	}
        	query.replace(query.length()-1, query.length(), " ");
        }
        else{
        	query.append("-1");
        }
        query.append(")");
    
        
        /*
         * 将查询到的设备信息传至jsp页面
         */
        // System.out.println("设备个数是：" + nodeList.size());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nodeList", nodeList);

        
        request.setAttribute("query", query.toString());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
    	return "/bios_report/biosReport.jsp";
    }

    
    /**
     * 网络设备报表
     * 
     * @return
     */
    public String netReport() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        /**
         * 判断是超级用户还是正常用户
         */
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        }
        else if (bid == null || bid.trim().length() == 0) {
            result = false;
        }
        else {
            /*
             * 设定正常用户在权限范围内可访问的设备
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        /*
         * 获取设备信息
         */
        List<NodeDTO> nodeList = null;
        if (result) {
            List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
                    Constant.TYPE_NET, Constant.ALL_SUBTYPE);
            nodeList = nodeUtil.conversionToNodeDTO(list);
        }
        if (nodeList == null) {
            nodeList = new ArrayList<NodeDTO>();
        }
        /*
         * 将查询到的设备信息传至jsp页面
         */
        // System.out.println("设备个数是：" + nodeList.size());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nodeList", nodeList);

        return "/bios_report/netReport.jsp";
    }

    
    /**
     * 端口号的查询
     */
    public String netInterFace(){
    	
    	String startdate = request.getParameter("starttime");
        String todate = request.getParameter("endtime");

        String type = request.getParameter("nodeType");
        String subtype = request.getParameter("nodeSubtype");
        String nodeid = request.getParameter("nodeId");
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        NodeUtil nodeUtil = new NodeUtil();
        List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
        		type, subtype);
        List<NodeDTO> NodeDTOList =  nodeUtil.conversionToNodeDTO(list);
        NodeDTO nodeDTO = null;
        for (NodeDTO nodeDTOPer : NodeDTOList) {
        	if (nodeDTOPer.getNodeid().equals(nodeid)) {
        		nodeDTO = nodeDTOPer;
        		break;
        	}
		}
        List<AlarmPort> alarmPortList = null;
        AlarmPortDao alarmPortDao = new AlarmPortDao();
         try {
			alarmPortList = (List<AlarmPort>) alarmPortDao.loadByIpaddress(nodeDTO.getIpaddress());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarmPortDao.close();
		}
		
		request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nlist", alarmPortList);
        
    	return "/bios_report/port.jsp";
    }
    /**
     * 主机报表
     * 
     * @return
     */
    public String hostReport() {

        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        HostNodeDao dao = new HostNodeDao();
        request.setAttribute("list", dao.loadHostByFlag(1));

        return "/bios_report/hostReport.jsp";
    }

    /**
     * 数据库报表
     * 
     * @return
     */
    public String dbReport() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        /**
         * 判断是超级用户还是正常用户
         */
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        }
        else if (bid == null || bid.trim().length() == 0) {
            result = false;
        }
        else {
            /*
             * 设定正常用户在权限范围内可访问的设备
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        /*
         * 获取设备信息
         */
        List<NodeDTO> nodeList = null;
        if (result) {
            List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
                    Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            nodeList = nodeUtil.conversionToNodeDTO(list);
        }
        if (nodeList == null) {
            nodeList = new ArrayList<NodeDTO>();
        }
        /*
         * 将查询到的设备信息传至jsp页面
         */
        System.out.println("设备个数是：" + nodeList.size());

        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nodeList", nodeList);
        return "/bios_report/dbReport.jsp";
    }

    /**
     * 中间件报表
     * 
     * @return
     */
    public String middlewareReport() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        /**
         * 判断是超级用户还是正常用户
         */
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        }
        else if (bid == null || bid.trim().length() == 0) {
            result = false;
        }
        else {
            /*
             * 设定正常用户在权限范围内可访问的设备
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        /*
         * 获取设备信息
         */
        List<NodeDTO> nodeList = null;
        if (result) {
            List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(
                    Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            nodeList = nodeUtil.conversionToNodeDTO(list);
        }
        if (nodeList == null) {
            nodeList = new ArrayList<NodeDTO>();
        }
        /*
         * 将查询到的设备信息传至jsp页面
         */
        // System.out.println("设备个数是：" + nodeList.size());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nodeList", nodeList);
        return "/bios_report/middlewareReport.jsp";
    }
    
    /**
     * 告警处理情况报表
     * 
     * @return
     */
    public String alarmDealReport(){
    	
    	return "";
    }
    
    /**
     * 	统计用户访问在线时长报表页面
     * 
     * @return
     */
    public String onlinetime(){
    	 User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
         String bid = user.getBusinessids();
         NodeUtil nodeUtil = new NodeUtil();
         
         
         //初始查询日期 
         String startdate = simpleDateFormat.format(new Date());
         String todate = simpleDateFormat.format(new Date());
         
         StringBuffer sb = new StringBuffer();
         String[] bidtmp = bid.split(",");
        
         DepartmentDao departmentDao = new DepartmentDao();
         List<Department> departmentList = new ArrayList<Department>();
         try{
        	 departmentList = departmentDao.loadAll();
         } catch(Exception e){
        	 e.printStackTrace();
         } finally {
        	 departmentDao.close();
         }
         
         String query = getFindInSetSQL(bidtmp);
         request.setAttribute("query", query);	// 用于权限查询
         request.setAttribute("bid", bid);
    	 request.setAttribute("startdate", startdate);
         request.setAttribute("todate", todate);
         request.setAttribute("departmentList", departmentList);	
         
    	return "/bios_report/onlinetime.jsp";
    }
    
    private String getFindInSetSQL(String[] ss){
    	StringBuffer sql = new StringBuffer();
    	for(String s: ss) {
    		if(s.trim().length() > 0) {
       		 sql.append(" find_in_set('" + s +"',u.businessids) or");
       	 }
    	}
    	String tmp = sql.substring(0, sql.length()-2);
    	return tmp;
    }
    
//    public void test() {
//    	List rows = gjyw.getAutopzfs().equals("0") ? 
//    			hslb.equals("")? gjyw.LoadYwPzInfo5():
//    				cwVer.trim().equals("hndl")?
//    						gjyw.LoadYwPzInfo7():
//    							gjyw.LoadYwPzInfo6() 
//    						: gjyw.getAutopzfs().equals("4") 
//    						? gjyw.LoadYwPzInfo4()  
//    								: gjyw.LoadYwPzInfo2();
//    	if(gjyw.getAutopzfs().equals("0")){
//    		if(hslb.equals("")) {
//    			gjyw.LoadYwPzInfo5()
//    		}else if(cwVer.trim().equals("hndl")){
//    			gjyw.LoadYwPzInfo7()
//    		} else {
//    			gjyw.LoadYwPzInfo6()
//    		}
//    	}else {
//    		if(gjyw.getAutopzfs().equals("4") ) {
//    			gjyw.LoadYwPzInfo4()
//    		}else {
//    			gjyw.LoadYwPzInfo2();
//    		}
//    	}
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
