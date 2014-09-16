package com.will.io;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * ＜p＞Description: Download some specified url resources to local specified location. BTW, proxy is supported.＜/p＞
 **/
public class SaveFile {
    public final static boolean DEBUG = true; 
    private static int BUFFER_SIZE = 8096;
    private Vector downloadFiles = new Vector();
    private Vector saveToLocalFiles = new Vector();

    public SaveFile() {
    }

    public void resetList() {
        downloadFiles.clear();
        saveToLocalFiles.clear();
    }

    /**
     * Add a url to download list, and saving file name to file list.
     */
    public void addItem(String url, String filename) {
        downloadFiles.add(url);
        saveToLocalFiles.add(filename);
    }

    /**
     * Download the resources in the downloadList.
     */
    public void downLoadByList() {
        String url = null;
        String filename = null;
        for (int i = 0; i < downloadFiles.size(); i++) {
            url = (String) downloadFiles.get(i);
            filename = (String) saveToLocalFiles.get(i);
            try {
                saveToFile(url, filename);
            } catch (IOException err) {
                if (DEBUG) {
                    System.out.println("资源[" + url + "]下载失败!!!");
                }
            }
        }
        if (DEBUG) {
            System.out.println("下载完成!!!");
        }
    }

    /**
     * 
     * 
     * @param destUrl
     * @param fileName
     * @throws Exception
     */
    public void saveToFile(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        try {
            URL url = null;
            byte[] buf = new byte[BUFFER_SIZE];
            int size = 0;
            // create connection
            url = new URL(destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            // connect to the remote resources.
            httpUrl.connect();
            // get the inputStream
            bis = new BufferedInputStream(httpUrl.getInputStream());
            // create a local file OutputStream.
            fos = new FileOutputStream(fileName);
            if (this.DEBUG)
                System.out.println("正在获取链接[" + destUrl + "]的内容.../n将其保存为文件["
                        + fileName + "]");
            // save file
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } finally {
            fos.close();
            bis.close();
            httpUrl.disconnect();
        }
    }

    /**
     * 将HTTP资源另存为文件
     * 
     * @param destUrl
     *            String
     * @param fileName
     *            String
     * @throws Exception
     */
    public void saveToFile2(String destUrl, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        // create a connection
        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
        // String authString = "username" + ":" + "password";
        String authString = "50301" + ":" + "88888888";
        String auth = "Basic "
                + new sun.misc.BASE64Encoder().encode(authString.getBytes());
        httpUrl.setRequestProperty("Proxy-Authorization", auth);
        // 连接指定的资源
        httpUrl.connect();
        // 获取网络输入流
        bis = new BufferedInputStream(httpUrl.getInputStream());
        // 建立文件
        fos = new FileOutputStream(fileName);
        if (this.DEBUG)
            System.out.println("正在获取链接[" + destUrl + "]的内容.../n将其保存为文件["
                    + fileName + "]");
        // 保存文件
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);
        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

    /**
     * Set up your proxy server.
     * 
     * @param proxy
     * @param proxyPort
     */
    public void setProxyServer(String proxy, String proxyPort) {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", proxy);
        System.getProperties().put("proxyPort", proxyPort);
    }

    public void setAuthenticator(String uid, String pwd) {
        Authenticator.setDefault(new MyAuthenticator());
    }

    public static void main(String argv[]) {
        SaveFile save = new SaveFile();
        try {
            /**
             * add to download list
             */
             save.addItem("http://fdfs.xmcdn.com/group5/M01/0B/6F/wKgDtlN0IBawqt-gAANUfn6i5YQ573_common_medium.jpg","./111111111.jpg");//
             save.addItem("https://jersey.java.net/images/jersey_logo.png","./2222222222.png");
             save.addItem("http://img4.duitang.com/uploads/item/201207/19/20120719132725_UkzCN.thumb.600_0.jpeg","./luffy.jpeg");
            /**
             * begin to download
             */
             save.downLoadByList();
//            save.saveToFile(
//                    "http://fdfs.xmcdn.com/group5/M01/0B/6F/wKgDtlN0IBawqt-gAANUfn6i5YQ573_common_medium.jpg",
//                    "./dsdfdsdf.jpg");
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }
}