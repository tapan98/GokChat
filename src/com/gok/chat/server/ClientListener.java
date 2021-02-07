
package com.gok.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class ClientListener extends Server {

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

    private synchronized ClientHandler createAssignID(Socket client) {
        
        ClientHandler handle;
        ArrayList<ClientHandler> list = clientsList;
        
        for (int i = 0; i < list.size(); i++) {
        
            if (list.get(i) == null) {
            
                handle = new ClientHandler(client, clientsList, threadpool, i);
                list.set(i, handle);
                return handle;
            }
        }
        
        handle = new ClientHandler(client, clientsList, threadpool, list.size());
        list.add(handle);
        return handle;
    }
    
    @Override
    public void run() {
        try {

            while (running) {

                Socket client = listener.accept(); // exception occurs
                // in an attempt to close current thread

                //serverMessage(client.getInetAddress() + " connected.");
                
                ClientHandler clientThread = createAssignID(client);
                        //ClientHandler(client, clientsList, threadpool);

                // addToClientsList(clientThread);
                threadpool.execute(clientThread);
            }

        } catch (SocketException e) {
            // Socket.accept() method interruption
        } catch (IOException e) {

            serverError("Exception in ClientListener");
            e.printStackTrace();

        }
    }
    
}
