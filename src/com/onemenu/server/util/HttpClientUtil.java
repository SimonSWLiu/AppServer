package com.onemenu.server.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
/**
* httpClient 工具类
* @author file
* @since 2015-1-13 下午9:00:20
* @version 1.0
*/
@SuppressWarnings("deprecation")
public final class HttpClientUtil {
	
	private static final int HttpConnnectTimeout = 10 * 1000;
	private static final int HttpSoTimeout = 10 * 1000;
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
	/**
	 * 根据url生成默认的Httpclient
	 * @param url
	 * @return
	 */
	private static HttpClient createDefault(String url){
		HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
       HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setConnectionTimeout(httpParams, HttpConnnectTimeout);
		HttpConnectionParams.setSoTimeout(httpParams, HttpSoTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		return createDefault(url,httpParams);
	}
	/**
	 * 根据url和header参数生成默认的httpclient
	 * @param url
	 * @param httpParams
	 * @return
	 */
	public static HttpClient createDefault(String url,HttpParams httpParams){
		ClientConnectionManager cm = null;
		if (url.startsWith("https:")) {
			try {
				SSLContext sslContext = SSLContext.getInstance("SSL");
				// set up a TrustManager that trusts everything
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						if(logger.isInfoEnabled())
						logger.info("getAcceptedIssuers =============");
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
						if(logger.isInfoEnabled())
							logger.info("checkClientTrusted =============");
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {
						if(logger.isInfoEnabled())
							logger.info("checkServerTrusted =============");
					}
				} }, new SecureRandom());
				SSLSocketFactory sf = new SSLSocketFactory(sslContext);
				Scheme httpsScheme = new Scheme("https", 443, sf);
				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(httpsScheme);

				// apache HttpClient version >4.2 should use
				// BasicClientConnectionManager
				cm = new BasicClientConnectionManager(schemeRegistry);
			} catch (NoSuchAlgorithmException e) {
				logger.error("", e);
			} catch (KeyManagementException e) {
				logger.error("", e);
			}

		}
		HttpClient client = new DefaultHttpClient(cm,httpParams);
		return client;
	}
	/**
	 * 通过get方式访问url，获取数据
	 * @param url url
	 * @param params 参数
	 * @return
	 */
	public static String doGetMethod(String url,Map<String,String> params){
		
		StringBuilder builder = new StringBuilder(url);
		if(builder.indexOf("?")==-1){
			builder.append("?t=").append(System.currentTimeMillis());
		}else{
			builder.append("&t=").append(System.currentTimeMillis());
		}
		HttpClient client = createDefault(builder.toString());
		if(params!=null&&!params.isEmpty()){
			for(Entry<String,String> entry:params.entrySet()){
				builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());	
			}
		}	
		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(new HttpGet(builder.toString()));
			if(logger.isDebugEnabled())
				logger.debug("statusCode:"+httpResponse.getStatusLine().getStatusCode() + " entity:"+httpResponse.getEntity());
			return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	/**
	 * 通过post方式提交数据，并返回数据
	 * @param url  url
	 * @param params 提交的数据
	 * @return
	 */
	public static String doPostMethod(String url,Map<String,String> params){
		
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		List<NameValuePair> _params = null;
		if(params!=null&&!params.isEmpty()){
			_params = new ArrayList<NameValuePair>(params.size());
			for(Entry<String,String> entry:params.entrySet())
				_params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			
		}
		HttpClient client = createDefault(url);
		try {
			httpPost = new HttpPost(url);
			HttpEntity entity =  new UrlEncodedFormEntity(_params , "UTF-8");
			httpPost.setEntity(entity);
			httpResponse = client.execute(httpPost);
			
			return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	/**
	 * 通过post方式提交数据(String类型)，并返回数据
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPostMethod(String url,String params){
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		HttpClient client = createDefault(url);
		try {
			httpPost = new HttpPost(url);
			HttpEntity entity = new StringEntity(params, "UTF-8");;
			httpPost.setEntity(entity);
			httpResponse = client.execute(httpPost);
			return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 通过get方式访问url，获取数据,带着头参数设置
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String doGetMethod(String url,Map<String,String> params,Map<String,String> header){
		if(header==null||header.isEmpty())
			return doGetMethod(url, params);
		
		StringBuilder builder = new StringBuilder(url);
		if(builder.indexOf("?")==-1){
			builder.append("?t=").append(System.currentTimeMillis());
		}else{
			builder.append("&t=").append(System.currentTimeMillis());
		}
		HttpClient client = createDefault(builder.toString());
		if(params!=null&&!params.isEmpty()){
			for(Entry<String,String> entry:params.entrySet()){
				builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());	
			}
		}	
		HttpResponse httpResponse = null;
		try {
			HttpGet get = new HttpGet(builder.toString());
			for(Entry<String,String> entry:header.entrySet()){
				get.addHeader(entry.getKey(), entry.getValue());
			}
			httpResponse = client.execute(get);
			return EntityUtils.toString(getHttpEntity(httpResponse), "UTF-8");
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
		
	}
	
	/**
	 * 通过post方式提交数据，并返回数据,带着头参数设置
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String doPostMethod(String url,Map<String,String> params,Map<String,String> header){
		if(header==null||header.isEmpty())
			return doPostMethod(url, params);
		
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		List<NameValuePair> _params = null;
		if(params!=null&&!params.isEmpty()){
			_params = new ArrayList<NameValuePair>(params.size());
			for(Entry<String,String> entry:params.entrySet())
				_params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			
		}
		HttpClient client = createDefault(url);
		try {
			httpPost = new HttpPost(url);
			HttpEntity entity =  new UrlEncodedFormEntity(_params , "UTF-8");
			httpPost.setEntity(entity);
			for(Entry<String,String> entry:header.entrySet()){
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
			httpResponse = client.execute(httpPost);
			return EntityUtils.toString(getHttpEntity(httpResponse), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 通过post方式提交数据(String)，并返回数据,带着头参数设置
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String doPostMethod(String url,String params,Map<String,String> header){
		if(header==null||header.isEmpty())
			return doPostMethod(url, params);
		
		
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		HttpClient client = createDefault(url);
		try {
			httpPost = new HttpPost(url);
			HttpEntity entity = new StringEntity(params, "UTF-8");;
			httpPost.setEntity(entity);
			for(Entry<String,String> entry:header.entrySet()){
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
			httpResponse = client.execute(httpPost);
			return EntityUtils.toString(getHttpEntity(httpResponse), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	
	
	private static HttpEntity  getHttpEntity(HttpResponse httpResponse){
		HttpEntity  entity = httpResponse.getEntity();
		Header header = entity.getContentEncoding();
		if(header!=null)
			for(HeaderElement element:header.getElements()){
				if(element.getName().equalsIgnoreCase("gzip"))
					return new GzipDecompressingEntity(entity);
			}
		return entity;
	}
	
	
	
}

