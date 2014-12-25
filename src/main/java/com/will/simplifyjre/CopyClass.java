package com.will.simplifyjre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * http://blog.csdn.net/xiaoping8411/article/details/6973887
 * 
 * 执行命令：
 * @echo off
    C:/Java/jdk1.6.0_16/bin/java -jar  -classpath lib/*.jar; -verbose:class printSoft.jar >> class.txt
    pause
    
    
 * 结果文件内容：
 *  [Loaded java.lang.Math from shared objects file]
    [Loaded java.nio.charset.Charset$3 from C:\Java\jdk1.6.0_16\jre\lib\rt.jar]
    [Opened C:\Java\jdk1.6.0_16\jre\lib\charsets.jar]
    [Loaded sun.nio.cs.AbstractCharsetProvider from C:\Java\jdk1.6.0_16\jre\lib\rt.jar]
    [Loaded sun.nio.cs.ext.ExtendedCharsets from C:\Java\jdk1.6.0_16\jre\lib\charsets.jar]
    [Loaded java.lang.Class$1 from shared objects file]
    [Loaded sun.reflect.ReflectionFactory$1 from shared objects file]
    [Loaded sun.reflect.NativeConstructorAccessorImpl from shared objects file]
 * 
 * 
 * 
 * 由于class.txt每行都是形同: [Loaded java.lang.System from shared objects file]的一串字符,修改文本以方便获取类完整名
 * java.lang.System,从而获得类似类路径java/lang/System的一串字符,方便后继编写类拷贝程序.
 * 
 * 修改方法:
        1. 查找并替换[Loaded 为空,达到删除[Loaded 的目的.
        2. 使用任意一个具有正则表达式查找替换功能的文本编辑器,查找并替换 from.*为空,达到删除 from及其后面的字符串的目的.
        3. 查找并替换.为/
        4. 删除以[Opened 开头的行.
        5. 删除程序中System.out.println的输出行.
 * 
 * <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-12-24
 */
public class CopyClass {
    private String source = "C:\\Users\\lzp\\Desktop\\printSoft\\jre6\\lib\\"; // 类源目录
    private String dest = "C:\\Users\\lzp\\Desktop\\printSoft\\jre6\\lib\\"; // 类拷贝目的目录
    String[] jarArr = new String[] { "rt", "charsets" };

    /***
     * 
     * @param source
     *            类源目录
     * @param dest
     *            类拷贝目的目录
     * @param jarArr
     *            需要的提取的jar文件
     */
    public CopyClass(String source, String dest, String[] jarArr) {
        this.source = source;
        this.dest = dest;
        this.jarArr = jarArr;
    }

    public static void main(String[] args) {
        String[] jarArr = new String[] { "rt", "charsets" };
        CopyClass obj = new CopyClass(
                "C:\\Users\\lzp\\Desktop\\printSoft\\jre6\\lib\\",
                "C:\\Users\\lzp\\Desktop\\printSoft\\jre6\\lib\\", jarArr);
        obj.readAndCopy("C:\\Users\\lzp\\Desktop\\printSoft\\class.txt");
    }

    /***
     * @param logName
     *            提取class明细
     */
    public void readAndCopy(String logName) {
        int count = 0; // 用于记录成功拷贝的类数
        try {
            FileInputStream fi = new FileInputStream(logName);
            InputStreamReader ir = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(ir);

            String string = br.readLine();
            while (string != null) {
                if (copyClass(string) == true)
                    count++;
                else
                    System.out.println("ERROR " + count + ": " + string);
                string = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
        System.out.println("count: " + count);
    }

    /***
     * 从原jar路径提取相应的类到目标路径，如将java/lang/CharSequence类从rt目录提取到rt1目录
     * 
     * @param string
     *            提取类的全路径
     * @return
     * @throws IOException
     */
    public boolean copyClass(String string) throws IOException {
        String classDir = string.substring(0, string.lastIndexOf("/"));
        String className = string.substring(string.lastIndexOf("/") + 1,
                string.length())
                + ".class";

        boolean result = false;

        for (String jar : jarArr) {
            File srcFile = new File(source + "/" + jar + "/" + classDir + "/"
                    + className);
            if (!srcFile.exists()) {
                continue;
            }

            byte buf[] = new byte[256];
            FileInputStream fin = new FileInputStream(srcFile);

            /* 目标目录不存在,创建 */
            File destDir = new File(dest + "/" + jar + "1/" + classDir);
            if (!destDir.exists())
                destDir.mkdirs();

            File destFile = new File(destDir + "/" + className);
            FileOutputStream fout = new FileOutputStream(destFile);
            int len = 0;
            while ((len = fin.read(buf)) != -1) {
                fout.write(buf, 0, len);
            }
            fout.flush();
            result = true;
            break;
        }
        return result;
    }
}
