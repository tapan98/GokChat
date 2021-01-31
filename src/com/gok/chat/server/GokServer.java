package com.gok.chat.server;

public class GokServer {
	
	
	public static void main(String[] args) {
		
		int port;
		
		if (args.length != 1) {
			System.out.println("Usage: java ServerMain PORT");
			return;
		}
		
		port = Integer.parseInt(args[0]);

		Thread server = new Thread(new Server(port));
		
		server.start();
	}
}
