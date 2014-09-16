package com.will.io;

import java.io.File;

public class ApacheConfReader {
    public static void main(String[] args) {
        String apacheConf = "/etc/apache2/sites-enabled";
        File confDir = new File(apacheConf);
        for (File file : confDir.listFiles()) {
            
            System.out.println(file.getAbsolutePath());
        }
    }
}
