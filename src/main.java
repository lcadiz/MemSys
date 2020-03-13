
import java.sql.Statement;
import javax.swing.UIManager;
import memsys.ui.user.Login;

public class main {

    static Statement stmt;

    public static void main(String[] args) throws IllegalAccessException {

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
              //  InitLoad.loadBootRpt();
                //JFrame frame = (JFrame) Class.forName("mod.ui.user.Login").newInstance();
                Login frame=new Login();
//                //JFrame frame = (JFrame) Class.forName("Class.ui.Main.ParentWindow").newInstance();
//                //x frame=new x();
                frame.setVisible(true);
                
//                                CurrentSignature1 frame=new CurrentSignature1();
                //JFrame frame = (JFrame) Class.forName("Class.ui.Main.ParentWindow").newInstance();
                //x frame=new x();
//                frame.setVisible(true);

//         try {
//            myReports.rptBoot();
//        } catch (IOException ex) {
//            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
//        }
 
    }
}
