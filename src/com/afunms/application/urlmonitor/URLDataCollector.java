/*
 * @(#)URLDataCollector.java     v1.01, Mar 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.urlmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.afunms.application.model.Urlmonitor_realtime;
import com.afunms.common.util.SysLogger;

/**
 * ClassName:   URLDataCollector.java
 * <p>{@link URLDataCollector} URL 数据获取类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 18, 2013 5:15:57 PM
 */
public class URLDataCollector {

    /**
     * getData:
     * <p>获取数据
     *
     * @param   urlString
     *          - {@link String} 类型 <code>url</code> 地址
     * @param   timeOut
     *          - 超时时间
     * @return  {@link String}
     *          - 返回 {@link String} 类型的页面内容
     *
     * @since   v1.01
     */
    public static String getData(String urlString, int timeOut) {
        String data = null;
        try {
//            URL url = new URL(urlString);
//            URLConnection connection = url.openConnection();
//            connection.setConnectTimeout(timeOut);
//            connection.setAllowUserInteraction(true);
//            connection.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(connection
//                    .getInputStream());
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String content = "";
//            String line = null;
//            while ((line = bufferedReader.readLine()) != null) {
//                // 读取当前日期的日志文件，要根据读取行数定位。
//                content += line + "\n";
//            }
//            logger.info(content);
//            bufferedReader.close();
//            inputStreamReader.close();
            HttpClient client = new HttpClient();
            GetMethod getMethod = new GetMethod(urlString);
            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());   
            int statusCode = client.executeMethod(getMethod);   
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());   
            }
            //读取内容    
            byte[] responseBody = getMethod.getResponseBody();   
            //处理内容   
            String charset = getMethod.getResponseCharSet();//获得编码信息
            data = new String(responseBody, charset);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static long getResponseTime(String urlString, int timeOut) {
        long responseTime = 0;
        try {
            URL url = new URL(urlString);
            long time = System.currentTimeMillis();
            URLConnection con = url.openConnection();
            con.setConnectTimeout(timeOut);
            responseTime = System.currentTimeMillis() - time;
            if (responseTime == 0) {
                responseTime = new Double(Math.random() * 5).intValue() + 1;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseTime;
    }

    public static void main(String[] args) {
        System.out.println(getData("http://127.0.0.1:8080/afunms", 5000));
    }
}

