/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.others;

import memsys.global.DBConn.MainDBConn;
import java.util.HashMap;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class InitLoad {

  public static void loadBootRpt()  {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;
            HashMap parameters = new HashMap();
            jasperReport = JasperCompileManager.compileReport("rpt/rptboot.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());
            JasperViewer Viewer = new JasperViewer(jPrint, false);
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;

        }
    }
}
