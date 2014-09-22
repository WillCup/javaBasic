package com.will.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApacheAppDiscover implements PHPWebServerDiscoverInterface {
    public static final String SERVER_NAME = "SERVERNAME";

    public static final Pattern INCLUDE_PATTERN = Pattern.compile("Include.*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern VHOST_START_PATTERN = Pattern.compile(
            "<VirtualHost .*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern VHOST_END_PATTERN = Pattern.compile(
            "</VirtualHost>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern VHOST_ENABLE_PATTERN = Pattern.compile(
            "NameVirtualHost .*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern SERVER_NAME_PATTERN = Pattern.compile(
            "ServerName .*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static final Pattern COMMENT_PATTERN = Pattern.compile("#.*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * Check that whether current location is in a <></> section or not.
     */
    public AtomicBoolean isInVHostSection = new AtomicBoolean(false);

    /**
     * Current enabled hosts. We just want the info of the host in this list. We
     * should clear the list before we begin another round. Cause if some hosts
     * in this list has been disabled, then they should remove from the list.
     * But to find a disabled host is more difficult then to discover a enabled
     * host. So, just clear.
     */
    public List<String> enablesHosts = new CopyOnWriteArrayList<String>();

    public List<ApacheHostInfo> tmpList = new CopyOnWriteArrayList<ApacheHostInfo>();

    public List<ApacheHostInfo> cacheList = new CopyOnWriteArrayList<ApacheHostInfo>();

    public String globalServerName = null;
    
    public AtomicBoolean isModuleOn = new AtomicBoolean(false);
    
    public static final String loadModuleStr = "LoadModule smart_agent_module modules/mod_smart_agent.so";
    
    public static final String hostEnable = "AddOutputFilter OUTPUT  php html htm";

    public static void main(String[] args) {
        ApacheAppDiscover apacheReader = new ApacheAppDiscover();
        Thread thread = new Thread(apacheReader);
        thread.start();
    }

    /**
     * Handle the key words things, and save them into cached list.
     * 
     * @param file2Read
     * @throws IOException
     */
    public void readFile(File file2Read) throws IOException {
        String serverName = null;
        Matcher matcher = null;
        String group = null;
        String vhost = null;
        int lineCounter = 0;
        int startLineNum = 0;
        ApacheHostInfo domainInfo = null;

        List<String> readLines = FileUtils.readLines(file2Read);
        for (String line : readLines) {
            if (line.trim().startsWith("Listen")) {
                System.out.println(file2Read.getName() + " >>>> " + line);
            }
            lineCounter++;
//            System.out.println(file2Read.getName() + " >>>> " + line);
            if (StringUtils.isBlank(line))
                continue;
            /**
             * 如果是注释，直接略过
             */
            if (COMMENT_PATTERN.matcher(line).find())
                continue;
            /**
             * 如果是NameVirtualHost，那么应该找到开启了的VHOST端口
             */
            matcher = VHOST_ENABLE_PATTERN.matcher(line);
            if (matcher.find()) {
                group = matcher.group();
                System.out.println("Found VHOST_ENABLE : " + group);
                vhost = getPart2(group);
                // 加入已经启动的host列表
                enablesHosts.add(vhost);
            }
            /**
             * 如果是VHOST的开始
             */
            matcher = VHOST_START_PATTERN.matcher(line);
            if (matcher.find()) {
                group = matcher.group();
                isInVHostSection.set(true);
                vhost = getPart2(group);
                startLineNum = lineCounter;
                if (vhost.endsWith(">")) {
                    vhost = vhost.substring(0, vhost.length() - 1);
                }
            }
            /**
             * 如果是ServerName
             */
            matcher = SERVER_NAME_PATTERN.matcher(line);
            if (matcher.find()) {
                group = matcher.group();
                // 如果在vhost section里
                if (isInVHostSection.get()) {
                    serverName = getPart2(group);
                } else {
                    globalServerName = getPart2(group);
                }
            }
            /**
             * 如果是VHOST的结束. 一旦结束，马上清帐. 1. 如果vhost不为空，而且已经被启动，那么就可以加入发现的列表。 2.
             * 关掉insection。
             */
            matcher = VHOST_END_PATTERN.matcher(line);
            if (matcher.find()) {
                System.out.println("++++++++++++++++++++++++ " + serverName);
                if (StringUtils.isNotBlank(vhost)
                        && enablesHosts.contains(vhost)) {
                    domainInfo = new ApacheHostInfo();
                    domainInfo.setVhost(vhost);
                    if (StringUtils.isNotBlank(serverName)) {
                        System.out.println("ssssssssssssssssssssssssssss "
                                + serverName);
                        domainInfo.setServerName(serverName);
                    } else {
                        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSS "
                                + globalServerName);
                        domainInfo.setServerName(globalServerName);
                    }
                    domainInfo.setFileFullName(file2Read.getAbsolutePath());
                    domainInfo.setStartLine(startLineNum);
                    domainInfo.setEndLine(lineCounter); // current line is
                                                        // just the endLine.
                    System.out
                            .println("++++++++++++++++++++++++ " + domainInfo);
                    if (!cacheList.contains(domainInfo)) {
                        tmpList.add(domainInfo);
                    }
                }
                isInVHostSection.set(false);
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

    public String getPart2(String group) {
        return group.split("\\s", 2)[1].trim();
    }
    
    public void insertHost(ApacheHostInfo host, String str, File confFile) throws IOException {
        File bakFile = new File(confFile.getAbsoluteFile() + ".bak");
        FileUtils.copyFile(confFile, bakFile);
        BufferedWriter writer = null;
        try {
            int lineCounter = 0;
            List<String> fileLines = FileUtils.readLines(confFile);
            FileUtils.deleteQuietly(confFile);
            writer = new BufferedWriter(new FileWriter(confFile, true));
            for (String line : fileLines) {
                lineCounter++;
                writer.append(line);
                writer.newLine();
                if (lineCounter == host.getStartLine() && !fileLines.contains(hostEnable)) {
                    writer.append(str);
                    writer.newLine();
                }
            }
            writer.flush();
        } finally {
            try {  
                if(writer != null) {
                    writer.close();  
                    writer = null;
                }
            } catch (IOException e) {  
                //TODO
            } 
        }
        FileUtils.deleteQuietly(bakFile);
    }
    
    /**
     * Only add the LoadModule xxx_module modules/cloudwise.so to the main configuration file.
     * 
     * If one of the hosts has been turned on. We should load our module in Apache Server.
     * 
     * @param str
     * @param mainFile
     * @throws IOException
     */
    public void turnOnModule(File mainFile) throws IOException {
        File bakFile = new File(mainFile.getAbsoluteFile() + ".bak");
        FileUtils.copyFile(mainFile, bakFile);
        BufferedWriter writer = null;
        try {
            List<String> fileLines = FileUtils.readLines(mainFile);
            FileUtils.deleteQuietly(mainFile);
            writer = new BufferedWriter(new FileWriter(mainFile, true));
            if (fileLines.size() > 0 && !fileLines.get(0).trim().equals(loadModuleStr)) {
                writer.append(loadModuleStr);
                writer.newLine();
            }
            for (String line : fileLines) {
                if (!line.equals(loadModuleStr)) {
                    writer.append(line);
                    writer.newLine();
                }
            }
            writer.flush();
        } finally {
            try {  
                if(writer != null) {
                    writer.close();  
                    writer = null;
                }
            } catch (IOException e) {  
                //TODO
            } 
        }
        FileUtils.deleteQuietly(bakFile);
    }

    public void run() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            enablesHosts.clear();
            String apacheConf = "E:/files/work/collegues/Leon/apache2Conf/apache2.conf";
            File confDir = new File(apacheConf);
            readFile(confDir);
            System.out.println("**************像以前一样，只要已经发送过了，那么就不再入选了******************");
            int num = 0;
            for (ApacheHostInfo info : tmpList) {
                num++;
                System.out.println(mapper.writeValueAsString(info));
                if (info.isRumOn()) {
                    if (!isModuleOn.get()) {
                        isModuleOn.set(true);
                    }
                    if (num == 1) {
                        insertHost(info, hostEnable, new File(info.getFileFullName()));
                        System.out.println(info.getFileFullName() + " > has been inserted. Please Check");
                    }
                }
            }

            if (isModuleOn.get() && !checkModule(confDir)) {
                turnOnModule(confDir);
            }
            
            cacheList.addAll(tmpList);
            tmpList.clear();

            for (String host : enablesHosts) {
                System.out.println("################################");
                System.out.println(host);
            }
            System.out.println("globalServerName is : " + globalServerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

    /**
     * To check whether the module is on in this main configuration file.
     * 
     * @param confDir
     * @throws IOException 
     */
    private boolean checkModule(File confDir) throws IOException {
        List<String> readLines = FileUtils.readLines(confDir);
        if (readLines.contains(loadModuleStr)) {
            return true;
        }
        return false;
    }



    static interface CustomPattern {
        public String doLogic();
    }

    @JsonIgnoreProperties({ "fileFullName", "startLine", "endLine", "rumOn" })
    static class ApacheHostInfo implements WebServerInfo {
        String serverName;
        String vhost;
        String fileFullName;
        int startLine;
        int endLine;
        boolean rumOn;

        public boolean isRumOn() {
            return rumOn;
        }

        public void setRumStatus(boolean rumStatus) {
            this.rumOn = rumStatus;
        }

        public String getFileFullName() {
            return fileFullName;
        }

        public void setFileFullName(String fileFullName) {
            this.fileFullName = fileFullName;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getVhost() {
            return vhost;
        }

        public void setVhost(String vhost) {
            this.vhost = vhost;
        }

        public int getStartLine() {
            return startLine;
        }

        public void setStartLine(int startLine) {
            this.startLine = startLine;
        }

        public int getEndLine() {
            return endLine;
        }

        public void setEndLine(int endLine) {
            this.endLine = endLine;
        }

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((serverName == null) ? 0 : serverName.hashCode());
            result = prime * result + ((vhost == null) ? 0 : vhost.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ApacheHostInfo other = (ApacheHostInfo) obj;
            if (serverName == null) {
                if (other.serverName != null)
                    return false;
            } else if (!serverName.equals(other.serverName))
                return false;
            if (vhost == null) {
                if (other.vhost != null)
                    return false;
            } else if (!vhost.equals(other.vhost))
                return false;
            return true;
        }

        public String toString() {
            return "ApacheHostInfo [serverName=" + serverName + ", vhost="
                    + vhost + ", fileFullName=" + fileFullName + ", startLine="
                    + startLine + ", endLine=" + endLine + ", rumOn="
                    + rumOn + "]";
        }
    }
}