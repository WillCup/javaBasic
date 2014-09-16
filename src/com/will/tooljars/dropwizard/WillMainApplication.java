package com.will.tooljars.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.will.tooljars.dropwizard.health.WillHealthCheck;
import com.will.tooljars.dropwizard.resource.WillResource;

public class WillMainApplication extends Application<WillConfiguration> {
//    private static final Logger logger = LoggerFactory.getLogger(WillMainService.class);

    public String getName() {
        return "Alert Engine";
    }

    public static void main(String[] args) throws Exception {
//        new WillMainApplication().run(args);
        String[] serverArgs = new String[args.length + 2];
        int i = 0;
        serverArgs[i++] = "server";
        serverArgs[i++] = "resource/dropwizard/suro-server.yml";
        for (String arg : args) {
            i++;
            serverArgs[i] = arg;
        }
        new WillMainApplication().run(serverArgs);
    }

    public void initialize(Bootstrap<WillConfiguration> bootstrap) {
        
    }

    public void run(WillConfiguration configuration,
            Environment environment) throws Exception {

        environment.jersey().register(new WillResource());
        environment.healthChecks().register("sample", new WillHealthCheck());
//        logger.info("Setting up alert engine service");
    }
}