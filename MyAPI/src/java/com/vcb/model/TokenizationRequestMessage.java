/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.model;

/**
 *
 * @author Do Tuan Anh
 */
public class TokenizationRequestMessage extends RequestMessageBase{
    private String pan;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
    
}
