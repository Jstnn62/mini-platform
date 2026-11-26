package com.example.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
class SecurityConfig {

  @Bean
  SecurityFilterChain api(HttpSecurity http) throws Exception {
    http
      .cors(Customizer.withDefaults())
      .headers(h -> h.contentSecurityPolicy(c -> c.policyDirectives(
          "default-src 'self'; frame-ancestors 'none'; object-src 'none'; base-uri 'self'")))
      .authorizeHttpRequests(a -> a
        .requestMatchers("/actuator/health/**").permitAll()
        .requestMatchers("/v1/**").hasAuthority("SCOPE_config.read")
        .anyRequest().authenticated())
      .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(List.of("https://app.test"));
    c.setAllowedMethods(List.of("GET", "PUT"));
    c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    c.setAllowCredentials(true);
    c.setMaxAge(600L);
    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/v1/**", c);
    return src;
  }
}
