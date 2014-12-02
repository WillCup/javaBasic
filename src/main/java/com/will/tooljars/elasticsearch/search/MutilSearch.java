package com.will.tooljars.elasticsearch.search;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * The multi search API allows to execute several search requests within the
 * same API. The endpoint for it is _msearch.<br/>
 * <br/>
 * 
 * The response returns a responses array, which includes the search response
 * for each search request matching its order in the original multi search
 * request. If there was a complete failure for that specific search request, an
 * object with error message will be returned in place of the actual search
 * response.
 * 
 * <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-8-4
 */
public class MutilSearch {
    public static void main(String[] args) {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "TSB-QA").build();
        // on startup
        Client client = new TransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress("10.0.1.68", 9300))
                .addTransportAddress(
                        new InetSocketTransportAddress("10.0.1.69", 9300));

        SearchRequestBuilder srb1 = client.prepareSearch()
                .setQuery(QueryBuilders.queryString("TSB-QA")).setSize(1);
        SearchRequestBuilder srb2 = client.prepareSearch()
                .setQuery(QueryBuilders.matchQuery("name", "kimchy"))
                .setSize(1);

        MultiSearchResponse sr = client.prepareMultiSearch().add(srb1)
                .add(srb2).execute().actionGet();

        // You will get all individual responses from
        // MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            System.out.println(response.getHits().getTotalHits());
            nbHits += response.getHits().getTotalHits();
        }
        System.out.println(nbHits);
    }
}
