package com.laughter.laughter.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration  implements WebMvcConfigurer {
    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("laughter/**")
        .allowedOrigins("http://172.16.17.113:5500/")
        .allowedHeaders("Content-Type","Authorization")
        .allowedMethods("GET","PUT","DELETE","OPTIONS")
        .allowCredentials(true);
    }  
}
