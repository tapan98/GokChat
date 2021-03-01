
package com.gok.chat;

import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.swing.text.DefaultCaret;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI for the Client
 */
public class ClientWindow extends javax.swing.JFrame implements Runnable {

    private int mouseX;
    private int mouseY;
    
    private DefaultCaret caret;
    
    final private PrintWriter sender;
    final private String username;
    private DateTimeFormatter timeFormat;
    private LocalDateTime timeNow;
    private boolean timestamp = true;
    private boolean getUsers = true;
    private int uID = -1;
    private int timeout = 0;
    
    
    /**
     * Front-end client
     * @param sender PrintWriter output_handle
     * @param name username
     */
    public ClientWindow(PrintWriter sender, String name) {
        
        this.sender = sender;
        this.username = name;
        
        initComponents();
        caret = (DefaultCaret)history.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        
        timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        
    }
    
    @Override
    public void run() {
        
        setVisible(true);
        
    }

    public void setUID(int UID) {
    
        this.uID = UID;
    }
    
    /**
     * Sends input value to the server or parses valid client side command.
     * If the command is unrecognized by the client, it is then sent to the server.
     * @param msg the input String reference
     */
    private void send(String msg) {

        if (msg.isEmpty()) {
            return;
        } else if (msg.startsWith("/")) {

            parseClientCommands(msg);
            return;

        }else {     
            if (Client.sender != null) {
                Client.sender.println(msg);
            }
            else {
                
                clientError("No connection to the server.");
            }

            txtMessage.setText("");
        }

    }
    
    /**
     * Parses client commands.
     * If the command is unrecognized, then it is send to the connected server.
     * @param command the input String reference
     */
    private void parseClientCommands(String command) {

        if (command.equalsIgnoreCase("/Q") || command.equalsIgnoreCase("/QUIT")) {

            Client.closeConnection();
            closeClient();

        } else if (command.equalsIgnoreCase("/TIMESTAMP")) {
            
            if (timestamp) timestamp = false;
            else timestamp = true;
            
        } else if ( command.equalsIgnoreCase("/CLEAR") ) {
            
            history.setText("");
        
        } else if( command.equalsIgnoreCase("/CHELP") ) {
            
            showHelp();
            
        }
        else { // must be a request for the server

            if (Client.sender != null) {
                Client.sender.println(command);
            }

        }
    }
    
    public void initGetUsers(){
        
        Thread handle = new Thread("GetUser"){
        
            public int sendRequest(){
                
                if ( Client.sender != null ){
                
                    Client.sender.println("[USRS]");
                    
                    return 0;
                } else {
                    
                    return -1;
                }
            }
            
            @Override
            public void run(){
              
                while(getUsers){
                    
                    try { 
                        
                        if (sendRequest() == -1 || timeout > 3) {

                            getUsers = false;
                            throw new Exception();
                        
                        } else {
                        
                            timeout++;
                        }
                        
                        Thread.sleep(5000); 
                                            
                    } catch (InterruptedException ex) { 
                        clientError("Interruption in GetUser thread!"); 
                    }
                    catch( Exception ex ){
                        clientError("Cannot get a list of Users");
                    }
                }
            }
            
        };
        
        handle.start();
    }
    
    /**
     * 
     * @param users Semicolon separated list of Users
     */
    
    public void setUsersList(String users) {
        
        timeout = 0;
        
        String usersArr[] = users.split(";");

        if ( usersArr.length != 0 ) {
            
            UsersList.setListData(usersArr);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelHead = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Title = new javax.swing.JLabel();
        PanelBody = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        history = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        LabelOnlineUsers = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        UsersList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gok Chat");
        setBackground(new java.awt.Color(0, 0, 0));
        setLocation(new java.awt.Point(0, 0));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(700, 540));
        setUndecorated(true);
        setSize(new java.awt.Dimension(700, 540));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelHead.setBackground(new java.awt.Color(209, 48, 61));
        PanelHead.setPreferredSize(new java.awt.Dimension(700, 40));
        PanelHead.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                PanelHeadMouseDragged(evt);
            }
        });
        PanelHead.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PanelHeadMousePressed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(16, 16));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });

        Title.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Title.setForeground(new java.awt.Color(255, 255, 255));
        Title.setText("Gok Chat");

        javax.swing.GroupLayout PanelHeadLayout = new javax.swing.GroupLayout(PanelHead);
        PanelHead.setLayout(PanelHeadLayout);
        PanelHeadLayout.setHorizontalGroup(
            PanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 418, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelHeadLayout.setVerticalGroup(
            PanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
            .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(PanelHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 40));

        PanelBody.setBackground(new java.awt.Color(33, 37, 43));

        jScrollPane1.setForeground(new java.awt.Color(102, 102, 102));

        history.setEditable(false);
        history.setBackground(new java.awt.Color(0, 0, 0));
        history.setColumns(20);
        history.setForeground(new java.awt.Color(255, 255, 255));
        history.setLineWrap(true);
        history.setRows(5);
        history.setAutoscrolls(false);
        history.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                historyKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(history);

        txtMessage.setBackground(new java.awt.Color(0, 0, 0));
        txtMessage.setForeground(new java.awt.Color(255, 255, 255));
        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMessageKeyPressed(evt);
            }
        });

        btnSend.setBackground(new java.awt.Color(51, 56, 66));
        btnSend.setForeground(new java.awt.Color(255, 255, 255));
        btnSend.setText("Send");
        btnSend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSendMouseClicked(evt);
            }
        });
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        LabelOnlineUsers.setForeground(new java.awt.Color(255, 255, 255));
        LabelOnlineUsers.setText("Online Users:");

        UsersList.setBackground(new java.awt.Color(0, 0, 0));
        UsersList.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(UsersList);

        javax.swing.GroupLayout PanelBodyLayout = new javax.swing.GroupLayout(PanelBody);
        PanelBody.setLayout(PanelBodyLayout);
        PanelBodyLayout.setHorizontalGroup(
            PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(LabelOnlineUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                .addContainerGap())
        );
        PanelBodyLayout.setVerticalGroup(
            PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addComponent(LabelOnlineUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMessage)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(PanelBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 980, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        closeClient();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void PanelHeadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_PanelHeadMousePressed

    private void PanelHeadMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMouseDragged
        // TODO add your handling code here:
        this.setLocation(this.getX() + evt.getX() - mouseX, this.getY() + evt.getY() - mouseY);
    }//GEN-LAST:event_PanelHeadMouseDragged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        // TODO add your handling code here:
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross_pressed.png")));
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        // TODO add your handling code here:
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross.png")));
    }//GEN-LAST:event_jLabel1MouseReleased

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnSendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendMouseClicked
        // TODO add your handling code here:
        send(txtMessage.getText());
    }//GEN-LAST:event_btnSendMouseClicked

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            send(txtMessage.getText());
            txtMessage.setText("");
        }
    }//GEN-LAST:event_txtMessageKeyPressed

    private void historyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_historyKeyPressed
        // TODO add your handling code here:
        char key = evt.getKeyChar();

        if ( (key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z') || (key >= '0' && key <='9') || key == '/' ) {
            
            txtMessage.requestFocus();
            txtMessage.setText(txtMessage.getText() + evt.getKeyChar());
            
        }else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
            send(txtMessage.getText());
        }
    }//GEN-LAST:event_historyKeyPressed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ClientWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ClientWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ClientWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ClientWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ClientWindow().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelOnlineUsers;
    private javax.swing.JPanel PanelBody;
    private javax.swing.JPanel PanelHead;
    private javax.swing.JLabel Title;
    private javax.swing.JList<String> UsersList;
    private javax.swing.JButton btnSend;
    private javax.swing.JTextArea history;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables

    public void showHelp(){
        
        clientMessage("Help is here!");
        clientMessage("/CHELP: Displays this help.");
        clientMessage("/Q or /QUIT: As you've probably guessed, BEGONE! :)");
        clientMessage("/CLEAR: Does some cleansing...");
        clientMessage("/TIMESTAMP: Toggles timestamp.");
    }
    
    /**
     * Appends Client logs to the history component
     * @param msg 
     */
    public void clientMessage(String msg) {
	
        println("[CLIENT]: " + msg);
    }
    
    /**
     * Appends Error message to the history component
     * @param msg 
     */
    public void clientError(String msg) {
		
	println("[ERROR]: " + msg);
    }

    /**
     * Appends String to the history component
     * @param msg 
     */
    public void messageAppend(String msg) {
        
        
        //history.append(msg);
        println(msg);
    }
    
    /**
     * closes Socket and terminates program by calling System.exit(0);
     */
    private void closeClient() {
        
        Client.closeConnection();
        System.exit(0);
    }
    
    /**
     * Appends String msg to the text area with a new line
     * @param msg 
     */
    public synchronized void println(String msg) {
    
        if (timestamp) {
            timeNow = LocalDateTime.now();
            history.append("[" + timeFormat.format(timeNow) + "] ");
        }
        
        history.append(msg + "\n");
    }
    
    /**
     * Sets window title
     * @param title 
     */
    public void title(String title) {
    
        Title.setText(title);
        this.setTitle(title);
    }
    
    /**
     * Sets ClientWindow Input text box String
     * @param txt 
     */
    public void setTextBoxText(String txt) {
        
        this.txtMessage.setText(txt);
    }
}
