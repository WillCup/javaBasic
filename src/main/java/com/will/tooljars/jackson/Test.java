package com.will.tooljars.jackson;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        
        Map<String, String> configs = new HashMap<String, String>();
        String config = "{\"test\":true,\"number\":123,\"string\":\"Hello World\"}";
        
        
        
        String content = "{\"target_id\": null,\"account_id\": 0, \"mac\" : \"sdffdsfsdf\"}";
        Map val = mapper.readValue(content, Map.class);
        
        Object targetId = val.get("target_id");
        String strTargetId = String.valueOf(targetId);
        System.out.println(targetId.getClass());
        System.out.println(strTargetId.getClass());
        System.out.println(val.get("account_id").getClass());
    }
}
