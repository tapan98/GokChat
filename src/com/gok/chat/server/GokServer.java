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
		
		int port = 8080;

		if (args.length != 1) {
                    
			System.out.println("Usage: java GokServer [PORT]");
                        System.out.println("Using default port: 8080");

		} else {
                    
                    port = Integer.parseInt(args[0]);

                }
		
                
		Thread server = new Thread(new Server(port));
		
		server.start();
	}
}
