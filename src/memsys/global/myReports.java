package memsys.global;

import memsys.global.DBConn.MainDBConn;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
//import net.sf.jasperreports.engine.data.JRXmlDataSource;
//import net.sf.jasperreports.engine.export.JRXlsExporter;

public class myReports {
    
    
     public static void rptMeterTestData() {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
           // parameters.put("mpID", mpID);

            jasperReport = JasperCompileManager.compileReport("rpt/rptMeterTestData.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Meter Test Data");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
    
        public static void rptMeterRecordCard(int mpID) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("mpID", mpID);

            jasperReport = JasperCompileManager.compileReport("rpt/rptMeterRecordCard.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Meter Record Card");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptCertificate(int param) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("batchID", param);

            jasperReport = JasperCompileManager.compileReport("rpt/rptPartCert.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("PMES Participant Certificate");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptCertReprint1b1(String param) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("partID", param);

            jasperReport = JasperCompileManager.compileReport("rpt/rptReprintPartCert1b1.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("PMES Participant Certificate Re-Print");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptCosting(int acctno, String name, String address, String totalcapt, String note, String draft, String pby) {

        JasperReport jasperReport;
        JasperPrint jPrint;

        HashMap parameters = new HashMap();
        parameters.put("ACCTNO", acctno);
        parameters.put("NAME", name);
        parameters.put("ADDRESS", address);
        parameters.put("TOTALCAPT", totalcapt);
        parameters.put("NOTE", note);
        parameters.put("DRAFT", draft);
        parameters.put("printby", pby);

        try {
            jasperReport = JasperCompileManager.compileReport("rpt/rptCostingPreview.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(myReports.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void rptCosting2(int acctno, String name, String address, String totalcapt, String note, String draft) {

        JasperReport jasperReport;
        JasperPrint jPrint;

        HashMap parameters = new HashMap();
        parameters.put("ACCTNO", acctno);
        parameters.put("NAME", name);
        parameters.put("ADDRESS", address);
        parameters.put("TOTALCAPT", totalcapt);
        parameters.put("NOTE", note);
        parameters.put("DRAFT", draft);

        try {
            jasperReport = JasperCompileManager.compileReport("rpt/rptCostingPreview.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);

            Viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(myReports.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

     public static void rptCostingInquiry(int acctno, String name, String address, String totalcapt, String pby, String draft) {

        JasperReport jasperReport;
        JasperPrint jPrint;

        HashMap parameters = new HashMap();
        parameters.put("ACCTNO", acctno);
        parameters.put("NAME", name);
        parameters.put("ADDRESS", address);
        parameters.put("TOTALCAPT", totalcapt);
        parameters.put("DRAFT", draft);
        parameters.put("printby", pby);

        try {
            jasperReport = JasperCompileManager.compileReport("rpt/costinginquiry/rptCostingPreview.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(myReports.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public static void rptConnectOrder(String AcctNo, String dateIssued, String MemID) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("ACCTNO", AcctNo);
            parameters.put("DATEISSUED", dateIssued);
            parameters.put("MEMBERID", MemID);

            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrder.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptConnectOrder1(String AcctNo) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("ACCTNO", AcctNo);
           //  parameters.put("SDW", sdw);

            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrdNew.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptConnectOrderWH(String AcctNo, String unym) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("ACCTNO", AcctNo);
            parameters.put("SDW", "");
            parameters.put("USER", unym);

            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrdNewWH.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static void rptConnectOrderInquiry(String AcctNo, String unym) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("ACCTNO", AcctNo);
            parameters.put("SDW", "");
            parameters.put("USER", unym);

            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrdNewInquiry.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }


    public static void rptID(String picpath, String sigpath) {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("PHOTOPATH", picpath);
            parameters.put("SIGPATH", sigpath);
            //  parameters.put("ORNO", orno);

            jasperReport = JasperCompileManager.compileReport("rpt/rptID2.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Members Identification Card");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public static String getPicPath() throws IOException {
        Scanner scanSTR = new Scanner(new File("picPathIDConfig.txt"));
        String Str = scanSTR.nextLine();
        return Str;
    }

    public static void rptSDWLaborCharges(Date sDate, Date eDate) throws FileNotFoundException, IOException {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("from", sDate);
            parameters.put("to", eDate);

            jasperReport = JasperCompileManager.compileReport("rpt/rptSDWLaborCharges.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);

            Viewer.setTitle("SDW Stringing Labor Charges");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;

        }

    }

    public static void rptBoot() throws FileNotFoundException, IOException {
        try {
            JasperReport jasperReport;

            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();

            jasperReport = JasperCompileManager.compileReport("rpt/rptboot.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            //return;
        }
    }

    public static void main(String[] args) {

    }
}
