package com.onemenu.server.util;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;




/**
 * 
 * 
 * @author linhang
 * @date [2012-10-26]
 */
public class FbUtils {
	
    private static Logger LOGGER = Logger.getLogger(FbUtils.class);

    private static final int SUCCESS = 200;

    public static String FACEBOOK_PERSONAL_INFO =
            "https://graph.facebook.com/v2.2/me?access_token=";

    @SuppressWarnings({"resource", "deprecation"})
    public static String getFacebookTokenContent(String accessToken) throws Exception {

        String result = new String();

        // HttpGet httpGet = new HttpGet(FACEBOOK_PERSONAL_INFO + accessToken);
        // HttpClient client = getHttpClient();
        // HttpResponse httpResponse = client.execute(httpGet);
        // if (httpResponse.getStatusLine().getStatusCode() == SUCCESS) {
        // result = NetUtils.unzipDataAndLog(httpResponse.getEntity().getContent());
        // LOGGER.info(result);
        // }

        // 创建HttpClient实例
        HttpClient httpClient = new DefaultHttpClient();
        // 创建Get方法实例
        HttpGet httpGets = new HttpGet(FACEBOOK_PERSONAL_INFO + accessToken);
        HttpResponse response = httpClient.execute(httpGets);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream is = entity.getContent();
            result = NetUtils.unzipDataAndLog(is);
            // Do not need the rest
            httpGets.abort();
        }

        return result;
    }

    private static HttpClient getHttpClient() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }

}
