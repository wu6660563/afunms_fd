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
 * <p>{@link FTPUtil} ���� FTP �ϴ����ع���
 * 
 * @author      ����
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
            return "�޷����ӷ�����";

        case INCORRECT_PASSWORD:
            return "�û��������벻��ȷ";
            
        case NO_EIXST_FILE:
            return "�ļ�������";

        case SUCCESS:
            return "�ɹ�";

        default:
            return "�޷����ӷ�����";
        }
    }

    /**
     * download:
     * <p>����
     *
     * @param   host
     *          - ������ IP
     * @param   port
     *          - �˿�
     * @param   userName
     *          - �û���
     * @param   password
     *          - ����
     * @param   fileName
     *          - �����ļ���
     * @param   localPath
     *          - �����ļ��ı��ش��Ŀ¼
     * @param   remotePath
     *          - �����ļ���ŵķ�����Ŀ¼
     * @return  {@link Integer}
     *          - �������ؽ����-1 Ϊ���ӷ�����ʧ�ܣ�-2Ϊ��¼ʧ�ܣ��û���������󣩣�0Ϊ���ļ������ڣ�1Ϊ���سɹ�
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
     * �����������ڲ���
     * 
     * @param args
     * 
     * @since v1.01
     */
    public static void main(String[] args) {

    }
}
