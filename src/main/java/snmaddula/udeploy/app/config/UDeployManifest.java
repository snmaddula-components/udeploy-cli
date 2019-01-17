package snmaddula.udeploy.app.config;

import static org.apache.commons.codec.binary.Base64.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@SuppressWarnings("serial")
@ConfigurationProperties("manifest")
public class UDeployManifest {

	private String baseUrl;
	private String username;
	private String password;

	private String infoUri;
	private String createUri;

	public UriComponents createResourceUri() {
		return UriComponentsBuilder.fromHttpUrl(baseUrl).path(createUri).build();
	}

	public HttpHeaders getBasicAuthHeaders() {
		return new HttpHeaders() {{
			add("Authorization", "Basic " + new String(encodeBase64((username + ":" + password).getBytes())));
		}};
	}

}
