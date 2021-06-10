package com.fh.shop.api.config;

import com.fh.shop.api.filter.CrossOriginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBean {

    @Bean
    public CrossOriginFilter crossOriginFilter(){
        return new CrossOriginFilter();
    }
}
