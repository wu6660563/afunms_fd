package com.huilin.tinysoap.server;

import java.net.ServerSocket;
import java.net.Socket;


public class TinyServer {
	int port = 15699;

	public void setPort(int port) {
		this.port = port;
	}

	void server() throws Exception {
		ServerSocket listenSocket = new ServerSocket(port);
		while (true) {
			Socket sock = listenSocket.accept();
			
			PrintRequest send = new PrintRequest(sock);
			send.start();
		}
	}

	public static void main(String[] args) throws Exception {
		TinyServer p = new TinyServer();
		//System.out.println(args[0]);
		p.server();
	}
}
