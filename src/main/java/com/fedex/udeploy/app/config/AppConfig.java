package com.fedex.udeploy.app.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() throws Exception {
		return new RestTemplate(clientHttpRequestFactory());
	}
	
	private ClientHttpRequestFactory clientHttpRequestFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		final TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;
		final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		final HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext)).build(); 
		return new HttpComponentsClientHttpRequestFactory() {{
			setHttpClient(httpClient);
		}};
	}
}

