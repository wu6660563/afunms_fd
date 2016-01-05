package com.afunms.config.manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.dao.AlarmWayDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.capreport.common.DateTime;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.JspPage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.HaweitelnetconfAuditDao;
import com.afunms.config.dao.HaweitelnetconfDao;
import com.afunms.config.dao.Hua3VPNFileConfigDao;
import com.afunms.config.dao.PasswdTimingBackupTelnetConfigDao;
import com.afunms.config.dao.TimingBackupTelnetConfigDao;
import com.afunms.config.model.ConfiguringDevice;
import com.afunms.config.model.HaweitelnetconfAudit;
import com.afunms.config.model.Hua3VPNFileConfig;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.config.model.PasswdTimingBackupTelnetConfig;
import com.afunms.config.model.TimingBackupTelnetConfig;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.manage.PollMonitorManager;
import com.afunms.polling.telnet.CiscoTelnet;
import com.afunms.polling.telnet.Huawei3comvpn;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

public class HaweitelnetconfManager extends BaseManager implements ManagerInterface {
    static StringBuffer result = new StringBuffer();
    public String execute(String action) {
        //System.out.println("action====================" + action);

        // TODO Auto-generated method stub
        if (action.equals("list")) {
            return list();
        }
        if (action.equals("passwdList")) {
            return passwdList();
        }

        if (action.equals("configlist")) {
            return configlist();
        }

        if (action.equals("ready_add"))// list.jspҳ�� ���
        {
            return readyAdd();
        }
        if (action.equals("add")) // list.jspҳ��->���->��Ӱ�ť
        {
            return add();
        }
        if (action.equals("delete")) {
            return delete();
        }
        if (action.equals("update")) {
            return update();
        }
        if (action.equals("ready_edit")) {// list.jspҳ�� �Ҽ��˵� �༭
            DaoInterface dao = new HaweitelnetconfDao();
            setTarget("/config/vpntelnet/edit.jsp");
            return readyEdit(dao);
        }
        if (action.equals("findip")) {
            return findip();
        }
        if (action.equals("netip")) {
            return netip();
        }
        if (action.equals("ipmenu")) {
            return ipmenu();
        }
        if (action.equals("add_pg")) {
            return add_pg();
        }
        if (action.equals("ready_edit_gp")) {
            DaoInterface dao = new HaweitelnetconfDao();
            setTarget("/config/vpntelnet/edit_pg.jsp");
            return readyEdit(dao);
        }
        if (action.equals("bkpCfg"))// �Ҽ��˵�->����->���ݰ�ť
        {
            // return bkpCfg();
            return bkpCfg_1();
        }
        if (action.equals("detailPage_readybkpCfg")) {
            return detailPageReadyBackupConfig();
        }
        if (action.equals("readySetupConfig"))// list.jspҳ�� �Ҽ��˵� ������
        {
            return readySetupConfig();
        }
        if (action.equals("readyBackupConfig"))// list.jspҳ�� �Ҽ��˵� ����
        {
            return readyBackupConfig();
        }
        if (action.equals("setupConfig")) {
            return setupConfig_1();
        }
        if (action.equals("download")) {
            return download();
        }
        if (action.equals("deviceList")) {
            return deviceList();
        }
        if (action.equals("readyTelnetModify"))// list.jspҳ�� �Ҽ��˵� �޸�����
        {
            return readyTelnetModify();
        }
        if (action.equals("updatepassword")) {
            return updatepassword();
        }
        if (action.equals("readyuploadCfgFile"))// list.jspҳ�� �Ҽ��˵� �ϴ�����
        {
            return readyuploadCfgFile();
        }
        if (action.equals("uploadFile"))// list.jspҳ��->�Ҽ��˵�->�ϴ�����->�ϴ���ť
        {
            return uploadFile();
        }
        if (action.equals("synchronizeData")) {
            return synchronizeData();
        }
        if (action.equals("ready_addForBatch")) {
            return ready_addForBatch();
        }
        if (action.equals("multi_netip")) {
            return multi_netip();
        }
        if (action.equals("batchAdd")) {
            return batchAdd();
        }
        if (action.equals("ready_multi_modify"))// list.jspҳ�� �����޸�����
        {
            return ready_multi_modify1();
        }
        if (action.equals("ready_multi_audit"))// list.jspҳ�� �����Ϣ
        {
            return ready_multi_audit();
        }
        if (action.equals("queryByCondition"))// list.jspҳ�� �����Ϣ
        {
            return queryByCondition();
        }
        if (action.equals("multi_modify"))// list.jspҳ��->�����޸�����->�޸İ�ť
        {
            return modifyTelnetPasswordForBatch();
        }
        if (action.equals("ready_backupForBatch"))// list.jspҳ�� ��������
        {
            return ready_backupForBatch();
        }
        if (action.equals("bkpCfg_forBatch"))// list.jspҳ��->��������->���ݰ�ť
        {
            return bkpCfg_forBatch();
        }
        if (action.equals("ready_deployCfgForBatch"))// list.jspҳ��->����Ӧ��
        {
            return ready_deployCfgForBatch();
        }
        if (action.equals("deployCfgForBatch"))// list.jspҳ��->����Ӧ��->����Ӧ�ð�ť
        {
            return deployCfgForBatch();
        }
        if (action.equals("ready_timingBackup")) {// ��ʱ����ҳ��
            return ready_timingBackup();
        }
        if (action.equals("timingBackup")) {// ��ʱ���ݶ���
            return timingBackup();
        }
        if (action.equals("multi_telnet_netip")) {// Զ�̵�¼���豸�б�
            return multi_telnet_netip();
        }
        if (action.equals("deleteTimingBackupTelnetConfig")) {// ɾ����ʱ���ݵ�����
            return deleteTimingBackupTelnetConfig();
        }
        if (action.equals("addTimingBackupTelnetConfig")) {
            return addTimingBackupTelnetConfig();// ���Ӷ�ʱ���������ļ�������
        }
        if (action.equals("ready_editTimingBackupTelnetConfig")) {
            return ready_editTimingBackupTelnetConfig();// �༭��ʱ���������ļ�������
        }
        if (action.equals("modifyTimingBackup")) {
            return modifyTimingBackup();
        }
        if (action.equals("addBackup")) {// ��Ӷ�ʱ����
            return addBackup();
        }
        if (action.equals("addPasswdBackup")) {// ��Ӷ�ʱ����
            return addPasswdBackup();
        }
        if (action.equals("disBackup")) {// ȡ����ʱ����
            return disBackup();
        }
        if (action.equals("disPasswdBackup")) {// ȡ����ʱ����
            return disPasswdBackup();
        }
        if (action.equals("serverip")) {
            return serverip();
        }
        if (action.equals("ready_addPasswd")) {
            return ready_addPasswd();
        }
        if (action.equals("passwdTimingBackup")) {
            return modifyPasswdTimingBackup();
        }
        if (action.equals("passwdEditTimingBackup")) {
            return modifyEditPasswdTimingBackup();
        }
        if (action.equals("multi_warn_netip")) {
            return multi_warn_netip();
        }
        if (action.equals("deletePasswdTimingBackupTelnetConfig")) {
            return deletePasswdTimingBackupTelnetConfig();
        }
        if (action.equals("ready_editPasswdTimingBackupTelnetConfig")) {
            return ready_editPasswdTimingBackupTelnetConfig();
        }
        if (action.equals("chooselist")) {
            return chooselist();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }
    
    private String ready_addPasswd(){
        return "/config/vpntelnet/addPasswdTimingBackup.jsp";
    }
    
    private String modifyPasswdTimingBackup() {
        PasswdTimingBackupTelnetConfigDao passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        PasswdTimingBackupTelnetConfig passwdTimingBackupTelnetConfig = new PasswdTimingBackupTelnetConfig();
        String ipaddress = getParaValue("ipaddress");
        String warntype = getParaValue("way-name");
        String backup_fileName = getParaValue("filename");
        String backup_type = getParaValue("type");
        String[] sendtimemonth = request.getParameterValues("sendtimemonth");
        String[] sendtimeweek = request.getParameterValues("sendtimeweek");
        String[] sendtimeday = request.getParameterValues("sendtimeday");
        String[] sendtimehou = request.getParameterValues("sendtimehou");
        String[] transmitfrequency = request.getParameterValues("transmitfrequency");
        String status = getParaValue("status");
        List<PasswdTimingBackupTelnetConfig> passwdTimingBackupTelnetConfigList = null;
        SysLogger.info("ipaddress===" + ipaddress);
        DateTime dt = new DateTime();
        try {
            passwdTimingBackupTelnetConfig.setTelnetconfigids(ipaddress);
            passwdTimingBackupTelnetConfig.setWarntype(warntype);
            passwdTimingBackupTelnetConfig.setBackup_filename(backup_fileName);
            passwdTimingBackupTelnetConfig.setBackup_type(backup_type);
            passwdTimingBackupTelnetConfig.setBackup_sendfrequency(arrayToString(transmitfrequency).substring(1, arrayToString(transmitfrequency).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_month(arrayToString(sendtimemonth).substring(1, arrayToString(sendtimemonth).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_week(arrayToString(sendtimeweek).substring(1, arrayToString(sendtimeweek).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_day(arrayToString(sendtimeday).substring(1, arrayToString(sendtimeday).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_hou(arrayToString(sendtimehou).substring(1, arrayToString(sendtimehou).length()-1));
            // timingBackupTelnetConfig.setBackup_date(Integer.parseInt(dt.getMyDateTime(DateTime.Datetime_Format_14)));
            passwdTimingBackupTelnetConfig.setStatus(status);
            passwdTimingBackupTelnetConfigDao.save(passwdTimingBackupTelnetConfig);
            passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
            passwdTimingBackupTelnetConfigList = passwdTimingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            passwdTimingBackupTelnetConfigDao.close();
        }
        request.setAttribute("list", passwdTimingBackupTelnetConfigList);
        return "/config/vpntelnet/device_list.jsp";
    }

    
    //�޸�
    private String modifyEditPasswdTimingBackup() {
        PasswdTimingBackupTelnetConfigDao passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        PasswdTimingBackupTelnetConfig passwdTimingBackupTelnetConfig = new PasswdTimingBackupTelnetConfig();
        String ipaddress = getParaValue("ipaddress");
        String warntype = getParaValue("way-name");
        String backup_fileName = getParaValue("filename");
        String backup_type = getParaValue("type");
        String[] sendtimemonth = request.getParameterValues("sendtimemonth");
        String[] sendtimeweek = request.getParameterValues("sendtimeweek");
        String[] sendtimeday = request.getParameterValues("sendtimeday");
        String[] sendtimehou = request.getParameterValues("sendtimehou");
        String[] transmitfrequency = request.getParameterValues("transmitfrequency");
        String status = getParaValue("status");
        int id = Integer.parseInt(getParaValue("id"));
        List<PasswdTimingBackupTelnetConfig> passwdTimingBackupTelnetConfigList = null;
        SysLogger.info("ipaddress===" + ipaddress);
        DateTime dt = new DateTime();
        try {
            passwdTimingBackupTelnetConfig.setTelnetconfigids(ipaddress);
            passwdTimingBackupTelnetConfig.setWarntype(warntype);
            passwdTimingBackupTelnetConfig.setBackup_filename(backup_fileName);
            passwdTimingBackupTelnetConfig.setBackup_type(backup_type);
            passwdTimingBackupTelnetConfig.setBackup_sendfrequency(arrayToString(transmitfrequency).substring(1, arrayToString(transmitfrequency).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_month(arrayToString(sendtimemonth).substring(1, arrayToString(sendtimemonth).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_week(arrayToString(sendtimeweek).substring(1, arrayToString(sendtimeweek).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_day(arrayToString(sendtimeday).substring(1, arrayToString(sendtimeday).length()-1));
            passwdTimingBackupTelnetConfig.setBackup_time_hou(arrayToString(sendtimehou).substring(1, arrayToString(sendtimehou).length()-1));
            // timingBackupTelnetConfig.setBackup_date(Integer.parseInt(dt.getMyDateTime(DateTime.Datetime_Format_14)));
            passwdTimingBackupTelnetConfig.setStatus(status);
            passwdTimingBackupTelnetConfig.setId(id);
            passwdTimingBackupTelnetConfigDao.update(passwdTimingBackupTelnetConfig);
            passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
            passwdTimingBackupTelnetConfigList = passwdTimingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            passwdTimingBackupTelnetConfigDao.close();
        }
        request.setAttribute("list", passwdTimingBackupTelnetConfigList);
        return "/config/vpntelnet/device_list.jsp";
    }
    
    private String addBackup() {
        String id = getParaValue("id");
        List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = null;
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        try {
            timingBackupTelnetConfigDao.updateStatus("1", id);
            timingBackupTelnetConfigList = timingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("timingBackupTelnetConfigList", timingBackupTelnetConfigList);
        return "/config/vpntelnet/timingBackup.jsp";
    }
    
    private String addPasswdBackup() {
        String id = getParaValue("id");
        List<PasswdTimingBackupTelnetConfig> passwdTimingBackupTelnetConfigList = null;
        PasswdTimingBackupTelnetConfigDao PasswdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        try {
            PasswdTimingBackupTelnetConfigDao.updateStatus("��", id);
            passwdTimingBackupTelnetConfigList = PasswdTimingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PasswdTimingBackupTelnetConfigDao.close();
        }
        request.setAttribute("list", passwdTimingBackupTelnetConfigList);
        return "/config/vpntelnet/device_list.jsp";
    }
    
    private String disBackup() {
        String id = getParaValue("id");
        List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = null;
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        try {
            timingBackupTelnetConfigDao.updateStatus("0", id);
            timingBackupTelnetConfigList = timingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("timingBackupTelnetConfigList", timingBackupTelnetConfigList);
        return "/config/vpntelnet/timingBackup.jsp";
    }
    
    private String disPasswdBackup() {
        String id = getParaValue("id");
        List<PasswdTimingBackupTelnetConfig> passwdTimingBackupTelnetConfigList = null;
        PasswdTimingBackupTelnetConfigDao PasswdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        try {
            PasswdTimingBackupTelnetConfigDao.updateStatus("��", id);
            passwdTimingBackupTelnetConfigList = PasswdTimingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PasswdTimingBackupTelnetConfigDao.close();
        }
        request.setAttribute("list", passwdTimingBackupTelnetConfigList);
        return "/config/vpntelnet/device_list.jsp";
    }

    /**
     * �޸Ķ�ʱ��������
     * 
     * @return
     */
    private String modifyTimingBackup() {
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        TimingBackupTelnetConfig timingBackupTelnetConfig = new TimingBackupTelnetConfig();
        String ipaddress = getParaValue("ipaddress");
        String[] sendtimemonth = request.getParameterValues("sendtimemonth");
        String[] sendtimeweek = request.getParameterValues("sendtimeweek");
        String[] sendtimeday = request.getParameterValues("sendtimeday");
        String[] sendtimehou = request.getParameterValues("sendtimehou");
        String transmitfrequency = request.getParameter("transmitfrequency");
        String status = getParaValue("status");
        String id = getParaValue("id");
        List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = null;
        SysLogger.info("ipaddress===" + ipaddress);
        DateTime dt = new DateTime();
        try {
            timingBackupTelnetConfig.setId(Integer.parseInt(id));
            timingBackupTelnetConfig.setTelnetconfigids(ipaddress);
            timingBackupTelnetConfig.setBackup_sendfrequency(Integer.parseInt(transmitfrequency));
            timingBackupTelnetConfig.setBackup_time_month(arrayToString(sendtimemonth));
            timingBackupTelnetConfig.setBackup_time_week(arrayToString(sendtimeweek));
            timingBackupTelnetConfig.setBackup_time_day(arrayToString(sendtimeday));
            timingBackupTelnetConfig.setBackup_time_hou(arrayToString(sendtimehou));
            // timingBackupTelnetConfig.setBackup_date(Integer.parseInt(dt.getMyDateTime(DateTime.Datetime_Format_14)));
            timingBackupTelnetConfig.setStatus(status);
            timingBackupTelnetConfigDao.update(timingBackupTelnetConfig);
            timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
            timingBackupTelnetConfigList = timingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("list", timingBackupTelnetConfigList);
        return "/config/vpntelnet/timingBackup.jsp";
    }

    /**
     * �༭��ʱ���������ļ�������
     * 
     * @return
     */
    private String ready_editTimingBackupTelnetConfig() {
        String id = getParaValue("id");
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        TimingBackupTelnetConfig timingBackupTelnetConfig = null;
        try {
            timingBackupTelnetConfig = (TimingBackupTelnetConfig) timingBackupTelnetConfigDao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("id", id);
        request.setAttribute("timingBackupTelnetConfig", timingBackupTelnetConfig);
        return "/config/vpntelnet/editTimingBackup.jsp";
    }
    
    private String ready_editPasswdTimingBackupTelnetConfig() {
        String id = getParaValue("id");
        PasswdTimingBackupTelnetConfigDao passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        PasswdTimingBackupTelnetConfig passwdTimingBackupTelnetConfig = null;
        try {
            passwdTimingBackupTelnetConfig = (PasswdTimingBackupTelnetConfig) passwdTimingBackupTelnetConfigDao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            passwdTimingBackupTelnetConfigDao.close();
        }
        request.setAttribute("id", id);
        request.setAttribute("list", passwdTimingBackupTelnetConfig);
        return "/config/vpntelnet/editPasswdTimingBackup.jsp";
    }
    
    public String chooselist(){
        String jsp = "/config/vpntelnet/chooselist.jsp";
        String alarmWayIdEvent = getParaValue("alarmWayIdEvent");
        String alarmWayNameEvent = getParaValue("alarmWayNameEvent");
        try {
            List list = getList();
            request.setAttribute("list", list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request.setAttribute("alarmWayIdEvent", alarmWayIdEvent);
        request.setAttribute("alarmWayNameEvent", alarmWayNameEvent);
        return jsp;
    }

    /**
     * ���Ӷ�ʱ���������ļ�������
     * 
     * @return
     */
    private String addTimingBackupTelnetConfig() {
        return "/config/vpntelnet/addTimingBackup.jsp";
    }
    /**
     * ת��ʱ����ҳ��
     * 
     * @return
     */
    private String ready_timingBackup() {
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = null;
        try {
            timingBackupTelnetConfigList = timingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("timingBackupTelnetConfigList", timingBackupTelnetConfigList);
        return "/config/vpntelnet/timingBackup.jsp";
    }

    /**
     * ��ʱ����
     * 
     * @return
     */
    private String timingBackup() {
        TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
        TimingBackupTelnetConfig timingBackupTelnetConfig = new TimingBackupTelnetConfig();
        String ipaddress = getParaValue("ipaddress");
        String[] sendtimemonth = request.getParameterValues("sendtimemonth");
        String[] sendtimeweek = request.getParameterValues("sendtimeweek");
        String[] sendtimeday = request.getParameterValues("sendtimeday");
        String[] sendtimehou = request.getParameterValues("sendtimehou");
        String transmitfrequency = request.getParameter("transmitfrequency");
        String status = getParaValue("status");
        List<TimingBackupTelnetConfig> timingBackupTelnetConfigList = null;
        SysLogger.info("ipaddress===" + ipaddress);
        DateTime dt = new DateTime();
        try {
            timingBackupTelnetConfig.setTelnetconfigids(ipaddress);
            timingBackupTelnetConfig.setBackup_sendfrequency(Integer.parseInt(transmitfrequency));
            timingBackupTelnetConfig.setBackup_time_month(arrayToString(sendtimemonth));
            timingBackupTelnetConfig.setBackup_time_week(arrayToString(sendtimeweek));
            timingBackupTelnetConfig.setBackup_time_day(arrayToString(sendtimeday));
            timingBackupTelnetConfig.setBackup_time_hou(arrayToString(sendtimehou));
            timingBackupTelnetConfig.setBackup_date(Integer.parseInt(dt.getMyDateTime(DateTime.Datetime_Format_14)));
            timingBackupTelnetConfig.setStatus(status);
            timingBackupTelnetConfigDao.save(timingBackupTelnetConfig);
            timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
            timingBackupTelnetConfigList = timingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            timingBackupTelnetConfigDao.close();
        }
        request.setAttribute("timingBackupTelnetConfigList", timingBackupTelnetConfigList);
        return "/config/vpntelnet/timingBackup.jsp";
    }

    /**
     * ɾ����ʱ���������ļ�������
     * 
     * @return
     */
    private String deleteTimingBackupTelnetConfig() {
        String[] ids = getParaArrayValue("checkbox");
        if (ids != null) {
            if (ids != null && ids.length != 0) {
                TimingBackupTelnetConfigDao timingBackupTelnetConfigDao = new TimingBackupTelnetConfigDao();
                try {
                    for (String id : ids) {
                        if (id != null && !"".equals(id)) {
                            timingBackupTelnetConfigDao.delete(id);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    timingBackupTelnetConfigDao.close();
                }
            }
        }
        return ready_timingBackup();
    }
    
    private String deletePasswdTimingBackupTelnetConfig() {
        String[] ids = getParaArrayValue("checkbox");
        if (ids != null) {
            if (ids != null && ids.length != 0) {
                PasswdTimingBackupTelnetConfigDao passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
                try {
                    for (String id : ids) {
                        if (id != null && !"".equals(id)) {
                            passwdTimingBackupTelnetConfigDao.delete(id);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    passwdTimingBackupTelnetConfigDao.close();
                }
            }
        }
        return deviceList();
    }

    public static String splitDate(String item, String[] itemCh, String type) {
        String[] idValue = null;
        String value = "";
        idValue = new String[item.split("/").length];
        idValue = item.split("/");

        for (int i = 0; i < idValue.length; i++) {
            if (!idValue[i].equals("")) {
                if (type.equals("week")) {
                    value += itemCh[Integer.parseInt(idValue[i])];
                } else if (type.equals("day")) {
                    value += (idValue[i] + "�� ");
                } else if (type.equals("hour")) {
                    value += (idValue[i] + "ʱ ");
                } else {
                    value += itemCh[Integer.parseInt(idValue[i]) - 1];
                }
            }
        }

        return value;
    }

    public String arrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        if (array != null) {
            for (String value : array) {
                sb.append("/");
                sb.append(value);
            }
            sb.append("/");
        }
        return sb.toString();
    }

    // ��ʱִ��ҳ���У����ѡ���豸��ִ�и÷���
    private String multi_telnet_netip() {
        HaweitelnetconfDao haweitelnetconfDao = new HaweitelnetconfDao();
        List list = null;
        try {
            list = haweitelnetconfDao.getAllTelnetConfig();
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            haweitelnetconfDao.close();
        }
        request.setAttribute("list", list);
        return "/config/vpntelnet/multi_telnet_netip.jsp";
    }
    
    //��ʱִ��ҳ���У����ѡ��澯��ʽ��ִ�и÷���
    private String multi_warn_netip() {
        String jsp = "/config/vpntelnet/chooselist.jsp";
        
        String alarmWayIdEvent = getParaValue("alarmWayIdEvent");
        
        String alarmWayNameEvent = getParaValue("alarmWayNameEvent");
        
        try {
            List list = getList();
            
            request.setAttribute("list", list);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        request.setAttribute("alarmWayIdEvent", alarmWayIdEvent);
        
        request.setAttribute("alarmWayNameEvent", alarmWayNameEvent);
        
        return jsp;
    }
    
    public List getList(){
        String sqlQuery = "";
        
        AlarmWayDao alarmWayDao = new AlarmWayDao();
        try {
            list(alarmWayDao, sqlQuery);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            alarmWayDao.close();
        }
        List list = (List)request.getAttribute("list");
        return list;
    }
    
//    public String getSQLQueryForList(){
//        return "";
//    }

    public String serverip() {
        HostNodeDao dao = new HostNodeDao();
        List list = dao.loadServer();
        int listsize = list.size();
        request.setAttribute("iplist", list);
        HostNodeDao listdao = new HostNodeDao();
        setTarget("/config/vpntelnet/serverip.jsp");
        String page = list(listdao);
        JspPage jp = (JspPage) request.getAttribute("page");
        jp.setTotalRecord(listsize);
        request.setAttribute("page", jp);
        return page;
    }
    private String deployCfgForBatch() {
        String fileName = null;
        String serverFilePath = null;
        FileItem fileIntem = null;
        String ipAddresses = null;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 10);
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        factory.setRepository(new File(prefix + "cfg\\batch"));// ���÷������˱���·��
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1000000);
        String jsp = "/config/vpntelnet/status.jsp";
        try {
            List fileItems = upload.parseRequest(this.request);
            Iterator iter = fileItems.iterator(); // ���δ���ÿ���ؼ�
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();// �����������ļ�������б���Ϣ
                // System.out.println(item.getName());
                if (item.getName() != null) {
                    fileIntem = item;
                    System.out.println(fileIntem.getName());
                    fileName = fileIntem.getName();
                }
                if (item.isFormField()) {
                    if (item.getFieldName().equals("ipaddress"))// �ļ���
                    {
                        ipAddresses = item.getString();
                    }

                }
            }
            serverFilePath = prefix + "cfg\\\\batch\\\\" + fileName;
            fileIntem.write(new File(serverFilePath));

        } catch (Exception e) {
            e.printStackTrace();
            jsp = null;
            setErrorCode(0);// δ֪����
        }

        String[] split = ipAddresses.substring(1).split(",");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 30, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                new ThreadPoolExecutor.CallerRunsPolicy());
        String s = "";
        for (int i = 0; i < split.length; i++) {
            s = s + "," + split[i];
        }
        String s2 = s.substring(1);
        String sql = "select * from topo_node_telnetconfig where ip_address in('" + s2.replace(",", "','") + "')";
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        List list = dao.findByCriteria(sql);
        dao.close();

        result.delete(0, result.length());
        for (int i = 0; i < split.length; i++) {
            threadPool.execute(new BatchDeployTask(result, (Huaweitelnetconf) list.get(i), fileName, serverFilePath));
        }
        threadPool.shutdown();
        try {
            boolean loop = true;
            do { // �ȴ������������
                loop = !threadPool.awaitTermination(2, TimeUnit.SECONDS);
            } while (loop);
        } catch (Exception e) {
        }

        System.out.println(result.toString());
        request.setAttribute("result", result.toString());
        return "/config/vpntelnet/multi_modify_status.jsp";
    }
    private String ready_deployCfgForBatch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String b_time = sdf.format(new Date());
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        String fileName = prefix + "cfg\\\\" + "_" + b_time + "batch.cfg";
        request.setAttribute("fileName", fileName);
        return "/config/vpntelnet/deployCfgForBatch.jsp";
    }
    private String bkpCfg_forBatch() {
        String bkpType = this.getParaValue("bkptype");
        result.delete(0, result.length());
        String fileName = this.getParaValue("fileName");
        String fileDesc = this.getParaValue("fileDesc");
        String ipAddresses = request.getParameter("ipaddress");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String bkptime = "";
        Date bkpDate = new Date();
        String reg = "_(.*)cfg.cfg";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(fileName);
        if (m.find()) {
            bkptime = m.group(1);
        }
        try {
            bkpDate = sdf.parse(bkptime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] split = ipAddresses.substring(1).split(",");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 30, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                new ThreadPoolExecutor.CallerRunsPolicy());
        String s = "";
        for (int i = 0; i < split.length; i++) {
            s = s + "," + split[i];
        }
        String s2 = s.substring(1);
        String sql = "select * from topo_node_telnetconfig where ip_address in('" + s2.replace(",", "','") + "')";
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        List list = dao.findByCriteria(sql);
        dao.close();
        result.delete(0, result.length());
        String prefixFileName = fileName.substring(2);
        for (int i = 0; i < split.length; i++) {
            threadPool.execute(new BatchBackupTask(result, (Huaweitelnetconf) list.get(i), prefixFileName, fileDesc, bkpDate, bkpType));
        }
        threadPool.shutdown();
        try {
            boolean loop = true;
            do { // �ȴ������������
                loop = !threadPool.awaitTermination(2, TimeUnit.SECONDS);
            } while (loop);
        } catch (Exception e) {
        }

        System.out.println(result.toString());
        request.setAttribute("result", result.toString());
        return "/config/vpntelnet/multi_modify_status.jsp";
    }
    private String ready_backupForBatch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String b_time = sdf.format(new Date());
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        String fileName = prefix + "cfg\\\\" + "IP" + "_" + b_time + "cfg.cfg";
        System.out.println(fileName + "======back");
        request.setAttribute("fileName", fileName);
        return "/config/vpntelnet/batchBackup.jsp";
    }
    private String ready_multi_modify1() {
        return "/config/vpntelnet/multi_telnetmodifypassword.jsp";
    }
    private String ready_multi_modify() {
        String[] ids = this.getParaArrayValue("checkbox");
        if (ids != null) {
            String s = "";
            for (int i = 0; i < ids.length; i++) {
                s = s + "," + ids[i];
            }
            String s2 = s.substring(1);
            String sql = "select * from topo_node_telnetconfig where id in(" + s2 + ")";
            HaweitelnetconfDao dao = new HaweitelnetconfDao();
            List list = dao.findByCriteria(sql);
            dao.close();
            if (list.size() == 1)// ���ֻ��ѡ��һ������ô�ͽ��뵥����Ԫ���޸Ĳ�����������δ���͵�����Ԫ���޸Ĳ�����ͬ
            {
                HaweitelnetconfDao telnetcfgdao = new HaweitelnetconfDao();
                // Huaweitelnetconf telnetCfg = (Huaweitelnetconf)telnetcfgdao.findByID(id);
                Huaweitelnetconf telnetCfg = (Huaweitelnetconf) telnetcfgdao.findByID(s2);
                request.setAttribute("vpntelnetconf", telnetCfg);
                telnetcfgdao.close();
                if (telnetCfg.getDeviceRender().equals("h3c")) {
                    request.setAttribute("deviceType", "h3c");
                    return "/config/vpntelnet/telnetmodifypassword.jsp";
                } else // ˼��
                {
                    request.setAttribute("deviceType", "cisco");
                    return "/config/vpntelnet/cisco_telnet_modify.jsp";
                }
            } else// ������������޸�
            {
                Huaweitelnetconf vo = (Huaweitelnetconf) list.get(0);
                int num = 1;
                request.setAttribute("vpntelnetconf", vo);
                request.setAttribute("ipAddresses", s2);
                // HaweitelnetconfDao telnetcfgdao=new HaweitelnetconfDao();
                return "/config/vpntelnet/multi_telnetmodifypassword.jsp";
                /*
                 * for(int i = 1;i <list.size();i++) { Huaweitelnetconf t1 = (Huaweitelnetconf)list.get(i); //�ж���ѡ�豸�ĳ�ʼ�����Ƿ���ͬ��ֻ������ͬ������²���ִ����������
                 * if(vo.getUser().equals(t1.getUser()) && vo.getPassword().equals(t1.getPassword()) && vo.getSuuser().equals(t1.getSuuser()) &&
                 * vo.getSupassword().equals(t1.getSupassword()) && vo.getDeviceRender().equals(t1.getDeviceRender())) { num++; continue; } else {
                 * break; } } if(num == list.size())//��� ˵���������������Խ����������� { request.setAttribute("vpntelnetconf", vo);
                 * request.setAttribute("ipAddresses", s2); if(vo.getDeviceRender().equals("h3c")) { request.setAttribute("deviceType", "h3c"); return
                 * "/config/vpntelnet/multi_telnetmodifypassword.jsp"; } else//if(vo.getDeviceRender().equals("cisco"))//�޷����ԣ�δ���� { return ""; } }
                 * else { return "/config/vpntelnet/multi_modify_error.jsp"; }
                 */
            }
        } else
            return list();

    }
    
    //�����޸���Ϣ
    private String ready_multi_audit() {
        HaweitelnetconfAuditDao haweitelnetconfAuditDao = new HaweitelnetconfAuditDao();
        
        this.list(haweitelnetconfAuditDao);
        List<HaweitelnetconfAudit> auditlist = (List<HaweitelnetconfAudit>) request.getAttribute("list");
        request.setAttribute("list", auditlist);
        return "/config/vpntelnet/passwdAudit.jsp";
    }
    
    //��Ʋ�ѯ
    private String queryByCondition() {
//        String key = getParaValue("key");
//        String value = getParaValue("value");
//        User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
//
//        StringBuffer s = new StringBuffer();
//        int _flag = 0;
//        if (current_user.getBusinessids() != null) {
//            if (current_user.getBusinessids() != "-1") {
//                String[] bids = current_user.getBusinessids().split(",");
//                if (bids.length > 0) {
//                    for (int i = 0; i < bids.length; i++) {
//                        if (bids[i].trim().length() > 0) {
//                            if (_flag == 0) {
//                                s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
//                                _flag = 1;
//                            } else {
//                                // flag = 1;
//                                s.append(" or bid like '%," + bids[i].trim() + ",%' ");
//                            }
//                        }
//                    }
//                    s.append(") and " + key + " like '%" + value + "%'");
//                }
//            }
//        }
//        request.setAttribute("actionlist", "list");
//        setTarget("/config/vpntelnet/passwdAudit.jsp");
//        HaweitelnetconfAuditDao haDao = new HaweitelnetconfAuditDao();
//        if (current_user.getRole() == 0) {
//            return list(haDao, "where 1=1 and " + key + " like '%" + value + "%'");
//        } else {
//            return list(haDao, "where 1=1 " + s);
//        }
        
        String key = getParaValue("key");
        String value = getParaValue("value");     
        HaweitelnetconfAuditDao haweitelnetconfAuditDao = new HaweitelnetconfAuditDao();
        
        request.setAttribute("key", key);
        request.setAttribute("value", value);
        setTarget("/config/vpntelnet/passwdAuditFind.jsp");
        return list(haweitelnetconfAuditDao," where "+key+" = '"+value+"'");
    }
    
    
    // �����޸��豸�������
    private String modifyTelnetPasswordForBatch() {
        String ipAddresses = request.getParameter("ipaddress");
        String modifyuser = this.getParaValue("modifyuser");
        String threeA = this.getParaValue("threeA");
        int encrypt = this.getParaIntValue("encrypt");
        String newpassword = this.getParaValue("newpassword");
        String[] split = ipAddresses.substring(1).split(",");

        /**
         * �̳߳�
         * 
         * @param �̳߳�ά���̵߳���������
         * @param �̳߳�ά���̵߳��������
         * @param �̳߳�ά���߳�������Ŀ���ʱ��
         * @param �̳߳�ά���߳�������Ŀ���ʱ��ĵ�λ
         * @param �̳߳���ʹ�õĻ������
         * @param �̳߳ضԾܾ�����Ĵ������
         *            ,�˴��Ĳ����� ������ӵ�ǰ�����������Զ��ظ�����execute()����
         * @author GZM
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 30, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                new ThreadPoolExecutor.CallerRunsPolicy());
        String s = "";
        for (int i = 0; i < split.length; i++) {
            s = s + "," + split[i];
        }
        String s2 = s.substring(1);
        String sql = "select * from topo_node_telnetconfig where ip_address in('" + s2.replace(",", "','") + "')";
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        List list = dao.findByCriteria(sql);
        dao.close();
        result.delete(0, result.length());
        for (int i = 0; i < list.size(); i++) {
            threadPool.execute(new BatchModifyTask(result, (Huaweitelnetconf) list.get(i), modifyuser, newpassword, threeA, encrypt));
        }
        threadPool.shutdown();
        try {
            boolean loop = true;
            do { // �ȴ������������
                loop = !threadPool.awaitTermination(2, TimeUnit.SECONDS);
            } while (loop);
        } catch (Exception e) {
        }

        /*
         * while(threadPool.getActiveCount() == 0)//��������ִ������Ľ����߳���,Ϊ0 ˵�����������Ѿ�ִ���� { try{ Thread.sleep(1000); }catch(Exception e){e.printStackTrace();}
         * }
         */
        System.out.println(result.toString());
        request.setAttribute("result", result.toString());
        return "/config/vpntelnet/multi_modify_status.jsp";
    }

    // ִ��������Ӳ���
    private String batchAdd() {
        int isSynchronized = 1;
        String ipAddress = getParaValue("ipaddress");
        String threeA = this.getParaValue("threeA");
        String device_render = "unknow";
        int encrypt = this.getParaIntValue("encrypt");
        String ipAddressTmp = ipAddress.substring(1);
        String[] split = ipAddressTmp.split(",");
        String tmp = ipAddress.substring(1);
        HaweitelnetconfDao hdao = new HaweitelnetconfDao();
        boolean b = hdao.isExistsIp(tmp);
        hdao.close();
        if (b) {
            setErrorCode(ErrorMessage.IP_ADDRESS_EXIST);
            return null;
        } else {
            HaweitelnetconfDao dao = new HaweitelnetconfDao();
            try {
                for (int k = 0; k < split.length; k++) {
                    String temp1 = split[k];
                    System.out.println(temp1);
                    HostNodeDao hostNodeDao = new HostNodeDao();
                    HostNode hostNode = null;
                    BaseVo baseVo = null;
                    try {
                        baseVo = hostNodeDao.findByIpaddress(temp1);
                        hostNode = (HostNode) baseVo;
                    } catch (Exception e) {
                        hostNode = null;
                        isSynchronized = 0;// ˵�����ݲ�ͬ��
                    }
                    hostNodeDao.close();
                    if (hostNode != null) {
                        HaweitelnetconfDao tmpDao = new HaweitelnetconfDao();
                        tmpDao.delete(hostNode.getId() + "");
                        tmpDao.close();
                    }

                    Huaweitelnetconf vo = new Huaweitelnetconf();

                    // ����ID
                    if (hostNode != null) {
                        vo.setId(hostNode.getId());
                        String sysOid = hostNode.getSysOid();
                        if (sysOid.startsWith("1.3.6.1.4.1.25506") || sysOid.startsWith("1.3.6.1.4.1.2011"))// ����
                        {
                            // vo.setDeviceRender("h3c");
                            device_render = "h3c";
                        } else if (sysOid.equals("1.3.6.1.4.1.9.1.209"))// ˼��
                        {
                            // vo.setDeviceRender("cisco");
                            device_render = "cisco";
                        } else {
                            // vo.setDeviceRender("unknow");
                            device_render = "unknow";
                        }
                    } else {
                        HaweitelnetconfDao dao1 = new HaweitelnetconfDao();
                        int minId = dao1.getMinId();
                        dao1.close();
                        if (minId > 0)
                            vo.setId(-1);
                        else
                            vo.setId(minId - 1);
                    }
                    //

                    vo.setUser(getParaValue("user"));
                    vo.setPassword(getParaValue("password"));
                    vo.setSuuser(getParaValue("suuser"));
                    vo.setSupassword(getParaValue("supassword"));
                    vo.setIpaddress(temp1);
                    vo.setPort(getParaIntValue("port"));
                    vo.setDefaultpromtp(getParaValue("defaultpromtp"));
                    vo.setEnablevpn(getParaIntValue("enablevpn"));
                    vo.setIsSynchronized(isSynchronized);
                    vo.setDeviceRender(device_render);
                    vo.setThreeA(threeA);
                    vo.setEncrypt(encrypt);
                    dao.addBatch(vo);
                }
                dao.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
            return "/vpntelnetconf.do?action=list";
        }
    }
    // ��������豸ҳ���У����ѡ���豸��ִ�и÷���
    private String multi_netip() {
        HostNodeDao dao = new HostNodeDao();
        List list = dao.loadNetwork(-1);
        dao.close();
        // int listsize=list.size();
        request.setAttribute("list", list);
        /*
         * HostNodeDao listdao=new HostNodeDao(); setTarget("/config/vpntelnet/multi_netip.jsp"); String page=list(listdao); JspPage jp =
         * (JspPage)request.getAttribute("page"); jp.setTotalRecord(listsize); request.setAttribute("page", jp);
         */
        return "/config/vpntelnet/multi_netip.jsp";
    }
    // ��ת���������ҳ��
    private String ready_addForBatch() {
        return "/config/vpntelnet/batch_add.jsp";
    }
    // ͬ������
    private String synchronizeData() {
        HaweitelnetconfDao hdao = new HaweitelnetconfDao();
        // �ѱ�topo_node_telnetconfig �� is_synchronized=0 ���� ip_address=topo_host_node.ip_address ���������id����Ϊ��topo_host_node �е� id
        String sql = "update topo_node_telnetconfig  set topo_node_telnetconfig.id=(select topo_host_node.id from topo_host_node where topo_node_telnetconfig.ip_address=topo_host_node.ip_address),topo_node_telnetconfig.is_synchronized=1,topo_node_telnetconfig.device_render=(select topo_host_node.sys_oid from topo_host_node where topo_node_telnetconfig.ip_address=topo_host_node.ip_address) where topo_node_telnetconfig.is_synchronized=0 and topo_node_telnetconfig.ip_address in(select topo_host_node.ip_address from topo_host_node)";
        System.out.println(sql);
        hdao.executeUpdate(sql);
        hdao.close();
        return list();
    }
    // �ϴ��ļ�
    private String uploadFile() {
        String id = null;// Huaweitelnetconf ������ID
        String fileName = null;
        String fileDesc = null;
        String serverFilePath = null;
        FileItem fileIntem = null;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 10);
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        factory.setRepository(new File(prefix + "cfg\\"));
        ServletFileUpload upload = new ServletFileUpload(factory);
        // upload.setHeaderEncoding("utf-8");
        upload.setSizeMax(1000000);
        String jsp = "/config/vpntelnet/status.jsp";
        try {
            List fileItems = upload.parseRequest(this.request);
            Iterator iter = fileItems.iterator(); // ���δ���ÿ���ؼ�
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();// �����������ļ�������б���Ϣ
                // System.out.println(item.getName());
                if (item.getName() != null) {
                    fileIntem = item;
                }
                if (item.isFormField()) {
                    if (item.getFieldName().equals("filena"))// �ļ���
                    {
                        fileName = item.getString();
                    }
                    if (item.getFieldName().equals("fileName"))// ȫ·��
                    {
                        serverFilePath = item.getString();
                    }
                    if (item.getFieldName().equals("fileDesc")) {
                        fileDesc = new String(item.getString().getBytes("iso-8859-1"), "gbk");
                        // fileDesc = item.getString();
                    }
                    if (item.getFieldName().equals("id")) {
                        id = item.getString();
                    }
                    // System.out.println(item.getFieldName()+"="+item.getString()); //��ñ�����
                }
            }
            fileIntem.write(new File(serverFilePath));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
            String bkptime = "";
            Date bkpDate = new Date();
            String reg = "_(.*)cfg.cfg";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(fileName);
            if (m.find()) {
                bkptime = m.group(1);
            }
            bkpDate = sdf.parse(bkptime);
            HaweitelnetconfDao dao = new HaweitelnetconfDao();
            Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
            dao.close();
            HostNodeDao hostDao = new HostNodeDao();
            HostNode hostNode = (HostNode) hostDao.findByIpaddress(vo.getIpaddress());
            hostDao.close();

            File f = new File(serverFilePath);
            int fileSize = 0;
            FileInputStream fis = new FileInputStream(f);
            fileSize = fis.available();
            if (fileSize != 0) {
                fileSize = fileSize / 1000;
                if (fileSize == 0)
                    fileSize = 1;
            }

            Hua3VPNFileConfig h3vpn = new Hua3VPNFileConfig();
            h3vpn.setFileName(serverFilePath);
            h3vpn.setDescri(fileDesc);
            h3vpn.setIpaddress(vo.getIpaddress());
            h3vpn.setFileSize(fileSize);
            h3vpn.setBackupTime(new Timestamp(bkpDate.getTime()));
            Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
            h3Dao.save(h3vpn);
            h3Dao.close();
            request.setAttribute("id", id);
        } catch (Exception e) {
            e.printStackTrace();
            jsp = null;
            setErrorCode(0);// δ֪����
        }
        return jsp;
    }
    // ��ת���ϴ��ļ�ҳ��
    private String readyuploadCfgFile() {
        String id = this.getParaValue("id");
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
        dao.close();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String b_time = sdf.format(new Date());
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        String fileName = prefix + "cfg\\\\" + vo.getIpaddress() + "_" + b_time + "cfg.cfg";
        request.setAttribute("id", id);
        request.setAttribute("ipaddress", vo.getIpaddress());
        request.setAttribute("fileName", fileName);
        return "/config/vpntelnet/uploadfile.jsp";
    }

    private String updatepassword() {

        String deviceType = this.getParaValue("deviceType");
        SysLogger.info("deviceType=====" + deviceType);

        if (deviceType.equals("h3c"))// h3c
        {
            String id = this.getParaValue("id");
            String modifyuser = this.getParaValue("modifyuser");
            String newpassword = this.getParaValue("newpassword");
            int encrypt = this.getParaIntValue("encrypt");
            String threeA = this.getParaValue("threeA");

            HaweitelnetconfDao telnetcfgdao = new HaweitelnetconfDao();
            Huaweitelnetconf hmo = (Huaweitelnetconf) telnetcfgdao.findByID(id);
            telnetcfgdao.close();

            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(hmo.getSuuser());// su
            tvpn.setSupassword(hmo.getSupassword());// su����
            tvpn.setUser(hmo.getUser());// �û�
            tvpn.setPassword(hmo.getPassword());// ����
            tvpn.setIp(hmo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(hmo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(hmo.getPort());
            // boolean b = tvpn.Modifypassowd(modifyuser, newpassword);
            boolean b = tvpn.Modifypassowd(modifyuser, newpassword, encrypt, threeA, hmo.getOstype());
            if (b) {
                if (modifyuser.equals("su")) {
                    hmo.setSupassword(newpassword);
                } else {
                    hmo.setUser(modifyuser);
                    hmo.setPassword(newpassword);
                }
                HaweitelnetconfDao hdao = new HaweitelnetconfDao();
                hdao.update(hmo);
                hdao.close();
                return "/config/vpntelnet/status.jsp";
            } else {
                setErrorCode(0);
                return null;
            }

        }
        // return "";
        else if (deviceType.equals("cisco"))// cisco
        {
            String id = this.getParaValue("id");
            String modifyuser = this.getParaValue("modifyuser");
            String newpassword = this.getParaValue("newpassword");
            HaweitelnetconfDao telnetcfgdao = new HaweitelnetconfDao();

            HaweitelnetconfAudit hmoa = new HaweitelnetconfAudit();
            HaweitelnetconfAuditDao dao = new HaweitelnetconfAuditDao();
            User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
            int userid = user.getId();
            String username = user.getName();
            String oldPassword = this.getParaValue("password");
            String newPassword = this.getParaValue("newpassword");
            hmoa.setIp(getParaValue("ipaddress"));
            hmoa.setUserid(userid);
            hmoa.setUsername(username);
            hmoa.setOldpassword(oldPassword);
            hmoa.setNewpassword(newPassword);
            dao.save(hmoa);
            Huaweitelnetconf hmo = null;
            try {
                hmo = (Huaweitelnetconf) telnetcfgdao.findByID(id);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                telnetcfgdao.close();
                dao.close();
            }

            CiscoTelnet ciscoTelnet = new CiscoTelnet(hmo.getIpaddress(), hmo.getUser(), hmo.getPassword());
            if (ciscoTelnet.login()) {
                if (ciscoTelnet.modifyPasswd(hmo.getSupassword(), modifyuser, newpassword)) {
                    hmo.setUser(modifyuser);
                    hmo.setPassword(newpassword);
                    HaweitelnetconfDao tdao = new HaweitelnetconfDao();
                    try {
                        tdao.update(hmo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        tdao.close();
                    }
                    return "/config/vpntelnet/status.jsp";
                } else {
                    setErrorCode(-1);
                    // return null;
                    SysLogger.info("===================3");
                    return "/config/vpntelnet/error.jsp";
                }
            } else {
                setErrorCode(-1);
                SysLogger.info("===================1");
                return "/config/vpntelnet/error.jsp";
            }
        } else {
            SysLogger.info("===================2");
            setErrorCode(-1);
            return "/config/vpntelnet/error.jsp";
        }
    }

    // �Ҽ��˵��е� �޸����� ����
    private String readyTelnetModify() {
        String id = this.getParaValue("id");
        HaweitelnetconfDao telnetcfgdao = new HaweitelnetconfDao();
        Huaweitelnetconf telnetCfg = (Huaweitelnetconf) telnetcfgdao.findByID(id);
        request.setAttribute("vpntelnetconf", telnetCfg);
        telnetcfgdao.close();
        if (telnetCfg.getDeviceRender().equals("h3c"))// ����
        {
            request.setAttribute("deviceType", "h3c");
            return "/config/vpntelnet/telnetmodifypassword.jsp?deviceType=h3c";
        } else // if(sysOid.equals("1.3.6.1.4.1.9.1.209"))//˼��
        {
            request.setAttribute("deviceType", "cisco");
            return "/config/vpntelnet/cisco_telnet_modify.jsp?deviceType=cisco";
        }
    }
    
//    private String deviceList1() {
//        List list = new ArrayList();
//        Hua3VPNFileConfigDao vpnFileDao = new Hua3VPNFileConfigDao();
//        // List vpnDevicelist = vpnFileDao.getDeviceWithLastModify();
//        this.list(vpnFileDao);
//        List vpnDevicelist = (List) request.getAttribute("list");
//        vpnFileDao.close();
//        HostNodeDao hostNodeDao = new HostNodeDao();
//        HaweitelnetconfDao haweiDao = new HaweitelnetconfDao();
//        for (int i = 0; i < vpnDevicelist.size(); i++) {
//            ConfiguringDevice cfgingDevice = new ConfiguringDevice();
//            Hua3VPNFileConfig vpnFileCfg = (Hua3VPNFileConfig) vpnDevicelist.get(i);
//            String ipaddress = vpnFileCfg.getIpaddress();
//            HostNode host = (HostNode) hostNodeDao.findByIpaddress(ipaddress);
//            Huaweitelnetconf telnetConf = (Huaweitelnetconf) haweiDao.loadByIp(ipaddress);
//            String alias = host.getAlias();
//            cfgingDevice.setId(vpnFileCfg.getId());
//            cfgingDevice.setCategory(host.getCategory());
//            cfgingDevice.setAlias(alias);
//            cfgingDevice.setIpaddress(ipaddress);
//            cfgingDevice.setLastUpdateTime(vpnFileCfg.getBackupTime());
//            int temp = telnetConf.getEnablevpn();
//            cfgingDevice.setEnablevpn(temp);
//            cfgingDevice.setPrompt(telnetConf.getDefaultpromtp());
//            list.add(cfgingDevice);
//        }
//        hostNodeDao.close();
//        haweiDao.close();
//
//        request.setAttribute("list", list);
//        // return "/config/vpntelnet/devicelist.jsp";
//        return "/config/vpntelnet/device_list.jsp";
//    }
    
    private String deviceList() {
        PasswdTimingBackupTelnetConfigDao passwdTimingBackupTelnetConfigDao = new PasswdTimingBackupTelnetConfigDao();
        List<PasswdTimingBackupTelnetConfig> passwdTimingBackupTelnetConfigList = null;
        try {
            passwdTimingBackupTelnetConfigList = passwdTimingBackupTelnetConfigDao.getAlList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            passwdTimingBackupTelnetConfigDao.close();
        }
        
        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String id = getParaValue("id");
        AlarmIndicatorsNode alarmIndicatorsNode= null;
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNode = (AlarmIndicatorsNode)alarmIndicatorsNodeDao.findByID(id);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Hashtable alarmWayHashtable = new Hashtable();
        
        if(alarmIndicatorsNode!=null){
            nodeid = alarmIndicatorsNode.getNodeid();
            type = alarmIndicatorsNode.getType();
            subtype = alarmIndicatorsNode.getSubtype();
            AlarmWayDao alarmWayDao = new AlarmWayDao();
            try {
                AlarmWay alarmWay0 = (AlarmWay)alarmWayDao.findByID(alarmIndicatorsNode.getWay0());
                if(alarmWay0!=null){
                    alarmWayHashtable.put("way0", alarmWay0);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                alarmWayDao.close();
            }
        }
        request.setAttribute("alarmWayHashtable", alarmWayHashtable);
        request.setAttribute("alarmIndicatorsNode", alarmIndicatorsNode);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        request.setAttribute("list", passwdTimingBackupTelnetConfigList);
        return "/config/vpntelnet/device_list.jsp";
    }
    
    private String download() {
        String id = this.getParaValue("id");
        Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
        Hua3VPNFileConfig h3 = (Hua3VPNFileConfig) h3Dao.findByID(id);
        h3Dao.close();
        String filename = h3.getFileName();
        request.setAttribute("filename", filename);
        return "/capreport/net/download.jsp";
    }
    private String delete() {
        String[] id = getParaArrayValue("checkbox");
        if (id != null) {
            HaweitelnetconfDao telnetcfgdao = new HaweitelnetconfDao();
            List telnetcfgList = telnetcfgdao.loadByIds(id);
            telnetcfgdao.close();
            Hua3VPNFileConfigDao vpnDao = new Hua3VPNFileConfigDao();
            if (id != null && id.length != 0) {
                for (int i = 0; i < telnetcfgList.size(); i++) {
                    Huaweitelnetconf huawei = (Huaweitelnetconf) telnetcfgList.get(i);
                    List vpnConfigList = vpnDao.loadByIp(huawei.getIpaddress());
                    String[] vpncol = new String[vpnConfigList.size()];
                    for (int j = 0; j < vpnConfigList.size(); j++) {
                        Hua3VPNFileConfig tmp = (Hua3VPNFileConfig) vpnConfigList.get(j);
                        vpncol[j] = new Integer(tmp.getId()).toString();
                        File f = new File(tmp.getFileName());
                        f.delete();
                    }
                    vpnDao.delete(vpncol);
                }
            }
            vpnDao.close();
            DaoInterface dao = new HaweitelnetconfDao();
            setTarget("/vpntelnetconf.do?action=list&jp=1");
            return delete(dao);
        } else
            return list();

    }
    /*
     * private String bkpCfg() { String id = getParaValue("id"); String ipaddress=getParaValue("ipaddress"); String page = getParaValue("page");
     * HaweitelnetconfDao Hdao=new HaweitelnetconfDao(); String idrs=Hdao.findbyip(ipaddress); Hdao.close(); HaweitelnetconfDao dao = new
     * HaweitelnetconfDao(); Huaweitelnetconf vo = (Huaweitelnetconf)dao.findByID(idrs); dao.close(); Huawei3comvpn tvpn = new Huawei3comvpn();
     * tvpn.setSuuser(vo.getSuuser());// su tvpn.setSupassword(vo.getSupassword());// su���� tvpn.setUser(vo.getUser());// �û�
     * tvpn.setPassword(vo.getPassword());// ���� tvpn.setIp(vo.getIpaddress());// ip��ַ tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
     * tvpn.setPort(23); //String result = tvpn.Getcommantvalue("disp cu"); //tvpn.Backupconffile(); String result = "adsfasdf\r\nadsfasdf\r\n";
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm"); String b_time = sdf.format(new Date()); String prefix =
     * ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\"); String fileName = prefix + "cfg\\\\"+vo.getIpaddress()+"_"+b_time+"cfg.cfg";
     * File f = new File(fileName); //f.mkdir(); try{ FileWriter fw = new FileWriter(f); fw.write(result); fw.flush(); fw.close(); //PrintWriter pw =
     * new PrintWriter(new BufferedWriter(new FileWriter(f))); //pw.print(result); //pw.close(); }catch(Exception e){e.printStackTrace();}
     * request.setAttribute("id", id); PollMonitorManager pm = new PollMonitorManager(); pm.setRequest(request); String jsp = "";
     * if(page.equals("liusu")) { jsp = "/topology/network/networkview.jsp?flag=0"; return jsp; } else if(page.equals("netcpu")) { return
     * pm.execute("netcpu"); } else if(page.equals("netarp")) { return pm.execute("netarp"); } else if(page.equals("netfdb")) { return
     * pm.execute("netfdb"); } if(page.equals("netroute")) { return pm.execute("netroute"); } else if(page.equals("netiplist")) { return
     * pm.execute("netiplist"); } else if(page.equals("netenv")) { return pm.execute("netenv"); } else if(page.equals("netevent")) { return
     * pm.execute("netevent"); } else { return pm.execute("netevent"); } }
     */
    private String bkpCfg_1() {
        String bkpType = this.getParaValue("bkptype");
        String id = getParaValue("id");// Huaweitelnetconf ������ID
        String fileName = this.getParaValue("fileName");
        String fileDesc = this.getParaValue("fileDesc");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String bkptime = "";
        Date bkpDate = new Date();
        String reg = "_(.*)cfg.cfg";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(fileName);
        if (m.find()) {
            bkptime = m.group(1);
        }
        try {
            bkpDate = sdf.parse(bkptime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
        dao.close();

        String result = "";
        String jsp = null;
        if (vo.getDeviceRender().equals("h3c"))// h3c
        {
            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(vo.getSuuser());// su
            tvpn.setSupassword(vo.getSupassword());// su����
            tvpn.setUser(vo.getUser());// �û�
            tvpn.setPassword(vo.getPassword());// ����
            tvpn.setIp(vo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(vo.getPort());
            result = tvpn.Backupconffile(bkpType);
            if (!result.equals("user or password error")) {
                jsp = "/config/vpntelnet/status.jsp";
            }
            // System.out.println("############################################123");
            // System.out.println(result);
        } else if (vo.getDeviceRender().equals("cisco"))// cisco
        {

            CiscoTelnet telnet = new CiscoTelnet(vo.getIpaddress(), vo.getUser(), vo.getPassword());
            if (telnet.login()) {
                result = telnet.getCfg(vo.getSupassword(), bkpType);
                jsp = "/config/vpntelnet/status.jsp";
            }
        }
        if (jsp != null) {
            File f = new File(fileName);
            int fileSize = 0;
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(result);
                fw.flush();
                fw.close();
                FileInputStream fis = new FileInputStream(f);
                fileSize = fis.available();
                if (fileSize != 0) {
                    fileSize = fileSize / 1000;
                    if (fileSize == 0)
                        fileSize = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Hua3VPNFileConfig h3vpn = new Hua3VPNFileConfig();
            h3vpn.setFileName(fileName);
            h3vpn.setDescri(fileDesc);
            h3vpn.setIpaddress(vo.getIpaddress());
            h3vpn.setFileSize(fileSize);
            h3vpn.setBackupTime(new Timestamp(bkpDate.getTime()));
            Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
            h3vpn.setBkpType(bkpType);
            h3Dao.save(h3vpn);
            h3Dao.close();
        }

        request.setAttribute("id", id);
        if (jsp == null) {
            this.setErrorCode(1003);// �û������������
        }
        return jsp;
    }
    private String readySetupConfig() {
        String id = this.getParaValue("id");
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
        dao.close();
        String ipaddress = this.getParaValue("ipaddress");
        Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
        List list = h3Dao.loadByIp(ipaddress);
        h3Dao.close();
        request.setAttribute("list", list);
        request.setAttribute("id", id);
        request.setAttribute("ipaddress", ipaddress);
        return "/config/vpntelnet/setupConfig_cisco.jsp";
    }
    private String readyBackupConfig() {
        String id = this.getParaValue("id");
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
        dao.close();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String b_time = sdf.format(new Date());
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        String fileName = prefix + "cfg\\\\" + vo.getIpaddress() + "_" + b_time + "cfg.cfg";
        request.setAttribute("id", id);
        request.setAttribute("ipaddress", vo.getIpaddress());
        request.setAttribute("fileName", fileName);
        return "/config/vpntelnet/backup_cisco.jsp";
    }
    private String detailPageReadyBackupConfig() {
        String ipaddress = this.getParaValue("ipaddress");
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.loadByIp(ipaddress);
        String id = new Integer(vo.getId()).toString();
        dao.close();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm");
        String b_time = sdf.format(new Date());
        String prefix = ResourceCenter.getInstance().getSysPath().replace("\\", "\\\\");
        String fileName = prefix + "cfg\\\\" + vo.getIpaddress() + "_" + b_time + "cfg.cfg";
        request.setAttribute("id", id);
        request.setAttribute("ipaddress", vo.getIpaddress());
        request.setAttribute("fileName", fileName);
        return "/config/vpntelnet/backup.jsp";
    }
    private String setupConfig() {
        // String id = getParaValue("id");
        String ipaddress = getParaValue("ipaddress");
        String page = getParaValue("page");
        HaweitelnetconfDao Hdao = new HaweitelnetconfDao();
        String idrs = Hdao.findbyip(ipaddress);
        Hdao.close();
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(idrs);
        dao.close();
        Huawei3comvpn tvpn = new Huawei3comvpn();
        tvpn.setSuuser(vo.getSuuser());// su
        tvpn.setSupassword(vo.getSupassword());// su����
        tvpn.setUser(vo.getUser());// �û�
        tvpn.setPassword(vo.getPassword());// ����
        tvpn.setIp(vo.getIpaddress());// ip��ַ
        tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
        tvpn.setPort(23);
        // String result = tvpn.Getcommantvalue("disp cu");

        String filePath = this.getParaValue("filePath");
        File f = new File(filePath);
        StringBuffer content = new StringBuffer();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String s = null;
            while ((s = br.readLine()) != null) {
                content.append(s);
            }
            System.out.println("content=" + content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvpn.Writeconffile(content.toString());

        PollMonitorManager pm = new PollMonitorManager();
        pm.setRequest(request);
        String jsp = "";
        if (page.equals("liusu")) {
            jsp = "/topology/network/networkview.jsp?flag=0";
            return jsp;
        } else if (page.equals("netcpu")) {
            return pm.execute("netcpu");
        } else if (page.equals("netarp")) {
            return pm.execute("netarp");
        } else if (page.equals("netfdb")) {
            return pm.execute("netfdb");
        }
        if (page.equals("netroute")) {
            return pm.execute("netroute");
        } else if (page.equals("netiplist")) {
            return pm.execute("netiplist");
        } else if (page.equals("netenv")) {
            return pm.execute("netenv");
        } else {
            return pm.execute("netevent");
        }
    }
    private String setupConfig_1() {
        String id = getParaValue("id");
        String radio = this.getParaValue("radio");

        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf vo = (Huaweitelnetconf) dao.findByID(id);
        dao.close();

        Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
        Hua3VPNFileConfig vpncfg = (Hua3VPNFileConfig) h3Dao.findByID(radio);
        h3Dao.close();

        String filePath = vpncfg.getFileName();
        String remoteFileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        /*
         * File f = new File(filePath); StringBuffer content = new StringBuffer(); try{ FileReader fr = new FileReader(f); BufferedReader br = new
         * BufferedReader(fr); String s = null; while((s=br.readLine())!=null) { content.append(s+"\r\n"); }
         * 
         * System.out.println("content="+content.toString()); br.close(); fr.close(); }catch(Exception e){e.printStackTrace();}
         */

        String jsp = null;
        if (vo.getDeviceRender().equals("h3c"))// h3c��sysoid
        {
            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(vo.getSuuser());// su
            tvpn.setSupassword(vo.getSupassword());// su����
            tvpn.setUser(vo.getUser());// �û�
            tvpn.setPassword(vo.getPassword());// ����
            tvpn.setIp(vo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(vo.getPort());
            // tvpn.Backupconffile();
            // boolean b = tvpn.Writeconffile(content.toString());
            boolean b = tvpn.setupNewConfFile("/" + remoteFileName, filePath, vo.getIpaddress(), 21, vo.getUser(), vo.getPassword());
            if (b) {
                jsp = "/config/vpntelnet/status.jsp";
            } else {
                this.setErrorCode(1003);// �û������������
            }
        } else if (vo.getDeviceRender().equals("cisco"))// cisco��sysoid
        {
            CiscoTelnet telnet = new CiscoTelnet("172.25.25.240", "1", "1");
            // telnet.writeCfgFile(content.toString());
        }

        // tvpn.Writeconffile(content.toString());
        return jsp;
    }

    private String update() {
        Huaweitelnetconf vo = new Huaweitelnetconf();
        vo.setId(getParaIntValue("id"));
        vo.setUser(getParaValue("user"));
        vo.setIpaddress(getParaValue("ipaddress"));
        vo.setSupassword(getParaValue("supassword"));
        vo.setSuuser(getParaValue("suuser"));
        vo.setPort(getParaIntValue("port"));
        vo.setDefaultpromtp(getParaValue("defaultpromtp"));
        vo.setPassword(getParaValue("password"));
        vo.setDefaultpromtp(getParaValue("defaultpromtp"));
        vo.setEnablevpn(getParaIntValue("enablevpn"));// �Ƿ�����

        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        String target = null;
        if (dao.update(vo))
            target = "/vpntelnetconf.do?action=list";
        return target;
    }

    /***
     * 
     * �޸�telnet ���û������룬����޸ĳɹ���telnet ���ӵ��û�������µ����ݿ������enable�ֶν����޸�
     * 
     * @return
     */
    public String modifypassword() {
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        Huaweitelnetconf hmo = new Huaweitelnetconf();
        Huawei3comvpn tvpn = new Huawei3comvpn();

        hmo.setId(Integer.parseInt(getParaValue("id")));
        hmo.setUser(getParaValue("user"));
        hmo.setIpaddress(getParaValue("ipaddress"));
        hmo.setSupassword(getParaValue("supassword"));
        hmo.setSuuser(getParaValue("suuser"));
        hmo.setPort(Integer.parseInt(getParaValue("port")));
        hmo.setDefaultpromtp(getParaValue("defaultpromtp"));
        hmo.setPassword(getParaValue("password"));
        hmo.setDefaultpromtp(getParaValue("defaultpromtp"));

        String modifyuser = getParaValue("modifyuser");
        String newpassword = getParaValue("newpassword");
        int encrypt = Integer.parseInt(getParaValue("encrypt"));
        String threeA = getParaValue("threeA");

        try {
            tvpn.setDEFAULT_TELNET_PORT(hmo.getPort());// �˿�
            tvpn.setSuuser(hmo.getSuuser());// su
            tvpn.setSupassword(hmo.getSupassword());// su����
            tvpn.setUser(hmo.getUser());// �û�
            tvpn.setPassword(hmo.getPassword());// ����
            tvpn.setIp(hmo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(hmo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(hmo.getPort());
            boolean mflg = tvpn.Modifypassowd(modifyuser, newpassword, encrypt, threeA, hmo.getOstype());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/vpntelnetconf.do?action=findpassword&id=" + hmo.getId() + "&ipaddress=" + hmo.getIpaddress();
    }

    public String list() {
        List configingDeviceList = new ArrayList();
        Hua3VPNFileConfigDao vpnFileDao = new Hua3VPNFileConfigDao();
        List vpnDevicelist = vpnFileDao.getDeviceWithLastModify();
        vpnFileDao.close();

        HaweitelnetconfDao telnetConfDao = new HaweitelnetconfDao();
        this.list(telnetConfDao);
        List telnetConfList = (List) request.getAttribute("list");
        int vpnDevicelistSize = 0;
        int tmp = 0;
        Hua3VPNFileConfig tmp2 = null;
        HostNodeDao hostDao = new HostNodeDao();
        HostNode hostNode = null;
        for (int i = 0; i < telnetConfList.size(); i++) {
            ConfiguringDevice cfgingDevice = new ConfiguringDevice();
            Huaweitelnetconf telnetConf = (Huaweitelnetconf) telnetConfList.get(i);
            cfgingDevice.setId(telnetConf.getId());
            cfgingDevice.setIpaddress(telnetConf.getIpaddress());
            cfgingDevice.setPrompt(telnetConf.getDefaultpromtp());
            cfgingDevice.setEnablevpn(telnetConf.getEnablevpn());
            cfgingDevice.setIsSynchronized(telnetConf.getIsSynchronized());
            cfgingDevice.setDeviceRender(telnetConf.getDeviceRender());
            vpnDevicelistSize = vpnDevicelist.size();
            tmp = 0;

            while (vpnDevicelistSize > tmp)// �б����ļ����豸��װ�������һ�εı���ʱ�䣬�ޱ����ļ����豸���ֶ���null
            {
                tmp2 = (Hua3VPNFileConfig) vpnDevicelist.get(tmp);
                if (tmp2.getIpaddress().equals(telnetConf.getIpaddress())) {
                    cfgingDevice.setLastUpdateTime(tmp2.getBackupTime());
                    break;
                }
                tmp++;
                if (vpnDevicelistSize == tmp) {
                    cfgingDevice.setLastUpdateTime(null);
                }
            }
            configingDeviceList.add(cfgingDevice);
        }
        hostDao.close();
        request.setAttribute("list", configingDeviceList);
        return "/config/vpntelnet/list.jsp";
    }

    public String configlist() {
        List configingDeviceList = new ArrayList();
        Hua3VPNFileConfigDao vpnFileDao = new Hua3VPNFileConfigDao();
        List vpnDevicelist = vpnFileDao.getDeviceWithLastModify();
        vpnFileDao.close();

        HaweitelnetconfDao telnetConfDao = new HaweitelnetconfDao();
        this.list(telnetConfDao);
        List telnetConfList = (List) request.getAttribute("list");
        int vpnDevicelistSize = 0;
        int tmp = 0;
        Hua3VPNFileConfig tmp2 = null;
        HostNodeDao hostDao = new HostNodeDao();
        HostNode hostNode = null;
        for (int i = 0; i < telnetConfList.size(); i++) {
            ConfiguringDevice cfgingDevice = new ConfiguringDevice();
            Huaweitelnetconf telnetConf = (Huaweitelnetconf) telnetConfList.get(i);
            cfgingDevice.setId(telnetConf.getId());
            cfgingDevice.setIpaddress(telnetConf.getIpaddress());
            cfgingDevice.setPrompt(telnetConf.getDefaultpromtp());
            cfgingDevice.setEnablevpn(telnetConf.getEnablevpn());
            cfgingDevice.setIsSynchronized(telnetConf.getIsSynchronized());
            cfgingDevice.setDeviceRender(telnetConf.getDeviceRender());
            vpnDevicelistSize = vpnDevicelist.size();
            tmp = 0;

            while (vpnDevicelistSize > tmp)// �б����ļ����豸��װ�������һ�εı���ʱ�䣬�ޱ����ļ����豸���ֶ���null
            {
                tmp2 = (Hua3VPNFileConfig) vpnDevicelist.get(tmp);
                if (tmp2.getIpaddress().equals(telnetConf.getIpaddress())) {
                    cfgingDevice.setLastUpdateTime(tmp2.getBackupTime());
                    break;
                }
                tmp++;
                if (vpnDevicelistSize == tmp) {
                    cfgingDevice.setLastUpdateTime(null);
                }
            }
            configingDeviceList.add(cfgingDevice);
        }
        hostDao.close();
        request.setAttribute("list", configingDeviceList);
        return "/config/vpntelnet/configlist.jsp";
    }

    public String passwdList() {
        List configingDeviceList = new ArrayList();
        Hua3VPNFileConfigDao vpnFileDao = new Hua3VPNFileConfigDao();
        List vpnDevicelist = vpnFileDao.getDeviceWithLastModify();
        vpnFileDao.close();

        HaweitelnetconfDao telnetConfDao = new HaweitelnetconfDao();
        this.list(telnetConfDao);
        List telnetConfList = (List) request.getAttribute("list");
        int vpnDevicelistSize = 0;
        int tmp = 0;
        Hua3VPNFileConfig tmp2 = null;
        HostNodeDao hostDao = new HostNodeDao();
        HostNode hostNode = null;
        for (int i = 0; i < telnetConfList.size(); i++) {
            ConfiguringDevice cfgingDevice = new ConfiguringDevice();
            Huaweitelnetconf telnetConf = (Huaweitelnetconf) telnetConfList.get(i);
            cfgingDevice.setId(telnetConf.getId());
            cfgingDevice.setIpaddress(telnetConf.getIpaddress());
            cfgingDevice.setPrompt(telnetConf.getDefaultpromtp());
            cfgingDevice.setEnablevpn(telnetConf.getEnablevpn());
            cfgingDevice.setIsSynchronized(telnetConf.getIsSynchronized());
            cfgingDevice.setDeviceRender(telnetConf.getDeviceRender());
            vpnDevicelistSize = vpnDevicelist.size();
            tmp = 0;

            while (vpnDevicelistSize > tmp)// �б����ļ����豸��װ�������һ�εı���ʱ�䣬�ޱ����ļ����豸���ֶ���null
            {
                tmp2 = (Hua3VPNFileConfig) vpnDevicelist.get(tmp);
                if (tmp2.getIpaddress().equals(telnetConf.getIpaddress())) {
                    cfgingDevice.setLastUpdateTime(tmp2.getBackupTime());
                    break;
                }
                tmp++;
                if (vpnDevicelistSize == tmp) {
                    cfgingDevice.setLastUpdateTime(null);
                }
            }
            configingDeviceList.add(cfgingDevice);
        }
        hostDao.close();
        request.setAttribute("list", configingDeviceList);
        return "/config/vpntelnet/passwdList.jsp";
    }

    public String readyAdd() {
        return "/config/vpntelnet/add.jsp";
    }
    public String add() {
        String result = "";
        int isSynchronized = 1;
        String ipAddress = getParaValue("ipaddress");
        String threeA = this.getParaValue("threeA");
        int encrypt = this.getParaIntValue("encrypt");
        String ostype = this.getParaValue("ostype");
        HaweitelnetconfDao tmpDao = new HaweitelnetconfDao();
        Huaweitelnetconf tmp = (Huaweitelnetconf) tmpDao.loadByIp(ipAddress);
        tmpDao.close();
        if (tmp != null) {
            setErrorCode(ErrorMessage.IP_ADDRESS_EXIST);
            return null;
        } else {
            HostNodeDao hostNodeDao = new HostNodeDao();
            HostNode hostNode = null;
            BaseVo baseVo = null;
            try {
                baseVo = hostNodeDao.findByIpaddress(ipAddress);
                hostNode = (HostNode) baseVo;// �����topo_host_node���ҵ�ָ��ip���������򲻻ᱨ��
            } catch (Exception e) {
                hostNode = null;// ����˵��topo_host_node��û�ж�Ӧ����
                isSynchronized = 0;// ˵�����ݲ�ͬ��
            }
            hostNodeDao.close();

            Huaweitelnetconf vo = new Huaweitelnetconf();
            vo.setOstype(ostype);
            vo.setDeviceRender("unknow");
            HaweitelnetconfDao dao = new HaweitelnetconfDao();
            if (hostNode != null) {
                vo.setId(hostNode.getId());
                String sysOid = hostNode.getSysOid();
                if (sysOid.startsWith("1.3.6.1.4.1.25506") || sysOid.startsWith("1.3.6.1.4.1.2011"))// ����
                {
                    vo.setDeviceRender("h3c");
                } else if (sysOid.equals("1.3.6.1.4.1.9.1.209"))// ˼��
                {
                    vo.setDeviceRender("cisco");
                } else {
                    vo.setDeviceRender("unknow");
                }
            } else {
                HaweitelnetconfDao dao1 = new HaweitelnetconfDao();
                int minId = dao1.getMinId();
                dao1.close();
                if (minId > 0)
                    vo.setId(-1);
                else
                    vo.setId(minId - 1);
            }
            vo.setUser(getParaValue("user"));
            vo.setPassword(getParaValue("password"));
            vo.setSuuser(getParaValue("suuser"));
            vo.setSupassword(getParaValue("supassword"));
            vo.setIpaddress(ipAddress);
            vo.setPort(getParaIntValue("port"));
            vo.setDefaultpromtp(getParaValue("defaultpromtp"));
            vo.setEnablevpn(getParaIntValue("enablevpn"));
            vo.setIsSynchronized(isSynchronized);
            vo.setDeviceRender(this.getParaValue("deviceVender"));
            vo.setThreeA(threeA);
            vo.setEncrypt(encrypt);
            if (vo.getDeviceRender().equals("h3c")) {
                Huawei3comvpn tvpn = new Huawei3comvpn();
                tvpn.setSuuser(vo.getSuuser());// su
                tvpn.setSupassword(vo.getSupassword());// su����
                tvpn.setUser(vo.getUser());// �û�
                tvpn.setPassword(vo.getPassword());// ����
                tvpn.setIp(vo.getIpaddress());// ip��ַ
                tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
                tvpn.setPort(vo.getPort());

                // result = tvpn.isPasswordCorrect();��������Ƿ���ȷ
                result = "";
                if (result.equals(""))// ���ؿհ��ַ�����˵�������ɹ���û�б���
                {
                    try {
                        dao.save(vo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dao.close();
                    }
                    return "/vpntelnetconf.do?action=list";
                } else {
                    this.setErrorCode(1003);// �û������������
                    return null;
                }
            } else if (vo.getDeviceRender().equals("cisco")) {
                try {
                    dao.save(vo);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dao.close();
                }
                return "/vpntelnetconf.do?action=list";
            } else
                return "/vpntelnetconf.do?action=list";
        }
    }
    public String add_pg() {
        Huaweitelnetconf vo = new Huaweitelnetconf();
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        String ipaddress = getParaValue("ipaddress");
        vo.setUser(getParaValue("user"));
        vo.setPassword(getParaValue("password"));
        vo.setSuuser(getParaValue("suuser"));
        vo.setSupassword(getParaValue("supassword"));
        vo.setIpaddress(ipaddress);
        vo.setPort(getParaIntValue("port"));
        vo.setDefaultpromtp(getParaValue("defaultpromtp"));
        vo.setEnablevpn(getParaIntValue("enablevpn"));

        try {
            dao.save(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }

        return "/config/vpntelnet/success.jsp";
    }

    public String findip() {
        HaweitelnetconfDao dao = new HaweitelnetconfDao();
        String ipfindaddress = getParaValue("ipfindaddress");
        List findlist = dao.find(ipfindaddress);
        HaweitelnetconfDao listdao = new HaweitelnetconfDao();
        setTarget("/config/vpntelnet/list.jsp");
        String page = list(listdao);
        request.setAttribute("list", findlist);
        JspPage jp = (JspPage) request.getAttribute("page");
        jp.setTotalPage(1);
        jp.setCurrentPage(1);
        jp.setMinNum(1);
        request.setAttribute("page", jp);
        System.out.println("===page=======" + page);

        return page;
    }

    public String netip() {
        HostNodeDao dao = new HostNodeDao();
        List list = dao.loadNetwork(1);
        int listsize = list.size();
        request.setAttribute("iplist", list);
        HostNodeDao listdao = new HostNodeDao();
        setTarget("/config/vpntelnet/netip.jsp");
        String page = list(listdao);
        JspPage jp = (JspPage) request.getAttribute("page");
        jp.setTotalRecord(listsize);
        request.setAttribute("page", jp);
        return page;
    }
    public String ipmenu() {
        String ipaddress = getParaValue("ipaddress");
        int id = getParaIntValue("id");
        HaweitelnetconfDao Hdao = new HaweitelnetconfDao();
        String idrs = Hdao.findbyip(ipaddress);
        Hdao.close();
        if (idrs.equals("")) {
            request.setAttribute("ipadd", ipaddress);
            return "/config/vpntelnet/add_pg.jsp";
        } else {
            return "/vpntelnetconf.do?action=ready_edit_gp&id=" + idrs;
        }
    }
}
class BatchModifyTask implements Runnable {
    StringBuffer result;
    Huaweitelnetconf hmo;
    String modifyuser;
    String newpassword;
    String threeA;
    int encrypt;
    public BatchModifyTask(StringBuffer result, Huaweitelnetconf hmo, String modifyuser, String newpassword, String threeA, int encrypt) {
        this.result = result;
        this.hmo = hmo;
        this.modifyuser = modifyuser;
        this.newpassword = newpassword;
        this.threeA = threeA;
        this.encrypt = encrypt;
    }
    public void run() {

        if (hmo.getDeviceRender().equals("h3c"))// h3c
        {
            /*
             * String modifyuser = this.getParaValue("modifyuser"); String newpassword = this.getParaValue("newpassword"); int encrypt =
             * this.getParaIntValue("encrypt"); String threeA = this.getParaValue("threeA");
             */

            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(hmo.getSuuser());// su
            tvpn.setSupassword(hmo.getSupassword());// su����
            tvpn.setUser(hmo.getUser());// �û�
            tvpn.setPassword(hmo.getPassword());// ����
            tvpn.setIp(hmo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(hmo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(hmo.getPort());
            // boolean b = tvpn.Modifypassowd(modifyuser, newpassword);
            boolean b = tvpn.Modifypassowd(modifyuser, newpassword, encrypt, threeA, hmo.getOstype());
            if (b) {
                if (modifyuser.equals("su")) {
                    hmo.setSupassword(newpassword);
                } else {
                    hmo.setUser(modifyuser);
                    hmo.setPassword(newpassword);
                }
                HaweitelnetconfDao hdao = new HaweitelnetconfDao();
                hdao.update(hmo);
                hdao.close();
            } else {
                synchronized (result) {
                    result.append("," + hmo.getIpaddress());
                }

            }

        }
        // return "";
        else// cisco
        {
            Huaweitelnetconf telnetcfg = new Huaweitelnetconf();
            CiscoTelnet ciscoTelnet = new CiscoTelnet(hmo.getIpaddress(), hmo.getUser(), hmo.getPassword());
            if (ciscoTelnet.login()) {
                if (ciscoTelnet.modifyPasswd(hmo.getSupassword(), modifyuser, newpassword)) {
                    hmo.setUser(modifyuser);
                    hmo.setPassword(newpassword);
                    HaweitelnetconfDao tdao = new HaweitelnetconfDao();
                    tdao.update(hmo);
                    tdao.close();
                } else {
                    synchronized (result) {
                        result.append("," + hmo.getIpaddress());
                    }
                }
            } else {
                synchronized (result) {
                    result.append("," + hmo.getIpaddress());
                }
            }
        }
    }
}
class BatchBackupTask implements Runnable {
    StringBuffer result;
    Huaweitelnetconf vo;
    String fileName;
    String fileDesc;
    Date bkpDate;
    String bkpType;
    public BatchBackupTask(StringBuffer result, Huaweitelnetconf hmo, String fileName, String fileDesc, Date bkpDate, String bkpType) {
        this.result = result;
        this.vo = hmo;
        this.fileName = hmo.getIpaddress() + fileName;;
        this.fileDesc = fileDesc;
        this.bkpDate = bkpDate;
        this.bkpType = bkpType;
    }
    public void run() {
        String temp_result = "";
        boolean isSuccess = true;
        if (vo.getDeviceRender().equals("h3c"))// h3c
        {
            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(vo.getSuuser());// su
            tvpn.setSupassword(vo.getSupassword());// su����
            tvpn.setUser(vo.getUser());// �û�
            tvpn.setPassword(vo.getPassword());// ����
            tvpn.setIp(vo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(vo.getPort());
            temp_result = tvpn.Backupconffile(bkpType);
            if (temp_result.equals("user or password error")) {
                synchronized (result) {
                    result.append("," + vo.getIpaddress());
                }
                isSuccess = false;
            }
        } else if (vo.getDeviceRender().equals("cisco"))// cisco
        {
            CiscoTelnet telnet = new CiscoTelnet(vo.getIpaddress(), vo.getUser(), vo.getPassword());
            /*
             * if(telnet.login()) { result = telnet.getCfg(vo.getSupassword()); jsp = "/config/vpntelnet/status.jsp"; }
             */
        }
        if (isSuccess) {
            File f = new File(fileName);
            int fileSize = 0;
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(temp_result);
                fw.flush();
                fw.close();
                FileInputStream fis = new FileInputStream(f);
                fileSize = fis.available();
                if (fileSize != 0) {
                    fileSize = fileSize / 1000;
                    if (fileSize == 0)
                        fileSize = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Hua3VPNFileConfig h3vpn = new Hua3VPNFileConfig();
            h3vpn.setFileName(fileName);
            h3vpn.setDescri(fileDesc);
            h3vpn.setIpaddress(vo.getIpaddress());
            h3vpn.setFileSize(fileSize);
            h3vpn.setBackupTime(new Timestamp(bkpDate.getTime()));
            Hua3VPNFileConfigDao h3Dao = new Hua3VPNFileConfigDao();
            h3Dao.save(h3vpn);
            h3Dao.close();
        }
    }
}
class BatchDeployTask implements Runnable {
    Huaweitelnetconf vo = null;
    String remoteFileName = null;
    StringBuffer result = null;
    String localFullPathFileName = null;
    public BatchDeployTask(StringBuffer result, Huaweitelnetconf vo, String remoteFileName, String localFullPathFileName) {
        this.vo = vo;
        this.remoteFileName = remoteFileName;
        this.result = result;
        this.localFullPathFileName = localFullPathFileName;
    }
    public void run() {
        if (vo.getDeviceRender().equals("h3c"))// h3c��sysoid
        {
            Huawei3comvpn tvpn = new Huawei3comvpn();
            tvpn.setSuuser(vo.getSuuser());// su
            tvpn.setSupassword(vo.getSupassword());// su����
            tvpn.setUser(vo.getUser());// �û�
            tvpn.setPassword(vo.getPassword());// ����
            tvpn.setIp(vo.getIpaddress());// ip��ַ
            tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());// ������Ƿ���
            tvpn.setPort(vo.getPort());
            tvpn.setDEFAULT_PROMPT(vo.getDefaultpromtp());
            // tvpn.Backupconffile();
            // boolean b = tvpn.Writeconffile(content.toString());
            boolean b = tvpn.setupNewConfFile("/" + remoteFileName, localFullPathFileName, vo.getIpaddress(), 21, vo.getUser(), vo.getPassword());
            if (!b) {
                synchronized (result) {
                    result.append("," + vo.getIpaddress());
                }
            }
        } else if (vo.getDeviceRender().equals("cisco"))// cisco��sysoid
        {
            CiscoTelnet telnet = new CiscoTelnet("172.25.25.240", "1", "1");
            // telnet.writeCfgFile(content.toString());
        }
    }
}
