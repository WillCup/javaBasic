package com.will.cloudwise.model;

import java.util.List;

public class PluginConfigModel {
    
    private String ptql;
    private String pluginName;
    private String className;
    private List<ServiceItem> serviceGroup;
    private List<Metric> metrics;
    private List<Metric> subMetrics;
    
    public String getPtql() {
        return ptql;
    }
    public void setPtql(String ptql) {
        this.ptql = ptql;
    }
    public String getPluginName() {
        return pluginName;
    }
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public List<ServiceItem> getServiceGroup() {
        return serviceGroup;
    }
    public void setServiceGroup(List<ServiceItem> serviceGroup) {
        this.serviceGroup = serviceGroup;
    }
    public List<Metric> getMetrics() {
        return metrics;
    }
    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }
    public List<Metric> getSubMetrics() {
        return subMetrics;
    }
    public void setSubMetrics(List<Metric> subMetrics) {
        this.subMetrics = subMetrics;
    }
}
