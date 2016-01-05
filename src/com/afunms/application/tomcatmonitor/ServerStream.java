/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.afunms.application.tomcatmonitor;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.net.URL;
import java.text.SimpleDateFormat;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.afunms.common.util.SysLogger;

import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ServerStream {
	StringBuffer table_tag = new StringBuffer();

	int sServerTag;

	int eServerTag;

	int jvm_tag;

	HashMap map;

	Hashtable hServer;

	String sMain;

	Hashtable hServerTag, hServerFi;

	Hashtable jvm_info;

	Hashtable port_sum1;

	Hashtable detail_sum1;

	Hashtable port_sum2;

	Hashtable detail_sum2;

	public Hashtable data_ht;

	String nexttime;
	
	String time;

	String interval;

	static Logger logger = Logger.getLogger(ServerStream.class);

	public ServerStream() throws Exception {
		PropertyConfigurator.configure(this.getClass().getClassLoader().getResource(
				"log4j.properties"));
		
	}

	/**
	 * 
	 * foundData:
	 * <p>该方法 不再使用
	 *
	 * @param host
	 * @param port
	 * @param user
	 * @param pass
	 *
	 * @since   v1.01
	 */
	@Deprecated
	public void foundData(String host, String port, String user, String pass) {
		ServerConnector sc = new ServerConnector();

//		sc.setWebServerHost(host);
//		sc.setWebServerPort(Integer.parseInt(port));
//		sc.setUser(user);
//		sc.setPass(pass);
//		sc.setStatusPath("/manager/status/");
		try {
//			sc.start();
//			map = sc.getMStream();
			String time = getCurrTime();
			String next_time= getNextTime();
			//System.out.println("map size==="+map.size());
			for (int i = 0; i < map.size(); i++) {
				String str = String.valueOf(map.get(String.valueOf(i)));
				//System.out.println(str+"======&&&&&&&&&&&&"+i);
				if (str.length() > 5) {
					if ("table".equalsIgnoreCase(str.substring(1, 6))
							|| "</table>".equalsIgnoreCase(str)) {
						table_tag.append(String.valueOf(i) + "/");
					}
				}
				if(str.length() > 12 && str.indexOf("<h1>JVM</h1>")>-1){
					jvm_tag = i;
					map.put(i, str.substring(str.indexOf("<h1>JVM</h1>")));
				}

			}

			table_tag.append("huilet");
			String[] tmp = table_tag.toString().split("/");
			sServerTag = Integer.parseInt(tmp[tmp.length - 3]);//开始标记	
			eServerTag = Integer.parseInt(tmp[tmp.length - 2]);//结束标记

			tagMapFound(host, port); //建立服务器信息以及主信息元数据
		} catch (Exception e) {
			SysLogger.error("",e);
		}
	}

	public String validServer(Hashtable serverlist) {
		StringBuffer pos = new StringBuffer();
		int listsize = serverlist.size();
		for (int list_i = 0; list_i < listsize; list_i++) {
			hServerFi = new Hashtable();
			jvm_info = new Hashtable();
			port_sum1 = new Hashtable();
			detail_sum1 = new Hashtable();
			port_sum2 = new Hashtable();
			detail_sum2 = new Hashtable();

			data_ht = new Hashtable();
			String tmps = serverlist.get(String.valueOf(list_i)).toString();
			String[] serverinfo = tmps.split(",");
			try {
//				I_TomcatmonitorConn conn = new TomcatmonitorConnManager();
//				TomcatmonitorConn tc = new TomcatmonitorConn();
				if (isValid(serverinfo[0], serverinfo[1], "/manager/status",
						"Tomcat Manager Application", serverinfo[2],
						serverinfo[3]) == false) {
//					tc.setIsconnect(0);     //服务器不可用

					serverlist.remove(String.valueOf(list_i));
				} else {
//					tc.setIsconnect(1);		//服务器可用
					pos.append(String.valueOf(list_i));
					pos.append(",");
				}
//				tc.setMon_time(new GregorianCalendar());
//				tc.setServer_ip(serverinfo[0]);
//				conn.addTomcatmonitorConn(tc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pos.append("huilet");
		if (serverlist.size() == 0) {
			System.out.println("无可监控服务器！");
			return "";
		}
		return pos.toString();
	}

	public void tagMapFound(String ip, String port) {
		/**
		 * 取得服务器信息，将原始哈希表解析成{属性：值}的形式，该信息相对固定
		 */
		ServerInfo serverInfo = new ServerInfo(map, sServerTag, eServerTag);
		hServer = serverInfo.hServerTag(); //取得服务器信息元数据
		hServerTag = serverInfo.hServerInfo(hServer); //去除标签
		hServerFi = serverInfo.hServerInfoFi(hServerTag); //取得对应数据

		/**
		 * 取得监控信息，处理sMain字符串，解析成{类型：属性：值：单位：行号} 如果单位不存在，则用字符串null代替
		 * 行号如果为0，表示该信息属于总体信息，如为其他，则代表该信息在详细信息表中的行数
		 */
		MainInfo mainInfo = new MainInfo(map, jvm_tag);
		sMain = mainInfo.sMainTag();
		//System.out.println(sMain);
		jvm_info = mainInfo.hJVMInfo(sMain);

		port_sum1 = mainInfo.hPORTInfoSum(sMain, 0);
		detail_sum1 = mainInfo.hPORTInfoDetail(sMain, 0);
//		port_sum2 = mainInfo.hPORTInfoSum(sMain, 1);
//		detail_sum2 = mainInfo.hPORTInfoDetail(sMain, 1);

		sendParamToServer(hServerFi, jvm_info, port_sum1, detail_sum1,
				port_sum2, detail_sum2, ip, port);
	}

	public String getCurrTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = format.format(date);
		System.out.println(time);
		return time;
	}
	public String getNextTime() {
		Date date = new Date();
		int curmin = date.getMinutes()+5;
		date.setMinutes(curmin);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nexttime = format.format(date);
		System.out.println(nexttime);
		return nexttime;
	}

	public Hashtable sendParamToServer(Hashtable h_server, Hashtable h_jvm,
			Hashtable h_portsum1, Hashtable h_portdetail1,
			Hashtable h_portsum2, Hashtable h_portdetail2, String ip,
			String port) {
		ServerConnector sc = new ServerConnector();
		StringBuffer sb_server = new StringBuffer();
		StringBuffer sb_jvm = new StringBuffer();
		StringBuffer sb_portsum1 = new StringBuffer();
		StringBuffer sb_portsum2 = new StringBuffer();
		StringBuffer sb_portdetail1 = new StringBuffer();
		StringBuffer sb_portdetail2 = new StringBuffer();

		for (int i = 0; i < h_server.size(); i++) {
			sb_server.append(h_server.get(String.valueOf(i)));
			sb_server.append(",");
		}
		sb_server.append("huilet");
		data_ht.put("server", sb_server.toString());

		for (int i = 0; i < h_jvm.size(); i++) {
			sb_jvm.append(h_jvm.get(String.valueOf(i)));
			sb_jvm.append(",");
		}
		sb_jvm.append("huilet");
		data_ht.put("jvm", sb_jvm.toString());

		for (int i = 0; i < h_portsum1.size(); i++) {
			sb_portsum1.append(h_portsum1.get(String.valueOf(i)));
			sb_portsum1.append(",");
		}
		sb_portsum1.append("huilet");
		data_ht.put("portsum1", sb_portsum1.toString());

		for (int i = 0; i < h_portsum2.size(); i++) {
			sb_portsum2.append(h_portsum2.get(String.valueOf(i)));
			sb_portsum2.append(",");
		}
		sb_portsum1.append("huilet");
		data_ht.put("portsum2", sb_portsum2.toString());

		for (int i = 0; i < h_portdetail1.size(); i++) {
			sb_portdetail1.append(h_portdetail1.get(String.valueOf(i)));
			sb_portdetail1.append(",");
		}
		sb_portdetail1.append("huilet");
		data_ht.put("portdetail1", sb_portdetail1.toString());

		for (int i = 0; i < h_portdetail2.size(); i++) {
			sb_portdetail2.append(h_portdetail2.get(String.valueOf(i)));
			sb_portdetail2.append(",");
		}
		sb_portdetail2.append("huilet");
		data_ht.put("portdetail2", sb_portdetail2.toString());

		data_ht.put("ip", ip);
		data_ht.put("port", port);
		data_ht.put("mon_time", time);
		data_ht.put("nexttime", nexttime);
		return data_ht;
	}

	public boolean isValid(String ip, String port, String target, String realm,
			String user, String password) {
		boolean returnVal = true;
		HTTPResponse rsp = null;
		String str = "http://" + ip + ":" + port;
		System.out.println("验证：" + str);
		try {

//			URL url = new URL(str);
//			System.out.println("验证=============================：" + url);
//			HTTPConnection con = new HTTPConnection(url);
//			con.addBasicAuthorization(realm, user.trim(), password.trim());
			URL url = new URL(str);

			HTTPConnection con = new HTTPConnection(url);
			con.addBasicAuthorization(realm, user.trim(), password.trim());

			rsp = con.Get(target);

			if (rsp == null)
				returnVal = false;
		} catch (Exception e) {
			returnVal = false;
			System.out.println("服务器连接无效:" + str);
		}
		return returnVal;
	}
	
}