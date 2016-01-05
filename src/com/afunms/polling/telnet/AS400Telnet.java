package com.afunms.polling.telnet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class AS400Telnet {
	
	private String ip;
	
	private String username;
	
	private String password;
	
	private String loginPrompt;
	
	private String passwordPrompt;
	
	private String shellPrompt;
	
	private TelnetWrapperForAS400 telnetWrapperForAS400;
	
	
	

	/**
	 * 
	 */
	public AS400Telnet() {
		// TODO Auto-generated constructor stub
	}
	
	

	


	/**
	 * @param ip
	 * @param username
	 * @param password
	 * @param loginPrompt
	 * @param passwordPrompt
	 * @param shellPrompt
	 */
	public AS400Telnet(String ip, String username, String password,
			String loginPrompt, String passwordPrompt, String shellPrompt) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.loginPrompt = loginPrompt;
		this.passwordPrompt = passwordPrompt;
		this.shellPrompt = shellPrompt;
	}






	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	/**
	 * @return the loginPrompt
	 */
	public String getLoginPrompt() {
		return loginPrompt;
	}






	/**
	 * @param loginPrompt the loginPrompt to set
	 */
	public void setLoginPrompt(String loginPrompt) {
		this.loginPrompt = loginPrompt;
	}






	/**
	 * @return the passwordPrompt
	 */
	public String getPasswordPrompt() {
		return passwordPrompt;
	}






	/**
	 * @param passwordPrompt the passwordPrompt to set
	 */
	public void setPasswordPrompt(String passwordPrompt) {
		this.passwordPrompt = passwordPrompt;
	}






	/**
	 * @return the shellPrompt
	 */
	public String getShellPrompt() {
		return shellPrompt;
	}






	/**
	 * @param shellPrompt the shellPrompt to set
	 */
	public void setShellPrompt(String shellPrompt) {
		this.shellPrompt = shellPrompt;
	}






	public boolean init(String ip, String username, String password,
			String loginPrompt, String passwordPrompt, String shellPrompt){
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.loginPrompt = loginPrompt;
		this.passwordPrompt = passwordPrompt;
		this.shellPrompt = shellPrompt;
		return false;
	}
	
	public boolean connect(){
		boolean result = false;
		telnetWrapperForAS400 = new TelnetWrapperForAS400();
		try {
			telnetWrapperForAS400.connect(ip, 23);
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	public boolean login(){
		boolean result = false;
		try {
			telnetWrapperForAS400.login(username , password , loginPrompt  , null , shellPrompt);
			telnetWrapperForAS400.setPrompt("===>");
			telnetWrapperForAS400.send("");
			result = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	public Hashtable execute(Hashtable gatherhash){
		Hashtable hashtable = new Hashtable(); 
		if(gatherhash.containsKey("disk")){
			Vector diskVector = getWrkdsksts();
			hashtable.put("AS400disk", diskVector);
		}
		
		if(gatherhash.containsKey("system")){
			Hashtable hashtable2 = getWrksyssts();
			hashtable.put("SystemStatus", hashtable2);
		}		
		try {
			telnetWrapperForAS400.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtable;
	}
	
	
	public Vector getWrkdsksts(){
		Vector vector = new Vector();
		
		try {
			String result = new String(telnetWrapperForAS400.write("wrkdsksts"));
			
			String value = StringUtilForAS400.filterCode(result);
			
			vector = parseWrkdsksts(value);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vector;
	}
	
	
	public Vector parseWrkdsksts(String result){
		Vector vector = new Vector();
		
		if(result == null || result.length() == 0){
			return vector;
		}
		
		if(result.indexOf("Size")<0 || result.indexOf("Busy")<0 || result.indexOf("Command")<0 || result.indexOf("Size") > result.indexOf("Command")){
			return vector;
		}
		
		String data = result.substring(result.indexOf("Busy")+4, result.indexOf("Bottom"));
		
		
		String[] values = data.trim().split("(" + (char)32 + "|" + (char)9 + ")+");
		
		if(values== null || values.length ==0 ){
			return vector;
		}
		
		Hashtable valuehashtable = new Hashtable();
		
		for(int i = 0 ; i < values.length ; i++){
			String value = values[i].trim();
			
			int j = i%11;
			
			//System.out.println(value+"=====i========" + i + "========j======" + j); 
			if( j == 0 ){
				valuehashtable.put("Unit", value);
				continue;
			}
			if( j == 1 ){
				valuehashtable.put("Type", value);
				continue;
			}
			if( j == 2 ){
				valuehashtable.put("Size(M)", value);
				continue;
			}
			if( j == 3 ){
				valuehashtable.put("%Used", value);
				continue;
			}
			if( j == 4 ){
				valuehashtable.put("I/O Rqs", value);
				continue;
			}
			if( j == 5 ){
				valuehashtable.put("Request Size(K)", value);
				continue;
			}
			if( j == 6 ){
				valuehashtable.put("Read Rqs", value);
				continue;
			}
			if( j == 7 ){
				valuehashtable.put("Write Rqs", value);
				continue;
			}
			if( j == 8 ){
				valuehashtable.put("Read(K)", value);
				continue;
			}
			if( j == 9 ){
				valuehashtable.put("Write(K)", value);
				continue;
			}
			if( j == 10 ){
				valuehashtable.put("%Busy", value);
			}
			vector.add(valuehashtable);
			valuehashtable = new Hashtable();
		}
		
		System.out.println(vector.size()+"===========vector.size(=================");
		return vector;
	}
	
	/**
	 * 此方法只获取其 cpu 和 db 的数据 其他数据被丢弃
	 * @return
	 */
	public Hashtable getWrksyssts(){
		Hashtable hashtable = new Hashtable();
		
		try {
			String result = new String(telnetWrapperForAS400.write("wrksyssts"));
			
			String value = StringUtilForAS400.filterCode(result);
			
			hashtable = parseWrksyssts(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashtable;
	}
	
	public Hashtable parseWrksyssts(String result){
		Hashtable hashtable = new Hashtable();
		if(result == null || result.length() == 0){
			return hashtable;
		}
		
		String cpuValue = "0";
		
		try {
			if(result.indexOf("% CPU used . . . . . . . :")>0 || result.indexOf("System ASP . . . . . . . :")>0){
				int cpuIndex = result.indexOf("% CPU used . . . . . . . :");
				int cpulength = "% CPU used . . . . . . . :".length();
				cpuValue = result.substring(cpuIndex + cpulength, result.indexOf("System ASP . . . . . . . :")).trim();
				if(cpuValue != null && cpuValue.indexOf(".") == 0){
					cpuValue = "0" + cpuValue;
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hashtable.put("cpu", cpuValue);
		
		
		String DBCapabilityValue = "0";
		
		try {
			if(result.indexOf("% DB capability  . . . . :")>0 || result.indexOf("% system ASP used  . . . :")>0){
				int DBCapabilityIndex = result.indexOf("% DB capability  . . . . :");
				int DBCapabilitylength = "% DB capability  . . . . :".length();
				DBCapabilityValue = result.substring(DBCapabilityIndex + DBCapabilitylength, result.indexOf("% system ASP used  . . . :")).trim();
				if(DBCapabilityValue != null && DBCapabilityValue.indexOf(".") == 0){
					DBCapabilityValue = "0" + DBCapabilityValue;
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hashtable.put("DBCapability", DBCapabilityValue);
		
		
		return hashtable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TelnetWrapperForAS400 telnetWrapperForAS400 = new TelnetWrapperForAS400();
		String user = "WUSER";
		String pwd = "DEMO2PWD";
		String ip = "iSeriesD.DFW.IBM.COM";
		try {
			telnetWrapperForAS400.connect(ip, 23);
			telnetWrapperForAS400.login(user , pwd , "2005." , null , "2005.");
			telnetWrapperForAS400.setPrompt("===>");
			telnetWrapperForAS400.send("");
			String result3 = new String(telnetWrapperForAS400.write("wrkactjob"));
			String result4 = telnetWrapperForAS400.send(telnetWrapperForAS400.write((char)12));
			System.out.println(StringUtilForAS400.filterCode(result3));
			System.out.println(StringUtilForAS400.filterCode(result4));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	

}
