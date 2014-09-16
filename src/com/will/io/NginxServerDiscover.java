package com.will.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NginxServerDiscover implements PHPWebServerDiscoverInterface {
    
    public static final Pattern SERVER_NAME_PATTERN = Pattern.compile(
            "server_name .*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
    public static final Pattern SERVER_PORT_PATTERN = Pattern.compile(
            "listen .*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern COMMENT_PATTERN = Pattern.compile("#.*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
    public static final Pattern INCLUDE_PATTERN = Pattern.compile("include .*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
    public List<NginxServerInfo> tmpList = new CopyOnWriteArrayList<NginxServerInfo>();

    public List<NginxServerInfo> cacheList = new CopyOnWriteArrayList<NginxServerInfo>();
    
    public static void main(String[] args) {
        NginxServerDiscover nginxDiscover = new NginxServerDiscover();
        Thread thread = new Thread(nginxDiscover);
        thread.start();
    }
    
    public void run() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String nginxCfg = "C:/Users/Will/Desktop/nginx/nginx.conf";
            File confDir = new File(nginxCfg);
            readFile(confDir);
            System.out
                    .println("**************像以前一样，只要已经发送过了，那么就不再入选了******************");
            for (NginxServerInfo info : tmpList) {
                System.out.println(mapper.writeValueAsString(info));
            }

            cacheList.addAll(tmpList);
            tmpList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read and parse the configuration file. Including the included files or
     * directories, and Regex things like "include conf/*.conf".
     * 
     * @param file2Read
     * @throws IOException
     */
    public void readFile(File file2Read) throws IOException {
        String serverName = null;
        Matcher matcher = null;
        String group = null;
        String port = null;
        int lineCounter = 0;
        int portLine = 0;
        int serverNameLine = 0;

        List<String> readLines = FileUtils.readLines(file2Read);
        for (String line : readLines) {
            lineCounter++;
            System.out.println(file2Read.getName() + " >>>> " + line);
            if (StringUtils.isBlank(line))
                continue;
            /**
             * 如果是注释，直接略过
             */
            if (COMMENT_PATTERN.matcher(line).find())
                continue;
            /**
             * 如果是server_name， 就看看当前有么有port，有的话，就done了。done以后还要清空这两个值，以防影响后面的判断。
             */
            matcher = SERVER_NAME_PATTERN.matcher(line);
            if (matcher.find()) {
                group = matcher.group();
                System.out.println("Found SERVER_NAME_PATTERN : " + group);
                serverName = getPart2(group);
                serverNameLine = lineCounter;
                if (StringUtils.isNotBlank(port)) {
                    generateNewInfo(serverName, port, portLine, serverNameLine);
                    serverName = null;
                    port = null;
                }
            }
            /**
             * 如果是port， 就看看当前有么有server_name，有的话，就done了。done以后还要清空这两个值，以防影响后面的判断。
             */
            matcher = SERVER_PORT_PATTERN.matcher(line);
            if (matcher.find()) {
                group = matcher.group();
                port = getPart2(group);
                portLine = lineCounter;
                if (StringUtils.isNotBlank(serverName)) {
                    generateNewInfo(serverName, port, portLine, serverNameLine);
                    serverName = null;
                    port = null;
                }
            }
            /**
             * 如果是include, 就引入文件
             */
            matcher = INCLUDE_PATTERN.matcher(line);
            while (matcher.find()) {
                System.out
                        .println("############################################");
                System.out.println(matcher.group());
                String regexFile = getPart2(matcher.group());
                handleIncludedFile(file2Read, regexFile);
            }
        }
        System.out
                .println("----------------------------------------------------------");
    }

    public void generateNewInfo(String serverName, String port, int portLine,
            int serverNameLine) {
        NginxServerInfo info = new NginxServerInfo();
        info.setPort(port);
        info.setPortLine(portLine);
        info.setServerLine(serverNameLine);
        info.setServerName(serverName);
        if (!cacheList.contains(info)) {
            tmpList.add(info);
        }
    }

    public void handleIncludedFile(File currentFile, String regexFile)
            throws IOException {
        String currentDir = currentFile.getParent();
        File include = new File(currentDir + File.separator + regexFile);
        String regexPath = include.getAbsolutePath();
        if (include.exists()) {
            System.out.println("we got the included configuration file :"
                    + regexPath);
            if (include.isDirectory()) {
                System.out.println("we have to check out the directory "
                        + regexPath + " : ");
                for (File f : include.listFiles()) {
                    readFile(f);
                }
            } else {
                readFile(include);
            }
        } else {
            System.out.println("!!!!!!!!!!!This is a regex include operation"
                    + regexPath);
            String regexName = "."
                    + regexPath.substring(
                            regexPath.lastIndexOf(File.separator) + 1,
                            regexPath.length());
            String subDirStr = regexPath.substring(0,
                    regexPath.lastIndexOf(File.separator));
            File subDir = new File(subDirStr);
            for (File fil : subDir.listFiles()) {
                if (Pattern.matches(regexName, fil.getAbsolutePath())) {
                    readFile(fil);
                }
            }
        }
    }

    
    /**
     * For the classic configuration format: "ServerName wwww.cloudwise.com".
     * 
     * @param group
     * @return
     */
    public String getPart2(String group) {
        String trim = group.split("\\s", 2)[1].trim();
        return trim.substring(0, trim.lastIndexOf(";"));
    }
    
    /**
     * Insert the string into the host's section.
     * XXX bakFile should be handled outside.
     * 
     * @param host
     * @param str
     * @param confFile
     * @throws IOException 
     */
    public void insertHost(NginxServerInfo host, String str, File confFile) throws IOException {
        File bakFile = new File(confFile.getAbsoluteFile() + ".bak");
        FileUtils.copyFile(confFile, bakFile);
        int lineCounter = 0;
        String newContent = "";
        for (String line : FileUtils.readLines(confFile)) {
            lineCounter++;
            newContent += line;
            if (lineCounter == host.getPortLine()) {
                newContent += str;
            }
        }
        FileUtils.writeStringToFile(confFile, newContent, Charset.forName("UTF-8"));
        FileUtils.deleteQuietly(bakFile);
    }
    
    @JsonIgnoreProperties({ "portLine", "serverLine"})
    static class NginxServerInfo {
        String port;
        String serverName;
        int portLine;
        int serverLine;
        public String getPort() {
            return port;
        }
        public void setPort(String port) {
            this.port = port;
        }
        public String getServerName() {
            return serverName;
        }
        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
        public int getPortLine() {
            return portLine;
        }
        public void setPortLine(int portLine) {
            this.portLine = portLine;
        }
        public int getServerLine() {
            return serverLine;
        }
        public void setServerLine(int serverLine) {
            this.serverLine = serverLine;
        }
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((port == null) ? 0 : port.hashCode());
            result = prime * result
                    + ((serverName == null) ? 0 : serverName.hashCode());
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            NginxServerInfo other = (NginxServerInfo) obj;
            if (port == null) {
                if (other.port != null)
                    return false;
            } else if (!port.equals(other.port))
                return false;
            if (serverName == null) {
                if (other.serverName != null)
                    return false;
            } else if (!serverName.equals(other.serverName))
                return false;
            return true;
        }
        public String toString() {
            return "NginxServerInfo [port=" + port + ", serverName="
                    + serverName + ", portLine=" + portLine + ", serverLine="
                    + serverLine + "]";
        }
    }
}
