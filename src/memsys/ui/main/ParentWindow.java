/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.main;

import memsys.global.FunctionFactory;
import memsys.global.DBConn.MainDBConn;
import memsys.ui.user.Login;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import memsys.others.About;

/**
 *
 * @author LESTER
 */
public final class ParentWindow extends javax.swing.JFrame {

    public static int UGID, userid, gender;
    static Statement stmt;
    public static String fname;

    public ParentWindow() {
        initComponents();
        
        LoadStandardMenus(this);
        LoadMainMenus();

        //System.out.println(userid);
        this.setExtendedState(this.getExtendedState() | ParentWindow.MAXIMIZED_BOTH); //set JFrame maximized
        showuser();
//tempo();
    }

    public static int getUserID() {
        return userid;
    }

    public static String getUserName() {
        return fname;
    }

    public void GetFnameAndGender() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT FullName FROM Users WHERE UserID=" + userid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                fname = rs.getString(1);
                //gender = rs.getInt(2);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    void showuser() {
        GetFnameAndGender();

        String uico = "";
        if (gender == 1) {
            uico = "/img/malemini.png";
        } else if (gender == 2) {
            uico = "/img/femalemini.png";
        }
        String lbl = "<html><table border=0 cellpadding=0 cellspacing=0>"
                + "<tr><td><font color=#FF9900>Welcome </font><font color=#585858>" + fname + "!</font></td>"
                + "<td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img src=" + getClass().getResource("/img/logintime.png") + ">&nbsp</td><td><font color=#FF9900>Login Date/Time:&nbsp</font><font color=#585858>" + FunctionFactory.GetSystemNowDate() + "</font>&nbsp&nbsp<font color=#006600>" + FunctionFactory.GetSystemNowTime() + "</font></td></th>";
        lbluser.setText(lbl);
    }

    void LoadStandardMenus(final JFrame frame) {
        menubar.removeAll();

        //Load Basic Main Menus
        JMenu mnu = new JMenu();
       // mnu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JMenuItem iLogOff = new JMenuItem();
        JMenuItem iAbout = new JMenuItem();
        JMenuItem iExit = new JMenuItem();
        JMenuItem iSecSet = new JMenuItem();
        JSeparator s = new JSeparator();

        Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/aa.png"));
        mnu.setText("SYSTEM");
        mnu.setIcon(ico1);
        mnu.setMnemonic('S');

        menubar.add(mnu);

        Icon ico4 = new javax.swing.ImageIcon(getClass().getResource("/img/secset.png"));
        iSecSet.setIcon(ico4);
        iSecSet.setText("My User Account Settings");
        iSecSet.setMnemonic('y');

        Icon ico5 = new javax.swing.ImageIcon(getClass().getResource("/img/myinquiry.png"));
        iAbout.setIcon(ico5);
        iAbout.setText("About");
        iAbout.setMnemonic('A');

        Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/logoff2.png"));
        iLogOff.setIcon(ico2);
        iLogOff.setText("Log-off");
        iLogOff.setMnemonic('L');

        Icon ico3 = new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"));
        iExit.setText("Exit");
        iExit.setIcon(ico3);
        iExit.setMnemonic('x');

        //iExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
        mnu.add(iSecSet);
        mnu.add(iAbout);
        mnu.add(iLogOff);
        mnu.add(s);
        mnu.add(iExit);
        //--Basic Menus End Here

        //Action Listener for basic menus
        iSecSet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when Logoff button is pressed
                //frame.dispose();
                //sec_set frm = new sec_set();
                //sec_set.userid = userid;
                //myDesktop.add(frm);
                //frm.setVisible(true);

            }
        });

        iAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when Logoff button is pressed
               // frame.dispose();

                About frm = new About();
                myDesktop.add(frm);
               // frm.setIconImage(Toolkit.getDefaultToolkit().getImage("ico.png"));
                frm.setVisible(true);
            }
        });

        iLogOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when Logoff button is pressed
                frame.dispose();

                Login frm = new Login();
                frm.setIconImage(Toolkit.getDefaultToolkit().getImage("ico.png"));
                frm.setVisible(true);
            }
        });

        iExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when Exit button is pressed
                System.exit(0);
            }
        });

        frame.setJMenuBar(menubar);
        frame.pack();
    }

    void LoadMainMenus() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        //QUERY - GROUP ALL MENU HEADER TO BE ABLE TO DISPLAY IT IN MDI FORM
        createString = "SELECT mi.MHID, mh.Caption, mh.Mnemonic, mh.ico, mh.seq "
                + "FROM userGroupsAssignTBL ua,userGroupPrevTBL up, menuItemTBL mi, menuHeaderTBL mh "
                + "WHERE ua.UGID=up.UGID AND up.prevID=mi.prevID AND mi.MHID=mh.MHID AND ua.UserID=" + userid + " "
                + "GROUP BY mi.MHID, mh.Caption, mh.Mnemonic, mh.ico, mh.seq  "
                + "ORDER BY mh.seq";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                String shrt = rs.getString(3);
                char monic = shrt.charAt(0);
                //System.out.println(rs.getString(3));
                //System.out.println(rs.getString(4));
                Icon ico = new javax.swing.ImageIcon(getClass().getResource(rs.getString(4)));

                JMenu mnu = new JMenu();
               // mnu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                mnu.setMnemonic(monic);
                mnu.setIcon(ico);
                mnu.setText(rs.getString(2));
                menubar.add(mnu);

                LoadMenuItems(rs.getInt(1), mnu);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    void LoadMenuItems(int menuid, JMenu mnu) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        //QUERY - LOAD MENUITEMS
        // System.out.println(userid);
        createString = "SELECT mi.MIID, mi.MHID, mi.ItemCaption, mi.Mnemonic, p.classpath, p.display,  mi.ico,  mi.seq "
                + " FROM userGroupsAssignTBL ua,userGroupPrevTBL up, menuItemTBL mi, menuHeaderTBL mh, privilegesTBL p "
                + " WHERE ua.UGID=up.UGID AND up.prevID=mi.prevID AND mi.MHID=mh.MHID AND p.prevID=up.prevID AND ua.UserID = " + userid + " AND mi.MHID =" + menuid + " "
                + " GROUP BY   mi.seq, mi.ico ,mi.MHID, mi.MIID, mi.ItemCaption, mi.Mnemonic, p.classpath, p.display ORDER BY mi.seq ";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                //   System.out.println(rs.getString(5));
                Icon ico = new javax.swing.ImageIcon(getClass().getResource(rs.getString(7)));

                String shrt = rs.getString(4);
                char monic = shrt.charAt(0);

                JMenuItem mnuI = new JMenuItem();

                mnuI.setText(rs.getString(3));
                mnuI.setMnemonic(monic);
                mnuI.setIcon(ico);
                mnu.add(mnuI);

                //Action Listener for basic menus
                final String cls = rs.getString(5);
                final int disp = rs.getInt(6);

                mnuI.addActionListener(new ActionListener() {
                    //PUT ACTION ON MENUITEM CLICK
                    public void actionPerformed(ActionEvent e) {
                        //Execute when Logoff button is pressed

                        try {
                            try {
                                if (disp < 3) {
                                    JInternalFrame frame = (JInternalFrame) Class.forName(cls).newInstance();
                                    myDesktop.add(frame);
                                    if (disp == 1) {
                                        try {
                                            frame.setMaximum(true);
                                        } catch (PropertyVetoException ex) {
                                            Logger.getLogger(ParentWindow.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    frame.setVisible(true);
                                } else {
                                    JFrame frame = (JFrame) Class.forName(cls).newInstance();
                                    frame.setVisible(true);
                                }
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(ParentWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                Logger.getLogger(ParentWindow.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (java.lang.ClassNotFoundException se) {
                            JOptionPane.showMessageDialog(null, se.getMessage());
                        }

                    }
                });

            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myDesktop = new javax.swing.JDesktopPane();
        bck = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        lbluser = new javax.swing.JLabel();
        menubar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MemSys 2016");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        myDesktop.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        bck.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout bckLayout = new javax.swing.GroupLayout(bck);
        bck.setLayout(bckLayout);
        bckLayout.setHorizontalGroup(
            bckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );
        bckLayout.setVerticalGroup(
            bckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        myDesktop.add(bck);
        bck.setBounds(40, 50, 630, 350);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);

        lbluser.setText("Welcome!");
        jToolBar1.add(lbluser);

        menubar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myDesktop, javax.swing.GroupLayout.DEFAULT_SIZE, 1193, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myDesktop, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        bck.setBounds(0, 0, myDesktop.getWidth(), myDesktop.getHeight());
        bck.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay wimyDesktopt look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ParentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ParentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ParentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParentWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bck;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbluser;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JDesktopPane myDesktop;
    // End of variables declaration//GEN-END:variables
}
