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
	private String checkTeamUri;
	private String createTagUri;
	private String createTeamUri;
	private String checkResourceUri;
	private String createResourceUri;
	private String createComponentUri;
	private String createApplicationUri;
	private String createEnvironmentUri;
	private String addComponentToAppUri;
	private String addBaseResourceToEnvUri;

	public UriComponents createResourceUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createResourceUri).build();
	}

	public UriComponents createComponentUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createComponentUri).build();
	}
	
	public UriComponents createEnvironmentUri(String appName, String level, String color) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createEnvironmentUri)
				.queryParam("application", appName)
				.queryParam("name", level)
				.queryParam("color", color)
				.queryParam("description", "")
				.queryParam("requireApprovals", false)
				.build();
	}

	public UriComponents createApplicationUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createApplicationUri).build();
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

	public UriComponents addComponentToAppUri(String application, String component) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(addComponentToAppUri)
				.queryParam("application", application)
				.queryParam("component", component)
				.build();
	}

	public UriComponents addBaseResourceToEnvUri(String application, String environment, String resource) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(addBaseResourceToEnvUri)
				.queryParam("application", application)
				.queryParam("environment", environment)
				.queryParam("resource", resource)
				.build();
	}
	
	public HttpHeaders getBasicAuthHeaders() {
		return new HttpHeaders() {{
			add("Authorization", "Basic " + new String(encodeBase64((username + ":" + password).getBytes())));
		}};
	}

}
