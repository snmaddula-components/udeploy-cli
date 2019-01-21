package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.GET;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidationService {

	private static final String SLASH = "/";
	
	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void validateTeam(String team) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.checkTeamUri(team).toUri(), GET, entity, Object.class);
		} catch (HttpClientErrorException.NotFound ex) {
			System.err.println("TEAM: [ " + team + " ] IS NOT VALID!");
			System.out.println("Exiting..");
			System.exit(-1);
		}
	}
	
	public void validateParent(String parent) {
		if(!parent.startsWith(SLASH)) {
			parent = SLASH + parent;
		}
		if(!parent.endsWith(SLASH)) {
			parent = parent + SLASH;
		}
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.checkParentUri(parent).toUri(), GET, entity, Object.class);
		} catch (HttpClientErrorException.NotFound ex) {
			System.err.println("PARENT RESOURCE: [ " + parent + " ] IS NOT VALID!");
			System.out.println("Exiting..");
			System.exit(-1);
		}
	}
}
