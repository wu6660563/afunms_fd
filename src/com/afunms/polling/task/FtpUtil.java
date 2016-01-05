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
					//远程文件目录
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
						FtpUtil ftputil = new FtpUtil("127.0.0.1",21,"hongli","hongli1","E:/ftp/","D:/Tomcat5.0/webapps/afunms/ftpupload","aix服务器_cpu.xml");
//						ftputil.ftpOne();
						ftputil.uploadFile("127.0.0.1", "hongli", "hongli", "D:/Tomcat5.0/webapps/afunms/ftpupload/aix服务器_cpu.xml");
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

				//System.out.println("正在执行连接" + ip + "  服务器   " +serverfilepath+ "   File");
				
				//设置登陆信息	
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
				
				//设置远程源文件 
				CoFile file = new FtpFile(remotefilepath, ftp);				
				//设置本地下载目录
				CoFile to = new LocalFile(serverfilepath, File);
				//下载文件
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
				//断开连接
				ftp.disconnect();
			}
			//return false;
		}
		
		/**
		 * guangfei  add
		 * FTP上传文件
		 * ipaddress IP地址
		 * username 用户名
		 * password 密码
		 * path 上传文件的路径
		 * @return
		 */
		public boolean uploadFile(String ipaddress,String username ,String password,String path)
		{
			
			  // 初始表示上传失败
			  boolean success = false;
			  // 创建FTPClient对象
			  FTPClient ftp = new FTPClient();
			  try {
			   int reply;
			   // 连接FTP服务器
			   // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			   ftp.connect(ip);
			   // 登录ftp
			   ftp.login(username, password);
			   // 看返回的值是不是230，如果是，表示登陆成功
			   reply = ftp.getReplyCode();
			   File f = new File(path);
			   FileInputStream input = new FileInputStream(f);
			   // 将上传文件存储到指定目录
			   ftp.storeFile(f.getName(), input);
			   // 关闭输入流
			   input.close();
			   // 退出ftp
			   ftp.logout();
			   // 表示上传成功
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
