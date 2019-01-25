package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AgentService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void addAgent(String parent, String appName, String level, String agent) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, appName, level, agent), manifest.getBasicAuthHeaders());
		ResponseEntity<String> response =  rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, String.class);
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("AGENT CREATED [ " + agent + " ]");
		}else if(statusCode == 400) {
			System.out.println("AGENT [ " + agent + " ] ALREADY EXISTS");
		} else {
			System.err.println(response.getBody());
		}
	}
}
