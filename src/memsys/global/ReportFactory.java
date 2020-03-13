/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import memsys.global.DBConn.MainDBConn;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author LESTER
 */
public class ReportFactory {

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

    public static void rptReconnection(int id, String bd, String office, String position, String title, String manager) {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
             parameters.put("id", id);
             parameters.put("bd", bd);
             parameters.put("office", office);
             parameters.put("position", position);
             parameters.put("title", title);
             
             parameters.put("manager", manager);
             

            jasperReport = JasperCompileManager.compileReport("rpt/discoreco/rptReconnection.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Reconnection Order");
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

    public static void rptCosting(int acctno, String name, String address, String totalcapt, String note, String draft) throws FileNotFoundException, IOException {

        try {
            JasperReport jasperReport;
            JasperPrint jPrint;

            //parameters
            HashMap parameters = new HashMap();
            parameters.put("ACCTNO", acctno);
            parameters.put("NAME", name);
            parameters.put("ADDRESS", address);
            parameters.put("TOTALCAPT", totalcapt);
            parameters.put("NOTE", note);
            parameters.put("DRAFT", draft);

            jasperReport = JasperCompileManager.compileReport("rpt/rptCostingPreview.jrxml");

            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());

//            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
//            Viewer.setTitle("Payment Costing - Print Preview");
//            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            OutputStream output = new FileOutputStream(new File("rpt/rptCostingPreview.pdf"));
            JasperExportManager.exportReportToPdfStream(jPrint, output);

//             Viewer.setVisible(true);
//            JRXlsExporter exporterXLS = new JRXlsExporter();
//            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jPrint);
//            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, output);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"rpt/report.xls");
//            exporterXLS.exportReport();
            output.flush();
            output.close();

            try {
//File pdfFile = new File("rpt/report.xls");
                File pdfFile = new File("rpt/rptCostingPreview.pdf");
                if (pdfFile.exists()) {

                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        //System.out.println("Awt Desktop is not supported!");
                    }

                } else {
                    //	System.out.println("File is not exists!");
                }

                //System.out.println("Done");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;

        }

    }//

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

    public static void rptConnectOrderPerMonth(int month, int year, String title) {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;
            //parameters
            HashMap parameters = new HashMap();
            parameters.put("month", month);
            parameters.put("year", year);
            parameters.put("title", title);
            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrderPerMonth.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());
            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order - Monthly");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rptConnectOrderPerDistrict(int towncode, String title) {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;
            //parameters
            HashMap parameters = new HashMap();
            parameters.put("towncode", towncode);
            parameters.put("title", title);
            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrderPerDistrict.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());
            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order - Per District");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rptConnectOrderPerStatus(int status, String title) {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;
            //parameters
            HashMap parameters = new HashMap();
            parameters.put("status", status);
            parameters.put("title", title);
            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrderPerStatus.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());
            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order - Per Status");
            Viewer.setExtendedState(Viewer.getExtendedState() | Viewer.MAXIMIZED_BOTH);
            Viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rptConnectOrderPerCustomDate(Date start, Date end, String title) {
        try {
            JasperReport jasperReport;
            JasperPrint jPrint;
            //parameters
            HashMap parameters = new HashMap();
            parameters.put("start", start);
            parameters.put("end", end);
            parameters.put("title", title);
            jasperReport = JasperCompileManager.compileReport("rpt/rptConnectOrderPerCustomDate.jrxml");
            jPrint = JasperFillManager.fillReport(jasperReport, parameters, MainDBConn.getConnection());
            JasperViewer Viewer = new JasperViewer(jPrint, false);
            //JasperExportManager.exportReportToPdf(jPrint);
            Viewer.setTitle("Connect Order - Custom Date");
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

    public static void main(String[] args) {
        try {
            //        String path=null;
            //        try {
            //            path = getPicPath();
            //            
            //        } catch (IOException ex) {
            //            Logger.getLogger(myReports.class.getName()).log(Level.SEVERE, null, ex);
            //        }
            //        rptID(path);
            rptCosting(1, "", "", "", "", "");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReportFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
