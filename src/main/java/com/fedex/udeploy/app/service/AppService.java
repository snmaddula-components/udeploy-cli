package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.config.UdeployConfig;
import com.fedex.udeploy.app.dto.UDResourceReq;
import com.fedex.udeploy.app.dto.UDResourceRes;

import lombok.AllArgsConstructor;

//@Service
@AllArgsConstructor
public class AppService {

	private RestTemplate rt;
	private UdeployConfig udeploy;
	private UDeployManifest manifest;
	private ApplicationContext applicationContext;

	public void createResource() {
		try {
			final String parent = udeploy.getResourceGroup();
			final String appName = udeploy.getAppName();
			final String team = udeploy.getTeam();
			final String component = udeploy.getComponentName();
			udeploy.getDataCenters().forEach(dc -> {
				dc.getResourceMap().forEach((level, agents) -> {
					agents.forEach(agent -> {
						addAgent(parent, appName, level, agent);
						addTeam(agent, team);
						addComponent(parent, appName, level, agent, component);
					});
				});
			});
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			shutdown();
		}
	}

	private void addAgent(String parent, String appName, String level, String agent) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, appName, level, agent), manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
			System.out.println("AGENT CREATED [ " + agent + " ]");
		} catch (HttpClientErrorException.BadRequest ex) {
			System.err.println("AGENT [ " + agent + " ] ALREADY EXISTS");
		}
	}

	private void addTeam(String agent, String team) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.createTeamUri(agent, team).toUri(), PUT, entity, Object.class);
			System.out.println("AGENT: [" + agent + " ] ADDED TO TEAM: [ " + team + " ]");
		} catch (HttpClientErrorException.BadRequest ex) {
			System.err.println(ex.getLocalizedMessage());
		}
	}

	private void addComponent(String parent, String appName, String level, String agent, String component) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, appName, level, agent, component), manifest.getBasicAuthHeaders());
		try {
			rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
			System.out.println("COMPONENT: [" + component + " ] ADDED TO AGENT: [ " + agent + " ]");
		} catch (HttpClientErrorException.BadRequest ex) {
			System.err.println("COMPONENT [ " + component + " ] ALREADY EXISTS FOR AGENT [ " + agent + " ]");
		}
	}
	
	private void shutdown() {
		try {
			((ConfigurableApplicationContext) applicationContext).close();
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}

}
