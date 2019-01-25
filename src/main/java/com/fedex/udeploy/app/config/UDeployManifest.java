package com.fedex.udeploy.app.config;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@SuppressWarnings("serial")
@ConfigurationProperties("manifest")
public class UDeployManifest {

	private String baseUrl;
	private String username;
	private String password;

	private String infoUri;
	private String createUri;
	private String checkTeamUri;
	private String createTagUri;
	private String createTeamUri;
	private String checkResourceUri;

	public UriComponents createResourceUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createUri).build();
	}

	public UriComponents checkTeamUri(String team) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(checkTeamUri)
				.queryParam("team", team)
				.build();
	}
	
	public UriComponents createTeamUri(String agent, String team) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createTeamUri)
				.queryParam("agent", agent)
				.queryParam("team", team)
				.build();
	}

	public UriComponents createTagUri(String resource, String dcName) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createTagUri)
				.queryParam("resource", resource)
				.queryParam("tag", dcName)
				.build();
	}
	
	public UriComponents checkParentUri(String parent) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(checkResourceUri)
				.queryParam("parent", parent)
				.build();
	}

	public HttpHeaders getBasicAuthHeaders() {
		return new HttpHeaders() {{
			add("Authorization", "Basic " + new String(encodeBase64((username + ":" + password).getBytes())));
		}};
	}

}
