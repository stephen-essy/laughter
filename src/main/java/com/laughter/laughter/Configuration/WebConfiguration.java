package com.laughter.laughter.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration  implements WebMvcConfigurer {
    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
        .allowedOrigins("http://127.0.0.1:5500")
        .allowedHeaders("*")
        .allowedMethods("GET","PUT","DELETE","OPTIONS")
        .allowCredentials(true);
    }  
}

