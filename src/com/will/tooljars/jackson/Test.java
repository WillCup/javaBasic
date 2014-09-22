package com.will.tooljars.jackson;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String content = "{\"target_id\": null,\"account_id\": 0, \"mac\" : \"sdffdsfsdf\"}";
        Map val = mapper.readValue(content, Map.class);
        
        Object targetId = val.get("target_id");
        String strTargetId = String.valueOf(targetId);
        System.out.println(targetId.getClass());
        System.out.println(strTargetId.getClass());
        System.out.println(val.get("account_id").getClass());
    }
}
