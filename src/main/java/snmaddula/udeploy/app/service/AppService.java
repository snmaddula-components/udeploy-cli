package snmaddula.udeploy.app.service;

import static org.springframework.http.HttpMethod.PUT;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import snmaddula.udeploy.app.config.UDeployManifest;
import snmaddula.udeploy.app.config.Udeploy;
import snmaddula.udeploy.app.domain.UDResourceReq;
import snmaddula.udeploy.app.domain.UDResourceRes;

@Service
@AllArgsConstructor
public class AppService {

	private RestTemplate rt;
	private UDeployManifest manifest;

	private Udeploy udeploy;

	public void createResource() {

		udeploy.getResourceMap().forEach((level, agents) -> {
			String parent = udeploy.getResourceGroup();
			String component = udeploy.getComponentName();
			agents.forEach(agent -> {
				HttpEntity<UDResourceReq> entity = new HttpEntity<>(new UDResourceReq(parent, level, agent));
				try {
					rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
					System.out.println("AGENT CREATED [ " +agent +" ]");
				}catch(Exception ex) {
					System.err.println(ex.getMessage());
				}
				try { 
					entity = new HttpEntity<>(new UDResourceReq(parent, level, agent, component));
					rt.exchange(manifest.createResourceUri().toUri(), PUT, entity, UDResourceRes.class);
					System.out.println("COMPONENT: ["+component+" ] was added to AGENT: [ " +agent +" ]");
				}catch(Exception ex) {
					System.err.println(ex.getMessage());
				}
			});
		});
	}
}
