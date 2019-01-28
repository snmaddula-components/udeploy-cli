package com.fedex.udeploy.app.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(NON_EMPTY)
public class UDResourceReq {
	
	private static final String SLASH = "/";
	
	private String role;
	private String agent;
	private String parent;

	public UDResourceReq(String parent, String appName) {
		this.parent = SLASH + parent + SLASH + appName;
	}
	
	public UDResourceReq(String parent, String appName, String level) {
		this(parent, appName);
		this.parent = this.parent + SLASH + level;
	}
	
	public UDResourceReq(String parent, String appName, String level, String agent) {
		this(parent, appName);
		this.parent = this.parent + SLASH + level;
		this.agent = agent;
	}
	
	public UDResourceReq(String parent, String appName, String level, String agent, String component) {
		this(parent, appName, level, agent);
		this.parent = this.parent + SLASH + agent;
		this.role = component;
	}

}
