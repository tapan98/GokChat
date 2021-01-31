package com.gok.chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {


	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JLabel lblIPDesc;
	private JLabel lblPortDesc;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Login() {
		setTitle("Login");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // native OS UI look and feel
		} catch (Exception e) {

			e.printStackTrace();
		} 
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,310);
		
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(135, 39, 145, 20);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter nickname:");
		lblNewLabel.setBounds(10, 42, 115, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(10, 93, 115, 14);
		contentPane.add(lblIpAddress);
		
		txtAddress = new JTextField();
		txtAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtAddress.setColumns(10);
		txtAddress.setBounds(135, 90, 145, 20);
		contentPane.add(txtAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(10, 177, 115, 14);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setHorizontalAlignment(SwingConstants.CENTER);
		txtPort.setColumns(10);
		txtPort.setBounds(135, 174, 145, 20);
		contentPane.add(txtPort);
		
		lblIPDesc = new JLabel("(e.g.192.168.1.1)");
		lblIPDesc.setBounds(165, 121, 115, 14);
		contentPane.add(lblIPDesc);
		
		lblPortDesc = new JLabel("(e.g. 8080)");
		lblPortDesc.setBounds(165, 205, 115, 14);
		contentPane.add(lblPortDesc);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}
		});
		btnNewButton.setBounds(97, 230, 89, 23);
		contentPane.add(btnNewButton);
	}
	
	private void login(String name, String address, int port) { // login
		
		dispose();
		new Client(name, address, port);
	}
}
