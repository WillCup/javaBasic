package com.will.tooljars.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestList {
    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
//        simpleOne(mapper);
        String result = "{\"host_id\":\"142619606906800\",\"account_id\":1,\"app_config_list\":{\"apache1\":[{\"app_name\":\"www.will.com\",\"port\":80,\"switch_status\":1,\"service_qualifier\":\"67fecd6f4b3bc3bbd12d8188d3cb6911\",\"service_type\":201,\"domain_id\":\"8546867820034080\",\"app_id\":\"8546867820034080\"}],\"apache2\":[{\"app_name\":\"www.will.com\",\"port\":80,\"switch_status\":1,\"service_qualifier\":\"67fecd6f4b3bc3bbd12d8188d3cb6911\",\"service_type\":201,\"domain_id\":\"8546867820034080\",\"app_id\":\"8546867820034080\"}]}}";
        
        AppConfig cfg = mapper.readValue(result,
                AppConfig.class);
        System.out.println(cfg.getApp_config_list());
//        Map<String, Object> newConfig = mapper.readValue(result, Map.class);
//        if(newConfig!=null){
//            Object appList = newConfig.get("app_config_list");
//            if(appList instanceof Map){
//                Map appMap = (Map)appList; 
//                Iterator appIter = appMap.keySet().iterator();
//                while(appIter.hasNext()){
//                    String servName = (String)appIter.next();
//                    List servList =  (List)appMap.get(servName);
//                    System.out.println(servName+" -> "+servList);
//                }
//                
//            }
//        }
    }

    /**
     * 
     * 
     * <br/>
     * @param mapper
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    private static void simpleOne(ObjectMapper mapper) throws IOException,
            JsonParseException, JsonMappingException, JsonProcessingException {
        Map<String, String> configs = new HashMap<String, String>();
        String config1 = "{\"test\":true,\"number\":123,\"str\":\"Hello World\"}";
        String config2 = "{\"test\":true,\"number\":124,\"str\":\"Hello World\"}";
        Obj readV = mapper.readValue(config1, Obj.class);
        System.out.println(readV);
        configs.put("1", config1);
        configs.put("2", config2);
        String result = mapper.writeValueAsString(configs.values());
//        ArrayList<String> readValue = mapper.readValue(result, ArrayList.class);
        String[] readValue = mapper.readValue(result, String[].class);
        for (String lt : readValue) {
            System.out.println(lt);
            Obj readValue2 = mapper.readValue(lt, Obj.class);
            System.out.println(readValue2);
            System.out.println(lt);
        }
    }
    
}
