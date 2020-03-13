/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPFactory {

    private String ftphost;
    private int ftpport;
    private String user;
    private String pass;

    private void getConfigParameters() {
        String filePath = System.getProperty("user.dir") + "\\config.properties";
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException ex) {
            Logger.getLogger(FTPFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        ftphost = properties.getProperty("ftphost");
        String ftptemp = properties.getProperty("ftpport");
        ftpport = Integer.parseInt(ftptemp);
        user = "Admin";
        pass = "SudoMaster2017";
    }

    public String GetFTPPicPath() {
        getConfigParameters();
        String path = "ftp://" + user + ":" + pass + "@" + ftphost + ":" + ftpport + "/img/pic/";
        return path;
    }

    public String GetFTPSigPath() {
        getConfigParameters();
        String path = "ftp://" + user + ":" + pass + "@" + ftphost + ":" + ftpport + "/img/signature/";
        return path;
    }

    public void FTPViewImage(String urlstr, JLabel lbl) {
        try {
            URL url = new URL(urlstr);
            BufferedImage myImage;

            myImage = ImageIO.read(url);
            lbl.setIcon(new ImageIcon(myImage));
            lbl.revalidate();
        } catch (IOException ex) {
            //Logger.getLogger(FTPFactory.class.getName()).log(Level.SEVERE, null, ex);
            //Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/nophoto.jpg"));
            lbl.setIcon(null);
            lbl.revalidate();
        }
    }

    public void FTPSaveImage(String local, String remote) {
        getConfigParameters();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftphost, ftpport);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            File LocalFile = new File(local);
            String RemoteFile = remote;
            InputStream IStream = new FileInputStream(LocalFile);

            OutputStream outputStream = ftpClient.storeFileStream(RemoteFile);
            byte[] bytesIn = new byte[4096];
            int read = 0;

            while ((read = IStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            IStream.close();
            outputStream.close();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public String GetFTPVersionPath() {
        getConfigParameters();
        String path = "ftp://" + user + ":" + pass + "@" + ftphost + ":" + ftpport + "/updates/memsys/version.properties";
        return path;
    }

    public void FTPUpdate() {
        getConfigParameters();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftphost, ftpport);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory("updates/memsys");
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                System.out.println(file.getName());
                // APPROACH #1: using retrieveFile(String, OutputStream)
                if (file.isDirectory()) {
                } else {
                    SysUp x = new SysUp();
                    x.insertlog(file.getName());

                    String remoteFile1 = "/updates/memsys/" + file.getName();
                    File downloadFile1 = new File(file.getName());
                    OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                    boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
                    outputStream1.close();
                }
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void FTPUpdateLib() {
        getConfigParameters();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftphost, ftpport);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory("updates/memsys/lib");
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                System.out.println(file.getName());

                // APPROACH #1: using retrieveFile(String, OutputStream)
                SysUp x = new SysUp();
                x.insertlog(file.getName());
                String remoteFile1 = "/updates/memsys/lib/" + file.getName();
                File downloadFile1 = new File("./lib/" + file.getName());
                OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
                outputStream1.close();
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        FTPFactory x = new FTPFactory();
        x.FTPUpdateLib();
        x.FTPUpdate();
    }

}
