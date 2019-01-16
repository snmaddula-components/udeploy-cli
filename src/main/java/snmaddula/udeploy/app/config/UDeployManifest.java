package snmaddula.udeploy.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
	
	
}
