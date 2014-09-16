package com.will.properties;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties文件的前提是这个文件是在classpath里面的。
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-8-19
 */
public class ReadPropertiesFromFile {
    public static void main(String[] args) {
        try {
            Properties procs = new Properties();
            InputStream resourceAsStream = ReadPropertiesFromFile.class.getResourceAsStream("agent-common.properties");
            procs.load(resourceAsStream);
             System.out.println(procs);
             System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
