/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.config.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.portscan.dao.PortScanDao;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;

/**
 * 增加一个节点，要在server.jsp增加一个节点;
 * 删除或更新一个节点信息，在这则不直接操作server.jsp,而是让任务updateXml来更新xml.
 */
public class PortconfigManager extends BaseManager implements ManagerInterface {

    private String list() {
        PortconfigDao dao = new PortconfigDao();
        String ipaddress = getParaValue("ipaddress");
        String where = "";
        if (ipaddress != null && !"".equals(ipaddress) && !"-1".equals(ipaddress)) {
        	where = " where ipaddress='"+ipaddress+"'";
        }
        List ips = null;
        try {
            ips = dao.getIps();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("ips", ips);
        dao = new PortconfigDao();
        try {
            setTarget("/config/portconfig/list.jsp");
            list(dao, where);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/portconfig/list.jsp";
    }

    private String empty() {
        PortconfigDao dao = new PortconfigDao();
        try {
            dao.empty();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        dao = new PortconfigDao();
        List ips = null;
        try {
            ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("ips", ips);
        dao = new PortconfigDao();
        try {
            setTarget("/config/portconfig/list.jsp");
            list(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/portconfig/list.jsp";
    }

    private String emptyNode() {
        String ipaddress = getParaValue("ipaddress");
        String id = getParaValue("id");
        PortconfigDao dao = new PortconfigDao();
        try {
            dao.emptyNode(ipaddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return nodeportlist();
    }

    private String monitornodelist() {
        PortconfigDao dao = new PortconfigDao();
        setTarget("/config/portconfig/portconfiglist.jsp");
        return list(dao, " where managed=1");
    }

    private String fromlasttoconfig() {
        List nodeList = null;
        HostNodeDao hostNodeDao = new HostNodeDao();
        try {
            nodeList = hostNodeDao.loadAll();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        for (Object object : nodeList) {
            HostNode hostNode = (HostNode) object;
            fromLastToPortconfig(hostNode.getIpAddress(), hostNode.getId() + "");
        }
        PortconfigDao dao = new PortconfigDao();
        List ips = new ArrayList();
        try {
            ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("ips", ips);
        dao = new PortconfigDao();
        try {
            setTarget("/config/portconfig/list.jsp");
            list(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/portconfig/list.jsp";
    }

    /**
     * 
     * @author gaoguangfei
     * @date 2011-1-26 下午5:03:00
     * @param
     * @return String
     * @Description: TODO(单个设备端口刷新)
     */
    private String fromNodeLasttoconfig() {
        String ipaddress = getParaValue("ipaddress");
        String id = getParaValue("id");

        fromLastToPortconfig(ipaddress, id);// 先添加数据
        PortconfigDao dao = new PortconfigDao();
        request.setAttribute("id", id);
        List list = new ArrayList();
        try {
            list = dao.loadByIpaddress(ipaddress);
        } catch (Exception e) {

        } finally {
            dao.close();
        }
        request.setAttribute("list", list);
        request.setAttribute("ipaddress", ipaddress);
        return "/config/portconfig/nodeportlist.jsp";
    }

    private String showPortStatus() {
        Date d = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String index = getParaValue("index");

        String todate = getParaValue("todate");
        if (todate == null) {
            todate = sdf0.format(d);
        }
        String starttime = todate + " 00:00:00";
        String totime = todate + " 23:59:59";
        String ip = getParaValue("ip");
        String newip = SysUtil.doip(ip);
        PortScanDao dao = new PortScanDao();

        String hourdata = dao.getHourData(newip, index, starttime, totime);

        // Hashtable statusHash=dao.getPortStatusByIp(newip, starttime, totime);
        // // 画图----------------------
        // String timeType = "minute";
        // PollMonitorManager pollMonitorManager = new PollMonitorManager();
        // pollMonitorManager.chooseDrawLineType(timeType,
        // statusHash, "端口状态", newip + "portstatus",
        // 740, 250);
        request.setAttribute("ip", ip);
        request.setAttribute("index", index);
        request.setAttribute("newip", newip);
        // request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("hourData", hourdata);
        return "/config/portconfig/showPortStatus.jsp";
    }

    private String readyEdit() {
        PortconfigDao dao = new PortconfigDao();
        Portconfig vo = new Portconfig();
        String id = getParaValue("id");
        try {
            vo = (Portconfig) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("vo", vo);
        return "/config/portconfig/edit.jsp";
    }

    private String update() {
        Portconfig vo = new Portconfig();
        int sms = getParaIntValue("sms");
        int reportflag = getParaIntValue("reportflag");
        int important = getParaIntValue("important");

        PortconfigDao dao = null;

        dao = new PortconfigDao();
        String id = getParaValue("id");
        try {
            vo = (Portconfig) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        // String linkuse = getParaValue("linkuse");

        // vo.setLinkuse(linkuse);
        if (sms > -1)
            vo.setSms(sms);
        if (reportflag > -1)
            vo.setReportflag(reportflag);
        if (important > -1)
            vo.setImportant(important);
        // String inportalarm = getParaValue("inportalarm");
        // vo.setInportalarm(inportalarm);
        // String outportalarm = getParaValue("outportalarm");
        // vo.setOutportalarm(outportalarm);
        dao = new PortconfigDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        dao = new PortconfigDao();
        List ips = null;
        try {
            ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("ips", ips);
        // dao = new PortconfigDao();
        return "/portconfig.do?action=list";
    }

    private String updatenodeport() {
        Portconfig vo = new Portconfig();
        String id = getParaValue("id");
        int sms = getParaIntValue("sms");
        int reportflag = getParaIntValue("reportflag");
        int important = getParaIntValue("important");
        String sflag = getParaValue("sflag");
        String jp = getParaValue("jp");

        PortconfigDao dao = null;

        dao = new PortconfigDao();
        try {
            vo = (Portconfig) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        if (sms > -1)
            vo.setSms(sms);
        if (reportflag > -1)
            vo.setReportflag(reportflag);
        if (important > -1)
            vo.setImportant(important);
        dao = new PortconfigDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        // dao = new PortconfigDao();
        if (sflag != null && "1".equalsIgnoreCase(sflag)) {
            return "/portconfig.do?action=list&flag=0&jp=" + jp;
        } else
            return "/portconfig.do?action=nodeportlist&ipaddress="
                    + vo.getIpaddress();
    }

    private String updateport() {
        Portconfig vo = new Portconfig();
        String id = getParaValue("id");
        PortconfigDao portconfigDao = new PortconfigDao();
        try {
            vo = (Portconfig) portconfigDao.findByID(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        String linkuse = getParaValue("linkuse");
        if (linkuse != null) {
            vo.setLinkuse(linkuse);
        }
        int sms = getParaIntValue("sms");
        int reportflag = getParaIntValue("reportflag");
        if (sms > -1) {
            vo.setSms(sms);
        }
        if (reportflag > -1) {
            vo.setReportflag(reportflag);
        }
        int important = getParaIntValue("important");
        if (important > -1) {
            vo.setImportant(important);
        }
        String inportalarm = getParaValue("inportalarm");
        if (inportalarm != null) {
            vo.setInportalarm(inportalarm);
        }
        String outportalarm = getParaValue("outportalarm");
        if (outportalarm != null) {
            vo.setOutportalarm(outportalarm);
        }
        PortconfigDao dao = new PortconfigDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        // dao = new PortconfigDao();
        // List ips = dao.getIps();
        //		
        // try{
        // //request.setAttribute("ips", ips);
        // }catch(Exception e){
        // e.printStackTrace();
        // }finally{
        // dao.close();
        // }
        return "/portconfig.do?action=list";
    }

    private String updateselect() {
        String key = getParaValue("key");
        String value = getParaValue("value");
        PortconfigDao dao = new PortconfigDao();
        request.setAttribute("key", key);
        request.setAttribute("value", value);
        String id = getParaValue("id");
        Portconfig vo = new Portconfig();
        try {
            vo = (Portconfig) dao.findByID(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }
        
        String linkuse = getParaValue("linkuse");
        int sms = getParaIntValue("sms");
        int reportflag = getParaIntValue("reportflag");
        vo.setLinkuse(linkuse);
        int important = getParaIntValue("important");
        vo.setSms(sms);
        vo.setImportant(important);
        vo.setReportflag(reportflag);
        String inportalarm = getParaValue("inportalarm");
        vo.setInportalarm(inportalarm);
        String outportalarm = getParaValue("outportalarm");
        vo.setOutportalarm(outportalarm);
        dao = new PortconfigDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dao = new PortconfigDao();
        setTarget("/config/portconfig/list.jsp");
        return list(dao, " where " + key + " = '" + value + "'");
    }

    private String find() {
        String ipaddress = getParaValue("ipaddress");
        PortconfigDao dao = new PortconfigDao();
        request.setAttribute("ipaddress", ipaddress);
        List ips = null;
        try {
            ips = dao.getIps();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("ips", ips);
        dao = new PortconfigDao();
        try {
            setTarget("/config/portconfig/list.jsp");
            list(dao, " where ipaddress = '" + ipaddress + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/config/portconfig/list.jsp";
    }

    private String nodeportlist() {
        String ipaddress = getParaValue("ipaddress");
        String id = getParaValue("id");
        PortconfigDao dao = new PortconfigDao();
        request.setAttribute("id", id);
        List list = new ArrayList();
        try {
            list = dao.loadByIpaddress(ipaddress);
        } catch (Exception e) {

        } finally {
            dao.close();
        }
        request.setAttribute("list", list);
        request.setAttribute("ipaddress", ipaddress);
        return "/config/portconfig/nodeportlist.jsp";
    }

    /**
     * fromLastToPortconfig:
     * <p>从上次采集的数据中刷新至端口配置中
     *
     * @param   ipaddress
     *          - 设备 IP
     * @param   id
     *          - 设备 Id
     *
     * @since   v1.01
     */
    public void fromLastToPortconfig(String ipaddress, String id) {
        List<NodeTemp> interfaceList = null;
        Hashtable<String, Portconfig> portconfigHashtable = new Hashtable<String, Portconfig>();
        InterfaceTempDao interfaceDao = new InterfaceTempDao(ipaddress);
        try {
            interfaceList = interfaceDao
                    .findByCondition(" where subentity = 'ifDescr' or subentity = 'ifSpeed'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            interfaceDao.close();
        }
        Hashtable<String, Hashtable<String, String>> interfaceHash = new Hashtable<String, Hashtable<String, String>>();
        if (interfaceList == null || interfaceList.size() == 0) {
            return;
        }
        for (NodeTemp nodeTemp : interfaceList) {
            Hashtable<String, String> hashtable = interfaceHash.get(nodeTemp.getSindex());
            if (hashtable == null) {
                hashtable = new Hashtable<String, String>();
            }
            if ("ifDescr".equalsIgnoreCase(nodeTemp.getSubentity())) {
                hashtable.put("ifDescr", nodeTemp.getThevalue());
            }
            if ("ifSpeed".equalsIgnoreCase(nodeTemp.getSubentity())) {
                hashtable.put("ifSpeed", nodeTemp.getThevalue());
            }
            interfaceHash.put(nodeTemp.getSindex(), hashtable);
        }
        // 从端口配置表里获取列表
        List<Portconfig> portconfiglist = null;
        PortconfigDao portconfigdao = new PortconfigDao();
        try {
            portconfiglist = portconfigdao.loadByIpaddress(ipaddress);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigdao.close();
        }
        if (portconfiglist != null) {
            for (Portconfig portconfig : portconfiglist) {
                portconfigHashtable.put(String.valueOf(portconfig.getPortindex()), portconfig);
            }
        }
        LinkDao linkDao = new LinkDao();
        List<Link> linkList = null;
        try {
            linkList = linkDao.findByNodeId(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            linkDao.close();
        }
        HostNodeDao hostNodeDao = new HostNodeDao();
        List<HostNode> hostNodeList = null;
        Hashtable<String, HostNode> hostNodeHashtable = new Hashtable<String, HostNode>();
        try {
            hostNodeList = hostNodeDao.loadAll();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        if (hostNodeList != null) {
            for (HostNode hostNode : hostNodeList) {
                hostNodeHashtable.put(String.valueOf(hostNode.getId()), hostNode);
            }
        }
        Hashtable<String, String> linkuseHashtable = new Hashtable<String, String>();
        if (portconfiglist != null) {
            for (Link link : linkList) {
                if (id.equals(String.valueOf(link.getStartId()))) {
                    HostNode endHostNode = hostNodeHashtable.get(String.valueOf(link.getEndId()));
                    if (endHostNode != null) {
                        String linkUse = "至 " + endHostNode.getAlias() + "(" + endHostNode.getIpAddress() + ")";
                        linkuseHashtable.put(link.getStartIndex(), linkUse);
                    }
                } else {
                    HostNode startHostNode = hostNodeHashtable.get(String.valueOf(link.getStartId()));
                    if (startHostNode != null) {
                        String linkUse = "至 " + startHostNode.getAlias() + "(" + startHostNode.getIpAddress() + ")";
                        linkuseHashtable.put(link.getEndIndex(), linkUse);
                    }
                }
                
            }
        }
        Enumeration<String> enumeration = interfaceHash.keys();
        List<Portconfig> list = new ArrayList<Portconfig>();
        while (enumeration.hasMoreElements()) {
            String index = enumeration.nextElement().trim();
            Hashtable<String, String> hashtable = interfaceHash.get(index);
            Portconfig portconfig = portconfigHashtable.get(index);
            
            int important = 0;
            int sms = 0;
            String linkUse = linkuseHashtable.get(index);
            if (linkUse != null && linkUse.length() > 0) {
                important = 1;
            } else {
                linkUse = "";
            }
            String bak = "";
            String name = hashtable.get("ifDescr");
            String speed = hashtable.get("ifSpeed");
            if (portconfig == null) {
                portconfig = new Portconfig();
                portconfig.setBak(bak);
                portconfig.setSms(sms);
                portconfig.setReportflag(new Integer(0));
                portconfig.setInportalarm("2000");// 默认入口流速阀值
                portconfig.setOutportalarm("2000");// 默认出口流速阀值
            }
            String oldlinkUse = portconfig.getLinkuse();
            if (oldlinkUse == null || oldlinkUse.length() == 0) {
                portconfig.setLinkuse(linkUse);
            }
            portconfig.setImportant(important);
            portconfig.setIpaddress(ipaddress);
            portconfig.setName(name);
            portconfig.setPortindex(Integer.valueOf(index));
            portconfig.setSpeed(speed);
            list.add(portconfig);
        }
        portconfigdao = new PortconfigDao();
        try {
            portconfigdao.deleteByIpaddress(ipaddress);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            portconfigdao.close();
        }
        portconfigdao = new PortconfigDao();
        try {
            portconfigdao.save(list);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            portconfigdao.close();
        }
    }

    public String execute(String action) {
        if (action.equals("list"))
            return list();
        if (action.equals("monitornodelist"))
            return monitornodelist();
        if (action.equals("fromlasttoconfig"))
            return fromlasttoconfig();
        if (action.equals("fromnodelasttoconfig"))
            return fromNodeLasttoconfig();
        if (action.equals("showedit"))
            return readyEdit();
        if (action.equals("update"))
            return update();
        if (action.equals("updatenodeport")) {
            return updatenodeport();
        }
        if (action.equals("updateport"))
            return updateport();
        if (action.equals("find"))
            return find();
        if (action.equals("nodeportlist")) {
            return nodeportlist();
        }
        if (action.equals("updateselect"))
            return updateselect();
        if (action.equals("empty"))
            return empty();
        if (action.equals("emptyNode"))
            return emptyNode();
        if (action.equals("ready_add"))
            return "/config/portconfig/add.jsp";
        if (action.equals("delete")) {
            PortconfigDao dao = new PortconfigDao();
            try {
                setTarget("/portconfig.do?action=list");
                delete(dao);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            return getTarget();
        }
        if (action.equals("showPortStatus")) {
            return showPortStatus();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }
}
