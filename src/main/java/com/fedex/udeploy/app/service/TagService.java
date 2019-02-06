package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

	private static final String SLASH = "/";
	
	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void addTag(String parent, String appName, String level, String agent, String dcName) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		parent = SLASH + parent + SLASH + appName + SLASH + level + SLASH + agent;
		ResponseEntity<String> response = rt.exchange(manifest.createTagUri(parent, dcName).toUri(), PUT, entity, String.class);
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("TAG: [" + dcName + " ] ADDED TO AGENT: [ " + agent + " ]");
		}else {
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
	}
}
