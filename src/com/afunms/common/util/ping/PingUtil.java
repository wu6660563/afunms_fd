/*
 * @(#)PingUtil.java     v1.01, 2013 8 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.common.util.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.poi3.hssf.record.formula.AddPtg;

/**
 * ClassName:   PingUtil.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 8 29 09:14:10
 */
public class PingUtil {

    /**
     * COMMAND:
     * <p>命令
     *
     * @since   v1.01
     */
    private static String COMMAND;

    /**
     * DEFAULT_COMMAND:
     * <p>默认命令
     *
     * @since   v1.01
     */
    private static String DEFAULT_COMMAND;

    /**
     * OS_NAME:
     * <p>操作系统
     *
     * @since   v1.01
     */
    private static final String OS_NAME = System.getProperty("os.name");

    /**
     * ping:
     * <p> Ping 地址
     *
     * @param   ipAddress
     *          - IP地址
     * @return  {@link Integer[]}
     *          - 
     *
     * @since   v1.01
     */
    public static int[] ping(String ipAddress) {
        String data = execute(ipAddress);
        return parseResult(data);
    }

    public static String getCommand() {
        if (COMMAND == null) {
            String command = System.getProperty("pingcommand");
            if(command != null) {
                COMMAND = command;
            } else {
                COMMAND = getDefaultCommand();
            }
        }
        return COMMAND;
    }

    /**
     * getDefaultCommand:
     * <p>获取默认命令
     *
     * @return  {@link String}
     *          - 默认命令
     *
     * @since   v1.01
     */
    public static String getDefaultCommand() {
        return DEFAULT_COMMAND;
    }

    public static String execute(String ipAddress){
        StringBuffer data = new StringBuffer();
        Process process = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            String command = getCommand() + ipAddress;
            process = Runtime.getRuntime().exec(command);
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                data.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }

    public static int[] parseResult(String data) {
        int[] result = new int[2];
        if (data == null || data.length() == 0) {
            return result;
        }
        String[] dataArray = data.split("\n");
        int sign = 0;

        int connect = 0;
        int responseTime = 0;
        for (String line : dataArray) {
            line = line.trim().replaceAll("数据包","Packets").replaceAll("已发送","Sent").replaceAll("已接收","Received").replaceAll("丢失","Lost")
                    .replaceAll("最短","Minimum").replaceAll("最长","Maximum").replaceAll("平均","Average").replaceAll("，",",")
                    .replaceAll("往返行程的估计时间(以毫秒为单位):", "Approximate round trip times in milli-seconds:").replaceAll("TTL 传输中过期。", "TTL expired in transit.")
                    .replaceAll("目标网无法访问。", "Destination net unreachable.").replaceAll("无法访问目标主机", "Destination net unreachable.").replaceAll("请求超时。", "Request timed out.");
            if (line.trim().indexOf("往返行程的估计时间") != -1) {
                line = "Approximate round trip times in milli-seconds:";
            }
            if( line.indexOf("expired in transit") != -1 || line.indexOf("unreachable") != -1){
                sign = 1;
            }
            if (line.toLowerCase().indexOf("packets") >= 0) {
                if (sign == 1) {
                    int a = line.indexOf("Received = ");
                    String str = line.substring(0, a + 10);
                    String str1 = line.substring(a + 12);
                    line = str + "0" + str1;
                }
                connect = parseConnect(line);
            } else {
                String[] packetLine = line.split(" ");
                if (packetLine[1].equalsIgnoreCase("packets") && packetLine[2].equalsIgnoreCase("transmitted,")) {
                    connect = Integer.parseInt(packetLine[3]) * 100 / Integer.parseInt(packetLine[0]);
                }
            }
            if (line.contains("=")) {
                String[] lines = line.split(",");
                if (lines.length >= 3) {
                    String values0= lines[0].substring(0, lines[0].indexOf("=")).trim();
                    if (values0.equalsIgnoreCase("Minimum")) {
                        responseTime = parseResponseTime(line);
                    }
                }
            } else {
                String[] lines = line.split(" ");
                if (lines.length >= 3) {
                    if (lines[0].trim().equalsIgnoreCase("rtt")) {
                        String[] avgtime = lines[3].trim().split("/");
                        responseTime = new Integer(avgtime[1] + "");
                    }               
                }
            }
        }
        result[0] = connect;
        result[1] = responseTime;
        return result;
    }

    /**
     * parseConnect:
     * <p>解析连通率
     *
     * @param   line
     *          - 数据
     * @return  {@link Integer}
     *          - 连通率
     *
     * @since   v1.01
     */
    public static int parseConnect(String line) {
        String[] lines = line.split(",");
        int connect = 0;
        if (lines.length >= 3) {
            if(OS_NAME.startsWith("Windows")) {
                String values0 = lines[0].substring(lines[0].indexOf("=") + 1, lines[0].length()).trim();
                String values1 = lines[1].substring(lines[1].indexOf("=") + 1, lines[1].length()).trim();
                connect = Integer.parseInt(values1) * 100 / Integer.parseInt(values0);
            } if(OS_NAME.startsWith("Linux")) {
                String values0 = lines[0].substring(0, lines[0].indexOf("packets") -1).trim();
                String values1 = lines[1].substring(0, lines[1].indexOf("received") -1).trim();
                connect = Integer.parseInt(values1) * 100 / Integer.parseInt(values0);
            }
        }
        return connect;
    }

    public static int parseResponseTime(String line){
        String[] lines = line.split(",");
        int responseTime = 0;
        if (lines.length >= 3) {
            if(OS_NAME.startsWith("Windows")){
                String values2 = lines[2].substring(lines[2].indexOf("=") + 1, lines[2].length()).trim();
                if (values2 != null) {
                    values2 = values2.replaceAll("ms", "");
                    responseTime = Integer.parseInt(values2.trim());
                }
            }
         }
         return responseTime;
    }

    static {
        if (OS_NAME.startsWith("SunOS") || OS_NAME.startsWith("Solaris")) {
            DEFAULT_COMMAND = "/usr/sbin/ping ";
        } else  if(OS_NAME.startsWith("Linux")) {
            DEFAULT_COMMAND = "/bin/ping -c 10 -i 2 ";
        } else  if(OS_NAME.startsWith("FreeBSD")) {
            DEFAULT_COMMAND = "/sbin/ping -c 3";
        } else if(OS_NAME.startsWith("Windows")) {
            DEFAULT_COMMAND = "ping -n 3 -w 4000 ";
        }
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
        String ipAddressTemp = "192.168.28.";
        String[] ipAddress = new String[255];
        for (int i = 0; i < ipAddress.length; i++) {
            ipAddressTemp = ipAddressTemp + i;
        }
//        ping("192.168.28.50");
    }

}

