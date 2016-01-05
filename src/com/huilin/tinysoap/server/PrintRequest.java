package com.huilin.tinysoap.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class PrintRequest extends Thread{
	Socket socket = null;

	public PrintRequest(Socket sock) {
		this.socket = sock;

	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			String e;
			while((e = in.readLine()) != null) {
				System.out.println(e);
			}
			
			in.close();
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
