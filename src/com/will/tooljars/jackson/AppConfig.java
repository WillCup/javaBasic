package com.will.tooljars.jackson;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppConfig {
    @JsonProperty("host_id")
    private String host_key;
    private int account_id;
    private Map<String, List<AppConfigItem>> app_config_list;
    
    public String getHost_key() {
        return host_key;
    }
    public void setHost_key(String host_key) {
        this.host_key = host_key;
    }
    public int getAccount_id() {
        return account_id;
    }
    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }
    public Map<String, List<AppConfigItem>> getApp_config_list() {
        return app_config_list;
    }
    public void setApp_config_list(
            Map<String, List<AppConfigItem>> app_config_list) {
        this.app_config_list = app_config_list;
    }
}