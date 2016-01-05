/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2009-10-29
 */

package com.afunms.application.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.CicsConfigDao;
import com.afunms.application.dao.DnsConfigDao;
import com.afunms.application.dao.DominoConfigDao;
import com.afunms.application.dao.EmailConfigDao;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.Ftpmonitor_historyDao;
import com.afunms.application.dao.Ftpmonitor_realtimeDao;
import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.dao.MQConfigDao;
import com.afunms.application.dao.PSTypeDao;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.dao.WasConfigDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.dao.WeblogicConfigDao;
import com.afunms.application.model.EmailMonitorConfig;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.Ftpmonitor_realtime;
import com.afunms.application.model.MQConfig;
import com.afunms.application.model.PSTypeVo;
import com.afunms.application.model.Tomcat;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.DateE;
import com.afunms.common.util.InitCoordinate;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.ProcsDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.initialize.ResourceCenter;
import com.afunms.node.service.NodeService;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.manage.PollMonitorManager;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.FTPDataCollector;
import com.afunms.polling.task.FtpUtil;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.model.User;
import com.afunms.topology.util.KeyGenerator;

public class FTPManager extends BaseManager implements ManagerInterface {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static final SysLogger logger = SysLogger.getLogger(FTPManager.class);

    public String execute(String action) {
        if (action.equals("list")) {
            return list();
        } else if (action.equals("add")) {
            return add();
        } else if (action.equals("save")) {
            return save();
        } else if (action.equals("delete")) {
            return delete();
        } else if (action.equals("edit")) {
            return edit();
        } else if (action.equals("update")) {
            return update();
        } else if (action.equals("detail")) {
            return detail();
        } else if (action.equals("sychronizeData")) {
            return sychronizeData();
        } else if (action.equals("changeMonflag")) {
            return changeMonflag();
        } else if (action.equals("allservice")) {
            return allServiceList();
        } else if (action.equals("midalllist")) {
            return midalllist();

        } else if (action.equals("showPingReport")) {
            return showPingReport();

        } else if (action.equals("showCompositeReport")) {
            return showCompositeReport();

        } else if (action.equals("showServiceEventReport")) {
            return showServiceEventReport();

        }
        if (action.equals("isOK"))
            return isOK();
        if (action.equals("alarm")) {
            return alarm();
        }

        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

    private String alarm() {
        detail();
        Vector vector = new Vector();

        String ip = "";
        String tmp = "";
        List list = new ArrayList();
        int status = 99;
        int level1 = 99;
        String b_time = "";
        String t_time = "";
        try {

            tmp = request.getParameter("id");
            request.setAttribute("id", Integer.parseInt(tmp));
            status = getParaIntValue("status");
            level1 = getParaIntValue("level1");
            if (status == -1)
                status = 99;
            if (level1 == -1)
                level1 = 99;
            request.setAttribute("status", status);
            request.setAttribute("level1", level1);

            b_time = getParaValue("startdate");
            t_time = getParaValue("todate");

            if (b_time == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                b_time = sdf.format(new Date());
            }
            if (t_time == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t_time = sdf.format(new Date());
            }
            String starttime1 = b_time + " 00:00:00";
            String totime1 = t_time + " 23:59:59";

            try {
                User vo = (User) session
                        .getAttribute(SessionConstant.CURRENT_USER); // 用户姓名
                EventListDao dao = new EventListDao();
                list = dao
                        .getQuery(starttime1, totime1, status + "",
                                level1 + "", vo.getBusinessids(), Integer
                                        .parseInt(tmp), "ftp");
                // list = dao.getQuery(starttime1,totime1,status+"",level1+"",
                // vo.getBusinessids(),Integer.parseInt(tmp));

                // ConnectUtilizationhash =
                // hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("vector", vector);
        request.setAttribute("id", Integer.parseInt(tmp));
        request.setAttribute("list", list);
        request.setAttribute("startdate", b_time);
        request.setAttribute("todate", t_time);
        return "/application/ftp/alarm.jsp";
    }

    /**
     * 生成FTP饼图 guangfei
     * 
     * @return
     */
    private String detail() {
        java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FTPConfigDao configdao = new FTPConfigDao();
        Ftpmonitor_realtimeDao realtimedao = new Ftpmonitor_realtimeDao();
        Ftpmonitor_historyDao historydao = new Ftpmonitor_historyDao();
        List urllist = new ArrayList(); // 用做条件选择列表
        FTPConfig initconf = new FTPConfig(); // 当前的对象
        String lasttime = "";
        String nexttime = "";
        String conn_name = "";
        String valid_name = "";
        String fresh_name = "";
        String wave_name = "";
        String delay_name = "";
        String connrate = "0";
        String validrate = "0";
        String freshrate = "0";
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Date nowdate = new Date();
        nowdate.getHours();
        String from_date1 = getParaValue("from_date1");
        if (from_date1 == null) {
            from_date1 = timeFormatter.format(new Date());
            request.setAttribute("from_date1", from_date1);
        }
        String to_date1 = getParaValue("to_date1");
        if (to_date1 == null) {
            to_date1 = timeFormatter.format(new Date());
            request.setAttribute("to_date1", to_date1);
        }
        String from_hour = getParaValue("from_hour");
        if (from_hour == null) {
            from_hour = "00";
            request.setAttribute("from_hour", from_hour);
        }
        String to_hour = getParaValue("to_hour");
        if (to_hour == null) {
            to_hour = nowdate.getHours() + "";
            request.setAttribute("to_hour", to_hour);
        }
        String starttime = from_date1 + " " + from_hour + ":00:00";
        String totime = to_date1 + " " + to_hour + ":59:59";
        int flag = 0;
        try {
            User operator = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER);
            String bids = operator.getBusinessids();
            String bid[] = bids.split(",");
            Vector rbids = new Vector();
            if (bid != null && bid.length > 0) {
                for (int i = 0; i < bid.length; i++) {
                    if (bid[i] != null && bid[i].trim().length() > 0)
                        rbids.add(bid[i].trim());
                }
            }
            try {
                urllist = configdao.getFtpByBID(rbids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                configdao.close();
            }

            Integer queryid = getParaIntValue("id");// .getUrl_id();
            request.setAttribute("id", queryid);
            if (urllist.size() > 0 && queryid == null) {
                Object obj = urllist.get(0);
            }
            if (queryid != null) {
                // 如果是链接过来则取用查询条件
                configdao = new FTPConfigDao();
                try {
                    initconf = (FTPConfig) configdao.findByID(queryid + "");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    configdao.close();
                }
            }
            queryid = initconf.getId();
            conn_name = queryid + "urlmonitor-conn";
            valid_name = queryid + "urlmonitor-valid";
            fresh_name = queryid + "urlmonitor-refresh";
            wave_name = queryid + "urlmonitor-rec";
            delay_name = queryid + "urlmonitor-delay";
            List urlList = null;
            try {
                urlList = realtimedao.getByFTPId(queryid);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                realtimedao.close();
            }

            Calendar last = null;
            if (urlList != null && urlList.size() > 0) {
                last = ((Ftpmonitor_realtime) urlList.get(0)).getMon_time();
            }
            int interval = 0;
            // TaskXmlUtil taskutil = new TaskXmlUtil("urltask");
            try {
                // Session session = this.beginTransaction();
                List numList = new ArrayList();
                TaskXml taskxml = new TaskXml();
                numList = taskxml.ListXml();
                for (int i = 0; i < numList.size(); i++) {
                    Task task = new Task();
                    BeanUtils.copyProperties(task, numList.get(i));
                    if (task.getTaskname().equals("urltask")) {
                        interval = task.getPolltime().intValue();
                        // numThreads = task.getPolltime().intValue();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DateE de = new DateE();
            if (last == null) {
                last = new GregorianCalendar();
                flag = 1;
            }
            lasttime = de.getDateDetail(last);
            last.add(Calendar.MINUTE, interval);
            nexttime = de.getDateDetail(last);

            int hour = 1;
            if (getParaValue("hour") != null) {
                hour = Integer.parseInt(getParaValue("hour"));
            } else {
                request.setAttribute("hour", "1");
                // urlconfForm.setHour("1");
            }

            InitCoordinate initer = new InitCoordinate(new GregorianCalendar(),
                    hour, 1);
            // Minute[] minutes=initer.getMinutes();
            TimeSeries ss1 = new TimeSeries("", Minute.class);
            TimeSeries ss2 = new TimeSeries("", Minute.class);

            // ss.add()
            TimeSeries[] s = new TimeSeries[1];
            TimeSeries[] s_ = new TimeSeries[1];
            // Vector wave_v = historyManager.getInfo(queryid,initer);
            Vector wave_v = null;
            try {
                wave_v = historydao.getByFTPid(queryid, starttime, totime, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // historydao.close();
            }
            if (wave_v == null)
                wave_v = new Vector();
            for (int i = 0; i < wave_v.size(); i++) {
                Hashtable ht = (Hashtable) wave_v.get(i);
                double conn = Double.parseDouble(ht.get("conn").toString());
                String time = ht.get("mon_time").toString();
                ss1.addOrUpdate(new Minute(sdf1.parse(time)), conn);
            }
            s[0] = ss1;
            s_[0] = ss2;
            ChartGraph cg = new ChartGraph();
            cg.timewave(s, "时间", "连通", "", wave_name, 600, 120);
            // cg.timewave(s_,"时间","时延(ms)","",delay_name,600,120);
            // p_draw_line(s,"","url"+queryid+"ConnectUtilization",740,120);

            // 是否连通
            String conn[] = new String[2];
            if (flag == 0)
                conn = historydao.getAvailability(queryid, starttime, totime,
                        "is_canconnected");
            else {
                conn = historydao.getAvailability(queryid, starttime, totime,
                        "is_canconnected");
            }
            // System.out.println(conn[0] + "!!!" + conn[1]);
            String[] key1 = { "连通", "未连通" };
            drawPiechart(key1, conn, "", conn_name);
            connrate = getF(String.valueOf(Float.parseFloat(conn[0])
                    / (Float.parseFloat(conn[0]) + Float.parseFloat(conn[1]))
                    * 100))
                    + "%";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            historydao.close();
            configdao.close();
            realtimedao.close();
        }
        request.setAttribute("urllist", urllist);
        request.setAttribute("initconf", initconf);
        request.setAttribute("lasttime", lasttime);
        request.setAttribute("nexttime", nexttime);
        request.setAttribute("conn_name", conn_name);
        request.setAttribute("valid_name", valid_name);
        request.setAttribute("fresh_name", fresh_name);
        request.setAttribute("wave_name", wave_name);
        request.setAttribute("delay_name", delay_name);
        request.setAttribute("connrate", connrate);
        request.setAttribute("validrate", validrate);
        request.setAttribute("freshrate", freshrate);
        request.setAttribute("from_date1", from_date1);
        request.setAttribute("to_date1", to_date1);
        request.setAttribute("from_hour", from_hour);
        request.setAttribute("to_hour", to_hour);
        return "/application/ftp/detail.jsp";
    }

    private String showPingReport() {
        Date d = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = getParaValue("startdate");
        Hashtable reporthash = new Hashtable();
        if (startdate == null) {
            startdate = sdf0.format(d);
        }
        String todate = getParaValue("todate");
        if (todate == null) {
            todate = sdf0.format(d);
        }
        String starttime = startdate + " 00:00:00";
        String totime = todate + " 23:59:59";

        String newip = "";
        String ip = "";
        Integer queryid = getParaIntValue("id");
        FTPConfig initconf = null;
        FTPConfigDao configdao = new FTPConfigDao();
        try {
            initconf = (FTPConfig) configdao.findByID(String.valueOf(queryid));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }
        try {
            ip = getParaValue("ipaddress");

            newip = SysUtil.doip(ip);
            String runmodel = PollingEngine.getCollectwebflag();

            Ftpmonitor_historyDao historydao = new Ftpmonitor_historyDao();

            Hashtable ConnectUtilizationhash = historydao.getPingDataById(
                    queryid, starttime, totime);
            String curPing = "";
            String pingconavg = "";
            if (ConnectUtilizationhash.get("avgPing") != null)
                pingconavg = (String) ConnectUtilizationhash.get("avgPing");
            String minPing = "";

            if (ConnectUtilizationhash.get("minPing") != null) {
                minPing = (String) ConnectUtilizationhash.get("minPing");
            }
            // Ftpmonitor_realtimeDao realDao=new Ftpmonitor_realtimeDao();
            // List curList=realDao.getByFTPId(queryid);
            // Ftpmonitor_realtime ftpReal=new Ftpmonitor_realtime();
            // ftpReal=(Ftpmonitor_realtime) curList.get(0);
            // int ping=ftpReal.getIs_canconnected();
            // if (ping==1) {
            // curPing="100";
            // }else{
            // curPing="0";
            // }
            if (ConnectUtilizationhash.get("curPing") != null) {
                curPing = (String) ConnectUtilizationhash.get("curPing"); // 取当前连通率可直接从
                // nms_ftp_history表获取,没必要再从nms_ftp_realtime表获取
            }

            // 画图----------------------
            String timeType = "minute";
            PollMonitorManager pollMonitorManager = new PollMonitorManager();
            pollMonitorManager.chooseDrawLineType(timeType,
                    ConnectUtilizationhash, "连通率", newip + "pingConnect", 740,
                    150);

            // 画图-----------------------------
            reporthash.put("servicename", initconf.getName());
            reporthash.put("Ping", curPing);
            reporthash.put("ip", ip);
            reporthash.put("ping", ConnectUtilizationhash);
            reporthash.put("starttime", startdate);
            reporthash.put("totime", startdate);
            request.setAttribute("id", String.valueOf(queryid));
            request.setAttribute("pingmax", minPing);// 最小连通率(pingmax 暂时定义)
            request.setAttribute("Ping", curPing);
            request.setAttribute("avgpingcon", pingconavg);
            request.setAttribute("newip", newip);
            request.setAttribute("ipaddress", ip);
            request.setAttribute("startdate", startdate);
            request.setAttribute("todate", todate);
            request.setAttribute("type", "FTP");
            session.setAttribute("reporthash", reporthash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/capreport/service/showPingReport.jsp";
    }

    private String showCompositeReport() {
        Date d = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = getParaValue("startdate");
        Hashtable reporthash = new Hashtable();
        if (startdate == null) {
            startdate = sdf0.format(d);
        }
        String todate = getParaValue("todate");
        if (todate == null) {
            todate = sdf0.format(d);
        }
        String starttime = startdate + " 00:00:00";
        String totime = todate + " 23:59:59";

        String newip = "";
        String ip = "";
        Integer queryid = getParaIntValue("id");
        FTPConfig initconf = null;
        List<String> infoList = new ArrayList<String>();
        FTPConfigDao configdao = new FTPConfigDao();
        try {
            initconf = (FTPConfig) configdao.findByID(String.valueOf(queryid));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }
        try {
            ip = getParaValue("ipaddress");

            newip = SysUtil.doip(ip);
            String runmodel = PollingEngine.getCollectwebflag();

            Ftpmonitor_historyDao historydao = new Ftpmonitor_historyDao();

            Hashtable ConnectUtilizationhash = historydao.getPingDataById(
                    queryid, starttime, totime);
            String curPing = "";
            String pingconavg = "";
            if (ConnectUtilizationhash.get("avgPing") != null)
                pingconavg = (String) ConnectUtilizationhash.get("avgPing");
            String minPing = "";

            if (ConnectUtilizationhash.get("minPing") != null) {
                minPing = (String) ConnectUtilizationhash.get("minPing");
            }
            // Ftpmonitor_realtimeDao realDao=new Ftpmonitor_realtimeDao();
            // List curList=realDao.getByFTPId(queryid);
            // Ftpmonitor_realtime ftpReal=new Ftpmonitor_realtime();
            // ftpReal=(Ftpmonitor_realtime) curList.get(0);
            // int ping=ftpReal.getIs_canconnected();
            // if (ping==1) {
            // curPing="100";
            // }else{
            // curPing="0";
            // }
            if (ConnectUtilizationhash.get("curPing") != null) {
                curPing = (String) ConnectUtilizationhash.get("curPing"); // 取当前连通率可直接从
                // nms_ftp_history表获取,没必要再从nms_ftp_realtime表获取
            }

            // 画图----------------------
            String timeType = "minute";
            PollMonitorManager pollMonitorManager = new PollMonitorManager();
            pollMonitorManager.chooseDrawLineType(timeType,
                    ConnectUtilizationhash, "连通率", newip + "pingConnect", 740,
                    150);

            // 画图-----------------------------
            if (initconf != null) {
                String name = initconf.getName();
                String type = "       类型: 端口服务监视";
                ip = initconf.getIpaddress();
                String file = initconf.getFilename();
                infoList.add("名称: " + name);
                infoList.add(type);
                infoList.add("      测试文件: " + file);
                infoList.add("      IP地址: " + ip);

            }
            reporthash.put("servicename", initconf.getName());
            reporthash.put("Ping", curPing);
            reporthash.put("ip", ip);
            reporthash.put("ping", ConnectUtilizationhash);
            reporthash.put("starttime", startdate);
            reporthash.put("totime", startdate);
            reporthash.put("type", "FTP");
            reporthash.put("comInfo", infoList);
            request.setAttribute("id", String.valueOf(queryid));
            request.setAttribute("pingmax", minPing);// 最小连通率(pingmax 暂时定义)
            request.setAttribute("Ping", curPing);
            request.setAttribute("avgpingcon", pingconavg);
            request.setAttribute("newip", newip);
            request.setAttribute("ipaddress", ip);
            request.setAttribute("startdate", startdate);
            request.setAttribute("todate", todate);
            request.setAttribute("type", "FTP");
            session.setAttribute("reporthash", reporthash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/capreport/service/showServiceCompositeReport.jsp";
    }

    private String showServiceEventReport() {
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
        String ipaddress = getParaValue("ipaddress");
        String id = getParaValue("id");
        request.setAttribute("ipaddress", ipaddress);
        Date d = new Date();
        String startdate = getParaValue("startdate");
        if (startdate == null) {
            startdate = sdf0.format(d);
        }
        String todate = getParaValue("todate");
        if (todate == null) {
            todate = sdf0.format(d);
        }
        String starttime = startdate + " 00:00:00";
        String totime = todate + " 23:59:59";

        List orderList = new ArrayList();
        FTPConfig initconf = null;
        FTPConfigDao configdao = new FTPConfigDao();
        try {
            initconf = (FTPConfig) configdao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }
        List infolist = null;
        List list = null;
        if (initconf != null) {

            // 事件列表

            int status = getParaIntValue("status");
            int level1 = getParaIntValue("level1");
            if (status == -1)
                status = 99;
            if (level1 == -1)
                level1 = 99;
            request.setAttribute("status", status);
            request.setAttribute("level1", level1);

            User user = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER); // 用户姓名
            EventListDao eventdao = new EventListDao();
            StringBuffer s = new StringBuffer();
            s.append("select * from system_eventlist where recordtime>= '"
                    + starttime + "' " + "and recordtime<='" + totime + "' ");
            s.append(" and nodeid=" + id);
            try {
                list = eventdao.getQuery(starttime, totime, "ftp", status + "",
                        level1 + "", user.getBusinessids(), initconf.getId());

                infolist = eventdao.findByCriteria(s.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                eventdao.close();
            }
            if (infolist != null && infolist.size() > 0) {
                int levelone = 0;
                int levletwo = 0;
                int levelthree = 0;

                for (int j = 0; j < infolist.size(); j++) {
                    EventList eventlist = (EventList) infolist.get(j);
                    if (eventlist.getContent() == null)
                        eventlist.setContent("");

                    if (eventlist.getLevel1() == 1) {
                        levelone = levelone + 1;
                    } else if (eventlist.getLevel1() == 2) {
                        levletwo = levletwo + 1;
                    } else if (eventlist.getLevel1() == 3) {
                        levelthree = levelthree + 1;
                    }

                }
                String servName = initconf.getName();
                String ip = initconf.getIpaddress();
                List<String> ipeventList = new ArrayList<String>();
                ipeventList.add(ip);
                ipeventList.add(servName);
                ipeventList.add((levelone + levletwo + levelthree) + "");
                ipeventList.add(levelone + "");
                ipeventList.add(levletwo + "");
                ipeventList.add(levelthree + "");

                orderList.add(ipeventList);

            }
        }
        Hashtable reporthash = new Hashtable();

        request.setAttribute("id", id);
        request.setAttribute("starttime", starttime);
        request.setAttribute("totime", totime);
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        request.setAttribute("eventlist", orderList);
        request.setAttribute("type", "FTP");
        request.setAttribute("list", list);
        reporthash.put("starttime", starttime);
        reporthash.put("starttime", starttime);
        reporthash.put("totime", totime);
        reporthash.put("eventlist", orderList);
        reporthash.put("list", list);
        session.setAttribute("reporthash", reporthash);
        return "/capreport/service/showServiceEventReport.jsp";

    }

    private String sychronizeData() {

        int queryid = getParaIntValue("id");
        NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
        List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
        Hashtable<String, Hashtable<String, NodeGatherIndicators>> urlHash = new Hashtable<String, Hashtable<String, NodeGatherIndicators>>();// 存放需要监视的DB2指标
        // <dbid:Hashtable<name:NodeGatherIndicators>>

        try {
            // 获取被启用的SOCKET所有被监视指标
            monitorItemList = indicatorsdao.getByNodeId(queryid + "", 1,
                    "service", "ftp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indicatorsdao.close();
        }
        if (monitorItemList == null)
            monitorItemList = new ArrayList<NodeGatherIndicators>();
        Hashtable gatherHash = new Hashtable();
        for (int i = 0; i < monitorItemList.size(); i++) {
            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList
                    .get(i);
            gatherHash
                    .put(nodeGatherIndicators.getName(), nodeGatherIndicators);
        }

        try {
            FTPDataCollector ftpcollector = new FTPDataCollector();
            ftpcollector.collect_data(queryid + "", gatherHash);
        } catch (Exception exc) {

        }
        return "/FTP.do?action=detail&id=" + queryid;
    }

    private String isOK() {

        int queryid = getParaIntValue("id");
        FTPConfigDao configdao = new FTPConfigDao();
        Calendar date = Calendar.getInstance();
        FTPConfig ftpConfig = null;
        try {
            ftpConfig = (FTPConfig) configdao.findByID(queryid + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }
        String reason = "服务有效";

        NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
        List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
        Hashtable<String, Hashtable<String, NodeGatherIndicators>> urlHash = new Hashtable<String, Hashtable<String, NodeGatherIndicators>>();// 存放需要监视的DB2指标
        // <dbid:Hashtable<name:NodeGatherIndicators>>

        try {
            // 获取被启用的FTP所有被监视指标
            monitorItemList = indicatorsdao.getByNodeId(queryid + "", 1,
                    "service", "ftp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            indicatorsdao.close();
        }
        if (monitorItemList == null)
            monitorItemList = new ArrayList<NodeGatherIndicators>();
        Hashtable gatherHash = new Hashtable();
        for (int i = 0; i < monitorItemList.size(); i++) {
            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) monitorItemList
                    .get(i);
            gatherHash
                    .put(nodeGatherIndicators.getName(), nodeGatherIndicators);
        }

        FtpUtil ftputil = new FtpUtil(ftpConfig.getIpaddress(), 21, ftpConfig
                .getUsername(), ftpConfig.getPassword(), "", ResourceCenter
                .getInstance().getSysPath()
                + "/ftpdownload/", ftpConfig.getFilename());

        boolean downloadflag = true;
        boolean uploadsuccess = true;
        try {
            if (gatherHash.containsKey("download")) {
                downloadflag = ftputil.ftpOne();
            }

            if (gatherHash.containsKey("upload")) {
                uploadsuccess = ftputil.uploadFile(ftpConfig.getIpaddress(),
                        ftpConfig.getUsername(), ftpConfig.getPassword(),
                        ResourceCenter.getInstance().getSysPath()
                                + "/ftpupload/ftpupload.txt");
            }

            if (gatherHash.containsKey("download")
                    && gatherHash.containsKey("upload")) {
                if (downloadflag && uploadsuccess) {
                    reason = "服务有效";
                } else {
                    if (downloadflag == true && uploadsuccess == false) {
                        reason = "上载服务无效,下载服务正常";
                    } else if (downloadflag == false && uploadsuccess == true) {
                        reason = "上载服务正常,下载服务无效";
                    } else {
                        reason = "FTP服务无效";
                    }
                }
            } else if (gatherHash.containsKey("download")) {
                // 只测试接收邮件服务
                if (downloadflag) {
                    reason = "下载服务正常";
                } else {
                    reason = "下载服务无效";
                }
            } else if (gatherHash.containsKey("upload")) {
                // 只测试发送邮件服务
                if (uploadsuccess) {
                    reason = "上载服务正常";
                } else {
                    reason = "上载服务无效";
                }
            }

        } catch (Exception e) {

        }
        request.setAttribute("isOK", reason);
        request.setAttribute("name", ftpConfig.getName());
        request.setAttribute("str", ftpConfig.getIpaddress());
        return "/tool/ftpisok.jsp";
    }

    private void drawPiechart(String[] keys, String[] values, String chname,
            String enname) {
        ChartGraph cg = new ChartGraph();
        DefaultPieDataset piedata = new DefaultPieDataset();
        for (int i = 0; i < keys.length; i++) {
            piedata.setValue(keys[i], new Double(values[i]).doubleValue());
        }
        cg.pie(chname, piedata, enname, 300, 120);
    }

    public String getF(String s) {
        if (s.length() > 5)
            s = s.substring(0, 5);
        return s;
    }

    private String allServiceList() {

        User operator = (User) session
                .getAttribute(SessionConstant.CURRENT_USER);
        String bids = operator.getBusinessids();
        String bid[] = bids.split(",");
        Vector rbids = new Vector();
        if (bid != null && bid.length > 0) {
            for (int i = 0; i < bid.length; i++) {
                if (bid[i] != null && bid[i].trim().length() > 0)
                    rbids.add(bid[i].trim());
            }
        }
        // ftp list
        FTPConfigDao ftpdao = new FTPConfigDao();
        List ftplist = null;
        try {
            if (operator.getRole() == 0) {
                ftplist = ftpdao.loadAll();
            } else
                ftplist = ftpdao.getFtpByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ftpdao.close();
        }
        request.setAttribute("ftplist", ftplist);

        // mail list
        EmailConfigDao emailConfigDao = new EmailConfigDao();
        List<EmailMonitorConfig> userEmailMonitorConfigList = new ArrayList<EmailMonitorConfig>();
        try {
            userEmailMonitorConfigList = (List<EmailMonitorConfig>) emailConfigDao
                    .getByBIDAndFlag(operator.getBusinessids(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            emailConfigDao.close();
        }

        request.setAttribute("emaillist", userEmailMonitorConfigList);

        // process list
        ProcsDao pdao = new ProcsDao();
        List prolist = null;
        try {
            prolist = pdao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pdao.close();
        }
        request.setAttribute("prolist", prolist);

        // web list
        WebConfigDao configdao = new WebConfigDao();
        List weblist = null;
        try {
            weblist = configdao.getWebByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configdao.close();
        }
        request.setAttribute("weblist", weblist);

        // port list
        PSTypeDao portdao = new PSTypeDao();
        List portlist = null;
        try {
            if (operator.getRole() == 0) {
                portlist = portdao.loadAll();
            } else
                portlist = portdao.getSocketByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            portdao.close();
        }
        if (portlist == null)
            portlist = new ArrayList();
        for (int i = 0; i < portlist.size(); i++) {
            PSTypeVo vo = (PSTypeVo) portlist.get(i);
            Node socketNode = PollingEngine.getInstance().getSocketByID(
                    vo.getId());
            if (socketNode == null)
                vo.setStatus(0);
            else
                vo.setStatus(socketNode.getStatus());
        }
        request.setAttribute("portlist", portlist);

        return "/application/ftp/servicelist.jsp";
    }

    /**
     * list:
     * <p>
     * 获取列表信息，返回列表页面
     * 
     * @return {@link String} - 列表页面
     * 
     * @since v1.01
     */
    private String list() {
        String bidSql = getBidSql("bid");
        FTPConfigDao dao = new FTPConfigDao();
        try {
            if (bidSql == null) {
                // 如果为 null 则为超级用户，不需要检查 bid
                list(dao);
            } else if (bidSql.trim().length() > 0) {
                list(dao, "where " + bidSql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/application/ftp/list.jsp";
    }

    public List<FTPConfig> getAllFTPConfigList() {
        FTPConfigDao ftpConfigDao = null;
        List<FTPConfig> allFTPConfigList = null;
        try {
            ftpConfigDao = new FTPConfigDao();
            allFTPConfigList = (List<FTPConfig>) ftpConfigDao.loadAll();
        } catch (Exception e) {

        } finally {
            ftpConfigDao.close();
        }
        return allFTPConfigList;
    }

    /**
     * 根据用户的所属业务来提取FTPConfig列表
     * 
     * @param allFTPConfigList
     * @return
     */
    public List<FTPConfig> getFTPConfigListByUser(List<FTPConfig> ftpConfigList) {

        User operator = (User) session
                .getAttribute(SessionConstant.CURRENT_USER);
        String businessids = operator.getBusinessids();
        List<FTPConfig> userFTPConfigList = new ArrayList<FTPConfig>();
        List<String> userBusinessidList = getBusinessidList(businessids);

        // 如果用户所属的业务id列表 包含 FTPConfig所属的业务 则显示
        if (userBusinessidList != null && userBusinessidList.size() > 0) {
            for (int i = 0; i < ftpConfigList.size(); i++) {
                String FTPConfigbids = ftpConfigList.get(i).getBid();
                List<String> ftpConfigBusinessidList = getBusinessidList(FTPConfigbids);
                for (int j = 0; j < userBusinessidList.size(); j++) {
                    if (ftpConfigBusinessidList.contains(userBusinessidList
                            .get(j))) {
                        userFTPConfigList.add(ftpConfigList.get(i));
                        break;
                    }
                }

            }
        }
        return userFTPConfigList;
    }

    public List<FTPConfig> getFTPConfigListByMonflag(Integer flag) {
        FTPConfigDao ftpConfigDao = null;
        List<FTPConfig> ftpConfigList = null;
        try {
            ftpConfigDao = new FTPConfigDao();
            ftpConfigList = (List<FTPConfig>) ftpConfigDao
                    .getFTPConfigListByMonFlag(flag);
        } catch (Exception e) {

        } finally {
            ftpConfigDao.close();
        }
        return ftpConfigList;
    }

    /**
     * 将业务id的字符串 拆分成一个业务id的列表
     * 
     * @param businessids
     * @return
     */
    private List<String> getBusinessidList(String businessids) {
        String bid[] = businessids.split(",");
        List<String> businessidList = new ArrayList<String>();
        if (bid != null && bid.length > 0) {
            for (int i = 0; i < bid.length; i++) {
                // 去掉空白字符串
                if (bid[i] != null && bid[i].trim().length() > 0)
                    businessidList.add(bid[i].trim());
            }
        }
        return businessidList;
    }

    /**
     * snow 增加前将供应商查找到
     * 
     * @return
     * @date 2010-5-21
     */
    private String add() {
        return "/application/ftp/add.jsp";
    }

    /**
     * save:
     * <p>保存 FTP
     * 
     * @return  {@link String}
     *          - 保存 FTP 后返回至列表页面
     * 
     * @since v1.01
     */
    private String save() {
        boolean result = false;
        FTPConfig ftpConfig = createFTPConfig();
        ftpConfig.setId(KeyGenerator.getInstance().getNextKey());
        FTPConfigDao ftpConfigDao = new FTPConfigDao();
        try {
            result = ftpConfigDao.save(ftpConfig);
            if (result) {
                NodeService service = new NodeService();
                service.addNode(ftpConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            ftpConfigDao.close();
        }
        return list();
    }

    /**
     * 删除 FTPConfig
     * 
     * @return
     */
    private String delete() {
        String[] ids = getParaArrayValue("checkbox");
        FTPConfigDao ftpConfigDao = new FTPConfigDao();
        try {
            if (ids != null && ids.length > 0) {
                NodeService nodeService = new NodeService();
                for (String id : ids) {
                    FTPConfig node = (FTPConfig) ftpConfigDao.findByID(id);
                    if (node != null) {
                        nodeService.deleteNode(node);
                    }
                }
                ftpConfigDao.delete(ids);
            }
        } catch (Exception ex) {
            logger.error("FTPManager.delete()", ex);
        } finally {
            ftpConfigDao.close();
        }
        return list();
    }

    /**
     * 发送更新 FTPConfig 的页面
     * 
     * @return
     */
    private String edit() {
        FTPConfigDao ftpConfigdao = new FTPConfigDao();
        String targetJsp = "/application/ftp/edit.jsp";
        try {
            setTarget(targetJsp);
            readyEdit(ftpConfigdao);
        } catch (Exception ex) {
            logger.error("FTPManager.edit()", ex);
        } finally {
            ftpConfigdao.close();
        }
        return targetJsp;
    }

    /**
     * 更新 FTPConfig
     * 
     * @return
     */
    private String update() {
        FTPConfig vo = createFTPConfig();
        int id = getParaIntValue("id");
        vo.setId(id);
        boolean result = false;
        FTPConfigDao ftpConfigDao = new FTPConfigDao();
        try {
            result = ftpConfigDao.update(vo);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        } finally {
            ftpConfigDao.close();
        }
        if (result) {
            return list();
        } else {
            return "/application/ftp/savefail.jsp";
        }

    }

    /**
     * 根据页面的参数来创建 FTPConfig
     * 
     * @return
     */
    private FTPConfig createFTPConfig() {

        FTPConfig ftpConfig = new FTPConfig();

        String name = getParaValue("name");
        String username = getParaValue("username");
        String password = getParaValue("password");
        
        String ipaddress = getParaValue("ipaddress");
        String remotePath = getParaValue("remotePath");
        int port = getParaIntValue("port");
        String filename = getParaValue("filename");
        int timeout = getParaIntValue("timeout");
        int monflag = getParaIntValue("monflag");
        String sendmobiles = getParaValue("sendmobiles");
        String sendemail = getParaValue("sendemail");
        String sendphone = getParaValue("sendphone");
        String bid = getParaValue("bid");

        ftpConfig.setId(getParaIntValue("id"));
        ftpConfig.setName(name);
        ftpConfig.setUsername(username);
        ftpConfig.setPassword(password);
        ftpConfig.setTimeout(timeout);
        ftpConfig.setMonflag(monflag);
        ftpConfig.setIpaddress(ipaddress);
        ftpConfig.setFilename(filename);
        ftpConfig.setRemotePath(remotePath);
        ftpConfig.setPort(port);
        ftpConfig.setSendmobiles(sendmobiles);
        ftpConfig.setSendemail(sendemail);
        ftpConfig.setSendphone(sendphone);
        ftpConfig.setSupperid(getParaIntValue("supperid"));
        ftpConfig.setBid(bid);

        return ftpConfig;
    }

    /**
     * 修改 FTPConfig 的监视信息之后返回FTPConfig列表页面
     * 
     * @return
     */
    private String changeMonflag() {
        boolean result = false;
        FTPConfig ftpConfig = new FTPConfig();
        FTPConfigDao ftpConfigDao = null;
        try {
            String id = getParaValue("id");
            int monflag = getParaIntValue("value");
            ftpConfigDao = new FTPConfigDao();
            ftpConfig = (FTPConfig) ftpConfigDao.findByID(id);
            ftpConfig.setMonflag(monflag);
            result = ftpConfigDao.update(ftpConfig);
            Ftp ftp = (Ftp) PollingEngine.getInstance().getFtpByID(
                    Integer.parseInt(id));
            ftp.setMonflag(monflag);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            ftpConfigDao.close();
        }
        if (result) {
            return list();
        } else {
            return "/application/ftp/savefail.jsp";
        }
    }

    private String midalllist() {

        User operator = (User) session
                .getAttribute(SessionConstant.CURRENT_USER);
        String bids = operator.getBusinessids();
        String bid[] = bids.split(",");
        Vector rbids = new Vector();
        if (bid != null && bid.length > 0) {
            for (int i = 0; i < bid.length; i++) {
                if (bid[i] != null && bid[i].trim().length() > 0)
                    rbids.add(bid[i].trim());
            }
        }

        // Tomcat
        TomcatDao tomcatdao = new TomcatDao();
        List tomcatlist = null;
        try {
            if (operator.getRole() == 0) {
                tomcatlist = tomcatdao.loadAll();
            } else {
                
            }
                //tomcatlist = tomcatdao.f(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tomcatdao.close();
        }
        if (tomcatlist == null)
            tomcatlist = new ArrayList();
        for (int i = 0; i < tomcatlist.size(); i++) {
            Tomcat tomcatvo = (Tomcat) tomcatlist.get(i);
            Node tomcatNode = PollingEngine.getInstance().getTomcatByID(
                    tomcatvo.getId());
            if (tomcatNode == null)
                tomcatvo.setStatus(0);
            else
                tomcatvo.setStatus(tomcatNode.getStatus());
        }

        // mq
        MQConfigDao mqconfigdao = new MQConfigDao();
        List mqlist = null;
        try {
            if (operator.getRole() == 0) {
                mqlist = mqconfigdao.loadAll();
            } else
                mqlist = mqconfigdao.getMQByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mqconfigdao.close();
        }
        if (mqlist == null)
            mqlist = new ArrayList();
        for (int i = 0; i < mqlist.size(); i++) {
            MQConfig mqvo = (MQConfig) mqlist.get(i);
            Node mqNode = PollingEngine.getInstance().getMqByID(mqvo.getId());
            if (mqNode == null)
                mqvo.setStatus(0);
            else
                mqvo.setStatus(mqNode.getStatus());
        }
        // domimo
        DominoConfigDao dominoconfigdao = new DominoConfigDao();
        List dominolist = new ArrayList();
        try {
            dominolist = dominoconfigdao.getDominoByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dominoconfigdao.close();
        }

        // was
        WasConfigDao wasconfigdao = new WasConfigDao();
        List waslist = null;
        try {
            waslist = wasconfigdao.getWasByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wasconfigdao.close();
        }

        // weblogic
        WeblogicConfigDao weblogicconfigdao = new WeblogicConfigDao();
        List weblogiclist = null;
        try {
            if (operator.getRole() == 0) {
                weblogiclist = weblogicconfigdao.loadAll();
            } else
                weblogiclist = weblogicconfigdao.getWeblogicByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            weblogicconfigdao.close();
        }
        // IIS
        IISConfigDao iisconfigdao = new IISConfigDao();
        List iislist = new ArrayList();
        try {
            iislist = iisconfigdao.getIISByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iisconfigdao.close();
        }

        // CICS
        CicsConfigDao cicsconfigdao = new CicsConfigDao();
        List cicslist = new ArrayList();
        try {
            cicslist = cicsconfigdao.getCicsByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cicsconfigdao.close();
        }
        // DNS
        DnsConfigDao dnsconfigdao = new DnsConfigDao();
        List dnslist = null;
        try {
            dnslist = dnsconfigdao.getDnsByBID(rbids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dnsconfigdao.close();
        }

        request.setAttribute("dnslist", dnslist);
        request.setAttribute("cicslist", cicslist);
        request.setAttribute("iislist", iislist);
        request.setAttribute("weblogiclist", weblogiclist);
        request.setAttribute("waslist", waslist);
        request.setAttribute("tomcatlist", tomcatlist);
        request.setAttribute("mqlist", mqlist);
        request.setAttribute("dominolist", dominolist);
        return "/application/ftp/midalllist.jsp";
    }

}