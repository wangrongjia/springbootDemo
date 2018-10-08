package com.codinger.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfiguration {
    
    @Bean
    public FilterRegistrationBean<MyFilter> testFilterRegistration() {

        FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<MyFilter>();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }
    
}
