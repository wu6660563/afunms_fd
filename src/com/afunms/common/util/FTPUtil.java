/*
 * @(#)FTPUtil.java     v1.01, Dec 25, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * ClassName: FTPUtil.java
 * <p>{@link FTPUtil} 用于 FTP 上传下载功能
 * 
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 25, 2012 2:24:44 PM
 */
public class FTPUtil {

    public static final int NO_CONNECT = -1;

    public static final int INCORRECT_PASSWORD = -2;

    public static final int NO_EIXST_FILE = 0;

    public static final int SUCCESS = 1;

    public static String getErrorInfo(int code) {
        switch (code) {
        case NO_CONNECT:
            return "无法连接服务器";

        case INCORRECT_PASSWORD:
            return "用户名或密码不正确";
            
        case NO_EIXST_FILE:
            return "文件不存在";

        case SUCCESS:
            return "成功";

        default:
            return "无法连接服务器";
        }
    }

    /**
     * download:
     * <p>下载
     *
     * @param   host
     *          - 服务器 IP
     * @param   port
     *          - 端口
     * @param   userName
     *          - 用户名
     * @param   password
     *          - 密码
     * @param   fileName
     *          - 下载文件名
     * @param   localPath
     *          - 下载文件的本地存放目录
     * @param   remotePath
     *          - 下载文件存放的服务器目录
     * @return  {@link Integer}
     *          - 返回下载结果，-1 为连接服务器失败，-2为登录失败（用户名密码错误），0为该文件不存在，1为下载成功
     *
     * @since   v1.01
     */
    public static int download(String host, int port, String userName,
            String password, String fileName, String localPath, String remotePath) {
        int result = NO_EIXST_FILE;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            if (!ftpClient.login(userName, password)) {
                result = -2;
            }
            if (remotePath != null && remotePath.trim().length() > 0) {
                ftpClient.changeWorkingDirectory(remotePath);
            }
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.getName().equals(fileName)) {
                    File localFile = new File(localPath + fileName);
                    OutputStream outputStream = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), outputStream);
                    outputStream.close();
                    result = SUCCESS;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            result = NO_CONNECT;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = NO_EIXST_FILE;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                result = NO_CONNECT;
            }
        }
        return result;
    }

    public static int upload(String host, int port, String user,
            String pwd, String fileName, String localPath, String remotePath) {
        int result = NO_EIXST_FILE;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            if (ftpClient.login(user, pwd)) {
                result = INCORRECT_PASSWORD;
            }
            if (remotePath != null) {
                ftpClient.changeWorkingDirectory(remotePath);
            }
            InputStream inputStream = new FileInputStream(localPath + fileName);
            ftpClient.storeFile(remotePath, inputStream);
            inputStream.close();
            ftpClient.disconnect();
            result = SUCCESS;
        } catch (SocketException e) {
            e.printStackTrace();
            result = NO_CONNECT;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = NO_EIXST_FILE;
        } catch (IOException e) {
            e.printStackTrace();
            result = NO_EIXST_FILE;
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                result = NO_CONNECT;
            }
        }
        return result;
    }

    /**
     * main:
     * <p>
     * 主方法，用于测试
     * 
     * @param args
     * 
     * @since v1.01
     */
    public static void main(String[] args) {

    }
}
