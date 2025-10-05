package com.laughter.laughter.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
        .allowedOrigins("http://127.0.0.1:5500","http://192.168.88.246:5500/","http://172.16.17.113:5500/","http://192.168.159.1:5500")
        .allowedHeaders("*")
        .allowedMethods("GET","PUT","DELETE","OPTIONS")
        .allowCredentials(true);
    }  


}

