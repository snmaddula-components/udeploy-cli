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
	private String createTeamUri;

	public UriComponents createResourceUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createUri).build();
	}

	public UriComponents createTeamUri(String agent, String team) {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createTeamUri)
				.queryParam("agent", agent)
				.queryParam("team", team)
				.build();
	}

	public HttpHeaders getBasicAuthHeaders() {
		return new HttpHeaders() {{
			add("Authorization", "Basic " + new String(encodeBase64((username + ":" + password).getBytes())));
		}};
	}

}
