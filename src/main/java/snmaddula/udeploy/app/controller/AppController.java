package snmaddula.udeploy.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import snmaddula.udeploy.app.transformer.AppDataTransformer;

@RestController
public class AppController {
	
	@Autowired
	private AppDataTransformer transformer;
	
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, String env) throws Exception {
		
		transformer.it(file.getInputStream());
		
		return "DONE!";
	}
}
