package com.gok.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public class ClientHandler extends Server {

    private Socket client;
    private BufferedReader receiver;
    protected PrintWriter sender;
    private ArrayList<ClientHandler> clientsList;
    private int easterEgg = 0;
    private int connection_timeout = 0;
    public String username;
    public final int UID;

    public ClientHandler(Socket client, ArrayList<ClientHandler> clientsList, ExecutorService threadpool, int UID) {

        this.client = client;
        this.clientsList = clientsList;
        this.threadpool = threadpool;
        this.UID = UID;

        try {
            receiver = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            sender = new PrintWriter(client.getOutputStream(), true);
            running = true;
        } catch (IOException ex) {

            serverError("Exception trying to establish sender stream");
            setNullThis();
            running = false;
        }
    }

    public void setNullThis() {

        ArrayList<ClientHandler> list = clientsList;
        int index = list.indexOf(this);
        list.set(index, null);
    }

    @Override
    public void run() {

        if (!running) {
            return;
        }

        try {

            { // if there's no response
                int count = 0;
                while ( receiver != null && !receiver.ready() && count < 4) {

                    Thread.sleep(100);
                    count++;
                }
                if (count >= 30) { // client has left
                    running = false;
                }
            }

            String request = "";

            if (running) {
                request = receiver.readLine();
            }

            if (sender != null && running && request.startsWith("[JYN]")) { // join request

                sender.println("[ACK]" + UID);

                username = request.substring(request.indexOf(']') + 1);
                serverMessage("Client " + username + " from " + client.getInetAddress() + " connected.");

                sender.println("Welcome to the GokServer");
                sender.println("Enter /HELP and get some help :>");

                sayToAll(clientsList, username + " (" + UID + ") has joined the server.");

                Thread timeout = new Thread("Timeout") {

                    @Override
                    public void run() {

                        while (running && sender != null) {

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }

                            if (connection_timeout > 3) { // connection assumed to be lost
                                running = false;
                            } else { // ping client
                                connection_timeout++;

                                if ( sender != null ) sender.println("[PNG]");
                            }
                        }
                        

                    }
                };
                threadpool.execute(timeout);

            } else {

                serverError("connection request was: " + request);
                running = false;
                throw new IOException();
            }

            while (running) {

                while (!receiver.ready() && running) {

                    Thread.sleep(50);

                }

                if (receiver.ready()) {
                    request = receiver.readLine();
                }

                if (!request.isEmpty()) {

//                    try {
//                        serverMessage ("Client " + client.getInetAddress() + " requested: " + request);
//
//                    } catch (StringIndexOutOfBoundsException e) {
//
//                        serverError("Out of bounds while trying to print client request string");
//                    }
                    if (request.substring(0, 1).equals("/")) {
                        
                        String req = request.toUpperCase();

                        if ( req.startsWith("/SAY ") ) {

                            easterEgg = 0;
                            sender.println("you said: " + request.substring(request.indexOf(' ') + 1));

                        } else if (req.startsWith("/DATE")) {

                            easterEgg = 0;
                            sender.println(new Date().toString());

                        } else if ( req.startsWith("/QUIT") ) {

                            running = false;

                        } else if ( req.equals("/HELP") ) {

                            easterEgg = 0;
                            sendHelp();

                        } else if ( req.startsWith("/PM") ) {
                            
                            sendPM(request);
                            easterEgg = 0;
                       
                        } else if ( req.equals("/USERS") ) {
                            
                            sendUsers();
                            easterEgg = 0;
                        }
                       
                        else {

                            easterEgg++;

                            if (easterEgg >= 10) {

                                sender.println("Just Stop It.");
                            } else if (easterEgg > 4) {

                                sender.println("Stop it. Get some /HELP.");
                            } else {

                                sender.println("Mmm? Get some /HELP :>");
                            }
                        }

                    } else if (request.equals("[PRE]")) {

                        connection_timeout = 0;
                    
                    } else if (request.equals("[USRS]")) {
                        
                        sendUsers2();
                        
                    }
                    
                    else { // normal message

                        sayToAll(clientsList, username + " (" + UID + "): " + request);

                    }
                }
            }

            serverMessage("Client " + username + "@" + client.getInetAddress() + " has disconnected.");

            sayToAll(clientsList, username + " (" + UID + ") disconnected.");
        } catch (SocketException e) {

            serverMessage("Exception: Client has probably disconnected");
            e.printStackTrace();
        } catch (IOException e) {

            serverError("Exception in ClientHandler");
            e.printStackTrace();

        } catch (InterruptedException e) {

            System.err.println("[WARNING]: Interruption in ClientHandler");

        } finally {

            try {
                client.close();
                //clientsList.remove(clientsList.size() - 1);
                receiver = null;
                sender = null;

                setNullThis();
                

            } catch (IOException e) {
                serverError("Exception in Client Handler");
                e.printStackTrace();
            }
        }
        
    }

    private void sendHelp() {
        sender.println(
                "Usage:\n"
                + "/HELP: to get this help\n"
                + "/DATE: get a date\n"
                + "/PM <id> <MESSAGE>: send a Personal Message\n"
                + "/USERS: get a list of online users\n"
                + "/QUIT: quit the server"
        );
    }
    
    
    /**
     * Counts the number of given char value in a String
     * @param text
     *        the base String
     * @param c
     *        the char to look for
     * @param upto
     *        is the minimum number of characters to look for
     * @return
     *        A Boolean value true if the number of parameter 'c' occurrences is more than or equal to the parameter 'upto'
     *        otherwise false
     */
    private static boolean countChars(String text, char c, int upto) {

        int length = text.length();
        int count = 0;

        for (int i = 0; i < length; i++) {

            if (text.charAt(i) == c) count++;

            if (count == upto) return true;
        }

        return false;
    }

    /**
     * Sends a PM (Personal Message)
     * @param msg the String message to send
     * @return returns true if the message was sent successfully otherwise false
     */
    public boolean sendPM(String msg) {      
        
        String helpText = "/PM <ID> <MESSAGE>";
        int tID = -1;
        
        if ( !countChars(msg, ' ', 2) ) { // if not enough spaces

            sender.println(helpText); 
            return false;
        }
            
        String tokens[] = msg.split(" ", 3); // --/PM <ID> <MESSAGE>--
        
        try { // parse tID
            tID = Integer.parseInt(tokens[1]);
        }
        catch ( NumberFormatException e ) {
            
            sender.println(helpText);
            return false;
        }
        
        
        if (clientsList.size() <= tID || tID < 0 || clientsList.get(tID) == null || sender == null) {
            
            if (sender != null) {
                
                sender.println("User is not available");             
            }
            
            return false;
        }

        // send token[2] i.e. message to uID

        if ( clientsList.get(tID).sender != null && sender != null ) {
            
            sender.println("PM to " + clientsList.get(tID).getUserName() + " (" + clientsList.get(tID).getUID() + "): " + tokens[2]);
            clientsList.get(tID).sender.println("PM from " + username + " (" + UID + "): " + tokens[2]);
        }

        
        return true;
    }
    
    /**
     * Sends a list of users to the client
     */
    private void sendUsers(){
        String user;
        int uID;
        
        sender.println("\n----Users----");
        
        for (ClientHandler handle: clientsList) {
            
            if (handle != null) {
                user = handle.getUserName();
                uID = handle.getUID();
                sender.println(user + " (" + uID + ")");
            }
        }
        sender.println("-------------\n");
    }
    
    /**
     * Sends semicolon separated list of users
     */
    private void sendUsers2() {
    
        String user;
        int uID;
        String packet = "[USRS]";
        
        for ( ClientHandler handle : clientsList ) {
            
            if (handle != null) {
                
                user = handle.getUserName();
                uID = handle.getUID();
                packet = packet + user + " (" + uID + ");";
                
            }
        }
        if (sender != null) sender.println(packet);
    }
    
    
    /**
     * 
     * @return username
     */
    public String getUserName() {
    
        return username;
    }
    
    /**
     * 
     * @return user ID
     */
    public int getUID() {
        return UID;
    }
    
    
    
}
