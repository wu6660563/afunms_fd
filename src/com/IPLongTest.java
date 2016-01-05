/*
 * @(#)IPLongTest.java     v1.01, Dec 18, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class IPLongTest {
	
	static List test1(List list){
		list = null;
		return list;
	}
		  
	static void test2(List list){
		list.add(null);
	}
			  
	public static void main(String[] args) {
	}

	/**
	 * main:
	 * <p>
	 * 
	 * @param args
	 * 
	 * @since v1.01
	 */
	// public static void main(String[] args) {
	// // String ip = "64.2.48.76";
	// // System.out.println(NetworkUtil.ip2long(ip));
	//    	
	// String url = "POST /huawei-remote?cmd=reverse_channel\\r\\nIp:
	// 192.168.1.101\\r\\nPort: 8080\\r\\nContent-Length: 0\\r\\n";
	//
	// }
	public static void main1(String[] args) {

		// Customer customer = new Customer("test", 1);
		//
		// // 序列化
		// ObjectOutputStream out = null;
		// try {
		// out = new ObjectOutputStream(new FileOutputStream(
		// "D:\\objectFile.obj"));
		// out.writeObject(customer);
		// out.flush();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (out != null)
		// out.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// // 反序列化
		// ObjectInputStream in = null;
		// try {
		// in = new ObjectInputStream(
		// new FileInputStream("D:\\objectFile.obj"));
		// Object vo = in.readObject();
		// Customer customer2 =(Customer) vo;
		// System.out.println(customer2.toString());
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (in != null)
		// in.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		//
		String str = "bcm0: flags=4000c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX,RESERVED>\n"+
"     NetRAIN Virtual Interface: nr1 \n"+
"     NetRAIN Attached Interfaces: ( bcm0 ee0 ) Active Interface: ( bcm0 )\n"+

"bcm1: flags=c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX>\n"+
"     inet 192.168.106.30 netmask ffffff00 broadcast 192.168.106.255 ipmtu 1500 \n"+

"bcm2: flags=c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX>\n"+
"     inet 192.168.206.30 netmask ffffff00 broadcast 192.168.206.255 ipmtu 1500 \n"+

"ee0: flags=4000c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX,RESERVED>\n"+
"     NetRAIN Virtual Interface: nr1 \n"+
"     NetRAIN Attached Interfaces: ( bcm0 ee0 ) Active Interface: ( bcm0 )\n"+

"lo0: flags=100c89<UP,LOOPBACK,NOARP,MULTICAST,SIMPLEX,NOCHECKSUM>\n"+
"     inet 127.0.0.1 netmask ff000000 ipmtu 4096 \n"+

"nr1: flags=c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX>\n"+
"     NetRAIN Attached Interfaces: ( bcm0 ee0 ) Active Interface: ( bcm0 )\n"+
"     inet 192.168.108.23 netmask ffffff00 broadcast 192.168.108.255 ipmtu 1500 \n"+

"sl0: flags=10<POINTOPOINT>\n"+

"tun0: flags=80<NOARP>\n"+

"tun1: flags=80<NOARP>";

		String[] ipconfigLineArr = str.trim().split("\n");
		Hashtable hash = new Hashtable();
		Hashtable hashtable = null;
		
		String name = "";
		String status = "";
		String ipaddress = "";
		
		for (int i = 0; i < ipconfigLineArr.length; i++) {
			String lineArr = ipconfigLineArr[i].trim();
			if (lineArr.indexOf("flags") >= 0) {
				//ee0: flags=4000c63<UP,BROADCAST,NOTRAILERS,RUNNING,MULTICAST,SIMPLEX,RESERVED>
				hashtable = new Hashtable();
				String[] tmpData = lineArr.split(":");
				name = tmpData[0].trim();
				
				if (hashtable != null) {
					hash.put(name, hashtable);
				}
				
				hashtable.put("name", name);
				if(tmpData[1].indexOf("<")>0&&tmpData[1].indexOf(",")>0){
					status = tmpData[1].substring(tmpData[1].indexOf("<") +1 , tmpData[1].indexOf(",")).trim();
					hashtable.put("status", status);
				}
			}
			if(lineArr.indexOf("inet") >= 0){
				//inet 127.0.0.1 netmask ff000000 ipmtu 4096 
				ipaddress = lineArr.substring(lineArr.indexOf("inet")+4, lineArr.indexOf("netmask")).trim();
				hashtable.put("ipaddress", ipaddress);
			}
		}
//		for (Object object : ipconfigVector) {
//			Hashtable hashtable2 = (Hashtable) object;
//			System.out.println(hashtable2.get("name") + "==" + hashtable2.get("status") + "==" + hashtable2.get("ipaddress"));
//		}
		
		Iterator iterator = hash.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			Hashtable ipconfigHash = (Hashtable) hash.get(key);
//			System.out.println("key====="+key+">>>name:"+ipconfigHash.get("name")+"****"+ipconfigHash.get("status")+"****"+ipconfigHash.get("ipaddress"));
		}
		
	}

}

class Customer implements Serializable {
	/**
	 * serialVersionUID:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static final long serialVersionUID = 5914581588890422105L;
	private String name;
	private int age;

	public Customer(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String toString() {
		return "name=" + name + ", age=" + age;
	}
}
