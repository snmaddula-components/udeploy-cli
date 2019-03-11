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
public class ComponentService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public void addComponent(String parent, String appName, String level, String agent, String component) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, appName, level, agent, component), manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("COMPONENT: [" + component + " ] ADDED TO AGENT: [ " + agent + " ]");
		}else if(statusCode == 400) {
			System.out.println("COMPONENT [ " + component + " ] ALREADY EXISTS FOR AGENT [ " + agent + " ]");
		}else {
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
	}

	public void createComponent(String component, String description, String fileSystemPath) {
		Map<String, Object> requestMap = new HashMap<String, Object>() {{
			put("defaultVersionType", "FULL");
			put("importAutomatically", false);
			put("useVfs", true);
			put("name", component);
			put("description", description);
			put("sourceConfigPlugin", "File System (Versioned)");
			put("properties", new HashMap<String, Object>(){{
				put("FileSystemVersionedComponentProperties/basePath", fileSystemPath);
				put("FileSystemVersionedComponentProperties/saveFileExecuteBits", false);
			}});
		}};
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(manifest.createComponentUri().toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("CREATED COMPONENT: [" + component + " ]");
		}else if(statusCode == 400) {
			System.out.println("COMPONENT [ " + component + " ] WAS ALREADY CREATED");
		}else {
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
	}
	
	public void addComponentToApp(String appName, String component) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(manifest.addComponentToAppUri(appName, component).toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("COMPONENT: [" + component + " ] ADDED TO APPLICATION: [ " + appName + " ]");
		}else if(statusCode == 400) {
			System.out.println("COMPONENT [ " + component + " ] ALREADY EXISTS FOR APPLICATION [ " + appName + " ]");
		}else {
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
		}
	}

	public void addComponentProperty(String component, String name, String value) {
		HttpEntity<UDResourceReq> entity = new HttpEntity<>(manifest.getBasicAuthHeaders());
		ResponseEntity<String> response = rt.exchange(manifest.addComponentPropertyUri(component, name, value).toUri(), PUT, entity, String.class);
		
		int statusCode = response.getStatusCodeValue();
		if(statusCode == 200) {
			System.out.println("PROPERTY: [" + name + "=" + value + " ] ADDED TO COMPONENT: [ " + component + " ]");
		}
			if(StringUtils.hasText(response.getBody())) System.err.println(response.getBody());
	}
}
