/**
 * <p>Description:host,including server and exchange device</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-27
 */

package com.afunms.polling.node;

import com.afunms.polling.node.Application;

public class Ftp extends Application {

    /**
     * username:
     * <p>�û���
     *
     * @since   v1.01
     */
    private String username;
    
    /**
     * password:
     * <p>����
     *
     * @since   v1.01
     */
    private String password;

    /**
     * filename:
     * <p>�ļ���
     *
     * @since   v1.01
     */
    private String filename;

    /**
     * remotePath:
     * <p> FTP ������·��
     *
     * @since   v1.01
     */
    private String remotePath;

    /**
     * port:
     * <p>�˿�
     *
     * @since   v1.01
     */
    private int port;

    /**
     * timeout:
     * <p>��ʱʱ��
     *
     * @since   v1.01
     */
    private int timeout;
    /**
     * supperid:
     * <p>��Ӧ��
     *
     * @since   v1.01
     */
    private int supperid;
    
    /**
     * monflag:
     * <p>�Ƿ���� 0Ϊfalse 1Ϊtrue
     *
     * @since   v1.01
     */
    private int monflag;
    
    /**
     * bid:
     * <p>����ҵ��
     *
     * @since   v1.01
     */
    private String bid;
    
    /**
     * sendmobiles:
     * <p>���Ž�����(����ʹ��)
     *
     * @since   v1.01
     */
    private String sendmobiles;
    
    /**
     * sendemail:
     * <p>email������(����ʹ��)
     *
     * @since   v1.01
     */
    private String sendemail;
    
    /**
     * sendphone:
     * <p>�绰������(����ʹ��)
     *
     * @since   v1.01
     */
    private String sendphone;

    /**
     * getUsername:
     * <p>��ȡ�û���
     *
     * @return  {@link String}
     *          - �û���
     * @since   v1.01
     */
    public String getUsername() {
        return username;
    }

    /**
     * setUsername:
     * <p>�����û���
     *
     * @param   username
     *          - �û���
     * @since   v1.01
     */
    public void setUsername(String username) {
        this.username = username;
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
     * getFilename:
     * <p>��ȡ�ļ���
     *
     * @return  {@link String}
     *          - �ļ���
     * @since   v1.01
     */
    public String getFilename() {
        return filename;
    }

    /**
     * setFilename:
     * <p>�����ļ���
     *
     * @param   filename
     *          - �ļ���
     * @since   v1.01
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * getRemotePath:
     * <p>��ȡ ftp ������·��
     *
     * @return  String
     *          - ftp ������·��
     * @since   v1.01
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * setRemotePath:
     * <p>���� ftp ������·��
     *
     * @param   remotePath
     *          - ftp ������·��
     * @since   v1.01
     */
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
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
     * getTimeout:
     * <p>��ȡ��ʱʱ��
     *
     * @return  {@link Integer}
     *          - ��ʱʱ��
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>���ó�ʱʱ��
     *
     * @param   timeout
     *          - ��ʱʱ��
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * getSupperid:
     * <p>��ȡ��Ӧ�� id
     *
     * @return  {@link Integer}
     *          - ��Ӧ�� id
     * @since   v1.01
     */
    public int getSupperid() {
        return supperid;
    }

    /**
     * setSupperid:
     * <p>���ù�Ӧ�� id
     *
     * @param   supperid
     *          - ��Ӧ�� id
     * @since   v1.01
     */
    public void setSupperid(int supperid) {
        this.supperid = supperid;
    }

    /**
     * getMonflag:
     * <p>�Ƿ��أ�������򷵻� 1�����򷵻� 0
     *
     * @return  {@link Integer}
     *          - �Ƿ��أ�������򷵻� 1�����򷵻� 0
     * @since   v1.01
     */
    public int getMonflag() {
        return monflag;
    }

    /**
     * setMonflag:
     * <p>�����Ƿ��أ�������򷵻� 1�����򷵻� 0
     *
     * @param   monflag
     *          - �Ƿ��أ�������򷵻� 1�����򷵻� 0
     * @since   v1.01
     */
    public void setMonflag(int monflag) {
        this.monflag = monflag;
    }

    /**
     * getBid:
     * <p>��ȡ����ҵ��
     *
     * @return  {@link String}
     *          - ����ҵ��
     * @since   v1.01
     */
    public String getBid() {
        return bid;
    }

    /**
     * setBid:
     * <p>��������ҵ��
     *
     * @param   bid
     *          - ����ҵ��
     * @since   v1.01
     */
    public void setBid(String bid) {
        this.bid = bid;
    }

    /**
     * getSendmobiles:
     * <p>��ȡ���Ž�����(����ʹ��)
     *
     * @return  {@link String}
     *          - ���Ž�����(����ʹ��)
     * @since   v1.01
     */
    public String getSendmobiles() {
        return sendmobiles;
    }

    /**
     * setSendmobiles:
     * <p>���ö��Ž�����(����ʹ��)
     *
     * @param   sendmobiles
     *          - ���Ž�����(����ʹ��)
     * @since   v1.01
     */
    public void setSendmobiles(String sendmobiles) {
        this.sendmobiles = sendmobiles;
    }

    /**
     * getSendemail:
     * <p>��ȡ�ʼ�������(����ʹ��)
     *
     * @return  {@link String}
     *          - �ʼ�������(����ʹ��)
     * @since   v1.01
     */
    public String getSendemail() {
        return sendemail;
    }

    /**
     * setSendemail:
     * <p>�����ʼ�������(����ʹ��)
     *
     * @param   sendemail
     *          - �ʼ�������(����ʹ��)
     * @since   v1.01
     */
    public void setSendemail(String sendemail) {
        this.sendemail = sendemail;
    }

    /**
     * getSendphone:
     * <p>��ȡ�绰������(����ʹ��)
     *
     * @return  {@link String}
     *          - �绰������(����ʹ��)
     * @since   v1.01
     */
    public String getSendphone() {
        return sendphone;
    }

    /**
     * setSendphone:
     * <p>���õ绰������(����ʹ��)
     *
     * @param   sendphone
     *          - �绰������(����ʹ��)
     * @since   v1.01
     */
    public void setSendphone(String sendphone) {
        this.sendphone = sendphone;
    }

}