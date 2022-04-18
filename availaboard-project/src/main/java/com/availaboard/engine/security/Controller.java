package com.availaboard.engine.security;

import java.util.Map;
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpSession; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.MediaType; 
import org.springframework.security.authentication.BadCredentialsException; 
import org.springframework.security.authentication.LockedException; 
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam;

import com.availaboard.engine.resource.User; 

public class Controller {         
   @Autowired private SecurityUserDetailsService userDetailsManager; 
   @Autowired
   private PasswordEncoder passwordEncoder; 
   
   @GetMapping("/") 
   public String index() { 
      return "index"; 
   }
   @GetMapping("/login") 
   public String login(HttpServletRequest request, HttpSession session) { 
      session.setAttribute(
         "error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION")
      ); 
      return "login"; 
   } 
   @GetMapping("/register") 
   public String register() {  
      return "register"; 
   } 
   @PostMapping(
      value = "/register", 
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = { 
      MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
   )
   public void addUser(@RequestParam Map<String, String> body) {
      User user = new User(); user.setUsername(body.get("username")); 
      user.setPassword(passwordEncoder.encode(body.get("password"))); 
      user.setAccountNonLocked(true); userDetailsManager.createUser(user); 
   }
   private String getErrorMessage(HttpServletRequest request, String key) {
      Exception exception = (Exception) request.getSession().getAttribute(key); 
      String error = ""; 
      if (exception instanceof BadCredentialsException) { 
         error = "Invalid username and password!"; 
      } else if (exception instanceof LockedException) { 
         error = exception.getMessage(); 
      } else { 
         error = "Invalid username and password!"; 
      } 
      return error;
   }
}
