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

/**
 *
 * @author Do Tuan Anh
 */
public class TokenizationMondoDBImpl implements TokenizationInterface {

    static final Logger log = Logger.getLogger(TokenizationMondoDBImpl.class);

    @Override
    public TokenModel getTokenByPan(String pan) {
        try {
            org.bson.Document document = (org.bson.Document) NosqlDB.findOne(Constants.TOKEN_TABLE_NAME, Filters.eq(Constants.TOKEN_COL_PAN_NAME, pan));

            ObjectMapper objectMapper = new ObjectMapper();

            Pojo.jsonToObj(document.toJson(), TokenModel.class);
            TokenModel token = objectMapper.readValue(document.toJson(), TokenModel.class);
            return token;
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
