package com.fedex.udeploy.app.service;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fedex.udeploy.app.config.Udeploy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppService {

	private Udeploy udeploy;
	private ObjectWriter writer;
	private ApplicationContext applicationContext;
	
	private TeamService teamService;
	private AgentService agentService;
	private ComponentService componentService;
	
	public void createResource() {
		try {
			final String parent = udeploy.getResourceGroup();
			final String appName = udeploy.getAppName();
			final String team = udeploy.getTeam();
			final String component = udeploy.getComponentName();
			udeploy.getDataCenters().forEach(dc -> {
				dc.getResourceMap().forEach((level, agents) -> {
					agents.forEach(agent -> {
						agentService.addAgent(parent, appName, level, agent);
						teamService.addTeam(agent, team);
						componentService.addComponent(parent, appName, level, agent, component);
					});
				});
			});
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			shutdown();
		}
	}


	@PostConstruct
	public void logConfig() throws JsonProcessingException {
		System.out.println("\n==================================== UDEPLOY CONFIG =====================================");
		System.out.println("\n" + writer.writeValueAsString(udeploy));
		System.out.println("\n=========================================================================================\n");
	}
	
	private void shutdown() {
		try {
			((ConfigurableApplicationContext) applicationContext).close();
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}

}