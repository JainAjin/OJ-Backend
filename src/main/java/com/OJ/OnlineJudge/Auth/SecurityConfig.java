package com.OJ.OnlineJudge.Auth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        // You will need to inject your custom JWT filter.
        // Ensure you have a JwtAuthFilter class defined as a @Component.
        @Bean
        public JwtFilter jwtAuthFilter(){
            return new JwtFilter();
        }

        @Bean
        public UserDetailsService userDetailsService(){
            return new CustomUserDetailService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    // 1. Use the robust CORS configuration defined below
                    .cors(withDefaults())
                    // 2. Disable CSRF as it's not needed for stateless JWT APIs
                    .csrf(AbstractHttpConfigurer::disable)
                    // 3. Define authorization rules
                    .authorizeHttpRequests(auth -> auth
                            // Allow preflight OPTIONS requests for CORS
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            // Allow public access to all /api/auth/** endpoints (login, register)
                            .requestMatchers("/auth/**","/problem").permitAll()
                            // All other requests must be authenticated
                            .anyRequest().authenticated()
                    )
                    // 4. Set session management to STATELESS
                    // This is crucial for JWTs, as we don't want the server to create sessions.
                    // 5. Add the custom JWT filter before the standard username/password filter
                    .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        /**
         * This Bean provides the CORS configuration to the Spring Security filter chain.
         * It is the recommended way to handle CORS in a modern Spring Security application.
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    }
