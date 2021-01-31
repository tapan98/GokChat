package com.gok.chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

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
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String name, address;
	private int port;
	private JTextField txtMessage;
	private JTextArea history;
	
	private DatagramSocket socket;
	private InetAddress ip_address;
	
	private Thread send;
	
	public Client(String name, String address, int port) {
		setTitle("Gok Chat Client");
		
		this.name = name;
		this.address = address;
		this.port = port;
		
		createWindow();
		
		clientMessage("Name: " + name + "; IP: " + address + "; Port: " + port);
		
		boolean connect = openConnection(address, port);
		if (!connect) {
			
			clientError("Connection failed");
		}
	}
	
	private void createWindow() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // native OS UI look and feel
		} catch (Exception e) {

			e.printStackTrace();
		} 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		setVisible(true);
		txtMessage.requestFocus();
	}
	
	private boolean openConnection(String address, int port) {
		
		try {
			socket = new DatagramSocket(port);
			ip_address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			
			clientError("Unknown host");
			e.printStackTrace();
			
			return false;
		} catch (SocketException e) {
			
			clientError("Exception while opening a port");
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	private String receive() {
		
		byte[] data = new byte[1024];
		
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			
			socket.receive(packet);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		String message = new String(packet.getData());
		
		return message;
	}
	
	private void send(final byte[] data) {
		
		send = new Thread("Send") {
			
			public void run() {
				
				DatagramPacket packet = new DatagramPacket(data, data.length, ip_address, port);
				
				try {
					socket.send(packet);
					
				} catch (IOException e) {
					
					clientError("Exception attempting to sending packet");
					e.printStackTrace();
				}
			}
		};
		
		send.start();
		
	}
	
	private void send(String msg) {
		
		if (msg.isBlank()) return;
		
		clientMessage(msg);
		txtMessage.setText("");
	}
	
	public void clientMessage(String msg) {
		
		history.append("\n" + msg);
	}
	
	public void clientError(String msg) {
		
		history.append("\n[ERROR]: " + msg);
	}

}
