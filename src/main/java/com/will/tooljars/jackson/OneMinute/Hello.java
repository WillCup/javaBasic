package com.will.tooljars.jackson.OneMinute;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Hello {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MyValue value = mapper.readValue(new File(
                    "src/com/will/tooljars/jackson/data.json"), MyValue.class);
            // or:
            value = mapper.readValue(new URL("http://some.com/api/entry.json"),
                    MyValue.class);
            // or:
            value = mapper.readValue("{\"name\":\"Bob\", \"age\":13}",
                    MyValue.class);

            MyValue myResultObject = new MyValue();
            mapper.writeValue(new File("src/com/will/tooljars/jackson/result.json"),myResultObject);
            // or:
            byte[] jsonBytes = mapper.writeValueAsBytes(myResultObject);
            // or:
            String jsonString = mapper.writeValueAsString(myResultObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
