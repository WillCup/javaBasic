package com.will.io;

import java.io.File;
import java.io.IOException;

import com.will.io.NginxServerDiscover.NginxServerInfo;

/**
 * The configuration reader for web servers like Apache, Nginx. (Their config file is not structured well)
 * 
 * @author Will
 * @created at 2014-9-9 上午9:45:09
 */
public interface PHPWebServerDiscoverInterface extends Runnable{

    /**
     * For the classic configuration format: "ServerName wwww.cloudwise.com".
     * 
     * @param group
     * @return
     */
    public String getPart2(String group);
    
    /**
     * Read and parse the configuration file. Including the included files or
     * directories, and Regex things like "Include conf/*.conf".
     * 
     * @param file2Read
     * @throws IOException
     */
    public void readFile(File file2Read) throws IOException;
    
    /**
     * @param currentFile
     * @param regexFile
     * @throws IOException
     */
    public void handleIncludedFile(File currentFile, String regexFile) throws IOException;
/*
    *//**
     * Insert the string into the host's section.
     * XXX bakFile should be handled outside.
     * 
     * @param host
     * @param str
     * @param confFile
     * @throws IOException 
     *//*
    public void insertHost(WebServerInfo host, String str, File confFile) throws IOException;*/
    
    static interface WebServerInfo {
        
    }
}
