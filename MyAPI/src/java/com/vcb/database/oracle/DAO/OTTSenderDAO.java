/*    */ package com.vcb.database.oracle.DAO;

/*    */
 /*    */
import com.vcb.database.oracle.DBPoolConnection;
import com.vcb.database.oracle.DBUtils;
/*    */ import java.sql.CallableStatement;
/*    */ import java.sql.Connection;
/*    */ import org.apache.log4j.Logger;

/*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */
 /*    */ public class OTTSenderDAO /*    */ {

    /*  29 */ private static final Logger log = Logger.getLogger(DBPoolConnection.class);

    /*    */
 /*    */ public static String insertOTTMessage(String mobileNo, String cif, String content, String devideType, String imei, String rq) {
        /* 26 */ Connection con = null;
        /* 27 */ CallableStatement cs = null;
        /* 28 */ String rs = "";
        /*    */ try {
            /* 30 */ log.info("insertOTTMessage@mobileNo:" + mobileNo);
            /* 31 */ String sql = "{call ABC_PACKAGE.proc_insert_ott_message(?,?,?,?,?,?,?)}";
            /* 32 */ con = DBPoolConnection.getConnection();
            /* 33 */ cs = con.prepareCall(sql);
            /* 34 */ cs.registerOutParameter(1, 12);
            /* 35 */ cs.setString(2, rq);
            /* 36 */ cs.setString(3, mobileNo);
            /* 37 */ cs.setString(4, cif);
            /* 38 */ cs.setString(5, content);
            /* 39 */ cs.setString(6, devideType);
            /* 40 */ cs.setString(7, imei);
            /* 41 */ cs.executeQuery();
            /* 42 */ rs = (String) cs.getObject(1);
            /* 43 */        } catch (Throwable e) {
            /* 44 */ log.error(e.getMessage(), e);
            /*    */        } finally {
            /* 46 */ DBUtils.closeObject(cs);
            /* 47 */ DBUtils.closeObject(con);
            /*    */        }
        /* 49 */ return rs;
        /*    */    }
    /*    */ }


/* Location:              D:\Projects\test\ottJob\VCB_OTT_Jobs.jar!\com\vnpay\database\DAO\OTTSenderDAO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
