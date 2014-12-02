package com.will.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * For some file operations.
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-9-29
 */
public abstract class CommonFileUtil {
    /**
     * 通过文件名后缀查找文件
     * 
     * @param dir
     * @param suffix
     * @param recursive
     */
    public synchronized static void getFilesBySuffix(File dir, String suffix,
            boolean recursive, List<File> suffixFileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                // 满足条件就输出
                if (file.getName().endsWith(suffix)) {
                    System.out.println(file.getCanonicalPath());
                    suffixFileList.add(file);
                }
                // 如果是目录,并且使用了迭代
                if (recursive && file.isDirectory()) {
                    getFilesBySuffix(file, suffix, recursive, suffixFileList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历指定目录下的文件。
     * 
     * @param file
     * @param recursive
     * @param fileList
     */
    public synchronized static void getFiles(File file, boolean recursive, List<File> fileList) {
        getFiles(file, recursive, fileList, null);
    }

    /**
     * 文件名后缀过滤
     * 
     * @author Will
     * @created at 2014-8-30 上午11:18:46
     */
    static class SuffixFileFilter implements FilenameFilter {
        String suffix = null;

        public SuffixFileFilter(String suffix) {
            super();
            this.suffix = suffix;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(suffix);
        }
    }
    
    /**
     * 文件名前缀过滤。
     * 
     * @author Will
     * @created at 2014-8-30 上午11:19:02
     */
    static class PrefixFileFilter implements FilenameFilter {
        String prefix = null;

        public PrefixFileFilter(String prefix) {
            super();
            this.prefix = prefix;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(prefix);
        }
    }
    
    /**
     * 遍历指定目录下的文件, 找到文件名符合正則表達式的文件。
     * 
     * @param file
     * @param recursive
     * @param fileList
     */
    public synchronized static void getFiles(File file, boolean recursive, List<File> fileList, String regex) {
        try {
            if (regex != null) {
                if (Pattern.matches(regex, file.getAbsolutePath())) {
                    fileList.add(file);
                    System.err.println(file.getAbsolutePath());
                }
            } else {
                fileList.add(file);
                System.err.println(file.getAbsolutePath());
            }
            // 如果是目录
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                // 遍历子目录
                for (File f : listFiles) {
                    if (f.isDirectory()) {
                        for (File inf : f.listFiles()) {
                            getFiles(inf, true, fileList);
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
