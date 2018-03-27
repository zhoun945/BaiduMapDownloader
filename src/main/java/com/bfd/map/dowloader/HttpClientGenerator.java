package com.bfd.map.dowloader;

import com.bfd.map.util.ConfigUtil;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class HttpClientGenerator {

  private PoolingHttpClientConnectionManager connectionManager;
  private Integer threadSize;

  public HttpClientGenerator() {
    threadSize = ConfigUtil.getInstance().getInt("download.thread");
    Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.INSTANCE).build();
    connectionManager = new PoolingHttpClientConnectionManager(reg);
    connectionManager.setMaxTotal(threadSize * 10);
    connectionManager.setDefaultMaxPerRoute(threadSize);
  }

  public CloseableHttpClient getClient() {
    return generateClient();
  }

  private CloseableHttpClient generateClient() {
    HttpClientBuilder httpClientBuilder = HttpClients.custom();
    httpClientBuilder.setConnectionManager(connectionManager);
    SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000)
        .setSoKeepAlive(true).setTcpNoDelay(true).build();
    httpClientBuilder.setDefaultSocketConfig(socketConfig);
    connectionManager.setDefaultSocketConfig(socketConfig);
    httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
    return httpClientBuilder.build();
  }

}
