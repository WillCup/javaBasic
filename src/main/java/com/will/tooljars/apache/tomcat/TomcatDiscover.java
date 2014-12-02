package com.will.tooljars.apache.tomcat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.will.io.CommonFileUtil;
import com.will.tooljars.xml.XMLReader;

public class TomcatDiscover {
    static class TomcatApp {
        private String domain;
        private int port;
        public String getDomain() {
            return domain;
        }
        public void setDomain(String domain) {
            this.domain = domain;
        }
        public int getPort() {
            return port;
        }
        public void setPort(int port) {
            this.port = port;
        }
    }
    
    public static void main(String[] args) {

        try {
//            String path = "E:/files/learning/tomcat/apache-tomcat-7.0.55-src/output/build";
//            String catalinaHome = "E:/apps/Dev/apache-tomcat-6.0.39";
            
            String suffix = "server.xml";
            String catalinaHome = "/usr/local/apache-tomcat-7.0.54";
            File dir = new File(catalinaHome );

            List<File> fileList = new ArrayList<File>();
            List<TomcatApp> apps = new ArrayList<TomcatApp>();
            
//        getFiles(dir, true, fileList);

            CommonFileUtil.getFilesBySuffix(dir, suffix, true, fileList);
            
            for (File file : fileList) {
                XMLReader reader = new XMLReader(file);
                NodeList nodeList = reader.getNodeList("Server/Service/Engine/Host");
                System.out.println(nodeList.getLength());
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node item = nodeList.item(i);
                    NamedNodeMap attributes = item.getAttributes();
                    Node namedItem = attributes.getNamedItem("appBase");
                    String appHome = namedItem.getTextContent();
                    File app = new File(catalinaHome + File.separator + appHome);
                    for (File f : app.listFiles()) {
                        if (f.isDirectory()) {
                            TomcatApp e = new TomcatApp();
                            apps.add(e );
                            System.out.println(appHome + " --> " + f.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
