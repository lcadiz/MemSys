/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import memsys.global.DBConn.MainDBConn;

/**
 *
 * @author JoKoiZidac
 */
public class ORValidator {

    static Statement stmt;

    public double GetAmountPaid(String ORNo, int flg, String ORDate) {    //fld    1=Teller 2=Cashier
        double AmntPaid = 0;
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (flg == 1) {
            createString = "SELECT top 1 * FROM "
                    + "(SELECT ct.TransAmt FROM CollectionMisc cm "
                    + "    INNER JOIN CollectionTrans ct ON ct.TransID=cm.TransID "
                    + "    LEFT OUTER JOIN CollectionMiscName cmn ON cmn.TransID=ct.TransID "  //cm.AcctNo
                    + "WHERE cm.orno='" + ORNo + "' AND ct.TransDate='"+ORDate+"' AND ct.TransID NOT IN (SELECT transid FROM CollectionTransCancelled) "
                    + "UNION "
                    + "SELECT ct.TransAmt FROM CollectionBill cb "
                    + "    INNER JOIN CollectionTrans ct ON ct.TransID=cb.TransID "
                    + "    INNER JOIN Bill b ON b.BillNo=cb.BillNo "  //b.acctno
                    + "WHERE cb.orno='" + ORNo + "' AND ct.TransDate='"+ORDate+"' AND ct.TransID NOT IN (SELECT transid FROM CollectionTransCancelled) "
                    + ")Collection ";
        } else if (flg == 2) {
            createString = "SELECT ord.AmtPaid FROM ORTrans ort "
                    + "    INNER JOIN ORTransDetail ord  ON ord.ORTransID=ort.ORTransID " //ort.AcctNo
                    + "WHERE ord.ORNo='" + ORNo + "' AND ord.TransDate='"+ORDate+"' AND ord.ORTransID NOT IN (SELECT ORTransID FROM ORTransCancelled) ";
        }
        
//        if (flg == 1) {
//            createString = "SELECT top 1 * FROM "
//                    + "(SELECT ct.TransAmt FROM CollectionMisc cm "
//                    + "    INNER JOIN CollectionTrans ct ON ct.TransID=cm.TransID "
//                    + "    LEFT OUTER JOIN CollectionMiscName cmn ON cmn.TransID=ct.TransID "
//                    + "    LEFT OUTER JOIN Consumer c ON c.AcctNo=cm.AcctNo "
//                    + "    LEFT OUTER JOIN Users u ON u.UserID=ct.UserID "
//                    + "WHERE cm.orno='" + ORNo + "' AND ct.TransDate='"+ORDate+"' AND ct.TransID NOT IN (SELECT transid FROM CollectionTransCancelled) "
//                    + "UNION "
//                    + "SELECT ct.TransAmt FROM CollectionBill cb "
//                    + "    INNER JOIN CollectionTrans ct ON ct.TransID=cb.TransID "
//                    + "    INNER JOIN Bill b ON b.BillNo=cb.BillNo "
//                    + "    INNER JOIN Consumer c ON c.AcctNo=b.AcctNo "
//                    + "    INNER JOIN Users u ON u.UserID=ct.UserID "
//                    + "WHERE cb.orno='" + ORNo + "' AND ct.TransDate='"+ORDate+"' AND ct.TransID NOT IN (SELECT transid FROM CollectionTransCancelled) "
//                    + ")Collection ";
//        } else if (flg == 2) {
//            createString = "SELECT ord.AmtPaid FROM ORTrans ort "
//                    + "    INNER JOIN ORTransDetail ord  ON ord.ORTransID=ort.ORTransID "
//                    + "    LEFT OUTER JOIN Users u ON u.UserID=ort.UserID "
//                    + "    LEFT OUTER JOIN Consumer c ON c.AcctNo=ort.AcctNo "
//                    + "WHERE ord.ORNo='" + ORNo + "' AND ord.TransDate='"+ORDate+"' AND ord.ORTransID NOT IN (SELECT ORTransID FROM ORTransCancelled) ";
//        }

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                AmntPaid = rs.getDouble(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return AmntPaid;
    }

    public double GetCurrentBalance(String AcctNo) {
        double CurrentBalance = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = ""
                + "SELECT sum(totalamt-coalesce(amtpaid,0)) as balance FROM bill b "
                + "   INNER JOIN Consumer c "
                + "    on c.acctno=b.acctno "
                + "    left outer join collectiondata cd "
                + "    on b.billno=cd.billno "
                + "    WHERE c.acctno='" + AcctNo + "' and (totalamt-coalesce(amtpaid,0)>=0) and b.billno not in (select billno from billcancelled) and b.billno not in (select billno from billquestionable) and (b.duedate<=getdate()) "
                + "    GROUP BY c.acctno,c.membershipID,acctname "
                + "";

        int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                CurrentBalance = rs.getDouble(1);
                rc++;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return CurrentBalance;
    }

    public boolean ValidatePayments(String AcctNo, String ORNum, int flg, String ORDate) {
        boolean IsOk = false;
        
        
        ORValidator valdtr = new ORValidator();
        double AmntPaid = valdtr.GetAmountPaid(ORNum, flg, ORDate);
        double Balance = valdtr.GetCurrentBalance(AcctNo) + AmntPaid;
 System.out.println("Balance:"+Balance);
        double Percentage = AmntPaid / Balance * 100;

        if (Percentage > 50) {
            IsOk = true;
        }

        return IsOk;
    }

    public static void main(String[] args) {

        ORValidator x = new ORValidator();
        double f = x.GetAmountPaid("6546178", 1, "2019-08-19");
        boolean ff = x.ValidatePayments("217905", "6546178", 1, "2019-08-19");
        
        double d=x.GetCurrentBalance("937029");
        System.out.println(ff);
        System.out.println(f);
        System.out.println(d);
    }

}
