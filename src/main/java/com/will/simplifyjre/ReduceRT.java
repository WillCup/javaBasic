package com.will.simplifyjre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class ReduceRT {
    // 文件拷贝
    public static boolean copy(String file1, String file2) {
        try // must try and catch,otherwide will compile error
        {
            // instance the File as file_in and file_out
            java.io.File file_in = new java.io.File(file1);
            java.io.File file_out = new java.io.File(file2);
            FileInputStream in1 = new FileInputStream(file_in);
            FileOutputStream out1 = new FileOutputStream(file_out);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in1.read(bytes)) != -1)
                out1.write(bytes, 0, c);
            in1.close();
            out1.close();
            return (true); // if success then return true
        } catch (Exception e) {
            System.out.println("Error!");
            return (false); // if fail then return false
        }
    }

    // 读取路径,copy
    public static int dealClass(String needfile, String sdir, String odir)
            throws IOException {
        int sn = 0; // 成功个数

        File usedclass = new File(needfile);
        if (usedclass.canRead()) {
            String line = null;
            LineNumberReader reader = new LineNumberReader(
                    new InputStreamReader(new FileInputStream(usedclass),
                            "UTF-8"));
            while ((line = reader.readLine()) != null) {
                line = lineProc(line);// [Loaded java.lang.Object from
                                      // C:\Program Files\Java\jre7\lib\rt.jar]
                                      // ==> java.lang.Object
                if (line == null)
                    continue;
                line = line.trim();
                int dirpos = line.lastIndexOf("/");
                if (dirpos > 0) {
                    String dir = odir + line.substring(0, dirpos);
                    line = line.substring(0, dirpos);
                    File fdir = new File(dir);
                    if (!fdir.exists())
                        fdir.mkdirs();
                    String sf = sdir + line + ".class";
                    String of = odir + line + ".class";
                    boolean copy_ok = copy(sf.trim(), of.trim());
                    if (copy_ok)
                        sn++;
                    else {
                        System.out.println(line);
                    }
                }
            }
        }
        return sn;
    }

    // [Loaded java.lang.Object from C:\Program Files\Java\jre7\lib\rt.jar] ==>
    // java.lang.Object
    public static String lineProc(String line) {
        String ostr = "ERROR";
        String str[];
        String stro[];
        String oostr = null;
        String patternStr = "\\[Loaded.*from.*rt\\.jar\\]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher isFilteredProtocol = pattern.matcher(line);
        if (isFilteredProtocol.matches()) {
            str = line.split(" ");
            ostr = str[1];
            stro = ostr.split("\\.");
            oostr = "";
            for (String s : stro) {
                oostr += s;
                oostr += "/";
            }
        }
        return oostr;
    }

    public static void main(String[] args) {
        try {
            String needfile = "/usr/local/will/test/smart_agent2/smart_agent/java_result";
            System.out.println(FileUtils.readFileToString(new File(needfile)));
            System.exit(0);
            String sdir = "./rt/";
            String odir = "./rt1/";
            int sn = dealClass(needfile, sdir, odir);
            System.out.print(sn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // File usedclass = new File(needfile);
        // try{
        // if (usedclass.canRead()) {
        // String line = null;
        // String newLine = null;
        // LineNumberReader reader = new LineNumberReader(
        // new InputStreamReader(new FileInputStream(usedclass),
        // "UTF-8"));
        // while ((line = reader.readLine()) != null) {
        // newLine=lineProc(line);//[Loaded java.lang.Object from C:\Program
        // Files\Java\jre7\lib\rt.jar] ==> java.lang.Object
        // if(newLine !="ERROR"){
        // System.out.println(newLine);
        // }
        // // else {
        // // System.out.println(line);
        // // }
        // }
        // }
        // }
        // catch(Exception e){
        // e.printStackTrace();
        // }
    }
}