package com.code4fun.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuditLoggingFilter extends OncePerRequestFilter {

 private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

 @Override
 protected void doFilterInternal(
         HttpServletRequest request,
         HttpServletResponse response,
         FilterChain filterChain) throws ServletException, IOException {

     String method = request.getMethod();
     if (List.of("POST", "PUT", "PATCH", "DELETE").contains(method)) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String userId = "anonymous";

         if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
             userId = auth.getName(); // or extract from JWT claims if needed
         }

         auditLog.info("USER_ACTION | user={} | method={} | uri={} | ip={}",
                 userId,
                 method,
                 request.getRequestURI(),
                 request.getRemoteAddr());
     }

     filterChain.doFilter(request, response);
 }
}