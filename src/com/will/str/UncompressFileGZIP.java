package com.will.str;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
 
/**
 * -----------------------------------------------------------------------------
 * Used to provide an example of uncompressing a file in the GZIP Format.
 *
 * @version 1.0
 * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
 * @author  http://www.idevelopment.info
 * -----------------------------------------------------------------------------
 */
 
public class UncompressFileGZIP {

    public static final String LINE_SEPERATOR = System.getProperty("line.separator");
    /**
     * Uncompress the incoming file.
     * @param inFileName Name of the file to be uncompressed
     */
    public static String doUncompressFiles(String inFileName) {
 
        StringBuilder out = new StringBuilder();
        try {
            if (!getExtension(inFileName).equalsIgnoreCase("gzip")) {
                System.err.println("File name must have extension of \".gz\"");
                System.exit(1);
            }
 
            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            try {
                in = new GZIPInputStream(new FileInputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }
 
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            System.out.println("Open the output file.");
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                out.append(line);
//                out.append(LINE_SEPERATOR);
//            }
            InputStreamReader reader = new InputStreamReader(in);
            char[] tempchars = new char[30];
            int charread = 0;
            while ((charread = reader.read(tempchars)) != -1) {
                for (int i = 0; i < charread; i++) {
                        System.out.print(tempchars[i]);
                }
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return out.toString();
    }
    
    /**
     * Uncompress the incoming file.
     * @param inFileName Name of the file to be uncompressed
     */
    public static void doUncompressFile(String inFileName) {
 
        try {
 
            if (!getExtension(inFileName).equalsIgnoreCase("gzip")) {
                System.err.println("File name must have extension of \".gz\"");
                System.exit(1);
            }
 
            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            try {
                in = new GZIPInputStream(new FileInputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }
 
            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                System.err.println("Could not write to file. " + outFileName);
                System.exit(1);
            }
 
            System.out.println("Transfering bytes from compressed file to the output file.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
 
            System.out.println("Closing the file and stream");
            in.close();
            out.close();
        
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
 
    }
 
    /**
     * Used to extract and return the extension of a given file.
     * @param f Incoming file to get the extension of
     * @return <code>String</code> representing the extension of the incoming
     *         file.
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
 
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }       
        return ext;
    }
 
    /**
     * Used to extract the filename without its extension.
     * @param f Incoming file to get the filename
     * @return <code>String</code> representing the filename without its
     *         extension.
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');
 
        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }       
        return fname;
    }
 
    /**
     * Sole entry point to the class and application.
     * @param args Array of String arguments.
     */
    public static void main(String[] args) {
        System.out.println("ddf" + LINE_SEPERATOR + "");
            System.out.println(doUncompressFiles("/var/will/david/javaagent-1412846676150.gzip"));
    }
 
}