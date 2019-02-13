package com.fedex.udeploy.app.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() throws Exception {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}

			public void handleError(ClientHttpResponse response) throws IOException {
			}
		});
		return restTemplate;
	}

	@Bean
	public ObjectWriter writer() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.writerWithDefaultPrettyPrinter();
	}

	private static ClientHttpRequestFactory clientHttpRequestFactory()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();
		final HttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext)).build();
		return new HttpComponentsClientHttpRequestFactory() {
			{
				setHttpClient(httpClient);
			}
		};
	}
	
	public static void main(String[] args) throws RestClientException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String response = new RestTemplate(clientHttpRequestFactory()).getForObject("https://localhost:8443/", String.class);
		System.out.println(response);
	}
}
