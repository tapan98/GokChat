
package com.gok.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    
    protected int port;
	protected int maxThreadPool = 10;
	
	protected ArrayList<ClientHandler> clientsList;
	
	protected ExecutorService threadpool;
	
	public boolean running = false;
	
	ServerSocket listener;
	
	Server() {}
	
	Server(int port) {
		
		this.port = port;
		
		serverMessage("Starting the server...");
		
		clientsList = new ArrayList<>();
		
		threadpool = Executors.newFixedThreadPool(maxThreadPool);
		
		try {
			
			listener = new ServerSocket(port);
			
			running = true;
		} catch (IOException e) {

			serverError("Exception while attempting to open a port: " + port);
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {

		BufferedReader keyboardIn = null;
		
		if (running) {
			
			serverMessage("Starting Client Listener...");
			
			Thread clientlistener = new Thread(new ClientListener(listener, clientsList, threadpool, port));
			
			clientlistener.start();
			
			keyboardIn = new BufferedReader(new InputStreamReader(System.in));

			printHelp();
		}
		
		
		try {


            while(running) { // server command input loop
            	
            	String command = "";
            	
            	if (keyboardIn != null)
            		command = keyboardIn.readLine();

                if (command.equalsIgnoreCase("Hello")) {
                    serverMessage("Hello, have a good day :)");

                }else if (command.equals("/help")) {
                    printHelp();

                }else if (command.equals("/shutdown") || command.equals("/q")) {
                    running = false;

                }else if (command.equals("/port")) {
                    serverMessage(""+port);

                } else {
                    serverMessage("Don't understand that command");
                }

            }

            serverMessage("Server is shutting down...");

        } catch (IOException e) {
            serverError("Exception while listening to commands");
            e.printStackTrace();
        } finally {

            try {


                serverMessage("Shutting down pool...");

                for (ClientHandler handle : clientsList) {

                    handle.running = false;
                }
                
                threadpool.shutdown();

                threadpool.awaitTermination(5, TimeUnit.SECONDS);

                if(threadpool.isTerminated()) serverMessage("All tasks are terminated");
                else {
                    
                    serverMessage("Some tasks are still running, forcing tasks to shutdown...");
                    threadpool.shutdownNow();
                    Thread.sleep(100);
                } 

                serverMessage("Shutting down listener...");

                listener.close();
                
                if(listener.isClosed()) serverMessage("listener is closed");
                else serverMessage("listener is still running");


            } catch (IOException e) {
                serverError("Exception while attempting to close the server");
                e.printStackTrace();
            } catch (InterruptedException e) {
                serverError("Exception in Thread.sleep");
                e.printStackTrace();
            }
        }
		
		
	}
	
	private void printHelp() {

        System.out.println("--Help is here--");
        System.out.println("/help: displays this help");
        System.out.println("/shutdown or /q: quits the server");
        System.out.println("/port: displays server port");
    }
	
	protected synchronized void serverError(String msg) {
		
		System.err.println("[ERROR]" + msg);
	}
	
	protected synchronized void serverMessage(String msg) {

        System.out.println("[INFO]: " + msg);
    }
}
