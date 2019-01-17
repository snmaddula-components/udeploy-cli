package com.fedex.udeploy.app.config;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Setter
@Controller
@EnableSwagger2
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {

	private String title;
    private String version;
    private String description;
    private String basePackage;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    
    @Bean
    public Docket swaggerApi() {
    	return new Docket(SWAGGER_2)
                    .select()
                    .apis(basePackage(Objects.toString(basePackage, "")))
                    .build()
                    .useDefaultResponseMessages(false)
                    .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
    	return new ApiInfoBuilder()
                    .title(title)
                    .version(version)
                    .description(description)
                    .contact(new Contact(contactName, contactUrl, contactEmail))
                    .build();
    }
    
	@GetMapping("/")
	public String swaggerUI() {
		return "redirect:swagger-ui.html";
	}

}
