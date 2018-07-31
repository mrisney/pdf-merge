package com.itis.shaca.mvar.pdf;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class MVARMergePDFApplication {

	public static void main(String[] args) {
		SpringApplication.run(MVARMergePDFApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		config.addExposedHeader("Accept");
		config.addExposedHeader("Location");
		config.addExposedHeader("Content-Type");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Configuration
	class SwaggerConfig {
		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(RequestHandlerSelectors.basePackage("com.itis.shaca.mvar.pdf")).paths(PathSelectors.any())
					.build().apiInfo(apiInfo());
		}

		private ApiInfo apiInfo() {
			return new ApiInfo("MVAR Merge PDF service REST API", "REST endpoints for merging Adobe PDF files.",
					"1.0.0", "Terms of service",
					new Contact("Marc Risney", "https://www.itis.com", "marc.risney@gmail.com"), "License of API",
					"https://opensource.org/licenses/MIT", Collections.emptyList());
		}
	}
}