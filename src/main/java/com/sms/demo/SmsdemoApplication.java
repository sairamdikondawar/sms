package com.sms.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableAutoConfiguration
@OpenAPIDefinition(info = @Info(title = "Sms API", version = "2.0", description = "Sms API's Information"))
@SecurityScheme(name = "smsapi", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@EnableCaching
public class SmsdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsdemoApplication.class, args);
	}

}
