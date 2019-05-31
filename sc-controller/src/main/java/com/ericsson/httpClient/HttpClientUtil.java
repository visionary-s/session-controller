package com.ericsson.httpClient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http client tool.
 * @author shibiyun
 *
 */
public class HttpClientUtil {
	// Log record
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static CloseableHttpClient httpClient = null;
	
	// Timeout for getting available connection from connection pool.
	static final int connectionRequestTimeout = 5000;
	
	// Timeout until connection is established.
	static final int connectTimeout = 5000;
	
	// Timeout to receive data.
	static final int socketTimeout = 10000;
	
	// Max number of connections allowed across all routes.
	static final int maxTotal = 500;
	 
	// Max number of connections allowed for one route.
	static final int maxPerRoute = 100;
	
	// Max retry count.
	static final int retryTime = 3;
	
	/**
	 * Get httpClient connection.
	 * @return
	 */
	public static CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			synchronized (HttpClientUtil.class) {
				if (httpClient == null) {
					httpClient = init();
				}
			}
		}
		return httpClient;
	}
	
	/**
	 * Initialize connection pool.
	 * @return
	 */
	public static CloseableHttpClient init() {
		// Setup connection pool.
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		cm.setMaxTotal(maxTotal);
		cm.setDefaultMaxPerRoute(maxPerRoute);
		
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= retryTime) {	// Exceed retry limit
					logger.error("connect date: " + new Date());
                    logger.error("connection failed, exceed retry limit");
					return false;
				}
				if (exception instanceof InterruptedIOException) {	// Timeout
					logger.error("connect date: " + new Date());
                    logger.error("connection timeout");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
					return false;
				}
				if (exception instanceof UnknownHostException) {	// Unreachable unknown host
					logger.error("connect date: " + new Date());
                    logger.error("unknown host");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
					return false;
				}
				if (exception instanceof ConnectException) {	// Connection refused
					logger.error("connect date: " + new Date());
                    logger.error("connection is refused");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
					return false;
				}
				if (exception instanceof SSLException) {	// SSL handshake exception
					logger.error("connect date: " + new Date());
                    logger.error("SSL connection error");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
					return false;
				}
				if (exception instanceof NoHttpResponseException) {	// If the connection is lost, retry
					logger.error("connect date: " + new Date());
                    logger.error("there is no response from server");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
					return true;
				}
				
				HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                
				// Retry if the request is considered idempotent
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                	return true;
                }
                return false;
			}
		};
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		return HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).setDefaultRequestConfig(requestConfig).build();

	}
}
