package com.gok.chat;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String name, address;
	private int port;
	private JTextField txtMessage;
	private JTextArea history;
	
	private Socket socket;
	private BufferedReader receiver;
	private PrintWriter sender;
	boolean connected = false;

	private DefaultCaret caret;
	
	private Thread serverHandler = null;
	
	public Client(String name, String address, int port) {
		setTitle("Gok Chat Client");
		
		this.name = name;
		this.address = address;
		this.port = port;
		
		createWindow();
		
		clientMessage("Username: " + name + "; IP: " + address + "; Port: " + port);
		
		
		connected = openConnection(address, port); // opening connection
		
		
		if (connected) {
			
			
			/**
			 * appends server responses to the history text field
			 */
			serverHandler = new Thread("Receiver") { // handles server responses
                
                void message(String msg) {

                    serverResponse(msg);
                }

                public void run() {

                    String serverResponse;

                    try {
                        
                        while(!socket.isInputShutdown()) {
                            
                            while (receiver.ready()) {

                                serverResponse = receiver.readLine();
                                
                                message(serverResponse);
                            }
                            Thread.sleep(50); // reduces CPU usage
                        }

                        clientMessage("Disconnected from the server.");
                    } catch (IOException e) {
                        
                        System.err.println("[ERROR]: Exception in PlayClient serverHandler thread.");
                        e.printStackTrace();
                    } catch (InterruptedException e) {

                        // Thread.sleep() interruption
                    }
                }
            };
            serverHandler.start();
			
            clientMessage("Connected to the server.");
            
		}
		else {
			
			clientError("Connection failed");
		}
	}
	
	/**
	 * parses client commands
	 * @param command
	 */
	private void parseClientCommands(String command) {
		
		if (command.equals("/q")) {

			if (serverHandler != null)
				serverHandler.interrupt();
			
			closeSocket();
				
			System.exit(0);
		}
		else { // must be a reqest from the server

			sender.println(command);
			txtMessage.setText("");
		}
	}
	
	/**
	 * tries to connect to the server, establishes sender and receiver
	 * @param address
	 * @param port
	 * @return
	 */
	private boolean openConnection(String address, int port) {
		
		try {

			clientMessage("Connecting to the server[" + address + ":" + port + "]...");
			
			socket = new Socket(address, port);
			
			receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			sender = new PrintWriter(socket.getOutputStream(), true);

			sender.println("[JYN]" + name);
			
		} catch (IOException e) {
			
			clientError("Cannot connect to the server using IP: " + address + " Port: " + port);
			System.err.println("Cannot connect to the server using IP: " + address + " Port: " + port);

			System.err.println(e);
			
			return false;
		}
		
		return true;
	}
	
	
	
	private void send(String msg) {
		
		if (msg.isEmpty()) return;
		
		else if (msg.startsWith("/")) { 
			
			parseClientCommands(msg); 
			return; 
			
		}
		
		if (sender != null) {
			
			sender.println("[MSG]"+ "[" + name + "]" + msg);
		}
		
		txtMessage.setText("");
	}
	
	public void serverResponse(String msg) {
		
		if (msg.startsWith("[MSG]")) {
			
			String user = msg.substring(msg.indexOf(']')+2);
			String message = user.substring(user.indexOf(']')+1);
			user = user.substring(0, user.indexOf(']'));
			

			history.append("\n" + user + ": " + message);
			
		} else if (msg.equals("[PNG]")) { // server tries to ping
			
			if (sender != null) sender.println("[PRE]"); // Ping reply
		
		} else if (msg.startsWith("[JYN]")) { // user joined

			String username = msg.substring(msg.indexOf(']')+1);

			history.append("\n" + username + " has joined the server");
		}
		else {
			history.append("\n[SERVER RESPONSE]: " + msg);
		}
	}
	
	public void clientMessage(String msg) {
		
		history.append("\n[CLIENT]: " + msg);
	}
	
	public void clientError(String msg) {
		
		history.append("\n[ERROR]: " + msg);
	}
	
	private void closeSocket() {
		
		
		if (sender != null) {
			
			sender.println("/quit");
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

private void createWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // native OS UI look and feel
		} catch (Exception e) {

			e.printStackTrace();
		} 
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{30, 730, 30, 10};
		gbl_contentPane.rowHeights = new int[]{40, 530, 30};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		history = new JTextArea();
		history.setEditable(false);
		caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scroll = new JScrollPane(history);
		
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.gridwidth = 2; 
		scrollConstraints.insets = new Insets(5, 0, 0, 0);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		contentPane.add(scroll, scrollConstraints);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				send(txtMessage.getText());
			}
		});
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					send(txtMessage.getText());
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		
		addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                
            	if (serverHandler != null)      
            		serverHandler.interrupt();
            	
            	closeSocket();
            	
            	System.exit(0);
            }
        });

		setVisible(true);
		txtMessage.requestFocus();
	}

}
