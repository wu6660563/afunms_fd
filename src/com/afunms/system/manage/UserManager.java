/**
 * <p>Description:UserManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.manage;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.axis.CompassFormat;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import wfm.encode.MD5;

import com.afunms.alarm.dao.AlarmWayDetailDao;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.application.dao.ApacheConfigDao;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.DominoConfigDao;
import com.afunms.application.dao.EmailConfigDao;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.GrapesConfigDao;
import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.dao.JBossConfigDao;
import com.afunms.application.dao.MQConfigDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.dao.PSTypeDao;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.dao.WasConfigDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.model.ApacheConfig;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.DominoConfig;
import com.afunms.application.model.EmailMonitorConfig;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.GrapesConfig;
import com.afunms.application.model.IISConfig;
import com.afunms.application.model.JBossConfig;
import com.afunms.application.model.MQConfig;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.model.PSTypeVo;
import com.afunms.application.model.Tomcat;
import com.afunms.application.model.WasConfig;
import com.afunms.application.model.WebConfig;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.FlexDataXml;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.home.role.dao.HomeRoleDao;
import com.afunms.home.role.model.HomeRoleModel;
import com.afunms.home.user.dao.HomeUserDao;
import com.afunms.home.user.model.HomeUserModel;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Avgcollectdata;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.system.dao.DepartmentDao;
import com.afunms.system.dao.PositionDao;
import com.afunms.system.dao.RoleDao;
import com.afunms.system.dao.SysLogDao;
import com.afunms.system.dao.UserAuditDao;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.SysLog;
import com.afunms.system.model.User;
import com.afunms.system.service.OperationPermissionService;
import com.afunms.system.service.RoleOperationPermissionService;
import com.afunms.system.vo.EventVo;
import com.afunms.system.vo.FlexVo;
import com.afunms.system.vo.Vos;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.MonitorHostDTO;
import com.afunms.topology.model.MonitorNetDTO;
import com.afunms.topology.model.MonitorNodeDTO;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.util.NodeHelper;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

public class UserManager extends BaseManager implements ManagerInterface {
    
	// ����Ա������̨�˻� ����������session��Ч��Ϊ������Ч
	private static final String ADMIN_USER = "admin";
	private static final String FUWUTAI_USER = "fuwutai";
	
	// ϵͳHttpSession ��Ч��Ϊ600s(10����)
	private static final int HTTPSESSION_INACTIVE_INTERVAL = 600;
	
	private FlexSession flexSession = FlexContext.getFlexSession();

    private String save() {
        User vo = new User();
        vo.setName(getParaValue("name"));
        vo.setUserid(getParaValue("userid"));
        vo.setSex(getParaIntValue("sex"));
        vo.setDept(getParaIntValue("dept"));
        vo.setPosition(getParaIntValue("position"));
        vo.setRole(getParaIntValue("role"));
        vo.setPhone(getParaValue("phone"));
        vo.setMobile(getParaValue("mobile"));
        vo.setEmail(getParaValue("email"));
        vo.setBusinessids(getParaValue("bid"));
        String bid = getParaValue("bid");
        if (bid == null || bid.trim().length() == 0
                || "null".equals(bid.trim())) {
            bid = ",-1,";
        }
        vo.setBusinessids(bid);
        // String[] ids = getParaArrayValue("checkbox");
        // if (ids != null && ids.length > 0) {
        // String ids_str = ",";
        // if (ids.length == 1) {
        // vo.setBusinessids("," + ids[0] + ",");
        // } else {
        // for (int i = 0; i < ids.length; i++) {
        // ids_str = ids_str + ids[i] + ",";
        // }
        // vo.setBusinessids(ids_str);
        // }
        // }

        MD5 md = new MD5();

        vo.setPassword(md.getMD5ofStr(getParaValue("password")));
        UserDao dao = new UserDao();
        int result = dao.save(vo);

        String target = null;
        if (result == 0) {
            target = null;
            setErrorCode(ErrorMessage.USER_EXIST);
        } else if (result == 1)
            target = "/user.do?action=list";
        else
            target = null;
        return target;
    }

    private String update() {
        User vo = new User();
        vo.setId(getParaIntValue("id"));
        vo.setName(getParaValue("name"));
        vo.setId(getParaIntValue("id"));
        vo.setUserid(getParaValue("userid"));
        vo.setSex(getParaIntValue("sex"));
        vo.setDept(getParaIntValue("dept"));
        vo.setPosition(getParaIntValue("position"));
        vo.setRole(getParaIntValue("role"));
        vo.setPhone(getParaValue("phone"));
        vo.setMobile(getParaValue("mobile"));
        vo.setEmail(getParaValue("email"));
        String bid = getParaValue("bid");
        if (bid == null || bid.trim().length() == 0
                || "null".equals(bid.trim())) {
            bid = ",-1,";
        }
        vo.setBusinessids(bid);

        // vo.setBusinessids(getParaValue("businessids"));
        // String[] ids = getParaArrayValue("checkbox");
        // if (ids != null && ids.length > 0) {
        // String ids_str = ",";
        // if (ids.length == 1) {
        // vo.setBusinessids("," + ids[0] + ",");
        // } else {
        // for (int i = 0; i < ids.length; i++) {
        // ids_str = ids_str + ids[i] + ",";
        // }
        // vo.setBusinessids(ids_str);
        // }
        // }

        String pwd = getParaValue("password");
        if (!pwd.equals("")) {
            MD5 md = new MD5();
            vo.setPassword(md.getMD5ofStr(pwd));
        } else
            vo.setPassword(null);

        UserDao dao = new UserDao();
        String target = null;
        if (dao.update(vo))
            target = "/user.do?action=list";
        return target;
    }

    private String readyAdd() {
        if ((new RoleDao()).loadAll().size() == 0) {
            setErrorCode(ErrorMessage.NO_ROLE);
            return null;
        }
        if ((new PositionDao()).loadAll().size() == 0) {
            setErrorCode(ErrorMessage.NO_POSITION);
            return null;
        }
        if ((new DepartmentDao()).loadAll().size() == 0) {
            setErrorCode(ErrorMessage.NO_DEPARTMENT);
            return null;
        }
        return "/system/user/add.jsp";
    }

    private String ready_edit() {

        String targetJsp = "/system/user/edit.jsp";
        BaseVo vo = null;
        UserDao dao = new UserDao();
        try {
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return targetJsp;
    }

    private String read() {
        String targetJsp = "/system/manage/read.jsp";
        BaseVo vo = null;
        UserDao dao = new UserDao();
        try {
            vo = dao.findByID(getParaValue("id"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo != null) {
            request.setAttribute("vo", vo);
        }
        return targetJsp;
    }

    /**
     * �û���¼
     */
    private String login() {
        if (getParaValue("password") == null) {
            setErrorCode(ErrorMessage.INCORRECT_PASSWORD);
            return null;
        }
        int flag = 0;
//         try {
//         // �ж���֤��
//         LicenseUtil.checkLicense();
//         } catch (Exception e) {
//         // e.printStackTrace();
//            
//         SysLogger.info(e.getMessage());
//         SysLogger.info("LICENSE��֤ʧ��,���볧����ϵ...");
//         flag = 1;
//         }
        // ����֤��ʧ��,����ʾ����ҳ��
        if (flag == 1)
            return "/limited.jsp";

        MD5 md = new MD5();
        String pwd = md.getMD5ofStr(getParaValue("password"));
        UserDao dao = new UserDao();
        User vo = null;
        try {
            vo = dao.findByLogin(getParaValue("userid"), pwd);
        } catch (Exception e) {

        } finally {
            dao.close();
        }
        if (vo == null) // �û��������벻��ȷ
        {
            setErrorCode(ErrorMessage.INCORRECT_PASSWORD);
            return null;
        }

        session.setAttribute(SessionConstant.CURRENT_USER, vo); // �û�����
        
        // ����session��Ч�� add by yag 2012-08-22
        if(FUWUTAI_USER.equals(vo.getUserid().trim())
        		|| ADMIN_USER.equals(vo.getUserid().trim())){
        	session.setMaxInactiveInterval(-1);
        	
        } else {
        	session.setMaxInactiveInterval(HTTPSESSION_INACTIVE_INTERVAL);	// Session��Ч��Ϊ10����
        }
        
        //if (!"127.0.0.1".equals(request.getRemoteAddr())) {
            SysLog slvo = new SysLog();
            slvo.setUserid(vo.getId());
            slvo.setUser(vo.getName());
            slvo.setEvent("��¼ϵͳ");
            slvo.setLogTime(SysUtil.getCurrentTime());
            slvo.setIp(request.getRemoteAddr());
            if(findUserInAlarmWay(vo.getId())){
            	slvo.setType(1);	// �Ӹ澯��ʽ���ҵ�����Ա�����ĵ�¼��¼���ͱ��Ϊ1��
            						// �����ı��Ϊ2
            } else {
            	slvo.setType(2);
            }
            		
            SysLogDao sldao = new SysLogDao();
            try {
                sldao.save(slvo);
            } catch (Exception e) {

            } finally {
                sldao.close();
            }
            
        //}
        
        return "/common/index.jsp";
    }

    /**
     * �����Ƿ�������û����͸澯��Ϣ��澯����
     * 
     * findUserInAlarmWay:
     * <p>
     *
     * @param id
     * @return
     *
     * @since   v1.01
     */
    public boolean findUserInAlarmWay(int id) {
    	AlarmWayDetailDao wayDao = new AlarmWayDetailDao();
    	List<AlarmWayDetail> list = null;
    	try{
    		String condition = " where find_in_set('" + id +"', user_ids)";
    		list = (List<AlarmWayDetail>)wayDao.findByCondition(condition);
    	}catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		wayDao.close();
    	}
    	
    	if(null != list && list.size() > 0){
    		return true;
    	}
    	return false;
    }
    
    /**
     * ���߲�ѯ���ݿ��ж��Ƿ���Ҫ��ҳԪ�ؼ���
     * <p>
     * </P>
     * <p>
     * </P>
     * 
     * @return
     * @author makewen
     * @date Jun 17, 2011
     */
    private void homeModuleSet() {
        String str = "";
        // ��ѯ�û���ҳģ������ ���û����ôֱ����ת���޸�ҳ��
        User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        

        String sqlCondition = " where user_id='" + uservo.getUserid() + "'";
        HomeUserDao homeUserDao = new HomeUserDao();
        List homeUserList = null;
        try {
            homeUserList = homeUserDao.findByCondition(sqlCondition);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            homeUserDao.close();
        }

        Hashtable smallHashtable = new Hashtable();
        Hashtable bigHashtable = new Hashtable();
        if (homeUserList == null || homeUserList.size() == 0) {
            HomeRoleDao homeRoleDao = new HomeRoleDao();
            List homeRoleList = null;
            try {
                homeRoleList = homeRoleDao.findByCondition(" where role_id='"
                        + uservo.getRole() + "'");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                homeRoleDao.close();
            }
            if (homeRoleList != null) {
                for (int i = 0; i < homeRoleList.size(); i++) {
                    HomeRoleModel homeRoleModel = (HomeRoleModel) homeRoleList
                            .get(i);
                    if (homeRoleModel.getType() == 0) {
                        smallHashtable.put(homeRoleModel.getChName(), homeRoleModel
                                .getVisible());
                    }
                    if (homeRoleModel.getType() == 1) {
                        bigHashtable.put(homeRoleModel.getChName(), homeRoleModel
                                .getVisible());
                    }
                }
            }
        } else {
            for (int i = 0; i < homeUserList.size(); i++) {
                HomeUserModel homeUserModel = (HomeUserModel) homeUserList
                        .get(i);
                if (homeUserModel.getType() == 0) {
                    smallHashtable.put(homeUserModel.getChName(), homeUserModel
                            .getVisible());
                }
                if (homeUserModel.getType() == 1) {
                    bigHashtable.put(homeUserModel.getChName(), homeUserModel
                            .getVisible());
                }
            }
        }
        request.setAttribute("smallHashtable", smallHashtable);
        request.setAttribute("bigHashtable", bigHashtable);
    }

    private String home() {
        this.getHome();
        Date date4 = new Date();
        this.homeModuleSet();
        Date date5 = new Date();
        System.out.println("date5 - date4 = " + (date5.getTime() - date4.getTime()));
        return "/common/home_topo.jsp";
    }

    private String personhome() {
        this.getPersonHome();
        return "/common/personhome.jsp";
    }

    private void getHome() {
        User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }

        // �豸����
        List routeList = new ArrayList(); // ·�����б�
        List switchList = new ArrayList(); // �������б�
        List hostList = new ArrayList(); // �������б�
        List DBList = new ArrayList(); // ���ݿ��б�
        List secureList = new ArrayList(); // ��ȫ�б�

        int routeSize = 0; // ·��������
        int switchSize = 0; // ����������
        int hostSize = 0; // ����������
        int DBSize = 0; // ���ݿ�����
        int middleSize = 0; // �м������
        int serviceSize = 0; // ��������
        int secureSize = 0; // ��ȫ����
        
        int routeMaxAlarmLevel = 0; // ·������߸澯�ȼ�
        int switchMaxAlarmLevel = 0; // ��������߸澯�ȼ�
        int hostMaxAlarmLevel = 0; // ��������߸澯�ȼ�
        int DBMaxAlarmLevel = 0; // ���ݿ���߸澯�ȼ�
        int middleMaxAlarmLevel = 0; // �м����߸澯�ȼ�
        int serviceMaxAlarmLevel = 0; // ������߸澯�ȼ�
        int secureMaxAlarmLevel = 0; // ��ȫ��߸澯�ȼ�
        
        String routeSnapStatusImage = ""; // ·����ϵͳ����״̬ͼƬ
        String switchSnapStatusImage = ""; // ������ϵͳ����״̬ͼƬ
        String hostSnapStatusImage = ""; // ������ϵͳ����״̬ͼƬ
        String DBSnapStatusImage = ""; // ���ݿ�ϵͳ����״̬ͼƬ
        String middleSnapStatusImage = ""; // �м��ϵͳ����״̬ͼƬ
        String serviceSnapStatusImage = ""; // ����ϵͳ����״̬ͼƬ
        String secureSnapStatusImage = ""; // ��ȫϵͳ����״̬ͼƬ

//        HostNodeDao hostNodeDao = new HostNodeDao();
//        try {
//            routeList = hostNodeDao.loadNetworkByBidAndCategory(1, bids);
//            switchList = hostNodeDao.loadNetworkByBidAndCategory(2, bids);
//            // hostList = hostNodeDao.loadNetworkByBid(4, bids);
//            secureList = hostNodeDao.loadNetworkByBid(8, bids);
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        } finally {
//            hostNodeDao.close();
//        }
//
//        try {
//            DBTypeVo oracleType = null;
//            DBTypeDao DBTypeDao = new DBTypeDao();
//            try {
//                oracleType = (DBTypeVo) DBTypeDao.findByDbtype("Oracle");
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                DBTypeDao.close();
//            }
//            DBDao DBDao = new DBDao();
//            try {
//                DBList = DBDao.getDbByBID(bids);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            } finally {
//                DBDao.close();
//            }
//            List oracleList = new ArrayList();
//            List otherDBList = new ArrayList();
//            for (Object object : DBList) {
//                DBVo dbvo = (DBVo) object;
//                if (dbvo.getDbtype() == oracleType.getId()) {
//                    oracleList.add(dbvo);
//                } else {
//                    otherDBList.add(dbvo);
//                }
//            }
//            for (int i = 0; i < oracleList.size(); i++) {
//                DBVo dbvo = (DBVo) oracleList.get(i);
//                List oracles = new ArrayList();
//                OraclePartsDao odao = new OraclePartsDao();
//                try {
//                    oracles = odao.findOracleParts(dbvo.getId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    odao.close();
//                }
//                for (int j = 0; j < oracles.size(); j++) {
//                    OracleEntity ora = (OracleEntity) oracles.get(j);
//                    DBVo nvo = new DBVo();
//                    nvo.setAlias(ora.getAlias());
//                    nvo.setBid(ora.getBid());
//                    nvo.setCategory(dbvo.getCategory());
//                    nvo.setCollecttype(ora.getCollectType());
//                    nvo.setDbName(ora.getSid());
//                    nvo.setDbtype(dbvo.getDbtype());
//                    nvo.setId(ora.getId());
//                    nvo.setIpAddress(dbvo.getIpAddress());
//                    nvo.setManaged(dbvo.getManaged());
//                    nvo.setPassword(ora.getPassword());
//                    nvo.setPort(dbvo.getPort());
//                    nvo.setSendemail(dbvo.getSendemail());
//                    nvo.setUser(ora.getUser());
//                    otherDBList.add(nvo);
//                }
//            }
//            DBList = otherDBList;
//        } catch (Exception e) {
//
//        }
//
//        NodeUtil nodeUtil = new NodeUtil();
//        nodeUtil.setBid(bids);
//        
//        routeList = nodeUtil.conversionToNodeDTO(routeList);
//        switchList = nodeUtil.conversionToNodeDTO(switchList);
//        hostList = nodeUtil.conversionToNodeDTO(nodeUtil.getHostList());
//        secureList = nodeUtil.conversionToNodeDTO(secureList);
//        DBList = nodeUtil.conversionToNodeDTO(DBList);
//        List<NodeDTO> middlewareList = nodeUtil.conversionToNodeDTO(nodeUtil.getMiddlewareList());
//        List<NodeDTO> serviceList = nodeUtil.conversionToNodeDTO(nodeUtil.getServiceList());
//        
//        NodeAlarmService nodeAlarmService = new NodeAlarmService();
//        if (routeList != null) {
//            routeSize = routeList.size();
//            routeMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(routeList);
//        }
//        if (switchList != null) {
//            switchSize = switchList.size();
//            switchMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(switchList);
//        }
//        if (hostList != null) {
//            hostSize = hostList.size();
//            hostMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(hostList);
//        }
//        if (DBList != null) {
//            DBSize = DBList.size();
//            DBMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(DBList);
//        }
//        if (middlewareList != null) {
//            middleSize = middlewareList.size();
//            middleMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(middlewareList);
//        }
//        if (serviceList != null) {
//            serviceSize = serviceList.size();
//            serviceMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(serviceList);
//        }
//        if (secureList != null) {
//            secureSize = secureList.size();
//            secureMaxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(secureList);
//        }
//
        Date date = new Date();
        // ��ȡ����ͼƬ
        routeSnapStatusImage = NodeHelper.getSnapStatusImage(routeMaxAlarmLevel, 1);
        switchSnapStatusImage = NodeHelper.getSnapStatusImage(switchMaxAlarmLevel, 2);
        hostSnapStatusImage = NodeHelper.getSnapStatusImage(hostMaxAlarmLevel, 3);
        DBSnapStatusImage = NodeHelper.getSnapStatusImage(DBMaxAlarmLevel, 4);
        middleSnapStatusImage = NodeHelper.getSnapStatusImage(middleMaxAlarmLevel, 5);
        serviceSnapStatusImage = NodeHelper.getSnapStatusImage(serviceMaxAlarmLevel, 6);
        secureSnapStatusImage = NodeHelper.getSnapStatusImage(secureMaxAlarmLevel, 7);

        Date date1 = new Date();
        // �ؼ�ҵ��
//        NetworkMonitor networkMonitor = new NetworkMonitor();
//        Hashtable bussinessviewHash = networkMonitor.getBussinessviewHash(vo);
        Hashtable bussinessviewHash = new Hashtable();

        Date date2 = new Date();
        // ��������ͼ
        ManageXmlDao dao = new ManageXmlDao();
        ManageXml manageXml = null;
        try {
            manageXml = (ManageXml) dao.findByView("1", bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        String home_topo_view = "network.jsp";
        if (manageXml != null) {
            home_topo_view = manageXml.getXmlName();
        }
        session.setAttribute(SessionConstant.HOME_TOPO_VIEW, home_topo_view);
        Date date3 = new Date();

        // �澯���� ��Ϊ�첽��ȡ
        // AlarmSummarize alarmSummarize = new AlarmSummarize();
        // �澯״̬ ��Ϊ�첽��ȡ
        String alarmState = "0";
        // alarmState = alarmSummarize.getAlarmState();
        // ����澯�������
        String[][] alarmTypeSizeByDay = null;
        // alarmTypeSizeByDay = alarmSummarize.getAlarmTypeSizeByDay();
        // ���ܸ澯������� ��Ϊ�첽��ȡ
        String alarmTypeSizeByWeek = "0";
        // alarmTypeSizeByWeek = alarmSummarize.getAlarmTypeSizeByWeek();
        // ���ո澯�� ��Ϊ�첽��ȡ
        String alarmSizeByDay = "0";
        // alarmSizeByDay = alarmSummarize.getAlarmSizeByDay();
        // ���ܸ澯�� ��Ϊ�첽��ȡ
        String alarmSizeByWeek = "0";
        // String alarmSizeByWeek = alarmSummarize.getAlarmSizeByWeek();
        // �澯�ر���ͼƬ
        String closedAlarmPicFile = "0";
        // String closedAlarmPicFile =
        // alarmSummarize.closedAlarmPic(vo.getUserid() + "_" + vo.getId());

        // �豸����
        
        System.out.println("routeSize="+routeSize);
        System.out.println("switchSize="+switchSize);
        System.out.println("hostSize="+hostSize);
        System.out.println("DBSize="+DBSize);
        System.out.println("servieSize="+serviceSize);
        
        
        request.setAttribute("routeSize", String.valueOf(routeSize));
        request.setAttribute("switchSize", String.valueOf(switchSize));
        request.setAttribute("hostSize", String.valueOf(hostSize));
        request.setAttribute("DBSize", String.valueOf(DBSize));
        request.setAttribute("middleSize", String.valueOf(middleSize));
        request.setAttribute("serviceSize", String.valueOf(serviceSize));
        request.setAttribute("secureSize", String.valueOf(secureSize));

        // ϵͳ����״̬ͼƬ
        // request.setAttribute("routeSnapStatusImage", String.valueOf(routeSnapStatusImage));
        request.setAttribute("switchSnapStatusImage", String.valueOf(switchSnapStatusImage));
        request.setAttribute("hostSnapStatusImage", String.valueOf(hostSnapStatusImage));
        request.setAttribute("DBSnapStatusImage", String.valueOf(DBSnapStatusImage));
        request.setAttribute("middleSnapStatusImage", String.valueOf(middleSnapStatusImage));
        request.setAttribute("serviceSnapStatusImage", String.valueOf(serviceSnapStatusImage));
       // request.setAttribute("secureSnapStatusImage", String.valueOf(secureSnapStatusImage));

        // �ؼ�ҵ��
        request.setAttribute("bussinessviewHash", bussinessviewHash);

        // ����ͼ
        request.setAttribute("manageXml", manageXml);

        // �澯����
        request.setAttribute("alarmState", alarmState);
        request.setAttribute("alarmTypeSizeByDay", alarmTypeSizeByDay);
        request.setAttribute("alarmTypeSizeByWeek", alarmTypeSizeByWeek);
        request.setAttribute("alarmSizeByDay", alarmSizeByDay);
        request.setAttribute("alarmSizeByWeek", alarmSizeByWeek);
        request.setAttribute("closedAlarmPicFile", closedAlarmPicFile);
    }

    /**
     * HONGLI �õ��豸��������ת�������б�ʱ ���Ӻ��Ӧ��bid����
     * 
     * @param vo
     * @param deviceHash
     * @return
     */
    private Hashtable getTreeBidHash(User vo, Hashtable deviceHash) {
        Hashtable treeBidHash = new Hashtable();
        String bids = vo.getBusinessids();
        String[] bidsArr = bids.split(",");
        // �õ��豸������·������ת��������ҵ���id
        String treeBidRouter = "";
        String treeBidHost = "";
        String treeBidSwitch = "";
        String treeBidDb = "";
        String treeBidMid = "";
        String treeBidService = "";
        String treeBidSecu = "";
        boolean loopRoute = true;
        boolean loopHost = true;
        boolean loopSwitch = true;
        boolean loopDb = true;
        boolean loopMid = true;
        boolean loopService = true;
        boolean loopSecu = true;
        // Ĭ��ѡ����û���һ������ҵ����Ϊ��ת��treeBid
        List routelist = null;
        List hostlist = null;
        List switchlist = null;
        List dblist = null;
        List seculist = null;
        // �м��
        List iislist = (ArrayList) session.getAttribute("iislist");
        List tomcatlist = (ArrayList) session.getAttribute("tomcatlist");
        List weblogiclist = (ArrayList) session.getAttribute("weblogiclist");
        List waslist = (ArrayList) session.getAttribute("waslist");
        List dominolist = (ArrayList) session.getAttribute("dominolist");
        List mqlist = (ArrayList) session.getAttribute("mqlist");
        List jbosslist = (ArrayList) session.getAttribute("jbosslist");
        List apachelist = (ArrayList) session.getAttribute("apachelist");
        // ����
        List socketlist = (ArrayList) session.getAttribute("socketlist");
        List ftplist = (ArrayList) session.getAttribute("ftplist");
        List emaillist = (ArrayList) session.getAttribute("emaillist");
        List weblist = (ArrayList) session.getAttribute("weblist");

        if (deviceHash.containsKey("routelist")) {
            routelist = (ArrayList) deviceHash.get("routelist");
        }
        if (deviceHash.containsKey("hostlist")) {
            hostlist = (ArrayList) deviceHash.get("hostlist");
        }
        if (deviceHash.containsKey("switchlist")) {
            switchlist = (ArrayList) deviceHash.get("switchlist");
        }
        if (deviceHash.containsKey("dblist")) {
            dblist = (LinkedList) deviceHash.get("dblist");
        }
        if (deviceHash.containsKey("seculist")) {
            seculist = (ArrayList) deviceHash.get("seculist");
        }
        for (int i = 0; i < bidsArr.length; i++) {
            if (bidsArr[i] != null && !bidsArr[i].equals("")) {
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (routelist != null && loopRoute) {
                    for (int j = 0; j < routelist.size(); j++) {
                        HostNode node = (HostNode) routelist.get(j);
                        if (node != null && !node.getBid().contains(bidsArr[i])) {
                            // ��·���������ڸ�ҵ�� ����ѭ�����
                            continue;
                        } else {
                            // ��·�������ڸ�ҵ�� ����ѭ����
                            treeBidRouter = bidsArr[i];
                            loopRoute = false;
                            break;
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (hostlist != null && loopHost) {
                    for (int j = 0; j < hostlist.size(); j++) {
                        HostNode node = (HostNode) hostlist.get(j);
                        if (node != null && !node.getBid().contains(bidsArr[i])) {
                            // ��·���������ڸ�ҵ�� ����ѭ�����
                            continue;
                        } else {
                            // ��·�������ڸ�ҵ�� ����ѭ����
                            treeBidHost = bidsArr[i];
                            loopHost = false;
                            break;
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (switchlist != null && loopSwitch) {
                    for (int j = 0; j < switchlist.size(); j++) {
                        HostNode node = (HostNode) switchlist.get(j);
                        if (node != null && !node.getBid().contains(bidsArr[i])) {
                            // ��·���������ڸ�ҵ�� ����ѭ�����
                            continue;
                        } else {
                            // ��·�������ڸ�ҵ�� ����ѭ����
                            treeBidSwitch = bidsArr[i];
                            loopSwitch = false;
                            break;
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (dblist != null && loopDb) {
                    for (int j = 0; j < dblist.size(); j++) {
                        DBVo node = (DBVo) dblist.get(j);
                        if (node != null && !node.getBid().contains(bidsArr[i])) {
                            // ��·���������ڸ�ҵ�� ����ѭ�����
                            continue;
                        } else {
                            // ��·�������ڸ�ҵ�� ����ѭ����
                            treeBidDb = bidsArr[i];
                            loopDb = false;
                            break;
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (loopMid) {
                    if (iislist != null && loopMid) {
                        for (int j = 0; j < iislist.size(); j++) {
                            IISConfig node = (IISConfig) iislist.get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopMid = false;
                                break;
                            }
                        }
                    }
                    if (tomcatlist != null && loopMid) {
                        for (int j = 0; j < tomcatlist.size(); j++) {
                            Tomcat node = (Tomcat) tomcatlist.get(j);
                            if (node != null
                                    && !node.getBid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (weblogiclist != null && loopMid) {
                        for (int j = 0; j < weblogiclist.size(); j++) {
                            WeblogicConfig node = (WeblogicConfig) weblogiclist
                                    .get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (waslist != null && loopMid) {
                        for (int j = 0; j < waslist.size(); j++) {
                            WasConfig node = (WasConfig) waslist.get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (dominolist != null && loopMid) {
                        for (int j = 0; j < dominolist.size(); j++) {
                            DominoConfig node = (DominoConfig) dominolist
                                    .get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (mqlist != null && loopMid) {
                        for (int j = 0; j < mqlist.size(); j++) {
                            MQConfig node = (MQConfig) mqlist.get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (jbosslist != null && loopMid) {
                        for (int j = 0; j < jbosslist.size(); j++) {
                            JBossConfig node = (JBossConfig) jbosslist.get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                    if (apachelist != null && loopMid) {
                        for (int j = 0; j < apachelist.size(); j++) {
                            ApacheConfig node = (ApacheConfig) apachelist
                                    .get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidMid = bidsArr[i];
                                loopDb = false;
                                break;
                            }
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (loopService) {
                    if (socketlist != null && loopService) {
                        for (int j = 0; j < socketlist.size(); j++) {
                            PSTypeVo node = (PSTypeVo) socketlist.get(j);
                            if (node != null
                                    && !node.getBid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidService = bidsArr[i];
                                loopService = false;
                                break;
                            }
                        }
                    }
                    if (ftplist != null && loopService) {
                        for (int j = 0; j < ftplist.size(); j++) {
                            FTPConfig node = (FTPConfig) ftplist.get(j);
                            if (node != null
                                    && !node.getBid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidService = bidsArr[i];
                                loopService = false;
                                break;
                            }
                        }
                    }
                    if (emaillist != null && loopService) {
                        for (int j = 0; j < emaillist.size(); j++) {
                            EmailMonitorConfig node = (EmailMonitorConfig) emaillist
                                    .get(j);
                            if (node != null
                                    && !node.getBid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidService = bidsArr[i];
                                loopService = false;
                                break;
                            }
                        }
                    }
                    if (weblist != null && loopService) {
                        for (int j = 0; j < weblist.size(); j++) {
                            WebConfig node = (WebConfig) weblist.get(j);
                            if (node != null
                                    && !node.getNetid().contains(bidsArr[i])) {
                                // ��·���������ڸ�ҵ�� ����ѭ�����
                                continue;
                            } else {
                                // ��·�������ڸ�ҵ�� ����ѭ����
                                treeBidService = bidsArr[i];
                                loopService = false;
                                break;
                            }
                        }
                    }
                }
                // �鿴��ǰ����ҵ�����Ƿ���ڸ����豸
                if (seculist != null && loopSecu) {
                    for (int j = 0; j < seculist.size(); j++) {
                        HostNode node = (HostNode) seculist.get(j);
                        if (node != null && !node.getBid().contains(bidsArr[i])) {
                            // ��·���������ڸ�ҵ�� ����ѭ�����
                            continue;
                        } else {
                            // ��·�������ڸ�ҵ�� ����ѭ����
                            treeBidSecu = bidsArr[i];
                            loopSecu = false;
                            break;
                        }
                    }
                }
            }
        }
        treeBidHash.put("treeBidRouter", treeBidRouter);
        treeBidHash.put("treeBidHost", treeBidHost);
        treeBidHash.put("treeBidSwitch", treeBidSwitch);
        treeBidHash.put("treeBidDb", treeBidDb);
        treeBidHash.put("treeBidMid", treeBidMid);
        treeBidHash.put("treeBidService", treeBidService);
        treeBidHash.put("treeBidSecu", treeBidSecu);
        return treeBidHash;
    }

    public ArrayList<EventVo> getEventList() {
        ArrayList<EventVo> rpceventlist = (ArrayList<EventVo>) flexSession
                .getAttribute("rpceventlist");
        return rpceventlist;
    }

    // ��ȡ�¼��б�
    private ArrayList<EventVo> getEventList(String bids) {
        ArrayList<EventVo> flexDataList = new ArrayList<EventVo>();
        List rpceventlist = new ArrayList();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        EventListDao eventdao = new EventListDao();
        String timeFormat = "MM-dd HH:mm:ss";
        java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
                timeFormat);
        try {
            rpceventlist = eventdao.getQuery_flex(startTime, endTime, "99",
                    "99", bids, 99);
            if (rpceventlist != null && rpceventlist.size() > 0) {
                for (int i = 0; i < rpceventlist.size(); i++) {
                    EventVo Vo = new EventVo();
                    EventList event = (EventList) rpceventlist.get(i);
                    Vo.setContent(event.getContent());
                    Vo.setEventlocation(event.getEventlocation());
                    Date d2 = event.getRecordtime().getTime();
                    String time = timeFormatter.format(d2);
                    Vo.setRecordtime(time);
                    String level = String.valueOf(event.getLevel1());
                    if ("0".equals(level)) {
                        level = "��ʾ��Ϣ";
                    }
                    if ("1".equals(level)) {
                        level = "��ͨ�澯";
                    }
                    if ("2".equals(level)) {
                        level = "���ظ澯";
                    }
                    if ("3".equals(level)) {
                        level = "�����澯";
                    }
                    Vo.setLevel1(level);
                    Vo.setNodeid(event.getNodeid());
                    flexDataList.add(Vo);
                    if (i == 11)
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventdao.close();
        }
        return flexDataList;
    }

    public void getPersonHome() {
        User vo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        Date d = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = sdf0.format(d);
        String todate = sdf0.format(d);

        String starttime = startdate + " 00:00:00";
        String totime = todate + " 23:59:59";
        int one = 0;
        int two = 0;
        int three = 0;
        List list = new ArrayList();
        List allinutillist = new ArrayList();
        List alloututillist = new ArrayList();
        List allhostcpulist = new ArrayList();
        List allhostmemorylist = new ArrayList();
        List allhostdisklist = new ArrayList();

        List rpceventlist = new ArrayList();
        // ����
        HostNodeDao nodedao = new HostNodeDao();
        List networklist = new ArrayList();

        List routelist = new ArrayList();
        List switchlist = new ArrayList();
        // ������
        List hostlist = new ArrayList();
        // ���ݿ�
        List dblist = new ArrayList();
        DBDao dbdao = new DBDao();

        // ��ȫ
        List seculist = new ArrayList();

        // ����
        int servicesize = 0;

        // �м��
        int midsize = 0;

        int routesize = 0;
        int switchsize = 0;

        Hashtable lineHash = new Hashtable();
        EventListDao eventdao = new EventListDao();

        // �����¼��б�
        try {
            servicesize = getServiceNum(vo);
            midsize = getMiddleService(vo);
            String bids = vo.getBusinessids();
            if (vo.getRole() == 0 || vo.getRole() == 1) {
                bids = "-1";
            }
            // ��������Ա
            try {
                rpceventlist = eventdao.getQuery(starttime, totime, "99", "99",
                        bids, -1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventdao.close();
            }
            try {

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                nodedao.close();
            }
            try {
                networklist = nodedao.loadNetworkByBid(1, bids);
                // nodedao = new HostNodeDao();
                seculist = nodedao.loadNetworkByBid(8, bids);
                // nodedao = new HostNodeDao();
                routelist = nodedao.loadNetworkByBidAndCategory(1, bids);
                // nodedao = new HostNodeDao();
                switchlist = nodedao.loadNetworkByBidAndCategory(2, bids);
                // nodedao = new HostNodeDao();
                hostlist = nodedao.loadNetworkByBid(4, bids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                nodedao.close();
            }
            OraclePartsDao partdao = null;
            DBTypeDao typedao = null;
            try {
                dblist = dbdao.getDbByBID(bids);

                typedao = new DBTypeDao();
                List list1 = new LinkedList();
                if (dblist != null) {
                    DBTypeVo oracletypevo = null;

                    oracletypevo = (DBTypeVo) typedao.findByDbtype("oracle");
                    for (int i = 0; i < dblist.size(); i++) {
                        DBVo vo1 = (DBVo) dblist.get(i);
                        if (vo1.getDbtype() != oracletypevo.getId()) {
                            list1.add(vo1);
                        }
                    }
                    String[] bidarr = bids.split(",");
                    Vector vec = new Vector();
                    for (String bid : bidarr) {
                        if (!"".equals(bid.trim())) {
                            vec.add(bid);
                        }
                    }
                    if (dbdao != null)
                        dbdao.close();
                    dbdao = new DBDao();
                    List oraclelist = dbdao.getDbByType(oracletypevo.getId());
                    if (oraclelist != null) {

                        for (int i = 0; i < oraclelist.size(); i++) {
                            partdao = new OraclePartsDao();
                            try {
                                DBVo oraclevo = (DBVo) oraclelist.get(i);
                                List<OracleEntity> oracleparts = partdao
                                        .getOraclesByDbAndBid(oraclevo.getId(),
                                                vec);
                                for (OracleEntity ora : oracleparts) {
                                    DBVo nvo = new DBVo();
                                    nvo.setAlias(ora.getAlias());
                                    nvo.setBid(ora.getBid());
                                    nvo.setCategory(oraclevo.getCategory());
                                    nvo.setCollecttype(ora.getCollectType());
                                    nvo.setDbName(ora.getSid());
                                    nvo.setDbtype(oraclevo.getDbtype());
                                    nvo.setId(ora.getId());
                                    nvo.setIpAddress(oraclevo.getIpAddress()
                                            + ":" + ora.getSid());
                                    nvo.setManaged(oraclevo.getManaged());
                                    nvo.setPassword(ora.getPassword());
                                    nvo.setPort(oraclevo.getPort());
                                    nvo.setSendemail(oraclevo.getSendemail());
                                    nvo.setUser(ora.getUser());
                                    list1.add(nvo);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                partdao.close();
                            }
                        }
                    }
                    dblist = list1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbdao.close();
                typedao.close();
            }

            List cpulist = new ArrayList();
            List inutilhdxlist = new ArrayList();
            List oututilhdxlist = new ArrayList();
            List hostcpulist = new ArrayList();
            List hostmemorylist = new ArrayList();
            List hostdisklist = new ArrayList();
            if (networklist != null && networklist.size() > 0) {
                for (int i = 0; i < networklist.size(); i++) {
                    HostNode node = (HostNode) networklist.get(i);
                    // I_HostCollectData hostManager = new
                    // HostCollectDataManager();
                    // String startTime = new
                    // SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "
                    // 00:00:00";
                    // String endTime = new SimpleDateFormat("yyyy-MM-dd
                    // HH:mm:ss").format(new Date());
                    // // ��collectdataȡcpu����ʷ����,����ڱ���
                    // Hashtable cpuHash = new Hashtable();
                    // try {
                    // cpuHash = hostManager.getCategory(node.getIpAddress(),
                    // "CPU", "Utilization", startTime, endTime);
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    // if(cpuHash.get("avgcpucon")!=null){
                    // String avgcpucon = (String) cpuHash.get("avgcpucon");
                    // Avgcollectdata avgcollectdata = new Avgcollectdata();
                    // avgcollectdata.setIpaddress(node.getIpAddress());
                    // avgcollectdata.setThevalue(avgcpucon.replace("%", ""));
                    // cpulist.add(avgcollectdata);
                    // }
                    // ��ȡ������ʷ����ƽ��ֵ
                    // Hashtable inutilHash = new Hashtable();
                    // try {
                    // inutilHash =
                    // hostManager.getAllutilhdx(node.getIpAddress(),
                    // "AllInBandwidthUtilHdx", startTime, endTime, "");
                    // } catch (RuntimeException e) {
                    // e.printStackTrace();
                    // }
                    // if(inutilHash.get("avgput")!=null){
                    // String avgutilcon = (String) inutilHash.get("avgput");
                    // Avgcollectdata avgcollectdata = new Avgcollectdata();
                    // avgcollectdata.setIpaddress(node.getIpAddress());
                    // avgcollectdata.setThevalue(avgutilcon);
                    // inutilhdxlist.add(avgcollectdata);
                    // }
                    // Hashtable oututilHash = new Hashtable();
                    // try {
                    // oututilHash =
                    // hostManager.getAllutilhdx(node.getIpAddress(),
                    // "AllOutBandwidthUtilHdx", startTime, endTime, "");
                    // } catch (RuntimeException e) {
                    // e.printStackTrace();
                    // }
                    // if(oututilHash.get("avgput")!=null){
                    // String avgutilcon = (String) oututilHash.get("avgput");
                    // Avgcollectdata avgcollectdata = new Avgcollectdata();
                    // avgcollectdata.setIpaddress(node.getIpAddress());
                    // avgcollectdata.setThevalue(avgutilcon);
                    // oututilhdxlist.add(avgcollectdata);
                    // }
                    Hashtable ipAllData = (Hashtable) ShareData.getSharedata()
                            .get(node.getIpAddress());

                    if (ipAllData != null) {
                        Vector cpuV = (Vector) ipAllData.get("cpu");
                        if (cpuV != null && cpuV.size() > 0) {

                            CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
                            Avgcollectdata avgcollectdata = new Avgcollectdata();
                            avgcollectdata.setIpaddress(node.getIpAddress());
                            avgcollectdata.setThevalue(cpu.getThevalue()
                                    .replace("%", ""));
                            cpulist.add(avgcollectdata);
                        }
                        Vector allutil = (Vector) ipAllData.get("allutilhdx");
                        if (allutil != null && allutil.size() == 3) {
                            Avgcollectdata avgcollectdatai = new Avgcollectdata();
                            AllUtilHdx inutilhdx = (AllUtilHdx) allutil.get(0);
                            avgcollectdatai.setIpaddress(node.getIpAddress());
                            avgcollectdatai
                                    .setThevalue(inutilhdx.getThevalue());
                            inutilhdxlist.add(avgcollectdatai);
                            Avgcollectdata avgcollectdatao = new Avgcollectdata();
                            AllUtilHdx oututilhdx = (AllUtilHdx) allutil.get(1);
                            avgcollectdatao.setIpaddress(node.getIpAddress());
                            avgcollectdatao.setThevalue(oututilhdx
                                    .getThevalue());
                            oututilhdxlist.add(avgcollectdatao);
                        }
                    }
                }
            }
            if (hostlist != null && hostlist.size() > 0) {
                for (int i = 0; i < hostlist.size(); i++) {
                    HostNode node = (HostNode) hostlist.get(i);
                    // I_HostCollectData hostManager = new
                    // HostCollectDataManager();
                    // String startTime = new
                    // SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "
                    // 00:00:00";
                    // String endTime = new SimpleDateFormat("yyyy-MM-dd
                    // HH:mm:ss").format(new Date());
                    // // ��collectdataȡcpu����ʷ����,����ڱ���
                    // Hashtable cpuHash = new Hashtable();
                    // try {
                    // cpuHash = hostManager.getCategory(node.getIpAddress(),
                    // "CPU", "Utilization", startTime, endTime);
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    // if(cpuHash.get("avgcpucon")!=null){
                    // String avgcpucon = (String) cpuHash.get("avgcpucon");
                    // Avgcollectdata avgcollectdata = new Avgcollectdata();
                    // avgcollectdata.setIpaddress(node.getIpAddress());
                    // avgcollectdata.setThevalue(avgcpucon.replace("%", ""));
                    // hostcpulist.add(avgcollectdata);
                    // }
                    // // ��collectdataȡ�ڴ����ʷ����,����ڱ���
                    // Hashtable[] memoryHash = null;
                    // try {
                    // memoryHash = hostManager.getMemory(node.getIpAddress(),
                    // "Memory", startTime, endTime);
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    // if (memoryHash != null && memoryHash.length > 0) {
                    // Hashtable physicalMemoryHash = new Hashtable();
                    // physicalMemoryHash = (Hashtable)memoryHash[2];
                    // String avgmemory = (String)
                    // physicalMemoryHash.get("PhysicalMemory");
                    // if(avgmemory!=null){
                    // Avgcollectdata avgcollectdata = new Avgcollectdata();
                    // avgcollectdata.setIpaddress(node.getIpAddress());
                    // avgcollectdata.setThevalue(avgmemory.replace("%", ""));
                    // hostmemorylist.add(avgcollectdata);
                    // }
                    // }
                    Hashtable ipAllData = (Hashtable) ShareData.getSharedata()
                            .get(node.getIpAddress());
                    if (ipAllData != null) {
                        Vector cpuV = (Vector) ipAllData.get("cpu");
                        if (cpuV != null && cpuV.size() > 0) {
                            CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
                            Avgcollectdata avgcollectdata = new Avgcollectdata();
                            avgcollectdata.setIpaddress(node.getIpAddress());
                            avgcollectdata.setThevalue(cpu.getThevalue()
                                    .replace("%", ""));
                            hostcpulist.add(avgcollectdata);
                        }
                        Vector memoryVector = (Vector) ipAllData.get("memory");
                        Vector diskVector = null;
                        try {
                            diskVector = (Vector) ipAllData.get("disk");
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        DecimalFormat df = new DecimalFormat("#.##");
                        if (memoryVector != null && memoryVector.size() > 0) {
                            for (int si = 0; si < memoryVector.size(); si++) {
                                Memorycollectdata memorydata = (Memorycollectdata) memoryVector
                                        .elementAt(si);
                                if (memorydata.getEntity().equalsIgnoreCase(
                                        "Utilization")) {
                                    // ������
                                    if (memorydata.getSubentity()
                                            .equalsIgnoreCase("PhysicalMemory")) {
                                        Avgcollectdata avgcollectdata = new Avgcollectdata();
                                        avgcollectdata.setIpaddress(node
                                                .getIpAddress());
                                        avgcollectdata.setThevalue(df
                                                .format(Double
                                                        .parseDouble(memorydata
                                                                .getThevalue()
                                                                .replace("%",
                                                                        ""))));
                                        hostmemorylist.add(avgcollectdata);
                                    }
                                }
                            }
                        }
                        if (diskVector != null && diskVector.size() > 0) {
                            Hashtable hostdata = ShareData.getHostdata();
                            for (int si = 0; si < diskVector.size(); si++) {
                                Diskcollectdata diskdata = (Diskcollectdata) diskVector
                                        .elementAt(si);
                                if (diskdata.getEntity().equalsIgnoreCase(
                                        "Utilization")) {
                                    // ������
                                    if (node.getOstype() == 4
                                            || node.getSysOid().startsWith(
                                                    "1.3.6.1.4.1.311.1.1.3")) {
                                        diskdata
                                                .setSubentity(diskdata
                                                        .getSubentity()
                                                        .substring(0, 3));
                                    }
                                    hostdisklist.add(diskdata);
                                }
                            }
                        }

                    }
                }
            }
            // ��orderList����theValue��������
            // list = null;

            if (cpulist != null && cpulist.size() > 0) {
                for (int m = 0; m < cpulist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) cpulist.get(m);
                    for (int n = m + 1; n < cpulist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) cpulist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            cpulist.remove(m);
                            cpulist.add(m, hosdata);
                            cpulist.remove(n);
                            cpulist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    list.add(hdata);
                    hdata = null;
                }
            }
            if (inutilhdxlist != null && inutilhdxlist.size() > 0) {
                for (int m = 0; m < inutilhdxlist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) inutilhdxlist
                            .get(m);
                    for (int n = m + 1; n < inutilhdxlist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) inutilhdxlist
                                .get(n);
                        if (new Double(hdata.getThevalue()).doubleValue() < new Double(
                                hosdata.getThevalue()).doubleValue()) {
                            inutilhdxlist.remove(m);
                            inutilhdxlist.add(m, hosdata);
                            inutilhdxlist.remove(n);
                            inutilhdxlist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    allinutillist.add(hdata);
                    hdata = null;
                }
            }
            if (oututilhdxlist != null && oututilhdxlist.size() > 0) {
                for (int m = 0; m < oututilhdxlist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) oututilhdxlist
                            .get(m);
                    for (int n = m + 1; n < oututilhdxlist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) oututilhdxlist
                                .get(n);
                        if (new Double(hdata.getThevalue()).doubleValue() < new Double(
                                hosdata.getThevalue()).doubleValue()) {
                            oututilhdxlist.remove(m);
                            oututilhdxlist.add(m, hosdata);
                            oututilhdxlist.remove(n);
                            oututilhdxlist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    alloututillist.add(hdata);
                    hdata = null;
                }
            }
            if (hostcpulist != null && hostcpulist.size() > 0) {
                for (int m = 0; m < hostcpulist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) hostcpulist.get(m);
                    for (int n = m + 1; n < hostcpulist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) hostcpulist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            hostcpulist.remove(m);
                            hostcpulist.add(m, hosdata);
                            hostcpulist.remove(n);
                            hostcpulist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    allhostcpulist.add(hdata);
                    hdata = null;
                }
            }
            if (hostmemorylist != null && hostmemorylist.size() > 0) {
                for (int m = 0; m < hostmemorylist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) hostmemorylist
                            .get(m);
                    for (int n = m + 1; n < hostmemorylist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) hostmemorylist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            hostmemorylist.remove(m);
                            hostmemorylist.add(m, hosdata);
                            hostmemorylist.remove(n);
                            hostmemorylist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    allhostmemorylist.add(hdata);
                    hdata = null;
                }
            }
            if (hostdisklist != null && hostdisklist.size() > 0) {
                for (int m = 0; m < hostdisklist.size(); m++) {
                    Diskcollectdata hdata = (Diskcollectdata) hostdisklist
                            .get(m);
                    for (int n = m + 1; n < hostdisklist.size(); n++) {
                        Diskcollectdata hosdata = (Diskcollectdata) hostdisklist
                                .get(n);
                        if (new Double(hdata.getThevalue()).doubleValue() < new Double(
                                hosdata.getThevalue()).doubleValue()) {
                            hostdisklist.remove(m);
                            hostdisklist.add(m, hosdata);
                            hostdisklist.remove(n);
                            hostdisklist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    allhostdisklist.add(hdata);
                    hdata = null;
                }
            }
            // SysLogger.info("size:"+list.size()+"==========");
            // if(list != null)

            // } else {
            // rpceventlist = eventdao.getQuery(starttime, totime, "99", "99",
            // vo.getBusinessids(), 99);
            // networklist = nodedao.loadNetworkByBid(1, vo.getBusinessids());
            // nodedao = new HostNodeDao();
            // hostlist = nodedao.loadNetworkByBid(2, vo.getBusinessids());
            // nodedao = new HostNodeDao();
            // seculist = nodedao.loadNetworkByBid(8, vo.getBusinessids());
            // nodedao = new HostNodeDao();
            // routelist = nodedao.loadNetworkByBidAndCategory(1,
            // vo.getBusinessids());
            // nodedao = new HostNodeDao();
            // switchlist = nodedao.loadNetworkByBidAndCategory(2,
            // vo.getBusinessids());
            // dblist = dbdao.getDbByBID(vo.getBusinessids());
            // }

            // if (rpceventlist != null && rpceventlist.size() > 0) {
            // for (int i = 0; i < rpceventlist.size(); i++) {
            // EventList eventlist = (EventList) rpceventlist.get(i);
            //
            // int hours = eventlist.getRecordtime().getTime().getHours();
            // if (lineHash.get(hours) != null) {
            // Integer nums = (Integer) lineHash.get(hours);
            // nums = nums + 1;
            // lineHash.put(hours, nums);
            // } else {
            // lineHash.put(hours, new Integer(1));
            // }
            // if (eventlist.getLevel1() == 1) {
            // one = one + 1;
            // } else if (eventlist.getLevel1() == 2) {
            // two = two + 1;
            // } else if (eventlist.getLevel1() == 3) {
            // three = three + 1;
            // }
            // }
            // }
            List<FlexVo> evList = new ArrayList<FlexVo>();
            FlexVo fVo = new FlexVo();
            fVo.setObjectName("�����豸");
            fVo.setObjectNumber(networklist.size() + "");
            evList.add(fVo);
            fVo = new FlexVo();
            fVo.setObjectName("������");
            fVo.setObjectNumber(hostlist.size() + "");
            evList.add(fVo);
            fVo = new FlexVo();
            fVo.setObjectName("���ݿ�");
            fVo.setObjectNumber(dblist.size() + "");
            evList.add(fVo);
            fVo = new FlexVo();
            fVo.setObjectName("Ӧ�÷���");
            fVo.setObjectNumber(servicesize + "");
            evList.add(fVo);
            fVo = new FlexVo();
            fVo.setObjectName("�м��");
            fVo.setObjectNumber(midsize + "");
            evList.add(fVo);
            fVo = new FlexVo();
            fVo.setObjectName("��ȫ�豸");
            fVo.setObjectNumber(seculist.size() + "");
            evList.add(fVo);

            // //�¼�����
            // List evList = new ArrayList();
            // Vector v = new Vector();
            // v.add(0,"��ͨ�¼�");
            // v.add(1,one+"");
            // evList.add(v);
            // v = new Vector();
            // v.add(0,"�����¼�");
            // v.add(1,two+"");
            // evList.add(v);
            // v = new Vector();
            // v.add(0,"�����¼�");
            // v.add(1,three+"");
            // evList.add(v);
            // createxmlfile(evList);
            this.removeFlexSession();
            // ���Ӷ���
            session.setAttribute("deviceList", evList);
            // �����豸CPU
            session.setAttribute("networkCPUList", list);
            // �����豸�������
            session.setAttribute("networkInList", allinutillist);
            // �����豸��������
            session.setAttribute("networkOutList", alloututillist);
            // ������CPU
            session.setAttribute("hostCPUList", allhostcpulist);
            // �����������ڴ�
            session.setAttribute("hostMemoryList", allhostmemorylist);
            // ����������������
            session.setAttribute("hostDiskList", allhostdisklist);
            // // ���Ӷ���
            // this.createFlexXml(evList, "pie_summary_surveillance", 9999);
            // // �����豸CPU
            // this.setCPUList(list, "column_network_cpu_top10");
            // // ������CPU
            // this.setCPUList(allhostcpulist, "column_host_cpu_top10");
            // // �����豸�������
            // this.setUtilHdxList(allinutillist, "column_network_in_top10");
            // // �����豸��������
            // this.setUtilHdxList(alloututillist, "column_network_out_top10");
            // // �����������ڴ�
            // this.setMemoryList(allhostmemorylist,
            // "column_host_memory_top10");
            // // ����������������
            // this.setDiskist(allhostdisklist, "column_host_disk_top10");

            // ���Ӷ���
            this.createFlexXml(evList, null, "pie_summary_surveillance", 9999);

            // // �����豸CPU
            // this.setCPUList(list, "column_network_cpu_top10", "network_cpu");
            // // ������CPU
            // this.setCPUList(allhostcpulist, "column_host_cpu_top10",
            // "host_cpu");
            // // �����豸�������
            // this.setUtilHdxList(allinutillist, "column_network_in_top10",
            // "network_in","AllInBandwidthUtilHdx");
            // // �����豸��������
            // this.setUtilHdxList(alloututillist, "column_network_out_top10",
            // "network_out","AllOutBandwidthUtilHdx");
            // // �����������ڴ�
            // this.setMemoryList(allhostmemorylist, "column_host_memory_top10",
            // "host_memory");
            // // ����������������
            // this.setDiskist(allhostdisklist, "column_host_disk_top10",
            // "host_disk");

            // yangjun add ��Ȩ�޴���ҵ����ͼ
            ManageXmlDao manageXmlDao = new ManageXmlDao();
            List xmlList = new ArrayList();
            try {
                xmlList = manageXmlDao.loadByPerAll(bids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                manageXmlDao.close();
            }
            try {
                if (xmlList != null && xmlList.size() > 0) {
                    this.createBussTree(xmlList);// yangjun
                    // this.createxmlfile(xmlList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // for(int i=0;i<xmlList.size();i++){
            // ManageXml manageXml = (ManageXml)xmlList.get(i);
            // this.createOneXmlfile(manageXml.getXmlName().replace("jsp",
            // "xml"),manageXml.getTopoName(),"pod0"+(i+1),i+1+"");
            // }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventdao.close();
            nodedao.close();
            dbdao.close();
        }
        // request.setAttribute("infoshowlist", list);

        if (routelist != null)
            routesize = routelist.size();
        if (switchlist != null)
            switchsize = switchlist.size();
        session.setAttribute("rpceventlist", rpceventlist);
        session.setAttribute("networklist", networklist);
        session.setAttribute("hostlist", hostlist);
        session.setAttribute("midsize", midsize + "");
        session.setAttribute("servicesize", servicesize + "");
        session.setAttribute("securesize", seculist.size() + "");
        session.setAttribute("dbsize", dblist.size() + "");
        session.setAttribute("routesize", routesize + "");
        session.setAttribute("switchsize", switchsize + "");
    }

    // yangjun add �����Ӷ����б�
    public void createEquipXmlfile(List list) {
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("jianshi");
            chartxml.addEquipXML(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // yangjun add �豸�б�
    public void createequipXmlfile(List list) {
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("equip");
            chartxml.addequipXML(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �˳�ϵͳ
     * 
     * 	���Ӽ�¼�û��˳���ʱ�� add by yag 2012-08-22
     */
    private String logout() {
        session.removeAttribute(SessionConstant.CURRENT_MENU);

        User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
        if(user != null ) {
			System.out.println("�˳���" + user.getUserid() +" @ "); 
			SysLogDao dao = new SysLogDao();
			int id = 0;
			try{
				id = dao.getIdByUsername(user.getName());
			} catch(Exception e){
				
			} finally {
				dao.close();
			}
			if(id > 0){
				try{
					dao = new SysLogDao();
					dao.saveQuitTime(SysUtil.getCurrentTime(), id);
				} catch (Exception e){
					
				} finally {
					dao.close();
				}
			}
		}
        
        session.removeAttribute(SessionConstant.CURRENT_USER);
        return "/index.jsp";
    }

    public String execute(String action) {
        // ------------20091029 pm--------------------------------------------
        if (action == null)
            action = "";
        if (action.equals("setReceiver")) {
            return setReceiver();
        }
        if (action.equals("ready_add"))
            return readyAdd();
        if (action.equals("add"))
            return save();
        if (action.equals("update"))
            return update();
        if (action.equals("setdbmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DBVo vo = new DBVo();
                DBDao dbdao = new DBDao();
                vo = (DBVo) dbdao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setdbphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DBVo vo = new DBVo();
                DBDao dbdao = new DBDao();
                vo = (DBVo) dbdao.findByID(id);
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0) {
                    String mobiles[] = userphone.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        if (action.equals("setwebmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WebConfig vo = new WebConfig();
                WebConfigDao webdao = new WebConfigDao();
                vo = (WebConfig) webdao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setwebphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WebConfig vo = new WebConfig();
                WebConfigDao webdao = new WebConfigDao();
                vo = (WebConfig) webdao.findByID(id);
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0) {
                    String mobiles[] = userphone.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        if (action.equals("setmqmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                MQConfig vo = new MQConfig();
                MQConfigDao mqdao = new MQConfigDao();
                vo = (MQConfig) mqdao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setmqemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                MQConfig vo = new MQConfig();
                MQConfigDao mqdao = new MQConfigDao();
                vo = (MQConfig) mqdao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setdbphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                MQConfig vo = new MQConfig();
                MQConfigDao mqdao = new MQConfigDao();
                vo = (MQConfig) mqdao.findByID(id);
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0) {
                    String mobiles[] = userphone.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        if (action.equals("setwebemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WebConfig vo = new WebConfig();
                WebConfigDao webdao = new WebConfigDao();
                vo = (WebConfig) webdao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setwebphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WebConfig vo = new WebConfig();
                WebConfigDao webdao = new WebConfigDao();
                vo = (WebConfig) webdao.findByID(id);
                String useremails = vo.getSendphone();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        if (action.equals("setdbemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DBVo vo = new DBVo();
                DBDao dbdao = new DBDao();
                vo = (DBVo) dbdao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("list")) {
            setTarget("/system/user/list.jsp");
            UserDao dao = new UserDao();
            try {
                User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
                RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getUserManagerOperationPermission());
                boolean isRetrieveOperation = roleOperationPermissionService.isRetrieveOperationPermission();
                if (isRetrieveOperation) {
                    list(dao);
                } else {
                    list(dao, "where id='" + user.getId() + "'");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            return "/system/user/list.jsp";
        }
        if (action.equals("delete")) {
            boolean result = false;
            String jsp = "/user.do?action=list";
            String[] id = getParaArrayValue("checkbox");
            UserDao dao = new UserDao();
            try {
                result = dao.delete(id);
                if (result) {
                    UserAuditDao userAuditDao = new UserAuditDao();
                    try {
                        for (int i = 0; i < id.length; i++) {
                            userAuditDao.deleteByUserId(id[i]);
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        userAuditDao.close();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }

            return jsp;
        }
        if (action.equals("ready_edit")) {
            return ready_edit();
            // DaoInterface dao = new UserDao();
            // setTarget("/system/user/edit.jsp");
            // return readyEdit(dao);
        }
        if (action.equals("read")) {
            return read();
            // DaoInterface dao = new UserDao();
            // setTarget("/system/user/read.jsp");
            // return readyEdit(dao);
        }
        if (action.equals("login"))
            return login();
        if (action.equals("home"))
            return home();
        if (action.equals("personhome"))
            return personhome();
        if (action.equals("testJQury"))
            return testJQury();
        if (action.equals("logout"))
            return logout();
        if (action.equals("setdominomobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DominoConfig vo = new DominoConfig();
                DominoConfigDao dominodao = new DominoConfigDao();
                vo = (DominoConfig) dominodao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setdominoemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DominoConfig vo = new DominoConfig();
                DominoConfigDao dominodao = new DominoConfigDao();
                vo = (DominoConfig) dominodao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setdominophone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                DominoConfig vo = new DominoConfig();
                DominoConfigDao dominodao = new DominoConfigDao();
                vo = (DominoConfig) dominodao.findByID(id);
                String useremails = vo.getSendphone();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        // ����IIS����,�ʼ�,�绰������
        if (action.equals("setiismobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                IISConfig vo = new IISConfig();
                IISConfigDao iisdao = new IISConfigDao();
                try {
                    vo = (IISConfig) iisdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    iisdao.close();
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setiisemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                IISConfig vo = new IISConfig();
                IISConfigDao iisdao = new IISConfigDao();
                try {
                    vo = (IISConfig) iisdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    iisdao.close();
                }
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setiisphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                IISConfig vo = new IISConfig();
                IISConfigDao iisdao = new IISConfigDao();
                try {
                    vo = (IISConfig) iisdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    iisdao.close();
                }
                String useremails = vo.getSendphone();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }

        if (action.equals("setweblogicmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WeblogicConfig vo = new WeblogicConfig();
                WeblogicConfigDao weblogicdao = new WeblogicConfigDao();
                vo = (WeblogicConfig) weblogicdao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setweblogicemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WeblogicConfig vo = new WeblogicConfig();
                WeblogicConfigDao weblogicdao = new WeblogicConfigDao();
                vo = (WeblogicConfig) weblogicdao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }

        if (action.equals("setweblogicphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WeblogicConfig vo = new WeblogicConfig();
                WeblogicConfigDao weblogicdao = new WeblogicConfigDao();
                vo = (WeblogicConfig) weblogicdao.findByID(id);
                String userphones = vo.getSendphone();
                if (userphones != null && userphones.trim().length() > 0
                        && !"null".equalsIgnoreCase(userphones)) {
                    String email[] = userphones.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }

        if (action.equals("settomcatmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                Tomcat vo = new Tomcat();
                TomcatDao tomcatdao = new TomcatDao();
                vo = (Tomcat) tomcatdao.findByID(id);
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0
                        && !"null".equalsIgnoreCase(usermobiles)) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("settomcatemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                Tomcat vo = new Tomcat();
                TomcatDao weblogicdao = new TomcatDao();
                vo = (Tomcat) weblogicdao.findByID(id);
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0
                        && !"null".equalsIgnoreCase(useremails)) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("settomcatphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                Tomcat vo = new Tomcat();
                TomcatDao tomcatdao = new TomcatDao();
                vo = (Tomcat) tomcatdao.findByID(id);
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0
                        && !"null".equalsIgnoreCase(userphone)) {
                    String phones[] = userphone.split(",");
                    if (phones != null && phones.length > 0) {
                        for (int i = 0; i < phones.length; i++) {
                            ids.add(Integer.parseInt(phones[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }
        // ����SOCKET
        if (action.equals("setsocketmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                PSTypeVo vo = new PSTypeVo();
                PSTypeDao socketdao = new PSTypeDao();
                try {
                    vo = (PSTypeVo) socketdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socketdao.close();
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0
                        && !"null".equalsIgnoreCase(usermobiles)) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setsocketemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                PSTypeVo vo = new PSTypeVo();
                PSTypeDao socketdao = new PSTypeDao();
                try {
                    vo = (PSTypeVo) socketdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socketdao.close();
                }
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0
                        && !"null".equalsIgnoreCase(useremails)) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setsocketphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                PSTypeVo vo = new PSTypeVo();
                PSTypeDao socketdao = new PSTypeDao();
                try {
                    vo = (PSTypeVo) socketdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socketdao.close();
                }
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0
                        && !"null".equalsIgnoreCase(userphone)) {
                    String phones[] = userphone.split(",");
                    if (phones != null && phones.length > 0) {
                        for (int i = 0; i < phones.length; i++) {
                            ids.add(Integer.parseInt(phones[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setphone.jsp";
        }

        if (action.equals("setwasmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WasConfig vo = new WasConfig();
                WasConfigDao wasdao = null;
                try {
                    wasdao = new WasConfigDao();
                    vo = (WasConfig) wasdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (wasdao != null) {
                        wasdao.close();
                    }
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setwasemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                WasConfig vo = new WasConfig();
                WasConfigDao wasdao = null;
                try {
                    wasdao = new WasConfigDao();
                    vo = (WasConfig) wasdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (wasdao != null) {
                        wasdao.close();
                    }
                }
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }

        if (action.equals("setnetmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                HostNode vo = new HostNode();
                HostNodeDao hostdao = new HostNodeDao();
                try {
                    vo = (HostNode) hostdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hostdao.close();
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            if (mobiles[i].equalsIgnoreCase("null"))
                                continue;
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setnetemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                HostNode vo = new HostNode();
                HostNodeDao hostdao = new HostNodeDao();
                try {
                    vo = (HostNode) hostdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hostdao.close();
                }
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            if (email[i].equalsIgnoreCase("null"))
                                continue;
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }
        if (action.equals("setnetphone")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                HostNode vo = new HostNode();
                HostNodeDao hostdao = new HostNodeDao();
                try {
                    vo = (HostNode) hostdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hostdao.close();
                }
                String userphone = vo.getSendphone();
                if (userphone != null && userphone.trim().length() > 0) {
                    String phone[] = userphone.split(",");
                    if (phone != null && phone.length > 0) {
                        for (int i = 0; i < phone.length; i++) {
                            if (phone[i].equalsIgnoreCase("null"))
                                continue;
                            ids.add(Integer.parseInt(phone[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setnetphone.jsp";
        }

        if (action.equals("setgrapesmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                GrapesConfig vo = new GrapesConfig();
                GrapesConfigDao grapesdao = new GrapesConfigDao();
                try {
                    vo = (GrapesConfig) grapesdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    grapesdao.close();
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }
        if (action.equals("setgrapesemail")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                GrapesConfig vo = new GrapesConfig();
                GrapesConfigDao grapesdao = new GrapesConfigDao();
                try {
                    vo = (GrapesConfig) grapesdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    grapesdao.close();
                }
                String useremails = vo.getSendemail();
                if (useremails != null && useremails.trim().length() > 0) {
                    String email[] = useremails.split(",");
                    if (email != null && email.length > 0) {
                        for (int i = 0; i < email.length; i++) {
                            ids.add(Integer.parseInt(email[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setemail.jsp";
        }

        if (action.equals("setnetsmsmobiles")) {
            DaoInterface dao = new UserDao();
            List list = dao.loadAll();
            request.setAttribute("list", list);
            String id = request.getParameter("id");
            Vector ids = new Vector();
            if (id != null && id.length() > 0) {
                HostNode vo = new HostNode();
                HostNodeDao hostdao = new HostNodeDao();
                try {
                    vo = (HostNode) hostdao.findByID(id);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hostdao.close();
                }
                String usermobiles = vo.getSendmobiles();
                if (usermobiles != null && usermobiles.trim().length() > 0) {
                    String mobiles[] = usermobiles.split(",");
                    if (mobiles != null && mobiles.length > 0) {
                        for (int i = 0; i < mobiles.length; i++) {
                            if (mobiles[i].equalsIgnoreCase("null"))
                                continue;
                            ids.add(Integer.parseInt(mobiles[i]));
                        }
                    }
                }
            }
            request.setAttribute("ids", ids);
            return "/system/user/setmobiles.jsp";
        }

        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

    private String setReceiver() {
        DaoInterface dao = new UserDao();
        List allUser = dao.loadAll();
        request.setAttribute("allUser", allUser);
        String event = request.getParameter("event");
        request.setAttribute("event", event);
        String value = request.getParameter("value");
        List idList = new ArrayList<Integer>();
        if (value != null && value.trim().length() > 0) {
            String email[] = value.split(",");
            if (email != null && email.length > 0) {
                for (int i = 0; i < email.length; i++) {
                    idList.add(Integer.parseInt(email[i]));
                }
            }
        }

        request.setAttribute("idList", idList);
        return "/system/user/setreceiver.jsp";

    }

    /**//*
         * �ѻ�����ͼ 21
         */
    public JFreeChart creStackedBarChart(
            DefaultCategoryDataset defaultcategorydataset) {
        DefaultCategoryDataset _defaultcategorydataset = new DefaultCategoryDataset();
        _defaultcategorydataset.addValue(32.399999999999999D, "Series   1 ",
                "Category   1 "); // ֻ��
        // ʾ������ɫ�ġ�
        _defaultcategorydataset.addValue(0, "Series   2 ", "Category   1 ");
        _defaultcategorydataset.addValue(0, "Series   3 ", "Category   1 ");
        _defaultcategorydataset.addValue(43.200000000000003D, "Series   1 ",
                "Category   2 ");
        _defaultcategorydataset.addValue(15.6D, "Series   2 ", "Category   2 ");
        _defaultcategorydataset.addValue(18.300000000000001D, "Series   3 ",
                "Category   2 ");
        _defaultcategorydataset.addValue(23D, "Series   1 ", "Category   3 ");
        _defaultcategorydataset.addValue(11.300000000000001D, "Series   2 ",
                "Category   3 ");
        _defaultcategorydataset.addValue(25.5D, "Series   3 ", "Category   3 ");
        _defaultcategorydataset.addValue(13D, "Series   1 ", "Category   4 ");
        _defaultcategorydataset.addValue(11.800000000000001D, "Series   2 ",
                "Category   4 ");
        _defaultcategorydataset.addValue(29.5D, "Series   3 ", "Category   4 ");

        JFreeChart jfreechart = ChartFactory.createStackedBarChart("Exception",
                "Segment Num", "Segment Average Motion",
                _defaultcategorydataset, PlotOrientation.VERTICAL, true, true,
                false);

        jfreechart.setBackgroundPaint(Color.white);
        CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setRangeGridlinePaint(Color.white);
        StackedBarRenderer stackedbarrenderer = (StackedBarRenderer) categoryplot
                .getRenderer();
        stackedbarrenderer.setDrawBarOutline(false);
        stackedbarrenderer.setBaseItemLabelsVisible(true);
        stackedbarrenderer.setSeriesItemLabelGenerator(0,
                new StandardCategoryItemLabelGenerator());

        return jfreechart;
    }

    public int getMiddleService(User vo) {
        int midsize = 0;
        // �м��
        List iislist = new ArrayList();
        List tomcatlist = new ArrayList();
        List weblogiclist = new ArrayList();
        List waslist = new ArrayList();
        List dominolist = new ArrayList();
        List mqlist = new ArrayList();
        List jbosslist = new ArrayList();
        List apachelist = new ArrayList();

        String bids = vo.getBusinessids();
        String bid[] = bids.split(",");
        Vector rbids = new Vector();
        if (bid != null && bid.length > 0) {
            for (int i = 0; i < bid.length; i++) {
                if (bid[i] != null && bid[i].trim().length() > 0)
                    rbids.add(bid[i].trim());
            }
        }
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            // ��������Ա
            MQConfigDao mqdao = new MQConfigDao();
            try {
                mqlist = mqdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mqdao.close();
            }
            if (mqlist != null && mqlist.size() > 0) {
                midsize = midsize + mqlist.size();
            }
            DominoConfigDao dominodao = new DominoConfigDao();
            try {
                dominolist = dominodao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dominodao.close();
            }
            if (dominolist != null && dominolist.size() > 0) {
                midsize = midsize + dominolist.size();
            }
            WasConfigDao wasdao = new WasConfigDao();
            try {
                waslist = wasdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                wasdao.close();
            }
            if (waslist != null && waslist.size() > 0) {
                midsize = midsize + waslist.size();
            }
            WeblogicConfigDao weblogicdao = new WeblogicConfigDao();
            try {
                weblogiclist = weblogicdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                weblogicdao.close();
            }
            if (weblogiclist != null && weblogiclist.size() > 0) {
                midsize = midsize + weblogiclist.size();
            }
            TomcatDao tomcatdao = new TomcatDao();
            try {
                tomcatlist = tomcatdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tomcatdao.close();
            }
            if (tomcatlist != null && tomcatlist.size() > 0) {
                midsize = midsize + tomcatlist.size();
            }
            IISConfigDao iisdao = new IISConfigDao();
            try {
                iislist = iisdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                iisdao.close();
            }
            if (iislist != null && iislist.size() > 0) {
                midsize = midsize + iislist.size();
            }
            ApacheConfigDao apachedao = new ApacheConfigDao();
            try {
                apachelist = apachedao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                apachedao.close();
            }
            if (apachelist != null && apachelist.size() > 0) {
                midsize = midsize + apachelist.size();
            }
            JBossConfigDao jbossdao = new JBossConfigDao();
            try {
                jbosslist = jbossdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jbossdao.close();
            }
            if (jbosslist != null && jbosslist.size() > 0) {
                midsize = midsize + jbosslist.size();
            }
        } else {
            MQConfigDao mqdao = new MQConfigDao();
            try {
                mqlist = mqdao.getMQByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mqdao.close();
            }
            if (mqlist != null && mqlist.size() > 0) {
                midsize = midsize + mqlist.size();
            }
            DominoConfigDao dominodao = new DominoConfigDao();
            try {
                dominolist = dominodao.getDominoByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dominodao.close();
            }
            if (dominolist != null && dominolist.size() > 0) {
                midsize = midsize + dominolist.size();
            }
            WasConfigDao wasdao = new WasConfigDao();
            try {
                waslist = wasdao.getWasByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                wasdao.close();
            }
            if (waslist != null && waslist.size() > 0) {
                midsize = midsize + waslist.size();
            }
            WeblogicConfigDao weblogicdao = new WeblogicConfigDao();
            try {
                weblogiclist = weblogicdao.getWeblogicByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                weblogicdao.close();
            }
            if (weblogiclist != null && weblogiclist.size() > 0) {
                midsize = midsize + weblogiclist.size();
            }
            TomcatDao tomcatdao = new TomcatDao();
            try {
                tomcatlist = tomcatdao.getTomcatByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tomcatdao.close();
            }
            if (tomcatlist != null && tomcatlist.size() > 0) {
                midsize = midsize + tomcatlist.size();
            }
            IISConfigDao iisdao = new IISConfigDao();
            try {
                iislist = iisdao.getIISByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                iisdao.close();
            }
            if (iislist != null && iislist.size() > 0) {
                midsize = midsize + iislist.size();
            }
            ApacheConfigDao apachedao = new ApacheConfigDao();
            try {
                apachelist = apachedao.getApacheByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                apachedao.close();
            }
            if (apachelist != null && apachelist.size() > 0) {
                midsize = midsize + apachelist.size();
            }
            JBossConfigDao jbossdao = new JBossConfigDao();
            try {
                jbosslist = jbossdao.getJBossByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jbossdao.close();
            }
            if (jbosslist != null && jbosslist.size() > 0) {
                midsize = midsize + jbosslist.size();
            }
        }
        if (session != null) {
            session.setAttribute("iislist", iislist);
            session.setAttribute("tomcatlist", tomcatlist);
            session.setAttribute("weblogiclist", weblogiclist);
            session.setAttribute("waslist", waslist);
            session.setAttribute("dominolist", dominolist);
            session.setAttribute("mqlist", mqlist);
            session.setAttribute("jbosslist", jbosslist);
            session.setAttribute("apachelist", apachelist);
        }
        return midsize;
    }

    public int getServiceNum(User vo) {
        int servicesize = 0;
        // ����
        List socketlist = new ArrayList();
        List ftplist = new ArrayList();
        List emaillist = new ArrayList();
        List weblist = new ArrayList();
        PSTypeDao psdao = new PSTypeDao();
        FTPConfigDao ftpdao = new FTPConfigDao();
        EmailConfigDao emaildao = new EmailConfigDao();
        WebConfigDao webdao = new WebConfigDao();
        String bids = vo.getBusinessids();
        String bid[] = bids.split(",");
        Vector rbids = new Vector();
        if (bid != null && bid.length > 0) {
            for (int i = 0; i < bid.length; i++) {
                if (bid[i] != null && bid[i].trim().length() > 0)
                    rbids.add(bid[i].trim());
            }
        }
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            // ��������Ա
            try {
                socketlist = psdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                psdao.close();
            }
            if (socketlist != null && socketlist.size() > 0) {
                servicesize = servicesize + socketlist.size();
            }
            try {
                ftplist = ftpdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ftpdao.close();
            }
            if (ftplist != null && ftplist.size() > 0) {
                servicesize = servicesize + ftplist.size();
            }
            try {
                emaillist = emaildao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                emaildao.close();
            }
            if (emaillist != null && emaillist.size() > 0) {
                servicesize = servicesize + emaillist.size();
            }
            try {
                weblist = webdao.loadAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webdao.close();
            }
            if (weblist != null && weblist.size() > 0) {
                servicesize = servicesize + weblist.size();
            }
        } else {
            try {
                socketlist = psdao.getSocketByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                psdao.close();
            }
            if (socketlist != null && socketlist.size() > 0) {
                servicesize = servicesize + socketlist.size();
            }
            try {
                ftplist = ftpdao.getFtpByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ftpdao.close();
            }
            if (ftplist != null && ftplist.size() > 0) {
                servicesize = servicesize + ftplist.size();
            }
            try {
                emaillist = emaildao.getByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                emaildao.close();
            }
            if (emaillist != null && emaillist.size() > 0) {
                servicesize = servicesize + emaillist.size();
            }
            try {
                weblist = webdao.getWebByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                webdao.close();
            }
            if (weblist != null && weblist.size() > 0) {
                servicesize = servicesize + weblist.size();
            }
        }
        if (session != null) {
            session.setAttribute("socketlist", socketlist);
            session.setAttribute("ftplist", ftplist);
            session.setAttribute("emaillist", emaillist);
            session.setAttribute("weblist", weblist);
        }
        return servicesize;
    }

    /**
     * �����豸CPU��������CPU
     */
    // private void setCPUList(List list, String fileName) {
    // List<FlexVo> flexDataList = new ArrayList<FlexVo>();
    // FlexVo fVo;
    // for (int i = 0; i < list.size(); i++) {
    // CPUcollectdata cPUcollectdata = (CPUcollectdata) list.get(i);
    // fVo = new FlexVo();
    // fVo.setObjectName(cPUcollectdata.getIpaddress());
    // fVo.setObjectNumber(cPUcollectdata.getThevalue());
    // flexDataList.add(fVo);
    // }
    // this.createFlexXml(flexDataList, fileName, 10);
    // }
    /**
     * �����豸��ڡ���������
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    // private void setUtilHdxList(List list, String fileName) {
    // List<FlexVo> flexDataList = new ArrayList<FlexVo>();
    // FlexVo fVo;
    // for (int i = 0; i < list.size(); i++) {
    // AllUtilHdx allUtilHdx = (AllUtilHdx) list.get(i);
    // fVo = new FlexVo();
    // fVo.setObjectName(allUtilHdx.getIpaddress());
    // fVo.setObjectNumber(allUtilHdx.getThevalue());
    // flexDataList.add(fVo);
    // }
    // this.createFlexXml(flexDataList, fileName, 10);
    // }
    /**
     * �����������ڴ�
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    // private void setMemoryList(List list, String fileName) {
    // List<FlexVo> flexDataList = new ArrayList<FlexVo>();
    // FlexVo fVo;
    // for (int i = 0; i < list.size(); i++) {
    // Memorycollectdata memorycollectdata = (Memorycollectdata) list.get(i);
    // fVo = new FlexVo();
    // fVo.setObjectName(memorycollectdata.getIpaddress());
    // fVo.setObjectNumber(memorycollectdata.getThevalue());
    // flexDataList.add(fVo);
    // }
    // this.createFlexXml(flexDataList, fileName, 10);
    // }
    /**
     * ����������������
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    // private void setDiskist(List list, String fileName) {
    // List<FlexVo> flexDataList = new ArrayList<FlexVo>();
    // FlexVo fVo;
    // for (int i = 0; i < list.size(); i++) {
    // Diskcollectdata diskcollectdata = (Diskcollectdata) list.get(i);
    // fVo = new FlexVo();
    // fVo.setObjectName(diskcollectdata.getIpaddress() + " " +
    // diskcollectdata.getSubentity());
    // fVo.setObjectNumber(diskcollectdata.getThevalue());
    // flexDataList.add(fVo);
    // }
    // this.createFlexXml(flexDataList, fileName, 10);
    // }
    /**
     * �����豸CPU��������CPU
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private void setCPUList(List list, String fileName, String dir) {
        List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        FlexVo fVo;
        for (int i = 0; i < list.size(); i++) {
            Avgcollectdata cPUcollectdata = (Avgcollectdata) list.get(i);

            if (i < 10) {
                this.setCPUValue(cPUcollectdata.getIpaddress(), dir);
            }

            fVo = new FlexVo();
            fVo.setObjectName(cPUcollectdata.getIpaddress());
            fVo.setObjectNumber(cPUcollectdata.getThevalue());
            flexDataList.add(fVo);
        }
        this.createFlexXml(flexDataList, null, fileName, 10);
    }

    /**
     * ������Ӧip��ַ��cpu�ڵ������ʱ��ε�ֵ
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-28
     */
    private void setCPUValue(String ipAddress, String dir) {
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡcpu����ʷ����,����ڱ���
        Hashtable cpuHash = new Hashtable();
        try {
            cpuHash = hostManager.getCategory(ipAddress, "CPU", "Utilization",
                    startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List cpuList = new ArrayList();
        if (cpuHash.get("list") != null) {
            List<FlexVo> flexDataList = new ArrayList<FlexVo>();
            cpuList = (ArrayList) cpuHash.get("list");
            FlexVo fVo;
            for (int i = 0; i < cpuList.size(); i++) {
                Vector cpuVector = new Vector();
                fVo = new FlexVo();
                cpuVector = (Vector) cpuList.get(i);
                if (cpuVector != null || cpuVector.size() > 0) {
                    fVo.setObjectNumber((String) cpuVector.get(0) + "%");
                    fVo.setObjectName((String) cpuVector.get(1));
                    flexDataList.add(fVo);
                }
            }
            this.createFlexXml(flexDataList, dir, ipAddress, 999999);
        }
    }

    /**
     * �����豸��ڡ���������
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private void setUtilHdxList(List list, String fileName, String dir,
            String subentity) {
        List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        FlexVo fVo;
        for (int i = 0; i < list.size(); i++) {
            Avgcollectdata allUtilHdx = (Avgcollectdata) list.get(i);

            if (i < 10) {
                this.setUtilHdxValue(allUtilHdx.getIpaddress(), subentity, dir);
            }

            fVo = new FlexVo();
            fVo.setObjectName(allUtilHdx.getIpaddress());
            fVo.setObjectNumber(allUtilHdx.getThevalue() + "Kb/��");
            flexDataList.add(fVo);
        }
        this.createFlexXml(flexDataList, null, fileName, 10);
    }

    /**
     * ������Ӧip��ַ�������豸�����ڵ������ʱ��ε�ֵ
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-28
     */
    private void setUtilHdxValue(String ipAddress, String subentity, String dir) {
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ�ڴ����ʷ����,����ڱ���
        Hashtable utilHash = null;
        try {
            utilHash = hostManager.getAllutilhdx(ipAddress, subentity,
                    startTime, endTime, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (utilHash != null) {
            List<FlexVo> flexDataList = new ArrayList<FlexVo>();
            List utilHdxList = new ArrayList();
            utilHdxList = (List) utilHash.get("list");
            if (utilHdxList != null && utilHdxList.size() > 0) {
                for (int i = 0; i < utilHdxList.size(); i++) {
                    FlexVo fVo = new FlexVo();
                    Vector utilHdxVector = new Vector();
                    utilHdxVector = (Vector) utilHdxList.get(i);
                    if (utilHdxVector != null) {
                        fVo.setObjectNumber((String) utilHdxVector.get(0)
                                + (String) utilHdxVector.get(2));
                        fVo.setObjectName((String) utilHdxVector.get(1));
                        flexDataList.add(fVo);
                    }
                }
            }
            this.createFlexXml(flexDataList, dir, ipAddress, 999999);
        }
    }

    /**
     * �����������ڴ�
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private void setMemoryList(List list, String fileName, String dir) {
        List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        FlexVo fVo;
        for (int i = 0; i < list.size(); i++) {
            Avgcollectdata memorycollectdata = (Avgcollectdata) list.get(i);

            if (i < 10) {
                this.setMemoryValue(memorycollectdata.getIpaddress(), dir);
            }

            fVo = new FlexVo();
            fVo.setObjectName(memorycollectdata.getIpaddress());
            fVo.setObjectNumber(memorycollectdata.getThevalue());
            flexDataList.add(fVo);
        }
        this.createFlexXml(flexDataList, null, fileName, 10);
    }

    /**
     * ������Ӧip��ַ���ڴ��ڵ������ʱ��ε�ֵ
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-28
     */
    private void setMemoryValue(String ipAddress, String dir) {
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ�ڴ����ʷ����,����ڱ���
        Hashtable[] memoryHash = null;
        try {
            memoryHash = hostManager.getMemory(ipAddress, "Memory", startTime,
                    endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (memoryHash != null && memoryHash.length > 0) {
            Hashtable physicalMemoryHash = new Hashtable();
            physicalMemoryHash = (Hashtable) memoryHash[0];
            Vector physicalMemoryVector = new Vector();
            if (physicalMemoryHash.get("PhysicalMemory") != null) {
                List<FlexVo> flexDataList = new ArrayList<FlexVo>();
                physicalMemoryVector = (Vector) physicalMemoryHash
                        .get("PhysicalMemory");
                FlexVo fVo;
                for (int i = 0; i < physicalMemoryVector.size(); i++) {
                    Vector memoryVector = new Vector();
                    fVo = new FlexVo();
                    memoryVector = (Vector) physicalMemoryVector.get(i);
                    if (memoryVector != null || memoryVector.size() > 0) {
                        fVo.setObjectNumber((String) memoryVector.get(0) + "%");
                        fVo.setObjectName((String) memoryVector.get(1));
                        flexDataList.add(fVo);
                    }
                }
                this.createFlexXml(flexDataList, dir, ipAddress, 999999);
            }
        }
    }

    /**
     * ����������������
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private void setDiskist(List list, String fileName, String dir) {
        List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        FlexVo fVo;
        for (int i = 0; i < list.size(); i++) {
            Diskcollectdata diskcollectdata = (Diskcollectdata) list.get(i);

            if (i < 10) {
                this.setDiskValue(diskcollectdata.getIpaddress(), dir);
            }

            fVo = new FlexVo();
            fVo.setObjectName(diskcollectdata.getIpaddress() + " "
                    + diskcollectdata.getSubentity());
            fVo.setObjectNumber(diskcollectdata.getThevalue() + "%");
            flexDataList.add(fVo);
        }
        this.createFlexXml(flexDataList, null, fileName, 10);
    }

    /**
     * ������Ӧip��ַ�Ĵ����ڵ������ʱ��ε�ֵ
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-28
     */
    private void setDiskValue(String ipAddress, String dir) {
        I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ���̵���ʷ����,����ڱ���
        Hashtable diskHash = new Hashtable();
        try {
            diskHash = hostlastmanager.getDisk(ipAddress, "Disk", startTime,
                    endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List diskList = new ArrayList();
        // if (diskHash.get("list") != null) {
        // List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        // diskList = (ArrayList) diskHash.get("list");
        // FlexVo fVo;
        // for (int i = 0; i < diskList.size(); i++) {
        // Vector diskVector = new Vector();
        // fVo = new FlexVo();
        // diskVector = (Vector) diskList.get(i);
        // if (diskVector != null || diskVector.size() > 0) {
        // fVo.setObjectNumber((String) diskVector.get(0) + "%");
        // fVo.setObjectName((String) diskVector.get(1));
        // flexDataList.add(fVo);
        // }
        // }
        // this.createFlexXml(flexDataList, dir, ipAddress, 999999);
        // }
    }

    /**
     * ����xml�ļ���������
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private void createFlexXml(List list, String dir, String xmlFileName,
            int topN) {
        try {
            FlexDataXml xml = new FlexDataXml(dir, xmlFileName);
            xml.buildXml(list, topN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ҵ����ͼȨ�޿���xml
    public void createxmlfile(List list) {
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("yewu");
            chartxml.addViewXML(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ����ҵ���� yangjun
    private void createBussTree(List list) {
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("tree");
            chartxml.addViewTree(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOneXmlfile(String filename, String name, String id,
            String i) {
        try {
            ChartXml chartxml;
            chartxml = new ChartXml("yewu" + i);
            chartxml.addViewXML(filename, name, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����jQuery��Ajax����
     * 
     * @Author DHCC-huangguolong
     * @Date 2009-12-17
     */
    private String testJQury() {
        String param1 = new String(request.getParameter("param"));
        request.setAttribute("param1", param1);
        return "/common/device_list.jsp";
    }

    /**
     * Creates a sample chart.
     * 
     * @return a sample chart.
     */
    private JFreeChart createChart() {
        final XYDataset direction = createDirectionDataset(600);
        final JFreeChart chart = ChartFactory.createTimeSeriesChart("��ʪ������ͼ",
                "ʱ��", "ʪ��ֵ", direction, true, true, false);

        final XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        plot.setRangeCrosshairVisible(true);
        plot.setDomainCrosshairVisible(true);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.darkGray);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(new Color(139, 69, 19));
        XYLineAndShapeRenderer render0 = (XYLineAndShapeRenderer) plot
                .getRenderer(0);
        render0.setSeriesPaint(0, Color.BLUE);

        XYAreaRenderer xyarearenderer = new XYAreaRenderer();
        xyarearenderer.setSeriesPaint(1, Color.GREEN); // �����ɫ
        xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
        xyarearenderer.setPaint(Color.GREEN);

        // configure the range axis to display directions...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        final TickUnits units = new TickUnits();
        units.add(new NumberTickUnit(180.0, new CompassFormat()));
        units.add(new NumberTickUnit(90.0, new CompassFormat()));
        units.add(new NumberTickUnit(45.0, new CompassFormat()));
        units.add(new NumberTickUnit(22.5, new CompassFormat()));
        rangeAxis.setStandardTickUnits(units);

        // add the wind force with a secondary dataset/renderer/axis
        plot.setRangeAxis(rangeAxis);
        final XYItemRenderer renderer2 = new XYAreaRenderer();
        final ValueAxis axis2 = new NumberAxis("�¶�ֵ");
        axis2.setRange(0.0, 12.0);
        xyarearenderer.setSeriesPaint(1, new Color(0, 204, 0)); // �����ɫ
        xyarearenderer.setSeriesFillPaint(1, Color.GREEN);
        xyarearenderer.setPaint(Color.GREEN);
        plot.setDataset(1, createForceDataset(600));
        plot.setRenderer(1, xyarearenderer);
        plot.setRangeAxis(1, axis2);
        plot.mapDatasetToRangeAxis(1, 1);

        return chart;
    }

    /**
     * Creates a sample dataset.
     * 
     * @param count
     *            the item count.
     * 
     * @return the dataset.
     */
    private XYDataset createForceDataset(final int count) {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries s1 = new TimeSeries("ʪ��", Minute.class);
        RegularTimePeriod start = new Minute();
        double force = 3.0;
        for (int i = 0; i < count; i++) {
            s1.add(start, force);
            start = start.next();
            force = Math.max(0.5, force + (Math.random() - 0.5) * 0.5);
        }
        dataset.addSeries(s1);
        return dataset;
    }

    /**
     * Creates a sample dataset.
     * 
     * @param count
     *            the item count.
     * 
     * @return the dataset.
     */
    private XYDataset createDirectionDataset(final int count) {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries s1 = new TimeSeries("�¶�", Minute.class);
        RegularTimePeriod start = new Minute();
        double direction = 180.0;
        for (int i = 0; i < count; i++) {
            s1.add(start, direction);
            start = start.next();
            direction = direction + (Math.random() - 0.5) * 15.0;
            if (direction < 0.0) {
                direction = direction + 360.0;
            } else if (direction > 360.0) {
                direction = direction - 360.0;
            }
        }
        dataset.addSeries(s1);
        return dataset;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset
     *            the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart _createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createBarChart3D("����IP�ֲ�ͼ", // chart
                // title
                "IP", // domain axis label
                "���ʴ���", // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
                );
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(1.0f);

        // left align the category labels...
        final CategoryAxis axis = plot.getDomainAxis();
        final CategoryLabelPositions p = axis.getCategoryLabelPositions();

        final CategoryLabelPosition left = new CategoryLabelPosition(
                RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
                TextAnchor.CENTER_LEFT, 0.0, CategoryLabelWidthType.RANGE,
                0.30f);
        axis.setCategoryLabelPositions(CategoryLabelPositions
                .replaceLeftPosition(p, left));
        return chart;
    }

    /**
     * ������CPU������(flex����)
     * 
     * @Date 2010-1-8
     */
    public List<Avgcollectdata> getHostCPUList() {
        User vo = (User) flexSession.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }
        HostNodeDao nodedao = new HostNodeDao();
        List hostlist = new ArrayList();
        try {
            hostlist = nodedao.loadNetworkByBid(4, bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodedao.close();
        }
        List hostCPUList = new ArrayList();
        List hostcpulist = new ArrayList();
        if (hostlist != null && hostlist.size() > 0) {
            for (int i = 0; i < hostlist.size(); i++) {
                HostNode node = (HostNode) hostlist.get(i);
                I_HostCollectData hostManager = new HostCollectDataManager();
                String startTime = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date())
                        + " 00:00:00";
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());
                // ��collectdataȡcpu����ʷ����,����ڱ���
                Hashtable cpuHash = new Hashtable();
                try {
                    cpuHash = hostManager.getCategory(node.getIpAddress(),
                            "CPU", "Utilization", startTime, endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cpuHash.get("avgcpucon") != null) {
                    String avgcpucon = (String) cpuHash.get("avgcpucon");
                    Avgcollectdata avgcollectdata = new Avgcollectdata();
                    avgcollectdata.setIpaddress(node.getIpAddress());
                    avgcollectdata.setThevalue(avgcpucon.replace("%", ""));
                    hostcpulist.add(avgcollectdata);
                }
            }
            if (hostcpulist != null && hostcpulist.size() > 0) {
                for (int m = 0; m < hostcpulist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) hostcpulist.get(m);
                    for (int n = m + 1; n < hostcpulist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) hostcpulist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            hostcpulist.remove(m);
                            hostcpulist.add(m, hosdata);
                            hostcpulist.remove(n);
                            hostcpulist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    hostCPUList.add(hdata);
                    hdata = null;
                }
            }
        }
        return hostCPUList;
    }

    public List<Avgcollectdata> getHostTempCPUList() {
        List<Avgcollectdata> hostCPUList = (List<Avgcollectdata>) flexSession
                .getAttribute("hostCPUList");
        return hostCPUList;
    }

    /**
     * ��ȡ��Ӧip��ַ��cpu�ڵ������ʱ��ε�ֵ(flex����)
     */
    public ArrayList<Vos> getCPUValue(String ipAddress) {
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡcpu����ʷ����,����ڱ���
        Hashtable cpuHash = new Hashtable();
        try {
            cpuHash = hostManager.getCategory(ipAddress, "CPU", "Utilization",
                    startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List cpuList = new ArrayList();
        if (cpuHash.get("list") != null) {
            ArrayList<Vos> flexDataList = new ArrayList<Vos>();
            cpuList = (ArrayList) cpuHash.get("list");
            Vos fVo;
            for (int i = 0; i < cpuList.size(); i++) {
                Vector cpuVector = new Vector();
                fVo = new Vos();
                cpuVector = (Vector) cpuList.get(i);
                if (cpuVector != null || cpuVector.size() > 0) {
                    fVo.setObjectNumber((String) cpuVector.get(0));
                    fVo.setObjectName2((String) cpuVector.get(1));
                    fVo.setObjectName1(ipAddress);
                    flexDataList.add(fVo);
                }
            }
            return flexDataList;
        }
        return null;
    }

    /**
     * �������ڴ�������(flex����)
     * 
     * @Author DHCC-huangguolong
     * @Date 2010-1-8
     */
    public List<Memorycollectdata> getHostMemoryList() {
        User vo = (User) flexSession.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }
        List hostlist = new ArrayList();
        HostNodeDao nodedao = new HostNodeDao();
        try {
            hostlist = nodedao.loadNetworkByBid(4, bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodedao.close();
        }
        List hostmemorylist = new ArrayList();
        List hostMemoryList = new ArrayList();
        if (hostlist != null && hostlist.size() > 0) {
            for (int i = 0; i < hostlist.size(); i++) {
                HostNode node = (HostNode) hostlist.get(i);
                I_HostCollectData hostManager = new HostCollectDataManager();
                String startTime = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date())
                        + " 00:00:00";
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());
                // ��collectdataȡ�ڴ����ʷ����,����ڱ���
                Hashtable[] memoryHash = null;
                try {
                    memoryHash = hostManager.getMemory(node.getIpAddress(),
                            "Memory", startTime, endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (memoryHash != null && memoryHash.length > 0) {
                    Hashtable physicalMemoryHash = new Hashtable();
                    physicalMemoryHash = (Hashtable) memoryHash[2];
                    String avgmemory = (String) physicalMemoryHash
                            .get("PhysicalMemory");
                    if (avgmemory != null) {
                        Avgcollectdata avgcollectdata = new Avgcollectdata();
                        avgcollectdata.setIpaddress(node.getIpAddress());
                        avgcollectdata.setThevalue(avgmemory.replace("%", ""));
                        hostmemorylist.add(avgcollectdata);
                    }
                }
            }
            if (hostmemorylist != null && hostmemorylist.size() > 0) {
                for (int m = 0; m < hostmemorylist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) hostmemorylist
                            .get(m);
                    for (int n = m + 1; n < hostmemorylist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) hostmemorylist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            hostmemorylist.remove(m);
                            hostmemorylist.add(m, hosdata);
                            hostmemorylist.remove(n);
                            hostmemorylist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    hostMemoryList.add(hdata);
                    hdata = null;
                }
            }
        }
        return hostMemoryList;
    }

    public List<Memorycollectdata> getHostTempMemoryList() {
        List<Memorycollectdata> hostMemoryList = (List<Memorycollectdata>) flexSession
                .getAttribute("hostMemoryList");
        return hostMemoryList;
    }

    /**
     * ��ȡ��Ӧip��ַ���ڴ��ڵ������ʱ��ε�ֵ(flex����)
     * 
     * @Date 2009-12-28
     */
    public ArrayList<Vos> getMemoryValue(String ipAddress) {
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ�ڴ����ʷ����,����ڱ���
        Hashtable[] memoryHash = null;
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            memoryHash = hostManager.getMemory(ipAddress, "Memory", startTime,
                    endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (memoryHash != null && memoryHash.length > 0) {
            Hashtable physicalMemoryHash = new Hashtable();
            physicalMemoryHash = (Hashtable) memoryHash[0];
            Vector physicalMemoryVector = new Vector();
            if (physicalMemoryHash.get("PhysicalMemory") != null) {
                ArrayList<Vos> flexDataList = new ArrayList<Vos>();
                physicalMemoryVector = (Vector) physicalMemoryHash
                        .get("PhysicalMemory");
                Vos fVo;
                for (int i = 0; i < physicalMemoryVector.size(); i++) {
                    Vector memoryVector = new Vector();
                    fVo = new Vos();
                    memoryVector = (Vector) physicalMemoryVector.get(i);
                    if (memoryVector != null || memoryVector.size() > 0) {
                        fVo.setObjectNumber(df.format(Double
                                .parseDouble((String) memoryVector.get(0))));
                        fVo.setObjectName2((String) memoryVector.get(1));
                        fVo.setObjectName1(ipAddress);
                        flexDataList.add(fVo);
                    }
                }
                return flexDataList;
            }
        }
        return null;
    }

    /**
     * �����豸CPU(flex����)
     */
    public List<CPUcollectdata> getNetworkCPUList() {
        User vo = (User) flexSession.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }
        HostNodeDao nodedao = new HostNodeDao();
        List networklist = new ArrayList();
        try {
            networklist = nodedao.loadNetworkByBid(1, bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodedao.close();
        }
        List networkCPUList = new ArrayList();
        List cpulist = new ArrayList();
        if (networklist != null && networklist.size() > 0) {
            for (int i = 0; i < networklist.size(); i++) {
                HostNode node = (HostNode) networklist.get(i);
                I_HostCollectData hostManager = new HostCollectDataManager();
                String startTime = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date())
                        + " 00:00:00";
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());
                // ��collectdataȡcpu����ʷ����,����ڱ���
                Hashtable cpuHash = new Hashtable();
                try {
                    cpuHash = hostManager.getCategory(node.getIpAddress(),
                            "CPU", "Utilization", startTime, endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cpuHash.get("avgcpucon") != null) {
                    String avgcpucon = (String) cpuHash.get("avgcpucon");
                    Avgcollectdata avgcollectdata = new Avgcollectdata();
                    avgcollectdata.setIpaddress(node.getIpAddress());
                    avgcollectdata.setThevalue(avgcpucon.replace("%", ""));
                    cpulist.add(avgcollectdata);
                }
            }
            if (cpulist != null && cpulist.size() > 0) {
                for (int m = 0; m < cpulist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) cpulist.get(m);
                    for (int n = m + 1; n < cpulist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) cpulist
                                .get(n);
                        if (new Double(hdata.getThevalue().replace("%", ""))
                                .doubleValue() < new Double(hosdata
                                .getThevalue().replace("%", "")).doubleValue()) {
                            cpulist.remove(m);
                            cpulist.add(m, hosdata);
                            cpulist.remove(n);
                            cpulist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    networkCPUList.add(hdata);
                    hdata = null;
                }
            }
        }
        return (List<CPUcollectdata>) networkCPUList;
    }

    public List<CPUcollectdata> getNetworkTempCPUList() {
        List<CPUcollectdata> networkCPUList = (List<CPUcollectdata>) flexSession
                .getAttribute("networkCPUList");
        return networkCPUList;
    }

    /**
     * �����豸�������(flex����)
     */
    public List<AllUtilHdx> getNetworkInList() {
        User vo = (User) flexSession.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }
        HostNodeDao nodedao = new HostNodeDao();
        List networklist = new ArrayList();
        try {
            networklist = nodedao.loadNetworkByBid(1, bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodedao.close();
        }
        List networkInList = new ArrayList();
        List inutilhdxlist = new ArrayList();
        if (networklist != null && networklist.size() > 0) {
            for (int i = 0; i < networklist.size(); i++) {
                HostNode node = (HostNode) networklist.get(i);
                I_HostCollectData hostManager = new HostCollectDataManager();
                String startTime = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date())
                        + " 00:00:00";
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());
                Hashtable inutilHash = new Hashtable();
                try {
                    inutilHash = hostManager.getAllutilhdx(node.getIpAddress(),
                            "AllInBandwidthUtilHdx", startTime, endTime, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inutilHash.get("avgput") != null) {
                    String avgutilcon = (String) inutilHash.get("avgput");
                    Avgcollectdata avgcollectdata = new Avgcollectdata();
                    avgcollectdata.setIpaddress(node.getIpAddress());
                    avgcollectdata.setThevalue(avgutilcon);
                    inutilhdxlist.add(avgcollectdata);
                }
            }
            if (inutilhdxlist != null && inutilhdxlist.size() > 0) {
                for (int m = 0; m < inutilhdxlist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) inutilhdxlist
                            .get(m);
                    for (int n = m + 1; n < inutilhdxlist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) inutilhdxlist
                                .get(n);
                        if (new Double(hdata.getThevalue()).doubleValue() < new Double(
                                hosdata.getThevalue()).doubleValue()) {
                            inutilhdxlist.remove(m);
                            inutilhdxlist.add(m, hosdata);
                            inutilhdxlist.remove(n);
                            inutilhdxlist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    networkInList.add(hdata);
                    hdata = null;
                }
            }
        }
        return networkInList;
    }

    public List<AllUtilHdx> getNetworkTempInList() {
        List<AllUtilHdx> networkInList = (List<AllUtilHdx>) flexSession
                .getAttribute("networkInList");
        return networkInList;
    }

    public List<Vos> getInValue(String ipAddress) {
        List<Vos> flexDataList = new ArrayList<Vos>();
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ�ڴ����ʷ����,����ڱ���
        Hashtable utilHash = null;
        try {
            utilHash = hostManager.getAllutilhdx(ipAddress,
                    "AllInBandwidthUtilHdx", startTime, endTime, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (utilHash != null) {
            List utilHdxList = new ArrayList();
            utilHdxList = (List) utilHash.get("list");
            if (utilHdxList != null && utilHdxList.size() > 0) {
                for (int i = 0; i < utilHdxList.size(); i++) {
                    Vos fVo = new Vos();
                    Vector utilHdxVector = new Vector();
                    utilHdxVector = (Vector) utilHdxList.get(i);
                    if (utilHdxVector != null) {
                        fVo.setObjectNumber((String) utilHdxVector.get(0));
                        fVo.setObjectName2((String) utilHdxVector.get(1));
                        fVo.setObjectName1(ipAddress);
                        flexDataList.add(fVo);
                    }
                }
            }
        }
        return flexDataList;
    }

    /**
     * �����豸��������(flex����)
     */
    public List<AllUtilHdx> getNetworkOutList() {
        User vo = (User) flexSession.getAttribute(SessionConstant.CURRENT_USER);
        String bids = vo.getBusinessids();
        if (vo.getRole() == 0 || vo.getRole() == 1) {
            bids = "-1";
        }
        HostNodeDao nodedao = new HostNodeDao();
        List networklist = new ArrayList();
        try {
            networklist = nodedao.loadNetworkByBid(1, bids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodedao.close();
        }
        List networkOutList = new ArrayList();
        List oututilhdxlist = new ArrayList();
        if (networklist != null && networklist.size() > 0) {
            for (int i = 0; i < networklist.size(); i++) {
                HostNode node = (HostNode) networklist.get(i);
                I_HostCollectData hostManager = new HostCollectDataManager();
                String startTime = new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date())
                        + " 00:00:00";
                String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date());
                Hashtable oututilHash = new Hashtable();
                try {
                    oututilHash = hostManager.getAllutilhdx(
                            node.getIpAddress(), "AllOutBandwidthUtilHdx",
                            startTime, endTime, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (oututilHash.get("avgput") != null) {
                    String avgutilcon = (String) oututilHash.get("avgput");
                    Avgcollectdata avgcollectdata = new Avgcollectdata();
                    avgcollectdata.setIpaddress(node.getIpAddress());
                    avgcollectdata.setThevalue(avgutilcon);
                    oututilhdxlist.add(avgcollectdata);
                }
            }
            if (oututilhdxlist != null && oututilhdxlist.size() > 0) {
                for (int m = 0; m < oututilhdxlist.size(); m++) {
                    Avgcollectdata hdata = (Avgcollectdata) oututilhdxlist
                            .get(m);
                    for (int n = m + 1; n < oututilhdxlist.size(); n++) {
                        Avgcollectdata hosdata = (Avgcollectdata) oututilhdxlist
                                .get(n);
                        if (new Double(hdata.getThevalue()).doubleValue() < new Double(
                                hosdata.getThevalue()).doubleValue()) {
                            oututilhdxlist.remove(m);
                            oututilhdxlist.add(m, hosdata);
                            oututilhdxlist.remove(n);
                            oututilhdxlist.add(n, hdata);
                            hdata = hosdata;
                            hosdata = null;
                        }
                    }
                    // �õ�������Subentity���б�
                    networkOutList.add(hdata);
                    hdata = null;
                }
            }
        }
        return networkOutList;
    }

    public List<AllUtilHdx> getNetworkTempOutList() {
        List<AllUtilHdx> networkOutList = (List<AllUtilHdx>) flexSession
                .getAttribute("networkOutList");
        return networkOutList;
    }

    public List<Vos> getOutValue(String ipAddress) {
        List<Vos> flexDataList = new ArrayList<Vos>();
        I_HostCollectData hostManager = new HostCollectDataManager();
        String startTime = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date())
                + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        // ��collectdataȡ�ڴ����ʷ����,����ڱ���
        Hashtable utilHash = null;
        try {
            utilHash = hostManager.getAllutilhdx(ipAddress,
                    "AllOutBandwidthUtilHdx", startTime, endTime, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (utilHash != null) {
            List utilHdxList = new ArrayList();
            utilHdxList = (List) utilHash.get("list");
            if (utilHdxList != null && utilHdxList.size() > 0) {
                for (int i = 0; i < utilHdxList.size(); i++) {
                    Vos fVo = new Vos();
                    Vector utilHdxVector = new Vector();
                    utilHdxVector = (Vector) utilHdxList.get(i);
                    if (utilHdxVector != null) {
                        fVo.setObjectNumber((String) utilHdxVector.get(0));
                        fVo.setObjectName2((String) utilHdxVector.get(1));
                        fVo.setObjectName1(ipAddress);
                        flexDataList.add(fVo);
                    }
                }
            }
        }
        return flexDataList;

    }

    /**
     * ����������������(flex����)
     */
    public List<FlexVo> getHostDiskList() {
        List<Diskcollectdata> hostDiskList = (List<Diskcollectdata>) flexSession
                .getAttribute("hostDiskList");
        List<FlexVo> flexDataList = new ArrayList<FlexVo>();
        FlexVo fVo;
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < hostDiskList.size(); i++) {
            Diskcollectdata diskcollectdata = (Diskcollectdata) hostDiskList
                    .get(i);
            fVo = new FlexVo();
            fVo.setObjectName(diskcollectdata.getIpaddress() + " "
                    + diskcollectdata.getSubentity());
            fVo.setObjectNumber(df.format(Double.parseDouble(diskcollectdata
                    .getThevalue())));
            flexDataList.add(fVo);
        }
        return flexDataList;
    }

    /**
     * �Ƴ�session
     */
    private void removeFlexSession() {
        // �����豸
        if (session.getAttribute("deviceList") != null) {
            session.removeAttribute("deviceList");
        }
        // �����豸CPU
        if (session.getAttribute("networkCPUList") != null) {
            session.removeAttribute("networkCPUList");
        }
        // �����豸�������
        if (session.getAttribute("networkInList") != null) {
            session.removeAttribute("networkInList");
        }
        // �����豸��������
        if (session.getAttribute("networkOutList") != null) {
            session.removeAttribute("networkOutList");
        }
        // ������CPU
        if (session.getAttribute("hostCPUList") != null) {
            session.removeAttribute("hostCPUList");
        }
        // �����������ڴ�
        if (session.getAttribute("hostMemoryList") != null) {
            session.removeAttribute("hostMemoryList");
        }
        // ����������������
        if (session.getAttribute("hostDiskList") != null) {
            session.removeAttribute("hostDiskList");
        }
    }

    /**
     * �Լ���б��������
     * 
     * @author nielin
     * @date 2010-08-09
     * @param montinorList
     *            <code>����б�</code>
     * @param category
     *            <code>�豸����</code>
     * @param field
     *            <code>�����ֶ�</code>
     * @param type
     *            <code>��������</code>
     * @return
     */
    public List monitorListSort(List montinorList, String category,
            String field, String type) {
        // System.out.println(category+ "===String==" + field +"====String===="+
        // type);
        // if("host".equals(category)){
        // for(int i = 0 ; i < montinorList.size() -1 ; i ++){
        // for(int j = i + 1 ; j < montinorList.size() ; j ++){
        // MonitorHostDTO monitorHostDTO = (MonitorHostDTO)montinorList.get(i);
        // MonitorHostDTO monitorHostDTO2 = (MonitorHostDTO)montinorList.get(j);
        //					
        // String fieldValue = "";
        //					
        // String fieldValue2 = "";
        // if("name".equals(field)){
        // fieldValue = monitorHostDTO.getAlias();
        //						
        // fieldValue2 = monitorHostDTO2.getAlias();
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) < 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) > 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        //							
        // }else if ("ipaddress".equals(field)){
        // fieldValue = monitorHostDTO.getIpAddress();
        //						
        // fieldValue2 = monitorHostDTO2.getIpAddress();
        //						
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) < 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(fieldValue.compareTo(fieldValue2) > 0){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("cpu".equals(field)){
        // fieldValue = monitorHostDTO.getCpuValue();
        //						
        // fieldValue2 = monitorHostDTO2.getCpuValue();
        //						
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("ping".equals(field)){
        // fieldValue = monitorHostDTO.getPingValue();
        //						
        // fieldValue2 = monitorHostDTO2.getPingValue();
        //						
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }else if ("memory".equals(field)){
        // fieldValue = monitorHostDTO.getMemoryValue();
        //						
        // fieldValue2 = monitorHostDTO2.getMemoryValue();
        //						
        // if("desc".equals(type)){
        // // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) < Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }else if ("asc".equals(type)){
        // // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
        // if(Double.valueOf(fieldValue) > Double.valueOf(fieldValue2)){
        // montinorList.set(i, monitorHostDTO2);
        // montinorList.set(j, monitorHostDTO);
        // }
        // }
        // }
        //					
        //					
        // }
        // }
        // } else if("net".equals(category)){
        for (int i = 0; i < montinorList.size() - 1; i++) {
            for (int j = i + 1; j < montinorList.size(); j++) {
                MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO) montinorList
                        .get(i);
                MonitorNodeDTO monitorNodeDTO2 = (MonitorNodeDTO) montinorList
                        .get(j);

                String fieldValue = "";

                String fieldValue2 = "";
                if ("name".equals(field)) {
                    fieldValue = monitorNodeDTO.getAlias();

                    fieldValue2 = monitorNodeDTO2.getAlias();
                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }

                } else if ("ipaddress".equals(field)) {
                    fieldValue = monitorNodeDTO.getIpAddress();

                    fieldValue2 = monitorNodeDTO2.getIpAddress();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("cpu".equals(field)) {
                    fieldValue = monitorNodeDTO.getCpuValue();

                    fieldValue2 = monitorNodeDTO2.getCpuValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("ping".equals(field)) {
                    fieldValue = monitorNodeDTO.getPingValue();

                    fieldValue2 = monitorNodeDTO2.getPingValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("memory".equals(field)) {
                    fieldValue = monitorNodeDTO.getMemoryValue();

                    fieldValue2 = monitorNodeDTO2.getMemoryValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("inutilhdx".equals(field)) {
                    fieldValue = monitorNodeDTO.getInutilhdxValue();

                    fieldValue2 = monitorNodeDTO2.getInutilhdxValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("oututilhdx".equals(field)) {
                    fieldValue = monitorNodeDTO.getOututilhdxValue();

                    fieldValue2 = monitorNodeDTO2.getOututilhdxValue();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) < Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (Double.valueOf(fieldValue) > Double
                                .valueOf(fieldValue2)) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                } else if ("category".equals(field)) {
                    fieldValue = monitorNodeDTO.getCategory();

                    fieldValue2 = monitorNodeDTO2.getCategory();

                    if ("desc".equals(type)) {
                        // ����ǽ��� �� ǰһ�� С�� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) < 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    } else if ("asc".equals(type)) {
                        // ��������� �� ǰһ�� ���� ��һ�� �򽻻�
                        if (fieldValue.compareTo(fieldValue2) > 0) {
                            montinorList.set(i, monitorNodeDTO2);
                            montinorList.set(j, monitorNodeDTO);
                        }
                    }
                }

            }
        }
        // }

        return montinorList;
    }

    public List getMonitorListByCategory(String category) {

        String where = "";

        if ("node".equals(category)) {
            where = " where managed=1";
        } else if ("net_server".equals(category)) {
            where = " where managed=1 and category=4";
        } else if ("net".equals(category)) {
            where = " where managed=1 and (category=1 or category=2 or category=3 or category=7) ";
        } else if ("net_router".equals(category)) {
            where = " where managed=1 and category=1";
        } else if ("net_switch".equals(category)) {
            where = " where managed=1 and (category=2 or category=3 or category=7) ";
        }

        where = where + getBidSql();

        HostNodeDao dao = new HostNodeDao();

        String key = getParaValue("key");

        String value = getParaValue("value");
        if (key != null && key.trim().length() > 0 && value != null
                && value.trim().length() > 0) {
            where = where + " and " + key + "='" + value + "'";

            // System.out.println(where);

        }
        try {
            list(dao, where);
        } catch (Exception e) {

        } finally {
            dao.close();
        }
        // request.setAttribute("flag", 1);
        List list = (List) request.getAttribute("list");
        return list;
    }

    /**
     * ���ҵ��Ȩ�޵� SQL ���
     * 
     * @author nielin
     * @date 2010-08-09
     * @return
     */
    public String getBidSql() {
        User current_user = (User) session
                .getAttribute(SessionConstant.CURRENT_USER);

        StringBuffer s = new StringBuffer();
        int _flag = 0;
        if (current_user.getBusinessids() != null) {
            if (current_user.getBusinessids() != "-1") {
                String[] bids = current_user.getBusinessids().split(",");
                if (bids.length > 0) {
                    for (int i = 0; i < bids.length; i++) {
                        if (bids[i].trim().length() > 0) {
                            if (_flag == 0) {
                                s.append(" and ( bid like '%," + bids[i].trim()
                                        + ",%' ");
                                _flag = 1;
                            } else {
                                // flag = 1;
                                s.append(" or bid like '%," + bids[i].trim()
                                        + ",%' ");
                            }
                        }
                    }
                    s.append(") ");
                }

            }
        }

        // SysLogger.info("select * from topo_host_node where managed=1 "+s);

        String sql = "";
        if (current_user.getRole() == 0) {
            sql = "";
        } else {
            sql = s.toString();
        }
        return sql;
    }

    /**
     * ͨ�� hostNode ����װ MonitorNodeDTO
     * 
     * @param hostNode
     * @return
     */
    public MonitorNodeDTO getMonitorNodeDTOByHostNode(HostNode hostNode) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String starttime = date + " 00:00:00";
        String totime = date + " 23:59:59";

        NumberFormat numberFormat = new DecimalFormat();
        numberFormat.setMaximumFractionDigits(0);

        MonitorNodeDTO monitorNodeDTO = null;

        String ipAddress = hostNode.getIpAddress();
        int nodeId = hostNode.getId();
        String alias = hostNode.getAlias();

        int category = hostNode.getCategory();
        if (category == 1) {
            monitorNodeDTO = new MonitorNetDTO();
            monitorNodeDTO.setCategory("·����");
        } else if (category == 4) {
            monitorNodeDTO = new MonitorHostDTO();
            monitorNodeDTO.setCategory("������");
        } else {
            monitorNodeDTO = new MonitorNetDTO();
            monitorNodeDTO.setCategory("������");
        }

        // ����id
        monitorNodeDTO.setId(nodeId);
        // ����ip
        monitorNodeDTO.setIpAddress(ipAddress);
        // ��������
        monitorNodeDTO.setAlias(alias);

        Host node = (Host) PollingEngine.getInstance().getNodeByID(nodeId);
        // �澯״̬
        if (node != null) {
            monitorNodeDTO.setStatus(node.getStatus() + "");
        } else {
            monitorNodeDTO.setStatus("0");
        }
        // monitorNodeDTO.setStatus(node.getAlarmlevel() + "");

        String cpuValue = "0"; // cpu Ĭ��Ϊ 0
        String memoryValue = "0"; // memory Ĭ��Ϊ 0
        String inutilhdxValue = "0"; // inutilhdx Ĭ��Ϊ 0
        String oututilhdxValue = "0"; // oututilhdx Ĭ��Ϊ 0
        String pingValue = "0"; // ping Ĭ��Ϊ 0
        String eventListCount = ""; // eventListCount Ĭ��Ϊ 0
        String collectType = ""; // �ɼ�����

        String cpuValueColor = "green"; // cpu ��ɫ
        String memoryValueColor = "green"; // memory ��ɫ

        String generalAlarm = "0"; // ��ͨ�澯�� Ĭ��Ϊ 0
        String urgentAlarm = "0"; // ���ظ澯�� Ĭ��Ϊ 0
        String seriousAlarm = "0"; // �����澯�� Ĭ��Ϊ 0

        double cpuValueDouble = 0;
        double memeryValueDouble = 0;

        Hashtable eventListSummary = new Hashtable();

        Hashtable sharedata = ShareData.getSharedata();

        Hashtable ipAllData = (Hashtable) sharedata.get(ipAddress);

        Hashtable allpingdata = ShareData.getPingdata();

        if (ipAllData != null) {
            Vector cpuV = (Vector) ipAllData.get("cpu");
            if (cpuV != null && cpuV.size() > 0) {
                CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
                if (cpu != null && cpu.getThevalue() != null) {
                    cpuValueDouble = Double.valueOf(cpu.getThevalue());
                    cpuValue = numberFormat.format(cpuValueDouble);
                }
            }

            Vector memoryVector = (Vector) ipAllData.get("memory");

            int allmemoryvalue = 0;
            if (memoryVector != null && memoryVector.size() > 0) {
                for (int si = 0; si < memoryVector.size(); si++) {
                    Memorycollectdata memorydata = (Memorycollectdata) memoryVector
                            .elementAt(si);
                    if (memorydata.getEntity().equalsIgnoreCase("Utilization")) {
                        // ������
                        // if
                        // (memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory"))
                        // {
                        // memeryValueDouble = memeryValueDouble +
                        // Double.valueOf(memorydata.getThevalue());
                        // }
                        if (category == 4
                                && memorydata.getSubentity().equalsIgnoreCase(
                                        "PhysicalMemory")) {// �����������
                            memeryValueDouble = Double.valueOf(memorydata
                                    .getThevalue());
                        }
                        if (category == 1 || category == 2 || category == 3) {// �����豸�����
                            allmemoryvalue = allmemoryvalue
                                    + Integer
                                            .parseInt(memorydata.getThevalue());
                            if (si == memoryVector.size() - 1) {
                                memeryValueDouble = allmemoryvalue
                                        / memoryVector.size();
                            }
                        }
                    }
                }
                memoryValue = numberFormat.format(memeryValueDouble);
            }

            Vector allutil = (Vector) ipAllData.get("allutilhdx");
            if (allutil != null && allutil.size() == 3) {
                AllUtilHdx inutilhdx = (AllUtilHdx) allutil.get(0);
                inutilhdxValue = inutilhdx.getThevalue();

                AllUtilHdx oututilhdx = (AllUtilHdx) allutil.get(1);
                oututilhdxValue = oututilhdx.getThevalue();
            }
        }

        if (allpingdata != null) {
            Vector pingData = (Vector) allpingdata.get(ipAddress);
            if (pingData != null && pingData.size() > 0) {
                Pingcollectdata pingcollectdata = (Pingcollectdata) pingData
                        .get(0);
                pingValue = pingcollectdata.getThevalue();
            }
        }
        String count = "";
        EventListDao eventListDao = new EventListDao();
        try {
            generalAlarm = eventListDao.getCountByWhere(" where nodeid='"
                    + hostNode.getId() + "'"
                    + " and level1='1' and recordtime>='" + starttime
                    + "' and recordtime<='" + totime + "'");
            urgentAlarm = eventListDao.getCountByWhere(" where nodeid='"
                    + hostNode.getId() + "'"
                    + " and level1='2' and recordtime>='" + starttime
                    + "' and recordtime<='" + totime + "'");
            seriousAlarm = eventListDao.getCountByWhere(" where nodeid='"
                    + hostNode.getId() + "'"
                    + " and level1='3' and recordtime>='" + starttime
                    + "' and recordtime<='" + totime + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventListDao.close();
        }
        eventListCount = count;
        eventListSummary.put("generalAlarm", generalAlarm);
        eventListSummary.put("urgentAlarm", urgentAlarm);
        eventListSummary.put("seriousAlarm", seriousAlarm);

        monitorNodeDTO.setEventListSummary(eventListSummary);

        if (SystemConstant.COLLECTTYPE_SNMP == hostNode.getCollecttype()) {
            collectType = "SNMP";
        } else if (SystemConstant.COLLECTTYPE_PING == hostNode.getCollecttype()) {
            collectType = "PING";
        } else if (SystemConstant.COLLECTTYPE_REMOTEPING == hostNode
                .getCollecttype()) {
            collectType = "REMOTEPING";
        } else if (SystemConstant.COLLECTTYPE_SHELL == hostNode
                .getCollecttype()) {
            collectType = "SHELL";
        } else if (SystemConstant.COLLECTTYPE_SSH == hostNode.getCollecttype()) {
            collectType = "SSH";
        } else if (SystemConstant.COLLECTTYPE_TELNET == hostNode
                .getCollecttype()) {
            collectType = "TELNET";
        } else if (SystemConstant.COLLECTTYPE_WMI == hostNode.getCollecttype()) {
            collectType = "WMI";
        }

        NodeMonitorDao nodeMonitorDao = new NodeMonitorDao();

        List nodeMonitorList = null;
        try {
            nodeMonitorList = nodeMonitorDao.loadByNodeID(nodeId);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            nodeMonitorDao.close();
        }
        if (nodeMonitorList != null) {
            for (int j = 0; j < nodeMonitorList.size(); j++) {
                NodeMonitor nodeMonitor = (NodeMonitor) nodeMonitorList.get(j);
                if ("cpu".equals(nodeMonitor.getCategory())) {
                    if (cpuValueDouble > nodeMonitor.getLimenvalue2()) {
                        cpuValueColor = "red";
                    } else if (cpuValueDouble > nodeMonitor.getLimenvalue1()) {
                        cpuValueColor = "orange";
                    } else if (cpuValueDouble > nodeMonitor.getLimenvalue0()) {
                        cpuValueColor = "yellow";
                    } else {
                        cpuValueColor = "green";
                    }
                }

                if ("memory".equals(nodeMonitor.getCategory())) {
                    if (memeryValueDouble > nodeMonitor.getLimenvalue2()) {
                        memoryValueColor = "red";
                    } else if (memeryValueDouble > nodeMonitor.getLimenvalue1()) {
                        memoryValueColor = "orange";
                    } else if (memeryValueDouble > nodeMonitor.getLimenvalue0()) {
                        memoryValueColor = "yellow";
                    } else {
                        memoryValueColor = "green";
                    }
                }
            }
        }

        monitorNodeDTO.setCpuValue(cpuValue);
        monitorNodeDTO.setMemoryValue(memoryValue);
        monitorNodeDTO.setInutilhdxValue(inutilhdxValue);
        monitorNodeDTO.setOututilhdxValue(oututilhdxValue);
        monitorNodeDTO.setPingValue(pingValue);
        monitorNodeDTO.setEventListCount(eventListCount);
        monitorNodeDTO.setCollectType(collectType);
        monitorNodeDTO.setCpuValueColor(cpuValueColor);
        monitorNodeDTO.setMemoryValueColor(memoryValueColor);
        return monitorNodeDTO;
    }
}