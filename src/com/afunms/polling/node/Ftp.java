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
     * <p>用户名
     *
     * @since   v1.01
     */
    private String username;
    
    /**
     * password:
     * <p>密码
     *
     * @since   v1.01
     */
    private String password;

    /**
     * filename:
     * <p>文件名
     *
     * @since   v1.01
     */
    private String filename;

    /**
     * remotePath:
     * <p> FTP 服务器路径
     *
     * @since   v1.01
     */
    private String remotePath;

    /**
     * port:
     * <p>端口
     *
     * @since   v1.01
     */
    private int port;

    /**
     * timeout:
     * <p>超时时间
     *
     * @since   v1.01
     */
    private int timeout;
    /**
     * supperid:
     * <p>供应商
     *
     * @since   v1.01
     */
    private int supperid;
    
    /**
     * monflag:
     * <p>是否监视 0为false 1为true
     *
     * @since   v1.01
     */
    private int monflag;
    
    /**
     * bid:
     * <p>所属业务
     *
     * @since   v1.01
     */
    private String bid;
    
    /**
     * sendmobiles:
     * <p>短信接收人(不再使用)
     *
     * @since   v1.01
     */
    private String sendmobiles;
    
    /**
     * sendemail:
     * <p>email接收人(不再使用)
     *
     * @since   v1.01
     */
    private String sendemail;
    
    /**
     * sendphone:
     * <p>电话接收人(不再使用)
     *
     * @since   v1.01
     */
    private String sendphone;

    /**
     * getUsername:
     * <p>获取用户名
     *
     * @return  {@link String}
     *          - 用户名
     * @since   v1.01
     */
    public String getUsername() {
        return username;
    }

    /**
     * setUsername:
     * <p>设置用户名
     *
     * @param   username
     *          - 用户名
     * @since   v1.01
     */
    public void setUsername(String username) {
        this.username = username;
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
     * getFilename:
     * <p>获取文件名
     *
     * @return  {@link String}
     *          - 文件名
     * @since   v1.01
     */
    public String getFilename() {
        return filename;
    }

    /**
     * setFilename:
     * <p>设置文件名
     *
     * @param   filename
     *          - 文件名
     * @since   v1.01
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * getRemotePath:
     * <p>获取 ftp 服务器路径
     *
     * @return  String
     *          - ftp 服务器路径
     * @since   v1.01
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * setRemotePath:
     * <p>设置 ftp 服务器路径
     *
     * @param   remotePath
     *          - ftp 服务器路径
     * @since   v1.01
     */
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
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
     * getTimeout:
     * <p>获取超时时间
     *
     * @return  {@link Integer}
     *          - 超时时间
     * @since   v1.01
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setTimeout:
     * <p>设置超时时间
     *
     * @param   timeout
     *          - 超时时间
     * @since   v1.01
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * getSupperid:
     * <p>获取供应商 id
     *
     * @return  {@link Integer}
     *          - 供应商 id
     * @since   v1.01
     */
    public int getSupperid() {
        return supperid;
    }

    /**
     * setSupperid:
     * <p>设置供应商 id
     *
     * @param   supperid
     *          - 供应商 id
     * @since   v1.01
     */
    public void setSupperid(int supperid) {
        this.supperid = supperid;
    }

    /**
     * getMonflag:
     * <p>是否监控，如果是则返回 1，否则返回 0
     *
     * @return  {@link Integer}
     *          - 是否监控，如果是则返回 1，否则返回 0
     * @since   v1.01
     */
    public int getMonflag() {
        return monflag;
    }

    /**
     * setMonflag:
     * <p>设置是否监控，如果是则返回 1，否则返回 0
     *
     * @param   monflag
     *          - 是否监控，如果是则返回 1，否则返回 0
     * @since   v1.01
     */
    public void setMonflag(int monflag) {
        this.monflag = monflag;
    }

    /**
     * getBid:
     * <p>获取所属业务
     *
     * @return  {@link String}
     *          - 所属业务
     * @since   v1.01
     */
    public String getBid() {
        return bid;
    }

    /**
     * setBid:
     * <p>设置所属业务
     *
     * @param   bid
     *          - 所属业务
     * @since   v1.01
     */
    public void setBid(String bid) {
        this.bid = bid;
    }

    /**
     * getSendmobiles:
     * <p>获取短信接收人(不再使用)
     *
     * @return  {@link String}
     *          - 短信接收人(不再使用)
     * @since   v1.01
     */
    public String getSendmobiles() {
        return sendmobiles;
    }

    /**
     * setSendmobiles:
     * <p>设置短信接收人(不再使用)
     *
     * @param   sendmobiles
     *          - 短信接收人(不再使用)
     * @since   v1.01
     */
    public void setSendmobiles(String sendmobiles) {
        this.sendmobiles = sendmobiles;
    }

    /**
     * getSendemail:
     * <p>获取邮件接收人(不再使用)
     *
     * @return  {@link String}
     *          - 邮件接收人(不再使用)
     * @since   v1.01
     */
    public String getSendemail() {
        return sendemail;
    }

    /**
     * setSendemail:
     * <p>设置邮件接收人(不再使用)
     *
     * @param   sendemail
     *          - 邮件接收人(不再使用)
     * @since   v1.01
     */
    public void setSendemail(String sendemail) {
        this.sendemail = sendemail;
    }

    /**
     * getSendphone:
     * <p>获取电话接收人(不再使用)
     *
     * @return  {@link String}
     *          - 电话接收人(不再使用)
     * @since   v1.01
     */
    public String getSendphone() {
        return sendphone;
    }

    /**
     * setSendphone:
     * <p>设置电话接收人(不再使用)
     *
     * @param   sendphone
     *          - 电话接收人(不再使用)
     * @since   v1.01
     */
    public void setSendphone(String sendphone) {
        this.sendphone = sendphone;
    }

}