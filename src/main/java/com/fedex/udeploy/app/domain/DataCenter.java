package com.fedex.udeploy.app.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class DataCenter {
	
	private String name;
	private Map<String, List<String>> resourceMap;
	
	public DataCenter(String name) {
		this.name = name;
		resourceMap = new LinkedHashMap<>();
	}
	
	public void addLevel(String level) {
		if(!resourceMap.containsKey(level)) {
			getResourceMap().put(level, new ArrayList<>());
		}
	}
	
	public void addAgent(String level, String agent) {
		if(resourceMap.containsKey(level)) {
			getResourceMap().get(level).add(agent);
		}
	}
	
}
