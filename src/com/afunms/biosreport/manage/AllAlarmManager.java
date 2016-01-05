package com.afunms.biosreport.manage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.flex.networkTopology.NetworkMonitor;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.system.model.User;
import com.afunms.topology.model.ManageXml;

public class AllAlarmManager extends BaseManager implements ManagerInterface {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    public AllAlarmManager() {

        // TODO Auto-generated constructor stub
    }

    public String execute(String action) {

        // TODO Auto-generated method stub
        if ("list".equals(action)) {
            return allAlarmList();
        }
        else if ("nodealarm".equals(action)) {
            return nodeAlarmList();
        }
        else if ("busview".equals(action)) {
            return bussinessViewReport();
        } else if ("alarmreport".equals(action)) {
        	return alarmreport();
        }
        return null;
    }

    /**
     * 综合告警报表业务
     * 
     * @return
     */
    public String allAlarmList() {

        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        return "/bios_report/allalarm.jsp";
    }

    /**
     * 判断当前登录用户是超级用户还是正常用户，为用户权限相关的操作提供便利
     * 
     * @return true :
     */
    private boolean isSuperOrNormal() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        return false;
    }

    /**
     * 根据用户的权限获取对应的设备信息 设备告警浏览业务
     * 
     * @return
     */
    public String nodeAlarmList() {

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
        return "/bios_report/nodealarm.jsp";
    }

    /**
     * 业务视图报表业务方法
     * 
     * @return
     */
    public String bussinessViewReport() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        NetworkMonitor networkMonitor = new NetworkMonitor();
        List<ManageXml> list = new ArrayList<ManageXml>();
        Hashtable<ManageXml, List<NodeDTO>> nodeDependListHashtable = null;
        nodeDependListHashtable = networkMonitor.getBussinessviewHash(user);
        if (nodeDependListHashtable == null) {
            nodeDependListHashtable = new Hashtable<ManageXml, List<NodeDTO>>();
        }
        Iterator<ManageXml> iterator = nodeDependListHashtable.keySet()
                .iterator();
        while (iterator.hasNext()) {
            ManageXml manageXml = iterator.next();
            list.add(manageXml);
        }

        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request
                .setAttribute("nodeDependListHashtable",
                        nodeDependListHashtable);
        request.setAttribute("list", list);
        request.setAttribute("bid", user.getBusinessids());

        return "/bios_report/bussinessView.jsp";
    }
    
    /**
     * 系统告警处理状态以及告警报告相关报表处理
     * 
     * @return
     */
    public String alarmDealAndReport(){
    	
    	String startDate = "";
    	String endDate = "";    	
    	
    	request.getSession(true).removeAttribute("list");
		
		try {
			request.getRequestDispatcher("/ReportEmitter?rpt=contractState.brt").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return "";
    }
    
    public String alarmreport() {

        /**
         * 初始查询日期
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());
        
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // 默认查询所有的类型和所有的子类型的设备
        if (type == null) {
            type = Constant.ALL_TYPE;
        }

        if (subtype == null) {
            subtype = Constant.ALL_SUBTYPE;
        }

        if (nodeid == null) {
            nodeid = "-1";
        }

        // 先查询出该用户所能查看的所有类型和子类型的设备
        List<NodeDTO> allNodeDTOlist = null;
        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        NodeUtil nodeUtil = new NodeUtil();
        String bid = user.getBusinessids();
        boolean isCheck = false;
        if (user.getRole() == 0) {
            isCheck = true;
        } else if (bid == null || bid.trim().length() == 0) {
            isCheck = false;
        } else {
            String[] bids = bid.trim().split(",");
            if (bids == null || bids.length == 0) {
                isCheck = false;
            } else {
                nodeUtil.setBid(bid);
                isCheck = true;
            }
        }
        if (isCheck) {
            List<BaseVo> nodelist = nodeUtil.getNodeByTyeAndSubtype(
                            Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            allNodeDTOlist = nodeUtil.conversionToNodeDTO(nodelist);
        }
        if (allNodeDTOlist == null) {
            allNodeDTOlist = new ArrayList<NodeDTO>();
        }
        Hashtable<String, NodeDTO> allNodeDTOHashtable = new Hashtable<String, NodeDTO>();
        for (NodeDTO nodeDTO : allNodeDTOlist) {
            allNodeDTOHashtable.put(String.valueOf(nodeDTO.getId()) + ":"
                            + nodeDTO.getType() + ":" + nodeDTO.getSubtype(),
                            nodeDTO);
        }

        // 查询出该用户所能查看的所有类型和子类型的设备当前告警
        // 为了分页
        CheckEventDao checkEventDao = new CheckEventDao();
        try {
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" where 1=1 ");

            if (!"-1".equalsIgnoreCase(type)) {
                sqlBuffer.append(" and type='" + type + "'");
            }
            if (!"-1".equalsIgnoreCase(subtype)) {
                sqlBuffer.append(" and subtype='" + subtype + "'");
            }
            if (!"-1".equalsIgnoreCase(nodeid)) {
                sqlBuffer.append(" and node_id='" + nodeid + "'");
            }
            sqlBuffer.append(" and node_id in("); // 从所有数据中查找
            if (allNodeDTOlist != null && allNodeDTOlist.size() > 0) {
                for (NodeDTO nodeDTO : allNodeDTOlist) {
                    sqlBuffer.append(nodeDTO.getNodeid() + ",");
                }
            }
            sqlBuffer.append("-1);");
            list(checkEventDao, sqlBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkEventDao.close();
        }
        // 查询的当前告警列表
        List list = (List) request.getAttribute("list");

        request.setAttribute("list", list);
        request.setAttribute("allNodeDTOlist", allNodeDTOlist);
        request.setAttribute("allNodeDTOHashtable", allNodeDTOHashtable);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);

        

        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        //request.setAttribute("nodeDependListHashtable",
         //               nodeDependListHashtable);
        //request.setAttribute("list", list);
        //request.setAttribute("bid", user.getBusinessids());

        return "/bios_report/alarmreport.jsp";
    }
}
