package com.will.collection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SetIntGetStr {
    public static void main(String[] args) {
        try {
            String content = "{\"config\" : {\"port\" : true}}";
            ObjectMapper mapper = new ObjectMapper();
            
            Port port = mapper.readValue(content, Port.class);
            System.out.println(port);
            System.out.println(port.getConfig().get("port").getClass().getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    static class Port {

        private Map<String, String> config = new HashMap<String, String>();

        public Map<String, String> getConfig() {
            return config;
        }

        public void setConfig(Map<String, String> config) {
            this.config = config;
        }

        @Override
        public String toString() {
            return "Port [config=" + config + "]";
        }
        
    }
}
