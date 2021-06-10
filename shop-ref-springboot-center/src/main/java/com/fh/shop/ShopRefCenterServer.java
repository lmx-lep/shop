package com.fh.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ShopRefCenterServer {

    public static void main(String[] args) {
        SpringApplication.run(ShopRefCenterServer.class,args);
    }

}
