package snmaddula.udeploy.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties("manifest")
public class UDeployManifest {

	private String baseUrl;
	private String username;
	private String password;
	
	private String infoUri;
	private String createUri;

	public UriComponents createResourceUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createUri).build();
//				.buildAndExpand("junit-5");
	}

}
