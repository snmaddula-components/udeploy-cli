package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeamService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void addTeam(String agent, String team) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.createTeamUri(agent, team).toUri(), PUT, entity, Object.class);
			System.out.println("AGENT: [" + agent + " ] ADDED TO TEAM: [ " + team + " ]");
		} catch (HttpClientErrorException.BadRequest ex) {
			System.err.println(ex.getLocalizedMessage());
		}
	}
}
