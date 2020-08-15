/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vcb.services;

import com.vcb.model.TokenModel;
import com.vcb.bussiness.TokenizationMondoDBImpl;
import com.vcb.bussiness.TokenizationInterface;
import com.vcb.bussiness.TokenizationOracleImpl;
import com.vcb.model.TokenizationRequestMessage;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Do Tuan Anh
 */
@Path("mina")
public class TokenService {

    private static final Logger log = Logger.getLogger(TokenService.class);

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TokenService
     */
    public TokenService() {
    }

    /**
     * Retrieves representation of an instance of xxx.TokenService
     *
     * @return an instance of java.lang.String
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/getTokenByPan")
    public TokenModel getTokenByPan(TokenizationRequestMessage input) {
        try {
            log.info("getTokenByPan for pan:" + input.getPan());

            TokenizationInterface tokenService = new TokenizationOracleImpl();
            TokenModel token = tokenService.getTokenByPan(input.getPan());

            log.info("getTokenByPan for pan:" + input.getPan() + " - Result:" + token.toString());
            return token;
        } catch (Exception e) {
            log.error(e);
        }

        return null;
    }

}
