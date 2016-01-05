package com.afunms.polling.telnet;

import java.io.IOException;

public class TelnetWrapperForAS400 extends TelnetWrapper
{
	/**
	 * Login into remote host. This is a convenience method and only works if
	 * the prompts are "login:" and "Password:".
	 * 
	 * @param user
	 *            the user name
	 * @param pwd
	 *            the password
	 */
	public void login(String user, String pwd, String loginPrompt,
			String passwordPrompt , String shellPrompt) throws IOException
	{
		this.username = user;
		this.password = pwd;
		this.loginPrompt = loginPrompt;
		this.passwordPrompt = passwordPrompt;
		
		
		
		waitfor(loginPrompt); // throw output away
		
//		setSystemEnterLine("\t");
		
		send(user + "\t" + pwd);
		
		this.setPrompt(shellPrompt);

	}
	
	
	
	public String write(String cmdStr) throws IOException
	{
		byte arr[];
		System.out.println(cmdStr);
		arr = (cmdStr + "\n").getBytes("iso-8859-1");
		
		handler.transpose(arr);
		if (getPrompt() != null)
		{
			String result = waitfor(getPrompt());
			if(result != null)
			{
//				result = changeCharset(result);
//				result = result.replaceAll("\r\n", "\n");
//				String[] lines = result.split("\n");
//				StringBuilder sb = new StringBuilder();
//				for(int i = 1 ; i < lines.length - 1 ; i++)
//				{
//					sb.append(lines[i]);
//					if(i == lines.length - 2)
//					{
//						break;
//					}
//					sb.append("\n");
//				}
//				String res = sb.toString();
//				log("cmd = " + cmd + " , result = "  + res);
				return result;
			}
		}
		
		log("cmd = " + cmdStr + " , result = null");
		return null;
	}
	
	
	public String write(char cmdStr) throws IOException
	{
		byte arr[];
		System.out.println(cmdStr);
//		arr = (cmdStr + "\n").getBytes("iso-8859-1");
		arr = String.valueOf(cmdStr).getBytes("iso-8859-1");
		handler.transpose(arr);
		if (getPrompt() != null)
		{
			String result = waitfor(getPrompt());
			if(result != null)
			{
//				result = changeCharset(result);
//				result = result.replaceAll("\r\n", "\n");
//				String[] lines = result.split("\n");
//				StringBuilder sb = new StringBuilder();
//				for(int i = 1 ; i < lines.length - 1 ; i++)
//				{
//					sb.append(lines[i]);
//					if(i == lines.length - 2)
//					{
//						break;
//					}
//					sb.append("\n");
//				}
//				String res = sb.toString();
//				log("cmd = " + cmd + " , result = "  + res);
				return result;
			}
		}
		
		log("cmd = " + cmdStr + " , result = null");
		return null;
	}
	
	
	
}
