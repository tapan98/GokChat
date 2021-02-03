/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gok.chat.server;

/**
 *
 * @author newboi
 */
public class GokServer {
    
    public static void main(String[] args) {
		
		int port;
		
                // --------- UNCOMMENT THIS ---------
//		if (args.length != 1) {
//			System.out.println("Usage: java ServerMain PORT");
//			return;
//		}
//		
//		port = Integer.parseInt(args[0]);
                // ----------------------------------
                
                
                
                // --------- REMOVE THIS ---------
                port = 8080;
                // -------------------------------

		Thread server = new Thread(new Server(port));
		
		server.start();
	}
}
