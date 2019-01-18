package com.fedex.udeploy.app.config;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fedex.udeploy.app.domain.DataCenter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("udeploy")
public class UdeployConfig {

	private String team;
	private String appName;
	private String componentName;
	private String resourceGroup;
	private List<DataCenter> dataCenters;

	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
}
