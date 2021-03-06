package snmaddula.udeploy.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import snmaddula.udeploy.app.config.UdeployConfig;

@SpringBootApplication
public class UdeployCliApp {

	public static void main(String[] args) {
		SpringApplication.run(UdeployCliApp.class, args);
	}
	
	@Bean
	public CommandLineRunner cli(UdeployConfig ud) {
		return (args) -> System.out.println(ud);
	}

}

