package com.will.elasticsearch.clients;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * The TransportClient connects remotely to an elasticsearch cluster using the
 * transport module. It does not join the cluster, but simply gets one or more
 * initial transport addresses and communicates with them in round robin fashion
 * on each action (though most actions will probably be "two hop" operations).
 * 
 * <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-8-4
 */
public class TransportClientTest {
    public static void main(String[] args) {
        // You have to set the cluster name if you use one different than "elasticsearch"
        Settings settings = ImmutableSettings.settingsBuilder()
                                .put("cluster.name", "TSB-QA").build(); 
        // on startup
        Client client = new TransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress("10.0.1.68", 9300))
                            .addTransportAddress(new InetSocketTransportAddress("10.0.1.69", 9300));
//        search(client);
//        smallestSearch(client);
        scroll(client);
        // on shutdown
        client.close();
    }

    private static void scroll(Client client) {
        QueryBuilder qb = termQuery("multi", "test");

        SearchResponse scrollResp = client.prepareSearch("test")
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(100).execute().actionGet(); //100 hits per shard will be returned for each scroll
        //Scroll until no hits are returned
        while (true) {
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(600000)).execute().actionGet();
            for (SearchHit hit : scrollResp.getHits()) {
                //Handle the hit...
                System.out.println(hit.getSourceAsString());
            }
            //Break condition: No hits are returned
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }
    }

    /**
     * all parameters are optional. Here is the smallest search call you can write
     * 
     * <br/>
     * @param client
     */
    private static void smallestSearch(Client client) {
        // MatchAll on the whole cluster with all default options
        SearchResponse response = client.prepareSearch().execute().actionGet();
    }

    /**
     * The search API allows one to execute a search query and get back search hits that match the query. 
     * It can be executed across one or more indices and across one or more types. 
     * The query can either be provided using the query Java API or the filter Java API. 
     * The body of the search request is built using the SearchSourceBuilder.
     *
     * <br/>
     * @param client
     */
    private static void search(Client client) {
        SearchResponse response = client.prepareSearch("index1", "index2")
                .setTypes("type1", "type2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi", "test"))             // Query
                .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
    }
}
