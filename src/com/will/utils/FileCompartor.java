package com.will.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileCompartor {
    public static void main(String[] args) throws IOException {
        File pomFile = new File("C:\\Users\\Will\\Desktop\\poms");
        String poms = FileUtils.readFileToString(pomFile);
        File gradFiles = new File("C:\\Users\\Will\\Desktop\\grads");
        String grads = FileUtils.readFileToString(gradFiles);
        BufferedReader reader = new BufferedReader(new FileReader(pomFile));
        String tmp ="";
        List<String> pomList = new ArrayList<String>();
        while ((tmp = reader.readLine()) != null) {
            pomList.add(tmp.substring(tmp.lastIndexOf("\\") + 1));
        }
        reader = new BufferedReader(new FileReader(gradFiles));
        tmp ="";
        List<String> gradList = new ArrayList<String>();
        while ((tmp = reader.readLine()) != null) {
            gradList.add(tmp.substring(tmp.lastIndexOf("\\") + 1));
        }
        
        System.out.println("----------------pom > grad---------------------");
        Collections.sort(pomList);
        for (String s : pomList) {
            if (!gradList.contains(s)) {
                System.out.println(s);
            }
        }
        
        System.out.println();
        System.out.println();
        System.out.println("---------------- grad-  > pom --------------------");
        Collections.sort(gradList);
        for (String s : gradList) {
            if (!pomList.contains(s)) {
                System.out.println(s);
            }
        }
//        System.out.println(poms);
//        poms = poms.replaceAll("[*\\*.jar]", "*.jar");
//        System.out.println(poms);
    }
}
