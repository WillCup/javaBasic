package com.will.tooljars.eclipse.jetty;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

/**
 * Restful api for communication with plugins.
 * 
 * <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-11-18
 */
@Path("/")
@Singleton
public class MasterResource {
    private final static Log logger = LogFactory.getLog(MasterResource.class);

    public static final String SUCCESS_MSG = "agent success";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(@QueryParam("text") String text) {
        return text;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComplexObject echo(ComplexObject obj) {
        return obj;
    }

    /**
     * Just make sure that master's server is Ok.
     * 
     * <br/>
     * 
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/ping")
    public String ping() {
        return "pang";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/postData")
    public String postData( ) {
        return null;
    }

}

class ComplexObject {
    String key;
    int value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
