package com.afunms.alarm.sms.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import montnets.SmsDao;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.event.model.SendSmsConfig;
import com.afunms.initialize.ResourceCenter;
import com.afunms.system.dao.AlertInfoServerDao;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.AlertInfoServer;
import com.afunms.system.model.SmsConfig;
import com.afunms.system.model.User;

public class SendSMSAlarmByDBService {

    /**
     * 短信发送
     * 
     * @param phone
     *            手机号
     * @param userName
     *            用户名
     * @param content
     *            发送内容
     * @return
     */
    public static boolean sendSMS(String driver, String dbURL, String mobile, String content) {
        boolean sendResult = false;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curtime = sdf.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean aa = mobile != null && mobile.matches("^1[3,5,8][0-9]{9}$")
                        && !"".equals(mobile);
        if (aa) {
            // SMSModel sms = new SMSModel();
            // sms.setExtCode("");
            // // 接受手机MSISDN,多个用“;”分割,最大不超过50 个
            // sms.setDestAddr(mobile);
            // // 短信内容,当MSGFMT 为4时,消息内容为十六进制字符串
            // sms.setMessageContent(content);
            // // 是否需要状态报告,0:不需要,1:需要
            // sms.setReqDeliveryReport("1");
            // // 消息类型,0- ASCII,4- Binary,8- usc2,15- gb2312,16-gb18030
            // sms.setMagFmt("15");
            // // 0:普通短信/?:普通短信立即显示/?:长短信组包/?:带结构短信
            // sms.setSendMethod("0");
            // // 入库时间(短信发送请求时间)
            // sms.setRequestTime(new Date());
            // // EC/SI 应用的ID
            // sms.setApplicationId("APP056");
            // // 企业ID，企业移动代理服务器模式可选提供
            // sms.setEcId("defaultema");
            Connection conn = null;
            PreparedStatement pre = null;
            String uuid = java.util.UUID.randomUUID().toString()
                            .substring(0, 32);

            java.sql.Date date1 = null;
            try {
                date1 = new java.sql.Date((sdf.parse(curtime)).getTime());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(dbURL);
                String sql = "Insert into sms_outbox (sismsid, extcode, destaddr, messagecontent, reqdeliveryreport,"
                                + "msgfmt, sendmethod, requesttime, applicationid, ecid)VALUES"
                                + "(?,?,?,?,?,?,?,?,?,?)";
                pre = conn.prepareStatement(sql);
                pre.setString(1, uuid);
                pre.setString(2, "");
                pre.setString(3, mobile);
                content = content + " 产生时间:" + curtime;
                pre.setString(4, content);
                pre.setInt(5, 1);
                pre.setInt(6, 15);
                pre.setInt(7, 0);
                pre.setDate(8, date1);
                pre.setString(9, "APP054");
                pre.setString(10, "defaultema");
                pre.execute();
                sendResult = true; // 发送成功
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pre != null)
                    try {
                        pre.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
        return sendResult;
    }

    public static void main(String[] args) {
    }
}
