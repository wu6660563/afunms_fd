/*
 * Created on 2005-4-5
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import com.afunms.initialize.ResourceCenter;

import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpConnect;
import cz.dhl.ftp.FtpFile;
import cz.dhl.io.CoFile;
import cz.dhl.io.CoLoad;
import cz.dhl.io.LocalFile;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FtpUtil {
			private String File = "";
			private String id;
			private String ip = "";
			private	int port =21;
					//Զ���ļ�Ŀ¼
			private String filepath = "";
			private String username = "";
			private String password = "";
			private String serverpath = "";
	/**
	 * 
	 */

	public FtpUtil(String ip,int port,String username,String password,String filepath,String serverpath,String currentfile) {
			super();
			this.ip=ip;
			this.port=port;
			this.username=username;
			this.password=password;
			this.filepath=filepath;
			this.File=currentfile;
			this.serverpath=serverpath;
			// TODO Auto-generated constructor stub
		}


	public static void main(String[] args) {
					try {
						//FtpUtil ftputil=new FtpUtil();
						//ftputil.ftpOne();
						FtpUtil ftputil = new FtpUtil("127.0.0.1",21,"hongli","hongli1","E:/ftp/","D:/Tomcat5.0/webapps/afunms/ftpupload","aix������_cpu.xml");
//						ftputil.ftpOne();
						ftputil.uploadFile("127.0.0.1", "hongli", "hongli", "D:/Tomcat5.0/webapps/afunms/ftpupload/aix������_cpu.xml");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
	
		public boolean ftpOne() {
			Ftp ftp;
			ftp = new Ftp();
			try {
				FtpConnect ftpConnect;
				ftpConnect = new FtpConnect();
				//FtpFile ftpFile;
				String remotefilepath="";
				if (filepath==null || filepath.length()==0){
					remotefilepath=File;
				}else
					remotefilepath=filepath+File;
				String serverfilepath=serverpath;

				//System.out.println("����ִ������" + ip + "  ������   " +serverfilepath+ "   File");
				
				//���õ�½��Ϣ	
				ftpConnect.setHostName(ip);
				ftpConnect.setPortNum(port);
				ftpConnect.setUserName(username);
				ftpConnect.setPassWord(password);
				try{
					boolean flag = ftp.connect(ftpConnect);
					if (!flag) return false;
				}catch(IOException exp){
					return false;
				}
				
				//����Զ��Դ�ļ� 
				CoFile file = new FtpFile(remotefilepath, ftp);				
				//���ñ�������Ŀ¼
				CoFile to = new LocalFile(serverfilepath, File);
				//�����ļ�
				try{
					boolean flag = CoLoad.copy(to, file);	
					if (!flag) return false;
				}catch(Exception ex){
					return false;
				}
				return true;
			} catch (RuntimeException e) {
				e.printStackTrace();
				return false;
			}finally{
				//�Ͽ�����
				ftp.disconnect();
			}
			//return false;
		}
		
		/**
		 * guangfei  add
		 * FTP�ϴ��ļ�
		 * ipaddress IP��ַ
		 * username �û���
		 * password ����
		 * path �ϴ��ļ���·��
		 * @return
		 */
		public boolean uploadFile(String ipaddress,String username ,String password,String path)
		{
			
			  // ��ʼ��ʾ�ϴ�ʧ��
			  boolean success = false;
			  // ����FTPClient����
			  FTPClient ftp = new FTPClient();
			  try {
			   int reply;
			   // ����FTP������
			   // �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(url)�ķ�ʽֱ������FTP������
			   ftp.connect(ip);
			   // ��¼ftp
			   ftp.login(username, password);
			   // �����ص�ֵ�ǲ���230������ǣ���ʾ��½�ɹ�
			   reply = ftp.getReplyCode();
			   File f = new File(path);
			   FileInputStream input = new FileInputStream(f);
			   // ���ϴ��ļ��洢��ָ��Ŀ¼
			   ftp.storeFile(f.getName(), input);
			   // �ر�������
			   input.close();
			   // �˳�ftp
			   ftp.logout();
			   // ��ʾ�ϴ��ɹ�
			   success = true;
			  } catch (IOException e) {
			   e.printStackTrace();
			  } finally {
			   if (ftp.isConnected()) {
			    try {
			     ftp.disconnect();
			    } catch (IOException ioe) {
			    	ioe.printStackTrace();
			    }
			   }
			  }
			  return success;

		}
		/**
		 * @return
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param string
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return
		 */
		public String getServerpath() {
			return serverpath;
		}

		/**
		 * @param string
		 */
		public void setServerpath(String serverpath) {
			this.serverpath = serverpath;
		}

			/**
			 * @return
			 */
			public String getFile() {
				return File;
			}

					/**
					 * @return
					 */
					public String getFilepath() {
						return filepath;
					}

			/**
			 * @param string
			 */
			public void setFile(String string) {
				File = string;
			}

					/**
					 * @param string
					 */
					public void setFilepath(String string) {
						filepath = string;
					}

}
