/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.bussiness;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.vcb.database.mongodb.NosqlDB;
import com.vcb.model.Pojo;
import com.vcb.model.TokenModel;
import org.apache.log4j.Logger;
import com.vcb.common.Constants;
import com.vcb.database.oracle.DAO.TokenDAO;

/**
 *
 * @author Do Tuan Anh
 */
public class TokenizationOracleImpl implements TokenizationInterface {

    static final Logger log = Logger.getLogger(TokenizationOracleImpl.class);

    @Override
    public TokenModel getTokenByPan(String pan) {
        try {
            TokenModel tokenModel = new TokenModel();
            tokenModel.setPan(pan);
            tokenModel.setToken(TokenDAO.getTokenByPan(pan));
            return tokenModel;
        } catch (Exception ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public TokenModel getPanByToken(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
