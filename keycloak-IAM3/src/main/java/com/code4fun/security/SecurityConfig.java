package com.code4fun.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	 private final AuditLoggingFilter auditLoggingFilter;
	 
	    public SecurityConfig() {
	        this.auditLoggingFilter = new AuditLoggingFilter();
			System.out.println("✅ SecurityConfig loaded!");
	    }
 	 
		@Bean
		SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
		    JwtAuthenticationConverter converter = jwtAuthenticationConverter();

	        // ENFORCE ROLE-BASED ACCESS CONTROL (RBAC): 
			// ONLY USERS WITH "ADMIN" AUTHORITY CAN ACCESS /USERS ENDPOINT
	        // ALL OTHER REQUESTS MUST BE AUTHENTICATED
	        http.authorizeHttpRequests(authz -> authz
	                .requestMatchers("/users/**").hasAuthority("ADMIN")
	                .requestMatchers("/public", "/health", "/actuator/info", "/swagger-ui.html","/debug-auth").permitAll()
	                .anyRequest().authenticated()
	        );

	        // STATELESS SESSION MANAGEMENT: DO NOT CREATE OR USE HTTP SESSIONS (RECOMMENDED FOR JWT/APIs)
	        http.sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        );

	        // ENABLE CORS USING CUSTOM CORS CONFIGURATION (DEFINED IN CorsConfig)
	        http.cors(Customizer.withDefaults());

	        // CONFIGURE JWT-BASED AUTHENTICATION USING OAUTH2 RESOURCE SERVER
	        http.oauth2ResourceServer(oauth2 -> oauth2
					.jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
	        );

	        // APPLY ESSENTIAL SECURITY HEADERS
	        http.headers(headers -> headers
	        		.contentSecurityPolicy(csp -> csp
	                        .policyDirectives("default-src 'self'; frame-ancestors 'none'")
	                )
	                .httpStrictTransportSecurity(hsts -> hsts
	                        .includeSubDomains(true)
	                        .maxAgeInSeconds(31536000)
	                )
	                .frameOptions(frame -> frame.deny())
	                .contentTypeOptions(Customizer.withDefaults())
	                .crossOriginEmbedderPolicy(coep -> coep
	                        .policy(CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP)
	                )
	                .crossOriginOpenerPolicy(coop -> coop
	                        .policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN)
	                )
	                .crossOriginResourcePolicy(corp -> corp
	                        .policy(CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN)
	                )
	        );

	        // ADD AUDIT LOGGING FILTER AFTER JWT AUTHENTICATION
	        http.addFilterAfter(auditLoggingFilter, BearerTokenAuthenticationFilter.class);

	        return http.build();
	        
	    }
		
		@Bean
		@Primary
		JwtAuthenticationConverter jwtAuthenticationConverter() {
			JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
			grantedAuthoritiesConverter.setAuthorityPrefix("");

			JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
			jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
			return jwtAuthenticationConverter;
		}

}