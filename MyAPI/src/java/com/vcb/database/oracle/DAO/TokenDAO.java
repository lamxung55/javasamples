/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.database.oracle.DAO;

import com.vcb.database.oracle.DBPoolConnection;
import com.vcb.database.oracle.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author Do Tuan Anh
 */
public class TokenDAO {
    
    private static final Logger log = Logger.getLogger(TokenDAO.class);
    
    public static String getTokenByPan(String pan) {
        log.info("Get token by pan:" + pan);
        Connection con = null;
        PreparedStatement prp = null;
        ResultSet rs = null;
        String token = "";
        try {
            con = DBPoolConnection.getConnection();
            String sql = "select token from vcb_tokenizations where pan = '" + pan + "'";
            prp = con.prepareStatement(sql);
            rs = prp.executeQuery();
            if (rs.next()) {
                token = rs.getString("token");
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            DBUtils.closeObject(rs);
            DBUtils.closeObject(prp);
            DBUtils.closeObject(con);
        }
        log.debug("Got token:" + token);
        return token;
    }
    
}
