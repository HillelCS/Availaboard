package com.availaboard.UI.init;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.flow.spring.annotation.EnableVaadin;
/**
 * The entry point of the Spring Boot application.
 */


@EnableVaadin({"com.availaboard.UI"})
@SpringBootApplication(scanBasePackages = {
        "com.availaboard"
})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}