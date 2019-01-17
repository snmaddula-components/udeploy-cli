package com.fedex.udeploy.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fedex.udeploy.app.config.UdeployConfig;

@SpringBootApplication
public class UdeployCliApp {

	public static void main(String[] args) {
		SpringApplication.run(UdeployCliApp.class, args);
	}

	@Bean
	public CommandLineRunner cli(UdeployConfig udeployConfig) {
		return (args) -> System.out.println(udeployConfig);
	}
}
