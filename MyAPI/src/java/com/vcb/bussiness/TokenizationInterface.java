/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.bussiness;

import com.vcb.model.TokenModel;

/**
 *
 * @author Do Tuan Anh
 */
public interface TokenizationInterface {

    public TokenModel getTokenByPan(String pan);

    public TokenModel getPanByToken(String token);

}
