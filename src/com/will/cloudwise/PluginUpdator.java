package com.will.cloudwise;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.cloudwise.model.MainInfo;
import com.will.cloudwise.model.PluginConfigModel;

public class PluginUpdator {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        String content = FileUtils.readFileToString(new File("conf/plugin_after_config.txt"));
        PluginConfigModel model = mapper.readValue(content, PluginConfigModel.class);
        System.out.println(mapper.writeValueAsString(model));
        
        String base = FileUtils.readFileToString(new File("conf/base.txt"));
        MainInfo info = mapper.readValue(base, MainInfo.class);
        System.out.println(mapper.writeValueAsString(info));
    }
}
