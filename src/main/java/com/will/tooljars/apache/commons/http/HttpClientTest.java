package com.will.tooljars.apache.commons.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {
    public static void main(String[] args) {
        try {
            testPost();
//            testGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testGet() throws IOException, ClientProtocolException {
        String url = "http://www.toushibao.com/proxy_discover/getServices";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
    }

    private static void testPost() throws UnsupportedEncodingException,
            IOException, ClientProtocolException {
        String url = "http://www.toushibao.com/proxy_discover/postServices/764618a4379ae9a32e6abf5af5de1019";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity("This is a test"));
        HttpResponse response = client.execute(post);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
