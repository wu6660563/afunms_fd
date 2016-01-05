package com.afunms.topology.manage;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.util.ProcessGroupConfigurationUtil;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.IpAliasDao;
import com.afunms.config.dao.IpaddressPanelDao;
import com.afunms.config.dao.NetNodeCfgFileDao;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.IpaddressPanel;
import com.afunms.config.model.Portconfig;
import com.afunms.discovery.DiscoverDataHelper;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.LinkRoad;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Bussiness;
import com.afunms.polling.node.Host;
import com.afunms.polling.task.UpdateXmlTaskTest;
import com.afunms.portscan.dao.PortScanDao;
import com.afunms.system.model.User;
import com.afunms.temp.model.Objbean;
import com.afunms.topology.dao.CommonDao;
import com.afunms.topology.dao.ConnectTypeConfigDao;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.EquipImageDao;
import com.afunms.topology.dao.HintItemDao;
import com.afunms.topology.dao.HintNodeDao;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.IpMacBaseDao;
import com.afunms.topology.dao.IpMacChangeDao;
import com.afunms.topology.dao.IpMacDao;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.dao.NodeEquipDao;
import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.dao.RelationDao;
import com.afunms.topology.dao.RepairLinkDao;
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.dao.XmlNodeStandardDao;
import com.afunms.topology.model.EquipImage;
import com.afunms.topology.model.HintNode;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.model.NodeEquip;
import com.afunms.topology.model.Relation;
import com.afunms.topology.model.TreeNode;
import com.afunms.topology.model.XmlNodeStandardVo;
import com.afunms.topology.service.TopoNodeInfoService;
import com.afunms.topology.service.TopoService;
import com.afunms.topology.util.EquipService;
import com.afunms.topology.util.ManageXmlOperator;
import com.afunms.topology.util.NodeHelper;
import com.afunms.topology.util.TopoUI;
import com.afunms.topology.util.XmlOperator;

public class SubMapManager extends BaseManager implements ManagerInterface {

    // 创建子图，保存已选图元信息
    private synchronized String saveSubMap() {

        User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        ManageXml vo = new ManageXml();
        String xmlName = SysUtil.getCurrentLongTime() + ".jsp";
        if (Integer.parseInt(getParaValue("topo_type")) == 4) {
            xmlName = "submap" + xmlName;
        } else if (Integer.parseInt(getParaValue("topo_type")) == 3) {
            xmlName = "breviarymap" + xmlName;
        } else if (Integer.parseInt(getParaValue("topo_type")) == 2) {
            xmlName = "hintmap" + xmlName;
        } else if (Integer.parseInt(getParaValue("topo_type")) == 1) {
            xmlName = "businessmap" + xmlName;
        } else {
            xmlName = "topmap" + xmlName;
        }
        ManageXmlDao mxDao = new ManageXmlDao();
        if (Integer.parseInt(getParaValue("topo_type")) == 1) {
            mxDao.updateBusView(uservo.getBusinessids());
        } else {
            mxDao.updateView(uservo.getBusinessids());
        }
        mxDao.close();
        String bid = ",";
        String arr[] = getParaArrayValue("checkbox");
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                bid = bid + arr[i] + ",";
            }
        }
        vo.setXmlName(xmlName);
        vo.setTopoName(getParaValue("topo_name"));
        vo.setAliasName(getParaValue("alias_name"));
        vo.setTopoTitle(getParaValue("topo_title"));
        vo.setTopoArea(getParaValue("topo_area"));
        vo.setTopoBg(getParaValue("topo_bg"));
        vo.setTopoType(Integer.parseInt(getParaValue("topo_type")));
        if (getParaValue("home_view") != null
                && Integer.parseInt(getParaValue("home_view")) == 1) {
            if (Integer.parseInt(getParaValue("topo_type")) == 1) {
                vo.setBus_home_view(1);
                vo.setHome_view(0);
            } else {
                vo.setHome_view(1);
                vo.setBus_home_view(0);
            }
            vo.setPercent(Float.parseFloat(getParaValue("zoom_percent")));
        } else {
            vo.setBus_home_view(0);
            vo.setHome_view(0);
            vo.setPercent(1);
        }
        vo.setBid(bid);
        DaoInterface dao = new ManageXmlDao();
        save(dao, vo);

        // yangjun
        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        ManageXmlDao mXmlDao = new ManageXmlDao();
        List xmlList = new ArrayList();
        try {
            xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mXmlDao.close();
        }
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("tree");
            chartxml.addViewTree(xmlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ServletContext context = FlexContext.getServletContext();
        // String path = context.getRealPath("/flex/data/init.properties");
        // Properties props = new Properties();
        // String[] str = {"map_a","map_b","map_c","map_d"};
        // ManageXmlDao mXmlDao = new ManageXmlDao();
        // List mlist = mXmlDao.findByTopoType(1);
        // try {
        // if(mlist!=null&&mlist.size()>0){
        // for(int i=0;i<mlist.size();i++){
        // ManageXml manageXml = (ManageXml)mlist.get(i);
        // InputStream fis = new FileInputStream(path);
        // // 从输入流中读取属性列表（键和元素对）
        // props.load(fis);
        // OutputStream fos = new FileOutputStream(path);
        // props.setProperty(str[i], manageXml.getXmlName().replace("jsp",
        // "xml"));
        // // 将此 Properties表中的属性列表（键和元素对）写入输出流
        // props.store(fos, "Update '" + str[i] + "'value");
        // }
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        // 将业务节点加到内存中
        if (Integer.parseInt(getParaValue("topo_type")) == 1) {
            ManageXmlDao manageXmlDao = new ManageXmlDao();
            ManageXml manageXml = (ManageXml) manageXmlDao.findByXml(xmlName);
            Bussiness bus = new Bussiness();
            bus.setId(manageXml.getId());
            bus.setBid(manageXml.getBid());
            bus.setName(manageXml.getTopoName());
            bus.setAlias(manageXml.getTopoName());
            bus.setCategory(80);
            bus.setStatus(0);
            bus.setType("业务");
            PollingEngine.getInstance().addBus(bus);
        }
        String[] values = getParaValue("objEntityStr").split(",");
        String[] lineValues = getParaValue("linkStr").split(";");
        String[] asslineValues = getParaValue("asslinkStr").split(";");

        XmlOperator xmlOpr = new XmlOperator();
        xmlOpr.setFile(vo.getXmlName());
        xmlOpr.init4createXml();
        xmlOpr.createXml();
        xmlOpr.writeXml();

        if (values.length > 0 && !"".equals(values[0])) {

            ManageXmlOperator mxmlOpr = new ManageXmlOperator();
            mxmlOpr.setFile(xmlName);
            // 保存节点信息
            mxmlOpr.init4editNodes();
            int index = 0;
            for (int i = 0; i < values.length; i++) {
                if (!mxmlOpr.isNodeExist(values[i])) // no exist
                    mxmlOpr.addNode(values[i], ++index);
            }
            mxmlOpr.deleteNodes();
            mxmlOpr.writeXml();
            // 保存链路信息
            LinkDao linkDao = new LinkDao();
            mxmlOpr.init4editLines();
            if (lineValues.length > 0 && !"".equals(lineValues[0])) {

                for (int i = 0; i < lineValues.length; i++) {
                    String line[] = lineValues[i].split(",");

                    int loc = line[0].indexOf("_");
                    int tag = 0;
                    String id1 = line[0].substring(0, loc);
                    String id2 = line[0].substring(loc + 1);
                    String tempValues = id2 + "_" + id1 + "," + line[1];
                    for (int j = i + 1; j < lineValues.length; j++) {
                        if (lineValues[j].equals(tempValues)
                                || lineValues[j].equals(lineValues[i])) {
                            tag++;
                        }
                    }
                    if (tag == 0) {
                        Link link = (Link) linkDao.findByID(line[1]);
                        String startid = "";
                        String endid = "";
                        if (id1.indexOf(String.valueOf(link.getStartId())) != -1) {
                            startid = id1;
                            endid = id2;
                        } else {
                            startid = id2;
                            endid = id1;
                        }
                        mxmlOpr.addLine(link.getLinkName(), String
                                .valueOf(line[1]), startid, endid);
                    }
                }

            }
            if (asslineValues.length > 0 && !"".equals(asslineValues[0])) {

                for (int i = 0; i < asslineValues.length; i++) {
                    String line[] = asslineValues[i].split(",");

                    int loc = line[0].indexOf("_");
                    int tag = 0;
                    String id1 = line[0].substring(0, loc);
                    String id2 = line[0].substring(loc + 1);
                    String tempValues = id2 + "_" + id1 + "," + line[1];
                    for (int j = i + 1; j < asslineValues.length; j++) {
                        if (asslineValues[j].equals(tempValues)
                                || asslineValues[j].equals(asslineValues[i])) {
                            tag++;
                        }
                    }
                    if (tag == 0) {
                        Link link = (Link) linkDao.findByID(line[1]);
                        String startid = "";
                        String endid = "";
                        if (id1.indexOf(String.valueOf(link.getStartId())) != -1) {
                            startid = id1;
                            endid = id2;
                        } else {
                            startid = id2;
                            endid = id1;
                        }
                        mxmlOpr.addAssistantLine(link.getLinkName(), String
                                .valueOf(line[1]), startid, endid);
                    }
                }

            }
            mxmlOpr.writeXml();
            linkDao.close();
        }
        return null;
    }

    public Objbean[] refreshImage(Objbean obj[]) {
        // System.out.println("000000000000000000000000");
        // System.out.println(obj.getClass());

        if (obj != null && obj.length > 0) {
            NodeAlarmService nodeAlarmService = new NodeAlarmService();
            NodeUtil nodeUtil = new NodeUtil();
            for (int i = 0; i < obj.length; i++) {
                Objbean objbean = obj[i];
                NodeDTO node = null;
                if (objbean.getId().indexOf("hin") >= 0) {
                    // 示意设备 暂不考虑.
                    if (objbean.getRelationMap() != null) {
                        ManageXml xml = null;
                        ManageXmlDao dao = new ManageXmlDao();
                        try {
                            xml = (ManageXml) dao.findByXml(objbean.getRelationMap());
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        } finally {
                            dao.close();
                        }
                        if (xml != null) {
                            node = nodeUtil.conversionToNodeDTO(xml);
                            TopoService service = new TopoService();
                            boolean isAlarm = service.isAlarm(xml);
                            System.out.println(isAlarm);
                            if (isAlarm) {
                                // 如果有告警 则将如果是未告警的图片换成告警的
                                String image = objbean.getImage();
                                if (image.substring(objbean.getImage().lastIndexOf(".")).indexOf("alarm") < 0) {
                                    image = objbean.getImage().substring(0, objbean.getImage().lastIndexOf("."));
                                    image += "-alarm.gif";
                                }
                                objbean.setImage(image);
                            } else {
                             // 如果有告警 则将如果是告警的图片换成未告警的
                                String image = objbean.getImage();
                                if (image.substring(objbean.getImage().lastIndexOf(".")).indexOf("alarm") >= 0) {
                                    image = objbean.getImage().replace("-alarm", "");
                                }
                                objbean.setImage(image);
                            }
                        }
                    }
                    objbean.setImage(objbean.getImage().substring(objbean.getImage().indexOf("resource/") + 9));
                    continue;
                }
                String Id = objbean.getId().replaceAll("node_", "");
                String nodeTag = Id.substring(0, 3);
                String nodeid = Id.substring(3);
                
                List<BaseVo> list = nodeUtil.getByNodeTag(nodeTag, null);
                
                for (BaseVo baseVo : list) {
                    NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(baseVo);
                    if (nodeid.equals(nodeDTO.getNodeid())) {
                        node = nodeDTO;
                        break;
                    }
                }

                if (node == null) {
                    continue;
                }
                int alarmLevel = nodeAlarmService.getMaxAlarmLevel(node);
                // com.afunms.polling.base.Node node = null;
                // if(objbean.getId().indexOf("dbs") != -1){
                // node = PollingEngine.getInstance().getNodeByCategory("dbs",
                // id);
                // } else {
                // node =
                // PollingEngine.getInstance().getNodeByCategory(objbean.getCategory(),
                // id);
                // }
                NodeEquip vo = null;
                NodeEquipDao nodeEquipDao = new NodeEquipDao();// yangjun add
                try {
                    vo = (NodeEquip) nodeEquipDao.findByNodeAndXml(Id, objbean
                            .getXmlname());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    nodeEquipDao.close();
                }
                if (Constant.TYPE_HOST.equals(node.getType())) {
                    if (vo != null) {
                        if (alarmLevel > 0) {// 报警
                            EquipService equipservice = new EquipService();
                            objbean.setImage("image/topo/"
                                    + equipservice.getAlarmImage(vo
                                            .getEquipId()));
                        } else {
                            EquipService equipservice = new EquipService();
                            objbean.setImage("image/topo/"
                                    + equipservice
                                            .getTopoImage(vo.getEquipId()));
                        }
                    } else {
                        if (alarmLevel > 0) { // 报警
                            objbean.setImage(NodeHelper
                                    .getServerAlarmImage(node.getSysOid()));
                        } else {
                            objbean.setImage(NodeHelper.getServerTopoImage(node
                                    .getSysOid()));
                        }
                    }

                } else {
                    // if (node.getDiscoverstatus() > 0) {
                    // // 没有被发现的设备
                    // objbean.setImage(NodeHelper.getLostImage(objbean.getCategory()));
                    // } else {
                    if (vo != null) {
                        if (alarmLevel > 0) { // 报警
                            EquipService equipservice = new EquipService();
                            objbean.setImage("image/topo/"
                                    + equipservice.getAlarmImage(vo
                                            .getEquipId()));
                        } else {
                            EquipService equipservice = new EquipService();
                            objbean.setImage("image/topo/"
                                    + equipservice
                                            .getTopoImage(vo.getEquipId()));
                        }
                    } else {
                        if (alarmLevel > 0) { // 报警
                            objbean.setImage(NodeHelper.getAlarmImage(objbean
                                    .getCategory()));
                        } else {
                            objbean.setImage(NodeHelper.getTopoImage(objbean
                                    .getCategory()));
                        }
                        // }
                    }
                }
            }
        }
        return obj;
    }

    public String getShowMessage(String Id, String category, String xmlname) {
        //
        // ManageXmlDao manageXmlDao = new ManageXmlDao();
        // ManageXml mvo = null;
        // try{
        // mvo = (ManageXml) manageXmlDao.findByXml(xmlname);
        // }catch(Exception e){
        // e.printStackTrace();
        // }finally{
        // manageXmlDao.close();
        // }
        // AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        // IndicatorsTopoRelationDao indicatorsTopoRelationDao = new
        // IndicatorsTopoRelationDao();
        // String nodeid = Id.substring(3);
        // List<IndicatorsTopoRelation> list1 =
        // indicatorsTopoRelationDao.findByTopoId(mvo.getId()+"",nodeid);
        // // System.out.println(((ManageXml)
        // subfileList.get(j)).getXmlName()+"====@@@@@@@@@==="+list1.size());
        // indicatorsTopoRelationDao.close();
        // List moidList = new ArrayList();
        // for(int k=0;k<list1.size();k++){
        // IndicatorsTopoRelation nm = (IndicatorsTopoRelation)list1.get(k);
        // if(nm!=null){
        // AlarmIndicatorsNode alarmIndicatorsNode =
        // alarmIndicatorsUtil.getAlarmInicatorsNodes(nm.getIndicatorsId(),nodeid);
        // moidList.add(alarmIndicatorsNode);
        // }
        // }
        // Host host = (Host)
        // PollingEngine.getInstance().getNodeByID(Integer.valueOf(nodeid));
        // host.setMoidList(moidList);
        // host.getAlarmMessage().clear();
        // host.doPoll();
        //
        // int id = Integer.valueOf(Id.substring(3)).intValue();
        // com.afunms.polling.base.Node node = null;
        // System.out.println(Id + "=================" + category + "===" +
        // xmlname);
        // if(Id.indexOf("dbs") != -1){
        // node = PollingEngine.getInstance().getNodeByCategory("dbs", id);
        // } else {
        // node = PollingEngine.getInstance().getNodeByCategory(category, id);
        // }
        if (Id.indexOf("hin") >= 0) {
            // 示意设备 暂不考虑
            return "示意设备";
        }
        String nodeTag = Id.substring(0, 3);
        String nodeid = Id.substring(3);
        
        NodeUtil nodeUtil = new NodeUtil();
        List<BaseVo> list = nodeUtil.getByNodeTag(nodeTag, null);
        NodeDTO node = null;
        for (BaseVo baseVo : list) {
            NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(baseVo);
            if (nodeid.equals(nodeDTO.getNodeid())) {
                node = nodeDTO;
                break;
            }
        }
        if (node == null) {
            SysLogger.info("发现一个被删除的节点，ID=" + Id);
            return "该节点已被删除";
        }
        return TopoNodeInfoService.getInstance(node).getNodeInfo();

    }

    // 从设备树向根图添加实体设备
    public String addEquipToMap(String xmlName, String node, String category,
            HttpSession httpSession) {

        ManageXmlOperator mxmlOpr = new ManageXmlOperator();
        mxmlOpr.setFile(xmlName);
        // 保存节点信息
        mxmlOpr.init4editNodes();
        // ###########先判断该节点是否已添加到拓扑图 HONGLI ADD######
        boolean exist = mxmlOpr.isIdExist(node);
        if (exist) {
            SysLogger.info("#######该设备" + node + "已被添加到拓扑图中，操作失败######");
            return "success";
        }
        // #############end############
        mxmlOpr.addNode(node, 1, category);
        mxmlOpr.writeXml();
        if (xmlName.indexOf("businessmap") != -1) {// 如果是业务视图
            NodeDependDao nodeDependDao = new NodeDependDao();
            try {
                if (!nodeDependDao.isNodeExist(node, xmlName)) {// 判断节点是否存在于nms_node_depend表中，不存在则添加
                    Node fnode = PollingEngine.getInstance().getNodeByCategory(
                            category, Integer.parseInt(node.substring(3)));
                    // System.out.println("232111111111111111111111111");
                    NodeDepend nodeDepend = new NodeDepend();
                    nodeDepend.setAlias(fnode.getAlias());
                    nodeDepend.setLocation("10px,10px");
                    nodeDepend.setNodeId(node);
                    nodeDepend.setXmlfile(xmlName);
                    NodeDependDao nodeDependDaos = new NodeDependDao();
                    nodeDependDaos.save(nodeDepend);
                }
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            } finally {
                nodeDependDao.close();
            }

            // yangjun
            session = WebContextFactory.get().getSession();
            User user = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER);
            ManageXmlDao mXmlDao = new ManageXmlDao();
            List xmlList = new ArrayList();
            try {
                xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mXmlDao.close();
            }
            try {
                ChartXml chartxml;
                chartxml = new ChartXml("tree");
                chartxml.addViewTree(xmlList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ManageXmlDao subMapDao = new ManageXmlDao();
            ManageXml manageXml = (ManageXml) subMapDao.findByXml(xmlName);
            if (manageXml != null) {
                NodeDependDao nodeDepenDao = new NodeDependDao();
                try {
                    List list = nodeDepenDao.findByXml(xmlName);
                    ChartXml chartxml;
                    chartxml = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml"));
                    chartxml.addBussinessXML(manageXml.getTopoName(), list);
                    ChartXml chartxmlList;
                    chartxmlList = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml").replace(
                                    "businessmap", "list"));
                    chartxmlList.addListXML(manageXml.getTopoName(), list);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    nodeDepenDao.close();
                }
            }
        }
        return "success";
    }

    // 从设备树向根图添加实体设备
    // public String addEquipToMap(String xmlName,String node,String category) {
    //
    // // String xmlName = getParaValue("xml");
    // // String node = getParaValue("node");
    // // String category = getParaValue("category");
    // ManageXmlOperator mxmlOpr = new ManageXmlOperator();
    // mxmlOpr.setFile(xmlName);
    // // 保存节点信息
    // mxmlOpr.init4editNodes();
    // mxmlOpr.addNode(node, 1, category);
    // mxmlOpr.writeXml();
    // //request.setAttribute("fresh", "fresh");
    // //return "/topology/network/save.jsp";
    // return "success";
    //
    // }
    // 从拓扑图移除实体设备
    private String removeEquipFromSubMap() {
        String xmlName = getParaValue("xml");
        String node = getParaValue("node");
        if (xmlName.indexOf("businessmap") != -1) {
            LineDao lineDao = new LineDao();
            lineDao.deleteByidXml(node, xmlName);
            NodeDependDao nodeDependDao = new NodeDependDao();
            if (nodeDependDao.isNodeExist(node, xmlName)) {
                nodeDependDao.deleteByIdXml(node, xmlName);
            } else {
                nodeDependDao.close();
            }

            // yangjun
            User user = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER);
            ManageXmlDao mXmlDao = new ManageXmlDao();
            List xmlList = new ArrayList();
            try {
                xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mXmlDao.close();
            }
            try {
                ChartXml chartxml;
                chartxml = new ChartXml("tree");
                chartxml.addViewTree(xmlList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ManageXmlDao subMapDao = new ManageXmlDao();
            ManageXml manageXml = (ManageXml) subMapDao.findByXml(xmlName);
            if (manageXml != null) {
                NodeDependDao nodeDepenDao = new NodeDependDao();
                try {
                    List list = nodeDepenDao.findByXml(xmlName);
                    ChartXml chartxml;
                    chartxml = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml"));
                    chartxml.addBussinessXML(manageXml.getTopoName(), list);
                    ChartXml chartxmlList;
                    chartxmlList = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml").replace(
                                    "businessmap", "list"));
                    chartxmlList.addListXML(manageXml.getTopoName(), list);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    nodeDepenDao.close();
                }
            }
        }
        ManageXmlOperator mxmlOpr = new ManageXmlOperator();
        mxmlOpr.setFile(xmlName);
        mxmlOpr.init4editNodes();
        if (mxmlOpr.isIdExist(node)) // no exist
            mxmlOpr.deleteNodeByID(node);
        mxmlOpr.writeXml();
        return "/" + xmlName;
    }

    // 删除实体设备
    private String deleteEquipFromSubMap() {

        String nodeid = getParaValue("node");
        String category = getParaValue("category");
        String id = nodeid.substring(3);
        String xmlName = getParaValue("xml");
        if (nodeid.indexOf("dbs") != -1) {
            category = "dbs";
        }
        PollingEngine.getInstance().deleteNodeByCategory(category,
                Integer.parseInt(id));// 按照节点类型和id更新内存

        // 更新数据库
        TreeNodeDao treeNodeDao = new TreeNodeDao();
        TreeNode tvo = (TreeNode) treeNodeDao.findByName(category);
        if (tvo != null && tvo.getTableName() != null
                && !"".equals(tvo.getTableName())) {// 根据表名
            if ("net".equals(nodeid.subSequence(0, 3))) {
                CommonDao dao = new CommonDao(tvo.getTableName());
                HostNode host = (HostNode) dao.findByID(id);
                dao.close();
                String ip = host.getIpAddress();
                // String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
                // String tempStr = "";
                // String allipstr = "";
                // if (ip.indexOf(".") > 0) {
                // ip1 = ip.substring(0, ip.indexOf("."));
                // ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
                // tempStr = ip.substring(ip.indexOf(".") + 1,
                // ip.lastIndexOf("."));
                // }
                // ip2 = tempStr.substring(0, tempStr.indexOf("."));
                // ip3 = tempStr.substring(tempStr.indexOf(".") + 1,
                // tempStr.length());
                // allipstr = ip1 + ip2 + ip3 + ip4;
                String allipstr = SysUtil.doip(ip);

                CreateTableManager ctable = new CreateTableManager();
                DBManager conn = new DBManager();
                try {
                    if (host.getCategory() < 4 || host.getCategory() == 6
                            || host.getCategory() == 7
                            || host.getCategory() == 8) {
                        // 先删除网络设备表
                        // 连通率
                        try {
                            ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
                            // //ctable.dropSeq(conn, "ping", allipstr);//删除序列
                            ctable.deleteTable(conn, "pinghour", allipstr,
                                    "pinghour");// Ping
                            // //ctable.dropSeq(conn, "pinghour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "pingday", allipstr,
                                    "pingday");// Ping
                            // //ctable.dropSeq(conn, "pingday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 内存
                        try {
                            ctable.deleteTable(conn, "memory", allipstr, "mem");// 内存
                            // //ctable.dropSeq(conn, "memory", allipstr);//删除序列
                            ctable.deleteTable(conn, "memoryhour", allipstr,
                                    "memhour");// 内存
                            // //ctable.dropSeq(conn, "memoryhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "memoryday", allipstr,
                                    "memday");// 内存
                            // //ctable.dropSeq(conn, "memoryday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        ctable.deleteTable(conn, "flash", allipstr, "flash");// 闪存
                        // ctable.dropSeq(conn, "flash", allipstr);//删除序列
                        ctable.deleteTable(conn, "flashhour", allipstr,
                                "flashhour");// 闪存
                        // ctable.dropSeq(conn, "flashhour", allipstr);//删除序列
                        ctable.deleteTable(conn, "flashday", allipstr,
                                "flashday");// 闪存
                        // ctable.dropSeq(conn, "flashday", allipstr);//删除序列

                        ctable.deleteTable(conn, "buffer", allipstr, "buffer");// 缓存
                        // ctable.dropSeq(conn, "buffer", allipstr);//删除序列
                        ctable.deleteTable(conn, "bufferhour", allipstr,
                                "bufferhour");// 缓存
                        // ctable.dropSeq(conn, "bufferhour", allipstr);//删除序列
                        ctable.deleteTable(conn, "bufferday", allipstr,
                                "bufferday");// 缓存
                        // ctable.dropSeq(conn, "bufferday", allipstr);//删除序列

                        ctable.deleteTable(conn, "fan", allipstr, "fan");// 风扇
                        // ctable.dropSeq(conn, "fan", allipstr);//删除序列
                        ctable
                                .deleteTable(conn, "fanhour", allipstr,
                                        "fanhour");// 风扇
                        // ctable.dropSeq(conn, "fanhour", allipstr);//删除序列
                        ctable.deleteTable(conn, "fanday", allipstr, "fanday");// 风扇
                        // ctable.dropSeq(conn, "fanday", allipstr);//删除序列

                        ctable.deleteTable(conn, "power", allipstr, "power");// 电源
                        // ctable.dropSeq(conn, "power", allipstr);//删除序列
                        ctable.deleteTable(conn, "powerhour", allipstr,
                                "powerhour");// 电源
                        // ctable.dropSeq(conn, "powerhour", allipstr);//删除序列
                        ctable.deleteTable(conn, "powerday", allipstr,
                                "powerday");// 电源
                        // ctable.dropSeq(conn, "powerday", allipstr);//删除序列

                        ctable.deleteTable(conn, "vol", allipstr, "vol");// 电压
                        // ctable.dropSeq(conn, "vol", allipstr);//删除序列
                        ctable
                                .deleteTable(conn, "volhour", allipstr,
                                        "volhour");// 电压
                        // ctable.dropSeq(conn, "volhour", allipstr);//删除序列
                        ctable.deleteTable(conn, "volday", allipstr, "volday");// 电压
                        // ctable.dropSeq(conn, "volday", allipstr);//删除序列

                        // CPU
                        try {
                            ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                            // ctable.dropSeq(conn, "cpu", allipstr);//删除序列
                            ctable.deleteTable(conn, "cpuhour", allipstr,
                                    "cpuhour");// CPU
                            // ctable.dropSeq(conn, "cpuhour", allipstr);//删除序列
                            ctable.deleteTable(conn, "cpuday", allipstr,
                                    "cpuday");// CPU
                            // ctable.dropSeq(conn, "cpuday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 带宽利用率
                        try {
                            ctable.deleteTable(conn, "utilhdxperc", allipstr,
                                    "hdperc");
                            // ctable.dropSeq(conn, "utilhdxperc",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "hdxperchour", allipstr,
                                    "hdperchour");
                            // ctable.dropSeq(conn, "hdxperchour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "hdxpercday", allipstr,
                                    "hdpercday");
                            // ctable.dropSeq(conn, "hdxpercday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 端口状态
                        try {
                            ctable.deleteTable(conn, "portstatus", allipstr,
                                    "port");
                        } catch (Exception e) {
                        }

                        // 流速
                        try {
                            ctable
                                    .deleteTable(conn, "utilhdx", allipstr,
                                            "hdx");
                            // ctable.dropSeq(conn, "utilhdx", allipstr);//删除序列
                            ctable.deleteTable(conn, "utilhdxhour", allipstr,
                                    "hdxhour");
                            // ctable.dropSeq(conn, "utilhdxhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "utilhdxday", allipstr,
                                    "hdxday");
                            // ctable.dropSeq(conn, "utilhdxday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 综合流速
                        try {
                            ctable.deleteTable(conn, "allutilhdx", allipstr,
                                    "allhdx");
                            // ctable.dropSeq(conn, "allutilhdx",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "allutilhdxhour",
                                    allipstr, "allhdxhour");
                            // ctable.dropSeq(conn, "allutilhdxhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "allutilhdxday", allipstr,
                                    "allhdxday");
                            // ctable.dropSeq(conn, "allutilhdxday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 丢包率
                        try {
                            ctable.deleteTable(conn, "discardsperc", allipstr,
                                    "dcardperc");
                            // ctable.dropSeq(conn, "discardsperc",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "dcardperchour", allipstr,
                                    "dcardperchour");
                            // ctable.dropSeq(conn, "dcardperchour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "dcardpercday", allipstr,
                                    "dcardpercday");
                            // ctable.dropSeq(conn, "dcardpercday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 错误率
                        try {
                            ctable.deleteTable(conn, "errorsperc", allipstr,
                                    "errperc");
                            // ctable.dropSeq(conn, "errorsperc",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "errperchour", allipstr,
                                    "errperchour");
                            // ctable.dropSeq(conn, "errperchour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "errpercday", allipstr,
                                    "errpercday");
                            // ctable.dropSeq(conn, "errpercday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 数据包
                        try {
                            ctable
                                    .deleteTable(conn, "packs", allipstr,
                                            "packs");
                            // ctable.dropSeq(conn, "packs", allipstr);//删除序列
                            ctable.deleteTable(conn, "packshour", allipstr,
                                    "packshour");
                            // ctable.dropSeq(conn, "packshour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "packsday", allipstr,
                                    "packsday");
                            // ctable.dropSeq(conn, "packsday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 入口数据包
                        try {
                            ctable.deleteTable(conn, "inpacks", allipstr,
                                    "inpacks");
                            // ctable.dropSeq(conn, "inpacks", allipstr);//删除序列
                            ctable.deleteTable(conn, "inpackshour", allipstr,
                                    "inpackshour");
                            // ctable.dropSeq(conn, "inpackshour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "inpacksday", allipstr,
                                    "inpacksday");
                            // ctable.dropSeq(conn, "inpacksday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 出口数据包
                        try {
                            ctable.deleteTable(conn, "outpacks", allipstr,
                                    "outpacks");
                            // ctable.dropSeq(conn, "outpacks", allipstr);//删除序列
                            ctable.deleteTable(conn, "outpackshour", allipstr,
                                    "outpackshour");
                            // ctable.dropSeq(conn, "outpackshour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "outpacksday", allipstr,
                                    "outpacksday");
                            // ctable.dropSeq(conn, "outpacksday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 温度
                        try {
                            ctable.deleteTable(conn, "temper", allipstr,
                                    "temper");
                            // ctable.dropSeq(conn, "temper", allipstr);//删除序列
                            ctable.deleteTable(conn, "temperhour", allipstr,
                                    "temperhour");
                            // ctable.dropSeq(conn, "temperhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "temperday", allipstr,
                                    "temperday");
                            // ctable.dropSeq(conn, "temperday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
                        try {
                            dcDao.deleteMonitor(host.getId(), host
                                    .getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dcDao.close();
                            conn.close();
                        }

                        EventListDao eventdao = new EventListDao();
                        try {
                            // 同时删除事件表里的相关数据
                            eventdao.delete(host.getId(), "network");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            eventdao.close();
                        }

                        PortconfigDao portconfigdao = new PortconfigDao();
                        try {
                            // 同时删除端口配置表里的相关数据
                            portconfigdao
                                    .deleteByIpaddress(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            portconfigdao.close();
                        }

                        // 删除nms_ipmacchange表里的对应的数据
                        IpMacChangeDao macchangebasedao = new IpMacChangeDao();
                        try {
                            // delte后,conn已经关闭
                            macchangebasedao
                                    .deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            macchangebasedao.close();
                        }

                        // 删除网络设备配置文件表里的对应的数据
                        NetNodeCfgFileDao configdao = new NetNodeCfgFileDao();
                        try {
                            // delte后,conn已经关闭
                            configdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            configdao.close();
                        }

                        // 删除网络设备SYSLOG接收表里的对应的数据
                        NetSyslogDao syslogdao = new NetSyslogDao();
                        try {
                            // delte后,conn已经关闭
                            syslogdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            syslogdao.close();
                        }

                        // 删除网络设备端口扫描表里的对应的数据
                        PortScanDao portscandao = new PortScanDao();
                        try {
                            // delte后,conn已经关闭
                            portscandao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            portscandao.close();
                        }

                        // 删除网络设备面板图表里的对应的数据
                        IpaddressPanelDao addresspaneldao = new IpaddressPanelDao();
                        try {
                            // delte后,conn已经关闭
                            addresspaneldao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            addresspaneldao.close();
                        }

                        // 删除网络设备接口表里的对应的数据
                        HostInterfaceDao interfacedao = new HostInterfaceDao();
                        try {
                            // delte后,conn已经关闭
                            interfacedao.deleteByHostId(host.getId() + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            interfacedao.close();
                        }

                        // 删除网络设备IP别名表里的对应的数据
                        IpAliasDao ipaliasdao = new IpAliasDao();
                        try {
                            // delte后,conn已经关闭
                            ipaliasdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ipaliasdao.close();
                        }

                        // 删除网络设备手工配置的链路表里的对应的数据
                        RepairLinkDao repairdao = new RepairLinkDao();
                        try {
                            // delte后,conn已经关闭
                            repairdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            repairdao.close();
                        }

                        // 删除网络设备IPMAC表里的对应的数据
                        IpMacDao ipmacdao = new IpMacDao();
                        try {
                            // delte后,conn已经关闭
                            ipmacdao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ipmacdao.close();
                        }

                        // 删除该设备的采集指标
                        NodeGatherIndicatorsDao gatherdao = new NodeGatherIndicatorsDao();
                        try {
                            gatherdao.deleteByNodeIdAndTypeAndSubtype(host
                                    .getId()
                                    + "", "net", "");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            gatherdao.close();
                        }

                        // 删除网络设备指标采集表里的对应的数据
                        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
                        try {
                            // delte后,conn已经关闭
                            indicatdao.deleteByNodeId(host.getId() + "", "net");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            indicatdao.close();
                        }

                        // 删除IP-MAC-BASE表里的对应的数据
                        IpMacBaseDao macbasedao = new IpMacBaseDao();
                        try {
                            // delte后,conn已经关闭
                            macbasedao.deleteByHostIp(host.getIpAddress());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            macbasedao.close();
                        }
                    } else if (host.getCategory() == 4) {
                        // 删除主机服务器
                        try {
                            ctable.deleteTable(conn, "pro", allipstr, "pro");// 进程
                            // ctable.dropSeq(conn, "pro", allipstr);//删除序列
                            ctable.deleteTable(conn, "prohour", allipstr,
                                    "prohour");// 进程小时
                            // ctable.dropSeq(conn, "prohour", allipstr);//删除序列
                            ctable.deleteTable(conn, "proday", allipstr,
                                    "proday");// 进程天
                            // ctable.dropSeq(conn, "proday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "log", allipstr, "log");// 进程天
                            // ctable.dropSeq(conn, "log", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "memory", allipstr, "mem");// 内存
                            // ctable.dropSeq(conn, "memory", allipstr);//删除序列
                            ctable.deleteTable(conn, "memoryhour", allipstr,
                                    "memhour");// 内存
                            // ctable.dropSeq(conn, "memoryhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "memoryday", allipstr,
                                    "memday");// 内存
                            // ctable.dropSeq(conn, "memoryday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
                            // ctable.dropSeq(conn, "cpu", allipstr);//删除序列
                            ctable.deleteTable(conn, "cpuhour", allipstr,
                                    "cpuhour");// CPU
                            // ctable.dropSeq(conn, "cpuhour", allipstr);//删除序列
                            ctable.deleteTable(conn, "cpuday", allipstr,
                                    "cpuday");// CPU
                            // ctable.dropSeq(conn, "cpuday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "cpudtl", allipstr,
                                    "cpudtl");// CPU
                            ctable.deleteTable(conn, "cpudtlhour", allipstr,
                                    "cpudtlhour");// CPU
                            ctable.deleteTable(conn, "cpudtlday", allipstr,
                                    "cpudtlday");// CPU
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "disk", allipstr, "disk");// disk
                            // ctable.dropSeq(conn, "disk", allipstr);//删除序列
                            ctable.deleteTable(conn, "diskhour", allipstr,
                                    "diskhour");// disk
                            // ctable.dropSeq(conn, "diskhour", allipstr);//删除序列
                            ctable.deleteTable(conn, "diskday", allipstr,
                                    "diskday");// disk
                            // ctable.dropSeq(conn, "diskday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "diskincre", allipstr,
                                    "diskincre");// 磁盘增长
                            // ctable.dropSeq(conn, "diskincre",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "diskincrehour", allipstr,
                                    "diskincrehour");// 磁盘增长
                            // ctable.dropSeq(conn, "diskincrehour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "diskincreday", allipstr,
                                    "diskincreday");// 磁盘增长
                            // ctable.dropSeq(conn, "diskincreday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        /*
                         * ctable.createTable("disk",allipstr,"disk");
                         * ctable.createTable("diskhour",allipstr,"diskhour");
                         * ctable.createTable("diskday",allipstr,"diskday");
                         */
                        try {
                            ctable.deleteTable(conn, "ping", allipstr, "ping");
                            // ctable.dropSeq(conn, "ping", allipstr);//删除序列
                            ctable.deleteTable(conn, "pinghour", allipstr,
                                    "pinghour");
                            // ctable.dropSeq(conn, "pinghour", allipstr);//删除序列
                            ctable.deleteTable(conn, "pingday", allipstr,
                                    "pingday");
                            // ctable.dropSeq(conn, "pingday", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "utilhdxperc", allipstr,
                                    "hdperc");
                            // ctable.dropSeq(conn, "utilhdxperc",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "hdxperchour", allipstr,
                                    "hdperchour");
                            // ctable.dropSeq(conn, "hdxperchour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "hdxpercday", allipstr,
                                    "hdpercday");
                            // ctable.dropSeq(conn, "hdxpercday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable
                                    .deleteTable(conn, "utilhdx", allipstr,
                                            "hdx");
                            // ctable.dropSeq(conn, "utilhdx", allipstr);//删除序列
                            ctable.deleteTable(conn, "utilhdxhour", allipstr,
                                    "hdxhour");
                            // ctable.dropSeq(conn, "utilhdxhour",
                            // allipstr);//删除序列
                            ctable.deleteTable(conn, "utilhdxday", allipstr,
                                    "hdxday");
                            // ctable.dropSeq(conn, "utilhdxday",
                            // allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        try {
                            ctable.deleteTable(conn, "software", allipstr,
                                    "software");
                            // ctable.dropSeq(conn, "software", allipstr);//删除序列
                        } catch (Exception e) {

                        }

                        // 温度
                        try {
                            ctable.deleteTable(conn, "temper", allipstr,
                                    "temper");
                            ctable.deleteTable(conn, "temperhour", allipstr,
                                    "temperhour");
                            ctable.deleteTable(conn, "temperday", allipstr,
                                    "temperday");
                        } catch (Exception e) {

                        }

                        ctable.deleteTable(conn, "allutilhdx", allipstr,
                                "allhdx");
                        // ctable.dropSeq(conn, "allutilhdx", allipstr);//删除序列
                        ctable.deleteTable(conn, "allutilhdxhour", allipstr,
                                "allhdxhour");
                        // ctable.dropSeq(conn, "allutilhdxhour",
                        // allipstr);//删除序列
                        ctable.deleteTable(conn, "allutilhdxday", allipstr,
                                "allhdxday");
                        // ctable.dropSeq(conn, "allutilhdxday",
                        // allipstr);//删除序列

                        ctable.deleteTable(conn, "discardsperc", allipstr,
                                "dcardperc");
                        // ctable.dropSeq(conn, "discardsperc", allipstr);//删除序列
                        ctable.deleteTable(conn, "dcardperchour", allipstr,
                                "dcardperchour");
                        // ctable.dropSeq(conn, "dcardperchour",
                        // allipstr);//删除序列
                        ctable.deleteTable(conn, "dcardpercday", allipstr,
                                "dcardpercday");
                        // ctable.dropSeq(conn, "dcardpercday", allipstr);//删除序列

                        ctable.deleteTable(conn, "errorsperc", allipstr,
                                "errperc");
                        // ctable.dropSeq(conn, "errorsperc", allipstr);//删除序列
                        ctable.deleteTable(conn, "errperchour", allipstr,
                                "errperchour");
                        // ctable.dropSeq(conn, "errperchour", allipstr);//删除序列
                        ctable.deleteTable(conn, "errpercday", allipstr,
                                "errpercday");
                        // ctable.dropSeq(conn, "errpercday", allipstr);//删除序列

                        ctable.deleteTable(conn, "packs", allipstr, "packs");
                        // ctable.dropSeq(conn, "packs", allipstr);//删除序列
                        ctable.deleteTable(conn, "packshour", allipstr,
                                "packshour");
                        // ctable.dropSeq(conn, "packshour", allipstr);//删除序列
                        ctable.deleteTable(conn, "packsday", allipstr,
                                "packsday");
                        // ctable.dropSeq(conn, "packsday", allipstr);//删除序列

                        ctable
                                .deleteTable(conn, "inpacks", allipstr,
                                        "inpacks");
                        // ctable.dropSeq(conn, "inpacks", allipstr);//删除序列
                        ctable.deleteTable(conn, "inpackshour", allipstr,
                                "inpackshour");
                        // ctable.dropSeq(conn, "inpackshour", allipstr);//删除序列
                        ctable.deleteTable(conn, "inpacksday", allipstr,
                                "inpacksday");
                        // ctable.dropSeq(conn, "inpacksday", allipstr);//删除序列

                        ctable.deleteTable(conn, "outpacks", allipstr,
                                "outpacks");
                        // ctable.dropSeq(conn, "outpacks", allipstr);//删除序列
                        ctable.deleteTable(conn, "outpackshour", allipstr,
                                "outpackshour");
                        // ctable.dropSeq(conn, "outpackshour", allipstr);//删除序列
                        ctable.deleteTable(conn, "outpacksday", allipstr,
                                "outpacksday");
                        // ctable.dropSeq(conn, "outpacksday", allipstr);//删除序列

                        if (host.getOstype() == 15) {
                            // AS400
                            ctable.deleteTable(conn, "systemasp", allipstr,
                                    "systemasp");
                            // ctable.dropSeq(conn, "systemasp",
                            // allipstr);//生成序列
                            ctable.deleteTable(conn, "systemasphour", allipstr,
                                    "systemasphour");
                            // ctable.dropSeq(conn, "systemasphour",
                            // allipstr);//生成序列
                            ctable.deleteTable(conn, "systemaspday", allipstr,
                                    "systemaspday");
                            // ctable.dropSeq(conn, "systemaspday",
                            // allipstr);//生成序列

                            ctable.deleteTable(conn, "dbcapability", allipstr,
                                    "dbcapability");
                            // ctable.dropSeq(conn, "dbcapability",
                            // allipstr);//生成序列
                            ctable.deleteTable(conn, "dbcaphour", allipstr,
                                    "dbcaphour");
                            // ctable.dropSeq(conn, "dbcaphour",
                            // allipstr);//生成序列
                            ctable.deleteTable(conn, "dbcapday", allipstr,
                                    "dbcapday");
                            // ctable.dropSeq(conn, "dbcapday", allipstr);//生成序列
                        }

                        if (host.getOstype() == 6) {
                            // AIX系统,增加页使用率
                            ctable.deleteTable(conn, "page", allipstr, "page");// page
                            // ctable.dropSeq(conn, "page", allipstr);//生成序列
                            ctable.deleteTable(conn, "pagehour", allipstr,
                                    "pagehour");// page
                            // ctable.dropSeq(conn, "pagehour", allipstr);//生成序列
                            ctable.deleteTable(conn, "pageday", allipstr,
                                    "pageday");// page
                            // ctable.dropSeq(conn, "pageday", allipstr);//生成序列
                        }

                        // 删除该设备的采集指标
                        NodeGatherIndicatorsDao gatherdao = new NodeGatherIndicatorsDao();
                        try {
                            gatherdao.deleteByNodeIdAndTypeAndSubtype(host
                                    .getId()
                                    + "", "host", "");
                        } catch (RuntimeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            gatherdao.close();
                        }

                        // 删除服务器指标采集表里的对应的数据
                        AlarmIndicatorsNodeDao indicatdao = new AlarmIndicatorsNodeDao();
                        try {
                            // delte后,conn已经关闭
                            indicatdao
                                    .deleteByNodeId(host.getId() + "", "host");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            indicatdao.close();
                        }

                        EventListDao eventdao = new EventListDao();
                        try {
                            // 同时删除事件表里的相关数据
                            eventdao.delete(host.getId(), "host");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            eventdao.close();
                        }

                        // 删除diskconfig
                        String[] otherTempData = new String[] { "nms_diskconfig" };
                        String[] ipStrs = new String[] { host.getIpAddress() };
                        ctable.clearTablesData(otherTempData, "ipaddress",
                                ipStrs);
                        // 删除进程组的数据
                        ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
                        processGroupConfigurationUtil
                                .deleteProcessGroupAndConfigurationByNodeid(host
                                        .getId()
                                        + "");

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    conn.close();
                }
            } else {
                try {
                    // 同时删除事件表里的相关数据
                    EventListDao eventdao = new EventListDao();
                    eventdao.delete(Integer.parseInt(id), category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CommonDao cdao = new CommonDao(tvo.getTableName());
            cdao.delete(id);
            String[] ids = { id };
            // 删除设备在临时表里中存储的数据
            String[] nmsTempDataTables = { "nms_cpu_data_temp",
                    "nms_device_data_temp", "nms_disk_data_temp",
                    "nms_diskperf_data_temp", "nms_envir_data_temp",
                    "nms_fdb_data_temp", "nms_fibrecapability_data_temp",
                    "nms_fibreconfig_data_temp", "nms_flash_data_temp",
                    "nms_interface_data_temp", "nms_lights_data_temp",
                    "nms_memory_data_temp", "nms_other_data_temp",
                    "nms_ping_data_temp", "nms_process_data_temp",
                    "nms_route_data_temp", "nms_sercice_data_temp",
                    "nms_software_data_temp", "nms_storage_data_temp",
                    "nms_system_data_temp", "nms_user_data_temp",
                    "nms_nodeconfig", "nms_nodecpuconfig",
                    "nms_nodediskconfig", "nms_nodememconfig" };
            CreateTableManager createTableManager = new CreateTableManager();
            createTableManager.clearNmsTempDatas(nmsTempDataTables, ids);
        }

        // 2.更新xml
        // 主机服务器
        XmlOperator opr1 = new XmlOperator();
        opr1.setFile("server.jsp");
        opr1.init4updateXml();
        if (opr1.isNodeExist(id)) {
            opr1.deleteNodeByID(id);
        }
        opr1.writeXml();
        // 更新所有拓扑图
        ManageXmlDao mdao = new ManageXmlDao();
        List<ManageXml> list = mdao.loadAll();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ManageXml manageXml = list.get(i);
                XmlOperator xopr = new XmlOperator();
                xopr.setFile(manageXml.getXmlName());
                xopr.init4updateXml();
                if (xopr.isNodeExist(nodeid)) {
                    xopr.deleteNodeByID(nodeid);
                }
                xopr.writeXml();
            }
        }
        // 删除指标全局阈值表对应的数据
        NodeMonitorDao nodeMonitorDao = new NodeMonitorDao();
        try {
            nodeMonitorDao.deleteByID(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            nodeMonitorDao.close();
        }
        // 删除关联拓扑图表的数据
        RelationDao rdao = new RelationDao();
        Relation vo = (Relation) rdao.findByNodeId(id, xmlName);
        if (vo != null) {
            rdao.deleteByNode(id, xmlName);
        } else {
            rdao.close();
        }
        // 删除关联图元表的数据
        NodeEquipDao nodeEquipDao = new NodeEquipDao();
        if (nodeEquipDao.findByNode(nodeid) != null) {
            nodeEquipDao.deleteByNode(nodeid);
        } else {
            nodeEquipDao.close();
        }
        // 如果是业务视图，则删除相关表数据
        if (xmlName.indexOf("businessmap") != -1) {
            LineDao lineDao = new LineDao();
            lineDao.deleteByidXml(nodeid, xmlName);
            NodeDependDao nodeDependDao = new NodeDependDao();
            if (nodeDependDao.isNodeExist(nodeid, xmlName)) {
                nodeDependDao.deleteByIdXml(nodeid, xmlName);
            } else {
                nodeDependDao.close();
            }

            // yangjun
            User user = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER);
            ManageXmlDao mXmlDao = new ManageXmlDao();
            List xmlList = new ArrayList();
            try {
                xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mXmlDao.close();
            }
            try {
                ChartXml chartxml;
                chartxml = new ChartXml("tree");
                chartxml.addViewTree(xmlList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ManageXmlDao subMapDao = new ManageXmlDao();
            ManageXml manageXml = (ManageXml) subMapDao.findByXml(xmlName);
            if (manageXml != null) {
                NodeDependDao nodeDepenDao = new NodeDependDao();
                try {
                    List lists = nodeDepenDao.findByXml(xmlName);
                    ChartXml chartxml;
                    chartxml = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml"));
                    chartxml.addBussinessXML(manageXml.getTopoName(), lists);
                    ChartXml chartxmlList;
                    chartxmlList = new ChartXml("NetworkMonitor", "/"
                            + xmlName.replace("jsp", "xml").replace(
                                    "businessmap", "list"));
                    chartxmlList.addListXML(manageXml.getTopoName(), lists);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    nodeDepenDao.close();
                }
            }
        }
        // yangjun add
        ConnectTypeConfigDao connectTypeConfigDao = new ConnectTypeConfigDao();
        try {
            connectTypeConfigDao.deleteByNodeId(id);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            connectTypeConfigDao.close();
        }
        return null;

    }

    // 准备编辑子图属性
    private String readyEditSubMap() {
        String fileName = (String) getParaValue("xml");
        ManageXmlDao dao = new ManageXmlDao();
        ManageXml vo = (ManageXml) dao.findByXml(fileName);
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return "/topology/submap/editSubMap.jsp";
    }

    // 保存编辑子图属性
    private String editSubMap() {
        User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        ManageXmlDao mxDao = new ManageXmlDao();
        if (getParaIntValue("topo_type") == 1) {
            mxDao.updateBusView(uservo.getBusinessids());
        } else {
            mxDao.updateView(uservo.getBusinessids());
        }
        mxDao.close();
        ManageXml vo = new ManageXml();
        String bid = ",";
        String arr[] = getParaArrayValue("checkbox");
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                bid = bid + arr[i] + ",";
            }
        }
        vo.setId(getParaIntValue("id"));
        vo.setXmlName(getParaValue("xml_name"));
        vo.setTopoName(getParaValue("topo_name"));
        vo.setAliasName(getParaValue("alias_name"));
        vo.setTopoTitle(getParaValue("topo_title"));
        vo.setTopoArea(getParaValue("topo_area"));
        vo.setTopoBg(getParaValue("topo_bg"));
        vo.setTopoType(getParaIntValue("topo_type"));
        if (getParaValue("home_view") != null
                && Integer.parseInt(getParaValue("home_view")) == 1) {
            if (Integer.parseInt(getParaValue("topo_type")) == 1) {
                vo.setBus_home_view(1);
                vo.setHome_view(0);
            } else {
                vo.setHome_view(1);
                vo.setBus_home_view(0);
            }
            vo.setPercent(Float.parseFloat(getParaValue("zoom_percent")));
        } else {
            vo.setBus_home_view(0);
            vo.setHome_view(0);
            vo.setPercent(1);
        }
        vo.setBid(bid);
        DaoInterface dao = new ManageXmlDao();
        update(dao, vo);
        return null;

    }

    // 删除子图
    private String deleteSubMap() {
        String fileName = (String) getParaValue("xml");
        ManageXmlDao dao = new ManageXmlDao();
        ManageXml map = (ManageXml) dao.delete(fileName);// 删除子图表的数据
        if (map != null) {
            XmlOperator xmlOpr = new XmlOperator();
            xmlOpr.setFile(fileName);
            xmlOpr.deleteXml();// 删除文件夹下对应子图的xml文件
            RelationDao rdao = new RelationDao();
            String str = rdao.delete(map.getId() + "");// 删除关联拓扑图表的数据
            if (str != null && !"".equals(str)) {
                String node[] = str.split(",");
                String nodeId = node[0];
                String xmlName = node[1];
                ManageXmlOperator mXmlOpr = new ManageXmlOperator();
                mXmlOpr.setFile(xmlName);
                mXmlOpr.init4updateXml();
                if (mXmlOpr.isNodeExist(nodeId)) {
                    mXmlOpr.updateNode(nodeId, "relationMap", "");
                }
                mXmlOpr.writeXml();
            }
            // PollingEngine.getInstance().deleteBusByID(map.getId());//按照节点类型和id更新内存20100513
            if (map != null && map.getTopoType() == 1) {
                PollingEngine.getInstance().deleteBusByID(map.getId());// 按照节点类型和id更新内存20100513
            }

            // 删除节点与图元图片关联表的数据
            NodeEquipDao nodeEquipDao = new NodeEquipDao();
            if (nodeEquipDao.findByXml(fileName) != null) {
                nodeEquipDao.deleteByXml(fileName);
            } else {
                nodeEquipDao.close();
            }
            // 删除业务视图
            if (map.getTopoType() == 1) {
                xmlOpr = new XmlOperator();
                xmlOpr.setfile(fileName.replace("jsp", "xml"));
                xmlOpr.deleteXml();// 删除文件夹下对应业务视图的xml文件
                xmlOpr.setfile(fileName.replace("jsp", "xml").replace(
                        "businessmap", "list"));
                xmlOpr.deleteXml();// 删除文件夹下对应业务视图的xml文件

                User user = (User) session
                        .getAttribute(SessionConstant.CURRENT_USER);
                ManageXmlDao mXmlDao = new ManageXmlDao();
                List xmlList = new ArrayList();
                try {
                    xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mXmlDao.close();
                }
                try {
                    ChartXml chartxml;
                    chartxml = new ChartXml("tree");
                    chartxml.addViewTree(xmlList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 删除关联表数据
            NodeDependDao nodeDependDao = new NodeDependDao();
            HintNodeDao hintNodeDao = new HintNodeDao();
            LineDao lineDao = new LineDao();
            try {
                nodeDependDao.deleteByXml(fileName);
                hintNodeDao.deleteByXml(fileName);
                lineDao.deleteByXml(fileName);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                nodeDependDao.close();
                hintNodeDao.close();
                lineDao.close();
            }
        }
        return null;
    }
    
    //根据NodeId，保存默认打开的拓扑图XML名称
    public String saveTopologyXmlByNode(String nodeId,String xmlName){
    	XmlNodeStandardVo vo = new XmlNodeStandardVo();
    	vo.setNodeId(nodeId);
    	vo.setXmlName(xmlName);
    	String flag = "error";
    	XmlNodeStandardDao dao = new XmlNodeStandardDao();
    	try {
    		if(nodeId !=null && !"".equals(nodeId) && xmlName != null){
    			XmlNodeStandardVo nodeStandardVo = dao.getXmlNodeStandardVoByNodeId(nodeId);
	       		if(nodeStandardVo != null){
	       			nodeStandardVo.setXmlName(xmlName);
	       			//已經存在，只需要修改
	       			dao = new XmlNodeStandardDao();
	       			try {
	       				dao.update(nodeStandardVo);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						dao.close();
					}
	       		} else {
	       			//不存在，新增
	       			dao = new XmlNodeStandardDao();
	       			try {
	       				dao.save(vo);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						dao.close();
					}
	       		}
	       		flag = "success";
	       	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return flag;
    }
    
    //查询默认打开的拓扑图XML
    public String getNodeTologyXMLName(String nodeId,String type,String subtype) {
    	String xmlName = "network.jsp";	//默认network.jsp
    	XmlNodeStandardDao dao = new XmlNodeStandardDao();
    	try {
    		XmlNodeStandardVo nodeStandardVo = dao.getXmlNodeStandardVo(nodeId, type, subtype);
    		if(nodeStandardVo != null){
    			xmlName = nodeStandardVo.getXmlName();
    		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
    	return xmlName;
    }

    // 保存关联拓扑图的节点
    private String relationMap() {

        String fileName = (String) getParaValue("xml");
        RelationDao dao = new RelationDao();
        Relation vo = new Relation();
        vo.setXmlName(fileName);
        vo.setNodeId(getParaValue("nodeId"));// 节点id
        vo.setCategory(getParaValue("category"));// 节点类型
        vo.setMapId(getParaValue("radio"));// 子图的id
        Relation vo1 = (Relation) dao.findByNodeId(getParaValue("nodeId"),
                fileName);
        if (vo1 != null) {
            vo.setId(vo1.getId());
            update(dao, vo);
        } else {
            save(dao, vo);
        }
        ManageXmlDao mdao = new ManageXmlDao();
        ManageXml manageXml = (ManageXml) mdao.findByID(getParaValue("radio"));
        ManageXmlOperator mXmlOpr = new ManageXmlOperator();
        mXmlOpr.setFile(fileName);
        mXmlOpr.init4updateXml();
        if (mXmlOpr.isNodeExist(getParaValue("nodeId"))) {
            mXmlOpr.updateNode(getParaValue("nodeId"), "relationMap", manageXml
                    .getXmlName());
        }
        mXmlOpr.writeXml();
        mdao.close();
        return null;
    }

    // 取消关联节点
    private String cancelRelation() {
        String fileName = getParaValue("xml");
        RelationDao dao = new RelationDao();
        dao.deleteByNode(getParaValue("nodeId"), fileName);
        ManageXmlOperator mXmlOpr = new ManageXmlOperator();
        mXmlOpr.setFile(fileName);
        mXmlOpr.init4updateXml();
        if (mXmlOpr.isNodeExist(getParaValue("nodeId"))) {
            mXmlOpr.updateNode(getParaValue("nodeId"), "relationMap", "");
        }
        mXmlOpr.writeXml();
        return null;

    }

    // 在子图上创建示意链路
    private String addLines() {
        String xml = getParaValue("xml");

        ManageXmlOperator mxmlOpr = new ManageXmlOperator();
        mxmlOpr.setFile(xml);
        mxmlOpr.init4updateXml();
        String id1 = getParaValue("id1");
        String id2 = getParaValue("id2");
        int lineId = mxmlOpr.findMaxDemoLineId();
        mxmlOpr.addLine("hl" + lineId, id1, id2);
        mxmlOpr.writeXml();

        return "/topology/submap/change.jsp?submapview=" + xml;
    }

    // 删除子图上的示意链路
    private String deleteLines() {
        String id = getParaValue("id");
        String xml = getParaValue("xml");
        // 删除示意链路表数据
        LineDao lineDao = new LineDao();
        // 更新xml
        if (!"".equals(id) && !"".equals(xml)) {
            ManageXmlOperator mxmlOpr = new ManageXmlOperator();
            mxmlOpr.setFile(xml);
            mxmlOpr.init4updateXml();
            if (lineDao.delete(id, xml)) {
                NodeDependDao nodeDependDao = new NodeDependDao();
                System.out.println(nodeDependDao.isNodeExist(id, xml)
                        + "==============");
                if (nodeDependDao.isNodeExist(id, xml)) {
                    nodeDependDao.deleteByIdXml(id, xml);
                } else {
                    nodeDependDao.close();
                }
                mxmlOpr.deleteDemoLinesByID(id);
            }
            mxmlOpr.writeXml();
        }
        return "/topology/submap/change.jsp?submapview=" + xml;
    }

    // 子图上创建示意图元
    private String readyAddHintMeta() {

        String galleryPanel = "";
        String resTypeName = getParaValue("resTypeName");
        String fileName = getParaValue("xml");
        if (resTypeName == null || "".equals(resTypeName)) {
            resTypeName = "路由器";
        } else {
            try {
                resTypeName = new String(resTypeName.getBytes("ISO-8859-1"),
                        "gb2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HintItemDao hintItemDao = new HintItemDao();
        TopoUI topoUI = new TopoUI();
        List<String> list = hintItemDao.getGalleryListing();
        String gallery = topoUI.createGallery(list, -1, 1);

        Map map = hintItemDao.getGallery(resTypeName);
        if (map != null) {
            Object obj = map.get(resTypeName);
            if (obj instanceof List) {
                List iconList = (List) obj;
                galleryPanel = topoUI.getGalleryPanel(iconList);
            }
        }
        hintItemDao.close();
        request.setAttribute("resTypeName", resTypeName);
        request.setAttribute("galleryPanel", galleryPanel);
        request.setAttribute("gallery", gallery);
        request.setAttribute("fileName", fileName);
        return "/topology/submap/gallery.jsp";
    }

    // 保存示意图元
    public String addHintMeta(String[] returnValue) {
        String result = "error";
        String str = "";
        String equipName = "";
        int nodeId = 0;
        if (returnValue.length == 4) {
            ManageXmlOperator mXmlOpr = new ManageXmlOperator();
            mXmlOpr.setFile(returnValue[2]);
            mXmlOpr.init4editNodes();
            str = returnValue[3];
            equipName = returnValue[0];
            nodeId = mXmlOpr.findMaxNodeId();
            HintNodeDao hintNodeDao = new HintNodeDao();
            HintNode hintNode = new HintNode();
            hintNode.setName(equipName);
            hintNode.setNodeId("hin" + String.valueOf(nodeId));
            hintNode.setXmlfile(returnValue[2]);
            hintNode.setImage(returnValue[1]);
            hintNode.setAlias(equipName);
            if (hintNodeDao.save(hintNode) && returnValue[1] != null
                    && !"".equals(returnValue[1])) {
                mXmlOpr.addDemoNode("hin" + String.valueOf(nodeId), str,
                        returnValue[1].substring(17), equipName, String
                                .valueOf(1 * 30), "15");
                result = "hin" + String.valueOf(nodeId);
            }
            mXmlOpr.writeXml();
        }
        return result;
    }

    // 删除示意设备
    private String deleteHintMeta() {
        String id = getParaValue("nodeId");
        String fileName = getParaValue("xml");

        HintNodeDao hintNodeDao = new HintNodeDao();
        LineDao lineDao = new LineDao();
        // 更新xml
        XmlOperator opr = new XmlOperator();
        opr.setFile(fileName);
        opr.init4updateXml();
        if (hintNodeDao.deleteByXml(id, fileName)
                && lineDao.deleteByidXml(id, fileName)) {
            if (fileName.indexOf("businessmap") != -1) {// 如果是业务视图，则删除节点父子关系表数据
                NodeDependDao nodeDependDao = new NodeDependDao();
                if (nodeDependDao.isNodeExist(id, fileName)) {
                    nodeDependDao.deleteByIdXml(id, fileName);
                } else {
                    nodeDependDao.close();
                }
            }
            opr.deleteNodeById(id);
        }
        opr.writeXml();
        // 删除关联拓扑图表的数据
        RelationDao rdao = new RelationDao();
        Relation vo = (Relation) rdao.findByNodeId(id, fileName);
        if (vo != null) {
            rdao.deleteByNode(id, fileName);
        } else {
            rdao.close();
        }
        return null;
    }

    // 子图上创建实体链路yangjun add
    private String readyAddLink() {
        HostNodeDao dao = new HostNodeDao();
        List list = null;
        try {
            list = dao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        String fileName = getParaValue("xml");
        String start_id = getParaValue("start_id");
        String end_id = getParaValue("end_id");
        int id1 = Integer.parseInt(start_id.substring(3));
        int id2 = Integer.parseInt(end_id.substring(3));
        String alias1 = "";
        String ipAddress1 = "";
        String alias2 = "";
        String ipAddress2 = "";
        String link_name = "";
        for (int i = 0; i < list.size(); i++) {
            HostNode node = (HostNode) list.get(i);
            if (node.getId() == id1) {
                alias1 = node.getAlias();
                ipAddress1 = node.getIpAddress();
            }
            if (node.getId() == id2) {
                alias2 = node.getAlias();
                ipAddress2 = node.getIpAddress();
            }
        }
        link_name = ipAddress1 + "/" + ipAddress2;
        List<Portconfig> portconfigList1 = null;
        PortconfigDao portconfigDao = new PortconfigDao();
        try {
            portconfigList1 = portconfigDao.loadByIpaddress(ipAddress1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        List<Portconfig> portconfigList2 = null;
        portconfigDao = new PortconfigDao();
        try {
            portconfigList2 = portconfigDao.loadByIpaddress(ipAddress2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        if (portconfigList1 == null) {
            portconfigList1 = new ArrayList<Portconfig>();
        }
        if (portconfigList2 == null) {
            portconfigList2 = new ArrayList<Portconfig>();
        }

        request.setAttribute("start_if", portconfigList1);
        request.setAttribute("end_if", portconfigList2);
        request.setAttribute("alias_start", alias1);
        request.setAttribute("ipAddress_start", ipAddress1);
        request.setAttribute("alias_end", alias2);
        request.setAttribute("ipAddress_end", ipAddress2);
        request.setAttribute("start_id", start_id);
        request.setAttribute("end_id", end_id);
        request.setAttribute("link_name", link_name);
        request.setAttribute("xml", fileName);

        return "/topology/submap/addLink.jsp";
    }

    // 保存实体链路
    public String addLink(String direction1, String linkName, String maxSpeed,
            String maxPer, String xml, String start_id, String start_index,
            String end_id, String end_index, String cable_type, String cable_capacity) {

        String returns = "error";
        String startIndex = "";
        String endIndex = "";
        String start_Id = "";
        String end_Id = "";

        if (direction1 != null && direction1.equals("1")) {// 上行设备
            startIndex = start_index;
            endIndex = end_index;
            start_Id = start_id;
            end_Id = end_id;
        } else {// 下行设备
            startIndex = end_index;
            endIndex = start_index;
            start_Id = end_id;
            end_Id = start_id;
        }
        if (!"".equals(start_Id) && !"".equals(end_Id)) {
            int startId = Integer.parseInt(start_Id.substring(3));
            int endId = Integer.parseInt(end_Id.substring(3));

            LinkDao dao = new LinkDao();
            try {
                int exist = dao
                        .linkExists(startId, startIndex, endId, endIndex);
                if (exist == 1) {
                    setErrorCode(ErrorMessage.LINK_EXIST);
                    dao.close();
                    return "error1";
                }
                if (exist == 2) {
                    setErrorCode(ErrorMessage.DOUBLE_LINKS);
                    dao.close();
                    return "error2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            String alias1 = "";
            String ipAddress1 = "";
            String alias2 = "";
            String ipAddress2 = "";
            String descr1 = "";
            String descr2 = "";
            HostNodeDao hostNodeDao = new HostNodeDao();
            List hostNodeList = null;
            try {
                hostNodeList = hostNodeDao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hostNodeDao.close();
            }
            for (int i = 0; i < hostNodeList.size(); i++) {
                HostNode node = (HostNode) hostNodeList.get(i);
                if (node.getId() == startId) {
                    alias1 = node.getAlias();
                    ipAddress1 = node.getIpAddress();
                }
                if (node.getId() == endId) {
                    alias2 = node.getAlias();
                    ipAddress2 = node.getIpAddress();
                }
            }
            List<Portconfig> portconfigList1 = null;
            PortconfigDao portconfigDao = new PortconfigDao();
            try {
                portconfigList1 = portconfigDao.loadByIpaddress(ipAddress1);
                if (portconfigList1 != null) {
                    for (Portconfig portconfig : portconfigList1) {
                        if (startIndex.trim().equals(String.valueOf(portconfig.getPortindex()))) {
                            descr1 = portconfig.getName();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                portconfigDao.close();
            }
            List<Portconfig> portconfigList2 = null;
            portconfigDao = new PortconfigDao();
            try {
                portconfigList2 = portconfigDao.loadByIpaddress(ipAddress2);
                if (portconfigList2 != null) {
                    for (Portconfig portconfig : portconfigList2) {
                        if (endIndex.trim().equals(String.valueOf(portconfig.getPortindex()))) {
                            descr2 = portconfig.getName();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                portconfigDao.close();
            }
            Link link = new Link();
            link.setLinkName(linkName);// yangjun add
            link.setMaxSpeed(maxSpeed);// yangjun add
            link.setMaxPer(maxPer);// yangjun add
            link.setStartId(startId);
            link.setEndId(endId);
            link.setStartIndex(startIndex);
            link.setEndIndex(endIndex);
            link.setStartIp(ipAddress1);
            link.setEndIp(ipAddress2);
            link.setStartDescr(descr1);
            link.setEndDescr(descr2);
            link.setCableType(cable_type);
            link.setCableCapacity(cable_capacity);
            link.setType(1);
            Link newLink = null;
            try {
                newLink = dao.save(link);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            } finally {
                dao.close();
            }
            if (newLink != null) {
                // 更新所有拓扑图xml
                ManageXmlDao mdao = new ManageXmlDao();
                List<ManageXml> list = mdao.loadAll();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        ManageXml manageXml = list.get(i);
                        XmlOperator xopr = new XmlOperator();
                        xopr.setFile(manageXml.getXmlName());
                        xopr.init4updateXml();
                        if (xopr.isNodeExist(start_Id)
                                && xopr.isNodeExist(end_Id)) {
                            if (newLink.getAssistant() == 0)
                                xopr.addLine(linkName, String.valueOf(newLink
                                        .getId()), start_Id, end_Id);
                            else
                                xopr.addAssistantLine(linkName, String
                                        .valueOf(newLink.getId()), start_Id,
                                        end_Id);
                        }
                        xopr.writeXml();
                    }
                }

                // 链路信息实时更新
                LinkRoad lr = new LinkRoad();
                lr.setId(newLink.getId());
                lr.setLinkName(linkName);// yangjun add
                lr.setMaxSpeed(maxSpeed);// yangjun add
                lr.setMaxPer(maxPer);// yangjun add
                lr.setStartId(startId);
                lr.setStartIp(newLink.getStartIp());
                lr.setStartIndex(startIndex);
                lr.setStartDescr(newLink.getStartDescr());
                lr.setEndIp(newLink.getEndIp());
                lr.setEndId(endId);
                lr.setEndIndex(endIndex);
                lr.setEndDescr(newLink.getEndDescr());
                lr.setAssistant(newLink.getAssistant());
                PollingEngine.getInstance().getLinkList().add(lr);
                returns = newLink.getId() + ":" + newLink.getAssistant();
            }
        }
        return returns;
    }

    // 子图上的保存按钮，保存子图上图元的位置
    private String save() {
        String fileName = (String) session
                .getAttribute(SessionConstant.CURRENT_SUBMAP_VIEW);
        String xmlString = request.getParameter("hidXml");// 从showMap.jsp传入的数据信息字符串

        xmlString = xmlString.replace("<?xml version=\"1.0\"?>",
                "<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        XmlOperator xmlOpr = new XmlOperator();
        xmlOpr.setFile(fileName);
        xmlOpr.saveImage(xmlString);
        saveBusXML(fileName);// 保存业务视图文件
        request.setAttribute("fresh", "fresh");
        return "/topology/submap/save.jsp";
    }

    // 保存业务视图xml文件
    private String saveBusXML(String filename) {
        if (filename != null && !"".equalsIgnoreCase(filename)
                && filename.indexOf("businessmap") != -1) {
            NodeDependDao nodeDependDao = null;
            nodeDependDao = new NodeDependDao();
            List list = nodeDependDao.findByXml(filename);
            ManageXmlOperator mXmlOpr = new ManageXmlOperator();
            mXmlOpr.setFile(filename);
            mXmlOpr.init4updateXml();
            Hashtable hash = mXmlOpr.getAllXY();
            if (hash != null && hash.size() > 0) {
                for (int i = 0; i < hash.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        NodeDepend nvo = (NodeDepend) list.get(j);
                        if (hash.get(nvo.getNodeId()) != null) {
                            nodeDependDao = new NodeDependDao();
                            nodeDependDao.updateById(nvo.getNodeId(),
                                    (String) hash.get(nvo.getNodeId()),
                                    filename);
                            nodeDependDao.close();
                        }
                    }
                }
            }

            ManageXmlDao subMapDao = new ManageXmlDao();
            ManageXml vo = null;
            try {
                vo = (ManageXml) subMapDao.findByXml(filename);
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            } finally {
                subMapDao.close();
            }
            nodeDependDao = new NodeDependDao();
            String xml = filename.replace("jsp", "xml");
            try {
                List lists = nodeDependDao.findByXml(filename);
                ChartXml chartxml;
                chartxml = new ChartXml("NetworkMonitor", "/" + xml);
                chartxml.addBussinessXML(vo.getTopoName(), lists);
                ChartXml chartxmlList;
                chartxmlList = new ChartXml("NetworkMonitor", "/"
                        + xml.replace("businessmap", "list"));
                chartxmlList.addListXML(vo.getTopoName(), lists);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                nodeDependDao.close();
            }
        }
        return "";
    }

    // 重建拓扑图，将子图和关联拓扑图都删除
    // public String reBuildMap() {
    // SysLogger.info("###### 重建拓扑图 ######");
    // DiscoverDataHelper helper = new DiscoverDataHelper();
    // try {
    // helper.DB2NetworkXml();
    // // helper.DB2NetworkVlanXml();
    // helper.DB2ServerXml();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // // ManageXmlDao dao = new ManageXmlDao();
    // // String xmlName[] = dao.deleteAll().split(",");
    // // for (int i = 0; i < xmlName.length; i++) {
    // // XmlOperator xmlOpr = new XmlOperator();
    // // xmlOpr.setFile(xmlName[i]);
    // // xmlOpr.deleteXml();
    // // }
    //
    // //清空关联拓扑图表的数据
    // RelationDao rdao = new RelationDao();
    // rdao.deleteAll();
    //
    // LineDao lineDao = new LineDao();
    // lineDao.deleteByXml("network.jsp");
    //		
    // HintNodeDao hintNodeDao = new HintNodeDao();
    // hintNodeDao.deleteByXml("network.jsp");
    // // 清空节点与图元图片关联表的数据
    // NodeEquipDao nodeEquipDao = new NodeEquipDao();
    // nodeEquipDao.deleteAll();
    // return null;
    // }
    
    
    public String loadLink() {
    	String xmlName = request.getParameter("xmlName");
    	
    	DBManager db = null;
        ResultSet rs = null;
        db = new DBManager();
    	ManageXmlDao manageXmlDao = new ManageXmlDao();
    	try {
    		ManageXml manageXml = (ManageXml) manageXmlDao.findByXml(xmlName);
    		
    		 XmlOperator xopr = new XmlOperator();
             xopr.setFile(manageXml.getXmlName());
             xopr.init4updateXml();
             rs = db.executeQuery("select * from topo_network_link where assistant=0 and linktype != -1 order by id");
             while(rs.next()){
                 if(xopr.isNodeExist("net"+rs.getString("start_id"))&&xopr.isNodeExist("net"+rs.getString("end_id"))&&!xopr.isLinkExist(rs.getString("id"))){
                     xopr.addLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));    
                 }
             }
             rs = db.executeQuery("select * from topo_network_link where assistant=1 and linktype != -1 order by id");
             while(rs.next()){
                 if(xopr.isNodeExist("net"+rs.getString("start_id"))&&xopr.isNodeExist("net"+rs.getString("end_id"))&&!xopr.isAssLinkExist(rs.getString("id"))){
                     xopr.addAssistantLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));
                 }
             }
             xopr.writeXml();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	manageXmlDao.close();
        	db.close();
        }
    	
    	return null;
    }

    public String reBuildMap() {
        DiscoverDataHelper helper = new DiscoverDataHelper();
        try {
            helper.DB2NetworkXml();
            helper.createLinkXml();
            // helper.DB2NetworkVlanXml();
            helper.DB2ServerXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ManageXmlDao dao = new ManageXmlDao();
        // String xmlName[] = dao.deleteAll().split(",");
        // for (int i = 0; i < xmlName.length; i++) {
        // XmlOperator xmlOpr = new XmlOperator();
        // xmlOpr.setFile(xmlName[i]);
        // xmlOpr.deleteXml();
        // }

        // 清空关联拓扑图表的数据
        RelationDao rdao = new RelationDao();
        rdao.deleteAll();

        LineDao lineDao = new LineDao();
        lineDao.deleteByXml("network.jsp");

        HintNodeDao hintNodeDao = new HintNodeDao();
        hintNodeDao.deleteByXml("network.jsp");
        // 清空节点与图元图片关联表的数据
        NodeEquipDao nodeEquipDao = new NodeEquipDao();
        nodeEquipDao.deleteAll();
        return null;
    }

    // 编辑实体链路
    private String readyEditLink() {

        HostNodeDao dao = new HostNodeDao();
        List list = null;
        try {
            list = dao.loadAll();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }
        if (list == null) {
            list = new ArrayList();
        }

        String lineId = getParaValue("lineId");

        LinkDao linkdao = new LinkDao();
        Link link = null;
        try {
            link = (Link) linkdao.findByID(lineId);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            linkdao.close();
        }
        if (link == null) {
            return "/topology/network/editLinkError.jsp";
        }

        int startId = link.getStartId();
        int endId = link.getEndId();
        String startIndex = link.getStartIndex();
        String endIndex = link.getEndIndex();

        String alias_start = "";
        String ipAddress_start = "";
        String alias_end = "";
        String ipAddress_end = "";
        String link_name = link.getLinkName();
        String max_speed = link.getMaxSpeed();
        String max_per = link.getMaxPer();
        String cable_type = link.getCableType();
        String cable_capacity = link.getCableCapacity();
        if (list != null) {
            for (int i = 0; i < list.size() && list != null; i++) {
                HostNode node = (HostNode) list.get(i);
                if (node.getId() == startId) {
                    alias_start = node.getAlias();
                    ipAddress_start = node.getIpAddress();
                }
                if (node.getId() == endId) {
                    alias_end = node.getAlias();
                    ipAddress_end = node.getIpAddress();
                }
            }
        }
        List<Portconfig> portconfigList1 = null;
        PortconfigDao portconfigDao = new PortconfigDao();
        try {
            portconfigList1 = portconfigDao.loadByIpaddress(ipAddress_start);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        List<Portconfig> portconfigList2 = null;
        portconfigDao = new PortconfigDao();
        try {
            portconfigList2 = portconfigDao.loadByIpaddress(ipAddress_end);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        if (portconfigList1 == null) {
            portconfigList1 = new ArrayList<Portconfig>();
        }
        if (portconfigList2 == null) {
            portconfigList2 = new ArrayList<Portconfig>();
        }

        request.setAttribute("start_if", portconfigList1);
        request.setAttribute("end_if", portconfigList2);
        request.setAttribute("alias_start", alias_start);
        request.setAttribute("ipAddress_start", ipAddress_start);
        request.setAttribute("alias_end", alias_end);
        request.setAttribute("ipAddress_end", ipAddress_end);
        request.setAttribute("start_id", new Integer(startId));
        request.setAttribute("end_id", new Integer(endId));
        request.setAttribute("start_index", startIndex);
        request.setAttribute("end_index", endIndex);
        request.setAttribute("link_name", link_name);
        request.setAttribute("max_speed", max_speed);
        request.setAttribute("max_per", max_per);
        request.setAttribute("id", lineId);
        request.setAttribute("cable_type", cable_type);
        request.setAttribute("cable_capacity", cable_capacity);
        return "/topology/submap/editLink.jsp";
    }

    // 删除实体链路
    private String deleteLink() {
        String id = getParaValue("lineId");
        String xml = getParaValue("xml");
        // 更新数据库
        LinkDao dao = new LinkDao();
        dao.delete(id);
        // 更新所有拓扑图
        ManageXmlDao mdao = new ManageXmlDao();
        List<ManageXml> list = mdao.loadAll();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ManageXml manageXml = list.get(i);
                XmlOperator xopr = new XmlOperator();
                xopr.setFile(manageXml.getXmlName());
                xopr.init4updateXml();
                if (xopr.isLinkExist(id)) {
                    xopr.deleteLineByID(id);
                }
                if (xopr.isAssLinkExist(id)) {
                    xopr.deleteAssLineByID(id);
                }
                xopr.writeXml();
            }
        }

        // 更新内存
        PollingEngine.getInstance().deleteLinkByID(Integer.parseInt(id));
        return "/topology/network/change.jsp?customview=" + xml;
    }

    private String editLink() {
        String direction1 = getParaValue("direction1");
        String startIndex = "";
        String endIndex = "";
        int startId = 0;
        int endId = 0;
        String cable_type = getParaValue("cable_type");
        String cable_capacity = getParaValue("cable_capacity");
        if (direction1 != null && direction1.equals("1")) {// 上行设备
            startIndex = getParaValue("end_index");
            endIndex = getParaValue("start_index");
            startId = getParaIntValue("end_id");
            endId = getParaIntValue("start_id");
        } else {// 下行设备
            startIndex = getParaValue("start_index");
            endIndex = getParaValue("end_index");
            startId = getParaIntValue("start_id");
            endId = getParaIntValue("end_id");
        }
        String linkName = getParaValue("link_name");
        String maxSpeed = getParaValue("max_speed");
        String maxPer = getParaValue("max_per");
        String id = getParaValue("id");
        if (startId == endId) {
            setErrorCode(ErrorMessage.DEVICES_SAME);
            return null;
        }

        LinkDao dao = new LinkDao();
        Link formerLink = null;
        try {
            formerLink = (Link) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
//        String formerStartIndex = formerLink.getStartIndex();
//        String formerEndIndex = formerLink.getEndIndex();

//        Host startHost = (Host) PollingEngine.getInstance()
//                .getNodeByID(startId);
//        IfEntity if1 = startHost.getIfEntityByIndex(startIndex);
//        Host endHost = (Host) PollingEngine.getInstance().getNodeByID(endId);
//        IfEntity if2 = endHost.getIfEntityByIndex(endIndex);

//        RepairLink repairLink = null;
//        repairLink = repairdao.loadLink(startHost.getIpAddress(),
//                formerStartIndex, endHost.getIpAddress(), formerEndIndex);

        // formerLink.setStartId(startId);
        HostNodeDao hostNodeDao = new HostNodeDao();
        List hostNodeList = null;
        try {
            hostNodeList = hostNodeDao.loadAll();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            hostNodeDao.close();
        }
        if (hostNodeList == null) {
            hostNodeList = new ArrayList();
        }
        String alias1 = "";
        String ipAddress1 = "";
        String alias2 = "";
        String ipAddress2 = "";
        String descr1 = "";
        String descr2 = "";
        for (int i = 0; i < hostNodeList.size(); i++) {
            HostNode node = (HostNode) hostNodeList.get(i);
            if (node.getId() == startId) {
                alias1 = node.getAlias();
                ipAddress1 = node.getIpAddress();
            }
            if (node.getId() == endId) {
                alias2 = node.getAlias();
                ipAddress2 = node.getIpAddress();
            }
        }
        List<Portconfig> portconfigList1 = null;
        PortconfigDao portconfigDao = new PortconfigDao();
        try {
            portconfigList1 = portconfigDao.loadByIpaddress(ipAddress1);
            if (portconfigList1 != null) {
                for (Portconfig portconfig : portconfigList1) {
                    if (startIndex.trim().equals(String.valueOf(portconfig.getPortindex()))) {
                        descr1 = portconfig.getName();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        List<Portconfig> portconfigList2 = null;
        portconfigDao = new PortconfigDao();
        try {
            portconfigList2 = portconfigDao.loadByIpaddress(ipAddress2);
            if (portconfigList2 != null) {
                for (Portconfig portconfig : portconfigList2) {
                    if (endIndex.trim().equals(String.valueOf(portconfig.getPortindex()))) {
                        descr2 = portconfig.getName();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portconfigDao.close();
        }
        // Link link = new Link();
        formerLink.setLinkName(linkName);// yangjun add
        formerLink.setMaxSpeed(maxSpeed);// yangjun add
        formerLink.setMaxPer(maxPer);// yangjun add
        formerLink.setStartIndex(startIndex);
        formerLink.setEndIndex(endIndex);
        formerLink.setStartDescr(descr1);
        formerLink.setEndDescr(descr2);
        formerLink.setType(1);
        formerLink.setCableType(cable_type);
        formerLink.setCableCapacity(cable_capacity);
        dao = new LinkDao();
        try {
            dao.update(formerLink);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        // 对新修改的连接关系进行原始备份
//        if (repairLink == null) {
//            // 需要再判断该连接关系是否已经被修改过
//            repairLink = repairdao.loadRepairLink(startHost.getIpAddress(),
//                    formerStartIndex, endHost.getIpAddress(), formerEndIndex);
//            if (repairLink == null) {
//                // 说明是第一次修改
//                repairLink = new RepairLink();
//                repairLink.setStartIp(startHost.getIpAddress());
//                repairLink.setStartIndex(formerStartIndex);
//                repairLink.setNewStartIndex(formerLink.getStartIndex());
//                repairLink.setEndIp(endHost.getIpAddress());
//                repairLink.setEndIndex(formerEndIndex);
//                repairLink.setNewEndIndex(formerLink.getEndIndex());
//                repairdao.save(repairLink);
//            } else {
//                // 曾经被修改过
//                repairLink.setNewStartIndex(formerLink.getStartIndex());
//                repairLink.setNewEndIndex(formerLink.getEndIndex());
//                System.out.println("修改连接关系!");
//                repairdao.update(repairLink);
//            }
//        } else {
//            repairLink.setNewStartIndex(formerLink.getStartIndex());
//            repairLink.setNewEndIndex(formerLink.getEndIndex());
//            repairdao.update(repairLink);
//        }

        LinkRoad lr = new LinkRoad();
        lr.setId(formerLink.getId());
        lr.setLinkName(formerLink.getLinkName());
        lr.setLinkName(formerLink.getLinkName());// yangjun add
        lr.setMaxSpeed(formerLink.getMaxSpeed());// yangjun add
        lr.setMaxPer(formerLink.getMaxPer());// yangjun add
        lr.setStartId(formerLink.getStartId());
        lr.setStartIp(formerLink.getStartIp());
        lr.setStartIndex(formerLink.getStartIndex());
        lr.setStartDescr(formerLink.getStartDescr());
        lr.setEndIp(formerLink.getEndIp());
        lr.setEndId(formerLink.getEndId());
        lr.setEndIndex(formerLink.getEndIndex());
        lr.setEndDescr(formerLink.getEndDescr());
        lr.setAssistant(formerLink.getAssistant());
        PollingEngine.getInstance().deleteLinkByID(lr.getId());
        PollingEngine.getInstance().getLinkList().add(lr);

        return null;
    }

    // 示意图元属性
    private String hintProperty() {

        String nodeId = getParaValue("nodeId");
        Node node = PollingEngine.getInstance().getNodeByID(
                Integer.parseInt(nodeId));
        String galleryPanel = "";
        String resTypeName = getParaValue("resTypeName");
        if (resTypeName == null || "".equals(resTypeName)) {
            resTypeName = "路由器";// node.getType();
        } else {
            try {
                resTypeName = new String(resTypeName.getBytes("ISO-8859-1"),
                        "gb2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HintItemDao hintItemDao = new HintItemDao();
        TopoUI topoUI = new TopoUI();
        List<String> list = hintItemDao.getGalleryListing();
        String gallery = topoUI.createGallery(list, -1, 1);

        Map map = hintItemDao.getGallery(resTypeName);
        if (map != null) {
            Object obj = map.get(resTypeName);
            if (obj instanceof List) {
                List iconList = (List) obj;
                galleryPanel = topoUI.getGalleryPanel(iconList);
            }
        }
        hintItemDao.close();
        request.setAttribute("resTypeName", resTypeName);
        if (node != null) {
            request.setAttribute("equipName", node.getAlias());
        } else {
            request.setAttribute("equipName", "");
        }
        request.setAttribute("galleryPanel", galleryPanel);
        request.setAttribute("gallery", gallery);
        request.setAttribute("nodeId", nodeId);

        return "/topology/submap/editEquip.jsp";

    }

    // 替换示意设备图片
    private String replacePic() {

        String iconPath[] = {};
        String nodeId = getParaValue("nodeId");
        String fileName = (String) getParaValue("xml");
        String returnValue[] = getParaArrayValue("returnValue");
        String equipName = "";

        iconPath = returnValue[0].split(",");
        if (iconPath.length == 2) {
            try {
                equipName = new String(iconPath[0].getBytes("ISO-8859-1"),
                        "gb2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ManageXmlOperator mXmlOpr = new ManageXmlOperator();
            mXmlOpr.setFile(fileName);
            mXmlOpr.init4updateXml();
            if (mXmlOpr.isNodeExist(nodeId)) {
                if (iconPath[1] != null && !"".equals(iconPath[1])) {
                    mXmlOpr
                            .updateNode(nodeId, "img", iconPath[1]
                                    .substring(17));
                }
                mXmlOpr.updateNode(nodeId, "alias", equipName);
            }
            mXmlOpr.writeXml();
        }
        return null;
    }

    // 实体图元属性
    private String equipProperty() {
        /*
         * 1.根据节点id获得所有该节点设备信息 2.根据节点信息加载对应的设备图片列
         */
        String nodeid = getParaValue("nodeId");
        String nodeId = nodeid.substring(3);
        String category = getParaValue("category");
        String type = getParaValue("type");

        Node node = PollingEngine.getInstance().getNodeByCategory(type,
                Integer.parseInt(nodeId));

        if (category == null || "".equals(category)) {
            category = node.getCategory() + "";
        }
        String galleryPanel = "";

        EquipImageDao equipImageDao = new EquipImageDao();
        List<String> list = equipImageDao.getGalleryListing();

        TopoUI topoUI = new TopoUI();
        String gallery = topoUI.createGallery(list);

        Map map = equipImageDao.getGallery(Integer.parseInt(category));
        if (map != null) {
            Object obj = map.get(Integer.parseInt(category));
            if (obj instanceof List) {
                List iconList = (List) obj;
                galleryPanel = topoUI.getEquipGalleryPanel(iconList);
            }
        }
        equipImageDao.close();
        request.setAttribute("equipName", node.getAlias());
        request.setAttribute("category", category);
        request.setAttribute("galleryPanel", galleryPanel);
        request.setAttribute("gallery", gallery);
        request.setAttribute("nodeId", nodeid);
        request.setAttribute("type", type);

        return "/topology/network/editEquip.jsp";

    }

    // 替换实体设备图元图片
    private String replaceEquipPic() {
        /*
         * 1.修改设备别名 2.建立设备节点与设备图元表对应的关系 3.修改对应的视图xml文件
         */
        String fileName = "";
        String equipName = "";
        String imageId = "";
        String equipType = "";
        String nodeid = getParaValue("nodeId");
        String nodeId = nodeid.substring(3);
        String returnValue[] = getParaArrayValue("returnValue");
        String arr[] = returnValue[0].split(",");
        if (arr.length == 4) {
            try {
                equipName = new String(arr[0].getBytes("ISO-8859-1"), "gb2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            imageId = arr[1];
            fileName = arr[2];
            equipType = arr[3];
        }
        String imgPath = "";
        if (!"".equals(imageId)) {
            EquipImageDao equipImageDao = new EquipImageDao();
            EquipImage equipImage = (EquipImage) equipImageDao
                    .findImageById(Integer.parseInt(imageId));
            imgPath = equipImage.getPath();
            equipImageDao.close();
        }
        if (!"".equals(equipName) && !"".equals(nodeId) && !"".equals(fileName)
                && !"".equals(equipType)) {
            TreeNodeDao treeNodeDao = new TreeNodeDao();
            TreeNode vo = (TreeNode) treeNodeDao.findByName(equipType);
            // 更新数据库
            if (vo != null && vo.getTableName() != null
                    && !"".equals(vo.getTableName())) {
                CommonDao dao = new CommonDao(vo.getTableName());
                dao.updateAliasById(equipName, nodeId);
            }
            // 更新内存
            Node node = PollingEngine.getInstance().getNodeByCategory(
                    equipType, Integer.parseInt(nodeId));
            if (node != null) {// 内存中有，更新内存
                node.setAlias(equipName);
                node.setManaged(true);
            } else {
                SysLogger.error("update a null node=" + nodeid);
                return "";
            }

            // 2
            if (!"".equals(imageId)) {
                NodeEquipDao nodeEquipDao = new NodeEquipDao();
                NodeEquip Vo = (NodeEquip) nodeEquipDao.findByNodeAndXml(
                        nodeid, fileName);
                NodeEquip VO = new NodeEquip();
                VO.setEquipId(Integer.parseInt(imageId));
                VO.setNodeId(nodeid);
                VO.setXmlName(fileName);
                if (Vo == null) {
                    save(nodeEquipDao, VO);
                } else {
                    VO.setId(Vo.getId());
                    update(nodeEquipDao, VO);
                }

            }

            // 3
            ManageXmlOperator mXmlOpr = new ManageXmlOperator();
            mXmlOpr.setFile(fileName);
            mXmlOpr.init4updateXml();
            if (mXmlOpr.isNodeExist(nodeid)) {
                if (!"".equals(imgPath)) {
                    mXmlOpr.updateNode(nodeid, "img", imgPath.substring(17));
                }
                mXmlOpr.updateNode(nodeid, "alias", equipName);
            }
            mXmlOpr.writeXml();
        }

        return null;
    }

    // 分类展现设备树
    private String showTree() {
        String treeFlag = getParaValue("treeFlag");
        String equiptype = getParaValue("equiptype");
        String typeStr = "";
        TreeNodeDao treeNodeDao = new TreeNodeDao();
        TreeNode vo = (TreeNode) treeNodeDao.findByName(equiptype);
        List nodeList = PollingEngine.getInstance().getAllTypeMap().get(
                equiptype);
        List<Node> list = new ArrayList<Node>();
        if (nodeList != null && nodeList.size() > 0) {
            if (equiptype.indexOf("net") == 0) {// 网络设备的情况下
                if (vo != null && vo.getCategory() != null
                        && !"".equals(vo.getCategory())) {
                    String cate[] = vo.getCategory().split(",");
                    if (cate.length > 0) {
                        for (int i = 0; i < nodeList.size(); i++) {
                            Node node = (Node) nodeList.get(i);
                            for (int j = 0; j < cate.length; j++) {
                                if (node.getCategory() == Integer
                                        .parseInt(cate[j])) {
                                    list.add(node);
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < nodeList.size(); i++) {
                    Node node = (Node) nodeList.get(i);
                    list.add(node);
                }
            }
        }
        if (vo != null) {
            typeStr = vo.getText();
        }
        request.setAttribute("equiptype", equiptype);
        request.setAttribute("typeStr", typeStr);
        request.setAttribute("nodeList", list);
        return "/topology/network/tree.jsp?treeflag=" + treeFlag;

    }

    // 准备编辑根图属性
    private String readyEditMap() {
        ManageXmlDao dao = new ManageXmlDao();
        ManageXml vo = (ManageXml) dao.findByXml("network.jsp");
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return "/topology/submap/editSubMap.jsp";
    }

    // 链路样式
    private String linkProperty() {
        String lineId = getParaValue("lineId");
        request.setAttribute("lineId", lineId);

        return "/topology/submap/editLine.jsp";
    }

    // 保存链路样式
    private String saveLinkProperty() {

        String lineId = getParaValue("lineId");
        String fileName = getParaValue("xml");
        String link_name = getParaValue("link_name");
        String link_width = getParaValue("link_width");

        ManageXmlOperator mXmlOpr = new ManageXmlOperator();
        mXmlOpr.setFile(fileName);
        mXmlOpr.init4updateXml();
        if (mXmlOpr.isLinkExist(lineId)) {
            mXmlOpr.updateLine(lineId, "lineWidth", link_width);
        } else if (mXmlOpr.isAssLinkExist(lineId)) {
            mXmlOpr.updateAssLine(lineId, "lineWidth", link_width);
        } else if (mXmlOpr.isDemoLinkExist(lineId)) {
            mXmlOpr.updateDemoLine(lineId, "lineWidth", link_width);
            if (!"".equals(link_name)) {
                mXmlOpr.updateDemoLine(lineId, "lineInfo", link_name);
            }
        } else {
            SysLogger.error("SubMapManager.saveLinkProperty:" + "拓扑图没有该链路");
        }
        mXmlOpr.writeXml();
        return null;
    }

    // 设备面板图展示
    private String showPanel() {
        String target = "";
        String ip = getParaValue("ip");
        // 更新设备面板图 HONGLI ADD
        UpdateXmlTaskTest updateXmlTaskTest = UpdateXmlTaskTest.getInstance();
        updateXmlTaskTest.updatePanel(ip);
        // 更新设备面板图 END
        IpaddressPanelDao ipaddressPanelDao = new IpaddressPanelDao();
        IpaddressPanel panel = ipaddressPanelDao.loadIpaddressPanel(ip);
        Host host = (Host) PollingEngine.getInstance().getNodeByIP(ip);
        if (panel == null) {
            target = "/panel/view/blank.jsp";
        } else {
            String filename = SysUtil.doip(ip);
            String oid = host.getSysOid();
            // oid = oid.replaceAll("\\.","-");
            // oid = oid+"_"+panel.getImageType();
            target = "/panel/view/custom.jsp?filename=" + filename + "&oid="
                    + oid + "&imageType=" + panel.getImageType();
        }
        return target;
    }

    // 将主机关联的应用、中间件添加到拓扑图上
    private String addApplications() {

        String xmlName = getParaValue("xml");
        String nodeid = getParaValue("node");
        String ip = getParaValue("ip");

        Node node_db = PollingEngine.getInstance().getDbByIP(ip);
        if (node_db != null) {
            addApp(xmlName, node_db, nodeid, "dbs" + node_db.getId());
        }
        Node node_cics = PollingEngine.getInstance().getCicsByIP(ip);
        if (node_cics != null) {
            addApp(xmlName, node_cics, nodeid, "cic" + node_cics.getId());
        }
        Node node_domino = PollingEngine.getInstance().getDominoByIP(ip);
        if (node_domino != null) {
            addApp(xmlName, node_domino, nodeid, "dom" + node_domino.getId());
        }
        Node node_ftp = PollingEngine.getInstance().getFtpByIP(ip);
        if (node_ftp != null) {
            addApp(xmlName, node_ftp, nodeid, "ftp" + node_ftp.getId());
        }
        Node node_iis = PollingEngine.getInstance().getIisByIP(ip);
        if (node_iis != null) {
            addApp(xmlName, node_iis, nodeid, "iis" + node_iis.getId());
        }
        Node node_mail = PollingEngine.getInstance().getMailByIP(ip);
        if (node_mail != null) {
            addApp(xmlName, node_mail, nodeid, "mai" + node_mail.getId());
        }
        Node node_mq = PollingEngine.getInstance().getMqByIP(ip);
        if (node_mq != null) {
            addApp(xmlName, node_mq, nodeid, "mqs" + node_mq.getId());
        }
        Node node_tomcat = PollingEngine.getInstance().getTomcatByIP(ip);
        if (node_tomcat != null) {
            addApp(xmlName, node_tomcat, nodeid, "tom" + node_tomcat.getId());
        }
        Node node_was = PollingEngine.getInstance().getWasByIP(ip);
        if (node_was != null) {
            addApp(xmlName, node_was, nodeid, "was" + node_was.getId());
        }
        Node node_web = PollingEngine.getInstance().getWebByIP(ip);
        if (node_web != null) {
            addApp(xmlName, node_web, nodeid, "wes" + node_web.getId());
        }
        Node node_weblogic = PollingEngine.getInstance().getWeblogicByIP(ip);
        if (node_weblogic != null) {
            addApp(xmlName, node_weblogic, nodeid, "web"
                    + node_weblogic.getId());
        }
        request.setAttribute("fresh", "fresh");
        return "/topology/network/save.jsp";

    }

    public void addApp(String xmlName, Node node, String id1, String id2) {
        XmlOperator xmlOperator = new XmlOperator();
        xmlOperator.setFile(xmlName);
        // 保存节点信息
        xmlOperator.init4updateXml();
        String eleImage = null;
        int index = 1;
        boolean bool = false;
        eleImage = NodeHelper.getTopoImage(node.getCategory());
        if (!xmlOperator.isNodeExist(id2)) {
            xmlOperator.addNode(id2, node.getCategory(), eleImage, node
                    .getIpAddress(), node.getAlias(), String
                    .valueOf(index * 30), "15");
            bool = true;
        }
        xmlOperator.writeXml();
        // 保存示意链路
        ManageXmlOperator mxmlOpr = new ManageXmlOperator();
        mxmlOpr.setFile(xmlName);
        mxmlOpr.init4updateXml();
        int lineId = mxmlOpr.findMaxDemoLineId();
        if (bool) {
            mxmlOpr.addLine("hl" + lineId, id1, id2);
        }
        mxmlOpr.writeXml();
    }

    public String execute(String action) {
        if (action.equals("createSubMap")) {// yangjun add
            String objEntityStr = getParaValue("objEntityStr");
            String linkStr = getParaValue("linkStr");
            String asslinkStr = getParaValue("asslinkStr");
            request.setAttribute("objEntityStr", objEntityStr);
            request.setAttribute("linkStr", linkStr);
            request.setAttribute("asslinkStr", asslinkStr);
            return "/topology/submap/createSubMap.jsp";
        }
        if (action.equals("relationList")) {
            String fileName = (String) session.getAttribute("fatherXML");
			ManageXmlDao dao = new ManageXmlDao();
			List list = null;
			String sql = " where 1=1 ";
			String bidSql = getBidSql("bid");
			if (bidSql != null && bidSql.trim().length() > 0) {
				sql += bidSql;
            }
			sql = sql+ " and xml_name!='"+ fileName +"'";
			try {
				list = dao.findByCondition(sql);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			String nodeId = getParaValue("nodeId");
			String category = getParaValue("category");
			RelationDao rdao = new RelationDao();
			Relation vo1 = null;
			try {
				vo1 = (Relation) rdao.findByNodeId(nodeId, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				rdao.close();
			}
			if (vo1 != null) {
			    request.setAttribute("mapId", vo1.getMapId());
			} else {
			    request.setAttribute("mapId", "-1");
			}
            request.setAttribute("list", list);
            request.setAttribute("category", category);
            request.setAttribute("nodeId", nodeId);
            return "/topology/submap/relation.jsp";
        }
        if (action.equals("save"))
            return save();
        if (action.equals("save_relation_node"))
            return relationMap();
        if (action.equals("cancel_relation_node"))
            return cancelRelation();
        if (action.equals("readyAddLink"))
            return readyAddLink();
        // if (action.equals("addLink"))
        // return addLink();
        if (action.equals("deleteLink"))
            return deleteLink();
        if (action.equals("editLink"))
            return editLink();
        if (action.equals("readyEditLink"))
            return readyEditLink();
        if (action.equals("addLines"))
            return addLines();
        if (action.equals("deleteLines"))
            return deleteLines();
        if (action.equals("deleteSubMap"))
            return deleteSubMap();
        if (action.equals("saveSubMap"))
            return saveSubMap();
        if (action.equals("readyEditSubMap"))
            return readyEditSubMap();
        if (action.equals("editSubMap"))
            return editSubMap();
        if (action.equals("reBuild"))
            return reBuildMap();
        if (action.equals("deleteEquipFromSubMap"))
            return deleteEquipFromSubMap();
        if (action.equals("removeEquipFromSubMap"))
            return removeEquipFromSubMap();
        if (action.equals("hintProperty"))
            return hintProperty();
        if (action.equals("replacePic"))
            return replacePic();
        if (action.equals("equipProperty"))
            return equipProperty();
        if (action.equals("replaceEquipPic"))
            return replaceEquipPic();
        if (action.equals("showTree"))
            return showTree();
        if (action.equals("readyAddHintMeta"))
            return readyAddHintMeta();
        if (action.equals("deleteHintMeta"))
            return deleteHintMeta();
        if (action.equals("readyEditMap"))
            return readyEditMap();
        if (action.equals("linkProperty"))
            return linkProperty();
        if (action.equals("saveLinkProperty"))
            return saveLinkProperty();
        if (action.equals("addApplications"))
            return addApplications();
        if (action.equals("showpanel"))
            return showPanel();
        if (action.equals("loadLink")) {
        	return loadLink();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

}
