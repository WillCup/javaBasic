package com.will.tooljars.elasticsearch.clients;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

/**
 * The benefit of using the Client is the fact that operations are automatically
 * routed to the node(s) the operations need to be executed on, without
 * performing a "double hop". For example, the index operation will
 * automatically be executed on the shard that it will end up existing at. <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-8-4
 */
public class NodeClient {

    public static void main(String[] args) {
        // simplest();
        // setNodeName();
//        aboutData();
//        localNode();
    }

    /**
     * Another common usage is to start the Node and use the Client in unit/integration tests. 
     * In such a case, we would like to start a "local" Node (with a "local" discovery and transport).
     *  Again, this is just a matter of a simple setting when starting the Node. Note, 
     *  "local" here means local on the JVM (well, actually class loader) level, meaning that two local
     *   servers started within the same JVM will discover themselves and form a cluster.
     *
     * <br/>
     */
    private static void localNode() {
        Node node = nodeBuilder().local(true).node();
        Client client = node.client();

        // on shutdown

        node.close();
    }

    /**
     * When you start a Node, the most important decision is whether it should
     * hold data or not. In other words, should indices and shards be allocated
     * to it. Many times we would like to have the clients just be clients,
     * without shards being allocated to them. This is simple to configure by
     * setting either node.data setting to false or node.client to true (the
     * NodeBuilder respective helper methods on it) <br/>
     */
    private static void aboutData() {
        Node node = nodeBuilder().client(true).node();
        Client client = node.client();
        node.close();
    }

    private static void setNodeName() {
        Node node = nodeBuilder().clusterName("yourclustername").node();
        Client client = node.client();
    }

    /**
     * When you start a Node, it joins an elasticsearch cluster.
     * 
     * <br/>
     */
    private static void simplest() {
        // on startup
        Node node = nodeBuilder().node();
        Client client = node.client();

        // on shutdown
        node.close();
    }
}
