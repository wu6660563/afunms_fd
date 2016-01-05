package com.afunms.application.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class InstanceClient {
	Socket socket;
	BufferedReader in;
	PrintWriter out;

	public InstanceClient(String serverIp) 
	{
		boolean serverStatus = false;
		while (!serverStatus) 
		{
			try {
				socket = new Socket(serverIp, 10000);
				in = getReader(socket);
				out = getWriter(socket);
				serverStatus = true;
			} catch (Exception e) {
				serverStatus = false;
				System.out.println("δ�����ӷ�����");
				//e.printStackTrace();
				try{
					Thread.sleep(2000);
				}catch(Exception ee){
					//ee.printStackTrace();
				}
				continue;
			}
			
			try {
				System.out.println("���ӳɹ���");
				String msg = in.readLine();
				System.out.println("start:" + msg);
				
				while (!msg.equals("bye")) 
				{
					System.out.println("client:�յ�����:" + msg);
					if (msg.contains("shutdown")) {
						out.print("�ͻ��˼����رա�����");
						out.flush();
						Process p = Runtime.getRuntime().exec(msg);
						// line.close();
						out.close();
						in.close();
						socket.close();
						System.out.println("client:�ر���socket");
						System.out.println("client:��ʼִ�йرպ����");
						break;
						// Process p = Runtime.getRuntime().exec(s);
					}
					msg = in.readLine();
				}

				// line.close();
				out.close();
				in.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				serverStatus = false;
				continue;
			}
		}
	}

	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

	public static void main(String[] args) {
		new InstanceClient(args[0]);
	}
}