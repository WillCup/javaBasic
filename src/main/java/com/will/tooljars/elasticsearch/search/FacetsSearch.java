package com.will.tooljars.elasticsearch.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;

public class FacetsSearch {
    public static void main(String[] args) {
        Node node = NodeBuilder.nodeBuilder().clusterName("TSB-QA").build();

        SearchResponse sr = node.client()
                                .prepareSearch()
                                .setQuery(QueryBuilders.matchAllQuery())
                                .addFacet(FacetBuilders.termsFacet("f1").field("field"))
                                .addFacet(FacetBuilders.dateHistogramFacet("f2").field("birth")
                                .interval("year")).execute().actionGet();

        // Get your facet results
        TermsFacet f1 = (TermsFacet) sr.getFacets().facetsAsMap().get("f1");
        DateHistogramFacet f2 = (DateHistogramFacet) sr.getFacets()
                .facetsAsMap().get("f2");
    }
}
