package com.fedex.udeploy.app.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("udeploy")
public class UdeployConfig {

	private String team;
	private String appName;
	private String componentName;
	private String resourceGroup;
	private Map<String, List<String>> resourceMap;

}
