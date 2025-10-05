package com.code4fun.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    
	    // TODO :: allow your trusted frontend(s)
	    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
	    
	    // TODO :: Explicitly list safe methods
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    
	    // TODO :: NEVER use "*" with credentials. Even without credentials, be explicit.
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
	    
	    // TODO :: Disable credentials (since you're using Bearer tokens, not cookies)
	    configuration.setAllowCredentials(false);
	    
	    //  TODO :: cache preflight for 1 hour
	    configuration.setMaxAge(3600L);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    
	    // TODO :: Apply to ALL endpoints
	    source.registerCorsConfiguration("/**", configuration);
	    
	    return source;
	    
	}


}
