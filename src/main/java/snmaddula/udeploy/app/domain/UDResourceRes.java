package snmaddula.udeploy.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import lombok.Data;

@Data
@JsonInclude(NON_EMPTY)
public class UDResourceRes {

	private String id;
	private String name;
	private String path;
	private boolean active;
	private String description;
	private boolean inheritTeam;
	private boolean hasAgent;
}
