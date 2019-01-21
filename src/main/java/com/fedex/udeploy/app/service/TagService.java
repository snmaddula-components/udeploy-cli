package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;
import com.fedex.udeploy.app.dto.UDResourceRes;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void addTag(String parent, String agent, String dcName) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.createTagUri(parent, agent, dcName).toUri(), PUT, entity, UDResourceRes.class);
			System.out.println("TAG: [" + dcName + " ] ADDED TO AGENT: [ " + agent + " ]");
		} catch (HttpClientErrorException.BadRequest ex) {
			System.err.println(ex.getMessage());
		}
	}
}
