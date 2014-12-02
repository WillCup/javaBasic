package com.will.tooljars.jackson;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The very mapping model for domain configuration file.
 * <br/><br/>
 * @author will
 *
 * @2014-9-23
 */
public class AppConfigItem {
    private String app_id;
    private String domain_id;
    @JsonProperty("app_name")
    private String domain_name;
    private int port;
    /**
     * On or off.
     */
    private int switch_status;
    /**
     * To handle different web server domains depending on this property.
     */
    private int service_type;
    /**
     * To got the configuration file directly.
     */
    private String fullFileName;
    
    private String service_qualifier;
    
    public String getService_qualifier() {
        return service_qualifier;
    }

    public void setService_qualifier(String service_qualifier) {
        this.service_qualifier = service_qualifier;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSwitch_status() {
        return switch_status;
    }

    public void setSwitch_status(int switch_status) {
        this.switch_status = switch_status;
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(String domain_id) {
        this.domain_id = domain_id;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }
    
    public String getApacheStr() {
        return domain_name + ":" + port + "|" + domain_id + "|" + app_id;
    }
 }