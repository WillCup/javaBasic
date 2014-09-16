package com.will.tooljars.dropwizard.resource;

import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class WillResource {
    @GET
    @Path("/willPing")
    public String willPing() {
        Properties clientPros = new Properties();

        /*clientPros.setProperty(ClientConfig.CLIENT_TYPE,
                "sync");
        clientPros.setProperty(ClientConfig.LB_SERVER,
                "10.0.1.137:7101");
        clientPros.setProperty(ClientConfig.LB_TYPE,
                "");
        SuroClient sClient = new SuroClient(clientPros);*/
        return "will pong";
    }
}
