package com.afunms.polling.telnet;
//连接Linux的telnet程序
import org.apache.commons.net.telnet.*;
import java.io.*;

public class CiscoTelnet
{
	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;
	private String prompt = ">";
	private String server;
	private String user;
	private String password;
	private int nummber = 2000;// 定义一个读取字符的总数
	
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public CiscoTelnet(String server, String user, String password) 
	{
		this.server = server;
		this.user = user;
		this.password = password;
	}
	public boolean login()
	{
		boolean isLogin = false;
		try {
			// Connect to the specified server
			telnet.connect(server, 23);

			// Get input and output stream references
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());

			// Log the user on
			readUntil("Username:");
			write(user);
			readUntil("Password:");
			write(password);
			// Advance to a prompt
			String temp = readUntil1(prompt,"Username:");
			if(temp.endsWith(">"))
				isLogin = true;
		} catch (Exception e) {
			isLogin = false;
			this.disconnect();
			e.printStackTrace();
		}
		return isLogin;
	}
	private String readUntil1(String pattern1,String pattern2) 
	{
		StringBuffer sb = new StringBuffer();
		try {
			//System.out.println("======读取");
			//char lastChar = pattern.charAt(pattern.length() - 1);

			char ch = (char) in.read();
			int n = 0;
			while (true) {
				// System.out.print(ch);// ---需要注释掉

				if (ch == 0 || ch == 13 || ch == 10 || (ch >= 32)) {
					sb.append(ch);
				}
				if (sb.toString().endsWith(pattern1) ) //规定，如果以pattern1结尾，说明命令执行成功
				{
					// System.out.println(sb.toString());
					return pattern1;
				}
				if (sb.toString().endsWith(pattern2) ) //如果以pattern1结尾，说明命令执行成功
				{
					// System.out.println(sb.toString());
					return pattern2;
				}
					
				ch = (char) in.read();
				//System.out.println(ch+"==========="+n);
				n++;
				if (n > this.nummber) {// 如果读取的字符个数大于2万个字符就认没有正确
					sb.delete(0, sb.length());
					sb.append("user or password error");
					break;
				}
				// System.out.println(n);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
	public void su(String password) 
	{
		try {
			write("su");
			readUntil("Password: ");
			write(password);
			prompt = "#";
			readUntil(prompt + " ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readUntil(String pattern) 
	{
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			boolean found = false;
			char ch = (char) in.read();
			while (true) 
			{
				
				System.out.print(ch);
				sb.append(ch);
				if(sb.toString().endsWith(" --More-- "))
				{
					out.write(32);
					out.flush();
				}
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) 
					{
						return sb.toString();
					}
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void write(String value) 
	{
		try {
			out.println(value);
			out.flush();
			//System.out.println(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String sendCommand(String command) 
	{
		try {
			write(command);
			return readUntil(prompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void disconnect() 
	{
		try {
			telnet.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getCfg(String enPasswd,String bkptype)
	{
		String result = null;
		
		setPrompt("Password:");
		sendCommand("en");
		setPrompt("#");
		sendCommand(enPasswd);
		if(bkptype.equals("run"))
			result = sendCommand("show run");
		else
			result = sendCommand("show startup");
		// 对结果进行格式化
		if (null != result && !result.equals("user or password error")) {
			String[] st = result.split("\r\n");
			StringBuffer buff = new StringBuffer();
			for (int i = 1; i < st.length-1; i++) { 
				
				if(!st[i].contains("--More--"))
				{
					buff.append(st[i]).append("\r\n");
				}
			}
			result = buff.toString();

		}
		disconnect();
		return result;
	}
	public void setupCfg(String enPasswd)
	{
		setPrompt("Password:");
		sendCommand("en");
		disconnect();
	}
	public boolean modifyPasswd(String enPasswd,String newUser,String newPasswd)
	{
		boolean isSuccess = false;
		try{
		String temp = null;
		setPrompt("Password:");
		temp =sendCommand("en");
		if(!isContainInvalidateWords(temp))
		{
			setPrompt("#");
			temp = sendCommand(enPasswd);
			if(!isContainInvalidateWords(temp))
			{
				temp = sendCommand("conf t");
				if(!isContainInvalidateWords(temp))
				{
					temp = sendCommand("username "+newUser+" password 0 "+ newPasswd);
					if(!isContainInvalidateWords(temp))
					{
						isSuccess = true;
					}
				}
			}
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			disconnect();
		}
		return isSuccess;
	}
	private boolean isContainInvalidateWords(String content)
	{
		boolean isContained = false;
		if(content.contains("invalid") || content.contains("Unknown"))
		{
			isContained = true;
		}
		return isContained;
	}
	public String writeCfgFile(String content)
	{
		return "";
	}
	public static void main(String[] args)
	{
		CiscoTelnet telnet = new CiscoTelnet("172.25.25.240", "1","2");
		if(telnet.login())
		{
			telnet.modifyPasswd("2", "1", "2");
		}
		
		//telnet.getCfg(2+"");
	}
}