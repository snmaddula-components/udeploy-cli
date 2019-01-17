package snmaddula.udeploy.app.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(NON_EMPTY)
public class UDResourceReq {
	
	private static final String SLASH = "/";
	
	public UDResourceReq(String parent, String level, String agent) {
		this.parent = SLASH + parent + SLASH + level;
		this.agent = agent;
	}
	
	public UDResourceReq(String parent, String level, String agent, String component) {
		this.parent = SLASH + parent + SLASH + level + SLASH + agent;
		this.role = component;
	}

	private String name;
	private String role;
	private String agent;
	private String parent;
	private String componentTag;
}
