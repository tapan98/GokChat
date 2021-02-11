package com.gok.chat;

import java.awt.event.KeyEvent;

public class Login extends javax.swing.JFrame {

    
    private int mouseX;
    private int mouseY;
    
    public Login() {
        initComponents();
    }
    
    /**
     * 
     * @return
     * 1 = Invalid name;
     * 2 = Invalid IP;
     * 3 = Invalid Port
     */
    int checkConstraints() {
        
        //boolean login = false;
        String name = Name.getText();
        String address = Address.getText();
        
        try {
            
            Integer.parseInt(Port.getText());
            
            if (name.contains("[") || name.contains("]") || name.isEmpty() ) {
                
                return 1; // name
                
            } else if (address.isBlank()) return 2; // address
            
        } catch (NumberFormatException e) {
        
            return 3; // port
        }
        
        return 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelHead = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        PanelBody = new javax.swing.JPanel();
        Address = new javax.swing.JTextField();
        Name = new javax.swing.JTextField();
        Port = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Login = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gok Chat Login");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(300, 340));
        setUndecorated(true);
        setSize(new java.awt.Dimension(300, 340));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelHead.setBackground(new java.awt.Color(209, 48, 61));
        PanelHead.setPreferredSize(new java.awt.Dimension(300, 40));
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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gok Chat Login");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross.png"))); // NOI18N
        jLabel5.setText("jLabel5");
        jLabel5.setMaximumSize(new java.awt.Dimension(16, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(16, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(16, 16));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel5MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout PanelHeadLayout = new javax.swing.GroupLayout(PanelHead);
        PanelHead.setLayout(PanelHeadLayout);
        PanelHeadLayout.setHorizontalGroup(
            PanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelHeadLayout.setVerticalGroup(
            PanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(PanelHead, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 40));

        PanelBody.setBackground(new java.awt.Color(33, 37, 43));
        PanelBody.setMinimumSize(new java.awt.Dimension(300, 300));

        Address.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        Address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddressActionPerformed(evt);
            }
        });
        Address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AddressKeyReleased(evt);
            }
        });

        Name.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameActionPerformed(evt);
            }
        });
        Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                NameKeyReleased(evt);
            }
        });

        Port.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        Port.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                PortKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("IP Address:");

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Username:");

        jLabel4.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Port:");

        Login.setBackground(new java.awt.Color(51, 56, 66));
        Login.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Login.setForeground(new java.awt.Color(255, 255, 255));
        Login.setText("Login");
        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("(e.g. 192.168.1.1)");

        jLabel7.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("(e.g. 8080)");

        javax.swing.GroupLayout PanelBodyLayout = new javax.swing.GroupLayout(PanelBody);
        PanelBody.setLayout(PanelBodyLayout);
        PanelBodyLayout.setHorizontalGroup(
            PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBodyLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(PanelBodyLayout.createSequentialGroup()
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Port, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7))
                .addGap(20, 20, 20))
        );
        PanelBodyLayout.setVerticalGroup(
            PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBodyLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(22, 22, 22)
                .addGroup(PanelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(Login, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        getContentPane().add(PanelBody, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 300, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddressActionPerformed

    private void PanelHeadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_PanelHeadMousePressed

    private void PanelHeadMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelHeadMouseDragged
        // TODO add your handling code here:
        this.setLocation(this.getX() + evt.getX() - mouseX, this.getY() + evt.getY() - mouseY);
    }//GEN-LAST:event_PanelHeadMouseDragged

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginActionPerformed
        // TODO add your handling code here:
        
        if (checkConstraints() == 0) {
            
            login(Name.getText(), Address.getText(), Integer.parseInt(Port.getText()));
        }
        
        
    }//GEN-LAST:event_LoginActionPerformed

    /**
     * Disposes itself, creates Client Object and calls runClient method
     * @param name username
     * @param address IP Address
     * @param port Port number
     */
    private void login(String name, String address, int port) {

        dispose();
        
        Client client = new Client(name, address, port);
        client.runClient();
    }
    private void NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameActionPerformed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        // TODO add your handling code here:
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross_pressed.png")));
    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseReleased
        // TODO add your handling code here:
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gok/chat/images/cross.png")));
    }//GEN-LAST:event_jLabel5MouseReleased

    private void NameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NameKeyReleased
        // TODO add your handling code here:
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            
            if (checkConstraints() == 0) {
            
            login(Name.getText(), Address.getText(), Integer.parseInt(Port.getText()));
            }
        }
        
    }//GEN-LAST:event_NameKeyReleased

    private void AddressKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AddressKeyReleased
        // TODO add your handling code here:
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            
            if (checkConstraints() == 0) {
            
            login(Name.getText(), Address.getText(), Integer.parseInt(Port.getText()));
            }
        }
    }//GEN-LAST:event_AddressKeyReleased

    private void PortKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PortKeyReleased
        // TODO add your handling code here:
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) {
            
            if (checkConstraints() == 0) {
            
            login(Name.getText(), Address.getText(), Integer.parseInt(Port.getText()));
            }
        }
    }//GEN-LAST:event_PortKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Address;
    private javax.swing.JButton Login;
    private javax.swing.JTextField Name;
    private javax.swing.JPanel PanelBody;
    private javax.swing.JPanel PanelHead;
    private javax.swing.JTextField Port;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
