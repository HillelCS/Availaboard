package com.availaboard.engine.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

	
	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    // Not using Spring CSRF here to be able to use plain HTML for the login page
	    http.csrf().disable() 

	            // Register our CustomRequestCache that saves unauthorized access attempts, so
	            // the user is redirected after login.
	            .requestCache().requestCache(new CustomRequestCache()) 

	            // Restrict access to our application.
	            .and().authorizeRequests()

	            // Allow all flow internal requests.
	            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() 

	            // Allow all requests by logged in users.
	            .anyRequest().authenticated() 

	            // Configure the login page.
	            .and().formLogin().loginPage(LOGIN_URL).permitAll() 
	            .loginProcessingUrl(LOGIN_PROCESSING_URL) 
	            .failureUrl(LOGIN_FAILURE_URL)

	            // Configure logout
	            .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}
}