package com.will.cloudwise.model;

import java.util.Map;

public class ServiceItem {
    private String id;
    private String active;
    private int frequency;
    private int probability;
    private Map serviceConfig;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getActive() {
        return active;
    }
    public void setActive(String active) {
        this.active = active;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public int getProbability() {
        return probability;
    }
    public void setProbability(int probability) {
        this.probability = probability;
    }
    public Map getServiceConfig() {
        return serviceConfig;
    }
    public void setServiceConfig(Map serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
