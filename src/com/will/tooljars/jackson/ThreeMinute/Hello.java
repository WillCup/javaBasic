package com.will.tooljars.jackson.ThreeMinute;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.will.tooljars.jackson.OneMinute.MyValue;

public class Hello {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
//            String jsonSource = "{\"name\":\"Bob\", \"age\":13}";
//            Map<String, Integer> scoreByName = mapper .readValue(jsonSource, Map.class);
//            List<String> names = mapper.readValue(jsonSource, List.class);

            /**
             * and can obviously write out as well
             */
//            mapper.writeValue(new File("names.json"), names);
//            
//            Map<String, MyValue> results = mapper.readValue(jsonSource,
//                    new TypeReference<Map<String, MyValue>>() { } );
         /**
          * why extra work? Java Type Erasure will prevent type detection otherwise
          *  (note: no extra effort needed for serialization, regardless of generic types)
          */
            
         /**
          *  can be read as generic JsonNode, if it can be Object or Array; or,
          *  if known to be Object, as ObjectNode, if array, ArrayNode etc:
          */
         ObjectNode root = (ObjectNode) mapper.readTree(new File("src/com/will/tooljars/jackson/ThreeMinute/stuff.json"));
         String name = root.get("name").asText();
         int age = root.get("age").asInt();
         /**
          * can modify as well: this adds child Object as property 'other', set property 'type'
          */
         root.with("other").put("type", "student");
         String json = mapper.writeValueAsString(root);
         System.out.println(json);
         /**
          *  with above, we end up with something like as 'json' String:
                  {
                    "name" : "Bob", "age" : 13,
                    "other" : {
                       "type" : "student"
                    {
                  }
          */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
