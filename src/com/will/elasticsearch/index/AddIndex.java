package com.will.elasticsearch.index;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The index API allows one to index a typed JSON document into a specific index
 * and make it searchable. 1. Generate json document; 2. Index document; 3.
 * Operating threading. By default, operationThreaded is set to true which means
 * the operation is executed on a different thread. <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-8-4
 */
public class AddIndex {
    /**
     *
     * <br/>
     * @param args
     */
    public static void main(String[] args) {
        try {
            // You have to set the cluster name if you use one different than "elasticsearch"
            Settings settings = ImmutableSettings.settingsBuilder()
                                    .put("cluster.name", "TSB-QA").build(); 
            // on startup
            Client client = new TransportClient(settings)
                                .addTransportAddress(new InetSocketTransportAddress("10.0.1.68", 9300))
                                .addTransportAddress(new InetSocketTransportAddress("10.0.1.69", 9300));
            
            IndexResponse response = client
                    .prepareIndex("twitter", "tweet")
                    .setSource(
//                         getJsonFromXContentFactoryJsonBuilder()
                           getJsonFromObjectMapper()
                     )
                    .execute()
                    .actionGet();

            // Index name
            String _index = response.getIndex();
            // Type name
            String _type = response.getType();
            // Document ID (generated or not)
            String _id = response.getId();
            // Version (if it's the first time you index this document, you will
            // get: 1)
            long _version = response.getVersion();
            
            // on shutdown
            client.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getJsonFromObjectMapper()
            throws JsonProcessingException {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "kimchy");
        json.put("postDate", new Date());
        json.put("message", "trying out Elasticsearch");

        // instance a json mapper
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        // generate json String
        return mapper.writeValueAsString(json);
    }

    private static XContentBuilder getJsonFromXContentFactoryJsonBuilder() throws IOException {
        return XContentFactory.jsonBuilder()
        .startObject()
        .field("user", "kimchy")
        .field("postDate", new Date())
        .field("message", "trying out Elasticsearch")
        .endObject();
    }
}
