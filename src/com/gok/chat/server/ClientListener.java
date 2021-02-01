package com.gok.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class ClientListener extends Server implements Runnable {

	
	ClientListener(ServerSocket listener, ArrayList<ClientHandler> clientsList, ExecutorService pool, int port) {

        
        this.listener = listener;
        this.clientsList = clientsList;

        this.threadpool = pool;
        this.port = port;

        running = true;

    }
	
	public void stopListen() {
		running = false;
	}
	
	@Override
	public void run() {
		try {

            while(running) {

                
                Socket client = listener.accept(); // exception occurs
                                                   // in an attempt to close current thread

                //serverMessage(client.getInetAddress() + " connected.");

                ClientHandler clientThread = new ClientHandler(client, clientsList, threadpool, clientsList.size());

                addToClientsList(clientThread);
                threadpool.execute(clientThread);
            }

        } catch (SocketException e) {
            // Socket.accept() method interruption
        } catch (IOException e) {

            serverError("Exception in ClientListener");
            e.printStackTrace();

        }
	}

    protected synchronized void addToClientsList(ClientHandler handle) {

        clientsList.add(handle);
    }
	
}
