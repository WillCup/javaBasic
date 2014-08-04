package com.will.elasticsearch.search;

import java.util.Collection;
import java.util.Date;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * 
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-8-4
 */
public class MonitorInfoFromES {

    public static void main(String[] args) {
        try {
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "TSB-QA").build();

            // 尝试加入不存在的IP，会花一段时间去尝试链接，不成功后，使用已有连接进行查询。
            // 有些不足的是，IP是否有效并没有在控制台给出直接的Info.
            // 另外，在此测试的时候，没有体验到使用一个address和使用多个address有什么明显的区别。
            Client client = new TransportClient(settings)
                                .addTransportAddress(new InetSocketTransportAddress("10.0.1.68", 9300))
                                .addTransportAddress(new InetSocketTransportAddress("10.0.1.69", 9300))
//                                .addTransportAddress(new InetSocketTransportAddress("10.0.1.22", 9300)) 
                                ;
            SearchRequestBuilder requestBuilder = null;

            String target_type = "1";
            String target_id = "10";
            String service_type = "2";
            
            int resp_time = 1000;
            requestBuilder = client.prepareSearch("agenttopic_201408")
                                   .setTypes("type_107")
                                   .setScroll(new TimeValue(60000))
                                   ;
            Date now  = new Date();
            long currentTime = now.getTime()/1000l;
            long fromTime = currentTime - 1*60 * 60;
            
            requestBuilder.setQuery(
                    filteredQuery(
                            matchAllQuery(),
                            andFilter(
                                termFilter("service_type", 107)
//                                ,rangeFilter("collTime").gte(fromTime)
                            )
                        )
                    );
            requestBuilder.setSize(1);
            requestBuilder.addAggregation(
                              terms("targets").field("target_id")
                              .subAggregation(
                                      AggregationBuilders.avg("RespStatus").field("totalCount")
                              )
                          );
            
            System.out.println("-----"+requestBuilder);
            String querySource = requestBuilder.toString();
            querySource = querySource.replaceAll("\\s{2,}", "");
            querySource = querySource.replaceAll("\r\n", "");

            System.err.println("source--->'" + querySource + "'");
            SearchResponse searchResponse = requestBuilder.execute().actionGet();
            System.out.println("Time token : " + searchResponse.getTook());
            System.out.println("ScrollId is : " + searchResponse.getScrollId());
            System.out.println("Total shards : " + searchResponse.getTotalShards());
            System.out.println("Failed shards : " + searchResponse.getFailedShards());
            System.out.println("Successful shards : " + searchResponse.getSuccessfulShards());
            System.out.println("Aggregations : " + searchResponse.getAggregations());
            System.out.println("Facets : " + searchResponse.getFacets());
            System.out.println("Headers : " + searchResponse.getHeaders());
            System.out.println("Hits : " + searchResponse.getHits());
            System.out.println("Search failures : " + searchResponse.getShardFailures());
            System.out.println("Suggests : " + searchResponse.getSuggest());
            System.out.println("Took " + searchResponse.getTook());
            System.out.println(searchResponse);

            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
