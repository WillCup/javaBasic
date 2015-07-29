package com.will.solr;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.google.common.collect.Lists;

public class SolrTest extends TestCase {
    public void testAdd() throws Exception {
        HttpSolrServer server = new HttpSolrServer("http://10.10.2.195:8983/solr");
        for(int i=0;i<1000;++i) {
          SolrInputDocument doc = new SolrInputDocument();
          doc.addField("cat", "book");
          doc.addField("id", "book-" + i);
          doc.addField("name", "The Legend of the Hobbit part " + i);
          server.add(doc);
          if(i%100==0) server.commit();  // periodically flush
        }
        server.commit();
        server.close();
    }
    
    public void testCloudAdd() throws Exception {
        CloudSolrClient server = new CloudSolrClient("10.10.2.195:9983");
        server.setDefaultCollection("gettingstarted");
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField( "id", "1234");
        doc.addField( "name", "A lovely summer holiday");
        doc.addField( "sex", "boy");
        server.add(doc);
        server.commit();
        server.close();
    }
    
    public void testCloudAddPojo() throws Exception {
        CloudSolrServer server = new CloudSolrServer("10.10.2.195:9983");
        server.setDefaultCollection("gettingstarted");
        Item item = new Item();
        item.id = "onePojo";
        item.categories =  new String[] { "aaa", "bbb", "ccc" };
        item.features = Lists.newArrayList("the properties cannot be null");
        server.addBean(item);
        server.commit();
        server.close();
    }
    
    public void testCloudQuery() throws Exception {

        CloudSolrServer server = new CloudSolrServer("10.10.2.195:9983");
        server.setDefaultCollection("gettingstarted");
        SolrQuery query = new SolrQuery();
        query.setQuery( "title:PlainTextEntityProcessor (Solr 5.2.1 API)" );
        query.set("rows", 3);
        query.set("fl", "*,score");
//        query.addSortField( "name", SolrQuery.ORDER.asc );
        QueryResponse rsp = server.query( query );
        SolrDocumentList list = rsp.getResults();
        for (int i = 0; i < list.size(); i++) {
            SolrDocument doc = list.get(i);
            System.out.println("\t >>>>>>>> " + doc);
        }
        server.close();
    }
    
    public void testCloudDelete() throws Exception {
        CloudSolrServer server = new CloudSolrServer("10.10.2.195:9983");
        server.setDefaultCollection("gettingstarted");
        server.deleteByQuery( "id:1234" );
        server.close();
    }
    
    public class Item {
        @Field
        String id;

        @Field("cat")
        String[] categories;

        @Field
        ArrayList<String> features;

      }
}
