package com.fedex.udeploy.app.domain;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataCenter {
	
	private String name;
	private Map<String, List<String>> resourceMap;
	
	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
}
