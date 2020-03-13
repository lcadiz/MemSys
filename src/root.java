
import java.io.IOException;
import javax.swing.UIManager;
import memsys.global.myReports;
import memsys.others.InitLoad;
import memsys.ui.user.Login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author LESTER
 */
public class root {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

//                    UIManager.put("nimbusBase", new Color(191, 98, 14));
//                    UIManager.put("nimbusBlueGrey", new Color(242, 242, 189));
//                    UIManager.put("control", new Color(242, 242, 189));
//                    UIManager.put("nimbusBase", new Color(75,0,130));
//                    UIManager.put("nimbusBlueGrey", new Color(123,104,238));
//                    UIManager.put("control", new Color(176,196,222));
            // Set nimbus look and feel. nimbusBase works only for it.
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        InitLoad.loadBootRpt();
        Login frame=new Login();
        frame.setVisible(true);
        System.out.println(java.lang.System.getProperty("java.library.path"));
//            a frame=new a();
        frame.setVisible(true);
        try {
            myReports.rptBoot();
        } catch (IOException ex) {
        }

//        SysUp frame = new SysUp();
//        frame.setVisible(true);

    }

}
