package com.will.str;

import java.io.File;

import org.apache.commons.io.FileUtils;


public class RegTest {
    public static void main(String[] args) {
        try {
            String appKey = "fuckKey";

            File originalFile = new File("toBeIncluded.txt");
            String originalContent = FileUtils.readFileToString(originalFile);
            originalContent = originalContent.replaceAll("\\{installPath\\}", "");
            originalContent = originalContent.replaceAll("\\{appkey\\}", appKey);
            System.out.println("-------------------------------------------------------------------------");
            System.out.println(originalContent);
            System.out.println("-------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
