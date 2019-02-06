package com.fedex.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import java.util.HashMap;
import java.util.Map;

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
public class ApplicationService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void createApplication(String appName, String parent) {
		Map<String, Object> requestMap = new HashMap<String, Object>() {{
			put("name",  appName);
			put("description", parent);
			put("enforceCompleteSnapshots", false);
			put("notificationScheme", "Default Notification Scheme");
		}};
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(manifest.createApplicationUri().toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("CREATED APPLICATION: [" + appName + " ]");
		}else if(statusCode == 400) {
			System.out.println("APPLICATION [ " + appName + " ] WAS ALREADY CREATED");
		}else {
			if(StringUtils.hasText(response.getBody())) if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
	}
}
