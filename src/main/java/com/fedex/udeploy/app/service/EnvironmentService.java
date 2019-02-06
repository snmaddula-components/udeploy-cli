package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fedex.udeploy.app.config.UDeployManifest;
import com.fedex.udeploy.app.dto.UDResourceReq;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnvironmentService {
	
	private RestTemplate rt;
	private UDeployManifest manifest;

	private HashMap<String, String> envColors = new HashMap<String, String>() {
		{
			put("L0", "%23404041");
			put("L1", "%23DD731C"); // cinnamon
			put("L2", "%2317AF4B"); // mountain meadow
			put("L3", "%23D9182D"); // crimson
			put("L4", "%23FFCF01"); // tangerine yellow
			put("L5", "%23838329"); // olive
			put("L6", "%2300B2EF"); // bright cerulean
			put("L7", "%2383827F"); // battleship gray
			put("L8", "%23EF98AA"); // mauvelous
			put("LP", "%237F1C7D");
		}
	};
	
	public void createEnvironment(String appName, String level) {
		String color = envColors.get(level);
		if (level == null) {
			level = "";
		}
		
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(
				manifest.createEnvironmentUri(appName, level, color).toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("ENVIRONMENT : [" + level + " ] CREATED FOR APP : [ " + appName + " ]");
		}else if(statusCode == 400) {
			System.out.println("ENVIRONMENT [ " + level + " ] ALREADY EXISTS FOR APP [ " + appName + " ]");
		}else {
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
		
	}
}
