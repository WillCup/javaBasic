package com.will.tooljars.eclipse.jetty;

import org.eclipse.jetty.server.Server;

/**
 * To embed a Jetty server, the following steps are typical:
        
        Create the server
        Add/Configure Connectors
        Add/Configure Handlers
        Add/Configure Servlets/Webapps to Handlers
        Start the server
        Wait (join the server to prevent main exiting)
 * 
 * @author Will
 * @created at 2014-8-13 下午5:41:14
 */
public class SimplestServer
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.start();
        server.join();
        // It is not a very useful server as it has no handlers and thus returns a 404 error for every request.
    }
}