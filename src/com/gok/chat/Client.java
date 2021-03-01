
package com.gok.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    
    final private String username;
    final private String address;
    final private int port;
    private int UID = -1;
    
    ClientWindow window;
    Thread windowThread;
    

    private static Socket socket;
    private static BufferedReader receiver;
    public static PrintWriter sender;
    boolean connected = false;
    private static Thread serverHandler = null;
    
    /**
     * Back-end Client
     * 
     * @param name
     * @param address
     * @param port 
     */
    Client(String name, String address, int port) {
        
        this.username = name;
        this.address = address;
        this.port = port;

    }
    
    /**
     * - Create and runs a new ClientWindow thread
     * - Opens the socket connection
     */
    public void runClient() {
    
        window = new ClientWindow(sender, username);
        windowThread = new Thread(window); // creates window
        windowThread.start(); // displays window
        
        // try { Thread.sleep(3000); } catch (InterruptedException e) {}

        window.clientMessage("Entering /CHELP displays command help (not for the server)");
        window.clientMessage("Press Ctrl + End on the chat area to resume auto-scroll");
        window.clientMessage("Username: " + username + "; IP: " + address + "; Port: " + port);
        
        connected = openConnection(address, port);
        
        if (connected) {

            window.title(username + "@" + address + ":" + port + " - Gok Chat Client");

            serverHandler = initServerHandler(); // new handler thread
            serverHandler.start();
            
            window.initGetUsers();

        } else {

            window.clientError("Connection failed");
        }
    }
    
    /**
     * Creates server handler object
     * @return serverHandler Thread
     */
    private Thread initServerHandler() {

        /**
         * appends server responses to the history text field can also be used
         * to send messages
         */
        Thread handle = new Thread("Receiver") { // handles server responses

            public void message(String msg) {

                serverResponse(msg);
            }

            public void send(String msg) {

                if (sender != null && !socket.isClosed()) {

                    sender.println(msg);
                }

            }

            public void run() {

                String serverResponse;

                try {

                    while (!socket.isInputShutdown() && !this.isInterrupted()) {

                        while (receiver.ready()) {

                            serverResponse = receiver.readLine();

                            message(serverResponse);
                        }
                        Thread.sleep(50); // reduces CPU usage
                    }

                    window.clientMessage("Disconnected from the server.");
                } catch (IOException e) {

                    System.err.println("[ERROR]: Exception in Client serverHandler thread.");
                    e.printStackTrace();
                } catch (InterruptedException e) {

                    // Thread.sleep() interruption
                }
            }
        };

        return handle;
    }

    /**
     * 
     * @return the username of the client
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Identifies server response type and appends appropriate message to the history field
     * @param msg the String type reference
     */
    private void serverResponse(String msg) {

        if (msg.equals("[PNG]")) { // server ping request

            if (sender != null) sender.println("[PRE]"); // Ping reply

        } else if (msg.startsWith("[USRS]")) {
            
            window.setUsersList(msg.substring(msg.indexOf("]")+1));
            
        }
        else {
            window.println(msg);
        }
    }

    /**
     * Tries to connect to the server, establishes Input(receiver) and Output(sender) Stream
     *
     * @param address
     * @param port
     * @return
     */
    private boolean openConnection(String address, int port) {

        try {

            window.clientMessage("Connecting to the server " + address + ":" + port + "...");

            socket = new Socket(address, port);

            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sender = new PrintWriter(socket.getOutputStream(), true);

            // remove this
            //try {Thread.sleep(7000); } catch (InterruptedException e) {}
            
            
            sender.println("[JYN]" + username);

            { // if there's no response
                int count = 0;
                while (!receiver.ready() && count < 4) {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    count++;
                }
                if (count >= 30) {

                    throw new IOException();
                }
            }

            String ack = receiver.readLine();

            if (!ack.startsWith("[ACK]")) {

                window.clientError("Reply was: " + ack);
                throw new IOException();
            } else {

                UID = Integer.parseInt(ack.substring(ack.indexOf(']')+1));
                window.setUID(UID);
                //history.append("\nConnected to the server.\n" + receiver.readLine());
            }

        } catch (IOException e) {

            window.clientError("Cannot connect to the server using IP: " + address + " Port: " + port);
            System.err.println("Cannot connect to the server using IP: " + address + " Port: " + port);

            System.err.println(e);

            return false;
        }

        return true;
    }
   
    /**
     * This method sends quit message to the server (indicating to close the socket connection),
     * tries to interrupt serverHandler and
     * finally tries to close the socket connection
     */
    public static void closeConnection() {

        if (sender != null) {

            sender.println("/quit");
        }
        
        if (serverHandler != null && serverHandler.isAlive()) {
            serverHandler.interrupt();
            
            try { Thread.sleep(5); } catch (InterruptedException ex) { 
                System.err.println("Sleep was interrupted while interrupting serverHandler thread."); 
            }
        }
        
        
        if ( serverHandler != null && serverHandler.isAlive()) {
            System.err.println("Receiver thread is still alive!");
        }

        if (socket != null && !socket.isClosed()) {

            try {
                socket.close();
            } catch (IOException e) {

                System.err.println("Error while attempting to close the socket");

                e.printStackTrace();
            }

        }
    }
    
}
