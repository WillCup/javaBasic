package com.will.tooljars.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IgnoreTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String str = "{\"setAge\":19,\"name\":\"will\"}";
        Person p = mapper.readValue(str, Person.class); 
        System.out.println(mapper.writeValueAsString(p));
    }
    
}
@JsonIgnoreProperties(ignoreUnknown=true)
class Person {
    @JsonProperty("newAge")
    private int age;
    @JsonIgnore
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}