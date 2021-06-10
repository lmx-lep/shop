package com.fh.shop.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy//网管
public class ShopGateWayApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopGateWayApp.class,args);
    }

}
