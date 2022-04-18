package com.availaboard.UI.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.flow.spring.annotation.EnableVaadin;

/**
 * The entry point of the Spring Boot application.
 */

@ComponentScan({ "com.availaboard" })
@EntityScan("com.availaboard")
@EnableJpaRepositories("com.availaboard")
@EnableVaadin({ "com.availaboard.UI" })
@SpringBootApplication(scanBasePackages = { "com.availaboard" }, exclude = { SecurityAutoConfiguration.class })
public class Application extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}