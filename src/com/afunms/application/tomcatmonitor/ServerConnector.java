/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.afunms.application.tomcatmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.afunms.common.util.SysLogger;

import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;

/**
 * ClassName:   ServerConnector.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 7, 2013 3:59:43 PM
 */
public class ServerConnector {

    private static final SysLogger logger = SysLogger.getLogger(ServerConnector.class);

    /**
     * host:
     * <p>服务器地址
     *
     * @since   v1.01
     */
    protected String host;

    /**
     * port:
     * <p>端口
     *
     * @since   v1.01
     */
    protected int port;

    /**
     * path:
     * <p>路径
     *
     * @since   v1.01
     */
    protected String path;

    /**
     * userName:
     * <p>用户名
     *
     * @since   v1.01
     */
    protected String userName;

    /**
     * password:
     * <p>密码
     *
     * @since   v1.01
     */
    protected String password;

    /**
     * isAuth:
     * <p>是否验证
     *
     * @since   v1.01
     */
    public boolean isAuth;

    /**
     * domain:
     * <p>域
     *
     * @since   v1.01
     */
    protected String domain;

    /**
     * filter:
     * <p>过滤器
     *
     * @since   v1.01
     */
    protected String filter;

    /**
     * isConnect:
     * <p>是否连接成功
     *
     * @since   v1.01
     */
    protected boolean isConnect;

    /**
     * inputStream:
     * <p>输入流
     *
     * @since   v1.01
     */
    protected InputStream inputStream;

    /**
     * getHost:
     * <p>获取主机
     *
     * @return  {@link String}
     *          - 主机
     * @since   v1.01
     */
    public String getHost() {
        return host;
    }

    /**
     * setHost:
     * <p>设置主机
     *
     * @param   host
     *          - 主机
     * @since   v1.01
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * getPort:
     * <p>获取端口
     *
     * @return  {@link Integer}
     *          - 端口
     * @since   v1.01
     */
    public int getPort() {
        return port;
    }

    /**
     * setPort:
     * <p>设置端口
     *
     * @param   port
     *          - 端口
     * @since   v1.01
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getPath:
     * <p>获取路径
     *
     * @return  {@link String}
     *          - 路径
     * @since   v1.01
     */
    public String getPath() {
        return path;
    }

    /**
     * setPath:
     * <p>设置路径
     *
     * @param   path
     *          - 路径
     * @since   v1.01
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * getUserName:
     * <p>获取用户名
     *
     * @return  {@link String}
     *          - 用户名
     * @since   v1.01
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName:
     * <p>设置用户名
     *
     * @param   userName
     *          - 用户名
     * @since   v1.01
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getPassword:
     * <p>获取密码
     *
     * @return  {@link String}
     *          - 密码
     * @since   v1.01
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword:
     * <p>设置密码
     *
     * @param   password
     *          - 密码
     * @since   v1.01
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * isAuth:
     * <p>如果需要验证，则返回 <code>true</code> ，否则返回 <code>false</code>
     *
     * @return  {@link Boolean}
     *          - 如果需要验证，则返回 <code>true</code> ，否则返回 <code>false</code>
     * @since   v1.01
     */
    public boolean isAuth() {
        return isAuth;
    }

    /**
     * setAuth:
     * <p>如果需要验证，则设置 <code>true</code> ，否则设置 <code>false</code>
     *
     * @param   isAuth
     *          - 如果需要验证，则设置 <code>true</code> ，否则设置 <code>false</code>
     * @since   v1.01
     */
    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     * getDomain:
     * <p>获取域
     *
     * @return  {@link String}
     *          - 域
     * @since   v1.01
     */
    public String getDomain() {
        return domain;
    }

    /**
     * setDomain:
     * <p>设置域
     *
     * @param   domain
     *          - 域
     * @since   v1.01
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * getFilter:
     * <p>获取过滤器
     *
     * @return  {@link String}
     *          - 过滤器
     * @since   v1.01
     */
    public String getFilter() {
        return filter;
    }

    /**
     * setFilter:
     * <p>设置过滤器
     *
     * @param   filter
     *          - 过滤器
     * @since   v1.01
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    /**
     * isConnect:
     * <p>如果连接成功，则返回为 <code>true</code>, 否者为false
     *
     * @return  {@link Boolean}
     *          - 如果连接成功，则返回为 <code>true</code>, 否者为false
     * @since   v1.01
     */
    public boolean isConnect() {
        return isConnect;
    }

    /**
     * setConnect:
     * <p>如果连接成功，则设置为 <code>true</code>, 否者为false
     *
     * @param   isConnect
     *          - 如果连接成功，则设置为 <code>true</code>, 否者为false
     * @since   v1.01
     */
    public void setConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    /**
     * getInputStream:
     * <p>获取输入流
     *
     * @return  {@link InputStream}
     *          - 输入流
     * @since   v1.01
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * setInputStream:
     * <p>设置输入流
     *
     * @param   inputStream
     *          - 输入流
     * @since   v1.01
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * getContent:
     * <p>获取内容
     *
     * @return
     *
     * @since   v1.01
     */
    public String getContent() {
        InputStream inputStream = getInputStream();
        if (inputStream == null) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer content = new StringBuffer();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                content.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    public boolean connect(String ip, int port, String target,
            String realm, String user, String password, boolean isAuth) {
        try {
            URL url = new URL("http://" + ip + ":" + port);
            logger.info("连接：" + url);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            con.setRequestMethod("GET");
//            con.addRequestProperty("username", user);
//            con.addRequestProperty("password", password);
//            int code = con.getResponseCode();
//            System.out.println(code);
//            if (code == HttpURLConnection.HTTP_OK) {
//                setInputStream(con.getInputStream());
//            }

//            HTTPResponse rsp = con.Get(target);
//            System.out.println(rsp);//
            HTTPConnection con = new HTTPConnection(url);
            if (isAuth == true) {
                con.addBasicAuthorization(realm, user.trim(), password.trim());
            }
            HTTPResponse rsp = con.Get(target);
            System.out.println(rsp);
            setInputStream(rsp.getInputStream());
            setConnect(true);
        } catch (Exception e) {
            setConnect(false);
            logger.error("Tomcat http://" + ip + ":" + port + "  连接服务器失败");
        }
        return isConnect();
    }

    public void disConnect() {
        try {
            if (getInputStream() != null) {
                getInputStream().close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}