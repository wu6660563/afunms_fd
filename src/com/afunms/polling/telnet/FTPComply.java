package com.afunms.polling.telnet;


/**
 * �ó�����������FTP���������������
 * connectServer();���Է��������������
 * loadFile();���Է����������ļ����
 * uploadFile();���Է������ϴ��ļ����
 */
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import org.apache.log4j.Logger;

public class FTPComply {
    
    private  Logger logger= Logger.getLogger(FTPComply.class);
    public  String conmes=""; //����FTP�������쳣��Ϣ
    public  String uploadmes=""; //FTP�ϴ�����ִ�н���쳣��Ϣ
    public  String downloadmes="";	//FTP��������ִ�н���쳣��Ϣ
    private boolean binaryTransfer = false;
    /**
     * 
     * ���ӵ�������
     * @return true ���ӷ������ɹ���false ���ӷ�����ʧ��
     */
    public boolean connectServer(String ipaddress,int port,String userName,String password,int timeOut) 
    {
    	FTPClient ftpClient = null;
    	boolean flag = true; 
        if (ftpClient == null) { 
        	int reply; 
        	try { 
        		ftpClient = new FTPClient(); 
        		ftpClient.setControlEncoding("UTF-8"); 
        		ftpClient.setDefaultPort(port); 
        		ftpClient.connect(ipaddress); 

        		reply = ftpClient.getReplyCode(); 
        		
        		ftpClient.setDataTimeout(timeOut); 
        		if (!FTPReply.isPositiveCompletion(reply)) { 
        			ftpClient.disconnect(); 
        			//System.err.println("FTP server refused connection."); 
        			flag = false; 
                }
        	} catch (SocketException e) { 
        		flag = false; 
        		e.printStackTrace(); 
        		logger.error("��¼ftp������ " + ipaddress + " ʧ��,���ӳ�ʱ��"); 
        	} catch (IOException e) { 
        		flag = false; 
        		e.printStackTrace(); 
        		logger.error("��¼ftp������ " + ipaddress + " ʧ�ܣ�FTP�������޷��򿪣�"); 
        	} 
        } 
        return flag; 
    } 
    
    
    /** 
     * �����ļ� 
     * 
     * @param remoteFileName --�������ϵ��ļ��� 
     * @param localFileName--�����ļ��� 
     * @return true ���سɹ���false ����ʧ�� 
     */
    public boolean loadFile(String remoteFileName,String localFileName,String ipaddress,int port,String userName,String password,int timeOut) 
    { 
    	FTPClient ftpClient = new FTPClient();
    	boolean flag = true; 
        // �����ļ� 
        BufferedOutputStream buffOut = null; 
        try { 
        	ftpClient.setDefaultPort(port); 
        	ftpClient.connect(ipaddress); 
            ftpClient.login(userName, password); 

        	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 

        	buffOut = new BufferedOutputStream(new FileOutputStream(localFileName)); 
        	flag = ftpClient.retrieveFile(remoteFileName, buffOut); 
        	if (flag == true) { 
        		logger.info("�����ļ��ɹ���"); 
        	} else { 
        		logger.info("�����ļ�ʧ�ܣ�"); 
        	}
        	buffOut.close();
        } catch (Exception e) { 
                e.printStackTrace(); 
                flag=false;
                logger.debug("�����ļ�����ʧ�ܣ�", e); 
        } finally { 
        	try { 
        		if (buffOut != null) 
        			buffOut.close(); 
        		ftpClient.disconnect();
        	} catch (Exception e) { 
        		e.printStackTrace(); 
        	} 
        } 
        
        return flag; 
    } 
    /**  
     * �ϴ�һ�������ļ���Զ��ָ���ļ�  
     *   
     * @param remoteAbsoluteFile  
     *            Զ���ļ���(��������·��)  
     * @param localAbsoluteFile  
     *            �����ļ���(��������·��)  
     * @return �ɹ�ʱ������true��ʧ�ܷ���false  
     */  
    public boolean uploadFile(String remoteAbsoluteFile,String localAbsoluteFile,String ipaddress,int port,String userName,String password,int timeOut) 
    {
    	FTPClient ftpClient = new FTPClient(); 
    	boolean flag = true; 
    	InputStream input = null;   
    	try {
        	ftpClient.setDefaultPort(port); 
        	ftpClient.connect(ipaddress); 
            ftpClient.login(userName, password); 

            
            //�����ļ���������   
            if (binaryTransfer) {   
            	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);   
            } else {   
            	ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);   
            }   
            // ������ 
            input = new FileInputStream(localAbsoluteFile);   
            flag=ftpClient.storeFile(remoteAbsoluteFile, input);
            if (flag == true) { 
        		logger.info("�ϴ��ļ��ɹ���"); 
        	} else { 
        		logger.info("�ϴ��ļ�ʧ�ܣ�");
        	} 
            input.close();   
            logger.debug("delete " + localAbsoluteFile);   
        } catch (Exception e) { 
            e.printStackTrace(); 
            flag=false;
            logger.debug("�����ļ�����ʧ�ܣ�", e); 
        } finally { 
        	try { 
        		if (input != null) 
        			input.close(); 
        		ftpClient.disconnect();
        	} catch (Exception e) { 
        		e.printStackTrace(); 
        	} 
        } 
        
        return flag;   
    }   

    public static void main(String[] args)
    {
    	FTPComply ft = new FTPComply();
    	long last = System.currentTimeMillis();
    	ft.connectServer("10.10.152.254",21,"admin","admin",12000);
    	String connect_time = (System.currentTimeMillis()-last)+"";   //���ӷ�������Ӧʱ��
    	System.out.println("����ʱ��="+connect_time);
    	long download_last = System.currentTimeMillis();
    	//ft.loadFile("/demo.txt","D:\\demo2.txt","192.168.1.103",21,"nms","webnms",12000);
    	//String download_time = (System.currentTimeMillis()-download_last)+""; //����ʱ��
    	//System.out.println("����ʱ��="+download_time);
    	long upload_last=System.currentTimeMillis();
    	ft.uploadFile("/cfg1.cfg", "C:\\Documents and Settings\\GZM\\cfg1.cfg","10.10.152.254",21,"admin","admin",12000);
        //ft.uploadFile(new File("D:\\logs.tar"), new File("D:\\BC\\logs.tar"), "logs.tar","192.168.1.103",21,"nms","webnms",12000);
        String upload_time = (System.currentTimeMillis()-upload_last)+"";   //�ϴ�ʱ��
        System.out.println("�ϴ�ʱ��="+upload_time);
    	
    }
}
