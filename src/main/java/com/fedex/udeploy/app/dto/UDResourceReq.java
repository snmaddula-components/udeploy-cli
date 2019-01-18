package com.fedex.udeploy.app.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(NON_EMPTY)
public class UDResourceReq {
	
	private static final String SLASH = "/";
	
	public UDResourceReq(String parent, String appName, String level, String agent) {
		this.parent = SLASH + parent + SLASH + appName + SLASH + level;
		this.agent = agent;
		this.role = "";
	}
	
	public UDResourceReq(String parent, String appName, String level, String agent, String component) {
		this.parent = SLASH + parent + SLASH + appName + SLASH + level + SLASH + agent;
		this.role = component;
	}

	private String role;
	private String agent;
	private String parent;
}
