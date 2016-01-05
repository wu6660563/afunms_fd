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
     * �ۺϸ澯����ҵ��
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
     * �жϵ�ǰ��¼�û��ǳ����û����������û���Ϊ�û�Ȩ����صĲ����ṩ����
     * 
     * @return true :
     */
    private boolean isSuperOrNormal() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        return false;
    }

    /**
     * �����û���Ȩ�޻�ȡ��Ӧ���豸��Ϣ �豸�澯���ҵ��
     * 
     * @return
     */
    public String nodeAlarmList() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
        /**
         * ��ʼ��ѯ����
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());

        /**
         * �ж��ǳ����û����������û�
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
             * �趨�����û���Ȩ�޷�Χ�ڿɷ��ʵ��豸
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        /*
         * ��ȡ�豸��Ϣ
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
         * ����ѯ�����豸��Ϣ����jspҳ��
         */
        // System.out.println("�豸�����ǣ�" + nodeList.size());
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("nodeList", nodeList);
        return "/bios_report/nodealarm.jsp";
    }

    /**
     * ҵ����ͼ����ҵ�񷽷�
     * 
     * @return
     */
    public String bussinessViewReport() {

        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);

        /**
         * ��ʼ��ѯ����
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
     * ϵͳ�澯����״̬�Լ��澯������ر�����
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
         * ��ʼ��ѯ����
         */
        String startdate = simpleDateFormat.format(new Date());
        String todate = simpleDateFormat.format(new Date());
        
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // Ĭ�ϲ�ѯ���е����ͺ����е������͵��豸
        if (type == null) {
            type = Constant.ALL_TYPE;
        }

        if (subtype == null) {
            subtype = Constant.ALL_SUBTYPE;
        }

        if (nodeid == null) {
            nodeid = "-1";
        }

        // �Ȳ�ѯ�����û����ܲ鿴���������ͺ������͵��豸
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

        // ��ѯ�����û����ܲ鿴���������ͺ������͵��豸��ǰ�澯
        // Ϊ�˷�ҳ
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
            sqlBuffer.append(" and node_id in("); // �����������в���
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
        // ��ѯ�ĵ�ǰ�澯�б�
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
