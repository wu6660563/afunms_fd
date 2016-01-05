/*
 * @(#)TomcatServerConnector.java     v1.01, Jan 7, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.tomcatmonitor;

import java.util.Hashtable;

/**
 * ClassName:   TomcatServerConnector.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 7, 2013 5:14:27 PM
 */
public class TomcatServerConnector extends ServerConnector {

    private static final String TARGET = "/manager/status";
    
    private static final String REALM = "Tomcat Manager Application";

    private static final boolean ISAUTH = true;

    /**
     * connect:
     * <p>连接服务器
     *
     * @param   ip
     *          - 主机
     * @param   port
     *          - 端口
     * @param   user
     *          - 用户名
     * @param   password
     *          - 密码
     * @param   isAuth
     *          - 是否开启验证
     * @return
     *
     * @since   v1.01
     * @see com.afunms.application.tomcatmonitor.ServerConnector#connect(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public boolean connect(String ip, int port, String user, String password, boolean isAuth) {
        // TODO Auto-generated method stub
        return super.connect(ip, port, TARGET, REALM, user, password, isAuth);
    }

    /**
     * connect:
     * <p>连接服务器
     *
     * @param   ip
     *          - 主机
     * @param   port
     *          - 端口
     * @param   user
     *          - 用户名
     * @param   password
     *          - 密码
     * @param   isAuth
     *          - 是否开启验证
     * @return
     *
     * @since   v1.01
     * @see com.afunms.application.tomcatmonitor.ServerConnector#connect(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public boolean connect(String ip, int port, String user, String password) {
        // TODO Auto-generated method stub
        return super.connect(ip, port, TARGET, REALM, user, password, ISAUTH);
    }

    /**
     * parseContent:
     * <p>解析 Tomcat 内容
     *
     * @param   content
     *          - 内容
     * @return  {@link TomcatInfo}
     *          - {@link TomcatInfo}
     *
     * @since   v1.01
     */
    public TomcatInfo parseContent(String content) {
        TomcatInfo info = new TomcatInfo();
        String[] lines = content.split("\n");
        int i = 0;
        for (String perLine : lines) {
            if (perLine == null) {
                continue;
            }
            if (perLine.contains("<td class=\"header-center\"><small>")) {
                
            } else if (perLine.contains("<td class=\"row-center\"><small>")) {
                i++;
                String value = perLine.replace("<td class=\"row-center\"><small>", "").replace("</small></td>", "").trim();
                if (i == 1) {
                    info.setTomcatVersion(value);
                } else if (i == 2) {
                    info.setJVMVersion(value);
                } else if (i == 3) {
                    info.setJVMVendor(value);
                } else if (i == 4) {
                    info.setOSName(value);
                } else if (i == 5) {
                    info.setOSVersion(value);
                }  else if (i == 6) {
                    info.setOSArchitecture(value);
                }
            } else if (perLine.contains("<h1>JVM</h1>")) {
                String JVMInfo = perLine.substring(perLine.indexOf("<h1>JVM</h1>"));
                JVMInfo = JVMInfo.substring(0, JVMInfo.indexOf("</p>"));
                JVMInfo = JVMInfo.replace("<h1>JVM</h1><p>", "").trim();
                System.out.println(JVMInfo);
                String[] perJVMInfo = JVMInfo.split("MB");
                int j = 0;
                for (String string : perJVMInfo) {
                    String[] temp = string.trim().split(":");
                    if (j == 0) {
                        info.setFreeMemory(temp[1].replaceAll("MB", "").trim());
                    } else if (j == 1) {
                        info.setTotalMemory(temp[1].replaceAll("MB", "").trim());
                    } else if (j == 2) {
                        info.setMaxMemory(temp[1].replaceAll("MB", "").trim());
                    }
                    j++ ;
                }
            }
        }
        return info;
    }
    /**
     * main:
     * <p>
     *
     * @param args
     *
     * @since   v1.01
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

