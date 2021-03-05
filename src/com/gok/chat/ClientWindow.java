
package com.gok.chat;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import javax.swing.text.DefaultCaret;
import java.time.format.DateTimeFormatter;

/**
 * GUI for the Client
 */
public class ClientWindow extends javax.swing.JFrame implements Runnable {

    private int mouseX;
    private int mouseY;
    
    private DefaultCaret caret;
    
    private DateTimeFormatter timeFormat;
    private LocalDateTime timeNow;
    private boolean timestamp = true;
    private boolean getUsers = true;
    private int timeout = 0;
    private Client server = null;
    
    
    /**
     * Front-end client
     * @param server object reference to call Client methods
     */
    public ClientWindow(Client server) {
        
        this.server = server;

        initComponents();
        
        caret = (DefaultCaret)history.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        
    }
    
    @Override
    public void run() {
        
        setVisible(true);
        
    }

    
    /**
     * Appends String msg to the text area with a new line
     * @param msg the text message to append to the JTextArea (history) component
     */
    public synchronized void println(String msg) {
    
        if (timestamp) {
            timeNow = LocalDateTime.now();
            history.append("[" + timeFormat.format(timeNow) + "] ");
        }
        
        history.append(msg + "\n");
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
     * Sets window title
     * @param title 
     */
    public void title(String title) {
    
        Title.setText(title);
        this.setTitle(title);
    }


    /**
     * Initiates online users query which in turn displays a list of users in GUI component.
     */
    public void initGetUsers(){
        
        getUsers = true;

        Thread handle = new Thread("GetUser"){
        
            public int sendRequest(){
                
                int returnValue = 1;

                returnValue = server.send("[USRS]");
                
                if ( returnValue == -1 )
                    return -1;
                
                return 0;
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
                        setUsersList("");
                    }
                }
            }
            
        };
        
        handle.start();
    }


    /**
     * Updates the Users list in the online users GUI component.
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
     * Sends input value to the server or parses valid client side command.
     * If the command is unrecognized by the client, it is then sent to the server.
     * Note: The command is recognized by a beginning character '/' forward slash
     * @param msg the input String reference
     */
    private void send(String msg) {

        if (msg.isEmpty()) {
            return;
        } else if (msg.startsWith("/")) {

            parseClientCommands(msg);
            return;

        }else {     

            int returnValue = 1;
            returnValue = server.send(msg);
            if ( returnValue == -1 ) {
                
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

            server.closeConnection();
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

            if ( server.send(command) == -1 ) {
                
                clientError("No connection to the server.");
            }

        }
    }
    
    private void setColorScheme(){
        
        try {
        
            // Title Bar Color
            PanelHead.setBackground(Color.decode(titleBarColTxtField.getText()));

            // Title Bar Text Color
            Title.setForeground(Color.decode(titleBarTextColTxtField.getText()));

            // Body Panel Background Color
            PanelBody.setBackground(Color.decode(bodyBgColTxtField.getText()));

            // Components Background Color
            history.setBackground(Color.decode(componentBgColTxtField.getText()));
            txtMessage.setBackground(Color.decode(componentBgColTxtField.getText()));
            UsersList.setBackground(Color.decode(componentBgColTxtField.getText()));
            titleBarColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));
            titleBarTextColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));
            bodyBgColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));
            componentBgColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));
            textColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));
            buttonBgColTxtField.setBackground(Color.decode(componentBgColTxtField.getText()));


            // Text Color
            history.setForeground(Color.decode(textColTxtField.getText()));
            txtMessage.setForeground(Color.decode(textColTxtField.getText()));
            UsersList.setForeground(Color.decode(textColTxtField.getText()));
            titleBarColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            titleBarTextColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            bodyBgColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            componentBgColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            textColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            buttonBgColTxtField.setForeground(Color.decode(textColTxtField.getText()));
            btnSend.setForeground(Color.decode(textColTxtField.getText()));
            btnSetDefaultCol.setForeground(Color.decode(textColTxtField.getText()));
            btnSetColors.setForeground(Color.decode(textColTxtField.getText()));

            
            // Button Background Color
            btnSend.setBackground(Color.decode(buttonBgColTxtField.getText()));
            btnSetDefaultCol.setBackground(Color.decode(buttonBgColTxtField.getText()));
            btnSetColors.setBackground(Color.decode(buttonBgColTxtField.getText()));
        
        } catch (NumberFormatException e) {
            
            clientError("Invalid Color Code!");
        }
    }
    
    // auto-generated!
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
        LabelOnlineUsers1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        titleBarColTxtField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        titleBarTextColTxtField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        bodyBgColTxtField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        componentBgColTxtField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textColTxtField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        buttonBgColTxtField = new javax.swing.JTextField();
        btnSetColors = new javax.swing.JButton();
        btnSetDefaultCol = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gok Chat");
        setBackground(new java.awt.Color(0, 0, 0));
        setLocation(new java.awt.Point(0, 0));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(980, 540));
        setUndecorated(true);
        setSize(new java.awt.Dimension(700, 540));
        getContentPane().setLayout(null);

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

        getContentPane().add(PanelHead);
        PanelHead.setBounds(0, 0, 980, 40);

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

        LabelOnlineUsers1.setForeground(new java.awt.Color(255, 255, 255));
        LabelOnlineUsers1.setText("Colors:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Title Bar Color:");

        titleBarColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        titleBarColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        titleBarColTxtField.setText("#D1303D");
        titleBarColTxtField.setToolTipText("");
        titleBarColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleBarColTxtFieldActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Title Bar Text Color:");

        titleBarTextColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        titleBarTextColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        titleBarTextColTxtField.setText("#FFFFFF");
        titleBarTextColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleBarTextColTxtFieldActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Body BG Color:");

        bodyBgColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        bodyBgColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        bodyBgColTxtField.setText("#21252B");
        bodyBgColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bodyBgColTxtFieldActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Component BG Color:");

        componentBgColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        componentBgColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        componentBgColTxtField.setText("#000000");
        componentBgColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                componentBgColTxtFieldActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Text Color:");

        textColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        textColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        textColTxtField.setText("#FFFFFF");
        textColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textColTxtFieldActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Button BG Color:");

        buttonBgColTxtField.setBackground(new java.awt.Color(0, 0, 0));
        buttonBgColTxtField.setForeground(new java.awt.Color(255, 255, 255));
        buttonBgColTxtField.setText("#333842");
        buttonBgColTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBgColTxtFieldActionPerformed(evt);
            }
        });

        btnSetColors.setBackground(new java.awt.Color(51, 56, 66));
        btnSetColors.setForeground(new java.awt.Color(255, 255, 255));
        btnSetColors.setText("Set Colors");
        btnSetColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetColorsActionPerformed(evt);
            }
        });

        btnSetDefaultCol.setBackground(new java.awt.Color(51, 56, 66));
        btnSetDefaultCol.setForeground(new java.awt.Color(255, 255, 255));
        btnSetDefaultCol.setText("Set Default Colors");
        btnSetDefaultCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDefaultColActionPerformed(evt);
            }
        });

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
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelOnlineUsers1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LabelOnlineUsers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelBodyLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(titleBarColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelBodyLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(titleBarTextColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelBodyLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bodyBgColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelBodyLayout.createSequentialGroup()
                                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 12, Short.MAX_VALUE))
                            .addGroup(PanelBodyLayout.createSequentialGroup()
                                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(btnSetDefaultCol))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(componentBgColTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(textColTxtField)
                                    .addComponent(buttonBgColTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(btnSetColors, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(244, 244, 244))))
        );
        PanelBodyLayout.setVerticalGroup(
            PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(txtMessage)))
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addComponent(LabelOnlineUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelOnlineUsers1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(titleBarColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(titleBarTextColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(bodyBgColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(componentBgColTxtField)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(buttonBgColTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSetColors)
                            .addComponent(btnSetDefaultCol))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(PanelBody);
        PanelBody.setBounds(0, 40, 1208, 500);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked

        closeClient();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void PanelHeadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMousePressed

        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_PanelHeadMousePressed

    private void PanelHeadMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMouseDragged

        this.setLocation(this.getX() + evt.getX() - mouseX, this.getY() + evt.getY() - mouseY);
    }//GEN-LAST:event_PanelHeadMouseDragged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross_pressed.png")));
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross.png")));
    }//GEN-LAST:event_jLabel1MouseReleased

    private void btnSetColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetColorsActionPerformed

        setColorScheme();
    }//GEN-LAST:event_btnSetColorsActionPerformed

    private void buttonBgColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBgColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_buttonBgColTxtFieldActionPerformed

    private void textColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_textColTxtFieldActionPerformed

    private void componentBgColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_componentBgColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_componentBgColTxtFieldActionPerformed

    private void bodyBgColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bodyBgColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_bodyBgColTxtFieldActionPerformed

    private void titleBarTextColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleBarTextColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_titleBarTextColTxtFieldActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed

        send(txtMessage.getText());
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            send(txtMessage.getText());
            txtMessage.setText("");
        }
    }//GEN-LAST:event_txtMessageKeyPressed

    private void historyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_historyKeyPressed

        char key = evt.getKeyChar();

        if ( (key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z') || (key >= '0' && key <='9') || key == '/' ) {

            txtMessage.requestFocus();
            txtMessage.setText(txtMessage.getText() + evt.getKeyChar());

        }else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            send(txtMessage.getText());
        }
    }//GEN-LAST:event_historyKeyPressed

    private void btnSetDefaultColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDefaultColActionPerformed

        
        // Title Bar Color
        PanelHead.setBackground(Color.decode("#D1303D"));
        titleBarColTxtField.setText("#D1303D");

        // Title Bar Text Color
        Title.setForeground(Color.decode("#FFFFFF"));
        titleBarTextColTxtField.setText("#FFFFFF");

        // Body Panel Background Color
        PanelBody.setBackground(Color.decode("#21252B"));
        bodyBgColTxtField.setText("#21252B");

        // Components Background Color
        history.setBackground(Color.decode("#000000"));
        txtMessage.setBackground(Color.decode("#000000"));
        UsersList.setBackground(Color.decode("#000000"));
        titleBarColTxtField.setBackground(Color.decode("#000000"));
        titleBarTextColTxtField.setBackground(Color.decode("#000000"));
        bodyBgColTxtField.setBackground(Color.decode("#000000"));
        componentBgColTxtField.setBackground(Color.decode("#000000"));
        textColTxtField.setBackground(Color.decode("#000000"));
        buttonBgColTxtField.setBackground(Color.decode("#000000"));
        componentBgColTxtField.setText("#000000");


        // Text Color
        history.setForeground(Color.decode("#FFFFFF"));
        txtMessage.setForeground(Color.decode("#FFFFFF"));
        UsersList.setForeground(Color.decode("#FFFFFF"));
        titleBarColTxtField.setForeground(Color.decode("#FFFFFF"));
        titleBarTextColTxtField.setForeground(Color.decode("#FFFFFF"));
        bodyBgColTxtField.setForeground(Color.decode("#FFFFFF"));
        componentBgColTxtField.setForeground(Color.decode("#FFFFFF"));
        textColTxtField.setForeground(Color.decode("#FFFFFF"));
        buttonBgColTxtField.setForeground(Color.decode("#FFFFFF"));
        btnSend.setForeground(Color.decode("#FFFFFF"));
        btnSetDefaultCol.setForeground(Color.decode("#FFFFFF"));
        btnSetColors.setForeground(Color.decode("#FFFFFF"));
        textColTxtField.setText("#FFFFFF");

        // Button Background Color
        btnSend.setBackground(Color.decode("#333842"));
        btnSetDefaultCol.setBackground(Color.decode("#333842"));
        btnSetColors.setBackground(Color.decode("#333842"));
        buttonBgColTxtField.setText("#333842");
        
    }//GEN-LAST:event_btnSetDefaultColActionPerformed

    private void titleBarColTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleBarColTxtFieldActionPerformed

        setColorScheme();
    }//GEN-LAST:event_titleBarColTxtFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelOnlineUsers;
    private javax.swing.JLabel LabelOnlineUsers1;
    private javax.swing.JPanel PanelBody;
    private javax.swing.JPanel PanelHead;
    private javax.swing.JLabel Title;
    private javax.swing.JList<String> UsersList;
    private javax.swing.JTextField bodyBgColTxtField;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnSetColors;
    private javax.swing.JButton btnSetDefaultCol;
    private javax.swing.JTextField buttonBgColTxtField;
    private javax.swing.JTextField componentBgColTxtField;
    private javax.swing.JTextArea history;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField textColTxtField;
    private javax.swing.JTextField titleBarColTxtField;
    private javax.swing.JTextField titleBarTextColTxtField;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables

    private void showHelp(){
        
        clientMessage("Help is here!");
        clientMessage("/CHELP: Displays this help.");
        clientMessage("/Q or /QUIT: As you've probably guessed, BEGONE! :)");
        clientMessage("/CLEAR: Does some cleansing...");
        clientMessage("/TIMESTAMP: Toggles timestamp.");
    }
    
    
    /**
     * closes Socket and terminates program by calling System.exit(0);
     */
    private void closeClient() {
        
        server.closeConnection();
        System.exit(0);
    }

}
