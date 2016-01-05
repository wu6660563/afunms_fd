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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 7, 2013 3:59:43 PM
 */
public class ServerConnector {

    private static final SysLogger logger = SysLogger.getLogger(ServerConnector.class);

    /**
     * host:
     * <p>��������ַ
     *
     * @since   v1.01
     */
    protected String host;

    /**
     * port:
     * <p>�˿�
     *
     * @since   v1.01
     */
    protected int port;

    /**
     * path:
     * <p>·��
     *
     * @since   v1.01
     */
    protected String path;

    /**
     * userName:
     * <p>�û���
     *
     * @since   v1.01
     */
    protected String userName;

    /**
     * password:
     * <p>����
     *
     * @since   v1.01
     */
    protected String password;

    /**
     * isAuth:
     * <p>�Ƿ���֤
     *
     * @since   v1.01
     */
    public boolean isAuth;

    /**
     * domain:
     * <p>��
     *
     * @since   v1.01
     */
    protected String domain;

    /**
     * filter:
     * <p>������
     *
     * @since   v1.01
     */
    protected String filter;

    /**
     * isConnect:
     * <p>�Ƿ����ӳɹ�
     *
     * @since   v1.01
     */
    protected boolean isConnect;

    /**
     * inputStream:
     * <p>������
     *
     * @since   v1.01
     */
    protected InputStream inputStream;

    /**
     * getHost:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getHost() {
        return host;
    }

    /**
     * setHost:
     * <p>��������
     *
     * @param   host
     *          - ����
     * @since   v1.01
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * getPort:
     * <p>��ȡ�˿�
     *
     * @return  {@link Integer}
     *          - �˿�
     * @since   v1.01
     */
    public int getPort() {
        return port;
    }

    /**
     * setPort:
     * <p>���ö˿�
     *
     * @param   port
     *          - �˿�
     * @since   v1.01
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getPath:
     * <p>��ȡ·��
     *
     * @return  {@link String}
     *          - ·��
     * @since   v1.01
     */
    public String getPath() {
        return path;
    }

    /**
     * setPath:
     * <p>����·��
     *
     * @param   path
     *          - ·��
     * @since   v1.01
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * getUserName:
     * <p>��ȡ�û���
     *
     * @return  {@link String}
     *          - �û���
     * @since   v1.01
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName:
     * <p>�����û���
     *
     * @param   userName
     *          - �û���
     * @since   v1.01
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getPassword:
     * <p>��ȡ����
     *
     * @return  {@link String}
     *          - ����
     * @since   v1.01
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword:
     * <p>��������
     *
     * @param   password
     *          - ����
     * @since   v1.01
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * isAuth:
     * <p>�����Ҫ��֤���򷵻� <code>true</code> �����򷵻� <code>false</code>
     *
     * @return  {@link Boolean}
     *          - �����Ҫ��֤���򷵻� <code>true</code> �����򷵻� <code>false</code>
     * @since   v1.01
     */
    public boolean isAuth() {
        return isAuth;
    }

    /**
     * setAuth:
     * <p>�����Ҫ��֤�������� <code>true</code> ���������� <code>false</code>
     *
     * @param   isAuth
     *          - �����Ҫ��֤�������� <code>true</code> ���������� <code>false</code>
     * @since   v1.01
     */
    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     * getDomain:
     * <p>��ȡ��
     *
     * @return  {@link String}
     *          - ��
     * @since   v1.01
     */
    public String getDomain() {
        return domain;
    }

    /**
     * setDomain:
     * <p>������
     *
     * @param   domain
     *          - ��
     * @since   v1.01
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * getFilter:
     * <p>��ȡ������
     *
     * @return  {@link String}
     *          - ������
     * @since   v1.01
     */
    public String getFilter() {
        return filter;
    }

    /**
     * setFilter:
     * <p>���ù�����
     *
     * @param   filter
     *          - ������
     * @since   v1.01
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    /**
     * isConnect:
     * <p>������ӳɹ����򷵻�Ϊ <code>true</code>, ����Ϊfalse
     *
     * @return  {@link Boolean}
     *          - ������ӳɹ����򷵻�Ϊ <code>true</code>, ����Ϊfalse
     * @since   v1.01
     */
    public boolean isConnect() {
        return isConnect;
    }

    /**
     * setConnect:
     * <p>������ӳɹ���������Ϊ <code>true</code>, ����Ϊfalse
     *
     * @param   isConnect
     *          - ������ӳɹ���������Ϊ <code>true</code>, ����Ϊfalse
     * @since   v1.01
     */
    public void setConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    /**
     * getInputStream:
     * <p>��ȡ������
     *
     * @return  {@link InputStream}
     *          - ������
     * @since   v1.01
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * setInputStream:
     * <p>����������
     *
     * @param   inputStream
     *          - ������
     * @since   v1.01
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * getContent:
     * <p>��ȡ����
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
            logger.info("���ӣ�" + url);
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
            logger.error("Tomcat http://" + ip + ":" + port + "  ���ӷ�����ʧ��");
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