package snmaddula.udeploy.app.service;

import org.springframework.http.HttpEntity;
import static org.springframework.http.HttpMethod.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import snmaddula.udeploy.app.config.UDeployManifest;
import snmaddula.udeploy.app.domain.UDResourceReq;
import snmaddula.udeploy.app.domain.UDResourceRes;

@Service
@AllArgsConstructor
public class AppService {

	private RestTemplate rt;
	private UDeployManifest manifest;
	
	public UDResourceRes createResource(UDResourceReq resourceReq) {
		return rt.
				exchange(manifest.createResourceUri().toUri(), PUT, new HttpEntity(resourceReq), UDResourceRes.class)
				.getBody();
	}
}
