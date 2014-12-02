package com.will.tooljars.jackson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class TestListMap {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("/root/workspace/will/apm-agent/product/0.2/service/conf/instance/readable/apache.json");
            String content = FileUtils.readFileToString(file);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
