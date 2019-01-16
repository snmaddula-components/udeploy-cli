package snmaddula.udeploy.app.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(NON_EMPTY)
public class UDResourceReq {

	private String name;
	private String role;
	private String agent;
	private String parent;
	private String componentTag;
}
