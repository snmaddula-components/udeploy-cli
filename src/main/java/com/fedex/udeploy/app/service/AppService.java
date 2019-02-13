package com.fedex.udeploy.app.service;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fedex.udeploy.app.config.Udeploy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppService {

	private static final String SLASH = "/";
	
	private Udeploy udeploy;
	private ObjectWriter writer;
	private ApplicationContext applicationContext;
	
	private TagService tagService;
	private TeamService teamService;
	private AgentService agentService;
	private ResourceService resourceService;
	private ComponentService componentService;
	private ValidationService validationService;
	private ApplicationService applicationService;
	private EnvironmentService environmentService;
	
	public void createResource() {
		try {
			final String parent = udeploy.getResourceGroup();
			final String appName = udeploy.getAppName();
			final String team = udeploy.getTeam();
			final String componentName = udeploy.getComponentName();
			final String componentDesc = udeploy.getComponentDesc();
			final String componentPath = udeploy.getComponentPath();
			
			applicationService.createApplication(appName, parent);
			componentService.createComponent(componentName, componentDesc, componentPath);
			componentService.addComponentToApp(appName, componentName);
			
			resourceService.createRoot(parent);
			resourceService.createApp(parent, appName);
			
	//		validationService.validateTeam(team);
	//		validationService.validateParent(parent);
			
			udeploy.getDataCenters().forEach(dc -> {
				final String dcName = dc.getName();
				dc.getResourceMap().forEach((level, agents) -> {
					resourceService.createGroup(parent, appName, level);
					agents.forEach(agent -> {
						environmentService.createEnvironment(appName, level);
						agentService.addAgentToEnv(parent, appName, level, agent);
						agentService.addAgent(parent, appName, level, agent);
						teamService.addTeam(agent, team);
						tagService.addTag(parent, appName, level, agent, dcName);
						if(StringUtils.hasText(componentName)) {
							componentService.addComponent(parent, appName, level, agent, componentName);
						}
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