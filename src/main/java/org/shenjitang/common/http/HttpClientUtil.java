package org.shenjitang.common.http;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import java.io.IOException;
import java.util.Map;

/**
 * Httpclient API封装工具类
 *
 * @author fy 2010-05-14
 */
public class HttpClientUtil {

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final int DEFAULT_CONNECTION_TIMEOUT = 50000;// 连接的超时时间

	public static final int DEFAULT_SO_TIMEOUT = 300000;// 读取数据的超时时间

	/**
	 * 创建默认的HttpClient的对象
	 */
	public static HttpClient createDefaultHttpClient() {
		HttpClient client = new HttpClient();
		client.getParams().setVersion(HttpVersion.HTTP_1_1);
		client.getParams().setHttpElementCharset(DEFAULT_CHARSET);
		client.getParams().setContentCharset(DEFAULT_CHARSET);
		HttpConnectionManagerParams managerParams = client
				.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		managerParams.setSoTimeout(DEFAULT_SO_TIMEOUT);
		return client;
	}

	/**
	 * 创建代理的HttpClient
	 */
	public static HttpClient createProxyHttpClient(String host, int port,
												   String username, String password) {
		HttpClient client = createDefaultHttpClient();
		client.getHostConfiguration().setProxy(host, port);
		client.getParams().setAuthenticationPreemptive(true);
		client.getState().setProxyCredentials(
				new AuthScope(host, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(username, password));
		return client;
	}

	/**
	 * 清理HttpClient的有关资源
	 */
	private static void abortHttpClient(HttpMethod method, HttpClient client) {
		if (method != null) {
			method.releaseConnection();
		}
		if (client != null) {
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
	}

	/**
	 * get提交
	 */
	public static String get(HttpClient client, String url,
							 Map<String, String> headerMap) {
		String responseString = "";
		GetMethod get = null;
		try {
			get = new GetMethod(url);
			if (isMapNotEmpty(headerMap)) {
				for (Map.Entry<String, String> map : headerMap.entrySet()) {
					get.addRequestHeader(map.getKey(), map.getValue());
				}
			}
			client.executeMethod(get);
			responseString = get.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortHttpClient(get, client);
		}
		return responseString;
	}

	/**
	 * post提交
	 */
	public static String post(HttpClient client, String url,
							  Map<String, String> headerMap, Map<String, String> paramMap) {
		String responseString = "";
		PostMethod post = null;
		try {
			post = new PostMethod(url);
			if (isMapNotEmpty(headerMap)) {
				for (Map.Entry<String, String> map : headerMap.entrySet()) {
					post.addRequestHeader(map.getKey(), map.getValue());
				}
			}
			if (isMapNotEmpty(paramMap)) {
				post.setRequestBody(getParameterArray(paramMap));
			}
			client.executeMethod(post);
			responseString = post.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortHttpClient(post, client);
		}
		return responseString;
	}

	private static boolean isMapNotEmpty(Map<String, String> map) {
		if (map != null && map.size() > 0) {
			return true;
		}
		return false;
	}

	private static NameValuePair[] getParameterArray(
			Map<String, String> paramMap) {
		NameValuePair[] nameValuePairs = new NameValuePair[paramMap.size()];
		int i = 0;
		for (Map.Entry<String, String> map : paramMap.entrySet()) {
			nameValuePairs[i] = new NameValuePair(map.getKey(), map.getValue());
			i++;
		}
		return nameValuePairs;
	}

	public static void main(String[] args) {
	}


}
