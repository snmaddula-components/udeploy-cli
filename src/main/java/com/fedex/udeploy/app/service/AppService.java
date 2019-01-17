package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.config.UdeployConfig;
import com.fedex.udeploy.app.domain.UDResourceReq;
import com.fedex.udeploy.app.domain.UDResourceRes;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppService {

	private RestTemplate rt;
	private UdeployConfig udeploy;
	private UDeployManifest manifest;

	public void createResource() {
		HttpHeaders authHeaders = manifest.getBasicAuthHeaders();
		udeploy.getResourceMap().forEach((level, agents) -> {
			final String parent = udeploy.getResourceGroup();
			final String component = udeploy.getComponentName();
			agents.forEach(agent -> {
				HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, level, agent), authHeaders);
				try {
					rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
					System.out.println("AGENT CREATED [ " + agent + " ]");
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
				try {
					entity = new HttpEntity<>(new UDResourceReq(parent, level, agent, component));
					rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
					System.out.println("COMPONENT: [" + component + " ] was added to AGENT: [ " + agent + " ]");
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			});
		});
	}
}
