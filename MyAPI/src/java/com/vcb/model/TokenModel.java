/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.model;

import com.vcb.model.Pojo;

/**
 *
 * @author Do Tuan Anh
 */
public class TokenModel extends Pojo {
    private String token;
    private String pan;

    public TokenModel() {
        super();
    }
    
    

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public TokenModel(String token, String pan) {
        this.token = token;
        this.pan = pan;
    }

    @Override
    public String toString() {
        return "TokenModel{" + "token=" + token + ", pan=" + pan + '}';
    }
    
    
    
}
