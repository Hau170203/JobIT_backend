package com.example.learnJava.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        
        // Gửi kèm theo cookies hay không 
        config.setAllowCredentials(true);

        //  Cho phép các URL nào có thể kết nối với backendbackend
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4173", "http://localhost:5173"));

        // Các phần header đơcj phép gửi lênlên
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-no-retry")); 

        // Các phương thức được phép gửi lên
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 


        config.addExposedHeader("Authorization");

        // Thời gian pre-flight requet có thể cache (tính theo seconds)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
