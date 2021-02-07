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
                sender.println("Enter /help and get some help :>");

                sayToAll(request);

                Thread timeout = new Thread("Timeout") {

                    @Override
                    public void run() {

                        while (running && sender != null) {

                            try {
                                Thread.sleep(3000);
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

                        if (request.startsWith("/say ")) {

                            easterEgg = 0;
                            sender.println("you said: " + request.substring(request.indexOf(' ') + 1));

                        } else if (request.equals("/date")) {

                            easterEgg = 0;
                            sender.println(new Date().toString());

                        } else if (request.equalsIgnoreCase("/quit")) {

                            running = false;

                        } else if (request.equals("/help")) {

                            easterEgg = 0;
                            sendHelp();

                        } else {

                            easterEgg++;

                            if (easterEgg >= 10) {

                                sender.println("Just Stop It.");
                            } else if (easterEgg > 4) {

                                sender.println("Stop it. Get some /help.");
                            } else {

                                sender.println("Mmm? Get some /help :>");
                            }
                        }

                    } else if (request.equals("[PRE]")) {

                        connection_timeout = 0;
                    } else if (request.startsWith("[MSG]")) {

                        sayToAll(request);

                    }
                }
            }

            serverMessage("Client " + username + "@" + client.getInetAddress() + " has disconnected.");

            sayToAll("[DISC]" + username);
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
                + "/help: to get this help\n"
                + "/date: get a date\n"
                + "/quit: quit the server"
        );
    }

    private void sayToAll(String msg) {

        for (ClientHandler client : clientsList) {

            if (client != null) {
                client.sender.println(msg);
            }
        }
    }

}
