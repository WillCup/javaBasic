package com.will.tooljars.eclipse.jetty;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Guice;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;

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
        Guice.createInjector(Stage.PRODUCTION, new MasterModule());
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server,
                "/", ServletContextHandler.SESSIONS);
        context.addFilter(GuiceFilter.class, "/*", EnumSet
                .<javax.servlet.DispatcherType> of(
                        javax.servlet.DispatcherType.REQUEST,
                        javax.servlet.DispatcherType.ASYNC));
        context.addServlet(DefaultServlet.class, "/*");
        server.start();
        server.join();
        // It is not a very useful server as it has no handlers and thus returns a 404 error for every request.
    }
}